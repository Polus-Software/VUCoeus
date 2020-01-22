<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java"
	isErrorPage="true"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>

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
				<!-- JM 5-31-2011 updated per 4.4.2 -->
				<table width="100%" cellpadding="0" cellspacing="0"
					STYLE="background-color: black; border: 0">
					<tr>
						<!-- JM 5-31-2011 updated per 4.4.2 -->
						<td><img
							src="<bean:write name='ctxtPath'/><bean:message key="loginPage.headerImage"/>"
							width="675" height="82"></td>
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

	<!-- JM 5-31-2011 updated per 4.4.2 -->
	<table bgcolor='#000000' align="center" width="80%" height="100%"
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