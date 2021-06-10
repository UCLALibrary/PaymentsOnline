package edu.ucla.library.libservices.webservices.ecommerce.utility.testing;

import edu.ucla.library.libservices.invoicing.webservices.payments.db.procs.ApplyFullPaymentProcedure;

import edu.ucla.library.libservices.webservices.ecommerce.beans.AlmaFees;
import edu.ucla.library.libservices.webservices.ecommerce.beans.AlmaInvoice;
import edu.ucla.library.libservices.webservices.ecommerce.beans.AlmaUser;
import edu.ucla.library.libservices.webservices.ecommerce.web.clients.AlmaClient;

import java.text.SimpleDateFormat;

import javax.ws.rs.core.Response;

public class Tester
{
  public Tester()
  {
    super();
  }

  public static void main(String[] args)
    throws Exception
  {
    /*ApplyFullPaymentProcedure proc;

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
    }*/
    AlmaClient theClient = new AlmaClient();
    theClient.setKey("l8xx8cb982c2d4b04ef79375f5c776dbae71");
    //theClient.setResourceURI("/fees/");
    //theClient.setFineID("2563469710006533");
    theClient.setUriBase("https://api-na.hosted.exlibrisgroup.com/almaws/v1/users/");
    theClient.setUserID("603513612");
    
    AlmaUser david = theClient.getThePatron();
    System.out.println(david.getFirstName() + " " + david.getLastName());
    
    /*AlmaInvoice theInvoice = theClient.getTheInvoice();
    System.out.println(theInvoice.getInvoiceNumber() + "\t (" + theInvoice.getType().getDescription() + " on " +
                       theInvoice.getBarcode() + ", incurred " + theInvoice.getFeeDate() + " at " +
                       theInvoice.getOwner() + ") : " + theInvoice.getBalance());

    AlmaFees theFees = theClient.getTheFees();
    for (AlmaInvoice theInvoice: theFees.getFees())
    {
      System.out.println(theInvoice.getId() + "\t (" + theInvoice.getType().getDescription() + " on " +
                         theInvoice.getBarcode() + ", incurred " + theInvoice.getFeeDate() + " at " +
                         theInvoice.getOwner() + ") : " + theInvoice.getBalance());
    }*/

    /*String dateTime = "2021-05-12T01:38:10.489Z";
    String formatIn = "yyyy-MM-dd";
    String formatOut = "MMMM dd, yyyy";
    System.out.println(dateTime.substring(0, dateTime.lastIndexOf("T")));
    System.out.println(new SimpleDateFormat(formatOut)
                       .format(new SimpleDateFormat(formatIn).parse(dateTime.substring(0, dateTime.lastIndexOf(":")))));*/
  }

}
