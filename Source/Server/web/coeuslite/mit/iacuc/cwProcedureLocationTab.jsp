<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@page
	import="org.apache.struts.taglib.html.JavascriptValidatorTag,
edu.mit.coeuslite.utils.CoeusLiteConstants, org.apache.struts.action.DynaActionForm,java.util.Vector,java.util.HashMap"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="iacucProcedureLocationTab" scope="session"
	class="edu.mit.coeuslite.utils.CoeusDynaFormList" />
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
                    document.iacucProceduresLocationList.action = CLICKED_LINK;
                    document.iacucProceduresLocationList.submit();
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
            
            function closeSelection(isDataModified){
                sList.close();  
                document.iacucProceduresLocationList.action = "<%=request.getContextPath()%>/refreshLocationTab.do?isDataModified="+isDataModified;
                document.iacucProceduresLocationList.submit();
            }
            
            function saveProcedurePersonnel(){
               document.iacucProceduresLocationList.action = "<%=request.getContextPath()%>/saveLocationTab.do";
               document.iacucProceduresLocationList.submit();
            }
            
            function openLookupWindow(lookupWin, lookupVal, lookupArgument, locationFormId,locationIndex) {
                index = locationFormId;
                var linkValue;
                var winleft = (screen.width - 830) / 2;
                var winUp = (screen.height - 450) / 2;  
                var win = "scrollbars=1,resizable=1,width=830,height=450,left="+winleft+",top="+winUp
                if(lookupWin == "locationType"){
                    searchSelection = 'locationType'
                    linkValue = 'iacucSearch.do';
                    lookupVal = 'Location Type In Location Tab';
                    lookupWin = "LOCATIONTYPESEARCH";
                }else if(lookupWin == "locationName"){
                    searchSelection = 'locationName'
                    linkValue = 'iacucSearch.do';
                    lookupVal = 'Location Name';
                    lookupWin = "LOCATIONNAMESEARCH";
                }
                if((lookupWin == "w_arg_value_list") || (lookupWin == "w_arg_code_tbl") || (lookupWin == "w_select_cost_element")){
                    linkValue = 'getArgumentData.do';
                    var win = "scrollbars=1,resizable=1,width=580,height=300,left="+winleft+",top="+winUp

                }
                win = "scrollbars=1,resizable=1,location=0,width=580,height=300";
                link = linkValue+'?type='+lookupVal+'&search=true&searchName='+lookupWin+'&argument='+lookupArgument+'&index='+locationIndex+'&locationFormId='+locationFormId+'&locationIndex='+locationIndex;
                sList = window.open('<bean:write name='ctxtPath'/>'+"/"+link, "list", win);
                if (parseInt(navigator.appVersion) >= 4) {
                    window.sList.focus(); 
                }
            }    
            
            function dataChanged(){
                DATA_CHANGED = 'true';
            }
            function fetch_Data(result,searchType){
                dataChanged();
                if(searchSelection == "locationType"){
                    var currentValue = document.getElementsByName('locations['+index+'].locationTypeCode'); 
                    if( currentValue != null && currentValue.length > 0 ){
                        currentValue[0].value = result["LOCATION_TYPE_CODE"];       
                    }
                    var currentValue1 = document.getElementsByName('locations['+index+'].locationTypeDesc'); 
                    if( currentValue1 != null && currentValue1.length > 0 ){
                        currentValue1[0].value = result["LOCATION"];       
                    }

                }else if(searchSelection == "locationName"){
                    var currentValue = document.getElementsByName('locations['+index+'].locationId'); 
                    if( currentValue != null && currentValue.length > 0 ){
                        currentValue[0].value = result["LOCATION_ID"];       
                    }
                    var currentValue1 = document.getElementsByName('locations['+index+'].loactionName'); 
                    if( currentValue1 != null && currentValue1.length > 0 ){
                        currentValue1[0].value = result["LOCATION_NAME"];       
                    }

                }

            }
            
            function removeLocation(locationFormId){
                if(confirm('<bean:message bundle="iacuc" key="studyGroup.locationTab.removeLocationConfirmation"/>')){ 
                    dataChanged();
                    document.iacucProceduresLocationList.action = "<%=request.getContextPath()%>/removeLocationInLocationTab.do?selectedLocationFormId="+locationFormId;
                    document.iacucProceduresLocationList.submit();             
                }
            }
            
            function loadLocationTypeFromSearch(locationIndex, locationFormId, locationTypeCode){
                valueChange('locations['+locationIndex+']');
                document.iacucProceduresLocationList.action = "<%=request.getContextPath()%>/loadLocationForTypeInLocationTab.do?selectedLocationType="+locationTypeCode+"&selectedFormId="+locationFormId;
                document.iacucProceduresLocationList.submit();  
            }
            function loadLocationForLocType(form,locationIndex, locationFormId){
                valueChange('locations['+locationIndex+']');
                document.iacucProceduresLocationList.action = "<%=request.getContextPath()%>/loadLocationForTypeInLocationTab.do?selectedLocationType="+form.value+"&selectedFormId="+locationFormId;
                document.iacucProceduresLocationList.submit();  
            }
            
            function valueChange(form){
                 var formActype = form+'.acType';
                 var formElement = document.getElementsByName(formActype);
                 if(formElement != null && formElement[0].value != 'I'){
                    formElement[0].value = 'U';
                 }
                 dataChanged();
            }  
           
            function addLocation(){
                dataChanged();
                document.iacucProceduresLocationList.action = "<%=request.getContextPath()%>/addLocationInLocationTab.do";
                document.iacucProceduresLocationList.submit();  
            }

            function openProceduresSelection(formId, mode){  
                var winleft = (screen.width - 550) / 2;
                var winUp = (screen.height - 450) / 2;  
                var url_value = "<%=request.getContextPath()%>/loadProceduresForLocationForm.do?selectedFormId="+formId+"&mode="+mode;
                var win = "scrollbars=yes,resizable=0,width=700,height=450,left="+winleft+",top="+winUp;
                sList = window.open(url_value, "list", win);

                if (parseInt(navigator.appVersion) >= 4) {
                    window.sList.focus(); 
                }
            }   
            
            function assignAllIacucProcedures(formId){
                if(confirm('<bean:message bundle="iacuc" key="studyGroup.locationTab.assignAllProcedure"/>')){
                    document.iacucProceduresLocationList.action = "<%=request.getContextPath()%>/assignAllProceduresForLocationForm.do?selectedFormId="+formId;
                    document.iacucProceduresLocationList.submit();
                }
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
    String calImage = request.getContextPath()+"/coeusliteimages/cal.gif";
    String isProcedureExists = (String)request.getAttribute("IS_PROCEDURE_EXISTS");
    
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
	<html:form action="/saveLocationTab" method="post">
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
								style="<%=currentTabStyle%>" class="tabbghl"
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

			<%if(!modeValue && !"N".equals(isProcedureExists)){%>
			<tr>
				<td>
					<table cellspacing="1" cellpadding="0" border="0" align="center"
						width="99%" class="tabtable">

						<tr class="copybold">
							<td><bean:message bundle="iacuc"
									key="procedure.addProcedureLocations.msg" /></td>
						</tr>
						<tr>
							<td>&nbsp;</td>
						</tr>

						<tr class='copy' align="left">
							<td>&nbsp; <font color="red"> <logic:messagesPresent>
										<html:errors header="" footer="" />
										<script>errValue = true;</script>
									</logic:messagesPresent> <logic:messagesPresent message="true">
										<script>errValue = true;</script>
										<html:messages id="message" message="true" bundle="iacuc"
											property="locationTypeCodeRequired">
											<script>errLock = true;</script>
											<li><bean:write name="message" /></li>
										</html:messages>
										<html:messages id="message" message="true" bundle="iacuc"
											property="locationIdRequired">
											<script>errLock = true;</script>
											<li><bean:write name="message" /></li>
										</html:messages>
										<html:messages id="message" message="true" bundle="iacuc"
											property="lengthExceed">
											<script>errLock = true;</script>
											<li><bean:write name="message" /></li>
										</html:messages>
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

						<tr class="copy">
							<td><html:link href="javaScript:addLocation()">
                                        &nbsp;<u><bean:message
											bundle="iacuc" key="studyGroup.label.AddLocation" /></u>
								</html:link></td>
						</tr>

					</table>
				</td>
			</tr>

			<%}else{%>
			<tr>
				<td>
					<table cellspacing="1" cellpadding="0" border="0" align="center"
						width="99%" class="tabtable">

						<tr class="copybold">
							<td>
								<%if("N".equals(isProcedureExists)){%> <bean:message
									bundle="iacuc" key="studyGroup.personTab.noProcedureForProto" />
								<%}%>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<%}%>
			<tr>
				<td>
					<table cellspacing="1" cellpadding="0" border="0" align="center"
						width="99%" class="tabtable">

						<tr>
							<td>
								<table width="100%" class="table" border="0" align="center">
									<logic:present name="iacucProcedureLocationTab"
										property="additionalInfoList" scope="session">
										<%
                                            int vecStudyLocationsDataIndex = -1;
                                            %>
										<logic:iterate id="locations" name="iacucProcedureLocationTab"
											property="additionalInfoList"
											type="org.apache.struts.action.DynaActionForm"
											indexId="locationIndex">
											<logic:notEqual name="locations" property="acType" value="D">
												<%
                                                    vecStudyLocationsDataIndex ++;
                                                    Integer locStudyGroupId = (Integer) locations.get("studyGroupId");
                                                    
                                                    Integer studyGroupLocationId = (Integer) locations.get("studyGroupLoctaionId");
                                                    Integer code  = (Integer) locations.get("locationTypeCode");
                                                    Integer locationFormId  = (Integer) locations.get("locationFormId");
                                                    
                                                    %>
												<tr>
													<td class="tabtable" colspan="4">
														<table border="0" width="100%" class="copybold"
															cellspacing="1" cellpadding="1">
															<tr>
																<td width="13%" class="copybold" align="right"><font
																	color="red">*</font> <bean:message bundle="iacuc"
																		key="studyGroup.label.LocationType" />:</td>
																<%
                                                                    String locationTypeCode = "0";
                                                                    
                                                                    if(locations.get("locationTypeCode") != null){
                                                                        locationTypeCode = String.valueOf(locations.get("locationTypeCode"));
                                                                    }
                                                                    
                                                                    String locationChangeLink = "javaScript:valueChange('locations["+locationIndex+"]')";
                                                                    String locTypeChangeLink = "javascript:loadLocationForLocType(this,'"+locationIndex+"','"+locationFormId+"')";%>
																<td width="25%" class="copybold" align="left"><html:select
																		style="width:210px;" property="locationTypeCode"
																		styleId="locationTypeCode" styleClass="textbox-long"
																		disabled="<%=modeValue%>" name="locations"
																		indexed="true" onchange="<%=locTypeChangeLink%>">
																		<html:option value="">
																			<bean:message key="generalInfoLabel.pleaseSelect" />
																		</html:option>
																		<html:options collection="vecAcLocationTypes"
																			property="code" labelProperty="description" />
																	</html:select> &nbsp; <%if(!modeValue){%> <%String urllink="javaScript:openLookupWindow('locationType','Location Type','"+locationTypeCode+"','"+locationFormId+"','"+locationIndex+"')";%>
																	<html:link href="<%=urllink%>">
																		<u><bean:message bundle="iacuc"
																				key="label.invesKeypersons.search" /></u>
																	</html:link> <%}%></td>
																<% request.setAttribute("vecAcLocationNames", (Vector)locations.get("vecAcLocationNames"));%>
																<td class="copy" width="34%">
																	<%if(!modeValue){%> <%String removeLocationLink = "javaScript:removeLocation('"+locationFormId+"');";%>
																	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <html:link
																		href="<%=removeLocationLink%>">
																		<u><bean:message bundle="iacuc"
																				key="studyGroup.label.removeProcLoc" /></u>
																	</html:link> <%}%>
																</td>
															</tr>
															<tr>
																<td width="13%" class="copybold" align="right"><font
																	color="red">*</font>
																<bean:message bundle="iacuc"
																		key="studyGroup.label.LocationName" />:</td>

																<td width="25%" class="copybold" align="left"><html:select
																		style="width:210px;" property="locationId"
																		styleId="locationId" styleClass="textbox-long"
																		name="locations" indexed="true"
																		onchange="<%=locationChangeLink%>"
																		disabled="<%=modeValue%>">
																		<html:option value="">
																			<bean:message key="generalInfoLabel.pleaseSelect" />
																		</html:option>
																		<html:options collection="vecAcLocationNames"
																			property="code" labelProperty="description" />
																	</html:select> &nbsp; <%if(!modeValue){%> <%String link = "javaScript:openLookupWindow('locationName','Location Name','"+locationTypeCode+"','"+locationFormId+"','"+locationIndex+"')";%>
																	<html:link href="<%=link%>">
																		<u><bean:message bundle="iacuc"
																				key="label.invesKeypersons.search" /></u>
																	</html:link> <%}%></td>
															</tr>
															<tr>
																<td width="8%" class="copybold" align="right">
																	&nbsp;&nbsp;<bean:message bundle="iacuc"
																		key="studyGroup.label.Room" />:
																</td>
																<td width="20%" class="copybold"><html:text
																		property="studyGroupLocationRoom" style="width:210px;"
																		styleClass="textbox-long" maxlength="60"
																		name="locations" indexed="true"
																		onchange="<%=locationChangeLink%>"
																		disabled="<%=modeValue%>" /></td>
																<td width="8%" class="copybold"></td>
															</tr>
															<tr>
																<td width="8%" class="copybold" align="right">
																	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <bean:message
																		bundle="iacuc" key="studyGroup.label.Description" />:
																</td>
																<td width="20%" class="copybold"><html:text
																		property="studyGroupLocationDesc" style="width:210px;"
																		styleClass="textbox-long" maxlength="60"
																		name="locations" indexed="true"
																		onchange="<%=locationChangeLink%>"
																		disabled="<%=modeValue%>" /></td>
																<td width="8%" class="copybold"></td>
															</tr>
															<tr>
																<td width="8%" class="copybold" align="right"><font
																	color="red">*</font> <bean:message bundle="iacuc"
																		key="studyGroup.label.locationProcedures" /></td>
																<td width="20%" class="copybold" colspan="2">
																	<%if(!modeValue){%> <%String openProcedureSelectionLink = "javaScript:openProceduresSelection('"+locationFormId+"','M')";%>

																	<html:link href="<%=openProcedureSelectionLink%>">
																		<u><bean:message bundle="iacuc"
																				key="studyGroup.label.AddProcedures" /></u>
																	</html:link> &nbsp;&nbsp; <%String assignAllProceLink = "javaScript:assignAllIacucProcedures('"+locationFormId+"')";%>
																	<html:link href="<%=assignAllProceLink%>">
																		<u><bean:message bundle="iacuc"
																				key="studyGroup.label.AssignAllProcedures" /></u>
																	</html:link> <%}%> &nbsp;&nbsp; <%String openProcedureSelectionLink = "javaScript:openProceduresSelection('"+locationFormId+"','D')";%>
																	<html:link href="<%=openProcedureSelectionLink%>">
																		<u><bean:message bundle="iacuc"
																				key="studyGroup.label.ViewProcedures" /></u>
																	</html:link>

																</td>

															</tr>
														</table>
													</td>
												</tr>
												<html:hidden property="acType" name="locations"
													indexed="true" />
												<html:hidden property="studyGroupId" name="locations"
													indexed="true" />
											</logic:notEqual>
										</logic:iterate>
									</logic:present>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<%if(!modeValue && !"N".equals(isProcedureExists)){ %>
			<tr align="right" class="theader" height='30'>
				<td align='left'>&nbsp; <html:button property="Save"
						value="Save" onclick="saveProcedurePersonnel()"
						styleClass="clsavebutton" />
				</td>
			</tr>
			<%}%>
		</table>
	</html:form>
</body>

<script>
    DATA_CHANGED = 'false';
    LINK = "<%=request.getContextPath()%>/saveLocationTab.do";
    FORM_LINK = document.iacucProceduresLocationList;
    PAGE_NAME = "<bean:message key="studyGroups.studyGroupsLocations" bundle="iacuc"/>";
    var isDataModified = '<%=request.getAttribute("isDataModified")%>';    
    if(isDataModified == 'Y' || (errValue && !errLock)){
        dataChanged();
    }
    linkForward(errValue);
    </script>
</html:html>
