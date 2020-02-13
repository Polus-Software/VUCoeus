<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.apache.struts.action.DynaActionForm,
         org.apache.commons.beanutils.DynaBean,
         edu.dartmouth.coeuslite.coi.beans.QABean,
         edu.mit.coeuslite.coiv2.services.CoiCommonService,edu.mit.coeuslite.coiv2.beans.CoiPersonProjectDetails,
         java.util.List,edu.mit.coeuslite.utils.CoeusDynaBeansList" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<%@ page import="edu.mit.coeuslite.utils.CoeusLiteConstants,
         edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean,
         java.util.HashMap, java.util.Vector,java.util.HashMap,edu.mit.coeuslite.coiv2.utilities.CoiConstants"%>
<jsp:useBean  id="proposalList" scope="session" class="java.util.Vector"/>
<jsp:useBean  id="proposalColumnNames" scope="session" class="java.util.Vector"/>

<%
            String proposalNumber = (String) session.getAttribute("proposalNumber" + session.getId());
            String mode = (String) session.getAttribute("mode" + session.getId());          
          
                mode = "";
            //if (val == 2) {
            //    mode = "";
           // }
            String actFrom = "";
            boolean modeValue = false;
            boolean originalMode = false;

            int qnrListSize = 0;

            if(request.getAttribute("qnrListSize") != null) {
                qnrListSize = Integer.parseInt(request.getAttribute("qnrListSize").toString());
            }

            if (request.getAttribute("actFrom") != null) {
                actFrom = (String) request.getAttribute("actFrom");
            }
            if (mode != null && !mode.equals("")) {
                
                if (mode.equalsIgnoreCase("display") || mode.equalsIgnoreCase("D")) {

                    modeValue = true;
                    originalMode = true;
                }
              
            }
            QuestionnaireAnswerHeaderBean questionnaireModuleObject = new QuestionnaireAnswerHeaderBean();
            if(session.getAttribute("questionnaireModuleObject") != null) {
                 questionnaireModuleObject =
                        (QuestionnaireAnswerHeaderBean) session.getAttribute("questionnaireModuleObject");
                if (questionnaireModuleObject == null) {
                    questionnaireModuleObject = new QuestionnaireAnswerHeaderBean();
                }
             }
            int count = 0;
            HashMap hmQuestionnaireData = new HashMap();
            String buttonName ="";
            String mess = "";
            Vector savedQnrList = new Vector();

            if(session.getAttribute("savedQnrList") != null) {
                savedQnrList = (Vector)session.getAttribute("savedQnrList");
             }

            if(session.getAttribute("questionnaireInfo") != null) {
                 hmQuestionnaireData = (HashMap) session.getAttribute("questionnaireInfo");
                hmQuestionnaireData = (hmQuestionnaireData == null) ? new HashMap() : hmQuestionnaireData;
                 mess = (String) hmQuestionnaireData.get("message");
                if (mess != null && mess.equals("COMPLETED") && savedQnrList.size() == 0) {
                    modeValue = true;
                }
                String pageNo = (String) hmQuestionnaireData.get("page");
                Vector vecQuestionnaireData = (Vector) hmQuestionnaireData.get("dataObject");
                 buttonName = "Save & Proceed";
                if (vecQuestionnaireData != null && vecQuestionnaireData.size() > 2) {
                    String totalPages = (String) vecQuestionnaireData.get(2);
                    if (totalPages != null && pageNo.equals(totalPages)) {
                        buttonName = "Save & Continue";
                    }
                }
                }

            //String mess = "";
            CoeusDynaBeansList qaList = null;
            if (session.getAttribute("questionsList") != null) {
                 qaList = (CoeusDynaBeansList) session.getAttribute("questionsList");
                Vector vecData = (Vector) qaList.getList();
                Vector prevData = new Vector();
                for (int index = 0; index < vecData.size(); index++) {
                    DynaBean dynaFormData = (DynaBean) vecData.get(index);
                    QABean qaBean = new QABean();
                    qaBean.setQuestionId((Integer) dynaFormData.get("questionId"));
                    qaBean.setAnswer((String) dynaFormData.get("answer"));
                    prevData.add(qaBean);

                }
                session.setAttribute("prevQAList", prevData);
            }


%>

<html:html>
<head>
    <title>Coeus Web</title>
    <%String path = request.getContextPath();%>
    <link href="<%=path%>/coeuslite/dartmouth/utils/css/coeus_styles.css" rel="stylesheet" type="text/css">
    <html:base/>
    <script language="javascript" type="text/JavaScript" src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/coeusLiteCalendar.js"></script>
</head>

<style>
    .textbox-longer{ width: 400px ;}
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
        var win = "scrollbars=1,resizable=0,width=770,height=450,left="+winleft+",top="+winUp
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
     debugger;
        var opType='<%=request.getParameter("operationType")%>';
        if(opType==null || opType=='null'){
            opType='<%=request.getAttribute("operationType")%>';
        }
        if(operation == 'START_OVER' &&
            !confirm("<bean:message bundle="proposal" key="questionnaire.startOverErrorMessage"/>")){
            return;
        }
       // if(errValue){
      //  document.dynaBeanList.action = "<%=request.getContextPath()%>/saveCoiQuestionnaireCoiv2.do?operation="+operation;
     //   }else{
     
        document.dynaBeanList.action = "<%=request.getContextPath()%>/createDisclosureCoiv2.do?operation="+operation+"&operationType="+opType;
    //    }
        document.dynaBeanList.submit();
    }
     function saveproceed(operation){
         var opType='<%=request.getParameter("operationType")%>';
        if(opType==null || opType=='null'){
            opType='<%=request.getAttribute("operationType")%>';
        }
         document.dynaBeanList.action = "<%=request.getContextPath()%>/createDisclosureCoiv2.do?operation="+operation+"&operationType="+opType;
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

    function continueProcess(){
        var opType='<%=request.getParameter("operationType")%>';
        document.dynaBeanList.action = "<%=request.getContextPath()%>/getProjectDetailsAndFinEntityDetailsCoiv2.do?&operationType="+opType;
        document.dynaBeanList.submit();
    }
    function continueWithoutQnr(){        
        document.dynaBeanList.action = "<%=request.getContextPath()%>/continueWithoutQnr.do?&type=event";
        document.dynaBeanList.submit();
    }
     function exitToCoi(){
        var answer = confirm("This operation will discard your current changes.. Do you want to continue..?");
             if(answer) {
                document.location = '<%=request.getContextPath()%>/exitDisclosure.do';
                window.location;
             }
     }

</script>
<body>
    <form action="/createDisclosureCoiv2.do" name="dynaBeanList"  method="POST">
        <%if (mess != null && mess.equals("COMPLETED")
                            && request.getAttribute("COMPLETED") == null) {%>
        <script>
            <%if (questionnaireModuleObject.getModuleItemCode() == 10) {%>
                alert('Questionnaire Completed for Disclosure <%=questionnaireModuleObject.getModuleItemKey()%>');
            <%}%>

        </script>
        <%}%>
        <%--<table id="bodyTable1" class="table" style="width: 765px;" border="0">
            <tr style="background-color:#6E97CF;" >
                <td colspan="6">
                    <h1 style="background-color:#6E97CF;color:#FFFFFF;float:left;font-size:14px;font-weight:bold;margin: 0;padding: 2px 0 2px 10px;position: relative;text-align: left;">Financial Disclosure by <bean:write name="person" property="fullName"/></h1>
                </td>
            </tr>
            <tr>
                <td style="color: #333333;font-family:Arial,Helvetica,sans-serif;font-size:12px;font-weight:bold;width: 95px;" align="left">
                    Reporter Name :
                </td>
                <td style="color: #333333;font-family:Arial,Helvetica,sans-serif;font-size:12px;align:left;">
                    <bean:write name="person" property="fullName"/>
                </td>
                <td style="color: #333333;font-family:Arial,Helvetica,sans-serif;font-size:12px;font-weight:bold;" align="right">
                    Department&nbsp;&nbsp;&nbsp;:
                </td>
                <td style="color: #333333;font-family:Arial,Helvetica,sans-serif;font-size:12px;align:left;">
                    <bean:write name="person" property="dirDept"/>
                </td>
                <td style="color: #333333;font-family:Arial,Helvetica,sans-serif;font-size:12px;font-weight:bold;" align="right">
                    Reporter Email&nbsp;&nbsp;&nbsp;&nbsp;:
                </td>
                <td style="color: #333333;font-family:Arial,Helvetica,sans-serif;font-size:12px;align:left;">
                    <bean:write name="person" property="email"/>
                </td>
            </tr>
            <tr>
                <td style="color: #333333;font-family:Arial,Helvetica,sans-serif;font-size:12px;font-weight:bold;float: left">
                    Office Location :
                </td>
                <td style="color: #333333;font-family:Arial,Helvetica,sans-serif;font-size:12px;align:left;">
                    <bean:write name="person" property="offLocation"/>
                </td>
                <td style="color: #333333;font-family:Arial,Helvetica,sans-serif;font-size:12px;font-weight:bold;" align="right">
                    Office Phone :
                </td>
                <td style="color: #333333;font-family:Arial,Helvetica,sans-serif;font-size:12px;align:left;">
                    <bean:write name="person" property="offPhone"/>
                </td>
                <td style="color: #333333;font-family:Arial,Helvetica,sans-serif;font-size:12px;font-weight:bold;" align="right">
                    Secondary Office:
                </td>
                <td style="color: #333333;font-family:Arial,Helvetica,sans-serif;font-size:12px;align:left;">
                    <bean:write name="person" property="secOffLoc"/>
                </td>
            </tr>
            <tr>
                <td colspan="6">
                    <img height="2" border="0" width="100%" src="/coeus44server/coeusliteimages/line4.gif"/>
                </td>
            </tr>
        </table> --%>
     <%
            String startDate = "";
            String endDate = "";
            String pjctTitle="";
            String moduleItemKey="";
            String awardTitle="";
            String pjctId="";
            String projectType = (String) request.getSession().getAttribute("projectType");
            if(projectType.equalsIgnoreCase("Proposal")||projectType.equalsIgnoreCase("iacucProtocol")||projectType.equalsIgnoreCase("Protocol")||projectType.equalsIgnoreCase("Award")){
            Vector pjctDetails = (Vector)request.getSession().getAttribute("projectDetailsListInSeesion");
            if(pjctDetails !=null){
            CoiPersonProjectDetails coiPersonProjectDetails =(CoiPersonProjectDetails) pjctDetails.get(0);
            pjctTitle=coiPersonProjectDetails.getCoiProjectTitle();
            moduleItemKey=coiPersonProjectDetails.getModuleItemKey();
            awardTitle=coiPersonProjectDetails.getAwardTitle();
            pjctId=coiPersonProjectDetails.getModuleItemKey();
            CoiCommonService coiCommonService1 = CoiCommonService.getInstance();
            if(coiPersonProjectDetails.getCoiProjectStartDate()!=null)
            {
                startDate = (String) coiCommonService1.getFormatedDate(coiPersonProjectDetails.getCoiProjectStartDate());
            }
            if(coiPersonProjectDetails.getCoiProjectEndDate()!=null)
            {
              endDate = (String) coiCommonService1.getFormatedDate(coiPersonProjectDetails.getCoiProjectEndDate());
            }}}
        %>

        <table width="100%" border="0" class="table">
             <%
                if(projectType.equals("Protocol") || projectType != null && projectType.equalsIgnoreCase("iacucProtocol")){%>
               <tr><td colspan="2"><b>For your project listed below, please answer the certification questionnaire:</b></td></tr>
                <tr><td colspan="2"><b>Project #:</b><%=moduleItemKey%></td></tr>
                <tr><td colspan="2"><b>Project Title :</b><%=pjctTitle%></td></tr>
                <tr>
                    <td style="width: 415px;"><b>Application Date :</b><%=startDate%></td>
                    <td style="width: 405px;"><b>Expiration Date :</b><%=endDate%></td>
                </tr>
                 <%}else if(projectType.equals("Proposal")){%>
                        <tr><td colspan="2"><b>For your project listed below, please answer the certification questionnaire:</b></td></tr>
                        <tr><td colspan="2"><b>Project # :</b><%=moduleItemKey%></td></tr> 
                        <tr><td colspan="2"> <b>Project Title :</b><%=pjctTitle%></td></tr> 
                        <tr>
                            <td style="width: 415px;" ><b>Start Date :</b><%=startDate%></td>
                            <td style="width: 405px;"><b>End Date :</b><%=endDate%></td>
                        </tr>
                 <%} else if(projectType.equals("Award")){ %>
                     <tr><td colspan="2"><b>For your project listed below, please answer the certification questionnaire:</b></td></tr>
                     <tr><td colspan="2"><b>Project #:</b><%=moduleItemKey%></td></tr>
                     <tr><td colspan="2"> <b>Project Title :</b><%=awardTitle%></td></tr>
                     <tr>
                         <td style="width: 410px;"><b>Start Date :</b><%=startDate%></td>
                         <td style="width: 410px;"><b>End Date :</b><%=endDate%></td>
                     </tr>
                <% } %> 
                   
            <tr>
                <td align="left" valign="top" colspan="2">
                      <table width="100%" height="20"  border="0" cellpadding="0" cellspacing="0" class="table">
                                    <tr height="20px">
                                        <%--         <td>  <%=session.getAttribute("questionaireLabel")%>
                                                 </td>--%>
                                        <td height="23" colspan="2" class="theader" style="font-size:14px" ><bean:message bundle="coi" key="AnnualDisclosure.Certification.Header"/>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>

                        <table width="100%"  border="0" cellpadding="0" cellspacing="0" class="table">
                            <logic:present name="questionsList"> <tr><td class="copybold" height="30px"><bean:message bundle="coi" key="AnnualDisclosure.Certification.request"/></td></tr></logic:present>
                            <%--<tr>
                                <td>
                                    <div id="helpText" class='helptext'>
                                        <bean:message bundle="proposal" key="helpTextProposal.Questionnaire"/>
                                    </div>
                                </td>
     		</tr>--%>


                            <logic:messagesPresent message="true">
                                <tr>
                                    <td class="copybold">
                                        <script>errValue = true;</script>
                                        <font color='red'  style="font-size:12px;font-weight:'bold';">
                                            <html:messages id="message" message="true" property="answerMandatory" bundle="proposal">
                                                <li><bean:write name = "message"/></li>
                                            </html:messages>
                                            <html:messages id="message" message="true" property="numberFormatException" bundle="proposal">
                                                <li><bean:write name = "message"/></li>
                                            </html:messages>
                                            <html:messages id="message" message="true" property="notValidDate" bundle="proposal">
                                                <li><bean:write name = "message"/></li>
                                            </html:messages>
                                            <html:messages id="message" message="true" property="customElements.notValidLength" bundle="proposal">
                                                <li><bean:write name = "message"/></li>
                                            </html:messages>
                                            <html:messages id="message" message="true" property="errMsg" bundle="proposal">
                                                <script>errLock = true;</script>
                                                <li><bean:write name = "message"/></li>
                                            </html:messages>
                                        </font>
                                    </td>
                                </tr>
                            </logic:messagesPresent>
                                
                            <%--<logic:notPresent name="questionsList">
                                     <tr>
                                        <td colspan="8">
                                        <font color="red">No Questionnaire is defined for this module.</font>
                                        </td>
                                        </tr>
                                </logic:notPresent>--%>
                            <%
                                if(qnrListSize == 0) {
                                
                            %>
                            <tr>
                                        <td colspan="8">
                                        <font color="red"><bean:message bundle="coi" key="coiMessage.noQuestionnaire"/></font>
                                        </td>
                                        </tr>
                            <tr>
                                <td colspan='4' class='savebutton' align='left'>
                                    <html:submit  value="Continue" styleClass="clsavebutton" onclick="javascript:continueWithoutQnr();"/>
                                </td>
                            </tr>        
                             <%} else if(qnrListSize > 1) {%>
                                <tr>
                                        <td colspan="8">
                                        <font color="red"><bean:message bundle="coi" key="coiMessage.moreThanOneQuestionnaire"/></font>
                                        </td>
                                        </tr>
                             <%} else {%>
                            <logic:present name="questionsList">
                            <logic:notEmpty name="questionsList" property="list" scope="session">

                                <tr class="copybold"  bgcolor="#D6DCE5">
                                    <td height="20" valign="center" align="left" colspan="2">
                                        <%--   <%if(!modeValue){%>
                                               &nbsp;&nbsp;
                                               <%if(pageNo != null && pageNo.equals("1")){%>
                                                     <u><bean:message bundle="proposal" key="questionnaire.previous"/></u>
                                               <%} else {%>
                                                   <html:link href="javascript:processData('PREVIOUS')">
                                                       <u><bean:message bundle="proposal" key="questionnaire.previous"/></u>
                                                   </html:link>
                                               <%}%>
                                               &nbsp;&nbsp;&nbsp;&nbsp;
                                                   <html:link href="javascript:processData('MODIFY')">
                                                       <u><bean:message bundle="proposal" key="questionnaire.modify"/></u>
                                                   </html:link>
                                               &nbsp;&nbsp;&nbsp;&nbsp;
                                               <html:link href="javascript:processData('START_OVER')">
                                                   <u><bean:message bundle="proposal" key="questionnaire.startOver"/></u>
                                               </html:link>
                                           <%}} else {%>
                                               &nbsp;&nbsp;
                                                   <u><bean:message bundle="proposal" key="questionnaire.previous"/></u>
                                               &nbsp;&nbsp;&nbsp;&nbsp;
                                               <%if(mess != null && mess.equals("COMPLETED") && !originalMode){
                                                   %>
                                                   <html:link href="javascript:processData('MODIFY')">
                                                       <u><bean:message bundle="proposal" key="questionnaire.modify"/></u>
                                                   </html:link>
                                               <%} else {%>
                                               <html:link href="javascript:processData('MODIFY')">
                                                   <u><bean:message bundle="proposal" key="questionnaire.modify"/></u>
                                                   </html:link>
                                               <%}%>
                                               &nbsp;&nbsp;&nbsp;&nbsp;
                                                   <u><bean:message bundle="proposal" key="questionnaire.startOver"/></u>
                                           <%}%>--%>
                                    </td>
                                </tr>
                                <tr align="left">
                                    <td colspan="2" valign="top">
                                        <table width="100%" height="100%" border="0" cellpadding="5" cellspacing="0" >
                                            <%
                                                        String strBgColor = "#DCE5F3";
                                                        String calImage = request.getContextPath() + "/coeusliteimages/cal.gif";
                                                        HashMap hmQuestionNumber = new HashMap();
                                                        int questionNum = 1;
                                            %>

                                            <logic:iterate id="dynaFormData" name="questionsList" property="list" type="org.apache.struts.action.DynaActionForm" indexId="index" scope="session">
                                                <%
                                                            if (count % 4 == 0) {
                                                                strBgColor = "#D6DCE5";
                                                            } else {
                                                                strBgColor = "#DCE5F1";
                                                            }
                                                            String questionId = ((Integer) dynaFormData.get("questionId")).toString();
                                                            String questionNumber = ((Integer) dynaFormData.get("questionNumber")).toString();
                                                            String question = (String) dynaFormData.get("description");
                                                            String maxAnswers = ((Integer) dynaFormData.get("maxAnswers")).toString();
                                                            String answer = (String) dynaFormData.get("answer");
                                                            String validAnswers = (String) dynaFormData.get("validAnswer");
                                                            String lookUpName = (String) dynaFormData.get("lookUpName");
                                                            String lookupGUI = (String) dynaFormData.get("lookUpGui");
                                                            String answerDataType = (String) dynaFormData.get("answerDataType");
                                                            String answerMaxLength = ((Integer) dynaFormData.get("answerMaxLength")).toString();
                                                            String answerNumber = ((Integer) dynaFormData.get("answerNumber")).toString();
                                                            String searchName = (String) dynaFormData.get("searchName");
                                                            boolean isLookupPresent = false;
                                                            boolean isLookupDisabled = false;
                                                            if (lookupGUI != null && !lookupGUI.equals("")) {
                                                                isLookupPresent = true;
                                                            }
                                                            answer = (answer == null) ? "" : answer;
                                                %>
                                                <%if (Integer.parseInt(answerNumber) == 0) {%>
                                                <%--<tr height="5px"><td >&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>--%>
                                                <tr  bgcolor="<%=strBgColor%>" class="rowLine" onmouseover="className='rowHover rowLine'" onmouseout="className='rowLine'">
                                                    <td height="20" valign="top" class="copy">
                                                        <%String key = "" + questionId + questionNumber;
                                                            if (hmQuestionNumber.get(key) == null) {
                                                                hmQuestionNumber.put(key, "" + questionNum);
                                                                questionNum++;
                                                            }%>
                                                        <%=hmQuestionNumber.get(key)%>.&nbsp;
                                                    </td>
                                                    <td  width="50%" class="copy">
                                                        <bean:write name="dynaFormData" property="description"/>
                                                    </td>
                                                    <%} else {%>
                                                    <td width="20%" class="copy" nowrap valign="top">

                                                        <% if (validAnswers != null && !validAnswers.equals("")) {
                                                                if (validAnswers.equalsIgnoreCase("Text")) {%>

                                                        <%--if(Integer.parseInt(answerMaxLength) <= 20){ %>
                                                            <html:text property="answer" name="dynaFormData" maxlength="<%=answerMaxLength%>" indexed="true" disabled="<%=modeValue%>" onchange="dataChanged()"/>
                                                        <%}else if(Integer.parseInt(answerMaxLength) > 20 && Integer.parseInt(answerMaxLength) <= 80){%>
                                                            <html:text property="answer" name="dynaFormData" styleClass="textbox-long" maxlength="<%=answerMaxLength%>" indexed="true" disabled="<%=modeValue%>" onchange="dataChanged()"/>
                                                        <%}else{%>
                                                            <html:textarea property="answer" name="dynaFormData" styleClass="textbox-longer" cols="100" rows="5" indexed="true" disabled="<%=modeValue%>" onchange="dataChanged()"/>
                                                        <%}--%>

                                                        <%if (answer.length() <= 2000) {%>
                                                        <%if (answer.length() <= 60) {%>
                                                        <html:text property="answer" name="dynaFormData" indexed="true" disabled="<%=modeValue%>" onchange="dataChanged()"/>
                                                        <%} else if (answer.length() > 60 && answer.length() <= 120) {%>
                                                        <html:text property="answer" name="dynaFormData" styleClass="textbox-long" indexed="true" disabled="<%=modeValue%>" onchange="dataChanged()"/>
                                                        <%} else {%>
                                                        <html:textarea property="answer" name="dynaFormData" styleClass="textbox-longer" cols="150" rows="5" indexed="true" disabled="<%=modeValue%>" onchange="dataChanged()"/>
                                                        <%}%>
                                                        <%}%>

                                                        <% if (!modeValue) {
                                                                                                     if (answerDataType.equals("Date")) {
                                                                                                         String calender = "javascript:displayCalendarWithTopLeft('dynaFormData[" + count + "].answer',8,25)";%>

                                                        <html:link href="<%=calender%>" onclick="dataChanged()">
                                                            <html:img src="<%=calImage%>" border="0" height="16" width="16"/>
                                                        </html:link>
                                                        <%}
                                                                                                 }%>
                                                        <%} else if (validAnswers.equalsIgnoreCase("YN")) {
                                                        %>
                                                        <html:radio property="answer" name="dynaFormData" value="Y" indexed="true" disabled="<%=modeValue%>" onchange="dataChanged()"/>Yes
                                                        &nbsp;
                                                        <html:radio property="answer" name="dynaFormData" value="N" indexed="true" disabled="<%=modeValue%>" onchange="dataChanged()"/>No
                                                        <%} else if (validAnswers.equalsIgnoreCase("YNX")) {%>
                                                        <html:radio property="answer" name="dynaFormData" value="Y" indexed="true" disabled="<%=modeValue%>" onchange="dataChanged()"/>Yes
                                                        &nbsp;
                                                        <html:radio property="answer" name="dynaFormData" value="N" indexed="true" disabled="<%=modeValue%>" onchange="dataChanged()"/>No
                                                        <html:radio property="answer" name="dynaFormData" value="X" indexed="true" disabled="<%=modeValue%>" onchange="dataChanged()"/>N/A
                                                        <%} else if (isLookupPresent) {%>
                                                        <html:text property="answer" name="dynaFormData" maxlength="2000" indexed="true"  readonly="true" disabled="<%=modeValue%>" onchange="dataChanged()"/>
                                                        <%  String pageUrl = "javaScript:openLookupWindow('" + lookupGUI + "','" + lookUpName + "','','" + count + "')";
                                                                                                 if (!modeValue) {
                                                                                                     String image = request.getContextPath() + "/coeusliteimages/search.gif";%>
                                                        <html:link  href="<%=pageUrl%>" ><u><bean:message bundle="proposal" key="proposalOrganization.Search"/></u></html:link>
                                                        <%}%>

                                                        <%}%>
                                                        <%--<html:text property="answer" name="dynaFormData" maxlength="10" indexed="true" readonly ="true" disabled="<%=modeValue%>" />
                                                        <a id="hlIRBDate" onclick="displayCalendarWithTopLeft('dynaFormData[<%=count%>].answer',8,25)" tabindex="32" href="javascript:void(0);"
                                                         runat="server"><img id="imgIRBDate" title="" height="16" alt="" src="<bean:write name='ctxtPath'/>/coeusliteimages/cal.gif" width="16"
                                                         border="0" runat="server">
                                                        </a>--%>
                                                        <%}
                                                            if (!modeValue) {
                                                                if (isLookupPresent) {
                                                                    String image = request.getContextPath() + "/coeusliteimages/search.gif";
                                                                    //String pageUrl="javaScript:openLookupWindow('"+lookupGUI+"','"+lookUpName+"','"+validAnswers+"','"+count+"')";
                                                        %>
                                                        <%-- <html:link  href=""><html:img src="<%=image%>"/> </html:link>--%>
                                                        <%}
                                                            }%>
                                                    </td>
                                                    <td width="25%" class="copybold" nowrap valign="center">&nbsp;
                                                        <html:hidden property="searchName"  name="dynaFormData"  indexed="true"/>
                                                        <span style="font-weight:bold;" id="searchName<%=count%>">
                                                            <%if (searchName != null && searchName.length() > 0) {%>
                                                            <%=searchName%>
                                                            <%}%>
                                                        </span>
                                                    </td>
                                                </tr>
                                                <%}%>
                                                <% count++;%>
                                            </logic:iterate>
                                        </table>
                                    </td>
                                </tr>
                            </logic:notEmpty>
                        </logic:present>
                     
                            <tr>
                                <td>
                                    <font color='red'>
                                        <logic:present name="questionsList">
                                        <logic:empty name="questionsList" property="list" scope="session">
                                            <%String errorMessage = "";%>
                                            <%if (questionnaireModuleObject.getModuleItemCode() == 7) {%>
                                            <%errorMessage = "This " + session.getAttribute("questionaireLabel") + " is not answered for this protocol";%>
                                            <%} else if (questionnaireModuleObject.getModuleItemCode() == 3) {%>
                                            <%errorMessage = "This " + session.getAttribute("questionaireLabel") + " is not answered for this proposal";%>
                                            <%}%>
                                            <li> <%=errorMessage%></li>
                                        </logic:empty>
                                            </logic:present>
                                    </font>
                                </td>
                            </tr>
                            <logic:present name="questionsList">
                            <% if (!modeValue) {%>
                              <tr align="left" class='table'>
                                <td class='savebutton' nowrap align="left">
                                   <%-- &nbsp;<html:button property="saveAndProceed" styleClass="clsavebutton" style="width:150px;" value="<%=buttonName%>" onclick="javascript:saveproceed('SAVE')"/>--%>
                                  &nbsp;<html:button property="saveAndProceed" styleClass="clsavebutton" style="width:150px;" value="<%=buttonName%>" onclick="javascript:processData('SAVE')"/>
                                    <!-- &nbsp;&nbsp;&nbsp;&nbsp;
                                    <html:button property="print" styleClass="clsavebutton" value="Print" onclick="javascript:processData('PRINT')"/>

                                    &nbsp;<html:button property="saveAndProceed" styleClass="clsavebutton" style="width:150px;" value="Discard Changes" onclick="javascript:exitToCoi()" />

                                </td>
                            </tr>
                                    
                            <%} else if (modeValue) {
                                 /*CoeusDynaBeansList questList=(CoeusDynaBeansList)session.getAttribute("questionsList");
                                 request.setAttribute("questionsList",questList);*/%>

                            <!-- Modified for Case#4447 : Next phase of COI enhancements - Start
                            <tr><td><table width="100%" height="20"  colspan='2' border="0" cellpadding="0" cellspacing="0" class="table">-->

                            <tr>
                                <td colspan='4' class='savebutton' align='left'>
                                    <html:submit  value="Continue" styleClass="clsavebutton" onclick="javascript:continueProcess();"/>
                                </td>
                            </tr>
                            <!--Case#4447 - End-->                           
                            <%}%>
                            </logic:present>
                            <%}%>
                   </table>
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
  </table>
</body>
</html:html>
