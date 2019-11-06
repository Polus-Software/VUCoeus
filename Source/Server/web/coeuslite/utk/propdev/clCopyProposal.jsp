<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<%@page import="java.util.Vector, org.apache.struts.action.DynaActionForm"%>
<jsp:useBean id="vecUserUnits" scope="request" class="java.util.Vector" />


<html:html>

<%
  String strHasBudget = (String) request.getAttribute("strHasBudget");
  String strHasNarrative = (String) request.getAttribute("strHasNarrative");
  String budgetFinalVersionExist = (String) request.getAttribute("budgetFinalVersionExist"); 
  String budgetFlag = (String) request.getAttribute("budgetVersionFlag");
  String msgKeyForFinalFlag = (String)request.getAttribute("inactiveType_For_Final_Flag");
  String msgKeyForFinalVersion ="budgetSelect_exceptionCode."+msgKeyForFinalFlag;
  String msgKeyForAllVersionFlag = (String)request.getAttribute("inactiveType_For_All_version_Flag");
  String msgKeyForAllVersion ="budgetSelect_exceptionCode."+msgKeyForAllVersionFlag;
     
  boolean hasBudget = true;
  boolean hasNarrative = true;
  boolean budgetVersions = true;
  if(strHasBudget.equals("YES")){
       hasBudget = false;
   }
   if(strHasNarrative.equals("YES")){
       hasNarrative = false;
   }
  // Case# 2594:Problem with copy proposal - Start
  // Get the Vector which conatins Unit Information
  Vector vecUnits = (Vector) request.getAttribute("vecUserUnits");
  int unitCount ;
  
  String gUnitNumber = "";
  String gUnitName = "";
  
  // Get the Number of Units 
  if (vecUnits == null){
      unitCount = 0;
  } else{
      unitCount = vecUnits.size();
  }
  
  // If there is Only one Unit, get the Unit Number and Unit Name
  if (unitCount == 1){
    DynaActionForm userUnit = (DynaActionForm)vecUnits.get(0);
    gUnitNumber = (String)userUnit.get("unitNumber");
    gUnitName = (String)userUnit.get("unitName");
  }
  // Case# 2594:Problem with copy proposal - End
 
  String display =(String) request.getAttribute("display");
  String mode=(String)session.getAttribute("mode"+session.getId());
  boolean modeValue=false;
  //if(mode!=null && !mode.equals("")) {   
     // if(mode.equalsIgnoreCase("display")){
            // modeValue=true;
           //  hasBudget = true;  // for display mode
            // hasNarrative = true;
          
        
      //}
      
      
 // }
  Boolean canCopyQnr = (Boolean) request.getAttribute("canCopyQnr");
  
  //Added for COEUSQA-3509 : Add warning message to Copy proposal window - start
  Boolean ggProposal = (Boolean) request.getAttribute("gg_Proposal");
  //Added fro COEUSQA-3509 : Add warning message to Copy proposal window - end
  %>
<head><title>JSP Page</title>
<script>
var selectedValue="";
var errorMessageDisplayed = false;
function setVersionValue(value){
    selectedValue = value;
}
function proposalCopy(){ 
    //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start
    if(selectedValue == 'F'){
         <%if(msgKeyForFinalFlag != null && !"0".equals(msgKeyForFinalFlag)){%>
                if(confirm('<bean:message bundle="budget" key="<%=msgKeyForFinalVersion%>"/>') == 1) {
                document.getElementById('unitDiv').style.display = 'block';
                }else{
                return false;
                }
        <%}%>
        document.getElementById('unitDiv').style.display = 'block';  
    }
    if(selectedValue == 'A'){
        <%if(msgKeyForAllVersionFlag != null && !"0".equals(msgKeyForAllVersionFlag)){%>
            if(confirm('<bean:message bundle="budget" key="<%=msgKeyForAllVersion%>"/>') == 1) {
                document.getElementById('unitDiv').style.display = 'block';
            }else{
             return false;
            }
        <%}%>
        document.getElementById('unitDiv').style.display = 'block';       
     }
     //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - end
     <%if(canCopyQnr != null && !canCopyQnr.booleanValue()){ %>
     if( errorMessageDisplayed == false && document.copyProposalForm.questionnaireFlag.checked ==  true){
         document.getElementById('main').style.display = 'none';
         document.getElementById('error').style.display = 'block';
         document.getElementById('mainbutton').style.display = 'none';
         document.getElementById('errorbutton').style.display = 'block';
         document.getElementById('unitDiv').style.display = 'none';
         errorMessageDisplayed = true;
         return;
     }    
     <%} %>

    // Case# 2594:Problem with copy proposal - Start
    
    <% // If  the User has Copy Proposal Right only at One Unit
    if(unitCount == 1){
      // Call copyProposalInfo on Clicking the button 'Copy proposal' button %>   
        copyProposalInfo('<%=gUnitNumber%>', '<%=gUnitName%>');
    <%} else{        
      // Display all the Units with Unit Number %> 
        //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start
        //If allow_to_copy is true then allow to copy
        if(document.copyProposalForm.budgetFlag.checked == false){
         document.getElementById('unitDiv').style.display = 'block';
        }
        //Commented and added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - start
        <!--if(allow_to_copy == true && document.copyProposalForm.budgetFlag.checked == true)-->
        if(document.copyProposalForm.budgetFlag.checked == true){
        <%--if((msgKeyForFinalFlag!=null && !msgKeyForFinalFlag.equals("0")) || (msgKeyForAllVersionFlag!=null && !msgKeyForAllVersionFlag.equals("0"))) { %>  
        var msg='';
           <%if(msgKeyForFinalFlag != null && !"0".equals(msgKeyForFinalFlag)){%>
                msg = '<bean:message bundle="budget" key="<%=msgKeyForFinalVersion%>"/>';
            <%}else{
            if(msgKeyForAllVersionFlag != null && !"0".equals(msgKeyForAllVersionFlag)){%>
                msg = '<bean:message bundle="budget" key="<%=msgKeyForAllVersion%>"/>';
            <%}%>       
       <% } %>
       if (msg.length > 0 && confirm(msg)==false){                     
            return false;                  
        } 
        
        
        <% }--%>
        document.getElementById('unitDiv').style.display = 'block';
        }
        //Added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - end
    <%}%>
    //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - end
    // Case# 2594:Problem with copy proposal - End 
}
function copyProposalInfo(unitNumber, unitName){
var narrative = "N";
if (document.copyProposalForm.narrativeFlag.checked == true) {
  narrative = "Y";
}

var questionnaire = "N";
if (document.copyProposalForm.questionnaireFlag.checked == true) {
  questionnaire = "Y";
}
// document.copyProposalForm.action ="<%=request.getContextPath()%>/copyProposalDetails.do?unitNumber="+unitNumber+"&unitName="+unitName+"&selectedVersion="+selectedValue+"&narrative="+narrative+"&Menu_Id=003" ; 
document.copyProposalForm.action ="<%=request.getContextPath()%>/copyProposalDetails.do?unitNumber="+unitNumber+"&unitName="+unitName+"&selectedVersion="+selectedValue+"&narrative="+narrative+"&questionnaire="+questionnaire+"&Menu_Id=003" ; 
document.copyProposalForm.submit();

}

function setBudgetFlag() {
    if (document.copyProposalForm.budgetFlag.checked == true) {
        selectedValue = "A";
        document.getElementById('enable').style.display = 'block';
        document.getElementById('disable').style.display = 'none';
    } else {
        document.getElementById('enable').style.display = 'none';
        document.getElementById('disable').style.display = 'block';
        selectedValue = "";
    }
}

function goToGeneralInfo(){
  document.copyProposalForm.action ="<%=request.getContextPath()%>/getGeneralInfo.do?proposalNumber=<%=session.getAttribute("proposalNumber"+session.getId())%>"; 
  document.copyProposalForm.submit();
}
</script>

</head>
<body>

<html:form action="/getCopyProposal.do" >
<table width="100%" height="100%"  border="0" cellpadding="0" cellspacing="0" class='table'>

<tr>
    <td class='core'>    
    <table width="100%" height="100%" align=center  border="0" cellpadding="5" cellspacing="1" class='tabtable'>
        
        
        
    <tr>
        <td>
            <div id="helpText" class='helptext'>            
                <bean:message bundle="proposal" key="helpTextProposal.CopyProposal"/>  
            </div>                                     
        </td>
    </tr>    
    <%if(unitCount == 0){%>
    <tr>
        <td>
        <font color="red">
            <li><bean:message bundle="proposal" key="copyProposal.noRight"/></li>
        </font>
        </td>
    </tr>
    <%}else{%> 
    <tr>
        
        <td>    
            <div id="main" style="display:block;" >
            <table width="100%" height="100%"  cellpadding="0" cellspacing="5">
                
                
                <tr>
                    
                    <td align="left" class='copybold'>                        
                        
                        <html:checkbox property='budgetFlag' value="Y" disabled="<%=hasBudget%>" onclick="setBudgetFlag();"/>  <bean:message bundle="proposal" key="copyProposal.budget"/> 
                    </td>        
                </tr>
                <tr>
                    
                    <td   class='copybold' > 
                        <div id="disable">
                            &nbsp;&nbsp;&nbsp;&nbsp;   <html:radio property='budgetVersionFlag' value="A" disabled="true" onclick="setVersionValue('A');"/>   <bean:message bundle="proposal" key="copyProposal.allVersions"/><br> 
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<html:radio property='budgetVersionFlag' value="F" disabled="true" onclick="setVersionValue('F');"/>   <bean:message bundle="proposal" key="copyProposal.finalVersion"/>       
                        </div>  
                        <div id="enable" style='display: none;'>
                            &nbsp;&nbsp;&nbsp;&nbsp;   <html:radio property='budgetVersionFlag' value="A" disabled="false" onclick="setVersionValue('A');"/>   <bean:message bundle="proposal" key="copyProposal.allVersions"/><br> 
                            <%if(budgetFinalVersionExist.equals("0")) {%>
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<html:radio property='budgetVersionFlag' value="F" disabled="true" onclick="setVersionValue('F');"/>   
                            <%} else {%>
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<html:radio property='budgetVersionFlag' value="F" disabled="false" onclick="setVersionValue('F');"/>   
                            <%}%>
                            <bean:message bundle="proposal" key="copyProposal.finalVersion"/>       
                        </div>
                    </td> 
                </tr>
                <tr>
                    <td align="left" class='copybold'>                        
                        <html:checkbox property='narrativeFlag' value="Y" disabled="<%=hasNarrative%>"/> <bean:message bundle="proposal" key="copyProposal.narrative"/> 
                    </td> 
                </tr>  
                <tr>
                    <td align="left" class='copybold'>                        
                        <html:checkbox property='questionnaireFlag' value="Y" /> <bean:message bundle="proposal" key="copyProposal.questionnaire"/> 
                    </td>                
                </tr> 
                <!--Added for COEUSQA-3509 : Add warning message to Copy proposal window - start -->
                <!--If proposal is grants gov. proposal then we need to display the warning msg while copying the proposal -->
                 <tr>
                    <td align="left" class='copy'>  
                       <font color="red">
                       <%if(ggProposal != null && ggProposal){%>
                            <br> &nbsp;&nbsp;&nbsp; <bean:message bundle="proposal" key="proposal.ggProposalMsg"/> 
                       <%}%>
                    </td>                   
                </tr>
                <!--Added for COEUSQA-3509 : Add warning message to Copy proposal window - end -->
            </table>
            </div>
        </td>
    </tr>
   
    <tr>
        <td class="copybold"><div id="error" style="display:none;" >
            <bean:message bundle="proposal" key="copyProposal.invalidQuestionnaire"/>
      </div>  </td>
    </tr>
     <tr> <td> 
         
         
     </td> </tr>
    
     
     </table>
     </td>
</tr>

<tr> 
    <td class='savebutton'>
        <div id="mainbutton" style="display:block;" >
        <html:button property="Ok" styleClass="clbutton" disabled="<%=modeValue%>" onclick="proposalCopy()">
            <bean:message bundle="proposal" key="copyProposal.ok"/>
        </html:button>
        &nbsp;&nbsp;&nbsp;
      <%--  <html:button property="Close" styleClass="clbutton" value="Cancel" onclick="JavaScript:window.history.back();"/>  --%>
      </div>
   </td>
   
 </tr>
 
 <tr> 
    <td class='savebutton'>
        <div id="errorbutton" style="display:none;" >
        <html:button property="Ok" styleClass="clsavebutton"  onclick="proposalCopy()">
            Ok
        </html:button>
        &nbsp;&nbsp;&nbsp;
        
        <html:button property="Cancel" styleClass="clsavebutton" onclick="goToGeneralInfo()">
            Cancel
        </html:button>
      <%--  <html:button property="Close" styleClass="clbutton" value="Cancel" onclick="JavaScript:window.history.back();"/>  --%>
      </div>
   </td>
   
 </tr>

<tr>
    <td width='100%'>
    <div id='unitDiv' class='core'  style='display: none;'>
    <table width="100%" height="100%" align=center border="0" cellpadding="0" cellspacing="0" class="tabtable">
     <tr>
        <td>
            <table width="100%" align=center border="0" cellpadding="0" cellspacing="0" class="tableheader">
            <tr>
                <td>
                    <bean:message bundle="proposal" key="copyProposal.unitHeader"/>
                </td>
            </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td height='15'>
        </td>
    </tr>
    
     <tr>
        <td  align="left" valign="top" class='copybold'>
          <table  width="99%"  border="0" align="center" cellpadding="4" cellspacing="1" class="tabtable">  
          <tr>            
                <td class='theader' width='15%'><bean:message bundle="proposal" key="generalInfoProposal.unitNumber"/></td>
                <td class='theader' align="left"><bean:message bundle="proposal" key="generalInfoProposal.unitName"/></td>          
          </tr>
            
           
            <%
                String strBgColor = "#DCE5F1";
                 int index=0;
                %>
            <logic:iterate id="units" name="vecUserUnits" type="org.apache.commons.beanutils.DynaBean">
                       <% String unitNumber = (String)units.get("unitNumber");
                          String unitName  = (String)units.get("unitName");  
                          String link = "javascript:copyProposalInfo('"+unitNumber+"','"+unitName+"')";
                        %>
                       <%
                           if (index%2 == 0) {
                                strBgColor = "#D6DCE5"; 
                            }
                           else { 
                                strBgColor="#DCE5F1"; 
                             }  %>   
                        <tr  bgcolor="<%=strBgColor%>" onmouseover="className='TableItemOn'" onmouseout="className='TableItemOff'">                        
                            <td>
                                     <html:link  href="<%=link%>">
                                              <u>  <bean:write name="units" property="unitNumber"/> </u> 
                                      </html:link>                            
                            </td>                            
                            <td class='copy'>
                                <html:link  href="<%=link%>">
                                    <bean:write name="units" property="unitName"/>
                                </html:link>
                            </td>                  
                        </tr>
                        <% 
                            index++;
                       %>   
            </logic:iterate>                           
          </table>
        </td>
      </tr> 
    
      
      <tr>
        <td height='15'>
        </td>
    </tr>
     </table>
     </div>
   </td>
   </tr>
   <%}%>
</table>

<tr>
    <td height='15'>
    </td>
</tr>
 
</html:form>
<script>
      var help = '<bean:message bundle="proposal" key="helpTextProposal.CopyProposal"/>';
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
