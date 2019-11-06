<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@page
	import="java.lang.Boolean,java.sql.Date,edu.mit.coeuslite.arra.bean.ArraAwardHeaderBean,edu.mit.coeus.utils.CoeusFunctions,edu.mit.coeuslite.utils.CoeusLiteConstants,edu.mit.coeus.utils.CoeusVector,java.util.Vector"%>
<jsp:useBean id="arraAwardProjectStatus" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="awardTypes" scope="session" class="java.util.Vector" />
<jsp:useBean id="columnProperties" scope="session"
	class="java.util.HashMap" />

<html:html>
<head>
<title>CoeusLite</title>
<link rel=stylesheet
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	type="text/css">
<style>
.cltextbox-color {
	font-weight: normal;
	width: 220px
}

.textbox {
	width: 40px;
}

.mbox {
	background-color: #eee;
	padding: 8px;
	border: 2px outset #666;
}
</style>
<script language="javascript" type="text/JavaScript"
	src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/coeusLiteCalendar.js"></script>
<script language="javascript" type="text/JavaScript"
	src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/Balloon.js"></script>
<script>
                var errValue = false;
                var errLock = false;
                var searchSelection = "";
                function searchWindow(searchType) {
                
                searchSelection = searchType;
                var winleft = (screen.width - 650) / 2;
                var winUp = (screen.height - 450) / 2;  
                var win = "scrollbars=1,resizable=1,width=750,height=450,left="+winleft+",top="+winUp;
                if(searchSelection == 'infraContactSearch' || searchSelection == 'primaryPlaceSearch'){
                sList = window.open('<%=request.getContextPath()%>/generalProposalSearch.do?type=<bean:message key="searchWindow.title.person" bundle="proposal"/>&search=true&searchName=WEBROLODEXSEARCH', "list", win);  
                }else  if(searchSelection == 'sponsorSearch'){
                sList = window.open('<%=request.getContextPath()%>/generalProposalSearch.do?type=<bean:message key="searchWindow.title.sponsor" bundle="proposal"/>&search=true&searchName=SPONSORSEARCH', "list", win);  
                }else  if(searchSelection == 'unitSearch'){                
                sList = window.open('<%=request.getContextPath()%>/generalProposalSearch.do?type=<bean:message key="searchWindow.title.unit" bundle="proposal"/>&search=true&searchName=WEBLEADUNITSEARCH', "list", win);  
                }
                if (parseInt(navigator.appVersion) >= 4) {
                    window.sList.focus(); 
                }
            } 
      
            function fetch_Data(result){
            var  roloId = "";
            var  roloAddress = "";
            if(result["ROLODEX_ID"] != 'null' && result["ROLODEX_ID"] != undefined ){
                roloId = result["ROLODEX_ID"];
            }
            if(result["ORGANIZATION"] != 'null' && result["ORGANIZATION"] != undefined ){
            roloAddress = result["ORGANIZATION"]+"\n"; 
            }if(result["ADDRESS_LINE_1"] != 'null' && result["ADDRESS_LINE_1"] != undefined ){ 
            roloAddress += result["ADDRESS_LINE_1"]+"\n";
            }if(result["ADDRESS_LINE_2"] != 'null' && result["ADDRESS_LINE_2"] != undefined ){ 
            roloAddress += result["ADDRESS_LINE_2"]+"\n";
            }if(result["ADDRESS_LINE_3"] != 'null' && result["ADDRESS_LINE_3"] != undefined ){ 
            roloAddress += result["ADDRESS_LINE_3"]+"\n";
            }if(result["CITY"] != 'null' && result["CITY"] != undefined ){ 
            roloAddress += result["CITY"]+"\n";
            }if(result["COUNTY"] != 'null' && result["COUNTY"] != undefined ){ 
            roloAddress += result["COUNTY"]+"\n";
            }if(result["STATE"] != 'null' && result["STATE"] != undefined ){ 
            roloAddress += result["STATE"]+"\n";
            }if(result["POSTAL_CODE"] != 'null' && result["POSTAL_CODE"] != undefined ){ 
            roloAddress += result["POSTAL_CODE"]+"\n";
            }if(result["COUNTRY_NAME"] != 'null' && result["COUNTRY_NAME"] != undefined ){ 
            roloAddress += result["COUNTRY_NAME"]+"\n";
            }
            if(searchSelection == "unitSearch"){
                document.arraAwardDetailsForm.leadUnit.value=result["UNIT_NUMBER"];
                if(result["UNIT_NUMBER"]=="null"){
                    document.arraAwardDetailsForm.leadUnit.value = "";
                } else { document.arraAwardDetailsForm.leadUnit.value = result["UNIT_NUMBER"] };
                if(result["UNIT_NAME"]=="null"){
                    document.arraAwardDetailsForm.unitName.value = "";
                } else { document.arraAwardDetailsForm.unitName.value = result["UNIT_NAME"] };
            }            
            else if(searchSelection == 'infraContactSearch'){
                document.arraAwardDetailsForm.infraContactAddress.value = roloAddress;
                document.arraAwardDetailsForm.infraContactId.value = roloId;
            }else if(searchSelection == 'primaryPlaceSearch'){
                document.arraAwardDetailsForm.primPlaceOfPerfAddress.value = roloAddress;
                document.arraAwardDetailsForm.primPlaceOfPerfId.value = roloId;
            }else if(searchSelection == "sponsorSearch"){                        
                        document.arraAwardDetailsForm.sponsorAwardNumber.value = result["SPONSOR_CODE"] ;
                        document.arraAwardDetailsForm.sponsorName.value = result["SPONSOR_NAME"] ;
            }
            dataChanged();
            }  
            
            function updateData(){
                if(!validateArraDetails()){return;} 
                document.arraAwardDetailsForm.Save.disabled=true;
                document.arraAwardDetailsForm.finalFlag.disabled=false;
                document.arraAwardDetailsForm.projectStatus.disabled=false;                
                document.arraAwardDetailsForm.activityCode.disabled=false;
                document.arraAwardDetailsForm.awardType.disabled=false;
                 
                document.arraAwardDetailsForm.action = "<%=request.getContextPath()%>/saveArraAwardDetails.do?";
                document.arraAwardDetailsForm.submit();
            }
            
            function showHideDetails(val){
            if(val=='showAwdDetails') {
                document.getElementById('awardDetails').style.display = 'block';
                document.getElementById('hideAwdDetails').style.display = 'block';
                document.getElementById('showAwdDetails').style.display = 'none';
            } else if(val=='hideAwdDetails') {
                document.getElementById('awardDetails').style.display = 'none';
                document.getElementById('hideAwdDetails').style.display = 'none';
                document.getElementById('showAwdDetails').style.display = 'block';   
            }else if(val=='showInfraContact') {
                document.getElementById('infraContact').style.display = 'block';
                document.getElementById('infraRationale').style.display = 'block';
                document.getElementById('hideInfraContact').style.display = 'block';
                document.getElementById('showInfraContact').style.display = 'none';
            } else if(val=='hideInfraContact') {
                document.getElementById('infraContact').style.display = 'none';
                document.getElementById('infraRationale').style.display = 'none';
                document.getElementById('hideInfraContact').style.display = 'none';
                document.getElementById('showInfraContact').style.display = 'block';   
            }else if(val=='showPrimPlace') {
                document.getElementById('primPlace1').style.display = 'block';
                document.getElementById('primPlace2').style.display = 'block';
                document.getElementById('hidePrimPlace').style.display = 'block';
                document.getElementById('showPrimPlace').style.display = 'none';
            } else if(val=='hidePrimPlace') {
                document.getElementById('primPlace1').style.display = 'none';
                document.getElementById('primPlace2').style.display = 'none';
                document.getElementById('hidePrimPlace').style.display = 'none';
                document.getElementById('showPrimPlace').style.display = 'block';   
            }else if(val=='showHighlyComp') {
                document.getElementById('highlyComp').style.display = 'block';
                document.getElementById('hideHighlyComp').style.display = 'block';
                document.getElementById('showHighlyComp').style.display = 'none';
            } else if(val=='hideHighlyComp') {
                document.getElementById('highlyComp').style.display = 'none';
                document.getElementById('hideHighlyComp').style.display = 'none';
                document.getElementById('showHighlyComp').style.display = 'block';   
            }else if(val=='showjobsCreated') {
                document.getElementById('jobsCreated').style.display = 'block';
                document.getElementById('hidejobsCreated').style.display = 'block';
                document.getElementById('showjobsCreated').style.display = 'none';
            } else if(val=='hidejobsCreated') {
                document.getElementById('jobsCreated').style.display = 'none';
                document.getElementById('hidejobsCreated').style.display = 'none';
                document.getElementById('showjobsCreated').style.display = 'block';   
            }
        }
    
        function findTotal(onload){
       
            var awdJobs = document.arraAwardDetailsForm.noOfJobs.value;
            if(awdJobs == 'null' || awdJobs == ''){
                awdJobs = 0;
            }else{
               awdJobs = parseFloat(trim(document.arraAwardDetailsForm.noOfJobs.value));               
            }
            var subJobs = parseFloat(document.arraAwardDetailsForm.jobsAtSubs.value);            
            var sum = awdJobs+subJobs;
            sum=Math.round(sum*Math.pow(10,2))/Math.pow(10,2);
            document.arraAwardDetailsForm.totalJobs.value = sum;
            if(onload=='0'){
                dataChanged();  
        }
        }
        
        function trim(s) {
            var removeDollar = new RegExp("[,]","g");
            return s.replace(removeDollar, '');
      }
      
       function isNumber(evt){
	var e = event || evt; // for trans-browser compatibility
	var charCode = e.which || e.keyCode;
	
	if (charCode > 31 && (charCode < 48 || charCode > 57))
                return false;

        return true;
        }

    var display = false;
    var divIdDisplay = '';

    function showHelp(divId, elementId){
        document.getElementById(divId).style.display="";
        tabId = divId+"Tab";
        height = document.getElementById(tabId).clientHeight;
        width = document.getElementById(tabId).clientWidth;
        divHeight = document.getElementById(divId).clientHeight;//style.height;
        document.getElementById(divId).style.display="none";
        document.getElementById(divId).style.height = height+55;
        document.getElementById(divId+"2").style.height = height+10;

        elem = document.getElementById(elementId);
        var pos = getPosition(elementId);
        //Change Positions if the Help Window Boundaries goes out of Browser Window Boundaries
        browserWidth = getWindowWidth();
        browserHeight = getWindowHeight();
        var helpTop = pos[1], helpLeft = pos[0];
        if((pos[0]+width) > (browserWidth-20)){
            helpLeft = pos[0] - ((pos[0]+width) - browserWidth)-20;
        }
        if((pos[1]+height) > browserHeight){
            helpTop = pos[1] - ((pos[1]+height) - browserHeight);
            //if help is way top. center it
            if((helpTop+height) < pos[1]) helpTop = pos[1] - (height/2);
        }
        document.getElementById(divId).style.top = helpTop;
        document.getElementById(divId).style.left = helpLeft;
        showMe(divId);
    }

    function showMe(divId){
        if(divIdDisplay != '') {
            document.getElementById(divIdDisplay).style.display="none";
        }
        display = true;
        document.getElementById(divId).style.display="";
        divIdDisplay = divId;
    }

    function hideHelp(divId){
        display = false;
        setTimeout("hideMe('"+divId+"')", 250);
        //hideMe(divId);
    }
    
    function hideMe(divId){
        if(!display) {
            document.getElementById(divId).style.display="none";
        }
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
    function enableFields(){    
        
        document.arraAwardDetailsForm.awardingAgencyCode.readOnly=false;        
        document.arraAwardDetailsForm.awardingAgencyCode.className="cltextbox-medium";
        document.arraAwardDetailsForm.fundingAgencyCode.readOnly=false;        
        document.arraAwardDetailsForm.fundingAgencyCode.className="cltextbox-medium";
        
      
        document.arraAwardDetailsForm.projectStatus.disabled=false;
        document.arraAwardDetailsForm.projectStatus.className="cltextbox-medium";
      
        document.arraAwardDetailsForm.awardType.disabled=false;
        document.arraAwardDetailsForm.awardType.className="cltextbox-medium";
        
        document.arraAwardDetailsForm.strAwardAmount.readOnly=false;
        document.arraAwardDetailsForm.strAwardAmount.className="cltextbox-medium";
        document.arraAwardDetailsForm.cfdaNumber.readOnly=false;
        document.arraAwardDetailsForm.cfdaNumber.className="cltextbox-medium";
        document.arraAwardDetailsForm.agencyTAS.readOnly=false;
        document.arraAwardDetailsForm.agencyTAS.className="cltextbox-medium";
        document.arraAwardDetailsForm.strTotalExpenditure.readOnly=false;
        document.arraAwardDetailsForm.strTotalExpenditure.className="cltextbox-medium";
        document.arraAwardDetailsForm.recipientDUNSNumber.readOnly=false;
        document.arraAwardDetailsForm.recipientDUNSNumber.className="cltextbox-medium";
        document.arraAwardDetailsForm.strIndSubAwards.readOnly=false;
        document.arraAwardDetailsForm.strIndSubAwards.className="cltextbox-medium";
        document.arraAwardDetailsForm.strIndSubAwardAmount.readOnly=false;
        document.arraAwardDetailsForm.strIndSubAwardAmount.className="cltextbox-medium";
        document.arraAwardDetailsForm.strVendorLess25K.readOnly=false;
        document.arraAwardDetailsForm.strVendorLess25K.className="cltextbox-medium";
        document.arraAwardDetailsForm.strVendorLess25KAmount.readOnly=false;
        document.arraAwardDetailsForm.strVendorLess25KAmount.className="cltextbox-medium";
        document.arraAwardDetailsForm.strSubAwdLess25K.readOnly=false;
        document.arraAwardDetailsForm.strSubAwdLess25K.className="cltextbox-medium";
        document.arraAwardDetailsForm.strSubAwdLess25KAmount.readOnly=false;
        document.arraAwardDetailsForm.strSubAwdLess25KAmount.className="cltextbox-medium";
        
        document.arraAwardDetailsForm.activityCode.disabled=false;
        document.arraAwardDetailsForm.activityCode.className="cltextbox-medium";
        
        
        document.arraAwardDetailsForm.awardDescription.readOnly=false;
        document.arraAwardDetailsForm.awardDescription.className="textbox-longer";
        
        
        document.arraAwardDetailsForm.activityDescription.readOnly=false;
        document.arraAwardDetailsForm.activityDescription.className="textbox-longer";
      
        document.arraAwardDetailsForm.noOfJobs.readOnly=false;
        document.arraAwardDetailsForm.noOfJobs.className="cltextbox-medium";
      
        document.arraAwardDetailsForm.employmentImpact.readOnly=false;
        document.arraAwardDetailsForm.employmentImpact.className="textbox-longer";
        
        var curInfraContactAddress = document.arraAwardDetailsForm.infraContactAddress;
        if(curInfraContactAddress != null && curInfraContactAddress != undefined){
        document.arraAwardDetailsForm.infraContactAddress.className="cltextbox-color";
        }
        
        var curInfrastructureRationale = document.arraAwardDetailsForm.infrastructureRationale;
        if(curInfrastructureRationale != null && curInfrastructureRationale != undefined){
            document.arraAwardDetailsForm.infrastructureRationale.readOnly=false;
            document.arraAwardDetailsForm.infrastructureRationale.className="textbox-longer";
        }
        var curStrTotalInfraExpenditure = document.arraAwardDetailsForm.strTotalInfraExpenditure;
        if(curStrTotalInfraExpenditure != null && curStrTotalInfraExpenditure != undefined){
            document.arraAwardDetailsForm.strTotalInfraExpenditure.readOnly=false; 
            document.arraAwardDetailsForm.strTotalInfraExpenditure.className="cltextbox-medium";
        }
        
        document.arraAwardDetailsForm.primPlaceOfPerfAddress.className="cltextbox-color";
          
        document.arraAwardDetailsForm.congressionalDistrict.readOnly=false;        
        document.arraAwardDetailsForm.congressionalDistrict.className="textbox";
        
        var curInfrastructrueContactStyleId = document.getElementById("infrastructrueContactStyleId");
        if(curInfrastructrueContactStyleId != null && curInfrastructrueContactStyleId != undefined){
            document.getElementById("infrastructrueContactStyleId").style.display="block";  
        }
        document.getElementById("primPlaceOfPerfStyleId").style.display="block";  
        
         document.getElementById("awardHeader").style.display="block";  
         document.arraAwardDetailsForm.awardNumber.readOnly=false;       
         document.arraAwardDetailsForm.awardNumber.className="cltextbox-medium";       
         document.arraAwardDetailsForm.piName.readOnly=false;       
         document.arraAwardDetailsForm.piName.className="cltextbox-medium";
         document.arraAwardDetailsForm.accountNumber.readOnly=false;       
         document.arraAwardDetailsForm.accountNumber.className="cltextbox-medium";
         document.arraAwardDetailsForm.strTotalFederalInvoiced.readOnly=false;       
         document.arraAwardDetailsForm.strTotalFederalInvoiced.className="cltextbox-medium";
         document.getElementById("calID").style.display="block";  
         document.getElementById("sponsorCodeId").style.display="block";
         document.getElementById("leadUnitId").style.display="block";
         document.arraAwardDetailsForm.projectTitle.readOnly=false;       
         document.arraAwardDetailsForm.projectTitle.className="textbox-longer";
         document.arraAwardDetailsForm.finalFlag.disabled=false;
         document.arraAwardDetailsForm.orderNumber.readOnly=false;
         document.arraAwardDetailsForm.orderNumber.className="cltextbox-medium";
         document.arraAwardDetailsForm.govContractingOfficeCode.readOnly=false;
         document.arraAwardDetailsForm.govContractingOfficeCode.className="cltextbox-medium";
    }
    function disableEditAll(){    
     document.getElementById("editAll").style.display="none";  
    }

    function validateArraDetails(){
        total = document.arraAwardDetailsForm.totalJobs.value;
        total = parseFloat(total);
        if(total != 0 && total != "NaN" && total > 0){
            //Should have Comments
            comments = document.arraAwardDetailsForm.employmentImpact.value;
            if(trim(comments).length == 0){
                alert("<bean:message bundle="arra" key="message.JobsCreatedCannotBeBlank"/>");
                document.arraAwardDetailsForm.employmentImpact.focus();
                return false;
            }
        }
        return true;
    }
    function trim(s) {
        return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
    }
    </script>
</head>
<body onload="findTotal('1'),checkErrors()">
	<% ArraAwardHeaderBean headerBean = (ArraAwardHeaderBean)session.getAttribute("arraAwardHeaderBean");
String awardingAgencyCodeProperty = "";
String fundingAgencyCodeProperty = "";
String projectStatusProperty ="";
String activityCodeProperty = "";
String awardDescriptionProperty = "";
String activityDescriptionProperty = "";
String numberOfJobsProperty = "";
String jobsCreatedDescProperty = "";
String infrastructureRationaleProperty = "";
String totalFedArraInfraExpendProperty = "";
String primPlaceOfPerfCongDistProperty = "";
String infrastructrueContactIdProperty = "";
String primPlaceOfPerfProperty = "";
String awardTypeProp = "";
String awardAmountProp = "";
String cfdaNumberProp =	"";
String agencyTASProp =	"";
String totalExpenditureProp = "";
String totalFedInvoicedProp = "";
String recipientDunsNoProp = "";
String indSubAwardsProp = "";
String indSubAwardAmountProp =	"";
String vendorLess25KProp = "";
String vendorLess25KAmountProp = "";
String subAwdLess25KProp = "";
String subAwdLess25KAmountProp = "";

String awardNumberProp = "";
String piNameProp = "";
String accountNumberProp = "";
String sponsorCodeProp =	"";
String leadUnitProp = "";
String projectTitleProp = "";
String awardDateProp = "";
String finalFlagProp = "";
String orderNumberProp = "";
String govContractingOffCodeProp = "";

if(columnProperties != null && columnProperties.size()>0){
    awardingAgencyCodeProperty = (String)columnProperties.get("AWARDING_AGENCY_CODE");
    fundingAgencyCodeProperty = (String)columnProperties.get("FUNDING_AGENCY_CODE");
    projectStatusProperty = (String)columnProperties.get("PROJECT_STATUS");
    activityCodeProperty = (String)columnProperties.get("ACTIVITY_CODE");
    awardDescriptionProperty = (String)columnProperties.get("AWARD_DESCRIPTION");
    activityDescriptionProperty = (String)columnProperties.get("ACTIVITY_DESCRIPTION");
    numberOfJobsProperty = (String)columnProperties.get("NUMBER_OF_JOBS");
    jobsCreatedDescProperty = (String)columnProperties.get("JOBS_CREATED_DESCRIPTION");
    infrastructureRationaleProperty = (String)columnProperties.get("INFRASTRUCTURE_RATIONALE");
    totalFedArraInfraExpendProperty = (String)columnProperties.get("TOTAL_FED_ARRA_INFRA_EXPEND");
    primPlaceOfPerfCongDistProperty = (String)columnProperties.get("PRIM_PLACE_OF_PERF_CONG_DIST");
    infrastructrueContactIdProperty = (String)columnProperties.get("INFRASTRUCTURE_CONTACT_ID");
    primPlaceOfPerfProperty = (String)columnProperties.get("PRIM_PLACE_OF_PERF");
    awardTypeProp =  (String)columnProperties.get("AWARD_TYPE");
    awardAmountProp =	 (String)columnProperties.get("AWARD_AMOUNT");
    cfdaNumberProp =	 (String)columnProperties.get("CFDA_NUMBER");
    agencyTASProp = (String)columnProperties.get("AGENCY_TAS");
    totalExpenditureProp =  (String)columnProperties.get("TOTAL_FED_ARRA_EXPENDITURE");
    totalFedInvoicedProp = (String)columnProperties.get("TOTAL_FED_ARRA_FUNDS_INVOICED");
    recipientDunsNoProp =  (String)columnProperties.get("RECIPIENT_DUNS_NUMBER");
    indSubAwardsProp =  (String)columnProperties.get("NUM_OF_SUBS_TO_INDIVIDUALS");
    indSubAwardAmountProp =  (String)columnProperties.get("AMOUNT_OF_SUBS_TO_INDIVIDUALS");
    vendorLess25KProp =  (String)columnProperties.get("NUM_OF_PAY_VENDOR_LESS_25K");
    vendorLess25KAmountProp =	 (String)columnProperties.get("AMOUNT_OF_PAY_VENDOR_LESS_25K");
    subAwdLess25KProp =  (String)columnProperties.get("NUM_OF_SUBS_LESS_25K");
    subAwdLess25KAmountProp =  (String)columnProperties.get("AMOUNT_OF_SUBS_LESS_25K");
    finalFlagProp = (String)columnProperties.get("FINAL_REPORT_FLAG");
    awardNumberProp = (String)columnProperties.get("AWARD_NUMBER");
    piNameProp = (String)columnProperties.get("PI_NAME");
    accountNumberProp = (String)columnProperties.get("ACCOUNT_NUMBER");
    sponsorCodeProp = (String)columnProperties.get("SPONSOR_CODE");
    leadUnitProp = (String)columnProperties.get("LEAD_UNIT");
    projectTitleProp = (String)columnProperties.get("PROJECT_TITLE");
    awardDateProp = (String)columnProperties.get("AWARD_DATE");
    orderNumberProp = (String)columnProperties.get("ORDER_NUMBER");
    govContractingOffCodeProp = (String)columnProperties.get("GOV_CONTRACTING_OFFICE_CODE");
}
Boolean editAllRights = (Boolean)session.getAttribute("canEditAllRight");
String mode=(String)session.getAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId());

boolean modeValue=false;
boolean awardingAgencyCodeValue = true;
boolean fundingAgencyCodeValue = true;
boolean projectStatusValue =true;
boolean activityCodeValue =true;
boolean awardDescValue = true;
boolean activityDescValue = true;
boolean numberOfJobsValue = true;
boolean jobsCreatedDesValue=true;
boolean infraStrucRationleValue = true;
boolean totalFedArraInfraExpendValue = true;
boolean primPlaceOfPerfCongDistValue = true;
boolean infrastructrueContactIdValue = true;
boolean primPlaceOfPerfValue =true;
boolean awardTypeValue =  true;
boolean awardAmountValue =  true;
boolean cfdaNumberValue =  true;
boolean agencyTASValue =  true;
boolean totalExpenditureValue =  true;
boolean totalFedInvoicedValue = true;
boolean recipientDunsNoValue =  true;
boolean indSubAwardsValue =  true;
boolean indSubAwardAmountValue = true;
boolean vendorLess25KValue =  true;
boolean vendorLess25KAmountValue =  true;
boolean subAwdLess25KValue =  true;
boolean subAwdLess25KAmountValue =  true;

boolean awardNumberValue = true;
boolean piNameValue = true;
boolean accountNumberValue = true;
boolean sponsorCodeValue = true;
boolean leadUnitValue = true;
boolean projectTitleValue = true;
boolean awardDateValue = true;
boolean finalFlagValue = true;
boolean orderNumberValue = true;
boolean govContrCodeValue = true;
if(headerBean!=null && "Y".equalsIgnoreCase(headerBean.getComplete()) || "S".equalsIgnoreCase(headerBean.getComplete())) {
    modeValue=true;
}else if(CoeusLiteConstants.DISPLAY_MODE.equalsIgnoreCase(mode)){
    modeValue=true;
}else if(CoeusLiteConstants.MODIFY_MODE.equalsIgnoreCase(mode)){
    modeValue=false;
    if("yes".equalsIgnoreCase(awardingAgencyCodeProperty)){
        awardingAgencyCodeValue = false;
    }
    if("yes".equalsIgnoreCase(fundingAgencyCodeProperty)){
        fundingAgencyCodeValue = false;
    }
    if("yes".equalsIgnoreCase(projectStatusProperty)){
        projectStatusValue = false;
    }
    if("yes".equalsIgnoreCase(activityCodeProperty)){
        activityCodeValue = false;
    } if("yes".equalsIgnoreCase(awardDescriptionProperty)){
        awardDescValue = false;
    }  if("yes".equalsIgnoreCase(activityDescriptionProperty)){
        activityDescValue = false;
    }
    if("yes".equalsIgnoreCase(numberOfJobsProperty)){
        numberOfJobsValue = false;
    }
    if("yes".equalsIgnoreCase(jobsCreatedDescProperty)){
        jobsCreatedDesValue = false;
    }
    if("yes".equalsIgnoreCase(infrastructureRationaleProperty)){
        infraStrucRationleValue = false;
    }
    if("yes".equalsIgnoreCase(totalFedArraInfraExpendProperty)){
        totalFedArraInfraExpendValue = false;
    }
    if("yes".equalsIgnoreCase(primPlaceOfPerfCongDistProperty)){
        primPlaceOfPerfCongDistValue = false;
    }
    if("yes".equalsIgnoreCase(infrastructrueContactIdProperty)){
        infrastructrueContactIdValue = false;
    }
    if("yes".equalsIgnoreCase(primPlaceOfPerfProperty)){
        primPlaceOfPerfValue = false;
    }
    if("yes".equalsIgnoreCase(awardTypeProp)){
        awardTypeValue = false;
    }
    if("yes".equalsIgnoreCase(awardAmountProp)){
        awardAmountValue = false;
    }
    if("yes".equalsIgnoreCase(cfdaNumberProp)){
        cfdaNumberValue = false;
    }
    if("yes".equalsIgnoreCase(agencyTASProp)){
        agencyTASValue = false;
    }
    if("yes".equalsIgnoreCase(totalExpenditureProp)){
        totalExpenditureValue = false;
    }
    if("yes".equalsIgnoreCase(totalFedInvoicedProp)){
        totalFedInvoicedValue = false;
    }
    if("yes".equalsIgnoreCase(recipientDunsNoProp)){
        recipientDunsNoValue = false;
    }
    if("yes".equalsIgnoreCase(indSubAwardsProp)){
        indSubAwardsValue = false;
    }
    if("yes".equalsIgnoreCase(indSubAwardAmountProp)){
        indSubAwardAmountValue = false;
    }
    if("yes".equalsIgnoreCase(vendorLess25KProp)){
        vendorLess25KValue = false;
    }
    if("yes".equalsIgnoreCase(vendorLess25KAmountProp)){
        vendorLess25KAmountValue = false;
    }
    if("yes".equalsIgnoreCase(subAwdLess25KProp)){
        subAwdLess25KValue = false;
    }
    if("yes".equalsIgnoreCase(subAwdLess25KAmountProp)){
        subAwdLess25KAmountValue = false;
    }
     if("yes".equalsIgnoreCase(awardNumberProp)){
        awardNumberValue = false;
    }
     if("yes".equalsIgnoreCase(piNameProp)){
        piNameValue = false;
    }
     if("yes".equalsIgnoreCase(accountNumberProp)){
        accountNumberValue = false;
    }
    if("yes".equalsIgnoreCase(finalFlagProp)){
        finalFlagValue = false;
    }
     if("yes".equalsIgnoreCase(sponsorCodeProp)){
        sponsorCodeValue = false;
    }
     if("yes".equalsIgnoreCase(leadUnitProp)){
        leadUnitValue = false;
    }
     if("yes".equalsIgnoreCase(projectTitleProp)){
        projectTitleValue = false;
    }
     if("yes".equalsIgnoreCase(awardDateProp)){
        awardDateValue = false;
    }
     if("yes".equalsIgnoreCase(orderNumberProp)){
        orderNumberValue = false;
    }
     if("yes".equalsIgnoreCase(govContractingOffCodeProp)){
        govContrCodeValue = false;
    }
}

CoeusFunctions coeusFunctions = new CoeusFunctions();
String paramValue = coeusFunctions.getParameterValue("SHOW_ARRA_INFRASTRUCTURE_SECTION");
boolean showInfraStructure = (paramValue!=null && "1".equals(paramValue.trim()));
paramValue = coeusFunctions.getParameterValue("SHOW_ARRA_HIGHLY_COMPENSATED");
boolean showHighlyComp = (paramValue!=null && "1".equals(paramValue.trim()));
String plus = request.getContextPath()+"/coeusliteimages/plus.gif";
String minus = request.getContextPath()+"/coeusliteimages/minus.gif";
//image for help icon
String help_image = request.getContextPath()+"/coeusliteimages/icon_help.gif";
String calImage = request.getContextPath()+"/coeusliteimages/cal.gif"; 

%>
	<%!
//<html:link> tag is used insted of help method to render the image link tag
//   String help(String helpId, String linkId){
//      return "<a href=\"javascript:showHelp('"+helpId+"','"+linkId+"')\" id='"+linkId+"' onmouseover=\"javascript:showHelp('"+helpId+"','"+linkId+"')\" onmouseout=\"javascript:hideHelp('"+helpId+"')\"><font size='3'>?</font></a>";
//   }

%>
	<html:form action="/getArraDetails.do" method="post"
		onsubmit="disableSaveButton()">
		<table width="100%" height="100%" border="0" cellpadding="0"
			cellspacing="0" class="table">
			<!-- <tr class='theader'>
                    <td  width='70%' colspan="2">
    <bean:message bundle="arra" key="awardDetails.heading"/>
                    </td>
                </tr>-->

			<tr class='copy' align="left">
				<td colspan="2"><font color="red"> <logic:messagesPresent>
							<script>errValue = true;</script>
							<html:errors header="" footer="" />

						</logic:messagesPresent> <logic:messagesPresent message="true">
							<html:messages id="message" message="true" bundle="arra"
								property="alreadyLocked">
								<script>errLock = true;</script>
								<li><bean:write name="message" /></li>
							</html:messages>
							<html:messages id="message" message="true" property="errMsg">
								<script>errLock = true;</script>
								<script>errValue = true;</script>
								<li><bean:write name="message" /></li>
							</html:messages>
						</logic:messagesPresent>
				</font></td>
			</tr>

			<tr>
				<td align="left" valign="top" colspan="2"><table width="100%"
						border="0" cellpadding="0" cellspacing="0" class="tableheader">
						<tr>
							<td class="tableheader" height='20'>
								<div id='showAwdDetails'>
									<html:link href="javaScript:showHideDetails('showAwdDetails');">
										<html:img src="<%=plus%>" border="0" />
									</html:link>
									&nbsp;
									<bean:message bundle="arra" key="awardDetails.heading" />

								</div>
								<div id='hideAwdDetails' style='display: none;'>
									<html:link href="javaScript:showHideDetails('hideAwdDetails');">
										<html:img src="<%=minus%>" border="0" />
									</html:link>
									&nbsp;
									<bean:message bundle="arra" key="awardDetails.heading" />
								</div>
							</td>
							<%if(editAllRights.booleanValue() && !modeValue){ %>
							<td align="right">
								<div id="editAll" style="display: block">
									<a href="javaScript:enableFields();disableEditAll();"><u>Edit
											All</u></a>
								</div>
							</td>
							<%}else{%>
							<td align="right">
								<div id="editAll" style="display: none">
									<a href="#">Edit All</a>
								</div>
							</td>
							<%} %>
						</tr>
					</table></td>

			</tr>
			<!--Display read only fields-->

			<tr valign="top">
				<!-- JM 3-26-2012 updated to Vandy colors
        <td  align="left" valign="top" colspan='2' bgcolor="#D1E5FF"> -->
				<td align="left" valign="top" colspan='2' bgcolor="#e9e9e9">
					<div id='awardDetails' style='display: none;'>
						<!-- JM 3-26-2012 added tabtable class -->
						<table width="100%" border="0" align="left" cellpadding="4"
							cellspacing="0" class="tabtable">

							<tr>

								<td width="15%" nowrap class='copybold' align="right">&nbsp;&nbsp;&nbsp;<bean:message
										bundle="arra" key="awardDetails.awardtype" />&nbsp;
								</td>
								<%--<td width="30%" class='copy' nowrap>
                        <bean:write name="arraAwardDetailsForm" property = "awardType"/>--%>

								<td width="30%" class="copy" align="left">
									<%if(modeValue || awardTypeValue){%> <html:select
										name="arraAwardDetailsForm" styleId="awardTypeId"
										style="width :220px;" styleClass="cltextbox-nonEditcolor"
										property="awardType" disabled="<%=awardTypeValue%>"
										onchange="dataChanged()">
										<html:options collection="awardTypes" property="description"
											labelProperty="description" />
									</html:select> <%}else{%> <html:select name="arraAwardDetailsForm"
										styleId="awardTypeId" styleClass="cltextbox-medium"
										property="awardType" disabled="<%=modeValue%>"
										onchange="dataChanged()">
										<html:options collection="awardTypes" property="description"
											labelProperty="description" />
									</html:select> <%}%>
								</td>


								<%--<%if(modeValue || awardTypeValue){%>
                          <html:text onblur="" property="awardType" styleId="awardTypeId" styleClass="cltextbox-nonEditcolor" style="width:90;"  readonly="<%=awardTypeValue%>"  maxlength="8"/>
                         <%}else{%>
                         <html:text onblur="" property="awardType" styleId="awardTypeId" styleClass="cltextbox-medium" style="width:90;"  readonly="<%=modeValue%>"  maxlength="8"/>
                         <%}%>
                        </td>--%>
								<td width="10%" nowrap class='copybold' align="right">&nbsp;&nbsp;&nbsp;<bean:message
										bundle="arra" key="awardDetails.awardingAgencyCode" />&nbsp;
								</td>
								<td width="40%" class='copy' nowrap>
									<%if(modeValue || awardingAgencyCodeValue){%> <html:text
										onblur="" property="awardingAgencyCode"
										styleId="awardingAgencyCodeId" onchange="dataChanged()"
										styleClass="cltextbox-nonEditcolor" style="width:90;"
										readonly="<%=awardingAgencyCodeValue%>" maxlength="4" /> <%}else{%>
									<html:text onblur="" property="awardingAgencyCode"
										styleId="awardingAgencyCodeId" onchange="dataChanged()"
										styleClass="cltextbox-medium" style="width:90;"
										readonly="<%=modeValue%>" maxlength="4" /> <%}%>
								</td>
								<%--<td  width="10%" nowrap class='copybold' align=left><bean:message bundle="arra" key="awardDetails.awardAmount"/>&nbsp;</td>
                        <td width="40%" class='copy' nowrap><logic:notEmpty name="arraAwardDetailsForm" property = "awardAmount"><coeusUtils:formatString name="arraAwardDetailsForm" property="awardAmount" formatType="currencyFormat"/></logic:notEmpty></td>
                    --%>
							</tr>
							<tr>
								<%-- <td  width="15%" nowrap class='copybold' >&nbsp;&nbsp;&nbsp;<bean:message bundle="arra" key="awardDetails.awardDate"/>&nbsp;</td>
                        <td width="30%" class='copy' nowrap ><coeusUtils:formatDate name="arraAwardDetailsForm" formatString="MM/dd/yyyy" property="awardDate"/></td> --%>
								<%-- <td  width="10%" nowrap class='copybold' align=left><bean:message bundle="arra" key="awardDetails.awardAmount"/>&nbsp;</td>
                        <td width="40%" class='copy' nowrap><logic:notEmpty name="arraAwardDetailsForm" property = "awardAmount"><coeusUtils:formatString name="arraAwardDetailsForm" property="awardAmount" formatType="currencyFormat"/></logic:notEmpty></td>
                        --%>
							</tr>
							<%String awardAmount="",strAwdAmt="",strAmts="";%>
							<logic:present name="arraAwardDetailsForm" property="awardAmount">
								<bean:define id="awdAmount" name="arraAwardDetailsForm"
									property="awardAmount" type="java.lang.Double" />
								<%awardAmount = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(((Double)awdAmount).doubleValue());
            %>
							</logic:present>

							<logic:present name="arraAwardDetailsForm"
								property="strAwardAmount">
								<bean:define id="strAwdAmount" name="arraAwardDetailsForm"
									property="strAwardAmount" type="java.lang.String" />
								<%strAwdAmt = (String)strAwdAmount;%>
							</logic:present>
							<% 
        
        if(strAwdAmt!=null && !strAwdAmt.equals("")){
                strAmts  = strAwdAmt;
                strAwdAmt = strAwdAmt.replaceAll("[$,/,]","");
                try{
                    strAwdAmt = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(new Double(strAwdAmt));
                }catch(java.lang.NumberFormatException ne){
                    strAwdAmt = strAmts;
                }
                awardAmount = strAwdAmt;
        }
        %>
							<tr>
								<%--<td  width="15%" nowrap class='copybold' >&nbsp;&nbsp;&nbsp;<bean:message bundle="arra" key="awardDetails.fundsInvoiced"/>&nbsp;</td>
                        <td width="30%" class='copy' nowrap ><logic:notEmpty name="arraAwardDetailsForm" property = "totalFederalInvoiced"><coeusUtils:formatString name="arraAwardDetailsForm" property="totalFederalInvoiced" formatType="currencyFormat"/></logic:notEmpty></td>
                        --%>
								<td width="10%" nowrap class='copybold' align="right"><bean:message
										bundle="arra" key="awardDetails.awardAmount" />&nbsp;</td>

								<td width="40%" class='copy' nowrap>
									<%--<logic:notEmpty name="arraAwardDetailsForm" property = "awardAmount"><coeusUtils:formatString name="arraAwardDetailsForm" property="awardAmount" formatType="currencyFormat"/></logic:notEmpty>--%>
									<%if(modeValue || awardAmountValue){%> <html:text
										property="strAwardAmount" name="arraAwardDetailsForm"
										styleId="awardAmountId" readonly="<%=awardAmountValue%>"
										styleClass="cltextbox-nonEditcolor" onchange="dataChanged()"
										style="text-align: right; width: 130px "
										value="<%=awardAmount%>" maxlength="13" /> <%}else{%> <html:text
										property="strAwardAmount" name="arraAwardDetailsForm"
										styleId="awardAmountId" readonly="<%=modeValue%>"
										styleClass="cltextbox-medium" onchange="dataChanged()"
										style="text-align: right; width: 130px "
										value="<%=awardAmount%>" maxlength="13" /> <%}%>
								</td>
								<%--   <td  width="10%" nowrap class='copybold' align="right">&nbsp;&nbsp;&nbsp;<bean:message bundle="arra" key="awardDetails.CFDANumber"/>&nbsp;</td>
                        <td width="40%" class='copy' nowrap> --%>
								<%--<bean:write name="arraAwardDetailsForm" property = "cfdaNumber"/>--%>
								<%--   <%if(modeValue || cfdaNumberValue){%>
                         <html:text onblur="" property="cfdaNumber" styleId="cfdaNumberId" styleClass="cltextbox-nonEditcolor" style="width:90;"  readonly="<%=cfdaNumberValue%>"  maxlength="6"/>
                        <%}else{%>
                        <html:text onblur="" property="cfdaNumber" styleId="cfdaNumberId" styleClass="cltextbox-medium" style="width:90;"  readonly="<%=modeValue%>"  maxlength="6"/>
                        <%}%>
                        </td> --%>
								<%--
                        <td  width="10%" nowrap class='copybold' align=left><bean:message bundle="arra" key="awardDetails.totalArraExpenditure"/>:&nbsp;</td>
                        <td width="40%" class='copy' nowrap><logic:notEmpty name="arraAwardDetailsForm" property = "totalExpenditure"><coeusUtils:formatString name="arraAwardDetailsForm" property="totalExpenditure" formatType="currencyFormat"/></logic:notEmpty></td>
                        --%>
								<td width="10%" nowrap class='copybold' align="right">&nbsp;&nbsp;&nbsp;<bean:message
										bundle="arra" key="awardDetails.fundingAgencyCode" />&nbsp;
								</td>
								<td width="40%" class='copy' nowrap>
									<%if(modeValue || fundingAgencyCodeValue){%> <html:text onblur=""
										property="fundingAgencyCode" onchange="dataChanged()"
										styleId="fundingAgencyCodeId"
										styleClass="cltextbox-nonEditcolor" style="width:90;"
										readonly="<%=fundingAgencyCodeValue%>" maxlength="4" /> <%}else{%>
									<html:text onblur="" property="fundingAgencyCode"
										onchange="dataChanged()" styleId="fundingAgencyCodeId"
										styleClass="cltextbox-medium" style="width:90;"
										readonly="<%=modeValue%>" maxlength="4" /> <%}%>
								</td>
							</tr>
							<%String totalExpenditure="",strTotalExpenditure="",strAmount="";%>
							<logic:present name="arraAwardDetailsForm"
								property="totalExpenditure">
								<bean:define id="totExpend" name="arraAwardDetailsForm"
									property="totalExpenditure" type="java.lang.Double" />
								<%totalExpenditure = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(((Double)totExpend).doubleValue());
            %>
							</logic:present>

							<logic:present name="arraAwardDetailsForm"
								property="strTotalExpenditure">
								<bean:define id="strTotExp" name="arraAwardDetailsForm"
									property="strTotalExpenditure" type="java.lang.String" />
								<%strTotalExpenditure = (String)strTotExp;%>
							</logic:present>
							<% 
        
        if(strTotalExpenditure!=null && !strTotalExpenditure.equals("")){
                strAmount  = strTotalExpenditure;
                strTotalExpenditure = strTotalExpenditure.replaceAll("[$,/,]","");
                try{
                    strTotalExpenditure = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(new Double(strTotalExpenditure));
                }catch(java.lang.NumberFormatException ne){
                    strTotalExpenditure = strAmount;
                }
                totalExpenditure = strTotalExpenditure;
        }
        %>
							<tr>
								<td width="10%" nowrap class='copybold' align="right"><bean:message
										bundle="arra" key="awardDetails.totalArraExpenditure" />:&nbsp;</td>
								<td width="40%" class='copy' nowrap>
									<%--<logic:notEmpty name="arraAwardDetailsForm" property = "totalExpenditure"><coeusUtils:formatString name="arraAwardDetailsForm" property="totalExpenditure" formatType="currencyFormat"/></logic:notEmpty>--%>
									<%if(modeValue || totalExpenditureValue){%> <html:text
										property="strTotalExpenditure" name="arraAwardDetailsForm"
										styleId="totalExpenditureId"
										readonly="<%=totalExpenditureValue%>"
										styleClass="cltextbox-nonEditcolor" onchange="dataChanged()"
										style="text-align: right; width: 130px "
										value="<%=totalExpenditure%>" maxlength="13" /> <%}else{%> <html:text
										property="strTotalExpenditure" name="arraAwardDetailsForm"
										styleId="totalExpenditureId" readonly="<%=modeValue%>"
										styleClass="cltextbox-medium" onchange="dataChanged()"
										style="text-align: right; width: 130px "
										value="<%=totalExpenditure%>" maxlength="13" /> <%}%>
								</td>
								<td width="10%" nowrap class='copybold' align="right">&nbsp;&nbsp;&nbsp;<bean:message
										bundle="arra" key="awardDetails.agencyTAS" />&nbsp;
								</td>
								<td width="40%" class='copy' nowrap>
									<%--<bean:write name="arraAwardDetailsForm" property = "cfdaNumber"/>--%>
									<%if(modeValue || agencyTASValue){%> <html:text onblur=""
										property="agencyTAS" styleId="agencyTASId"
										onchange="dataChanged()" styleClass="cltextbox-nonEditcolor"
										style="width:130;" readonly="<%=agencyTASValue%>"
										maxlength="17" /> <%}else{%> <html:text onblur=""
										property="agencyTAS" styleId="agencyTASId"
										onchange="dataChanged()" styleClass="cltextbox-medium"
										style="width:130;" readonly="<%=modeValue%>" maxlength="17" />
									<%}%>
								</td>
							</tr>
							<%String totalInvoicedAmount="",strTotalInvoicedAmount="",strInvAmount="";%>
							<logic:present name="arraAwardDetailsForm"
								property="totalFederalInvoiced">
								<bean:define id="totInvoiced" name="arraAwardDetailsForm"
									property="totalFederalInvoiced" type="java.lang.Double" />
								<%totalInvoicedAmount = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(((Double)totInvoiced).doubleValue());
            %>
							</logic:present>

							<logic:present name="arraAwardDetailsForm"
								property="strTotalFederalInvoiced">
								<bean:define id="strTotFedInv" name="arraAwardDetailsForm"
									property="strTotalFederalInvoiced" type="java.lang.String" />
								<%strTotalInvoicedAmount = (String)strTotFedInv;%>
							</logic:present>
							<% 
        
        if(strTotalInvoicedAmount!=null && !strTotalInvoicedAmount.equals("")){
                strInvAmount  = strTotalInvoicedAmount;
                strTotalInvoicedAmount = strTotalInvoicedAmount.replaceAll("[$,/,]","");
                try{
                    strTotalInvoicedAmount = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(new Double(strTotalInvoicedAmount));
                }catch(java.lang.NumberFormatException ne){
                    strTotalInvoicedAmount = strInvAmount;
                }
                totalInvoicedAmount = strTotalInvoicedAmount;
        }
        %>
							<tr>
								<td width="10%" nowrap class='copybold' align="right"><bean:message
										bundle="arra" key="awardDetails.fundsInvoiced" />&nbsp;</td>
								<td width="40%" class='copy' nowrap>
									<%if(modeValue || totalFedInvoicedValue){%> <html:text
										property="strTotalFederalInvoiced" name="arraAwardDetailsForm"
										styleId="totalInvoicedAmountId"
										readonly="<%=totalFedInvoicedValue%>"
										styleClass="cltextbox-nonEditcolor" onchange="dataChanged()"
										style="text-align: right; width: 130px "
										value="<%=totalInvoicedAmount%>" maxlength="13" /> <%}else{%> <html:text
										property="strTotalFederalInvoiced" name="arraAwardDetailsForm"
										styleId="totalInvoicedAmountId" readonly="<%=modeValue%>"
										styleClass="cltextbox-medium" onchange="dataChanged()"
										style="text-align: right; width: 130px "
										value="<%=totalInvoicedAmount%>" maxlength="13" /> <%}%>
								</td>
								<td width="10%" nowrap class='copybold' align="right">&nbsp;&nbsp;&nbsp;<bean:message
										bundle="arra" key="awardDetails.CFDANumber" />&nbsp;
								</td>
								<td width="40%" class='copy' nowrap>
									<%--<bean:write name="arraAwardDetailsForm" property = "cfdaNumber"/>--%>
									<%if(modeValue || cfdaNumberValue){%> <html:text onblur=""
										property="cfdaNumber" styleId="cfdaNumberId"
										onchange="dataChanged()" styleClass="cltextbox-nonEditcolor"
										style="width:130;" readonly="<%=cfdaNumberValue%>"
										maxlength="6" /> <%}else{%> <html:text onblur=""
										property="cfdaNumber" styleId="cfdaNumberId"
										onchange="dataChanged()" styleClass="cltextbox-medium"
										style="width:130;" readonly="<%=modeValue%>" maxlength="6" />
									<%}%>
								</td>
							</tr>

							<tr>
								<td width="10%" nowrap class='copybold' align="right"><bean:message
										bundle="arra" key="awardDetails.orderNumber" />&nbsp;</td>
								<td width="40%" class='copy' nowrap>
									<%if(modeValue || orderNumberValue){%> <html:text
										property="orderNumber" name="arraAwardDetailsForm"
										styleId="orderNumberId" readonly="<%=orderNumberValue%>"
										styleClass="cltextbox-nonEditcolor" onchange="dataChanged()"
										style="width: 130px " maxlength="50" /> <%}else{%> <html:text
										property="orderNumber" name="arraAwardDetailsForm"
										styleId="orderNumberId" readonly="<%=modeValue%>"
										styleClass="cltextbox-medium" onchange="dataChanged()"
										style="width: 130px " maxlength="50" /> <%}%>
								</td>
								<td width="10%" nowrap class='copybold' align="right"><bean:message
										bundle="arra" key="awardDetails.recipientDUNSNumber" />&nbsp;</td>
								<td width="40%" class='copy' nowrap>
									<%--<bean:write name="arraAwardDetailsForm" property = "recipientDUNSNumber"/>--%>
									<%if(modeValue || recipientDunsNoValue){%> <html:text onblur=""
										property="recipientDUNSNumber" styleId="recipientDUNSNumberId"
										styleClass="cltextbox-nonEditcolor" style="width:90;"
										onchange="dataChanged()" readonly="<%=recipientDunsNoValue%>"
										maxlength="9" /> <%}else{%> <html:text onblur=""
										property="recipientDUNSNumber" styleId="recipientDUNSNumberId"
										styleClass="cltextbox-medium" style="width:90;"
										onchange="dataChanged()" readonly="<%=modeValue%>"
										maxlength="9" /> <%}%>
								</td>
							</tr>

							<tr>
								<td width="10%" class='copybold' align="right">&nbsp;&nbsp;&nbsp;<bean:message
										bundle="arra" key="headerLabel.finalFlag" />&nbsp;
								</td>
								<td width="38%" class='copy' nowrap>
									<%if(modeValue || finalFlagValue){%> <html:checkbox
										styleId="finalFlagId" property="finalFlag"
										onchange="dataChanged()" disabled="<%=finalFlagValue%>" /> <%}else{%>
									<html:checkbox styleId="finalFlagId" property="finalFlag"
										onchange="dataChanged()" disabled="<%=modeValue%>" /> <%}%>
								</td>
								<td width="10%" nowrap class='copybold' align="right"><bean:message
										bundle="arra" key="awardDetails.govContractingOfficeCode" />&nbsp;</td>
								<td width="40%" class='copy' nowrap>
									<%--<bean:write name="arraAwardDetailsForm" property = "recipientDUNSNumber"/>--%>
									<%if(modeValue || govContrCodeValue){%> <html:text onblur=""
										property="govContractingOfficeCode"
										styleId="govContractingOfficeCodeId"
										styleClass="cltextbox-nonEditcolor" style="width:90;"
										onchange="dataChanged()" readonly="<%=govContrCodeValue%>"
										maxlength="6" /> <%}else{%> <html:text onblur=""
										property="govContractingOfficeCode"
										styleId="govContractingOfficeCodeId"
										styleClass="cltextbox-medium" style="width:90;"
										onchange="dataChanged()" readonly="<%=modeValue%>"
										maxlength="6" /> <%}%>
								</td>
							</tr>

							<%--
                    <tr>
                        <td  width="10%" nowrap class='copybold'>&nbsp;&nbsp;&nbsp;<bean:message bundle="arra" key="awardDetails.awardingAgencyCode"/>&nbsp;</td>
                        <td width="40%" class='copy' nowrap><html:text onblur="" property="awardingAgencyCode"  styleClass="cltextbox-medium" style="width:90;" disabled="<%=modeValue%>"  maxlength="4"/></td>
                    </tr>
                    --%>
							<tr valign="top">
								<!-- JM 3-26-2012 updated to Vandy colors
            <td colspan="4" bgcolor="#D1E5FF" >-->
								<td colspan="4" bgcolor="e9e9e9">
									<table width="92%" cellpadding="3" cellspacing="0"
										class="tabtable" border="1" valign="top">


										<tr>
											<td width="10%" nowrap class='copybold' align="center">&nbsp;&nbsp;&nbsp;</td>
											<td width="30%" class='copybold' nowrap align="center"><bean:message
													bundle="arra" key="awardDetails.indSubAwards" />&nbsp;<html:link
													href="javaScript:showHelp('help.SubawardsToIndividuals','SubawardsToIndividuals');"
													styleId="SubawardsToIndividuals">
													<html:img src="<%=help_image%>" border="0" />
												</html:link></td>
											<td width="30%" nowrap class='copybold' align="right"><bean:message
													bundle="arra" key="awardDetails.vendorLess25K" />&nbsp;<html:link
													href="javaScript:showHelp('help.VendorsLessThan25K','VendorsLessThan25K');"
													styleId="VendorsLessThan25K">
													<html:img src="<%=help_image%>" border="0" />
												</html:link></td>
											<td width="30%" class='copybold' nowrap align="center"><bean:message
													bundle="arra" key="awardDetails.subAwdLess25K" />&nbsp;<html:link
													href="javaScript:showHelp('help.SubawardsLessThan25K','SubawardsLessThan25K');"
													styleId="SubawardsLessThan25K">
													<html:img src="<%=help_image%>" border="0" />
												</html:link></td>
										</tr>

										<tr>

											<td width="10%" class='copybold' align="left" nowrap><bean:message
													bundle="arra" key="awardDetails.number" />:</td>
											<td width="30%" class='copy' nowrap align="right">
												<%String indSubAwards ="";String strIndSubAwards="",strIndNo="";%>
												<logic:present name="arraAwardDetailsForm"
													property="indSubAwards">
													<bean:define id="indNo" name="arraAwardDetailsForm"
														property="indSubAwards" type="java.lang.Integer" />
													<%indSubAwards =indNo.toString();
                                %>
												</logic:present> <logic:present name="arraAwardDetailsForm"
													property="strIndSubAwards">
													<bean:define id="strIndNum" name="arraAwardDetailsForm"
														property="strIndSubAwards" type="java.lang.String" />
													<%strIndSubAwards = strIndNum;%>
												</logic:present> <%                                        
                            if(strIndSubAwards!=null && !strIndSubAwards.equals("")){
                                    strIndSubAwards = strIndSubAwards.replaceAll("[$,/,,]","");
                                    indSubAwards = strIndSubAwards;
                            }                                           
                            %> <%String vendorLess25K ="";String strVendorLess25K="",strVendNo="";%>
												<logic:present name="arraAwardDetailsForm"
													property="vendorLess25K">
													<bean:define id="vendNo" name="arraAwardDetailsForm"
														property="vendorLess25K" type="java.lang.Integer" />
													<%vendorLess25K =vendNo.toString();
                                %>
												</logic:present> <logic:present name="arraAwardDetailsForm"
													property="strVendorLess25K">
													<bean:define id="strVendNum" name="arraAwardDetailsForm"
														property="strVendorLess25K" type="java.lang.String" />
													<%strVendorLess25K = strVendNum;%>
												</logic:present> <%                                        
                            if(strVendorLess25K!=null && !strVendorLess25K.equals("")){
                                    strVendorLess25K = strVendorLess25K.replaceAll("[$,/,,]","");
                                    vendorLess25K = strVendorLess25K;
                            }                                           
                            %> <%String subAwdLess25K ="";String strSubAwdLess25K="";%>
												<logic:present name="arraAwardDetailsForm"
													property="subAwdLess25K">
													<bean:define id="subAwdNo" name="arraAwardDetailsForm"
														property="subAwdLess25K" type="java.lang.Integer" />
													<%subAwdLess25K =subAwdNo.toString();
                                %>
												</logic:present> <logic:present name="arraAwardDetailsForm"
													property="strSubAwdLess25K">
													<bean:define id="strSubAwdNum" name="arraAwardDetailsForm"
														property="strSubAwdLess25K" type="java.lang.String" />
													<%strSubAwdLess25K = strSubAwdNum;%>
												</logic:present> <%                                        
                            if(strSubAwdLess25K!=null && !strSubAwdLess25K.equals("")){
                                    strSubAwdLess25K = strSubAwdLess25K.replaceAll("[$,/,,]","");
                                    subAwdLess25K = strSubAwdLess25K;
                            }                                           
                            %> <%-- <bean:write name="arraAwardDetailsForm" property = "indSubAwards"/>--%>

												<%if(modeValue || indSubAwardsValue){%> <html:text onblur=""
													value="<%=indSubAwards%>" onchange="dataChanged()"
													property="strIndSubAwards" styleId="indSubAwardsId"
													styleClass="cltextbox-nonEditcolor"
													style="width:130;text-align: right;"
													readonly="<%=indSubAwardsValue%>" maxlength="6" /> <%}else{%>
												<html:text onblur="" value="<%=indSubAwards%>"
													onchange="dataChanged()" property="strIndSubAwards"
													styleId="indSubAwardsId" styleClass="cltextbox-medium"
													style="width:130;text-align: right;"
													readonly="<%=modeValue%>" maxlength="6" /> <%}%>
											</td>
											<td width="30%" class='copy' nowrap align="right">
												<%--<bean:write name="arraAwardDetailsForm" property = "vendorLess25K"/>--%>
												<%if(modeValue || vendorLess25KValue){%> <html:text onblur=""
													value="<%=vendorLess25K%>" onchange="dataChanged()"
													property="strVendorLess25K" styleId="vendorLess25KId"
													styleClass="cltextbox-nonEditcolor"
													style="width:130;text-align: right;"
													readonly="<%=vendorLess25KValue%>" maxlength="6" /> <%}else{%>
												<html:text onblur="" value="<%=vendorLess25K%>"
													onchange="dataChanged()" property="strVendorLess25K"
													styleId="vendorLess25KId" styleClass="cltextbox-medium"
													style="width:130;text-align: right;"
													readonly="<%=modeValue%>" maxlength="6" /> <%}%>
											</td>
											<td width="30%" class='copy' nowrap align="right">
												<%--<bean:write name="arraAwardDetailsForm" property = "subAwdLess25K"/>--%>
												<%if(modeValue || subAwdLess25KValue){%> <html:text onblur=""
													value="<%=subAwdLess25K%>" onchange="dataChanged()"
													property="strSubAwdLess25K" styleId="subAwdLess25KId"
													styleClass="cltextbox-nonEditcolor"
													style="width:130;text-align: right;"
													readonly="<%=subAwdLess25KValue%>" maxlength="6" /> <%}else{%>
												<html:text onblur="" value="<%=subAwdLess25K%>"
													onchange="dataChanged()" property="strSubAwdLess25K"
													styleId="subAwdLess25KId" styleClass="cltextbox-medium"
													style="width:130;text-align: right;"
													readonly="<%=modeValue%>" maxlength="6" /> <%}%>
											</td>
										</tr>
										<%String indSubAwardAmount="",strIndSubAwardAmount="",strIndAmount="";%>
										<logic:present name="arraAwardDetailsForm"
											property="indSubAwardAmount">
											<bean:define id="indAmt" name="arraAwardDetailsForm"
												property="indSubAwardAmount" type="java.lang.Double" />
											<%indSubAwardAmount = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(((Double)indAmt).doubleValue());
                        %>
										</logic:present>

										<logic:present name="arraAwardDetailsForm"
											property="strIndSubAwardAmount">
											<bean:define id="strIndAmt" name="arraAwardDetailsForm"
												property="strIndSubAwardAmount" type="java.lang.String" />
											<%strIndSubAwardAmount = (String)strIndAmt;%>
										</logic:present>
										<% 
                    
                    if(strIndSubAwardAmount!=null && !strIndSubAwardAmount.equals("")){
                            strIndAmount  = strIndSubAwardAmount;
                            strIndSubAwardAmount = strIndSubAwardAmount.replaceAll("[$,/,]","");
                            try{
                                strIndSubAwardAmount = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(new Double(strIndSubAwardAmount));
                            }catch(java.lang.NumberFormatException ne){
                                strIndSubAwardAmount = strIndAmount;
                            }
                            indSubAwardAmount = strIndSubAwardAmount;
                    }
                    %>
										<%String vendorLess25KAmount="",strVendorLess25KAmount="",strVendorAmount="";%>
										<logic:present name="arraAwardDetailsForm"
											property="vendorLess25KAmount">
											<bean:define id="vendorAmt" name="arraAwardDetailsForm"
												property="vendorLess25KAmount" type="java.lang.Double" />
											<%vendorLess25KAmount = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(((Double)vendorAmt).doubleValue());
                        %>
										</logic:present>

										<logic:present name="arraAwardDetailsForm"
											property="strVendorLess25KAmount">
											<bean:define id="strVendAmt" name="arraAwardDetailsForm"
												property="strVendorLess25KAmount" type="java.lang.String" />
											<%strVendorLess25KAmount = (String)strVendAmt;%>
										</logic:present>
										<% 
                    
                    if(strVendorLess25KAmount!=null && !strVendorLess25KAmount.equals("")){
                            strVendorAmount  = strVendorLess25KAmount;
                            strVendorLess25KAmount = strVendorLess25KAmount.replaceAll("[$,/,]","");
                            try{
                                strVendorLess25KAmount = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(new Double(strVendorLess25KAmount));
                            }catch(java.lang.NumberFormatException ne){
                                strVendorLess25KAmount = strVendorAmount;
                            }
                            vendorLess25KAmount = strVendorLess25KAmount;
                    }
                    %>
										<%String subAwdLess25KAmount="",strSubAwdLess25KAmount="",strSubAwdAmount="";%>
										<logic:present name="arraAwardDetailsForm"
											property="subAwdLess25KAmount">
											<bean:define id="subAmt" name="arraAwardDetailsForm"
												property="subAwdLess25KAmount" type="java.lang.Double" />
											<%subAwdLess25KAmount = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(((Double)subAmt).doubleValue());
                        %>
										</logic:present>

										<logic:present name="arraAwardDetailsForm"
											property="strSubAwdLess25KAmount">
											<bean:define id="strSubAmt" name="arraAwardDetailsForm"
												property="strSubAwdLess25KAmount" type="java.lang.String" />
											<%strSubAwdLess25KAmount = (String)strSubAmt;%>
										</logic:present>
										<% 
                    
                    if(strSubAwdLess25KAmount!=null && !strSubAwdLess25KAmount.equals("")){
                            strSubAwdAmount  = strSubAwdLess25KAmount;
                            strSubAwdLess25KAmount = strSubAwdLess25KAmount.replaceAll("[$,/,]","");
                            try{
                                strSubAwdLess25KAmount = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(new Double(strSubAwdLess25KAmount));
                            }catch(java.lang.NumberFormatException ne){
                                strSubAwdLess25KAmount = strSubAwdAmount;
                            }
                            subAwdLess25KAmount = strSubAwdLess25KAmount;
                    }
                    %>
										<tr>
											<td width="10%" class='copybold' align="left" nowrap><bean:message
													bundle="arra" key="awardDetails.totalAmount" />:</td>
											<td width="30%" class='copy' nowrap align="right">
												<%--<logic:notEmpty name="arraAwardDetailsForm" property = "indSubAwardAmount"><coeusUtils:formatString name="arraAwardDetailsForm" property = "indSubAwardAmount" formatType="currencyFormat"/></logic:notEmpty>--%>
												<%if(modeValue || indSubAwardAmountValue){%> <html:text
													property="strIndSubAwardAmount" name="arraAwardDetailsForm"
													styleId="indSubAwardAmountId"
													readonly="<%=indSubAwardAmountValue%>"
													styleClass="cltextbox-nonEditcolor"
													onchange="dataChanged()"
													style="text-align: right; width: 130px "
													value="<%=indSubAwardAmount%>" maxlength="13" /> <%}else{%> <html:text
													property="strIndSubAwardAmount" name="arraAwardDetailsForm"
													styleId="indSubAwardAmountId" readonly="<%=modeValue%>"
													styleClass="cltextbox-medium" onchange="dataChanged()"
													style="text-align: right; width: 130px "
													value="<%=indSubAwardAmount%>" maxlength="13" /> <%}%>
											</td>
											<td width="30%" class='copy' nowrap align="right">
												<%--<logic:notEmpty name="arraAwardDetailsForm" property = "vendorLess25KAmount"><coeusUtils:formatString name="arraAwardDetailsForm" property = "vendorLess25KAmount" formatType="currencyFormat"/></logic:notEmpty>--%>
												<%if(modeValue || vendorLess25KAmountValue){%> <html:text
													property="strVendorLess25KAmount"
													name="arraAwardDetailsForm" styleId="vendorLess25KAmountId"
													readonly="<%=vendorLess25KAmountValue%>"
													styleClass="cltextbox-nonEditcolor"
													onchange="dataChanged()"
													style="text-align: right; width: 130px "
													value="<%=vendorLess25KAmount%>" maxlength="13" /> <%}else{%>
												<html:text property="strVendorLess25KAmount"
													name="arraAwardDetailsForm" styleId="vendorLess25KAmountId"
													readonly="<%=modeValue%>" styleClass="cltextbox-medium"
													onchange="dataChanged()"
													style="text-align: right; width: 130px "
													value="<%=vendorLess25KAmount%>" maxlength="13" /> <%}%>
											</td>
											<td width="30%" class='copy' nowrap align="right">
												<%--<logic:notEmpty name="arraAwardDetailsForm" property = "subAwdLess25KAmount"><coeusUtils:formatString name="arraAwardDetailsForm" property = "subAwdLess25KAmount" formatType="currencyFormat"/></logic:notEmpty>--%>
												<%if(modeValue || subAwdLess25KAmountValue){%> <html:text
													property="strSubAwdLess25KAmount"
													name="arraAwardDetailsForm" styleId="subAwdLess25KAmountId"
													readonly="<%=subAwdLess25KAmountValue%>"
													styleClass="cltextbox-nonEditcolor"
													onchange="dataChanged()"
													style="text-align: right; width: 130px "
													value="<%=subAwdLess25KAmount%>" maxlength="13" /> <%}else{%>
												<html:text property="strSubAwdLess25KAmount"
													name="arraAwardDetailsForm" styleId="subAwdLess25KAmountId"
													readonly="<%=modeValue%>" styleClass="cltextbox-medium"
													onchange="dataChanged()"
													style="text-align: right; width: 130px "
													value="<%=subAwdLess25KAmount%>" maxlength="13" /> <%}%>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							</tr>
						</table>
					</div>
				</td>
			</tr>

			<tr valign="top">
				<!-- JM 3-26-2012 updated to Vandy colors
<td  align="left" valign="top" colspan='2' bgcolor="#D1E5FF">-->
				<td align="left" valign="top" colspan='2' bgcolor="#e9e9e9">
					<div id="awardHeader" style="display: none;">
						<table width="100%" border="0" align="left" cellpadding="3"
							cellspacing="0">
							<%--<tr>
            <td height="1" colspan="4" bgcolor="#9DBFE9">
                &nbsp;
            </td>
        </tr>--%>
							<tr>
								<td width="15%" nowrap class='copybold'>&nbsp;&nbsp;&nbsp;<bean:message
										bundle="arra" key="headerLabel.awardNumber" />&nbsp;
								</td>
								<td width="30%" class='copy' nowrap>
									<%if(modeValue || awardNumberValue){%> <html:text
										property="awardNumber" name="arraAwardDetailsForm"
										styleId="awardNumberId" readonly="<%=awardNumberValue%>"
										styleClass="cltextbox-nonEditcolor" onchange="dataChanged()"
										style="text-align: right; width: 130px " maxlength="50" /> <%}else{%>
									<html:text property="awardNumber" name="arraAwardDetailsForm"
										styleId="awardNumberId" readonly="<%=modeValue%>"
										styleClass="cltextbox-medium" onchange="dataChanged()"
										style="text-align: right; width: 130px " maxlength="50" /> <%}%>
								</td>
								<td width="10%" nowrap class='copybold' align=left><bean:message
										bundle="arra" key="headerLabel.piName" />&nbsp;</td>
								<td width="40%" class='copy' nowrap>
									<%if(modeValue || piNameValue){%> <html:text property="piName"
										name="arraAwardDetailsForm" styleId="awardNumberId"
										readonly="<%=piNameValue%>"
										styleClass="cltextbox-nonEditcolor" onchange="dataChanged()"
										style="width: 180px " maxlength="90" /> <%}else{%> <html:text
										property="piName" name="arraAwardDetailsForm"
										styleId="awardNumberId" readonly="<%=modeValue%>"
										styleClass="cltextbox-medium" onchange="dataChanged()"
										style="width: 180px " maxlength="90" /> <%}%>
								</td>
							</tr>
							<tr>
								<td width="15%" nowrap class='copybold' align="left">&nbsp;&nbsp;&nbsp;<bean:message
										bundle="arra" key="awardDetails.accountNumber" />&nbsp;
								</td>
								<td width="30%" class='copy' nowrap>
									<%if(modeValue || accountNumberValue){%> <html:text
										property="accountNumber" name="arraAwardDetailsForm"
										styleId="awardNumberId" readonly="<%=accountNumberValue%>"
										styleClass="cltextbox-nonEditcolor" onchange="dataChanged()"
										style="text-align: right; width: 130px " maxlength="50" /> <%}else{%>
									<html:text property="accountNumber" name="arraAwardDetailsForm"
										styleId="awardNumberId" readonly="<%=modeValue%>"
										styleClass="cltextbox-medium" onchange="dataChanged()"
										style="text-align: right; width: 130px " maxlength="50" /> <%}%>
								</td>

								<td width="10%" nowrap class='copybold' align="left"><bean:message
										bundle="arra" key="awardDetails.awardDate" />&nbsp;</td>
								<td width="40%" class='copy' nowrap>
									<table
										<tr>
                        <td>
                            <html:text property="awardDate" name="arraAwardDetailsForm" styleId="awardNumberId" styleClass="cltextbox-nonEditcolor" readonly="true" style="width:70px;" onchange="dataChanged()" maxlength="50"/>
                        </td>
                        <td>
                            <%if(modeValue || awardDateValue){%> 
                            <html:link href="javascript:void(0)" styleId="calID" style="display:none"  onclick="displayCalendarWithTopLeft('awardDate',1,1)" tabindex="32" >
                                <img id="imgIRBDate" title="" height="16" alt="" src="<bean:write name='ctxtPath'/>/coeusliteimages/cal.gif" width="16" onclick='dataChanged()' border="0" runat="server">
                            </html:link>             
                            <%}else{%>
                            <html:link href="javascript:void(0)" styleId="calID" style="display:block"  onclick="displayCalendarWithTopLeft('awardDate',1,1)" tabindex="32" >
                                <img id="imgIRBDate" title="" height="16" alt="" src="<bean:write name='ctxtPath'/>/coeusliteimages/cal.gif" width="16" onclick='dataChanged()' border="0" runat="server">
                            </html:link>  
                            <%}%>
                        </td>
                </table>    
            </td>
        </tr>
										<tr>
            <td class='copybold' align="left">&nbsp;&nbsp;&nbsp;<bean:message bundle="arra" key="headerLabel.sponsor"/>&nbsp;</td>
            <td class='copy' colspan="3" align=left nowrap>
                <table>
                        <tr>
                            <td>
                                <html:text property="sponsorAwardNumber" styleClass="cltextbox-nonEditcolor" style="width: 130px;text-align: right;" onchange="dataChanged()" readonly="true" maxlength="8"/> 
                            </td>
                            <td>
                                <%if(modeValue || sponsorCodeValue){ %>
                                <div id="sponsorCodeId" style="display:none">
                                    <a href="javascript:searchWindow('sponsorSearch')" class='search'>
                                    <u><bean:message bundle="proposal" key="generalInfoProposal.search"/></u></a>
                                </div>
                                <%}else{%> 
                                <div id="sponsorCodeId" style="display:block">
                                    <a href="javascript:searchWindow('sponsorSearch')" class='search'>
                                    <u><bean:message bundle="proposal" key="generalInfoProposal.search"/></u></a>
                                </div>
                                <%}%>
                                <td><html:text property="sponsorName" styleClass="cltextbox-color" readonly="true"/></td>
                            </td>
                        </tr></table>
								</td>
							</tr>
							<tr>
								<td class='copybold' align="left">&nbsp;&nbsp;&nbsp;<bean:message
										bundle="arra" key="headerLabel.leadUnit" />&nbsp;
								</td>
								<td class='copy' colspan="3" align=left nowrap>
									<table>
										<tr>
											<td><html:text property="leadUnit"
													styleClass="cltextbox-nonEditcolor"
													style="width: 130px;text-align: right;"
													onchange="dataChanged()" readonly="true" maxlength="8" /></td>
											<td>
												<%if(modeValue || leadUnitValue){%>
												<div id="leadUnitId" style="display: none">
													<a href="javascript:searchWindow('unitSearch');"
														class="copy"> <u><bean:message bundle="proposal"
																key="generalInfoProposal.search" /></u></a>
												</div> <%}else{%>
												<div id="leadUnitId" style="display: block">
													<a href="javascript:searchWindow('unitSearch');"
														class="copy"> <u><bean:message bundle="proposal"
																key="generalInfoProposal.search" /></u></a>
												</div> <%}%>
											
											<td><html:text property="unitName"
													styleClass="cltextbox-color" readonly="true" /></td>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td class='copybold' valign="top" align="left">&nbsp;&nbsp;&nbsp;<bean:message
										bundle="arra" key="headerLabel.title" />&nbsp;
								</td>
								<td class='copy' colspan="3" align=left>
									<%if(modeValue || projectTitleValue){%> <html:textarea onblur=""
										styleId="titleId" style="width:600px;" property="projectTitle"
										styleClass="cltextbox-nonEditcolor" rows="3" cols="80"
										readonly="<%=projectTitleValue%>" onchange="dataChanged()" />
									<%}else{%> <html:textarea onblur="" styleId="titleId"
										style="width:600px;" property="projectTitle"
										styleClass="textbox-longer" rows="3" cols="80"
										readonly="<%=modeValue%>" onchange="dataChanged()" /> <%}%>
								</td>
							</tr>
							<%--<tr>
            <td class='copybold' valign="top"  align="left">&nbsp;&nbsp;&nbsp;<bean:message bundle="arra" key="headerLabel.finalFlag"/>&nbsp;</td>
            <td class='copy' colspan="3" align=left >
                <%if(modeValue || finalFlagValue){%>
                 <html:checkbox styleId="finalFlagId" property="finalFlag" onchange="dataChanged()" disabled="<%=finalFlagValue%>" />
                <%}else{%>
                <html:checkbox styleId="finalFlagId" property="finalFlag" onchange="dataChanged()" disabled="<%=modeValue%>" />               
                <%}%>
            </td>            
        </tr>--%>
							</div>
						</table>
				</td>
			</tr>

			<tr>
				<td height="3">&nbsp;</td>
			</tr>
			<tr valign="top">
				<!-- JM 3-26-2012 updated to Vandy colors
    <td  align="left" valign="top" colspan='2' bgcolor="#D1E5FF">-->
				<td align="left" valign="top" colspan='2' bgcolor="#e9e9e9">
					<table width="100%" border="0" align="left" cellpadding="3"
						cellspacing="0">
						<tr>
							<td><table>
									<tr>
										<td align="left" class='copybold'>&nbsp;<bean:message
												bundle="arra" key="awardDetails.ProjectStatus" /></td>
										<td align="left" class='copybold'><html:link
												href="javaScript:showHelp('help.ProjectStatus','ProjectStatus');"
												styleId="ProjectStatus">
												<html:img src="<%=help_image%>" border="0" />
											</html:link></td>
									</tr>
								</table></td>
							<td colspan="3" class="copy" align="left">
								&nbsp;&nbsp;&nbsp; <%if(modeValue || projectStatusValue){%> <html:select
									name="arraAwardDetailsForm" styleId="projectStatusId"
									style="width :220px;" styleClass="cltextbox-nonEditcolor"
									property="projectStatus" disabled="<%=projectStatusValue%>"
									onchange="dataChanged()">
									<html:option value="">
										<bean:message bundle="proposal"
											key="generalInfoProposal.pleaseSelect" />
									</html:option>
									<html:options collection="arraAwardProjectStatus"
										property="description" labelProperty="description" />
								</html:select> <%}else{%> <html:select name="arraAwardDetailsForm"
									styleId="projectStatusId" styleClass="cltextbox-medium"
									property="projectStatus" disabled="<%=modeValue%>"
									onchange="dataChanged()">
									<html:option value="">
										<bean:message bundle="proposal"
											key="generalInfoProposal.pleaseSelect" />
									</html:option>
									<html:options collection="arraAwardProjectStatus"
										property="description" labelProperty="description" />
								</html:select> <%}%>
							</td>

						</tr>
						<tr>
							<td><table>
									<tr>
										<td align="left" class='copybold'>&nbsp;<bean:message
												bundle="arra" key="awardDetails.activityCode" /></td>
										<td align="left" class='copybold'><html:link
												href="javaScript:showHelp('help.ActivityCode','activityCode');"
												styleId="activityCode">
												<html:img src="<%=help_image%>" border="0" />
											</html:link></td>
									</tr>
								</table></td>
							<td colspan="3" class="copy" align="left">
								&nbsp;&nbsp;&nbsp; <%if(modeValue || activityCodeValue){%> <html:select
									name="arraAwardDetailsForm" style="width:220px;"
									styleId="activityCodeId" styleClass="cltextbox-nonEditcolor"
									property="activityCode" disabled="<%=activityCodeValue%>"
									onchange="dataChanged()">
									<html:option value="">
										<bean:message bundle="proposal"
											key="generalInfoProposal.pleaseSelect" />
									</html:option>
									<html:options collection="arraAwardActivityTypes"
										property="code" labelProperty="description" />
								</html:select> <%}else{%> <html:select name="arraAwardDetailsForm"
									styleId="activityCodeId" styleClass="cltextbox-medium"
									property="activityCode" disabled="<%=modeValue%>"
									onchange="dataChanged()">
									<html:option value="">
										<bean:message bundle="proposal"
											key="generalInfoProposal.pleaseSelect" />
									</html:option>
									<html:options collection="arraAwardActivityTypes"
										property="code" labelProperty="description" />
								</html:select> <%}%>
							</td>
						</tr>

						<tr>
							<td><table>
									<tr>
										<td nowrap class="copybold" align="left">&nbsp;<bean:message
												bundle="arra" key="awardDetails.awdDescription" /></td>
										<td align="left" class='copybold'><html:link
												href="javaScript:showHelp('help.AwardDescription','awardDescription');"
												styleId="awardDescription">
												<html:img src="<%=help_image%>" border="0" />
											</html:link></td>
									</tr>
								</table></td>
							<td colspan="3" class="copy" align="left">&nbsp;&nbsp;&nbsp;
								<%if(modeValue || awardDescValue){%> <html:textarea onblur=""
									styleId="awardDescriptionId" style="width:600px;"
									property="awardDescription" styleClass="cltextbox-nonEditcolor"
									rows="3" cols="80" readonly="<%=awardDescValue%>"
									onchange="dataChanged()" /> <%}else{%> <html:textarea onblur=""
									styleId="awardDescriptionId" property="awardDescription"
									styleClass="textbox-longer" rows="3" cols="80"
									readonly="<%=modeValue%>" onchange="dataChanged()" /> <%}%>
							</td>
						</tr>
						<tr>
							<td><table>
									<tr>
										<td nowrap class="copybold" align="left">&nbsp;<bean:message
												bundle="arra" key="awardDetails.activityDescription" /></td>
										<td align="left" class='copybold'><html:link
												href="javaScript:showHelp('help.ProjectDescription','projectDescription');"
												styleId="projectDescription">
												<html:img src="<%=help_image%>" border="0" />
											</html:link></td>
									</tr>
								</table></td>
							<td colspan="3" class="copy" align="left">&nbsp;&nbsp;&nbsp;
								<%if(modeValue || activityDescValue){%> <html:textarea onblur=""
									property="activityDescription" style="width:600px;"
									styleId="activityDescriptionId"
									styleClass="cltextbox-nonEditcolor" rows="3" cols="80"
									readonly="<%=activityDescValue%>" onchange="dataChanged()" /> <%}else{%>
								<html:textarea onblur="" property="activityDescription"
									styleId="activityDescriptionId" styleClass="textbox-longer"
									rows="3" cols="80" readonly="<%=modeValue%>"
									onchange="dataChanged()" /> <%}%>
							</td>
						</tr>


						<!--<tr>
            <td nowrap class="copybold"  width="10%"align="left"><bean:message bundle="arra" key="awardDetails.infrastructureRationale"/></td>                                                
                                <td colspan="3" class="copy" width="85%" align="left">
            <html:textarea    onblur="" property="infrastructureRationale" styleId="infrastructureRationale" styleClass="textbox-longer" disabled="<%=modeValue%>" rows ="3" cols="80" onchange="dataChanged()" />
                                </td>
                            </tr>-->
					</table>
				</td>
			</tr>

			<tr>
				<td height="3">&nbsp;</td>
			</tr>

			<tr>
				<td align="left" valign="top" colspan="2"><table width="100%"
						border="0" cellpadding="0" cellspacing="0" class="tableheader">
						<tr>
							<td class="tableheader" height='20'>
								<div id='showjobsCreated' style='display: none;'>
									<html:link
										href="javaScript:showHideDetails('showjobsCreated');">
										<html:img src="<%=plus%>" border="0" />
									</html:link>
									&nbsp;
									<bean:message bundle="arra" key="awardDetails.jobsCreated" />

								</div>
								<div id='hidejobsCreated'>
									<html:link
										href="javaScript:showHideDetails('hidejobsCreated');">
										<html:img src="<%=minus%>" border="0" />
									</html:link>
									&nbsp;
									<bean:message bundle="arra" key="awardDetails.jobsCreated" />
								</div>
							</td>
						</tr>
					</table></td>
			</tr>

			<tr valign=top>
				<!--  JM 3-26-2012 updated to Vandy colors
    <td  width="100%" align="left" valign="top" colspan='2' bgcolor="#D1E5FF"> -->
				<td width="100%" align="left" valign="top" colspan='2'
					bgcolor="#e9e9e9">
					<div id='jobsCreated'>
						<table width="100%" align="left" border="0" cellpadding="3"
							cellspacing='0' class="tabtable">
							<tr>
								<td colspan="4">
									<table width="100%" border="0" align="left" cellpadding="3"
										cellspacing="0">
										<tr>
											<td nowrap class="copybold" align="left"><bean:message
													bundle="arra" key="awardDetails.NumOfJobs" />:&nbsp;<html:link
													href="javaScript:showHelp('help.NoOfJobs','noOfJobs');"
													styleId="noOfJobs">
													<html:img src="<%=help_image%>" border="0" />
												</html:link></td>
											<td class="copybold" align="left">
												<%if(modeValue || numberOfJobsValue){%> <html:text onblur=""
													property="noOfJobs" styleId="noOfJobsId"
													styleClass="cltextbox-nonEditcolor"
													style="width:90;text-align: right"
													readonly="<%=numberOfJobsValue%>" onchange="findTotal('0')"
													maxlength="13" /> <%}else{%> <html:text onblur=""
													property="noOfJobs" styleId="noOfJobsId"
													styleClass="cltextbox-medium"
													style="width:90;text-align: right"
													readonly="<%=modeValue%>" onchange="findTotal('0')"
													maxlength="13" /> <%}%> +&nbsp;<bean:message bundle="arra"
													key="awardDetails.jobsAtSubs" /> <html:text
													name="arraAwardDetailsForm" property="jobsAtSubs"
													styleId="jobsAtSubsId" styleClass="cltextbox-nonEditcolor"
													style="width: 90px;text-align: right" disabled="false"
													readonly="true" />&nbsp;&nbsp;&nbsp;= &nbsp;&nbsp;<html:text
													name="arraAwardDetailsForm" property="totalJobs"
													styleId="totalJobsId"
													style="width: 90px; text-align: right"
													styleClass="cltextbox-nonEditcolor" disabled="false"
													readonly="true" />
											</td>
										</tr>
										<tr>
											<td nowrap class="copybold" valign="top" width="10%"
												align="left"><bean:message bundle="arra"
													key="awardDetails.descriptionOfJobsCreated" />&nbsp;<html:link
													href="javaScript:showHelp('help.EmploymentImpact','employmentImpact');"
													styleId="employmentImpact">
													<html:img src="<%=help_image%>" border="0" />
												</html:link></td>
											<td colspan="4" valign="top" class="copy" align="left">
												<%if(modeValue || jobsCreatedDesValue){%> <html:textarea
													onblur="" property="employmentImpact"
													styleId="employmentImpactId" style="width:595"
													styleClass="cltextbox-nonEditcolor" rows="3"
													readonly="<%=jobsCreatedDesValue%>"
													onchange="dataChanged()" /> <%}else{%> <html:textarea
													onblur="" property="employmentImpact"
													styleId="employmentImpactId" style="width:595"
													styleClass="textbox-longer" rows="3"
													readonly="<%=modeValue%>" onchange="dataChanged()" /> <%}%>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td width="30%" align="left" class="theader">&nbsp;&nbsp;<bean:message
										bundle="arra" key="awardDetails.personName" /></td>
								<td width="20%" align="left" class="theader"><bean:message
										bundle="arra" key="awardDetails.jobTitle" /></td>
								<td width="10%" align="left" class="theader"><bean:message
										bundle="arra" key="awardDetails.fte" /></td>
								<td width="40%" align="left" class="theader">&nbsp;&nbsp;</td>
							</tr>
							<%  String strBgColor = "#DCE5F1";
                int count = 0;
                %>
							<logic:present name="arraAwardJobsCreated">
								<logic:iterate id="job" name="arraAwardJobsCreated"
									type="org.apache.struts.validator.DynaValidatorForm"
									indexId="index">

									<% 
                        if (count%2 == 0)
                            strBgColor = "#D6DCE5";
                        else
                            strBgColor="#DCE5F1";
                        %>
									<tr bgcolor="<%=strBgColor%>" valign="top"
										onmouseover="className='TableItemOn'"
										onmouseout="className='TableItemOff'">
										<td width="30%" align="left" class="copy"><bean:write
												name="job" property="personName" /></td>
										<td width=20% " align="left" class="copy"><bean:write
												name="job" property="jobTitle" /></td>
										<td width=10% " align="left" class="copy"><bean:write
												name="job" property="fte" /></td>
										<td width=40% " align="left" class="copy">&nbsp;&nbsp;</td>
									</tr>
									<% count++;%>
								</logic:iterate>
							</logic:present>
						</table>
					</div>
				</td>
			</tr>

			<tr>
				<td height="3">&nbsp;</td>
			</tr>

			<%if(showInfraStructure){%>
			<tr>
				<td align="left" valign="top" colspan="2"><table width="100%"
						border="0" cellpadding="0" cellspacing="0" class="tableheader">
						<tr>
							<td class="tableheader" height='20'>
								<div id='showInfraContact'>
									<html:link
										href="javaScript:showHideDetails('showInfraContact');">
										<html:img src="<%=plus%>" border="0" />
									</html:link>
									&nbsp;
									<bean:message bundle="arra"
										key="awardDetails.infrastructureContact" />

								</div>
								<div id='hideInfraContact' style='display: none;'>
									<html:link
										href="javaScript:showHideDetails('hideInfraContact');">
										<html:img src="<%=minus%>" border="0" />
									</html:link>
									&nbsp;
									<bean:message bundle="arra"
										key="awardDetails.infrastructureContact" />
								</div>
							</td>
						</tr>
					</table></td>
			</tr>
			<tr valign="top">
				<!--  JM 3-26-2012 updated to Vandy colors
    <td align="left" valign="top" colspan='2' bgcolor="#D1E5FF">-->
				<td align="left" valign="top" colspan='2' bgcolor="#e9e9e9">
					<div id='infraContact' style='display: none;'>
						<table width="100%" border="0" align="left" cellpadding="3"
							cellspacing="0">
							<tr>

								<td width="20%" nowrap class="copybold" align="left"
									valign="top">&nbsp;<bean:message bundle="arra"
										key="awardDetails.Address" /></td>
								<td class="copy" align="left" width="30%">
									<%if(modeValue || infrastructrueContactIdValue){%> <html:textarea
										name="arraAwardDetailsForm" property="infraContactAddress"
										style="width:220px;" styleId="infraContactAddressId"
										readonly="true" rows="4" cols="20"
										styleClass="cltextbox-nonEditcolor" onchange="dataChanged()" />
									<%}else{%> <html:textarea name="arraAwardDetailsForm"
										property="infraContactAddress" styleId="infraContactAddressId"
										readonly="true" rows="4" cols="20"
										styleClass="cltextbox-color" onchange="dataChanged()" /> <%}%>
								</td>
								<td align="left" valign="top">
									<%String infraContactSearch = "javaScript:searchWindow('infraContactSearch')";
                        if(modeValue){%> <html:link href="#"
										styleId="infrastructrueContactStyleId" style="display:none">
										<bean:message bundle="proposal"
											key="proposalOrganization.Search" />
									</html:link> <%}else{%> <%if(!infrastructrueContactIdValue){%> <html:link
										href="<%=infraContactSearch%>"
										styleId="infrastructrueContactStyleId" style="display:block">
										<bean:message bundle="proposal"
											key="proposalOrganization.Search" />
									</html:link> <%}else{%> <html:link href="<%=infraContactSearch%>"
										styleId="infrastructrueContactStyleId" style="display:none">
										<bean:message bundle="proposal"
											key="proposalOrganization.Search" />
									</html:link> <%}%> <%}%>
								</td>
							</tr>
						</table>
					</div>
				</td>
			</tr>
			<tr valign="top">
				<!--  JM 3-26-2012 updated to Vandy colors
    <td align="left" valign="top" colspan='2' bgcolor="#D1E5FF">-->
				<td align="left" valign="top" colspan='2' bgcolor="#e9e9e9">
					<div id='infraRationale' style='display: none;'>
						<table border="0" align="left" cellpadding="0" cellspacing="3">
							<tr>
								<td nowrap class="copybold" valign="top" align="left">&nbsp;<bean:message
										bundle="arra" key="awardDetails.infrastructureRationale" />
								</td>
								<td align="left">&nbsp;&nbsp; <%if(modeValue || infraStrucRationleValue){%>
									<html:textarea onblur="" property="infrastructureRationale"
										styleId="infrastructureRationaleId"
										styleClass="cltextbox-nonEditcolor"
										readonly="<%=infraStrucRationleValue%>" rows="3" cols="80"
										style="width:600" onchange="dataChanged()" /> <%}else{%> <html:textarea
										onblur="" property="infrastructureRationale"
										styleId="infrastructureRationaleId"
										styleClass="textbox-longer" readonly="<%=modeValue%>" rows="3"
										cols="80" style="width:600" onchange="dataChanged()" /> <%}%>
								</td>
							</tr>
							<%String totInfraAmt="",strTotInfraAmt="",strAmt="";%>
							<logic:present name="arraAwardDetailsForm"
								property="totalInfraExpenditure">
								<bean:define id="totInfra" name="arraAwardDetailsForm"
									property="totalInfraExpenditure" type="java.lang.Double" />
								<%totInfraAmt = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(((Double)totInfra).doubleValue());
                    %>
							</logic:present>

							<logic:present name="arraAwardDetailsForm"
								property="strTotalInfraExpenditure">
								<bean:define id="strTotInfra" name="arraAwardDetailsForm"
									property="strTotalInfraExpenditure" type="java.lang.String" />
								<%strTotInfraAmt = (String)strTotInfra;%>
							</logic:present>
							<% 
                /*    if(totInfra!=null){
                totInfraAmt = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(((Double)totInfra).doubleValue());
                }else{
                totInfraAmt = "";
                } */
                if(strTotInfraAmt!=null && !strTotInfraAmt.equals("")){
                        strAmt  = strTotInfraAmt;
                        strTotInfraAmt = strTotInfraAmt.replaceAll("[$,/,]","");
                        try{
                            strTotInfraAmt = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(new Double(strTotInfraAmt));
                        }catch(java.lang.NumberFormatException ne){
                            strTotInfraAmt = strAmt;
                        }
                        totInfraAmt = strTotInfraAmt;
                }
                %>
							<tr>

								<td colspan="1" width="10%" nowrap class='copybold' align=left><bean:message
										bundle="arra" key="awardDetails.totalInfraExpenditure" />&nbsp;
									<%--<logic:notEmpty name="arraAwardDetailsForm" property = "totalInfraExpenditure"><coeusUtils:formatString name="arraAwardDetailsForm" property="totalInfraExpenditure" formatType="currencyFormat"/></logic:notEmpty></td> --%>
								<td>&nbsp;&nbsp;&nbsp; <%if(modeValue || totalFedArraInfraExpendValue){%>
									<html:text property="strTotalInfraExpenditure"
										name="arraAwardDetailsForm"
										styleId="strTotalInfraExpenditureId"
										readonly="<%=totalFedArraInfraExpendValue%>"
										styleClass="cltextbox-nonEditcolor" value="<%=totInfraAmt%>"
										onchange="dataChanged()"
										style="text-align: right; width: 130px " maxlength="13" /> <%}else{%>
									<html:text property="strTotalInfraExpenditure"
										name="arraAwardDetailsForm"
										styleId="strTotalInfraExpenditureId" readonly="<%=modeValue%>"
										styleClass="cltextbox-medium" value="<%=totInfraAmt%>"
										onchange="dataChanged()"
										style="text-align: right; width: 130px " maxlength="13" /> <%}%>

								</td>
							</tr>
						</table>
					</div>
				</td>
			</tr>

			<tr>
				<td height="3">&nbsp;</td>
			</tr>
			<%}%>

			<tr>
				<td align="left" valign="top" colspan="2"><table width="100%"
						border="0" cellpadding="0" cellspacing="0" class="tableheader">
						<tr>
							<td class="tableheader" height='20'>
								<div id='showPrimPlace'>
									<html:link href="javaScript:showHideDetails('showPrimPlace');">
										<html:img src="<%=plus%>" border="0" />
									</html:link>
									&nbsp;
									<bean:message bundle="arra" key="awardDetails.primPlaceofPerf" />

								</div>
								<div id='hidePrimPlace' style='display: none;'>
									<html:link href="javaScript:showHideDetails('hidePrimPlace');">
										<html:img src="<%=minus%>" border="0" />
									</html:link>
									&nbsp;
									<bean:message bundle="arra" key="awardDetails.primPlaceofPerf" />
								</div>
							</td>
						</tr>
					</table></td>
			</tr>

			<tr>
				<!--  JM 3-26-2012 updated to Vandy colors
    <td height="100%" width="40%" valign="top" bgcolor="#D1E5FF">-->
				<td height="100%" width="40%" valign="top" bgcolor="#e9e9e9">
					<div id='primPlace1' style='display: none;'>
						<!-- Left section -->
						<table width="40%" align="left" border="0" cellpadding="3"
							cellspacing="0">
							<tr>
								<td nowrap class="copybold" width="10%" align="left"
									valign="top">&nbsp;<bean:message bundle="arra"
										key="awardDetails.Address" /></td>
								<td class="copy" width="70%" align="left">
									<%if(modeValue || primPlaceOfPerfValue){%> <html:textarea
										name="arraAwardDetailsForm" style="width:220px;"
										property="primPlaceOfPerfAddress"
										styleId="primPlaceOfPerfAddressId" readonly="true" rows="4"
										cols="20" styleClass="cltextbox-nonEditcolor" /> <%}else{%> <html:textarea
										name="arraAwardDetailsForm" property="primPlaceOfPerfAddress"
										styleId="primPlaceOfPerfAddressId" readonly="true" rows="4"
										cols="20" styleClass="cltextbox-color" /> <%}%>
								</td>
								<td width="10%" align="left" valign="top">
									<%String primaryPlaceSearch = "javaScript:searchWindow('primaryPlaceSearch')";
                        if(modeValue){%> <html:link href="#"
										styleId="primPlaceOfPerfStyleId" style="display:none">
										<bean:message bundle="proposal"
											key="proposalOrganization.Search" />
									</html:link> <%}else{%> <%if(!primPlaceOfPerfValue){%> <html:link
										href="<%=primaryPlaceSearch%>"
										styleId="primPlaceOfPerfStyleId" style="display:block">
										<bean:message bundle="proposal"
											key="proposalOrganization.Search" />
									</html:link> <%}else{%> <html:link href="<%=primaryPlaceSearch%>"
										styleId="primPlaceOfPerfStyleId" style="display:none">
										<bean:message bundle="proposal"
											key="proposalOrganization.Search" />
									</html:link> <%}%> <%}%>
								</td>
							</tr>
						</table>
					</div>
				</td>
				<!--  JM 3-26-2012 updated to Vandy colors
    <td align="left" valign="top" width="40%" height="100%" bgcolor="#D1E5FF">-->
				<td align="left" valign="top" width="40%" height="100%"
					bgcolor="#e9e9e9">
					<div id='primPlace2' style='display: none;'>
						<!--Right Section Start -->
						<table width="40%" align="left" border="0" cellpadding="3"
							cellspacing="0">
							<tr>
								<td nowrap class="copybold" align="left">&nbsp;<bean:message
										bundle="arra" key="awardDetails.congrDistrict" />&nbsp;&nbsp;
								</td>
								<td class="copy" align="left">
									<%if(modeValue || primPlaceOfPerfCongDistValue){%> <html:text
										onblur="" property="congressionalDistrict" style="width:40px;"
										styleId="congressionalDistrictId"
										styleClass="cltextbox-nonEditcolor" maxlength="2"
										readonly="<%=primPlaceOfPerfCongDistValue%>"
										onchange="dataChanged()" /> <%}else{%> <html:text onblur=""
										property="congressionalDistrict"
										styleId="congressionalDistrictId" styleClass="textbox"
										maxlength="2" readonly="<%=modeValue%>"
										onchange="dataChanged()" /> <%}%>
								</td>
							</tr>
						</table>
					</div>
				</td>
			</tr>

			<tr>
				<td height="3">&nbsp;</td>
			</tr>

			<tr>
				<td colspan='4' class='savebutton'><html:button property="Save"
						value="Save" disabled="<%=modeValue%>" onclick="updateData();" />
				</td>
			</tr>



			<tr>
				<td height="3">&nbsp;</td>
			</tr>

			<%if(showHighlyComp){%>
			<tr>
				<td align="left" valign="top" colspan="2"><table width="100%"
						border="0" cellpadding="0" cellspacing="0" class="tableheader">
						<tr>
							<td class="tableheader" height='20'>
								<div id='showHighlyComp'>
									<html:link href="javaScript:showHideDetails('showHighlyComp');">
										<html:img src="<%=plus%>" border="0" />
									</html:link>
									&nbsp;
									<bean:message bundle="arra"
										key="awardDetails.highlyCompensated" />

								</div>
								<div id='hideHighlyComp' style='display: none;'>
									<html:link href="javaScript:showHideDetails('hideHighlyComp');">
										<html:img src="<%=minus%>" border="0" />
									</html:link>
									&nbsp;
									<bean:message bundle="arra"
										key="awardDetails.highlyCompensated" />
								</div>
							</td>
						</tr>
					</table></td>
			</tr>

			<tr valign=top>
				<!--  JM 3-26-2012 updated to Vandy colors  
    <td height="45%" width="80%" align="left" valign="top" colspan='2' bgcolor="#D1E5FF"> -->
				<td height="45%" width="80%" align="left" valign="top" colspan='2'
					bgcolor="#e9e9e9">
					<div id='highlyComp' style='display: none;'>
						<table width="100%" align="left" border="0" cellpadding="3"
							cellspacing='0' class="tabtable">
							<tr>
								<td width="30%" nowrap align="left" class="theader">&nbsp;&nbsp;<bean:message
										bundle="arra" key="awardDetails.personName" /></td>
								<td width="10%" nowrap align="right" class="theader"><bean:message
										bundle="arra" key="awardDetails.compensationAmt" /></td>
								<td width="50%" align="left" class="theader">&nbsp;&nbsp;</td>
							</tr>

							<%  count = 0;
                %>
							<logic:present name="arraHighlyCompensated">
								<logic:iterate id="highlyComp" name="arraHighlyCompensated"
									type="org.apache.struts.validator.DynaValidatorForm"
									indexId="index">
									<tr bgcolor="<%=strBgColor%>" valign="top"
										onmouseover="className='TableItemOn'"
										onmouseout="className='TableItemOff'">
										<td width="30%" align="left" class="copy"><bean:write
												name="highlyComp" property="personName" /></td>
										<td width=20% " align="right" class="copy"><logic:notEmpty
												name="highlyComp" property="compensation">
												<coeusUtils:formatString name="highlyComp"
													property="compensation" formatType="currencyFormat" />
											</logic:notEmpty></td>
										<td width=50% " align="left" class="copy">&nbsp;&nbsp;</td>
									</tr>
									<% count++;%>
								</logic:iterate>
							</logic:present>
						</table>
					</div>
				</td>
			</tr>

			<tr>
				<td height="3">&nbsp;</td>
			</tr>
			<%}%>
			<html:hidden property="primPlaceOfPerfId" />

			<html:hidden property="infraContactId" />
			<html:hidden property="arraReportNumber" />
			<html:hidden property="versionNumber" />
			<html:hidden property="mitAwardNumber" />
			<html:hidden property="sequenceNumber" />
			<html:hidden property="updateUser" />
			<html:hidden property="updateTimestamp" />
		</table>
	</html:form>


	<logic:messagesPresent
		message="awardDetails.error.infraRationaleLength">
		<script>showHideDetails('showInfraContact');</script>
	</logic:messagesPresent>
	<logic:messagesPresent message="subcontract.error.emplImpactLength">
		<script>showHideDetails('showJobsCreated');</script>
	</logic:messagesPresent>
	<logic:messagesPresent
		message="awardDetails.error.invalidTotalInfraExpediture">
		<script>showHideDetails('showInfraContact');</script>
	</logic:messagesPresent>
	<script>
      DATA_CHANGED = 'false';
      if(errValue && !errLock) {
            DATA_CHANGED = 'true';
      }
      LINK = "<%=request.getContextPath()%>/saveArraAwardDetails.do";
      FORM_LINK = document.arraAwardDetailsForm;
      PAGE_NAME = "<bean:message bundle="arra" key="awardDetails.arraawardDetails"/>";
      function dataChanged(){
        <%if(!modeValue){ %>
            DATA_CHANGED = 'true';
        <%}%>
      }
      linkForward(errValue);
</script>
	<%
//Arra changes - Popup style is changed and a close button is added to close the popup
String helpArr[] = {"help.ProjectStatus","help.ActivityCode", "help.AwardDescription", "help.ProjectDescription", "help.NoOfJobs", "help.EmploymentImpact", "help.SubawardsToIndividuals", "help.VendorsLessThan25K", "help.SubawardsLessThan25K"};
for(int index=0; index < helpArr.length; index++){%>
	<div id="<%=helpArr[index]%>" class="mbox"
		style="position: absolute; width: 450; height: 225; display: none;">
		<table cellpadding='0' cellspacing='0' border="2" align=left
			class='tabtable' width="450">
			<tr class='theader'>
				<td width='100%'><bean:message bundle="arra"
						key="awardDetails.help" /></td>
			</tr>
		</table>
		<br />
		<div id="<%=helpArr[index]%>2"
			style="overflow: auto; width: 450; height: 186">
			<table border="2" id="<%=helpArr[index]%>Tab" cellpadding="2"
				cellspacing="2" class="lineBorderWhiteBackgrnd" width="450"
				height="50">
				<tr>
					<td><bean:message bundle="arra" key="<%=helpArr[index]%>" /></td>
				</tr>
			</table>
		</div>
		<input type="button"
			onclick="javascript:hideHelp('<%=helpArr[index]%>')" value="Close">
	</div>
	<%}
%>
</body>
<script>
function checkErrors(){
    <%String submitErrorKey = (String)request.getAttribute("arraSubmitErrorKey");
    if(submitErrorKey != null){
        submitErrorKey = "arraSubmit.submitError."+submitErrorKey;%>
        alert("<bean:message bundle="arra" key="<%=submitErrorKey%>"/>");
    <%}%>
}
</script>
</html:html>
