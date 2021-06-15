package edu.ucla.library.libservices.webservices.ecommerce.utility.db;

import edu.ucla.library.libservices.invoicing.utiltiy.db.DataSourceFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class DataHandler
{
  private static final String INSERT = "INSERT INTO vger_support.alma_invoice_patron(invoice_id,patron_id) VALUES(?,?)";
  private static final String SELECT_PATRON = "SELECT patron_id FROM vger_support.alma_invoice_patron WHERE invoice_id = ?";
  private static final String SELECT_FEE = "SELECT item_code FROM some_table WHERE fee_type = ?";
  
  private DataSource ds;
  private String dbName;
  private String invoiceID;
  private String patronID;
  private String feeType;

  public DataHandler()
  {
    super();
  }

  public void setDbName(String dbName)
  {
    this.dbName = dbName;
  }

  private String getDbName()
  {
    return dbName;
  }

  public void setInvoiceID(String invoiceID)
  {
    this.invoiceID = invoiceID;
  }

  private String getInvoiceID()
  {
    return invoiceID;
  }

  public void setPatronID(String patronID)
  {
    this.patronID = patronID;
  }

  private String getPatronID()
  {
    return patronID;
  }

  public void setFeeType(String feeType)
  {
    this.feeType = feeType;
  }

  private String getFeeType()
  {
    return feeType;
  }

  private void makeConnection()
  {
    //ds = DataSourceFactory.createDataSource( getDbName() );
    ds = DataSourceFactory.createVgerSource();
  }

  public static void saveInvoiceData(String dbName, String invoiceID, String patronID)
  {
    //DataSource source = DataSourceFactory.createDataSource( dbName );
    DataSource source = DataSourceFactory.createVgerSource();
    new JdbcTemplate( source ).update(INSERT, new Object[]{invoiceID, patronID});
  }
  
  public String getPatronData()
  {
    String theID = null;
    makeConnection();
    theID = new JdbcTemplate( ds ).queryForObject(SELECT_PATRON, new Object[]{getInvoiceID()}, String.class).toString();
    return theID;
  }
  
  public static String getfeeData(String dbName, String feeType)
  {
    //DataSource source = DataSourceFactory.createDataSource( dbName );
    DataSource source = DataSourceFactory.createVgerSource();
    return new JdbcTemplate( source ).queryForObject(SELECT_FEE, new Object[]{feeType}, String.class).toString();
  }
}
