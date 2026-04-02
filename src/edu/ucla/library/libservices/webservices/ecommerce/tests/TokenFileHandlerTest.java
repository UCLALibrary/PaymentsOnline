package edu.ucla.library.libservices.webservices.ecommerce.tests;

import com.google.gson.Gson;

import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroTokenBean;
import edu.ucla.library.libservices.webservices.ecommerce.utility.handlers.TokenFileHandler;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TokenFileHandlerTest
{
  private static XeroTokenBean SOURCE_BEAN;
  private static XeroTokenBean OUT_BEAN;

  @Before
  public void setUp()
    throws Exception
  {
    TestUtilities.writeFutureFile();
    SOURCE_BEAN = TestUtilities.populateBean("1800", true);
    OUT_BEAN = TestUtilities.populateBean("1800", false);
  }

  @After
  public void tearDown()
    throws Exception
  {
    TestUtilities.clearFiles();
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
    outputFile = TestUtilities.getWriteFile();

    handler = new TokenFileHandler();
    handler.setTokensFile(outputFile);
    handler.writeTokensFile(gson.toJson(OUT_BEAN));
    Assert.assertTrue (Files.exists(Paths.get(outputFile)));
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
    handler.setTokensFile(TestUtilities.getFutureFile());
    result = handler.readTokensFile();
    
    Assert.assertTrue (result.equals(SOURCE_BEAN));
  }

  /**
   * @see edu.ucla.library.libservices.webservices.ecommerce.utility.handlers.TokenFileHandler#setTokensFile(String)
   */
  @Test
  public void testSetGetTokensFile()
  {
    TokenFileHandler handler;

    handler = new TokenFileHandler();
    handler.setTokensFile(TestUtilities.getFutureFile());
    Assert.assertTrue (handler.getTokensFile().equals(TestUtilities.getFutureFile()));
  }

}