package edu.ucla.library.libservices.webservices.ecommerce.tests;

import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroPayment;
import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroPaymentAccount;
import edu.ucla.library.libservices.webservices.ecommerce.beans.XeroPaymentInvoice;
import edu.ucla.library.libservices.webservices.ecommerce.web.clients.XeroPaymentClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.Before;
import org.junit.Test;

public class XeroPaymentClientTest
{
  private static XeroPayment mockPayment;

  @Before
  public void setUp()
    throws Exception
  {
    XeroPaymentInvoice invoice;
    XeroPaymentAccount account;

    invoice = new XeroPaymentInvoice();
    invoice.setInvoiceID("123-456");

    account = new XeroPaymentAccount();
    account.setAccountID("987-654");

    mockPayment = new XeroPayment();
    mockPayment.setAccount(account);
    mockPayment.setAmount(200.00d);
    mockPayment.setDate(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
    mockPayment.setInvoice(invoice);
    mockPayment.setReference("Reference");
  }

  /**
   * @see edu.ucla.library.libservices.webservices.ecommerce.web.clients.XeroPaymentClient#setInvoiceNumber(String)
   */
  @Test
  public void testSetGetInvoiceNumber()
  {
    XeroPaymentClient testClient;
    testClient = new XeroPaymentClient();
    testClient.setInvoiceNumber("INV-123");
    assert(testClient.getInvoiceNumber().equals("INV-123"));
  }

  /**
   * @see edu.ucla.library.libservices.webservices.ecommerce.web.clients.XeroPaymentClient#setReference(String)
   */
  @Test
  public void testSetGetReference()
  {
    XeroPaymentClient testClient;
    testClient = new XeroPaymentClient();
    testClient.setReference("reference");
    assert(testClient.getReference().equals("reference"));
  }

  /**
   * @see edu.ucla.library.libservices.webservices.ecommerce.web.clients.XeroPaymentClient#putPayment()
   */
  @Test
  public void testBuildPayment()
  {
    XeroPaymentClient testClient;
    XeroPayment testPayment;
    testClient = new XeroPaymentClient();
    testClient.setReference("Reference");
    
    testPayment = testClient.buildPayment("987-654", "123-456", 20000.00d);
    assert(testPayment.equals(mockPayment));
  }
}
