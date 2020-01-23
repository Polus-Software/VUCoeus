<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<%@ page import="java.util.Vector,
             edu.mit.coeuslite.utils.ComboBoxBean"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld"  prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld"  prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"  prefix="coeusUtils"%>


<html:html>
<%
   //COEUSQA-1689 Role Restrictions for Budget Rates - Start
   String mode = (String)session.getAttribute("mode"+session.getId());
   String budgetStatusMode = (String)session.getAttribute("budgetStatusMode");
   String modifyProposalRates = (String)session.getAttribute("modifyProposalRates");
   boolean readOnly = false;
   boolean proposalRatesReadOnly = false;
   if(mode!= null && mode.equals("display")){
       readOnly = true;
   }else if(budgetStatusMode != null && budgetStatusMode.equals("complete")){
        readOnly = true;
   }
   if(modifyProposalRates != null && modifyProposalRates.equals("norights")){
        proposalRatesReadOnly = true;
   }
   //COEUSQA-1689 Role Restrictions for Budget Rates - End
%>
<head><title>JSP Page</title></head>
<body>
<script language="JavaScript">
var resetClicked = "" ;
var errValue = false;
      function proposalRates_Actions(actionName){
            
            if(actionName == "RESET"){
                var msg = '<bean:message bundle="budget" key="propRate.resetConfirm" />';
                        if (confirm(msg)==true)
                        {
                             document.applicableRateList.action = "<%=request.getContextPath()%>/getBudgetProposalRate.do?operation=RESET";
                             document.applicableRateList.submit();
                        }
            }else if(actionName == "SYNC"){
                // This needs refreshing of page with latest values.
                // go to the server and fetch values from Institute rates table
                // and refresh the screen. Do not copy the values to proposal rates table
                     var msg2 = '<bean:message bundle="budget" key="propRate.syncConfirm" />'; 
                    if (confirm(msg2)==true)
                    {
                                document.applicableRateList.action = "<%=request.getContextPath()%>/updateBudgetProposalRate.do?operation=SYNC";
                                document.applicableRateList.submit();
                     }           
                
            }else if(actionName == "SAVE"){
                 document.applicableRateList.action = "<%=request.getContextPath()%>/updateBudgetProposalRate.do?operation=SAVE";
                 document.applicableRateList.submit();
            }
        }

       
</script>
<html:form action="/updateBudgetProposalRate.do" method="POST"> 

 <!-- Declare variables which define the order -->  
     <div id="helpText" class='helptext'>            
        <bean:message bundle="budget" key="helpTextBudget.Rates"/>  
    </div> 
    <bean:define id="varFirst" type="String">
          <bean:message bundle="budget" key="propRate.first"/>
    </bean:define>
    <bean:define id="varSecond" type="String">
          <bean:message bundle="budget" key="propRate.second"/>
    </bean:define>                    
    <bean:define id="varThird" type="String">
          <bean:message bundle="budget" key="propRate.third"/>
    </bean:define>
    <bean:define id="varFourth" type="String">
          <bean:message bundle="budget" key="propRate.fourth"/>
    </bean:define>
    <bean:define id="varFifth" type="String">
          <bean:message bundle="budget" key="propRate.fifth"/>
    </bean:define>
    <bean:define id="varSixth" type="String">
          <bean:message bundle="budget" key="propRate.sixth"/>
    </bean:define>
    <bean:define id="varSeventh" type="String">
          <bean:message bundle="budget" key="propRate.seventh"/>
    </bean:define>
    <!-- Declare variables for titles -->  
    
    <bean:define id="varFirstTitle" type="String">
          <bean:message bundle="budget" key="propRate.firstTitle"/>
    </bean:define>
    <bean:define id="varSecondTitle" type="String">
          <bean:message bundle="budget" key="propRate.secondTitle"/>
    </bean:define>                    
    <bean:define id="varThirdTitle" type="String">
          <bean:message bundle="budget" key="propRate.thirdTitle"/>
    </bean:define>
    <bean:define id="varFourthTitle" type="String">
          <bean:message bundle="budget" key="propRate.fourthTitle"/>
    </bean:define>
    <bean:define id="varFifthTitle" type="String">
          <bean:message bundle="budget" key="propRate.fifthTitle"/>
    </bean:define>
    <bean:define id="varSixthTitle" type="String">
          <bean:message bundle="budget" key="propRate.sixthTitle"/>
    </bean:define>
    <bean:define id="varSeventhTitle" type="String">
          <bean:message bundle="budget" key="propRate.seventhTitle"/>
    </bean:define>
    <!-- Declare variables for hiding/showing columns and buttons -->  
    
    <bean:define id="showRateClass" type="String">
          <bean:message bundle="budget" key="propRate.showRateClass"/>
    </bean:define>

<bean:define id="showRateType" type="String">
          <bean:message bundle="budget" key="propRate.showRateType"/>
    </bean:define>

<bean:define id="showFiscalYear" type="String">
          <bean:message bundle="budget" key="propRate.showFiscalYear"/>
    </bean:define>

<bean:define id="showOnOffCampus" type="String">
          <bean:message bundle="budget" key="propRate.showOnOffCampus"/>
    </bean:define>

<bean:define id="showStartDate" type="String">
          <bean:message bundle="budget" key="propRate.showStartDate"/>
    </bean:define>

<bean:define id="showInstituteRate" type="String">
          <bean:message bundle="budget" key="propRate.showInstituteRate"/>
    </bean:define>

<bean:define id="showApplicableRate" type="String">
          <bean:message bundle="budget" key="propRate.showApplicableRate"/>
    </bean:define>

<bean:define id="showActivityType" type="String">
          <bean:message bundle="budget" key="propRate.showActivityType"/>
    </bean:define>


    
    
           <table width="97%"  border="0" cellspacing="1" cellpadding="3" class="tabtable">
           <tr>
                <td colspan='6'>
                    <font color='red'>
                    <!-- prps error check start-->
                    <logic:messagesPresent>
                        <script>errValue = true;</script>
                        <html:errors header="" footer = ""/>
                    </logic:messagesPresent>
                     <!-- prps error check start-->
                    <logic:messagesPresent message="true">
                        <html:messages id="message" message="true" property="errMsg"> 
                            <script>errLock = true; </script>
                            <li><bean:write name = "message"/></li>
                        </html:messages>               
                    </logic:messagesPresent>
                     </font>
                </td>
           </tr>
                 <%
                    String strBgColor = "#DCE5F1";
                    String checkString = "" ; 
                     int index=0;
                     int loopCount = 0 ;
                     int outerLoopCount = 0 ;
                     int group = 0;
                     String titleString = "" ;
                     String on_Off_campus_flag = "" ;
                    %>
                    
 <!-- section one start -->              
     
 
 
   
         <logic:present name ="applicableRateList" scope = "session">  
         <!-- This iterate will loop jusy once to get activity type info -->
        <logic:iterate id = "budgetProposalRateOuter" name ="applicableRateList" property="list"
                            type = "org.apache.struts.action.DynaActionForm" length="1"> 
                   <TR><TD><B>Activity Type: <coeusUtils:formatOutput name="budgetProposalRateOuter" property="activityTypeDescription_wmc"/> </B> </TD></TR>         
        </logic:iterate> 
                    
<%
     while(group < 7 ) // This outer loop should loop 6 times to pick all rate class type
    {           
                                loopCount= 0 ;
                                group++ ;
                                if (group == 1) 
                                {
                                    checkString = varFirst ;
                                    titleString = varFirstTitle ;
                                }    
                                if (group == 2) 
                                {
                                    checkString = varSecond ;
                                    titleString = varSecondTitle ;
                                }    
                                if (group == 3) 
                                {
                                    checkString = varThird ;
                                    titleString = varThirdTitle ;
                                }    
                                if (group == 4) 
                                {
                                    checkString = varFourth ;
                                    titleString = varFourthTitle ;
                                }    
                                if (group == 5) 
                                {
                                    checkString = varFifth ;
                                    titleString = varFifthTitle ;
                                }    
                                if (group == 6) 
                                {
                                    checkString = varSixth ;
                                    titleString = varSixthTitle ;
                                }
                                if (group == 7) 
                                {
                                    checkString = varSeventh ;
                                    titleString = varSeventhTitle ;
                                }   
                                %>
                                
                                <%
                                if (outerLoopCount == 0)
                                { outerLoopCount++ ; // increment this so that this heading is shown only one time
                                %>
                                
                                <% } %>
                                
                                
                            
        <logic:iterate id = "budgetProposalRate_wmc" name ="applicableRateList" property="list"
                            type = "org.apache.struts.action.DynaActionForm" indexId="count" >
            <%
             
             if (checkString.equals(budgetProposalRate_wmc.get("rateClassType_wmc")) )
            {
            %>
                    <%
                       if (index%2 == 0) {
                            strBgColor = "#D6DCE5"; 
                        }
                       else { 
                            strBgColor="#DCE5F1"; 
                         }  %>     

                    <%
                        if (loopCount == 0) // display table header once per rate class 
                        { %>
                           <tr><td><B>    
                                         <%=titleString%>
                                         </B></td></tr>
                                                     
                            <tr align="center" valign="top">
                            
                         

                            <%if (showRateClass.equals("Y")) { %>
                            <td  align="left" class="theader"><bean:message bundle="budget" key="propRate.rateClass"/> </td>
                            <% } %>
                            <%if (showRateType.equals("Y")) { %>
                              <td  align="left" class="theader"><bean:message bundle="budget" key="propRate.rateType"/> </td>
                              <% } %>
                             <%if (showOnOffCampus.equals("Y")) { %> 
                             <td  align="left" class="theader"><bean:message bundle="budget" key="propRate.onOffCampus"/> </td>                  
                             <% } %>
                              <%if (showFiscalYear.equals("Y")) { %>
                              <td  align="left" class="theader"><bean:message bundle="budget" key="propRate.fiscalYear"/> </td>
                              <% } %>
                              <%if (showStartDate.equals("Y")) { %>
                              <td  align="left" class="theader"><bean:message bundle="budget" key="propRate.startDate"/> </td>
                              <% } %>
                              <%if (showInstituteRate.equals("Y")) { %>
                              <td  align="left" class="theader"><bean:message bundle="budget" key="propRate.instituteRate"/> </td>
                              <% } %>
                              <%if (showApplicableRate.equals("Y")) { %>
                              <td  align="left" class="theader"><bean:message bundle="budget" key="propRate.applicableRate"/> </td>
                              <% } %>
                              <%if (showActivityType.equals("Y")) { %>
                              <td  align="left" class="theader"><bean:message bundle="budget" key="propRate.activityType"/> </td>
                              <% } %>
                            </tr>

                        <% } %>
                    <tr class="copy" bgcolor="<%=strBgColor%>"> 
                        <%if (showRateClass.equals("Y")) { %>
                        <td> <coeusUtils:formatOutput name="budgetProposalRate_wmc" property="rateClassDescription_wmc" />  </td>
                        <% } %>
                        <%if (showRateType.equals("Y")) { %>
                        <td> <coeusUtils:formatOutput name="budgetProposalRate_wmc" property="rateTypeDescription_wmc" />  </td>
                        <% } %>
                        <%if (showOnOffCampus.equals("Y")) {   
                           if (budgetProposalRate_wmc.get("onOffCampusFlag_wmc").equals("N"))
                                    on_Off_campus_flag = "On" ;
                            else
                                    on_Off_campus_flag = "Off" ;
                             // on_Off_campus_flag = "av - " + budgetProposalRate_wmc.get("onOffCampusFlag_wmc") + " - aw -" + budgetProposalRate_wmc.get("awOnOffCampusFlag_wmc") ;
                            %> 
                        <td> <%=on_Off_campus_flag%> </td>
                        <% } %>
                        <%if (showFiscalYear.equals("Y")) { %>
                        <td> <coeusUtils:formatOutput name="budgetProposalRate_wmc" property="fiscalYear_wmc" /> 
                        </td>
                        
                        <% } %>
                        <%if (showStartDate.equals("Y")) { %>
                        <td> <coeusUtils:formatDate name="budgetProposalRate_wmc" property="startDate_wmc" /> </td>
                        <% } %>
                        <%if (showInstituteRate.equals("Y")) { %>
                        <td> <coeusUtils:formatOutput name="budgetProposalRate_wmc" property="instituteRate_wmc"/> </td>
                        <% } %>
                        <!-- COEUSQA-1689 Role Restrictions for Budget Rates - Start -->
                        <%if (showApplicableRate.equals("Y")) { %>
                        <td>
                        <%-- <html:text name="budgetProposalRate_wmc"  property="applicableRate_wmc" indexed="true" readonly="<%=(readOnly)%>" onchange="dataChanged()" /> --%>
                        <html:text name="budgetProposalRate_wmc"  property="applicableRate_wmc" indexed="true" readonly="<%=(proposalRatesReadOnly || readOnly)%>" onchange="dataChanged()" />
                        </td>
                        <!-- COEUSQA-1689 Role Restrictions for Budget Rates - End -->
                        <% } %>
                        <%if (showActivityType.equals("Y")) { %>
                        <td> <coeusUtils:formatOutput name="budgetProposalRate_wmc" property="activityTypeDescription_wmc"/>
                        <% } %>
                        <%    index++;   
                             %> 
                    </tr>
           <%
            loopCount++;
              }    
             %>            
                    
        </logic:iterate>              
                    
   <%
    } // end while
    %>     
      
                </logic:present>
                
<!-- section one ends -->                                   
             
                
          </table> 
          
          
                            <table width="100%"  border="0" cellpadding="2" cellspacing="0" >
                                <tr class='copy' align='center' width='100%'>
                                      <td width='33%' colspan='1'align="center" nowrap>

                                        <html:button accesskey="r" property="Reset" styleClass="clbutton" disabled="<%=readOnly%>" onclick="proposalRates_Actions('RESET')">
                                            <bean:message bundle="budget" key="propRate.resetButton"/>
                                        </html:button>
                                    </td>
                                    <td width='33%' colspan='1' align="center">
                                        <html:button accesskey="c" property="Sync" styleClass="clbutton" disabled="<%=readOnly%>" onclick="proposalRates_Actions('SYNC')">
                                            <bean:message bundle="budget" key="propRate.syncButton"/>
                                        </html:button>
                                    </td>
                                    <!-- COEUSQA-1689 Role Restrictions for Budget Rates - Start -->
                                    <td nowrap width='33%' colspan='1'align="center">
                                        <%-- <html:button accesskey="s" property="Save" styleClass="clbutton" disabled="<%=(readOnly)%>" onclick="proposalRates_Actions('SAVE')"> --%>
                                        <html:button accesskey="s" property="Save" styleClass="clbutton" disabled="<%=(proposalRatesReadOnly || readOnly)%>" onclick="proposalRates_Actions('SAVE')">
                                            <bean:message bundle="budget" key="propRate.saveButton"/>
                                        </html:button>
                                    </td>
                                    <!-- COEUSQA-1689 Role Restrictions for Budget Rates - End -->
                                </tr>
                                <tr>
                                    <td>
                                        <img src="<bean:write name='ctxtPath'/>/coeuslite images/spacer.gif"width="1" height="2">
                                    </td>
                               </tr>
                            </table>
                
          
          

</html:form>
    <script>
          DATA_CHANGED = 'false';
          if(errValue){
                DATA_CHANGED = 'true';
          }          
          LINK = "<%=request.getContextPath()%>/getBudgetProposalRate.do?operation=SAVE";
          FORM_LINK = document.applicableRateList;
          <!-- COEUSQA-1689 Role Restrictions for Budget Rates - Start -->
          <!-- #Observation:Save On change of Proposal Rate - Issue fix -->
          <!-- PAGE_NAME = ""; -->
          PAGE_NAME = "<bean:message bundle="budget" key="budget.proposalRate"/>";
          <!-- COEUSQA-1689 Role Restrictions for Budget Rates - End -->
          function dataChanged(){
                DATA_CHANGED = 'true';
          }
          linkForward(errValue);
    </script>
    <script>
      var help = '<bean:message bundle="budget" key="helpTextBudget.Rates"/>';
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

