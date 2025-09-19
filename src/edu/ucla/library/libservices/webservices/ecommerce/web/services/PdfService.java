package edu.ucla.library.libservices.webservices.ecommerce.web.services;

import edu.ucla.library.libservices.webservices.ecommerce.web.clients.XeroInvoiceClient;

import javax.servlet.ServletConfig;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Path("/xero/")
public class PdfService
{
  @Context
  ServletConfig config;

  public PdfService()
  {
    super();
  }

  @GET
  @Produces("application/pdf")
  @Path("display_invoice/{in}")
  public Response getInvoicePdf(@PathParam("in") String invoiceNo)
  {
    XeroInvoiceClient pdfClient;
    String filename;

    filename = invoiceNo.concat(".pdf");

    pdfClient = new XeroInvoiceClient();
    pdfClient.setInvoiceID(invoiceNo);
    pdfClient.setPort(0);
    pdfClient.setSecretsFile(config.getServletContext().getInitParameter("secrets"));
    pdfClient.setTokensFile(config.getServletContext().getInitParameter("tokens"));
    return Response.ok( pdfClient.getInvoicePDF().getEntityInputStream() ).header("Content-Disposition", "inline; filename=".concat(filename)).build();
  }
}
