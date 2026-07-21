package edu.ucla.library.libservices.webservices.ecommerce.utility.db;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import java.sql.Connection;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DataSourceFactory
{
  private static final Logger LOGGER = LogManager.getLogger( DataSourceFactory.class );

  public DataSourceFactory()
  {
    super();
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
    catch (NamingException ne)
    {
      LOGGER.error("error retrieving pooled db connection: "  + ne.getMessage());
      connection = null;
    }

    return connection;
  }
}
