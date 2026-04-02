package edu.ucla.library.libservices.webservices.ecommerce.tests;

import com.google.gson.Gson;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroContact;
import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroContactList;
import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroTokenBean;
import edu.ucla.library.libservices.webservices.ecommerce.web.clients.XeroContactClient;

import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.InetSocketAddress;

import java.nio.file.Paths;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class XeroContactClientTest
{
  private static XeroContact mockContact;
  private static XeroContactList mockList;
  private static String BASE_PATH = Paths.get(System.getProperty("user.dir"), "public_html", "resources").toString();
  private static String SECRETS_FILE = Paths.get(BASE_PATH, "mock_xero.props").toString();
  private static XeroTokenBean FUTURE_BEAN;

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
    mockList = new XeroContactList();
    mockList.getContacts().add(mockContact);

    TestUtilities.writeFutureFile();
    FUTURE_BEAN = TestUtilities.populateBean("1800", false);
  }

  @After
  public void tearDown()
    throws Exception
  {
    TestUtilities.clearFiles();
  }

  /**
   * @see XeroContactClient#setSecretsFile(String)
   */
  @Test
  public void testSetGetSecretsFile()
  {
    XeroContactClient testClient;
    testClient = new XeroContactClient();
    testClient.setSecretsFile(SECRETS_FILE);
    Assert.assertTrue(testClient.getSecretsFile().equals(SECRETS_FILE));
  }

  /**
   * @see XeroContactClient#setTokensFile(String)
   */
  @Test
  public void testSetGetTokensFile()
  {
    XeroContactClient testClient;
    testClient = new XeroContactClient();
    testClient.setTokensFile(TestUtilities.getFutureFile());
    Assert.assertTrue(testClient.getTokensFile().equals(TestUtilities.getFutureFile()));
  }

  /**
   * @see XeroContactClient#setUserID(String)
   */
  @Test
  public void testSetGetUserID()
  {
    XeroContactClient testClient;
    testClient = new XeroContactClient();
    testClient.setUserID("1234");
    Assert.assertTrue(testClient.getUserID().equals("1234"));
  }

  /**
   * @see XeroContactClient#getTheContact()
   */
  @Test
  public void testGetTheContact()
    throws IOException
  {
    int port;
    Gson gson;
    HttpHandler handler;
    HttpServer mockServer;
    InetSocketAddress mockAddress;
    String mockJson;
    XeroContactClient testClient;
    XeroContact testContact;

    port = TestUtilities.findFreePort();
    mockAddress = new InetSocketAddress(port);
    mockServer = HttpServer.create(mockAddress, 0);
    try
    {
      gson = new Gson();
      mockJson = gson.toJson(mockList);
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
      mockServer.createContext("/api.xro/2.0/Contacts", handler);
      mockServer.start();

      testClient = new XeroContactClient();
      testClient.setSecretsFile(SECRETS_FILE);
      testClient.setTokensFile(TestUtilities.getFutureFile());
      testClient.setUserID("1234");
      testClient.setPort(port);
      testContact = testClient.getTheContact();
      Assert.assertTrue(testContact.equals(mockContact));
    }
    finally
    {
      mockServer.stop(0);
    }
  }
}
