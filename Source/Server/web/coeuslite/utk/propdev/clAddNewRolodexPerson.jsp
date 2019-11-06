<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType = "text/html;charset=UTF-8" language="java" %> 
<%@ page import      = "edu.mit.coeuslite.utils.CoeusLiteConstants" %>
<%@ page import      = "edu.mit.coeuslite.utils.DateUtils" %>
<%@ page import      = "org.apache.struts.taglib.html.JavascriptValidatorTag"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<%  DateUtils dateUtils = new edu.mit.coeuslite.utils.DateUtils();%>
<!-- Added code to hold the Country and State lists for the forms drop down.            -->
<jsp:useBean id="countryList"  scope="session"       class="java.util.Vector" />
<jsp:useBean id="stateList"    scope="session"       class="java.util.Vector" />
<!--Added for Case#3789 -Add a New Rolodex Entry using CoeusLite interface -Start -->
<jsp:useBean id="validCountryStateList" scope="session" class="java.util.HashMap" />
<!--Added for Case#3789 -Add a New Rolodex Entry using CoeusLite interface  - End -->
<html:html>
<head>
    <style>
        .cltextbox-medium{ width: 160px; }        
        .textbox { width: 160px; font-weight: normal; }  
        .cltextbox-color { width: 500px; font-weight: normal; }
    </style><title>Coeus Web Add Rolodex Person</title>
    
    <script>
//Added for Case#3789 -Add a New Rolodex Entry using CoeusLite interface- Start
var formName = "";
var selectedField = "";
var textValue = new Array();
var index =0;
var checkListsize = 0;
        
<logic:iterate name="validCountryStateList" id="stateCountryList" >
textValue[index++] = 'DES<bean:write name="stateCountryList" property="key" />';
var VAL<bean:write name="stateCountryList" property="key" />= new Array();  // it declares array variables initially
var DES<bean:write name="stateCountryList" property="key" />= new Array();// it declares array variables initially
VAL<bean:write name="stateCountryList" property="key" />.push('');  // appends value
DES<bean:write name="stateCountryList" property="key" />.push(' <bean:message bundle="proposal" key="newRolodexEntryMessage.PleaseSelect"/> '); // appends value
      <bean:define id="countryCode" name="stateCountryList" property="value" type="java.util.Map"/>
      <logic:iterate id="sl" name="countryCode">
      <logic:present name="sl">
            VAL<bean:write name="stateCountryList" property="key" />.push('<bean:write name="sl" property="key" />');
            DES<bean:write name="stateCountryList" property="key" />.push('<bean:write name="sl" property="value" />');
      </logic:present>
      </logic:iterate>  
</logic:iterate>

function trim(val) {
val = val.replace( /^\s*/, "" ).replace( /\s*$/, "" );
return val;
}

var desc1 ="";
var val1 ="";
function populateState(form,selectValue,fieldName,check){       
     if(check){
        dataChanged();
      }
      chosen=selectValue;
      var desc="DES"+chosen;
      var val="VAL"+chosen;
      val1 = val;
      desc1 = desc;
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
                              select.options[i]=new Option(trim(eval(desc)[i]), trim(eval(val)[i]));
                          }
                        } else {
                          select.options[0]=new Option('<bean:message bundle="proposal" key="newRolodexEntryMessage.PleaseSelect"/>', '');
                          select.options[0].selected=true;                        
                        }
            }
}

function setCountryStates(form,selectValue){
    formName = form;
    selectedField = selectValue;
    populateState(formName,selectedField,'state',true);
}
//Added for Case#3789 -Add a New Rolodex Entry using CoeusLite interface - End


//              Start Sponsor Search Function

// ********************************************************************************************
// ** To mimic the Coeus Premium screen we need to go back to the database and get the sponsor
// ** address. In order to do this, it is required to submit the form to get back to the server
// ** side. The only other option would be load all the addresses of the sponsors in the list 
// ** of the sponsor search form which would be more work than we need to do.
// ** JDF University of Tennessee.
// ********************************************************************************************
                function needSponsorAddress(){
				    var sponsorCodeNumber = document.RolodexInfoForm.sponsor.value;
					var actionToGetSponsorAddress = "<%=request.getContextPath()%>/getRolodexInfo.do?dataModified=Y";
					if(sponsorCodeNumber.length != 0){
					    document.RolodexInfoForm.action = actionToGetSponsorAddress;
// ********************************************************************************************
// ** We set this hidden field to know that the user has performed a sponsor search.
// ********************************************************************************************
						document.RolodexInfoForm.getSponsorAddress.value = "getTheAddress";
					    document.RolodexInfoForm.submit();
					}
				}
				
                var errValue = false;
                var errLock  = false;			
                
                function open_search_window(link,windowName){
                    var winleft = (screen.width - 650) / 2;
                    var winUp = (screen.height - 450) / 2;  
                    var win = "scrollbars=1,resizable=1,width=800,height=450,left="+winleft+",top="+winUp
                    if(windowName == "Sponsor"){
                        win = "scrollbars=1,resizable=1,width=800,height=450,left="+winleft+",top="+winUp
                        sList = window.open('<bean:write name='ctxtPath'/>'+"/"+link, "list", win);
                    }
                    if (parseInt(navigator.appVersion) >= 4) {
                        window.sList.focus(); 
                        }
                }
                
                function fetch_Data(result,searchType){
                    dataChanged();
                    if(searchType == "SponsorSearch"){
                        document.RolodexInfoForm.sponsor.value      = result["SPONSOR_CODE"];
                        document.RolodexInfoForm.organization.value = result["SPONSOR_NAME"];
                        document.RolodexInfoForm.searchFlag.value = 'Y';
                    }
                        needSponsorAddress();                        
                 }
                 
             function validateForm(form) { 
                var var_init_begin_date = new Date(form.startDate.value.substring(6,10),
                                                   form.startDate.value.substring(0,2)-1,
                                                   form.startDate.value.substring(3,5));           

                var var_init_end_date = new Date(form.endDate.value.substring(6,10),
                                                 form.endDate.value.substring(0,2)-1,
                                                 form.endDate.value.substring(3,5));

                var init_difference = var_init_end_date.getTime() - var_init_begin_date.getTime(); 
                var validate = true;         

                 //return validateGeneralInfoProposal(form); 

                if(validateGeneralInfoProposal(form)){
                   if(Math.round(init_difference) < Math.round(1) && Math.round(init_difference)!= Math.round(0)){                        
                        alert("<bean:message bundle="proposal" key="generalInfoProposal.invalidDate"/>");
                        form.startDate.focus();
                        return false;
                    }
                    if(document.generalInfoProposal.sponsorCode.value == "<bean:message bundle="proposal" key="generalInfoProposal.NIHSponsorCode"/>"){
                        if(document.generalInfoProposal.title.value.length >81){
                            alert("<bean:message bundle="proposal" key="generalInfoProposal.title.lengthIFNIH"/>");
                            form.title.focus();
                            return false;
                        }                   
                    }
                }else{
                    return false;
                }            
            }

//          This function uses recursion (trim function)
            function TrimUsingRecursion(str){
                if(str.charAt(0) == " "){
	                str = TrimUsingRecursion(str.substring(1));
                }
                if (str.charAt(str.length-1) == " "){
	                str = TrimUsingRecursion(str.substring(0,str.length-1));
                }
                return str;
           }
//         This function uses a while loop (trim function)
           function TrimUsingWhileLoop(str){
                while(str.charAt(0) == (" ") ){  str = str.substring(1);}
                while(str.charAt(str.length-1) == " "){str = str.substring(0,str.length-1);}
                return str;
           }
           function validateRolodex(){
               var country = document.forms[0].country.value;
               //	TrimUsingWhileLoop(country);
               var email   = document.forms[0].email.value;
               var state   = document.forms[0].state.value;
	           var organization = document.forms[0].organization.value;
	           var isEmailOK = true	
// ********************************************************************************************
// * Added a fix for non-displayed white space in the data. This was a    *
// * in the UTK Database for the country code.                            *
// ********************************************************************************************                    
	           if(country=='USA' || country=='US' || country.substring(0, 2) == 'US'){
                   /**Modified for Case#3789 -Add a New Rolodex Entry using CoeusLite interface - Start**/
	               if(state==null || state=='' || state == '<bean:message bundle="proposal" key="newRolodexEntryMessage.PleaseSelect"/>'){
                   /**Modified for Case#3789 -Add a New Rolodex Entry using CoeusLite interface - End**/
                       alert("State is a required field if the country is United States!");
                       return false;
                   }
	           }
               //if(organization=='' || organization==null || organization.length == 0){
               //    alert("Organization is a required field!");
               //    return false;
               //}
	
	           if(email.length != 0){
	               isEmailOK = emailCheck(email);
	               if(isEmailOK==false){
                       return false;
	               }
	           }
	           return true;
          }
// ********************************************************************************************
// ** We are attempting to force a valid email address. else the user should leave it blank.
// ********************************************************************************************

function emailCheck (emailStr) {
var checkTLD=1;
var knownDomsPat=/^(com|net|org|edu|int|mil|gov|arpa|biz|aero|name|coop|info|pro|museum)$/;
var emailPat=/^(.+)@(.+)$/;
var specialChars="\\(\\)><@,;:\\\\\\\"\\.\\[\\]";
var validChars="\[^\\s" + specialChars + "\]";
var quotedUser="(\"[^\"]*\")";
var ipDomainPat=/^\[(\d{1,3})\.(\d{1,3})\.(\d{1,3})\.(\d{1,3})\]$/;
var atom=validChars + '+';
var word="(" + atom + "|" + quotedUser + ")";
var userPat=new RegExp("^" + word + "(\\." + word + ")*$");
var domainPat=new RegExp("^" + atom + "(\\." + atom +")*$");
var matchArray=emailStr.match(emailPat);
if (matchArray==null) {
alert("Email address seems incorrect (check @ and .'s)");
return false;
}
var user=matchArray[1];
var domain=matchArray[2];
for (i=0; i<user.length; i++) {
if (user.charCodeAt(i)>127) {
alert("Ths username contains invalid characters.");
return false;
   }
}
for (i=0; i<domain.length; i++) {
if (domain.charCodeAt(i)>127) {
alert("Ths domain name contains invalid characters.");
return false;
   }
}
if (user.match(userPat)==null) {
alert("The username doesn't seem to be valid.");
return false;
}
var IPArray=domain.match(ipDomainPat);
if (IPArray!=null) {
for (var i=1;i<=4;i++) {
if (IPArray[i]>255) {
alert("Destination IP address is invalid!");
return false;
   }
}
return true;
}
var atomPat=new RegExp("^" + atom + "$");
var domArr=domain.split(".");
var len=domArr.length;
for (i=0;i<len;i++) {
if (domArr[i].search(atomPat)==-1) {
alert("The domain name does not seem to be valid.");
return false;
   }
}
if (checkTLD && domArr[domArr.length-1].length!=2 && 
domArr[domArr.length-1].search(knownDomsPat)==-1) {
alert("The address must end in a well-known domain or two letter " + "country.");
return false;
}
if (len<2) {
alert("This address is missing a hostname!");
return false;
}
return true;
}

function clearFields(){
    dataChanged();
    document.RolodexInfoForm.searchFlag.value = 'N';
    var sponsorCode = document.RolodexInfoForm.sponsor.value;    
    sponsorCode = sponsorCode.length;    
    if(sponsorCode < 6){
        document.RolodexInfoForm.organization.value = '';
        document.RolodexInfoForm.address1.value = '';
        document.RolodexInfoForm.address2.value = '';
        document.RolodexInfoForm.address3.value = '';
        document.RolodexInfoForm.city.value = '';
        document.RolodexInfoForm.email.value = '';
        document.RolodexInfoForm.fax.value = '';
        document.RolodexInfoForm.postalCode.value = '';
        document.RolodexInfoForm.county.value = '';
        document.RolodexInfoForm.phone.value = '';
        document.RolodexInfoForm.comments.value = '';
        document.RolodexInfoForm.country.selectedIndex = '';
        document.RolodexInfoForm.state.selectedIndex = '';    
    }
}

<%--
Commented for Case#3789 -Add a New Rolodex Entry using CoeusLite interface - Start
function showHideState(sel){
    dataChanged();
    var countryCode = sel.options[sel.selectedIndex].value;      
    if(countryCode == 'USA'){    
        document.getElementById('divState1').style.display = 'block';
        document.getElementById('divState2').style.display = 'none';        
    }else{
        document.getElementById('divState1').style.display = 'none';
        document.getElementById('divState2').style.display = 'block';  
        document.RolodexInfoForm.state.value = '';
    }
}
Commented for Case#3789 -Add a New Rolodex Entry using CoeusLite interface - End
--%>
//  End -->
                  </script>
    <html:base/>
</head>
<body>
<%
String mode = (String)session.getAttribute("mode"+session.getId());
System.out.println("*******The mode is set at " + mode + " (RolodexForm)");
boolean modeValue=false;
if(mode!=null && mode!=""){
    if(mode.equalsIgnoreCase("display")){
        modeValue=true;
    }
}
String maintainRolodex = (String)session.getAttribute("maintainRolodex");
if(maintainRolodex.equalsIgnoreCase("no")){
    modeValue=true;
} else {
    modeValue=false;
}
session.removeAttribute("maintainRolodex");
String lastRolodexId = (String)session.getAttribute("lastRolodexId");
session.removeAttribute("lastRolodexId");
%>
<html:form action="/rolodexInfoSave.do"  method="post" onsubmit="return validateRolodex();">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table">
    <tr>
        <td align="left" valign="top" class="theader">
            <bean:message bundle="proposal" key="addNewRolodexPerson.AddNewRolodexPerson"/> 
        </td>       
    </tr>
    <tr><td>
            <!-- Start the Body of AddRolodexPerson Table -->
            <table class="table" border="0" cellpadding="0" cellspacing="0" width="100%">
                <tbody><tr valign="top">
                        <td align="left" valign="top">
                            <!-- Start Rolodex specific messages --> 
                            <font color="red">*</font> <bean:message bundle="proposal" key="generalInfoProposal.mandatory"/>        
                            <tr class='theader' style='font-weight: normal;'>
                                <td class='copy'>
                                    
                                    <logic:messagesPresent>
                                        <script>errValue = true;</script>
                                        <html:errors header="" footer=""/>
                                    </logic:messagesPresent>
                                    <logic:messagesPresent message="true">
                                        <script>errValue = true;</script>
                                        <!-- Start Rolodex specific messages --> 
                                        <html:messages id="message" message="true" property="AddRolodexInformation" bundle="proposal">
                                            <script>errValue = false;</script>
                                            <br><bean:write name = "message"/>
                                        </html:messages>
                                        <html:messages id="message" message="true" property="RolodexInformationAdded" bundle="proposal">
                                            <script>errValue = false;</script>
                                            <li><bean:write name = "message"/>&nbsp;&nbsp;(Rolodex ID was <%= lastRolodexId %>)</li>
                                        </html:messages>
                                        <html:messages id="message" message="true" property="AddRolodexInformationNextEntry" bundle="proposal">
                                            <script>errValue = false;</script>
                                            <li><bean:write name = "message"/></li>
                                        </html:messages>
                                        <html:messages id="message" message="true" property="AddRolodexInformationRight" bundle="proposal">
                                            <script>errValue = true;</script>
                                            <li><bean:write name = "message"/></li>
                                        </html:messages>
                                        <html:messages id="message" message="true" property="RolodexInformationBadSponsor" bundle="proposal">
                                            <script>errValue = true;</script>
                                            <font color="red"><li><bean:write name = "message"/></li></font>
                                        </html:messages>
                                        <!-- Stop Rolodex specific messages -->
                                        <!-- If lock is deleted then show this message --> 
                                        <html:messages id="message" message="true" property="errMsg" bundle="proposal"> 
                                            <script>errLock = true;</script>
                                            <li><bean:write name = "message"/></li>
                                        </html:messages>
                                        <!-- If lock is acquired by another user, then show this message -->
                                        <html:messages id="message" message="true" property="acqLock" bundle="proposal">
                                            <script>errLock = true;</script>
                                            <li><bean:write name = "message"/></li>
                                        </html:messages>
                                        <html:messages id="message" message="true" property="organizationRequired" bundle="proposal">
                                            <script>errValue = true;</script>
                                            <font color="red"><li><bean:write name = "message"/></li></font>
                                        </html:messages>                                        
                                    </logic:messagesPresent>   
                                    
                                </td>
                            </tr>
                            <!-- Add Rolodex Information: - Start  -->
                            <table class="tabtable" align="center" border="0" cellpadding="0" cellspacing="0" width="99%">
                                <tbody><tr>
                                        <td align="left" valign="top">
                                            <table class="tableheader" border="0" cellpadding="0" cellspacing="0" width="100%">
                                                <tbody><tr>
                                                        <td> 
                                                        </td>
                                                    </tr>
                                            </tbody></table>
                                        </td>
                                    </tr>
                                    <tr>      
                                        <td class="helptext" align="left">
                                        </td>
                                    </tr>
                                    <!-- start - display the GLOBAL_MESSAGE  (the "property name" marker to use for global messages) -->
                                    <tr class="copy" align="left">
                                        <td>
                                            <font color="red"></font>
                                        </td>
                                    </tr>
                                    <!-- end - display the GLOBAL_MESSAGE -->
                                    <!-- start - display unitNumber validation error message if there is any -->
                                    <tr class="copybold">
                                        <td class="copy">
                                            <font color="red">
                                            </font>
                                        </td>
                                    </tr> 
                                    <tr>
                                        <td align="left" valign="top" width="100%"><table class="tabtable" align="center" border="0" cellpadding="0" cellspacing="0" width="100%">
                                                <tbody><tr>
                                                        <td class="copy">
                                                            <table border="0" cellpadding="0" cellspacing="3" width="100%">
                                                            <!--DWLayoutTable-->
                                                            <tbody>
                                                            <tr>
                                                                <td width="15%" align="right" height="22" nowrap="nowrap" class="copybold">
                                                                    <bean:message key="newRolodexEntryLabel.FirstName" bundle="proposal"/>:
                                                                </td>
                                                                <td width="18%" align="left" valign="top" class="copy">                        
                                                                    <html:text property="firstName" styleClass="textbox" disabled="<%=modeValue%>" onchange="dataChanged()" maxlength="20" />
                                                                </td>
                                                                <td width="15%" align="right" nowrap="nowrap" class="copybold">
                                                                    <bean:message key="newRolodexEntryLabel.LastName" bundle="proposal"/>:
                                                                </td>                    
                                                                <td width="18%" valign="top" class="copy">
                                                                    <html:text property="lastName" styleClass="textbox" disabled="<%=modeValue%>" onchange="dataChanged()" maxlength="20" />                    
                                                                </td>                     
                                                                <td width="15%" align="right" nowrap="nowrap" class="copybold">
                                                                    <bean:message key="newRolodexEntryLabel.MiddleName" bundle="proposal"/>:
                                                                </td>
                                                                <td class="copy" width="18%">
                                                                    <html:text property="middleName" styleClass="textbox" disabled="<%=modeValue%>" onchange="dataChanged()" maxlength="20" />                        
                                                                </td>                   
                                                            </tr>
                                                            <tr>
                                                                <td width="15%" align="right" height="22" nowrap="nowrap" class="copybold">
                                                                    <bean:message key="newRolodexEntryLabel.Suffix" bundle="proposal"/>:
                                                                </td>
                                                                <td width="18%" align="left" valign="top">
                                                                    <html:text property="suffix" styleClass="textbox" disabled="<%=modeValue%>" onchange="dataChanged()" maxlength="10" />
                                                                </td>
                                                                <td width="15%" align="right" valign="middle" nowrap="nowrap" class="copybold">
                                                                    <bean:message key="newRolodexEntryLabel.Prefix" bundle="proposal"/>:
                                                                </td>
                                                                <td width="18%" align="right" valign="top" class="copy">
                                                                    <html:text property="prefix" styleClass="textbox" disabled="<%=modeValue%>" onchange="dataChanged()" maxlength="10" />
                                                                </td>
                                                                <td width="15%" align="right" nowrap="nowrap" class="copybold">
                                                                    <bean:message key="newRolodexEntryLabel.Title" bundle="proposal"/>:
                                                                </td>
                                                                <td class="copybold" width="18%" align="right">
                                                                    <html:text property="title" styleClass="textbox" disabled="<%=modeValue%>" onchange="dataChanged()" maxlength="35" />
                                                                </td>                    
                                                            </tr>
                                                            <tr>
                                                                <td width="15%" height="22" align="right" class="copybold">
                                                                    <bean:message key="newRolodexEntryLabel.Sponsor" bundle="proposal"/>:
                                                                </td>
                                                                <td width="18%" >
                                                                    <html:text property="sponsor" styleClass="textbox" disabled="<%=modeValue%>" onchange="clearFields()" maxlength="6" />
                                                                </td>
                                                                <td width="15%">
                                                                    <%if(!modeValue){%>
                                                                    <a href="javascript:open_search_window('/generalProposalSearch.do?type=SponsorSearch&search=true&searchName=SPONSORSEARCH','Sponsor')" class='search'>
                                                                        <u><bean:message bundle="proposal" key="generalInfoProposal.search"/></u>
                                                                    </a>
                                                                    <%}%>
                                                                </td>
                                                                <td width="18%"></td>
                                                            </tr>
                                                            <tr>
                                                                <td width="15%" height="22" class="copybold">
                                                                    <font color="red">*</font><bean:message key="newRolodexEntryLabel.Organization" bundle="proposal"/>:
                                                                </td>
                                                                <td colspan="3" width="18%">
                                                                    <html:text property="organization" styleClass="textbox" style="width:422px;" disabled="<%=modeValue%>" maxlength="120" />
                                                                </td>                                                                
                                                                <!--<td width="15%"></td>
                                                                <td width="18%"></td>-->
                                                                
                                                                <!--coeusqa-1528 start shabarish-->
                                                                <td class='copybold' nowrap>
                                                                    <html:radio property='rolodexStatus' value="A" disabled="<%=modeValue%>"/><bean:message bundle="proposal" key="newRolodexEntryLabel.activeStatus"/> 
                                                                 </td>
                                                                <td align="left" class='copybold' nowrap>
                                                                    <html:radio property='rolodexStatus' value="I" disabled="<%=modeValue%>" /><bean:message bundle="proposal" key="newRolodexEntryLabel.inactiveStatus"/>                    
                                                                </td> 
                                                                <!--coeusqa-1528 end-->
                                                            </tr>
                                                            <tr>
                                                                <td width="15%" height="22" align="right" class="copybold">
                                                                    <bean:message key="newRolodexEntryLabel.Address" bundle="proposal"/>:
                                                                </td>                                                                
                                                                <td width="18%" align="right" class="copybold">
                                                                    <html:text property="address1" styleClass="textbox" style="width:190px;" disabled="<%=modeValue%>" onchange="dataChanged()" maxlength="80" />
                                                                </td>
                                                                <td width="15%"></td>
                                                                <td width="18%"></td>                                                                                                                             
                                                                <!--Added for Case#3789 -Add a New Rolodex Entry using CoeusLite interface - End -->
                                                            </tr>
                                                            
                                                            <tr>
                                                                <td height="22" width="15%" >
                                                                </td>
                                                                <td width="18%" align="right" class="copybold">
                                                                    <html:text property="address2" styleClass="textbox" style="width:190px;" disabled="<%=modeValue%>" onchange="dataChanged()" maxlength="80" />
                                                                </td>
                                                                <td width="15%"></td>
                                                                <td width="18%"></td>                                                                
                                                            </tr>
                                                            <tr>
                                                                <td height="22" width="15%" >
                                                                </td>
                                                                <td width="18%" align="right" class="copybold">
                                                                    <html:text property="address3" styleClass="textbox" style="width:190px;" disabled="<%=modeValue%>" onchange="dataChanged()" maxlength="80" />
                                                                </td>
                                                                <td width="15%"></td>
                                                                <td width="18%"></td>                                                                
                                                            </tr>
                                                            <%-- 
                                                            Commented for Case#3789 -Add a New Rolodex Entry using CoeusLite interface - Start
                                                            <tr>
                                                                <td width="15%" height="22" align="right" class="copybold">
                                                                    <bean:message key="newRolodexEntryLabel.City" bundle="proposal"/>:
                                                                </td>
                                                                <td width="18%">
                                                                    <html:text property="city" styleClass="textbox" disabled="<%=modeValue%>" onchange="dataChanged()" maxlength="30" />
                                                                </td>
                                                                <td width="15%" align="right" valign="top" class="copybold">
                                                                    <bean:message key="newRolodexEntryLabel.County" bundle="proposal"/>:
                                                                </td>
                                                                <td width="15%">
                                                                    <html:text property="county" styleClass="textbox" disabled="<%=modeValue%>" onchange="dataChanged()" maxlength="30" />
                                                                </td>
                                                                <td width="15%"></td>
                                                                <td width="18%"></td>
                                                            </tr>
                                                            // Commented for Case#3789 -Add a New Rolodex Entry using CoeusLite interface - End
                                                            --%>
                                                            <tr>
                                                                <!--Added for Case#3789 -Add a New Rolodex Entry using CoeusLite interface - Start-->
                                                                <td width="15%" align="right" height="22" class="copybold">
                                                                    <bean:message key="newRolodexEntryLabel.Country" bundle="proposal"/>:
                                                                </td>
                                                                <td width="18%">
                                                                    <html:select name="RolodexInfoForm" styleClass="cltextbox-medium" property="country" disabled="<%=modeValue%>" onchange="setCountryStates('RolodexInfoForm',this.value);"> 
                                                                        <html:option value=""><bean:message bundle="proposal" key="generalInfoProposal.pleaseSelect"/></html:option>
                                                                        <html:options collection="countryList" property="code" labelProperty="description" />
                                                                    </html:select>                                                             
                                                                </td>                                                               
                                                                <!--Added for Case#3789 -Add a New Rolodex Entry using CoeusLite interface- End -->
                                                                
                                                                <td width="15%" height="22" align="right" class="copybold">
                                                                    <bean:message key="newRolodexEntryLabel.State" bundle="proposal"/>:
                                                                </td>
                                                                <!--Modified for Case#3789 -Add a New Rolodex Entry using CoeusLite interface - Start -->
                                                                <%--
                                                                <td width="18%">
                                                                    <div id="divState1" style='display: block;'>
                                                                        <html:select name="RolodexInfoForm" styleClass="cltextbox-medium" property="state" disabled="<%=modeValue%>" onchange="dataChanged()"> 
                                                                            <html:option value=""><bean:message bundle="proposal" key="generalInfoProposal.pleaseSelect"/></html:option>
                                                                            <html:options collection="stateList" property="code" labelProperty="description" />
                                                                        </html:select>     
                                                                    </div>
                                                                    <div id="divState2" style='display: none;'>
                                                                        <html:select name="RolodexInfoForm" styleClass="cltextbox-medium" property="tempState" disabled="<%=modeValue%>" onchange="dataChanged()"> 
                                                                            <html:option value=""><bean:message bundle="proposal" key="generalInfoProposal.pleaseSelect"/></html:option>                                                                            
                                                                        </html:select>     
                                                                    </div>                                                                    
                                                                </td>
                                                                --%>
                                                                <td width="18%">
                                                                    <html:select name="RolodexInfoForm" styleClass="cltextbox-medium" property="state" disabled="<%=modeValue%>" onchange="dataChanged()"> 
                                                                        <html:option value=""><bean:message bundle="proposal" key="newRolodexEntryMessage.PleaseSelect"/></html:option>                                                                            
                                                                    </html:select> 
                                                                </td>
                                                                <!--Modified for Case#3789 -Add a New Rolodex Entry using CoeusLite interface - End -->
                                                                <td width="15%" align="right" class="copybold">
                                                                    <bean:message key="newRolodexEntryLabel.PostalCode" bundle="proposal"/>:
                                                                </td>
                                                                <td width="18%" class="copybold">
                                                                    <html:text property="postalCode" styleClass="textbox" disabled="<%=modeValue%>" onchange="dataChanged()" maxlength="15" />
                                                                </td>
                                                                <td width="15%"></td>
                                                                <td width="18%"></td>
                                                            </tr>
                                                            <tr>
                                                                <td width="15%" height="22" align="right" class="copybold">
                                                                    <bean:message key="newRolodexEntryLabel.City" bundle="proposal"/>:
                                                                </td>
                                                                <td width="18%">
                                                                    <html:text property="city" styleClass="textbox" disabled="<%=modeValue%>" onchange="dataChanged()" maxlength="30" />
                                                                </td>
                                                                <td width="15%" align="right" valign="middle" class="copybold">
                                                                    <bean:message key="newRolodexEntryLabel.County" bundle="proposal"/>:
                                                                </td>
                                                                <td width="15%">
                                                                    <html:text property="county" styleClass="textbox" disabled="<%=modeValue%>" onchange="dataChanged()" maxlength="30" />
                                                                </td>
                                                                <td width="15%"></td>
                                                                <td width="18%"></td>
                                                            </tr>
                                                            <!--Commented for Case#3789 -Add a New Rolodex Entry using CoeusLite interface - Start-->
                                                            <%--tr>
                                                                
                                                                <td width="15%" align="right" height="22" class="copybold">
                                                                    <bean:message key="newRolodexEntryLabel.Country" bundle="proposal"/>:
                                                                </td>
                                                                <td width="18%">                                                                    
                                                                    <html:select name="RolodexInfoForm" styleClass="cltextbox-medium" property="country" disabled="<%=modeValue%>" onchange="showHideState(this)">
                                                                        <html:option value=""><bean:message bundle="proposal" key="generalInfoProposal.pleaseSelect"/></html:option>
                                                                        <html:options collection="countryList" property="code" labelProperty="description" />
                                                                    </html:select>                                                                     
                                                                </td>
                                                                <td width="15%"></td>
                                                                <td width="18%"></td>
                                                                <td width="15%"></td>
                                                                <td width="18%"></td>
                                                            </tr--%>                                                            
                                                        <%--/td>
                                                        <td width="15%" align="right" class="copybold">
                                                            <bean:message key="newRolodexEntryLabel.Phone" bundle="proposal"/>:
                                                        </td> 
                                                        <td width="18%">
                                                            <html:text property="phone" styleClass="textbox" disabled="<%=modeValue%>" onchange="dataChanged()" maxlength="20" />
                                                        </td>
                                                        <td width="15%"></td>
                                                        <td width="18%"></td>
                                                        <td width="15%"></td>
                                                        <td width="18%"></td>
                                                    </tr--%>
                                                    <!--Commented for Case#3789 -Add a New Rolodex Entry using CoeusLite interface- End -->
                                                    <tr>
                                                        <td width="15%" align="right" height="21" class="copybold">
                                                            <bean:message key="newRolodexEntryLabel.Email" bundle="proposal"/>:
                                                        </td>
                                                        <td width="18%">
                                                            <html:text property="email" styleClass="textbox" disabled="<%=modeValue%>" onchange="dataChanged()" maxlength="60" />
                                                        </td>
                                                        <td width="15%" align="right" class="copybold">
                                                            <bean:message key="newRolodexEntryLabel.Phone" bundle="proposal"/>:
                                                        </td> 
                                                        <td width="18%">
                                                            <html:text property="phone" styleClass="textbox" disabled="<%=modeValue%>" onchange="dataChanged()" maxlength="20" />
                                                        </td>
                                                        <td width="15%" align="right" class="copybold">
                                                            <bean:message key="newRolodexEntryLabel.Fax" bundle="proposal"/>:
                                                        </td>
                                                        <td width="18%">
                                                            <html:text property="fax" styleClass="textbox" disabled="<%=modeValue%>" onchange="dataChanged()" maxlength="20" />
                                                        </td>
                                                        <%--td width="15%"></td>
                                                        <td width="18%"></td--%>
                                                    </tr>
                                                    <tr>
                                                        <td align="right" class="copybold">
                                                            <bean:message key="newRolodexEntryLabel.Comments" bundle="proposal"/>:
                                                        </td>
                                                        <td colspan="5">
                                                            <html:textarea property="comments" disabled="<%=modeValue%>" cols="80" rows="2" onchange="dataChanged()"/>
                                                        </td>
                                                        <script>
                                                            if(navigator.appName == "Microsoft Internet Explorer")
                                                            {
                                                                document.RolodexInfoForm.comments.cols=80;
                                                                document.RolodexInfoForm.comments.rows=3;
                                                                document.RolodexInfoForm.organization.style.width=420;
                                                            }
                                                        </script>  
                                                    </tr>
                                                </tbody>
                                            </table>
                                        </td>
                                        <td>&nbsp;</td>
                                        <td>&nbsp;</td>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td height="10">
                                        </td>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                    </tr>
                            </tbody></table>
                        </td>
                    </tr>
                    <tr class="table" style="padding-left: 2px;">
                        <td class="copy" align="left" height="25" nowrap="nowrap" valign="middle">
                            <html:submit property="Save" value="Save" disabled="<%=modeValue%>" styleClass="clsavebutton" />
                        </td>   
                    </tr>
            </tbody></table>
        </td>
    </tr>
</tbody></table>
</td>
</tr>
</tbody>
</table>
<html:hidden property="acType"/>
<html:hidden property="createUser"/>
<html:hidden property="updateTimestamp"/>
<html:hidden property="rolodexId"/>
<html:hidden property="emptyString"/>
<html:hidden property="ownedByUnit" />
<html:hidden property="sponsorAddressFlag" />
<html:hidden property="ownerRight" />
<html:hidden property="getSponsorAddress" />
<html:hidden property="searchFlag" />
</html:form>
<script>
      DATA_CHANGED = 'false';
      var dataModified = 'null';
      var dataModified = '<%=request.getAttribute("dataModified")%>';
      if(dataModified == 'modified' || (errValue && !errLock)){
            DATA_CHANGED = 'true';
      }
      LINK = "<%=request.getContextPath()%>/rolodexInfoSave.do";
      FORM_LINK = document.RolodexInfoForm;
      PAGE_NAME = "Proposal/Add New Rolodex Person";
      function dataChanged(){
        DATA_CHANGED = 'true';
      }
      linkForward(errValue);
</script>
<script>
      //var help = '';
      //help = trim(help);
      //if(help.length == 0) {
      //  document.getElementById('helpText').style.display = 'none';
      //}
      //function trim(s) {
      //      return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
      //} 
</script> 
<%--script>
    var countryCode = document.RolodexInfoForm.country.value;    
    if(countryCode == 'USA'){    
        document.getElementById('divState1').style.display = 'block';
        document.getElementById('divState2').style.display = 'none';
    }else{
        document.getElementById('divState1').style.display = 'none';
        document.getElementById('divState2').style.display = 'block';    
    }
</script--%>
<script>    
    if(document.RolodexInfoForm.country.value == ''){
        document.RolodexInfoForm.country.value = "USA";
    }
    populateState('RolodexInfoForm',document.RolodexInfoForm.country.value,'state',false);
    document.RolodexInfoForm.state.value = '<bean:write name="RolodexInfoForm" property="state"/>';
</script> 
</body>
</html:html>