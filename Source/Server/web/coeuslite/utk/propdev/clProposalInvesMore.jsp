<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList, java.util.List, java.util.HashMap"%>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<jsp:useBean  id="proposalPersonsDetails" scope="session" class="java.util.Vector"/>
<jsp:useBean id="personEditableColumns" scope="session" class="java.util.HashMap" />
<bean:size id="colSize" name ="personEditableColumns" />
<jsp:useBean  id="degreeTypes" scope="session" class="java.util.Vector"/>

<jsp:useBean id="validCountryStateCodes" scope="session" class="java.util.HashMap" />
<bean:size id="submissionSize" name ="validCountryStateCodes" />
<jsp:useBean id="getCountryTypes" scope="session" class="java.util.Vector" />
<jsp:useBean id="stateCodesData" scope="session" class="java.util.LinkedHashMap" />


<%

     String certifyRight = (String) session.getAttribute("CERTIFY_RIGHTS_EXIST");
     String invPersonId = (String) session.getAttribute("InvPersonId");
     String invPersonName = (String) session.getAttribute("InvPersonName");
     String piFlag = (String) session.getAttribute("InvPiFlag");
     String proposalNumber = (String) session.getAttribute("proposalNumber"+session.getId());
     String mode=(String)session.getAttribute("mode"+session.getId());
           boolean modeValue=false;
           if(mode!=null && !mode.equals("")) {
            if(mode.equalsIgnoreCase("display")){
                modeValue=true;
             }
            }
      boolean isKpUnitEnabled = true;

      if(session.getAttribute("isKpUnitEnabled") != null) {
          isKpUnitEnabled = (Boolean)session.getAttribute("isKpUnitEnabled");
      }

%>
<%String plus = request.getContextPath()+"/coeusliteimages/plus.gif";
  String minus = request.getContextPath()+"/coeusliteimages/minus.gif";%>
    <html:html>
    <bean:write name="validCountryStateCodes" property="key" />
    <head>
    <title> <bean:message bundle="proposal" key="moreInvPerson.personDetails"/>   </title>
    <link rel=stylesheet href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css" type="text/css">

    <script>

        var textValue = new Array();
        var index =0;
        var errValue = false;
        var errLock = false;
        var checkListsize = 0;
        <logic:iterate name="validCountryStateCodes" id="validSubmissionReview" >
        textValue[index++] = 'DES<bean:write name="validSubmissionReview" property="key" />';
        var DES<bean:write name="validSubmissionReview" property="key" />= new Array();  // it declares array variables initially
        var VAL<bean:write name="validSubmissionReview" property="key" />= new Array();// it declares array variables initially
        DES<bean:write name="validSubmissionReview" property="key" />.push('');  // appends value
        VAL<bean:write name="validSubmissionReview" property="key" />.push(' <bean:message bundle="proposal" key="generalInfoProposal.pleaseSelect"/> '); // appends value
              <bean:define id="reviewID" name="validSubmissionReview" property="value" type="java.util.Map"/>
              <logic:iterate id="sl" name="reviewID">
              <logic:present name="sl">
                    DES<bean:write name="validSubmissionReview" property="key" />.push('<bean:write name="sl" property="key" />');
                    VAL<bean:write name="validSubmissionReview" property="key" />.push('<bean:write name="sl" property="value" />');
              </logic:present>
              </logic:iterate>
        </logic:iterate>

        var desc1 ="";
        var val1 ="";
        function populateReviewType(form,selectValue,fieldName ){
              chosen=selectValue;
              var desc="DES"+chosen;
              var val="VAL"+chosen;
              val1 = val;
              desc1 = desc;
              dataChanged();
              var isPresent = false;
              for (count = 0 ; count < index ; count++) {
                if(desc == textValue[count]) {
                    isPresent = true;
                }
              }
               if(eval(form+'.'+fieldName)!=undefined){
                    select = eval(form+'.'+fieldName);
                          select.options.length = 0 ;

                                if(isPresent){
                                  for(i=0; i< eval(val).length; i++){
                                      select.options[i]=new Option(trim(eval(val)[i],eval(desc)[i]));
                                  }
                                } else {
                                  select.options[0]=new Option('<bean:message bundle="proposal" key="generalInfoProposal.pleaseSelect"/>','');
                                  select.options[0].selected=true;
                                }

                    }
        }

function trim(val) {
val = val.replace( /^\s*/, "" ).replace( /\s*$/, "" );
return val;
}





        function moveTab(link)
        {
                document.personDynaBeansList.action = "<%=request.getContextPath()%>/getPersonDetailsInfo.do?personTab="+link;
                document.personDynaBeansList.submit();
        }

     function close_action(){
       CLICKED_LINK = "<%=request.getContextPath()%>/getInvKeyPersons.do";
       if(validateSavedInfo()) {
            document.personDynaBeansList.action = "<%=request.getContextPath()%>/getInvKeyPersons.do";
            document.personDynaBeansList.submit();
        }

    }
     function certifyInvestigator(personId, proposalNum, personName) {
           CLICKED_LINK = "<%=request.getContextPath()%>/getPersonsCertify.do?personId="+personId+"&proposalNumber="+proposalNum+"&personName="+personName;
           if(validateSavedInfo()) {
               document.personDynaBeansList.action = "<%=request.getContextPath()%>/getPersonsCertify.do?personId="+personId+"&proposalNumber="+proposalNum+"&personName="+personName;
               document.personDynaBeansList.submit();
            }
    }

    function summaryHeader(idValue, value) {
        if(value==1) {
            document.getElementById(idValue).style.display = 'block';
            document.getElementById('hide_'+idValue).style.display = 'block';
            document.getElementById('show_'+idValue).style.display = 'none';
        } else if(value==2) {
            document.getElementById(idValue).style.display = 'none';
            document.getElementById('hide_'+idValue).style.display = 'none';
            document.getElementById('show_'+idValue).style.display = 'block';
        }
    }

    var selectedIndex ="";
    function findUnitDetails(index)
      {
      selectedIndex = index;
      var winleft = (screen.width - 550) / 2;
            var winUp = (screen.height - 350) / 2;
            var win = "scrollbars=yes, resizable=1,width=800,height=420,left="+winleft+",top="+winUp
            sList = window.open('<%=request.getContextPath()%>/generalProposalSearch.do?type=<bean:message key="searchWindow.title.unit" bundle="proposal"/>&search=true&searchName=w_unit_select', "list", win);

      }

       function addUnitDetails(actionName){
            if(actionName == "A"){
                document.personDynaBeansList.action = "<%=request.getContextPath()%>/addUnitDetails.do";
                document.personDynaBeansList.submit();
            }
       }

      function addDegreeInfo(actionName){
            if(actionName == "A"){
                document.personDynaBeansList.action = "<%=request.getContextPath()%>/addDegreeInfo.do";
                document.personDynaBeansList.submit();
            }
       }

       function fetch_Data(result){
        var unitNumber="";
        var flag="";
        var unitName="";
         dataChanged();
            flag = "N";
            if(result["UNIT_NUMBER"]!="null" && result["UNIT_NUMBER"]!= undefined){
                unitNumber = result["UNIT_NUMBER"];
            }
            if(result["UNIT_NAME"]!=null && result["UNIT_NAME"]!= undefined){
                unitName = result["UNIT_NAME"];
            }

            if(selectedIndex !=""){
                var modifiedValue = document.getElementsByName('dynaFormData['+selectedIndex+'].unitName');
                modifiedValue[0].value = unitName;
                modifiedValue = document.getElementsByName('dynaFormData['+selectedIndex+'].unitNumber');
                modifiedValue[0].value = unitNumber;
            }

      }

       function removeUnits(unitIndex) {
           document.personDynaBeansList.action = "<%=request.getContextPath()%>/removeUnitDetails.do?unitIndex="+unitIndex;
           document.personDynaBeansList.submit();
       }

       function saveAllDetails() {
           document.personDynaBeansList.action = "<%=request.getContextPath()%>/saveAllDetails.do";
           document.personDynaBeansList.submit();
       }
       function removeDegreeInfo(degreeIndex) {
           document.personDynaBeansList.action = "<%=request.getContextPath()%>/removeDegreeInfo.do?degreeIndex="+degreeIndex;
           document.personDynaBeansList.submit();
       }


    </script>

    </head>
    <body>
     <html:form  action="/getMoreDetails.do" method="post">
    <table border="0" cellpadding="0" cellspacing="5" width="100%" height="100%" class='table'>
    <tr valign='top'>
    <td>
    <table border="0" cellpadding="1" cellspacing="1" width="98%"  class='tabtable' >
    <tr> <td class='copybold' width='60%' align=left> Person Details for <%=invPersonName%>  </td>
    <td width='40%' class='copybold' align="right" >

            <html:link href="javascript:close_action();">
                <u><bean:message bundle="proposal" key="proposalInvCertify.returnToInvestigator"/></u>
            </html:link>


   </td>

    </tr>

    <tr class='copy' align="left">
            <td  colspan='2'>
                <font color="red" >
                <logic:messagesPresent>
                    <html:errors header="" footer=""/>
                    </logic:messagesPresent>

                    <logic:messagesPresent message="true">
                    <script>errValue = true;</script>
                    <html:messages id="message" message="true" property="errMsg">
                    <script>errLock = true;</script>
                    <li><bean:write name = "message"/></li>
                    </html:messages>
                    <html:messages id="message" message="true" property= "moreInvPerson.error.invalidUnitNo" bundle="proposal">
                   <li> <bean:write name = "message" /> </li>
                    </html:messages>
                    <html:messages id="message" message="true" property= "moreInvPerson.error.atleaseOneUnit" bundle="proposal">
                   <li> <bean:write name = "message" /> </li>
                    </html:messages>
                    <html:messages id="message" message="true" property= "moreInvPerson.error.unitRequired" bundle="proposal">
                    <li><bean:write name = "message" /></li>
                    </html:messages>
                    <html:messages id="message" message="true" property= "moreInvPerson.error.unitDuplicate" bundle="proposal">
                    <li><bean:write name = "message" /></li>
                    </html:messages>
                    <html:messages id="message" message="true" property= "moreInvPerson.error.gradDateRequired" bundle="proposal">
                    <li><bean:write name = "message" /></li>
                    </html:messages>

                    <html:messages id="message" message="true" property="moreInvPerson.error.invalidGradDate"  bundle="proposal">
                    <li><bean:write name = "message"/></li>
                    </html:messages>
                    <html:messages id="message" message="true" property="moreInvPerson.error.degreeCodeRequired"  bundle="proposal">
                    <li><bean:write name = "message"/></li>
                    </html:messages>
                    <html:messages id="message" message="true" property="moreInvPerson.error.degreeRequired"  bundle="proposal">
                    <li><bean:write name = "message"/></li>
                    </html:messages>
                     <html:messages id="message" message="true" property="moreInvPerson.error.degreeDuplicate"  bundle="proposal">
                    <li><bean:write name = "message"/></li>
                    </html:messages>


                </logic:messagesPresent>
                </font>
          </td>

      </tr>


 <%   if(piFlag !=null && (( piFlag.equals("Y") || piFlag.equals("N")) || (piFlag.equalsIgnoreCase("KSP") && isKpUnitEnabled))) { %>
 <tr valign='top'>
 <td colspan='2'>
  <table width="100%" border="0" cellpadding="0" cellspacing="0" class="table">


        <tr valign='top'>
             <td>
                 <table width='100%' border="0" align="left" cellpadding="0" cellspacing="0">
                    <tr>
                            <td class="tableheader" height='20'>
                                <div id='show_unitDetails' style='display: none;'>
                                    <html:link href="javaScript:summaryHeader('unitDetails','1');">
                                    <html:img src="<%=plus%>" border="0"/></html:link>&nbsp;
                                     <bean:message bundle="proposal" key="proposalInvKeyPersons.unitDetails"/>

                                </div>
                                <div id='hide_unitDetails'>
                                    <html:link href="javaScript:summaryHeader('unitDetails','2');">
                                    <html:img src="<%=minus%>" border="0"/></html:link>&nbsp;
                                     <bean:message bundle="proposal" key="proposalInvKeyPersons.unitDetails"/>
                                </div>
                            </td>
                    </tr>

                    <tr>
                    <td>
                        <div id='unitDetails'>
                            <table width="100%" border="0" cellpadding="1" cellspacing="1">

                                 <tr valign=top>
                                    <td>
                                     <table width="100%" align=center border="0" cellpadding="1" cellspacing='1' class="tabtable">
                                          <tr>
                                         <% if(piFlag !=null && ( piFlag.equals("Y"))) {  %>
                                            <td width="5%" align="left" class="theader"> <bean:message bundle="proposal" key="moreInvPerson.lead"/> </td>
                                         <% } %>
                                            <td width="15%" align="left" class="theader"> <bean:message bundle="proposal" key="moreInvPerson.number"/> </td>
                                            <td width="50%" align="left" class="theader"> <bean:message bundle="proposal" key="moreInvPerson.name"/>  </td>
                                            <td width="10%" align="left" class="theader"> &nbsp; </td>
                                             <td width="10%" align="left" class="theader"> &nbsp; </td>


                                          </tr>
                                        <logic:notEmpty name="personDynaBeansList"   property="list" scope="session" >
                                          <logic:iterate id="dynaFormData" name="personDynaBeansList" property="list" type="org.apache.struts.action.DynaActionForm" indexId="index">
                                          <%  String awAcType = (String) dynaFormData.get("awAcType");
                                              String leadUnitFlag = (String) dynaFormData.get("leadUnitFlag");
                                             if(awAcType == null ||  awAcType.equals("")){            %>
                                            <tr  valign="top" >
                                             <% if(piFlag !=null && ( piFlag.equals("Y"))) {  %>
                                                <td align="center" nowrap class="copy">
                                                   <%
                                                      if (leadUnitFlag !=null && leadUnitFlag.equals("Y")) { %>
                                                        <img src="<bean:write name='ctxtPath'/>/coeusliteimages/checked.gif">
                                                     <% } else { %>
                                                        <img src="<bean:write name='ctxtPath'/>/coeusliteimages/spacer.gif" width="11" height="11">
                                                     <% } %>
                                                   <%-- <html:checkbox property='leadUnitFlag' indexed="true"  name="dynaFormData" value="Y" disabled="<%=modeValue%>" onclick="dataChanged()"/>   --%>
                                                </td>
                                            <% } // end of piFlag %>
                                                <td align="left"nowrap class="copy">
                                                     <%
                                                      if (leadUnitFlag !=null && !leadUnitFlag.equals("Y")) { %>
                                                        <html:text property="unitNumber"  name="dynaFormData" styleClass="textbox" indexed="true" onkeypress="dataChanged();" disabled="<%=modeValue%>"  maxlength="10" />
                                                     <% }else{ %>
                                                       <html:text property="unitNumber"  name="dynaFormData" styleClass="textbox" indexed="true" readonly="true" disabled="<%=modeValue%>"  maxlength="10" />
                                                     <% }  %>
                                                </td>
                                                <td align="left" class="copy">
                                                    <%--   <bean:write property="unitName"  name="dynaFormData" /> --%>
                                                    <html:text property="unitName"  name="dynaFormData" styleClass="cltextbox-color"  indexed="true" style="width:400;"  size="300" readonly="true" disabled="<%=modeValue%>" maxlength="70" />
                                                </td>
                                                <td align="center" class="copy">
                                                 <%    String strfindUnitDetails ="javascript:findUnitDetails('"+index+"')";
                                                        if(!modeValue && leadUnitFlag !=null && !leadUnitFlag.equals("Y")){
                                                    %>
                                                    <html:link href="<%=strfindUnitDetails%>"> <bean:message bundle="proposal" key="moreInvPerson.FindUnit"/> </html:link>

                                                    <% }else{ %>
                                                      <bean:message bundle="proposal" key="moreInvPerson.FindUnit"/>
                                                    <%  }  %>
                                                </td>

                                                <td align="center" class="copy">
                                                <%     String unitNo = (String) dynaFormData.get("unitNumber");
                                                       String removeUnitDetails ="javascript:removeUnits('"+index+"')";
                                                        if(!modeValue && leadUnitFlag !=null && !leadUnitFlag.equals("Y")){
                                                    %>
                                                  <html:link href="<%=removeUnitDetails%>" > Remove </html:link>
                                                   <% }else { %>
                                                   <bean:message bundle="proposal" key="moreInvPerson.Remove"/>
                                                   <% }  %>
                                                </td>
                                          </tr>
                                          <% } /*end of If awAcType */ %>
                                        </logic:iterate>
                                     </logic:notEmpty>
                                    </table>
                                 </td>
                               </tr>

                                <tr valign=top>
                                    <td  class='copy' align='left'>
                                     <% if(!modeValue){
                                        %>
                                        <html:link href="javascript:addUnitDetails('A')" >
                                        <u><bean:message bundle="proposal" key="moreInvPerson.addUnit"/></u></html:link>
                                     <%} %>
                                    </td>
                                </tr>

                           </table>
                        </div>
                    </td>
                   </tr>


                </table>
            </td>
        </tr>
     </table>
   </td>
  </tr>
  <% } %>

 <!-- Unit Details End   -->


    <!--  Person Details Start   -->

 <tr valign='top'>
 <td colspan='2'>
  <table width="100%" border="0" cellpadding="1" cellspacing="1" class="table">
<tr valign='top'>
 <td>
 <table width='100%' border="0" align="left" cellpadding="0" cellspacing="0">
<tr>
        <td class="tableheader" height='10'>
            <div id='show_personDetails' style='display: none;'>
                <html:link href="javaScript:summaryHeader('personDetails','1');">
                <html:img src="<%=plus%>" border="0"/></html:link>&nbsp;<bean:message bundle="proposal" key="moreInvPerson.PersonDetails"/>
            </div>
            <div id='hide_personDetails'>
                <html:link href="javaScript:summaryHeader('personDetails','2');">
                <html:img src="<%=minus%>" border="0"/></html:link>&nbsp;<bean:message bundle="proposal" key="moreInvPerson.PersonDetails"/>
            </div>
        </td>
</tr>
<tr> <td height='10'> </td> </tr>



<tr>
    <td colspan='2'>
            <div id='personDetails'>
            <table width="100%" border="0" cellpadding="1" cellspacing="4" class="tabtable">
            <!--  Organization Tab  Start -->

               <logic:notEmpty name="personDynaBeansList"   property="infoList" scope="session" >
                     <logic:iterate id="dynaFormInfo" name="personDynaBeansList" property="infoList" type="org.apache.struts.action.DynaActionForm" indexId="index">

                     <tr>
                            <td>
                                 <tr>
                                        <td align='left' class='copybold' nowrap>
                                            <bean:message bundle="proposal" key="moreInvPerson.FullName"/>
                                        </td>
                                        <td class='copy'>
                                         <%  String fullNameValue = (String) personEditableColumns.get("fullName");
                                                if(fullNameValue !=null) {   %>
                                            <html:text property="fullName" style="width:200;" name="dynaFormInfo" styleClass="textbox" onkeypress="dataChanged();" onchange="dataChanged()" indexed="true" disabled="<%=modeValue%>" maxlength="90" />
                                             <% } else {%>
                                             <html:text property="fullName" style="width:200;" name="dynaFormInfo" styleClass="cltextbox-nonEditcolor" indexed="true" readonly="true" disabled="<%=modeValue%>" maxlength="90" />
                                              <% } %>
                                        </td>
                                        <td align="left" class='copybold' width='15%'><bean:message bundle="proposal" key="moreInvPerson.UserName"/> </td>
                                        <td width='30%' align="left">
                                            <%  String userNameValue = (String) personEditableColumns.get("userName");
                                                if(userNameValue !=null) {   %>
                                            <html:text property="userName" name="dynaFormInfo" style="width:200;" styleClass="textbox" indexed="true"  onkeypress="dataChanged();"  onchange="dataChanged()" disabled="<%=modeValue%>" maxlength="60"/>
                                                 <% } else {%>
                                                 <html:text property="userName" name="dynaFormInfo" style="width:200;" styleClass="cltextbox-nonEditcolor"  indexed="true" readonly="true" disabled="<%=modeValue%>" maxlength="60"/>
                                                 <% } %>
                                        </td>

                                   </tr>
                                   <tr>
                                        <td align="left" class='copybold' width='15%'><bean:message bundle="proposal" key="moreInvPerson.EmailAddress"/></td>
                                        <td width='30%' align="left">
                                          <%  String emailValue = (String) personEditableColumns.get("emailAddress");
                                                if(emailValue !=null) {   %>
                                                    <html:text property="emailAddress" name="dynaFormInfo" style="width:200;" styleClass="textbox" onkeypress="dataChanged();" onchange="dataChanged()" indexed="true" disabled="<%=modeValue%>" maxlength="60" />
                                              <% } else {%>
                                                <html:text property="emailAddress" name="dynaFormInfo" style="width:200;" styleClass="cltextbox-nonEditcolor" indexed="true" readonly="true" disabled="<%=modeValue%>"  maxlength="60" />
                                                 <% } %>
                                        </td>
                                        <td width='15%' align="left" class='copybold'> <bean:message bundle="proposal" key="moreInvPerson.OfficePhone"/>
                                        </td>
                                        <td width='30%' align="left">
                                           <%  String officePhoneValue = (String) personEditableColumns.get("officePhone");
                                                if(officePhoneValue !=null) {   %>
                                            <html:text property="officePhone" name="dynaFormInfo" style="width:200;" styleClass="textbox" onkeypress="dataChanged();" onchange="dataChanged()" indexed="true" disabled="<%=modeValue%>"  maxlength="20" />
                                             <% } else {%>
                                              <html:text property="officePhone" name="dynaFormInfo" style="width:200;" styleClass="cltextbox-nonEditcolor" indexed="true" readonly="true" disabled="<%=modeValue%>" maxlength="20" />
                                              <% } %>
                                        </td>
                                   </tr>

                                    <tr>
                                        <td align="left" class='copybold' width='10%'> <bean:message bundle="proposal" key="moreInvPerson.PrimaryTitle"/> </td>
                                        <td align="left">
                                           <%  String primaryTitleValue = (String) personEditableColumns.get("primaryTitle");
                                                if(primaryTitleValue !=null) {   %>
                                                    <html:text property="primaryTitle" name="dynaFormInfo" style="width:200;" styleClass="textbox" onkeypress="dataChanged();" onchange="dataChanged()" indexed="true" disabled="<%=modeValue%>" maxlength="51"/>
                                            <% } else {%>
                                                    <html:text property="primaryTitle" name="dynaFormInfo" style="width:200;" styleClass="cltextbox-nonEditcolor" indexed="true" readonly="true" disabled="<%=modeValue%>" maxlength="51"/>
                                               <% } %>
                                        </td>
                                        <td align="left" nowrap class='copybold'> <bean:message bundle="proposal" key="moreInvPerson.DirectoryTitle"/>
                                        </td>
                                        <td align="left">
                                         <%  String directoryTitleValue = (String) personEditableColumns.get("directoryTitle");
                                                if(directoryTitleValue !=null) {   %>
                                            <html:text property="directoryTitle" name="dynaFormInfo" style="width:200;" styleClass="textbox" onkeypress="dataChanged();" onchange="dataChanged()" indexed="true" disabled="<%=modeValue%>"  maxlength="50" />
                                          <% } else {%>
                                            <html:text property="directoryTitle" name="dynaFormInfo" style="width:200;" styleClass="cltextbox-nonEditcolor" indexed="true" readonly="true" disabled="<%=modeValue%>"  maxlength="50" />
                                          <% } %>
                                        </td>
                                   </tr>


                                   <tr>

                                        <td align="left" class='copybold' width='10%'> <bean:message bundle="proposal" key="moreInvPerson.HomeUnit"/>  </td>
                                        <td align="left">
                                         <%  String homeUnitValue = (String) personEditableColumns.get("homeUnit");
                                                if(homeUnitValue !=null) {   %>
                                            <html:text property="homeUnit" name="dynaFormInfo" style="width:200;" styleClass="textbox" onkeypress="dataChanged();" onchange="dataChanged()" indexed="true" disabled="<%=modeValue%>" maxlength="8"/>
                                        <% } else {%>
                                             <html:text property="homeUnit" name="dynaFormInfo" style="width:200;" styleClass="cltextbox-nonEditcolor" indexed="true" readonly="true" disabled="<%=modeValue%>" maxlength="8"/>
                                         <% } %>
                                        </td>
                                        <td align='left' class='copybold' nowrap>
                                            <bean:message bundle="proposal" key="moreInvPerson.School"/>
                                        </td>
                                        <td class='copy' >
                                         <%  String schoolValue = (String) personEditableColumns.get("school");
                                                if(schoolValue !=null) {   %>
                                            <html:text property="school" name="dynaFormInfo" style="width:200;" styleClass="textbox" indexed="true" onkeypress="dataChanged();" onchange="dataChanged()" disabled="<%=modeValue%>" maxlength="50" />
                                           <% } else {%>
                                           <html:text property="school" name="dynaFormInfo" style="width:200;" styleClass="cltextbox-nonEditcolor" indexed="true" readonly="true" disabled="<%=modeValue%>" maxlength="50" />
                                            <% } %>
                                        </td>

                                   </tr>
                                   <tr>
                                        <td align="left"  class='copybold' width='15%'> <bean:message bundle="proposal" key="moreInvPerson.EraCommName"/>  </td>
                                        <td width='30%' align="left">
                                         <%  String eraValue = (String) personEditableColumns.get("eraCommonsUserName");
                                                if(eraValue !=null) {   %>
                                            <html:text property="eraCommonsUserName" name="dynaFormInfo" style="width:200;" indexed="true" onkeypress="dataChanged();" onchange="dataChanged()" disabled="<%=modeValue%>" styleClass="textbox"  maxlength="20"/>
                                            <% } else {%>
                                             <html:text property="eraCommonsUserName" name="dynaFormInfo" style="width:200;" indexed="true" readonly="true" disabled="<%=modeValue%>" styleClass="cltextbox-nonEditcolor"  maxlength="20"/>
                                              <% } %>
                                        </td>
                                        <td width='15%' nowrap align="left" class='copybold'> <bean:message bundle="proposal" key="moreInvPerson.Fax"/>
                                        </td>
                                        <td width='30%' align="left">
                                         <%  String faxNumberValue = (String) personEditableColumns.get("faxNumber");
                                                if(faxNumberValue !=null) {   %>
                                            <html:text property="faxNumber" name="dynaFormInfo" style="width:200;" styleClass="textbox" onkeypress="dataChanged();" onchange="dataChanged()" indexed="true" disabled="<%=modeValue%>"  maxlength="20" />
                                            <% } else {%>
                                            <html:text property="faxNumber" name="dynaFormInfo" style="width:200;" styleClass="cltextbox-nonEditcolor" indexed="true" readonly="true" disabled="<%=modeValue%>" maxlength="20" />
                                             <% } %>
                                        </td>
                                   </tr>

                                   <tr>
                                        <td align="left" nowrap class='copybold' width='15%'> <bean:message bundle="proposal" key="moreInvPerson.Pager"/>    </td>
                                        <td width='30%' align="left">
                                         <%  String pagerNumberValue = (String) personEditableColumns.get("pagerNumber");
                                                if(pagerNumberValue !=null) {   %>
                                            <html:text property="pagerNumber" name="dynaFormInfo" style="width:200;" styleClass="textbox" onkeypress="dataChanged();" onchange="dataChanged()" indexed="true" disabled="<%=modeValue%>" maxlength="20"/>
                                          <% } else {%>
                                             <html:text property="pagerNumber" name="dynaFormInfo" style="width:200;" styleClass="cltextbox-nonEditcolor" indexed="true" readonly="true" disabled="<%=modeValue%>" maxlength="20"/>
                                            <% } %>
                                        </td>
                                        <td width='15%' nowrap align="left" class='copybold'> <bean:message bundle="proposal" key="moreInvPerson.Mobile"/>
                                        </td>
                                        <td width='30%' align="left">
                                         <%  String mobileNumberValue = (String) personEditableColumns.get("mobilePhoneNumber");
                                                if(mobileNumberValue !=null) {   %>
                                            <html:text property="mobilePhoneNumber" name="dynaFormInfo" style="width:200;" styleClass="textbox" onkeypress="dataChanged();" onchange="dataChanged()" indexed="true" disabled="<%=modeValue%>"  maxlength="10" />
                                           <% } else {%>
                                           <html:text property="mobilePhoneNumber" name="dynaFormInfo"  styleClass="cltextbox-nonEditcolor" indexed="true" readonly="true" disabled="<%=modeValue%>"  maxlength="10" />
                                           <% } %>
                                        </td>
                                   </tr>

                                   <tr>
                                       <td align="left" class='copybold'>  <bean:message bundle="proposal" key="moreInvPerson.OfficeLocation"/>
                                        </td>
                                        <td align="left">
                                         <%  String officeLocationValue = (String) personEditableColumns.get("officeLocation");
                                                if(officeLocationValue !=null) {   %>
                                            <html:text property="officeLocation" name="dynaFormInfo" style="width:200;" styleClass="textbox" onkeypress="dataChanged();" onchange="dataChanged()" indexed="true" disabled="<%=modeValue%>"  maxlength="30" />
                                          <% } else {%>
                                             <html:text property="officeLocation" name="dynaFormInfo" style="width:200;" styleClass="cltextbox-nonEditcolor" indexed="true" readonly="true" disabled="<%=modeValue%>"  maxlength="30" />
                                             <% } %>
                                        </td>
                                       <td align="left" class='copybold'> <bean:message bundle="proposal" key="moreInvPerson.SecOfficeLocation"/>
                                        </td>
                                        <td align="left">
                                         <%  String secOfficeLocationValue = (String) personEditableColumns.get("secondaryOfficeLocation");
                                                if(secOfficeLocationValue !=null) {   %>
                                            <html:text property="secondaryOfficeLocation" name="dynaFormInfo" style="width:200;" styleClass="textbox" onkeypress="dataChanged();" onchange="dataChanged()" indexed="true" disabled="<%=modeValue%>"  maxlength="30" />
                                             <% } else {%>
                                                <html:text property="secondaryOfficeLocation" name="dynaFormInfo" style="width:200;" styleClass="cltextbox-nonEditcolor" indexed="true" readonly="true" disabled="<%=modeValue%>"  maxlength="30" />
                                             <% } %>
                                        </td>
                                   </tr>


                                   <tr>
                                        <td align='left' class='copybold' nowrap>
                                            <bean:message bundle="proposal" key="moreInvPerson.AddressLine1"/>
                                        </td>
                                        <td class='copy'>
                                         <%  String addressLine1Value = (String) personEditableColumns.get("addressLine1");
                                                if(addressLine1Value !=null) {   %>
                                            <html:text property="addressLine1" name="dynaFormInfo" style="width:200;" indexed="true" styleClass="textbox"  onkeypress="dataChanged();" onchange="dataChanged()" disabled="<%=modeValue%>" maxlength="80" />
                                         <% } else {%>
                                             <html:text property="addressLine1" name="dynaFormInfo" style="width:200;" indexed="true" styleClass="cltextbox-nonEditcolor" readonly="true" disabled="<%=modeValue%>" maxlength="80" />
                                         <% } %>
                                        </td>
                                        <td align='left' class='copybold' nowrap>
                                           <bean:message bundle="proposal" key="moreInvPerson.AddressLine2"/>
                                        </td>
                                        <td class='copy'>
                                         <%  String addressLine2Value = (String) personEditableColumns.get("addressLine2");
                                                if(addressLine2Value !=null) {   %>
                                                <html:text property="addressLine2"  name="dynaFormInfo" style="width:200;" styleClass="textbox" indexed="true" onkeypress="dataChanged();" onchange="dataChanged()" disabled="<%=modeValue%>" maxlength="80" />
                                          <% } else {%>
                                                <html:text property="addressLine2"  name="dynaFormInfo" style="width:200;" styleClass="cltextbox-nonEditcolor" indexed="true" readonly="true" disabled="<%=modeValue%>" maxlength="80" />
                                           <% } %>
                                        </td>
                                   </tr>

                                   <tr>
                                        <td align='left' class='copybold' nowrap>
                                           <bean:message bundle="proposal" key="moreInvPerson.AddressLine3"/>
                                        </td>
                                        <td class='copy'>
                                          <%  String addressLine3Value = (String) personEditableColumns.get("addressLine3");
                                                if(addressLine3Value !=null) {   %>
                                            <html:text property="addressLine3" name="dynaFormInfo" style="width:200;"  styleClass="textbox" indexed="true" onkeypress="dataChanged();" onchange="dataChanged()" disabled="<%=modeValue%>" maxlength="80" />
                                             <% } else {%>
                                                <html:text property="addressLine3"  name="dynaFormInfo" style="width:200;" styleClass="cltextbox-nonEditcolor" indexed="true" readonly="true" disabled="<%=modeValue%>" maxlength="80" />
                                           <% } %>
                                        </td>
                                        <td align="left" nowrap class='copybold' width='15%'> <bean:message bundle="proposal" key="moreInvPerson.City"/> </td>
                                        <td width='30%' align="left">
                                        <%  String cityValue = (String) personEditableColumns.get("city");
                                                if(cityValue !=null) {   %>
                                            <html:text property="city" name="dynaFormInfo" style="width:200;" styleClass="textbox" indexed="true" onkeypress="dataChanged();" disabled="<%=modeValue%>" maxlength="30"/>
                                          <% } else {%>
                                             <html:text property="city" name="dynaFormInfo" style="width:200;" styleClass="cltextbox-nonEditcolor" indexed="true" readonly="true" disabled="<%=modeValue%>" maxlength="30"/>
                                           <% } %>
                                        </td>
                                   </tr>
                                   <tr>
                                        <td width='15%' nowrap align="left" class='copybold'> <bean:message bundle="proposal" key="moreInvPerson.County"/>
                                        </td>
                                        <td width='30%' align="left">
                                         <%  String countryValue = (String) personEditableColumns.get("county");
                                                if(countryValue !=null) {   %>
                                                <html:text property="county" name="dynaFormInfo" style="width:200;" styleClass="textbox" indexed="true" onkeypress="dataChanged();" onchange="dataChanged()" disabled="<%=modeValue%>"  maxlength="30" />
                                           <% } else {%>
                                                <html:text property="county" name="dynaFormInfo" style="width:200;" styleClass="cltextbox-nonEditcolor" indexed="true" readonly="true" disabled="<%=modeValue%>"  maxlength="30" />
                                            <% } %>

                                        </td>
                                         <td align="left" nowrap class='copybold' width='15%'> <bean:message bundle="proposal" key="moreInvPerson.State"/>  </td>
                                        <td width='30%' align="left">
                                          <%  String stateValue = (String) personEditableColumns.get("state");
                                                if(stateValue !=null) {   %>
                                                 <html:select  name="dynaFormInfo"  styleClass="textbox" style="width:200" property="state" onchange="dataChanged()"  disabled="<%=modeValue%>" >
                                                     <html:option value=""> <bean:message bundle="proposal" key="generalInfoProposal.pleaseSelect"/> </html:option>
                                                      <logic:notEmpty name="stateCodesData">
                                                            <html:options collection="stateCodesData" property="key" labelProperty="value" />
                                                      </logic:notEmpty>
                                                </html:select>
                                              <% } else {%>
                                                <html:select  name="dynaFormInfo"  styleClass="cltextbox-nonEditcolor" style="width:200" property="state" onchange="dataChanged()" disabled="true" >
                                                     <html:option value=""> <bean:message bundle="proposal" key="generalInfoProposal.pleaseSelect"/> </html:option>
                                                      <logic:notEmpty name="stateCodesData">
                                                            <html:options collection="stateCodesData" property="key" labelProperty="value" />
                                                      </logic:notEmpty>
                                                </html:select>
                                               <% } %>

                                          <%--  <html:select  name="dynaFormData" property="reviewType" styleClass="clcombobox-smallest" disabled="<%=modeValue%>" >
                                                  <html:option value="">
                                                        <bean:message key="protocolSubmission.pleaseSelect"/>
                                                  </html:option>

                                            </html:select>   --%>
                                        </td>
                                   </tr>
                                   <tr>
                                        <td width='15%' nowrap align="left" class='copybold'> <bean:message bundle="proposal" key="moreInvPerson.PostalCode"/>
                                        </td>
                                        <td width='30%' align="left">
                                         <%  String postalCodeValue = (String) personEditableColumns.get("postalCode");
                                                if(postalCodeValue !=null) {   %>
                                            <html:text property="postalCode" name="dynaFormInfo" style="width:200;" indexed="true" styleClass="textbox" onkeypress="dataChanged();" onchange="dataChanged()" disabled="<%=modeValue%>"  maxlength="10" />
                                          <% } else {%>
                                            <html:text property="postalCode" name="dynaFormInfo" style="width:200;" indexed="true"  styleClass="cltextbox-nonEditcolor" readonly="true" disabled="<%=modeValue%>"  maxlength="10" />
                                            <% } %>
                                        </td>
                                        <td align="left" nowrap class='copybold' width='15%'> <bean:message bundle="proposal" key="moreInvPerson.Country"/> </td>
                                        <td width='30%' align="left">
                                        <%--     <html:select  name="dynaFormInfo"  styleClass="textbox" style="width:200" indexed="true"  property="country" disabled="<%=modeValue%>" onchange="dataChanged()" >
                                                     <html:option value=""> <bean:message bundle="proposal" key="generalInfoProposal.pleaseSelect"/>  </html:option>
                                              </html:select>    --%>
                                           <%  String countryCodeValue = (String) personEditableColumns.get("country");
                                                if(countryCodeValue !=null) {   %>
                                            <html:select  name="dynaFormInfo" property="country" styleClass="textbox" style="width:200"  indexed="true" disabled="<%=modeValue%>" onchange="populateReviewType('personDynaBeansList',this.value,'state');">
                                                 <html:option value="">  <bean:message bundle="proposal" key="generalInfoProposal.pleaseSelect"/> </html:option>
                                                 <html:options collection="getCountryTypes" property="countryCode" labelProperty="country"/>
                                            </html:select>
                                             <% } else {%>
                                               <html:select  name="dynaFormInfo" property="country" styleClass="cltextbox-nonEditcolor" style="width:200" disabled="true" indexed="true" onchange="populateReviewType('personDynaBeansList',this.value,'state');">
                                                 <html:option value="">  <bean:message bundle="proposal" key="generalInfoProposal.pleaseSelect"/> </html:option>
                                                 <html:options collection="getCountryTypes" property="countryCode" labelProperty="country"/>
                                              </html:select>
                                              <% } %>
                                        </td>
                                   </tr>
                                   <!-- COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - Start -->
                                   <tr>
                                        <td width='15%' nowrap align="left" class='copybold'> <bean:message bundle="proposal" key="moreInvPerson.Division"/>
                                        </td>
                                        <td width='30%' align="left">
                                         <%  String division = (String) personEditableColumns.get("division");
                                                if(division !=null) {   %>
                                            <html:text property="division" name="dynaFormInfo" style="width:200;" indexed="true" styleClass="textbox" onkeypress="dataChanged();" onchange="dataChanged()" disabled="<%=modeValue%>"  maxlength="30" />
                                          <% } else {%>
                                            <html:text property="division" name="dynaFormInfo" style="width:200;" indexed="true"  styleClass="cltextbox-nonEditcolor" readonly="true" disabled="<%=modeValue%>"  maxlength="30" />
                                            <% } %>
                                        </td>
                                   </tr>
                                   <!-- COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - End -->
                           </td>
                    </tr>
                      </logic:iterate>
             </logic:notEmpty>
 <!--  End of Organization Tab   -->
    </table>
    </div>
    </td>
</tr>
 </table>
 </td>
 </tr>

 </table>
 </td>
 </tr>
     <!--  Person Details End -->

     <!-- Degree Info Details Start -->

<tr valign='top'>
 <td colspan='2'>
  <table width="100%" border="0" cellpadding="0" cellspacing="0" class="table">


      <tr>
        <td colspan='4' align="left" valign="top">
          <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableheader">
            <tr>
              <td>
                    <div id='show_degreeDetails' style='display: none;'>
                        <html:link href="javaScript:summaryHeader('degreeDetails','1');">
                        <html:img src="<%=plus%>" border="0"/></html:link>&nbsp; <bean:message bundle="proposal" key="moreInvPerson.Degrees"/>
                    </div>
                    <div id='hide_degreeDetails'>
                        <html:link href="javaScript:summaryHeader('degreeDetails','2');">
                        <html:img src="<%=minus%>" border="0"/></html:link>&nbsp; <bean:message bundle="proposal" key="moreInvPerson.Degrees"/>
                    </div>
              </td>
            </tr>
          </table>
        </td>
      </tr>
<tr>
       <td>
       <div id='degreeDetails'>
      <table width="100%" border="0" cellspacing='1' cellpadding="1">
      <tr>
          <td align="left" valign="top"><table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0" class="tabtable">

               <tr align="center">
                 <td>
                        <table width="100%" align=center border="0" cellpadding="1" cellspacing='1' class="tabtable">
                          <tr>
                            <td width="20%" align="left" class="theader"> <bean:message bundle="proposal" key="moreInvPerson.DegreeType"/> </td>
                            <td width="15%" align="left" class="theader"> <bean:message bundle="proposal" key="moreInvPerson.Degree"/> </td>
                            <td width="15%" align="left" class="theader"> <bean:message bundle="proposal" key="moreInvPerson.GraduationYear"/>  </td>
                            <td width="20%" align="left" class="theader"> <bean:message bundle="proposal" key="moreInvPerson.School"/>  </td>
                             <td width="10%" align="left" class="theader">&nbsp;</td>
                          </tr>

                          <% String strBgColor = "#DCE5F1"; //BGCOLOR=EFEFEF  FBF7F7
                              int count = 0;%>


                           <% if (count%2 == 0)
                                strBgColor = "#D6DCE5";
                             else
                                strBgColor="#DCE5F1";%>


                     <logic:notEmpty name="personDynaBeansList"   property="beanList" scope="session" >
                 <logic:iterate id="dynaFormBean" name="personDynaBeansList" property="beanList" type="org.apache.struts.action.DynaActionForm" indexId="index" scope="session">


                          <tr bgcolor="<%=strBgColor%>" valign="top">
                            <td align="left" nowrap class="copy">
                              <html:select  name="dynaFormBean"  styleClass="cltextbox-medium" property="degreeCode" indexed="true" disabled="<%=modeValue%>" onchange="dataChanged()">
                                     <html:option value=""> <bean:message bundle="proposal" key="generalInfoProposal.pleaseSelect"/> </html:option>
                                    <html:options  collection="degreeTypes" property="code"  labelProperty="description"/>
                              </html:select>

                            </td>
                            <td align="left"nowrap class="copy">
                                <html:text property="degree"  name="dynaFormBean" styleClass="textbox" indexed="true" onkeypress="dataChanged();" disabled="<%=modeValue%>"  maxlength="80" />
                            </td>
                            <td align="left" class="copy">
                            <%  String gradDate =  (String)dynaFormBean.get("graduationDate");
                                if(gradDate !=null && gradDate !="" && gradDate.length()>4) {
                                    gradDate = gradDate.substring(0,4);
                                    }
                                        if(gradDate !=null && gradDate !="" && gradDate.equals("0000")){
                                        gradDate = "";
                                        }
                                %>
                                <html:text property="graduationDate"  name="dynaFormBean" maxlength="4" size="4" value="<%=gradDate%>"  onkeypress="dataChanged();" onchange="dataChanged()" styleClass="textbox" indexed="true" disabled="<%=modeValue%>" />
                            </td>
                            <td align="left" class="copy">
                                <html:text property="school"  name="dynaFormBean" styleClass="textbox" onkeypress="dataChanged();" onchange="dataChanged()" style="width:150;" indexed="true" disabled="<%=modeValue%>"  maxlength="50" />
                             </td>
                             <td align="center" class="copy">
                                                <%
                                                       String removeDegreeDetails ="javascript:removeDegreeInfo('"+index+"')";
                                                        if(!modeValue){
                                                    %>
                                                  <u> <html:link href="<%=removeDegreeDetails%>" > <bean:message bundle="proposal" key="moreInvPerson.Remove"/>
                                                     </html:link>
                                                   <% }else { %>
                                                   <u><bean:message bundle="proposal" key="moreInvPerson.Remove"/></u>
                                                   <% }  %>
                            </td>



                          </tr>
                          <%count++;%>

                 </logic:iterate>
                     </logic:notEmpty>


                        </table>
                      </td>
                    </tr>
        </table></td>
      </tr>
          <tr valign=top>
            <td  class='copy' align='left'>
             <% if(!modeValue){ %>
                <html:link href="javascript:addDegreeInfo('A')" >
                <u>Add Degree</u></html:link>
             <% } %>
            </td>
       </tr>

  <!-- List of Investigators  -End -->
  </table>
  </div>
  </td>
  </tr>


</table>
</td>
</tr>

       <%if(!modeValue){%>
        <tr class='table' style='padding-left: 2px;'>
          <td colspan='2' nowrap class="copy" height='10' align="left" valign=middle>
            <html:button property="Save" value="Save"  onclick="javascript:saveAllDetails()" styleClass="clsavebutton"/>  &nbsp;&nbsp;
          </td>
        </tr>
         <%}%>

   <!-- Degree Info Details End -->

</table>

     </html:form>
    </body>

    <script>
      DATA_CHANGED = 'false';
      var dataModi = '<%=request.getAttribute("dataModified")%>';
      if(dataModi == 'modified' || (errValue && !errLock)){
            DATA_CHANGED = 'true';
      }
      LINK = "<%=request.getContextPath()%>/saveAllDetails.do";
      FORM_LINK = document.personDynaBeansList;
      PAGE_NAME = "<bean:message bundle="proposal" key="moreInvPerson.personDetails"/>";
      function dataChanged(){
            DATA_CHANGED = 'true';
      }
      linkForward(errValue);
  </script>
    </html:html>

