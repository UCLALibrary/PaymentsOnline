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

import java.net.ServerSocket;

import java.nio.file.Paths;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class XeroInvoiceClientTest
{
  private static String BASE_PATH = Paths.get(System.getProperty("user.dir"), "public_html", "resources").toString();
  private static String TOKENS_FILE = Paths.get(BASE_PATH, "future_proof.txt").toString();
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
    assert (testClient.getSecretsFile().equals(SECRETS_FILE));
  }

  /**
   * @see edu.ucla.library.libservices.webservices.ecommerce.web.clients.XeroInvoiceClient#setTokensFile(String)
   */
  @Test
  public void testSetGetTokensFile()
  {
    XeroInvoiceClient testClient;
    testClient = new XeroInvoiceClient();
    testClient.setTokensFile(TOKENS_FILE);
    assert (testClient.getTokensFile().equals(TOKENS_FILE));
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
    assert (testClient.getContactID().equals(dummyID));
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
    assert (testClient.getInvoiceID().equals(dummyID));
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

    port = findFreePort();
    mockAddress = new InetSocketAddress(port);
    mockServer = HttpServer.create(mockAddress, 0);

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
    testClient.setTokensFile(TOKENS_FILE);
    testClient.setPort(port);

    testInvoice = testClient.getSingleInvoice();
    assert (testInvoice.equals(mockInvoiceList));

    mockServer.stop(0);
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
    XeroInvoiceList testList;
    
    port = findFreePort();
    mockAddress = new InetSocketAddress(port);
    mockServer = HttpServer.create(mockAddress, 0);

    gson = new Gson();
    mockJson = gson.toJson(mockInvoiceList);
    handler = new HttpHandler()
    {
      public void handle(HttpExchange exchange)
        throws IOException
      {
        byte[] response = mockJson.getBytes();
        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
      }
    };
    mockServer.createContext("/api.xro/2.0/Invoices?Statuses=AUTHORISED&ContactIDs=1234", handler);
    mockServer.start();

    testClient = new XeroInvoiceClient();
    testClient.setContactID("1234");
    testClient.setSecretsFile(SECRETS_FILE);
    testClient.setTokensFile(TOKENS_FILE);
    testClient.setPort(port);

    testList = testClient.getAllUnpaid();
    assert (testList.equals(mockInvoice));

    mockServer.stop(0);
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

}
