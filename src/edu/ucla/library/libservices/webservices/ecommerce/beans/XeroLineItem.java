package edu.ucla.library.libservices.webservices.ecommerce.beans;

import edu.ucla.library.libservices.webservices.ecommerce.web.clients.XeroInvoiceClient;

/**
 * Represents a charged item/service in an invoice
 */
public class XeroLineItem implements Comparable<XeroLineItem>
{
  // needed for compareTo method
  private String LineItemID;
  // passed to Transact, along with Description, for display to patron
  private String ItemCode;
  private String Description;
  // the pr-tax charge for the libne item
  private double LineAmount;
  private double TaxAmount;
  private String AccountCode;
  // used to link to a XeroAccount object
  private String AccountID;
  // Transact code retrieved from an account record (cf XeroAccount)
  private String transactItemCode;

  public XeroLineItem()
  {
    super();
  }

  public void setLineItemID(String LineItemID)
  {
    this.LineItemID = LineItemID;
  }

  public String getLineItemID()
  {
    return LineItemID;
  }

  public void setItemCode(String ItemCode)
  {
    this.ItemCode = ItemCode;
  }

  public String getItemCode()
  {
    return ItemCode;
  }

  public void setDescription(String Description)
  {
    this.Description = Description;
  }

  public String getDescription()
  {
    return Description;
  }

  public void setLineAmount(double LineAmount)
  {
    this.LineAmount = LineAmount;
  }

  public double getLineAmount()
  {
    return LineAmount;
  }

  public void setTaxAmount(double TaxAmount)
  {
    this.TaxAmount = TaxAmount;
  }

  public double getTaxAmount()
  {
    return TaxAmount;
  }

  public void setAccountCode(String AccountCode)
  {
    this.AccountCode = AccountCode;
  }

  public String getAccountCode()
  {
    return AccountCode;
  }

  public void setAccountID(String AccountID)
  {
    this.AccountID = AccountID;
  }

  public String getAccountID()
  {
    return AccountID;
  }

  public void setTransactItemCode(String transactItemCode)
  {
    this.transactItemCode = transactItemCode;
  }

  public String getTransactItemCode()
  {
    return transactItemCode;
  }

  /**
   * Multiply the sum by 100 to head-off floating point
   * rounding errors in XeroInvoiceClient.groupdAndSumCodes()
   * @return sum of base line amount and tax, if any
   */
  public double getLineTotal()
  {
    return (getLineAmount() + getTaxAmount()) * 100d;
  }

  @Override
  public boolean equals(Object theOther)
  {
    if (this == theOther)
      return true;
    if (theOther == null || getClass() != theOther.getClass())
      return false;
    XeroLineItem aBean = (XeroLineItem) theOther;

    return this.getItemCode() == aBean.getItemCode() && this.getDescription() == aBean.getDescription() &&
           this.getLineAmount() == aBean.getLineAmount() && this.getTaxAmount() == aBean.getTaxAmount() &&
           this.getAccountCode() == aBean.getAccountCode() && this.getAccountID() == aBean.getAccountID() &&
           this.getTransactItemCode() == aBean.getTransactItemCode();
  }

  @Override
  public int compareTo(XeroLineItem otherLine)
  {
    return this.getLineItemID().compareTo(otherLine.getLineItemID());
  }
}
