package edu.ucla.library.libservices.webservices.ecommerce.tests;

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
import edu.ucla.library.libservices.webservices.ecommerce.web.clients.XeroInvoiceClient;

import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.InetSocketAddress;

import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class XeroInvoiceClientTest
{
  private static String BASE_PATH = Paths.get(System.getProperty("user.dir"), "public_html", "resources").toString();
  private static String SECRETS_FILE = Paths.get(BASE_PATH, "mock_xero.props").toString();
  private static XeroContact mockContact;
  private static XeroInvoice mockInvoice;
  private static XeroInvoiceList mockInvoiceList;
  private static XeroLineItem mockLine;
  private static ArrayList<XeroLineItem> mockLineList;
  private static XeroAccount mockAccount;
  private static XeroAccountList mockAccountList;

  @Before
  public void setUp()
    throws Exception
  {
    TestUtilities.writeFutureFile();

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
    mockLine.setTransactItemCode("45400-PR-E");
    mockLineList = new ArrayList<>();
    mockLineList.add(mockLine);

    mockInvoice = new XeroInvoice();
    mockInvoice.setAmountDue(100.00d);
    mockInvoice.setInvoiceID("123-456");
    mockInvoice.setInvoiceNumber("SR00125");
    mockInvoice.setReference("SRLF");
    mockInvoice.setContact(mockContact);
    mockInvoice.setLineItems(mockLineList);
    mockInvoice.setItemCodeAmts(groupAndSumCodes(mockInvoice.getLineItems()));
    mockInvoice.setAccountAmts(groupAndSumAccounts(mockInvoice.getLineItems()));

    mockInvoiceList = new XeroInvoiceList();
    mockInvoiceList.getInvoices().add(mockInvoice);

    mockAccount = new XeroAccount();
    mockAccount.setAccountID("ad4e5b15-3583");
    mockAccount.setName("45400-PR-E");
    mockAccount.setCode("PRESERV#01");

    mockAccountList = new XeroAccountList();
    mockAccountList.getAccounts().add(mockAccount);
  }

  @After
  public void tearDown()
    throws Exception
  {
    TestUtilities.clearFiles();
  }

  /**
   * @see edu.ucla.library.libservices.webservices.ecommerce.web.clients.XeroInvoiceClient#setSecretsFile(String)
   */
  @Test
  public void testSetGetSecretsFile()
  {
    XeroInvoiceClient testClient;
    testClient = new XeroInvoiceClient();
    testClient.setSecretsFile(SECRETS_FILE);
    Assert.assertTrue(testClient.getSecretsFile().equals(SECRETS_FILE));
  }

  /**
   * @see edu.ucla.library.libservices.webservices.ecommerce.web.clients.XeroInvoiceClient#setTokensFile(String)
   */
  @Test
  public void testSetGetTokensFile()
  {
    XeroInvoiceClient testClient;
    testClient = new XeroInvoiceClient();
    testClient.setTokensFile(TestUtilities.getFutureFile());
    Assert.assertTrue(testClient.getTokensFile().equals(TestUtilities.getFutureFile()));
  }

  /**
   * @see edu.ucla.library.libservices.webservices.ecommerce.web.clients.XeroInvoiceClient#setContactID(String)
   */
  @Test
  public void testSetGetContactID()
  {
    String dummyID;
    XeroInvoiceClient testClient;
    dummyID = "1234";
    testClient = new XeroInvoiceClient();
    testClient.setContactID(dummyID);
    Assert.assertTrue(testClient.getContactID().equals(dummyID));
  }

  /**
   * @see edu.ucla.library.libservices.webservices.ecommerce.web.clients.XeroInvoiceClient#setInvoiceID(String)
   */
  @Test
  public void testSetGetInvoiceID()
  {
    String dummyID;
    XeroInvoiceClient testClient;
    dummyID = "1234";
    testClient = new XeroInvoiceClient();
    testClient.setInvoiceID(dummyID);
    Assert.assertTrue(testClient.getInvoiceID().equals(dummyID));
  }

  /**
   * @see edu.ucla.library.libservices.webservices.ecommerce.web.clients.XeroInvoiceClient#getSingleInvoice()
   */
  @Test
  public void testGetSingleInvoice()
    throws IOException
  {
    int port;
    Gson gson;
    HttpHandler invoiceHandler;
    HttpHandler accountHandler;
    HttpServer mockServer;
    InetSocketAddress mockAddress;
    String mockInvoiceJson;
    String mockAccountJson;
    XeroInvoiceClient testClient;
    XeroInvoice testInvoice;

    port = TestUtilities.findFreePort();
    mockAddress = new InetSocketAddress(port);
    mockServer = HttpServer.create(mockAddress, 0);

    try
    {
      gson = new Gson();
      mockInvoiceJson = gson.toJson(mockInvoiceList);
      invoiceHandler = new HttpHandler()
      {
        public void handle(HttpExchange exchange)
          throws IOException
        {
          byte[] response = mockInvoiceJson.getBytes();
          exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length);
          exchange.getResponseBody().write(response);
          exchange.close();
        }
      };
      mockAccountJson = gson.toJson(mockAccountList);
      accountHandler = new HttpHandler()
      {
        public void handle(HttpExchange exchange)
          throws IOException
        {
          byte[] response = mockAccountJson.getBytes();
          exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length);
          exchange.getResponseBody().write(response);
          exchange.close();
        }
      };
      mockServer.createContext("/api.xro/2.0/Invoices/123-456", invoiceHandler);
      mockServer.createContext("/api.xro/2.0/Accounts/ad4e5b15-3583", accountHandler);

      mockServer.start();

      testClient = new XeroInvoiceClient();
      testClient.setInvoiceID("123-456");
      testClient.setSecretsFile(SECRETS_FILE);
      testClient.setTokensFile(TestUtilities.getFutureFile());
      testClient.setPort(port);

      testInvoice = testClient.getSingleInvoice();
      Assert.assertTrue(testInvoice.equals(mockInvoice));
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      mockServer.stop(0);

    }

  }

  /**
   * @see edu.ucla.library.libservices.webservices.ecommerce.web.clients.XeroInvoiceClient#getAllUnpaid()
   */
  @Test
  public void testGetAllUnpaid()
    throws IOException
  {
    int port;
    Gson gson;
    HttpHandler handler;
    HttpServer mockServer;
    InetSocketAddress mockAddress;
    String mockJson;
    XeroInvoiceClient testClient;
    ArrayList<XeroInvoice> testList;

    port = TestUtilities.findFreePort();
    mockAddress = new InetSocketAddress(port);
    mockServer = HttpServer.create(mockAddress, 0);

    try
    {
      gson = new Gson();
      mockJson = gson.toJson(mockInvoiceList);
      handler = new HttpHandler()
      {
        public void handle(HttpExchange exchange)
          throws IOException
        {
          String query = exchange.getRequestURI().getQuery();
          String path = exchange.getRequestURI().getPath();
          Assert.assertEquals("Statuses=AUTHORISED&ContactIds=1234", query);
          Assert.assertEquals("/api.xro/2.0/Invoices", path);

          byte[] response = mockJson.getBytes();
          exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length);
          exchange.getResponseBody().write(response);
          exchange.close();
        }
      };
      mockServer.createContext("/api.xro/2.0/Invoices", handler);
      mockServer.start();

      testClient = new XeroInvoiceClient();
      testClient.setContactID("1234");
      testClient.setSecretsFile(SECRETS_FILE);
      testClient.setTokensFile(TestUtilities.getFutureFile());
      testClient.setPort(port);

      testList = testClient.getAllUnpaid();
      Assert.assertTrue(testList.equals(mockInvoiceList.getInvoices()));
    }
    finally
    {
      mockServer.stop(0);
    }
  }

  private static HashMap<String, Double> groupAndSumCodes(ArrayList<XeroLineItem> theLines)
  {
    return (HashMap<String, Double>) theLines.stream()
           .collect(Collectors.groupingBy(XeroLineItem::getTransactItemCode,
                                          Collectors.summingDouble(XeroLineItem::getLineTotal)));
  }

  private static HashMap<String, Double> groupAndSumAccounts(ArrayList<XeroLineItem> theLines)
  {
    return (HashMap<String, Double>) theLines.stream()
           .collect(Collectors.groupingBy(XeroLineItem::getAccountID,
                                          Collectors.summingDouble(XeroLineItem::getLineTotal)));
  }
}
