<%--
/*
 * @(#) Question.jsp 1.0 2002/06/05
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  Geo Thomas.
 */
--%>

<%-- This JSP file is for listing all the details pertaining to a particular question --%>
<%@page errorPage="ErrorPage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ include file="CoeusContextPath.jsp"  %>
<jsp:useBean id ="questionDetails" scope = "request" class ="edu.mit.coeus.coi.bean.QuestionDetailsBean" />

<html:html locale="true">
<head>
<title> <bean:message key="question.title" /> </title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<style type="text/css">
div {font-family:verdana;font-size:12px}
</style>
</head>

<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" border="0" cellspacing="0" cellpadding="5">
  <tr bgcolor="#CC9999">
    <td colspan="5" height="20">
      <div align="left"><b><font color="#FFFFFF" size="2">&nbsp;<bean:message key ="question.header" /></font></b></div>
    </td>
  </tr>


  <tr>
    <td colspan="4" height="30" bgcolor="#FBF7F7" valign="top">
         <div align="left"> <bean:write name="questionDetails" property="questionID" /> : </div>
    </td>
    <td height="30" bgcolor="#FBF7F7">
    	<div align="left">
	<bean:write name="questionDetails" property="description" /> </div>
    </td>
  </tr>
  <tr>
    <td colspan="5" height="10">
      <div align="right"></div>
    </td>
  </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="5">
  <tr bgcolor="#CC9999">
    <td colspan="5" height="20">
      <div align="left">
        <font color="#FFFFFF"><b>
            <font color="#FFFFFF" size="2">&nbsp;</font>
            <font size="2"> <bean:message key ="question.explanationTitle" /> </font>
         </b></font>
      </div>
    </td>
  </tr>
  <tr bgcolor="#FBF7F7">
    <td colspan="5" height="30">
    	<div align="left">
	<!-- CASE #315 Comment Begin -->
	<%--<bean:write name ="questionDetails" property = "explanation" /> --%>
	<!-- CASE #315 Comment End -->
        <!-- CASE #315 Begin -->
        <!-- Replace CR/LF stored in database as ascii 13 with <br> tag -->
	<%
	String explanation = questionDetails.getExplanation();
	if(explanation == null || explanation.equals("")){
		out.print("&nbsp;");
	}
	else{
		//Display paragraph formatting.
		for(int i=0; i<explanation.length(); i++){
			int intValue = explanation.charAt(i);
			if(intValue == 13){
				out.print("<br>");
			}
			else{
				out.print(String.valueOf((char)intValue));
			}
		}
	}
	%>
	<!-- CASE #315 End -->
	</div>
    </td>
  </tr>
  <tr>
    <td colspan="5" height="10">
      <div align="right"></div>
    </td>
  </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="5">
  <tr bgcolor="#CC9999">
    <td colspan="5" height="20">
      <div align="left"><b><font color="#FFFFFF" size="2">&nbsp;<bean:message key ="question.policy" /></font></b></div>
    </td>
  </tr>
  <tr bgcolor="#FBF7F7">
    <td colspan="5" height="30">
     <div align="left"> <bean:write name ="questionDetails" property = "policy"/> </div>
    </td>
  </tr>
  <tr>
    <td colspan="5" height="10">
      <div align="right"></div>
    </td>
  </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="5">
  <tr bgcolor="#CC9999">
    <td colspan="5" height="20">
      <div align="left"><b><font color="#FFFFFF" size="2">&nbsp;<bean:message key ="question.regulation" /></font></b></div>
    </td>
  </tr>
  <tr bgcolor="#FBF7F7">
    <td colspan="5" height="30">
              <div align="left"> <bean:write name ="questionDetails" property = "regulation" /> </div>
    </td>
  </tr>
  <tr>
    <td colspan="5" height="10">
      <div align="right"></div>
    </td>
  </tr>

  <tr>
    <td width="83" height="40">&nbsp;</td>
    <td width="198" height="40">&nbsp;</td>
    <td width="22" height="40">&nbsp;</td>
    <td width="100" height="40">&nbsp;</td>
    <td width="209" height="40">
      <div align="right">&nbsp;<a href="JavaScript:window.close();"><img src="<bean:write name='ctxtPath'/>/images/close.gif" width="42" height="22" border="0"></a>
        &nbsp; &nbsp; &nbsp; </div>
    </td>
  </tr>
</table>

</body>
</html:html>
