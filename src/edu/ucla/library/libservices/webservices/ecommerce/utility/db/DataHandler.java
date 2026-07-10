package edu.ucla.library.libservices.webservices.ecommerce.utility.db;

import edu.ucla.library.libservices.invoicing.utility.db.DataSourceFactory;
import edu.ucla.library.libservices.invoicing.webservices.logging.beans.CashnetLog;
import edu.ucla.library.libservices.webservices.ecommerce.utility.strings.StringHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.jdbc.core.JdbcTemplate;

public class DataHandler
{
  private static final Logger LOGGER = LogManager.getLogger( DataHandler.class );
  private static final String LOST_ITEM_REPLACEMENT_FEE = "LOSTITEMREPLACEMENTFEE";
  private static final String OVERDUE_FINE = "OVERDUEFINE";

  private static final String COUNT = "SELECT count(*) FROM public.\"ALMA_INVOICE_PATRON\" WHERE \"INVOICE_ID\" = ?";
  private static final String DELETE = "DELETE FROM public.\"ALMA_INVOICE_PATRON\" WHERE \"INVOICE_ID\" = ?";
  private static final String INSERT = "INSERT INTO public.\"ALMA_INVOICE_PATRON\"(\"INVOICE_ID\", \"PATRON_ID\") VALUES(?,?)";
  private static String INSERT_LOG =
    "INSERT INTO public.\"cashnet_log\"(\"ucla_ref_no\", \"result_code\"," +
    " \"cn_trans_no\", \"cn_batch_no\", \"pmt_code\", \"eff_date\", \"cn_details\")" + " VALUES(?, ?, ?, ?, ?, ?, ?)";
  private static final String SELECT_ALMA_FEE = "SELECT \"item_code\" FROM public.\"alma_itemcodes\" WHERE \"fine_fee_type\" = ?";
  private static final String SELECT_FEE = "SELECT item_code FROM invoice_owner.location_service_vw WHERE service_name = ?";
  private static final String SELECT_FEE_LAW =
    "SELECT item_code FROM invoice_owner.location_service_vw WHERE service_name = ? || ' LAW'";
  private static final String SELECT_FEE_CLICC =
    "SELECT item_code FROM invoice_owner.location_service_vw WHERE service_name = ? || ' CLICC'";
  private static final String SELECT_PATRON =
    "SELECT \"PATRON_ID\" FROM public.\"ALMA_INVOICE_PATRON\" WHERE \"INVOICE_ID\" = ?";
  private static final String UNPAID =
    "SELECT COUNT(invoice_number) FROM invoice_vw WHERE patron_id = ? AND status IN ('Partially Paid','Unpaid',"
    + "'Deposit Due','Final Payment Due')";
  private static final String OVERDUEFINE = "OVERDUEFINE";

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
    if (Integer.valueOf( new JdbcTemplate(source).queryForObject(COUNT, new Object[] { cleanInvoice }, String.class) ) == 0)
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

  public static String getAlmaItemCode(String dbName, String feeType, boolean isLaw, boolean isClicc)
  {
    String query;
    String fineFeeName;
    String itemCode;
    if ( isLaw && feeType.equals(LOST_ITEM_REPLACEMENT_FEE) )
    {
      fineFeeName = feeType.concat("_LAW");
    }
    else if ( isClicc && (feeType.equals(LOST_ITEM_REPLACEMENT_FEE) || feeType.equals(OVERDUE_FINE)) )
    {
      fineFeeName = feeType.concat("_CLICC");;
    }
    else
    {
      fineFeeName = feeType;
    }
    try (Connection con = DataSourceFactory.createDataSource(dbName).getConnection())
    {
      PreparedStatement pstmt;
      ResultSet rs;
      con.setAutoCommit(false);
      pstmt = con.prepareStatement(SELECT_ALMA_FEE);
      pstmt.setString(1, fineFeeName);
      rs = pstmt.executeQuery();
      rs.next();
      itemCode = rs.getString("item_code");
	}
    catch (SQLException sqle)
    {
      sqle.printStackTrace();
      itemCode= null;
    }
    return itemCode;
  }

  public int getUnpaidCount()
  {
    makeConnection();
    return Integer.valueOf( new JdbcTemplate(ds).queryForObject(UNPAID, new Object[] { getPatronID() }, String.class) );
  }

  public static void logCashnetMessage(CashnetLog data, String dbName)
  {
    try (Connection con = DataSourceFactory.createDataSource(dbName).getConnection())
    {
      PreparedStatement pstmt;
      con.setAutoCommit(false);
      pstmt = con.prepareStatement(INSERT_LOG);
      pstmt.setString(1, data.getRefNumber());
      pstmt.setString(2, data.getResultCode());
      pstmt.setString(3, data.getTransNumber());
      pstmt.setString(4, data.getBatchNumber());
      pstmt.setString(5, data.getPmtCode());
      pstmt.setString(6, data.getEffDate());
      pstmt.setString(7, data.getDetails());
      pstmt.executeUpdate();

      con.commit();
      pstmt.close();

    }
    catch (SQLException sqle)
    {
      sqle.printStackTrace();
    }
  }
}
