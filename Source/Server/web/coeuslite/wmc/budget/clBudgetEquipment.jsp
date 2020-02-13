<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<%@ page import="java.util.Vector,edu.wmc.coeuslite.budget.bean.CategoryBean,
                java.util.List,edu.mit.coeuslite.utils.CoeusDynaBeansList"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld"  prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld"  prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"  prefix="coeusUtils"%>
<jsp:useBean  id="BudgetPeriodData"  scope="session" class="java.util.Vector"/>
<jsp:useBean  id="costElementData"  scope="session" class="java.util.Vector"/>
<jsp:useBean  id="FilterBudgetDetailData"  scope="request" class="java.util.Vector"/>
<jsp:useBean  id="pageConstantValue" scope="session" class="java.lang.String"/>
<jsp:useBean  id="popUp"  scope="request" class="java.lang.String"/>
<bean:size id="budgetLineItemsSize" name="FilterBudgetDetailData"/>
<html:html locale="true">
<head>
    <style>
        .textbox{ width: 30px}
    </style>
</head>
<title>Budget Equipment</title>
<% boolean valueChanges = false; 
   String mode = (String)session.getAttribute("mode"+session.getId());
   String budgetStatusMode = (String)session.getAttribute("budgetStatusMode");
   Vector vecCostElementMessages = (Vector) session.getAttribute("inactive_CE_messages");
   boolean readOnly = false;
   if(mode!= null && mode.equals("display")){
       readOnly = true;
   }else if(budgetStatusMode != null && budgetStatusMode.equals("complete")){
        readOnly = true;
    }
   String editImage = request.getContextPath()+"/coeusliteimages/edit.gif";
   String delImage= request.getContextPath()+"/coeusliteimages/delete.gif";
   String spacerImage= request.getContextPath()+"/coeusliteimages/spacer.gif";
   CoeusDynaBeansList coeusLineItemDynaList = (CoeusDynaBeansList)session.getAttribute("lineItemDynaBeanList");
   List beanList = coeusLineItemDynaList.getBeanList();
   Integer sessionPeriodNumber = (Integer)beanList.get(0);
   
    

   
   
%>
<script language="JavaScript">
        var change = "";
        var errValue = false;
        var errLock = false;
        /* Method to activate tabs */
	function activateTab(requestPeriod,prevPeriod) {
             var page = "<%=session.getAttribute("pageConstantValue")%>";
             <% boolean dataChangesFlag = false;
                if(session.getAttribute("dataChanges") != null){
                    dataChangesFlag = ((Boolean)session.getAttribute("dataChanges")).booleanValue();
                }%>
             document.lineItemDynaBeanList.action = "<%=request.getContextPath()%>/getBudgetEquipment.do?Period="+requestPeriod+"&PAGE="+page+"&OldPeriod="+prevPeriod;
             CLICKED_LINK = "<%=request.getContextPath()%>/getBudgetEquipment.do?Period="+requestPeriod+"&PAGE="+page+"&OldPeriod="+prevPeriod;
	     if(requestPeriod != prevPeriod)
                {
                    LINK = "<%=request.getContextPath()%>/calculateBudget.do?budgetPeriod="+prevPeriod+"&Save=S"+"&requestBudgetPeriod="+requestPeriod;
                    if(validate()){
/*                        var msg = '<bean:message bundle="budget" key="budgetLabel.saveConfirmation" />';
                        if (confirm(msg)==true){
                            document.lineItemDynaBeanList.action = "<%=request.getContextPath()%>/calculateBudget.do?budgetPeriod="+prevPeriod+"&Save=S"+"&requestBudgetPeriod="+requestPeriod;
                            document.lineItemDynaBeanList.submit();
                        }
                    }else{*/
                        document.lineItemDynaBeanList.submit();
                    }
                }
         }
        /* Method to set action on buttons */
        function proposalBudget_Actions(actionName){        
           var budgetPeriod = "<%=sessionPeriodNumber%>";
            if(actionName == "A"){
                document.lineItemDynaBeanList.action = "<%=request.getContextPath()%>/AddLineItem.do?AddBudgetPeriodRow="+budgetPeriod;
                document.lineItemDynaBeanList.submit();
            }else if(actionName == "C"){
                    document.lineItemDynaBeanList.action = "<%=request.getContextPath()%>/calculateBudget.do?budgetPeriod="+budgetPeriod;
                    document.lineItemDynaBeanList.submit();
                 // Hide the code in first div tag
                    document.getElementById('equipmentFormDiv').style.display = 'none';
                 // Display code in second div tag
                    document.getElementById('calculateDiv').style.display = 'block';  
                    
            }else if(actionName == "S"){
                    document.lineItemDynaBeanList.action = "<%=request.getContextPath()%>/calculateBudget.do?budgetPeriod="+budgetPeriod+"&Save=S";
                    document.lineItemDynaBeanList.submit();
                 // Hide the code in first div tag
                    document.getElementById('equipmentFormDiv').style.display = 'none';
                 // Display code in second div tag
                    document.getElementById('saveDiv').style.display = 'block';  
            }
        }
        /* method for removing line item */
        function removeLineItem(link,isFormulatedLineItem){
            if(isFormulatedLineItem == 'true'){
                var formulatedMsg = '<bean:message bundle="budget" key="budgetFormulatedLineItem.delete" />';
                if (confirm(formulatedMsg)==true){
                        document.lineItemDynaBeanList.action = "<%=request.getContextPath()%>/"+link;
                        document.lineItemDynaBeanList.submit();
                }
            }else{
                var msg = '<bean:message bundle="budget" key="budgetLabel.deleteConfirmation" />';
                if (confirm(msg)==true){
                        document.lineItemDynaBeanList.action = "<%=request.getContextPath()%>/"+link;
                        document.lineItemDynaBeanList.submit();
                }
             }
        }
        /* Method to open Line Item Detail Window. First save all the line items.*/
        function open_line_item(lineItemNumber){
                var budgetPeriod = "<%=sessionPeriodNumber%>";
                document.lineItemDynaBeanList.action = "<%=request.getContextPath()%>/calculateBudget.do?budgetPeriod="+budgetPeriod+"&Save=S&Edit=E&lineItemNumber="+lineItemNumber;
                document.lineItemDynaBeanList.submit();
                // Hide the code in first div tag
                    document.getElementById('equipmentFormDiv').style.display = 'none';
                 // Display code in second div tag
                    document.getElementById('messageDiv').style.display = 'block';                
        }
        /* Method to check data is modified or not */
        function dataModified(index, type, oldValue){
            dataChanged();
            var val = "";
            if(type == 'Desc'){
                var desc = "dynaFormData["+index+"].lineItemDescription";
                val = document.getElementsByName(desc);
                val = val[0].value;
                val = val.replace( /^\s*/, "" ).replace( /\s*$/, "" );
                if(val != oldValue){
                    change = "true";
                }
            }else if(type == 'lineItemCost'){
               
                var cost = "dynaFormData["+index+"].tempLineItemCost";
                val = document.getElementsByName(cost);
                val = val[0].value;  
               // val = val.toString().replace(/\$|\,/g,'');  
                var removeDollar = new RegExp("[$,/,]","g");
                val = val.replace(removeDollar, '');
                if(val != oldValue){
                   change = "true";
                }
        
            }else if(type == 'lineCE'){
                var CE = "dynaFormData["+index+"].costElement";
                val = document.getElementsByName(CE);         
                val = val[0][val[0].selectedIndex].value; 
                if(val != "" && val != oldValue){
                    change = "true";
                }
            }else if(type == 'quantity'){                           //Added for Case #3132 - start Changing quantity field from integer to float
                var qty = "dynaFormData["+index+"].tempQuantity";
                val = document.getElementsByName(qty);
                val = val[0].value;
                if(val != oldValue){
                    change = "true";
                }
            }                                                       //Added for Case #3132 - end
        }

    function openLocationWin(lineItemNumber,subAwardNumber) {    
        var budgetPeriod = "<%=sessionPeriodNumber%>";
        var lineItemNo = "<%=request.getAttribute("lineItemNumber")%>";
        var w = 760;
        var h = 400;
        if(lineItemNo == 'null' || lineItemNo == undefined) { 
            lineItemNo = lineItemNumber;
        }
        if (window.screen) {
               leftPos = Math.floor(((window.screen.width-w) / 2));
               topPos = Math.floor(((window.screen.height- h) / 2));
         }
        var loc = '<bean:write name='ctxtPath'/>'+"/getLineItemDetails.do?budgetPeriod="+budgetPeriod+"&lineItemNumber="+lineItemNo+"&subAwardNumber="+subAwardNumber;
        var newWin 
          = window.open(loc,"formName",'dependent=1,toolbar=0,location=0,directories=0,status=0, menubar=0,scrollbars=1,resizable=0,width=' + w + ',height=' + h+ ',left='+ leftPos + ',top=' + topPos);
        newWin.window.focus();
     }
        
</script>
<html:base/>
<body>
<html:form action="/getBudgetEquipment.do" method="POST">
<!-- New Templates for BudgetEquipment Page   -->
 <div id='equipmentFormDiv'>
 <%-- Starts for opening the child window --%>
<logic:present name ="popUp" scope="request">
<logic:equal scope="request" name="popUp" value="fromLocation">
<script>
    openLocationWin('0','0'); 
</script>
</logic:equal>        
</logic:present>
<%-- End of opening the child window --%>
<table width="100%" height="100%"  border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td>
            <div id="helpText" class='helptext'> 
                 <logic:present name="pageConstantValue" scope="session">
                       <logic:equal name="pageConstantValue" value="EQ">
                            <bean:message bundle="budget" key="helpTextBudget.Equipment"/>  
                       </logic:equal>
                       <logic:equal name="pageConstantValue" value="TR"> 
                             <bean:message bundle="budget" key="helpTextBudget.Travel"/>    
                       </logic:equal>
                       <logic:equal name="pageConstantValue" value="PT">
                             <bean:message bundle="budget" key="helpTextBudget.ParticipantTrainee"/>  
                       </logic:equal>
                       <logic:equal name="pageConstantValue" value="ODC"> 
                            <bean:message bundle="budget" key="helpTextBudget.OtherDirectCosts"/>  
                       </logic:equal>
                 </logic:present>
            </div>
        </td>
    </tr>
      <tr>
        <td  align="left" valign="top">
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
        </td>
      </tr>
      
      <tr>
          <td>
              <table align='center' width="100%" border="0" cellpadding="10" cellspacing="0" class="table">
                  
                  <tr>
                      <td>
                          <table align='center' width="100%" border="0" cellpadding="2" cellspacing="0" class="tabtable">
                              <tr class='tableheader'>
                                  <td colspan="8">
                                      <bean:message bundle="budget" key="equipmentLabel.periodTotals" />
                                  </td>
                              </tr>
                              <!--<tr>
                                  <td colspan="5" align="center">
                                      <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tabtable">-->
                                          <tr>
                                              <logic:present name ="BudgetPeriodData" scope = "session">       
                                                  <logic:iterate id="data" name="BudgetPeriodData" type = "org.apache.commons.beanutils.DynaBean">
                                                      <%                     
                                                      int beanPeriod = ((Integer)data.get("budgetPeriod")).intValue();
                                                      if(budgetPeriodnumber == beanPeriod ){
                                                      %>
                                                      <tr>  
                                                          <td align="right" nowrap><b> <bean:message bundle="budget" key="budgetLabel.directCost" /> :</b>
                                                          </td>
                                                          
                                                          <td  align="25%" nowrap>
                                                              <coeusUtils:formatString name="data" property="totalDirectCost" formatType="currencyFormat"/>
                                                              </td>
                                                          
                                                          <td  align="right" nowrap><b> <bean:message bundle="budget" key="budgetLabel.inDirectCost" /> :</b>
                                                          </td>
                                                          
                                                          <td  align="25%" nowrap>
                                                              <coeusUtils:formatString name="data" property="totalIndirectCost" formatType="currencyFormat"/>
                                                              </td>
                                                          
                                                          <td align="right" nowrap><b> <bean:message bundle="budget" key="budgetLabel.totalCost" /> :</b>
                                                          </td>
                                                          
                                                          <td align="25%" nowrap>
                                                              <coeusUtils:formatString name="data" property="totalCost" formatType="currencyFormat"/>
                                                              </td>
                                                          <td align="right" nowrap><b><bean:message bundle="budget" key="budgetLabel.NoOfPeriodMonths" /> :</b>
                                                          </td>
                                                          
                                                          <td align="25%" nowrap>
                                                              <coeusUtils:formatOutput name="data" property="noOfPeriodMonths" />
                                                              </td>    
                                                              
                                                      </tr>
                                                      <tr>
                                                          <td  align="right" nowrap><b> <bean:message bundle="budget" key="budgetLabel.underRecovery" /> :</b>
                                                          </td>
                                                          
                                                          <td  align="25%" nowrap>
                                                              <coeusUtils:formatString name='data' property='underRecoveryAmount' formatType='currencyFormat'/>
                                                              </td>
                                                          
                                                          <td  align="right" nowrap><b> <bean:message bundle="budget" key="budgetLabel.costShare" /> :</b>
                                                          </td>
                                                          
                                                          <td  align="25%" nowrap>
                                                              <coeusUtils:formatString name="data" property="costSharingAmount" formatType="currencyFormat"/>
                                                              </td>
                                                          
                                                          <td align="right" nowrap><b> <bean:message bundle="budget" key="budgetLabel.period" /> :</b>
                                                          </td>
                                                          
                                                          <td align="15%" nowrap>
                                                              <coeusUtils:formatDate name="data" property="startDate" />
                                                              -
                                                              <coeusUtils:formatDate name="data" property="endDate" />
                                                              </td>
                                                              <td> </td>
                                                              <td > </td>
                                                               
                                                      </tr>
                                                      <%}%>
                                                  </logic:iterate>
                                              </logic:present>
                                              <%}%>
                                          </tr>
                                      <!--</table>
                                  </td>
                              </tr>-->
                              <tr>
                                  <td height='5'>
                                  </td>
                              </tr>
                          </table>
                      </td>
                  </tr>
                  <tr>
                      <td height='5'>
                      </td>
                  </tr>
                              <tr>
                                  <td height="100%" align="left" valign="top">
                                      <table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0" class="tabtable">
                                          <tr>
                                              <td align="left" valign="top">
                                                  <table width="100%" height="20"  border="0" cellpadding="0" cellspacing="0" class="table">
                                                      <tr>
                                                          <td  align="left" class="tableheader">
                                                              <logic:present name="pageConstantValue" scope="session">
                                                                  <logic:equal name="pageConstantValue" value="EQ">
                                                                      <bean:message bundle="budget" key="equipmentLabel.budgetEquipment"/>&nbsp; - <bean:message bundle="budget" key="budgetLineItem.header"/> 
                                                                  </logic:equal>
                                                                  <logic:equal name="pageConstantValue" value="TR"> 
                                                                      <bean:message bundle="budget" key="travelLabel.budgetTravel"/>&nbsp; - <bean:message bundle="budget" key="travelLabel.header"/>                                       
                                                                  </logic:equal>
                                                                  <logic:equal name="pageConstantValue" value="PT">
                                                                      <bean:message bundle="budget" key="participantTraineeLabel.participantTrainee"/>
                                                                      <%--&nbsp; - <bean:message bundle="budget" key="participantTraineeLabel.header"/> 
                                                       <html:text property="NumberOfTrainees" value="" disabled="true" size="3"/>--%>
                                                                  </logic:equal>
                                                                  <logic:equal name="pageConstantValue" value="ODC"> 
                                                                      <bean:message bundle="budget" key="budgetOtherDC.OtherDirectCosts"/>
                                                                  </logic:equal>
                                                              </logic:present>
                                                          </td>      
                                                      </tr>
                                                  </table>
                                              </td>
                                          </tr>
                                          <tr>
                                              <td class='copy' align="left" valign="top">
                                                  <font color='red'>
                                                      <logic:messagesPresent>
                                                          <script>errValue = true; </script>
                                                          <html:errors header="" footer=""/>
                                                      </logic:messagesPresent>
                                                      <logic:messagesPresent message="true">
                                                          <script>errValue = true; </script>
                                                          <html:messages id="message" message="true" property="budget_common_exceptionCode.1011" bundle="budget">                
                                                              <li><bean:write name = "message"/></li>
                                                          </html:messages>
                                                          <html:messages id="message" message="true" property="budget_common_exceptionCode.101" bundle="budget">                
                                                              <li><bean:write name = "message"/></li>
                                                          </html:messages>
                                                          <html:messages id="message" message="true" property="budget_common_exceptionCode.102" bundle="budget">                
                                                              <li><bean:write name = "message"/></li>
                                                          </html:messages>
                                                          <!-- If lock is deleted then show this message --> 
                                                          <html:messages id="message" message="true" property="errMsg"> 
                                                              <script>errLock = true; </script>
                                                              <li><bean:write name = "message"/></li>
                                                          </html:messages>
                                                          <!-- If lock is acquired by another user, then show this message -->
                                                          <html:messages id="message" message="true" property="acqLock"> 
                                                              <script>errLock = true; </script>
                                                              <li><bean:write name = "message"/></li>
                                                          </html:messages>
                                                          <!--Added for Case #3132 - start
                                                              Changing quantity field from integer to float-->
                                                          <html:messages id="message" message="true" property="error.lineItemDetails.quantity" bundle="budget">
                                                              <li><bean:write name = "message"/></li>
                                                          </html:messages>   
                                                          <!--Added for Case #3132 - end-->
                                                      </logic:messagesPresent>   
                                                      <!--Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start -->
                                                      <%if(vecCostElementMessages != null && vecCostElementMessages.size() >0) {%>                                                
                                                      <%for(int index = 0; index < vecCostElementMessages.size(); index++){%>
                                                      <tr>
                                                          <td align = "left">
                                                              <font color="red">
                                                                  <li> <%=vecCostElementMessages.elementAt(index)%> </li>
                                                              </font>
                                                          </td>
                                                          <td>
                                                              <br>
                                                              &nbsp;
                                                              <br>
                                                          </td>
                                                      </tr>                                                
                                                      <%}}
                                                      session.setAttribute("inactive_CE_messages",null);
                                                      %>
                                                      <!--Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - end -->
                                                  </font>
                                              </td>
                                          </tr>
                                          <tr>
                                              <td align="left" valign="top">&nbsp;</td>
                                          </tr> 
                                          
                                          <tr>
                                              <td>
                                                  <!--start Iteration here-->
                                                  <table width="98%" align="center" border="0" cellpadding="2" cellspacing="0"  class="tabtable" >
                                                      <%double totalFunds = 0;%>
                                                      <%-- <logic:present name ="FilterBudgetDetailData" scope = "request">                                                               --%>
                                                      <tr class='copybold'>
                                                          <td nowrap width='25%' align="center" class="theader">
                                                              <bean:message bundle="budget" key="equipment.Type"/>
                                                          </td>
                                                          <td nowrap width='20%' align="center" class="theader">
                                                              <bean:message bundle="budget" key="equipment.Description"/>
                                                          </td>
                                                          
                                                          <logic:equal name="pageConstantValue" value="PT">
                                                              <td nowrap width='20%' align="center" class="theader">
                                                                  <bean:message bundle="budget" key="equipment.Qty"/>
                                                              </td>
                                                          </logic:equal>
                                                          
                                                          <td width='12%'  align="left" class="theader">
                                                              <bean:message bundle="budget" key="equipment.FundRequested"/>
                                                          </td>
                                                          
                                                          <td width='5%'  nowrap align="center" class="theader" >
                                                              <!--<bean:message bundle="budget" key="budgetModCumulative.edit"/>-->
                                                          </td>
                                                          <td width='5%'  align="center" class="theader">
                                                              <!--<bean:message bundle="budget" key="equipment.Remove"/>-->
                                                          </td>
                                                      </tr> 
                                                      <% int lineItemIndex = 0; %>
                                                      <!--logic:iterate id = "budgetLineItem" name ="FilterBudgetDetailData"                                                                     
                                                type = "org.apache.commons.beanutils.DynaBean" indexId = "index"-->   
                                                 
                                                      <logic:iterate id="dynaFormData" name="lineItemDynaBeanList" property="list" type="org.apache.struts.action.DynaActionForm" indexId="index">  
                                                          <tr>  
                                                              <td width='25%' align='center'>
                                                                  <% String strCostElement = (String)dynaFormData.get("costElement");                                                              
                                                                  java.text.NumberFormat numberFormat = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US);
                                                                  String lineItemCost = numberFormat.format(((Double)dynaFormData.get("lineItemCost")).doubleValue()); 
                                                                  
                                                                  String javascript = "dataModified('"+lineItemIndex+"','lineCE','"+strCostElement+"')";
                                                                  String tempLineItemCost = (String)dynaFormData.get("tempLineItemCost");                                                            
                                                                  if(tempLineItemCost!=null && !tempLineItemCost.equals("")){
                                                                  String strCost = tempLineItemCost;
                                                                  tempLineItemCost = tempLineItemCost.replaceAll("[$,/,]","");
                                                                  try{
                                                                  tempLineItemCost 
                                                                  = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(new Double(tempLineItemCost));
                                                                  }catch(java.lang.NumberFormatException ne){
                                                                  tempLineItemCost = strCost;
                                                                  }
                                                                  lineItemCost = tempLineItemCost;
                                                                  }
                                                                  //Added for Case #3132 - start
                                                                  //Changing quantity field from integer to float
                                                                  String strQuantity = ((Double)dynaFormData.get("quantity")).toString();
                                                                  String tempQuantity = (String)dynaFormData.get("tempQuantity");
                                                                  if(tempQuantity != null && !tempQuantity.equals("")) {
                                                                      strQuantity = tempQuantity;
                                                                  }
                                                                  numberFormat = java.text.NumberFormat.getNumberInstance(java.util.Locale.US);
                                                                  strQuantity = numberFormat.format((Double)dynaFormData.get("quantity"));
                                                                  if(strQuantity.indexOf(".") == -1) {
                                                                      strQuantity = strQuantity + ".00";
                                                                  }
                                                                  //Added for Case #3132 - end
                                                                  boolean disableSubAwardLinItem = false;
                                                                  int subAwardNumber = 0;
                                                                  if(dynaFormData.get("subAwardNumber")!= null){
                                                                      subAwardNumber = ((java.lang.Integer)dynaFormData.get("subAwardNumber")).intValue();
                                                                      if(subAwardNumber > 0){
                                                                          disableSubAwardLinItem = true;
                                                                      }else{
                                                                          disableSubAwardLinItem = readOnly;
                                                                      }
                                                                  }else{
                                                                      disableSubAwardLinItem = readOnly;
                                                                  }
                                                                  // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - Start
                                                                  boolean disableLineItemType = false;
                                                                  boolean disableDescription = false;
                                                                  boolean disableQuantity = false;
                                                                  boolean disableFundsRequested = false;
                                                                  boolean disableEditLink = false;
                                                                  boolean disableRemoveLink = false;
                                                                  boolean isFormulatedLineItem = false;
                                                                  if(!readOnly && !disableSubAwardLinItem &&
                                                                      dynaFormData.get("isFormulatedLineItem") != null && ((Boolean)(dynaFormData.get("isFormulatedLineItem"))).booleanValue()){
                                                                      disableLineItemType = true;
                                                                      disableQuantity = true;
                                                                      disableFundsRequested = true;
                                                                      isFormulatedLineItem = true;
                                                                  }else if(readOnly || disableSubAwardLinItem){
                                                                      disableLineItemType = true;
                                                                      disableDescription = true;
                                                                      disableQuantity = true;
                                                                      disableFundsRequested = true;
                                                                      disableEditLink = true;
                                                                      disableRemoveLink = true;
                                                                  }
                                                                  // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - End
                                                                  %>
                                                                                                                                    
                                                                  <html:select name="dynaFormData" property="costElement" indexed="true" styleClass="clcombobox-longer" disabled="<%=disableLineItemType%>"
                                                                               onchange="<%=javascript%>">                                                                                                                                                       
                                                                      <html:options collection="costElementData" property="costElement" labelProperty="description"/>                                                                      
                                                                      <!-- COEUSQA-1414 Allow schools to indicate if cost element is still active - Start -->
                                                                      <%if(!"".equals(strCostElement)){
                                                                      int count = 0;
                                                                      %>
                                                                      <logic:iterate id="costElementId" name="costElementData">  
                                                                          <logic:equal name="costElementId" property="costElement" value="<%=strCostElement%>">
                                                                              <% count = count+1;%>
                                                                          </logic:equal>                                                                          
                                                                      </logic:iterate>
                                                                      <%if(count==0){%>
                                                                          <html:option value="<%=strCostElement%>"> <bean:write name="dynaFormData"  property="costElementDescription"/> </html:option>                                                                      
                                                                          <%}%>
                                                                      <%}%>
                                                                      <!-- COEUSQA-1414 Allow schools to indicate if cost element is still active - End -->
                                                                  </html:select>
                                                              </td>                        
                                                              <td width='20%' align='center'>
                                                                  <% String strLineDescription = (String)dynaFormData.get("lineItemDescription");
                                                                  javascript = "dataModified('"+lineItemIndex+"','Desc','"+strLineDescription+"')";%>  
                                                                  
                                                                  <html:text name="dynaFormData" maxlength="80" styleClass="clcombobox-longer" property="lineItemDescription" indexed="true" 
                                                                             onchange="<%=javascript%>" readonly="<%=disableDescription%>"/>
                                                              </td>
                                                              <logic:equal name="pageConstantValue" value="PT">
                                                                  <td width='20%' align='center'>
                                                                  <!--Modified for Case #3132 - start
                                                                      Changing quantity field from integer to float-->
                                                                  <% Double dblQuantity = (Double)dynaFormData.get("quantity");
                                                                  javascript = "dataModified('"+lineItemIndex+"','quantity','"+dblQuantity+"')";%>  
                                                                      <html:text property="tempQuantity" name="dynaFormData" indexed="true" size="3" value="<%=strQuantity%>"
                                                                         onchange="<%=javascript%>" readonly="<%=disableQuantity%>" styleClass="cltextbox-number"/>
                                                                      <html:hidden property="quantity" name="dynaFormData" indexed="true"/>
                                                                      <!--Modified for Case #3132 - end-->
                                                                  </td>  
                                                              </logic:equal>                                                    
                                                              <td width='12%' align='left' >
                                                                  <% Double strLineCost = (Double)dynaFormData.get("lineItemCost");  
                                                                  javascript = "dataModified('"+lineItemIndex+"','lineItemCost','"+strLineCost+"')";%>
                                                                  <html:text name="dynaFormData"  property="tempLineItemCost"  value = "<%=lineItemCost%>" 
                                                                             styleClass="cltextbox-number-medium"  style="text-align: right" indexed="true" onchange="<%=javascript%>" readonly="<%=disableFundsRequested%>"  />
                                                                  <html:hidden property="lineItemCost" name="dynaFormData" indexed = "true"/>
                                                              </td>
                                                              <td width = "5%" align = "center">
                                                                  <%
                                                                  totalFunds = totalFunds + ((Double)dynaFormData.get("lineItemCost")).doubleValue();
                                                                  Integer  lineItemNum = (Integer)dynaFormData.get("lineItemNumber");
                                                                  String openLineItem="javaScript:open_line_item("+lineItemNum+")";
                                                                  %>
                                                                  <%  if(!disableEditLink){%>           
                                                                  <html:link href="<%=openLineItem%>" >
                                                                      
                                                                      <bean:message bundle="budget" key="budgetModCumulative.edit"/>
                                                                      <%--<img src="../../../../coeusliteimages/edit.gif"  border="0">--%>
                                                                  </html:link>
                                                                  <% }else {  %>
                                                                  <%openLineItem="javaScript:openLocationWin("+lineItemNum+","+subAwardNumber+")";%>
                                                                  <html:link href="<%=openLineItem%>" >
                                                                      <bean:message bundle="budget" key="budget.View"/>
                                                                      <%--<img src="../../../../coeusliteimages/view1.gif"  border="0">--%>
                                                                  </html:link>
                                                                  <% } %>
                                                              </td>
                                                              <td width = "5%" align = "center">
                                                                  <%  if(disableRemoveLink){%>
                                                                        <bean:message bundle="budget" key="equipment.Remove"/>
                                                                  <% }else{ %>
                                                                  <%
                                                                  
                                                                  String removeLinkScript = "javaScript:removeLineItem('RemoveLineItem.do?lineItemNumber="+lineItemNum+"&budgetPeriod="+budgetPeriodnumber+"','"+isFormulatedLineItem+"')";
                                                                  %>
                                                                  <html:link href="<%=removeLinkScript%>">
                                                                      <bean:message bundle="budget" key="equipment.Remove"/>
                                                                      <%--<img src="../../../../coeusliteimages/trash.gif"  border="0">--%>
                                                                  </html:link>
                                                                  <%}%>
                                                              </td>
                                                          </tr>
                                                          <html:hidden property="budgetPeriod" name="dynaFormData" />
                                                          <html:hidden property="isRowAdded" name="dynaFormData" />
                                                          <html:hidden property="lineItemStartDate" name="dynaFormData"/>
                                                          <html:hidden property="lineItemEndDate" name="dynaFormData"/>
                                                          <html:hidden property="proposalNumber" name="dynaFormData"/>
                                                          <html:hidden property="versionNumber" name="dynaFormData"/>
                                                          <html:hidden property="budgetPeriod" name="dynaFormData"/>
                                                          <html:hidden property="lineItemNumber" name="dynaFormData"/>
                                                          <html:hidden property="budgetCategoryCode" name="dynaFormData"/>
                                                          <html:hidden property="basedOnLineItem" name="dynaFormData"/>
                                                          <html:hidden property="lineItemSequence" name="dynaFormData"/>
                                                          <html:hidden property="costSharingAmount" name="dynaFormData"/>
                                                          <html:hidden property="underRecoveryAmount" name="dynaFormData"/>
                                                          <html:hidden property="onOffCampusFlag" name="dynaFormData"/>
                                                          <html:hidden property="applyInRateFlag" name="dynaFormData"/>
                                                          <html:hidden property="budgetJustification" name="dynaFormData"/>
                                                          <html:hidden property="categoryType" name="dynaFormData"/>
                                                          <html:hidden property="quantity" name="dynaFormData"/>
                                                          <html:hidden property="directCost" name="dynaFormData"/>
                                                          <html:hidden property="indirectCost" name="dynaFormData"/>
                                                          <html:hidden property="totalCostSharing" name="dynaFormData"/>
                                                          <html:hidden property="updateTimestamp" name="dynaFormData"/>
                                                          <html:hidden property="updateUser" name="dynaFormData"/>
                                                          <html:hidden property="costElement" name="dynaFormData"/>
                                                          <% lineItemIndex++; %>
                                                      </logic:iterate>
                                                      <%--</logic:present>--%>
                                                  </table>
                                          </td></tr>
                                          <tr> <td> <br>
                                                  <logic:notEmpty name="lineItemDynaBeanList" property="list" scope="session">                                   
                                                      <table  width="98%"  align="center" border="0" cellpadding="2" cellspacing="0" class="table">
                                                          <!--End iteration here-->
                                                          <tr align='center'>
                                                              <logic:present name="pageConstantValue" scope="session">
                                                                  <logic:equal name="pageConstantValue" value="EQ">
                                                                      <td width = "25%"> 
                                                                          <%--html:img src="<%=spacerImage%>" width="25" height="1"/--%>
                                                                      </td>
                                                                      <td colspan='0'align="right" class='copybold' width = "20%" nowrap>
                                                                          <bean:message bundle="budget" key="equipment.Total"/>
                                                                          <%--html:img src="<%=spacerImage%>" width="4" height="5"/--%>
                                                                          </td>
                                                                  </logic:equal>
                                                                  <logic:equal name="pageConstantValue" value="TR">
                                                                      <td width = "25%">
                                                                          <html:img src="<%=spacerImage%>" width="24" height="1"/>
                                                                      </td>
                                                                      <td colspan='0'align="right" class='copybold' width = "20%" nowrap>
                                                                          <bean:message bundle="budget" key="budgetTravel.Total"/>
                                                                          <%--html:img src="<%=spacerImage%>" width="25" height="5"/--%>
                                                                          </td>
                                                                  </logic:equal>
                                                                  <logic:equal name="pageConstantValue" value="PT">
                                                                      <td width = "20%">
                                                                          <html:img src="<%=spacerImage%>" width="25" height="1"/>
                                                                      </td>
                                                                      <td colspan='0'align="right" class='copybold' width = "25%" nowrap>
                                                                          <bean:message bundle="budget" key="budgetParticipant.Total"/>
                                                                          <%--html:img src="<%=spacerImage%>" width="4" height="5"/--%>
                                                                          </td>
                                                                  </logic:equal>                               
                                                                  <logic:equal name="pageConstantValue" value="ODC">
                                                                      <td width = "25%">
                                                                          <html:img src="<%=spacerImage%>" width="25" height="1"/>
                                                                      </td>
                                                                      <td colspan='0'align="right" class='copybold' width = "20%" nowrap>
                                                                          <bean:message bundle="budget" key="budgetOtherDC.Total"/>
                                                                          <%--html:img src="<%=spacerImage%>" width="4" height="5"/--%>
                                                                          </td>
                                                                  </logic:equal>
                                                              </logic:present>
                                                              <td class='copy' align = "left" width = "29%">
                                                                  <% String output = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(totalFunds); %>                                                   
                                                                  <html:text property="totalEquipment" value="<%=output%>" readonly="true" style="text-align: right" styleClass="cltextbox-equip" />
                                                                  
                                                              </td>
                                                          </tr>
                                                          <tr>
                                                              <td colspan=2>
                                                                  <html:img src="<%=spacerImage%>" width="10" height="2"/>
                                                                  <% if(!readOnly){ %>
                                                                  <html:link href="javascript:proposalBudget_Actions('A')" >
                                                                      <logic:present name="pageConstantValue" scope="session">
                                                                          <logic:equal name="pageConstantValue" value="EQ">
                                                                              <u><bean:message bundle="budget" key="equipment.AddEquipment"/></u>
                                                                              <script>PAGE_NAME = "<bean:message bundle="budget" key="equipmentLabel.budgetEquipment"/>";</script>
                                                                          </logic:equal>
                                                                          <logic:equal name="pageConstantValue" value="TR"> 
                                                                              <u><bean:message bundle="budget" key="travel.NewTravel"/></u>
                                                                              <script>PAGE_NAME = "<bean:message bundle="budget" key="travelLabel.budgetTravel"/>";</script>
                                                                          </logic:equal>
                                                                          <logic:equal name="pageConstantValue" value="PT">
                                                                              <u><bean:message bundle="budget" key="participantTraineeLabel.NewTrainee"/></u>
                                                                              <script>PAGE_NAME = "<bean:message bundle="budget" key="participantTraineeLabel.participantTrainee"/>";</script>
                                                                          </logic:equal>
                                                                          <logic:equal name="pageConstantValue" value="ODC"> 
                                                                              <u><bean:message bundle="budget" key="budgetOtherDC.NewCost"/></u>
                                                                              <script>PAGE_NAME = "<bean:message bundle="budget" key="budgetOtherDC.OtherDirectCosts"/>";</script>
                                                                          </logic:equal>
                                                                      </logic:present>
                                                                      
                                                                  </html:link>
                                                                  <%}else{%>
                                                                  <logic:present name="pageConstantValue" scope="session">
                                                                      <logic:equal name="pageConstantValue" value="EQ">
                                                                          <u><bean:message bundle="budget" key="equipment.AddEquipment"/></u>
                                                                          <script>PAGE_NAME = "<bean:message bundle="budget" key="equipmentLabel.budgetEquipment"/>";</script>
                                                                      </logic:equal>
                                                                      <logic:equal name="pageConstantValue" value="TR"> 
                                                                          <u><bean:message bundle="budget" key="travel.NewTravel"/></u>
                                                                          <script>PAGE_NAME = "<bean:message bundle="budget" key="travelLabel.budgetTravel"/>";</script>
                                                                      </logic:equal>
                                                                      <logic:equal name="pageConstantValue" value="PT">
                                                                          <u><bean:message bundle="budget" key="participantTraineeLabel.NewTrainee"/></u>
                                                                          <script>PAGE_NAME = "<bean:message bundle="budget" key="participantTraineeLabel.participantTrainee"/>";</script>
                                                                      </logic:equal>
                                                                      <logic:equal name="pageConstantValue" value="ODC"> 
                                                                          <u><bean:message bundle="budget" key="budgetOtherDC.NewCost"/></u>
                                                                          <script>PAGE_NAME = "<bean:message bundle="budget" key="budgetOtherDC.OtherDirectCosts"/>";</script>
                                                                      </logic:equal>
                                                                  </logic:present>
                                                                  
                                                                  <%}%>
                                                                  |
                                                                  <% if(!readOnly){ %>
                                                                  <html:link href="javascript:proposalBudget_Actions('C')" >
                                                                      <u><bean:message bundle="budget" key="budgetButton.calculate"/></u>
                                                                  </html:link>
                                                                  <%}else{%>
                                                                  <bean:message bundle="budget" key="budgetButton.calculate"/>
                                                                  <%}%> 
                                                                  <br>
                                                              </td>
                                                          </tr>
                                                      </table>
                                                  </logic:notEmpty>
                                              </td>
                                          </tr>
                                      </table>
                                      
                                  </td>
                              </tr>
                              <tr class='table'>
                                  <td class='savebutton' style='padding-left= 5px;'>
                                      <html:button accesskey="s" property="Save" styleClass="clsavebutton" onclick="proposalBudget_Actions('S')" disabled="<%=readOnly%>">
                                          <bean:message bundle="budget" key="budgetButton.save" />
                                      </html:button>
                                      
                                  </td>
                                  
                              </tr>
                          </table>            
                      </td>
                  </tr>   
                  <tr> <td> &nbsp; </td></tr>
                  
      </table>
</div>

<div id='messageDiv' style='display: none;'>
    <table width="100%" height="100%" align="center" border="0" valign = "top" cellpadding="3" cellspacing="0" class="tabtable">
        <tr>
            <td align='center' class='copyred'>  <br> <bean:message bundle="budget" key="budgetMessages.pleaseWait"/>  </td>
        </tr>
    </table>
</div>
<div id='calculateDiv' style='display: none;'>
    <table width="100%" height="100%" align="center" valign = "top" border="0" cellpadding="3" cellspacing="0" class="tabtable">
        <tr>
            <td align='center' class='copyred'><bean:message bundle="budget" key="budgetMessages.calculatingBudget"/> 
                <br> <bean:message bundle="budget" key="budgetMessages.pleaseWait"/>  
            </td>
        </tr>
    </table>
</div>
<div id='saveDiv' style='display: none;'>
    <table width="100%" height="100%" align="center" valign = "top" border="0" cellpadding="3" cellspacing="0" class="tabtable">
        <tr>
            <td align='center' class='copyred'><bean:message bundle="budget" key="budgetMessages.savingBudget"/> 
                <br> &nbsp;&nbsp;&nbsp;  <bean:message bundle="budget" key="budgetMessages.pleaseWait"/>  
            </td>
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
          LINK = "<%=request.getContextPath()%>/calculateBudget.do?budgetPeriod="+budgetPeriod+"&Save=S";
          FORM_LINK = document.lineItemDynaBeanList;
          function dataChanged(){
                DATA_CHANGED = 'true';
          }
          linkForward(errValue);
    </script>
    <script>
      var help = '<bean:message bundle="budget" key="helpTextBudget.Equipment"/>';
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


