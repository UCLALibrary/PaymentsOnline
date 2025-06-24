package edu.ucla.library.libservices.webservices.ecommerce.beans;

public class XeroContact
{
  private String ContactID;
  private String ContactNumber;
  private String FirstName;
  private String LastName;
  
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
}
