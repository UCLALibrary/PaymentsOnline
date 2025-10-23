package edu.ucla.library.libservices.webservices.ecommerce.web.clients;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import edu.ucla.library.libservices.invoicing.utiltiy.testing.ContentTests;
import edu.ucla.library.libservices.webservices.ecommerce.utility.signatures.SignatureBuilder;
import edu.ucla.library.libservices.invoicing.webservices.payments.beans.ReceiptInfo;
import edu.ucla.library.libservices.webservices.ecommerce.beans.AlmaUser;
import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroContact;
import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroInvoice;
import edu.ucla.library.libservices.webservices.ecommerce.utility.db.DataHandler;
import edu.ucla.library.libservices.webservices.ecommerce.utility.strings.StringHandler;
//import org.apache.log4j.Logger;

public class ReceiptClient
{
  //private static final Logger LOGGER = Logger.getLogger( ReceiptClient.class );
  private ReceiptInfo theReceipt;
  private String resourceURI;
  private String vgerName;
  private String libBillName;
  private String apiKey;
  private String uriBase;
  private String almaUriBase;
  private String user;
  private String crypt;
  private String invoiceNumber;
  private String secretsFile;
  private String tokensFile;

  public ReceiptClient()
  {
    super();
  }

  public ReceiptInfo getTheReceipt()
  {
    if (theReceipt == null)
    {
      if (getInvoiceNumber().startsWith("alma"))
      {
        theReceipt = buildAlmaReceipt();
      }
      else if (getInvoiceNumber().contains("-"))
      {
        theReceipt = buildXeroReceipt();
      }
      else
      {
        theReceipt = buildLibBillReceipt();
      }
    }
    return theReceipt;
  }

  private String makeAuthorization(String request)
  {
    return SignatureBuilder.computeAuth(SignatureBuilder.buildSimpleSignature("GET", request), getUser(), getCrypt());
  }

  public void setResourceURI(String resourceURI)
  {
    this.resourceURI = resourceURI;
  }

  private String getResourceURI()
  {
    return resourceURI;
  }

  public void setUriBase(String uriBase)
  {
    this.uriBase = uriBase;
  }

  private String getUriBase()
  {
    return uriBase;
  }

  public void setUser(String user)
  {
    this.user = user;
  }

  private String getUser()
  {
    return user;
  }

  public void setCrypt(String crypt)
  {
    this.crypt = crypt;
  }

  private String getCrypt()
  {
    return crypt;
  }

  public void setInvoiceNumber(String invoiceNumber)
  {
    this.invoiceNumber = invoiceNumber;
  }

  private String getInvoiceNumber()
  {
    return invoiceNumber;
  }

  public void setVgerName(String vgerName)
  {
    this.vgerName = vgerName;
  }

  private String getVgerName()
  {
    return vgerName;
  }

  public void setLibBillName(String libBillName)
  {
    this.libBillName = libBillName;
  }

  private String getLibBillName()
  {
    return libBillName;
  }

  public void setApiKey(String apiKey)
  {
    this.apiKey = apiKey;
  }

  private String getApiKey()
  {
    return apiKey;
  }

  public void setAlmaUriBase(String almaUriBase)
  {
    this.almaUriBase = almaUriBase;
  }

  private String getAlmaUriBase()
  {
    return almaUriBase;
  }

  public void setSecretsFile(String secretsFile)
  {
    this.secretsFile = secretsFile;
  }

  public String getSecretsFile()
  {
    return secretsFile;
  }

  public void setTokensFile(String tokensFile)
  {
    this.tokensFile = tokensFile;
  }

  public String getTokensFile()
  {
    return tokensFile;
  }

  private ReceiptInfo buildAlmaReceipt()
  {
    AlmaClient client;
    AlmaUser user;
    DataHandler handler;
    ReceiptInfo almaReceipt;
    String patronID;
    String cleanInvoiceNo;

    cleanInvoiceNo = StringHandler.extractInvoiceID(getInvoiceNumber());

    handler = new DataHandler();
    handler.setDbName(getVgerName());
    handler.setInvoiceID(cleanInvoiceNo);

    //LOGGER.info("calling DataHandler.getPatronData");
    patronID = handler.getPatronData();
    handler.setPatronID(patronID);

    client = new AlmaClient();
    prepAlmaClient(client, patronID, cleanInvoiceNo);
    user = client.getThePatron();

    almaReceipt = new ReceiptInfo();
    almaReceipt.setUid(patronID);
    almaReceipt.setStatus(client.getTheInvoice().getStatus());
    almaReceipt.setUserName(user.getFirstName() + " " + user.getLastName());
    handler.setDbName(getLibBillName());
    almaReceipt.setUnpaid(client.getTheFees()
                                .getFees()
                                .size() + handler.getUnpaidCount());

    return almaReceipt;
  }

  private ReceiptInfo buildLibBillReceipt()
  {
    AlmaClient almaClient;
    Client client;
    ReceiptInfo libBillReceipt;
    WebResource webResource;
    ClientResponse response;

    client = Client.create();
    //webResource = client.resource("https://webservices.library.ucla.edu/invoicing-dev/patrons/by_uid/".concat( getUserID() ));
    webResource = client.resource(getUriBase().concat(getResourceURI()).concat(getInvoiceNumber()));
    response = webResource.header("Authorization", makeAuthorization(getResourceURI().concat(getInvoiceNumber())))
                          .type("application/json")
                          .get(ClientResponse.class);
    if (response.getStatus() == 200)
    {
      libBillReceipt = response.getEntity(ReceiptInfo.class);
    }
    else
    {
      libBillReceipt = new ReceiptInfo();
    }
    //libBillReceipt =
    //webResource.header("Authorization", makeAuthorization(getResourceURI().concat(getInvoiceNumber())))
    //.get(ReceiptInfo.class);

    almaClient = new AlmaClient();
    //check that invoice number is not empty
    if (!ContentTests.isEmpty(getInvoiceNumber()))
    {
      prepAlmaClient(almaClient, String.valueOf(libBillReceipt.getPatronID()), getInvoiceNumber());
      libBillReceipt.setUnpaid(libBillReceipt.getUnpaid() + almaClient.getTheFees().getRecordCount());
    }

    return libBillReceipt;
  }

  private void prepAlmaClient(AlmaClient theClient, String patronID, String invoice)
  {
    theClient.setDbName(getVgerName());
    theClient.setFineID(invoice);
    theClient.setKey(getApiKey());
    theClient.setResourceURI("/fees?status=ACTIVE&apikey=");
    theClient.setUriBase(getAlmaUriBase());
    theClient.setUserID(patronID);
  }

  private ReceiptInfo buildXeroReceipt()
  {
    ReceiptInfo xeroReceipt;
    String cleanInvoiceNo;
    XeroContact thePatron;
    XeroContactClient patronClient;
    XeroInvoiceClient invoiceClient;
    XeroInvoice theInvoice;

    cleanInvoiceNo = StringHandler.extractInvoiceID(getInvoiceNumber());
    invoiceClient = new XeroInvoiceClient();
    invoiceClient.setInvoiceID(cleanInvoiceNo);
    invoiceClient.setSecretsFile(getSecretsFile());
    invoiceClient.setTokensFile(getTokensFile());
    theInvoice = invoiceClient.getSingleInvoice();

    patronClient = new XeroContactClient();
    patronClient.setSecretsFile(getSecretsFile());
    patronClient.setTokensFile(getTokensFile());
    patronClient.setUserID(theInvoice.getContact().getAccountNumber());
    thePatron = patronClient.getTheContact();

    xeroReceipt = new ReceiptInfo();
    xeroReceipt.setUid(thePatron.getContactID());
    xeroReceipt.setStatus(theInvoice.getStatus());
    xeroReceipt.setUserName(thePatron.getFirstName() + " " + thePatron.getLastName());

    invoiceClient.setContactID(thePatron.getContactID());

    xeroReceipt.setUnpaid(invoiceClient.getAllUnpaid().size());

    return xeroReceipt;
  }
}
