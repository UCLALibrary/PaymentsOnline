<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=windows-1252" errorPage="errors.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<c:if test="${empty param.uid and empty header.SHIBUCLAUNIVERSITYID and empty header.SHIBEDUPERSONPRINCIPALNAME and empty cookie.almaID}">
  <c:redirect url="https://webservices-test.library.ucla.edu/lpo/landing.html"/>
</c:if>

<c:set var="fakeUID" value="800328433"/>
<c:set var="fakeLogon" value=""/>

<jsp:useBean id="idSource"
             class="edu.ucla.library.libservices.webservices.ecommerce.utility.user.LogonSetter">
  <jsp:setProperty name="idSource" property="uidParam" param="uid"/>
  <jsp:setProperty name="idSource" property="shibUID" value="${header.SHIBUCLAUNIVERSITYID}"/>
  <jsp:setProperty name="idSource" property="shibName" value="${header.SHIBEDUPERSONPRINCIPALNAME}"/>
</jsp:useBean>

<jsp:useBean id="libBillInvoices"
             class="edu.ucla.library.libservices.webservices.ecommerce.web.clients.PatronClient">
  <jsp:setProperty property="userID" name="libBillInvoices" value="${idSource.userID}"/>
  <%--jsp:setProperty property="UID" name="libBillInvoices" value="${idSource.userID}"/>
  <jsp:setProperty property="logonID" name="libBillInvoices" value="${idSource.userID}"/--%>
  <jsp:setProperty property="uriBase" name="libBillInvoices" value='<%= application.getInitParameter("uri.base") %>'/>
  <jsp:setProperty property="resourceURI" name="libBillInvoices" value='<%= application.getInitParameter("uri.patron") %>'/>
  <jsp:setProperty property="user" name="libBillInvoices" value='<%= application.getInitParameter("key.one") %>'/>
  <jsp:setProperty property="crypt" name="libBillInvoices" value='<%= application.getInitParameter("key.two") %>'/>
</jsp:useBean>

<jsp:useBean id="almaSource"
             class="edu.ucla.library.libservices.webservices.ecommerce.web.clients.AlmaClient">
  <jsp:setProperty property="userID" name="almaSource" value="${idSource.userID}"/>
  <jsp:setProperty property="uriBase" name="almaSource" value='<%= application.getInitParameter("alma.base.fees") %>'/>
  <jsp:setProperty property="resourceURI" name="almaSource" value='<%= application.getInitParameter("alma.resource.fees") %>'/>
  <jsp:setProperty property="key" name="almaSource" value='<%= application.getInitParameter("alma.key") %>'/>
</jsp:useBean>

<%--jsp:useBean id="invoiceSource"
             class="edu.ucla.library.libservices.webservices.ecommerce.web.clients.InvoiceClient">
  <jsp:setProperty property="uriBase" name="invoiceSource" value='<%= application.getInitParameter("uri.base") %>'/>
  <jsp:setProperty property="resourceURI" name="invoiceSource" value='<%= application.getInitParameter("uri.invoice") %>'/>
  <jsp:setProperty property="user" name="invoiceSource" value='<%= application.getInitParameter("key.one") %>'/>
  <jsp:setProperty property="crypt" name="invoiceSource" value='<%= application.getInitParameter("key.two") %>'/>
</jsp:useBean--%>

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
          <img src="http://www.library.ucla.edu/images/logo_blu_nobar.gif">
        </td>
        <td bgcolor="#536895" align="center">
          <font color="#ffffff" class="body"><b>Library Payments Online</b></font>
        </td>
        <td  width="155" bgcolor="#536895">
        </td>
      </tr>
    </table>

    <c:choose>
      <c:when test="${empty almaSource.thePatron.lastName}">
        <table width="960" border="0" align="center" cellpadding="0" cellspacing="0">
          <tr><td colspan="2">&nbsp;</td></tr>
          <tr>
            <td colspan="2">
              <h3>No patron information found</h3>
            </td>
          </tr>
          <tr>
            <td colspan="2">
              We're sorry: No patron record was found matching your login. Please contact UCLA Library Access 
              Services at <a href="mailto:AskUs@library.ucla.edu">AskUs@library.ucla.edu</a> and tell them Payments Online 
              reported that the logon/UCLA ID ${param.uid} did not match a patron record.
            </td>
          </tr>
        </table>
      </c:when>
      <c:otherwise>
        <table width="960" border="0" align="center" cellpadding="0" cellspacing="0">
          <tr><td></td><td>&nbsp;</td></tr>
          <tr>
            <td>
              <h3>${almaSource.thePatron.firstName}&nbsp;${almaSource.thePatron.lastName}</h3>
            </td>
            <td align="right">
              <form method="POST" action="https://webservices.library.ucla.edu/Shibboleth.sso/Logout"><!--?entityId=https://webservices.library.ucla.edu/lpo/shibboleth-sp"-->
                <input type="hidden" name="return" value="https://shb.ais.ucla.edu/shibboleth-idp/Logout"/>
                <input type="submit" value="Logout">
              </form>
            </td>
          </tr>
        </table>

        <table align="center" width="960" border="1" cellpadding="3">
          <tr>
            <td>
              The invoices below are due and payable. Please select the invoice 
              you want to pay by clicking the radio button. Pay one invoice 
              at a time. 
            </td>
          </tr>
        </table>
        <br/>
        <form method="POST" name="invoice_form" id="invoice_form" action="submit.jsp">

          <table align="center" width="960" class="footer">
            <tr>
              <th align="left">Invoice Number</th>
              <th align="left">Date</th>
              <th align="left">Department</th>
              <th align="right">Amount</th>
              <th align="left"></th>
            </tr>
            <c:set var="index" value="0"/>
            <c:set var="first" value="true"/>
            <c:forEach var="theInvoice" items="${libBillInvoices.thePatron.invoices}">
              <%--jsp:setProperty property="invoiceID" name="invoiceSource"
                               value="${theID}"/--%>
              <tr>
                <td>
                  <input type="radio" name="invoice" id='invoice' value="${theInvoice.invoiceNumber}" <c:if test="${first}">checked</c:if>>&nbsp;${theInvoice.invoiceNumber}<!--invoiceSource.-->
                </td>
                <td>
                  <fmt:formatDate value="${theInvoice.invoiceDate}" pattern="MMMM dd, yyyy"/>
                </td>
                <td>
                  ${theInvoice.locationName}
                </td>
                <td align="right">
                  <fmt:formatNumber currencySymbol="$" minFractionDigits="2" value="${theInvoice.balanceDue}" pattern="$###,###.##"/>
                  <%--&nbsp;<a href="https://webservices-test.library.ucla.edu/pdfoutput/pdfs/display_invoice/${theInvoice.invoiceNumber}/${libBillInvoices.thePatron.institutionID}" target="_blank">View Invoice (PDF)*</a>--%>
                  &nbsp;<a href="https://webservices.library.ucla.edu/pdfoutput/pdfs/display_invoice/${theInvoice.invoiceNumber}/${idSource.userID}" target="_blank">View Invoice (PDF)*</a>
                </td>
              </tr>
              <c:set var="index" value="${index + 1}"/>
              <c:set var="first" value="false"/>
            </c:forEach>
            <c:forEach var="theAlmaInvoice" items="${almaSource.theFees.fees}">
              <tr>
                <td>
                  <input type="radio" name="invoice" id='invoice' value="alma${theAlmaInvoice.invoiceNumber}" <c:if test="${first}">checked</c:if>>&nbsp;${theAlmaInvoice.invoiceNumber}<!--invoiceSource.-->
                </td>
                <td>
                  ${theAlmaInvoice.feeDate}
                </td>
                <td>
                  ${theAlmaInvoice.owner}
                </td>
                <td align="right">
                  <fmt:formatNumber currencySymbol="$" minFractionDigits="2" value="${theAlmaInvoice.balance}" pattern="$###,###.##"/>
                  &nbsp;<a href="https://webservices-test.library.ucla.edu/pdfoutput/pdfs/alma/${theAlmaInvoice.invoiceNumber}/${idSource.userID}" target="_blank">View Invoice (PDF)*</a>
                </td>
              </tr>
              <c:set var="index" value="${index + 1}"/>
              <c:set var="first" value="false"/>
            </c:forEach>
            <c:if test="${index eq 0}">
              <tr>
                <td colspan="0">
                  No unpaid invoices found. If you believe you should have invoices to pay, please contact 
                  UCLA Library Business Services at 310-825-8416 or 
                  <a href="mailto:mailto:lbs-billing@library.ucla.edu">lbs-billing@library.ucla.edu</a>.
                </td>
              </tr>
            </c:if>
            <tr>
              <td colspan="3"><p>&nbsp;</p>
                <input type="submit" value="Continue to checkout"></td>
              </td>
            </tr>
          </table>
          <input type="hidden" name="patronID" value="${idSource.userID}"/>
        </form>
        <br/>&nbsp;<br/>
        <table align="center" width="960" border="0" cellpadding="3">
          <tr>
            <td>
              *You will need Adobe Acrobat to view the invoices. Adobe Acrobat 
              is available for free <a href="http://adobe.com" target="_blank">here</a>. 
              <br/>&nbsp<br/>
              Please log off when you are finished.
            </td>
          </tr>
        </table>
      </c:otherwise>
    </c:choose>
  </body>
</html>