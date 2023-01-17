package edu.ucla.library.libservices.webservices.ecommerce.web.servlets;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import edu.ucla.library.libservices.webservices.ecommerce.utility.generators.PdfGenerator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.*;
import javax.servlet.http.*;

public class PdfServlet
  extends HttpServlet
{
  private static final String CONTENT_TYPE = "application/pdf;charset=UTF-8";

  public void init(ServletConfig config)
    throws ServletException
  {
    super.init(config);
  }

  /**Process the HTTP doGet request.
   */
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    ByteArrayOutputStream baos;
    Document document;
    PdfGenerator generator;
    ServletOutputStream out;

    response.setContentType(CONTENT_TYPE);
    response.addHeader("Content-Disposition", "inline; filename=" + "invoice.pdf");
    out = response.getOutputStream();

    document = new Document(PageSize.LETTER);
    baos = new ByteArrayOutputStream();
    generator = new PdfGenerator();
    generator.setInvoiceNumber(request.getParameter("in"));
    generator.setPatronID(request.getParameter("uid"));
    generator.setDbName(getServletContext().getInitParameter("datasource.invoice"));
    generator.setApiKey(getServletContext().getInitParameter("alma.key"));
    generator.setUriBase(getServletContext().getInitParameter("alma.base.fees"));

    try
    {
      PdfWriter.getInstance(document, baos);
      generator.populatePdf(document);
      baos.writeTo(out);
    }
    catch (DocumentException de)
    {
      response.sendError(500, de.getMessage());      
    }
    
  }
}
