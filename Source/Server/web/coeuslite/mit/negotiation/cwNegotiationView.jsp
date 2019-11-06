<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>

<html:form action="/viewNegotiation.do" method="post">
	<head>
<title>Negotiation Details</title>
	</head>
	<body topmargin="0" leftmargin="0">
		<table width="100%" border="0" align="center" cellpadding="2"
			cellspacing="0" class="tabtable">
			<tr>
				<td align="left" valign="top">
					<table width="100%" border="0" cellpadding="0" cellspacing="0"
						class="tableheader">
						<tr>
							<td><bean:message bundle="negotiation"
									key="negotiationView.heading" /></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr align="center">
				<td>
					<table width="100%" align="left" border="0" class="tabtable"
						cellpadding="3" cellspacing="0">
						<tr>
							<td width="15%" nowrap class='copybold'>&nbsp;&nbsp;&nbsp;<bean:message
									bundle="negotiation" key="negotiationView.negotiatior" />&nbsp;
							</td>
							<td width="30%" class='copy'><bean:write
									name="negotiationInfoForm" property="negotiatorName" /></td>
							<td width="10%" nowrap class='copybold' align=left><bean:message
									bundle="negotiation" key="negotiationView.negotiatiorStatus" />&nbsp;</td>
							<td width="40%" class='copy'><bean:write
									name="negotiationInfoForm" property="statusDescription" /></td>
						</tr>
						<tr>
							<td width="15%" nowrap class='copybold'>&nbsp;&nbsp;&nbsp;<bean:message
									bundle="negotiation" key="negotiationView.startDate" />&nbsp;
							</td>
							<td width="30%" class='copy'><coeusUtils:formatDate
									name="negotiationInfoForm" formatString="dd-MMM-yyyy"
									property="startDate" /></td>
							<td width="10%" nowrap class='copybold' align=left><bean:message
									bundle="negotiation" key="negotiationView.closedDate" />&nbsp;</td>
							<td width="40%" class='copy'><coeusUtils:formatDate
									name="negotiationInfoForm" formatString="dd-MMM-yyyy"
									property="closedDate" /></td>
						</tr>
						<tr>
							<td width="15%" nowrap class='copybold'>&nbsp;&nbsp;&nbsp;<bean:message
									bundle="negotiation" key="negotiationView.docFolder" />&nbsp;
							</td>
							<td width="30%" class='copy'><bean:write
									name="negotiationInfoForm" property="docFileAddress" /></td>
							<td width="10%" nowrap class='copybold' align=left></td>
							<td width="40%" class='copy'></td>
						</tr>
						<tr>
							<td width="15%" nowrap class='copybold'>&nbsp;&nbsp;&nbsp;<bean:message
									bundle="negotiation" key="negotiationView.agreementType" />&nbsp;
							</td>
							<td width="30%" class='copy'><bean:write
									name="negotiationInfoForm"
									property="negotiationAgreeTypeDescription" /></td>
							<td width="10%" nowrap class='copybold' align=left><bean:message
									bundle="negotiation" key="negotiationView.anticipatedAwardDate" />&nbsp;</td>
							<td width="40%" class='copy'><coeusUtils:formatDate
									name="negotiationInfoForm" formatString="dd-MMM-yyyy"
									property="proposedStartDate" /></td>
						</tr>
						<tr>
							<td width="15%" nowrap class='copybold'>&nbsp;&nbsp;&nbsp;<bean:message
									bundle="negotiation" key="negotiationView.proposalType" />&nbsp;
							</td>
							<td width="30%" class='copy'><bean:write
									name="negotiationInfoForm" property="proposalTypeDescription" /></td>
							<td width="10%" nowrap class='copybold' align=left><bean:message
									bundle="negotiation" key="negotiationView.primeSponsor" />&nbsp;</td>
							<td width="40%" class='copy'><logic:notEmpty
									name="negotiationInfoForm" property="primeSponsorCode">
									<bean:write name="negotiationInfoForm"
										property="primeSponsorCode" />:<bean:write
										name="negotiationInfoForm" property="primeSponsorName" />
								</logic:notEmpty></td>
						</tr>
						<tr>
							<td width="15%" nowrap class='copybold'>&nbsp;&nbsp;&nbsp;<bean:message
									bundle="negotiation" key="negotiationView.title" />&nbsp;
							</td>
							<td width="70%" class='copy' colspan="4"><html:textarea
									readonly="true" property="title" name="negotiationInfoForm"
									rows="2" cols="65" styleClass="cltextbox-nonEditcolor"
									style="width:500px"></html:textarea></td>
						</tr>
						<tr>
							<td width="15%" nowrap class='copybold'>&nbsp;&nbsp;&nbsp;<bean:message
									bundle="negotiation" key="negotiationView.contractAdmin" />&nbsp;
							</td>
							<td width="30%" class='copy'><bean:write
									name="negotiationInfoForm" property="initialContractAdmin" /></td>
							<td width="10%" nowrap class='copybold' align=left>&nbsp;</td>
							<td width="40%" class='copy'></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<table width="100%" border="0" align="center" cellpadding="2"
			cellspacing="0" class="tabtable">
			<tr>
				<td align="left" valign="top">
					<table width="100%" border="0" cellpadding="0" cellspacing="0"
						class="tableheader">
						<tr>
							<td><bean:message bundle="negotiation"
									key="negotiationView.location.heading" /></td>
						</tr>
					</table>
				</td>
			</tr>
			<logic:notPresent name="negotiationLocations">
				<tr align="center">
					<td class="copybold" align="left" colspan="4"><bean:message
							bundle="negotiation" key="negotiationView.notLocationDetails" />
					</td>
				</tr>
			</logic:notPresent>
			<tr align="center">
				<td><logic:present name="negotiationLocations">
						<logic:iterate id="lastLocation" name="negotiationLocations"
							type="org.apache.struts.validator.DynaValidatorForm"
							indexId="index" scope="request">
							<table width="100%" align="left" border="0" class="tabtable"
								cellpadding="3" cellspacing="0">
								<tr>
									<td width="15%" nowrap class='copybold'><bean:message
											bundle="negotiation" key="negotiationView.location.heading" />&nbsp;</td>
									<td width="30%" class='copy' nowrap><bean:write
											name="lastLocation" property="negotiationLocationTypeDes" /></td>
									<td width="10%" nowrap class='copybold' align=left><bean:message
											bundle="negotiation"
											key="negotiationView.location.receiptDate" />&nbsp;</td>
									<td width="40%" class='copy' nowrap><coeusUtils:formatDate
											name="lastLocation" formatString="dd-MMM-yyyy"
											property="effectiveDate" /></td>
									<td width="15%" nowrap class='copybold'><bean:message
											bundle="negotiation" key="negotiationView.location.noOfDays" />&nbsp;</td>
									<td width="30%" class='copy' nowrap><bean:write
											name="lastLocation" property="numberOfDays" /></td>
								</tr>
							</table>
						</logic:iterate>
					</logic:present></td>
			</tr>
			<tr>
				<td align="left" valign="top">
					<table width="100%" border="0" cellpadding="0" cellspacing="0"
						class="tableheader">
						<tr>
							<td><bean:message bundle="negotiation"
									key="negotiationView.locationHistory.heading" /></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr align="center">
				<td>
					<table width="100%" align="left" border="0" class="tabtable"
						cellpadding="3" cellspacing="0">
						<tr>
							<td>
								<table width="100%" align="center" border="0" cellpadding="3"
									cellspacing='0' class="tabtable">
									<tr>
										<%--<td width="10%" nowrap align="left" class="theader">Location Number</td>--%>
										<td width="20%" align="left" class="theader"><bean:message
												bundle="negotiation" key="negotiationView.location.heading" /></td>
										<td width="20%" align="left" class="theader"><bean:message
												bundle="negotiation"
												key="negotiationView.locationHistory.effectiveDate" /></td>
										<td width="10%" align="left" class="theader"><bean:message
												bundle="negotiation"
												key="negotiationView.locationHistory.noOfDays" /></td>
										<td width="20%" align="left" class="theader"><bean:message
												bundle="negotiation"
												key="negotiationView.locationHistory.updateTimeStamp" /></td>
										<td width="20%" nowrap align="left" class="theader"><bean:message
												bundle="negotiation"
												key="negotiationView.locationHistory.updateUser" /></td>
									</tr>
									<% int count = 0;
                                        %>
									<logic:notPresent name="negotiationLocationHistory">
										<tr align="center">
											<td class="copybold" align="left" colspan="4"><bean:message
													bundle="negotiation"
													key="negotiationView.notLocationHistoryDetails" /></td>
										</tr>
									</logic:notPresent>

									<logic:present name="negotiationLocationHistory">
										<logic:iterate id="locationHistory"
											name="negotiationLocationHistory"
											type="org.apache.struts.validator.DynaValidatorForm"
											indexId="index" scope="request">
											<tr valign="top" onmouseover="className='TableItemOn'"
												onmouseout="className='TableItemOff'">
												<%--<td width="20%" align="left" class="copy"><bean:write name="locationHistory" property="locationNumber"/></td>--%>
												<td width="20%" align="left" class="copy"><bean:write
														name="locationHistory"
														property="negotiationLocationTypeDes" /></td>
												<td width="20%" align="left" class="copy"><coeusUtils:formatDate
														name="locationHistory" formatString="dd-MMM-yyyy"
														property="effectiveDate" /></td>
												<td width="10%" align="left" class="copy"><bean:write
														name="locationHistory" property="numberOfDays" /></td>
												<td width="20%" nowrap align="left" class="copy"><coeusUtils:formatDate
														name="locationHistory" formatString="dd-MMM-yyyy hh:mm:ss"
														property="updateTimestamp" /></td>
												<td width="20%" nowrap align="left" valign="top"
													class="copy"><bean:write name="locationHistory"
														property="updateUser" /></td>
											</tr>
											<% count++;%>
										</logic:iterate>
									</logic:present>

								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</body>
</html:form>