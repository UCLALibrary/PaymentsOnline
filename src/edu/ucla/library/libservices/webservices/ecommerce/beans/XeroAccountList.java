package edu.ucla.library.libservices.webservices.ecommerce.beans;

import java.util.ArrayList;

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
