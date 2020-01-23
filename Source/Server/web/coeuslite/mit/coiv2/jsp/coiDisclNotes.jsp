<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@ page
	import="edu.mit.coeus.bean.PersonInfoBean,edu.mit.coeuslite.coiv2.beans.Coiv2NotesBean,java.util.Vector,java.util.HashMap,edu.mit.coeuslite.coiv2.utilities.CoiConstants;"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%    Vector notesList = (Vector) request.getAttribute("disclosureNotesData");            
            boolean disable = false;            
            String path = request.getContextPath();
                     PersonInfoBean loggedPer = (PersonInfoBean) session.getAttribute("LOGGED_IN_PERSON");
            String loggedperson=loggedPer.getUserId();

%>
<html:html locale="true">
<head>
<title>Disclosure Notes</title>
<script language="javaScript">
            function editNotes(entityNum,comments,title,restricedView,notepadEnrtyType,user)
            {
                var cannnotContinue='<%=disable%>';
                 var loggedPerson='<%=loggedperson%>'
                if(loggedPerson!=user)
                    {
                        alert("Persission Denied")
                        cannnotContinue='false';
                    }
                if(cannnotContinue=='false'){
                    document.forms[0].entiryNumber.value = entityNum;
                    document.forms[0].comments.value = comments;
                    document.forms[0].title.value = title;
                    document.forms[0].acType.value = "U";

                    document.forms[0].coiNotepadEntryTypeCode.value = notepadEnrtyType;
                    if(restricedView!=null && restricedView=="R"){
                        document.forms[0].resttrictedView.checked = true;
                    }
                }
            }
            function removeNotes(entityNum,coiDisclosureNumber,coiSequenceNumber,user)
            {
                var cannnotContinue='<%=disable%>';
                 var loggedPerson='<%=loggedperson%>'
                if(loggedPerson!=user)
                    {
                        alert("Persission Denied")
                        cannnotContinue='false';
                    }
                if(cannnotContinue=='false'){
                    var operationType='<%= request.getAttribute("operationType")%>'
                    document.forms[0].entiryNumber.value = entityNum;
                    document.forms[0].coiDisclosureNumber.value = coiDisclosureNumber;
                    document.forms[0].coiSequenceNumber.value = coiSequenceNumber;
                    document.forms[0].acType.value = "R";
                    document.forms[0].action= '<%=path%>' +"/saveNotesCoiv2.do?operationType="+operationType;
                    document.forms[0].submit();
                }
            }
            function saveNotes()
            {
                var success=validateNotes();
                if(success==true){
                    var operationType='<%= request.getAttribute("operationType")%>'
                    document.forms[0].action= '<%=path%>' +"/saveNotesCoiv2.do?operationType="+operationType;
                    document.forms[0].submit();
                }

            }
            function validateNotes()
            {
                if(document.forms[0].title.value==null || document.forms[0].title.value.replace(/^\s+/,'').replace(/\s+$/,'')=='')
                {
                    alert('Please enter Title');
                    document.forms[0].title.focus();
                    return false;
           
                }
                if(document.forms[0].coiNotepadEntryTypeCode.value==null || document.forms[0].coiNotepadEntryTypeCode.value.replace(/^\s+/,'').replace(/\s+$/,'')=='')
                {
                    alert('Please enter Note Type');
                    document.forms[0].coiNotepadEntryTypeCode.focus();
                    return false;
                }
                if(document.forms[0].comments.value==null || document.forms[0].comments.value.replace(/^\s+/,'').replace(/\s+$/,'')=='')
                {
                    alert('Please enter Comment');
                    document.forms[0].comments.focus();
                    return false;
                }
              
                return true;
            }
            function saveAndcontinue()
            {
                var operationType= '<%= request.getAttribute("operationType")%>'
                document.forms[0].action= '<%=path%>' +"/getAttachmentsCoiv2.do?operationType="+operationType;
                document.forms[0].submit();
            }
            function setFocus()
            {
                document.forms[0].title.focus();
            }
        </script>

</head>
<body onload="javaScript:setFocus();">
	<td valign="top">
		<table id="bodyTable1" class="table" style="width: 765px;" border="0">
			<tr style="background-color: #6E97CF;">
				<td colspan="6">
					<h1
						style="background-color: #6E97CF; color: #FFFFFF; float: left; font-size: 14px; font-weight: bold; margin: 0; padding: 2px 0 2px 10px; position: relative; text-align: left;">
						Financial Disclosure by
						<bean:write name="person" property="fullName" />
					</h1>
				</td>
			</tr>
			<tr>
				<td
					style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: left">
					Reporter Name :</td>
				<td
					style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; align: left;">
					<bean:write name="person" property="fullName" />
				</td>
				<td
					style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: left">
					Department :</td>
				<td
					style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; align: left;">
					<bean:write name="person" property="dirDept" />
				</td>
				<td
					style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: left">
					Reporter Email :</td>
				<td
					style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; align: left;">
					<bean:write name="person" property="email" />
				</td>
			</tr>
			<tr>
				<td
					style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: left">
					Office Location :</td>
				<td
					style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; align: left;">
					<bean:write name="person" property="offLocation" />
				</td>
				<td
					style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: left">
					Office Phone :</td>
				<td
					style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; align: left;">
					<bean:write name="person" property="offPhone" />
				</td>
				<td
					style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: left">
					Secondary Office:</td>
				<td
					style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; align: left;">
					<bean:write name="person" property="secOffLoc" />
				</td>
			</tr>
			<tr>
				<td colspan="6"><img height="2" border="0" width="100%"
					src="/coeus44server/coeusliteimages/line4.gif" /></td>
			</tr>
		</table> <html:form action="/saveNotesCoiv2.do">
			<div id="notesProtocol">
				<table style="width: 765px;" height="100%" border="0"
					cellpadding="0" cellspacing="0" class="table" align='center'>

					<tr>
						<td>
							<table width="100%" border="0" cellpadding="0" cellspacing="0">
								<tr>
									<td height="20" width="50%" align="left" valign="top"
										class="theader">Disclosure Notes</td>
									<td align="right" width="50%" class="theader"><a
										id="helpPageText" href="#"> <bean:message
												key="helpPageTextProtocol.help" />
									</a></td>
								</tr>
							</table>
						</td>
					</tr>



					<tr class="copy">
						<td align="left" width='99%'></td>
					</tr>



					<tr class='copybold' align='left' width='100%'>
						<td></td>
					</tr>

					<!-- Add Disclosure Notes - Start  -->
					<tr>
						<td align="left" valign="top" class='core'>
							<div id='open_window'>
								<table width="100%" border="0" align="center" cellpadding="0"
									cellspacing="0" class="tabtable">
									<tr>
										<td>
											<div id="helpText" class='helptext'>&nbsp;&nbsp;
												Disclosure Notes</div>
										</td>
									</tr>

									<tr>
										<td colspan="4" align="left" valign="top">
											<table width="100%" height="20" border="0" cellpadding="0"
												cellspacing="0" class="tableheader">
												<tr>
													<td><bean:message key="protocolNote.label.addNotes" />
													</td>
												</tr>
											</table>
										</td>
									</tr>

									<tr>
										<td>
											<table width="100%" border="0" cellpadding="0">
												<html:text style="visibility: hidden"
													property="entiryNumber" />
												<html:text style="visibility: hidden"
													property="coiDisclosureNumber" />
												<html:text style="visibility: hidden"
													property="coiSequenceNumber" />
												<html:text style="visibility: hidden" property="acType" />
												<tr>
													<td align="left" class="copybold" style="width: 50px;">

														Title:</td>
													<td><html:text styleClass="copy"
															disabled="<%=disable%>" property="title" maxlength="500" />
													</td>
													<td align="left" class="copybold" style="width: 300px;">



														<logic:notEmpty name="notepadTypeList">
                                                                Note Type:
                                                                <html:select
																property="coiNotepadEntryTypeCode" style="width:200px"
																styleClass="textbox-long" disabled="<%=disable%>">
																<html:option value="">
																	<bean:message key="specialReviewLabel.pleaseSelect" />
																</html:option>
																<html:options collection="notepadTypeList"
																	property="code" labelProperty="description" />>
                                                                </html:select>
														</logic:notEmpty> <logic:empty name="notepadTypeList">
                                                                Note Type:
                                                                &nbsp;<html:select
																property="coiNotepadEntryTypeCode"
																disabled="<%=disable%>" styleClass="textbox-long"
																style="width:200px">
																<html:option value="">
																	<bean:message key="specialReviewLabel.pleaseSelect" />
																</html:option>
															</html:select>
														</logic:empty>
													</td>

													<td align="left" class="copybold" style="width: 150px;">
														Private Flag: <html:checkbox styleClass="copy"
															disabled="<%=disable%>" value="R"
															property="resttrictedView" />
													</td>
													<td align="left" style="width: 50px;">&nbsp;</td>
													<td>&nbsp;</td>
												</tr>
												<tr>
													<td class="copybold">
														<%-- Commented for case id#2627
                                                            <font color="red">*</font>
                                                            --%> <bean:message
															key="protocolNote.label.comment" />:
													</td>
													<td colspan="4"><html:textarea styleClass="copy"
															disabled="<%=disable%>" property="comments" cols="120"
															rows="5" /></td>
												</tr>

											</table>
										</td>
									</tr>
									<tr>
										<td height='10'>&nbsp;</td>
									</tr>

									<tr class="table">
										<td class='savebutton'><html:button property="Save"
												styleClass="clsavebutton" disabled="<%=disable%>"
												onclick="javaScript:saveNotes();">
												<bean:message key="protocolNote.button.save" />
											</html:button></td>
									</tr>
								</table>
							</div>
						</td>
					</tr>
					<!-- Add Disclosure Notes - Ends  -->



					<!-- List of Disclosure Notes Start -->
					<tr>
						<td align="left" valign="top" class='core'>
							<table width="100%" border="0" align="center" cellpadding="0"
								cellspacing="0" class="tabtable">

								<tr>
									<td colspan="5" align="left" valign="top">
										<table width="100%" height="20" border="0" cellpadding="0"
											cellspacing="0" class="tableheader">
											<tr>
												<td>List of Disclosure Notes</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td height='20'></td>
								</tr>

								<tr>
									<td class="theader" align='left'><bean:message
											key="protocolNote.label.comment" /></td>
									<td class="theader" align='left'>Title</td>
									<td class="theader" align='left'><bean:message
											key="protocolNote.label.time" /></td>

									<td width="10%" class="theader" align='left'>
										<%--<bean:message key="protocolNote.label.restricted"/>--%>
									</td>

									<td class="theader" align='left'>
										<%--<bean:message key="protocolNote.label.view"/>--%>
									</td>
								</tr>
								<%
                                                String strBgColor = "#D6DCE5";
                                                int index = 0;
                                                int i = 0;

                                    %>
								<logic:present name="disclosureNotesData">
									<logic:iterate id="disclosureNotes" name="disclosureNotesData"
										type="edu.mit.coeuslite.coiv2.beans.Coiv2NotesBean">
										<%

                                                        if (index % 2 == 0) {
                                                            strBgColor = "#D6DCE5";
                                                        } else {
                                                            strBgColor = "#DCE5F1";
                                                        }
                                                        Coiv2NotesBean noteBean = (Coiv2NotesBean) notesList.get(i);
                                                        i++;
                                            %>
										<tr bgcolor="<%=strBgColor%>">
											<td class="copy" align='left'><bean:write
													name="disclosureNotes" property="comments" /></td>
											<td class="copy" align='left'><bean:write
													name="disclosureNotes" property="title" /></td>
											<td class="copy" align='left'><bean:write
													name="disclosureNotes" property="updateTimestamp" /></td>

											<td class="copy" align='left'>
												<%

                                                                String link = "javaScript:editNotes('" + noteBean.getEntiryNumber() + "','" + noteBean.getComments() + "','" + noteBean.getTitle() + "','" + noteBean.getResttrictedView() + "','" + noteBean.getCoiNotepadEntryTypeCode() + "','" + noteBean.getUpdateUser() + "')";
                                                    %> <html:link
													href="<%=link%>">
                                                        Edit
                                                    </html:link>
											</td>

											<td class="copy" align='left'>
												<%

                                                                String link1 = "javaScript:removeNotes('" + noteBean.getEntiryNumber() + "','" + noteBean.getCoiDisclosureNumber() + "','" + noteBean.getCoiSequenceNumber() + "','" + noteBean.getUpdateUser() + "')";
                                                    %> <html:link
													href="<%=link1%>">
                                                        Remove
                                                    </html:link>
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
								<td class='savebutton'><html:button
										onclick="javaScript:saveAndcontinue();" property="Save"
										styleClass="clsavebutton">
                                            Continue
                                        </html:button></td>
							</table>
						</td>
					</tr>
					<!-- List of Disclosure Notes End -->

				</table>
			</div>

		</html:form>
	</td>
	</tr>
	</table>
</body>
</html:html>