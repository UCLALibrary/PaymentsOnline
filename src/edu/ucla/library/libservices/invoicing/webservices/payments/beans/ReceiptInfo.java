package edu.ucla.library.libservices.invoicing.webservices.payments.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "receipt")
@XmlAccessorType( XmlAccessType.FIELD )
public class ReceiptInfo
{
  @XmlElement( name = "status" )
  private String status;
  @XmlElement( name = "userName" )
  private String userName;
  @XmlElement( name = "patronID" )
  private String patronID;
  @XmlElement( name = "uid" )
  private String uid;
  @XmlElement( name = "unpaid" )
  private int unpaid;
  
  public ReceiptInfo()
  {
    super();
  }

  public void setStatus( String status )
  {
    this.status = status;
  }

  public String getStatus()
  {
    return status;
  }

  public void setUserName( String userName )
  {
    this.userName = userName;
  }

  public String getUserName()
  {
    return userName;
  }

  public void setPatronID( String patronID )
  {
    this.patronID = patronID;
  }

  public String getPatronID()
  {
    return patronID;
  }

  public void setUnpaid( int unpaid )
  {
    this.unpaid = unpaid;
  }

  public int getUnpaid()
  {
    return unpaid;
  }

  public void setUid( String uid )
  {
    this.uid = uid;
  }

  public String getUid()
  {
    return uid;
  }
}