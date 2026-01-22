package edu.ucla.library.libservices.invoicing.webservices.patrons.beans;

import edu.ucla.library.libservices.invoicing.utiltiy.strings.StringCleaner;
import edu.ucla.library.libservices.invoicing.utiltiy.testing.ContentTests;

import edu.ucla.library.libservices.invoicing.webservices.invoices.beans.SimpleHeader;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "patron")
@XmlAccessorType( XmlAccessType.FIELD )
public class SimplePatron
{
  @XmlElement( name = "patronID" )
  private String patronID;
  @XmlElement( name = "barcode" )
  private String barcode;
  @XmlElement( name = "lastName" )
  private String lastName;
  @XmlElement( name = "firstName" )
  private String firstName;
  @XmlElement( name = "ucMember" )
  private boolean isUC;
  @XmlElement( name = "email" )
  private String email;
  @XmlElement( name = "institutionID" )
  private String institutionID;
  @XmlElement( name = "invoiceIDs" )
  private List<SimpleHeader> invoices;
  @XmlElement( name = "primaryID" )
  private String primaryID;

  public SimplePatron()
  {
    super();
    lastName = null;
  }

  public void setPatronID( String patronID )
  {
    this.patronID = patronID;
  }

  public String getPatronID()
  {
    return patronID;
  }

  public void setBarcode( String barcode )
  {
    this.barcode =
        ( !ContentTests.isEmpty( barcode ) ? StringCleaner.removeControlChars( barcode ):
          "" );
  }

  public String getBarcode()
  {
    return barcode;
  }

  public void setLastName( String lastName )
  {
    this.lastName =
        ( !ContentTests.isEmpty( lastName ) ? StringCleaner.removeControlChars( lastName ):
          "" );
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setFirstName( String firstName )
  {
    this.firstName =
        ( !ContentTests.isEmpty( firstName ) ? StringCleaner.removeControlChars( firstName ):
          "" );
  }

  public String getFirstName()
  {
    return firstName;
  }

  public void setIsUC( boolean isUC )
  {
    this.isUC = isUC;
  }

  public boolean isIsUC()
  {
    return isUC;
  }

  public void setEmail( String email )
  {
    this.email =
        ( !ContentTests.isEmpty( email ) ? StringCleaner.removeControlChars( email ):
          "" );
  }

  public String getEmail()
  {
    return email;
  }

  public void setInstitutionID( String institutionID )
  {
    this.institutionID =
        ( !ContentTests.isEmpty( institutionID ) ? StringCleaner.removeControlChars( institutionID ):
          "" );
  }

  public String getInstitutionID()
  {
    return institutionID;
  }

  public void setInvoices( List<SimpleHeader> invoices )
  {
    this.invoices = invoices;
  }

  public List<SimpleHeader> getInvoices()
  {
    return invoices;
  }

  public void setPrimaryID(String primaryID)
  {
    this.primaryID = primaryID;
  }

  public String getPrimaryID()
  {
    return primaryID;
  }
}