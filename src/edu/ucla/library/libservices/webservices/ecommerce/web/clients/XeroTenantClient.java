package edu.ucla.library.libservices.webservices.ecommerce.web.clients;

import com.google.gson.Gson;

import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroTenantID;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.representation.Form;

import org.apache.log4j.Logger;

/**
 * Client used to retrieve tenant ID from Xero
 * We expect to have only one, unchanging tenant ID... this class is for
 * cases when said tenant ID is rejected 
 */
public class XeroTenantClient
{
  private static final Logger LOGGER = Logger.getLogger(XeroTenantClient.class);
  private String accessToken;
  private String tenantURL;

  public void setAccessToken(String accessToken)
  {
    this.accessToken = accessToken;
  }

  public String getAccessToken()
  {
    return accessToken;
  }

  public void setTenantURL(String tenantURL)
  {
    this.tenantURL = tenantURL;
  }

  public String getTenantURL()
  {
    return tenantURL;
  }

  /**
   * Connects to Xero API to retrieve tenant ID
   * @return String representation of a Xero tenant ID
   */
  public String getTenantID()
  {
    Client client;
    ClientResponse response;
    String authString;
    String json;
    WebResource webResource;
    Gson gson;
    XeroTenantID[] tenantBeans;

    gson = new Gson();
    authString = "Bearer ".concat(getAccessToken());

    client = Client.create();
    webResource = client.resource(getTenantURL());
    response = webResource.accept("application/json")
                          .header("Authorization", authString)
                          .get(ClientResponse.class);

    if (response.getStatus() == 200)
    {
      json = response.getEntity(String.class);
      tenantBeans = gson.fromJson(json, XeroTenantID[].class);
      return tenantBeans[0].getTenantId();
    }
    else
    {
      LOGGER.error("tenant service return code " + response.getStatus() + "\t" + response.getEntity(String.class));
      return null;
    }
  }
}
