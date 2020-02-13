<%@ page import="org.apache.struts.validator.DynaValidatorForm"%>

<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@page errorPage="ErrorPage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@page
	import="java.sql.Date, edu.mit.coeuslite.irb.bean.ProtocolHeaderDetailsBean, java.text.SimpleDateFormat"%>
<jsp:useBean id="protocolSubmissionHeader" scope="session"
	class="edu.mit.coeuslite.irb.bean.ProtocolHeaderDetailsBean" />

<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="protocolSubDetails" scope="request"
	class="java.util.Vector" />


<html:html>
<head>
<link rel=stylesheet
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/ipf.css"
	type="text/css">
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<script language="javascript" type="text/JavaScript"
	src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/sorttable.js"></script>
<title>Protocol Submission Details</title>


</head>

<body>


	<html:form action="/getSubmissionDetails.do">
		<a name="top"></a>
		<%--  ************  START OF BODY TABLE  ************************--%>
		<table width="750" border="0" cellpadding="3" cellspacing="0"
			class="table">

			<tr>
				<td height="50" align="left" valign="top"><table width="99%"
						border="0" align="center" cellpadding="0" cellspacing="0"
						class="tabtable">
						<tr>
							<td colspan="4" align="left" valign="top"><table
									width="100%" height="20" border="0" cellpadding="0"
									cellspacing="0" class="tableheader">
									<tr>
										<td>Protocol Submission Details</td>
									</tr>
								</table></td>
						</tr>
						<tr>
							<%try{%>
							<%
                ProtocolHeaderDetailsBean headerBean=(ProtocolHeaderDetailsBean)session.getAttribute("protocolSubmissionHeader");
                 String lineImage = request.getContextPath()+"/coeusliteimages/line4.gif";
                if(headerBean!=null){
                 String EMPTYSTRING ="";
                String updateTimestamp = headerBean.getUpdateTimeStamp();
                String updateUser =headerBean.getUpdateUser();
                String createTimestamp =headerBean.getCreateTimestamp();
                String createUser = headerBean.getCreateUser();
                String title = headerBean.getTitle().trim();
                if(title!=null && (!title.equals(EMPTYSTRING))){
                    title=title.length()>60?title.substring(0,61)+"...":title;
                }else{title=EMPTYSTRING;}
                String leadUnit=headerBean.getLeadUnit();
                if(leadUnit!=null && (!leadUnit.equals(EMPTYSTRING))){
                    leadUnit=leadUnit.length()>60?leadUnit.substring(0,61)+"...":leadUnit;
                }else{leadUnit=EMPTYSTRING;}
                Date ApprovalDate=headerBean.getApprovalDate();
                Date ExpiryDate = headerBean.getExpirationDate();

                String strApprovalDate=EMPTYSTRING;
                String strExpiryDate =EMPTYSTRING;    

                if(ApprovalDate!=null){
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                        strApprovalDate = dateFormat.format(ApprovalDate);
                }
                if(ExpiryDate!=null){
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                        strExpiryDate = dateFormat.format(ExpiryDate);
                }

                //Added for CoeusLite4.3 header changes enhacement - Start
                Date LastApprovalDate = headerBean.getLastApprovalDate();
                String strLastApprovalDate =EMPTYSTRING;
                if(LastApprovalDate!=null){
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                    strLastApprovalDate = dateFormat.format(LastApprovalDate);
                }
                //Added for CoeusLite4.3 header changes enhacement - end

                String status=headerBean.getProtocolStatusDescription();
                if(status!=null && (!status.equals(EMPTYSTRING))){
                     status=(status.length()>60)?status.substring(0,61)+"...":status;
                     }else{status=EMPTYSTRING;}
                    //added for user Name id
                    String updateUserName = headerBean.getUpdateUserName();
                    String createUserName = headerBean.getCreateUserName();
                        %>
							<td height='10'>
								<table width='100%' height='100%' border='0' cellpadding='1'
									cellspacing='0' class='table'>
									<tr>
										<td nowrap class='copybold'>&nbsp;&nbsp;&nbsp;<bean:message
												key="protocolHeader.ProtocolNum" />:
										</td>

										<td class='copy' nowrap><%=headerBean.getProtocolNumber()%>
											<b>(<%=status%>)
										</b></td>

										<td nowrap class='copybold'>&nbsp;&nbsp;&nbsp;<bean:message
												key="protocolHeader.ExpirationDate" />:
										</td>
										<%if(!strExpiryDate.equals(EMPTYSTRING)){%>
										<td class='copy' nowrap>&nbsp;&nbsp;&nbsp;<%=strExpiryDate%>
										</td>
										<%}else{%>
										<td class='copy' nowrap></td>
										<%}%>
									</tr>

									<tr>
										<%if(headerBean.getPrincipalInvestigator()!=null && !headerBean.getPrincipalInvestigator().equals(EMPTYSTRING)){%>
										<td class='copybold' nowrap>&nbsp;&nbsp;&nbsp;<bean:message
												key="protocolHeader.Investigator" />:
										</td>
										<td class='copy' nowrap><%=headerBean.getPrincipalInvestigator()%>
										</td>
										<%}else{%>
										<td class='copybold' nowrap>&nbsp;&nbsp;&nbsp;<bean:message
												key="protocolHeader.Investigator" />:
										</td>
										<td class='copy' nowrap></td>
										<%}%>
										<td align="left" class='copybold' nowrap>
											&nbsp;&nbsp;&nbsp;<bean:message
												key="protocolHeader.LastAppDate" />:
										</td>
										<%if(!strLastApprovalDate.equals(EMPTYSTRING)){%>
										<td class='copy' nowrap>&nbsp;&nbsp;&nbsp;<%=strLastApprovalDate%>
										</td>
										<%}else{%>
										<td class='copy' nowrap></td>
										<%}%>
									</tr>
									<tr>
										<td class='copybold' nowrap>&nbsp;&nbsp;&nbsp;<bean:message
												key="irbGeneralInfoForm.title" />:
										</td>
										<td colspan="3" class='copy' nowrap><%=title%></td>
									</tr>


								</table> <%}%> <% } catch(Exception e){e.printStackTrace(); } %>
							</td>
						</tr>

						<tr align="center">
							<td>
								<%boolean isEntered = false;%> <logic:notEmpty
									name="protocolSubDetails">
									<logic:iterate id="submissionList" name="protocolSubDetails"
										type="org.apache.struts.action.DynaActionForm">

										<table width="100%" height="100%" border="0" cellpadding="1"
											cellspacing="3">

											<tr>
												<td>
													<table width="100%" height="100%" border="0"
														cellpadding="2" cellspacing="3" class="tabtable">
														<tr class="copy">
															<td><b><bean:message
																		key="protocolActions.SubmissionType" /> :</b> &nbsp;<bean:write
																	name="submissionList" property="submissionDescription" />
															</td>
															<td colspan="2"><b><bean:message
																		key="protocolActions.ReviewType" /> :</b>&nbsp; <bean:write
																	name="submissionList" property="reviewDescription" /></td>
														</tr>
														<tr class="copy">
															<td colspan="4"><b><bean:message
																		key="protocolActions.TypeQualifier" /> :</b>&nbsp;<bean:write
																	name="submissionList" property="qualsDescription" /></td>
														</tr>
														<tr class="copy">
															<td colspan="3"><b><bean:message
																		key="protocolActions.SubmissionStatus" /> : </b>&nbsp;<bean:write
																	name="submissionList"
																	property="submissionStatusDescription" /></td>
														</tr>
														<tr class="copy">
															<td><b><bean:message key="protocolActions.Date" />
																	:</b>&nbsp;<coeusUtils:formatDate name="submissionList"
																	property="submissionDate" /></td>
															<td colspan="2"><b> <bean:message
																		key="protocolActions.Place" /> :
															</b> <bean:write name="submissionList" property="place" /></td>
														</tr>
														<tr class="copy">
															<td><b><bean:message
																		key="protocolActions.CommitteeId" /> : </b>&nbsp;<bean:write
																	name="submissionList" property="committeeId" /></td>
															<td><b><bean:message
																		key="protocolActions.ScheduleId" /> :</b>&nbsp;<bean:write
																	name="submissionList" property="scheduleId" /></td>
															<td><b><bean:message
																		key="protocolActions.ScheduleDate" /> :</b>&nbsp;<coeusUtils:formatDate
																	name="submissionList" property="scheduleDate" /></td>
														</tr>
														<tr class="copy">
															<td colspan="3"><b><bean:message
																		key="protocolActions.CommitteeName" /> :</b>
															<bean:write name="submissionList"
																	property="committeeName" /></td>
														</tr>
													</table>
												</td>
											</tr>
										</table>

									</logic:iterate>
								</logic:notEmpty> <logic:empty name="protocolSubDetails">
									<bean:message key="protocolActions.NoDetails" />
								</logic:empty>
							</td>
						</tr>
						<tr>
							<td height='10'>&nbsp;</td>
						</tr>

					</table></td>
			</tr>
			<tr>
				<td height='45' align="left">&nbsp; &nbsp; <html:link
						href="javascript:window.close();" styleClass="copybold">
						<bean:message key="locking.close" />
					</html:link>

				</td>
			</tr>

		</table>
</body>

</html:form>
</html:html>