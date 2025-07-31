package edu.ucla.library.libservices.webservices.ecommerce.beans;

public class XeroLineItem
{
  private String ItemCode;
  private String Description;
  private double LineAmount;
  private String AccountCode;
  private String transactItemCode;

  public XeroLineItem()
  {
    super();
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

  public void setAccountCode(String AccountCode)
  {
    this.AccountCode = AccountCode;
  }

  public String getAccountCode()
  {
    return AccountCode;
  }

  public void setTransactItemCode(String transactItemCode)
  {
    this.transactItemCode = transactItemCode;
  }

  public String getTransactItemCode()
  {
    return transactItemCode;
  }
}
