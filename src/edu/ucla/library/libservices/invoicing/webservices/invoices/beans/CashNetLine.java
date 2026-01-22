package edu.ucla.library.libservices.invoicing.webservices.invoices.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
//import javax.xml.bind.annotation.XmlRootElement;

@XmlType( name = "invoiceLine" )
@XmlAccessorType( XmlAccessType.FIELD )
public class CashNetLine
{
  @XmlElement( name = "invoiceNumber" )
  private String invoiceNumber;
  @XmlElement( name = "itemCode" )
  private String itemCode;
  @XmlElement( name = "totalPrice" )
  private double totalPrice;

  public CashNetLine()
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

  public void setItemCode( String itemCode )
  {
    this.itemCode = itemCode;
  }

  public String getItemCode()
  {
    return itemCode;
  }

  public void setTotalPrice( double totalPrice )
  {
    this.totalPrice = totalPrice;
  }

  public double getTotalPrice()
  {
    return totalPrice;
  }
}