<%--
/*
 * @(#) PersonSearchResult.jsp 1.0 2002/06/05
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  Geo Thomas.
 */
--%>
<%-- Page for displaying person search results --%>
<%@page import="edu.mit.coeus.search.bean.DisplayBean,
                java.util.Vector,
                java.util.HashMap,
                edu.mit.coeus.utils.UtilFactory"
	errorPage="/CoeusSearchError.jsp" %>
<%@ taglib uri="/WEB-INF/struts-html.tld"     prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld"     prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld"     prefix="logic" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"     prefix="coeusUtils" %>
<jsp:useBean id="searchResult" scope='request' class="java.util.Hashtable"/>
<jsp:useBean id="searchinfoholder" scope='session'
                class="edu.mit.coeus.search.bean.SearchInfoHolderBean"/>
<jsp:useBean id ="fieldName" scope ='request' class = "java.lang.String" />
<jsp:useBean id ="reqType" scope ='request' class = "java.lang.String" />
<jsp:useBean id ="reqPage" scope ='request' class = "java.lang.String" />
<bean:define id='retrieveLimit' name='searchinfoholder' property='retrieveLimit'/>

<%@ include file="CoeusContextPath.jsp"  %>

<html:html>
<head><title><bean:write name='searchinfoholder' property='displayLabel'/> Result</title>
<style type="text/css">
div {font-family:verdana;font-size:10px}
a {font-family:verdana;font-size:10px;color:blue;text-decoration:none;}
a:active {color:red;text-decoration:none;}
a:visited {color:blue;text-decoration:none;}
a:hover {color:red;text-decoration:none;}
</style>
<script langauge="javascript">
function populateResultData(sltdPersonID,sltdPersonName){
    var requestedPage = "<%=reqPage%>";
    if(parseInt(<%=reqType%>)==0){
        window.opener.location.href="<%=request.getContextPath()%>/personDetails.do?personID="+sltdPersonID+"&reqPage="+requestedPage;
    }else{
        window.opener.document.forms[0].personId.value=sltdPersonID;
        window.opener.document.forms[0].personName.value=sltdPersonName;
    }
    window.close();
}
</script>
</head>
<body>
<FORM name='frmSearchResult'>
<table>
    <table width='100%'>
    <tr bgcolor="cc9999"><td>
       &nbsp;<font color="#FFFFFF" size="2"><b>
        <font face="Verdana, Arial, Helvetica, sans-serif">
            <bean:write name='searchinfoholder' property='displayLabel'/> Result
        </font></b></font>
   </td></tr>
   </table>
<%
    Vector displayList = (Vector)searchResult.get("displaylabels");
    Vector resList = (Vector)searchResult.get("reslist");
    String rowCount = (String)searchResult.get("rowcount");
    /* CASE #748 Begin */
    //Get the columnNames for the fields that will be displayed, in order to iterate through the resultSet.
    Vector colNames = new Vector();
    for(int i = 0; i<displayList.size(); i++){
    	DisplayBean dispBean = (DisplayBean)displayList.get(i);
    	colNames.add(dispBean.getName());
    }
    /* CASE #748 End */
    pageContext.setAttribute("rowCount",new Integer((rowCount!=null?rowCount:"0")));
    pageContext.setAttribute("displayList",displayList);
    pageContext.setAttribute("resList",resList);
    int index = 0;
    int colIndex =0;
    if(resList!=null && !resList.isEmpty()){
%>
   <table width='100%'>
    <tr  bgcolor="#CC9999">
    <logic:iterate id='label' name='displayList' scope='page'>
<%
    if(colIndex++!=0){
%>
        <td width="<bean:write name='label' property='size'/>"><font
            face="Verdana, Arial, Helvetica, sans-serif" size='1'><b>
        <bean:write name='label' property='value'/></b></font></td>
<%
    }
%>
    </logic:iterate>
    </tr>
<!-- CASE #748 Comment Begin -->
<%--
    <logic:iterate id='values' name='resList' type='java.util.Vector' scope='page'>
<%
    colIndex =0;
    String personId = "";
%>
    <tr bgColor="<%= ((++index)%2==0?"#F7EEEE":"#FFFFFF") %>">
        <logic:iterate id='value' name='values' type='java.lang.String'>
<%
    if(colIndex==0){
        personId = value;
    }else if (colIndex ==1) {
%>
                <td><div>
                <!-- CASE #448 Comment Begin -->
                <%--
                <a href ="JavaScript:populateResultData('<%=personId%>','<%=value%>');"><%=UtilFactory.dispEmptyStr(value)%></a></div></td>
                --%>
                <%--
                <!-- CASE #448 Begin -->
                <!-- escape any apostrophe in person name -->
                <a href ="JavaScript:populateResultData('<%=personId%>','<%=UtilFactory.escapeApostropheWithSlash(value)%>');"><%=UtilFactory.dispEmptyStr(value)%></a></div></td>
                <!-- CASE #448 End -->
<%
    } else {
%>
                <td><div><%=UtilFactory.dispEmptyStr(value)%></div></td>
<%
    }
    colIndex++;
%>
        </logic:iterate>
    </tr>
    </logic:iterate>
--%>
<!-- CASE #748 Comment End -->
<!-- CASE #748 Begin -->
<%
	for(int resRowCnt=0; resRowCnt<resList.size(); resRowCnt++){
		HashMap resultRow = (HashMap)resList.get(resRowCnt);
    		String personId = "";
%>
    		<tr bgColor="<%= ((++index)%2==0?"#F7EEEE":"#FFFFFF") %>">
<%
		for(colIndex=0; colIndex<colNames.size(); colIndex++){
			Object objValue = resultRow.get(colNames.get(colIndex));
			String value = objValue == null ? "&nbsp;" : objValue.toString();
			if (colIndex ==0) {
        			personId = value;
    			}else if (colIndex ==1) {
%>
				<td><div>
				<!-- CASE #448 Comment Begin -->
				<%--
				<a href ="JavaScript:populateResultData('<%=personId%>','<%=value%>');"><%=UtilFactory.dispEmptyStr(value)%></a></div></td>
				--%>
				<!-- CASE #448 Begin -->
				<!-- escape any apostrophe in person name -->
				<a href ="JavaScript:populateResultData('<%=personId%>','<%=UtilFactory.escapeApostropheWithSlash(value)%>');"><%=UtilFactory.dispEmptyStr(value)%></a></div></td>
				<!-- CASE #448 End -->
<%
    			} else {
%>
                		<td><div><%=UtilFactory.dispEmptyStr(value)%></div></td>
<%
    			}
    		}
%>

    </tr>
<%
	}
%>
<!-- CASE #748 End -->
</table>
<%
    }else{
%>
    <logic:greaterThan name='rowCount' value='0'>
        <div align='center'>The current selection criteria will retrieve <%=rowCount%> rows.
        <br>This is more than the limit of <%=retrieveLimit%> set in Resource File.
        <br>Please modify the selection criteria to retrieve fewer rows.<div>
    </logic:greaterThan>
    <logic:equal name='rowCount' value='0'>
        <div align='center'>No result found</div>
    </logic:equal>
<%
    }
%>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td colspan='5' >
              <div align="left">
                <a href="JavaScript:history.back();"><img src="<bean:write name='ctxtPath'/>/images/searchagain.gif" width="80" height="22" border="0"></a>&nbsp;
                <a href="JavaScript:window.close();"><img src="<bean:write name='ctxtPath'/>/images/close.gif" width="42" height="22" border="0"></a>
                &nbsp; &nbsp; &nbsp; </div>
            </td>
          </tr>
    </table>
</table>
<%-- <input type='button' name='btnSearchAgain' value='Search Again' onclick='javascript:history.back()'>
<input type='button' name='btnAnotherSearch' value='Another Search' onclick='changeAction(2)'/>
--%>
</FORM>
</body>
</html:html>
