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
}
