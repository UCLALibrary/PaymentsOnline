package edu.ucla.library.libservices.webservices.ecommerce.tests;

import static org.mockito.Mockito.*;

import com.google.gson.Gson;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroAccount;
import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroAccountList;
import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroContact;
import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroInvoice;
import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroInvoiceList;
import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroLineItem;
import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroPayment;

import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroPaymentAccount;
import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroPaymentInvoice;

import edu.ucla.library.libservices.webservices.ecommerce.web.clients.XeroPaymentClient;

import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

import java.nio.file.Paths;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;

public class PaymentServletTest
{
  private static String BASE_PATH = Paths.get(System.getProperty("user.dir"), "public_html", "resources").toString();
  private static String TOKENS_FILE = Paths.get(BASE_PATH, "future_proof.txt").toString();
  private static String SECRETS_FILE = Paths.get(BASE_PATH, "mock_xero.props").toString();

  private static XeroPayment mockPayment;
  private static XeroContact mockContact;
  private static XeroInvoice mockInvoice;
  private static XeroInvoiceList mockInvoiceList;
  private static XeroLineItem mockLine;
  private static ArrayList<XeroLineItem> mockLineList;
  private static XeroAccount mockAccount;
  private static XeroAccountList mockAccountList;
  private static HttpServletRequest mockRequest;

  public PaymentServletTest()
  {
  }

  public static void main(String[] args)
  {
    String[] args2 =
    {
      PaymentServletTest.class.getName()
    };
    JUnitCore.main(args2);
  }

  @Before
  public void setUp()
    throws Exception
  {
    XeroPaymentInvoice invoice;
    XeroPaymentAccount account;

    invoice = new XeroPaymentInvoice();
    invoice.setInvoiceID("123-456");

    account = new XeroPaymentAccount();
    account.setAccountID("ad4e5b15-3583");

    mockPayment = new XeroPayment();
    mockPayment.setAccount(account);
    mockPayment.setAmount(200.00d);
    mockPayment.setDate(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
    mockPayment.setInvoice(invoice);
    mockPayment.setReference("payment from LPO; payment method: cc; Transact transaction #: 95175");

    mockContact = new XeroContact();
    mockContact.setContactID("1234");
    mockContact.setContactNumber("5678");
    mockContact.setFirstName("Joe");
    mockContact.setLastName("Bruin");
    mockContact.setName("Joe Bruin, esq");

    mockLine = new XeroLineItem();
    mockLine.setLineItemID("14b5b762-ebd2");
    mockLine.setAccountCode("PRESERV#01");
    mockLine.setAccountID("ad4e5b15-3583");
    mockLine.setDescription("Labor: Principle Scientist");
    mockLine.setItemCode("PRSVLBR - SC");
    mockLine.setLineAmount(56.58);
    mockLine.setTaxAmount(5.52);
    mockLineList = new ArrayList<>();
    mockLineList.add(mockLine);

    mockInvoice = new XeroInvoice();
    mockInvoice.setAmountDue(100.00d);
    mockInvoice.setInvoiceID("123-456");
    mockInvoice.setInvoiceNumber("SR00125");
    mockInvoice.setReference("SRLF");
    mockInvoice.setContact(mockContact);
    mockInvoice.setLineItems(mockLineList);

    mockInvoiceList = new XeroInvoiceList();
    mockInvoiceList.getInvoices().add(mockInvoice);

    mockAccount = new XeroAccount();
    mockAccount.setAccountID("ad4e5b15-3583");
    mockAccount.setName("45400-PR-E");
    mockAccount.setCode("PRESERV#01");

    mockAccountList = new XeroAccountList();
    mockAccountList.getAccounts().add(mockAccount);

    mockRequest = mock(HttpServletRequest.class);
    when(mockRequest.getParameter("pmtcode")).thenReturn("cc");
    when(mockRequest.getParameter("tx")).thenReturn("95175");
    when(mockRequest.getParameter("UCLA_REF_NO")).thenReturn("123-456");
  }

  @After
  public void tearDown()
    throws Exception
  {
  }

  @BeforeClass
  public static void setUpBeforeClass()
    throws Exception
  {
  }

  @AfterClass
  public static void tearDownAfterClass()
    throws Exception
  {
  }

  /**
   * @see edu.ucla.library.libservices.webservices.ecommerce.web.servlets.PaymentServlet#doXeroPayment()
   * replicating code from servlet rather than direct call to class method, to avoid putting
   * output statements in class that would only be used by test
   */
  @Test
  @SuppressWarnings("oracle.jdeveloper.java.tag-invalid-see-reference")
  public void testDoXeroPayment()
    throws IOException
  {
    int port;
    String reference;
    XeroPaymentClient theClient;

    port = findFreePort();
    
    reference = buildReference(mockRequest);
    assertEquals(reference, mockPayment.getReference());
    theClient = new XeroPaymentClient();
    theClient.setInvoiceNumber(mockRequest.getParameter("UCLA_REF_NO"));
    theClient.setPort(port);
    theClient.setReference(reference);
    theClient.setSecretsFile(SECRETS_FILE);
    theClient.setTokensFile(TOKENS_FILE);
    XeroPayment testPayment = theClient.buildPayment(mockAccount.getAccountID(), mockInvoice.getInvoiceID(), 20000.00d);
    assertEquals(testPayment, mockPayment);

  }

  private static int findFreePort()
    throws IOException
  {
    try (ServerSocket socket = new ServerSocket(0))
    {
      socket.setReuseAddress(true);
      return socket.getLocalPort();
    }
  }

  private static String buildReference(HttpServletRequest mockRequest)
  {
    StringBuffer buffer;
    buffer = new StringBuffer("payment from LPO; ");
    buffer.append("payment method: " + mockRequest.getParameter("pmtcode") + "; ");
    buffer.append("Transact transaction #: " + mockRequest.getParameter("tx"));
    return buffer.toString();
  }
}
