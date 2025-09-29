package edu.ucla.library.libservices.webservices.ecommerce.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;

/**
 * A single invoice, with related XeroContact and XeroLineItem data
 */
public class XeroInvoice
  implements Comparable<XeroInvoice>
{
  //used for sorting, submitting payment
  private String InvoiceID;
  // the user-set(?) invoice number, to be used as pass-through value to Transact
  private String InvoiceNumber;
  // date invoice was issued, displayed in invoices.jsp
  private String Date;
  private Double AmountDue;
  private XeroContact Contact;
  //optional text field, when filled will be displayed in unpaid-invoice list
  private String Reference;
  private ArrayList<XeroLineItem> LineItems;
  // grouped and summed values per Transact item code
  private HashMap<String, Double> itemCodeAmts;
  // grouped and summed values per line-item account ID
  private HashMap<String, Double> accountAmts;

  public XeroInvoice()
  {
    super();
    LineItems = new ArrayList<>();
    itemCodeAmts = new HashMap<>();
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

  public void setAmountDue(Double AmountDue)
  {
    this.AmountDue = AmountDue;
  }

  public Double getAmountDue()
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

  public void setAccountAmts(HashMap<String, Double> accountAmts)
  {
    this.accountAmts = accountAmts;
  }

  public HashMap<String, Double> getAccountAmts()
  {
    return accountAmts;
  }

  public void setDate(String Date)
  {
    this.Date = Date;
  }

  public String getDate()
  {
    return Date;
  }

  @Override
  public boolean equals(Object theOther)
  {
    if (this == theOther)
      return true;
    if (theOther == null || getClass() != theOther.getClass())
      return false;
    XeroInvoice aBean = (XeroInvoice) theOther;

    boolean simpleCompare =
      this.getInvoiceID() == aBean.getInvoiceID() && this.getInvoiceNumber() == aBean.getInvoiceNumber() &&
      this.getAmountDue() == aBean.getAmountDue() && this.getContact().equals(aBean.getContact()) &&
      this.getReference() == aBean.getReference();
    Collections.sort(this.getLineItems());
    Collections.sort(aBean.getLineItems());
    boolean lineCompare = this.getLineItems().equals(aBean.getLineItems());
    boolean amountsCompare =
      this.getItemCodeAmts().equals(aBean.getItemCodeAmts()) && this.getAccountAmts().equals(aBean.getAccountAmts());
    return simpleCompare && lineCompare && amountsCompare;
  }

  @Override
  public int compareTo(XeroInvoice other)
  {
    return this.getInvoiceID().compareTo(other.getInvoiceID());
  }
}
