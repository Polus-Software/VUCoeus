<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%--start 1--%>
<%@page
	import="org.apache.struts.taglib.html.JavascriptValidatorTag,
                edu.mit.coeuslite.utils.CoeusLiteConstants"%>
<%--end 1--%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<jsp:useBean id="keyPersonData" scope="request" class="java.util.Vector" />
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>

<html:html>
<head>
<title>Coeus Web</title>
<html:base />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">

<script language="JavaScript">
    var validFlag; 
    
    function insert_data(data){
           
        document.keyStudyPerson.acType.value = data;
         document.keyStudyPerson.action = "<%=request.getContextPath()%>/keyStudyPerson.do";
     }
    
    function delete_data(data,subjectCount,timestamp){
         
         if (confirm("Are you sure you want to delete the key person?")==true){
            document.keyStudyPerson.acType.value = data;
            document.keyStudyPerson.personName.value=data;
            document.keyStudyPerson.personRole.value=subjectCount;
            document.keyStudyPerson.personId.value = subjectCount;
            document.keyStudyPerson.affiliationTimeStamp.value=timestamp; 
            document.keyStudyPerson.action = "<%=request.getContextPath()%>/keyStudyPerson.do";
            document.keyStudyPerson.submit();
         }
    }
    
    function open_search(valid)
          {
            validFlag = valid;
            var winleft = (screen.width - 650) / 2;
            var winUp = (screen.height - 450) / 2;  
            var win = "scrollbars=1,resizable=1,width=750,height=450,left="+winleft+",top="+winUp
            if(valid){
                sList = window.open('<%=request.getContextPath()%>/iacucSearch.do?type=<bean:message key="searchWindow.title.person"/>&search=true&searchName=WEBPERSONSEARCH', "list", win); 
                
           }else {
                sList = window.open('<%=request.getContextPath()%>/iacucSearch.do?type=<bean:message key="searchWindow.title.rolodex"/>&search=true&searchName=WEBROLODEXSEARCH', "list", win);
               
            }
          }
     
      function search()
        {
          
            if(document.keyStudyPerson.nonEmployeeFlag.checked){
                open_search(true);
            }else{
                open_search(false);
             }
         }
         
      function fetch_Data(result){
        var lastName;
        var suffix;
        var prefix;
        var firstName;
        var middleName;
        var rolodexName;
         if(validFlag){
             document.keyStudyPerson.personName.value = result["FULL_NAME"];
             
             document.keyStudyPerson.personId.value = result["PERSON_ID"];
        }else{
       
            if(result["LAST_NAME"]=="null"){lastName = ""}else{lastName = result["LAST_NAME"]};
            if(result["SUFFIX"]=="null"){suffix = ""}else{suffix = result["SUFFIX"]};
            if(result["PREFIX"]=="null"){prefix = ""}else{prefix = result["PREFIX"]};
            if(result["FIRST_NAME"]=="null"){firstName = ""}else{firstName = result["FIRST_NAME"]};
            if(result["MIDDLE_NAME"]=="null"){middleName = ""}else{middleName = result["MIDDLE_NAME"]};
            rolodexName = lastName+""+suffix+", "+prefix+""+firstName+""+middleName;
            if(rolodexName==", "){
            rolodexName = result["ORGANIZATION"];
            }
            document.keyStudyPerson.personName.value = rolodexName;
            document.keyStudyPerson.personId.value = result["ROLODEX_ID"];
         }
     }

    
</script>
<html:javascript formName="keyStudyPerson" dynamicJavascript="true"
	staticJavascript="true" />

</head>
<body>
	<%-- <html:errors/>--%>



	<% 
 //start--2
    
    String protocolNo = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
//end--2      
            if(protocolNo!= null){ 
                 protocolNo = "["+"Protocol No. - "+ protocolNo+"]";
            }else{
                protocolNo = "";
                }
//start--3        
    
    String mode=(String)session.getAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId());
            boolean modeValue=false;
                
            
            if(mode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE)){
                modeValue=true;
                
            
            }else if(mode.equalsIgnoreCase(CoeusLiteConstants.MODIFY_MODE)){
                modeValue=false;
            
            }else if(mode.equalsIgnoreCase(CoeusLiteConstants.ADD_MODE)){
                modeValue=false;
              }
//end--3            
 %>

	<script>
function validateForm(form) {
    insert_data("I");
    return validateKeyStudyPerson(form);
}

function refresh(){
    document.keyStudyPerson.personName.value="";
}
</script>

	<html:form action="/keyStudyPerson" method="post"
		onsubmit="return validateForm(this)">

		<!-- New Template for cwKeyStudyPersonForm - Start   -->


		<table width="100%" height="100%" border="0" cellpadding="0"
			cellspacing="0" class="table">

			<tr>
				<td height="20" align="left" valign="top" class="theader"><bean:message
						key="keyStudyPerLabel.KeyStudyPer" /></td>
			</tr>
			<tr>
				<td class="copybold">&nbsp;&nbsp; <font color="red">*</font> <bean:message
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
			<!-- Add Key Study Personnel - Start  -->
			<tr>
				<td align="left" valign="top"><table width="99%" border="0"
						align="center" cellpadding="0" cellspacing="0" class="tabtable">
						<tr>
							<td colspan="4" align="left" valign="top"><table
									width="100%" height="20" border="0" cellpadding="0"
									cellspacing="0" class="tableheader">
									<tr>
										<td><bean:message key="keyStudyPerLabel.AddKeyStudyPer" />:</td>
									</tr>
								</table></td>
						</tr>
						<tr>
							<%-- Left column fields --%>
							<td width="70%" valign="top" class="copy" align="left">
								<table width="100%" border="0" cellpadding="0">
									<tr>
										<td nowrap class="copybold" width="10%" align="left"><font
											color="red">*</font>
										<bean:message key="keyStudyPerLabel.PersonName" />:</td>
										<td class="copysmall" nowrap colspan="2"><html:text
												property="personName" styleClass="textbox-long"
												disabled="<%=modeValue%>" readonly="true" value=""
												maxlength="90" /> <%if(!modeValue){%> <%--a href="javascript:search();" class="copysmall"> 
                                        <bean:message key="label.search"/>  
                                    </a--%> <html:link
												href="javascript:search()">
												<bean:message key="label.search" />
											</html:link> <%}%></td>
									</tr>

									<tr>
										<td nowrap class="copybold" align="left">&nbsp;&nbsp;<bean:message
												key="keyStudyPerLabel.Employee" />:
										</td>
										<td class="copy" colspan="2"><html:checkbox
												property="nonEmployeeFlag" disabled="<%=modeValue%>"
												onclick="refresh()" value="" /></td>
									</tr>

									<tr>
										<td nowrap class="copybold" width="10%" align="left"><font
											color="red">*</font>
										<bean:message key="keyStudyPerLabel.Role" />:</td>
										<td class="copy" colspan="2"><html:text
												property="personRole" styleClass="textbox-long"
												disabled="<%=modeValue%>" value="" maxlength="60" /></td>
									</tr>
									<tr>
										<td nowrap class="copybold" width="10%" align="left"><font
											color="red">*</font>
										<bean:message key="keyStudyPerLabel.Affiliation" />:</td>
										<td><html:select property="code"
												styleClass="textbox-long" disabled="<%=modeValue%>">
												<html:options collection="affiliationTypes" property="code"
													labelProperty="description" />
											</html:select></td>
									</tr>
								</table> <%if(!modeValue){%>
							
						<tr>
							<td colspan="3" nowrap class="copy" align="center" width="50%"><br>

								<html:submit property="Save" value="Save" styleClass="clbutton" />

							</td>
							<%}%>
						</tr>
						</td>
						</tr>

						<tr>
							<td>&nbsp;</td>
						</tr>

					</table></td>
			</tr>
			<!-- Add Key Study Personnel - End -->

			<tr>
				<td height='10'>&nbsp;</td>
			</tr>
			<!-- List of Key Study Personnel - Start -->
			<tr>
				<td align="left" valign="top"><table width="99%" border="0"
						align="center" cellpadding="0" cellspacing="0" class="tabtable">
						<tr>
							<td colspan="4" align="left" valign="top"><table
									width="100%" height="20" border="0" cellpadding="0"
									cellspacing="0" class="tableheader">
									<tr>
										<td><bean:message key="keyStudyPerLabel.LstOfKeyStudyPer" />:
										</td>
									</tr>
								</table></td>
						</tr>
						<tr align="center">
							<td colspan="3"><br>
								<DIV
									STYLE="overflow: auto; width: 750px; height: 405px; padding: 0px; margin: 0px">
									<%
                                            //if (keyPersonData.size() > 0) 
                                            //{ %>
									<table width="95%" border="0" cellpadding="0" class="tabtable">
										<tr>
											<td width="60%" align="left" class="theader"><bean:message
													key="keyStudyPerLabel.PersonName" /></td>
											<td width="25%" align="left" class="theader"><bean:message
													key="keyStudyPerLabel.Role" /></td>
											<td width="25%" align="left" class="theader"><bean:message
													key="keyStudyPerLabel.Affiliation" /></td>
											<%if(!modeValue){%>
											<td width="25%" align="left" class="theader"></td>
											<%}%>

										</tr>
										<% String strBgColor = "#DCE5F1"; //BGCOLOR=EFEFEF  FBF7F7
                              int count = 0;
                             %>
										<logic:present name="keyPersonData">
											<logic:iterate id="keyDataBean" name="keyPersonData"
												type="org.apache.commons.beanutils.DynaBean">

												<%
                             System.out.println("beging of for loop");
                             //for(int index=0; index<keyPersonData.size();index++) {
                                // System.out.println("inside of for loop");
                                 //org.apache.struts.validator.DynaValidatorForm keyDataBean =
                                       // (org.apache.struts.validator.DynaValidatorForm)keyPersonData.get(index);
                                 System.out.println("personName "+keyDataBean.get("personName"));
                             if (count%2 == 0) 
                             strBgColor = "#D6DCE5"; 
                             else 
                             strBgColor="#DCE5F1"; 
                             %>
												<tr bgcolor='<%=strBgColor%>'
													onmouseover="className='TableItemOn'"
													onmouseout="className='TableItemOff'">
													<td align="left" nowrap class="copy"><%=keyDataBean.get("personName")%></td>
													<td align="left" nowrap class="copy"><%=keyDataBean.get("personRole")%></td>
													<td align="left" nowrap class="copy"><%=keyDataBean.get("description")%></td>
													<%if(!modeValue){
                                    String removeLink = "javascript:delete_data('D','" +keyDataBean.get("personId") +"','" +keyDataBean.get("affiliationTimeStamp") +"')";
                                %>
													<td nowrap class="copy">
														<%--a href="javascript:delete_data('D','<%=keyDataBean.get("personId")%>','<%=keyDataBean.get("affiliationTimeStamp")%>')">
                                        <bean:message key="fundingSrc.Remove"/>
                                    </a--%> <html:link
															href="<%=removeLink%>">
															<bean:message key="fundingSrc.Remove" />
														</html:link>

													</td>
													<%}%>
												</tr>
												<% count++;%>
											</logic:iterate>
										</logic:present>
									</table>
									<%//}%>
								</DIV></td>
						</tr>

						<tr>
							<td>&nbsp;</td>
						</tr>

					</table></td>
			</tr>
			<!-- List of Key Study Personnel -End -->

			<tr>
				<td height='10'>&nbsp;</td>
			</tr>
		</table>
		<!-- New Template for cwKeyStudyPersonForm - End  -->
		<html:hidden property="acType" />
		<html:hidden property="personId" />
		<html:hidden property="protocolNumber" />
		<html:hidden property="sequenceNumber" />
		<html:hidden property="affiliationTimeStamp" />

	</html:form>
</body>
</html:html>
