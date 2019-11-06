<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@page
	import="java.util.Vector,java.util.HashMap,edu.mit.coeuslite.utils.CoeusLiteConstants,edu.mit.coeus.search.bean.DisplayBean"%>
<jsp:useBean id="loggedinUser" scope="session" class="java.lang.String" />
<html:html>
<head>
<title>Pending PI Action</title>
<html:base />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<link href="/coeuslite/mit/utils/css/ipf.css" rel="stylesheet"
	type="text/css">
<script language="javascript" type="text/JavaScript"
	src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/sorttable.js"></script>
<script>
function showHide(val){
    if(val == 1){
            document.getElementById('showPendingPanel').style.display = "none";
            document.getElementById('hidePendingPanel').style.display = "block";
            document.getElementById('pendingPanel').style.display = "block";
    }else if(val == 2){
            document.getElementById('showPendingPanel').style.display = "block";
            document.getElementById('hidePendingPanel').style.display = "none";
            document.getElementById('pendingPanel').style.display = "none";
     }   
    if(val == 3){
            document.getElementById('showCompletedPanel').style.display = "none";
            document.getElementById('hideCompletedPanel').style.display = "block";
            document.getElementById('completedPanel').style.display = "block";
    }else if(val == 4){
            document.getElementById('showCompletedPanel').style.display = "block";
            document.getElementById('hideCompletedPanel').style.display = "none";
            document.getElementById('completedPanel').style.display = "none";
    }   
    if(val == 5){
            document.getElementById('showEXPPanel').style.display = "none";
            document.getElementById('hideEXPPanel').style.display = "block";
            document.getElementById('expPanel').style.display = "block";
    }     
    if(val == 6){
            document.getElementById('showReviewTypeDetermination').style.display = "none";
            document.getElementById('hideReviewTypeDetermination').style.display = "block";
            document.getElementById('reviewTypeDetermination').style.display = "block";
    }
    if(val == 7){
        document.getElementById('showReviewTypeDetermination').style.display = "block";
        document.getElementById('hideReviewTypeDetermination').style.display = "none";
        document.getElementById('reviewTypeDetermination').style.display = "none";
    }

}
        </script>
</head>
<%
    Vector vecCompletedColumns =(Vector)session.getAttribute("IACUC_REVIEWER_REVIEW_COMPLETED_PROTOCOL_SEARCH_COLUMNS");
    Vector vecPendingColumns =(Vector)session.getAttribute("IACUC_REVIEWER_REVIEW_INCOMPLETED_PROTOCOL_SEARCH_COLUMNS");
    Vector reviewDeterminationColumns =(Vector)session.getAttribute("IACUC_REVIEW_DETERMINATION_PROTOCOL_SEARCH_COLUMNS");
    String plus = request.getContextPath()+"/coeusliteimages/plus.gif";
    String minus = request.getContextPath()+"/coeusliteimages/minus.gif";
    %>
<body>

	<html:form action="/iacucList.do" method="post">

		<%--<table width="100%"  border="0" cellpadding="0" cellspacing="0" class="table">
   
        
      
    <tr>
        <td>--%>
		<table width="100%" align='center' border="0" cellpadding="0"
			cellspacing="0" class='tabtable'>

			<tr>
				<td colspan='2'>
					<table align='center'>
						<tr>
							<td class="copybold">These are the Protocol Submissions for
								which <%=loggedinUser%> is specified for review determination
							</td>
						</tr>
						<tr>
							<td colspan='4'>
								<table width="933" align=center border="0" cellpadding="0"
									cellspacing="0">
									<tr>
										<td class='copybold'>
											<div id='showReviewTypeDetermination' style='display: none;'
												class='tableheader'>
												<%String divlink1 = "javascript:showHide(6)";%>
												<html:link href="<%=divlink1%>">
													<%String imagePlus=request.getContextPath()+"/coeusliteimages/plus.gif";%>
													<html:img src="<%=plus%>" border="0" />
												</html:link>
												&nbsp; Review Type Determination
											</div>
											<div id='hideReviewTypeDetermination' class='tableheader'>
												<% divlink1 = "javascript:showHide(7)";%>
												<html:link href="<%=divlink1%>">
													<%String imageMinus=request.getContextPath()+"/coeusliteimages/minus.gif";%>
													<html:img src="<%=minus%>" border="0" />
												</html:link>
												&nbsp; Review Type Determination
											</div>
										</td>
									</tr>
									<tr class='tabtable'>
										<td></td>
									</tr>
									<tr>
										<td>
											<div id='reviewTypeDetermination'>
												<table width="933" align=center border="0" cellpadding="1"
													cellspacing="0" id="t1" class="sortable">
													<logic:present
														name="IACUC_REVIEW_DETERMINATION_PROTOCOL_SEARCH">
														<tr>
															<%if(reviewDeterminationColumns != null && reviewDeterminationColumns.size()>0){
                                                                for(int index=0;index<reviewDeterminationColumns.size();index++){
                                                                edu.mit.coeus.search.bean.DisplayBean displayBean = (edu.mit.coeus.search.bean.DisplayBean)reviewDeterminationColumns.elementAt(index);
                                                                if(displayBean.isVisible()){
                                                                String strColumnName = displayBean.getValue();
                                                                %>
															<td class="theader"><%=strColumnName%></td>
															<%}}}%>
														</tr>
														<%String strBgColor = "#DCE5F1";
                                                            String protocolNumber="";
                                                            String submissionNumber = "";  
                                                            int count = 0;%>
														<logic:iterate id="reviewDetermination"
															name="IACUC_REVIEW_DETERMINATION_PROTOCOL_SEARCH"
															type="java.util.HashMap" scope="session">
															<%if (count%2 == 0) 
                                                                strBgColor = "#D6DCE5"; 
                                                                else 
                                                                strBgColor="#DCE5F1";%>
															<tr bgcolor="<%=strBgColor%>" valign="top"
																onmouseover="className='TableItemOn'"
																onmouseout="className='TableItemOff'">
																<%String destPage = CoeusLiteConstants.REVIEW_COMMENTS;
                                                                    if(reviewDeterminationColumns != null && reviewDeterminationColumns.size()>0){
                                                                    String sequenceNumber = reviewDetermination.get("SEQUENCE_NUMBER").toString().trim();
                                                                    submissionNumber = reviewDetermination.get("SUBMISSION_NUMBER").toString().trim();
                                                                    for(int index=0;index<reviewDeterminationColumns.size();index++){
                                                                    edu.mit.coeus.search.bean.DisplayBean displayBean = 
                                                                    (edu.mit.coeus.search.bean.DisplayBean)reviewDeterminationColumns.elementAt(index);
                                                                    if(!displayBean.isVisible())
                                                                    continue;
                                                                    String key = displayBean.getName();
                                                                    if(key != null){
                                                                    String value = reviewDetermination.get(key) == null ? "" :
                                                                    reviewDetermination.get(key).toString();
                                                                    if(index == 0){
                                                                    protocolNumber=value;%>

																<%} else if(index==1){
                                                                    if(value != null && value.length() > 20){
                                                                    value = value.substring(0,20);
                                                                    value = value+" ...";
                                                                    }
                                                                    }else if(index == 2){
                                                                    if(value != null && value.length() > 20){
                                                                    value = value.substring(0,20);
                                                                    value = value+" ...";
                                                                    }
                                                                    }else if(index == 5){
                                                                    if(value != null && value.length() > 38){
                                                                    value = value.substring(0,38);
                                                                    value = value+" ...";
                                                                    }
                                                                    }
                                                                    %>

																<td align="left" nowrap class="copy"><a
																	href="<%=request.getContextPath()%>/getIacucData.do?SEARCH_ACTION=SEARCH_ACTION&protocolNumber=<%=protocolNumber.trim()%>&PAGE=<%=destPage.trim()%>&sequenceNumber=<%=sequenceNumber%>&submissionNumber=<%=submissionNumber%>&fromIsReviewer=true">
																		<u> <%=value%>
																	</u>
																</a></td>

																<%
                                                                    }
                                                                    }
                                                                    }
                                                                    %>
															</tr>
															<% count++;%>
														</logic:iterate>
													</logic:present>
													<logic:notPresent
														name="IACUC_REVIEW_DETERMINATION_PROTOCOL_SEARCH">

														<table width="100%" height="20" border="0" cellpadding="0"
															cellspacing="0" class="copy">
															<tr>
																<td><bean:message bundle="iacuc"
																		key="protocolMyReview.msg.noReviewDetermination" /></td>
															</tr>
														</table>
													</logic:notPresent>

													</div>
													</td>
													</tr>
												</table>
										</td>
									</tr>

									<tr>
										<td class="copybold">These are the Protocol Submissions
											for which <%=loggedinUser%> is specified as a Reviewer
										</td>
									</tr>


									<tr>
										<td colspan='4'>
											<table width="100%" align=center border="0" cellpadding="0"
												cellspacing="0">
												<tr>
													<td class='copybold'>
														<div id='showPendingPanel' style='display: none;'
															class='tableheader'>
															<%String divlink = "javascript:showHide(1)";%>
															<html:link href="<%=divlink%>">
																<html:img src="<%=plus%>" border="0" />
															</html:link>
															&nbsp;
															<bean:message key="protocolMyReview.header.reviewPending" />
														</div>
														<div id='hidePendingPanel' class='tableheader'>
															<% divlink = "javascript:showHide(2)";%>
															<html:link href="<%=divlink%>">
																<html:img src="<%=minus%>" border="0" />
															</html:link>
															&nbsp;
															<bean:message key="protocolMyReview.header.reviewPending" />
														</div>
													</td>
												</tr>
												<tr class='tabtable'>
													<td></td>
												</tr>
												<tr>
													<td>
														<div id='pendingPanel'>
															<table width="933" align=center border="0"
																cellpadding="1" cellspacing="0" id="t1" class="sortable">
																<logic:present
																	name="IACUC_REVIEWER_REVIEW_INCOMPLETED_PROTOCOL_SEARCH">
																	<tr>
																		<%if(vecPendingColumns != null && vecPendingColumns.size()>0){
                                                                for(int index=0;index<vecPendingColumns.size();index++){
                                                                edu.mit.coeus.search.bean.DisplayBean displayBean = (edu.mit.coeus.search.bean.DisplayBean)vecPendingColumns.elementAt(index);
                                                                if(displayBean.isVisible()){
                                                                String strColumnName = displayBean.getValue();
                                                                %>
																		<td class="theader"><%=strColumnName%></td>
																		<%}}}%>
																	</tr>



																	<%String strBgColor = "#DCE5F1";
                                                            String protocolNumber="";
                                                            String submissionNumber = "";
                                                            int reviewerType = 0;
                                                            int count = 0;%>
																	<logic:iterate id="pendingReviewProtocol"
																		name="IACUC_REVIEWER_REVIEW_INCOMPLETED_PROTOCOL_SEARCH"
																		type="java.util.HashMap" scope="session">
																		<%if (count%2 == 0) 
                                                                strBgColor = "#D6DCE5"; 
                                                                else 
                                                                strBgColor="#DCE5F1";%>
																		<tr bgcolor="<%=strBgColor%>" valign="top"
																			onmouseover="className='TableItemOn'"
																			onmouseout="className='TableItemOff'">
																			<%String destPage = CoeusLiteConstants.REVIEW_COMMENTS;
                                                                    if(vecPendingColumns != null && vecPendingColumns.size()>0){
                                                                    String sequenceNumber = pendingReviewProtocol.get("SEQUENCE_NUMBER").toString().trim();
                                                                    submissionNumber = pendingReviewProtocol.get("SUBMISSION_NUMBER").toString().trim();
                                                                    reviewerType = Integer.parseInt(pendingReviewProtocol.get("REVIEWER_TYPE_CODE").toString());
                                                                    pendingReviewProtocol.get("SUBMISSION_NUMBER").toString().trim();
                                                                    for(int index=0;index<vecPendingColumns.size();index++){
                                                                    edu.mit.coeus.search.bean.DisplayBean displayBean = 
                                                                    (edu.mit.coeus.search.bean.DisplayBean)vecPendingColumns.elementAt(index);
                                                                    if(!displayBean.isVisible())
                                                                    continue;
                                                                    String key = displayBean.getName();
                                                                    if(key != null){
                                                                    String value = pendingReviewProtocol.get(key) == null ? "" :
                                                                    pendingReviewProtocol.get(key).toString();
                                                                    if(index == 0){
                                                                    protocolNumber=value;%>

																			<%} else if(index==1){
                                                                    if(value != null && value.length() > 20){
                                                                    value = value.substring(0,20);
                                                                    value = value+" ...";
                                                                    }
                                                                    }else if(index == 2){
                                                                    if(value != null && value.length() > 20){
                                                                    value = value.substring(0,20);
                                                                    value = value+" ...";
                                                                    }
                                                                    }else if(index == 5){
                                                                    if(value != null && value.length() > 38){
                                                                    value = value.substring(0,38);
                                                                    value = value+" ...";
                                                                    }
                                                                    }
                                                                    %>
																			<%if(index == 6){
                                                                    if(reviewerType == 1){%>

																			<td width="3%" align="center" nowrap class="copy">
																				<img
																				src="<bean:write name='ctxtPath'/>/coeusliteimages/checked.gif">
																			</td>
																			<%}else{%>
																			<td align="center" nowrap class="copy"></td>
																			<%}%>
																			<%}else{%>
																			<td align="left" nowrap class="copy"><a
																				href="<%=request.getContextPath()%>/getIacucData.do?SEARCH_ACTION=SEARCH_ACTION&protocolNumber=<%=protocolNumber.trim()%>&PAGE=<%=destPage.trim()%>&sequenceNumber=<%=sequenceNumber%>&submissionNumber=<%=submissionNumber%>&fromIsReviewer=true">
																					<u> <%=value%>
																				</u>
																			</a></td>
																			<%}%>
																			<%
                                                                    }
                                                                    }
                                                                    }
                                                                    %>
																		</tr>
																		<% count++;%>
																	</logic:iterate>
																</logic:present>

															</table>
															<logic:notPresent
																name="IACUC_REVIEWER_REVIEW_INCOMPLETED_PROTOCOL_SEARCH">

																<table width="100%" height="20" border="0"
																	cellpadding="0" cellspacing="0" class="copy">
																	<tr>
																		<td><bean:message
																				key="protocolMyReview.msg.noReviewPending" /></td>
																	</tr>
																</table>
															</logic:notPresent>
														</div>
													</td>
												</tr>

												</td>
												</tr>

												<tr>
													<td colspan='4'>
														<table width="933" align=center border="0" cellpadding="0"
															cellspacing="0">
															<tr>
																<td class='copybold'>
																	<div id='showCompletedPanel' style='display: none;'
																		class='tableheader'>
																		<%divlink = "javascript:showHide(3)";%>
																		<html:link href="<%=divlink%>">
																			<%String imagePlus=request.getContextPath()+"/coeusliteimages/plus.gif";%>
																			<html:img src="<%=plus%>" border="0" />
																		</html:link>
																		&nbsp;
																		<bean:message
																			key="protocolMyReview.header.reviewComplete" />
																	</div>
																	<div id='hideCompletedPanel' class='tableheader'>
																		<% divlink = "javascript:showHide(4)";%>
																		<html:link href="<%=divlink%>">
																			<%String imageMinus=request.getContextPath()+"/coeusliteimages/minus.gif";%>
																			<html:img src="<%=minus%>" border="0" />
																		</html:link>
																		&nbsp;
																		<bean:message
																			key="protocolMyReview.header.reviewComplete" />
																	</div>
																</td>
															</tr>
															<tr class='tabtable'>
																<td></td>
															</tr>
															<tr>
																<td>
																	<div id='completedPanel'>
																		<table width="933" align=center border="0"
																			cellpadding="1" cellspacing="0" id="t1"
																			class="sortable">
																			<logic:present
																				name="IACUC_REVIEWER_REVIEW_COMPLETED_PROTOCOL_SEARCH">
																				<tr>
																					<%if(vecCompletedColumns != null && vecCompletedColumns.size()>0){
                                                                for(int index=0;index<vecCompletedColumns.size();index++){
                                                                edu.mit.coeus.search.bean.DisplayBean displayBean = (edu.mit.coeus.search.bean.DisplayBean)vecCompletedColumns.elementAt(index);
                                                                if(displayBean.isVisible()){
                                                                String strColumnName = displayBean.getValue();
                                                                %>
																					<td class="theader"><%=strColumnName%></td>
																					<%}}}%>
																				</tr>
																				<%String strBgColor = "#DCE5F1";
                                                            String protocolNumber="";
                                                            String submissionNumber = "";  
                                                            int reviewerType = 0;
                                                            int count = 0;%>
																				<logic:iterate id="completedReviewProtocol"
																					name="IACUC_REVIEWER_REVIEW_COMPLETED_PROTOCOL_SEARCH"
																					type="java.util.HashMap" scope="session">
																					<%if (count%2 == 0) 
                                                                strBgColor = "#D6DCE5"; 
                                                                else 
                                                                strBgColor="#DCE5F1";%>
																					<tr bgcolor="<%=strBgColor%>" valign="top"
																						onmouseover="className='TableItemOn'"
																						onmouseout="className='TableItemOff'">
																						<%String destPage = CoeusLiteConstants.REVIEW_COMMENTS;
                                                                    if(vecCompletedColumns != null && vecCompletedColumns.size()>0){
                                                                    String sequenceNumber = completedReviewProtocol.get("SEQUENCE_NUMBER").toString().trim();
                                                                    submissionNumber = completedReviewProtocol.get("SUBMISSION_NUMBER").toString().trim();
                                                                    reviewerType = Integer.parseInt(completedReviewProtocol.get("REVIEWER_TYPE_CODE").toString());
                                                                    for(int index=0;index<vecCompletedColumns.size();index++){
                                                                    edu.mit.coeus.search.bean.DisplayBean displayBean = 
                                                                    (edu.mit.coeus.search.bean.DisplayBean)vecCompletedColumns.elementAt(index);
                                                                    if(!displayBean.isVisible())
                                                                    continue;
                                                                    String key = displayBean.getName();
                                                                    if(key != null){
                                                                    String value = completedReviewProtocol.get(key) == null ? "" :
                                                                    completedReviewProtocol.get(key).toString();
                                                                    if(index == 0){
                                                                    protocolNumber=value;%>

																						<%} else if(index==1){
                                                                    if(value != null && value.length() > 20){
                                                                    value = value.substring(0,20);
                                                                    value = value+" ...";
                                                                    }
                                                                    }else if(index == 2){
                                                                    if(value != null && value.length() > 20){
                                                                    value = value.substring(0,20);
                                                                    value = value+" ...";
                                                                    }
                                                                    }else if(index == 5){
                                                                    if(value != null && value.length() > 38){
                                                                    value = value.substring(0,38);
                                                                    value = value+" ...";
                                                                    }
                                                                    }
                                                                    %>
																						<%if(index == 6){
                                                                    if(reviewerType == 1){%>
																						<td width="3%" align="center" nowrap class="copy">
																							<img
																							src="<bean:write name='ctxtPath'/>/coeusliteimages/checked.gif">
																						</td>
																						<%}else{%>
																						<td align="center" nowrap class="copy"></td>
																						<%}%>
																						<%}else{%>
																						<td align="left" nowrap class="copy"><a
																							href="<%=request.getContextPath()%>/getIacucData.do?SEARCH_ACTION=SEARCH_ACTION&protocolNumber=<%=protocolNumber.trim()%>&PAGE=<%=destPage.trim()%>&sequenceNumber=<%=sequenceNumber%>&submissionNumber=<%=submissionNumber%>&fromIsReviewer=true">
																								<u> <%=value%>
																							</u>
																						</a></td>
																						<%}%>



																						<%
                                                                    }
                                                                    }
                                                                    }
                                                                    %>
																					</tr>
																					<% count++;%>
																				</logic:iterate>
																			</logic:present>
																			<logic:notPresent
																				name="IACUC_REVIEWER_REVIEW_COMPLETED_PROTOCOL_SEARCH">

																				<table width="100%" height="20" border="0"
																					cellpadding="0" cellspacing="0" class="copy">
																					<tr>
																						<td><bean:message
																								key="protocolMyReview.msg.noReviewCompleted" />
																						</td>
																					</tr>
																				</table>
																			</logic:notPresent>

																			</div>
																			</td>
																			</tr>
																		</table>
																</td>
															</tr>


														</table>
													</td>
												</tr>
											</table> <%--  </td>
    </tr>
</table>--%> </html:form>
</body>
</html:html>
