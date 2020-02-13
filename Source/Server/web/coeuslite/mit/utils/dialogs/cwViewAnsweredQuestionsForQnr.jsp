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
    boolean modeValue=true;
    
    QuestionnaireAnswerHeaderBean questionnaireModuleObject = 
            (QuestionnaireAnswerHeaderBean)session.getAttribute("questionnaireModuleObject");
    if(questionnaireModuleObject == null){
        questionnaireModuleObject = new QuestionnaireAnswerHeaderBean();
    }
    int count = 0;
    HashMap hmQuestionnaireData = (HashMap)session.getAttribute("questionnaireInfo");
    hmQuestionnaireData = (hmQuestionnaireData == null)? new HashMap() : hmQuestionnaireData;
    
    
    Vector vecQuestionnaireData =(Vector) hmQuestionnaireData.get("dataObject");
    String buttonName = "Save & Proceed";
    if(vecQuestionnaireData != null && vecQuestionnaireData.size() > 2){
        String totalPages = (String) vecQuestionnaireData.get(2);
        if(totalPages != null && totalPages.equals("1")){
            buttonName = "Save";
        }
    }
    
    String actionFrom = (String)session.getAttribute("actionFrom");
    Integer questionnaireId = (Integer)session.getAttribute("questionnaireId"+session.getId());
    String questionnaireLabel = (String)session.getAttribute("questionaireLabel");
    String menuId = (String)session.getAttribute("menuCode");
    boolean questionnaireInfoReqd = false;
    String questionnaireInfo = null;
    %>

<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/ipf.css"
	rel="stylesheet" type="text/css">
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
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
var index = "" ;
var errValue = false;
var errLock = false;
function openLookupWindow(lookupWin,lookupVal, lookupArgument, count) {
    index = count;
    var linkValue = 'generalProposalSearch.do';
    var winleft = (screen.width - 650) / 2;
    var winUp = (screen.height - 450) / 2;  
    var win = "scrollbars=1,resizable=0,width=810,height=450,left="+winleft+",top="+winUp
    
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
        if(currentDesc != null &&  currentDesc.length > 0){
        currentDesc[0].value =name;        
       
       
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
    
    var printQuestion = 'N';
    var printAllQuestion = 'Y';
    window.open("<%=request.getContextPath()%>/saveQuestionnaireData.do?operation="+operation+"&printQuestions="+printQuestion+"&printAllQuestions="+printAllQuestion);
    
}

function showQuestion(link) {
    var winleft = (screen.width - 650) / 2;
    var winUp = (screen.height - 450) / 2;  
    var win = "scrollbars=1,resizable=1,width=790,height=450,left="+winleft+",top="+winUp
    sList = window.open('<bean:write name='ctxtPath'/>'+"/"+link, "list", win);
    if (parseInt(navigator.appVersion) >= 4) {
    window.sList.focus(); 
    }
}
</script>



<body>
	<html:form action="/saveQuestionnaireData" method="POST">

		<script>
 <%
      request.getAttribute("qnrLabel");
      String linkFromHistoryPage = request.getParameter("linkFromHistoryPage");
      //Changes to show Questionnair for Amendment/Renewal from amendment/renewal summary
      String qactionFrom = request.getParameter("actionFrom");
      String protocolAmendmentNumber = request.getParameter("protocolAmendmentNumber");
      if("AMENDMENT_REWNEWAL_HISTORY".equals(qactionFrom)){
        session.setAttribute("actionFrom","AMENDMENT_REWNEWAL_HISTORY");
      }
      String link = request.getParameter("link");
      String completed = request.getParameter("completed");
      String apSubModuleCode = request.getParameter("apSubModuleCode");
      String apModuleItemKey = request.getParameter("apModuleItemKey");
      String apModuleSubItemKey = request.getParameter("apModuleSubItemKey");
      if("AMENDMENT_REWNEWAL_HISTORY".equals(qactionFrom) || "IACUC_AMENDMENT_REWNEWAL_HISTORY".equals(qactionFrom)){
        questionnaireInfoReqd = true;
        if("0".equals(apSubModuleCode)  && "Y".equals(completed)){
            questionnaireInfo  = "Original Protocol Questionnaire"; //Original protocol Qnr
        }else{
            questionnaireInfo = "Amendment/Renewal Questionnaire" ; // Amendment Qnr
        }
      }
      
      String questionaireLabel = request.getParameter("questionaireLabel");
      String submissionNumber = request.getParameter("submissionNumber");
      if("true".equalsIgnoreCase(linkFromHistoryPage)){
%>

    document.dynaBeanList.action = "<%=request.getContextPath()%><%=link%>&questionaireLabel=<%=questionaireLabel%>&fromViewHistoryPage=true&submissionNumber=<%=submissionNumber%>&actionFrom=PROTOCOL_SUBMISSION";
    document.dynaBeanList.submit();  

<%}else if(link != null){%>
    document.dynaBeanList.action = "<%=request.getContextPath()%><%=link%>&questionaireLabel=<%=questionaireLabel%>&protocolAmendmentNumber=<%= protocolAmendmentNumber%>&actionFrom=AMENDMENT_REWNEWAL_HISTORY&apSubModuleCode=<%=apSubModuleCode%>&apModuleItemKey=<%=apModuleItemKey%>&apModuleSubItemKey=<%=apModuleSubItemKey%>&completed=<%=completed%>";
    document.dynaBeanList.submit();  
<%}%>
</script>
		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			class="tabtable">

		</table>
	</html:form>


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
					<%if(questionnaireInfoReqd){%>
					<tr>
						<td colspan="4" align="left" valign="top">
							<table width="100%" height="20" border="0" cellpadding="0"
								cellspacing="0" class="tableheader">
								<tr class='tableheader'>
									<td><%=questionnaireInfo%></td>
								</tr>
							</table>
						</td>
					</tr>
					<%}%>
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
					<logic:notEmpty name="questionsList" scope="session">
						<tr class="copybold">
							<td height="20" valign="center" align="left" colspan="2"></td>
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
										property="list" type="org.apache.struts.action.DynaActionForm"
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

											<td height="20" width="8%" align="center" valign="top">
												<%String key = ""+questionId+questionNumber;
                        if(hmQuestionNumber.get(key) == null){
                            hmQuestionNumber.put(key, ""+questionNum);
                            questionNum++;
                        }%> <%=hmQuestionNumber.get(key)%>)

											</td>

											<td width="82%" align="left"><bean:write
													name="dynaFormData" property="description" /></td>
										</tr>
										<%} else {%>
										<tr>
											<td width="3%">&nbsp;</td>

											<td width="82%" class="copy" nowrap align="left">
												<% if(validAnswers!=null && !validAnswers.equals("")){                                 
                                   if(validAnswers.equalsIgnoreCase("Text")){%>



												<%if(maxLength <= 2000){%> <%if(maxLength <= 60){ 
                                                        //Addded for COEUSDEV-136: Questionnaire question presentation text blocks vary in Lite and limited to 1 line in Premium - Start
                                                        //Sets the textfield size to 20, if the maxanswerlength is less than 21, otherwise sets the amswermaxlength
                                                        String textFieldSize = "";    
                                                        if(maxLength <= 20){
                                                            textFieldSize = "20";    
                                                        }else{
                                                            textFieldSize = answerMaxLength;    
                                                        }
                                                     
                                                    if(answerDataType!= null && answerDataType.equals("Date")){ 
                                                    %> <html:text
													property="answer" name="dynaFormData" indexed="true"
													size="20" maxlength="10" disabled="<%=modeValue%>"
													onchange="dataChanged()" /> <%}else{%> <html:text
													property="answer" name="dynaFormData" indexed="true"
													size="<%=textFieldSize%>" maxlength="<%=answerMaxLength%>"
													disabled="<%=modeValue%>" onchange="dataChanged()" /> <%}%> <%}else{%>
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
												<html:radio property="answer" name="dynaFormData" value="Y"
													indexed="true" disabled="<%=modeValue%>"
													onchange="dataChanged()" />Yes &nbsp; <html:radio
													property="answer" name="dynaFormData" value="N"
													indexed="true" disabled="<%=modeValue%>"
													onchange="dataChanged()" />No <html:radio property="answer"
													name="dynaFormData" value="X" indexed="true"
													disabled="<%=modeValue%>" onchange="dataChanged()" />N/A <%}else if(isLookupPresent){%>
												<html:text property="answer" name="dynaFormData"
													maxlength="2000" indexed="true" readonly="true"
													disabled="<%=modeValue%>" onchange="dataChanged()" /> <%  String pageUrl="javaScript:openLookupWindow('"+lookupGUI+"','"+lookUpName+"','','"+count+"')";
                                        if(!modeValue){
                                        String image= request.getContextPath()+"/coeusliteimages/search.gif";%>
												<html:link href="<%=pageUrl%>">
													<u><bean:message bundle="proposal"
															key="proposalOrganization.Search" /></u>
												</html:link> <%}%> <html:text property="searchName" name="dynaFormData"
													maxlength="2000" styleClass="cltextbox-color"
													indexed="true" readonly="true" /> <%}%> <%}
                                  if(!modeValue){
                                  if(isLookupPresent){
                                   String image= request.getContextPath()+"/coeusliteimages/search.gif";
                                   //String pageUrl="javaScript:openLookupWindow('"+lookupGUI+"','"+lookUpName+"','"+validAnswers+"','"+count+"')";
                                   %> <%}}%>
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
									name="questionsList" scope="session">
									<%String errorMessage = "";%>
									<%if(questionnaireModuleObject.getModuleItemCode() == 7){%>
									<%errorMessage = "This form "+session.getAttribute("questionaireLabel")+" is not answered for this protocol";%>
									<%} else if(questionnaireModuleObject.getModuleItemCode() == 3){%>
									<%errorMessage = "This form "+session.getAttribute("questionaireLabel")+" is not answered for this proposal";%>
									<%}%>
									<li><%=errorMessage%></li>
								</logic:empty>
						</font></td>
					</tr>
					<% if(!modeValue){%>
					<tr align="left" class='table'>
						<td class='savebutton' colspan="2"></td>
					</tr>
					<%} else {%>
					<tr align="left" class='table'>
						<td class='savebutton' colspan="2" nowrap height="1%">&nbsp;

						</td>
					</tr>
					<%}%>
				</table>
			</td>
		</tr>
	</table>



</body>
</html:html>
