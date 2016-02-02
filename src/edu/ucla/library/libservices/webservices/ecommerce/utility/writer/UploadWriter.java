package edu.ucla.library.libservices.webservices.ecommerce.utility.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class UploadWriter
{
  private String directory;
  private String invoiceNumber;
  private int aeonID;

  public UploadWriter()
  {
    super();
  }

  public void setDirectory( String directory )
  {
    this.directory = directory;
  }

  private String getDirectory()
  {
    return directory;
  }

  public void setInvoiceNumber( String invoiceNumber )
  {
    this.invoiceNumber = invoiceNumber;
  }

  private String getInvoiceNumber()
  {
    return invoiceNumber;
  }

  public void setAeonID( int aeonID )
  {
    this.aeonID = aeonID;
  }

  private int getAeonID()
  {
    return aeonID;
  }

  public void writeFile()
  {
    BufferedWriter writer;
    String directory;
    String fileName;

    directory = getDirectory();
    fileName = String.valueOf( getAeonID() ).concat( ".txt" );

    try
    {
      writer =
          new BufferedWriter( new FileWriter( new File( directory.concat( "/" ).concat( fileName ) ) ) );
      writer.write( "<libbill-invoice>" );
      writer.newLine();
      writer.write( "<aeon-request-id>" + getAeonID() +
                    "</aeon-request-id>" );
      writer.newLine();
      writer.write( "<libbill-invoice-number>" + getInvoiceNumber() +
                    "</libbill-invoice-number>" );
      writer.newLine();
      writer.write( "</libbill-invoice>" );
      writer.newLine();
      writer.flush();
      writer.close();
    }
    catch ( IOException ioe )
    {
      ioe.printStackTrace();
    }
  }
}
