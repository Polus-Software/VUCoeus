<? xml version="1.0" ?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="edu.mit.coeus.bean.PersonInfoBean"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="user" scope="session"
	class="edu.mit.coeus.bean.UserInfoBean" />
<jsp:useBean id="person" scope="session"
	class="edu.mit.coeus.bean.PersonInfoBean" />
<jsp:useBean id="loggedinpersonid" scope="session"
	class="java.lang.String" />

<html>
<head>
<meta http-equiv="Content-Language" content="en-us">
<title>Conflict of Interest Data</title>

<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<style type="text/css">
<!--
body {
	margin-left: 3px;
}
-->
</style>
</head>
<body>

	<!-- New Template for cwCOIMenu Start  -->

	<table width="980" height="100%" border="0" cellpadding="0"
		cellspacing="0" class="table">
		<tr>
			<td height="20" align="left" valign="top" class="theader">
				&nbsp; <bean:message bundle="coi" key="coiMenu.coi" /> - <%=person.getFullName()%>
			</td>
		</tr>
		<tr>
			<td height="250" align="left" valign="top">
				<table width="99%" border="0" align="center" cellpadding="2"
					cellspacing="0" class="tabtable">
					<tr>
						<td width="100%" class="copy"><bean:message bundle="coi"
								key="coiMenu.coiInfo" /></td>
					</tr>
					<tr>
						<td width="100%">
							<table border="0" width="100%">
								<tr>
									<td width="650"><br>
									<table border="0" width="800" cellspacing="0" cellpadding="0">
											<tr>
												<td width="85%" colspan="2" class="copybold"><bean:message
														bundle="coi" key="coiMenu.toCreateCOI" /></td>
											</tr>
											<tr>
												<td>&nbsp;</td>
											</tr>
											<tr>
												<td width="50%" class="copy">
													<ul>
														<li><bean:message bundle="coi" key="coiMenu.step1" />
															<html:link action="/getAddFinEnt.do">
																<u> <bean:message bundle="coi"
																		key="coiMenu.addModifyFinEnt" /></u>
															</html:link></li>
													</ul>
												</td>

												<td width="100%" class="copy" align="right">
													<ul>
														<a href> <bean:message bundle="coi"
																key="coiMenu.reportableFinEnt" /></a>
													</ul>
												</td>
											</tr>
											<tr>
												<td width="100%" colspan="2" class="copy">
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													&nbsp;&nbsp;&nbsp;&nbsp;
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <bean:message
														bundle="coi" key="coiMenu.note" />

													<p>
												</td>
												<td>&nbsp;</td>
											</tr>

											<tr>
												<td width="50%" class="copy">
													<ul>
														<li><bean:message bundle="coi" key="coiMenu.step2" />
															<html:link action="/getAddDisclosure.do">
																<u> <bean:message bundle="coi"
																		key="coiMenu.addCOIDiscl" /></u>
															</html:link> <bean:message bundle="coi" key="coiMenu.proposal" /></li>
													</ul>
												</td>
												<td width="50%">&nbsp;</td>
											</tr>
											<tr rowspan="5">
												<td>&nbsp;</td>&nbsp;
											</tr>
											<!-- Show annual disclosure link based on boolean attribute of PersonInfoBean in session, BUT
                            only if user has not done a select person.  If user has done select person, never show ann discl link -->
											<%
                            
                            if(loggedinpersonid != null && person.getPersonID() != null
                                        && person.getPersonID().equals(loggedinpersonid) && person.getPendingAnnDisclosure() ){
                            %>

											<tr>
												<td width="50%" class="copy"><bean:message bundle="coi"
														key="coiMenu.annualDisclDue" /> <html:link
														action="/getAnnDiscPendingFEs.do">
														<u><bean:message bundle="coi"
																key="coiMenu.completAnnDiscl" /></u>
													</html:link></td>
												<td width="50%">&nbsp;</td>

											</tr>
											<%
                                }
                        %>
										</table></td>
								</tr>
							</table>
						</td>
					</tr>

				</table>
			</td>
		</tr>
		<tr>
			<td align="left" valign="top">&nbsp;</td>
		</tr>
	</table>
	<!-- New Template for cwCOIMenu End -->
</body>
</html>
