package edu.ucla.library.libservices.webservices.ecommerce.tests;

import com.google.gson.Gson;

import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroTokenBean;
import edu.ucla.library.libservices.webservices.ecommerce.utility.handlers.TokenFileHandler;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

public class TokenFileHandlerTest
{
  private static String BASE_PATH = Paths.get(System.getProperty("user.dir"), "public_html", "resources").toString();
  private static String TOKENS_FILE = Paths.get(BASE_PATH, "default_secrets.txt").toString();
  private static XeroTokenBean SOURCE_BEAN;

  public TokenFileHandlerTest()
  {
  }

  public static void main(String[] args)
  {
    String[] args2 =
    {
      TokenFileHandlerTest.class.getName()
    };
    org.junit
       .runner
       .JUnitCore
       .main(args2);
  }

  @Before
  public void setUp()
    throws Exception
  {
    SOURCE_BEAN = new XeroTokenBean();
    SOURCE_BEAN.setAccess_token("eyJhbGciOiJSUzI1Ni");
    SOURCE_BEAN.setExpires_in("1800");
    SOURCE_BEAN.setRefresh_token("wSzpv1rx0k9gCkvGrzXT");
    SOURCE_BEAN.setScope("accounting.settings accounting.transactions accounting.contacts offline_access");
  }

  /**
   * @see edu.ucla.library.libservices.webservices.ecommerce.utility.handlers.TokenFileHandler#writeTokensFile(String)
   */
  @Test
  public void testWriteTokensFile()
    throws IOException
  {
    TokenFileHandler handler;
    String outputFile;
    Gson gson;

    gson = new Gson();
    outputFile = Paths.get(BASE_PATH, "test_secrets.txt").toString();

    handler = new TokenFileHandler();
    handler.setTokensFile(outputFile);
    handler.writeTokensFile(gson.toJson(SOURCE_BEAN));
    assert (Files.exists(Paths.get(outputFile)));
  }

  /**
   * @see edu.ucla.library.libservices.webservices.ecommerce.utility.handlers.TokenFileHandler#readTokensFile()
   */
  @Test
  public void testReadTokensFile()
  {
    XeroTokenBean result;
    TokenFileHandler handler;

    handler = new TokenFileHandler();
    handler.setTokensFile(TOKENS_FILE);
    result = handler.readTokensFile();
    assert (result.equals(SOURCE_BEAN));
  }

  /**
   * @see edu.ucla.library.libservices.webservices.ecommerce.utility.handlers.TokenFileHandler#setTokensFile(String)
   */
  @Test
  public void testSetGetTokensFile()
  {
    TokenFileHandler handler;

    handler = new TokenFileHandler();
    handler.setTokensFile(TOKENS_FILE);
    assert (handler.getTokensFile().equals(TOKENS_FILE));
  }

}
