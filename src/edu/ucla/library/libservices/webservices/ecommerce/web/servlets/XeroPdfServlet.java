package edu.ucla.library.libservices.webservices.ecommerce.web.servlets;

import com.sun.jersey.api.client.ClientResponse;

import edu.ucla.library.libservices.webservices.ecommerce.web.clients.XeroInvoiceClient;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.*;
import javax.servlet.http.*;

public class XeroPdfServlet
  extends HttpServlet
{
  @SuppressWarnings("compatibility:-4386877370931444547")
  private static final long serialVersionUID = 1043653224671454044L;
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
    ServletOutputStream out;
    XeroInvoiceClient pdfClient;

    response.setContentType(CONTENT_TYPE);
    response.addHeader("Content-Disposition", "inline; filename=invoice.pdf");

    out = response.getOutputStream();
    pdfClient = new XeroInvoiceClient();
    pdfClient.setInvoiceID(request.getParameter("display_invoice"));
    pdfClient.setPort(0);
    pdfClient.setSecretsFile("secretsFile");
    pdfClient.setTokensFile("tokensFile");
    //out.write(pdfClient.getInvoicePDF().getEntity(ClientResponse.class));
  }
}
