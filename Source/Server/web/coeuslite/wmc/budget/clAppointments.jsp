<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page pageEncoding="UTF-8"%>
<%@ page import="org.apache.struts.validator.DynaValidatorForm,
java.util.Vector,java.util.HashMap"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<jsp:useBean id="multipleAppointmentsPersons" scope="session" class="java.util.HashMap" />
<bean:size id="personSize" name ="multipleAppointmentsPersons" />
<jsp:useBean  id="budgetInfoBean"  scope="session" class="edu.mit.coeus.budget.bean.BudgetInfoBean"/>
<%
   
    String proposalNumber = budgetInfoBean.getProposalNumber();
    int versionNumber = budgetInfoBean.getVersionNumber();
    String pageNavigate =(String) session.getAttribute("syncPageNavigate");
    String navigator = null;
    
     if(pageNavigate.equals("openGetBudget")){
        navigator="/getBudget.do";        
     }else if(pageNavigate.equals("OpenBudgetVersion")){
        navigator="/openBudgetVersion.do";        
     }else if(pageNavigate.equals("addNewVersion")){
         navigator="/addNewVersion.do";        
      }
    
%>

<html:html>
    <head>
        <title>Appointments</title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  
    <script language="JavaScript">
    function multipleAppointments(){
                var proposalNum = "<%=proposalNumber%>"; 
                document.syncPersonsDynaList.action = "<%=request.getContextPath()%>"+"<%=navigator%>?multipleAppointmentExist=Y&proposalNumber="+proposalNum+"&versionNumber="+<%=versionNumber%>;
                document.syncPersonsDynaList.submit();
                
              // Hide the code in first div tag
                    document.getElementById('multipleAppsDiv').style.display = 'none';
             // Display code in second div tag
                    document.getElementById('saveDiv').style.display = 'block';     
            }
    </script>
    </head>
    
    <body>
    <html:form action="/getBudget.do" method="post">
     <div id='multipleAppsDiv'>
        <table width="100%" height="50%"  border="0" cellpadding="0" cellspacing="0" class="table">
            <tr>
                <td align=left valign=top>
                           <table width='100%'  border='0' cellpadding='0' cellspacing='0' class='tabtable'>
                               <tr>
                                  <td> 
                                    <table width="100%" height="20"  border="0" cellpadding="0" cellspacing="0" class="tableheader">
                                        <tr>
                                            <td align="left" valign="top" class='copy'>
                                             
                                                <bean:message bundle="budget" key="personnelAppointments.Appointments"/>
                                            </td>
                                        </tr>
                                    </table>
                                  </td>  
                                </tr>
                                <tr>
                                    <td>
                                    <table width='100%'  border='0' cellpadding='0' cellspacing='0' class='tabtable'>                                        
                                        <tr>                                       
                                            <td nowrap class="copy" align="left" height='35'> 
                                                <%--The selected person has Multiple Appointments. Please select a relevant appointment--%>
                                                &nbsp;&nbsp;
                                                <bean:message  bundle="budget" key="personnelAppointments.HasMultipleAppointments"/>
                                            </td>
                                        </tr>
                                    </table>
                                    </td>
                                </tr>                                                                                                                                                                                           
                              <logic:iterate id="hmapId"  name="multipleAppointmentsPersons" indexId="index">                                                                                    
                                 <tr>
                                    <td class="copybold">   
                                      &nbsp;  <html:text  styleClass="cltextbox-color" name="hmapId" style="cltextbox-long" property="key" readonly="true" indexed="true"/>                                                                                   
                                            <bean:define id="mapKey">
                                                           <bean:write name="hmapId" property="key" />
                                            </bean:define>                                            
                                    </td>
                                 </tr> 
                                  <bean:define id="mapKey" name="hmapId"  property="key" type="java.lang.String" /> 
                                   <tr> <td class='copybold'> <!--bean:write name="personData" property="fullName" /-->  </td> </tr>                                           
                                <tr>
                                    <td> 
                                        <table width='100%'  border='0' cellpadding='6' cellspacing='1' class='tabtable'>
                                            <tr>
                                                <td width='15%' class='theader'> <bean:message bundle="budget" key="personnelAppointments.PrimarySecondary" /> </td>
                                                <td width='9%' class='theader'> <bean:message bundle="budget" key="summaryLabel.StartDate"/></td>
                                                <td width='9%' class='theader'> <bean:message bundle="budget" key="summaryLabel.EndDate" /></td>
                                                <td width='10%' class='theader'><bean:message bundle="budget" key="persons.appointmentType"/></td>
                                                <td width='5%' class='theader'><bean:message bundle="budget" key="persons.jobCode"/></td>
                                                <td width='15%' class='theader'> <bean:message bundle="budget" key="personnelAppointments.JobTitle"/></td>
                                                <td width='5%' class='theader'> <bean:message bundle="budget" key="personnelAppointments.UnitNo"/></td>
                                                <td width='20%' class='theader'> <bean:message bundle="budget" key="personnelAppointments.UnitName"/></td>
                                                <td width='5%' class='theader'> Select </td>
                                            </tr>
                                             <%        String appKey = null;
                                                        String strBgColor = "#DCE5F1";
                                                         int row=0;                                                        
                                                        %>                                            
                                             <bean:define id="beanData"  name="hmapId" property="value" type="java.util.Vector" />                                                            
                                            <logic:iterate id="personData" name="beanData"  type="org.apache.commons.beanutils.DynaBean" >                                                          
                                          
                                           <%             appKey = String.valueOf(row);
                                                               if (row%2 == 0) {
                                                                    strBgColor = "#D6DCE5"; 
                                                                }
                                                               else { 
                                                                    strBgColor="#DCE5F1"; 
                                                                 }  %>   
                                                                                          
                                            <tr bgcolor="<%=strBgColor%>" onmouseover="className='TableItemOn'" onmouseout="className='TableItemOff'">
                                                <td class='copy'> <bean:write name="personData" property="primarySecondaryIndicator" />  </td>
                                                <td class='copy'> <bean:write name="personData" property="appointmentStartDate" />  </td>                                                
                                                <td class='copy'> <bean:write name="personData" property="appointmentEndDate" />  </td>
                                                <td class='copy'> <bean:write name="personData" property="appointmentType" /> </td>
                                                <td class='copy'> <bean:write name="personData" property="jobCode" />  </td> 
                                                <td class='copy'> <bean:write name="personData" property="jobTitle" /> </td>
                                                <td class='copy'> <bean:write name="personData" property="unitNumber" /> </td>
                                                <td class='copy'> <bean:write name="personData" property="unitName" /> </td>
                                           <%--    <td> <html:radio name="personData" property="selected"  indexed="true" value="<%=hmKey%>" />  </td> --%>
                                                <td>  <input type="radio"  style='copy' name='<bean:write name="hmapId" property="key"/>' value='<%=appKey%>' checked >  </td>                                                 
                                            </tr>
                                              <% row++; %>                                              
                                             </logic:iterate>                                                        
                                        </table>
                                    </td>    
                                </tr>                              
                          </logic:iterate>                             
                          </table>
                </td>
            </tr>
            <tr align=center> 
                <td class='savebutton'>                 
                 <html:button accesskey="s"  property="Save" styleClass="clsavebutton"  onclick='multipleAppointments()' >
                           <bean:message bundle="budget" key="adjustPeriod.Save"/>
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
