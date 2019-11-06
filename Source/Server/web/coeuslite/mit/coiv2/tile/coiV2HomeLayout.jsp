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
          var DATA_CHANGED = '';
        var LINK = '';
        var FORM_LINK;
        var PAGE_NAME = '';
        var CLICKED_LINK = '';
        var CHECK_ATTACH = '';
        function validateSavedInfo() {           
            if( DATA_CHANGED == 'true' && confirm("<bean:message key="confirmation.save"/> "+PAGE_NAME+" ?")) { 
              while(CLICKED_LINK.indexOf('&')!=-1){
                    CLICKED_LINK = CLICKED_LINK.replace("&","$");
                }
                if(LINK.indexOf('?')!=-1) {
                    FORM_LINK.action = LINK+"&CLICKED_LINK="+CLICKED_LINK;
                } else {
                    FORM_LINK.action = LINK+"?CLICKED_LINK="+CLICKED_LINK;
                }
                FORM_LINK.submit();
                return false;
            }
            return true;
        }
        function linkForward(errValue) {            
              CLICKED_LINK = '<%=request.getParameter("CLICKED_LINK")%>';
              if(CLICKED_LINK == 'null' || CLICKED_LINK == null){
                CLICKED_LINK = '<%=request.getAttribute("CLICKED_LINK")%>';
              }              
              if(!errValue && CLICKED_LINK!='null' && CLICKED_LINK.length>0) {
                while(CLICKED_LINK.indexOf('$')!=-1){
                    CLICKED_LINK = CLICKED_LINK.replace("$","&");
                }
                window.location= CLICKED_LINK;
                CLICKED_LINK ='';
              }
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
			<td width="200" valign="top" align="center">
				<table width="200" border='0' cellspacing="4" cellpadding="0">
					<tr>
						<td><tiles:insert attribute='menu' /></td>
					</tr>
				</table>

			</td>


			<%-- changes adding Jaisha  --%>
			<td width="780" valign="top" align="center">
				<table width="100%" cellpadding="0" cellspacing="4" align="left">
					<tr valign="top">
						<td><tiles:insert attribute='body1'></tiles:insert></td>
					</tr>
					<tr>
						<td valign="top"><tiles:insert attribute='body'></tiles:insert>
						</td>
					</tr>
				</table>

			</td>

			<%-- changes adding Jaisha  --%>



		</tr>

		<tr>
			<%--  <td  height='84' valign="top" class='copybold' colspan="2">--%>
			<td colspan="2" valign="bottom"><tiles:insert attribute="footer" />
			</td>
		</tr>


	</table>

</body>
</html>
