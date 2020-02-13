<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%--start 1--%>

<%@ page
	import="org.apache.struts.taglib.html.JavascriptValidatorTag,
                edu.mit.coeuslite.utils.CoeusLiteConstants,
                org.apache.struts.validator.DynaValidatorForm"%>
<%--end 1--%>

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@page
	import="java.util.Vector,
                java.util.ArrayList,
                edu.mit.coeus.bean.*,
                edu.mit.coeus.unit.bean.*,
                edu.mit.coeus.rolodexmaint.bean.*,
                edu.mit.coeus.utils.ComboBoxBean,
                edu.mit.coeus.iacuc.bean.ProtocolInvestigatorUnitsBean,
                edu.mit.coeuslite.iacuc.bean.IRBInvestigatorBean,
                edu.mit.coeus.bean.UserDetailsBean,
                edu.mit.coeus.rolodexmaint.bean.RolodexMaintenanceDataTxnBean,
                edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean,
                edu.mit.coeus.unit.bean.UnitDataTxnBean,
                edu.mit.coeus.unit.bean.UnitDetailFormBean,
                java.net.URLEncoder"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<jsp:useBean id="investigatorRoles" scope="request"
	class="java.util.Vector" />
<jsp:useBean id="affiliationTypes" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="protocolInvData" scope="request"
	class="java.util.Vector" />
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>


<%
  String strMenuStatus = "";
//  String mode = "";
  String strAdwardAdminDeptNum = "";
  String strAdwardAdminDept = "";
  // The commented code is not used anywhere.
  //ComboBoxBean invRole = null;
  //if(investigatorRoles != null && investigatorRoles.size()>0){
   //invRole = (ComboBoxBean)investigatorRoles.firstElement();
    //}
  boolean blnReadOnly = false;

//start--2

    String mode=(String)session.getAttribute(CoeusLiteConstants.MODE_DETAILS);
    boolean modeValue=false;
    if(mode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE)){
        modeValue=true;
    }else if(mode.equalsIgnoreCase(CoeusLiteConstants.MODIFY_MODE)){
        modeValue=false;
    }else if(mode.equalsIgnoreCase(CoeusLiteConstants.ADD_MODE)){
        modeValue=false;
    }
    // Added by chandra
    DynaValidatorForm formdata = (DynaValidatorForm)request.getAttribute("investigatorForm");
//end--2                
 %>

<%
//start--3      

    String protocolNo = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER);
    if(protocolNo!= null){ 
         protocolNo = "["+"Protocol No. - "+ protocolNo+"]";
    }else{
        protocolNo = "";
    }


//end--3
%>


<html:html>
<head>
<title>Coeus Web</title>
<html:base />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">

<script language="javascript" type="text/JavaScript"
	src="..\scripts\_js.js"></script>
<script language="javascript" type="text/JavaScript"
	src="..\scripts\CheckForward.js"></script>
<script language="JavaScript" type="text/JavaScript">
    var validFlag;
    var unitFlag = "";
      
      function insert_data(data){
            document.irbInvestigatorForm.acType.value = data;
             if(document.irbInvestigatorForm.is_Employee.checked){
                document.irbInvestigatorForm.is_Employee.value="Y";
             }else if(!document.irbInvestigatorForm.is_Employee.checked) {
                document.irbInvestigatorForm.is_Employee.value = "N";
             }
            document.irbInvestigatorForm.action = "<%=request.getContextPath()%>/investigator.do";
            
      }
      
      function showChoice() 
      {   
        var strAdwardAdminDeptNum = "";
        var strAdwardAdminDept = "";
        strAdwardAdminDeptNum = "<%=strAdwardAdminDeptNum%>";
        strAdwardAdminDept = "<%=strAdwardAdminDept%>";
        selection = confirm("Adward Admin department is "+strAdwardAdminDeptNum+" "+strAdwardAdminDept+"\nDo you wish to change the Lead Investigator's department to the Adward Admin department?" );
        if(selection == true) 
        {
            document.investigatorForm.invDeptNumber.value = strAdwardAdminDeptNum;
            document.investigatorForm.invDeptName.value = strAdwardAdminDept;
            document.investigatorForm.invDeptNumber.focus();
        }else {
            document.investigatorForm.invDeptNumber.focus();
        }
      }
      
      function showList(frm, fld_dept_no, fld_dept_name) 
      {
        var winleft = (screen.width - 450) / 2;
        var winUp = (screen.height - 350) / 2;  
        var url_value = "<%=request.getContextPath()%>/search.do?type=dept&form=" + frm + "&fld1=" + fld_dept_no + "&fld2=" + fld_dept_name;
        var win = "scrollbars=yes,width=450,height=350,left="+winleft+",top="+winUp;
        sList = window.open(url_value, "list", win);

        if (parseInt(navigator.appVersion) >= 4) {
          window.sList.focus(); 
        }

      }
      
    function open_search(valid)
          {
            validFlag = valid;
            var winleft = (screen.width - 650) / 2;
            var winUp = (screen.height - 450) / 2;  
            var win = "scrollbars=1,resizable=1,width=750,height=450,left="+winleft+",top="+winUp;
            if(valid){
                sList = window.open('<%=request.getContextPath()%>/iacucSearch.do?type=<bean:message key="searchWindow.title.person"/>&search=true&searchName=WEBPERSONSEARCH', "list", win);  
            }else {
                sList = window.open('<%=request.getContextPath()%>/iacucSearch.do?type=<bean:message key="searchWindow.title.rolodex"/>&search=true&searchName=WEBROLODEXSEARCH', "list", win);  
            }
          }
     
      function search()
        {
            if(document.irbInvestigatorForm.is_Employee.checked){
                open_search(true);
            }else{
                open_search(false);
             }
         }
         
      function unit_search(data)
      {
      unitFlag = data;
      
      var winleft = (screen.width - 450) / 2;
            var winUp = (screen.height - 350) / 2;  
            var win = "scrollbars=yes,width=650,height=360,left="+winleft+",top="+winUp
            sList = window.open('<%=request.getContextPath()%>/iacucSearch.do?type=<bean:message key="searchWindow.title.unit"/>&search=true&searchName=WEBLEADUNITSEARCH', "list", win);  
      }
      
      function person_search()
      {
        var winleft = (screen.width - 450) / 2;
        var winUp = (screen.height - 350) / 2;  
        var win = "scrollbars=yes,width=450,height=350,left="+winleft+",top="+winUp
        sList = window.open('<%=request.getContextPath()%>/web/dialogs/clPersonSearch.jsp?type=investigator&search=true', "list", win);
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
      
      function delete_data(data,personId,timestamp){
        if (confirm("Are you sure you want to delete the investigator?")==true){
          document.irbInvestigatorForm.acType.value = data;
          document.irbInvestigatorForm.personId.value = personId;
          document.irbInvestigatorForm.protoInvestigatortimeStamp.value = timestamp;
          document.irbInvestigatorForm.invFirstName.value = "a ";
          document.irbInvestigatorForm.invLastName.value = "b ";
          document.irbInvestigatorForm.unitNumber.value = "c ";
          document.irbInvestigatorForm.action = "<%=request.getContextPath()%>/investigator.do";
          document.irbInvestigatorForm.submit();
        }
     }
    
    function fetch_Data(result){
         var unitData = unitFlag;
         if(unitFlag == "unit") {
              document.irbInvestigatorForm.unitNumber.value=result["UNIT_NUMBER"];
              if(result["UNIT_NUMBER"]=="null"){document.irbInvestigatorForm.unitNumber.value = "";}else{document.irbInvestigatorForm.unitNumber.value = result["UNIT_NUMBER"]}; 
              if(result["UNIT_NAME"]=="null"){document.irbInvestigatorForm.unitName.value = "";}else{document.irbInvestigatorForm.unitName.value = result["UNIT_NAME"]}; 
              unitFlag="";
              //document.irbInvestigatorForm.unitNumber.value = result["UNIT_NUMBER"];
              //document.irbInvestigatorForm.unitName.value = result["UNIT_NAME"];
         }else if(validFlag){
              
             document.irbInvestigatorForm.personId.value = result["PERSON_ID"];
             if(result["LAST_NAME"]=="null" || result["LAST_NAME"]==""){
                if(result["ORGANIZATION"]==undefined || result["ORGANIZATION"]=='null' ){
                        document.irbInvestigatorForm.invLastName.value =result["FULL_NAME"] ;
                        document.irbInvestigatorForm.personName.value = result["FULL_NAME"];
                            }else{
                        document.irbInvestigatorForm.invLastName.value = result["ORGANIZATION"];
                        document.irbInvestigatorForm.personName.value = result["ORGANIZATION"];
                        }
              } else {
                    if(result["LAST_NAME"]=="null"){document.irbInvestigatorForm.invLastName.value = "";}else{document.irbInvestigatorForm.invLastName.value = result["LAST_NAME"]}; 
                    if(result["FIRST_NAME"]=="null"){document.irbInvestigatorForm.invFirstName.value = "";}else{document.irbInvestigatorForm.invFirstName.value = result["FIRST_NAME"]}; 
                    if(result["HOME_UNIT"]=="null"){document.irbInvestigatorForm.unitNumber.value = "";}else{document.irbInvestigatorForm.unitNumber.value = result["HOME_UNIT"]}; 
                    if(result["UNIT_NAME"]=="null"){document.irbInvestigatorForm.unitName.value = "";}else{document.irbInvestigatorForm.unitName.value = result["UNIT_NAME"]}; 
                    document.irbInvestigatorForm.personName.value = result["FULL_NAME"];
              }
              
             
             if(result["OFFICE_PHONE"]=="null"){document.irbInvestigatorForm.invPhone.value = "";}else{document.irbInvestigatorForm.invPhone.value = result["OFFICE_PHONE"]}; 
             if(result["EMAIL_ADDRESS"]=="null"){document.irbInvestigatorForm.invEmail.value = "";}else{document.irbInvestigatorForm.invEmail.value = result["EMAIL_ADDRESS"]}; 
         }else {
              document.irbInvestigatorForm.personId.value = result["ROLODEX_ID"];
              if(result["LAST_NAME"]=="null" || result["LAST_NAME"]==""){
                document.irbInvestigatorForm.invLastName.value = result["ORGANIZATION"];
                document.irbInvestigatorForm.personName.value = result["ORGANIZATION"];
              }
              else{
                 document.irbInvestigatorForm.personName.value = result["FIRST_NAME"] + "," + result["LAST_NAME"];
                 if(result["LAST_NAME"]=="null"){document.irbInvestigatorForm.invLastName.value = "";}else{document.irbInvestigatorForm.invLastName.value = result["LAST_NAME"]}; 
                 if(result["FIRST_NAME"]=="null"){document.irbInvestigatorForm.invFirstName.value = "";}else{document.irbInvestigatorForm.invFirstName.value = result["FIRST_NAME"]}; 
             }
             
             if(result["PHONE_NUMBER"]=="null"){document.irbInvestigatorForm.invPhone.value = "";}else{document.irbInvestigatorForm.invPhone.value = result["PHONE_NUMBER"]}; 
             if(result["EMAIL_ADDRESS"]=="null"){document.irbInvestigatorForm.invEmail.value = "";}else{document.irbInvestigatorForm.invEmail.value = result["EMAIL_ADDRESS"]};
             //document.irbInvestigatorForm.invLastName.value = result["LAST_NAME"];
             //document.irbInvestigatorForm.invFirstName.value = result["FIRST_NAME"];
             //document.irbInvestigatorForm.invPhone.value = result["PHONE_NUMBER"];
             //document.irbInvestigatorForm.invEmail.value = result["EMAIL_ADDRESS"];
             
             
         }
     }
     
     function validateForm(form) {
       insert_data("I");
      // alert('validating');
       return validateIrbInvestigatorForm(form);
    }


function refresh(){
    document.irbInvestigatorForm.invLastName.value="";
    document.irbInvestigatorForm.invFirstName.value="";
}
      
    </script>

<html:javascript formName="irbInvestigatorForm" dynamicJavascript="true"
	staticJavascript="true" />

<!--<link href="css/ipf.css" rel="stylesheet" type="text/css">
        <style type="text/css">
        </style>-->

</head>

<body>




	<html:form action="/investigator.do" method="post"
		onsubmit="return validateForm(this)">

		<!-- New Template for cwInvestigator - Start   -->


		<table width="100%" height="100%" border="0" cellpadding="0"
			cellspacing="0" class="table">

			<tr>
				<td height="20" align="left" valign="top" class="theader"><bean:message
						key="investigatorsLabel.Investigators" /></td>
			</tr>
			<tr>
				<td class="copy">&nbsp;&nbsp; <font color="red">*</font> <bean:message
						key="label.indicatesReqFields" /> <%--Indicates Required Fields--%>
				</td>
			</tr>
			<tr class='copybold' align="left">
				<td><font color="red"> <html:messages id="message"
							message="true">
							<bean:write name="message" />
						</html:messages>
				</font></td>
			</tr>

			<!-- Add Investigator Information: - Start  -->
			<% if(!blnReadOnly) {%>
			<tr>
				<td align="left" valign="top"><table width="99%" border="0"
						align="center" cellpadding="0" cellspacing="0" class="tabtable">
						<tr>
							<td colspan="4" align="left" valign="top"><table
									width="100%" height="20" border="0" cellpadding="0"
									cellspacing="0" class="tableheader">
									<tr>
										<td><bean:message key="investigatorsLabel.AddInvInfo" />:
										</td>
									</tr>
								</table></td>
						</tr>
						<tr>
							<td width="50%" align="right" valign="top" class="copy">
								<table width="100%" border="0" cellpadding="0">
									<tr>
										<%-- Left column fields --%>
										<td width="50%" align="right" valign="top" class="copy">
											<table width="100%" border="0" cellpadding="0">
												<tr>
													<td align="left" nowrap class="copybold"><font
														color="red">*</font> <bean:message
															key="investigatorsLabel.LastName" />:</td>
													<td align="left" class="copysmall">
														<%String objValue = formdata==null ? "" : (String)formdata.get("invLastName");%>
														<html:text property="invLastName" styleClass="textbox"
															onchange="setChangeFlag();" disabled="<%=modeValue%>"
															readonly="true" value="<%= objValue%>" /> <%if(!modeValue){%>

														<%--a href="javascript:search();" class="copysmall">  
                                    <bean:message key="label.search"/> 
                                </a--%> <html:link
															href="javascript:search()">
															<bean:message key="label.search" />
														</html:link> <%}%>
													</td>
												</tr>
												<tr>
													<td nowrap align="left" class="copybold"><bean:message
															key="investigatorsLabel.FirstName" />:</td>
													<td align="left" class="copy" align="right">
														<% objValue = formdata==null ? "" : (String)formdata.get("invFirstName");%>
														<html:text property="invFirstName" styleClass="textbox"
															onchange="setChangeFlag();" disabled="<%=modeValue%>"
															readonly="true" value="<%= objValue%>" />
													</td>
												</tr>
												<tr>
													<td nowrap class="copybold" align="left"><bean:message
															key="investigatorsLabel.Employee" />:</td>
													<td class="copy" colspan="2">
														<%objValue = formdata==null ? "" : (String)formdata.get("is_Employee");
                                if(objValue.equals("Y"))
                                        objValue = "";%> <html:checkbox
															property="is_Employee" disabled="<%=modeValue%>"
															onclick="refresh()" value="<%= objValue%>" />
													</td>
												</tr>
												<tr>
													<td nowrap align="left" class="copybold"><bean:message
															key="investigatorsLabel.Phone" />:</td>
													<td align="left" class="copy" align="right">
														<% objValue = formdata==null ? "" : (String)formdata.get("invPhone");%>
														<html:text property="invPhone" styleClass="textbox"
															disabled="<%=modeValue%>" onchange="setChangeFlag();"
															readonly="true" value="<%= objValue%>" />
													</td>
												</tr>
												<tr>
													<td nowrap align="left" class="copybold"><bean:message
															key="investigatorsLabel.Email" />:</td>
													<td align="left" class="copy" align="right">
														<%objValue = formdata==null ? "" : (String)formdata.get("invEmail");%>
														<html:text property="invEmail" styleClass="textbox"
															disabled="<%=modeValue%>" onchange="setChangeFlag();"
															readonly="true" value="<%= objValue%>" />
													</td>
												</tr>
											</table>
										</td>
										<%-- Right column fields --%>
										<td width="50%" align="left" valign="top" nowrap class="copy">
											<table width="100%" border="0" cellpadding="0">
												<tr>
													<td align="left" nowrap class="copybold"><font
														color="red">*</font>
													<bean:message key="investigatorsLabel.Dept" />:</td>
													<td align="left" nowrap class="copysmall">
														<%objValue = formdata==null ? "" : (String)formdata.get("unitNumber");%>
														<html:text property="unitNumber" styleClass="textbox"
															disabled="<%=modeValue%>" onblur="setDeptNum();"
															onchange="setChangeFlag();" readonly="<%=blnReadOnly%>"
															value="<%= objValue%>" /> <%if(!modeValue){%> <%--a href="javascript:unit_search('unit');" class="copysmall" >
                                    <bean:message key="label.search"/>
                                </a--%> <html:link
															href="javascript:unit_search('unit')">
															<bean:message key="label.search" />
														</html:link> <%}%>

													</td>
												</tr>
												<tr>
													<td align="left" nowrap class="copy"></td>
													<td align="left" class="copy">
														<%objValue = formdata==null ? "" : (String)formdata.get("unitName");%>
														<html:text property="unitName"
															styleClass="textbox-longer-clear"
															onchange="setChangeFlag();" readonly="true"
															value="<%=objValue%>" /><br>
													</td>
												</tr>
												<tr>
													<td nowrap class="copybold"><font color="red">*</font>
													<bean:message key="investigatorsLabel.Role" />:</td>
													<td align="left" class="copy"><html:select
															property="invRoleCode" styleClass="textbox-long"
															onchange="setChangeFlag();" disabled="<%=modeValue%>">
															<html:options collection="investigatorRoles"
																property="code" labelProperty="description" />
														</html:select></td>
												</tr>
												<tr>
													<td nowrap class="copybold"><font color="red">*</font>
													<bean:message key="investigatorsLabel.AffiliationType" />:
													</td>
													<td align="left" class="copy"><html:select
															property="affiliationTypeCode" styleClass="textbox-long"
															onchange="setChangeFlag();" disabled="<%=modeValue%>">
															<html:options collection="affiliationTypes"
																property="code" labelProperty="description" />
														</html:select></td>
												</tr>
												<tr>
													<td colspan="2" nowrap class="copy"><img
														src="<bean:write name='ctxtPath'/>/coeusliteimages/spacer.gif"
														width="5" height="25"></td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<%if(!modeValue){%>
						<tr>
							<td nowrap class="copy" align="center"><br> <html:submit
									property="Save" value="Save" styleClass="clbutton" /></td>

						</tr>

						<%}%>
						<%}%>



						<tr>
							<td>&nbsp;</td>
						</tr>

					</table></td>
			</tr>
			<!--Add Investigator Information: - End   -->
			<tr>
				<td height='10'>&nbsp;</td>
			</tr>
			<!-- List of Investigators  - Start -->
			<tr>
				<td align="left" valign="top"><table width="99%" border="0"
						align="center" cellpadding="0" cellspacing="0" class="tabtable">
						<tr>
							<td colspan="4" align="left" valign="top"><table
									width="100%" height="20" border="0" cellpadding="0"
									cellspacing="0" class="tableheader">
									<tr>
										<td><bean:message key="investigatorsLabel.LstOfInv" />:</td>
									</tr>
								</table></td>
						</tr>
						<tr align="center">
							<td colspan="3"><br>
								<DIV
									STYLE="overflow: auto; width: 750px; height: 330px; padding: 0px; margin: 0px">
									<%//if (protocolInvData.size() > 0){ %>
									<table width="100%" border="0" cellpadding="0" class="tabtable">
										<tr>
											<td width="20%" align="left" class="theader"><bean:message
													key="investigatorsLabel.Name" /></td>
											<td width="30%" align="left" class="theader"><bean:message
													key="investigatorsLabel.Dept" /></td>
											<td width="20%" align="left" class="theader"><bean:message
													key="investigatorsLabel.Role" /></td>
											<td width="20%" align="left" class="theader"><bean:message
													key="investigatorsLabel.AffiliationType" /></td>
											<%if(!modeValue){%>
											<td width="10%" align="left" class="theader">&nbsp;</td>
											<%}%>
										</tr>

										<% String strBgColor = "#DCE5F1"; //BGCOLOR=EFEFEF  FBF7F7
                          int count = 0;%>
										<logic:present name="protocolInvData">
											<logic:iterate id="protoInvBean" name="protocolInvData"
												type="org.apache.struts.validator.DynaValidatorForm">

												<% if (count%2 == 0) 
                            strBgColor = "#D6DCE5"; 
                         else 
                            strBgColor="#DCE5F1";
                       %>

												<tr bgcolor="<%=strBgColor%>" valign="top"
													onmouseover="className='TableItemOn'"
													onmouseout="className='TableItemOff'">
													<td align="left" nowrap class="copy"><%=protoInvBean.get("personName")%></td>
													<td align="left" nowrap class="copy">
														<% java.util.ArrayList vecInvUnits = (ArrayList)protoInvBean.get("investigatorUnits");
                           String strLeadUnit = "";
                           if (vecInvUnits != null&& vecInvUnits.size()>0) {
                                ProtocolInvestigatorUnitsBean protoInvUnitsBean = (ProtocolInvestigatorUnitsBean)vecInvUnits.get(0);
                                strLeadUnit = protoInvUnitsBean.getUnitName();
                           }
                           
                           %> <%if(strLeadUnit != null){%> <%=strLeadUnit%>
														<%}else{%> <%=""%> <%}%>
													</td>
													<td align="left" nowrap class="copy"><logic:equal
															name="protoInvBean" property="principalInvestigatorFlag"
															value="Y">
                                Principal Investigator
                            </logic:equal> <logic:equal name="protoInvBean"
															property="principalInvestigatorFlag" value="N">
                                Co-Investigator
                            </logic:equal></td>
													<td align="left" nowrap class="copy"><%=protoInvBean.get("affiliationTypeDescription")%></td>
													<td nowrap class="copy">
														<%if(!modeValue){
                            String removeLink = "javascript:delete_data('D','" +protoInvBean.get("personId") +"','" +protoInvBean.get("protoInvestigatortimeStamp") +"')";
                        %> <%--a href="javascript:delete_data('D','<%=protoInvBean.get("personId")%>','<%=protoInvBean.get("protoInvestigatortimeStamp")%>')"> 
                                <bean:message key="fundingSrc.Remove"/>
                            </a--%> <html:link href="<%=removeLink%>">
															<bean:message key="fundingSrc.Remove" />
														</html:link> <%}%>
													</td>
												</tr>
												<%count++;%>
											</logic:iterate>
										</logic:present>
									</table>
									<%//}%>
								</div></td>
						</tr>



						<tr>
							<td>&nbsp;</td>
						</tr>

					</table></td>
			</tr>
			<!-- List of Investigators  -End -->

			<html:hidden property="acType" />
			<html:hidden property="protocolNumber" />
			<html:hidden property="sequenceNumber" />
			<%String personValue = formdata==null ? "" : (String)formdata.get("personId");%>
			<html:hidden property="personId" value="<%=personValue%>" />
			<html:hidden property="protoInvestigatortimeStamp" />
			<html:hidden property="principalInvestigatorFlag" />
			<%String value = formdata==null ? "" : (String)formdata.get("personName");%>
			<html:hidden property="personName" value="<%= value%>" />




			<tr>
				<td height='10'>&nbsp;</td>
			</tr>


		</table>
		<!-- New Template for cwInvestigator - End  -->


	</html:form>

	<script>
      setForm(document.investigatorForm);
    </script>
</body>
</html:html>
