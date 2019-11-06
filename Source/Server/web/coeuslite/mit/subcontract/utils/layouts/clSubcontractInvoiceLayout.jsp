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
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/ipf.css"
	type="text/css">
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<title>Coeus Web Application</title>
<script>
        var DATA_CHANGED = '';
        var FORM_LINK;
        var LINK = '';
        var CLICKED_LINK = '';
        var PAGE_NAME = '';
        function validateSavedInfo() {
            if( DATA_CHANGED == 'true' && confirm("<bean:message bundle="proposal" key="confirmation.save"/> "+PAGE_NAME+" ?")) {
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
<!-- JM 5-31-2011 updated per 4.4.2 -->
<body text="#000000" link="#023264" alink="#023264" vlink="#023264"
	style="">
	<table height="100%" cellspacing="0" cellpadding="0" border="0"
		align="center">

		<tr>
			<td colspan="2" valign="top" height='104'><tiles:insert
					attribute="header" /></td>
		</tr>

		<tr>
			<td width="20%" valign="top"><tiles:insert attribute='menu' />
			</td>
			<td width='80%' valign="top" align="left" class="table"><tiles:insert
					attribute='body' /></td>
		</tr>

		<tr>
			<td width='973' colspan="2" valign="top"><tiles:insert
					attribute="footer" /></td>
		</tr>
	</table>
</body>
</html>

