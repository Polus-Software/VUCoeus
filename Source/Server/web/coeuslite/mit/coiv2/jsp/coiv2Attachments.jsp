<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@ page
	import="edu.mit.coeus.bean.PersonInfoBean,java.util.Vector,edu.mit.coeuslite.coiv2.beans.Coiv2AttachmentBean,java.util.HashMap,edu.mit.coeuslite.coiv2.services.CoiCommonService,
         edu.mit.coeuslite.coiv2.beans.CoiPersonProjectDetails,
         edu.mit.coeuslite.coiv2.utilities.CoiConstants,java.util.Date;"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%  String path = request.getContextPath();
        String startDate = "";
         String endDate = "";
          String pjctTitle="";
          String moduleItemKey="";
          String awardTitle="";
          String pjctId="";
          String eventname="";

           
            boolean disable = false;
            
            PersonInfoBean loggedPer = (PersonInfoBean) session.getAttribute("LOGGED_IN_PERSON");
            String loggedperson=loggedPer.getUserId();

            boolean canView = true;            
            
%>
<html:html locale="true">
<head>
<title>Disclosure Notes</title>
<script language="javaScript">

            function selectFile(){                
                checkFormat();
                dataChanged();
                //checkSize();
                document.forms[0].fileName.value = document.forms[0].document.value;
                document.forms[0].fileNameHidden.value = document.forms[0].document.value;
            } 
               function checkFormat(){               
               //alert("entering checkFormat()");
              var filename = document.forms[0].document.value;
              var filelength = parseInt(filename.length) - 3;
              var fileext = filename.substring(filelength,filelength + 3);

              if( fileext=="exe" ||  fileext=="ini" || fileext=="bat" ){
                 alert("Please dont upload  "+fileext+" files");
                 document.forms[0].document.value="";
                 return false;
         }     }

          function format(){
              var filename = document.forms[0].document.value;
              var filelength = parseInt(filename.length) - 3;
              var fileext = filename.substring(filelength,filelength + 3);
               if( fileext=="exe" ||  fileext=="ini" || fileext=="bat" ){
                   alert("change upload document format");
                   return fileext;
               }
          }



            function removeAttachment(entityNum,coiDisclosureNumber,coiSequenceNumber,user)
            {
                var cannnotgoForward='<%=disable%>';
                var loggedPerson='<%=loggedperson%>'
                if(loggedPerson!=user)
                    {
                        alert("Permission Denied")
                        cannnotgoForward='true';
                    }
                if(cannnotgoForward=='false'){
                    var operationType='<%=request.getParameter("operationType")%>'
                    if (confirm("Are you sure you want to delete the document?")==true){
                        document.forms[0].entityNumber.value = entityNum;
                        document.forms[0].disclosureNumber.value = coiDisclosureNumber;
                        document.forms[0].sequenceNumber.value = coiSequenceNumber;
                        document.forms[0].acType.value = "D";
                        document.forms[0].action= '<%=path%>' +"/saveAttachmentsCoiv2.do?&operationType="+operationType;
                        document.forms[0].submit();
                    }
                }
            }

            function viewAttachment(entityNum,coiDisclosureNumber,coiSequenceNumber)
            {              
                 var canView='<%=canView%>'
                if(canView=='true'){
                // window.location("request.getContextPath()/jsp/downloadPage.jsp");
                var operationType='<%=request.getParameter("operationType")%>'
                var isViewer='<%=request.getAttribute("isViewer")%>';
                window.open("<%=request.getContextPath()%>/viewAttachment.do?&entityNum="+entityNum+"&disclosureNumber="+coiDisclosureNumber+"&SeqNumber="+coiSequenceNumber+"$operationType="+operationType+"&isViewer="+isViewer);
                }
        }

            function saveAndcontinue()
            {
               if(document.forms[0].fileName.value==null || document.forms[0].fileName.value=='')
                {
                     var operationType='<%=request.getParameter("operationType")%>'
                document.forms[0].action= '<%=path%>' +"/getNotesCoiv2.do?operationType="+operationType;
                document.forms[0].submit();
                }else{
                var operationType='<%=request.getParameter("operationType")%>'
                if(confirm('Please save the attachment')==true){
                var test=saveOrDelete();}
                else{
                    document.forms[0].action= '<%=path%>' +"/getNotesCoiv2.do?operationType="+operationType;
                     document.forms[0].submit();
                } 
                }
            } 

            function saveOrDelete()
            {
                var success= validateSaveOrDelete();
                var operationType='<%=request.getParameter("operationType")%>'
                if(success==true){
                    document.forms[0].action= '<%=path%>' +"/saveAttachmentsCoiv2.do?&operationType="+operationType;
                    document.forms[0].submit();
                    return true;
                }else{
                    return false;
                }
            }
            function validateSaveOrDelete(){
                if(document.forms[0].docType.value==null || document.forms[0].docType.value=='')
                {
                    alert('Please select Document Type');
                    document.forms[0].docType.focus();
                    return false;
                }

                if(document.forms[0].description.value==null ||document.forms[0].description.value.replace(/^\s+/,'').replace(/\s+$/,'')=='')
                {
                    alert('Please enter Description');
                    document.forms[0].description.focus();
                    return false;
                }
                if(document.forms[0].fileName.value==null || document.forms[0].fileName.value=='')
                {
                    alert('Please select a File');
                    document.forms[0].fileName.focus();
                    return false;
                }
                var extdoc=format();
                if(extdoc=="exe" ||  extdoc=="ini" || extdoc=="bat"){
                    return false
                }else{
                return true;
            }
            }
            function setFocus()
            {
                document.forms[0].docType.focus();
            }
               function exitToCoi(){
                var answer = confirm( "This operation will discard current changes.  Prior screen entries will be saved.  You may continue this disclosure event by selecting it from the Pending section of your COI home page.  Click 'OK' to continue.");
                     if(answer) {
                        document.location = '<%=request.getContextPath()%>/exitDisclosure.do';
                        window.location;
                     }
             }
        </script>

</head>
<body onload="javaScript:setFocus()">
	<html:form action="/saveAttachmentsCoiv2.do"
		enctype="multipart/form-data">
		<div id="notesProtocol">
			<table style="width: 100%;" height="100%" border="0" cellpadding="2"
				cellspacing="0" class="table" align='center'>

				<%--      code added for displaying project details...starts--%>
				<%
             
            String projectType = (String) request.getSession().getAttribute("projectType");
            if(projectType.equalsIgnoreCase("Proposal")||projectType.equalsIgnoreCase("iacucProtocol")||projectType.equalsIgnoreCase("Protocol")||projectType.equalsIgnoreCase("Award")||projectType.equalsIgnoreCase("Travel")){
            Vector pjctDetails = (Vector)request.getSession().getAttribute("projectDetailsListInSeesion");
            if(pjctDetails !=null){
            CoiPersonProjectDetails coiPersonProjectDetails =(CoiPersonProjectDetails) pjctDetails.get(0);
            pjctTitle=coiPersonProjectDetails.getCoiProjectTitle();
            moduleItemKey=coiPersonProjectDetails.getModuleItemKey();
            awardTitle=coiPersonProjectDetails.getAwardTitle();
            pjctId=coiPersonProjectDetails.getModuleItemKey();
            eventname=coiPersonProjectDetails.getEventName();
            CoiCommonService coiCommonService1 = CoiCommonService.getInstance();
            if(coiPersonProjectDetails.getCoiProjectStartDate()!=null)
            {
                startDate = (String) coiCommonService1.getFormatedDate(coiPersonProjectDetails.getCoiProjectStartDate());
            }
            if(coiPersonProjectDetails.getCoiProjectEndDate()!=null)
            {
              endDate = (String) coiCommonService1.getFormatedDate(coiPersonProjectDetails.getCoiProjectEndDate());
            }}}
        %>

				<%
                if(projectType.equals("Protocol")) {%>
				<tr>
					<td colspan="4"><b>For your project listed below, please
							answer the certification questionnaire:</b></td>
				</tr>
				<tr>
					<td align="right" width="16%" style="float: none" nowrap><b>IRB
							Protocol # :</b></td>
					<td style="float: none" align="left" width="30%"><%=moduleItemKey%></td>
					<td width="28%" style="float: none"></td>
					<td style="float: none"></td>
				</tr>
				<tr>
					<td align="right" width="16%" style="float: none" nowrap><b>Title
							:</b></td>
					<td colspan="3" style="float: none" align="left" width="30%"><%=pjctTitle%></td>
					<%--<td width="28%" style="float: none"></td>
                            <td style="float: none"></td>--%>
				</tr>
				<tr>
					<td align="right" width="16%" style="float: none" nowrap><b>Application
							Date :</b></td>
					<td style="float: none" align="left" width="30%"><%=startDate%></td>
					<td align="right" width="15%" style="float: none" nowrap><b>Expiration
							Date :</b></td>
					<td align="left" width="38%" style="float: none"><%=endDate%></td>
				</tr>
				<%}else if(projectType.equalsIgnoreCase("iacucProtocol")){%>
				<tr>
					<td colspan="4"><b>For your project listed below, please
							answer the certification questionnaire:</b></td>
				</tr>
				<tr>
					<td align="right" width="15%" style="float: none" nowrap><b>IACUC
							Protocol # :</b></td>
					<td style="float: none" align="left" width="30%"><%=moduleItemKey%></td>
					<td width="28%" style="float: none"></td>
					<td style="float: none"></td>
				</tr>
				<tr>
					<td align="right" width="15%" style="float: none" nowrap><b>Title
							:</b></td>
					<td colspan="3" style="float: none" align="left" width="30%"><%=pjctTitle%></td>
					<%--<td width="28%" style="float: none"></td>
                            <td style="float: none"></td>--%>
				</tr>
				<tr>
					<td align="right" width="15%" style="float: none" nowrap><b>Application
							Date :</b></td>
					<td style="float: none" align="left" width="30%"><%=startDate%></td>
					<td align="right" width="15%" style="float: none" nowrap><b>Expiration
							Date :</b></td>
					<td align="left" width="38%" style="float: none"><%=endDate%></td>
				</tr>
				<%}else if(projectType.equals("Proposal")){%>
				<tr>
					<td colspan="4"><b>For your project listed below, please
							answer the certification questionnaire:</b></td>
				</tr>
				<tr>
					<td align="right" width="16%" style="float: none" nowrap>&emsp;<b>Proposal
							# :</b></td>
					<td style="float: none" align="left" width="30%"><%=moduleItemKey%></td>
					<td width="28%" style="float: none"></td>
					<td style="float: none"></td>
				</tr>
				<tr>
					<td align="right" width="16%" style="float: none" nowrap>&emsp;<b>Title
							:</b></td>
					<td colspan="3" style="float: none" align="left" width="30%"><%=pjctTitle%></td>
					<%--<td width="28%" style="float: none"></td>
                            <td style="float: none"></td>--%>
				</tr>
				<tr>
					<td align="right" width="16%" style="float: none" nowrap>&emsp;<b>Start
							Date :</b></td>
					<td style="float: none" align="left" width="30%"><%=startDate%></td>
					<td align="right" width="28%" style="float: none" nowrap><b>End
							Date :</b></td>
					<td align="left" width="35%" style="float: none"><%=endDate%></td>
				</tr>
				<%} else if(projectType.equals("Award")){ %>
				<tr>
					<td colspan="4"><b>For your project listed below, please
							answer the certification questionnaire:</b></td>
				</tr>
				<tr>
					<td align="right" width="16%" style="float: none" nowrap>&emsp;<b>Award
							# :</b></td>
					<td style="float: none" align="left" width="30%"><%=moduleItemKey%></td>
					<td width="28%" style="float: none"></td>
					<td style="float: none"></td>
				</tr>
				<tr>
					<td align="right" width="16%" style="float: none" nowrap>&emsp;<b>Title:</b></td>
					<td style="float: none" align="left" width="30%"><%=pjctTitle%></td>
					<td width="28%" style="float: none"></td>
					<td style="float: none"></td>
				</tr>
				<tr>
					<td align="right" width="16%" style="float: none" nowrap>&emsp;<b>Start
							Date :</b></td>
					<td style="float: none" align="left" width="30%"><%=startDate%></td>
					<td align="right" width="28%" style="float: none" nowrap><b>End
							Date :</b></td>
					<td align="left" width="35%" style="float: none"><%=endDate%></td>
				</tr>
				<% } else if(projectType.equals("Travel")){ %>
				<tr>
					<td colspan="4"><b>For your project listed below, please
							answer the certification questionnaire:</b></td>
				</tr>
				<tr>
					<td align="right" width="16%" style="float: none" nowrap>&emsp;<b>Travel
							# :</b></td>
					<td style="float: none" align="left" width="30%"><%=moduleItemKey%></td>
					<td width="28%" style="float: none"></td>
					<td style="float: none"></td>
				</tr>
				<tr>
					<td align="right" width="16%" style="float: none" nowrap>&emsp;<b>Title:</b></td>
					<td style="float: none" align="left" width="30%"><%=pjctTitle%></td>
					<td width="28%" style="float: none"></td>
					<td style="float: none"></td>
				</tr>
				<tr>
					<td align="right" width="16%" style="float: none" nowrap>&emsp;<b>Start
							Date :</b></td>
					<td style="float: none" align="left" width="30%"><%=startDate%></td>
					<td align="right" width="28%" style="float: none" nowrap><b>End
							Date :</b></td>
					<td align="left" width="35%" style="float: none"><%=endDate%></td>
				</tr>
				<% } %>

				<%--      code added for displaying project details...ends--%>
				<tr>
					<td colspan="5">
						<table width="100%" border="0" cellpadding="2" cellspacing="0">
							<tr>
								<td height="20" width="50%" align="left" valign="top"
									class="theader">Disclosure Attachment</td>
								<td align="right" width="50%" class="theader"><a
									id="helpPageText" href="#"> </a></td>
							</tr>
						</table>
					</td>
				</tr>

				<%--             <tr class = "copy">
                            <td align="left" width='99%'>

                            </td>
                        </tr>--%>

				<%--         <tr class='copybold' align='left' width='100%'>
                            <td>
                            </td>
                        </tr>--%>

				<!-- Add Documents: - Start  -->
				<tr>
					<td colspan="4" align="left" valign="top" class='core'><table
							width="100%" border="0" align="center" cellpadding="0"
							cellspacing="0" class="tabtable">
							<tr>
								<td colspan="5" align="left" valign="top"><table
										width="100%" height="2%" border="0" cellpadding="0"
										cellspacing="0" class="tableheader">
										<tr>
											<td align="left" class="copybold" style="width: 300px;">

												<bean:message key="uploadDocLabel.AddDocuments" />


											</td>
										</tr>
									</table></td>
							</tr>

							<tr>
								<td colspan="4" align="left" valign="top">
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
											<td width="12%" align="left" class="copybold" nowrap><bean:message
													key="uploadDocLabel.DocumentType" />:</td>
											<td width="30%" align="left" nowrap><logic:notEmpty
													name="DocTypes">
                                                            &nbsp;<html:select
														property="docType" styleClass="textbox-long"
														disabled="<%=disable%>" onchange="dataChanged()">
														<html:option value="">
															<bean:message key="specialReviewLabel.pleaseSelect" />
														</html:option>
														<html:options collection="DocTypes" property="code"
															labelProperty="description" />
													</html:select>
												</logic:notEmpty> <logic:empty name="DocTypes">
                                                            &nbsp;<html:select
														property="docType" styleClass="textbox-long">
														<html:option value="">
															<bean:message key="specialReviewLabel.pleaseSelect" />
														</html:option>
													</html:select>
												</logic:empty></td>
											<td width="15%" align=right nowrap class="copybold"><bean:message
													key="uploadDocLabel.Description" />:</td>
											<td width='43%' align=left>&nbsp;<html:text
													property="description" maxlength="150"
													disabled="<%=disable%>" size="40" onchange="dataChanged()"></html:text></td>
										</tr>
										<tr>

											<td align="left" nowrap class="copybold"><bean:message
													key="uploadDocLabel.FileName" />:</td>

											<td align="left" class='copy' colspan="4">&nbsp;<html:file
													property="document" disabled="<%=disable%>"
													onchange="selectFile()" maxlength="300" size="50" />
											</td>
										</tr>
										<tr>
											<td align="left" class="copybold" nowrap>Project #:</td>
											<td style="font-size: 13px" colspan="4"><logic:present
													name="projectList">
													<logic:notEmpty name="projectList">
														<html:select property="pjtName" styleClass="textbox-long"
															style="width:650;" onchange="dataChanged()">
															<html:option value="0">
																<bean:message key="specialReviewLabel.pleaseSelect" />
															</html:option>
															<html:options collection="projectList" property="code"
																labelProperty="description" />
														</html:select>
													</logic:notEmpty>

												</logic:present> <logic:notPresent name="projectList">
													<%  String moduleitemkey = "";
                                        // String projectType = (String) request.getSession().getAttribute("projectType");
                                            if((projectType.equals("Protocol")))
                                             {
                                                  moduleitemkey= (String)session.getAttribute("checkedprotocolno");
                                             }
                                             if((projectType.equals("Proposal"))){
                                                  moduleitemkey= (String)session.getAttribute("checkedproposal");
                                             }
                                                 if((projectType.equals("Award"))){
                                                  moduleitemkey= (String)session.getAttribute("checkedawardno");
                                             }
                                                 if((projectType.equalsIgnoreCase("IacucProtocol"))){
                                                  moduleitemkey= (String)session.getAttribute("checkediacucprotocolno");
                                             }
                                                 if((projectType.equals("Travel"))){
                                                  moduleitemkey= moduleItemKey; 
                                             }
                                      String name = "";
                                                if(request.getSession().getAttribute("pjctname") != null) {
                                                    name= (String)request.getSession().getAttribute("pjctname");
                                                }
                                               int length=name.length();
                                               if(length >25)
                                               {
                                                name=name.substring(0, 25);
                                               }
                                                        %>

													<%=moduleitemkey%>
													<html:hidden property="pjtName" value="<%=moduleitemkey%>" />
												</logic:notPresent></td>
										</tr>
										<tr>
											<%if((projectType.equals("Travel"))){%>
											<td align="left" class="copybold" nowrap colspan="6"></td>
											<%} else {%>
											<td align="left" class="copybold" nowrap>Financial
												Entity:</td>
											<td colspan="5"><logic:present name="FinEntForPerson">
													<html:select property="entity" styleClass="textbox-long"
														style="width:650;">
														<html:option value="0">
															<bean:message key="specialReviewLabel.pleaseSelect" />
														</html:option>
														<html:options collection="FinEntForPerson" property="code"
															labelProperty="description" />
													</html:select>
												</logic:present> <%--<html:select property="entity" styleClass="textbox-long" style="width:200%;" >
                                                                          <html:option value="0"><bean:message key="specialReviewLabel.pleaseSelect"/></html:option>--%>
												<logic:notPresent name="FinEntForPerson">
													<html:options collection="FinEntForPerson" property="code"
														labelProperty="description" />
												</logic:notPresent></td>
											<%}%>
										</tr>
										<tr>
											<td>&nbsp;</td>
											<td colspan="5" style="visibility: hidden"><html:text
													property="fileName" style="width: 450px;"
													disabled="<%=disable%>" styleClass="cltextbox-color"
													readonly="true" /></td>
										</tr>
									</table>
								</td>
							</tr>

							<tr class='table'>
								<td class='savebutton'><html:button property="Save"
										onclick="javaScript:saveOrDelete();" value="Save"
										disabled="<%=disable%>" styleClass="clsavebutton" />

									&nbsp;&nbsp;&nbsp;</td>

							</tr>


						</table></td>
				</tr>
				<!-- Add Documents: - End  -->



				<!-- List of Disclosure Attachments Start -->
				<tr>
					<td colspan="4" align="left" valign="top" class='core'>
						<table width="100%" border="0" align="center" cellpadding="0"
							cellspacing="0" class="tabtable">

							<tr>
								<td colspan="10" align="left" valign="top">
									<table width="100%" border="0" cellpadding="0" cellspacing="0"
										class="tableheader">
										<tr>
											<td>List of Attachments</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td height='2'></td>
							</tr>

							<tr>
								<td class="theader" align="left" width="15%">Attachment
									Type</td>

								<td class="theader" align='left' width="17%">File Name</td>
								<td class="theader" align='left' width="18%">Description</td>
								<td class="theader" align='left' width="12%">Project #</td>
								<%if((projectType.equals("Travel"))){%>
								<td class="theader" align='left' width="15%" colspan="2">
									Update Timestamp</td>
								<%} else {%>
								<td class="theader" align='left' width="12%">Entity</td>
								<td class="theader" align='left' width="15%">Update
									Timestamp</td>
								<%}%>
								<td class="theader" align='left' width="3%"></td>
								<td class="theader" align='center' width="8%"></td>

							</tr>

							<%
                                                String strBgColor = "#D6DCE5";
                                                int index = 0;
                                                int i = 0;
                                                Vector attachmentList = (Vector) request.getAttribute("attachmentList");

                                    %>
							<logic:present name="attachmentList">
								<logic:iterate id="disclosureAttachments" name="attachmentList">
									<%

                                                        if (index % 2 == 0) {
                                                            strBgColor = "#D6DCE5";
                                                        } else {
                                                            strBgColor = "#DCE5F1";
                                                        }
                                                        Coiv2AttachmentBean attachmentBean = (Coiv2AttachmentBean) attachmentList.get(i);
                                                        String docTyp=attachmentBean.getDocdescription();
                                                        String desc=attachmentBean.getDescription();
                                                        String pjtname=attachmentBean.getProjectName();
                                                       String  fileName=attachmentBean.getFileName();
                                                       String  updateUser=attachmentBean.getUserName();
                                                       Date updateTimestamp = attachmentBean.getUpdateTimeStamp();
                                                       String annlPjtName=(String)request.getAttribute("projectName");

                                                       if(docTyp == null) {
                                                           docTyp = "";
                                                       }

                                                        i++; 
                                            %>
									<tr bgcolor="<%=strBgColor%>" class="rowLine"
										onmouseover="className='rowHover rowLine'"
										onmouseout="className='rowLine'">
										<td class="copy" align='left' style="width: 15%">
											<%--<bean:write name="disclosureAttachments" property="docType"/>--%><%=docTyp%>
										</td>
										<%-- <logic:present name="eventDiscl">
                                                       <% String name1= (String)request.getSession().getAttribute("pjctname");
                                                       int size=name1.length();
                                                       if(size >25){
                                                          name1=name1.substring(1,25);}
                                                          %>
                                                            <td class="copy" align='justify' style="padding-left: 5px">
                                                          <%=name1%>
                                                         </td>
                                                    </logic:present>
                                                        <logic:notPresent name="eventDiscl">
                                                         <td class="copy" align='justify' style="padding-left: 5px">
                                                         <%=annlPjtName%>
                                                         </td>
                                                    </logic:notPresent>--%>

										<td class="copy" align='left' style="width: 15%"><%=fileName%>
										</td>

										<td class="copy" align='left'><%=desc%></td>
										<td class="copy" align='left'><bean:write
												name="disclosureAttachments" property="moduleItemKey" /></td>
										<%if((projectType.equals("Travel"))){%>
										<td class="copy" align='left' colspan="2"><%=updateTimestamp%>
											by <%=updateUser%></td>
										<%}else {%>
										<td class="copy" align='left'><bean:write
												name="disclosureAttachments" property="entName" /></td>
										<td class="copy" align='left'><%=updateTimestamp%> by <%=updateUser%>
										</td>
										<%}%>
										<td class="copy" align='left'>
											<%
                                                                String link1 = "javaScript:viewAttachment('" + attachmentBean.getEntityNumber() + "','" + attachmentBean.getDisclosureNumber() + "','" + attachmentBean.getSequenceNumber() + "','" + attachmentBean.getUpdateUser() + "')";
                                                    %> <html:link
												href="<%=link1%>">
                                                        View
                                                    </html:link>
										</td>
										<td class="copy" align='left'>
											<%
                                                      String link = "javaScript:removeAttachment('" + attachmentBean.getEntityNumber() + "','" + attachmentBean.getDisclosureNumber() + "','" + attachmentBean.getSequenceNumber() + "','" + attachmentBean.getUpdateUser() + "')";
                                                    %> <html:link
												href="<%=link%>">
                                                        &nbsp;&nbsp;Remove
                                                    </html:link>&nbsp;&nbsp;
										</td>


									</tr>
									<tr>
										<td height=2></td>
									</tr>
									<%
                                                        index++;
                                            %>
								</logic:iterate>
							</logic:present>
							<tr>
								<td class='savebutton' align="left" colspan="3"><html:button
										onclick="javaScript:saveAndcontinue();" property="Save"
										styleClass="clsavebutton" style="width:150px;">
                                           Continue
                                        </html:button> <html:button
										onclick="javaScript:exitToCoi();" property="Save"
										styleClass="clsavebutton" style="width:150px;">
                                          Quit
                                        </html:button></td>
							</tr>
						</table>
					</td>
				</tr>
				<!-- List of Disclosure Attachments End -->
				<%--<html:hidden property="docName"/>--%>
				<html:hidden property="acType" />
				<html:hidden property="disclosureNumber" />
				<html:hidden property="sequenceNumber" />
				<html:hidden property="entityNumber" />
				<html:hidden property="fileBytes" />
				<html:hidden property="fileNameHidden" />
			</table>
		</div>

	</html:form>
	<script>
      DATA_CHANGED = 'false';
      var errValue = false;
      if(errValue && !errLock) {
            DATA_CHANGED = 'true';
      }      
      function dataChanged(){
        CHECK_ATTACH ="ATTACH";
        PAGE_NAME = "COI Attachment";  
        FORM_LINK = document.coiv2Attachment;
        var operationType='<%=request.getParameter("operationType")%>'
        LINK = "<%=request.getContextPath()%>/saveAttachmentsCoiv2.do?&operationType="+operationType;
       
        DATA_CHANGED = 'true';
      }
      linkForward(errValue);
</script>
</body>
</html:html>