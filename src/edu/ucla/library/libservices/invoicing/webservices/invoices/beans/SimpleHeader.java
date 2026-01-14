package edu.ucla.library.libservices.invoicing.webservices.invoices.beans;

import edu.ucla.library.libservices.invoicing.utiltiy.adapters.DateAdapter;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement( name = "invoice" )
@XmlAccessorType( XmlAccessType.FIELD )
public class SimpleHeader
{
  @XmlElement( name = "invoiceNumber" )
  private String invoiceNumber;
  @XmlElement( name = "invoiceDate" )
  @XmlJavaTypeAdapter( DateAdapter.class )
  private Date invoiceDate;
  @XmlElement( name = "balanceDue" )
  private double balanceDue;
  @XmlElement( name = "locationName" )
  private String locationName;

  public SimpleHeader()
  {
    super();
  }

  public void setInvoiceNumber( String invoiceNumber )
  {
    this.invoiceNumber = invoiceNumber;
  }

  public String getInvoiceNumber()
  {
    return invoiceNumber;
  }

  public void setInvoiceDate( Date invoiceDate )
  {
    this.invoiceDate = invoiceDate;
  }

  public Date getInvoiceDate()
  {
    return invoiceDate;
  }

  public void setBalanceDue( double balanceDue )
  {
    this.balanceDue = balanceDue;
  }

  public double getBalanceDue()
  {
    return balanceDue;
  }

  public void setLocationName( String locationName )
  {
    this.locationName = locationName;
  }

  public String getLocationName()
  {
    return locationName;
  }
}