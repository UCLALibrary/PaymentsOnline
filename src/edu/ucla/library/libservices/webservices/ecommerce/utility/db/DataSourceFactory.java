package edu.ucla.library.libservices.webservices.ecommerce.utility.db;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import java.sql.Connection;
import javax.sql.DataSource;

public class DataSourceFactory
{
  public DataSourceFactory()
  {
    super();
  }

  /*
  	* TODO: rewrite this to use plain jdbc
  */
  public static Connection createVgerSource()
  {
    /*DriverManagerDataSource ds;

    ds = new DriverManagerDataSource();
    ds.setDriverClassName( "oracle.jdbc.OracleDriver" );
    ds.setUrl( "oracle_url" );
    ds.setUsername( "catalog_user" );
    ds.setPassword( "pwd" );

    return ds;*/
    return null;
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
