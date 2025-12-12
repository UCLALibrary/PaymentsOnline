package edu.ucla.library.libservices.webservices.ecommerce.web.servlets;

import edu.ucla.library.libservices.invoicing.utiltiy.db.DataSourceFactory;
import edu.ucla.library.libservices.invoicing.webservices.payments.db.procs.ApplyFullPaymentProcedure;
import edu.ucla.library.libservices.webservices.ecommerce.utility.db.DataHandler;
//import edu.ucla.library.libservices.webservices.ecommerce.utility.sftp.SftpClient;
import edu.ucla.library.libservices.webservices.ecommerce.utility.strings.StringHandler;
//import edu.ucla.library.libservices.webservices.ecommerce.utility.writer.UploadWriter;
//import edu.ucla.library.libservices.webservices.ecommerce.web.clients.PaymentClient;

import edu.ucla.library.libservices.webservices.ecommerce.web.clients.AlmaClient;

import edu.ucla.library.libservices.webservices.ecommerce.web.clients.XeroPaymentClient;

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

    handlePayment(request, log);
  }

  /**
   * Record a payment in Alma/LibBill/Xero
   * @param request HTTP request, holding parameters defining the payment
   * @param log logger passed to subsidiary methods
   */
  private void handlePayment(HttpServletRequest request, Logger log)
  {
    if (request.getParameter("UCLA_REF_NO").startsWith("alma"))
    {
      doAlmaPayment(request, log);
    }
    else if (request.getParameter("UCLA_REF_NO").contains("-"))
    {
      doXeroPayment(request, log);
    }
    else
    {
      doLibBillPayment(request, log);
    }
  }

  private void doAlmaPayment(HttpServletRequest request, Logger log)
  {
    AlmaClient payClient;
    String invoiceNo;
    int responseCode;
    
    invoiceNo = StringHandler.extractInvoiceID(request.getParameter("UCLA_REF_NO"));
    log.info("working with invoice " + invoiceNo);
    
    payClient = new AlmaClient();
    payClient.setAmount(String.valueOf(getPaymentAmount(request))); //.getParameter("amount0"));
    payClient.setFineID(invoiceNo);
    payClient.setKey(getServletContext().getInitParameter("alma.key"));
    payClient.setPayMethod(request.getParameter("pmtcode").equalsIgnoreCase("CC")? "CREDIT_CARD": "ONLINE");
    log.info("transaction number " + request.getParameter("tx") );
    payClient.setTransNo(request.getParameter("tx"));
    payClient.setUriBase(getServletContext().getInitParameter("alma.base.fees"));
    payClient.setUserID(getUser(invoiceNo, log));
    responseCode = payClient.postPayment();
    log.info("payment POST response = " + responseCode);
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

  private void doXeroPayment(HttpServletRequest request, Logger log)
  {
    XeroPaymentClient theClient;
    theClient = new XeroPaymentClient();
    theClient.setInvoiceNumber(request.getParameter("UCLA_REF_NO").replace("~fromxero", ""));
    theClient.setPort(0);
    theClient.setReference(buildReference(request));
    theClient.setSecretsFile(getServletContext().getInitParameter("xero.secrets"));
    theClient.setTokensFile(getServletContext().getInitParameter("xero.tokens"));
    try
    {
      theClient.putPayment();
    }
    catch (Exception e)
    {
      log.fatal("Payment failed: ".concat(e.getMessage()));
    }
  }

  /**
   * Method used to populate the "reference" field in a Xero payment
   * record, spcifying the source/method of the payment and the
   * transaction number from Transact/Cashnet for audit purposes
   * @param request HTTP request, holding parameters used to build a string
   * @return Free-text string passed to Xero
   */
  private String buildReference(HttpServletRequest request)
  {
    StringBuffer buffer;
    buffer = new StringBuffer("payment from LPO; ");
    buffer.append("payment method: " + request.getParameter( "pmtcode" ) + "; ");
    buffer.append("Transact transaction #: " + request.getParameter( "tx" ));
    return buffer.toString();
  }
}
