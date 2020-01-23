<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>

<html:html>

<head>
<html:base />
<script language="javascript">
	
</script>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<title>Coeus Web</title>
</head>
<body>
	<table width="100%" height="100%" class='table' border="0"
		cellpadding="0">
		<tr>
			<h4 class='theader'>Welcome to Coeus Web</h4>
		</tr>
		<tr>
			<% String path = "/logonAction.do?url=/web/tiles/cwProtocolMainPageTile.jsp";%>
			<html:link page="<%=path%>"> Coeus Web </html:link>
	</table>
</body>
</html:html>