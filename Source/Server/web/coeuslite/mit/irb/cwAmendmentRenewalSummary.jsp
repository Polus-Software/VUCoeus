<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@ page
	import="org.apache.struts.validator.DynaValidatorForm, java.util.Vector"%>
<%@ page import="edu.mit.coeuslite.utils.CoeusLiteConstants"%>
<jsp:useBean id="amendRenevData" scope="request"
	class="java.util.Vector" />
<jsp:useBean id="enabledQuestionnaires" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="checkedQuestionnaires" scope="session"
	class="java.util.Vector" />
<html:html>
<head>
<title>Amendments/Renewal Summary</title>
<%
        String modeVal = (String)session.getAttribute("newAmendRenew");// this is for setting mode value
        modeVal = modeVal!=null ? modeVal : "";
        //COEUSQA-2602-Remove checkboxes from Renewal Summary screen
        String renewalProtocolInRenewalSummary = (String)request.getAttribute("renewalProtocol");
        renewalProtocolInRenewalSummary = renewalProtocolInRenewalSummary!=null ? renewalProtocolInRenewalSummary : "";
%>
<script language="JavaScript">
var errValue = false;
var errLock = false;
function pageSave(amendRenewVal) {
    if('<%=modeVal%>'.length > 0){
        document.amendmentRenewalForm.action = "<%=request.getContextPath()%>/saveNewAmendmentRenewal.do?saveNewAR="+amendRenewVal;
        document.amendmentRenewalForm.submit();
    }else{
        document.amendmentRenewalForm.action = "<%=request.getContextPath()%>/saveAmendmentRenewal.do?operation=S";
        document.amendmentRenewalForm.submit();
    }
    
}
function selectCheckBox(component, modeValue){
    if(modeValue == 'Y'){
        component.checked = true;   
    }
}
var obj = new Array();
var index = 0;

function setForm(formData){
   /* alert(formData);
    for(count=0 ; count < index ; count++){
        var inputs = formData.getElementsByTagName("input");   
        for(var t = 0;t < inputs.length;t++){
            if(inputs[t].type == "checkbox" && inputs[t].name == obj[count]){
                inputs[t].checked = true;
                break;
            }
        }
    }*/
    for(count=0 ; count < index ; count++){
        var inputs = document.getElementsByName(obj[count]);
        inputs[0].checked = true;
    }
}
</script>
</head>
<html:form action="/getAmendmentRenewal.do" focus="summary">
	<body onload="setForm(this)">
		<%  String summary =(String) session.getAttribute("summary");
    Vector vecData =(Vector) session.getAttribute("amendRenevData");
    String EMPTY_STRING = "";
    String summaryData = EMPTY_STRING;
    edu.mit.coeuslite.utils.DateUtils dateUtils = new edu.mit.coeuslite.utils.DateUtils();
    String createdDate = EMPTY_STRING;
    String updateTimestamp = EMPTY_STRING;
    String protocolNumber = EMPTY_STRING;
    String sequenceNumber = EMPTY_STRING;
    DynaValidatorForm dynaFormModule = (DynaValidatorForm) session.getAttribute("amendRenewModulesSummary");
    String updateUser = EMPTY_STRING;
    DynaValidatorForm dynaForm = null;
    if(vecData!=null && vecData.size()>0) {
        dynaForm =(DynaValidatorForm) vecData.get(0);
        summaryData = (String) dynaForm.get("summary");
        createdDate = (String) dynaForm.get("createdDate");
        if(createdDate!=null && !createdDate.equals(EMPTY_STRING)) {
            createdDate = dateUtils.formatDate(createdDate,"MM/dd/yyyy");
        }
        updateTimestamp = (String) dynaForm.get("updateTimestamp");
        protocolNumber = (String) dynaForm.get("protocolNumber");
        if(dynaForm.get("sequenceNumber")!=null) {
            sequenceNumber = dynaForm.get("sequenceNumber").toString();
        }
        updateUser = (String) dynaForm.get("updateUser");
    }
      String mode=(String)session.getAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId());
        boolean modeValue=false;
        if(mode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE)){
           // if((!modeVal.equals(""))&&(modeVal.equals("NA") || modeVal.equals("NR"))){
            //Added for case#4330 - Use consistent name for Renewal with Amendment -start
            if((!modeVal.equals(""))&&(modeVal.equals("NA") || modeVal.equals("NR") ||  modeVal.equals("RA"))){
            //Added for case#4330 - Use consistent name for Renewal with Amendment -end
             modeValue = false;   
            }else{
            modeValue=true;
            }
        }else if(mode.equalsIgnoreCase(CoeusLiteConstants.MODIFY_MODE)){
            modeValue=false;
        }else if(mode.equalsIgnoreCase(CoeusLiteConstants.ADD_MODE)){
            modeValue=false;
        }
        String moduleValue = EMPTY_STRING;%>

		<table width='100%' cellpadding='0' cellspacing='0' class='tabtable'>
			<tr>
				<td class='tableheader'><%=summary%> <bean:message
						key="label.AmendRenew.summary" /></td>
			</tr>
			<tr>
				<td class='copybold'><font color='red'> <logic:messagesPresent>
							<script>errValue = true;</script>
							<html:errors header="" footer="" />
						</logic:messagesPresent> <logic:messagesPresent message="true">
							<html:messages id="message" message="true"
								property="alreadyLocked">
								<li><bean:write name="message" /></li>
							</html:messages>
							<html:messages id="message" message="true" property="errMsg">
								<script>errLock = true;</script>
								<li><bean:write name="message" /></li>
							</html:messages>
							<html:messages id="message" message="true" property="acqLock">
								<script>errLock = true;</script>
								<li><bean:write name="message" /></li>
							</html:messages>
						</logic:messagesPresent>
				</font></td>
			</tr>
			<tr>
				<td>
					<table width="99%" height="100%" align=center border="0"
						cellpadding="1" cellspacing="1">
						<tr>
							<td>
								<%--html:textarea name="amendmentRenewalForm" property="summary" value="<%=summaryData%>" disabled="<%=modeValue%>" styleClass="copy" cols="150" rows="25" onchange="dataChanged()"/--%>
								<html:textarea name="amendmentRenewalForm" property="summary"
									value="<%=summaryData%>" disabled="<%=modeValue%>"
									styleClass="copy" cols="145" rows="15" onchange="dataChanged()" />
								<script>
                        if(navigator.appName == "Microsoft Internet Explorer")
                        {
                            document.amendmentRenewalForm.summary.cols=145;
                            document.amendmentRenewalForm.summary.rows=15;
                        }                    
                </script>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td height="5"></td>
			</tr>
			<%if(dynaFormModule!=null){%>
			<%--COEUSQA-2602:Remove checkboxes from Renewal Summary screen - start --%>
			<%if(!modeVal.equalsIgnoreCase("NR") && !renewalProtocolInRenewalSummary.equalsIgnoreCase("Y")){%>
			<tr>
				<td>
					<table width="99%" align="center" cellpadding="0" cellspacing="0"
						border="0">
						<tr>
							<td height="30" width="2%" valign="middle">
								<%moduleValue = (String) dynaFormModule.get("generalInfo");
                          moduleValue = (moduleValue==null) ? EMPTY_STRING : moduleValue;
                          if(moduleValue.equals("N")){%> <html:checkbox
									property="generalInfo" style="copy" disabled="true"
									onclick="dataChanged()" /> <%} else {%> <html:checkbox
									property="generalInfo" style="copy" disabled="<%=modeValue%>"
									value='Y' onclick="dataChanged()" /> <%}%> <script>
                              //selectCheckBox('generalInfo', '<%=moduleValue%>');
                              if('<%=moduleValue%>' == 'Y'){
                                obj[index++] = 'generalInfo';
                              }
                          </script>
							</td>
							<td width="31%" valign="middle" class="copybold"><bean:message
									key="mainPage.subHeader1" /></td>
							<td width="2%" valign="middle">
								<%moduleValue = (String) dynaFormModule.get("organization");
                          moduleValue = (moduleValue==null) ? EMPTY_STRING : moduleValue;
                          if(moduleValue.equals("N")){%> <html:checkbox
									property="organization" disabled="true" onclick="dataChanged()" />
								<%} else {%> <html:checkbox property='organization' value="Y"
									disabled="<%=modeValue%>" onclick="dataChanged()" /> <%}%> <script>
                              //selectCheckBox('organization', '<%=moduleValue%>');
                              if('<%=moduleValue%>' == 'Y'){
                                obj[index++] = 'organization';
                              }                              
                          </script>
							</td>
							<td width="31%" valign="middle" class="copybold"><bean:message
									key="protocolOrganization.label.organization" /></td>
							<td width="2%" valign="middle">
								<%moduleValue = (String) dynaFormModule.get("investigatorsStudyPersons");
                          moduleValue = (moduleValue==null) ? EMPTY_STRING : moduleValue;
                          if(moduleValue.equals("N")){%> <html:checkbox
									property="investigatorsStudyPersons" style="copy"
									disabled="true" onclick="dataChanged()" /> <%} else {%> <html:checkbox
									property="investigatorsStudyPersons" style="copy"
									disabled="<%=modeValue%>" value='Y' onclick="dataChanged()" />
								<%}%> <script>
                              //selectCheckBox('investigatorsStudyPersons', '<%=moduleValue%>');
                              if('<%=moduleValue%>' == 'Y'){
                                obj[index++] = 'investigatorsStudyPersons';
                              }                              
                          </script>
							</td>
							<td width="32%" valign="middle" class="copybold"><bean:message
									key="keyStudyPerLabel.KeyStudyPer" /></td>
						</tr>
						<tr>
							<td height="30" width="2%" valign="middle">
								<%moduleValue = (String) dynaFormModule.get("correspondents");
                          moduleValue = (moduleValue==null) ? EMPTY_STRING : moduleValue;
                          if(moduleValue.equals("N")){%> <html:checkbox
									property="correspondents" style="copy" disabled="true"
									onclick="dataChanged()" /> <%} else {%> <html:checkbox
									property="correspondents" style="copy"
									disabled="<%=modeValue%>" value='Y' onclick="dataChanged()" />
								<%}%> <script>
                              //selectCheckBox('correspondents', '<%=moduleValue%>');
                              if('<%=moduleValue%>' == 'Y'){
                                obj[index++] = 'correspondents';
                              }                              
                          </script>
							</td>
							<td width="31%" valign="middle" class="copybold"><bean:message
									key="correspondents.correspondents" /></td>
							<td width="2%" valign="middle">
								<%moduleValue = (String) dynaFormModule.get("areasOfResearch");
                          moduleValue = (moduleValue==null) ? EMPTY_STRING : moduleValue;
                          if(moduleValue.equals("N")){%> <html:checkbox
									property="areasOfResearch" style="copy" disabled="true"
									onclick="dataChanged()" /> <%} else {%> <html:checkbox
									property="areasOfResearch" style="copy"
									disabled="<%=modeValue%>" value='Y' onclick="dataChanged()" />
								<%}%> <script>
                              //selectCheckBox('areasOfResearch', '<%=moduleValue%>');
                              if('<%=moduleValue%>' == 'Y'){
                                obj[index++] = 'areasOfResearch';
                              }                              
                          </script>
							</td>
							<td width="31%" valign="middle" class="copybold"><bean:message
									key="amendmentRenewal.areaOfResearch" /></td>
							<td width="2%" valign="middle">
								<%moduleValue = (String) dynaFormModule.get("fundingSource");
                          moduleValue = (moduleValue==null) ? EMPTY_STRING : moduleValue;
                          if(moduleValue.equals("N")){%> <html:checkbox
									property="fundingSource" style="copy" disabled="true"
									onclick="dataChanged()" /> <%} else {%> <html:checkbox
									property="fundingSource" style="copy" disabled="<%=modeValue%>"
									value='Y' onclick="dataChanged()" /> <%}%> <script>
                              //selectCheckBox('subjects', '<%=moduleValue%>');
                              if('<%=moduleValue%>' == 'Y'){
                                obj[index++] = 'fundingSource';
                              }                              
                          </script>
							</td>
							<td width="32%" valign="middle" class="copybold"><bean:message
									key="fundingSrc.FundingSources" /></td>
						</tr>
						<tr>
							<td width="2%" valign="middle">
								<%moduleValue = (String) dynaFormModule.get("subjects");
                          moduleValue = (moduleValue==null) ? EMPTY_STRING : moduleValue;
                          if(moduleValue.equals("N")){%> <html:checkbox
									property="subjects" style="copy" disabled="true"
									onclick="dataChanged()" /> <%} else {%> <html:checkbox
									property="subjects" style="copy" disabled="<%=modeValue%>"
									value='Y' onclick="dataChanged()" /> <%}%> <script>
                              //selectCheckBox('subjects', '<%=moduleValue%>');
                              if('<%=moduleValue%>' == 'Y'){
                                obj[index++] = 'subjects';
                              }                              
                          </script>
							</td>
							<td width="32%" valign="middle" class="copybold"><bean:message
									key="vulnSubLabel.VulnerableSubjects" /></td>
							<td height="30" width="2%" valign="middle">
								<%moduleValue = (String) dynaFormModule.get("specialReview");
                          moduleValue = (moduleValue==null) ? EMPTY_STRING : moduleValue;
                          if(moduleValue.equals("N")){%> <html:checkbox
									property="specialReview" style="copy" disabled="true"
									onclick="dataChanged()" /> <%} else {%> <html:checkbox
									property="specialReview" style="copy" disabled="<%=modeValue%>"
									value='Y' onclick="dataChanged()" /> <%}%> <script>
                              //selectCheckBox('specialReview', '<%=moduleValue%>');
                              if('<%=moduleValue%>' == 'Y'){
                                obj[index++] = 'specialReview';
                              }                              
                          </script>
							</td>
							<td width="31%" valign="middle" class="copybold"><bean:message
									key="specialReviewLabel.SpecialReview" /></td>
							<td width="2%" valign="middle">
								<%moduleValue = (String) dynaFormModule.get("references");
                          moduleValue = (moduleValue==null) ? EMPTY_STRING : moduleValue;
                          if(moduleValue.equals("N")){%> <html:checkbox
									property="references" style="copy" disabled="true"
									onclick="dataChanged()" /> <%} else {%> <html:checkbox
									property="references" style="copy" disabled="<%=modeValue%>"
									value='Y' onclick="dataChanged()" /> <%}%> <script>
                              //selectCheckBox('references', '<%=moduleValue%>');
                              if('<%=moduleValue%>' == 'Y'){
                                obj[index++] = 'references';
                              }                              
                          </script>
							</td>
							<td width="32%" valign="middle" class="copybold"><bean:message
									key="amendmentRenewal.references" /></td>
						</tr>
						<tr>
							<td height="30" width="2%" valign="middle">
								<%moduleValue = (String) dynaFormModule.get("uploadDocuments");
                          moduleValue = (moduleValue==null) ? EMPTY_STRING : moduleValue;
                          if(moduleValue.equals("N")){%> <html:checkbox
									property="uploadDocuments" style="copy" disabled="true"
									onclick="dataChanged()" /> <%} else {%> <html:checkbox
									property="uploadDocuments" style="copy"
									disabled="<%=modeValue%>" value='Y' onclick="dataChanged()" />
								<%}%> <script>
                              selectCheckBox('uploadDocuments', '<%=moduleValue%>');
                              if('<%=moduleValue%>' == 'Y'){
                                obj[index++] = 'uploadDocuments';
                              }                              
                          </script>
							</td>
							<td width="31%" valign="middle" class="copybold"><bean:message
									key="uploadDocLabel.UploadDocument" /></td>
							<td height="30" width="2%" valign="middle">
								<%moduleValue = (String) dynaFormModule.get("others");
                          moduleValue = (moduleValue==null) ? EMPTY_STRING : moduleValue;
                          if(moduleValue.equals("N")){%> <html:checkbox
									property="others" style="copy" disabled="true"
									onclick="dataChanged()" /> <%} else {%> <html:checkbox
									property="others" style="copy" disabled="<%=modeValue%>"
									value='Y' onclick="dataChanged()" /> <%}%> <script>
                              selectCheckBox('others', '<%=moduleValue%>');
                              if('<%=moduleValue%>' == 'Y'){
                                obj[index++] = 'others';
                              }                              
                    </script>
							</td>
							<td width="31%" valign="middle" class="copybold"><bean:message
									bundle="iacuc" key="amendment.summary.others" /></td>
						</tr>
						<logic:notEmpty name="vecQuestionnaires" scope="session">
							<tr class="theader">
								<td height="25" width="31%" colspan="6" valign="middle"
									class="copybold">&nbsp;Questionnaire</td>
							</tr>
							<%int qnrIndex = 0;%>
							<logic:iterate name="vecQuestionnaires" scope="session"
								id="qnrCombo" type="edu.mit.coeus.utils.ComboBoxBean">
								<%if(qnrIndex == 0){%>
								<tr>
									<%}%>

									<td height="30" width="2%" valign="middle">
										<% String code = qnrCombo.getCode();
                            boolean enabled = false;
                            boolean checked = false;
                            
                            if(!modeValue && enabledQuestionnaires!= null && enabledQuestionnaires.contains(code)){
                                enabled = true;
                            }
                            if(checkedQuestionnaires!= null && checkedQuestionnaires.contains(code)){
                                checked = true;
                            }%> <%if(enabled && checked){%> <input
										type="checkbox" name="<%=code%>" checked
										onclick="dataChanged()"> <%}else if(enabled && !checked){%>
										<input type="checkbox" name="<%=code%>"
										onclick="dataChanged()"> <%}else if(!enabled && checked){%>
										<input type="checkbox" name="<%=code%>" checked
										disabled="true"> <%}else if(!enabled && !checked){%> <input
										type="checkbox" name="<%=code%>" disabled="true"> <%}%>
									</td>

									<td width="31%" valign="middle" class="copybold"><bean:write
											name="qnrCombo" property="description" /></td>

									<% qnrIndex++;
                        if(qnrIndex == 3){
                            qnrIndex = 0;
                        }
                        if(qnrIndex == 0){%>
								</tr>
								<tr>
									<td>&nbsp;</td>
								</tr>
								<%}
                    %>

							</logic:iterate>
						</logic:notEmpty>
					</table>
				</td>
			</tr>
			<%}%>
			<%}%>
			<%--COEUSQA-2602:Remove checkboxes from Renewal Summary screen - end --%>
			<tr>
				<td height="5"></td>
			</tr>
			<%if(!modeValue){%>
			<tr>
				<td align=center class='savebutton'>
					<%String saveSummary = "Save ";%> <%String pageSave = "javascript:pageSave('"+modeVal+"');";%>
					<html:button property="ok" value="<%=saveSummary%>"
						styleClass="clsavebutton" onclick="<%=pageSave%>" />
				</td>
			</tr>
			<%}%>
		</table>




		<html:hidden property="createdDate" value="<%=createdDate%>" />
		<html:hidden property="awUpdateTimestamp" value="<%=updateTimestamp%>" />
		<html:hidden property="protocolNumber" value="<%=protocolNumber%>" />
		<html:hidden property="sequenceNumber" value="<%=sequenceNumber%>" />
		<html:hidden property="awUpdateUser" value="<%=updateUser%>" />

		<script>
  DATA_CHANGED = 'false';
  if(errValue && !errLock) {
    DATA_CHANGED = 'true';
  }
  var linkVal = '<%=modeVal%>';
  if(linkVal == 'NA'){
    LINK = "<%=request.getContextPath()%>/saveNewAmendmentRenewal.do?saveNewAR="+linkVal;
    dataChanged();
  }else if(linkVal == 'NR'){
    LINK = "<%=request.getContextPath()%>/saveNewAmendmentRenewal.do?saveNewAR="+linkVal;
    dataChanged();
  }else{
    LINK = "<%=request.getContextPath()%>/saveAmendmentRenewal.do?operation=S";
  }
  FORM_LINK = document.amendmentRenewalForm;
  PAGE_NAME = "<%=summary%> <bean:message key="label.AmendRenew.saveSummary"/>";
  function dataChanged(){
    DATA_CHANGED = 'true';
  }
  linkForward(errValue);
 </script>


	</body>
</html:form>
</html:html>
