<%@ page import="org.apache.struts.validator.DynaValidatorForm"%>

<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@page errorPage="ErrorPage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="PendingDisclInfo" scope="session"
	class="java.util.Vector" />

<jsp:useBean id="person" scope="session"
	class="edu.mit.coeus.bean.PersonInfoBean" />


<html:html>
<head>
<title>Coeus Web</title>
<html:base />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<script language="javascript" type="text/JavaScript"
	src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/sorttable.js"></script>
</head>
<body>

	<%--  <html:form action="/keyStudyPerson"  method="post" onsubmit="return validateForm(this)"> --%>
	<a name="top"></a>
	<%--  ************  START OF BODY TABLE  ************************--%>

	<table width="980" height="100%" border="0" cellpadding="0"
		cellspacing="0" class="table">

		<tr>
			<td height="20" align="left" valign="top" class="tableheader"><bean:message
					bundle="coi" key="coiDisclosure.coiDisclHeader" /></td>
		</tr>

		<tr>
			<td height="20" class="copy" valign="top">&nbsp;&nbsp; <bean:message
					bundle="coi" key="coiDisclosure.coiDiscInfo" />
			</td>
		</tr>



		<!-- COI Disclosure - Start  -->
		<tr>
			<td align="center" valign="top">


				<table width="98%" border="0" cellpadding="2" cellspacing="0"
					id="t1" class="tabtable">
					<tr class="tableheader">
						<td width="20%" align="left" class="theader"><bean:message
								bundle="coi" key="label.principalInvestigator" /></td>
						<!--JIRA COEUSQA-2316 - START-->
						<td width="10%" align="left" class="theader"><bean:message
								bundle="coi" key="label.proposalKey" /></td>
						<!--JIRA COEUSQA-2316 - END-->
						<td width="10%" align="left" class="theader"><bean:message
								bundle="coi" key="label.disclosureNumber" /></td>
						<td width="10%" align="left" class="theader"><bean:message
								bundle="coi" key="label.sponsor" /></td>
						<td width="30%" align="left" class="theader"><bean:message
								bundle="coi" key="label.title" /></td>
						<td width="20%" align="left" class="theader"><bean:message
								bundle="coi" key="label.lastUpdated" /></td>

					</tr>


					<logic:present name="PendingDisclInfo" scope="session">
						<%  int disclIndex = 0;
                                                     String strBgColor = "#DCE5F1";

                                                    %>
						<logic:iterate id="discInfo" name="PendingDisclInfo"
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
								<td width="20%" class="copy" valign=top wrap colspan="0">
									<coeusUtils:formatOutput name="discInfo" property="fullName" />
								</td>
								<!--JIRA COEUSQA-2316 - START-->
								<td width="10%" class="copy" valign=top wrap colspan="0">
									<coeusUtils:formatOutput name="discInfo"
										property="moduleItemKey" />
								</td>
								<!--JIRA COEUSQA-2316 - END-->
								<td width="10%" class="copy" valign=top wrap colspan="0"><coeusUtils:formatOutput
										name="discInfo" property="disclNo" /></td>
								<td width="10%" class="copy" valign=top wrap colspan="0"><coeusUtils:formatOutput
										name="discInfo" property="sponsorName" /></td>
								<td width="30%" class="copy" valign=top wrap colspan="0"><coeusUtils:formatOutput
										name="discInfo" property="title" /></td>
								<td width="20%" class="copy" valign=top wrap colspan="0"><coeusUtils:formatDate
										name="discInfo" formatString="MM-dd-yyyy hh:mm a"
										property="updtimestamp" /> &nbsp; <bean:write name="discInfo"
										property="upduser" /></td>
							</tr>
							<% disclIndex++ ;%>
						</logic:iterate>
					</logic:present>

				</table>

			</td>
		</tr>
		<!-- COI Disclosure - End  -->
		<tr>
			<td height='10'>&nbsp;</td>
		</tr>
	</table>

</body>
</html:html>
