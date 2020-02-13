<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ page import="edu.mit.coeuslite.utils.CoeusLiteConstants"%>
<%@ page import="java.util.Vector,java.util.HashMap"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%
// 4331: PI cannot view long comment in approval comments thru CoeusLite IRB - Start
// String value = request.getParameter("value").toString();
// if(value == null || value.equals("null")){
//    value = "";
//}    

String indexValue = request.getParameter("value").toString();
String value = "";

 Vector vecActionDetails = (Vector)session.getAttribute("protocolActionDetails");
      if(vecActionDetails != null && vecActionDetails.size() > 0){
        HashMap hmActionData = (HashMap)vecActionDetails.get(Integer.parseInt(indexValue) - 1);
        value = (String) hmActionData.get("Comments");
    }
// 4331: PI cannot view long comment in approval comments thru CoeusLite IRB - End
%>
<html>
<head>
<title>Comments</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/ipf.css"
	rel="stylesheet" type="text/css">
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
</head>
<body>
	<form name="comments">
		<table WIDTH='100%' border="0" align="center" cellpadding="0"
			cellspacing="0" class="tabtable">
			<tr bgcolor='#E1E1E1'>
				<td height="15%" align="left" valign=bottom class="theader">
					<table height="40%" border="0" cellpadding="0" cellspacing="0"
						class="tableheader">
						<tr>
							<td align=left class='theader'><b> <bean:message
										key="protocolActions.Comments" /></b></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td><html:textarea property="txtDesc" styleClass="copy"
						cols="100" rows="10" value="<%=value%>" readonly="true" /></td>
			</tr>
			<tr>
				<td align='left'><html:button property="Close" value="Close"
						styleClass="clbutton" onclick="window.close();" /></td>
			</tr>
		</table>
	</form>
</body>
</html>