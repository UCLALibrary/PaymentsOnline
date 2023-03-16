package edu.ucla.library.libservices.webservices.ecommerce.utility.generators;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.draw.LineSeparator;

import edu.ucla.library.libservices.invoicing.utiltiy.testing.ContentTests;
import edu.ucla.library.libservices.webservices.ecommerce.beans.Address;
import edu.ucla.library.libservices.webservices.ecommerce.beans.AlmaInvoice;
import edu.ucla.library.libservices.webservices.ecommerce.beans.AlmaUser;
import edu.ucla.library.libservices.webservices.ecommerce.web.clients.AlmaClient;

import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class PdfGenerator
{
  private AlmaClient invoiceClient;
  private AlmaInvoice theInvoice;
  private AlmaUser thePatron;
  private Address thePatronAddress;
  private String invoiceNumber;
  private String patronID;
  private String dbName;
  private String uriBase;
  private String apiKey;
  private Paragraph paragraph;
  private PdfPCell defaultCell;
  private PdfPCell tableCell;
  private PdfPCell numberCell;
  private PdfPTable table;
  private StringBuffer buffer;
  private float[] columnWidths;
  //private double adjustTotal = 0D;
  //private boolean didAdjustCalc = false;

  private static final SimpleDateFormat DATE_OUTPUT = new SimpleDateFormat("MM/dd/yyyy");
  private static final DecimalFormat MONEY_OUTPUT = new DecimalFormat("$###########0.00");
  private static final Font BOLD = new Font(Font.getFamily("Arial-BoldMT"), 8, Font.BOLD);
  private static final Font HEADER = new Font(Font.getFamily("Arial-BoldMT"), 14, Font.BOLD);
  private static final Font ITALIC = new Font(Font.getFamily("ArialMT"), 8, Font.ITALIC);
  private static final Font NORMAL = new Font(Font.getFamily("ArialMT"), 8, Font.NORMAL);

  public PdfGenerator()
  {
    super();
  }

  public void setInvoiceNumber(String invoiceNumber)
  {
    this.invoiceNumber = invoiceNumber;
  }

  private String getInvoiceNumber()
  {
    return invoiceNumber;
  }

  public void setPatronID(String patronID)
  {
    this.patronID = patronID;
  }

  private String getPatronID()
  {
    return patronID;
  }

  public void setDbName(String dbName)
  {
    this.dbName = dbName;
  }

  private String getDbName()
  {
    return dbName;
  }

  public void setUriBase(String uriBase)
  {
    this.uriBase = uriBase;
  }

  private String getUriBase()
  {
    return uriBase;
  }

  public void setApiKey(String apiKey)
  {
    this.apiKey = apiKey;
  }

  private String getApiKey()
  {
    return apiKey;
  }

  public void populatePdf(Document document)
  {
    prepAlmaClient();
    theInvoice = invoiceClient.getTheInvoice();
    thePatron = invoiceClient.getThePatron();
    //System.out.println(thePatron.getPatronID() + "\t" + thePatron.getContactInfo().getAddresses().size());
    if (!thePatron.getContactInfo().getAddresses().isEmpty()) 
    {
      if ( thePatron.getContactInfo()
                                  .getAddresses()
                                  .stream()
                                  .filter(e -> e.isPreferred())
                                  .count() > 0 )
      {
        thePatronAddress = thePatron.getContactInfo()
                                    .getAddresses()
                                    .stream()
                                    .filter(e -> e.isPreferred())
                                    .findFirst()
                                    .get();
      }
    }
    //System.out.println(thePatronAddress.getLine1() + "\t" + thePatronAddress.getCity() 
      //                 + "\t" + thePatronAddress.getZipCode());

    try
    {
      document.open();

      addImage(document);
      addTitle(document);
      addHeader(document);
      addPayment(document);
      addMiddle(document);
      addEntries(document);

      document.add(new LineSeparator(1F, 100F, BaseColor.BLACK, Element.ALIGN_MIDDLE, 0F));
      columnWidths = new float[]
      {
        60f, 20f, 10f, 10f
      };
      table = new PdfPTable(4);
      table.setWidthPercentage(98);
      table.setSpacingAfter(5f);
      table.setWidths(columnWidths);

      defaultCell = table.getDefaultCell();
      defaultCell.setBorder(PdfPCell.NO_BORDER);
      defaultCell.setHorizontalAlignment(Element.ALIGN_LEFT);
      defaultCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

      buffer = new StringBuffer((!ContentTests.isEmpty(thePatron.getFirstName())? thePatron.getFirstName().trim(): ""));
      buffer.append(" ").append((!ContentTests.isEmpty(thePatron.getLastName()))? thePatron.getLastName().trim(): "");

      table.addCell(new Paragraph(buffer.toString().trim(), NORMAL));
      table.addCell(prepEmptyCell(1));
      table.addCell(new Paragraph("NonTaxable:", NORMAL));
      numberCell = getRightAlignedCell();
      paragraph = new Paragraph( MONEY_OUTPUT.format( theInvoice.getBalance() ), NORMAL );
      paragraph.setAlignment(Element.ALIGN_RIGHT);
      numberCell.addElement(paragraph);
      table.addCell(numberCell);

      //buffer = new StringBuffer();
      //for ( InvoiceNote theNote: theInvoice.getInvoiceNotes() )
      //{
      //if ( !theNote.isInternal() )
      //buffer.append( theNote.getNote().trim() ).append( " " );
      //}
      tableCell = new PdfPCell();
      tableCell.setBorder(PdfPCell.NO_BORDER);
      tableCell.setHorizontalAlignment(Element.ALIGN_LEFT);
      tableCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
      tableCell.setIndent(10f);
      paragraph = new Paragraph("", ITALIC);
      paragraph.setIndentationLeft(10f);
      tableCell.addElement(paragraph);
      table.addCell(tableCell);
      table.addCell(prepEmptyCell(1));
      table.addCell(new Paragraph("Taxable:", NORMAL));
      numberCell = getRightAlignedCell();
      paragraph = new Paragraph( MONEY_OUTPUT.format( 0.0D ), NORMAL );
      paragraph.setAlignment(Element.ALIGN_RIGHT);
      numberCell.addElement(paragraph);
      table.addCell(numberCell);

      table.addCell(prepEmptyCell(1));
      table.addCell(prepEmptyCell(1));
      table.addCell(new Paragraph("Tax:", NORMAL));
      numberCell = getRightAlignedCell();
      //MONEY_OUTPUT.format( theInvoice.getHeader().getCountyTax() + theInvoice.getHeader().getStateTax() ),
      paragraph = new Paragraph(MONEY_OUTPUT.format( 0.0D ), NORMAL);
      //paragraph = new Paragraph(outputTax(), NORMAL);
      paragraph.setAlignment(Element.ALIGN_RIGHT);
      numberCell.addElement(paragraph);
      table.addCell(numberCell);

      table.addCell(prepEmptyCell(1));
      table.addCell(prepEmptyCell(1));
      table.addCell(new Paragraph("Adjustments:", NORMAL));
      numberCell = getRightAlignedCell();
      paragraph = new Paragraph(MONEY_OUTPUT.format(getAdjustTotal()), NORMAL);
      paragraph.setAlignment(Element.ALIGN_RIGHT);
      numberCell.addElement(paragraph);
      table.addCell(numberCell);

      table.addCell(prepEmptyCell(1));
      table.addCell(prepEmptyCell(1));
      table.addCell(new Paragraph("Amount Due:", NORMAL));
      numberCell = getRightAlignedCell();
      paragraph = new Paragraph(MONEY_OUTPUT.format(theInvoice.getBalance()), NORMAL);
      paragraph.setAlignment(Element.ALIGN_RIGHT);
      numberCell.addElement(paragraph);
      table.addCell(numberCell);

      document.add(table);

      printFooter(document, paragraph);

      document.close();
    }
    catch (DocumentException de)
    {
      de.printStackTrace();
      document = null;
    }
    catch (MalformedURLException mue)
    {
      mue.printStackTrace();
      document = null;
    }
    catch (IOException ioe)
    {
      ioe.printStackTrace();
      document = null;
    }
  }

  private void prepAlmaClient()
  {
    invoiceClient = new AlmaClient();
    invoiceClient.setDbName(getDbName());
    invoiceClient.setKey(getApiKey());
    invoiceClient.setFineID(getInvoiceNumber());
    invoiceClient.setUriBase(getUriBase());
    invoiceClient.setUserID(getPatronID());
  }

  private void addImage(Document doc)
    throws BadElementException, MalformedURLException, IOException, DocumentException
  {
    Image image;

    image = Image.getInstance(new URL("https://webservices.library.ucla.edu/images/Lib_Logo4Invoices.gif"));

    image.setAbsolutePosition(350f, 750f);
    image.scalePercent(50f);
    doc.add(image);
  }

  private void addTitle(Document doc)
    throws DocumentException
  {
    paragraph = new Paragraph("Invoice", HEADER);
    paragraph.setAlignment(Element.ALIGN_CENTER);
    paragraph.setSpacingAfter(5f);

    doc.add(paragraph);
  }

  private void addHeader(Document doc)
    throws DocumentException
  {
    PdfPTable nestedTable = null;
    PdfPCell defaultNestedCell = null;

    columnWidths = new float[]
    {
      20f, 30f, 30f, 20f
    };
    table = new PdfPTable(4);
    table.setWidthPercentage(98);
    table.setSpacingAfter(3f);
    table.setWidths(columnWidths);

    defaultCell = prepDefaultCell(table.getDefaultCell());

    table.addCell(new Paragraph("Invoice Date:", NORMAL));
    table.addCell(new Paragraph(theInvoice.getFeeDate(), NORMAL));
    table.addCell(prepEmptyCell(2));
    table.addCell(new Paragraph("Invoice Number:", NORMAL));
    table.addCell(new Paragraph(theInvoice.getInvoiceNumber(), NORMAL));
    table.addCell(prepEmptyCell(2));

    table.addCell(prepEmptyCell(4));

    //will need to extract location from invoice later
    table.addCell(new Paragraph("Send Payment To:", NORMAL));
    table.addCell(new Paragraph("Library Business Services", NORMAL));
    table.addCell(prepEmptyCell(1));
    table.addCell(new Paragraph(theInvoice.getOwner(), NORMAL));

    table.addCell(prepEmptyCell(1));
    table.addCell(new Paragraph("Payment Processing Unit", NORMAL));
    table.addCell(prepEmptyCell(1));
    table.addCell(new Paragraph("", NORMAL));

    table.addCell(prepEmptyCell(1));
    table.addCell(new Paragraph("Box 951575", NORMAL));
    table.addCell(prepEmptyCell(1));
    table.addCell(new Paragraph("", NORMAL));
    //table.addCell( new Paragraph( theInvoice.getHeader().getPhoneNumber(), NORMAL ) );

    table.addCell(prepEmptyCell(1));
    table.addCell(new Paragraph("Los Angeles, CA  90095-1575", NORMAL));
    table.addCell(prepEmptyCell(1));
    table.addCell(prepEmptyCell(1));

    for (int i = 0; i < 9; i++)
      table.addCell(prepEmptyCell(4));

    buffer = new StringBuffer((!ContentTests.isEmpty(thePatron.getFirstName())? thePatron.getFirstName().trim(): ""));
    buffer.append(" ").append((!ContentTests.isEmpty(thePatron.getLastName()))? thePatron.getLastName().trim(): "");

    table.addCell(prepEmptyCell(1));
    table.addCell(new Paragraph(buffer.toString().trim(), NORMAL));
    table.addCell(prepEmptyCell(1));

    nestedTable = prepNestedTable(nestedTable);
    defaultNestedCell = prepNestedCell(nestedTable, defaultNestedCell);
    nestedTable.addCell(new Paragraph("NonTaxable:", NORMAL));
    numberCell = getRightAlignedCell();
    paragraph = new Paragraph(MONEY_OUTPUT.format(theInvoice.getBalance()), NORMAL);
    paragraph.setAlignment(Element.ALIGN_RIGHT);
    numberCell.addElement(paragraph);
    nestedTable.addCell(numberCell);
    table.addCell(nestedTable);

    table.addCell(prepEmptyCell(1));
    table.addCell(new Paragraph(getStreetAddress(), NORMAL));
    table.addCell(prepEmptyCell(1));

    nestedTable = prepNestedTable(nestedTable);
    defaultNestedCell = prepNestedCell(nestedTable, defaultNestedCell);
    nestedTable.addCell(new Paragraph("Taxable:", NORMAL));
    numberCell = getRightAlignedCell();
    paragraph = new Paragraph("$0.00", NORMAL);
    //paragraph = new Paragraph(MONEY_OUTPUT.format(theInvoice.getHeader().getTaxableTotal()), NORMAL);
    paragraph.setAlignment(Element.ALIGN_RIGHT);
    numberCell.addElement(paragraph);
    nestedTable.addCell(numberCell);
    table.addCell(nestedTable);

    table.addCell(prepEmptyCell(1));
    table.addCell(new Paragraph(getCityStateZip(), NORMAL));
    table.addCell(prepEmptyCell(1));

    nestedTable = prepNestedTable(nestedTable);
    defaultNestedCell = prepNestedCell(nestedTable, defaultNestedCell);
    nestedTable.addCell(new Paragraph("Tax:", NORMAL));
    numberCell = getRightAlignedCell();
    //MONEY_OUTPUT.format( theInvoice.getHeader().getCountyTax() + theInvoice.getHeader().getStateTax() ),
    paragraph = new Paragraph(MONEY_OUTPUT.format( 0.0D ), NORMAL);
    //paragraph = new Paragraph(outputTax(), NORMAL);
    paragraph.setAlignment(Element.ALIGN_RIGHT);
    numberCell.addElement(paragraph);
    nestedTable.addCell(numberCell);
    table.addCell(nestedTable);

    table.addCell(prepEmptyCell(1));
    table.addCell(prepEmptyCell(1));
    table.addCell(prepEmptyCell(1));

    nestedTable = prepNestedTable(nestedTable);
    defaultNestedCell = prepNestedCell(nestedTable, defaultNestedCell);
    nestedTable.addCell(new Paragraph("Adjustments:", NORMAL));
    numberCell = getRightAlignedCell();
    paragraph = new Paragraph(MONEY_OUTPUT.format(getAdjustTotal()), NORMAL);
    paragraph.setAlignment(Element.ALIGN_RIGHT);
    numberCell.addElement(paragraph);
    nestedTable.addCell(numberCell);
    table.addCell(nestedTable);

    table.addCell(prepEmptyCell(1));
    table.addCell(prepEmptyCell(1));
    table.addCell(prepEmptyCell(1));

    nestedTable = prepNestedTable(nestedTable);
    defaultNestedCell = prepNestedCell(nestedTable, defaultNestedCell);
    nestedTable.addCell(new Paragraph("Amount Due:", BOLD));
    numberCell = getRightAlignedCell();
    paragraph = new Paragraph(MONEY_OUTPUT.format(theInvoice.getBalance()), BOLD);
    paragraph.setAlignment(Element.ALIGN_RIGHT);
    numberCell.addElement(paragraph);
    nestedTable.addCell(numberCell);
    table.addCell(nestedTable);

    doc.add(table);
  }

  private PdfPCell prepDefaultCell(PdfPCell data)
  {
    PdfPCell defaultCell = data;
    defaultCell.setBorder(PdfPCell.NO_BORDER);
    defaultCell.setHorizontalAlignment(Element.ALIGN_LEFT);
    defaultCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

    return defaultCell;
  }

  private PdfPCell prepEmptyCell(int cols)
  {
    PdfPCell empty;

    empty = new PdfPCell();
    empty.setColspan(cols);
    empty.setBorder(PdfPCell.NO_BORDER);

    return empty;
  }

  private void addPayment(Document document)
    throws DocumentException
  {
    columnWidths = new float[]
    {
      60f, 20f, 20f
    };
    table = new PdfPTable(3);
    table.setWidthPercentage(98);
    table.setSpacingAfter(5f);
    table.setWidths(columnWidths);

    defaultCell = prepDefaultCell(table.getDefaultCell());

    table.addCell(new Paragraph("---", NORMAL));
    table.addCell(prepEmptyCell(1));
    table.addCell(new Paragraph("                                          ---", NORMAL));

    table.addCell(prepEmptyCell(1));
    table.addCell(new Paragraph("Name on card:", NORMAL));
    table.addCell(new Paragraph("______________________", NORMAL));

    table.addCell(prepEmptyCell(1));
    table.addCell(new Paragraph("Credit Card Type (Circle One):", NORMAL));
    table.addCell(new Paragraph("VISA/MC/DISC/AMEX", NORMAL));

    table.addCell(prepEmptyCell(1));
    table.addCell(new Paragraph("Account Number:", NORMAL));
    table.addCell(new Paragraph("______________________", NORMAL));

    table.addCell(prepEmptyCell(1));
    table.addCell(new Paragraph("Expiration Date:", NORMAL));
    table.addCell(new Paragraph("______________________", NORMAL));

    table.addCell(prepEmptyCell(1));
    table.addCell(new Paragraph("Amount to be charged:", NORMAL));
    table.addCell(new Paragraph("______________________", NORMAL));

    table.addCell(prepEmptyCell(1));
    table.addCell(new Paragraph("Authorized Signature:", NORMAL));
    table.addCell(new Paragraph("______________________", NORMAL));

    document.add(table);
  }

  private PdfPTable prepNestedTable(PdfPTable nestedTable)
  {
    nestedTable = new PdfPTable(2);
    nestedTable.setWidthPercentage(98);

    return nestedTable;
  }

  private PdfPCell prepNestedCell(PdfPTable nestedTable, PdfPCell defaultNestedCell)
  {
    defaultNestedCell = nestedTable.getDefaultCell();
    defaultNestedCell.setBorder(PdfPCell.NO_BORDER);
    defaultNestedCell.setHorizontalAlignment(Element.ALIGN_LEFT);
    defaultNestedCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

    return defaultNestedCell;
  }

  private PdfPCell getRightAlignedCell()
  {
    PdfPCell right;

    right = new PdfPCell();
    right.setBorder(PdfPCell.NO_BORDER);
    right.setHorizontalAlignment(Element.ALIGN_RIGHT);
    right.setVerticalAlignment(Element.ALIGN_MIDDLE);

    return right;
  }

  private String getStreetAddress()
  {
    buffer = new StringBuffer();
    if (!ContentTests.isEmpty(thePatronAddress))
    {
      buffer.append(thePatronAddress.getLine1().trim());
      if (!ContentTests.isEmpty(thePatronAddress.getLine2()))
        buffer.append(" ").append(thePatronAddress.getLine2().trim());
      if (!ContentTests.isEmpty(thePatronAddress.getLine3()))
        buffer.append(" ").append(thePatronAddress.getLine3().trim());
      if (!ContentTests.isEmpty(thePatronAddress.getLine4()))
        buffer.append(" ").append(thePatronAddress.getLine4().trim());
      if (!ContentTests.isEmpty(thePatronAddress.getLine5()))
        buffer.append(" ").append(thePatronAddress.getLine5().trim());
    }
    return buffer.toString().trim();
  }

  private String getCityStateZip()
  {
    buffer = new StringBuffer();
    if (!ContentTests.isEmpty(thePatronAddress))
    {
      if (!ContentTests.isEmpty(thePatronAddress.getCity()))
        buffer.append(thePatronAddress.getCity().trim());
      if (!ContentTests.isEmpty(thePatronAddress.getState()))
        buffer.append(" ").append(thePatronAddress.getState().trim());
      if (!ContentTests.isEmpty(thePatronAddress.getZipCode()))
        buffer.append(" ").append(thePatronAddress.getZipCode().trim());
      if (!ContentTests.isEmpty(thePatronAddress.getCountry()))
        buffer.append(" ").append(thePatronAddress.getCountry().getValue().trim());
    }

    return buffer.toString().trim();
  }

  private String outputTax()
  {
    return "0.0";
  }

  private double getAdjustTotal()
  {
    return 0.0;
  }

  private void addMiddle(Document document)
    throws DocumentException
  {
    paragraph =
      new Paragraph("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------",
                    NORMAL);
    paragraph.setAlignment(Element.ALIGN_LEFT);
    paragraph.setSpacingAfter(5f);
    document.add(paragraph);

    paragraph =
      new Paragraph("Submit top portion with payment.  Payment terms: due " +
                    "and payable upon receipt.  Deposits are non-refundable.", NORMAL);
    paragraph.setAlignment(Element.ALIGN_CENTER);
    paragraph.setSpacingAfter(5f);
    document.add(paragraph);

    paragraph = new Paragraph("Keep lower portion of invoice for your records.", NORMAL);
    paragraph.setAlignment(Element.ALIGN_CENTER);
    paragraph.setSpacingAfter(5f);
    document.add(paragraph);

    paragraph = new Paragraph("UCLA FEIN: 95-6006143", NORMAL);
    paragraph.setAlignment(Element.ALIGN_CENTER);
    paragraph.setSpacingAfter(5f);
    document.add(paragraph);
  }

  private void addEntries(Document document)
    throws DocumentException
  {
    printLinesHeader();
    printLine();
    document.add(table);
  }

  private void printFooter(Document document, Paragraph paragraph)
    throws DocumentException
  {
    List list;

    paragraph = new Paragraph(" ", NORMAL);
    paragraph.setAlignment(Element.ALIGN_CENTER);
    paragraph.setSpacingAfter(5f);
    document.add(paragraph);

    paragraph = new Paragraph("To pay by check:", NORMAL);
    paragraph.setAlignment(Element.ALIGN_LEFT);
    paragraph.setSpacingAfter(5f);
    document.add(paragraph);

    list = new List(false, 10);
    list.setListSymbol("*");
    list.add(new ListItem("Make payable to the Regents of the University of California.  Do not send cash.", NORMAL));
    document.add(list);

    paragraph = new Paragraph("To pay by credit card:", NORMAL);
    paragraph.setAlignment(Element.ALIGN_LEFT);
    paragraph.setSpacingAfter(5f);
    paragraph.setSpacingBefore(5f);
    document.add(paragraph);

    list = new List(false, 10);
    list.setListSymbol("*");
    list.add(new ListItem("Go to Library Payments Online, http://www.library.ucla.edu/use/access-privileges/library-payments-online.  E-check also accepted.",
                          NORMAL));
    list.add(new ListItem("Or complete credit card information requested above and mail to above address or telephone information to 310.206.9770.  Do not send credit card information by fax or email.",
                          NORMAL));
    document.add(list);

    paragraph = new Paragraph("Questions?", NORMAL);
    paragraph.setAlignment(Element.ALIGN_LEFT);
    paragraph.setSpacingAfter(5f);
    paragraph.setSpacingBefore(5f);
    document.add(paragraph);

    list = new List(false, 10);
    list.setListSymbol("*");
    list.add(new ListItem("Contact the library location listed above for questions about the bill amount or the services you?ve received.",
                          NORMAL));
    list.add(new ListItem("Contact Library Business Services for questions about payment options and payment status. 310.206.9770, lbs-billing@library.ucla.edu",
                          NORMAL));
    document.add(list);
  }

  private void printLinesHeader()
    throws DocumentException
  {
    columnWidths = new float[]
    {
      40f, 40f, 20f
    };
    table = new PdfPTable(3);
    table.setWidthPercentage(98);
    table.setSpacingAfter(5f);
    table.setWidths(columnWidths);

    defaultCell = prepDefaultCell(table.getDefaultCell());

    tableCell = prepTableCell(new PdfPCell(), 2, Element.ALIGN_LEFT);
    tableCell.addElement(new Paragraph("Product/Service", BOLD));
    table.addCell(tableCell);
    tableCell = prepTableCell(new PdfPCell(), 1, Element.ALIGN_RIGHT);
    tableCell.addElement(new Paragraph("Total", BOLD));
    table.addCell(tableCell);

    tableCell = prepTableCell(new PdfPCell(), 3, Element.ALIGN_LEFT);
    tableCell.addElement(new LineSeparator(1F, 100F, BaseColor.BLACK, Element.ALIGN_MIDDLE, 0F));
    table.addCell(tableCell);
  }

  private void printLine()
  {
    table.addCell(new Paragraph(theInvoice.getType().getDescription(), NORMAL));
    table.addCell( new Paragraph( "", NORMAL ) );
    numberCell = getRightAlignedCell();
    paragraph = new Paragraph(MONEY_OUTPUT.format(theInvoice.getBalance()), NORMAL);
    paragraph.setAlignment(Element.ALIGN_RIGHT);
    numberCell.addElement(paragraph);
    table.addCell(numberCell);
  }

  private PdfPCell prepTableCell(PdfPCell cell, int cols, int align)
  {
    PdfPCell defaultCell = cell;
    defaultCell.setBorder(PdfPCell.NO_BORDER);
    defaultCell.setHorizontalAlignment(align);
    defaultCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    defaultCell.setColspan(cols);

    return defaultCell;
  }
}
