<%--
/**
 * @(#)NSFProgAnnContent.jsp
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author: ES
 * @version 1.0 $ $ Date Dec 18, 2002 $ $
 *
 * Display list of NSF Program Announcements
 * stored in request object by DisplayNSFProgAnnAction.
 * For attributes for which a null value is permitted in the database,
 * check for null value.  If value is null, display an empty table cell.
 */
--%>
<%@ page language="java" %>
<%@ page  errorPage="ErrorPage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%	System.out.println("enter not details content"); %>

<jsp:useBean id="nsfProgAnn"
		class="edu.mit.coeus.nsfProgAnn.bean.NSFProgAnnBean"
		scope="session" />

<html:html>
<body>

<table border = "0" width="100%">
<tr>
	<td bgcolor=#aa3333 colspan=2><b><font color=#FBF7F7>
		NSF Program Announcements</font></b>
	</td>

</tr>
</table>

<table border = "0" width="100%">
<tr>
        <td width="10%"><b>Program Announcement ID</b></td>
        <td width="69%" align="center" ><b>Description</b></td>
        <td width="10%"><b>Effective Date</b></td>
        <td width="10%"><b>Expiration Date</b></td>
        <td width="1%"></td>
</tr>

<% int count =0; %>

<logic:iterate id= "index" name= "nsfProgAnn" property= "progAnnounceID">



<%
	Integer countInt = new Integer(count);
	String countString = countInt.toString();
%>

<tr bgcolor='<%=( (count) % 2 == 0 ? "#FBF7F7" : "#F7EEEE") %>'>



	<td>

              <bean:write name="nsfProgAnn" property='<%= "progAnnounceID[" + countString + "]"  %>' />

	</td>


	<td>

                <a href="displayNSFProgAnnDets.do?progAnnounceID=<bean:write name="nsfProgAnn" property='<%="progAnnounceID[" + countString + "]"  %>' />  ">
		<bean:write name="nsfProgAnn" property='<%= "progAnnounceDesc[" + countString + "]"  %>' />
                </a>
	</td>

<!-- CASE #748 Comment Begin -->
<%--
        <td>
             <logic:notEqual name="nsfProgAnn" property='<%= "effectiveDt[" + countString + "]" %>' value="null">
		<bean:write name="nsfProgAnn" property='<%= "effectiveDt[" + countString + "]" %>' />
	    </logic:notEqual>
            &nbsp
        </td>


        <td>
	       <logic:notEqual name="nsfProgAnn" property='<%= "expirationDt[" + countString + "]" %>' value="null">
		 <bean:write name="nsfProgAnn" property='<%= "expirationDt[" + countString + "]" %>' />
		</logic:notEqual>
	            &nbsp
        </td>
--%>
<!-- CASE #748 Comment End -->
<!-- CASE #748 Begin -->
        <td>
             <logic:present name="nsfProgAnn" property='<%= "effectiveDt[" + countString + "]" %>'>
		<bean:write name="nsfProgAnn" property='<%= "effectiveDt[" + countString + "]" %>' />
	    </logic:present>
            &nbsp
        </td>


        <td>
	       <logic:present name="nsfProgAnn" property='<%= "expirationDt[" + countString + "]" %>'>
		 <bean:write name="nsfProgAnn" property='<%= "expirationDt[" + countString + "]" %>' />
		</logic:present>
	            &nbsp
        </td>
<!-- CASE #748 End -->
</tr>

<% count++; %>

</logic:iterate>


</table>
</body>
</html:html>