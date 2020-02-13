<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<html:html>
<head>
<style>
.clsavebutton {
	width: 130px;
}
</style>
<title>Protocol Submission Success Page</title>
</head>
<body>
	<html:form action="/getProtocolData.do?PAGE=G">
		<table width='100%' cellpadding='2' cellspacing='0' class='table'
			align="center">
			<tr>
				<td>&nbsp; <br>
				</td>
			</tr>
			<tr>
				<td align="left"><html:messages id="message" message="true"
						property="confirmationMessage1">
						<bean:write name="message" />
					</html:messages> <html:messages id="message" message="true"
						property="confirmationMessage3">
						<bean:write name="message" />
					</html:messages></td>
			</tr>
			<tr>
				<td align="left"><br> <html:submit property="Save"
						styleClass="clsavebutton">
						<bean:message key="protocolSubmission.button.returnToProtocol" />
					</html:submit></td>
			</tr>
		</table>
	</html:form>
</body>
</html:html>


