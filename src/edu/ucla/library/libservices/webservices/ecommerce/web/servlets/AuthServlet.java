package edu.ucla.library.libservices.webservices.ecommerce.web.servlets;

import edu.ucla.library.libservices.webservices.ecommerce.web.clients.AuthClient;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthServlet
  extends HttpServlet
{

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
      //response.addHeader("SHIBUCLAUNIVERSITYID", request.getParameter("userID"));
      Cookie authCookie = new Cookie( "almaID", 
                                      request.getParameter("userID") );
      authCookie.setHttpOnly(true);
      authCookie.setMaxAge(300);
      authCookie.setSecure(true);
      response.addCookie( authCookie );
      response.sendRedirect("protected/invoices.jsp");
    }
    else
    {
      response.addCookie( new Cookie( "logonError", 
                                      "user name or password not recognized" ) );
      response.sendRedirect("protected/errors.jsp");
    }
  }
}
