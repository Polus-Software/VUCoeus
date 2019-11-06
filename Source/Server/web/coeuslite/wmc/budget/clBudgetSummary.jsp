<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<%@ page import="java.util.Vector,
             edu.mit.coeuslite.utils.ComboBoxBean,edu.mit.coeus.budget.bean.BudgetInfoBean"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld"  prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld"  prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"  prefix="coeusUtils"%>

<jsp:useBean  id="budgetInfoData"  scope="request" class="org.apache.struts.validator.DynaValidatorForm"/>
<jsp:useBean  id="budgetPeriodData"  scope="session" class="java.util.Vector"/>
<jsp:useBean  id="budgetStatus"  scope="session" class="java.util.Vector"/>
<jsp:useBean  id="proposalBudgetStatus"  scope="request" class="java.lang.String"/>
<jsp:useBean  id="rateClass"  scope="session" class="java.util.Vector"/>
<jsp:useBean  id="campusFlag" scope="session" class="java.util.Vector"/>  <!-- Case id# 2924 -->
<jsp:useBean  id="budgetInfoBean"  scope="session" class="edu.mit.coeus.budget.bean.BudgetInfoBean"/>
<jsp:useBean  id="statusUpdated"  scope="request" class="java.lang.String"/>


<html:html>
<%  
    String mode = (String)session.getAttribute("mode"+session.getId());
    String budgetStatusMode = (String)session.getAttribute("budgetStatusMode");
    String proposalNumber = budgetInfoBean.getProposalNumber();
    String versionNumber = String.valueOf(budgetInfoBean.getVersionNumber());
    java.text.NumberFormat numberFormat = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US);
    String totalCostLimit = numberFormat.format(budgetInfoBean.getTotalCostLimit());
    String residualFunds = numberFormat.format(budgetInfoBean.getResidualFunds());
    String totalDirectCostLimit = numberFormat.format(budgetInfoBean.getTotalDirectCostLimit());
    Vector vecAppAndPerTypeMessages = null;
   if(session.getAttribute("inactive_App_per_type_messages")!=null){
        vecAppAndPerTypeMessages = (Vector) session.getAttribute("inactive_App_per_type_messages"); 
   }
    boolean readOnly = false;
    if(mode!= null && mode.equals("display")){
       readOnly = true;
   }else if(budgetStatusMode != null && budgetStatusMode.equals("complete")){
        readOnly = true;
    }
    // Added for Cost Sharing Distribution Validation - 
    String forceCSDValidation = (String)session.getAttribute("ForceCSDInBudgetSummary");
    String validationMsg = (String)session.getAttribute("CSDSummaryValidationMsg");
    // Under Recovery Validation
    String forceUnderRecValidation = (String)session.getAttribute("forceURDInBudgetSummary");
    String validationURDMsg = (String)session.getAttribute("URDSummaryValidationMsg");     
%>
    <head>
        <title>Coeus Lite</title>
        <html:base/>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
    </head>         
    <body onload="checkDataChanged()">
        <script>
            //Function to Validate
            var errValue = false;
            var errLock = false;
            //Added/Modified for COEUSQA-1500 CoeusLite - Budget Summary - Allow end user to manually insert dollar amounts-Start
            var elementLength = 0;
            function validateFormData(validationCSDVal,validationURDVal){
            dataChanged();
            var elementValue = "dynaFormData[0].budgetStatusCode";
            elementValue = document.getElementsByName(elementValue);
            
            var bgtStatus = document.getElementById("bgtStatus").value;
            if(bgtStatus == "N") {
                alert('Budget Status cannot be set to none.');
                document.getElementById("bgtStatus").value = "I";
            }
            if(elementValue[0].selectedIndex == 1){
                    var finalVersionFlag = "dynaFormData[0].finalVersionFlag";
                    finalVersionFlag = document.getElementsByName(finalVersionFlag);
                    if(finalVersionFlag[0].checked == false){
                    alert("<bean:message bundle="budget" key="budgetSummary.selectFinalVersion" />");
                    elementValue[0].selectedIndex = 0;
                    return false;
                    }
                 }//End outer if
                 // Added for Cost Sharing Distribution Validation - start
                 if(elementValue[0].selectedIndex == 1){
                        var validateCSDMsg = '<%=validationMsg%>';
                        var validateURDMsg = '<%=validationURDMsg%>';
                        if(validationCSDVal == 'force' || validationURDVal == 'force'){
                            if(validateCSDMsg == 'amtUnequal'){
                                alert("<bean:message bundle="budget" key="costSharingDistribution.error.UnequalAmts1"/>\n<bean:message bundle="budget" key="costSharingDistribution.error.UnequalAmts2"/>");
                            }else if(validateCSDMsg == 'noCSD'){
                               alert("<bean:message bundle="budget" key="costSharingDistribution.error.CSDforVersion"/>");
                            }
                            if(validateURDMsg == 'amtUnequal'){
                                alert("<bean:message bundle="budget" key="underRecoveryDistribution.alertMessage1"/>\n<bean:message bundle="budget" key="underRecoveryDistribution.alertMessage2"/>");
                            }else if(validateURDMsg == 'noUnderRec'){
                               alert("<bean:message bundle="budget" key="underRecoveryDistribution.error.URDforVersion"/>");
                            }
                            elementValue[0].selectedIndex = 0;
                            return false;
                        }
                       /* }else if(validationURDVal == 'force'){
                            if(validateURDMsg == 'amtUnequal'){
                                alert("<%--bean:message bundle="budget" key="underRecoveryDistribution.alertMessage1"/>\n<bean:message bundle="budget" key="underRecoveryDistribution.alertMessage2"/--%>");
                            }else if(validateURDMsg == 'noUnderRec'){
                               alert("<%--bean:message bundle="budget" key="underRecoveryDistribution.error.URDforVersion"/--%>");
                            }
                            elementValue[0].selectedIndex = 0;
                            return false;
                        }*/
                        /*else if(validationCSDVal == 'notForce'){
                            return true;
                        }else if(validationURDVal == 'notForce'){
                            return true;
                        }*/
                        document.budgetSummaryDynaList.action = "<%=request.getContextPath()%>/budgetSummary.do?forwardTo=Validation";
                        document.budgetSummaryDynaList.submit();
                        
                 }
                 // Added for Cost Sharing Distribution Validation - end
            //return validateBudgetSummary(form);
            }//End function

            /*This function checks the budget status
            if the budget status is complete the final version flag
            is always checked 
            */
            function checkFinalVerison(){
            dataChanged();
            var finalVersionFlag = "dynaFormData[0].finalVersionFlag";
            finalVersionFlag = document.getElementsByName(finalVersionFlag);
            if(finalVersionFlag[0].checked == false){ 
                var elementValue = "dynaFormData[0].budgetStatusCode";
                elementValue = document.getElementsByName(elementValue);  
            if(elementValue[0].selectedIndex == 1){        
                finalVersionFlag[0].checked = true;
            }
            }
            }//End Fucntion

            var oldRateType ;


            function rateSelected(value){   
            oldRateType = value.selectedIndex;
            }
 
            function isOHRateTypeChanged(value){
            dataChanged();
            var msg = '<bean:message bundle="budget" key="budgetSummary.OHRateType" />';
            if (confirm(msg)==true){
            document.budgetSummaryDynaList.action = "<%=request.getContextPath()%>/changeLineItemOHRate.do";
            document.budgetSummaryDynaList.submit();                          
            }else{
                var elementValue = "dynaFormData[0].ohRateClassCode";
                elementValue = document.getElementsByName(elementValue);
                elementValue[0].selectedIndex = oldRateType;       
            return false;
            }
            } 


            function isURRateClassChanged(value){  
            dataChanged();
            var msg = '<bean:message bundle="budget" key="budgetSummary.URRateType" />';
            if (confirm(msg)==true){
            document.budgetSummaryDynaList.action = "<%=request.getContextPath()%>/changeLineItemURRate.do";
            document.budgetSummaryDynaList.submit();                          
            }else{
                var elementValue = "dynaFormData[0].urRateClassCode";
                elementValue = document.getElementsByName(elementValue);
                elementValue[0].selectedIndex = oldRateType;       
            return false;
            }
            }
            
        
        /* Case id# 2924 - start */
            function isOnOffChanged(value){           
            dataChanged();
            var msg = '<bean:message bundle="budget" key="budgetSummary.OnOffCampus" />';
            if (confirm(msg)==true){
            document.budgetSummaryDynaList.action = "<%=request.getContextPath()%>/changeLineItemOnOff.do";
            document.budgetSummaryDynaList.submit();                          
            }else{            
           // if(value == "Y" ){
           //     document.budgetSummaryDynaList.onOffCampusFlag.selectedIndex = 1 ;
           // }else {          
           //     document.budgetSummaryDynaList.onOffCampusFlag.selectedIndex = 0 ;
            //} 
                var elementValue = "dynaFormData[0].onOffCampusFlag";
                elementValue = document.getElementsByName(elementValue);
                elementValue[0].selectedIndex = oldRateType;
            return false;
            }
            }
        /* Case id# 2924 - end */
        
        function validateBudget(finalVersion){
            document.budgetSummaryDynaList.action = "<%=request.getContextPath()%>/budgetSummary.do?forwardTo=Validation";
            document.budgetSummaryDynaList.submit();
        }
        //Added/Modified for COEUSQA-1500 CoeusLite - Budget Summary - Allow end user to manually insert dollar amounts-End
        function checkDataChanged(){
        <logic:equal name="statusUpdated" value = "Y">
                dataChanged(); 
            </logic:equal>
        }
        //Added for COEUSQA-1500 CoeusLite - Budget Summary - Allow end user to manually insert dollar amounts-Start
        //Start
        function setAllTotalCost(index, fieldName){             
            var grandTotalDirectCost = '0.00';            
            var costElement = "";            
            var fieldCost = '0.00';
            var isDot = 0; 
            var ValidChars = "0123456789.";    
            var ch = '';
            var IsNumber = true;
                costElement = "dynaFormBean["+index+"]."+fieldName;
                costElement = document.getElementsByName(costElement);
                fieldCost = costElement[0].value;
                if(fieldCost == "" && fieldCost.length == 0){
                    fieldCost = '0.00';
                }             
                var strLen = fieldCost.length;
                for(var i=0;i<strLen;i++){
                    fieldCost = fieldCost.replace(",",""); 
                    fieldCost = fieldCost.replace("$",""); 
                }
                
                if(fieldCost<0){
                    costElement[0].value = '$'+'0.00';  
                    fieldCost = costElement[0].value;
                }
                if(isNaN(fieldCost)){
                    alert("Please Enter a Valid Number");
                    costElement[0].value = '$'+'0.00';
                    fieldCost = costElement[0].value;
                    return;
                }                
                fieldCost = parseFloat(fieldCost);
                grandTotalDirectCost = parseFloat(grandTotalDirectCost);                 
                for(var i=0;i<elementLength;i++){
                    costElement = "dynaFormBean["+i+"]."+fieldName;
                    costElement = document.getElementsByName(costElement);
                    fieldCost = costElement[0].value;
                    var len = fieldCost.length;
                    for(var j=0;j<len;j++){
                        fieldCost = fieldCost.replace("$","");                                                  
                        fieldCost = fieldCost.replace(",","");                                          
                    }
                    if(fieldCost == "" && fieldCost.length == 0){
                        fieldCost = '0.00';
                    }
                    fieldCost = Math.round(fieldCost*Math.pow(10,2))/Math.pow(10,2);                                                                                  
                    grandTotalDirectCost = grandTotalDirectCost + fieldCost;               
                }                   
                var newFieldCost = "dynaFormBean["+index+"]."+fieldName;
                newFieldCost = document.getElementsByName(newFieldCost);
                var fieldCostValue = newFieldCost[0].value;
                if(fieldCostValue == "" && fieldCostValue.length == 0){
                    fieldCostValue = '0.00';
                }
                var length = fieldCostValue.length;
                for(var j=0;j<length;j++){
                    fieldCostValue = fieldCostValue.replace("$","");                                                  
                    fieldCostValue = fieldCostValue.replace(",","");                                          
                }  
                fieldCostValue = parseFloat(fieldCostValue);
                fieldCostValue = Math.round(fieldCostValue*Math.pow(10,2))/Math.pow(10,2); 
                if(fieldCostValue.toString().indexOf(".")== -1){
                         fieldCostValue = '$'+fieldCostValue +'.00';
                    }else{
                        if(fieldCostValue.toString().length - fieldCostValue.toString().indexOf(".") == 2){
                            fieldCostValue = '$'+fieldCostValue+'0';
                        }else{
                            fieldCostValue = '$'+fieldCostValue;
                        }
                }                 
                newFieldCost[0].value = fieldCostValue;                 
                grandTotalDirectCost = Math.round(grandTotalDirectCost*Math.pow(10,2))/Math.pow(10,2);                    
                var isDot = grandTotalDirectCost.toString().indexOf(".");                
                if(isDot == -1){
                    grandTotalDirectCost = grandTotalDirectCost +'.00'                    
                }else{                     
                    if(grandTotalDirectCost.toString().length - isDot == 2){
                        grandTotalDirectCost = grandTotalDirectCost+'0';
                    }
                }
                if('strTotalDirectCost' == fieldName){                
                    document.getElementById("totalAllDirectCost").innerHTML='$'+grandTotalDirectCost;
                }else if('strTotalIndirectCost' == fieldName){
                    document.getElementById("totalAllIndirectCost").innerHTML='$'+grandTotalDirectCost;
                }else if('strTotalCost' == fieldName){
                    document.getElementById("totalAllCost").innerHTML='$'+grandTotalDirectCost;
                }else if('strUnderRecoveryAmount' == fieldName){
                    document.getElementById("underAllRecoveryAmount").innerHTML='$'+grandTotalDirectCost;
                }else{
                    document.getElementById("costAllSharingAmount").innerHTML='$'+grandTotalDirectCost;
                }
                dataChanged();
        }
        //End
          
        //Start
        function setTotalCost(index, fieldName){  
            var ValidChars = "0123456789.";    
            var ch = '';
            var IsNumber = true;
            var strTotalDirectCost = "dynaFormBean["+index+"].strTotalDirectCost"; 
            var strTotalIndirectCost = "dynaFormBean["+index+"].strTotalIndirectCost";             
            strTotalDirectCost = document.getElementsByName(strTotalDirectCost);
            strTotalIndirectCost = document.getElementsByName(strTotalIndirectCost);                                
            var index = parseInt(index);            
            var totalDICost = strTotalDirectCost[0].value;             
            var totalIDCost = strTotalIndirectCost[0].value;   
            if(totalDICost == "" && totalDICost.length == 0){
                totalDICost = '0.00';
            }
            if(totalIDCost == "" && totalIDCost.length == 0){
                totalIDCost = '0.00';
            }
            var sumOfAllTotalCost = '0.00';
            var allTotalDirectCost = '0.00';
            var total = '0.00';
            var totalIDCostLength = totalIDCost.length;
            for(var i=0;i<totalIDCostLength;i++){            
                totalIDCost = totalIDCost.replace(",","");
                totalIDCost = totalIDCost.replace("$","");
            }
            var totalDICostLength = totalDICost.length;
            for(var i=0;i<totalDICostLength;i++){            
                totalDICost = totalDICost.replace(",","");
                totalDICost = totalDICost.replace("$","");
            }                   
            if(totalDICost<0 || totalIDCost<0){                
                if(totalDICost<0){
                    strTotalDirectCost[0].value = '$'+'0.00';  
                    totalDICost = strTotalDirectCost[0].value;
                }
                if(totalIDCost<0){
                    strTotalIndirectCost[0].value = '$'+'0.00';  
                    totalIDCost = strTotalIndirectCost[0].value;
                }   
            }
            if(isNaN(totalDICost)|| isNaN(totalIDCost)){
              alert("Please Enter a Valid Number");                              
              if(isNaN(totalDICost)){
                strTotalDirectCost[0].value = '$'+'0.00';
                totalDICost = strTotalDirectCost[0].value;
              }
              if(isNaN(totalIDCost)){
                strTotalIndirectCost[0].value = '$'+'0.00';
                totalIDCost = strTotalIndirectCost[0].value;
              }
              return;
            }
            totalIDCost = parseFloat(totalIDCost);
            totalDICost = parseFloat(totalDICost);
            var totalCost = "dynaFormBean["+index+"].strTotalCost";
            var strTotalCost = document.getElementsByName(totalCost);             
            var finalTotalCost = totalIDCost + totalDICost;                
            finalTotalCost = Math.round(finalTotalCost*Math.pow(10,2))/Math.pow(10,2);            
            var is_dot_ok = finalTotalCost.toString().indexOf(".");
            if(is_dot_ok == -1){
                finalTotalCost = finalTotalCost+'.00';                 
            }              
            if(finalTotalCost.toString().length - is_dot_ok == 2){
                finalTotalCost = finalTotalCost+'0';  
            }
            strTotalCost[0].value = '$'+finalTotalCost;            
            if(parseFloat(finalTotalCost)>0){                
                strTotalCost[0].readOnly=true;                 
            }else{
                strTotalCost[0].readOnly=false;
            }              
            for(var i=0;i<elementLength;i++){
                sumOfAllTotalCost = "dynaFormBean["+i+"].strTotalCost";
                sumOfAllTotalCost = document.getElementsByName(sumOfAllTotalCost);
                if(sumOfAllTotalCost == "" && sumOfAllTotalCost.length == 0){
                    sumOfAllTotalCost = '0.00';
                }
                var isDollar=sumOfAllTotalCost[0].value.charAt(0);                         
                if('$' == isDollar){            
                    sumOfAllTotalCost = sumOfAllTotalCost[0].value.substr(1); 
                }                     
                allTotalDirectCost = parseFloat(allTotalDirectCost) + parseFloat(sumOfAllTotalCost);               
            }
            var isDot = allTotalDirectCost.toString().indexOf(".");                 
                if(isDot == -1){
                    allTotalDirectCost = allTotalDirectCost +'.00'                    
                }                 
            document.getElementById("totalAllCost").innerHTML='$'+allTotalDirectCost;
                setAllTotalCost(index, fieldName);
                dataChanged();
        }   
        //End                         
        //Start
        function setStatus(index, elementObject){
            var strTotalDirectCost = "dynaFormBean["+index+"].strTotalDirectCost"; 
            var strTotalIndirectCost = "dynaFormBean["+index+"].strTotalIndirectCost";             
            strTotalDirectCost = document.getElementsByName(strTotalDirectCost);
            strTotalIndirectCost = document.getElementsByName(strTotalIndirectCost);                                                     
            var totalDICost = strTotalDirectCost[0].value;             
            var totalIDCost = strTotalIndirectCost[0].value;
            if(totalDICost == "" && totalDICost.length == 0){
                totalDICost = '0.00';
            }
            if(totalIDCost == "" && totalIDCost.length == 0){
                totalIDCost = '0.00';
            }
            var idDollar=totalIDCost.charAt(0);
            var diDollar=totalDICost.charAt(0);                         
            if('$' == idDollar){            
                totalIDCost = totalIDCost.substr(1); 
            }
            if('$' == diDollar){
                totalDICost = totalDICost.substr(1); 
            }            
            var totalCost = "dynaFormBean["+index+"].strTotalCost";
            totalCost = document.getElementsByName(totalCost); 
            var finalTotalCost = parseFloat(totalIDCost) + parseFloat(totalDICost);             
            finalTotalCost = Math.round(finalTotalCost*Math.pow(10,2))/Math.pow(10,2);             
             if(parseFloat(finalTotalCost)>0){                
                totalCost[0].readOnly=true;               
            }else{
                totalCost[0].readOnly=false;           
            }  
        }  
        //End
        //COEUSQA-1500 CoeusLite - Budget Summary - Allow end user to manually insert dollar amounts-End
        </script>

        <html:messages id="message" message="true" property="onlyViewRights" bundle="budget">     
            <bean:write name = "message"/>                                                                  
        </html:messages>
        <!--Added/Modified for COEUSQA-1500 CoeusLite - Budget Summary - Allow end user to manually insert dollar amounts-Start-->
        <html:form action="/budgetSummary.do" method="post"> 
        <%if(!readOnly && "CHANGE_BUDGET_DATES".equals(request.getAttribute("CHANGE_BUDGET_DATES"))){%>
        <script>
                if(confirm("<bean:message bundle="budget" key="adjustPeriod_exceptionCode.1465"/>")){
                    document.budgetSummaryDynaList.action = "<%=request.getContextPath()%>/getAdjustPeriodData.do?periodsChangeRequired=true";
                    document.budgetSummaryDynaList.submit();
                }
        </script>        
        <%}%>
            <table width="100%" height="100%"  border="0" cellpadding="2" cellspacing="0" class="table">
                <tr class="theader">
                    <td><bean:message bundle="budget" key="summaryHeader.BudgetSummary"/></td>
                </tr>
                <tr class='copybold'>
                    <td>
                        <font color="red"  >
                            <logic:messagesPresent>
                                <script>errValue = true;</script>
                                <html:errors header="" footer=""/>
                            </logic:messagesPresent>
                            
                            <logic:messagesPresent message="true">
                                <script>errValue = true;</script>
                                <html:messages id="message" message="true" property="error.invalidTotalCostLimit" bundle="budget">
                                    <font color='red'><li><bean:write name = "message"/></li></font>
                                </html:messages>
                                <html:messages id="message" message="true" property="error.budgetSummary.totalCostLimit" bundle="budget">
                                    <font color='red'><li><bean:write name = "message"/></li></font>
                                </html:messages>
                                <html:messages id="message" message="true" property="error.budgetSummary.totalDirectCost" bundle="budget">
                                    <font color='red'><li><bean:write name = "message"/></li></font>
                                </html:messages>
                                <html:messages id="message" message="true" property="error.budgetSummary.residualFunds" bundle="budget">
                                    <font color='red'><li><bean:write name = "message"/></li></font>
                                </html:messages>
                                <html:messages id="message" message="true" property="budget_summary_modular_budget_exceptionCode.1120" bundle="budget">
                                    <font color='red'><li><bean:write name = "message"/></li></font>
                                </html:messages>
                                <html:messages id="message" message="true" property="budget_summary_modular_budget_exceptionCode.1121" bundle="budget">
                                    <font color='red'><li><bean:write name = "message"/></li></font>
                                </html:messages>
                                <!--COEUSQA-1500 CoeusLite - Budget Summary - Allow end user to manually insert dollar amounts-Start-->
                                <html:messages id="message" message="true" property="error.budgetSummary.directCost" bundle="budget">
                                    <font color='red'><li><bean:write name = "message"/></li></font>
                                </html:messages>
                                <html:messages id="message" message="true" property="error.budgetSummary.inDirectCost" bundle="budget">
                                    <font color='red'><li><bean:write name = "message"/></li></font>
                                </html:messages>
                                <html:messages id="message" message="true" property="error.budgetSummary.underRecoverCost" bundle="budget">
                                    <font color='red'><li><bean:write name = "message"/></li></font>
                                </html:messages>
                                <html:messages id="message" message="true" property="error.budgetSummary.costSharing" bundle="budget">
                                    <font color='red'><li><bean:write name = "message"/></li></font>
                                </html:messages>
                                <html:messages id="message" message="true" property="error.budgetSummary.TotalCost" bundle="budget">
                                    <font color='red'><li><bean:write name = "message"/></li></font>
                                </html:messages>                               
                                <!--COEUSQA-1500 CoeusLite - Budget Summary - Allow end user to manually insert dollar amounts-End-->
                                <!-- If lock is deleted then show this message --> 
                                <html:messages id="message" message="true" property="errMsg">
                                    <script>errLock = true;</script>
                                    <li><bean:write name = "message"/></li>
                                </html:messages>
                                <!-- If lock is acquired by another user, then show this message -->
                                <html:messages id="message" message="true" property="acqLock">
                                    <script>errLock = true;</script>
                                    <li><bean:write name = "message"/></li>
                                </html:messages>
                            </logic:messagesPresent>  
                            <!-- Added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - start -->
                            <%if(vecAppAndPerTypeMessages != null && vecAppAndPerTypeMessages.size() >0) {%>                                                
                            <%for(int index = 0; index < vecAppAndPerTypeMessages.size(); index++){%>
                            <tr>
                                <td align = "left">
                                    <font color="red">
                                        <li> <%=vecAppAndPerTypeMessages.elementAt(index)%> </li>
                                    </font>
                                </td>
                                <td>
                                    <br>
                                    &nbsp;
                                    <br>
                                </td>
                            </tr>                                                
                            <%}}
                            session.setAttribute("inactive_App_per_type_messages",null);
                            %>
                            <!--Added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - end-->
                        </font>
                    </td>
                </tr>
                
                
                <tr>
                    <td height="10%" align="left" valign="top"><br>
                        <table width="98%"  border="0" align="center" cellpadding="2" cellspacing="0" class="tabtable">
                            
                            <tr>
                                <td colspan="4" align="left" class="tableheader">
                                    <bean:message bundle="budget" key="summaryHeader.BudgetSummary"/> : <bean:message bundle="budget" key="budgetVersions.Version" /> <%=versionNumber%> 
                                </td>
                            </tr>
                            <tr>
                                <td colspan="4">
                                    <div id="helpText" class='helptext'>            
                                        <bean:message bundle="budget" key="helpTextBudget.Summary"/>  
                                    </div>  
                                </td>
                            </tr>
                            
                            <tr>
                                <td width='10%' align="right" class='copy'>
                                    &nbsp;<b><bean:message bundle="budget" key="summaryLabel.BudgetStatus"/>: </b>
                                </td> 
                                <!--Modified COEUSQA-1500 CoeusLite - Budget Summary - Allow end user to manually insert dollar amounts-Start-->
                                <logic:present name="budgetSummaryDynaList" property="list" scope="session">  
                                <logic:iterate id="dynaFormData" name="budgetSummaryDynaList" property="list"
                                               type="org.apache.struts.action.DynaActionForm" indexId="listIndex" scope="session">                                             
                                <td width='40%' align="left" >
                                    <% // modified for Cost Sharing Distribution Validation - 
                                    String validateForm = "javascript: validateFormData('"+forceCSDValidation+"','"+forceUnderRecValidation+"');";%>
                                    <html:select name="dynaFormData" property="budgetStatusCode" styleClass="textbox-long" indexed="true" disabled="<%=readOnly%>" onchange="<%=validateForm%>" styleId="bgtStatus">
                                        <html:options collection="budgetStatus"  property="code" labelProperty="description"  />
                                    </html:select>
                                </td>
                                
                                <%--   <td width='20%' align="right" class='copy'><b><bean:message bundle="budget" key="summaryLabel.Final"/>:&nbsp;</b>
                                    <html:checkbox name="budgetSummary" property="finalVersionFlag" value="Y" style="copy" onclick="checkFinalVerison()" disabled="<%=readOnly%>"/>
                                </td>
                                <td width='32%' align="left" class='copy'>&nbsp;&nbsp;&nbsp;<b><bean:message bundle="budget" key="summaryLabel.ModularBudget"/>:&nbsp;</b>                       
                                    <html:checkbox name="budgetSummary" property="modularBudgetFlag" value="Y" style="copy" disabled="<%=readOnly%>" onclick="dataChanged()"/>
                                </td>   --%>
                                
                                <td width='20%' colspan="2" align="left" class='copy'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b><bean:message bundle="budget" key="summaryLabel.Final"/>:</b>
                                    <html:checkbox name="dynaFormData" property="finalVersionFlag" value="Y" style="copy" indexed="true" onclick="checkFinalVerison()" disabled="<%=readOnly%>"/>
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b><bean:message bundle="budget" key="summaryLabel.ModularBudget"/>:</b>                       
                                    <html:checkbox name="dynaFormData" property="modularBudgetFlag" value="Y" style="copy" indexed="true" disabled="<%=readOnly%>" onclick="dataChanged()"/>
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b><bean:message bundle="budget" key="summaryLabel.SubmitCostSharingFlag"/>:</b>                       
                                    <html:checkbox name="dynaFormData" property="submitCostSharingFlag" value="Y" style="copy" indexed="true" disabled="<%=readOnly%>" onclick="dataChanged()"/>
                                </td>    
                            </tr>
                            <!-- Case id# 2924 start -->
                            <tr>
                                <td width='10%' align="right" class='copy'>&nbsp;<b><bean:message bundle="budget" key="summaryLabel.OnOffCampus"/>:&nbsp;</b></td>
                                <td width='40%' align="left" class='copy'>
                                    <html:select name="dynaFormData" property="onOffCampusFlag" styleClass="textbox-long" indexed="true" disabled="<%=readOnly%>" onchange="isOnOffChanged(this.value)" onclick="rateSelected(this)">
                                        <html:options collection="campusFlag" property="code" labelProperty="description"  />
                                    </html:select>
                                </td>
                                <td width='20%' align="right" class='copy'><b><bean:message bundle="budget" key="summaryLabel.TotalCostLimit"/>: </b></td>
                                <td width='32%' align="left" class='copy'>                                                                                                                            
                                    <html:text size="25" name="dynaFormData"  property="totalCostLimit"  style="text-align: right"  styleClass="cltextbox" indexed="true" value="<%=totalCostLimit%>" disabled="<%=readOnly%>" onchange="dataChanged()"/>
                                </td>
                            </tr>
                            <!-- Case id# 2924 end -->
                            <tr>    
                                <td width='10%' align="right" class='copy'>&nbsp;<b><bean:message bundle="budget" key="summaryLabel.ResidualFunds"/>:</b></td>
                                <td width='40%' align="left" class='copy'>
                                    <html:text size="25" name="dynaFormData" property="residualFunds"  value="<%=residualFunds%>" maxlength="14" style="text-align: right" indexed="true" styleClass="cltextbox" disabled="<%=readOnly%>" onchange="dataChanged()"/> 
                                </td>
                                
                                
                                <td width='20%' align="right" class='copy'><b>Total Direct Cost Limit: </b></td>
                                <td width='32%' align="left" class='copy'>                                                                                                                            
                                    <html:text size="25" name="dynaFormData" property="totalDirectCostLimit"  style="text-align: right"  styleClass="cltextbox" value="<%=totalDirectCostLimit%>" indexed="true" disabled="<%=readOnly%>" onchange="dataChanged()"/>
                                </td>
                            </tr>
                            
                            <tr>
                                <td width='10%'  nowrap align="right" valign="top" class='copy'>&nbsp;<b><bean:message bundle="budget" key="summaryLabel.OverHeadRateType"/>:</b></td>
                                <td width='40%' align="left">
                                    
                                    <html:select name="dynaFormData" property="ohRateClassCode" styleClass="textbox-long" disabled="<%=readOnly%>" indexed="true" onchange="isOHRateTypeChanged(this.value)" onclick="rateSelected(this)">
                                        <html:options collection="rateClass" property="code" labelProperty="description"  />
                                    </html:select>
                                    
                                </td>
                                <td width='20%'  nowrap align="right" valign="top" class='copy'><b><bean:message bundle="budget" key="summaryLabel.UnderrecoveryRateType"/>: </b></td>
                                <td width='30%' align="left">
                                    <html:select name="dynaFormData" property="urRateClassCode" styleClass="textbox-long" disabled="<%=readOnly%>" indexed="true" onchange="isURRateClassChanged(this.value)" onclick="rateSelected(this)">
                                        <html:options collection="rateClass" property="code" labelProperty="description"  />
                                    </html:select>
                                </td>
                            </tr>
                            
                            <tr>
                                <td width='10%' align="right" valign="top" class='copybold'>
                                    &nbsp;<bean:message bundle="budget" key="summaryLabel.Comments"/>:
                                </td>
                                <td align="left" colspan='3'>
                                    <html:textarea name="dynaFormData" property="comments" cols="122" styleClass="textbox-longer" indexed="true" disabled="<%=readOnly%>" onchange="dataChanged()"/>
                                    <script>                                    
                                    if(navigator.appName == "Microsoft Internet Explorer")
                                    {
                                        var elementValue = "dynaFormData[0].comments";
                                        elementValue = document.getElementsByName(elementValue);
                                        elementValue[0].cols=128;
                                        elementValue[0].rows=3;
                                    }                                    
                                    </script>
                                </td>
                            </tr>                                              
                            <!--Modified COEUSQA-1500 CoeusLite - Budget Summary - Allow end user to manually insert dollar amounts-End-->
                            <!--
                        <tr>
                        <td colspan="4" align="left" valign="top">
                        <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                        <tr>
                        <td width="1%">&nbsp;</td>
                        <td width="97%" align="center" valign="top">                                        
                            <%--<html:submit property="Save" value="Save" styleClass="clbutton" disabled="<%=readOnly%>"/>--%>
                        </td>
                        <td width="2%">&nbsp;</td>
                        </tr>
                        <tr><td> &nbsp; </td></tr>
                        </table>
                        </td>
                        </tr> 
                        -->

                            <html:hidden name="dynaFormData" property="proposalNumber"/>
                            <html:hidden name="dynaFormData" property="versionNumber"/>
                            <html:hidden name="dynaFormData" property="startDate"/>
                            <html:hidden name="dynaFormData" property="endDate"/>
                            <html:hidden name="dynaFormData" property="totalCost"/>
                            <html:hidden name="dynaFormData" property="totalDirectCost"/>
                            <html:hidden name="dynaFormData" property="totalIndirectCost"/>
                            <html:hidden name="dynaFormData" property="costSharingAmount"/>
                            <html:hidden name="dynaFormData" property="underRecoveryAmount"/>
                            <html:hidden name="dynaFormData" property="ohRateTypeCode"/>
                            <html:hidden name="dynaFormData" property="updateTimestamp"/>
                            <html:hidden name="dynaFormData" property="updateUser"/>
                            <html:hidden name="dynaFormData" property="unitNumber"/>
                            <html:hidden name="dynaFormData" property="activityTypeCode"/>                             
                            </logic:iterate>
                            </logic:present>         
                        </table>
                        <br>
                    </td>
                </tr>
                
                <%--<tr>
                    <td class='savebutton'>&nbsp;&nbsp;
                        <html:submit property="Save" value="Save" styleClass="clsavebutton" disabled="<%=readOnly%>"/>
                        <br><br>
                    </td>
                </tr>--%>
                
                <tr>
                    <td>
                        <table align='center' width="98%" border="0" cellpadding="2" cellspacing="0" class="tabtable">
                            <tr>
                                <td class='tableheader' colspan='6'>
                                    <bean:message bundle="budget" key="summaryHeader.BudgetTotals" />
                                </td>
                            </tr>
                            <!--Modified COEUSQA-1500 CoeusLite - Budget Summary - Allow end user to manually insert dollar amounts-Start-->
                            <logic:present name ="budgetInfoBean" scope = "session" >  
                                
                                <tr>
                                    <td align="right" nowrap>&nbsp;<b><bean:message bundle="budget" key="budgetLabel.directCost" /> :</b>
                                    </td>
                                    <td align="20%" nowrap>  
                                    <%
                                        String totalAllDirectCost = numberFormat.format(budgetInfoBean.getTotalDirectCost());
                                    %>                                
                                        <span id="totalAllDirectCost"><%=totalAllDirectCost%></span>
                                      <%--<coeusUtils:formatString name="budgetInfoBean" property="totalDirectCost" formatType="currencyFormat"/>--%>
                                        </td>
                                    <td  align="right" nowrap><b> <bean:message bundle="budget" key="budgetLabel.inDirectCost" /> :</b>
                                    </td>
                                    <td align="120%" nowrap>
                                    <%
                                        String totalAllIndirectCost = numberFormat.format(budgetInfoBean.getTotalIndirectCost());
                                    %>                                
                                        <span id="totalAllIndirectCost"><%=totalAllIndirectCost%></span>
                                        <%--<coeusUtils:formatString  name="budgetInfoBean" property="totalIndirectCost" formatType="currencyFormat"/>--%>
                                        </td>
                                    <td align="right" nowrap><b> <bean:message bundle="budget" key="budgetLabel.totalCost" /> :</b>
                                    </td>
                                    <td align="170%" nowrap>
                                    <%
                                        String totalCost = numberFormat.format(budgetInfoBean.getTotalCost());
                                    %>                                
                                        <span id="totalAllCost"><%=totalCost%></span>
                                        <%--<coeusUtils:formatString name="budgetInfoBean" property="totalCost" formatType="currencyFormat"/>--%>
                                        </td>
                                </tr>
                                
                                <tr>
                                    <td align="right" nowrap>&nbsp;<b><bean:message bundle="budget" key="budgetLabel.underRecovery" />  :</b>
                                    </td>
                                    <td align="20%" nowrap>
                                    <%
                                        String underRecoveryAmount = numberFormat.format(budgetInfoBean.getUnderRecoveryAmount());
                                    %>
                                         <span id="underAllRecoveryAmount"><%=underRecoveryAmount%></span>
                                        <%--<coeusUtils:formatString name="budgetInfoBean" property="underRecoveryAmount" formatType="currencyFormat"/>--%>
                                        </td>
                                    <td align="right" nowrap><b> <bean:message bundle="budget" key="budgetLabel.costShare" /> :</b>
                                    </td>
                                    <td align="120%" nowrap>
                                    <%
                                        String costSharingAmount = numberFormat.format(budgetInfoBean.getCostSharingAmount());
                                    %>
                                         <span id="costAllSharingAmount"><%=costSharingAmount%></span>
                                        <%--<coeusUtils:formatString name="budgetInfoBean" property="costSharingAmount" formatType="currencyFormat"/>--%>
                                        </td>
                                    <td align="right" nowrap><b> <bean:message bundle="budget" key="budgetSummary.period" /> :</b>
                                    </td>
                                    <td align="170%" nowrap>
                                        <coeusUtils:formatDate name="budgetInfoBean" property="startDate" />                                       
                                        <coeusUtils:formatDate name="budgetInfoBean" property="endDate" />   
                                        </td>
                                </tr>                   
                            </logic:present>
                            <!--Modified COEUSQA-1500 CoeusLite - Budget Summary - Allow end user to manually insert dollar amounts-End-->
                        </table>
                    </td>
                </tr>
                
                
                <tr>
                    <td align="left" valign="top"><br>
                        <table width="98%" border="0" align="center" cellpadding="2" cellspacing="0" class="tabtable">
                            <tr>
                                <td class='tableheader' colspan='9'>
                                    <bean:message bundle="budget" key="summaryHeader.BudgetPeriods"/>
                                </td>
                            </tr>
                            
                            <tr align="center" valign="top">
                                <td  align="left" class="theader" width="3%" >&nbsp;<bean:message bundle="budget" key="summaryLabel.Period"/></td>
                                <td  align="left" class="theader" width="12%"><bean:message bundle="budget" key="summaryLabel.StartDate"/> </td>
                                <td  align="left" class="theader" width="12%"><bean:message bundle="budget" key="summaryLabel.EndDate"/> </td>
                                <td  align="left" class="theader" width="6%"><bean:message bundle="budget" key="budgetLabel.NoOfMonths"/> </td>
                                <td  align="right" class="theader" width="13%"><bean:message bundle="budget" key="summaryLabel.DirectCost"/> </td>
                                <td  align="right" class="theader" width="13%"><bean:message bundle="budget" key="summaryLabel.IndirectCost"/> </td>
                                <td  align="right" class="theader" width="13%"><bean:message bundle="budget" key="summaryLabel.UnderRecovery"/> </td>
                                <td  align="right" class="theader" width="13%"><bean:message bundle="budget" key="summaryLabel.CostSharing"/> </td>
                                <td  align="right" class="theader" width="13%"><bean:message bundle="budget" key="summaryLabel.TotalCost"/> </td>
                            </tr>
                            <!--Modified COEUSQA-1500 CoeusLite - Budget Summary - Allow end user to manually insert dollar amounts-Start-->
                            <%
                            String strBgColor = "#DCE5F1";
                            int index=0;
                            String calImage = request.getContextPath()+"/coeusliteimages/cal.gif";
                            String totalDirectCost = "";
                            String getTotalCost = "";
                            String underRecoveryAmount = "";
                            String costSharingAmount = "";
                            String totalCost = ""; 
                            String setStatus = "";
                            String totalCostUnderRecoveryAmount = "";
                            String totalCostSharingAmount = "";
                            String allTotalCost = "";
                            %>                             
                            <%--<logic:present name ="budgetPeriodData" scope = "session">                              
                                <logic:iterate id = "budgetPeriods" name ="budgetPeriodData" 
                                               type = "org.apache.commons.beanutils.DynaBean">--%>
                            <logic:present name="budgetSummaryDynaList" property="beanList" scope="session">
                                <logic:iterate id="dynaFormBean" name="budgetSummaryDynaList" 
                                    property="beanList" indexId="beanListIndex" type="org.apache.struts.action.DynaActionForm" scope="session" >
                              
                                    
                                    <%
                                    boolean disabled = readOnly;
                                    if (index%2 == 0) {
                                    strBgColor = "#D6DCE5"; 
                                    }
                                    else { 
                                    strBgColor="#DCE5F1"; 
                                    }  
                                    if("Y".equals(dynaFormBean.get("modularBudgetFlag"))){
                                        disabled = true;
                                    }                                     
                                    %>     
                                    <tr  bgcolor="<%=strBgColor%>" onmouseover="className='TableItemOn'" onmouseout="className='TableItemOff'"> 
                                        <td class="copy">
                                            &nbsp;<coeusUtils:formatOutput name="dynaFormBean" property="budgetPeriod"/>
                                        </td>
                                        <td class="copy">
                                            <coeusUtils:formatDate name="dynaFormBean" property="startDate" />                                                           
                                        </td>
                                        <td class="copy">
                                            <coeusUtils:formatDate name="dynaFormBean" property="endDate" />             
                                        </td>
                                        <td class="copy">
                                           <coeusUtils:formatOutput name="dynaFormBean" property="noOfPeriodMonths" />                    
                                        </td>
                                        <td class="copy" align="right">
                                            <%
                                                totalDirectCost = numberFormat.format(dynaFormBean.get("totalDirectCost"));
                                                getTotalCost = "javascript: setTotalCost('"+index+"','strTotalDirectCost');";
                                             
                                            %>                                            
                                            <html:text name="dynaFormBean" style="text-align: right;width:108px;" size="14" maxlength="14" property="strTotalDirectCost" indexed="true" styleClass="cltextbox" value="<%=totalDirectCost%>"   onchange="<%=getTotalCost%>" disabled="<%=disabled%>"/>                                             
                                            <%--<coeusUtils:formatString name="budgetPeriods" property="totalDirectCost" 
                                                                     formatType="currencyFormat"/>--%>
                                        </td>
                                        <td class="copy" align="right">
                                            <%
                                                    String totalIndirectCost = numberFormat.format(dynaFormBean.get("totalIndirectCost"));                                            
                                                    getTotalCost = "javascript: setTotalCost('"+index+"','strTotalIndirectCost');";                                                    
                                            %>
                                            <html:text name="dynaFormBean" size="14" maxlength="14" property="strTotalIndirectCost" style="text-align: right;width:108px;" indexed="true" styleClass="cltextbox" value="<%=totalIndirectCost%>" onchange="<%=getTotalCost%>" disabled="<%=disabled%>"/>                                             
                                           <%-- <coeusUtils:formatString name="budgetPeriods" property="totalIndirectCost" 
                                                                     formatType="currencyFormat"/>--%>
                                        </td>
                                        <td class="copy" align="right">
                                            <%                                                                       
                                                    underRecoveryAmount = numberFormat.format(dynaFormBean.get("underRecoveryAmount"));
                                                    totalCostUnderRecoveryAmount = "javascript: setAllTotalCost('"+index+"','strUnderRecoveryAmount');";
                                            %>
                                            <html:text name="dynaFormBean" size="14" maxlength="14" property="strUnderRecoveryAmount" style="text-align: right" indexed="true" styleClass="cltextbox" value="<%=underRecoveryAmount%>" disabled="<%=disabled%>" onchange='<%=totalCostUnderRecoveryAmount%>'/>                                             
                                            <%--<coeusUtils:formatString name="budgetPeriods" property="underRecoveryAmount" 
                                                                     formatType="currencyFormat"/>--%>
                                        </td>                                            
                                        <td class="copy" align="right">
                                            <%
                                                costSharingAmount = numberFormat.format(dynaFormBean.get("costSharingAmount"));
                                                totalCostSharingAmount = "javascript: setAllTotalCost('"+index+"','strCostSharingAmount');";
                                            %>
                                            <html:text name="dynaFormBean" size="14" maxlength="14" property="strCostSharingAmount" style="text-align: right;width:108px;" indexed="true" styleClass="cltextbox" value="<%=costSharingAmount%>" disabled="<%=disabled%>" onchange='<%=totalCostSharingAmount%>'/>                                             
                                            <%--<coeusUtils:formatString name="budgetPeriods" property="costSharingAmount" 
                                                                     formatType="currencyFormat"/>--%>
                                        </td>
                                        <td class="copy" align="right">
                                            <%
                                                totalCost = numberFormat.format(dynaFormBean.get("totalCost")); 
                                                setStatus = "javascript: setStatus('"+index+"',this);";
                                                allTotalCost = "javascript: setAllTotalCost('"+index+"','strTotalCost');";
                                            %>
                                            <html:text name="dynaFormBean" size="14" maxlength="14" property="strTotalCost" style="text-align: right;width:108px;" indexed="true" styleClass="cltextbox" value="<%=totalCost%>" disabled="true" onchange='<%=allTotalCost%>' onmouseover="<%=setStatus%>"/>                                             
                                            <%--<coeusUtils:formatString name="budgetPeriods" property="totalCost" 
                                                                     formatType="currencyFormat"/>--%>
                                        </td>
                                        </tr>
                                        <%                                        
                                        index++;
                                        %>   
                                        <script>
                                            elementLength = <%=index%>
                                        </script>
                                </logic:iterate>
                            </logic:present>
                            <!--Modified COEUSQA-1500 CoeusLite - Budget Summary - Allow end user to manually insert dollar amounts-End-->
                            <%--<html:hidden property="unitNumber"/>
                            <html:hidden property="activityTypeCode"/>--%>
                            
                            <tr><td>&nbsp;</td></tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td class='savebutton'>&nbsp;&nbsp;
                        <html:submit property="Save" value="Save" styleClass="clsavebutton" disabled="<%=readOnly%>"/>
                        <br><br>
                    </td>
                </tr>
                <tr>
                    <td align="left" valign="top">&nbsp;</td>
                </tr>
            </table>
        </html:form>
    <script>
          DATA_CHANGED = 'false';
          if(errValue && !errLock){
                DATA_CHANGED = 'true';
          }
          LINK = "<%=request.getContextPath()%>/budgetSummary.do";
          FORM_LINK = document.budgetSummaryDynaList;
          PAGE_NAME = "<bean:message bundle="budget" key="summaryHeader.BudgetSummary"/>";
          function dataChanged(){
                DATA_CHANGED = 'true';
          }
          linkForward(errValue);
    </script>
    <script>
      var help = '<bean:message bundle="budget" key="helpTextBudget.Summary"/>';
      help = trim(help);
      if(help.length == 0) {
            document.getElementById('helpText').style.display = 'none';
      }
      function trim(s) {
            return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
      }  
    </script>       
    </body>
</html:html>
