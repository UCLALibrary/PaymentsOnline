package edu.ucla.library.libservices.webservices.ecommerce.web.servlets;

import edu.ucla.library.libservices.invoicing.webservices.logging.beans.CashnetLog;
import edu.ucla.library.libservices.invoicing.webservices.logging.db.procs.AddCashnetLogProcedure;
import edu.ucla.library.libservices.webservices.ecommerce.web.clients.LoggingClient;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;

import javax.servlet.http.HttpSession;

import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

public class LoggingServlet
  extends HttpServlet
{
  private static final String CONTENT_TYPE =
    "text/html; charset=windows-1252";

  public void init( ServletConfig config )
    throws ServletException
  {
    super.init( config );
  }

  /**Process the HTTP doGet request.
   */
  public void doGet( HttpServletRequest request,
                     HttpServletResponse response )
    throws ServletException, IOException
  {
    doPost( request, response );
  }

  /**Process the HTTP doPost request.
   */
  public void doPost( HttpServletRequest request,
                      HttpServletResponse response )
    throws ServletException, IOException
  {
    Cookie[] cookies;
    response.setContentType( CONTENT_TYPE );

    CashnetLog data;
    Logger log;

    log = Logger.getLogger( LoggingServlet.class );

    log.info( "logging payment result for invoice " +
              request.getParameter( "UCLA_REF_NO" ) + "; result: " +
              ( request.getParameter( "result" ).equalsIgnoreCase( "0" ) ?
                "Paid ": "Failed " ) +
              ( !request.getParameter( "result" ).equalsIgnoreCase( "0" ) ?
                "Reason:{" + request.getParameter( "respmessage" ) + "}":
                "" ) );
    cookies = request.getCookies();
    if( cookies != null ) {
      for (Cookie cookie : cookies) {
          log.info("Cookie Name : " + cookie.getName( ) + ",  ");
          log.info("Cookie Value: " + cookie.getValue( )+" <br/>");
       }
    } else {
       log.info("<h2>No cookies founds</h2>");
    }
    HttpSession session = request.getSession();
    log.info( "alma user = " + session.getAttribute("almaUser") );
    
    if ( ( request.getRemoteAddr().equalsIgnoreCase( getServletContext().getInitParameter( "cashnet.ip.one" ) ) ) ||
         ( request.getRemoteAddr().equalsIgnoreCase( getServletContext().getInitParameter( "cashnet.ip.two" ) ) ) )
    {
      data = new CashnetLog();
      prepCashnetLog( request, data );
      logCashnetMessage( data, log );
      log.info( "<p>logged</p>" );

      if ( request.getParameter( "result" ).equalsIgnoreCase( "0" ) )
        request.getRequestDispatcher( "paymentservlet" ).forward( request,
                                                                  response );
    }
    else
    {
      log.info( "ecommerce.LoggingServlet: invalid logging/payment call from ".concat( request.getRemoteAddr() ) );
    }

  }

  private void prepCashnetLog( HttpServletRequest request,
                               CashnetLog data )
  {
    StringBuffer details;

    data.setBatchNumber( request.getParameter( "batchno" ) );
    data.setEffDate( request.getParameter( "effdate" ) );
    data.setPmtCode( request.getParameter( "pmtcode" ) );
    data.setRefNumber( request.getParameter( "UCLA_REF_NO" ) );
    data.setResultCode( request.getParameter( "result" ) );
    data.setTransNumber( ( request.getParameter( "result" ).equalsIgnoreCase( "0" ) ?
                           "S:".concat( request.getParameter( "tx" ) ):
                           "F:".concat( request.getParameter( "failedtx" ) ) ) );

    details = new StringBuffer();
    if ( !request.getParameter( "result" ).equalsIgnoreCase( "0" ) )
      details.append( "RM:{" ).append( request.getParameter( "respmessage" ) ).append( "}" );
    for ( int index = 1;
          index <= Integer.parseInt( request.getParameter( "itemcnt" ) );
          index++ )
      details.append( "GL:{" ).append( request.getParameter( "gl".concat( String.valueOf( index ) ) ) ).append( "}" );
    data.setDetails( ( details.toString().length() > 1000 ?
                       details.toString().substring( 0, 1000 ):
                       details.toString() ) );
  }

  private void logCashnetMessage( CashnetLog data, Logger log )
  {
    AddCashnetLogProcedure proc;

    proc = new AddCashnetLogProcedure();
    proc.setData( data );
    proc.setDbName( getServletContext().getInitParameter( "datasource.invoice" ) );
    proc.setUser( getServletContext().getInitParameter( "user.logging.cashnet" ) );
    try
    {
      proc.addLog();
    }
    catch ( Exception e )
    {
      log.info( "Log-entry creation failed: ".concat( e.getMessage() ) );
    }
    //LoggingClient client;

    //log("ecommerce.LoggingServlet: logCashnetMessage()");

    /*client = new LoggingClient();
    client.setCrypt( getServletContext().getInitParameter( "key.two" ) );
    client.setResourceURI( getServletContext().getInitParameter( "uri.logging" ) );
    client.setUriBase( getServletContext().getInitParameter( "uri.base" ) );
    client.setUser( getServletContext().getInitParameter( "key.one" ) );
    client.setData( data );

    log.info( client.doLogging() );*/
  }
}
