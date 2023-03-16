package edu.ucla.library.libservices.webservices.ecommerce.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "email")
@XmlAccessorType(XmlAccessType.FIELD)
public class Email
{
  @XmlElement(name="email_address")
  private String emailAddress;
  @XmlElement(name="description")
  private String description;
  @XmlElement(name="preferred")
  private boolean preferred;

  public Email()
  {
    super();
  }

  public void setEmailAddress(String emailAddress)
  {
    this.emailAddress = emailAddress;
  }

  public String getEmailAddress()
  {
    return emailAddress;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  public String getDescription()
  {
    return description;
  }

  public void setPreferred(boolean preferred)
  {
    this.preferred = preferred;
  }

  public boolean isPreferred()
  {
    return preferred;
  }
}
