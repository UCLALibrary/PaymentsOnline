<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=utf-8" isErrorPage="true"
         import="java.io.CharArrayWriter, java.io.PrintWriter"%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>Errors Have Occurred</title>
    <!--style type="text/css">
      body {
      background-color: #ff0000; 
}
    </style-->
  </head>
  <body>An error occured:<br/><pre>
    We're sorry: an error has occurred and Library Payments Online is unable to process your request. Please contact 
    the Library Help Desk for assistance; please report when you accessed Payments Online and what you were attempting to do (e.g., 
    logging in, viewing an invoice, submitting payment) when the error occurred.
    <%
      /*if (exception != null) 
      { 
        out.println("Please include the following error message when you contact the Help Desk: ");
        out.println(exception.getMessage());
        CharArrayWriter charArrayWriter = new CharArrayWriter(); 
        PrintWriter printWriter = new PrintWriter(charArrayWriter, true); 
        exception.printStackTrace(printWriter); 
        out.println(charArrayWriter.toString()); 
      }*/ 
      for (Cookie aCookie : request.getCookies() )
      {
        if (aCookie.getName() != null && aCookie.getName().equals("logonError"))
        {
          out.println("Please include the following error message when you contact the Help Desk: ");
          out.println(aCookie.getValue());
          out.println("<div class=\"login-repeat\"><p>Forgot Password? First Time User?</p>");
          out.println("<a href=\"https://search.library.ucla.edu/view/resetPw?institutionCode=01UCS_LAL\" target=\"_blank\">Click here to create or reset.</a></p></div>");
        }
      }
    %>
    <br/><br/>
    You can report this error via the <a href="https://jira.library.ucla.edu/servicedesk/customer/portals">Service Portal</a>, 
    or by email to <a href="mailto:techhelp@library.ucla.edu">techhelp@library.ucla.edu</a>
    </pre>
    To leave Payments Online, click here: 
    <form method="POST" action="https://webservices.library.ucla.edu/Shibboleth.sso/Logout"><!--?entityId=https://webservices.library.ucla.edu/lpo/shibboleth-sp"-->
      <input type="hidden" name="return" value="https://shb.ais.ucla.edu/shibboleth-idp/Logout"/>
      <input type="submit" value="Logout">
    </form>
  </body>
</html>
