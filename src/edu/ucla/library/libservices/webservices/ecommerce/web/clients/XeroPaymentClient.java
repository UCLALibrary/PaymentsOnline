package edu.ucla.library.libservices.webservices.ecommerce.web.clients;

import com.sun.jersey.api.client.ClientResponse;

import com.sun.jersey.api.client.WebResource;

import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroInvoice;
import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroLineItem;
import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroPayment;
import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroPaymentAccount;
import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroPaymentInvoice;
import edu.ucla.library.libservices.webservices.ecommerce.constants.XeroConstants;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.log4j.Logger;

public class XeroPaymentClient
  extends AbstractXeroClient
{
  private static final Logger LOGGER = Logger.getLogger(XeroInvoiceClient.class);

  private String invoiceID;
  private String reference;

  public XeroPaymentClient()
  {
    super();
    port = 0;
  }

  public void setInvoiceID(String invoiceID)
  {
    this.invoiceID = invoiceID;
  }

  public String getInvoiceID()
  {
    return invoiceID;
  }

  public void setReference(String reference)
  {
    this.reference = reference;
  }

  public String getReference()
  {
    return reference;
  }

  private String getPaymentURL()
  {
    return secrets.getProperty(XeroConstants.PAYMENT_URL);
  }

  private XeroInvoice getInvoice()
  {
    XeroInvoiceClient theClient;

    theClient = new XeroInvoiceClient();
    theClient.setInvoiceID(getInvoiceID());
    theClient.setPort(getPort());
    theClient.setSecretsFile(getSecretsFile());
    theClient.setTokensFile(getTokensFile());

    return theClient.getSingleInvoice();
  }

  private XeroPayment buildPayment(XeroLineItem theLine)
  {
    XeroPayment thePayment;
    XeroPaymentInvoice invoice;
    XeroPaymentAccount account;

    invoice = new XeroPaymentInvoice();
    invoice.setInvoiceID(getInvoiceID());
    account = new XeroPaymentAccount();
    account.setAccountID(theLine.getAccountID());

    thePayment = new XeroPayment();
    thePayment.setAccount(account);
    thePayment.setAmount(theLine.getLineTotal() / 100D);
    thePayment.setDate(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    thePayment.setInvoice(invoice);
    thePayment.setReference(getReference());
    return thePayment;
  }

  public ClientResponse putPayment()
  {
    ClientResponse response;
    WebResource webResource;

    loadProperties();
    webResource = getWebResource(replacePort(getPaymentURL()));
    response = webResource.accept(XeroConstants.JSON_ACCEPT)
                          .header(XeroConstants.AUTH_HEADER, getAuthString())
                          .header(XeroConstants.TENANT_HEADER, getTenantID())
                          .put(null);
    if (response.getStatus() != 200)
    {
      LOGGER.error("contact service return code " + response.getStatus() + "\t" + response.getEntity(String.class));
    }
    return response;
  }
}
