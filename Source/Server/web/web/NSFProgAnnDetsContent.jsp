<%--
/**
 * @(#)NSFProgAnnDetsContent.jsp
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author: ES
 * @version 1.0 $ $ Date Dec 18, 2002 $ $
 *
 * details of program announcement
 */
--%>
<%@ page language="java" %>
<%@ page  errorPage="ErrorPage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ include file="CoeusContextPath.jsp"  %>


<jsp:useBean id="nsfProgAnnDets"
		class="edu.mit.coeus.nsfProgAnn.bean.NSFProgAnnDetsBean"
		scope="session" />


<html:html>
<body>

<table width="100%" border="0">
<tr>
<td width="90%"&nbsp;</td>

<td>
  <a href="JavaScript:history.back();">
  <img src="<bean:write name="ctxtPath"/>/images/goback.gif" width="55" height="22" border="0">
									</a>
</td>
</tr>
</table>


<table border = "0" width="100%">
<tr>

        <td bgcolor=#cccccc colspan=2><b><font color=#aa3333>
	<b>Program Announcement ID <bean:write name="nsfProgAnnDets" property="progAnnounceID[0]" /> </b>
	</td>

</tr>
</table>


<table border = "0" width="100%">
<tr>
        <td width="32%"><b>Division Name</b></td>
        <td width="10%"><b>Division code</b></td>
        <td width="8%"><b>Program element code</b></td>
	<td width="30%"><b>Program name</b></td>
	<td width="9%"><b>Org unit code</b></td>
        <td width="1%"></td>
</tr>



<% int count =0; %>

<logic:iterate id= "index" name= "nsfProgAnnDets" property= "progAnnounceID">

<%
	Integer countInt = new Integer(count);
	String countString = countInt.toString();
%>

<tr bgcolor='<%=( (count) % 2 == 0 ? "#FBF7F7" : "#F7EEEE") %>'>


	<td>
              <bean:write name="nsfProgAnnDets" property='<%= "divisionName[" + countString + "]"  %>' />
	</td>

<td>
             <logic:notEqual name="nsfProgAnnDets" property='<%= "divisionCode[" + countString + "]" %>' value="null">
		<bean:write name="nsfProgAnnDets" property='<%= "divisionCode[" + countString + "]" %>' />
	    </logic:notEqual>
            &nbsp
        </td>


	<td>
              <bean:write name="nsfProgAnnDets" property='<%= "programElementCode[" + countString + "]"  %>' />
	</td>

	<td>
              <bean:write name="nsfProgAnnDets" property='<%= "programName[" + countString + "]"  %>' />
	</td>

	<td>
              <bean:write name="nsfProgAnnDets" property='<%= "orgUnitCode[" + countString + "]"  %>' />
	</td>
</tr>

<% count++; %>

</logic:iterate>
</table>

<table width="100%" border="0">
<tr>
<td width="90%"&nbsp;</td>

<td>
  <a href="JavaScript:history.back();">
  <img src="<bean:write name="ctxtPath"/>/images/goback.gif" width="55" height="22" border="0">
									</a>
</td>
</tr>
</table>

</body>
</html:html>