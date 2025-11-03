package edu.ucla.library.libservices.webservices.ecommerce.web.clients;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import edu.ucla.library.libservices.webservices.ecommerce.utility.signatures.SignatureBuilder;


public class PaymentClient
{
  private Client client;
  private WebResource webResource;
  private String invoiceNumber;
  private int paymentTypeID;
  private String resourceURI;
  private String uriBase;
  private String user;
  private String crypt;

  public PaymentClient()
  {
    super();
    client = null;
    webResource = null;
  }

  public void setInvoiceNumber( String invoiceNumber )
  {
    this.invoiceNumber = invoiceNumber;
  }

  private String getInvoiceNumber()
  {
    return invoiceNumber;
  }

  public void setPaymentTypeID( int paymentTypeID )
  {
    this.paymentTypeID = paymentTypeID;
  }

  private int getPaymentTypeID()
  {
    return paymentTypeID;
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

  private String makeAuthorization( String request )
  {
    return SignatureBuilder.computeAuth( SignatureBuilder.buildSimpleSignature( "PUT",
                                                                                request ),
                                         getUser(), getCrypt() );
  }

  public int doPayment()
  {
    StringBuffer uriWithParams;

    uriWithParams = new StringBuffer( getResourceURI() );
    uriWithParams.append( "/invoice/" ).append( getInvoiceNumber() ).append( "/type/" ).append( getPaymentTypeID() );
    
    //System.out.println( "resource = " + getUriBase().concat( uriWithParams.toString() ) );
    ClientResponse response;
    client = Client.create();
    webResource =
        client.resource( getUriBase().concat( uriWithParams.toString() ) );
    response =
        webResource.header( "Authorization", makeAuthorization( uriWithParams.toString() ) ).type( "text/xml" ).put( ClientResponse.class,
                                                                                                                     null );
    return response.getClientResponseStatus().getStatusCode();
  }

}
  //import java.util.ArrayList;
  //import java.util.List;
  //private double amount;
  //private String userName;
  /*public void setAmount( double amount )
  {
    this.amount = amount;
  }

  private double getAmount()
  {
    return amount;
  }*/
  /*private List<String> buildContentList()
  {
    List<String> content;

    content = new ArrayList<String>( 6 );
    content.add( "<payment>" );
    content.add( "<invoiceNumber>" + getInvoiceNumber() +
                 "</invoiceNumber>" );
    content.add( "<amount>" + getAmount() + "</amount>" );
    content.add( "<createdBy>" + getUser() + "</createdBy>" );
    content.add( "<paymentTypeID>" + getPaymentTypeID() +
                 "</paymentTypeID>" );
    content.add( "</payment>" );
    return content;
  }

  private String buildContentString()
  {
    StringBuffer content;

    content = new StringBuffer(6);
    content.append( "<payment>" );
    content.append( "<invoiceNumber>" + getInvoiceNumber() +
                 "</invoiceNumber>" );
    content.append( "<amount>" + getAmount() + "</amount>" );
    content.append( "<createdBy>" + getUserName() + "</createdBy>" );
    content.append( "<paymentTypeID>" + getPaymentTypeID() +
                 "</paymentTypeID>" );
    content.append( "</payment>" );
    return content.toString();
  }*/
  /*public void setUserName( String userName )
  {
    this.userName = userName;
  }

  private String getUserName()
  {
    return userName;
  }*/
