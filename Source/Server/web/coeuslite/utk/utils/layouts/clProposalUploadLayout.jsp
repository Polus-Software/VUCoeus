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
      <link rel=stylesheet href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/ipf.css" type="text/css">  
      <title>Coeus Web Application</title>
		
  </HEAD>

<body text="#000000" link="#023264" alink="#023264" vlink="#023264" >
<table height="100%" cellspacing="0" cellpadding="0" border="0" align="center" >
  
    <tr>
    <td colspan="2" valign="top" height='104' align="left">
        <tiles:insert attribute="header" />
    </td>
    </tr>
  
    <tr>
        <td width="200" valign="top" >
            <tiles:insert attribute='menu'/>
        </td> 
        <td width='770' valign="top" align="left" >
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
    <td colspan="2" valign="top">
        <tiles:insert attribute="footer" />
    </td>
    </tr>
</table>
</body>
</html>

