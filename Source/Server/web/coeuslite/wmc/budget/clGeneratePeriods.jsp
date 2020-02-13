<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<%@ taglib uri="/WEB-INF/struts-logic.tld"  prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld"  prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld"  prefix="html"%>
<jsp:useBean  id="pageConstantValue" scope="session" class="java.lang.String"/>

<html:html locale="true">
<%  boolean isPeriodsGenerated = false;
    if(session.getAttribute("Generated")!= null){
        isPeriodsGenerated = ((Boolean)session.getAttribute("Generated")).booleanValue();
    }
    
    String mode = (String)session.getAttribute("mode"+session.getId());  
    String budgetStatusMode = (String)session.getAttribute("budgetStatusMode");
    boolean readOnly = false;
    if(mode!= null && mode.equals("display")){
       readOnly = true;
    }else if(budgetStatusMode != null && budgetStatusMode.equals("complete")){
        readOnly = true;
    }
%>
<head>
<script language = "JavaScript">
function generatePeriod(){
 // Hide the code in first div tag
    document.getElementById('generatedPeriodsDiv').style.display = 'none';
 // Display code in second div tag
    document.getElementById('messageDiv').style.display = 'block';
    return;
}
</script>

</head>

<body>
    
<div id="generatedPeriodsDiv">
        <table width="100%" height="100%"  border="0" cellpadding="0" cellspacing="0" class="table">
            <tr>
                <td>
                    <table width="99%"  border="0" align="center" cellpadding="4" cellspacing="0" class="table">
                        <tr>
                            <td align = "left" class = "tableheader">
                                <bean:message bundle="budget" key="generate.title"/>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr class = "copy">
                <td>
                     <logic:messagesPresent message = "true">  
                     <font color='red'>
                        <html:messages id="message" message="true" property="generate.period_generated" bundle="budget">                
                           <li>                            
                                    <bean:write name = "message"/>                                
                           </li>
                      </html:messages>
                      </font>
                  </logic:messagesPresent>   
                </td>
            </tr>
            <tr>
                <td>
                    <table width="99%"  border="0" align="center" cellpadding="4" cellspacing="0" class="tabtable">
                        <tr>
                            <td class = "copybold">
                              <% if(!isPeriodsGenerated){%>
                                <bean:message bundle="budget" key="generate.infoMessage1"/>
                                <bean:message bundle="budget" key="generate.infoMessage2"/>                               
                                 <%if(!readOnly){%>
                                 <bean:message bundle="budget" key="generate.infoMessage3"/>
                                 <html:link action="/generateAllPeriods.do" onclick="generatePeriod()" scope="page"><u> 
                                                            <bean:message bundle="budget" key="generate.title"/>  </u>
                                </html:link><bean:message bundle="budget" key="generate.infoMessage4"/>
                                <%}}else{%>
                                    <bean:message bundle="budget" key="generate.periods.successfull"/>
                                <%}%>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
</div>
<div id='messageDiv' style='display: none;'>
    <table width="100%" height="100%" align="center" border="0" cellpadding="3" cellspacing="0" class="tabtable">
        <tr>
            <td align='center' class='copyred'>  <br> <bean:message bundle="budget" key="budgetMessages.pleaseWait"/>  </td>
        </tr>
    </table>
</div>

</body>
</html:html>
