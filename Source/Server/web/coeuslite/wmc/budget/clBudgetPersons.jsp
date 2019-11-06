<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<%@ page import="java.util.Vector,java.sql.Date,java.text.SimpleDateFormat,
edu.mit.coeuslite.utils.ComboBoxBean, edu.mit.coeus.utils.DateUtils,
java.util.ArrayList,java.util.List"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld"  prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld"  prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"  prefix="coeusUtils"%>

<jsp:useBean  id="appointmentTypesData"  scope="session" class="java.util.Vector"/>

<html:html locale="true">
<head>

</head>
<title>Budget Persons</title>
<style>
    .cltextbox-color{ font-weight: normal; width: 220px}
    .textbox{ width: 40px; }  
    .mbox{background-color:#9CBFE9; padding:8px; border:2px outset #666;}
</style>
<% boolean valueChanges = false; 
   String mode = (String)session.getAttribute("mode"+session.getId());
   String budgetStatusMode = (String)session.getAttribute("budgetStatusMode");
   String enableSalaryAnnivDate = (String)session.getAttribute("ENABLE_SALARY_INFLATION_ANNIV_DATE");
   boolean readOnly = false;
   if(mode!= null && mode.equals("display")){
       readOnly = true;
   }else if(budgetStatusMode != null && budgetStatusMode.equals("complete")){
        readOnly = true;
   }
   Vector vecCEMessages = null;
   if(session.getAttribute("appointmentType_inactive_messages")!=null){
        vecCEMessages = (Vector) session.getAttribute("appointmentType_inactive_messages"); 
   }
   String editImage = request.getContextPath()+"/coeusliteimages/edit.gif";
   String delImage= request.getContextPath()+"/coeusliteimages/delete.gif";
   String spacerImage= request.getContextPath()+"/coeusliteimages/spacer.gif";
   //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
   Vector vecBudgetPeriods = (Vector)session.getAttribute("budgetPeriodData");
   int noOfPeriods = 0;
   if(vecBudgetPeriods != null){
       noOfPeriods = vecBudgetPeriods.size();
       //to display only ten periods
       if(noOfPeriods > 10){
           noOfPeriods = 10;
       }
   }
   //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
   
%>
<script language="JavaScript">
        var errValue = false;
        var errLock = false;
        var previousPersonId;
        function proposalBudget_Actions(actionName){
            var budgetPeriod = "<%=request.getAttribute("SelectedBudgetPeriodNumber")%>";
            if(actionName == "P"){
		var url = "<%=request.getContextPath()%>" + "/personSearch.do?type=budgetPersonSearch&search=true&searchName=ALL_PERSON_SEARCH";                
                sList=window.open(url,'welcome','resizable=1,scrollbars=1,width=800,height=450');
                if (parseInt(navigator.appVersion) >= 4) {
                window.sList.focus(); 
                }
            }else if(actionName == "A"){
                document.budgetPersonsDynaBean.action = "<%=request.getContextPath()%>/AddLineItem.do?AddBudgetPeriodRow="+budgetPeriod;
                document.budgetPersonsDynaBean.submit();
            }else if(actionName == "S"){
                document.budgetPersonsDynaBean.action = "<%=request.getContextPath()%>/setBudgetPersons.do";
                document.budgetPersonsDynaBean.submit();
            }else if(actionName == "R"){
		var url = "<%=request.getContextPath()%>" + "/personSearch.do?type=budgetRolodexSearch&search=true&searchName=ROLODEXSEARCH";                
                sList=window.open(url,'welcome','resizable=1,scrollbars=1,width=800,height=450');
                if (parseInt(navigator.appVersion) >= 4) {
                window.sList.focus(); 
                }
            }else if(actionName == "T"){
		var url = "<%=request.getContextPath()%>" + "/tbaSearch.do";                
                //sList=window.open(url,'welcome','resizable=1,scrollbars=1,width=770,height=450');
                
                var winleft = (screen.width - 650) / 2;
                var winUp = (screen.height - 450) / 2;
                var win = "scrollbars=1,resizable=1,width=770,height=450,left="+winleft+",top="+winUp;                  
                sList = window.open(url, "list", win);                 
                
                if (parseInt(navigator.appVersion) >= 4) {
                    window.sList.focus(); 
                }            
            }else if(actionName == "B"){
               
                if(DATA_CHANGED == 'true'){
                alert('<bean:message bundle="budget" key="persons.save.required" />');
                }else{
                document.budgetPersonsDynaBean.action = "<%=request.getContextPath()%>/calculateAllBaseSalary.do";
                document.budgetPersonsDynaBean.submit();
                }
            }
        }
        
        function removeLineItem(link){
            var msg = '<bean:message bundle="budget" key="budgetLabel.deleteConfirmation" />';
            if (confirm(msg)==true){
                document.budgetPersonsDynaBean.action = "<%=request.getContextPath()%>/"+link;
                document.budgetPersonsDynaBean.submit();
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

       
    function showBaseSalaryByPeriod(personId, period, elementId){
    
      if(DATA_CHANGED == 'true'){
          alert('<bean:message bundle="budget" key="persons.save.required" />');
       }else{
        var divId = "baseSalaryByPeriodId"+personId;
        var prevDivId = "baseSalaryByPeriodId"+previousPersonId;
        if(personId == previousPersonId){
            document.getElementById(divId).style.display="none";
        }else{           
            if(previousPersonId != null){
                document.getElementById(prevDivId).style.display="none";
            }
        }
        previousPersonId = personId;        
        document.getElementById(divId).style.display="";
        //to calculate the height based on number of periods
        var height = (period*20)+80;
        var width = 260;
        document.getElementById(divId).style.height = height;        
        document.getElementById(divId).style.width = width;
        
        elem = document.getElementById(elementId);
        var posY = getY(elem);
        var posX = getX(elem);
        //Change Positions if the Help Window Boundaries goes out of Browser Window Boundaries
        var browserWidth = getWindowWidth();
        var browserHeight = getWindowHeight();
        var helpTop = posY;
        var helpLeft = posX;
        if((posX+width) > (browserWidth-20)){
            helpLeft = posX - ((posX+width+width) - browserWidth);
        }
        if((posY+height) > browserHeight){
            helpTop = posY - ((posY+height) - browserHeight);
            //if help is way top. center it
            if((helpTop+height) < posY) helpTop = posY - (height/2);
        }
        document.getElementById(divId).style.top = helpTop;
        document.getElementById(divId).style.left = helpLeft;
        showMe(divId);
        
        }
    }
    
    function getY( oElement )
    {
        var iReturnValue = 0;
        while( oElement != null ) {
            iReturnValue += oElement.offsetTop;
            oElement = oElement.offsetParent;
        }
        return iReturnValue;
    }
    
    function getX( oElement )
    {
        var iReturnValue = 0;
        while( oElement != null ) {
            iReturnValue += oElement.offsetLeft;
            oElement = oElement.offsetParent;
        }
        return iReturnValue;
    }
    
    function showOnLoad(personId, period){
        var divId = "baseSalaryByPeriodId"+personId;
        var height = (period*20)+80;
        var width = 260;
        document.getElementById(divId).style.height = height;        
        document.getElementById(divId).style.width = width;
        var helpTop = 222;
        var helpLeft = 760;
        document.getElementById(divId).style.top = helpTop;
        document.getElementById(divId).style.left = helpLeft;
        document.getElementById(divId).style.display="";
    }
    
    function calculateBaseSalaryByPeriod(personid){        
        document.budgetPersonsDynaBean.action = "<%=request.getContextPath()%>/calculateBaseSalary.do?id="+personid;
        document.budgetPersonsDynaBean.submit();
    }

    function showMe(divId){
        document.getElementById(divId).style.display="";        
    }
       
    function getWindowWidth()
    {
        var x = 0;
        if (self.innerHeight)
        {
            x = self.innerWidth;
        }
        else if (document.documentElement && document.documentElement.clientHeight)
        {
            x = document.documentElement.clientWidth;
        }
        else if (document.body)
        {
            x = document.body.clientWidth;
        }
        return x;
    }

    function getWindowHeight()
    {
        var y = 0;
        if (self.innerHeight)
        {
            y = self.innerHeight;
        }
        else if (document.documentElement && document.documentElement.clientHeight)
        {
            y = document.documentElement.clientHeight;
        }
        else if (document.body)
        {
            y = document.body.clientHeight;
        }
        return y;
    }
    
    function disposeBaseSalaryByPeriod(divId){    
        document.getElementById(divId).style.display="none";
    }
    
    function saveBaseSalaryByPeriod(divId){        
        document.budgetPersonsDynaBean.action = "<%=request.getContextPath()%>/setBudgetPersons.do";
        document.budgetPersonsDynaBean.submit();
        document.getElementById(divId).style.display="none";
    }
    
    function validateData( propName, count, object){    
        var fieldCost = '0.00';
        var id = 'dynaFormData'+count+propName;
        var ValidChars = "0123456789.";
        var IsNumber = true;        
        fieldCost = object.value;
        if(fieldCost == "" && fieldCost.length == 0){
            fieldCost = '0.00';
        }             
        var strLen = fieldCost.length;
        for(var i=0;i<strLen;i++){
            fieldCost = fieldCost.replace(",",""); 
            fieldCost = fieldCost.replace("$",""); 
        }        
        if(fieldCost<0){
            object.value = '$'+'0.00';  
            fieldCost = object.value;
        }
        if(isNaN(fieldCost)){
            alert("Please Enter a Valid Number");
            object.value = '$'+'0.00';
            fieldCost = object.value;
            return;
        }                
        fieldCost = parseFloat(fieldCost);
        fieldCost = Math.round(fieldCost*Math.pow(10,2))/Math.pow(10,2); 
        if(fieldCost.toString().indexOf(".")== -1){
            fieldCost = '$'+fieldCost +'.00';
        }else{
            if(fieldCost.toString().length - fieldCost.toString().indexOf(".") == 2){
                fieldCost = '$'+fieldCost+'0';
            }else{
                fieldCost = '$'+fieldCost;
            }
        }
        document.getElementById(id).value = fieldCost;
        dataChanged();
    }
        
</script>
<html:base/>
<body onLoad='saveConfirm();showOnLoad("<%=request.getParameter("id")%>","<%=noOfPeriods%>");'>
<html:form action="/getBudgetPersons.do" method="POST">
<!-- New Templates for BudgetPersons Page   -->
<table width="100%" height="100%"  border="0" cellpadding="0" cellspacing="0" class="table">
<%if(!readOnly && "CHANGE_BUDGET_DATES".equals(request.getAttribute("CHANGE_BUDGET_DATES"))){%>
<script>
        if(confirm("<bean:message bundle="budget" key="adjustPeriod_exceptionCode.1465"/>")){
            document.budgetPersonsDynaBean.action = "<%=request.getContextPath()%>/getAdjustPeriodData.do?periodsChangeRequired=true";
            document.budgetPersonsDynaBean.submit();
        }
</script>
<%}%>
    <tr class='tableheader'>
        <td align='left'>      
              <bean:message bundle="budget" key="persons.label"/>
        </td>
    </tr>
    <tr>
        <td>
            <div id="helpText" class='helptext'>            
                <bean:message bundle="budget" key="helpTextBudget.Persons"/>  
            </div> 
        </td>
    </tr>
    <tr>
        <td class='copy' align="left" valign="top">
                <font color="red">
              <logic:messagesPresent>
                    <script>errValue = true; </script>
                    <html:errors header="" footer = ""/>
              </logic:messagesPresent>
              
              <logic:messagesPresent message="true">
                    <script>errValue = true; </script>
                    <html:messages id="message" message="true" property="budgetPersons_exceptionCode.1000" bundle="budget">                
                        <li><bean:write name = "message"/></li>
                    </html:messages>
                    <html:messages id="message" message="true" property="persons.baseSalary.invalid" bundle="budget">                
                        <li><bean:write name = "message"/></li>
                    </html:messages>
                    <html:messages id="message" message="true" property="persons.salaryAnnivDate.required" bundle="budget">                
                        <li><bean:write name = "message"/></li>
                    </html:messages>
                    <html:messages id="message" message="true" property="persons.remove.invalid" bundle="budget">                
                        <li><bean:write name = "message"/></li>
                    </html:messages>                    
                    <!-- If lock is deleted then show this message --> 
                    <html:messages id="message" message="true" property="errMsg"> 
                       <script>errLock = true;</script>
                       <li><bean:write name = "message"/></li>
                    </html:messages>
                    <!-- If lock is acquired by another user, then show this message -->
                    <html:messages id="message" message="true" property="acqLock">  
                       <script>errLock = true;</script>
                       <li><bean:write name = "message"/></li>
                    </html:messages>                                   	
                </logic:messagesPresent>   
                <!--Added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - start-->
                <%if(vecCEMessages != null && vecCEMessages.size() >0) {%>                                                
                <%for(int index = 0; index < vecCEMessages.size(); index++){%>
                <tr>
                    <td align = "left">
                        <font color="red">
                            <li> <%=vecCEMessages.elementAt(index)%> </li>
                        </font>
                    </td>
                    <td>
                        <br>
                        &nbsp;
                        <br>
                    </td>
                </tr>                                                
                <%}}
                session.setAttribute("appointmentType_inactive_messages",null);
                %>
                <!--Added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - end-->
            </font>
        </td>
    </tr>

    <tr>
        <td class='core'><br>
        <table width="100%" border="0" align="center" cellpadding="2" cellspacing="0" class="tabtable">


        <%double totalFunds = 0;%>
        <logic:present name ="budgetPersonsDynaBean" scope = "session"> 
            <tr class='copybold'>
                <td nowrap width='20%' align="left" class="theader">
                    &nbsp;<bean:message bundle="budget" key="persons.fullName"/>
                </td>
                <!-- COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start -->
                <!--<td nowrap width='10%' align="left" class="theader">-->
                <td nowrap width='8%' align="left" class="theader">
                <!-- COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End -->
                    <bean:message bundle="budget" key="persons.jobCode"/></td>
                <td width='15%'  align="left" class="theader">
                    <bean:message bundle="budget" key="persons.appointmentType"/>
                </td>
                 <!-- COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start -->
                <!--<td width='10%'  nowrap align="left" class="theader" >-->
                <td width='8%'  nowrap align="left" class="theader" >
                    <bean:message bundle="budget" key="persons.effectiveDate"/> 
                </td>
                <!--<td width='10%'  nowrap align="left" class="theader" >-->
                <td width='8%'  nowrap align="left" class="theader" >
                    <bean:message bundle="budget" key="persons.baseSalary"/> 
                </td>
                <!-- COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End -->
                <% if(enableSalaryAnnivDate !=null && enableSalaryAnnivDate.equals("1")){ %>
                <!-- COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start -->
                <!--<td nowrap width='10%' align="left" class="theader">-->
                <td width='8%'  nowrap align="left" class="theader" >
                <!-- COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End -->
                   <bean:message bundle="budget" key="persons.salaryAnnivDate"/>                    
                </td>                
                <% } %> 
                <!-- COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start -->
                <!--<td nowrap width='10%' align="left" class="theader">-->
                <td width='8%'  nowrap align="left" class="theader" >
                <!-- COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End -->
                     <% if(!readOnly){ %>
                    <bean:message bundle="budget" key="budget.Remove"/>
                    <%}%>
                </td>
                <!-- COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start -->
                <td width='25%' align="center" class="theader">
                    <bean:message bundle="budget" key="budget.BaseSalByPeriod"/>
                </td>
                <!-- COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End -->
            </tr> 
            <tr class='copybold'>
            <td height='10px'>
            </td>
            </tr>              
            <% int lineItemIndex = 0;%>
                            <logic:iterate id="dynaFormData" name="budgetPersonsDynaBean" property="list" type="org.apache.struts.action.DynaActionForm" indexId="ctr">
               <% 
               //Malini:4-4-2016
               	String fontClass="";
              
               	String isBudgetPersonActive = (String)dynaFormData.get("isBudgetPersonActive");
               	String isPersonExternal = (String)dynaFormData.get("isExternal");

               	
               	if (isBudgetPersonActive.equals("Y")&& isPersonExternal.equals("N")) {
					fontClass = "copy";
					
				}
               	else  if (isBudgetPersonActive.equals("N")&& isPersonExternal.equals("N") ) {
					fontClass = "copyInactivePerson";
					
				}  
               	else if (isBudgetPersonActive.equals("Y")&& isPersonExternal.equals("Y")){
               		fontClass = "copyExternalPerson";
               	}
               	
               	else if (isBudgetPersonActive.equals("N") && isPersonExternal.equals("Y")) {
					
               		fontClass = "copyInactivePerson";
				}

               %>
                <tr>                                                                        
                    <td align='left' >
                                     <html:text disabled="true" styleClass="<%=fontClass%>" name="dynaFormData" property= "personName" indexed="true" />
<%--                                      <%="DEBUG::"+isBudgetPersonActive + "-"+isPersonExternal+"="+fontClass%>
 --%>                    </td>
                    <td align='left' >
                                     <html:text styleClass="cltextbox-number" disabled="<%=readOnly%>" name= "dynaFormData" style="width:60" property= "jobCode" maxlength="6" indexed="true" onchange="dataChanged()"/>
                    </td>
                    <td align='left'>
                                    <%
                                    String strAppointmentType = (String)dynaFormData.get("appointmentType");                                    
                                    %>
                                    <html:select styleClass="combobox-long" style="width:125" disabled="<%=readOnly%>" name="dynaFormData" property="appointmentType" indexed="true" onchange="dataChanged()">
                                            <html:options collection="appointmentTypesData" property="description" labelProperty="description" />
                                            <!-- COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start -->
                                           <%if(strAppointmentType != null && !"".equals(strAppointmentType)){
                                            int repeat = 0;
                                            %>
                                            <logic:iterate id="appointmentTypeId" name="appointmentTypesData">  
                                                <logic:equal name="appointmentTypeId" property="code" value="<%=strAppointmentType%>">
                                                    <% repeat = repeat+1;%>
                                                </logic:equal>                                                                                    
                                            </logic:iterate>
                                            <%if(repeat==0){%>
                                                <html:option value="<%=strAppointmentType%>"> <bean:write name="dynaFormData" property="appointmentType"/> </html:option>
                                            <%}%>                                            
                                            <%}%>
                                            <!-- COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - End -->
                                    </html:select>
                    </td>         
                               <%-- <bean:define id="effDate" name= "list" property= "awEffectiveDate"/> --%>
                                <%  
                                java.sql.Date sqlDate = (java.sql.Date)dynaFormData.get("awEffectiveDate");
                                
                                DateUtils   dateUtils = new DateUtils();
                                String strEffectiveDate = "";
                                if(sqlDate != null && sqlDate.toString().length() > 0 ){
                                    strEffectiveDate = dateUtils.formatDate(sqlDate.toString(),"MM/dd/yyyy");
                                }
                                 %> 
                                    
                    <td align='left'>
                                     <html:text styleClass="cltextbox-number" disabled="<%=readOnly%>" name= "dynaFormData" style="width:70" property= "effectiveDate" value = "<%=strEffectiveDate%>" indexed="true" onchange="dataChanged()"/>
                    </td>  
                    <td align='left' >
                                    <%   
                                      //  System.out.println("dynaFormData >>>>>>>>"+dynaFormData);
                                        double calcBase = ((Double)dynaFormData.get("calculationBase")).doubleValue();
                                        String calBase = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(calcBase); 
                                       // System.out.println("CAL BASE DOUBLE >>>>>>>>"+calcBase);
                                        
                                        String strAmount = (String)dynaFormData.get("strCalculationBase");
                                         if(strAmount!=null && !strAmount.equals("")){
                                             String strTempAmount = strAmount;
                                             strAmount = strAmount.replaceAll("[$,/,]","");
                                             try{
                                             strAmount 
                                                    = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(new Double(strAmount));
                                             }catch(java.lang.NumberFormatException ne){
                                                 strAmount = strTempAmount;
                                             }
                                             calBase = strAmount;
                                         }
                                        
                                     %>  
                                     <html:text style="text-align: right" disabled="<%=readOnly%>" styleClass="cltextbox-number-medium" name= "dynaFormData" property= "strCalculationBase" value="<%=calBase%>" indexed="true" onchange="dataChanged()"/>
                                    <html:hidden name="dynaFormData" property="calculationBase" indexed="true"/>  
                    </td>
                   <%   String strAnnivDate = "";
                        if(dynaFormData.get("salaryAnniversaryDate") != null){                   
                             java.sql.Date sqlDate2 = (java.sql.Date)dynaFormData.get("salaryAnniversaryDate");                                                                                       
                                    if(sqlDate2 != null && sqlDate2.toString().length() > 0 ){
                                        strAnnivDate = dateUtils.formatDate(sqlDate2.toString(),"MM/dd/yyyy");                                   
                                    } 
                                  }  
                     if(enableSalaryAnnivDate !=null && enableSalaryAnnivDate.equals("1")){   %>
                     <td align='left'>
                                <html:text styleClass="cltextbox-number" disabled="<%=readOnly%>" name= "dynaFormData" style="width:70" property= "strSalaryAnniversaryDate" value = "<%=strAnnivDate%>" indexed="true" onchange="dataChanged()"/> 
                    </td>                 
                    <% } %>  
                    <td align = "left">
                    <% if(!readOnly){ %>
                       <a href="javaScript:removeLineItem('RemovePersonLineItem.do?rowIndex=<%=ctr%>')" class="copysmall">
                      <u><bean:message bundle="budget" key="budget.Remove"/></u>
                        <%--  <img border="0" src="../../../../coeusliteimages/trash.gif" alt="Remove <%=dynaFormData.get("personName")%> from Budget Persons">--%>  </a> 
                    <%} else {%>
                        <bean:message bundle="budget" key="budget.Remove"/>
                    <%}%>
                    </td>
                    <!-- COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start -->
                    <td align = "left"><!-- JM 3-17-2016 removed valign -->
                        <%String baseSalDivId = (String)dynaFormData.get("personId");                        
                        String onClickVal = "javaScript:showBaseSalaryByPeriod('"+baseSalDivId+"','"+noOfPeriods+"','showBaseSalaryLink')";                        
                        %>                       
                       <html:link href="<%=onClickVal%>" styleId="showBaseSalaryLink" style="text-decoration:underline">
                           <bean:message bundle="budget" key="budget.BaseSalByPeriod"/>
                       </html:link>
                    </td>
                    <!-- COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End -->
                 </tr> 
                <tr class='copybold'>
                <td height='10px'>
                </td>
                </tr>
              </logic:iterate>
        </logic:present>

        <tr class='copybold'>
        <td height='10'>
        </td>
        </tr>  


        <tr>
              <td colspan='6' class='copybold'>&nbsp;
                   <% if(!readOnly){ %>
                    <html:link href="javascript:proposalBudget_Actions('P')" >
                        <u><bean:message bundle="budget" key="budgetButton.addPerson"/></u>
                        
                    </html:link>
                   <%}else{%>
                        <bean:message bundle="budget" key="budgetButton.addPerson"/>
                        
                   <%}%>
                   &nbsp;&nbsp;|&nbsp;&nbsp;
                    <% if(!readOnly){ %>
                  <html:link href="javascript:proposalBudget_Actions('R')" >
                        <u><bean:message bundle="budget" key="budgetButton.addRolodex"/></u>
                  </html:link>
                  <%}else{%>
                        <bean:message bundle="budget" key="budgetButton.addRolodex"/>
                  <%}%>
                  <!-- JM 2-25-2016 removing Add TBA option
                  &nbsp;&nbsp;|&nbsp;&nbsp;
                  -->
                   <%-- if(!readOnly){ --%>
                   	<!-- 
                    <html:link href="<%-- javascript:proposalBudget_Actions('T') --%>" >
                        <u><bean:message bundle="budget" key="tba.label.add"/></u>                        
                    </html:link>
                    -->
                   <%--}else{--%>
                   		<!-- 
                        <bean:message bundle="budget" key="tba.label.add"/>
                        -->
                   <%--}--%> 
                     
                   <!-- COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start -->
                   &nbsp;&nbsp;|&nbsp;&nbsp;
                   <% if(!readOnly){ %>
                    <html:link href="javascript:proposalBudget_Actions('B')" >                        
                        <u><bean:message bundle="budget" key="persons.calculateBaseSal"/></u>                        
                    </html:link>
                   <%}else{%>
                        <bean:message bundle="budget" key="persons.calculateBaseSal"/>
                   <%}%>   
                   <!-- COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End -->
            </td>
        </tr>

        <tr class='copybold'>
        <td height='10'>
        </td>
        </tr>   

        </table>  
        <br>
        </td>
    </tr>
  
   <tr>
        <td class='savebutton'>&nbsp;&nbsp;
            <html:button accesskey="s" property="Save" styleClass="clsavebutton" onclick="proposalBudget_Actions('S')" disabled="<%=readOnly%>">
                <bean:message bundle="budget" key="budgetButton.save" />
            </html:button><br><br>
        </td>
   </tr>
    
</table>
<!-- COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start -->
    <logic:present name ="budgetPersonsDynaBean" scope = "session">
        <logic:iterate id="dynaFormData" name="budgetPersonsDynaBean" property="list" type="org.apache.struts.action.DynaActionForm" indexId="counter">
            <% String personId = (String)dynaFormData.get("personId");
            String baseSalId = "baseSalaryByPeriodId"+personId;%>
            <div id="<%=baseSalId%>" class="mbox" style="position:absolute;width:250;height:50;display:none;">
                <table>
                    <tr>
                        <td>
                            <font size="2" face="arial"><B>Base Salary by Period</B></font>
                            <font size="2" face="arial"><B></B></font>
                            <table cellpadding='0' cellspacing='0' border="1" align=left class="tabtable" width="250">
                                <!--<table border="2" cellpadding="0" cellspacing="0" class="lineBorderWhiteBackgrnd" width="300" height="225">-->
                                <tr class='copybold'>
                                    <td nowrap width='5%' align="left" class="theader">                    
                                        &nbsp;Period
                                    </td>
                                    <td nowrap width='8%' align="left" class="theader" >                            
                                        &nbsp;Base Salary
                                    </td>
                                </tr>
                                <% String strBgColor = "#D6DCE5";
                                for(int count=0;count<noOfPeriods;count++){
                                    if (count%2 == 0)
                                        strBgColor = "#D6DCE5";
                                    else
                                        strBgColor = "#DCE5F1";
                                %>
                                <tr class='copybold' bgcolor="<%=strBgColor%>" onmouseover="className='TableItemOn'" onmouseout="className='TableItemOff'">
                                    <td align='left'>
                                        <% int period = count+1;
                                        String propName = "basesalaryp"+(period);
                                        String strPropName = "strBasesalaryp"+(period);
                                        Double salBase = 0.00;
                                        String baseSalary = "0.00";                            
                                        %>
                                        &nbsp;<%=period%>
                                    </td>                                                                                      
                                    <td align='left' >
                                        <%   
                                        if(dynaFormData.get(propName) != null){
                                            salBase = ((Double)dynaFormData.get(propName)).doubleValue();
                                        }
                                        baseSalary = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(salBase);
                                        
                                        String strAmount = (String)dynaFormData.get(strPropName);
                                        if(strAmount!=null && !strAmount.equals("")){
                                            String strTempAmount = strAmount;
                                            strAmount = strAmount.replaceAll("[$,/,]","");
                                            try{
                                                strAmount
                                                        = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(new Double(strAmount));
                                            }catch(java.lang.NumberFormatException ne){
                                                strAmount = strTempAmount;
                                            }
                                            baseSalary = strAmount;
                                        }
                                        String baseSalDivId = (String)dynaFormData.get("personId");                        
                                        String onClickData = "javaScript:validateData('"+strPropName+"','"+personId+"',this)";
                                        String id = "dynaFormData"+personId+strPropName;
                                        %>
                                        <html:text style="text-align: right" disabled="<%=readOnly%>" styleClass="cltextbox-number-medium" name= "dynaFormData" size="13" maxlength="13" styleId="<%=id%>" property= "<%=strPropName%>" indexed="true" value="<%=baseSalary%>" onchange="<%=onClickData%>"/>
                                        <html:hidden name="dynaFormData" property="<%=propName%>" indexed="true"/>
                                    </td>
                                </tr>
                                <%}%>                    
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td>                
                            <table>                    
                                <tr>                        
                                    <td>
                                        <%String onClickSave = "javaScript:saveBaseSalaryByPeriod('"+baseSalId+"')";%>
                                        <html:button property="Save" styleClass="clsavebutton" style="width:70" onclick="<%=onClickSave%>" disabled="<%=readOnly%>">
                                            <bean:message bundle="budget" key="budgetButton.save" />
                                        </html:button>                            
                                    </td>
                                    <td>
                                        <%String onClickCalculate = "javaScript:calculateBaseSalaryByPeriod('"+personId+"')";%>
                                        <html:button property="Calculate" styleClass="clsavebutton" style="width:90" onclick="<%=onClickCalculate%>" disabled="<%=readOnly%>">
                                            <bean:message bundle="budget" key="budgetButton.calculate" />
                                        </html:button>
                                    </td>
                                    <td>
                                        <%String onClickClose = "javaScript:disposeBaseSalaryByPeriod('"+baseSalId+"')";%>
                                        <html:button property="Close" styleClass="clsavebutton" style="width:70" onclick="<%=onClickClose%>">
                                            <bean:message bundle="budget" key="budgetButton.close" />
                                        </html:button>                        
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </div>
        </logic:iterate>
    </logic:present>
    <!-- COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End -->
</html:form>
    <script>
          DATA_CHANGED = 'false';
          var dataModified = '<%=request.getAttribute("dataModified")%>';
          if(dataModified == 'modified' || (errValue && !errLock)){
                DATA_CHANGED = 'true';
          }
          LINK = "<%=request.getContextPath()%>/setBudgetPersons.do";
          FORM_LINK = document.budgetPersonsDynaBean;
          PAGE_NAME = "<bean:message bundle="budget" key="persons.label"/>";
          function dataChanged(){
                DATA_CHANGED = 'true';
          }
          linkForward(errValue);
    </script>
    <script>
      var help = '<bean:message bundle="budget" key="helpTextBudget.Persons"/>';
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


