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
  private static String TOKENS_FILE = Paths.get(BASE_PATH, "default_secrets.txt").toString();
  private static String SECRETS_FILE = Paths.get(BASE_PATH, "mock_xero.props").toString();
  private static String FUTURE_FILE = Paths.get(BASE_PATH, "future_proof.txt").toString();
  private static String EXPIRED_FILE = Paths.get(BASE_PATH, "expired.txt").toString();
  private static XeroTokenBean FUTURE_BEAN;
  private static XeroTokenBean FUTURE_READ;
  private static XeroTokenBean EXPIRED_BEAN;

  @Before
  public void setUp()
    throws Exception
  {
    //PropertiesHandler props;
    //String fakeAccessToken;
    //TokenFileHandler expiredHandler;

    //props = new PropertiesHandler();
    // a file specifically to hold a Xero OAuth refresh token
    //props.setFileName(Paths.get(BASE_PATH, "refresh.props").toString());

    //expiredHandler = new TokenFileHandler();
    //expiredHandler.setTokensFile(TOKENS_FILE);

    //fakeAccessToken = expiredHandler.readTokensFile().getAccess_token();
    FUTURE_BEAN = TestUtilities.populateBean("1800");
    /*FUTURE_BEAN = new XeroTokenBean();
    FUTURE_BEAN.setAccess_token(fakeAccessToken);
    FUTURE_BEAN.setExpires_in("1800");
    FUTURE_BEAN.setRefresh_token("wSzpv1rx0k9gCkvGrzXT");
    FUTURE_BEAN.setScope("accounting.settings accounting.transactions accounting.contacts offline_access");*/

    EXPIRED_BEAN = TestUtilities.populateBean("-1800");
    /*EXPIRED_BEAN = new XeroTokenBean();
    EXPIRED_BEAN.setAccess_token(fakeAccessToken);
    EXPIRED_BEAN.setExpires_in("-1800");
    EXPIRED_BEAN.setRefresh_token(props.loadProperties().getProperty("refresh_token"));
    EXPIRED_BEAN.setScope("accounting.settings accounting.transactions accounting.contacts offline_access");*/

    TokenFileHandler handler;
    Gson gson;

    gson = new Gson();
    handler = new TokenFileHandler();

    //handler.setTokensFile(FUTURE_FILE);
    //handler.writeTokensFile(gson.toJson(FUTURE_BEAN));
    TestUtilities.writeFutureFile();

    //handler.setTokensFile(EXPIRED_FILE);
    //handler.writeTokensFile(gson.toJson(EXPIRED_BEAN));
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
    theHandler.setTokensFile(TOKENS_FILE);
    Assert.assertTrue(theHandler.getTokensFile().equals(TOKENS_FILE));
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
    handler.setTokensFile(FUTURE_FILE);

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
    handler.setTokensFile(EXPIRED_FILE);
    handler.setPort(port);
    result = handler.getTokens();
    Assert.assertFalse(result.equals(EXPIRED_BEAN));
    mockServer.stop(0);
  }

}
