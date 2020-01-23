<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
	import="java.util.Vector,org.apache.struts.validator.DynaValidatorForm"%>

<%--Added for 1485-user data entry is not restricted -(Wrapping) start--%>
<%! String alterSearchKey; %>
<%--Added for 1485-2260-user data entry is not restricted -(Wrapping) end--%>

<%
    String indexValue = request.getParameter("recordId").toString();
    //Added for 1485-user data entry is not restricted -(Wrapping) start
    String alterSearchItem = request.getParameter("alternativeSearchItem").toString();;
    //Added for 1485-user data entry is not restricted -(Wrapping) start
    Vector vecList = null;
    String value = "";
    vecList = (Vector)session.getAttribute("vecAddedAlternativeSearches");
   
    if(vecList != null && vecList.size() > 0 && Integer.parseInt(indexValue) < vecList.size()){
        DynaValidatorForm form = (DynaValidatorForm)vecList.get(Integer.parseInt(indexValue));
        //Added for 1485-user data entry is not restricted -(Wrapping) start
        if(alterSearchItem.trim().equals("yearsSearched")) {
           value = (String) form.get("yearsSearched");
           alterSearchKey = "protocolActions.YearsSearched";
        } else if(alterSearchItem.trim().equals("keyWordsSearched")) {
            value = (String) form.get("keyWordsSearched");
            alterSearchKey = "protocolActions.KeywordsSearched";
        } else if(alterSearchItem.trim().equals("comments")) {
            value = (String) form.get("comments");
            alterSearchKey = "protocolActions.Comments";
       }
        //Added for 1485-user data entry is not restricted -(Wrapping) end
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
<script type="text/javascript">
     function CloseWindow(value)
   {   
      window.frameElement.reply(value);
   }
    </script>
<body>
	<table WIDTH='80%' border="0" align="center" cellpadding="0"
		cellspacing="0" class="tabtable">
		<tr bgcolor='#E1E1E1'>
			<td height="20%" align="left" valign=bottom>
				<table height="40%" width="100%" border="0" cellpadding="0"
					cellspacing="0" class="tableheader">
					<tr>
						<%--Added for 1485-user data entry is not restricted -(Wrapping) start--%>
						<%-- <td align=left class='theader'><b> <bean:message key="protocolActions.Comments"/></b> </td> --%>
						<td align=left class='theader'><b> <bean:message
									key='<%=alterSearchKey%>' bundle='iacuc' /></b></td>
						<%--Added for 1485-user data entry is not restricted -(Wrapping) end--%>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td><html:textarea property="txtDesc" styleClass="copy"
					readonly="true" cols="100" rows="10" value="<%=value%>" /></td>
		</tr>
		<tr>
			<td style="height: 22px;" align=left><html:button
					property="Close" value="Close" onclick="window.close();"
					styleClass="clbutton" /></td>
		</tr>
	</table>
</body>
</html>

