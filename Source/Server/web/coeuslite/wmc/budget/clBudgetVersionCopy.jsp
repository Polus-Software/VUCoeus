<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<%@ page import="edu.mit.coeus.budget.bean.BudgetInfoBean"%>
<jsp:useBean  id="budgetInfoBean"  scope="session" class="edu.mit.coeus.budget.bean.BudgetInfoBean"/>
<%
    String copyVersionNumber = request.getParameter("versionNumber");
    String proposalNumber = budgetInfoBean.getProposalNumber();
    String msgKeyForPeriodOne = (String)request.getAttribute("inactiv_type_for_budget_one");
    String msgKeyForPeriodOneKey ="budgetSelect_exceptionCode."+msgKeyForPeriodOne;
    String msgKeyForPeriodAll = (String)request.getAttribute("inactive_type_for_budget_all");
    String msgKeyForPeriodAllKey ="budgetSelect_exceptionCode."+msgKeyForPeriodAll;
    String mode = (String)session.getAttribute("mode"+session.getId());
    boolean readOnly = false;
    if(mode!= null && mode.equals("display")){
      readOnly = true;
    }
%>
<html:html>
<head><title>Jsp Page</title>

<script language="JavaScript" type="text/JavaScript">
    var allowcopy = 'true';
    function budgetVersionCopy(versionNo){ 
    //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start
       <%if((msgKeyForPeriodOne!=null && !msgKeyForPeriodOne.equals("0")) || (msgKeyForPeriodAll!=null && !msgKeyForPeriodAll.equals("0"))) { %>
       var msg='';
       if(document.getElementById('CopyPeriodOne').checked){
            <%if(msgKeyForPeriodOne != null && !"0".equals(msgKeyForPeriodOne)){%>
                msg = '<bean:message bundle="budget" key="<%=msgKeyForPeriodOneKey%>"/>';
            <%}%>
       }else{
            <%if(msgKeyForPeriodAll != null && !"0".equals(msgKeyForPeriodAll)){%>
                msg = '<bean:message bundle="budget" key="<%=msgKeyForPeriodAllKey%>"/>';
            <%}%>
       }
                   
        
        if (msg.length > 0 && confirm(msg)==false){                     
            return false;                  
        }     
        
        if(document.budgetVersions.copyPeriodCode[0].checked)
        {     
           //Commented/Added for case#3698 - Lite-Error Message-Copy Budget Version - start
           //document.budgetVersions.action = "<%=request.getContextPath()%>/copyBudgetVersion.do?actionFrom=Copy&proposalNumber="+<%=proposalNumber%>+"&versionNumber="+<%=copyVersionNumber%>+"&copyPeriod=1" ; 
           document.budgetVersions.action = "<%=request.getContextPath()%>/copyBudgetVersion.do?actionFrom=Copy&proposalNumber=<%=proposalNumber%>&versionNumber=<%=copyVersionNumber%>&copyPeriod=1";
        }else{            
           //document.budgetVersions.action = "<%=request.getContextPath()%>/copyBudgetVersion.do?actionFrom=Copy &proposalNumber="+<%=proposalNumber%>+" &versionNumber="+<%=copyVersionNumber%>+"&copyPeriod=2" ; 
           document.budgetVersions.action = "<%=request.getContextPath()%>/copyBudgetVersion.do?actionFrom=Copy&proposalNumber=<%=proposalNumber%>&versionNumber=<%=copyVersionNumber%>&copyPeriod=2";
           //Commented/Added for case#3698 - Lite-Error Message-Copy Budget Version - end
        }
         document.budgetVersions.submit();         
    <% } else{ %>
    
    if(document.budgetVersions.copyPeriodCode[0].checked)
        {     
           //Commented/Added for case#3698 - Lite-Error Message-Copy Budget Version - start
           //document.budgetVersions.action = "<%=request.getContextPath()%>/copyBudgetVersion.do?actionFrom=Copy&proposalNumber="+<%=proposalNumber%>+"&versionNumber="+<%=copyVersionNumber%>+"&copyPeriod=1" ; 
           document.budgetVersions.action = "<%=request.getContextPath()%>/copyBudgetVersion.do?actionFrom=Copy&proposalNumber=<%=proposalNumber%>&versionNumber=<%=copyVersionNumber%>&copyPeriod=1";
        }else{            
           //document.budgetVersions.action = "<%=request.getContextPath()%>/copyBudgetVersion.do?actionFrom=Copy &proposalNumber="+<%=proposalNumber%>+" &versionNumber="+<%=copyVersionNumber%>+"&copyPeriod=2" ; 
           document.budgetVersions.action = "<%=request.getContextPath()%>/copyBudgetVersion.do?actionFrom=Copy&proposalNumber=<%=proposalNumber%>&versionNumber=<%=copyVersionNumber%>&copyPeriod=2";
           //Commented/Added for case#3698 - Lite-Error Message-Copy Budget Version - end
        }
         document.budgetVersions.submit();
    <%} %> 
    }
    //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - end
</script>
</head>
<body>

<html:form action="/getBudgetVersions.do" method="post">
<table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0" class="table">
<tr>
    <td height='3'></td>
</tr>

<tr>
    <td colspan="4" align="left" valign="top">
    <table width="99%"  border="0" align="center" cellpadding="0" cellspacing="0" class="tabtable">
    <tr>
        <td colspan="4" align="left" valign="top">
            <table width="100%" height="20"  border="0" cellpadding="0" cellspacing="0" class="tableheader">
            <tr>
                <td> <bean:message bundle="budget" key="budgetVersionCopy.CopyBudget"/> </td>
            </tr>
            </table>
        </td>
    </tr>
      <tr class='copy' align="left">
        <td>
            <font color="red"  >
                <html:messages id="message" message="true">
                          <!-- If lock is acquired by another user, then show this message -->
                           <html:messages id="message" message="true" property="acqLock">                
                               <li><bean:write name = "message"/></li>
                          </html:messages>
                </html:messages>
            </font>
        </td>
        </tr>     

    <tr>
        
        <td class="theader" align=left height='40'>
            &nbsp;&nbsp;<bean:message bundle="budget" key="budgetVersionCopy.NewVersionMessage"/>  <%=copyVersionNumber%> 
        </td>
    </tr>
    <tr>
        <td height='10'>
        </td>
    </tr>
    <tr>
        
        <td>
            <table width="100%" height="20"  border="0" cellpadding="0" cellspacing="0" >
            <tr>
                <td width='30%' nowrap>&nbsp;
                
                </td>
                <td width="70%" class='copy'>
                    <html:radio value="1" property="copyPeriodCode" styleId="CopyPeriodOne"/> 
                        <bean:message bundle="budget" key="budgetVersionCopy.CopyPeriodOne"/> 
                </td>
            </tr>
            <tr>
                <td width='30%' nowrap>&nbsp;
                    
                </td>
                <td width="70%" class='copy'>
                   <%-- <input type=radio name=1>Copy All Periods  --%>
                    <html:radio value="2" property="copyPeriodCode" styleId="CopyAllPeriods"/>
                        <bean:message bundle="budget" key="budgetVersionCopy.CopyAllPeriods"/>  
                </td>
            </tr>
            </table>
        </td>
    </tr>       
    <tr>
        <td height='20'></td>
    </tr>
    </table>    
    </td>
    </tr>
    <tr>
        <td height='5'>
        </td>
    </tr>
    <tr>
        <td>&nbsp;
            
        </td>
        <td align=center>


            <html:button property="Ok" styleClass="clsavebutton" disabled="<%=readOnly%>" onclick="return budgetVersionCopy('')">
                <bean:message bundle="budget" key="budgetVersionCopy.Ok"/>
            </html:button>
            &nbsp;&nbsp;&nbsp;
            <html:button property="Close" styleClass="clsavebutton" value="Cancel" onclick="JavaScript:window.history.back();"/>

       </td>
    </tr>
    <tr>
        <td height='5'>
        </td>
    </tr>
    
</table>

</html:form>

</body>
</html:html>

