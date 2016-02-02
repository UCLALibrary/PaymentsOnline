package edu.ucla.library.libservices.webservices.ecommerce.web.clients;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import edu.ucla.library.libservices.invoicing.webservices.logging.beans.CashnetLog;
import edu.ucla.library.libservices.webservices.ecommerce.utility.signatures.SignatureBuilder;

import java.util.ArrayList;
import java.util.List;

public class LoggingClient
{
  private Client client;
  private WebResource webResource;
  private CashnetLog data;
  private String resourceURI;
  private String uriBase;
  private String user;
  private String crypt;

  public LoggingClient()
  {
    super();
    client = null;
    webResource = null;
  }

  public void setData( CashnetLog data )
  {
    this.data = data;
  }

  private CashnetLog getData()
  {
    return data;
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
    return SignatureBuilder.computeAuth( SignatureBuilder.buildComplexSignature( "PUT",
                                                                                 request,
                                                                                 buildContentList() ),
                                         getUser(), getCrypt() );
  }

  private List<String> buildContentList()
  {
    List<String> content;

    content = new ArrayList<String>( 9 );
    content.add( "<cnLog>" );
    content.add( "<refNumber>" + getData().getRefNumber() +
                 "</refNumber>" );
    content.add( "<resultCode>" + getData().getResultCode() +
                 "</resultCode>" );
    content.add( "<transNumber>" + getData().getTransNumber() +
                 "</transNumber>" );
    content.add( "<batchNumber>" + getData().getBatchNumber() +
                 "</batchNumber>" );
    content.add( "<pmtCode>" + getData().getPmtCode() + "</pmtCode>" );
    content.add( "<effDate>" + getData().getEffDate() + "</effDate>" );
    content.add( "<details>" + getData().getDetails() + "</details>" );
    content.add( "</cnLog>" );

    return content;
  }

  private String buildContentString()
  {
    StringBuffer content;

    content = new StringBuffer( 6 );
    content.append( "<cnLog>\n" );
    content.append( "<refNumber>" + getData().getRefNumber() +
                    "</refNumber>\n" );
    content.append( "<resultCode>" + getData().getResultCode() +
                    "</resultCode>\n" );
    content.append( "<transNumber>" + getData().getTransNumber() +
                    "</transNumber>\n" );
    content.append( "<batchNumber>" + getData().getBatchNumber() +
                    "</batchNumber>\n" );
    content.append( "<pmtCode>" + getData().getPmtCode() +
                    "</pmtCode>\n" );
    content.append( "<effDate>" + getData().getEffDate() +
                    "</effDate>\n" );
    content.append( "<details>" + getData().getDetails() +
                    "</details>\n" );
    content.append( "</cnLog>" );
    return content.toString();
  }

  public String doLogging()
  {
    ClientResponse response;
    client = Client.create();
    webResource =
        client.resource( getUriBase().concat( getResourceURI() ) );
    response =
        webResource.header( "Authorization", makeAuthorization( getResourceURI() ) ).type( "application/xml" ).put( ClientResponse.class,
                                                                                                                    buildContentString() );
    return String.valueOf( response.getClientResponseStatus().getStatusCode() ).concat( ":" ).concat( response.getClientResponseStatus().getReasonPhrase() );
  }
}
