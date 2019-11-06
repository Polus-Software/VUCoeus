<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page
	import="org.apache.struts.taglib.html.JavascriptValidatorTag,
                org.apache.struts.validator.DynaValidatorForm"%>
<%@ page
	import="java.util.Vector,
                java.util.ArrayList,
                edu.mit.coeus.bean.*,
                edu.mit.coeus.unit.bean.*,
                edu.mit.coeuslite.utils.CoeusLiteConstants,
                 edu.mit.coeuslite.iacuc.bean.IacucProtocolDisclosureDetailsBean,
                edu.mit.coeus.rolodexmaint.bean.*,
                edu.mit.coeus.utils.ComboBoxBean,
                edu.mit.coeus.propdev.bean.ProposalLeadUnitFormBean,
                edu.mit.coeus.user.bean.UserMaintDataTxnBean,
                edu.mit.coeuslite.iacuc.bean.ProtocolHeaderDetailsBean,
                edu.mit.coeus.utils.CoeusConstants,
                java.net.URLEncoder"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
String path = request.getContextPath();
%>



<%@page import="java.sql.Date,java.text.SimpleDateFormat"%>
<jsp:useBean id="vecRolesType" scope="session" class="java.util.Vector" />
<jsp:useBean id="vecAffiliationType" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="iacucPersonsData" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="vecKeyPersonRoles" scope="session"
	class="java.util.Vector" />
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>


<%  String EMPTY_STRING = "";
    String link = EMPTY_STRING;
    boolean hasValidationMessage = false;
     String mode=(String)session.getAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId());
    boolean modeValue=false;
    if(mode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE)){
        modeValue=true;
    }else if(mode.equalsIgnoreCase(CoeusLiteConstants.MODIFY_MODE)){
        modeValue=false;
    }else if(mode.equalsIgnoreCase(CoeusLiteConstants.ADD_MODE)){
        modeValue=false;
    }

    //Added for coeus4.3 Amendments and Renewal enhancement
    String strProtocolNum = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
    if(strProtocolNum == null)
        strProtocolNum = "";
    //Added for case id COEUSQA-2868/COEUSQA-2869 begin
    boolean disableField = false;
    String formShowHide = (String)request.getAttribute("formVisiblility" + strProtocolNum);

    if(formShowHide != null && "inVisible".equals(formShowHide)){
        disableField = true;
    }
    boolean isStudyPerson = false;
    String studyPerson = (String)request.getAttribute("studyPerson" + strProtocolNum);

    if(studyPerson == null){
        studyPerson = (String)request.getParameter("studyPerson" + strProtocolNum);
    }
    if(studyPerson != null && "true".equals(studyPerson)){
        isStudyPerson = true;
    }


    boolean isInvestig = false;
    String isInvesOrCoinves = (String)request.getAttribute("isInvesOrCoinves" + strProtocolNum);

    if(isInvesOrCoinves == null){
        isInvesOrCoinves = (String)request.getParameter("isInvesOrCoinves" + strProtocolNum);
    }
    if(isInvesOrCoinves != null && "true".equals(isInvesOrCoinves)){
        isInvestig = true;
    }

    //Added for case id COEUSQA-2868/COEUSQA-2869 end
    //Modified for internal issue#142 : While creating a Amendment protocol Unable to Edit
    String amendRenewPageMode = (String)session.getAttribute("amendRenewPageMode"+request.getSession().getId());
    if(strProtocolNum.length() > 10 && (amendRenewPageMode == null ||
            amendRenewPageMode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE))){
        modeValue = true;
    }   
    int notifFlag = -1;
    if(request.getAttribute(CoeusConstants.ENABLE_IACUC_PERSONNEL_NOTIFICATION)!=null){
     notifFlag=Integer.parseInt(request.getAttribute(CoeusConstants.ENABLE_IACUC_PERSONNEL_NOTIFICATION).toString());
       }
%>

<html:html locale="true">
<head>
<style>
#mbox {
	background-color: #6e97cf;
	padding: 0px 8px 8px 8px;
	border: 3px solid #095796;
}

#mbm {
	font-family: sans-serif;
	font-weight: bold;
	float: right;
	padding-bottom: 5px;
}

#ol {
	background-image:
		url('../coeuslite/mit/utils/scripts/modal/overlay.png');
}

.dialog {
	display: none
}

* html #ol {
	background-image: none;
	filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src="../coeuslite/mit/utils/scripts/modal/overlay.png",
		sizingMethod="scale");
}

.deleteRow {
	font-weight: bold;
	color: #CC0000;
	background-color: white;
}

.addRow {
	font-weight: bold;
	background-color: white;
}

.rowHeight {
	height: 25px;
}

.cltextbox-medium {
	width: 160px;
}

.cltextbox-color {
	width: 160px;
	font-weight: normal;
}

.textbox {
	width: 160px;
	font-weight: normal;
}

.cltextbox-nonEditcolor {
	width: 160px;
	font-weight: normal;
}
</style>
<title>CoeusLite</title>
<script language="javascript" type="text/JavaScript"
	src="..\scripts\_js.js"></script>
<script language="javascript" type="text/JavaScript"
	src="..\scripts\CheckForward.js"></script>
<script
	src="<%=request.getContextPath()%>/coeuslite/mit/utils/scripts/modal/modaldbox.js"></script>
<script language="JavaScript" type="text/JavaScript">
        
     // Added for COEUSQA_2633 Ability to indicate which procedures IACUC study personnel will perform and training qualifications for species_start
    //load person id into the array
    var personNameId = new Array();
    <%if(iacucPersonsData != null && iacucPersonsData.size()>0){
            for(int index=0;index<iacucPersonsData.size();index++){
                org.apache.struts.action.DynaActionForm investiKeyPersonForm =
                        (org.apache.struts.action.DynaActionForm)iacucPersonsData.get(index);%>
                personNameId[<%=index%>] = '<%=(String)investiKeyPersonForm.get("personId")%>';
       <% }
        }
       %>
    // Added for COEUSQA_2633 Ability to indicate which procedures IACUC study personnel will perform and training qualifications for species_end
    var searchSelection = "";
    var errValue = false;
    var errLock = false;
    var selectedValue=0;
     function searchWindow(value) {
        searchSelection = value;
        var winleft = (screen.width - 650) / 2;
        var winUp = (screen.height - 450) / 2;
        var win = "scrollbars=1,resizable=1,width=750,height=450,left="+winleft+",top="+winUp;
        if(value == 'person'){
            sList = window.open('<%=request.getContextPath()%>/iacucSearch.do?type=<bean:message bundle="iacuc"  key="searchWindow.title.person"/>&search=true&searchName=WEBPERSONSEARCH', "list", win);
        }else if (value == 'rolodex'){
            sList = window.open('<%=request.getContextPath()%>/iacucSearch.do?type=<bean:message bundle="iacuc"  key="searchWindow.title.rolodex"/>&search=true&searchName=WEBROLODEXSEARCH', "list", win);
        } else if(value == 'unit') {
        sList = window.open('<%=request.getContextPath()%>/iacucSearch.do?type=<bean:message bundle="iacuc"  key="searchWindow.title.unit"/>&search=true&searchName=WEBLEADUNITSEARCH', "list", win);
        }
      }

      function fetch_Data(result){
        var name="";
        var flag="";
        var personId="";
        var phone="";

        dataChanged();
        document.iacucInvesKeyPersons.departmentNumber.value = '';
        document.iacucInvesKeyPersons.departmentName.value = '';
        if(searchSelection == 'unit') {
            if(result["UNIT_NUMBER"]!='null' && result["UNIT_NUMBER"]!= undefined){
                document.iacucInvesKeyPersons.departmentNumber.value = result["UNIT_NUMBER"];
            }
            if(result["UNIT_NAME"]!='null' && result["UNIT_NAME"]!= undefined){
                document.iacucInvesKeyPersons.departmentName.value = result["UNIT_NAME"];
            }
        } else {
            document.iacucInvesKeyPersons.acType.value = "I";
            document.iacucInvesKeyPersons.principleInvestigatorFlag.value = '';
            if(searchSelection == 'person'){
                flag = "N";
                if(result["FULL_NAME"]!="null" && result["FULL_NAME"]!= undefined){
                    name=result["NAME"];
                }else{
                    name='';
                }
                if(result["PERSON_ID"]!='null' && result["PERSON_ID"]!= undefined){
                    personId = result["PERSON_ID"];
                }else{
                    personId='';
                }
                if(result["OFFICE_PHONE"]!='null' && result["OFFICE_PHONE"]!= undefined){
                    phone = result["OFFICE_PHONE"];
                }else{
                    phone='';
                }
                if(result["HOME_UNIT"]!='null' && result["HOME_UNIT"]!= undefined){
                    document.iacucInvesKeyPersons.departmentNumber.value = result["HOME_UNIT"];
                }else{
                    document.iacucInvesKeyPersons.departmentNumber.value = '';
                }
                if(result["UNIT_NAME"]!='null' && result["UNIT_NAME"]!= undefined){
                    document.iacucInvesKeyPersons.departmentName.value = result["UNIT_NAME"];
                }else{
                    document.iacucInvesKeyPersons.departmentName.value = '';
                }
                if(result["FAX_NUMBER"]!='null' && result["FAX_NUMBER"]!= undefined){
                    document.iacucInvesKeyPersons.faxNumber.value = result["FAX_NUMBER"];
                }else{
                    document.iacucInvesKeyPersons.faxNumber.value = '';
                }
                if(result["MOBILE_PHONE_NUMBER"]!='null' && result["MOBILE_PHONE_NUMBER"]!= undefined){
                    document.iacucInvesKeyPersons.mobileNumber.value = result["MOBILE_PHONE_NUMBER"];
                }else{
                    document.iacucInvesKeyPersons.mobileNumber.value = '';
                }
            } else if (searchSelection == 'rolodex') {
                flag = "Y";
                if(result["LAST_NAME"]!="null" && result["LAST_NAME"]!= undefined){
                    name=result["LAST_NAME"]+", ";
                }
                if(result["FIRST_NAME"]!="null" && result["FIRST_NAME"]!= undefined){
                    name+=result["FIRST_NAME"];
                }
                if(result["ROLODEX_ID"]!='null' && result["ROLODEX_ID"]!= undefined){
                    personId = result["ROLODEX_ID"];
                }else{
                    personId = '';
                }
                if(name=='null' || name== undefined || name.length==0){
                    name = result["ORGANIZATION"];
                }
                if(result["PHONE_NUMBER"]!='null' && result["PHONE_NUMBER"]!= undefined){
                    phone = result["PHONE_NUMBER"];
                }else{
                    phone = '';
                }
                if(result["TITLE"]!='null' && result["TITLE"]!= undefined){
                    document.iacucInvesKeyPersons.personRole.value = result["TITLE"];
                }else{
                    document.iacucInvesKeyPersons.personRole.value = '';
                }
                if(result["FAX_NUMBER"]!='null' && result["FAX_NUMBER"]!= undefined){
                    document.iacucInvesKeyPersons.faxNumber.value = result["FAX_NUMBER"];
                }else{
                    document.iacucInvesKeyPersons.faxNumber.value = '';
                }
            }
            document.iacucInvesKeyPersons.email.value = '';
            if(result["EMAIL_ADDRESS"]!='null' && result["EMAIL_ADDRESS"]!= undefined){
                document.iacucInvesKeyPersons.email.value = result["EMAIL_ADDRESS"];
            }else{
                document.iacucInvesKeyPersons.email.value = '';
            }
            document.iacucInvesKeyPersons.mobileNumber.value = '';
            document.iacucInvesKeyPersons.personName.value = name;
            document.iacucInvesKeyPersons.personId.value = personId;
            document.iacucInvesKeyPersons.phone.value = phone;
            document.iacucInvesKeyPersons.nonEmployeeFlag.value = flag;
            document.iacucInvesKeyPersons.acType.value = 'N';
            document.iacucInvesKeyPersons.personRole.value = '';
            //document.invesKeyPersons.affiliationTypeCode.value = '';
          //  alert(result["IS_FACULTY"]);
            if(result["IS_FACULTY"]!='null' && result["IS_FACULTY"]!= undefined){
                if(result["IS_FACULTY"]=='Y'){
                document.iacucInvesKeyPersons.affiliationTypeCode.value = '1';   
                }else{
                document.iacucInvesKeyPersons.affiliationTypeCode.value = '3';    
                }
            }else{
            document.iacucInvesKeyPersons.affiliationTypeCode.value = '';    
            }
            //COEUSQA:3162 - Adding investigator/ key person retains info from previous person - Start
            document.iacucInvesKeyPersons.role.value = '';
            //COEUSQA:3162 - End
            document.iacucInvesKeyPersons.action = "<%=request.getContextPath()%>/getIacucInvesKeyPersons.do?operation=N";
            document.iacucInvesKeyPersons.submit();
        }
      }
      function updateData(data, timeStamp, personId,seqNumber, piFlag) {
            // Added for COEUSQA_2633 Ability to indicate which procedures IACUC study personnel will perform and training qualifications for species_start
            var personCount = 0;
            for(var ind=0;ind < personNameId.length;ind++){
                if(personNameId[ind].indexOf(personId) == 0){
                    personCount = personCount+1;
                }
            }
            // Added for COEUSQA_2633 Ability to indicate which procedures IACUC study personnel will perform and training qualifications for species_end
            if(data == 'D' && confirm("<bean:message bundle="iacuc"  key="validation.invesKeypersons.deleteConfirmation"/>")) {
                // Added for COEUSQA_2633 Ability to indicate which procedures IACUC study personnel will perform and training qualifications for species_start
                if(personCount == 1){
                    data = 'DA';
                }
                // Added for COEUSQA_2633 Ability to indicate which procedures IACUC study personnel will perform and training qualifications for species_end
                document.iacucInvesKeyPersons.awUpdateTimestamp.value = timeStamp;
                document.iacucInvesKeyPersons.personId.value = personId;
                 document.iacucInvesKeyPersons.sequenceNumber.value = seqNumber;
                document.iacucInvesKeyPersons.principleInvestigatorFlag.value = piFlag;
                document.iacucInvesKeyPersons.action = "<%=request.getContextPath()%>/getIacucInvesKeyPersons.do?operation="+data;
                document.iacucInvesKeyPersons.submit();
            } else if(data == 'S') {
                document.iacucInvesKeyPersons.action = "<%=request.getContextPath()%>/saveIacucInvesKeyPersons.do?operation="+data;
                document.iacucInvesKeyPersons.submit();
            //Added for case id COEUSQA-2868/COEUSQA-2869 begin
            }else if(data == 'N') {
                alert("<bean:message  bundle="iacuc" key="validation.invesKeypersons.oneInvestigRequired"/>");
            }
          //Added for case id COEUSQA-2868/COEUSQA-2869 end
      }
      function editData(data, timeStamp, personId,seqNumber, piFlag){
            document.iacucInvesKeyPersons.awUpdateTimestamp.value = timeStamp;
            document.iacucInvesKeyPersons.personId.value = personId;
            document.iacucInvesKeyPersons.principleInvestigatorFlag.value = piFlag;
            //Added for case id COEUSQA-2868/COEUSQA-2869 begin
            document.iacucInvesKeyPersons.sequenceNumber.value = seqNumber;
            //Added for case id COEUSQA-2868/COEUSQA-2869 end
            document.iacucInvesKeyPersons.action = "<%=request.getContextPath()%>/editIacucInvesKeyPersons.do?operation="+data;
            document.iacucInvesKeyPersons.submit();
      }

      function showHide(toggleId) {
               // alert(toggleId);
                //1-Show 0- Hide
             if(toggleId == 1){
                document.getElementById('showOrHide').style.display = "none";
                selectedValue=0;
            }else if(toggleId == 0){
                document.getElementById('showOrHide').style.display = "";
                document.getElementById('showInvestigators').style.display = "none";
                selectedValue=1;
            }
          }
          function cancelInvestigators(){
            clearFormData();
            //document.getElementById('showInvestigators').style.display = "";
            //document.getElementById('showOrHide').style.display = "none";
             document.iacucInvesKeyPersons.action = "<%=request.getContextPath()%>/getIacucInvesKeyPersons.do";
             document.iacucInvesKeyPersons.submit();

          }
          function clearFormData(){
            document.iacucInvesKeyPersons.acType.value = 'I';
            document.iacucInvesKeyPersons.personId.value = '';
            document.iacucInvesKeyPersons.personName.value = '';
            document.iacucInvesKeyPersons.email.value = '';
            document.iacucInvesKeyPersons.phone.value = '';
            document.iacucInvesKeyPersons.mobileNumber.value = '';
            document.iacucInvesKeyPersons.faxNumber.value = '';
            document.iacucInvesKeyPersons.departmentNumber.value = '';
            document.iacucInvesKeyPersons.departmentName.value = '';
            document.iacucInvesKeyPersons.role.value = '';
            document.iacucInvesKeyPersons.personRole.value = '';
            document.iacucInvesKeyPersons.affiliationTypeCode.value = '';
          }
          //Added for case id COEUSQA-2868/COEUSQA-2869 begin
          function manageInvestStudyPerson(){

            if((document.iacucInvesKeyPersons.role.value == 0)
            ||(document.iacucInvesKeyPersons.role.value == 1)){
                document.iacucInvesKeyPersons.personRole.value= '';
                document.iacucInvesKeyPersons.personRole.disabled=true;
                document.iacucInvesKeyPersons.departmentNumber.disabled =false;

            }else if(document.iacucInvesKeyPersons.role.value == 2){
                document.iacucInvesKeyPersons.personRole.disabled=false;
                document.iacucInvesKeyPersons.departmentNumber.value= '';
                document.iacucInvesKeyPersons.departmentNumber.disabled =true;
            }
            <!-- COEUSQA-3205 COEUS Lite 4.4.4 IACUC Error - Start -->
            dataChanged();
            <!-- COEUSQA-3205 COEUS Lite 4.4.4 IACUC Error - End -->
          }
          //Added for case id COEUSQA-2868/COEUSQA-2869 end
          <!-- Added for case id COEUSQA-3027 Start -->
          function disablePersonField(){
            if((document.iacucInvesKeyPersons.role.value == 0)
            ||(document.iacucInvesKeyPersons.role.value == 1)
            ||(document.iacucInvesKeyPersons.role.value == "")){
                document.iacucInvesKeyPersons.personRole.value= '';
                document.iacucInvesKeyPersons.personRole.disabled=true;
            }else if(document.iacucInvesKeyPersons.role.value == 2){
                document.iacucInvesKeyPersons.personRole.disabled=false;
            }
          }
 <%--   Function to send notification in iacuc starts--%>
     
function showDisclDialog(divId)
 {
           <logic:empty name="iacucProtoDisclDetails">
                   alert("This protocol has no protocol persons");
          </logic:empty>
          <logic:notEmpty name="iacucProtoDisclDetails">
                width =450; document.getElementById(divId).style.pixelWidth;
                height =150; document.getElementById(divId).style.pixelHeight;
                sm(divId,width,height);
           </logic:notEmpty>
 document.getElementById("mbox").style.left="450";
 document.getElementById("mbox").style.top="250";
 }
 
 
<%if(notifFlag==2){%>
  function showDialog(divId)
 {
           <logic:empty name="iacucPersonsData">
               alert("This protocol has no protocol persons");
          </logic:empty>
          <logic:notEmpty name="iacucPersonsData">
             // alert(divId);
              width =450; document.getElementById(divId).style.pixelWidth;
              height =175; document.getElementById(divId).style.pixelHeight;
              sm(divId,width,height);
              
           </logic:notEmpty>
             document.getElementById("mbox").style.left="450";
             document.getElementById("mbox").style.top="250";
       }
 function  validateSendNotify()
            {
var total="";
var len=document.iacucInvesKeyPersons.check.length;
if(len== undefined){
    var j = 0;
    if(document.getElementById(j).checked){
        total+=document.iacucInvesKeyPersons.check.value;
    }
}
else if(len==0){
    alert("This protocol has no protocol persons");
}
else{
    for(var i=0; i < document.iacucInvesKeyPersons.check.length; i++){
        if(i==0){
        var j = 0;
        if(document.getElementById(j).checked == true){
            total+=document.getElementById(j).value;
            document.iacucInvesKeyPersons.check[j].checked=true;
        }
        }else{
            if(document.iacucInvesKeyPersons.check[i].checked == true){
                total+=document.iacucInvesKeyPersons.check[i].value;
            }
        }    
    }
}
if(total==""){alert("Please Select any User");}
else{
    <%--alert(<%=request.getContextPath()%>);--%>
 document.iacucInvesKeyPersons.action = "<%=request.getContextPath()%>/sendEmailIacuc.do";
 document.iacucInvesKeyPersons.submit();
 if(document.getElementById('something').style.visibility == 'visible'){
     document.getElementById('something').style.visibility = 'hidden';
     document.getElementById('something').style.height = '1px';
 }else{
     document.getElementById('something').style.visibility = 'visible';
     document.getElementById('something').style.height = '50px';
 }
}

}
function selectDeselectAll(value)
{
 var len=document.iacucInvesKeyPersons.check.length;
 if(len==undefined)
     {document.iacucInvesKeyPersons.check.checked=value;
      document.getElementById(0).checked=value;
     }
else{
    for(var i=0; i < len;i++){
   document.iacucInvesKeyPersons.check[i].checked=value;
   document.getElementById(i).checked=value;
    }
    }
}

 function selectThis(i){
//debugger;
if(i==0)
 {
    if(document.iacucInvesKeyPersons.check.checked)
    {
   document.getElementById(0).checked = false;
   document.iacucInvesKeyPersons.check.checked=false;
    }else
    {
   document.getElementById(0).checked = true;
   document.iacucInvesKeyPersons.check.checked=true;
    }
 }
 else
     {
if(document.iacucInvesKeyPersons.check[i].checked)
{  document.getElementById(i).checked = false;
   document.iacucInvesKeyPersons.check[i].checked=false;
}
else
{  document.getElementById(i).checked = true;
   document.iacucInvesKeyPersons.check[i].checked=true;
}

    }
}
<%}%>
 <%--   Function to send notification in iacuc ends--%>
function showDisclDialog(divId)
 {
           <logic:empty name="iacucProtoDisclDetails">
                   alert("This protocol has no protocol persons");
          </logic:empty>
          <logic:notEmpty name="iacucProtoDisclDetails">
                width = document.getElementById(divId).style.pixelWidth;
                height = document.getElementById(divId).style.pixelHeight;
                sm(divId,width,height);
           </logic:notEmpty>
 document.getElementById("mbox").style.left="450";
 document.getElementById("mbox").style.top="250";
 }
    </script>
</head>
<!-- Added for case id COEUSQA-3027 Start -->
<body onload="javascript:disablePersonField()">
	<!-- Added for case id COEUSQA-3027 End -->
	<html:form action="/getIacucInvesKeyPersons">

		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			class="table">
			<script type="text/javascript">
    document.getElementById('txt').innerHTML = '<bean:message bundle="iacuc" key="helpPageTextProtocol.InvestigatorKeyPerson"/>';
    </script>




			<%--  <%try{%>--%>
			<%if(!modeValue) {%>
			<tr valign="top">
				<td align="left" valign="top">
					<table width="99%" border="0" align="center" cellpadding="0"
						cellspacing="0" class="tabtable">
						<tr class='tableheader'>
							<td>
								<table width="100%" border="0" cellpadding="0" cellspacing="0">
									<tr class='tableheader'>
										<td><bean:message bundle="iacuc"
												key="label.invesKeypersons.investigatorKeyPersonnelDetails" />
										</td>
										<%--<logic:present name="iacucProtoDisclDetails">--%>
										<td align="right" style="padding-right: 100px"><a
											href="javascript:showDisclDialog('divDisclosureStaus')">COI
												Disclosure Status</a></td>
										<%--</logic:present>--%>
										<td align="left">
											<%if(notifFlag==1){%> Send Notification <%}else if(notifFlag==2){%>
											<a href="javascript:showDialog('iacucSendNotification')">Send
												Notification</a> <%}%>
										</td>
										<td align="center"><a id="helpPageText"
											href="javascript:showBalloon('helpPageText', 'helpText')">
												<bean:message bundle="iacuc" key="helpPageTextProtocol.help" />
										</a></td>
									</tr>
								</table>
						</tr>
						<tr>
							<td>
								<div id="helpText" class='helptext'>
									<bean:message bundle="iacuc"
										key="helpTextProtocol.InvestigatorKeyPerson" />
								</div>
							</td>
						</tr>
						<tr>
							<td class="copy" align="left">
								<div id="helpText" class='helptext'>
									<bean:message bundle="iacuc"
										key="label.invesKeypersons.investigatorKeyPersonnelName" />
								</div>
							</td>
						</tr>
						<tr>
							<td class="copy" align="left">
								<div id="helpText" class='helptext'>
									<bean:message bundle="iacuc"
										key="label.invesKeypersons.requiredForInvestigators" />
								</div>
							</td>
						</tr>

						<tr>
							<td class="copy" align="left">
								<div id="helpText" class='helptext'>
									<bean:message bundle="iacuc"
										key="label.invesKeypersons.keyPersonRoleRequired" />
								</div>
							</td>
						</tr>

						<%if(!modeValue){%>
						<tr>
							<td class='copy'><font color="red"> <logic:messagesPresent>
										<%hasValidationMessage = true;%>
										<script>errValue = true;
                </script>
										<html:errors header="" footer="" />
									</logic:messagesPresent> <logic:messagesPresent message="true">
										<%hasValidationMessage = true;%>
										<script>errValue = true;</script>
										<html:messages id="message" message="true" property="errMsg">
											<script>errLock = true;</script>
											<li><bean:write name="message" /></li>
										</html:messages>
										<html:messages id="message" message="true"
											property="duplicateInvestigatorRecordsNotAllowed">
											<li><bean:write name="message" /></li>
										</html:messages>
										<html:messages id="message" message="true"
											property="duplicateKeyPersonsRecordsNotAllowed">
											<li><bean:write name="message" /></li>
										</html:messages>
										<html:messages id="message" message="true"
											property="unitNumberRequired">
											<li><bean:write name="message" /></li>
										</html:messages>
										<%-- Added for Case# 3054 -Lite Investigator's Screen - Leave without Saving -Start --%>
										<html:messages id="message" message="true"
											property="noPIInformation" bundle="iacuc">
											<script>errValue = true;</script>
											<li><bean:write name="message" /></li>
										</html:messages>
										<html:messages id="message" message="true"
											property="selectPIInformation" bundle="iacuc">
											<script>errValue = true;</script>
											<li><bean:write name="message" /></li>
										</html:messages>
										<html:messages id="message" message="true"
											property="savePIInformation" bundle="iacuc">
											<script>errValue = true;</script>
											<li><bean:write name="message" /></li>
										</html:messages>
										<%-- Added for Case# 3054 -Lite Investigator's Screen - Leave without Saving -End --%>
									</logic:messagesPresent>
							</font></td>
						</tr>
						<%}%>
						<%if(!modeValue){%>
						<tr>
							<td>
								<div id="showInvestigators">
									<!--Modified for the COEUSQA-3168 : IACUC changes for procedure personnel - start-->
									<!--<a href="javascript:showHide(selectedValue);"><u>Add Investigators / Study Personnel Details<u></a>-->
									<!--<a href="javascript:showHide(selectedValue);"><u>Add Procedure Personnel</u></a>-->
									<%link = "javascript:showHide(selectedValue)";%>
									<html:link href="<%=link%>">
										<u><bean:message bundle="iacuc"
												key="label.invesKeypersons.addInvestigatorKeyPersons" /></u>
									</html:link>
									<!--Modified for the COEUSQA-3168 : IACUC changes for procedure personnel - end-->
								</div>
							</td>
						</tr>
						<%}%>
						<tr>
							<td>
								<div id="showOrHide" style="display: none">
									<table width='100%' cellpadding='0' cellspacing='0'
										align=center class='tabtable'>
										<%--
                <tr class='theader'>
                    <td>
                        <bean:message bundle="iacuc" key="label.invesKeypersons.addInvestigatorKeyPersons"/>:
                    </td>
                </tr>--%>

										<tr>
											<td align='left' colspan='6'>
												<%link = "javaScript:searchWindow('person')";%> <html:link
													href="<%=link%>">
													<u><bean:message bundle="iacuc"
															key="label.invesKeypersons.employeeSearch" /></u>&nbsp;|
                        </html:link> <%link = "javaScript:searchWindow('rolodex')";%>
												<html:link href="<%=link%>">
													<u><bean:message bundle="iacuc"
															key="label.invesKeypersons.otherPersonSearch" /></u>
												</html:link>
											</td>
										</tr>

										<tr>
											<td class='core'>
												<table width='100%' cellpadding='1' cellspacing='0'>
													<tr>
														<td nowrap class="copybold" align="right"><bean:message
																bundle="iacuc" key="label.invesKeypersons.name" />:&nbsp;
														</td>
														<td class="copy"><html:text property="personName"
																styleClass="cltextbox-color" readonly="true"
																onchange="dataChanged()" /></td>
														<td nowrap class="copybold" align="right"><bean:message
																bundle="iacuc" key="label.invesKeypersons.email" />:&nbsp;
														</td>
														<td class="copy"><html:text property="email"
																styleClass="cltextbox-color" readonly="true"
																onchange="dataChanged()" /></td>
														<td nowrap class="copybold" width='5%' align="right">
															<bean:message bundle="iacuc"
																key="label.invesKeypersons.phone" />:&nbsp;
														</td>
														<td class="copy"><html:text property="phone"
																styleClass="cltextbox-color" readonly="true"
																onchange="dataChanged()" /></td>
													</tr>

													<tr>
														<td></td>
														<td></td>
														<td nowrap class="copybold" align="right"><bean:message
																bundle="iacuc" key="investigatorsLabel.mobile" />:&nbsp;
														</td>
														<td class="copy"><html:text property="mobileNumber"
																styleClass="cltextbox-color" readonly="true"
																onchange="dataChanged()" /></td>
														<td nowrap class="copybold" align="right"><bean:message
																bundle="iacuc" key="investigatorsLabel.fax" />:&nbsp;</td>
														<td class="copy"><html:text property="faxNumber"
																styleClass="cltextbox-color" readonly="true"
																onchange="dataChanged()" /></td>
													</tr>

													<tr>
														<td class='copybold' align="right"><bean:message
																bundle="iacuc" key="label.invesKeypersons.unit" />:&nbsp;
														</td>
														<%-- Modified for case id COEUSQA-2868/COEUSQA-2869 begin --%>
														<td class="copy">
															<%if(isStudyPerson || disableField) {%> <html:text
																property="departmentNumber" styleClass="textbox"
																style="width : 70px;" readonly="true"
																onchange="dataChanged()" /> <%}else{%> <html:text
																property="departmentNumber" styleClass="textbox"
																style="width : 70px;" onchange="dataChanged()" /> <%}%> <%if(isStudyPerson || disableField){%>
															<u><bean:message bundle="iacuc"
																	key="label.invesKeypersons.search" /></u> <%}else {%> <%link = "javaScript:searchWindow('unit')";%>
															<html:link href="<%=link%>">
																<u><bean:message bundle="iacuc"
																		key="label.invesKeypersons.search" /></u>
															</html:link> <%}%> <%if(isStudyPerson){
                                        String fieldName = "studyPerson" + strProtocolNum;
                                    %> <input type="hidden"
															name="<%=fieldName%>" value="false" /> <%}%>

														</td>

														<td colspan='4'><html:text property="departmentName"
																styleClass="cltextbox-color" style="width : 480px;"
																readonly="true" onchange="dataChanged()" /></td>
													</tr>

													<tr>
														<td nowrap class="copybold" align="right"><bean:message
																bundle="iacuc" key="label.invesKeypersons.protocolRole" />:&nbsp;
														</td>
														<td class="copy">
															<%-- COEUSQA-3205 COEUS Lite 4.4.4 IACUC Error - Start --%>
															<%-- <html:select name="iacucInvesKeyPersons" property="role" onchange="dataChanged()" disabled="<%=disableField%>"  onchange="manageInvestStudyPerson()" styleClass="cltextbox-medium">--%>
															<html:select name="iacucInvesKeyPersons" property="role"
																disabled="<%=disableField%>"
																onchange="manageInvestStudyPerson()"
																styleClass="cltextbox-medium">
																<%-- COEUSQA-3205 COEUS Lite 4.4.4 IACUC Error - End --%>
																<html:option value="">
																	<bean:message bundle="iacuc"
																		key="correspondents.pleaseSelect" />
																</html:option>
																<html:options collection="vecRolesType" property="code"
																	labelProperty="description" />
															</html:select> <%if(disableField == true){%> <input type="hidden"
															name="role" value="0" /> <%}%>
														</td>
														<td nowrap class="copybold" align="right"><bean:message
																bundle="iacuc" key="label.invesKeypersons.keyPersonRole" />:&nbsp;
														</td>
														<td class="copy">
															<%--  <html:text property="personRole" styleClass="cltextbox-medium" maxlength="60" onchange="dataChanged()"/>
                                --%> <html:select
																name="iacucInvesKeyPersons" property="personRole"
																onchange="dataChanged()" disabled="<%=isInvestig%>"
																styleClass="cltextbox-medium">
																<html:option value="">
																	<bean:message bundle="iacuc"
																		key="correspondents.pleaseSelect" />
																</html:option>
																<html:options collection="vecKeyPersonRoles"
																	property="code" labelProperty="description" />
															</html:select> <%if(isInvestig == true){
                                    String fieldName = "isInvesOrCoinves" + strProtocolNum;%>
															<input type="hidden" name="<%=fieldName%>" value="true" />
															<%}%>
														</td>
														<%-- Modified for case id COEUSQA-2868/COEUSQA-2869 end --%>
														<td nowrap class="copybold" align="right"><bean:message
																bundle="iacuc" key="label.invesKeypersons.affiliation" />:&nbsp;
														</td>
														<td class="copy"><html:select
																name="iacucInvesKeyPersons"
																property="affiliationTypeCode" onchange="dataChanged()"
																styleClass="textbox">
																<html:option value="">
																	<bean:message bundle="iacuc"
																		key="correspondents.pleaseSelect" />
																</html:option>
																<html:options collection="vecAffiliationType"
																	property="code" labelProperty="description" />
															</html:select></td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td height='10'></td>
										</tr>
										<tr class='table'>
											<td class='savebutton'><html:button property="save"
													value="Save" styleClass="clsavebutton"
													onclick="updateData('S')" /> <%-- Added for case id COEUSQA-3160 - Start --%>
												<html:button property="save" styleClass="clsavebutton"
													onclick="cancelInvestigators();">
													<bean:message key="button.label.cancel" />
													<%-- Added for case id COEUSQA-3160 - End --%>
												</html:button></td>
										</tr>
									</table>
								</div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<%}%>

			<tr>
				<td>
					<table width='99%' cellpadding='2' cellspacing='0' class='tabtable'
						align=center>
						<tr>
							<td colspan="7">
								<table width='100%' cellpadding='0' cellspacing='0'
									class='tabtable' align=center>
									<tr class='theader'>
										<td width="24"><bean:message bundle="iacuc"
												key="label.invesKeypersons.personName" /></td>

										<td width='24%'><bean:message bundle="iacuc"
												key="label.invesKeypersons.department" /></td>
										<td width='5%'><bean:message bundle="iacuc"
												key="label.invesKeypersons.leadUnit" /></td>
										<td width='19%'><bean:message bundle="iacuc"
												key="label.invesKeypersons.role" /></td>
										<td width='10%'><bean:message bundle="iacuc"
												key="label.invesKeypersons.affliate" /></td>
										<td width='6%'><bean:message bundle="iacuc"
												key="label.invesKeypersons.training" /></td>

										<%if(!modeValue) {%>
										<td width='6%'>&nbsp;</td>
										<%}%>
									</tr>
									<% String strBgColor = "#DCE5F1"; //BGCOLOR=EFEFEF  FBF7F7
                   int count = 0;
                   int investCount = 0;
                %>
								</table>
						</tr>
						<logic:present name="iacucPersonsData">
							<logic:iterate id="data" name="iacucPersonsData"
								type="org.apache.struts.validator.DynaValidatorForm"
								scope="session">
								<%
                    String piFlag =(String) data.get("principleInvestigatorFlag");
                    if(!"".equals(piFlag)){
                      investCount++;
                    }
                %>

							</logic:iterate>
						</logic:present>


						<logic:present name="iacucPersonsData">
							<logic:iterate id="data" name="iacucPersonsData"
								type="org.apache.struts.validator.DynaValidatorForm"
								scope="session">

								<%

                    String timeStamp =(String) data.get("updateTimestamp");
                    String personId = (String) data.get("personId");
                    String piFlag =(String) data.get("principleInvestigatorFlag");
                    String role = (String) data.get("role");
                    Integer sequenceNumber = (Integer)data.get("sequenceNumber");




                    if (count%2 == 0)
                        strBgColor = "#D6DCE5";
                    else
                        strBgColor="#DCE5F1";
                %>

								<tr bgcolor="<%=strBgColor%>"
									onmouseover="className='TableItemOn'"
									onmouseout="className='TableItemOff'">
									<% String editLink = "javaScript:editData('U','"+timeStamp+"','"+personId+"','"+sequenceNumber+"','"+piFlag +"')";%>
									<td width='24%' class='copy'>
										<%if(!modeValue) {%> <html:link href="<%=editLink%>">
											<bean:write name="data" property="personName" />
										</html:link> <%} else {%> <bean:write name="data" property="personName" /> <%}%>
									</td>
									<%-- Commented for case id COEUSQA-2868/COEUSQA-2869 begin --%>
									<%-- <td width='24%' class='copy'>
                        <bean:write name="data" property="departmentName"/>
                    </td>

                    --%>

									<%-- Commented for case id COEUSQA-2868/COEUSQA-2869 end --%>

									<%-- Added for case id COEUSQA-2868/COEUSQA-2869 begin --%>

									<td width='24%' class='copy'>
										<% java.util.ArrayList vecInvUnits = (ArrayList)data.get("investigatorUnits");

                      String deptName= "";
                      for(int index=0 ; index<vecInvUnits.size() ; index++){
                          org.apache.struts.validator.DynaValidatorForm unitForm = (org.apache.struts.validator.DynaValidatorForm)vecInvUnits.get(index);
                          deptName = (String)unitForm.get("departmentName");%>
										<%=deptName%> <%}%>

									</td>
									<%-- Added for case id COEUSQA-2868/COEUSQA-2869 end--%>

									<td width='5%' class='copy' align=center><logic:equal
											name="data" property="principleInvestigatorFlag" value="Y">
											<img
												src="<bean:write name='ctxtPath'/>/coeusliteimages/checked.gif">
										</logic:equal></td>
									<td width='19%' class='copy'>
										<%
                         if (piFlag==null || piFlag.equals(EMPTY_STRING)) {%>
										<bean:write name="data" property="personRoleDesc" /> <%} else {%>
										<logic:equal name="data" property="principleInvestigatorFlag"
											value="Y">
											<bean:message bundle="iacuc"
												key="label.invesKeypersons.principalInvestigator" />
										</logic:equal> <logic:equal name="data" property="principleInvestigatorFlag"
											value="N">
											<bean:message bundle="iacuc"
												key="label.invesKeypersons.coInvestigator" />
										</logic:equal> <%}%>
									</td>
									<td width='10%' class='copy'><bean:write name="data"
											property="affiliationTypeDescription" /></td>
									<%  String completeImage= request.getContextPath()+"/coeusliteimages/complete.gif";
                        String noneImage= request.getContextPath()+"/coeusliteimages/none.gif";        %>
									<td align='center' width='6%'><logic:equal name="data"
											property="trainingStatusFlag" value="0">
											<html:img src="<%=noneImage%>" />
										</logic:equal> <logic:equal name="data" property="trainingStatusFlag"
											value="1">
											<html:img src="<%=completeImage%>" />
										</logic:equal></td>
									<%-- Modified for case id COEUSQA-2868/COEUSQA-2869 begin--%>

									<%if(!modeValue) {%>
									<td width='6%' align=center class='copy'>
										<%
                                if(investCount > 1 || "".equals(piFlag)){
                                    link = "javaScript:updateData('D','"+timeStamp+"','"+personId+"','"+sequenceNumber+"','"+piFlag+"')";
                                }else{
                                    link = "javaScript:updateData('N','"+timeStamp+"','"+personId+"','"+sequenceNumber+"','"+piFlag+"')";
                                }
                             %> <html:link href="<%=link%>">
											<bean:message bundle="iacuc"
												key="label.invesKeypersons.remove" />
										</html:link>
									</td>

									<%-- Modified for case id COEUSQA-2868/COEUSQA-2869 end--%>
									<%}%>
								</tr>
								<%count++;%>
							</logic:iterate>
						</logic:present>

					</table>
				</td>
			</tr>
		</table>

		<tr>
			<td height='5'><html:hidden property="nonEmployeeFlag" /> <html:hidden
					property="principleInvestigatorFlag" /> <html:hidden
					property="awUpdateTimestamp" /> <html:hidden property="personId" />
				<html:hidden property="acType" /> <%-- Added for case id COEUSQA-2868/COEUSQA-2869 begin--%>

				<html:hidden property="awDepartmentNumber" /> <html:hidden
					property="awUnitUpdateTimestamp" /> <html:hidden
					property="sequenceNumber" /> <%-- Added for case id COEUSQA-2868/COEUSQA-2869 end--%>

			</td>
		</tr>
		</table>
		<% String prtocolNum="";
       ProtocolHeaderDetailsBean headerBean=(ProtocolHeaderDetailsBean)session.getAttribute("iacucHeaderBean");
         String title="";
                Vector disclHeaderDet = (Vector)session.getAttribute("iacucProtoDisclDetails");
                  if(disclHeaderDet != null && disclHeaderDet.size()>0){
          for(int i=0;i<disclHeaderDet.size();i++){
             IacucProtocolDisclosureDetailsBean iacucInf =  (IacucProtocolDisclosureDetailsBean)disclHeaderDet.get(i);
               prtocolNum =  (String)headerBean.getProtocolNumber();
             title = (String)iacucInf.getTitle();

          }
      }




                      %>
		<div id="divDisclosureStaus" class="dialog"
			style="width: 450px; height: 160px; overflow: hidden; position: absolute;">
			<%  strBgColor = "#DCE5F1"; //BGCOLOR=EFEFEF  FBF7F7
             count = 0;%>
			<logic:present name="iacucProtoDisclDetails">
				<table width="450px" height="120px" border="0" cellpadding="0"
					cellspacing="0" class="table" align="center">
					<!-- CertificationQuestion - Start  -->

					<tr>
						<td align="left" valign="top" class="tableheader">COI
							Disclosure Status</td>
					</tr>
					<tr>
						<td>
							<table width="100%" height="7" border="0" cellpadding="1"
								cellspacing="1" class="table">
								<%--<tr><td>&nbsp;</td></tr>--%>
								<tr>
									<td height="10%" width="20%">Project # :</td>
									<td width="80%" align="left"><%=prtocolNum%></td>
								</tr>
								<tr>
									<td width="20%">Title :</td>
									<td width="80%" align="left"><%=title%></td>
								</tr>
								<%-- <tr><td>&nbsp;</td></tr>--%>
							</table>
						</td>
					</tr>


					<tr>
						<td align="center" valign="top">
							<table width="100%" border="0" cellspacing="0" cellpadding="3"
								class="tabtable" align="center">
								<tr>
									<td valign="top" width="33%" class="theader" align="left">Person
										Name</td>
									<%--<td valign="top"  class="theader" align="left">Department</td>--%>
									<td valign="top" width="33%" class="theader" align="left">Role</td>
									<td valign="top" class="theader" align="left">COI
										Disclosure Status</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td>
							<div
								style="height: 65px; width: 100%; background-color: #9DBFE9; overflow-y: scroll; overflow: auto;">
								<table width="100%" border="0" cellspacing="0" cellpadding="3"
									class="tabtable" align="center">
									<% int index = 0;%>
									<logic:iterate id="iacucProtoDisclDetailsList"
										name="iacucProtoDisclDetails">
										<tr bgcolor="<%=strBgColor%>" valign="top"
											onmouseover="className='TableItemOn'"
											onmouseout="className='TableItemOff'">
											<td class="copy" align="left"><logic:notEmpty
													name="iacucProtoDisclDetailsList" property="fullName">
													<bean:write name="iacucProtoDisclDetailsList"
														property="fullName" />
												</logic:notEmpty></td>
											<%--                  <td class="copy" align="left">
                              <logic:notEmpty name="iacucProtoDisclDetailsList" property="unitName">
                      <bean:write name="iacucProtoDisclDetailsList" property="unitName"/>
                              </logic:notEmpty>
                        </td>--%>
											<td class="copy" align="left"><logic:notEmpty
													name="iacucProtoDisclDetailsList" property="role">
													<bean:write name="iacucProtoDisclDetailsList"
														property="role" />
												</logic:notEmpty></td>
											<td class="copy" align="left"><logic:notEmpty
													name="iacucProtoDisclDetailsList" property="description">
													<bean:write name="iacucProtoDisclDetailsList"
														property="description" />
												</logic:notEmpty> <logic:empty name="iacucProtoDisclDetailsList"
													property="description">
                                Not Disclosed
                            </logic:empty></td>
										</tr>

										<%index++;%>

									</logic:iterate>
								</table>
							</div>
						</td>
					</tr>

					<tr class="table">

						<td align="center" colspan="3"><input type="button"
							value="Close" class="clsavebutton"
							onclick="hm('divDisclosureStaus')" /></td>

					</tr>
				</table>
			</logic:present>
		</div>

		<%--   div iacucSendNotification statrs(for iacuc send notification)--%>

		<%if(notifFlag==2){%>
		<div id="iacucSendNotification" class="dialog"
			style="width: 450px; height: 150px; overflow: hidden; position: absolute;">
			<logic:present name="iacucPersonsData">
				<table width="450px" border="0" cellpadding="0" cellspacing="0"
					class="table" align="center">
					<!-- CertificationQuestion - Start  -->

					<tr>
						<td colspan="2" align="left" valign="top">
							<div class="tableheader">Send Notification</div>

						</td>
					</tr>
					<tr>
						<td>
							<table width="100%" height="7" border="0" cellpadding="1"
								cellspacing="1" class="table">
								<%--<tr><td>&nbsp;</td></tr>--%>
								<tr>
									<td height="10%" width="20%">Project # :</td>
									<td width="80%" align="left"><%=prtocolNum%></td>
								</tr>
								<tr>
									<td width="20%">Title :</td>
									<td width="80%" align="left"><%=title%></td>
								</tr>
								<%-- <tr><td>&nbsp;</td></tr>--%>
							</table>
						</td>
					</tr>
					<tr>
						<td align="center" valign="top">
							<table width="100%" border="0" cellspacing="0" cellpadding="3"
								class="tabtable" align="center">
								<tr>


									<td valign="top" width="15%" class="theader" align="center">Select</td>
									<td valign="top" width="50%" class="theader" align="left">Name</td>
									<td valign="top" class="theader" align="left">Last
										Notification</td>
								</tr>
							</table>
							<div
								style="height: 75px; width: 100%; background-color: #9DBFE9; overflow-y: scroll; overflow: auto;">
								<table width="100%" border="0" cellspacing="0" cellpadding="3"
									class="tabtable" align="center">
									<% int index1 = 0;%>
									<logic:iterate id="sendNotificList" name="iacucPersonsData">
										<tr>
											<td class="copy" width="15%" align="center"><input
												type="checkbox" name="check" id="<%=index1%>"
												value="<bean:write name='sendNotificList' property='personId'/>"
												onclick="javascript:selectThis('<%=index1%>')" /></td>
											<td class="copy" width="50%" align="left"><bean:write
													name="sendNotificList" property="personName" /></td>
											<td class="copy" align="left"><bean:write
													name="sendNotificList" property="notificationDate" /></td>
										</tr>
										<%index1++;%>
									</logic:iterate>
								</table>
							</div>
							<table width="100%" border="0" cellspacing="0" cellpadding="3"
								class="tabtable" align="center">
								<tr>
									<td align="left">Select : <a
										href="javascript:selectDeselectAll(true)">All</a> | <a
										href="javascript:selectDeselectAll(false)">None</a></td>
									<td align="center" colspan="2"><input type="button"
										name="sendButton" value="Send" Class="clsavebutton"
										onclick="validateSendNotify();" /> <input type="button"
										value="Close" class="clsavebutton"
										onclick="javascript:selectDeselectAll(false);hm('iacucSendNotification')" />
									</td>

								</tr>
							</table>
						</td>
					</tr>
				</table>
			</logic:present>

			<center>
				<div id="something"
					style="visibility: hidden; overflow: hidden; width: 300px; height: 1px; border: 1px solid black; background-color: #d1e5fe; margin-left: auto; margin-right: auto; float: none; padding-top: 20px;">
					Please wait...!!</div>
			</center>
			<logic:present name="mailSend">
				<logic:equal name="mailSend" value="true">
					<script>alert("All notifications are sent.");</script>
				</logic:equal>
				<logic:equal name="mailSend" value="false">
					<script>alert("Notifications not sent due to missing mail address.");</script>
				</logic:equal>
			</logic:present>

		</div>
		<%}%>
		<%--   div iacucSendNotification end(for iacuc send notification)--%>
		<script> 
      DATA_CHANGED = 'false';
      var dataModified = '<%=request.getAttribute("dataModified")%>';
      if(dataModified == 'modified' || (errValue && !errLock)){
            DATA_CHANGED = 'true';
      }
      LINK = "<%=request.getContextPath()%>/saveIacucInvesKeyPersons.do?operation=S";
      FORM_LINK = document.iacucInvesKeyPersons;
      PAGE_NAME = "<bean:message bundle="iacuc" key="label.invesKeypersons.investigatorKeyPersonnelDetails"/>";
      function dataChanged(){
        DATA_CHANGED = 'true';
      }
      linkForward(errValue);
</script>
		<script>
      var help = '<bean:message bundle="iacuc" key="helpTextProtocol.InvestigatorKeyPerson"/>';
      help = trim(help);
      if(help.length == 0) {
            document.getElementById('helpText').style.display = 'none';
      }
      function trim(s) {
            return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
      }

      if(document.iacucInvesKeyPersons.acType.value == 'U' || document.iacucInvesKeyPersons.acType.value == 'N'){
        showHide(0);
      }
      <%if(hasValidationMessage){%>
        showHide(0);
      <%}%>

</script>
		<script>
              if((document.invesKeyPersons.role.value == 0)
            ||(document.invesKeyPersons.role.value == 1)
            ||(document.invesKeyPersons.role.value == "")){
                document.invesKeyPersons.personRole.value= '';
                document.invesKeyPersons.personRole.disabled=true;
            }else if(document.invesKeyPersons.role.value == 2){
                document.invesKeyPersons.personRole.disabled=false;
            }
      </script>
	</html:form>
</body>
</html:html>