package edu.ucla.library.libservices.invoicing.utility.db;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class DataSourceFactory
{
  public DataSourceFactory()
  {
    super();
  }

  public static DriverManagerDataSource createVgerSource()
  {
    DriverManagerDataSource ds;

    ds = new DriverManagerDataSource();
    ds.setDriverClassName( "oracle.jdbc.OracleDriver" );
    ds.setUrl( "oracle_url" );
    ds.setUsername( "catalog_user" );
    ds.setPassword( "pwd" );

    return ds;
  }

  public static DriverManagerDataSource createBillSource()
  {
    DriverManagerDataSource ds;

    ds = new DriverManagerDataSource();
    ds.setDriverClassName( "oracle.jdbc.OracleDriver" );
    ds.setUrl( "bill_url" );
    ds.setUsername( "bill_user" );
    ds.setPassword( "bill_pwd" );

    return ds;
  }

  public static DataSource createDataSource( String name )
  {
    Context envContext;
    InitialContext context;
    DataSource connection;

    try
    {
      context = new InitialContext();
      envContext = (Context) context.lookup("java:/comp/env");
      connection = (DataSource) envContext.lookup(name);
    }
    catch (NamingException e)
    {
      e.printStackTrace();
      connection = null;
    }

    return connection;
  }
}
