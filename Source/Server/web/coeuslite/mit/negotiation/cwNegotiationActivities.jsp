<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@page import="edu.mit.coeus.negotiation.bean.*"%>
<html:form action="/getActivitiesForNegotiation.do" method="post">
	<head>
<title>Negotiation Activities</title>
	</head>
	<%String negotiationNumber = (String)session.getAttribute("negotiationNumber");%>
	<body topmargin="0" leftmargin="0">
		<table width="100%" border="0" align="center" cellpadding="2"
			cellspacing="0" class="tabtable">
			<tr>
				<td align="left" valign="top">
					<table width="100%" border="0" cellpadding="0" cellspacing="0"
						class="tableheader">
						<tr>
							<td><bean:message bundle="negotiation"
									key="negotiationActivities.heading" /></td>
							<td align="right">
								<%String printAllLink = request.getContextPath()+"/printActivity.do?NEGOTIATION_NUMBER="+negotiationNumber+"&PRINT_TYPE=printAll";%>
								<a href="<%=printAllLink%>" target='_blank'> <u><bean:message
											bundle="negotiation" key="negotiationActivities.printAll" /></u>
							</a>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<logic:notPresent name="negotiationActivities">
				<tr align="center">
					<td class="copybold" align="left" colspan="4"><bean:message
							bundle="negotiation" key="negotiationActivities.notActivities" />
					</td>
				</tr>
			</logic:notPresent>

			<logic:present name="negotiationActivities">
				<%NegotiationActivitiesBean negotiationActivitiesBean = new NegotiationActivitiesBean();%>
				<logic:iterate id="dynaForm" name="negotiationActivities"
					type="edu.mit.coeus.negotiation.bean.NegotiationActivitiesBean"
					indexId="index" scope="request">
					<tr align="center">
						<td>
							<table width="100%" align="left" border="0" class="tabtable"
								cellpadding="3" cellspacing="0">

								<tr>
									<td width="10%" nowrap class='copybold' align="left"><bean:message
											bundle="negotiation" key="negotiationActivities.activityType" />&nbsp;</td>
									<td width="20%" class='copy' nowrap><bean:write
											name="dynaForm" property="activityTypeDescription" /></td>
									<td width="15%" nowrap class='copybold'><bean:message
											bundle="negotiation" key="negotiationActivities.activityDate" />&nbsp;</td>
									<td width="20%" class='copy' nowrap><coeusUtils:formatDate
											name="dynaForm" formatString="MM/dd/yyyy"
											property="activityDate" /></td>
									<td width="15%" nowrap class='copybold'><bean:message
											bundle="negotiation" key="negotiationActivities.createdDate" />&nbsp;</td>
									<td width="20%" class='copy' nowrap><coeusUtils:formatDate
											name="dynaForm" formatString="MM/dd/yyyy"
											property="createDate" /></td>
								</tr>
								<tr>
									<td width="10%" nowrap class='copybold' align="left"><bean:message
											bundle="negotiation" key="negotiationActivities.followupDate" />&nbsp;</td>
									<td width="20%" class='copy' nowrap><coeusUtils:formatDate
											name="dynaForm" formatString="MM/dd/yyyy"
											property="followUpDate" /></td>
									<td width="15%" nowrap class='copybold'><bean:message
											bundle="negotiation" key="negotiationActivities.lastUpdate" />&nbsp;</td>
									<td width="20%" class='copy' nowrap><coeusUtils:formatDate
											name="dynaForm" formatString="MM/dd/yyyy"
											property="updateTimestamp" /></td>
									<td width="15%" nowrap class='copybold'><bean:message
											bundle="negotiation" key="negotiationActivities.lastUpdateBy" />&nbsp;</td>
									<td width="20%" class='copy' nowrap><bean:write
											name="dynaForm" property="lastUpdatedBy" /></td>
								</tr>
								<tr>

								</tr>
								<tr>
									<td width="15%" nowrap class='copybold' align="left"><bean:message
											bundle="negotiation" key="negotiationActivities.description" />&nbsp;</td>

									<td width="30%" class='copy' colspan="5"><bean:write
											name="dynaForm" property="description" /></td>

								</tr>
								<tr>
									<td width="15%" nowrap class='copybold' align="left"><bean:message
											bundle="negotiation" key="negotiationActivities.ospOnly" />&nbsp;</td>
									<td width="30%" class='copy' nowrap><bean:define
											id="chekOSP" name="dynaForm" property="restrictedView" /> <%Boolean checkOSPValue = (Boolean)chekOSP;
                                                                         if(checkOSPValue.booleanValue()){%>
										<img
										src="<bean:write name='ctxtPath'/>/coeusliteimages/checked.gif">
										<%}else{%> <img
										src="<bean:write name='ctxtPath'/>/coeusliteimages/checkbox.gif">
										<%}%></td>
									<td width="10%" nowrap class='copybold' align=right></td>
									<td width="40%" class='copy' align=right nowrap colspan="4">
										<bean:define id="attachmentPresentId" name="dynaForm"
											property="attachmentPresent" /> <%
                                        negotiationActivitiesBean = (NegotiationActivitiesBean)dynaForm;
                                        int activityNumber = new Integer(negotiationActivitiesBean.getActivityNumber()).intValue();
                                        String negotiationNumberForActivity = negotiationActivitiesBean.getNegotiationNumber();
                                        Boolean attachmentPresentValue = (Boolean)attachmentPresentId;
                                        if(attachmentPresentValue.booleanValue()){
                                        String viewAttachmentPath = request.getContextPath()+"/viewAttachmentForActivity.do?NEGOTIATION_NUMBER="+negotiationNumberForActivity+"&ACTIVITY_NUMBER="+activityNumber;                                       
                                        %> <a
										href="<%=viewAttachmentPath%>" target='_blank'> <u>

												View Attachment </u>
									</a>&nbsp;&nbsp;|&nbsp;&nbsp; <%}%> <% String printActivityLink = request.getContextPath()+"/printActivity.do?NEGOTIATION_NUMBER="+negotiationNumberForActivity+"&ACTIVITY_NUMBER="+activityNumber+"&PRINT_TYPE=printOne";%>
										<a href="<%=printActivityLink%>" target='_blank'><u>Print</u></a>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</logic:iterate>
			</logic:present>
		</table>
	</body>
</html:form>