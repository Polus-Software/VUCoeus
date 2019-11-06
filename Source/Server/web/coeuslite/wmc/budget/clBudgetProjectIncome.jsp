<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<%@ page import="java.util.Vector,edu.wmc.coeuslite.budget.bean.CategoryBean,
                java.util.List,edu.wmc.coeuslite.utils.CoeusLineItemDynaList"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld"  prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld"  prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"  prefix="coeusUtils"%>

<jsp:useBean id="budgetPeriods" scope="session" class="java.util.Vector" />
<jsp:useBean id="summary" scope="session" class="java.util.ArrayList" />

<html:html locale="true">
<head>
<title>Project Income</title>
<script language="JavaScript">

    var index = "";
    var area = "";    
    var isError = "";
    var errValue = false;
    var errLock = false;
    function called(value) {
        if(value=='1'){
            document.getElementById('open_window').style.display = 'block';
            document.getElementById('hide_Add').style.display = 'block';
            document.getElementById('open_Add').style.display = 'none';
        }else if(value=='2'){
            document.getElementById('open_window').style.display = 'none';
            document.getElementById('hide_Add').style.display = 'none';
            document.getElementById('open_Add').style.display = 'block';
        }
    }
    
    function projectIncomeLink(type){
        if(type == "S"){
            document.budgetProjectIncomeList.action = "<%=request.getContextPath()%>/saveBudgetProjectIncome.do";
            document.budgetProjectIncomeList.submit();
        }
        if(type == "M"){
            document.budgetProjectIncomeList.action = "<%=request.getContextPath()%>/updateBudgetProjectIncome.do";
            document.budgetProjectIncomeList.submit();
        }
         //Hide the code in first div tag
         document.getElementById('projectIncome').style.display = 'none';
         //Display code in second div tag
         document.getElementById('saveDiv').style.display = 'block';     
 
        
    }    
    
    function deleteProjectIncome(link){
            document.budgetProjectIncomeList.action = "<%=request.getContextPath()%>"+link;
            document.budgetProjectIncomeList.submit();
            //Hide the code in first div tag
            document.getElementById('projectIncome').style.display = 'none';
            //Display code in second div tag
            document.getElementById('saveDiv').style.display = 'block';     
    }
    var txtValue = '';
    function fetch_Data(value) {
        var currentValue = "";
        if(area == '1'){
            currentValue = document.getElementsByName('dynaFormBean['+index+'].description');
        } else if(area == '2'){
            currentValue = document.getElementsByName('dynaFormData['+index+'].description');
        }
        if(value != txtValue)
            dataChanged();
        currentValue[0].value = value;
    }
    
    function openDesc(value) {
        index = value;
        area = '1';
        var currentValue = document.getElementsByName('dynaFormBean['+index+'].description');        
        var txtValue = currentValue[0].value;
        openWindow(index);        
    }
    
    function openDescription(value) {
        index = value;
        area = '2';
        var currentValue = document.getElementsByName('dynaFormData['+index+'].description');        
        txtValue = currentValue[0].value;
        openWindow(value);
    }
    
    function openWindow(txtValue){
        var w = 520;
        var h = 200;
        if(navigator.userAgent.indexOf("Netscape")!=-1) {
            h = 300;
           
        }
        if(navigator.userAgent.indexOf("Firefox")!=-1) {
            h = 213;
            w = 550;
        }
        
        if(navigator.userAgent.indexOf("Mozilla")!=-1) {
            h = 213;
            
        }

        if (window.screen) {            
                leftPos = Math.floor(((window.screen.width-w) / 2));
                topPos = Math.floor(((window.screen.height- h) / 2));
        }        
        var loc = '<bean:write name='ctxtPath'/>/coeuslite/wmc/utils/dialogs/clViewProjectIncome.jsp?value='+txtValue;
        var newWin = window.open(loc,"formName",'dependent=1,toolbar=0,location=0,directories=0,status=0, menubar=0,scrollbars=0,resizable=0,width=' + w + ',height=' + h+ ',left='+ leftPos + ',top=' + topPos);
        newWin.window.focus();     
    } 

</script>

<%
   String mode = (String)session.getAttribute("mode"+session.getId());
   String budgetStatusMode = (String)session.getAttribute("budgetStatusMode");
   boolean readOnly = false;
   if(mode!= null && mode.equals("display")){
       readOnly = true;
   }else if(budgetStatusMode != null && budgetStatusMode.equals("complete")){
        readOnly = true;
   }
    try{
    String editImage = request.getContextPath()+"/coeusliteimages/edit.gif";
    String deleteImage = request.getContextPath()+"/coeusliteimages/delete.gif";
    }catch(Exception ex){
        ex.printStackTrace();
    }
%>

<html:base/> 
</head>
<body onkeypress='dataChanged()'>

<html:form action="/getBudgetProjectIncome.do" onsubmit="called();">
<div id='projectIncome'>
<table width="100%" height="100%"  border="0" cellpadding="0" cellspacing="0" class="table" align='center'>


    <tr>
        <td align='left' class="copy">
        <font color='red'>
     
            <logic:messagesPresent>
                <script>
                    isError = '1';
                    errValue = true;
                </script>
                <html:errors header="" footer=""/>
            </logic:messagesPresent>
            <logic:messagesPresent message="true">
                <script>
                    isError = '1';
                    errValue = true;
                </script>
                <html:messages id="message" message="true" property="project.income.error.incomerequired" bundle="budget">
                            <li><bean:write name = "message"/></li>
                </html:messages>
                <html:messages id="message" message="true" property="project.income.error.descrequired" bundle="budget">
                            <li><bean:write name = "message"/></li>
                </html:messages>
               
                <html:messages id="message" message="true" property="project.income.error.invalidIncome" bundle="budget">
                            <li><bean:write name = "message"/></li>
                </html:messages>
                <html:messages id="message" message="true" property="projectIncome.error.maxAmount" bundle="budget">
                            <li><bean:write name = "message"/></li>
                </html:messages>
                <html:messages id="message" message="true" property="errMsg">
                    <script>errLock = true;</script>
                    <li><bean:write name = "message"/></li>
                </html:messages>               
            </logic:messagesPresent>
             </font>
        </td>
        
    </tr>
    <!-- Summary Section begin -->
    <tr>
        <td style='core'>
        <table width="100%" height="100%" align="left" border="0" cellpadding="2" cellspacing="2" class="tabtable">
        <tr>
            <td colspan='2' class='helptext'>
                 <div id="helpText">            
                <bean:message bundle="budget" key="helpTextBudget.Income"/>
                </div>
            </td>
        </tr
        <tr>
            <td colspan='2' class="tableheader">
                <bean:message bundle="budget" key="projectIncome.Summary"/>
            </td>
        </tr>
        
        <tr>
            <td width="10%" class="theader" align='left'>
                <bean:message bundle="budget" key="projectIncome.Period"/>
            </td>
            <td width="90%" class="theader" align='left'>
                <bean:message bundle="budget" key="projectIncome.Income"/>
            </td>            
        </tr>
        <%
            String strBgColor = "#D6DCE5";
            int count=0;
        %>
        <logic:iterate id="projectIncomeSummary" name="summary" type="org.apache.struts.action.DynaActionForm">
        <%
            if (count%2 == 0) {
                strBgColor = "#D6DCE5"; 
            }
            else { 
                strBgColor="#DCE5F1"; 
            }
        %>   
        <tr bgcolor="<%=strBgColor%>"> 
            <td class="copy" align='left'>
                <bean:write name="projectIncomeSummary" property="budgetPeriod"/>
            </td>
            <td class="copy" align='left'>
                 <bean:write name="projectIncomeSummary" property="strAmount"/>
            </td>            
        </tr>
        <tr>
            <td height=2>
            </td>
        </tr>   
        <% 
            count++;
        %> 
        </logic:iterate>           

        </table>
        </td>
    </tr>
    <tr>
        <td height='3'>
        </td>
    </tr> 
    <!-- Summary Section end -->
  
    <tr>
        <td height='3'>
        </td>
    </tr> 
    
    <!-- Details Section begin -->
    <tr>
        <td style='core'>
        <table width="100%" height="100%" align="left" border="0" cellpadding="2" cellspacing="2" class="tabtable" align='center'>
        <tr>
            <td colspan='5' class="tableheader">
                <bean:message bundle="budget" key="projectIncome.Details"/>
            </td>
        </tr>
        

        <tr>
            <td nowrap width='5%' align="left" class="theader">
                <bean:message bundle="budget" key="projectIncome.Period"/>
            </td>
            <td nowrap width='10%' align="left" class="theader">
                <bean:message bundle="budget" key="projectIncome.Income"/>
            </td>
            <td width='60%' align="left" class="theader">
                <bean:message bundle="budget" key="projectIncome.Description"/>
            </td>
            <td width="3%" class="theader" nowrap align="left">
            </td>
            <td width="3%" class="theader" nowrap align="left">
            </td>                    
        </tr>
        
        <logic:iterate id="dynaFormData" name="budgetProjectIncomeList" property="list" type="org.apache.struts.action.DynaActionForm" indexId="index">
        <%
         String amount = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(((Double)dynaFormData.get("amount")).doubleValue());
         String strAmount = (String)dynaFormData.get("strAmount");
         if(strAmount!=null && !strAmount.equals("")){
             String strTempAmount = strAmount;
             strAmount = strAmount.replaceAll("[$,/,]","");
             try{
             strAmount 
                    = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(new Double(strAmount));
             }catch(java.lang.NumberFormatException ne){
                 strAmount = strTempAmount;
             }
             amount = strAmount;
         }
        %>
        <tr>
            <td width="5%" class='copybold' align='center'>
             <logic:equal name="dynaFormData"  property="acType" value="I">
                 <html:select name="dynaFormData" property="budgetPeriod" indexed="true" onchange="dataChanged()" styleClass="clcombobox">
                    <html:options collection="budgetPeriods" property="budgetPeriod" labelProperty="budgetPeriod"/>
                </html:select>
             </logic:equal>
              <logic:notEqual name="dynaFormData"  property="acType" value="I">
                 <bean:write name="dynaFormData" property="budgetPeriod"/>
             </logic:notEqual>
            </td>
            <td width='15%'>
                <html:text name="dynaFormData" property="strAmount" disabled="<%=readOnly%>" indexed="true" value="<%=amount%>" style="text-align: right" styleClass="textbox"/>  
                <html:hidden name="dynaFormData" property="amount" indexed="true"/>  
            </td>
            <td width='15%'>
                <html:text name="dynaFormData" property="description" disabled="<%=readOnly%>" indexed="true" maxlength="2000" styleClass="textbox-long" style="width: 520px"/>  
            </td>
            <td width="10%" class='copybold' align='center'>
                <%String openDesciption="javaScript:openDescription('"+index+"');";%>
                <html:link href="<%=openDesciption%>">
                    <bean:message bundle="budget" key="projectIncome.Edit"/>
                </html:link>
            </td>
             <% 
                  String link = "javaScript:deleteProjectIncome('/deleteBudgetProjectIncome.do?rowId="+index+"')";
             %>
            <td width="10%" align='center' class='copybold'>
                <% if(!readOnly){%>
                <html:link href="<%=link%>">
                    <bean:message bundle="budget" key="projectIncome.Delete"/>
                </html:link>
                <%} else {%>
                    <bean:message bundle="budget" key="projectIncome.Delete"/>
                <%}%>
            </td>                    
        </tr>
        <tr height="1px">
            <td ></td>
        </tr>
        

        </logic:iterate>

          <tr class='copybold' align='left' width='100%'>
            <td colspan="5">
                <div id='open_Add'>
                <%if(!readOnly) {%>
                <%--<html:link href="javaScript:called('1');">
                    <u><bean:message bundle="budget" key="projectIncome.AddBudget"/></u>
                </html:link>--%>
                  <%     
                    java.util.Map params = new java.util.HashMap();
                    params.put("actionFrom", "actionFrom");
                    pageContext.setAttribute("paramsMap", params);
                  %>

                <html:link action="/getBudgetProjectIncome.do" name="paramsMap" scope="page" >
                    <u><bean:message bundle="budget" key="projectIncome.AddBudget"/></u>
                </html:link> 
                <%} else {%>
                    <bean:message bundle="budget" key="projectIncome.AddBudget"/>
                <%}%>
                </div>
                <div id='hide_Add' style='display: none;'>
                <html:link href="javaScript:called('2');">
                    <u><bean:message bundle="budget" key="projectIncome.Hide"/></u>
                </html:link>
                </div>        
            </td>
            </tr>
            <tr>
                <td>&nbsp;</td>
            </tr>
        </table>
        </td>
        </tr>
        <!-- Details Section end -->
        <tr>
            <td class='savebutton'>
                <logic:notEmpty name="budgetProjectIncomeList" property="list">
                    <html:button accesskey="s" property="Save" disabled="<%=readOnly%>" styleClass="clsavebutton" onclick="projectIncomeLink('S')">
                        <bean:message bundle="budget" key="budgetButton.save"/>
                    </html:button>
                </logic:notEmpty>
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

<script>
    if(isError=='1'){
        document.getElementById('open_window').style.display = 'block';
        document.getElementById('hide_Add').style.display = 'block';
        document.getElementById('open_Add').style.display = 'none';
    }
</script>
</html:form>

<script>
          DATA_CHANGED = 'false';
          var dataModified = '<%=request.getAttribute("dataModified")%>';
          if(dataModified == 'modified' || (errValue && !errLock)){
                DATA_CHANGED = 'true';
          }
          LINK = "<%=request.getContextPath()%>/saveBudgetProjectIncome.do";
          FORM_LINK = document.budgetProjectIncomeList;
          PAGE_NAME = "Project Income";
          function dataChanged(){
                DATA_CHANGED = 'true';
          }
          linkForward(errValue);
    </script>  
<script>
      var help = '<bean:message bundle="budget" key="helpTextBudget.Income"/>';
      help = trim(help);
      if(help.length == 0) {
        document.getElementById('helpText').style.display = 'none';
      }
      function trim(s) {
            return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
      }  
  </script>        
</body>
</html:html>




