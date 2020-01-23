
<%--
    Document   : addTravels
    Created on : Mar 20, 2012, 11:59:17 AM
    Author     : indhulekha
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ page
	import="java.util.Vector,edu.mit.coeuslite.coiv2.beans.CoiEventTypeAttrBean,edu.mit.coeuslite.coiv2.beans.CoiProjectEntityDetailsBean,edu.mit.coeuslite.coiv2.services.CoiCommonService,java.util.HashMap;"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>COI</title>
<%String path = request.getContextPath();%>
<link href="<%=path%>/coeuslite/dartmouth/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css" />
<link
	href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/layout.css"
	rel="stylesheet" type="text/css" />
<link
	href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/styles.css"
	rel="stylesheet" type="text/css" />
<link
	href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/collapsemenu.css"
	rel="stylesheet" type="text/css" />

<%
          boolean mode = false;
        Boolean modeValue = (Boolean)request.getAttribute("flag");
            boolean edit=Boolean.parseBoolean(request.getParameter("edit"));
             request.setAttribute("edit", edit);

          if(edit == true){
              mode = true;
              }
      CoiEventTypeAttrBean fieldBean = null;
        HashMap inputFields = new HashMap();

        int shrtTxtField_count = 0;
        int lngTxtField_count = 0;
        int dateField_count = 0;
        int SlctField_count = 0;
        int nmbrField_count = 0;

        if(request.getAttribute("travelInputFields") != null) {
            inputFields = (HashMap) request.getAttribute("travelInputFields");
            request.setAttribute("travelField", inputFields);
         }

        if(request.getAttribute("shrtTxtCount") != null) {
            shrtTxtField_count = Integer.parseInt(request.getAttribute("shrtTxtCount").toString());
        }
        if(request.getAttribute("lngTxtCount") != null) {
            lngTxtField_count = Integer.parseInt(request.getAttribute("lngTxtCount").toString());
        }
        if(request.getAttribute("nmrFldCount") != null) {
            nmbrField_count = Integer.parseInt(request.getAttribute("nmrFldCount").toString());
        }
        if(request.getAttribute("dateCount") != null) {
            dateField_count = Integer.parseInt(request.getAttribute("dateCount").toString());
        }
        if(request.getAttribute("selectCount") != null) {
            SlctField_count = Integer.parseInt(request.getAttribute("selectCount").toString());
        }

       String SHRT_TXT_FIELD = "USE_SHRT_TXT_FLD";
         String LNG_TXT_FIELD = "USE_LNG_TXT_FLD";
         String DATE_FIELD = "USE_DATE_FLD";
         String NBR_FIELD = "USE_NMBR_FLD";
         String SLCT_FIELD = "USE_SLCT_BOX";

         String REQ_SHRT_TXT_FIELD = "REQ_SHRT_TXT_FLD";
         String REQ_LNG_TXT_FIELD = "REQ_LNG_TXT_FLD";
         String REQ_DATE_FIELD = "REQ_DATE_FLD";
         String REQ_NBR_FIELD = "REQ_NMBR_FLD";
         String REQ_SLCT_FIELD = "REQ_SLCT_BOX";

         String SHRT_TXT_FIELD_LBL = "SHRT_TXT_FLD";
         String LNG_TXT_FIELD_LBL = "LNG_TXT_FLD";
         String DATE_FIELD_LBL = "DATE_FLD";
         String NBR_FIELD_LBL = "NMBR_FLD";
         String SLCT_FIELD_LBL = "SLCT_BOX";


             String create= request.getParameter("create");
            String operationType = (String) request.getAttribute("operation");
             String target = "#";

          target = path + "/coiCommonSave.do?";
           String eventName="";
            String sponsor="";
            String destination="";
            String purpose="";
            Double amount=0.0;
             String url ="";
             String url1 ="";
             String startDate="";
             String endDate="";
              HashMap travelUserDet = null;

           if(session.getAttribute("travelList") != null){
             travelUserDet=(HashMap)session.getAttribute("travelList");
              }
                if(travelUserDet !=null){

              url = path + "/coiInProgress.do?discl="+ request.getAttribute("disclosureNumber")+"&seq="+ request.getAttribute("sequenceNumber")+"&personId="+ request.getAttribute("personId")+"&param6=throughShowAllDiscl&projectID="+request.getParameter("projectID")+"&eventType=Travel&moduleCode=0";

              url1 = path +"/getannualdisclosure.do?&param1=" + request.getAttribute("disclosureNumber") + "&param2=" +request.getAttribute("sequenceNumber") +  "&param7=" + request.getAttribute("personId")+ "&projectID="+request.getParameter("selectedPjct")+"&param5=Travel&param6=throughShowAllDiscl&fromReview="+request.getParameter("fromReview");
              session.setAttribute("param2", request.getAttribute("sequenceNumber"));
              session.setAttribute("param1", request.getAttribute("disclosureNumber"));
              session.setAttribute("param3", request.getAttribute("personId"));
                }
        %>

<script language="javascript" type="text/javascript">

        function CheckNumeric (e)
        {
            var key;
            key = e.which ? e.which : e.keyCode;
            if (key!=8 && key!=9 && key!=46){
                if((key>=48 && key<=57 )) {
                    e.returnValue= true;
                }
                else
                {
                    if(key!=45  && key!=92 && key!= 47 && key!=37 && key!=38 && key!=39 && key!=40){
                        alert("please enter Numeric only.select a valid date from date picker");
                        e.returnValue = false;
                    }
                }
            }
       }

     function isValidDate(dateStr,dateProp)
       {
           debugger;
           var strProperty=dateProp;
           dateStr =fomatToMMDDYY(dateStr);

           <%
                String dateLbl1 = "";
                String dateFld1 = "";

                 if(dateField_count > 0){
                    String useDateFld = "";
                    String dateTxtLabel  = "";

                    for(int i=1; i <= dateField_count; i++ ){
                        useDateFld = DATE_FIELD+"_"+i;
                        if(inputFields.containsKey(useDateFld) && inputFields.get(useDateFld).toString().equalsIgnoreCase("Y")) {
                            dateTxtLabel = DATE_FIELD_LBL+"_"+i+"_LABEL";

                            if(inputFields.get(dateTxtLabel) != null) {
                                dateLbl1 = inputFields.get(dateTxtLabel).toString();
                                dateFld1 = dateLbl1.replaceAll(" ", "_");
                               %>
                                 if(strProperty=='<%=dateFld1%>'){
                                    if(document.forms[0].<%=dateFld1%> != null){
                                    document.forms[0].<%=dateFld1%>.value='';
                                     document.forms[0].<%=dateFld1%>.value=dateStr;
                                   }
                                   var datePat = /^(\d{1,2})(\/|-)(\d{1,2})\2(\d{2}|\d{4})$/;
                                    if(dateStr!=''){
                                    var matchArray = dateStr.match(datePat); // is the format ok?
                                    if (matchArray == null)
                                    {
                                        alert("Invalid date format. Please enter the date in the MM/DD/YY format (example: 1/15/08) or select a date by clicking the calendar icon.")
                                        if(strProperty=='<%=dateFld1%>'){
                                            //alert("entering since true");
                                            if(document.forms[0].<%=dateFld1%> != null){
                                            document.forms[0].<%=dateFld1%>.value='';
                                            document.forms[0].<%=dateFld1%>.focus();
                                            }
                                            //document.forms[0].coiProjectStartDate.focus();
                                        }
                                        //ctrl.focus();
                                        return false;
                                    }
                                    month = matchArray[1]; // parse date into variables
                                    day = matchArray[3];
                                    year = matchArray[4];
                                    if (month < 1 || month > 12)
                                    {
                                    // check month range
                                        alert("Month must be between 1 and 12.");
                                        return false;
                                    }
                                    if (day < 1 || day > 31)
                                    {
                                        alert("Day must be between 1 and 31.");
                                        return false;
                                    }
                                    if ((month==4 || month==6 || month==9 || month==11) && day==31)
                                    {
                                        alert("Month "+month+" doesn't have 31 days!")
                                        return false
                                    }
                                    if (month == 2)
                                    {
                                    // check for february 29th
                                        var isleap = (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));
                                        if (day>29 || (day==29 && !isleap))
                                        {
                                        alert("February " + year + " doesn't have " + day + " days!");
                                        return false;
                                        }
                                    }

                                    }
                          <%}
                }
              }
           } %>

      return true; // date is valid
    }
    function fomatToMMDDYY(datestr){
    debugger;
    //alert(datestr);
    var strlen=datestr.length;
    //alert(strlen);
    for(var i=0;i<=strlen;i++){
        datestr=datestr.replace("-","/")
   }
   // alert("datestr")
    for(var i=0;i<=strlen;i++){
        datestr=datestr.replace("\\","/")
    }
    return datestr;
}


   function maxNumber(txt,maxLen){
        try{
            if(txt.value.length > (maxLen-1)) {
            return false;
            }
           }
           catch(e){
           }
       return true;
    }

    </script>
</head>

<script language="javascript" type="text/JavaScript"
	src="<%=request.getContextPath()%>/coeuslite/mit/utils/scripts/coeusLiteCalendar.js"></script>

<html:form action="/createDisclosureCoiv2.do" method="post">

	<table class="table" style="height: auto;" border="0"
		<% if(travelUserDet == null){%>
		<tr><td colspan="4">Please add a new Travel Event to create a new COI disclosure</td></tr>
        <%}%>
   
         <tr style="height: 3px;"><td colspan="4"></td></tr>
        <%
            String label = "";
            String fieldId = "";
        %>
        <%/*if(shrtTxtField_count > 0){*/
            String useShtFld = "";
            String srtTxtLabel  = "";
            String fieldValue = "";
            String editValue = "";
          /*  for(int i=1; i <= shrtTxtField_count; i++ ){*/
                useShtFld = SHRT_TXT_FIELD+"_"+1;
                if(inputFields.containsKey(useShtFld) && inputFields.get(useShtFld).toString().equalsIgnoreCase("Y")) {
                    srtTxtLabel = SHRT_TXT_FIELD_LBL+"_"+1+"_LABEL";
                    editValue = SHRT_TXT_FIELD_LBL+"_"+1;
                    if(inputFields.get(srtTxtLabel) != null) {
                        label = inputFields.get(srtTxtLabel).toString();
                        fieldId = label.replaceAll(" ", "_");
                        if(travelUserDet != null && travelUserDet.containsKey(editValue)){
                            fieldValue = (String)travelUserDet.get(editValue);
                        }

                %>
		<tr bgcolor="#D1E5FF">
                    <td style="width: 25%;color: #333333;font-family:Arial,Helvetica,sans-serif;font-size:12px;font-weight:bold;text-align:inherit;padding-left: 3px;padding-top: 5px;">
                       <%=label%>:
                    </td>
                    <td style="padding-top: 5px;" colspan="3">
                        <% if(edit) { %>
                        <input type="text" name="<%=fieldId%>" id="<%=fieldId%>" disabled="<%=mode%>" maxlength="20" value="<%=fieldValue%>"class="textbox" />
                        <% } else {%>
                        <input type="text" name="<%=fieldId%>" id="<%=fieldId%>" maxlength="20"  value="<%=fieldValue%>"class="textbox" />
                        <% }%>
                    </td>
                </tr>

                  <%   }
                }
          /*   }
        }*/%>
        

     <%if(lngTxtField_count > 0){
            String useLngFld = "";
            String lngTxtLabel  = "";
             fieldValue = "";
             editValue = "";
            for(int i=1; i <= lngTxtField_count; i++ ){
                useLngFld = LNG_TXT_FIELD+"_"+i;
                if(inputFields.containsKey(useLngFld) && inputFields.get(useLngFld).toString().equalsIgnoreCase("Y")) {
                    lngTxtLabel = LNG_TXT_FIELD_LBL+"_"+i+"_LABEL";
                    editValue = LNG_TXT_FIELD_LBL+"_"+i;

                    if(inputFields.get(lngTxtLabel) != null) {
                        label = inputFields.get(lngTxtLabel).toString();
                        fieldId = label.replaceAll(" ", "_");
                        if(travelUserDet != null && travelUserDet.containsKey(editValue)){
                            fieldValue = (String)travelUserDet.get(editValue);
                        }
                %>
		<tr bgcolor="#D1E5FF">
                    <td style="width: 25%;color: #333333;font-family:Arial,Helvetica,sans-serif;font-size:12px;font-weight:bold;text-align:inherit;padding-left: 3px;padding-top: 3px;">
                       <%=label%>:
                    </td>
                    <td style="padding-top: 3px;" colspan="3">
                         <% if(edit) { %>
                        <textarea style="width: 95%!important;" onkeypress="return maxNumber(this,201)" rows="2" cols="70" name="<%=fieldId%>" id="<%=fieldId%>"  class="textbox-longer" disabled="<%=mode%>"><%=fieldValue%></textarea>
                        <% } else {%>
                         <textarea style="width: 95%!important;" onkeypress="return maxNumber(this,201)" rows="2" cols="70" name="<%=fieldId%>" id="<%=fieldId%>"  class="textbox-longer"><%=fieldValue%></textarea>
                    <%}%>
                    </td>

                </tr>

                  <%   }
                }
             }
        }%>
        

       <%if(nmbrField_count > 0){
            String useNmbFld = "";
            String NmTxtLabel  = "";
            fieldValue = "";
            editValue = "";

            for(int i=1; i <= lngTxtField_count; i++ ){
                useNmbFld = NBR_FIELD+"_"+i;
                if(inputFields.containsKey(useNmbFld) && inputFields.get(useNmbFld).toString().equalsIgnoreCase("Y")) {
                    NmTxtLabel = NBR_FIELD_LBL+"_"+i+"_LABEL";
                    editValue = NBR_FIELD_LBL+"_"+i;

                    if(inputFields.get(NmTxtLabel) != null) {
                        label = inputFields.get(NmTxtLabel).toString();
                        String lb = label.replaceAll("\\(", "");
                         String lbl1 = lb.replaceAll("\\)", "");
                        fieldId = lbl1.replaceAll(" ", "_");
                         if(travelUserDet != null && travelUserDet.containsKey(editValue)){
                            fieldValue = travelUserDet.get(editValue).toString();
                        }
                %>
		<tr bgcolor="#D1E5FF">
                            <td style="width: 25%;color: #333333;font-family:Arial,Helvetica,sans-serif;font-size:12px;font-weight:bold;text-align:inherit;padding-left: 3px;padding-top: 3px;">
                               <%=label%>:
                            </td>
                            <td style="width:180px!important;padding-top: 3px;">
                                 <% if(edit) { %>
                                <input type="text" name="<%=fieldId%>" id="<%=fieldId%>" disabled="<%=mode%>" onkeypress="CheckFund(event)"  value="<%=fieldValue%>" class="textbox"/>
                                 <% } else { %>
                                 <input type="text" name="<%=fieldId%>" id="<%=fieldId%>" onkeypress="CheckFund(event)"  value="<%=fieldValue%>" class="textbox"/>
                                 <% } %>
                            </td>

                            <%
                             useShtFld = "";
                             srtTxtLabel  = "";
                             fieldValue = "";
                             editValue = "";

                                useShtFld = SHRT_TXT_FIELD+"_"+2;
                                if(inputFields.containsKey(useShtFld) && inputFields.get(useShtFld).toString().equalsIgnoreCase("Y")) {
                                    srtTxtLabel = SHRT_TXT_FIELD_LBL+"_"+2+"_LABEL";
                                    editValue = SHRT_TXT_FIELD_LBL+"_"+2;
                                    if(inputFields.get(srtTxtLabel) != null) {
                                        label = inputFields.get(srtTxtLabel).toString();
                                        fieldId = label.replaceAll(" ", "_");
                                        if(travelUserDet != null && travelUserDet.containsKey(editValue)){
                                            fieldValue = (String)travelUserDet.get(editValue);
                                        }
                            %>
                             <td style="width: 25%;color: #333333;font-family:Arial,Helvetica,sans-serif;font-size:12px;font-weight:bold;text-align:inherit;padding-left: 3px;padding-top: 5px;">
                               <%=label%>:
                            </td>
                            <td style="padding-top: 5px;">
                                <% if(edit) { %>
                                <input type="text" name="<%=fieldId%>" id="<%=fieldId%>" disabled="<%=mode%>" maxlength="20" value="<%=fieldValue%>"class="textbox" />
                                <% } else {%>
                                <input type="text" name="<%=fieldId%>" id="<%=fieldId%>" maxlength="20"  value="<%=fieldValue%>"class="textbox" />
                                <% }%>
                            </td>
                           <%}}%>

                        </tr>
                  <%   }
                }
             }
        }%>

        

      <%/*if(dateField_count > 0){*/
            String useDateFld = "";
            String dateTxtLabel  = "";
             fieldValue = "";
             editValue = "";

          /*  for(int i=1; i <= dateField_count; i++ ){*/
                useDateFld = DATE_FIELD+"_"+1;
                if(inputFields.containsKey(useDateFld) && inputFields.get(useDateFld).toString().equalsIgnoreCase("Y")) {
                    dateTxtLabel = DATE_FIELD_LBL+"_"+1+"_LABEL";
                    editValue = DATE_FIELD_LBL+"_"+1;

                    if(inputFields.get(dateTxtLabel) != null) {
                        label = inputFields.get(dateTxtLabel).toString();
                        fieldId = label.replaceAll(" ", "_");
                        if(travelUserDet != null && travelUserDet.containsKey(editValue)){
                            fieldValue = (String)travelUserDet.get(editValue);
                        }
                %>
		<tr bgcolor="#D1E5FF">
                            <td style="width: 25%;color: #333333;font-family:Arial,Helvetica,sans-serif;font-size:12px;font-weight:bold;text-align:inherit;padding-left: 3px;padding-top: 3px;">
                               <%=label%>:
                            </td>
                            <td style="padding-top: 3px;">
                                <% if(edit) { %>
                                <input type="text" name="<%=fieldId%>" id="<%=fieldId%>" value="<%=fieldValue%>" maxlength="10" disabled="<%=mode%>"  onkeypress="CheckNumeric(event)" onblur="isValidDate(document.forms[0].<%=fieldId%>.value,'<%=fieldId%>')" class="textbox" />
                                          <a id="hlIRBDate"  tabindex="32" href="javascript:void(0);" >
                                              <img id="imgIRBDate" title="" height="16" alt=""  src="<%=request.getContextPath()%>/coeusliteimages/cal.gif" width="16" border="0" />
                                           </a>
                                           <% } else {%>
                                            <input type="text" name="<%=fieldId%>" id="<%=fieldId%>" value="<%=fieldValue%>" maxlength="10"   onkeypress="CheckNumeric(event)" onblur="isValidDate(document.forms[0].<%=fieldId%>.value,'<%=fieldId%>')" class="textbox" />
                                          <a id="hlIRBDate" onclick="displayCalendarWithTopLeft('<%=fieldId%>',8,25)" tabindex="32" href="javascript:void(0);" >
                                              <img id="imgIRBDate" title="" height="16" alt=""  src="<%=request.getContextPath()%>/coeusliteimages/cal.gif" width="16" border="0" />
                                           </a>
                                         <%}%>
                            </td>

                            <%
                                 useDateFld = "";
                                 dateTxtLabel  = "";
                                 fieldValue = "";
                                 editValue = "";
                                 useDateFld = DATE_FIELD+"_"+2;
                                    if(inputFields.containsKey(useDateFld) && inputFields.get(useDateFld).toString().equalsIgnoreCase("Y")) {
                                        dateTxtLabel = DATE_FIELD_LBL+"_"+2+"_LABEL";
                                        editValue = DATE_FIELD_LBL+"_"+2;

                                        if(inputFields.get(dateTxtLabel) != null) {
                                            label = inputFields.get(dateTxtLabel).toString();
                                            fieldId = label.replaceAll(" ", "_");
                                            if(travelUserDet != null && travelUserDet.containsKey(editValue)){
                                                fieldValue = (String)travelUserDet.get(editValue);
                                            }
                                         
                            %>

                            <td style="width: 25%;color: #333333;font-family:Arial,Helvetica,sans-serif;font-size:12px;font-weight:bold;text-align:inherit;padding-left: 3px;padding-top: 3px;">
                               <%=label%>:
                            </td>
                            <td style="padding-top: 3px;">
                                <% if(edit) { %>
                                <input type="text" name="<%=fieldId%>" id="<%=fieldId%>" value="<%=fieldValue%>" maxlength="10" disabled="<%=mode%>"  onkeypress="CheckNumeric(event)" onblur="isValidDate(document.forms[0].<%=fieldId%>.value,'<%=fieldId%>')" class="textbox" />
                                          <a id="hlIRBDate"  tabindex="32" href="javascript:void(0);" >
                                              <img id="imgIRBDate" title="" height="16" alt=""  src="<%=request.getContextPath()%>/coeusliteimages/cal.gif" width="16" border="0" />
                                           </a>
                                           <% } else {%>
                                            <input type="text" name="<%=fieldId%>" id="<%=fieldId%>" value="<%=fieldValue%>" maxlength="10"   onkeypress="CheckNumeric(event)" onblur="isValidDate(document.forms[0].<%=fieldId%>.value,'<%=fieldId%>')" class="textbox" />
                                          <a id="hlIRBDate" onclick="displayCalendarWithTopLeft('<%=fieldId%>',8,25)" tabindex="32" href="javascript:void(0);" >
                                              <img id="imgIRBDate" title="" height="16" alt=""  src="<%=request.getContextPath()%>/coeusliteimages/cal.gif" width="16" border="0" />
                                           </a>
                                         <%}%>
                            </td>
                            <%}}%>
                        </tr>
                  <%   }
                }
          /*   }
        }*/%>


       <%if(SlctField_count > 0){
            String useSltFld = "";
            String sltTxtLabel  = "";
             fieldValue = "";
             editValue = "";

            for(int i=1; i <= SlctField_count; i++ ){
                useSltFld = SLCT_FIELD+"_"+i;
                if(inputFields.containsKey(useSltFld) && inputFields.get(useSltFld).toString().equalsIgnoreCase("Y")) {
                    sltTxtLabel = SLCT_FIELD_LBL+"_"+i+"_LABEL";
                    editValue = SLCT_FIELD_LBL+"_"+i;

                    if(inputFields.get(sltTxtLabel) != null) {
                        label = inputFields.get(sltTxtLabel).toString();
                        fieldId = label.replaceAll(" ", "_");
                        if(travelUserDet != null && travelUserDet.containsKey(editValue)){
                            fieldValue = (String)travelUserDet.get(editValue);
                        }
                %>
		<tr bgcolor="#D1E5FF">
                            <td style="width: 25%;color: #333333;font-family:Arial,Helvetica,sans-serif;font-size:12px;font-weight:bold;text-align:inherit;padding-left: 3px;padding-top: 3px;">
                               <%=label%>:
                            </td>
                            <td style="padding-top: 3px;">
                                <% if(edit) { %>
                                    <select id="<%=fieldId%>" name="fieldId" style="width: 100%" onchange="">
                                        <option value="<%=fieldId%>"><%=fieldId%></option>
                                   </select>
                                 <% } else {%>
                                    <select id="<%=fieldId%>" name="fieldId" style="width: 100%" onchange="">
                                        <option value="<%=fieldId%>"><%=fieldId%></option>
                                   </select>
                                 <%}%>
                            </td>

                        </tr>
                  <%   }
                }
             }
        }%>


        <tr bgcolor="#D1E5FF">
            <td colspan="4">
                <br />
                <% if(modeValue == null){
                String link="javaScript:saveProject()";

                session.removeAttribute("travelList");
                    %>
                             &nbsp;<html:button styleClass="clsavebutton"  value="Continue" property="save"  onclick="<%=link%>"/>
                             <% }else if(modeValue != null) {
                              String link="javaScript:continueProject()";
                              session.removeAttribute("travelList");
                                    %>
                             <html:button styleClass="clsavebutton"  value="Continue" property="save"  onclick="<%=link%>"/>
                             <% } %>

            </td>
        </tr></table>
	<script language="javascript" type="text/javascript">
       function saveProject()
      {
        var success=validateNewproject();
        //var success=allnumeric();
          if(success==true){
            document.forms[0].action='<%=target%>';
            document.forms[0].submit();
        }
      }

        function continueProject()
      {
        var success=validateNewproject();
        //var success=allnumeric();
          if(success==true){
              if(!<%=edit%>){
            document.forms[0].action ='<%=url%>';
            document.forms[0].submit();}
        else{
            document.forms[0].action ='<%=url1%>';
            document.forms[0].submit();
        }
        }
        }

      function validateNewproject(){
           <%
                String label1 = "";
                String fieldId1 = "";

                if(shrtTxtField_count > 0){
                    useShtFld = "";
                    srtTxtLabel  = "";
                    String shrtReq = "";
                    for(int i=1; i <= shrtTxtField_count; i++ ){
                        useShtFld = SHRT_TXT_FIELD+"_"+i;
                        if(inputFields.containsKey(useShtFld) && inputFields.get(useShtFld).toString().equalsIgnoreCase("Y")) {
                            srtTxtLabel = SHRT_TXT_FIELD_LBL+"_"+i+"_LABEL";
                            shrtReq = REQ_SHRT_TXT_FIELD+"_"+i;

                        //    if(inputFields.get(shrtReq) != null && inputFields.get(shrtReq).toString().equalsIgnoreCase("Y")){
                            if(inputFields.get(srtTxtLabel) != null) {
                                label1 = inputFields.get(srtTxtLabel).toString();
                                fieldId1 = label1.replaceAll(" ", "_");
                               if(fieldId1.trim().length() > 1) { %>
                                  if(document.forms[0].<%=fieldId1%> != null) {
                                     if(document.forms[0].<%=fieldId1%>.value==null || document.forms[0].<%=fieldId1%>.value=='')
                                    {
                                        alert("Please enter a Travel <%=label1%>");
                                        document.forms[0].<%=fieldId1%>.focus();
                                        return false;
                                    }
                                 }
                            <%}
                         //       }
                                }
                            }
                        }
            }

                if(lngTxtField_count > 0){
            String useLngFld = "";
            String lngTxtLabel  = "";
            String lngReq = "";
            for(int i=1; i <= lngTxtField_count; i++ ){
                useLngFld = LNG_TXT_FIELD+"_"+i;
                if(inputFields.containsKey(useLngFld) && inputFields.get(useLngFld).toString().equalsIgnoreCase("Y")) {
                    lngTxtLabel = LNG_TXT_FIELD_LBL+"_"+i+"_LABEL";
                    lngReq = REQ_LNG_TXT_FIELD+"_"+i;
                    if(inputFields.get(lngReq) != null && inputFields.get(lngReq).toString().equalsIgnoreCase("Y")){
                    if(inputFields.get(lngTxtLabel) != null) {
                        label1 = inputFields.get(lngTxtLabel).toString();
                        fieldId1 = label1.replaceAll(" ", "_");
                            if(fieldId1.trim().length() > 1) {%>
                                  if(document.forms[0].<%=fieldId1%> != null) {
                                     if(document.forms[0].<%=fieldId1%>.value==null || document.forms[0].<%=fieldId1%>.value=='')
                                    {
                                        alert("Please enter a <%=label1%>");
                                        document.forms[0].<%=fieldId1%>.focus();
                                        return false;
                                    }

                                    if((document.forms[0].<%=fieldId1%>.value.length >200))
                                     {
                                         alert("<%=label1%> should not exceed 200 characters");
                                             document.forms[0].<%=fieldId1%>.focus();
                                         return false;
                                      }
                                 }
                            <%

                                }
                        }
                    }
                    }
            }
            }
           if(nmbrField_count > 0){
                String useNmbFld = "";
                String NmTxtLabel  = "";
                String reqNum = "";

                for(int i=1; i <= lngTxtField_count; i++ ){
                    useNmbFld = NBR_FIELD+"_"+i;
                    if(inputFields.containsKey(useNmbFld) && inputFields.get(useNmbFld).toString().equalsIgnoreCase("Y")) {
                        NmTxtLabel = NBR_FIELD_LBL+"_"+i+"_LABEL";
                        reqNum = REQ_NBR_FIELD+"_"+i;
                      if(inputFields.get(reqNum) != null && inputFields.get(reqNum).toString().equalsIgnoreCase("Y")){
                        if(inputFields.get(NmTxtLabel) != null) {
                            label1 = inputFields.get(NmTxtLabel).toString();
                            String lb = label1.replaceAll("\\(", "");
                            label1 = lb.replaceAll("\\)", "");
                            fieldId1 = label1.replaceAll(" ", "_");%>
                                  if(document.forms[0].<%=fieldId1%> != null) {
                                     if(document.forms[0].<%=fieldId1%>.value==null || document.forms[0].<%=fieldId1%>.value=='')
                                    {
                                        alert("Please enter a Travel <%=label1%>");
                                        document.forms[0].<%=fieldId1%>.focus();
                                        return false;
                                    }else{
                                         var validate = /^\D$/;
                                         if (validate.test(document.forms[0].<%=fieldId1%>.value)) {
                                         alert("Please enter a numeric value");
                                         return false;
                                        } 
                                    }
                                 }
                            <%

                            }
                        }
                        }
                    }
            }

                if(dateField_count > 0){
                    useDateFld = "";
                    dateTxtLabel  = "";
                    String dateReq = "";

                    for(int i=1; i <= dateField_count; i++ ){
                        useDateFld = DATE_FIELD+"_"+i;
                        if(inputFields.containsKey(useDateFld) && inputFields.get(useDateFld).toString().equalsIgnoreCase("Y")) {
                            dateTxtLabel = DATE_FIELD_LBL+"_"+i+"_LABEL";
                            dateReq = REQ_DATE_FIELD+"_"+i;

                            if(inputFields.get(dateReq) != null && inputFields.get(dateReq).toString().equalsIgnoreCase("Y")) {
                            if(inputFields.get(dateTxtLabel) != null) {
                                label1 = inputFields.get(dateTxtLabel).toString();
                                fieldId1 = label1.replaceAll(" ", "_");%>
                                  if(document.forms[0].<%=fieldId1%> != null) {
                                     if(document.forms[0].<%=fieldId1%>.value==null || document.forms[0].<%=fieldId1%>.value=='')
                                    {
                                        alert("Please enter a Travel <%=label1%>");
                                        document.forms[0].<%=fieldId1%>.focus();
                                        return false;
                                    }
                                 }
                            <%
                                }
                            }
                            }
                        }
                    }

                if(SlctField_count > 0){
                    String useSltFld = "";
                    String sltTxtLabel  = "";
                    String sltReq = "";

                    for(int i=1; i <= SlctField_count; i++ ){
                        useSltFld = SLCT_FIELD+"_"+i;
                        if(inputFields.containsKey(useSltFld) && inputFields.get(useSltFld).toString().equalsIgnoreCase("Y")) {
                            sltTxtLabel = SLCT_FIELD_LBL+"_"+i+"_LABEL";
                            sltReq = REQ_SLCT_FIELD+"_"+i;

                            if(inputFields.get(sltReq) != null && inputFields.get(sltReq).toString().equalsIgnoreCase("Y")){
                            if(inputFields.get(sltTxtLabel) != null) {
                                label1 = inputFields.get(sltTxtLabel).toString();
                                fieldId1 = label1.replaceAll(" ", "_");%>
                                  if(document.forms[0].<%=fieldId1%> != null) {
                                     if(document.forms[0].<%=fieldId1%>.value==null || document.forms[0].<%=fieldId1%>.value=='')
                                    {
                                        alert("Please enter a Travel <%=label1%>");
                                        document.forms[0].<%=fieldId1%>.focus();
                                        return false;
                                    }
                                 }
                            <%
                                }
                            }
                        }
                    }
                }
                %>

           return true;
        }

         function CheckFund (e)
        {
        var key;
        key = e.which ? e.which : e.keyCode;
        if (key!=8 && key!=9 && key!=46){
        if((key>=48 && key<=57)) {
        e.returnValue= true;
        }
            else
        {
        if(key==46){
            e.returnValue= true;
            }else{
             alert("please enter a numeric value");
            e.returnValue = false;
             document.forms[0].<%=fieldId1%>.value='';
             document.forms[0].<%=fieldId1%>.focus();
        }
       }
      }
     }
          </script>

</html:form>



