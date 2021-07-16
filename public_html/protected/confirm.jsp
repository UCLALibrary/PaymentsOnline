<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=windows-1252" errorPage="errors.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>

<c:set var="invoiceNo" value="${fn:replace(param.UCLA_REF_NO, 'alma', '')}"/>

<jsp:useBean id="receiptSource"
             class="edu.ucla.library.libservices.webservices.ecommerce.web.clients.ReceiptClient">
  <jsp:setProperty property="invoiceNumber" name="receiptSource" value="${param.UCLA_REF_NO}"/>
  <jsp:setProperty property="almaUriBase" name="receiptSource" value='<%= application.getInitParameter("alma.base.fees") %>'/>
  <jsp:setProperty property="apiKey" name="receiptSource" value='<%= application.getInitParameter("alma.key") %>'/>
  <jsp:setProperty property="vgerName" name="receiptSource" value='<%= application.getInitParameter("datasource.ucladb") %>'/>
  <jsp:setProperty property="libBillName" name="receiptSource" value='<%= application.getInitParameter("datasource.invoice") %>'/>
  <jsp:setProperty property="uriBase" name="receiptSource" value='<%= application.getInitParameter("uri.base") %>'/>
  <jsp:setProperty property="resourceURI" name="receiptSource" value='<%= application.getInitParameter("uri.receipt") %>'/>
  <jsp:setProperty property="user" name="receiptSource" value='<%= application.getInitParameter("key.one") %>'/>
  <jsp:setProperty property="crypt" name="receiptSource" value='<%= application.getInitParameter("key.two") %>'/>
</jsp:useBean>

<jsp:useBean id="now" class="java.util.Date" scope="page"/>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
    <meta http-equiv="PRAGMA" content="NO-CACHE"> 
    <meta http-equiv="cache-control" content="NO-CACHE"> 
    <meta http-equiv="cache-control" content="NO-STORE"> 
    <meta http-equiv="cache-control" content="PRIVATE"> 
    <link href="http://www.library.ucla.edu/css/wht.css" rel="stylesheet" type="text/css">
    <title>UCLA Library Payments Online</title>
  </head>
  <body bgcolor="#FFFFFF" topmargin="0" marginheight="0" marginwidth="0" leftmargin="0" width="960">
    <table width="960" cellpadding="0" cellspacing="0" align="center">
      <tr>
        <td width="165" bgcolor="#536895" align="center">
          <img src="http://www.library.ucla.edu/images/logo_blu_nobar.gif" alt="UCLA">
          <!--img src="../images/UCLAbanner22535.png"-->
        </td>
        <td bgcolor="#536895" align="center">
          <font color="#ffffff" class="body"><b>Library Payments Online</b></font>
        </td>
        <td  width="155" bgcolor="#536895">
        </td>
      </tr>
    </table>
    <c:choose>
      <c:when test="${param.result eq 0}">
        <!-- success output -->
        <table width="960" border="0" align="center" cellpadding="0" cellspacing="0">
          <tr><td colspan="2">&nbsp;</td></tr>
          <tr>
            <td>&nbsp;</td>
            <td align="right">
              <form method="POST" action="https://webservices.library.ucla.edu/Shibboleth.sso/Logout"><!--?entityId=https://webservices.library.ucla.edu/lpo/shibboleth-sp"-->
                <input type="hidden" name="return" value="https://shb.ais.ucla.edu/shibboleth-idp/Logout"/>
                <input type="submit" value="Logout">
              </form>
            </td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td>
              <h3>Payment received for invoice ${invoiceNo} on <fmt:formatDate value="${now}" pattern="MMMM dd, yyyy"/></h3>
            </td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td>
              Thank you, ${receiptSource.theReceipt.userName}.<br/>
              <%
                double total = 0.0d;
                for ( int index = 1;
                      index <= Integer.parseInt( request.getParameter( "itemcnt" ) );
                      index++ )
                {
                  double amount = Double.parseDouble( request.getParameter( "amount".concat( String.valueOf( index )) ) );
                  total += amount;
                }
                out.print( "A payment of " + new java.text.DecimalFormat( "$###########0.00" ).format( total ) 
                + " by " + ( request.getParameter( "pmtcode" ).equalsIgnoreCase( "CC" ) ? "credit card" : "e-check" ) 
                + " has been received for invoice " + request.getParameter( "UCLA_REF_NO" ).replace("alma", "") + ".<br/>" );
              %>
              Payment transaction number: ${param.tx}
            </td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td>
              <c:choose>
                <c:when test="${receiptSource.theReceipt.status eq 'Paid'}">
                  This invoice has been paid in full.
                </c:when>
                <c:when test="${receiptSource.theReceipt.status eq 'Deposit Paid'}">
                  The deposit on this invoice has been paid.
                </c:when>
                <c:otherwise>
                  Other invoice status reported: ${receiptSource.theReceipt.status}.
                </c:otherwise>
              </c:choose>
            </td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td>A receipt has also been emailed to the address you entered on the payment screen.</td>
          </tr>
          <c:if test="${receiptSource.theReceipt.unpaid gt 0}">
            <tr>
              <td>&nbsp;</td>
              <td>
                <!--a href="invoices.jsp">Pay another invoice</a-->
                <form action="invoices.jsp">
                  <input id="uid" type="hidden" name="uid" value="${receiptSource.theReceipt.uid}"/>
                  <input type="submit" value="Pay another invoice">
                </form>
              </td>
            </tr>
          </c:if>
        </table>
      </c:when>
      <c:otherwise>
        <!-- failure output -->
        <table width="960" border="0" align="center" cellpadding="0" cellspacing="0">
          <tr><td colspan="2">&nbsp;</td></tr>
          <tr>
            <td>&nbsp;</td>
            <td>
              <h3>Sorry, ${receiptSource.theReceipt.userName}, your payment for invoice ${invoiceNo} was not successful.</h3>
            </td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td>
              <h3>The payment system returned this message:<br/>
              ${param.respmessage}</h3>              
            </td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td>
              <h3>Failed Transaction #${param.failedtx}.</h3>
            </td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td>
              If you need help and wish to contact the UCLA Library, please 
              include:  transaction number listed above, your name, invoice 
              number, email address or telephone number, date and approximate 
              time of the failed transaction. 
            </td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td>
              <em>Technical problem?</em>&nbsp;<a href="mailto:helpdesk@library.ucla.edu">helpdesk@library.ucla.edu</a>.
            </td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td>
              <em>Not a technical problem or unsure?</em>&nbsp;
              <a href="lbs-billing@library.ucla.edu">lbs-billing@library.ucla.edu</a>.
              Please be patient; we will respond during regular business hours.  
              Or between 9:00 a.m. and 5:00 p.m. Pacific Time Monday through 
              Friday, 310-825-8416.
            </td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td>
              Thank you. 
            </td>
          </tr>
        </table>
      </c:otherwise>
    </c:choose>
  </body>
</html>