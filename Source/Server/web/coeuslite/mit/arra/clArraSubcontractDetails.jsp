
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@page import="org.apache.struts.taglib.html.JavascriptValidatorTag"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@ page
	import="edu.mit.coeuslite.arra.bean.ArraAwardHeaderBean,edu.mit.coeuslite.utils.CoeusLiteConstants"%>
<jsp:useBean id="subcontractsColumnProperties" scope="session"
	class="java.util.HashMap" />
<jsp:useBean id="vendorColumnProperties" scope="session"
	class="java.util.HashMap" />
<jsp:useBean id="highlyCompensColumnProperties" scope="session"
	class="java.util.HashMap" />
<bean:size id="vendorsSize" name="subcontractDynaBeansList"
	property="list" />
<bean:size id="highlyCompensatedsSize" name="subcontractDynaBeansList"
	property="beanList" />
<html:html locale="true">
<head>
<title>CoeusLite</title>
<style>
.cltextbox-color {
	font-weight: normal;
	width: 220px
}

.textbox {
	width: 100px;
}

.textbox-longer {
	width: 540px;
}
</style>

<script language="javascript" type="text/JavaScript"
	src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/coeusLiteCalendar.js"></script>
<script>  
            
        var searchSelection = "";
        var errValue = false;
        var typeCode = '';        
               
          function goBacktoSubcontract(){
            CLICKED_LINK = "<%=request.getContextPath()%>/getArraSubcontracts.do";
            if(validate()) {
                document.subcontractDynaBeansList.action = "<%=request.getContextPath()%>/getArraSubcontracts.do";
                document.subcontractDynaBeansList.submit();
            }
        }
        
        function showHideVendor(val){
       
            if(val==1) {
                document.getElementById('vendorDetails').style.display = 'block';
                document.getElementById('hideVendor').style.display = 'block';
                document.getElementById('showVendor').style.display = 'none';
            } else if(val==2) {
                document.getElementById('vendorDetails').style.display = 'none';
                document.getElementById('hideVendor').style.display = 'none';
                document.getElementById('showVendor').style.display = 'block';   
            }
        }
        
        function showHideIndividuals(val){
            if(val==1) {
                document.getElementById('individualDetails').style.display = 'block';
                document.getElementById('hideInd').style.display = 'block';
                document.getElementById('showInd').style.display = 'none';
            } else if(val==2) {
                document.getElementById('individualDetails').style.display = 'none';
                document.getElementById('hideInd').style.display = 'none';
                document.getElementById('showInd').style.display = 'block';   
            }
        }
        
         function searchWindow(searchType) {
                
                searchSelection = searchType;
                var winleft = (screen.width - 650) / 2;
                var winUp = (screen.height - 450) / 2;  
                var win = "scrollbars=1,resizable=1,width=750,height=450,left="+winleft+",top="+winUp;
                sList = window.open('<%=request.getContextPath()%>/generalProposalSearch.do?type=<bean:message key="searchWindow.title.person" bundle="proposal"/>&search=true&searchName=WEBROLODEXSEARCH', "list", win);  
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
            var modifiedValue = document.getElementsByName('dynaFormInfo[0].primPlaceOfPerfId');
            modifiedValue[0].value = roloId;
            modifiedValue = document.getElementsByName('dynaFormInfo[0].primPlaceOfPerfAddress');
            modifiedValue[0].value = roloAddress; 
            dataChanged();
            }  
            
            function addVendor(){
                document.subcontractDynaBeansList.action = "<%=request.getContextPath()%>/addSubcontractVendor.do";
                document.subcontractDynaBeansList.submit();     
            }     
            
            function addIndividual(){
                document.subcontractDynaBeansList.action = "<%=request.getContextPath()%>/addHighlyCompIndividual.do";
                document.subcontractDynaBeansList.submit();     
            }
            
            function removeVendors(unitIndex) {
             if(confirm("<bean:message bundle="arra" key="awardVendors.deleteConfirm"/>")){
                document.subcontractDynaBeansList.action = "<%=request.getContextPath()%>/removeVendorDetails.do?vendorIndex="+unitIndex;
                document.subcontractDynaBeansList.submit();      
             }
            }
       
       function removeIndividual(unitIndex) {       
            if(confirm("<bean:message bundle="arra" key="highlyCompensated.deleteConfirm"/>")){
                document.subcontractDynaBeansList.action = "<%=request.getContextPath()%>/removeIndivdualDetails.do?indIndex="+unitIndex;
                document.subcontractDynaBeansList.submit();    
           }
       }
       
       function updateData(){
               document.subcontractDynaBeansList.action = "<%=request.getContextPath()%>/saveSubcontractDetails.do";
                document.subcontractDynaBeansList.submit();
      }
      
      function isNumber(evt)
        {
            var e = event || evt; // for trans-browser compatibility
            var charCode = e.which || e.keyCode;

            if (charCode > 31 && (charCode < 48 || charCode > 57))
		return false;

            return true;
        }
        
        




function showHidePlaceOfPerformace(val){
        
            if(val==1) {
                document.getElementById('primPlace1').style.display = 'block';
                document.getElementById('primPlace2').style.display = 'block';
                document.getElementById('hidePrimPlace').style.display = 'block';
                document.getElementById('showPrimPlace').style.display = 'none';
            } else if(val==2) {
                document.getElementById('primPlace1').style.display = 'none';
                document.getElementById('primPlace2').style.display = 'none';
                document.getElementById('hidePrimPlace').style.display = 'none';
                document.getElementById('showPrimPlace').style.display = 'block';    
            }
        }
        
    function enableFields(vendorSize,highlyCompensatedsSize){    
        document.getElementById("subcontractCongDistId").readOnly=false;
        document.getElementById("subcontractCongDistId").className="cltextbox-medium";        
        
        document.getElementById("noOfJobsId").readOnly=false;
        document.getElementById("noOfJobsId").className="cltextbox-medium";
        
        document.getElementById("employmentImpactId").readOnly=false;    
        document.getElementById("employmentImpactId").className="textbox-longer";
        
        document.getElementById("subAwardAmountId").readOnly=false;    
        document.getElementById("subAwardAmountId").className="textbox-medium";
        
        document.getElementById("subAwardAmtDispursedId").readOnly=false;    
        document.getElementById("subAwardAmtDispursedId").className="textbox-medium";
        
        document.getElementById("subRecipientDUNSId").readOnly=false;    
        document.getElementById("subRecipientDUNSId").className="textbox-medium";
        
        document.getElementById("subAwardNumberId").readOnly=false;    
        document.getElementById("subAwardNumberId").className="textbox-medium";
        
       document.getElementById("calId").style.display="block";
               
        for(var index=0;index < vendorSize;index++){
        var vendorNameId = "vendorNameId"+index;  
        var vendorDUNSId = "vendorDUNSId"+index;  
        var zipCodeId = "zipCodeId"+index;  
        var paymentAmtId = "paymentAmtId"+index;  
        var serviceDescId = "serviceDescId"+index; 
        document.getElementById(vendorNameId).readOnly=false;   
        document.getElementById(vendorNameId).className="cltextbox-medium";
        document.getElementById(vendorDUNSId).readOnly=false;
        document.getElementById(vendorDUNSId).className="cltextbox-medium";
        document.getElementById(zipCodeId).readOnly=false;
        document.getElementById(zipCodeId).className="textbox";
        document.getElementById(paymentAmtId).readOnly=false;
        document.getElementById(paymentAmtId).className="textbox";
        document.getElementById(serviceDescId).readOnly=false;
        document.getElementById(serviceDescId).className="textbox-longer";
        }
        for(var count=0;count < highlyCompensatedsSize;count++){
        var personNameId = "personNameId"+count;  
        var compensationAmtId = "compensationAmtId"+count; 
        document.getElementById(personNameId).readOnly=false;
        document.getElementById(personNameId).className="cltextbox-medium";
        document.getElementById(compensationAmtId).readOnly=false;
        document.getElementById(compensationAmtId).className="cltextbox-medium";
        }
  
        document.getElementById("primPlaceCongDistId").readOnly=false;        
        document.getElementById("primPlaceCongDistId").className="textbox";
        
        
        document.getElementById("primPlaceOfPerfAddressId").className="cltextbox-color";
        
        //document.getElementById("removeVendorsId").style.display="block"; 
        //document.getElementById("addVendorsId").style.display="block"; 
        //document.getElementById("addHighlyCompenId").style.display="block";
        //document.getElementById("removeHighlyCompenId").style.display="block";
        document.getElementById("primaryPlaceSearchId").style.display="block";  
        
    }
    
    function disableEditAll(){    
     document.getElementById("editAll").style.display="none";  
    }
        </script>
</head>
<body>
	<% ArraAwardHeaderBean headerBean = (ArraAwardHeaderBean)session.getAttribute("arraAwardHeaderBean");
        Boolean editAllRights = (Boolean)session.getAttribute("canEditAllRight");
        
        String subcontractCongDistProperty = "";
        String noOfJobsProperty = "";
        String jobsCreatedDescProperty = "";                
        String vendorDetailsProperty = "";
        String highlyCompensatedIndivProperty = "";
        String primPlaceOfPerfCongDistProperty = "";
        String primPlaceOfPerfProperty = "";
        String subRecipientDunsProperty = "";
        String subAwdAmtProperty = "";
        String subAwdAmtDispursedProperty = "";
        String subAwdDateProperty = "";
        String subAwdNumberProperty = "";
        
        String vendorNameProp = "";
        String vendorDUNSProp = "";
        String zipCodeProp = "";
        String paymentAmtProp = "";
        String serviceDescProp = "";
        
        String personNameProp = "";
        String compensationAmtProp = "";
           
        if(subcontractsColumnProperties != null && subcontractsColumnProperties.size()>0){
              subcontractCongDistProperty = (String)subcontractsColumnProperties.get("SUB_RECIPIENT_CONG_DIST"); 
              noOfJobsProperty = (String)subcontractsColumnProperties.get("NUMBER_OF_JOBS");   
              jobsCreatedDescProperty = (String)subcontractsColumnProperties.get("JOBS_CREATED_DESCRIPTION");                
              vendorDetailsProperty = (String)subcontractsColumnProperties.get("SUBCONTRACT_CODE");            
              highlyCompensatedIndivProperty = (String)subcontractsColumnProperties.get("SUBCONTRACTOR_ID");
              primPlaceOfPerfProperty = (String)subcontractsColumnProperties.get("PRIM_PLACE_OF_PERF");
              primPlaceOfPerfCongDistProperty = (String)subcontractsColumnProperties.get("PRIM_PLACE_OF_PERF_CONG_DIST");
              subRecipientDunsProperty = (String)subcontractsColumnProperties.get("SUB_RECIPIENT_DUNS");
              subAwdAmtProperty = (String)subcontractsColumnProperties.get("SUBAWARD_AMOUNT");
              subAwdAmtDispursedProperty = (String)subcontractsColumnProperties.get("SUBAWARD_AMOUNT_DISPURSED");
              subAwdDateProperty = (String)subcontractsColumnProperties.get("SUBAWARD_DATE");
              subAwdNumberProperty = (String)subcontractsColumnProperties.get("SUBCONTRACT_NUMBER");
        }
        if(vendorColumnProperties != null && vendorColumnProperties.size()>0){
              vendorNameProp = (String)vendorColumnProperties.get("VENDOR_NAME"); 
              vendorDUNSProp = (String)vendorColumnProperties.get("VENDOR_DUNS");   
              zipCodeProp = (String)vendorColumnProperties.get("VENDOR_HQ_ZIP_CODE");                
              paymentAmtProp = (String)vendorColumnProperties.get("PAYMENT_AMOUNT");            
              serviceDescProp = (String)vendorColumnProperties.get("SERVICE_DESCRIPTION");
        }
        
        if(highlyCompensColumnProperties != null && highlyCompensColumnProperties.size()>0){
             personNameProp = (String)highlyCompensColumnProperties.get("PERSON_NAME"); 
             compensationAmtProp = (String)highlyCompensColumnProperties.get("COMPENSATION");   
        }
        boolean modeValue = false;
        boolean subcontractCongDistValue = true;
        boolean  noOfJobsValue = true;
        boolean jobsCreatedDescValue = true;
        boolean vendorDetailsValue = true;
        boolean highlyCompensatedIndivValue = true;
        boolean primPlaceOfPerfCongDistValue = true;
        boolean primPlaceOfPerfValue = true;
        boolean subRecipientDunsValue = true;
        boolean subAwdAmtValue = true;
        boolean subAwdAmtDispursedValue = true;
        boolean subAwdDateValue = true;
        boolean subAwdNumberValue = true;
        
        boolean vendorNameValue = true;
        boolean vendorDUNSValue = true;
        boolean zipCodeValue = true;
        boolean paymentAmtValue = true;
        boolean serviceDescValue = true;
        
        boolean personNameValue = true;
        boolean compensationAmtValue = true;
        
        String mode=(String)session.getAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId());
        if(headerBean!=null && "Y".equalsIgnoreCase(headerBean.getComplete()) || "S".equalsIgnoreCase(headerBean.getComplete())) {
            modeValue=true;
        }else if(CoeusLiteConstants.DISPLAY_MODE.equalsIgnoreCase(mode)){
            modeValue=true;
        }else if(CoeusLiteConstants.MODIFY_MODE.equalsIgnoreCase(mode)){
            modeValue=false;
             if("yes".equalsIgnoreCase(subcontractCongDistProperty)){
                subcontractCongDistValue = false;
            }  if("yes".equalsIgnoreCase(noOfJobsProperty)){
                noOfJobsValue = false;
            }  if("yes".equalsIgnoreCase(jobsCreatedDescProperty)){
                jobsCreatedDescValue = false;
            }
            if("yes".equalsIgnoreCase(vendorDetailsProperty)){
                vendorDetailsValue = false;
            }
            if("yes".equalsIgnoreCase(highlyCompensatedIndivProperty)){
                highlyCompensatedIndivValue = false;
            }
             if("yes".equalsIgnoreCase(vendorNameProp)){
                vendorNameValue = false;
            } 
             if("yes".equalsIgnoreCase(vendorDUNSProp)){
                vendorDUNSValue = false;
            } 
             if("yes".equalsIgnoreCase(zipCodeProp)){
                zipCodeValue = false;
            } 
             if("yes".equalsIgnoreCase(paymentAmtProp)){
                paymentAmtValue = false;
            } 
             if("yes".equalsIgnoreCase(serviceDescProp)){
                serviceDescValue = false;
            } 
            if("yes".equalsIgnoreCase(primPlaceOfPerfProperty)){
                primPlaceOfPerfValue = false;
            } 
            if("yes".equalsIgnoreCase(primPlaceOfPerfCongDistProperty)){
                primPlaceOfPerfCongDistValue = false;
            } 
            if("yes".equalsIgnoreCase(personNameProp)){
                personNameValue = false;
            } 
            if("yes".equalsIgnoreCase(compensationAmtProp)){
                compensationAmtValue = false;
            } 
             if("yes".equalsIgnoreCase(subRecipientDunsProperty)){
                subRecipientDunsValue = false;
            }
             if("yes".equalsIgnoreCase(subAwdAmtProperty)){
                subAwdAmtValue = false;
            }
             if("yes".equalsIgnoreCase(subAwdAmtDispursedProperty)){
                subAwdAmtDispursedValue = false;
            }
             if("yes".equalsIgnoreCase(subAwdDateProperty)){
                subAwdDateValue = false;
            }
            if("yes".equalsIgnoreCase(subAwdNumberProperty)){
                subAwdNumberValue = false;
            }
       
        }
        
        String calImage = request.getContextPath()+"/coeusliteimages/cal.gif";   
        
        String plus = request.getContextPath()+"/coeusliteimages/plus.gif";
        String minus = request.getContextPath()+"/coeusliteimages/minus.gif";%>

	<html:form action="/saveSubcontractDetails" method="post">
		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			class="table">

			<tr class='copy' align="left">
				<td colspan="3"><font color="red"> <logic:messagesPresent>
							<script>errValue = true;</script>
							<html:errors header="" footer="" />
						</logic:messagesPresent> <logic:messagesPresent message="true">
							<html:messages id="message" message="true" property="errMsg">
								<script>errValue = true;</script>
								<li><bean:write name="message" /></li>
							</html:messages>
						</logic:messagesPresent>
				</font></td>
			</tr>
			<% String subAwdAmt,strSubAwdAmt,strTempAmt=null,strTempAmtDis = null,subAwdAmtDispurs,strSubAwardAmtDispursed;%>
			<logic:notEmpty name="subcontractDynaBeansList" property="infoList"
				scope="session">
				<logic:iterate id="dynaFormInfo" name="subcontractDynaBeansList"
					property="infoList" type="org.apache.struts.action.DynaActionForm"
					indexId="index" scope="session">
					<tr class='theader'>
						<td>&nbsp; <bean:message bundle="arra"
								key="awardSubcontract.subcontract" /> for <bean:write
								name="dynaFormInfo" property="subcontractCode" /></td>
						<td class='copybold' align="right">
							<%if(editAllRights.booleanValue() && !modeValue){ %> <span
							id="editAll" style="display: block"><a
								href="javaScript:enableFields('<%=vendorsSize%>','<%=highlyCompensatedsSize%>');disableEditAll();"><u>Edit
										All</u></a></span> <%}else{%> <span id="editAll" style="display: none"><a
								href="#"><u>Edit All</u></a></span> <%} %>
						</td>
						<td class='copybold' align="right">&nbsp;&nbsp; <html:link
								href="javascript:goBacktoSubcontract();" style="align: right;">
								<u><bean:message bundle="arra" key="awardSubcontract.return" /></u>
							</html:link>
						</td>
					</tr>
					<!--Display fields-->
					<tr valign="top">
						<td align="left" valign="top" colspan='3' bgcolor="#D1E5FF">
							<table width="100%" border="0" align="left" cellpadding="3"
								cellspacing="0">
								<tr>
									<td width="15%" nowrap class='copybold'><bean:message
											bundle="arra" key="awardSubcontract.subcontractCode" />:&nbsp;</td>
									<td width="30%" class='copy' nowrap><bean:write
											property="subcontractCode" name="dynaFormInfo" /></td>
									<td width="10%" nowrap class='copybold' align=left><bean:message
											bundle="arra" key="awardSubcontract.subcontractorName" />:&nbsp;</td>
									<td width="40%" class='copy' nowrap><bean:write
											property="subcontractorName" name="dynaFormInfo" /></td>
								</tr>
								<tr>
									<td width="15%" nowrap class='copybold'><bean:message
											bundle="arra" key="awardSubcontract.recipientDUNS" />:&nbsp;</td>
									<td width="30%" class='copy' nowrap>
										<!--<bean:write property = "subRecipientDUNS" name="dynaFormInfo"/>-->
										<%if(modeValue || subRecipientDunsValue){%> <html:text
											property="subRecipientDUNS" name="dynaFormInfo"
											indexed="true" styleId="subRecipientDUNSId"
											readonly="<%=subRecipientDunsValue%>"
											styleClass="cltextbox-nonEditcolor" style="width:70px;"
											maxlength="9" onchange="dataChanged()" /> <%}else{%> <html:text
											property="subRecipientDUNS" name="dynaFormInfo"
											indexed="true" styleId="subRecipientDUNSId"
											readonly="<%=modeValue%>" styleClass="cltextbox-medium"
											style="width:70px;" maxlength="9" onchange="dataChanged()" />
										<%}%>
									</td>
									<td width="10%" nowrap class="copybold"><bean:message
											bundle="arra" key="awardDetails.congrDistrict" /></td>
									<td width="40%" class="copy">
										<%if(modeValue || subcontractCongDistValue){%> <html:text
											property="subRecipientCongDist" name="dynaFormInfo"
											indexed="true" styleId="subcontractCongDistId"
											readonly="<%=subcontractCongDistValue%>"
											styleClass="cltextbox-nonEditcolor" style="width:40"
											maxlength="2" onchange="dataChanged()" /> <%}else{%> <html:text
											property="subRecipientCongDist" name="dynaFormInfo"
											indexed="true" styleId="subcontractCongDistId"
											readonly="<%=modeValue%>" styleClass="cltextbox-medium"
											style="width:40" maxlength="2" onchange="dataChanged()" /> <%}%>
									</td>
								</tr>
								<tr>
									<td width="10%" nowrap class='copybold' align=left><bean:message
											bundle="arra" key="awardSubcontract.subawardDate" />:&nbsp;</td>

									<td width="30%" class='copy' nowrap valign="center">
										<table border="0" cellspacing="0" cellpadding="0">
											<tr>
												<td>
													<%-- <coeusUtils:formatDate name="dynaFormInfo" formatString="MM/dd/yyyy" property="subAwardDate"/>--%>
													<% 
                                                             String strsubAwdDate;
                                                             edu.mit.coeuslite.utils.DateUtils dateUtils = new edu.mit.coeuslite.utils.DateUtils();
                                                             strsubAwdDate = (String)dynaFormInfo.get("subAwardDate");
                                                             strsubAwdDate = (strsubAwdDate==null)?"":strsubAwdDate.trim();
                                                             if(!"".equals(strsubAwdDate)) {
                                                             
                                                             try{ 
                                                             strsubAwdDate = dateUtils.formatDate(strsubAwdDate,"MM/dd/yyyy");
                                                             } catch (Exception e){
                                                             
                                                             }    
                                                             } 
                                                             %> <html:text
														property="subAwardDate" name="dynaFormInfo" indexed="true"
														readonly="true" styleClass="cltextbox-nonEditcolor"
														style="width:70px;" onchange="dataChanged()"
														value="<%=strsubAwdDate%>" /> &nbsp; <% String strSubAwardDate = "dynaFormInfo["+index+"].subAwardDate";
                                                             String strSubAwardDateScriptSrc ="javascript:displayCalendarWithTopLeft('"+strSubAwardDate+"',0,0)";                    
                                                             %>
												</td>
												<td>
													<%if(!subAwdDateValue){%> <html:link
														href="<%=strSubAwardDateScriptSrc%>" styleId="calId"
														style="display:block" onclick="dataChanged()">
														<html:img src="<%=calImage%>" border="0" height="16"
															width="16" />
													</html:link> <%}else{%> <html:link href="<%=strSubAwardDateScriptSrc%>"
														styleId="calId" style="display:none"
														onclick="dataChanged()">
														<html:img src="<%=calImage%>" border="0" height="16"
															width="16" />
													</html:link> <%}%>

												</td>
											</tr>
										</table>
									</td>
									<!-- new -->
									<td width="10%" nowrap class="copybold"><bean:message
											bundle="arra" key="awardSubcontract.subAwardNumber" />:&nbsp;</td>
									<td width="40%" class="copy">
										<%if(modeValue || subAwdNumberValue){%> <html:text
											property="subAwardNumber" name="dynaFormInfo" indexed="true"
											styleId="subAwardNumberId" readonly="<%=subAwdNumberValue%>"
											styleClass="cltextbox-nonEditcolor"
											style="width:100;text-align: right" maxlength="55"
											onchange="dataChanged()" /> <%}else{%> <html:text
											property="subAwardNumber" name="dynaFormInfo" indexed="true"
											styleId="subAwardNumberId" readonly="<%=modeValue%>"
											styleClass="cltextbox-medium"
											style="width:100;text-align: right" maxlength="55"
											onchange="dataChanged()" /> <%}%>
									</td>
								</tr>
								<tr>
									<td width="15%" nowrap class='copybold'><bean:message
											bundle="arra" key="awardSubcontract.subawardAmount" />:&nbsp;</td>
									<td width="30%" class='copy' nowrap>
										<%--<logic:notEmpty name="dynaFormInfo" property="subAwardAmount"><coeusUtils:formatString name="dynaFormInfo" property="subAwardAmount" formatType="currencyFormat"/></logic:notEmpty>--%>
										<%         
                                            if(dynaFormInfo.get("subAwardAmount")!=null){
                                            subAwdAmt = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(((Double)dynaFormInfo.get("subAwardAmount")).doubleValue());
                                            }else{
                                            subAwdAmt = "";
                                            }
                                            strSubAwdAmt = (String)dynaFormInfo.get("strSubAwardAmount");
                                            if(strSubAwdAmt!=null && !strSubAwdAmt.equals("")){
                                            strTempAmt  = strSubAwdAmt;
                                            strSubAwdAmt = strSubAwdAmt.replaceAll("[$,/,]","");
                                            try{
                                            strSubAwdAmt = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(new Double(strSubAwdAmt));
                                            }catch(java.lang.NumberFormatException ne){
                                            strSubAwdAmt = strTempAmt;
                                            }
                                            subAwdAmt = strSubAwdAmt;
                                            }
                                                
                                                if(dynaFormInfo.get("subAwardAmtDispursed")!=null){
                                                subAwdAmtDispurs = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(((Double)dynaFormInfo.get("subAwardAmtDispursed")).doubleValue());
                                                }else{
                                                subAwdAmtDispurs = "";
                                                }
                                                strSubAwardAmtDispursed = (String)dynaFormInfo.get("strSubAwardAmtDispursed");
                                                if(strSubAwardAmtDispursed!=null && !strSubAwardAmtDispursed.equals("")){
                                                strTempAmtDis  = strSubAwardAmtDispursed;
                                                strSubAwardAmtDispursed = strSubAwardAmtDispursed.replaceAll("[$,/,]","");
                                                try{
                                                strSubAwardAmtDispursed = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(new Double(strSubAwardAmtDispursed));
                                                }catch(java.lang.NumberFormatException ne){
                                                strSubAwardAmtDispursed = strTempAmtDis;
                                                }
                                                subAwdAmtDispurs = strSubAwardAmtDispursed;
                                                }
                                         %> <%if(modeValue || subAwdAmtValue){%>
										<html:text property="strSubAwardAmount"
											styleId="subAwardAmountId" name="dynaFormInfo" indexed="true"
											styleClass="cltextbox-nonEditcolor"
											readonly="<%=subAwdAmtValue%>"
											style="width:100;text-align: right" onchange="dataChanged()"
											value="<%=subAwdAmt%>" maxlength="13" /> <%}else{%> <html:text
											property="strSubAwardAmount" styleId="subAwardAmountId"
											name="dynaFormInfo" indexed="true"
											styleClass="cltextbox-medium" readonly="<%=modeValue%>"
											style="width:100;text-align: right" onchange="dataChanged()"
											value="<%=subAwdAmt%>" maxlength="13" /> <%}%>
									</td>
									<td width="10%" nowrap class='copybold' align=left><bean:message
											bundle="arra" key="awardSubcontract.subawardAmtDispursed" />:&nbsp;</td>
									<td width="40%" class='copy' nowrap>
										<%--<logic:notEmpty name="dynaFormInfo" property="subAwardAmtDispursed"><coeusUtils:formatString name="dynaFormInfo" property="subAwardAmtDispursed" formatType="currencyFormat"/></logic:notEmpty>--%>
										<%if(modeValue || subAwdAmtDispursedValue){%> <html:text
											property="strSubAwardAmtDispursed"
											styleId="subAwardAmtDispursedId" name="dynaFormInfo"
											indexed="true" styleClass="cltextbox-nonEditcolor"
											readonly="<%=subAwdAmtDispursedValue%>"
											style="width:100;text-align: right" onchange="dataChanged()"
											value="<%=subAwdAmtDispurs%>" maxlength="13" /> <%}else{%> <html:text
											property="strSubAwardAmtDispursed"
											styleId="subAwardAmtDispursedId" name="dynaFormInfo"
											indexed="true" styleClass="cltextbox-medium"
											readonly="<%=modeValue%>" style="width:100;text-align: right"
											onchange="dataChanged()" value="<%=subAwdAmtDispurs%>"
											maxlength="13" /> <%}%>

									</td>
								</tr>
							</table>
						</td>
					</tr>
					<!--Editable fields-->
					<tr valign="top">
						<td align="left" valign="top" colspan='3' bgcolor="#D1E5FF">
							<table width="78%" border="0" align="left" cellpadding="3"
								cellspacing="0">
								<tr>
									<td nowrap class="copybold" align="left"><bean:message
											bundle="arra" key="awardDetails.NoOfJobs" /></td>
									<td class="copy" align="left">
										<%if(modeValue || noOfJobsValue){%> <html:text
											property="noOfJobs" styleId="noOfJobsId" name="dynaFormInfo"
											indexed="true" styleClass="cltextbox-nonEditcolor"
											readonly="<%=noOfJobsValue%>"
											style="width:100;text-align: right" onchange="dataChanged()"
											maxlength="13" /> <%}else{%> <html:text property="noOfJobs"
											styleId="noOfJobsId" name="dynaFormInfo" indexed="true"
											styleClass="cltextbox-medium" readonly="<%=modeValue%>"
											style="width:100;text-align: right" onchange="dataChanged()"
											maxlength="13" /> <%}%>
									</td>
								</tr>
								<tr>
									<td nowrap class="copybold" valign="top" align="left"><bean:message
											bundle="arra" key="awardDetails.descriptionOfJobsCreated" /></td>
									<td class="copy" align="left">
										<%if(modeValue || jobsCreatedDescValue){%> <html:textarea
											styleId="employmentImpactId" property="employmentImpact"
											name="dynaFormInfo" style="width: 540px;" indexed="true"
											readonly="<%=jobsCreatedDescValue%>"
											styleClass="cltextbox-nonEditcolor" rows="3" cols="80"
											onchange="dataChanged()" /> <%}else{%> <html:textarea
											styleId="employmentImpactId" property="employmentImpact"
											name="dynaFormInfo" indexed="true" readonly="<%=modeValue%>"
											styleClass="textbox-longer" rows="3" cols="80"
											onchange="dataChanged()" /> <%}%>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<html:hidden property="primPlaceOfPerfId" name="dynaFormInfo"
						indexed="true" />
				</logic:iterate>
			</logic:notEmpty>



			<tr>
				<td align="left" valign="top" colspan="3"><table width="100%"
						border="0" cellpadding="0" cellspacing="0" class="tableheader">
						<tr>
							<td class="tableheader" height='20'>
								<div id='showVendor'>
									<html:link href="javaScript:showHideVendor('1');">
										<html:img src="<%=plus%>" border="0" />
									</html:link>
									&nbsp;
									<bean:message bundle="arra"
										key="awardSubcontract.vendorDetails" />

								</div>
								<div id='hideVendor' style='display: none;'>
									<html:link href="javaScript:showHideVendor('2');">
										<html:img src="<%=minus%>" border="0" />
									</html:link>
									&nbsp;
									<bean:message bundle="arra"
										key="awardSubcontract.vendorDetails" />
								</div>
							</td>
						</tr>
					</table></td>
			</tr>

			<tr>
				<td align="left" valign="top" colspan="3" bgcolor="#D1E5FF">
					<div id='vendorDetails' style='display: none;'>
						<table width="100%" align="left" border="0" cellpadding="3"
							cellspacing="0">
							<tr>
								<td width="30%" align="left" class="theader"><bean:message
										bundle="arra" key="awardVendors.vendorName" /></td>
								<td width="30%" align="left" class="theader"><bean:message
										bundle="arra" key="awardVendors.vendorDUNS" /></td>
								<td width="10%" align="left" class="theader"><bean:message
										bundle="arra" key="awardVendors.zipCode" /></td>
								<td width="20%" align="left" class="theader"><bean:message
										bundle="arra" key="awardVendors.paymentAmount" /></td>
								<td width="10%" align="left" class="theader">&nbsp;</td>
							</tr>
							<%  String strBgColor = "#DCE5F1";
                                String paymentAmt,strPaymentAmt,strAmt=null;
                                int count = 0;
                                %>
							<logic:notEmpty name="subcontractDynaBeansList" property="list"
								scope="session">
								<logic:iterate id="dynaFormData" name="subcontractDynaBeansList"
									property="list" type="org.apache.struts.action.DynaActionForm"
									indexId="index" scope="session">
									<% 
                                        if (count%2 == 0){
                                        strBgColor = "#D6DCE5";
                                        }else{
                                        strBgColor="#DCE5F1";
                                        }
                                        String lineImage = request.getContextPath()+"/coeusliteimages/line4.gif";
                                        if(dynaFormData.get("paymentAmount")!=null){
                                        paymentAmt = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(((Double)dynaFormData.get("paymentAmount")).doubleValue());
                                        }else{
                                        paymentAmt = "";
                                        }
                                        strPaymentAmt = (String)dynaFormData.get("strPaymentAmount");
                                        if(strPaymentAmt!=null && !strPaymentAmt.equals("")){
                                        strAmt  = strPaymentAmt;
                                        strPaymentAmt = strPaymentAmt.replaceAll("[$,/,]","");
                                        try{
                                        strPaymentAmt = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(new Double(strPaymentAmt));
                                        }catch(java.lang.NumberFormatException ne){
                                        strPaymentAmt = strAmt;
                                        }
                                        paymentAmt = strPaymentAmt;
                                        }
                                        if(count != 0){ %>
									<tr>
										<td width="30%" align="left" class="theader"><bean:message
												bundle="arra" key="awardVendors.vendorName" /></td>
										<td width="30%" align="left" class="theader"><bean:message
												bundle="arra" key="awardVendors.vendorDUNS" /></td>
										<td width="10%" align="left" class="theader"><bean:message
												bundle="arra" key="awardVendors.zipCode" /></td>
										<td width="20%" align="left" class="theader"><bean:message
												bundle="arra" key="awardVendors.paymentAmount" /></td>
										<td width="10%" align="left" class="theader">&nbsp;</td>
									</tr>
									<tr>
										<%--<td class='copy' colspan="5"><html:img src="<%=lineImage%>" width="100%" height="1" border="0"/></td> --%>
									</tr>
									<tr>
										<!-- JM 7-2-2012 fixed typo on height attribute -->
										<%-- <td class='copy' height='1' colspan="4">&nbsp;</td> --%>
									</tr>
									<%}%>

									<tr>
										<% String vendorNameIds = "vendorNameId"+count;
                                            String vendorDUNSIds = "vendorDUNSId"+count;
                                            String zipCodeIds = "zipCodeId"+count;
                                            String paymentAmtIds = "paymentAmtId"+count;%>

										<%if(modeValue ||  vendorNameValue){%>
										<td width="30%" align="left" class="copy"><html:text
												property="vendorName" name="dynaFormData"
												styleId='<%=vendorNameIds%>' style="width:220px;"
												readonly="<%=vendorNameValue%>" indexed="true"
												styleClass="cltextbox-nonEditcolor" onchange="dataChanged()"
												maxlength="55" /></td>
										<%}else{%>
										<td width="30%" align="left" class="copy"><html:text
												property="vendorName" name="dynaFormData"
												styleId='<%=vendorNameIds%>' style="width:220px;"
												readonly="<%=modeValue%>" indexed="true"
												styleClass="cltextbox-medium" onchange="dataChanged()"
												maxlength="55" /></td>
										<%}%>
										<%if(modeValue ||  vendorDUNSValue){%>
										<td width="30%" align="left" class="copy"><html:text
												property="vendorDUNS" name="dynaFormData"
												styleId='<%=vendorDUNSIds%>' style="width:220px;"
												readonly="<%=vendorDUNSValue%>" indexed="true"
												styleClass="cltextbox-nonEditcolor" onchange="dataChanged()"
												maxlength="9" /></td>
										<%}else{%>
										<td width="30%" align="left" class="copy"><html:text
												property="vendorDUNS" name="dynaFormData"
												styleId='<%=vendorDUNSIds%>' style="width:220px;"
												readonly="<%=modeValue%>" indexed="true"
												styleClass="cltextbox-medium" onchange="dataChanged()"
												maxlength="9" /></td>
										<%}%>
										<%if(modeValue || zipCodeValue){%>
										<td width="10%" align="left" class="copy"><html:text
												property="vendorHQZipCode" name="dynaFormData"
												styleId='<%=zipCodeIds%>' style="width:100px;"
												readonly="<%=zipCodeValue%>" indexed="true"
												styleClass="cltextbox-nonEditcolor" onchange="dataChanged()"
												maxlength="9" /></td>
										<%}else{%>
										<td width="10%" align="left" class="copy"><html:text
												property="vendorHQZipCode" name="dynaFormData"
												styleId='<%=zipCodeIds%>' style="width:100px;"
												readonly="<%=modeValue%>" indexed="true"
												styleClass="textbox" onchange="dataChanged()" maxlength="9" /></td>
										<%}%>
										<%if(modeValue ||  paymentAmtValue){%>
										<td width="20%" align="left" class="copy"><html:text
												property="strPaymentAmount" name="dynaFormData"
												styleId='<%=paymentAmtIds%>' readonly="<%=paymentAmtValue%>"
												indexed="true" styleClass="cltextbox-nonEditcolor"
												onchange="dataChanged()"
												style="text-align: right;width:100px;"
												value="<%=paymentAmt%>" maxlength="13" /></td>
										<%}else{%>
										<td width="20%" align="left" class="copy"><html:text
												property="strPaymentAmount" name="dynaFormData"
												styleId='<%=paymentAmtIds%>' readonly="<%=modeValue%>"
												indexed="true" styleClass="textbox" onchange="dataChanged()"
												style="text-align: right;width:100px;"
												value="<%=paymentAmt%>" maxlength="13" /></td>
										<%}%>
										<%if(!modeValue){ 
                                            String removeVendors ="javascript:removeVendors('"+index+"')";%>
										<td width="10%" align="left" class="copy" valign="center">
											<%-- <%if(!vendorDetailsValue){%>                                                
                                                <html:link href="<%=removeVendors%>"  styleId="removeVendorsId" style="display:block"><bean:message bundle="arra" key="awardSubcontract.remove"/></html:link>
                                                <%}else{%> --%> <html:link
												href="<%=removeVendors%>" styleId="removeVendorsId"
												style="display:block">
												<bean:message bundle="arra" key="awardSubcontract.remove" />
											</html:link> <%-- <%}%>  --%>
										</td>
										<%}%>
									</tr>
									<tr>
										<td colspan="4">
											<table width="100%" border="0" cellpadding="0"
												cellspacing="0">
												<tr>
													<td align="left" nowrap class="copybold" valign="top"><bean:message
															bundle="arra" key="awardVendors.serviceDesc" />:</td>
													<td align="left" class="copy" valign="top">
														<% String serviceDescIds = "serviceDescId"+count;%> <%if(modeValue || serviceDescValue){ %>
														<html:textarea property="serviceDescription"
															styleId='<%=serviceDescIds%>' name="dynaFormData"
															indexed="true" readonly="<%=serviceDescValue%>"
															style="width:540px;" styleClass="cltextbox-nonEditcolor"
															rows="3" onchange="dataChanged()" /> <%}else{%> <html:textarea
															property="serviceDescription"
															styleId='<%=serviceDescIds%>' name="dynaFormData"
															indexed="true" readonly="<%=modeValue%>"
															styleClass="textbox-longer" rows="3"
															onchange="dataChanged()" /> <%}%>
													</td>
												</tr>
											</table>
										</td>

									</tr>

									<tr>
										<!-- JM 7-2-2012 fixed typo on height attribute -->
										<td class='copy' height='1' colspan="5">&nbsp;</td>
									</tr>
									<% count++;%>
								</logic:iterate>
							</logic:notEmpty>
							<%if(!modeValue){ %>
							<tr>
								<td align="left" class="copy">&nbsp; <%-- <%if(!vendorDetailsValue){%>    
                                        <html:link href="javascript:addVendor()"  styleId="addVendorsId" style="display:block">
                                            <u><bean:message bundle="arra" key="awardSubcontract.addVendor"/></u>
                                        </html:link>
                                        <%}else{%>   --%> <html:link
										href="javascript:addVendor()" styleId="addVendorsId"
										style="display:block">
										<u><bean:message bundle="arra"
												key="awardSubcontract.addVendor" /></u>
									</html:link> <%--<%}%>  --%>
								</td>
							</tr>
							<%}%>
						</table>
					</div>
				</td>
			</tr>

			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td align="left" valign="top" colspan="3"><table width="100%"
						border="0" cellpadding="0" cellspacing="0" class="tableheader">
						<tr>
							<td class="tableheader" height='20'>
								<div id='showInd'>
									<html:link href="javaScript:showHideIndividuals('1');">
										<html:img src="<%=plus%>" border="0" />
									</html:link>
									&nbsp;
									<bean:message bundle="arra"
										key="awardSubcontract.highlyCompensated" />

								</div>
								<div id='hideInd' style='display: none;'>
									<html:link href="javaScript:showHideIndividuals('2');">
										<html:img src="<%=minus%>" border="0" />
									</html:link>
									&nbsp;
									<bean:message bundle="arra"
										key="awardSubcontract.highlyCompensated" />
								</div>
							</td>
						</tr>
					</table></td>
			</tr>

			<tr align="center">
				<td colspan="3" bgcolor="#D1E5FF">
					<div id='individualDetails' style='display: none;'>
						<table width="100%" align="left" border="0" cellpadding="0"
							cellspacing="0">

							<tr valign=top>
								<td>
									<table width="100%" align="left" border="0" cellpadding="3"
										cellspacing='0'>
										<tr>
											<td width="30%" align="left" class="theader">&nbsp;&nbsp;<bean:message
													bundle="arra" key="awardDetails.personName" /></td>
											<td width="20%" align="left" class="theader">&nbsp;&nbsp;<bean:message
													bundle="arra" key="awardDetails.compensationAmt" /></td>
											<td width="50%" align="left" class="theader">&nbsp;&nbsp;</td>
										</tr>

										<%  count = 0;
                                            String compAmt,strCompAmt;
                                            %>
										<logic:notEmpty name="subcontractDynaBeansList"
											property="beanList" scope="session">
											<logic:iterate id="dynaFormBean"
												name="subcontractDynaBeansList" property="beanList"
												type="org.apache.struts.action.DynaActionForm"
												indexId="index" scope="session">

												<% 
                                                    if (count%2 == 0){
                                                    strBgColor = "#D6DCE5";
                                                    }else{
                                                    strBgColor="#DCE5F1";
                                                    }
                                                    
                                                    if(dynaFormBean.get("compensation")!=null){
                                                    compAmt = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(((Double)dynaFormBean.get("compensation")).doubleValue());
                                                    }else{
                                                    compAmt = "";
                                                    }
                                                    strCompAmt = (String)dynaFormBean.get("strCompensation");
                                                    if(strCompAmt!=null && !strCompAmt.equals("")){
                                                    strAmt  = strCompAmt;
                                                    strCompAmt = strCompAmt.replaceAll("[$,/,]","");
                                                    try{
                                                    strCompAmt = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(new Double(strCompAmt));
                                                    }catch(java.lang.NumberFormatException ne){
                                                    strCompAmt = strAmt;
                                                    }
                                                    compAmt = strCompAmt;
                                                    }
                                                    %>
												<tr>
													<td width="30%" align="left" class="copy">
														<% String personNameIds = "personNameId"+count;%> <%if(modeValue || personNameValue ){ %>
														<html:text property="personName" name="dynaFormBean"
															style="width:220px;" styleId='<%=personNameIds%>'
															indexed="true" readonly="<%=personNameValue%>"
															styleClass="cltextbox-nonEditcolor"
															onchange="dataChanged()" maxlength="55" /> <%}else{%> <html:text
															property="personName" name="dynaFormBean"
															styleId='<%=personNameIds%>' indexed="true"
															readonly="<%=modeValue%>" styleClass="cltextbox-medium"
															onchange="dataChanged()" maxlength="55" /> <%}%>
													</td>
													<td width=20% " align="left" class="copy">
														<% String compensationAmtIds = "compensationAmtId"+count;%>
														<%if(modeValue || compensationAmtValue){ %> <html:text
															property="strCompensation" name="dynaFormBean"
															styleId='<%=compensationAmtIds%>' indexed="true"
															readonly="<%=compensationAmtValue%>"
															styleClass="cltextbox-nonEditcolor"
															onchange="dataChanged()"
															style="text-align: right width:220px;"
															value="<%=compAmt%>" maxlength="13" /> <%}else{%> <html:text
															property="strCompensation" name="dynaFormBean"
															styleId='<%=compensationAmtIds%>' indexed="true"
															readonly="<%=modeValue%>" styleClass="cltextbox-medium"
															onchange="dataChanged()" style="text-align: right"
															value="<%=compAmt%>" maxlength="13" /> <%}%>
													</td>
													<%if(!modeValue){ 
                                                        String removeIndividual ="javascript:removeIndividual('"+index+"')";%>
													<td width=50% " align="left" class="copy">&nbsp;&nbsp;
														<%--<%if(!highlyCompensatedIndivValue){%>  
                                                            <html:link href="<%=removeIndividual%>" styleId="removeHighlyCompenId" ><bean:message bundle="arra" key="awardSubcontract.remove"/></html:link>
                                                            <%}else{%>--%>
														<html:link href="<%=removeIndividual%>"
															styleId="removeHighlyCompenId">
															<bean:message bundle="arra" key="awardSubcontract.remove" />
														</html:link> <%--<%}%>--%>
													</td>
													<%}%>
												</tr>
												<% count++;%>
											</logic:iterate>
										</logic:notEmpty>
										<%if(!modeValue){ %>
										<tr>
											<td align="left" class="copy">&nbsp; <%-- <%if(!highlyCompensatedIndivValue){%>                                                
                                                    <html:link href="javascript:addIndividual()" styleId="addHighlyCompenId" style="display:block">
                                                        <u><bean:message bundle="arra" key="awardSubcontract.addIndividual"/></u>
                                                    </html:link>
                                                    <%}else{%>--%> <html:link
													href="javascript:addIndividual()"
													styleId="addHighlyCompenId" style="display:block">
													<u><bean:message bundle="arra"
															key="awardSubcontract.addIndividual" /></u>
												</html:link> <%--<%}%>--%>

											</td>
										</tr>
										<%}%>
									</table>
								</td>
							</tr>
						</table>
					</div>
				</td>
			</tr>



			<tr>
				<td>&nbsp;</td>
			</tr>
			<logic:notEmpty name="subcontractDynaBeansList" property="infoList"
				scope="session">
				<logic:iterate id="dynaFormInfo" name="subcontractDynaBeansList"
					property="infoList" type="org.apache.struts.action.DynaActionForm"
					indexId="index" scope="session">

					<tr>
						<td align="left" valign="top" colspan="3"><table width="100%"
								border="0" cellpadding="0" cellspacing="0" class="tableheader">
								<tr>
									<td class="tableheader" height='20'>
										<div id='showPrimPlace'>
											<html:link href="javaScript:showHidePlaceOfPerformace('1');">
												<html:img src="<%=plus%>" border="0" />
											</html:link>
											&nbsp;
											<bean:message bundle="arra"
												key="awardDetails.primPlaceofPerf" />

										</div>
										<div id='hidePrimPlace' style='display: none;'>
											<html:link href="javaScript:showHidePlaceOfPerformace('2');">
												<html:img src="<%=minus%>" border="0" />
											</html:link>
											&nbsp;
											<bean:message bundle="arra"
												key="awardDetails.primPlaceofPerf" />
										</div>
									</td>
								</tr>
							</table></td>
					</tr>



					<tr>
						<td height="100%" width="40%" valign="top" bgcolor="#D1E5FF">
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
												name="dynaFormInfo" styleId="primPlaceOfPerfAddressId"
												indexed="true" property="primPlaceOfPerfAddress"
												readonly="true" rows="4"
												readonly="<%=primPlaceOfPerfValue%>" cols="20"
												styleClass="cltextbox-nonEditcolor" style="width:220px;" />
											<%}else{%> <html:textarea name="dynaFormInfo"
												styleId="primPlaceOfPerfAddressId" indexed="true"
												property="primPlaceOfPerfAddress" readonly="true" rows="4"
												readonly="<%=modeValue%>" cols="20"
												styleClass="cltextbox-color" /> <%}%>
										</td>
										<td width="10%" align="left" valign="top">
											<%String primaryPlaceSearch = "javaScript:searchWindow('primaryPlaceSearch')";
                                                if(modeValue){%> <html:link
												href="<%=primaryPlaceSearch%>"
												styleId="primaryPlaceSearchId" style="display:none">
												<bean:message bundle="proposal"
													key="proposalOrganization.Search" />
											</html:link> <%}else{%> <%if(!primPlaceOfPerfValue){%> <html:link
												href="<%=primaryPlaceSearch%>"
												styleId="primaryPlaceSearchId" style="display:block">
												<bean:message bundle="proposal"
													key="proposalOrganization.Search" />
											</html:link> <%}else{%> <html:link href="<%=primaryPlaceSearch%>"
												styleId="primaryPlaceSearchId" style="display:none">
												<bean:message bundle="proposal"
													key="proposalOrganization.Search" />
											</html:link> <%}%> <%}%>
										</td>
									</tr>
								</table>
							</div>
						</td>
						<td align="left" valign="top" width="40%" height="100%"
							bgcolor="#D1E5FF">
							<div id='primPlace2' style='display: none;'>
								<!--Right Section Start -->
								<table width="40%" align="left" border="0" cellpadding="3"
									cellspacing="0">
									<tr>
										<td nowrap class="copybold" align="left">&nbsp;<bean:message
												bundle="arra" key="awardDetails.congrDistrict" />&nbsp;&nbsp;
										</td>
										<td class="copy" align="left">
											<%if(modeValue || primPlaceOfPerfCongDistValue){ %> <html:text
												onblur="" styleId="primPlaceCongDistId" name="dynaFormInfo"
												indexed="true" property="primPlaceCongDist"
												styleClass="cltextbox-nonEditcolor" maxlength="2"
												readonly="<%=primPlaceOfPerfCongDistValue%>"
												style="width:40px;" onchange="dataChanged()" /> <%}else{%> <html:text
												onblur="" styleId="primPlaceCongDistId" name="dynaFormInfo"
												indexed="true" property="primPlaceCongDist"
												styleClass="textbox" maxlength="2" readonly="<%=modeValue%>"
												style="width:40px;" onchange="dataChanged()" /> <%}%>
										</td>
										<td></td>
										<td></td>
									</tr>
								</table>
							</div>
						</td>
						<td height="100%" width="20%" valign="top" bgcolor="#D1E5FF">
						</td>
					</tr>


				</logic:iterate>
			</logic:notEmpty>


			<tr>
				<td>&nbsp;</td>
			</tr>

			<tr>
				<td colspan='4' class='savebutton'><html:submit property="Save"
						value="Save" disabled="<%=modeValue%>" styleClass="clsavebutton" />
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>

			<logic:messagesPresent
				message="subcontractVendor.error.serviceDescLength">
				<script>
                    showHideVendor(1);
                </script>
			</logic:messagesPresent>
			<logic:messagesPresent message="subcontractHighlyComp.error.validAmt">
				<script>showHideIndividuals(1);</script>
			</logic:messagesPresent>
			<logic:messagesPresent message="subcontract.error.validPaymentAmount">
				<script>showHideVendor(1);</script>
			</logic:messagesPresent>
			</html:form>
			<script>
      DATA_CHANGED = 'false';
      if(errValue) {
            DATA_CHANGED = 'true';
      }
      LINK = "<%=request.getContextPath()%>/saveSubcontractDetails.do";
      FORM_LINK = document.subcontractDynaBeansList;
      PAGE_NAME = "<bean:message key="awardSubcontract.subcontract" bundle="arra"/>";
      function dataChanged(){
        DATA_CHANGED = 'true';
      }
      linkForward(errValue);
      <%
      String vendorUpdated = (String)request.getAttribute("vendorUpdated");
      String indUpdated = (String)request.getAttribute("indUpdated");
      if("Y".equals(vendorUpdated)){%>
        dataChanged();
        showHideVendor(1);
      <%}
      if("Y".equals(indUpdated)){%>
        dataChanged();
        showHideIndividuals(1);
      <%}%>
        </script>
</body>
</html:html>
