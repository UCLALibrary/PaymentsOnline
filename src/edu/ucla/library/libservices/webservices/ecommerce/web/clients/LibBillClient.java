package edu.ucla.library.libservices.webservices.ecommerce.web.clients;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import edu.ucla.library.libservices.invoicing.webservices.patrons.beans.SimplePatron;

import edu.ucla.library.libservices.webservices.ecommerce.utility.signatures.SignatureBuilder;

public class LibBillClient
{

  private SimplePatron thePatron;
  private Client client;
  private WebResource webResource;
  private String userID;
  private String resourceURI;
  private String uriBase;
  private String user;
  private String crypt;

  public LibBillClient()
  {
    super();
    thePatron = null;
    client = null;
    webResource = null;
    userID = null;
  }

  public SimplePatron getThePatron()
  {
    if ( thePatron == null )
    {
      client = Client.create();
      webResource =
          client.resource( getUriBase().concat( getResourceURI() ).concat( getUserID() ) );
      thePatron =
          webResource.header( "Authorization",
                              makeAuthorization( getResourceURI().concat( getUserID() ) ) ).get( SimplePatron.class );
    }
    return thePatron;
  }

  private String getUserID()
  {
    return userID;
  }

  private String makeAuthorization(String request)
  {
    return SignatureBuilder.computeAuth(
      SignatureBuilder.buildSimpleSignature( "GET", request ),
      getUser(),
      getCrypt() );
  }

  public void setResourceURI( String resourceURI )
  {
    this.resourceURI = resourceURI;
  }

  private String getResourceURI()
  {
    return resourceURI;
  }

  public void setUriBase( String uriBase )
  {
    this.uriBase = uriBase;
  }

  private String getUriBase()
  {
    return uriBase;
  }

  public void setUser( String user )
  {
    this.user = user;
  }

  private String getUser()
  {
    return user;
  }

  public void setCrypt( String crypt )
  {
    this.crypt = crypt;
  }

  private String getCrypt()
  {
    return crypt;
  }

  public void setUserID( String userID )
  {
    this.userID = userID;
  }
}
