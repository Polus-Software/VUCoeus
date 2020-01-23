<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@page
	import="org.apache.struts.taglib.html.JavascriptValidatorTag,
edu.mit.coeuslite.utils.CoeusLiteConstants, org.apache.struts.action.DynaActionForm,java.util.Vector,java.util.HashMap"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="iacucProcedurePersons" scope="session"
	class="edu.mit.coeuslite.utils.CoeusDynaFormList" />
<html:html>
<head>

<title>Coeus Web</title>
<style>
.cltextbox-color {
	font-weight: normal;
	width: 500px
}

.textbox {
	width: 30px
}
</style>
<script>
            var errValue = false;
            var errLock = false;   
            var sList;
            function activateTab(tabName){
                var procLink = '';
                if(tabName == 'PROCEDURE'){
                    procLink = '/getStudyGroups.do';
                }else if(tabName == 'PERSONNEL'){
                    procLink = '/loadPersonResponsible.do';
                }else if(tabName == 'LOCATION'){
                    procLink = '/loadStudyGroupsLocation.do';
                }else if(tabName == 'VIEWALL'){
                    procLink = '/viewAllStudyGroupDetails.do';
                }
                CLICKED_LINK = "<%=request.getContextPath()%>"+procLink;
                if(validate()){
                    document.iacucProtoStudyGroupsDynaBeansList.action = CLICKED_LINK;
                    document.iacucProtoStudyGroupsDynaBeansList.submit();
                }
            }
            
            function showTrainingDetails(personId,personName){
                var winleft = (screen.width - 650) / 2;
                var winUp = (screen.height - 450) / 2;  
                var url_value = "<%=request.getContextPath()%>/getPersonTrainingDetailsInProcedureTab.do?personId="+personId+"&personName="+personName;
                var win = "scrollbars=yes,resizable=1,width=650,height=400,location=no,left="+winleft+",top="+winUp;
                sList = window.open(url_value, "list", win);

                if (parseInt(navigator.appVersion) >= 4) {
                    window.sList.focus(); 
                }
            }
            
            function closeSelection(isDataModified){
                sList.close();  
                document.iacucProtoStudyGroupsDynaBeansList.action = "<%=request.getContextPath()%>/refreshPersonnelTab.do?isDataModified="+isDataModified;
                document.iacucProtoStudyGroupsDynaBeansList.submit();
            }
            
            function assignAllIacucProcedures(personId,personName){
                if(confirm('<bean:message bundle="iacuc" key="studyGroup.personTab.assignAllProcedure"/>')){
                    document.iacucProtoStudyGroupsDynaBeansList.action = "<%=request.getContextPath()%>/assignAllIacucProcedures.do?personId="+personId;
                    document.iacucProtoStudyGroupsDynaBeansList.submit();
                }
            }
            
            function openProceduresSelection(personId,personName,mode){      
                var winleft = (screen.width - 550) / 2;
                var winUp = (screen.height - 450) / 2;  
                var url_value = "<%=request.getContextPath()%>/addProceduresForPerson.do?personId="+personId+"&personName="+personName+"&mode="+mode;
                var win = "scrollbars=yes,resizable=0,width=700,height=450,left="+winleft+",top="+winUp;
                sList = window.open(url_value, "list", win);

                if (parseInt(navigator.appVersion) >= 4) {
                    window.sList.focus(); 
                }
            }            
            
            function saveProcedurePersonnel(){
               document.iacucProtoStudyGroupsDynaBeansList.action = "<%=request.getContextPath()%>/saveAssignedProceduresForPerson.do";
               document.iacucProtoStudyGroupsDynaBeansList.submit();
            }
            
            function dataChanged(){
                DATA_CHANGED = 'true';
            }
        </script>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>
<%
    String mode=(String)session.getAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId());
    boolean modeValue=false;
    if(mode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE)){
        modeValue = true;
    }else if(mode.equalsIgnoreCase(CoeusLiteConstants.MODIFY_MODE)){
        modeValue=false;
    }else if(mode.equalsIgnoreCase(CoeusLiteConstants.ADD_MODE)){
        modeValue=false;
    }
    
    String isProcedureExists = (String)request.getAttribute("IS_PROCEDURE_EXISTS");

    String calImage = request.getContextPath()+"/coeusliteimages/cal.gif";
    
    String strProtocolNum = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
    if(strProtocolNum == null){
        strProtocolNum = "";    
    }
    String amendRenewPageMode = (String)session.getAttribute("amendRenewPageMode"+request.getSession().getId());
    if(strProtocolNum.length() > 10 && (amendRenewPageMode == null || 
        amendRenewPageMode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE))){
        modeValue = true;
    }
    
    String currentTabStyle = "background-image:url('"+request.getContextPath()+"/coeusliteimages/tab2.gif');cursor: pointer;";
    String remainingTabStyle = "background-image:url('"+request.getContextPath()+"/coeusliteimages/tab1.gif');cursor: pointer;";
    %>
<body>
	<html:form action="/saveAssignedProceduresForPerson.do" method="post">
		<script type="text/javascript">
                document.getElementById('txt').innerHTML = '<bean:message bundle="iacuc" key="helpPageTextProtocol.Procedures"/>';
            </script>
		<table width="100%" height="100%" border="0" cellpadding="0"
			cellspacing="0" class="table">
			<tr>
				<td>
					<table width="100%" height="20" border="0" cellpadding="0"
						cellspacing="0" class="tableheader">
						<tr>
							<td style="font-family: Arial, Helvetica, sans-serif;"><bean:message
									bundle="iacuc" key="iacuc.speciesGroups.lblProtocolProcedures" />
							</td>
							<td align="right" valign="top"><a id="helpPageText"
								href="javascript:showBalloon('helpPageText', 'helpText')"> <u><bean:message
											bundle="iacuc" key="helpPageTextProtocol.help" /></u>
							</a></td>&nbsp;&nbsp;
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td align="left" valign="top">
					<table width="99%" border="0" align="center" cellpadding="0"
						cellspacing="0">
						<tr>
							<td width="20%" height="27" align="center" valign="middle"
								style="<%=remainingTabStyle%>" class="tabbghl"
								onclick="activateTab('PROCEDURE')"><b><bean:message
										bundle="iacuc" key="studyGroup.procedureTabName" /></b></td>
							<td width="20%" height="27" align="center" valign="middle"
								style="<%=currentTabStyle%>" class="tabbghl"
								onclick="activateTab('PERSONNEL')"><b><bean:message
										bundle="iacuc" key="studyGroup.personnelTabName" /></b></td>
							<td width="20%" height="27" align="center" valign="middle"
								style="<%=remainingTabStyle%>" class="tabbghl"
								onclick="activateTab('LOCATION')"><b><bean:message
										bundle="iacuc" key="studyGroup.locationTabName" /></b></td>
							<td width="20%" height="27" align="center" valign="middle"
								style="<%=remainingTabStyle%>" class="tabbghl"
								onclick="activateTab('VIEWALL')"><b><bean:message
										bundle="iacuc" key="studyGroup.viewAllTabName" /></b></td>
							<td width="20%">&nbsp;</td>
						</tr>

					</table>
				</td>
			</tr>

			<tr>
				<td>
					<table cellspacing="1" cellpadding="0" border="0" align="center"
						width="99%" class="tabtable">
						<tr class="copybold">
							<td>
								<%if("N".equals(isProcedureExists)){%> <bean:message
									bundle="iacuc" key="studyGroup.personTab.noProcedureForProto" />
								<%}else{%> <logic:notPresent name="iacucProcedurePersons"
									scope="session" property="infoList">
									<bean:message bundle="iacuc"
										key="studyGroup.personTab.noPerson" />
								</logic:notPresent> <% if(!modeValue){%> <logic:present name="iacucProcedurePersons"
									scope="session" property="infoList">
									<bean:message bundle="iacuc"
										key="procedure.addProcedurePersons.msg" />
								</logic:present> <%}%> <%}%>
							</td>
						</tr>

					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table cellspacing="1" cellpadding="0" border="0" align="center"
						width="99%">
						<tr class="copybold">
							<td>&nbsp; <font color="red"> <logic:messagesPresent>
										<html:errors header="" footer="" />
										<script>errValue = true;</script>
									</logic:messagesPresent> <logic:messagesPresent message="true">
										<script>errValue = true;</script>
										<html:messages id="message" message="true">
											<html:messages id="message" message="true" property="errMsg">
												<script>errLock = true;</script>
												<li><bean:write name="message" /></li>
											</html:messages>
										</html:messages>
									</logic:messagesPresent>
							</font>
							</td>
						</tr>

					</table>
				</td>
			</tr>


			<tr>
				<td><logic:present name="iacucProcedurePersons" scope="session"
						property="infoList">
						<table width="99%" border="0" cellpadding="3" cellspacing="4"
							class="tabtable" align="center">
							<%int personIndex = 0;
                                %>
							<logic:iterate id="personForm" name="iacucProcedurePersons"
								property="infoList"
								type="org.apache.struts.action.DynaActionForm" scope="session">
								<%
                                    String personId = (String)personForm.get("personId");
                                    String personName = (String)personForm.get("personName");
                                    %>
								<tr class="copy">
									<td width="35%"><bean:write name="personForm"
											property="personName" /></td>
									<td width="15%">
										<%String trainingDetailLink = "javaScript:showTrainingDetails('"+personId+"','"+personName+"')";%>
										<html:link href="<%=trainingDetailLink%>">
											<u><bean:message bundle="iacuc"
													key="studyGroup.label.TrainingDetails" /></u>
										</html:link>
									</td>

									<%
                                            if(!modeValue && !"N".equals(isProcedureExists)){ 
                                                String openProcedureSelectionLink = "javaScript:openProceduresSelection('"+personId+"','"+personName+"','M')";%>
									<td width="15%"><html:link
											href="<%=openProcedureSelectionLink%>">
											<u><bean:message bundle="iacuc"
													key="studyGroup.label.AddProcedures" /></u>
										</html:link></td>
									<td width="20%">
										<%String assignAllProceLink = "javaScript:assignAllIacucProcedures('"+personId+"','"+personName+"')";%>
										<html:link href="<%=assignAllProceLink%>">
											<u><bean:message bundle="iacuc"
													key="studyGroup.label.AssignAllProcedures" /></u>
										</html:link>
									</td>
									<%}else if(modeValue){%>
									<%String openProcedureSelectionLink = "javaScript:openProceduresSelection('"+personId+"','"+personName+"','D')";%>
									<td><html:link href="<%=openProcedureSelectionLink%>">
											<u><bean:message bundle="iacuc"
													key="studyGroup.label.ViewProcedures" /></u>
										</html:link></td>
									<%}%>
									<td></td>
								</tr>

								<%personIndex++;%>
							</logic:iterate>

						</table>
					</logic:present></td>
			</tr>
			<%if(!modeValue && !"N".equals(isProcedureExists)){ %>
			<tr>
				<td>&nbsp;</td>
			</tr>

			<tr align="right" class="theader" height='30'>
				<td align='left' colspan="5">&nbsp; <html:button
						property="Save" value="Save" onclick="saveProcedurePersonnel()"
						styleClass="clsavebutton" />
				</td>
			</tr>
			<%}%>
		</table>
	</html:form>
</body>
<script>
    DATA_CHANGED = 'false';
    LINK = "<%=request.getContextPath()%>/saveAssignedProceduresForPerson.do";
    FORM_LINK = document.iacucProtoStudyGroupsDynaBeansList;
    PAGE_NAME = "<bean:message key="studyGroups.studyGroupsPersonnel" bundle="iacuc"/>";
    var isDataModified = '<%=request.getAttribute("isDataModified")%>';
    if(isDataModified == 'Y' || (errValue && !errLock)){
        dataChanged();
    }
    linkForward(errValue);
    </script>
</html:html>
