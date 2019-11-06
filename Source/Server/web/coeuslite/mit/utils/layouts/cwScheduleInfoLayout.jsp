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

<HTML>
<HEAD>

<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1252">
<link rel=stylesheet
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	type="text/css">
<title>Coeus Web Application</title>
<script>
        function validate() {
            if( DATA_CHANGED == 'true' && confirm("<bean:message key="confirmation.save"/> "+PAGE_NAME+" ?")) {
                FORM_LINK.action = LINK;
                FORM_LINK.submit();
                return false;
            }
            return true;
        }
        var DATA_CHANGED = '';
        var LINK = '';
        var FORM_LINK;
        var PAGE_NAME = '';
    </script>
</HEAD>
<!-- JM 5-31-2011 updated per 4.4.2 -->
<body text="#000000" link="#3e3e3e" alink="#3e3e3e" vlink="#3e3e3e"
	style=''>
	<table width='72%' height="100%" cellspacing="0" cellpadding="0"
		border="0" align="center">

		<tr>
			<td colspan="3" valign="top" height='104'><tiles:insert
					attribute="header" /></td>
		</tr>

		<tr>

			<td width="170" valign="top"><tiles:insert attribute='menu' />

			</td>
			<td width='830' valign="top" align=center>
				<table width='100%' border='0' cellspacing="0" cellpadding="0">
					<tr>
						<td><tiles:insert attribute="subheader" /></td>
					</tr>
					<tr>
						<td><tiles:insert attribute="body" /></td>
					</tr>
				</table>
			</td>
		</tr>

		<tr>
			<td colspan="3" valign="bottom"><tiles:insert attribute="footer" />
			</td>
		</tr>
	</table>
</body>
</html>

