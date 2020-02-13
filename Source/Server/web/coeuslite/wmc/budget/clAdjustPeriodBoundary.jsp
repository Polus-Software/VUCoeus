<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib uri="/WEB-INF/struts-bean.tld"  prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld"  prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"  prefix="coeusUtils"%>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<jsp:useBean  id="adjustBudgetPeriodData" scope="session" class="java.util.ArrayList"/>


<%  
   String mode = (String)session.getAttribute("mode"+session.getId());
   String budgetStatusMode = (String)session.getAttribute("budgetStatusMode");
   boolean readOnly = false;
   if(mode!= null && mode.equals("display")){
       readOnly = true;
   }else if(budgetStatusMode != null && budgetStatusMode.equals("complete")){
        readOnly = true;
    }
%>
<html:html locale="true">
<head>

<script language="javascript" type="text/JavaScript" src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/coeusLiteCalendar.js">
</script>
<script language="JavaScript">
    var errValue = false;
    var errLock = false;
    function adjustPeriodAction(actionName){
            if(actionName == "A"){
                document.dynaBeanList.action = "<%=request.getContextPath()%>/addAdjustPeriod.do";
                document.dynaBeanList.submit();
                // Hide the code in first div tag
                document.getElementById('adjustPeriodDiv').style.display = 'none';
                // Display code in second div tag
                document.getElementById('commonDiv').style.display = 'block';
              
            }else if(actionName == "S"){
                // Hide the code in first div tag
                document.getElementById('adjustPeriodDiv').style.display = 'none';
                // Display code in second div tag
                document.getElementById('saveDiv').style.display = 'block';
            
            }
        }
        function removeLineItem(link){
            document.dynaBeanList.action = "<%=request.getContextPath()%>/"+link;
            document.dynaBeanList.submit();
        }
        
</script>

<html:base/> 
</head>
<title>Adjust Periods</title>
<body>

<html:form action="/saveAdjustPeriodData.do" method="post" >
<div id='adjustPeriodDiv'>
<table width="100%" height="100%"  border="0" cellpadding="0" cellspacing="0" class="table"> 

    <tr>
            <td class="tableheader" colspan='5'>
                <bean:message bundle="budget" key="adjustPeriod.Title"/>
            </td>
        </tr>
        <tr>
            <td colspan='5'>
                <div id="helpText" class='helptext'>            
                    <bean:message bundle="budget" key="helpTextBudget.Adjust"/>  
                </div>   
            </td>
        </tr>
    
    <tr>
        <td class='copy' align="left" valign="top" nowrap>
        <font color='red'>
        
            <logic:messagesPresent>
                    <script>errValue = true;</script>
                    <html:errors header=" " footer = " "/>                  
            </logic:messagesPresent>
            
            <logic:messagesPresent message="true">
                    <script>errValue = true;</script>
                    <html:messages id="message" message="true" property="budget_common_exceptionCode.1011" bundle="budget">                
                        <li><bean:write name = "message"/></li>
                    </html:messages>
                    <html:messages id="message" message="true" property="adjustPeriod_exceptionCode.1457" bundle="budget">                
                        <li><bean:write name = "message"/></li>
                    </html:messages>
                    <html:messages id="message" message="true" property="adjustPeriod_exceptionCode.1451" bundle="budget">                
                        <li><bean:write name = "message"/></li>
                    </html:messages>
                    <html:messages id="message" message="true" property="adjustPeriod_exceptionCode.1455" bundle="budget">                
                        <li><bean:write name = "message"/></li>
                    </html:messages>
                    <html:messages id="message" message="true" property="adjustPeriod_exceptionCode.1456" bundle="budget">                
                        <li><bean:write name = "message"/></li>
                    </html:messages>
                    <html:messages id="message" message="true" property="adjustPeriod_exceptionCode.1458" bundle="budget">                
                        <li><bean:write name = "message"/></li>
                    </html:messages>
                    <html:messages id="message" message="true" property="budget_common_exceptionCode.101" bundle="budget">                
                        <li><bean:write name = "message"/></li>
                    </html:messages>
                    <html:messages id="message" message="true" property="budget_common_exceptionCode.102" bundle="budget">                
                        <li><bean:write name = "message"/></li>
                    </html:messages>
                    <html:messages id="message" message="true" property="errMsg">
                        <script>errLock = true;</script>
                        <li><bean:write name = "message"/></li>
                    </html:messages>
                    <html:messages id="message" message="true" property="adjustPeriod_exceptionCode.1466" bundle="budget">                
                        <script>errLock = true;</script>
                        <li><bean:write name = "message"/></li>
                    </html:messages>
            </logic:messagesPresent>
            </font>
        </td>
        
    </tr>
    
    <tr class='copybold'>
        <td height='10px'>
        </td>
    </tr> 
    
    <tr>
    <td style='core'>
    <table width="98%" height="100%"  align="center" border="0" cellpadding="2" cellspacing="0" class="tabtable">
    
        <tr align="center" nowrap>
            <td width="5%" align="center" class="theader" nowrap><bean:message bundle="budget" key="adjustPeriod.PeriodLabel"/></td>
            <td width="25%" class="theader" nowrap><font color="red">* </font><bean:message bundle="budget" key="adjustPeriod.StartDateLabel"/> </td>
            <td width="25%" class="theader" nowrap><font color="red">* </font><bean:message bundle="budget" key="adjustPeriod.EndDateLabel"/> </td>
            <td width="20%" class="theader" nowrap>
                <%--<bean:message bundle="budget" key="adjustPeriod.Remove"/>--%>
            </td>
            <td width="25%" class="theader" nowrap></td>
        </tr>
        <tr class='copybold'>
        <td height='10px'>
        </td>
        </tr>  

                       
                      <%    String calImage = request.getContextPath()+"/coeusliteimages/cal.gif";
                            String imageSrc = request.getContextPath()+"/coeusliteimages/delete.gif"; 
                            String strBgColor = "#DCE5F1";
                            int count = 0;
                      %>
                      <logic:iterate id="dynaFormData" name="dynaBeanList" property="list" type="org.apache.struts.action.DynaActionForm" indexId="index">  
                      <%
                            if (count%2 == 0) {
                                strBgColor = "#D6DCE5";
                            }    
                            else {
                                strBgColor = "#DCE5F1";
                            }
                     %>

                    <tr align="center" valign="top">
                        <% Integer budgetPeriod = (Integer)dynaFormData.get("budgetPeriod");
                            String link = "javaScript:removeLineItem('deleteAdjustPeriod.do?budgetPeriod="+budgetPeriod+"')";

                        %>
                        <td class='copybold'><%=budgetPeriod%></td>
                        <td nowrap>
                            <html:text property="periodStartDate"  name="dynaFormData" indexed="true"  styleClass="cltextbox" disabled="<%=readOnly%>" maxlength="10" onchange="dataChanged()"/> 
                            <% String strStartDateField = "dynaFormData["+index+"].periodStartDate"; 
                                String strStartDateScriptSrc ="javascript:displayCalendarWithTopLeft('"+strStartDateField+"',8,25)";
                                if(readOnly){
                            %>
                            <%-- User has only View Rights --%>
                                <html:img src="<%=calImage%>" border="0" height="16" width="16" />
                            <%}else{ %>
                                <html:link href="<%=strStartDateScriptSrc%>" onclick="dataChanged()">
                                        <html:img src="<%=calImage%>" border="0" height="16" width="16" />
                                </html:link>

                            <%}%>
                        </td>
                        <td nowrap>
                            <html:text property="periodEndDate"  name="dynaFormData" disabled="<%=readOnly%>" indexed="true" styleClass="cltextbox" maxlength="10" onchange="dataChanged()"/>

                            <% String strEndDateField = "dynaFormData["+index+"].periodEndDate"; 
                                String strEndDateScriptSrc ="javascript:displayCalendarWithTopLeft('"+strEndDateField+"',8,25)";
                                if(readOnly){
                            %>
                            <%-- User has only View Rights --%>
                                <html:img src="<%=calImage%>" border="0" height="16" width="16" />
                            <%}else{ %>
                                <html:link href="<%=strEndDateScriptSrc%>" onclick="dataChanged()">
                                    <html:img src="<%=calImage%>" border="0" height="16" width="16"/>
                                </html:link>

                            <%}%>

                        </td>
                        <td align="center">
                            <%if(readOnly){%>
                                <bean:message bundle="budget" key="adjustPeriod.Remove"/></u>
                            <%}else{%>
                                <html:link  href="<%=link%>" onclick="dataChanged()">
                                    <u><bean:message bundle="budget" key="adjustPeriod.Remove"/></u>
                                    <%--<img border="0" src="../../../../coeusliteimages/trash.gif" alt="Remove Period <%=budgetPeriod%>">--%>
                                </html:link> </td>
                            <%}%>
                        </tr>
                     
                    <tr class='copybold'>
                    <td height='10px'>
                    </td>
                    </tr>  
                    <% count++;%>
                </logic:iterate>

        <tr class='copybold'>
        <td height='10'>
        </td>
        </tr>              
        <tr>
            <td class="copybold" colspan='5'>&nbsp;
                <% if(!readOnly){ %>
                <html:link href="javascript:adjustPeriodAction('A')" >
                    <u><bean:message bundle="budget" key="adjustPeriod.AddPeriodLabel"/></u>
                </html:link>
                <%}else{%>
                    <bean:message bundle="budget" key="adjustPeriod.AddPeriodLabel"/>
                <%}%>
            </td>
         </tr>  
        <tr class='copybold'>
        <td height='10'>
        </td>
        </tr>             
        </table> 
    </td>
    </tr>
    
    <tr class='copybold'>
        <td height='10'>
        </td>
    </tr>   
    
    <tr>
        <td class='savebutton'>&nbsp;&nbsp;
            <html:submit accesskey="s" property="Save" styleClass="clsavebutton" disabled="<%=readOnly%>" onclick="adjustPeriodAction('S')">
            <bean:message bundle="budget" key="adjustPeriod.Save"/>
            </html:submit>
        </td>
    </tr>
    
    <tr class='copybold'>
        <td height='10'>
        </td>
    </tr> 
    
</table>
</div>
<div id='commonDiv' style='display: none;'>
    <table width="100%" height="100%" align="center" border="0" cellpadding="3" cellspacing="0" class="tabtable">
        <tr>
            <td align='center' class='copyred'>  <br> <bean:message bundle="budget" key="budgetMessages.pleaseWait"/>  </td>
        </tr>
    </table>
</div>
<div id='saveDiv' style='display: none;'>
    <table width="100%" height="100%" align="center" border="0" cellpadding="3" cellspacing="0" class="tabtable">
        <tr>
            <td align='center' class='copyred'><bean:message bundle="budget" key="budgetMessages.savingBudget"/> 
                <br> &nbsp;&nbsp;&nbsp;  <bean:message bundle="budget" key="budgetMessages.pleaseWait"/>  
            </td>
        </tr>
    </table>
</div>
</html:form>
    <script>
          DATA_CHANGED = 'false';
          var dataModified = '<%=request.getAttribute("dataModified")%>';
          if(dataModified == 'modified' || (errValue && !errLock)){
                DATA_CHANGED = 'true';
          }
          LINK = "<%=request.getContextPath()%>/saveAdjustPeriodData.do";
          FORM_LINK = document.dynaBeanList;
          PAGE_NAME = "<bean:message bundle="budget" key="adjustPeriod.Title"/>";
          function dataChanged(){
                DATA_CHANGED = 'true';
          }
          linkForward(errValue);
    </script>
    <script>
      var help = '<bean:message bundle="budget" key="helpTextBudget.Adjust"/>';
      help = trim(help);
      if(help.length == 0){
            document.getElementById('helpText').style.display = 'none';
      }
      function trim(s){
            return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
      }  
    </script>    
</body>
</html:html>
