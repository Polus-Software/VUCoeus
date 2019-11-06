<%-- 
    Document   : awardSearch
    Created on : Jan 6, 2011, 10:50:53 AM
    Author     : vineetha
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
<title>Award Search</title>
<style type="text/css">
.copy u {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 12px;
	color: #003399;
}

div {
	font-family: verdana;
}

a {
	font-family: verdana;
	color: blue;
	text-decoration: none;
}

a:active {
	color: red;
	text-decoration: none;
}

a:visited {
	color: blue;
	text-decoration: none;
}

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

<body onload="populateResultData()" style="min-width: 900px;" />
<% String srch = "/awardSearch.do?type=+strType&search=true"; %>
<%if(resData == null) {%>
<html:form action="/awardSearch.do?type=AwardList_Search" method="post">

	<%if(search!=null){%>
	<input type="hidden" name="fieldName" value='<%=fldName%>'>
	<input type="hidden" name="searchName" value='<%=searchName%>'>
	<table class="tabtable" width="100%" style="height: 96%;">
		<tr>
			<td>

				<table width="100%" border="0" cellpadding="0" cellspacing="0"
					style="height: auto;">

					<tr class="theader">
						<li><bean:message key="search.header1" /> <%--<%=session.getAttribute("type").toString()%> =strType--%>
							<bean:message key="search.header2" /> <b><bean:message
									key="search.header3" /> </b> <bean:message key="search.or" /><b><bean:message
									key="search.header5" /></b> <bean:message key="search.or" /> <b><bean:message
									key="search.header6" /></b> <bean:message key="search.header4" /></li>

					</tr>
					<%--<tr><td>&nbsp;</td></tr>--%>
				</table>
			</td>
		</tr>
		<tr valign="top">
			<td>
				<table width="100%" cellpadding="0" cellspacing="0" border="0"
					style="height: auto;">
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

						<%
                        if(type.equals("text")){%>
						<td width="auto" nowrap class="copy" align="right"
							style="min-width: 150px;"><b><%=fieldBean.getLabel()%></b>:&nbsp;
						</td>
						<td width="auto" style="min-width: 150px;"><input
							class="textbox-long" type='text'
							name='<%=criteriaBean.getName()%>' />&nbsp;&nbsp;</td>
						<%}
                            if(type.equals("list")){%>
						<td width="auto" nowrap class="copy" align="right"
							style="min-width: 150px;"><b><%=fieldBean.getLabel()%></b>:&nbsp;
						</td>
						<td width="auto" style="min-width: 150px;"><select
							name='<%=criteriaBean.getName()%>' class="textbox-long">
								<% if(vecComboList!=null && vecComboList.size()>0){
                                for(int index =0 ; index < vecComboList.size(); index++){
                                    ComboBoxBean cmbBean = (ComboBoxBean)vecComboList.get(index);
                            %>
								<option value="<%= cmbBean.getCode() %>"><%= cmbBean.getDescription() %></option>
								<% }}%>
						</select></td>
						<br>
						<%}%>



						<%  count++ ;
  if ( count%2 == 0){%>
					</tr>
					<%
        }
 }%>

				</table>
			</td>
		</tr>
		<tr valign="top">
			<td>
				<table width="100%" border="0" style="height: auto;">
					<tr valign="top">
						<td align="center" class="copy"><br> <span class="copy">
								<html:submit property="Search" value="Search"
									styleClass="clbutton" />
						</span> &nbsp;&nbsp;&nbsp; <span class="copy"> <html:button
									property="Cancel" value="Cancel" onclick="window.close();"
									styleClass="clbutton" /> <br>
							<br>
						</span></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
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
				</div> <%  if (!strSearch.equalsIgnoreCase("true")) {
    displayList = (Vector)searchResult.get("displaylabels");
    resList = (Vector)searchResult.get("reslist");
    String rowCount = (String)searchResult.get("rowcount");
    Vector colNames = new Vector();%> <%pageContext.setAttribute("rowCount",new Integer((rowCount!=null?rowCount:"0")));
    pageContext.setAttribute("displayList",displayList);
    int index = 0;
    if(resList!=null && !resList.isEmpty()){
%>

				<table width="100%" border="0">
					<tr>
						<td>
							<table width="100%" class="tabtable" border="0">
								<tr>
									<td class='tableheader' colspan="15" width="100%">&nbsp;
										<center>
											<b> <font face="Verdana, Arial, Helvetica, sans-serif"
												color='black'> <bean:write name='searchinfoholder'
														property='displayLabel' /> <bean:message
														key="search.result" />
											</font></b>
										</center>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td>
							<!-- Top section for displaying search again and close window -->
							<table width="100%" border="0" class="tabtable" cellspacing="0"
								cellpadding="0">
								<tr>
									<td align="right" class="theader" height='30' colspan="10">
										<html:link href="javascript:history.go(-1)">
											<bean:message key="search.searchAgain" />
										</html:link>
									</td>
									<td class='theader' height='30'>&nbsp;&nbsp;</td>
									<td align="left" class="theader" height='30'><html:link
											href="javascript://" onclick="window.close()">
											<bean:message key="search.closeWindow" />
										</html:link></td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td>
							<table id="t1" class="sortable" width="auto" cellpadding="0"
								cellspacing="0" border="0">
								<tr class="theader">

									<logic:iterate id="dispBean" name="displayList"
										type="edu.mit.coeus.search.bean.DisplayBean">
										<%if(dispBean.isVisible()){
                        colNames.add(dispBean.getName());%>
										<td class="copy" width="200px">
											<%String dispValue = dispBean.getValue();%> <b> <%=dispValue%>
										</b>
										</td>
										<%}%>
									</logic:iterate>

								</tr>


								<%
	for(int resRowCnt=0; resRowCnt<resList.size(); resRowCnt++){
		HashMap resultRow = (HashMap)resList.get(resRowCnt);
%>
								<tr bgColor="<%= ((++index)%2==0?"#D6DCE5":"#DCE5F1") %>"
									onmouseover="className='TableItemOn'"
									onmouseout=" className='TableItemOff'">

									<%
		for(int colIndex=0; colIndex<colNames.size(); colIndex++){
			Object objValue = resultRow.get(colNames.get(colIndex));
                        System.out.println(objValue);
                        System.out.println(resultRow);
			String value = objValue == null ? "&nbsp" : objValue.toString();
                        String destPage = CoeusLiteConstants.GENERAL_INFO_PAGE;
                        
                        String searchLink = "javaScript:getRowData('"
                                        +request.getContextPath()
                                        +"/getAwardInfo.do?SEARCH_ACTION=SEARCH_WINDOW&awardNumber="
                                        +resultRow.get("AWARD_NUMBER")+"')";

                         if (colIndex ==0) {

%>
									<td style="width: auto;" class="copy"><html:link
											href="<%=searchLink%>">
											<u><%=value%></u>
										</html:link></td>
									<%
    			}else{
            if((value!=null && !value.equals("") && !value.equals("&nbsp")) && (colIndex==2 || colIndex==3)){
                            edu.mit.coeuslite.utils.DateUtils date=new edu.mit.coeuslite.utils.DateUtils();
                            value=date.restoreDate(value,":/.,|-");
               }%>
									<td class="copy"><html:link href="<%=searchLink%>">
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


							</table> <%
    }else{
%> <logic:greaterThan name='rowCount' value='0'>

								<div align='center'>
									<bean:message key="search.selection1" /><%=rowCount%><bean:message
										key="search.selection2" />
									<br>
									<bean:message key="search.selection3" /><%=retrieveLimit%><bean:message
										key="search.selection4" />
									<br>
									<bean:message key="search.selection5" />
									<div>
							</logic:greaterThan> <logic:equal name='rowCount' value='0'>
								<div align='center'>
									<bean:message key="search.noResult" />
									</>No result found
								</div>
							</logic:equal> <%
    }
%> <!-- Bottom section for displaying search again and close window-->
							<table width="100%" border="0" class='tabtable' cellspacing="0"
								cellpadding="0">
								<tr>
									<td align="right" class="theader" height='30'><html:link
											href="javascript:history.go(-1)">
											<bean:message key="search.searchAgain" />
										</html:link></td>
									<td class="theader" height='30'>&nbsp;&nbsp;</td>
									<td align="left" class="theader" height='30'><html:link
											href="javascript://" onclick="window.close()">
											<bean:message key="search.closeWindow" />
										</html:link></td>
								</tr>
							</table>
						</td>
					</tr>
				</table> <% } %>
			</td>
		</tr>
		<tr style="height: 100%;">
			<td></td>
		</tr>
	</table>
</html:form>
<%}%>
</body>
</html:html>




