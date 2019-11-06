<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.Vector, java.util.HashMap"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%--4331: PI cannot view long comment in approval comments thru CoeusLite IRB - Start --%>
<%-- <jsp:useBean  id="protocolActionDetails"  scope="request" class="java.util.Vector"/> --%>
<jsp:useBean id="protocolActionDetails" scope="session"
	class="java.util.Vector" />
<%--4331: PI cannot view long comment in approval comments thru CoeusLite IRB  - End --%>
<jsp:useBean id="protocolSubmissionDetails" scope="request"
	class="java.util.Vector" />
<html:html>
<head>
<title>Coeus Web</title>
<script
	src="<%=request.getContextPath()%>/coeuslite/mit/utils/scripts/modal/modaldbox.js"></script>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/coeuslite/mit/utils/scripts/modal/modaldbox.css"
	type="text/css" />
<script language="JavaScript" type="text/JavaScript">
            function view_data(protocolNumber, sequenceNumber, actionId, protoCorrespTypeCode){
            window.open("<%=request.getContextPath()%>/getProtocolAction.do?&protocolNumber="+protocolNumber+"&acType=V&sequenceNumber="+sequenceNumber+"&actionId="+actionId+"&protoCorrespTypeCode="+protoCorrespTypeCode); 
            }
        </script>
<script>
            //Added for View Submission details
            function showHide(val,value){
            var panel = 'Panel'+value;
            var pan = 'pan'+value;
            var hidePanel  = 'hidePanel'+value;
            if(val == 1){
                        document.getElementById(panel).style.display = "none";
                        document.getElementById(hidePanel).style.display = "block";
                        document.getElementById(pan).style.display = "block";
                    }
            else if(val == 2){
                        document.getElementById(panel).style.display = "block";
                        document.getElementById(hidePanel).style.display = "none";
                        document.getElementById(pan).style.display = "none";
                    }        

            }
            //Added for View Submission details
            //Added for case#3046 - Notify IRB attachments - start
            function viewDocument(protocolNumber, sequenceNumber, submissionNumber, documentId, fileName){
                window.open("<%=request.getContextPath()%>/viewProtoSubAtt.do?protocolNumber="
                                                                                        +protocolNumber
                                                                                        +"&sequenceNumber="
                                                                                        +sequenceNumber
                                                                                        +"&submissionNumber="
                                                                                        +submissionNumber
                                                                                        +"&documentId="
                                                                                        +documentId
                                                                                        +"&fileName="
                                                                                        +fileName);
            }   
            //Added for case#3046 - Notify IRB attachments - end
            
            // Case 4332: Unable to view the full comments entered by the IRB when action is taken on a submission - Start
            function view_comments(commentIndex) {
      
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
        
                var loc = '<bean:write name='ctxtPath'/>/coeuslite/mit/utils/dialogs/cwViewComments.jsp?value='+commentIndex;
                var newWin = window.open(loc,"formName",'dependent=1,toolbar=0,location=0,directories=0,status=0, menubar=0,scrollbars=0,resizable=0,width=' + w + ',height=' + h+ ',left='+ leftPos + ',top=' + topPos);
                newWin.window.focus();
         
            }
            // Case 4332 : Unable to view the full comments entered by the IRB when action is taken on a submission - End
          
            //Added for COEUSDEV-352 : Can only view one attachment for Notify IRB in Protocol History - Start
            function showDialog(divId){
                //Default w,h is for 1024 by 768 pixels screen resolution
                var w = 220;
                var h = 475;
                var screenWidth = window.screen.width;
                var screenHeight = window.screen.height;
                if(screenWidth == 800 && screenHeight == 600){
                    w = 2;
                    h = 300;
                } else if(screenWidth == 1152 && screenHeight == 864){
                    w = 350;
                    h = 565;
                } else if(screenWidth == 1280 && screenHeight == 720){
                    w = 475;
                    h = 425;
                }else if(screenWidth == 1280 && screenHeight == 768){
                    w = 475;
                    h = 475;
                }else if(screenWidth == 1280 && screenHeight == 1024){
                    w = 475;
                    h = 725;
                }
                //width, height is for pop-up dialog 
                var width =  Math.floor(((screenWidth - w) / 2));
                var height = Math.floor(((screenHeight - h) / 2));
                sm(divId,width,height);
            }
            //Added for COEUSDEV-352 : Can only view one attachment for Notify IRB in Protocol History - End
            // Coeusdev-86: Questionnaire for submission - Start
            function viewAnsweredQuestionsForQnr(qnrLink, submissionNumber) {
      
                var w = 830;
                var h = 513;
                if(navigator.appName == "Microsoft Internet Explorer") {
                    w = 830;
                    h = 563;
                }
                if (window.screen) {
                    /*leftPos = Math.floor(((window.screen.width )/ 2));
                    topPos = Math.floor(((window.screen.height) ));
                    alert(leftPos);*/
                    
                    leftPos = 115;
                    topPos = 120;
                }
               
                var loc = '<bean:write name='ctxtPath'/>/coeuslite/mit/utils/dialogs/cwViewAnsweredQuestionsForQnr.jsp?submissionNumber='+submissionNumber+'&linkFromHistoryPage=true&link='+qnrLink;
                var newWin = window.open(loc,"formName",'dependent=1,toolbar=0,location=0,directories=0,status=0, menubar=0,scrollbars=1,resizable=0,width=' + w + ',height=' + h+ ',left='+ leftPos + ',top=' + topPos);
                newWin.window.focus();
         
            }
            // Coeusdev-86: Questionnaire for submission - End
        </script>
</head>
<body>
	<%
        String docDescription = "";
        String viewLink = "";
        String viewEnable = "";
        String comments = "";
        //Added for view Submission
        String plus = request.getContextPath()+"/coeusliteimages/plus.gif";
        String minus = request.getContextPath()+"/coeusliteimages/minus.gif";
        //Added for view Submission
        //Added for case#3046 - Notify IRB attachments
        String protocolActionCode = "";
        //Added for case#3072 - Documents Premium - Final flag is not sticking
        Vector vecCorrespondences = null;
    %>
	<html:form action="/getProtocolAction.do" method="post">
		<table width="100%" height="100%" border="0" cellpadding="0"
			cellspacing="0" class="table">
			<tr>
				<td height="2%" align="left" valign="top" class="theader"><bean:message
						key="protocolActions.ProtocolAction" /></td>
			</tr>
			<tr align="center">
				<td colspan="3">
					<table width="100%" border="0" cellpadding="2" cellspacing='3'
						class="tabtable">
						<tr>
							<td width="30%" align="left" class="theader"><bean:message
									key="protocolActions.Description" /></td>
							<td width="20%" align="left" class="theader"><bean:message
									key="protocolActions.Date" /></td>
							<td width="20%" align="left" class="theader"><bean:message
									key="protocolActions.ActionDate" /></td>
							<td width="15%" align="left" class="theader"><bean:message
									key="protocolActions.Comments" /></td>
							<td width="5%" align="center" class="theader">&nbsp;</td>
						</tr>
						<% 
                    //BGCOLOR=EFEFEF  FBF7F7
                    String STRBGCOLOR = "#DCE5F1";
                    int INDEX = 1;
                %>
						<logic:present name="protocolActionDetails">
							<logic:iterate id="protocolList" name="protocolActionDetails"
								type="java.util.HashMap">
								<% 
                           if (INDEX%2 == 0) 
                              STRBGCOLOR = "#D6DCE5"; 
                           else 
                              STRBGCOLOR="#DCE5F1"; 
                              
                              docDescription = (String) protocolList.get("Description");
                              docDescription = ((docDescription != null && docDescription.length() > 60) ? (docDescription.substring(0,60)+"...") :docDescription);
                              comments = (String) protocolList.get("Comments");
                              // Case 4332: Unable to view the full comments entered by the IRB when action is taken on a submission 
                              //comments = ((comments != null && comments.length() > 50) ? (comments.substring(0,40)+"...") :comments);
                              protocolActionCode = protocolList.get("ProtocolActionCode").toString();     
                              //Added for case#3072 - Documents Premium - Final flag is not sticking
                              vecCorrespondences = (Vector)protocolList.get("Correspondences");
                              %>


								<%if(docDescription != null || protocolList.get("Date") != null || protocolList.get("ActionDate")!= null || comments != null){// added this during view Submission%>

								<tr bgcolor="<%=STRBGCOLOR%>" valign="top"
									onmouseover="className='TableItemOn'"
									onmouseout="className='TableItemOff'">
									<!-- modified for View Submission-->
									<td align="left" nowrap class="copy" width="30%">
										<%String divName="Panel"+INDEX;%>
										<div id='<%=divName%>'>

											<%String divlink = "javascript:showHide(1,'"+INDEX+"')";%>
											<html:link href="<%=divlink%>">
												<!-- Modified for Case#3291 - Start
                                                            To avoid the double quotes when setting the attributes to img tag-->
												<%String imagePlus=request.getContextPath()+"/coeusliteimages/plus.gif";%>
												<html:img src="<%=imagePlus%>" border="0" />
												<%--<html:img src="<%=request.getContextPath()+"/coeusliteimages/plus.gif"%>" border="0"/> --%>
												<!-- Modified for Case#3291 - End -->
											</html:link>
											&nbsp;&nbsp;<%=docDescription%>
										</div> <% String divsnName="hidePanel"+INDEX;%>
										<div id='<%=divsnName%>' style='display: none;'>

											<% String divsnlink = "javascript:showHide(2,'"+INDEX+"')";%>
											<html:link href="<%=divsnlink%>">
												<!-- Modified for Case#3291 - Start
                                                    To avoid the double quotes when setting the attributes to img tag-->
												<%String imageMinus=request.getContextPath()+"/coeusliteimages/minus.gif";%>
												<html:img src="<%=imageMinus%>" border="0" />
												<%--<html:img src="<%=request.getContextPath()+"/coeusliteimages/minus.gif"%>" border="0"/> --%>
												<!-- Modified for Case#3291 - End -->
											</html:link>
											&nbsp;&nbsp;<%=docDescription%>
										</div>
									</td>
									<!-- modified for View Submission-->
									<%--td align="left" nowrap class="copy" width="30%"><%=docDescription%></td--%>
									<td width="5%" align="left" nowrap class="copy"><%=protocolList.get("Date")%></td>
									<td width="5%" align="left" nowrap class="copy"><%=protocolList.get("ActionDate")%></td>
									<%-- Case 4332: Unable to view the full comments entered by the IRB when action is taken on a submission - Start --%>
									<%-- <td width="45%" align="left" class="copy"><%=comments%></td> --%>
									<td width="45%" align="left" class="copy">
										<%if(comments != null && comments.length() > 38){%> <%=comments.substring(0,38)+"...."%>
										<%}else{%> <%=comments%> <%}
                                comments = comments.replaceAll("\"", "");
                                comments = comments.replaceAll("\'", "");
                                %>
									</td>
									<td>
										<%
                                // 4331: PI cannot view long comment in approval comments thru CoeusLite IRB - Start
                                // String commentLink = "javascript:view_comments('" +comments +"')";
                                String commentLink = "javascript:view_comments('" +INDEX +"')";
                                // 4331: PI cannot view long comment in approval comments thru CoeusLite IRB - End
                                %> <html:link href="<%=commentLink%>">
											<bean:message key="label.View" />
										</html:link>
									</td>
									<%-- Case 4332: Unable to view the full comments entered by the IRB when action is taken on a submission - End --%>
								</tr>

								<%}// added this during view Submission %>
								<!--Added here View Submission Details-->
								<tr>
									<td colspan="5">
										<%String divisionName="pan"+INDEX;%>
										<div id='<%=divisionName%>' style='display: none;'>
											<%boolean isEntered = false;%>
											<logic:notEmpty name="protocolSubmissionDetails">
												<logic:iterate id="submissionList"
													name="protocolSubmissionDetails"
													type="org.apache.struts.action.DynaActionForm">
													<%
                                            int submNumber = Integer.parseInt(submissionList.get("submissionNumber").toString());                                            
                                            //System.out.println("sub num"+protocolList.get("SubmissionNumber"));                                                                                                              
                                            if(new Integer(submNumber).equals(protocolList.get("SubmissionNumber"))){
                                            %>
													<table width="100%" height="100%" border="0"
														cellpadding="1" cellspacing="3">
														<tr class="theader">
															<td><bean:message
																	key="protocolActions.SubmissionDetails" /></td>
														</tr>
														<tr>
															<td>
																<table width="100%" height="100%" border="0"
																	cellpadding="2" cellspacing="3" class="tabtable">
																	<tr class="copy">
																		<td><b><bean:message
																					key="protocolActions.SubmissionType" /> :</b> &nbsp;<bean:write
																				name="submissionList"
																				property="submissionDescription" /></td>
																		<td colspan="2"><b><bean:message
																					key="protocolActions.ReviewType" /> :</b>&nbsp; <bean:write
																				name="submissionList" property="reviewDescription" /></td>
																	</tr>
																	<tr class="copy">
																		<td colspan="4"><b><bean:message
																					key="protocolActions.TypeQualifier" /> :</b>&nbsp;<bean:write
																				name="submissionList" property="qualsDescription" />
																		</td>
																	</tr>
																	<tr class="copy">
																		<td colspan="3"><b><bean:message
																					key="protocolActions.SubmissionStatus" /> : </b>&nbsp;<bean:write
																				name="submissionList"
																				property="submissionStatusDescription" /></td>
																	</tr>
																	<tr class="copy">
																		<td><b><bean:message
																					key="protocolActions.Date" /> :</b>&nbsp;<coeusUtils:formatDate
																				name="submissionList" property="submissionDate" /></td>
																		<td colspan="2"><b> <bean:message
																					key="protocolActions.Place" /> :
																		</b> <bean:write name="submissionList" property="place" /></td>
																	</tr>
																	<tr class="copy">
																		<td><b><bean:message
																					key="protocolActions.CommitteeId" /> : </b>&nbsp;<bean:write
																				name="submissionList" property="committeeId" /></td>
																		<td><b><bean:message
																					key="protocolActions.ScheduleId" /> :</b>&nbsp;<bean:write
																				name="submissionList" property="scheduleId" /></td>
																		<td><b><bean:message
																					key="protocolActions.ScheduleDate" /> :</b>&nbsp;<coeusUtils:formatDate
																				name="submissionList" property="scheduleDate" /></td>
																	</tr>
																	<tr class="copy">
																		<td colspan="3"><b><bean:message
																					key="protocolActions.CommitteeName" /> :</b>
																		<bean:write name="submissionList"
																				property="committeeName" /></td>
																	</tr>
																</table>
															</td>
														</tr>
														<%--Commented code for case # 3048 - Remove Reviewers List and Voting Details - Start --%>
														<%--tr class="theader"><td><bean:message key="protocolActions.ReviewerList"/> </td></tr>
                                            <tr>
                                                <td>
                                                    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tabtable">
                                                        
                                                        <logic:notEmpty name="submissionList" property="reviewerList">
                                                        <logic:iterate id="reviewers" property="reviewerList" name="submissionList" type="org.apache.struts.action.DynaActionForm">
                                                        <tr>
                                                            <td class="copy"><bean:write name="reviewers" property="reviewerPersonName"/></td><td align="left" class="copy"> <bean:write name="reviewers" property="reviewerTypeDesc"/></td>
                                                        </tr>
                                                        </logic:iterate>
                                                        </logic:notEmpty>
                                                        
                                                        <logic:empty name="submissionList" property="reviewerList">
                                                            <tr>
                                                                <td><bean:message key="protocolActions.NoReviewersList"/></td>
                                                            </tr>
                                                        </logic:empty>
                                                        <!--iterate ends here-->
                                                    </table>
                                                </td>
                                            </tr--%>
														<%--tr class="theader"><td><bean:message key="protocolActions.VotingDetails"/> </td></tr>
                                            <tr>
                                                <td>
                                                    <table width="100%" border="0" cellpadding="0" cellspacing="2" class="tabtable">
                                                        <tr>
                                                            <td class="copy">
                                                                <b><bean:message key="protocolActions.Yes"/> :</b>&nbsp;<bean:write name="submissionList" property="yesVoteCount"/> &nbsp;&nbsp;&nbsp;
                                                                <b><bean:message key="protocolActions.No"/> : </b>&nbsp;<bean:write name="submissionList" property="noVoteCount"/> &nbsp;&nbsp;&nbsp;
                                                                <b><bean:message key="protocolActions.Abstainer"/> : </b>&nbsp;<bean:write name="submissionList" property="abstainerCount"/>&nbsp;&nbsp;&nbsp;
                                                                <b><bean:message key="protocolActions.VotingComments"/> : </b>&nbsp;<bean:write name="submissionList" property="votingComments"/> 
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr--%>
														<!-- Added for case#3072 - Documents Premium - Final flag is not sticking - start -->
														<%if(vecCorrespondences != null && vecCorrespondences.size() > 0){%>
														<tr class="theader">
															<td><bean:message
																	key="protocolAction.label.Correspondence" /></td>
														</tr>

														<tr>
															<td>
																<table width="100%" border="0" cellpadding="0"
																	cellspacing="0" class="tabtable">
																	<logic:iterate property="Correspondences"
																		id="correspondenceList" name="protocolList"
																		type="java.util.HashMap">
																		<%                                    
                                                        viewLink = "javascript:view_data('" +protocolList.get("protocolNumber") +"',"+"'"+protocolList.get("sequenceNumber")+"','"+protocolList.get("actionId")+"','"+correspondenceList.get("protoCorrespTypeCode")+"')";
                                                        viewEnable = (String)correspondenceList.get("FinalFlag");
                                                        if(viewEnable != null && viewEnable.equals("Y")) {
                                                        %>
																		<tr>
																			<td width="50%" class="copy"><bean:write
																					name="correspondenceList"
																					property="CorrespondenceTypeDescription" /></td>
																			<td class="copy"><html:link href="<%=viewLink%>">
																					<bean:message key="protocolAction.label.View" />
																				</html:link></td>
																		</tr>
																		<%}%>
																	</logic:iterate>
																</table>
															</td>
														</tr>
														<%}%>
														<!-- Added for case#3072 - Documents Premium - Final flag is not sticking - end -->
														<%--Commented code for case # 3048 - Remove Reviewers List and Voting Details - End --%>
														<%-- Modified for COEUSDEV-352 : Can only view one attachment for Notify IRB in Protocol History - Start
                                            -- To display multiple documents for Notify IRB(116), Request to Close(105) , Request for Suspension(106) 
                                            -- Request to Close Enrolment(108), Request for Data Analysis Only ()
                                            -- Request to reopen Enrollment

                                            <!-- Added for case#3046 - Notify IRB attachments - start -->
                                            <%if(protocolActionCode.equals("116") || protocolActionCode.equals("303")){%>                                            
                                            <tr class="theader">
                                                <td>
                                                    <bean:message key="protocolAction.label.Attachment"/>
                                                </td>
                                            </tr>
                                            <%                                            
                                            String fileName = submissionList.get("fileName").toString();
                                            %>                                                  
                                            <tr>
                                                <td>
                                                    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tabtable">                                                     
                                                        <tr>
                                                            <td class="copy">
                                                                <%if(!fileName.equals("")){
                                                                String protocolNumber = submissionList.get("protocolNumber").toString();                                            
                                                                String sequenceNumber = submissionList.get("sequenceNumber").toString();
                                                                String submissionNumber = submissionList.get("submissionNumber").toString();
                                                                String documentId = submissionList.get("documentId").toString();                                                
                                                                String docLink = "javaScript:viewDocument('" 
                                                                                                        +protocolNumber
                                                                                                        +"','"
                                                                                                        +sequenceNumber
                                                                                                        +"','"
                                                                                                        +submissionNumber
                                                                                                        +"','"
                                                                                                        +documentId
                                                                                                        +"','"
                                                                                                        +fileName
                                                                                                        +"')";
                                                                
                                                                %>
                                                                    <html:link href="<%=docLink%>" >
                                                                        <bean:write name="submissionList" property="fileName"/>
                                                                    </html:link> 
                                                                <%}else{%>
                                                                    <bean:message key="protocolAction.label.noAtt"/>                                                                   
                                                                <%}%>
                                                            </td>
                                                        </tr>

                                                    </table>
                                                </td>
                                            </tr>  
                                            <%}%>
                                            <!-- Added for case#3046 - Notify IRB attachments - end --> 
                                            Modified for COEUSDEV-352 : Can only view one attachment for Notify IRB in Protocol History - End--%>
														<%if(protocolActionCode.equals("116") || protocolActionCode.equals("105") ||
                                                    protocolActionCode.equals("106")  || protocolActionCode.equals("108") || 
                                                    protocolActionCode.equals("114")  || protocolActionCode.equals("115")){%>

														<tr class="theader">
															<td><bean:message
																	key="protocolAction.label.attachments" />:</td>
														</tr>
														<tr>
															<td>
																<%
                                                    int count = 0;
                                                    %>
																<table width="100%" cellpadding="2" cellspacing="3"
																	class="tabtable">
																	<%Vector vecActionDoc = (Vector)submissionList.get("actionDocuments");
                                                        if(vecActionDoc == null || (vecActionDoc != null && vecActionDoc.size() > 0) ){
                                                        %>
																	<tr class="theader">
																		<td width="50%"><bean:message
																				key="protocolAction.label.description" /></td>
																		<td width="50%"><bean:message
																				key="uploadDocLabel.FileName" /></td>
																	</tr>
																	<%}%>
																	<logic:iterate id="actionDoc"
																		property="actionDocuments" name="submissionList"
																		type="org.apache.struts.action.DynaActionForm">
																		<%
                                                            String protocolNumber = submissionList.get("protocolNumber").toString();
                                                            String sequenceNumber = submissionList.get("sequenceNumber").toString();
                                                            String submissionNumber = submissionList.get("submissionNumber").toString();
                                                            String documentId = actionDoc.get("documentId").toString();
                                                            String fileName = actionDoc.get("fileName").toString();
                                                            String docLink = "javaScript:viewDocument('"
                                                                    +protocolNumber
                                                                    +"','"
                                                                    +sequenceNumber
                                                                    +"','"
                                                                    +submissionNumber
                                                                    +"','"
                                                                    +documentId
                                                                    +"','"
                                                                    +fileName
                                                                    +"')";
                                                            %>
																		<% 
                                                            if (count%2 == 0) {
                                                                STRBGCOLOR = "#D6DCE5";
                                                            }else {
                                                                STRBGCOLOR="#DCE5F1";
                                                            }
                                                            %>
																		<tr bgcolor="<%=STRBGCOLOR%>" width="600px"
																			valign="top" onmouseover="className='TableItemOn'"
																			onmouseout="className='TableItemOff'">
																			<td class="copy" width="50%"
																				id="descriptionTd<%=count%>">
																				<%
                                                                    String showDescription = "javascript:showDialog('"+count+"description"+INDEX+"')";%>
																				<%
                                                                    String actionDocDescription = actionDoc.get("description").toString();
                                                                    if(actionDocDescription != null){
                                                                        actionDocDescription = actionDocDescription.trim();
                                                                        if(!"".equals(actionDocDescription) && actionDocDescription.length() > 50){
                                                                            String doc = actionDocDescription.substring(0,50);%>
																				<div id="<%=count%>description<%=INDEX%>"
																					class="dialog"
																					style="width: 405px; height: 150px; display: none;">
																					<div id="<%=count%>innerdescription<%=INDEX%>"
																						style="overflow: auto; width: 405px; height: 125px">
																						<table cellpadding='0' cellspacing='1' align=left
																							class='tabtable' width='400px'>
																							<tr class='theader'>
																								<td width='100%'><bean:message
																										key="label.ProtocolAction.DocDescription" /></td>
																							</tr>
																						</table>
																						<table border="0" id="descriptionTable"
																							cellpadding="2" cellspacing="0"
																							class="lineBorderWhiteBackgrnd" width="400"
																							height="100">
																							<tr>
																								<td><html:textarea
																										styleId="descriptionTextArea"
																										property="txtDesc" styleClass="copy"
																										readonly="true"
																										value="<%=actionDocDescription%>"
																										disabled="false" rows="5"
																										style="border-top-width:0px; border-right-width:0px; 
                                                                                                       border-bottom-width:0px; border-left-width:0px;width:395px" />
																								</td>
																							</tr>
																						</table>
																					</div>

																					<input type="button"
																						onclick="javascript:hm('description')"
																						value="Close">
																				</div> <%=doc%><html:link href="<%=showDescription%>">&nbsp;[...]</html:link>
																				<%}else{%> <%=actionDocDescription%> <%}
                                                                    }%>


																			</td>
																			<td class="copy" width="50%"><html:link
																					href="<%=docLink%>">
																					<%=fileName%>
																				</html:link></td>
																		</tr>
																		<% count++;%>
																	</logic:iterate>
																	<tr>
																		<td class="copy">
																			<%if(count < 1){%> <bean:message
																				key="protocolAction.label.noAtt" /> <%}%>
																		
																		<td>
																	</tr>

																</table>
															</td>
														</tr>
														<%}%>
														<!-- Added for case#3046 - Notify IRB attachments - end -->

														<% // Coeusdev-86: Questionnaire for submission - Start
                                            Vector vecAnsweredQnrs = (Vector)submissionList.get("vecAnsweredQuestionnaires");
                                            if(vecAnsweredQnrs != null && vecAnsweredQnrs.size() > 0 ){
                                            int qnrCount = 0;
                                            // %>
														<tr class="theader">
															<td>Answered Forms:</td>
														</tr>
														<tr>
															<td>
																<table width="100%" cellpadding="1" cellspacing="1"
																	class="tabtable">
																	<logic:iterate id="qnrAnsHeaderBean"
																		property="vecAnsweredQuestionnaires"
																		name="submissionList"
																		type="edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean">

																		<% 
                                                                if (qnrCount%2 == 0) {
                                                                STRBGCOLOR = "#D6DCE5";
                                                                }else {
                                                                STRBGCOLOR="#DCE5F1";
                                                                }
                                                                
                                                                String qnrLink = "/getSubmissionQuestionnaire.do?questionnaireId="+qnrAnsHeaderBean.getQuestionnaireId()+"&questionaireLabel="+qnrAnsHeaderBean.getLabel();
                                                                String qnrCompletionId = qnrAnsHeaderBean.getQuestionnaireCompletionId();
                                                                String strQnrNotAnswered = "This form " +qnrAnsHeaderBean.getLabel()+ " is not answered for this protocol";
                                                                %>

																		<tr bgcolor="<%=STRBGCOLOR%>" valign="top"
																			onmouseover="className='TableItemOn'"
																			onmouseout="className='TableItemOff'">

																			<td class="copy">
																				<%if(qnrCompletionId != null && !"".equals(qnrCompletionId)) {%>
																				<a
																				href="javaScript:viewAnsweredQuestionsForQnr('<%=qnrLink%>','<%=submNumber%>')"><bean:write
																						name="qnrAnsHeaderBean" property="label" /> </a> <%} else {%>
																				<a
																				href="javascript:alert('<%= strQnrNotAnswered %>');"><bean:write
																						name="qnrAnsHeaderBean" property="label" /> </a> <%}%>
																			</td>

																			<%
                                                                qnrCount++;
                                                                %>
																		</tr>
																	</logic:iterate>

																</table>
															</td>
														</tr>
														<%}//// Coeusdev-86: Questionnaire for submission - End %>

													</table>
													<div>&nbsp;</div>
													<%}else if(protocolList.get("SubmissionNumber").equals(new Integer(0)) && !isEntered){%>
													<bean:message key="protocolActions.NoDetails" />
													<%isEntered = true;%>
													<%}%>
												</logic:iterate>
											</logic:notEmpty>
											<logic:empty name="protocolSubmissionDetails">
												<bean:message key="protocolActions.NoDetails" />
											</logic:empty>
										</div>
									</td>
								</tr>


								<!--Added here View Submission Details-->
								<% INDEX++;%>
							</logic:iterate>
						</logic:present>
						<!-- commented during View Submssion -->
						<%-- 
                       if (INDEX%2 == 0) 
                          STRBGCOLOR = "#D6DCE5"; 
                       else 
                          STRBGCOLOR="#DCE5F1"; 

                          

                    %>
                                  <% INDEX++;--%>
						<!-- commented during View Submssion -->
					</table>
				</td>
			</tr>
		</table>
	</html:form>
</body>
</html:html>
