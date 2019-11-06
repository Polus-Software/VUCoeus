<!--
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
-->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"  prefix="coeusUtils"%>
<%@ page import="java.text.DecimalFormat,edu.mit.coeuslite.utils.CoeusDynaBeansList"%>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<jsp:useBean  id="budgetInfoBean"  scope="session" class="edu.mit.coeus.budget.bean.BudgetInfoBean"/>
<jsp:useBean  id="UROnOffCampus"  scope="session" class="java.util.Vector"/>
<jsp:useBean  id="URBudgetPeriodData"  scope="session" class="java.util.Vector"/>
<html:html>
    <%String mode = (String)session.getAttribute("mode"+session.getId());
    String budgetStatusMode = (String)session.getAttribute("budgetStatusMode");
    boolean readOnly = false;
    if(mode!= null && mode.equals("display")){
    readOnly = true;
    }else if(budgetStatusMode != null && budgetStatusMode.equals("complete")){
    readOnly = true;
    }
    boolean NoURDistribution = ((Boolean)session.getAttribute("noUnderRecoveryDistribution")).booleanValue();
    %>
    <head>
        <title><bean:message bundle="budget" key="underRecoveryDistribution.header"/></title>
        <script>
            var errValue = false;
            var errLock = false;
            function addDistribution(){
                document.underRecoveryDynaList.action = "<%=request.getContextPath()%>/addUnderRecoveryDistribution.do";
                document.underRecoveryDynaList.submit();
            }
            
            function saveDistribution(){
                document.underRecoveryDynaList.action = "<%=request.getContextPath()%>/saveUnderRecoveryDistribution.do";
                document.underRecoveryDynaList.submit();
            }
            
            function deleteDistribution(removeCount){
                if(confirm("<bean:message bundle="budget" key="underRecoveryDistribution.deleteConfirmation"/>")== true){
                document.underRecoveryDynaList.action = "<%=request.getContextPath()%>/deleteUnderRecoveryDistribution.do?removeUrRowCount="+removeCount;
                document.underRecoveryDynaList.submit();
                }
            }
            </script>
    </head>   
    <html:form action="/getUnderRecoveryDistribution.do">
        <body>
            <%if(NoURDistribution){%>
            
            <table width="100%"  class="tabtable" border="0" cellpadding="0" cellspacing="0">
                <tr>
                    <td class="copy" height="100%">
                        <font color="red">
                            <bean:message bundle="budget" key="underRecoveryDistribution.NoDistribution"/>
                        </font>
                    </td>
                </tr>
            </table>
           
            <%}else{%>
            
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
                        <logic:messagesPresent>
                            <font color='red'>
                                <script>errValue = true;</script>
                                <html:errors header="" footer=""/>
                            </font>
                        </logic:messagesPresent>
                        <logic:messagesPresent message = "true"> 
                            <script>errValue = true;</script>
                            <font color="red">
                                <html:messages id="message" message="true" bundle="budget" property="underRecoveryDistribution.InvalidUnderRecoveryAmount">                
                                    <li><bean:write name = "message"/></li>
                                </html:messages>
                                <html:messages id="message" message="true" bundle="budget" property="underRecoveryDistribution.ApplicableRates.range">                
                                    <li><bean:write name = "message"/></li>
                                </html:messages>
                                <html:messages id="message" message="true" bundle="budget" property="underRecoveryDistribution.ApplicableRates.mask">                
                                    <li><bean:write name = "message"/></li>
                                </html:messages>
                                <html:messages id="message" message="true" bundle="budget" property="underRecoveryDistribution.error.RepeatedRow">                
                                    <li><bean:write name = "message"/></li>
                                </html:messages>
                                <html:messages id="message" message="true" bundle="budget" property="underRecoveryDistribution.error.InvalidFiscalYear">                
                                    <li><bean:write name = "message"/></li>
                                </html:messages>
                                <html:messages id="message" message="true" bundle="budget" property="underRecoveryDistribution.error.FiscalYearEmpty">                
                                    <li><bean:write name = "message"/></li>
                                </html:messages>
                                <html:messages id="message" message="true" bundle="budget" property="underRecoveryDistribution.error.SourceAccountEmpty">                
                                    <li><bean:write name = "message"/></li>
                                </html:messages>
                                <html:messages id="message" message="true" bundle="budget" property="underRecoveryDistribution.error.TotalsNotEqual">                
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
                                <td nowrap><bean:message bundle="budget" key="underRecoveryDistribution.period"/></td>
                                <td nowrap><bean:message bundle="budget" key="underRecoveryDistribution.UnderRecoveryAmount"/></td>
                            </tr>
                            <% String strBgColor = "#DCE5F1";
                            int rowCount=0;
                            %>                                            
                            <logic:iterate id="periodData" name="URBudgetPeriodData" type="org.apache.struts.validator.DynaValidatorForm">
                                <%if (rowCount%2 == 0) {
                                strBgColor = "#D6DCE5"; 
                                }
                                else { 
                                strBgColor="#DCE5F1"; 
                                } %>
                                <tr bgcolor="<%=strBgColor%>" onmouseover="className='TableItemOn'" onmouseout="className='TableItemOff'">
                                    <td nowrap class="copy"><bean:write  property="budgetPeriod" name="periodData"  /></td>
                                    <td nowrap class="copy"><coeusUtils:formatString name="periodData" property="underRecoveryAmount" formatType="currencyFormat"/></td>
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
                                <td colspan="6" height="20">
                                    <bean:message bundle="budget" key="underRecoveryDistribution.DistributionList"/> :
                                </td>
                            </tr>
                            <tr>
                                <td colspan="6">
                                    <%if(!readOnly){%>
                                        <html:link href="javascript:addDistribution();">
                                            <bean:message bundle="budget" key="underRecoveryDistribution.AddList"/>
                                        </html:link>
                                    <%}else{%>
                                    <bean:message bundle="budget" key="underRecoveryDistribution.AddList"/>
                                    <%}%>
                                </td>
                            </tr>
                            <tr class="theader">
                                <td><bean:message bundle="budget" key="underRecoveryDistribution.applicableRates"/></td>
                                <td><bean:message bundle="budget" key="underRecoveryDistribution.Fiscalyear"/></td>
                                <td><bean:message bundle="budget" key="underRecoveryDistribution.OnOffCampus"/></td>
                                <td><bean:message bundle="budget" key="underRecoveryDistribution.Amount"/></td>
                                <td><bean:message bundle="budget" key="underRecoveryDistribution.SourceAccount"/></td>
                                <td></td>
                            </tr>
                            <%int count = 0;%>
                            <!-- Added for Case#2402- use a parameter to set the length of the account number throughout app - Start -->
                            <%String accountNumberMaxLength = session.getAttribute("AccountNumberMaxLength").toString();%>
                            <!--Case#2402 - End-->
                            <logic:iterate id="dynaFormData" name="underRecoveryDynaList" property="list" type="org.apache.struts.action.DynaActionForm" indexId="row"  scope="session">
                                <tr>
                                    <td><html:text  property="strApplicableRate_wmc" name="dynaFormData" indexed="true" readonly="<%=readOnly%>" onchange="dataChanged();" maxlength="6" /></td>
                                    <td><html:text  property="fiscalYear_wmc" name="dynaFormData" maxlength="4" readonly="<%=readOnly%>" onchange="dataChanged();" indexed="true"/></td>
                                    <td align="center"><html:select name="dynaFormData" property="onOffCampusFlag_wmc" indexed="true" disabled="<%=readOnly%>" onchange="dataChanged()" styleClass="clcombobox"> 
                                                        <html:options collection="UROnOffCampus" property="code" labelProperty="description"/>
                                                        </html:select>
                                    </td>
                                    <td><html:text  property="strUnderRecoveryAmt_wmc" name="dynaFormData" indexed="true" readonly="<%=readOnly%>" onchange="dataChanged();" maxlength="12" /></td>
                                    <!-- Modified for Case#2402- use a parameter to set the length of the account number throughout app - Start 
                                    <td><html:text  property="sourceAccount_wmc" name="dynaFormData" indexed="true" readonly="<%=readOnly%>" onchange="dataChanged();" maxlength="7" /></td>-->
                                    <td><html:text  property="sourceAccount_wmc" name="dynaFormData" indexed="true" readonly="<%=readOnly%>" onchange="dataChanged();" maxlength="<%=accountNumberMaxLength%>" /></td>
                                    <!-- Case#2402 - End -->
                                    <%String removeLink = "javascript:deleteDistribution('"+count+"');";%>
                                    <td><%if(!readOnly){%>
                                        <html:link href="<%=removeLink%>">
                                            <u><bean:message bundle="budget" key="underRecoveryDistribution.Remove"/></u>
                                        </html:link><%}else{%><bean:message bundle="budget" key="underRecoveryDistribution.Remove"/><%}%>
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
                        <html:button property="Save" styleClass="clsavebutton" onclick="javascript:saveDistribution();">
                            <bean:message bundle="budget" key="underRecoveryDistribution.Save"/>
                        </html:button>
                        <%}%>
                    </td>
                </tr>
                <tr class="table"><td colspan="2">&nbsp;</td></tr>
            </table>
            
            <%}%>
            <script>
          DATA_CHANGED = 'false';
          var dataModified = '<%=request.getAttribute("dataModified")%>';
          if(dataModified == 'modified' || (errValue && !errLock)){
                DATA_CHANGED = 'true';
          }
          LINK = "<%=request.getContextPath()%>/saveUnderRecoveryDistribution.do";
          FORM_LINK = document.underRecoveryDynaList;
          PAGE_NAME = "<bean:message bundle="budget" key="underRecoveryDistribution.header"/>";
          function dataChanged(){
                DATA_CHANGED = 'true';
          }
          linkForward(errValue);
            </script>
            
        </body>
    </html:form>
</html:html>
