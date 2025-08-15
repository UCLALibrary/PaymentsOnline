package edu.ucla.library.libservices.webservices.ecommerce.web.clients;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.representation.Form;

import edu.ucla.library.libservices.webservices.ecommerce.constants.XeroConstants;

import java.util.Base64;

import org.apache.log4j.Logger;

/**
 * Client used to retrieve fresh access/refresh OAuth tokens from Xero token service
 */
public class XeroTokenClient extends AbstractXeroClient
{
  private static final Logger LOGGER = Logger.getLogger(XeroTokenClient.class);
  //OAuth token used to access Xero tokens service
  private String refreshToken;

  public XeroTokenClient()
  {
    super();
    port = 0;
  }

  public void setRefreshToken(String refreshToken)
  {
    this.refreshToken = refreshToken;
  }

  public String getRefreshToken()
  {
    return refreshToken;
  }

  /**
   * Builds the authorization header value passes to Xero tokens service
   * @return authorization string
   */
  private String buildAuthString()
  {
    String start = "Basic ";
    String clientID = secrets.getProperty(XeroConstants.CLIENT_ID);
    String clientSecret = secrets.getProperty(XeroConstants.CLIENT_SECRET);
    StringBuffer buffer = new StringBuffer(clientID).append(":").append(clientSecret);
    String encoded = Base64.getEncoder().encodeToString(buffer.toString().getBytes());
    String authString = start.concat(encoded);
    return authString;
  }

  /**
   * Builds authroization header and request body, then calls 
   * Xero token service
   * @return JSON representation of fresh OAuth tokens from Xero
   */
  public String getTokens()
  {
    ClientResponse response;
    String authString;
    String json;
    WebResource webResource;

    loadProperties();
    authString = buildAuthString();

    webResource = getWebResource(replacePort(secrets.getProperty(XeroConstants.TOKEN_URL)));

    Form form = new Form();
    form.add("grant_type", "refresh_token");
    form.add("refresh_token", getRefreshToken());

    response = webResource.accept(XeroConstants.JSON_ACCEPT)
                          .header(XeroConstants.AUTH_HEADER, authString)
                          .entity(form, XeroConstants.FORM_ENCODE)
                          .post(ClientResponse.class);
    if (response.getStatus() == 200)
    {
      json = response.getEntity(String.class);
    }
    else
    {
      LOGGER.error("token service return code " + response.getStatus() + "\t" + response.getEntity(String.class));
      json = null;
    }
    return json;
  }
}
