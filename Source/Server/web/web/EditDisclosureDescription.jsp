<!--
/*
 * @(#)EditDisclosureDescription.jsp	1.0	2002/06/11	23:12:30
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 *
 * @author  coeus-dev-team
 */
-->

<%@ taglib uri="/WEB-INF/struts-html.tld"     prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld"     prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld"     prefix="logic" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"     prefix="coeusUtils" %>
<%
    String desc = request.getParameter("desc");
    String index = request.getParameter("index");
%>
<%@ include file="CoeusContextPath.jsp"  %>
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
        if(document.frmCoeusDescription.txtDesc.value.length > 1000) {
          alert("Explanation length is limited to 1000 characters");
	    document.frmCoeusDescription.txtDesc.value = document.frmCoeusDescription.txtDesc.value.substring(0,1000);
          document.frmCoeusDescription.txtDesc.focus();
	  }else {
          window.opener.document.forms[0].description<%=index%>.value = document.frmCoeusDescription.txtDesc.value;
          window.close();
	  }
}

window.onload=function(){
   //alert(window.opener.document.forms[0].description<%=index%>.value);
   document.frmCoeusDescription.txtDesc.value = window.opener.document.forms[0].description<%=index%>.value;
   document.frmCoeusDescription.txtDesc.focus();
}
</script>
</head>
<body>
<form name='frmCoeusDescription' >
<table width="100%" border="0" cellspacing="1" cellpadding="0" align="right" >
   <tr>
      <td  height="20%">
        <a href="JavaScript:window.populateDescription();"><img src="<bean:write name='ctxtPath'/>/images/close.gif" width="42" height="22" border="0"></a>
      </td>
   </tr>
   <tr>
   	<td>
   		<div><font color="#7F1B00"><bean:message key="editDisclosureDescription.textLimit" /></div>
   	</td>
   </tr>
    <tr bgcolor="#CC9999">
      <td height="25%">
            <div align="center"><font color="#FFFFFF"> <bean:message key="viewCOIDisclosureDetails.label.explanation"/> </font></div>
       </td>
    </tr>
    <tr >
     <td height = "60%"  bgcolor="#FBF7F7" >
       <textarea name="txtDesc" wrap='virtual' cols="55" rows="20" ></textarea>
       <%-- <textarea name="txtDesc" wrap='virtual' cols="55" rows="20" ><%= desc %></textarea> --%>
     </td>
   </tr>
 <tr>
  <td height = "5">
  </td>
 </tr>
 <tr>
      <td  height="20%">
        <a href="JavaScript:window.populateDescription();"><img src="<bean:write name='ctxtPath'/>/images/close.gif" width="42" height="22" border="0"></a>

      </td>
    </tr>

</table>
</form>
</body>
</html>
