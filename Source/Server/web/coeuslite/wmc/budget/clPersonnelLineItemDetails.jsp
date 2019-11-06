<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="org.apache.struts.validator.DynaValidatorForm,java.util.List,edu.mit.coeuslite.utils.CoeusDynaBeansList"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"  prefix="coeusUtils"%>

<jsp:useBean  id="popUp"  scope="request" class="java.lang.String"/>

<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<%   
    String index = request.getParameter("lineItem");
    String mode = (String)session.getAttribute("mode"+session.getId());
    String budgetStatusMode = (String)session.getAttribute("budgetStatusMode");
    String spacerImage= request.getContextPath()+"/coeusliteimages/spacer.gif";
    boolean readOnly = false;
    if(mode!= null && mode.equals("display")){
       readOnly = true;
   }else if(budgetStatusMode != null && budgetStatusMode.equals("complete")){
        readOnly = true;
    }
    CoeusDynaBeansList coeusLineItemDynaList = (CoeusDynaBeansList)session.getAttribute("lineItemDynaBeanList");
   Integer sessionPeriodNumber = (Integer)request.getAttribute("period");
   String hasMultiplePersons = (String)session.getAttribute("hasMultiplePersons");
%>

<html:html>
  <head>  
    <title>Line Item</title>
    <html:base/>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
    <link rel=stylesheet href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/ipf.css" type="text/css">
    <link href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css" rel="stylesheet" type="text/css">
            <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">            
   
   
   <script language="javascript" type="text/JavaScript" src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/coeusLiteCalendar.js"></script>
   <script>
   <% boolean isSavePerformed = false;%>
         function calculate_budget_later(){
            var budgetPeriod = "<%=sessionPeriodNumber%>";
            var lineItem = "<%=index%>";
            var  value = validate();
            if(value == ""){
	            document.budgetPersonnelLineItemDynaBean.action = "<%=request.getContextPath()%>/applyToPersonnelLaterPeriods.do?budgetPeriod="+budgetPeriod+"&requestBudgetPeriod="+budgetPeriod+"&Save=S"+"&reCalculate=Y";
      	      document.budgetPersonnelLineItemDynaBean.submit();
	            <% isSavePerformed = true;%>
	             // Hide the code in first div tag
	            document.getElementById('lineItemDiv').style.display = 'none';
	             // Display code in second div tag
	            document.getElementById('saveDiv').style.display = 'block';       
            }else{
                  alert(value);
		}
         }
         function calculate_budget_current(){  
           var budgetPeriod = "<%=sessionPeriodNumber%>";
            var lineItem = "<%=index%>";
            document.budgetPersonnelLineItemDynaBean.action = "<%=request.getContextPath()%>/applyToPersonnelCurrentPeriods.do?budgetPeriod="+budgetPeriod+"&requestBudgetPeriod="+budgetPeriod+"&Save=S"+"&reCalculate=Y";
            document.budgetPersonnelLineItemDynaBean.submit();
            <% isSavePerformed = true;%>
             // Hide the code in first div tag
            document.getElementById('lineItemDiv').style.display = 'none';
             // Display code in second div tag
            document.getElementById('saveDiv').style.display = 'block';       
         }
         function close_action(){
            
            //var budgetPeriod = "<%=sessionPeriodNumber%>";
            document.budgetPersonnelLineItemDynaBean.action = "<%=request.getContextPath()%>/getBudgetPersonnel.do";
            document.budgetPersonnelLineItemDynaBean.submit();
            window.close();
         }
        function validate(){
            var value;
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
       function lineItemChecked(index){
            var applyRateFlag= "beanList["+index+"].applyRateFlag";
            val = document.getElementsByName(applyRateFlag);
            val = val[0].checked;
		for(i=0; i < document.forms[0].elements.length; i++){
			if(document.forms[0].elements[i].name == applyRateFlag) {	
				document.forms[0].elements[i].value = val;
			}
	  	}

        }
      function intOnly(formFieldName) {
        if(formFieldName.value.length>0) {
            formFieldName.value = formFieldName.value.replace(/[^\d]+/g, ''); 
        }
      }
     </script>
  </head>
  <body>
    <html:form action="/getPersonnelLineItemDetails.do"  method="POST"> 
  	  <logic:equal scope="request" name="popUp" value="close">
            <script>
            	var page = "<%=session.getAttribute("pageConstantValue")%>";
            	var budgetPeriod = "<%=sessionPeriodNumber%>";
			var params = "budgetPeriod="+budgetPeriod+"&OldPeriod="+budgetPeriod;
			params = params + "&reloadData=TRUE";
	      	parent.opener.location="<%=request.getContextPath()%>/getBudgetPersonnel.do?" + params;
            	self.close();
            </script>
         </logic:equal>
        <!-- New Template for clLineItemDetails Start  -->
        <div id='lineItemDiv'>
        <table width="100%" height="100%"  border="0" cellpadding="0" cellspacing="0" class="table">       
              <tr>
                <td  align="left" valign="top">
                    <table width="100%" height="20"  border="0" cellpadding="0" cellspacing="0" class="tabtable">
                        <tr>
                         <%-- <td class='table' align="left"><b> <bean:message bundle="budget" key="budgetLineItemDetails.personnelLineItemDetailsHeader"/></b></td> --%>
                          <td class='table' ><b><font color="red">
                                    <html:errors header="" footer = ""/>
                                    <logic:messagesPresent message = "true">                                       
                                                    <html:messages id="message" message="true" property="budget_period_exceptionCode.1159" bundle="budget">
                                                        <li><bean:write name = "message"/></li>
                                                    </html:messages>   
                                                    <html:messages id="message" message="true" property="budget_period_exceptionCode.1156" bundle="budget">
                                                        <li><bean:write name = "message"/></li>
                                                    </html:messages>   
                                                    <html:messages id="message" message="true" property="budget_period_exceptionCode.1153" bundle="budget">
                                                        <li><bean:write name = "message"/></li>
                                                    </html:messages>
                                                    <html:messages id="message" message="true" property="budget_period_exceptionCode.1160" bundle="budget">
                                                        <li><bean:write name = "message"/></li>
                                                    </html:messages>
                                                    <html:messages id="message" message="true" property="budget_period_exceptionCode.1157" bundle="budget">
                                                        <li><bean:write name = "message"/></li>
                                                    </html:messages>
                                                    <html:messages id="message" message="true" property="budget_period_exceptionCode.1158" bundle="budget">
                                                        <li><bean:write name = "message"/></li>
                                                    </html:messages>
                                                    <html:messages id="message" message="true" property="budget_common_exceptionCode.1011" bundle="budget">                
                                                        <li><bean:write name = "message"/></li>
                                                    </html:messages>
                                                    <html:messages id="message" message="true" property="adjustPeriod_exceptionCode.1459" bundle="budget">                
                                                        <li><bean:write name = "message"/></li>
                                                    </html:messages>
                                                    <html:messages id="message" message="true" property="adjustPeriod_exceptionCode.1460" bundle="budget">                
                                                        <li><bean:write name = "message"/></li>
                                                    </html:messages>
                                    </logic:messagesPresent>                   
                            </font></b> 
                          </td>
                        </tr>
                    </table>
                </td>
                </tr>
                <tr>
                     <td colspan='6' class="tabtable">
                      <table align="left" width="100%"  border="0"  cellpadding="0" class='tabtable'>
                        <tr>
                            <%if(!readOnly){%>
                            <td width="35%" align="center" class="copybold" nowrap>
                                <%--<html:button property="Submit"  styleClass="clbutton" onclick="calculate_budget_current()" disabled="<%=readOnly%>"> 
                                    <bean:message bundle="budget" key="budgetLineItemDetailsButton.saveAndApplyToCurrentPeriod"/> 
                                </html:button>--%>
                                <html:link href="javascript:calculate_budget_current()"> 
                                    <u><bean:message bundle="budget" key="budgetLineItemDetailsButton.saveAndApplyToCurrentPeriod"/></u>
                                </html:link>
                            </td>
                            <td align="center" class="copybold">
                                <%--<html:button property="Submit"  styleClass="clbutton" onclick="calculate_budget_later()" disabled="<%=readOnly%>">
                                     <bean:message bundle="budget" key="budgetLineItemDetailsButton.saveAndApplyToCurrentAndLaterPeriods"/> 
                                </html:button>--%>
                                <html:link href="javascript:calculate_budget_later();">
                                     <u><bean:message bundle="budget" key="budgetLineItemDetailsButton.saveAndApplyToCurrentAndLaterPeriods"/></u> 
                                </html:link>
                            </td>
                            
                            <%}else{%>
                            <td align="center" class="copybold">
                                <bean:message bundle="budget" key="budgetLineItemDetailsButton.saveAndApplyToCurrentPeriod"/> 
                            </td>
                            <td align="center" class="copybold">
                                <bean:message bundle="budget" key="budgetLineItemDetailsButton.saveAndApplyToCurrentAndLaterPeriods"/> 
                            </td>
                            <%}%>
                            <td align="center" class="copybold">
                                <%--<html:button property="Submit"  styleClass="clbutton" onclick="close_action()">
                                    <bean:message bundle="budget" key="budgetLineItemDetailsButton.close"/> 
                                </html:button>--%>
                                <html:link href="javascript:close_action();">
                                    <u><bean:message bundle="budget" key="budgetLineItemDetailsButton.close"/> </u>
                                </html:link>
                            </td>
                        </tr>
                        </table>
                    </td>
                  </tr>
                  <tr>
                      <td>
                          <table width="100%"  border="0" cellpadding="0" cellspacing="2" class="tabtable">
                              <logic:iterate id="list" name="budgetPersonnelLineItemDynaBean" property="list" type="org.apache.struts.action.DynaActionForm" indexId="ctrr">
                                              <tr>
                                                  <td align="right" class="copybold" nowrap><bean:message bundle="budget" key="personnel.fullName"/></td>      
                                                  <td align="left" class="copy" nowrap colspan="5"> 
                                                      <html:text styleClass="textbox-longer" disabled="true" maxlength="80" name="list" property="fullName" indexed="true" />
                                                  </td>
                                              </tr>
                                              <tr>
                                                  <td align="right" class="copybold" nowrap ><bean:message bundle="budget" key="personnel.projectRole"/></td>
                                                  <td align="left" class="copy" colspan="2">
                                                      <html:text maxlength="50" styleClass="textbox-long" disabled="true" name="list" property="projectRole" indexed="true"/>
                                                  </td>
                                                  <td align="left" class="copy" colspan="3">
                                                      <html:text maxlength="50" styleClass="textbox-long" disabled="<%=readOnly%>" name="list" property="projectRole" indexed="true" />
                                                  </td>
                                              </tr>
                                              <tr>
                                                  <td align="right" class="copybold" nowrap><bean:message bundle="budget" key="budgetLineItemDetailsLabel.description"/></td>   
                                                  <td align="left" class="copy" colspan="5"> 
                                                      <html:text styleClass="textbox-longer" disabled="<%=readOnly%>" maxlength="80" name="list" property="lineItemDescription" indexed="true"/>
                                                  </td>
                                              </tr>
                                              <tr>
                                                  <td align="right" class="copybold" nowrap><bean:message bundle="budget" key="budgetLineItemDetailsLabel.startDate"/></td>   
                                                  <td align="left" class="copy" nowrap> 
                                                      <!-- Commented/Added for case#3696 -start -->
                                                      <%--  
                                                      <html:text maxlength="10" size="10" styleClass="cltextbox" readonly="<%=readOnly%>" name="list" property="tempLineItemStartDate" indexed="true"/>
                                                      --%>
                                                      <html:text maxlength="10" size="10" styleClass="cltextbox" disabled="true" name="list" property="tempLineItemStartDate" indexed="true"/>
                                                      <html:hidden name="list" property="startDate" indexed="true"/>                                                      
                                                      </td>
                                                  <td align="right" class="copybold" nowrap><bean:message bundle="budget" key="budgetLineItemDetailsLabel.endDate"/></td>
                                                  <td align="left" class="copy" >
                                                      <%--
                                                      <html:text maxlength="10" size="10" styleClass="cltextbox" readonly="<%=readOnly%>" name="list" property="tempLineItemEndDate" indexed="true"/>
                                                      --%>
                                                      <html:text maxlength="10" size="10" styleClass="cltextbox" disabled="true" name="list" property="tempLineItemEndDate" indexed="true"/>                                                      
                                                      <!-- Commented/Added for case#3696 -end -->
                                                      <html:hidden name="list" property="endDate" indexed="true"/>                                                      
                                                  </td>
                                              </tr>
                                              <tr>
                                                  <td align="right" class="copybold"><bean:message bundle="budget" key="budgetLineItemDetailsLabel.cost"/></td>   
                                                  <td align="left" class="copy">
                                                      <%   double salRequested = ((Double)list.get("salaryRequested")).doubleValue();
                                                      String salReq = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(salRequested);                                         
                                                      %>  
                                                      <html:text size="15" styleClass="cltextbox" style="text-align: right" disabled="true" name="list" property="salaryRequested" value="<%=salReq%>" indexed="true"/>
                                                      
                                                  </td>
                                                  <td align="right" class="copybold" nowrap><bean:message bundle="budget" key="budgetLineItemDetailsLabel.costSharing"/></td>   
                                                  <td align="left" class="copy">
                                                      <%   double costShare = ((Double)list.get("costSharingAmount")).doubleValue();
                                                      String costSharingAmt = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(costShare);                                         
                                                      %>  
                                                      <html:text size="15" styleClass="cltextbox" style="text-align: right" disabled="true" name="list" property="costSharingAmount" value="<%=costSharingAmt%>" indexed="true"/>
                                                  </td>
                                                  <td align="right" class="copybold"><bean:message bundle="budget" key="budgetLineItemDetailsLabel.underrecovery"/></td>   
                                                  <td align="left" class="copy">
                                                      <%   double underRecovery = ((Double)list.get("underRecoveryAmount")).doubleValue();
                                                      String underRecoveryAmt = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(underRecovery);                                         
                                                      %>  
                                                      <html:text size="15" styleClass="cltextbox" style="text-align: right" disabled="true" name="list" property="underRecoveryAmount" value="<%=underRecoveryAmt%>" indexed="true"/>
                                                  </td>
                                              </tr>
                                              <tr>
                                                  <td align="right" class="copybold" nowrap><bean:message bundle="budget" key="budgetLineItemDetailsLabel.applyInflation"/></td>   
                                                  <td align="left" class="copy">                                                                                    
                                                      <html:checkbox  disabled="true" name="list" property="applyInRateFlag"  indexed="true"/>
                                                  </td>
                                                  <td align="right" class="copybold" nowrap><bean:message bundle="budget" key="budgetLineItemDetailsLabel.onCampus"/></td>   
                                                  <td align="left" class="copy">
                                                      <% if(hasMultiplePersons !=null && hasMultiplePersons.equals("Yes")){
                                                          %>
                                                          <html:checkbox name="list" property="onOffCampusFlag" indexed="true" disabled="true"/>
                                                     <% } else{ %>     
                                                          <html:checkbox name="list" property="onOffCampusFlag" indexed="true" disabled="<%=readOnly%>"/> 
                                                      <% } %>    
                                                  </td>
                                                  <td align="right" class="copybold" nowrap><bean:message bundle="budget" key="budgetLineItemDetailsLabel.SubmitCostSharingFlag"/></td>   
                                                  <td align="left" class="copy">                                                                                    
                                                       <html:checkbox name="list"  property="submitCostSharingFlag"  value="Y" indexed="true" disabled="<%=readOnly%>"/>
                                                       <%-- <html:checkbox name="list"  property="tempSubmitCostSharingFlag" indexed="true" disabled="<%=readOnly%>"/> --%>
                                                  </td>
                                                  
                                              </tr>
                              </logic:iterate>
                          </table>
                      </td>
                  </tr>
                        
                  <tr>
                  <td>
                  <br>
                 <table width="100%"border="0" cellpadding="0" class="tabtable">
                  <tr>
                    <td colspan="4" align="left" valign="top">
                        <table width="100%" height="20"  border="0" cellpadding="0" cellspacing="0" class="tableheader">
                        <tr>
                          <td><bean:message bundle="budget" key="budgetLineItemDetails.ratesApplicabletoLineItemHeader"/>  </td>
                        </tr>
                        </table>
                    </td>
                  </tr>
                  
                  <tr align=left>
                    <td colspan="4" class="tabtable" align="center" >
                            <!--DIV STYLE="overflow: auto; width: 740px; height: 125;padding:0px; margin: 0px"-->
                        <table width="100%"border="0" cellpadding="0" class="tabtable">
                            <tr>
                                <td width="15%" align="left" class="theader"><bean:message bundle="budget" key="budgetLineItemDetailstableHeader.rateTypes"/></td>
                                <td width="5%" align="center" class="theader"><bean:message bundle="budget" key="budgetLineItemDetailstableHeader.apply"/></td>
                                <td width="10%" align="left" class="theader"><bean:message bundle="budget" key="budgetLineItemDetailstableHeader.cost"/></td>
                                <td width="10%" align="left" class="theader"><bean:message bundle="budget" key="budgetLineItemDetailstableHeader.costSharing"/></td>
                            </tr>
                            <logic:notPresent name="budgetPersonnelLineItemDynaBean" scope = "session">
                             <tr>
                                 <td>
                                      <table width="95%" align="right" border="0">
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
                         </logic:notPresent>   

                                  <% 
                                      try{
                                      String strBgColor = "#EFEFEF"; //BGCOLOR=EFEFEF  FBF7F7
                                      int count = 0;%>
                                  <logic:present name="budgetPersonnelLineItemDynaBean" scope="session">
                                  <logic:iterate id="beanList" name="budgetPersonnelLineItemDynaBean" property="beanList" type="org.apache.struts.action.DynaActionForm" indexId="ctr">
                                   <% if (count%2 == 0) 
                                        strBgColor = "FBF7F7"; 
                                     else 
                                        strBgColor="EFEFEF";%>
                                    <tr bgcolor="<%=strBgColor%>" valign="top" class="rowLine">
                                        <td align="left" width='24%' nowrap bgcolor='#D1E5FF'>
                                            <bean:write name='beanList' property="rateTypeDescription"/></td>                                          
                                        <td align="center" class="cltextbox-equip" width='5%' >                                                                                              
					<%
                                         String javascript = "lineItemChecked('"+ctr+"')";
					%>
							
                                        <!-- Commented/Added for case#3696 -start -->
                                        <%--
						    <html:checkbox name="beanList" property="applyRateFlag" disabled="<%=readOnly%>" indexed="true" onclick="<%=javascript%>"/>
                                                    --%>
                                        <html:checkbox name="beanList" property="applyRateFlag" disabled="true" indexed="true" onclick="<%=javascript%>"/>            
                                        <!-- Commented/Added for case#3696 -end -->
                                        <html:hidden name="beanList" property="applyRateFlag" indexed="true"/>
                                       </td>
                                       <td align="left" bgcolor='#D1E5FF' width='10%'> 
                                       
                                        <% String calculatedCost = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(beanList.get("calculatedCost")); %>
                                        <html:text property="strcalculatedCost" value="<%=calculatedCost%>" readonly="true" styleClass="cltextbox-equip"/>
                                        </td>
                                        <td align="left" nowrap class="cltextbox-equip" width='15%'>   
                                         <% calculatedCost = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(beanList.get("calculatedCostSharing")); %>                                
                                        <html:text property="strcalculatedCostSharing" value="<%=calculatedCost%>" readonly="true" styleClass="cltextbox-equip"/>

                                        </td>

                                    </tr>
                                  <%count++;%>
                                  </logic:iterate>
                                  </logic:present>
                                  <%}catch(Exception ex){
                                      ex.printStackTrace();
                                    }%>
                        </table>
                    </td>
                    </tr>
                            <!--/div-->
                            </table>
                    </td>
                    </tr>
             <%--      <tr>
                    <td colspan='6' class='tabtable'>
                        <table align="left" width="100%"  border="0"  cellpadding="0" class='tabtable'>
                        <tr>
                            <%if(!readOnly){%>
                            <td width="35%" align="center" class="copybold">
                                
                                <html:link href="javascript:calculate_budget_current()"> 
                                    <u><bean:message bundle="budget" key="budgetLineItemDetailsButton.saveAndApplyToCurrentPeriod"/></u>
                                </html:link>
                            </td>
                            <td width="35%" align="left" class="copybold">
                               
                                <html:link href="javascript:calculate_budget_later();">
                                     <u><bean:message bundle="budget" key="budgetLineItemDetailsButton.saveAndApplyToCurrentAndLaterPeriods"/></u>
                                </html:link>
                            </td>
                            <%}else{%>
                            <td width="35%" align="center" class="copybold">
                                <bean:message bundle="budget" key="budgetLineItemDetailsButton.saveAndApplyToCurrentPeriod"/> 
                            </td>
                            <td width="35%" align="left" class="copybold">
                                <bean:message bundle="budget" key="budgetLineItemDetailsButton.saveAndApplyToCurrentAndLaterPeriods"/> 
                            </td>
                            <%}%>
                            <td width="5%" align="center" class="copybold">
                                
                                <html:link href="javascript:close_action();">
                                    <u><bean:message bundle="budget" key="budgetLineItemDetailsButton.close"/> </u>
                                </html:link>
                            </td>
                        </tr>
                        </table>
                    </td>
                    </tr> --%>
                    <tr>
                     <td colspan='6' class="tabtable">
                      <table align="left" width="100%"  border="0"  cellpadding="0" class='tabtable'>
                        <tr>
                            <%if(!readOnly){%>
                            <td width="35%" align="center" class="copybold" nowrap>
                                <%--<html:button property="Submit"  styleClass="clbutton" onclick="calculate_budget_current()" disabled="<%=readOnly%>"> 
                                    <bean:message bundle="budget" key="budgetLineItemDetailsButton.saveAndApplyToCurrentPeriod"/> 
                                </html:button>--%>
                                <html:link href="javascript:calculate_budget_current()"> 
                                    <u><bean:message bundle="budget" key="budgetLineItemDetailsButton.saveAndApplyToCurrentPeriod"/></u>
                                </html:link>
                            </td>
                            <td align="center" class="copybold">
                                <%--<html:button property="Submit"  styleClass="clbutton" onclick="calculate_budget_later()" disabled="<%=readOnly%>">
                                     <bean:message bundle="budget" key="budgetLineItemDetailsButton.saveAndApplyToCurrentAndLaterPeriods"/> 
                                </html:button>--%>
                                <html:link href="javascript:calculate_budget_later();">
                                     <u><bean:message bundle="budget" key="budgetLineItemDetailsButton.saveAndApplyToCurrentAndLaterPeriods"/></u> 
                                </html:link>
                            </td>
                            
                            <%}else{%>
                            <td align="center" class="copybold">
                                <bean:message bundle="budget" key="budgetLineItemDetailsButton.saveAndApplyToCurrentPeriod"/> 
                            </td>
                            <td align="center" class="copybold">
                                <bean:message bundle="budget" key="budgetLineItemDetailsButton.saveAndApplyToCurrentAndLaterPeriods"/> 
                            </td>
                            <%}%>
                            <td align="center" class="copybold">
                                <%--<html:button property="Submit"  styleClass="clbutton" onclick="close_action()">
                                    <bean:message bundle="budget" key="budgetLineItemDetailsButton.close"/> 
                                </html:button>--%>
                                <html:link href="javascript:close_action();">
                                    <u><bean:message bundle="budget" key="budgetLineItemDetailsButton.close"/> </u>
                                </html:link>
                            </td>
                        </tr>
                        </table>
                    </td>
                  </tr>
                    <tr>
                    <td align="left" valign="top">&nbsp;</td>
                    </tr>
                    </table>
                    </div>
                    <div id='saveDiv' style='display: none;'>
                        <table width="100%" height="100%" align="center" border="0" cellpadding="3" cellspacing="0" class="tabtable">
                            <tr>
                                <td align='center' class='copyred'> <bean:message bundle="budget" key="budgetMessages.saving"/>  
                                    <br> <bean:message bundle="budget" key="budgetMessages.pleaseWait"/>  
                                </td>
                            </tr>
                        </table>
                    </div>
                <!-- New Template for clLineItemDetails End  -->
                </html:form>
</body>
</html:html>
