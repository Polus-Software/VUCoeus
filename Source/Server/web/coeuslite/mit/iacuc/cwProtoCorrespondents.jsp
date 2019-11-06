<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@ page
	import="edu.mit.coeuslite.utils.CoeusLiteConstants,
                java.util.Vector,
                org.apache.struts.validator.DynaValidatorForm,
                edu.mit.coeuslite.utils.ComboBoxBean"%>

<%--
The taglib directive below imports the JSTL library. If you uncomment it,
you must also add the JSTL library to the project. The Add Library... action
on Libraries node in Projects view can be used to add the JSTL 1.1 library.
--%>
<%--
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html:html>
<% boolean hasValidationMessage = false;
    String mode=(String)session.getAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId());
    boolean modeValue=false;
    if(mode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE)){
        modeValue=true;
    }else if(mode.equalsIgnoreCase(CoeusLiteConstants.MODIFY_MODE)){
        modeValue=false;
    }else if(mode.equalsIgnoreCase(CoeusLiteConstants.ADD_MODE)){
        modeValue=false;
    }    
    String strProtocolNum = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
    if(strProtocolNum == null)
        strProtocolNum = "";    
    String amendRenewPageMode = (String)session.getAttribute("amendRenewPageMode"+request.getSession().getId());
    if(strProtocolNum.length() > 10 && (amendRenewPageMode == null || 
            amendRenewPageMode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE))){
        modeValue = true;
    }    
    Vector correspondentData = (Vector) session.getAttribute("iacucCorrespondentData");          
    %>

<html:base />

<head>

<title>Protocol Correspondents</title>
<script language="JavaScript" type="text/JavaScript">    
        
        var searchSelection = "";    
        var errValue = false;
        var errLock = false;  
        var selectedValue=0;
        function searchWindow(value){            
            searchSelection = value;
            var winleft = (screen.width - 650) / 2;
            var winUp = (screen.height - 450) / 2;  
            var win = "scrollbars=1,resizable=1,width=750,height=450,left="+winleft+",top="+winUp;
            if(value == 'person'){
                sList = window.open('<%=request.getContextPath()%>/iacucSearch.do?type=<bean:message key="searchWindow.title.person"/>&search=true&searchName=WEBPERSONSEARCH', "list", win);  
            }else if (value == 'rolodex'){
                sList = window.open('<%=request.getContextPath()%>/iacucSearch.do?type=<bean:message key="searchWindow.title.rolodex"/>&search=true&searchName=WEBROLODEXSEARCH', "list", win);  
            }
            if (parseInt(navigator.appVersion) >= 4) {
                window.sList.focus(); 
            }            
        }  
        
        function fetch_Data(result){
            var name="";
            var flag="";
            var personId="";        
            dataChanged();      
            document.iacucCorrespondentsForm.acType.value = 'I';            
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
            }else{    
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
            }            
            document.iacucCorrespondentsForm.nonEmployeeFlag.value = flag;
            document.iacucCorrespondentsForm.personId.value = personId;
            document.iacucCorrespondentsForm.personName.value = name;    
        }  
        
        function saveCorrespondents(){
            document.iacucCorrespondentsForm.action = "<%=request.getContextPath()%>/saveIacucCorrespondent.do";
            document.iacucCorrespondentsForm.submit();                     
        }
        
        function deletCorrespondent(correspondentType, personId){
            if(confirm("<bean:message key="correspondents.deleteConfirmation"/>")) {
                document.iacucCorrespondentsForm.action = "<%=request.getContextPath()%>/deleteIacucCorrespondent.do?correspondentType="+correspondentType+"&personId="+personId;
                document.iacucCorrespondentsForm.submit();        
            }
        }
        
        function editCorrespondent(correspondentType, personId){            
            document.iacucCorrespondentsForm.action = "<%=request.getContextPath()%>/editIacucCorrespondent.do?correspondentType="+correspondentType+"&personId="+personId;
            document.iacucCorrespondentsForm.submit();        
        }  
        
        function viewComments(comments){
            var w = 550;
            var h = 213;
            if(navigator.appName == "Microsoft Internet Explorer") {
                w = 522;
                h = 196;
            }
            if(window.screen){
                leftPos = Math.floor(((window.screen.width - 500) / 2));
                topPos = Math.floor(((window.screen.height - 350) / 2));
            }
            var loc = '<bean:write name='ctxtPath'/>/coeuslite/mit/utils/dialogs/cwViewText.jsp?value='+comments;
            var newWin = window.open(loc,"formName",'dependent=1,toolbar=0,location=0,directories=0,status=0, menubar=0,scrollbars=0,resizable=0,width=' + w + ',height=' + h+ ',left='+ leftPos + ',top=' + topPos);
            newWin.window.focus();        
        }
        function showHide(toggleId) {
               // alert(toggleId); 
                //1-Show 0- Hide
             if(toggleId == 1){
                document.getElementById('showOrHide').style.display = "none";
                selectedValue=0;
            }else if(toggleId == 0){
                document.getElementById('showOrHide').style.display = "";
                document.getElementById('showCorrespondents').style.display = "none"; 
                selectedValue=1;
            }
          }
          function cancelInvestigators(){
            document.iacucCorrespondentsForm.personName.value = '';
            document.iacucCorrespondentsForm.correspondentType.value = '';
            document.iacucCorrespondentsForm.comments.value = '';
            document.getElementById('showCorrespondents').style.display = ""; 
            document.getElementById('showOrHide').style.display = "none";
            document.getElementById('showValPanel').style.display = "none";
          }
        </script>
</head>
<body>
	<html:form action="/getIacucCorrespondents.do">
		<%--Issue #1621-Correspondent Help Message is Not Provided-Lite start --%>
		<script type="text/javascript">
            document.getElementById('txt').innerHTML = '<bean:message bundle="iacuc" key="helpPageTextProtocol.Correspondents"/>';
        </script>
		<%--Issue #1621-Correspondent Help Message is Not Provided-Lite end --%>
		<table width='100%' cellpadding='0' cellspacing='0' class='table'>

			<tr>
				<td>
					<table width="100%" border="0" cellpadding="0" cellspacing="0"
						class="tableheader">
						<tr>
							<td height="20" width="50%" align="left" valign="top"><bean:message
									key="correspondents.correspondents" /></td>
							<td align="right" width="50%"><a id="helpPageText"
								href="javascript:showBalloon('helpPageText', 'helpText')"> <bean:message
										bundle="iacuc" key="helpPageTextProtocol.help" />
							</a></td>
						</tr>
					</table>
				</td>
			</tr>



			<tr>
				<td>
					<div id="helpText" class='helptext'>
						<bean:message key="helpTextProtocol.Correspondents" />
					</div>
				</td>
			</tr>


			<tr class='theader' style='font-weight: normal;'>
				<td class='copy'><div id='showValPanel'>
						<font color="red"> <logic:messagesPresent>
								<script>errValue = true; </script>
								<%hasValidationMessage = true;%>
								<html:errors header="" footer="" />
							</logic:messagesPresent> <logic:messagesPresent message="true">
								<%hasValidationMessage = true;%>
								<script>errValue = true; </script>
								<html:messages id="message" message="true"
									property="duplicateRecordsNotAllowed">
									<li><bean:write name="message" /></li>
								</html:messages>
								<html:messages id="message" message="true"
									property="personRequired">
									<li><bean:write name="message" /></li>
								</html:messages>
								<html:messages id="message" message="true"
									property="correspondentRequired">
									<li><bean:write name="message" /></li>
								</html:messages>
								<html:messages id="message" message="true" property="errMsg"
									bundle="proposal">
									<script>errLock = true;</script>
									<li><bean:write name="message" /></li>
								</html:messages>
							</logic:messagesPresent>
						</font>
					</div></td>
			</tr>
			<%if(!modeValue){%>
			<tr>
				<td><div id="showCorrespondents">
						<a href="javascript:showHide(selectedValue);"><u><bean:message
									key="correspondents.addCorrespondents" /><u></a>
					</div></td>
			</tr>
			<%}%>
			<!-- Add Section -->
			<tr>
				<td>
					<div id="showOrHide" style="display: none">
						<table width='100%' cellpadding='2' cellspacing='0'
							class='tabtable' border="0">
							<tr>
								<td align="left" nowrap colspan="3"><a
									href="javaScript:searchWindow('person')" class="copy"> <u><bean:message
												key="correspondents.findPerson" /></u>&nbsp;|
								</a> <a href="javaScript:searchWindow('rolodex')" class="copy">
										<u><bean:message key="correspondents.findRolodex" /></u>
								</a></td>
							</tr>

							<tr>
								<td nowrap class="copybold" align="right"><bean:message
										key="correspondents.name" />:&nbsp;</td>
								<td colspan="3"><html:text property="personName"
										styleClass="cltextbox-color" disabled="<%=modeValue%>"
										readonly="true" /></td>
							</tr>

							<tr>
								<td nowrap class="copybold" align="right"><bean:message
										key="correspondents.type" />:&nbsp;</td>
								<td><html:select property="correspondentType"
										styleClass="textbox-long" disabled="<%=modeValue%>"
										onchange="dataChanged()">
										<html:option value="">
											<bean:message key="correspondents.pleaseSelect" />
										</html:option>
										<html:options collection="correspondentTypes" property="code"
											labelProperty="description" />
									</html:select></td>
							</tr>
							<tr>
								<td nowrap class="copybold" align="right"><bean:message
										key="correspondents.comments" />:&nbsp;</td>
								<td><html:textarea property="comments"
										styleClass="textbox-longer" cols="80"
										readonly="<%=modeValue%>" onchange="dataChanged()" /></td>
							</tr>
							<tr class='table'>
								<td style='padding-left: 4px;' height='25' colspan="2"
									align="left" valign=middle><html:button property="Save"
										styleClass="clsavebutton" onclick="saveCorrespondents()">
										<bean:message key="correspondents.button.save" />
									</html:button> <html:button property="cancel" styleClass="clsavebutton"
										onclick="cancelInvestigators();">
										<bean:message bundle="iacuc" key="button.label.cancel" />
									</html:button></td>
							</tr>
						</table>
					</div>
				</td>
			</tr>

			<!-- List Section -->
			<tr>
				<td class='core'>
					<table width="100%" border="0" align="center" cellpadding="0"
						cellspacing="0" class="tabtable">
						<tr>
							<td>
								<table width="100%" border="0" cellpadding="0" class="tabtable">
									<tr>
										<td width="25%" class="theader"><bean:message
												key="correspondents.type" /></td>
										<td width="25%" class="theader"><bean:message
												key="correspondents.name" /></td>
										<td width="40%" class="theader"><bean:message
												key="correspondents.comments" /></td>
										<td width="5%" class="theader"></td>
										<td width="5%" class="theader"></td>
									</tr>

									<%
                                        String strBgColor = "#DCE5F1";
                                        int count = 0;
                                    %>

									<logic:present name="iacucCorrespondentData">
										<logic:iterate id="correspondentBean"
											name="iacucCorrespondentData"
											type="org.apache.struts.validator.DynaValidatorForm">
											<%                                         
                                       if (count%2 == 0) 
                                            strBgColor = "#D6DCE5"; 
                                       else 
                                            strBgColor="#DCE5F1"; 
                                    %>
											<tr bgcolor="<%=strBgColor%>"
												onmouseover="className='TableItemOn'"
												onmouseout="className='TableItemOff'">
												<td align="left" class="copy">
													<%                                                    
                                                    String correspondentName = "";
                                                    Vector vecCorrespondentTypes = (Vector)session.getAttribute("correspondentTypes");        
                                                    if(vecCorrespondentTypes != null && vecCorrespondentTypes.size() > 0) {
                                                        for(int index=0 ; index < vecCorrespondentTypes.size(); index++) {
                                                            ComboBoxBean comboBoxBean = (ComboBoxBean) vecCorrespondentTypes.get(index);
                                                            if(comboBoxBean != null && comboBoxBean.getCode().equals(correspondentBean.get("correspondentType"))) {
                                                                correspondentName = comboBoxBean.getDescription();
                                                            }
                                                        }
                                                    }                                               
                                                %> <%  
                                                    String editLink = "javascript:editCorrespondent('" 
                                                            +correspondentBean.get("correspondentType")
                                                            +"','"
                                                            +correspondentBean.get("personId")
                                                            +"')";
                                                %> <%if(!modeValue){%> <html:link
														href="<%=editLink%>">
														<%=correspondentName%>
													</html:link> <%}else{%> <%=correspondentName%> <%}%>
												</td>
												<td align="left" class="copy"><%=correspondentBean.get("personName")%>
												</td>
												<td align="left" class="copy">
													<%if(correspondentBean.get("comments") != null){
                                                    String viewComments = (String)correspondentBean.get("comments");
                                                    viewComments = (viewComments.length() > 40) ? viewComments.substring(0, 41) +"..." : viewComments;                                                        
                                                %> <%=viewComments%> <%}%>
												</td>
												<td nowrap class="copy">
													<%
                                                    String viewLink = "javascript:viewComments('"
                                                            +correspondentBean.get("comments")
                                                            +"')";
                                                %> <html:link
														href="<%=viewLink%>">
														<bean:message key="correspondents.view" />
													</html:link>
												</td>
												<td nowrap class="copy">
													<%  
                                                    String deleteLink = "javascript:deletCorrespondent('" 
                                                            +correspondentBean.get("correspondentType")
                                                            +"','"
                                                            +correspondentBean.get("personId")
                                                            +"')";
                                                %> <%if(!modeValue){%> <html:link
														href="<%=deleteLink%>">
														<bean:message key="correspondents.remove" />
													</html:link> <%}%>
												</td>
											</tr>
											<% count++;%>
										</logic:iterate>
									</logic:present>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>

		</table>
		<html:hidden property="acType" />
		<html:hidden property="personId" />
		<html:hidden property="nonEmployeeFlag" />
	</html:form>
	<script>
      DATA_CHANGED = 'false';      
      if(errValue && !errLock){
            DATA_CHANGED = 'true';
      }
      if(document.iacucCorrespondentsForm.acType.value == 'U'){
        showHide(0);
      }
      LINK = "<%=request.getContextPath()%>/saveIacucCorrespondent.do";
      FORM_LINK = document.iacucCorrespondentsForm;
      PAGE_NAME = "<bean:message key="correspondents.correspondents"/>";
      function dataChanged(){
        DATA_CHANGED = 'true';
      }
      linkForward(errValue);
</script>
	<script>
      var help = '<bean:message key="helpTextProtocol.Correspondents"/>';
      help = trim(help);
      if(help.length == 0) {
        document.getElementById('helpText').style.display = 'none';
      }
      function trim(s) {
            return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
      }  
      <%if(hasValidationMessage){%>
        showHide(0);
      <%}%>
</script>
</body>
</html:html>
