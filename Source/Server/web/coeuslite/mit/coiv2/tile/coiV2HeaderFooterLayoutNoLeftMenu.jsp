<%-- 
    Document   : coiV2HeaderFooterLayout
    Created on : Apr 7, 2010, 4:05:33 PM
    Author     : Sony
--%>


<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@page
	import="java.util.HashMap,edu.mit.coeuslite.coiv2.utilities.CoiConstants"%>



<%

            boolean disable = false;
            

%>



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
    function validate(){
        return true;
    }
    </script>
</HEAD>

<body text="#000000" link="#023264" alink="#023264" vlink="#023264">
	<table border="0" height="100%" cellspacing="0" cellpadding="0"
		align="center">
		<tr>
			<td height='84' valign="top"><tiles:insert attribute="header" />
			</td>
		</tr>


		<tr>
			<td height='22' valign="top"><tiles:insert attribute="subheader" />
			</td>
		</tr>


		<tr>
			<td valign="top" height="100%" class="table"><tiles:insert
					attribute='body' /></td>
		</tr>

		<tr>
			<td valign="bottom"><tiles:insert attribute="footer" /></td>
		</tr>

	</table>
</body>
</html>
