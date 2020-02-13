<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="util"%>

<%@ page import="java.util.HashMap,edu.mit.coeus.utils.ModuleConstants"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="inboxList" scope="session" class="java.util.Vector" />
<%--<jsp:useBean  id="COIFromUsers" scope="session" class="java.util.HashMap"/>--%>
<jsp:useBean id="inboxColumnNames" scope="session"
	class="java.util.Vector" />
<bean:size id="inboxListSize" name="inboxList" />

<html:html>
<head>
<title>Coeus Web</title>
<html:base />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/ipf.css"
	rel="stylesheet" type="text/css">
<!--Added for Princeton Enhancement Case # 2802 - Start-->
<script>
            function subcontractInvoiceSummary(seqNum,lineNum,subcontractId,messageId){
                document.inboxForm.action = "<%=request.getContextPath()%>/getSubcontractInvSummary.do?invoiceSeqNum="+seqNum+"&invoiceLineNum="+lineNum+
                                            "&invoiceSubId="+subcontractId+"&invMessageId="+messageId;
                document.inboxForm.submit();
            }
            <!--Added for Princeton Enhancement Case # 2802 - End-->
            <!--Added for Case#3682 - Enhancements related to Delegations - Start -->
            function delegationDetails(moduleItemKey,moduleCode){                
                document.inboxForm.action = "<%=request.getContextPath()%>/displayProposal.do?messageType=unresolved&deleModItemKey="+moduleItemKey+"&deleModuleCode="+moduleCode;
                document.inboxForm.submit();
            }
            <!-- Added for Case#3682 - Enhancements related to Delegations - End -->
        </script>
</head>
<body>
	<html:form action="/updateInboxMessages.do?messageType=unresolved"
		method="POST">
		<a name="top"></a>
		<table width="100%" height="100%" border="0" cellpadding="0"
			cellspacing="0" class="table">
			<%String messageType = request.getParameter("messageType"); %>
			<!-- EntityDetails - Start  -->
			<%--<tr><td height="1">&nbsp;</td></tr>--%>
			<tr>
				<td height="119" align="left" valign="top">
					<table width="99%" border="0" align="center" cellpadding="0"
						cellspacing="0" class="tabtable">
						<tr>
							<td colspan="4" align="left" valign="top">
								<table width="100%" height="20" border="0" cellpadding="0"
									cellspacing="0" class="tableheader">
									<tr class="theader">
										<td><bean:message bundle="coi"
												key="unresolvedMsgs.header" /></td>
									</tr>
								</table>
							</td>
						</tr>
						<logic:equal name="inboxListSize" value="0">
							<%--<tr>
                                    <td>
                                        <table width="95%" align="right" border="0">
                                            <tr>
                                                <td>
                                                    <b> <bean:message bundle="coi" key="unresolvedMsgs.noMessages"/>    
                                                    <!--bean:write name="userInfoBean" property="userName" /--></b>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>--%>
							<tr>
								<td><logic:messagesPresent message="true">
										<html:messages id="message" message="true"
											property="listExceedsLimit" bundle="coi">
											<b><bean:write name="message" /><b>
										</html:messages>
									</logic:messagesPresent> <logic:messagesNotPresent message="true">
										<b> <bean:message bundle="coi"
												key="unresolvedMsgs.noMessages" /> <!--bean:write name="userInfoBean" property="userName" /--></b>
									</logic:messagesNotPresent> </font></td>
							</tr>

						</logic:equal>

						<logic:notEqual name="inboxListSize" value="0">

							<tr>
								<td align="left" class="copy"
									style='height: 30px; padding-left: 5px'><html:submit
										property="save" value="Move Selected Messages"
										styleClass="clbutton" /></td>
							</tr>
							<tr class='copy' align="left">
								<td><font color="red"> <html:messages id="message"
											message="true" bundle="coi">
											<bean:write name="message" />
										</html:messages>
								</font></td>
							</tr>

							<tr align="center">
								<td colspan="3">
									<DIV
										STYLE="overflow: auto; width: 955px; padding: 0px; margin: 0px">
										<table width="100%" border="0" cellpadding="1" cell
											cellspacing="0" class="tabtable">
											<%--Code commented for COEUSQA-2073-Improve management of Coeus Inbox Starts--%>
											<%--<tr>
                                                    <td width="5%" align="left" class="theader">&nbsp;<img src="<bean:write name='ctxtPath'/>/coeusliteimages/checked.gif" border="0"/></td>
                                                    <td width="5%" align="left" class="theader"><html:link page="/getInboxMessages.do?messageType=unresolved" anchor="legend">
                                                        <img src="<bean:write name='ctxtPath'/>/coeusliteimages/gflag.gif" border="0">
                                                    </html:link>
                                                    </td>
                                                                                       
                                                    <td width="5%" align="left" class="theader"> <bean:message bundle="coi" key="label.from"/></td>
                                                    <td width="45%" align="left" class="theader">  <bean:message bundle="coi" key="label.message"/></td>
                                                    <td width="20%" align="left" class="theader"> <bean:message bundle="coi" key="label.proposalTitle"/></td>
                                                    <td width="10%" align="left" class="theader"> <bean:message bundle="coi" key="label.proposalNumber"/></td>
                                                    <td width="10%" align="left" class="theader"> <bean:message bundle="coi" key="label.dateReceived"/></td>
                                                </tr>--%>
											<%--Code commented for COEUSQA-2073-Improve management of Coeus Inbox Ends--%>
											<%--COEUSQA-2073-Improve management of Coeus Inbox Starts--%>
											<tr>
												<td width="5%" align="left" class="theader">&nbsp;<img
													src="<bean:write name='ctxtPath'/>/coeusliteimages/checked.gif"
													border="0" /></td>
												<%
                                                     if(inboxColumnNames != null && inboxColumnNames.size()>0) {
                                                         for(int index=0;index<inboxColumnNames.size();index++) {
                                                             edu.mit.coeus.search.bean.DisplayBean displayBean = (edu.mit.coeus.search.bean.DisplayBean)inboxColumnNames.elementAt(index);
                                                             if(displayBean.isVisible()) {
                                                                 String strColumnName = displayBean.getValue();
                                                                 String key = displayBean.getName();
                                                                 if(key.equals("DEADLINE_DATE")) { %>
												<td width="5%" align="left" class="theader"><html:link
														page="/getInboxMessages.do?messageType=unresolved"
														anchor="legend">
														<img
															src="<bean:write name='ctxtPath'/>/coeusliteimages/gflag.gif"
															border="0">
													</html:link></td>
												<%} else { %>
												<td class="theader"><%=strColumnName%></td>
												<%}
                                                             }
                                                        }
                                                     }
                                                    %>
											</tr>
											<%--COEUSQA-2073-Improve management of Coeus Inbox Ends--%>
											<% int count =0; %>
											<%  int disclIndex = 0;
                                                    String strBgColor = "#DCE5F1";
                                                %>
											<logic:iterate id="inbox" scope="session" name="inboxList"
												type="org.apache.commons.beanutils.DynaBean" indexId="ctr">

												<%
                                                        Integer countInt = new Integer(count);
                                                        String countString = countInt.toString();
                                                        /*HashMap hmFromUsers  = (HashMap)session.getAttribute("COIFromUsers");*/
                                                        java.util.Vector vecInbox = (java.util.Vector) inbox.get("vecSearchXML");
                                                        java.util.HashMap hmInbox = (java.util.HashMap) vecInbox.get(0);
                                                    %>
												<%
                                                        if (disclIndex%2 == 0) {
                                                                strBgColor = "#D6DCE5";
                                                        } else {
                                                                strBgColor="#DCE5F1";
                                                        }
                                                        %>

												<tr valign='top' bgcolor='<%=strBgColor%>'
													onmouseover="className='TableItemOn'"
													onmouseout="className='TableItemOff'">
													<td><html:multibox property="whichMessagesAreChecked">
															<%= countInt %>
															<%--   <%=inbox.get("messageId")%> --%>
														</html:multibox></td>
													<%--Code commented for COEUSQA-2073-Improve management of Coeus Inbox Starts--%>
													<%--<td width='5%'>
                                                            <logic:notEqual name="inbox" property="daysUntilDeadline" value="-1">
                                                                <logic:greaterEqual name="inbox" property="daysUntilDeadline" value="0" >
                                                                    <logic:lessThan name="inbox" property="daysUntilDeadline" value="3" >
                                                                        <img src="<bean:write name='ctxtPath'/>/coeusliteimages/rflag.gif" border="0"/> 
                                                                    </logic:lessThan>
                                                                    <logic:greaterEqual name="inbox"
                                                                        property="daysUntilDeadline"
                                                                        value="3" >
                                                                        <logic:lessThan name="inbox"
                                                                            property="daysUntilDeadline"
                                                                            value="5" >                                                                      
                                                                            <img src="<bean:write name='ctxtPath'/>/coeusliteimages/yflag.gif" border="0"/>
                                                                        </logic:lessThan>
                                                                        <logic:greaterEqual name="inbox"
                                                                            property="daysUntilDeadline"
                                                                            value="5" >
                                                                            <logic:lessThan name="inbox"
                                                                                property="daysUntilDeadline"
                                                                                value="11" >
                                                                                <img src="<bean:write name='ctxtPath'/>/coeusliteimages/gflag.gif" border="0"/>
                                                                            </logic:lessThan>
                                                                        </logic:greaterEqual>
                                                                    </logic:greaterEqual>
                                                                </logic:greaterEqual>
                                                            </logic:notEqual>
                                                        </td>
                                                        <!-- Modified for Case#4204 -  routing comments
                                                        nowrap is removed to wrap the text in From Column
                                                        -->
                                                        <td width='5%' align=left class='copy'><%String userNameTemp = (String)hmFromUsers.get(inbox.get("fromUser"));%> <%--=inbox.get("fromUser")--%>
													<%--Comment started again<%=userNameTemp%>&nbsp;&nbsp;</td>   

                                                        <%--td class="copy">
                                                            <%=inbox.get("message")%>
                                                        </td>
                                                        <td class='copy'>
                                                            <u> 
                                                                <html:link  action="/displayProposal.do" paramName="inbox" paramProperty="moduleItemKey" paramId="proposalNo"> <%=inbox.get("proposalTitle")%> </html:link>
                                                            </u> 
                                                        </td>
                                                        <td align="justify" class="copy"> 
                                                            <u>
                                                                <html:link  action="/displayProposal.do" paramName="inbox" paramProperty="moduleItemKey" paramId="proposalNo">  <%=inbox.get("moduleItemKey")%> </html:link>
                                                            </u>
                                                        </td--%>
													<!--Modified for Princeton Enhancement Case # 2802 - Start-->
													<%--Comment started again<%
                                                            String message = (String)inbox.get("message");
                                                            String subcontractInvoiceLink = "";
                                                            if(message != null && (message.indexOf("|") != -1) && (Integer.parseInt((String)inbox.get("moduleCode")) == 4 )){
                                                                String seqNum = message.substring(0,message.indexOf("|"));
                                                                message = message.substring(message.indexOf("|")+1);
                                                                String lineNum = message.substring(0,message.indexOf("|"));
                                                                message = message.substring(message.indexOf("|")+1);
                                                                subcontractInvoiceLink = "javaScript:subcontractInvoiceSummary('"+seqNum+"','"+lineNum+"','"+inbox.get("moduleItemKey")+"','"+inbox.get("messageId")+"');";
                                                                }%>
                                                                <%
                                                                //Added for Case#3682 - Enhancements related to Delegations - Start
                                                                String delegationsLink = "";
                                                                if(Integer.parseInt((String)inbox.get("moduleCode")) == 0 ){
                                                                    delegationsLink = "javaScript:delegationDetails('"+inbox.get("moduleItemKey")+"','"+inbox.get("moduleCode")+"');";
                                                                }
                                                                //Added for Case#3682 - Enhancements related to Delegations - End
                                                                %>
                                                        <td class="copy">
                                                            <%=message%>
                                                            <%--=inbox.get("message")--%>
													<%--Comment started again</td>
                                                        <td class='copy'>
                                                            <u> 
                                                                <%
                                                                    String proposalTitle = (String) inbox.get("proposalTitle");                                                                    
                                                                    if (proposalTitle == null ){
                                                                          proposalTitle = "";
                                                                    } 
                                                                    if(Integer.parseInt((String)inbox.get("moduleCode")) == 4){                                                                    
                                                                %>
                                                                    <html:link href="<%=subcontractInvoiceLink%>"><%=proposalTitle%></html:link>
                                                                <%}else if(Integer.parseInt((String)inbox.get("moduleCode")) == 7){%>
                                                                <html:link  action="/displayProtocol.do" paramName="inbox" paramProperty="moduleItemKey" paramId="protocolNumber"> <%=proposalTitle%> </html:link>
                                                                <%}else 
                                                                     //Added for Case#4204 -  routing comments - Start
                                                                     //To check ModuleCode is DevelopmentProposal
                                                                    if(Integer.parseInt((String)inbox.get("moduleCode")) == 3){%>
                                                                    <!-- Added for Case#4204 - End -->
                                                                <html:link  action="/displayProposal.do" paramName="inbox" paramProperty="moduleItemKey" paramId="proposalNo"> <%=proposalTitle%> </html:link>
                                                                <%}
                                                                    //Added for Case#4204 -  routing comments - Start
                                                                    //To display when module code is not equal to 3(Development Proposal) ,4(Award) ,7(Protocol),0 (Delegation)
                                                                    else{%> <%=proposalTitle%>
                                                                    <!-- Added for Case#4204 - End -->
                                                                <%}%>
                                                            </u> 
                                                        </td>
                                                        <td align="justify" class="copy"> 
                                                            <u>
                                                                <%if(Integer.parseInt((String)inbox.get("moduleCode")) == 4){%>
                                                                <html:link href="<%=subcontractInvoiceLink%>"><%=inbox.get("moduleItemKey")%></html:link>
                                                                <%}else if(Integer.parseInt((String)inbox.get("moduleCode")) == 7){%>
                                                                <html:link  action="/displayProtocol.do" paramName="inbox" paramProperty="moduleItemKey" paramId="protocolNumber">  <%=inbox.get("moduleItemKey")%> </html:link>
                                                                <!--Added for Case#3682 - Enhancements related to Delegations - Start -->
                                                                <%}else if(Integer.parseInt((String)inbox.get("moduleCode")) == 0){%>
                                                                <html:link href="<%=delegationsLink%>"><%=inbox.get("moduleItemKey")%></html:link>
                                                                <!--Added for Case#3682 - Enhancements related to Delegations - End -->
                                                                <%}else
                                                                    //Added for Case#4204 -  routing comments - Start
                                                                    if(Integer.parseInt((String)inbox.get("moduleCode")) == 3){
                                                                    //Added for Case#4204 - End
                                                                %>
                                                                <html:link  action="/displayProposal.do" paramName="inbox" paramProperty="moduleItemKey" paramId="proposalNo">  <%=inbox.get("moduleItemKey")%> </html:link>
                                                                <%}
                                                                    //Added for Case#4204 -  routing comments - Start
                                                                    else{%> <%=inbox.get("moduleItemKey")%> 
                                                                    <%}
                                                                   // Added for Case#4204 - End
                                                                %>
                                                            </u>
                                                        </td>                 
                                                        <!--Modified for Princeton Enhancement Case # 2802 - End -->
                                                        <td class="copy">   
                                                            <%=inbox.get("strArrivalDate")%>                                  
                                                        </td>--%>
													<%--Code commented for COEUSQA-2073-Improve management of Coeus Inbox Ends--%>
													<%--COEUSQA-2073-Improve management of Coeus Inbox Starts--%>
													<%
                                                            String subcontractInvoiceLink = "";
                                                            String delegationsLink = "";
                                                            String message = hmInbox.get("MESSAGE").toString();
                                                            String moduleCode = hmInbox.get("MODULE_CODE").toString();
                                                            String moduleItemKey = hmInbox.get("MODULE_ITEM_KEY").toString();
                                                            String messageId = hmInbox.get("MESSAGE_ID").toString();
                                                            //COEUSQA-3066 Lite In-box returns blank screen from Delegation notifications - Start
                                                            //String proposalTitle = hmInbox.get("TITLE").toString();
                                                            String proposalTitle = "";
                                                            if(hmInbox.get("TITLE")!=null){
                                                                proposalTitle = hmInbox.get("TITLE").toString();
                                                            }
                                                            //COEUSQA-3066 Lite In-box returns blank screen from Delegation notifications - End
                                                            if(inboxColumnNames != null && inboxColumnNames.size() > 0) {
                                                                for(int index=0; index < inboxColumnNames.size(); index++) {
                                                                    String value = "";
                                                                    edu.mit.coeus.search.bean.DisplayBean displayBean = (edu.mit.coeus.search.bean.DisplayBean)inboxColumnNames.elementAt(index);
                                                                    if(!displayBean.isVisible())
                                                                        continue;
                                                                    String key = displayBean.getName();
                                                                    if(key != null) {
                                                                        if(key.equals("DEADLINE_DATE")) {%>
													<td class="copy"><logic:notEqual name="inbox"
															property="daysUntilDeadline" value="-1">
															<logic:greaterEqual name="inbox"
																property="daysUntilDeadline" value="0">
																<logic:lessThan name="inbox"
																	property="daysUntilDeadline" value="3">
																	<img
																		src="<bean:write name='ctxtPath'/>/coeusliteimages/rflag.gif"
																		border="0" />
																</logic:lessThan>
																<logic:greaterEqual name="inbox"
																	property="daysUntilDeadline" value="3">
																	<logic:lessThan name="inbox"
																		property="daysUntilDeadline" value="5">
																		<img
																			src="<bean:write name='ctxtPath'/>/coeusliteimages/yflag.gif"
																			border="0" />
																	</logic:lessThan>
																	<logic:greaterEqual name="inbox"
																		property="daysUntilDeadline" value="5">
																		<logic:lessThan name="inbox"
																			property="daysUntilDeadline" value="11">
																			<img
																				src="<bean:write name='ctxtPath'/>/coeusliteimages/gflag.gif"
																				border="0" />
																		</logic:lessThan>
																	</logic:greaterEqual>
																</logic:greaterEqual>
															</logic:greaterEqual>
														</logic:notEqual></td>
													<!--Modified for Princeton Enhancement Case # 2802 - Start-->
													<%} else if(key.equals("MESSAGE")) {
                                                                        if(message != null && (message.indexOf("|") != -1) && (Integer.parseInt(moduleCode) == ModuleConstants.SUBCONTRACTS_MODULE_CODE )){
                                                                        String seqNum = message.substring(0,message.indexOf("|"));
                                                                        message = message.substring(message.indexOf("|")+1);
                                                                        String lineNum = message.substring(0,message.indexOf("|"));
                                                                        message = message.substring(message.indexOf("|")+1);
                                                                        subcontractInvoiceLink = "javaScript:subcontractInvoiceSummary('"+seqNum+"','"+lineNum+"','"+moduleItemKey+"','"+messageId+"');";
                                                                        }
                                                                        //Added for Case#3682 - Enhancements related to Delegations - Start
                                                                        if(Integer.parseInt(moduleCode) == 0 ){
                                                                        delegationsLink = "javaScript:delegationDetails('"+moduleItemKey+"','"+moduleCode+"');";
                                                                        }
                                                                        //Added for Case#3682 - Enhancements related to Delegations - End
                                                                        %>
													<td class="copy"><%=message%></td>
													<%} else if(key.equals("TITLE")) {%>
													<td class='copy'><u> <%
                                                                                if(
                                                                                Integer.parseInt(moduleCode) == ModuleConstants.SUBCONTRACTS_MODULE_CODE){                                                                    
                                                                                %>
															<html:link href="<%=subcontractInvoiceLink%>"><%=proposalTitle%></html:link>
															<%}else if(Integer.parseInt(moduleCode) == ModuleConstants.PROTOCOL_MODULE_CODE){%>
															<html:link action="/displayProtocol.do" paramName="inbox"
																paramProperty="moduleItemKey" paramId="protocolNumber">
																<%=proposalTitle%>
															</html:link> <%}else if(Integer.parseInt(moduleCode) == ModuleConstants.IACUC_MODULE_CODE){%>
															<html:link action="/displayIacuc.do" paramName="inbox"
																paramProperty="moduleItemKey" paramId="protocolNumber">
																<%=proposalTitle%>
															</html:link> <%}else 
                                                                                //Added for Case#4204 -  routing comments - Start
                                                                                //To check ModuleCode is DevelopmentProposal
                                                                                if(Integer.parseInt(moduleCode) == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){%>

															<!-- Added for Case#4204 - End --> <html:link
																action="/displayProposal.do" paramName="inbox"
																paramProperty="moduleItemKey" paramId="proposalNo">
																<%=proposalTitle%>
															</html:link> <%}
                                                                                //Added for Case#4204 -  routing comments - Start
                                                                                //To display when module code is not equal to 3(Development Proposal) ,4(Award) ,7(Protocol),0 (Delegation)
                                                                                else{%>
															<%=proposalTitle%> <!-- Added for Case#4204 - End --> <%}%>
													</u></td>
													<%} else if(key.equals("MODULE_ITEM_KEY")) {%>
													<td align="justify" class="copy"><u> <%if(Integer.parseInt(moduleCode) == ModuleConstants.SUBCONTRACTS_MODULE_CODE){%>
															<html:link href="<%=subcontractInvoiceLink%>"><%=moduleItemKey%></html:link>
															<%}else if(Integer.parseInt(moduleCode) == ModuleConstants.PROTOCOL_MODULE_CODE){%>
															<html:link action="/displayProtocol.do" paramName="inbox"
																paramProperty="moduleItemKey" paramId="protocolNumber">
																<%=moduleItemKey%>
															</html:link> <%}else if(Integer.parseInt(moduleCode) == ModuleConstants.IACUC_MODULE_CODE){%>
															<html:link action="/displayIacuc.do" paramName="inbox"
																paramProperty="moduleItemKey" paramId="protocolNumber">
																<%=moduleItemKey%>
															</html:link> <%}else if(Integer.parseInt(moduleCode) == 0){%> <html:link
																href="<%=delegationsLink%>"><%=moduleItemKey%></html:link>
															<!--Added for Case#3682 - Enhancements related to Delegations - End -->
															<%}else
                                                                                //Added for Case#4204 -  routing comments - Start
                                                                                if(Integer.parseInt(moduleCode) == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
                                                                                //Added for Case#4204 - End
                                                                                %>
															<html:link action="/displayProposal.do" paramName="inbox"
																paramProperty="moduleItemKey" paramId="proposalNo">
																<%=moduleItemKey%>
															</html:link> <%}
                                                                                //Added for Case#4204 -  routing comments - Start
                                                                                else{%>
															<%=moduleItemKey%> <%}
                                                                                // Added for Case#4204 - End
                                                                                %>
													</u></td>
													<!--Modified for Princeton Enhancement Case # 2802 - End -->
													<%} else if(key.equals("ARRIVAL_DATE")) {%>
													<td class="copy"><%=inbox.get("strArrivalDate")%></td>
													<%} else if(key.equals("SYSTEM_DATE")) {%>
													<td class="copy"><%=inbox.get("systemDate")%></td>
													<%} else if(key.equals("UPDATE_TIMESTAMP")) {%>
													<td class="copy"><%=inbox.get("updateTimestamp")%></td>
													<%} else {
                                                                        value = hmInbox.get(key) == null ? "" : hmInbox.get(key).toString();%>
													<td class="copy"><%=value%></td>
													<%}
                                                                    }
                                                                }
                                                            }
                                                            %>
													<%--COEUSQA-2073-Improve management of Coeus Inbox Ends--%>
													<input type="hidden" name='hdnMessageId'
														value='<%=inbox.get("messageId")%>'>
												</tr>
												<% disclIndex++ ;%>
												<% count++; %>

											</logic:iterate>
										</table>
									</DIV>
								</td>
							</tr>
							<tr>
								<td align="center">

									<table table width="95%" border="0" cellpadding="0"
										cellspacing="0" class='theader'>
										<a name="legend"></a>
										<tr>
											<td width="10%" class='copy' align="center"><b> <bean:message
														bundle="coi" key="label.legend" />
											</b></td>
											<td width="20%" class='copy' align="left"><img
												src="<bean:write name='ctxtPath'/>/coeusliteimages/gflag.gif"
												border="0"> <bean:message bundle="coi"
													key="label.deadline10days" /></td>
											<td width="20%" class='copy' align="left"><img
												src="<bean:write name='ctxtPath'/>/coeusliteimages/yflag.gif"
												border="0" /> <bean:message bundle="coi"
													key="label.deadline4days" /></td>
											<td width="20%" class='copy' align="left"><img
												src="<bean:write name='ctxtPath'/>/coeusliteimages/rflag.gif"
												border="0" /> <bean:message bundle="coi"
													key="label.deadline2days" /></td>
										</tr>

									</table>
								</td>
							</tr>

							<tr>
								<td align="left" class="copy"
									style='height: 30px; padding-left: 5px'><html:submit
										property="save" value="Move Selected Messages"
										styleClass="clbutton" /></td>
							</tr>

						</logic:notEqual>


					</table>
				</td>
			</tr>
		</table>

	</html:form>
</body>

</html:html>



