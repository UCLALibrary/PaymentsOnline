package edu.ucla.library.libservices.webservices.ecommerce.web.servlets;

import edu.ucla.library.libservices.webservices.ecommerce.utility.sftp.SftpClient;
import edu.ucla.library.libservices.webservices.ecommerce.utility.writer.UploadWriter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.*;
import javax.servlet.http.*;

public class TestServlet
  extends HttpServlet
{
  private static final String CONTENT_TYPE =
    "text/html; charset=windows-1252";

  public void init( ServletConfig config )
    throws ServletException
  {
    super.init( config );
  }

  public void doGet( HttpServletRequest request,
                     HttpServletResponse response )
    throws ServletException, IOException
  {
    response.setContentType( CONTENT_TYPE );
    PrintWriter out = response.getWriter();
    out.println( "<html>" );
    out.println( "<head><title>TestServlet</title></head>" );
    out.println( "<body>" );
    out.println( "<p>The servlet has received a GET. This is the reply.</p>" );
    out.println( "<p>Will attempt to write file to " +
                 getServletContext().getRealPath( getServletContext().getInitParameter( "upload.local" ) ) );
    writeUpload( "SC000123", 1234 );
    out.println( "<p>Attepting upload</p>" );
    uploadFile( 1234 );
    out.println( "</body></html>" );
    out.close();
  }

  private void writeUpload( String invoice, int aeon )
  {
    UploadWriter writer;

    writer = new UploadWriter();
    writer.setAeonID( aeon );
    writer.setInvoiceNumber( invoice );
    writer.setDirectory( getServletContext().getRealPath( getServletContext().getInitParameter( "upload.local" ) ) );

    writer.writeFile();
  }

  private void uploadFile( int aeonID )
  {
    SftpClient sftp;

    sftp = new SftpClient();
    sftp.setFileName( String.valueOf( aeonID ).concat( ".txt" ) );
    sftp.setHost( getServletContext().getInitParameter( "sftp.host" ) );
    sftp.setKeyfilePath( getServletContext().getRealPath( getServletContext().getInitParameter( "keyfile.path" ) ) );
    sftp.setLocalDir( getServletContext().getRealPath( getServletContext().getInitParameter( "upload.local" ) ) );
    sftp.setRemoteDir( getServletContext().getInitParameter( "upload.remote" ) );
    sftp.setUser( getServletContext().getInitParameter( "sftp.user" ) );
    
    sftp.getSftpConnect();
    sftp.uploadFile();
  }
}
