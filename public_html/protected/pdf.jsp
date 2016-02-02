<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=windows-1252"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:useBean id="pdfSource"
             class="edu.ucla.library.libservices.webservices.ecommerce.web.clients.PdfClient">
  <jsp:setProperty property="uriBase" name="pdfSource" value='https://webservices.library.ucla.edu'/>
  <jsp:setProperty property="resourceURI" name="pdfSource" value='<%= application.getInitParameter("uri.pdf") %>'/>
  <jsp:setProperty property="user" name="pdfSource" value='<%= application.getInitParameter("key.one") %>'/>
  <jsp:setProperty property="crypt" name="pdfSource" value='<%= application.getInitParameter("key.two") %>'/>
  <jsp:setProperty property="invoice" name="pdfSource" param='in'/>
</jsp:useBean>             

<html>
  ${pdfSource.pdf}
</html>