<%-- 
    Document   : attachments
    Created on : May 8, 2010, 1:05:10 PM
    Author     : Mr
--%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page
	import="edu.mit.coeus.bean.PersonInfoBean,edu.mit.coeuslite.coiv2.beans.Coiv2NotesBean,java.util.HashMap,java.util.Vector,edu.mit.coeuslite.coiv2.beans.Coiv2AttachmentBean,edu.mit.coeuslite.coiv2.utilities.CoiConstants,edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean;"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<%String path = request.getContextPath();
        
                    PersonInfoBean loggedPer = (PersonInfoBean) session.getAttribute("LOGGED_IN_PERSON");
                    String loggedperson = loggedPer.getUserId();                    
                      boolean canView = true;
            String projectType = "";

            if(session.getAttribute("projectType") != null) {
                projectType = session.getAttribute("projectType").toString();
            }
            
        %>
<script src="<%=path%>/coeuslite/dartmouth/utils/scripts/divSlide.js"></script>
<script src="<%=path%>/coeuslite/dartmouth/utils/scripts/Balloon.js"></script>
<link href="<%=path%>/coeuslite/dartmouth/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css" />
<link
	href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/layout.css"
	rel="stylesheet" type="text/css" />
<link
	href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/styles.css"
	rel="stylesheet" type="text/css" />
<link
	href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/collapsemenu.css"
	rel="stylesheet" type="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
         function selectFile(){
                 checkFormat();
                document.forms[0].fileName.value = document.forms[0].document.value;
                document.forms[0].fileNameHidden.value = document.forms[0].document.value;
            }
         function checkFormat(){
               debugger;
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
                   alert("Please dont upload  "+fileext+" files");
                   return fileext;
               }
          }
         function removeAttachment(entityNum,coiDisclosureNumber,coiSequenceNumber,user)
            {
                var canContinue=true;
                var isViewer='<%=request.getAttribute("isViewer")%>';
                var operationType='<%=request.getAttribute("operationType")%>'
                var loggedPerson='<%=loggedperson%>'
                if(loggedPerson!=user)
                    {
                        alert("You don\'t have permission to remove the attachment")
                        canContinue=false;
                    }
                if(canContinue==true){
                       if (confirm("Are you sure you want to delete the document?")==true){
                        document.forms[0].entityNumber.value = entityNum;
                        document.forms[0].disclosureNumber.value = coiDisclosureNumber;
                        document.forms[0].sequenceNumber.value = coiSequenceNumber;
                        document.forms[0].acType.value = "D";
                        document.forms[0].action= '<%=path%>' +"/saveAttachmentsCoiv2.do?&operationType="+operationType+"&isViewer="+isViewer;
                        document.forms[0].submit();
                    }
                }
            }
         function viewAttachment(entityNum,coiDisclosureNumber,coiSequenceNumber)
            {
                var canView='<%=canView%>'
                if(canView=='true'){
                var operationType='<%=request.getAttribute("operationType")%>'
                var isViewer='<%=request.getAttribute("isViewer")%>';
                window.open("<%=request.getContextPath()%>/viewAttachment.do?&entityNum="+entityNum+"&disclosureNumber="+coiDisclosureNumber+"&SeqNumber="+coiSequenceNumber+"&operationType="+operationType+"&isViewer="+isViewer);
                }
            }
         function saveOrDelete()
            {
                var success= validateSaveOrDelete();
                var isViewer='<%=request.getAttribute("isViewer")%>';
                 
                var operationType='<%=request.getAttribute("operationType")%>';
               
                 if(success==true){
                    document.forms[0].action= '<%=path%>' +"/saveAttachmentsCoiv2.do?&operationType="+operationType+"&isViewer="+isViewer;
                    document.forms[0].submit();
                    return true;
                } else{
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
                 if(document.forms[0].document.value==null || document.forms[0].document.value=='')
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
             function saveAndcontinue()
            {
               
                 if(document.forms[0].document != null) {
                     if(document.forms[0].document.value==null || document.forms[0].document.value==''){
                        document.forms[0].action= '<%=path%>'+"/notes.do";
                        document.forms[0].submit(); }
                    else{

                        if(confirm('Please save the attachment')==true){
                        var test=saveOrDelete();}
                    else{
                        document.forms[0].action= '<%=path%>'+"/notes.do";
                        document.forms[0].submit();
                        }
                    }
                }
                else{
                    document.forms[0].action= '<%=path%>'+"/notes.do";
                    document.forms[0].submit();
                }
            }
        </script>
</head>

<body>
	<html:form action="/saveAttachmentsCoiv2.do"
		enctype="multipart/form-data">

		<%-- <logic:present name="userHasRight">
        <logic:equal name="userHasRight" value="true">--%>


		<logic:notPresent name="showAttachment">
			<table style="width: 100%;" height="100%" border="0" cellpadding="0"
				cellspacing="0" class="table" align='center'>
				<tr>
					<td>
						<table width="100%" border="0" cellpadding="0" cellspacing="0">
							<tr>
								<td height="20" width="50%" align="left" valign="top"
									class="theader">Disclosure Attachment</td>
								<td align="right" width="50%" class="theader"><a
									id="helpPageText" href="#"></a></td>
							</tr>
						</table>
					</td>
				</tr>

				<!-- Add Documents: - Start  -->
				<tr>
					<td colspan="2" align="left" valign="top" class="copybold"><bean:message
							key="uploadDocLabel.AddDocuments" /></td>
				</tr>
				<%String addLink = "javascript:add_documents()";%>

				<tr>
					<td colspan="2">
						<table width="100%" border="0" cellpadding="1" cellspacing="5">
							<tr>
								<td width="12%" align="left" class="copybold" nowrap><bean:message
										key="uploadDocLabel.DocumentType" />:</td>
								<td width="30%" align="left" nowrap>
									<%--Modified DocTypes displaying
                        Earlier no checking done if the collection DocTypes was empty
                        (DocTypes collection is empty when there are no entry in Code Table)- Start--%>
									<logic:notEmpty name="DocTypes">
										<html:select property="docType" name="coiv2Attachment"
											styleClass="textbox-long">
											<html:option value="">
												<bean:message key="specialReviewLabel.pleaseSelect" />
											</html:option>
											<html:options collection="DocTypes" property="code"
												labelProperty="description" />
										</html:select>
									</logic:notEmpty> <logic:empty name="DocTypes">
										<html:select property="docType" name="coiv2Attachment"
											styleClass="textbox-long">
											<html:option value="">
												<bean:message key="specialReviewLabel.pleaseSelect" />
											</html:option>
										</html:select>
									</logic:empty> <%--Modified DocTypes displaying - End --%>
								</td>
								<td width="15%" align=right nowrap class="copybold"><bean:message
										key="uploadDocLabel.Description" />:</td>
								<td width='43%' align=left>&nbsp;<html:text
										property="description" maxlength="150" name="coiv2Attachment"
										size="40" styleClass="textbox-long"></html:text>
								</td>
							</tr>
							<tr>
								<td align="left" nowrap class="copybold"><bean:message
										key="uploadDocLabel.FileName" />:</td>
								<td align="left" class='copy' colspan='3'><html:file
										property="document" onchange="javaScript:selectFile()"
										maxlength="300" size="50" /></td>
							</tr>
							<tr>
								<td align="left" class="copybold" nowrap>Project:</td>
								<td style="font-size: 13px" colspan="3"><logic:present
										name="projectList">
										<logic:notEmpty name="projectList">
											<html:select property="pjtName" styleClass="textbox-long"
												style="width:650;">
												<html:option value="">
													<bean:message key="specialReviewLabel.pleaseSelect" />
												</html:option>
												<html:options collection="projectList" property="code"
													labelProperty="description" />
											</html:select>
										</logic:notEmpty>
									</logic:present> <logic:notPresent name="projectList">
										<%=session.getAttribute("selectedPjct")%>
									</logic:notPresent> <input type="hidden" name="pjtName" id="pjtName"
									value="<%=session.getAttribute("selectedPjct")%>" /></td>
							</tr>
							<tr>
								<%  if(projectType.equalsIgnoreCase("Travel")){ %>
								<td align="left" class="copybold" nowrap colspan="4"></td>
								<%} else {%>
								<td align="left" class="copybold" nowrap>Financial Entity:
								</td>
								<td colspan="3"><logic:present name="FinEntForPerson">
										<logic:notEmpty name="FinEntForPerson">
											<html:select property="entity" styleClass="textbox-long"
												style="width:650;">
												<html:option value="0">
													<bean:message key="specialReviewLabel.pleaseSelect" />
												</html:option>
												<html:options collection="FinEntForPerson" property="code"
													labelProperty="description" />
											</html:select>
										</logic:notEmpty>
										<logic:empty name="FinEntForPerson">
											<html:select property="entity" styleClass="textbox-long"
												style="width:650;">
												<html:option value="0">
													<bean:message key="specialReviewLabel.pleaseSelect" />
												</html:option>
											</html:select>
										</logic:empty>
									</logic:present> <input type="hidden" name="entity" id="entity"
									value="<%=request.getAttribute("entity")%>" disabled="true" />
								</td>
								<%}%>
							</tr>
							<tr>
								<td colspan='4'></td>
							</tr>
						</table>
					</td>
				</tr>
				<tr class='table'>
					<td class='savebutton' colspan="2"><html:button
							property="Save" onclick="javaScript:saveOrDelete();" value="Save"
							styleClass="clsavebutton" /></td>
				</tr>

				<html:hidden name="coiv2Attachment" property="acType" />
				<html:hidden name="coiv2Attachment" property="disclosureNumber" />
				<html:hidden name="coiv2Attachment" property="sequenceNumber" />
				<html:hidden name="coiv2Attachment" property="entityNumber" />
				<html:hidden name="coiv2Attachment" property="fileBytes" />
				<html:hidden name="coiv2Attachment" property="fileNameHidden" />
			</table>
		</logic:notPresent>
		<%--</logic:equal>
     </logic:present>--%>

		<table id="attBodyTable" class="table" style="width: 100%;" border="0">
			<tr style="background-color: #6E97CF; height: 22px;">
				<td
					style="color: #FFFFFF; font-size: 12px; font-weight: bold; padding: 2px 0 2px 10px;"
					colspan="8">List of Attachments</td>
			</tr>
			<tr style="background-color: #6E97CF; height: 22px;">
				<td style="font-size: 12px; font-weight: bold;" width="15%">Attachment
					Type</td>
				<td style="font-size: 12px; font-weight: bold;" width="16%">File
					Name</td>
				<td style="font-size: 12px; font-weight: bold;" width="16%">Description</td>
				<td style="font-size: 12px; font-weight: bold;" width="12%">Project
					#</td>
				<%  if(projectType.equalsIgnoreCase("Travel")){ %>
				<td style="font-size: 12px; font-weight: bold;" width="12%"
					colspan="2">Update Timestamp</td>
				<%} else {%>
				<td style="font-size: 12px; font-weight: bold;" width="12%">Entity
				</td>
				<td style="font-size: 12px; font-weight: bold;" width="12%">Update
					Timestamp</td>
				<%}%>
				<td style="font-size: 12px; font-weight: bold;" width="4%"></td>
				<td style="font-size: 12px; font-weight: bold;" width="10%"></td>
			</tr>
			<logic:notPresent name="attachmentList">
				<tr>
					<td colspan="8"><font color="red">No attachments found</font>
					</td>
				</tr>
			</logic:notPresent>
			<logic:present name="attachmentList">
				<logic:notEmpty name="attachmentList">
					<%
                                String strBgColoratt = "#D6DCE5";
                                int indexatt = 0;
                                Vector attachmentList = (Vector) request.getAttribute("attachmentList");
                    %>
					<logic:iterate id="coiv2Attachment" name="attachmentList">
						<%
                                    if (indexatt % 2 == 0) {
                                        strBgColoratt = "#D6DCE5";
                                    } else {
                                        strBgColoratt = "#DCE5F1";
                                    }
                                    Coiv2AttachmentBean attachmentBean = (Coiv2AttachmentBean) attachmentList.get(indexatt);
                                    
                        %>
						<tr bgcolor="<%=strBgColoratt%>" class="rowLine"
							onmouseover="className='rowHover rowLine'"
							onmouseout="className='rowLine'">
							<td><bean:write name="coiv2Attachment"
									property="docdescription" /></td>
							<td><bean:write name="coiv2Attachment" property="fileName" /></td>
							<td><bean:write name="coiv2Attachment"
									property="description" /></td>
							<td><bean:write name="coiv2Attachment"
									property="moduleItemKey" /></td>
							<%  if(projectType.equalsIgnoreCase("Travel")){ %>
							<td colspan="2"><bean:write name="coiv2Attachment"
									property="updateTimeStamp" />by <bean:write
									name="coiv2Attachment" property="userName" /></td>
							<%} else {%>
							<td><bean:write name="coiv2Attachment" property="entName" /></td>

							<td><bean:write name="coiv2Attachment"
									property="updateTimeStamp" />by <bean:write
									name="coiv2Attachment" property="userName" /></td>
							<%}%>
							<td class="copy">
								<%String link2 = "javaScript:viewAttachment('" + attachmentBean.getEntityNumber() + "','" + attachmentBean.getDisclosureNumber() + "','" + attachmentBean.getSequenceNumber() + "','" + attachmentBean.getUpdateUser() + "')";%>
								<html:link href="<%=link2%>"> View </html:link>
							</td>
							<td class="copy">&nbsp; <%String link3 = "javaScript:removeAttachment('" + attachmentBean.getEntityNumber() + "','" + attachmentBean.getDisclosureNumber() + "','" + attachmentBean.getSequenceNumber() + "','" + attachmentBean.getUpdateUser() + "')";%>
								<html:link href="<%=link3%>"> Remove </html:link>


							</td>
						</tr>
						<%indexatt++;%>
					</logic:iterate>
				</logic:notEmpty>
			</logic:present>
			<tr>
				<td><html:button onclick="javaScript:saveAndcontinue();"
						property="Save" styleClass="clsavebutton" style="width:150px;">
                                             Continue
                                         </html:button></td>
			</tr>
		</table>

	</html:form>
</body>
</html>
