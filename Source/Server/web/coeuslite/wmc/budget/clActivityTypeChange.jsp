<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<%@ page import="edu.mit.coeus.budget.bean.BudgetInfoBean"%>
<jsp:useBean  id="budgetInfoBean"  scope="session" class="edu.mit.coeus.budget.bean.BudgetInfoBean"/>
<%
    String versionNumber = request.getParameter("versionNumber");
    String proposalNumber = budgetInfoBean.getProposalNumber();
    String pageNavigate =(String) request.getAttribute("pageNavigate");
    String copyPeriod = (String) request.getAttribute("copyPeriod");
    String navigator = null;
    if(pageNavigate.equals("OpenBudgetVersion")){
        navigator="/openBudgetVersion.do";        
     }else if(pageNavigate.equals("CopyBudgetVersion")){
        navigator="/copyBudgetVersion.do";        
     }else if(pageNavigate.equals("NewBudgetVersion")){
        navigator="/addNewVersion.do";         
     }else if(pageNavigate.equals("GetBudgetVersion")){
        navigator="/getBudget.do";         
     }
     // 3681: Message about proposal's activity type changing- Start
     String oldActivityType =(String) request.getAttribute("oldActivityType");
     int oldActivity = -1;
     if(oldActivityType != null && !"".equals(oldActivityType)){
         oldActivity = Integer.parseInt(oldActivityType);
     }
     // 3681: Message about proposal's activity type changing- End
%>
<html:html>
<head><title>Jsp Page</title>
<script language="JavaScript" type="text/JavaScript">
    function activityTypeCalculate(calculate){  
        var proposalNum = "<%=proposalNumber%>";   
        if(calculate == "Y")
        { 
           document.budgetVersions.action = "<%=request.getContextPath()%>"+"<%=navigator%>?calculate=Y&proposalNumber="+proposalNum+"&versionNumber="+<%=versionNumber%>+"&copyPeriod="+<%=copyPeriod%>; 
        }else if(calculate == "N") {            
            document.budgetVersions.action = "<%=request.getContextPath()%>"+"<%=navigator%>?calculate=N&proposalNumber="+proposalNum+"&versionNumber="+<%=versionNumber%>+"&copyPeriod="+<%=copyPeriod%>; 
        }
         document.budgetVersions.submit();      
            
          // Hide the code in first div tag
                document.getElementById('activityTypeDiv').style.display = 'none';
         // Display code in second div tag
                document.getElementById('saveDiv').style.display = 'block';     
}
</script>
</head>
<body>

<html:form action="/getBudgetVersions.do" method="post">
<div id='activityTypeDiv'>
<table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0" class="table">
<tr>
    <td height='3'></td>
</tr>
<tr>
    <td colspan="4" align="left" valign="top" class='core'>
        <table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0" class="tabtable">
            <tr>
                <td colspan="4" align="left" valign="top">
                    <table width="100%" height="20"  border="0" cellpadding="0" cellspacing="0" class="tableheader">
                    <tr>
                        <td> 
                           <%  if(oldActivity == 0){ 
                            // No Rate information available for the proposal
                            // Added for 3681: Message about proposal's activity type changing%>
                            <bean:message bundle="budget" key="activityTypeChange.ratesNotAvailable.Header"/> 
                            <%  } else { %>
                            <bean:message bundle="budget" key="activityTypeChange.Header"/> 
                           <%   } %>
                        </td>
                    </tr>
                    </table>
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
                            <td class='copy' nowrap>&nbsp;
                                                    <%  if(oldActivity == 0){ 
                                                    // No Rate information available for the proposal
                                                    // Added for 3681: Message about proposal's activity type changing %>
                                                    <bean:message bundle="budget" key="activityTypeChange.RatesNotAvailable"/> 
                                                    <%  } else { %>
                                                    <bean:message bundle="budget" key="activityTypeChange.Message"/>      
                                                    <%   } %>
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
        
        <td class='savebutton'>
            <html:button accesskey="y" property="Yes" styleClass="clsavebutton" onclick="activityTypeCalculate('Y')">
                <bean:message bundle="budget" key="activityTypeChange.Yes"/>
            </html:button>
            &nbsp;&nbsp;&nbsp;
            <html:button accesskey="n" property="No" styleClass="clsavebutton" onclick="activityTypeCalculate('N')">
                <bean:message bundle="budget" key="activityTypeChange.No"/>
            </html:button>

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

</body>
</html:html>

