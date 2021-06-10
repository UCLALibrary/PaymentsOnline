package edu.ucla.library.libservices.webservices.ecommerce.beans;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType( XmlAccessType.FIELD )
@XmlRootElement( name = "fees" )
public class AlmaFees
{
  @XmlElement( name = "fee" )
  private List<AlmaInvoice> fees;
  
  public AlmaFees()
  {
    super();
  }

  public void setFees(List<AlmaInvoice> fees)
  {
    this.fees = fees;
  }

  public List<AlmaInvoice> getFees()
  {
    return fees;
  }
}
