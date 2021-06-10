package edu.ucla.library.libservices.webservices.ecommerce.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlType(name = "type")
@XmlAccessorType( XmlAccessType.FIELD )
public class FeeType
{
  @XmlAttribute(name = "desc")
  private String description;
  @XmlValue
  private String value;

  public FeeType()
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
