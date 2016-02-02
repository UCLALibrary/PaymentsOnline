package edu.ucla.library.libservices.webservices.ecommerce.web.clients;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import javax.ws.rs.core.Response;

import edu.ucla.library.libservices.webservices.ecommerce.utility.signatures.SignatureBuilder;

public class PdfClient
{
  private Client client;
  private WebResource webResource;
  private String resourceURI;
  private String uriBase;
  private String user;
  private String crypt;
  private String invoice;
  private Response pdf;

  public PdfClient()
  {
    super();
    client = null;
    webResource = null;
    invoice = null;
    pdf = null;
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

  public void setInvoice( String invoice )
  {
    this.invoice = invoice;
  }

  private String getInvoice()
  {
    return invoice;
  }

  public Response getPdf()
  {
    client = Client.create();
    //webResource = client.resource("https://webservices.library.ucla.edu/invoicing-dev/patrons/by_uid/".concat( getUserID() ));
    webResource =
      client.resource( getUriBase().concat( getResourceURI() ).concat( getInvoice() ) );
    pdf =
      webResource.header( "Authorization", 
              makeAuthorization( getResourceURI().concat( getInvoice() ) ) ).get( Response.class );
    return pdf;
  }
  
  private String makeAuthorization(String request)
  {
    return SignatureBuilder.computeAuth(
      SignatureBuilder.buildSimpleSignature( "GET", request.concat( getInvoice() ) ), 
      getUser(), 
      getCrypt() );
  }
}
