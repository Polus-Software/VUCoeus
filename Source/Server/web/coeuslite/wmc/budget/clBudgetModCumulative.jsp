<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<%@ page import="java.util.Vector"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld"  prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld"  prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"  prefix="coeusUtils"%>
<jsp:useBean  id="cumulativeData"  scope="session" class="java.util.HashMap"/>
<html:html locale="true">
<head><title>Budget Modular</title>
<script language="JavaScript">
      /* Method to activate tabs */
	function activateTab(requestPeriod) {
            document.coeusBudgetModularDynaList.action = "<%=request.getContextPath()%>/getBudgetModularData.do?ModularPeriod="+requestPeriod;
            document.coeusBudgetModularDynaList.submit();
            document.getElementById('modularCumulativeFormDiv').style.display = 'none';
            document.getElementById('modularCumulativeFormDivWait').style.display = 'block';
	}
 
</script>


</head>
<html:base/>
<body>
<html:form action="/getBudgetModularData.do" method="POST">
<div id='modularCumulativeFormDiv'>
<table width="100%" height="100%"  border="0" cellpadding="0" cellspacing="0" class="table">
  <tr>
    <td  align="left" valign="top">
        <table width="99%"   border="0" align="center" cellpadding="0" cellspacing="0">
                <tr>
                    <logic:present name ="cumulativeData" scope = "session"> 
                        <bean:define id="periodBeanData"  name="cumulativeData" property="budgetPeriodCumulativeData" type="java.util.Vector" />                                                            
                                         
                       <logic:iterate id = "budgetModularTab" name ="periodBeanData" type = "org.apache.commons.beanutils.DynaBean">
                        <td width="57" height="27" align="center" valign="middle" 
                                        STYLE="background-image:url('<bean:write name='ctxtPath'/>/coeusliteimages/cumtab_bg.gif');border:0;cursor: pointer;" class="tabbg" 
                                onClick="activateTab(<%=budgetModularTab.get("budgetPeriod")%>)">
                                <bean:message bundle="budget" key="budgetLabel.period" />&nbsp;<%=budgetModularTab.get("budgetPeriod")%> </td>
                       </logic:iterate>
                       <td width="85" height="27" align="center" valign="middle" STYLE="background-image:url('<bean:write name='ctxtPath'/>/coeusliteimages/cumtab_bg_hl.gif');border:0;" class="tabbghl">
                                        <bean:message bundle="budget" key="budgetModCumulative.Cumulativelabel" /></td>
                    </logic:present>
                 <td  align="left" valign="top">&nbsp;</td>
              </tr>                
        </table>
    </td>
  </tr>
  <tr>
  <td>
   <logic:present name ="cumulativeData" scope = "session">
   <table align='center' width="100%" border="0" cellpadding="0" cellspacing="0" class="tabtable">
    <tr>
        <td width='100%' colspan='2' class='tableheader'>
            
                    <bean:message bundle="budget" key="budgetModCumulative.label" />
               
        </td>
        </tr>
        <tr class='copybold'>
            <td height='5'></td>
        </tr>
       <tr >
            <td width="40%" align='right' height='20' class='copybold'><bean:message bundle="budget" key="budgetModCumulative.entireDCLessFNA" /></td>
            <td width="40%" height='20' class='copy'>&nbsp;&nbsp;&nbsp;&nbsp;<coeusUtils:formatString name="cumulativeData" property="entireDcLessFNA" formatType="currencyFormat"/></td>
        </tr>
        <tr >
            <td width="40%" align='right' height='20' class='copybold'><bean:message bundle="budget" key="budgetModCumulative.entireConsFNA" /></td>
            <td width="40%" height='20' class='copy'>&nbsp;&nbsp;&nbsp;&nbsp;<coeusUtils:formatString name="cumulativeData" property="entireConsFNA" formatType="currencyFormat"/></td>
        </tr>
        <tr>
            <td width="40%" align='right' height='20' class='copybold'><bean:message bundle="budget" key="budgetModCumulative.entireTDC" /></td>
            <td width="40%" height='20' class='copy'>&nbsp;&nbsp;&nbsp;&nbsp;<coeusUtils:formatString name="cumulativeData" property="entireDC" formatType="currencyFormat"/></td>
        </tr>
        <tr >
            <td width="40%" align='right' height='20' class='copybold'><bean:message bundle="budget" key="budgetModCumulative.entireTIDC" /></td>
            <td width="40%" height='20' class='copy'>&nbsp;&nbsp;&nbsp;&nbsp;<coeusUtils:formatString name="cumulativeData" property="entireIDC" formatType="currencyFormat"/></td>
        </tr>
        <tr >
            <td width="40%" align='right' height='20' class='copybold'><bean:message bundle="budget" key="budgetModCumulative.entireTDCNIDC" /></td>
            <td width="40%" height='20' class='copy'>&nbsp;&nbsp;&nbsp;&nbsp;<coeusUtils:formatString name="cumulativeData" property="entireDCIDC" formatType="currencyFormat"/></td>
        </tr>
        <tr class='copybold'>
            <td height='15'></td>
        </tr>
        
     </table>
  </logic:present>
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
</html:form>
</body>
</html:html>
