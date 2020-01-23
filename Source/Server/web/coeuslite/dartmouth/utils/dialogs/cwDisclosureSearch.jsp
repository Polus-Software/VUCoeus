<%@ page contentType="text/html;charset=UTF-8" language="java"
	import="edu.mit.coeus.utils.ComboBoxBean,
edu.mit.coeus.search.bean.DisplayBean,
java.util.Vector,
java.util.HashMap,
java.util.Hashtable,
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
<jsp:useBean id="searchResult" scope='session'
	class="java.util.Hashtable" />
<jsp:useBean id="ressList" scope="session" class="java.util.Vector" />
<jsp:useBean id="fieldName" scope='request' class="java.lang.String" />
<bean:define id='retrieveLimit' name='searchinfoholder'
	property='retrieveLimit' />
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
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
if (request.getParameter("COI_DISCL_TYPE") != null)
    strType = request.getParameter("COI_DISCL_TYPE");
%>

<html:html locale="true">
<head>
<title><%=session.getAttribute("COI_DISCL_TYPE").toString()%></title>
<style type="text/css">
</style>

<html:base />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<script language="javascript" type="text/JavaScript"
	src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/sorttable.js"></script>
<script language="javascript" type="text/JavaScript" src="_js.js"></script>
<script language="JavaScript" type="text/JavaScript">
      
      function getRowData(url) {
            var url = url;
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


</head>

<body onload="populateResultData()" class='tabtable'>
	<% String srch = "/getSearchDisclosures.do?type=+strType"; %>
	<%if(resData == null) {%>
	<html:form action="<%=srch%>" method="post">


		<%if(search!=null){%>

		<input type="hidden" name="fieldName" value='<%=fldName%>'>
		<input type="hidden" name="searchName" value='<%=searchName%>'>

		<table width="100%" border="0" cellpadding="0" cellspacing="0">

			<tr class="theader">
				<li><bean:message bundle="coi" key="search.header1" /> <bean:message
						bundle="coi" key="search.header2" /> <b><bean:message
							bundle="coi" key="search.header3" /> </b> or <b><bean:message
							bundle="coi" key="search.header5" /></b> or <b><bean:message
							bundle="coi" key="search.header6" /></b> <bean:message bundle="coi"
						key="search.header4" /></li>

			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
			<logic:iterate id='criteria' name='criteriaList' indexId="index">

				<%Integer count = (Integer)pageContext.getAttribute("index");
            
            boolean isNewRow = false;
            if(count.intValue() % 2 == 0){%>
				<tr>
					<%}%>
					<td><bean:define id='field' name='criteria'
							property='fieldBean' type="edu.mit.coeus.search.bean.FieldBean" />
						<bean:define id='fieldNames' name='field' property='name'
							type='java.lang.String' /> <logic:equal name='field'
							property='type' value="list">
							<bean:define id='fields' name='field' property='comboList'
								type='java.util.Vector' />
							<td nowrap class="copy" align="right" maxlength="20"><b><%=field.getLabel()%></b>:&nbsp;
							</td>
							<td maxlength="30"><select class="textbox-long"
								name='<%=fieldNames%>'>

									<logic:iterate id="cmbBean" name="field" property="comboList"
										type="edu.mit.coeus.utils.ComboBoxBean">
										<option value="<%= cmbBean.getCode() %>"><%= cmbBean.getDescription() %></option>
									</logic:iterate>

							</select></td>
						</logic:equal> <logic:notEqual name='field' property='type' value='list'>
							<td nowrap class="copy" align="right" maxlength="30"><b><%=field.getLabel()%></b>:&nbsp;
							</td>
							<td maxlength="20"><input class="textbox-long" type='text'
								name='<%=fieldNames%>' />&nbsp;&nbsp;</td>

						</logic:notEqual></td>
			</logic:iterate>
			<tr align="center">
				<td colspan="9" class="copy"><br> <span class="copy">
						<html:submit property="Search" value="Search"
							styleClass="clbutton" /><a href="javascript:search();"></a>
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
				cellpadding="0" cellspacing="0">
				<tr>
					<td align='center' class='copybold'><bean:message bundle="coi"
							key="clDisclosureSearch.searching" /><br> <bean:message
							bundle="coi" key="clDisclosureSearch.pleaseWait" /></td>
				</tr>
			</table>
		</div>


		<%  if (!strSearch.equalsIgnoreCase("true")) {
            displayList = (Vector)searchResult.get("displaylabels");
            session.setAttribute("disclosure","searchDisclosure");
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
				<tr valign="center">
					<td class='tableheader'>&nbsp;<font color="#FFFFFF" size="2"><b>
								<font face="Verdana, Arial, Helvetica, sans-serif" color='black'>
									Disclosure Search Result </font>
						</b></font></td>
				</tr>
			</table>
			<!-- Top section for displaying search again and close window-->
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
			<table id="t1" class="sortable" width="100%">
				<tr class='theader'>

					<logic:iterate id="dispBean" name="displayList"
						type="edu.mit.coeus.search.bean.DisplayBean">
						<%if(dispBean.isVisible()){
                    colNames.add(dispBean.getName());%>
						<%   if(dispBean.getName().equals("FULL_NAME")){
                    %><td nowrap
							width="<bean:write name='dispBean' property='name'/>"><font
							face="Verdana, Arial, Helvetica, sans-serif" size='1'><b>
									<bean:write name='dispBean' property='name' />
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									&nbsp;&nbsp;
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							</b></font></td>
						<%  }else { %><td
							width="<bean:write name='dispBean' property='name'/>"><font
							face="Verdana, Arial, Helvetica, sans-serif" size='1'><b>
									<bean:write name='dispBean' property='name' />
							</b></font></td>
						<%  } %>

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
                            String value = objValue == null ? "&nbsp" : objValue.toString();
                            String searchLink = "javaScript:getRowData('"
                                        +request.getContextPath() 
                                        +"/coiMain.do?SEARCH_ACTION=SEARCH_WINDOW&coiDisclosureNumber="
                                        +resultRow.get("COI_DISCLOSURE_NUMBER")
                                        +"&COI_DISCL_TYPE=disclosureSearch" +
                                        "')";
                            if (colIndex ==0) {
                %>
					<td><html:link href="<%=searchLink%>">
							<u><%=UtilFactory.dispEmptyStr(value)%></u>
						</html:link></td>
					<%
                            }else {
                                if(colIndex==1){
                %>


					<html:link href="<%=searchLink%>">
						<input type=hidden name='hdnName<%=index%>' value='<%=value%>'>
					</html:link>

					<%
                                }else if(colIndex == 3 && value != null){
                                edu.mit.coeuslite.utils.DateUtils date=new edu.mit.coeuslite.utils.DateUtils();
                                value = value.toLowerCase();
                                value = value.substring(0,3)+value.substring(3,4).toUpperCase()+value.substring(4);
                                value=date.restoreDate(value,":/.,|-");
                                value = date.formatDate(value,"MM/dd/yyyy");       
                                }
                %>
					<td class='copy'><html:link href="<%=searchLink%>">
							<%=UtilFactory.dispEmptyStr(value)%>
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
					<bean:message bundle="coi" key="search.selection1" /><%=rowCount%><bean:message
						bundle="coi" key="search.selection2" />
					<br>
					<bean:message bundle="coi" key="search.selection3" /><%=retrieveLimit%><bean:message
						bundle="coi" key="search.selection4" />
					<br>
					<bean:message bundle="coi" key="search.selection5" />
					<div>
			</logic:greaterThan>
			<logic:equal name='rowCount' value='0'>
				<div align='center'>
					<bean:message bundle="coi" key="search.noResult" />
				</div>
			</logic:equal>
			<%
        }
        %>
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
		</table>


		<% } %>
	</html:form>
	<%}%>
</body>
</html:html>