<!--
/*
 * @(#)ProposalSearchCoeusWeb.jsp	1.0	2002/06/11	23:12:30
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 *
 * @author  coeus-dev-team
 */
-->

<%@page import="edu.mit.coeus.search.bean.FieldBean,
		edu.mit.coeus.utils.ComboBoxBean,
		edu.mit.coeus.utils.CoeusConstants"
	errorPage="CoeusSearchError.jsp" %>

<%@ taglib uri="/WEB-INF/struts-html.tld"     prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld"     prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld"     prefix="logic" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"     prefix="coeusUtils" %>
<%@ taglib uri='/WEB-INF/privilege.tld' prefix='coeusPrivilege' %>
<%@ taglib uri='/WEB-INF/request.tld' prefix='req' %>

<jsp:useBean id="searchinfoholder" scope='session'
                class="edu.mit.coeus.search.bean.SearchInfoHolderBean"/>
<bean:define id="criteriaList" name="searchinfoholder" property="criteriaList"/>
<jsp:useBean id ="fldName" scope ='request' class = "java.lang.String" />
<jsp:useBean id ="searchName" scope ='request' class = "java.lang.String" />

<req:setAttribute name="usePropDevErrorPage">true</req:setAttribute>

<coeusPrivilege:checkLogin  
	requestedURL="coeusSearch.do?searchname=proposalDevSearchNoRoles&fieldName=proposalNum"
	forwardName="<%=CoeusConstants.LOGIN_KEY%>" />

<%@ include file="CoeusContextPath.jsp"  %>

<% System.out.println("--------> ProposalSearchCoeusWeb <---"); %>

<html:html>
<head><title><bean:write name='searchinfoholder' property='displayLabel'/></title>
<style type="text/css">
	div {font-family:verdana;font-size:12px}
	a {color:blue;}
	a:active {color:red;}
	a:visited {color:blue;}
	a:hover {color:red;}
	h1 {font-size: 16px}
	h2 {font-size: 14px}
	h3 {font-size: 12px}
	body, td, th, p, a, h4, h3, h2, h1 {font-family:verdana, helvetica, sans-serif}
	body, td, h4 {font-family:verdana, helvetica, sans-serif; font-size:12px}
	.fontBrown {color:#7F1B00}
	.smallbrown {font-family:verdana, helvetica, sans-serif; font-size:10px; color:#7F1B00}
	.smallsize {font-family:verdana, helvetica, sans-serif; font-size:10px}
	.smallred {font-family:verdana, helvetica, sans-serif; font-size:10px; color:red}
	.header {font-family:verdana, helvetica, sans-serif; font-size:13px; font-weight:bold}
</style>
</head>
<script langauge="javascript">
function search(){
    var objElements = document.frmProposalSearchCoeusWeb.elements;
    var flag = false;
    for(index = 0;index<objElements.length;index++){
        var obj = objElements[index];
        var objType = obj.type;
        if((objType=="text" && (isNotEmpty(obj.value))) ||
            ((objType.indexOf("select")!=-1) && (isNotEmpty(obj.options[obj.selectedIndex].value)))){
            flag = true;
            break;
        }
    }
    if(flag){
        document.frmProposalSearchCoeusWeb.submit();
    }else{
        alert("Please enter necessary search criteria");
    }
}
// for checking the empty/blank spaces
function isNotEmpty(tmpVal){
    var tmp;
    var tmpLen = tmpVal.length;
    var tmpCount = 0;

    for(tmp=0;tmp<tmpLen;tmp++){
	if(tmpVal.charAt(tmp) != " "){
	    tmpCount = tmpCount + 1;
        }
    }
    return (tmpCount!=0);
}
function clearAll(){
    var objElements = document.frmProposalSearchCoeusWeb.elements;
    for(index = 0;index<objElements.length;index++){
        var obj = objElements[index];
        var objType = obj.type;
        if(objType=="text"){
            obj.value="";
        }else if(objType.indexOf("select")!=-1){
            obj.selectedIndex=0;
        }
    }
}
</script>
<body>
<form name='frmProposalSearchCoeusWeb' action='coeusSearchResult.do' method='POST'>
<input type="hidden" name="fieldName" value='<%=fldName%>'>
<input type="hidden" name="searchName" value='<%=searchName%>'>
<table width='100%'>
<!-- CASE #275 Begin --><!-- CASE #1374 like % help no longer needed. -->
<!--
<div class="help">
<ul>
	<li>
	To search for a proposal whose title <b>contains cell</b>, type <b>like %cell%</b> in the Title field.
	</li>
	<li>
	To search for a proposal whose title <b>begins with cell</b>, type <b>like cell%</b> in the Title field.
	</li>
	<li>
	To search for a proposal whose title <b>ends with cell</b>, type <b>like %cell</b> in the Title field.
	</li>
</ul>
</div>
-->
<!-- CASE #275 End -->
    <table width='100%'>
    <!-- CASE #1374 Begin -->
    <tr>
        <td height="20" valign="top">
        &nbsp;Use * for wildcard searches.
        <td>
    </tr>
    <!-- CASE #1374 End -->    
    <tr bgcolor="cc9999"><td>
       &nbsp;<font color="#FFFFFF" size="2"><b>
        <font face="Verdana, Arial, Helvetica, sans-serif">
            <bean:write name='searchinfoholder' property='displayLabel'/>
        </font></b></font></td>
  </tr>
  </table>
  <table>
<tr bgcolor="#CC9999" bgcolor="#FBF7F7">
<logic:iterate  id='criteria' name='criteriaList'>
    <bean:define id='field' name='criteria' property='fieldBean'/>
    <td><div><b><bean:write name='field' property="label"/></b></div></td>
</logic:iterate>
</tr>
<%
    request.setAttribute("fieldName",fldName);
    for(int i=0;i<10;i++){
        String bgColor = ((i%2)==0)?"#F7EEEE":"#FBF7F7";
%>
<tr bgcolor="<%=bgColor%>">
<logic:iterate  id='criteria' name='criteriaList'>
    <bean:define id='field' name='criteria' property='fieldBean'/>
    <bean:define id='fieldName' name='field' property='name' type='java.lang.String'/>
    <div><td>
        <logic:equal name='field' property='type' value="list">
            <bean:define id='fields' name='field' property='comboList' type='java.util.Vector'/>
            <select name='<%=fieldName%>'>
<%
        int lstSize = fields.size();
        for(int fieldIndex=0;fieldIndex<lstSize;fieldIndex++){
            ComboBoxBean combBean = (ComboBoxBean)fields.elementAt(fieldIndex);
%>
                <option value="<%= combBean.getCode() %>"><%= combBean.getDescription() %></option>
<%
        }
%>
<%--                <html:options collection='fields' property='code' labelProperty='description'/>--%>
            </select>
        </logic:equal>
       <logic:notEqual name='field' property='type' value='list'>
       		<!-- CASE #748 Comment Begin -->
       		<%--
		<input size="<bean:write name='criteria' property='size'/>"
			type='text' name='<%=fieldName%>'/>
            	--%>
            	<!-- CASE #748 Comment End -->
            	<!-- CASE #748 Begin -->
            	<input size="<bean:write name='criteria' property='webSize'/>"
                	type='text' name='<%=fieldName%>'/>
                <!-- CASE #748 End -->
        </logic:notEqual>
    </td></div>
</logic:iterate>
</tr>
<%
    }
%>
 </table>
 <table>
 <tr>
      <td width="30%" height="40">
        <div align="left">
        <a href="javascript:search();"><img src="<bean:write name='ctxtPath'/>/images/find.gif" width="42" height="22" border="0"></a>&nbsp;
        <a href="JavaScript:clearAll();"><img src="<bean:write name='ctxtPath'/>/images/reset.gif" width="42" height="22" border="0"></a>&nbsp;
        <a href="JavaScript:window.close();"><img src="<bean:write name='ctxtPath'/>/images/close.gif" width="42" height="22" border="0"></a>
          &nbsp; &nbsp; &nbsp; </div>
      </td>
    </tr>
 </table>
</table>
</form>
</body>
</html:html>

