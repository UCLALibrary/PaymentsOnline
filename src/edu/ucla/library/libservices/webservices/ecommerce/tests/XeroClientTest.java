package edu.ucla.library.libservices.webservices.ecommerce.tests;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.ucla.library.libservices.webservices.ecommerce.web.clients.XeroContactClient;

public class XeroClientTest
{
  public XeroClientTest()
  {
  }

  public static void main(String[] args)
  {
    String[] args2 =
    {
      XeroClientTest.class.getName()
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
   * @see edu.ucla.library.libservices.webservices.ecommerce.web.clients.XeroContactClient#getTheContact()
   */
  @Test
  public void testGetTheContact()
  {
    XeroContactClient testClient = new XeroContactClient();
    
    fail("Unimplemented");
  }
}
