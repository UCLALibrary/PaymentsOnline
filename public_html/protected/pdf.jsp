<%@ page contentType="application/pdf;charset=UTF-8"%>
<jsp:useBean id="pdfSource" class="edu.ucla.library.libservices.webservices.ecommerce.web.clients.XeroInvoiceClient">
  <jsp:setProperty property="invoiceID" name="pdfSource" value="${invNumber}"/>
  <jsp:setProperty property="secretsFile" name="pdfSource" value='<%= application.getInitParameter("xero.secrets") %>'/>
  <jsp:setProperty property="tokensFile" name="pdfSource" value='<%= application.getInitParameter("xero.tokens") %>'/>
</jsp:useBean>             

  ${pdfSource.invoicePDF}

