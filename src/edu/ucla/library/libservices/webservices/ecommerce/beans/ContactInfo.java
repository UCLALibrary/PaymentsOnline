package edu.ucla.library.libservices.webservices.ecommerce.beans;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "contact_info")
@XmlAccessorType(XmlAccessType.FIELD)
public class ContactInfo
{
  @XmlElement(name = "address")
  private List<Address> addresses;
  @XmlElement(name = "email")
  private List<Email> emails;
  @XmlElement(name = "phone")
  private List<Phone> phones;
  
  public ContactInfo()
  {
    super();
  }

  public void setAddresses(List<Address> addresses)
  {
    this.addresses = addresses;
  }

  public List<Address> getAddresses()
  {
    return addresses;
  }

  public void setEmails(List<Email> emails)
  {
    this.emails = emails;
  }

  public List<Email> getEmails()
  {
    return emails;
  }

  public void setPhones(List<Phone> phones)
  {
    this.phones = phones;
  }

  public List<Phone> getPhones()
  {
    return phones;
  }
}