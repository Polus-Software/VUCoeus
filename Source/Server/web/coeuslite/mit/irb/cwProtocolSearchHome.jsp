<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<html:html>
<head>
<title>Coeus Web</title>
<html:base />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<link href="/coeuslite/mit/utils/css/ipf.css" rel="stylesheet"
	type="text/css">
<script language="javascript" type="text/JavaScript"
	src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/sorttable.js"></script>
</head>
<body>
	<html:messages id="message" message="true">
		<table width="970" height="100%" border="0" cellpadding="0"
			cellspacing="0" class="tabtable">
			<tr>
				<td width="50%"><img
					src="<bean:write name='ctxtPath'/>/coeusliteimages/coeus_splash.jpg"
					width="630" height="468"></td>
			</tr>
		</table>
	</html:messages>
</body>
</html:html>
