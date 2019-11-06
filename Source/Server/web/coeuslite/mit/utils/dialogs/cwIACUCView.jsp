<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
	import="java.util.Vector,org.apache.struts.validator.DynaValidatorForm"%>

<%! String searchKey = ""; %>
<%
    String indexValue = request.getParameter("recordId");
    String selectedItem = request.getParameter("selectedItem").toString();
    String type = request.getParameter("type").toString();
    
    Vector vecList = null;
    String value = "";
    
    //Get Protocol Exceptions List
    if(type.equals("E")){
        vecList = (Vector)session.getAttribute("vecAddedExceptions");
    }
    
    if(vecList != null && vecList.size() > 0){
        DynaValidatorForm form = (DynaValidatorForm)vecList.get(Integer.parseInt(indexValue));
        if(selectedItem.trim().equals("Description")) {
            value = (String) form.get("exceptionDescription");
            searchKey = "protocolActions.Description";
        }
    }
    value = (value==null)?"":value;
%>
<%--script>
alert(value);
</script--%>
<html>
<head>
<title>Exception Description</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/ipf.css"
	rel="stylesheet" type="text/css">
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
</head>
<body>
	<table WIDTH='80%' border="0" align="center" cellpadding="0"
		cellspacing="0" class="tabtable">
		<tr bgcolor='#E1E1E1'>
			<td height="20%" align="left" valign=bottom>
				<table height="40%" width="100%" border="0" cellpadding="0"
					cellspacing="0" class="tableheader">
					<tr>
						<td align=left class='theader'><b> <bean:message
									key='<%=searchKey%>' bundle='iacuc' /></b></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td><html:textarea property="txtDesc" styleClass="copy"
					readonly="true" cols="100" rows="10" value="<%=value%>" /></td>
		</tr>
		<tr>
			<td align=left><html:button property="Close" value="Close"
					onclick="window.close();" styleClass="clbutton" /></td>
		</tr>
	</table>
</body>
</html>

