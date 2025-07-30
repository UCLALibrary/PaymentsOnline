package edu.ucla.library.libservices.webservices.ecommerce.beans;

import java.util.ArrayList;
import java.util.HashMap;

public class XeroInvoice
{
  private String InvoiceID;
  private String InvoiceNumber;
  private String AmountDue;
  private XeroContact Contact;
  private String Reference;
  private ArrayList<XeroLineItem> LineItems;
  private HashMap<String, Double> itemCodeAmts;

  public XeroInvoice()
  {
    super();
    LineItems = new ArrayList<>();
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

  public void setReference(String Reference)
  {
    this.Reference = Reference;
  }

  public String getReference()
  {
    return Reference;
  }

  public void setContact(XeroContact Contact)
  {
    this.Contact = Contact;
  }

  public XeroContact getContact()
  {
    return Contact;
  }

  public void setLineItems(ArrayList<XeroLineItem> LineItems)
  {
    this.LineItems = LineItems;
  }

  public ArrayList<XeroLineItem> getLineItems()
  {
    return LineItems;
  }

  public void setItemCodeAmts(HashMap<String, Double> itemCodeAmts)
  {
    this.itemCodeAmts = itemCodeAmts;
  }

  public HashMap<String, Double> getItemCodeAmts()
  {
    return itemCodeAmts;
  }
}
