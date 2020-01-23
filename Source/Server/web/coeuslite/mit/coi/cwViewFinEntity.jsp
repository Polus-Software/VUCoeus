<%@ page import="org.apache.struts.validator.DynaValidatorForm"%>

<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@page errorPage="ErrorPage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="viewFinEntityData" scope="request"
	class="java.util.Vector" />
<jsp:useBean id="viewFinEntityCertData" scope="request"
	class="java.util.Vector" />
<jsp:useBean id="person" scope="session"
	class="edu.mit.coeus.bean.PersonInfoBean" />
<jsp:useBean id="entityNumber" scope="request" class="java.lang.String" />
<jsp:useBean id="loggedinpersonid" class="java.lang.String"
	scope="session" />
<jsp:useBean id="userprivilege" class="java.lang.String" scope="session" />
<%
//create a hash map which holds the parameter names and values
java.util.HashMap htmlLinkValues = new java.util.HashMap();
htmlLinkValues.put("entityNo", entityNumber); ;
pageContext.setAttribute("htmlLinkValues", htmlLinkValues);
%>

<html:html>
<head>
<link rel=stylesheet
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/ipf.css"
	type="text/css">
<title>Coeus Web</title>
<html:base />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<script language='javascript'>
         function showQuestion(link)
         {
            var winleft = (screen.width - 650) / 2;
            var winUp = (screen.height - 450) / 2;  
            var win = "scrollbars=1,resizable=1,width=790,height=450,left="+winleft+",top="+winUp
            sList = window.open('<bean:write name='ctxtPath'/>'+"/"+link, "list", win);
            if (parseInt(navigator.appVersion) >= 4) {
            window.sList.focus(); 
            }
         }
    </script>
</head>
<body>
	<%--  ************  START OF BODY TABLE  ************************--%>

	<table width="980" border="0" cellpadding="0" cellspacing="0"
		class="table">
		<logic:present name="viewFinEntityData" scope="request">
			<logic:iterate id="viewdata" name="viewFinEntityData"
				type="org.apache.commons.beanutils.DynaBean">
				<tr>
					<td height="20" align="left" valign="top" class="theader"><bean:message
							bundle="coi" key="financialEntity.headerFinEntitiesDet" /> - <coeusUtils:formatOutput
							name="viewdata" property="entityName" /></td>
				</tr>

				<tr>
					<td height='25'>
						<table width="100%" cellspacing="0" border="0">
							<tr>
								<td class="copybold" width="50%">&nbsp;&nbsp; <bean:message
										bundle="coi" key="label.personName" /> : <%=person.getFullName()%>
								</td>
								<%
                                String header = request.getParameter("header");
                                if(!(header != null && (header.equalsIgnoreCase("n") || header.equalsIgnoreCase("no")))){
                                %>
								<td class='copybold' align="left">
									<%--<a href="<bean:write name='ctxtPath'/>/getReviewFinEnt.do"><u>Back</u></a>--%>
									<a href="javascript:history.back()"><u>Back</u></a>
								</td>
								<%}%>
								<td class='copybold' align="left">
									<%if(!(header != null && (header.equalsIgnoreCase("n") || header.equalsIgnoreCase("no")))){
                                    %> <logic:notPresent
										name="viewFinEntityDisplay" scope="request">
										<%
                                        if (Integer.parseInt(userprivilege) == 2){                                     
                                        %>
										<html:link action="/editFinEnt.do" paramName="viewdata"
											paramProperty="entityNumber" paramId="entityNumber">
											<u><bean:message bundle="coi"
													key="financialEntity.editLink" /></u>
										</html:link>
										<%
                                        } else{
                                            if(viewdata.get("personId").equals(loggedinpersonid)){
                                        %>
										<html:link action="/editFinEnt.do" paramName="viewdata"
											paramProperty="entityNumber" paramId="entityNumber">
											<u><bean:message bundle="coi"
													key="financialEntity.editLink" /></u>
										</html:link>
										<%
                                            }
                                        }
                                        %>
									</logic:notPresent> <%}else{%> <a href="javascript:window.close()"><big>Close</big></a>
									<%}%>
								</td>
							</tr>
						</table>
					</td>
				</tr>


				<tr class='copy' align="left">
					<font color="red"> <html:messages id="message"
							message="true">
							<bean:write name="message" />
						</html:messages>
					</font>
				</tr>
				<!-- EntityDetails - Start  -->
				<tr>
					<td height="119" align="left" valign="top">
						<table width="99%" border="0" align="center" cellpadding="0"
							cellspacing="0" class="tabtable">
							<tr>
								<td align="left" valign="top">
									<table width="100%" height="20" border="0" cellpadding="0"
										cellspacing="0" class="tableheader">
										<tr>
											<td><bean:message bundle="coi"
													key="financialEntity.subheaderEntityDet" /></td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>

								<td valign="top" class="copy" align="left">
									<table width="100%" border="0" cellpadding="2">
										<tr>
											<td nowrap class="copybold" width="10%" align="left"><bean:message
													bundle="coi" key="label.entityName" /></td>
											<td width="5" class="copybold">:</td>
											<td width="40%" class="copy"><coeusUtils:formatOutput
													name="viewdata" property="entityName" /></td>

											<td nowrap class="copybold" align="left"><bean:message
													bundle="coi" key="label.shareOwnerShip" /></td>
											<td width="5" class="copybold">:</td>
											<td width="40%" class="copy"><logic:equal
													name="viewdata" property="shareOwnerShip" value='P'>
													<bean:message bundle="coi" key="financialEntity.public" />
												</logic:equal> <logic:notEqual name="viewdata" property="shareOwnerShip"
													value='P'>
													<bean:message bundle="coi" key="financialEntity.private" />
												</logic:notEqual> <logic:notPresent name="viewdata" property="shareOwnerShip">
                                                    &nbsp;
                                                </logic:notPresent></td>
										</tr>

										<tr>
											<td nowrap class="copybold" align="left"><bean:message
													bundle="coi" key="label.currentStatus" /></td>
											<td class="copybold">:</td>
											<td class="copy"><coeusUtils:formatOutput
													name="viewdata" property="status" /></td>

											<td nowrap class="copybold" align="left"><bean:message
													bundle="coi" key="label.orgTypeCode" /></td>
											<td class="copybold">:</td>
											<td class="copy"><coeusUtils:formatOutput
													name="viewdata" property="entityType" /></td>
										</tr>

										<tr>
											<td nowrap class="copybold" align="left"><bean:message
													bundle="coi" key="label.statusExplanation" /></td>
											<td class="copybold">:</td>
											<td class="copy"><coeusUtils:formatOutput
													name="viewdata" property="statusDesc" /></td>
										</tr>

										<tr>
											<td nowrap class="copybold" align="left"><bean:message
													bundle="coi" key="label.lastUpdated" /></td>
											<td class="copybold">:</td>
											<td class="copy"><coeusUtils:formatDate name="viewdata"
													formatString="MM-dd-yyyy  hh:mm a" property="updtimestamp" />
												&nbsp; <bean:write name="viewdata" property="upduser" /></td>

										</tr>

									</table>
								</td>
							</tr>

						</table>
					</td>
				</tr>
				<tr>
					<td height='10'>&nbsp;</td>
				</tr>
				<!-- RelationShip - Start -->
				<tr>
					<td height="52" align="left" valign="top"><table width="99%"
							border="0" align="center" cellpadding="0" cellspacing="0"
							class="tabtable">
							<tr>
								<td colspan="4" align="left" valign="top"><table
										width="100%" height="20" border="0" cellpadding="0"
										cellspacing="0" class="tableheader">
										<tr>
											<td><bean:message bundle="coi"
													key="financialEntity.subheaderRelEntity" /></td>
										</tr>
									</table></td>
							</tr>

							<tr>
								<td>
									<table width="100%" border="0" cellpadding="2">
										<tr>
											<td nowrap class="copybold" width="10%" align="left"><bean:message
													bundle="coi" key="label.entityRelType" /></td>
											<td width="6" class="copybold">:</td>
											<td class="copy" nowrap colspan="2">&nbsp;&nbsp; <coeusUtils:formatOutput
													name="viewdata" property="relationShipTypeDesc" />
											</td>
										</tr>
										<tr>
											<td class="copybold" width="10%" align="left"><bean:message
													bundle="coi" key="label.descRelationship" /></td>
											<td width="6" class="copybold">:</td>
											<td class="copy" colspan="2">&nbsp;&nbsp; <coeusUtils:formatOutput
													name="viewdata" property="relationShipDesc" />
											</td>
										</tr>
										<tr>
											<td class="copybold" width="10%" align="left"><bean:message
													bundle="coi" key="label.relationShipToResearch" /></td>
											<td width="6" class="copybold">:</td>
											<td class="copy" colspan="2">&nbsp;&nbsp; <%-- JIRA COEUSDEV-544 --%>
												<coeusUtils:formatOutput name="viewdata"
													property="orgRelnDesc" />
											</td>
										</tr>
									</table>

								</td>
							</tr>
							</logic:iterate>
							</logic:present>

						</table></td>
				</tr>
				<!-- RelationShip -End -->

				<!--Certification -Start -->

				<tr>
					<td height="119" align="left" valign="top"><br>
						<table width="99%" border="0" align="center" cellpadding="2"
							cellspacing="0" class="tabtable">
							<tr class="tableheader">
								<td colspan="3"><bean:message bundle="coi"
										key="financialEntity.subheaderCertification" /></td>
							</tr>

							<% String strBgColor = "#DCE5F1";
                int certificateIndex=0;
                %>

							<logic:present name="viewFinEntityCertData" scope="request">
								<logic:iterate id="viewDataCert" name="viewFinEntityCertData"
									type="org.apache.commons.beanutils.DynaBean">

									<%
                        if (certificateIndex%2 == 0) {
                    strBgColor = "#D6DCE5";
                        } else {
                    strBgColor="#DCE5F1";
                        }   %>
									<tr bgcolor="<%=strBgColor%>" class="rowLine"
										onmouseover="className='rowHover rowLine'"
										onmouseout="className='rowLine'">
										<td width="8%" class="copybold" valign=top wrap colspan="0"><bean:message
												bundle="coi" key="label.question" />
											<%--Question--%> <coeusUtils:formatOutput name="viewDataCert"
												property="label" /></td>
										<td width="80%" class="copy" wrap colspan="0"><coeusUtils:formatOutput
												name="viewDataCert" property="questionDesc" /> <logic:notPresent
												name="viewFinEntityDisplay" scope="request">
												<a
													href='javascript:showQuestion("/question.do?questionNo=<bean:write name='viewDataCert' property='questionId'/>",  "ae");'
													method="POST"> <u> <bean:message bundle="coi"
															key="financialEntity.moreAbtQuestion" />
												</u>
												</a>
											</logic:notPresent></td>
										<td align="center" class="copy"><logic:equal
												name="viewDataCert" property="answer" value='Y'>
												<bean:message bundle="coi" key="financialEntity.yes" />
											</logic:equal> <logic:equal name="viewDataCert" property="answer" value='N'>
												<bean:message bundle="coi" key="financialEntity.no" />
											</logic:equal> <logic:equal name="viewDataCert" property="answer" value='X'>
												<bean:message bundle="coi" key="financialEntity.na" />
											</logic:equal> <logic:notPresent name="viewDataCert" property="answer">
                                    &nbsp;
                                </logic:notPresent> <%
                                certificateIndex++;
                                %></td>
									</tr>
								</logic:iterate>
							</logic:present>
							<%--<tr> 
                    <td class='copybold' align="center">
                        <logic:present name="viewFinEntityDisplay"  scope="request">
                            <html:link href="javascript:window.close();" styleClass="copy">
                                <u><bean:message bundle="budget" key="budgetLineItemDetailsButton.close"/></u>
                            </html:link>
                        </logic:present>
                    </td>
                </tr>--%>

						</table></td>
				</tr>
				<!--Certification -End -->
				<tr>
					<td height='10' align="center"><br> <%String header = request.getParameter("header");
            if(header != null && (header.equalsIgnoreCase("n") || header.equalsIgnoreCase("no"))){%>
						<a href="javascript:window.close()"><big>Close</big></a> <%}%><br>
					</td>
				</tr>
	</table>

</body>
</html:html>