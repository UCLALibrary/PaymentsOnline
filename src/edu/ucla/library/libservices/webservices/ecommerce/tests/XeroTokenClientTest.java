package edu.ucla.library.libservices.webservices.ecommerce.tests;

import edu.ucla.library.libservices.webservices.ecommerce.utility.handlers.PropertiesHandler;
import edu.ucla.library.libservices.webservices.ecommerce.web.clients.XeroTokenClient;

import java.nio.file.Paths;

import static org.junit.Assert.assertNotNull;
import org.junit.Test;

public class XeroTokenClientTest
{
  private static String BASE_PATH = Paths.get(System.getProperty("user.dir"), "public_html", "resources").toString();
  private static String SECRETS_FILE = Paths.get(BASE_PATH, "xero.props").toString();
  private static String DUMMY_REFRESH = "wSzpv1rx0k9gCkvGrzXTc";

  /**
   * @see edu.ucla.library.libservices.webservices.ecommerce.web.clients.XeroTokenClient#setSecretsFile(String)
   */
  @Test
  public void testSetGetSecretsFile()
  {
    XeroTokenClient theClient;
    theClient = new XeroTokenClient();
    theClient.setSecretsFile(SECRETS_FILE);
    assert(theClient.getSecretsFile().equals(SECRETS_FILE));
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
    assert(theClient.getRefreshToken().equals(DUMMY_REFRESH));
  }

  /**
   * @see edu.ucla.library.libservices.webservices.ecommerce.web.clients.XeroTokenClient#getTokens()
   */
  @Test
  public void testGetTokens()
  {
    PropertiesHandler props;
    String json;
    XeroTokenClient theClient;

    props = new PropertiesHandler();
    // a file specifically to hold a Xero OAuth refresh token
    props.setFileName(Paths.get(BASE_PATH, "refresh.props").toString());

    theClient = new XeroTokenClient();
    theClient.setRefreshToken(props.loadProperties().getProperty("refresh_token"));
    theClient.setSecretsFile(SECRETS_FILE);
    json = theClient.getTokens();
    assertNotNull(json);
    assert(json.contains("access_token") && json.contains("refresh_token") );
  }
}
