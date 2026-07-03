package edu.ucla.library.libservices.webservices.ecommerce.web.servlets;

import edu.ucla.library.libservices.invoicing.webservices.logging.beans.CashnetLog;
import edu.ucla.library.libservices.invoicing.webservices.logging.db.procs.AddCashnetLogProcedure;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    response.setContentType( CONTENT_TYPE );

    CashnetLog data;
    Logger log;

    log = LoggerFactory.getLogger( LoggingServlet.class );

    log.info( "payment result for invoice " +
              request.getParameter( "UCLA_REF_NO" ) + "; result: " +
              ( request.getParameter( "result" ).equalsIgnoreCase( "0" ) ?
                "Paid ": "Failed " ) +
              ( !request.getParameter( "result" ).equalsIgnoreCase( "0" ) ?
                "Reason:{" + request.getParameter( "respmessage" ) + "}":
                "" ) );

    if ( ( request.getRemoteAddr().equalsIgnoreCase( getServletContext().getInitParameter( "cashnet.ip.one" ) ) ) ||
         ( request.getRemoteAddr().equalsIgnoreCase( getServletContext().getInitParameter( "cashnet.ip.two" ) ) ) ||
         ( request.getRemoteAddr().equalsIgnoreCase( getServletContext().getInitParameter( "cashnet.ip.three" ) ) ) )
    {
      log.info(prepCashnetLog( request ));
      /*data = new CashnetLog();
      logCashnetMessage( data, log );
      log.info( "<p>logged</p>" );*/

      //log.info();

      if ( request.getParameter( "result" ).equalsIgnoreCase( "0" ) )
        request.getRequestDispatcher( "paymentservlet" ).forward( request,
                                                                  response );
    }
    else
    {
      log.info( "ecommerce.LoggingServlet: invalid logging/payment call from ".concat( request.getRemoteAddr() ) );
    }

  }

  private String prepCashnetLog( HttpServletRequest request )
  {
    StringBuffer details;
    String transNo;
    details = new StringBuffer();

    details.append( "batchno: " + request.getParameter( "batchno" ) );
    details.append( " effdate: " + request.getParameter( "effdate" ) );
    details.append( " pmtcode: " + request.getParameter( "pmtcode" ) );
    details.append( " invoice: " + request.getParameter( "UCLA_REF_NO" ) );
    if ( request.getParameter( "result" ).equalsIgnoreCase( "0" ) )
    {
	  transNo = "S:".concat( request.getParameter( "tx" ) );
	}
	else
	{
	  transNo = "F:".concat( request.getParameter( "failedtx" ) );
	}
    details.append( transNo );

    if ( !request.getParameter( "result" ).equalsIgnoreCase( "0" ) )
      details.append( " RM:{" ).append( request.getParameter( "respmessage" ) ).append( "}" );
    for ( int index = 1;
          index <= Integer.parseInt( request.getParameter( "itemcnt" ) );
          index++ )
      details.append( " GL:{" ).append( request.getParameter( "gl".concat( String.valueOf( index ) ) ) ).append( "}" );
    return details.toString().length() > 1000 ?
                       details.toString().substring( 0, 1000 ):
                       details.toString();
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
  }
}
