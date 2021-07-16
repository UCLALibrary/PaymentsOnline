package edu.ucla.library.libservices.webservices.ecommerce.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "address")
@XmlAccessorType(XmlAccessType.FIELD)
public class Address
{
  @XmlElement(name="preferred")
  private boolean preferred;
  @XmlElement(name = "line1")
  private String line1;
  @XmlElement(name = "line2")
  private String line2;
  @XmlElement(name = "line3")
  private String line3;
  @XmlElement(name = "line4")
  private String line4;
  @XmlElement(name = "line5")
  private String line5;
  @XmlElement(name = "city")
  private String city;
  @XmlElement(name = "state_province")
  private String state;
  @XmlElement(name = "postal_code")
  private String zipCode;
  @XmlElement(name = "country")
  private String country;

  public Address()
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

  public void setLine1(String line1)
  {
    this.line1 = line1;
  }

  public String getLine1()
  {
    return line1;
  }

  public void setLine2(String line2)
  {
    this.line2 = line2;
  }

  public String getLine2()
  {
    return line2;
  }

  public void setLine3(String line3)
  {
    this.line3 = line3;
  }

  public String getLine3()
  {
    return line3;
  }

  public void setLine4(String line4)
  {
    this.line4 = line4;
  }

  public String getLine4()
  {
    return line4;
  }

  public void setLine5(String line5)
  {
    this.line5 = line5;
  }

  public String getLine5()
  {
    return line5;
  }

  public void setCity(String city)
  {
    this.city = city;
  }

  public String getCity()
  {
    return city;
  }

  public void setState(String state)
  {
    this.state = state;
  }

  public String getState()
  {
    return state;
  }

  public void setZipCode(String zipCode)
  {
    this.zipCode = zipCode;
  }

  public String getZipCode()
  {
    return zipCode;
  }

  public void setCountry(String country)
  {
    this.country = country;
  }

  public String getCountry()
  {
    return country;
  }
}
