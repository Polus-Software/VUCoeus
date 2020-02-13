<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"  prefix="coeusUtils"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<%@ page import="java.util.List,java.lang.Integer, edu.mit.coeus.s2s.bean.FormInfoBean, edu.mit.coeus.s2s.validator.S2SValidationException, edu.mit.coeus.exception.CoeusException"%>
<jsp:useBean  id="PROPOSAL_RULES_MAP"  scope="session" class="java.util.Vector"/>
<jsp:useBean id="VALIDATION_FROM" scope="request" class="java.lang.String" />
<jsp:useBean id="NewFinalVersion" scope="request" class="java.lang.String" />
<html:html>
<head>
<title>Validation Rules</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<% String lineImage = request.getContextPath()+"/coeusliteimages/line4.gif";
    String proposalNumber=(String)session.getAttribute("proposalNumber"+session.getId());
    String actionType = (String)session.getAttribute("SUBMIT_ACTION");
    boolean proposalSuccess = false;
    int currentErrModule    = -1;
    int currentWarnModule   = -1;
%>
<script>
    var validationPresent = false;
    var errorPresent = false;
    var warningPresent = false;
    function showProposalDetails(){
        var type = '<%=actionType%>';
        if(type!='null') {
            document.validateProposalForm.ok.disabled=true;//Added for COEUSDEV-178:Clicking on ok button causes multiple submissions
            var data = true ;
            if(validationPresent && !errorPresent){
               data = confirm("<bean:message bundle="proposal" key="proposal.submitForProposal"/>")
            }
            if(data && !errorPresent) {
                document.validateProposalForm.action = "<%=request.getContextPath()%>/submitForApproval.do?&proposalNumber=<%=proposalNumber%>&SUBMIT_FOR_APPROVE=SUBMIT_FOR_APPROVE";
                document.validateProposalForm.submit();
            } else {
                document.validateProposalForm.action = "<%=request.getContextPath()%>/getGeneralInfo.do?&proposalNumber=<%=proposalNumber%>";
                document.validateProposalForm.submit();
            }
        } else {
            var calledFrom = '<%=VALIDATION_FROM%>';
            if(calledFrom == 'BV' || calledFrom == 'BS') {
                var statusChange = 'I';
                if(!errorPresent){
                    if(warningPresent){
                        if(confirm("<bean:message bundle="budget" key="budget.statuschange.confirm"/>")){
                            statusChange = 'C';
                        }
                     }else{
                            statusChange = 'C';
                     }
                }
                if(calledFrom == 'BV'){
                    document.validateProposalForm.action = "<%=request.getContextPath()%>/getBudgetVersions.do?PAGE=V&actionFrom=BudgetValidation&statusChange="+statusChange+"&versionNumber="+<%=NewFinalVersion%>;
                }else{
                    document.validateProposalForm.action = "<%=request.getContextPath()%>/getBudgetSummary.do?proposalNumber=<%=proposalNumber%>&statusChange="+statusChange+"&versionNumber="+<%=NewFinalVersion%>;
                }
                document.validateProposalForm.submit();
            }else if(calledFrom == 'B') {
                document.validateProposalForm.action = "<%=request.getContextPath()%>/getBudgetSummary.do?proposalNumber=<%=proposalNumber%>&versionNumber=<%=NewFinalVersion%>";
                document.validateProposalForm.submit();
            <%-- Added for COEUSDEV-858 Add a validation button to proposal summary in lite (approval screens) for business rules Validations - start--%>
            }else if(calledFrom == 'VC'){
                document.validateProposalForm.action = "<%=request.getContextPath()%>/displayProposal.do?proposalNo=<%=proposalNumber%>";
                document.validateProposalForm.submit();
            <%-- Added for COEUSDEV-858 Add a validation button to proposal summary in lite (approval screens) for business rules Validations - end--%>
            }else{
                document.validateProposalForm.action = "<%=request.getContextPath()%>/getGeneralInfo.do?&proposalNumber=<%=proposalNumber%>";
                document.validateProposalForm.submit();
            }
        }
    }
   
    function validate() {
         var calledFrom = '<%=VALIDATION_FROM%>';
         if((calledFrom == 'BV' || calledFrom == 'BS')
                                && CLICKED_LINK != '') {
            var statusChange = 'I';
            if(calledFrom == 'BV'){
                PAGE_NAME = "<bean:message bundle="budget" key="budgetVersions.BudgetVersion"/>";
            }else{
                PAGE_NAME = "<bean:message bundle="budget" key="summaryHeader.BudgetSummary"/>";
            }
            //Modified to remove the white space for save confirmation message
            if(confirm("<bean:message key="confirmation.save"/> "+PAGE_NAME+"?")) {
                if(!errorPresent){
                    if(warningPresent){
                        if(confirm("<bean:message bundle="budget" key="budget.statuschange.confirm"/>")){
                            statusChange = 'C';
                        }
                    }else{
                            statusChange = 'C';
                    }
                } 
                
                if(calledFrom == 'BV'){
                    LINK = "<%=request.getContextPath()%>/getBudgetVersions.do?actionFrom=SaveAfterValidate&statusChange="+statusChange+"&versionNumber="+<%=NewFinalVersion%>;
                }else{
                    LINK = "<%=request.getContextPath()%>/budgetSummary.do?proposalNumber=<%=proposalNumber%>&statusChange="+statusChange+"&versionNumber="+<%=NewFinalVersion%>;
                }
                while(CLICKED_LINK.indexOf('&')!=-1){
                    CLICKED_LINK = CLICKED_LINK.replace("&","$");
                }   
                if(LINK.indexOf('?')!=-1) {
                    document.validateProposalForm.action = LINK+"&CLICKED_LINK="+CLICKED_LINK;
                } else {
                    document.validateProposalForm.action = LINK+"?CLICKED_LINK="+CLICKED_LINK;
                }
                document.validateProposalForm.submit();
                return false;
            }
          }           
         return true;
        }
    
</script>
</head>
<body>
<html:form action="/validateProposal.do" method="post">
<table width="100%" height="100%"  border="0" cellpadding="0" cellspacing="0" class="table" >
   
    <!--Warning Messages prior to validation - case 2158:Budget Validations : Start-->
   <%
        String warningMessage = (String)request.getAttribute("WarningMessage");
        if(warningMessage != null) {%>
        <table width="100%">
            <tr><td>
                <table align="center" width="100%" border="0" cellspacing="0" cellpadding="0" class="theader">
                    <tr><td>
                      &nbsp;Warning:      
                    </td></tr>
                </table> 
            </td></tr>
            <tr><td>
                <table align="center" width="100%" border="0" cellspacing="0" cellpadding="0" class="tabtable">
                    <tr><td>
                        <font color="blue">
                            <b><%out.print(warningMessage);%></b>
                        </font>
                    </td></tr>
                </table>
            </td></tr>
        
        </table> 
        <%}%>   
   <!--Warning Messages End-->
   
    <!-- Grants Gov Validation Messages - START -->
        <%
        Exception ex = (Exception)request.getAttribute("Exception");
        if(ex != null) {%>
        <tr><td>
                <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tabtable" >
                    <tr><td>
                            <table width="100%" border="0" cellspacing="0" cellpadding="2" class="theader">
                                <tr><td>
                                        <%if (ex instanceof CoeusException){%>
                                            Error:
                                         <%}else{%>
                                            Grants Gov Error:
                                          <%}%>
                                    </td>
                                </tr>
                            </table>
                    </td></tr>
                    
                    <tr><td>
                            <div id="errorDetails" style="overflow:hidden">
                                <table align="center" width="100%" border="0" cellspacing="0" cellpadding="0" class="tabtable">
                                    
                                    <tr><td class="copy">
                                            <table width="100%" border="0"> 
                                                <tr><td>
                                                        <img src="<%=request.getContextPath()%>/coeusliteimages/error.gif" hspace="5" vspace="5">
                                                    </td>
                                                    <td class="copybold">
                                                        <font color="red">
                                                            <%
                                                            String exceptionMessage = ex.getMessage();
                                                            //if message has a link. display link in next line
                                                            int index = exceptionMessage.indexOf("<a");
                                                            if(index != -1){
                                                            exceptionMessage = exceptionMessage.substring(0, index) +"<br>"+exceptionMessage.substring(index, exceptionMessage.length());
                                                            }
                                                            out.print(exceptionMessage);
                                                        %></font>
                                                </td></tr>
                                            </table>
                                    </td></tr>
                                    <tr><td>
                                            <table align="center" width="98%" border="0" cellspacing="0" cellpadding="2" class="tabtable">
                                                <%
                                                if(ex instanceof S2SValidationException) {%>
                                                <tr><td><font color="red"><b>Please Correct the Following Errors:</b></font></td></tr>
                                                <%
                                                S2SValidationException s2SValidationException = (S2SValidationException)ex;
                                                List errList = s2SValidationException.getErrors();
                                                S2SValidationException.ErrorBean errorBean;
                                                FormInfoBean formInfoBean;
                                                for(index = 0; index < errList.size(); index++) {
                                                errorBean = (S2SValidationException.ErrorBean)errList.get(index);
                                                if(errorBean.getMsgObj() instanceof FormInfoBean) {
                                                formInfoBean = (FormInfoBean)errorBean.getMsgObj();
                                                %><tr class="rowLine table"><td><b><%=formInfoBean.getFormName()%></b></td></tr>
                                                <%
                                                }else {%>
                                                <tr class="rowLine"><td><%=errorBean.getMsgObj()%></td></tr>
                                                <%    }
                                                }
                                                }
                                                %>
                                    </table> <br>  </td></tr>
                                </table>
                            </div>
                    </td></tr>
        </table> </td></tr>
        <%}%>
        <!-- Grants Gov Validation Messages - END-->
        
        <tr >
        <td height='100%'>
        <%if((PROPOSAL_RULES_MAP!=null)&&(PROPOSAL_RULES_MAP.size()>0)){%>
          <script>
            validationPresent = true;
          </script>
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tabtable" >
                    <tr>
                        <td width="100%" colspan='3' class='copybold'>
                            <table width="100%" border='0' class='tableheader'>
                                <tr>
                                     <td>
                                            <%--<bean:message bundle="proposal" key="clProposalValidationRules.Proposal"/>--%>
                                            <bean:message bundle="proposal" key="clProposalValidationRules.FailedValidations"/>
                                            <font color='Red'>
                                                <%--<bean:write name="proposalNumber"/>--%>
                                                <%=proposalNumber%>
                                            </font>
                                     <td>
                                </tr>
                            </table>
                        </td>
                    </tr>   
                    <tr>
                        <td  colspan='3' height='100%'>
                                            <div id="helpText" class='helptext'>            
                                                <bean:message bundle="proposal" key="helpTextProposal.Validate"/>  
                                            </div>  
                        </td>
                    </tr>
                 
                 <logic:present name ="PROPOSAL_RULES_MAP" scope = "session"> 
                 <logic:iterate id="validationRules"   name="PROPOSAL_RULES_MAP" 
                                                type="org.apache.commons.beanutils.DynaBean">
                     <% String proposalRuleType =(String) validationRules.get("ruleType");
                        int submoduleCode = Integer.parseInt((String) validationRules.get("submoduleCode"));
                          if(proposalRuleType != null && proposalRuleType.equalsIgnoreCase("V")){proposalRuleType="Validation";} %>
                    <%if(validationRules.get("ruleCategory") != null && validationRules.get("ruleCategory").equals("E") && submoduleCode!=currentErrModule){
                        currentErrModule = submoduleCode; %>   
                        <script>
                            errorPresent = true;
                        </script>
                    <tr>
                        <td class="copybold" colspan="2">
                            <font color=red>The 
                            <%if(currentErrModule==1){%>
                                budget for
                            <%}%>
                            proposal <%=proposalNumber%> failed for following Errors. Please fix them.</font>
                        </td>
                    </tr>
                   <tr>
                        <td height="10" class="copybold" colspan="2">
                            &nbsp;
                        </td>
                    </tr>                    
                    <%} else if(validationRules.get("ruleCategory") != null && validationRules.get("ruleCategory").equals("W") && submoduleCode!=currentWarnModule){
                         currentWarnModule = submoduleCode;%>
                         <script>
                            warningPresent = true;
                        </script>
                    <tr>
                        <td class="copybold" colspan="2">
                            <font color=blue>The 
                            <%if(currentWarnModule==1){%>
                                budget for
                            <%}%>
                            proposal <%=proposalNumber%> validated with the following warnings.</font>
                        </td>
                    </tr>
                   <tr>
                        <td height="10" class="copybold" colspan="2">
                            &nbsp;
                        </td>
                    </tr>                    
                    <%}%>
                    <tr>
                        <td width="7%" class="copybold">Department:</td>
                        <td width="80%" class="copy"><coeusUtils:formatOutput name="validationRules" property="unitName" />
                        </td>
                    </tr>                    
                    <tr>
                        <td colspan='2' class="copy"><coeusUtils:formatOutput name="validationRules" property="description" />
                        </td>
                    </tr>
                    <tr>
                        <td colspan='3' width="100%">&nbsp;
                        </td>
                    </tr>
                    <tr>
                        <td colspan='3' width="100%">&nbsp;
                        </td>
                    </tr>
                 </logic:iterate>
                 </logic:present>
            </table>
            <script>
                  var help = '<bean:message bundle="proposal" key="helpTextProposal.Validate"/>';
                  help = trim(help);
                  if(help.length == 0) {
                    document.getElementById('helpText').style.display = 'none';
                  }
                  function trim(s) {
                        return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
                  }  
            </script>
         <%}else{
                proposalSuccess = true;
                //Check for Grants Gov Validation Messages
            if(ex == null) {%>
            <table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" class="table" >
            <tr>
            <td  height='100%'>
             &nbsp;
            </td>
        </tr>
                <tr>
                    <td  height='100%' align=center>
                    &nbsp;<bean:message bundle="proposal" key="clProposalValidationRules.SuccessfullyPassed" />
                    
                    </td>
                </tr>
                <tr>
            <td  height='100%'>
             &nbsp;
            </td>
        </tr>
            </table>
         <%}
                }%>   
        </td>
        </tr>
        <tr>
            <td align=center width='30%' class='savebutton'>
            <html:button value="OK"  property="ok" styleClass="clsavebutton" onclick="showProposalDetails()"  />
            </td>
        </tr>
   
    </table>
</html:form>
</body>
</html:html>