<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@page errorPage="ErrorPage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<html:html>
<head>
<title>Coeus Web</title>
<html:base />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
</head>
<%String messageType = request.getParameter("messageType"); 
    messageType = messageType != null ? messageType :"" ;
    %>
<body>
	<table width="980" height="100%" border="0" cellpadding="0"
		cellspacing="0" class="tabtable">
		<tr>
			<td align="center" valign="middle" class="copy"><bean:message
					bundle="coi" key="label.coeusDelegationErrorMsg" /></td>
		</tr>
		<tr>
			<td align="center" valign="middle">
				<%String link ="/getInboxMessages.do?messageType="+messageType+"&Menu_Id=005";%>
				<u><html:link action='<%=link%>'>
						<bean:message bundle="coi" key="label.back" />
					</html:link></u>
			</td>
		</tr>
	</table>
</body>
</html:html>