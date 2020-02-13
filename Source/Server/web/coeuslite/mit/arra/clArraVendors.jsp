<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page
	import="org.apache.struts.taglib.html.JavascriptValidatorTag,
org.apache.struts.validator.DynaValidatorForm,edu.mit.coeuslite.arra.bean.ArraAwardHeaderBean,edu.mit.coeuslite.utils.CoeusLiteConstants"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="arraVendorColumnProperties" scope="session"
	class="java.util.HashMap" />


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
	src="..\scripts\_js.js"></script>
<script language="javascript" type="text/JavaScript"
	src="..\scripts\CheckForward.js"></script>
<script>
        var errValue = false;
        function enableFields(vendorSize){    
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
        }
        function removeVendors(unitIndex) {
             if(confirm("<bean:message bundle="arra" key="awardVendors.deleteConfirm"/>")){
                document.arraVendorDynaBeansList.action = "<%=request.getContextPath()%>/removeArraVendorDetails.do?vendorIndex="+unitIndex;
                document.arraVendorDynaBeansList.submit();      
             }
        }
        function disableEditAll(){    
        document.getElementById("editAll").style.display="none";  
        }
        function addVendor(){
                document.arraVendorDynaBeansList.action = "<%=request.getContextPath()%>/addVendors.do";
                document.arraVendorDynaBeansList.submit();    
        } 
        function called(value) {
            if(value=='1'){
            document.getElementById('open_window').style.display = 'block';
            document.getElementById('hide_Add').style.display = 'block';
            document.getElementById('open_Add').style.display = 'none';         
            }else if(value=='2'){
            document.getElementById('open_window').style.display = 'none';
            document.getElementById('hide_Add').style.display = 'none';
            document.getElementById('open_Add').style.display = 'block';
            }
            }      
        </script>
</head>

<body>
	<% 
        ArraAwardHeaderBean headerBean = (ArraAwardHeaderBean)session.getAttribute("arraAwardHeaderBean");
        Boolean editAllRights = (Boolean)session.getAttribute("canEditAllRight");
        boolean modeValue = false;
        
        String vendorNameProp = "";
        String vendorDUNSProp = "";
        String zipCodeProp = "";
        String paymentAmtProp = "";
        String serviceDescProp = "";
        if(arraVendorColumnProperties != null && arraVendorColumnProperties.size()>0){
              vendorNameProp = (String)arraVendorColumnProperties.get("VENDOR_NAME"); 
              vendorDUNSProp = (String)arraVendorColumnProperties.get("VENDOR_DUNS");   
              zipCodeProp = (String)arraVendorColumnProperties.get("VENDOR_HQ_ZIP_CODE");                
              paymentAmtProp = (String)arraVendorColumnProperties.get("PAYMENT_AMOUNT");            
              serviceDescProp = (String)arraVendorColumnProperties.get("SERVICE_DESCRIPTION");
        }
        boolean vendorNameValue = true;
        boolean vendorDUNSValue = true;
        boolean zipCodeValue = true;
        boolean paymentAmtValue = true;
        boolean serviceDescValue = true;
        String mode=(String)session.getAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId());
        if(headerBean!=null && "Y".equalsIgnoreCase(headerBean.getComplete()) || "S".equalsIgnoreCase(headerBean.getComplete()) || !editAllRights.booleanValue()) {
            modeValue=true;
        }else if(CoeusLiteConstants.DISPLAY_MODE.equalsIgnoreCase(mode)){
            modeValue=true;
        }else if(CoeusLiteConstants.MODIFY_MODE.equalsIgnoreCase(mode)){
            modeValue=false;
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
        }
        %>
	<html:form action="/saveVendorDetails.do">

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
			<tr>
				<td align="left" valign="top"><table width="100%" border="0"
						align="center" cellpadding="2" cellspacing="0" class="tabtable">
						<tr>
							<td align="left" valign="top"><table width="100%" border="0"
									cellpadding="0" cellspacing="0" class="tableheader">
									<tr>
										<td><bean:message bundle="arra"
												key="awardVendors.vendorList" /></td>

										<logic:notEmpty name="arraVendorDynaBeansList" property="list">
											<bean:size id="vendorsSize" name="arraVendorDynaBeansList"
												property="list" />

											<td nowrap class="tableheader" align="right" height='20'>
												<%if(editAllRights.booleanValue() && !modeValue){ %> <span
												id="editAll" style="display: block"> <a
													href="javaScript:enableFields('<%=vendorsSize%>');disableEditAll();"><u>Edit
															All</u></a>
											</span> <%}else{%> <span id="editAll" style="display: none"><a
													href="#"><u>Edit All</u></a></span> <%} %>
											</td>
										</logic:notEmpty>
									</tr>
								</table></td>
						</tr>
						<logic:empty name="arraVendorDynaBeansList" property="list"
							scope="session">
							<tr align="center">
								<td class="copybold" align="left" colspan="4"><bean:message
										bundle="arra" key="awardVendors.noVendors.displayText" /></td>
							</tr>
						</logic:empty>
						<logic:notEmpty name="arraVendorDynaBeansList" property="list"
							scope="session">
							<tr align="center">
								<td class="copybold" align="left"><bean:message
										bundle="arra" key="awardVendors.displayText" /></td>
							</tr>
						</logic:notEmpty>
						<tr>
							<td align="left" class="copy">&nbsp; <%if(editAllRights.booleanValue() && !modeValue){ %>
								<html:link href="javascript:addVendor()" styleId="addVendorsId"
									style="display:block">
									<u><bean:message bundle="arra"
											key="awardSubcontract.addVendor" /></u>
								</html:link> <%}%>
							</td>
						</tr>
						<tr align="center">
							<td>
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
									<logic:present name="arraVendorDynaBeansList" property="list"
										scope="session">

										<logic:iterate id="dynaFormData"
											name="arraVendorDynaBeansList" property="list"
											type="org.apache.struts.action.DynaActionForm"
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
														styleClass="cltextbox-nonEditcolor"
														onchange="dataChanged()" maxlength="55" /></td>
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
														styleClass="cltextbox-nonEditcolor"
														onchange="dataChanged()" maxlength="9" /></td>
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
														styleClass="cltextbox-nonEditcolor"
														onchange="dataChanged()" maxlength="9" /></td>
												<%}else{%>
												<td width="10%" align="left" class="copy"><html:text
														property="vendorHQZipCode" name="dynaFormData"
														styleId='<%=zipCodeIds%>' style="width:100px;"
														readonly="<%=modeValue%>" indexed="true"
														styleClass="textbox" onchange="dataChanged()"
														maxlength="9" /></td>
												<%}%>
												<%if(modeValue ||  paymentAmtValue){%>
												<td width="20%" align="left" class="copy"><html:text
														property="strPaymentAmount" name="dynaFormData"
														styleId='<%=paymentAmtIds%>'
														readonly="<%=paymentAmtValue%>" indexed="true"
														styleClass="cltextbox-nonEditcolor"
														onchange="dataChanged()"
														style="text-align: right;width:100px;"
														value="<%=paymentAmt%>" maxlength="13" /></td>
												<%}else{%>
												<td width="20%" align="left" class="copy"><html:text
														property="strPaymentAmount" name="dynaFormData"
														styleId='<%=paymentAmtIds%>' indexed="true"
														readonly="<%=modeValue%>" styleClass="textbox"
														onchange="dataChanged()"
														style="text-align: right;width:100px;"
														value="<%=paymentAmt%>" maxlength="13" /></td>
												<%}%>
												<%if(editAllRights.booleanValue() && !modeValue){ 
                                                        String removeVendors ="javascript:removeVendors('"+index+"')";%>
												<td width="10%" align="left" class="copy" valign="center">
													<html:link href="<%=removeVendors%>"
														styleId="removeVendorsId" style="display:block">
														<bean:message bundle="arra" key="awardSubcontract.remove" />
													</html:link>
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
																	style="width:540px;"
																	styleClass="cltextbox-nonEditcolor" rows="3"
																	onchange="dataChanged()" /> <%}else{%> <html:textarea
																	property="serviceDescription"
																	styleId='<%=serviceDescIds%>' name="dynaFormData"
																	readonly="<%=modeValue%>" indexed="true"
																	style="width:540px;" styleClass="textbox-longer"
																	indexed="true" rows="3" onchange="dataChanged()" /> <%}%>
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
									</logic:present>
									<tr>
										<td colspan='4' class='savebutton'>
											<%if(editAllRights.booleanValue()){ %> <html:submit
												property="Save" value="Save" disabled="<%=modeValue%>"
												styleClass="clsavebutton" /> <%}%>
										</td>
									</tr>
									<logic:notPresent name="arraVendorDynaBeansList">
										<tr>
											<td colspan="4"><bean:write name="dynaFormData"
													property="awardVendors.noVendors.displayText" /></td>

										</tr>
									</logic:notPresent>
									<logic:empty name="arraVendorDynaBeansList" property="list">
										<SCRIPT>
                                                        document.arraVendorDynaBeansList.Save.disabled=true;
                                                    </SCRIPT>
									</logic:empty>
								</table>
							</td>
						</tr>

					</table></td>
			</tr>
		</table>

	</html:form>
	<script>
      DATA_CHANGED = 'false';
      if(errValue) {
            DATA_CHANGED = 'true';
      }
      LINK = "<%=request.getContextPath()%>/saveVendorDetails.do";
      FORM_LINK = document.arraVendorDynaBeansList;
      PAGE_NAME = "<bean:message key="awardSubcontract.vendorDetails" bundle="arra"/>";
      function dataChanged(){
        document.arraVendorDynaBeansList.Save.disabled=false;
        DATA_CHANGED = 'true';
      }
      linkForward(errValue);
      <%
      String vendorUpdated = (String)request.getAttribute("vendorUpdated");
      
      if("Y".equals(vendorUpdated)){%>
        dataChanged();
      <%}%>
    </script>
</body>
</html:html>

