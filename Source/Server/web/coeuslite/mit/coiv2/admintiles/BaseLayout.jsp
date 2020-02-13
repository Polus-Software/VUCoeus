<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>

<%@page
	import="java.util.HashMap,edu.mit.coeuslite.coiv2.utilities.CoiConstants"%>

<%
 boolean disable = true;
 /*HashMap thisUserRights = (HashMap) request.getAttribute("rights");
            Integer val = (Integer) thisUserRights.get(CoiConstants.DISCL);
            boolean disable = false;
            if (val == 2) {
                disable =true ;
            }*/

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
	href="<%=request.getContextPath() %>/coeuslite/mit/coiv2/css/layout.css"
	rel="stylesheet" type="text/css" />
<link
	href="<%=request.getContextPath() %>/coeuslite/mit/coiv2/css/styles.css"
	rel="stylesheet" type="text/css" />
<link
	href="<%=request.getContextPath()%>/coeuslite/dartmouth/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css" />
<link
	href="<%=request.getContextPath() %>/coeuslite/mit/coiv2/css/collapsemenu.css"
	rel="stylesheet" type="text/css" />
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


	<table width="72%" border="0" cellspacing="0" cellpadding="0"
		align="center">
		<tr>
			<td height='84' valign="top" class='copybold' colspan="2"><tiles:insert
					attribute="header" /></td>
		</tr>
		<tr>
			<td height='22' valign="top" class='copybold' colspan="2"><tiles:insert
					attribute="subheader" /></td>
		</tr>



		<tr style="background-color: #D1E5FF">

			<%--  <%if(disable){%>--%>
			<td width="200" valign="top" align="center">
				<table width="200" border='0' cellspacing="4" cellpadding="0">
					<tr>
						<td><tiles:insert attribute='menu' /></td>
					</tr>
				</table>
			</td>

			<td width="750" valign="top" align="center">
				<!-- JM 11-1-2012 width was 780 -->
				<table width='750' border='0' cellspacing="0" cellpadding="0"
					align="left">
					<!-- JM 11-1-2012 width was 780 -->
					<tr valign="top">
						<td><tiles:insert attribute='body1'></tiles:insert></td>
					</tr>
					<tr valign="top">
						<td><tiles:insert attribute='body' /></td>
					</tr>
				</table>
			</td>

			<%-- changes adding Jaisha  --%>

		</tr>

		<tr>
			<td colspan="2" valign="bottom"><tiles:insert attribute="footer" />
			</td>
		</tr>
	</table>


</body>
</html>


