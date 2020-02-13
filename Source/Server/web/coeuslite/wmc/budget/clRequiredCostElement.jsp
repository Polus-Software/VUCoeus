<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>

<html:html>

<head>
<html:base/>
<script language="javascript">
	
</script>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

<title>
   No Cost Element is Defined
</title>

<!--<link href="css/ipf.css" rel="stylesheet" type="text/css">
        <style type="text/css">
        </style>-->


<SCRIPT language="JavaScript">
    function noCostElement(){
       var msg = "<bean:message bundle="budget" key="budgetLabel.NoCostElement"/>";
       alert(msg);
 
    }
</SCRIPT>
<html:base/>
</head>

<body onLoad='noCostElement()'>


<table width="100%" height="100%"  border="0" cellpadding="0">
	<tr>
	  <td align="center" valign="top" class="copy" colspan="2"><p><br>
      <br>
      <img src="<bean:write name='ctxtPath'/>/coeusliteimages/header_coeus2.gif" width="675" height="50"><br>

      <span class="copybold">            </span></p>
	      
      <table width="50%"  border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td class="tablemain" align="left">  
            <b><bean:message bundle="budget" key="requiredCostElement.costElementLabel"/><%--No cost element is Defined--%>.</b>
          </td>
        </tr>
        <tr>
          <td>
            <br>
            <br>
            <!--Display Error Message - START -->
            <bean:message bundle="budget" key="budgetLabel.NoCostElement"/><%--Cost Elements are not defined for this budget category.<br>
            Please define cost elements using Cost Elements Maintenance.--%>
            <!--Display Error Message - END -->
          </td>
        </tr>
      </table>		  
              
      <p><span class="copybold">
      <br>            

    <br>
    <%--If you do not have an Onyen account, <a href="http://onyen.unc.edu" target="_blank">click here</a> to obtain one using your UNC PID </p>--%></td>
	</tr>
</table>

</body>
</html:html>