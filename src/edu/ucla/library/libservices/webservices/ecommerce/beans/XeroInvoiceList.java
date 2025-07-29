package edu.ucla.library.libservices.webservices.ecommerce.beans;

import java.util.ArrayList;

public class XeroInvoiceList
{
  private ArrayList<XeroInvoice> Invoices;

  public XeroInvoiceList()
  {
    super();
    Invoices = new ArrayList<XeroInvoice>();
  }

  public void setInvoices(ArrayList<XeroInvoice> Invoices)
  {
    this.Invoices = Invoices;
  }

  public ArrayList<XeroInvoice> getInvoices()
  {
    return Invoices;
  }
}
