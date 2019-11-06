<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="org.apache.struts.validator.DynaValidatorForm,java.util.List,edu.mit.coeuslite.utils.CoeusDynaBeansList"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"  prefix="coeusUtils"%>

<jsp:useBean  id="budgetDetails" scope="request" class="org.apache.struts.validator.DynaValidatorForm"/>
<jsp:useBean  id="budgetDetailCalAmts" scope="session" class="edu.mit.coeus.utils.CoeusVector"/>

<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<bean:size id="budgetDetailCalAmtSize" name="budgetDetailCalAmts"/>
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
    boolean isSubAwardLineItem = false;
    if(!readOnly && request.getParameter("subAwardNumber") != null){
        int subAwardNumber = Integer.parseInt((String)request.getParameter("subAwardNumber"));
        if(subAwardNumber > 0){
            isSubAwardLineItem = true;
        }else{
            isSubAwardLineItem = readOnly;
        }
    }else{
         isSubAwardLineItem = readOnly;
    }
    CoeusDynaBeansList coeusLineItemDynaList = (CoeusDynaBeansList)session.getAttribute("lineItemDynaBeanList");
    List beanList = coeusLineItemDynaList.getBeanList();
    Integer sessionPeriodNumber = (Integer)beanList.get(0);
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
             var  value = validate();
             if(value == ""){
               //var budgetPeriod = "<%=request.getAttribute("period")%>";
               var budgetPeriod = "<%=sessionPeriodNumber%>";
               
               var lineItem = "<%=index%>";
               document.lineItemDetails.action = "<%=request.getContextPath()%>/applyToLaterPeriods.do?budgetPeriod="+budgetPeriod+"&lineItem="+lineItem;
                <% isSavePerformed  = true;%>
                document.lineItemDetails.submit();
             }else{
                 alert(value);
                  //var budgetPeriod = "<%=request.getAttribute("period")%>";
                  var budgetPeriod = "<%=sessionPeriodNumber%>";
                  var lineItem = "<%=index%>";
                  document.lineItemDetails.action = "<%=request.getContextPath()%>/applyToLaterPeriods.do?budgetPeriod="+budgetPeriod+"&lineItem="+lineItem;
                  <% isSavePerformed = true;%>
                  document.lineItemDetails.submit();
             }
                 // Hide the code in first div tag
                 document.getElementById('lineItemDiv').style.display = 'none';
                 // Display code in second div tag
                document.getElementById('saveDiv').style.display = 'block';     
         }
         function calculate_budget_current(){  
           // var budgetPeriod = "<%=request.getAttribute("period")%>";
           var budgetPeriod = "<%=sessionPeriodNumber%>";
            var lineItem = "<%=index%>";
            document.lineItemDetails.action = "<%=request.getContextPath()%>/applyToCurrentPeriods.do?budgetPeriod="+budgetPeriod+"&lineItem="+lineItem;
            <% isSavePerformed = true;%>
            document.lineItemDetails.submit();
             // Hide the code in first div tag
            document.getElementById('lineItemDiv').style.display = 'none';
             // Display code in second div tag
            document.getElementById('saveDiv').style.display = 'block';       
         }
         function close_action(){
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
       
      function intOnly(formFieldName) {
        if(formFieldName.value.length>0) {
            formFieldName.value = formFieldName.value.replace(/[^\d.]+/g, '');
        }
      }
     </script>
  </head>
  <body>
    <html:form action="/getLineItemDetails.do"  method="post"> 
        <logic:equal name="lineItemDetails" property="popUp" value="close"> 
             <script>
              var page = "<%=session.getAttribute("pageConstantValue")%>";
            // var budgetPeriod = "<%=request.getAttribute("period")%>";
             var budgetPeriod = "<%=sessionPeriodNumber%>";
            parent.opener.location="<%=request.getContextPath()%>/getBudgetEquipment.do?Period="+budgetPeriod+"&PAGE="+page;
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
                          <td class='table' align="left"><b> <bean:message bundle="budget" key="budgetLineItemDetails.lineItemDetailsHeader"/></b></td> 
                             <td class='table' nowrap><b><font color="red">
                                    <html:errors header=" " footer = " "/>
                                    <logic:messagesPresent message = "true">                                       
                                                    <html:messages id="message" message="true" property="error.lineItemDetails.lineItemCost" bundle="budget">
                                                        <li><bean:write name = "message"/></li>
                                                    </html:messages>   
                                                    <html:messages id="message" message="true" property="error.lineItemDetails.costSharingAmount" bundle="budget">
                                                        <li><bean:write name = "message"/></li>
                                                    </html:messages>   
                                                    <html:messages id="message" message="true" property="error.lineItemDetails.underRecovery" bundle="budget">
                                                        <li><bean:write name = "message"/></li>
                                                    </html:messages>   
                                                    <!-- Added for Case #3132 - start
                                                         Changing quantity field from integer to float-->
                                                    <html:messages id="message" message="true" property="error.lineItemDetails.quantity" bundle="budget">
                                                        <li><bean:write name = "message"/></li>
                                                    </html:messages>   
                                                    <!-- Added for Case #3132 - end-->
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
                            <td width="35%" height='25' align="center" class="copybold" nowrap valign=middle>
                                <%--<html:button property="Submit"  styleClass="clbutton" onclick="calculate_budget_current()" disabled="<%=readOnly%>"> 
                                    <bean:message bundle="budget" key="budgetLineItemDetailsButton.saveAndApplyToCurrentPeriod"/> 
                                </html:button>--%>
                                <html:link href="javascript:calculate_budget_current()"> 
                                    <u><bean:message bundle="budget" key="budgetLineItemDetailsButton.saveAndApplyToCurrentPeriod"/></u>
                                </html:link>
                            </td>
                            <td width="35%" height='25' align="left" class="copybold" valign=middle>
                                <%--<html:button property="Submit"  styleClass="clbutton" onclick="calculate_budget_later()" disabled="<%=readOnly%>">
                                     <bean:message bundle="budget" key="budgetLineItemDetailsButton.saveAndApplyToCurrentAndLaterPeriods"/> 
                                </html:button>--%>
                                
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
                            <td width="5%" height='25' align="center" class="copybold" valign=middle>
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
                    <td class="tabtable">
                        <table width="100%"  border="0" cellpadding="0" >
                             <tr>
                                <td align="right" class="copybold"><bean:message bundle="budget" key="budgetLineItemDetailsLabel.description"/></td>   
                                <td align="left" nowrap colspan="5"> 
                                    <html:text property="lineItemDescription"  styleClass="textbox-longer" style="width: 635px;" readonly="<%=isSubAwardLineItem%>" disabled="<%=readOnly%>" maxlength="80"/>
                                </td>
                            </tr>
                         <!--</table>
                    </td>
                  </tr>
                  <tr>
                      <td class="tabtable" colspan='6'>
                        <table width="100%"  border="0" cellpadding="0" >-->
                               <tr>
                                    <td align="right" class="copybold" nowrap><bean:message bundle="budget" key="budgetLineItemDetailsLabel.startDate"/></td>   
                                    <td align="left" class="copy"> 
                                       <html:text property="tempLineItemStartDate" maxlength="10" size="10" styleClass="cltextbox" readonly="<%=isSubAwardLineItem%>" disabled="<%=readOnly%>"/>
                                        <html:hidden property="lineItemStartDate"/>

                                    </td>
                                    <td align="right" class="copybold"><bean:message bundle="budget" key="budgetLineItemDetailsLabel.endDate"/></td>
                                    <td align="left" class="copy" nowrap >
                                    <html:text property="tempLineItemEndDate" maxlength="10" size="10" styleClass="cltextbox" readonly="<%=isSubAwardLineItem%>" disabled="<%=readOnly%>"/>
                                       <html:hidden property="lineItemEndDate"/>

                                    </td>
                                    <td align="right" class="copybold"><bean:message bundle="budget" key="budgetLineItemDetailsLabel.quantity"/></td>   
                                    <td align="left" class="copy">
                                        <!--Modified for Case #3132 - start
                                            Changing quantity field from integer to float-->
                                        <%-- <html:text property="quantity" maxlength="7" size="15" styleClass="cltextbox" disabled="<%=readOnly%>"  onchange="intOnly(this);" 
                                                onkeyup="intOnly(this);" onkeypress="intOnly(this);"/> --%>
                                        <html:text property="quantity" size="15" styleClass="cltextbox" readonly="<%=isSubAwardLineItem%>" disabled="<%=readOnly%>"/>
                                        <!--Modified for Case #3132 - end-->
                                    </td>
                                </tr>
                            <!--</table>
                      </td>
                  </tr>
                  <tr>
                      <td colspan='6' class="tabtable">
                        <table width="100%"  border="0" cellpadding="0" >-->
                            <tr>
                                <td align="right" class="copybold"><bean:message bundle="budget" key="budgetLineItemDetailsLabel.cost"/>&nbsp;</td>   
                                <td align="left" class="copy">
                                    <html:text property="lineItemCost" size="15" styleClass="cltextbox" style="text-align: right" readonly="<%=isSubAwardLineItem%>" disabled="<%=readOnly%>" />

                                </td>
                                <td align="right" class="copybold" nowrap><bean:message bundle="budget" key="budgetLineItemDetailsLabel.costSharing"/></td>   
                                <td align="left" class="copy">
                                    <html:text property="costSharingAmount" size="15" styleClass="cltextbox" style="text-align: right" readonly="<%=isSubAwardLineItem%>" disabled="<%=readOnly%>" />
                                </td>
                                <td align="right" class="copybold"><bean:message bundle="budget" key="budgetLineItemDetailsLabel.underrecovery"/></td>   
                                <td align="left" class="copy">
                                    <html:text property="underRecoveryAmount" size="15" styleClass="cltextbox" style="text-align: right" disabled="true"/>
                                </td>
                            </tr>
                        <!--</table>
                      </td>
                  </tr>
                  
                  <tr>
                      <td colspan='6' class="tabtable">
                        <table width="100%"  border="0" cellpadding="0" >-->
                            <tr>
                                <td align="right" class="copybold" nowrap><bean:message bundle="budget" key="budgetLineItemDetailsLabel.applyInflation"/></td>   
                                <td align="left" class="copy">                                                                                    
                                    <html:checkbox  property="applyInRateFlag" disabled="<%=isSubAwardLineItem%>"/>
                                </td>
                                <td align="right" class="copybold" nowrap><bean:message bundle="budget" key="budgetLineItemDetailsLabel.onCampus"/></td>   
                                <td align="left" class="copy">
                                    <html:checkbox  property="onOffCampusFlag" disabled="<%=isSubAwardLineItem%>"/>
                                </td>
                                 <td align="right" class="copybold" nowrap><bean:message bundle="budget" key="budgetLineItemDetailsLabel.SubmitCostSharingFlag"/></td>   
                                <td align="left" class="copy">                                                                                    
                                    <html:checkbox name ="lineItemDetails" property="submitCostSharingFlag"  value="Y"  disabled="<%=readOnly%>"/>
                                    
                                </td>
                                                                 
                                    
                                </td>                                
                            </tr>
                        </table>
                      </td>
                  </tr>

                    <html:hidden property="budgetPeriod"/>
                     <html:hidden property="lineItemNumber"/>
                     <html:hidden property="costElement"/>
                     <html:hidden property="indirectCost"/>
                     <html:hidden property="directCost"/>
                     <html:hidden property="proposalNumber"/>
                     <html:hidden property="versionNumber"/>
                     <html:hidden property="lineItemSequence"/>                                                                         
                     <html:hidden property="basedOnLineItem"/>
                     <html:hidden property="totalCostSharing"/>
                     <html:hidden property="budgetCategoryCode"/> 
                    <html:hidden property="updateTimestamp"/>                  
                  <tr>
                  <td>
                  <br>
                 <table width="100%"border="0" cellpadding="0" class="tabtable">
                  <tr>
                    <td colspan="5" align="left" valign="top">
                        <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableheader">
                        <tr>
                          <td><bean:message bundle="budget" key="budgetLineItemDetails.ratesApplicabletoLineItemHeader"/>  </td>
                        </tr>
                        </table>
                    </td>
                  </tr>
                  
                  <tr align=left>
                    <td colspan="5" class="tabtable" align="center" >
                            <!--DIV STYLE="overflow: auto; width: 740px; height: 125;padding:0px; margin: 0px"-->
                        <table width="100%"border="0" cellpadding="0" class="tabtable">
                            <tr>
                                <td width="15%" height='20' align="left" class="theader"><bean:message bundle="budget" key="budgetLineItemDetailstableHeader.rateTypes"/></td>
                                <td width="5%" align="center" class="theader"><bean:message bundle="budget" key="budgetLineItemDetailstableHeader.apply"/></td>
                                <td width="10%" align="left" class="theader"><bean:message bundle="budget" key="budgetLineItemDetailstableHeader.cost"/></td>
                                <td width="10%" align="left" class="theader"><bean:message bundle="budget" key="budgetLineItemDetailstableHeader.costSharing"/></td>
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

                                  <% String strBgColor = "#EFEFEF"; //BGCOLOR=EFEFEF  FBF7F7
                                      int count = 0;%>
                                  <logic:present name="budgetDetailCalAmts">
                                  <logic:iterate id="calAmts" name="budgetDetailCalAmts"
                                                 type="org.apache.struts.validator.DynaValidatorForm">

                                   <% if (count%2 == 0) 
                                        strBgColor = "FBF7F7"; 
                                     else 
                                        strBgColor="EFEFEF";%>
                                    <tr bgcolor="<%=strBgColor%>" valign="top" >
                                        <td align="left" width='24%' nowrap bgcolor='#D1E5FF'>
                                            <bean:write name='calAmts' property="rateTypeDescription"/></td>
                                          <%--<html:text  name="calAmts" property="rateTypeDescription" readonly="true" /> --%>
                                        <td align="center" class="cltextbox-equip" width='5%' >                                                                                              


                                        <% 
                                            //This logic is used for getting selected and unselected checkboxes with value 0--caAmts size
                                            boolean flagValue = ((Boolean)calAmts.get("applyRateFlag")).booleanValue(); 
                                            String value =""; 
                                            String checked ="";
                                            //COEUSQA-4060
                                           if(flagValue){value="true";
                                           %>                                            
                                            <input type="checkbox" name="applyRateFlag"  value="<%= count%>" checked>
                                            <%}else{
                                            value ="false";                                           
                                              %>
                                           <input type="checkbox" name="applyRateFlag"  value="<%= count%>" > 
                                           
                                            <%}
                                            //COEUSQA-4060
                                            %>                                                         
                                       </td>
                                       <td align="left" bgcolor='#D1E5FF' width='10%'> 
                                        <!--input type="textbox"  name="calculatedCost<%=count%>" value="<%=calAmts.get("calculatedCost")%>" readonly="readonly" class="cltextbox-long-readonly"-->
                                        <% String calculatedCost = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(calAmts.get("calculatedCost")); %>
                                        <html:text property="strcalculatedCost" value="<%=calculatedCost%>" readonly="true" styleClass="cltextbox-equip"/>
                                        </td>
                                        <td align="left" nowrap class="cltextbox-equip" width='15%'>   
                                         <% calculatedCost = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(calAmts.get("calculatedCostSharing")); %>                                
                                        <html:text property="strcalculatedCostSharing" value="<%=calculatedCost%>" readonly="true" styleClass="cltextbox-equip"/>

                                        </td>

                                    </tr>
                                  <%count++;%>
                                  <html:hidden property="rateClassCode"/>
                                 <html:hidden property="rateTypeCode"/>
                                 <html:hidden property="updateTimestamp"/>
                                  </logic:iterate>
                                  </logic:present>
                        </table>
                    </td>
                    </tr>
                            <!--/div-->
                            </table>
                    </td>
                    </tr>
                    <tr>
                    <td colspan='6' class='tabtable'>
                        <table align="left" width="100%" border="0" cellpadding="0" class='tabtable'>
                        <tr>
                            <%if(!readOnly){%>
                            <td width="35%" height='25' align="center" class="copybold" valign=middle>
                                <%--<html:button property="Submit"  styleClass="clbutton" onclick="calculate_budget_current()" disabled="<%=readOnly%>"> 
                                    <bean:message bundle="budget" key="budgetLineItemDetailsButton.saveAndApplyToCurrentPeriod"/> 
                                </html:button>--%>
                                <html:link href="javascript:calculate_budget_current()"> 
                                    <u><bean:message bundle="budget" key="budgetLineItemDetailsButton.saveAndApplyToCurrentPeriod"/></u>
                                </html:link>
                            </td>
                            <td width="35%" align="left" class="copybold" valign=middle>
                                <%--<html:button property="Submit"  styleClass="clbutton" onclick="calculate_budget_later()" disabled="<%=readOnly%>">
                                     <bean:message bundle="budget" key="budgetLineItemDetailsButton.saveAndApplyToCurrentAndLaterPeriods"/> 
                                </html:button>--%>
                                
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
                            <td width="5%" height='25' align="center" class="copybold" valign=middle>
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