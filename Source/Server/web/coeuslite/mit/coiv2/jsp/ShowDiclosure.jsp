<%--
    Document   : assignDisclView
    Created on : Apr 29, 2010, 2:54:48 PM
    Author     : Mr
--%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page
	import="edu.mit.coeuslite.coiv2.beans.Coiv2NotesBean,java.util.Vector,edu.mit.coeuslite.coiv2.beans.Coiv2AttachmentBean,edu.mit.coeuslite.coiv2.utilities.CoiConstants,edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean;"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>C O I</title>
<%String path = request.getContextPath();%>
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
</head>
<script src="js/jquery.js" type="text/javascript"></script>
<script type="text/javascript">
        function viewAttachment(entityNum,coiDisclosureNumber,coiSequenceNumber)
        {
            // window.location("request.getContextPath()/jsp/down    loadPage.jsp");
            window.open("<%=request.getContextPath()%>/viewAttachment.do?&entityNum="+entityNum+"&disclosureNumber="+coiDisclosureNumber+"&SeqNumber="+coiSequenceNumber);
        }

        function openDiv(divid,height){
            document.getElementById(divid).style.visibility = 'visible';
            document.getElementById(divid).style.height = height;
        }

        function changeColor(id, color){
        <%--alert("color of " + id + "is : "+" "+color);--%>
                element = document.getElementById(id);
                event.cancelBubble = true;
                oldcolor = element.currentStyle.backgroundColor;
                element.style.background = color;
            }
            function selectProj(index){
                var a = "imgtoggle"+index;
                //    alert(a);
                //    alert("=====index===="+index+"  "+lastinx);
                if(document.getElementById(index).style.visibility == 'visible'){
                    document.getElementById(index).style.visibility = 'hidden';
                    document.getElementById(index).style.height = "    1px";
                    document.getElementById(a).src="<%=path%>/coeusliteimages/plus.gif";
                }else{
                    last();
                    ds = new DivSlider();
                    ds.showDiv(index,1000);
                    document.getElementById(index).style.visibility = 'visible';
                    document.getElementById(index).style.height = "10    0px";
                    document.getElementById(a).src="<%=path%>/coeusliteimages/minus.gif";
                }

            }
            function update(){
                debugger;
                var dispSts = document.getElementById("dispositionStatus").value;
                var disclSts = document.getElementById("disclosureStatus").value;
                //alert(dispSts+" "+disclSts);
                if(dispSts != "" && disclSts != ""){
                    document.forms[0].action = "<%=path%>/updateStatus.do?&discNoValue="+disclSts+"&dispoNoValue="+dispSts;
                    document.forms[0].submit();
                }else{
                    alert("Please select Disposition Status and Disclosure Status:");
                }
            }




            function editNotes(entityNum,comments,title,restricedView,notepadEnrtyType)
            {
                document.forms[0].entiryNumber.value = entityNum;
                document.forms[0].comments.value = comments;
                document.forms[0].title.value = title;
                document.forms[0].acType.value = "U";

                document.forms[0].coiNotepadEntryTypeCode.value = notepadEnrtyType;
                if(restricedView!=null && restricedView=="R"){
                    document.forms[0].resttrictedView.checked = true;
                }

            }
            function removeNotes(entityNum,coiDisclosureNumber,coiSequenceNumber)
            {
                var success=validateNotes();
                if(success==true){
                    var isViewer='<%=request.getAttribute("isViewer")%>';
                    var operationType='<%= request.getAttribute("operationType")%>'
                    document.forms[0].entiryNumber.value = entityNum;
                    document.forms[0].coiDisclosureNumber.value = coiDisclosureNumber;
                    document.forms[0].coiSequenceNumber.value = coiSequenceNumber;
                    document.forms[0].acType.value = "R";
                    document.forms[0].action= '<%=path%>' +"/saveNotesCoiv2.do?&operationType="+operationType+"&isViewer="+isViewer;
                    document.forms[0].submit();
                }
            }
            function saveNotes()
            {
                debugger;
                var success=validateNotes();
                var isViewer='<%=request.getAttribute("isViewer")%>';
                if(success==true){
                    var operationType='<%= request.getAttribute("operationType")%>'
                    document.forms[0].action= '<%=path%>' +"/saveNotesCoiv2.do?&operationType="+operationType+"&isViewer="+isViewer;
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
            function setFocus()
            {
                document.forms[0].title.focus();
            }

    </script>
<html:form action="/assignDisclToUser.do">
	<body>
		<div id="content"
			style="padding: 4px; width: 970px; background-color: #D1E5FF">
			<table style="background-color: #D1E5FF">
				<tr>
					<td valign="top">
						<table class="table" align="left" width="200px" bgcolor="#9DBFE9"
							style="padding: 4px;">
							<tr>
								<td>
									<div id="contentleft">
										<table class="table" align="center" width="98%"
											style="padding: 4px; border: 0">
											<tr class="menuHeaderName">
												<td colspan="3">Disclosure View</td>
											</tr>
											<tr class="rowline">
												<td width="16%" height='16' align="left" valign="top"
													class="coeusMenu"></td>
												<td width="80%" height='16' align="left" valign="middle"
													class="coeusMenu"><html:link
														page="/screeningquestions.do">Screening Questions</html:link>
												</td>
												<td width='4%' align=right class="selectedMenuIndicator"></td>
											</tr>
											<tr class="rowLine">
												<td width="16%" height='16' align="left" valign="top"
													class="coeusMenu"></td>
												<td width='80%' align="left" valign="middle"
													class="coeusMenu"><html:link
														page="/financialentities.do"> Financial Entity</html:link>
												</td>
												<td width='4%' align=right class="selectedMenuIndicator"></td>
											</tr>
											<tr class="rowLine">
												<td width="16%" height='16' align="left" valign="top"
													class="coeusMenu"></td>
												<td width="80%" align="left" valign="middle"
													class="coeusMenu"><html:link page="/attachments.do">Attachments</html:link>
												</td>
												<td width='4%' align=right class="selectedMenuIndicator"></td>
											</tr>
											<tr class="rowLine">
												<td width="16%" height='16' align="left" valign="top"
													class="coeusMenu"></td>
												<td align="left" valign="middle" class="coeusMenu"><html:link
														page="/notes.do">Notes</html:link></td>
												<td width='4%' align=right class="selectedMenuIndicator"></td>
											</tr>

											<tr class="rowLine">
												<td width="16%" height='16' align="left" valign="top"
													class="coeusMenu"></td>
												<td width="80%" align="left" valign="middle"
													class="coeusMenu"><html:link
														page="/certifications.do">Certification</html:link></td>
												<td width='4%' align=right class="selectedMenuIndicator"></td>
											</tr>
											<%-- <tr class="rowLine">
                                                      <td width="16%"  height='16' align="left" valign="top" class = "coeusMenu"></td>
                                                     <td width="80%"  align="left" valign="middle" class = "coeusMenu" >
                                                     <html:link page="/disclstatus.do">Disclosure Status</html:link>
                                                       </td>
                                                       <td width='4%' align=right  class ="selectedMenuIndicator" ></td>
                                                 </tr>    <tr class="rowLine">
                                                      <td width="16%"  height='16' align="left" valign="top" class = "coeusMenu"></td>
                                                     <td width="80%"  align="left" valign="middle" class = "coeusMenu" >
                                                     <html:link page="/disposistatus.do">Disposition Status</html:link>
                                                       </td>
                                                       <td width='4%' align=right  class ="selectedMenuIndicator" ></td>
                                                 </tr>--%>
											<logic:present name="<%=CoiConstants.IS_ADMIN%>">
												<logic:equal value="true" name="<%=CoiConstants.IS_ADMIN%>">
													<tr class="rowLine">
														<td width="16%" height='16' align="left" valign="top"
															class="coeusMenu"></td>
														<td width="80%" align="left" valign="middle"
															class="coeusMenu"><html:link page="/setstatus.do">Set Status</html:link>
														</td>
														<td width='4%' align=right class="selectedMenuIndicator"></td>
													</tr>
													<tr class="rowLine">
														<td width="16%" height='16' align="left" valign="top"
															class="coeusMenu"></td>
														<td width="80%" align="left" valign="middle"
															class="coeusMenu"><html:link
																page="/assignviewer.do">Assign Viewer</html:link></td>
														<td width='4%' align=right class="selectedMenuIndicator"></td>
													</tr>
												</logic:equal>
											</logic:present>
											<tr bgcolor="#9DBFE9">
												<td style="height: 410;">&nbsp;</td>
											</tr>
										</table>

									</div>
								</td>
							</tr>
						</table>
					</td>
					<td valign="top">
						<div id="contentright"
							style="padding: 0px; width: 755px; border: 0">
							<table id="bodyTable" class="table" style="width: 755px;"
								border="0">
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
										style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: left; width: 100px;">
										Reporter Name :</td>
									<td
										style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; align: left; width: 150px;">
										<bean:write name="person" property="fullName" />
									</td>
									<td
										style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: left; width: 100px;">
										Department :</td>
									<td
										style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; align: left; width: 150px;">
										<bean:write name="person" property="dirDept" />
									</td>
									<td
										style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: left; width: 100px;">
										Reporter Email :</td>
									<td
										style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; align: left; width: 150px;">
										<bean:write name="person" property="email" />
									</td>
								</tr>
								<tr>
									<td colspan="6"><img height="2" border="0" width="100%"
										src="/coeus44server/coeusliteimages/line4.gif" /></td>
								</tr>
							</table>
							<div class="myForm"
								style="background-color: #9dbfe9; visibility: hidden; height: 1px; width: 755px"
								id="certdiv">
								<logic:present name="certDetView">
									<table id="bodyTable" class="table" style="width: 755px;"
										border="0">
										<tr style="background-color: #6E97CF;">
											<td colspan="6">
												<h1
													style="background-color: #6E97CF; color: #FFFFFF; float: left; font-size: 14px; font-weight: bold; margin: 0; padding: 2px 0 2px 10px; position: relative; text-align: left;">Disclosure
													: Certification</h1>
											</td>
										</tr>
									</table>
									<%--<b>Certification:</b>--%>
									<%-- <div style="width: 755px;height: 22px">
                                         <h1 style="width: 750px;height: 22px;background-color:#6E97CF;color:#FFFFFF;font-size:14px;font-weight:bold;margin: 0;padding: 2px 0 2px 10px;position: relative;text-align: left;">Disclosure : Certification</h1></div>
                                        --%>
									<br />
									<logic:iterate id="certView" name="certDetView">
										<bean:write name="certView" property="certificationText" />
										<br />
										<br />
										<br />
										<b>Certified By:</b>
										<bean:write name="certView" property="certifiedBy" />
										<br />
										<br />
										<br />
										<b>Last Updated :</b>
										<bean:write name="certView" property="certificationTimestamp" />
										<br />
										<br />
										<br />
									</logic:iterate>
								</logic:present>
							</div>
							<div class="myForm"
								style="background-color: #9dbfe9; visibility: hidden; height: 1px; width: 755px"
								id="assignviewerdiv">
								<br /> Select A Disclosure: <br /> <br /> Assign Rights: <select
									styleClass="textbox-long">
									<option>Select</option>
									<option>Add New</option>
									<option>View</option>
									<option>Edit</option>
								</select> <br />
								<br /> Select User: <select styleClass="textbox-long">
									<option>Select</option>
									<option>Coeus</option>
									<option>lijo</option>
									<option>demouser</option>
								</select> <br />
								<br />
								<%--<html:button styleClass="clsavebutton"  value="Set" property=""/><html:button styleClass="clsavebutton"  value="Close" property=""/>--%>
							</div>

							<%--<html:form action="/updateStatus.do">--%>
							<div class="myForm"
								style="background-color: #9dbfe9; visibility: hidden; height: 1px; width: 755px"
								id="setstatusdiv">
								<form action="/updateStatus.do">
									<br />
									<logic:present name="message">
										<logic:equal value="true" name="message">
											<font color="red">Status Updated</font>
										</logic:equal>
										<logic:equal value="false" name="message">
											<font color="red">Status Not Updated</font>
										</logic:equal>
									</logic:present>
									<br /> Disposition Status: <select id="dispositionStatus"
										name="dispDisclStatusForm" styleClass="textbox-long">
										<logic:present name="statusDispDetView">
											<logic:iterate id="statusDispView" name="statusDispDetView">
												<option
													value="<bean:write name="statusDispView" property="disclosureDispositionCode"/>"><bean:write
														name="statusDispView" property="dispositionStatus" /></option>
											</logic:iterate>
										</logic:present>
										<option>Select</option>
										<logic:present name="DisposStatusListView">
											<logic:iterate id="dispView" name="DisposStatusListView">
												<option
													value="<bean:write name="dispView" property="disclosureDispositionCode"/>"><bean:write
														name="dispView" property="dispositionStatus" /></option>
											</logic:iterate>
										</logic:present>
									</select><a href="<%=path%>/setstatus.do">Customize</a> <br />
									<br /> Disclosure Status: <select id="disclosureStatus"
										name="dispDisclStatusForm" styleClass="textbox-long">
										<logic:present name="statusDetView">
											<logic:iterate id="statusView" name="statusDetView">
												<option
													value="<bean:write name="statusView" property="disclosureStatusCode"/>"><bean:write
														name="statusView" property="disclosureStatus" /></option>
											</logic:iterate>
										</logic:present>
										<option>Select</option>
										<logic:present name="DisclStatusListView">
											<logic:iterate id="discView" name="DisclStatusListView">
												<option
													value="<bean:write name="discView" property="disclosureStatusCode"/>"><bean:write
														name="discView" property="disclosureStatus" /></option>
											</logic:iterate>
										</logic:present>
									</select><a href="<%=path%>/setstatus.do">Customize</a> <br />
									<br />
									<html:button styleClass="clsavebutton"
										onclick="javaScript:update();" property="button" value="Set"></html:button>
									<html:button styleClass="clsavebutton" value="Close"
										property="button" />
								</form>
							</div>
							<%--</html:form>--%>



							<div class="myForm"
								style="visibility: hidden; height: 1px; width: 755px"
								id="screeningquestionsdiv">
								<table id="noteBodyTable" class="table" style="width: 755px;"
									border="0">
									<tr style="background-color: #6E97CF; height: 22px;">
										<td
											style="color: #FFFFFF; font-size: 12px; font-weight: bold;">Question
											Id</td>
										<td
											style="color: #FFFFFF; font-size: 12px; font-weight: bold;">Questions</td>
										<td
											style="color: #FFFFFF; font-size: 12px; font-weight: bold;">Answers</td>
									</tr>
									<logic:present name="questionDetView">
										<%
                                                        String qnBgColor = "#DCE5F1";
                                                        int indexqn = 0;
                                                        int qid = 1;
                                            %>
										<logic:iterate id="qnsAnsView" name="questionDetView">
											<%
                                                            if (indexqn % 2 == 0) {
                                                                qnBgColor = "#D6DCE5";
                                                            } else {
                                                                qnBgColor = "#DCE5F1";
                                                            }
                                                %>
											<tr bgcolor="<%=qnBgColor%>" id="row<%=indexqn%>"
												class="rowLine" onmouseover="className='rowHover rowLine'"
												onmouseout="className='rowLine'" height="22px">
												<td><%= qid%></td>
												<td><bean:write name="qnsAnsView" property="question" /></td>
												<td><bean:write name="qnsAnsView"
														property="answerString" /></td>
											</tr>
											<%indexqn++;
                                                            qid++;%>
										</logic:iterate>
									</logic:present>
								</table>
							</div>
							<div class="myForm"
								style="visibility: hidden; height: 1px; vertical-align: top;"
								id="notesdiv">
								<table id="noteBodyTable" class="table" style="width: 500px;"
									border="0">
									<logic:present name="userHaveRight">
										<logic:equal name="userHaveRight" value="true">
											<tr>
												<td align="left" class="copybold" style="width: 50px;">

													Title:</td>
												<td><html:text styleClass="copy" name="coiv2NotesBean"
														property="title" maxlength="500" /></td>
												<td align="left" class="copybold" style="width: 300px;">
													<logic:notEmpty name="notepadTypeList">
                                                        Note Type:
                                                        <html:select
															property="coiNotepadEntryTypeCode" name="coiv2NotesBean"
															style="width:200px" styleClass="textbox-long">
															<html:option value="">
																<bean:message key="specialReviewLabel.pleaseSelect" />
															</html:option>
															<html:options collection="notepadTypeList"
																property="code" labelProperty="description" />>
                                                        </html:select>
													</logic:notEmpty> <logic:empty name="notepadTypeList">
                                                        Note Type:
                                                        &nbsp;<html:select
															property="coiNotepadEntryTypeCode" name="coiv2NotesBean"
															styleClass="textbox-long" style="width:200px">
															<html:option value="">
																<bean:message key="specialReviewLabel.pleaseSelect" />
															</html:option>
														</html:select>
													</logic:empty>
												</td>

												<td align="left" class="copybold" style="width: 150px;">
													Private Flag: <html:checkbox styleClass="copy"
														name="coiv2NotesBean" value="R" property="resttrictedView" />
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
														property="comments" name="coiv2NotesBean" cols="120"
														rows="5" /></td>
											</tr>

											<tr>
												<td class='savebutton'><html:button property="Save"
														styleClass="clsavebutton"
														onclick="javaScript:saveNotes();">
														<bean:message key="protocolNote.button.save" />
													</html:button></td>
											</tr>

										</logic:equal>
									</logic:present>
									<tr style="background-color: #6E97CF; height: 22px;">
										<td
											style="color: #FFFFFF; font-size: 12px; font-weight: bold; width: 15%">Title</td>
										<logic:notEqual name="userHaveRight" value="true">
											<td
												style="color: #FFFFFF; font-size: 12px; font-weight: bold; width: 65%">Comments</td>
										</logic:notEqual>
										<td
											style="color: #FFFFFF; font-size: 12px; font-weight: bold; width: 10%">User</td>
										<td
											style="color: #FFFFFF; font-size: 12px; font-weight: bold; width: 10%">Timestamp</td>
										<logic:present name="userHaveRight">
											<logic:equal name="userHaveRight" value="true">
												<td
													style="color: #FFFFFF; font-size: 12px; font-weight: bold; width: 10%"></td>
												<td
													style="color: #FFFFFF; font-size: 12px; font-weight: bold; width: 10%"></td>
											</logic:equal>
										</logic:present>
									</tr>
									<logic:present name="disclosureNotesData">
										<%
                                                        String notstrBgColor = "#DCE5F1";
                                                        int indexnote = 0;
                                                        int i = 0;
                                                        Vector notesList = (Vector) request.getAttribute("disclosureNotesData");
                                            %>
										<logic:iterate id="disclosureNotes" name="disclosureNotesData"
											type="edu.mit.coeuslite.coiv2.beans.Coiv2NotesBean">
											<%
                                                            if (indexnote % 2 == 0) {
                                                                notstrBgColor = "#D6DCE5";
                                                            } else {
                                                                notstrBgColor = "#DCE5F1";
                                                            }
                                                            Coiv2NotesBean noteBean = (Coiv2NotesBean) notesList.get(i);
                                                            i++;
                                                %>
											<tr bgcolor="<%=notstrBgColor%>" id="row<%=indexnote%>"
												class="rowLine" onmouseover="className='rowHover rowLine'"
												onmouseout="className='rowLine'" height="22px">
												<td><bean:write name="disclosureNotes" property="title" /></td>
												<td><bean:write name="disclosureNotes"
														property="comments" /></td>
												<td><bean:write name="disclosureNotes"
														property="updateUser" /></td>
												<td><bean:write name="disclosureNotes"
														property="updateTimestamp" /></td>
												<logic:present name="userHaveRight">
													<logic:equal name="userHaveRight" value="true">
														<td class="copy" align='left'>
															<%

                                                                        String link = "javaScript:editNotes('" + noteBean.getEntiryNumber() + "','" + noteBean.getComments() + "','" + noteBean.getTitle() + "','" + noteBean.getResttrictedView() + "','" + noteBean.getCoiNotepadEntryTypeCode() + "')";
                                                            %> <html:link
																href="<%=link%>">
                                                                Edit
                                                            </html:link>
														</td>

														<td class="copy" align='left'>
															<%

                                                                        String link1 = "javaScript:removeNotes('" + noteBean.getEntiryNumber() + "','" + noteBean.getCoiDisclosureNumber() + "','" + noteBean.getCoiSequenceNumber() + "')";
                                                            %> <html:link
																href="<%=link1%>">
                                                                Remove
                                                            </html:link>
														</td>
													</logic:equal>
												</logic:present>
											</tr>
											<%indexnote++;%>
										</logic:iterate>
									</logic:present>
								</table>
								<html:text style="visibility: hidden" property="entiryNumber"
									name="coiv2NotesBean" />
								<html:text style="visibility: hidden"
									property="coiDisclosureNumber" name="coiv2NotesBean" />
								<html:text style="visibility: hidden"
									property="coiSequenceNumber" name="coiv2NotesBean" />
								<html:text style="visibility: hidden" property="acType"
									name="coiv2NotesBean" />
							</div>

							<div class="myForm"
								style="visibility: hidden; height: 1px; width: 755px; vertical-align: top;"
								id="attachementdiv">
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
                                                    &nbsp;<html:select
													property="docType" name="coiv2Attachment"
													styleClass="textbox-long">
													<html:option value="">
														<bean:message key="specialReviewLabel.pleaseSelect" />
													</html:option>
													<html:options collection="DocTypes" property="code"
														labelProperty="description" />>
                                                    </html:select>
											</logic:notEmpty> <logic:empty name="DocTypes">
                                                    &nbsp;<html:select
													property="docType" name="coiv2Attachment"
													styleClass="textbox-long">
													<html:option value="">
														<bean:message key="specialReviewLabel.pleaseSelect" />
													</html:option>
												</html:select>
											</logic:empty> <%--Modified DocTypes displaying - End --%>
										</td>
										<td width="15%" align=right nowrap class="copybold">
											<%-- Commented for case id#2627
                                                <font color="red">*</font>
                                                --%> <bean:message
												key="uploadDocLabel.Description" />:
										</td>
										<td width='43%' align=left>&nbsp;<html:textarea
												property="description" name="coiv2Attachment"
												styleClass="copy" cols="40" rows="2"></html:textarea></td>
									</tr>
									<tr>

										<td align="left" nowrap class="copybold">
											<%-- Commented for case id#2627
                                                <font color="red">*</font>
                                                --%> <bean:message
												key="uploadDocLabel.FileName" />:
										</td>

										<td align="left" style='' colspan='3'>&nbsp;<html:file
												property="document" name="coiv2Attachment"
												onchange="selectFile()" maxlength="300" size="50" />
										</td>
									</tr>
									<tr>
										<td>&nbsp;</td>
										<td colspan='3'><html:text property="fileName"
												style="width: 450px;" name="coiv2Attachment"
												styleClass="cltextbox-color" disabled="true" readonly="true" />
										</td>
									</tr>
								</table>
								<html:hidden name="coiv2Attachment" property="acType" />
								<html:hidden name="coiv2Attachment" property="disclosureNumber" />
								<html:hidden name="coiv2Attachment" property="sequenceNumber" />
								<html:hidden name="coiv2Attachment" property="entityNumber" />
								<html:hidden name="coiv2Attachment" property="fileBytes" />
								<html:hidden name="coiv2Attachment" property="fileNameHidden" />
								<b>List of Attachments</b>
								<table id="attBodyTable" class="table" style="width: 755px;"
									border="0">
									<tr style="background-color: #6E97CF; height: 22px;">
										<td
											style="color: #FFFFFF; font-size: 12px; font-weight: bold;">Attachment
											Type</td>
										<td
											style="color: #FFFFFF; font-size: 12px; font-weight: bold;">Description</td>
										<td
											style="color: #FFFFFF; font-size: 12px; font-weight: bold;">User</td>
										<td
											style="color: #FFFFFF; font-size: 12px; font-weight: bold;">Timestamp</td>
										<td>&nbsp;</td>
									</tr>
									<logic:present name="attachmentList">
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
												onmouseout="className='rowLine'" height="22px">
												<td><bean:write name="coiv2Attachment"
														property="docType" /></td>
												<td><bean:write name="coiv2Attachment"
														property="description" /></td>
												<td><bean:write name="coiv2Attachment"
														property="updateUser" /></td>
												<td><bean:write name="coiv2Attachment"
														property="updateTimeStamp" /></td>
												<td class="copy" align='left'>
													<%String link2 = "javaScript:viewAttachment('" + attachmentBean.getEntityNumber() + "','" + attachmentBean.getDisclosureNumber() + "','" + attachmentBean.getSequenceNumber() + "')";%>
													<html:link href="<%=link2%>"> View </html:link>
												</td>
											</tr>
											<%indexatt++;%>
										</logic:iterate>
									</logic:present>
								</table>
							</div>
							<div
								style="width: 755px; height: 500px; overflow-x: hidden; overflow-y: hidden; vertical-align: top;">
								<div class="myForm"
									style="visibility: hidden; height: 1px; width: 755px"
									id="financialentitydiv">
									<table id="bodyTable" class="table"
										style="width: 755px; height: 100%;" border="0">
										<tr style="background-color: #6E97CF; height: 22px;">
											<td>&nbsp;</td>
											<td
												style="background-color: #6E97CF; color: #FFFFFF; float: left; font-size: 14px; font-weight: bold; margin: 0; padding: 2px 0 2px 10px; position: relative; text-align: left;"><strong>Entity
													Names</strong></td>
											<td><strong>&nbsp;</strong></td>
											<td><strong>&nbsp;</strong></td>
										</tr>
										<%--<%
                                                  String [] rowColors = {"#D6DCE5","#DCE5F1"};
                                                  int i=0;
                                                  String rowColor="";
                                                                      %> --%>
										<%--<logic:present name="pendingView">
                                                <b>Pending View</b>
                                            </logic:present>--%>

										<logic:present name="entityNameList">
											<%
                                                            String strBgColor = "#DCE5F1";
                                                            Vector projectNameList = (Vector) request.getAttribute("entityNameList");
                                                            int index = 0;
                                                %>
											<logic:iterate id="pjtTitle" name="entityNameList">
												<%
                                                                if (index % 2 == 0) {
                                                                    strBgColor = "#D6DCE5";
                                                                } else {
                                                                    strBgColor = "#DCE5F1";
                                                                }
                                                    %>

												<tr bgcolor="<%=strBgColor%>" id="row<%=index%>"
													class="rowLine" onmouseover="className='rowHover rowLine'"
													onmouseout="className='rowLine'" height="22px">
													<td colspan="1" style="width: 25px;"><img
														src='<%=request.getContextPath()%>/coeusliteimages/plus.gif'
														border='none' id="imgtoggle<%=index%>"
														name="imgtoggle<%=index%>" border="none"
														onclick="javascript:selectProj(<%=index%>);" /> <img
														src='<%=request.getContextPath()%>/coeusliteimages/minus.gif'
														style="display: none;" border='none'
														id="imgtoggleminus<%=index%>"
														name="imgtoggleminus<%=index%>" border="none"
														onclick="javascript:selectProj(<%=index%>);" /></td>
													<td colspan="3"><bean:write name="pjtTitle" /></td>

													<%--  <td>
                                                      <bean:write name="pjtEntView" property="entityName"/>
                                                          </td>
                                                          <td>
                                                      <bean:write name="pjtEntView" property="entityStatus"/>
                                                          </td>--%>
												</tr>
												<tr>
													<td colspan="4">
														<div id="<%=index%>"
															style="height: 0px; width: 755px; visibility: hidden; background-color: #9DBFE9; overflow-x: hidden; overflow-y: scroll;">
															<%--Project List For <b><bean:write name="pjtTitle"/></b><br/>
                                                                <logic:iterate id="pjtEntView" name="pjtEntDetView">
                                                                    <logic:equal value="my first proposal for a fresh reserach" property="coiProjectTitle" name="pjtEntView">
                                                                        <bean:write name="pjtEntView" property="entityName"/><bean:write name="pjtEntView" property="entityStatus"/><br/>
                                                                    </logic:equal>
                                                                </logic:iterate>--%>

															<table id="bodyTable" class="table"
																style="width: 760px; height: 100%;" border="0">
																<tr>
																	<td
																		style="background-color: #6E97CF; color: #FFFFFF; font-size: 12px; font-weight: bold;"><strong>Project
																			Name</strong></td>
																	<td
																		style="background-color: #6E97CF; color: #FFFFFF; font-size: 12px; font-weight: bold;"><strong>Entity
																			Status</strong></td
																</tr>

																<logic:iterate id="pjtEntView" name="pjtEntDetView">

																	<% String projectName = (String) projectNameList.get(index);%>
																	<logic:equal value="<%=projectName%>"
																		property="entityName" name="pjtEntView">
																		<tr class="rowLineLight"
																			onmouseover="className='rowHover rowLine'"
																			onmouseout="className='rowLineLight'" height="22px">
																			<td><bean:write name="pjtEntView"
																					property="coiProjectTitle" /></td>
																			<td><bean:write name="pjtEntView"
																					property="entityStatus" /></td>
																		</tr>
																	</logic:equal>
																</logic:iterate>

															</table>
														</div>
													</td>
												</tr>
												<%index++;%>
											</logic:iterate>
											<%request.getSession().setAttribute("lastIndex", index);%>
										</logic:present>


									</table>
								</div>
							</div>






							<logic:present name="option">
								<logic:equal name="option" value="overview">
									<script> openDiv("overviewdiv",100)</script>
								</logic:equal>
								<logic:equal name="option" value="screeningquestions">
									<script> openDiv("screeningquestionsdiv",100)</script>
								</logic:equal>
								<logic:equal name="option" value="financialentities">
									<script> openDiv("financialentitydiv",100)</script>
								</logic:equal>
								<logic:equal name="option" value="notes">
									<script> openDiv("notesdiv",300)</script>
								</logic:equal>
								<logic:equal name="option" value="attachments">
									<script> openDiv("attachementdiv",100)</script>
								</logic:equal>
								<logic:equal name="option" value="cert">
									<script> openDiv("certdiv",225)</script>
								</logic:equal>
								<logic:equal name="option" value="disclstatus">
									<script> openDiv("disclstatusdiv",100)</script>
								</logic:equal>
								<logic:equal name="option" value="disposistatus">
									<script> openDiv("disposistatusdiv",100)</script>
								</logic:equal>
								<logic:equal name="option" value="setstatus">
									<script> openDiv("setstatusdiv",150)</script>
								</logic:equal>
								<logic:equal name="option" value="assignviewer">
									<script> openDiv("assignviewerdiv",200)</script>
								</logic:equal>
							</logic:present>


						</div>
					</td>
				</tr>
			</table>
		</div>
		<script>
                function last(){
                    var lastinx = "<%=request.getSession().getAttribute("lastIndex")%>";
                    //   alert(lastinx);
                    for(var i=0;i<lastinx;i=i+1){
                        //    alert(i);
                        var b = "imgtoggle"+i;
                        if(document.getElementById(i).style.visibility == 'visible'){
                            document.getElementById(i).style.visibility = 'hidden';
                            document.getElementById(i).style.height = "1px";
                            document.getElementById(b).src="<%=path%>/coeusliteimages/plus.gif";
                        }}}
            </script>
	</body>
</html:form>
</html>
