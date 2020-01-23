<!--
/*
 * @(#)ProposalSearchResultCoeusWeb.jsp	1.0	2002/06/11	23:12:30
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 *
 * @author  coeus-dev-team
 */
-->

<%@page import="edu.mit.coeus.search.bean.DisplayBean,
                java.util.Vector,
                java.util.HashMap,
                edu.mit.coeus.utils.UtilFactory"
	errorPage="CoeusSearchError.jsp" %>

<%@ taglib uri="/WEB-INF/struts-html.tld"     prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld"     prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld"     prefix="logic" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"     prefix="coeusUtils" %>
<jsp:useBean id="searchResult" scope='request' class="java.util.Hashtable"/>
<jsp:useBean id="searchinfoholder" scope='session'
                class="edu.mit.coeus.search.bean.SearchInfoHolderBean"/>
<jsp:useBean id ="fieldName" scope ='request' class = "java.lang.String" />
<bean:define id='retrieveLimit' name='searchinfoholder' property='retrieveLimit'/>

<%@ include file="CoeusContextPath.jsp"  %>

<% System.out.println("--------> ProposalSearchResultCoeusWeb <-----------"); %>


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
function populateResultData(ProposalNum,rowId){
        window.close();
        //window.opener.location.replace("displayProposal.do?proposalNumber=00000502");
        window.opener.location.href = "displayProposal.do?proposalNumber=" + ProposalNum;
}
</script>
</head>
<body>
<FORM name='frmProposalSearchResultCoeusWeb'>
<%
    Vector displayList = (Vector)searchResult.get("displaylabels");
    Vector resList = (Vector)searchResult.get("reslist");
    Vector resListVisibleOnly = new Vector();
    Vector displayListVisibleOnly = new Vector();
    String rowCount = (String)searchResult.get("rowcount");
    //Get the columnNames for the fields that will be displayed, in order to iterate through the resultSet.
    Vector colNames = new Vector();
    for(int i = 0; i<displayList.size(); i++){
    	DisplayBean dispBean = (DisplayBean)displayList.get(i);
    	if(dispBean.isVisible()){
		colNames.add(dispBean.getName());
		displayListVisibleOnly.add(displayList.get(i));
		//System.out.println("add "+dispBean.getName()+" to colNames");
		//System.out.println("add "+displayList.get(i)+" to displayListVisibleOnly");
    	}
    }
    pageContext.setAttribute("rowCount",new Integer((rowCount!=null?rowCount:"0")));
    pageContext.setAttribute("displayList",displayList);
    pageContext.setAttribute("resList",resList);
    pageContext.setAttribute("displayListVisibleOnly",displayListVisibleOnly);
    int index = 0;
    //int colIndex =0;
    if(resList!=null && !resList.isEmpty()){
%>
<table>
    <table width='100%'>
    <tr bgcolor="cc9999"><td>
       &nbsp;<font color="#FFFFFF" size="2"><b>
        <font face="Verdana, Arial, Helvetica, sans-serif">
            <bean:write name='searchinfoholder' property='displayLabel'/> Result
        </font></b></font></td>
   </tr>
   </table>
   <table>
    <tr  bgcolor="#CC9999">
    <logic:iterate id='label' name='displayListVisibleOnly' scope='page'>
        <td width="<bean:write name='label' property='size'/>"><font
        face="Verdana, Arial, Helvetica, sans-serif" size='1'><b>
        <bean:write name='label' property='value'/></b></font></td>
    </logic:iterate>
    
    </tr>
<!-- CASE #748 Comment Begin -->
<%--
    <logic:iterate id='values' name='resList' type='java.util.Vector' scope='page'>
<%
    colIndex =0;
%>
    <tr bgColor="<%= ((++index)%2==0?"#F7EEEE":"#FFFFFF") %>">
        <logic:iterate id='value' name='values' type='java.lang.String'>
        --%>
            <%-- <logic:equal name="colIndex"  value="0" > --%>
        <%--
<%
    if (colIndex ==0){
%>
                <td><div><a href ="JavaScript:populateResultData('<%=value%>',<%=index%>);"><%=UtilFactory.dispEmptyStr(value)%></a></div></td>
<%
    }else{
        if(colIndex==1){
%>
            <input type=hidden name='hdnLeadName<%=index%>' value='<%=value%>'>
<%
        }
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
%>
    		<tr bgColor="<%= ((++index)%2==0?"#F7EEEE":"#FFFFFF") %>">
<%
		for(int colIndex=0; colIndex<colNames.size(); colIndex++){
			Object objValue = resultRow.get(colNames.get(colIndex));
			String value = objValue == null ? "&nbsp" : objValue.toString();

			if (colIndex ==0) {
%>
                		<td><div><a href ="JavaScript:populateResultData('<%=value%>',<%=index%>);">
                		<%=UtilFactory.dispEmptyStr(value)%></a></div></td>
<%
    			}else{
        			if(colIndex==1){
%>
            				<input type=hidden name='hdnLeadName<%=index%>' value='<%=value%>'>
<%
        			}
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
            <td colspan='5'>
              <div align="left">
                <a href="JavaScript:history.back();"><img src="<bean:write name='ctxtPath'/>/images/searchagain.gif" width="80" height="22" border="0"></a>&nbsp;
                <a href="JavaScript:window.close();"><img src="<bean:write name='ctxtPath'/>/images/close.gif" width="42" height="22" border="0"></a>
                &nbsp; &nbsp; &nbsp; </div>
            </td>
          </tr>
    </table>
</table>
</FORM>
</body>
</html:html>

