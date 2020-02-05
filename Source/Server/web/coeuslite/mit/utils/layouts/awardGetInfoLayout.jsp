<%-- 
    Document   : awardGetInfoLayout
    Created on : Dec 29, 2010, 2:11:00 PM
    Author     : vineetha
--%>
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

<%-- Layout Tiles
  This layout render a header, body and footer.
  @param title String use in page title
  @param header Header tile (jsp url or definition name)
   @param body Body
  @param footer Footer
--%>
<%--
System.out.println("************************************** ");
System.out.println("**** In cwInformationLayout.jsp ****** ");
System.out.println("************************************** ");
--%>
<HTML>
<HEAD>
<link rel=stylesheet
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/ipf.css"
	type="text/css">
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<title>Coeus Web Application</title>
<script>
    function validateSavedInfo(){
        return true;
    }
    </script>
</HEAD>

<body class="body">
	<table height="100%" width="72%" cellspacing="0" cellpadding="0"
		border="0" align="center" class="tabtable">

		<tr>
			<td colspan="2" width="100%" valign="top" height='84'><tiles:insert
					attribute="header" /></td>
		</tr>

		<tr>
			<td width="180" height="100%" valign="top">
				<table width="180" height="100%" border='0' cellspacing="2"
					cellpadding="0">
					<tr>
						<td><tiles:insert attribute='menu' /></td>
					</tr>
				</table>
			</td>
			<td width="840" valign="top" align="left">
				<table width='100%' border="0" cellspacing="4" cellpadding="0">
					<tr>
						<%--
                    <td height='100%'>
                        <tiles:insert attribute="bodyHeader"/>
                    </td>
               --%>
					</tr>
					<tr valign="top">
						<td><tiles:insert attribute="body" /></td>
					</tr>
				</table>
			</td>
		</tr>
<table width="90%" cellspacing="0" cellpadding="0" border="0" align="center" class="tabtable">
		<tr>
			<td colspan="2" valign="top"><tiles:insert attribute="footer" />
			</td>
		</tr>
</table>
	</table>
</body>
</html>


