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
  private static XeroTokenBean OUT_BEAN;

  @Before
  public void setUp()
    throws Exception
  {
    SOURCE_BEAN = new XeroTokenBean();
    SOURCE_BEAN.setAccess_token("eyJhbGciOiJSUzI1Ni");
    SOURCE_BEAN.setExpires_in("2035-07-08T16:17:53.186");
    SOURCE_BEAN.setRefresh_token("wSzpv1rx0k9gCkvGrzXT");
    SOURCE_BEAN.setScope("accounting.settings accounting.transactions accounting.contacts offline_access");

    OUT_BEAN = new XeroTokenBean();
    OUT_BEAN.setAccess_token("eyJhbGciOiJSUzI1Ni");
    OUT_BEAN.setExpires_in("1800");
    OUT_BEAN.setRefresh_token("wSzpv1rx0k9gCkvGrzXT");
    OUT_BEAN.setScope("accounting.settings accounting.transactions accounting.contacts offline_access");
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
    handler.writeTokensFile(gson.toJson(OUT_BEAN));
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
    System.out.println(SOURCE_BEAN.getAccess_token() + "\t" + SOURCE_BEAN.getExpires_in() + "\t" + SOURCE_BEAN.getRefresh_token() +"\t"
                       + SOURCE_BEAN.getScope());
    System.out.println(result.getAccess_token() + "\t" + result.getExpires_in() + "\t" + result.getRefresh_token() +"\t"
                       + result.getScope());
    System.out.println(result.equals(SOURCE_BEAN));
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