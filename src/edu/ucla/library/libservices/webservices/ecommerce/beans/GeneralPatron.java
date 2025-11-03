package edu.ucla.library.libservices.webservices.ecommerce.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "user")
@XmlAccessorType( XmlAccessType.FIELD )
public class GeneralPatron
{
  @XmlElement( name = "primary_id" )
  private String almaID;
  @XmlElement( name = "first_name" )
  private String firstName;
  @XmlElement( name = "last_name" ) 
  private String lastName;
  private String xeroID;

  public GeneralPatron()
  {
    super();
  }

  public void setAlmaID(String patronID)
  {
    this.almaID = patronID;
  }

  public String getAlmaID()
  {
    return almaID;
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

  public void setXeroID(String xeroID)
  {
    this.xeroID = xeroID;
  }

  public String getXeroID()
  {
    return xeroID;
  }
}
