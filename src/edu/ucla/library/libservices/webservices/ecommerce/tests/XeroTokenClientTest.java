package edu.ucla.library.libservices.webservices.ecommerce.tests;

import edu.ucla.library.libservices.webservices.ecommerce.utility.handlers.PropertiesHandler;
import edu.ucla.library.libservices.webservices.ecommerce.web.clients.XeroTokenClient;
import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroTokenBean;

import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Before;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

import com.google.gson.Gson;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.net.HttpURLConnection;
import java.net.InetSocketAddress;

import java.net.ServerSocket;

import java.io.IOException;

public class XeroTokenClientTest
{
  private static String BASE_PATH = Paths.get(System.getProperty("user.dir"), "public_html", "resources").toString();
  private static String SECRETS_FILE = Paths.get(BASE_PATH, "mock_xero.props").toString();
  private static String DUMMY_REFRESH = "wSzpv1rx0k9gCkvGrzXTc";
  private static XeroTokenBean SOURCE_BEAN;

  @Before
  public void setUp()
    throws Exception
  {
    SOURCE_BEAN = new XeroTokenBean();
    SOURCE_BEAN.setAccess_token("eyJhbGciOiJSUzI1Ni");
    SOURCE_BEAN.setExpires_in("2035-07-08T16:17:53.186");
    SOURCE_BEAN.setRefresh_token("wSzpv1rx0k9gCkvGrzXT");
    SOURCE_BEAN.setScope("accounting.settings accounting.transactions accounting.contacts offline_access");
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
    PropertiesHandler props;
    String json;
    XeroTokenClient theClient;

    port = findFreePort();
    mockAddress = new InetSocketAddress(port);
    mockServer = HttpServer.create(mockAddress, 0);

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

    props = new PropertiesHandler();
    // a file specifically to hold a Xero OAuth refresh token
    props.setFileName(Paths.get(BASE_PATH, "refresh.props").toString());

    theClient = new XeroTokenClient();
    theClient.setRefreshToken(props.loadProperties().getProperty("refresh_token"));
    theClient.setSecretsFile(SECRETS_FILE);
    theClient.setPort(port);
    json = theClient.getTokens();
    assertNotNull(json);
    Assert.assertTrue(json.contains("access_token") && json.contains("refresh_token") );
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