<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page
	import="org.apache.struts.taglib.html.JavascriptValidatorTag,
               edu.mit.coeuslite.utils.CoeusLiteConstants,
               org.apache.struts.validator.DynaValidatorForm"%>
<%@ page
	import="java.util.Vector,
                java.util.ArrayList,
                edu.mit.coeus.bean.*,
                edu.mit.coeus.unit.bean.*,
                edu.mit.coeus.rolodexmaint.bean.*,
                edu.mit.coeus.utils.ComboBoxBean,
                edu.mit.coeus.propdev.bean.ProposalLeadUnitFormBean,edu.mit.coeuslite.irb.bean.ProtocolHeaderDetailsBean,
                edu.mit.coeus.user.bean.UserMaintDataTxnBean,edu.mit.coeuslite.irb.bean.IrbProtocolDisclosureStatusBean,
                edu.mit.coeus.utils.CoeusConstants,
                java.net.URLEncoder"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%-- page body header information --%>
<jsp:useBean id="vecRolesType" scope="session" class="java.util.Vector" />
<jsp:useBean id="vecAffiliationType" scope="session"
	class="java.util.Vector" />

<!-- Added for case id COEUSQA-3160 - Start -->
<jsp:useBean id="personsData" scope="session" class="java.util.Vector" />
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<!-- Added for case id COEUSQA-3160 - End -->
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>


<%  String EMPTY_STRING = "";
    String link = EMPTY_STRING;
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
    String strProtocolNum = (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
    if(strProtocolNum == null)
        strProtocolNum = "";

    //Added for case id COEUSQA-3160 - Start
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
    //Added for case id COEUSQA-3160 - End

    String amendRenewPageMode = (String)session.getAttribute("amendRenewPageMode"+request.getSession().getId());
    if(strProtocolNum.length() > 10 && (amendRenewPageMode == null ||
            amendRenewPageMode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE))){
        modeValue = true;
    }
    int notifFlag = -1;
    if(request.getAttribute(CoeusConstants.ENABLE_IRB_PERSONNEL_NOTIFICATION)!=null){
     notifFlag=Integer.parseInt(request.getAttribute(CoeusConstants.ENABLE_IRB_PERSONNEL_NOTIFICATION).toString());
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
<%--tw......................--%>
<script LANGUAGE="JavaScript">


    var searchSelection = "";
    var errValue = false;
    var errLock = false;
    var selectedValue=0;

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

    function searchWindow(value){
        searchSelection = value;
        var winleft = (screen.width - 650) / 2;
        var winUp = (screen.height - 450) / 2;
        var win = "scrollbars=1,resizable=1,width=750,height=450,left="+winleft+",top="+winUp;
        if(value == 'person'){
            sList = window.open('<%=request.getContextPath()%>/irbSearch.do?type=<bean:message key="searchWindow.title.person"/>&search=true&searchName=WEBPERSONSEARCH', "list", win);
        }else if (value == 'rolodex'){
            sList = window.open('<%=request.getContextPath()%>/irbSearch.do?type=<bean:message key="searchWindow.title.rolodex"/>&search=true&searchName=WEBROLODEXSEARCH', "list", win);
        } else if(value == 'unit') {
            sList = window.open('<%=request.getContextPath()%>/irbSearch.do?type=<bean:message key="searchWindow.title.unit"/>&search=true&searchName=WEBLEADUNITSEARCH', "list", win);
        }
      }

      function fetch_Data(result){
        var name="";
        var flag="";
        var personId="";
        var phone="";

        dataChanged();
        document.invesKeyPersons.departmentNumber.value = '';
        document.invesKeyPersons.departmentName.value = '';
        if(searchSelection == 'unit') {
            if(result["UNIT_NUMBER"]!='null' && result["UNIT_NUMBER"]!= undefined){
                document.invesKeyPersons.departmentNumber.value = result["UNIT_NUMBER"];
            }
            if(result["UNIT_NAME"]!='null' && result["UNIT_NAME"]!= undefined){
                document.invesKeyPersons.departmentName.value = result["UNIT_NAME"];
            }
        } else {
            document.invesKeyPersons.acType.value = "I";
            document.invesKeyPersons.principleInvestigatorFlag.value = '';
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
                    document.invesKeyPersons.departmentNumber.value = result["HOME_UNIT"];
                }else{
                    document.invesKeyPersons.departmentNumber.value = '';
                }
                if(result["UNIT_NAME"]!='null' && result["UNIT_NAME"]!= undefined){
                    document.invesKeyPersons.departmentName.value = result["UNIT_NAME"];
                }else{
                    document.invesKeyPersons.departmentName.value = '';
                }
                if(result["FAX_NUMBER"]!='null' && result["FAX_NUMBER"]!= undefined){
                    document.invesKeyPersons.faxNumber.value = result["FAX_NUMBER"];
                }else{
                    document.invesKeyPersons.faxNumber.value = '';
                }
                if(result["MOBILE_PHONE_NUMBER"]!='null' && result["MOBILE_PHONE_NUMBER"]!= undefined){
                    document.invesKeyPersons.mobileNumber.value = result["MOBILE_PHONE_NUMBER"];
                }else{
                    document.invesKeyPersons.mobileNumber.value = '';
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
                    document.invesKeyPersons.personRole.value = result["TITLE"];
                }else{
                    document.invesKeyPersons.personRole.value = '';
                }
                if(result["FAX_NUMBER"]!='null' && result["FAX_NUMBER"]!= undefined){
                    document.invesKeyPersons.faxNumber.value = result["FAX_NUMBER"];
                }else{
                    document.invesKeyPersons.faxNumber.value = '';
                }
            }
            document.invesKeyPersons.email.value = '';
            if(result["EMAIL_ADDRESS"]!='null' && result["EMAIL_ADDRESS"]!= undefined){
                document.invesKeyPersons.email.value = result["EMAIL_ADDRESS"];
            }else{
                document.invesKeyPersons.email.value = '';
            }
            document.invesKeyPersons.mobileNumber.value = '';
            document.invesKeyPersons.personName.value = name;
            document.invesKeyPersons.personId.value = personId;
            document.invesKeyPersons.phone.value = phone;
            document.invesKeyPersons.nonEmployeeFlag.value = flag;
            document.invesKeyPersons.acType.value = 'N';
            document.invesKeyPersons.personRole.value = '';
            //document.invesKeyPersons.affiliationTypeCode.value = '';
         //   alert(result["IS_FACULTY"]);
            if(result["IS_FACULTY"]!='null' && result["IS_FACULTY"]!= undefined){
                if(result["IS_FACULTY"]=='Y'){
                document.invesKeyPersons.affiliationTypeCode.value = '1';   
                }else{
                document.invesKeyPersons.affiliationTypeCode.value = '3';    
                }
            }else{
            document.invesKeyPersons.affiliationTypeCode.value = '';    
            }
            //COEUSQA:3162 - Adding investigator/ key person retains info from previous person - Start
            document.invesKeyPersons.role.value = '';
            //COEUSQA:3162 - End
            document.invesKeyPersons.action = "<%=request.getContextPath()%>/getInvesKeyPersons.do?operation=N";
            document.invesKeyPersons.submit();

        }
      }
      function showDisclDialog(divId)
 {
           <logic:empty name="irbProtDetails">
                   alert("This protocol has no protocol persons");
          </logic:empty>
          <logic:notEmpty name="irbProtDetails">
                width =450;// document.getElementById(divId).style.pixelWidth;
                height =150;// document.getElementById(divId).style.pixelHeight;
                sm(divId,width,height);
           </logic:notEmpty>
           
 document.getElementById("mbox").style.left="450";
 document.getElementById("mbox").style.top="250";
            }
      <!-- Added for case id COEUSQA-3160 - Start -->
      function updateData(data, timeStamp, personId,seqNumber, piFlag,personRoletmp) {
            if(data == 'D' && confirm("<bean:message key="validation.invesKeypersons.deleteConfirmation"/>")) {
                document.invesKeyPersons.awUpdateTimestamp.value = timeStamp;
                document.invesKeyPersons.personId.value = personId;
                document.invesKeyPersons.personRole.value=personRoletmp;
                document.invesKeyPersons.sequenceNumber.value = seqNumber;
                document.invesKeyPersons.principleInvestigatorFlag.value = piFlag;
                document.invesKeyPersons.action = "<%=request.getContextPath()%>/getInvesKeyPersons.do?operation="+data+"&DelPersonRole="+personRoletmp;
                document.invesKeyPersons.submit();
            } else if(data == 'S') {
                document.invesKeyPersons.action = "<%=request.getContextPath()%>/saveInvesKeyPersons.do?operation="+data;
                document.invesKeyPersons.submit();
            }else if(data == 'N') {
                alert("<bean:message key="validation.invesKeypersons.oneInvestigRequired"/>");
            }
      }
      <!-- Added for case id COEUSQA-3160 - End -->

      function editData(data, timeStamp, personId,seqNumber, piFlag){
            document.invesKeyPersons.awUpdateTimestamp.value = timeStamp;
            document.invesKeyPersons.personId.value = personId;
            <!-- Added for case id COEUSQA-3160 - Start -->
            document.invesKeyPersons.sequenceNumber.value = seqNumber;
            <!-- Added for case id COEUSQA-3160 - End -->
            document.invesKeyPersons.principleInvestigatorFlag.value = piFlag;
            document.invesKeyPersons.action = "<%=request.getContextPath()%>/editInvesKeyPersons.do?operation="+data;
            document.invesKeyPersons.submit();
      }

      <!-- Added for case id COEUSQA-3027 - Start -->

      function cancelInvestigators(){
         clearFormData();
         document.invesKeyPersons.action = "<%=request.getContextPath()%>/getInvesKeyPersons.do";
         document.invesKeyPersons.submit();
      }

      function clearFormData(){
        document.invesKeyPersons.acType.value = 'I';
        document.invesKeyPersons.personId.value = '';
        document.invesKeyPersons.personName.value = '';
        document.invesKeyPersons.email.value = '';
        document.invesKeyPersons.phone.value = '';
        document.invesKeyPersons.mobileNumber.value = '';
        document.invesKeyPersons.faxNumber.value = '';
        document.invesKeyPersons.departmentNumber.value = '';
        document.invesKeyPersons.departmentName.value = '';
        document.invesKeyPersons.role.value = '';
        document.invesKeyPersons.personRole.value = '';
        document.invesKeyPersons.affiliationTypeCode.value = '';
      }
      <!-- Added for case id COEUSQA-3160 - End -->

<%if(notifFlag==2){%>
       function showDialog(divId)
 {
           <logic:empty name="personsData">
                   alert("This protocol has no protocol persons");
          </logic:empty>
          <logic:notEmpty name="personsData">
                width = 450;//document.getElementById(divId).style.pixelWidth;
                height = 150;//document.getElementById(divId).style.pixelHeight;
                sm(divId,width,height);
           </logic:notEmpty>
             document.getElementById("mbox").style.left="450";
             document.getElementById("mbox").style.top="250";
       }

function  validateSendNotify(){
             var total="";
             var len=document.invesKeyPersons.check.length;
             if(len== undefined){
                 var j = 0;
                 if(document.getElementById(j).checked){
                     total+=document.invesKeyPersons.check.value;
                 }
             }else if(len==0){
                 alert("This protocol has no protocol persons");
             }
             else{
                 for(var i=0; i < document.invesKeyPersons.check.length; i++){
                     if(i==0){var j = 0;
                         if(document.getElementById(j).checked == true){
                             total+=document.getElementById(j).value;
                             document.invesKeyPersons.check[j].checked=true;
                         }
                     }else{
                         if(document.invesKeyPersons.check[i].checked == true){
                             total+=document.invesKeyPersons.check[i].value;
                         }
                     }
                 }
             }

if(total==""){alert("Please Select any User");}
else{
    document.invesKeyPersons.action = "<%=request.getContextPath()%>/sendEmailIrb.do";
    document.invesKeyPersons.submit();
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
 var len=document.invesKeyPersons.check.length;
 if(len==undefined)
     {document.invesKeyPersons.check.checked=value;
      document.getElementById(0).checked=value;
     }
else{
    for(var i=0; i < len;i++){
   document.invesKeyPersons.check[i].checked=value;
   document.getElementById(i).checked=value;
    }
    }
}
 function selectThis(i){
//debugger;
if(i==0)
 {
    if(document.invesKeyPersons.check.checked)
    {
   document.getElementById(0).checked = false;
   document.invesKeyPersons.check.checked=false;
    }else
    {
   document.getElementById(0).checked = true;
   document.invesKeyPersons.check.checked=true;
    }
 }
 else
     {
if(document.invesKeyPersons.check[i].checked)
{  document.getElementById(i).checked = false;
   document.invesKeyPersons.check[i].checked=false;
}
else
{  document.getElementById(i).checked = true;
   document.invesKeyPersons.check[i].checked=true;
}

    }
}

<%}%>
</script>
</head>
<!-- Added for case id COEUSQA-3027 Start -->
<body>
	<!-- Added for case id COEUSQA-3027 End -->
	<html:form action="/getInvesKeyPersons.do">
		<table width='100%' cellpadding='0' cellspacing='0' class='table'>
			<script type="text/javascript">
    document.getElementById('txt').innerHTML = '<bean:message key="helpPageTextProtocol.InvestigatorKeyPerson"/>';
</script>
			<%if(!modeValue) {%>
			<tr>
				<td>
					<table width='99%' cellpadding='0' cellspacing='0' class='tabtable'
						align=center>
						<tr class='tableheader'>
							<td>
								<table width="100%" border="0" cellpadding="0" cellspacing="0">
									<tr class='tableheader'>
										<td><bean:message
												key="label.invesKeypersons.investigatorKeyPersonnelDetails" />
										</td>
										<td align="right"><a id="helpPageText"
											href="javascript:showBalloon('helpPageText', 'helpText')">
												<bean:message key="helpPageTextProtocol.help" />
										</a></td>
									</tr>
								</table>
						</tr>
						<tr>
							<td>
								<div id="helpText" class='helptext'>
									<bean:message key="helpTextProtocol.InvestigatorKeyPerson" />
								</div>
							</td>
						</tr>

						<tr>
							<td class="copy" align="left">
								<div id="helpText" class='helptext'>
									<bean:message
										key="label.invesKeypersons.investigatorKeyPersonnelName" />
								</div>
							</td>
						</tr>

						<tr>
							<td class="copy" align="left">
								<div id="helpText" class='helptext'>
									<bean:message
										key="label.invesKeypersons.requiredForInvestigators" />
								</div>
							</td>
						</tr>

						<tr>
							<td class="copy" align="left">
								<div id="helpText" class='helptext'>
									<bean:message key="label.invesKeypersons.keyPersonRoleRequired" />
								</div>
							</td>
						</tr>

						<tr>
							<td class='copy'><font color="red"> <logic:messagesPresent>
										<script>errValue = true;</script>
										<html:errors header="" footer="" />
									</logic:messagesPresent> <logic:messagesPresent message="true">
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
											property="noPIInformation">
											<script>errValue = true;</script>
											<li><bean:write name="message" /></li>
										</html:messages>
										<html:messages id="message" message="true"
											property="savePIInformation">
											<script>errValue = true;</script>
											<li><bean:write name="message" /></li>
										</html:messages>
										<%-- Added for Case# 3054 -Lite Investigator's Screen - Leave without Saving -End --%>
									</logic:messagesPresent>
							</font></td>
						</tr>
						<!-- Added for case id COEUSQA-3160 - Start -->
						<tr>
							<td>
								<div id="showInvestigators">
									<a href="javascript:showHide(selectedValue);"><u>Add
											Investigators / Study Personnel Details</u></a>
								</div>
							</td>
						</tr>
						<!-- Added for case id COEUSQA-3160 - End -->
						<tr>
							<td>
								<!-- Added for case id COEUSQA-3160 - Start -->
								<div id="showOrHide" style="display: none">
									<!-- Added for case id COEUSQA-3160 - End -->
									<table width='100%' cellpadding='0' cellspacing='0'
										align=center class='tabtable'>
										<%--
                <tr class='theader'>
                    <td>
                        <bean:message key="label.invesKeypersons.addInvestigatorKeyPersons"/>:
                    </td>
                </tr>--%>

										<tr>
											<td align='left' colspan='6'>
												<%link = "javaScript:searchWindow('person')";%> <html:link
													href="<%=link%>">
													<u><bean:message
															key="label.invesKeypersons.employeeSearch" /></u>&nbsp;
                        </html:link> <%link = "javaScript:searchWindow('rolodex')";%>
												<html:link href="<%=link%>">
													<u><bean:message
															key="label.invesKeypersons.otherPersonSearch" /></u>
												</html:link>
											</td>
										</tr>

										<tr>
											<td class='core'>
												<table width='100%' cellpadding='1' cellspacing='0'>
													<tr>
														<td nowrap class="copybold"><bean:message
																key="label.invesKeypersons.name" />:</td>
														<td class="copy"><html:text property="personName"
																styleClass="cltextbox-color" readonly="true"
																onchange="dataChanged()" /></td>
														<td nowrap class="copybold"><bean:message
																key="label.invesKeypersons.email" />:</td>
														<td class="copy"><html:text property="email"
																styleClass="cltextbox-color" readonly="true"
																onchange="dataChanged()" /></td>
														<td nowrap class="copybold" width='5%'><bean:message
																key="label.invesKeypersons.phone" />:</td>
														<td class="copy"><html:text property="phone"
																styleClass="cltextbox-color" readonly="true"
																onchange="dataChanged()" /></td>
													</tr>

													<tr>
														<td></td>
														<td></td>
														<td nowrap class="copybold"><bean:message
																key="investigatorsLabel.mobile" />:</td>
														<td class="copy"><html:text property="mobileNumber"
																styleClass="cltextbox-color" readonly="true"
																onchange="dataChanged()" /></td>
														<td nowrap class="copybold"><bean:message
																key="investigatorsLabel.fax" />:</td>
														<td class="copy"><html:text property="faxNumber"
																styleClass="cltextbox-color" readonly="true"
																onchange="dataChanged()" /></td>
													</tr>

													<tr>
														<td class='copybold'><bean:message
																key="label.invesKeypersons.unit" />:</td>

														<%-- Modified for case id COEUSQA-3160 begin --%>
														<td>
															<%--<html:text property="departmentNumber" styleClass="textbox" style="width : 70px;" readonly="true" onchange="dataChanged()"/>
                                <%link = "javaScript:searchWindow('unit')";%>
                                <html:link href="<%=link%>">
                                    <u><bean:message key="label.invesKeypersons.search"/></u>
                                </html:link>--%> <%if(isStudyPerson || disableField) {%>
															<html:text property="departmentNumber"
																styleClass="textbox" style="width : 70px;"
																readonly="true" onchange="dataChanged()" /> <%}else{%> <html:text
																property="departmentNumber" styleClass="textbox"
																style="width : 70px;" onchange="dataChanged()" /> <%}%> <%if(isStudyPerson || disableField){%>
															<u><bean:message key="label.invesKeypersons.search" /></u>
															<%}else {%> <%link = "javaScript:searchWindow('unit')";%> <html:link
																href="<%=link%>">
																<u><bean:message key="label.invesKeypersons.search" /></u>
															</html:link> <%}%> <%if(isStudyPerson){
                                        String fieldName = "studyPerson" + strProtocolNum;
                                    %> <input type="hidden"
															name="<%=fieldName%>" value="false" /> <%}%> <%-- Modified for case id COEUSQA-3160 end --%>
														</td>
														<td colspan='4'><html:text property="departmentName"
																styleClass="cltextbox-color" style="width : 480px;"
																readonly="true" onchange="dataChanged()" /></td>
													</tr>

													<tr>
														<td nowrap class="copybold"><bean:message
																key="label.invesKeypersons.protocolRole" />:</td>
														<td class="copy"><html:select name="invesKeyPersons"
																property="role" onchange="selectDataChanged()"
																styleClass="cltextbox-medium">
																<html:option value="">
																	<bean:message key="correspondents.pleaseSelect" />
																</html:option>
																<html:options collection="vecRolesType" property="code"
																	labelProperty="description" />
															</html:select> <%-- Modified for case id COEUSQA-3160 start --%> <%if(disableField == true){%>
															<input type="hidden" name="role" value="0" /> <%}%> <%-- Modified for case id COEUSQA-3160 end --%>
														</td>
														<td nowrap class="copybold"><bean:message
																key="label.invesKeypersons.keyPersonRole" />:</td>
														<td class="copy"><html:text property="personRole"
																styleClass="cltextbox-medium" maxlength="60"
																onchange="dataChanged()" /></td>
														<td nowrap class="copybold"><bean:message
																key="label.invesKeypersons.affiliation" />:</td>
														<td class="copy"><html:select name="invesKeyPersons"
																property="affiliationTypeCode" onchange="dataChanged()"
																styleClass="textbox">
																<html:option value="">
																	<bean:message key="correspondents.pleaseSelect" />
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
						<tr class='tableheader'>
							<td><bean:message
									key="label.invesKeypersons.investigatorKeyPersonnel" />:</td>
							<td align="right" style="padding-right: 100px"><a
								href="javascript:showDisclDialog('divDisclosureStaus')">COI
									Disclosure Status</a></td>
							<td>
								<%if(notifFlag==1){%> Send Notification <%}else if(notifFlag==2){%>
								<a href="javascript:showDialog('irbSendNotification')">Send
									Notification</a> <%}%>
							</td>
						</tr>

						<tr>
							<td colspan="3">
								<table width='100%' cellpadding='0' cellspacing='0'
									class='tabtable' align=center>
									<tr class='theader'>
										<td width='24%'><bean:message
												key="label.invesKeypersons.personName" /></td>
										<td width='24%'><bean:message
												key="label.invesKeypersons.department" /></td>
										<td width='5%'><bean:message
												key="label.invesKeypersons.leadUnit" /></td>
										<td width='19%'><bean:message
												key="label.invesKeypersons.role" /></td>
										<td width='10%'><bean:message
												key="label.invesKeypersons.affliate" /></td>
										<td width='6%'><bean:message
												key="label.invesKeypersons.training" /></td>

										<%if(!modeValue) {%>
										<td width='6%'>&nbsp;</td>
										<%}%>
									</tr>
									<% String strBgColor = "#DCE5F1"; //BGCOLOR=EFEFEF  FBF7F7
                   int count = 0;
                   int investCount = 0;
                   String piFlag = "";
                %>

									<!-- Added for case id COEUSQA-3160 - Start -->
									<logic:present name="personsData">
										<logic:iterate id="data" name="personsData"
											type="org.apache.struts.validator.DynaValidatorForm"
											scope="session">
											<%
                     piFlag =(String) data.get("principleInvestigatorFlag");
                    if(!"".equals(piFlag)){
                      investCount++;
                    }
                %>

										</logic:iterate>
									</logic:present>
									<!-- Added for case id COEUSQA-3160 - End -->
									<logic:present name="personsData">
										<logic:iterate id="data" name="personsData"
											type="org.apache.struts.validator.DynaValidatorForm"
											scope="session">
											<%
                    String timeStamp =(String) data.get("updateTimestamp");
                    String personId = (String) data.get("personId");
                    piFlag =(String) data.get("principleInvestigatorFlag");
                    String role = (String) data.get("role");
                    //Added for case id COEUSQA-3160 - Start
                    Integer sequenceNumber = (Integer)data.get("sequenceNumber");
                    //Added for case id COEUSQA-3160 - End

                    System.out.println("role >>>" +role);
                    if (count%2 == 0)
                        strBgColor = "#D6DCE5";
                    else
                        strBgColor="#DCE5F1";
                %>
											<tr bgcolor="<%=strBgColor%>"
												onmouseover="className='TableItemOn'"
												onmouseout="className='TableItemOff'">
												<%
                    //Added for case id COEUSQA-3160 - Start
                    //String editLink = "javaScript:editData('U','"+timeStamp+"','"+personId+"','"+piFlag +"')";
                    String editLink = "javaScript:editData('U','"+timeStamp+"','"+personId+"','"+sequenceNumber+"','"+piFlag +"')";
                    //Added for case id COEUSQA-3160 - End
                    %>
												<td width='24%' class='copy'>
													<%if(!modeValue) {%> <html:link href="<%=editLink%>">
														<bean:write name="data" property="personName" />
													</html:link> <%} else {%> <bean:write name="data" property="personName" />
													<%}%>
												</td>
												<%-- Commented for case id COEUSQA-3160 begin --%>
												<%-- <td width='24%' class='copy'>
                        <bean:write name="data" property="departmentName"/>
                    </td>

                    --%>

												<%-- Commented for case id COEUSQA-3160 end --%>

												<%-- Added for case id COEUSQA-3160 - Start --%>

												<td width='24%' class='copy'>
													<% java.util.ArrayList vecInvUnits = (ArrayList)data.get("investigatorUnits");

                        String deptName= "";
                        String personRoletmp="";
                        for(int index=0 ; index<vecInvUnits.size() ; index++){
                            org.apache.struts.validator.DynaValidatorForm unitForm = (org.apache.struts.validator.DynaValidatorForm)vecInvUnits.get(index);
                            deptName = (String)unitForm.get("departmentName");%>
													<ul>
														<li><%=deptName%></li>
													</ul> <%}%>

												</td>
												<%-- Added for case id COEUSQA-3160 - End --%>

												<td width='5%' class='copy' align=center><logic:equal
														name="data" property="principleInvestigatorFlag" value="Y">
														<img
															src="<bean:write name='ctxtPath'/>/coeusliteimages/checked.gif">
													</logic:equal></td>
												<td width='19%' class='copy'>
													<%
                         if (piFlag==null || piFlag.equals(EMPTY_STRING)) {
                             personRoletmp=data.getString("personRole");
                             if(personRoletmp==null){
                                 personRoletmp="Study Personnel";
                             }
                            %> <bean:write name="data"
														property="personRole" /> <%} else {
                            personRoletmp="Investigator";
                            %> <logic:equal name="data"
														property="principleInvestigatorFlag" value="Y">
														<bean:message
															key="label.invesKeypersons.principalInvestigator" />
													</logic:equal> <logic:equal name="data"
														property="principleInvestigatorFlag" value="N">
														<bean:message key="label.invesKeypersons.coInvestigator" />
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
												<%-- Added for case id COEUSQA-3160 - Start --%>
												<%if(!modeValue) {%>
												<td width='6%' align=center class='copy'>
													<%
                            if(investCount > 1 || "".equals(piFlag)){
                                link = "javaScript:updateData('D','"+timeStamp+"','"+personId+"','"+sequenceNumber+"','"+piFlag+"','"+personRoletmp+"')";
                            }else{
                                link = "javaScript:updateData('N','"+timeStamp+"','"+personId+"','"+sequenceNumber+"','"+piFlag+"','"+personRoletmp+"')";
                            }
                            /*link = "javaScript:updateData('D','"+timeStamp+"','"+personId+"','"+piFlag+"')";*/%>
													<html:link href="<%=link%>">
														<bean:message key="label.invesKeypersons.remove" />
													</html:link>
												</td>
												<%-- Added for case id COEUSQA-3160 - End --%>
												<%}%>
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
			<tr>
				<td height='5'><html:hidden property="nonEmployeeFlag" /> <html:hidden
						property="principleInvestigatorFlag" /> <html:hidden
						property="awUpdateTimestamp" /> <html:hidden property="personId" />
					<html:hidden property="acType" /> <html:hidden
						property="awDepartmentNumber" /> <html:hidden
						property="awUnitUpdateTimestamp" /> <%-- Added for case id COEUSQA-3160 - Start --%>
					<html:hidden property="sequenceNumber" /> <%-- Added for case id COEUSQA-3160 - End --%>
				</td>
			</tr>
		</table>
		<%
  String prtocolNum="";
      ProtocolHeaderDetailsBean headerBean=(ProtocolHeaderDetailsBean)session.getAttribute("protocolHeaderBean");
         String title="";
                Vector disclHeaderDet = (Vector)session.getAttribute("irbProtDetails");
                  if(disclHeaderDet != null && disclHeaderDet.size()>0){
          for(int i=0;i<disclHeaderDet.size();i++){
             IrbProtocolDisclosureStatusBean irbInf =(IrbProtocolDisclosureStatusBean)disclHeaderDet.get(i);
               prtocolNum =  (String)headerBean.getProtocolNumber();
             title = (String)irbInf.getTitle();

          }
      }

 %>
		<div id="divDisclosureStaus" class="dialog"
			style="width: 450px; height: 155px; overflow: hidden; position: absolute;">

			<logic:present name="irbProtDetails">
				<table width="450px" height="120px" border="0" cellpadding="0"
					cellspacing="0" class="table" align="center">
					<tr>
						<td align="left" valign="top" class="tableheader">COI
							Disclosure Status</td>
					</tr>
					<tr>
						<td><table width="100%" height="7" border="0" cellpadding="1"
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
							</table></td>
					</tr>

					<tr>
						<td align="center" valign="top">
							<table width="100%" border="0" cellspacing="0" cellpadding="2"
								class="tabtable" align="center">
								<tr>
									<td valign="top" width="33%" class="theader" align="left">Person
										Name</td>
									<%--   <td valign="top"  class="theader" align="left">Department</td>--%>
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
								style="height: 60px; width: 100%; background-color: #9DBFE9; overflow-y: scroll; overflow: auto;">
								<table width="100%" border="0" cellspacing="0" cellpadding="2"
									class="tabtable" align="center">
									<% int index = 0;%>
									<logic:iterate id="irbProtDetailsList" name="irbProtDetails">

										<tr bgcolor="<%=strBgColor%>" valign="top"
											onmouseover="className='TableItemOn'"
											onmouseout="className='TableItemOff'">

											<td class="copy" align="left"><logic:notEmpty
													name="irbProtDetailsList" property="fullName">
													<bean:write name="irbProtDetailsList" property="fullName" />
												</logic:notEmpty></td>
											<%--                <td class="copy" align="left">
                              <logic:notEmpty name="irbProtDetailsList" property="unitName">
                      <bean:write name="irbProtDetailsList" property="unitName"/>
                              </logic:notEmpty>
                        </td>--%>
											<td class="copy" align="left"><logic:notEmpty
													name="irbProtDetailsList" property="role">
													<bean:write name="irbProtDetailsList" property="role" />
												</logic:notEmpty></td>
											<td class="copy" align="left"><logic:notEmpty
													name="irbProtDetailsList" property="description">
													<bean:write name="irbProtDetailsList"
														property="description" />
												</logic:notEmpty> <logic:empty name="irbProtDetailsList"
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

						<td align="center"><input type="button" value="Close"
							class="clsavebutton" onclick="hm('divDisclosureStaus')" /></td>

					</tr>
				</table>

			</logic:present>


		</div>

		<%if(notifFlag==2){%>
		<div id="irbSendNotification" class="dialog"
			style="width: 450px; height: 150px; overflow: hidden; position: absolute;">

			<logic:present name="personsData">
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

							<table width="100%" height="0" border="0" cellspacing="0"
								cellpadding="2" class="tabtable" align="center">

								<tr>

									<td valign="top" width="15%" class="theader" align="center">Select</td>
									<td valign="top" width="50%" class="theader" align="left">Name</td>
									<td valign="top" class="theader" align="left">Last
										Notification</td>
								</tr>
							</table>
							<div
								style="height: 70px; width: 100%; background-color: #9DBFE9; overflow-y: scroll; overflow: auto;">
								<table width="100%" height="0" border="0" cellspacing="0"
									cellpadding="2" class="tabtable" align="center">
									<% int index1 = 0;%>

									<logic:iterate id="sendNotificList" name="personsData">
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
							<table width="100%" height="0" border="0" cellspacing="0"
								cellpadding="2" class="tabtable" align="center">
								<tr>
									<td align="left">Select : <a
										href="javascript:selectDeselectAll(true)">All</a> | <a
										href="javascript:selectDeselectAll(false)">None</a></td>
									<td align="center" colspan="2"><input type="button"
										name="sendButton" value="Send" Class="clsavebutton"
										onclick="validateSendNotify();" /> <input type="button"
										value="Close" class="clsavebutton"
										onclick="javascript:selectDeselectAll(false);hm('irbSendNotification')" />
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


		</div>


		<logic:present name="mailSend">
			<logic:equal name="mailSend" value="true">
				<script>alert("Notifications are sent.");</script>
			</logic:equal>
			<logic:equal name="mailSend" value="false">
				<script>alert("Notifications not sent due to missing mail address.");</script>
			</logic:equal>
		</logic:present>
		<%}%>
		<script>
    //document.invesKeyPersons.principleInvestigatorFlag.value = '';
</script>
		<script>
      DATA_CHANGED = 'false';
      var dataModified = '<%=request.getAttribute("dataModified")%>';
      if(dataModified == 'modified' || (errValue && !errLock)){
            DATA_CHANGED = 'true';
      }
      LINK = "<%=request.getContextPath()%>/saveInvesKeyPersons.do?operation=S";
      FORM_LINK = document.invesKeyPersons;
      PAGE_NAME = "<bean:message key="label.invesKeypersons.investigatorKeyPersonnelDetails"/>";
      function dataChanged(){
            DATA_CHANGED = 'true';
      }

      <!-- Added for case id COEUSQA-3027 Start -->
      function selectDataChanged(){
        <!-- The value '2' points the 'Study Personnel' in the select options -->
        if('2' == (document.invesKeyPersons.role.value)){
           document.invesKeyPersons.personRole.disabled = false;
        }else{
           document.invesKeyPersons.personRole.value = "";
           document.invesKeyPersons.personRole.disabled = true;
        }
        DATA_CHANGED = 'true';
      }
      <!-- Added for case id COEUSQA-3027 End -->
      linkForward(errValue);
</script>
		<script>
      var help = '<bean:message key="helpTextProtocol.InvestigatorKeyPerson"/>';
      help = trim(help);
      if(help.length == 0) {
            document.getElementById('helpText').style.display = 'none';
      }
      function trim(s) {
            return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
      }
      <!-- Added for case id COEUSQA-3160 - Start -->
      if(document.invesKeyPersons.acType.value == 'U' || document.invesKeyPersons.acType.value == 'N'){
        showHide(0);
      }
      <!-- Added for case id COEUSQA-3160 - End -->
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
