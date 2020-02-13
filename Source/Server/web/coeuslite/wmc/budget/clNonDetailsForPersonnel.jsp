<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"  prefix="coeusUtils"%>
<%@ page import="org.apache.struts.validator.DynaValidatorForm,java.util.Vector"%>
<jsp:useBean  id="nonDetailsPersonnelForm" scope="session" class="org.apache.struts.validator.DynaValidatorForm"/>
<jsp:useBean  id="noDetailsForPersonnel"  scope="session" class="java.util.Vector"/>
<jsp:useBean  id="detailsForPersonnel"  scope="session" class="java.util.Vector"/>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<html:html>
  <head>  
    <title>Line Item</title>
    <html:base/>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
    <link rel=stylesheet href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/ipf.css" type="text/css">
    <link href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css" rel="stylesheet" type="text/css">
            <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">            
   <script>
    function close_action(){
            window.close();
         }
   </script>
   </head>
   <body>
    <html:form action="/nonDetailsForPersonnel"  method="post"> 
     <table width="100%"   border="0" cellpadding="0" cellspacing="0" class="table">       
     <tr>
            <td colspan='6' class='table'><b><bean:message bundle="budget" key="budgetLineItemDetails.lineItemDetailsHeader"/></b>
            </td>
         </tr>
      <logic:iterate id="personnelDetails" name="detailsForPersonnel"
                                                 type="org.apache.struts.validator.DynaValidatorForm">
                                         
      <tr>
        <td colspan='6' class="tabtable">
            <table width="100%"  border="0" cellpadding="0" cellspacing="0" >
                 <tr>
                    <td width="12%" align="left" class="copybold"><bean:message bundle="budget" key="budgetLineItemDetailsLabel.description"/></td>   
                    <td width="88%" align="left" nowrap> 
                        <html:text property="costElementDescription"  name="personnelDetails" readonly="true" styleClass="cltextbox-nonEditcolor" style="width: 635px;"  maxlength="80"/>
                    </td>
                </tr>
             </table>
        </td>
        </tr>
        <tr>
        <td colspan='6' class="tabtable">
            <table width="100%"  border="0" cellpadding="0"  cellspacing="0">
                   <tr>
                        <td align="left" class="copybold" width="12%" nowrap><bean:message bundle="budget" key="budgetLineItemDetailsLabel.startDate"/></td>   
                        <td align="left" class="copy" width="22%"> 
                           <html:text property="personStartDate" readonly="true" name="personnelDetails" readonly="true" maxlength="10"  size="10" styleClass="cltextbox-nonEditcolor" style="width: 120px;" />
                        </td>
                        <td align="left" class="copybold" width="13%"><bean:message bundle="budget" key="budgetLineItemDetailsLabel.endDate"/></td>
                        <td align="left" class="copy" nowrap width="22%">
                        <html:text property="personEndDate" readonly="true" maxlength="10" name="personnelDetails" readonly="true" size="10" styleClass="cltextbox-nonEditcolor" style="width: 120px;" />
                        </td>
                        <td width="13%" align="left" class="copybold"><bean:message bundle="budget" key="budgetLineItemDetailsLabel.quantity"/></td>   
                        <td width="18%" align="left" class="copy">
                            <html:text property="quantity" readonly="true" maxlength="4" name="personnelDetails" readonly="true" size="15" styleClass="cltextbox-nonEditcolor" style="width: 120px;"/>
                        </td>
                    </tr>
                </table>
          </td>
          </tr>
          <tr>
          <td colspan='6' class="tabtable">
            <table width="100%"  border="0" cellpadding="0" cellspacing="0" >
                <tr>
                    <td width="12%" align="left" class="copybold"><bean:message bundle="budget" key="budgetLineItemDetailsLabel.cost"/>&nbsp;</td>   
                    <td width="22%" align="left" class="copy">
                        <html:text property="lineItemCost" size="15" readonly="true" name="personnelDetails" readonly="true" styleClass="cltextbox-nonEditcolor" style="text-align: right; width: 120px;"/>

                    </td>
                    <td width="13%" align="left" class="copybold" nowrap><bean:message bundle="budget" key="budgetLineItemDetailsLabel.costSharing"/></td>   
                    <td width="22%" align="left" class="copy">
                        <html:text property="costSharingAmount" readonly="true" size="15" name="personnelDetails" readonly="true" styleClass="cltextbox-nonEditcolor" style="text-align: right; width: 120px;"  />
                    </td>
                    <td width="13%" align="left" class="copybold"><bean:message bundle="budget" key="budgetLineItemDetailsLabel.underrecovery"/></td>   
                    <td width="18%" align="left" class="copy">
                        <html:text property="underRecoveryAmount" readonly="true" size="15" name="personnelDetails" readonly="true"  styleClass="cltextbox-nonEditcolor" style="text-align: right; width: 120px;" />
                    </td>
                </tr>
            </table>
          </td>
          </tr>
          <tr>
          <td colspan='6' class="tabtable">
                <table width="100%"  border="0" cellpadding="0" cellspacing="0" >
                    <tr>
                        <td align="left" class="copybold" width='12%' nowrap><bean:message bundle="budget" key="budgetLineItemDetailsLabel.applyInflation"/></td>   
                        <td align="left" width='22%' class="copy">                                                                                    
                       
                            <html:checkbox  property="applyInRateFlag" name="personnelDetails"  disabled="true" />
                        </td>
                        <td width="13%" align="left" class="copybold" nowrap>On/Off Campus:</td>   
                        <td width="22%" align="left" class="copy">
                            <html:checkbox  property="onOffCampusFlag" name="personnelDetails" disabled="true" />
                        </td>
                        <td width="13%" align="left" class="copybold"></td>   
                        <td width="18%" align="left" class="copy">
                        </td>                                
                    </tr>
                </table>
              </td>
      </tr>
      </logic:iterate>
      <tr>
        <td width="15%" align="left" class="theader"><bean:message bundle="budget" key="budgetLineItemDetailstableHeader.rateTypes"/></td>
        <td width="5%" align="center" class="theader"><bean:message bundle="budget" key="budgetLineItemDetailstableHeader.apply"/></td>
        <td width="10%" align="left" class="theader"><bean:message bundle="budget" key="budgetLineItemDetailstableHeader.cost"/></td>
        <td width="10%" align="left" class="theader"><bean:message bundle="budget" key="budgetLineItemDetailstableHeader.costSharing"/></td>
    </tr>
    <%String strBgColor = "#DCE5F1"; %>
    <%int count = 0;%>
     <logic:iterate id="calAmts" name="noDetailsForPersonnel"
             type="org.apache.struts.validator.DynaValidatorForm">
             
                                   <% if (count%2 == 0) 
                                         strBgColor = "#D6DCE5"; 
                                     else 
                                        strBgColor="#DCE5F1";%>
                                    <tr bgcolor="<%=strBgColor%>" valign="top" >
                                        <td align="left" width='24%' nowrap bgcolor='#D1E5FF'>
                                            <bean:write name='calAmts' property="rateTypeDescription"/></td>
                                        <td align="center" class="cltextbox-equip" width='5%' >                                                                                              
                                        <% 
                                            boolean flagValue = ((Boolean)calAmts.get("applyRateFlag")).booleanValue();%>
                                            <%if(flagValue){%>
                                             <input type="checkbox" name="applyRateFlag" checked disabled>
                                          <% }else{%>
                                                <input type="checkbox" name="applyRateFlag" disabled>
                                            <%}%>
                                       </td>
                                       <td align="left" bgcolor='#D1E5FF' width='10%'> 
                                        <% String calculatedCost = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(calAmts.get("calculatedCost")); %>
                                        <html:text property="calculatedCost" value="<%=calculatedCost%>" readonly="true" styleClass="cltextbox-nonEditcolor" style="width: 120px;"/>
                                        </td>
                                        <td align="left" nowrap class="cltextbox-equip" width='15%'>   
                                         <% calculatedCost = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(calAmts.get("calculatedCostSharing")); %>                                
                                        <html:text property="calculatedCostSharing" value="<%=calculatedCost%>" readonly="true" styleClass="cltextbox-nonEditcolor" style="width: 120px;"/>

                                        </td>

                                    </tr>
                                  <%count++;%>

         </logic:iterate>
         <tr>
            <td colspan='6' class='table'>&nbsp;
            </td>
         </tr>
         <tr>
         <td colspan='6' class="tabtable" align=right>
            <html:link href="javascript:close_action();">
                <u><bean:message bundle="budget" key="budgetLineItemDetailsButton.close"/> </u>
            </html:link>
        </td>
      </tr>
     </table>
     </html:form>
     </body>
</html:html>     