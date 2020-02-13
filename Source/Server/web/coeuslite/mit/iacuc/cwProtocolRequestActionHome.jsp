<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@page import="edu.mit.coeuslite.iacuc.bean.ProtocolHeaderDetailsBean"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%
ProtocolHeaderDetailsBean headerBean = (ProtocolHeaderDetailsBean)session.getAttribute("iacucHeaderBean");
String statusCode = new Integer(headerBean.getProtocolStatusCode()).toString();
String submissionStatusCode = (String)session.getAttribute("iacucSubmissionStatusCode");

%>


<html:html locale="true">
<head>
<title>Protocol Request Actions Home</title>
<script language="JavaScript">
                function showActionPage(actionId){
                   
                    if(actionId == '108'){ <!-- Request To Lift Hold -->
                        document.iacucProtocolActionsForm.action = "<%=request.getContextPath()%>/getRequestForLiftHold.do?IACUC_ACTION_CODE="+actionId;
                        document.iacucProtocolActionsForm.submit();                    
                    }else if(actionId == '107'){ <!-- Request to Deativate -->
                        document.iacucProtocolActionsForm.action = "<%=request.getContextPath()%>/getRequestForDeactivate.do?IACUC_ACTION_CODE="+actionId;
                        document.iacucProtocolActionsForm.submit();                    
                     }else if(actionId == '114'){ <!-- Notify IACUC -- >
                        document.iacucProtocolActionsForm.action = "<%=request.getContextPath()%>/getNotifyIacuc.do?IACUC_ACTION_CODE="+actionId;
                        document.iacucProtocolActionsForm.submit();                    
                   }else if(actionId == '115'){ <!-- Withdrawn Submission -->
                        document.iacucProtocolActionsForm.action = "<%=request.getContextPath()%>/getIacucWithdrawSubmission.do?IACUC_ACTION_CODE="+actionId;
                        document.iacucProtocolActionsForm.submit();                    
                    }else if(actionId == '100'){
                        document.iacucProtocolActionsForm.action = "<%=request.getContextPath()%>/getSubmitForIacuc.do?PAGE=SR";
                        document.iacucProtocolActionsForm.submit();                     
                    }
                    // Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_start
                    else if(actionId == '117'){
                        document.iacucProtocolActionsForm.action = "<%=request.getContextPath()%>/getIacucAbandonProtocol.do?IACUC_ACTION_CODE="+actionId;
                        document.iacucProtocolActionsForm.submit();                     
                    }
                    // Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_end
                }
        </script>
<html:base />
</head>
<body>
	<html:form action="/getAllIacucActions.do">
		<script type="text/javascript">
            document.getElementById('txt').innerHTML = '<bean:message bundle="iacuc" key="helpPageTextProtocol.Actions"/>';
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
										bundle="iacuc" key="helpPageTextProtocol.help" />
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
							<td><b><bean:message bundle="iacuc"
										key="protocolAction.label.right" /></b></td>
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
						<%if(!((statusCode.equals("100")) || (statusCode.equals("101")) || (statusCode.equals("104"))
                                    || (statusCode.equals("202")) || (statusCode.equals("203")) || (statusCode.equals("309") || (statusCode.equals("107"))))){%>
						<tr>
							<td class="copybold"><html:link
									href="javascript:showActionPage('114')">
									<u>
									<bean:message bundle="iacuc" key="protocolAction.114.label" /></u>
								</html:link></td>
							<td><bean:message bundle="iacuc"
									key="protocolAction.114.description" /></td>
						</tr>

						<%}
                            if(statusCode.equals("201")){%><!-- Active - On Hold -->

						<tr>
							<td class="copybold"><html:link
									href="javascript:showActionPage('108')">
									<u>
									<bean:message bundle="iacuc" key="protocolAction.108.label" /></u>
								</html:link></td>
							<td><bean:message bundle="iacuc"
									key="protocolAction.108.description" /></td>
						</tr>
						<tr>
							<td class="copybold"><html:link
									href="javascript:showActionPage('107')">
									<u>
									<bean:message bundle="iacuc" key="protocolAction.107.label" /></u>
								</html:link></td>
							<td><bean:message bundle="iacuc"
									key="protocolAction.107.description" /></td>
						</tr>
						<%}
                            if(statusCode.equals("200")){%><!-- Active -->

						<tr>
							<td class="copybold"><html:link
									href="javascript:showActionPage('107')">
									<u>
									<bean:message bundle="iacuc" key="protocolAction.107.label" /></u>
								</html:link></td>
							<td><bean:message bundle="iacuc"
									key="protocolAction.107.description" /></td>
						</tr>
						<%}
                            // Modified for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_start
                            if(statusCode.equals("100") || statusCode.equals("102") || statusCode.equals("103") 
                            || statusCode.equals("104") || statusCode.equals("105") || statusCode.equals("303")
                            || statusCode.equals("107") || statusCode.equals("203")|| statusCode.equals("302")){%>

						<tr>
							<td class="copybold"><html:link
									href="javascript:showActionPage('100')">
									<u>
									<bean:message key="protocolAction.100.label" /></u>
								</html:link></td>
							<td><bean:message bundle="iacuc"
									key="protocolAction.100.description" /></td>
						</tr>
						<%if( statusCode.equals("104") || statusCode.equals("107")){%>
						<tr>
							<td class="copybold"><html:link
									href="javascript:showActionPage('117')">
									<u>
									<bean:message bundle="iacuc" key="protocolAction.117.label" /></u>
								</html:link></td>
							<td><bean:message bundle="iacuc"
									key="protocolAction.117.description" /></td>
						</tr>
						<!-- Modified for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_end-->
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
						<%--if("101".equals(submissionStatusCode) || //Pending
                        "102".equals(submissionStatusCode) ||  //Submitted to Committee
                        "103".equals(submissionStatusCode))    // In Agenda
                    {--%>

						<%--
                            <tr>
                                <td class="copybold">
                                    <html:link href="javascript:showActionPage('115')">
                                        <u><bean:message bundle="iacuc" key="iacucProtocolAction.115.label"/></u>
                                    </html:link>
                                </td>   
                                <td>
                                    <bean:message bundle="iacuc" key="iacucProtocolAction.115.description"/>
                                </td>                        
                            </tr>  --%>

						<!--COEUSQA-2870-IACUC - modify actions available for withdrawn protocol and revise displayed messages - Start -->

						<%if(("101".equals(statusCode)||"203".equals(statusCode)||"200".equals(statusCode))&& ("101".equals(submissionStatusCode) || //Pending
                                    "102".equals(submissionStatusCode) ||  //Submitted to Committee
                                    "103".equals(submissionStatusCode) || //In Agenda
                                    "214".equals(submissionStatusCode)) //Administratively Incomplete
                         ){%>

						<!--COEUSQA-2870-IACUC - modify actions available for withdrawn protocol and revise displayed messages - End-->
						<tr>
							<td class="copybold"><html:link
									href="javascript:showActionPage('115')">
									<u>
									<bean:message bundle="iacuc"
										key="iacucProtocolAction.115.label" /></u>
								</html:link></td>
							<td><bean:message bundle="iacuc"
									key="iacucProtocolAction.115.description" /></td>
						</tr>
						<%}%>


						<%--}--%>
						<!-- Added for response and expedited approval actions - end -->
						<!--Added for Case #2250 - Expire Protocol -Start -->
						<%if(statusCode.equals("105") || statusCode.equals("201")){%>
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
