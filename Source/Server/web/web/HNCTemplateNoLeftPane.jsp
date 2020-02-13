<%--
/*
 * @(#)HNCTemplate.jsp	1.0 2002/06/12 01:32:13 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  RaYaKu
 */
--%>
<%--
A Standard Template to layout the components in a fashion.
Acronym of HNC is H-Header,N-Naviagation(leftpane) and C-Content .
--%>
<%@page errorPage = "ErrorPage.jsp" %>
<%@ taglib uri='/WEB-INF/struts-template.tld' prefix='template' %>
<%@ taglib uri='/WEB-INF/struts-html.tld' prefix='html' %>
<%@ taglib uri='/WEB-INF/struts-bean.tld' prefix='bean' %>

<%@ include file="CoeusContextPath.jsp"  %>

<html:html locale='true'>
<head>
<title><template:get name='title'/></title>
<html:base/>
<style type="text/css">
	div {font-family:verdana;font-size:12px}
	a {color:blue;text-decoration:none;}
	a:active {color:red;text-decoration:none;}
	a:visited {color:blue;text-decoration:none;}
	a:hover {color:red;text-decoration:none;}
	h1 {font-size: 16px}
	h2 {font-size: 14px}
	h3 {font-size: 12px}
	body, td, th, p, a, h4, h3, h2, h1 {font-family:verdana, helvetica, sans-serif}
	body, td, h4 {font-family:verdana, helvetica, sans-serif; font-size:12px}
	.mediumsize {font-family:verdana, helvetica, sans-serif; font-size:12px}
	.mediumsizebrown {font-family:verdana, helvetica, sans-serif; font-size:12px; color:#7F1B00}
	.mediumsizered {font-family:verdana, helvetica, sans-serif; font-size:12px; color:red}
	.smallbrown {font-family:verdana, helvetica, sans-serif; font-size:10px; color:#7F1B00}
	.smallsize {font-family:verdana, helvetica, sans-serif; font-size:10px}
	.smallred {font-family:verdana, helvetica, sans-serif; font-size:10px; color:red}
	.header {font-family:verdana, helvetica, sans-serif; font-size:13px; font-weight:bold}
</style>
<script language='javascript' src='<bean:write name="ctxtPath"/>/web/scripts/UtilScripts.js'>
</script>
</head>
<body bgcolor="#FFFFFF"  leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="780" border="0" cellpadding="0" cellspacing="0">
<!-- Application Header page information -->
  <tr bgcolor="#7F1B00">
    <td height="109" ><template:get name='header'/></td>
  </tr>
  <tr> 
 <!-- Application Navigation  page information -->
    <%--<td width="123" valign="top"><template:get name='navbar'/></td>--%>
 <!-- Actual Content of a page -->
    <td width="780" valign="top" align="left"><template:get name='actualContent'/></td>
  </tr>
</table>
</body>
</html:html>