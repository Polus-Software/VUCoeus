<? xml version="1.0" ?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page
	import="org.apache.struts.taglib.html.JavascriptValidatorTag,
                edu.mit.coeuslite.utils.CoeusLiteConstants"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="uploadLatestData" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="historyData" scope="request" class="java.util.Vector" />
<jsp:useBean id="parentProtoData" scope="session"
	class="java.util.Vector" />
<html:html locale="true">
<%Integer historyDocId = (Integer) request.getAttribute("historyDataDocId");
String protoNum = request.getParameter("protocolNumber");
java.util.HashMap hmFromUsers = (java.util.HashMap)session.getAttribute("hmFromUsers");
hmFromUsers = (hmFromUsers==null)?new java.util.HashMap() : hmFromUsers;
String user = ""; 
//Added for COEUSDEV-323 :  Lite - IRB - remove 'Add New Document' link from when user is modifying an attachment - Start
//To change update timestamp time into 12 hrs format
java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("MM/dd/yyyy hh:mm a");
java.sql.Timestamp timeStamp; 
String updateTimeStamp = "";
//COEUSDEV-323 : end
//COEUSQA-2530_Allow users to add an attachment for a renewal_Start
boolean isInRenewalStatus = false;
if(session.getAttribute("setAttachmentModifyForRenewal") != null){
    isInRenewalStatus = (Boolean)session.getAttribute("setAttachmentModifyForRenewal");
}
//COEUSQA-2530_Allow users to add an attachment for a renewal_End
%>
<head>
<title>MIT- IRB</title>
<script language="javascript" type="text/JavaScript"
	src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/sorttable.js"></script>
<script language="JavaScript" type="text/JavaScript">
            function insert_data(){
                document.uploadDocForm.docType.disabled=false;
                document.uploadDocForm.fileName.disabled=false;
                document.uploadDocForm.action = "<%=request.getContextPath()%>/uploadAction.do";
                document.uploadDocForm.Save.disabled=true;//Added for case 4267-Double clicking save results in error
                document.uploadDocForm.submit();
            
            }
            /* Added for COEUSDEV-120  Lite - protocol attachments throws an error on large attachments on second attempt - Start */
            function validate_upload_form(){
                var isValid = true;
                var description = document.uploadDocForm.description.value;
                description = description.replace(/^\s+|\s+$/, '');
                var descriptionLength = description.length;
                document.uploadDocForm.description.value = description;
                if(document.uploadDocForm.docType.value == 0 || document.uploadDocForm.docType.value < 1){
                    alert("<bean:message key="error.UploadDoc.documentTypeMandatory"/>");
                    document.uploadDocForm.docType.focus();
                    isValid = false;
                }else if(descriptionLength < 1){
                  alert("<bean:message key="error.UploadDoc.documentDescriptionMandatory"/>");
                  document.uploadDocForm.description.focus();
                  isValid = false;
                }
                if(isValid){
                    insert_data();
                }
            }
            /* COEUSDEV-120 - End */
            
            var errValue = false;
            var errLock = false;
            function delete_data(activityType,docType,versionNumber,documentId,status,seqNum){
                document.uploadDocForm.documentId.value = documentId;
                if (confirm("Are you sure you want to delete the document?")==true){
                document.uploadDocForm.action = "<%=request.getContextPath()%>/updDeleteUploadAction.do?docType="+docType+"&acType=D&versionNumber="+versionNumber+"&status="+status+"&seqNum="+seqNum;;
                document.uploadDocForm.submit();
                }
                
            }

            /* Modified for Case#3036 - Didn't recognize document file type in Protocol attachments  -Start
            function view_data(activityType,docType,versionNumber, sequenceNumber, isParent){*/
            function view_data(activityType,docType,versionNumber, sequenceNumber,documentId, isParent){ 
            //Modified for Case#3036 - Didn't recognize document file type in Protocol attachments - End
                //window.open("<%=request.getContextPath()%>/viewUploadAction.do?&docType="+docType+"&acType=V&versionNumber="+versionNumber+"&SeqNumber="+sequenceNumber+"&Parent="+isParent); 
                window.open("<%=request.getContextPath()%>/viewUploadAction.do?&docType="+docType+"&acType=V&versionNumber="+versionNumber+"&SeqNumber="+sequenceNumber+"&Parent="+isParent+"&protocDocId="+documentId); 
            }
            
            function history(value){
            if(value == 1 ){
                         document.getElementById('showAll').style.display = 'block';
                         document.getElementById('showLink').style.display = 'none';
                         document.getElementById('hideLink').style.display = 'block';
                         
             }
            if(value == 2 ){
                         document.getElementById('showAll').style.display = 'none';
                         document.getElementById('showLink').style.display = 'block';
                         document.getElementById('hideLink').style.display = 'none';
             }
            }
            
            function trim(s) {
            return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
        }
            function update_data(docCode, versionNumber, documentId, parent){
                document.uploadDocForm.acType.value = 'E';
                document.uploadDocForm.versionNumber.value = versionNumber;
                document.uploadDocForm.docCode.value = docCode;
                document.uploadDocForm.documentId.value = documentId;
                document.uploadDocForm.parent.value = parent;
                document.uploadDocForm.action = "<%=request.getContextPath()%>/updDeleteUploadAction.do";
                document.uploadDocForm.submit();
            }
            
            function history_data(docCode, versionNumber, documentId, protocolNumber, showAll){
                <%if(historyDocId!=null){%>
                    if(<%=historyDocId.intValue()%>==documentId && '<%=protoNum%>'==protocolNumber){
                        protocolNumber = '';
                    }
                <%}%>
                document.uploadDocForm.acType.value = 'H';
                document.uploadDocForm.versionNumber.value = versionNumber;
                document.uploadDocForm.docCode.value = docCode;
                document.uploadDocForm.documentId.value = documentId;
                if(showAll == 'showAll')
                    document.uploadDocForm.action = "<%=request.getContextPath()%>/updDeleteUploadAction.do?protocolNumber="+protocolNumber+"&showAll=showAll";
                else
                    document.uploadDocForm.action = "<%=request.getContextPath()%>/updDeleteUploadAction.do?protocolNumber="+protocolNumber;
                document.uploadDocForm.submit();            
            }
            
            function add_documents(){                
                document.uploadDocForm.docType.disabled=false;
                document.uploadDocForm.fileName.disabled=false;                
                if(validate() == true){
                    document.uploadDocForm.action = "<%=request.getContextPath()%>/getProtocolData.do?PAGE=U";
                    document.uploadDocForm.submit();
                }
            }
            //Added for the case # COEUSQA-2353-Need to a better way to handle large numbers of protocol attachments-start
            function showAddDocuments(){
                 document.getElementById("showAddDocument").style.display="block";  
                 document.getElementById("header_label").style.display="none";                  
                 //Added for case #COEUSQA-2507_Cannot upload protocol amendment - Start
                 document.getElementById("addDoc_label").style.display="block";  
                 //Added for case #COEUSQA-2507_Cannot upload protocol amendment - End
                 document.uploadDocForm.docType.focus();
            }
            //Added for the case # COEUSQA-2353-Need to a better way to handle large numbers of protocol attachments-end
            function showAll(value){
                document.uploadDocForm.acType.value = '';
                if(value==1)
                    document.uploadDocForm.action = "<%=request.getContextPath()%>/getProtocolData.do?PAGE=U&showAll=showAll";
                else
                    document.uploadDocForm.action = "<%=request.getContextPath()%>/getProtocolData.do?PAGE=U";
                document.uploadDocForm.submit();            
            }
            
            /*Added for Case#3533 Losing attachments - Start*/
            function selectFile(){
                dataChanged();
                document.uploadDocForm.fileName.value = document.uploadDocForm.document.value;
                document.uploadDocForm.fileNameHidden.value = document.uploadDocForm.document.value;
            }
            /*Added for Case#3533 Losing attachments - Start*/
            </script>

<%--Added for case#3035 - Insufficient Room in New/Changed Attachments List in Protocol Attachments -Start--%>
<script>
                function view_Description(val) {
                   var value;
                   if(val.length<1000)
                            {value=val; }
                   else
                             {value=val.substring(0,1000);}
                    var w = 550;
                    var h = 213;
                    if(navigator.appName == "Microsoft Internet Explorer") {
                        w = 522;
                        h = 196;
                    }
                    if (window.screen) {
                           leftPos = Math.floor(((window.screen.width - 500) / 2));
                           topPos = Math.floor(((window.screen.height - 350) / 2));
                     }

                            var loc = '<bean:write name='ctxtPath'/>/coeuslite/mit/utils/dialogs/cwViewSpecialReview.jsp?value='+value+'&type=PA';
                            var newWin = window.open(loc,"formName",'dependent=1,toolbar=0,location=0,directories=0,status=0, menubar=0,scrollbars=0,resizable=0,width=' + w + ',height=' + h+ ',left='+ leftPos + ',top=' + topPos);
                            newWin.window.focus();

                     }
            </script>
<%--Added for case#3035 - Insufficient Room in New/Changed Attachments List in Protocol Attachments -End --%>
</head>
<body>

	<% String mode=(String)session.getAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId());



            String viewLink = null;
            boolean modeValue=false;
            boolean showAmend = true;
            boolean isAttachmentEditable = false;
            if(mode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE)){
            modeValue=true;
                //Added for Case#4275 - upload attachments until in agenda - Start
                String modifyAttachmentInDisplay = (String)session.getAttribute("MODIFY_PROTOCOL_ATTACHMENT"+session.getId());
                if(modifyAttachmentInDisplay != null && modifyAttachmentInDisplay.equalsIgnoreCase(CoeusLiteConstants.MODIFY_MODE)){
                         modeValue=false;
                         isAttachmentEditable = true;
                }
                //Case#4275 - End
            }else if(mode.equalsIgnoreCase(CoeusLiteConstants.MODIFY_MODE)){
            modeValue=false;
            }else if(mode.equalsIgnoreCase(CoeusLiteConstants.ADD_MODE)){
            modeValue=false;
            }
           
        %>
	<%String protocolNo = (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
              /* if(protocolNo.indexOf("R")!=-1){
                    showAmend = false;
               }*/        
            boolean  showParent = true;
            if((protocolNo.indexOf("A") == -1) && (protocolNo.indexOf("R") == -1)){
                showParent = false;
            }
           String editImage = request.getContextPath()+"/coeusliteimages/edit.gif";
          String delImage= request.getContextPath()+"/coeusliteimages/delete.gif";
          String docDescription = "";
        //Added for coeus4.3 Amendments and Renewal enhancement 
        String amendRenewPageMode = (String)session.getAttribute("amendRenewPageMode"+request.getSession().getId());
        if(protocolNo.length() > 10 && (amendRenewPageMode == null || 
                amendRenewPageMode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE))){
                //Modified for Case#4275 - upload attachments until in agenda - Start
                //modeValue = true;
                if(!isAttachmentEditable){
                   modeValue = true;
                }
                //Case#4275 - End
        }%>

	<!-- Added for Page Help - Start-->
	<script type="text/javascript">
        document.getElementById('txt').innerHTML = '<bean:message key="helpPageTextProtocol.UploadDocuments"/>';
        </script>
	<!-- Added for Page Help - End -->

	<html:form action="/uploadAction.do" method="post"
		enctype="multipart/form-data">

		<!-- New Template for cwUploadTemplate - Start   -->


		<table width="100%" height="100%" border="0" cellpadding="0"
			cellspacing="0" class="table">

			<tr>
				<td>
					<!-- Added for Page Help - Start -->
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td height="2%" align="left" valign="top" class="theader">
								Attachments</td>
							<td height="20" align="right" valign="top" class="tableheader">
								<a id="helpPageText"
								href="javascript:showBalloon('helpPageText', 'helpText')"> <u><bean:message
											key="helpPageTextProtocol.help" /></u>
							</a>
							</td>&nbsp;&nbsp;
						</tr>
					</table> <!-- Added for Page Help - End -->
				</td>
			</tr>
			<tr>
				<td>
					<div id="helpText" class='helptext'>
						&nbsp;&nbsp;
						<bean:message key="helpTextProtocol.UploadDocuments" />
					</div>
				</td>
			</tr>
			<%-- Commented for case id#2627
        <tr>
            <td class="copybold">
                &nbsp;&nbsp;
                <font color="red">*</font> 
                <bean:message key="label.indicatesReqFields"/>
            </td>
        </tr>
        --%>
			<tr>
				<td class='copy'><font color='red'> <logic:messagesPresent>
							<script>errValue = true;</script>
							<html:errors header="" footer="" />
						</logic:messagesPresent> <logic:messagesPresent message="true">
							<html:messages id="message" message="true" property="errMsg">
								<script>errLock = true;</script>
								<li><bean:write name="message" /></li>
							</html:messages>
							<html:messages id="message" message="true" property="amendMsg">
								<script>errLock = true;</script>
								<li><bean:write name="message" /></li>
							</html:messages>
						</logic:messagesPresent>
				</font></td>
			</tr>
			<!-- Add Documents: - Start  -->
			<%--Modified for the case # COEUSQA-2353-Need to a better way to handle large numbers of protocol attachments-start--%>
			<%if(!modeValue || isInRenewalStatus){%>
			<tr>
				<td colspan="4" align="left" valign="top"><table width="100%"
						height="2%" border="0" cellpadding="0" cellspacing="0"
						class="tableheader">
						<tr>
							<td>
								<div id='header_label' id='header_label' style='display: block;'>
									<html:link href="javascript:showAddDocuments();">
										<u><bean:message key="uploadDocLabel.AddDocuments" /></u>
									</html:link>
								</div>

							</td>
						</tr>
					</table></td>
			</tr>
			<%}%>
			<%--Modified for the case # COEUSQA-2353-Need to a better way to handle large numbers of protocol attachments-end--%>
			<%--Added for case #COEUSQA-2507_Cannot upload protocol amendment - Start --%>
			<tr>
				<td colspan="4" align="left" valign="top"><table width="100%"
						height="2%" border="0" cellpadding="0" cellspacing="0"
						class="tableheader">
						<tr>
							<td>
								<div id='addDoc_label' name='addDoc_label'
									style='display: none;'>
									<bean:message key="uploadDocLabel.AddDocuments" />
								</div>

							</td>
						</tr>
					</table></td>
			</tr>
			<%--Added for case #COEUSQA-2507_Cannot upload protocol amendment - End --%>
			<tr>
				<td align="left" valign="top" class='core'>
					<div id="showAddDocument" style="display: none;">
						<table width="100%" border="0" align="center" cellpadding="0"
							cellspacing="0" class="tabtable">
							<tr>
								<td align="left" valign="top">
									<table width="100%">
										<tr>
											<td>
												<%
                                  String addLink = "javascript:add_documents()";
                                  %>
												<div id='add_label' style='display: none;'>
													&nbsp;&nbsp;&nbsp;&nbsp;
													<html:link href="<%=addLink%>">
														<u><bean:message key="uploadDocLabel.AddDocuments" /></u>
													</html:link>
												</div>
											</td>
										</tr>
									</table>
								</td>
							</tr>

							<tr>
								<td>
									<table width="100%" border="0" cellpadding="1" cellspacing="5">
										<tr>
											<td width="12%" align="left" class="copybold" nowrap>
												<%-- Commented for case id#2627
                                             &nbsp;<font color="red">*</font>
                                             --%> <bean:message
													key="uploadDocLabel.DocumentType" />:
											</td>
											<td width="30%" align="left" nowrap>
												<%--Modified DocTypes displaying
                                        Earlier no checking done if the collection DocTypes was empty
                                        (DocTypes collection is empty when there are no entry in Code Table)- Start--%>
												<logic:notEmpty name="DocTypes">
													<%if(!isInRenewalStatus){%>
                                      &nbsp;<html:select
														property="docType" disabled="<%=modeValue%>"
														onchange="dataChanged()" styleClass="textbox-long">
														<html:option value="">
															<bean:message key="specialReviewLabel.pleaseSelect" />
														</html:option>
														<html:options collection="DocTypes" property="code"
															labelProperty="description" />
													</html:select>
													<%}else{%>
                                      &nbsp;<html:select
														property="docType" disabled="<%=!isInRenewalStatus%>"
														onchange="dataChanged()" styleClass="textbox-long">
														<html:option value="">
															<bean:message key="specialReviewLabel.pleaseSelect" />
														</html:option>
														<html:options collection="DocTypes" property="code"
															labelProperty="description" />
													</html:select>
													<%}%>
												</logic:notEmpty> <logic:empty name="DocTypes">
													<%if(!isInRenewalStatus){%>
                                      &nbsp;<html:select
														property="docType" disabled="<%=modeValue%>"
														styleClass="textbox-long">
														<html:option value="">
															<bean:message key="specialReviewLabel.pleaseSelect" />
														</html:option>
													</html:select>
													<%}else{%>
                                      &nbsp;<html:select
														property="docType" disabled="<%=!isInRenewalStatus%>"
														styleClass="textbox-long">
														<html:option value="">
															<bean:message key="specialReviewLabel.pleaseSelect" />
														</html:option>
													</html:select>
													<%}%>
												</logic:empty> <%--Modified DocTypes displaying - End --%>
											</td>
											<td width="15%" align=right nowrap class="copybold">
												<%-- Commented for case id#2627
                                            <font color="red">*</font>
                                            --%> <bean:message
													key="uploadDocLabel.Description" />:
											</td>
											<%if(!isInRenewalStatus){%>
											<td width='43%' align=left>&nbsp;<html:text
													property="description" maxlength="200"
													styleClass="textbox-long" style="width:290px;"
													disabled="<%=modeValue%>" readonly="false"
													onchange="dataChanged()" /></td>
											<%}else{%>
											<td width='43%' align=left>&nbsp;<html:text
													property="description" maxlength="200"
													styleClass="textbox-long" style="width:290px;"
													disabled="<%=!isInRenewalStatus%>" readonly="false"
													onchange="dataChanged()" /></td>
											<%}%>

										</tr>
										<tr>

											<td align="left" nowrap class="copybold">
												<%-- Commented for case id#2627
                                            <font color="red">*</font>
                                            --%> <bean:message
													key="uploadDocLabel.FileName" />:
											</td>

											<td align="left" style='' colspan='3'>
												<%if(!isInRenewalStatus){%> &nbsp;<html:file
													property="document" onchange="selectFile()" maxlength="300"
													size="50" disabled="<%=modeValue%>" /> <%--Modified for Case#3533 Losing attachments--%>
												<%}else{%> &nbsp;<html:file property="document"
													onchange="selectFile()" maxlength="300" size="50"
													disabled="false" /> <%}%>

											</td>
										</tr>
										<tr>
											<td>&nbsp;</td>
											<td colspan='3'><html:text property="fileName"
													style="width: 450px;" styleClass="cltextbox-color"
													disabled="true" readonly="true" />
												<%--Modified for Case#3533 Losing attachments--%></td>
										</tr>
									</table>
								</td>
							</tr>
							<%if(!modeValue || isInRenewalStatus){%>
							<tr class='table'>
								<td class='savebutton'>
									<%-- Modified for COEUSDEV-120  Lite - protocol attachments throws an error on large attachments on second attempt - Start
                            <html:button property="Save" value="Save"  styleClass="clsavebutton" onclick="insert_data()" /> --%>
									<html:button property="Save" value="Save"
										styleClass="clsavebutton" onclick="validate_upload_form()" />
									<%--Added for COEUSDEV-323 :  Lite - IRB - remove Add New Document link from when user is modifying an attachment - Start --%>
									&nbsp;&nbsp;&nbsp;<html:button property="Cancel" value="Cancel"
										styleClass="clsavebutton" onclick="add_documents()" /> <%--COEUSDEV--323 : End%>
                            <%--COEUSDEV-120 - End--%>
								</td>

							</tr>
							<%}%>

						</table>
					</div>
				</td>
			</tr>
			<!-- Add Documents: - End  -->


			<!-- List of Uploaded Documents:  - Start -->
			<%if(request.getParameter("showAll")== null){%>
			<logic:present name="uploadLatestData">
				<logic:notEmpty name="uploadLatestData">
					<tr>
						<td align="left" valign="top" class='core'><table
								width="100%" border="0" align="center" cellpadding="0"
								cellspacing="0" class="tabtable">
								<tr>
									<td colspan="4" align="left" valign="top"><table
											width="100%" height="2%" border="0" cellpadding="0"
											cellspacing="0" class="tableheader">
											<tr>
												<td nowrap><bean:message
														key="uploadDocLabel.NewChangedAttachment" /></td>
												<td align=right><html:link
														href="javaScript:showAll('1');">
														<u><bean:message key="uploadDocLabel.ShowAll" /></u>
													</html:link></td>
											</tr>
										</table></td>
								</tr>

								<tr align="center">
									<td colspan="3">
										<%
                         //if (uploadData.size() > 0) 
                         //{ %>
										<table width="100%" height="100%" cellspacing="0" border="0"
											cellpadding="0" id="t1" class="sortable">
											<tr>
												<%-- Modified for Case#4344 - Add modify link in Attachments page - Start
                                        <td width="20%" align="left" class="theader"><bean:message key="uploadDocLabel.Type"/></td>
                                        <td width="25%" align="left" class="theader"><bean:message key="uploadDocLabel.Description"/></td>
                                        <td width="20%" align="left" class="theader"><bean:message key="uploadDocLabel.Timestamp"/></td>
                                        --%>
												<td width="21%" align="left" s class="theader"><bean:message
														key="uploadDocLabel.Type" /></td>
												<td width="30%" align="left" class="theader"><bean:message
														key="uploadDocLabel.Description" /></td>
												<td width="23%" align="left" class="theader"><bean:message
														key="uploadDocLabel.Timestamp" /></td>
												<%-- Case#4244 - End --%>
												<td width="23%" align="left" class="theader"><bean:message
														key="uploadDocLabel.UpdateUser" /></td>


												<!--    <td width="15%" align="left" class="theader"><bean:message key="generalInfoLabel.Status"/></td>
                                        <td width="10%" align="left" class="theader" ><bean:message key="label.AmendRenew.versionNo"/></td>-->
												<td width="4%" align="left" class="theader">&nbsp;</td>
												<%-- Modified for Case#4344 - Add modify link in Attachments page - Start --%>
												<td width="5%" align="left" class="theader">&nbsp;</td>
												<%-- <td width="10%" align="left" class="theader">&nbsp;</td> --%>
												<td width="5%" align="left" class="theader">&nbsp;</td>
												<%--<td width="5%" align="left" class="theader">&nbsp;</td>--%>
												<!-- Case#4344 - End -->
												<td width="10%" align="left" class="theader">&nbsp;&nbsp;</td>
											</tr>


											<% 
                                    //BGCOLOR=EFEFEF  FBF7F7
                                    String strBgColor = "#DCE5F1";
                                    int count = 0;
                                %>

											<logic:iterate id="uploadDocList" name="uploadLatestData"
												type="edu.mit.coeus.irb.bean.UploadDocumentBean">
												<% 
                                       if (count%2 == 0) 
                                          strBgColor = "#D6DCE5"; 
                                       else 
                                          strBgColor="#DCE5F1"; 
                                    int statusCode=uploadDocList.getStatusCode();
                                    boolean showRemove=true;
                                    if(statusCode == 3){
                                            showRemove=false;
                                        }
                                        
                                    //Commented for case#3035 - Insufficient Room in New/Changed Attachments List in Protocol Attachments -Start    
                                    //docDescription = uploadDocList.getDescription().length() > 30 ? uploadDocList.getDescription().substring(0,15): uploadDocList.getDescription();
                                    docDescription = uploadDocList.getDescription();
                                    //Commented for case#3035 - Insufficient Room in New/Changed Attachments List in Protocol Attachments -End
                                    
                                    if(showRemove){%>
												<tr bgcolor="<%=strBgColor%>" width="15%" valign=top
													onmouseover="className='TableItemOn'"
													onmouseout="className='TableItemOff'">
													<%-- Modified for Case#4344 - Add modify link in Attachments page - Start
                                            Modify Hyperlink is added instead of DocumentType hyperlink
                                            <td width="20%" height='20' align="left" nowrap class="copy">
                                             <%if(!modeValue && showRemove){
                                                String link = "javascript:update_data('" +uploadDocList.getDocCode() +"','"+uploadDocList.getVersionNumber()+"','"+uploadDocList.getDocumentId()+"','N')";%>
                                                
                                            <html:link href="<%=link%>">
                                               <%=uploadDocList.getDocType()%>
                                            </html:link>
                                            <%}else {%>
                                                <%=uploadDocList.getDocType()%>
                                            <%}%> --%>
													<td width="21%" height='20' align="left" nowrap
														class="copy"><%=uploadDocList.getDocType()%> <%--Case#4344 - End --%>

													</td>

													<%--Commented and Added for case#3035 - Insufficient Room in New/Changed Attachments List in Protocol Attachments -Start--%>
													<%--td width="20%" align="left" nowrap class="copy"><%=docDescription%></td--%>
													<%-- <%String  viewDescription = "javaScript:view_Description('"+uploadDocList.getDescription()+"');";  %> --%>
													<td width="30%" align="left" class="copy"><%=docDescription%>
														&nbsp; <%-- <html:link  href="<%=viewDescription%>">
                                                <bean:message key="uploadDocLabel.ViewDescription"/>
                                            </html:link> --%></td>
													<%--Commented and Added for case#3035 - Insufficient Room in New/Changed Attachments List in Protocol Attachments -End--%>

													<!--   <td width="15%" align="left" nowrap class="copy"><%=uploadDocList.getStatusDescription()%></td>
                                        <td width="10%" align="left" class="copy">&nbsp;&nbsp;&nbsp;&nbsp;<%=uploadDocList.getVersionNumber()%></td>-->
													<td width="23%" align="left" class="copy">
														<%
                                            //Added for COEUSDEV-323 :  Lite - IRB - remove 'Add New Document' link from when user is modifying an attachment - Start
                                            timeStamp = uploadDocList.getUpdateTimestamp();
                                            if(timeStamp != null && !timeStamp.equals("")){
                                                updateTimeStamp = dateFormat.format(timeStamp);
                                            }
                                            %> <%=updateTimeStamp%> <%//COEUSDEV-323-End%>
													</td>
													<%user =(String) hmFromUsers.get(uploadDocList.getUpdateUser());
                                          user = ((user==null) || (user.equals("")))?uploadDocList.getUpdateUser() : user;%>
													<td width="23%" align="left" class="copy"><%=user%></td>
													<td width="4%" align="center" nowrap class="copy">
														<%if((!modeValue && showRemove)||isInRenewalStatus){
                                             String link = "javascript:update_data('" +uploadDocList.getDocCode() +"','"+uploadDocList.getVersionNumber()+"','"+uploadDocList.getDocumentId()+"','N')";%>
														<html:link href="<%=link%>">
															<bean:message key="label.Modify" />&nbsp;
                                             </html:link> <%}%>
													</td>

													<td width="5%" align="center" nowrap class="copy">
														<%
                                               /* //Modified for Case#3036 - Didn't recognize document file type in Protocol attachments  -Start
                                                viewLink = "javascript:view_data('V','" +uploadDocList.getDocCode() +"',"+"'"+uploadDocList.getVersionNumber()+"','"+uploadDocList.getSequenceNumber()+"','N')";
                                                //Modified for Case#3036 - Didn't recognize document file type in Protocol attachments  -End*/
                                                viewLink = "javascript:view_data('V','" +uploadDocList.getDocCode() +"',"+"'"+uploadDocList.getVersionNumber()+"','"+uploadDocList.getSequenceNumber()+"','"+uploadDocList.getDocumentId()+"','N')";
                                            if(showRemove){%>
														&nbsp;&nbsp;<html:link href="<%=viewLink%>">
															<bean:message key="label.View" />
														</html:link> <%} else {%> <bean:message key="label.View" /> <%}%>
													</td>
													<%-- Modified for Case#4344 - Add modify link in Attachments page 
                                        <td width="10%" align="center"  nowrap class="copy"> --%>
													<td width="5%" align="center" nowrap class="copy">
														<%if((!modeValue && showRemove)||isInRenewalStatus){
                                                String removeLink = "javascript:delete_data('D','" +uploadDocList.getDocCode() +"','"+
                                                    uploadDocList.getVersionNumber()+"','"+uploadDocList.getDocumentId()+"','"+statusCode+"','"+uploadDocList.getSequenceNumber()+"')";
                                            %> &nbsp;&nbsp;<html:link
															href="<%=removeLink%>">
															<bean:message key="fundingSrc.Remove" />&nbsp;
                                                </html:link> <%}%>

													</td>

													<td width="10%" align="center" nowrap class="copy">
														<%
                                                String link = "javascript:history_data('" +uploadDocList.getDocCode() +"','"+uploadDocList.getVersionNumber()+"','"+uploadDocList.getDocumentId()+"','"+uploadDocList.getProtocolNumber()+"','')";
                                            if(uploadDocList.getVersionNumber()!=1){
                                                    if(historyDocId!=null && historyDocId.intValue()==uploadDocList.getDocumentId() 
                                                    && protoNum.equals(uploadDocList.getProtocolNumber())){%>
														<html:link href="<%=link%>">
															<%--bean:message key="uploadDocLabel.History"/--%>
															<bean:message key="uploadDocLabel.HideHistory" />
														</html:link> &nbsp;&nbsp;&nbsp; <%} else {%> <html:link href="<%=link%>">
															<bean:message key="uploadDocLabel.ShowHistory" />
														</html:link> &nbsp;&nbsp;&nbsp; <%}%> <%}%>
													</td>

												</tr>

												<!--   <tr>
                                        <td colspan='7'>
                                        <table width='100%' border='0' cellpadding="0">   -->

												<%if(historyDocId!=null && historyDocId.intValue()==uploadDocList.getDocumentId() && protoNum.equals(uploadDocList.getProtocolNumber())){%>
												<logic:notEmpty name="historyData">
													<logic:iterate id="uploadDocList" name="historyData"
														type="edu.mit.coeus.irb.bean.UploadDocumentBean">
														<tr bgcolor="<%=strBgColor%>" width="15%" valign="top"
															onmouseover="className='TableItemOn'"
															onmouseout="className='TableItemOff'">
															<%--Modifief for Case#4344 - Add modify link in Attachments page 
                                        <td width="20%" align="left" nowrap class="copy"><%=uploadDocList.getDocType()%></td> --%>
															<td width="16%" height='20' align="left" nowrap
																class="copy"><%=uploadDocList.getDocType()%></td>

															<%//Commented for case#3035 - Insufficient Room in New/Changed Attachments List in Protocol Attachments -Start    
                                        //docDescription = uploadDocList.getDescription().length() > 30 ? uploadDocList.getDescription().substring(0,15): uploadDocList.getDescription();
                                        docDescription = uploadDocList.getDescription();
                                        //Commented for case#3035 - Insufficient Room in New/Changed Attachments List -End    
                                        %>

															<%--Commented and Added for case#3035 - Insufficient Room in New/Changed Attachments List in Protocol Attachments -Start--%>
															<%--td width="20%" align="left" nowrap class="copy"><%=docDescription%></td--%>
															<%-- <%  String viewDescr = "javaScript:view_Description('"+uploadDocList.getDescription()+"');"; %> --%>
															<td width="25%" align="left" class="copy"><%=docDescription%>
																<%-- <html:link  href="<%=viewDescr%>">
                                                <bean:message key="uploadDocLabel.ViewDescription"/>
                                            </html:link> --%></td>
															<%--Commented and Added for case#3035 - Insufficient Room in New/Changed Attachments List in Protocol Attachments -End--%>

															<!--   <td width="15%" align="left" nowrap class="copy"><%=uploadDocList.getStatusDescription()%></td>
                                        <td width="10%" align="left" class="copy">&nbsp;&nbsp;&nbsp;&nbsp;<%=uploadDocList.getVersionNumber()%></td>-->
															<td width="20%" align="left" class="copy">
																<%
                                            //Added for COEUSDEV-323 :  Lite - IRB - remove 'Add New Document' link from when user is modifying an attachment - Start
                                            timeStamp = uploadDocList.getUpdateTimestamp();
                                            if(timeStamp != null && !timeStamp.equals("")){
                                                updateTimeStamp = dateFormat.format(timeStamp);
                                            }%> <%=updateTimeStamp%> <%//COEUSDEV-323%>
															</td>
															<%user =(String) hmFromUsers.get(uploadDocList.getUpdateUser());
                                          user = ((user==null) || (user.equals("")))?uploadDocList.getUpdateUser() : user;%>
															<td width="20%" align="left" class="copy"><%=user%></td>
															<%--Added for Case#4344 - Add modify link in Attachments page - Start --%>
															<td width="5%" align="center" nowrap class="copy"></td>
															<%--Case#4344 - End --%>
															<td width="5%" align="center" nowrap class="copy">
																<%
                                              /*  //Modified for Case#3036 - Didn't recognize document file type in Protocol attachments  -Start
                                                viewLink = "javascript:view_data('V','" +uploadDocList.getDocCode() +"',"+"'"+uploadDocList.getVersionNumber()+"','"+uploadDocList.getSequenceNumber()+"','N')";
                                                //Modified for Case#3036 - Didn't recognize document file type in Protocol attachments  -End */
                                                viewLink = "javascript:view_data('V','" +uploadDocList.getDocCode() +"',"+"'"+uploadDocList.getVersionNumber()+"','"+uploadDocList.getSequenceNumber()+"','"+uploadDocList.getDocumentId()+"','N')";
                                            %> &nbsp;&nbsp;<html:link
																	href="<%=viewLink%>">
																	<bean:message key="label.View" />
																</html:link>
															</td>
															<td width="10%" align="center" class="copy">&nbsp;</td>
															<td width="5%" align="center" class="copy">&nbsp;</td>
														</tr>
													</logic:iterate>
												</logic:notEmpty>
												<%}%>
												<!--  </table>
                                        </td>
                                    </tr>  -->
												<% count++;%>
												<%}%>
											</logic:iterate>

										</table> <%//}%>

									</td>
								</tr>

								<tr>
									<td>&nbsp;</td>
								</tr>

							</table></td>
					</tr>
				</logic:notEmpty>
			</logic:present>
			<%}%>



			<!-- List of Uploaded Documents: for Show All - Start -->
			<%if(request.getParameter("showAll")!=null){%>
			<logic:present name="uploadLatestData">
				<logic:notEmpty name="uploadLatestData">
					<tr>
						<td align="left" valign="top" class='core'>
							<table width="100%" border="0" align="center" cellpadding="0"
								cellspacing="0" class="tabtable">
								<tr>
									<td colspan="4" align="left" valign="top">
										<table width="100%" height="2%" border="0" cellpadding="0"
											cellspacing="0" class="tableheader">
											<tr>
												<td nowrap><bean:message
														key="uploadDocLabel.NewChangedAttachment" /></td>
												<td align=right><html:link
														href="javaScript:showAll('2');">
														<u><bean:message key="uploadDocLabel.ShowActive" /></u>
													</html:link></td>
											</tr>
										</table>
									</td>
								</tr>
								<tr align="center">
									<td colspan="3">
										<table width="100%" height="100%" cellspacing="0" border="0"
											cellpadding="0" id="t1" class="sortable">
											<tr>
												<!-- Modified for Case#4344 - Add modify link in Attachments page - Start
                        <td width="20%" align="left" class="theader"><bean:message key="uploadDocLabel.Type"/></td>
                        <td width="25%" align="left" class="theader"><bean:message key="uploadDocLabel.Description"/></td>
                        <td width="20%" align="left" class="theader"><bean:message key="uploadDocLabel.Timestamp"/></td>
                        -->
												<td width="21%" align="left" class="theader"><bean:message
														key="uploadDocLabel.Type" /></td>
												<td width="30%" align="left" class="theader"><bean:message
														key="uploadDocLabel.Description" /></td>
												<td width="23%" align="left" class="theader"><bean:message
														key="uploadDocLabel.Timestamp" /></td>
												<!-- Case#4244 - End -->
												<td width="23%" align="left" class="theader"><bean:message
														key="uploadDocLabel.UpdateUser" /></td>


												<td width="4%" align="center" class="theader">&nbsp;</td>

												<%-- Modified for Case#4344 - Add modify link in Attachments page - Start
                        <td width="5%" align="center" class="theader">&nbsp;</td>
                        <td width="10%" align="center" class="theader">&nbsp;</td>
                        <%--td width="5%" align="center" class="theader">&nbsp;</td--%>
												<td width="5%" align="left" class="theader">&nbsp;</td>
												<td width="5%" align="left" class="theader">&nbsp;</td>
												<td width="5%" align="center" class="theader">&nbsp;</td>
												<%-- Case#4344 - End --%>

											</tr>
											<% 
                        String strBgColor = "#DCE5F1";
                        int count = 0;
                    %>

											<logic:iterate id="uploadDocList" name="uploadLatestData"
												type="edu.mit.coeus.irb.bean.UploadDocumentBean">
												<% 
                            if (count%2 == 0) 
                                strBgColor = "#D6DCE5"; 
                            else 
                                strBgColor="#DCE5F1"; 
                            int statusCode=uploadDocList.getStatusCode();
                            boolean showRemove=true;
                            if(statusCode == 3){
                                    showRemove=false;
                            }
                            
                            //Commented for case#3035 - Insufficient Room in New/Changed Attachments List in Protocol Attachments -Start
                            //docDescription = uploadDocList.getDescription().length() > 30 ? uploadDocList.getDescription().substring(0,15): uploadDocList.getDescription();
                            docDescription =  uploadDocList.getDescription();
                            //Commented for case#3035 - Insufficient Room in New/Changed Attachments List in Protocol Attachments -Start
                            
                        %>
												<tr bgcolor="<%=strBgColor%>" width="15%" valign="top"
													onmouseover="className='TableItemOn'"
													onmouseout="className='TableItemOff'">
													<%--Modified for  Case#4344 - Add modify link in Attachments page - Start
                            <td width="20%" align="left" nowrap class="copy">
                            <%if(!modeValue && showRemove){
                            String link = "javascript:update_data('" +uploadDocList.getDocCode() +"','"+uploadDocList.getVersionNumber()+"','"+uploadDocList.getDocumentId()+"','N')";%>
                            <html:link href="<%=link%>">
                                <%=uploadDocList.getDocType()%>
                            </html:link>
                            <%}else {%>
                            <%=uploadDocList.getDocType()%>
                            <%}%>
                            --%>
													<td width="21%" height='20' align="left" nowrap
														class="copy"><%=uploadDocList.getDocType()%></td>
													<%--Case#4344 - End--%>
													<%--Commented and Added for case#3035 - Insufficient Room in New/Changed Attachments List in Protocol Attachments -Start--%>
													<%--td width="20%" align="left" nowrap class="copy"><%=docDescription%></td--%>
													<%-- <% String viewDesc = "javaScript:view_Description('"+uploadDocList.getDescription()+"');";  %> --%>
													<td width="30%" align="left" class="copy"><%=docDescription%>
														<%-- <html:link  href="<%=viewDesc%>">
                                   <bean:message key="uploadDocLabel.ViewDescription"/>
                                </html:link> --%></td>
													<%--Commented and Added for case#3035 - Insufficient Room in New/Changed Attachments List in Protocol Attachments -End--%>
													<td width="23%" align="left" class="copy">
														<%
                                    //Added for COEUSDEV-323 :  Lite - IRB - remove 'Add New Document' link from when user is modifying an attachment - Start
                                    timeStamp = uploadDocList.getUpdateTimestamp();
                                    if(timeStamp != null && !timeStamp.equals("")){
                                          updateTimeStamp = dateFormat.format(timeStamp);
                                    }
                                    updateTimeStamp = dateFormat.format(timeStamp);
                                %> <%=updateTimeStamp%> <%//COEUSDEV-323:End%>
													</td>
													<%user =(String) hmFromUsers.get(uploadDocList.getUpdateUser());
                              user = ((user==null) || (user.equals("")))?uploadDocList.getUpdateUser() : user;%>
													<td width="23%" align="left" class="copy"><%=user%></td>
													<%-- Added for  Case#4344 - Add modify link in Attachments page - Start --%>
													<td width="4%" align="center" nowrap class="copy">
														<%if((!modeValue && showRemove)||isInRenewalStatus){
                                String link = "javascript:update_data('" +uploadDocList.getDocCode() +"','"+uploadDocList.getVersionNumber()+"','"+uploadDocList.getDocumentId()+"','N')";%>
														<html:link href="<%=link%>">
															<bean:message key="label.Modify" />&nbsp;
                                </html:link> <%}%>
													</td>
													<%--Case#4344 -End--%>
													<%-- End --%>
													<td width="5%" align="center" nowrap class="copy">
														<%
                                    /* //Modified for Case#3036 - Didn't recognize document file type in Protocol attachments  -Start
                                    viewLink = "javascript:view_data('V','" +uploadDocList.getDocCode() +"',"+"'"+uploadDocList.getVersionNumber()+"','"+uploadDocList.getSequenceNumber()+"','N')";
                                    //Modified for Case#3036 - Didn't recognize document file type in Protocol attachments  -End */
                                    viewLink = "javascript:view_data('V','" +uploadDocList.getDocCode() +"',"+"'"+uploadDocList.getVersionNumber()+"','"+uploadDocList.getSequenceNumber()+"','"+uploadDocList.getDocumentId()+"','N')";
                                    if(showRemove || isInRenewalStatus){%>
														&nbsp;&nbsp; <html:link href="<%=viewLink%>">
															<bean:message key="label.View" />
														</html:link> <%}%>
													</td>
													<%-- Modified for Case#4344 - Add modify link in Attachments page
                            <td width="5%" align="center"  nowrap class="copy"> --%>
													<td width="5%" align="center" nowrap class="copy">
														<%if((!modeValue && showRemove)||isInRenewalStatus){
                                    String removeLink = "javascript:delete_data('D','" +uploadDocList.getDocCode() +"','"+
                                        uploadDocList.getVersionNumber()+"','"+uploadDocList.getDocumentId()+"','"+statusCode+"','"+uploadDocList.getSequenceNumber()+"')";
                                %> &nbsp;&nbsp;<html:link
															href="<%=removeLink%>">
															<bean:message key="fundingSrc.Remove" />&nbsp;
                                    </html:link> <%}%>
													</td>
													<td width="5%" align="center" nowrap class="copy">
														<%
                                    String link = "javascript:history_data('" +uploadDocList.getDocCode() +"','"+uploadDocList.getVersionNumber()+"','"+uploadDocList.getDocumentId()+"','"+uploadDocList.getProtocolNumber()+"','showAll')";
                                if(uploadDocList.getVersionNumber()!=1){%>
														<%if(historyDocId!=null && historyDocId.intValue()==uploadDocList.getDocumentId() && 
                                protoNum.equals(uploadDocList.getProtocolNumber())){%>
														<html:link href="<%=link%>">
															<%--bean:message key="uploadDocLabel.History"/--%>
															<bean:message key="uploadDocLabel.HideHistory" />
														</html:link> &nbsp;&nbsp;&nbsp; <%} else {%> <html:link href="<%=link%>">
															<bean:message key="uploadDocLabel.ShowHistory" />
														</html:link> &nbsp;&nbsp;&nbsp; <%}%> <%}%>
													</td>
												</tr>
												<%if(historyDocId!=null && historyDocId.intValue()==uploadDocList.getDocumentId() && 
                                protoNum.equals(uploadDocList.getProtocolNumber())){%>
												<logic:notEmpty name="historyData">
													<logic:iterate id="uploadDocList" name="historyData"
														type="edu.mit.coeus.irb.bean.UploadDocumentBean">
														<tr bgcolor="<%=strBgColor%>" width="15%" valign="top"
															onmouseover="className='TableItemOn'"
															onmouseout="className='TableItemOff'">
															<%--Case#4344 -Add modify link in Attachments page
                                <td width="20%" align="left" nowrap class="copy"><%=uploadDocList.getDocType()%></td>--%>
															<td width="16%" align="left" height='20' nowrap
																class="copy"><%=uploadDocList.getDocType()%></td>

															<%//Commented for case#3035 - Insufficient Room in New/Changed Attachments List in Protocol Attachments -Start    
                                //docDescription = uploadDocList.getDescription().length() > 30 ? uploadDocList.getDescription().substring(0,15): uploadDocList.getDescription();
                                docDescription =  uploadDocList.getDescription();
                                //Commented for case#3035 - Insufficient Room in New/Changed Attachments List -End
                                %>

															<%--Commented and Added for case#3035 - Insufficient Room in New/Changed Attachments List in Protocol Attachments -Start--%>
															<%--td width="20%" align="left" nowrap class="copy"><%=docDescription%></td--%>
															<%-- <% String viewDescptn = "javaScript:view_Description('"+uploadDocList.getDescription()+"');";  %>  --%>
															<td width="25%" align="left" class="copy"><%=docDescription%>
																<%-- <html:link  href="<%=viewDescptn%>">
                                       <bean:message key="uploadDocLabel.ViewDescription"/>
                                    </html:link> --%></td>
															<%--Commented and Added for case#3035 - Insufficient Room in New/Changed Attachments List in Protocol Attachments -End--%>

															<td width="20%" align="left" class="copy">
																<%
                                        //Added for COEUSDEV-323 :  Lite - IRB - remove 'Add New Document' link from when user is modifying an attachment - Start
                                        timeStamp = uploadDocList.getUpdateTimestamp();
                                        if(timeStamp != null && !timeStamp.equals("")){
                                               updateTimeStamp = dateFormat.format(timeStamp);
                                        }
                                        updateTimeStamp = dateFormat.format(timeStamp);
                                    %> <%=updateTimeStamp%> <%//COEUSDEV-323 : End%>
															</td>
															<%user =(String) hmFromUsers.get(uploadDocList.getUpdateUser());
                                  user = ((user==null) || (user.equals("")))?uploadDocList.getUpdateUser() : user;%>
															<td width="20%" align="left" class="copy"><%=user%></td>
															<%--Case#4344 - Add modify link in Attachments page - Start--%>
															<td width="5%" align="center" nowrap class="copy"></td>
															<%-- Case#4344 - End--%>
															<td width="5%" align="center" nowrap class="copy">
																<%
                                        /*//Modified for Case#3036 - Didn't recognize document file type in Protocol attachments  -Start
                                        viewLink = "javascript:view_data('V','" +uploadDocList.getDocCode() +"',"+"'"+uploadDocList.getVersionNumber()+"','"+uploadDocList.getSequenceNumber()+"','N')";
                                        //Modified for Case#3036 - Didn't recognize document file type in Protocol attachments  -End*/
                                        viewLink = "javascript:view_data('V','" +uploadDocList.getDocCode() +"',"+"'"+uploadDocList.getVersionNumber()+"','"+uploadDocList.getSequenceNumber()+"','"+uploadDocList.getDocumentId()+"','N')";
                                    if(uploadDocList.getStatusCode()!=3){%>
																&nbsp;&nbsp;<html:link href="<%=viewLink%>">
																	<bean:message key="label.View" />
																</html:link> <%}%>
															</td>
															<td width="10%" align="center" class="copy">&nbsp;</td>
															<td width="5%" align="center" class="copy">&nbsp;</td>
														</tr>
													</logic:iterate>
												</logic:notEmpty>
												<%}%>
												<% count++;%>
											</logic:iterate>
										</table>
									</td>
								</tr>
								<tr>
									<td>&nbsp;</td>
								</tr>
							</table>
						</td>
					</tr>
				</logic:notEmpty>
			</logic:present>
			<%}%>


			<!--Parent protocol documents start-->
			<!--Start-->
			<%if(showParent){%>
			<tr>
				<td align="left" valign="top" class='core'>

					<table width="100%" border="0" align="center" cellpadding="0"
						cellspacing="0" class="tabtable">
						<tr>
							<td colspan="4" align="left" valign="top">
								<table width="100%" height="20" border="0" cellpadding="0"
									cellspacing="0" class="tableheader">
									<tr>
										<td><bean:message key="uploadDocLabel.OriginalProtocol" />
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr align="center">
							<td colspan="3">

								<table width="100%" height="100%" cellspacing="0" border="0"
									cellpadding="0" cellspacing='0' id="t1" class="sortable">
									<tr>
										<td width="20%" align="left" class="theader"><bean:message
												key="uploadDocLabel.Type" /></td>
										<td width="25%" align="left" class="theader"><bean:message
												key="uploadDocLabel.Description" /></td>
										<!--<td width="15%" align="left" class="theader"><bean:message key="generalInfoLabel.Status"/></td>
                                        <td width="10%" align="left" class="theader"><bean:message key="label.AmendRenew.versionNo"/></td>-->
										<td width="22%" align="left" class="theader"><bean:message
												key="uploadDocLabel.Timestamp" /></td>
										<td width="20%" align="left" class="theader"><bean:message
												key="uploadDocLabel.UpdateUser" /></td>
										<%--if(!modeValue){--%>
										<td width="5%" align="center" class="theader">&nbsp;</td>
										<td width="10%" align="center" class="theader">&nbsp;</td>
										<%--td width="5%" align="center" class="theader">&nbsp;</td--%>
										<%--}--%>
									</tr>

									<% 
                                    //BGCOLOR=EFEFEF  FBF7F7
                                    String STRBGCOLOR = "#DCE5F1";
                                    int INDEX = 0;
                                %>
									<logic:present name="parentProtoData">
										<logic:iterate id="uploadParentDocList" name="parentProtoData"
											type="edu.mit.coeus.irb.bean.UploadDocumentBean">
											<% 
                                       if (INDEX%2 == 0) 
                                          STRBGCOLOR = "#D6DCE5"; 
                                       else 
                                          STRBGCOLOR="#DCE5F1"; 
                                          
                                          //Commented for case#3035 - Insufficient Room in New/Changed Attachments List in Protocol Attachments -Start    
                                          //docDescription = uploadParentDocList.getDescription().length() > 30 ? uploadParentDocList.getDescription().substring(0,15) :uploadParentDocList.getDescription();
                                          docDescription = uploadParentDocList.getDescription();
                                          //Commented for case#3035 - Insufficient Room in New/Changed Attachments List in Protocol Attachments -Start    
                                          
                                    %>
											<tr bgcolor="<%=STRBGCOLOR%>" width="15%" valign="top"
												onmouseover="className='TableItemOn'"
												onmouseout="className='TableItemOff'">
												<td align="left" nowrap class="copy"><%=uploadParentDocList.getDocType()%></td>

												<%--Commented and Added for case#3035 - Insufficient Room in New/Changed Attachments List in Protocol Attachments -Start--%>
												<%--td width="20%" align="left" nowrap class="copy"><%=docDescription%></td--%>
												<%-- <% String viewCription = "javaScript:view_Description('"+uploadParentDocList.getDescription()+"');";  %> --%>
												<td width="25%" align="left" class="copy"><%=docDescription%>
													<%-- <html:link  href="<%=viewCription%>">
                                               <bean:message key="uploadDocLabel.ViewDescription"/>
                                            </html:link> --%></td>
												<%--Commented and Added for case#3035 - Insufficient Room in New/Changed Attachments List in Protocol Attachments -End--%>

												<!--    <td width="15%" align="left" nowrap class="copy"><%=uploadParentDocList.getStatusDescription()%></td>
                                        <td width="10%" align="left" class="copy">&nbsp;&nbsp;&nbsp;&nbsp;<%=uploadParentDocList.getVersionNumber()%></td> -->
												<td width="22%" align="left" class="copy">
													<%
                                                //Added for COEUSDEV-323 :  Lite - IRB - remove 'Add New Document' link from when user is modifying an attachment - Start
                                                timeStamp = uploadParentDocList.getUpdateTimestamp();
                                                if(timeStamp != null && !timeStamp.equals("")){
                                                    updateTimeStamp = dateFormat.format(timeStamp);
                                                }
                                            %> <%=updateTimeStamp%> <%//COEUSDEV-323 : End%>
												</td>
												<%user =(String) hmFromUsers.get(uploadParentDocList.getUpdateUser());
                                          user = ((user==null) || (user.equals("")))?uploadParentDocList.getUpdateUser() : user;%>
												<td width="20%" align="left" class="copy"><%=user%></td>
												<td width="5%" align="center" nowrap class="copy">
													<%
                                                /*//Modified for Case#3036 - Didn't recognize document file type in Protocol attachments  -Start 
                                                viewLink = "javascript:view_data('V','" +uploadParentDocList.getDocCode() +"',"+"'"+uploadParentDocList.getVersionNumber()+"','"+uploadParentDocList.getSequenceNumber()+"','P')";
                                                //Modified for Case#3036 - Didn't recognize document file type in Protocol attachments  -End */
                                                viewLink = "javascript:view_data('V','" +uploadParentDocList.getDocCode() +"',"+"'"+uploadParentDocList.getVersionNumber()+"','"+uploadParentDocList.getSequenceNumber()+"','"+uploadParentDocList.getDocumentId()+"','P')";
                                            %> &nbsp;&nbsp;<html:link
														href="<%=viewLink%>">
														<bean:message key="label.View" />
													</html:link>
												</td>
												<td width="10%" align="center" class="copy">
													<%if(!modeValue && showAmend){                                        
                                                String link = "javascript:update_data('" +uploadParentDocList.getDocCode() +"','"+uploadParentDocList.getVersionNumber()+"','"+uploadParentDocList.getDocumentId()+"','P')";%>
													<html:link href="<%=link%>">
														<bean:message key="uploadDocLabel.Amend" />
													</html:link> <%}%>
												</td>
												<td width="5%" align="center" nowrap class="copy">
													<%
                                                String link = "javascript:history_data('" +uploadParentDocList.getDocCode() +"','"+uploadParentDocList.getVersionNumber()+"','"+uploadParentDocList.getDocumentId()+"','"+uploadParentDocList.getProtocolNumber()+"','')";
                                            if(uploadParentDocList.getVersionNumber()!=1){%>
													<%if(historyDocId!=null && historyDocId.intValue()==uploadParentDocList.getDocumentId() && protoNum.equals(uploadParentDocList.getProtocolNumber())){%>
													<html:link href="<%=link%>">
														<%--bean:message key="uploadDocLabel.History"/--%>
														<bean:message key="uploadDocLabel.HideHistory" />
													</html:link> &nbsp;&nbsp;&nbsp; <%} else {%> <html:link href="<%=link%>">
														<bean:message key="uploadDocLabel.ShowHistory" />
													</html:link> &nbsp;&nbsp;&nbsp; <%}%> <%}%>
												</td>
											</tr>

											<%if(historyDocId!=null && historyDocId.intValue()==uploadParentDocList.getDocumentId() && protoNum.equals(uploadParentDocList.getProtocolNumber())){%>
											<logic:notEmpty name="historyData">
												<logic:iterate id="uploadDocList" name="historyData"
													type="edu.mit.coeus.irb.bean.UploadDocumentBean">
													<tr bgcolor="#CCCCFF" width="15%" valign="top"
														onmouseover="className='TableItemOn'"
														onmouseout="className='TableItemOff'">
														<td width="20%" align="left" nowrap class="copy"><%=uploadDocList.getDocType()%></td>

														<%//Commented for case#3035 - Insufficient Room in New/Changed Attachments List in Protocol Attachments -Start    
                                        //docDescription = uploadDocList.getDescription().length() > 30 ? uploadDocList.getDescription().substring(0,15): uploadDocList.getDescription();
                                        docDescription = uploadDocList.getDescription();
                                        //Commented for case#3035 - Insufficient Room in New/Changed Attachments List in Protocol Attachments -End%>

														<%--Commented and Added for case#3035 - Insufficient Room in New/Changed Attachments List in Protocol Attachments -Start--%>
														<%--td width="20%" align="left" nowrap class="copy"><%=docDescription%></td--%>
														<%-- <%  String viewDes = "javaScript:view_Description('"+uploadDocList.getDescription()+"');";  %>  --%>
														<td width="25%" align="left" class="copy"><%=docDescription%>
															<%-- <html:link  href="<%=viewDes%>">
                                                <bean:message key="uploadDocLabel.ViewDescription"/>
                                            </html:link> --%></td>
														<%--Commented and Added for case#3035 - Insufficient Room in New/Changed Attachments List in Protocol Attachments -End--%>

														<!--   <td width="15%" align="left" nowrap class="copy"><%=uploadDocList.getStatusDescription()%></td>
                                        <td width="10%" align="left" class="copy">&nbsp;&nbsp;&nbsp;&nbsp;<%=uploadDocList.getVersionNumber()%></td> -->
														<td width="20%" align="left" class="copy">
															<%
                                                //Added for COEUSDEV-323 :  Lite - IRB - remove 'Add New Document' link from when user is modifying an attachment - Start
                                                timeStamp = uploadDocList.getUpdateTimestamp();
                                                if(timeStamp != null && !timeStamp.equals("")){
                                                    updateTimeStamp = dateFormat.format(timeStamp);
                                                }
                                                updateTimeStamp = dateFormat.format(timeStamp);
                                            %> <%=updateTimeStamp%> <%//COEUSDEV-323:End%>
														</td>
														<%user =(String) hmFromUsers.get(uploadDocList.getUpdateUser());
                                          user = ((user==null) || (user.equals("")))?uploadDocList.getUpdateUser() : user;%>
														<td width="20%" align="left" class="copy"><%=user%></td>
														<td width="5%" align="center" nowrap class="copy">
															<%
                                                /* //Modified for Case#3036 - Didn't recognize document file type in Protocol attachments  -Start
                                                viewLink = "javascript:view_data('V','" +uploadDocList.getDocCode() +"',"+"'"+uploadDocList.getVersionNumber()+"','"+uploadDocList.getSequenceNumber()+"','N')";
                                                //Modified for Case#3036 - Didn't recognize document file type in Protocol attachments  -End */
                                                viewLink = "javascript:view_data('V','" +uploadDocList.getDocCode() +"',"+"'"+uploadDocList.getVersionNumber()+"','"+uploadDocList.getSequenceNumber()+"','"+uploadDocList.getDocumentId()+"','N')";
                                            if(uploadDocList.getStatusCode() !=3){%>
															&nbsp;&nbsp; <html:link href="<%=viewLink%>">
																<bean:message key="label.View" />
															</html:link> <%}%>
														</td>
														<td width="10%" align="center" class="copy">&nbsp;</td>
														<td width="5%" align="center" class="copy">&nbsp;</td>
													</tr>
												</logic:iterate>
												<%//}%>
											</logic:notEmpty>
											<%}%>

											<% INDEX++;%>
										</logic:iterate>
									</logic:present>
								</table>
							</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
						</tr>

					</table>

				</td>
			</tr>
			<%}%>
			<!--End-->
			<!--Paren t protocol documents end-->
			<!--End-->
			<tr>
				<td height='10'>&nbsp;</td>
			</tr>
		</table>
		<!-- New Template for cwUploadTemplate - End  -->

		<html:hidden property="versionNumber" />
		<html:hidden property="statusCode" />
		<html:hidden property="docCode" />
		<html:hidden property="acType" />
		<html:hidden property="protocolNumber" />
		<html:hidden property="sequenceNumber" />
		<html:hidden property="documentId" />
		<html:hidden property="fileBytes" />
		<html:hidden property="parent" />
		<!-- Added for Case#3533 - Losing attachments -->
		<html:hidden property="fileNameHidden" />
	</html:form>
	<script>
        //Modified for Case#3533 - Losing attachments - Start
        
        if(document.uploadDocForm.acType.value=='U'){
            document.uploadDocForm.docType.value = document.uploadDocForm.docCode.value;
        }
        document.uploadDocForm.fileName.value = document.uploadDocForm.fileNameHidden.value;
        //Modified for Case#3533 - Losing attachments - End
          DATA_CHANGED = 'false';
          if(errValue && !errLock) {
            DATA_CHANGED = 'true';
          }
          if(document.uploadDocForm.acType.value.length==0 || document.uploadDocForm.acType.value == 'H'){
            document.uploadDocForm.acType.value = 'I';
          } else if(document.uploadDocForm.acType.value=='U'){
            document.uploadDocForm.docType.disabled=true;
            document.getElementById('header_label').innerHTML = '<bean:message key="uploadDocLabel.UploadNewVersionDocument"/>' ;                       
            //Added for case #COEUSQA-2507_Cannot upload protocol amendment - Start
            document.getElementById("showAddDocument").style.display="block";  
            //Added for case #COEUSQA-2507_Cannot upload protocol amendment - End
          }
          LINK = "<%=request.getContextPath()%>/uploadAction.do";
          FORM_LINK = document.uploadDocForm;
          PAGE_NAME = "changes";
          function dataChanged(){
            document.uploadDocForm.docCode.value = document.uploadDocForm.docType.value;
            DATA_CHANGED = 'true';
          }
          linkForward(errValue);
         </script>
	<script>
      var help = ' <bean:message key="helpTextProtocol.UploadDocuments"/>';
      help = trim(help);
      if(help.length == 0) {
        document.getElementById('helpText').style.display = 'none';
      }
      function trim(s) {
            return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
      }  
  </script>
</body>
</html:html>
