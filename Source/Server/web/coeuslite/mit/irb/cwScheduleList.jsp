<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@page
	import="java.util.HashMap, 
java.util.ArrayList, 
java.util.Hashtable, 
java.util.Iterator, 
java.util.Vector, 
java.util.HashMap, 
edu.mit.coeuslite.utils.CoeusLiteConstants"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="protocolList" scope="session" class="java.util.Vector" />
<jsp:useBean id="protocolColumnNames" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="loggedinUser" scope="session" class="java.lang.String" />
<%
    HashMap  hmSubmittedProtocols = (HashMap)  request.getAttribute("schSubmittedProtocols");    
    Vector protoList = (Vector) session.getAttribute("protocolList");
    boolean hasErrorMessage = false;
%>

<html:html>
<head>
<title>Coeus Web</title>
<html:base />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<link href="/coeuslite/mit/utils/css/ipf.css" rel="stylesheet"
	type="text/css">
<script language="javascript" type="text/JavaScript"
	src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/sorttable.js"></script>

</head>
<body>
	<script>
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
            
           <%-- Added for COEUSQA:3333 - IRB and IACUC - Ability to Add Attachments to Minutes and Agenda - Start --%>            
           function showScheduleAttachments(schID, PROTOCOL_TYPE)
          {
            var winleft = (screen.width - 800) / 2;
            var winUp = (screen.height - 450) / 2; 
            var win = "scrollbars=1,resizable=1,width=800,height=500,left="+winleft+",top="+winUp;    
            sList = window.open("<%=request.getContextPath()%>/protocolList.do?scheduleId="+schID+"&PROTOCOL_TYPE="+PROTOCOL_TYPE, "list", win);                        
          }                    
                  
          <%-- Added for COEUSQA:3333 - End --%>
            </script>
	<html:form action="/protocolList.do" method="post">
		<table width="100%" height="100%" border="0" cellpadding="0"
			cellspacing="0" class="table">
			<tr>
				<td class="copy"><html:messages id="message" message="true"
						property="noViewRight">
						<font color='red'><li><bean:write name="message" /></li></font>
						<%hasErrorMessage = true;%>

					</html:messages></td>
			</tr>
			<tr>
				<td height="653" align="left" valign="top"><table width="99%"
						border="0" align="center" cellpadding="2" cellspacing="0"
						class="tabtable">
						<tr>
							<td colspan="4" align="left" valign="top"><table
									width="100%" height="20" border="0" cellpadding="0"
									cellspacing="0" class="tableheader">
									<tr>
										<td><bean:message key="scheduleList.title" /></td>
									</tr>
								</table></td>
						</tr>

						<%if(protoList == null || protoList.isEmpty()){%>
						<tr>
							<td class="copy" align="left">&nbsp;&nbsp;&nbsp;<bean:message
									key="scheduleList.noSchedulesAvailable" />
							</td>
						</tr>
						<%} else {%>
						<tr align="center">
							<td>
								<table width="98%" height="100%" border="0" cellpadding="2"
									cellspacing="0" id="t1">
									<tr>
										<td class="theader"></td>
										<%
                                            if(protocolColumnNames != null && protocolColumnNames.size()>0){
                                                for(int index=0;index<protocolColumnNames.size();index++){
                                                    edu.mit.coeus.search.bean.DisplayBean displayBean = (edu.mit.coeus.search.bean.DisplayBean)protocolColumnNames.elementAt(index);
                                                    if(displayBean.isVisible()){
                                                        String strColumnName = displayBean.getValue();
                                            %>
										<td class="theader"><%=strColumnName%></td>

										<%
                                            }
                                            }%>
										<td class="theader"></td>
										<td class="theader"></td>
										<%}
                                            %>

									</tr>
									<%  
                                        String strBgColor = "#DCE5F1";
                                        String protocolNumber="";
                                        int count = 0;
                                        %>
									<logic:present name="protocolList">
										<logic:iterate id="protocol" name="protocolList"
											type="java.util.HashMap">
											<%
                                                if (count%2 == 0)
                                                    strBgColor = "#D6DCE5";
                                                else
                                                    strBgColor="#DCE5F1"; 
                                                %>
											<tr bgcolor="<%=strBgColor%>" valign="top"
												onmouseover="className='TableItemOn'"
												onmouseout="className='TableItemOff'">
												<%
                                                    String destPage = CoeusLiteConstants.REVIEW_COMMENTS;                                   
                                                    String sequenceNumber = "";
                                                    String submissionNumber = "";
                                                    String scheduleId ="";
                                                    String generatedAgenda = "";
                                                    int generatedAgendaNumber = 0;
                                                    if(protocolColumnNames != null && protocolColumnNames.size()>0){

                                                        for(int index=0;index<protocolColumnNames.size();index++){
                                                            edu.mit.coeus.search.bean.DisplayBean displayBean = (edu.mit.coeus.search.bean.DisplayBean)protocolColumnNames.elementAt(index);
                                                           String key = displayBean.getName();
                                                            if(!displayBean.isVisible()){
                                                                if(index == 0){
                                                                    scheduleId = protocol.get(key) == null ? "" : protocol.get(key).toString();
                                                                }
                                                                if(index == 5) {
                                                                    generatedAgenda = protocol.get(key) == null ? "" : protocol.get(key).toString();
                                                                    generatedAgendaNumber = Integer.parseInt(generatedAgenda);                                                                
                                                                }
                                                                continue;
                                                            }
                                                            if(key != null){
                                                                String value = protocol.get(key) == null ? "" : protocol.get(key).toString();
                                                               
                                                    %>
												<%  if(index == 0){
                                                                    protocolNumber=value;
                                                                                                                       
                                                    %>

												<%
                                                    } else if(index==2 || index== 4){
                                                                    if(value != null && value.length() > 50){
                                                                        value = value.substring(0,47);
                                                                        value = value+" ...";
                                                                    }
                                                    }                                                               
                                                     
                                                    %>




												<% if(index == 1){ %>
												<td width="1%">
													<%String divName="Panel"+count;%>
													<div id='<%=divName%>'>

														<%String divlink = "javascript:showHide(1,'"+count+"')";%>
														<html:link href="<%=divlink%>">
															<%String imagePlus=request.getContextPath()+"/coeusliteimages/plus.gif";%>
															<html:img src="<%=imagePlus%>" border="0" />
														</html:link>
													</div> <% String divsnName="hidePanel"+count;%>
													<div id='<%=divsnName%>' style='display: none;'>

														<% String divsnlink = "javascript:showHide(2,'"+count+"')";%>
														<html:link href="<%=divsnlink%>">
															<%String imageMinus=request.getContextPath()+"/coeusliteimages/minus.gif";%>
															<html:img src="<%=imageMinus%>" border="0" />
														</html:link>
													</div>

												</td>
												<%}%>
												<td align="left" nowrap class="copy"><a
													href="<%=request.getContextPath()%>/protocolList.do?PROTOCOL_TYPE=REVIEWER_SCHEDULE_AGENDA_SEARCH&#38;Menu_Id=002&scheduleId=<%=scheduleId%>&fromIsReviewer=true&isAgenda=false&openAgenda=false"><u>
															<%=value%>
													</u> </a></td>
												<%
                                                    }
                                                    }%>
												<td align="left" nowrap class="copy">
													<%if(generatedAgendaNumber > 0) { %> <a
													href="<%=request.getContextPath()%>/protocolList.do?&scheduleId=<%=scheduleId%>&acType=V&PROTOCOL_TYPE=REVIEWER_SCHEDULE_SEARCH&#38;Menu_Id=002&SUBHEADER_ID=SH008&isAgenda=true&openAgenda=true"
													, target="_blank"><u> View Agenda</u> </a> <%}else{%> <a
													href="<%=request.getContextPath()%>/protocolList.do?&scheduleId=<%=scheduleId%>&acType=V&PROTOCOL_TYPE=REVIEWER_SCHEDULE_SEARCH&#38;Menu_Id=002&SUBHEADER_ID=SH008&isAgenda=true&openAgenda=true"><u>
															View Agenda</u> </a> <%}%>
												</td>
												<!-- COEUSQA:3333 - IRB and IACUC - Ability to Add Attachments to Minutes and Agenda - Start -->
												<td align="left" nowrap class="copy"><a
													href="javaScript:showScheduleAttachments(<%=scheduleId%>, 'SCHEDULE_ATTACHMENTS');"
													class="menu"> <u> Attachments</u>
												</a></td>
												<!-- COEUSQA:3333 - End -->
												<%}
                                                    %>
											</tr>

											<tr>
												<td colspan="7" align="right">
													<%String divisionName="pan"+count;%>
													<div id='<%=divisionName%>' style='display: none;'>
														<% Vector vecSubmittedProtocols = (Vector)  hmSubmittedProtocols.get(scheduleId);
                                                                if(vecSubmittedProtocols != null && vecSubmittedProtocols.size() >0){%>
														<table width="99%" height="100%" border="0"
															cellpadding="0" cellspacing="0" class="table">


															<tr>
																<td height="100%" align="left" valign="top"><table
																		width="100%" border="0" align="center" cellpadding="0"
																		cellspacing="0" class="tabtable">

																		<tr>
																			<td colspan="7" align="left" valign="top">
																				<table width="100%" height="20" border="0"
																					cellpadding="0" cellspacing="0" class="tableheader">
																					<tr>
																						<td>Protocols included in this meeting where
																							<%=loggedinUser%> is the reviewer
																						</td>
																					</tr>
																				</table>
																			</td>
																		</tr>

																		<tr align="center">
																			<td>
																				<table width="100%" height="100%" border="0"
																					cellpadding="2" cellspacing="0" id="t1"
																					class="sortable" align="right">

																					<% 
                                                                                        if(vecSubmittedProtocols != null && vecSubmittedProtocols.size() >0){%>
																					<tr>
																						<td class="theader" width="17%"><bean:message
																								key="scheduleList.protocolNumber" /></td>
																						<td class="theader" width="30%"><bean:message
																								key="scheduleList.protocolTitle" /></td>
																						<td class="theader" width="25%"><bean:message
																								key="scheduleList.pi" /></td>
																						<td class="theader" width="15%"><bean:message
																								key="scheduleList.submissionStatus" /></td>
																						<!--Added for COEUSQA-2288 : Ability for Reviewer to mark Review Comments complete and make recommendation - Start -->
																						<td class="theader" width="15%"><bean:message
																								key="schedule.primaryReviewer" /></td>
																						<!--Added for COEUSQA-2288 : Ability for Reviewer to mark Review Comments complete and make recommendation - End -->

																						<%
                                                                                      
                                                                                            
                                                                                            %>

																					</tr>
																					<%  
                                                                                        String strInnerBgColor = "#DCE5F1";
                                                                                        String submittedprotocol="";
                                                                                        int counter = 0;
                                                                                        
                                                                                              
                                                                                        
                                                                                                for(int ind = 0; ind < vecSubmittedProtocols.size(); ind++){
                                                                                                
                                                                                                HashMap hmSubmittedprotocol = (HashMap) vecSubmittedProtocols.elementAt(ind);
                                                                                                %>

																					<%
                                                                                                if (counter%2 == 0)
                                                                                                    strInnerBgColor = "#D6DCE5";
                                                                                                else
                                                                                                    strInnerBgColor="#DCE5F1"; 
                                                                                                %>
																					<tr bgcolor="<%=strInnerBgColor%>" valign="top"
																						onmouseover="className='TableItemOn'"
																						onmouseout="className='TableItemOff'">
																						<%
                                                                                                    String innerDestPage = CoeusLiteConstants.REVIEW_COMMENTS;
                                                                                                    String protoSequenceNumber = "";
                                                                                                    String protoSubmissionNumber = "";
                                                                                                    
                                                                                                    if(protocolColumnNames != null && protocolColumnNames.size()>0){
                                                                                                        
                                                                                                            protoSequenceNumber = hmSubmittedprotocol.get("SEQUENCE_NUMBER").toString().trim();
                                                                                                            protoSubmissionNumber = hmSubmittedprotocol.get("SUBMISSION_NUMBER").toString().trim();
                                                                                                      
                                                                                                            String value = "";
                                                                                                             String key = "";
                                                                                                             String title = "";
                                                                                                             
                                                                                                             String pi = "";
                                                                                                             
                                                                                                             String submissionStatus = "";
                                                                                                             
                                                                                                            submittedprotocol = (String) hmSubmittedprotocol.get("PROTOCOL_NUMBER");
                                                                                                            title = (String) hmSubmittedprotocol.get("TITLE");
                                                                                                           if(title != null && title.length() > 50){
                                                                                                               title = title.substring(0, 50)+ "...";
                                                                                                           }
                                                                                                             
                                                                                                            submissionStatus = (String) hmSubmittedprotocol.get("DESCRIPTION");
                                                                                                            pi = (String) hmSubmittedprotocol.get("PERSON_NAME");
                                                                                                            
                                                                                                            if(pi != null && pi.length() > 34){
                                                                                                                pi = pi.substring(0, 31)+ "...";
                                                                                                            }
                                                                                                            %>

																						<td align="left" nowrap class="copy"><a
																							href="<%=request.getContextPath()%>/getProtocolData.do?SEARCH_ACTION=SEARCH_ACTION&protocolNumber=<%=submittedprotocol%>&PAGE=<%=innerDestPage.trim()%>&sequenceNumber=<%=protoSequenceNumber%>&submissionNumber=<%=protoSubmissionNumber%>&fromIsReviewer=true"><u>
																									<%=submittedprotocol%>
																							</u> </a></td>
																						<td align="left" nowrap class="copy"><a
																							href="<%=request.getContextPath()%>/getProtocolData.do?SEARCH_ACTION=SEARCH_ACTION&protocolNumber=<%=submittedprotocol%>&PAGE=<%=innerDestPage.trim()%>&sequenceNumber=<%=protoSequenceNumber%>&submissionNumber=<%=protoSubmissionNumber%>&fromIsReviewer=true"><u>
																									<%=title%>
																							</u> </a></td>
																						<td align="left" nowrap class="copy"><a
																							href="<%=request.getContextPath()%>/getProtocolData.do?SEARCH_ACTION=SEARCH_ACTION&protocolNumber=<%=submittedprotocol%>&PAGE=<%=innerDestPage.trim()%>&sequenceNumber=<%=protoSequenceNumber%>&submissionNumber=<%=protoSubmissionNumber%>&fromIsReviewer=true"><u>
																									<%=pi%>
																							</u> </a></td>
																						<td align="left" nowrap class="copy"><a
																							href="<%=request.getContextPath()%>/getProtocolData.do?SEARCH_ACTION=SEARCH_ACTION&protocolNumber=<%=submittedprotocol%>&PAGE=<%=innerDestPage.trim()%>&sequenceNumber=<%=protoSequenceNumber%>&submissionNumber=<%=protoSubmissionNumber%>&fromIsReviewer=true"><u>
																									<%=submissionStatus%>
																							</u> </a></td>
																						<!--Added for COEUSQA-2288 : Ability for Reviewer to mark Review Comments complete and make recommendation - Start -->
																						<td align="center" nowrap class="copy">
																							<%int reviewerTypeCode = Integer.parseInt(hmSubmittedprotocol.get("REVIEWER_TYPE_CODE").toString());
                                                                                                        if(reviewerTypeCode == 1){
                                                                                                        %>
																							<img
																							src="<bean:write name='ctxtPath'/>/coeusliteimages/checked.gif">
																							<%}%>
																						</td>
																						<!--Added for COEUSQA-2288 : Ability for Reviewer to mark Review Comments complete and make recommendation - End -->

																						<%
                                                                                                    }
                                                                                                    %>
																					</tr>
																					<% counter++;%>
																					<%
                                                                                          }
                                                                                          } 
                                                                                          %>

																				</table>
																			</td>
																		</tr>



																	</table></td>
															</tr>
														</table>
														<%}else{%>
														<table width="100%" height="20" border="0" cellpadding="0"
															cellspacing="0">
															<tr>
																<td class="copy">&nbsp;&nbsp;<bean:message
																		key="schedule.noProtocol" />
																</td>
															</tr>


														</table>
														<%}%>


													</div>
												</td>
											</tr>

											<% count++;%>
										</logic:iterate>
									</logic:present>

								</table>
							</td>
						</tr>
						<%}%>


					</table></td>
			</tr>
			<tr>
				<td height='10'>&nbsp;</td>
			</tr>
		</table>
		<script>
                   <%if(!hasErrorMessage){%>
                    var msg = '<bean:message key="schedule_no_agenda_details"/>';
                        var agendaNumPresent = "<%=(String)session.getAttribute("AGENDA_NUMBER"+session.getId())%>";
                    if(agendaNumPresent=="0"){
                        alert(msg);
                    }
                    <%}%>
                </script>
	</html:form>
</body>
</html:html>
