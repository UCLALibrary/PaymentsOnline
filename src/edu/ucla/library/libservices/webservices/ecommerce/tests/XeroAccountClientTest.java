package edu.ucla.library.libservices.webservices.ecommerce.tests;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;

import com.google.gson.Gson;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroAccount;
import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroAccountList;
import edu.ucla.library.libservices.webservices.ecommerce.web.clients.XeroAccountClient;

import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.InetSocketAddress;

import java.net.ServerSocket;

import java.nio.file.Paths;

public class XeroAccountClientTest
{
  private static String BASE_PATH = Paths.get(System.getProperty("user.dir"), "public_html", "resources").toString();
  private static String TOKENS_FILE = Paths.get(BASE_PATH, "future_proof.txt").toString();
  private static String SECRETS_FILE = Paths.get(BASE_PATH, "mock_xero.props").toString();
  private static XeroAccount mockAccount;
  private static XeroAccountList mockAccountList;

  public XeroAccountClientTest()
  {
  }

  public static void main(String[] args)
  {
    String[] args2 =
    {
      XeroAccountClientTest.class.getName()
    };
    JUnitCore.main(args2);
  }

  @Before
  public void setUp()
    throws Exception
  {
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
   * @see edu.ucla.library.libservices.webservices.ecommerce.web.clients.XeroAccountClient#setSecretsFile(String)
   */
  @Test
  public void testSetGetSecretsFile()
  {
    XeroAccountClient testClient;
    testClient = new XeroAccountClient();
    testClient.setSecretsFile(SECRETS_FILE);
    assert(testClient.getSecretsFile().equals(SECRETS_FILE));
  }

  /**
   * @see edu.ucla.library.libservices.webservices.ecommerce.web.clients.XeroAccountClient#setTokensFile(String)
   */
  @Test
  public void testSetGetTokensFile()
  {
    XeroAccountClient testClient;
    testClient = new XeroAccountClient();
    testClient.setTokensFile(TOKENS_FILE);
    assert(testClient.getTokensFile().equals(TOKENS_FILE));
  }

  /**
   * @see edu.ucla.library.libservices.webservices.ecommerce.web.clients.XeroAccountClient#setAccountID(String)
   */
  @Test
  public void testSetGetAccountID()
  {
    XeroAccountClient testClient;
    testClient = new XeroAccountClient();
    testClient.setAccountID("123");
    assert(testClient.getAccountID().equals("123"));
  }

  /**
   * @see edu.ucla.library.libservices.webservices.ecommerce.web.clients.XeroAccountClient#getItemCode()
   */
  @Test
  public void testGetItemCode()
    throws IOException
  {
    Gson gson;
    HttpHandler accountHandler;
    HttpServer mockServer;
    InetSocketAddress mockAddress;
    String mockAccountJson;
    String testItemCode;
    XeroAccountClient testClient;
    int port;
    
    port = findFreePort();

    mockAddress = new InetSocketAddress(port);
    mockServer = HttpServer.create(mockAddress, 0);
    
    gson = new Gson();
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
    mockServer.createContext("/api.xro/2.0/Accounts/ad4e5b15-3583", accountHandler);
    mockServer.start();

    testClient = new XeroAccountClient();
    testClient.setAccountID("ad4e5b15-3583");
    testClient.setSecretsFile(SECRETS_FILE);
    testClient.setTokensFile(TOKENS_FILE);
    testClient.setPort(port);
    testItemCode = testClient.getItemCode();
    
    assert(testItemCode.equals(mockAccount.getName()));
    
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
