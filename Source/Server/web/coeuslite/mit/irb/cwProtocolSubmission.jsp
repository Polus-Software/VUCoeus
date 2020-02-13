<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@ page
	import="edu.mit.coeuslite.utils.CoeusLiteConstants, 
    org.apache.commons.validator.GenericTypeValidator,
    java.text.SimpleDateFormat,edu.mit.coeuslite.utils.DateUtils,java.util.List,edu.mit.coeuslite.utils.ProtocolSubmissionDynaBeanList"%>
<jsp:useBean id="getSubmissionTypes" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="getSubmissionTypeQualifiers" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="getProtoSubmissionCommittee" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="getProtocolReviewerList" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="validSubmissionReviewTypes" scope="session"
	class="java.util.HashMap" />
<jsp:useBean id="validProtoSubTypeQuals" scope="session"
	class="java.util.HashMap" />
<jsp:useBean id="getTypeQualifiers" scope="session"
	class="java.util.HashMap" />
<jsp:useBean id="getReviewType" scope="session"
	class="java.util.HashMap" />
<bean:size id="submissionSize" name="validSubmissionReviewTypes" />
<bean:size id="reviewSize" name="getReviewType" />
<html:html>
<bean:write name="validSubmissionReviewTypes" property="key" />
<%      int count=0;    
        String EMPTY_STRING = ""; 
        
        String mode=(String)session.getAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId());
        String lockMode = (String) session.getAttribute(CoeusLiteConstants.LOCK_MODE+session.getId());
        String scheduleExist = (String)request.getAttribute("scheduleExist");
        String reviewerExist = (String)request.getAttribute("reviewerExist");
        String MaxProtocolsExceeded = (String)request.getAttribute("MaxProtocolsExceeded");
        String checkListExist = (String)request.getAttribute("checkListExist");
        String protocolSubmissionMode = (String) session.getAttribute("getProtocolSubmissionMode");
        String protocolSubmitWithoutComm = (String) session.getAttribute("protocolSubmitWithoutSelectingCommittee");
        String protocolSubitWithouSchld = (String) session.getAttribute("protocolSubmitWithoutSelectingSchedule");
        String protocolSubmitWithoutReviewer = (String) session.getAttribute("protocolSubmitWithoutSelectingReviewer");
        String submissionConfirmation = (String)session.getAttribute("submissionConfirmation");
        //Added for COEUSDEV-296:  Can't enter reviewer Due Dates in protocol Submission Details and wrong message given - Start
        ProtocolSubmissionDynaBeanList protocolSubmissionDynaBeanList = (ProtocolSubmissionDynaBeanList)session.getAttribute("submitForReviewList");
        List reviewerList = (List)protocolSubmissionDynaBeanList.getReviewerList();
        int noOfReviewers = -1;
        if(reviewerList != null){
            noOfReviewers = reviewerList.size();
        }
        //COEUSDEV-296 : End
        String protocolNumber = (String) session.getAttribute("PROTOCOL_NUMBER"+session.getId());       
        String submissionMode = EMPTY_STRING;
       
        if( protocolSubmissionMode !=null){
            if(protocolSubmissionMode.equalsIgnoreCase("O")){
                    submissionMode = "O";
                }else if(protocolSubmissionMode.equalsIgnoreCase("M")){
                        submissionMode = "M";                        
                    }else if(protocolSubmissionMode.equalsIgnoreCase("D")){
                        submissionMode = "D";                        
                    }            
            }
        
        boolean modeValue=false;
        //Modified to set modeValue true if lockMode or mode value is'D'
        if(lockMode != null && lockMode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE)){
            modeValue=true;
        }else if(mode!=null){
            if(mode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE)){
                modeValue=true;
            }else if(mode.equalsIgnoreCase(CoeusLiteConstants.MODIFY_MODE)){
                modeValue=false;
            }else if(mode.equalsIgnoreCase(CoeusLiteConstants.ADD_MODE)){
                modeValue=false;
            }
        }
        //Added for case#3529 - Coeus Lite - Expedited Review Checklist Display- Start 
        boolean showChkList = false;
        if(request.getAttribute("showCheckList")!=null)
        showChkList =  new Boolean(request.getAttribute("showCheckList").toString()).booleanValue();
        String reviewListChosen = (String)request.getAttribute("reviewListChosen");
        reviewListChosen = reviewListChosen != null?reviewListChosen :"";
        //Added for case#3529 - Coeus Lite - Expedited Review Checklist Display- End 
        // 3282: Reviewer view of Protocol materials - Start
        String calImage = request.getContextPath()+"/coeusliteimages/cal.gif";   
        edu.mit.coeuslite.utils.DateUtils dateUtils = new edu.mit.coeuslite.utils.DateUtils();
        // 3282: Reviewer view of Protocol materials - End
%>

<head>
<title>JSP Page</title>
<style>
.clcombobox-longest {
	width: 592px;
}
</style>
<!-- 3282: Reviewer view of Protocol materials-->
<script language="javascript" type="text/JavaScript"
	src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/coeusLiteCalendar.js"></script>
<script language='javascript'>
var formName = "";
var selectedField = "";
var textValue = new Array();
var index =0;
var checkListsize = 0;
<logic:iterate name="validSubmissionReviewTypes" id="validSubmissionReview" >
textValue[index++] = 'DES<bean:write name="validSubmissionReview" property="key" />';
var DES<bean:write name="validSubmissionReview" property="key" />= new Array();  // it declares array variables initially
var VAL<bean:write name="validSubmissionReview" property="key" />= new Array();// it declares array variables initially
DES<bean:write name="validSubmissionReview" property="key" />.push('');  // appends value
VAL<bean:write name="validSubmissionReview" property="key" />.push(' <bean:message key="protocolSubmission.pleaseSelect"/> '); // appends value
      <bean:define id="reviewID" name="validSubmissionReview" property="value" type="java.util.Map"/>
      <logic:iterate id="sl" name="reviewID">
      <logic:present name="sl">
            DES<bean:write name="validSubmissionReview" property="key" />.push('<bean:write name="sl" property="key" />');
            VAL<bean:write name="validSubmissionReview" property="key" />.push('<bean:write name="sl" property="value" />');
      </logic:present>
      </logic:iterate>  
</logic:iterate>

var qualTextValue = new Array();
var qualifierIndex =0;
<logic:iterate name="validProtoSubTypeQuals" id="validTypeQualifiers" >
qualTextValue[qualifierIndex++] = 'QUALDES<bean:write name="validTypeQualifiers" property="key" />';
var QUALDES<bean:write name="validTypeQualifiers" property="key" />= new Array();  // it declares array variables initially
var QUALVAL<bean:write name="validTypeQualifiers" property="key" />= new Array();// it declares array variables initially
QUALDES<bean:write name="validTypeQualifiers" property="key" />.push('');  // appends value
QUALVAL<bean:write name="validTypeQualifiers" property="key" />.push(' <bean:message key="protocolSubmission.pleaseSelect"/> '); // appends value
      <bean:define id="reviewID" name="validTypeQualifiers" property="value" type="java.util.Map"/>
      <logic:iterate id="sl" name="reviewID">
      <logic:present name="sl">
            QUALDES<bean:write name="validTypeQualifiers" property="key" />.push('<bean:write name="sl" property="key" />');
            QUALVAL<bean:write name="validTypeQualifiers" property="key" />.push('<bean:write name="sl" property="value" />');
      </logic:present>
      </logic:iterate>  
</logic:iterate>

function cancelSubmission(){ 
<logic:iterate name="validSubmissionReviewTypes" id="validSubmissionReview" >
textValue[index++] = 'DES<bean:write name="validSubmissionReview" property="key" />';
var DES<bean:write name="validSubmissionReview" property="key" />= new Array();  // it declares array variables initially
var VAL<bean:write name="validSubmissionReview" property="key" />= new Array();// it declares array variables initially
DES<bean:write name="validSubmissionReview" property="key" />.push('');  // appends value
VAL<bean:write name="validSubmissionReview" property="key" />.push(' <bean:message key="protocolSubmission.pleaseSelect"/> '); // appends value
      <bean:define id="reviewID" name="validSubmissionReview" property="value" type="java.util.Map"/>
      <logic:iterate id="sl" name="reviewID">
      <logic:present name="sl">
            DES<bean:write name="validSubmissionReview" property="key" />.push('<bean:write name="sl" property="key" />');
            VAL<bean:write name="validSubmissionReview" property="key" />.push('<bean:write name="sl" property="value" />');
      </logic:present>
      </logic:iterate>  
</logic:iterate>
}


var desc1 ="";
var val1 ="";
function populateReviewType(form,selectValue,fieldName ){       
      dataChanged();
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
                              select.options[i]=new Option(trim(eval(val)[i],eval(desc)[i]));
                          }
                        } else {
                          select.options[0]=new Option('<bean:message key="protocolSubmission.pleaseSelect"/>','');
                          select.options[0].selected=true;                        
                        }
            }
}

function setReviewTypeQualifier(form,selectValue){
    formName = form;
    selectedField = selectValue;
    populateReviewType(formName,selectedField,'reviewType' );
    populateTypeQualifier(formName,selectedField,'typeQualifier' );
}


function populateTypeQualifier(form,selectValue,fieldName ){       
      dataChanged();
      chosen=selectValue;
      var desc="QUALDES"+chosen;
      var val="QUALVAL"+chosen;      
      var isQualPresent = false;
      for (count = 0 ; count < qualifierIndex ; count++) {
        if(desc == qualTextValue[count]) {
            isQualPresent = true;
        }
      }
       if(eval(form+'.'+fieldName)!=undefined){        
            select = eval(form+'.'+fieldName); 
                  select.options.length = 0 ;

                        if(isQualPresent){
                          for(i=0; i< eval(val).length; i++){                                    
                              select.options[i]=new Option(trim(eval(val)[i],eval(desc)[i]));
                          }
                        } else {
                          select.options[0]=new Option('<bean:message key="protocolSubmission.pleaseSelect"/>','');
                          select.options[0].selected=true;                        
                        }
                  
            }
}

//Modified for COEUSDEV-296 :  Can't enter reviewer Due Dates in protocol Submission Details and wrong message given - Start
function confirmBeforeSubmit(){
    var validation = true;
     for(index=0;index<<%=noOfReviewers%>;index++){
        var assignedDateIndex = 'assignedDate'+index;
        var dueDateIndex = 'dueDate'+index;
        var assignedDate = document.getElementById(assignedDateIndex).value;
        var dueDate = document.getElementById(dueDateIndex).value;
        assignedDate = trim(assignedDate);
        dueDate = trim(dueDate);
        if(assignedDate != null && assignedDate.length>0 && !validateDateFormat(assignedDate)) {
            alert('Enter a valid assigned date in MM/DD/YYYY format.');
            document.getElementById(assignedDateIndex).focus();
            validation = false;
            break;
        }
        if(dueDate != null && dueDate.length>0 && !validateDateFormat(document.getElementById(dueDateIndex).value)){
            alert('Enter a valid due date in MM/DD/YYYY format.');
            document.getElementById(dueDateIndex).focus();
            validation = false;
            break;
        }
        var assignedDate = new Date(document.getElementById(assignedDateIndex).value);
        var dueDate = new Date(document.getElementById(dueDateIndex).value);
        if (dueDate < assignedDate) {
            validation = false;
            var errMsg = 'Assigned date should be prior to due date.'
            alert(errMsg);
             validation = false;
            document.getElementById(dueDateIndex).focus();
            break;
         }else{
            validation = true;
         }
     }
     if (validation){
        var conform = confirm("<bean:message key="protocolSubmission.confirm"/>");
        if(conform == true){
            protocolSubmission();
        }else{
            cancelSubmission();
        }
    }
}

function trim(val) {
    val = val.replace( /^\s*/, "" ).replace( /\s*$/, "" );
    return val;
}

function validateDateFormat(dateValue) {
    
    var Char1 = dateValue.charAt(2);
    var Char2 = dateValue.charAt(5);
    
    var flag =false;
    
    if ( Char1 =='/' && Char2 == '/' ) {
        flag = true;
    } else {
        flag =false;
    }
    
    var day;
    var month;
    var year;
    
    month = dateValue.substring(0,2);
    day = dateValue.substring(3,5);
    year = dateValue.substring(6,10);

    if( validMonth(month) && validYear(year) && (flag == true) && validDay(day,month,year) ) {
        return true;
    } else {
        return false;
    }
    
}

function IsNumeric(sText) {
    
    var ValidChars = "0123456789.";
    var IsNumber=true;
    var Char;
    
    for (i = 0; i < sText.length && IsNumber == true; i++) {
        Char = sText.charAt(i);
        if (ValidChars.indexOf(Char) == -1) {
            IsNumber = false;
        }
    }
    
    return IsNumber;
} 


function validDay(day,month,year) {
    if ( IsNumeric(day) ) {
        if(checkValidDay(day,month,year)){
            return true;
        }else {
            return false;
        }
    } else {
        return false;
    }
    
}

function checkValidDay(day,month,year){
    var leapYear = 0;
    if((year%4 == 0 ) || (year%100 == 0 )||(year%400 == 0)){
        leapYear = 1;
    }
    if((month == 2) && (leapYear == 1) && (day>29)){
        return false;
    }
    if((month == 2) && (leapYear != 1) && (day>28)){
        return false;
    }
    if(((month == '01') || (month == '03') || (month == '05') || (month == '07') || (month == '08') || (month == '10') || (month == '12')) && (day >31)){
        return false;
    }
    if(((month == '04') || (month == '06') || (month == '09') || (month == '11')) && (day > 30)){
        return false;
    }
    return true;
}

function validMonth(month) {
    if ( IsNumeric(month) ) {
        if( month >0 && month <13) {
            return true;
        } else {
            return false;
        }
    } else {
        return false;
    }
}

function validYear(year) {
    if( year.length!= 4) { return false; }
    
    if ( IsNumeric(year) ) {
        return true;
    } else {
        return false;
    }
    
}
//COEUSDEV-296 : End



function protocolSubmission(){
    document.submitForReviewList.action ="<%=request.getContextPath()%>/protocolSubmission.do"; 
    document.submitForReviewList.Save.disabled=true;//Added for case 4267-Double clicking save results in error
    document.submitForReviewList.submit();
}

var committeeId ="";
var errValue = false;
var errLock = false;
function showSchedule(){
   // document.submitForReviewList.committeeId.value = selectedValueCommitteeId;
  //  document.submitForReviewList.committeeName.value = selectedValueCommitteeName;
    document.submitForReviewList.action ="<%=request.getContextPath()%>/getScheduleDetails.do"; 
    document.submitForReviewList.submit();
}
function setValues(val){
}
function setCommittee(value1, value2) {
  //  selectedValueCommitteeId = value1;
  //  selectedValueCommitteeName = value2;
    document.submitForReviewList.committeeId.value = value1;
    document.submitForReviewList.committeeName.value = value2;
}
function setSchedule(id, schdDate){   
  //  selectedValueScheduleId = id;
 //   selectedValueScheduleDate = schdDate;
    document.submitForReviewList.scheduleId.value = id;
    document.submitForReviewList.scheduleDate.value = schdDate;
}

function showCommittee(){
  
    //Added for case#3529 - Coeus Lite - Expedited Review Checklist Display- Start 
    if(document.submitForReviewList.committeeId.value != "" ||
       document.submitForReviewList.committeeName.value != "" ||
       document.submitForReviewList.scheduleId.value != "" ||
       document.submitForReviewList.scheduleDate.value != ""){
    if (confirm("<bean:message key="protocolSubmission.clearCommitteAndSchedule"/>")==true){
        document.submitForReviewList.committeeId.value = "";
        document.submitForReviewList.committeeName.value = "";
        document.submitForReviewList.scheduleId.value = "";
        document.submitForReviewList.scheduleDate.value = "";
        // Added for COEUSQA-2803_IACUC protocol in pending status_start
        for(index=0;index<<%=noOfReviewers%>;index++){            
            var selectReviewerIndex = 'selectReviewer'+index;  
            document.getElementById(selectReviewerIndex).value="";            
        }
        // Added for COEUSQA-2803_IACUC protocol in pending status_end
    }
   }
   //Added for case#3529 - Coeus Lite - Expedited Review Checklist Display- End
   
    document.submitForReviewList.action = "<%=request.getContextPath()%>/getCommitteeDetails.do";
    document.submitForReviewList.submit();
}
function showReviewer(){
    //document.submitForReviewList.scheduleId.value = selectedValueScheduleId;
    // document.submitForReviewList.scheduleDate.value = selectedValueScheduleDate;
    // document.submitForReviewList.committeeId.value = selectedValueCommitteeId;
    // document.submitForReviewList.committeeName.value = selectedValueCommitteeName;
    document.submitForReviewList.action ="<%=request.getContextPath()%>/getReviewerDetails.do"; 
    document.submitForReviewList.submit();
}
function showAll(){
    var schdId = document.submitForReviewList.scheduleId.value;
    var commId = document.submitForReviewList.committeeId.value;
    
    if( (commId !=null && commId !="") && (schdId !=null && schdId !="" )){
            if (confirm("<bean:message key="protocolSubmission.clearCommitteAndSchedule"/>")==true){
            document.submitForReviewList.committeeId.value = "";
            document.submitForReviewList.committeeName.value = "";
            document.submitForReviewList.scheduleId.value = "";
            document.submitForReviewList.scheduleDate.value = "";
            // Added for COEUSQA-2803_IACUC protocol in pending status_start
            for(index=0;index<<%=noOfReviewers%>;index++){            
                var selectReviewerIndex = 'selectReviewer'+index;  
                document.getElementById(selectReviewerIndex).value="";            
            }
            // Added for COEUSQA-2803_IACUC protocol in pending status_end
        }
    }    
    
    document.submitForReviewList.action ="<%=request.getContextPath()%>/getAllCommitteeList.do"; 
    document.submitForReviewList.submit();
}
function showCheckList(){     
    //  var reviewType = document.submitForReviewList.reviewType.value;   
      document.submitForReviewList.action ="<%=request.getContextPath()%>/getCheckList.do?selectedReviewType="+selected; 
      document.submitForReviewList.submit();

}
var selected = "";
function setCheckList(val){
    dataChanged();
    if(val1 != "" && val.length !=1 ) {       
        var x=document.submitForReviewList.reviewType.selectedIndex;  
        
        if(trim(eval(val1)[x],eval(desc1)[x]) == 'Expedited' ) {           
            document.getElementById('showCheckList').style.display = 'block';
            selected = 'Expedited';
        }  else if(trim(eval(val1)[x],eval(desc1)[x]) == 'Exempt') {
            document.getElementById('showCheckList').style.display = 'block';
            selected = 'Exempt';        
        }  else {
            document.getElementById('showCheckList').style.display = 'none';                     
        }
        document.getElementById('checkListDiv').style.display = 'none';
        deselectCheckList();
     }else if(val.length ==1){       
   
                 
        if(val == 2 ) {          
            document.getElementById('showCheckList').style.display = 'block';
            selected = 'Expedited';
        }  else if( val == 3) {
            document.getElementById('showCheckList').style.display = 'block';
            selected = 'Exempt';        
        }  else {
            document.getElementById('showCheckList').style.display = 'none';            
        }   
             document.getElementById('checkListDiv').style.display = 'none';
             deselectCheckList();
     }//end of elseif
}

function deselectCheckList() {
   
    if(checkListsize > 0) {
        for (index=0; index < checkListsize;index++) {
            var currentValue = document.getElementsByName('dynaFormCheck['+index+'].selectCheckList');
            currentValue[0].checked = false;  
        }
    }
}
 

</script>

</head>
<body>
	<html:form action="/getSubmitForReview.do">
		<script type="text/javascript">
    document.getElementById('txt').innerHTML = '<bean:message key="helpPageTextProtocol.Actions"/>';
</script>
		<script>  <% if( submissionConfirmation !=null && submissionConfirmation.equals("YES")){ %>
     //Modified for Case# 3063 3063_Notice Page to the PI that a Protocol Submission to the IRB is successful - Start
     // alert("<bean:message key="protocolSubmission.protocolNumber"/> <%=protocolNumber %> <bean:message key="protocolSubmission.submitForReview"/>");    
     document.submitForReviewList.action ="<%=request.getContextPath()%>/mailAction.do"; 
     document.submitForReviewList.submit();

 <%   }   %>   </script>
		<%   if(submissionConfirmation == null) {   %>
		<table width='100%' cellpadding='0' cellspacing='0' class='table'>
			<tr>
				<td align="left" valign="top" class="core">
					<table width="100%" border="0" align="center" cellpadding="0"
						cellspacing="0" class="tabtable">
						<tr class="theader">
							<td class='theader'><bean:message
									key="protocolSubmission.protocolSubmission" /></td>
							<td align="right"><a id="helpPageText"
								href="javascript:showBalloon('helpPageText', 'helpText')"> <bean:message
										key="helpPageTextProtocol.help" />
							</a></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<div id="helpText" class='helptext'>
						&nbsp;&nbsp;
						<bean:message key="helpTextProtocol.SubmitForReview" />
					</div>
				</td>
			</tr>
			<tr>
				<td class="copybold">&nbsp;<font color="red">*</font> <bean:message
						key="label.indicatesReqFields" />
				</td>
			</tr>
			<tr>
				<td class='copy'><logic:messagesPresent>
						<font color='red'> <script>errValue = true;</script> <html:errors
								header="" footer="" />
						</font>
					</logic:messagesPresent> <logic:messagesPresent message="true">
						<script>errValue = true;</script>
						<font color="red"> <html:messages id="message"
								message="true" property="errMsg">
								<script>errLock = true;</script>
								<li><bean:write name="message" /></li>
							</html:messages> <html:messages id="message" message="true"
								property="noSubmissionAndReviewType">
								<li><bean:write name="message" /></li>
							</html:messages> <html:messages id="message" message="true"
								property="noReviewType">
								<li><bean:write name="message" /></li>
							</html:messages> <html:messages id="message" message="true"
								property="noCheckListExistForReviewType">
								<li><bean:write name="message" /></li>
							</html:messages> <html:messages id="message" message="true"
								property="pleaseSelectCommittee">
								<li><bean:write name="message" /></li>
							</html:messages> <html:messages id="message" message="true"
								property="noScheduleList">
								<li><bean:write name="message" /></li>
							</html:messages> <html:messages id="message" message="true"
								property="inValidSchedule">
								<li><bean:write name="message" /></li>
							</html:messages> <html:messages id="message" message="true"
								property="noActiveMembers">
								<li><bean:write name="message" /></li>
							</html:messages> <html:messages id="message" message="true"
								property="noActiveMembersforSchedule">
								<li><bean:write name="message" /></li>
							</html:messages> <html:messages id="message" message="true"
								property="errorMessage">
								<bean:write name="message" />
								<!--  3282: Reviewer view of Protocol materials - Start-->
							</html:messages> <html:messages id="message" message="true"
								property="protocolSubmission.invalidDate">
								<li><bean:write name="message" /></li>
							</html:messages> <!--  3282: Reviewer view of Protocol materials - End-->
						</font>
					</logic:messagesPresent></td>
			</tr>
			<tr>
				<td class='tableheader'><bean:message
						key="protocolSubmission.submissionDetails" /></td>
			</tr>

			<tr>
				<td>
					<table width='100%' cellpadding='1' cellspacing='1'
						class='tabtable'>


						<tr>
							<td height='10'></td>
						</tr>
						<logic:notEmpty name="submitForReviewList" property="list"
							scope="session">
							<logic:iterate id="dynaFormData" name="submitForReviewList"
								property="list" type="org.apache.struts.action.DynaActionForm"
								indexId="index" scope="session">
								<tr>
									<td class='copybold'><font color="red">*</font>
									<bean:message key="protocolSubmission.type" />:</td>
									<td>
										<%--html:select  name="dynaFormData" property="submissionType" styleClass="clcombobox-bigger"  indexed="true" disabled="<%=modeValue%>" onchange="populateReviewType('submitForReviewList',this.value,'reviewType');"--%>
										<html:select name="dynaFormData" property="submissionType"
											styleClass="clcombobox-bigger" indexed="true"
											disabled="<%=modeValue%>"
											onchange="setReviewTypeQualifier('submitForReviewList',this.value);">
											<html:option value="">
												<bean:message key="protocolSubmission.pleaseSelect" />
											</html:option>
											<html:options collection="getSubmissionTypes"
												property="submissionTypeCode"
												labelProperty="submissionDescription" />
										</html:select>
									</td>
									<td class='copybold'><font color="red">*</font>
									<bean:message key="protocolSubmission.reviewType" />:</td>
									<td><html:select name="dynaFormData" property="reviewType"
											styleClass="clcombobox-smallest" disabled="<%=modeValue%>"
											onchange="setCheckList(this.value)">
											<html:option value="">
												<bean:message key="protocolSubmission.pleaseSelect" />
											</html:option>

											<logic:notEmpty name="getReviewType">
												<html:options collection="getReviewType" property="key"
													labelProperty="value" />
											</logic:notEmpty>
										</html:select></td>
									<td width='10%' class='copybold' nowrap align=left>
										<div id="showCheckList" style='display: none'>
											&nbsp;&nbsp;&nbsp;&nbsp;
											<html:link href="javascript:showCheckList()">
												<u><bean:message key="protocolSubmission.checkList" /></u>
											</html:link>
											&nbsp;
										</div>
									</td>
								</tr>
								<tr>
									<td class='copybold'><bean:message
											key="protocolSubmission.typeQualifier" />:</td>
									<td colspan='4' align="left">
										<%--<html:select  name="dynaFormData" property="typeQualifier" onchange="dataChanged()" styleClass="clcombobox-longest" indexed="true" disabled="<%=modeValue%>">--%>
										<html:select name="dynaFormData" property="typeQualifier"
											onchange="dataChanged()" styleClass="clcombobox-longest"
											disabled="<%=modeValue%>">
											<html:option value="">
												<bean:message key="protocolSubmission.pleaseSelect" />
											</html:option>
											<%--html:options collection="getSubmissionTypeQualifiers" property="submissionTypeQualCode" labelProperty="qualsDescription"/--%>
											<logic:notEmpty name="getTypeQualifiers">
												<html:options collection="getTypeQualifiers" property="key"
													labelProperty="value" />
											</logic:notEmpty>
										</html:select>
									</td>
									<td>&nbsp;</td>
								</tr>
								<% if(submissionMode !=null && ! submissionMode.equals("D"))  {       %>

								<tr>


									<td class='copybold'><bean:message
											key="protocolSubmission.committeeId" />:</td>
									<td><html:text name="dynaFormData" readonly="true"
											property="committeeId" onchange="dataChanged()" /></td>
									<td class='copybold'><bean:message
											key="protocolSubmission.committeeName" />:</td>
									<td><html:text name="dynaFormData"
											property="committeeName" readonly="true"
											onchange="dataChanged()" /></td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td class='copybold'><bean:message
											key="protocolSubmission.scheduleId" />:</td>
									<td><html:text name="dynaFormData" property="scheduleId"
											readonly="true" onchange="dataChanged()" /></td>
									<td class='copybold'><bean:message
											key="protocolSubmission.scheduleDate" />:</td>
									<td><html:text name="dynaFormData" property="scheduleDate"
											readonly="true" onchange="dataChanged()" /></td>
									<td>&nbsp;</td>
								</tr>
								<% }  //end of submissionMode  %>
							</logic:iterate>
						</logic:notEmpty>




					</table>
				</td>
			</tr>
			<% if( modeValue != true){  %>
			<tr class='table'>
				<td colspan='4' class='savebutton' nowrap><html:button
						property="Save" styleClass="clsavebutton"
						disabled="<%=modeValue%>" onclick="confirmBeforeSubmit()">
						<bean:message key="protocolSubmission.submitButtonLabel" />
					</html:button></td>
			</tr>
			<% }  %>
			<tr>
				<td>
					<table width="100%" cellpadding='0' height='25' cellspacing='0'>
						<tr>

							<%--<td  class='copybold' nowrap align=left>
                 <div id="showCheckList" style='display:none'>&nbsp;&nbsp;&nbsp;&nbsp;
                 <html:link  href="javascript:showCheckList()">
                        <u><bean:message key="protocolSubmission.checkList"/></u>
                 </html:link> &nbsp;
                 </div>
             </td>--%>



							<% if(submissionMode !=null && ! submissionMode.equals("D") && modeValue != true){       %>
							<td width='80%' class='copybold' nowrap>&nbsp; <!-- Links  -->
								<!--Modified for case#3529 - Coeus Lite - Expedited Review Checklist Display-->
								<%   if(scheduleExist == null && reviewerExist == null && !showChkList){  %>
								<html:link href="javascript:showAll()">
									<u><bean:message key="protocolSubmission.showAll" /></u>
								</html:link>&nbsp;&nbsp;&nbsp;&nbsp; <%}else{%> &nbsp;&nbsp; <%}%> <!--Modified for case#3529 - Coeus Lite - Expedited Review Checklist Display -->
								<%if(scheduleExist !=null && !scheduleExist.equals("1")){ %>
								&nbsp;&nbsp; <%} else {%> <html:link
									href="javascript:showSchedule()">
									<u><bean:message key="protocolSubmission.selectSchedule" /></u>
								</html:link>&nbsp;&nbsp;&nbsp;&nbsp; <%}%> <%   if(scheduleExist == null && reviewerExist == null ){   %>
								<!--Modified for case#3529 - Coeus Lite - Expedited Review Checklist Display-Start -->
								<%if(showChkList){%> <html:link href="javascript:showCommittee()">
									<u><bean:message key="protocolSubmission.selectCommittee" /></u>
								</html:link> &nbsp;&nbsp;&nbsp;&nbsp; <%}else{%>&nbsp;&nbsp;<%}%> <!--Modified for case#3529 - Coeus Lite - Expedited Review Checklist Display - End -->
								<%} else {%> <html:link href="javascript:showCommittee()">
									<u><bean:message key="protocolSubmission.selectCommittee" /></u>
								</html:link> &nbsp;&nbsp;&nbsp;&nbsp; <%}%> <%if(reviewerExist !=null && !reviewerExist.equals("1")){ %>
								&nbsp;&nbsp; <%} else {%> <html:link
									href="javascript:showReviewer()">
									<u><bean:message key="protocolSubmission.selectReviewer" /></u>
								</html:link> <%}%>&nbsp;&nbsp;
							</td>
							<% } //end of submissionMode  %>
						</tr>
					</table>
				</td>
			</tr>
			<% if(submissionMode !=null && ! submissionMode.equals("D")) {       %>
			<%   if(scheduleExist == null && reviewerExist == null ){   %>
			<tr>
				<td>
					<div id='committeDiv' style="display: block">
						<%//Modified for case#3529 - Coeus Lite - Expedited Review Checklist Display- Start 
     if(!showChkList){%>
						<tr>
							<td class='tableheader'><bean:message
									key="protocolSubmission.committeeList" /></td>
						</tr>
						<tr>
							<td>
								<table width='99%' cellpadding='3' cellspacing='2' align=center
									class='tabtable'>
									<tr class='theader'>
										<td width='2%'>&nbsp;</td>
										<td width='30%'><bean:message
												key="protocolSubmission.committeeId" /></td>
										<td width='57%'><bean:message
												key="protocolSubmission.committeeName" /></td>
										<%
            String strBgColor = "#D6DCE5";
            int rowId=0;
        %>
									</tr>
									<logic:notEmpty name="submitForReviewList" property="beanList"
										scope="session">
										<logic:iterate id="dynaFormBean" name="submitForReviewList"
											property="beanList"
											type="org.apache.struts.action.DynaActionForm"
											indexId="index" scope="session">
											<%
            if (rowId%2 == 0) {
                strBgColor = "#D6DCE5"; 
            }
            else { 
                strBgColor="#DCE5F1"; 
            }
        %>
											<tr bgcolor="<%=strBgColor%>"
												onmouseover="className='TableItemOn'"
												onmouseout="className='TableItemOff'">
												<% String commId =(String) dynaFormBean.get("matchingCommitteeId");
                        String commName = (String) dynaFormBean.get("matchingCommitteeName");
                        String commLink = "javaScript:setCommittee('"+commId+"','"+commName+"');";
                        
        %>
												<td><html:radio name="dynaFormBean"
														property="selectCommittee" onchange="dataChanged()"
														value='<%=commId%>' onclick="<%=commLink%>"
														disabled="<%=modeValue%>" /></td>
												<td class='copy' width='20%' align=left><bean:write
														name="dynaFormBean" property="matchingCommitteeId" /></td>
												<td width='27%' class='copy'><bean:write
														name="dynaFormBean" property="matchingCommitteeName" /></td>
											</tr>
											<% 
                    rowId++;
                %>
										</logic:iterate>
									</logic:notEmpty>
								</table>
							</td>
						</tr>
					</div> <%}//Modified for case#3529 - Coeus Lite - Expedited Review Checklist Display- End%>
				</td>
			</tr>
			<%}%>

			<!--  Schedule Details -->

			<tr>
				<td>
					<div id='scheduleDiv' style='display: none;'>
						<table width='100%' cellpadding='3' cellspacing='2' align=center>

							<tr>
								<td class='tableheader'><bean:message
										key="protocolSubmission.scheduleList" /></td>
							</tr>
							<tr>
								<td>
									<table width='100%' cellpadding='3' cellspacing='2'
										align=center class='tabtable'>
										<tr class='theader'>
											<td width='2%'>&nbsp;</td>
											<td width='10%'><bean:message
													key="protocolSubmission.scheduleId" /></td>
											<td width='10%'><bean:message
													key="protocolSubmission.scheduleDate" /></td>
											<td width='10%'><bean:message
													key="protocolSubmission.submissionDeadline" /></td>
											<td width='10%'><bean:message
													key="protocolSubmission.place" /></td>
											<td width='10%'><bean:message
													key="protocolSubmission.time" /></td>
											<%
            String strBgColor = "#D6DCE5";
            int rowId=0;
        %>
										</tr>
										<logic:notEmpty name="submitForReviewList"
											property="scheduleList" scope="session">
											<logic:iterate id="dynaFormSchedule"
												name="submitForReviewList" property="scheduleList"
												type="org.apache.struts.action.DynaActionForm"
												indexId="index" scope="session">
												<%
            if (rowId%2 == 0) {
                strBgColor = "#D6DCE5"; 
            }
            else { 
                strBgColor="#DCE5F1"; 
            }
        %>
												<tr bgcolor="<%=strBgColor%>"
													onmouseover="className='TableItemOn'"
													onmouseout="className='TableItemOff'">
													<% String schdId =(String) dynaFormSchedule.get("scheduleId");
                        String scheduleDate = (String) dynaFormSchedule.get("scheduleDate");                        
                        String protocolSubDeadLine = (String) dynaFormSchedule.get("protocolSubDeadLine");
                        String time = (String) dynaFormSchedule.get("time");
                        //edu.mit.coeuslite.utils.DateUtils dateUtils = new edu.mit.coeuslite.utils.DateUtils();
                        scheduleDate = dateUtils.formatDate(scheduleDate,"MM/dd/yyyy");
                        if(scheduleDate==null){
                           scheduleDate ="";
                        }
                        protocolSubDeadLine = dateUtils.formatDate(protocolSubDeadLine,"MM/dd/yyyy");   
                        //Added for case#4269 - Time field should show the time instead of date - Start
                        //time = dateUtils.formatDate(time,"MM/dd/yyyy");  
                        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
                        time = (time != null ? dateFormat.format(java.sql.Timestamp.valueOf(time)) : null);
                        //Added for case#4269 - Time field should show the time instead of date - End
                          if(protocolSubDeadLine == null){
                            protocolSubDeadLine ="";
                         }
                         if(time == null){
                            time ="";
                         }
                        String schdLink = "javaScript:setSchedule('"+schdId+"','"+scheduleDate+"');";
                       
        %>
													<td><html:radio name="dynaFormSchedule"
															property="selectSchedule" onchange="dataChanged()"
															value='<%=schdId%>' onclick="<%=schdLink%>" /></td>
													<td class='copy' width='10%' align=left><bean:write
															name="dynaFormSchedule" property="scheduleId" /></td>
													<td width='10%' class='copy'><%=scheduleDate%></td>
													<td width='10%' class='copy'><%=protocolSubDeadLine%>
													</td>
													<td width='10%' class='copy'><bean:write
															name="dynaFormSchedule" property="place" /></td>
													<td width='10%' class='copy'><%= time %></td>
												</tr>
												<% 
                    rowId++;
                %>
											</logic:iterate>
										</logic:notEmpty>
									</table>
								</td>
							</tr>
						</table>
					</div>
				</td>
			</tr>

			<!--  Schedule Details -->
			<tr>
				<td>
					<div id='reviewerDiv' style='display: none;'>
						<table width='100%' cellpadding='3' cellspacing='2' align=center>

							<tr>
								<td class='tableheader'><bean:message
										key="protocolSubmission.reviewerList" /></td>
							</tr>
							<tr>
								<td>
									<table width='100%' cellpadding='3' cellspacing='2'
										align=center class='tabtable'>
										<tr class='theader'>
											<td width='2%'>&nbsp;</td>
											<td width='30%'><bean:message
													key="protocolSubmission.name" /></td>
											<td width='20%'><bean:message
													key="protocolSubmission.reviewerType" /></td>
											<!-- 3282: Reviewer view of Protocol materials - Start -->
											<td width='20%'><bean:message
													key="protocolSubmission.assignedDate" /></td>
											<td width='20%'><bean:message
													key="protocolSubmission.dueDate" /></td>
											<!-- 3282: Reviewer view of Protocol materials - End-->
											<%
            String strBgColor1 = "#D6DCE5";
            int rowId1=0;
        %>
										</tr>
										<logic:notEmpty name="submitForReviewList"
											property="reviewerList" scope="session">
											<logic:iterate id="dynaFormReviewer"
												name="submitForReviewList" property="reviewerList"
												type="org.apache.struts.action.DynaActionForm"
												indexId="index" scope="session">
												<%
            if (rowId1%2 == 0) {
                strBgColor1 = "#D6DCE5"; 
            }
            else { 
                strBgColor1="#DCE5F1"; 
            }
        %>
												<tr bgcolor="<%=strBgColor1%>">
													<!-- Modified for COEUSQA-2803_IACUC protocol in pending status_start -->
													<%String selectReviewerStyleId = "selectReviewer"+rowId1;%>
													<td class='copy'><html:checkbox
															name="dynaFormReviewer"
															styleId="<%=selectReviewerStyleId%>"
															onchange="dataChanged()" property="selectReviewer"
															indexed="true" /></td>
													<!--Modified for COEUSQA-2803_IACUC protocol in pending status_end -->
													<td class='copy' width='30%' align=left><bean:write
															name="dynaFormReviewer" property="personName" /></td>
													<td width='20%' class='copy'><html:select
															name="dynaFormReviewer" property="selectReviewerType"
															styleClass="clcombobox-smallest" onchange="dataChanged()"
															indexed="true" disabled="<%=modeValue%>">
															<html:options collection="getProtocolReviewerList"
																property="reviewerTypeCode"
																labelProperty="reviewerTypeDescription" />
														</html:select></td>
													<!-- 3282: Reviewer view of Protocol materials - Start -->
													<td width='20%'>
														<% 
                        String assignedDate = (String) dynaFormReviewer.get("assignedDate");
                        String dueDate = (String) dynaFormReviewer.get("dueDate");
                        if(assignedDate != null && !EMPTY_STRING.equals(assignedDate)){
                            try {
                                String formattedAssignedDate = dateUtils.formatDate(assignedDate,"MM/dd/yyyy");
                                dynaFormReviewer.set("assignedDate", formattedAssignedDate);
                            }catch(Exception e){
                                dynaFormReviewer.set("assignedDate", assignedDate);
                            }
                            
                        }
                        
                        if(dueDate != null && !EMPTY_STRING.equals(dueDate)){
                            try {
                                String formattedDueDate = dateUtils.formatDate(dueDate,"MM/dd/yyyy");
                                dynaFormReviewer.set("dueDate", formattedDueDate);
                            }catch(Exception e){
                                dynaFormReviewer.set("dueDate", dueDate);
                            }
                        } 
                        %> <%String assignedDateStyleId = "assignedDate"+rowId1;%>
														<html:text property="assignedDate"
															styleId="<%=assignedDateStyleId%>"
															name="dynaFormReviewer" indexed="true"
															styleClass="textbox" maxlength="10"
															onchange="dataChanged()" /> <% String strAssignedDateField = "dynaFormReviewer["+index+"].assignedDate";
                        String strAssignedDateScriptSrc ="javascript:displayCalendarWithTopLeft('"+strAssignedDateField+"',8,25)";                    
                        %> <html:link
															href="<%=strAssignedDateScriptSrc%>"
															onclick="dataChanged()">
															<html:img src="<%=calImage%>" border="0" height="16"
																width="16" />
														</html:link>
													</td>
													<td width='20%'>
														<%String dueDateStyleId = "dueDate"+rowId1;%> <html:text
															property="dueDate" styleId="<%=dueDateStyleId%>"
															name="dynaFormReviewer" indexed="true"
															styleClass="textbox" maxlength="10"
															onchange="dataChanged()" /> <% String strDueDateField = "dynaFormReviewer["+index+"].dueDate";
                        String strDueDateScriptSrc ="javascript:displayCalendarWithTopLeft('"+strDueDateField+"',8,25)";                    
                        %> <html:link href="<%=strDueDateScriptSrc%>"
															onclick="dataChanged()">
															<html:img src="<%=calImage%>" border="0" height="16"
																width="16" />
														</html:link>
													</td>
													<!-- 3282: Reviewer view of Protocol materials - End -->
												</tr>
												<% 
                    rowId1++;
                %>
											</logic:iterate>
										</logic:notEmpty>
									</table>
								</td>
							</tr>
						</table>
					</div>
				</td>
			</tr>

			<%  } // end of submissionMode
          %>
			<!-- CheckList  Start -->


			<tr>
				<td>
					<div id='checkListDiv'>
						<% if(checkListExist !=null) { %>
						<table width='100%' cellpadding='3' cellspacing='2' align=center>

							<tr>
								<td class='tableheader'>
									<%if(showChkList){%><%=reviewListChosen%>
									<%}//Added for case#3529 - Coeus Lite - Expedited Review Checklist Display- %>
									<bean:message key="protocolSubmission.checkList" />
								</td>
							</tr>
							<tr>
								<td class='copybold'><bean:message
										key="protocolSubmission.currentSelection" /></td>
							</tr>
							<tr>
								<td>
									<table width='100%' cellpadding='3' cellspacing='2'
										align=center class='tabtable'>
										<tr class='theader'>
											<td width='2%'>&nbsp;</td>
											<td width='2%'>&nbsp;</td>
											<td width='94'>&nbsp;</td>

											<%
            String strBgColor2 = "#D6DCE5";
            int rowId2=0;
        %>
										</tr>
										<logic:notEmpty name="submitForReviewList"
											property="checkList" scope="session">
											<logic:iterate id="dynaFormCheck" name="submitForReviewList"
												property="checkList"
												type="org.apache.struts.action.DynaActionForm"
												indexId="index" scope="session">
												<%
            if (rowId2%2 == 0) {
                strBgColor2 = "#D6DCE5"; 
            }
            else { 
                strBgColor2="#DCE5F1"; 
            }
        %>
												<tr bgcolor="<%=strBgColor2%>">

													<td width='2%' class='copy'><html:checkbox
															name="dynaFormCheck" onchange="dataChanged()"
															property="selectCheckList" indexed="true" /></td>
													<td width='2%' class='copy'><bean:write
															name="dynaFormCheck" property="checkListCode" /></td>
													<td width='94%' class='copy'><bean:write
															name="dynaFormCheck" property="checkListDescription" />
													</td>

												</tr>
												<% 
                    rowId2++;
                %>
											</logic:iterate>
										</logic:notEmpty>
										<script>checkListsize = <%=rowId2%></script>
									</table>
								</td>
							</tr>
						</table>
						<%  }  %>
					</div>
				</td>
			</tr>


			<!-- CheckList  End -->
			<tr>
				<td>&nbsp;</td>
			</tr>

		</table>

		<%  }%>
	</html:form>

	<% if(submissionConfirmation == null) {  %>
	<script>

<%if(request.getParameter("committeeId")!=null) {%>
    document.submitForReviewList.committeeId.value = '<%=request.getParameter("committeeId")%>';
    document.submitForReviewList.committeeName.value = '<%=request.getParameter("committeeName")%>';
<%}%>
<%if(request.getParameter("scheduleId")!=null) {%>
    document.submitForReviewList.scheduleId.value = '<%=request.getParameter("scheduleId")%>';
    document.submitForReviewList.scheduleDate.value = '<%=request.getParameter("scheduleDate")%>';
<%}%>

<% if(MaxProtocolsExceeded !=null && MaxProtocolsExceeded.equals("YES")){    
        reviewerExist = null; %>
if (confirm("<bean:message key="protocolSubmission.maxProtocolsExceeded"/>")==true){
 
  <%reviewerExist="1";%>
  document.getElementById('reviewerDiv').style.display = 'block';
  document.getElementById('scheduleDiv').style.display = 'none';
 
}else{
    <%scheduleExist="1";%>
      document.getElementById('reviewerDiv').style.display = 'none';
      document.getElementById('scheduleDiv').style.display = 'block';
   
}
<% } %>

 <% if( (submissionMode !=null && submissionMode.equals("O")) && (protocolSubmitWithoutComm !=null && protocolSubmitWithoutComm.equals("YES") )){       %>  
 if (confirm("<bean:message key="protocolSubmission.protocolSubmitWithoutCommittee"/>")== true){     
     document.submitForReviewList.action ="<%=request.getContextPath()%>/protocolSubmission.do?submitWithoutComm=YES"; 
     document.submitForReviewList.submit();
 }
 <% } //end of submissionMode checking for 'O' option %>
 <% if( (submissionMode !=null && submissionMode.equals("O")) && (protocolSubitWithouSchld !=null && protocolSubitWithouSchld.equals("YES") )){       %>  
 if (confirm("<bean:message key="protocolSubmission.protocolSubitWithouSchedule"/>")== true){     
     document.submitForReviewList.action ="<%=request.getContextPath()%>/protocolSubmission.do?submitWithoutSchedule=YES"; 
     document.submitForReviewList.submit();
 }
 <% } %>
 <% if( submissionMode !=null && (submissionMode.equals("O") || submissionMode.equals("M")) && (protocolSubmitWithoutReviewer !=null && protocolSubmitWithoutReviewer.equals("YES") )){       %>  
 if (confirm("<bean:message key="protocolSubmission.protocolSubmitWithoutReviewer"/>") != true){     
     document.submitForReviewList.action ="<%=request.getContextPath()%>/protocolSubmission.do?submitWithoutReviewer=YES"; 
     document.submitForReviewList.submit();
 }
 <% } %>
 
 

</script>

	<%if(scheduleExist !=null && !scheduleExist.equals("1")){ %>
	<script>
                  document.getElementById('reviewerDiv').style.display = 'none';
                  document.getElementById('scheduleDiv').style.display = 'block';
        </script>

	<%} else if(reviewerExist !=null && !reviewerExist.equals("1")){ %>
	<script>
                  document.getElementById('reviewerDiv').style.display = 'block';
                  document.getElementById('scheduleDiv').style.display = 'none';
        </script>

	<%}%>
	<script>
var varcheckList = document.submitForReviewList.reviewType.value;
if(varcheckList ==2){
    document.getElementById('showCheckList').style.display = 'block';
    selected = 'Expedited';
} else if( varcheckList == 3) {
   document.getElementById('showCheckList').style.display = 'block';
   selected = 'Exempt';        
}
//var selectedValueCommitteeId = document.submitForReviewList.committeeId.value;
//var selectedValueCommitteeName = document.submitForReviewList.committeeName.value;
//var selectedValueScheduleId = document.submitForReviewList.scheduleId.value;
//var selectedValueScheduleDate = document.submitForReviewList.scheduleDate.value;
</script>
	<script>
          DATA_CHANGED = 'false';
          if(errValue && !errLock) {
                DATA_CHANGED = 'true';
          }
          LINK = "<%=request.getContextPath()%>/protocolSubmission.do";
          FORM_LINK = document.submitForReviewList;
          PAGE_NAME = "<bean:message key="protocolSubmission.protocolSubmission"/>";
          function dataChanged(){
                DATA_CHANGED = 'true';
          }
          linkForward(errValue);
         </script>
	<script>
          var help = ' <bean:message key="helpTextProtocol.SubmitForReview"/>';
          help = trim(help);
          if(help.length == 0) {
            document.getElementById('helpText').style.display = 'none';
          }
          function trim(s) {
                return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
          }  
      </script>

	<% }  %>
</body>

</html:html>
