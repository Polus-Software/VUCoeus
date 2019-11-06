<%@ page
	import="org.apache.struts.validator.DynaValidatorForm,
edu.mit.coeuslite.utils.ComboBoxBean,
java.util.Vector"%>

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
<jsp:useBean id="allEntityProjects" scope="session"
	class="java.util.Vector" />
<bean:size id="totalEntityProjects" name="allEntityProjects" />
<jsp:useBean id="entityNumber" scope="request" class="java.lang.String" />
<jsp:useBean id="entityName" scope="request" class="java.lang.String" />
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
        document.forms[0].submit();
    }
    function trim(stringToTrim) {
        return stringToTrim.replace(/^\s+|\s+$/g,"");
    }
     function view_comments(val,module,moduleItemKey) {
       var value;
       var module=module;
       var moduleItemKey=moduleItemKey;
       if(val.length<1000)
                {value=val; }
       else
                 {value=val.substring(0,1000);}
        var w = 550;
        var h = 148;
        if(navigator.appName == "Microsoft Internet Explorer") {
            w = 522;
            h = 131;
        }
        if (window.screen) {
               leftPos = Math.floor(((window.screen.width - 500) / 2));
               topPos = Math.floor(((window.screen.height - 350) / 2));
         }
        
                var loc = '<bean:write name='ctxtPath'/>/coeuslite/dartmouth/utils/dialogs/cwProjectComments.jsp?moduleItemKey='+moduleItemKey+'&module='+module +'&value='+value +'&type=S';
                var newWin = window.open(loc,"formName",'dependent=1,toolbar=0,location=0,directories=0,status=0, menubar=0,scrollbars=0,resizable=0,width='+w+',height='+h+',left='+ leftPos + ',top=' + topPos);
                newWin.window.focus();
         
         }
</script>
<body>

	<html:form action="/updAnnDisclosure.do" method="post">
		<table width="800" height="100%" border="0" cellpadding="0"
			cellspacing="0" class="table">
			<logic:present name="financialEntity" scope="session">
				<logic:iterate id="finEntData" name="financialEntity">
					<tr>
						<td height="119" align="left" valign="top">
							<table width="100%" border="0" align="center" cellpadding="0"
								cellspacing="0" class="tabtable">
								<tr>
									<td colspan="4" align="left" valign="top">
										<table width="100%" height="20" border="0" cellpadding="0"
											cellspacing="0" class="theader">
											<tr>
												<td height="20" align="left" valign="top" class="theader">
													<bean:message bundle="coi" key="label.annualDisclosure" />
													<%=person.getFullName()%> - <font color="red"><bean:write
															name="finEntData" property="entityName" /></font>
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
														<bean:message bundle="coi" key="label.annDisclMainMsg3" />&nbsp;&nbsp;
														<html:link
															page="/coeuslite/mit/coi/tiles/annualDisclosureConfirmationTile.jsp">
															<b><u> <bean:message bundle="coi" key="label.ok" /></u></b>
														</html:link>
													</td>
												</tr>
											</table></td>
									</tr>

								</logic:present>

								<logic:messagesPresent message="true">
									<tr class='copybold' align="left">
										<td>&nbsp;&nbsp;</td>
									<tr>
									<tr class='copybold' align="left">
										<td><font color="red"> <html:messages id="message"
													message="true" property="invalidConflictStatus"
													bundle="coi">
													<bean:write name="message" />
												</html:messages>
										</font></td>
									</tr>
									<tr class='copybold' align="left">
										<td><font color="red"><html:messages id="message"
													message="true" property="invalidRelationShip" bundle="coi">
													<bean:write name="message" />
												</html:messages> </font></td>
									</tr>
								</logic:messagesPresent>
								<logic:messagesNotPresent message="true">
									<tr>
										<td>&nbsp;&nbsp;</td>
									</tr>
								</logic:messagesNotPresent>
								<tr class='copybold' align="left">
									<td>&nbsp;&nbsp;</td>
								<tr>
								<tr>
									<td class="copy" colspan="6">&nbsp;&nbsp;<bean:message
											bundle="coi" key="label.annDisclMainMsg6" /> :&nbsp; <select
										name="conflictStatus" style='' onchange="changeAllStatus();">
											<option class='copy' selected>-
												<bean:message bundle="coi" key="label.pleaseSelect" /></option>

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
									<td colspan="0" align="center"><br>
										<table width="98%" border="0" cellpadding="3" class="tabtable">

											<tr>
												<td width="10%" align="left" class="theader"><bean:message
														bundle="coi" key="label.sponsor" /></td>
												<td width="37%" align="left" class="theader"><bean:message
														bundle="coi" key="label.title" /></td>
												<td width="20%" align="left" class="theader"><bean:message
														bundle="coi" key="label.conflictStatus" /></td>
												<td width="15" align="left" class="theader"><bean:message
														bundle="coi" key="label.awardProposalProtocol" /></td>
												<td width="11%" align="left" class="theader"><bean:message
														bundle="coi" key="label.number" /></td>
												<td width="7%" align="left" class="theader"><bean:message
														bundle="coi" key="label.comments" /></td>
											</tr>

											<%  int disclIndex = 0;
                    int index = 0;
                    String strBgColor = "#DCE5F1";
                    session.getAttribute("allEntityProjects");
                    %>
											<logic:present name="allEntityProjects" scope="session">
												<logic:iterate id="disclosure" name="allEntityProjects"
													type="org.apache.commons.beanutils.DynaBean">
													<%
                        String entityNum = (String)disclosure.get("entityNumber");
                        String EMPTY_STRING = "";
                        if((entityNum != null && entityNumber.equals(entityNum)) || (entityNum == null || EMPTY_STRING.equals(""))){     
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
																<%  Integer statusCode = (Integer)disclosure.get("coiStatusCode");
                                        String status = statusCode != null ? statusCode.toString() : null;
                                        String code = "";
                                        String description = "";
                                        String selected = "";
                                        %>
																<option value='0'>-
																	<bean:message bundle="coi" key="label.pleaseSelect" /></option>
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
														<td width="15%" class="copy" align="left">
															<%String moduleCode = ((String)disclosure.get("module"));%>
															<%=moduleCode%>
														</td>
														<td width="11%" class="copy"><bean:write
																name='disclosure' property='moduleItemKey' /></td>
														<%String relationShip = ((String)disclosure.get("relationship"));
                                    relationShip = relationShip==null ? "" : relationShip;
                                    %>
														<INPUT type='hidden' id="relComments"
															name="relComments<%=index%>" value="<%=relationShip%>">
														<%
                                String moduleItemKey = ((String)disclosure.get("moduleItemKey"));
                                String comments = (String)disclosure.get("relationship");
                                String viewLink = "javascript:view_comments('"+index+"','"+moduleCode+"','"+moduleItemKey+"')";
                                %>
														<td width="7%" class="copy"><html:link
																href="<%=viewLink%>">
																<bean:message bundle="coi" key="label.comments" />
															</html:link> <%Boolean relationShipFlag = (Boolean)disclosure.get("relationShipFlag");
                                    if(relationShipFlag != null && relationShipFlag.booleanValue()){
                                    %> <!--  <font color="red">*</font> -->
															<%
                                    }
                                    %> <%Vector newParam = (Vector)request.getAttribute("entPrjValidateData");
                                    request.setAttribute("entPrjValidateData",newParam);
                                    %></td>
													</tr>
													<% index++;%>
													<%}%>
												</logic:iterate>
											</logic:present>

											<%
                    Vector allEntityPro = (Vector)session.getAttribute("allEntityProjects");
                    session.setAttribute("allEntityPro",allEntityPro);
                    %>
											<tr class='table'>
												<td colspan='6' class='savebutton' align='left'><html:button
														property="save" value="Save and Proceed to Next Entity"
														styleClass="clbutton" onclick="javascript:chkReln();" />
													&nbsp; <html:button property="back" value="Back"
														styleClass="clbutton"
														onclick="javascript: history.go(-1);" /></td>


											</tr>

											<INPUT type='hidden' name='entityNumber'
												value='<%=entityNumber%>'>
										</table></td>
								</tr>
								<tr>
									<td>&nbsp;</td>
								</tr>

							</table>
						</td>
					</tr>

					<tr>
						<td height='10'>&nbsp;</td>
					</tr>
		</table>

	</html:form>
</body>
</html:html>