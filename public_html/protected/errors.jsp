<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=utf-8" isErrorPage="true"
         import="java.io.CharArrayWriter, java.io.PrintWriter"%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>Errors Have Occurred</title>
    <style type="text/css">
      body {
      background-color: #ff0000; 
}
    </style>
  </head>
  <body>An error occured:<br/><pre>
    <%
      if (exception != null) 
      { 
        out.println(exception.getMessage());
        CharArrayWriter charArrayWriter = new CharArrayWriter(); 
        PrintWriter printWriter = new PrintWriter(charArrayWriter, true); 
        exception.printStackTrace(printWriter); 
        out.println(charArrayWriter.toString()); 
      } 
    %>
    </pre></body>
</html>