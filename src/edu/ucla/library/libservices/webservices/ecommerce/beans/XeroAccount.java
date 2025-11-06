package edu.ucla.library.libservices.webservices.ecommerce.beans;

/**
 * Represents an account in Xero
 * Used in LPO to retrieve Transact item codes (stored in Name field)
 */
public class XeroAccount
{
  private String AccountID;
  private String Code;
  private String Name;

  public XeroAccount()
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

  public void setCode(String Code)
  {
    this.Code = Code;
  }

  public String getCode()
  {
    return Code;
  }

  public void setName(String Name)
  {
    this.Name = Name;
  }

  public String getName()
  {
    return Name;
  }
}
