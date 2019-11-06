<%@ page contentType="text/html;charset=UTF-8" language="java"
	import="edu.mit.coeus.utils.ComboBoxBean,
   edu.mit.coeus.search.bean.DisplayBean,
                java.util.Vector,
                java.util.HashMap,
                edu.mit.coeus.utils.UtilFactory"%>



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
<%@ include file="CoeusContextPath.jsp"%>

<% 

Vector resList = null;
String strSearch  = "";
String strType    = "";
HttpSession sessions = request.getSession();
String search = (String)request.getAttribute("search");
 HashMap resData = (HashMap)request.getAttribute("getRowData");
if (request.getParameter("search") != null)
  strSearch = request.getParameter("search");
if (request.getParameter("type") != null)
  strType = request.getParameter("type");
  //  strType = (String) session.getAttribute("type");
 
%>

<html:html locale="true">
<head>
<title><%=session.getAttribute("type").toString()%></title>
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

<html:base />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">

<script language="javascript" type="text/JavaScript" src="_js.js"></script>
<script language="JavaScript" type="text/JavaScript">
      
   
   
    
     
function getRowData(rowId) {
    document.midYearDisclosure.action = "<%=request.getContextPath()%>/coiSearch.do?actionPerformed="+rowId;
    document.midYearDisclosure.submit();
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

<%--link href="../css/ipf.css" rel="stylesheet" type="text/css"--%>
<style type="text/css">
</style>

</head>

<body onload="populateResultData()" class='tabtable'>
	<% String srch = "/coiSearch.do?type=+strType"; %>
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
						key="search.header4" /> <%-- To search for an <%=strType%>, type <b> *value*</b> or <b>*value</b> or <b>value*</b> in a search field. --%>
				</li>

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
							<td maxlength="20"><select name='<%=fieldNames%>'>

									<logic:iterate id="cmbBean" name="field" property="comboList"
										type="edu.mit.coeus.utils.ComboBoxBean">
										<option value="<%= cmbBean.getCode() %>"><%= cmbBean.getDescription() %></option>
									</logic:iterate>

							</select></td>
						</logic:equal> <logic:notEqual name='field' property='type' value='list'>
							<td nowrap class="copy" align="right" maxlength="20"><b><%=field.getLabel()%></b>:&nbsp;
							</td>
							<td maxlength="20"><input class="textbox-long" type='text'
								name='<%=fieldNames%>' /> &nbsp;&nbsp;</td>
						</logic:notEqual></td>
			</logic:iterate>
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
		<%--/html:form--%>
		<%}%>
		<div id='searchingDiv' style='display: none;'>
			<table width="100%" height="100%" align="center" border="0"
				cellpadding="0" cellspacing="0">
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
				<tr valign="center">
					<td class='tableheader'>&nbsp;<font color="#FFFFFF" size="2"><b>
								<font face="Verdana, Arial, Helvetica, sans-serif" color='black'>
									<bean:write name='searchinfoholder' property='displayLabel' />
									<bean:message key="search.result" />
							</font>
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
			<table>
				<tr class='theader'>

					<%--  <logic:iterate id="dispBean" name="displayList" type="edu.mit.coeus.search.bean.DisplayBean">
        <%if(dispBean.isVisible()){
         colNames.add(dispBean.getName());%>
        <td width="<bean:write name='dispBean' property='name'/>" class='copy'><b>
        <bean:write name='dispBean' property='name'/></b></td>
        <%}%>
    </logic:iterate>  --%>


					<logic:iterate id="dispBean" name="displayList"
						type="edu.mit.coeus.search.bean.DisplayBean">
						<%if(dispBean.isVisible()){
         colNames.add(dispBean.getName());%>
						<%   if(dispBean.getName().equals("FULL_NAME")){
               %><td class='copy' nowrap
							width="<bean:write name='dispBean' property='name'/>"><b>
								<bean:write name='dispBean' property='name' />
						</b></td>
						<%  }else { %><td class='copy'
							width="<bean:write name='dispBean' property='name'/>"><b>
								<bean:write name='dispBean' property='name' />
						</b></td>
						<%  } %>

						<%}%>
					</logic:iterate>

				</tr>
				<%--logic:iterate id="resultRow" name="<%=resList%>" type="java.util.HashMap"--%>

				<%
	for(int resRowCnt=0; resRowCnt<resList.size(); resRowCnt++){
		HashMap resultRow = (HashMap)resList.get(resRowCnt);
%>
				<!-- JM 5-31-2011 updated per 4.4.2 -->
				<tr bgColor="<%= ((++index)%2==0?"#CCCCCC":"#D9D9D9") %>"
					onmouseover="className='TableItemOn'"
					onmouseout="className='TableItemOff'">
					<%--logic:iterate id="objValue" name="<%=resultRow%>" property="" type="java.lang.Object"--%>
					<%
		for(int colIndex=0; colIndex<colNames.size(); colIndex++){
			Object objValue = resultRow.get(colNames.get(colIndex));
			String value = objValue == null ? "&nbsp" : objValue.toString();
                        String searchLink = "javascript:getRowData('" +resRowCnt +"')";

			if (colIndex ==0) {
                            
%>
					<td nowrap class='copy'><html:link href="<%=searchLink%>">
							<%=UtilFactory.dispEmptyStr(value)%>
						</html:link></td>
					<%
    			}else{
        			if(colIndex==1){
%>

					<!--
                                    <td class='copy'>
                                    <html:link href="<%=searchLink%>">
                                        <u><input type=hidden name='hdnName<%=index%>' value='<%=value%>'></u>
                                    </html:link>
                                    </td>
                                    -->
					<%
        			}
%>
					<td class='copy'><html:link href="<%=searchLink%>">
							<u><%=UtilFactory.dispEmptyStr(value)%></u>
						</html:link></td>
					<%
    			}
    		}
%>
					<%--/logic:iterate--%>


				</tr>
				<%
	}
%>
				<%--/logic:iterate--%>


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

						<%--   <div align='center'>The current selection criteria will retrieve <%=rowCount%> rows.
        <br>This is more than the limit of <%=retrieveLimit%> set in Resource File.
        <br>Please modify the selection criteria to retrieve fewer rows.<div>   --%>
			</logic:greaterThan>
			<logic:equal name='rowCount' value='0'>
				<div align='center'>
					<bean:message key="search.noResult" />
				</div>
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


		<%}%>

	</html:form>
	<%}%>
</body>
</html:html>