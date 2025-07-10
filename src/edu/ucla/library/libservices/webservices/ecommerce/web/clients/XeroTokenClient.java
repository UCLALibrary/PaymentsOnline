package edu.ucla.library.libservices.webservices.ecommerce.web.clients;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.representation.Form;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.Base64;
import java.util.Properties;

import org.apache.log4j.Logger;

public class XeroTokenClient
{
  private static final Logger LOGGER = Logger.getLogger(XeroTokenClient.class);
  private Properties secrets;
  private String refreshToken;
  private String secretsFile;

  public XeroTokenClient()
  {
    super();
  }

  private void loadProperties()
  {
    secrets = new Properties();
    try
    {
      secrets.load(new FileInputStream(new File(getSecretsFile())));
    }
    catch (IOException ioe)
    {
      LOGGER.fatal("problem with props file" + ioe.getMessage());
    }
  }

  private String buildAuthString()
  {
    String start = "Basic ";
    String clientID = secrets.getProperty("client_id");
    String clientSecret = secrets.getProperty("client_secret");
    StringBuffer buffer = new StringBuffer(clientID).append(":").append(clientSecret);
    String encoded = Base64.getEncoder().encodeToString(buffer.toString().getBytes());
    String authString = start.concat(encoded);
    return authString;
  }

  public String getTokens()
  {
    Client client;
    ClientResponse response;
    String authString;
    String json;
    WebResource webResource;

    loadProperties();
    authString = buildAuthString();

    client = Client.create();
    webResource = client.resource(secrets.getProperty("token_url"));

    Form form = new Form();
    form.add("grant_type", "refresh_token");
    form.add("refresh_token", getRefreshToken());

    response = webResource.accept("application/json")
                          .header("Authorization", authString)
                          .entity(form, "application/x-www-form-urlencoded")
                          .post(ClientResponse.class);
    if (response.getStatus() == 200)
    {
      json = response.getEntity(String.class);
    }
    else
    {
      LOGGER.error("token service return code " + response.getStatus());
      json = null;
    }
    return json;
  }

  public void setSecretsFile(String secretsFile)
  {
    this.secretsFile = secretsFile;
  }

  public String getSecretsFile()
  {
    return secretsFile;
  }

  public void setRefreshToken(String refreshToken)
  {
    this.refreshToken = refreshToken;
  }

  public String getRefreshToken()
  {
    return refreshToken;
  }
}
