<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
	import="java.util.Vector,org.apache.struts.validator.DynaValidatorForm"%>
<%
    String indexValue = request.getParameter("value").toString();
    
    String reviewCommentsFrom = request.getParameter("reviewCommentsFrom").toString();
    
    
    Vector vecList = null;
    String value = "";
    
    if("MyReviewComments".equals(reviewCommentsFrom)){
        vecList = (Vector)session.getAttribute("myIacucReviewComments");
    }else if("OtherReviewComments".equals(reviewCommentsFrom)){
        vecList = (Vector)session.getAttribute("otherIacucReviewerReviewComments");
    }
    
    
    if(vecList != null && vecList.size() > 0 && Integer.parseInt(indexValue) < vecList.size()){
        DynaValidatorForm form = (DynaValidatorForm)vecList.get(Integer.parseInt(indexValue));
        value = (String) form.get("minuteEntry");
    }
    value = (value==null)?"":value;
    
%>
<%--script>
alert(value);
</script--%>
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
	<table WIDTH='80%' border="0" align="center" cellpadding="0"
		cellspacing="0" class="tabtable">
		<tr bgcolor='#E1E1E1'>
			<td height="20%" align="left" valign=bottom>
				<table height="40%" width="100%" border="0" cellpadding="0"
					cellspacing="0" class="tableheader">
					<tr>
						<td align=left class='theader'><b> <bean:message
									key="protocolActions.Comments" /></b></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td><html:textarea property="txtDesc" styleClass="copy"
					readonly="true" cols="100" rows="10" value="<%=value%>" /></td>
		</tr>
		<tr>
			<td style="height: 25px;" align=left><html:button
					style="width:18%;height:22px;" property="Close" value="Close"
					onclick="window.close();" styleClass="clbutton" /></td>
		</tr>
	</table>
</body>
</html>

