package edu.ucla.library.libservices.webservices.ecommerce.beans;

import java.util.ArrayList;

/**
 * Java bean to model a collection of XeroContact objects
 * Will probably only deal with one contact at a time, but Xero API
 * insists on returning responnses as json arrays
 */
public class XeroContactList
{
  private ArrayList<XeroContact> Contacts;

  public XeroContactList()
  {
    super();
    Contacts = new ArrayList<XeroContact>();
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
