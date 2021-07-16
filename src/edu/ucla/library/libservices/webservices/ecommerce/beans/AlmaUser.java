package edu.ucla.library.libservices.webservices.ecommerce.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "user")
@XmlAccessorType( XmlAccessType.FIELD )
public class AlmaUser
{
  @XmlElement( name = "primary_id" )
  private String patronID;
  @XmlElement( name = "first_name" )
  private String firstName;
  @XmlElement( name = "last_name" ) 
  private String lastName;
  @XmlElement( name = "contact_info" )
  private ContactInfo contactInfo;
 
  public AlmaUser()
  {
    super();
  }

  public void setPatronID(String patronID)
  {
    this.patronID = patronID;
  }

  public String getPatronID()
  {
    return patronID;
  }

  public void setFirstName(String firstName)
  {
    this.firstName = firstName;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public void setLastName(String lastName)
  {
    this.lastName = lastName;
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setContactInfo(ContactInfo contactInfo)
  {
    this.contactInfo = contactInfo;
  }

  public ContactInfo getContactInfo()
  {
    return contactInfo;
  }
}
