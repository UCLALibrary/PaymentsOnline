package edu.ucla.library.libservices.webservices.ecommerce.beans;

/**
 * Simple class used for Xero payment json payload
 */
public class XeroPaymentAccount
{
  private String AccountID;

  public XeroPaymentAccount()
  {
    super();
  }

  public void setAccountID(String AccountID)
  {
    this.AccountID = AccountID;
  }

  public String getAccountID()
  {
    return AccountID;
  }
}
