<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@ page import="edu.mit.coeuslite.utils.CoeusLiteConstants"%>
<%@ page import="edu.mit.coeus.utils.CoeusConstants"%>
<jsp:useBean id="amendRenevData" scope="session"
	class="java.util.Vector" />
<html:html>
<head>
<title>Amendments/Renewal</title>
<script
	src="<%=request.getContextPath()%>/coeuslite/mit/utils/scripts/modal/modaldbox.js"></script>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/coeuslite/mit/utils/scripts/modal/modaldbox.css"
	type="text/css" />
<script language="JavaScript">
     function view_summary(value) {
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
                var loc = '<bean:write name='ctxtPath'/>/coeuslite/mit/utils/dialogs/cwViewSpecialReview.jsp?value='+value;
                var newWin = window.open(loc,"formName",'dependent=1,toolbar=0,location=0,directories=0,status=0, menubar=0,scrollbars=0,resizable=0,width=' + w + ',height=' + h+ ',left='+ leftPos + ',top=' + topPos);
                newWin.window.focus();
     }
     
     function modifySummary(type, protoNum) {
       //Modified for COEUSQA-2812 Questionnaire for amendment does not appear initially start.
       
        //var data = "&page="+type+"S&protocolAmendRenNumber="+protoNum+"&present=present";
        var data ="&page="+type+"S&protocolNumber="+protoNum+"&amendRenewHistroy="+'true'+"&PAGE="+'G'+"&SEARCH_ACTION="+'SEARCH_ACTION';
        //document.iacucAmendmentRenewalForm.action = "<%=request.getContextPath()%>/getIacucAmendmentRenewal.do?"+data;
        document.iacucAmendmentRenewalForm.action = "<%=request.getContextPath()%>/getIacucData.do?"+data;
        //Modified for COEUSQA-2812 Questionnaire for amendment does not appear initially end.
        document.iacucAmendmentRenewalForm.submit();      
     }
     
     //Added for COEUSDEV-86 : Show Amendment/Renewal Summary for approved Amendments/Renewals
     function ApproveAmmendmentSummary(type, protoNum,approvedAmendmentSequenceNumber,iacucSummaryCount) {
        var data = "&page="+type+"AS&iacucSummaryCount="+iacucSummaryCount+"&protocolAmendRenNumber="+protoNum+"&present=present&approvedAmendmentSequenceNumber="+approvedAmendmentSequenceNumber;
        document.iacucAmendmentRenewalForm.action = "<%=request.getContextPath()%>/getIacucAmendmentRenewal.do?"+data;         
        document.iacucAmendmentRenewalForm.submit();      
     }
     
     function showErrorMsg(value) {
            alert("<bean:message key="error.AmendRenew.activeEnrollment"/>");
     }
     //Added for COEUSDEV-329 : No Amendment/Renewal Summary box in the Amendment/Renewal History tab in Lite - Start
     function showDialog(divId){
       if(navigator.appName == "Microsoft Internet Explorer") {
            document.getElementById(divId).style.width = 400;
            document.getElementById('inside'+divId).style.width = 400;
            document.getElementById('inside'+divId).style.height = 180;
            document.getElementById('summaryTable').style.width = 300;
            document.getElementById('summaryTextArea').style.width = 393;
        }
        //Default w,h is for 1024 by 768 pixels screen resolution
        var w = 220;
        var h = 350;
        var screenWidth = window.screen.width;
        var screenHeight = window.screen.height;
        if(screenWidth == 800 && screenHeight == 600){
                w = 2;
                h = 175;
        } else if(screenWidth == 1152 && screenHeight == 864){
                w = 350;
                h = 450;
        } else if(screenWidth == 1280 && screenHeight == 720){
                w = 475;
                h = 300;
        }else if(screenWidth == 1280 && screenHeight == 768){
                w = 475;
                h = 350;
        }else if(screenWidth == 1280 && screenHeight == 1024){
                w = 475;
                h = 600;
        }
        
        //widt, height is for pop-up dialog 
        var width =  Math.floor(((screenWidth - w) / 2));
        var height = Math.floor(((screenHeight - h) / 2));
        sm(divId,width,height);
     }
     //Added for COEUSDEV-329 : No Amendment/Renewal Summary box in the Amendment/Renewal History tab in Lite - End
</script>
</head>
<html:form action="/getIacucAmendmentRenewal.do">
	<body>
		<table width='100%' cellpadding='0' cellspacing='0' class='tabtable'>
			<tr>
				<td class='tableheader'><bean:message
						key="label.AmendRenew.AmendRenew" bundle="iacuc" /></td>
			</tr>
			<tr>
				<td height='20'></td>
			</tr>
			<tr>
				<td>
					<table width='98%' cellpadding='0' cellspacing='1' align=center
						class='tabtable'>
						<tr class='theader'>
							<td width="15%"><bean:message key="label.AmendRenew.type" />
							</td>
							<td width="10%"><bean:message
									key="label.AmendRenew.versionNo" /></td>
							<td width="40%">Summary</td>
							<td width="20%"><bean:message key="label.AmendRenew.status" />
							</td>
							<td width="20%"><bean:message
									key="label.AmendRenew.createdDate" /></td>
						</tr>
						<% String strBgColor = "#DCE5F1";
           int count = 0;
           int iacucSummaryCount = 0;%>
						<logic:present name="iacucAmendRenevData">
							<logic:iterate id="data" name="iacucAmendRenevData"
								type="org.apache.struts.validator.DynaValidatorForm">
								<% if (count%2 == 0) 
                    strBgColor = "#D6DCE5"; 
                   else 
                    strBgColor="#DCE5F1";
        %>
								<tr bgcolor="<%=strBgColor%>"
									onmouseover="className='TableItemOn'"
									onmouseout="className='TableItemOff'">
									<td class='copy' width="20%">
										<%  String value =(String) data.get("protocolAmendRenNumber");
                    String status =(String) data.get("status");
                    String createdDate =((String) data.get("createdDate")).substring(0,10);
                    //COEUSDEV-329
                    String summary = ((String) data.get("summary"));
                    String type = value.substring(10,11);
                    // modified code for displaying error msg 
                    String seqNumber =(String)data.get("strSequenceNumber");
                    
                    String viewLink  = "javascript:modifySummary('" +type+"','"+value+"')";
                    // commented code for displaying error msg 
                    /*if(status!=null && (status.equals("Active - Open to Enrollment") || status.equals("Suspended by IRB"))) {
                        viewLink  = "javascript:showErrorMsg('1')";
                    }*/
                    // modified code for displaying error msg 
                    if(!seqNumber.equals(" ") && !seqNumber.equals("0")){
                        ////Added for COEUSDEV-86 : Show Amendment/Renewal Summary for approved
                        viewLink  = "javascript:ApproveAmmendmentSummary('" +type+"','"+value+"','"+seqNumber+"','"+iacucSummaryCount+"')";                        
                    }
                    %> <html:link href="<%=viewLink%>">
											<u> <%if(type!=null && type.equals(Character.toString(CoeusConstants.IACUC_AMENDMENT))) {%>
												<bean:message key="label.AmendRenew.amendment" /> <%} else if(type!=null && type.equals(Character.toString(CoeusConstants.IACUC_RENEWAL))) {%>
												<bean:message key="label.AmendRenew.renewal" /> <%} else if(type!=null && type.equals(Character.toString(CoeusConstants.IACUC_RENEWAL_WITH_AMENDMENT))) {%>
												<bean:message key="label.AmendRenew.AmendRenew"
													bundle="iacuc" /> <%--CoeusQA-1724-Added for new Amendment type-Start--%>
												<%} else if(type!=null && type.equals(Character.toString(CoeusConstants.IACUC_CONTINUATION_CONTINUING_REVIEW))) {%>
												<bean:message key="label.AmendRenew.contRev" bundle="iacuc" />
												<%} else if(type!=null && type.equals(Character.toString(CoeusConstants.IACUC_CONTI_CONTINUING_REVIEW_WITH_AMEND))) {%>
												<bean:message key="label.AmendRenew.contRevAmend"
													bundle="iacuc" /> <%--CoeusQA-1724-Added for new Amendment type-End--%>
												<%}%></u>
										</html:link>
									</td>
									<td class='copy'>
										<%type = value.substring(11);%> <html:link href="<%=viewLink%>">
											<u><%=type%></u>
										</html:link>
									</td>
									<td class='copy' width="30%">
										<!-- Added for COEUSDEV-329 : No Amendment/Renewal Summary box in the Amendment/Renewal History tab in Lite - Start -->
										<%if(summary != null && summary.length()>40){%>
										<div id="summary<%=count%>" class="dialog"
											style="width: 450px; height: 200px; display: none;">
											<div id="insideSummary<%=count%>"
												style="overflow: auto; width: 450px; height: 186px">
												<table cellpadding='0' cellspacing='1' align=left
													class='tabtable' width='400px'>
													<tr class='theader'>
														<td width='100%'><bean:message
																key="label.AmendRenew.summaryTitle" /> <%=value%></td>
													</tr>
												</table>
												<table border="0" id="summaryTable" cellpadding="2"
													cellspacing="0" class="lineBorderWhiteBackgrnd" width="400"
													height="150">
													<tr>
														<td><html:textarea styleId="summaryTextArea"
																property="txtDesc" styleClass="copy" readonly="true"
																value="<%=summary%>" disabled="false" rows="9"
																style="border-top-width:0px; border-right-width:0px; 
                                    border-bottom-width:0px; border-left-width:0px;width:395px" />
														</td>
													</tr>
												</table>

											</div>
											<input type="button" onclick="javascript:hm('summary')"
												value="Close">
										</div> <%}%> <%
                   String showSummary = "javascript:showDialog('summary"+count+"')";%>

										<%if(summary != null && summary.length()>40){
                     summary = summary.substring(0,40);
                   %> <%=summary%><html:link href="<%=showSummary%>">&nbsp;[...]</html:link>
										<%}else{%> <%=summary%> <%}%> <!-- Added for COEUSDEV-329 : No Amendment/Renewal Summary box in the Amendment/Renewal History tab in Lite - End -->

									</td>

									<td class='copy' width="15%"><html:link
											href="<%=viewLink%>">
											<u><bean:write name="data" property="status" /></u>
										</html:link></td>
									<td class='copy' width="20%"><html:link
											href="<%=viewLink%>">

											<u><%=createdDate%></u>
										</html:link></td>
								</tr>
								<%count++;
          iacucSummaryCount++;
        %>
							</logic:iterate>
						</logic:present>
					</table>
				</td>
			</tr>
			<tr>
				<td height='20'></td>
			</tr>
		</table>


	</body>
</html:form>
</html:html>


