<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="iacucOtherAttachments" scope="session"
	class="edu.mit.coeus.utils.CoeusVector" />
<%--Added for COEUSQA-3461 : IACUC CoeusLite View FYI Attachments - start--%>
<jsp:useBean id="attachmentDetails" scope="session"
	class="java.util.Vector" />
<%--Added for COEUSQA-3461 : IACUC CoeusLite View FYI Attachments - end--%>
<html:html>
<%
    //Added for COEUSQA-3461 : IACUC CoeusLite View FYI Attachments- start
    String actionDate = "";
    int docId = 0;
    //Added for COEUSQA-3461 : IACUC CoeusLite View FYI Attachments - end
    %>
<head>
<title>Other Attachments</title>
</head>
<script language="javascript" type="text/JavaScript"
	src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/sorttable.js"></script>
<script>
            function viewDocument(val){
                window.open("<%=request.getContextPath()%>/IacucViewAttachment.do?otherAttchId="+val)                
            }
            
            //Added for COEUSQA-3461 : IACUC CoeusLite View FYI Attachments - start
            function viewNotiAttach(sequenceNumber,submissionNumber,documentId,protocolNumber){
              window.open("<%=request.getContextPath()%>/viewIacucNotificationAttachments.do?&SeqNumber="+sequenceNumber+"&submissionNumber="+submissionNumber+"&documentId="+documentId+"&protocolNumber="+protocolNumber); 
            }            
            //Added for COEUSQA-3461 : IACUC CoeusLite View FYI Attachments - end
        </script>
<html:form action="/getIacucAllOtherAttachments.do" method="POST">
	<body>
		<table cellpadding="3" cellspacing="0" border="0" width="100%"
			class="table">
			<!-- JM 5-31-2011 updated class per 4.4.2 -->
			<tr>
				<td height="30" class="tableheader"><bean:message
						key="otherAttachLabel.Header" /></td>
			</tr>
			<tr>
				<td></td>
			</tr>
			<tr>
				<td>
					<div id="docPresent" style="display: block">
						<table cellpadding="2" cellspacing="1" border="0" width="100%"
							class="tabtable">
							<!-- JM 5-31-2011 updated class per 4.4.2 -->
							<tr>
								<td height="20" colspan="5" class="tableheader"><bean:message
										key="otherAttachLabel.Title" bundle="iacuc" /></td>
							</tr>
							<tr>
								<td height="7" colspan="5"></td>
							</tr>
							<tr>
								<td>
									<table cellpadding="2" cellspacing="1" border="0" width="100%"
										id="t1" class="sortable">
										<tr>
											<td width="20%" align="left" class="theader"><bean:message
													key="otherAttachLabel.Type" bundle="iacuc" /></td>
											<td width="35%" align="left" class="theader"><bean:message
													key="otherAttachLabel.Description" bundle="iacuc" /></td>
											<td width="20%" align="left" class="theader"><bean:message
													key="otherAttachLabel.Timestamp" bundle="iacuc" /></td>
											<td width="20%" align="left" class="theader"><bean:message
													key="otherAttachLabel.UpdateUser" bundle="iacuc" /></td>
											<td width="5%" align="left" class="theader">&nbsp;</td>
										</tr>
										<%String bgColor= "#DCE5F1";int count=0;%>
										<logic:notEmpty name="iacucOtherAttachments">
											<logic:iterate id="otherAttachments"
												type="edu.mit.coeus.iacuc.bean.UploadDocumentBean"
												scope="session" indexId="index" name="iacucOtherAttachments">
												<%if(count%2 == 0)
                                                    bgColor = "#D6DCE5";
                                                    else
                                                    bgColor = "#DCE5F1";                                    
                                                    String docType = otherAttachments.getDocType();
                                                    String description = otherAttachments.getDescription();
                                                    String updateTimeStamp = otherAttachments.getUpdateTimestamp() == null ? "" :
                                                    otherAttachments.getUpdateTimestamp().toString();
                                                    ////Added for COEUSDEV-323 :  Lite - IRB - remove 'Add New Document' link from when user is modifying an attachment - Start
                                                    if(updateTimeStamp != null || !updateTimeStamp.equals("")){
                                                    java.sql.Timestamp timeStamp = java.sql.Timestamp.valueOf(updateTimeStamp);
                                                    java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("MM/dd/yyyy hh:mm a");
                                                    updateTimeStamp = dateFormat.format(timeStamp);
                                                    }
                                                    //COEUSDEV-323 : End
                                                    String updateUser = otherAttachments.getUpdateUserName();
                                                    //int docId = otherAttachments.getDocumentId();
                                                    %>
												<tr bgcolor="<%=bgColor%>"
													onmouseover="className='TableItemOn'"
													onmouseout="className='TableItemOff'">
													<td align="left" class='copy'><%=docType%></td>
													<td align="left" class='copy'><%=description%></td>
													<td align="left" class='copy'><%=updateTimeStamp%></td>
													<td align="left" class='copy'><%=updateUser%></td>
													<%String link = "javascript:viewDocument('"+otherAttachments.getDocumentId()+"')";%>
													<td align="left" class='copy'><html:link
															href="<%=link%>">View</html:link></td>
												</tr>
												<%count++;%>
											</logic:iterate>
										</logic:notEmpty>
									</table>
								</td>
							</tr>
						</table>
					</div>
				</td>
			</tr>
			<tr>
				<td width="100%" height="100%"><logic:empty
						name="iacucOtherAttachments">
						<div id="noDocPresent" style="display: block">
							<font color="red"><b>
									<%--No Attachments Found --%>
							</b></font>
						</div>
					</logic:empty></td>
			</tr>

			<!--Added for COEUSQA-3461 : IACUC CoeusLite View FYI Attachments - start-->
			<tr align="center">
				<table width="100%" border="0" cellpadding="0" cellspacing="0"
					class="tabtable">

					<logic:notEmpty name="attachmentDetails">
						<tr align="left">
							<td width="100%" class="theader" colspan="10"><bean:message
									key="protocolAction.label.AttachmentFromNotification" /></td>
						</tr>
						<tr align="left">
							<table cellpadding="2" cellspacing="1" border="0" width="100%"
								id="t1" class="sortable">
								<tr>
									<td width="20%" class="theader"><bean:message
											key="attchmentFromNotification.Action" /></td>
									<td width="20%" class="theader"><bean:message
											key="attchmentFromNotification.date" /></td>
									<td width="20%" class="theader"><bean:message
											key="attchmentFromNotification.actionDate" /></td>
									<td width="35%" class="theader"><bean:message
											key="attchmentFromNotification.description" /></td>
									<td width="5%" align="left" class="theader">&nbsp;</td>
								</tr>
								<logic:iterate id="notificationAttachments"
									name="attachmentDetails"
									type="edu.mit.coeus.iacuc.bean.ProtocolActionsBean">

									<tr bgColor="#DCE5F1" width="15%" valign="top"
										onmouseover="className='TableItemOn'"
										onmouseout="className='TableItemOff'">

										<td width="20%" align="left" nowrap class="copy"><%=notificationAttachments.getActionTypeDescription()%></td>
										<%   
                                        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("MM/dd/yyyy hh:mm a");
                                        java.sql.Timestamp timeStamp;
                                        timeStamp = notificationAttachments.getUpdateTimestamp();
                                        if(timeStamp != null && !timeStamp.equals("")){
                                            actionDate = dateFormat.format(timeStamp);
                                        }
                                        %>
										<td width="20%" align="left" nowrap class="copy"><%=actionDate%></td>
										<td width="20%" align="left" nowrap class="copy"><%=actionDate%></td>
										<td width="20%" align="left" nowrap class="copy"><%=notificationAttachments.getProtocolActionDocumentDescription()%></td>

										<% if(notificationAttachments.getProtocolActionDocumentBean() != null) {
                                            edu.mit.coeus.iacuc.bean.ProtocolActionDocumentBean docBean = (edu.mit.coeus.iacuc.bean.ProtocolActionDocumentBean)notificationAttachments.getProtocolActionDocumentBean();
                                            docId = docBean.getDocumentId();
                                        }
                                        %>

										<%String link = "javascript:viewNotiAttach('"+notificationAttachments.getSequenceNumber()+"','"+notificationAttachments.getSubmissionNumber()+"','"+docId+"','"+notificationAttachments.getProtocolNumber()+"')";%>
										<td align="left" class='copy'><html:link href="<%=link%>">
												<bean:message key="label.View" />
											</html:link></td>

									</tr>

								</logic:iterate>
								</logic:notEmpty>
							</table>
						</tr>
				</table>
			</tr>
			<!--Added for COEUSQA-3461 : IACUC CoeusLite View FYI Attachments - end-->

		</table>
	</body>
</html:form>
</html:html>