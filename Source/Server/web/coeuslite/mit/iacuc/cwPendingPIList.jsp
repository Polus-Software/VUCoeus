<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@page
	import="java.util.Vector,java.util.HashMap,edu.mit.coeuslite.utils.CoeusLiteConstants,edu.mit.coeus.search.bean.DisplayBean"%>
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
            document.getElementById('showSMRPanel').style.display = "none";
            document.getElementById('hideSMRPanel').style.display = "block";
            document.getElementById('smrPanel').style.display = "block";
        }
else if(val == 2){
            document.getElementById('showSMRPanel').style.display = "block";
            document.getElementById('hideSMRPanel').style.display = "none";
            document.getElementById('smrPanel').style.display = "none";
        }   
if(val == 3){
            document.getElementById('showSRPanel').style.display = "none";
            document.getElementById('hideSRPanel').style.display = "block";
            document.getElementById('srPanel').style.display = "block";
        }
else if(val == 4){
            document.getElementById('showSRPanel').style.display = "block";
            document.getElementById('hideSRPanel').style.display = "none";
            document.getElementById('srPanel').style.display = "none";
        }   
if(val == 5){
            document.getElementById('showEXPPanel').style.display = "none";
            document.getElementById('hideEXPPanel').style.display = "block";
            document.getElementById('expPanel').style.display = "block";
        }
else if(val == 6){
            document.getElementById('showEXPPanel').style.display = "block";
            document.getElementById('hideEXPPanel').style.display = "none";
            document.getElementById('expPanel').style.display = "none";
        }           

}
</script>
</head>
<%
       //Modified for Case#3021 - Lite: Pending PI Action/List of expiring protocols -Start
       //Vector vecColData = (Vector)session.getAttribute("protocolColumnNamesList");
       Vector vecColData = (Vector)session.getAttribute("protocolColumnNames");
       //Modified for Case#3021 - Lite: Pending PI Action/List of expiring protocols -End
       if(vecColData == null ){
           vecColData = new Vector();
           }
       Vector vecColumnData = new Vector();
       if(vecColData !=null && vecColData.size() > 0){
        vecColumnData = (Vector)vecColData.get(0);
       }
       String plus = request.getContextPath()+"/coeusliteimages/plus.gif";
       String minus = request.getContextPath()+"/coeusliteimages/minus.gif";
%>
<body>
	<logic:messagesPresent message="true">

		<html:messages id="rights" message="true"
			property="noDisplayAndEditRights" bundle="protocol">
			<script>
                    var message = '<bean:write name = "rights"/>';                  
                </script>
		</html:messages>
		<script>
                    var displayMode = "<%=session.getAttribute("mode"+session.getId())%>";    
                    if(displayMode == "noRights"){
                        alert(message);
                    }
                </script>

	</logic:messagesPresent>
	<html:form action="/pendingPIList.do" method="post">

		<table width="970" border="0" cellpadding="0" cellspacing="0"
			class="table">
			<tr class='theader'>
				<td colspan='2' class='tableheader' height='20'>List of Pending
					PI Action Protocols</td>
			</tr>
			<tr>
				<td>
					<table width='953' align=center border="0" cellpadding="0"
						cellspacing="0" class='tabtable'>
						<tr>
							<td colspan='2'>
								<table align=center>
									<tr>
										<td colspan='4'>
											<table width="933" align=center border="0" cellpadding="0"
												cellspacing="0">
												<tr>
													<td class='copybold'>
														<div id='showSMRPanel' style='display: none;'
															class='tableheader'>
															<%String divlink = "javascript:showHide(1)";%>
															<html:link href="<%=divlink%>">
																<!-- Modified for Case#3291 - Start
                                                To avoid the double quotes when setting the attributes to img tag-->
																<%String imagePlus=request.getContextPath()+"/coeusliteimages/plus.gif";%>
																<html:img src="<%=imagePlus%>" border="0" />
																<%--<html:img src="<%=request.getContextPath()+"/coeusliteimages/plus.gif"%>" border="0"/> --%>
																<!-- Modified for Case#3291 - End -->
															</html:link>
															&nbsp;
															<bean:message bundle="iacuc"
																key="pendingPI.SMRprotocolSearch" />
														</div>
														<div id='hideSMRPanel' class='tableheader'>
															<% divlink = "javascript:showHide(2)";%>
															<html:link href="<%=divlink%>">
																<!-- Modified for Case#3291 - Start
                                                To avoid the double quotes when setting the attributes to img tag-->
																<%String imageMinus=request.getContextPath()+"/coeusliteimages/minus.gif";%>
																<html:img src="<%=imageMinus%>" border="0" />
																<%--<html:img src="<%=request.getContextPath()+"/coeusliteimages/minus.gif"%>" border="0"/> --%>
																<!-- Modified for Case#3291 - End -->
															</html:link>
															&nbsp;
															<bean:message bundle="iacuc"
																key="pendingPI.SMRprotocolSearch" />
														</div>
													</td>
												</tr>
												<tr class='tabtable'>
													<td></td>
												</tr>
												<tr>
													<td>
														<div id='smrPanel'>
															<table width="933" align=center border="0"
																cellpadding="1" cellspacing="0" id="t1" class="sortable">
																<tr>
																	<%if(vecColumnData != null && vecColumnData.size()>0){
                                                         for(int index=0;index<vecColumnData.size();index++){
                                                             edu.mit.coeus.search.bean.DisplayBean displayBean = (edu.mit.coeus.search.bean.DisplayBean)vecColumnData.elementAt(index);
                                                             if(displayBean.isVisible()){
                                                                 String strColumnName = displayBean.getValue();
                                                                 %>
																	<td class="theader"><%=strColumnName%></td>
																	<%}}}%>
																</tr>
																<logic:present
																	name="PENDING_PI_ACTION_SMR_RQD_IACUC_PROTO_SEARCH">
																	<%String strBgColor = "#DCE5F1";
                                                          String protocolNumber="";
                                                          int count = 0;%>
																	<logic:iterate id="smrProtoSearch"
																		name="PENDING_PI_ACTION_SMR_RQD_IACUC_PROTO_SEARCH"
																		type="java.util.HashMap" scope="session">
																		<%if (count%2 == 0) 
                                                                              strBgColor = "#D6DCE5"; 
                                                                           else 
                                                                               strBgColor="#DCE5F1";%>
																		<tr bgcolor="<%=strBgColor%>" valign="top"
																			onmouseover="className='TableItemOn'"
																			onmouseout="className='TableItemOff'">
																			<%String destPage = CoeusLiteConstants.GENERAL_INFO_PAGE;
                                                                             if(vecColumnData != null && vecColumnData.size()>0){
                                                                                String sequenceNumber = smrProtoSearch.get("SEQUENCE_NUMBER").toString().trim();
                                                                                for(int index=0;index<vecColumnData.size();index++){
                                                                                 edu.mit.coeus.search.bean.DisplayBean displayBean = 
                                                                                    (edu.mit.coeus.search.bean.DisplayBean)vecColumnData.elementAt(index);
                                                                                    if(!displayBean.isVisible())
                                                                                        continue;
                                                                                    String key = displayBean.getName();
                                                                                    if(key != null){
                                                                                        String value = smrProtoSearch.get(key) == null ? "" :
                                                                                                smrProtoSearch.get(key).toString();
                                                                                            if(index == 0){
                                                                                            protocolNumber=value;%>

																			<%} else if(index==1){
                                                                                if(value != null && value.length() > 80){
                                                                                    value = value.substring(0,70);
                                                                                    value = value+" ...";
                                                                                }
                                                                             }
                                                                              System.out.println("value :"+value+" : index value :"+index);          
                                                                            %>
																			<td align="left" nowrap class="copy"><a
																				href="<%=request.getContextPath()%>/getIacucData.do?SEARCH_ACTION=SEARCH_ACTION&protocolNumber=<%=protocolNumber.trim()%>&PAGE=<%=destPage.trim()%>&sequenceNumber=<%=sequenceNumber%>&notFromIsReviewer=true">
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
																	name="PENDING_PI_ACTION_SMR_RQD_IACUC_PROTO_SEARCH">
																	<tr>
																		<td colspan='4' class='copy'>No rows found with
																			current selection criteria</td>
																	</tr>
																</logic:notPresent>
															</table>
														</div>
													</td>
												</tr>
											</table>
										</td>
									</tr>

									<tr>
										<td>&nbsp;</td>
									</tr>

									<tr>
										<td colspan='4'>
											<table width="933" align=center border="0" cellpadding="0"
												cellspacing="0">
												<tr>
													<td class='copybold'>
														<div id='showSRPanel' style='display: none;'
															class='tableheader'>
															<%divlink = "javascript:showHide(3)";%>
															<html:link href="<%=divlink%>">
																<!-- Modified for Case#3291 - Start
                                                To avoid the double quotes when setting the attributes to img tag-->
																<%String imagePlus=request.getContextPath()+"/coeusliteimages/plus.gif";%>
																<html:img src="<%=imagePlus%>" border="0" />
																<%--<html:img src="<%=request.getContextPath()+"/coeusliteimages/plus.gif"%>" border="0"/> --%>
																<!-- Modified for Case#3291 - End -->
															</html:link>
															&nbsp;
															<bean:message bundle="iacuc"
																key="pendingPI.SRProtocolSearch" />
														</div>
														<div id='hideSRPanel' class='tableheader'>
															<% divlink = "javascript:showHide(4)";%>
															<html:link href="<%=divlink%>">
																<!-- Modified for Case#3291 - Start
                                                To avoid the double quotes when setting the attributes to img tag-->
																<%String imageMinus=request.getContextPath()+"/coeusliteimages/minus.gif";%>
																<html:img src="<%=imageMinus%>" border="0" />
																<%--<html:img src="<%=request.getContextPath()+"/coeusliteimages/minus.gif"%>" border="0"/> --%>
																<!-- Modified for Case#3291 - End -->
															</html:link>
															&nbsp;
															<bean:message bundle="iacuc"
																key="pendingPI.SRProtocolSearch" />
														</div>
													</td>
												</tr>
												<tr class='tabtable'>
													<td></td>
												</tr>
												<tr>
													<td>
														<div id='srPanel'>
															<table width="933" align=center border="0"
																cellpadding="1" cellspacing="0" id="t1" class="sortable">
																<tr>
																	<%if(vecColumnData != null && vecColumnData.size()>0){
                                                         for(int index=0;index<vecColumnData.size();index++){
                                                             edu.mit.coeus.search.bean.DisplayBean displayBean = (edu.mit.coeus.search.bean.DisplayBean)vecColumnData.elementAt(index);
                                                             if(displayBean.isVisible()){
                                                                 String strColumnName = displayBean.getValue();
                                                                 %>
																	<td class="theader"><%=strColumnName%></td>
																	<%}}}%>
																</tr>
																<logic:present
																	name="PENDING_PI_ACTION_SR_RQD_IACUC_PROTO_SEARCH">
																	<%String strBgColor = "#DCE5F1";
                                                          String protocolNumber="";
                                                          int count = 0;%>
																	<logic:iterate id="smrProtoSearch"
																		name="PENDING_PI_ACTION_SR_RQD_IACUC_PROTO_SEARCH"
																		type="java.util.HashMap" scope="session">
																		<%if (count%2 == 0) 
                                                                              strBgColor = "#D6DCE5"; 
                                                                           else 
                                                                               strBgColor="#DCE5F1";%>
																		<tr bgcolor="<%=strBgColor%>" valign="top"
																			onmouseover="className='TableItemOn'"
																			onmouseout="className='TableItemOff'">
																			<%String destPage = CoeusLiteConstants.GENERAL_INFO_PAGE;
                                                                             if(vecColumnData != null && vecColumnData.size()>0){
                                                                                String sequenceNumber = smrProtoSearch.get("SEQUENCE_NUMBER").toString().trim();
                                                                                for(int index=0;index<vecColumnData.size();index++){
                                                                                 edu.mit.coeus.search.bean.DisplayBean displayBean = 
                                                                                    (edu.mit.coeus.search.bean.DisplayBean)vecColumnData.elementAt(index);
                                                                                    if(!displayBean.isVisible())
                                                                                        continue;
                                                                                    String key = displayBean.getName();
                                                                                    if(key != null){
                                                                                        String value = smrProtoSearch.get(key) == null ? "" :
                                                                                                smrProtoSearch.get(key).toString();
                                                                                            if(index == 0){
                                                                                            protocolNumber=value;%>

																			<%} else if(index==1){
                                                                                if(value != null && value.length() > 60){
                                                                                    value = value.substring(0,55);
                                                                                    value = value+" ...";
                                                                                }
                                                                             }
                                                                            %>
																			<td align="left" nowrap class="copy"><a
																				href="<%=request.getContextPath()%>/getIacucData.do?SEARCH_ACTION=SEARCH_ACTION&protocolNumber=<%=protocolNumber.trim()%>&PAGE=<%=destPage.trim()%>&sequenceNumber=<%=sequenceNumber%>&notFromIsReviewer=true">
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
																	name="PENDING_PI_ACTION_SR_RQD_IACUC_PROTO_SEARCH">
																	<tr>
																		<td colspan='4' class='copy'>No rows found with
																			current selection criteria</td>
																	</tr>
																</logic:notPresent>
															</table>
														</div>
													</td>
												</tr>
											</table>
										</td>
									</tr>

									<tr>
										<td>&nbsp;</td>
									</tr>
									<tr>
										<td colspan='4'>
											<table width="933" align=center border="0" cellpadding="0"
												cellspacing="0">
												<tr>
													<td class='copybold'>
														<div id='showEXPPanel' style='display: none;'
															class='tableheader'>
															<%divlink = "javascript:showHide(5)";%>
															<html:link href="<%=divlink%>">
																<!-- Modified for Case#3291 - Start
                                                To avoid the double quotes when setting the attributes to img tag-->
																<%String imagePlus=request.getContextPath()+"/coeusliteimages/plus.gif";%>
																<html:img src="<%=imagePlus%>" border="0" />
																<%--<html:img src="<%=request.getContextPath()+"/coeusliteimages/plus.gif"%>" border="0"/> --%>
																<!-- Modified for Case#3291 - End -->
															</html:link>
															&nbsp;
															<bean:message bundle="iacuc"
																key="pendingPI.ExpiringProtocolSearch" />
														</div>
														<div id='hideEXPPanel' class='tableheader'>
															<% divlink = "javascript:showHide(6)";%>
															<html:link href="<%=divlink%>">
																<!-- Modified for Case#3291 - Start
                                                To avoid the double quotes when setting the attributes to img tag-->
																<%String imageMinus=request.getContextPath()+"/coeusliteimages/minus.gif";%>
																<html:img src="<%=imageMinus%>" border="0" />
																<%--<html:img src="<%=request.getContextPath()+"/coeusliteimages/minus.gif"%>" border="0"/> --%>
																<!-- Modified for Case#3291 - End -->
															</html:link>
															&nbsp;
															<bean:message key="pendingPI.ExpiringProtocolSearch" />
														</div>
													</td>
												</tr>
												<tr class='tabtable'>
													<td></td>
												</tr>
												<tr>
													<td>
														<div id='expPanel'>
															<table width="933" align=center border="0"
																cellpadding="1" cellspacing="0" id="t1" class="sortable">
																<tr>
																	<%if(vecColumnData != null && vecColumnData.size()>0){
                                                         for(int index=0;index<vecColumnData.size();index++){
                                                             edu.mit.coeus.search.bean.DisplayBean displayBean = (edu.mit.coeus.search.bean.DisplayBean)vecColumnData.elementAt(index);
                                                             if(displayBean.isVisible()){
                                                                 String strColumnName = displayBean.getValue();
                                                                 %>
																	<td class="theader"><%=strColumnName%></td>
																	<%}}}%>
																</tr>
																<logic:present
																	name="PENDING_PI_ACTION_EXPIRE_IACUC_PROTO_SEARCH">
																	<%String strBgColor = "#DCE5F1";
                                                          String protocolNumber="";
                                                          int count = 0;%>
																	<logic:iterate id="smrProtoSearch"
																		name="PENDING_PI_ACTION_EXPIRE_IACUC_PROTO_SEARCH"
																		type="java.util.HashMap" scope="session">
																		<%if (count%2 == 0) 
                                                                              strBgColor = "#D6DCE5"; 
                                                                           else 
                                                                               strBgColor="#DCE5F1";%>
																		<tr bgcolor="<%=strBgColor%>" valign="top"
																			onmouseover="className='TableItemOn'"
																			onmouseout="className='TableItemOff'">
																			<%String destPage = CoeusLiteConstants.GENERAL_INFO_PAGE;
                                                                             if(vecColumnData != null && vecColumnData.size()>0){
                                                                                String sequenceNumber = smrProtoSearch.get("SEQUENCE_NUMBER").toString().trim();
                                                                                for(int index=0;index<vecColumnData.size();index++){
                                                                                 edu.mit.coeus.search.bean.DisplayBean displayBean = 
                                                                                    (edu.mit.coeus.search.bean.DisplayBean)vecColumnData.elementAt(index);
                                                                                    if(!displayBean.isVisible())
                                                                                        continue;
                                                                                    String key = displayBean.getName();
                                                                                    if(key != null){
                                                                                        String value = smrProtoSearch.get(key) == null ? "" :
                                                                                                smrProtoSearch.get(key).toString();
                                                                                            if(index == 0){
                                                                                            protocolNumber=value;%>

																			<%} else if(index==1){
                                                                                if(value != null && value.length() > 60){
                                                                                    value = value.substring(0,55);
                                                                                    value = value+" ...";
                                                                                }
                                                                             }
                                                                            %>
																			<td align="left" nowrap class="copy"><a
																				href="<%=request.getContextPath()%>/getIacucData.do?SEARCH_ACTION=SEARCH_ACTION&protocolNumber=<%=protocolNumber.trim()%>&PAGE=<%=destPage.trim()%>&sequenceNumber=<%=sequenceNumber%>&notFromIsReviewer=true">
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
																	name="PENDING_PI_ACTION_EXPIRE_IACUC_PROTO_SEARCH">
																	<tr>
																		<td colspan='4' class='copy'>No rows found with
																			current selection criteria</td>
																	</tr>
																</logic:notPresent>
															</table>
														</div>
													</td>
												</tr>
											</table>
									<tr>
										<td>&nbsp;</td>
									</tr>
									</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>

	</html:form>
</body>
</html:html>
