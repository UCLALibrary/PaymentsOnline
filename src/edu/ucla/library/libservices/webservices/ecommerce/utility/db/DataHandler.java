package edu.ucla.library.libservices.webservices.ecommerce.utility.db;

import edu.ucla.library.libservices.invoicing.utiltiy.db.DataSourceFactory;

import edu.ucla.library.libservices.webservices.ecommerce.utility.strings.StringHandler;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import org.apache.log4j.Logger;

public class DataHandler
{
  private static final Logger LOGGER = Logger.getLogger( DataHandler.class );

  private static final String COUNT = "SELECT count(*) FROM vger_support.alma_invoice_patron WHERE invoice_id = ?";
  private static final String DELETE = "DELETE FROM vger_support.alma_invoice_patron WHERE invoice_id = ?";
  private static final String INSERT = "INSERT INTO vger_support.alma_invoice_patron(invoice_id,patron_id) VALUES(?,?)";
  private static final String SELECT_FEE = "SELECT item_code FROM invoice_owner.location_service_vw WHERE service_name = ?";
  private static final String SELECT_FEE_LAW =
    "SELECT item_code FROM invoice_owner.location_service_vw WHERE service_name = ? || ' LAW'";
  private static final String SELECT_PATRON =
    "SELECT patron_id FROM vger_support.alma_invoice_patron WHERE invoice_id = ?";
  private static final String UNPAID =
    "SELECT COUNT(invoice_number) FROM invoice_vw WHERE patron_id = ? AND status IN ('Partially Paid','Unpaid'," 
    + "'Deposit Due','Final Payment Due')";

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

  /*private String getFeeType()
  {
    return feeType;
  }*/

  private void makeConnection()
  {
    ds = DataSourceFactory.createDataSource(getDbName());
    //ds = DataSourceFactory.createBillSource(); //.createVgerSource();
  }

  public static void saveInvoiceData(String dbName, String invoiceID, String patronID)
  {
    DataSource source = DataSourceFactory.createDataSource(dbName);
    //DataSource source = DataSourceFactory.createVgerSource();
    String cleanInvoice = StringHandler.extractInvoiceID(invoiceID);
    if (new JdbcTemplate(source).queryForInt(COUNT, new Object[] { cleanInvoice }) == 0)
    {
      new JdbcTemplate(source).update(INSERT, new Object[] { cleanInvoice, patronID });
    }
  }

  public void deleteInvoiceData()
  {
    makeConnection();
    new JdbcTemplate(ds).update(DELETE, new Object[] { getInvoiceID() });
  }

  public String getPatronData()
  {
    String theID = null;
    makeConnection();
    //LOGGER.info(SELECT_PATRON.replace("?", "'" + getInvoiceID() + "'"));
    theID =
      new JdbcTemplate(ds).queryForObject(SELECT_PATRON, new Object[] { getInvoiceID() }, String.class).toString();
    return theID;
  }

  public static String getfeeData(String dbName, String feeType, boolean isLaw)
  {
    String query = ( isLaw ? SELECT_FEE_LAW : SELECT_FEE );
    DataSource source = DataSourceFactory.createDataSource(dbName);
    //DataSource source = DataSourceFactory.createBillSource();
    LOGGER.info(query.replace("?", feeType));
    System.out.println(query.replace("?", feeType));
    return new JdbcTemplate(source).queryForObject(query, new Object[] { feeType }, String.class).toString();
  }

  public int getUnpaidCount()
  {
    makeConnection();
    return new JdbcTemplate(ds).queryForInt(UNPAID, new Object[] { getPatronID() });
  }
}
