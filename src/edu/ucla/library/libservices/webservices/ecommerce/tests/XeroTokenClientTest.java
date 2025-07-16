package edu.ucla.library.libservices.webservices.ecommerce.tests;

import edu.ucla.library.libservices.webservices.ecommerce.web.clients.XeroTokenClient;

import java.nio.file.Paths;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;

public class XeroTokenClientTest
{
  private static String BASE_PATH = Paths.get(System.getProperty("user.dir"), "public_html", "resources").toString();
  private static String SECRETS_FILE = Paths.get(BASE_PATH, "test_secrets.txt").toString();
  private static String DUMMY_REFRESH = "wSzpv1rx0k9gCkvGrzXTc";

  public XeroTokenClientTest()
  {
  }

  public static void main(String[] args)
  {
    String[] args2 =
    {
      XeroTokenClientTest.class.getName()
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
    fail("Unimplemented");
  }
}
