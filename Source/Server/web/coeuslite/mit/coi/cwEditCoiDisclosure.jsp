<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page
	import="org.apache.struts.validator.DynaValidatorForm,
   java.util.HashMap,java.util.Set,java.util.Iterator,
  edu.mit.coeuslite.utils.ComboBoxBean"%>
<%@ page import="java.text.SimpleDateFormat, java.util.Date"%>

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ taglib uri="/WEB-INF/privilege.tld" prefix="priv"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="proposalType" scope="session" class="java.util.Vector" />
<jsp:useBean id="coiStatus" scope="session" class="java.util.Vector" />
<jsp:useBean id="personDescDet" scope="session" class="java.util.Vector" />
<jsp:useBean id="questionsData" scope="session" class="java.util.Vector" />
<jsp:useBean id="dynaForm" scope="session"
	class="org.apache.struts.validator.DynaValidatorForm" />
<jsp:useBean id="coiReviewer" scope="session" class="java.util.Vector" />
<jsp:useBean id="disclosureStatus" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="disclHeader" scope="session"
	class="org.apache.struts.validator.DynaValidatorForm" />
<jsp:useBean id="coiHeaderInfo" scope="session"
	class="org.apache.struts.validator.DynaValidatorForm" />
<jsp:useBean id="userprivilege" class="java.lang.String" scope="session" />
<jsp:useBean id="person" scope="session"
	class="edu.mit.coeus.bean.PersonInfoBean" />

<priv:hasOSPRight name="hasOspRightToEdit"
	value="<%=Integer.parseInt(userprivilege)%>">
	<jsp:useBean id="hasMaintainCOI" class="java.lang.String"
		scope="session" />
</priv:hasOSPRight>


<% DynaValidatorForm formdata = (DynaValidatorForm)session.getAttribute("dynaForm");%>

<html:html>
<head>
<title>Coeus Web</title>
<html:base />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
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
        
        function changeAllReviewStatus(){
            var conflictStatus = document.forms[0].reviewerCode.value;
            for(var elementIndex=0; elementIndex < document.forms[0].elements.length; elementIndex++){
                var elementName = document.forms[0].elements[elementIndex].name;
                if(elementName.indexOf("sltReviewerCode") != -1) {
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
            }
            else
            {
              return validateCoiDisclosure(form);
            }
        }
        
        function showQuestion(link)
        {
            var winleft = (screen.width - 650) / 2;
            var winUp = (screen.height - 450) / 2;  
            var win = "scrollbars=1,resizable=1,width=790,height=450,left="+winleft+",top="+winUp
            sList = window.open('<bean:write name='ctxtPath'/>'+"/"+link, "list", win);
            if (parseInt(navigator.appVersion) >= 4) {
            window.sList.focus(); 
            }
        }
        
        // Case 3513 - START
        function limitText(limitField, limitNum) {
            if (limitField.value.length > limitNum) {
                limitField.value = limitField.value.substring(0, limitNum);
            } 
        }
        //Case 3513 - END
        
        /**
         * Sets the hidden field updated to 'U' if the combobox, or the desc textfield have been modified.
         */
        function entityUpdated(index) {
            var element = "updated"+index;
            document.getElementById(element).value = 'U';
        }
        
        var maxId = <%=personDescDet.size()%>
        /**
         * Sets the hidden field updated to 'U' if the setConflictStatus or reviewedby combobox have been modified.
         */
        function allEntitiesUpdated() {
            for(index=0; index < maxId; index++) {
                entityUpdated(index);
            }
        }
    </script>
<html:javascript formName="coiDisclosure" dynamicJavascript="true"
	staticJavascript="true" />
<body>
	<%--  ************  START OF BODY TABLE  ************************--%>
	<html:form action="/updCoiDisclosure.do" method="post"
		onsubmit="return validateForm(this)">

		<div id="disclFormDiv">
			<table width="100%" border="0" cellpadding="0" cellspacing="0"
				class="table">

				<tr>
					<td height="20" align="left" valign="top" class="theader"><bean:message
							bundle="coi" key="coiDisclosure.headerDiscldetails" /> - <%= person.getFullName() %>
					</td>
				</tr>
				<tr>
					<td class="copybold">&nbsp;&nbsp; <b><font color="red">*</font></b>
						<font class="fontBrown"> <bean:message bundle="coi"
								key="coi.indicatesReqField" />
							<%--indicates required field--%></font>
					</td>
				</tr>

				<tr align="left">
					<td class='copy' valign="top"><font color="red"> <logic:messagesPresent
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


				<!-- EntityDetails - Start  -->
				<tr>
					<td height="119" align="left" valign="top">
						<table width="99%" border="0" align="center" cellpadding="0"
							cellspacing="0" class="tabtable">
							<tr>
								<td colspan="4" align="left" valign="top"><table
										width="100%" height="20" border="0" cellpadding="0"
										cellspacing="0" class="tableheader">
										<tr>
											<td><bean:message bundle="coi"
													key="financialEntity.subheaderEntityDet" /></td>
										</tr>
									</table></td>
							</tr>

							<tr>
								<td>

									<table width="100%" border="0" cellspacing="0" cellpadding="6">
										<tr>
											<logic:present name="dynaForm" scope="session">
												<logic:equal name="dynaForm" property="disclosureTypeCode"
													value="1">
													<td nowrap class="copybold" width="10%" align="left"
														valign="top">&nbsp;&nbsp;<font color="red">*</font>
													<bean:message bundle="coi" key="label.awardTitle" /> </b></td>
													<td width="6" valign="top">
														<div align="left">:</div>
													</td>
												</logic:equal>

												<logic:notEqual name="dynaForm"
													property="disclosureTypeCode" value="1">
													<td nowrap class="copybold" width="10%" align="left"
														valign="top">&nbsp;&nbsp;<font color="red">*</font>
													<bean:message bundle="coi" key="label.proposalTitle" /> </b></td>
													<td width="6" valign="top">
														<div align="left">:</div>
													</td>
												</logic:notEqual>

												<logic:equal name="dynaForm" property="disclosureTypeCode"
													value="3">
													<td colspan="2" class='copy'>
														<%--<html:textarea  property="title" cols="50" value="<%= objValue%>"/>--%>
														<textarea name="title" cols="70" rows="3" class='copy'><bean:write
																name="dynaForm" scope="session" property="title" /></textarea>
													</td>
												</logic:equal>
												<%--Case 3513 - START--%>
												<logic:notEqual name="dynaForm"
													property="disclosureTypeCode" value="3">
													<td colspan="2" class='copy'><textarea name="title"
															readonly="true" cols="70" rows="3" class='copy'
															onchange="limitText(this.form.title, 150)"><bean:write
																name="dynaForm" scope="session" property="title" /></textarea></td>
												</logic:notEqual>
												<%-- Case 3513 - END --%>
											</logic:present>

											<td class="copybold" align="right" valign="bottom"><bean:message
													bundle="coi" key="label.createDate" /></td>
											<td class="copybold" valign="bottom">:</td>
											<td class="copy" valign="bottom">
												<%
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = simpleDateFormat.parse((String) request.getAttribute("createTimestamp"));
            SimpleDateFormat requiredDateFormat = new SimpleDateFormat("MM-dd-yyyy  hh:mm a");
            String output = requiredDateFormat.format(date);
            out.print(output);%> &nbsp; <%= request.getAttribute("createUser")%>
											</td>
										</tr>

										<tr>
											<logic:equal name="dynaForm" property="disclosureTypeCode"
												value="3">
												<td nowrap class="copybold" width="10%" align="left"
													valign="top">&nbsp;&nbsp;<font color="red">*</font>
												<bean:message bundle="coi" key="label.proposalType" /><b></b></td>
												<td width="6">
													<div align="left">:</div>
												</td>
												<td colspan="2"><select name="proposalTypeCode"
													class='textbox-long'>
														<%
                                        String preselectedTypeCode = (String)dynaForm.get("proposalTypeCode");
                                        
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
												</select> <%-- <html:select property ="proposalTypeCode">                                                
                            <html:options collection="proposalType" property="code" labelProperty="description"/>
                            </html:select>--%></td>
											</logic:equal>
										</tr>

										<tr>
											<logic:equal name="dynaForm" property="disclosureTypeCode"
												value="3">
												<td nowrap class="copybold" width="10%" align="left"
													valign="top">&nbsp;&nbsp;<font color="red">*</font>
												<bean:message bundle="coi" key="label.sponsor" /><b></b>
												</td>
												<td width="6"><div align="left">:</div></td>
												<td colspan="2">
													<%String objValue = formdata==null ? "" : (String)formdata.get("sponsorName");%>
													<html:text size="25" property="sponsorName"
														styleClass="textbox-long" value="<%=objValue%>" />
												</td>
											</logic:equal>
											<logic:notEqual name="dynaForm" property="disclosureTypeCode"
												value="3">
												<td nowrap class="copybold" width="10%" align="left"
													valign="top">&nbsp;&nbsp;<font color="red">*</font>
												<bean:message bundle="coi" key="label.sponsor" /><b></b>
												</td>
												<td width="6"><div align="left">:</div></td>
												<td colspan="2">
													<%String objValue = formdata==null ? "" : (String)formdata.get("sponsorName");%>
													<html:text size="25" readonly="true" property="sponsorName"
														styleClass="textbox-long" value="<%=objValue%>" />
												</td>
											</logic:notEqual>

											<td nowrap class="copybold" width="123" align="right"><bean:message
													bundle="coi" key="label.lastUpdated" /></td>
											<td>:</td>
											<td class="copy" width="238" nowrap colspan="2"><coeusUtils:formatDate
													name="disclHeader" formatString="MM-dd-yyyy  hh:mm a"
													property="updtimestamp" /> &nbsp; <bean:write
													name="disclHeader" property="upduser" /></td>

										</tr>
									</table>
								</td>
							</tr>
							<!--<tr>
                  <td>
                      &nbsp;
                  </td>
              </tr>  -->

						</table>
					</td>
				</tr>
				<tr>
					<td height='10'>&nbsp;</td>
				</tr>
				<!-- Certificate - Start -->
				<tr>
					<td align="center" valign="top">
						<!--<DIV STYLE="overflow: auto; width: 900px; height: 165; padding:0px; margin: 0px">-->
						<table width="965" border="0" cellpadding="2" cellspacing="0"
							class="tabtable">
							<tr>
								<td width="80%" align="center" class="theader"><bean:message
										bundle="coi" key="label.question" /></td>
								<td width="20%" align="center" class="theader"><bean:message
										bundle="coi" key="label.answer" /></td>
							</tr>
							<logic:present name="questionsData" scope="session">
								<% 
                                    String preSelectedQuestions[] =(String[])session.getAttribute("selectedQuestions");
                                    String preSelectedAnswers[] =(String[])session.getAttribute("selectedAnswers");
                                    String strBgColor = "#EFEFEF";
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
                                                    strBgColor = "FBF7F7"; 
                                            } else { 
                                                    strBgColor="EFEFEF"; 
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
                                                }
                                                else if(answer.equals("N") ){
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
								<tr class="rowLine" onmouseover="className='rowHover rowLine'"
									onmouseout="className='rowLine'">
									<td width="80%" class="copy" valign="top"><logic:present
											name="certQuestionErrors" scope="request">
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
                                    if(errorCertCode != null &&  errorCertCode.equals(questionId)){
                                            errorObject = certQuestionErrors.get(errorCertCode);

                                            if(errorObject.getClass().getName().equals("java.util.HashMap")){
                                                    entitiesWithYes = (HashMap)certQuestionErrors.get(errorCertCode);

                                            }
                                            else{
                                                    errorCode = certQuestionErrors.get(errorCertCode).toString();

                                            }
                                    }	
                                    //If error is because user answered No, and has one or more entities
                                    //with answer Yes, get the list of entities with Yes answer.
                                    if(entitiesWithYes != null){
                                            if(entitiesWithYes.size() == 1){

                              %>
											<font color="red"> <bean:message
													key="error.addCOIDisclosure.entYesAnsNoForOne" bundle="coi" />
												<%=correspEntQuestLabel %> <bean:message
													key="error.addCOIDisclosure.entYesAnsNo2ForOne"
													bundle="coi" />
											</font>
											<%
                                     }
                                        else{

                            %>
											<font color="red"> <bean:message
													key="error.addCOIDisclosure.entYesAnsNoForTwo" bundle="coi" />
												<%=correspEntQuestLabel%> <bean:message
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
												href="<bean:write name='ctxtPath'/>/editFinEnt.do?actionFrom=editDiscl&entityNumber=<%=entityNumber%>">
												<%=entityName%></a>
											<%
                                        if(cnt<(entitiesWithYes.size()-1)){
                                                out.print(",");
                                        }
                                }

                                            if(entitiesWithYes.size() == 1){
                              %>
											<font color="red"> <bean:message
													key="error.addCOIDisclosure.entYesAnsNo3ForOne"
													bundle="coi" /> <%=correspEntQuestLabel%> <bean:message
													key="error.addCOIDisclosure.entYesAnsNo4ForOne"
													bundle="coi" />
											</font>
											<%
                                            }
                                            else{
                            %>
											<font color="red"> <bean:message
													key="error.addCOIDisclosure.entYesAnsNo3ForTwo"
													bundle="coi" /> <%=correspEntQuestLabel%> <bean:message
													key="error.addCOIDisclosure.entYesAnsNo4ForTwo"
													bundle="coi" />
											</font>
											<%
                                            }		

                                    }
                                    //User has answered Yes but has no financial entities
                                    else if(errorCode != null && errorCode.equals("-1")){

                            %>
											<font color="red"> <bean:message
													key="error.addCOIDisclosure.noFinEntAnsReqExplForOne"
													bundle="coi" /><br> <html:link
													action="/getAddFinEnt.do?actionFrom=editDiscl">
													<bean:message bundle="coi" key="label.addFinancialEntity" />
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
													key="error.addCOIDisclosure.entNoAnsYes" bundle="coi" /> <%=correspEntQuestLabel%>
												<bean:message key="error.addCOIDisclosure.entNoAnsYes2"
													bundle="coi" /> <a
												href="<bean:write name="ctxtPath"/>/getReviewFinEnt.do?actionFrom=editDiscl">
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
										</logic:present> <b><bean:message bundle="coi" key="label.question" /><%=dynaValidatorForm.get("label")%></b>
										&nbsp;:&nbsp;<%=dynaValidatorForm.get("description")%> <a
										href='javascript:showQuestion("/question.do?questionNo=<%=dynaValidatorForm.get("questionId")%>",  "ae");'
										method="POST"> <u> <bean:message bundle="coi"
													key="financialEntity.moreAbtQuestion" />
										</u>
									</a></td>
									<td width="20%" valign="top">
										<table border="0" width="100%" cellpadding="0" cellspacing="0"
											border="0" class="copy">
											<tr align="center">
												<td style="border-style: solid none; border-width: 0px 0;">
													<bean:message bundle="coi" key="financialEntity.yes" /><br>
													<input type="radio" name="<%= questionId%>" value="Y"
													<%=checkedYes%> class='copy'>
												</td>
												<td style="border-style: solid none; border-width: 0px 0;">
													<bean:message bundle="coi" key="financialEntity.no" /><br>
													<input type="radio" name="<%= certCode%>" value="N"
													<%=checkedNo%> class='copy'>
												</td>
												<td style="border-style: solid none; border-width: 0px 0;">
													<% if (noOfAns != null && Integer.parseInt(noOfAns) > 2) {%>
													<bean:message bundle="coi" key="financialEntity.na" /><br>
													<input type="radio" name="<%= certCode%>" value="X"
													<%=checkedNA%> class='copy'> <%}%>
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<% }certificateIndex++;//End For%>
							</logic:present>
						</table> <!--</DIV>-->

					</td>
				</tr>
				<!-- Certificate -End -->

				<!--FinancialEntitesDisclosed -Start -->

				<tr>
					<td align="left" valign="top"><br>
						<table width="99%" border="0" align="center" cellpadding="3"
							cellspacing="0" class="tabtable">
							<tr class="tableheader">
								<!--<td align="left" valign="top">
                              <table width="100%" height="20"  border="0" cellpadding="0" cellspacing="0" class="tableheader">
                              <tr>-->
								<td><bean:message bundle="coi"
										key="coiDisclosure.headerFinEntities" /></td>
								<!--</tr>
                          </table></td>-->
							</tr>

							<tr>
								<td class='copy' style=''>&nbsp; &nbsp; <bean:message
										bundle="coi" key="label.setConflictStatus" />
									<%--Set conflict status for all entities to:--%> <select
									name="conflictStatus" style=''
									onchange="changeAllStatus(),allEntitiesUpdated();">
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
								</select> <logic:present name="hasMaintainCOI">
										<bean:message bundle="coi" key="label.setReviewedBy" />
										<select name='reviewerCode' style=''
											onchange="changeAllReviewStatus(), allEntitiesUpdated();">
											<option selected>-
												<bean:message bundle="coi" key="label.pleaseSelect" />
												<%
                                        int size = coiReviewer.size();
                                        for(int i=0; i<size; i++) {
                                            String selected = "";
                                            ComboBoxBean comboBox = (ComboBoxBean)coiReviewer.get(i);
                                            String revCode = comboBox.getCode();
                                            String revDescription = comboBox.getDescription();
                                            
                                        %>
											
											<option value="<%= revCode %>"><%= revDescription %>
											</option>
											<%
                                        }%>

										</select>
									</logic:present>
								</td>
							</tr>


							<tr align="center">
								<td>
									<%--<DIV STYLE="overflow: auto; width: 968px; height: 165; padding:0px; margin: 0px">--%>
									<table width="99%" border="0" cellpadding="0" class="tabtable"
										border="3">
										<tr>
											<td width="25%" class="theader"><bean:message
													bundle="coi" key="label.entityName" /></td>
											<td class="theader" width="10%" colspan="2"><bean:message
													bundle="coi" key="label.conflictStatus" /></td>
											<td class="theader" width="10%" height="25"><bean:message
													bundle="coi" key="label.reviewedBy" /></td>
											<td class="theader" width="55%" height="25"><bean:message
													bundle="coi" key="label.relationshipDesc" /></td>
										</tr>
										<logic:present name="personDescDet" scope="session">
											<%  int disclIndex = 0;
                         String strBgColor = "#DCE5F1";
                             
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
                            String updated = (String)personDet.get("updated");
                            if(updated != null && Integer.parseInt(updated)>0) {
                                updated = "U";
                            }else {
                                updated = "";
                            }
                            %>
												<!-- Case 4305 -->
												<INPUT type='hidden' name='updated<%=disclIndex%>'
													id='updated<%=disclIndex%>' value='<%=updated%>'>
												<!-- Case 4305 -->
												<%                                  
                                           if (disclIndex%2 == 0) {
                                                strBgColor = "#D6DCE5"; 
                                            }
                                           else { 
                                                strBgColor="#DCE5F1"; 
                                             }
                                        %>

												<tr bgcolor='<%=strBgColor%>' class="rowLine">
													<td width="20%" class="copy"><coeusUtils:formatOutput
															name="personDet" property="entityName" /></td>
													<td width="15%" colspan="2" class="copy"><logic:present
															name="hasMaintainCOI">
															<SELECT ONCHANGE="entityUpdated('<%=disclIndex%>')"
																name='sltConflictStatus<%=disclIndex%>'
																style='width: 125px;'>
																<%
                                        String strConfStatCode = (String)personDet.get("coiStatusCode");
                                        int lstSize = coiStatus.size();
                                        for(int i=0;i<lstSize;i++){
                                                ComboBoxBean objCombBean = (ComboBoxBean)coiStatus.get(i);
                                                String code = objCombBean.getCode();
                                                String description = objCombBean.getDescription();
                                                String strSelected = "";
                                                if(code.equalsIgnoreCase(strConfStatCode)) {
                                                    strSelected = "selected";
                                                }
                                                %>
																<option <%= strSelected %> value="<%= code %>"><%= description %></option>
																<%
                                        }
                                        %>
															</SELECT>
														</logic:present> <logic:notPresent name="hasMaintainCOI">
															<!--  Dynamically generate drop down list.  Show the selected value as well as description for codes 301 and 200.  -->
															<SELECT ONCHANGE="entityUpdated('<%=disclIndex%>')"
																name='sltConflictStatus<%=disclIndex%>'
																style='width: 125px;'>
																<%
                                    String strConfStatCode = (String)personDet.get("coiStatusCode");
                                    int lstSize = coiStatus.size();
                                    for(int i=0;i<lstSize;i++){
                                            ComboBoxBean objCombBean = (ComboBoxBean)coiStatus.get(i);
                                            String code = objCombBean.getCode();
                                            String description = objCombBean.getDescription();
                                            if(code.equalsIgnoreCase(strConfStatCode)) {
                                            %>
																<option selected value="<%= code %>"><%= description %></option>
																<%
                                            }
                                    }
                                    for(int i=0;i<lstSize;i++){
                                            ComboBoxBean objCombBean = (ComboBoxBean)coiStatus.get(i);
                                            String code = objCombBean.getCode();
                                            String description = objCombBean.getDescription();
                                            if(!code.equalsIgnoreCase(strConfStatCode) && (code.equalsIgnoreCase("301") || code.equalsIgnoreCase("200"))) {
                                            %>
																<option value="<%= code %>"><%= description %></option>
																<%
                                        }
                                    }

                                    %>
															</SELECT>
														</logic:notPresent></td>

													<td width="10%" align="center" class="copy">
														<!-- check for maintain COI role  --> <logic:present
															name="hasMaintainCOI">
															<select ONCHANGE="entityUpdated('<%=disclIndex%>')"
																name='sltReviewerCode<%=disclIndex%>'
																style='width: 75px;'>
																<%
                                            String reviewerCode = (String)personDet.get("coiReviewerCode");
                                            int revListSize = coiReviewer.size();
                                            for(int i=0; i<revListSize; i++)
                                            {
                                                    String selected = "";
                                                    ComboBoxBean comboBox = (ComboBoxBean)coiReviewer.get(i);
                                                    String revCode = comboBox.getCode();
                                                    String revDescription = comboBox.getDescription();
                                                    if(revCode.equalsIgnoreCase(reviewerCode))
                                                    {
                                                            selected = "selected";
                                                    }
                                            %>
																<option <%= selected %> value="<%= revCode %>"><%= revDescription %>
																</option>
																<%
                                            }
                                            %>
															</select>
														</logic:present> <logic:notPresent name="hasMaintainCOI">
															<bean:write name="personDet" property="coiReviewer" />
														</logic:notPresent>
													</td>
													<td>
														<%--<html:text property = "relationShipDesc" styleClass="textbox-long" maxlength="1000"/> --%>
														<%  String relnDescription = (String)personDet.get("description");
                                        if(relnDescription == null ){
                                            relnDescription = "";
                                       }%> <INPUT type='text' size='75'
														ONCHANGE="entityUpdated('<%=disclIndex%>')"
														name='relationShipDesc<%=disclIndex%>'
														value='<%=relnDescription%>' maxlength="1000">
													</td>
												</tr>
												<% disclIndex++ ;%>
											</logic:iterate>
										</logic:present>
									</table> <%-- </div>--%>
								</td>
							</tr>
							<% 	int userpriv = Integer.parseInt(userprivilege);
                if(userpriv > 0 ){
            %>
							<%-- This Block Depends on the Maintain COI Rights Will be moved to another Jsp--%>
							<%--<logic:present name="hasMaintainCOI" scope="request">--%>
							<tr>
								<td>
									<table width="100%" border='0' cellspacing='0'>
										<tr>
											<td width="5" height="30">&nbsp;</td>
											<td width="100" class='copybold' align="left" class="copy">
												<div>
													<bean:message bundle="coi" key="label.personName" />
												</div>
											</td>
											<td width="6"><div>:</div></td>
											<td width="176" class='copy' align="left">
												<div>
													<bean:write name="disclHeader" scope="session"
														property="fullName" />
												</div>
											</td>
											<td width="123" align="left" class="copybold">
												<div>
													<bean:message bundle="coi" key="label.disclosureNumber" />
												</div>
											</td>
											<td width="3"><div>:</div></td>
											<td width="238" class='copy' align="left">
												<div>
													<bean:write name="disclHeader" scope="session"
														property="coiDisclosureNumber" />
												</div>
											</td>
										</tr>

										<tr>
											<td width="5" height="30">&nbsp;</td>
											<td width="100" align="left" class="copybold">
												<div>
													<bean:message bundle="coi" key="label.appliesTo" />
												</div>
											</td>
											<td width="6"><div>:</div></td>
											<td width="176" class='copy' align="left">
												<div>
													<bean:write name="disclHeader" scope="session"
														property="module" />
												</div>
											</td>
											<td width="123" align="left" class="copybold">
												<div>
													<bean:message bundle="coi" key="label.awardNumber" />
												</div>
											</td>
											<td width="3"><div>:</div></td>
											<td width="238" class='copy' align="left">
												<div>
													<bean:write name="disclHeader" scope="session"
														property="moduleItemKey" />
												</div>
											</td>
										</tr>

										<tr>
											<td width="5" height="30">&nbsp;</td>
											<td width="100" align="left" class="copybold">
												<div>
													<bean:message bundle="coi" key="label.costObject" />
												</div>
											</td>
											<td width="6"><div>:</div></td>
											<td width="176" class='copy' align="left">
												<div>
													<bean:write name="coiHeaderInfo" scope="session"
														property="account" />
												</div>
											</td>
											<td width="123" align="left" class="copybold">
												<div>
													<bean:message bundle="coi" key="label.sponsor" />
												</div>
											</td>
											<td width="3"><div>:</div></td>
											<td width="238" class='copy' align="left">
												<div>
													<bean:write name="dynaForm" scope="session"
														property="sponsorName" />
												</div>
											</td>
										</tr>

										<tr>
											<td width="5" height="30">&nbsp;</td>
											<td width="100" align="left" class="copybold">
												<div>
													<bean:message bundle="coi" key="label.disclosureType" />
												</div>
											</td>
											<td width="6"><div>:</div></td>
											<td width="176" class="copy" align="left">
												<div>
													<html:select property='disclosureType'
														styleClass="textbox-long">
														<html:option value="I">
															<bean:message bundle="coi" key="label.initial" />
															<%--Initial--%>
														</html:option>
														<html:option value="A">
															<bean:message bundle="coi" key="label.annual" />
															<%--Annual--%>
														</html:option>
													</html:select>
												</div>
											</td>
											<td width="123" align="left" class="copybold">
												<div>
													<logic:equal name="moduleCode" scope="session" value="1">
														<bean:message bundle="coi" key="label.awardStatus" />
													</logic:equal>
													<logic:notEqual name="moduleCode" scope="session" value="1">
														<bean:message bundle="coi" key="label.proposalStatus" />
													</logic:notEqual>
												</div>
											</td>
											<td width="3"><div>:</div></td>
											<td width="238" class="copy" align="left">
												<div>
													<bean:write name="coiHeaderInfo" scope="session"
														property="awardName" />
												</div>
											</td>
										</tr>

										<tr>
											<td width="5" height="30">&nbsp;</td>
											<td width="100" align="left" class="copybold">
												<div>
													<bean:message bundle="coi" key="label.reviewedBy" />
												</div>
											</td>
											<td width="6"><div>:</div></td>
											<td width="176" align="left">
												<div>
													<%--<logic:equal name="action" value="edit" >--%>
													<!-- check for maintain coi disclosure role	If the user has this role, then make reviewed by field editable.-->
													<logic:present name="hasMaintainCOI">
														<div>
															<b> <select name='disclReviewerCode'
																Class='textbox-long'>
																	<%
                                        String discReviewerCode = (String)disclHeader.get("coiReviewerCode");
                                        int revListSize = coiReviewer.size();
                                        
                                        for(int i=0; i<revListSize; i++)
                                        {
                                                String selected = "";				
                                                ComboBoxBean comboBox = (ComboBoxBean)coiReviewer.get(i);
                                                String revCode = comboBox.getCode();
                                                String revDescription = comboBox.getDescription();
                                                if(revCode.equalsIgnoreCase(discReviewerCode))
                                                {
                                                        selected = "selected";
                                                }
                                        %>
																	<option <%= selected %> value="<%= revCode %>"><%= revDescription %>
																	</option>
																	<%
                                        }
                                        %>
															</select>
															</b>
														</div>
													</logic:present>

													<logic:notPresent name="hasMaintainCOI">
														<bean:write name="disclHeader" scope="session"
															property="reviewer" />
                                &nbsp;
                                <input type="hidden"
															name="disclReviewerCode"
															value="<bean:write name='disclHeader' scope='session' property='reviewer'/>" />
													</logic:notPresent>
													<%--</logic:equal>--%>
												</div>
											</td>

											<td width="123" align="left" class="copybold">
												<div>
													<bean:message bundle="coi" key="label.disclosureStatus" />
												</div>
											</td>
											<td width="3"><div>:</div></td>
											<td width="238" align="left">
												<div>
													<b> <%--  <logic:equal name="action" value="edit" >--%>
														<!-- Check for maintain coi disclosure role.  If user has this role, then make disclosure status editable.  	-->
														<logic:present name="hasMaintainCOI">
															<div>
																<select name='disclStatCode' class='textbox-long'>
																	<%
                                            String disclStatCode = (String)disclHeader.get("coiDisclosureStatusCode");
                                            int statListSize = disclosureStatus.size();
                                            for(int i=0; i<statListSize; i++)
                                            {
                                                    String selected = "";
                                                    ComboBoxBean comboBox = (ComboBoxBean)disclosureStatus.get(i);
                                                    String statCode = comboBox.getCode();
                                                    String statDescription = comboBox.getDescription();
                                                    if(statCode.equalsIgnoreCase(disclStatCode))
                                                    {
                                                            selected = "selected";
                                                    }
                                            %>
																	<option <%= selected %> value="<%= statCode %>"><%= statDescription %>
																	</option>
																	<%
                                            }
                                            %>

																</select>
															</div>
														</logic:present> <logic:notPresent name="hasMaintainCOI">
															<bean:write name="disclHeader" scope="session"
																property="coiDisclosureStatusCode" />
                                &nbsp;
                                <input type="hidden"
																name="disclStatCode"
																value='<bean:write name="disclHeader" scope="session" property="coiDisclosureStatusCode"/>' />
														</logic:notPresent> <%--</logic:equal>--%> <%--<logic:notEqual name="action" value="edit">
                            <b><bean:write name="disclHeader" scope="session" property="disclStatus"/></b>&nbsp;
                            </logic:notEqual></b> </div> --%>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<%--</logic:present>--%>
							<%
                        }
                    %>
							<%-- End Here--%>
							<logic:notPresent name="hasMaintainCOI">
								<input type="hidden" name="disclStatCode"
									value='<bean:write name="disclHeader" scope="session" property="coiDisclosureStatusCode"/>' />
							</logic:notPresent>

							<logic:present name="showSyncButton" scope="request">
								<table width='70%'>
									<tr class='copy'>
										<td width="30%"><bean:message
												key="editCOIDisclosure.sync.message" bundle="coi" />
											&nbsp;&nbsp; <html:image page="/images/sync.gif" border="0"
												alt="Synchronize Financial Entities"
												property="Synchronize Financial Entities" /></td>
									</tr>
								</table>
							</logic:present>

							<tr>

							</tr>

							<tr>
								<td height='10'>&nbsp;</td>
							</tr>

						</table></td>
				</tr>
				<!--FinancialEntitesDisclosed -End -->

				<html:hidden property="coiDisclosureNumber" />
				<html:hidden property="disclosureTypeCode" />
				<html:hidden property="disclosureType" />
				<html:hidden property="acType" value="U" />
				<html:hidden property="personId" />
				<html:hidden property="sequenceNum" />
				<html:hidden property="appliesToCode" />
				<html:hidden property="fullName" />
				<tr>
					<td colspan="3" nowrap><br>&nbsp;&nbsp;<html:submit
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
	<% if(request.getAttribute("FESubmitSuccess") != null){
	request.removeAttribute("FESubmitSuccess");
	
	String message = (String)request.getAttribute("entityName");
	String actionType = (String)request.getAttribute("actionType");       
	if(actionType != null){
		if(actionType.equals("I")){
			message += " successfully added.";
		}
		else if(actionType.equals("U") ){
			message += " successfully modified.";
		}
	}
	int indexOfApost = message.indexOf("'");
	if(indexOfApost != -1){
		int indexOfApostIncr = indexOfApost+1;
		int endOfMessage = message.length();
		String message1 = message.substring(0, indexOfApost);
		String message2 = message.substring(indexOfApostIncr, message.length());
		message = message1+message2;
	}	
	out.print("<script language='javascript'>");
	out.print("confirm('"+message+"');");        
	out.print("</script>");
}

%>
</body>
</html:html>



