<%@ page
	import="org.apache.struts.validator.DynaValidatorForm,
    edu.mit.coeuslite.utils.ComboBoxBean"%>

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="person" scope="session"
	class="edu.mit.coeus.bean.PersonInfoBean" />
<jsp:useBean id="allAnnualDiscEntities" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="financialEntity" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="allPendingDisclosures" scope="session"
	class="java.util.Vector" />
<bean:size id="totalPendingDisclosures" name="allPendingDisclosures" />
<jsp:useBean id="entityNumber" scope="request" class="java.lang.String" />
<jsp:useBean id="coiStatus" scope="session" class="java.util.Vector" />


<html:html>
<head>
<title>Coeus Web</title>
<html:base />

</head>
<script>
    function changeAllStatus(){

        var conflictStatus = document.forms[0].conflictStatus.value;
        for(var elementIndex=0; elementIndex < document.forms[0].elements.length; elementIndex++){
            var elementName = document.forms[0].elements[elementIndex].name;
            if( (elementName.indexOf("sltConflictStatus") != -1)
                || (elementName.indexOf("disclConflictStatus") != -1) ){
                document.forms[0].elements[elementIndex].value=conflictStatus;
            }
        }	
    }
    function chkReln(){
        var strValue=document.getElementById('reln').value;
        var value=trim(strValue);
        if(value==null || value.length==0)
        {
            alert("Please enter Financial Entity's realtion with your MIT Research");
            document.getElementById('reln').focus();
            return false;
        }else if(document.coiDisclosure.entityOrgRln.value.length > 2000) {
            //alert(document.coiDisclosure.entityOrgRln.value.length);
            alert('<bean:message bundle="coi" key="error.EntityRelDesc.length"/>');
            document.coiDisclosure.entityOrgRln.focus();
            return false;
        }else{
            document.forms[0].submit();
        }
    }
    function trim(stringToTrim) {
        return stringToTrim.replace(/^\s+|\s+$/g,"");
    }
</script>
<body>
	<html:form action="/annualDisclosuresUpdate.do" method="post">

		<table width="775" height="100%" border="0" cellpadding="0"
			cellspacing="0" class="table">

			<logic:present name="financialEntity" scope="session">
				<logic:iterate id="finEntData" name="financialEntity">


					<!-- Annual Disclosure - Start  -->
					<tr>
						<td height="119" align="left" valign="top">
							<table width="99%" border="0" align="center" cellpadding="0"
								cellspacing="0" class="tabtable">
								<tr>
									<td colspan="4" align="left" valign="top">
										<table width="100%" height="20" border="0" cellpadding="0"
											cellspacing="0" class="theader">
											<tr>
												<td height="20" align="left" valign="top" class="theader">
													<bean:message bundle="coi" key="label.annualDisclosure" />
													<%--Annual Disclosure for--%> <%=person.getFullName()%> - <bean:write
														name="finEntData" property="entityName" />

												</td>
											</tr>
										</table>
									</td>
								</tr>

								<logic:notPresent name="allEntitiesUpdated">
									<tr>
										<td class="copybold">&nbsp;&nbsp; <bean:message
												bundle="coi" key="label.annDisclMainMsg1" /> <font
											color="red"><bean:write name="finEntData"
													property="entityName" /> </font> <bean:message bundle="coi"
												key="label.annDisclMainMsg2" />
										</td>
									</tr>
								</logic:notPresent>

								<logic:present name="allEntitiesUpdated">
									<tr>
										<td><br>
											<table class="tabtable" width="98%" align="center" border="0"
												cellpadding="10" cellspacing="0">
												<tr>
													<td bgcolor="#FFFF99" class='copybold' align="center">
														<bean:message bundle="coi" key="label.annDisclMainMsg3" />
														&nbsp;&nbsp; <%-- #3139 following link is broken, since attribute is given as link instead of page. by Geo on 08/17/2007 --%>
														<%-- <html:link href="coeuslite/mit/coi/tiles/annualDisclosureConfirmationTile.jsp"> --%>
														<%--html:link page="/coeuslite/mit/coi/tiles/annualDisclosureConfirmationTile.jsp">
                                            <b><u> <bean:message bundle="coi" key="label.ok"/> </u></b>
                                    </html:link--%>
													</td>
												</tr>
											</table></td>
									</tr>

								</logic:present>

								<logic:messagesPresent message="true">
									<tr class='copybold' align="left">
										<td><font color="red"> <html:messages id="message"
													message="true" property="invalidConflictStatus"
													bundle="coi">
													<bean:write name="message" />
												</html:messages>
										</font></td>
									</tr>
								</logic:messagesPresent>
								<logic:messagesNotPresent message="true">
									<tr>
										<td>&nbsp;&nbsp;</td>
									</tr>
								</logic:messagesNotPresent>


								<tr>
									<td class="copy" colspan="6">&nbsp;&nbsp; <bean:message
											bundle="coi" key="label.annDisclMainMsg4" /> <%--<bean:write name="finEntData" property="entityName" /> to--%>
										:&nbsp; <select name="conflictStatus" style=''
										onchange="changeAllStatus();">
											<option class='copy' selected>-
												<bean:message bundle="coi" key="label.pleaseSelect" />
												<%--Please Select ---%></option>

											<%
            String description1 = "";
            String code1 = "";
            for (int i = 0; i < coiStatus.size(); i++) {
                ComboBoxBean objCombBean = (ComboBoxBean) coiStatus.get(i);
                code1 = objCombBean.getCode();
                description1 = "";
                if (code1.equals("301") || code1.equals("200")) {
                    description1 = objCombBean.getDescription();
                            %>

											<option class="copy" value="<%=code1%>"><%=description1%></option>
											<%
                }
            }
                            %>

									</select></td>
								</tr>

								</logic:iterate>
								</logic:present>



								<tr align="center">
									<td colspan="0" align="center"><br> <!--<DIV STYLE="overflow: auto; width: 710px; height: 532; padding:0px; margin: 0px">-->
										<table width="98%" border="0" cellpadding="3" class="tabtable">

											<tr>
												<td width="10%" align="left" class="theader"><bean:message
														bundle="coi" key="label.sponsor" /></td>
												<td width="30%" align="left" class="theader"><bean:message
														bundle="coi" key="label.title" /></td>
												<td width="20%" align="left" class="theader"><bean:message
														bundle="coi" key="label.conflictStatus" /></td>
												<td width="15%" align="left" class="theader"><bean:message
														bundle="coi" key="label.awardProposal" /></td>
												<td width="15%" align="left" class="theader"><bean:message
														bundle="coi" key="label.number" /></td>
												<%--<td width="15%" align="left" class="theader"><bean:message bundle="coi" key="label.disclosureNumber"/></td>--%>
											</tr>

											<%  int disclIndex = 0;
            int index = 0;
            String strBgColor = "#DCE5F1";%>
											<logic:present name="allPendingDisclosures" scope="session">
												<logic:iterate id="disclosure" name="allPendingDisclosures"
													type="org.apache.commons.beanutils.DynaBean">

													<%
            if (index % 2 == 0) {
                strBgColor = "#D6DCE5";
            } else {
                strBgColor = "#DCE5F1";
            }
                            %>

													<tr bgcolor='<%=strBgColor%>' class="rowLine"
														onmouseover="className='rowHover rowLine'"
														onmouseout="className='rowLine'">
														<td class="copy"><bean:write name='disclosure'
																property='sponsorName' /></td>
														<td class="copy"><bean:write name='disclosure'
																property='title' /></td>
														<td><select name='disclConflictStatus<%=disclIndex%>'
															style=''>
																<%  String status = (String) disclosure.get("coiStatusCode");
            String code = "";
            String description = "";
            String selected = "";
                                        %>
																<option>-
																	<bean:message bundle="coi" key="label.pleaseSelect" />
																	<%--Please Select ---%></option>
																<%
            for (int i = 0; i < coiStatus.size(); i++) {
                ComboBoxBean comboBox = (ComboBoxBean) coiStatus.get(i);
                code = comboBox.getCode();
                if (code.equals("301")) {
                    description = comboBox.getDescription();
                    break;
                }
            }
            if ((status != null) && (status.equals("301"))) {
                selected = "selected";
            }
                                        %>
																<option <%=selected%> value='301'><%=description%></option>
																<%
            for (int i = 0; i < coiStatus.size(); i++) {
                ComboBoxBean comboBox = (ComboBoxBean) coiStatus.get(i);
                code = comboBox.getCode();
                if (code.equals("200")) {
                    description = comboBox.getDescription();
                    break;
                }
            }
            selected = "";
            if ((status != null) && (status.equals("200"))) {
                selected = "selected";
            }
                                        %>
																<option <%=selected%> value='200'><%=description%></option>
														</select> <% disclIndex++;%></td>
														<td class="copy"><bean:write name='disclosure'
																property='module' /></td>
														<td widith="15%" class="copy"><bean:write
																name='disclosure' property='moduleItemKey' /></td>
														<%--<td class="copy"><bean:write
                                                                    name='disclosure' property='coiDisclosureNumber' />
                                </td>--%>
													</tr>
													<% index++;%>
												</logic:iterate>
											</logic:present>


											<INPUT type='hidden' name='entityNumber'
												value='<%=entityNumber%>'>
										</table> <!--</div>--></td>
								</tr>
								<tr>
									<td><br>
										<table width="98%" border="0" align="center" border="0"
											cellpadding="0" cellspacing="0">
											<tr>
												<td align="left" nowrap class="copybold"><bean:message
														bundle="coi" key="label.entityRelResearch" />:</td>
											</tr>
											<tr>
												<td align="left" class="copy" width='100%'><textArea
														id="reln" name="entityOrgRln" cols="75" rows="3"
														styleClass="copy"><bean:write name="finEntData"
															property="orgRelnDesc" /></textArea> <input type="hidden"
													name="relationShipDesc"
													value='<bean:write name="finEntData" property="relationShipDesc" />'>
												</td>
											</tr>
										</table></td>
								</tr>
								<tr>
									<td><br> &nbsp;&nbsp;<html:button property="save"
											value="Save and Proceed" onclick="javascript:chkReln();" />
										<br>
									<br></td>
								</tr>

							</table>
						</td>
					</tr>

					<!-- Annual Disclosure -End  -->
					<tr>
						<td height='10'>&nbsp;</td>
					</tr>
		</table>

	</html:form>
</body>
</html:html>