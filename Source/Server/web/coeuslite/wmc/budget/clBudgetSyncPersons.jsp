<%@page import="java.util.Vector,edu.mit.coeuslite.utils.ComboBoxBean,
org.apache.struts.validator.DynaValidatorForm,edu.mit.coeuslite.utils.CoeusDynaBeansList" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<%@ page import="edu.mit.coeus.budget.bean.BudgetInfoBean"%>
<jsp:useBean  id="budgetInfoBean"  scope="session" class="edu.mit.coeus.budget.bean.BudgetInfoBean"/>
<jsp:useBean id="syncPersons" scope="session" class="java.util.Vector"/>
<jsp:useBean id="optionsAppointmentTypes" scope="session" class="java.util.Vector"/>
<bean:size id="optionSize" name="optionsAppointmentTypes" />
<bean:size id="syncSize" name="syncPersons" />


<%
String proposalNumber = budgetInfoBean.getProposalNumber();
int versionNumber = budgetInfoBean.getVersionNumber();

String pageNavigate =(String) session.getAttribute("syncPageNavigate");  
String navigate = null;

 if(pageNavigate.equals("openGetBudget")){
    navigate="getBudget";        
 }else if(pageNavigate.equals("OpenBudgetVersion")){
    navigate="openBudgetVersion";        
 }else if(pageNavigate.equals("addNewVersion")){
         navigate="addNewVersion";        
      }
     

%>

<html:html>
<head><title>Jsp Page</title>
 <script language="JavaScript">
    var errValue = false;
    var proposalNum = "<%=proposalNumber%>"; 
    var syncNavigator ="<%=navigate%>";    
    function syncBudgetPersons(){
                document.syncPersonsDynaList.action = "<%=request.getContextPath()%>/updateBudgetSyncPerson.do?&syncPersonsExist=Y&proposalNumber="+proposalNum+"&syncNavigator="+syncNavigator+"&versionNumber="+<%=versionNumber%>;
                document.syncPersonsDynaList.submit();
                   
              // Hide the code in first div tag
                    document.getElementById('syncPersonsDiv').style.display = 'none';
             // Display code in second div tag
                    document.getElementById('saveDiv').style.display = 'block';     
            }
    </script>
    
    <html:javascript formName="syncPersonsDynaList" dynamicJavascript="true" staticJavascript="true"/>

</head>
<body>


<html:form action="/getBudget.do" method="post">  

<div id='syncPersonsDiv'>
<table  width="100%"  border="0" align="center" cellpadding="0" cellspacing="0" class="table">
<tr>
    <td height='2'></td>
</tr>
<tr>
    <td colspan="4" align="left" valign="top" class='core'>
        <table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0" class="tabtable">
        <tr>
            <td colspan="4" align="left" valign="top">
                <table width="100%" height="20"  border="0" cellpadding="0" cellspacing="0" class="tableheader">
                <tr>
                    <td> <bean:message bundle="budget" key="syncBudgetPersons.SyncBudgetPersons" />  </td>
                </tr>
                </table>
            </td>
        </tr>
        <tr><td> <font  color='red'> &nbsp;&nbsp; <b> 
                <logic:messagesPresent>
                <script> errValue = true; </script>
                    <html:errors header="" footer="" />
                </logic:messagesPresent>
                </b> </font> </td> </tr>
        <tr>
            <td align=left height='40'>
                <b>&nbsp;&nbsp;&nbsp;&nbsp;<bean:message bundle="budget" key="syncBudgetPersons.AppointmentTitle" /></b>
            </td>
        </tr>
       
         <tr align="center">
            
            <td  height='75' valign=top>
                <table width="70%" border="0" align="left" cellpadding="2" cellspacing="2">
                <tr>
                    <td width='1%'>
                    </td>
                    <td>
                <table width="70%" border="0" align="left" cellpadding="2" cellspacing="2" class="tabtable">
                <tr align=left>
                    <td class="theader" width='16%'><bean:message bundle="budget" key="syncBudgetPersons.Name" /></td>
                    <td class="theader" width='15%'><bean:message bundle="budget" key="syncBudgetPersons.AppointmentType" /></td>
                    <td class="theader" width='15%'><bean:message bundle="budget" key="syncBudgetPersons.JobCode" /></td>
                </tr>
                
                <logic:iterate id="dynaFormData" name="syncPersonsDynaList" scope="session" property="list" type="org.apache.struts.action.DynaActionForm" indexId="index" >
                <tr>
                    <td height='30' align=left>
                        <html:text size="45" name="dynaFormData" property="fullName" styleClass="cltextbox-color" indexed="true"  readonly="true" onchange="dataChanged()"/>  
                      <%-- <bean:write name="dynaFormData" property="fullName" />  --%>
                    </td>
                    <td align=left>
                        <html:select name="dynaFormData" property="appointmentType" styleClass="clcombobox-small" indexed="true" onchange="dataChanged()">                                     
                               <html:options collection="optionsAppointmentTypes" property="code" labelProperty="description" />
                         </html:select>  
                    </td>
                    <td align=left>
                       <html:text  name="dynaFormData" property="jobCode" maxlength="6" indexed="true" styleClass="textbox" onchange="dataChanged()"/> 
                    </td>
                </tr>
                </logic:iterate>
                
                </table>
                    </td>
                    </tr>
                 </table>   
            </td>
        </tr>
        
        </table>    
    </td>
</tr>
<tr>
    <td class='savebutton'>
        <html:button accesskey="k" property="Ok" value="Save" styleClass="clsavebutton" onclick="syncBudgetPersons()"/>
        &nbsp;&nbsp;
       <%-- <html:submit property="Cancel" styleClass="clbutton" value="Cancel"/> --%>
    </td>
</tr>

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
          if(errValue){
                DATA_CHANGED = 'true';
          }
          LINK = "<%=request.getContextPath()%>/updateBudgetSyncPerson.do?&syncPersonsExist=Y&proposalNumber="+proposalNum+"&syncNavigator="+syncNavigator+"&versionNumber="+<%=versionNumber%>;
          FORM_LINK = document.syncPersonsDynaList;
          PAGE_NAME = "<bean:message bundle="budget" key="syncBudgetPersons.AppointmentTitle" />";
          function dataChanged(){
                DATA_CHANGED = 'true';
          }
    </script>
</body>
</html:html>

