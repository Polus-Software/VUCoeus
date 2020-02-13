<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page
	import="java.util.Vector,
                edu.mit.coeuslite.utils.bean.PlaceHolderBean"%>

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<jsp:useBean id="placeholder" scope="request"
	class="java.util.Hashtable" />


<html>
<head>
<title></title>
<!--<link href="css/ipf.css" rel="stylesheet" type="text/css">
        <style type="text/css">
        </style>-->
<script>
        
    </script>
</head>
<body>
	<html:form action="/cwPerson.do?actionType=insert" method="post">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">

			<tr>
				<bean:message key="label.TestingPOC" />
				<%--Testing POC for the Customization of the JSP's.--%>
			</tr>

			<tr rowspan=3>&nbsp;
			</tr>
			<tr>
				<font size='4' color='blue' face='bold' style=''> <bean:message
						key="label.PersonDetails" /> <%--Person Details--%>
				</font>
			</tr>
			<tr>
				<td width="18%" align="left"><bean:message key="PersonId" /> <%--PersonId--%>&nbsp;&nbsp;
				</td>
				<td width="80%" align="left"><html:text property="personId"
						styleClass="textbox" /></td>
			</tr>
			<tr>
				<td width="5%" align="left"><bean:message
						key="keyStudyPerLabel.PersonName" /> <%--PersonName--%>
					:&nbsp;&nbsp;</td>
				<td width="80%" align="left"><html:text property="personName"
						styleClass="textbox-long" />
			</tr>
			<tr>
				<td width="5%" align="left"><bean:message key="label.Age" /> <%--Age--%>:&nbsp;&nbsp;
				</td>
				<td width="80%" align="left"><html:text property="age"
						styleClass="textbox" />
			</tr>
			<tr>
				<td width="5%" align="left"><bean:message key="label.DOB" /> <%--DOB:--%>&nbsp;&nbsp;
				</td>
				<td width="80%" align="left"><html:text property="dob"
						styleClass="textbox" />
			</tr>

			<% String includeFileName="";
        PlaceHolderBean bean = (PlaceHolderBean)placeholder.get("cwPersonPH1");
        if(bean!= null && bean.getIncludeFile()!= null){
        includeFileName = bean.getIncludeFile();%>
			<jsp:include page="<%=includeFileName%>" />
			<% }
    %>


			<%--<tr>
  <td width = "5%" align="left">EmailId:&nbsp;&nbsp;  </td>
  <td width = "80%" align="left"><html:text size="50" property = "emailId" styleClass="textbox" />
  </tr>--%>
			<tr>
				<td width="5%" align="left"><bean:message key="label.Salary" />
					<%--Salary:--%> &nbsp;&nbsp;</td>
				<td width="80%" align="left"><html:text property="salary"
						styleClass="textbox" />
			</tr>
			<tr>
				<td width="5%" align="left"><bean:message
						key="label.MaritalStatus" /> <%--MaritalStatus:--%> &nbsp;</td>
				<td width="80%" align="left"><html:checkbox
						property="maritalStatus" />
			</tr>
			<tr>
				<td width="5%" align="left"><bean:message key="label.Timestamp" />
					<%--Timestamp:--%> &nbsp;&nbsp;</td>
				<td width="80%" align="left"><html:text property="timeStamp" />
			</tr>
			<% String academic="";
        PlaceHolderBean placeHolderBean = (PlaceHolderBean)placeholder.get("cwPersonPH2");
        if(placeHolderBean!= null && placeHolderBean.getIncludeFile()!= null){
        academic = placeHolderBean.getIncludeFile();%>
			<jsp:include page="<%=academic%>" />
			<% }
    %>
			<tr>
				<td width="5%" align="right"><html:submit value="submit"
						onclick="/cwPerson.do" onclick="showAcType()" /></td>
			</tr>



		</table>
	</html:form>
</body>
</html>
