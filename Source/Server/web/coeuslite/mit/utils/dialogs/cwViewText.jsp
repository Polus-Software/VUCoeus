<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ page import="edu.mit.coeuslite.utils.CoeusLiteConstants"%>
<%@ page
	import="edu.mit.coeuslite.utils.CoeusDynaBeansList,
                java.util.ArrayList,java.util.List,
                org.apache.struts.action.DynaActionForm"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%
    /*
    String indexValue = request.getParameter("value").toString();    
    String value = "";    
    CoeusDynaBeansList coeusDynaBeansList = (CoeusDynaBeansList)session.getAttribute("correspondentsList");    
    List list = (List)coeusDynaBeansList.getList();
    if(list != null && list.size() > 0){
        DynaActionForm form = (DynaActionForm)list.get(Integer.parseInt(indexValue));
        //System.out.println("form >>>>>> "+form);
        value = (String)form.get("comments");
        if(value == null){
            value = "";
        }
    }
    */
    String value = request.getParameter("value").toString();
    if(value == null || value.equals("null")){
        value = "";
    }    
%>

<%
        String mode=(String)session.getAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId());
        mode = (mode == null)? "" : mode;
        boolean modeValue=false;
        if(mode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE)){
            modeValue=true;
        }else if(mode.equalsIgnoreCase(CoeusLiteConstants.MODIFY_MODE)){
            modeValue=false;
        }else if(mode.equalsIgnoreCase(CoeusLiteConstants.ADD_MODE)){
            modeValue=false;
        }
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
<script>
    /*
    function fetchData(){
        opener.insertData(document.test.txtDesc.value);
        window.close();
    }
    */
</script>
</head>
<body>
	<form name="test">
		<table WIDTH='100%' border="0" align="center" cellpadding="0"
			cellspacing="0" class="tabtable">
			<tr bgcolor='#E1E1E1'>
				<td height="15%" align="left" valign=bottom>
					<table height="40%" border="0" cellpadding="0" cellspacing="0"
						class="tableheader">
						<tr>
							<td align=left class='copybold'><b> <bean:message
										key="correspondents.comments" /></b></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td><html:textarea property="txtDesc" disabled="<%=modeValue%>"
						styleClass="copy" cols="100" rows="10" value="<%=value%>"
						readonly="true" /></td>
			</tr>
			<tr>
				<td align='right'><html:button property="Close" value="Close"
						styleClass="clbutton" onclick="window.close();" /></td>
			</tr>
		</table>
	</form>
</body>
</html>