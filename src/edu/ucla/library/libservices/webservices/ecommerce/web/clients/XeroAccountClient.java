package edu.ucla.library.libservices.webservices.ecommerce.web.clients;

import com.google.gson.Gson;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroAccountList;
import edu.ucla.library.libservices.webservices.ecommerce.utility.handlers.PropertiesHandler;
import edu.ucla.library.libservices.webservices.ecommerce.utility.handlers.XeroTokenHandler;

import java.util.Properties;

import org.apache.log4j.Logger;

public class XeroAccountClient
{
  private static final Logger LOGGER = Logger.getLogger(XeroAccountClient.class);
  // collection of values needed to access Xero API
  private Properties secrets;
  // path for properties file with URIs and IDs to access Xero API
  private String secretsFile;
  // path for file with OAuth tokens to access Xero API
  private String tokensFile;

  // unique ID for a contact
  private String accountID;
  // XeroContact bean returned from class
  private String itemCode;
  
  private int port;

  public XeroAccountClient()
  {
    super();
    port = 0;
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

  public void setAccountID(String accountID)
  {
    this.accountID = accountID;
  }

  public String getAccountID()
  {
    return accountID;
  }

  public void setPort(int port)
  {
    this.port = port;
  }

  public int getPort()
  {
    return port;
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

  private String getAccountURL()
  {
    return secrets.getProperty("account_url");
  }

  public String getItemCode()
  {
    Client client;
    ClientResponse response;
    String authString;
    WebResource webResource;

    loadProperties();
    client = Client.create();
    webResource = client.resource(buildURL(getAccountID()));

    authString = "Bearer ".concat(getAccessToken());
    response = webResource.accept("application/json")
                          .header("Authorization", authString)
                          .header("xero-tenant-id", getTenantID())
                          .get(ClientResponse.class);
    if (response.getStatus() == 200)
    {
      XeroAccountList theList;
      String json = response.getEntity(String.class);
      theList = new Gson().fromJson(json, XeroAccountList.class);
      itemCode = theList.getAccounts()
                        .get(0)
                        .getName();
    }
    else
    {
      LOGGER.error("contact service return code " + response.getStatus() + "\t" + response.getEntity(String.class));
      itemCode = new String();
    }

    return itemCode;
  }

  
  private String buildURL(String queryOrID)
  {
    String url;
    url = getAccountURL();
    if ( getPort() != 0 )
    {
      url = url.replace("{port}}", ":".concat(String.valueOf(getPort())));
    }
    return url.concat(queryOrID);
  }
}
