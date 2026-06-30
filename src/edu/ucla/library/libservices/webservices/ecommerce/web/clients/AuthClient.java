package edu.ucla.library.libservices.webservices.ecommerce.web.clients;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import edu.ucla.library.libservices.webservices.ecommerce.utility.handlers.PropertiesHandler;

import java.util.Properties;

public class AuthClient
{
  private static final String ALMA_KEY = "alma.key";

  private Client client;
  private String userID;
  private String password;
  private String uriBase;
  private String key;
  private WebResource webResource;
  // collection of values needed to access Alma API
  private Properties almaSecrets;
  // path for properties file with URIs and IDs to access Alma API
  private String secretsFile;

  public AuthClient()
  {
    super();
    client = null;
    webResource = null;
    userID = null;
    password = null;
  }

  public void setUserID(String userID)
  {
    this.userID = userID;
  }

  private String getUserID()
  {
    return userID;
  }

  public void setPassword(String password)
  {
    this.password = password;
  }

  private String getPassword()
  {
    return password;
  }

  public void setUriBase(String uriBase)
  {
    this.uriBase = uriBase;
  }

  private String getUriBase()
  {
    return uriBase;
  }

  public void setSecretsFile(String secretsFile)
  {
    this.secretsFile = secretsFile;
  }

  public String getSecretsFile()
  {
    return secretsFile;
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
    almaSecrets = secretGetter.loadProperties();
  }

  public void setKey(String key)
  {
    this.key = key;
  }

  private String getKey()
  {
    return key;
  }

  public boolean isValidPatron()
  {
    ClientResponse response;

	loadProperties();
    client = Client.create();
    webResource = client.resource(getUriBase().concat(getUserID())
                                              .concat("??op=auth&apikey=")
                                              .concat(almaSecrets.getProperty(ALMA_KEY)));
    response = webResource.header("Exl-User-Pw", getPassword())
                          .post(ClientResponse.class);
    if (response.getStatus() == 204)
    {
      return true;
    }
    else
    {
      //System.out.println("status code = " + response.getStatus());
      return false;
    }
  }
}
