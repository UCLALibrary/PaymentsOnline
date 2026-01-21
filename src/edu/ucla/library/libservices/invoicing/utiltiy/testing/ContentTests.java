package edu.ucla.library.libservices.invoicing.utiltiy.testing;

import edu.ucla.library.libservices.invoicing.utiltiy.db.DataSourceFactory;

import java.util.regex.Pattern;

import org.springframework.jdbc.core.JdbcTemplate;

public class ContentTests
{
  private static final String GET_INVOICE =
    "SELECT COUNT(ivw.invoice_number) FROM invoice_vw ivw WHERE ivw.invoice_number" 
    + " = ? and ivw.patron_id = ?";

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

  public static boolean isLegitInvoice( String invoiceID, String uid,
                                        String dbName )
  {
    return ( 
      new JdbcTemplate( DataSourceFactory.createDataSource( dbName ) //DataSourceFactory.createBillSource()
                        ).queryForInt( 
          GET_INVOICE, new Object[] { invoiceID, uid } ) == 1 );
  }
  
  public static boolean isUID(String value)
  {
    return Pattern.matches( "[0-9]{9}", value );
  }
}
