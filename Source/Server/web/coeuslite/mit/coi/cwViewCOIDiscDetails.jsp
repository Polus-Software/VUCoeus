<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="org.apache.struts.validator.DynaValidatorForm"%>
<%@ page import="java.text.SimpleDateFormat, java.util.Date"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ taglib uri="/WEB-INF/privilege.tld" prefix="priv"%>


<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="DisclosureHeaderData" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="DisclosureInfo" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="CertificateDetails" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="proposalLog" scope="session" class="java.util.Vector" />
<jsp:useBean id="person" scope="session"
	class="edu.mit.coeus.bean.PersonInfoBean" />
<jsp:useBean id="userprivilege" class="java.lang.String" scope="session" />
<%
 String disableEdit = (String)  request.getAttribute("disableEditButton"); 
 String pageValue = (String) request.getAttribute("pageValue");
 String path = request.getContextPath();
%>

<html:html>
<head>
<title>Coeus Web</title>
<html:base />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<script
	src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/divSlide.js"></script>
<script
	src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/Balloon.js"></script>
<script language='javascript'>
        function showPopUp(linkId, frameId, width, height, src, bookmarkId, url) {
            showBalloon(linkId, frameId, width, height, src, bookmarkId);
            }
        
         function showQuestion(link)
         {
            var winleft = (screen.width - 650) / 2;
            var winUp = (screen.height - 450) / 2;  
            var win = "scrollbars=1,resizable=1,width=790,height=450,left="+winleft+",top="+winUp
            sList = window.open('<bean:write name='ctxtPath'/>'+"/"+link, "list", win);
            if (parseInt(navigator.appVersion) >= 4) {
            window.sList.focus(); 
            }
         }
        
        //var lastPopup;
        function showPopUp(linkId, frameId, width, height, src, bookmarkId, url) {
            showBalloon(linkId, frameId, width, height, src, bookmarkId);
            //showBalloonPopup(frameId);
        }
        
        var lastRowId = -1;
        var lastDataRowId;
        var lastDivId;
        var lastLinkId;
        var expandedRow = -1;

        var defaultStyle = "rowLine";
        var selectStyle = "rowHover";
        
        function selectRow(rowId, dataRowId, divId, linkId, historyFrame, url) {
            var row = document.getElementById(rowId); 
            if(rowId == lastRowId) {
                //Same Row just Toggle
                var style = document.getElementById(dataRowId).style.display;
                if(style == "") {
                    style = "none";
                    styleClass = defaultStyle;
                    expandedRow = -1;
                    toggleText = plus.src;
                }else{
                    style = "";
                    styleClass = selectStyle;
                    expandedRow = rowId;
                    toggleText = minus.src;
                }
                //document.getElementById(dataRowId).style.display = style;
                //Sliding - START
                ds = new DivSlider();
                if(style == "") {
                    document.getElementById(dataRowId).style.display = "";
                    ds.showDiv(divId);
                }else {
                     ds.hideDiv(divId, "document.getElementById('"+dataRowId+"').style.display='none'");
                }
                //Sliding - END
                row.className = defaultStyle;
                var imgId = "img"+linkId;
                document.getElementById(imgId).src=toggleText;
            }else {
                document.getElementById(dataRowId).style.display = "";
                
                historyFrame = document.getElementById(historyFrame);
                var frameSrc = historyFrame.getAttribute("src");
                if(frameSrc == null || frameSrc == "") {
                    historyFrame.setAttribute("src", url);
                }
                
                //Sliding - START
                ds2 = new DivSlider();
                ds2.showDiv(divId);
                //Sliding - END

                row.className = defaultStyle;
                expandedRow = rowId;
                var imgId = "img"+linkId;
                document.getElementById(imgId).src=minus.src;
                
                //reset last selected row
                if(lastRowId != -1) {
                    row = document.getElementById(lastRowId);
                    //document.getElementById(lastDataRowId).style.display = "none";

                    ds3 = new DivSlider();
                    ds3.hideDiv(lastDivId, "document.getElementById('"+lastDataRowId+"').style.display='none'");

                    row.className = defaultStyle;
                    var imgId = "img"+lastLinkId;
                    document.getElementById(imgId).src=plus.src;
                }
            }

            lastRowId = rowId;
            lastDivId = divId;
            lastDataRowId = dataRowId;
            lastLinkId = linkId;
        }
        
        
        var plus = new Image();
        var minus = new Image();
        
        function preLoadImages() {
            plus.src = "<%=path%>/coeusliteimages/plus.gif";
            minus.src = "<%=path%>/coeusliteimages/minus.gif";
        }
        preLoadImages();
         
        </script>

</head>

<body>

	<table width="980" border="0" cellpadding="0" cellspacing="0"
		class="table">

		<logic:present name="DisclosureHeaderData" scope="session">
			<logic:iterate id="headerData" name="DisclosureHeaderData"
				type="org.apache.commons.beanutils.DynaBean">
				<tr>
					<td height="20" align="left" valign="top" class="theader"><bean:message
							bundle="coi" key="coiDisclosure.headerDiscldetails" /> <%=person.getFullName()%>
					</td>
				</tr>
				<logic:present name="noFinEntAnsReqExpl">
					<tr class='copy'>
						<td><font color='red'><bean:message bundle="coi"
									key="viewCOIDisclosureDetails.noFinEntAnsReqExpl" /></font></td>
					</tr>
				</logic:present>
				<%DynaValidatorForm formData =(DynaValidatorForm)request.getAttribute("coiHeaderInfo"); %>
				<tr>
					<td width="70%" valign="top" class="copy" align="left">
						<table width="100%" border="0" cellpadding="0">
							<% if( disableEdit == null && pageValue ==null) {%>
							<tr>
								<td align="right" class='copy'><logic:equal
										name="canapprove" value="true" scope="request">
										<logic:notEqual name="userprivilege" value="">
											<priv:hasOSPRight name="hasosprighttoedit"
												value="<%=Integer.parseInt(userprivilege)%>">
												<input type="button" value="Approve"
													onclick="javascript:window.location='<%=path%>/approveDisclosureAction.do?disclosureNo=<bean:write name='headerData' property='coiDisclosureNumber'/>'"
													style="width: 100px">
											</priv:hasOSPRight>
										</logic:notEqual>
									</logic:equal></td>
								<td>&nbsp;</td>
								<td><input type="button" value="Edit"
									onclick="javascript:window.location='<%=path%>/editCoiDisc.do?disclosureNo=<bean:write name='headerData' property='coiDisclosureNumber'/>'"
									style="width: 100px"></td>
							</tr>
							<% } %>

							<tr>
								<logic:equal name="headerData" property="moduleCode" value="1">
									<td nowrap class="copybold" width="10%" align="left"><bean:message
											bundle="coi" key="label.awardTitle" /></td>
									<td width="6">
										<div align="left">:</div>
									</td>
									<td width="25%" class="copy" nowrap colspan="2">
										&nbsp;&nbsp; <%=formData.get("title")%>
									</td>
								</logic:equal>

								<logic:notEqual name="headerData" property="moduleCode"
									value="1">
									<td nowrap class="copybold" width="10%" align="left"><bean:message
											bundle="coi" key="label.proposalTitle" /></td>
									<td width="6">
										<div align="left">:</div>
									</td>
									<td width="25%" class="copy" nowrap colspan="2">&nbsp;&nbsp;
										<%=formData.get("title")%>
									</td>

								</logic:notEqual>
								<td class="copybold" align="right"><bean:message
										bundle="coi" key="label.createDate" /></td>
								<td class="copybold">:</td>
								<td class="copy">
									<% 
                                                 SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = simpleDateFormat.parse((String) request.getAttribute("createTimestamp"));
            SimpleDateFormat requiredDateFormat = new SimpleDateFormat("MM-dd-yyyy  hh:mm a");
            String output = requiredDateFormat.format(date);
                                                 out.print(output);%>
									&nbsp; <%= request.getAttribute("createUser")%>
								</td>
							</tr>

							<tr>
								<td nowrap class="copybold" width="10%" align="left"><bean:message
										bundle="coi" key="label.sponsor" /></td>
								<td width="6">
									<div align="left">:</div>
								</td>
								<td class="copy" nowrap colspan="2">&nbsp;&nbsp; <%=formData.get("sponsorName")%>
								</td>

								<td nowrap class="copybold" align="right"><bean:message
										bundle="coi" key="label.lastUpdated" /></td>
								<td class="copybold">:</td>
								<td class="copy" nowrap><coeusUtils:formatDate
										name="headerData" formatString="MM-dd-yyyy  hh:mm a"
										property="updtimestamp" /> &nbsp;<bean:write name="headerData"
										property="upduser" /></td>
							</tr>

							<%DynaValidatorForm formProposalData =(DynaValidatorForm)request.getAttribute("proposalLog"); %>

							<logic:present name="proposalLog" scope="request">

								<logic:equal name="headerData" property="moduleCode" value="2">
									<tr>
										<td nowrap class="copybold" width="10%" align="left"><bean:message
												bundle="coi" key="label.proposalType" /></td>
										<td width="6">
											<div align="left">:</div>
										</td>
										<td class="copy" nowrap colspan="2">&nbsp;&nbsp; <%=formProposalData.get("proposalType")%>
										</td>
									</tr>
								</logic:equal>
							</logic:present>

							</logic:iterate>
							</logic:present>
						</table>
					</td>
				</tr>
				<tr class='copy' align="left">
					<font color="red"> <html:messages id="message"
							message="true">
							<bean:write name="message" />
						</html:messages>
					</font>
				</tr>
				<!-- Certification - Start  -->
				<tr>
					<td align="center" valign="top">
						<table width="99%" border="0" align="center" cellpadding="2"
							cellspacing="0" class="tabtable">
							<tr class="tableheader">
								<td colspan="3"><bean:message bundle="coi"
										key="coiDisclosure.headerCert" /></td>
							</tr>
							<logic:present name="CertificateDetails" scope="session">
								<logic:iterate id="viewDataCert" name="CertificateDetails"
									type="org.apache.commons.beanutils.DynaBean">
									<tr class="rowLine" onmouseover="className='rowHover rowLine'"
										onmouseout="className='rowLine'">
										<td width="8%" align="center" class="copybold" valign="top">
											Question<coeusUtils:formatOutput name="viewDataCert"
												property="label" />
										</td>
										<td width="80%" class="copy" wrap colspan="0"><coeusUtils:formatOutput
												name="viewDataCert" property="description" /> <!--coeusUtils:formatOutput name="viewDataCert" property="description"  /-->
											<a
											href='javascript:showQuestion("/question.do?questionNo=<bean:write name='viewDataCert' property='questionId'/>",  "ae");'
											method="POST"> <!--bean:message key="financialEntityDetails.question.explanationLink" /-->
												<u> <bean:message bundle="coi"
														key="financialEntity.moreAbtQuestion" />
											</u>
										</a></td>
										<td align="center" class="copy"><logic:equal
												name="viewDataCert" property="answer" value='Y'>
												<bean:message bundle="coi" key="financialEntity.yes" />
											</logic:equal> <logic:equal name="viewDataCert" property="answer" value='N'>
												<bean:message bundle="coi" key="financialEntity.no" />
											</logic:equal> <logic:equal name="viewDataCert" property="answer" value='X'>
												<bean:message bundle="coi" key="financialEntity.na" />
											</logic:equal> <logic:notPresent name="viewDataCert" property="answer">
                                    &nbsp;
                                </logic:notPresent></td>
									</tr>
								</logic:iterate>
							</logic:present>

						</table>
					</td>
				</tr>
				<!-- Certification - End  -->

				<!-- Financial Entities Disclosed  - Start -->
				<tr>
					<td height="52" align="left" valign="top"><br>
						<table width="99%" border="0" align="center" cellpadding="0"
							cellspacing="0" class="tabtable">
							<tr>
								<td colspan="4" align="left" valign="top"><table
										width="100%" height="20" border="0" cellpadding="0"
										cellspacing="0" class="tableheader">
										<tr>
											<td><bean:message bundle="coi"
													key="coiDisclosure.headerFinEntities" /></td>
										</tr>
									</table></td>
							</tr>
							<tr>
								<td class="copy">&nbsp;&nbsp;&nbsp; <b> <bean:message
											bundle="coi" key="coiDisclosure.subheaderRelationShip" />
								</b></td>
							</tr>
							<tr align="center" valign="top">
								<td colspan="3">
									<!-- <DIV STYLE="overflow: auto; width: 948px;  padding:0px; margin: 0px">-->
									<table width="98%" border="0" cellpadding="2" class="tabtable">
										<tr>
											<td width="1%" class="theader">&nbsp;</td>
											<td width="15%" class="theader"><bean:message
													bundle="coi" key="label.entityName" /></td>
											<td width="15%" class="theader"><bean:message
													bundle="coi" key="label.relationShipToEntity" /></td>
											<td class="theader" width="10%" colspan="2"><bean:message
													bundle="coi" key="label.conflictStatus" /></td>
											<td class="theader" width="9%" height="25"><bean:message
													bundle="coi" key="label.reviewedBy" /></td>
											<td class="theader" width="25%" height="25"><bean:message
													bundle="coi" key="label.relationshipDesc" /></td>
											<td class="theader" width="20%" height="25"><bean:message
													bundle="coi" key="label.lastUpdated" /></td>
											<td class="theader" width="5%">&nbsp;</td>
											<%--td class="theader" width="10%" height="25">
                                                    <bean:message bundle="coi" key="label.userName"/>
                                                </td--%>
										</tr>
										<!-- tables values  -->
										<logic:present name="DisclosureInfo" scope="session">
											<%  int disclIndex = 0;
                                                     String strBgColor = "#DCE5F1";

                                                    %>
											<logic:iterate id="discInfo" name="DisclosureInfo"
												type="org.apache.commons.beanutils.DynaBean">
												<%                                  
                                                           if (disclIndex%2 == 0) {
                                                                strBgColor = "#D6DCE5"; 
                                                            }
                                                           else { 
                                                                strBgColor="#DCE5F1"; 
                                                             }
                                                        %>
												<tr bgcolor='<%=strBgColor%>' id="row<%=disclIndex%>"
													class="rowLine" onmouseover="className='rowHover rowLine'"
													onmouseout="className='rowLine'">

													<td class="copy" wrap colspan="0">
														<%
                                                            String entityNumber = (String)discInfo.get("entityNumber");
                                                            String disclosureNum = (String)discInfo.get("coiDisclosureNumber");
                                                            String seqNum = (String)discInfo.get("sequenceNum");
                                                            String entitySeqNum = (String)discInfo.get("entitySequenceNumber");
                                                            String url = ""+path+"/viewDisclosureDetails.do?entityNum="+entityNumber+"&disclNum="+disclosureNum+"&view=history";
                                                            //url = ""+path+"/viewFinEntityDisplay.do?entityNumber="+entityNumber+"&seqNum="+seqNum+"&header=no";
                                                            //<a href="JavaScript:open_new_window('<bean:write name='ctxtPath'/>/viewFinEntityDisplay.do?entityNumber=<bean:write name='infoHistory' property='entityNumber'/>&seqNum=<bean:write name='infoHistory' property='entitySequenceNumber' />&header=no' ,'EntityDetails');"  method="POST" > 
                                                            
                                                            String onClick = "selectRow('row"+disclIndex+"', 'historyRow"+disclIndex+"', 'historyData"+disclIndex+"' , 'toggle"+disclIndex+"', 'historyFrame"+disclIndex+"', '"+url+"')";
                                                            %> <script>
                                                                var link = "javascript:<%=onClick%>";
                                                                document.write("<a href=\""+link+"\" id=\"toggle<%=disclIndex%>\"> <img src='<%=request.getContextPath()%>/coeusliteimages/plus.gif' border='0' id='imgtoggle<%=disclIndex%>' name='imgtoggle<%=disclIndex%>'> </a>");
                                                            </script>
														<noscript>
															<a target="_blank" href="<%=url%>">&gt;&gt;</a>
														</noscript>
													</td>

													<td class="copy" wrap colspan="0">
														<%     
                                                        java.util.Map params = new java.util.HashMap();

                                                        //params.put("disclNum", discInfo.get("coiDisclosureNumber"));
                                                        //params.put("entityNum", discInfo.get("entityNumber"));
                                                        //pageContext.setAttribute("paramsMap", params);     
                                                        url = ""+path+"/viewFinEntityDisplay.do?entityNumber="+entityNumber+"&header=no";
                                                         %> <%--html:link  action="/viewDisclosureDetails.do" name="paramsMap" scope="page"  --%>
														<a target="_blank" href="<%=url%>"> <u> <coeusUtils:formatOutput
																	name="discInfo" property="entityName" />
														</u>
													</a> <%--/html:link--%>
													</td>
													<td><coeusUtils:formatOutput name="discInfo"
															property="relationShipTypeDesc" /></td>
													<td class="copy" wrap colspan="2"><coeusUtils:formatOutput
															name="discInfo" property="coiStatus" /></td>
													<td class="copy" wrap colspan="0" height="25"><coeusUtils:formatOutput
															name="discInfo" property="coiReviewer" /> <!--coeusUtils:formatOutput name="viewDataCert" property="description"  /-->
													</td>
													<td class="copy" wrap colspan="0" height="25">
														<%
                                                        Object desc = ((org.apache.commons.beanutils.DynaBean)discInfo).get("description");
                                                        String strDesc = (String)desc;
                                                        int offset = 25;
                                                        if(strDesc != null && (strDesc.length() > offset)) {
                                                            out.println(strDesc.substring(0, offset));
                                                            %> <a
														id="link<%=disclIndex%>"
														href="javascript:showPopUp('link<%=disclIndex%>', 'relationShipDesc<%=disclIndex%>')"
														title="Click to View Relationship Description">[...]</a>

														<div id="relationShipDesc<%=disclIndex%>"
															style="display: none; overflow: auto; position: absolute; width: 250; height: 75"
															onclick="hideBalloon('relationShipDesc<%=disclIndex%>')">
															<table border="0" width="100%" cellpadding="2"
																cellspacing="0" class="lineBorderWhiteBackgrnd">
																<tr>
																	<td style="border-style: none"></td>
																	<td style="border-style: none" align="right"><a
																		href="javascript:hideBalloon('relationShipDesc<%=disclIndex%>')"><img
																			border="0"
																			src="<bean:write name='ctxtPath'/>/coeusliteimages/none.gif"></a></td>
																</tr>
																<tr>
																	<td style="border-style: none" colspan="2" valign="top">
																		<bean:write name="discInfo" property="description" />
																	</td>
																</tr>
															</table>
														</div> <%
                                                        }else if(strDesc != null){
                                                            out.println(strDesc);
                                                        }
                                                        %> <%--<coeusUtils:formatOutput name="discInfo" property="description"/>--%>
													</td>

													<td class="copy" nowrap>
														<%--coeusUtils:formatOutput name="discInfo" property="updtimestamp"/--%>
														<coeusUtils:formatDate name="discInfo"
															formatString="MM-dd-yyyy  hh:mm a"
															property="updtimestamp" /> &nbsp; <bean:write
															name="discInfo" property="upduser" />
													</td>
													<td class="copybold"><logic:greaterThan
															name="discInfo" property="updated" value="0">
															<font color="red">(updated)</font>
														</logic:greaterThan></td>
													<%--td class="copy">
                                                        <bean:write name="discInfo" property="upduser"/>
                                                    </td--%>

												</tr>

												<!-- History Details - START-->
												<tr id="historyRow<%=disclIndex%>" style="display: none"
													class="copy rowHover">
													<td colspan="9">
														<div id="historyData<%=disclIndex%>"
															style="overflow: hidden;">
															<%--<iframe id="historyFrame<%=index%>" src="<%=path%>/getFinEntHistory.do?entityNumber=<bean:write name="data" property="entityNumber"/>" width="100%" name="historyFrame<%=index%>" scrolling="auto" marginheight="0" marginwidth="0" frameborder=0>  --%>
															<iframe id="historyFrame<%=disclIndex%>" width="100%"
																name="historyFrame<%=disclIndex%>" scrolling="auto"
																marginheight="0" marginwidth="0" frameborder=0>
															</iframe>
														</div>
													</td>
												</tr>
												<!-- History Details - END -->

												<% disclIndex++ ;%>
											</logic:iterate>
										</logic:present>
									</table> <!--</div>-->
								</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
							</tr>
						</table></td>
				</tr>
				<!-- Financial Entities Disclosed  -End -->

				<!--Disclosure Details  -Start -->

				<% 	int userpriv = Integer.parseInt(userprivilege);
                                if(userpriv > 0 )
                                {
                            %>
				<tr>
					<td height="119" align="left" valign="top"><br>
						<table width="99%" border="0" align="center" cellpadding="0"
							cellspacing="0" class="tabtable">
							<tr>
								<td align="left" valign="top">
									<table width="100%" height="20" border="0" cellpadding="0"
										cellspacing="0" class="tableheader">
										<tr>
											<td><bean:message bundle="coi"
													key="viewCoiDisclosure.disclDetails" /></td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td>
									<%  DynaValidatorForm disclFormData =(DynaValidatorForm)request.getAttribute("coiHeaderInfo");
                                           String sponsorName = (String) disclFormData.get("sponsorName");
                                           String account = (String)disclFormData.get("account");
                                           String awardName =(String) disclFormData.get("awardName");
                                       %>
									<table width="100%" border="0" cellspacing="0" cellpadding="5">
										<tr>
											<td colspan="6" height="10"></td>
										</tr>
										<tr>
											<td class="copybold" width="123"><bean:message
													bundle="coi" key="label.sponsor" /></td>
											<td class="copy" width="3"><div>:</div></td>
											<td class="copy" width="238"><%= (sponsorName!=null)? sponsorName:""%>
											</td>

											<td class="copybold" width="123"><bean:message
													bundle="coi" key="label.disclosureNumber" /></td>
											<td class="copy" width="3"><div>:</div></td>

											<logic:present name="DisclosureHeaderData" scope="session">

												<logic:iterate id="headerData" name="DisclosureHeaderData"
													type="org.apache.commons.beanutils.DynaBean">
													<td class="copy" width="238">
														<div align="left">
															&nbsp;&nbsp;
															<coeusUtils:formatOutput name="headerData"
																property="coiDisclosureNumber" />
														</div>
													</td>
										</tr>
										<tr>
											<td class="copybold" width="123"><bean:message
													bundle="coi" key="label.appliesTo" /></td>
											<td width="6"><div>:</div></td>
											<td class="copy" width="238"><coeusUtils:formatOutput
													name="headerData" property="module" defaultValue="&nbsp;" />
											</td>
											<logic:equal name="headerData" property="moduleCode"
												value="1">
												<td nowrap class="copybold" width="123" align="left"><bean:message
														bundle="coi" key="label.awardNumber" /></td>
												<td width="6">
													<div align="left">:</div>
												</td>
												<td class="copy" width="238" nowrap colspan="2">
													&nbsp;&nbsp; <coeusUtils:formatOutput name="headerData"
														property="moduleItemKey" />
												</td>
											</logic:equal>
											<logic:notEqual name="headerData" property="moduleCode"
												value="1">
												<td nowrap class="copybold" width="123" align="left"><bean:message
														bundle="coi" key="label.proposalNumber" /></td>
												<td width="6">
													<div align="left">:</div>
												</td>
												<td class="copy" nowrap colspan="2">&nbsp;&nbsp; <coeusUtils:formatOutput
														name="headerData" property="moduleItemKey" />
												</td>
											</logic:notEqual>
										</tr>
										<tr>
											<td class="copybold" width="123"><bean:message
													bundle="coi" key="label.costObject" /></td>
											<td width="6"><div>:</div></td>
											<td class="copy" width="238"><%= (account!=null)? account:""%>
											</td>
											<logic:equal name="headerData" property="moduleCode"
												value="1">
												<td nowrap class="copybold" width="123" align="left"><bean:message
														bundle="coi" key="label.awardStatus" /></td>
												<td width="6">
													<div align="left">:</div>
												</td>
												<td class="copy" width="238" nowrap colspan="2">
													&nbsp;&nbsp; <%= (awardName!=null)? awardName:""%>
												</td>
											</logic:equal>
											<logic:notEqual name="headerData" property="moduleCode"
												value="1">
												<td nowrap class="copybold" width="123" align="left">
													<%--Proposal Status--%> <bean:message bundle="coi"
														key="label.proposalStatus" />
												</td>
												<td width="6">
													<div align="left">:</div>
												</td>
												<td class="copy" nowrap colspan="2">&nbsp;&nbsp; <%= (awardName!=null)? awardName:""%>
												</td>
											</logic:notEqual>
										</tr>
										<tr>
											<td class="copybold" width="123"><bean:message
													bundle="coi" key="label.disclosureType" /></td>
											<td width="6"><div>:</div></td>
											<td class="copy" width="238"><logic:equal
													name="headerData" property="disclosureType" value="I">
                                                                            Initial
                                                                </logic:equal> <logic:notEqual
													name="headerData" property="disclosureType" value="I">
                                                                            Annual
                                                                </logic:notEqual></td>
											<td nowrap class="copybold" width="123" align="left"><bean:message
													bundle="coi" key="label.disclosureStatus" /></td>
											<td width="6">
												<div align="left">:</div>
											</td>
											<td class="copy" width="238" nowrap colspan="2">
												&nbsp;&nbsp; <coeusUtils:formatOutput name="headerData"
													property="discStat" />
											</td>

										</tr>
										<tr>
											<td class="copybold" width="123"><bean:message
													bundle="coi" key="label.reviewedBy" /></td>
											<td width="6"><div>:</div></td>
											<td class="copy" width="238"><coeusUtils:formatOutput
													name="headerData" property="reviewer" /></td>
											<td nowrap class="copybold" width="123" align="left"><bean:message
													bundle="coi" key="label.lastUpdated" /></td>
											<td width="6">
												<div align="left">:</div>
											</td>
											<td class="copy" width="238" nowrap colspan="2">
												&nbsp;&nbsp; <coeusUtils:formatDate name="headerData"
													formatString="MM-dd-yyyy  hh:mm a" property="updtimestamp" />
												&nbsp; <bean:write name="headerData" property="upduser" />
											</td>

										</tr>


										</logic:iterate>
										</logic:present>
									</table>
								</td>
							</tr>
							<tr>
								<td height='10'>&nbsp;</td>
							</tr>
						</table></td>
				</tr>
				<%
                             }
                            %>
				<!--Disclosure Details  -End -->
				<tr>
					<td height='10'>&nbsp;</td>
				</tr>
	</table>

</body>
</html:html>