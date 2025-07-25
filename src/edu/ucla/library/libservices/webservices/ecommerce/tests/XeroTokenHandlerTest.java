package edu.ucla.library.libservices.webservices.ecommerce.tests;

import com.google.gson.Gson;

import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroTokenBean;
import edu.ucla.library.libservices.webservices.ecommerce.utility.handlers.TokenFileHandler;
import edu.ucla.library.libservices.webservices.ecommerce.utility.handlers.XeroTokenHandler;

import java.nio.file.Paths;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class XeroTokenHandlerTest
{
  private static DateTimeFormatter FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
  private static String BASE_PATH = Paths.get(System.getProperty("user.dir"), "public_html", "resources").toString();
  private static String TOKENS_FILE = Paths.get(BASE_PATH, "default_secrets.txt").toString();
  private static String SECRETS_FILE = Paths.get(BASE_PATH, "xero.props").toString();
  private static String FUTURE_FILE = Paths.get(BASE_PATH, "future_proof.txt").toString();
  private static String EXPIRED_FILE = Paths.get(BASE_PATH, "expired.txt").toString();
  private static XeroTokenBean FUTURE_BEAN;
  private static XeroTokenBean EXPIRED_BEAN;

  @Before
  public void setUp()
    throws Exception
  {
    FUTURE_BEAN = new XeroTokenBean();
    FUTURE_BEAN.setAccess_token("eyJhbGciOiJSUzI1Ni");
    FUTURE_BEAN.setExpires_in("1800");
    FUTURE_BEAN.setRefresh_token("wSzpv1rx0k9gCkvGrzXT");
    FUTURE_BEAN.setScope("accounting.settings accounting.transactions accounting.contacts offline_access");

    EXPIRED_BEAN = new XeroTokenBean();
    EXPIRED_BEAN.setAccess_token("eyJhbGciOiJSUzI1Ni");
    EXPIRED_BEAN.setExpires_in("-1800");
    EXPIRED_BEAN.setRefresh_token("Wa4YwrESWhnaSwVkgTbBmu6rVV_y9kmM9jYFuw73Svs");
    EXPIRED_BEAN.setScope("accounting.settings accounting.transactions accounting.contacts offline_access");

    TokenFileHandler handler;
    Gson gson;

    gson = new Gson();

    handler = new TokenFileHandler();
    handler.setTokensFile(FUTURE_FILE);
    handler.writeTokensFile(gson.toJson(FUTURE_BEAN));

    handler.setTokensFile(EXPIRED_FILE);
    handler.writeTokensFile(gson.toJson(EXPIRED_BEAN));
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
    assert (theHandler.getSecretsFile().equals(SECRETS_FILE));
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
    assert (theHandler.getTokensFile().equals(TOKENS_FILE));
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
    assert (result.equals(FUTURE_BEAN));
  }

  /**
   * @see edu.ucla.library.libservices.webservices.ecommerce.utility.handlers.XeroTokenHandler#getTokens()
   */
  @Test
  public void testGetTokensExpired()
  {
    XeroTokenBean result;
    XeroTokenHandler handler;
    handler = new XeroTokenHandler();
    handler.setSecretsFile(SECRETS_FILE);
    handler.setTokensFile(EXPIRED_FILE);

    result = handler.getTokens();
    Assert.assertFalse(result.equals(EXPIRED_BEAN));
  }
}
