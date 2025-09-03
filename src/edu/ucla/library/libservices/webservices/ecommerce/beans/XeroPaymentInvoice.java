package edu.ucla.library.libservices.webservices.ecommerce.beans;

/**
 * Simple class used for Xero payment json payload
 */
public class XeroPaymentInvoice
{
  private String InvoiceID;

  public XeroPaymentInvoice()
  {
    super();
  }

  public void setInvoiceID(String InvoiceID)
  {
    this.InvoiceID = InvoiceID;
  }

  public String getInvoiceID()
  {
    return InvoiceID;
  }
}
