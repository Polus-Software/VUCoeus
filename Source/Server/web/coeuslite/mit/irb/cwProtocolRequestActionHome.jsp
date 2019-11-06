<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@page
	import="edu.mit.coeuslite.irb.bean.ProtocolHeaderDetailsBean,edu.mit.coeus.utils.CoeusConstants"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%
    ProtocolHeaderDetailsBean headerBean = (ProtocolHeaderDetailsBean)session.getAttribute("protocolHeaderBean");
    //String statusCode = (String)session.getAttribute("protocolStatusCode"); 
    String statusCode = new Integer(headerBean.getProtocolStatusCode()).toString(); 
    String submissionStatusCode=(String)session.getAttribute("irbSubmissionStatusCode"); 
    
%>

<html:html locale="true">
<head>
<title>Protocol Request Actions Home</title>
<script language="JavaScript">
                function showActionPage(actionId){
                    if(actionId == '105'){
                        document.protocolActionsForm.action = "<%=request.getContextPath()%>/getRequestToClose.do?ACTION_CODE="+actionId;
                        document.protocolActionsForm.submit();                    
                    }else if(actionId == '106'){
                        document.protocolActionsForm.action = "<%=request.getContextPath()%>/getRequestForSuspension.do?ACTION_CODE="+actionId;
                        document.protocolActionsForm.submit();                    
                    }else if(actionId == '108'){
                        document.protocolActionsForm.action = "<%=request.getContextPath()%>/getRequestToCloseEnrollment.do?ACTION_CODE="+actionId;
                        document.protocolActionsForm.submit();                    
                    }else if(actionId == '114'){
                        document.protocolActionsForm.action = "<%=request.getContextPath()%>/getRequestForDataAnalysis.do?ACTION_CODE="+actionId;
                        document.protocolActionsForm.submit();                    
                    }else if(actionId == '115'){
                        document.protocolActionsForm.action = "<%=request.getContextPath()%>/getRequestToReopenEnrol.do?ACTION_CODE="+actionId;
                        document.protocolActionsForm.submit();                    
                    }else if(actionId == '116'){
                        document.protocolActionsForm.action = "<%=request.getContextPath()%>/getNotifyIRB.do?ACTION_CODE="+actionId;
                        document.protocolActionsForm.submit();                    
                    }else if(actionId == '100'){
                        document.protocolActionsForm.action = "<%=request.getContextPath()%>/getSubmitForReview.do?PAGE=SR";
                        document.protocolActionsForm.submit();                     
                    <!-- Added for response and expedited approval actions - start -->    
                    }else if(actionId == '205'){
                        document.protocolActionsForm.action = "<%=request.getContextPath()%>/getExpeditedApproval.do?ACTION_CODE="+actionId;
                        document.protocolActionsForm.submit();                    
                    }else if(actionId == '208'){
                        document.protocolActionsForm.action = "<%=request.getContextPath()%>/getResponseApproval.do?ACTION_CODE="+actionId;
                        document.protocolActionsForm.submit();                    
                    //}                    
                    <!-- Added for response and expedited approval actions - end -->
                    <!--Added for Case #2250 - Expire Protocol -Start -->
                    }else if(actionId == '305'){
                        document.protocolActionsForm.action = "<%=request.getContextPath()%>/getExpire.do?ACTION_CODE="+actionId;
                        document.protocolActionsForm.submit();                    
                    }
                    <!--Added for Case #2250 - Expire Protocol -End -->
                    <!-- Added for case#3214 - Withdraw Submission - start -->
                    else if(actionId == '303'){
                        document.protocolActionsForm.action = "<%=request.getContextPath()%>/getWithdrawSubmission.do?ACTION_CODE="+actionId;
                        document.protocolActionsForm.submit();                    
                    }
                    <!-- Added for case#3214 - Withdraw Submission - end -->
                    <!-- Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved-Start -->
                    else if(actionId == '119'){
                        document.protocolActionsForm.action = "<%=request.getContextPath()%>/getAbandonProtocol.do?ACTION_CODE="+actionId;
                        document.protocolActionsForm.submit();                    
                    }
                    <!-- Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_end -->
                }
            </script>
<html:base />
</head>
<body>
	<html:form action="/getAllActions.do">
		<script type="text/javascript">
            document.getElementById('txt').innerHTML = '<bean:message key="helpPageTextProtocol.Actions"/>';
        </script>
		<table width="100%" height="100%" border="0" cellpadding="0"
			cellspacing="0" class="table" align='center'>

			<tr>
				<td align="left" valign="top" class='core'>
					<table width="100%" border="0" align="center" cellpadding="0"
						cellspacing="0" class="tabtable">
						<tr class="theader">
							<td height="20" align="left" valign="top"><bean:message
									key="protocolAction.label.header" /></td>
							<td align="right"><a id="helpPageText"
								href="javascript:showBalloon('helpPageText', 'helpText')"> <bean:message
										key="helpPageTextProtocol.help" />
							</a></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td align="left" valign="top" class='core'>
					<table width="100%" border="0" align="center" cellpadding="4"
						cellspacing="0" class="tabtable">
						<tr>
							<td><b><bean:message key="protocolAction.label.right" /></b>
							</td>
						</tr>
					</table>
				</td>
			</tr>

			<tr>
				<td align="left" valign="top" class='core'>
					<table width="100%" border="0" align="center" cellpadding="4"
						cellspacing="0" class="tabtable">
						<tr>
							<td height='10'></td>
						</tr>
						<!--Modified for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_start -->
						<%if(! ((statusCode.equals("100")) || (statusCode.equals("101")) || (statusCode.equals("102"))
                            || (statusCode.equals("103")) || (statusCode.equals("105")) || (statusCode.equals("106"))
                            || ("313".equals(statusCode)) || (statusCode.equals("104"))) ){%>
						<!--Modified for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_end -->
						<tr>
							<td class="copybold"><html:link
									href="javascript:showActionPage('116')">
									<u>
									<bean:message key="protocolAction.116.label" /></u>
								</html:link></td>
							<td><bean:message key="protocolAction.116.description" /></td>
						</tr>
						<%}%>
						<%if(statusCode.equals("200")){%>

						<tr>
							<td class="copybold"><html:link
									href="javascript:showActionPage('108')">
									<u>
									<bean:message key="protocolAction.108.label" /></u>
								</html:link></td>
							<td><bean:message key="protocolAction.108.description" /></td>
						</tr>

						<tr>
							<td class="copybold"><html:link
									href="javascript:showActionPage('114')">
									<u>
									<bean:message key="protocolAction.114.label" /></u>
								</html:link></td>
							<td><bean:message key="protocolAction.114.description" /></td>
						</tr>

						<tr>
							<td class="copybold"><html:link
									href="javascript:showActionPage('105')">
									<u>
									<bean:message key="protocolAction.105.label" /></u>
								</html:link></td>
							<td><bean:message key="protocolAction.105.description" /></td>
						</tr>

						<tr>
							<td class="copybold"><html:link
									href="javascript:showActionPage('106')">
									<u>
									<bean:message key="protocolAction.106.label" /></u>
								</html:link></td>
							<td><bean:message key="protocolAction.106.description" /></td>
						</tr>

						<%}else if(statusCode.equals("201")){%>
						<tr>
							<td class="copybold"><html:link
									href="javascript:showActionPage('105')">
									<u>
									<bean:message key="protocolAction.105.label" /></u>
								</html:link></td>
							<td><bean:message key="protocolAction.105.description" /></td>
						</tr>

						<tr>
							<td class="copybold"><html:link
									href="javascript:showActionPage('106')">
									<u>
									<bean:message key="protocolAction.106.label" /></u>
								</html:link></td>
							<td><bean:message key="protocolAction.106.description" /></td>
						</tr>

						<tr>
							<td class="copybold"><html:link
									href="javascript:showActionPage('114')">
									<u>
									<bean:message key="protocolAction.114.label" /></u>
								</html:link></td>
							<td><bean:message key="protocolAction.114.description" /></td>
						</tr>

						<tr>
							<td class="copybold"><html:link
									href="javascript:showActionPage('115')">
									<u>
									<bean:message key="protocolAction.115.label" /></u>
								</html:link></td>
							<td><bean:message key="protocolAction.115.description" /></td>
						</tr>
						<%}else if(statusCode.equals("202") || statusCode.equals("203")){%>
						<tr>
							<td class="copybold"><html:link
									href="javascript:showActionPage('105')">
									<u>
									<bean:message key="protocolAction.105.label" /></u>
								</html:link></td>
							<td><bean:message key="protocolAction.105.description" /></td>
						</tr>

						<tr>
							<td class="copybold"><html:link
									href="javascript:showActionPage('106')">
									<u>
									<bean:message key="protocolAction.106.label" /></u>
								</html:link></td>
							<td><bean:message key="protocolAction.106.description" /></td>
						</tr>
						<!-- Case# 3791: Unable to resubmit a protocol that had been disapproved by the IRB - Start -->
						<%-- <%}else if(statusCode.equals("100") || statusCode.equals("102") || statusCode.equals("103") || statusCode.equals("104") || statusCode.equals("105") || statusCode.equals("106") || statusCode.equals("304")){%>
                        --%>
						<%}else if(statusCode.equals("100") || statusCode.equals("102") || statusCode.equals("103") 
                                || statusCode.equals("104") || statusCode.equals("105") || statusCode.equals("106") 
                                || statusCode.equals("304") ||  statusCode.equals("306")){%>
						<!-- Case# 3791: Unable to resubmit a protocol that had been disapproved by the IRB - End  -->
						<tr>
							<td class="copybold"><html:link
									href="javascript:showActionPage('100')">
									<u>
									<bean:message key="protocolAction.100.label" /></u>
								</html:link></td>
							<td><bean:message key="protocolAction.100.description" /></td>
						</tr>

						<!-- Modified and Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved-Start -->
						<%if("102".equals(statusCode)|| "104".equals(statusCode)){%>
						<tr>
							<td class="copybold"><html:link
									href="javascript:showActionPage('119')">
									<u>
									<bean:message key="protocolAction.119.label" /></u>
								</html:link></td>
							<td><bean:message key="protocolAction.119.description" /></td>
						</tr>
						<!-- Modified and Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved- End -->
						<!-- Added for response and expedited approval actions - start -->
						<%}}else if(statusCode.equals("101")){%>
						<%--
                        <tr>
                            <td class="copybold">
                                <html:link href="javascript:showActionPage('205')">
                                    <u><bean:message key="protocolAction.label.ea"/></u>
                                </html:link>
                            </td>   
                            <td>
                                <bean:message key="protocolAction.label.sendea"/>
                            </td>                        
                        </tr>
                        <tr>
                            <td class="copybold">
                                <html:link href="javascript:showActionPage('208')">
                                    <u><bean:message key="protocolAction.label.ra"/></u>
                                </html:link>
                            </td>   
                            <td>
                                <bean:message key="protocolAction.label.sendra"/>
                            </td>                        
                        </tr> 
                        --%>
						<%}%>
						<!-- Added for case#3214 - Withdraw Submission - start -->

						<!--COEUSQA-2870-IACUC - modify actions available for withdrawn protocol and revise displayed messages-->
						<%if("100".equals(submissionStatusCode)||"101".equals(submissionStatusCode)||"102".equals(submissionStatusCode)){%>
						<tr>
							<td class="copybold"><html:link
									href="javascript:showActionPage('303')">
									<u>
									<bean:message key="protocolAction.303.label" /></u>
								</html:link></td>
							<td><bean:message key="protocolAction.303.description" /></td>
						</tr>
						<%}%>

						<!-- Added for case#3214 - Withdraw Submission - end -->
						<!-- Added for response and expedited approval actions - end -->
						<!--Added for Case #2250 - Expire Protocol -Start -->
						<%if(statusCode.equals("200") || statusCode.equals("201") || statusCode.equals("202") ||
                        statusCode.equals("203") || statusCode.equals("300") || statusCode.equals("301") ||
                        statusCode.equals("302") || statusCode.equals("304") || statusCode.equals("308")){%>
						<%--
                    <tr>
                        <td class="copybold">
                            <html:link href="javascript:showActionPage('305')">
                                <u><bean:message key="protocolAction.label.Expire"/></u>
                            </html:link>
                        </td>   
                        <td>
                            <bean:message key="protocolAction.label.ExpireProtocol"/>
                        </td>                        
                    </tr>  
                    --%>
						<%}%>
						<!--Added for Case #2250 - Expire Protocol -End -->
						<tr>
							<td height='10'></td>
						</tr>
					</table>
				</td>
			</tr>

		</table>
	</html:form>
</body>
</html:html>