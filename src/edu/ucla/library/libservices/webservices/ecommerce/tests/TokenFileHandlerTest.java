package edu.ucla.library.libservices.webservices.ecommerce.tests;

import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroTokenBean;
import edu.ucla.library.libservices.webservices.ecommerce.utility.handlers.TokenFileHandler;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

public class TokenFileHandlerTest
{
  private static String JSON = "{\"access_token\":\"eyJhbGciOiJSUzI1Ni\",\"expires_in\":1800,\"token_type\":\"Bearer\",\"refresh_token\":\"wSzpv1rx0k9gCkvGrzXT\",\"scope\":\"accounting.settings accounting.transactions accounting.contacts offline_access\"}";
  private static String BASE_PATH = System.getProperty("user.dir").concat("\\public_html\\resources");
  
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
  }

  @After
  public void tearDown()
    throws Exception
  {
  }

  @BeforeClass
  public static void setUpBeforeClass()
    throws Exception
  {
  }

  @AfterClass
  public static void tearDownAfterClass()
    throws Exception
  {
  }

  /**
   * @see edu.ucla.library.libservices.webservices.ecommerce.utility.handlers.TokenFileHandler#writeTokensFile(String)
   */
  @Test
  public void testWriteTokensFile()
  {
    TokenFileHandler handler;
    String tokensFile;

    tokensFile = BASE_PATH.concat("\\test_secrets.txt");

    handler = new TokenFileHandler();
    handler.setTokensFile(tokensFile);
    handler.writeTokensFile(JSON);
    assert( Files.exists(Paths.get(tokensFile)) );
  }

  /**
   * @see edu.ucla.library.libservices.webservices.ecommerce.utility.handlers.TokenFileHandler#readTokensFile()
   */
  @Test
  public void testReadTokensFile()
  {
    XeroTokenBean result;
    TokenFileHandler handler;
    String tokensFile;

    tokensFile = BASE_PATH.concat("\\default_secrets.txt");

    handler = new TokenFileHandler();
    handler.setTokensFile(tokensFile);
    result = handler.readTokensFile();
    assert(result.getAccess_token().equals("eyJhbGciOiJSUzI1Ni"));
    assert(result.getExpires_in().equals("2025-07-08T16:17:53.186"));
    assert(result.getRefresh_token().equals("wSzpv1rx0k9gCkvGrzXT"));
    assert(result.getScope().equals("accounting.settings accounting.transactions accounting.contacts offline_access"));
  }

  /**
   * @see edu.ucla.library.libservices.webservices.ecommerce.utility.handlers.TokenFileHandler#setTokensFile(String)
   */
  @Test
  public void testSetTokensFile()
  {
    fail("Unimplemented");
  }

  /**
   * @see edu.ucla.library.libservices.webservices.ecommerce.utility.handlers.TokenFileHandler#getTokensFile()
   */
  @Test
  public void testGetTokensFile()
  {
    fail("Unimplemented");
  }
}
