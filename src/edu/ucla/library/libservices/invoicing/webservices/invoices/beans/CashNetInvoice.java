package edu.ucla.library.libservices.invoicing.webservices.invoices.beans;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement( name = "invoice" )
@XmlAccessorType( XmlAccessType.FIELD )
public class CashNetInvoice
{
  @XmlElement( name = "invoiceNumber" )
  private String invoiceNumber;
  @XmlElement( name = "lines" )
  private List<CashNetLine> lineItems;
  
  public CashNetInvoice()
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

  public void setLineItems( List<CashNetLine> lineItems )
  {
    this.lineItems = lineItems;
  }

  public List<CashNetLine> getLineItems()
  {
    return lineItems;
  }
}