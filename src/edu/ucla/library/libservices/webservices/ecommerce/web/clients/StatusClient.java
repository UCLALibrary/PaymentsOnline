package edu.ucla.library.libservices.webservices.ecommerce.web.clients;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import edu.ucla.library.libservices.webservices.ecommerce.utility.signatures.SignatureBuilder;

public class StatusClient
{
  private Client client;
  private WebResource webResource;
  private String invoiceNumber;
  private String resourceURI;
  private String uriBase;
  private String user;
  private String crypt;
  private String userName;

  public StatusClient()
  {
    super();
  }

  public void setInvoiceNumber( String invoiceNumber )
  {
    this.invoiceNumber = invoiceNumber;
  }

  private String getInvoiceNumber()
  {
    return invoiceNumber;
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

  public void setUserName( String userName )
  {
    this.userName = userName;
  }

  private String getUserName()
  {
    return userName;
  }

  private String makeAuthorization( String request )
  {
    return SignatureBuilder.computeAuth( SignatureBuilder.buildSimpleSignature( "PUT",
                                                                                request ),
                                         getUser(), getCrypt() );
  }

  public int updateStatus()
  {
    ClientResponse response;
    client = Client.create();
    webResource =
        client.resource( getUriBase().concat( getResourceURI() ).concat( "/invoice/" +
                                                                         getInvoiceNumber() +
                                                                         "/status/Paid/whoby/" +
                                                                         getUserName() ) );
    response =
        webResource.header( "Authorization", makeAuthorization( getResourceURI() ).concat( "/invoice/" +
                                                                                           getInvoiceNumber() +
                                                                                           "/status/Paid/whoby/" +
                                                                                           getUserName() ) ).put( ClientResponse.class,
                                                                                                                  "" );
    return response.getClientResponseStatus().getStatusCode();
  }
}
