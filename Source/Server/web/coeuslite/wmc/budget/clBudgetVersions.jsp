<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="java.util.Vector,edu.mit.coeuslite.utils.ComboBoxBean,org.apache.struts.validator.DynaValidatorForm,java.util.LinkedHashMap" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"  prefix="coeusUtils"%>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>

<jsp:useBean id="budgetVersionsData" scope="session" class="java.util.Vector" />
<jsp:useBean id="optionsBudgetStatus" scope="session" class="java.util.Vector" />
<jsp:useBean id="budgetStatusCode" scope="session" class="java.lang.String" />
<jsp:useBean id="newBudgetStatus" scope="request" class="java.lang.String" />
<jsp:useBean id="sponsorCode" scope="request" class="java.lang.String" />
<jsp:useBean id="sponsorName" scope="request" class="java.lang.String" />
<jsp:useBean id="budgetFinalVersionValue" scope="session" class="java.lang.String"/>
<jsp:useBean id="versionUpdated" scope="request" class="java.lang.String"/>
<jsp:useBean id="forceCSDInVersions" scope="session" class="java.util.LinkedHashMap"/>
<jsp:useBean id="forceURDInVersions" scope="session" class="java.util.LinkedHashMap"/>
<bean:size id="versionSize" name="budgetVersionsData"/>
<html:html>
<%  
    String mode = (String)session.getAttribute("mode"+session.getId());

    String budgetStatusMode = (String)session.getAttribute("budgetStatusMode");     
    
    boolean readOnly = false;
    boolean modeReadOnly = false;
    boolean budgetStatusReadOnly = false;
    Vector vecAppPerTypesMessages = null;
    if(session.getAttribute("inactiv_App_Per_Type_Messages")!=null){
    vecAppPerTypesMessages = (Vector) session.getAttribute("inactiv_App_Per_Type_Messages"); 
    }
    if(budgetStatusCode.equalsIgnoreCase("C") || "C".equalsIgnoreCase(newBudgetStatus))
    {
    readOnly = true;          
    }
          
    if(mode!= null && mode.equals("display")){
    readOnly = true;
    modeReadOnly = true;
    }
    String validationCSDMsg = (String)session.getAttribute("CSDVersionsValidationMsg");
    String validationURDMsg = (String)session.getAttribute("URDVersionsValidationMsg");
%>
<head>
<title>
    Budget Versions
    <html:base/>
</title>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<script language="JavaScript" type="text/JavaScript">
// Added for Cost Sharing Distribution Validation - start    
var forceValidate= new Array();  
<logic:present name="forceCSDInVersions">
    <logic:iterate id="fv" name="forceCSDInVersions">
        forceValidate.push('<bean:write name="fv" property="value"/>');
    </logic:iterate>  
</logic:present>
// Added for Cost Sharing Distribution Validation -end

var forceURValidate= new Array();  
<logic:present name="forceURDInVersions">
    <logic:iterate id="urd" name="forceURDInVersions">
        forceURValidate.push('<bean:write name="urd" property="value"/>');
    </logic:iterate>  
</logic:present>
</script>
<script language="JavaScript" type="text/JavaScript">
        
         var errValue = false;
         var errLock = false;
         var validateCSD = " ";
         var validateURD = " ";
         function checkBudgetVersion(budgetVersionValue){
            dataChanged();
            var confirmMsg=confirm("<bean:message bundle="budget" key="budgetVersions.ModifyingTheFinalVersionStatus"/>");
            if(budgetVersionValue.checked==true){   
                if(confirmMsg==true){   
                    var count=0;
                    for(count=0;count<<%=versionSize%>;count++){ 
                        if(<%=versionSize%>>1)
                            document.budgetVersions.finalVersionFlag[count].checked=false;
                        else
                            document.budgetVersions.finalVersionFlag.checked=false;
                    }
                    budgetVersionValue.checked=true;
                    for(count=0;count<<%=versionSize%>;count++){
                        if(<%=versionSize%>>1){
                            if(document.budgetVersions.finalVersionFlag[count].checked==true){
                                document.budgetVersions.finalVersionValue.value=count+1;
                            }
                        }
                        else {
                            if(document.budgetVersions.finalVersionFlag.checked==true){
                                document.budgetVersions.finalVersionValue.value=count+1;
                            }
                        }
                    }
                }
                else
                    budgetVersionValue.checked=false;
            }
            else{
                if(confirmMsg==true){
                    budgetVersionValue.checked=false;
                    document.budgetVersions.finalVersionValue.value=" ";
                }
                else
                    budgetVersionValue.checked=true;
            }
        }

	 function checkBudgetStatus(budgetStatusValue){	   
            var count=0;
            dataChanged();
            var validationCSDMsg = '<%=validationCSDMsg%>';
            var validationURDMsg = '<%=validationURDMsg%>';
            if(budgetStatusValue.selectedIndex==0 || budgetStatusValue.selectedIndex==2){               
                if(<%=versionSize%>>1){
                    for(count=0;count<<%=versionSize%>;count++){
                        document.budgetVersions.finalVersionFlag[count].disabled=false;
                    }
                }
                else{
                    document.budgetVersions.finalVersionFlag.disabled=false;
                }
                //document.budgetVersions.NewVersion.disabled=false;
            }
            else if(budgetStatusValue.selectedIndex==1){                
                var checkedCount=0;
                if(<%=versionSize%>>1){
                    for(count=0;count<<%=versionSize%>;count++){
                        if(document.budgetVersions.finalVersionFlag[count].checked==true){
                            checkedCount++;
                                // Added for Cost Sharing Distribution Validation - start
                                
                                var verNum = document.budgetVersions.versionNumber[count].value;
                                 validateCSD = forceValidate[verNum-1];
                                 validateURD = forceURValidate[verNum-1];
                                     if(validateCSD == 'force'){ 
                                        if(validationCSDMsg == 'amtUnequal'){
                                            alert("<bean:message bundle="budget" key="costSharingDistribution.error.UnequalAmts1"/>\n<bean:message bundle="budget" key="costSharingDistribution.error.UnequalAmts2"/>");
                                        }else if(validationCSDMsg == 'noCSD'){
                                           alert("<bean:message bundle="budget" key="costSharingDistribution.error.CSDforVersion"/>");
                                        }
                                           budgetStatusValue.selectedIndex=0;
                                     }
                                     if(validateURD == 'force'){ 
                                        if(validationURDMsg == 'amtUnequal'){
                                            alert("<bean:message bundle="budget" key="underRecoveryDistribution.alertMessage1"/>\n<bean:message bundle="budget" key="underRecoveryDistribution.alertMessage2"/>");
                                        }else if(validationURDMsg == 'noURD'){
                                           alert("<bean:message bundle="budget" key="underRecoveryDistribution.error.URDforVersion"/>");
                                        }
                                           budgetStatusValue.selectedIndex=0;
                                     }
                                // Added for Cost Sharing Distribution Validation - end
                                     
                        }
                        
                    }
                }
                else{
                    if(document.budgetVersions.finalVersionFlag.checked==true){
                            checkedCount++;
                            // Added for Cost Sharing Distribution Validation -start

                            var verNum = document.budgetVersions.versionNumber.value;
                             validateCSD = forceValidate[verNum-1];
                             validateURD = forceURValidate[verNum-1];
                             if(validateCSD == 'force'){ 
                                      if(validationCSDMsg == 'amtUnequal'){
                                            alert("<bean:message bundle="budget" key="costSharingDistribution.error.UnequalAmts1"/>\n<bean:message bundle="budget" key="costSharingDistribution.error.UnequalAmts2"/>");
                                        }else{
                                           alert("<bean:message bundle="budget" key="costSharingDistribution.error.CSDforVersion"/>");
                                        }
                                      budgetStatusValue.selectedIndex=0;
                               }
                              if(validateURD == 'force'){ 
                                      if(validationURDMsg == 'amtUnequal'){
                                            alert("<bean:message bundle="budget" key="underRecoveryDistribution.alertMessage1"/>\n<bean:message bundle="budget" key="underRecoveryDistribution.alertMessage2"/>");
                                        }else{
                                           alert("<bean:message bundle="budget" key="underRecoveryDistribution.error.URDforVersion"/>");
                                        }
                                      budgetStatusValue.selectedIndex=0;
                               }
                             // Added for Cost Sharing Distribution Validation - end
                    }
                }
                    
                if(checkedCount==0){
                        alert("<bean:message bundle="budget" key="budgetVersions.SelectVersionOfTheBudget"/>");
                    budgetStatusValue.selectedIndex=0;
                        }
                        else{
                        //COEUSDEV - 159:Lite - cost share warning -won't allow complete status in lite
                            if(<%=versionSize%>>1){
                                // modified for Cost Sharing Distribution Validation -start
                                if(validateCSD == 'force' || validateURD == 'force'){
                                    for(count=0;count<<%=versionSize%>;count++){
                                        document.budgetVersions.finalVersionFlag[count].disabled=false;
                                    }
                                }else{
                                    for(count=0;count<<%=versionSize%>;count++){
                                        document.budgetVersions.finalVersionFlag[count].disabled=true;
                                    }
                                }
                                
                              // modified for Cost Sharing Distribution Validation - end
                            }
                            else{
                                    // modified for Cost Sharing Distribution Validation -start
                                    if(validateCSD == 'force' || validateURD == 'force'){
                                        document.budgetVersions.finalVersionFlag.disabled=false;
                                    }else{
                                        document.budgetVersions.finalVersionFlag.disabled=true;
                                    }
                                  // modified for Cost Sharing Distribution Validation - end
                                 }
                                 if(validateURD == 'notForce' && validateCSD == 'notForce'){
                                    validateBudget(document.budgetVersions.finalVersionValue.value);
                                 }
                                 //COEUSDEV - 159:End
                             //document.budgetVersions.NewVersion.disabled=true;
                          }
            }
            }

        function validateBudget(finalVersion){
            document.budgetVersions.action = "<%=request.getContextPath()%>/validateProposal.do?PAGE=BV&versionNumber="+finalVersion;
            document.budgetVersions.submit();
        }
	
	function checkDataChanged(){
            <logic:equal name="newBudgetStatus" value = "C">
            dataChanged(); 
        </logic:equal>
        <logic:equal name="versionUpdated" value = "Y">
            dataChanged(); 
        </logic:equal>   
        }
        
	function budgetVersion_Actions(actionName){
           var budgetVersion = "<%=versionSize%>";
            if(actionName == "A" && validate()){              
              document.budgetVersions.action = "<%=request.getContextPath()%>/addNewVersion.do?actionFrom=NewVersion &versionNumber="+budgetVersion;
              document.budgetVersions.submit();
               
            }else if(actionName == "S"){               
                    document.budgetVersions.action = "<%=request.getContextPath()%>/getBudgetVersions.do?actionFrom=Save &Versions="+budgetVersion;
                    document.budgetVersions.submit();               
            }
             // Hide the code in first div tag
                    document.getElementById('budgetVersionDiv').style.display = 'none';
             // Display code in second div tag
                    document.getElementById('saveDiv').style.display = 'block';     
        }
        
        function budgetVersionCopy_Action(link){
          CLICKED_LINK = "<%=request.getContextPath()%>"+link;
          if(validate()){        
            if(document.budgetVersions.budgetStatusCode.selectedIndex==1){
                alert("<bean:message bundle="budget" key="budgetVersions.BudgetComplete"/>"); 
                return false;
            }
          } else {
            return false;
          }
            }
         function budgetVersionOpen_Action(versionNumber){
         var budgetStatus = '';
          if(document.budgetVersions.budgetStatusCode.selectedIndex==0){
                budgetStatus = "I" ;
          }else if(document.budgetVersions.budgetStatusCode.selectedIndex==1)
          {
             budgetStatus = "C" ;
          }
          CLICKED_LINK = "<%=request.getContextPath()%>/openBudgetVersion.do?versionNumber="+versionNumber+"&BudgetStatus="+budgetStatus ;
          if(validate()){
              if(document.budgetVersions.budgetStatusCode.selectedIndex==0){
                document.budgetVersions.action = "<%=request.getContextPath()%>/openBudgetVersion.do?versionNumber="+versionNumber+"&BudgetStatus=I" ;
              }else if(document.budgetVersions.budgetStatusCode.selectedIndex==1)
              {
                 document.budgetVersions.action ="<%=request.getContextPath()%>/openBudgetVersion.do?versionNumber="+versionNumber+"&BudgetStatus=C" ;
              }
              document.budgetVersions.submit();
           }
         }
  
	
 </script>
</head>

<body onload="checkDataChanged()">
<html:form action="/getBudgetVersions.do" method="post">
 <div id='budgetVersionDiv'>
<table width="100%" height="100%"  border="0" cellpadding="0" cellspacing="0" class="table">
    <tr>
        <td>
            <table width="100%" height="20"  border="0" cellpadding="0" cellspacing="0" class="tableheader">
                <tr>
                  <td>
                        <bean:message bundle="budget" key="budgetVersions.BudgetVersion"/>
                  </td>
                </tr>
            </table>
        </td>
    </tr>
    <!-- BudgetVersion - Start  -->
    <tr>
      <td align="left" valign="top" class='core'>
          <br>
        <table width="98%"  border="0" align="center" cellpadding="0" cellspacing="0" class="tabtable">
              <tr>
                  <td height='10'>
                            <div id="helpText" class='helptext'>            
                                <bean:message bundle="budget" key="helpTextBudget.Versions"/>  
                            </div> 
                  </td>
              </tr> 
              <tr class='copy' align="left">
                <td>
                    <font color="red"  >
                        <logic:messagesPresent> 
                        <script>errValue = true;</script>
                        <html:errors header="" footer=""/>
                        </logic:messagesPresent>
                        
                        <logic:messagesPresent message="true">
                            <script>errValue = true;</script>
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
                        <!--Added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - start-->
                        <%if(vecAppPerTypesMessages != null && vecAppPerTypesMessages.size() >0) {%>                                                
                        <%for(int index = 0; index < vecAppPerTypesMessages.size(); index++){%>
                        <tr>
                            <td align = "left">
                                <font color="red">
                                    <li> <%=vecAppPerTypesMessages.elementAt(index)%> </li>
                                </font>
                            </td>
                            <td>
                                <br>
                                &nbsp;
                                <br>
                            </td>
                        </tr>                                                
                        <%}}
                        session.setAttribute("inactiv_App_Per_Type_Messages",null);
                        %>
                        <!--Added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - end-->
                    </font>
                </td>
                </tr>   
           <%--   <tr>
                    <td  class='copybold'> &nbsp;&nbsp;<bean:message bundle="budget" key="budgetVersions.Sponsor"/>  <%=sponsorCode%> - <%=sponsorName%></td> 
             </tr>  --%>
         <%--    <tr> <td> &nbsp; </td> </tr>
             <tr align='left'>
                  <td  width='20%' align="left" valign="top" class="copybold"> &nbsp;
                       
                        <%if(!readOnly){%>
                        <html:link  href="javascript:budgetVersion_Actions('A')" styleClass="copy">
                          <u>  <bean:message bundle="budget" key="budgetVersions.AddNewVersion"/> </u>
                        </html:link>
                        <%}else{%>
                            <bean:message bundle="budget" key="budgetVersions.AddNewVersion"/>
                        <%}%>
                        &nbsp;

                        <html:button  property="Save" styleClass="clbutton"  disabled="<%=modeReadOnly%>" onclick="budgetVersion_Actions('S')">
                            <bean:message bundle="budget" key="summaryButton.Save"/>
                        </html:button>
                   </td>
              </tr>   --%>
              <tr>
                <td>&nbsp;</td>
              </tr>
              <tr colspan="3">
                 <td class='copybold' width='60%' align=left>
                        &nbsp;&nbsp;<bean:message bundle="budget" key="summaryLabel.BudgetStatus"/>:                   
                      
                       <html:select  name="budgetVersions" disabled="<%=modeReadOnly%>"  property="budgetStatusCode" onchange="checkBudgetStatus(this)">
                             <html:options collection="optionsBudgetStatus" property="code" labelProperty="description"/>
                        </html:select> 
                       
                        &nbsp;&nbsp;&nbsp;  <bean:message bundle="budget" key="budgetVersions.FinalVersion"/> 
                        <html:text property="finalVersionValue" size="3" styleClass="cltextbox-color" readonly="true"/>
                        <%if(!budgetFinalVersionValue.equals("0") && !"Y".equalsIgnoreCase(versionUpdated)) {  %>
                             <script>
                                 document.budgetVersions.finalVersionValue.value=<%=budgetFinalVersionValue%>;
                             </script>
                         <% } %>   
                      
                 </td>
                 <td width='20%' align=left>&nbsp;   </td>
                 <td width='20%' align=left>&nbsp;</td>
              </tr>  
                  <tr>
                      <td width='100%'>
                      <br>
                        <table width="95%" border="0" align="center" cellpadding="5" cellspacing="0" class="tabtable">

                            <logic:present name="budgetVersionsData" scope="session">

                              <tr >
                                    <td class="theader" align=left><bean:message bundle="budget" key="budgetVersions.Version"/></td>
                                    <td class="theader" align=left><bean:message bundle="budget" key="summaryLabel.StartDate"/></td>
                                    <td class="theader" align=left><bean:message bundle="budget" key="summaryLabel.EndDate"/></td>
                                    <td class="theader" align=left><bean:message bundle="budget" key="summaryLabel.DirectCost"/> </td>
                                    <td class="theader" align=left><bean:message bundle="budget" key="summaryLabel.IndirectCost"/> </td>
                                    <td class="theader" align=left><bean:message bundle="budget" key="summaryLabel.TotalCost"/></td>
                                    <td class='theader'> <bean:message bundle="budget" key="summaryLabel.Final"/></td>
                                    <td class="theader">&nbsp;</td>
                                    <td class="theader">&nbsp; </td>                                    
                              </tr>
                              
                               <%
                                    String strBgColor = "#DCE5F1";
                                     int index=0;
                                    %>

                                <logic:iterate id="versionData" name="budgetVersionsData" type="org.apache.commons.beanutils.DynaBean">
                                     <%
                                           if (index%2 == 0) {
                                                strBgColor = "#D6DCE5"; 
                                            }
                                           else { 
                                                strBgColor="#DCE5F1"; 
                                             }  %>   
                                  <tr  bgcolor="<%=strBgColor%>" onmouseover="className='TableItemOn'" onmouseout="className='TableItemOff'"> 
                                        <td class="copy" align="center"><bean:write name="versionData" property="versionNumber"/>   </td>
                                        <td class="copy"> <coeusUtils:formatDate name="versionData" property="startDate" />  </td>
                                        <td class="copy"> <coeusUtils:formatDate name="versionData" property="endDate" />  </td>
                                        <td class="copy"> <coeusUtils:formatString name="versionData" property="totalDirectCost" formatType="currencyFormat"/>  </td>
                                        <td class="copy"> <coeusUtils:formatString name="versionData" property="totalIndirectCost" formatType="currencyFormat"/></td>
                                        <td class="copy"> <coeusUtils:formatString name="versionData" property="totalCost" formatType="currencyFormat"/>  </td>
                                        <td class="copy">                                       
                                            <html:checkbox name="versionData" property="finalVersionFlag"  style="copy" disabled="<%=readOnly%>"  value='Y' onclick="checkBudgetVersion(this)" />   
                                        </td>                                         
                                        <td class="copy">                                              
                                            <% String versionNo ="javaScript:budgetVersionOpen_Action("+versionData.get("versionNumber")+")"; %>                                            
                                                <html:link href="<%=versionNo%>"> 
                                                    <bean:message bundle="budget" key="budgetVersions.Open"/> 
                                                </html:link>                                                                                         
                                        </td>
                                        <td class="copy"> 
                                           
                                                <%  if(!modeReadOnly)
                                                    {
                                                        java.util.Map copyParams = new java.util.HashMap();
                                                        copyParams.put("versionNumber", versionData.get("versionNumber"));
                                                        copyParams.put("proposalNumber", versionData.get("proposalNumber"));
                                                        copyParams.put("actionFrom","Copy");
                                                        pageContext.setAttribute("copyParamsMap", copyParams);   
                                                        String link = "javascript:return budgetVersionCopy_Action('/getBudgetVersions.do?PAGE=V";
                                                        link += "&proposalNumber="+versionData.get("proposalNumber");
                                                        link += "&actionFrom=Copy&versionNumber="+versionData.get("versionNumber")+"')";
                                                     %>
                                                <html:link action="/getBudgetVersions.do?PAGE=V" name="copyParamsMap" onclick="<%=link%>" > 
                                                    <bean:message bundle="budget" key="budgetVersions.Copy"/>
                                                </html:link>                                               
                                                 <% } else {  %>  
                                                     <bean:message bundle="budget" key="budgetVersions.Copy"/>
                                                  <% } %>                                               
                                        </td>                                            
                                            <html:hidden name="versionData" property="updateTimestamp"/>
                                            <html:hidden name="versionData" property="proposalNumber"/>
                                            <html:hidden name="versionData" property="versionNumber"/>                                               
                                  </tr>
                                  <tr  bgcolor="<%=strBgColor%>" onmouseover="className='TableItemOn'" onmouseout="className='TableItemOff'">
                                      <td class="copybold"><bean:message bundle="budget" key="summaryLabel.Comments"/>:</td>
                                      <td colspan="8" class="copy"><html:textarea property="comments" name="versionData" rows="2" cols="65" styleClass="cltextbox-nonEditcolor" readonly="true" style="width:650px"></html:textarea></td>
                                  </tr>
                                  
                                   <% 
                                     index++;
                                   %>                                    
                               </logic:iterate>
                            </logic:present>
                        </table>
                      </td>
                  </tr>
                  
                  <tr> <td> &nbsp; </td> </tr>
                 <tr align='left'>
                  <td  width='20%' align="left" valign="top" class="copybold"> &nbsp;

                        <%--<html:button  property="NewVersion" styleClass="clbutton" disabled="<%=readOnly%>" onclick="budgetVersion_Actions('A')">
                            <bean:message bundle="budget" key="budgetVersions.AddNewVersion"/>
                        </html:button>--%>
                        <%if(!readOnly){%>
                        <html:link  href="javascript:budgetVersion_Actions('A')" styleClass="copy">
                          <u><bean:message bundle="budget" key="budgetVersions.AddNewVersion"/></u>
                        </html:link>
                        <%}else{%>
                          <u><bean:message bundle="budget" key="budgetVersions.AddNewVersion"/></u>
                        <%}%>
                        &nbsp;&nbsp;

                        
                   </td>
              </tr>
                  
                   <tr>
                      <td height='10'>
                          &nbsp;
                      </td>
                   </tr>                    
        </table>
        <br>
      </td>
  </tr>
    <!-- BudgetVersion - End  -->  
        <tr>
          <td height='10' class='savebutton'>&nbsp;&nbsp;
              <html:button  property="Save" styleClass="clsavebutton"  disabled="<%=modeReadOnly%>" onclick="budgetVersion_Actions('S')">
                            <bean:message bundle="budget" key="summaryButton.Save"/>
                        </html:button>
                        <br><br>
          </td>
      </tr>   
</table>
</div>
 <div id='saveDiv' style='display: none;'>
        <table width="100%" height="100%" align="center" border="0" cellpadding="3" cellspacing="0" class="tabtable">
            <tr>
                <td align='center' class='copyred'> 
                    <br> <bean:message bundle="budget" key="budgetMessages.pleaseWait"/>  
                </td>
            </tr>
        </table>
</div>
   
 </html:form>
<script>
      DATA_CHANGED = 'false';
      if(errValue && !errLock){
            DATA_CHANGED = 'true';
      }
      LINK = "<%=request.getContextPath()%>/getBudgetVersions.do?actionFrom=Save &Versions=<%=versionSize%>";
      FORM_LINK = document.budgetVersions;
      PAGE_NAME = "<bean:message bundle="budget" key="budgetVersions.BudgetVersion"/>";
      function dataChanged(){
            DATA_CHANGED = 'true';
      }
      linkForward(errValue);
</script>
    <script>
      var help = '<bean:message bundle="budget" key="helpTextBudget.Versions"/>';
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