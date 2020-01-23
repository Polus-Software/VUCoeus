<%@ page import="org.apache.struts.validator.DynaValidatorForm"%>

<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@page errorPage="ErrorPage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="DisclosureInfoDetail" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="DisclosureInfoHistory" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="COIHeaderInfo" scope="session" class="java.util.Vector" />
<jsp:useBean id="person" scope="session"
	class="edu.mit.coeus.bean.PersonInfoBean" />



<html:html>
<head>
<title>JSP Page</title>
<html:base />
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<script language="JavaScript">

 function open_new_window(link)
     {
        var winleft = (screen.width - 850) / 2;
        var winUp = (screen.height - 650) / 2;  
        var win = "scrollbars=1,resizable=1,width=975,height=650,left="+winleft+",top="+winUp
        sList = window.open(link, "list", win);
        if (parseInt(navigator.appVersion) >= 4) {
            window.sList.focus(); 
            }
     }
     

</script>


</head>
<body>
	<a name="top"></a>
	<%--  ************  START OF BODY TABLE  ************************--%>

	<table width="980" height="100%" border="0" cellpadding="0"
		cellspacing="0" class="table">
		<logic:present name="DisclosureInfoDetail" scope="session">
			<logic:iterate id="infoData" name="DisclosureInfoDetail"
				type="org.apache.commons.beanutils.DynaBean">
				<tr>
					<td height="20" align="left" valign="top" class="theader"><bean:message
							bundle="coi" key="coiDisclosure.headerDisclosure" /> # <bean:write
							name="infoData" property="coiDisclosureNumber" /> - <bean:message
							bundle="coi" key="coiDisclosure.headerDiscldetailsFor" /> <bean:write
							name="infoData" property="entityName" /></td>
				</tr>
				<tr>
					<td align='right' class="copy" width="5%"><html:link
							action="/viewCOIDisclosureDetails.do" paramName="infoData"
							paramProperty="coiDisclosureNumber" paramId="disclosureNo">
							<u><bean:message bundle="coi"
									key="coiDisclosure.backToDisclosure" /></u>
						</html:link></td>
				</tr>
				<!-- Financial Entity Details - Start  -->
				<tr>
					<td height="50" align="left" valign="top"><table width="99%"
							border="0" align="center" cellpadding="0" cellspacing="0"
							class="tabtable">
							<tr>
								<td colspan="4" align="left" valign="top"><table
										width="100%" height="20" border="0" cellpadding="0"
										cellspacing="0" class="tableheader">
										<tr>
											<td><bean:message bundle="coi"
													key="coiDisclosure.headerFinEntityDetails" /></td>
										</tr>
									</table></td>
							</tr>
							<tr>
								<%-- Left column fields --%>
								<td width="80%" valign="top" class="copy" align="left">
									<table width="100%" border="0" cellspacing='3' cellpadding="3">



										<tr>
											<td nowrap class="copybold" width="10%" align="left"><bean:message
													bundle="coi" key="label.personName" /></td>
											<td width="6">
												<div align="left">:</div>
											</td>
											<td class="copy" nowrap colspan="2"><%=person.getFullName()%>
											</td>
											<td width="15">&nbsp;</td>
											<td nowrap class="copybold" width="10%" align="left"><bean:message
													bundle="coi" key="label.entityName" /></td>
											<td width="6">
												<div align="left">:</div>
											</td>
											<td class="copy" nowrap colspan="2"><bean:write
													name="infoData" property="entityName" /></td>
										</tr>
										<tr>
											<td nowrap class="copybold" align="left"><bean:message
													bundle="coi" key="label.conflictStatus" /></td>
											<td width="6">
												<div align="left">:</div>
											</td>
											<td class="copy" colspan="2"><bean:write name="infoData"
													property="coiStatus" /></td>
											<td width="15">&nbsp;</td>
											<td nowrap class="copybold" align="left"><bean:message
													bundle="coi" key="label.reviewedBy" /></td>
											<td width="6">
												<div align="left">:</div>
											</td>
											<td class="copy" colspan="2"><bean:write name="infoData"
													property="coiReviewer" /></td>
										</tr>
										<tr>
											<td nowrap class="copybold" width="10%" align="left"><bean:message
													bundle="coi" key="label.explanation" /></td>
											<td width="6">
												<div align="left">:</div>
											</td>
											<td class="copy" colspan="2"><bean:write name="infoData"
													property="description" /></td>
										</tr>
										<tr>
											<logic:present name="COIHeaderInfo" scope="session">

												<logic:iterate id="coiData" name="COIHeaderInfo"
													type="org.apache.commons.beanutils.DynaBean">
													<td nowrap class="copybold" width="10%" align="left">
														<bean:message bundle="coi" key="label.disclosureNo" />
													</td>
													<td width="6">
														<div align="left">:</div>
													</td>
													<td class="copy" colspan="2"><bean:write
															name="infoData" property="coiDisclosureNumber" /></td>
													<td width="15">&nbsp;</td>

													<td nowrap class="copybold" width="10%" align="left">
														<bean:message bundle="coi" key="label.title" />
													</td>
													<td width="6">
														<div align="left">:</div>
													</td>
													<td class="copy" colspan="2"><bean:write
															name="coiData" property="title" /></td>
												</logic:iterate>
											</logic:present>
										</tr>
									</table>
							<tr>
								<td colspan="3" nowrap class="copy" align="center" width="50%"><br>

									<%-- value --%></td>
							</tr>
							</td>
							</tr>
							<tr>
								<td nowrap class="copy">&nbsp;</td>
								<td colspan="2" nowrap class="copy">&nbsp;</td>
							</tr>

							</logic:iterate>
							</logic:present>



						</table></td>
				</tr>
				<!-- Financial Entity Details - End -->
				<tr>
					<td height='10'>&nbsp;</td>
				</tr>
				<!-- History Details - START -->


				<tr>
					<td align="left" valign="top"><table width="99%" border="0"
							align="center" cellpadding="0" cellspacing="0" class="tabtable">
							<tr>
								<td colspan="4" align="left" valign="top"><table
										width="100%" height="20" border="0" cellpadding="0"
										cellspacing="0" class="tableheader">
										<tr>
											<td><bean:message bundle="coi"
													key="coiDisclosure.headerHistory" /></td>
										</tr>
									</table></td>
							</tr>
							<tr align="center">
								<td colspan="3"><br> <!--<DIV STYLE="overflow: auto; width: 948px;  padding:0px; margin: 0px">-->

									<table width="98%" border="0" cellpadding="3" class="tabtable">
										<tr>
											<td width="20%" align="left" class="theader"><bean:message
													bundle="coi" key="label.conflictStatus" /></td>
											<td width="15%" align="left" class="theader"><bean:message
													bundle="coi" key="label.reviewedBy" /></td>
											<td width="20%" align="left" class="theader"><bean:message
													bundle="coi" key="label.lastUpdate" /></td>
											<td width="45%" align="left" class="theader"><bean:message
													bundle="coi" key="label.explanation" /></td>

										</tr>

										<logic:present name="DisclosureInfoHistory" scope="session">
											<%  int disclIndex = 0;
                                                     String strBgColor = "#DCE5F1";

                                                    %>
											<logic:iterate id="infoHistory" name="DisclosureInfoHistory"
												type="org.apache.commons.beanutils.DynaBean">

												<%                                  
                                                           if (disclIndex%2 == 0) {
                                                                strBgColor = "#D6DCE5"; 
                                                            }
                                                           else { 
                                                                strBgColor="#DCE5F1"; 
                                                             }
                                                        %>

												<tr bgcolor='<%=strBgColor%>' class="rowLine"
													onmouseover="className='rowHover rowLine'"
													onmouseout="className='rowLine'">
													<td class="copy" width="20%">
														<%     
                                                                    java.util.Map params = new java.util.HashMap();

                                                                    params.put("entityNumber", infoHistory.get("entityNumber"));
                                                                    params.put("seqNum", infoHistory.get("sequenceNum"));
                                                                    params.put("entitySeqNum", infoHistory.get("entitySequenceNumber"));
                                                                    pageContext.setAttribute("paramsMap", params);     

                                                                     %>

														<%--     <html:link  action="/viewFinEntity.do" name="paramsMap" scope="page" onclick="open_new_window(/viewFinEntity.do+paramsMap)">
                                                        <bean:write name="infoHistory" property="coiStatus"/>  </html:link> --%>
														<a
														href="JavaScript:open_new_window('<bean:write name='ctxtPath'/>/viewFinEntityDisplay.do?entityNumber=<bean:write name='infoHistory' property='entityNumber'/>&seqNum=<bean:write name='infoHistory' property='sequenceNum' />&entitySeqNum=<bean:write name='infoHistory' property='entitySequenceNumber' />&header=no' ,'EntityDetails');"
														method="POST"> <u> <bean:write name="infoHistory"
																	property="coiStatus" />
														</u>
													</a>
													</td>
													<td class="copy" width="15%"><bean:write
															name="infoHistory" property="coiReviewer" /></td>
													<td class="copy" width="20%"><coeusUtils:formatDate
															name="infoHistory" formatString="MM-dd-yyyy hh:mm a"
															property="updtimestamp" /> &nbsp; <bean:write
															name="infoHistory" property="upduser" /></td>
													<td class="copy" width="45%"><bean:write
															name="infoHistory" property="description" /></td>
												</tr>
												<% disclIndex++ ;%>
											</logic:iterate>
										</logic:present>
									</table> <!--</DIV>--></td>
							</tr>
							<tr>
								<td>&nbsp;</td>
							</tr>

						</table></td>
				</tr>
				<!-- History Details -End -->
				<tr>
					<td height='10'>&nbsp;</td>
				</tr>
	</table>
	<!-- New Template for cwViewFinEntity - End  -->

</body>
</html:html>