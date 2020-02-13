<%@ page import="edu.mit.coeuslite.utils.CoeusDynaBeansList,
                java.util.List, java.lang.Double,org.apache.struts.action.DynaActionForm"%>
<%@ taglib uri="/WEB-INF/struts-html.tld"     prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld"     prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld"     prefix="logic" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"     prefix="coeusUtils" %>
<jsp:useBean id="subAwardDetailList" scope="request" class="edu.mit.coeuslite.utils.CoeusDynaBeansList" />
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
   
<html>
    <%
    String organizationName = (String)session.getAttribute("subAwardOrganizationName");
    String readOnlyMode = (String)session.getAttribute("subAwardDetailsMode");
    String enableSync = (String)session.getAttribute("enableSync");
    boolean disabled = true;
    if("false".equalsIgnoreCase(readOnlyMode)){
        disabled = false;
    }
    boolean enableSyncButton = true;
    if("false".equalsIgnoreCase(enableSync)){
        enableSyncButton = false;
    }
    
    java.text.NumberFormat numberFormat = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US);
    boolean closeWindow = false;
    if(request.getAttribute("closeSubAwardDetails") != null){
        closeWindow = ((java.lang.Boolean)request.getAttribute("closeSubAwardDetails")).booleanValue();
    }
    String strBgColor = "#DCE5F1";
    %>
    
    <head>
        <title>Sub Award Details</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/ipf.css" rel="stylesheet" type="text/css">
        <link href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css" rel="stylesheet" type="text/css">
    </head>
    <body>
        <script>
            function validateAndSetTotalCost(periodIndex, fieldName){  
                var acTypeField = "dynaFormBean["+periodIndex+"].acType";
                acTypeField = document.getElementsByName(acTypeField);
                
                if(acTypeField[0].value != 'I'){
                    acTypeField[0].value = 'U';
                }
                var costField = "dynaFormBean["+periodIndex+"]."+fieldName;
                costField = document.getElementsByName(costField);
                var costValue = costField[0].value;
                if(costValue == "" && costValue.length == 0){
                    costValue = '0.00';
                }
                var costLength = costValue.length;
                for(var i=0;i<costLength;i++){            
                    costValue = costValue.replace(",","");
                    costValue = costValue.replace("$","");
                }
                if(costValue<0){
                    costField[0].value = '$'+'0.00';  
                    costValue = costField[0].value;
                }
                 
                if(isNaN(costValue)){
                    alert("Please Enter a Cost"); 
                    costField[0].value = '$'+'0.00'; 
                    if(fieldName != 'costSharingAmount'){
                        setTotalCostValue('0.00', periodIndex,fieldName);
                    }
                    return;
                }else if(fieldName != 'costSharingAmount'){
                    setTotalCostValue(costValue, periodIndex,fieldName);
                }
            }   
       
            function getCostValue(periodIndex,fieldName){
                var costField = "dynaFormBean["+periodIndex+"]."+fieldName;
                costField = document.getElementsByName(costField);
                var costValue = costField[0].value;
                if(costValue == "" && costValue.length == 0){
                    costValue = '0.00';
                }
                var costLength = costValue.length;
                for(var i=0;i<costLength;i++){            
                    costValue = costValue.replace(",","");
                    costValue = costValue.replace("$","");
                }
                if(costValue<0){
                    alert('costValue<0');
                    costValue = costField[0].value;
                }
                return costValue;
            }
            
            function setTotalCostValue(currentFieldCostValue, periodIndex,fieldName){
                var costToAdd;
                if(fieldName == 'directCost'){
                    costToAdd = getCostValue(periodIndex,'indirectCost');
                }else if(fieldName == 'indirectCost'){
                    costToAdd =  getCostValue(periodIndex,'directCost');
                }
                var totalCost = parseFloat(currentFieldCostValue) + parseFloat(costToAdd);
                totalCost = Math.round(totalCost*Math.pow(10,2))/Math.pow(10,2);
                var is_dot_ok = totalCost.toString().indexOf(".");
                if(is_dot_ok == -1){
                    totalCost = totalCost+'.00';                 
                }              
                if(totalCost.toString().length - is_dot_ok == 2){
                    totalCost = totalCost+'0';  
                }
                var totalCostField = "dynaFormBean["+periodIndex+"].totalCost";
                totalCostField = document.getElementsByName(totalCostField);
                totalCostField[0].value = '$'+totalCost;       
            }
            
            function SyncXML(){
                var answer = confirm("Are you sure you want to sync the sub award details ?");
                if(answer){
                    document.subAwardDetailDynaList.action = "<%=request.getContextPath()%>/syncXMLSubAwardDetails.do";
                    document.subAwardDetailDynaList.submit();
                }
               
            }
            
        </script>
     <html:form action="/saveSubAwardDetails.do" method="post"> 
            
        <script>
               <%if(closeWindow){%>
                  parent.opener.location="<%=request.getContextPath()%>/getSubAwardBudget.do";
                  window.close();
              <%}%>
        </script>
        
        <table width="99%" border="0" align="center" cellpadding="2" cellspacing="0" class="tabtable">
            <tr class="theader">
                <td  colspan="5"><bean:message bundle="budget" key="subAwardBudget.Organization"/>: <%=organizationName%></td>
            </tr>
            <logic:messagesPresent message = "true"> 
            <tr>
                <td height='20' colspan='4' class='copybold'>
                    <font color='red'>
                        <html:errors header="" footer=""/>
                    </font>
                        <font color="red">                 
                        <html:messages bundle="coeus" id="message" message="true" property="sync.failed">                
                            <li><bean:write name = "message"/></li>
                        </html:messages>                  
                </td>
            </tr>
            </logic:messagesPresent>
            <tr>
                <td class='table' nowrap colspan="5">
                    <b>
                        <font color="red">
                            <logic:messagesPresent message = "true">                                       
                                <html:messages id="message" message="true" property="subAwardDetailCostElementInactive.error" bundle="budget">
                                    <li><bean:write name = "message"/></li>
                                </html:messages>   
                            </logic:messagesPresent>                                   
                        </font>
                    </b>                                   
                </td>
            </tr>
            <tr align="center" valign="top">
                <td align="left" class="theader" width="3%" >&nbsp;<bean:message bundle="budget" key="summaryLabel.Period"/>
                <td align="left" class="theader" width="24%" >&nbsp;<bean:message bundle="budget" key="summaryLabel.DirectCost"/>
                <td align="left" class="theader" width="24%" >&nbsp;<bean:message bundle="budget" key="summaryLabel.IndirectCost"/>
                <td align="left" class="theader" width="24%" >&nbsp;<bean:message bundle="budget" key="summaryLabel.CostSharing"/>
                <td align="left" class="theader" width="24%" >&nbsp;<bean:message bundle="budget" key="summaryLabel.TotalCost"/>
            </tr>
             
            <logic:iterate id="dynaFormData" name="subAwardDetailList" property="beanList" type="org.apache.struts.action.DynaActionForm" indexId="periodIndex" scope="request"> 
                <%
                    
                    if(periodIndex%2 == 0){
                        strBgColor = "#D6DCE5"; 
                    }else{ 
                        strBgColor="#DCE5F1"; 
                    }  
                    String budgetPeriod = ((java.lang.Integer)dynaFormData.get("budgetPeriod")).toString();
                    String acType = (String)dynaFormData.get("acType");
                    double directCostDouble = Double.parseDouble(dynaFormData.get("directCost").toString());
                    String directCost = numberFormat.format(directCostDouble);
                    double inDirectCostDouble = Double.parseDouble(dynaFormData.get("indirectCost").toString());
                    String inDirectCost = numberFormat.format(inDirectCostDouble);
                    double costSharingDouble = Double.parseDouble(dynaFormData.get("costSharingAmount").toString());
                    String costSharingAmount = numberFormat.format(costSharingDouble);
                    String totalCost = numberFormat.format(directCostDouble + inDirectCostDouble);
                %>
                <tr bgcolor="<%=strBgColor%>" onmouseover="className='TableItemOn'" onmouseout="className='TableItemOff'">
                    <td width="3%">
                        <%=budgetPeriod%>
                    </td>
                    <td width="3%">
                        <%String getDirectCost = "javascript: validateAndSetTotalCost('"+periodIndex+"','directCost');";%>
                        <html:text name="dynaFormBean" size="14" maxlength="14" property="directCost"  style="text-align: right;width:108px" styleClass="cltextbox"  indexed="true" disabled="<%=disabled%>" value="<%=directCost%>" onchange="<%=getDirectCost%>"/>
                    </td>
                    <td width="24%">
                        <%String getInDirectCost = "javascript: validateAndSetTotalCost('"+periodIndex+"','indirectCost');";%>
                        <html:text name="dynaFormBean" size="14" maxlength="14" property="indirectCost" style="text-align: right;width:108px" styleClass="cltextbox"  indexed="true" disabled="<%=disabled%>" value="<%=inDirectCost%>" onchange="<%=getInDirectCost%>"/>                                             
                    </td>
                    <td width="24%">
                        <%String getCostSharingAmount = "javascript: validateAndSetTotalCost('"+periodIndex+"','costSharingAmount');";%>
                        <html:text name="dynaFormBean" size="14" maxlength="14" property="costSharingAmount" style="text-align: right;width:108px" styleClass="cltextbox" indexed="true" disabled="<%=disabled%>" value="<%=costSharingAmount%>" onchange="<%=getCostSharingAmount%>"/>                                           
                    </td>
                    <td width="24%">
                        <html:text name="dynaFormBean" size="14" maxlength="14" property="totalCost" style="text-align: right;width:108px" styleClass="cltextbox" indexed="true" disabled="<%=disabled%>" readonly="true" value="<%=totalCost%>"/>                                            
                    </td>
                    <html:hidden name="dynaFormBean" property="acType" value="<%=acType%>" indexed="true"/>
                </tr>
            </logic:iterate> 
            <tr align="right" class="theader" height='30'>
                <td align='left' colspan="5">
                    <%if(!disabled){%>
                        <html:submit property="Save" value="Save" styleClass="clbutton"/>
                        <%if(enableSyncButton){%>
                        <!-- JM 6-19-2015 commented out Sync XML button, does not work
                        <html:button property="Sync" value="Sync XML" styleClass="clbutton" onclick="javascript:SyncXML()"/>
                        JM END -->
                        <%}%>
                    <%}%>
                    <html:button property="Close" value="Close" styleClass="clbutton" onclick="window.close();"/>
                </td>
                
            </tr>
        </table>
      </html:form>
    </body>
</html>
