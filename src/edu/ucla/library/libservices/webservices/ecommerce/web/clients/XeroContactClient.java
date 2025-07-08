package edu.ucla.library.libservices.webservices.ecommerce.web.clients;

import com.google.gson.Gson;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroContact;

import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroContactList;

import edu.ucla.library.libservices.webservices.ecommerce.utility.handlers.PropertiesHandler;
import edu.ucla.library.libservices.webservices.ecommerce.utility.handlers.XeroTokenHandler;

import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Class that retrieves contact (customer/patron) data from Xero API
 */
public class XeroContactClient
{
  private static final Logger LOGGER = Logger.getLogger(XeroContactClient.class);
  // collection of values needed to access Xero API
  private Properties secrets;
  // path for properties file with URIs and IDs to access Xero API
  private String secretsFile;
  // path for file with OAuth tokens to access Xero API
  private String tokensFile;

  // unique ID for a contact
  private String userID;
  // XeroContact bean returned from class
  private XeroContact theContact;

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

  /**
   * @return String representation of OAuth token used to call Xero API
   */
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

  /**
   * Retrieves properties (contact URI etc), calls Xero service, maps json response to
   * XeroContact bean
   * @return A Xero contact modeled as a XeroContact bean
   */
  public XeroContact getTheContact()
  {
    loadProperties();
    if (this.theContact == null)
    {
      Client client;
      WebResource webResource;
      ClientResponse response;
      client = Client.create();
      webResource = client.resource(getContactURL().concat(getUserID()));

      String authString = "Bearer ".concat(getAccessToken());

      response = webResource.accept("application/json")
                            .header("Authorization", authString)
                            .header("xero-tenant-id", getTenantID())
                            .get(ClientResponse.class);
      if (response.getStatus() == 200)
      {
        XeroContactList theList;
        String json = response.getEntity(String.class);
        theList = new Gson().fromJson(json, XeroContactList.class);
        this.theContact = theList.getContacts().get(0);
      }
      else
      {
        LOGGER.error("contact service return code " + response.getStatus() + "\t" + response.getEntity(String.class));
        this.theContact = new XeroContact();
      }
    }
    return this.theContact;
  }

  private String getTenantID()
  {
    return secrets.getProperty("tenant_id");
  }

  private String getContactURL()
  {
    return secrets.getProperty("contact_url");
  }
}