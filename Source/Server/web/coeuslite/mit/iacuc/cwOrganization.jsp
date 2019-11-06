<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>

<%@ page
	import="edu.mit.coeuslite.utils.CoeusLiteConstants,java.lang.StringBuffer"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>

<jsp:useBean id="protocolOrganizationTypes" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="iacucFreqUsedOrgNames" scope="session"
	class="java.util.Vector" />

<html:html locale="true">
<%
         boolean hasValidationMessage = false;
        String mode=(String)session.getAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId());
        boolean modeValue=false;
        if(mode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE)){
            modeValue=true;
        }
        
        
        edu.mit.coeuslite.utils.CoeusDynaBeansList coeusDynaBeansList = (edu.mit.coeuslite.utils.CoeusDynaBeansList) session.getAttribute("iacucOrganizationList");
    //Added for coeus4.3 Amendments and Renewal enhancement
    String strProtocolNum = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
    if(strProtocolNum == null)
        strProtocolNum = "";    
    String amendRenewPageMode = (String)session.getAttribute("amendRenewPageMode"+request.getSession().getId());
    if(strProtocolNum.length() > 10 && (amendRenewPageMode == null || 
            amendRenewPageMode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE))){
        modeValue = true;
    }%>

<head>
<style>
.cltextbox-color {
	font-weight: normal;
	width: 380px;
}

.textbox-longer {
	width: 620px;
}
</style>

<title>Organization</title>
<script language="JavaScript">
            var errValue = false;
            var errLock = false;
            var searchSelection = "";
            var organizationId = "";
            var organizationName = "";
            var roloAddress = "";
            var gIndex = "";
            var rolodexId = "";
            var rslt = '1';
            var toSelect = 0;
            var selectedValue=0;
            //Function for fetching result data
            function fetch_Data(result){
                var modifiedValue = "";
                if(searchSelection == 'organization'){
                    if(result["ORGANIZATION_NAME"]!="null" && result["ORGANIZATION_NAME"]!= undefined){                    
                        organizationName = result["ORGANIZATION_NAME"];
                        organizationId = result["ORGANIZATION_ID"];
                        modifiedValue = "dynaFormData[0].addOrganizationName";
                        modifiedValue = document.getElementsByName(modifiedValue);
                        modifiedValue[0].value = organizationName;              
                        modifiedValue = document.getElementsByName('dynaFormData[0].organizationId');
                        modifiedValue[0].value = organizationId;
                        //Added for Coeus4.3 Organization Page changes enhacement - Start
                        var orgCombo = document.getElementsByName('dynaFormData[0].freqUsedCode');                                                
                        checkOrganizationPresent(organizationId);                                                
                        if(rslt == '1'){
                            var oOption = document.createElement("option");
                            // Added for the case #3746 - Organization Search not populating on first try -Start-
                            oOption.selected=true;
                            // Added for the case #3746 - Organization Search not populating on first try -end-*/
                            oOption.value = organizationId;
                            oOption.innerHTML = organizationName;                        
                            orgCombo[0].appendChild(oOption);   
                            var len = orgCombo[0].length;
                            orgCombo[0].selectedIndex = len-1;
                       } else {
                             orgCombo[0].selectedIndex = toSelect;
                        }
                        //Added for Coeus4.3 Organization Page changes enhacement - end
                    }
                    rslt = '1';
                    dataChanged();
                }
        
                if(searchSelection == 'rolodex'){            
                    rolodexId = "";
                    rolodexId = result["ROLODEX_ID"];
                    /*
                    //Commented because address is picked using rolodexId from server side and then
                    //displayed in jsp.
                    roloAddress = "";
                    if(result["ORGANIZATION"] != 'null' && result["ORGANIZATION"] != undefined ){ 
                        roloAddress += result["ORGANIZATION"]+"\n";
                    }
                    if(result["ADDRESS_LINE_1"] != 'null' && result["ADDRESS_LINE_1"] != undefined ){ 
                        roloAddress += result["ADDRESS_LINE_1"]+"\n";
                    }
                    if(result["ADDRESS_LINE_2"] != 'null' && result["ADDRESS_LINE_2"] != undefined ){ 
                        roloAddress += result["ADDRESS_LINE_2"]+"\n";
                    }
                    if(result["ADDRESS_LINE_3"] != 'null' && result["ADDRESS_LINE_3"] != undefined ){ 
                        roloAddress += result["ADDRESS_LINE_3"]+"\n";
                    }
                    if(result["CITY"] != 'null' && result["CITY"] != undefined ){ 
                        roloAddress += result["CITY"]+"\n";
                    }
                    if(result["COUNTY"] != 'null' && result["COUNTY"] != undefined ){ 
                        roloAddress += result["COUNTY"]+"\n";
                    }                
                    if(result["STATE"] != 'null' && result["STATE"] != undefined ){ 
                        roloAddress += result["STATE"]+" - ";
                    }
                    if(result["POSTAL_CODE"] != 'null' && result["POSTAL_CODE"] != undefined ){ 
                        roloAddress += result["POSTAL_CODE"]+"\n";
                    }
                    if(result["COUNTRY_CODE"] != 'null' && result["COUNTRY_CODE"] != undefined ){ 
                        roloAddress += result["COUNTRY_CODE"];
                    }
                    */
                    //Added to facilitate updation of single record - start
                    var updateLink = "/updateIacucOrganization.do?rowId=" +gIndex;
                    updateOrganization(updateLink,'F');                    
                    //Added to facilitate updation of single record - end
                }
            }
    
            //Function to set new address and rolodexId
            /*
            //No longer used.
            function setAddressData(roloAddress,rolodexId){
                var modifiedValue = document.getElementsByName('dynaFormBean['+gIndex+'].completeAddress');
                modifiedValue[0].value = roloAddress;
                modifiedValue = document.getElementsByName('dynaFormBean['+gIndex+'].rolodexId');
                modifiedValue[0].value = rolodexId;
            }
            */

            //Function for opening search
            function open_search(value,rowIndex){
                searchSelection = value;
                gIndex = rowIndex;
                var winleft;
                var winUp;
                var win;
                if(value == 'rolodex'){
                    winleft = (screen.width - 650) / 2;
                    winUp = (screen.height - 450) / 2;
                    win = "scrollbars=1,resizable=1,width=750,height=450,left="+winleft+",top="+winUp;
                    sList = window.open('<%=request.getContextPath()%>/iacucSearch.do?type=<bean:message key="searchWindow.title.rolodex"/>&search=true&searchName=WEBROLODEXSEARCH', "list", win);  
                }else{
                    winleft = (screen.width - 500) / 2;
                    winUp = (screen.height - 450) / 2;
                    win = "scrollbars=1,resizable=1,width=650,height=450,left="+winleft+",top="+winUp
                    sList = window.open('<%=request.getContextPath()%>/iacucSearch.do?type=<bean:message key="searchWindow.title.organization"/>&search=true&searchName=WEBORGANIZATIONSEARCH', "list", win);          
                }
                window.sList.focus();
            }
    
            //Function for saving protocol organization
            function saveOrganization(){                
                document.iacucOrganizationList .action = "<%=request.getContextPath()%>/saveIacucOrganization.do?organizationId="+organizationId;
                document.iacucOrganizationList.Save.disabled=true;//Added for case 4267-Double clicking save results in error
                document.iacucOrganizationList.submit();
            }
    
            //Function to delete protocol organization
            function deleteProtocolOrgnization(link){
                if(confirm("<bean:message key="protocolOrganization.deleteOrganization" bundle="iacuc"/>")) {
                    document.iacucOrganizationList.action = "<%=request.getContextPath()%>"+link;
                    document.iacucOrganizationList.submit();
                }
            }
    
            //Function for updating protocol organization
            function updateOrganization(link,type){
                if(type=='C'){
                    if(confirm("<bean:message key="protocolOrganization.deleteAddress"/>")){
                        document.iacucOrganizationList.action = "<%=request.getContextPath()%>"+link +"&rolodexId=" +rolodexId;
                        document.iacucOrganizationList.submit();
                    } 
               }else{
                    document.iacucOrganizationList.action = "<%=request.getContextPath()%>"+link +"&rolodexId=" +rolodexId;
                    document.iacucOrganizationList.submit();
               }
            }
    
            //Function for clearing address and rolodexId
            /*
            //No longer used.
            function clearAddress(cIndex){
                if(confirm("<bean:message key="protocolOrganization.deleteAddress"/>")){
                    var modifiedValue = document.getElementsByName('dynaFormBean['+cIndex+'].completeAddress');
                    modifiedValue[0].value = "";
                    modifiedValue = document.getElementsByName('dynaFormBean['+cIndex+'].rolodexId');
                    modifiedValue[0].value = "";    
                }
            }
            */
            
            <!-- Added for Coeus4.3 Organization Page changes enhacement - Start -->            
            function setComponent(sel){
                dataChanged();
                organizationId = sel.options[sel.selectedIndex].value;
                organizationName = sel.options[sel.selectedIndex].text;   
                modifiedValue = "dynaFormData[0].addOrganizationName";
                modifiedValue = document.getElementsByName(modifiedValue);
                modifiedValue[0].value = organizationName;
                modifiedValue = document.getElementsByName('dynaFormData[0].organizationId');
                modifiedValue[0].value = organizationId;               
            }  

            function checkOrganizationPresent(orgId){                
                var v = document.getElementsByName('dynaFormData[0].freqUsedCode');
                var localOrgId = "";                
                for(var i = 0; i < v[0].length; i++){                    
                    localOrgId = v[0].options[i].value;
                    if(localOrgId == orgId){                        
                        rslt = '0';
                        toSelect = i;
                        break;
                    }
                }                  
            }
            function showHide(toggleId) {
                //alert(toggleId); 
                //1-Show 0- Hide
             if(toggleId == 1){
                document.getElementById('showOrHide').style.display = "none";
                selectedValue=0;
            }else if(toggleId == 0){
                document.getElementById('showOrHide').style.display = "";
                document.getElementById('showOrg').style.display = "none"; 
                selectedValue=1;
            }
          }
          function cancelAddOrganization(){
            //document.getElementById('showOrg').style.display = ""; 
            document.getElementById('showOrHide').style.display = "none";
            clearFormData();
            document.iacucOrganizationList .action = "<%=request.getContextPath()%>/getIacucOrganization.do";
            document.iacucOrganizationList.submit();
          }
            <!-- Added for Coeus4.3 Organization Page changes enhacement - End -->
            
          function clearFormData(){
            
          }
            
        </script>
<html:base />
</head>
<body>
	<html:form action="/getIacucOrganization.do">
		<script type="text/javascript">
            document.getElementById('txt').innerHTML = '<bean:message bundle="iacuc" key="helpPageTextProtocol.Organization"/>';
        </script>
		<table width="100%" height="100%" border="0" cellpadding="2"
			cellspacing="0" class="table" align='center'>

			<tr>
				<td>
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr class="tableheader">
							<td width="50%" height="20" align="left"><bean:message
									key="protocolOrganization.label.header" /></td>
							<td width="50%" height="20" align="right"><a
								id="helpPageText"
								href="javascript:showBalloon('helpPageText', 'helpText')"> <bean:message
										bundle="iacuc" key="helpPageTextProtocol.help" />
							</a></td>
						</tr>
					</table>
				</td>
			</tr>

			<%if(!modeValue){%>
			<tr class="copy">
				<td align="left" width='100%'><font color="red"> <logic:messagesPresent>
						<script>errValue = true; </script>
						<%hasValidationMessage = true;%>
						<html:errors header="" footer="" />
					</logic:messagesPresent> <logic:messagesPresent message="true">
						<%hasValidationMessage = true;%>
						<script>errValue = true;</script>
						<html:messages id="message" message="true" property="errMsg">
							<script>errLock = true;</script>
							<li><bean:write name="message" /></li>
						</html:messages>
						<html:messages id="message" message="true"
							property="protocolOrganization.error.orgType">
							<li><bean:write name="message" /></li>
						</html:messages>
						<html:messages id="message" message="true"
							property="protocolOrganization.error.orgName">
							<li><bean:write name="message" /></li>
						</html:messages>
						<html:messages id="message" message="true"
							property="protocolOrganization.error.orgDup">
							<li><bean:write name="message" /></li>
						</html:messages>
						<html:messages id="message" message="true"
							property="protocolOrganization.error.orgMin">
							<li><bean:write name="message" /></li>
						</html:messages>
					</logic:messagesPresent> </font></td>
			</tr>
			<%}%>
			<!-- Add Organization - Start  -->
			<%if(!modeValue){%>
			<tr>
				<td><div id="showOrg">
						<a href="javascript:showHide(selectedValue);"><u>Add
							Organization</u></a>
					</div></td>
			</tr>
			<%}%>
			<tr>
				<td align="left" valign="top">
					<div id="showOrHide" style="display: none">
						<table width="100%" border="0" align="center" cellpadding="0"
							cellspacing="0" class="tabtable">
							<tr>
								<td class="copysmall"><bean:message
										key="helpText.Organization.Modify" bundle="iacuc" /></td>
							</tr>
							<tr>
								<td>
									<table width="100%" border="0" cellpadding="0" cellspacing="3">
										<logic:present name="iacucOrganizationList">
											<logic:iterate id="dynaFormData" name="iacucOrganizationList"
												property="list"
												type="org.apache.struts.action.DynaActionForm"
												indexId="index" scope="session">
												<tr>
													<td class="copybold" nowrap width="10%" align="right">
														<bean:message key="protocolOrganization.label.type" />:&nbsp;
													</td>
													<td class="copy" width="90%"><html:select
															name="dynaFormData" property="code" indexed="true"
															onchange="dataChanged()" styleClass="textbox-long"
															disabled="<%=modeValue%>">
															<html:option value="">
																<bean:message key="generalInfoLabel.pleaseSelect" />
															</html:option>
															<html:options collection="protocolOrganizationTypes"
																property="code" labelProperty="description" />
														</html:select></td>
												</tr>
												<tr>
													<html:hidden name="dynaFormData" property="organizationId"
														indexed="true" />
													<html:hidden name="dynaFormData"
														property="addOrganizationName" indexed="true" />
													<td class="copybold" nowrap width="10%" align="right">
														<bean:message
															key="protocolOrganization.label.organization" />:&nbsp;
													</td>
													<td class="copy" width="90%"><html:select
															name="dynaFormData" property="freqUsedCode"
															indexed="true" onchange="setComponent(this)"
															styleClass="textbox-long" disabled="<%=modeValue%>">
															<html:option value="">
																<bean:message key="generalInfoLabel.pleaseSelect" />
															</html:option>
															<html:options collection="iacucFreqUsedOrgNames"
																property="code" labelProperty="description" />
														</html:select> <% if(!modeValue){%> <html:link
															href="javascript:open_search('organization','0');">
															<u>
															<bean:message key="protocolOrganization.link.search" /></u>
														</html:link> <%}%></td>
												</tr>
											</logic:iterate>
										</logic:present>
									</table>
								</td>
							</tr>
							<tr>
								<td style='padding-left: 4px;' class="table" height='25'
									colspan="2" align="left" valign=middle>
									<% if(!modeValue){%> <html:button property="Save"
										styleClass="clsavebutton" onclick="saveOrganization()">
										<bean:message key="protocolOrganization.button.save" />
									</html:button> <html:button property="Save" styleClass="clsavebutton"
										onclick="cancelAddOrganization()">
										<bean:message bundle="iacuc" key="button.label.cancel" />
									</html:button> <%}%>
								</td>
							</tr>
						</table>
					</div>
				</td>

			</tr>

			<!-- Add Organization - Ends  -->

			<!-- Display List of Organizations Start -->
			<tr>
				<td align="left" valign="top">
					<table width="100%" border="0" align="center" cellpadding="0"
						cellspacing="0" class="tabtable">

						<tr>
							<td>
								<table width="100%" border="0" cellpadding="0">
									<logic:present name="iacucOrganizationList">
										<logic:iterate id="dynaFormBean" name="iacucOrganizationList"
											property="beanList"
											type="org.apache.struts.action.DynaActionForm"
											indexId="beanIndex">
											<tr>
												<td align="right" valign="top" class="copybold" width="16%">
													<bean:message key="protocolOrganization.label.type" />:&nbsp;
												</td>
												<td class="copy" align="left" nowrap>
													<%--<html:text name="dynaFormBean" property="description" readonly="true" indexed="true" styleClass="cltextbox-color"/>--%>
													<bean:write name="dynaFormBean" property="description" />
												</td>
												<td class="copybold" nowrap>
													<% if(!modeValue){
                                    String deleteLink = "javaScript:deleteProtocolOrgnization('/deleteIacucOrganization.do?rowId="+beanIndex+"')";
                                %> <html:link href="<%=deleteLink%>">
														<u>
														<bean:message key="protocolOrganization.link.delete" /></u>
													</html:link> &nbsp;&nbsp;|&nbsp;&nbsp;<%}%> <% if(!modeValue){
                                    String searchLink = "javascript:open_search('rolodex','" +beanIndex +"')";
                                %> <html:link href="<%=searchLink%>">
														<u>
														<bean:message key="protocolOrganization.link.find" /></u>
													</html:link> &nbsp;&nbsp;|&nbsp;&nbsp;<%}%> <% if(!modeValue){
                                    //Commented because all updations are done first at the server side and then brought to jsp - start - 1
                                    //String clearLink = "javascript:clearAddress('" +beanIndex +"')";
                                    //Commented because all updations are done first at the server side and then brought to jsp - end - 1

                                    //Add to facilitate updation of single record - start - 2
                                    String updateLink = "javaScript:updateOrganization('/updateIacucOrganization.do?rowId="+beanIndex +"','C')";
                                    //Add to facilitate updation of single record - end - 2
                                %> <html:link href="<%=updateLink%>">
														<u>
														<bean:message key="protocolOrganization.link.clear" /></u>
													</html:link> <%}%>
												</td>

												<%--td class="copybold" align="right" nowrap>
                                <% if(!modeValue){
                                    String searchLink = "javascript:open_search('rolodex','" +beanIndex +"')";
                                %>
                                <html:link href="<%=searchLink%>">
                                    <u><bean:message key="protocolOrganization.link.find"/></u>
                                </html:link>
                                <%}%>
                            </td--%>

												<%--td class="copybold" align="right" nowrap>
                                <% if(!modeValue){
                                    //Commented because all updations are done first at the server side and then brought to jsp - start - 1
                                    //String clearLink = "javascript:clearAddress('" +beanIndex +"')";
                                    //Commented because all updations are done first at the server side and then brought to jsp - end - 1

                                    //Add to facilitate updation of single record - start - 2
                                    String updateLink = "javaScript:updateOrganization('/updateProtocolOrganization.do?rowId="+beanIndex +"','C')";
                                    //Add to facilitate updation of single record - end - 2
                                %>
                                <html:link href="<%=updateLink%>">
                                    <u><bean:message key="protocolOrganization.link.clear"/></u>
                                </html:link>
                                <%}%>
                            </td--%>

											</tr>

											<tr>
												<td class='copybold' valign="top" align="right" width="16%">
													<bean:message key="protocolOrganization.label.organization" />:&nbsp;
												</td>
												<td colspan='2' class='copy' width="90%" align="left" nowrap>
													<%--<html:text name="dynaFormBean" property="organizationName" readonly="true" indexed="true" styleClass="cltextbox-color"/>--%>
													<bean:write name="dynaFormBean" property="organizationName" />
												</td>
											</tr>
											<tr>
												<td class='copybold' valign="top" align="right" width="16%">
													<bean:message key="protocolOrganization.label.address" />:&nbsp;
												</td>
												<td class='copy' align="left">
													<%--
                                    <html:textarea name="dynaFormBean" property="completeAddress" readonly="true" rows="5" indexed="true" styleClass="cltextbox-color"/>
                                    <html:hidden name="dynaFormBean" property="rolodexId" indexed="true"/>
                                    --%> <%
                                    String tempContact = "";
                                    tempContact = (String)dynaFormBean.get("organization");
                                    if(tempContact != null){%> <%=tempContact%><br>
														<%}
                                    tempContact = (String)dynaFormBean.get("addressLine_1");
                                    if(tempContact != null){%> <%=tempContact%><br>
															<%}
                                    tempContact = (String)dynaFormBean.get("addressLine_2");
                                    if(tempContact != null){%> <%=tempContact%><br>
																<%}
                                    tempContact = (String)dynaFormBean.get("addressLine_3");
                                    if(tempContact != null){%> <%=tempContact%><br>
																	<%}
                                   
                                    tempContact = (String)dynaFormBean.get("city");
                                    if(tempContact != null){%> <%=tempContact%><br>
																		<%}
                                    tempContact = (String)dynaFormBean.get("county");
                                    if(tempContact != null){ 
                                        if(!tempContact.equals("")){%> <%=tempContact%><br>
																			<%}}
                                    tempContact = (String)dynaFormBean.get("state");
                                    if(tempContact != null){%> <%=tempContact%>
																			- <%}
                                    tempContact = (String)dynaFormBean.get("postalCode");
                                    if(tempContact != null){%> <%=tempContact%><br>
																				<%}
                                    tempContact = (String)dynaFormBean.get("countryCode");
                                    if(tempContact != null){%> <%=tempContact%><br>
																					<%}%>
																			
												</td>
											</tr>

											<tr>
												<td class='copybold' valign="top" align="right" width="16%">
													<bean:message bundle="iacuc"
														key="protocolOrganization.label.animalassurance" />:&nbsp;
												</td>
												<td colspan='2' class='copy' width="90%" align="left" nowrap>
													<bean:write name="dynaFormBean"
														property="animalWelfareAssurance" />
												</td>
											</tr>

											<tr height='10'>
												<td colspan="7"></td>
											</tr>

										</logic:iterate>
									</logic:present>
								</table> <%-- 
                    //Commented because we no longer update multiple records, instead a single record is updated at a time - start - 3
                    <tr>
                        <td height='10'>
                        </td>
                    </tr>             
                    <tr align="center">
                        <td>
                            
                            <% if(!modeValue){%>
                                <html:button property="Save" styleClass="clbutton" onclick="updateOrganization()">
                                    <bean:message key="protocolOrganization.button.save"/>
                                </html:button>
                            <%}%>
                            
                        </td>
                    </tr>       
                    <tr>
                        <td height='10'>
                        </td>
                    </tr>
                    //Commented because we no longer update multiple records, instead a single record is updated at a time - start - 3
                    --%>
							</td>
						</tr>
					</table>
				</td>
			</tr>

			<!-- Display List of Organizations End -->
		</table>
	</html:form>



	<script>
        
        DATA_CHANGED = 'false';
        if(errValue && !errLock) {
            DATA_CHANGED = 'true';
        }
        LINK = "<%=request.getContextPath()%>/saveIacucOrganization.do?organizationId="+organizationId;
        FORM_LINK = document.iacucOrganizationList;
        PAGE_NAME = "<bean:message key="protocolOrganization.label.header"/>";
        function dataChanged(){
            DATA_CHANGED = 'true';
            LINK = "<%=request.getContextPath()%>/saveIacucOrganization.do?organizationId="+organizationId;
        }
        linkForward(errValue);
    </script>
	<script>
        var help = '<bean:message key="helpTextProtocol.Organization"/>';
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
