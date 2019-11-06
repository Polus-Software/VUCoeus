<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>

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
 
      <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
      <!--Text boxes referred to this ipf.css file, so it is added. -->
      <link rel=stylesheet href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/ipf.css" type="text/css">
      <link rel=stylesheet href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css" type="text/css">
      <title>Coeus Web Application</title>
    <script>
        var DATA_CHANGED = '';
        var LINK = '';
        var FORM_LINK;
        var PAGE_NAME = '';
        var CLICKED_LINK = '';
        function validate() {
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

<body class="body" >
<!-- JM 7-25-2012 added table width to match header -->
<table width="970" height="100%" cellspacing="0" cellpadding="0" border="0" align="center" class="tabtable">
  
    <tr>
    <td colspan="2" valign="top" height='84'>
        <tiles:insert attribute="header" />
    </td>
    </tr>
  
    <tr>
        <td width="20%" valign="top" >
            <table width="100%" border='0' cellspacing="2" cellpadding="0"><tr><td>
                <tiles:insert attribute='menu'/>
            </td></tr></table>
        </td> 
        <td width="80%" valign="top" align="left" >
            <table width='100%' border="0" cellspacing="4" cellpadding="0">
                <tr valign="top">
                    <td>
                        <tiles:insert attribute='bodyHeader' />                        
                    </td>
                </tr>
              <tr valign="top">
                    <td>
                        <tiles:insert attribute='body' />
                    </td>
                </tr> 
            </table>  
        </td>
    </tr>

    <tr>
    <td colspan="2" valign="bottom">
        <tiles:insert attribute="footer" />
    </td>
    </tr>
</table>
</body>
</html>

