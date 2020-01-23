<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"  prefix="coeusUtils"%>
<%@ page import="java.text.DecimalFormat,edu.mit.coeuslite.utils.CoeusDynaBeansList"%>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<jsp:useBean  id="budgetInfoBean"  scope="session" class="edu.mit.coeus.budget.bean.BudgetInfoBean"/>
<jsp:useBean  id="CSDBudgetPeriodData"  scope="session" class="java.util.Vector"/>
<html:html>
    <%String mode = (String)session.getAttribute("mode"+session.getId());
    String budgetStatusMode = (String)session.getAttribute("budgetStatusMode");
    boolean readOnly = false;
    if(mode!= null && mode.equals("display")){
    readOnly = true;
    }else if(budgetStatusMode != null && budgetStatusMode.equals("complete")){
    readOnly = true;
    }
    boolean NoCostDistribution = ((Boolean)session.getAttribute("NoCostDistribution")).booleanValue();
    %>
    <head>
        <title>Cost Distribution Sharing</title>
        <script>
            var errValue = false;
            var errLock = false;
            function addDistribution(){
                document.costDistributionDynaList.action = "<%=request.getContextPath()%>/addCostSharingDistribution.do";
                document.costDistributionDynaList.submit();
            }
            
            function viewSubAwardDistribution(){
                var url = "<%=request.getContextPath()%>" + "/getSubAwardCostSharing.do";
                var winleft = (screen.width - 800) / 2;
                var winUp = (screen.height - 450) / 2; 
                var win = "scrollbars=1,resizable=1,width=500,height=200,left="+winleft+",top="+winUp;    
                sList = window.open(url, "list", win);      

                if (parseInt(navigator.appVersion) >= 4) {
                    window.sList.focus(); 
                }
            }
            
            function saveDistribution(){
                document.costDistributionDynaList.action = "<%=request.getContextPath()%>/saveCostSharingDistribution.do";
                document.costDistributionDynaList.submit();
            }
            
            function deleteDistribution(removeCount){
                if(confirm('<bean:message bundle="budget" key="costSharingDistribution.deleteConfirmation"/>')== true){
                document.costDistributionDynaList.action = "<%=request.getContextPath()%>/deleteCostSharingDistribution.do?removeCSDRowCount="+removeCount;
                document.costDistributionDynaList.submit();
                }
            }
            
            function floatOnly(formFieldName){
           if(formFieldName.value.length>0) {
            //var formvalue = trim(formFieldName.value);
            formFieldName.value = formFieldName.value.replace(/[a-z,~,@,#,^,$,@,#,(,),!,`,%, ,&,*]+/gi, ''); 
            //formFieldName.value = formvalue.replace(/[^0-9 .]+/g, ''); 
            
            dataChanged();
            }
            }
            
                    function intOnly(formFieldName) {
                    if(formFieldName.value.length>0) {
                        //formFieldName.value = formFieldName.value.replace(/[a-z,~,@,#,%, ,&,*]+/gi, ''); 
                        //var formvalue = trim(formFieldName.value);
                        formFieldName.value = formFieldName.value.replace(/[^0-9 , $ .]+/g,'');
                        dataChanged();
                        }
                    }
                    function numOnly(formFieldName) {
                    if(formFieldName.value.length>0) {
                        var strReplace = formFieldName.value;
                        //^[0-9]{0,3}(.[0-9]{0,2})?$
                        //formFieldName.value = formFieldName.value.replace(/[a-z,~,@,#,%,&,*, ,$,.]+/gi, ''); 
                         //var formvalue = trim(formFieldName.value);
                        formFieldName.value = formFieldName.value.replace(/[^0-9]+/g,''); 
                        dataChanged();
                    }
              }
              function trim(s) {
            return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
      }  
        </script>
    </head>   
    <html:form action="/getCostSharingDistribution.do">
        <body>
            <%if(!NoCostDistribution){%>
            <table width="100%" class="tabtable" border="0" cellpadding="0" cellspacing="0">
                <tr>
                    <td colspan="2">
                        <table align="center" border="0"  width="100%" cellpadding="0" cellspacing="0">
                            <logic:present name ="budgetInfoBean" scope = "session" >  
                                <tr>
                                    <td class="theader" colspan="6" height="20">
                                        Budget Totals
                                    </td>
                                </tr>
                                <tr>
                                    <td  nowrap class="copybold" >&nbsp;<bean:message bundle="budget" key="budgetLabel.directCost" /> :
                                    </td>
                                    <td  nowrap class="copy">
                                        <coeusUtils:formatString name="budgetInfoBean"  property="totalDirectCost" formatType="currencyFormat"/>
                                    </td>
                                    <td   nowrap class="copybold"> <bean:message bundle="budget" key="budgetLabel.inDirectCost" /> :
                                    </td>
                                    <td  nowrap class="copy"> 
                                        <coeusUtils:formatString  name="budgetInfoBean" property="totalIndirectCost" formatType="currencyFormat"/>
                                    </td>
                                    <td  nowrap class="copybold"> <bean:message bundle="budget" key="budgetLabel.totalCost" /> :
                                    </td>
                                    <td  nowrap class="copy">
                                        <coeusUtils:formatString name="budgetInfoBean" property="totalCost" formatType="currencyFormat"/>
                                    </td>
                                </tr>
                                
                                <tr>
                                    <td  nowrap class="copybold">&nbsp;<bean:message bundle="budget" key="budgetLabel.underRecovery" />  :
                                    </td>
                                    <td  nowrap class="copy">
                                        <coeusUtils:formatString name="budgetInfoBean" property="underRecoveryAmount" formatType="currencyFormat"/>
                                    </td>
                                    <td  nowrap class="copybold"> <bean:message bundle="budget" key="budgetLabel.costShare" /> :
                                    </td>
                                    <td  nowrap class="copy">
                                        <coeusUtils:formatString name="budgetInfoBean" property="costSharingAmount" formatType="currencyFormat"/>
                                    </td>
                                    <td  nowrap class="copybold"> <bean:message bundle="budget" key="budgetSummary.period" /> :
                                    </td>
                                    <td  nowrap class="copy">
                                        <coeusUtils:formatDate name="budgetInfoBean" property="startDate" />
                                        -
                                        <coeusUtils:formatDate name="budgetInfoBean" property="endDate" />   
                                    </td>
                                </tr>                   
                            </logic:present>
                        </table>
                    </td>
                </tr>
                <tr class="table"><td colspan="2">&nbsp;</td></tr>
                
                <tr>
                    <td height="10" class='copy' colspan='2'>
                        <%--logic:messagesPresent>
                            <font color='red'>
                                <script>errValue = true;</script>
                                <html:errors header="" footer=""/>
                            </font>
                        </logic:messagesPresent--%>
                        <logic:messagesPresent message = "true"> 
                            <script>errValue = true;</script>
                            <font color="red">
                                <html:messages id="message" message="true" bundle="budget" property="costSharingDistribution.InvalidCostSharingAmount">                
                                    <li><bean:write name = "message"/></li>
                                </html:messages>
                                <html:messages id="message" message="true" bundle="budget" property="costSharingDistribution.error.InvalidPercentage">                
                                    <li><bean:write name = "message"/></li>
                                </html:messages>
                                <html:messages id="message" message="true" bundle="budget" property="costSharingDistribution.error.RepeatedFiscalYearSourceAccount">                
                                    <li><bean:write name = "message"/></li>
                                </html:messages>
                                <html:messages id="message" message="true" bundle="budget" property="costSharingDistribution.error.InvalidFiscalYear">                
                                    <li><bean:write name = "message"/></li>
                                </html:messages>
                                <html:messages id="message" message="true" bundle="budget" property="costSharingDistribution.error.FiscalYearEmpty">                
                                    <li><bean:write name = "message"/></li>
                                </html:messages>
                                <html:messages id="message" message="true" bundle="budget" property="costSharingDistribution.error.SourceAccountEmpty">                
                                    <li><bean:write name = "message"/></li>
                                </html:messages>
                                <html:messages id="message" message="true" bundle="budget" property="costSharingDistribution.error.TotalsNotEqual">                
                                    <li><bean:write name = "message"/></li>
                                </html:messages>
                                <html:messages id="message" message="true" bundle="budget" property="costSharingDistribution.CostPercent.mask">                
                                    <li><bean:write name = "message"/></li>
                                </html:messages>
                                <html:messages id="message" message="true" bundle="budget" property="costSharingDistribution.CostPercent.range">                
                                    <li><bean:write name = "message"/></li>
                                </html:messages>
                                <html:messages id="message" message="true" property="errMsg">
                                    <script>errLock = true;</script>
                                    <li><bean:write name = "message"/></li>
                                </html:messages>                            
                            </font>
                        </logic:messagesPresent> 
                    </td>
                </tr>
                
                
                <tr>
                    <td colspan="2">
                        <table align="center" width="100%" border="0" cellpadding="0" cellspacing="0">
                            <tr class="theader">
                                <td nowrap><bean:message bundle="budget" key="costSharingDistribution.period"/></td>
                                <td nowrap><bean:message bundle="budget" key="costSharingDistribution.TotalAmt"/></td>
                            </tr>
                            <% String strBgColor = "#DCE5F1";
                            int rowCount=0;
                            %>                                            
                            <logic:iterate id="periodData" name="CSDBudgetPeriodData" type="org.apache.struts.validator.DynaValidatorForm">
                                <%if (rowCount%2 == 0) {
                                strBgColor = "#D6DCE5"; 
                                }
                                else { 
                                strBgColor="#DCE5F1"; 
                                } %>
                                <tr bgcolor="<%=strBgColor%>" onmouseover="className='TableItemOn'" onmouseout="className='TableItemOff'">
                                    <td nowrap class="copy"><bean:write  property="budgetPeriod" name="periodData"  /></td>
                                    <td nowrap class="copy"><coeusUtils:formatString name="periodData" property="costSharingAmount" formatType="currencyFormat"/></td>
                                </tr>
                                <%rowCount++;%>
                            </logic:iterate>
                        </table>
                    </td>
                </tr>
                <tr class="table"><td colspan="2">&nbsp;</td></tr>
                <tr>
                    <td colspan="2">
                        <table align="center" width="100%" class="tabtable" border="0" cellpadding="0" cellspacing="0">
                            <tr class="theader">
                                <td colspan="5" height="20">
                                    <bean:message bundle="budget" key="costSharingDistribution.DistributionList"/> :
                                </td>
                            </tr>
                            <tr>
                                <td colspan="5">
                                    <%if(!readOnly){%>
                                    <div id="showAddSection" >
                                        <html:link href="javascript:addDistribution();"><u><bean:message bundle="budget" key="costSharingDistribution.AddList"/></u></html:link>
                                        <!-- COEUSQA-2735 Cost sharing distribution for Sub awards - Start -->
                                        &nbsp;&nbsp;<html:link href="javascript:viewSubAwardDistribution();"><u><bean:message bundle="budget" key="costSharingDistribution.viewCostSharing"/></u></html:link>
                                        <!-- COEUSQA-2735 Cost sharing distribution for Sub awards - End -->
                                    </div>
                                    <%}else{%>
                                    <bean:message bundle="budget" key="costSharingDistribution.AddList"/>
                                    <!-- COEUSQA-2735 Cost sharing distribution for Sub awards - Start -->
                                    &nbsp;&nbsp;<html:link href="javascript:viewSubAwardDistribution();"><u><bean:message bundle="budget" key="costSharingDistribution.viewCostSharing"/></u></html:link>
                                    <!-- COEUSQA-2735 Cost sharing distribution for Sub awards - End -->
                                    <%}%>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="5">
                                    &nbsp;
                                </td>
                            </tr>
                            <tr class="theader">
                                <td><bean:message bundle="budget" key="costSharingDistribution.Fiscalyear"/></td>
                                <td><bean:message bundle="budget" key="costSharingDistribution.Percent"/></td>
                                <td><bean:message bundle="budget" key="costSharingDistribution.Amount"/></td>
                                <td><bean:message bundle="budget" key="costSharingDistribution.SourceAccount"/></td>
                                <td></td>
                            </tr>
                            <%int count = 0;%>
                            <!-- Added for Case#2402- use a parameter to set the length of the account number throughout app - Start -->
                            <%String accountNumberMaxLength = session.getAttribute("AccountNumberMaxLength").toString();%>
                            <!-- Case#2402 - End -->
                            <logic:iterate id="dynaFormData" name="costDistributionDynaList" property="list" type="org.apache.struts.action.DynaActionForm" indexId="row"  scope="session">
                                
                                <tr> 
                                    <td><html:text  property="costSharingFiscalYear" name="dynaFormData" indexed="true" maxlength="4" 
                                                    onchange="dataChanged()" readonly="<%=readOnly%>" /></td>
                                    <td><html:text  property="strCostSharingPercentage" name="dynaFormData"  maxlength="6" indexed="true"
                                                    onchange="dataChanged()" readonly="<%=readOnly%>" /></td>
                                    <td><html:text  property="strCostSharingAmount" name="dynaFormData" indexed="true" onchange="dataChanged()" maxlength="12" readonly="<%=readOnly%>"/></td>
                                    <!-- Modified for Case#2402- use a parameter to set the length of the account number throughout app - Start 
                                    <td><html:text  property="sourceAccount" name="dynaFormData" indexed="true" maxlength="7"  onchange="dataChanged()"readonly="<%=readOnly%>"/></td>-->
                                    <td><html:text  property="sourceAccount" name="dynaFormData" indexed="true" maxlength="<%=accountNumberMaxLength%>"  onchange="dataChanged()"readonly="<%=readOnly%>"/></td>
                                    <!-- Case#2402 - End -->
                                    <%String removeLink = "javascript:deleteDistribution('"+count+"');";%>
                                    <td><%if(!readOnly){%>
                                        <html:link href="<%=removeLink%>">
                                            <u><bean:message bundle="budget" key="costSharingDistribution.Remove"/></u>
                                        </html:link><%}else{%> <bean:message bundle="budget" key="costSharingDistribution.Remove"/><%}%>
                                    </td>
                                </tr>
                                <%count++;%>
                            </logic:iterate>
                        </table>
                    </td>
                </tr>
                
                <tr>
                    <td>
                        <%if(!readOnly){%>
                        <html:button property="Save" styleClass="clsavebutton" onclick="javasript:saveDistribution();">
                            <bean:message bundle="budget" key="costSharingDistribution.Save"/>
                        </html:button>
                        <%}%>
                    </td>
                </tr>
                <tr class="table"><td colspan="2">&nbsp;</td></tr>
            </table>
            <%}else{%>
            <table width="100%"  class="tabtable" border="0" cellpadding="0" cellspacing="0">
                <tr>
                    <td class="copy" height="100%" width="100%">
                        <font color="red">
                            <bean:message bundle="budget" key="costSharingDistribution.NoDistribution"/>
                        </font>
                    </td>
                </tr>
            </table>
            <%}%>
            <script>
          DATA_CHANGED = 'false';
          var dataModified = '<%=request.getAttribute("dataModified")%>';
          if(dataModified == 'modified' || (errValue && !errLock)){
                DATA_CHANGED = 'true';
          }
          LINK = "<%=request.getContextPath()%>/saveCostSharingDistribution.do";
          FORM_LINK = document.costDistributionDynaList;
          PAGE_NAME = "Cost Sharing Distribution";
          function dataChanged(){
                DATA_CHANGED = 'true';
          }
          linkForward(errValue);
            </script>
            
        </body>
    </html:form>
</html:html>
