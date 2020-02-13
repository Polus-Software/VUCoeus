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
      <link href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/ipf.css" rel="stylesheet" type="text/css">
      <link href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css" rel="stylesheet" type="text/css">      
      <title>Coeus Web Application</title>
    <script>
        function validate() {
            if( DATA_CHANGED == 'true' && confirm("<bean:message key="confirmation.save"/> "+PAGE_NAME+" ?")) {
                FORM_LINK.action = LINK;
                FORM_LINK.submit();
                return false;
            }
            return;
        }
        var DATA_CHANGED = '';
        var LINK = '';
        var FORM_LINK;
        var PAGE_NAME = '';
    </script>			
  </HEAD>

<body text="#000000" link="#023264" alink="#023264" vlink="#023264" >
<table height="100%" cellspacing="0" cellpadding="0" border="0" align="center" >
  
    <tr>
    <td width='1018' valign="top" height='80'>
        <tiles:insert attribute="header" />
    </td>
    </tr>
  
    <tr>
        
        <td width='1018' valign="top" align="left" >
            <table width='100%'>
                <tr>
                    <td>
                        <tiles:insert attribute='bodyHeader' />                        
                    </td>
                </tr>
              <tr>
                    <td>
                        <tiles:insert attribute='body' />
                    </td>
                </tr> 
            </table>  
        </td>
    </tr>

    <tr>
    <td valign="top">
        <tiles:insert attribute="footer" />
    </td>
    </tr>
</table>
</body>
</html>


