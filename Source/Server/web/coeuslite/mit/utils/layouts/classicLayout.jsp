<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>

<%  //Frorces caches not to store new copy of the page from the origin server
    response.setHeader("Cache-Control", "no-cache");
    //directs caches not to store the page under any circumstance
    response.setHeader("Cache-Control", "no-store");
    //causes the proxy cache to see the page as "stale"
    response.setDateHeader("Expires", 0);
    //http 1.0 backward compatibility
    response.setHeader("Pragma", "no-cache");%>

<%
    System.out.println("IN CLASSIC LAYOUT");
%>


<HTML>
<HEAD>

<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1252">
<link rel=stylesheet
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/ipf.css"
	type="text/css">
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<title>Coeus Web Application</title>

</HEAD>

<body text="#000000" link="#023264" alink="#023264" vlink="#023264"
	style="">
	<table width="72%" height="100%" cellspacing="0" cellpadding="0"
		border="0" align="center" class="tabtable">

		<tr>
			<td colspan="2" valign="top" height='104'><tiles:insert
					attribute="header" /></td>
		</tr>

		<tr>
			<td width="215" valign="top" align="center" height="100%">
				<table width="215" border='0' cellspacing="4" cellpadding="0">
					<tr>
						<td><tiles:insert attribute='menu' /></td>
					</tr>
				</table>
			</td>
			<td width="900" valign="top" align="center">
				<table width="100%" border='0' cellspacing="4" cellpadding="0">
					<tr valign="top">
						<td><tiles:insert attribute='body' /></td>
					</tr>
				</table>
			</td>
		</tr>

		<tr>
			<td colspan="2" valign="top"><tiles:insert attribute="footer" />
			</td>
		</tr>
	</table>
</body>
</html>

