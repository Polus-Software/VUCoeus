<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java"
	isErrorPage="true"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@page import="edu.mit.coeus.utils.UtilFactory"%>

<html:html>

<head>
<html:base />
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<script language="javascript">
        </script>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title><bean:message key="logon.label.title" /></title>
</head>

<body>

	<table align="center" width="80%" height="100%" class='table'
		border="0" cellpadding="0">
		<tr>
			<td>
				<table width="100%" cellpadding="0" cellspacing="0"
					STYLE="background-image:url('<bean:write name='ctxtPath'/>/coeusliteimages/header_background.gif');border:0">
					<tr>
						<td><img
							src="<bean:write name='ctxtPath'/>/coeusliteimages/header_coeus1.gif"
							width="675" height="50"></td>
					</tr>
				</table>
			</td>
		</tr>

		<tr>
			<td align="right" class="copy"><a
				href="<bean:message key="logon.helpUrl"/>"> <bean:message
						key="logon.label.help" />
			</a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		</tr>
		<%
    Exception ex = (Exception)request.getAttribute("Exception");
      String rootCause = ex!=null?("<i>Root cause</i> : "+ex.getMessage()):"";
      UtilFactory.log("FROM COEUS LITE : "+ex.getMessage(), "cwLogonError");
%>
		<tr>
			<td align="center" class="copy"><b><bean:message
						key="logon.label.invalid1" /></b> <br>
			<b><%=rootCause%></b> <br> <br>
			<b><bean:message key="logon.label.invalid2" /></b> <br> <br>
				<br></td>
		</tr>

		<tr>
			<td align="center" class="copy"><html:link forward="logout">
					<bean:message key="logon.label.try" />
				</html:link></td>
		</tr>

		<tr>
			<td height='70' align="center" valign="top" class="copy"
				class='tabtable'></td>
		</tr>

	</table>

	<table bgcolor='#376DAA' align="center" width="80%" height="100%"
		border="0" cellpadding="0">
		<tr>
			<td align="left"><font color="white" size="-1"> <bean:message
						key="label.copywriteMIT" />
			</font></td>
			<td align="right"><font color="white" size="-1"> <bean:message
						key="label.copywirteCoeus" />
			</font></td>
		</tr>
	</table>

</body>

</html:html>