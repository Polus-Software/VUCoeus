<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@page
	import="org.apache.struts.taglib.html.JavascriptValidatorTag,
edu.mit.coeuslite.utils.CoeusLiteConstants, org.apache.struts.action.DynaActionForm,java.util.Vector,java.util.HashMap"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="iacucProtoStudyGroupsDynaBeansList" scope="session"
	class="edu.mit.coeuslite.utils.CoeusDynaFormList" />
<jsp:useBean id="vecAddedSpeciesData" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="vecAcLocationTypes" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="vecAcLocationNames" scope="session"
	class="java.util.Vector" />

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
            function showHideProcedures(speciesId){
               if(document.getElementById("proceduresForSpecies"+speciesId).style.display == 'block'){
                  document.getElementById("plus"+speciesId).style.display = 'block';
                    document.getElementById("minus"+speciesId).style.display = 'none';
                    document.getElementById("proceduresForSpecies"+speciesId).style.display = 'none';
                }else{
                    document.getElementById("plus"+speciesId).style.display = 'none';
                    document.getElementById("minus"+speciesId).style.display = 'block';
                    document.getElementById("proceduresForSpecies"+speciesId).style.display = 'block';
                }
                var procedureElem = document.getElementsByTagName("div");
                for(var i = 0; i < procedureElem.length; i++) {
                    var id = procedureElem[i].id;
                    var showAllReg = new RegExp("species"+speciesId);
                    var showProcDetails = new RegExp("procedureplus");
                    var hideProcDetails = new RegExp("procedureminus");
                    var hideProcDetailsInfo = new RegExp("proceduresDetails"+speciesId);
                    if(showAllReg.exec(id) != null){
                        procedureElem[i].style.display = 'none';
                    }
                    
                    if(showProcDetails.exec(id) != null){
                        procedureElem[i].style.display = 'block';
                    }
                    if(hideProcDetails.exec(id) != null){
                        procedureElem[i].style.display = 'none';
                    }

                    if(hideProcDetailsInfo.exec(id) != null){
                        procedureElem[i].style.display = 'none';
                    }

                    
                }
            }
            
            function showHideProceduresDetails(speciesId, procStudyGroupId){
                if(document.getElementById("proceduresDetails"+speciesId+procStudyGroupId).style.display == 'block'){
                    document.getElementById("procedureplus"+speciesId+procStudyGroupId).style.display = 'block';
                    document.getElementById("procedureminus"+speciesId+procStudyGroupId).style.display = 'none';
                    document.getElementById("proceduresDetails"+speciesId+procStudyGroupId).style.display = 'none';
                }else{
                    document.getElementById("procedureplus"+speciesId+procStudyGroupId).style.display = 'none';
                    document.getElementById("procedureminus"+speciesId+procStudyGroupId).style.display = 'block';
                    document.getElementById("proceduresDetails"+speciesId+procStudyGroupId).style.display = 'block';
                }
            }
            
            function showTrainingDetails(indexId){
                var winleft = (screen.width - 650) / 2;
                var winUp = (screen.height - 450) / 2;  
                var url_value = "<%=request.getContextPath()%>/getPersonTrainingDetails.do?indexId="+indexId;
                var win = "scrollbars=yes,resizable=1,width=650,height=400,location=no,left="+winleft+",top="+winUp;
                sList = window.open(url_value, "list", win);

                if (parseInt(navigator.appVersion) >= 4) {
                window.sList.focus(); 
                }
            }
            
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
                document.iacucProtoStudyGroupsDynaBeansList.action = "<%=request.getContextPath()%>"+procLink;
                document.iacucProtoStudyGroupsDynaBeansList.submit();
            }
  
    </script>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>
<%
String completeImage= request.getContextPath()+"/coeusliteimages/complete.gif";
String noneImage= request.getContextPath()+"/coeusliteimages/none.gif";
String currentTabStyle = "background-image:url('"+request.getContextPath()+"/coeusliteimages/tab2.gif');cursor: pointer;";
String remainingTabStyle = "background-image:url('"+request.getContextPath()+"/coeusliteimages/tab1.gif');cursor: pointer;";
%>
<body>
	<html:form action="viewAllStudyGroupDetails.do">
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
								style="<%=remainingTabStyle%>" class="tabbghl"
								onclick="activateTab('PERSONNEL')"><b><bean:message
										bundle="iacuc" key="studyGroup.personnelTabName" /></b></td>
							<td width="20%" height="27" align="center" valign="middle"
								style="<%=remainingTabStyle%>" class="tabbghl"
								onclick="activateTab('LOCATION')"><b><bean:message
										bundle="iacuc" key="studyGroup.locationTabName" /></b></td>
							<td width="20%" height="27" align="center" valign="middle"
								style="<%=currentTabStyle%>" class="tabbghl"
								onclick="activateTab('VIEWALL')"><b><bean:message
										bundle="iacuc" key="studyGroup.viewAllTabName" /></b></td>
							<td width="20%">&nbsp;</td>
						</tr>
					</table>
				</td>
			</tr>

			<tr>
				<td>
					<table width="99%" border="0" cellpadding="3" cellspacing="4"
						class="tabtable" align="center">
						<%Vector vecSpecies = (Vector)session.getAttribute("vecAddedSpeciesData");
                if(vecSpecies == null || vecSpecies.isEmpty()){%>
						<tr class="copy">
							<td><bean:message bundle="iacuc"
									key="studyGroup.noSpeciesMsg" /></td>

						</tr>
						<%}%>
						<logic:present name="vecAddedSpeciesData" scope="session">
							<logic:iterate id="speciesGroupForm" name="vecAddedSpeciesData"
								type="org.apache.struts.action.DynaActionForm" indexId="index"
								scope="session">
								<%
                String speciesId = speciesGroupForm.get("speciesId").toString();
                %>
								<tr class="copy">
									<td width="1%">
										<div id="<%=("plus"+speciesGroupForm.get("speciesId"))%>">
											<%String showDivlink = "javascript:showHideProcedures('"+speciesGroupForm.get("speciesId")+"')";%>
											<html:link href="<%=showDivlink%>">
												<img border="0"
													src="<bean:write name='ctxtPath'/>/coeusliteimages/plus.gif">
											</html:link>
										</div>
										<div id="<%=("minus"+speciesGroupForm.get("speciesId"))%>">
											<%String hideDivlink = "javascript:showHideProcedures('"+speciesGroupForm.get("speciesId")+"')";%>
											<html:link href="<%=hideDivlink%>">
												<img border="0"
													src="<bean:write name='ctxtPath'/>/coeusliteimages/minus.gif">
											</html:link>
										</div>
									</td>
									<td width="30%" align="left"><b> <bean:message
												bundle="iacuc" key="label.groupSpecies.group" />:
									</b> &nbsp;<bean:write name="speciesGroupForm" property="groupName" />
									</td>
									<td width="30%" align="left"><b> <bean:message
												bundle="iacuc" key="label.groupSpecies.species" />:
									</b> &nbsp; <bean:write name="speciesGroupForm"
											property="speciesName" /></td>
									<td width="25%" align="left"><b> <bean:message
												bundle="iacuc" key="label.groupSpecies.totalCount" />:
									</b> <bean:write name="speciesGroupForm" property="speciesCount" />
									</td>
								</tr>
								<tr>
									<td colspan="8">
										<%int trainingIndex = 0;
                int procedureCount = 0;
                %>
										<div id="<%=("proceduresForSpecies"+speciesId)%>">

											<table width="98%" border="0" cellpadding="3" cellspacing="4"
												class="tabtable" align="center">
												<logic:present name="iacucProtoStudyGroupsDynaBeansList"
													property="list" scope="session">
													<logic:iterate id="procedures"
														name="iacucProtoStudyGroupsDynaBeansList" property="list"
														type="org.apache.struts.action.DynaActionForm"
														scope="session">
														<%
                            
                            String speciesIdInProcedure = procedures.get("speciesId").toString();
                            int procStudyGroupId = ((Integer)procedures.get("studyGroupId")).intValue();
                            if(speciesId.equals(speciesIdInProcedure)){
                                procedureCount++;
                            %>
														<tr class="copy">
															<td width="1%">
																<div
																	id="<%=("procedureplus"+speciesGroupForm.get("speciesId")+procStudyGroupId)%>">
																	<%showDivlink = "javascript:showHideProceduresDetails('"+speciesGroupForm.get("speciesId")+"','"+procStudyGroupId+"')";%>
																	<html:link href="<%=showDivlink%>">
																		<img border="0"
																			src="<bean:write name='ctxtPath'/>/coeusliteimages/plus.gif">
																	</html:link>
																</div>
																<div
																	id="<%=("procedureminus"+speciesGroupForm.get("speciesId")+procStudyGroupId)%>">
																	<%hideDivlink = "javascript:showHideProceduresDetails('"+speciesGroupForm.get("speciesId")+"','"+procStudyGroupId+"')";%>
																	<html:link href="<%=hideDivlink%>">
																		<img border="0"
																			src="<bean:write name='ctxtPath'/>/coeusliteimages/minus.gif">
																	</html:link>
																</div>
															</td>

															<td width="40%"><b><bean:message bundle="iacuc"
																		key="studyGroup.label.ProcedureCategory" />:</b>&nbsp; <bean:write
																	name="procedures" property="procedureCategoryDesc" /></td>
															<td width="40%"><b><bean:message bundle="iacuc"
																		key="studyGroup.label.Procedure" />:</b>&nbsp; <bean:write
																	name="procedures" property="procedureDesc" /></td>
															<td width="15%"><b><bean:message bundle="iacuc"
																		key="studyGroup.label.Count" />:</b>&nbsp; <bean:write
																	name="procedures" property="count" /></td>

														</tr>
														<tr>
															<td colspan="8">
																<div
																	id="<%=("proceduresDetails"+speciesId+procStudyGroupId)%>">
																	<table width="100%" border="0" cellpadding="3"
																		cellspacing="4" align="center">
																		<tr>
																			<td colspan="8">
																				<table width="100%" border="0" cellpadding="3"
																					cellspacing="4" class="tabtable" align="center">
																					<tr class="tableheader">
																						<td colspan="4"><bean:message bundle="iacuc"
																								key="studyGroup.label.AdditionalInformation" />
																						</td>
																					</tr>
																					<%int addInfoCount = 0;%>
																					<logic:present
																						name="iacucProtoStudyGroupsDynaBeansList"
																						property="infoList" scope="session">
																						<logic:iterate id="additionalInfoForm"
																							name="iacucProtoStudyGroupsDynaBeansList"
																							property="infoList"
																							type="org.apache.struts.action.DynaActionForm">
																							<%
                                                                int addInfoStudyGroupId  = ((Integer)additionalInfoForm.get("studyGroupId")).intValue();
                                                                if(procStudyGroupId == addInfoStudyGroupId){
                                                                    addInfoCount++;
                                                                %>
																							<tr class="copy">
																								<td class="copybold" width="20%" align="right"
																									valign="top"><bean:write
																										name="additionalInfoForm"
																										property="columnLabel" />:</td>
																								<td width="80%" colspan="3"><bean:write
																										name="additionalInfoForm"
																										property="columnValue" /></td>
																							</tr>
																							<%}%>
																						</logic:iterate>
																					</logic:present>
																					<%if(addInfoCount == 0){%>
																					<tr class="copy">
																						<td colspan="5"><bean:message bundle="iacuc"
																								key="studyGroup.noadditionalInformationMsg" /></td>
																					</tr>
																					<%}%>
																				</table>
																			</td>
																		</tr>

																		<tr>
																			<td colspan="8">
																				<table width="100%" border="0" cellpadding="3"
																					cellspacing="4" class="tabtable" align="center">
																					<tr class="tableheader">
																						<td colspan="4"><bean:message bundle="iacuc"
																								key="studyGroup.label.Locations" /></td>
																					</tr>
																					<tr class="copybold">
																						<td width="25%"><bean:message bundle="iacuc"
																								key="studyGroup.label.LocationType" /></td>
																						<td width="25%"><bean:message bundle="iacuc"
																								key="studyGroup.label.LocationName" /></td>
																						<td width="25%"><bean:message bundle="iacuc"
																								key="studyGroup.label.Room" /></td>
																						<td width="25%"><bean:message bundle="iacuc"
																								key="studyGroup.label.Description" /></td>
																					</tr>
																					<%int locationCount = 0;%>
																					<logic:present
																						name="iacucProtoStudyGroupsDynaBeansList"
																						property="beanList" scope="session">
																						<logic:iterate id="locationForm"
																							name="iacucProtoStudyGroupsDynaBeansList"
																							property="beanList"
																							type="org.apache.struts.action.DynaActionForm">
																							<%
                                                                int locStudyGroupId  = ((Integer)locationForm.get("studyGroupId")).intValue();
                                                                if(procStudyGroupId == locStudyGroupId){
                                                                    locationCount++;
                                                                %>
																							<tr class="copy">
																								<td width="25%"><bean:write
																										name="locationForm"
																										property="locationTypeDesc" /></td>
																								<td width="25%"><bean:write
																										name="locationForm" property="loactionName" />
																								</td>
																								<td width="25%"><bean:write
																										name="locationForm"
																										property="studyGroupLocationRoom" /></td>
																								<td width="25%"><bean:write
																										name="locationForm"
																										property="studyGroupLocationDesc" /></td>
																							</tr>
																							<%}%>
																						</logic:iterate>
																					</logic:present>
																					<%if(locationCount == 0){%>
																					<tr class="copy">
																						<td colspan="5"><bean:message bundle="iacuc"
																								key="studyGroup.noLocationMsg" /></td>
																					</tr>
																					<%}%>
																				</table>
																			</td>
																		</tr>
																		<tr>
																			<td colspan="8">
																				<table width="100%" border="0" cellpadding="3"
																					cellspacing="4" class="tabtable" align="center">

																					<tr class="tableheader">
																						<td colspan="4"><bean:message bundle="iacuc"
																								key="studyGroup.label.ProcedurePersonnel" /></td>
																					</tr>

																					<tr class="copybold">
																						<td width="50%"><bean:message bundle="iacuc"
																								key="studyGroup.label.InvestigatorsStudyPersonnel" />
																						</td>
																						<td width="15%"><bean:message bundle="iacuc"
																								key="studyGroup.label.Trained" /></td>
																						<td></td>
																					</tr>
																					<%int personCount = 0;
                                                        
                                                        %>
																					<logic:present
																						name="iacucProtoStudyGroupsDynaBeansList"
																						property="valueList" scope="session">
																						<logic:iterate id="personResponsible"
																							name="iacucProtoStudyGroupsDynaBeansList"
																							property="valueList"
																							type="org.apache.struts.action.DynaActionForm">
																							<%
                                                                int perStudyGroupId  = ((Integer)personResponsible.get("studyGroupId")).intValue();
                                                                if(procStudyGroupId == perStudyGroupId){
                                                                    personCount++;
                                                                %>
																							<tr class="copy">
																								<td width="50%"><bean:write
																										name="personResponsible" property="personName" />
																								</td>
																								<td width="15%">
																									<%
                                                                        if("Y".equals(personResponsible.get("isTrained").toString())){
                                                                        %>
																									<html:img src="<%=completeImage%>" border="0" />
																									<%}else{%> <html:img src="<%=noneImage%>"
																										border="0" /> <%}%>
																								</td>
																								<td>
																									<%String trainingDetailLink = "javaScript:showTrainingDetails('"+trainingIndex+"')";%>
																									<html:link href="<%=trainingDetailLink%>">
																										<u><bean:message bundle="iacuc"
																												key="studyGroup.label.TrainingDetails" /></u>
																									</html:link>
																								</td>
																							</tr>
																							<%trainingIndex++;%>
																							<%}%>
																						</logic:iterate>
																					</logic:present>
																					<%
                                                        if(personCount == 0){%>
																					<tr class="copy">
																						<td colspan="5"><bean:message bundle="iacuc"
																								key="studyGroup.noLocationMsg" /></td>
																					</tr>
																					<%}%>


																				</table>
																			</td>
																		</tr>
																	</table>
																</div>
															</td>
														</tr>
														<%}%>
													</logic:iterate>
												</logic:present>
												<%if(procedureCount == 0){%>
												<tr class="copy">
													<td><bean:message bundle="iacuc"
															key="studyGroup.noProcedureMsg" /></td>
												</tr>
												<%}%>
												</div>
											</table>
								</tr>
							</logic:iterate>
						</logic:present>
					</table>
				</td>
			</tr>
		</table>
	</html:form>
</body>

<script>
    <%
     
    if(vecAddedSpeciesData != null && !vecAddedSpeciesData.isEmpty()){
                                for(Object speciesDetails : vecAddedSpeciesData){
                                    DynaActionForm speciesDetailForm = (DynaActionForm)speciesDetails;
                                    String speciesId = speciesDetailForm.get("speciesId").toString(); 
    %>
                document.getElementById("plus"+<%=speciesId%>).style.display = 'block';
                document.getElementById("minus"+<%=speciesId%>).style.display = 'none';
                document.getElementById("proceduresForSpecies"+<%=speciesId%>).style.display = 'none';
             
        <%}
     }
  %>
</script>

</html:html>
