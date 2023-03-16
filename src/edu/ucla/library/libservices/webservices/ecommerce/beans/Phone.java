package edu.ucla.library.libservices.webservices.ecommerce.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "phone")
@XmlAccessorType(XmlAccessType.FIELD)
public class Phone
{
  @XmlElement(name="preferred")
  private boolean preferred;
  @XmlElement(name="phone_number")
  private String phoneNumber;

  public Phone()
  {
    super();
  }

  public void setPreferred(boolean preferred)
  {
    this.preferred = preferred;
  }

  public boolean isPreferred()
  {
    return preferred;
  }

  public void setPhoneNumber(String phoneNumber)
  {
    this.phoneNumber = phoneNumber;
  }

  public String getPhoneNumber()
  {
    return phoneNumber;
  }
}
