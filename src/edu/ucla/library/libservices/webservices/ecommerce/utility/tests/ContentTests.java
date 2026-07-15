package edu.ucla.library.libservices.webservices.ecommerce.utility.tests;

import edu.ucla.library.libservices.webservices.ecommerce.utility.db.DataSourceFactory;

import java.util.regex.Pattern;

import org.springframework.jdbc.core.JdbcTemplate;

public class ContentTests
{
  public ContentTests()
  {
    super();
  }

  public static boolean isEmpty( String value )
  {
    return ( value == null || value.trim().equalsIgnoreCase( "" ) ||
             value.trim().length() == 0 );
  }

  public static boolean isEmpty( Object value )
  {
    return ( value == null || value.toString().equalsIgnoreCase( "" ) ||
             value.toString().length() == 0 );
  }

  public static boolean isUID(String value)
  {
    return Pattern.matches( "[0-9]{9}", value );
  }
}
