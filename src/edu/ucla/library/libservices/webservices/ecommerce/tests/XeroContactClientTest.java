package edu.ucla.library.libservices.webservices.ecommerce.tests;

import static org.junit.Assert.*;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import edu.ucla.library.libservices.webservices.ecommerce.web.clients.XeroContactClient;

import java.net.HttpURLConnection;

import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroContact;

import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroContactList;

import java.io.IOException;

import java.net.InetSocketAddress;

import org.junit.Before;
import org.junit.Test;
import com.google.gson.Gson;

import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroTokenBean;

import edu.ucla.library.libservices.webservices.ecommerce.utility.handlers.PropertiesHandler;
import edu.ucla.library.libservices.webservices.ecommerce.utility.handlers.TokenFileHandler;

import java.nio.file.Paths;

public class XeroContactClientTest
{
  private static XeroContact mockContact;
  private static XeroContactList mockList;
  private static String BASE_PATH = Paths.get(System.getProperty("user.dir"), "public_html", "resources").toString();
  private static String TOKENS_FILE = Paths.get(BASE_PATH, "future_proof.txt").toString();
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

    FUTURE_BEAN = new XeroTokenBean();
    FUTURE_BEAN.setAccess_token("eyJhbGciOiJSUzI1Ni");
    FUTURE_BEAN.setExpires_in("1800");
    FUTURE_BEAN.setRefresh_token("wSzpv1rx0k9gCkvGrzXT");
    FUTURE_BEAN.setScope("accounting.settings accounting.transactions accounting.contacts offline_access");

    TokenFileHandler handler;
    Gson gson;

    gson = new Gson();

    handler = new TokenFileHandler();
    handler.setTokensFile(TOKENS_FILE);
    handler.writeTokensFile(gson.toJson(FUTURE_BEAN));
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
    assert(testClient.getSecretsFile().equals(SECRETS_FILE));
  }

  /**
   * @see XeroContactClient#setTokensFile(String)
   */
  @Test
  public void testSetGetTokensFile()
  {
    XeroContactClient testClient;
    testClient = new XeroContactClient();
    testClient.setTokensFile(TOKENS_FILE);
    assert(testClient.getTokensFile().equals(TOKENS_FILE));
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
    assert(testClient.getUserID().equals("1234"));
  }

  /**
   * @see XeroContactClient#getTheContact()
   */
  @Test
  public void testGetTheContact()
    throws IOException
  {
    Gson gson;
    HttpHandler handler;
    HttpServer mockServer;
    InetSocketAddress mockAddress;
    String mockJson;
    XeroContactClient testClient;
    XeroContact testContact;

    mockAddress = new InetSocketAddress(8000);
    mockServer = HttpServer.create(mockAddress, 0);
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
    mockServer.createContext("/api.xro/2.0/Contacts/1234", handler);
    mockServer.start();
    
    testClient = new XeroContactClient();
    testClient.setSecretsFile(SECRETS_FILE);
    testClient.setTokensFile(TOKENS_FILE);
    testClient.setUserID("1234");
    testContact = testClient.getTheContact();
    assert(testClient.equals(mockContact));
    
    mockServer.stop(0);
  }
}
