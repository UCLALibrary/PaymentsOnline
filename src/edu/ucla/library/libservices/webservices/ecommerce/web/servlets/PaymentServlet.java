package edu.ucla.library.libservices.webservices.ecommerce.web.servlets;

import edu.ucla.library.libservices.invoicing.utiltiy.db.DataSourceFactory;
import edu.ucla.library.libservices.invoicing.webservices.payments.db.procs.ApplyFullPaymentProcedure;
import edu.ucla.library.libservices.webservices.ecommerce.utility.db.DataHandler;
//import edu.ucla.library.libservices.webservices.ecommerce.utility.sftp.SftpClient;
import edu.ucla.library.libservices.webservices.ecommerce.utility.strings.StringHandler;
//import edu.ucla.library.libservices.webservices.ecommerce.utility.writer.UploadWriter;
//import edu.ucla.library.libservices.webservices.ecommerce.web.clients.PaymentClient;

import edu.ucla.library.libservices.webservices.ecommerce.web.clients.AlmaClient;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.*;

import javax.sql.DataSource;

//import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import org.springframework.jdbc.core.JdbcTemplate;

public class PaymentServlet
  extends HttpServlet
{
  @SuppressWarnings("compatibility:-306225955054470783")
  private static final long serialVersionUID = -5089517672925799118L;
  private static final String CONTENT_TYPE = "text/html; charset=windows-1252";
  //private static final String COUNT_QUERY =
    //"SELECT count(aeon_request_id) FROM invoice_aeon_request_vw WHERE invoice_number = ?";
  //private static final String ID_QUERY = "SELECT aeon_request_id FROM invoice_aeon_request_vw WHERE invoice_number = ?";
  private static final String UPDATE_QUERY =
    "update fine_fee_transactions set trans_note = to_char(sysdate, 'MM/DD/YY')" +
    " || ' Paid in LibBill               ' || substr(trans_note, 40) where " +
    "trans_note like '%Invoiced in LibBill           ' || ? || '%'";

  public void init(ServletConfig config)
    throws ServletException
  {
    super.init(config);
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    doPost(request, response);
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    response.setContentType(CONTENT_TYPE);
    Logger log;
    log = Logger.getLogger(LoggingServlet.class);

    //log.info( "ecommerce.PaymentServlet: doPost()" );

    handlePayment(request, log);
    /*if (request.getParameter("UCLA_REF_NO").startsWith("CS"))
    {
      updateVoyager(request, log);
    }*/
    //else if (!request.getParameter("UCLA_REF_NO").startsWith("alma"))
    //{
      //checkAeon(request, log);
    //}
  }

  private void handlePayment(HttpServletRequest request, Logger log)
  {
    if (request.getParameter("UCLA_REF_NO").startsWith("alma"))
    {
      doAlmaPayment(request, log);
    }
    else
    {
      log.info("going to libbill payments");
      doLibBillPayment(request, log);
    }
  }

  /*
   * check if invoice number linked to aeon request
   * if so, write payment file and upload to sftp
   */

  /*private void checkAeon(HttpServletRequest request, Logger log)
  {
    String invoice;
    DataSource ds;

    invoice = request.getParameter("UCLA_REF_NO");
    ds = DataSourceFactory.createDataSource(getServletContext().getInitParameter("datasource.invoice"));
    if (new JdbcTemplate(ds).queryForInt(COUNT_QUERY, new Object[] { invoice }) > 0)
    {
      int aeonID;

      aeonID = new JdbcTemplate(ds).queryForInt(ID_QUERY, new Object[] { invoice });
      writeUpload(invoice, aeonID);
      uploadFile(aeonID);
    }
  }

  private void writeUpload(String invoice, int aeon)
  {
    UploadWriter writer;

    writer = new UploadWriter();
    writer.setAeonID(aeon);
    writer.setInvoiceNumber(invoice);
    writer.setDirectory(getServletContext().getRealPath(getServletContext().getInitParameter("upload.local")));

    writer.writeFile();
  }

  private void uploadFile(int aeonID)
  {
    SftpClient sftp;

    sftp = new SftpClient();
    sftp.setFileName(String.valueOf(aeonID).concat(".txt"));
    sftp.setHost(getServletContext().getInitParameter("sftp.host"));
    sftp.setKeyfilePath(getServletContext().getRealPath(getServletContext().getInitParameter("keyfile.path")));
    sftp.setLocalDir(getServletContext().getRealPath(getServletContext().getInitParameter("upload.local")));
    sftp.setRemoteDir(getServletContext().getInitParameter("upload.remotel"));
    sftp.setUser(getServletContext().getInitParameter("sftp.user"));

    sftp.getSftpConnect();
    sftp.uploadFile();
  }*/

  private void updateVoyager(HttpServletRequest request, Logger log)
  {
    int updates;
    DataSource ds;

    updates = 0;
    ds = DataSourceFactory.createDataSource(getServletContext().getInitParameter("datasource.ucladb"));

    updates = new JdbcTemplate(ds).update(UPDATE_QUERY, new Object[] { request.getParameter("UCLA_REF_NO") });
    if (updates != 0)
      log.info("Voyager-LibBill update: no matches found for invoice " + request.getParameter("UCLA_REF_NO"));
  }

  private void doAlmaPayment(HttpServletRequest request, Logger log)
  {
    AlmaClient payClient;
    String invoiceNo;
    int responseCode;
    
    invoiceNo = StringHandler.extractInvoiceID(request.getParameter("UCLA_REF_NO"));
    log.info("working with invoice " + invoiceNo);
    
    payClient = new AlmaClient();
    //log.info("payment amount " + getPaymentAmount(request) );
    payClient.setAmount(String.valueOf(getPaymentAmount(request))); //.getParameter("amount0"));
    payClient.setFineID(invoiceNo);
    payClient.setKey(getServletContext().getInitParameter("alma.key"));
    payClient.setPayMethod(request.getParameter("pmtcode").equalsIgnoreCase("CC")? "CREDIT_CARD": "ONLINE");
    log.info("transaction number " + request.getParameter("tx") );
    payClient.setTransNo(request.getParameter("tx"));
    payClient.setUriBase(getServletContext().getInitParameter("alma.base.fees"));
    ///log.info("calling getUser with invoice number " + invoiceNo);
    payClient.setUserID(getUser(invoiceNo, log));
    responseCode = payClient.postPayment();
    log.info("payment POST response = " + responseCode);
    //clearRecord(invoiceNo);
  }

  private void doLibBillPayment(HttpServletRequest request, Logger log)
  {
    ApplyFullPaymentProcedure proc;

    proc = new ApplyFullPaymentProcedure();
    proc.setDbName(getServletContext().getInitParameter("datasource.invoice"));
    proc.setUserName(getServletContext().getInitParameter("user.logging.cashnet"));
    proc.setInvoiceNumber(request.getParameter("UCLA_REF_NO"));
    proc.setPaymentType(request.getParameter("pmtcode").equalsIgnoreCase("CC")? 3: 2);
    try
    {
      proc.addPayment();
    }
    catch (Exception e)
    {
      log.fatal("Payment failed: ".concat(e.getMessage()));
    }
 }

  private String getUser(String fine, Logger log)
  {
    log.info("in getUser with invoice number " + fine);
    DataHandler handler = new DataHandler();
    handler.setDbName(getServletContext().getInitParameter("datasource.ucladb"));
    handler.setInvoiceID(fine);
    //log.info("returning user" + handler.getPatronData());
    return handler.getPatronData();
  }

  private double getPaymentAmount(HttpServletRequest request)
  {
    double total = 0.0d;
    for ( int index = 1;
          index <= Integer.parseInt( request.getParameter( "itemcnt" ) );
          index++ )
    {
      double amount = Double.parseDouble( request.getParameter( "amount".concat( String.valueOf( index )) ) );
      total += amount;
    }
    
    return total;
  }
  /*private void clearRecord(String fine)
  {
    DataHandler handler = new DataHandler();
    handler.setDbName(getServletContext().getInitParameter("datasource.ucladb"));
    handler.setInvoiceID(fine);
    handler.deleteInvoiceData();
  }*/
}
