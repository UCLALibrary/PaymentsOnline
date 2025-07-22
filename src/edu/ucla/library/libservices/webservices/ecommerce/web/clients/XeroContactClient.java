package edu.ucla.library.libservices.webservices.ecommerce.web.clients;

import com.google.gson.Gson;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroContact;

import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroContactList;

import org.apache.log4j.Logger;

public class XeroContactClient
{
  private static final Logger LOGGER = Logger.getLogger(XeroContactClient.class);

  private Client client;
  private WebResource webResource;
  private String accessToken;
  private String resourceURI;
  private String tenantID;
  private String uriBase;
  private String userID;
  private XeroContact theContact;

  public XeroContactClient()
  {
    super();
  }

  public void setAccessToken(String accessToken)
  {
    this.accessToken = accessToken;
  }

  public String getAccessToken()
  {
    return accessToken;
  }

  public void setResourceURI(String resourceURI)
  {
    this.resourceURI = resourceURI;
  }

  public String getResourceURI()
  {
    return resourceURI;
  }

  public void setTenantID(String tenantID)
  {
    this.tenantID = tenantID;
  }

  public String getTenantID()
  {
    return tenantID;
  }

  public void setUriBase(String uriBase)
  {
    this.uriBase = uriBase;
  }

  public String getUriBase()
  {
    return uriBase;
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
      ClientResponse response;
      client = Client.create();
      webResource = client.resource(getUriBase().concat(getUserID()));
      
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
        theContact = new XeroContact();
      }
    }
    return theContact;
  }
}
