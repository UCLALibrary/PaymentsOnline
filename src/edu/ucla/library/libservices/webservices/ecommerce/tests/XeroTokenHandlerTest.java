package edu.ucla.library.libservices.webservices.ecommerce.tests;

import com.google.gson.Gson;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroTokenBean;
//import edu.ucla.library.libservices.webservices.ecommerce.utility.handlers.PropertiesHandler;
import edu.ucla.library.libservices.webservices.ecommerce.utility.handlers.TokenFileHandler;
import edu.ucla.library.libservices.webservices.ecommerce.utility.handlers.XeroTokenHandler;

import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.InetSocketAddress;

import java.nio.file.Paths;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class XeroTokenHandlerTest
{
  private static String BASE_PATH = Paths.get(System.getProperty("user.dir"), "public_html", "resources").toString();
  private static String SECRETS_FILE = Paths.get(BASE_PATH, "mock_xero.props").toString();
  private static XeroTokenBean FUTURE_BEAN;
  private static XeroTokenBean FUTURE_READ;
  private static XeroTokenBean EXPIRED_BEAN;

  @Before
  public void setUp()
    throws Exception
  {
    Gson gson;
    TokenFileHandler handler;

    FUTURE_BEAN = TestUtilities.populateBean("1800");
    EXPIRED_BEAN = TestUtilities.populateBean("-1800");

    gson = new Gson();
    handler = new TokenFileHandler();

    TestUtilities.writeFutureFile();
    TestUtilities.writeExpiredFile();

    handler.setTokensFile(TestUtilities.getFutureFile());
    FUTURE_READ = handler.readTokensFile();
  }


  @After
  public void tearDown()
    throws Exception
  {
    TestUtilities.clearFiles();
  }

  /**
   * @see edu.ucla.library.libservices.webservices.ecommerce.utility.handlers.XeroTokenHandler#setSecretsFile(String)
   */
  @Test
  public void testSetGetSecretsFile()
  {
    XeroTokenHandler theHandler;
    theHandler = new XeroTokenHandler();
    theHandler.setSecretsFile(SECRETS_FILE);
    Assert.assertTrue(theHandler.getSecretsFile().equals(SECRETS_FILE));
  }

  /**
   * @see edu.ucla.library.libservices.webservices.ecommerce.utility.handlers.XeroTokenHandler#setTokensFile(String)
   */
  @Test
  public void testSetGetTokensFile()
  {
    XeroTokenHandler theHandler;
    theHandler = new XeroTokenHandler();
    theHandler.setTokensFile(TestUtilities.getFutureFile());
    Assert.assertTrue(theHandler.getTokensFile().equals(TestUtilities.getFutureFile()));
  }

  /**
   * @see edu.ucla.library.libservices.webservices.ecommerce.utility.handlers.XeroTokenHandler#getTokens()
   */
  @Test
  public void testGetTokensUnexpired()
  {
    XeroTokenBean result;
    XeroTokenHandler handler;
    handler = new XeroTokenHandler();
    handler.setSecretsFile(SECRETS_FILE);
    handler.setTokensFile(TestUtilities.getFutureFile());

    result = handler.getTokens();
    Assert.assertTrue(result.equals(FUTURE_READ));
  }

  /**
   * @see edu.ucla.library.libservices.webservices.ecommerce.utility.handlers.XeroTokenHandler#getTokens()
   */
  @Test
  public void testGetTokensExpired()
    throws IOException
  {
    int port;
    Gson gson;
    HttpHandler tokenHandler;
    HttpServer mockServer;
    InetSocketAddress mockAddress;
    String mockTokenJson;
    XeroTokenBean result;
    XeroTokenHandler handler;

    port = TestUtilities.findFreePort();
    mockAddress = new InetSocketAddress(port);
    mockServer = HttpServer.create(mockAddress, 0);

    gson = new Gson();
    mockTokenJson = gson.toJson(FUTURE_BEAN);
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

    handler = new XeroTokenHandler();
    handler.setSecretsFile(SECRETS_FILE);
    handler.setTokensFile(TestUtilities.getExpiredFile());
    handler.setPort(port);
    result = handler.getTokens();
    Assert.assertFalse(result.equals(EXPIRED_BEAN));
    mockServer.stop(0);
  }

}
