<%-- 
    Document   : awardLayout
    Created on : Dec 22, 2010, 6:00:11 PM
    Author     : vineetha
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
    <link rel=stylesheet href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/ipf.css" type="text/css">
     <link href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css" rel="stylesheet" type="text/css">
    <title>Coeus Web Application</title>
    <script>
    function validateSavedInfo(){
        return true;
    }
    </script>
  </HEAD>

<body text="#000000" link="#023264" alink="#023264" vlink="#023264" style=" color:#D1E5FF">
    <table border="0" height="100%" cellspacing="0" cellpadding="0" align="center">
  <tr>
    <td  height='104' valign="top">
      <tiles:insert attribute="header" />
    </td>
  </tr>

 <tr style="height: 3px;"><td></td></tr> 
              <tr height="30px">
                  <td valign="top"  >

               <tiles:insert attribute='body1'></tiles:insert>
                  </td>
              </tr>
              <tr style="height: 3px;"><td></td></tr>
              <tr>
                  <td valign="top"  align="left">

               <tiles:insert attribute='body'></tiles:insert>
                  </td>
              </tr>
     

  <tr>
    <td valign="bottom"><tiles:insert attribute="footer" /></td>
  </tr>

</table>
</body>
</html>