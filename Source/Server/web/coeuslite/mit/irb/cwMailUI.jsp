<!--  Copyright (c) Massachusetts Institute of Technology
  77 Massachusetts Avenue, Cambridge, MA 02139-4307
All rights reserved.-->
<%@ page import="org.apache.struts.taglib.html.JavascriptValidatorTag"%>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="personList" scope="session"
	class="edu.mit.coeus.utils.CoeusVector" />
<jsp:useBean id="RoleList" scope="request"
	class="edu.mit.coeus.utils.CoeusVector" />
<jsp:useBean id="QualifierData" scope="request"
	class="edu.mit.coeus.utils.CoeusVector" />
<jsp:useBean id="errMsg" scope="request" class="java.lang.String" />
<jsp:useBean id="FILEPATH" scope="request" class="java.lang.String" />
<bean:size id="qualifierSize" name="QualifierData" />
<%@page import="java.util.HashMap"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html:html>
<head>
<title><bean:message key="mailNotification.mailTitle" /></title>
<html:base />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<link href="/coeuslite/mit/utils/css/ipf.css" rel="stylesheet"
	type="text/css">
<script>
            function getData(value) {
                if(value == 'qualifier') {
                    if(document.getElementById("qualifier") != null) {
                        var qualifier = document.getElementById("qualifier").value;
                        document.mailNotificationForm.qualifierCode.value = qualifier;
                        if('<%=qualifierSize%>' > 0 && (qualifier== 'null' || qualifier == '')){
                            document.mailNotificationForm.Search.disabled = true;
                        }else{
                            document.mailNotificationForm.Search.disabled = false;
                        }   
                    }
                } else if(value == 'search') {
                    document.mailNotificationForm.qualifierCode.value = document.getElementById("qualifier").value;
                    document.mailNotificationForm.actionType.value = 'PERSONINFO';
                } else if(value == 'clear') {
                    document.mailNotificationForm.roleCode.value = "";
                    document.mailNotificationForm.actionType.value = "";
                } else if(value == 'role') {
                    if(document.getElementById("role") != null) {
                        document.mailNotificationForm.roleCode.value = document.getElementById("role").value;
                    }
                    document.mailNotificationForm.actionType.value = 'AddRole';
                    document.mailNotificationForm.submit();
                }
                
            }
            var errValue = false;
            var personAdded = false;
            function toggle_visibility(id) {
            var e = document.getElementById(id);
            if(e.style.display == 'none')
            e.style.display = 'block';
            else
            e.style.display = 'none';
            }
            function sendMail(action) {
                if(action == 'DELETE' ) {
                    if(document.getElementById('recipients').value == "") {
                        alert("Please select a role to delete");
                        return;
                    } else if(confirm("Are you sure you want to delete? ")) {
                        document.mailNotificationForm.mailPersonId.value = document.getElementById('recipients').value;
                    }else{
                        return;
                    }
                }else if(action == 'SEND' ) {
                //Added for COEUSQA-2535 - Nothing happens when clicking send on Protocol Submitted email notification in Lite - start
                    if(document.mailNotificationForm.message.value == ""){
                //Added for COEUSQA-2535 - Nothing happens when clicking send on Protocol Submitted email notification in Lite - end
                        if(!confirm("<bean:message bundle="protocol" key="mailNotification.warning.noBody"/>")){
                            return;
                        }
                    }
                }
                
              //  document.mailNotificationForm.attachFile.value = document.mailNotificationForm.attach.value;
                document.mailNotificationForm.actionType.value = action;
                document.mailNotificationForm.submit();
            }
            function displayMailId(rolecode) {
                var winleft = (screen.width - 450) / 2;
                var winUp = (screen.height - 350) / 2;  
                var win = "scrollbars=yes,width=450,height=350,left="+winleft+",top="+winUp;
                var url_value="<%=request.getContextPath()%>/coeuslite/mit/utils/dialogs/cwMailList.jsp?role="+rolecode;
                window.open(url_value,'',win);
            }
             function searchWindow(value) {
                searchSelection = value;
                var winleft = (screen.width - 650) / 2;
                var winUp = (screen.height - 450) / 2;  
                var win = "scrollbars=1,resizable=1,width=750,height=450,left="+winleft+",top="+winUp;
                if(value == 'person'){
                    sList = window.open('<%=request.getContextPath()%>/irbSearch.do?type=<bean:message key="searchWindow.title.person"/>&search=true&searchName=WEBPERSONSEARCH', "list", win);  
                }else if (value == 'rolodex'){
                    sList = window.open('<%=request.getContextPath()%>/irbSearch.do?type=<bean:message key="searchWindow.title.rolodex"/>&search=true&searchName=WEBROLODEXSEARCH', "list", win);  
                } else if(value == 'unit') {
                    sList = window.open('<%=request.getContextPath()%>/irbSearch.do?type=<bean:message key="searchWindow.title.unit"/>&search=true&searchName=WEBLEADUNITSEARCH', "list", win);  
                } else if(value == 'role') {
                    var winleft = (screen.width - 450) / 2;
                    var winUp = (screen.height - 350) / 2;  
                    var win = "scrollbars=yes,width=450,height=350,left="+winleft+",top="+winUp;
                    window.open('<%=request.getContextPath()%>/roleSearch.do?actionType=ROLESEARCH&search=true', "list", win);
                }
              } 
              
              function fetch_Data(result) {
                var name="";
                var personId="";
                var userId="";
                var flag="";
                if(searchSelection == 'person'){
                    if(result["FULL_NAME"]!="null" && result["FULL_NAME"]!= undefined){
                        name=result["NAME"];
                    }else{
                        name='';
                    }
                    if(result["PERSON_ID"]!='null' && result["PERSON_ID"]!= undefined){
                        personId = result["PERSON_ID"];
                    }else{
                        personId='';
                    }
                    if(result["USER_NAME"]!='null' && result["USER_NAME"]!= undefined){
                        userId = result["USER_NAME"];
                    }else{
                        userId='';
                    }
                } else if (searchSelection == 'rolodex') {
                    if(result["LAST_NAME"]!="null" && result["LAST_NAME"]!= undefined){
                        name=result["LAST_NAME"]+", ";
                    }
                    if(result["FIRST_NAME"]!="null" && result["FIRST_NAME"]!= undefined){
                        name+=result["FIRST_NAME"];
                    }
                    if(result["ROLODEX_ID"]!='null' && result["ROLODEX_ID"]!= undefined){
                        personId = result["ROLODEX_ID"];
                    }else{
                        personId = '';
                    }
                    if(name=='null' || name== undefined || name.length==0){
                        name = result["ORGANIZATION"];
                    }
                    }
                    if(result["EMAIL_ADDRESS"]!='null' && result["EMAIL_ADDRESS"]!= undefined){
                        document.mailNotificationForm.emailId.value = result["EMAIL_ADDRESS"];
                    }else{
                        document.mailNotificationForm.emailId.value = '';
                    }
                    document.mailNotificationForm.mailPersonName.value = name;
                    document.mailNotificationForm.mailPersonId.value = personId;
                    document.mailNotificationForm.mailUserId.value = userId;
                    document.mailNotificationForm.actionType.value = 'PERSON';
                    document.mailNotificationForm.action = "<%=request.getContextPath()%>/mailAction.do";
                    document.mailNotificationForm.submit();
              }
              // Added for COEUQA-3012:Need notification for when a reviewer completes their review in  IACUC - start
              function deleteRole(roleCode,index) {
                if(confirm("Are you sure you want to delete?")) {
                    document.mailNotificationForm.mailPersonId.value = roleCode;
                    document.mailNotificationForm.actionType.value = 'DELETE';
                    document.mailNotificationForm.action = "<%=request.getContextPath()%>/mailAction.do?delIndex="+index;
                    document.mailNotificationForm.submit();
                }
              }
              // Added for COEUQA-3012 - end
              function moveData() {
                var file = document.mailNotificationForm.attach.value;
                var filelist = document.mailNotificationForm.filelist.value;
                var filepath = document.mailNotificationForm.filepath.value;
                var attachfile;
                var attachfilePath;
                if(filelist == "") {
                    attachfile = file;
                } else {
                    attachfile = filelist+";\n"+file;
                }
                if(filepath == "") {
                    attachfilePath = file;
                } else {
                    attachfilePath = filepath+";"+file;
                }
                dataChanged();
                document.mailNotificationForm.filelist.value = attachfile;
                document.mailNotificationForm.filepath.value = attachfilePath;
                document.mailNotificationForm.attach.value = "";
              }
   
        </script>
</head>
<body>
	<%
        String mailRole = null;
        String roleCode = null;
        String subject = null;
        // Variable 'message' renamed to messageBody to avoid the conflict with id Attribute of <html:messages/>  
        // String message = null;
        // Added for COEUQA-3012:Need notification for when a reviewer completes their review in  IACUC - start 
        int index = 0;
        // Added for COEUQA-3012 - end
        String messageBody = null;
        String removeLink = null;
        String qualifier = null;
        String defaultAttachment = null;
        String defaultFile = null;
        HashMap hmContent = (HashMap)request.getAttribute("MailContent");
        if(hmContent != null) {
            subject = (String)hmContent.get("Subject");
            messageBody = (String)hmContent.get("Message");
        }
        if(request.getAttribute("FILEPATH") != null) {
            defaultAttachment = (String)request.getAttribute("FILEPATH");
        }
        if(request.getAttribute("FILENAME") != null) {
            defaultFile = (String) request.getAttribute("FILENAME");
        }
        %>
	<html:form action="/mailAction.do" method="post"
		enctype="multipart/form-data">
		<table width="100%" height="100%" border="0" cellpadding="0"
			cellspacing="0" class="table">
			<tr>
				<td>
					<%-- Added for case# 2238 Displaying Error Messages from Properties File --%>
					<font color="red"> <logic:messagesPresent message="true">
							<html:messages id="message" message="true">
								<li><bean:write name="message" /></li>
							</html:messages>
						</logic:messagesPresent>
				</font>
				</td>
			</tr>
			<tr>
				<td>
					<table width="100%" height="100%" border="0" cellpadding="0"
						cellspacing="0" class="tabtable">
						<tr>
							<td colspan='3' class='tableheader'><bean:message
									key="mailNotification.title" /></td>
						</tr>
						<tr height='5'>
							<td></td>
						</tr>

						<tr>
							<td></td>
							<td nowrap class="copysmall" valign="top" align="left"><html:link
									href="javascript:void(0)" onclick="searchWindow('person')">
									<u><bean:message key="mailNotification.addperson" /></u>
								</html:link>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <html:link
									href="javascript:void(0)" onclick="searchWindow('rolodex')">
									<u><bean:message key="mailNotification.addrolodex" /></u>
								</html:link>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <html:link
									href="javascript:getData('role')">
									<u><bean:message key="mailNotification.addrole" /></u>
								</html:link></td>
						</tr>
						<tr height='10'>
							<td></td>
						</tr>
						<%-- Commented for case# 2238 -Displaying Error Messages from Properties File --%>
						<%--<logic:notEmpty name="errMsg">
                    <tr>
                        <td></td>
                        <td class="copybold">
                            &nbsp;&nbsp;<%=request.getAttribute("errMsg")%>
                        </td>
                    </tr>
                </logic:notEmpty>--%>

						<logic:notEmpty name="RoleList">
							<tr>
								<td></td>
								<td>
									<div id="rolesearch">
										<table class="table">
											<tr height='40'>
												<td class="copybold" align="left">&nbsp;<bean:message
														key="roleData.label.role" />:&nbsp;
												</td>
												<td align="left"><select id="role" class="textbox-long"
													onchange="getData('role')">
														<option value=""><bean:message
																key="generalInfoLabel.pleaseSelect" /></option>
														<logic:present name="RoleList">
															<logic:iterate id="roleData" name="RoleList"
																type="edu.mit.coeus.personroles.bean.PersonRoleInfoBean">
																<%if(roleData.getSelected()) {%>
																<option value="<%=roleData.getRoleCode()%>" selected><%=roleData.getRoleName()%></option>
																<%} else {%>
																<option value="<%=roleData.getRoleCode()%>"><%=roleData.getRoleName()%></option>
																<%}%>
															</logic:iterate>
														</logic:present>
												</select>&nbsp;&nbsp;&nbsp;</td>
												<td class="copybold" align="center"><bean:message
														key="roleData.label.qualifier" />:</td>

												<td align="left"><select id="qualifier"
													class="textbox-long" onchange="getData('qualifier')">
														<option value=""><bean:message
																key="generalInfoLabel.pleaseSelect" /></option>
														<logic:present name="QualifierData">
															<logic:iterate id="qualifierData" name="QualifierData"
																type="edu.mit.coeus.personroles.bean.PersonRoleInfoBean">
																<%if(qualifierData.getSelected()) {%>
																<option value="<%=qualifierData.getQualifierCode()%>"
																	selected><%=qualifierData.getRoleQualifier()%></option>
																<%} else {%>
																<option value="<%=qualifierData.getQualifierCode()%>"><%=qualifierData.getRoleQualifier()%></option>
																<%}%>
															</logic:iterate>
														</logic:present>
												</select> &nbsp;</td>
											</tr>

											<tr height='30'>
												<td></td>
												<td class="copy" align="right"><span class="copy">
														<bean:define id="selectedRole" name="mailNotificationForm"
															property="roleCode" type="java.lang.String" /> <%if(qualifierSize>0 || selectedRole==null || "".equals(selectedRole)){%>
														<html:button property="Search" value="Select"
															onclick="sendMail('ROLE')" styleClass="clbutton"
															disabled="true" /> <%}else{%> <html:button
															property="Search" value="Select"
															onclick="sendMail('ROLE')" styleClass="clbutton" /> <%}%>
												</span> &nbsp;&nbsp;&nbsp;&nbsp;</td>
												<td align="left"><span class="copy"> <html:button
															property="Cancel" value="Cancel"
															onclick="sendMail('CANCEL')" styleClass="clbutton" />
												</span></td>
											</tr>
										</table>
									</div>
								</td>
							</tr>
						</logic:notEmpty>
						<tr height='10'>
							<td></td>
						</tr>
						<tr>
							<td>
						<tr>
							<td nowrap class="copybold" valign="top"><bean:message
									key="mailNotification.label.to" />:</td>
							<td><logic:present name="personList">
									<logic:iterate id="personData" name="personList"
										type="edu.mit.coeus.mailaction.bean.MailActionInfoBean">
										<%
                                mailRole = personData.getRoleDescription();
                                roleCode = personData.getRoleCode();
                                if(mailRole == null) {
                                    mailRole = personData.getPersonName();
                                }
                                qualifier = personData.getQualifier();
                                if(qualifier != null && qualifier.length() > 0) {
                                    mailRole = mailRole + "< "+qualifier+" >";
                                }
                                // Added for COEUQA-3012:Need notification for when a reviewer completes their review in  IACUC - start
                                removeLink = "javascript:deleteRole('"+roleCode+"', '"+index+"')";
                                index++;
                                //Added for COEUQA-3012 - end 
                                %>
										<table width="86%">
											<tr>
												<td nowrap class="copy" width="50%"><script>
                                                if('<%=mailRole%>'.length > 0){
                                                    personAdded = true;
                                                }
                                            </script> <%=mailRole%></td>
												<td nowrap class="copybold" width="50%"><html:link
														href="<%=removeLink%>">
														<bean:message key="mailNotification.remove" />
													</html:link></td>
											</tr>
										</table>
									</logic:iterate>
								</logic:present></td>
						</tr>
						<tr>
							<td nowrap class="copybold"><bean:message
									key="mailNotification.label.subject" />:</td>
							<td><html:text property="subject" styleClass="textbox-mail"
									value="<%=subject%>" onchange="dataChanged()"></html:text></td>
						</tr>
						<tr height='5'>
							<td></td>
						</tr>
						<tr>
							<td nowrap class="copybold" valign="top"><bean:message
									key="mailNotification.label.message" />:</td>
							<td><html:textarea property="message"
									styleClass="textbox-mail" rows="10" value="<%=messageBody%>"
									onchange="dataChanged()" /></td>
						</tr>
						<tr height='5'>
							<td></td>
						</tr>
						<%--    <tr>
                    <td nowrap class="copybold"  valign="top"><bean:message key="mailNotification.label.attachment"/>:
                    </td>
                    <td>
                        <html:textarea property="filelist" readonly="true" styleClass="textboxmail-color" value="<%=defaultFile%>"/>
                    </td>
                </tr>
                <tr>
                    <td>
                    </td>
                    <td>
                        <div id="fileattach" style="display:none">
                            <table cellpadding="0" cellspacing="0" border="0">
                                <tr>
                                    <td  style='copy' colspan='3'>
                                        
                                        <html:file  property="attach" value=""  onchange="moveData()" disabled="false" maxlength="615"  size = "83" ></html:file>
                                        <script>
                                        if(navigator.appName == "Microsoft Internet Explorer")
                                        {
                                            document.mailNotificationForm.attach.size=82;                                
                                        }
                                       </script>  
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </td>
                </tr> 
                 <tr height='5'>
                    <td>                    
                    </td>
                </tr> 
                <tr>
                    <td>
                    </td>
                    <td>                        
                    <html:link href="javascript:void(0)" onclick="toggle_visibility('fileattach')">
                            <u><bean:message key="mailNotification.addattachment"/></u>
                        </html:link>
                        
                     </td>
                     
                </tr> --%>
						<tr height='10'>
							<td></td>
						</tr>
						<tr class="table">
							<td colspan="9" align="left" class="savebutton"><span
								class="copy"> <html:button property="Send" value="Send"
										onclick="sendMail('SEND')" styleClass="clsavebutton" />
							</span></td>
						</tr>

					</table>
				</td>
			</tr>
		</table>
		<html:hidden property="actionType" />
		<html:hidden property="mailPersonId" />
		<html:hidden property="mailUserId" />
		<html:hidden property="mailPersonName" />
		<html:hidden property="emailId" />
		<html:hidden property="attachFile" />
		<html:hidden property="roleCode" />
		<html:hidden property="qualifierCode" />
		<html:hidden property="filepath" value="<%=defaultAttachment%>" />
	</html:form>
	<script>
      DATA_CHANGED = 'false';
      if(personAdded == true){
        DATA_CHANGED = 'true';
       }
      if(errValue) {
        DATA_CHANGED = 'true';
      }
      FORM_LINK = document.mailNotificationForm;
      PAGE_NAME = "<bean:message key="mailNotification.mailTitle"/>";
      function dataChanged(){
        DATA_CHANGED = 'true';
        value = document.mailNotificationForm.attach.value;
        if(value != "null" && value != undefined) {
          LINK = "<%=request.getContextPath()%>/mailAction.do?ConfirmType=SEND&filePath="+value;
        } else {
          LINK = "<%=request.getContextPath()%>/mailAction.do?ConfirmType=SEND";
        }
      }
        </script>
</body>
</html:html>
