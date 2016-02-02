<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=windows-1252" errorPage="errors.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:useBean id="invoiceSource"
             class="edu.ucla.library.libservices.webservices.ecommerce.web.clients.CashNetClient">
  <jsp:setProperty property="uriBase" name="invoiceSource" value='<%= application.getInitParameter("uri.base") %>'/>
  <jsp:setProperty property="resourceURI" name="invoiceSource" value='<%= application.getInitParameter("uri.cashnet") %>'/>
  <jsp:setProperty property="user" name="invoiceSource" value='<%= application.getInitParameter("key.one") %>'/>
  <jsp:setProperty property="crypt" name="invoiceSource" value='<%= application.getInitParameter("key.two") %>'/>
  <jsp:setProperty property="invoiceID" name="invoiceSource" param="invoice"/>
</jsp:useBean>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
    <title>submit</title>
    <script language="javascript" type="text/javascript">
      function submitForm()
      {
        document.forms["Demo1"].submit();
      }
    </script>
  </head>
  <body onload="javascript:submitForm();">
    <!--form id="Demo1" method="post" action="https://commerce.cashnet.com/404handler/pageredirpost.aspx?virtual=LIBTEST"-->
    <form id="Demo1" method="post" action="https://commerce.cashnet.com/404Handler/pageredirpost.aspx?virtual=UCLALIBRARY">
      <div>
        <c:set var="index" value="0"/>
        <c:forEach var="theLine" items="${invoiceSource.theInvoice.lineItems}">
          <c:set var="index" value="${index + 1}"/>
          <input id="Hidden" type="hidden" name="itemcode${index}" value="${theLine.itemCode}"/>
          <input id="Hidden" type="hidden" name="amount${index}" value="${theLine.totalPrice}"/>
          <input id="Hidden" type="hidden" name="desc${index}" value="${theLine.invoiceNumber}-${theLine.itemCode}"/>
        </c:forEach>
        <c:if test="${index gt 1}">
          <input id="Hidden" type="hidden" name="itemcnt" value="${index}"/>
        </c:if>
        <input id="Hidden" type="hidden" name="ucla_ref_no" value="${invoiceSource.theInvoice.invoiceNumber}"/>
        <!--input id="Hidden" type="hidden" name="signoutURL" value="https://webservices-dev.library.ucla.edu/ecommerce-dev/protected/confirm.jsp"/-->
        <!--input id="Hidden" type="hidden" name="signoutURL" value="https://webservices-test.library.ucla.edu/ecommerce-test/protected/confirm.jsp"/-->
        <input id="Hidden" type="hidden" name="signoutURL" value="https://webservices.library.ucla.edu/lpo/protected/confirm.jsp"/>
      </div>
      <!--input type="submit" value="Submit"/-->
    </form>
  </body>
</html>