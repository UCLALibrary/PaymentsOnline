package edu.ucla.library.libservices.webservices.ecommerce.web.servlets;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;

//import edu.ucla.library.libservices.webservices.ecommerce.web.clients.XeroInvoiceClient;

import com.sun.jersey.api.client.WebResource;

import edu.ucla.library.libservices.webservices.ecommerce.constants.XeroConstants;

import java.io.IOException;
//import java.io.PrintWriter;

import javax.servlet.*;
import javax.servlet.http.*;

public class XeroPdfServlet
  extends HttpServlet
{
  @SuppressWarnings("compatibility:4151141589361119859")
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
    Client client;
    ClientResponse clientResponse;
    ServletOutputStream out;
    WebResource webResource;

    response.setContentType(CONTENT_TYPE);
    response.addHeader("Content-Disposition", "inline; filename=invoice.pdf");

    client = Client.create();
    webResource = client.resource("https://api.xero.com/api.xro/2.0/Invoices/6f889b66-0676-4c1b-9120-2f19f3d674a5");
    clientResponse = webResource.accept(XeroConstants.PDF_ACCEPT)
                          .header(XeroConstants.AUTH_HEADER, "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6IjFDQUY4RTY2NzcyRDZEQzAyOEQ2NzI2RkQwMjYxNTgxNTcwRUZDMTkiLCJ0eXAiOiJKV1QiLCJ4NXQiOiJISy1PWm5jdGJjQW8xbkp2MENZVmdWY09fQmsifQ.eyJuYmYiOjE3NTgyMzIxMTQsImV4cCI6MTc1ODIzMzkxNCwiaXNzIjoiaHR0cHM6Ly9pZGVudGl0eS54ZXJvLmNvbSIsImF1ZCI6Imh0dHBzOi8vaWRlbnRpdHkueGVyby5jb20vcmVzb3VyY2VzIiwiY2xpZW50X2lkIjoiMjI2QkJFNTUzMDlDNEU5NzhGRkMyRUI4NjU4MjM0MzMiLCJzdWIiOiJmODEzOTc5ZjA1ZWQ1M2Q0ODRlMGIzMmYzYjczOTQxZSIsImF1dGhfdGltZSI6MTc1ODIzMTk1NywieGVyb191c2VyaWQiOiI2NjJmNDViMC0yYmE5LTRkNzMtYmEzOC01MDVlYzc4ODMyYjYiLCJnbG9iYWxfc2Vzc2lvbl9pZCI6ImExMDdhYTcxYTA1MTRlMTdiMTVkYWY4ODYxZjJmY2MwIiwic2lkIjoiYTEwN2FhNzFhMDUxNGUxN2IxNWRhZjg4NjFmMmZjYzAiLCJhdXRoZW50aWNhdGlvbl9ldmVudF9pZCI6ImIwOGQxOTk3LWNiNjAtNDFhYS05YWI4LWI0ZjI2NzRjMjIxYyIsImp0aSI6IjNDMTBDNUI3QTA3MDY0REI2NUQxN0NBQjcyQ0Q0QTc1Iiwic2NvcGUiOlsiYWNjb3VudGluZy5jb250YWN0cyIsImFjY291bnRpbmcuc2V0dGluZ3MiLCJhY2NvdW50aW5nLnRyYW5zYWN0aW9ucyIsIm9mZmxpbmVfYWNjZXNzIl0sImFtciI6WyJwd2QiXX0.JgNqONQ_t_Bcb5WXlKgg6jf0u-LbsuTNNUnx3yk8qU2B8BUhT-zUV-zT2Aw2ZiHvBKHiRBck4eyHNoiv1GU_oTIF74-WkHs8al-JCb_WLk8MU_6BBQdqVxQkeLXC5srcHA1c78e9VX64h-yaGSLQuInGsWFsrEZ6v---xmSb_j9-UIYgP_cRyERkA_urpfaXqUV25aXTev0SC9N8usIExh0lEk58uAju8BmET5KIO8kNC0X5jFA9N7VhTjs3LVuJQZQ98i5gOoxS3wr5RUUCWS8ysn6th8mqG2Ion5jcojvuM7wgSjpC2QdjWMcdMbdcIsTqROvbHqMVqlSXBAr90w")
                          .header(XeroConstants.TENANT_HEADER, "d9e3104a-1995-4ab7-b3f7-c889b9e7338a")
                          .get(ClientResponse.class);
    out = response.getOutputStream();
    out.write(clientResponse.getEntity(String.class).toString().getBytes("UTF-8"));
    /*response.setContentType(CONTENT_TYPE);
    response.addHeader("Content-Disposition", "inline; filename=invoice.pdf");
    response.addHeader("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6IjFDQUY4RTY2NzcyRDZEQzAyOEQ2NzI2RkQwMjYxNTgxNTcwRUZDMTkiLCJ0eXAiOiJKV1QiLCJ4NXQiOiJISy1PWm5jdGJjQW8xbkp2MENZVmdWY09fQmsifQ.eyJuYmYiOjE3NTgyMTg4MjgsImV4cCI6MTc1ODIyMDYyOCwiaXNzIjoiaHR0cHM6Ly9pZGVudGl0eS54ZXJvLmNvbSIsImF1ZCI6Imh0dHBzOi8vaWRlbnRpdHkueGVyby5jb20vcmVzb3VyY2VzIiwiY2xpZW50X2lkIjoiMjI2QkJFNTUzMDlDNEU5NzhGRkMyRUI4NjU4MjM0MzMiLCJzdWIiOiJmODEzOTc5ZjA1ZWQ1M2Q0ODRlMGIzMmYzYjczOTQxZSIsImF1dGhfdGltZSI6MTc1ODIxODczMiwieGVyb191c2VyaWQiOiI2NjJmNDViMC0yYmE5LTRkNzMtYmEzOC01MDVlYzc4ODMyYjYiLCJnbG9iYWxfc2Vzc2lvbl9pZCI6IjgzMjY1ZDk3NmM2MjQyZTZhMmMyM2U1NTExMGYzOTRmIiwic2lkIjoiODMyNjVkOTc2YzYyNDJlNmEyYzIzZTU1MTEwZjM5NGYiLCJhdXRoZW50aWNhdGlvbl9ldmVudF9pZCI6IjgwNTQwYzg4LTEyNzktNGRkZC1hZWFiLWM5NDc5YjdmYWQzZiIsImp0aSI6IjQwRDgyNzBGMEE4NUNCQ0Q2QjE2NzdEREYxOTREOTM5Iiwic2NvcGUiOlsiYWNjb3VudGluZy5jb250YWN0cyIsImFjY291bnRpbmcuc2V0dGluZ3MiLCJhY2NvdW50aW5nLnRyYW5zYWN0aW9ucyIsIm9mZmxpbmVfYWNjZXNzIl0sImFtciI6WyJsZWdhY3kiXX0.S2UVtE55t4pnq3F_1fi1QdnOz2fkOAhPyARncj4QoEl6s4XFjqxx1NuC9XqiPcHKn28YMnb9Dk-iueJ7ZmVZh8LmER_1bQm85EKh33zdLwBneDAbzkVmFQ7iD_XSauJ-I6D0YElb3uOQJ676hjQrlsV8WKSSLNJ7TDdAcR8R-rTLq1LVVLJsJ7vXXtisQKoSyv-E0x7tZSK_S0x-w8k8p1RUYB_027fPJXUeTx66z6FemCHIQ33JC0M4LHINABIsq6Nd6UdMibIsJ1-PvssXgPs96o-kXTxVdE4BiMuJ6kRgWM65MGmK8JuqNxNdPdhQLk63xk5D2dg6rM2o20e46g");
    response.addHeader("xero-tenant-id", "d9e3104a-1995-4ab7-b3f7-c889b9e7338a");
    String invoiceURL;
    invoiceURL = "https://api.xero.com/api.xro/2.0/Invoices/6f889b66-0676-4c1b-9120-2f19f3d674a5";
    response.sendRedirect(invoiceURL);*/

    /*ServletOutputStream out;
    XeroInvoiceClient pdfClient;

    response.setContentType(CONTENT_TYPE);
    response.addHeader("Content-Disposition", "inline; filename=invoice.pdf");

    out = response.getOutputStream();
    pdfClient = new XeroInvoiceClient();
    pdfClient.setInvoiceID(request.getParameter("display_invoice"));
    pdfClient.setPort(0);
    pdfClient.setSecretsFile("secretsFile");
    pdfClient.setTokensFile("tokensFile");
    byte[] bytes = pdfClient.getInvoicePDF().getEntity(ClientResponse.class).toString().getBytes("UTF-16");
    out.write(bytes);*/
  }
}
