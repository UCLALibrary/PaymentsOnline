package edu.ucla.library.libservices.webservices.ecommerce.utility.db;

import edu.ucla.library.libservices.webservices.ecommerce.beans.CashnetLog;
import edu.ucla.library.libservices.webservices.ecommerce.utility.strings.StringHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DataHandler
{
  private static final Logger LOGGER = LogManager.getLogger( DataHandler.class );
  private static final String LOST_ITEM_REPLACEMENT_FEE = "LOSTITEMREPLACEMENTFEE";
  private static final String OVERDUE_FINE = "OVERDUEFINE";

  private static final String COUNT = "SELECT count(*) AS invoices FROM public.\"ALMA_INVOICE_PATRON\" WHERE \"INVOICE_ID\" = ?";
  private static final String DELETE = "DELETE FROM public.\"ALMA_INVOICE_PATRON\" WHERE \"INVOICE_ID\" = ?";
  private static final String INSERT = "INSERT INTO public.\"ALMA_INVOICE_PATRON\"(\"INVOICE_ID\", \"PATRON_ID\") VALUES(?,?)";
  private static String INSERT_LOG =
    "INSERT INTO public.\"cashnet_log\"(\"ucla_ref_no\", \"result_code\"," +
    " \"cn_trans_no\", \"cn_batch_no\", \"pmt_code\", \"eff_date\", \"cn_details\")" + " VALUES(?, ?, ?, ?, ?, to_date(?, 'MM/DD/YYYY'), ?)";
  private static final String SELECT_ALMA_FEE = "SELECT \"item_code\" FROM public.\"alma_itemcodes\" WHERE \"fine_fee_type\" = ?";
  private static final String SELECT_PATRON =
    "SELECT \"PATRON_ID\" FROM public.\"ALMA_INVOICE_PATRON\" WHERE \"INVOICE_ID\" = ?";

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

  private void makeConnection()
  {
    ds = DataSourceFactory.createDataSource(getDbName());
  }

  public static void saveInvoiceData(String dbName, String invoiceID, String patronID)
  {
    String cleanInvoice = StringHandler.extractInvoiceID(invoiceID);
    try (Connection con = DataSourceFactory.createDataSource(dbName).getConnection())
    {
      PreparedStatement pstmt;
      ResultSet rs;
      int count;
      pstmt = con.prepareStatement(COUNT);
      pstmt.setString(1, cleanInvoice);
      rs = pstmt.executeQuery();
      rs.next();
      count = rs.getInt("invoices");

      if (count == 0)
      {
	    pstmt = con.prepareStatement(INSERT);
        pstmt.setString(1, cleanInvoice);
        pstmt.setString(2, patronID);
        rs = pstmt.executeQuery();
        pstmt.executeUpdate();
      }
	}
    catch (SQLException sqle)
    {
      LOGGER.error("error saving Alma patron/invoice pair: "  + sqle.getMessage());
    }
  }

  public void deleteInvoiceData()
  {
    makeConnection();
    try (Connection con = ds.getConnection())
    {
      PreparedStatement pstmt;
      con.setAutoCommit(false);
      pstmt = con.prepareStatement(DELETE);
      pstmt.setString(1, getInvoiceID());
      pstmt.executeUpdate();

      con.commit();
      pstmt.close();

    }
    catch (SQLException sqle)
    {
      LOGGER.error("error deleting Alma patron/invoice pair: "  + sqle.getMessage());
    }
  }

  public String getPatronData()
  {
    String theID = null;
    makeConnection();
    try (Connection con = ds.getConnection())
    {
      PreparedStatement pstmt;
      ResultSet rs;
      pstmt = con.prepareStatement(SELECT_PATRON);
      pstmt.setString(1, getInvoiceID());
      rs = pstmt.executeQuery();
      rs.next();
      theID = rs.getString("PATRON_ID");
	}
    catch (SQLException sqle)
    {
      LOGGER.error("error retrieving Alma patron/invoice pair: "  + sqle.getMessage());
    }

    return theID;
  }

  public static String getAlmaItemCode(String dbName, String feeType, boolean isLaw, boolean isClicc)
  {
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
      pstmt = con.prepareStatement(SELECT_ALMA_FEE);
      pstmt.setString(1, fineFeeName);
      rs = pstmt.executeQuery();
      rs.next();
      itemCode = rs.getString("item_code");
	}
    catch (SQLException sqle)
    {
      LOGGER.error("error retrieving Alma item code: "  + sqle.getMessage());
      itemCode= null;
    }
    return itemCode;
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
      LOGGER.error("error saving payment log: "  + sqle.getMessage());
    }
  }
}
