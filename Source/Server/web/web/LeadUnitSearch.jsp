<!--
/*
 * @(#)LeadUnitSearch.jsp	1.0	2002/06/11	23:12:30
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
		edu.mit.coeus.utils.ComboBoxBean"
	errorPage="CoeusSearchError.jsp" %>

<%@ taglib uri="/WEB-INF/struts-html.tld"     prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld"     prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld"     prefix="logic" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"     prefix="coeusUtils" %>

<jsp:useBean id="searchinfoholder" scope='session'
                class="edu.mit.coeus.search.bean.SearchInfoHolderBean"/>
<bean:define id="criteriaList" name="searchinfoholder" property="criteriaList"/>
<jsp:useBean id ="fldName" scope ='request' class = "java.lang.String" />
<jsp:useBean id ="searchName" scope ='request' class = "java.lang.String" />

<%@ include file="CoeusContextPath.jsp"  %>

<% System.out.println("--------> LeadUnitSearch <-----------"); %>

<html:html>
<head><title><bean:write name='searchinfoholder' property='displayLabel'/></title>
<style type="text/css">
div {font-family:verdana;font-size:12px}
div.help {font-family:verdana;font-size:10px}
a {font-family:verdana;font-size:10px;color:blue;text-decoration:none;}
a:active {color:red;text-decoration:none;}
a:visited {color:blue;text-decoration:none;}
a:hover {color:red;text-decoration:none;}
</style>
</head>
<script langauge="javascript">
function search(){
    var objElements = document.frmLeadUnitSearch.elements;
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
        document.frmLeadUnitSearch.submit();
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
    var objElements = document.frmLeadUnitSearch.elements;
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
<form name='frmLeadUnitSearch' action='coeusSearchResult.do' method='POST'>
<input type="hidden" name="fieldName" value='<%=fldName%>'>
<input type="hidden" name="searchName" value='<%=searchName%>'>
<table width='100%'>
<!-- CASE #275 Begin --><!-- CASE #1374 Remove help text.  * wildcard is now available.-->
<!--
<div class="help">
<ul>
	<li>
	To search for a unit whose name <b>contains bio</b>, type <b>like %bio%</b> in the Unit Name field.
	</li>
	<li>
	To search for a unit whose name <b>begins with bio</b>, type <b>like bio%</b> in the Unit Name field.
	</li>
	<li>
	To search for a unit whose name <b>ends with ology</b>, type <b>like %ology</b> in the Unit Name field.
	</li>
</ul>
</div>
-->
<!-- CASE #275 End -->
<!-- CASE #1374 Begin -->
<div class="mediumsizebrown">
Use * for wildcard searches.
</div>
<!-- CASE #1374 End -->
    <table width='100%'>
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

