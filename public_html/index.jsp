<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=windows-1252"%>
<html>
	<head>
		<title>
		UCLA Library Payment Service
		</title>
		<META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE"> 
		<META HTTP-EQUIV="cache-control" CONTENT="NO-CACHE"> 
		<META HTTP-EQUIV="cache-control" CONTENT="NO-STORE"> 
		<META HTTP-EQUIV="cache-control" CONTENT="PRIVATE"> 
		<link href="http://www.library.ucla.edu/css/wht.css" rel="stylesheet" type="text/css">
	</head>
	<body bgcolor="#FFFFFF" topmargin="0" marginheight="0" marginwidth="0" leftmargin="0">
		<table width="100%" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td width="165" bgcolor="#536895" align="center">
					<img src="http://www.library.ucla.edu/images/logo_blu_nobar.gif">
				</td>
				<td bgcolor="#536895" align="center">
					<font color="#ffffff" class="body"><b>Payments Online</b></font>
				</td>
				<td  width="155" bgcolor="#536895">
				</td>
			</tr>
		</table>
		<p>&nbsp;</p>
		<table align="center">
			<tr>
				<td>
					<h3>Welcome to the UCLA Library Payment Service</h3>
					<p>&nbsp;</p>
				</td>
			</tr>
			<!--tr>
				<td>
					UCLA Students must pay through the SBAR system: <a href="protected/invoices.jsp">Click HERE to go to SBAR</a>.
					<p>&nbsp;</p>
				</td>
			</tr>
			<tr>
				<td>
					All other patrons can pay using this service.  Click the button below to begin.
					<p>&nbsp;</p>
				</td>
			</tr!-->
			<tr>
				<td>
					<form action="protected/invoices.jsp">
            <label for="uid">UID: </label>
            <input id="uid" type="text" name="uid" value="" size="10" maxlength="14"/>
						<input type="submit" value="login"><!--   UCLA Faculty and Staff login through campus authentication  -->
					</form>
				</td>
			</tr>
		</table>
  </body>
</html>