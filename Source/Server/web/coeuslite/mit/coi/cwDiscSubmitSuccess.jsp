<%@ page import="org.apache.struts.validator.DynaValidatorForm"%>

<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="dynaValidatorForm" scope="request"
	class="org.apache.struts.validator.DynaValidatorForm" />
<jsp:useBean id="acType" scope="session" class="java.lang.String" />
<jsp:useBean id="person" scope="session"
	class="edu.mit.coeus.bean.PersonInfoBean" />

<html:html>
<head>
<title>JSP Page</title>
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
</head>
<body>

	<table width="980" height="100%" border="0" cellpadding="0"
		cellspacing="0" class="table">
		<!-- COIDisclosure - Start  -->
		<tr>
			<td height="119" align="left" valign="top"><table width="99%"
					border="0" align="center" cellpadding="0" cellspacing="0"
					class="tabtable">
					<tr>
						<td colspan="4" align="left" valign="top"><table width="100%"
								height="20" border="0" cellpadding="0" cellspacing="0"
								class="theader">
								<tr>
									<td>&nbsp;<b> <bean:message bundle="coi"
												key="discSubmitSuccess.header" /> <logic:equal
												name="acType" scope="session" value="I">
												<bean:message bundle="coi"
													key="discSubmitSuccess.submission" />
											</logic:equal> <logic:equal name="acType" scope="session" value="U">
												<bean:message bundle="coi"
													key="discSubmitSuccess.modification" />
											</logic:equal> <%=person.getFullName()%>
									</b>

									</td>
								</tr>
							</table></td>
					</tr>
					<logic:equal name="requestedPage" scope="session"
						value="approvedisclosure">
						<tr>
							<td class='copy' width='90%' wrap align="center" colspan="3"><b>
									<bean:message key="discApproveSuccess.message2" bundle="coi" />
									<bean:write name="dynaValidatorForm" scope="request"
										property="sponsorName" /> <logic:equal
										name="dynaValidatorForm" scope="request"
										property="disclosureTypeCode" value="1">
										<bean:message bundle="coi" key="discSubmitSuccess.award" />
									</logic:equal> <logic:notEqual name="dynaValidatorForm" scope="request"
										property="disclosureTypeCode" value="1">
										<bean:message bundle="coi" key="discSubmitSuccess.proposal" />
									</logic:notEqual> <bean:message bundle="coi" key="discSubmitSuccess.message3" />
									<br> <i> <bean:write name="dynaValidatorForm"
											scope="request" property="title" /></i> <logic:equal
										name="acType" scope="session" value="I">
										<bean:message bundle="coi"
											key="discSubmitSuccess.message4Insert" />
									</logic:equal> <logic:equal name="acType" scope="session" value="U">
										<bean:message bundle="coi"
											key="discApproveSuccess.message4Update" />
									</logic:equal>
							</b></td>
						</tr>
					</logic:equal>
					<logic:notEqual name="requestedPage" scope="session"
						value="approvedisclosure">
						<tr>
							<td class='copy' width='90%' wrap align="center" colspan="3"><b>
									<bean:message key="discSubmitSuccess.message2" bundle="coi" />
									<bean:write name="dynaValidatorForm" scope="request"
										property="sponsorName" /> <logic:equal
										name="dynaValidatorForm" scope="request"
										property="disclosureTypeCode" value="1">
										<bean:message bundle="coi" key="discSubmitSuccess.award" />
									</logic:equal> <logic:notEqual name="dynaValidatorForm" scope="request"
										property="disclosureTypeCode" value="1">
										<bean:message bundle="coi" key="discSubmitSuccess.proposal" />
									</logic:notEqual> <bean:message bundle="coi" key="discSubmitSuccess.message3" />
									<br> <i> <bean:write name="dynaValidatorForm"
											scope="request" property="title" /></i> <logic:equal
										name="acType" scope="session" value="I">
										<bean:message bundle="coi"
											key="discSubmitSuccess.message4Insert" />
									</logic:equal> <logic:equal name="acType" scope="session" value="U">
										<bean:message bundle="coi"
											key="discSubmitSuccess.message4Update" />
									</logic:equal>
							</b></td>
						</tr>

					</logic:notEqual>
					<tr>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td class="copy">&nbsp;&nbsp; <logic:equal name="acType"
								scope="session" value="I">
								<input type="button"
									value="<bean:message bundle="coi" key="discSubmitSuccess.viewDisclISubmitted" />"
									onclick='javascript:window.location="<bean:write name='ctxtPath'/>/viewCOIDisclosureDetails.do?pageValue=display&disclosureNo=<bean:write name='dynaValidatorForm' property='coiDisclosureNumber'/>"'
									style="width: 250px">
							</logic:equal> <logic:equal name="acType" scope="session" value="U">
								<input type="button"
									value="<bean:message bundle="coi" key="discSubmitSuccess.viewDisclIModified" />"
									onclick='javascript:window.location="<bean:write name='ctxtPath'/>/viewCOIDisclosureDetails.do?pageValue=display&disclosureNo=<bean:write name='dynaValidatorForm' property='coiDisclosureNumber'/>"'
									style="width: 250px">
							</logic:equal></td>
					</tr>
					<tr>
						<td class="copy"><br>&nbsp;&nbsp; <logic:equal
								name="acType" scope="session" value="I">
								<input type="button"
									value="<bean:message bundle="coi" key="discSubmitSuccess.EditDisclosureISubmitted" />"
									onclick="javascript:window.location='<%=request.getContextPath()%>/editCoiDisc.do?disclosureNo=<bean:write name='dynaValidatorForm' property='coiDisclosureNumber'/>'"
									style="width: 250px">
							</logic:equal> <logic:equal name="acType" scope="session" value="U">
								<input type="button"
									value="<bean:message bundle="coi" key="discSubmitSuccess.EditDisclosureIModified" />"
									onclick="javascript:window.location='<%=request.getContextPath()%>/editCoiDisc.do?disclosureNo=<bean:write name='dynaValidatorForm' property='coiDisclosureNumber'/>'"
									style="width: 250px">
							</logic:equal></td>
					</tr>
					<tr>
						<td class="copy"><br>&nbsp;&nbsp; <logic:equal
								name="requestedPage" scope="session" value="approvedisclosure">
								<input type="button"
									value="<bean:message bundle="coi" key="discSubmitSuccess.finishedDiscl" />"
									onclick='javascript:window.location="<bean:write name='ctxtPath'/>/getApproveDiscl.do"'
									style="width: 250px">
							</logic:equal> <logic:notEqual name="requestedPage" scope="session"
								value="approvedisclosure">
								<input type="button"
									value="<bean:message bundle="coi" key="discSubmitSuccess.finishedDiscl" />"
									onclick='javascript:window.location="<bean:write name='ctxtPath'/>/coeuslite/mit/irb/tiles/cwCOITile.jsp"'
									style="width: 250px">
							</logic:notEqual></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
					</tr>
				</table></td>
		</tr>
		<!-- COIDisclosure - End  -->
		<tr>
			<td height='10'>&nbsp;</td>
		</tr>
	</table>
</body>
</html:html>
