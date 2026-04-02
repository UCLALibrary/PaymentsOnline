package edu.ucla.library.libservices.webservices.ecommerce.tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

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

import java.nio.file.Paths;

public class XeroAccountClientTest
{
  private static String BASE_PATH = Paths.get(System.getProperty("user.dir"), "public_html", "resources").toString();
  private static String SECRETS_FILE = Paths.get(BASE_PATH, "mock_xero.props").toString();
  private static XeroAccount mockAccount;
  private static XeroAccountList mockAccountList;

  @Before
  public void setUp()
    throws Exception
  {
    TestUtilities.writeFutureFile();
    
    mockAccount = new XeroAccount();
    mockAccount.setAccountID("ad4e5b153583");
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
   * @see edu.ucla.library.libservices.webservices.ecommerce.web.clients.XeroAccountClient#setSecretsFile(String)
   */
  @Test
  public void testSetGetSecretsFile()
  {
    XeroAccountClient testClient;
    testClient = new XeroAccountClient();
    testClient.setSecretsFile(SECRETS_FILE);
    Assert.assertTrue(testClient.getSecretsFile().equals(SECRETS_FILE));
  }

  /**
   * @see edu.ucla.library.libservices.webservices.ecommerce.web.clients.XeroAccountClient#setTokensFile(String)
   */
  @Test
  public void testSetGetTokensFile()
  {
    XeroAccountClient testClient;
    testClient = new XeroAccountClient();
    testClient.setTokensFile(TestUtilities.getFutureFile());
    Assert.assertTrue(testClient.getTokensFile().equals(TestUtilities.getFutureFile()));
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
    Assert.assertTrue(testClient.getAccountID().equals("123"));
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

    port = TestUtilities.findFreePort();

    mockAddress = new InetSocketAddress(port);
    mockServer = HttpServer.create(mockAddress, 0);

    try
    {
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
      mockServer.createContext("/api.xro/2.0/Accounts/ad4e5b153583", accountHandler);
      mockServer.start();

      testClient = new XeroAccountClient();
      testClient.setAccountID("ad4e5b153583");
      testClient.setSecretsFile(SECRETS_FILE);
      testClient.setTokensFile(TestUtilities.getFutureFile());
      testClient.setPort(port);
      testItemCode = testClient.getItemCode();

      Assert.assertTrue(testItemCode.equals(mockAccount.getName()));
    }
    finally
    {
      mockServer.stop(0);
    }
  }
}
