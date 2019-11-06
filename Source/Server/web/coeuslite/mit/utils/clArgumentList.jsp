<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="java.util.Vector"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="argumentList" scope="session" class="java.util.Vector" />
<html:html>

<head>
<title>
	<%
    boolean isDataPresent = false;
    String reqArgument = request.getParameter("searchName");
    if(reqArgument!= null && (reqArgument.equalsIgnoreCase("w_arg_value_list")|| reqArgument.equalsIgnoreCase("w_select_cost_element"))){
        isDataPresent = true;
    }
%> JSP Page
</title>
<html:base />
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/ipf.css"
	rel="stylesheet" type="text/css">
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<script>
    function processRequest(listCode,listDesc){
        // modified for Case#3023 - Coeus Lite - Others Tab - Start
        listDesc = listDesc == 'null' || listDesc == 'undefined' ?"":listDesc;
        // modified for Case#3023 - Coeus Lite - Others Tab - End
        opener.put_Data(listCode,listDesc);        
         window.close();
    }
</script>
</head>
<body>
	<table width="100%" height="100%" border="0" cellpadding="0"
		cellspacing="0" class="table">
		<tr valign=top>
			<td height='10'>
				<table width="100%" border="0" cellpadding="0" cellspacing="0"
					class="tableheader">
					<tr valign="top">
						<td>
							<% if(isDataPresent){%> <bean:message bundle="proposal"
								key="argumentList.lookupValues" /> <%}else{%> <bean:message
								bundle="proposal" key="argumentList.lookupValuesFor" /> <%=request.getParameter("argument")%>
							<%}%>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr valign="top">
			<td>
				<table width="100%" border="0" cellpadding="0" cellspacing="0"
					class="tabtable">
					<tr class='theader'>
						<td width='10%'>
							<%if(isDataPresent){%> <bean:message bundle="proposal"
								key="argumentList.value" /> <%}else{%> <bean:message
								bundle="proposal" key="argumentList.code" /> <%}%>
						</td>
						<td width='20%'><bean:message bundle="proposal"
								key="argumentList.description" /></td>
					</tr>
					<% int indexColor = 1;%>
					<logic:present name="argumentList">
						<logic:iterate id="listData" name="argumentList"
							type="edu.mit.coeuslite.utils.ComboBoxBean">
							<!-- JM 5-31-2011 updated per 4.4.2 -->
							<%  String strBgColor = "#CCCCCC";
                if (indexColor%2 == 0) { 
                    strBgColor = "#DCE5F1"; }
           %>
							<tr bgcolor="<%=strBgColor%>"
								onmouseover="className='TableItemOn'"
								onmouseout="className='TableItemOff'">
								<td height='20'>
									<%String pageLink = "javaScript:processRequest('"+listData.getCode()+"','"+listData.getDescription()+"')";
            %> <html:link href="<%=pageLink%>">
										<bean:write name="listData" property="code" />
									</html:link>
								</td>
								<td><html:link href="<%=pageLink%>">
										<bean:write name="listData" property="description" />
									</html:link></td>
							</tr>
							<% indexColor++;%>
						</logic:iterate>
					</logic:present>

				</table>
			</td>
		</tr>
	</table>
</body>
</html:html>
