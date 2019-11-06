<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="org.apache.struts.action.DynaActionForm"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@ page
	import="edu.mit.coeuslite.utils.CoeusLiteConstants,
            edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean,
            java.util.HashMap, java.util.Vector"%>
<jsp:useBean id="proposalList" scope="session" class="java.util.Vector" />
<jsp:useBean id="proposalColumnNames" scope="session"
	class="java.util.Vector" />

<% 
    String proposalNumber = (String)session.getAttribute("proposalNumber"+session.getId());
    String mode=(String)session.getAttribute("mode"+session.getId()); 
    String modifyCompletedQuestionnaire = (String)session.getAttribute("modifyCompletedQuestionnaire");
    String operation = (String)request.getAttribute("operation");
    
    boolean modeValue=false;
    boolean originalMode = false;
    if(mode!=null && !mode.equals("")){   
         if(mode.equalsIgnoreCase("display") || mode.equalsIgnoreCase("D")){
           //     modeValue=true;
           //     originalMode = true;
         }
    }
    QuestionnaireAnswerHeaderBean questionnaireModuleObject = 
            (QuestionnaireAnswerHeaderBean)session.getAttribute("questionnaireModuleObject");
    if(questionnaireModuleObject == null){
        questionnaireModuleObject = new QuestionnaireAnswerHeaderBean();
    }
    int count = 0;
    HashMap hmQuestionnaireData = (HashMap)session.getAttribute("questionnaireInfo");
    hmQuestionnaireData = (hmQuestionnaireData == null)? new HashMap() : hmQuestionnaireData;
    String mess = (String) hmQuestionnaireData.get("message");
    if(mess != null && mess.equals("COMPLETED")){
        modeValue=true;
    }
    Vector vecQuestionnaireData =(Vector) hmQuestionnaireData.get("dataObject");
    String buttonName = "Save & Proceed";
    if(vecQuestionnaireData != null && vecQuestionnaireData.size() > 2){
        String totalPages = (String) vecQuestionnaireData.get(2);
        if(totalPages != null && totalPages.equals("1")){
            buttonName = "Save";
        }
    }
    String pageNo = (String) hmQuestionnaireData.get("page");
    //String mess = "";
    // 4272: Maintain History of Questionnaires
    String newQuestionnaireVersion = (String) request.getAttribute("newQuestionnaireVersion");
    // COEUSDEV-230: Answered questionnaire says it is not Answered in Approval in Progress Proposal - Start
    String actionFrom = (String)session.getAttribute("actionFrom");
    Integer questionnaireId = (Integer)session.getAttribute("questionnaireId"+session.getId());
    String questionnaireLabel = (String)session.getAttribute("questionaireLabel");
    String menuId = (String)session.getAttribute("menuCode");
    // COEUSDEV-230: Answered questionnaire says it is not Answered in Approval in Progress Proposal -End
    %>
<html:html>
<head>
<title>Coeus Web</title>
<html:base />
<script language="javascript" type="text/JavaScript"
	src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/coeusLiteCalendar.js"></script>
</head>

<style>
.textbox-longer {
	width: 400px;
}
</style>

<script>
<%if("SAVE_COMPLETE".equals(operation) &&  !"COMPLETED".equals(mess)){%>
    alert("<bean:message bundle="coeus" key="questionnaire_exceptionCode.1020"/>");
<%}%>
var index = "" ;
var errValue = false;
var errLock = false;
function openLookupWindow(lookupWin,lookupVal, lookupArgument, count) {
    index = count;
    var linkValue = 'generalProposalSearch.do';
    var winleft = (screen.width - 650) / 2;
    var winUp = (screen.height - 450) / 2;  
    var win = "scrollbars=1,resizable=0,width=800,height=450,left="+winleft+",top="+winUp
    //Added for CASE#COEUSDEV-230-Answered questionnaire says it is not Answered in Approval in Progress Proposal.-start
    if(lookupWin == "ORGANIZATIONSEARCH"){
        lookupWin = "w_organization_select" ;
        lookupVal = "ORGANIZATIONSEARCH" ;
        lookupArgument =  "w_organization_select" ;
    }
     else if(lookupWin == "SPONSORSEARCH"){
        lookupWin = "w_sponsor_select" ;
        lookupVal = "SPONSORSEARCH" ;
        lookupArgument =  "w_sponsor_select" ;
    }
    //Added for CASE#COEUSDEV-230-Answered questionnaire says it is not Answered in Approval in Progress Proposal.end
    else if(lookupWin == "ROLODEXSEARCH"){
        lookupWin = "w_rolodex_select" ;
        lookupVal = "ROLODEXSEARCH" ;
        lookupArgument =  "w_rolodex_select" ;
    }else if(lookupWin == "PERSONSEARCH"){
        lookupWin = "w_person_select" ;
        lookupVal = "PERSONSEARCH" ;
    }else if(lookupWin == "CODETABLE" ){
        lookupWin = "w_arg_code_tbl" ;        
        lookupArgument = lookupVal;
    }else if(lookupWin == "SELECTCOSTELEMENT"){
        lookupWin = "w_select_cost_element" ;
        lookupArgument = "w_select_cost_element" ;
        lookupVal = "Cost Element" ;
    }else if(lookupWin == "UNITSEARCH"){
        lookupWin = "w_unit_select" ;
        lookupVal = "UNITSEARCH" ;
        lookupArgument =  "w_unit_select" ;
    }else if(lookupWin = "VALUELIST"){
        lookupWin = "w_arg_value_list" ;
        lookupArgument = lookupVal ;        
    }
    
    if((lookupWin == "w_arg_value_list") || (lookupWin == "w_arg_code_tbl") || (lookupWin == "w_select_cost_element")){
        linkValue = 'getArgumentData.do';
        var win = "scrollbars=1,resizable=0,width=580,height=300,left="+winleft+",top="+winUp
    }
    link = linkValue+'?type='+lookupVal+'&search=true&searchName='+lookupWin+'&argument='+lookupArgument;        
    sList = window.open('<bean:write name='ctxtPath'/>'+"/"+link, "list", win);
}
function fetch_Data(result,searchType){
    dataChanged();
    if(searchType == "UNITSEARCH"){ 
        var currentValue = document.getElementsByName('dynaFormData['+index+'].answer'); 
        //Added for the case# COEUSDEV-194 Questionnaire in Lite - value list box doesn't close start
        //Checking for null values 
        if( currentValue != null && currentValue.length > 0 ){
        currentValue[0].value = result["UNIT_NUMBER"];
        }
        var currentDesc = document.getElementsByName('dynaFormData['+index+'].searchName');  
        if(currentDesc != null  &&  currentDesc.length > 0){
        currentDesc[0].value = result["UNIT_NAME"];
        }
    }else if(searchType == "PERSONSEARCH"){
        var currentValue = document.getElementsByName('dynaFormData['+index+'].answer');   
        if( currentValue != null && currentValue.length > 0 ){
        currentValue[0].value = result["PERSON_ID"];
        }
        var currentDesc = document.getElementsByName('dynaFormData['+index+'].searchName');         
        if(currentDesc != null  &&  currentDesc.length > 0){
        currentDesc[0].value = result["FULL_NAME"];
        }
    }
     //Added for CASE#COEUSDEV-230-Answered questionnaire says it is not Answered in Approval in Progress Proposal.-start
    else if(searchType == "ORGANIZATIONSEARCH"){
        var currentValue = document.getElementsByName('dynaFormData['+index+'].answer'); 
        if( currentValue != null && currentValue.length > 0 ){
            currentValue[0].value = result["ORGANIZATION_ID"];
        }
        var currentDesc = document.getElementsByName('dynaFormData['+index+'].searchName');   
        if( currentDesc != null && currentDesc.length > 0 ){
            currentDesc[0].value = result["ORGANIZATION_NAME"];
        }
    }
     else if(searchType == "SPONSORSEARCH"){
        var currentValue = document.getElementsByName('dynaFormData['+index+'].answer'); 
        if( currentValue != null && currentValue.length > 0 ){
            currentValue[0].value = result["SPONSOR_CODE"];
        }
        var currentDesc = document.getElementsByName('dynaFormData['+index+'].searchName');   
        if( currentDesc != null && currentDesc.length > 0 ){
            currentDesc[0].value = result["SPONSOR_NAME"];
        }
    }
   //Added for CASE#COEUSDEV-230-Answered questionnaire says it is not Answered in Approval in Progress Proposal.-end
    else if(searchType == "ROLODEXSEARCH"){     
        var currentValue = document.getElementsByName('dynaFormData['+index+'].answer');   
        if( currentValue != null && currentValue.length > 0 ){
        currentValue[0].value = result["ROLODEX_ID"];       
        }
        var currentDesc = document.getElementsByName('dynaFormData['+index+'].searchName'); 
        if(currentDesc != null &&  currentDesc.length > 0){
        currentDesc[0].value = result["LAST_NAME"];
        if(result["LAST_NAME"] == "null" || result["LAST_NAME"] == undefined ){
            currentDesc[0].value = "";  
        }
      }     
    }
 }

function put_Data(listCode,listDesc) {
    dataChanged();
    var currentValue = document.getElementsByName('dynaFormData['+index+'].answer'); 
    if(currentValue != null && currentValue.length > 0 ){    
    currentValue[0].value = listCode;
    }
    var currentDesc = document.getElementsByName('dynaFormData['+index+'].searchName');
    if(currentDesc != null && currentDesc.length > 0){
    currentDesc[0].value = listDesc;
    }

}

function processData(operation){
    if(operation == 'START_OVER' &&
        !confirm("<bean:message bundle="proposal" key="questionnaire.startOverErrorMessage"/>")){
        return;
    }
    document.dynaBeanList.action = "<%=request.getContextPath()%>/saveQuestionnaireData.do?operation="+operation;
    document.dynaBeanList.submit();   
}

function view_data(operation){
    //Modified with case 4287:Questionnaire Templates.
    //The printing would be Print Questions n Answers and PrintAll Questions by default.
    //var winleft = (screen.width - 650);
    //var winUp = (screen.height - 450);  
    //var win = "scrollbars=1,resizable=0,width=400,height=100,left="+winleft+",top="+winUp
    //var url_value="<%=request.getContextPath()%>/coeuslite/utk/propdev/clPrintOption.jsp";
    //window.open(url_value,'',win);
    var printQuestion = 'N';
    var printAllQuestion = 'Y';
    window.open("<%=request.getContextPath()%>/saveQuestionnaireData.do?operation="+operation+"&printQuestions="+printQuestion+"&printAllQuestions="+printAllQuestion);
    //4287 End
}
<!--Added for Case#3524 - Add Explanation field to Questions - Start -->
function showQuestion(link) {
    var winleft = (screen.width - 650) / 2;
    var winUp = (screen.height - 450) / 2;  
    var win = "scrollbars=1,resizable=1,width=790,height=450,left="+winleft+",top="+winUp
    sList = window.open('<bean:write name='ctxtPath'/>'+"/"+link, "list", win);
    if (parseInt(navigator.appVersion) >= 4) {
    window.sList.focus(); 
    }
}
<!--Added for Case#3524 - Add Explanation field to Questions - End -->
</script>

<body>
	<html:form action="/saveQuestionnaireData" method="POST">
		<!-- 4272: Maintain History of Questionnaires - Start -->
		<script>
<%if(CoeusLiteConstants.YES.equalsIgnoreCase(newQuestionnaireVersion)){%>   
    alert('New version of this form is available. Answer the latest version of this form. ');
    // COEUSDEV-230: Answered questionnaire says it is not Answered in Approval in Progress Proposal - Start
    document.dynaBeanList.action = "<%=request.getContextPath()%>/getSubmissionQuestionnaire.do?questionnaireId=<%=questionnaireId%>&questionaireLabel=<%=questionnaireLabel%>&menuId=<%=menuId%>&actionFrom=<%=actionFrom%>&newVersionMsgDisplayed=true";
    document.dynaBeanList.submit();  
    // COEUSDEV-230: Answered questionnaire says it is not Answered in Approval in Progress Proposal - End
<%}%>
</script>
		<!-- 4272: Maintain History of Questionnaires - End-->
		<%if(mess != null && mess.equals("COMPLETED")
        && request.getAttribute("COMPLETED") == null){%>
		<script>
    <%if(questionnaireModuleObject.getModuleItemCode() == 7){%>
        alert('Questionnaire Completed for protocol <%=questionnaireModuleObject.getModuleItemKey().replace("T","")%>');
    <%}%>
</script>
		<%}%>
		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			class="tabtable">
			<tr>
				<td align="left" valign="top">
					<table width="100%" border="0" align="center" cellpadding="0"
						cellspacing="0" class="tabtable">
						<tr>
							<td colspan="4" align="left" valign="top">
								<table width="100%" height="20" border="0" cellpadding="0"
									cellspacing="0" class="tableheader">
									<tr class='tableheader'>
										<td><%=session.getAttribute("questionaireLabel")%></td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td>
								<div id="helpText" class='helptext'>
									<bean:message bundle="proposal"
										key="helpTextProposal.Questionnaire" />
								</div>
							</td>
						</tr>
						<tr>
							<td class="copy"><logic:messagesPresent message="true">
									<script>errValue = true;</script>
									<font color='red'> <html:messages id="message"
											message="true" property="answerMandatory" bundle="proposal">
											<li><bean:write name="message" /></li>
										</html:messages> <html:messages id="message" message="true"
											property="numberFormatException" bundle="proposal">
											<li><bean:write name="message" /></li>
										</html:messages> <html:messages id="message" message="true"
											property="notValidDate" bundle="proposal">
											<li><bean:write name="message" /></li>
										</html:messages> <html:messages id="message" message="true"
											property="customElements.notValidLength" bundle="proposal">
											<li><bean:write name="message" /></li>
										</html:messages> <html:messages id="message" message="true" property="errMsg"
											bundle="proposal">
											<script>errLock = true;</script>
											<li><bean:write name="message" /></li>
										</html:messages>
									</font>
								</logic:messagesPresent></td>
						</tr>
						<logic:notEmpty name="questionsList" property="list"
							scope="session">
							<tr class="copybold">
								<td height="20" valign="center" align="left" colspan="2">
									<%if(!modeValue){%> &nbsp;&nbsp; <%if(pageNo != null && pageNo.equals("1")){%>
									<u><bean:message bundle="proposal"
											key="questionnaire.previous" /></u> <%} else {%> <html:link
										href="javascript:processData('PREVIOUS')">
										<u><bean:message bundle="proposal"
												key="questionnaire.previous" /></u>
									</html:link> <%}%> &nbsp;&nbsp;&nbsp;&nbsp; <u><bean:message
											bundle="proposal" key="questionnaire.modify" /></u>
									&nbsp;&nbsp;&nbsp;&nbsp; <html:link
										href="javascript:processData('START_OVER')">
										<u><bean:message bundle="proposal"
												key="questionnaire.startOver" /></u>
									</html:link> <%} else {%> &nbsp;&nbsp; <u><bean:message bundle="proposal"
											key="questionnaire.previous" /></u> &nbsp;&nbsp;&nbsp;&nbsp; <%if(mess != null && mess.equals("COMPLETED") && !originalMode){%>
									<html:link href="javascript:processData('MODIFY')">
										<u><bean:message bundle="proposal"
												key="questionnaire.modify" /></u>
									</html:link> <%} else {%> <u><bean:message bundle="proposal"
											key="questionnaire.modify" /></u> <%}%> &nbsp;&nbsp;&nbsp;&nbsp; <u><bean:message
											bundle="proposal" key="questionnaire.startOver" /></u> <%}%>
								</td>
							</tr>
							<tr align="center">
								<td colspan="2">
									<table width="100%" height="100%" border="0" cellpadding="2"
										cellspacing="0">
										<%  
                    String strBgColor = "#DCE5F1";
                    String calImage = request.getContextPath()+"/coeusliteimages/cal.gif";
                    HashMap hmQuestionNumber = new HashMap();
                    int questionNum = 1;                    
                %>
										<logic:iterate id="dynaFormData" name="questionsList"
											property="list"
											type="org.apache.struts.action.DynaActionForm"
											indexId="index" scope="session">
											<%
                        if (count%2 == 0)
                            strBgColor = "#D6DCE5";
                        else 
                            strBgColor="#DCE5F1";
                        String questionId 		= ((Integer)dynaFormData.get("questionId")).toString();
                        String questionNumber 		= ((Integer)dynaFormData.get("questionNumber")).toString();
                        String question 		= (String)dynaFormData.get("description");
                        String maxAnswers   		= ((Integer)dynaFormData.get("maxAnswers")).toString();
                        String answer   		= (String)dynaFormData.get("answer");
                        String validAnswers   		= (String)dynaFormData.get("validAnswer");
                        String lookUpName   		= (String)dynaFormData.get("lookUpName");
                        String lookupGUI                = (String)dynaFormData.get("lookUpGui");  
                        String answerDataType           = (String)dynaFormData.get("answerDataType");
                        String answerMaxLength          = ((Integer)dynaFormData.get("answerMaxLength")).toString();
                        String answerNumber             = ((Integer)dynaFormData.get("answerNumber")).toString();
                        boolean isLookupPresent 	= false;
                        boolean isLookupDisabled	= false;
                        int maxLength = 0;
                        if(answerMaxLength != null && !answerMaxLength.equals("")){
                            maxLength = Integer.parseInt(answerMaxLength);
                        }
                        if(lookupGUI!=null && !lookupGUI.equals("")){
                                isLookupPresent 	= true;
                        }
                        answer = (answer == null)? "": answer;
                 %>
											<%if(Integer.parseInt(answerNumber) == 0){%>
											<tr>
												<td>&nbsp;</td>
												<td>&nbsp;</td>
											</tr>
											<tr class="copybold">
												<td height="20" width="3%">
													<%String key = ""+questionId+questionNumber;
                        if(hmQuestionNumber.get(key) == null){
                            hmQuestionNumber.put(key, ""+questionNum);
                            questionNum++;
                        }%> <%=hmQuestionNumber.get(key)%>)

												</td>

												<td width="82%"><bean:write name="dynaFormData"
														property="description" /></td>
												<!--Added for Case#3524 - Add Explanation field to Questions - Start -->
												<td class='copybold' align=left width="15%">
													<%String queLink = "javascript:showQuestion('/showQuestionExplanation.do?questionNo="+dynaFormData.get("questionId")+"&questionDesc="+dynaFormData.get("description")+"')";%>
													<html:link href="<%=queLink%>">
														<bean:message bundle="proposal" key="ynq.more" />
													</html:link>
												</td>
												<!--Added for Case#3524 - Add Explanation field to Questions - End -->
											</tr>
											<%} else {%>
											<tr>
												<td width="3%">&nbsp;</td>

												<td width="82%" class="copy" nowrap>
													<% if(validAnswers!=null && !validAnswers.equals("")){                                 
                                   if(validAnswers.equalsIgnoreCase("Text")){%>

													<%--if(Integer.parseInt(answerMaxLength) <= 20){ %>
                                            <html:text property="answer" name="dynaFormData" maxlength="<%=answerMaxLength%>" indexed="true" disabled="<%=modeValue%>" onchange="dataChanged()"/>
                                        <%}else if(Integer.parseInt(answerMaxLength) > 20 && Integer.parseInt(answerMaxLength) <= 80){%>
                                            <html:text property="answer" name="dynaFormData" styleClass="textbox-long" maxlength="<%=answerMaxLength%>" indexed="true" disabled="<%=modeValue%>" onchange="dataChanged()"/>
                                        <%}else{%>
                                            <html:textarea property="answer" name="dynaFormData" styleClass="textbox-longer" cols="100" rows="5" indexed="true" disabled="<%=modeValue%>" onchange="dataChanged()"/>
                                        <%}--%> <%if(maxLength <= 2000){%>
													<%if(maxLength <= 60){ 
                                                        //Addded for COEUSDEV-136: Questionnaire question presentation text blocks vary in Lite and limited to 1 line in Premium - Start
                                                        //Sets the textfield size to 20, if the maxanswerlength is less than 21, otherwise sets the amswermaxlength
                                                        String textFieldSize = "";    
                                                        if(maxLength <= 20){
                                                            textFieldSize = "20";    
                                                        }else{
                                                            textFieldSize = answerMaxLength;    
                                                        }
                                                        //COEUSDEV - 
                                                    //COEUSDEV-136 - End
                                                    //Sets text field maxlength to 10, if the datatype is date
                                                    if(answerDataType!= null && answerDataType.equals("Date")){ 
                                                    %> <html:text
														property="answer" name="dynaFormData" indexed="true"
														size="20" maxlength="10" disabled="<%=modeValue%>"
														onchange="dataChanged()" /> <%}else{%> <html:text
														property="answer" name="dynaFormData" indexed="true"
														size="<%=textFieldSize%>" maxlength="<%=answerMaxLength%>"
														disabled="<%=modeValue%>" onchange="dataChanged()" /> <%}%>
													<!--COEUSDEV-137 - End  --> <%}else{%> <html:textarea
														property="answer" name="dynaFormData"
														styleClass="textbox-longer" cols="150" rows="5"
														indexed="true" disabled="<%=modeValue%>"
														onchange="dataChanged()" /> <%}%> <%}%> <% if(!modeValue){
                                        if(answerDataType.equals("Date")){ 
                                            String calender ="javascript:displayCalendarWithTopLeft('dynaFormData["+count+"].answer',8,25)";%>

													<html:link href="<%=calender%>" onclick="dataChanged()">
														<html:img src="<%=calImage%>" border="0" height="16"
															width="16" />
													</html:link> <%}}%> <%}else if(validAnswers.equalsIgnoreCase("YN")){
                                    %> <html:radio property="answer"
														name="dynaFormData" value="Y" indexed="true"
														disabled="<%=modeValue%>" onchange="dataChanged()" />Yes
													&nbsp; <html:radio property="answer" name="dynaFormData"
														value="N" indexed="true" disabled="<%=modeValue%>"
														onchange="dataChanged()" />No <%}else if(validAnswers.equalsIgnoreCase("YNX")){%>
													<html:radio property="answer" name="dynaFormData" value="Y"
														indexed="true" disabled="<%=modeValue%>"
														onchange="dataChanged()" />Yes &nbsp; <html:radio
														property="answer" name="dynaFormData" value="N"
														indexed="true" disabled="<%=modeValue%>"
														onchange="dataChanged()" />No <html:radio
														property="answer" name="dynaFormData" value="X"
														indexed="true" disabled="<%=modeValue%>"
														onchange="dataChanged()" />N/A <%}else if(isLookupPresent){%>
													<html:text property="answer" name="dynaFormData"
														maxlength="2000" indexed="true" readonly="true"
														disabled="<%=modeValue%>" onchange="dataChanged()" /> <%  String pageUrl="javaScript:openLookupWindow('"+lookupGUI+"','"+lookUpName+"','','"+count+"')";
                                        if(!modeValue){
                                        String image= request.getContextPath()+"/coeusliteimages/search.gif";%>
													<html:link href="<%=pageUrl%>">
														<u><bean:message bundle="proposal"
																key="proposalOrganization.Search" /></u>
													</html:link> <%--Added for the case# COEUSDEV-194 Questionnaire in Lite - value list box doesn't close start--%>
													<html:text property="searchName" name="dynaFormData"
														maxlength="2000" styleClass="cltextbox-color"
														indexed="true" readonly="true" /> <%--Added for the case# COEUSDEV-194 Questionnaire in Lite - value list box doesn't close end--%>
													<%}}%> <%--<html:text property="answer" name="dynaFormData" maxlength="10" indexed="true" readonly ="true" disabled="<%=modeValue%>" />
                                        <a id="hlIRBDate" onclick="displayCalendarWithTopLeft('dynaFormData[<%=count%>].answer',8,25)" tabindex="32" href="javascript:void(0);"
                                         runat="server"><img id="imgIRBDate" title="" height="16" alt="" src="<bean:write name='ctxtPath'/>/coeusliteimages/cal.gif" width="16"
                                         border="0" runat="server">
                                        </a>--%> <%}
                                  if(!modeValue){
                                  if(isLookupPresent){
                                   String image= request.getContextPath()+"/coeusliteimages/search.gif";
                                   //String pageUrl="javaScript:openLookupWindow('"+lookupGUI+"','"+lookUpName+"','"+validAnswers+"','"+count+"')";
                                   %> <%-- <html:link  href=""><html:img src="<%=image%>"/> </html:link>--%>
													<%}}%>
												</td>
												<%-- //Commented for COEUSDEV-136 : Questionnaire question presentation text blocks vary in Lite and limited to 1 line in Premium - Start
                     <td width="75%" align="left" valign="top">
                                <html:text property="searchName"  name="dynaFormData" maxlength="2000" styleClass="cltextbox-color" indexed="true" readonly="true" onchange="dataChanged()"/>
                      </td> --%>
											</tr>
											<%}%>
											<% count++ ; %>
										</logic:iterate>
									</table>
								</td>
							</tr>
						</logic:notEmpty>
						<tr>
							<td><font color='red'> <logic:empty
										name="questionsList" property="list" scope="session">
										<%String errorMessage = "";%>
										<%if(questionnaireModuleObject.getModuleItemCode() == 7){%>
										<%errorMessage = "This "+session.getAttribute("questionaireLabel")+" is not answered for this protocol";%>
										<%} else if(questionnaireModuleObject.getModuleItemCode() == 3){%>
										<%errorMessage = "This "+session.getAttribute("questionaireLabel")+" is not answered for this proposal";%>
										<%}%>
										<li><%=errorMessage%></li>
									</logic:empty>
							</font></td>
						</tr>
						<% if(!modeValue){%>
						<tr align="left" class='table'>
							<td class='savebutton' colspan="2" nowrap>&nbsp;<html:button
									property="saveAndProceed" styleClass="clsavebutton"
									value="<%=buttonName%>"
									onclick="javascript:processData('SAVE')" /> <%if(modifyCompletedQuestionnaire != null && "YES".equals(modifyCompletedQuestionnaire) && !originalMode){%>
								&nbsp;<html:button property="saveAndComplete"
									styleClass="clsavebutton" value="Save & Complete"
									onclick="javascript:processData('SAVE_COMPLETE')" /> <%}%> &nbsp;<html:button
									property="print" styleClass="clsavebutton" value="Print"
									onclick="javascript:view_data('PRINT')" />
							</td>
						</tr>
						<%} else {%>
						<tr align="left" class='table'>
							<td class='savebutton' colspan="2" nowrap>&nbsp;<html:button
									property="print" styleClass="clsavebutton" value="Print"
									onclick="javascript:view_data('PRINT')" />
							</td>
						</tr>
						<%}%>
					</table>
				</td>
			</tr>
		</table>
	</html:form>
	<script>
      DATA_CHANGED = 'false';
      if(errValue && !errLock){
            DATA_CHANGED = 'true';
      }
      LINK = "<%=request.getContextPath()%>//saveQuestionnaireData.do?operation=SAVE";
      FORM_LINK = document.dynaBeanList;
      PAGE_NAME = "<%=session.getAttribute("questionaireLabel")%>";
      function dataChanged(){
            DATA_CHANGED = 'true';
      }
      linkForward(errValue);
</script>
	<script>
      var help = '<bean:message bundle="proposal" key="helpTextProposal.Questionnaire"/>';
      help = trim(help);
      if(help.length == 0){
            document.getElementById('helpText').style.display = 'none';
      }
      function trim(s){
            return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
      }  
</script>
</body>
</html:html>
