<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>

<html:html>

<% // COEUSDEV-202: Error about mandatory questionnaire - Does not identify the questionnaire - Start
String mandaroryQnrMessage = (String) request.getAttribute("mandatoryQnrMessage");  
String incompleteQnrMessage = (String) request.getAttribute("incompleteQnrMessage"); 
String newVersionQnrMessage = (String) request.getAttribute("newVersionQnrMessage"); 
// COEUSDEV-202: Error about mandatory questionnaire - Does not identify the questionnaire - End
%>
<head>
<title>JSP Page</title>
<html:base />
</head>
<body>

	<html:form action="/getSubmitForReview.do">
		<table width='100%' cellpadding='0' cellspacing='0' class='table'>

			<tr>
				<td class='tableheader'><bean:message
						key="protocolSubmission.protocolSubmission" /></td>
			</tr>

			<tr>
				<td>
					<table width='100%' cellpadding='0' cellspacing='0'
						class='tabtable'>
						<tr>
							<td height='20'></td>
						</tr>
						<tr>
							<td height='20' colspan='4' class='copybold'><font
								color='red'> <html:errors header="" footer="" />
							</font> <logic:messagesPresent message="true">
									<font color="red"> <html:messages id="message"
											message="true" property="noSubmissionRights">
											<li><bean:write name="message" /></li>
										</html:messages> <html:messages id="message" message="true"
											property="noLocation">
											<li><bean:write name="message" /></li>
										</html:messages> <html:messages id="message" message="true"
											property="noPrincipalInvestigator">
											<li><bean:write name="message" /></li>
										</html:messages> <html:messages id="message" message="true"
											property="noLeadUnit">
											<li><bean:write name="message" /></li>
										</html:messages> <html:messages id="message" message="true"
											property="noResearchArea">
											<li><bean:write name="message" /></li>
										</html:messages> <!-- Added for protocol custom elements validation - start - 1 -->
										<html:messages id="message" message="true"
											property="mandatoryCustomData">
											<li><bean:write name="message" /></li>
										</html:messages> <!-- Added for protocol custom elements validation - end - 1 -->
										<!-- 4272: Maintain History of Questionnaires- Start--> <html:messages
											id="message" message="true" property="mandatoryQuestionnaire">
											<li><bean:write name="message" /></li>
										</html:messages> <html:messages id="message" message="true"
											property="newQnrVersionAvailable">
											<li><bean:write name="message" /></li>
										</html:messages> <!-- 4272: Maintain History of Questionnaires- End -->
									</font>
								</logic:messagesPresent> <font color="red"> <% //COEUSDEV-202: Error about mandatory questionnaire - Does not identify the questionnaire - Start
               if(mandaroryQnrMessage != null && !"".equals(mandaroryQnrMessage)){%>
									<li><%=mandaroryQnrMessage%></li> <%}%> <%if(incompleteQnrMessage != null && !"".equals(incompleteQnrMessage)){%>
									<li><%=incompleteQnrMessage%></li> <%}%> <%if(newVersionQnrMessage != null && !"".equals(newVersionQnrMessage)){%>
									<li><%=newVersionQnrMessage%></li> <%} // COEUSDEV-202: Error about mandatory questionnaire - Does not identify the questionnaire - End%>
							</font></td>
						</tr>

						<tr>
							<td height='40'></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td height='10'></td>
			</tr>
		</table>
	</html:form>
</body>
</html:html>
