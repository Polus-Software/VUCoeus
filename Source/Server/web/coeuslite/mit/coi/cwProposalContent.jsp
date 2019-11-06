<%@ page import="org.apache.struts.validator.DynaValidatorForm"%>

<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@page errorPage="ErrorPage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>

<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="ProposalDevelopmentDet" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="BudgetForProposalDet" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="ProposalInvestigators" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="NarrativeInfo" scope="session" class="java.util.Vector" />
<jsp:useBean id="SponsorMaintenanceData" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="unitDescription" scope="session"
	class="java.lang.String" />

<jsp:useBean id="person" scope="session"
	class="edu.mit.coeus.bean.PersonInfoBean" />
<bean:size id="NarrativeInfoSize" name="NarrativeInfo" />

<html:html>
<head>
<title>Coeus Web</title>
<html:base />
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<script language='javascript'>
        function open_window(link)
        {
        var winleft = (screen.width - 650) / 2;
        var winUp = (screen.height - 450) / 2;  
        var win = "scrollbars=1,resizable=1,width=790,height=450,left="+winleft+",top="+winUp
        sList = window.open('<bean:write name='ctxtPath'/>'+"/"+link, "list", win);
               
        }

        </script>
</head>
<body>

	<table width="980" height="100%" border="0" cellpadding="0"
		cellspacing="0" class="table">

		<logic:present name="ProposalDevelopmentDet" scope="request">
			<logic:iterate id="data" name="ProposalDevelopmentDet"
				type="org.apache.commons.beanutils.DynaBean">
				<tr>
					<td height="20" align="left" valign="top" class="theader"><bean:message
							bundle="coi" key="proposalContent.proposal" /> <bean:write
							name="data" property="proposalNumber" /></td>
				</tr>

				<!-- Proposal Summary - Start  -->
				<tr>
					<td height="119" align="left" valign="top"><table width="99%"
							border="0" align="center" cellpadding="0" cellspacing="0"
							class="tabtable">
							<tr>
								<td colspan="4" align="left" valign="top"><table
										width="100%" height="20" border="0" cellpadding="0"
										cellspacing="0" class="tableheader">
										<tr>
											<td><bean:message bundle="coi"
													key="proposalContent.proposalSummary" /></td>
										</tr>
									</table></td>
							</tr>
							<tr>
								<td width="70%" valign="top" class="copy" align="left">
									<table width="100%" border="0" cellpadding="2">

										<tr>
											<td nowrap class="copy" width="10%" align="left"><bean:message
													bundle="coi" key="label.title" /></td>
											<td width="6">
												<div align="left">:</div>
											</td>
											<td class="copy" colspan="2">&nbsp;&nbsp; <b> <coeusUtils:formatOutput
														name="data" property="title" />
											</b>
											</td>
										</tr>
										<tr>
											<td nowrap class="copy" width="10%" align="left"><bean:message
													bundle="coi" key="label.proposalNumber" /></td>
											<td width="6">
												<div align="left">:</div>
											</td>
											<td class="copy" colspan="2">&nbsp;&nbsp; <b> <coeusUtils:formatOutput
														name="data" property="proposalNumber" />
											</b>
											</td>
										</tr>
										<tr>
											<td nowrap class="copy" width="10%" align="left"><bean:message
													bundle="coi" key="label.sponsor" /></td>
											<td width="6">
												<div align="left">:</div>
											</td>
											<td class="copybold" colspan="2">&nbsp;&nbsp; <logic:present
													name="data" property="sponsorCode">


													<bean:write name="data" property="sponsorCode" />
													<logic:present name="SponsorMaintenanceData"
														scope="request">
														<logic:iterate id="sponsordata"
															name="SponsorMaintenanceData"
															type="org.apache.commons.beanutils.DynaBean"> 
                                            :   <bean:write
																name="sponsordata" property="sponsorName" />
														</logic:iterate>
													</logic:present>
												</logic:present>
											</td>
										</tr>
										<tr>
											<td nowrap class="copy" width="10%" align="left"><bean:message
													bundle="coi" key="label.principalInvestigator" /></td>
											<td width="6">
												<div align="left">:</div>
											</td>
											<td class="copy" colspan="2">&nbsp;&nbsp; <b> <logic:present
														name="ProposalInvestigators" scope="request">
														<logic:iterate id="piData" name="ProposalInvestigators"
															type="org.apache.commons.beanutils.DynaBean">
															<logic:equal name="piData" property="principalInvFlag"
																value="Y">
																<coeusUtils:formatOutput name="piData"
																	property="personName" /></b> </logic:equal> </logic:iterate> </logic:present>
											</td>
										</tr>
										<tr>
											<td nowrap class="copy" width="10%" align="left"><bean:message
													bundle="coi" key="label.unit" /></td>
											<td width="6">
												<div align="left">:</div>
											</td>
											<td class="copybold" colspan="2">&nbsp;&nbsp; <coeusUtils:formatOutput
													name="data" property="ownedUnit" /> <logic:present
													name="unitDescription" scope="request">                                            
                                    :  <%=request.getAttribute("unitDescription") %>

												</logic:present>
											</td>
										</tr>
										<tr>
											<td nowrap class="copy" width="10%" align="left"><bean:message
													bundle="coi" key="label.status" /></td>
											<td width="6">
												<div align="left">:</div>
											</td>
											<td class="copy" colspan="2">&nbsp;&nbsp; <b> <logic:present
														name="data" property="creationStatusDesc">
														<coeusUtils:formatOutput name="data"
															property="creationStatusDesc" /></b> </logic:present>
											</td>
										</tr>
										<tr>
											<td nowrap class="copy" width="10%" align="left"><bean:message
													bundle="coi" key="label.deadLine" /></td>
											<td width="6">
												<div align="left">:</div>
											</td>
											<td class="copy" colspan="2">&nbsp;&nbsp; <b> <logic:present
														name="data" property="deadLineDate">
														<coeusUtils:formatDate name="data" property="deadLineDate" /></b>
												</logic:present>
											</td>
										</tr>
										<tr>
											<td nowrap class="copy" width="10%" align="left"><bean:message
													bundle="coi" key="label.startDate" /></td>
											<td width="6">
												<div align="left">:</div>
											</td>
											<td class="copy" colspan="2">&nbsp;&nbsp; <b> <logic:present
														name="data" property="reqStartDateInitial">
														<coeusUtils:formatDate name="data"
															property="reqStartDateInitial" /></b> </logic:present>
											</td>
										</tr>
										<tr>
											<td nowrap class="copy" width="10%" align="left"><bean:message
													bundle="coi" key="label.endDate" /></td>
											<td width="6">
												<div align="left">:</div>
											</td>
											<td class="copy" colspan="2">&nbsp;&nbsp; <b> <logic:present
														name="data" property="reqEndDateInitial">
														<coeusUtils:formatDate name="data"
															property="reqEndDateInitial" /></b> </logic:present>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							</logic:iterate>
							</logic:present>
							<tr>
								<td>&nbsp;</td>
							</tr>

						</table></td>
				</tr>

				<!--  Proposal Summary -End -->
				<tr>
					<td height='10'>&nbsp;</td>
				</tr>
				<!-- Budget Summary - Start -->
				<logic:present name="BudgetForProposalDet" scope="request">
					<tr>
						<td align="left" valign="top"><table width="99%" border="0"
								align="center" cellpadding="0" cellspacing="0" class="tabtable">
								<tr>
									<td colspan="4" align="left" valign="top"><table
											width="100%" height="20" border="0" cellpadding="0"
											cellspacing="0" class="tableheader">
											<tr>
												<td><bean:message bundle="coi"
														key="proposalContent.budgetSummary" /></td>
											</tr>
										</table></td>
								</tr>

								<logic:present name="budgetExists">
									<logic:equal name="budgetExists" value="false">
										<tr>
											<td class='copy'><bean:message bundle="coi"
													key="proposalContent.noBudgetInfo" /></td>
										</tr>
										<tr>
											<td>&nbsp;</td>
										</tr>
									</logic:equal>
									<logic:equal name="budgetExists" value="true">
										<tr>

											<td width="70%" valign="top" class="copy" align="left">
												<table width="100%" border="0" cellpadding="2">
													<logic:iterate id="budgetData" name="BudgetForProposalDet"
														type="org.apache.commons.beanutils.DynaBean">
														<tr>
															<td nowrap class="copy" width="10%" align="left"><bean:message
																	bundle="coi" key="label.totalDirectCost" /></td>
															<td width="6">
																<div align="left">:</div>
															</td>

															<td class="copybold" colspan="2">&nbsp;&nbsp; <logic:present
																	name="budgetData" property="totalDirectCost">
																	<coeusUtils:formatString name="budgetData"
																		property="totalDirectCost" formatType="currencyFormat" />
																</logic:present>
															</td>
														</tr>
														<tr>
															<td nowrap class="copy" width="10%" align="left"><bean:message
																	bundle="coi" key="label.totalInDirectCost" /></td>
															<td width="6">
																<div align="left">:</div>
															</td>
															<td class="copybold" colspan="2">&nbsp;&nbsp; <logic:present
																	name="budgetData" property="totalIndirectCost">
																	<coeusUtils:formatString name="budgetData"
																		property="totalIndirectCost"
																		formatType="currencyFormat" />
																</logic:present>
															</td>
														</tr>
														<tr>
															<td nowrap class="copy" width="10%" align="left"><bean:message
																	bundle="coi" key="label.totalCost" /></td>
															<td width="6">
																<div align="left">:</div>
															</td>
															<td class="copybold" colspan="2">&nbsp;&nbsp; <logic:present
																	name="budgetData" property="totalCost">
																	<coeusUtils:formatString name="budgetData"
																		property="totalCost" formatType="currencyFormat" />
																</logic:present>
															</td>
														</tr>
														<tr>
															<td nowrap class="copy" width="10%" align="left"><bean:message
																	bundle="coi" key="label.costSharing" /></td>
															<td width="6">
																<div align="left">:</div>
															</td>
															<td class="copybold" colspan="2">&nbsp;&nbsp; <logic:present
																	name="budgetData" property="costSharingAmount">
																	<coeusUtils:formatString name="budgetData"
																		property="costSharingAmount"
																		formatType="currencyFormat" />
																</logic:present>
															</td>
														</tr>

														<tr>
															<td nowrap class="copy" width="10%" align="left"><bean:message
																	bundle="coi" key="label.underRecAmount" /></td>
															<td width="6">
																<div align="left">:</div>
															</td>
															<td class="copybold" colspan="2">&nbsp;&nbsp; <logic:present
																	name="budgetData" property="underrecoveryAmount">
																	<coeusUtils:formatString name="budgetData"
																		property="underrecoveryAmount"
																		formatType="currencyFormat" />
																</logic:present>
															</td>
														</tr>

														<tr>
															<td nowrap class="copy" width="10%" align="left"><bean:message
																	bundle="coi" key="label.residualFunds" /></td>
															<td width="6">
																<div align="left">:</div>
															</td>
															<td class="copybold" colspan="2">&nbsp;&nbsp; <logic:present
																	name="budgetData" property="residualFunds">
																	<coeusUtils:formatString name="budgetData"
																		property="residualFunds" formatType="currencyFormat" />
																</logic:present>
															</td>
														</tr>
													</logic:iterate>

													<tr>
														<td class="copy"><a
															href="javascript:open_window('/viewBudgetSummary.do?proposalNumber=<bean:write name="data" property="proposalNumber"/>&versionNumber=<bean:write name="budgetData" property="versionNumber"/>','BudgetSummary');"><u>
																	<bean:message bundle="coi"
																		key="label.viewBudgetSummary" />
																	<%--View Budget Summary--%>
															</u></a></td>
													</tr>

												</table>
											</td>
										</tr>
									</logic:equal>
								</logic:present>
								</logic:present>

								<tr>
									<td>&nbsp;</td>
								</tr>

							</table></td>
					</tr>
					<!-- Budget Summary -End -->

					<!--Narratives   -Start -->
					<logic:present name="NarrativeInfo" scope="request">
						<logic:notEqual name="NarrativeInfoSize" value="0">
							<tr>
								<td align="left" valign="top"><br>
									<table width="99%" border="0" align="center" cellpadding="0"
										cellspacing="0" class="tabtable">
										<tr>
											<td align="left" valign="top"><table width="100%"
													height="20" border="0" cellpadding="0" cellspacing="0"
													class="tableheader">
													<tr>
														<td><bean:message bundle="coi"
																key="proposalContent.narratives" /></td>
													</tr>
												</table></td>
										</tr>

										<tr align="center">
											<td colspan="0"><br>
												<DIV
													STYLE="overflow: auto; width: 900px; height: 125; padding: 0px; margin: 0px">

													<table width="95%" border="0" cellpadding="3"
														class="tabtable">
														<tr>
															<td width="40%" align="left" class="theader"><bean:message
																	bundle="coi" key="proposalContent.moduleName" /></td>
															<td width="30%" align="left" class="theader"><bean:message
																	bundle="coi" key="proposalContent.moduleStatus" /></td>
															<td width="20%" align="left" class="theader">
																&nbsp;&nbsp;</td>

														</tr>
														<% int count =0; 
                                                           int index =0;
                                                           String strBgColor = "#DCE5F1";                    %>
														<logic:iterate id="narrativeData" name="NarrativeInfo"
															type="org.apache.commons.beanutils.DynaBean">
															<%
                                                            Integer countInt = new Integer(count);
                                                            String countString = countInt.toString();
                                                         %>

															<%                                  
                                                       if (index%2 == 0) {
                                                            strBgColor = "#D6DCE5"; 
                                                        }
                                                       else { 
                                                            strBgColor="#DCE5F1"; 
                                                         }
                                                    %>

															<tr bgcolor='<%=strBgColor%>'>
																<td width="40%" class='copy'><coeusUtils:formatOutput
																		name="narrativeData" property="moduleTitle" /></td>
																<td width="30%" class='copy'><coeusUtils:formatOutput
																		name="narrativeData" property="moduleStatus" /></td>
																<logic:equal name="narrativeData" property='viewRight'
																	value="1">
																	<td class="copy"><logic:present
																			name="narrativeData" property="pdfModuleNumber">
																			<a
																				href="<bean:write name='ctxtPath'/>/GetNarrativeDocument.do?proposalNumber=<bean:write name="narrativeData" property="proposalNumber" />&moduleNumber=<bean:write name="narrativeData" property="pdfModuleNumber" />&documentType=PDF">
																				<u>View PDF</u>
																			</a>
																		</logic:present> <logic:present name="narrativeData"
																			property="sourceModuleNumber">
																			<a
																				href="<bean:write name='ctxtPath'/>/GetNarrativeDocument.do?proposalNumber=<bean:write name="narrativeData" property="proposalNumber" />&moduleNumber=<bean:write name="narrativeData" property="pdfModuleNumber" />&documentType=WORD">
																				<u>View Word</u>
																			</a>
																		</logic:present></td>
																</logic:equal>
																<logic:equal name="narrativeData" property='viewRight'
																	value="0">
																	<td>&nbsp;&nbsp;</td>
																</logic:equal>
															</tr>
															<% index++; %>
														</logic:iterate>
													</table>
												</DIV></td>
										</tr>
										</logic:notEqual>
										</logic:present>
									</table></td>
							</tr>
							<tr>
								<td height='10'>&nbsp;</td>
							</tr>
	</table>
	</td>
	</tr>
	<!--Narratives -End -->

	</table>

</body>
</html:html>

