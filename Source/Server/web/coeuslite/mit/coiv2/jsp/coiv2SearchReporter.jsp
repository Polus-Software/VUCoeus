<%-- 
    Document   : Coiv2SearchReporter
    Created on : Oct 25, 2010, 12:15:19 PM
    Author     : indhulekha
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java"
	import="edu.mit.coeus.utils.ComboBoxBean,
    edu.mit.coeus.search.bean.CriteriaBean,
    edu.mit.coeus.search.bean.FieldBean,
   edu.mit.coeus.search.bean.DisplayBean,
                java.util.Vector,
                java.util.HashMap,
                edu.mit.coeus.utils.UtilFactory,
                edu.mit.coeuslite.utils.CoeusLiteConstants"%>



<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>


<jsp:useBean id="displayList" scope="session" class="java.util.Vector" />
<jsp:useBean id="searchinfoholder" scope='session'
	class="edu.mit.coeus.search.bean.SearchInfoHolderBean" />
<bean:define id="criteriaList" name="searchinfoholder"
	property="criteriaList" />
<jsp:useBean id="fldName" scope='request' class="java.lang.String" />
<jsp:useBean id="searchName" scope='request' class="java.lang.String" />
<jsp:useBean id="searchResult" scope='request'
	class="java.util.Hashtable" />
<jsp:useBean id="ressList" scope="session" class="java.util.Vector" />
<jsp:useBean id="fieldName" scope='request' class="java.lang.String" />
<bean:define id='retrieveLimit' name='searchinfoholder'
	property='retrieveLimit' />
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>

<%
session.removeAttribute("isEvent");
Vector resList = null;
String strSearch  = "";
String strType    = "";
String search = (String)request.getAttribute("search");
HttpSession sessions = request.getSession();
HashMap resData = (HashMap)request.getAttribute("getRowData");
if (request.getParameter("search") != null)
  strSearch = request.getParameter("search");
if (request.getParameter("type") != null)
  strType = request.getParameter("type");
%>

<html:html locale="true">
<head>
<title><bean:message bundle="coi"
		key="searchWindow.title.reporter" /></title>
<style type="text/css">
//
div {
	font-family: verdana;
	font-size: 10px
}

//
a {
	font-family: verdana;
	font-size: 10px;
	color: blue;
	text-decoration: none;
}

//
a:active {
	color: red;
	text-decoration: none;
}

//
a:visited {
	color: blue;
	text-decoration: none;
}

//
a:hover {
	color: red;
	text-decoration: none;
}
</style>
<script language="javascript" type="text/JavaScript"
	src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/sorttable.js"></script>
<html:base />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<script language="javascript" type="text/JavaScript" src="_js.js"></script>
<script language="JavaScript" type="text/JavaScript">



            function getRowData(url) {
            window.opener.location.href = url;
            window.close();
            }

            function populateResultData(){
            resArray = new Array();

          <%if(resData != null) {
          java.util.Set keys= resData.keySet();
            for ( java.util.Iterator iterator = keys.iterator(); iterator.hasNext(); ) {
                    String key = iterator.next().toString();
                %>
            resArray["<%=key%>"] = "<%=resData.get(key)%>";

            <%
             }%>
            opener.fetch_Data(resArray);
            window.close();
           <% } %>
            }
        </script>
<style type="text/css">
</style>

</head>

<body onload="populateResultData()" class='tabtable'>
	<% String srch = "/searchCoiDisclosureReporter.do?type=+strType&disclosureSearch=true"; %>
	<%if(resData == null) {%>
	<html:form action="<%=srch%>" method="post">

		<%if(search!=null){%>
		<input type="hidden" name="fieldName" value='<%=fldName%>'>
		<input type="hidden" name="searchName" value='<%=searchName%>'>

		<table width="100%" border="0" cellpadding="0" cellspacing="0">

			<tr class="theader">
				<li><bean:message key="search.header1" /> <%--<%=session.getAttribute("type").toString()%> <%-- =strType --%>
					<bean:message key="search.header2" /> <b><bean:message
							key="search.header3" /> </b> <bean:message key="search.or" />
					<%--or--%> <b><bean:message key="search.header5" /></b> <bean:message
						key="search.or" />
					<%--or--%> <b><bean:message key="search.header6" /></b> <bean:message
						key="search.header4" /></li>

			</tr>
			<%
        Vector vecCriteria = (Vector)searchinfoholder.getCriteriaList();
        int count = 0;
        for(int i = 0; i < vecCriteria.size() ; i ++ ){
                CriteriaBean criteriaBean = (CriteriaBean)vecCriteria.get(i);
                FieldBean fieldBean = (FieldBean)criteriaBean.getFieldBean();
                String type = "" ;
                Vector vecComboList =null ;
                if(fieldBean!=null){
                    type = fieldBean.getType();
                    vecComboList = (Vector)fieldBean.getComboList();
                }
    %>
			<% if ( count%2 == 0){%>
			<tr>
				<%}%>
				<td>
					<%
                        if(type.equals("text")){%>
				
				<td width="1%" nowrap class="copy" align="right"><b><%=fieldBean.getLabel()%></b>:&nbsp;
				</td>
				<td><input class="textbox-long" type='text'
					name='<%=criteriaBean.getName()%>' />&nbsp;&nbsp;</td>
				<%}
                            if(type.equals("list")){%>
				<td width="1%" nowrap class="copy" align="right"><b><%=fieldBean.getLabel()%></b>:&nbsp;
				</td>
				<td><select name='<%=criteriaBean.getName()%>'
					class="textbox-long">
						<% if(vecComboList!=null && vecComboList.size()>0){
                                for(int index =0 ; index < vecComboList.size(); index++){
                                    ComboBoxBean cmbBean = (ComboBoxBean)vecComboList.get(index);
                            %>
						<option value="<%= cmbBean.getCode() %>"><%= cmbBean.getDescription() %></option>
						<% }}%>
				</select></td>
				<br>
				<%}%>
				</td>


				<%  count++ ;}%>
			
			<tr align="center">
				<td colspan="9" class="copy"><br> <span class="copy">
						<html:submit property="Search" value="Search"
							styleClass="clbutton" />
				</span> &nbsp;&nbsp;&nbsp; <span class="copy"> <html:button
							property="Cancel" value="Cancel" onclick="window.close();"
							styleClass="clbutton" /> <br>
					<br>
				</span></td>
			</tr>
		</table>

		<%}%>
		<div id='searchingDiv' style='display: none;'>
			<table width="100%" height="100%" align="center" border="0"
				cellpadding="0" cellspacing="0" class="tablemainsub">
				<tr>
					<td align='center' class='copybold'><bean:message
							key="search.searching" /> <br> <bean:message
							key="search.pleaseWait" /></td>
				</tr>
			</table>
		</div>


		<%  if (!strSearch.equalsIgnoreCase("true")) {
    displayList = (Vector)searchResult.get("displaylabels");
    resList = (Vector)searchResult.get("reslist");
    String rowCount = (String)searchResult.get("rowcount");
    Vector colNames = new Vector();%>
		<%pageContext.setAttribute("rowCount",new Integer((rowCount!=null?rowCount:"0")));
    pageContext.setAttribute("displayList",displayList);
    int index = 0;
    if(resList!=null && !resList.isEmpty()){
%>
		<table>
			<table width='100%' class="tabtable">
				<tr>
					<td class='tableheader'>&nbsp;<font color="#FFFFFF" size="2"><b>
								<font face="Verdana, Arial, Helvetica, sans-serif" color='black'>
									<bean:write name='searchinfoholder' property='displayLabel' />
									<bean:message key="search.result" />
									<%-- Result--%>
							</font>
						</b></font></td>
				</tr>
			</table>
			<!-- Top section for displaying search again and close window -->
			<table width="100%" border="0" class='tabtable' cellspacing="0"
				cellpadding="0">
				<tr>
					<td align="right" class="theader" height='30'><html:link
							href="javascript:history.go(-1)">
							<bean:message key="search.searchAgain" />
						</html:link></td>
					<td class='theader' height='30'>&nbsp;&nbsp;</td>
					<td align="left" class="theader" height='30'><html:link
							href="javascript://" onclick="window.close()">
							<bean:message key="search.closeWindow" />
						</html:link></td>
				</tr>
			</table>

			<table id="t1" class="sortable">
				<tr class='theader'>

					<logic:iterate id="dispBean" name="displayList"
						type="edu.mit.coeus.search.bean.DisplayBean">
						<%if(dispBean.isVisible()){
                        colNames.add(dispBean.getName());%>
						<td class='copy'
							width="<bean:write name='dispBean' property='size'/>"
							align="center"><b> <bean:write name='dispBean'
									property='value' />
						</b></td>
						<%}%>
					</logic:iterate>



				</tr>


				<%
	for(int resRowCnt=0; resRowCnt<resList.size(); resRowCnt++){
		HashMap resultRow = (HashMap)resList.get(resRowCnt);
%>
				<tr bgColor="<%= ((++index)%2==0?"#D6DCE5":"#DCE5F1") %>"
					onmouseover="className='TableItemOn'"
					onmouseout="className='TableItemOff'">

					<%
		for(int colIndex=0; colIndex<colNames.size(); colIndex++){
			Object objValue = resultRow.get(colNames.get(colIndex));
                        System.out.println(objValue);
                        System.out.println(resultRow);
			String value = objValue == null ? "&nbsp" : objValue.toString();
                        String destPage = CoeusLiteConstants.GENERAL_INFO_PAGE;

                        String searchLink = "javaScript:getRowData('"
                                        +request.getContextPath()
                                        +"/getannualdisclosure.do?SEARCH_ACTION=SEARCH_WINDOW&param1="
                                        +resultRow.get("DISCLOSURE_NUMBER")
                                        +"&param2="
                                        +resultRow.get("SEQUENCE_NUMBER")
                                        +"&param7="
                                        +resultRow.get("PERSON_ID")
                                         +"&param5="
                                        +resultRow.get("DISCLOSURE_EVENT")
                                        +"&param6="
                                        +resultRow.get("throughShowAllDiscl")
                                        +"')";
                        if(value.equalsIgnoreCase("new")) {
                            value = "Annual";
                        }

                         if (colIndex ==0) {

%>
					<td class='copy'><html:link href="<%=searchLink%>">
							<u><%=value%></u>
						</html:link></td>
					<%
    			}else{
            if((value!=null && !value.equals("") && !value.equals("&nbsp")) && (colIndex==2 || colIndex==3)){
                            edu.mit.coeuslite.utils.DateUtils date=new edu.mit.coeuslite.utils.DateUtils();
                            value=date.restoreDate(value,":/.,|-");
               }%>
					<td class='copy'><html:link href="<%=searchLink%>">
							<u><%=UtilFactory.dispEmptyStr(value)%></u>
						</html:link></td>
					<%
    			}
    		}
%>



				</tr>
				<%
	}
%>


			</table>
			<%
    }else{
%>
			<logic:greaterThan name='rowCount' value='0'>

				<div align='center'>
					<bean:message key="search.selection1" /><%=rowCount%><bean:message
						key="search.selection2" />
					<br>
					<bean:message key="search.selection3" /><%=retrieveLimit%><bean:message
						key="search.selection4" />
					<br>
					<bean:message key="search.selection5" />
					<div>
			</logic:greaterThan>
			<logic:equal name='rowCount' value='0'>
				<div align='center'>
					<bean:message key="search.noResult" />
					</>
					<%--No result found</div>--%>
			</logic:equal>
			<%
    }
%>

			<!-- Bottom section for displaying search again and close window-->
			<table width="100%" border="0" class='tabtable' cellspacing="0"
				cellpadding="0">
				<tr>
					<td align="right" class="theader" height='30'><html:link
							href="javascript:history.go(-1)">
							<bean:message key="search.searchAgain" />
						</html:link></td>
					<td class='theader' height='30'>&nbsp;&nbsp;</td>
					<td align="left" class="theader" height='30'><html:link
							href="javascript://" onclick="window.close()">
							<bean:message key="search.closeWindow" />
						</html:link></td>
				</tr>
			</table>
		</table>
		<% } %>
	</html:form>
	<%}%>
</body>
</html:html>