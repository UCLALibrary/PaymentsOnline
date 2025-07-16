package edu.ucla.library.libservices.webservices.ecommerce.tests;

import edu.ucla.library.libservices.webservices.ecommerce.utility.handlers.XeroTokenHandler;

import java.nio.file.Paths;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;

public class XeroTokenHandlerTest
{
  private static String BASE_PATH = Paths.get(System.getProperty("user.dir"), "public_html", "resources").toString();
  private static String TOKENS_FILE = Paths.get(BASE_PATH, "default_secrets.txt").toString();
  private static String SECRETS_FILE = Paths.get(BASE_PATH, "test_secrets.txt").toString();

  public XeroTokenHandlerTest()
  {
  }

  public static void main(String[] args)
  {
    String[] args2 =
    {
      XeroTokenHandlerTest.class.getName()
    };
    JUnitCore.main(args2);
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
   * @see edu.ucla.library.libservices.webservices.ecommerce.utility.handlers.XeroTokenHandler#setSecretsFile(String)
   */
  @Test
  public void testSetGetSecretsFile()
  {
    XeroTokenHandler theHandler;
    theHandler = new XeroTokenHandler();
    theHandler.setSecretsFile(SECRETS_FILE);
    assert(theHandler.getSecretsFile().equals(SECRETS_FILE));
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
    assert(theHandler.getTokensFile().equals(TOKENS_FILE));
  }

  /**
   * @see edu.ucla.library.libservices.webservices.ecommerce.utility.handlers.XeroTokenHandler#getTokens()
   */
  @Test
  public void testGetTokens()
  {
    fail("Unimplemented");
  }
}
