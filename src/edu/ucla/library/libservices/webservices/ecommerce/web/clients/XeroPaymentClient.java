package edu.ucla.library.libservices.webservices.ecommerce.web.clients;

import com.google.gson.Gson;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroInvoice;
import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroPayment;
import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroPaymentAccount;
import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroPaymentInvoice;
import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroPaymentList;
import edu.ucla.library.libservices.webservices.ecommerce.constants.XeroConstants;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.log4j.Logger;

public class XeroPaymentClient
  extends AbstractXeroClient
{
  private static final Logger LOGGER = Logger.getLogger(XeroInvoiceClient.class);

  private String invoiceNumber;
  private String reference;

  public XeroPaymentClient()
  {
    super();
    port = 0;
  }

  public void setInvoiceNumber(String invoiceID)
  {
    this.invoiceNumber = invoiceID;
  }

  public String getInvoiceNumber()
  {
    return invoiceNumber;
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
    theClient.setInvoiceID(getInvoiceNumber());
    theClient.setPort(getPort());
    theClient.setSecretsFile(getSecretsFile());
    theClient.setTokensFile(getTokensFile());

    return theClient.getSingleInvoice();
  }

  private XeroPayment buildPayment(String accountID, String invoiceID, Double amount)
  {
    XeroPayment thePayment;
    XeroPaymentInvoice invoice;
    XeroPaymentAccount account;

    invoice = new XeroPaymentInvoice();
    invoice.setInvoiceID(invoiceID);
    account = new XeroPaymentAccount();
    account.setAccountID(accountID);

    thePayment = new XeroPayment();
    thePayment.setAccount(account);
    thePayment.setAmount(amount / 100D);
    thePayment.setDate(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    thePayment.setInvoice(invoice);
    thePayment.setReference(getReference());
    return thePayment;
  }

  public ClientResponse putPayment()
  {
    ClientResponse response;
    WebResource webResource;
    XeroInvoice toBePaid;
    XeroPaymentList payments;

    payments = new XeroPaymentList();
    loadProperties();
    toBePaid = getInvoice();
    for ( String accountID : toBePaid.getAccountAmts().keySet() )
    {
      payments.getPayments().add(buildPayment(accountID, toBePaid.getInvoiceID(), toBePaid.getAccountAmts().get(accountID)));
    }
    webResource = getWebResource(replacePort(getPaymentURL()));
    response = webResource.accept(XeroConstants.JSON_ACCEPT)
                          .header(XeroConstants.AUTH_HEADER, getAuthString())
                          .header(XeroConstants.TENANT_HEADER, getTenantID())
                          .put(ClientResponse.class, new Gson().toJson( payments ));
    if (response.getStatus() != 200)
    {
      LOGGER.error("payment service return code " + response.getStatus() + "\t" + response.getEntity(String.class));
    }
    return response;
  }
}
