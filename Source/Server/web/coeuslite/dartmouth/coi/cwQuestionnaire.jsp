<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page
	import="org.apache.struts.action.DynaActionForm,
org.apache.commons.beanutils.DynaBean,
edu.dartmouth.coeuslite.coi.beans.QABean,
java.util.List,edu.mit.coeuslite.utils.CoeusDynaBeansList"%>

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
     String actFrom="";
    boolean modeValue=false;
    boolean originalMode = false;
    if(request.getAttribute("actFrom")!=null){
        actFrom=(String)request.getAttribute("actFrom"); 
           }
    if(mode!=null && !mode.equals("")){   
         if(mode.equalsIgnoreCase("display") || mode.equalsIgnoreCase("D")){
          
                modeValue=true;
                originalMode = true;
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
     String pageNo = (String) hmQuestionnaireData.get("page");
    Vector vecQuestionnaireData =(Vector) hmQuestionnaireData.get("dataObject");
    String buttonName = "Save & Proceed";
    if(vecQuestionnaireData != null && vecQuestionnaireData.size() > 2){
        String totalPages = (String) vecQuestionnaireData.get(2);
        if(totalPages != null && pageNo.equals(totalPages)){
            buttonName = "Save & Continue";
        }
    }
   
    //String mess = "";
    if(session.getAttribute("questionsList")!=null){
    CoeusDynaBeansList qaList=(CoeusDynaBeansList)session.getAttribute("questionsList");
    Vector vecData=(Vector)qaList.getList();
    Vector prevData=new Vector();
    for(int index=0;index<vecData.size();index++){      
        DynaBean dynaFormData=(DynaBean)vecData.get(index);
        QABean qaBean=new QABean();
        qaBean.setQuestionId((Integer)dynaFormData.get("questionId"));
        qaBean.setAnswer((String)dynaFormData.get("answer"));
        prevData.add(qaBean);
        
    }
    session.setAttribute("prevQAList",prevData);
    }
                    
                    
                    %>

<html:html>
<head>
<title>Coeus Web</title>
<%String path=request.getContextPath();%>
<link href="<%=path%>/coeuslite/dartmouth/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
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
var index = "" ;
var errValue = false;
var errLock = false;
function openLookupWindow(lookupWin,lookupVal, lookupArgument, count) {
    index = count;
    var linkValue = 'generalProposalSearch.do';
    var winleft = (screen.width - 650) / 2;
    var winUp = (screen.height - 450) / 2;  
    var win = "scrollbars=1,resizable=0,width=810,height=450,left="+winleft+",top="+winUp
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
        if( currentDesc != null && currentDesc.length > 0 ){
            document.getElementById("searchName"+index).innerHTML = result["UNIT_NAME"];
            currentDesc[0].value = result["UNIT_NAME"];
        }
    }else if(searchType == "PERSONSEARCH"){
        var currentValue = document.getElementsByName('dynaFormData['+index+'].answer'); 
        if( currentValue != null && currentValue.length > 0 ){
            currentValue[0].value = result["PERSON_ID"];
        }
        var currentDesc = document.getElementsByName('dynaFormData['+index+'].searchName');   
        if( currentDesc != null && currentDesc.length > 0 ){
            document.getElementById("searchName"+index).innerHTML = result["FULL_NAME"];
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
            document.getElementById("searchName"+index).innerHTML = result["ORGANIZATION_NAME"];
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
            document.getElementById("searchName"+index).innerHTML = result["SPONSOR_NAME"];
            currentDesc[0].value = result["SPONSOR_NAME"];
        }
    }
   //Added for CASE#COEUSDEV-230-Answered questionnaire says it is not Answered in Approval in Progress Proposal.-end
    else if(searchType == "ROLODEXSEARCH"){          
        var currentValue = document.getElementsByName('dynaFormData['+index+'].answer');  
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
        var currentDesc = document.getElementsByName('dynaFormData['+index+'].searchName'); 
        if( currentDesc != null && currentDesc.length > 0 ){
            document.getElementById("searchName"+index).innerHTML = name;
            currentDesc[0].value = name;
        }
        
    }

}

function put_Data(listCode,listDesc) {
    dataChanged();
    var currentValue = document.getElementsByName('dynaFormData['+index+'].answer');   
    if( currentValue != null && currentValue.length > 0 ){
        currentValue[0].value = listCode;
    }
    var currentDesc = document.getElementsByName('dynaFormData['+index+'].searchName');  
    if( currentDesc != null && currentDesc.length > 0 ){
    document.getElementById("searchName"+index).innerHTML = listDesc;
    currentDesc[0].value = listDesc;
    }

}

function processData(operation){
    if(operation == 'START_OVER' &&
        !confirm("<bean:message bundle="proposal" key="questionnaire.startOverErrorMessage"/>")){
        return;
    }   
    if(errValue){    
    document.dynaBeanList.action = "<%=request.getContextPath()%>/saveCoiQuestionnaire.do?operation="+operation;   
    }else{
    document.dynaBeanList.action = "<%=request.getContextPath()%>/createDisclosure.do?operation="+operation;   
    }
    document.dynaBeanList.submit(); 
    }
function showMain(){
document.dynaBeanList.action="<%=path%>/addCertQuestions.do?mode=exit";
document.dynaBeanList.submit();
}
function showFin(){
document.dynaBeanList.action="<%=path%>/addCertQuestions.do?mode=continue";
document.dynaBeanList.submit();
}
</script>
<body>
	<form action="/createDisclosure.do" name="dynaBeanList" method="POST">
		<%if(mess != null && mess.equals("COMPLETED")
        && request.getAttribute("COMPLETED") == null){%>
		<script>
    <%if(questionnaireModuleObject.getModuleItemCode() == 10){%>
        alert('Questionnaire Completed for Disclosure <%=questionnaireModuleObject.getModuleItemKey()%>');
    <%}%>
  
</script>
		<%}%>

		<table width="986px" border="0" cellpadding="5" cellspacing="0"
			class="table">
			<tr>
				<td align="left" valign="top">
					<table width="100%" border="0" align="center" cellpadding="0"
						cellspacing="0" class="table">
						<tr>
							<td colspan="4" align="left" valign="top">
								<table width="100%" height="20" border="0" cellpadding="0"
									cellspacing="0" class="table">
									<tr height="20px">
										<%--         <td>  <%=session.getAttribute("questionaireLabel")%>                            
                            </td>--%>
										<td height="23" class="theader" style="font-size: 14px"><bean:message
												bundle="coi" key="AnnualDisclosure.Certification.Header" /></font>
										</td>
									</tr>
								</table>
							</td>
						</tr>

						<table width="100%" border="0" cellpadding="0" cellspacing="0"
							class="table">
							<tr>
								</td>
							<tr>
								<td class="copybold" height="30px"><bean:message
										bundle="coi" key="AnnualDisclosure.Certification.request" /></td>
							</tr>
							<%--<tr>
                    <td>
                        <div id="helpText" class='helptext'>            
                            <bean:message bundle="proposal" key="helpTextProposal.Questionnaire"/>  
                        </div> 
                    </td>
     		</tr>--%>


							<logic:messagesPresent message="true">
								<tr>
									<td class="copybold"><script>errValue = true;</script> <font
										color='red' style=""> <html:messages id="message"
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
									</font></td>
								</tr>
							</logic:messagesPresent>

							<logic:notEmpty name="questionsList" property="list"
								scope="session">

								<tr class="copybold" bgcolor="#D6DCE5">
									<td height="20" valign="center" align="left" colspan="2">
										<%if(!modeValue){%> &nbsp;&nbsp; <%if(pageNo != null && pageNo.equals("1")){%>
										<%--         <u><bean:message bundle="proposal" key="questionnaire.previous"/></u>--%>
										<%} else {%> <html:link
											href="javascript:processData('PREVIOUS')">
											<u><bean:message bundle="proposal"
													key="questionnaire.previous" /></u>
										</html:link> <%--      <%}%>--%> &nbsp;&nbsp;&nbsp;&nbsp; <html:link
											href="javascript:processData('MODIFY')">
											<u><bean:message bundle="proposal"
													key="questionnaire.modify" /></u>
										</html:link> &nbsp;&nbsp;&nbsp;&nbsp; <html:link
											href="javascript:processData('START_OVER')">
											<u><bean:message bundle="proposal"
													key="questionnaire.startOver" /></u>
										</html:link> <%}} else {%> &nbsp;&nbsp; <u><bean:message
												bundle="proposal" key="questionnaire.previous" /></u>
										&nbsp;&nbsp;&nbsp;&nbsp; <%if(mess != null && mess.equals("COMPLETED") && !originalMode){
                                %> <html:link
											href="javascript:processData('MODIFY')">
											<u><bean:message bundle="proposal"
													key="questionnaire.modify" /></u>
										</html:link> <%} else {%> <html:link href="javascript:processData('MODIFY')">
											<u><bean:message bundle="proposal"
													key="questionnaire.modify" /></u>
										</html:link> <%}%> &nbsp;&nbsp;&nbsp;&nbsp; <u><bean:message
												bundle="proposal" key="questionnaire.startOver" /></u> <%}%>
									</td>
								</tr>
								<tr align="left">
									<td colspan="2" valign="top">
										<table width="100%" height="100%" border="0" cellpadding="5"
											cellspacing="0">
											<%  
                    String strBgColor = "#DCE5F3";
                    String calImage = request.getContextPath()+"/coeusliteimages/cal.gif";
                    HashMap hmQuestionNumber = new HashMap();
                    int questionNum = 1;                    
                %>

											<logic:iterate id="dynaFormData" name="questionsList"
												property="list"
												type="org.apache.struts.action.DynaActionForm"
												indexId="index" scope="session">
												<%
                        if (count%4 == 0)
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
                        String searchName               = (String)dynaFormData.get("searchName");
                        boolean isLookupPresent 	= false;
                        boolean isLookupDisabled	= false;
                        if(lookupGUI!=null && !lookupGUI.equals("")){
                                isLookupPresent 	= true;
                        }
                        answer = (answer == null)? "": answer;
                 %>
												<%if(Integer.parseInt(answerNumber) == 0){%>
												<%--<tr height="5px"><td >&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>--%>
												<tr bgcolor="<%=strBgColor%>" class="rowLine"
													onmouseover="className='rowHover rowLine'"
													onmouseout="className='rowLine'">
													<td height="20" valign="top" class="copy">
														<%String key = ""+questionId+questionNumber;
                        if(hmQuestionNumber.get(key) == null){
                            hmQuestionNumber.put(key, ""+questionNum);
                            questionNum++;
                        }%> <%=hmQuestionNumber.get(key)%>.&nbsp;
													</td>
													<td width="50%" class="copy"><bean:write
															name="dynaFormData" property="description" /></td>
													<%} else {%>
													<td width="20%" class="copy" nowrap valign="top">
														<% if(validAnswers!=null && !validAnswers.equals("")){                                 
                                   if(validAnswers.equalsIgnoreCase("Text")){%>

														<%--if(Integer.parseInt(answerMaxLength) <= 20){ %>
                                            <html:text property="answer" name="dynaFormData" maxlength="<%=answerMaxLength%>" indexed="true" disabled="<%=modeValue%>" onchange="dataChanged()"/>
                                        <%}else if(Integer.parseInt(answerMaxLength) > 20 && Integer.parseInt(answerMaxLength) <= 80){%>
                                            <html:text property="answer" name="dynaFormData" styleClass="textbox-long" maxlength="<%=answerMaxLength%>" indexed="true" disabled="<%=modeValue%>" onchange="dataChanged()"/>
                                        <%}else{%>
                                            <html:textarea property="answer" name="dynaFormData" styleClass="textbox-longer" cols="100" rows="5" indexed="true" disabled="<%=modeValue%>" onchange="dataChanged()"/>
                                        <%}--%> <%if(answer.length() <= 2000){%>
														<%if(answer.length() <= 60){%> <html:text property="answer"
															name="dynaFormData" indexed="true"
															disabled="<%=modeValue%>" onchange="dataChanged()" /> <%}else if(answer.length() >60 && answer.length() <= 120){%>
														<html:text property="answer" name="dynaFormData"
															styleClass="textbox-long" indexed="true"
															disabled="<%=modeValue%>" onchange="dataChanged()" /> <%}else{%>
														<html:textarea property="answer" name="dynaFormData"
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
														<html:radio property="answer" name="dynaFormData"
															value="Y" indexed="true" disabled="<%=modeValue%>"
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
														</html:link> <%}%> <%}%> <%--<html:text property="answer" name="dynaFormData" maxlength="10" indexed="true" readonly ="true" disabled="<%=modeValue%>" />
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
													<td width="25%" class="copybold" nowrap valign="center">&nbsp;
														<html:hidden property="searchName" name="dynaFormData"
															indexed="true" /> <span style="font-weight: bold;"
														id="searchName<%=count%>"> <%if(searchName != null && searchName.length()>0){%>
															<%=searchName%> <%}%>
													</span>
													</td>
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
								<td class='savebutton' colspan="5" nowrap>&nbsp;<html:button
										property="saveAndProceed" styleClass="clsavebutton"
										value="<%=buttonName%>"
										onclick="javascript:processData('SAVE')" /> <!-- &nbsp;&nbsp;&nbsp;&nbsp;
            <html:button property="print" styleClass="clsavebutton" value="Print" onclick="javascript:processData('PRINT')"/>-->
								</td>
							</tr>
							<%}else if(modeValue){
                      /*CoeusDynaBeansList questList=(CoeusDynaBeansList)session.getAttribute("questionsList");
                      request.setAttribute("questionsList",questList);*/%>

							<!-- Modified for Case#4447 : Next phase of COI enhancements - Start
                            <tr><td><table width="100%" height="20"  colspan='2' border="0" cellpadding="0" cellspacing="0" class="table">-->
							<tr>
								<td colspan='4' class='savebutton' align='left'><html:submit
										value="Continue Annual Disclosure" style="width:200px"
										styleClass="clsavebutton" onclick="javascript:showFin();" /></td>
								</td>
							</tr>
							<!--Case#4447 - End-->
							</td>
							</tr>
						</table>
						</td>
						</tr>
						<%}%>
					</table>
		</table>
		</td>
		</tr>
		</table>
	</form>
	<script>
      DATA_CHANGED = 'false';
      if(errValue && !errLock){
            DATA_CHANGED = 'true';
      }
      LINK = "<%=request.getContextPath()%>/saveCoiQuestionnaire.do?operation=SAVE";
      FORM_LINK = document.dynaBeanList;
      PAGE_NAME = "<%=session.getAttribute("questionaireLabel")%>";
      function dataChanged(){
            DATA_CHANGED = 'true';
      }
    <!--  linkForward(errValue);-->
</script>
	<script>
<%--      var help = '<bean:message bundle="proposal" key="helpTextProposal.Questionnaire"/>';
      help = trim(help);
      if(help.length == 0){
            document.getElementById('helpText').style.display = 'none';
      }
      function trim(s){
            return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
      }  --%>
</script>
</body>
</html:html>
