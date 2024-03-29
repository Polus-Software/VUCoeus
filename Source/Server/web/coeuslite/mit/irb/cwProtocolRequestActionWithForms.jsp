<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@ page
	import="java.util.Date,java.text.SimpleDateFormat,edu.mit.coeuslite.utils.CoeusLiteConstants"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%
    String EMPTYSTRING ="";
    Date currentDate = new Date();
    
    String strCurrentDate = EMPTYSTRING;    
    if(currentDate != null){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        strCurrentDate = dateFormat.format(currentDate);
    }
    
    
    String actionCode = (String)session.getAttribute("ACTION_CODE");
    String error = (String)session.getAttribute("ERROR"); 
    
    String approvalActionErrorFlag = (String)session.getAttribute("approvalActionErrorFlag");
    String successFlag = (String)session.getAttribute("successFlag");
    
    String committeeMode = (String) session.getAttribute("IRB_COMM_SELECTION_DURING_SUBMISSION");
    committeeMode = (committeeMode == null)? EMPTYSTRING : committeeMode;
    
    java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("MM/dd/yyyy hh:mm a");
    java.sql.Timestamp timeStamp; 
    String updateTimeStamp = "";
    
    String tempSubmissionCompleted = (String) session.getAttribute("tempSubmissionSaved");
    String savedNotificationType = "";
    
    if(CoeusLiteConstants.YES.equalsIgnoreCase(tempSubmissionCompleted)){
        savedNotificationType = (String) request.getAttribute("savedNotificationType");
        
    } %>


<html:html locale="true">
<head>
<title>Protocol Request Actions</title>
<script language="javascript" type="text/JavaScript"
	src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/coeusLiteCalendar.js"></script>
<script language="JavaScript">
            var attachmentModified = false;
            var committeeId = '';
            var errValue = false;
            function saveActions(){
                if('<%=committeeMode%>' == 'O' && document.protocolActionsForm.committeeId.value == ''){
                    if(!confirm('Are you sure you want to perform the Notify IRB Submission without selecting the committee.')){
                        return;
                    }
                }
               
                if(savedNotifType!= undefined && savedNotifType != 'null'  && document.protocolActionsForm.notificationType.value != savedNotifType){
                    if(!confirm('<bean:message key="protocolAction.msg.changeNotifTypeConfirmation"/>')){
                        document.protocolActionsForm.notificationType.value = savedNotifType;
                        return;
                    }
                }
                
                document.protocolActionsForm.action = "<%=request.getContextPath()%>/saveTempProtocolActions.do?committeeId="+committeeId;
                document.protocolActionsForm.Save.disabled=true;
                document.protocolActionsForm.submit();
            }   
         
            function showGeneralInfoPage(){
                document.protocolActionsForm.action = "<%=request.getContextPath()%>/getProtocolData.do?PAGE=G";
                document.protocolActionsForm.submit();            
            }
            
            function setCommitteeId(sel){  
                committeeId = sel.options[sel.selectedIndex].value;   
                DATA_CHANGED = 'true';
                dataChanged();                
            }            
            
             
            function getActionPage(){
                document.protocolActionsForm.action = "<%=request.getContextPath()%>/getAllActions.do";
                document.protocolActionsForm.submit();            
            }
            
            function add_attachment(){
                
                document.protocolActionsForm.action = "<%=request.getContextPath()%>/addProtocolActionAttachment.do";
                document.protocolActionsForm.submit();
            }
            
             function view_attachment(id){
                document.protocolActionsForm.action = "<%=request.getContextPath()%>/viewProtocolActionAttachment.do?docId="+id;
                document.protocolActionsForm.submit();
            }
             function modify_attachment(id, mode){
                var confirmed = 'true';
                if(mode == 'D'){
                    if (!confirm("<bean:message key="protocolAction.msg.confirmDeleteAttachment"/>")==true){
                        confirmed = 'false';
                    } else {
                        clear_attachment_fields();
                    }
                }
                if(confirmed == 'true'){
                    document.protocolActionsForm.acType.value = 'U';
                    document.protocolActionsForm.action = "<%=request.getContextPath()%>/modifyProtocolActionAttachment.do?docId="+id+"&mode="+mode;
                    document.protocolActionsForm.submit();
                }
            }
             function clear_attachment_fields(){
                document.protocolActionsForm.acType.value = '';
                document.protocolActionsForm.fileName.value = '';
                document.protocolActionsForm.description.value = '';
                document.protocolActionsForm.document.value = '';
                attachmentModified = false;
                change_attachment_label();
             }
             
             function confirmAttachmentCancel(){
                  if(attachmentModified == true){
                 if (confirm("<bean:message key="protocolAction.msg.saveConfirmation"/>")==true ){
                        validate_and_save_upload_form();
                    } else {
                        clear_attachment_fields();
                    }
                    } else {
                        clear_attachment_fields();
                    }
             }
            
           function change_attachment_label(){ 
           <%if(!actionCode.equals("303")){%>
             if(document.protocolActionsForm.acType.value.length==0 || document.protocolActionsForm.acType.value == 'I'
            || document.protocolActionsForm.acType.value == ''){
               document.getElementById('header_label').innerHTML = 'Add Attachment' ;
             } else if(document.protocolActionsForm.acType.value=='U'){
                document.getElementById('header_label').innerHTML = 'Modify Attachment' ;
            }
            <%}%>
          }
          
            function validate_and_save_upload_form(){
                var isValid = true;
                var description = document.protocolActionsForm.description.value;
                description = description.replace(/^\s+|\s+$/, '');
                var descriptionLength = description.length;
                document.protocolActionsForm.description.value = description;
                if(descriptionLength < 1){
                   alert('<bean:message key="protocolAction.msg.enterDescription"/>');
                   document.protocolActionsForm.description.focus();
                   isValid = false;
                }
                if(isValid){
                    add_attachment();
                }
             }
             
             function attachmentFieldModified(){
             attachmentModified = true;
             dataChanged();
             }
             var savedNotifType;
            
               
             <%if(!"".equals(savedNotificationType)){%>
                savedNotifType = "<%=savedNotificationType%>";
                
             <%}%>
        
        </script>


<html:base />
<style>
.textbox-longer {
	width: 400px;
}

.clsavebutton {
	width: 110px;
}
</style>
</head>
<body>
	<html:form action="/getAllActions.do" enctype="multipart/form-data">
		<script type="text/javascript">
            document.getElementById('txt').innerHTML = '<bean:message key="helpPageTextProtocol.Actions"/>';
        </script>
		<table width="100%" height="100%" border="0" cellpadding="0"
			cellspacing="0" class="table" align='center'>
			<%-- 
            If there are no errors, appropriate action headers are displayed on the view (JSP)
            else they are hidden and only the error message is shown
        --%>
			<%if(error.equals("N")){%>
			<tr>
				<td align="left" valign="top" class="core">
					<table width="100%" border="0" align="center" cellpadding="0"
						cellspacing="0" class="tabtable">
						<tr class="theader">
							<td height="20" align="left" valign="top">
								<%if(actionCode.equals("105")){%> <bean:message
									key="protocolAction.label.rtc" /> <%}else if(actionCode.equals("106")){%>
								<bean:message key="protocolAction.label.rts" /> <%}else if(actionCode.equals("108")){%>
								<bean:message key="protocolAction.label.rce" /> <%}else if(actionCode.equals("114")){%>
								<bean:message key="protocolAction.label.rda" /> <%}else if(actionCode.equals("115")){%>
								<bean:message key="protocolAction.label.rtr" /> <%}else if(actionCode.equals("116")){%>
								<bean:message key="protocolAction.label.nirb" /> <%}else if(actionCode.equals("205")){%>
								<bean:message key="protocolAction.label.ea" /> <%}else if(actionCode.equals("208")){%>
								<bean:message key="protocolAction.label.ra" /> <%}else if(actionCode.equals("305")){%>
								Expire Protocol <%}else if(actionCode.equals("303")){%> <bean:message
									key="protocolAction.303.label" /> <%}%>

							</td>
							<td align="right"><a id="helpPageText"
								href="javascript:showBalloon('helpPageText', 'helpText')"> <bean:message
										key="helpPageTextProtocol.help" />
							</a></td>
						</tr>
					</table>
				</td>
			</tr>

			<%}%>

			<tr class="copy">
				<td align="left" width='99%'><font color="red"> <logic:messagesPresent>
						<html:errors header="" footer="" />
						<script>errValue = true;</script>
					</logic:messagesPresent> <logic:messagesPresent message="true">

						<tr>
							<td align="left" valign="top" class='core'>
								<table width="100%" border="0" align="center" cellpadding="0"
									cellspacing="0" class="tabtable">
									<%--
                            Show the extra row only 
                            1. For normal errors (If an approval action validation error occurs, hide it)
                            2. And when an action fails                            
                        --%>
									<%if(approvalActionErrorFlag.equals("N") && successFlag.equals("N")){%>
									<tr>
										<td height='40'></td>
									</tr>
									<%}%>
									<tr>
										<td height='20' colspan='4' class='copy'><font
											color="red"> <html:messages id="message"
												message="true" property="errMsg">
												<script>errLock = true;</script>
												<li><bean:write name="message" /></li>
											</html:messages> <html:messages id="message" message="true"
												property="protocolAction_exceptionCode.2007">
												<script>errValue = true;</script>
												<bean:message key="protocolAction_exceptionCode.2007" />
											</html:messages> <html:messages id="message" message="true"
												property="protocolAction_exceptionCode.2008">
												<script>errValue = true;</script>
												<bean:message key="protocolAction_exceptionCode.2008" />
											</html:messages> <html:messages id="message" message="true"
												property="protocolAction_exceptionCode.2018">
												<script>errValue = true;</script>
												<li><bean:message
														key="protocolAction_exceptionCode.2018" /></li>
											</html:messages> <html:messages id="message" message="true"
												property="protocolAction_exceptionCode.2200">
												<script>errValue = true;</script>
												<li><bean:write name="message" /></li>
											</html:messages> <html:messages id="message" message="true"
												property="protocolAction_exceptionCode.2031">
												<script>errValue = true;</script>
												<bean:message key="protocolAction_exceptionCode.2031" />
											</html:messages> <html:messages id="message" message="true"
												property="protocolAction_exceptionCode.2032">
												<script>errValue = true;</script>
												<bean:message key="protocolAction_exceptionCode.2032" />
											</html:messages> <html:messages id="message" message="true"
												property="protocolAction_exceptionCode.2024">
												<script>errValue = true;</script>
												<bean:message key="protocolAction_exceptionCode.2024" />
											</html:messages> <html:messages id="message" message="true"
												property="protocolAction_exceptionCode.2047">
												<script>errValue = true;</script>
												<bean:message key="protocolAction_exceptionCode.2047" />
											</html:messages> <html:messages id="message" message="true"
												property="protocolAction_exceptionCode.2048">
												<script>errValue = true;</script>
												<bean:message key="protocolAction_exceptionCode.2048" />
											</html:messages> <html:messages id="message" message="true"
												property="protocolAction_exceptionCode.2049">
												<script>errValue = true;</script>
												<bean:message key="protocolAction_exceptionCode.2049" />
											</html:messages> <html:messages id="message" message="true"
												property="protocolAction_exceptionCode.2050">
												<script>errValue = true;</script>
												<bean:message key="protocolAction_exceptionCode.2050" />
											</html:messages> <html:messages id="message" message="true"
												property="protocolAction_exceptionCode.2044">
												<script>errValue = true;</script>
												<bean:message key="protocolAction_exceptionCode.2044" />
											</html:messages> <html:messages id="message" message="true"
												property="protocolAction_exceptionCode.2014">
												<script>errValue = true;</script>
												<bean:message key="protocolAction_exceptionCode.2014" />
											</html:messages> <html:messages id="message" message="true"
												property="protocolAction_exceptionCode.2027">
												<script>errValue = true;</script>
												<bean:message key="protocolAction_exceptionCode.2027" />
											</html:messages> <html:messages id="message" message="true"
												property="protocolAction_exceptionCode.2045">
												<script>errValue = true;</script>
												<bean:message key="protocolAction_exceptionCode.2045" />
											</html:messages> <html:messages id="message" message="true"
												property="protocolAction_exceptionCode.2028">
												<script>errValue = true;</script>
												<bean:message key="protocolAction_exceptionCode.2028" />
											</html:messages> <html:messages id="message" message="true"
												property="protocolAction_exceptionCode.2046">
												<script>errValue = true;</script>
												<bean:message key="protocolAction_exceptionCode.2046" />
											</html:messages> <html:messages id="message" message="true"
												property="protocolAction_exceptionCode.date1">
												<script>errValue = true;</script>
												<li><bean:message
														key="protocolAction_exceptionCode.date1" /></li>
											</html:messages> <html:messages id="message" message="true"
												property="protocolAction_exceptionCode.date2">
												<script>errValue = true;</script>
												<li><bean:message
														key="protocolAction_exceptionCode.date2" /></li>
											</html:messages> <html:messages id="message" message="true"
												property="protocolAction_exceptionCode.date3">
												<script>errValue = true;</script>
												<li><bean:message
														key="protocolAction_exceptionCode.date3" /></li>
											</html:messages> <html:messages id="message" message="true"
												property="protocolAction_exceptionCode.date4">
												<script>errValue = true;</script>
												<li><bean:message
														key="protocolAction_exceptionCode.date4" /></li>
											</html:messages> <html:messages id="message" message="true"
												property="protocolAction_exceptionCode.2065">
												<script>errValue = true;</script>
												<li><bean:message
														key="protocolAction_exceptionCode.2065" /></li>
											</html:messages> <html:messages id="message" message="true"
												property="protocolAction_exceptionCode.2034">
												<script>errValue = true;</script>
												<li><bean:message
														key="protocolAction_exceptionCode.2034" /></li>
											</html:messages> <html:messages id="message" message="true"
												property="protocolAction_exceptionCode.2038">
												<script>errValue = true;</script>
												<li><bean:message
														key="protocolAction_exceptionCode.2038" /></li>
											</html:messages> <html:messages id="message" message="true"
												property="protocolAction_exceptionCode.2000">
												<script>errValue = true;</script>
												<li><bean:message
														key="protocolAction_exceptionCode.2000" /></li>
											</html:messages> <html:messages id="message" message="true"
												property="protocolAction_exceptionCode.2012">
												<script>errValue = true;</script>
												<li><bean:message
														key="protocolAction_exceptionCode.2012" /></li>
											</html:messages> <html:messages id="message" message="true"
												property="protocolAction_exceptionCode.2033">
												<script>errValue = true;</script>
												<li><bean:message
														key="protocolAction_exceptionCode.2033" /></li>
											</html:messages> <html:messages id="message" message="true"
												property="protocolAction_exceptionCode.2035">
												<script>errValue = true;</script>
												<li><bean:message
														key="protocolAction_exceptionCode.2035" /></li>
											</html:messages> <html:messages id="message" message="true"
												property="protocolAction_exceptionCode.2036">
												<script>errValue = true;</script>
												<li><bean:message
														key="protocolAction_exceptionCode.2036" /></li>
											</html:messages> <html:messages id="message" message="true"
												property="protocolAction_exceptionCode.2037">
												<script>errValue = true;</script>
												<li><bean:message
														key="protocolAction_exceptionCode.2037" /></li>
											</html:messages> <html:messages id="message" message="true"
												property="protocolAction_exceptionCode.2100">
												<script>errValue = true;</script>
												<li><bean:message
														key="protocolAction_exceptionCode.2100" /></li>
											</html:messages> <html:messages id="message" message="true"
												property="lockDeleted">
												<script>errLock = true;</script>
												<li><bean:write name="message" /></li>

											</html:messages> <html:messages id="message" message="true"
												property="actionDateRequired">
												<script>errValue = true;</script>
												<li><bean:write name="message" /></li>

											</html:messages> <html:messages id="message" message="true"
												property="invalidActionDate">
												<script>errValue = true;</script>
												<li><bean:write name="message" /></li>

											</html:messages> </font></td>
									</tr>
									<%--
                            Show the extra row only 
                            1. For normal errors (If an approval action validation error occurs, hide it)
                            2. And when an action fails                           
                        --%>
									<%if(approvalActionErrorFlag.equals("N") && successFlag.equals("N")){%>
									<tr>
										<td height='40'></td>
									</tr>
									<%}%>
								</table>
							</td>
						</tr>
						<%-- 
                            The OK button is NOT displayed
                            1. when an approval action validation error occurs (cause on approval action validation error, both the error message and the controls are dispalyed)
                            2. when a normal action performed is not a success
                        --%>
						<%if(approvalActionErrorFlag.equals("N") && successFlag.equals("N")){%>
						<tr>
							<td align='center' class='savebutton'><html:button
									value="OK" property="ok" styleClass="clsavebutton"
									onclick="showGeneralInfoPage()" /></td>
						</tr>
						<%}%>

					</logic:messagesPresent> </font></td>
			</tr>

			<%-- 
            If there are no errors, then all the controls are displayed on the view (JSP)
            else they are hidden and only the appropriate error message is shown
        --%>
			<%if(error.equals("N")){%>
			<tr>
				<td align="left" valign="top" class='core'>
					<table width="100%" border="0" align="center" cellpadding="1"
						cellspacing="0" class="tabtable">
						<tr height='5'>
						</tr>
						<%if(actionCode.equals("116")){%>
						<tr valign=middle>
							<td align="left" class="copybold" width='15%'>Notification
								Type:</td>
							<td align="left"><html:select property="notificationType"
									styleClass="textbox-long" onchange="dataChanged()">
									<html:option value="">
										<bean:message key="generalInfoLabel.pleaseSelect" />
									</html:option>
									<html:options collection="QUALIFIER_TYPE" property="code"
										labelProperty="description" />
								</html:select></td>
						</tr>
						<%} else {%>
						<html:hidden property="notificationType" />
						<%}%>

						<%if(actionCode.equals("105") || actionCode.equals("106") || actionCode.equals("108") || actionCode.equals("114") || actionCode.equals("115") 
                            || (actionCode.equals("116") && session.getAttribute("IRB_COMM_SELECTION_DURING_SUBMISSION") != null
                            && !session.getAttribute("IRB_COMM_SELECTION_DURING_SUBMISSION").equals("D"))){%>
						<tr valign=middle>
							<td align="left" class="copybold"><bean:message
									key="protocolAction.label.committee" />:</td>
							<td align="left"><html:select property="committeeId"
									styleClass="textbox-long" onchange="setCommitteeId(this)">
									<html:option value="">
										<bean:message key="generalInfoLabel.pleaseSelect" />
									</html:option>
									<html:options collection="committees" property="code"
										labelProperty="description" />
								</html:select> <script>
                                committeeId = document.protocolActionsForm.committeeId.value;
                            </script></td>
						</tr>
						<%}%>

						<%if(actionCode.equals("205") || actionCode.equals("208")){%>
						<tr>
							<td align="left" class="copybold"><bean:message
									key="protocolAction.label.approvalDate" />:</td>
							<td><html:text name="protocolActionsForm"
									property="approvalDate" styleClass="textbox" size="10"
									maxlength="10" onchange="dataChanged()" /> &nbsp; <html:link
									href="javascript:void(1)"
									onclick="displayCalendarWithTopLeft('approvalDate',-7,25)"
									tabindex="32">
									<img id="imgIRBDate" title="" height="16" alt=""
										src="<bean:write name='ctxtPath'/>/coeusliteimages/cal.gif"
										width="16" border="0" runat="server">
								</html:link> &nbsp;</td>
						</tr>
						<tr>
							<td align="left" class="copybold"><bean:message
									key="protocolAction.label.expirationDate" />:</td>
							<td><html:text name="protocolActionsForm"
									property="expirationDate" styleClass="textbox" size="10"
									maxlength="10" onchange="dataChanged()" /> &nbsp; <html:link
									href="javascript:void(2)"
									onclick="displayCalendarWithTopLeft('expirationDate',-7,25)"
									tabindex="32">
									<img id="imgIRBDate" title="" height="16" alt=""
										src="<bean:write name='ctxtPath'/>/coeusliteimages/cal.gif"
										width="16" border="0" runat="server">
								</html:link> &nbsp;</td>
						</tr>
						<%}%>

						<tr>
							<td align="left" class="copybold"><bean:message
									key="protocolAction.label.comments" />:</td>
							<td><html:textarea name="protocolActionsForm"
									styleClass="copy" property="comments" cols="120" rows="5"
									onchange="dataChanged()" /></td>
						</tr>
						<tr>
							<td align="left" class="copybold"><bean:message
									key="protocolAction.label.actionDate" />:</td>
							<td><html:text name="protocolActionsForm"
									property="actionDate" styleClass="textbox" size="10"
									maxlength="10" onchange="dataChanged()" /> <logic:empty
									name="protocolActionsForm" property="actionDate">
									<%  String tempSubmissionSaved = (String) session.getAttribute("tempSubmissionSaved");
                                if(!CoeusLiteConstants.YES.equalsIgnoreCase(tempSubmissionSaved)){ %>
									<logic:messagesNotPresent message="actionDateRequired">
										<script>
                                    document.protocolActionsForm.actionDate.value = '<%=strCurrentDate%>';
                                </script>
									</logic:messagesNotPresent>
									<%}%>
								</logic:empty> &nbsp; <html:link href="javascript:void(0)"
									onclick="displayCalendarWithTopLeft('actionDate',8,25)"
									tabindex="32">
									<img id="imgIRBDate" title="" height="16" alt=""
										src="<bean:write name='ctxtPath'/>/coeusliteimages/cal.gif"
										width="16" border="0" runat="server">
								</html:link> &nbsp;</td>
						</tr>
						<tr height='5'>
						</tr>

						<%if(actionCode.equals("116") // Notify IRB  
                          || actionCode.equals("105")    // Request To Close
                          || actionCode.equals("106")   // Request For Suspension
                          || actionCode.equals("108")     // Request to Close Enrollment
                          || actionCode.equals("114")   // Request for Data Analysis Only
                          || actionCode.equals("115")     // Request to Re-open Enrollment
                          ){%>

						<td colspan="2">
							<table width="100%" class="tabtable" cellspacing="2">
								<tr>
									<td></td>
								</tr>
								<tr>
									<td>

										<table width="100%" class="tabtable">

											<tr class='tableheader'>
												<td colspan="2">
													<div id='header_label' id='header_label'>
														<bean:message key="protocolAction.label.addAttachment" />
													</div>
												</td>
												<td></td>
											</tr>
											<tr height='5'>

											</tr>
											<tr>
												<td align="left" class="copybold" width='15%'><bean:message
														key="protocolAction.label.description" />:</td>
												<td><html:text property="description"
														styleClass="textbox-longer" maxlength="250"
														onchange="attachmentFieldModified();dataChanged();" /></td>
											</tr>

											<tr>
												<td align="left" class="copybold"><bean:message
														key="protocolAction.label.Attachment" />:</td>
												<td><html:file property="document" maxlength="300"
														size="50" accept="" onchange="attachmentFieldModified()" />

												</td>



											</tr>
											<tr>
												<td>&nbsp;</td>
												<td><html:text property="fileName"
														style="width: 400px;" styleClass="cltextbox-color"
														disabled="false" readonly="true" /></td>



											</tr>

											<tr>
												<td align="left" class="copybold" colspan="2"><html:button
														value="Save" property="save" styleClass="clsavebutton"
														onclick="validate_and_save_upload_form()" /> &nbsp; <html:button
														property="Cancel" value="Cancel" styleClass="clsavebutton"
														onclick="confirmAttachmentCancel()" /></td>



											</tr>

											<tr class='tableheader'>
												<td><bean:message
														key="protocolAction.label.attachments" />:</td>
												<td></td>
											</tr>
											<tr>
												<td colspan="2">
													<table class="table" width="100%" cellspacing="0"
														cellpadding="2">
														<logic:empty name="protocolActionsForm"
															property="vecAttachments">
															<tr class='tableheader'>
																<td colspan="4"><bean:message
																		key="protocolAction.msg.noAttachmentsAdded" /></td>

															</tr>
														</logic:empty>
														<logic:notEmpty name="protocolActionsForm"
															property="vecAttachments">
															<tr class='tableheader'>
																<td><bean:message
																		key="protocolAction.label.description" /></td>
																<td><bean:message
																		key="protocolAction.label.lastUpdated" /></td>
																<td><bean:message
																		key="protocolAction.label.updatedBy" /></td>
																<td>&nbsp;</td>
															</tr>
															<tr>
																<% 
                                            String strBgColor = "#DCE5F1"; //BGCOLOR=EFEFEF  FBF7F7
                                            int count = 0;
                                            %>

																<logic:iterate id="attachments"
																	name="protocolActionsForm" property="vecAttachments"
																	type="edu.mit.coeus.irb.bean.ProtocolActionDocumentBean">

																	<% 
                                                    if (count%2 == 0)
                                                    strBgColor = "#D6DCE5";
                                                    else
                                                    strBgColor="#DCE5F1";
                                                    
                                                    %>
																	<tr align="left" class="copy" bgcolor="<%=strBgColor%>"
																		onmouseover="className='TableItemOn'"
																		onmouseout="className='TableItemOff'">
																		<tr align="left" class="copy"
																			bgcolor="<%=strBgColor%>"
																			onmouseover="className='TableItemOn'"
																			onmouseout="className='TableItemOff'">
																			<td width='38%' class="copy"><%=attachments.getDescription()%>
																			</td>
																			<td class="copy" width='20%'>
																				<%
                                                            timeStamp = attachments.getUpdateTimestamp();
                                                            if(timeStamp != null && !timeStamp.equals("")){
                                                            updateTimeStamp = dateFormat.format(timeStamp);
                                                            }
                                                            %> <%=updateTimeStamp%>
																			</td>
																			<td class='copy' width='20%'><%=attachments.getUpdateUserName()%>
																			</td>
																			<td class='copy' witdth='22%'>
																				<%String viewLink =  "javascript:view_attachment('"+attachments.getDocumentId() +"')";%>
																				<html:link href="<%=viewLink%>">
                                                                View
                                                            </html:link> &nbsp;
																				&nbsp; &nbsp; <%String modifyLink = "javascript:modify_attachment('"+attachments.getDocumentId() +"','M')";%>
																				<html:link href="<%=modifyLink%>">
                                                                Modify
                                                            </html:link> &nbsp;
																				&nbsp; &nbsp; <%String removeLink = "javascript:modify_attachment('"+attachments.getDocumentId() +"','D')";%>
																				<html:link href="<%=removeLink%>">
                                                                Remove
                                                            </html:link>
																			</td>
																		</tr>
																		<% count++;%>
																	
																</logic:iterate>
														</logic:notEmpty>
														</tr>
													</table>
												</td>
											</tr>
										</table> <%--   </div> --%>
									</td>
								</tr>
								</td>

							</table> <%}%>

						</td>
						</tr>
					</table>

					<tr class="table">
						<td class='savebutton'>
							<%--Modified for Case# COEUSQA-2522_4.4.1 Submit to IRB "Submit" Button Inactive-Start --%>
							<%--html:button property="Save" value="Submit" styleClass="clsavebutton" onclick="saveActions()"--%>
							<html:button property="Save" value="Save Submission Details"
								style="width:200px;" styleClass="clsavebutton"
								onclick="saveActions()">
								<bean:message key="protocolAction.button.save" />
							</html:button> <%--Modified for Case# COEUSQA-2522_4.4.1 Submit to IRB "Submit" Button Inactive-End --%>

						</td>
					</tr> <%}%>
				
		</table>
		<html:hidden property="acType" />
		<html:hidden property="documentId" />
		<html:hidden property="oldNotificationType" />
	</html:form>

	<script>
            DATA_CHANGED = 'false';
            if(errValue) {
                DATA_CHANGED = 'true'; 
            }
            LINK = "<%=request.getContextPath()%>/saveTempProtocolActions.do?committeeId="+committeeId;
            FORM_LINK = document.protocolActionsForm;
            PAGE_NAME = "<bean:message key="protocolAction.label.header"/>";
            function dataChanged(){
                DATA_CHANGED = 'true';
                LINK = "<%=request.getContextPath()%>/saveTempProtocolActions.do?committeeId="+committeeId;
            }
            linkForward(errValue);
            
            
            
           <%if(error.equals("N")){%>
            <%if(!actionCode.equals("303")){%>
           change_attachment_label();
           
           <%}%>
           
           <%if(actionCode.equals("116")){%>
            document.protocolActionsForm.notificationType.focus();
           <%} else {%> 
            document.protocolActionsForm.committeeId.focus();
           <%}%>
           document.protocolActionsForm.oldNotificationType.value = savedNotifType;
           <%}%>
        </script>

</body>
</html:html>
