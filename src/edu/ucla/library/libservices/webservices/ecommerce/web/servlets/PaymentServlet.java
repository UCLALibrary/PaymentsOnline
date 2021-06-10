package edu.ucla.library.libservices.webservices.ecommerce.web.servlets;

import edu.ucla.library.libservices.invoicing.utiltiy.db.DataSourceFactory;
import edu.ucla.library.libservices.invoicing.webservices.payments.db.procs.ApplyFullPaymentProcedure;
import edu.ucla.library.libservices.webservices.ecommerce.utility.sftp.SftpClient;
import edu.ucla.library.libservices.webservices.ecommerce.utility.writer.UploadWriter;
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
  private static final String CONTENT_TYPE = "text/html; charset=windows-1252";
  private static final String COUNT_QUERY =
    "SELECT count(aeon_request_id) FROM invoice_aeon_request_vw WHERE invoice_number = ?";
  private static final String ID_QUERY = "SELECT aeon_request_id FROM invoice_aeon_request_vw WHERE invoice_number = ?";
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
    if (request.getParameter("UCLA_REF_NO").startsWith("CS"))
    {
      updateVoyager(request, log);
    }
    else
      checkAeon(request, log);
  }

  private void handlePayment(HttpServletRequest request, Logger log)
  {
    if (request.getParameter("UCLA_REF_NO").startsWith("ALMA"))
    {
      doAlmaPayment(request, log);
    }
    else
    {
      doLibBillPayment(request, log);
    }
  }

  /*
   * check if invoice number linked to aeon request
   * if so, write payment file and upload to sftp
   */

  private void checkAeon(HttpServletRequest request, Logger log)
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
  }

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
    payClient = new AlmaClient();
    payClient.setAmount(request.getParameter("amount0"));
    payClient.setFineID(request.getParameter("UCLA_REF_NO"));
    payClient.setKey(getServletContext().getInitParameter("alma.key"));
    payClient.setPayMethod(request.getParameter("pmtcode").equalsIgnoreCase("CC")? "CREDIT_CARD": "ONLINE");
    payClient.setTransNo(request.getParameter("Tx"));
    payClient.setUriBase(getServletContext().getInitParameter("alma.base.fees"));
    payClient.setUserID(getUser(request.getParameter("UCLA_REF_NO")));
    payClient.postPayment();
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

  private String getUser(String fine)
  {
    /*
     * using the fine ID, look up patron ID
     * delete row from table
     */
    return null;
  }
}
