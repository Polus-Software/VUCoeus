<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page
	import="org.apache.struts.validator.DynaValidatorForm,
edu.mit.coeuslite.utils.ComboBoxBean,
org.apache.struts.action.Action,
java.net.URLEncoder,
java.util.HashMap,
java.util.Set,
java.util.Iterator;"%>

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="proposalType" scope="session" class="java.util.Vector" />
<jsp:useBean id="coiStatus" scope="session" class="java.util.Vector" />
<jsp:useBean id="personDescDet" scope="session" class="java.util.Vector" />
<jsp:useBean id="questionsData" scope="session" class="java.util.Vector" />
<jsp:useBean id="dynaForm" scope="request"
	class="org.apache.struts.validator.DynaValidatorForm" />
<jsp:useBean id="person" scope="session"
	class="edu.mit.coeus.bean.PersonInfoBean" />
<jsp:useBean id="frmAddCOIDisclosure" scope="session"
	class="org.apache.struts.validator.DynaValidatorForm" />
<bean:size id="questionsSize" name="questionsData" />

<html:html>
<head>
<title>Coeus Web</title>
<html:base />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

</head>
<script>
function changeAllStatus(){

	var conflictStatus = document.forms[0].conflictStatus.value;
	for(var elementIndex=0; elementIndex < document.forms[0].elements.length; elementIndex++){
			var elementName = document.forms[0].elements[elementIndex].name;
			if( (elementName.indexOf("sltConflictStatus") != -1)
				|| (elementName.indexOf("disclConflictStatus") != -1) ){
				document.forms[0].elements[elementIndex].value=conflictStatus;
			}
	}	
}

function validateForm(form) { 
      
       
       if(validateCoiDisclosure(form) == true )
       {
         // Hide the code in first div tag
             document.getElementById('disclFormDiv').style.display = 'none';
         // Display code in second div tag
            document.getElementById('messageDiv').style.display = 'block';    
            return true;
        }
        else
        {
          return false;
        }
 }
function showQuestion(link)
{
    var winleft = (screen.width - 650) / 2;
    var winUp = (screen.height - 450) / 2;  
    var win = "scrollbars=1,resizable=1,width=790,height=450,left="+winleft+",top="+winUp
    sList = window.open('<bean:write name='ctxtPath'/>'+"/"+link, "list", win);
}

//Case 3513 - START
function limitText(limitField, limitNum) {
   if (limitField.value.length > limitNum) {
       limitField.value = limitField.value.substring(0, limitNum);
   } 
}
//Case 3513 - END
</script>
<html:javascript formName="coiDisclosure" dynamicJavascript="true"
	staticJavascript="true" />
<body>
	<%--  ************  START OF BODY TABLE  ************************--%>
	<html:form action="/addCoiDisclosure.do" method="post"
		onsubmit="return validateForm(this)">

		<div id="disclFormDiv">
			<table width="980" border="0" cellpadding="2" cellspacing="0"
				class="table">

				<tr>
					<td height="20" align="left" valign="top" class="theader"><bean:message
							bundle="coi" key="coiDisclosure.headerDiscl" /> <%=person.getFullName()%>
					</td>
				</tr>
				<tr>
					<td class="copybold">&nbsp;&nbsp; <b><font color="red">*</font></b>
						<font class="fontBrown"><bean:message bundle="coi"
								key="coi.indicatesReqField" /></font>
					</td>
				</tr>
				<tr>
					<td>
						<table width="100%" cellspacing="0" border="0">
							<tr align="left">
								<td class='copy'><font color="red"> <logic:messagesPresent
											message="true">
											<%-- Case 3513 - START --%>
											<html:messages id="message" message="true"
												property="proposalTitleLength" bundle="coi">
												<li><bean:write name="message" /></li>
											</html:messages>
											<%-- Case 3513 - END --%>
											<html:messages id="message" message="true"
												property="invalidConflictStatus" bundle="coi">
												<li><bean:write name="message" /></li>
											</html:messages>
											<html:messages id="message" message="true"
												property="invalidConflictStatusHasMaintain" bundle="coi">
												<li><bean:write name="message" /></li>
											</html:messages>
											<html:messages id="message" message="true"
												property="answersRequired" bundle="coi">
												<li><bean:write name="message" /></li>
											</html:messages>
											<html:messages id="message" message="true"
												property="initialdDisclosureTypeInvalid" bundle="coi">
												<li><bean:write name="message" /></li>
											</html:messages>
											<html:messages id="message" message="true"
												property="invalidDisclosureStatus" bundle="coi">
												<li><bean:write name="message" /></li>
											</html:messages>
											<html:messages id="message" message="true"
												property="duplicateSubmission" bundle="coi">
												<li><bean:write name="message" /></li>
											</html:messages>
										</logic:messagesPresent>
								</font></td>
							</tr>
						</table>
					</td>
				</tr>

				<!-- EntityDetails - Start  -->
				<tr>
					<td height="50" align="left" valign="top">
						<table width="99%" border="0" align="center" cellpadding="0"
							cellspacing="0" class="tabtable">
							<tr>
								<td>
									<table width="85%" border="0" cellspacing="2" cellpadding="0">
										<tr>
											<td valign="top" width="15%" class='copybold'><logic:present
													name="frmAddCOIDisclosure" scope="session">
													<logic:equal name="frmAddCOIDisclosure" scope="session"
														property="disclosureTypeCode" value="1">
                                                &nbsp;&nbsp; <%--Award--%>
														<bean:message bundle="coi" key="label.award" />
													</logic:equal>
													<logic:notEqual name="frmAddCOIDisclosure" scope="session"
														property="disclosureTypeCode" value="1">
                                                &nbsp;&nbsp;<font
															color="red">*</font>
														<bean:message bundle="coi" key="label.proposalTitle" />
													</logic:notEqual>
												</logic:present>&nbsp;:&nbsp;</td>
											<td><logic:equal name="frmAddCOIDisclosure"
													scope="session" property="disclosureTypeCode" value="3">
													<html:textarea cols="80" rows="3" property="title"
														styleClass="copy" />
												</logic:equal> <%-- Case 3513 - START --%> <logic:notEqual
													name="frmAddCOIDisclosure" scope="session"
													property="disclosureTypeCode" value="3">
													<td colspan="0" class='copy'><html:textarea cols="80"
															rows="3" property="title" readonly="true"
															styleClass="copy"
															onchange="limitText(this.form.title, 150)" /></td>
												</logic:notEqual> <%-- Case 3513 - END--%></td>
										</tr>
										<tr>
											<td width='5%'></td>
										</tr>

										<logic:equal name="frmAddCOIDisclosure" scope="session"
											property="disclosureTypeCode" value="3">
											<tr>
												<td valign="top" width="120" class='copybold'>
													&nbsp;&nbsp;<font color="red">*</font>
												<bean:message bundle="coi" key="label.proposalType" />&nbsp;:&nbsp;
												</td>
												<td valign="top" colspan="2"><select
													name="proposalTypeCode" class='textbox-long'>
														<%
                                                String preselectedTypeCode = (String)frmAddCOIDisclosure.get("proposalTypeCode");
                                                String selected = "";
                                                for(int cnt=0; cnt< proposalType.size(); cnt++){
                                                    ComboBoxBean comboBox = (ComboBoxBean)proposalType.get(cnt);
                                                    selected = "";
                                                    if(comboBox.getCode().equals(preselectedTypeCode)){
                                                        selected = "selected";
                                                    }
                                                
                                                %>
														<option value="<%=comboBox.getCode()%>" <%=selected%>><%=comboBox.getDescription()%></option>
														<%
                                                }
                                                %>
												</select></td>
											</tr>
										</logic:equal>
										<tr>
											<td width='5%'></td>
										</tr>
										<tr>
											<td valign="top" class='copybold'>&nbsp;&nbsp;<font
												color="red">*</font>
											<bean:message bundle="coi" key="label.sponsor" />&nbsp;:&nbsp;
											</td>
											<logic:equal name="frmAddCOIDisclosure" scope="session"
												property="disclosureTypeCode" value="3">
												<td class="copy"><html:text property="sponsorName"
														size="25" style="copy" styleClass="textbox-long" /></td>
											</logic:equal>
											<logic:notEqual name="frmAddCOIDisclosure" scope="session"
												property="disclosureTypeCode" value="3">
												<td colspan="3" class='copy'><html:text size="45"
														readonly="true" property="sponsorName" style="copy"
														styleClass="textbox-long" /></td>
											</logic:notEqual>
										</tr>

									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td height='10'>&nbsp;</td>
				</tr>
				<!-- Certificate - Start -->
				<tr>
					<td height="52" align="left" valign="top">
						<table width="99%" border="0" align="center" cellpadding="0"
							cellspacing="0" class="tabtable">
							<tr>
								<td colspan="4" align="left" valign="top">
									<table width="100%" height="20" border="0" cellpadding="0"
										cellspacing="0" class="tableheader">
										<tr>
											<td><bean:message bundle="coi"
													key="coiDisclosure.headerCert" /></td>
										</tr>
									</table>
								</td>
							</tr>

							<tr align="center">
								<td colspan="2"><br> <%--<DIV STYLE="overflow: auto; width: 900px; height: 165; padding:0px; margin: 0px">--%>
									<table width="99%" border="0" cellpadding="2" callspacing="0"
										class="tabtable">
										<logic:present name="questionsData" scope="session">
											<tr>
												<td width="80%" align="center" class="theader"><bean:message
														bundle="coi" key="label.question" /></td>
												<td width="40%" align="center" class="theader"><bean:message
														bundle="coi" key="label.answer" /></td>
											</tr>
											<logic:notEqual name="questionsSize" value="0">
												<% 
                                            String preSelectedQuestions[] =(String[])session.getAttribute("selectedQuestions");
                                            String preSelectedAnswers[] =(String[])session.getAttribute("selectedAnswers");
                                            String strBgColor = "#DCE5F1";
                                            int certificateIndex=0;
                                            for(int certificateCount = 0 ; certificateCount < questionsData.size() ; certificateCount++){
                                                DynaValidatorForm dynaValidatorForm =
                                                        (DynaValidatorForm)questionsData.elementAt(certificateCount) ;
                                                String questionId =(String)dynaValidatorForm.get("questionId");
                                                String noOfAns =(String)dynaValidatorForm.get("noOfAnswers");
                                                String answer = (String)dynaValidatorForm.get("answer");
                                                String certCode = (String)dynaValidatorForm.get("questionId");
                                                String correspEntQuestLabel =(String)dynaValidatorForm.get("correspEntQuestLabel");
                                                String questionLabel = (String)dynaValidatorForm.get("label");
                                                String checkedYes="";
                                                String checkedNo="";
                                                String checkedNA="";
                                                String preSelectedQuestionCode =null;
                                                String preSelectedAnswerCode = null;
                                                
                                                if( (preSelectedAnswers!=null) && (preSelectedAnswers.length>0 ) ){
                                                    preSelectedAnswerCode=preSelectedAnswers[certificateCount];
                                                }
                                                if (certificateIndex%2 == 0) {
                                                    strBgColor = "#D6DCE5";
                                                } else {
                                                    strBgColor="#DCE5F1";
                                                }
                                                
                                                if(preSelectedAnswerCode!=null){
                                                    if(preSelectedAnswerCode.equals("Y") ){
                                                        checkedYes="checked";
                                                    }
                                                    
                                                    if(preSelectedAnswerCode.equals("N") ){
                                                        checkedNo="checked";
                                                    }
                                                    
                                                    if(preSelectedAnswerCode.equals("X") ){
                                                        checkedNA="checked";
                                                    }
                                                }
                                                
                                                else if(answer != null){
                                                    if(answer.equals("Y") ){
                                                        checkedYes="checked";
                                                    } else if(answer.equals("N") ){
                                                        checkedNo="checked";
                                                    }
                                                    if(answer.equals("X") ){
                                                        checkedNA="checked";
                                                    }
                                                }
                                            
                                            %>
												<INPUT type='hidden' name='hdnQuestionId'
													value='<%=dynaValidatorForm.get("questionId")%>'>
												<INPUT type='hidden' name='hdnQuestionDesc'
													value='<%=dynaValidatorForm.get("description")%>'>
												<INPUT type='hidden' name='hdnSeqNo' value='1'>
												<tr bgcolor="<%=strBgColor%>" class="rowLine"
													onmouseover="className='rowHover rowLine'"
													onmouseout="className='rowLine'">
													<td class="copy" valign="top"><logic:present
															name="certQuestionErrors">
															<%
                                                        String errorCertCode = null;
                                                        Object errorObject = null;
                                                        HashMap entitiesWithYes = null;
                                                        String errorCode = null;
                                                        String entityNumber = null;
                                                        String entityName = null;
                                                        
                                                        HashMap certQuestionErrors = (HashMap)request.getAttribute("certQuestionErrors");
                                                        Iterator keysIterator = certQuestionErrors.keySet().iterator();
                                                        
                                                        while(keysIterator.hasNext()){
                                                            entitiesWithYes = null;
                                                            errorCode = null;
                                                            errorCertCode = (String)keysIterator.next();
                                                            
                                                            if(errorCertCode != null &&  errorCertCode.equals(certCode)){
                                                                errorObject = certQuestionErrors.get(errorCertCode);
                                                                if(errorObject.getClass().getName().equals("java.util.HashMap")){
                                                                    entitiesWithYes = (HashMap)certQuestionErrors.get(errorCertCode);
                                                                } else{
                                                                    errorCode = certQuestionErrors.get(errorCertCode).toString();
                                                                    
                                                                }
                                                            }
                                                            //If error is because user answered No, and has one or more entities
                                                            //with answer Yes, get the list of entities with Yes answer.
                                                            if(entitiesWithYes != null){
                                                                if(entitiesWithYes.size() == 1){
                                                        
                                                        %>
															<font color="red"> <bean:message
																	key="error.addCOIDisclosure.entYesAnsNoForOne"
																	bundle="coi" /> <%=correspEntQuestLabel%> <bean:message
																	key="error.addCOIDisclosure.entYesAnsNo2ForOne"
																	bundle="coi" />
															</font>
															<%
                                                                } else{
                                                        %>
															<font color="red"> <bean:message
																	key="error.addCOIDisclosure.entYesAnsNoForTwo"
																	bundle="coi" /> <%=correspEntQuestLabel%> <bean:message
																	key="error.addCOIDisclosure.entYesAnsNo2ForTwo"
																	bundle="coi" />
															</font>
															<%
                                                                }
                                                                Iterator entities = entitiesWithYes.keySet().iterator();
                                                                for(int cnt = 0; cnt<entitiesWithYes.size(); cnt++){
                                                                    entityNumber = (String)entities.next();
                                                                    entityName = (String)entitiesWithYes.get(entityNumber);
                                                        %>
															<a
																href="<bean:write name='ctxtPath'/>/editFinEnt.do?actionFrom=addDiscl&entityNumber=<%=entityNumber%>">
																<%=entityName%></a>
															<%
                                                        if(cnt<(entitiesWithYes.size()-1)){
                                                                        out.print(",");
                                                        }
                                                                }//End For
                                                                
                                                                if(entitiesWithYes.size() == 1){
                                                        %>
															<!--<font face="Verdana, Arial, Helvetica, sans-serif" size="1" color="red">-->
															<font color="red"> <bean:message
																	key="error.addCOIDisclosure.entYesAnsNo3ForOne"
																	bundle="coi" /> <%=correspEntQuestLabel%> <bean:message
																	key="error.addCOIDisclosure.entYesAnsNo4ForOne"
																	bundle="coi" />
															</font>
															<%
                                                                } else{
                                                        %>
															<!--<font face="Verdana, Arial, Helvetica, sans-serif" size="1" color="red">-->
															<font color="red"> <bean:message
																	key="error.addCOIDisclosure.entYesAnsNo3ForTwo"
																	bundle="coi" /> <%=correspEntQuestLabel%> <bean:message
																	key="error.addCOIDisclosure.entYesAnsNo4ForTwo"
																	bundle="coi" />
															</font>
															<%
                                                                }//End else
                                                                
                                                            }//End   if(entitiesWithYes != null)
                                                            
                                                            //User has answered Yes but has no financial entities
                                                            else if(errorCode != null && errorCode.equals("-1")){
                                                        
                                                        %>
															<font color="red"> <bean:message
																	key="error.addCOIDisclosure.noFinEntAnsReqExplForOne"
																	bundle="coi" /><br> <html:link
																	action="/getAddFinEnt.do?actionFrom=addDiscl">
																	<bean:message bundle="coi"
																		key="label.addFinancialEntity" />
																	<%--Add Financial Entity--%>
																</html:link> <bean:message
																	key="error.addCOIDisclosure.noFinEntAnsReqExplForTwo"
																	bundle="coi" />
															</font>
															<%
                                                            }
                                                            //Error code -2: User answered Yes, but no fin ent with answer Yes to this question.
                                                            else if(errorCode != null && errorCode.equals("-2")){
                                                        
                                                        %>

															<font color="red"> <bean:message
																	key="error.addCOIDisclosure.entNoAnsYes" bundle="coi" />
																<%=correspEntQuestLabel%> <bean:message
																	key="error.addCOIDisclosure.entNoAnsYes2" bundle="coi" />
																<a
																href="<bean:write name="ctxtPath"/>/getReviewFinEnt.do?actionFrom=addDiscl">
																	<bean:message key="error.addCOIDisclosure.entNoAnsYes3"
																		bundle="coi" />
															</a>
															</font>
															<bean:message key="error.addCOIDisclsoure.entNoAnsYes4"
																bundle="coi" />
															<%
                                                            }
                                                        }
                                                        %>
														</logic:present>
														<div align="justify">
															<b>Question <%=dynaValidatorForm.get("label")%></b>
															&nbsp;:&nbsp;<%=dynaValidatorForm.get("description")%>
															<a
																href='javascript:showQuestion("/question.do?questionNo=<%=dynaValidatorForm.get("questionId")%>",  "ae");'
																method="POST"> <u> <bean:message bundle="coi"
																		key="financialEntity.moreAbtQuestion" />
																	<%--More about this question--%>
															</u>
															</a>
														</div></td>
													<td class="copy" valign="top" width="50%">
														<table border="0" width="100%" cellpadding="0"
															cellspacing="0" border="0" class="copy">
															<tr align="center">
																<td
																	style="border-style: solid none; border-width: 0px 0;">
																	<bean:message bundle="coi" key="financialEntity.yes" /><br>
																	<input type="radio" name="<%= questionId %>" value="Y"
																	<%=checkedYes%> class='copy'>
																</td>
																<td
																	style="border-style: solid none; border-width: 0px 0;">
																	<bean:message bundle="coi" key="financialEntity.no" /><br>
																	<input type="radio" name="<%= certCode %>" value="N"
																	<%=checkedNo%> class='copy'>
																</td>
																<% if(noOfAns!=null && Integer.parseInt(noOfAns)> 2 ){%>
																<td
																	style="border-style: solid none; border-width: 0px 0;">
																	<bean:message bundle="coi" key="financialEntity.na" />
																	<input type="radio" name="<%= certCode %>" value="X"
																	<%=checkedNA%> class='copy'>
																</td>
																<%}%>
															</tr>
														</table>
													</td>
												</tr>
												<% }certificateIndex++;//End For%>
											</logic:notEqual>

										</logic:present>
										<logic:equal name="questionsSize" value="0">
											<tr>
												<td colspan='3' height="23" align=center>
													<div>
														<bean:message bundle="coi"
															key="financialEntity.noCertQuestionFound" />
														<%--No Certification Questions Found--%>
													</div>
												</td>
											</tr>
										</logic:equal>
									</table> <%--</DIV>--%></td>
							</tr>
							<tr>
								<td>&nbsp;</td>
							</tr>
						</table>
					</td>
				</tr>
				<!-- Certificate -End -->

				<!--Financial Entities Disclosed -Start -->

				<tr>
					<td height="119" align="left" valign="top"><br>
						<table width="99%" border="0" align="center" cellpadding="3"
							cellspacing="0" class="tabtable">
							<tr class="tableheader">
								<!--<td align="left" valign="top">
                            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableheader">
                                <tr>-->
								<td><bean:message bundle="coi"
										key="coiDisclosure.headerFinEntities" /></td>
								<!--</tr>
                        </table>
                    </td>-->
							</tr>

							<tr>
								<td colspan="6" class='copy' style=''>&nbsp;&nbsp;<bean:message
										bundle="coi" key="label.setConflictStatus" />
									<%--Set conflict status for all entities to:--%> <select
									name="conflictStatus" class='textbox-long'
									onchange="changeAllStatus();">
										<option selected>-
											<bean:message bundle="coi" key="label.pleaseSelect" /><%-- Please Select ---%></option>
										<%
                                String description1 = "";
                                String code1 = "";
                                for(int i=0;i<coiStatus.size();i++){
                                    ComboBoxBean objCombBean = (ComboBoxBean)coiStatus.get(i);
                                    code1 = objCombBean.getCode();
                                    description1 = "";
                                    if( code1.equals("301") || code1.equals("200") ){
                                        description1 = objCombBean.getDescription();
                                %>
										<option value="<%=code1%>"><%=description1%></option>
										<%
                                    }
                                }
                                %>
								</select>
								</td>
							</tr>


							<tr align="center">
								<td>
									<!--<DIV STYLE="overflow: auto; width: 920px; height: 165; padding:0px; margin: 0px">-->
									<table width="99%" border="1" cellpadding="2" cellspacing="0"
										class="tabtable">
										<tr>
											<td width="40%" class="theader"><bean:message
													bundle="coi" key="label.entityName" /></td>
											<td class="theader" width="20%"><bean:message
													bundle="coi" key="label.conflictStatus" /></td>
											<td class="theader" width="10%" nowrap><bean:message
													bundle="coi" key="label.reviewedBy" /></td>
											<td class="theader" width="30%"><bean:message
													bundle="coi" key="label.relationshipDesc" /></td>
										</tr>
										<logic:present name="personDescDet" scope="session">
											<%  String strBgColor = "#DCE5F1";
                            int disclIndex = 0;
                            String preSelectedConfStatus[] =(String[])session.getAttribute("selectedConflictStatus");
                            String preSelectedDesc[] =(String[])session.getAttribute("selectedDescription");                                  
                            %>
											<logic:iterate id="personDet" name="personDescDet"
												type="org.apache.commons.beanutils.DynaBean">
												<INPUT type='hidden' name='hdnEntityNum'
													value='<bean:write name="personDet" property="entityNumber"/>'>
												<INPUT type='hidden' name='hdnEntSeqNum'
													value='<bean:write name="personDet" property="entitySequenceNumber"/>'>
												<INPUT type='hidden' name='hdnSeqNum'
													value='<bean:write name="personDet" property="sequenceNum"/>'>
												<%                                  
                                if (disclIndex%2 == 0) {
                                strBgColor = "#D6DCE5";
                                } else {
                                strBgColor="#DCE5F1";
                                }
                                %>
												<tr bgcolor='<%=strBgColor%>' class="rowLine"
													onmouseover="className='rowHover rowLine'"
													onmouseout="className='rowLine'">
													<td class="copy"><coeusUtils:formatOutput
															name="personDet" property="entityName" /></td>
													<td class="copy"><select
														name="sltConflictStatus<%=disclIndex%>"
														class='textbox-long'>
															<%
                                            String  preSelectedConflictStatus = "";
                                            //If page is being reloaded after validation errors, get user-entered values.
                                            if( (preSelectedConfStatus != null) && (preSelectedConfStatus.length > 0 ) ){
                                                preSelectedConflictStatus = preSelectedConfStatus[disclIndex];
                                            }
                                            
                                            String code = "";
                                            String description = "";
                                            String selected = "";
                                            /*Don't include Not Previously Reported in the drop down.  Default to Select Conflict Status*/
                                            %>
															<option value="">-
																<bean:message bundle="coi" key="label.pleaseSelect" /></option>
															<%
                                            String description2 = "";
                                            String code2 = "";
                                            for(int i=0;i<coiStatus.size();i++){
                                                ComboBoxBean comboBox = (ComboBoxBean)coiStatus.get(i);
                                                code = comboBox.getCode();
                                                description = comboBox.getDescription();
                                                selected = "";
                                                //Not Previously Reported, No Conflict and PI Potential Conflict are the only
                                                //possible options here.
                                                if(code.equals("200") || code.equals("301") ){
                                                    
                                                    if ( preSelectedConflictStatus.equals(code) ) {
                                                        selected = "selected";
                                                    }
                                                    //If page is being loaded for the first time, status will be Not Previously Reported
                                                    //for all entities.
                                                    else if( code.equals("100") && preSelectedConflictStatus.equals("") ){
                                                        selected = "selected";
                                                    }
                                            %>
															<option <%=selected%> value='<%=code%>'><%=description%></option>
															<%
                                            
                                                }
                                            }
                                            %>
													</select></td>
													<td align="left" class="copy"><bean:message
															bundle="coi" key="label.pI" />
														<%--PI--%></td>
													<td>
														<%--INPUT type='text' class='textbox-long' name='relationShipDesc' value="<%=personDet.get("relationShipDesc") %>" maxlength="1000"--%>

														<INPUT type='text' class='textbox-long'
														name='relationShipDesc<%=disclIndex%>'
														value="<%=personDet.get("relationShipDesc") %>"
														maxlength="1000">

													</td>
												</tr>
												<% disclIndex++ ;%>
											</logic:iterate>
										</logic:present>
										<!--</div>-->
									</table>
								</td>
							</tr>
							<tr>

							</tr>

						</table></td>
				</tr>
				<!-- Financial Entities Disclosed -End -->
				<html:hidden property="coiDisclosureNumber" />
				<html:hidden property="disclosureTypeCode" />
				<html:hidden property="disclosureType" />
				<html:hidden property="acType" value="I" />
				<html:hidden property="personId" />
				<html:hidden property="sequenceNum" />
				<html:hidden property="appliesToCode" />
				<html:hidden property="fullName" />

				<tr>
					<td colspan="3" nowrap class="copy" align="left"
						style='padding-left: 6px;' width="100%"><html:submit
							property="save" value="Save and Submit" /></td>
				</tr>
			</table>
		</div>
		<div id='messageDiv' style='display: none;'>
			<table width="100%" height="100%" align="center" border="0"
				cellpadding="3" cellspacing="0" class="tabtable">
				<tr>
					<td align='center' class='copyred'><br>
						&nbsp;&nbsp;&nbsp; <bean:message bundle="coi"
							key="coiMessage.pleaseWait" /></td>
				</tr>
			</table>
		</div>

	</html:form>
</body>
</html:html>