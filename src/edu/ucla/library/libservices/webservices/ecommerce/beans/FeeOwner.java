package edu.ucla.library.libservices.webservices.ecommerce.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "owner")
@XmlAccessorType( XmlAccessType.FIELD )
public class FeeOwner
{
  @XmlAttribute(name = "desc")
  private String description;
  @XmlAttribute(name = "value")
  private String value;

  public FeeOwner()
  {
    super();
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  public String getDescription()
  {
    return description;
  }

  public void setValue(String value)
  {
    this.value = value;
  }

  public String getValue()
  {
    return value;
  }
}
