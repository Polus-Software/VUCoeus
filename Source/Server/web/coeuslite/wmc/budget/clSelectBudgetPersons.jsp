<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<%@ page import="java.util.Vector,java.sql.Date,java.text.SimpleDateFormat,
edu.mit.coeuslite.utils.ComboBoxBean,edu.mit.coeus.utils.ObjectCloner"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld"  prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld"  prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"  prefix="coeusUtils"%>

<jsp:useBean  id="budgetPersonData"  scope="session" class="java.util.Vector"/>
<jsp:useBean  id="appointmentTypesData"  scope="session" class="java.util.Vector"/>

<html:html locale="true">
<head>
<link href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css" rel="stylesheet" type="text/css">
</head>
<title>Budget Persons</title>
<script language="JavaScript">
        var budgetPeriod = "<%=request.getAttribute("SelectedBudgetPeriodNumber")%>";
        var errValue = false;
        function proposalBudget_Actions(actionName){
            if(actionName == "P"){
                document.budgetPersonnelDynaBean.action = "<%=request.getContextPath()%>/selectBudgetPersonnel.do?rowIndex=-1&budgetPeriod="+budgetPeriod;
                document.budgetPersonnelDynaBean.submit();
            }
        }
        
        function addPerson(link){
             var budgetPeriod = "<%=request.getAttribute("SelectedBudgetPeriodNumber")%>";
             document.budgetPersonnelDynaBean.action = "<%=request.getContextPath()%>/" + link + "&budgetPeriod=" + budgetPeriod;
             document.budgetPersonnelDynaBean.submit();
        }

        function selectAll(){
                dataChanged();
		var checkedValue = document.forms[0].selectAllPersonnel.checked;
		for(i=0; i<document.forms[0].elements.length; i++){
			if (document.forms[0].elements[i].type == "checkbox" && document.forms[0].elements[i].name != "selectAllPersonnel") {
				document.forms[0].elements[i].checked = checkedValue;
			}
	  	}
        }

       function saveConfirm(){
            var msg = '<bean:message bundle="budget" key="budgetLabel.saveConfirmation" />';
            <% 
                boolean value = false;
                if(request.getAttribute("saveChanges") != null){
                    value = ((Boolean)request.getAttribute("saveChanges")).booleanValue();
                }
            %>
            if(<%=value%>){
                var budgetPeriod = "<%=request.getAttribute("SelectedBudgetPeriodNumber")%>";
                var reqBudgetPeriod = "<%=request.getAttribute("RequestedBudgetPeriod")%>";
                if (confirm(msg)==true){
                    document.budgetPersonsData.action = "<%=request.getContextPath()%>/calculateBudget.do?budgetPeriod="+budgetPeriod+"&Save=S"+"&requestBudgetPeriod="+reqBudgetPeriod;
                    document.budgetPersonsData.submit();
                }else{
                    var page = "<%=session.getAttribute("pageConstantValue")%>";
                    document.budgetPersonsData.action = "<%=request.getContextPath()%>/getBudgetPersons.do?Period="+reqBudgetPeriod+"&PAGE="+page;
                    document.budgetPersonsData.submit();
                }
            }
       }
        
</script>
<html:base/>

<body onLoad='saveConfirm()'>
<html:errors/>
<html:form action="/AddBudgetPersonnel.do" method="POST">
<!-- New Templates for BudgetPersons Page   -->
<table width="100%" height="100%"  border="0" cellpadding="0" cellspacing="0" class="table">
      <table width="99%"   border="0" align="center" cellpadding="0" cellspacing="0" class="tableheader">
            <tr>
               <td  align="left">
                  <bean:message bundle="budget" key="persons.label"/> 
               </td>      
		</tr>
      </table>
      <tr>
        <td height="100%" align="left" valign="top">
            <table width="99%"  border="0" align="center" cellpadding="0" cellspacing="0" class="tabtable">
                  <tr>
                      <td>
                            <table width="100%"  border="0" cellpadding="2" cellspacing="0" >
                                
                                <tr>
                                    <td>
                                        <img src="<bean:write name='ctxtPath'/>/coeusliteimages/spacer.gif"width="1" height="2">
                                    </td>
                               </tr>
                            </table>
                      </td>
                  </tr>                 
                   <tr>
                        <td>
                             <!--start Iteration here-->
                        <table width="80%" align="center" border="0" cellpadding="3" cellspacing="1" class="tabtable">
                           <logic:present name ="budgetPersonnelDynaBean" scope = "session"> 
                                <tr class='copybold'>
                                    <td nowrap width='5%' align="center" class="theader">
							<input type="checkbox" name="selectAllPersonnel" onclick="selectAll();">
                                    </td>
                                    <td nowrap width='20%' align="center" class="theader">
                                        <bean:message bundle="budget" key="persons.fullName"/>
                                    </td>
                                    <td nowrap width='5%' align="center" class="theader">
                                        <bean:message bundle="budget" key="persons.jobCode"/></td>
                                    <td width='10%'  align="center" class="theader">
                                        <bean:message bundle="budget" key="persons.appointmentType"/>
                                    </td>
                                </tr> 
                 			  <%
                    			String strBgColor = "#DCE5F1";
                     			int index=0;
                    		  %>
						<logic:iterate id="list" name="budgetPersonnelDynaBean" property="list" type="org.apache.struts.action.DynaActionForm" indexId="ctr">
                    		  		<%
                       		  		if (index%2 == 0) {
                            				strBgColor = "#D6DCE5"; 
                        			}
                       				else { 
                            				strBgColor="#DCE5F1"; 
                         			}  
					  		%>     
                                    <tr  bgcolor="<%=strBgColor%>" onmouseover="className='TableItemOn'" onmouseout="className='TableItemOff'">
                                        <td class="copy" nowrap width='5%' align="center">
							 <html:checkbox name="list" property="personSelected" indexed="true" onclick="dataChanged()"/>
                                        </td>
                                        <td class="copy" width = "20%" align = "left"><a href="javaScript:addPerson('selectBudgetPersonnel.do?rowIndex=<%=ctr%>')">
							 <bean:write name= "list" property= "personName"/></a>
						    </td>
                                        <td class="copy" width='5%' align='left' >
							 <bean:write name= "list" property= "jobCode"/>
                                        </td>
                                        <td class="copy" width='10%' align='left'> 
							 <bean:write name= "list" property= "appointmentType"/>
                                        </td>         
                        			<% 
                         				index++;
                       				%>   
                                     </tr> 
                                  </logic:iterate>
                           </logic:present>
                        </table>
                                        <table width="100%"  border="0" cellpadding="2" cellspacing="0">                                                
                                            <tr>
                                                <td>
                                                    <img src="<bean:write name='ctxtPath'/>/coeusliteimages/spacer.gif"width="1" height="2">
                                                </td>
                                           </tr>
                                            <tr class='table' align='center' width='100%'>
                                              <td width='33%' colspan='1'align="left" nowrap>

                                        		<html:button property="Add Person" styleClass="clsavebutton" onclick="proposalBudget_Actions('P')">
                                              		<bean:message bundle="budget" key="budgetButton.save"/>
 		                                    </html:button>
                                            </td>
                                    </tr>    
                                              
                                        </table>
                        </td>
                </tr>
            </table>            
        </td>
      </tr>   

</table>
</html:form>
    <script>
          DATA_CHANGED = 'false';
          budgetPeriod = "<%=request.getAttribute("SelectedBudgetPeriodNumber")%>";
          LINK = "<%=request.getContextPath()%>/selectBudgetPersonnel.do?rowIndex=-1&budgetPeriod="+budgetPeriod;
          FORM_LINK = document.budgetPersonnelDynaBean;
          PAGE_NAME = "<bean:message bundle="budget" key="persons.label"/>";
          function dataChanged(){
                DATA_CHANGED = 'true';
          }
          linkForward(errValue);
    </script> 
</body>
</html:html>


