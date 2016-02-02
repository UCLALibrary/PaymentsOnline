package edu.ucla.library.libservices.webservices.ecommerce.web.clients;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import edu.ucla.library.libservices.webservices.ecommerce.utility.signatures.SignatureBuilder;
import edu.ucla.library.libservices.invoicing.webservices.payments.beans.ReceiptInfo;

public class ReceiptClient
{
  private ReceiptInfo theReceipt;
  private Client client;
  private WebResource webResource;
  private String resourceURI;
  private String uriBase;
  private String user;
  private String crypt;
  private String invoiceNumber;

  public ReceiptClient()
  {
    super();
  }

  public ReceiptInfo getTheReceipt()
  {
    client = Client.create();
    //webResource = client.resource("https://webservices.library.ucla.edu/invoicing-dev/patrons/by_uid/".concat( getUserID() ));
    webResource =
        client.resource( getUriBase().concat( getResourceURI() ).concat( getInvoiceNumber() ) );
    theReceipt =
        webResource.header( "Authorization", makeAuthorization( getResourceURI().concat( getInvoiceNumber() ) ) ).get( ReceiptInfo.class );

    return theReceipt;
  }

  private String makeAuthorization( String request )
  {
    return SignatureBuilder.computeAuth( SignatureBuilder.buildSimpleSignature( "GET",
                                                                                request ),
                                         getUser(), getCrypt() );
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

  public void setInvoiceNumber( String invoiceNumber )
  {
    this.invoiceNumber = invoiceNumber;
  }

  private String getInvoiceNumber()
  {
    return invoiceNumber;
  }
}
