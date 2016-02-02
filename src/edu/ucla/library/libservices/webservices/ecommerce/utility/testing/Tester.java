package edu.ucla.library.libservices.webservices.ecommerce.utility.testing;

import edu.ucla.library.libservices.invoicing.webservices.payments.db.procs.ApplyFullPaymentProcedure;

import javax.ws.rs.core.Response;

public class Tester
{
  public Tester()
  {
    super();
  }

  public static void main( String[] args )
  {
    ApplyFullPaymentProcedure proc;
    
    proc = new ApplyFullPaymentProcedure();
    proc.setDbName( "" );
    proc.setUserName( "ecommerce" );
    proc.setInvoiceNumber( "AS004782" );
    proc.setPaymentType( 3 );
    try
    {
      proc.addPayment();
    }
    catch ( Exception e )
    {
      System.out.println( e.getMessage() );
    }
  }

}
