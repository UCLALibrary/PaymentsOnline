package edu.ucla.library.libservices.webservices.ecommerce.web.clients;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import edu.ucla.library.libservices.invoicing.webservices.invoices.beans.CashNetInvoice;

import edu.ucla.library.libservices.webservices.ecommerce.utility.signatures.SignatureBuilder;

public class CashNetClient
{
  private CashNetInvoice theInvoice;
  private Client client;
  private WebResource webResource;
  private String invoiceID;
  private String resourceURI;
  private String uriBase;
  private String user;
  private String crypt;

  public CashNetClient()
  {
    super();
    theInvoice = null;
    client = null;
    webResource = null;
    invoiceID = null;
  }
  
  public void setInvoiceID( String userID )
  {
    this.invoiceID = userID;
  }

  private String getInvoiceID()
  {
    return invoiceID;
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

  public CashNetInvoice getTheInvoice()
  {
    if ( theInvoice == null )
    {
      client = Client.create();
      //webResource = client.resource("https://webservices.library.ucla.edu/invoicing-dev/patrons/by_uid/".concat( getUserID() ));
      webResource =
          client.resource( getUriBase().concat( getResourceURI() ).concat( getInvoiceID() ) );
      theInvoice =
          webResource.header( "Authorization",
                              makeAuthorization( getResourceURI().concat( getInvoiceID() ) ) )
        .get( CashNetInvoice.class );
    }
    return theInvoice;
  }

  private String makeAuthorization(String request)
  {
    return SignatureBuilder.computeAuth(
      SignatureBuilder.buildSimpleSignature( "GET", request ), 
      getUser(), 
      getCrypt() );
  }
}
