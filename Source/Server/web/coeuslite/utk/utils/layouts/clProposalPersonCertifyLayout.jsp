<%-- 
    Document   : clProposalPersonCertifyLayout
    Created on : Jan 17, 2011, 12:43:56 PM
    Author     : anishk
--%>

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
      <link rel=stylesheet href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css" type="text/css">
      <title>Coeus Web Application</title>
    <script>
        var DATA_CHANGED = '';
        var FORM_LINK;
        var LINK = '';
        var CLICKED_LINK = '';
        var PAGE_NAME = '';
        var ENABLE_COMPONENT = 'N';
        function validateSavedInfo() {
            if(ENABLE_COMPONENT == 'Y'){
                enableComponent();
            }
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

<body class="body">
<table width='72%' height="100%" cellspacing="0" cellpadding="0" border="0" align="center" class="tabtable">

    <tr>
        <td width='200'  valign="top" align=center height="65">
            <tiles:insert attribute="header" />
        </td>
    </tr>
     <tr  valign=top>
        <td width='820'  valign="top" align=center >
            <tiles:insert attribute="body" />
        </td>
    </tr>


</table>
</body>
</html>

