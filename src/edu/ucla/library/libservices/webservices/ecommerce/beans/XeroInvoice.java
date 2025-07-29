package edu.ucla.library.libservices.webservices.ecommerce.beans;

public class XeroInvoice
{
  private String InvoiceID;
  private String InvoiceNumber;
  private String AmountDue;
  private String ContactID;
  private String Reference;

  public XeroInvoice()
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

  public void setInvoiceNumber(String InvoiceNumber)
  {
    this.InvoiceNumber = InvoiceNumber;
  }

  public String getInvoiceNumber()
  {
    return InvoiceNumber;
  }

  public void setAmountDue(String AmountDue)
  {
    this.AmountDue = AmountDue;
  }

  public String getAmountDue()
  {
    return AmountDue;
  }

  public void setContactID(String ContactID)
  {
    this.ContactID = ContactID;
  }

  public String getContactID()
  {
    return ContactID;
  }

  public void setReference(String Reference)
  {
    this.Reference = Reference;
  }

  public String getReference()
  {
    return Reference;
  }
}
