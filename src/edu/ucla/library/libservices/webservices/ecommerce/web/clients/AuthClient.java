package edu.ucla.library.libservices.webservices.ecommerce.web.clients;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class AuthClient
{
  private Client client;
  private String userID;
  private String password;
  private String uriBase;
  private String key;
  private WebResource webResource;

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
    client = Client.create();
    webResource = client.resource(getUriBase().concat(getUserID())
                                              .concat("??op=auth&amp;apikey=")
                                              .concat(getKey()));
    System.out.println(webResource.getURI().toString());
    response = webResource.header("Exl-User-Pw", getPassword())
                          .header("Content-Type", "application/x-www-form-urlencoded")
                          //.accept("application/json")
                          .post(ClientResponse.class);
    if (response.getStatus() == 204)
    {
      return true;
    }
    else
    {
      System.out.println("status code = " + response.getStatus());
      return false;
    }
  }
}
