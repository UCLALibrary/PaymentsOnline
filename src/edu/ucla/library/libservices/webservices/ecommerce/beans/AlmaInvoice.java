package edu.ucla.library.libservices.webservices.ecommerce.beans;

import edu.ucla.library.libservices.webservices.ecommerce.utility.dates.DateConverter;
import edu.ucla.library.libservices.invoicing.webservices.invoices.beans.CashNetLine;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "fee")
@XmlAccessorType(XmlAccessType.FIELD)
public class AlmaInvoice
{
  @XmlElement(name = "id")
  private String invoiceNumber;
  @XmlElement(name = "balance")
  private double balance;
  @XmlElement(name = "title")
  private String title;
  @XmlElement(name = "barcode")
  private String barcode;
  @XmlElement(name = "type")
  private FeeType type;
  @XmlElement(name = "creation_time")
  private String feeDate;
  @XmlElement(name = "owner")
  private String owner;
  @XmlElement(name = "status")
  private String status;
  @XmlElement(name = "lines")
  private List<CashNetLine> lineItems;

  public AlmaInvoice()
  {
    super();
  }

  public void setInvoiceNumber(String id)
  {
    this.invoiceNumber = id;
  }

  public String getInvoiceNumber()
  {
    return invoiceNumber;
  }

  public void setBalance(double balance)
  {
    this.balance = balance;
  }

  public double getBalance()
  {
    return balance;
  }

  public void setTitle(String title)
  {
    this.title = title;
  }

  public String getTitle()
  {
    return title;
  }

  public void setBarcode(String barcode)
  {
    this.barcode = barcode;
  }

  public String getBarcode()
  {
    return barcode;
  }

  public void setType(FeeType type)
  {
    this.type = type;
  }

  public FeeType getType()
  {
    return type;
  }

  public void setFeeDate(String feeDate)
  {
    this.feeDate = feeDate;
  }

  public String getFeeDate()
  {
    return DateConverter.convert(feeDate.substring(0, feeDate.lastIndexOf("T")));
  }

  public void setOwner(String owner)
  {
    this.owner = owner;
  }

  public String getOwner()
  {
    return owner;
  }

  public void setStatus(String statusIn)
  {
    this.status = statusIn;
  }

  public String getStatus()
  {
    if ( status.equalsIgnoreCase("ACTIVE") )
    {
      return "Unpaid";
    }
    else if ( status.equalsIgnoreCase("CLOSED") )
    {
      return "Paid";
    }
    else
    {
      return Character.toUpperCase(status.charAt(0)) + status.substring(1);
    }
  }

  public void setLineItems(List<CashNetLine> lineItems)
  {
    this.lineItems = lineItems;
  }

  public List<CashNetLine> getLineItems()
  {
    return lineItems;
  }
}
