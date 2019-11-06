<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<%@ page import="java.util.Vector,java.util.List,edu.mit.coeuslite.utils.CoeusDynaBeansList"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld"  prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld"  prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"  prefix="coeusUtils"%>
<jsp:useBean  id="budgetModPeriodData"  scope="session" class="java.util.Vector"/>
<%
    double totalFundsIDC = 0;
    double totalDCFunds = 0;
    String strBudgetModLink = "";
    String totalOutput = "";
    String changeScript = "";
    Double dblOldValue = null;
   String mode = (String)session.getAttribute("mode"+session.getId());
   String budgetStatusMode = (String)session.getAttribute("budgetStatusMode");
   boolean readOnly = false;
   if((mode!= null && mode.equals("display")) || (budgetStatusMode != null && budgetStatusMode.equals("complete"))){
       readOnly = true;
   }
 //for Checking Data Changes/Inserted
 CoeusDynaBeansList coeusBudgetModularDynaList 
    = (CoeusDynaBeansList)session.getAttribute("coeusBudgetModularDynaList");
 CoeusDynaBeansList oldBudgetModData
     = (CoeusDynaBeansList)session.getAttribute("oldBudgetModularData");
 boolean isDataInserted = false;
 if((oldBudgetModData.getBeanList().size() < coeusBudgetModularDynaList.getBeanList().size())){
         isDataInserted = true;
 }
 int budgetModBeanPeriod = ((Integer)session.getAttribute("budgetModPeriod")).intValue();
%>
<html:html locale="true">
<head><title>Budget Modular</title>
<script language="JavaScript">
        var changeData = "";
        var errValue = false;
        var errLock = false;
	function activateTab(requestPeriod, prevPeriod) {
            document.coeusBudgetModularDynaList.action = "<%=request.getContextPath()%>/getBudgetModularData.do?ModularPeriod="+requestPeriod;
            CLICKED_LINK = "<%=request.getContextPath()%>/getBudgetModularData.do?ModularPeriod="+requestPeriod;
            if(requestPeriod != prevPeriod){
                if(!<%=readOnly%>){
                    if(!validate()){
                        var msg = '<bean:message bundle="budget" key="budgetLabel.saveConfirmation" />';
                        /*if (confirm(msg)==true){
                            document.coeusBudgetModularDynaList.action 
                                = "<%=request.getContextPath()%>/saveBudgetModularData.do?ModularPeriod="+prevPeriod+"&requestPeriod="+requestPeriod;
                            document.coeusBudgetModularDynaList.submit();
                            document.getElementById('modularCumulativeFormDiv').style.display = 'none';
                            document.getElementById('saveDiv').style.display = 'block';
                        }*///end if
                    }else{
                        document.coeusBudgetModularDynaList.submit();
                        document.getElementById('modularCumulativeFormDiv').style.display = 'none';
                        document.getElementById('modularCumulativeFormDivWait').style.display = 'block';
                    }
                 }else{
                        //display mode
                        document.coeusBudgetModularDynaList.submit();
                        document.getElementById('modularCumulativeFormDiv').style.display = 'none';
                        document.getElementById('modularCumulativeFormDivWait').style.display = 'block';
                 }
            }
        }
	function activateCumulativeTab(){
            //check here for save confirmation...period is available in Dyna List
            document.coeusBudgetModularDynaList.action = "<%=request.getContextPath()%>/cumulativeBudgetModular.do";
            CLICKED_LINK = "<%=request.getContextPath()%>/cumulativeBudgetModular.do";
            if(!<%=readOnly%>){
                var budgetPeriod = "<%=budgetModBeanPeriod%>";
                LINK = "<%=request.getContextPath()%>/saveBudgetModularData.do?ModularPeriod="+budgetPeriod+"&Cumulative=Cumulative";
                if(!validate()){
                    var msg = '<bean:message bundle="budget" key="budgetLabel.saveConfirmation" />';
                    /*if (confirm(msg)==true){
                        document.coeusBudgetModularDynaList.action 
                            = "<%=request.getContextPath()%>/saveBudgetModularData.do?ModularPeriod="+budgetPeriod+"&Cumulative=Cumulative";
                        document.coeusBudgetModularDynaList.submit();
                        document.getElementById('modularCumulativeFormDiv').style.display = 'none';
                        document.getElementById('saveDiv').style.display = 'block';
                    }//end if
                    */
                }else{
                    document.coeusBudgetModularDynaList.submit();
                    document.getElementById('modularCumulativeFormDiv').style.display = 'none';
                    document.getElementById('modularCumulativeFormDivWait').style.display = 'block';
                }
             }else{
                document.coeusBudgetModularDynaList.submit();
                document.getElementById('modularCumulativeFormDiv').style.display = 'none';
                document.getElementById('modularCumulativeFormDivWait').style.display = 'block';
             }
	}
	function budgetModLink(link,type){
            if(type == "S"){
                var budgetPeriod = "<%=budgetModBeanPeriod%>";
                document.coeusBudgetModularDynaList.action = "<%=request.getContextPath()%>/saveBudgetModularData.do?ModularPeriod="+budgetPeriod;
                document.coeusBudgetModularDynaList.submit();
                document.getElementById('modularCumulativeFormDiv').style.display = 'none';
                document.getElementById('saveDiv').style.display = 'block';
            }else{
                
                if(type == "A" || type == "D"){
                    document.coeusBudgetModularDynaList.action = "<%=request.getContextPath()%>"+link;
                    document.coeusBudgetModularDynaList.submit();
                    document.getElementById('modularCumulativeFormDiv').style.display = 'none';
                    document.getElementById('modularCumulativeFormDivWait').style.display = 'block';
                }else if(type == "SY"){
                    var msg = '<bean:message bundle="budget" key="budgetModCumulative.syncConfirmation" />';
                    if(confirm(msg) == true){
                        document.coeusBudgetModularDynaList.action = "<%=request.getContextPath()%>"+link;
                        document.coeusBudgetModularDynaList.submit();
                        document.getElementById('modularCumulativeFormDiv').style.display = 'none';
                        document.getElementById('syncedDiv').style.display = 'block';
                    }
                    
                }
            }
            
	}
	//method to check data is changed or not
	function dataModified(index, type, oldValue){
           var modifiedValue = "";
           var removeDollar = new RegExp("[$,/,]","g");
           dataChanged();
           if(type == "IDCRate"){
                modifiedValue = "dynaFormBean["+index+"].strDirectCostFA";
                modifiedValue = modifiedValue[0].value;
                if(modifiedValue != oldValue){
                   changeData = "true";
                }
            }else if(type == "Desc"){
                modifiedValue = "dynaFormBean["+index+"].description";
                modifiedValue = document.getElementsByName(modifiedValue);
                modifiedValue = modifiedValue[0].value;
                modifiedValue = modifiedValue.replace( /^\s*/, "" ).replace( /\s*$/, "" );
                if(modifiedValue != oldValue){
                    changeData = "true";
                }
            }else{
                if(type == "DcLess"){
                    modifiedValue = "dynaFormData["+index+"].strDirectCostFA";
                }else if(type == "ConsFNA"){
                    modifiedValue = "dynaFormData["+index+"].strConsortiumFNA";
                }else if(type == "IDCBase"){
                    modifiedValue = "dynaFormBean["+index+"].strIdcBase";
                }else if(type == "Fund"){
                    modifiedValue = "dynaFormBean["+index+"].strFundRequested";
                }
                modifiedValue = document.getElementsByName(modifiedValue);
                modifiedValue = modifiedValue[0].value;  
                modifiedValue = modifiedValue.replace(removeDollar, '');
                oldValue = oldValue.toString();
                if(modifiedValue != oldValue){
                    changeData = "true";
                    
                }
            }
         }//end function
 
</script>


</head>
<html:base/>
<body onkeypress='dataChanged()'>
<html:form action="/getBudgetModularData.do" method="POST">
<div id='modularCumulativeFormDiv'>
<table width="100%" height="100%"  border="0" cellpadding="0" cellspacing="0" class="table">
<tr>
    <td align='left' class="copy">
        <font color='red'>
            <logic:messagesPresent>
                <script>errValue = true; </script>
                <html:errors header=" " footer = " "/>
            </logic:messagesPresent>
            <logic:messagesPresent message="true">
                <html:messages id="message" message="true" property="errMsg"> 
                    <script>errLock = true; </script>
                    <li><bean:write name = "message"/></li>
                </html:messages>               
            </logic:messagesPresent>
        </font>
    </td>
</tr>
<tr>
    <td>
        <div id="helpText" class='helptext'>            
            <bean:message bundle="budget" key="helpTextBudget.Modular"/>  
        </div> 
    </td>
</tr>
<!-- Tab Section start -->
<tr>
    <td align="left" valign="top">
        <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
                <tr>
                       <logic:iterate id="budgetModularTab" name ="budgetModPeriodData" type="org.apache.commons.beanutils.DynaBean">
                       <%  int periodNumber = ((Integer)budgetModularTab.get("budgetPeriod")).intValue(); 
                            if(budgetModBeanPeriod == periodNumber){%>
                            <td width="57" height="27" align="center" valign="middle" STYLE="background-image:url('<bean:write name='ctxtPath'/>/coeusliteimages/tab_bg_hl.gif');border:0;" class="tabbghl">
                                        <bean:message bundle="budget" key="budgetLabel.period" />&nbsp;<%=budgetModularTab.get("budgetPeriod")%> </td>
                            <%}else{%>
                            <td width="57" height="27" align="center" valign="middle" 
                                        STYLE="background-image:url('<bean:write name='ctxtPath'/>/coeusliteimages/tab_bg.gif');border:0;cursor: pointer;" class="tabbg" 
                                onClick="activateTab(<%=budgetModularTab.get("budgetPeriod")%>, <bean:write name="budgetModPeriod"/>)">
                                <bean:message bundle="budget" key="budgetLabel.period" />&nbsp;<%=budgetModularTab.get("budgetPeriod")%> </td>
                       <%}%>                                
                       </logic:iterate>
                       <td width="85" height="27" align="center" valign="middle" STYLE="background-image:url('<bean:write name='ctxtPath'/>/coeusliteimages/cumtab_bg.gif');border:0;cursor: pointer;" class="tabbg" 
                            onClick="activateCumulativeTab()">
                            <bean:message bundle="budget" key="budgetModCumulative.Cumulativelabel" />
                       </td>
                    <td align="left" valign="top">&nbsp;</td>
              </tr>                
        </table>
    </td>
</tr>
<!-- Tab Section end -->

<!-- Start and end date begin  -->
<tr>
    <td style='core'>
        <logic:iterate id="data" name="budgetModPeriodData" type = "org.apache.commons.beanutils.DynaBean">
        <%  int periodNumber = ((Integer)data.get("budgetPeriod")).intValue(); 
            if(budgetModBeanPeriod == periodNumber){%>
        <table align='center' width="100%" border="0" cellpadding="0" cellspacing="0" class='tabtable'>
            <tr>
                <td align='left' class='copybold'>
                        &nbsp;
                        <bean:message bundle="budget" key="budgetModCumulative.period" />
                        :&nbsp;
                        <coeusUtils:formatDate name="data" property="startDate" />
                        &nbsp;-&nbsp;
                        <coeusUtils:formatDate name="data" property="endDate" />
                </td>
            </tr>
        </table>
        <%}%>
        </logic:iterate>
     </td>
</tr>
<!-- Start and end date end  -->  
  
<tr>
<td height='10'></td>
</tr>

<!-- Direct Cost Begin -->
<tr>
    <td style='core'>
    <logic:iterate id="dynaFormData" name="coeusBudgetModularDynaList" property="list" type="org.apache.struts.action.DynaActionForm" indexId="dcIndex">
    <% 
       
       String dcLess = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(((Double)dynaFormData.get("directCostFA")).doubleValue());
       String consFA = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(((Double)dynaFormData.get("consortiumFNA")).doubleValue()); 
       
     String strDCLess = (String)dynaFormData.get("strDirectCostFA");
     String strConsFA = (String)dynaFormData.get("strConsortiumFNA");
     if(strDCLess!=null && !strDCLess.equals("")){
         String strDCCost = strDCLess;
         strDCLess = strDCLess.replaceAll("[$,/,]","");
         try{
         strDCLess 
                = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(new Double(strDCLess));
         }catch(java.lang.NumberFormatException ne){
             strDCLess = strDCCost;
         }
         dcLess = strDCLess;
      }
      if(strConsFA!=null && !strConsFA.equals("")){
         String strConsCost = strConsFA;
         strConsFA = strConsFA.replaceAll("[$,/,]","");
         try{
         strConsFA 
                = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(new Double(strConsFA));
         }catch(java.lang.NumberFormatException ne){
             strConsFA = strConsCost;
         }
         consFA = strConsFA;
      }
      totalDCFunds = totalDCFunds 
        + ((Double)dynaFormData.get("directCostFA")).doubleValue() + ((Double)dynaFormData.get("consortiumFNA")).doubleValue();
      totalOutput = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(totalDCFunds);
    %>
    <table align='center' width="100%" border="0" cellpadding="0" cellspacing="0" class="tabtable">
    
        <tr>
            <td width='100%' colspan='2' class='tableheader'>
                <bean:message bundle="budget" key="budgetModCumulative.dcLabel" />
            </td>
        </tr>
        
        <tr>
        <%   dblOldValue = (Double)dynaFormData.get("directCostFA");
        changeScript = "dataModified('"+dcIndex+"','DcLess','"+dblOldValue+"')"; %>
            <td width="70%" align='right' height='20' class='copybold'>
                <bean:message bundle="budget" key="budgetModCumulative.dcLessFNA" />
            </td>
            <td width="30%" height='20'>&nbsp;&nbsp;&nbsp;&nbsp;
                <html:text name="dynaFormData"  property="strDirectCostFA"  value = "<%=dcLess%>" 
                    styleClass="cltextbox-number-medium"  style="text-align: right" indexed="true" readonly="<%=readOnly%>" onchange="<%=changeScript%>" />
                <html:hidden property="directCostFA" name="dynaFormData" indexed = "true"/>
            </td>
        </tr>
        
        <tr>
            <% 
                dblOldValue = (Double)dynaFormData.get("consortiumFNA");
                changeScript = "dataModified('"+dcIndex+"','ConsFNA','"+dblOldValue+"')"; %>
            <td width="70%" align='right' height='20' class='copybold'>
                <bean:message bundle="budget" key="budgetModCumulative.consFNA" />
            </td>
            <td width="30%" height='20'>&nbsp;&nbsp;&nbsp;&nbsp;
                <html:text name="dynaFormData"  property="strConsortiumFNA"  value = "<%=consFA%>" 
                    styleClass="cltextbox-number-medium"  style="text-align: right" indexed="true" readonly="<%=readOnly%>" onchange="<%=changeScript%>" />
                <html:hidden property="consortiumFNA" name="dynaFormData" indexed = "true"/>
            </td>
        </tr>
        
        <tr class='copybold'>
            <td width="70%" align='right' height='20'>
                <bean:message bundle="budget" key="budgetModCumulative.totalDCCost" />
            </td>
            <td width="30%" height='20'>&nbsp;&nbsp;&nbsp;&nbsp;
                <html:text property="totalDCModular" value="<%=totalOutput%>" readonly="true" style="text-align: right" styleClass="cltextbox-equip"/>
            </td>
        </tr>
        


        
     </table>
     </logic:iterate>
    </td>
</tr>
<!-- Direct Cost End -->  
  
  
<tr>
<td height='10'></td>
</tr>
  
<!-- Indirect Cost Begin -->
<tr>
    <td style='core'>
    <table align='center' width="100%" border="0" cellpadding="1" cellspacing="1" class="tabtable">
    
        <tr>
            <td width='100%' colspan='6' class='tableheader'>
                <bean:message bundle="budget" key="budgetModCumulative.idcLabel" />
            </td>
        </tr>

        <tr class='copybold'>
             <td nowrap width='10%' align="center" class="theader" ><bean:message bundle="budget" key="budgetModCumulative.rateLabel"/>
                </td>
             <td nowrap width='30%' align="center" class="theader"><bean:message bundle="budget" key="budgetModCumulative.idcType"/></td>
             <td width='15%'  align="center" class="theader">
                <bean:message bundle="budget" key="budgetModCumulative.idcRate"/>
             </td>
             <td width='15%'  nowrap align="center" class="theader" >
                <bean:message bundle="budget" key="budgetModCumulative.idcBase"/> 
             </td>
             <td width='18%'  align="center" class="theader">
                <bean:message bundle="budget" key="personnel.totalFunds"/>
             </td>
             <td width='12%'  align="center" class="theader">                
             </td>
        </tr>
        
        <tr class='copybold'>
            <td height='5'>
            </td>
        </tr>
    <logic:iterate id="dynaFormBean" name="coeusBudgetModularDynaList" property="beanList" type="org.apache.struts.action.DynaActionForm" indexId="idcIndex">
    <% 
       
       String idcBase = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(((Double)dynaFormBean.get("idcBase")).doubleValue());
       String idcfundRequested = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(((Double)dynaFormBean.get("fundRequested")).doubleValue()); 
       String idcRate = ""+((Double)dynaFormBean.get("idcRate")).doubleValue();
       
     String strIdcBase = (String)dynaFormBean.get("strIdcBase");
     String strIdcfundRequested = (String)dynaFormBean.get("strFundRequested");
     String strIdcRate = (String)dynaFormBean.get("strIdcRate");
     if(strIdcRate!=null && !strIdcRate.equals("")){
         idcRate = strIdcRate;
     }
     //to show in dollar currency format
     if(strIdcBase!=null && !strIdcBase.equals("")){
         String stridcBaseCost = strIdcBase;
         strIdcBase = strIdcBase.replaceAll("[$,/,]","");
         try{
         strIdcBase 
                = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(new Double(strIdcBase));
         }catch(java.lang.NumberFormatException ne){
             strIdcBase = stridcBaseCost;
         }
         idcBase = strIdcBase;
      }
      if(strIdcfundRequested!=null && !strIdcfundRequested.equals("")){
         String strFundCost = strIdcfundRequested;
         strIdcfundRequested = strIdcfundRequested.replaceAll("[$,/,]","");
         try{
         strIdcfundRequested 
                = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(new Double(strIdcfundRequested));
         }catch(java.lang.NumberFormatException ne){
             strIdcfundRequested = strFundCost;
         }
         idcfundRequested = strIdcfundRequested;
      }
      totalFundsIDC = totalFundsIDC + ((Double)dynaFormBean.get("fundRequested")).doubleValue() ;
     %>
    <tr>
        <td width='10%' align='center' class='copybold'><bean:write name="dynaFormBean" property="rateNumber"/></td>
        <td width='30%'>
            <% 
                String desc = (String)dynaFormBean.get("description");
                changeScript = "dataModified('"+idcIndex+"','Desc','"+desc+"')"; %>
            <html:text name="dynaFormBean" property="description" indexed="true" maxlength="60" 
                styleClass="clcombobox-longer" readonly="<%=readOnly%>" onchange="<%=changeScript%>"/>
        </td>
        <td width='15%'>
            <% 
                dblOldValue = (Double)dynaFormBean.get("idcRate");
                changeScript = "dataModified('"+idcIndex+"','IDCRate','"+dblOldValue+"')"; %>
            <html:text name="dynaFormBean" property="strIdcRate" styleClass="cltextbox-number-medium" 
                value="<%=idcRate%>" indexed="true" style="text-align: right" readonly="<%=readOnly%>" onchange="<%=changeScript%>"/>
            <html:hidden name="dynaFormBean" property="idcRate" styleClass="cltextbox-number-medium" indexed="true" />
        </td>
        <td width='15%'>
            <% 
                dblOldValue = (Double)dynaFormBean.get("idcRate");
                changeScript = "dataModified('"+idcIndex+"','IDCBase','"+idcBase+"')"; %>
            <html:text name="dynaFormBean" property="strIdcBase"  styleClass="cltextbox-number-medium" 
                value="<%=idcBase%>" indexed="true" style="text-align: right" readonly="<%=readOnly%>" onchange="<%=changeScript%>"/>
            <html:hidden property="idcBase" name="dynaFormBean" indexed = "true"/>
        </td>
        <td width='18%'>
            <% 
                dblOldValue = (Double)dynaFormBean.get("fundRequested");
                changeScript = "dataModified('"+idcIndex+"','Fund','"+dblOldValue+"')"; %>
            <html:text name="dynaFormBean" property="strFundRequested"  styleClass="cltextbox-number-medium" 
                value="<%=idcfundRequested%>" indexed="true" style="text-align: right" readonly="<%=readOnly%>" onchange="<%=changeScript%>"/>
            <html:hidden property="fundRequested" name="dynaFormBean" indexed = "true"/>
        </td>
        <td width='12%' align='center'>
            <%  if(readOnly){%>
                  <bean:message bundle="budget" key="projectIncome.Delete"/>
            <% }else{
                Integer rateNumber = (Integer)dynaFormBean.get("rateNumber");
                strBudgetModLink = "javaScript:budgetModLink('/deleteBudgetModularIDC.do?ModularPeriod="+budgetModBeanPeriod+"&RateNumber="+rateNumber+"','D')";%>
                <html:link href="<%=strBudgetModLink%>">
                    <bean:message bundle="budget" key="projectIncome.Delete"/>
                </html:link>
            <%}%>
        </td>
    </tr>
    </logic:iterate>
    <!-- Modified for IDC always visible  start -->
        <% totalOutput = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(totalFundsIDC); %>
        <tr class='copybold'>
			<td width="60%" align='right' height='20' colspan='4'>
                <bean:message bundle="budget" key="budgetModCumulative.totalCostIdc" />
            </td>
            <td width="40%" height='20' colspan='2'>
                <html:text property="totalIDCModular" value="<%=totalOutput%>" readonly="true" style="text-align: right" styleClass="cltextbox-equip"/>
            </td>
        </tr>
        <!-- Modified for IDC always visible  end -->
    <tr class='copybold'>
        <% double dblTotalDCIDC = totalFundsIDC + totalDCFunds;
            totalOutput = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(dblTotalDCIDC);%>
        <td width="60%" align='right' height='20' colspan='4'>
            <bean:message bundle="budget" key="budgetModCumulative.totalFunds" />
        </td>
        <td width="40%" height='20' colspan='2'>
            <html:text property="totalDCNIDCModular" value="<%=totalOutput%>" readonly="true" style="text-align: right" styleClass="cltextbox-equip"/>
        </td>
    </tr>


    <tr>
        <td class='copybold' colspan='6'>&nbsp;
            <%  if(readOnly){%>
                <bean:message bundle="budget" key="budgetModCumulative.addNewIdc"/>
                &nbsp;|&nbsp;
            <% }else{ %>
            <% strBudgetModLink = "javaScript:budgetModLink('/addBudgetModularIDC.do?ModularPeriod="+budgetModBeanPeriod+"','A')";%>
            <html:link href="<%=strBudgetModLink%>">
                <u><bean:message bundle="budget" key="budgetModCumulative.addNewIdc"/></u>
            </html:link>
            <%}%> 
            |

            <%  if(readOnly){%>
            <bean:message bundle="budget" key="budgetModCumulative.syncLabel"/>
            <% }else{ %>
            <% strBudgetModLink = "javaScript:budgetModLink('/syncedBudgetModular.do?ModularPeriod="+budgetModBeanPeriod+"','SY')";%>
            <html:link href="<%=strBudgetModLink%>">
            <u><bean:message bundle="budget" key="budgetModCumulative.syncLabel"/></u>
            </html:link>
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
<!-- Indirect Cost End -->

<tr>                    
    <td class='savebutton'>
        <html:button accesskey="s" property="Save" styleClass="clsavebutton" onclick="budgetModLink('S','S')" disabled="<%=readOnly%>">
            <bean:message bundle="budget" key="budgetButton.save"/>
        </html:button>
    </td>
</tr>
  
</table>
</div>
<div id='modularCumulativeFormDivWait' style='display: none;'>
    <table width="100%" height="100%" align="center" border="0" cellpadding="3" cellspacing="0" class="tabtable">
        <tr>
            <td align='center' class='copyred'>  <br> <bean:message bundle="budget" key="budgetMessages.pleaseWait"/>  </td>
        </tr>
    </table>
</div>
<div id='syncedDiv' style='display: none;'>
    <table width="100%" height="100%" align="center" border="0" cellpadding="3" cellspacing="0" class="tabtable">
        <tr>
            <td align='center' class='copyred'><bean:message bundle="budget" key="budgetModular.syncedPleaseWait"/> 
                <br> <bean:message bundle="budget" key="budgetMessages.pleaseWait"/>  
            </td>
        </tr>
    </table>
</div>
<div id='saveDiv' style='display: none;'>
    <table width="100%" height="100%" align="center" border="0" cellpadding="3" cellspacing="0" class="tabtable">
        <tr>
            <td align='center' class='copyred'><bean:message bundle="budget" key="budgetMessages.saving"/> 
                <br> &nbsp;&nbsp;&nbsp;  <bean:message bundle="budget" key="budgetMessages.pleaseWait"/>  
            </td>
        </tr>
    </table>
</div>
</html:form>
    <script>
          DATA_CHANGED = 'false';        
          var dataModi = '<%=request.getAttribute("dataModified")%>';
          if(dataModi == 'modified' || (errValue && !errLock)){
                DATA_CHANGED = 'true';
          }
          budgetPeriod = "<%=budgetModBeanPeriod%>";
          LINK = "<%=request.getContextPath()%>/saveBudgetModularData.do?ModularPeriod="+budgetPeriod;
          FORM_LINK = document.coeusBudgetModularDynaList;
          PAGE_NAME = "<bean:message bundle="budget" key="summaryLabel.ModularBudget"/>";
          function dataChanged(){
                DATA_CHANGED = 'true';
          }
          linkForward(errValue);
    </script>
     <script>
          var help = '<bean:message bundle="budget" key="helpTextBudget.Modular"/>';
          help = trim(help);
          if(help.length == 0) {
            document.getElementById('helpText').style.display = 'none';
          }
          function trim(s) {
                return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
          }  
    </script>
</body>
    <%session.removeAttribute("CLICKED");%>
</html:html>
