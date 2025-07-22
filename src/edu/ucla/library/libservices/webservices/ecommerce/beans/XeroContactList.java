package edu.ucla.library.libservices.webservices.ecommerce.beans;

import java.util.ArrayList;

public class XeroContactList
{
  private ArrayList<XeroContact> Contacts;

  public XeroContactList()
  {
    super();
  }

  public void setContacts(ArrayList<XeroContact> Contacts)
  {
    this.Contacts = Contacts;
  }

  public ArrayList<XeroContact> getContacts()
  {
    return Contacts;
  }
}
