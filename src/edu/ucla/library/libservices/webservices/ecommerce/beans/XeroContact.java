package edu.ucla.library.libservices.webservices.ecommerce.beans;

/**
 * Java bean model of a contact (customer/patron) from Xero
 */
public class XeroContact
{
  private String ContactID;
  // per Xero docs, "used to identify contacts in external systems"
  private String ContactNumber;
  private String Name;
  private String FirstName;
  private String LastName;
  private String AccountNumber;

  public XeroContact()
  {
    super();
  }

  public void setContactID(String ContactID)
  {
    this.ContactID = ContactID;
  }

  public String getContactID()
  {
    return ContactID;
  }

  public void setContactNumber(String ContactNumber)
  {
    this.ContactNumber = ContactNumber;
  }

  public String getContactNumber()
  {
    return ContactNumber;
  }

  public void setFirstName(String FirstName)
  {
    this.FirstName = FirstName;
  }

  public String getFirstName()
  {
    return FirstName;
  }

  public void setLastName(String LastName)
  {
    this.LastName = LastName;
  }

  public String getLastName()
  {
    return LastName;
  }

  public void setName(String Name)
  {
    this.Name = Name;
  }

  public String getName()
  {
    return Name;
  }

  public void setAccountNumber(String AccountNumber)
  {
    this.AccountNumber = AccountNumber;
  }

  public String getAccountNumber()
  {
    return AccountNumber;
  }

  @Override
  public boolean equals(Object theOther)
  {
    if (this == theOther)
      return true;
    if (theOther == null || getClass() != theOther.getClass())
      return false;
    XeroContact aBean = (XeroContact) theOther;

    return this.getContactID() == aBean.getContactID() && this.getContactNumber() == aBean.getContactNumber() &&
           this.getFirstName() == aBean.getFirstName() && this.getLastName() == aBean.getLastName() &&
           this.getName() == aBean.getName();
  }
}
