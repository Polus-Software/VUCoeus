<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<%@ page import="java.util.Vector,edu.mit.coeuslite.utils.ComboBoxBean, edu.wmc.coeuslite.budget.bean.CategoryBean,
edu.mit.coeus.utils.ObjectCloner,java.util.List,edu.mit.coeuslite.utils.CoeusDynaBeansList, org.apache.struts.validator.DynaValidatorForm,java.text.DecimalFormat"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld"  prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld"  prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"  prefix="coeusUtils"%>
<jsp:useBean  id="BudgetPeriodData"  scope="session" class="java.util.Vector"/>
<jsp:useBean  id="costElementData"  scope="session" class="java.util.Vector"/>
<jsp:useBean  id="FilterBudgetPersonnelData"  scope="request" class="java.util.Vector"/>
<jsp:useBean  id="pageConstantValue" scope="session" class="java.lang.String"/>
<jsp:useBean  id="popUp"  scope="request" class="java.lang.String"/>
<jsp:useBean  id="budgetPersons"  scope="session" class="java.util.Vector"/>
<jsp:useBean  id="projectRoles"  scope="session" class="java.util.Vector"/>
<jsp:useBean  id="periodTypes"  scope="session" class="java.util.Vector"/>
<jsp:useBean id="personnelCEwithoutPersons" scope="session" class="java.util.Vector"/>
<jsp:useBean  id="allPeriodTypes"  scope="session" class="java.util.Vector"/>
<jsp:useBean  id="dynamicPeriodTypes"  scope="session" class="java.util.Vector"/>
<html:html locale="true">
    <head>
        <link href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css" rel="stylesheet" type="text/css">
    </head>
    <script language="javascript" type="text/JavaScript" src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/coeusLiteCalendar.js">
    </script>    

    <title>Personnel Budget</title>
<% boolean valueChanges = false; 
    String mode = (String)session.getAttribute("mode"+session.getId());
   String budgetStatusMode = (String)session.getAttribute("budgetStatusMode");
   Vector vecCEMessages = null;
   if(session.getAttribute("personnel_inactive_CE_messages")!=null){
        vecCEMessages = (Vector) session.getAttribute("personnel_inactive_CE_messages"); 
   }
   boolean readOnly = false;
   if(mode!= null && mode.equals("display")){
       readOnly = true;
   }else if(budgetStatusMode != null && budgetStatusMode.equals("complete")){
        readOnly = true;
   }

   String editImage = request.getContextPath()+"/coeusliteimages/edit.gif";
   String delImage= request.getContextPath()+"/coeusliteimages/delete.gif";
   String spacerImage= request.getContextPath()+"/coeusliteimages/spacer.gif";

   CoeusDynaBeansList coeusLineItemDynaList = (CoeusDynaBeansList)session.getAttribute("budgetPersonnelDynaBean");
   List beanList = coeusLineItemDynaList.getList();

   Integer sessionPeriodNumber = (Integer)request.getAttribute("SelectedBudgetPeriodNumber");
   if(beanList.size() > 0){
   DynaValidatorForm personnelDynaForm = (DynaValidatorForm)beanList.get(0);
   sessionPeriodNumber = (Integer)personnelDynaForm.get("budgetPeriod");
   }
%>
<%String plus = request.getContextPath()+"/coeusliteimages/plus.gif";
  String minus = request.getContextPath()+"/coeusliteimages/minus.gif";%>
    <script language="JavaScript">
        var change = "";
        var errValue = false;
        var errLock = false;
        
        function activateTab(requestPeriod,prevPeriod) {
        var page = "<%=session.getAttribute("pageConstantValue")%>";
             <% boolean dataChangesFlag = false;
                if(request.getAttribute("dataChanges") != null){
                    dataChangesFlag = ((Boolean)request.getAttribute("dataChanges")).booleanValue();
                }%>
        document.budgetPersonnelDynaBean.action = "<%=request.getContextPath()%>/getBudgetPersonnel.do?budgetPeriod="+requestPeriod+"&PAGE="+page+"&OldPeriod="+prevPeriod;
        if(requestPeriod!= prevPeriod)
        {
                    
        if(<%=dataChangesFlag%> || (change == "true")){
        var msg = '<bean:message bundle="budget" key="budgetLabel.saveConfirmation" />';
        if (confirm(msg)==true){
        document.budgetPersonnelDynaBean.action = "<%=request.getContextPath()%>/calculatePersonnelBudget.do?budgetPeriod="+prevPeriod+"&Save=S"+"&requestBudgetPeriod="+requestPeriod;
        document.budgetPersonnelDynaBean.submit();
        // Hide the code in first div tag
        document.getElementById('equipmentFormDiv').style.display = 'none';
        // Display code in second div tag
        document.getElementById('saveDiv').style.display = 'block';       
        }
        }else{
                        
        document.budgetPersonnelDynaBean.submit();
        }
        }
        }
        
        function proposalBudget_Actions(actionName){
        //var budgetPeriod = "<%=request.getAttribute("SelectedBudgetPeriodNumber")%>";
        var budgetPeriod = "<%=sessionPeriodNumber%>";
        if(actionName == "A"){
        document.budgetPersonnelDynaBean.action = "<%=request.getContextPath()%>/AddBudgetPersonnel.do?budgetPeriod="+budgetPeriod;
        document.budgetPersonnelDynaBean.submit();
        }else if(actionName == "C"){
        document.budgetPersonnelDynaBean.action = "<%=request.getContextPath()%>/calculatePersonnelBudget.do?budgetPeriod="+budgetPeriod;
        document.budgetPersonnelDynaBean.submit();
                
        // Hide the code in first div tag
        document.getElementById('equipmentFormDiv').style.display = 'none';
        // Display code in second div tag
        document.getElementById('calculateDiv').style.display = 'block';                 
                
        }else if(actionName == "S"){
        document.budgetPersonnelDynaBean.action = "<%=request.getContextPath()%>/calculatePersonnelBudget.do?budgetPeriod=" + budgetPeriod + "&Save=S";
        document.budgetPersonnelDynaBean.submit();
                
        // Hide the code in first div tag
        document.getElementById('equipmentFormDiv').style.display = 'none';
        // Display code in second div tag
        document.getElementById('saveDiv').style.display = 'block';       
        }
        }
        
        function removeLineItem(link){
        var msg = '<bean:message bundle="budget" key="budgetLabel.deleteConfirmation" />';
        //var budgetPeriod = "<%=request.getAttribute("SelectedBudgetPeriodNumber")%>";
        var budgetPeriod = "<%=sessionPeriodNumber%>";
        if (confirm(msg)==true){
        document.budgetPersonnelDynaBean.action = "<%=request.getContextPath()%>/"+link+"&budgetPeriod=" + budgetPeriod;
        document.budgetPersonnelDynaBean.submit();
        // Hide the code in first div tag
        document.getElementById('equipmentFormDiv').style.display = 'none';
        // Display code in second div tag
        document.getElementById('calculateDiv').style.display = 'block';                 
        }
        }
        function open_line_item(rowIndex){
        dataChanged();
        //var budgetPeriod = "<%=request.getAttribute("SelectedBudgetPeriodNumber")%>";
        var budgetPeriod = "<%=sessionPeriodNumber%>";
        document.budgetPersonnelDynaBean.action = "<%=request.getContextPath()%>/calculatePersonnelBudget.do?budgetPeriod=" + budgetPeriod + "&Save=S&Edit=E&rowIndex=" + rowIndex;
        document.budgetPersonnelDynaBean.submit();
                   
        // Hide the code in first div tag
        document.getElementById('equipmentFormDiv').style.display = 'none';

        // Display code in second div tag
        document.getElementById('messageDiv').style.display = 'block';                
        }
        
        function dataModified(index, type, oldValue){
        dataChanged();
        var val = "";
        if(change != "true"){
        if(type == 'linePE'){ // Percent Effort
        val = document.getElementsByName("list[" + index + "].percentEffort");         
        val = val[0].value;
        }else if(type == 'linePR'){ // Project Role
        val = document.getElementsByName("list[" + index + "].projectRole");         
        val = val[0][val[0].selectedIndex].value; 
        }else if(type == 'lineCE'){ // Cost Element
        val = document.getElementsByName("list[" + index + "].costElement");         
        val = val[0][val[0].selectedIndex].value; 
        }else if(type == 'linePT'){ // Period Type
        val = document.getElementsByName("list[" + index + "].periodType");         
        val = val[0][val[0].selectedIndex].value; 
        }else if(type == 'linePC'){ // Percent Charged
        val = document.getElementsByName("list[" + index + "].percentCharged");         
        val = val[0].value;
        }
        if(val != oldValue){
        change = "true";
        }
        }
            
        }
        function formatField(val){
        if(isNaN(val)){
        val="0.0";
        return val;
        }
        var ch = val.indexOf(".",0);
        if(ch == -1){
        return val;
        }
        var value = val.substring(0,ch+3);
        return value;
            
        }
       
        function openLocationWin(lineItemNumber, personNo,value,version) {
            if(value == 'N'){
                var budgetPeriod = "<%=sessionPeriodNumber%>";
                var lineItemNo = "<%=request.getAttribute("lineItemNumber")%>";
                var personNumber = "<%=request.getAttribute("personNumber")%>";
                var w = 760;
                var h = 400;            
                 if( (lineItemNo == 'null' || lineItemNo == undefined) && (personNumber ='null' || personNumber == undefined )) {
                    lineItemNo = lineItemNumber;
                    personNumber = personNo;
                }

                if (window.screen) {
                leftPos = Math.floor(((window.screen.width-w) / 2));
                topPos = Math.floor(((window.screen.height- h) / 2));
                }
                var loc = '<bean:write name='ctxtPath'/>'+"/getPersonnelLineItemDetails.do?budgetPeriod="+budgetPeriod+"&lineItemNumber="+lineItemNo+"&personNumber="+personNumber;
                var newWin 
                = window.open(loc,"formName",'dependent=1,toolbar=0,location=0,directories=0,status=0, menubar=0,scrollbars=1,resizable=0,width=' + w + ',height=' + h+ ',left='+ leftPos + ',top=' + topPos);
                newWin.window.focus();
            }
            if(value == 'Y'){
            
                var w = 760;
                var h = 400;            
                if (window.screen) {
                leftPos = Math.floor(((window.screen.width-w) / 2));
                topPos = Math.floor(((window.screen.height- h) / 2));
                }
                var loc = '<bean:write name='ctxtPath'/>'+"/nonDetailsForPersonnel.do?noPersonBudgetPeriod="+lineItemNumber+"&noPersonLineItemNum="+personNo+"&noPersonVersion="+version;
                var newWin 
                = window.open(loc,"formName",'dependent=1,toolbar=0,location=0,directories=0,status=0, menubar=0,scrollbars=1,resizable=0,width=' + w + ',height=' + h+ ',left='+ leftPos + ',top=' + topPos);
                newWin.window.focus();
            }
        }
        
        function budgetDetailsShowHide(value) {
            if(value==3) {
                document.getElementById('budgetPeriodDetails').style.display = 'block';
                document.getElementById('budgetPeriodHideImage').style.display = 'block';            
                document.getElementById('budgetPeriodShowImage').style.display = 'none';
            } else if(value==4) {
                document.getElementById('budgetPeriodDetails').style.display = 'none';
                document.getElementById('budgetPeriodHideImage').style.display = 'none';            
                document.getElementById('budgetPeriodShowImage').style.display = 'block';
            } 
     }
    </script>
    <html:base/>

    <body>


        <html:form action="/getBudgetPersonnel.do" method="POST">

            <!-- New Templates for BudgetEquipment Page   -->
            <div id='equipmentFormDiv'>
                <%-- Starts for opening the child window --%>
                <logic:present name ="popUp" scope="request">
                    <logic:equal scope="request" name="popUp" value="fromLocation">
                        <script>                            
                            openLocationWin('0','0','N','1'); 
                        </script>
                    </logic:equal>        
                </logic:present>
                <%-- End of opening the child window --%>
                <table width="100%" height="100%"  border="0" cellpadding="0" cellspacing="0">
                <tr>
                    <td>
                        <div id="helpText" class='helptext'>            
                            <bean:message bundle="budget" key="helpTextBudget.Personnel"/>  
                        </div> 
                    </td>
                </tr>
                <tr>
                    <td style='core'>
                        <!-- Tab START -->
                        <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
                            <tr>
                            <%
                                int budgetPeriodnumber =0;
                                if(sessionPeriodNumber != null){
                                    budgetPeriodnumber = ((Integer)sessionPeriodNumber).intValue();
                            %>
                                <logic:present name ="BudgetPeriodData" scope = "session">                
                                    <logic:iterate id = "budgetLineItemTab" name ="BudgetPeriodData" 
                                        type = "org.apache.commons.beanutils.DynaBean">
                                    <%
                                        int beanPeriodNumber = ((Integer)budgetLineItemTab.get("budgetPeriod")).intValue();
                                        if(budgetPeriodnumber == beanPeriodNumber){
                                    %>
                                        <td width="57" height="27" align="center" valign="middle" STYLE="background-image:url('<bean:write name='ctxtPath'/>/coeusliteimages/tab_bg_hl.gif');border:0;" class="tabbghl"  
                                        onClick="activateTab(<%=budgetLineItemTab.get("budgetPeriod")%>,<%=sessionPeriodNumber%>)"><bean:message bundle="budget" key="budgetLabel.period" /> <%=budgetLineItemTab.get("budgetPeriod")%> </td>
                                     <%
                                        }else{
                                    %>
                                        <td width="57" height="27"  align="center" valign="middle" STYLE="background-image:url('<bean:write name='ctxtPath'/>/coeusliteimages/tab_bg.gif');border:0;cursor: pointer;" class="tabbg" 
                                        onClick="activateTab(<%=budgetLineItemTab.get("budgetPeriod")%>,<%=sessionPeriodNumber%>)"><bean:message bundle="budget" key="budgetLabel.period" /><%--Period--%> <%=budgetLineItemTab.get("budgetPeriod")%> </td>
                                     <%
                                        }
                                     %> 
                                    </logic:iterate>
                                </logic:present>   
                                <td  align="left" valign="top">&nbsp;</td>
                            </tr>                
                        </table>
                        <!-- Tab END -->
                    </td>
                </tr>
                
                <tr>
                    <td>
                    <table width="100%" border="0" cellspacing="4" cellpadding="0" class="table">
                        
                        <tr>
                            <td style='core'>
                                <!-- Period Summary - START -->
                                <table width="100%" height="45%"  border="0" cellpadding="0" cellspacing="2" class="tabtable">
                                    <tr>
                                        
                                        <logic:present name ="BudgetPeriodData" scope = "session">       
                                            <logic:iterate id="data" name="BudgetPeriodData" type = "org.apache.commons.beanutils.DynaBean">
                                                <%                     
                                                int beanPeriod = ((Integer)data.get("budgetPeriod")).intValue();
                                                if(budgetPeriodnumber == beanPeriod ){
                                                %>
                                                <tr>  
                                                    <td align="right" nowrap>
                                                        <b> <bean:message bundle="budget" key="budgetLabel.directCost" /> :</b>
                                                    </td>
                                                    
                                                    <td nowrap>
                                                        <coeusUtils:formatString name="data" property="totalDirectCost" formatType="currencyFormat"/>
                                                        </td>
                                                    
                                                    <td align="right" nowrap>
                                                        <b> <bean:message bundle="budget" key="budgetLabel.inDirectCost" /> :</b>
                                                    </td>
                                                    
                                                    <td nowrap>
                                                        <coeusUtils:formatString name="data" property="totalIndirectCost" formatType="currencyFormat"/>
                                                        </td>
                                                    
                                                    <td align="right" nowrap>
                                                        <b> <bean:message bundle="budget" key="budgetLabel.totalCost" /> :</b>
                                                    </td>
                                                    
                                                    <td nowrap>
                                                        <coeusUtils:formatString name="data" property="totalCost" formatType="currencyFormat"/>
                                                        </td>
                                                   <td align="right" nowrap><b> <bean:message bundle="budget" key="budgetLabel.NoOfPeriodMonths" /> :</b>
                                                   </td>
                                                          
                                                   <td align="20%" nowrap>
                                                              <coeusUtils:formatOutput name="data" property="noOfPeriodMonths" />
                                                    </td> 
                                                </tr>
                                                <tr>
                                                    <td align="right" nowrap><b> <bean:message bundle="budget" key="budgetLabel.underRecovery" /> :</b>
                                                    </td>
                                                    
                                                    <td nowrap>
                                                        <coeusUtils:formatString name='data' property='underRecoveryAmount' formatType='currencyFormat'/>
                                                        </td>
                                                    
                                                    <td align="right" nowrap><b> <bean:message bundle="budget" key="budgetLabel.costShare" /> :</b>
                                                    </td>
                                                    
                                                    <td nowrap>
                                                        <coeusUtils:formatString name="data" property="costSharingAmount" formatType="currencyFormat"/>
                                                        </td>
                                                    
                                                    <td align="right" nowrap><b> <bean:message bundle="budget" key="budgetLabel.period" /> :</b>
                                                    </td>
                                                    
                                                    <td nowrap>
                                                        <coeusUtils:formatDate name="data" property="startDate" />
                                                        -
                                                        <coeusUtils:formatDate name="data" property="endDate" />
                                                    </td>
                                                    <td> </td>
                                                    <td> </td>
                                                </tr>
                                                <%}%>
                                                
                                            </logic:iterate>
                                        </logic:present>
                                        <%}%>
                                    </tr>
                                    
                                </table>
                                <!-- Period Summary - END -->
                            </td>
                        </tr> 
                        
                        <tr>
                            <td style='core'>
                                <!-- Personnel Budget Details - START -->
                                <table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0" class="table">
                                    <tr>
                                        <td align="left" valign="top">
                                            <table width="100%" height="20"  border="0" cellpadding="0" cellspacing="0" class="tableheader">
                                                <tr>
                                                    <td  align="left">
                                                        <bean:message bundle="budget" key="personnel.label"/>
                                                    </td>      
                                                    
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class='copy' align="left" valign="top">
                                            <font color="red">                                                
                                                <logic:messagesPresent> 
                                                    <script>errValue = true;</script>
                                                    <html:errors header="" footer = ""/>
                                                </logic:messagesPresent>
                                                
                                                <logic:messagesPresent message="true">
                                                    
                                                    <script>errValue = true;</script>
                                                    <html:messages id="message" message="true" property="budgetPersonnel_exceptionCode.1000" bundle="budget">                
                                                        <li><bean:write name = "message"/></li>
                                                    </html:messages>
                                                    <!-- If lock is deleted then show this message --> 
                                                    <html:messages id="message" message="true" property="errMsg">
                                                        <script>errLock = true;</script>
                                                        <li><bean:write name = "message"/></li>
                                                    </html:messages>        
                                                    
                                                    <html:messages id="message" message="true" property= "budgetPersonnel_exceptionCode.StartDateNotPerior" bundle="budget">
                                                        <li><bean:write name = "message" /></li>
                                                    </html:messages>  
                                                    <html:messages id="message" message="true" property= "budgetPersonnel_exceptionCode.EndDateNotLater" bundle="budget">
                                                        <li><bean:write name = "message" /></li>
                                                    </html:messages>  
                                                    
                                                    <html:messages id="message" message="true" property= "budgetPersonnel_exceptionCode.StartDateNotLaterEndDate" bundle="budget">
                                                        <li><bean:write name = "message" /></li>
                                                    </html:messages>  
                                                    <html:messages id="message" message="true" property= "budgetPersonnel_exceptionCode.EndDateNotBeforeEndDate" bundle="budget">
                                                        <li><bean:write name = "message" /></li>
                                                    </html:messages>  
                                                    
                                                    <html:messages id="message" message="true" property= "budgetPersonnel_exceptionCode.inValidPercentCharged" bundle="budget">
                                                        <li><bean:write name = "message" /></li>
                                                    </html:messages>  
                                                    <html:messages id="message" message="true" property= "budgetPersonnel_exceptionCode.inValidPercentEffort" bundle="budget">
                                                        <li><bean:write name = "message" /></li>
                                                    </html:messages>                                                                  
                                                </logic:messagesPresent>
                                                <!--Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start -->
                                                <%if(vecCEMessages != null && vecCEMessages.size() >0) {%>                                                
                                                <%for(int index = 0; index < vecCEMessages.size(); index++){%>
                                                <tr>
                                                    <td align = "left">
                                                        <font color="red">
                                                            <li> <%=vecCEMessages.elementAt(index)%> </li>
                                                        </font>
                                                    </td>
                                                    <td>
                                                        <br>
                                                        &nbsp;
                                                        <br>
                                                    </td>
                                                </tr>                                                
                                                <%}}
                                                session.setAttribute("personnel_inactive_CE_messages",null);
                                                %>
                                                <!--Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - end-->
                                            </font>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            
                                            <!--start Iteration here-->
                                            <table width="100%" align="center" border="0" cellpadding="0" cellspacing="4">
                                                <%double totalFunds = 0;%>
                                                <logic:present name ="budgetPersonnelDynaBean" scope = "session">                                                               
                                                    <%
                                                    String strBgColor = "#DCE5F1";
                                                    int count=0;
                                                    %>
                                                    
                                                    <% int lineItemIndex = 0; %>
                                                    <logic:iterate id="list" name="budgetPersonnelDynaBean" property="list" type="org.apache.struts.action.DynaActionForm" indexId="ctr">
                                                        <%if (count%2 == 0) {
                                                        strBgColor = "#D6DCE5"; 
                                                        }
                                                        else { 
                                                        strBgColor="#DCE5F1"; 
                                                        }  %> 
                                                        <tr>
                                                            <td bgcolor='<%=strBgColor%>'>
                                                                <table width="100%" border="0" align="center" border="0" cellpadding="0" cellspacing="2" class="tabtable">
                                                                      
                                                                    <%
                                                                    totalFunds = totalFunds + ((Double)list.get("fundsRequested")).doubleValue();
                                                                    %>	
                                                                    <%--
                                                                    <tr style='height:15px;'>
                                                                        <td nowrap class="copybold">
                                                                            <bean:message bundle="budget" key="personnel.fullName"/>
                                                                        </td>
                                                                        <td nowrap class="copybold">
                                                                            &nbsp; <bean:message bundle="budget" key="personnel.costElement"/>
                                                                        </td>
                                                                        <td nowrap class="copybold">
                                                                            <bean:message bundle="budget" key="personnel.periodType"/>
                                                                        </td>
                                                                        <td class="copybold">
                                                                            <bean:message bundle="budget" key="personnel.percentCharged"/>
                                                                        </td>
                                                                        <td class="copybold">
                                                                            <bean:message bundle="budget" key="personnel.percentEffort"/>
                                                                        </td>
                                                                        <td nowrap>                                                    
                                                                            </td>
                                                                        
                                                                    </tr> 						    
                                                                    
                                                                    <tr>
                                                                        <td align = "left">
                                                                            <html:text readonly="true" style=" width:150;" styleClass="cltextbox-nonEditcolor" name= "list" property= "fullName" indexed="true" />
                                                                        </td> 
                                                                        <td align = "left">
                                                                            <%
                                                                            String strCostElement = (String)list.get("costElement");                                                              
                                                                            String javascript = "javascript:dataModified('"+lineItemIndex+"','lineCE','"+strCostElement+"')";
                                                                            %>
                                                                            &nbsp;  <html:select styleClass="cltextbox-medium" disabled="<%=readOnly%>" name="list" property="costElement"  indexed="true" onchange="<%=javascript%>">
                                                                                <html:options collection="costElementData" property="costElement" labelProperty="description"/>
                                                                            </html:select>                                                                							           	    
                                                                        </td>
                                                                        <td align = "left">
                                                                            <%
                                                                            String strPeriodType = (String)list.get("periodType");                                                              
                                                                            javascript = "javascript:dataModified('"+lineItemIndex+"','linePT','"+strPeriodType+"')";
                                                                            %>
                                                                            <html:select styleClass="clcombobox-small" disabled="<%=readOnly%>" name="list" property="periodType"  indexed="true" onchange="<%=javascript%>">
                                                                                <html:options collection="periodTypes" property="code" labelProperty="description"/>
                                                                            </html:select>                                                                							           	    
                                                                        </td>
                                                                        <td align='left' >
                                                                            <%
                                                                            // String percentCharged = ((Double)list.get("percentCharged")).toString();                                                              
                                                                            // added for displaying digits two places  after decimal - start
                                                                            DecimalFormat deciFormat = new DecimalFormat("0.00");
                                                                            String percentCharged = deciFormat.format((Double)list.get("percentCharged")) ;
                                                                            // added for displaying digits two places  after decimal - end+
                                                                            javascript = "javascript:dataModified('"+lineItemIndex+"','linePC','"+percentCharged+"')";
                                                                            
                                                                            
                                                                            %>
                                                                            <html:text style="text-align: right" disabled="<%=readOnly%>" styleClass="cltextbox-number-small" maxlength="6" name= "list" property= "percentCharged" value="<%=percentCharged%>" indexed="true" onchange="<%=javascript%>"/>
                                                                        </td>
                                                                        <td align='left' >
                                                                            <%
                                                                            //String percentEffort = ((Double)list.get("percentEffort")).toString();                                                              
                                                                            // added for displaying digits two places  after decimal - start
                                                                            String percentEffort = deciFormat.format((Double)list.get("percentEffort")) ;
                                                                            // added for displaying digits two places  after decimal - end
                                                                            javascript = "javascript:dataModified('"+lineItemIndex+"','linePE','"+percentEffort+"')";
                                                                            %>
                                                                            <html:text style="text-align: right" disabled="<%=readOnly%>" styleClass="cltextbox-number-small" maxlength="6" name= "list" property= "percentEffort" value="<%=percentEffort%>" indexed="true" onchange="<%=javascript%>"/>
                                                                            
                                                                        </td>
                                                                        

                                                                        <td align='center' class="copybold">
                                                                            
                                                                            
                                                                            <%
                                                                            Integer  lineItemNum = (Integer)list.get("lineItemNumber");
                                                                            String openLineItem="javaScript:open_line_item("+ctr+")";
                                                                            Integer personNumber = (Integer) list.get("personNumber");
                                                                            %>
                                                                            <%  if(!readOnly){%>                                      
                                                                            <html:link href="<%=openLineItem%>"> 
                                                                                <bean:message bundle="budget" key="budgetModCumulative.edit"/>
                                                                            </html:link>
                                                                            <% }else {  %>
                                                                            <%openLineItem="javaScript:openLocationWin("+lineItemNum+","+personNumber+", 'N')";%>
                                                                            <html:link href="<%=openLineItem%>" >
                                                                                <bean:message bundle="budget" key="budget.View"/>
                                                                            </html:link>
                                                                            <% } %> 
                                                                            
                                                                           
                                                                            <%  if(readOnly){%>
                                                                            <bean:message bundle="budget" key="budgetModCumulative.remove"/>
                                                                            <% }else{ %>
                                                                            <a href="javaScript:removeLineItem('RemovePersonnelLineItem.do?rowIndex=<%=ctr%>')" class="copysmall">
                                                                            <bean:message bundle="budget" key="budgetModCumulative.remove"/></a>
                                                                            <% } %>                                                                       
                                                                        </td>
                                                                        
                                                                    </tr>
                                                                    <tr style='height:15px;'>
                                                                        <td nowrap class="copybold">
                                                                            
                                                                            <bean:message bundle="budget" key="budgetLineItemDetailsLabel.startDate"/>
                                                                        </td>
                                                                        <td nowrap class="copybold">                                        
                                                                            &nbsp;   <bean:message bundle="budget" key="budgetLineItemDetailsLabel.endDate"/> 
                                                                            <html:img src="<%=spacerImage%>" width="58" height="2"/>
                                                                            <bean:message bundle="budget" key="personnel.personMonths"/>
                                                                        </td>
                                                                        
                                                                        <td nowrap class="copybold">
                                                                            
                                                                            <bean:message bundle="budget" key="personnel.salaryRequested"/>
                                                                        </td>
                                                                        <td class="copybold">
                                                                            
                                                                            <bean:message bundle="budget" key="personnel.fringeBenefit"/>
                                                                        </td>
                                                                        <td class="copybold" colspan='2'>
                                                                            
                                                                            <bean:message bundle="budget" key="personnel.totalFunds"/>
                                                                        </td>
                                                                        <td class="copybold">
                                                                            
                                                                        </td>
                                                                        
                                                                        
                                                                    </tr> 
                                                                    <tr>
                                                                        <% String calImage = request.getContextPath()+"/coeusliteimages/cal.gif"; %>
                                                                        <td align="left" class="copy" nowrap width="13%">
                                                                            <html:text maxlength="10" size="10" styleClass="cltextbox-number" readonly="<%=readOnly%>" name="list" property="personStartDate" indexed="true" onchange="dataChanged()"/>
                                                                            <%
                                                                            String strStartDateField = "list["+ctr+"].personStartDate";
                                                                            String strStartDateScriptSrc ="javascript:displayCalendarWithTopLeft('"+strStartDateField+"',8,25)";
                                                                            if(readOnly){
                                                                            %>  
                                                                            <html:img src="<%=calImage%>" border="0" height="16" width="16" />
                                                                            <%}else{ %>
                                                                            <html:link href="<%=strStartDateScriptSrc%>" onclick="dataChanged()">
                                                                                <html:img src="<%=calImage%>" border="0" height="16" width="16" />
                                                                            </html:link>
                                                                            <%}%>                                    
                                                                        </td>                                
                                                                        <td align="left" class="copy" >                                    
                                                                            &nbsp;   <html:text maxlength="10" size="10" styleClass="cltextbox-number" readonly="<%=readOnly%>" name="list" property="personEndDate" indexed="true" onchange="dataChanged()"/>
                                                                            <%
                                                                            String strEndDateField = "list["+ctr+"].personEndDate";
                                                                            String strEndDateScriptSrc ="javascript:displayCalendarWithTopLeft('"+strEndDateField+"',8,25)";
                                                                            if(readOnly){
                                                                            %>  
                                                                            <html:img src="<%=calImage%>" border="0" height="16" width="16" />
                                                                            <%}else{ %>
                                                                            <html:link href="<%=strEndDateScriptSrc%>" onclick="dataChanged()">
                                                                                <html:img src="<%=calImage%>" border="0" height="16" width="16" />
                                                                            </html:link>
                                                                            <%}%>                                    
                                                                            <html:img src="<%=spacerImage%>" width="11" height="2"/>
                                                                            <html:text  readonly="true" disabled="<%=readOnly%>" style="text-align: right; width:50;" styleClass="cltextbox-nonEditcolor" name= "list" property= "personMonths" indexed="true" />
                                                                        </td>
                                                                       
                                                                        <td align='left' >
                                                                            
                                                                            <%  double salRequested = ((Double)list.get("salaryRequested")).doubleValue();                                         
                                                                            String salReq = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(salRequested);                                        
                                                                            %>
                                                                            
                                                                            <html:text readonly="true" disabled="<%=readOnly%>" style="text-align: right; width:80;" styleClass="cltextbox-nonEditcolor" name= "list" property= "salaryRequested" value="<%=salReq%>" indexed="true" />
                                                                        </td>
                                                                        <td align='left' >
                                                                            
                                                                            <%  double fringeBenf = ((Double)list.get("fringeBenefit")).doubleValue();                                         
                                                                            String frBenifit = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(fringeBenf);                                         
                                                                            %>
                                                                            <html:text readonly="true" disabled="<%=readOnly%>" style="text-align: right; width:80;" styleClass="cltextbox-nonEditcolor" name= "list"  property= "fringeBenefit" value="<%=frBenifit%>" indexed="true" />
                                                                        </td>
                                                                        <td align='left' colspan='2'>
                                                                            
                                                                            <%  double fundsReq = ((Double)list.get("fundsRequested")).doubleValue();
                                                                            String fundsRequested = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(fundsReq);                                         
                                                                            %>
                                                                            <html:text readonly="true" style="text-align: right; width:80;" styleClass="cltextbox-nonEditcolor" name= "list" property= "fundsRequested" value="<%=fundsRequested%>" indexed="true" />
                                                                        </td>								    
                                                                        
                                                                        <td align = "center">
                                                                            
                                                                        </td>
                                                                    </tr>
                                                                    --%>
                                                                    <tr>
                                                                        <td nowrap class="copybold" align="right">
                                                                            <bean:message bundle="budget" key="personnel.fullName"/>&nbsp;
                                                                        </td>
                                                                        <td align = "left">
                                                                            <html:text readonly="true" style=" width:150;" styleClass="cltextbox-nonEditcolor" name= "list" property= "fullName" indexed="true" />
                                                                        </td> 
                                                                        <td nowrap class="copybold" align="right">
                                                                            &nbsp; <bean:message bundle="budget" key="personnel.costElement"/>
                                                                        </td>
                                                                        <td align = "left" colspan="2">
                                                                            <%
                                                                            String strCostElement = (String)list.get("costElement");                                                              
                                                                            String javascript = "javascript:dataModified('"+lineItemIndex+"','lineCE','"+strCostElement+"')";
                                                                            %>
                                                                            <html:select styleClass="cltextbox-medium" disabled="<%=readOnly%>" name="list" property="costElement"  indexed="true" onchange="<%=javascript%>">
                                                                                <html:options collection="costElementData" property="costElement" labelProperty="description"/>
                                                                                <!-- COEUSQA-1414 Allow schools to indicate if cost element is still active - Start -->
                                                                                  <%if(!"".equals(strCostElement)){
                                                                                  int repeat = 0;
                                                                                  %>
                                                                                  <logic:iterate id="costElementId" name="costElementData">  
                                                                                      <logic:equal name="costElementId" property="costElement" value="<%=strCostElement%>">
                                                                                          <% repeat = repeat+1;%>
                                                                                      </logic:equal>                                                                          
                                                                                  </logic:iterate>
                                                                                    <%if(repeat==0){ %>
                                                                                      <html:option value="<%=strCostElement%>"> <bean:write name="list"  property="costElementDescription"/> </html:option>                                                                      
                                                                                      <%}%>
                                                                                  <%}%>
                                                                                <!-- COEUSQA-1414 Allow schools to indicate if cost element is still active - End -->
                                                                            </html:select>                                                                							           	    
                                                                        </td>
                                                                        <td align='center' class="copybold">
                                                                            <%
                                                                            Integer  lineItemNum = (Integer)list.get("lineItemNumber");
                                                                            String openLineItem="javaScript:open_line_item("+ctr+")";
                                                                            Integer personNumber = (Integer) list.get("personNumber");
                                                                            %>
                                                                            <%  if(!readOnly){%>                                      
                                                                            <html:link href="<%=openLineItem%>"> 
                                                                                <bean:message bundle="budget" key="budgetModCumulative.edit"/>
                                                                            </html:link>
                                                                            <% }else {  %>
                                                                            <%openLineItem="javaScript:openLocationWin("+lineItemNum+","+personNumber+", 'N')";%>
                                                                            <html:link href="<%=openLineItem%>" >
                                                                                <bean:message bundle="budget" key="budget.View"/>
                                                                            </html:link>
                                                                            <% } %> 
                                                                            |
                                                                            <%  if(readOnly){%>
                                                                            <bean:message bundle="budget" key="budgetModCumulative.remove"/>
                                                                            <% }else{ %>
                                                                            <a href="javaScript:removeLineItem('RemovePersonnelLineItem.do?rowIndex=<%=ctr%>')" class="copysmall">
                                                                            <bean:message bundle="budget" key="budgetModCumulative.remove"/></a>
                                                                            <% } %>                                                                       
                                                                        </td>
                                                                    </tr>
                                                                    
                                                                    <tr>
                                                                        <td nowrap class="copybold" align="right">
                                                                            <bean:message bundle="budget" key="personnel.periodType"/>
                                                                        </td>
                                                                        <td align = "left">
                                                                            <%
                                                                            String strPeriodType = (String)list.get("periodType");
                                                                            javascript = "javascript:dataModified('"+lineItemIndex+"','linePT','"+strPeriodType+"')";
                                                                            String strPeriodTypeDescription = "";
                                                                            Vector vecdynamicPeriodTypes = new Vector();
                                                                            session.setAttribute("dynamicPeriodTypes",vecdynamicPeriodTypes);  
                                                                            %>
                                                                            <html:select styleClass="clcombobox-small" disabled="<%=readOnly%>" name="list" property="periodType" indexed="true" onchange="<%=javascript%>">                                                                                
                                                                                <!-- COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start -->
                                                                                <%if(!"".equals(strPeriodType)){
                                                                                int repeat = 0;
                                                                                %>
                                                                                <logic:iterate id="periodTypeId" name="periodTypes"> 
                                                                                    <% vecdynamicPeriodTypes.add((ComboBoxBean)periodTypeId);%>
                                                                                    <logic:equal name="periodTypeId" property="code" value="<%=strPeriodType%>">
                                                                                        <% repeat = repeat+1;%>
                                                                                    </logic:equal>                                                                                    
                                                                                </logic:iterate>
                                                                                <%if(repeat==0){%>
                                                                                <logic:iterate id="allPeriodTypeId" name="allPeriodTypes">  
                                                                                    <logic:equal name="allPeriodTypeId" property="code" value="<%=strPeriodType%>">
                                                                                        <% 
                                                                                        ComboBoxBean periodCmbBean = (ComboBoxBean)allPeriodTypeId;
                                                                                        strPeriodTypeDescription = periodCmbBean.getDescription();%>
                                                                                    </logic:equal>                                                                                    
                                                                                </logic:iterate>
                                                                                <%
                                                                                    ComboBoxBean cmbBean = new ComboBoxBean();
                                                                                    cmbBean.setCode(strPeriodType);
                                                                                    cmbBean.setDescription(strPeriodTypeDescription);                                                                                
                                                                                    vecdynamicPeriodTypes.add(cmbBean);
                                                                                    session.setAttribute("dynamicPeriodTypes",vecdynamicPeriodTypes);                                                                                
                                                                                %>
                                                                                    <html:options collection="dynamicPeriodTypes" property="code" labelProperty="description"/>
                                                                                <%}else{%>
                                                                                    <html:options collection="periodTypes" property="code" labelProperty="description"/>
                                                                                <%}%>
                                                                                <%}else{%>
                                                                                    <html:options collection="periodTypes" property="code" labelProperty="description"/>
                                                                                <%}%>
                                                                                <!-- COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - End -->
                                                                            </html:select>
                                                                        </td>
                                                                        <td class="copybold" align="right">
                                                                            <bean:message bundle="budget" key="personnel.percentCharged"/>
                                                                        </td>
                                                                        <td align='left'>
                                                                            <%
                                                                            // String percentCharged = ((Double)list.get("percentCharged")).toString();                                                              
                                                                            // added for displaying digits two places  after decimal - start
                                                                            DecimalFormat deciFormat = new DecimalFormat("0.00");
                                                                            String percentCharged = deciFormat.format((Double)list.get("percentCharged")) ;
                                                                            // added for displaying digits two places  after decimal - end+
                                                                            javascript = "javascript:dataModified('"+lineItemIndex+"','linePC','"+percentCharged+"')";
                                                                            
                                                                            
                                                                            %>
                                                                            <html:text style="text-align: right" disabled="<%=readOnly%>" styleClass="cltextbox-number-small" maxlength="6" name= "list" property= "percentCharged" value="<%=percentCharged%>" indexed="true" onchange="<%=javascript%>"/>
                                                                        </td>
                                                                        <td class="copybold" align="right">
                                                                            <bean:message bundle="budget" key="personnel.percentEffort"/>
                                                                        </td>
                                                                        <td align='left'>
                                                                            <%
                                                                            //String percentEffort = ((Double)list.get("percentEffort")).toString();                                                              
                                                                            // added for displaying digits two places  after decimal - start
                                                                            String percentEffort = deciFormat.format((Double)list.get("percentEffort")) ;
                                                                            // added for displaying digits two places  after decimal - end
                                                                            javascript = "javascript:dataModified('"+lineItemIndex+"','linePE','"+percentEffort+"')";
                                                                            %>
                                                                            <html:text style="text-align: right" disabled="<%=readOnly%>" styleClass="cltextbox-number-small" maxlength="6" name= "list" property= "percentEffort" value="<%=percentEffort%>" indexed="true" onchange="<%=javascript%>"/>
                                                                        </td>
                                                                    </tr>
                                                                    
                                                                    <tr>
                                                                        <td nowrap class="copybold" align="right">
                                                                            
                                                                            <bean:message bundle="budget" key="budgetLineItemDetailsLabel.startDate"/>
                                                                        </td>
                                                                        <% String calImage = request.getContextPath()+"/coeusliteimages/cal.gif"; %>
                                                                        <td align="left" class="copy" nowrap width="13%">
                                                                            <html:text maxlength="10" size="10" styleClass="cltextbox-number" readonly="<%=readOnly%>" name="list" property="personStartDate" indexed="true" onchange="dataChanged()"/>
                                                                            <%
                                                                            String strStartDateField = "list["+ctr+"].personStartDate";
                                                                            String strStartDateScriptSrc ="javascript:displayCalendarWithTopLeft('"+strStartDateField+"',8,25)";
                                                                            if(readOnly){
                                                                            %>  
                                                                            <html:img src="<%=calImage%>" border="0" height="16" width="16" />
                                                                            <%}else{ %>
                                                                            <html:link href="<%=strStartDateScriptSrc%>" onclick="dataChanged()">
                                                                                <html:img src="<%=calImage%>" border="0" height="16" width="16" />
                                                                            </html:link>
                                                                            <%}%>                                    
                                                                        </td>                                                                              
                                                                         <td nowrap class="copybold" align="right">                                        
                                                                            &nbsp;   <bean:message bundle="budget" key="budgetLineItemDetailsLabel.endDate"/> 
                                                                        </td>
                                                                        <td align="left" class="copy" >                                    
                                                                            <html:text maxlength="10" size="10" styleClass="cltextbox-number" readonly="<%=readOnly%>" name="list" property="personEndDate" indexed="true" onchange="dataChanged()"/>
                                                                            <%
                                                                            String strEndDateField = "list["+ctr+"].personEndDate";
                                                                            String strEndDateScriptSrc ="javascript:displayCalendarWithTopLeft('"+strEndDateField+"',8,25)";
                                                                            if(readOnly){
                                                                            %>  
                                                                            <html:img src="<%=calImage%>" border="0" height="16" width="16" />
                                                                            <%}else{ %>
                                                                            <html:link href="<%=strEndDateScriptSrc%>" onclick="dataChanged()">
                                                                                <html:img src="<%=calImage%>" border="0" height="16" width="16" />
                                                                            </html:link>
                                                                            <%}%>                                    
                                                                        </td>                                                                        
                                                                        <td nowrap class="copybold" align="right">  
                                                                            <bean:message bundle="budget" key="personnel.personMonths"/>
                                                                        </td>
                                                                        <td>
                                                                            <html:text  readonly="true" disabled="<%=readOnly%>" style="text-align: right; width:50;" styleClass="cltextbox-nonEditcolor" name= "list" property= "personMonths" indexed="true" />
                                                                        </td>
                                                                   </tr>
                                                                   
                                                                   <tr>
                                                                        <td nowrap class="copybold" align="right">
                                                                            <bean:message bundle="budget" key="personnel.salaryRequested"/>
                                                                        </td>
                                                                        <td align='left' >
                                                                            <%  double salRequested = ((Double)list.get("salaryRequested")).doubleValue();                                         
                                                                            String salReq = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(salRequested);                                        
                                                                            %>
                                                                            <html:text readonly="true" disabled="<%=readOnly%>" style="text-align: right; width:80;" styleClass="cltextbox-nonEditcolor" name= "list" property= "salaryRequested" value="<%=salReq%>" indexed="true" />
                                                                        </td>
                                                                        <td class="copybold" align="right">
                                                                            <bean:message bundle="budget" key="personnel.fringeBenefit"/>
                                                                        </td>
                                                                        <td align='left' >
                                                                            <%  double fringeBenf = ((Double)list.get("fringeBenefit")).doubleValue();                                         
                                                                            String frBenifit = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(fringeBenf);                                         
                                                                            %>
                                                                            <html:text readonly="true" disabled="<%=readOnly%>" style="text-align: right; width:80;" styleClass="cltextbox-nonEditcolor" name= "list"  property= "fringeBenefit" value="<%=frBenifit%>" indexed="true" />
                                                                        </td>
                                                                        <td class="copybold" align="right">
                                                                            <bean:message bundle="budget" key="personnel.totalFunds"/>
                                                                        </td>
                                                                        <td align='left'>
                                                                            <%  double fundsReq = ((Double)list.get("fundsRequested")).doubleValue();
                                                                            String fundsRequested = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(fundsReq);                                         
                                                                            %>
                                                                            <html:text readonly="true" style="text-align: right; width:80;" styleClass="cltextbox-nonEditcolor" name= "list" property= "fundsRequested" value="<%=fundsRequested%>" indexed="true" />
                                                                        </td>		
                                                                   </tr>

                                                                               <%count++;%>
                                                                    <% lineItemIndex++; %>
                                                                </table>
                                                            </td>
                                                        </tr>
                                                    </logic:iterate>
                                                </logic:present>
                                                
                                            </table>
                                            
                                            <tr class='copybold' align='left' width='100%'>
                                                <td width='20%' colspan='1'align="left" colspan='6'>
                                                    <% if(!readOnly){ %>
                                                    <html:link href="javascript:proposalBudget_Actions('A')" >
                                                        <u><bean:message bundle="budget" key="equipment.NewRow"/></u>
                                                        
                                                    </html:link>
                                                    <%}else{%>
                                                    <bean:message bundle="budget" key="equipment.NewRow"/>
                                                    
                                                    <%}%>
                                                    &nbsp;&nbsp;|&nbsp;&nbsp;
                                                    <% if(!readOnly){ %>
                                                    <html:link href="javascript:proposalBudget_Actions('C')" >
                                                        <u><bean:message bundle="budget" key="budgetButton.calculate"/></u>
                                                    </html:link>
                                                    <%}else{%>
                                                    <bean:message bundle="budget" key="budgetButton.calculate"/>
                                                    <%}%>
                                                </td>
                                            </tr>
                                            
                                            <tr>
                                                <td>
                                                    <html:img src="<%=spacerImage%>" width="1" height="2"/>
                                                </td>
                                            </tr>
                                            
                                            
                                        </td>
                                    </tr>
                                </table>
                                <!-- Personnel Budget Details - END -->
                                
                            </td>
                        </tr>   
                        <tr>
                            <td class='savebutton'>
                                <html:button accesskey="s" property="Save" styleClass="clsavebutton" onclick="proposalBudget_Actions('S')" disabled="<%=readOnly%>">
                                    <bean:message bundle="budget" key="budgetButton.save" />
                                </html:button>
                            </td>
                        </tr> 
                        <!--</table> -->
                        <tr>
                            <td>
                                <!-- Personnel Costs - START -->
                                <table width="100%" align="center" border="0" cellpadding="0" cellspacing="0" border="0">
                                    <tr valign=top>
                                        <td>
                                            
                                            <table width="100%" align=center border="0" cellpadding="0" cellspacing="2" class='theader'>
                                                <tr>
                                                    <td width='50%' align=left nowrap>
                                                        <div id='budgetPeriodShowImage' style='display: none;'>
                                                            <html:link href="javaScript:budgetDetailsShowHide('3');">
                                                            &nbsp;<html:img src="<%=plus%>" border="0"/></html:link>&nbsp;Personnel Costs - Line Items entered in Coeus Premium (View-only)
                                                        </div>
                                                        <div id='budgetPeriodHideImage'>
                                                            <html:link href="javaScript:budgetDetailsShowHide('4');">
                                                            &nbsp;<html:img src="<%=minus%>" border="0"/></html:link>&nbsp;Personnel Costs - Line Items entered in Coeus Premium (View-only)
                                                        </div>
                                                    </td>                         
                                                    
                                                </tr>
                                            </table>
                                            
                                        </td>
                                    </tr>
                                    <tr>
                                        <td align="center" valign="top">
                                            <logic:present name="personnelCEwithoutPersons" scope="session"> 
                                                <div id='budgetPeriodDetails'>
                                                    <table width="100%"  border="0" cellspacing="0" cellpadding="3" class="tabtable">
                                                        <tr align="center" valign="top">
                                                            <td  align="left" class="theader"> <bean:message bundle="budget" key="personnel.costElement"/></td>
                                                            <td  align="left" class="theader"><bean:message bundle="budget" key="summaryLabel.StartDate"/> </td>
                                                            <td  align="left" class="theader"><bean:message bundle="budget" key="summaryLabel.EndDate"/> </td>
                                                            <td  align="left" class="theader"> <bean:message bundle="budget" key="personnel.fringeBenefit"/></td>
                                                            <td  align="left" class="theader"> <bean:message bundle="budget" key="personnel.totalFunds"/></td>
                                                            <td  align="left" class="theader"> &nbsp; </td>
                                                            
                                                        </tr>
                                                        <%
                                                        String strBgColor1 = "#DCE5F1";
                                                        int index1=0;
                                                        %>
                                                        
                                                        <logic:iterate id="persondata" name="personnelCEwithoutPersons" type="org.apache.struts.validator.DynaValidatorForm">
                                                            <%
                                                            if (index1%2 == 0) {
                                                            strBgColor1 = "#D6DCE5"; 
                                                            }
                                                            else { 
                                                            strBgColor1="#DCE5F1"; 
                                                            }  %>     
                                                            <% int budgetPeriod = Integer.parseInt((String)(persondata.get("budgetPeriod").toString()));
                                                            int lineItemNum = Integer.parseInt((String)(persondata.get("lineItemNumber").toString()));
                                                            int version  = Integer.parseInt((String)(persondata.get("versionNumber").toString()));
                                                            String linkOpen = "javaScript:openLocationWin("+budgetPeriod+","+lineItemNum+",'Y',"+version+");";
                                                            totalFunds+= Double.parseDouble((String)(persondata.get("fundsRequested").toString()));%>   
                                                            <tr bgcolor="<%=strBgColor1%>" onmouseover="className='TableItemOn'" onmouseout="className='TableItemOff'"> 
                                                                <td class="copy">
                                                                    <bean:write name="persondata" property="costElementDescription"/> 
                                                                </td>
                                                                <td class="copy">
                                                                    <coeusUtils:formatDate name="persondata" property="startDate"/> 
                                                                </td>
                                                                <td class="copy">
                                                                    <coeusUtils:formatDate name="persondata" property="endDate"/>  
                                                                </td>
                                                                <td class="copy">
                                                                    <coeusUtils:formatString name="persondata" property="fringeBenefit" formatType="currencyFormat"/>                  
                                                                </td>
                                                                <td class="copy">
                                                                    <coeusUtils:formatString name="persondata" property="fundsRequested" formatType="currencyFormat"/>                   
                                                                </td>
                                                                <td class="copy">
                                                                    <html:link href="<%=linkOpen%>" >
                                                                        <bean:message bundle="budget" key="budget.View"/>              
                                                                    </html:link>      
                                                                </td>
                                                                
                                                                <% 
                                                                index1++;
                                                                %>   
                                                            </tr>
                                                        </logic:iterate>
                                                    </table>
                                                </div>
                                            </logic:present>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class='copy' align = "right">
                                            <logic:notEmpty name="budgetPersonnelDynaBean" scope="session">
                                                <table width="100%"  border="0" cellpadding="2" cellspacing="0" >
                                                    <!--End iteration here-->
                                                    <tr align='center'>
                                                        <td width = "30%" colspan='3'><img src="<bean:write name='ctxtPath'/>/coeusliteimages/spacer.gif"width="20%" height="0">
                                                        </td>
                                                        <td colspan='0'align="right" class='copybold' >
                                                            <bean:message bundle="budget" key="personnel.columnTotal"/>
                                                            <%--img src="<bean:write name='ctxtPath'/>/coeusliteimages/spacer.gif"width="4" height="5"--%>
                                                        </td>
                                                        <td class='copy' align = "left">
                                                            <% String totalFundsReq = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(totalFunds); %>
                                                            <INPUT type='text' disabled="true" size='25' style="text-align: right; font-weight:bold" maxlength='14' class='cltextbox-number-medium' name='totalFundsRequested' value="<%=totalFundsReq%>">
                                                        </td>
                                                    </tr>
                                                </table>
                                            </logic:notEmpty>                    
                                        </td>
                                    </tr>
                                </table>
                                <!-- Personnel Costs - END -->
                            </td>
                        </tr>
                    </table>
                 </td>
                        </tr>
                    </table>
            </div>

            <div id='messageDiv' style='display: none;'>
                <table width="100%" height="100%" align="center" border="0" cellpadding="3" cellspacing="0" class="tabtable">
                <tr>
                    <td align='center' class='copyred'>  <br> <bean:message bundle="budget" key="budgetMessages.pleaseWait"/>  </td>
                </tr>
                </table>
            </div>
            <div id='calculateDiv' style='display: none;'>
                <table width="100%" height="100%" align="center" border="0" cellpadding="3" cellspacing="0" class="tabtable">
                <tr>
                    <td align='center' class='copyred'><bean:message bundle="budget" key="budgetMessages.calculatingBudget"/> 
                        <br> <bean:message bundle="budget" key="budgetMessages.pleaseWait"/>  
                    </td>
                </tr>
                </table>
            </div>
            <div id='saveDiv' style='display: none;'>
                <table width="100%" height="100%" align="center" border="0" cellpadding="3" cellspacing="0" class="tabtable">
                <tr>
                    <td align='center' class='copyred'><bean:message bundle="budget" key="budgetMessages.savingBudget"/> 
                        <br> &nbsp;&nbsp;&nbsp;  <bean:message bundle="budget" key="budgetMessages.pleaseWait"/>  
                    </td>
                </tr>
                </table>
            </div>

            <div id='checkingDiv' style='display: none;'>
                <table width="100%" height="100%" align="center" border="0" cellpadding="3" cellspacing="0" class="tabtable">
                <tr>
                    <td align='center' class='copyred'>  <br> <bean:message bundle="budget" key="budgetMessages.pleaseWait"/>  </td>
                </tr>
                </table>
            </div>
         </html:form>
    <script>
          DATA_CHANGED = 'false';
          var dataModi = '<%=request.getAttribute("dataModified")%>';
          if(dataModi == 'modified' || (errValue && !errLock)){
                DATA_CHANGED = 'true';
          }
          budgetPeriod = "<%=sessionPeriodNumber%>";
          LINK = "<%=request.getContextPath()%>/calculatePersonnelBudget.do?budgetPeriod=" + budgetPeriod + "&Save=S";
          FORM_LINK = document.budgetPersonnelDynaBean;
          PAGE_NAME = "<bean:message bundle="budget" key="personnel.label"/>";
          function dataChanged(){
                DATA_CHANGED = 'true';
          }
          linkForward(errValue);
    </script>
    <script>
      var help = '<bean:message bundle="budget" key="helpTextBudget.Personnel"/>';
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


