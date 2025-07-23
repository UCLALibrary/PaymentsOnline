package edu.ucla.library.libservices.webservices.ecommerce.web.clients;

import com.google.gson.Gson;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroContact;

import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroContactList;

import edu.ucla.library.libservices.webservices.ecommerce.utility.handlers.PropertiesHandler;
import edu.ucla.library.libservices.webservices.ecommerce.utility.handlers.XeroTokenHandler;

import org.apache.log4j.Logger;

public class XeroContactClient
{
  private static final Logger LOGGER = Logger.getLogger(XeroContactClient.class);
  //path for properties file with URIs and IDs to access Xero API
  private String secretsFile;
  //path for file with OAuth tokens to access Xero API
  private String tokensFile;

  private String userID;
  private XeroContact theContact;

  public XeroContactClient()
  {
    super();
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

  public String getAccessToken()
  {
    //utility to retrieve current access/refresh tokens
    XeroTokenHandler tokenGetter;
    tokenGetter = new XeroTokenHandler();
    tokenGetter.setSecretsFile(getSecretsFile());
    tokenGetter.setTokensFile(getTokensFile());
    return tokenGetter.getTokens().getAccess_token();
  }

  public void setUserID(String userID)
  {
    this.userID = userID;
  }

  public String getUserID()
  {
    return userID;
  }

  public XeroContact getTheContact()
  {
    if (theContact == null)
    {
      Client client;
      WebResource webResource;
      ClientResponse response;
      client = Client.create();
      webResource = client.resource(getContactURL().concat(getUserID()));
      
      String authString = "Bearer ".concat(getAccessToken());

      response = webResource.accept("application/json").header("Authorization", authString).
                header("xero-tenant-id", getTenantID()).get(ClientResponse.class);
      if (response.getStatus() == 200)
      {
        XeroContactList theList;
        String json = response.getEntity(String.class);
        theList = new Gson().fromJson(json, XeroContactList.class);
        theContact = theList.getContacts().get(0);
      }
      else
      {
        LOGGER.error("contact service return code " + response.getStatus());
        theContact = new XeroContact();
      }
    }
    return theContact;
  }
  
  private String getTenantID()
  {
    PropertiesHandler secretGetter;
    secretGetter = new PropertiesHandler();
    secretGetter.setFileName(getSecretsFile());
    return secretGetter.loadProperties().getProperty("tenant_id");
  }

  private String getContactURL()
  {
    PropertiesHandler secretGetter;
    secretGetter = new PropertiesHandler();
    secretGetter.setFileName(getSecretsFile());
    return secretGetter.loadProperties().getProperty("contact_url");
  }
}
