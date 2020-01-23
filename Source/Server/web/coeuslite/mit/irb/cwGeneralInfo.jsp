<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="org.apache.struts.taglib.html.JavascriptValidatorTag"%>
<%--start 1--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
	import="edu.mit.coeuslite.utils.ProposalConstants, 
        edu.mit.coeuslite.utils.CoeusLiteConstants,
        edu.mit.coeuslite.utils.SessionConstants,
        java.util.Date,java.text.SimpleDateFormat,
        org.apache.struts.taglib.html.JavascriptValidatorTag"%>
<%--end 1--%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<jsp:useBean id="menuList" scope="request" class="java.util.Vector" />
<jsp:useBean id="ActivityTypes" scope="session" class="java.util.Vector" />
<jsp:useBean id="ProposalTypes" scope="session" class="java.util.Vector" />
<jsp:useBean id="organizations" scope="session" class="java.util.Vector" />
<jsp:useBean id="OrgInfo" scope="request" class="java.util.Vector" />
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>

<%
    String strMenuStatus = "";
    String EMPTYSTRING ="";
    if (request.getAttribute(ProposalConstants.IS_PAGE_SAVED) != null) { 
        strMenuStatus = (String) request.getAttribute(ProposalConstants.IS_PAGE_SAVED);
    }
    //start--2

    String mode=(String)session.getAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId());
    boolean modeValue=false;
    if(mode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE)){
        modeValue=true;
    }else if(mode.equalsIgnoreCase(CoeusLiteConstants.MODIFY_MODE)){
        modeValue=false;
    }else if(mode.equalsIgnoreCase(CoeusLiteConstants.ADD_MODE)){
        modeValue=false;
    }
    //end--2                 
    String newMode = request.getParameter("NEW_PROTOCOL");

    String str = (String) session.getAttribute(SessionConstants.PROPOSAL_TITLE);
    //start--3

    String strProtocolNum = (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
    //end--3
    if(strProtocolNum == null)
        strProtocolNum = "";
    //Added for coeus4.3 Amendments and Renewal enhancement 
    String amendRenewPageMode = (String)session.getAttribute("amendRenewPageMode"+request.getSession().getId());
    if(strProtocolNum.length() > 10 && (amendRenewPageMode == null || 
            amendRenewPageMode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE))){
        modeValue = true;
    }
    //Added for CoeusLite4.3 header changes enhacement - Start
    String newProtocolFlag = (String)session.getAttribute("newProtocolFlag"); 
    String approvalDateFlag = (String)session.getAttribute("approvalDateFlag");
    System.out.println("approvalDateFlag>>>"+approvalDateFlag);
    Date currentDate = new Date();
    String strCurrentDate = EMPTYSTRING;    
    if(currentDate != null){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        strCurrentDate = dateFormat.format(currentDate);
    }
    //Added for CoeusLite4.3 header changes enhacement - end
    String pendingARs = "";
    if (request.getAttribute("manyPendingAmendRenews") != null) { 
        pendingARs = (String) request.getAttribute("manyPendingAmendRenews");
    }
    String amendRenewalProto = "";
    if (request.getAttribute("amendRenewalProto") != null) { 
        amendRenewalProto = (String) request.getAttribute("amendRenewalProto");
    }
    String noRights = "";
    if (request.getAttribute("haveNoCreationRights") != null) { 
        noRights = (String) request.getAttribute("haveNoCreationRights");
    }
%>

<html:html locale="true">

<head>

<title>Research @ Carolina - Protocol Application</title>
<html:base />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<script language="javascript" type="text/JavaScript"
	src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/coeusLiteCalendar.js"></script>
<script language="javaScript" type="text/JavaScript">
    
    /*
    //Commented out because protocol organization section is taken out from general info page -start
    function fetch_Data(result){
        dataChanged();
        document.irbGeneralInfoForm.organizationId.value = result["ORGANIZATION_ID"];
        document.irbGeneralInfoForm.organizationName.value = result["ORGANIZATION_NAME"];
    }

    function organization_search(){
        var winleft = (screen.width - 500) / 2;
        var winUp = (screen.height - 450) / 2;  
        var win = "scrollbars=1,resizable=1,width=650,height=450,left="+winleft+",top="+winUp
        sList = window.open('<%=request.getContextPath()%>/irbSearch.do?type=<bean:message key="searchWindow.title.organization"/>&search=true&searchName=WEBORGANIZATIONSEARCH', "list", win);  
    }

    function add_organization(action){ 
        <%String sCode = (String)request.getAttribute("statusCode");%>
        var value = document.irbGeneralInfoForm.protocolStatusCode.value;
        if(document.irbGeneralInfoForm.organizationName.value == ""){
            alert("Please select an organisation");
        }else{
            document.irbGeneralInfoForm.orgAcType.value = action;
            document.irbGeneralInfoForm.action = "<%=request.getContextPath()%>/generalInfo.do?actionPerformed=organization&statusCode="+value;
            document.irbGeneralInfoForm.submit();
        }
    }
    
    function delete_data(data,orgId,timestamp){
        if (confirm("Are you sure you want to delete the organization?")==true){
            <%String stCode = (String)request.getAttribute("statusCode");%>
            var value = document.irbGeneralInfoForm.protocolStatusCode.value;            
            document.irbGeneralInfoForm.orgAcType.value = data;
            document.irbGeneralInfoForm.organizationId.value = orgId;
            document.irbGeneralInfoForm.locationUpdateTimestamp.value = timestamp;
            document.irbGeneralInfoForm.action = "<%=request.getContextPath()%>/generalInfo.do?actionPerformed=organization&statusCode="+value;
            document.irbGeneralInfoForm.submit();
        }
    }
    //Commented out because protocol organization section is taken out from general info page -end
    */
    var errValue = false;
    var errLock = false;
    function save_generalinfo(){    
        <%String code = (String)request.getAttribute("statusCode");%>
        //var value = document.irbGeneralInfoForm.protocolStatusCode.value;
        var value = '100'; 
        if('<%=code%>' !=null && '<%=code%>'.length>0){
            value = '<%=code%>';
        }
        /*var isBillable = 'N';
        if(document.irbGeneralInfoForm.isBillable.checked == true){
            isBillable = 'Y'
        }
        document.irbGeneralInfoForm.action = "<%=request.getContextPath()%>/generalInfo.do?Type=Add&statusCode="+value+"&isBillable="+isBillable;
        */
        document.irbGeneralInfoForm.action = "<%=request.getContextPath()%>/generalInfo.do?Type=Add&statusCode="+value;
    }

   function validateForm(form) {
       save_generalinfo();
       return validateIrbGeneralInfoForm(form);
   }
   
   //Added for case 4267-Double clicking save results in error
   function disableSaveButton(){
        document.irbGeneralInfoForm.Save.disabled=true;
   }
  
</script>

<script>
    function newAmendmentRenewal(){
    var numOfARs = '<%=pendingARs%>';
    var amendRenew = '<%=amendRenewalProto%>';
    var createRights = '<%=noRights%>';
        if(createRights == 'noRights'){
            if(amendRenew == 'NA'){
                alert("You do not have rights to create an Amendment");
                document.irbGeneralInfoForm.action = "<%=request.getContextPath()%>/getProtocolData.do?PAGE=G";
                document.irbGeneralInfoForm.submit();
            }
            //else if(amendRenew == 'NR'){
            //Added for the case#4330-Use consistent name for Renewal with Amendment  - start
            else if(amendRenew == 'NR' || amendRenew == 'RA'){
                alert("You do not have rights to create an Renewal ");
                document.irbGeneralInfoForm.action = "<%=request.getContextPath()%>/getProtocolData.do?PAGE=G";
                document.irbGeneralInfoForm.submit();
            }    
        }else if(createRights == 'hasRights'){
                if(numOfARs == 'morePending'){
                    if(confirm("There are pending Amendments/Renewals for this protocol  Do you want to create new Amendment/Renewal?") == true){
                        if(amendRenew == 'NA'){
                                document.irbGeneralInfoForm.action = "<%=request.getContextPath()%>/createNewAmendment.do?PAGE=NA";
                                document.irbGeneralInfoForm.submit();
                        }
                        if(amendRenew == 'NR'){
                            document.irbGeneralInfoForm.action = "<%=request.getContextPath()%>/createNewRenewal.do?PAGE=NR";
                            document.irbGeneralInfoForm.submit();
                        }
                        //Added for the case#4330-Use consistent name for Renewal with Amendment  - start
                        if(amendRenew == 'RA'){
                            document.irbGeneralInfoForm.action = "<%=request.getContextPath()%>/createNewRenewal.do?PAGE=RA";
                            document.irbGeneralInfoForm.submit();
                        }
                    }else{
                            document.irbGeneralInfoForm.action = "<%=request.getContextPath()%>/getProtocolData.do?PAGE=G";
                            document.irbGeneralInfoForm.submit();
                    }
                }
                if(numOfARs == 'noPending'){
                        if(amendRenew == 'NA'){
                                document.irbGeneralInfoForm.action = "<%=request.getContextPath()%>/createNewAmendment.do?PAGE=NA";
                                document.irbGeneralInfoForm.submit();
                        }
                       if(amendRenew == 'NR'){
                            document.irbGeneralInfoForm.action = "<%=request.getContextPath()%>/createNewRenewal.do?PAGE=NR";
                            document.irbGeneralInfoForm.submit();
                        }
                       //Added for the case#4330-Use consistent name for Renewal with Amendment  - start
                        if(amendRenew == 'RA'){
                            document.irbGeneralInfoForm.action = "<%=request.getContextPath()%>/createNewRenewal.do?PAGE=RA";
                            document.irbGeneralInfoForm.submit();
                        }
                }
        }
    }
</script>

<html:javascript formName="irbGeneralInfoForm" dynamicJavascript="true"
	staticJavascript="true" />
</head>
<body>
	<% 

    //start--4
    String protocolNo = (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
    //end--4
    if(protocolNo!= null && protocolNo!= ""){ 
         protocolNo = "["+"Protocol No. - "+ protocolNo+"]";
    }else{
        protocolNo = "";
    }
%>


	<html:form action="/generalInfo.do" method="post"
		focus="protocolTypeCode" onsubmit="disableSaveButton()">

		<!-- New Template for cwGeneralInfo - Start   -->
		<script type="text/javascript">
    document.getElementById('txt').innerHTML = '<bean:message key="helpPageTextProtocol.ProtocolSummary"/>';
</script>
		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			class="table">
			<script>newAmendmentRenewal()</script>
			<tr>
				<td height="20" align="left" valign="top" class="theader"><bean:message
						key="generalInfoLabel.GenProtoInfo" /></td>
			</tr>

			<tr>
				<td class="copybold">&nbsp;&nbsp;<font color="red">*</font> <bean:message
						key="label.indicatesReqFields" />
				</td>
			</tr>
			<tr>
				<td>
					<div id="helpText" class='helptext'>
						<bean:message key="helpTextProtocol.ProtocolSummary" />
					</div>
				</td>
			</tr>

			<tr class='copy' align="left">
				<td><font color="red"> <logic:messagesPresent>
							<html:errors header="" footer="" />
							<script>errValue = true;</script>
						</logic:messagesPresent> <logic:messagesPresent message="true">
							<script>errValue = true;</script>
							<html:messages id="message" message="true">
								<!-- If lock is deleted then show this message -->
								<html:messages id="message" message="true" property="errMsg">
									<script>errLock = true;</script>
									<li><bean:write name="message" /></li>
								</html:messages>
								<!-- If lock is acquired by another user, then show this message -->
								<html:messages id="message" message="true" property="acqLock">
									<script>errLock = true;</script>
									<li><bean:write name="message" /></li>
								</html:messages>
							</html:messages>
						</logic:messagesPresent>

				</font></td>
			</tr>
			<%-- COEUSQA-1433 - Allow Recall from Routing - Start --%>
			<tr>
				<td class='copy' align="left" colspan='2'><font color='red'>
						<%if(session.getAttribute("routingLockMessage") != null){%> <%=(String)session.getAttribute("routingLockMessage")%>
						<%session.removeAttribute("routingLockMessage");%> <%}%>
				</font></td>
			</tr>
			<%-- COEUSQA-1433 - Allow Recall from Routing - End --%>
			<!-- General Protocol Information - Start  -->
			<tr>
				<td height="119" align="left" valign="top" class='core'><table
						width="100%" border="0" align="center" cellpadding="0"
						cellspacing="0" class="tabtable">
						<tr>
							<td colspan="4" align="left" valign="top"><table
									width="100%" height="20" border="0" cellpadding="0"
									cellspacing="0" class="tableheader">
									<tr>
										<td><bean:message key="generalInfoLabel.DetailProtoInfo" />
										</td>
										<td align="right"><a id="helpPageText"
											href="javascript:showBalloon('helpPageText', 'helpText')">
												<bean:message key="helpPageTextProtocol.help" />
										</a></td>
									</tr>
								</table></td>
						</tr>
						<tr>
							<td>
								<table cellpadding='2'>
									<tr>
										<td width="1%" nowrap class="copybold"><font color="red">*</font>
										<bean:message key="generalInfoLabel.Type" />:</td>
										<td width="100%" class="copy"><html:select
												name="irbGeneralInfoForm" property="protocolTypeCode"
												styleClass="textbox-long" onchange="dataChanged()"
												disabled="<%=modeValue%>">
												<html:option value="">
													<bean:message key="generalInfoLabel.pleaseSelect" />
												</html:option>
												<html:options collection="ProtocolTypes" property="code"
													labelProperty="description" />
											</html:select></td>
									</tr>
									<%--
                        <tr>
                          <td width="1%" nowrap class="copybold">
                            <bean:message key="generalInfoLabel.Status"/>:
                          </td>
                          <td width="100%" class="copy">
                         
                            
                            <html:select  name="irbGeneralInfoForm" property="protocolStatusCode" styleClass="textbox-long" onchange="dataChanged()" disabled="true"> 
                                <html:options collection="ProtocolStatusTypes" property="code" labelProperty="description"/>
                            </html:select>
                         </td>
                        </tr>
                       --%>

									<%--
Commnetd by Geo. BT Request #2541
 --%>
									<html:hidden name="irbGeneralInfoForm" property="isBillable" />
									<%--
                        <tr>
                          <td width="1%" nowrap class="copybold"> 
                            <bean:message key="generalInfoLabel.Billable"/>:
                          </td>
                          <td width="100%" class="copy">
                            <html:checkbox name="irbGeneralInfoForm" property="isBillable" value="Y"  disabled="<%=modeValue%>"/>
                            
                          </td>
                        </tr>
--%>
									<tr>
										<td valign=top width="1%" nowrap class="copybold"><font
											color="red">*</font>
										<bean:message key="generalInfoLabel.Title" />:</td>
										<td width="100%" class="copy">
											<%--div class='copyitalics'><bean:message key="generalInfoLabel.titleLimit"/></div--%>
											<html:textarea name="irbGeneralInfoForm" onblur=""
												property="title" styleClass="textbox-longer" cols="80"
												readonly="<%=modeValue%>" onchange="dataChanged()" />
										</td>
									</tr>
									<tr>
										<td width="1%" nowrap class="copybold"><bean:message
												key="generalInfoLabel.Description" />:</td>
										<td width="100%" class="copy">
											<%--div class='copyitalics'><bean:message key="generalInfoLabel.descriptionLimit"/></div--%>
											<html:textarea name="irbGeneralInfoForm"
												property="description" styleClass="textbox-longer" cols="80"
												readonly="<%=modeValue%>" onchange="dataChanged()" /> <script>
                                if(navigator.appName == "Microsoft Internet Explorer")
                                {
                                    document.irbGeneralInfoForm.title.cols=86;
                                    document.irbGeneralInfoForm.title.rows=3;
                                    document.irbGeneralInfoForm.description.cols=86;
                                    document.irbGeneralInfoForm.description.rows=3;
                                }
                           </script>

										</td>
									</tr>
									<tr>
										<td width="1%" nowrap class="copybold"><font color="red">*</font>
										<bean:message key="generalInfoLabel.ApplDate" />:</td>
										<td width="100%" valign="top" nowrap class="copy">
											<!--  <input type="text" id="applicationDate" name="irbGeneralInfoForm" maxlength="10" size="10" value="" readonly="readonly" class="textbox"> -->

											<table width="100%" cellpadding='0' cellspacing='0'>
												<tr>
													<td width='48%' colspan='2'>
														<%if(!modeValue){%> <%if(newProtocolFlag != null){%> <html:text
															name="irbGeneralInfoForm" property="applicationDate"
															styleClass="textbox" size="10" maxlength="10"
															onchange="dataChanged()" value="<%=strCurrentDate%>" /> <%}else{%>
														<html:text name="irbGeneralInfoForm"
															property="applicationDate" styleClass="textbox" size="10"
															maxlength="10" onchange="dataChanged()" /> <%}%> &nbsp; <html:link
															href="javascript:void(0)"
															onclick="displayCalendarWithTopLeft('applicationDate',-7,45)"
															tabindex="32">
															<img id="imgIRBDate" title="" height="16" alt=""
																src="<bean:write name='ctxtPath'/>/coeusliteimages/cal.gif"
																width="16" onclick='dataChanged()' border="0"
																runat="server">
														</html:link> &nbsp; <%}else{%> <html:text name="irbGeneralInfoForm"
															property="applicationDate" styleClass="textbox" size="10"
															maxlength="10" readonly="true" onchange="dataChanged()" />
														<%}%>
													</td>

													<%if(approvalDateFlag == null && newProtocolFlag == null){%>
													<td width='21%' align=right class='copybold'><bean:message
															key="generalInfoLabel.ApprovalDate" />:</td>
													<td width='30%' align=center><html:text
															name="irbGeneralInfoForm" property="approvalDate"
															styleClass="cltextbox-nonEditcolor" style="width:120;"
															size="10" maxlength="10" readonly="true"
															onchange="dataChanged()" /></td>
													<%}%>
												</tr>
											</table>
										</td>
									</tr>
									<%--
                        <tr>
                             <td width="1%" nowrap class="copybold">
                                
                                <bean:message key="generalInfoLabel.LastApprovalDate"/>:
                            </td>
                            <td width='100%' valign='top' nowrap class='copy'>                                
                                <table width='100%' cellpadding='0' cellspacing='0' >
                                    <tr>
                                        <td width='48%' colspan='2'>
                                            <html:text name="irbGeneralInfoForm" property="lastApprovalDate" styleClass="textbox" size="10" maxlength="10" readonly="true" onchange="dataChanged()"/> 
                                        </td>
                                         <td width="21%" align=right nowrap class="copybold">
                                            <bean:message key="generalInfoLabel.ExpirationDate"/>:
                                        </td>
                                        <td width='30%' align=center> 
                                            <html:text name="irbGeneralInfoForm" property="expirationDate" styleClass="textbox" size="10" maxlength="10" readonly="true" onchange="dataChanged()"/> 
                                        </td> 
                                    </tr>
                                </table>     
                            </td> 
                        </tr> 
                        --%>
									<tr>
										<td width="1%" nowrap class="copybold"><bean:message
												key="generalInfoLabel.ReferenceNum1" />:</td>
										<td width="100%" class="copy">
											<table width='100%' cellpadding='0' cellspacing='0'>
												<tr>
													<td width='48%' colspan='2'><html:text
															name="irbGeneralInfoForm" property="refNum_1" size="58"
															styleClass="textbox" disabled="<%=modeValue%>"
															maxlength="50" onchange="dataChanged()" /></td>

													<td width="21%" align=right nowrap class="copybold"><bean:message
															key="generalInfoLabel.ReferenceNum2" />:</td>
													<td width='30%' align=center class="copy"><html:text
															name="irbGeneralInfoForm" property="refNum_2" size="58"
															styleClass="textbox" disabled="<%=modeValue%>"
															maxlength="50" onchange="dataChanged()" /></td>
												</tr>
											</table>
										</td>
									</tr>
									<tr>
										<td width="1%" nowrap class="copybold"><bean:message
												key="generalInfoLabel.FDAApplNo" />:</td>
										<td width="100%" class="copy"><html:text
												name="irbGeneralInfoForm" property="FDAApplicationNumber"
												styleClass="textbox" disabled="<%=modeValue%>"
												maxlength="15" onchange="dataChanged()" /></td>
									</tr>

									<html:hidden name="irbGeneralInfoForm"
										property="createTimestamp" />
									<html:hidden name="irbGeneralInfoForm" property="createUser" />
									<html:hidden name="irbGeneralInfoForm" property="updateUser" />
									<html:hidden name="irbGeneralInfoForm"
										property="updateTimestamp" />



								</table>
							</td>
						</tr>




					</table></td>
			</tr>
			<!-- General Protocol Information - End  -->
			<tr>
				<%if(!modeValue){%>
				<td colspan="2" class='savebutton'><html:submit
						property="Save" value="Save" styleClass="clsavebutton"
						onclick="save_generalinfo();" /></td>
				<%}%>
			</tr>



			<%--
  Commented out because protocol organization section is taken out from general info page - start
  <!-- Protocol Organizations:  - Start -->  
  <tr>
      <td height="52" align="left" valign="top"><table width="99%"  border="0" align="center" cellpadding="0" cellspacing="0" class="tabtable">
          <tr>
            <td colspan="4" align="left" valign="top"><table width="100%" height="20"  border="0" cellpadding="0" cellspacing="0" class="tableheader">
                <tr>
                  <td> <bean:message key="generalInfoLabel.ProtOrg"/>:  </td>
                </tr>
            </table></td>
          </tr> 
                 <tr>
                      <td width="1%" nowrap class="copybold">
                        <bean:message key="generalInfoLabel.OrgType"/>:
                      </td>
                      <td class="copy">
                        <html:select  property="protocolOrgTypeCode" styleClass="textbox-long" disabled="<%=modeValue%>"> 
                            <html:options collection="OrganizationTypes" property="code" labelProperty="description"/>
                        </html:select>
                      </td> 
                 </tr>
                  <tr>
                   <td>
                   </td>
                  </tr>
                <tr>
                      <td nowrap class="copybold"><bean:message key="generalInfoLabel.OrgName"/>: </td>
                      <td valign="top" nowrap class="copysmall">
                        <html:text name="irbGeneralInfoForm" property="organizationName"  styleClass="textbox-longer" maxlength="6" readonly="true" size="48" />
                       
                        <%if(!modeValue){%>
                        

                            <html:link href="javascript:organization_search();">
                                <bean:message key="label.search"/>
                            </html:link>
                        
                        <%}%>
                       
                      </td>
                  </tr> 
                                
                 <tr>
                    <%
                    //start--5    
                    if((String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId()) != null){
                    //end--5

                     %>
                     <%if(!modeValue){%>
                      <td colspan="2" align="center">
                        <br>                                  
                              <html:button property="Save Organization" value="Save" styleClass="clbutton" onclick="add_organization('U');"/>
                        <br>
                     </td>
                     <%}%>
                     <%}%>
                 </tr>  
      
          
                 <tr>
                      <td>
                          &nbsp;
                      </td>
                 </tr>                       

    </table></td>
  </tr> 
  <!-- Protocol Organizations:  -End -->   
  
   <!--List Of Organisation -Start --> 
   
                    <%
                            if (organizations.size() > 0) 
                            { %> 
  <tr>
        <td height="119" align="left" valign="top"><br>
            <table width="99%"  border="0" align="center" cellpadding="0" cellspacing="0" class="tabtable">
                        <tr>
                          <td align="left" valign="top"><table width="100%" height="20"  border="0" cellpadding="0" cellspacing="0" class="tableheader">
                              <tr>
                                <td>  <bean:message key="generalInfoLabel.LstOfOrg"/>:</td>
                              </tr>
                          </table></td>
                        </tr>
                        
                         <tr align="center">
                                        <td colspan="3"><br>
                                          <table width="95%"  border="0" cellpadding="0" class="tabtable">
                                            <tr>
                                              <td width="30%" align="left" class="theader"><bean:message key="generalInfoLabel.OrgType"/></td>
                                              <td width="15%" align="left" class="theader"><bean:message key="generalInfoLabel.OrgId"/></td>
                                              <td width="30%" align="left" class="theader"><bean:message key="generalInfoLabel.OrgName"/></td>
                                              <%if(!modeValue){%>
                                              <td width="10%" align="left" class="theader">&nbsp;</td>
                                               <%}%>
                                              
                                            </tr>
                                        <% String strBgColor = "#DCE5F1"; //BGCOLOR=EFEFEF  FBF7F7
                                          int count = 0;
                                        %>
                                        <logic:present name="organizations">
                                          <logic:iterate id="organizationBean" name="organizations" type="org.apache.struts.validator.DynaValidatorForm">
                                            <% 
                                                if (count%2 == 0) 
                                                  strBgColor = "#D6DCE5"; 
                                                else 
                                                  strBgColor="#DCE5F1"; 
                                              %>
                                            <tr bgcolor="<%=strBgColor%>" valign="top" onmouseover="className='TableItemOn'"  onmouseout="className='TableItemOff'">
                                              <td align="left" nowrap class="copy">
                                                <%=organizationBean.get("organizationTypeName")%>
                                              </td>
                                              <td align="left" nowrap class="copy">
                                                <%=organizationBean.get("organizationId")%>
                                              </td>
                                              <td align="left" nowrap class="copy">
                                                <%=organizationBean.get("organizationName")%>
                                              </td>
                                              <td align="center" nowrap class="copy">
                                                                   
                                                 
                                                   <%if(!modeValue){
                                                       String removeLink = "javascript:delete_data('D','" +organizationBean.get("organizationId") +"','" +organizationBean.get("locationUpdateTimestamp") +"')";
                                                   %>
                                                   
                                                        

                                                        
                                                        <html:link href="<%=removeLink%>">
                                                            <bean:message key="fundingSrc.Remove"/>
                                                        </html:link>
                                                        
                                                        
                                                   <%}%>
                                               
                                              </td> 
                                            </tr>
                                            <% count++;%>
                                          </logic:iterate>
                                        </logic:present>
                                        </table>
                                        </td>
                                       </tr>
                                      <% }%>                 



                        <tr>
                              <td height='10'>
                                  &nbsp;
                              </td>
                          </tr>     
                          
    </table>
  </td>
</tr>   
    <!--List Of Organisation -End -->
    Commented out because protocol organization section is taken out from general info page - end

--%>
		</table>


		<!-- New Template for cwGeneralInfo - End  -->
		<html:hidden name="irbGeneralInfoForm" property="organizationId" />
		<html:hidden property="acType" />
		<html:hidden property="protocolNumber" />
		<html:hidden property="sequenceNumber" />
		<html:hidden property="locationUpdateTimestamp" />
		<html:hidden property="orgAcType" />

		<html:hidden property="relatedProjectIndicator" />
		<html:hidden property="referenceIndicator" />
		<html:hidden property="correspondantIndicator" />

		<html:hidden property="fundingSourceIndicator" />
		<html:hidden property="keyStudyPersonIndicator" />
		<html:hidden property="vulnerableSubjectIndicator" />
		<html:hidden property="specialReviewIndicator" />
	</html:form>

	<script>
      DATA_CHANGED = 'false';
      if(errValue && !errLock) {
        DATA_CHANGED = 'true';
      }
      //var value = document.irbGeneralInfoForm.protocolStatusCode.value;
      var value = '100';
      LINK = "<%=request.getContextPath()%>/generalInfo.do?Type=Add&statusCode="+value;
      FORM_LINK = document.irbGeneralInfoForm;
      PAGE_NAME = "<bean:message key="generalInfoLabel.GenProtoInfo"/> ";
      function dataChanged(){
        DATA_CHANGED = 'true';
      }
      linkForward(errValue);
</script>
	<script>
      var help = '<bean:message key="helpTextProtocol.ProtocolSummary"/>';
      help = trim(help);
      if(help.length == 0) {
            document.getElementById('helpText').style.display = 'none';
      }
      function trim(s) {
            return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
      }  
</script>
</body>
</html:html>

