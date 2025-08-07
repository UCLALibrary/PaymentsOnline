package edu.ucla.library.libservices.webservices.ecommerce.beans;

import java.util.ArrayList;
import java.util.Collections;

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

  @Override
  public boolean equals(Object theOther)
  {
    if (this == theOther)
      return true;
    if (theOther == null || getClass() != theOther.getClass())
      return false;
    XeroInvoiceList aBean = (XeroInvoiceList) theOther;
    Collections.sort(this.getInvoices());
    Collections.sort(aBean.getInvoices());
    
    return this.getInvoices().equals(aBean.getInvoices());
  }
}
