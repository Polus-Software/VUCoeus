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
<title>Delete Protocol Page</title>
</head>
<body>
	<html:form action="/getIacucData.do?PAGE=G">
		<table width="100%" cellpadding="2" cellspacing="0" class="table"
			align="center">
			<tr>
				<td>&nbsp; <br>
				</td>
			</tr>
			<tr>
				<td align="left"><font color="red"> <html:messages
							id="message" message="true" property="protocolLocked">
							<li><bean:write name="message" /></li>
						</html:messages>
				</font></td>

			</tr>
			<tr>
				<td align="left"><br> <html:submit property="Save"
						styleClass="clsavebutton">
						<bean:message key="protocolDelete.button.returnToProtocol" />
					</html:submit></td>
			</tr>
		</table>
	</html:form>
</body>
</html:html>


