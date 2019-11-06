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
                edu.mit.coeus.rolodexmaint.bean.*,
                edu.mit.coeus.utils.ComboBoxBean,
                edu.mit.coeus.propdev.bean.ProposalLeadUnitFormBean,
                edu.mit.coeus.user.bean.UserMaintDataTxnBean,edu.utk.coeuslite.propdev.bean.ProposalDisclosureStausDetailBean,
                java.net.URLEncoder"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
String path = request.getContextPath();
%>

<%-- page body header information --%>
<jsp:useBean id="proposalNumber" scope="request"
	class="java.lang.String" />
<jsp:useBean id="user" scope="session"
	class="edu.mit.coeus.bean.UserInfoBean" />
<jsp:useBean id="loggedinUser" scope="session" class="java.lang.String" />
<jsp:useBean id="genInfoUpdTimestamp" scope="session"
	class="java.lang.String" />
<jsp:useBean id="createTimestamp" scope="session"
	class="java.lang.String" />
<jsp:useBean id="createUser" scope="session" class="java.lang.String" />
<jsp:useBean id="actionType" scope="request" class="java.lang.String" />
<jsp:useBean id="unitNumber" scope="session" class="java.lang.String" />
<jsp:useBean id="unitName" scope="session" class="java.lang.String" />
<jsp:useBean id="creationStatDesc" scope="session"
	class="java.lang.String" />
<jsp:useBean id="propInvPersonEditableColumns" scope="session"
	class="java.util.HashMap" />
<bean:size id="colSize" name="propInvPersonEditableColumns" />
<%-- end of header information --%>

<jsp:useBean id="investigatorRoles" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="proposalInvKeyData" scope="session"
	class="java.util.Vector" />
<%@page
	import="java.sql.Date,edu.utk.coeuslite.propdev.bean.EPSProposalHeaderBean, java.text.SimpleDateFormat"%>
<jsp:useBean id="epsProposalHeaderBean" scope="session"
	class="edu.utk.coeuslite.propdev.bean.EPSProposalHeaderBean" />
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%
   String flag="";
  flag=(String)session.getAttribute("flag");

  EPSProposalHeaderBean proposalHeaderBean=(EPSProposalHeaderBean)session.getAttribute("epsProposalHeaderBean");

   String Propn =proposalHeaderBean.getProposalNumber();
   String statusCode=proposalHeaderBean.getProposalStatusCode();

   String userid=(String)request.getAttribute("pi_id");
   boolean blnReadOnly = false;
      String mode=(String)session.getAttribute("mode"+session.getId());

      boolean modeValue=false;
      if(mode!=null && !mode.equals("")) {
         if(mode.equalsIgnoreCase("display")){
            modeValue=true;
            blnReadOnly=true;
         }
      }
      DynaValidatorForm formdata = (DynaValidatorForm)request.getAttribute("investigatorForm");
      String certifyRight = (String) session.getAttribute("CERTIFY_RIGHTS_EXIST");
   String viewCertificationRight = (String) session.getAttribute("VIEW_DEPT_PERSNL_CERTIFN");
      //Added for COEUSQA-2037 : Software allows you to delete an investigator who is assigned credit in the credit split window
      int investigatorCount = 0;
      Vector invKeyData = (Vector)session.getAttribute("proposalInvKeyData");
      if(invKeyData != null && invKeyData.size()>0){
          for(int index=0;index<invKeyData.size();index++){
             DynaValidatorForm propInv =  (DynaValidatorForm)invKeyData.get(index);
             String pInvFlag = (String)propInv.get("principalInvestigatorFlag");
             if(pInvFlag!=null && (pInvFlag.equals("Y") || pInvFlag.equals("N"))){
                 investigatorCount++;
             }

          }
      }
      //COEUSQA-2037 : End
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
</style>
<style>
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
</style>
<script
	src="<%=request.getContextPath()%>/coeuslite/mit/utils/scripts/modal/modaldbox.js"></script>
<style>
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

<%--tw......................--%>
<script LANGUAGE="JavaScript">
 function  popUP()
{
var lvar='<%= Propn%>';
tw= window.open("<%=request.getContextPath()%>/sendNotificationMsg.do?proposalNumber="+lvar,'',"menubar=1,resizable=1,width=600,height=325,target=_blank");
tw.focus();

}
function certify()
{
    var lvar='<%= Propn%>';
    document.pdInvestKeyPersForm.action="<%=request.getContextPath()%>/rightPersonCertify.do?proposalNumber="+lvar;
    document.pdInvestKeyPersForm.submit();
}
   function viewCertification()
            {
            var lvar='<%= Propn%>';
                document.pdInvestKeyPersForm.action="<%=request.getContextPath()%>/rightPersonCertify.do?proposalNumber="+lvar;
                document.pdInvestKeyPersForm.submit();
            }

     function viewcertifyppc(lvar,Pid,pNo)
       {
        document.pdInvestKeyPersForm.action="<%=request.getContextPath()%>/toGetRoledQuestionnaire.do?personName="+lvar+"&propPersonId="+Pid+"&proposalNumber="+pNo+"&link=hide";
        document.pdInvestKeyPersForm.submit();
        }

</script>
<%--tw..............................--%>



<script language="JavaScript" type="text/JavaScript">
    var pnFlag;
    var errValue = false;
    var errLock = false;
    function refresh(){
        document.pdInvestKeyPersForm.personId.value="";
        document.pdInvestKeyPersForm.invFirstName.value="";
        document.pdInvestKeyPersForm.invLastName.value="";
        document.pdInvestKeyPersForm.personName.value="";
        document.pdInvestKeyPersForm.invPhone.value="";
        document.pdInvestKeyPersForm.invEmail.value="";
        document.pdInvestKeyPersForm.unitNumber.value="";
        document.pfInvestKeyPersForm.unitName.value="";
        document.pfInvestKeyPersForm.percentageEffort.value="";
        // JM 9-15-2011 added efforts
        document.pfInvestKeyPersForm.academicEffort.value="";
        document.pfInvestKeyPersForm.summerEffort.value="";
        document.pfInvestKeyPersForm.calendarEffort.value="";
        // END
        document.pdInvestKeyPersForm.commonsUserName.value="";
    }

    function insert_data(){
        dataChanged();
         if(document.pdInvestKeyPersForm.facultyFlag.checked){
            document.pdInvestKeyPersForm.facultyFlag.value="Y";
         }else if(!document.pdInvestKeyPersForm.facultyFlag.checked) {
            document.pdInvestKeyPersForm.facultyFlag.value = "N";
         }
    }

    function certifyInvestigator(personId, proposalNum, personName) {


           document.pdInvestKeyPersForm.action = "<%=request.getContextPath()%>/getInvKeyPersonsCertify.do?personId="+personId+"&proposalNumber="+proposalNum+"&personName="+personName;

           document.pdInvestKeyPersForm.submit();

           // var winleft = (screen.width - 650) / 2;
           // var winUp = (screen.height - 450) / 2;
           // var win = "scrollbars=1,resizable=1,width=750,height=450,left="+winleft+",top="+winUp;
           // alert('<%=request.getContextPath()%>/getInvKeyPersonsCertify.do?personId='+personId+'&proposalNumber='+proposalNum);
           // certifyYNQ = window.open('<%=request.getContextPath()%>/getInvKeyPersonsCertify.do?personId='+personId+'&proposalNumber='+proposalNum+'&personName='+personName, "Certify", win);
    }

    function open_search(pnSearch)
          {

            pnFlag = pnSearch;
            var winleft = (screen.width - 650) / 2;
            var winUp = (screen.height - 450) / 2;
            var win = "scrollbars=1,resizable=1,width=750,height=450,left="+winleft+",top="+winUp;
            if(pnSearch){
                sList = window.open('<%=request.getContextPath()%>/generalProposalSearch.do?type=<bean:message key="searchWindow.title.person" bundle="proposal"/>&search=true&searchName=WEBPERSONSEARCH', "list", win);
            }else{
                sList = window.open('<%=request.getContextPath()%>/generalProposalSearch.do?type=<bean:message key="searchWindow.title.person" bundle="proposal"/>&search=true&searchName=WEBROLODEXSEARCH', "list", win);
            }
            if (parseInt(navigator.appVersion) >= 4) {
            window.sList.focus();
            }
          }

      function search(isEmployee)
        {
            if(isEmployee == "Y"){
                document.pdInvestKeyPersForm.is_Employee.value="Y";
                open_search(true);

            }else{
                document.pdInvestKeyPersForm.is_Employee.value = "N";
                open_search(false);
             }
         }

      function unit_search()
      {
      var winleft = (screen.width - 450) / 2;
            var winUp = (screen.height - 350) / 2;
            var win = "scrollbars=yes, resizable=1,width=675,height=360,left="+winleft+",top="+winUp
            sList = window.open('<%=request.getContextPath()%>/generalProposalSearch.do?type=<bean:message key="searchWindow.title.unit" bundle="proposal"/>&search=true&searchName=WEBLEADUNITSEARCH', "list", win);
            if (parseInt(navigator.appVersion) >= 4) {
            window.sList.focus();
            }
      }

       function setDeptNum()
      {
        var strTempNum = document.investigatorForm.invDeptNumber.value;
        if (isInteger(strTempNum)==false) {
          document.investigatorForm.invDeptNumber.focus();
          return;
        }
        if (strTempNum.length == 4) {
          document.investigatorForm.invDeptNumber.value = "00" + strTempNum;
        }
        else if(strTempNum.length == 5) {
          document.investigatorForm.invDeptNumber.value = "0" + strTempNum;
        }else if(strTempNum.length > 0 && strTempNum.length < 4) {
          document.investigatorForm.invDeptNumber.focus();
          return;
        }
        return;
       }

      function isInteger(s)
      {
        var i;
        for (i = 0; i < s.length; i++)
        {
            // Check that current character is number.
            var c = s.charAt(i);
            if (((c < "0") || (c > "9"))) return false;
        }
        // All characters are numbers.
        return true;
      }

      function delete_data(data,personId,timestamp,invKeyRole,isCreditSplitExists){
        var invCount = <%=investigatorCount%>;
        if (confirm("<bean:message bundle="proposal" key="proposalInvKeyDelete.error.confirmation"/>")==true){
          if(invCount >1 && isCreditSplitExists == "Y"){
                if(confirm("<bean:message bundle="proposal" key="proposalInvCreditSplit.error.exists"/>") ==true){
                    document.pdInvestKeyPersForm.action = "<%=request.getContextPath()%>/getCreditSplitInfo.do?invKeyRoleCode="+invKeyRole;
                    document.pdInvestKeyPersForm.submit();
                }
          }else{
            document.pdInvestKeyPersForm.acType.value = data;
            document.pdInvestKeyPersForm.personId.value = personId;
            document.pdInvestKeyPersForm.propInvTimestamp.value = timestamp;
            document.pdInvestKeyPersForm.invLastName.value = "a ";
            document.pdInvestKeyPersForm.invFirstName.value = "b ";
            document.pdInvestKeyPersForm.unitNumber.value = "c ";
            document.pdInvestKeyPersForm.personName.value = "d ";
            document.pdInvestKeyPersForm.invRoleCode.value = invKeyRole;
            document.pdInvestKeyPersForm.action = "<%=request.getContextPath()%>/propdevInvestigatorKeyPerson.do?invKeyRoleCode="+invKeyRole;
            document.pdInvestKeyPersForm.submit();
          }
        }
     }


     function edit_data(data,personId,timestamp,invKeyRole,user){
          document.pdInvestKeyPersForm.acType.value = data;
          document.pdInvestKeyPersForm.personId.value = personId;
          document.pdInvestKeyPersForm.awUpdateTimestamp.value = timestamp;
          document.pdInvestKeyPersForm.propInvUpdateUser.value = user;
          document.pdInvestKeyPersForm.invRoleCode.value = invKeyRole;
          document.pdInvestKeyPersForm.action = "<%=request.getContextPath()%>/editInvKeyPersons.do?role="+invKeyRole;
          document.pdInvestKeyPersForm.submit();
     }


            function fetch_Data(result,searchType){
            dataChanged();
            if(searchType == "Unit Search"){
                document.pdInvestKeyPersForm.unitNumber.value=result["UNIT_NUMBER"];
                if(result["UNIT_NUMBER"]=="null"){
                    document.pdInvestKeyPersForm.unitNumber.value = "";
                } else { document.pdInvestKeyPersForm.unitNumber.value = result["UNIT_NUMBER"] };
                if(result["UNIT_NAME"]=="null"){
                    document.pdInvestKeyPersForm.unitName.value = "";
                } else { document.pdInvestKeyPersForm.unitName.value = result["UNIT_NAME"] };
            }

            if (searchType == "Person Search") {
            //COEUSQA:3162 - Adding investigator/ key person retains info from previous person - Start
             if(document.pdInvestKeyPersForm.acType.value == 'U' || document.pdInvestKeyPersForm.acType.value == 'I') {
                document.pdInvestKeyPersForm.mobileNumber.value = "";
                document.pdInvestKeyPersForm.commonsUserName.value = "";
                document.pdInvestKeyPersForm.unitNumber.value = "";
                document.pdInvestKeyPersForm.unitName.value = "";
                document.pdInvestKeyPersForm.percentageEffort.value = "";
                document.getElementById("academicYearEffort").value = "";
                document.getElementById("summerYearEffort").value = "";
                document.getElementById("calendarYearEffort").value = "";   
                document.getElementById("multiPIFlag").checked = false;
             }
             //COEUSQA:3162 - Ends
                document.pdInvestKeyPersForm.acType.value = 'I';
                if(pnFlag){
                    document.pdInvestKeyPersForm.personId.value = result["PERSON_ID"]
                    if(result["LAST_NAME"]=="null" || result["LAST_NAME"]==""){
                        if(result["ORGANIZATION"]==undefined || result["ORGANIZATION"]=='null'){
                            document.pdInvestKeyPersForm.invLastName.value = result["FULL_NAME"];
                            document.pdInvestKeyPersForm.personName.value = result["FULL_NAME"];
                            document.pdInvestKeyPersForm.commonsUserName.value = result["FULL_NAME"];
                        }
                        else{
                            document.pdInvestKeyPersForm.invLastName.value = result["ORGANIZATION"];
                            document.pdInvestKeyPersForm.personName.value = result["ORGANIZATION"];
                            document.pdInvestKeyPersForm.commonsUserName.value = result["ORGANIZATION"];
                        }
                    }else{
                        if(result["LAST_NAME"]=="null"){document.pdInvestKeyPersForm.invLastName.value = "";}else{document.pdInvestKeyPersForm.invLastName.value = result["LAST_NAME"]};
                        if(result["FIRST_NAME"]=="null"){document.pdInvestKeyPersForm.invFirstName.value = "";}else{document.pdInvestKeyPersForm.invFirstName.value = result["FIRST_NAME"]};
                        if(result["HOME_UNIT"]=="null"){document.pdInvestKeyPersForm.unitNumber.value = "";}else{document.pdInvestKeyPersForm.unitNumber.value = result["HOME_UNIT"]};
                        if(result["UNIT_NAME"]=="null"){document.pdInvestKeyPersForm.unitName.value = "";}else{document.pdInvestKeyPersForm.unitName.value = result["UNIT_NAME"]};
                        if(result["DIRECTORY_TITLE"]=="null"){document.pdInvestKeyPersForm.keyPersRole.value = "";}else{document.pdInvestKeyPersForm.keyPersRole.value = result["DIRECTORY_TITLE"]};
                        if(result["ERA_COMMONS_USER_NAME"]=="null" || result["ERA_COMMONS_USER_NAME"]== undefined  ){document.pdInvestKeyPersForm.commonsUserName.value = "";}
                        else {document.pdInvestKeyPersForm.commonsUserName.value = result["ERA_COMMONS_USER_NAME"]};
                        document.pdInvestKeyPersForm.personName.value = result["FULL_NAME"];
                        document.pdInvestKeyPersForm.facultyFlag.value = result["IS_FACULTY"];
                    }
                    if(result["OFFICE_PHONE"]=="null"){document.pdInvestKeyPersForm.invPhone.value = "";}else{document.pdInvestKeyPersForm.invPhone.value = result["OFFICE_PHONE"]};
                    if(result["EMAIL_ADDRESS"]=="null"){document.pdInvestKeyPersForm.invEmail.value = "";}else{document.pdInvestKeyPersForm.invEmail.value = result["EMAIL_ADDRESS"]};
                    if(result["FAX_NUMBER"]=="null"){document.pdInvestKeyPersForm.faxNumber.value = "";}else{document.pdInvestKeyPersForm.faxNumber.value = result["FAX_NUMBER"]};
                    if(result["MOBILE_PHONE_NUMBER"]=="null"){document.pdInvestKeyPersForm.mobileNumber.value = "";}else{document.pdInvestKeyPersForm.mobileNumber.value = result["MOBILE_PHONE_NUMBER"]};

                }else{
                    document.pdInvestKeyPersForm.acType.value = 'I';
                    document.pdInvestKeyPersForm.personId.value = result["ROLODEX_ID"]
                    if(result["LAST_NAME"]=="null" || result["LAST_NAME"]==""){
                        document.pdInvestKeyPersForm.invLastName.value = result["ORGANIZATION"];
                        document.pdInvestKeyPersForm.personName.value = result["ORGANIZATION"];
                    } else{
                        document.pdInvestKeyPersForm.personName.value = result["LAST_NAME"] + ", " + result["FIRST_NAME"];
                        if(result["LAST_NAME"]=="null"){document.pdInvestKeyPersForm.invLastName.value = "";}else{document.pdInvestKeyPersForm.invLastName.value = result["LAST_NAME"]};
                        if(result["FIRST_NAME"]=="null"){document.pdInvestKeyPersForm.invFirstName.value = "";}else{document.pdInvestKeyPersForm.invFirstName.value = result["FIRST_NAME"]};
                        if(result["TITLE"]=="null"){document.pdInvestKeyPersForm.keyPersRole.value = "";}else{document.pdInvestKeyPersForm.keyPersRole.value = result["TITLE"]};
                    }
                    //alert(result["FAX_NUMBER"]);
                    if(result["PHONE_NUMBER"]=="null"){document.pdInvestKeyPersForm.invPhone.value = "";}else{document.pdInvestKeyPersForm.invPhone.value = result["PHONE_NUMBER"]};
                    if(result["EMAIL_ADDRESS"]=="null"){document.pdInvestKeyPersForm.invEmail.value = "";}else{document.pdInvestKeyPersForm.invEmail.value = result["EMAIL_ADDRESS"]};
                    if(result["FAX_NUMBER"]=="null"){document.pdInvestKeyPersForm.faxNumber.value = "";}else{document.pdInvestKeyPersForm.faxNumber.value = result["FAX_NUMBER"]};
                }
                document.pdInvestKeyPersForm.acType.value = 'N';
                document.pdInvestKeyPersForm.action = "<%=request.getContextPath()%>/propdevInvestigatorKeyPerson.do";
                document.pdInvestKeyPersForm.submit();

            }
        }

     function validateForm(form) {
       insert_data("I");
      // alert('validating');
       if(validatePdInvestKeyPersForm(form)){
           /*if (((form.invRoleCode.value == "0") || (form.invRoleCode.value == "1")) &&
                ((form.unitNumber.value == "null") || (form.unitNumber.value == ""))){
                alert("Unit is required for investigator!");
                form.unitNumber.focus();
                return false;
            }
            if ((form.invRoleCode.value == "2") && ((form.keyPersRole.value == "null") || (form.keyPersRole.value == ""))) {
                alert("Key Person Role is required for Key Pesonnel!");
                form.keyPersRole.focus();
                return false;
            } */
       }else{
            return false;
       }
     }

        function view_comments(personId, personName, piFlag)
        {
          if(piFlag == ""){
             piFlag = "KSP";
          }
          document.pdInvestKeyPersForm.action = "<bean:write name='ctxtPath'/>/getMoreDetails.do?personId="+personId+"&personName="+personName+"&piFlag="+piFlag;
          document.pdInvestKeyPersForm.submit();
         }

         function callDataChanged(sel){
            dataChanged();
            showHideMultiPIFlag(sel);
         }


  function viewcertifyppc(lvar,Pid,pNo)
       {
        document.pdInvestKeyPersForm.action="<%=request.getContextPath()%>/toGetRoledQuestionnaire.do?personName="+lvar+"&propPersonId="+Pid+"&proposalNumber="+pNo+"&link=hide";
        document.pdInvestKeyPersForm.submit();
      }

         function showHideMultiPIFlag(sel){
            var roleId = sel.options[sel.selectedIndex].value;
            if(roleId == '2'){
                document.getElementById('divKeyPersonRole1').style.display = 'block';
                document.getElementById('divKeyPersonRole2').style.display = 'block';
                document.getElementById('divMultiPIFlag1').style.display = 'none';
                document.getElementById('divMultiPIFlag2').style.display = 'none';
                // JM 9-15-2011 switched show and hide toggles to allow efforts for key person
                document.getElementById('divAYE1').style.display = 'block';
                document.getElementById('divAYE2').style.display = 'none';
                document.getElementById('divSYE1').style.display = 'block';
                document.getElementById('divSYE2').style.display = 'none';
                document.getElementById('divCYE1').style.display = 'block';
                document.getElementById('divCYE2').style.display = 'none';
                // END
                // JM 9-15-2011 commented out to allow these fields for key person
                //var domField = document.getElementsByName('academicYearEffort');
                //domField[0].value = 0.0;
                //domField = document.getElementsByName('summerYearEffort');
                //domField[0].value = 0.0;
                //domField = document.getElementsByName('calendarYearEffort');
                //domField[0].value = 0.0;
            }else{
                document.getElementById('divKeyPersonRole1').style.display = 'none';
                document.getElementById('divKeyPersonRole2').style.display = 'none';
                document.getElementById('divMultiPIFlag1').style.display = 'block';
                document.getElementById('divMultiPIFlag2').style.display = 'block';
                document.getElementById('divAYE1').style.display = 'block';
                document.getElementById('divAYE2').style.display = 'none';
                document.getElementById('divSYE1').style.display = 'block';
                document.getElementById('divSYE2').style.display = 'none';
                document.getElementById('divCYE1').style.display = 'block';
                document.getElementById('divCYE2').style.display = 'none';
            }
         }
 function showDialog(divId) 
 {
           <logic:empty name="notificationDetails">
                   alert("This proposal has no proposal persons");
          </logic:empty>
          <logic:notEmpty name="notificationDetails">
                width =450; document.getElementById(divId).style.pixelWidth;
                height =150; document.getElementById(divId).style.pixelHeight;
                sm(divId,width,height);
           </logic:notEmpty>
 document.getElementById("mbox").style.left="450";
 document.getElementById("mbox").style.top="250";
            }
 function showDisclDialog(divId)
 {
           <logic:empty name="propDisclDetails">
                   alert("This proposal has no proposal persons");
          </logic:empty>
          <logic:notEmpty name="propDisclDetails">
                width =450; document.getElementById(divId).style.pixelWidth;
                height =150;document.getElementById(divId).style.pixelHeight;
                sm(divId,width,height);
           </logic:notEmpty>
 document.getElementById("mbox").style.left="450";
 document.getElementById("mbox").style.top="250";
            }

<!-- send Notification START -->
 function  validate()
            {
                //debugger;
                var total="";
                var len=document.pdInvestKeyPersForm.check.length;

              if(len== undefined)
                 {   var j = 0;
                    if(document.getElementById(j).checked)
                      {
                          total+=document.pdInvestKeyPersForm.check.value;
                      }
                 }
                else if(len==0)
                    {
                        alert("This proposal has no proposal persons");
                    }
             else{
                   for(var i=0; i < document.pdInvestKeyPersForm.check.length; i++)
                   {
if(i==0){
  var j = 0;
                    if(document.getElementById(j).checked == true)
                      {
                          total+=document.getElementById(j).value;
                         
                          document.pdInvestKeyPersForm.check[j].checked=true;
                      }
                      
     }else{
         if(document.pdInvestKeyPersForm.check[i].checked == true)
                      {
                       total+=document.pdInvestKeyPersForm.check[i].value;
                       //document.pdInvestKeyPersForm.check[i].checked=value;
                      }
           }    }
                   }

if(total==""){
alert("Please Select any User")
}
else
    {
 document.pdInvestKeyPersForm.action = "<%=request.getContextPath()%>/sendEmail.do";
 document.pdInvestKeyPersForm.submit();
 if(document.getElementById('something').style.visibility == 'visible')
 {     document.getElementById('something').style.visibility = 'hidden';
     document.getElementById('something').style.height = '1px';
 }else
 {document.getElementById('something').style.visibility = 'visible';
     document.getElementById('something').style.height = '50px';
 }
    }
}
    function sendNotification(){
        alert("Entering ");
        document.forms[0].action="<%=request.getContextPath()%>/sendNotificationMsg.do";
        alert("action is"+document.forms[0].action)
        document.forms[0].submit();
    }
function selectDeselectAll(value)
{
 var len=document.pdInvestKeyPersForm.check.length;
 if(len==undefined)
     {document.pdInvestKeyPersForm.check.checked=value;
      document.getElementById(0).checked=value;
     }

else{
    for(var i=0; i < len;i++){   
   document.pdInvestKeyPersForm.check[i].checked=value;
   document.getElementById(i).checked=value;  
                             }
    }
}
function selectThis(i){
//debugger;
if(i==0)
 {
    if(document.pdInvestKeyPersForm.check.checked)
    {
   document.getElementById(0).checked = false;
   document.pdInvestKeyPersForm.check.checked=false;
    }else
    {
   document.getElementById(0).checked = true;
   document.pdInvestKeyPersForm.check.checked=true;
    }
 }
 else
     {
if(document.pdInvestKeyPersForm.check[i].checked)
{  document.getElementById(i).checked = false;
   document.pdInvestKeyPersForm.check[i].checked=false;
}
else
{  document.getElementById(i).checked = true;
   document.pdInvestKeyPersForm.check[i].checked=true;
}

    }
}
    </script>
</head>

<body>


	<html:form action="/propdevInvestigatorKeyPerson.do">

		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			class="table">
			<%--
      <tr>
        <td width="5%" height="25" align="left" valign="top" class="theader">
            <bean:message bundle="proposal" key="proposalInvKeyPersons.invKeyPersInfo"/>
        </td>
      </tr>
      <tr>
        <td align="left" valign="top">
            <font color="red">*</font> <bean:message bundle="proposal" key="generalInfoProposal.mandatory"/>
        </td>
      </tr>

       --%>
			<%
         // String Propn = (String)request.getAttribute("proposalnumber");
          %>
			<% if(!blnReadOnly) {%>
			<tr valign="top">
				<td align="left" valign="top">
					<table width="99%" border="0" align="center" cellpadding="0"
						cellspacing="0" class="tabtable">
						<tr>
							<td align="left" valign="top">
								<table width="100%" border="0" cellpadding="0" cellspacing="0"
									class="tableheader">
									<tr>
										<td><bean:message bundle="proposal"
												key="proposalInvKeyPersons.invKeyDetails" /></td>
									</tr>
								</table>
							</td>
						</tr>
						<%--<tr>
         <td class="copy" align="left">
            <div id="helpText" class='helptext'>
                <bean:message bundle="proposal" key="helpTextProposal.InvestigatorKeyPerson"/>
            </div>
         </td>
      </tr>--%>
						<tr>
							<td class="helptext" align="left"><bean:message
									bundle="proposal" key="proposalInvKeyPersons.invKeySfield" /> <br>
								<bean:message bundle="proposal"
									key="proposalInvKeyPersons.optionalUnit" /> <br> <bean:message
									bundle="proposal" key="proposalInvKeyPersons.optionalRole" /></td>
						</tr>

						<!-- start - display the GLOBAL_MESSAGE  (the "property name" marker to use for global messages) -->
						<tr class='copy' align="left">
							<td><font color="red"> <logic:messagesPresent>
										<script>errValue = true; </script>
										<html:errors header="" footer="" />
									</logic:messagesPresent> <logic:messagesPresent message="true">
										<script>errValue = true; </script>
										<html:messages id="message" message="true"
											property="duplicate" bundle="proposal">
											<bean:write name="message" />
										</html:messages>
										<html:messages id="message" message="true"
											property="error.investigator" bundle="proposal">
											<bean:write name="message" />
										</html:messages>
										<html:messages id="message" message="true"
											property="error.keyPerson" bundle="proposal">
											<bean:write name="message" />
										</html:messages>
										<html:messages id="message" message="true" property="errMsg"
											bundle="proposal">
											<script>errLock = true;</script>
											<li><bean:write name="message" /></li>
										</html:messages>

										<html:messages id="message" message="true"
											property="fundingSrc.error.invalidUnitNo">
											<li><bean:write name="message" /></li>
										</html:messages>

									</logic:messagesPresent>
							</font></td>

						</tr>
						<!-- end - display the GLOBAL_MESSAGE -->

						<!-- start - display unitNumber validation error message if there is any -->
						<tr class='copybold'>
							<td class="copy"><font color="red"> <logic:messagesPresent
										property="unitNumber">
										<bean:message bundle="proposal"
											key="error.pdInvestKeyPersForm.unitNumber" />
									</logic:messagesPresent>
							</font></td>
						</tr>
						<br>
						<!-- end - displaying unitNumber validation error message -->

						<!-- Add Investigator Information: - Start  -->
						<tr>
							<td width='100%' align="left" valign="top"><table
									width="100%" border="0" align="center" cellpadding="0"
									cellspacing="0" class="tabtable">
									<%--<tr class="theader">
            <td colspan="4" align="left" valign="top"> <bean:message bundle="proposal" key="proposalInvKeyPersons.AddInvInfo"/>:</td>
          </tr>--%>
									<tr>
										<td class="copy">
											<table border="0" cellspacing='3' cellpadding="0">

												<tr>
													<td align=left colspan='6'>
														<%if(!modeValue){%> <a href="javascript:search('Y');"
														class="copy"> <u><bean:message bundle="proposal"
																	key="proposalInvKeyPersons.employeeSearch" /></u></a>&nbsp;| <%}%>
														<%if(!modeValue){%> <a href="javascript:search('N');"
														class="copy"> <u><bean:message bundle="proposal"
																	key="proposalInvKeyPersons.otherPersonSearch" /></u></a> <%}%>
													</td>
												</tr>
												<tr>
													<td nowrap class="copybold"><bean:message
															bundle="proposal" key="proposalInvKeyPersons.name" />:</td>
													<td class="copy">
														<%  String objValue = formdata==null ? "" : (String)formdata.get("personName");%>
														<html:text property="personName"
															styleClass="cltextbox-nonEditcolor"
															disabled="<%=modeValue%>" readonly="true" />
													</td>
													<td nowrap class="copybold"><bean:message
															bundle="proposal" key="proposalInvKeyPersons.email" />:</td>
													<td class="copy">
														<%  objValue = formdata==null ? "" : (String)formdata.get("invEmail");
                            String invEmailValue = (String) propInvPersonEditableColumns.get("invEmail");
                            if(invEmailValue != null) { %> <html:text
															property="invEmail" styleClass="textbox"
															disabled="<%=modeValue%>" /> <% } else { %> <html:text
															property="invEmail" styleClass="cltextbox-nonEditcolor"
															disabled="<%=modeValue%>" readonly="true" /> <% } %>
													</td>
													<td nowrap class="copybold"><bean:message
															bundle="proposal" key="proposalInvKeyPersons.phone" />:</td>
													<td class="copy">
														<%  objValue = formdata==null ? "" : (String)formdata.get("invPhone");
                            String invPhoneValue = (String) propInvPersonEditableColumns.get("invPhone");
                            if(invEmailValue != null) { %> <html:text
															property="invPhone" styleClass="textbox"
															disabled="<%=modeValue%>" /> <% } else { %> <html:text
															property="invPhone" styleClass="cltextbox-nonEditcolor"
															disabled="<%=modeValue%>" readonly="true" /> <% } %>
													</td>
												</tr>

												<tr>
													<td nowrap class="copybold"><bean:message
															bundle="proposal"
															key="proposalInvKeyPersons.commonsUserName" />:</td>
													<td align="left">
														<%  objValue = formdata==null ? "" : (String)formdata.get("commonsUserName");
                            String commonsUserNameValue = (String) propInvPersonEditableColumns.get("commonsUserName");
                            if(commonsUserNameValue != null) { %> <html:text
															property="commonsUserName" styleClass="textbox"
															onchange="dataChanged()" disabled="<%=modeValue%>" /> <% } else { %>
														<html:text property="commonsUserName"
															styleClass="cltextbox-nonEditcolor"
															onchange="dataChanged()" disabled="<%=modeValue%>"
															readonly="true" /> <% } %> <!-- Faculty --> <html:hidden
															property="facultyFlag" />
													</td>
													<%--
                    <td nowrap class="copybold" width='5%'>
                        <bean:message bundle="proposal" key="proposalInvKeyPersons.faculty"/>:
                    </td>
                    <td class="copy"  width='30%'>
                        <html:checkbox property="facultyFlag" disabled="<%=modeValue%>" onclick="insert_data()"/>
                    </td>--%>
													<td nowrap class="copybold"><bean:message
															bundle="proposal" key="proposalInvKeyPersons.fax" />:</td>
													<td class="copy">
														<%  objValue = formdata==null ? "" : (String)formdata.get("faxNumber");
                            String faxNumberValue = (String) propInvPersonEditableColumns.get("faxNumber");
                            if(faxNumberValue != null) { %> <html:text
															property="faxNumber" styleClass="textbox"
															disabled="<%=modeValue%>" /> <% } else { %> <html:text
															property="faxNumber" styleClass="cltextbox-nonEditcolor"
															disabled="<%=modeValue%>" readonly="true" /> <% } %>
													</td>
													<td nowrap class="copybold"><bean:message
															bundle="proposal" key="proposalInvKeyPersons.mobile" />:
													</td>
													<td class="copy">
														<%  objValue = formdata==null ? "" : (String)formdata.get("mobileNumber");
                            String mobileNumberValue = (String) propInvPersonEditableColumns.get("mobileNumber");
                            if(mobileNumberValue != null) { %> <html:text
															property="mobileNumber" styleClass="textbox"
															disabled="<%=modeValue%>" /> <% } else { %> <html:text
															property="mobileNumber"
															styleClass="cltextbox-nonEditcolor"
															disabled="<%=modeValue%>" readonly="true" /> <% } %>
													</td>
												</tr>

												<tr>
													<td nowrap class="copybold"><bean:message
															bundle="proposal" key="proposalInvKeyPersons.unitNumber" />:
													</td>
													<td nowrap class="copy" colspan="1">
														<% objValue = formdata==null ? "" : (String)formdata.get("unitNumber");%>
														<%if(session.getAttribute("multipleUnits")== null){%> <html:text
															property="unitNumber" styleClass="textbox"
															onchange="dataChanged()" disabled="<%=modeValue%>"
															readonly="<%=blnReadOnly%>" /> <%if(!modeValue){%> <a
														href="javascript:unit_search();" class="copy"> <u><bean:message
																	bundle="proposal" key="generalInfoProposal.search" /></u></a> <% }%>
														<%}else{%> <html:text property="unitNumber"
															styleClass="textbox" onchange="dataChanged()"
															readonly="true" /> <%if(!modeValue){%> <u><bean:message
																bundle="proposal" key="generalInfoProposal.search" /></u> <% }%>
														<%}%>

													</td>
													<td nowrap class="copy" align='left' colspan="4">
														&nbsp; <%objValue = formdata==null ? "" : (String)formdata.get("unitName");%>
														<html:text property="unitName"
															styleClass="cltextbox-color" readonly="true" /><br>
													</td>
												</tr>

												<tr>
													<td nowrap class="copybold"><bean:message
															bundle="proposal" key="proposalInvKeyPersons.propRole" />:
													</td>
													<td class="copy"><html:select property="invRoleCode"
															styleClass="cltextbox-medium" disabled="<%=modeValue%>"
															onchange="callDataChanged(this)">
															<html:options collection="investigatorRoles"
																property="code" labelProperty="description" />
														</html:select></td>
													<td nowrap class="copybold">
														<div id='divKeyPersonRole1' style='display: none;'>
															<bean:message bundle="proposal"
																key="proposalInvKeyPersons.keyPersRole" />
															:
														</div>
														<div id='divMultiPIFlag1' style='display: block;'>
															<bean:message bundle="proposal"
																key="proposalInvKeyPersons.multiPIFlag" />
														</div>
													</td>
													<td class="copy">
														<div id='divKeyPersonRole2' style='display: none;'>
															<%objValue = formdata==null ? "" : (String)formdata.get("keyPersRole");%>
															<html:text property="keyPersRole"
																styleClass="cltextbox-medium" onchange="dataChanged()"
																disabled="<%=modeValue%>" />
														</div>
														<div id='divMultiPIFlag2' style='display: block;'>
															<html:checkbox property='multiPIFlag'
																styleId="multiPIFlag" value="Y"
																disabled="<%=modeValue%>" onclick="dataChanged()" />
														</div>
													</td>

													<td nowrap class="copybold"><bean:message
															bundle="proposal"
															key="proposalInvKeyPersons.PercentEffort" />:</td>
													<td class="copy">
														<%objValue = formdata==null ? "" : (String)formdata.get("percentageEffort");%>
														<html:text property="percentageEffort"
															styleClass="textbox" onchange="dataChanged()"
															disabled="<%=modeValue%>" />
													</td>
												</tr>
												<!-- Added for case#2270 - Tracking Effort - satrt -->
												<tr>
													<td nowrap class="copybold"><bean:message
															bundle="proposal"
															key="proposalInvKeyPersons.academicEffort" />:</td>
													<td class="copy">
														<div id='divAYE1' style='display: block;'>
															<%objValue = formdata==null ? "" : (String)formdata.get("academicYearEffort");%>
															<html:text property="academicYearEffort"
																styleId="academicYearEffort" styleClass="textbox"
																onchange="dataChanged()" disabled="<%=modeValue%>" />
														</div>
														<div id='divAYE2' style='display: none;'>
															<html:text property="academicYearEffort"
																styleId="academicYearEffort"
																styleClass="cltextbox-nonEditcolor" readonly="true"
																value="0.0" />
														</div>
													</td>
													<td nowrap class="copybold"><bean:message
															bundle="proposal"
															key="proposalInvKeyPersons.summerEffort" />:</td>
													<td class="copy">
														<div id='divSYE1' style='display: block;'>
															<%objValue = formdata==null ? "" : (String)formdata.get("summerYearEffort");%>
															<html:text property="summerYearEffort"
																styleId="summerYearEffort" styleClass="textbox"
																onchange="dataChanged()" disabled="<%=modeValue%>" />
														</div>
														<div id='divSYE2' style='display: none;'>
															<html:text property="summerYearEffort"
																styleId="summerYearEffort"
																styleClass="cltextbox-nonEditcolor" readonly="true"
																value="0.0" />
														</div>
													</td>
													<td nowrap class="copybold"><bean:message
															bundle="proposal"
															key="proposalInvKeyPersons.calendarEffort" />:</td>
													<td class="copy">
														<div id='divCYE1' style='display: block;'>
															<%objValue = formdata==null ? "" : (String)formdata.get("calendarYearEffort");%>
															<html:text property="calendarYearEffort"
																styleId="calendarYearEffort" styleClass="textbox"
																onchange="dataChanged()" disabled="<%=modeValue%>" />
														</div>
														<div id='divCYE2' style='display: none;'>
															<html:text property="calendarYearEffort"
																styleId="calendarYearEffort"
																styleClass="cltextbox-nonEditcolor" readonly="true"
																value="0.0" />
														</div>
													</td>
												</tr>
												<!-- Added for case#2270 - Tracking Effort - end -->

											</table>
										</td>
									</tr>

									<%--
        <!-- start - other custom elements -->
        <tr>
          <td width="100%" align="left" valign="top">
            <table width="100%" border="0" cellpadding="0">
             <tr>
                <td nowrap class="copy" align="left">
                 <html:checkbox property="piAcctSetup" disabled="<%=modeValue%>"/>
                 <bean:message bundle="proposal" key="proposalInvKeyPersons.AccountSetUp"/>
                </td>
             </tr>
            </table>
          </td>
        </tr>

        <tr>
          <td width="100%" align="left" valign="top">
            <table width="100%" border="0" cellpadding="0">
             <tr>
                <td no wrap class="copy" align="left">&nbsp;
                <bean:message bundle="proposal" key="propoaslInvKeyPersons.payType"/>
                </td>
             </tr>
             <tr>
                <td nowrap class="copy" align="left">
                <html:checkbox property="releasePayType" disabled="<%=modeValue%>"/>
                <bean:message bundle="proposal" key="proposalInvKeyPersons.payTypeR"/>
                </td>
             </tr>
             <tr>
                <td nowrap class="copy" align="left">
                <html:checkbox property="summerPayType" disabled="<%=modeValue%>"/>
                <bean:message bundle="proposal" key="proposalInvKeyPersons.payTypeS"/>
                </td>
             </tr>
             <tr>
                <td nowrap class="copy" align="left">
                <html:checkbox property="naPayType" disabled="<%=modeValue%>"/>
                <bean:message bundle="proposal" key="proposalInvKeyPersons.payTypeNA"/>
                </td>
             </tr>
            </table>
          </td>
        </tr>
        <!-- end of other custom elements -->
         --%>

									<%if (!modeValue) {%>
									<tr class='table' style='padding-left: 2px;'>
										<td nowrap class="copy" height='25' align="left" valign=middle>
											<html:submit property="Save" value="Save"
												styleClass="clsavebutton" /> <%--checking for rights--%> <%
                                                    //UserMaintDataTxnBean userTxn = new UserMaintDataTxnBean();
                                                    //boolean isRightExists=userTxn.getUserHasRightInAnyUnit(userid,"MAINTAIN_PERSON_CERTIFICATION");
                                                    //edu.mit.coeus.user.bean.UserMaintDataTxnBean
                                                    if (flag.equalsIgnoreCase("1")) {
                                                        String isRightExists = (String) session.getAttribute("PERSON_CERTIFY_RIGHTS_EXIST");
                                                        String isNotifyRightExists = (String) session.getAttribute("NOTIFY_PROPOSAL_PERSONS");
                                                        String isParent = (String)session.getAttribute("proposalIsParent");
// COEUSDEV-736:
//This checking is added to check whether the proposal is a child proposal and if it is child,disable certify and send notification button.
if (isParent == null || isParent.equals("Y")){
if ((isRightExists != null && !isRightExists.equals("") && isRightExists.equals("YES"))){%>
											<html:button property="Certify" value="Certify"
												styleClass="clsavebutton" onclick="certify()" /> <% } if (isNotifyRightExists != null && !isNotifyRightExists.equals("") && isNotifyRightExists.equals("YES")) {
                                                %> <html:button
												property="Send Notification" value="Send Notification"
												styleClass="clsavebutton"
												onclick="javascript:showDialog('divSendNotification')" /> <%                 }
                                                    }}
                                                %> <%--COEUSDEV-736:if proposal is child , disable certify and send notification ends. --%>


										</td>
										<%-- <td>
                                                 <%= Propn%><html:button property="Send Notification" value="Send Notification" onclick="popUP()" />
                                             </td>--%>

									</tr>
									<% }%>
								</table></td>
						</tr>
					</table>
				</td>
			</tr>
			<%}%>
			<!-- end of if(!blnReadOnly)    -->
			<!--Add Investigator Information: - End   -->
			<!-- List of Investigators  - Start -->


			<%--<tr class='table' style='padding-left: 2px;'>
          <td nowrap class="copy" height='25' align="left" valign=middle>
            <html:submit property="SendNotification" value="SendNotification" styleClass="clsavebutton"/>
          </td>
        </tr>--%>

			<tr>
				<% String isParent = (String)session.getAttribute("proposalIsParent"); %>
				<td align="left" valign="top">

					<table width="99%" border="0" align="center" cellpadding="2"
						cellspacing="0" class="tabtable">
						<tr>
							<td align="left" valign="top"><table width="100%" border="0"
									cellpadding="0" cellspacing="0" class="tableheader">
									<tr>
										<td><bean:message bundle="proposal"
												key="proposalInvKeyPersons.LstOfInvKey" /></td>
										<td align="right" style="padding-right: 100px">
											<%--COEUSDEV-736:
This checking is added to check whether the proposal is a child proposal and if it is child,disable View Certification link. --%>
											<%if (isParent == null || isParent.equals("Y")){ %> <%  if (flag.equalsIgnoreCase("1") && viewCertificationRight != null && (viewCertificationRight.equals("YES"))) {%>
											<a href="javascript:viewCertification()">View
												Certification</a> <%}}%> <%--COEUSDEV-736:View Certification end--%>
										</td>
										<%--COEUSDEV-736:Disclosure Status starts--%>
										<%-- <logic:present name="propDisclDetails">--%>
										<td align="right" style="padding-right: 100px"><a
											href="javascript:showDisclDialog('divDisclosureStaus')">COI
												Disclosure Status</a></td>
										<%-- </logic:present>--%>
										<%--COEUSDEV-736:Disclosure Status end--%>

									</tr>
								</table></td>
						</tr>
						<tr align="center">
							<td>
								<table width="100%" align="center" border="0" cellpadding="0"
									cellspacing='0' class="tabtable">
									<tr>
										<td width="10%" align="left" class="theader"><bean:message
												bundle="proposal" key="proposalInvKeyPersons.Name" /></td>
										<td width="10%" align="left" class="theader"><bean:message
												bundle="proposal" key="proposalInvKeyPersons.Department" /></td>
										<td width="3%" align="center" class="theader"><bean:message
												bundle="proposal"
												key="proposalInvKeyPersons.column.leadUnit" /></td>
										<td width="3%" align="center" class="theader"><bean:message
												bundle="proposal" key="proposalInvKeyPersons.column.multiPI" /></td>
										<td width="10%" align="left" class="theader"><bean:message
												bundle="proposal" key="proposalInvKeyPersons.role" /></td>
										<%--<td width="5%" align="left" class="theader"><bean:message bundle="proposal" key="proposalInvKeyPersons.faculty"/></td>--%>
										<td colspan="4" width="20%" align="center" class="theader"><bean:message
												bundle="proposal" key="proposalInvKeyPersons.PercentEffort" /></td>
										<%--<td width="5%" align="left" class="theader"><bean:message bundle="proposal" key="proposalInvKeyPersons.AccountSetUp"/></td>--%>
										<%--if(!modeValue){--%>
										<td width="5%" align="center" class="theader">&nbsp;</td>
										<%if(!flag.equalsIgnoreCase("1")){%>
										<td width="5%" align="center" class="theader">&nbsp;</td>
										<%--}--%>
										<%} if(flag.equalsIgnoreCase("1")){%>
										<%-- Added for COEUSDEV-736:
 This checking is added to check whether  the proposal is a child proposal and if it is child,disable certify link. --%>
										<% if (isParent == null || isParent.equals("Y")){%>
										<td width="6%" align="center" class="theader">Certify</td>
										<%}else {%>
										<%--COEUSDEV-736:end--%>
										<td width="6%" align="center" class="theader">&nbsp;</td>
										<%}} else{%>
										<td width="6%" align="center" class="theader">&nbsp;</td>
										<%}%>
										<td width="5%" align="center" class="theader">&nbsp;</td>
									</tr>
									<tr>
										<td width="10%" align="left" class="theader">&nbsp;</td>
										<td width="10%" align="left" class="theader">&nbsp;</td>
										<td width="3%" align="center" class="theader">&nbsp;</td>
										<td width="3%" align="center" class="theader">&nbsp;</td>
										<td width="10%" align="left" class="theader">&nbsp;</td>
										<td width="3%" align="center" class="theader"><bean:message
												bundle="proposal"
												key="proposalInvKeyPersons.column.totalEffort" /></td>
										<td width="3%" align="center" class="theader"><bean:message
												bundle="proposal"
												key="proposalInvKeyPersons.column.academicYearEffort" /></td>
										<td width="3%" align="center" class="theader"><bean:message
												bundle="proposal"
												key="proposalInvKeyPersons.column.summerYearEffort" /></td>
										<td width="3%" align="center" class="theader"><bean:message
												bundle="proposal"
												key="proposalInvKeyPersons.column.calendarYearEffort" /></td>
										<td width="5%" align="center" class="theader">&nbsp;</td>
										<%if(!flag.equalsIgnoreCase("1")){%>
										<td width="5%" align="center" class="theader">&nbsp;</td>
										<% } %>
										<td width="6%" align="center" class="theader">&nbsp;</td>
										<td width="5%" align="center" class="theader">&nbsp;</td>
									</tr>



									<% String strBgColor = "#DCE5F1"; //BGCOLOR=EFEFEF  FBF7F7
                          int count = 0;
                          
                           
                           %>
									<logic:present name="proposalInvKeyData">
										<logic:iterate id="propInvBean" name="proposalInvKeyData"
											type="org.apache.struts.validator.DynaValidatorForm">
											<% if (count%2 == 0)
                            strBgColor = "#D6DCE5";
                         else
                            strBgColor="#DCE5F1";
 
                       
                       
                      
                        %>

											<%  //System.out.println("Hello=>personId=>"+propInvBean.get("personId"));%>



											<% String piFlag = (String)propInvBean.get("principalInvestigatorFlag");
                      String multiPIFlag = (String)propInvBean.get("multiPIFlag"); %>


											<%
												
																
																//Malini:4/5/16
																
																String fontClass = "copy";
																String fontColor = "color:black";
																String isExternalFlag = (String) propInvBean.get("isExternal");
																String status = (String) propInvBean.get("status");
																if(status==null){
																		fontClass = "copyExternalPerson";
																		fontColor = "color:blue";																		
																	}
																else if (status.trim().equals("I")&&isExternalFlag.equals("N")) {
																	fontClass = "copyInactivePerson";
																	fontColor = "color:red";
																} else if (status.trim().equals("A")&& isExternalFlag.equals("N")) {
																	fontClass = "copy";
																	fontColor = "color:black";
																} else if(status.trim().equals("I")&&isExternalFlag.equals("Y")){
																	fontClass = "copyInactivePerson";
																	fontColor = "color:red";
																}else if(status.trim().equals("")&&isExternalFlag.equals("Y"))  {
																	fontClass = "copyExternalPerson";
																	fontColor = "color:blue";
																}else{
																	fontClass = "copyExternalPerson";
																	fontColor = "color:blue";
																}
																
									
																						
															//End Malini
											%>



											<%
												String invKeyRole = "2"; // default to key person role
																if (piFlag != null && (piFlag.equals("Y") || piFlag.equals("N"))) {
																	invKeyRole = "1"; // default to co-PI
																	if (piFlag.equals("Y")) {
																		invKeyRole = "0"; // set to be PI but when pass it to delete_data (javascription function), the 'value'
																							// will not be set to "0"; must use special check in InvKeyPersonAction (Server side)
																	}
																}
											%>

											<tr bgcolor="<%=strBgColor%>" valign="top"
												onmouseover="className='TableItemOn'"
												onmouseout="className='TableItemOff'">


												<td class="<%=fontClass%>" align="left">
													<%if(!modeValue){%> <a style="<%=fontColor%>"
													href="javascript:edit_data('E','<%=propInvBean.get("personId")%>','<%=propInvBean.get("propInvTimestamp")%>','<%=invKeyRole%>','<%=propInvBean.get("propInvUpdateUser")%>')">
														<%=propInvBean.get("personName")%>
												</a> <%} else {%> <%=propInvBean.get("personName")%> <%}%>

												</td>
												<td align="left" class="copy">
													<% java.util.ArrayList vecInvUnits = (ArrayList)propInvBean.get("investigatorUnits");
                           String strLeadUnit = "";
                           String strLeadUnitNum = "";
                           boolean isLeadUnit = false;
                           if (vecInvUnits != null&& vecInvUnits.size()>0) {
                               //Added for case#3296 - Lead Unit Check box placement - start
                               Vector vecUnits = new Vector();
                               //Find the unit which is the lead unit and add it at the first in the collection
                               for(int idx=0; idx<vecInvUnits.size(); idx++){
                                   ProposalLeadUnitFormBean propInvUnitsBean =
                                           (ProposalLeadUnitFormBean)vecInvUnits.get(idx);
                                   if(propInvUnitsBean.isLeadUnitFlag()){
                                       vecUnits.add(propInvUnitsBean);
                                       break;
                                   }
                               }
                               //Add the remaining units if any
                               for(int idx=0; idx<vecInvUnits.size(); idx++){
                                   ProposalLeadUnitFormBean propInvUnitsBean =
                                           (ProposalLeadUnitFormBean)vecInvUnits.get(idx);
                                   if(!propInvUnitsBean.isLeadUnitFlag()){
                                       vecUnits.add(propInvUnitsBean);
                                   }
                               }
                               //Added for case#3296 - Lead Unit Check box placement - end
                               for(int index=0 ; index<vecUnits.size() ; index++){
                                ProposalLeadUnitFormBean propInvUnitsBean = (ProposalLeadUnitFormBean)vecUnits.get(index);
                                strLeadUnit = propInvUnitsBean.getUnitName();
                                strLeadUnitNum = propInvUnitsBean.getUnitNumber();
                                isLeadUnit = propInvUnitsBean.isLeadUnitFlag();%>
													<li><%=strLeadUnit%></li> <%}
                      }%>
												</td>
												<td align="center" class="copy">
													<% if (piFlag !=null && piFlag.equals("Y")) { %> <img
													src="<bean:write name='ctxtPath'/>/coeusliteimages/checked.gif">
													<% } else { %> <img
													src="<bean:write name='ctxtPath'/>/coeusliteimages/spacer.gif"
													width="11" height="11"> <% } %>
												</td>
												<td align="center" class="copy">
													<%if(multiPIFlag != null && multiPIFlag.equals("Y")){%> <img
													src="<bean:write name='ctxtPath'/>/coeusliteimages/checked.gif">
													<%}else{%> <img
													src="<bean:write name='ctxtPath'/>/coeusliteimages/spacer.gif"
													width="11" height="11"> <%}%>
												</td>
												<td align="left" class="copy"><logic:equal
														name="propInvBean" property="principalInvestigatorFlag"
														value="Y">
                                   Principal Investigator
                               </logic:equal> <logic:equal name="propInvBean"
														property="principalInvestigatorFlag" value="N">
                                   Co-Investigator
                               </logic:equal> <%String keyPersonRole = (String)propInvBean.get("keyPersRole");
                                String EMPTY_STRING = "";
                                if (keyPersonRole != null && !keyPersonRole.equals(EMPTY_STRING)) { %>
													<%=keyPersonRole%> <%}%></td>
												<%--<td align="center" nowrap class="copy">
                           <logic:equal name="propInvBean" property="facultyFlag" value="Y">
                              <img src="<bean:write name='ctxtPath'/>/coeusliteimages/checked.gif">
                           </logic:equal>
                           <logic:equal name="propInvBean" property="facultyFlag" value="N">
                              <img src="<bean:write name='ctxtPath'/>/coeusliteimages/spacer.gif" width="11" height="11">
                           </logic:equal>
                        </td>--%>

												<td align="center" class="copy"><%=propInvBean.get("percentageEffort")%>
												</td>
												<td align="center" class="copy">
													<%if(propInvBean.get("academicYearEffort") != null){%> <%=propInvBean.get("academicYearEffort")%>
													<%}%>
												</td>
												<td align="center" class="copy">
													<%if(propInvBean.get("summerYearEffort") != null){%> <%=propInvBean.get("summerYearEffort")%>
													<%}%>
												</td>
												<td align="center" class="copy">
													<%if(propInvBean.get("calendarYearEffort") != null){%> <%=propInvBean.get("calendarYearEffort")%>
													<%}%>
												</td>

												<%--
                        <td align="center" nowrap class="copy">
                           <logic:equal name="propInvBean" property="piAcctSetup" value="Y">
                              <img src="<bean:write name='ctxtPath'/>/coeusliteimages/checked.gif">
                           </logic:equal>
                           <logic:equal name="propInvBean" property="piAcctSetup" value="N">
                              <img src="<bean:write name='ctxtPath'/>/coeusliteimages/spacer.gif" width="11" height="11">
                           </logic:equal>
                        </td>
                        --%>
												<td align="center" class="copy">
													<%if(!modeValue){%> <a
													href="javascript:delete_data('D','<%=propInvBean.get("personId")%>','<%=propInvBean.get("propInvTimestamp")%>','<%=invKeyRole%>','<%=propInvBean.get("isCreditSplitExists")%>')">
														<bean:message bundle="proposal" key="upload.remove" />
												</a> <%}%>
												</td>
												<%  String completeImage= request.getContextPath()+"/coeusliteimages/complete.gif";
                             String noneImage= request.getContextPath()+"/coeusliteimages/none.gif";
                             String certifyPersonId = (String)propInvBean.get("personId");
                             String certifyPersonName = (String) propInvBean.get("personName");
                             //If certifyPersonName contains ' use ` instead so that javascript doesn't think its string concatenation character
                             certifyPersonName = certifyPersonName.replace('\'','`');
                              %>
												<%if (flag.equalsIgnoreCase("1")) {
                                          } else {%>

												<td align="center" class="copy">
													<%if (certifyRight != null && (certifyRight.equals("YES") || (modeValue && certifyRight.equals("NO")))) {
                                if (piFlag.equals("Y") || piFlag.equals("N")) {%>
													<a
													href="javascript:certifyInvestigator('<%=propInvBean.get("personId")%>','<%=propInvBean.get("proposalNumber")%>','<%=certifyPersonName%>')">
														<bean:message bundle="proposal" key="proposalInv.certify" />
												</a> <%}
                             } else {%> Certify <%}%>
												</td>
												<%}%>
												<td align="center">
													<%if (flag != null && flag.equalsIgnoreCase("1")) {%> <%--COEUSDEV-736:
 This checking is added to  check whether the proposal is a child proposal and if the proposal is child,disable view  link and check mark. --%>
													<%  if (isParent == null || isParent.equals("Y")){ %> <logic:equal
														name="propInvBean" property="certifyFlag" value="-1">
                                    &nbsp;
                                </logic:equal> <logic:equal name="propInvBean"
														property="certifyFlag" value="0">
														<html:img src="<%=noneImage%>" />
													</logic:equal> <logic:equal name="propInvBean" property="certifyFlag"
														value="1">
														<html:img src="<%=completeImage%>" />
														<% if (flag.equalsIgnoreCase("1") && viewCertificationRight != null && viewCertificationRight.equals("YES")) {%>
														<a
															href="javascript:viewcertifyppc('<%=propInvBean.get("personName")%>','<%=propInvBean.get("personId")%>','<%=propInvBean.get("proposalNumber")%>')">
															<b>View</b>
														</a>
														<% } %>
													</logic:equal> <%} }else {%> <logic:equal name="propInvBean"
														property="certifyFlag" value="-1">
                                    &nbsp;
                            </logic:equal> <logic:equal name="propInvBean"
														property="certifyFlag" value="0">
														<html:img src="<%=noneImage%>" />
													</logic:equal> <logic:equal name="propInvBean" property="certifyFlag"
														value="1">
														<html:img src="<%=completeImage%>" />
													</logic:equal> <%}%> <%--  COEUSDEV-736:modificationd end--%>
												</td>
												<%
                            String viewLink = "javascript:view_comments('"+certifyPersonId+"', '"+certifyPersonName+"', '"+piFlag+"')";
                          %>
												<td align="center" class="copy"><html:link
														href="<%=viewLink%>">
														<bean:message bundle="proposal"
															key="proposalInvKeyPersons.more" />
													</html:link></td>
											</tr>
											<%count++;%>
										</logic:iterate>
									</logic:present>
								</table>
							</td>
						</tr>


					</table>
				</td>
			</tr>
			<!-- List of Investigators  -End -->

			<html:hidden property="acType" />
			<html:hidden property="proposalNumber" />
			<html:hidden property="propInvTimestamp" />
			<html:hidden property="awUpdateTimestamp" />
			<html:hidden property="propInvUpdateUser" />
			<html:hidden property="principalInvestigatorFlag" />
			<html:hidden property="is_Employee" />
			<html:hidden property="invLastName" />
			<html:hidden property="invFirstName" />
			<html:hidden property="personId" />
			<html:hidden property="coiFlag" />
			<html:hidden property="FEDRdebrFlag" />
			<html:hidden property="FEDRdelqFlag" />



		</table>

		<div id="divSendNotification" class="dialog"
			style="width: 450px; height: 150px; overflow: hidden; position: absolute;">

			<logic:present name="notificationDetails">
				<table width="450px" border="0" cellpadding="0" cellspacing="0"
					class="table" align="center">
					<!-- CertificationQuestion - Start  -->

					<tr>
						<td colspan="2" align="left" valign="top">
							<div class="tableheader">Send Notification</div>

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
								style="height: 75px; width: 100%; background-color: #9DBFE9 overflow-y:scroll; overflow: auto;">
								<table width="100%" border="0" cellspacing="0" cellpadding="3"
									class="tabtable" align="center">
									<% int index = 0;%>
									<logic:iterate id="sendNotificList" name="notificationDetails">
										<tr>
											<td class="copy" width="15%" align="center"><input
												type="checkbox" name="check" id="<%=index%>"
												value="<bean:write name='sendNotificList' property='id'/>"
												onclick="javascript:selectThis('<%=index%>')" /></td>
											<td class="copy" width="50%" align="left"><bean:write
													name="sendNotificList" property="name" /></td>
											<td class="copy" align="left"><bean:write
													name="sendNotificList" property="notificationDate" /></td>
										</tr>
										<%index++;%>
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
										onclick="validate()" /> <input type="button" value="Close"
										class="clsavebutton"
										onclick="javascript:selectDeselectAll(false);hm('divSendNotification')" />
									</td>

								</tr>
							</table>

						</td>

					</tr>
				</table>
			</logic:present>


			<center>
				<div id="something"
					style="visibility: hidden; overflow: hidden; width: 300px; height: 1px; border: 1px solid black; background-color: #d1e5fe; margin-left: auto; margin-right: auto; float: none">
					Please wait...!!</div>
			</center>


		</div>
		<%--</html:form>--%>
		<logic:present name="mailSend">
			<logic:equal name="mailSend" value="true">
				<script>alert("All notifications are sent");</script>
			</logic:equal>
			<logic:equal name="mailSend" value="false">
				<script>alert("Notifications not sent due to missing mail address.");</script>
			</logic:equal>
		</logic:present>


		<!-- send Notification STOPT -->
		<% String personId="";
         String title="";
                Vector disclHeaderDet = (Vector)session.getAttribute("propDisclDetails");
                  if(disclHeaderDet != null && disclHeaderDet.size()>0){
          for(int i=0;i<disclHeaderDet.size();i++){
             ProposalDisclosureStausDetailBean propInv =  (ProposalDisclosureStausDetailBean)disclHeaderDet.get(i);
               personId =  (String)propInv.getPersonId();
             title = (String)propInv.getTitle();

          }
      }




                      %>
		<div id="divDisclosureStaus" class="dialog"
			style="width: 450px; height: 150px; overflow: hidden; position: absolute;">

			<logic:present name="propDisclDetails">
				<table width="450px" height="120px" border="0" cellpadding="0"
					cellspacing="0" class="table" align="center">
					<!-- CertificationQuestion - Start  -->

					<tr>
						<td colspan="2" align="left" valign="top" class="tableheader">
							COI Disclosure Status</td>
					</tr>

					<tr>
						<td>
							<table width="100%" height="7" border="0" cellpadding="1"
								cellspacing="1" class="table">
								<%--<tr><td>&nbsp;</td></tr>--%>
								<tr>
									<td height="10%" width="20%">Project # :</td>
									<td width="80%" align="left"><%=Propn%></td>
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
									<td valign="top" width="37%" class="theader" align="left">Person
										Name</td>
									<%--  <td valign="top"  class="theader" align="left">Department</td>--%>
									<td valign="top" class="theader" width="33%" align="left">Role</td>
									<td valign="top" class="theader" align="left">COI
										Disclosure Status</td>
								</tr>

							</table>
						</td>
					</tr>
					<tr>
						<td>
							<div
								style="height: 55px; width: 100%; background-color: #9DBFE9; overflow-y: scroll; overflow: auto;">
								<table width="100%" height="7" border="0" cellpadding="1"
									cellspacing="1" class="table">
									<% int index = 0;%>
									<logic:iterate id="propDisclDetailsList"
										name="propDisclDetails">
										<tr bgcolor="<%=strBgColor%>" valign="top"
											onmouseover="className='TableItemOn'"
											onmouseout="className='TableItemOff'">
											<td class="copy" align="left"><logic:notEmpty
													name="propDisclDetailsList" property="fullName">
													<bean:write name="propDisclDetailsList" property="fullName" />
												</logic:notEmpty></td>
											<%--                  <td class="copy" align="left">
                              <logic:notEmpty name="propDisclDetailsList" property="unitName">
                      <bean:write name="propDisclDetailsList" property="unitName"/>
                              </logic:notEmpty>
                        </td>--%>
											<td class="copy" align="left"><logic:notEmpty
													name="propDisclDetailsList" property="role">
													<bean:write name="propDisclDetailsList" property="role" />
												</logic:notEmpty></td>
											<td class="copy" align="left"><logic:notEmpty
													name="propDisclDetailsList" property="description">
													<bean:write name="propDisclDetailsList"
														property="description" />
												</logic:notEmpty> <logic:empty name="propDisclDetailsList"
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
				</td>
				</tr>
				</table>
			</logic:present>
		</div>







		<script>
      DATA_CHANGED = 'false';
      var dataModified = '<%=request.getAttribute("dataModified")%>';
      if(dataModified == 'modified' || (errValue && !errLock)){
            DATA_CHANGED = 'true';
      }
      if(errValue && !errLock) {
            DATA_CHANGED = 'true';
      }
      LINK = "<%=request.getContextPath()%>/propdevInvestigatorKeyPerson.do";
      FORM_LINK = document.pdInvestKeyPersForm;
      PAGE_NAME = "<bean:message bundle="proposal" key="proposalInvKeyPersons.invKeyDetails"/>";
      function dataChanged(){
        DATA_CHANGED = 'true';
      }
      linkForward(errValue);
</script>
		<%if(!blnReadOnly){%>
		<script>
      //var help = '<bean:message bundle="proposal" key="helpTextProposal.InvestigatorKeyPerson"/>';
      //help = trim(help);
      //if(help.length == 0) {
      //  document.getElementById('helpText').style.display = 'none';
      //}
      //function trim(s) {
      //      return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
      //}
</script>
		<script>
    showHideMultiPIFlag(document.pdInvestKeyPersForm.invRoleCode);
</script>
		<%}%>
		<%session.removeAttribute("multipleUnits");%>
	</html:form>
</body>
</html:html>

 