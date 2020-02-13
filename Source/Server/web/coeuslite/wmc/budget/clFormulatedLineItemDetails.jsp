<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="org.apache.struts.validator.DynaValidatorForm,java.util.List,edu.mit.coeuslite.utils.CoeusDynaBeansList"%>
<%@ page import="edu.mit.coeus.unit.bean.UnitFormulatedCostBean,edu.mit.coeus.budget.bean.BudgetFormulatedCostDetailsBean,edu.mit.coeus.utils.CoeusVector"%>
<%@ page import="org.apache.struts.action.DynaActionForm,java.lang.Integer"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"  prefix="coeusUtils"%>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<jsp:useBean  id="formulatedDetails" scope="session" class="edu.mit.coeus.utils.CoeusVector"/>
<bean:size id="formulatedDetailstSize" name="formulatedDetails"/>
<jsp:useBean  id="budgetDetailCalAmts" scope="session" class="edu.mit.coeus.utils.CoeusVector"/>
<bean:size id="budgetDetailCalAmtSize" name="budgetDetailCalAmts"/>
<jsp:useBean  id="unitFormulatedCostDetails" scope="session" class="edu.mit.coeus.utils.CoeusVector"/>
<jsp:useBean  id="formulatedTypes" scope="session" class="edu.mit.coeus.utils.CoeusVector"/>


<%   
String mode = (String)session.getAttribute("mode"+session.getId());
String budgetStatusMode = (String)session.getAttribute("budgetStatusMode");
boolean readOnly = false;
if(mode!= null && mode.equals("display")){
    readOnly = true;
}else if(budgetStatusMode != null && budgetStatusMode.equals("complete")){
    readOnly = true;
}
int sessionPeriodNumber = Integer.parseInt((String)session.getAttribute("budgetPeriod"+session.getId()));
%>

<html:html>
    <head>  
        <title>
            <bean:message bundle="budget" key="budgetFormulatedLineItemDetails.formulatedLineItemTitle"/> 
        </title>
        <html:base/>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <link rel=stylesheet href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/ipf.css" type="text/css">
        <link href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css" rel="stylesheet" type="text/css">
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">            
        <script language="javascript" type="text/JavaScript" src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/coeusLiteCalendar.js"></script>
        <script>
            
        function calculate_budget_later(){
            if(validateForm()){
                var  value = validate();
                if(value == ""){
                    document.formulatedLineItemDetails.action = "<%=request.getContextPath()%>/applyToFormulatedLaterPeriods.do";
                    document.formulatedLineItemDetails.submit();
                }else{
                    alert(value);
                    document.formulatedLineItemDetails.action = "<%=request.getContextPath()%>/applyToFormulatedLaterPeriods.do";
                    document.formulatedLineItemDetails.submit();
                }
                // Hide the code in first div tag
                document.getElementById('lineItemDiv').style.display = 'none';
                // Display code in second div tag
                document.getElementById('saveDiv').style.display = 'block';
            }
        }
        
        function calculate_budget_current(){
            if(validateForm()){
                document.formulatedLineItemDetails.action = "<%=request.getContextPath()%>/applyToFormulatedCurrentPeriods.do";
                document.formulatedLineItemDetails.submit();
                // Hide the code in first div tag
                document.getElementById('lineItemDiv').style.display = 'none';
                // Display code in second div tag
                document.getElementById('saveDiv').style.display = 'block';
            }
        }
        
        function close_action(){
            var page = "<%=session.getAttribute("pageConstantValue")%>";
            var budgetPeriod = "<%=sessionPeriodNumber%>";
            parent.opener.location="<%=request.getContextPath()%>/getBudgetEquipment.do?Period="+budgetPeriod+"&PAGE="+page;
            window.close();
        }
        
        function validate(){
            var value='';
            var lastPeriod = "<%=session.getAttribute("lastPeriod")%>";
            var firstPeriod = "<%=session.getAttribute("firstPeriod")%>";
            if(firstPeriod == "true"){
                value = "This operation can be performed only after generating all the budget periods.";
                return value;
            }else if(lastPeriod == "true"){
                value = "This is the last period of the budget. Cannot perform this operation on last period of the budget.";
                return  value;
            }else{
                return "";
            }
        }
        
        function validateAndSetCalculateExpenses(fieldName){
            var field;
           
            if(fieldName == 'Unit Cost'){
                field = document.formulatedLineItemDetails.unitCost;
            }else if(fieldName == 'Count'){
                field = document.formulatedLineItemDetails.count;
            }else if(fieldName == 'Frequency'){
                field = document.formulatedLineItemDetails.frequency;
            }
            var fieldValue = field.value;
            if(fieldName == 'Unit Cost'){
                if(fieldValue == "" && fieldValue.length == 0){
                    fieldValue = '0.00';
                }
                
                var costLength = fieldValue.length;
                for(var i=0;i<costLength;i++){
                    fieldValue = fieldValue.replace(",","");
                    field.value = fieldValue.replace("$","");
                    
                }
                if(isNaN(field.value)){
                    alert("Please Enter a "+fieldName);
                    field.value = '0.00';
                    setTotalfieldValue();
                    field.focus();
                    return;
                }else{
                    field.value = roundNumber(field.value);
                    field.value = checkForDotOK(field.value);
                    var fieldSplit = field.value.split(".");
                    var fieldLength = fieldSplit[0].length;
                    if(fieldLength > 12){
                        alert(fieldName+" should not exceed 9,999,999,999.00");
                        field.value = '$0.00'
                        return;
                    }
                    setTotalfieldValue();
                }
                
            }else{
                if(isNaN(fieldValue)){
                    alert("Please Enter a "+fieldName);
                    field.value = '0'
                            return;
                }else{
                    var fieldLength = fieldValue.length;
                    if(fieldLength > 5){
                        alert(fieldName+" value length exceeds the limit");
                        field.value = '0'
                                return;
                    }
                    setTotalfieldValue();
                }
            }
        }
        
        function setTotalfieldValue(){
            var unitCost = document.formulatedLineItemDetails.unitCost.value;
            var count = document.formulatedLineItemDetails.count.value;
            var frequency = document.formulatedLineItemDetails.frequency.value;
            var calculatedExpenses;
            var unitCostLength = unitCost.length;
            for(var i=0;i<unitCostLength;i++){
                unitCost = unitCost.replace(",","");
                unitCost = unitCost.replace("$","");
            }
            if(unitCost == 0.00 || count == 0 || frequency == 0){
                calculatedExpenses = '0.00';
            }else{
                calculatedExpenses = parseFloat(unitCost) * parseInt(count) * parseInt(frequency);
                calculatedExpenses = roundNumber(calculatedExpenses);
                calculatedExpenses = checkForDotOK(calculatedExpenses);
            }
            
            var fieldSplit = calculatedExpenses.split(".");
            var fieldLength = fieldSplit[0].length;
            if(fieldLength > 12){
                alert('<bean:message bundle="budget" key="budgetFormulatedLineItemDetails.warningCalculatedExpensesLength"/>');
            }
            document.formulatedLineItemDetails.unitCost.value = '$'+unitCost;
            document.formulatedLineItemDetails.previousUnitCost.value = document.formulatedLineItemDetails.unitCost.value;
            document.formulatedLineItemDetails.count.value = count;
            document.formulatedLineItemDetails.frequency.value = frequency;
            document.formulatedLineItemDetails.calculatedExpenses.value = '$'+calculatedExpenses;
        }
        
        function roundNumber(cost) {
            var costValue = Math.round(cost*Math.pow(10,2))/Math.pow(10,2);
            return costValue;
        }
        
        function addFormulatedCost(){
            document.formulatedLineItemDetails.acType.value = 'I';
            showHide('A');
        }
        
        function showHide(toggleId) {
            if(toggleId == 'A'){
                
                document.getElementById('addFormulatedDetailsLink').style.display = "none";
                document.getElementById('editFormulatedDetails').style.display = "";
            }else if(toggleId == 'C'){
                document.getElementById('addFormulatedDetailsLink').style.display = "";
                document.getElementById('editFormulatedDetails').style.display = "none";
            }else if(toggleId == 'M'){
                document.getElementById('addFormulatedDetailsLink').style.display = "none";
                document.getElementById('editFormulatedDetails').style.display = "";
            }
        }
        
        function intOnly(formFieldName) {
            if(formFieldName.value.length>0) {
                formFieldName.value = formFieldName.value.replace(/[^\d.]+/g, '');
            }
        }
        
        function saveFormData(){
            if(validateForm()){
                document.formulatedLineItemDetails.action = "<%=request.getContextPath()%>/saveFormulatedLineItemDetail.do";
                document.formulatedLineItemDetails.submit();
            }
        }
        
        function deleteFormulatedCost(formulatedNumber){
            var msg = '<bean:message bundle="budget" key="budgetFormulatedLineItemDetails.deleteFormulatedCost" />';
            if (confirm(msg)==true){
                document.formulatedLineItemDetails.action = "<%=request.getContextPath()%>/deleteFormulatedLineItemDetail.do?&formulatedNumber="+formulatedNumber;
                document.formulatedLineItemDetails.submit();
            }
            
        }
        function modifyFormulatedCost(formulatedNumber,formulatedCode,unitCost,count,frequency,calculatedExpenses,awFormulatedCode){
            document.formulatedLineItemDetails.formulatedNumber.value = formulatedNumber;
            document.formulatedLineItemDetails.formulatedCode.value = formulatedCode;
            document.formulatedLineItemDetails.awFormulatedCode.value = awFormulatedCode;
            document.formulatedLineItemDetails.unitCost.value = unitCost;
            document.formulatedLineItemDetails.count.value = count;
            document.formulatedLineItemDetails.frequency.value = frequency;
            document.formulatedLineItemDetails.calculatedExpenses.value = calculatedExpenses;
            document.formulatedLineItemDetails.acType.value = 'U';
            showHide('M');
        }
        
        
        function validateForm(){
            var fieldSplit = document.formulatedLineItemDetails.calculatedExpenses.value.split(".");
            var fieldLength = fieldSplit[0].length;
            if(fieldLength > 12){
                alert('<bean:message bundle="budget" key="budgetFormulatedLineItemDetails.warningCalculatedExpensesLength"/>');
                return false;
            }
 
            var formulatedCode = document.formulatedLineItemDetails.formulatedCode.value;
            if(document.getElementById('editFormulatedDetails').style.display != 'none'){
                if(formulatedCode == 0 ){
                    alert('<bean:message bundle="budget" key="budgetFormulatedLineItemDetails.selectFormulatedType"/>');
                    return false;
                }
            }
            return true;
        }
        
        function cancelFormultedCost(){
            showHide('C');
            document.formulatedLineItemDetails.previousSelectedFormulatedCode.value  = '0';
            document.formulatedLineItemDetails.previousUnitCost.value = '$0.00';
            document.formulatedLineItemDetails.formulatedCode.value  = '0';
            document.formulatedLineItemDetails.unitCost.value = '$0.00';
            document.formulatedLineItemDetails.count.value = '0';
            document.formulatedLineItemDetails.frequency.value = '0';
            document.formulatedLineItemDetails.calculatedExpenses.value = '$0.00';
            document.formulatedLineItemDetails.acType.value = '';
            document.formulatedLineItemDetails.previousSelectedFormulatedCode.value = '0';
        }
        
        function loadUnitCost(sel){
            var formulatedCode = sel.options[sel.selectedIndex].value;
            if(!checkFormTypeCostAlreadyAdded(formulatedCode,sel)){
                var unitCost;
                if(formulatedCode > 0 ){
                    <% if(unitFormulatedCostDetails != null && !unitFormulatedCostDetails.isEmpty()){
                              for(Object unitFormulatedDetails : unitFormulatedCostDetails){
                                    UnitFormulatedCostBean unitFormulatedCostBean = (UnitFormulatedCostBean)unitFormulatedDetails;
                                    %>
                                if(formulatedCode == <%=unitFormulatedCostBean.getFormulatedCode()%>){
                                    unitCost = <%=unitFormulatedCostBean.getUnitCost()%>;
                                    unitCost = checkForDotOK(unitCost);
                                    document.formulatedLineItemDetails.unitCost.value = '$'+unitCost;
                                    validateAndSetCalculateExpenses('Unit Cost');
                                }
                            <%}
                    }%>
                }
                document.formulatedLineItemDetails.previousSelectedFormulatedCode.value = formulatedCode;
            }
        }
        
        function checkFormTypeCostAlreadyAdded(selectedFormulatedCode,sel){
                    <%if(formulatedDetails != null && !formulatedDetails.isEmpty()){
                        for(Object formulatedCostDetails : formulatedDetails){
                            DynaActionForm budgetFormulatedCostDetailsForm = (DynaActionForm)formulatedCostDetails;
                            int formulatedCode = ((Integer)budgetFormulatedCostDetailsForm.get("formulatedCode")).intValue();%>
                            
                            if(selectedFormulatedCode == <%=formulatedCode%> && document.formulatedLineItemDetails.awFormulatedCode.value != selectedFormulatedCode) {
                                alert("A formulated cost of type '"+sel.options[sel.selectedIndex].text+"' is already exists.");
                                document.formulatedLineItemDetails.formulatedCode.value = document.formulatedLineItemDetails.previousSelectedFormulatedCode.value;
                                document.formulatedLineItemDetails.unitCost.value = document.formulatedLineItemDetails.previousUnitCost.value;
                                validateAndSetCalculateExpenses('Unit Cost');
                                return true;
                            }
                    <%}
                    }%>
        }
        
        function checkForDotOK(cost){
            var is_dot_ok = cost.toString().indexOf(".");
            if(is_dot_ok == -1){
                cost = cost+'.00';
            }
            if(cost.toString().length - is_dot_ok == 2){
                cost = cost+'0';
            }
            return cost;
        }
      
                                    </script>
    </head>
    <body>
        <html:form action="/getFormulatedLineItemDetails.do"  method="post"> 
            <logic:equal name="formulatedLineItemDetails" property="popUp" value="close"> 
                <script>
              var page = "<%=session.getAttribute("pageConstantValue")%>";
             var budgetPeriod = "<%=sessionPeriodNumber%>";
            parent.opener.location="<%=request.getContextPath()%>/getBudgetEquipment.do?Period="+budgetPeriod+"&PAGE="+page;
            self.close();
                </script>
            </logic:equal>  
            <div id='lineItemDiv'>
                <table width="100%" height="100%"  border="1" cellpadding="0" cellspacing="0" class="table">       
                    <tr>
                        <td  class='table' align="left">
                            <b>
                                &nbsp;<bean:message bundle="budget" key="budgetFormulatedLineItemDetails.formulatedLineItemDetailsHeader"/>
                            </b>
                        </td>
                    </tr>
                    <tr>
                        <td class='table' nowrap>
                            <b>
                                <font color="red">
                                    <html:errors header=" " footer = " "/>
                                    <logic:messagesPresent message = "true">                                       
                                        <html:messages id="message" message="true" property="budget_common_exceptionCode.1011" bundle="budget">                
                                            <li><bean:write name = "message"/></li>
                                        </html:messages>
                                    </logic:messagesPresent>                   
                                </font>
                            </b> 
                        </td>
                    </tr>
                    <tr>
                        <td  class="tabtable">
                            <table align="left" width="100%"  border="0"  cellpadding="0" class='tabtable'>
                                <tr>
                                    <%if(!readOnly){%>
                                        <td width="20%" height='25'>
                                            <div id="addFormulatedDetailsLink">   
                                                
                                                <table width="100%"  border="0"  cellpadding="0" >
                                                    <tr>
                                                        <td>
                                                            <a href="javascript:addFormulatedCost();"><u> <bean:message bundle="budget" key="budgetFormulatedLineItemDetails.addFormulatedCostLink"/></u></a>
                                                        </td>
                                                    </tr>
                                                </table>
                                                
                                            </div>
                                        </td>

                                        <td width="30%" height='25' align="center" class="copybold" nowrap valign=middle>
                                            <html:link href="javascript:calculate_budget_current()"> 
                                                <u><bean:message bundle="budget" key="budgetFormulatedLineItemDetails.saveAndApplyToCurrentPeriod"/></u>
                                            </html:link>
                                        </td>
                                        <td width="35%" height='25' align="left" class="copybold" valign=middle>
                                            <html:link href="javascript:calculate_budget_later();">
                                                <u><bean:message bundle="budget" key="budgetFormulatedLineItemDetails.saveAndApplyToCurrentAndLaterPeriods"/></u> 
                                            </html:link>
                                        </td>
                                    <%}else{%>
                                        <td width="35%" height='25' align="center" class="copybold" valign=middle>
                                            <bean:message bundle="budget" key="budgetFormulatedLineItemDetails.saveAndApplyToCurrentPeriod"/> 
                                        </td>
                                        <td width="35%" align="left" class="copybold" valign=middle>
                                            <bean:message bundle="budget" key="budgetFormulatedLineItemDetails.saveAndApplyToCurrentAndLaterPeriods"/>
                                        </td>
                                    <%}%>
                                    <td width="10%" height='25' align="center" class="copybold" valign=middle>
                                        <html:link href="javascript:close_action();">
                                            <u><bean:message bundle="budget" key="budgetFormulatedLineItemDetails.close"/> </u>
                                        </html:link>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <%if(!readOnly){%>
                    <tr>
                        <td class="tabtable" colspan="6">
                            <div id="editFormulatedDetails">   
                                <table width="100%"  border="0" cellpadding="0" class="tabtable" cellspacing='3'>
                                    <tr>                                                
                                        <td nowrap class="copybold"  width="10%" align="right">
                                            <bean:message bundle="budget" key="budgetFormulatedLineItemDetails.formulatedType"/>:&nbsp;
                                        </td>    
                                        <td class="copysmall" width="20%" align="left">
                                            <html:select property="formulatedCode" styleClass="textbox-long" onchange="loadUnitCost(this)">
                                                <html:option value="0"> <bean:message bundle="proposal" key="generalInfoProposal.pleaseSelect"/> </html:option>
                                                <html:options collection="formulatedTypes" property="code" labelProperty="description" />>
                                            </html:select>
                                        </td>
                                    </tr> 
                                    <tr>
                                        <td nowrap class="copybold"  width="10%" align="right">
                                            <bean:message bundle="budget" key="budgetFormulatedLineItemDetails.unitCost"/>:&nbsp;
                                        </td>    
                                        <td align="left" class="copy">
                                            <%String getUnitCost = "javascript:validateAndSetCalculateExpenses('Unit Cost');";
                                            
                                            %>
                                            <html:text property="unitCost"  maxlength="16"  size="15" style="text-align: right" styleClass="cltextbox"  onchange="<%=getUnitCost%>" disabled="<%=readOnly%>"/>
                                        </td>
                                        <td nowrap class="copybold"  width="10%" align="right">
                                            <bean:message bundle="budget" key="budgetFormulatedLineItemDetails.count"/>:&nbsp;
                                        </td>    
                                        <td align="left" class="copy">
                                            <%String getCount = "javascript:validateAndSetCalculateExpenses('Count');";%>
                                            <html:text property="count" maxlength="5" size="15" styleClass="cltextbox" style="text-align: right" disabled="<%=readOnly%>" onchange="<%=getCount%>"/>
                                        </td>
                                    </tr>
                                    
                                    <tr>
                                        <td nowrap class="copybold"  width="10%" align="right">
                                            <bean:message bundle="budget" key="budgetFormulatedLineItemDetails.frequency"/>:&nbsp;
                                        </td>  
                                        <td align="left" class="copy">
                                            <%String getFrequency = "javascript:validateAndSetCalculateExpenses('Frequency');";%>
                                            <html:text property="frequency" maxlength="5" size="15" styleClass="cltextbox" style="text-align: right" disabled="<%=readOnly%>" onchange="<%=getFrequency%>"/>
                                        </td>
                                        <td nowrap class="copybold"  width="10%" align="right">
                                            <bean:message bundle="budget" key="budgetFormulatedLineItemDetails.calculatedExpenses"/>:&nbsp;
                                        </td>                                          
                                        <td align="left" class="copy">
                                            <html:text property="calculatedExpenses" size="15" styleClass="cltextbox" style="text-align: right" readonly="true" />
                                        </td>
                                    </tr>    
                                    
                                    <html:hidden property="acType"/>
                                    <html:hidden property="formulatedNumber"/>
                                    <html:hidden property="previousSelectedFormulatedCode"/>
                                    <html:hidden property="previousUnitCost"/>
                                    <html:hidden property="awFormulatedCode"/>
                                    
                                    <tr  class="table">
                                        <td colspan="4" width="100%" class="table">
                                            <html:button property="Save" value="Save"  styleClass="clsavebutton" onclick="saveFormData()"/>
                                            <html:button property="Save" styleClass="clsavebutton" value="Cancel" onclick="cancelFormultedCost()">
                                            </html:button>
                                        </td>
                                    </tr>
                                </table>      
                            </div>
                        </td>
                    </tr>
                    <%}%>
                    <tr>
                        <td class="tabtable" align="center" >
                            <table width="100%" border="0" cellpadding="0" class="tabtable">
                                <tr>
                                    <td width="30%" height="20" align="left" class="theader"><bean:message bundle="budget" key="budgetFormulatedLineItemDetails.formulatedType"/></td>
                                    <td width="15%" height="20" align="left" class="theader"><bean:message bundle="budget" key="budgetFormulatedLineItemDetails.unitCost"/></td>
                                    <td width="11%" height="20" align="left" class="theader"><bean:message bundle="budget" key="budgetFormulatedLineItemDetails.count"/></td>
                                    <td width="10%" height="20" align="left" class="theader"><bean:message bundle="budget" key="budgetFormulatedLineItemDetails.frequency"/></td>
                                    <td width="19%" height="20" align="left" class="theader"><bean:message bundle="budget" key="budgetFormulatedLineItemDetails.calculatedExpenses"/></td>
                                    <td width="15%" height="20" align="left" class="theader"></td>
                                </tr>
                                
                                <logic:equal name="formulatedDetailstSize" value="0" >
                                    <tr>
                                        <td>
                                            <table width="100%" align="right" border="0">
                                                <tr>
                                                    <td> &nbsp; </td>
                                                    <td> &nbsp; </td>
                                                    <td width='20%' nowrap align='center' class="copybold">
                                                        <font color='red'>
                                                            <bean:message bundle="budget" key="budgetFormulatedLineItemDetails.formulatedDetailsNotAvailable"/>
                                                        </font>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                </logic:equal>   
                                
                                <logic:present name="formulatedDetails">
                                    <% 
                                    String strBgColorForm = "#DCE5F1";
                                    int formulatedCount = 0;
                                    double calculatedExpensesTotal = 0.0;
                                    %>
                                    <logic:iterate id="formulatedCost" name="formulatedDetails"
                                                   type="org.apache.struts.validator.DynaValidatorForm">
                                        
                                        <% if (formulatedCount%2 == 0){
                                            strBgColorForm = "#D6DCE5";
                                        }else{
                                            strBgColorForm="#DCE5F1";
                                        }%>
                                        <tr  bgcolor="<%=strBgColorForm%>" onmouseover="className='TableItemOn'" onmouseout="className='TableItemOff'">
                                            <td align="left"  nowrap class="copy" height="20">
                                            <bean:write name="formulatedCost" property="formulatedCodeDescription"/></td>
                                            <%int formulatedCode = ((Integer)formulatedCost.get("formulatedCode")).intValue();%>
                                            <td align="left" class="copy">
                                                <% 
                                                Double doubleUnitsCost = new Double((String)formulatedCost.get("unitCost"));
                                                String unitCost = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(doubleUnitsCost); %>
                                                <%=unitCost%>
                                            </td>
                                            <td align="left" class="copy">
                                                <%int count = ((Integer)formulatedCost.get("count")).intValue();%>
                                                <%=count%>
                                            </td>
                                            <td align="left" class="copy">
                                                <%int frequency = ((Integer)formulatedCost.get("frequency")).intValue();%>
                                                <%=frequency%>
                                            </td>
                                            <td align="left" class="copy">
                                                <%
                                                Double calcualtedExpenses = new Double((String)formulatedCost.get("calculatedExpenses"));
                                                calculatedExpensesTotal = calculatedExpensesTotal + calcualtedExpenses.doubleValue();
                                                String calculatedExpense = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(calcualtedExpenses); %>
                                                <%=calculatedExpense%>
                                            </td>
                                            
                                            <td align="left" class="copy">
                                                <%if(!readOnly){
                                                    int formulatedNumber = ((Integer)formulatedCost.get("formulatedNumber")).intValue();
                                                    int awFormulatedCode = ((Integer)formulatedCost.get("awFormulatedCode")).intValue();
                                                    String modifyLink = "javascript:modifyFormulatedCost('"+formulatedNumber+"','"+formulatedCode+"','"+unitCost+"','"+count+"','"+frequency+"','"+calculatedExpense+"','"+awFormulatedCode+"');";%>
                                                    <a href="<%=modifyLink%>">Modify</a>&nbsp;&nbsp;
                                                    <%String removeLink = "javascript:deleteFormulatedCost('"+formulatedNumber+"');";%>
                                                    <a href="<%=removeLink%>">Remove</a>
                                                <%}%>
                                            </td>
                                            
                                        </tr>
                                        <%formulatedCount++;%>
                                    </logic:iterate>
                                    
                                    <tr class='table'>
                                        <td>&nbsp;</td>
                                        <td>&nbsp;</td>
                                        <td >&nbsp;</td>
                                        <td align="right">
                                            <b><bean:message bundle="budget" key="budgetFormulatedLineItemDetails.Total"/>:<b>&nbsp;
                                        </td>
                                        <td colspan="6" align="left">
                                            <%=java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(calculatedExpensesTotal)%>
                                        </td>
                                    </tr>
                                </logic:present>
                                
                                <tr>
                                    <td colspan="6" align="left" valign="top">
                                        <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableheader">
                                            <tr>
                                                <td><bean:message bundle="budget" key="budgetFormulatedLineItemDetails.ratesApplicabletoLineItemHeader"/>  </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan='2' height='20' align="left" class="theader"><bean:message bundle="budget" key="budgetFormulatedLineItemDetails.rateTypes"/></td>
                                    <td align="center" class="theader"><bean:message bundle="budget" key="budgetFormulatedLineItemDetails.apply"/></td>
                                    <td align="left" class="theader"><bean:message bundle="budget" key="budgetFormulatedLineItemDetails.cost"/></td>
                                    <td colspan='2'  align="left" class="theader"><bean:message bundle="budget" key="budgetFormulatedLineItemDetails.costSharing"/></td>
                                </tr>
                                <logic:equal name="budgetDetailCalAmtSize" value="0" >
                                    <tr>
                                        <td>
                                            <table width="100%" align="right" border="0">
                                                <tr>
                                                    <td> &nbsp; </td>
                                                    <td> &nbsp; </td>
                                                    <td width='20%' nowrap align='center' class="copybold"><font color='red'>
                                                        <bean:message bundle="budget" key="budgetLineItemDetails.noCalAmounts"/> </font>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                </logic:equal>   
                                
                                <tr align=left>
                                    <% String strBgColor = "#DCE5F1";
                                    int count = 0;%>
                                    <logic:present name="budgetDetailCalAmts">
                                        <logic:iterate id="calAmts" name="budgetDetailCalAmts"
                                                       type="org.apache.struts.validator.DynaValidatorForm">
                                            
                                             <%if(count%2 == 0){
                                                strBgColor = "#D6DCE5";
                                               }else{
                                                strBgColor="#DCE5F1";
                                               }%>
                                            <tr  bgcolor="<%=strBgColor%>" onmouseover="className='TableItemOn'" onmouseout="className='TableItemOff'">
                                                <td colspan='2' align="left"  nowrap class="copy">
                                                <bean:write name='calAmts' property="rateTypeDescription"/></td>
                                                <td align="center" class="copy">                                                                                              
                                                    <% 
                                                    boolean flagValue = ((Boolean)calAmts.get("applyRateFlag")).booleanValue();
                                                    String value ="";
                                                    String checked ="";
                                                    if(!readOnly){
                                                        if(flagValue){
                                                            value="true";%>
                                                            <input type="checkbox" name="applyRateFlag" value="<%=count%>" checked>
                                                        <%}else{
                                                            value ="false";%>
                                                            <input type="checkbox" name="applyRateFlag"  value="<%=count%>">                                                             
                                                        <%}
                                                    }else{
                                                        if(flagValue){
                                                            value="true";%>
                                                            <input type="checkbox" name="applyRateFlag" disabled value="<%=count%>" checked>
                                                        <%}else{
                                                        value ="false";%>
                                                            <input type="checkbox" name="applyRateFlag" disabled value="<%=count%>">                                                             
                                                    <%}
                                                    }%> 
                                                </td>
                                                <td align="left" class="copy" >
                                                    <% String calculatedCost = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(calAmts.get("calculatedCost")); %>
                                                    <%=calculatedCost%>&nbsp;&nbsp;
                                                </td>
                                                <td align="left" nowrap class="copy" colspan="2">   
                                                    <% String calculatedCostSharing = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(calAmts.get("calculatedCostSharing")); %>                                
                                                    <%=calculatedCostSharing%>
                                                </td>
                                            </tr>
                                            <%count++;%>
                                            <html:hidden property="rateClassCode"/>
                                            <html:hidden property="rateTypeCode"/>
                                            <html:hidden property="updateTimestamp"/>
                                        </logic:iterate>
                                    </logic:present>
                                </tr>
                            </table>
                        </td>
                        
                    </tr>
                    
                    <tr>
                        <td colspan='6' class='tabtable'>
                            <table align="left" width="100%" border="0" cellpadding="0" class='tabtable'>
                                <tr>
                                    <%if(!readOnly){%>
                                        <td width="25%" height='25'>&nbsp;</td>
                                        <td width="30%" height='25' align="center" class="copybold" valign=middle>
                                            <html:link href="javascript:calculate_budget_current()"> 
                                                <u><bean:message bundle="budget" key="budgetLineItemDetailsButton.saveAndApplyToCurrentPeriod"/></u>
                                            </html:link>
                                        </td>
                                        <td width="35%" align="left" class="copybold" valign=middle>
                                            <html:link href="javascript:calculate_budget_later();">
                                                <u><bean:message bundle="budget" key="budgetLineItemDetailsButton.saveAndApplyToCurrentAndLaterPeriods"/></u>
                                            </html:link>
                                        </td>
                                    <%}else{%>
                                        <td width="35%" height='25' align="center" class="copybold" valign=middle>
                                            <bean:message bundle="budget" key="budgetLineItemDetailsButton.saveAndApplyToCurrentPeriod"/> 
                                        </td>
                                        <td width="35%" align="left" class="copybold" valign=middle>
                                            <bean:message bundle="budget" key="budgetLineItemDetailsButton.saveAndApplyToCurrentAndLaterPeriods"/>
                                        </td>
                                    <%}%>
                                    <td width="10%" height='25' align="center" class="copybold" valign=middle>
                                        <html:link href="javascript:close_action();">
                                            <u><bean:message bundle="budget" key="budgetLineItemDetailsButton.close"/> </u>
                                        </html:link>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
        
                </table>
            </div>
            <%if(!readOnly){%>
            <div id='saveDiv' style='display: none;'>
                <table width="100%" height="100%" align="center" border="0" cellpadding="3" cellspacing="0" class="tabtable">
                    <tr>
                        <td align='center' class='copyred'> <bean:message bundle="budget" key="budgetMessages.saving"/>  
                            <br> <bean:message bundle="budget" key="budgetMessages.pleaseWait"/>  
                        </td>
                    </tr>
                </table>
            </div>
            <%}%>
        </html:form>
        <script>
            <%if(!readOnly){%>
                     document.getElementById('editFormulatedDetails').style.display = 'none';
            <%}%>
        </script>
    </body>
</html:html>