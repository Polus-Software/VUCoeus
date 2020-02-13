<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.Vector,org.apache.struts.validator.DynaValidatorForm"%>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<%Vector vecPropCreditSplit = (Vector) session.getAttribute("vecPropCreditSplit");
  Vector vecPropUnitCreditSplit = (Vector) session.getAttribute("vecPropUnitCreditSplit");
  Vector vecPersonUnitDetails = (Vector) session.getAttribute("vecPersonUnitDetails");
  String insertValue = (String)request.getAttribute("insertValue");
  boolean insertFlag = true;
  if(insertValue!=null){
      insertFlag = false;
  }
  String mode=(String)session.getAttribute("mode"+session.getId());
  boolean modeValue=false;
  if(mode!=null && !mode.equals("")) {   
      if(mode.equalsIgnoreCase("display")){
              modeValue=true;
      }
  }%>
<html>
<head>
<title>JSP Page</title>
<style>
    .textbox { width: 85px; }
    .cltextbox-color { width: 85px; font-weight: bold; border: 0px solid #D1E5FF;}
</style>
<script>
    var personIds = new Array();
    var creditSplit = new Array();
    var personUnits = new Array()
    var personIndex = 0;
    var dataCount = 0;
    var personUnitsCount = 0;
    var errValue = false;
    var errLock = false;
    
    function trim(s) {
        return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
    }     
    function calculateInves(){
        for(countValue=0 ; countValue < dataCount ; countValue++) {
            var total = 0;
            for(index=0 ; index < personIds.length ; index++) {
                var element = document.getElementById(personIds[index]+"-"+creditSplit[countValue]);
                var data = roundValue(element.value);
                if(!isNaN(data))
                    element.value = data;
                total += (data*1);
            }
            var element = document.getElementById("personTotal-"+creditSplit[countValue]);
            if(isNaN(total)){
                element.value = '0.00';
            } else {
                element.value = roundValue(total+'');
                if(total == '0'){
                    element.value = '0.00';
                }
            }
        }
    }
    
    function roundValue(value){
        var data = '';
        if(value.indexOf(".")!=-1){
            for(indexValue=0 ; indexValue <= (value.indexOf(".")+2) ; indexValue++) {
                data += value.charAt(indexValue);
            }
            if((value.indexOf(".")+2) == value.length)
                data +='0';
            if((value.indexOf(".")+2) == (value.length+1))
                data +='00';
        } else {
            data = value+'.00';
        }
        if(data == '.00')
            data = '0.00';
        return data;
    }
    
    function validFormat(val){
        var dataValue = '';
        val = Number(val);
        if(val.indexOf(".")!=-1){
            if((val.indexOf(".")+2) == val.length)
                dataValue +='0';
            if((val.indexOf(".")+2) == (val.length+1))
                dataValue +='00';
        } else {
            dataValue = val+'.00';
        }
        return dataValue;
    }
    
    function unitCalculate(unit, value, field, seq) {
        data = Number(0.00);
        for(ind=0 ; ind <= personUnits[seq]; ind++){
            fieldProperty = unit+"-"+value+ind;
            unitValue = trim(document.getElementById(fieldProperty).value);
            temp = roundValue(unitValue);
            data += Number(temp);
            document.getElementById(fieldProperty).value = temp;
        }
        if(field != -1){
            unitValue = trim(document.getElementById(unit+"-"+value+field).value);
            unitValue = roundValue(unitValue);
            if(!isNaN(unitValue)){
                document.getElementById(unit+"-"+value+field).value = unitValue;
            } else {
                document.getElementById(unit+"-"+value+field).value = '0.00';
            }
        }
        //var unitValue = document.getElementById(unit+"-"+value).value;
        //var data = roundValue(unitValue);
        if(!isNaN(data)){
            //document.getElementById(unit+"-"+value).value = unitValue;
            data = String(data);
            document.getElementById(unit+"-unitTotal-"+value).value = roundValue(data);
        } else {
            document.getElementById(unit+"-unitTotal-"+value).value = '0.00';
        }
        
    }
</script>

</head>
<body>
    <html:form action="/saveCreditSplitInfo.do"> 
    <table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%" class='table'>
    <tr>
        <td>
            <table width="100%" height="100%" align=center border="0" cellpadding="0" cellspacing="0" class="tableheader">
            <tr>
                <td>
                    <bean:message bundle="proposal" key="label.creditSplit"/>:                 
                </td>            
            </tr>
            </table>          
        </td>
    </tr>
      <tr>
          <td class="copy">
            <div id="helpText" class='helptext'>&nbsp;&nbsp;            
                <bean:message bundle="proposal" key="helpTextProposal.CreditSplit"/>  
            </div>           
          </td>
      </tr>    
    <tr class='tabtable'>
        <td class='copy'>
            <font color='red'>
                <logic:messagesPresent message="true">
                <script>errValue = true; </script>
                <html:messages id="message" message="true" property="unitNeedToFill" bundle="proposal">                
                    <li><bean:write name = "message"/></li>
                 </html:messages>
                <html:messages id="message" message="true" property="investigatorNeedToFill" bundle="proposal">                
                    <li><bean:write name = "message"/></li>
                 </html:messages>                 
                <html:messages id="message" message="true" property="errMsg" bundle="proposal">
                    <script>errLock = true; </script>
                   <li><bean:write name = "message"/></li>
                </html:messages> 
                <html:messages id="message" message="true" property="notValidNumber" bundle="proposal">
                   <li><bean:write name = "message"/></li>
                </html:messages>
                <html:messages id="message" message="true" property="numberLimitExceeds" bundle="proposal">
                   <li><bean:write name = "message"/></li>
                </html:messages>               
               </logic:messagesPresent>            
            </font>
        </td>
    </tr>
    <%if(vecPersonUnitDetails!=null && vecPersonUnitDetails.size()>0){
        String strBgColor = "#DCE5F1";%>     
    <tr>
        <td>
        <table border="0" cellpadding="0" cellspacing="0" width="100%" class='tabtable'>
        <tr class='theader'>
            <td>
            
            </td>
            <logic:iterate id="creditType" name="vecInvCreditType" scope="session" type="org.apache.struts.validator.DynaValidatorForm">
            <td>
                <script>
                    creditSplit[dataCount] = '<bean:write name="creditType" property="description"/>';
                    dataCount++;
                </script>
                <bean:write name="creditType" property="description"/>
            </td>
            </logic:iterate>            
        </tr>
        <logic:iterate id="personDetails" name="vecPersonUnitDetails" indexId="indexed" scope="session" type="org.apache.struts.validator.DynaValidatorForm">
        <%String unitTotal = "";
          String person = (String)personDetails.get("personId");
          String unitNumber = (String) personDetails.get("unitNumber");%>
                          <% 
                             if (indexed.intValue()%2 == 0) 
                                strBgColor = "#D6DCE5"; 
                             else 
                                strBgColor="#DCE5F1"; 
                           %>            
        <tr bgcolor="#ACACAC">
            <td class='copybold'>
                <%String pi = "";
                  if(personDetails.get("principalInvestigator")!=null && personDetails.get("principalInvestigator").equals("Y")){
                    pi = " (PI)";
                  }%>

                <bean:write name="personDetails" property="personName"/><%=pi%>
                
                <script>
                        personIds[personIndex] = '<bean:write name="personDetails" property="personId"/>';
                        personIndex++;
                </script>
            </td>
            <logic:iterate id="creditType" name="vecInvCreditType" indexId="index" scope="session" type="org.apache.struts.validator.DynaValidatorForm">
            <td>
                <%
                  String name = person+"-"+(String)creditType.get("description");
                  String value = "";
                  if(vecPropCreditSplit!=null && vecPropCreditSplit.size()>0){
                    for(int count = 0 ; count < vecPropCreditSplit.size() ; count++){
                        DynaValidatorForm form = (DynaValidatorForm) vecPropCreditSplit.get(count);
                        if(form!=null){
                            if(person.equals(form.get("personId")) && 
                                        form.get("unitNumber").equals(creditType.get("unitNumber")) &&
                                        form.get("invCreditTypeCode").equals(creditType.get("invCreditTypeCode"))) {
                                value = form.get("credit")==null?"":form.get("credit").toString();
                            }
                        }
                    }
                  }
                  if(value == null || value.equals("")){
                      value = "0.00";
                  }
                  if(!insertFlag){
                    value = request.getParameter(name);
                  }
                  if(!modeValue){%>
                    <input type=text style="text-align: right" id='<%=name%>' name='<%=name%>' class='textbox' value='<%=value%>' maxlength='6' onblur='javascript: calculateInves();' onchange="dataChanged()">

                 <%}else{%>
                    <input type=text readonly style="text-align: right" id='<%=name%>' class='textbox' name='<%=name%>' value='<%=value%>' maxlength='6' onblur='calculateInves();'>
                 <%}%>
            </td>
            </logic:iterate>
        </tr>
        <%int unitCount = -1;%>
        <logic:iterate id="creditUnitType" name="vecUnitCreditSplit" scope="session" type="org.apache.struts.validator.DynaValidatorForm">
        <%if(creditUnitType.get("personId")!=null && creditUnitType.get("personId").equals(person)){%>
        <tr bgcolor="#D6DCE5">
            <td class='copybold'>
                <bean:write name="creditUnitType" property="unitNumber"/> - <bean:write name="creditUnitType" property="unitName"/>
            </td>
            <%  unitCount++;
                unitNumber = (String) creditUnitType.get("unitNumber");
            %>
            <logic:iterate id="creditType" name="vecInvCreditType" indexId="index" scope="session" type="org.apache.struts.validator.DynaValidatorForm">
            <td>
                <%
                  String name = person+"-"+(String)creditType.get("description")+unitCount;
                  String value = "";
                  if(vecPropUnitCreditSplit!=null && vecPropUnitCreditSplit.size()>0){
                    for(int count = 0 ; count < vecPropUnitCreditSplit.size() ; count++){
                        DynaValidatorForm form = (DynaValidatorForm) vecPropUnitCreditSplit.get(count);
                        if(form!=null){
                            if(person.equals(form.get("personId")) && 
                                        form.get("invCreditTypeCode").equals(creditType.get("invCreditTypeCode")) &&
                                        form.get("unitNumber").equals(unitNumber)) {
                                value = form.get("credit")==null?"":form.get("credit").toString();
                            }
                        }
                    }
                  }
                  String click = "javaScript: unitCalculate('"+person+"','"+(String)creditType.get("description")+"','"+unitCount+"','"+indexed+"')";
                  if(value == null || value.equals("")){
                      value = "0.00";
                  }
                   if(!insertFlag){
                    value = request.getParameter(name);
                   }
                  if(!modeValue){%>                  
                    <input type=text style="text-align: right" id='<%=name%>' name='<%=name%>' class='textbox' maxlength='6' value='<%=value%>' onblur="<%=click%>" onchange="dataChanged()">
                 <%}else{%>
                    <input type=text readonly style="text-align: right" id='<%=name%>' class='textbox' name='<%=name%>' maxlength='6' value='<%=value%>' onblur="<%=click%>">
                 <%}%>
                 <%unitTotal = value;%>
            </td>
            </logic:iterate>
        </tr>
        <%}%>
         </logic:iterate>
         <script>
            personUnits[personUnitsCount++] = <%=unitCount%>;
         </script>
        <tr bgcolor="#DCE5F1" class='copybold'>
            <td>
                <bean:message bundle="proposal" key="creditSplit.unitTotal"/>
            </td>
            <logic:iterate id="creditType" name="vecInvCreditType" indexId="index" scope="session" type="org.apache.struts.validator.DynaValidatorForm">        
            <td>
                <%String name = (String)person+"-unitTotal-"+(String)creditType.get("description");
                  String value = "";
                  if(vecPropUnitCreditSplit!=null && vecPropUnitCreditSplit.size()>0){
                    for(int count = 0 ; count < vecPropUnitCreditSplit.size() ; count++){
                        DynaValidatorForm form = (DynaValidatorForm) vecPropUnitCreditSplit.get(count);
                        if(form!=null){
                            if(person.equals(form.get("personId")) && 
                                        form.get("invCreditTypeCode").equals(creditType.get("invCreditTypeCode"))) {
                                value = form.get("credit")==null?"":form.get("credit").toString();
                            }
                        }
                    }
                  }
                  String click = "javaScript: unitCalculate('"+person+"','"+(String)creditType.get("description")+"','-1','"+indexed+"')";
                  if(value == null || value.equals("")){
                      value = "0.00";
                  }
                   if(!insertFlag){
                    value = request.getParameter(name);
                   }%>                   
                    <input type=text align=right id='<%=name%>' style="background-color: #DCE5F1; text-align: right" class='cltextbox-color' readonly="true" maxlength='6' name='<%=name%>' value='<%=value%>'>
                    <script>
                        <%=click%>
                    </script>
            </td>
            </logic:iterate>
        </tr>
        <tr>
            <td height='5'>
            </td>
        </tr>        
        </logic:iterate>
        <tr class='copybold' bgcolor="#ACACAC">
            <td>
                <bean:message bundle="proposal" key="creditSplit.investigatorTotal"/>
            </td>
            <logic:iterate id="creditType" name="vecInvCreditType" indexId="index" scope="session" type="org.apache.struts.validator.DynaValidatorForm">        
            <td>
                <%String name = "personTotal-"+(String)creditType.get("description");
                  String value = "";
                   if(!insertFlag){
                    value = request.getParameter(name);
                   }%>                   
                <input type=text readonly="<%=modeValue%>" class='cltextbox-color' style="background-color: #ACACAC; text-align: right" id='<%=name%>' name='<%=name%>' maxlength='6' value='<%=value%>' readonly="true" >                
            </td>
            </logic:iterate>
        </tr>
        </table>        
        </td>
     </tr>
        <%if(!modeValue){%>
        <tr class='table' style='padding-left: 2px;'>
          <td nowrap class="copy" height='25' align="left" valign=middle>
            <html:submit property="Save" value="Save" styleClass="clsavebutton"/>
          </td>   
        </tr>
         <%}%>
     <%} else {%>
    <tr class='tabtable'>
        <td class='copybold' align=center>
            <bean:message bundle="proposal" key="creditSplit.noInvestigatorFound"/>
        </td>
    </tr>
     <%}%>
    </table>
    </html:form>
</body>
<script>calculateInves();</script>
<script>
      DATA_CHANGED = 'false';
      if(errValue && !errLock) {
            DATA_CHANGED = 'true';
      }
      LINK = "<%=request.getContextPath()%>/saveCreditSplitInfo.do";
      FORM_LINK = document.proposalCreditSplitForm; 
      PAGE_NAME = "<bean:message bundle="proposal" key="label.creditSplit"/>";      
      function dataChanged(){
            DATA_CHANGED = 'true';
      }
      linkForward(errValue);
</script>
<script>
      var help = '<bean:message bundle="proposal" key="helpTextProposal.CreditSplit"/>';
      help = trim(help);
      if(help.length == 0) {
            document.getElementById('helpText').style.display = 'none';
      }
</script> 
</html>
