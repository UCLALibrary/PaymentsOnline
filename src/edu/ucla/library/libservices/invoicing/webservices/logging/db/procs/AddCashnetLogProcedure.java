package edu.ucla.library.libservices.invoicing.webservices.logging.db.procs;

import edu.ucla.library.libservices.invoicing.utiltiy.db.DataSourceFactory;
import edu.ucla.library.libservices.invoicing.webservices.logging.beans.CashnetLog;
import edu.ucla.library.libservices.invoicing.utiltiy.adapters.DateAdapter;

import java.sql.Types;

import java.text.ParseException;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.object.StoredProcedure;

public class AddCashnetLogProcedure
  extends StoredProcedure
{
  private CashnetLog data;
  //private DriverManagerDataSource ds;
  private DataSource ds;
  private String dbName;
  private String user;

  public AddCashnetLogProcedure( JdbcTemplate jdbcTemplate, String string )
  {
    super( jdbcTemplate, string );
  }

  public AddCashnetLogProcedure( DataSource dataSource, String string )
  {
    super( dataSource, string );
  }

  public AddCashnetLogProcedure()
  {
    super();
  }

  public void setData( CashnetLog data )
  {
    this.data = data;
  }

  private CashnetLog getData()
  {
    return data;
  }

  public void setDbName( String dbName )
  {
    this.dbName = dbName;
  }

  private String getDbName()
  {
    return dbName;
  }

  private void makeConnection()
  {
    ds = DataSourceFactory.createDataSource( getDbName() );
    //ds = DataSourceFactory.createBillSource();
  }

  public void addLog()
  {
    Map results;

    makeConnection();
    prepProc();
    results = execute();
  }

  private void prepProc()
  {
    setDataSource( ds );
    setFunction( false );
    setSql( "insert_cashnet_log" );
    declareParameter( new SqlParameter( "p_ucla_ref_no",
                                        Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_result_code", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_cn_trans_no", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_cn_batch_no", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_pmt_code", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_eff_date", Types.DATE ) );
    declareParameter( new SqlParameter( "p_cn_details", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_user_name", Types.VARCHAR ) );
    compile();
  }

  private Map execute()
  {
    Map input;
    Map out;

    out = null;
    input = new HashMap();

    input.put( "p_ucla_ref_no", getData().getRefNumber() );
    input.put( "p_result_code", getData().getResultCode() );
    input.put( "p_cn_trans_no", getData().getTransNumber() );
    input.put( "p_cn_batch_no", getData().getBatchNumber() );
    input.put( "p_pmt_code", getData().getPmtCode() );
    try
    {
      input.put( "p_eff_date", new DateAdapter().unmarshal( getData().getEffDate() ) );
    }
    catch ( ParseException e )
    {
      input.put( "p_eff_date", "sysdate" );
    }
    input.put( "p_cn_details", getData().getDetails() );
    input.put( "p_user_name", getUser() );

    out = execute( input );

    return out;
  }

  public void setUser( String user )
  {
    this.user = user;
  }

  private String getUser()
  {
    return user;
  }
}