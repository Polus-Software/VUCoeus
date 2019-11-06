<? xml version="1.0" ?>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page
	import="org.apache.struts.taglib.html.JavascriptValidatorTag,
edu.mit.coeuslite.utils.CoeusLiteConstants,java.util.Vector,java.util.HashMap,edu.mit.coeus.utils.ComboBoxBean,
edu.mit.coeus.utils.CoeusFunctions"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<jsp:useBean id="vecAcSpeciesData" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="vecAddedSpecies" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="vecAcPainCategories" scope="session"
	class="java.util.Vector" />
<%--Added for-COEUSQA-2798 Add count type to species/group screen-Start--%>
<jsp:useBean id="vecAcSpeciesCountType" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="exceptionCategory" scope="session"
	class="java.util.Vector" />
<%--Added for-COEUSQA-2798 Add count type to species/group screen-End--%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<bean:size id="editingList" name="iacucProtoSpeciesDynaBeansList"
	property="list" />
<html:html>
<head>
<script
	src="<%=request.getContextPath()%>/coeuslite/mit/utils/scripts/modal/modaldbox.js"></script>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/coeuslite/mit/utils/scripts/modal/modaldbox.css"
	type="text/css" />
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<title>Coeus Web</title>
<script language="javaScript" type="text/JavaScript">
            var errValue = false;
            var errLock = false;
            
            var exceptionValue = "on";
            var usdaValue = "on";
            
            function showHide(val){
                    if(val == 1){
                        document.getElementById('panel1').style.display = "block";
                    }else if(val == 2){
                        document.getElementById('panel2').style.display = "block";
                    }else if(val == 3){
                        document.getElementById('showSpPanel').style.display = "none";
                        document.getElementById('hideSpPanel').style.display = "block";
                        document.getElementById('speciesPanel').style.display = "block";
                        document.getElementById('speciesPanelSave').style.display = "block";
                    }else if(val == 4){
                        if(document.getElementById('showSpPanel'))
                        {
                            document.getElementById('showSpPanel').style.display = "block";                  
                            document.getElementById('hideSpPanel').style.display = "none";
                            document.getElementById('speciesPanel').style.display = "none";
                            document.getElementById('speciesPanelSave').style.display = "none";   
                            if(document.getElementById('showValPanel'))
                            {
                                document.getElementById('showValPanel').style.display = "none";
                            }
                        }
                    }
                    else if(val == 5){
                        
                        document.getElementById('showSpPanel').style.display = "none";
                        document.getElementById('hideSpPanel').style.display = "block";
                        document.getElementById('speciesPanel').style.display = "block";
                        document.getElementById('speciesPanelSave').style.display = "block";
                        
                    }
            }   
            
            function saveSpeciesData(){     
            // COEUSQA:3317 - IACUC USDA/Exception Boxes Auto-Checking - Start
            if(document.getElementById("isUsdaCovered").checked) {
              usdaValue = 'on';
            }
            else {
              usdaValue = 'off';
            }           
             if(document.getElementById("isExceptionPresent").checked) {
              exceptionValue = 'on';
            }
            else {
              exceptionValue = 'off';
            }
            // COEUSQA:3317 - End
            document.iacucProtoSpeciesDynaBeansList.action = "<%=request.getContextPath()%>/saveIacucProtoSpecies.do?exception="+exceptionValue+"&usda="+usdaValue;
            document.iacucProtoSpeciesDynaBeansList.submit();
            }
            //Modified for COEUSQA-3035 Indicator Logic Implementation for new IACUC Screens
            function modifySpecies(speciesId, sequenceNumber){
                document.iacucProtoSpeciesDynaBeansList.action = "<%=request.getContextPath()%>/loadIacucProtoSpeciesDetails.do?speciesId="+speciesId+"&sequenceNumber="+sequenceNumber;
                document.iacucProtoSpeciesDynaBeansList.submit();
            }
            
            function removeSpecies(speciesId, updateTimestamp){
             if (confirm("Are you sure you want to delete this Species and associated Exceptions?")==true){
                document.iacucProtoSpeciesDynaBeansList.action = "<%=request.getContextPath()%>/removeIacucProtoSpecies.do?speciesId="+speciesId+"&updateTimestamp="+updateTimestamp;
                document.iacucProtoSpeciesDynaBeansList.submit();
                }
            }
            function clearFormData(){
                document.iacucProtoSpeciesDynaBeansList.action = "<%=request.getContextPath()%>/getIacucProtoSpecies.do";
                document.iacucProtoSpeciesDynaBeansList.submit();                
                
            }
            
            function addException(){            
                document.iacucProtoSpeciesDynaBeansList.action = "<%=request.getContextPath()%>/addIacucProtoSpeciesException.do?exception="+exceptionValue+"&usda="+usdaValue;
                document.iacucProtoSpeciesDynaBeansList.submit();
            }
            
            function removeException(speciesId,exceptionId){
             if (confirm("Are you sure you want to remove this Exception?")==true){
                document.iacucProtoSpeciesDynaBeansList.action = "<%=request.getContextPath()%>/removeIacucProtoSpeciesException.do?speciesId="+speciesId+"&exceptionId="+exceptionId;
                document.iacucProtoSpeciesDynaBeansList.submit();
                }
            }
        function showDialog(divId){ 
        
               if(navigator.appName == "Microsoft Internet Explorer") {
                    document.getElementById(divId).style.width = 400;
                    document.getElementById('inside'+divId).style.width = 400;
                    document.getElementById('inside'+divId).style.height = 180;
                    document.getElementById('painDescTable').style.width = 300;
                    document.getElementById('painDescTextArea').style.width = 393;
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
     
     
     function displayHide(val,value){
            var panel = 'plusMinusPanel'+value;
            var pan = 'pan'+value;
            var hidePanel  = 'hidePlusMinusPanel'+value;
            if(val == 1){
                        document.getElementById(panel).style.display = "none";
                        document.getElementById(hidePanel).style.display = "block";
                        document.getElementById(pan).style.display = "block";
                    }
            else if(val == 2){
                        document.getElementById(panel).style.display = "block";
                        document.getElementById(hidePanel).style.display = "none";
                        document.getElementById(pan).style.display = "none";
                    }        
        }        
        
        /*
        NOTE : USDA_CHECKED_VALUE and EXCEPTION_CHECKED_VALUE are variables declared in cwGeneralInfoLayout.jsp
        These were created to resolve the save confirmation problem for USDA & EXCEPTION checkboxes
        (i.e values for usdaValue and exceptionValue variables is not available while saving species+Exception data
         through save confirmation)
        */
        
        function checkUsda(obj){
         
            if(obj.checked == true){ 
             usdaValue = "on";  
             USDA_CHECKED_VALUE = 'on';
            }else if(obj.checked == false){
                usdaValue = "off";    
                USDA_CHECKED_VALUE = 'off';
            }
            EXCEPTION_CHECKED_VALUE = exceptionValue;
            dataChanged();    
        
        }
        
        function displayExceptionPanel(obj){        
            var isChecked = obj.checked;       
            if(obj.checked == true){ 
             exceptionValue = "on";
             EXCEPTION_CHECKED_VALUE = 'on';         
             document.getElementById('exceptionPanel').style.display = "block";
            }else if(obj.checked == false){
                exceptionValue = "off";             
                EXCEPTION_CHECKED_VALUE = 'off';
                document.getElementById('exceptionPanel').style.display = "none";

            }
            USDA_CHECKED_VALUE = usdaValue;
            dataChanged();
        }
        
       
      </script>
<style>
.textbox-longer {
	font-weight: normal;
	width: 500px
}
</style>
</head>
<body>

	<% 
String mode=(String)session.getAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId());

Boolean canModifySpeciesInDisplay =(Boolean)session.getAttribute("canModifySpeciesInDisplay"+session.getId());
if(canModifySpeciesInDisplay != null && canModifySpeciesInDisplay.booleanValue()){
    mode = CoeusLiteConstants.MODIFY_MODE;
}

boolean modeValue=false;
String procedureExistsForSpecies = (String)request.getAttribute("procedureExists");
HashMap hmExceptionForSpecies = new HashMap();
if(session.getAttribute("exceptionForSpecies") != null){
 hmExceptionForSpecies = (HashMap)session.getAttribute("exceptionForSpecies");    
}


String EMPTY_STRING = "";
if(mode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE)){
    modeValue=true;
}else if(mode.equalsIgnoreCase(CoeusLiteConstants.MODIFY_MODE)){
    modeValue=false;
}else if(mode.equalsIgnoreCase(CoeusLiteConstants.ADD_MODE)){
    modeValue=false;
}
%>

	<%String protocolNo = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());

if(protocolNo!= null){ 
protocolNo = "["+"Protocol No. - "+ protocolNo+"]";
}else{
protocolNo = "";
}

if(canModifySpeciesInDisplay == null || !canModifySpeciesInDisplay){
    String strProtocolNum = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
    if(strProtocolNum == null)
    strProtocolNum = "";    
    String amendRenewPageMode = (String)session.getAttribute("amendRenewPageMode"+request.getSession().getId());
    if(strProtocolNum.length() > 10 && (amendRenewPageMode == null || 
        amendRenewPageMode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE))){
        modeValue = true;
    }
}
%>
	<%try{%>

	<html:form action="/saveIacucProtoSpecies.do" method="post"
		onsubmit="validateForm(this)">
		<a name="top"></a>

		<script type="text/javascript">
    document.getElementById('txt').innerHTML = '<bean:message bundle="iacuc" key="helpPageTextProtocol.Species"/>';
    </script>

		<table width="100%" height="100%" border="0" cellpadding="0"
			cellspacing="0" class="table">



			<tr>
				<td>
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<!-- JM 5-31-2011 updated classes per 4.4.2 -->
							<td height="20" align="left" valign="center" class="tableheader">
								&nbsp;<bean:message bundle="iacuc"
									key="iacuc.speciesGroups.addSpeciesGroups" />
							</td>
							<td height="20" align="right" valign="top" class="tableheader">
								<!-- END --> <a id="helpPageText"
								href="javascript:showBalloon('helpPageText', 'helpText')"> <u><bean:message
											bundle="iacuc" key="helpPageTextProtocol.help" /></u>
							</a>
							</td>&nbsp;&nbsp;
						</tr>
					</table>
				</td>
			</tr>
			<tr class='copy' align="left">
				<td><font color="red"> <logic:messagesPresent>
							<html:errors header="" footer="" bundle="iacuc" />
							<script>errValue = true;</script>
						</logic:messagesPresent></td>
			</tr>

			<logic:messagesPresent message="true">
				<tr class='copy' align="left">
					<td><div id='showValPanel'>
							<font color="red"> <script>errValue = true;</script> <html:messages
									id="message" message="true">
									<html:messages id="message" message="true"
										property="groupAlreadyExists" bundle="iacuc">
										<script>errLock = true;</script>
										<li><bean:write name="message" /></li>
									</html:messages>
									<html:messages id="message" message="true"
										property="invalidSpeciesId" bundle="iacuc">
										<script>errLock = true;</script>
										<li><bean:write name="message" /></li>
									</html:messages>
									<html:messages id="message" message="true"
										property="invalidSpeciesCount" bundle="iacuc">
										<script>errLock = true;</script>
										<li><bean:write name="message" /></li>
									</html:messages>
									<html:messages id="message" message="true" property="errMsg"
										bundle="iacuc">
										<script>errLock = true;</script>
										<li><bean:write name="message" /></li>
									</html:messages>
									<html:messages id="message" message="true" bundle="iacuc"
										property="painCategoryCodeRequired">
										<script>errLock = true;</script>
										<li><bean:write name="message" /></li>
									</html:messages>
									<html:messages id="message" message="true"
										property="invalidExceptionCode" bundle="iacuc">
										<script>errLock = true;</script>
										<li><bean:write name="message" /></li>
									</html:messages>
									<html:messages id="message" message="true"
										property="invalidExceptionDesc" bundle="iacuc">
										<script>errLock = true;</script>
										<li><bean:write name="message" /></li>
									</html:messages>
									<html:messages id="message" message="true"
										property="groupNameEmpty" bundle="iacuc">
										<script>errLock = true;</script>
										<li><bean:write name="message" /></li>
									</html:messages>
								</html:messages>

							</font>
						</div></td>
				</tr>
			</logic:messagesPresent>


			<tr>
				<td align="left" valign="top" class='core'><table width="100%"
						border="0" align="left" cellpadding="0" cellspacing="0"
						class="tabtable">
						<%if(!modeValue){%>
						<tr class='table'>
							<td colspan="4" align="left" valign="top" class='table'>

								<div id='showSpPanel' class='table'>
									<%String divlink = "javascript:showHide(5)";%>
									<html:link href="<%=divlink%>">
										<u><bean:message bundle="iacuc"
												key="iacuc.speciesGroups.addSpeciesGroups" /></u>
									</html:link>
								</div>

							</td>
						</tr>
						<%}%>
						<tr>
							<td>
								<div id='speciesPanel' style='display: none;'>
									<%int listCount = 0;
                                String isException = null;%>
									<logic:present name="iacucProtoSpeciesDynaBeansList"
										property="list" scope="session">
										<logic:iterate id="dynaFormData"
											name="iacucProtoSpeciesDynaBeansList" property="list"
											type="org.apache.struts.action.DynaActionForm"
											indexId="listIndex" scope="session">
											<table width="100%" border="0" cellpadding="1"
												cellspacing="5">
												<%if(listCount == 0){%>
												<tr>
													<td width="15%" nowrap class="copybold" align="right">
														<font color="red">*</font>Group:
													</td>
													<td class="copy"><html:text property="groupName"
															name="dynaFormData" indexed="true"
															styleClass="textbox-long" disabled="<%=modeValue%>"
															maxlength="50" onchange="dataChanged()" /></td>
													<td width="10%" nowrap class="copybold" align="right">
														<font color="red">*</font>Species:
													</td>
													<td nowrap class="copy" width="10%" align="center"
														colspan="3"><html:select property="speciesCode"
															name="dynaFormData" indexed="true"
															styleClass="textbox-long" disabled="<%=modeValue%>"
															onchange="dataChanged()">
															<html:option value="">
																<bean:message bundle="proposal"
																	key="generalInfoProposal.pleaseSelect" />
															</html:option>
															<html:options collection="vecAcSpeciesData"
																property="code" labelProperty="description" />
														</html:select></td>
												</tr>
												<tr>
													<td nowrap class="copybold" align="right">Species
														Strain:</td>
													<td class="copy"><html:text property="strain"
															name="dynaFormData" indexed="true"
															styleClass="textbox-long" disabled="<%=modeValue%>"
															maxlength="30" onchange="dataChanged()" /></td>
													<td class="copybold" nowrap align="right">
														<%
                                                    String parameterValue = new CoeusFunctions().getParameterValue("IACUC_IS_PAIN_CATEGORY_MANDATORY");                                            
                                                    if (parameterValue != null && "1".equals(parameterValue.trim())) {%>
														<font color="red">*</font> <%} %> Pain Category:
													</td>
													<td class="copy" align="left"><html:select
															property="painCategoryCode" name="dynaFormData"
															indexed="true" styleClass="textbox-long"
															disabled="<%=modeValue%>" onchange="dataChanged()">
															<html:option value="">
																<bean:message bundle="proposal"
																	key="generalInfoProposal.pleaseSelect" />
															</html:option>
															<html:options collection="vecAcPainCategories"
																property="code" labelProperty="description" />
														</html:select></td>
												</tr>

												<tr>
													<td width="15%" nowrap class="copybold" align="right">
														USDA Covered Type:</td>
													<td><html:checkbox property="isUsdaCovered"
															styleId="isUsdaCovered" name="dynaFormData"
															indexed="true" styleClass="copy"
															onclick="checkUsda(this);" disabled="<%=modeValue%>" /></td>
													<td nowrap class="copybold" align="right">Count Type:
													</td>
													<td class="copy" align="left"><html:select
															property="speciesCountTypeCode" name="dynaFormData"
															indexed="true" styleClass="textbox-long"
															disabled="<%=modeValue%>" onchange="dataChanged()">
															<html:option value="">
																<bean:message bundle="proposal"
																	key="generalInfoProposal.pleaseSelect" />
															</html:option>
															<html:options collection="vecAcSpeciesCountType"
																property="code" labelProperty="description" />
														</html:select></td>
												</tr>
												<tr>
													<td nowrap class="copybold" align="right">Count:</td>
													<td class="copy">
														<!--COEUSQA-3384 Increase lengths for some IACUC fields - Start -->
														<html:text property="speciesCount" name="dynaFormData"
															indexed="true" styleClass="textbox"
															disabled="<%=modeValue%>" maxlength="8"
															style="width:23%;" onchange="dataChanged()" /> <!--COEUSQA-3384 Increase lengths for some IACUC fields - end -->
													</td>
													<td class="copybold" align="right">Exception:</td>
													<td><bean:define id="exceptionPresentValue"
															name="dynaFormData" property="isExceptionPresent" /> <%isException = (String)dynaFormData.get("isExceptionPresent");
                                                    boolean disabled = modeValue;
                                                    if("on".equals(exceptionPresentValue) && editingList>1){
                                                        disabled = true;
                                                    }%> <html:checkbox
															name="dynaFormData" indexed="true"
															property="isExceptionPresent"
															styleId="isExceptionPresent" styleClass="copy"
															onclick="displayExceptionPanel(this);"
															disabled="<%=disabled%>" /> <html:hidden
															property="acType" name="dynaFormData" indexed="true" /> <html:hidden
															property="protocolNumber" name="dynaFormData"
															indexed="true" /> <html:hidden property="sequenceNumber"
															name="dynaFormData" indexed="true" /> <html:hidden
															property="updateTimeStamp" name="dynaFormData"
															indexed="true" /> <html:hidden property="updateUser"
															name="dynaFormData" indexed="true" /> <html:hidden
															property="awUpdateTimeStamp" name="dynaFormData"
															indexed="true" /> <html:hidden property="awUpdateUser"
															name="dynaFormData" indexed="true" /> <html:hidden
															property="speciesId" name="dynaFormData" indexed="true" />
														<html:hidden property="awSpeciesId" name="dynaFormData"
															indexed="true" /> <html:hidden
															property="isExceptionPresent" name="dynaFormData"
															indexed="true" /></td>
												</tr>
												<%}%>
											</table>
											<%listCount++;%>
										</logic:iterate>
									</logic:present>
									<%listCount = 0;
                                if("on".equals(isException)){%>
									<div id='exceptionPanel' style='display: block;'>
										<%}else{%>
										<div id='exceptionPanel' style='display: none;'>
											<%}%>
											<table width="100%" border="0" cellpadding="1"
												cellspacing="5" class="tabtable">
												<tr>
													<td class="theader">Exceptions</td>
												</tr>
												<logic:present name="iacucProtoSpeciesDynaBeansList"
													property="list" scope="session">
													<logic:iterate id="dynaFormData"
														name="iacucProtoSpeciesDynaBeansList" property="list"
														type="org.apache.struts.action.DynaActionForm"
														indexId="listIndex" scope="session">
														<%if(listCount > 0){%>
														<tr>
															<td colspan="4">
																<table width="100%" border="0" class="tabtable">
																	<tr>
																		<td>

																			<table width="100%" border="0" cellpadding="0"
																				cellspacing="2">
																				<tr>
																					<td nowrap class="copybold" align="left"><font
																						color="red">*</font>Exception Category:</td>

																					<td nowrap class="copy" align="left"><html:select
																							name="dynaFormData" indexed="true"
																							property="exceptionCategoryCode"
																							styleClass="textbox-long"
																							disabled="<%=modeValue%>"
																							onchange="dataChanged()">
																							<html:option value="">
																								<bean:message bundle="proposal"
																									key="generalInfoProposal.pleaseSelect" />
																							</html:option>
																							<html:options collection="exceptionCategory"
																								property="code" labelProperty="description" />

																						</html:select></td>
																				</tr>

																				<tr>
																					<td nowrap class="copybold" align="left"
																						valign="top"><font color="red">*</font>Description:
																					</td>
																					<td colspan="3" align="left"><html:textarea
																							property="exceptionDescription"
																							name="dynaFormData" indexed="true" cols="99"
																							rows="3" styleClass="copy"
																							onchange="dataChanged()" /></td>
																				</tr>

																			</table>

																		</td>
																	</tr>
																</table>

															</td>
														</tr>
														<%}%>
														<%listCount++;%>
													</logic:iterate>
												</logic:present>
												<%String addExceptionLink = "javaScript:addException()";%>
												<tr>
													<td><html:link href="<%=addExceptionLink%>">
															<u>Add Exception</u>
														</html:link></td>
												</tr>
											</table>
										</div>
									</div>
							</td>
						</tr>
					</table></td>
			</tr>

			<tr>
				<td class='copy' nowrap colspan="6">
					<div id='speciesPanelSave' style='display: none;'>
						<table width="99%" border="0" cellpadding="0" cellspacing="5">
							<tr class='table'>
								<td width="15%" nowrap class="copybold" align="left"><html:button
										property="Save" value="Save" styleClass="clsavebutton"
										onclick="saveSpeciesData();" /></td>
								<td nowrap class="copybold" align="left">
									<div id='hideSpPanel' style='display: none;' class="copybold">
										<html:button property="Cancel" value="Cancel"
											styleClass="clsavebutton" onclick="clearFormData();" />
									</div>
								</td>
							</tr>
						</table>
					</div>
				</td>
			</tr>

			<tr>
				<td align="left" valign="top" class='core'>
					<table width="100%" border="0" align="center" cellpadding="3"
						cellspacing="0" class="theader">

						<tr align="center">
							<td colspan="5">

								<table width="100%" border="0" cellspacing="0" cellpadding="3"
									bgcolor="#9DBFE9">
									<tr>
										<td width="17%" align="left" class="theader">Group</td>
										<td width="17%" align="left" class="theader">Species</td>
										<td width="10%" align="left" class="theader">Species
											Strain</td>
										<td width="10%" align="left" class="theader">Pain
											Category</td>
										<td width="15%" align="left" class="theader">USDA Covered
											Type</td>
										<td width="10%" align="left" class="theader">Count Type</td>
										<td width="5%" align="left" class="theader">Count</td>
										<td width="8%" align="left" class="theader">Exception</td>
										<td width="4%" align="center" class="theader"></td>
										<td width="4%" align="center" class="theader"></td>
									</tr>

									<%  String strBgColor = "#DCE5F1";
                            int count = 0;
                            %>
									<logic:present name="iacucProtoSpeciesDynaBeansList"
										property="beanList" scope="session">
										<logic:iterate id="dynaFormBean"
											name="iacucProtoSpeciesDynaBeansList" property="beanList"
											type="org.apache.struts.action.DynaActionForm"
											indexId="beanIndex" scope="session">
											<% 
                                    if (count%2 == 0)
                                        strBgColor = "#D6DCE5";
                                    else
                                        strBgColor="#DCE5F1";
                                    %>
											<tr bgcolor="<%=strBgColor%>" valign="top"
												onmouseover="className='TableItemOn'"
												onmouseout="className='TableItemOff'">
												<td width="17%" align="left" class="copy">
													<%String divName="plusMinusPanel"+count;%>
													<div id='<%=divName%>' style='display: block;'>

														<%String divlink = "javascript:displayHide(1,'"+count+"')";%>
														<html:link href="<%=divlink%>">
															<%String imagePlus=request.getContextPath()+"/coeusliteimages/plus.gif";%>
															<html:img src="<%=imagePlus%>" border="0" />
														</html:link>
														&nbsp;
														<bean:write name="dynaFormBean" property="groupName" />

													</div> <%divName="hidePlusMinusPanel"+count;%>
													<div id='<%=divName%>' style='display: none;'>

														<% divlink = "javascript:displayHide(2,'"+count+"')";%>
														<html:link href="<%=divlink%>">
															<%String imageMinus=request.getContextPath()+"/coeusliteimages/minus.gif";%>
															<html:img src="<%=imageMinus%>" border="0" />
														</html:link>
														&nbsp;
														<bean:write name="dynaFormBean" property="groupName" />

													</div>
												</td>
												<td width="17%" align="left" class="copy"><bean:write
														name="dynaFormBean" property="speciesName" /></td>
												<td width="10%" align="left" class="copy"><bean:write
														name="dynaFormBean" property="strain" /></td>
												<td width="10%" align="left" class="copy">
													<%
                                            String painCategDesc = (String)dynaFormBean.get("painCategoryDesc");
                                          painCategDesc = painCategDesc == null ? "":painCategDesc;
                                            
                                            
                                            if(painCategDesc.length()>16){%>
													<div id="painDesc<%=count%>" class="dialog"
														style="width: 450px; height: 200px; display: none;">
														<div id="insidePainDesc<%=count%>"
															style="overflow: auto; width: 450px; height: 186px">
															<table border="0" cellpadding='0' cellspacing='1'
																align=left class='tabtable' width='400px'>
																<tr class='theader'>
																	<td width='100%'>Pain Category Description</td>
																</tr>
															</table>
															<table border="0" id="painDescTable" cellpadding="2"
																cellspacing="0" class="lineBorderWhiteBackgrnd"
																width="400" height="150">
																<tr>
																	<td><html:textarea styleId="painDescTextArea"
																			property="txtDesc" styleClass="copy" readonly="true"
																			value="<%=painCategDesc%>" disabled="false" rows="9"
																			style="border-top-width:0px; border-right-width:0px; 
                                                                border-bottom-width:0px; border-left-width:0px;width:395px" />
																	</td>
																</tr>
															</table>

														</div>
														<input type="button" onclick="javascript:hm('painDesc')"
															value="Close">
													</div> <%}%> <%
                                           String showSummary = "javascript:showDialog('painDesc"+count+"')";%>

													<%if(painCategDesc.length()>14){
                                             painCategDesc = painCategDesc.substring(0,14);
                                           %> <%=painCategDesc%><html:link
														href="<%=showSummary%>">&nbsp;[...]</html:link> <%}else{%> <%=painCategDesc%>
													<%}%>
												</td>
												<td width="15%" align="left" class="copy"><bean:write
														name="dynaFormBean" property="isUsdaCovered" /></td>
												<td width="10%" align="left" class="copy"><bean:write
														name="dynaFormBean" property="speciesCountTypeDesc" /></td>
												<td width="5%" align="left" class="copy"><bean:write
														name="dynaFormBean" property="speciesCount" /></td>
												<td width="8%" align="left" class="copy"><logic:equal
														name="dynaFormBean" property="isExceptionPresent"
														value="Y">
														<img
															src="<bean:write name='ctxtPath'/>/coeusliteimages/checked.gif">
													</logic:equal></td>
												<%if(!modeValue){%>
												<!--Modified for COEUSQA-3035 Indicator Logic Implementation for new IACUC Screens-->
												<td width="4%" align="center" class="copy"><a
													href="javaScript:modifySpecies('<bean:write name="dynaFormBean"  property="speciesId"/>','<bean:write name="dynaFormBean"  property="sequenceNumber"/>');">Modify</a>&nbsp;&nbsp;</td>
												<td width="4%" align="center" class="copy"><a
													href="javaScript:removeSpecies('<bean:write name="dynaFormBean"  property="speciesId"/>','<bean:write name="dynaFormBean"  property="awUpdateTimeStamp"/>');">Remove</a></td>
												<%}else{ %>
												<td width="4%" align="center" class="copy"></td>
												<td width="4%" align="center" class="copy"></td>
												<%
                                        } %>
											</tr>
											<!-- -->
											<tr>
												<td colspan="10">
													<%divName="pan"+count;%>
													<div id=<%=divName%> style="display: none;">
														<% String showExcepDetails = "N";
                                                //int = 
                                                showExcepDetails = (String)hmExceptionForSpecies.get(Integer.parseInt(dynaFormBean.get("speciesId").toString()));
                                                if("Y".equalsIgnoreCase(showExcepDetails)){%>
														<table width="100%" border="1" cellpadding="0"
															cellspacing="0" class="tabtable"
															style="border: 2px solid #789FD5;">
															<tr>
																<td height="100%">
																	<table width="100%" align="center" border="0"
																		cellpadding="0" cellspacing="0">
																		<tr>
																			<td width="25%" align="left" class="theader"
																				style="font-weight: normal;">Exception Category</td>
																			<td width="65%%" align="left" class="theader"
																				style="font-weight: normal;">Description</td>
																			<td width="10%" align="center" class="theader"
																				style="font-weight: normal;"></td>
																		</tr>
																		<logic:present name="iacucProtoSpeciesDynaBeansList"
																			property="infoList" scope="session">
																			<%  String strColor = "#DCE5F1";
                                                                    int infoListCount = 0;%>
																			<logic:iterate id="dynaFormInfo"
																				name="iacucProtoSpeciesDynaBeansList"
																				property="infoList"
																				type="org.apache.struts.action.DynaActionForm"
																				indexId="infoIndex" scope="session">
																				<% 
                                                                        if (infoListCount%2 == 0)
                                                                            strColor = "#D6DCE5";
                                                                        else
                                                                            strColor="#DCE5F1";
                                                                         if( Integer.parseInt(dynaFormBean.get("speciesId").toString()) == Integer.parseInt(dynaFormInfo.get("speciesId").toString())){%>
																				<tr bgcolor="<%=strColor%>"
																					onmouseover="className='TableItemOn'"
																					onmouseout="className='TableItemOff'">
																					<td align="left" class="copy"><bean:write
																							name="dynaFormInfo"
																							property="exceptionCategoryDesc" /></td>
																					<td align="left" class="copy">
																						<%
                                                                                String description  = (String)dynaFormInfo.get("exceptionDescription");
                                                                                if(description != null && description.length()>75){%>
																						<div id="excepDesc<%=infoListCount%>"
																							class="dialog"
																							style="width: 450px; height: 200px; display: none;">
																							<div id="inside<%=infoListCount%>"
																								style="overflow: auto; width: 450px; height: 186px">
																								<table cellpadding='0' cellspacing='1'
																									align=left class='tabtable' width='400px'>
																									<tr class='theader'>
																										<td><bean:message
																												key="scientifiJustification.exceptionCategoryDesc"
																												bundle="iacuc" /></td>
																									</tr>
																								</table>
																								<table border="0" id="summaryTable"
																									cellpadding="2" cellspacing="0"
																									class="lineBorderWhiteBackgrnd" width="400"
																									height="150">
																									<tr>
																										<td><html:textarea
																												styleId="summaryTextArea" property="txtDesc"
																												styleClass="copy" readonly="true"
																												value="<%=description%>" disabled="false"
																												rows="9"
																												style="border-top-width:0px; border-right-width:0px; 
                                                                                                                   border-bottom-width:0px; border-left-width:0px;width:395px" />
																										</td>
																									</tr>
																								</table>
																							</div>
																							<input type="button"
																								onclick="javascript:hm('description')"
																								value="Close">
																						</div> <%}%> <%  
                                                                                //Modified for COEUSQA-2711 Increase IACUC Exception description to 1000 characters-Start
                                                                                String showDescription = "javascript:showDialog('excepDesc"+infoListCount+"')";
                                                                                if(description != null && description.length()>301){
                                                                                description = description.substring(0,300);
                                                                                //Modified for COEUSQA-2711 Increase IACUC Exception description to 1000 characters-Start
                                                                                %>
																						<%=description%><html:link
																							href="<%=showDescription%>">&nbsp;[...]</html:link>
																						<%}else{%> <%=description%> <%}%>
																					</td>
																					<%if(!modeValue){%>
																					<td width="9%" align="center" class="copy"><a
																						href="javaScript:removeException('<bean:write name="dynaFormBean"  property="speciesId"/>','<bean:write name="dynaFormInfo" property="exceptionId"/>');">Remove</a></td>
																					<%}else
                                                                            {%>
																					<td width="7%" align="center" class="copy"></td>
																					<%}%>
																					<%}%>
																				</tr>
																				<%infoListCount++;%>
																			</logic:iterate>
																		</logic:present>
																	</table>
																</td>
															</tr>
														</table>
														<%} else{%>
														<table width="100%" cellpadding="0" cellspacing="0"
															class="tabtable">
															<tr>
																<td>
																	<table width="100%" border="1" cellpadding="0"
																		cellspacing="0" class="tabtable">
																		<tr>
																			<td colspan="4" class="copy">No Exception
																				Details available</td>
																		</tr>
																	</table>
																</td>
															</tr>
														</table>
														<%}%>
													</div>
												</td>
											</tr>
											<!-- -->
											<% count++;%>

										</logic:iterate>
									</logic:present>

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
	<script>
          DATA_CHANGED = 'false';
          var dataModified = '<%=request.getAttribute("dataModified")%>';
          if(dataModified == 'modified' || (errValue && !errLock)) {
                DATA_CHANGED = 'true';
          }
          if(dataModified == 'modified'){
            showHide(5);
          }
          LINK = "<%=request.getContextPath()%>/saveIacucProtoSpecies.do?exception="+exceptionValue+"&usda="+usdaValue; 
          //LINK = "<%=request.getContextPath()%>/saveIacucProtoSpecies.do?exception="+EXCEPTION_CHECKED_VALUE+"&usda="+USDA_CHECKED_VALUE; 
          FORM_LINK = document.iacucProtoSpeciesDynaBeansList;          
          PAGE_NAME = "<bean:message key="species.species" bundle="iacuc"/>";
          function dataChanged(){
                DATA_CHANGED = 'true';
          }          
          linkForward(errValue);
</script>
	<script>
      <%if("Y".equals(procedureExistsForSpecies)){%>
          alert('<bean:message key="species.procedure.exists" bundle="iacuc"/>');
      <%}%>
      var help = '<bean:message key="helpTextProtocol.Subjects"/>';
      help = trim(help);
      if(help.length == 0) {
            document.getElementById('helpText').style.display = 'none';
      }
      function trim(s) {
            return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
      }  
      
      var value = '<%=request.getAttribute("isUsdaCovered")%>';
      //alert("usda"+value);
      usdaValue = value;
      USDA_CHECKED_VALUE = value;
      value = '<%=request.getAttribute("isExceptionPresent")%>';
      //alert("isExceptionPresent"+value);
      exceptionValue = value;
      EXCEPTION_CHECKED_VALUE = value;
      //alert(usdaValue);
</script>
</body>
<%}catch(Exception e){
       e.printStackTrace();
    }
       %>
</html:html>
