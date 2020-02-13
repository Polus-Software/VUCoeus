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
        var errValue = false;
        var errLock = false;
        var searchSelection = ""; 
        var sList;
            function openProceduresSelection(speciesId){      
                var winleft = (screen.width - 550) / 2;
                var winUp = (screen.height - 450) / 2;  
                var url_value = "<%=request.getContextPath()%>/iacucProcedureList.do?speciesId="+speciesId;
                var win = "scrollbars=yes,resizable=0,width=700,height=450,left="+winleft+",top="+winUp;
                sList = window.open(url_value, "list", win);

                if (parseInt(navigator.appVersion) >= 4) {
                    window.sList.focus(); 
                }
            }
            function closeSelection(speciesId,isDataModified){
                sList.close();  
                document.iacucProtoStudyGroupsDynaBeansList.action = "<%=request.getContextPath()%>/refreshStudyGroup.do?selectedSpeciesId="+speciesId+"&isDataModified="+isDataModified;
                document.iacucProtoStudyGroupsDynaBeansList.submit();
            }
            function showProcedures(speciesId){
                document.getElementById("plus"+speciesId).style.display = 'none';
                document.getElementById("minus"+speciesId).style.display = 'block';
                document.getElementById("proceduresForSpecies"+speciesId).style.display = 'block';
                
                var procedureElem = document.getElementsByTagName("div");
                for(var i = 0; i < procedureElem.length; i++) {
                    var id = procedureElem[i].id;
                    var showAllReg = new RegExp("species"+speciesId);

                    if(showAllReg.exec(id) != null){
                        procedureElem[i].style.display = 'none';
                    }
                    
                }
            }
            
            function hideProcedures(speciesId){
                document.getElementById("plus"+speciesId).style.display = 'block';
                document.getElementById("minus"+speciesId).style.display = 'none';
                document.getElementById("proceduresForSpecies"+speciesId).style.display = 'none';
            }
            
            function hideLocations(location){
                document.getElementById(location+'Locations').style.display = 'none';
            }
            
            function showLocations(location){
                document.getElementById(location+'Locations').style.display = 'block';
                document.getElementById(location+'Persons').style.display = 'none';
                document.getElementById(location+'AdditionalInfo').style.display = 'none';
            }
            
            function hidePersons(person){
                document.getElementById(person+'Persons').style.display = 'none';
            }
            
            function showPersons(person){
                document.getElementById(person+'Persons').style.display = 'block';
                document.getElementById(person+'Locations').style.display = 'none';
                document.getElementById(person+'AdditionalInfo').style.display = 'none';
            }

            function hideAdditionalInfo(additionalInfo){
                document.getElementById(additionalInfo+'AdditionalInfo').style.display = 'none';
            }
            
            function showAdditionalInfo(additionalInfo){
                document.getElementById(additionalInfo+'AdditionalInfo').style.display = 'block';
                document.getElementById(additionalInfo+'Persons').style.display = 'none';
                document.getElementById(additionalInfo+'Locations').style.display = 'none';
            }
            
            function saveStudyGroup(){
                document.iacucProtoStudyGroupsDynaBeansList.action = "<%=request.getContextPath()%>/saveStudyGroups.do";
                document.iacucProtoStudyGroupsDynaBeansList.submit();
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
            
            function removePersonResponsible(studyGroupId, personId){
                if(confirm('<bean:message bundle="iacuc" key="studyGroup.removePersonConfirmation"/>')){
                    dataChanged();
                    document.iacucProtoStudyGroupsDynaBeansList.action = "<%=request.getContextPath()%>/removePersonResponsible.do?studyGroupId="+studyGroupId+"&personId="+personId;
                    document.iacucProtoStudyGroupsDynaBeansList.submit();
                }
            }
            
            function openLookupWindow(lookupWin, lookupVal, lookupArgument, count, studyGroupIdValue, indexValue, locationIndexValue) {
                index = count;
                var linkValue = 'generalProposalSearch.do';
                var winleft = (screen.width - 830) / 2;
                var winUp = (screen.height - 450) / 2;  
                var win = "scrollbars=1,resizable=1,width=830,height=450,left="+winleft+",top="+winUp
                if(lookupWin == "w_unit_select"){
                    lookupWin = "w_unit_select" ;
                    lookupVal = "UNITSEARCH" ;
                    lookupArgument =  "w_unit_select" ;
                }
                else  if(lookupWin == "w_organization_select"){
                    lookupWin = "w_organization_select" ;
                    lookupVal = "ORGANIZATIONSEARCH" ;
                    lookupArgument =  "w_organization_select" ;
                }
                else if(lookupWin == "w_sponsor_select"){
                    lookupWin = "w_sponsor_select" ;
                    lookupVal = "SPONSORSEARCH" ;
                    lookupArgument =  "w_sponsor_select" ;

                }else if(lookupWin == "w_rolodex_select"){
                    lookupWin = "w_rolodex_select" ;
                    lookupVal = "ROLODEXSEARCH" ;
                    lookupArgument =  "w_rolodex_select" ;
                }else if(lookupWin == "w_person_select"){
                    lookupWin = "w_person_select" ;
                    lookupVal = "PERSONSEARCH" ;
                }else if(lookupWin == "w_arg_code_tbl" ){
                    lookupWin = "w_arg_code_tbl" ;        
                    lookupVal = lookupArgument;
                }else if(lookupWin == "w_select_cost_element"){
                    lookupWin = "w_select_cost_element" ;
                    lookupArgument = "w_select_cost_element" ;
                    lookupVal = "Cost Element" ;
                }else if(lookupWin == "w_arg_value_list"){
                    lookupWin = "w_arg_value_list" ;
                    lookupVal = lookupArgument;        
                }else if(lookupWin == "locationType"){
                    searchSelection = 'locationType'
                    linkValue = 'iacucSearch.do';
                    lookupVal = 'Location Type';
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
                link = linkValue+'?type='+lookupVal+'&search=true&searchName='+lookupWin+'&argument='+lookupArgument+'&studyGroupId='+studyGroupIdValue+'&index='+indexValue+'&locationIndex='+locationIndexValue;
                sList = window.open('<bean:write name='ctxtPath'/>'+"/"+link, "list", win);
                if (parseInt(navigator.appVersion) >= 4) {
                    window.sList.focus(); 
                }
            }    
    
            function fetch_Data(result,searchType){
                dataChanged();
                if(searchType == "UNITSEARCH"){ 
                    var currentValue = document.getElementsByName('additionalInfoForm['+index+'].columnValue'); 
                    if( currentValue != null && currentValue.length > 0 ){
                        currentValue[0].value = result["UNIT_NUMBER"];
                    }
                    var currentDesc = document.getElementsByName('additionalInfoForm['+index+'].description');  
                    if(currentDesc != null  &&  currentDesc.length > 0){
                        currentDesc[0].value = result["UNIT_NAME"];
                    }
                }else if(searchType == "PERSONSEARCH"){
                    var currentValue = document.getElementsByName('additionalInfoForm['+index+'].columnValue');   
                    if( currentValue != null && currentValue.length > 0 ){
                        currentValue[0].value = result["PERSON_ID"];
                    }
                    var currentDesc = document.getElementsByName('additionalInfoForm['+index+'].description');         
                    if(currentDesc != null  &&  currentDesc.length > 0){
                        currentDesc[0].value = result["FULL_NAME"];
                    }
                }else if(searchType == "ORGANIZATIONSEARCH"){
                    var currentValue = document.getElementsByName('additionalInfoForm['+index+'].columnValue'); 
                    if( currentValue != null && currentValue.length > 0 ){
                        currentValue[0].value = result["ORGANIZATION_ID"];
                    }
                    var currentDesc = document.getElementsByName('additionalInfoForm['+index+'].description');   
                        if( currentDesc != null && currentDesc.length > 0 ){
                        currentDesc[0].value = result["ORGANIZATION_NAME"];
                    }
                }else if(searchType == "SPONSORSEARCH"){
                    var currentValue = document.getElementsByName('additionalInfoForm['+index+'].columnValue'); 
                    if( currentValue != null && currentValue.length > 0 ){
                        currentValue[0].value = result["SPONSOR_CODE"];
                    }
                    var currentDesc = document.getElementsByName('additionalInfoForm['+index+'].description');   
                    if( currentDesc != null && currentDesc.length > 0 ){
                        currentDesc[0].value = result["SPONSOR_NAME"];
                    }
                }else if(searchType == "ROLODEXSEARCH"){     
                    var currentValue = document.getElementsByName('additionalInfoForm['+index+'].columnValue');   
                    if( currentValue != null && currentValue.length > 0 ){
                        currentValue[0].value = result["ROLODEX_ID"];       
                    }
                    var name = '';
                    if(result["LAST_NAME"]!="null" && result["LAST_NAME"]!= undefined){
                        name=result["LAST_NAME"]+", ";
                    }
                    if(result["FIRST_NAME"]!="null" && result["FIRST_NAME"]!= undefined){
                        name+=result["FIRST_NAME"]+" ";
                    }
                    if(result["MIDDLE_NAME"]!="null" && result["MIDDLE_NAME"]!= undefined){
                        name+=result["MIDDLE_NAME"];
                    }
                    var currentDesc = document.getElementsByName('additionalInfoForm['+index+'].description'); 
                    if(currentDesc != null &&  currentDesc.length > 0){
                        currentDesc[0].value =name;        
                    }     
                }

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
    
            function put_Data(listCode,listDesc) {
                dataChanged();
                var currentValue = document.getElementsByName('additionalInfoForm['+index+'].columnValue');
                currentValue[0].value = listCode;
                var currentDesc = document.getElementsByName('additionalInfoForm['+index+'].description'); 
                currentDesc[0].value = listDesc;
            }
            
            function valueChanged(clicked){           
                if(clicked == true || clicked == 'true'){            
                    dataChanged();
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
                CLICKED_LINK = "<%=request.getContextPath()%>"+procLink;
                if(validate()){
                    document.iacucProtoStudyGroupsDynaBeansList.action = CLICKED_LINK;
                    document.iacucProtoStudyGroupsDynaBeansList.submit();
                }
            }
            
            function loadLocationIdForLocType(form,studyGroupId, studyGroupLocationId, locationIndex){
                loadLocationForLocType(studyGroupId, studyGroupLocationId, locationIndex, form.value);
            }
            function loadLocationForLocType(studyGroupId, studyGroupLocationId, locationIndex, locationTypeCode){
                valueChange('locations['+locationIndex+']');
                document.iacucProtoStudyGroupsDynaBeansList.action = "<%=request.getContextPath()%>/loadLocationForLocType.do?studyGroupId="+studyGroupId+"&studyGroupLocationId="+studyGroupLocationId+"&locationTypeCode="+locationTypeCode;
                document.iacucProtoStudyGroupsDynaBeansList.submit();  
            }

            function removeLocation(studyGroupId, studyGroupLoctaionId, speciesId){
                if(confirm('<bean:message bundle="iacuc" key="studyGroup.removeLocationConfirmation"/>')){          
                    dataChanged();
                    document.iacucProtoStudyGroupsDynaBeansList.action = "<%=request.getContextPath()%>/removeStudyGroupLocation.do?studyGroupId="+studyGroupId+"&studyGroupLoctaionId="+studyGroupLoctaionId+"&speciesId="+speciesId;
                    document.iacucProtoStudyGroupsDynaBeansList.submit();             
                }
            }
            
            function addLocation(studyGroupId, speciesId){
              document.iacucProtoStudyGroupsDynaBeansList.action = "<%=request.getContextPath()%>/addStudyGroupLocation.do?studyGroupId="+studyGroupId+"&speciesId="+speciesId;
              document.iacucProtoStudyGroupsDynaBeansList.submit();
            }
            
            function removeProcedure(speciesId, studyGroupId){
                if(confirm('<bean:message bundle="iacuc" key="studyGroup.removeProcedureConfirmation"/>')){
                    dataChanged();
                    document.iacucProtoStudyGroupsDynaBeansList.action = "<%=request.getContextPath()%>/removeStudyGroup.do?speciesId="+speciesId+"&studyGroupId="+studyGroupId;
                    document.iacucProtoStudyGroupsDynaBeansList.submit();
                }
            }
            
            function hideShowAllProcedureDetails(showHide){
                var showHide = 'none';
                if(showHide == 'SHOW'){
                    showHide = 'block';
                }
                var procedureElem = document.getElementsByTagName("div");
                for(var i = 0; i < procedureElem.length; i++) {
                    var id = procedureElem[i].id;
                    var locationReg = new RegExp("Locations");
                    var personsReg = new RegExp("Persons");
                    var additionalInfoReg = new RegExp("AdditionalInfo");
                    if(locationReg.exec(id) != null){
                        procedureElem[i].style.display = showHide;
                    }

                    if(personsReg.exec(id) != null){
                        procedureElem[i].style.display = showHide;
                    }

                    if(additionalInfoReg.exec(id) != null){
                        procedureElem[i].style.display = showHide;
                    }

                }
            }
            function saveProcedureFromList(speciesId){
                document.iacucProtoStudyGroupsDynaBeansList.action = "<%=request.getContextPath()%>/saveSelectedProceduresToGroup.do?speciesId="+speciesId+"&reloadFromSelect="+true;
                                                                                                                                    
                document.iacucProtoStudyGroupsDynaBeansList.submit();
            }

        </script>
<script language="javascript" type="text/JavaScript"
	src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/coeusLiteCalendar.js">          </script>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>
<% 
    String completeImage= request.getContextPath()+"/coeusliteimages/complete.gif";
    String noneImage= request.getContextPath()+"/coeusliteimages/none.gif";
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
	<html:form action="/saveStudyGroups" method="post">
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
								style="<%=currentTabStyle%>" class="tabbghl"
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
								style="<%=remainingTabStyle%>" class="tabbghl"
								onclick="activateTab('VIEWALL')"><b><bean:message
										bundle="iacuc" key="studyGroup.viewAllTabName" /></b></td>
							<td width="20%">&nbsp;</td>
						</tr>

					</table>
				</td>
			</tr>
			<%if(!modeValue){%>
			<tr>
				<td>
					<table cellspacing="1" cellpadding="0" border="0" align="center"
						width="99%" class="tabtable">
						<logic:notEmpty name="vecAddedSpeciesData" scope="session">
							<tr class="copybold">
								<td><bean:message bundle="iacuc"
										key="procedure.addProcedure.msg" /></td>
							</tr>
						</logic:notEmpty>
					</table>
				</td>
			</tr>
			<%}%>
			<logic:empty name="vecAddedSpeciesData" scope="session">
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr class="copy">
					<td>
						<table width="99%" border="0" cellpadding="3" cellspacing="4"
							class="tabtable" align="center">
							<tr class="copy">
								<td><bean:message bundle="iacuc"
										key="studyGroup.noSpeciesMsg" /></td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
			</logic:empty>

			<logic:notEmpty name="vecAddedSpeciesData" scope="session">
				<tr class='copy' align="left">
					<td>&nbsp; <font color="red"> <logic:messagesPresent>
								<html:errors header="" footer="" />
								<script>errValue = true;</script>
							</logic:messagesPresent> <logic:messagesPresent message="true">
								<script>errValue = true;</script>
								<html:messages id="message" message="true" bundle="iacuc"
									property="groupIsRequired">
									<script>errLock = true;</script>
									<li><bean:write name="message" /></li>
								</html:messages>
								<html:messages id="message" message="true" bundle="iacuc"
									property="procedureCategoryCodeRequired">
									<script>errLock = true;</script>
									<li><bean:write name="message" /></li>
								</html:messages>
								<html:messages id="message" message="true" bundle="iacuc"
									property="procedureCodeRequired">
									<script>errLock = true;</script>
									<li><bean:write name="message" /></li>
								</html:messages>
								<html:messages id="message" message="true" bundle="iacuc"
									property="painCategoryCodeRequired">
									<script>errLock = true;</script>
									<li><bean:write name="message" /></li>
								</html:messages>
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
								<html:messages id="message" message="true" bundle="iacuc"
									property="invalidDate">
									<script>errLock = true;</script>
									<li><bean:write name="message" /></li>
								</html:messages>
								<html:messages id="message" message="true" bundle="iacuc"
									property="invalidNumber">
									<script>errLock = true;</script>
									<li><bean:write name="message" /></li>
								</html:messages>

								<html:messages id="message" message="true" bundle="iacuc"
									property="overviewTimelineLengthExceed">
									<script>errLock = true;</script>
									<li><bean:write name="message" /></li>
								</html:messages>
								<html:messages id="message" message="true">
									<!-- If lock is deleted then show this message -->
									<html:messages id="message" message="true" property="errMsg">
										<script>errLock = true;</script>
										<li><bean:write name="message" /></li>
									</html:messages>
								</html:messages>
							</logic:messagesPresent>

					</font>
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>
						<table width="99%" border="0" cellpadding="3" cellspacing="4"
							class="tabtable" align="center">


							<logic:present name="vecAddedSpeciesData" scope="session">
								<logic:iterate id="speciesGroupForm" name="vecAddedSpeciesData"
									type="org.apache.struts.action.DynaActionForm" indexId="index"
									scope="session">
									<tr class="copy">
										<td width="1%">
											<div id="<%=("plus"+speciesGroupForm.get("speciesId"))%>">
												<%String showDivlink = "javascript:showProcedures('"+speciesGroupForm.get("speciesId")+"')";%>
												<html:link href="<%=showDivlink%>">
													<img border="0"
														src="<bean:write name='ctxtPath'/>/coeusliteimages/plus.gif">
												</html:link>
											</div>
											<div id="<%=("minus"+speciesGroupForm.get("speciesId"))%>">
												<%String hideDivlink = "javascript:hideProcedures('"+speciesGroupForm.get("speciesId")+"')";%>
												<html:link href="<%=hideDivlink%>">
													<img border="0"
														src="<bean:write name='ctxtPath'/>/coeusliteimages/minus.gif">
												</html:link>
											</div>
										</td>
										<td width="2%" class="copybold" align="right"><bean:message
												bundle="iacuc" key="label.groupSpecies.group" />:</td>
										<td width="25%" align="left"><bean:write
												name="speciesGroupForm" property="groupName" /></td>
										<td width="2%" class="copybold" align="right"><bean:message
												bundle="iacuc" key="label.groupSpecies.species" />:</td>
										<td width="25%" align="left"><bean:write
												name="speciesGroupForm" property="speciesName" /></td>
										<td width="15%" class="copybold" align="right"><bean:message
												bundle="iacuc" key="label.groupSpecies.totalCount" />:</td>
										<td width="8%" align="left"><bean:write
												name="speciesGroupForm" property="speciesCount" /></td>
										<td align="center">
											<%
                                String speciesId = speciesGroupForm.get("speciesId").toString();
                                if(!modeValue){
                                    String showProceduresLink = "javaScript:openProceduresSelection('"+speciesId+"')"; %>
											<html:link href="<%=showProceduresLink%>">
												<u><bean:message bundle="iacuc"
														key="label.groupSpecies.addProcedures" /></u>
											</html:link> <%}%>
										</td>

									</tr>
									<tr>
										<td colspan="8">
											<%int trainingIndex = 0;
                        int procedureIndex = 0;%>
											<div id="<%=("proceduresForSpecies"+speciesId)%>">

												<table width="98%" border="0" cellpadding="1"
													cellspacing="0" class="tabtable" align="center">
													<tr class="tableheader">
														<td width="25%"><bean:message bundle="iacuc"
																key="studyGroup.label.ProcedureCategory" /></td>
														<td width="25%"><bean:message bundle="iacuc"
																key="studyGroup.label.Procedure" /></td>

														<td width="10%"><bean:message bundle="iacuc"
																key="studyGroup.label.Count" /></td>
														<td></td>
													</tr>
													<logic:present name="iacucProtoStudyGroupsDynaBeansList"
														property="list" scope="session">
														<logic:iterate id="procedures"
															name="iacucProtoStudyGroupsDynaBeansList" property="list"
															type="org.apache.struts.action.DynaActionForm"
															scope="session">
															<logic:notEqual name="procedures" property="acType"
																value="D">
																<%
                                        String speciesIdInProcedure = procedures.get("speciesId").toString();
                                        int procStudyGroupId = ((Integer)procedures.get("studyGroupId")).intValue();
                                        
                                        if(speciesId.equals(speciesIdInProcedure)){
                                        %>


																<tr>
																	<td><html:text name="procedures" size="30"
																			indexed="true" styleClass="copy" readonly="true"
																			property="procedureCategoryDesc" disabled="true" /></td>
																	<td><html:text name="procedures" size="30"
																			indexed="true" styleClass="copy" readonly="true"
																			property="procedureDesc" disabled="true" /></td>
																	<td>
																		<%String countChangeLink = "javaScript:valueChange('procedures["+procedureIndex+"]')";%>
																		<html:text name="procedures" size="6" indexed="true"
																			styleClass="copy" property="count" styleId="count"
																			maxlength="8" disabled="<%=modeValue%>"
																			onchange="<%=countChangeLink%>" />
																	</td>

																	<td>
																		<%if(!modeValue){
                                                String removeProcedure = "javascript:removeProcedure('"+speciesId+"','"+procStudyGroupId+"')";%>
																		<html:link href="<%=removeProcedure%>">
																			<u><bean:message bundle="iacuc"
																					key="studyGroup.label.remove" /></u>
																		</html:link> <%}%> <%String divIdForShowLoca =  "species"+speciesId+"Procedure"+procStudyGroupId;
                                                String showLocationLink = "javascript:showLocations('"+divIdForShowLoca+"')";%>
																		&nbsp; <html:link href="<%=showLocationLink%>">
																			<u><bean:message bundle="iacuc"
																					key="studyGroup.label.Locations" /></u>
																		</html:link> <%String divIdForShowperson =  "species"+speciesId+"Procedure"+procStudyGroupId;
                                                String showPersonLink = "javascript:showPersons('"+divIdForShowperson+"')";%>
																		<% boolean hasPerson = false;%> <logic:present
																			name="iacucProtoStudyGroupsDynaBeansList"
																			property="valueList" scope="session">
																			<logic:iterate id="personResponsible"
																				name="iacucProtoStudyGroupsDynaBeansList"
																				property="valueList"
																				type="org.apache.struts.action.DynaActionForm"
																				scope="session">
																				<logic:equal name="personResponsible"
																					property="studyGroupId"
																					value="<%=String.valueOf(procStudyGroupId)%>">
																					<%hasPerson = true;%>
																				</logic:equal>
																			</logic:iterate>
																		</logic:present> <%if(hasPerson){%> &nbsp; <html:link
																			href="<%=showPersonLink%>">
																			<u><bean:message bundle="iacuc"
																					key="studyGroup.label.persons" /></u>
																		</html:link> <%}%> <%String divIdForShowAdditInfo =  "species"+speciesId+"Procedure"+procStudyGroupId;
                                                String showAdditInfoLink = "javascript:showAdditionalInfo('"+divIdForShowAdditInfo+"')";%>

																		<% boolean hasAdditionalInfo = false;%> <logic:present
																			name="iacucProtoStudyGroupsDynaBeansList"
																			property="infoList" scope="session">
																			<logic:iterate id="additionalInformation"
																				name="iacucProtoStudyGroupsDynaBeansList"
																				property="infoList"
																				type="org.apache.struts.action.DynaActionForm"
																				scope="session">
																				<logic:equal name="additionalInformation"
																					property="studyGroupId"
																					value="<%=String.valueOf(procStudyGroupId)%>">
																					<%hasAdditionalInfo = true;%>
																				</logic:equal>
																			</logic:iterate>
																		</logic:present> <%if(hasAdditionalInfo){%> &nbsp; <html:link
																			href="<%=showAdditInfoLink%>">
																			<u><bean:message bundle="iacuc"
																					key="studyGroup.label.AdditionalInformation" /></u>
																		</html:link> <%}%>

																	</td>
																</tr>
																<tr>
																	<td colspan="4">
																		<%boolean locationHeaderAdded = false;%>
																		<div
																			id="<%=("species"+speciesId+"Procedure"+procStudyGroupId+"Locations")%>">
																			<table width="99%" class="table" border="0">
																				<%String addLocationLink = "javaScript:addLocation("+procedures.get("studyGroupId")+","+speciesId+")";                                      
                                                        if(!modeValue){
                                                        if(!locationHeaderAdded){%>
																				<tr class="tableheader">
																					<td><bean:message bundle="iacuc"
																							key="studyGroup.label.Locations" /></td>
																					<td align="right">
																						<%String divIdForPerson = "species"+speciesId+"Procedure"+procStudyGroupId;
                                                                String hidePersonLink = "javascript:hideLocations('"+divIdForPerson+"')";%>
																						<html:link href="<%=hidePersonLink%>">
																							<u><bean:message bundle="iacuc"
																									key="studyGroup.label.hideLocation" /></u>
																						</html:link>
																					</td>
																				</tr>
																				<%}
                                                        if(!modeValue){%>
																				<tr>
																					<td colspan="4" class="tabtable"><html:link
																							href="<%=addLocationLink%>">
																							<u><bean:message bundle="iacuc"
																									key="studyGroup.label.AddLocation" /></u>
																						</html:link></td>
																				</tr>
																				<%}%>
																				<%}%>
																				<%
                                                        int locationCount = 0;
                                                        int vecStudyLocationsDataIndex = -1;
                                                        %>
																				<logic:present
																					name="iacucProtoStudyGroupsDynaBeansList"
																					property="beanList" scope="session">
																					<logic:iterate id="locations"
																						name="iacucProtoStudyGroupsDynaBeansList"
																						property="beanList"
																						type="org.apache.struts.action.DynaActionForm"
																						indexId="locationIndex">
																						<logic:notEqual name="locations" property="acType"
																							value="D">
																							<%
                                                                    vecStudyLocationsDataIndex ++;
                                                                    Integer locStudyGroupId = (Integer) locations.get("studyGroupId");
                                                                    Integer dynaStudyGroupId = (Integer) procedures.get("studyGroupId");
                                                                    Integer procCategoryCode = (Integer) procedures.get("procedureCategoryCode");
                                                                    if(locations != null && dynaStudyGroupId.equals(locStudyGroupId)){
                                                                        locationCount++;
                                                                        Integer studyGroupLocationId = (Integer) locations.get("studyGroupLoctaionId");
                                                                        Integer code  = (Integer) locations.get("locationTypeCode");
                                                                        if(!locationHeaderAdded){
                                                                            locationHeaderAdded = true;
                                                                    %>
																							<tr>
																								<td class="tableheader" colspan="4"><bean:message
																										bundle="iacuc"
																										key="studyGroup.label.Locations" /></td>
																							</tr>
																							<%}%>
																							<tr>
																								<td class="tabtable" colspan="4">

																									<table border="0" width="100%" class="copybold"
																										cellspacing="1" cellpadding="1">
																										<tr>
																											<td width="13%" class="copybold"
																												align="right"><font color="red">*</font>
																												<bean:message bundle="iacuc"
																													key="studyGroup.label.LocationType" />:</td>
																											<%
                                                                                    String locationTypeCode = "0";
                                                                                    
                                                                                    if(locations.get("locationTypeCode") != null){
                                                                                        locationTypeCode = String.valueOf(locations.get("locationTypeCode"));
                                                                                    }
                                                                                    
                                                                                    String locationChangeLink = "javaScript:valueChange('locations["+locationIndex+"]')";
                                                                                    String locTypeChangeLink = "javascript:loadLocationIdForLocType(this,'"+dynaStudyGroupId+"','"+studyGroupLocationId+"','"+locationIndex+"')";%>
																											<td width="18%" class="copybold" align="left">
																												<html:select style="width:210px;"
																													property="locationTypeCode"
																													styleId="locationTypeCode"
																													styleClass="textbox-long"
																													disabled="<%=modeValue%>" name="locations"
																													indexed="true"
																													onchange="<%=locTypeChangeLink%>">
																													<html:option value="">
																														<bean:message
																															key="generalInfoLabel.pleaseSelect" />
																													</html:option>
																													<html:options
																														collection="vecAcLocationTypes"
																														property="code"
																														labelProperty="description" />
																												</html:select>
																											</td>
																											<td width="3%" class="copybold">
																												<%if(!modeValue){%> <%String urllink="javaScript:openLookupWindow('locationType','Location Type','"+locationTypeCode+"','"+vecStudyLocationsDataIndex+"', '"+procStudyGroupId+"','"+studyGroupLocationId+"','"+locationIndex+"')";%>
																												<html:link href="<%=urllink%>">
																													<u><bean:message bundle="iacuc"
																															key="label.invesKeypersons.search" /></u>
																												</html:link> <%}%>
																											</td>
																											<% request.setAttribute("vecAcLocationNames", (Vector)locations.get("vecAcLocationNames"));%>
																											<td width="13%" class="copybold"
																												align="right"><font color="red">*</font>
																												<bean:message bundle="iacuc"
																													key="studyGroup.label.LocationName" />:</td>

																											<td width="18%" class="copybold" align="left">
																												<html:select style="width:210px;"
																													property="locationId" styleId="locationId"
																													styleClass="textbox-long" name="locations"
																													indexed="true"
																													onchange="<%=locationChangeLink%>"
																													disabled="<%=modeValue%>">
																													<html:option value="">
																														<bean:message
																															key="generalInfoLabel.pleaseSelect" />
																													</html:option>
																													<html:options
																														collection="vecAcLocationNames"
																														property="code"
																														labelProperty="description" />
																												</html:select>
																											</td>
																											<td width="3%" class="copybold">
																												<%if(!modeValue){%> <%String link = "javaScript:openLookupWindow('locationName','Location Name','"+locationTypeCode+"','"+vecStudyLocationsDataIndex+"','','','')";%>
																												<html:link href="<%=link%>">
																													<u><bean:message bundle="iacuc"
																															key="label.invesKeypersons.search" /></u>
																												</html:link> <%}%>
																											</td>
																										</tr>
																										<tr>
																											<td width="8%" class="copybold" align="right">
																												&nbsp;&nbsp; <bean:message bundle="iacuc"
																													key="studyGroup.label.Room" />:
																											</td>
																											<td width="20%" class="copybold"><html:text
																													property="studyGroupLocationRoom"
																													style="width:210px;"
																													styleClass="textbox-long" maxlength="60"
																													name="locations" indexed="true"
																													onchange="<%=locationChangeLink%>"
																													disabled="<%=modeValue%>" /></td>
																											<td width="8%" class="copybold"></td>

																											<td width="8%" class="copybold" align="right">
																												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <bean:message
																													bundle="iacuc"
																													key="studyGroup.label.Description" />:
																											</td>
																											<td width="20%" class="copybold"><html:text
																													property="studyGroupLocationDesc"
																													style="width:210px;"
																													styleClass="textbox-long" maxlength="60"
																													name="locations" indexed="true"
																													onchange="<%=locationChangeLink%>"
																													disabled="<%=modeValue%>" /></td>
																											<td width="8%" class="copybold">
																												<%if(!modeValue){%> <% String removeLocationLink = "javaScript:removeLocation("+dynaStudyGroupId+","+studyGroupLocationId+","+speciesId+");";%>
																												<html:link href="<%=removeLocationLink%>">
																													<u><bean:message bundle="iacuc"
																															key="studyGroup.label.remove" /></u>
																												</html:link> <%}%>
																											</td>
																										</tr>
																									</table>
																								</td>

																							</tr>
																							<html:hidden property="acType" name="locations"
																								indexed="true" />
																							<html:hidden property="speciesId"
																								name="locations" indexed="true"
																								value="<%=speciesId%>" />
																							<html:hidden property="studyGroupId"
																								name="locations" indexed="true" />
																							<%
                                                                    }
                                                                    %>
																						</logic:notEqual>
																					</logic:iterate>
																				</logic:present>
																				<%if(locationCount == 0){%>
																				<tr class="tabtable">
																					<td colspan="5"><bean:message bundle="iacuc"
																							key="studyGroup.noLocationMsg" /></td>
																				</tr>
																				<%}%>

																			</table>
																		</div>
																		<div
																			id="<%=("species"+speciesId+"Procedure"+procStudyGroupId+"Persons")%>">
																			<table width="99%" border="0" cellpadding="2"
																				cellspacing="0" class="tabtable" align="center">
																				<tr class="tableheader">
																					<td colspan="3"><bean:message bundle="iacuc"
																							key="studyGroup.label.ProcedurePersonnel" /></td>
																					<td align="right">
																						<%String divIdForPerson = "species"+speciesId+"Procedure"+procStudyGroupId;
                                                                String hidePersonLink = "javascript:hidePersons('"+divIdForPerson+"')";%>
																						<html:link href="<%=hidePersonLink%>">
																							<u><bean:message bundle="iacuc"
																									key="studyGroup.label.hidePersons" /></u>
																						</html:link>
																					</td>
																				</tr>

																				<tr class="copybold">
																					<td colspan="2" width="50%"><bean:message
																							bundle="iacuc"
																							key="studyGroup.label.InvestigatorsStudyPersonnel" />
																					</td>

																					<td width="10%"><bean:message bundle="iacuc"
																							key="studyGroup.label.Trained" /></td>

																				</tr>
																				<%int personIndex = 0;
                                                        int personCount = 0;
                                                        %>
																				<logic:present
																					name="iacucProtoStudyGroupsDynaBeansList"
																					property="valueList" scope="session">
																					<logic:iterate id="persons"
																						name="iacucProtoStudyGroupsDynaBeansList"
																						property="valueList"
																						type="org.apache.struts.validator.DynaValidatorForm"
																						scope="session">
																						<logic:notEqual name="persons" property="acType"
																							value="D">
																							<%
                                                                    int personStudyGroupId = ((Integer)persons.get("studyGroupId")).intValue();
                                                                    String personId = (String)persons.get("personId");
                                                                    String personName = (String)persons.get("personName");
                                                                    
                                                                    if(procStudyGroupId == personStudyGroupId){
                                                                        personCount++;
                                                                    %>
																							<tr>
																								<td colspan="2"><bean:write name="persons"
																										property="personName" /></td>

																								<td>
																									<%
                                                                            if("Y".equals(persons.get("isTrained").toString())){
                                                                            %>
																									<html:img src="<%=completeImage%>" border="0" />
																									<%}else{%> <html:img src="<%=noneImage%>"
																										border="0" /> <%}%>
																								</td>
																								<td>
																									<%String trainingDetailLink = "javaScript:showTrainingDetails('"+personId+"','"+personName+"')";%>
																									<html:link href="<%=trainingDetailLink%>">
																										<u><bean:message bundle="iacuc"
																												key="studyGroup.label.TrainingDetails" /></u>
																									</html:link> <%if(!modeValue){%> &nbsp;&nbsp; <%String removeLink = "javaScript:removePersonResponsible('"+personStudyGroupId+"','"+personId+"')";%>
																									<html:link href="<%=removeLink%>">
																										<u><bean:message bundle="iacuc"
																												key="studyGroup.label.remove" /></u>
																									</html:link> <%}%>

																								</td>

																							</tr>
																							<%
                                                                    trainingIndex++;
                                                                    personIndex++;
                                                                    }%>
																						</logic:notEqual>
																					</logic:iterate>
																				</logic:present>
																				<%if(personCount == 0){%>
																				<tr class="copy">
																					<td><bean:message bundle="iacuc"
																							key="studyGroup.noPersonMsg" /></td>
																				</tr>
																				<%}%>

																			</table>
																		</div> <%int addInfoCount = 0;%>
																		<div
																			id="<%=("species"+speciesId+"Procedure"+procStudyGroupId+"AdditionalInfo")%>">
																			<table width="99%" border="0" cellpadding="3"
																				cellspacing="4" class="tabtable" align="center">
																				<tr class="tableheader">
																					<td colspan="3"><bean:message bundle="iacuc"
																							key="studyGroup.label.AdditionalInformation" /></td>
																					<td align="right">
																						<%String divIdForAdditionalInfo = "species"+speciesId+"Procedure"+procStudyGroupId;
                                                                String hideAddInfoLink = "javascript:hideAdditionalInfo('"+divIdForAdditionalInfo+"')";%>
																						<html:link href="<%=hideAddInfoLink%>">
																							<u><bean:message bundle="iacuc"
																									key="studyGroup.label.hideAdditionalInformation" /></u>
																						</html:link>
																					</td>
																				</tr>
																				<tr class="tabtable">
																					<td colspan="4">
																						<table width="99%" cellspacing="2" cellpadding="2"
																							border="0">
																							<logic:present
																								name="iacucProtoStudyGroupsDynaBeansList"
																								property="infoList" scope="session">
																								<logic:iterate id="additionalInfoForm"
																									name="iacucProtoStudyGroupsDynaBeansList"
																									property="infoList"
																									type="org.apache.struts.action.DynaActionForm"
																									indexId="customDataIndex">
																									<% 
                                                                            Integer custDataStudyGroupId = (Integer)additionalInfoForm.get("studyGroupId");
                                                                            Integer procCatCode =  (Integer)additionalInfoForm.get("procedureCategoryCode");
                                                                            Integer studyGroupId = (Integer) procedures.get("studyGroupId");
                                                                            Integer dynaProcCatCode = (Integer)procedures.get("procedureCategoryCode");
                                                                            session.getAttribute("iacucProtoStudyGroupsDynaBeansList");
                                                                            if( dynaProcCatCode!=null && dynaProcCatCode.equals(procCatCode) &&  studyGroupId.equals(custDataStudyGroupId)){
                                                                                addInfoCount++;
                                                                                String hasLookup = (String)additionalInfoForm.get("hasLookUp");
                                                                                String lookUpWindow = (String)additionalInfoForm.get("lookUpWindow");
                                                                                String dataLength = ((String)additionalInfoForm.get("dataLength"));
                                                                                String lookUpValue = (String)additionalInfoForm.get("lookUpValue");
                                                                                String dataType = (String)additionalInfoForm.get("dataType");
                                                                                String argumentName = (String)additionalInfoForm.get("lookUpArgument");
                                                                                String clicked = "javascript:valueChanged('true')";
                                                                                boolean isPresent = false ;
                                                                                boolean isDisabled = false ;
                                                                                if(hasLookup!=null && !hasLookup.equals("")){
                                                                                    if(hasLookup.equalsIgnoreCase("Y")){
                                                                                        if(lookUpWindow!=null && !lookUpWindow.equals("")){
                                                                                            isPresent = true;
                                                                                            isDisabled = true;
                                                                                        }
                                                                                    }
                                                                                }
                                                                            %>
																									<tr>
																										<td class="copybold" align="right"
																											valign="top" width="20%"><bean:write
																												name="additionalInfoForm"
																												property="columnLabel" />:</td>
																										<td width="80%">
																											<% if(dataType != null && !dataType.equals("")){
                                                                                    if(dataType.equals("NUMBER")){%>

																											<html:text property="columnValue"
																												name="additionalInfoForm"
																												maxlength="<%=dataLength%>" indexed="true"
																												readonly="<%=isDisabled%>"
																												disabled="<%=modeValue%>"
																												onchange="<%=clicked%>" /> <% }else if(dataType.equals("DATE")){%>

																											<html:text property="columnValue"
																												name="additionalInfoForm" indexed="true"
																												size="20" maxlength="10"
																												disabled="<%=modeValue%>"
																												onchange="dataChanged()" /> <%if(dataType.equals("DATE")){
                                                                                    String calender ="javascript:displayCalendarWithTopLeft('additionalInfoForm["+customDataIndex+"].columnValue',8,25)";
                                                                                    %>
																											<%if(!modeValue){%> <html:link
																												href="<%=calender%>" onclick="dataChanged()">
																												<html:img src="<%=calImage%>" border="0"
																													height="16" width="16" />
																											</html:link> <%}%> <%}
                                                                                    %>

																											<%}else{
                                                                                    int intDataLength =0;
                                                                                    if(dataLength != null){
                                                                                    intDataLength = Integer.valueOf(dataLength);
                                                                                    }
                                                                                    if(intDataLength < 15){
                                                                                    %>

																											<html:text property="columnValue"
																												name="additionalInfoForm"
																												maxlength="<%=dataLength%>" indexed="true"
																												readonly="<%=isDisabled%>"
																												disabled="<%=modeValue%>"
																												onchange="<%=clicked%>" /> <%} else {%> <%if(!isPresent){ %>
																											<html:textarea property="columnValue"
																												name="additionalInfoForm"
																												style="width:400px;" indexed="true"
																												readonly="<%=isDisabled%>"
																												disabled="<%=modeValue%>"
																												onchange="<%=clicked%>" styleClass="copy"
																												rows="3" /> <%}else{%> <html:text
																												property="columnValue"
																												name="additionalInfoForm"
																												maxlength="<%=dataLength%>" indexed="true"
																												disabled="<%=modeValue%>"
																												onchange="<%=clicked%>" /> <%}%> <%}}}%>
																											&nbsp;&nbsp; <% 
                                                                                    if(!modeValue){
                                                                                    if(isPresent){
                                                                                        String image= request.getContextPath()+"/coeusliteimages/search.gif";
                                                                                        String pageUrl="javaScript:openLookupWindow('"+lookUpWindow+"','"+lookUpValue+"','"+argumentName+"','"+customDataIndex+"','','','')";%>
																											<html:link href="<%=pageUrl%>">
																												<u><bean:message key="label.search" /></u>
																											</html:link> <%}}
                                                                                    %>

																											<html:text property="description"
																												name="additionalInfoForm" maxlength="2000"
																												styleClass="cltextbox-color"
																												style="width:250px" indexed="true"
																												readonly="true" />
																										</td>

																									</tr>
																									<%}
                                                                            
                                                                            %>
																									<html:hidden property="studyGroupId"
																										name="additionalInfoForm" indexed="true" />
																									<html:hidden property="columnName"
																										name="additionalInfoForm" indexed="true" />
																								</logic:iterate>
																							</logic:present>
																						</table>
																					</td>

																				</tr>
																				<%if(addInfoCount == 0){%>
																				<tr class="copy">
																					<td colspan="4"><bean:message bundle="iacuc"
																							key="studyGroup.noadditionalInformationMsg" /></td>
																				</tr>
																				<%}%>
																			</table>

																		</div>

																	</td>
																</tr>
																<html:hidden name="procedures" property="acType"
																	indexed="true" />
																<html:hidden name="procedures" property="speciesId"
																	indexed="true" />
																<%procedureIndex++;%>
																<%}%>
															</logic:notEqual>
														</logic:iterate>
													</logic:present>
													<%if(procedureIndex == 0){%>
													<tr class="copy">
														<td colspan="5"><bean:message bundle="iacuc"
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
			</logic:notEmpty>
			<tr>
				<td>
					<table width="100%" height="20" border="0" cellpadding="0"
						cellspacing="0" class="tableheader">
						<tr>
							<td style="font-family: Arial, Helvetica, sans-serif;";><bean:message
									bundle="iacuc" key="iacuc.speciesGroups.lblOverviewTimeline" /></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table width="100%" border="0" align="center" cellpadding="6"
						cellspacing="0" class="tabtable">
						<tr>
							<logic:present name="iacucProtoStudyGroupsDynaBeansList"
								property="dataList">
								<logic:iterate id="overViewTimeLineForm"
									name="iacucProtoStudyGroupsDynaBeansList" property="dataList"
									type="org.apache.struts.action.DynaActionForm" scope="session">

									<td width="50%" class="copybold"><html:textarea
											property="overviewTimeline" name="overViewTimeLineForm"
											onblur="" styleClass="copy" indexed="true" cols="141"
											rows="4" readonly="<%=modeValue%>"
											onchange="valueChange('overViewTimeLineForm[0]')" /></td>
									<html:hidden name="overViewTimeLineForm" property="acType"
										indexed="true" />
								</logic:iterate>
							</logic:present>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
			<%if(!modeValue){%>
			<tr align="right" class="theader" height='30'>
				<td align='left' colspan="5">&nbsp; <html:button
						property="Save" value="Save" onclick="saveStudyGroup()"
						styleClass="clsavebutton" />
				</td>
			</tr>
			<%}%>
		</table>
	</html:form>
</body>

<script>
    <%
    String speciesIdToLoadProce  = (String)request.getAttribute("GroupToLoad");
    if(vecAddedSpeciesData != null && !vecAddedSpeciesData.isEmpty()){
        for(Object speciesDetails : vecAddedSpeciesData){
            DynaActionForm speciesDetailForm = (DynaActionForm)speciesDetails;
            String speciesId = speciesDetailForm.get("speciesId").toString();
            if(speciesIdToLoadProce != null && speciesIdToLoadProce.equals(speciesId)){
    %>
                document.getElementById("plus"+<%=speciesId%>).style.display = 'none';
                document.getElementById("minus"+<%=speciesId%>).style.display = 'block';
                document.getElementById("proceduresForSpecies"+<%=speciesId%>).style.display = 'block';

            <%}else{%>
    
                document.getElementById("plus"+<%=speciesId%>).style.display = 'block';
                document.getElementById("minus"+<%=speciesId%>).style.display = 'none';
                document.getElementById("proceduresForSpecies"+<%=speciesId%>).style.display = 'none';
            <%}%>            
        
        <%}
     }
    
    String showHideAllProcedures = (String)request.getAttribute("HIDE_SHOW_ALL_PROCEDURES");
    if(showHideAllProcedures != null && showHideAllProcedures.length()>0){%>
        hideShowAllProcedureDetails('HIDE');
    <%}
    String locationDivOpen = (String)request.getAttribute("OPEN_LOCATION_DIV");
    String addiInfoDiv = (String)request.getAttribute("OPEN_ADDITIONAL_INFO_DIV");
    if((locationDivOpen != null && locationDivOpen.length() > 0) ||
            addiInfoDiv != null && addiInfoDiv.length() > 0){
    %>
 
    var procedureElem = document.getElementsByTagName("div");
    for(var i = 0; i < procedureElem.length; i++) {
        var id = procedureElem[i].id;
        var locationReg = new RegExp("Locations");
        var personsReg = new RegExp("Persons");
        var additionalInfoReg = new RegExp("AdditionalInfo");
        if(locationReg.exec(id) != null){
            procedureElem[i].style.display = 'none';
        }
        
        if(personsReg.exec(id) != null){
            procedureElem[i].style.display = 'none';
        }
        
        if(additionalInfoReg.exec(id) != null){
            procedureElem[i].style.display = 'none';
        }

    }
        <%if(locationDivOpen != null && locationDivOpen.length() > 0){%>
        showLocations('<%=locationDivOpen%>');
        <%}%>
        <%if(addiInfoDiv != null && addiInfoDiv.length() > 0){%>
        showAdditionalInfo('<%=addiInfoDiv%>');
        <%}%>
    
    <%}%> 
    DATA_CHANGED = 'false';
    LINK = "<%=request.getContextPath()%>/saveStudyGroups.do";
    FORM_LINK = document.iacucProtoStudyGroupsDynaBeansList;
    PAGE_NAME = "<bean:message key="studyGroups.procedures" bundle="iacuc"/>";
    var isDataModified = '<%=request.getAttribute("isDataModified")%>';
    if(isDataModified == 'Y' || (errValue && !errLock)){
        dataChanged();
    }
    
    function valueChange(form){
         var formActype = form+'.acType';
         var formElement = document.getElementsByName(formActype);
         if(formElement != null && formElement[0].value != 'I'){
            formElement[0].value = 'U';
             
         }
         dataChanged();
    }
    
    function dataChanged(){
        DATA_CHANGED = 'true';
    }    
    linkForward(errValue);
    </script>

</html:html>
