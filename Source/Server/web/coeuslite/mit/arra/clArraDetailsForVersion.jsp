<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@ include
	file="/coeuslite/mit/arra/common/clArraReportHeaderDetails.jsp"%>
<jsp:useBean id="arraAwardSubcontractsForVersion" scope="request"
	class="java.util.Vector" />
<jsp:useBean id="arraAwardVendorsForVersion" scope="request"
	class="java.util.Vector" />
<jsp:useBean id="subcontractDynaBeansListForVersion" scope="request"
	class="java.util.HashMap" />
<%@page
	import="org.apache.struts.validator.DynaValidatorForm,java.lang.Boolean,java.sql.Date,edu.mit.coeuslite.arra.bean.ArraAwardHeaderBean,edu.mit.coeus.utils.CoeusFunctions,edu.mit.coeuslite.utils.CoeusLiteConstants"%>


<html>
<head>
<title>CoeusLite</title>
<link rel=stylesheet
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	type="text/css">
<style>
.cltextbox-color {
	font-weight: normal;
	width: 220px
}

.textbox {
	width: 40px;
}

.mbox {
	background-color: #eee;
	padding: 8px;
	border: 2px outset #666;
}
</style>
<script>
            function showHideDetails(val){
            if(val=='showAwdDetails') {
                document.getElementById('awardDetails').style.display = 'block';
                document.getElementById('hideAwdDetails').style.display = 'block';
                document.getElementById('showAwdDetails').style.display = 'none';
            } else if(val=='hideAwdDetails') {
                document.getElementById('awardDetails').style.display = 'none';
                document.getElementById('hideAwdDetails').style.display = 'none';
                document.getElementById('showAwdDetails').style.display = 'block';   
            }
            else if(val=='showVenDetails') {
                document.getElementById('vendorDetails').style.display = 'block';
                document.getElementById('hideVenDetails').style.display = 'block';
                document.getElementById('showVenDetails').style.display = 'none';
            } else if(val=='hideVenDetails') {
                document.getElementById('vendorDetails').style.display = 'none';
                document.getElementById('hideVenDetails').style.display = 'none';
                document.getElementById('showVenDetails').style.display = 'block';   
            }
            else if(val=='showSubcontracts') {
                document.getElementById('subcontracts').style.display = 'block';
                document.getElementById('hideSubcontracts').style.display = 'block';
                document.getElementById('showSubcontracts').style.display = 'none';
            } else if(val=='hideSubcontracts') {
                document.getElementById('subcontracts').style.display = 'none';
                document.getElementById('hideSubcontracts').style.display = 'none';
                document.getElementById('showSubcontracts').style.display = 'block';   
            }
            }
            
            function showHide(val,value){
            var panel = 'Panel'+value;
            var pan = 'pan'+value;
            var hidePanel  = 'hidePanel'+value;
            if(val == 1){
                        document.getElementById(panel).style.display = "none";
                        document.getElementById(hidePanel).style.display = "block";
                        document.getElementById(pan).style.display = "block";
                    }
            else if(val == 2){
                        document.getElementById(panel).style.display = "block";
                        document.getElementById(hidePanel).style.display = "none";
                        document.getElementById(pan).style.display = "none";
                    }        

            }
      
        </script>
<script language="javascript" type="text/JavaScript"
	src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/coeusLiteCalendar.js"></script>
<script language="javascript" type="text/JavaScript"
	src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/Balloon.js"></script>
</head>
<body>
	<%String plus = request.getContextPath()+"/coeusliteimages/plus.gif";
        String minus = request.getContextPath()+"/coeusliteimages/minus.gif";%>
	<html:form action="/getArraDetailsForVersion.do" method="post">
		<table width="100%" class="tabtable" border="0" cellpadding="0"
			cellspacing="0">


			<tr>
				<td align="left" valign="top" colspan="2">
					<table width="100%" border="0" cellpadding="0" cellspacing="0"
						class="tableheader">
						<tr>
							<td class="tableheader" height='20'>
								<div id='showAwdDetails' style='display: none;'>
									<html:link href="javaScript:showHideDetails('showAwdDetails');">
										<html:img src="<%=plus%>" border="0" />
									</html:link>
									&nbsp;
									<bean:message bundle="arra" key="awardDetails.heading" />

								</div>
								<div id='hideAwdDetails'>
									<html:link href="javaScript:showHideDetails('hideAwdDetails');">
										<html:img src="<%=minus%>" border="0" />
									</html:link>
									&nbsp;
									<bean:message bundle="arra" key="awardDetails.heading" />
								</div>
							</td>

						</tr>
					</table>
				</td>

			</tr>
			<!--Display read only fields-->
			<tr>
				<td></td>
			</tr>
			<tr valign="top">
				<td align="right" colspan='4' bgcolor="#D1E5FF">
					<div id='awardDetails' style='display: block;'>
						<table width="98%" border="0" align="left" cellpadding="4"
							cellspacing="0">
							<tr>
								<td align="right" valign="top" colspan='4' bgcolor="#D1E5FF">
									<table width="98%" border="0" align="left" cellpadding="4"
										class="tabtable" cellspacing="0">
										<tr>
											<td width="15%" class='copybold' align="left"><bean:message
													bundle="arra" key="awardDetails.awardtype" />&nbsp;</td>
											<td width="30%" class='copy' align=left><bean:write
													name="arraAwardPastReport" property="awardType" /></td>
											<td width="10%" align="left" class='copybold'><bean:message
													bundle="arra" key="awardDetails.awardingAgencyCode" />&nbsp;</td>
											<td width="40%" class='copy' align=left><bean:write
													name="arraAwardPastReport" property="awardingAgencyCode" /></td>
										</tr>
										<tr>
											<td width="15%" class='copybold' align="left"><bean:message
													bundle="arra" key="awardDetails.awardAmount" />&nbsp;</td>
											<td width="30%" class='copy' align=left><logic:notEmpty
													name="arraAwardPastReport" property="awardAmount">
													<coeusUtils:formatString name="arraAwardPastReport"
														property="awardAmount" formatType="currencyFormat" />
												</logic:notEmpty></td>
											<td width="10%" align="left" class='copybold'><bean:message
													bundle="arra" key="awardDetails.fundingAgencyCode" />&nbsp;</td>
											<td width="40%" class='copy' align=left><bean:write
													name="arraAwardPastReport" property="fundingAgencyCode" /></td>

											<%-- <td  width="10%"  class='copybold' align="left"><bean:message bundle="arra" key="awardDetails.CFDANumber"/>&nbsp;</td>
                                                <td width="40%" class='copy'    align=left>
                                                    <bean:write name="arraAwardPastReport" property = "cfdaNumber"/>
                                                </td>      --%>
										</tr>
										<tr>
											<td width="15%" nowrap class='copybold' align="left"><bean:message
													bundle="arra" key="awardDetails.totalArraExpenditure" />:&nbsp;</td>
											<td width="30%" class='copy' nowrap align=left><logic:notEmpty
													name="arraAwardPastReport" property="totalExpenditure">
													<coeusUtils:formatString name="arraAwardPastReport"
														property="totalExpenditure" formatType="currencyFormat" />
												</logic:notEmpty></td>
											<td width="10%" class='copybold' align="left"><bean:message
													bundle="arra" key="awardDetails.CFDANumber" />&nbsp;</td>
											<td width="40%" class='copy' align=left><bean:write
													name="arraAwardPastReport" property="cfdaNumber" /></td>
										</tr>
										<tr>
											<td width="15%" nowrap class='copybold' align="left"><bean:message
													bundle="arra" key="awardDetails.fundsInvoiced" />&nbsp;</td>
											<td width="30%" class='copy' nowrap align=left><logic:notEmpty
													name="arraAwardPastReport" property="totalFederalInvoiced">
													<coeusUtils:formatString name="arraAwardPastReport"
														property="totalFederalInvoiced"
														formatType="currencyFormat" />
												</logic:notEmpty></td>
											<td width="10%" nowrap class='copybold' align=left><bean:message
													bundle="arra" key="awardDetails.recipientDUNSNumber" />&nbsp;</td>
											<td width="40%" class='copy' nowrap align=left><bean:write
													name="arraAwardPastReport" property="recipientDUNSNumber" />
											</td>
										</tr>
										<tr>
											<td width="15%" nowrap class='copybold' align="left"><bean:message
													bundle="arra" key="awardDetails.orderNumber" />&nbsp;</td>
											<td width="30%" class='copy' nowrap align=left><bean:write
													name="arraAwardPastReport" property="orderNumber" /></td>
											<td width="10%" nowrap class='copybold' align=left><bean:message
													bundle="arra" key="awardDetails.govContractingOfficeCode" />&nbsp;</td>
											<td width="40%" class='copy' nowrap align=left><bean:write
													name="arraAwardPastReport"
													property="govContractingOfficeCode" /></td>
										</tr>
									</table>
								</td>
							</tr>

							<tr valign="top">
								<td colspan="4" bgcolor="#D1E5FF" align="right" valign="top">
									<table width="98%" cellpadding="3" align="left" cellspacing="0"
										class="tabtable" border="1" valign="top">
										<tr>
											<td width="10%" nowrap class='copybold' align="center">&nbsp;&nbsp;&nbsp;</td>
											<td width="30%" class='copybold' nowrap align="center"><bean:message
													bundle="arra" key="awardDetails.indSubAwards" />&nbsp;</td>
											<td width="30%" nowrap class='copybold' align="right"><bean:message
													bundle="arra" key="awardDetails.vendorLess25K" />&nbsp;</td>
											<td width="30%" class='copybold' nowrap align="center"><bean:message
													bundle="arra" key="awardDetails.subAwdLess25K" />&nbsp;</td>
										</tr>

										<tr>

											<td width="10%" class='copybold' align="left" nowrap><bean:message
													bundle="arra" key="awardDetails.number" />:</td>
											<td width="30%" class='copy' nowrap align="right"><bean:write
													name="arraAwardPastReport" property="indSubAwards" /></td>
											<td width="30%" class='copy' nowrap align="right"><bean:write
													name="arraAwardPastReport" property="vendorLess25K" /></td>
											<td width="30%" class='copy' nowrap align="right"><bean:write
													name="arraAwardPastReport" property="subAwdLess25K" /></td>
										</tr>

										<tr>
											<td width="10%" class='copybold' align="left" nowrap><bean:message
													bundle="arra" key="awardDetails.totalAmount" />:</td>
											<td width="30%" class='copy' nowrap align="right"><logic:notEmpty
													name="arraAwardPastReport" property="indSubAwardAmount">
													<coeusUtils:formatString name="arraAwardPastReport"
														property="indSubAwardAmount" formatType="currencyFormat" />
												</logic:notEmpty></td>
											<td width="30%" class='copy' nowrap align="right"><logic:notEmpty
													name="arraAwardPastReport" property="vendorLess25KAmount">
													<coeusUtils:formatString name="arraAwardPastReport"
														property="vendorLess25KAmount" formatType="currencyFormat" />
												</logic:notEmpty></td>
											<td width="30%" class='copy' nowrap align="right"><logic:notEmpty
													name="arraAwardPastReport" property="subAwdLess25KAmount">
													<coeusUtils:formatString name="arraAwardPastReport"
														property="subAwdLess25KAmount" formatType="currencyFormat" />
												</logic:notEmpty></td>
										</tr>
									</table>
								</td>
							</tr>

							<tr>
								<td align="left" valign="top" colspan='2' bgcolor="#D1E5FF">
									<table width="100%" border="0" align="left" cellpadding="4"
										cellspacing="0">
										<tr>
											<td width="15%" nowrap valign="top" class='copybold'
												align="left"><bean:message bundle="arra"
													key="awardDetails.ProjectStatus" />&nbsp;</td>
											<td width="30%" class='copy'><bean:write
													name="arraAwardPastReport" property="projectStatus" /></td>
											<td width="10%" nowrap class='copybold'></td>
											<td width="40%" class='copy' nowrap></td>
										</tr>
										<tr>
											<td width="15%" valign="top" nowrap class='copybold'
												align="left"><bean:message bundle="arra"
													key="awardDetails.activityCode" />&nbsp;</td>
											<td width="30%" class='copy'><bean:write
													name="arraAwardPastReport" property="activityCode" /></td>
											<td width="10%" nowrap class='copybold'></td>
											<td width="40%" class='copy' nowrap></td>
										</tr>
										<tr>
											<td width="15%" valign="top" class='copybold' align="left"><bean:message
													bundle="arra" key="awardDetails.awdDescription" />&nbsp;</td>
											<td width="85%" class='copy'><bean:write
													name="arraAwardPastReport" property="awardDescription" /></td>
											<td class='copybold'></td>
											<td class='copy'></td>
										</tr>
										<tr>
											<td width="15%" valign="top" nowrap class='copybold'
												align="left"><bean:message bundle="arra"
													key="awardDetails.activityDescription" />&nbsp;</td>
											<td width="85%" class='copy'><bean:write
													name="arraAwardPastReport" property="activityDescription" /></td>
											<td nowrap class='copybold'></td>
											<td class='copy' nowrap></td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td align="left" valign="top" colspan="4">
									<table width="100%" border="0" cellpadding="0" cellspacing="0"
										class="tableheader">
										<tr>
											<td class="tableheader" height='20'><bean:message
													bundle="arra" key="awardDetails.jobsCreated" /></td>
										</tr>
										<tr>
											<td width="99%" align="left" valign="top" colspan='4'
												bgcolor="#D1E5FF" class="tabtable">
												<div id='jobsCreated'>
													<table width="100%" align="left" border="0" cellpadding="3"
														cellspacing='0'>
														<%String noOfJbsStr ="",jobsAtSubsStr="";%>
														<tr>
															<td nowrap class="copybold" align="left"><bean:message
																	bundle="arra" key="awardDetails.NumOfJobs" />:&nbsp;</td>
															<td nowrap class="copybold" align="left"><logic:present
																	name="arraAwardPastReport" property="noOfJobs">
																	<bean:define id="noOfJobsId" name="arraAwardPastReport"
																		property="noOfJobs" type="java.lang.String" />
																	<%noOfJbsStr = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format((new Double(noOfJobsId)).doubleValue());
                                                                 %>
																	<%  if(noOfJbsStr!=null && !noOfJbsStr.equals("")){                                                                 
                                                                 noOfJbsStr = noOfJbsStr.replaceAll("[$,/,]","");
                                                                 }%>
																</logic:present> <logic:present name="arraAwardPastReport"
																	property="jobsAtSubs">
																	<bean:define id="jobsAtSubsId"
																		name="arraAwardPastReport" property="jobsAtSubs"
																		type="java.lang.String" />
																	<%jobsAtSubsStr = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format((new Double(jobsAtSubsId)).doubleValue());
                                                                 %>
																</logic:present> <%  if(jobsAtSubsStr!=null && !jobsAtSubsStr.equals("")){                                                                 
                                                             jobsAtSubsStr = jobsAtSubsStr.replaceAll("[$,/,]","");                                                               
                                                             }%> <%double totalJobsDb =(new Double(noOfJbsStr).doubleValue())+(new Double(jobsAtSubsStr).doubleValue());
                                                             String totalJobsDbStr = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(totalJobsDb);
                                                             if(totalJobsDbStr!=null && !totalJobsDbStr.equals("")){                                                                 
                                                             totalJobsDbStr = totalJobsDbStr.replaceAll("[$,/,]","");                                                               
                                                             }%> <bean:write
																	name="arraAwardPastReport" property="noOfJobs" />
																+&nbsp; <bean:message bundle="arra"
																	key="awardDetails.jobsAtSubs" />&nbsp; <bean:write
																	name="arraAwardPastReport" property="jobsAtSubs" /> =
																&nbsp; <%=totalJobsDbStr%></td>
															<td class="copybold" align="left"></td>
															<td class="copybold" align="left"></td>

														</tr>
														<tr>
															<td nowrap class="copybold" valign="top" width="10%"
																align="left"><bean:message bundle="arra"
																	key="awardDetails.descriptionOfJobsCreated" />&nbsp;</td>
															<td colspan="4" valign="top" width="40%" class="copy"
																align="left"><bean:write name="arraAwardPastReport"
																	property="employmentImpact" /></td>
														</tr>


														<%  String strBgColor = "#DCE5F1";
                                                 int count = 0;
                                                 %>
														<logic:notEmpty name="arraAwardJobsCreatedForVersion">
															<tr>
																<td width="30%" align="left" class="theader">&nbsp;&nbsp;<bean:message
																		bundle="arra" key="awardDetails.personName" /></td>
																<td width="20%" align="left" class="theader"><bean:message
																		bundle="arra" key="awardDetails.jobTitle" /></td>
																<td width="10%" align="left" class="theader"><bean:message
																		bundle="arra" key="awardDetails.fte" /></td>
																<td width="40%" align="left" class="theader">&nbsp;&nbsp;</td>
															</tr>
															<logic:iterate id="job"
																name="arraAwardJobsCreatedForVersion"
																type="org.apache.struts.validator.DynaValidatorForm"
																indexId="index">

																<% 
                                                         if (count%2 == 0)
                                                             strBgColor = "#D6DCE5";
                                                         else
                                                             strBgColor="#DCE5F1";
                                                         %>
																<tr bgcolor="<%=strBgColor%>" valign="top"
																	onmouseover="className='TableItemOn'"
																	onmouseout="className='TableItemOff'">
																	<td width="30%" align="left" class="copy"><bean:write
																			name="job" property="personName" /></td>
																	<td width=20% " align="left" class="copy"><bean:write
																			name="job" property="jobTitle" /></td>
																	<td width=10% " align="left" class="copy"><bean:write
																			name="job" property="fte" /></td>
																	<td width=40% " align="left" class="copy">&nbsp;&nbsp;</td>
																</tr>
																<% count++;%>
															</logic:iterate>
														</logic:notEmpty>
													</table>
												</div>
											</td>
										</tr>
									</table>
								</td>
							</tr>

							<tr>
								<td align="left" valign="top" colspan="4">
									<table width="100%" border="0" cellpadding="1" cellspacing="0"
										class="tableheader">
										<tr>
											<td class="tableheader" height='20'><bean:message
													bundle="arra" key="awardDetails.infrastructureContact" /></td>
										</tr>
										<tr valign="top">
											<td align="left" valign="top" colspan='4' bgcolor="#D1E5FF"
												class="tabtable">
												<div id='infraContact' style='display: block;'>
													<table width="98%" border="0" align="left" cellpadding="3"
														cellspacing="0">
														<tr>

															<td width="20%" nowrap class="copybold" align="left"
																valign="top">&nbsp;<bean:message bundle="arra"
																	key="awardDetails.Address" /></td>
															<td class="copy" width="60%" align="left"><bean:write
																	name="arraAwardPastReport"
																	property="infraContactAddress" /></td>
															<td align="left" valign="top"></td>
														</tr>
														<tr>

															<td width="20%" nowrap class="copybold" align="left"
																valign="top">&nbsp;<bean:message bundle="arra"
																	key="awardDetails.infrastructureRationale" /></td>
															<td class="copy" width="60%" align="left"><bean:write
																	name="arraAwardPastReport"
																	property="infrastructureRationale" /></td>
															<td align="left" valign="top"></td>
														</tr>
														<tr>

															<td width="20%" nowrap class="copybold" align="left"
																valign="top">&nbsp;<bean:message bundle="arra"
																	key="awardDetails.totalInfraExpenditure" /></td>
															<td class="copy" width="60%" align="left"><logic:notEmpty
																	name="arraAwardPastReport"
																	property="totalInfraExpenditure">
																	<coeusUtils:formatString name="arraAwardPastReport"
																		property="totalInfraExpenditure"
																		formatType="currencyFormat" />
																</logic:notEmpty></td>
															<td align="left" valign="top"></td>
														</tr>
													</table>
												</div>
											</td>
										</tr>
									</table>
								</td>
							</tr>


							<tr>
								<td align="left" valign="top" colspan="4">
									<table width="100%" border="0" cellpadding="0" cellspacing="0"
										class="tableheader">
										<tr>
											<td height='20'><bean:message bundle="arra"
													key="awardDetails.primPlaceofPerf" /></td>
										</tr>
										<tr>
											<td width="96%" valign="top" bgcolor="#D1E5FF" colspan="4"
												class="tabtable">
												<div id='primPlace1' style='display: block;'>
													<!-- Left section -->
													<table width="98%" align="left" border="0" cellpadding="3"
														cellspacing="0">
														<tr>
															<td nowrap class="copybold" width="10%" align="left"
																valign="top">&nbsp;<bean:message bundle="arra"
																	key="awardDetails.Address" /></td>
															<td class="copy" width="80%" align="left"><bean:write
																	name="arraAwardPastReport"
																	property="primPlaceOfPerfAddress" /></td>
															<td></td>
														</tr>
														<tr>
															<td nowrap class="copybold" width="10%" align="left"
																valign="top">&nbsp;<bean:message bundle="arra"
																	key="awardDetails.congrDistrict" /></td>
															<td class="copy" width="80%" align="left"><bean:write
																	name="arraAwardPastReport"
																	property="congressionalDistrict" /></td>
															<td></td>
														</tr>
													</table>
												</div>
											</td>

										</tr>
									</table>
								</td>
							</tr>

							<tr>
								<td align="left" valign="top" colspan="4"><logic:present
										name="arraHighlyCompensatedForVersion">
										<table width="100%" border="0" cellpadding="0" cellspacing="0"
											class="tabtable">
											<tr class='theader'>
												<td class="tableheader" height='20' colspan="4"><bean:message
														bundle="arra" key="awardDetails.highlyCompensated" /></td>
											</tr>
											<tr valign=top>
												<td height="45%" width="80%" align="left" valign="top"
													colspan='4' bgcolor="#D1E5FF">
													<div id='highlyComp' style='display: block;'>
														<table width="100%" align="left" border="0"
															cellpadding="3" cellspacing='0'>
															<tr>
																<td width="30%" nowrap align="left" class="theader">&nbsp;&nbsp;<bean:message
																		bundle="arra" key="awardDetails.personName" /></td>
																<td width="10%" nowrap align="right" class="theader"><bean:message
																		bundle="arra" key="awardDetails.compensationAmt" /></td>
																<td width="50%" align="left" class="theader">&nbsp;&nbsp;</td>
															</tr>

															<%  count = 0;
                                                                 strBgColor = "#DCE5F1";
                                                                 %>
															<logic:iterate id="highlyComp"
																name="arraHighlyCompensatedForVersion"
																type="org.apache.struts.validator.DynaValidatorForm"
																indexId="index">
																<% 
                                                                     if (count%2 == 0){
                                                                     strBgColor = "#D6DCE5";
                                                                     }else{
                                                                     strBgColor="#DCE5F1";
                                                                     }
                                                                     %>
																<tr bgcolor="<%=strBgColor%>" valign="top"
																	onmouseover="className='TableItemOn'"
																	onmouseout="className='TableItemOff'">
																	<td width="30%" align="left" class="copy"><bean:write
																			name="highlyComp" property="personName" /></td>
																	<td width=20% " align="right" class="copy"><logic:notEmpty
																			name="highlyComp" property="compensation">
																			<coeusUtils:formatString name="highlyComp"
																				property="compensation" formatType="currencyFormat" />
																		</logic:notEmpty></td>
																	<td width=50% " align="left" class="copy">&nbsp;&nbsp;</td>
																</tr>
																<% count++;%>
															</logic:iterate>
														</table>
													</div>
												</td>
											</tr>
										</table>
									</logic:present></td>
							</tr>

						</table>
					</div>
				</td>
			</tr>

			<tr>
				<td align="left" valign="top" colspan="2">
					<table width="100%" border="0" cellpadding="0" cellspacing="0"
						class="tableheader">
						<tr>
							<td class="tableheader" height='20'>
								<div id='showVenDetails'>
									<html:link href="javaScript:showHideDetails('showVenDetails');">
										<html:img src="<%=plus%>" border="0" />
									</html:link>
									&nbsp;
									<bean:message bundle="arra"
										key="arra.pastReports.vendorDetails" />

								</div>
								<div id='hideVenDetails' style='display: none;'>
									<html:link href="javaScript:showHideDetails('hideVenDetails');">
										<html:img src="<%=minus%>" border="0" />
									</html:link>
									&nbsp;
									<bean:message bundle="arra"
										key="arra.pastReports.vendorDetails" />
								</div>
							</td>

						</tr>
					</table>
				</td>

			</tr>
			<tr>
				<td>
					<div id="vendorDetails" style='display: none;'>
						<table width="100%" border="0" cellpadding="0" cellspacing="0"
							class="table">

							<tr>
								<td align="left" valign="top"><table width="99%" border="0"
										align="center" cellpadding="2" cellspacing="0"
										class="tabtable">
										<tr>
											<td align="left" valign="top"><table width="100%"
													border="0" cellpadding="0" cellspacing="0"
													class="tableheader">
													<tr>
														<td><bean:message bundle="arra"
																key="awardVendors.vendorList" /></td>
													</tr>
												</table></td>
										</tr>
										<logic:empty name="arraAwardVendorsForVersion">
											<tr align="center">
												<td class="copybold" align="left" colspan="4"><bean:message
														bundle="arra" key="awardVendors.noVendors.displayText" />
												</td>
											</tr>
										</logic:empty>
										<logic:notEmpty name="arraAwardVendorsForVersion">
											<tr align="center">
												<td class="copybold" align="left"><bean:message
														bundle="arra" key="awardVendors.displayText" /></td>
											</tr>
										</logic:notEmpty>
										<tr align="center">
											<td>
												<table width="98%" align="center" border="0" cellpadding="2"
													cellspacing='0' class="tabtable">

													<%  String strBgCor = "#DCE5F1";
                                                          count = 0;
                                                         %>
													<logic:notEmpty name="arraAwardVendorsForVersion">
														<tr>
															<td width="20%" align="left" class="theader"><bean:message
																	bundle="arra" key="awardVendors.vendorName" /></td>
															<td width="40%" align="left" class="theader"><bean:message
																	bundle="arra" key="awardVendors.vendorDUNS" /></td>
															<td width="20%" align="left" class="theader"><bean:message
																	bundle="arra" key="awardVendors.zipCode" /></td>
															<td width="20%" align="right" class="theader"><bean:message
																	bundle="arra" key="awardVendors.paymentAmount" /></td>
														</tr>
														<logic:iterate id="awdVendor" indexId="index"
															name="arraAwardVendorsForVersion"
															type="org.apache.struts.validator.DynaValidatorForm">
															<% 
                                                                 if (count%2 == 0){
                                                             strBgCor = "#D6DCE5";
                                                                 }else{
                                                             strBgCor="#DCE5F1";
                                                                 }
                                                                 %>
															<tr bgcolor="<%=strBgCor%>" valign="top"
																onmouseover="className='TableItemOn'"
																onmouseout="className='TableItemOff'">
																<td colspan="4">
																	<table width="98%">
																		<tr>
																			<td width="20%" align="left" class="copy"><bean:write
																					name="awdVendor" property="vendorName" /></td>
																			<td width="40%" align="left" class="copy"><bean:write
																					name="awdVendor" property="vendorDUNS" /></td>
																			<td width=20% " align="left" class="copy"><bean:write
																					name="awdVendor" property="vendorHQZipCode" /></td>
																			<td width="20%" align="right" class="copy"><logic:notEmpty
																					name="awdVendor" property="paymentAmount">
																					<coeusUtils:formatString name="awdVendor"
																						property="paymentAmount"
																						formatType="currencyFormat" />
																				</logic:notEmpty></td>
																		</tr>
																	</table>
																	<table align="left" width="90%">
																		<tr>
																			<td align="left" class="copy"><b><bean:message
																						bundle="arra" key="awardVendors.serviceDesc" />:</b>&nbsp;&nbsp;<bean:write
																					name="awdVendor" property="serviceDescription" /></td>
																		</tr>
																	</table>
																</td>
															</tr>
															<% count++;%>
														</logic:iterate>
													</logic:notEmpty>
													<logic:notPresent name="arraAwardVendorsForVersion">
														<tr>
															<td colspan="4"><bean:write name="awdVendor"
																	property="awardVendors.noVendors.displayText" /></td>

														</tr>
													</logic:notPresent>
												</table>
											</td>
										</tr>

									</table></td>
							</tr>
						</table>
					</div>
				</td>
			</tr>
			<tr>
				<td align="left" valign="top" colspan="2">
					<table width="100%" border="0" cellpadding="0" cellspacing="0"
						class="tableheader">
						<tr>
							<td class="tableheader" height='20'>
								<div id='showSubcontracts'>
									<html:link
										href="javaScript:showHideDetails('showSubcontracts');">
										<html:img src="<%=plus%>" border="0" />
									</html:link>
									&nbsp;Subcontracts

								</div>
								<div id='hideSubcontracts' style='display: none;'>
									<html:link
										href="javaScript:showHideDetails('hideSubcontracts');">
										<html:img src="<%=minus%>" border="0" />
									</html:link>
									&nbsp;Subcontracts
								</div>
							</td>

						</tr>
					</table>
				</td>

			</tr>
			<tr>
				<td>
					<div id="subcontracts" style='display: none;'>
						<table width="100%" border="1" cellpadding="1" cellspacing="0"
							class="table">
							<tr>
								<td height="2%" align="left" valign="top" class="theader">
									<bean:message bundle="arra"
										key="awardSubcontract.subcontractList" />
								</td>
							</tr>
							<logic:empty name="arraAwardSubcontractsForVersion">
								<tr align="center">
									<td class="copybold" align="left" colspan="4"><bean:message
											bundle="arra"
											key="awardSubcontract.noSubcontracts.dispalyText" /></td>
								</tr>
							</logic:empty>


							<%                   
                                             String STRBGCOLOR = "#DCE5F1";
                                             int INDEX = 1;
                                             String strsubAwdDate,awardAmount,amtDispursed;
                                             edu.mit.coeuslite.utils.DateUtils dateUtils = new edu.mit.coeuslite.utils.DateUtils();
                                             %>
							<logic:notEmpty name="arraAwardSubcontractsForVersion">
								<tr align="center">
									<td colspan="3">
										<table width="100%" border="0" cellpadding="2" cellspacing='1'
											class="tabtable">
											<tr>
												<td width="30%" align="left" class="theader"><bean:message
														bundle="arra" key="awardSubcontract.subcontractorName" /></td>
												<td width="20%" align="left" class="theader"><bean:message
														bundle="arra" key="awardSubcontract.awardDate" /></td>
												<td width="20%" align="left" class="theader"><bean:message
														bundle="arra" key="awardSubcontract.awardAmount" /></td>
												<td width="15%" align="left" class="theader"><bean:message
														bundle="arra" key="awardSubcontract.amtDispursed" /></td>
												<td width="5%" nowrap align="center" class="theader"><bean:message
														bundle="arra" key="awardDetails.jobsCreated" /></td>
											</tr>
											<logic:iterate id="awdSubcontract" indexId="index"
												name="arraAwardSubcontractsForVersion"
												type="org.apache.struts.validator.DynaValidatorForm">
												<% 
                                                     if (INDEX%2 == 0)
                                                         STRBGCOLOR = "#D6DCE5";
                                                     else
                                                         STRBGCOLOR="#DCE5F1"; 
                                                     %>

												<%strsubAwdDate = (String)awdSubcontract.get("subAwardDate");
                                                     strsubAwdDate = (strsubAwdDate==null)?"":strsubAwdDate.trim();
                                                     if(!"".equals(strsubAwdDate)) {
                                                     strsubAwdDate = dateUtils.formatDate(strsubAwdDate,"MM/dd/yyyy");
                                                     }
                                                     if(awdSubcontract.get("subAwardAmount")!= null){
                                                     awardAmount = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(((Double)awdSubcontract.get("subAwardAmount")).doubleValue());
                                                     }else{
                                                     awardAmount = "";
                                                     }
                                                     if(awdSubcontract.get("subAwardAmtDispursed") != null){
                                                     amtDispursed = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(((Double)awdSubcontract.get("subAwardAmtDispursed")).doubleValue()); 
                                                     }else{
                                                     amtDispursed = "";
                                                     }
                                                     %>


												<tr bgcolor="<%=STRBGCOLOR%>" valign="top"
													onmouseover="className='TableItemOn'"
													onmouseout="className='TableItemOff'">
													<!-- modified for View Submission-->
													<td align="left" nowrap class="copy" width="30%">
														<%String divName="Panel"+INDEX;%>
														<div id='<%=divName%>'>

															<%String divlink = "javascript:showHide(1,'"+INDEX+"')";%>
															<html:link href="<%=divlink%>">
																<%String imagePlus=request.getContextPath()+"/coeusliteimages/plus.gif";%>
																<html:img src="<%=imagePlus%>" border="0" />
															</html:link>
															&nbsp;&nbsp;
															<bean:write name="awdSubcontract"
																property="subcontractorName" />
														</div> <% String divsnName="hidePanel"+INDEX;%>
														<div id='<%=divsnName%>' style='display: none;'>

															<% String divsnlink = "javascript:showHide(2,'"+INDEX+"')";%>
															<html:link href="<%=divsnlink%>">
																<%String imageMinus=request.getContextPath()+"/coeusliteimages/minus.gif";%>
																<html:img src="<%=imageMinus%>" border="0" />
															</html:link>
															&nbsp;&nbsp;
															<bean:write name="awdSubcontract"
																property="subcontractorName" />
														</div>
													</td>
													<td width="5%" align="left" nowrap class="copy"><%=strsubAwdDate%></td>
													<td width="5%" align="left" nowrap class="copy"><%=awardAmount%></td>
													<td width="45%" align="left" class="copy"><%=amtDispursed%></td>
													<td><bean:write name="awdSubcontract"
															property="noOfJobs" /></td>
												</tr>

												<tr>
													<td colspan="5">
														<%String divisionName="pan"+INDEX;%>
														<div id='<%=divisionName%>' style='display: none;'>
															<%String subcontractCode = (String)awdSubcontract.get("subcontractCode");%>
															<logic:present name="subcontractDynaBeansListForVersion"
																scope="request">
																<logic:iterate id="hmapId"
																	name="subcontractDynaBeansListForVersion"
																	indexId="index">

																	<bean:define id="mapKey" name="hmapId" property="key"
																		type="java.lang.String" />
																	<bean:define id="mapValue" name="hmapId"
																		property="value"
																		type="edu.mit.coeuslite.utils.CoeusDynaFormList" />

																	<%if(subcontractCode.equalsIgnoreCase(mapKey)){%>
																	<logic:notEmpty name="mapValue" property="infoList">
																		<logic:iterate id="dynaFormInfo" name="mapValue"
																			property="infoList"
																			type="org.apache.struts.validator.DynaValidatorForm"
																			indexId="index">
																			<table width="98%" class="table" border="0"
																				align="center" cellpadding="3" cellspacing="0">
																				<tr class='theader'>
																					<td align="left" colspan='4'>&nbsp; <bean:message
																							bundle="arra" key="awardSubcontract.subcontract" />
																						for <bean:write name="dynaFormInfo"
																							property="subcontractCode" /></td>
																				</tr>
																				<!--Display fields-->
																				<tr valign="top">
																					<td align="left" valign="top" colspan='3'
																						bgcolor="#D1E5FF">
																						<table width="100%" border="0" align="left"
																							cellpadding="3" cellspacing="0">
																							<tr>
																								<td width="15%" nowrap class='copybold'><bean:message
																										bundle="arra"
																										key="awardSubcontract.subcontractCode" />:&nbsp;</td>
																								<td width="30%" class='copy'><bean:write
																										property="subcontractCode" name="dynaFormInfo" /></td>
																								<td width="10%" nowrap class='copybold'
																									align=left><bean:message bundle="arra"
																										key="awardSubcontract.subcontractorName" />:&nbsp;</td>
																								<td width="40%" class='copy'><bean:write
																										property="subcontractorName"
																										name="dynaFormInfo" /></td>
																							</tr>
																							<tr>
																								<td width="15%" nowrap class='copybold'><bean:message
																										bundle="arra"
																										key="awardSubcontract.recipientDUNS" />:&nbsp;</td>
																								<td width="30%" class='copy'><bean:write
																										property="subRecipientDUNS"
																										name="dynaFormInfo" /></td>
																								<td width="10%" nowrap class="copybold"><bean:message
																										bundle="arra" key="awardDetails.congrDistrict" /></td>
																								<td width="40%" class="copy"><bean:write
																										name="dynaFormInfo"
																										property="subRecipientCongDist" /></td>
																							</tr>
																							<tr>
																								<td width="10%" nowrap class='copybold'
																									align=left><bean:message bundle="arra"
																										key="awardSubcontract.subawardDate" />:&nbsp;</td>

																								<td width="30%" class='copy' valign="center">
																									<table border="0" cellspacing="0"
																										cellpadding="0">
																										<tr>
																											<td><coeusUtils:formatDate
																													name="dynaFormInfo"
																													formatString="MM/dd/yyyy"
																													property="subAwardDate" /></td>
																											<td></td>
																										</tr>
																									</table>
																								</td>
																								<td width="10%" valign="top" nowrap
																									class='copybold' align=left><bean:message
																										bundle="arra"
																										key="awardSubcontract.subAwardNumber" />:&nbsp;</td>

																								<td width="30%" valign="top" class='copy'
																									valign="center"><bean:write
																										name="dynaFormInfo" property="subAwardNumber" />
																								</td>
																							</tr>
																							<tr>
																								<td width="15%" nowrap class='copybold'><bean:message
																										bundle="arra"
																										key="awardSubcontract.subawardAmount" />:&nbsp;</td>
																								<td width="30%" class='copy'><logic:notEmpty
																										name="dynaFormInfo" property="subAwardAmount">
																										<coeusUtils:formatString name="dynaFormInfo"
																											property="subAwardAmount"
																											formatType="currencyFormat" />
																									</logic:notEmpty></td>
																								<td width="10%" nowrap class='copybold'
																									align=left><bean:message bundle="arra"
																										key="awardSubcontract.subawardAmtDispursed" />:&nbsp;</td>
																								<td width="40%" class='copy'><logic:notEmpty
																										name="dynaFormInfo"
																										property="subAwardAmtDispursed">
																										<coeusUtils:formatString name="dynaFormInfo"
																											property="subAwardAmtDispursed"
																											formatType="currencyFormat" />
																									</logic:notEmpty></td>
																							</tr>
																							<tr>
																								<td width="15%" nowrap valign="top"
																									class='copybold'><bean:message
																										bundle="arra" key="awardDetails.NoOfJobs" />&nbsp;</td>
																								<td width="30%" class='copy' valign="top">
																									<bean:write name="dynaFormInfo"
																										property="noOfJobs" />
																								</td>
																								<td width="10%" valign="top" nowrap
																									class='copybold' align=left><bean:message
																										bundle="arra"
																										key="awardDetails.employmentImpact" />&nbsp;</td>
																								<td width="40%" class='copy'><bean:write
																										name="dynaFormInfo"
																										property="employmentImpact" /></td>
																							</tr>
																						</table>
																					</td>
																				</tr>
																				<!--Editable fields-->



																				</logic:iterate>
																				</logic:notEmpty>
																				<logic:notEmpty name="mapValue" property="list">
																					<tr class='theader'>
																						<td align="left" colspan='4'>&nbsp; <bean:message
																								bundle="arra"
																								key="awardSubcontract.vendorDetails" />
																						</td>
																					</tr>
																					<logic:iterate id="dynaFormInfo" name="mapValue"
																						property="list"
																						type="org.apache.struts.validator.DynaValidatorForm"
																						indexId="index">
																						<tr valign="top">
																							<td align="left" valign="top" colspan='3'
																								bgcolor="#D1E5FF">
																								<table width="100%" border="0" align="left"
																									cellpadding="3" cellspacing="0">
																									<tr>
																										<td width="30%" align="left" class="theader"><bean:message
																												bundle="arra" key="awardVendors.vendorName" /></td>
																										<td width="30%" align="left" class="theader"><bean:message
																												bundle="arra" key="awardVendors.vendorDUNS" /></td>
																										<td width="10%" align="left" class="theader"><bean:message
																												bundle="arra" key="awardVendors.zipCode" /></td>
																										<td width="20%" align="left" class="theader"><bean:message
																												bundle="arra"
																												key="awardVendors.paymentAmount" /></td>
																										<td width="10%" align="left" class="theader">&nbsp;</td>
																									</tr>
																									<tr>
																										<td width="30%" align="left" class="copy"><bean:write
																												property="vendorName" name="dynaFormInfo" /></td>
																										<td width="30%" align="left" class="copy"><bean:write
																												property="vendorDUNS" name="dynaFormInfo" /></td>
																										<td width="10%" align="left" class="copy"><bean:write
																												property="vendorHQZipCode"
																												name="dynaFormInfo" /></td>
																										<td width="20%" align="left" class="copy"><logic:notEmpty
																												name="dynaFormInfo" property="paymentAmount">
																												<coeusUtils:formatString name="dynaFormInfo"
																													property="paymentAmount"
																													formatType="currencyFormat" />
																											</logic:notEmpty></td>
																									</tr>
																									<tr>
																										<td colspan="4">
																											<table width="100%" border="0"
																												cellpadding="0" cellspacing="0">
																												<tr>
																													<td align="left" nowrap class="copybold"
																														valign="top"><bean:message
																															bundle="arra"
																															key="awardVendors.serviceDesc" />:</td>
																													<td align="left" class="copy" valign="top">

																														<bean:write property="serviceDescription"
																															name="dynaFormInfo" />

																													</td>
																												</tr>
																											</table>
																										</td>
																									</tr>
																									<tr>
																										<!-- JM 7-2-2012 fixed typo on height attribute -->
																										<td class='copy' height='1' colspan="5">&nbsp;</td>
																									</tr>
																								</table>
																							</td>
																						</tr>
																					</logic:iterate>
																				</logic:notEmpty>
																				<logic:notEmpty name="mapValue" property="beanList">
																					<tr class='theader'>
																						<td align="left" colspan='4'><bean:message
																								bundle="arra"
																								key="awardDetails.highlyCompensated" />&nbsp;</td>
																					</tr>
																					<tr>
																						<td>
																							<table width="100%" align="left" border="0"
																								cellpadding="3" cellspacing='0' class="tabtable">
																								<tr>
																									<td width="30%" nowrap align="left"
																										class="theader">&nbsp;&nbsp;<bean:message
																											bundle="arra" key="awardDetails.personName" /></td>
																									<td width="10%" align="right" class="theader"><bean:message
																											bundle="arra"
																											key="awardDetails.compensationAmt" /></td>
																									<td width="50%" align="left" class="theader">&nbsp;&nbsp;</td>
																								</tr>

																								<%  count = 0;
                                                                                                          strBgColor = "#DCE5F1";
                                                                                                      %>

																								<logic:iterate id="dynaFormInfo" name="mapValue"
																									property="beanList"
																									type="org.apache.struts.validator.DynaValidatorForm"
																									indexId="index">
																									<% 
                                                                                                          if (count%2 == 0)
                                                                                                          strBgColor = "#D6DCE5";
                                                                                                          else
                                                                                                          strBgColor="#DCE5F1"; 
                                                                                                          %>
																									<tr bgcolor="<%=strBgColor%>" valign="top"
																										onmouseover="className='TableItemOn'"
																										onmouseout="className='TableItemOff'">
																										<td width="30%" align="left" class="copy"><bean:write
																												property="personName" name="dynaFormInfo" /></td>
																										<td width=20% " align="right" class="copy"><logic:notEmpty
																												name="dynaFormInfo" property="compensation">
																												<coeusUtils:formatString name="dynaFormInfo"
																													property="compensation"
																													formatType="currencyFormat" />
																											</logic:notEmpty></td>
																										<td width=50% " align="left" class="copy">&nbsp;&nbsp;</td>
																									</tr>
																									<% count++;%>
																								</logic:iterate>
																							</table>
																						</td>
																					</tr>
																				</logic:notEmpty>

																				<logic:notEmpty name="mapValue" property="infoList">
																					<logic:iterate id="dynaFormInfo" name="mapValue"
																						property="infoList"
																						type="org.apache.struts.validator.DynaValidatorForm"
																						indexId="index">

																						<tr class='theader'>
																							<td align="left" colspan='4'>&nbsp; <bean:message
																									bundle="arra"
																									key="awardDetails.primPlaceofPerf" />
																							</td>
																						</tr>
																						<!--Display fields-->
																						<tr valign="top">
																							<td align="left" valign="top" colspan='3'
																								bgcolor="#D1E5FF">
																								<table width="100%" border="0" align="left"
																									cellpadding="3" cellspacing="0">
																									<tr>
																										<td width="15%" nowrap class='copybold'><bean:message
																												bundle="arra" key="awardDetails.Address" />&nbsp;</td>
																										<td width="30%" class='copy'><bean:write
																												property="primPlaceOfPerfAddress"
																												name="dynaFormInfo" /></td>
																										<td width="10%" nowrap class='copybold'
																											align=left><bean:message bundle="arra"
																												key="awardDetails.congrDistrict" />&nbsp;</td>
																										<td width="40%" class='copy'><bean:write
																												property="primPlaceCongDist"
																												name="dynaFormInfo" /></td>
																									</tr>
																								</table>
																							</td>
																						</tr>

																					</logic:iterate>
																				</logic:notEmpty>
																			</table>
																			<%}%>
																		</logic:iterate>
															</logic:present>
														</div>
													</td>
												</tr>

												<% INDEX++;%>
											</logic:iterate>
											</logic:notEmpty>

										</table>
									</td>
								</tr>
						</table>
					</div>

				</td>
			</tr>


		</table>
	</html:form>



</body>
</html>
