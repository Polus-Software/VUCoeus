<%@ page contentType="text/html;charset=UTF-8" language="java"
	import="java.util.Vector"%>
<%@page
	import="edu.mit.coeuslite.utils.CoeusLiteConstants,
                edu.mit.coeuslite.utils.bean.MenuBean"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="menuItems" scope="session" class="java.util.Vector" />

<html:html locale="true">
<% try{ 
    String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
    String sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());
    String mode = (String)session.getAttribute("mode"+session.getId());
    String actionCode = (String)session.getAttribute("IACUC_ACTION_CODE");
%>
<html:base />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<style type="text/css">
<!--
body {
	margin-left: 3px;
}
-->
</style>
<script>          
             function call(link) {
                CLICKED_LINK = '<%=request.getContextPath()%>'+link;
                return validate();
             }
             function showAlert(){
             alert("<bean:message bundle="proposal" key="generalInfoProposal.notImplemented"/>");
             }     
             
           <%  
             String linkVal ="";
                   if("114".equals(actionCode)){ 
                        linkVal =  request.getContextPath()+"/getNotifyIacuc.do?IACUC_ACTION_CODE="+actionCode;
                    }else if("100".equals(actionCode)){
                        linkVal = request.getContextPath()+"/getIacucSubmitForReview.do?PAGE=SR";
                    }else if("205".equals(actionCode)){
                        linkVal = request.getContextPath()+"/getIacucResponseApproval.do?IACUC_ACTION_CODE="+actionCode;
                    }else if("115".equals(actionCode)){
                        linkVal = request.getContextPath()+"/getIacucWithdrawSubmission.do?IACUC_ACTION_CODE="+actionCode;
                    }else if("108".equals(actionCode)){
                        linkVal = request.getContextPath()+"/getRequestForLiftHold.do?IACUC_ACTION_CODE="+actionCode;
                    }else if("107".equals(actionCode)){
                        linkVal = request.getContextPath()+"/getRequestForDeactivate.do?IACUC_ACTION_CODE="+actionCode;
                    }
             
                   
                %>
             
        </script>
</head>
<body>
	<table width="100%" height="100%" border="0" cellpadding="0"
		cellspacing="3" class="table">

		<tr class="rowLine">
			<%int index=1;%>
			<td align="left" valign="top">
				<table width="98%" border="0" align="center" cellpadding="2"
					cellspacing="0" class="table">
					<jsp:useBean id="iacucActionMenuItems" scope="session"
						class="java.util.Vector" />
					<logic:iterate name="iacucActionMenuItems"
						id="protocolActionMenuBean"
						type="edu.mit.coeuslite.utils.bean.MenuBean">

						<%
                            String linkName = protocolActionMenuBean.getMenuName();
                            String linkValue = protocolActionMenuBean.getMenuLink();
                            String groupName = protocolActionMenuBean.getGroup();
                            String menuId = protocolActionMenuBean.getMenuId();
                            boolean isVisible = protocolActionMenuBean.isVisible();
                          %>
						<%if(index==Integer.parseInt(groupName)){%>
						<!--<tr>
                            <td>&nbsp;</td>
                          </tr>-->
				</table> <br>
				<table width="98%" border="0" align="center" cellpadding="2"
					cellspacing="0" class="table">
					<%index++;}%>
					<%if(menuId.equals("PA004")) {%>
					<tr class="menuHeaderName">
						<td></td>
						<td colspan='2'><%=linkName%></td>
					</tr>
					<%}else {%>
					<%if(isVisible){%>
					<tr>
						<td class="coeusMenu"><logic:equal
								name="protocolActionMenuBean" property="dataSaved" value="true">
								<img
									src="<bean:write name='ctxtPath'/>/coeusliteimages/img_saved.gif"
									width="22" height="19">
							</logic:equal></td>

						<td class="coeusMenu"><bean:define id="name"
								name="protocolActionMenuBean" property="menuName" /> <bean:define
								id="link_Value" name="protocolActionMenuBean"
								property="menuLink" /> <bean:define id="selected"
								name="protocolActionMenuBean" property="selected" /> <bean:define
								id="fieldName" name="protocolActionMenuBean"
								property="fieldName" /> <bean:define id="dataSaved"
								name="protocolActionMenuBean" property="dataSaved" /> <%
                                            if (linkValue.equals("/getIacucData.do")){
                                                String link = "return call('/getIacucData.do?protocolNumber="+protocolNumber+"&sequenceNumber="+sequenceNumber+"&PAGE=G')";%>
							<a
							href="<%=request.getContextPath() %>/getIacucData.do?protocolNumber=<%=protocolNumber%>&sequenceNumber=<%=sequenceNumber%>&PAGE=G"
							onclick="<%=link%>"> <%=linkName%>
						</a> <%}else if (linkValue.equals("/getProtocolSubmissionAction.do")){
                                                                                    
                                            %> <% if(((Boolean)selected).booleanValue()) { %>
							<font color="#6D0202"><b> <%=linkName%>
							</b> </font> <%}else {
                                                String actualLink = linkVal.replaceFirst(request.getContextPath(), "");
                                                String link = "return call('"+actualLink+"')";%>
							<a href="<%=linkVal%>" onclick="<%=link%>"> <%=linkName%>
						</a> <%}%> <%}else{
                                            %> <% if(((Boolean)selected).booleanValue()) { %>
							<font color="#6D0202"><b> <%=linkName%>
							</b> </font> <%}else {
                                            String link = "return call('"+linkValue+"')";%>
							<html:link page="<%=linkValue%>" styleClass="copy"
								onclick="<%=link%>">
								<%=linkName%></html:link> <%}%> <%}%></td>
						<td align=right nowrap class="selectedMenuIndicator"><logic:equal
								name="protocolActionMenuBean" property="selected" value="true">
								<bean:message key="menu.selected" />
							</logic:equal></td>
						<%}%>
					</tr>
					<%}%>

					</logic:iterate>

				</table> <%}catch(Exception ex){
                    ex.printStackTrace();
                }%>
			</td>
		</tr>
	</table>
</body>
</html:html>
