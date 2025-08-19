package edu.ucla.library.libservices.webservices.ecommerce.beans;

/**
 * Holds data for a single payment to an invoice/account combo 
 */
public class XeroPayment
{
  private XeroPaymentInvoice Invoice;
  private XeroPaymentAccount Account;
  private String Date;
  private Double Amount;
  private String Reference;
  
    public XeroPayment()
  {
    super();
  }

  public void setInvoice(XeroPaymentInvoice Invoice)
  {
    this.Invoice = Invoice;
  }

  public XeroPaymentInvoice getInvoice()
  {
    return Invoice;
  }

  public void setAccount(XeroPaymentAccount Account)
  {
    this.Account = Account;
  }

  public XeroPaymentAccount getAccount()
  {
    return Account;
  }

  public void setDate(String Date)
  {
    this.Date = Date;
  }

  public String getDate()
  {
    return Date;
  }

  public void setAmount(Double Amount)
  {
    this.Amount = Amount;
  }

  public Double getAmount()
  {
    return Amount;
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
