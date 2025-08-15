package edu.ucla.library.libservices.webservices.ecommerce.web.clients;

import com.google.gson.Gson;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroInvoice;
import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroInvoiceList;
import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroLineItem;
import edu.ucla.library.libservices.webservices.ecommerce.constants.XeroConstants;

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
  private static final String UNPAID_QUERY = "?Statuses=AUTHORISED&ContactIDs=";

  private String contactID;
  private String invoiceID;

  private XeroInvoice singleInvoice;
  private XeroInvoiceList allUnpaid;

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
        theLine.setTransactItemCode(getItemCode(theLine.getAccountID()));
      }
      singleInvoice.setItemCodeAmts(groupdAndSumCodes(singleInvoice.getLineItems()));
    }
    else
    {
      LOGGER.error("invoice service return code " + response.getStatus() + "\t" + response.getEntity(String.class));
      singleInvoice = new XeroInvoice();
    }
    return singleInvoice;
  }

  /**
   * Retieves details on a particular invoice in PDF format
   * @return One Xero invoice as a PDF
   */
  public ClientResponse getInvoicePDF()
  {
    ClientResponse response;
    WebResource webResource;

    loadProperties();
    webResource = getWebResource(replacePort(buildSingleURL()));
    response = getResponse(webResource, XeroConstants.PDF_ACCEPT);
    return response;
  }

  /**
   * Calls Xero invoice API, requesting unpaid/partially paid invoices for a patron
   * @return A collection of unpaid invoices for a particular patron
   */
  public XeroInvoiceList getAllUnpaid()
  {
    ClientResponse response;
    WebResource webResource;

    loadProperties();
    webResource = getWebResource(replacePort(buildUnpaidURL()));
    response = getResponse(webResource, XeroConstants.JSON_ACCEPT);
    if (response.getStatus() == 200)
    {
      String json = response.getEntity(String.class);
      allUnpaid = new Gson().fromJson(json, XeroInvoiceList.class);
    }
    else
    {
      LOGGER.error("contact service return code " + response.getStatus() + "\t" + response.getEntity(String.class));
      allUnpaid = new XeroInvoiceList();
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

  private HashMap<String, Double> groupdAndSumCodes(ArrayList<XeroLineItem> theLines)
  {
    return (HashMap<String, Double>) theLines.stream()
           .collect(Collectors.groupingBy(XeroLineItem::getTransactItemCode,
                                          Collectors.summingDouble(XeroLineItem::getLineTotal)));
  }
}
