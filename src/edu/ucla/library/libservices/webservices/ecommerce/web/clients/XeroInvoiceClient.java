package edu.ucla.library.libservices.webservices.ecommerce.web.clients;

import com.google.gson.Gson;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroInvoice;
import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroInvoiceList;
import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroLineItem;
import edu.ucla.library.libservices.webservices.ecommerce.constants.XeroConstants;

import java.io.InputStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

/**
 * Calls Xero invoice API and retrieves either singular invoice
 * or all unpaid invoices
 */
public class XeroInvoiceClient
  extends AbstractXeroClient
{
  private static final Logger LOGGER = Logger.getLogger(XeroInvoiceClient.class);
  private static final String UNPAID_QUERY = "?Statuses=AUTHORISED&ContactIds=";

  private String contactID;
  private String invoiceID;

  private XeroInvoice singleInvoice;
  private ArrayList<XeroInvoice> allUnpaid;

  public XeroInvoiceClient()
  {
    super();
    port = 0;
  }

  public void setContactID(String contactID)
  {
    this.contactID = contactID;
  }

  public String getContactID()
  {
    return contactID;
  }

  public void setInvoiceID(String invoiceID)
  {
    this.invoiceID = invoiceID;
  }

  public String getInvoiceID()
  {
    return invoiceID;
  }

  private String getInvoiceURL()
  {
    return secrets.getProperty(XeroConstants.INVOICE_URL);
  }

  private String buildSingleURL()
  {
    return getInvoiceURL().concat("/").concat(getInvoiceID());
  }

  private String buildUnpaidURL()
  {
    return getInvoiceURL().concat(UNPAID_QUERY).concat(getContactID());
  }

  /**
   * Retieves details on a particular invoice
   * Calls XeroAccountClient to retrieve Transact item codes per line item
   * Groups and sums line-item amounts per item code
   * @return One Xero invoice mapped to XeroInvoice object
   */
  public XeroInvoice getSingleInvoice()
    throws Exception
  {
    ClientResponse response;
    WebResource webResource;

    loadProperties();
    webResource = getWebResource(replacePort(buildSingleURL()));
    response = getResponse(webResource, XeroConstants.JSON_ACCEPT);
    if (response.getStatus() == 200)
    {
      String json = response.getEntity(String.class);
      singleInvoice = new Gson().fromJson(json, XeroInvoiceList.class)
                                .getInvoices()
                                .get(0);
      for (XeroLineItem theLine: singleInvoice.getLineItems())
      {
        if ( theLine.getAccountID() != null && theLine.getAccountID().length() != 0 && theLine.getLineAmount() > 0 )
        {
          theLine.setTransactItemCode(getItemCode(theLine.getAccountID()));
        }
        if ((theLine.getAccountID() == null || theLine.getAccountID().length() == 0) && theLine.getLineAmount() == 0 )
        {
          /*
           * This is a line item that lacks both account info (needed to retrieve Transact itemCode) and an amount
           * Log the occurance of empty line item for possible best-practices review/record keeping
           * Ignore line for Transact submission because there's no amount to be payed
           */
          LOGGER.error("line item for invoice " + singleInvoice.getInvoiceNumber() + " for " 
                       + theLine.getDescription() + " lacks account ID and amount");
        }
        if ((theLine.getAccountID() == null || theLine.getAccountID().length() == 0) && theLine.getLineAmount() > 0 )
        {
          throw new Exception(buildExceptionMessage(singleInvoice, theLine));
        }
      }
      singleInvoice.setItemCodeAmts(groupAndSumCodes(singleInvoice.getLineItems()));
      singleInvoice.setAccountAmts(groupAndSumAccounts(singleInvoice.getLineItems()));
    }
    else
    {
      LOGGER.error("invoice service return code " + response.getStatus() + " on URL " + buildSingleURL() + "\t" + response.getEntity(String.class));
      singleInvoice = new XeroInvoice();
    }
    LOGGER.info("returning invoice " + singleInvoice.toString());
    return singleInvoice;
  }

  /**
   * Retieves details on a particular invoice in PDF format
   * @return One Xero invoice as a PDF
   */
  public InputStream getInvoicePDF()
  {
    ClientResponse response;
    WebResource webResource;

    loadProperties();
    System.out.println(buildSingleURL());
    webResource = getWebResource(replacePort(buildSingleURL()));
    response = getResponse(webResource, XeroConstants.PDF_ACCEPT);
    //testing
    return response.getEntityInputStream();
  }

  /**
   * Calls Xero invoice API, requesting unpaid/partially paid invoices for a patron
   * @return A collection of unpaid invoices for a particular patron
   */
  public ArrayList<XeroInvoice> getAllUnpaid()
  {
    ClientResponse response;
    WebResource webResource;
      XeroInvoiceList unpaid;

    loadProperties();
    //LOGGER.info("calling Xero invoice service wih URL " + buildUnpaidURL());
    if ( getContactID() == null || getContactID().equals("") )
      return new ArrayList<XeroInvoice>();;

    webResource = getWebResource(replacePort(buildUnpaidURL()));
    response = getResponse(webResource, XeroConstants.JSON_ACCEPT);
    if (response.getStatus() == 200)
    {
      String json = response.getEntity(String.class);
      unpaid = new Gson().fromJson(json, XeroInvoiceList.class);
      allUnpaid = unpaid.getInvoices();
    }
    else
    {
      LOGGER.error("invoice service return code " + response.getStatus() + " on request URL " + buildUnpaidURL()  + "\t" + response.getEntity(String.class));
      allUnpaid = new ArrayList<XeroInvoice>();
    }
    return allUnpaid;
  }

  /**
   * @param accountID Account ID from line item
   * @return Transact item code for account
   */
  private String getItemCode(String accountID)
  {
    XeroAccountClient accountClient;
    accountClient = new XeroAccountClient();
    accountClient.setAccountID(accountID);
    accountClient.setPort(getPort());
    accountClient.setSecretsFile(getSecretsFile());
    accountClient.setTokensFile(getTokensFile());

    return accountClient.getItemCode();
  }

  private HashMap<String, Double> groupAndSumCodes(ArrayList<XeroLineItem> theLines)
  {
    return (HashMap<String, Double>) theLines.stream()
           .collect(Collectors.groupingBy(XeroLineItem::getTransactItemCode,
                                          Collectors.summingDouble(XeroLineItem::getLineTotal)));
  }

  private HashMap<String, Double> groupAndSumAccounts(ArrayList<XeroLineItem> theLines)
  {
    return (HashMap<String, Double>) theLines.stream()
           .collect(Collectors.groupingBy(XeroLineItem::getAccountID,
                                          Collectors.summingDouble(XeroLineItem::getLineTotal)));
  }

  private String buildExceptionMessage(XeroInvoice singleInvoice, XeroLineItem theLine)
  {
    StringBuffer buffer;
    buffer = new StringBuffer();
    buffer.append("Invoice ").append(singleInvoice.getInvoiceNumber()).append(" has an invalid line item. ");
    buffer.append("Please contact the issuing Library unit with this information: ");
    buffer.append("The charge for ").append(theLine.getDescription()).append(" lacks an account number/item code.");
    return buffer.toString();
  }
}
