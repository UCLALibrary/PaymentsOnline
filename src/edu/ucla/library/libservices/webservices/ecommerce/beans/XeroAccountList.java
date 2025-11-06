package edu.ucla.library.libservices.webservices.ecommerce.beans;

import java.util.ArrayList;

/**
 * Holds a collection of XeroAccount objects (since Xero API returns everything
 * as json array)
 */
public class XeroAccountList
{
  private ArrayList<XeroAccount> Accounts;
  
  public XeroAccountList()
  {
    super();
    Accounts = new ArrayList<>();
  }

  public void setAccounts(ArrayList<XeroAccount> Accounts)
  {
    this.Accounts = Accounts;
  }

  public ArrayList<XeroAccount> getAccounts()
  {
    return Accounts;
  }
}
