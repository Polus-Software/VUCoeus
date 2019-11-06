<%@ taglib uri="/WEB-INF/struts-html.tld"     prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld"     prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld"     prefix="logic" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"     prefix="coeusUtils" %>
<% String desc = request.getParameter("desc");
%>

<html>
<head><title>Coeus Description</title>
<style type="text/css">
div {font-family:verdana;font-size:10px}
a {font-family:verdana;font-size:10px;color:blue;text-decoration:none;}
a:active {color:red;text-decoration:none;}
a:visited {color:blue;text-decoration:none;}
a:hover {color:red;text-decoration:none;}
</style>
<script langauge="javascript">
function populateDescription(){
        //	window.opener.document.forms[0].hdnDescription.value = "Hi";
        window.close();
}
</script>
</head>
<body>
<form name='frmCoeusDescription' >
<table width="100%" border="0" cellspacing="1" cellpadding="0" align="right" >

 <tr>
      <td  height="20%">
        <a href="JavaScript:window.populateDescription();"><img src="<bean:write name='ctxtPath'/>/images/close.gif" width="42" height="22" border="0"></a>
          </div>
      </td>
    </tr>

     <tr bgcolor="#CC9999">
      <td height="25%">
            <div align="center"><font color="#FFFFFF"> <bean:message key="viewCOIDisclosureDetails.label.explanation"/> </font></div>
       </td>
    </tr>
   </tr>
    <tr >
    <td height = "60%"  bgcolor="#FBF7F7" >
       &nbsp;<%--
       <textarea name="desc"
                    wrap='virtual' cols="40" rows="2" width = "500" ><%= desc %></textarea>
       <font color="#FFFFFF" size="2"><b> --%>
        <font face="Verdana, Arial, Helvetica, sans-serif" size ="2" >
            <%= desc %>
        </font> <%-- </font> --%>
    <td>
  </tr>
 <tr>
 <td height = "5">
 </td>
 </tr>
 <tr>

   <%--       <td width="0%">&nbsp;</td>
    <td width="0%" height="40">&nbsp;</td>
    <td width="0%" height="40">&nbsp;</td>
      <td width="0%" height="40">&nbsp;</td>
      <td width="0%" height="40">&nbsp;</td>
      <td width="0%" height="40">&nbsp;</td>--%>
      <td  height="20%">
        <a href="JavaScript:window.populateDescription();"><img src="<bean:write name='ctxtPath'/>/images/close.gif" width="42" height="22" border="0"></a>
          </div>
      </td>
    </tr>

</table>
</form>
</body>
</html>
