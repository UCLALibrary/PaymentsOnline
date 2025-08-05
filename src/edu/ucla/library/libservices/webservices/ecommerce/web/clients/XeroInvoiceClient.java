package edu.ucla.library.libservices.webservices.ecommerce.web.clients;

import com.google.gson.Gson;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroInvoice;
import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroInvoiceList;
import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroLineItem;
import edu.ucla.library.libservices.webservices.ecommerce.utility.handlers.PropertiesHandler;
import edu.ucla.library.libservices.webservices.ecommerce.utility.handlers.XeroTokenHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import java.util.stream.Collectors;

import org.apache.log4j.Logger;

public class XeroInvoiceClient
{
  private static final Logger LOGGER = Logger.getLogger(XeroInvoiceClient.class);
  // collection of values needed to access Xero API
  private Properties secrets;
  // path for properties file with URIs and IDs to access Xero API
  private String secretsFile;
  // path for file with OAuth tokens to access Xero API
  private String tokensFile;

  private String contactID;
  private String invoiceID;

  private XeroInvoice singleInvoice;
  private XeroInvoiceList allUnpaid;

  public XeroInvoiceClient()
  {
    super();
  }

  /**
   * utility method to retrieve the properties needed by class
   */
  private void loadProperties()
  {
    // utility to retrieve properties
    PropertiesHandler secretGetter;
    secretGetter = new PropertiesHandler();
    secretGetter.setFileName(getSecretsFile());
    secrets = secretGetter.loadProperties();
  }

  /**
   * @return String representation of OAuth token used to call Xero API
   */
  private String getAccessToken()
  {
    //utility to retrieve current access/refresh tokens
    XeroTokenHandler tokenGetter;
    tokenGetter = new XeroTokenHandler();
    tokenGetter.setSecretsFile(getSecretsFile());
    tokenGetter.setTokensFile(getTokensFile());
    return tokenGetter.getTokens().getAccess_token();
  }

  private String getTenantID()
  {
    return secrets.getProperty("tenant_id");
  }

  private String getInvoiceURL()
  {
    return secrets.getProperty("invoice_url");
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

  public XeroInvoice getSingleInvoice()
  {
    Client client;
    ClientResponse response;
    String authString;
    WebResource webResource;

    loadProperties();
    client = Client.create();
    webResource = client.resource(getInvoiceURL().concat("/").concat(getInvoiceID()));
    authString = "Bearer ".concat(getAccessToken());
    response = webResource.accept("application/json")
                          .header("Authorization", authString)
                          .header("xero-tenant-id", getTenantID())
                          .get(ClientResponse.class);
    if (response.getStatus() == 200)
    {
      String json = response.getEntity(String.class);
      singleInvoice = new Gson().fromJson(json, XeroInvoiceList.class).getInvoices().get(0);
      for (XeroLineItem theLine : singleInvoice.getLineItems() )
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

  public XeroInvoiceList getAllUnpaid()
  {
    Client client;
    ClientResponse response;
    String authString;
    WebResource webResource;

    loadProperties();
    client = Client.create();
    webResource = client.resource(getInvoiceURL().concat("?Statuses=AUTHORISED&ContactIDs=")
                                                 .concat(getContactID()));
    authString = "Bearer ".concat(getAccessToken());
    response = webResource.accept("application/json")
                          .header("Authorization", authString)
                          .header("xero-tenant-id", getTenantID())
                          .get(ClientResponse.class);
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

  private String getItemCode(String accountID)
  {
    XeroAccountClient accountClient;
    accountClient = new XeroAccountClient();
    accountClient.setAccountID(accountID);
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
