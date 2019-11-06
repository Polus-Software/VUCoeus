<%--
/*
 * @(#) SelectPerson.jsp 1.0 2002/06/05
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  Geo Thomas.
 */
--%>
<%--
    This Page is to select a person when the user eneters into FinancialEntity.
    This page is using HNCTemplate.jsp as a  template that supports and layout the
    components in a standard fashion.
--%>
<%@ page import="java.util.*,java.text.*,edu.mit.coeus.coi.bean.*"
	errorPage="ErrorPage.jsp"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri='/WEB-INF/struts-bean.tld' prefix='bean' %>
<%@ taglib uri='/WEB-INF/struts-logic.tld' prefix='logic' %>
<%@ taglib uri='/WEB-INF/response.tld' prefix='res' %>
<%@ taglib uri='/WEB-INF/session.tld' prefix='sess' %>
<%@ taglib uri="/WEB-INF/privilege.tld" prefix="priv" %>
<%@ include file="CoeusContextPath.jsp"  %>

<html:html locale='true'>
<head>
<title><template:get name='title'/></title>
<html:base/>
<style type="text/css">
 div {font-family:verdana;font-size:10px}
 a {font-family:verdana;font-size:10px;color:blue;text-decoration:none;}
 a:active {color:red;text-decoration:none;}
 a:visited {color:blue;text-decoration:none;}
 a:hover {color:red;text-decoration:none;}
</style>

<script language='text/javascript' src='scripts/UtilScripts.js'>
</script>

<script language="JavaScript">
//function for opening a new window
function openwin(filename,winname){
    var newwin=window.open(filename,winname,"height=320,width=778,top=10,left=5,scrollbars=1");
}
</script>
</head>
<%	System.out.println("begin selectperson.jsp");	%>
<body bgcolor="#FFFFFF" onload="JavaScript:openwin('PersonSearch.jsp','person');">
<div id="Layer1" style="position:absolute; left:0px; top:0px; width:779px; height:110px; z-index:1">

<img src="<bean:write name="ctxtPath"/>/images/coeusheader.gif" width="780" height="111" usemap="#Map2" border="0">
<map name="Map2"><area shape="poly" coords="169,26,179,36" href="#"></map>
</div>
<div id="Layer2" style="position:absolute; left:0px; top:111px; width:128px; height:190px; z-index:2">
<jsp:useBean id = "userprivilege" class = "java.lang.String" scope="session"/>
<%	System.out.println("after useBean userprivilege");	%>
<table width="100" border="0" cellspacing="0" cellpadding="0" background="<bean:write name='ctxtPath'/>/images/coeusmenu-03.gif">
<tr>
<td>
 <html:link page="/getcoidisclosure.do">
  <img src="<bean:write name="ctxtPath"/>/images/coeusmenu.gif" width="129" height="23" border="0">
  </html:link>
</td>
</tr>
<tr>
<td>
 <html:link page="/getFinEntities.do">
  <img src="<bean:write name="ctxtPath"/>/images/coeusmenu-02.gif" width="129" height="21" border="0">
 </html:link>
</td>
</tr>
<priv:hasOSPRight name="hasOspRightToView" value="<%=Integer.parseInt(userprivilege)%>">
<%	System.out.println("after parseInt(userprivilege)");	%>
<tr>
<td>
 <html:link href="JavaScript:openwin('PersonSearch.jsp','person');" >
 <%	System.out.println("after javascript:openwindown");	%>
  <img src="<bean:write name="ctxtPath"/>/images/sperson.gif" width="129" height="21" border="0">
 </html:link>
</td>
</tr>
</priv:hasOSPRight>
<%	System.out.println("end of </priv:hasOSPRight>");	%>
<tr>
<td>
 <html:link page="/newdisclosure.do">
  <img src="<bean:write name="ctxtPath"/>/images/newdisclosure.gif" width="129" height="21" border="0">
 </html:link>
</td>
</tr>
<tr>
<td>
 <html:link page="/WelcomeCOI.jsp">
  <img src="<bean:write name="ctxtPath"/>/images/home.gif" width="129" height="19" border="0">
 </html:link>
</td>
</tr>
<tr>
<td height="79">&nbsp;</td>
</tr>
<tr valign="bottom">
<td>
<p><img src="<bean:write name="ctxtPath"/>/images/bottom.gif" width="129" height="5"></p>
</td>
</tr>
</table>
</div>
<div id="Layer3" style="position:absolute; left:130px; top:107px; width:650px; height:353px; z-index:3">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td height="5"></td>
  </tr>
  <tr bgcolor="#cccccc">
    <td height="23"> &nbsp;<font face="Verdana, Arial, Helvetica, sans-serif" size="2"><b>
            <bean:message key="selectPerson.header" /></b></font></td>
  </tr>
  <tr>
    <td height="46">
    <%System.out.println("before bean:message key='selectPerson.message'");	%>
      <div align="center"><b><bean:message key="selectPerson.message" /></b></div>
      <%	System.out.println("after bean:message key='selectperson.message'");	%>
    </td>
  </tr>
</table>
</div>
</body>
</html:html>