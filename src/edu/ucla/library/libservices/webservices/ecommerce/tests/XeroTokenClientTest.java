package edu.ucla.library.libservices.webservices.ecommerce.tests;

import com.google.gson.Gson;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroTokenBean;
import edu.ucla.library.libservices.webservices.ecommerce.web.clients.XeroTokenClient;

import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.InetSocketAddress;

import java.nio.file.Paths;

import org.junit.Assert;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;

public class XeroTokenClientTest
{
  private static String BASE_PATH = Paths.get(System.getProperty("user.dir"), "public_html", "resources").toString();
  private static String SECRETS_FILE = Paths.get(BASE_PATH, "mock_xero.props").toString();
  private static String DUMMY_REFRESH = "dummy_token";
  private static XeroTokenBean SOURCE_BEAN;

  @Before
  public void setUp()
    throws Exception
  {
    SOURCE_BEAN = TestUtilities.populateBean("2035-07-08T16:17:53.186", false);
  }

  /**
   * @see edu.ucla.library.libservices.webservices.ecommerce.web.clients.XeroTokenClient#setSecretsFile(String)
   */
  @Test
  public void testSetGetSecretsFile()
  {
    XeroTokenClient theClient;
    theClient = new XeroTokenClient();
    theClient.setSecretsFile(SECRETS_FILE);
    Assert.assertTrue(theClient.getSecretsFile().equals(SECRETS_FILE));
  }

  /**
   * @see edu.ucla.library.libservices.webservices.ecommerce.web.clients.XeroTokenClient#setRefreshToken(String)
   */
  @Test
  public void testSetGetRefreshToken()
  {
    XeroTokenClient theClient;
    theClient = new XeroTokenClient();
    theClient.setRefreshToken(DUMMY_REFRESH);
    Assert.assertTrue(theClient.getRefreshToken().equals(DUMMY_REFRESH));
  }

  /**
   * @see edu.ucla.library.libservices.webservices.ecommerce.web.clients.XeroTokenClient#getTokens()
   */
  @Test
  public void testGetTokens()
    throws IOException
  {
    int port;
    Gson gson;
    HttpHandler tokenHandler;
    HttpServer mockServer;
    InetSocketAddress mockAddress;
    String mockTokenJson;
    String json;
    XeroTokenClient theClient;

    port = TestUtilities.findFreePort();
    mockAddress = new InetSocketAddress(port);
    mockServer = HttpServer.create(mockAddress, 0);
    
    try
    {
      gson = new Gson();
      mockTokenJson = gson.toJson(SOURCE_BEAN);
      tokenHandler = new HttpHandler()
      {
        public void handle(HttpExchange exchange)
          throws IOException
        {
          byte[] response = mockTokenJson.getBytes();
          exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length);
          exchange.getResponseBody().write(response);
          exchange.close();
        }
      };
      mockServer.createContext("/connect/token", tokenHandler);
      mockServer.start();

      theClient = new XeroTokenClient();
      theClient.setRefreshToken(DUMMY_REFRESH);
      theClient.setSecretsFile(SECRETS_FILE);
      theClient.setPort(port);
      json = theClient.getTokens();
      assertNotNull(json);
      Assert.assertTrue(json.contains("access_token") && json.contains("refresh_token") );
    }
    finally
    {
      mockServer.stop(0);
    }
  }
}