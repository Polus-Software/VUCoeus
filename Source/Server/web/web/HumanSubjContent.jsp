<%--
/**
 * @(#)HumanSubjContent.jsp
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author: Coeus Dev Team
 * @version 1.0 $ $ Date Aug 6, 2002 $ $
 *
 * Display list of human subject trainees
 * Access HumanSubjBean
 * stored in request object by DisplayHumanSubjAction.
 * For attributes for which a null value is permitted in the database,
 * check for null value.  If value is null, display an empty table cell.
 */
--%>
<%@ page language="java" %>
<%@ page  errorPage="ErrorPage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<html:html>
<body>

<table border = "0" width="100%">
<tr>
	<td bgcolor=#aa3333 colspan=2><b><font color=#fffff7>
		People who have passed Human Subject Training</font></b>
	</td>

</tr>
</table>

<table border = "0" width="100%">
<tr>
        <td width="20%"><b>Full Name</b></td>
        <td width="20%"><b>Training</b></td>
        <td width="20%"><b>Department</b></td>
        <td width="15%"><b>Date Requested</b></td>
        <td width="15%"><b>Date Submitted</b></td>
        <td width="9%"><b>Score</b></td>
        <td width="1%"></td>
</tr>

<% int count =0; %>

<logic:iterate id= "index" name= "humansubj" property= "fullName">

<%
	Integer countInt = new Integer(count);
	String countString = countInt.toString();
%>

<tr bgcolor='<%=( (count) % 2 == 0 ? "#FBF7F7" : "#F7EEEE") %>'>



	<td>
              <bean:write name="humansubj" property='<%= "fullName[" + countString + "]"  %>' />
	</td>

	<td>
		<bean:write name="humansubj" property='<%= "description[" + countString + "]"  %>' />
	</td>
<!-- CASE #748 Comment Begin -->
<%--
        <td>
             <logic:notEqual name="humansubj" property='<%= "unitName[" + countString + "]" %>' value="null">
		<bean:write name="humansubj" property='<%= "unitName[" + countString + "]" %>' />
	    </logic:notEqual>
            &nbsp
        </td>
         <td>
             <logic:notEqual name="humansubj" property='<%= "dateRequestedDisplayString[" + countString + "]" %>' value="null">
		<bean:write name="humansubj" property='<%= "dateRequestedDisplayString[" + countString + "]" %>' />
	    </logic:notEqual>
            &nbsp
        </td>
         <td>
             <logic:notEqual name="humansubj" property='<%= "dateSubmittedDisplayString[" + countString + "]" %>' value="null">
		<bean:write name="humansubj" property='<%= "dateSubmittedDisplayString[" + countString + "]" %>' />
	    </logic:notEqual>
            &nbsp
        </td>
        
--%>
<!-- CASE #748 Comment End -->
<!-- CASE #748 Begin -->
        <td>
             <logic:present name="humansubj" property='<%= "unitName[" + countString + "]" %>'>
		<bean:write name="humansubj" property='<%= "unitName[" + countString + "]" %>' />
	    </logic:present>
            &nbsp
        </td>
         <td>
             <logic:present name="humansubj" property='<%= "dateRequestedDisplayString[" + countString + "]" %>'>
		<bean:write name="humansubj" property='<%= "dateRequestedDisplayString[" + countString + "]" %>' />
	    </logic:present>
            &nbsp
        </td>
         <td>
             <logic:present name="humansubj" property='<%= "dateSubmittedDisplayString[" + countString + "]" %>'>
		<bean:write name="humansubj" property='<%= "dateSubmittedDisplayString[" + countString + "]" %>' />
	    </logic:present>
            &nbsp
        </td>
        
<!-- CASE #748 End -->
        <td>
		<bean:write name="humansubj" property='<%= "score[" + countString + "]"  %>' />
                &nbsp
	</td>
</tr>

<% count++; %>

</logic:iterate>


</table>
</body>
</html:html>