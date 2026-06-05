package edu.ucla.library.libservices.invoicing.utility.adapters;

import java.text.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DateAdapter
  extends XmlAdapter<String, Date>
{
  private static final DateFormat df = new SimpleDateFormat( "MM/dd/yyyy" );

  public DateAdapter()
  {
    super();
  }

  public Date unmarshal( String v )
    throws ParseException
  {
    return df.parse( v );
  }

  public String marshal( Date v )
  {
    return df.format( v );
  }
}