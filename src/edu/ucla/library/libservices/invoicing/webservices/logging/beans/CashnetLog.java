package edu.ucla.library.libservices.invoicing.webservices.logging.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
//import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement( name = "cnLog" )
@XmlAccessorType( XmlAccessType.FIELD )
public class CashnetLog
{
  @XmlElement( name = "refNumber" )
  private String refNumber;
  @XmlElement( name = "resultCode" )
  private String resultCode;
  @XmlElement( name = "transNumber" )
  private String transNumber;
  @XmlElement( name = "batchNumber" )
  private String batchNumber;
  @XmlElement( name = "pmtCode" )
  private String pmtCode;
  @XmlElement( name = "effDate" )
  private String effDate;
  @XmlElement( name = "details" )
  private String details;

  public CashnetLog()
  {
    super();
  }

  public void setRefNumber( String refNumber )
  {
    this.refNumber = refNumber;
  }

  public String getRefNumber()
  {
    return refNumber;
  }

  public void setResultCode( String resultCode )
  {
    this.resultCode = resultCode;
  }

  public String getResultCode()
  {
    return resultCode;
  }

  public void setTransNumber( String transNumber )
  {
    this.transNumber = transNumber;
  }

  public String getTransNumber()
  {
    return transNumber;
  }

  public void setBatchNumber( String batchNumber )
  {
    this.batchNumber = batchNumber;
  }

  public String getBatchNumber()
  {
    return batchNumber;
  }

  public void setPmtCode( String pmtCode )
  {
    this.pmtCode = pmtCode;
  }

  public String getPmtCode()
  {
    return pmtCode;
  }

  public void setEffDate( String effDate )
  {
    this.effDate = effDate;
  }

  public String getEffDate()
  {
    return effDate;
  }

  public void setDetails( String details )
  {
    this.details = details;
  }

  public String getDetails()
  {
    return details;
  }
}