package edu.ucla.library.libservices.webservices.ecommerce.web.servlets;

import edu.ucla.library.libservices.webservices.ecommerce.web.clients.AuthClient;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.*;
import javax.servlet.http.*;

public class AuthServlet
  extends HttpServlet
{
  private static final String CONTENT_TYPE = "text/html; charset=windows-1252";

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
    doPost(request, response);
  }

  /**Process the HTTP doPost request.
   */
  public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    AuthClient theClient;
    theClient = new AuthClient();
    theClient.setKey(getServletContext().getInitParameter("alma.key.auth"));
    theClient.setPassword(request.getParameter("password"));
    theClient.setUriBase(getServletContext().getInitParameter("alma.base.fees"));
    theClient.setUserID(request.getParameter("userID"));
    if (theClient.isValidPatron())
    {
      //store userID in shib header
      //redirect to invoices.jsp
    }
    else
    {
      //redirect to error page with "unsuccessful login" message
    }
  }
}
