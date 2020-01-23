<%@ page contentType="text/html"%>
<%@ page
	import="java.util.Vector,edu.mit.coeus.utils.UtilFactory,edu.mit.coeus.coi.bean.*"%>
<%@page errorPage="ErrorPage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="person" class="edu.mit.coeus.bean.PersonInfoBean"
	scope="session" />
<html:html>
<head>
<%String path = request.getContextPath();%>
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<title>Coeus Web</title>
<html:base />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>
<body>
	<a name="top"></a>
	<%--  ************  START OF BODY TABLE  ************************--%>

	<table width="980" border="0" cellpadding="0" cellspacing="0"
		class="table">
		<logic:present name="person" scope="session">
			<tr>
				<td height="10" align="left" valign="top" class="theader"><bean:message
						bundle="coi" key="label.selectedPerson" /> <%=UtilFactory.dispEmptyStr(person.getFullName())%></td>
			</tr>
			<tr>
				<td height="20" align="left" valign="top" class="copybold">&nbsp;</td>
			</tr>
			<!-- EntityDetails - Start  -->
			<tr>
				<td height="119" align="left" valign="top"><table width="99%"
						border="0" align="center" cellpadding="0" cellspacing="0"
						class="tabtable">
						<tr>
							<td colspan="4" align="left" valign="top">
								<table width="100%" height="20" border="0" cellpadding="0"
									cellspacing="0" class="tableheader">
									<tr>
										<td><bean:message bundle="coi" key="label.personDetails" />
										</td>
									</tr>
								</table>
							</td>
						</tr>

						<tr>
							<td width="70%" valign="top" class="copy" align="left">
								<table width="80%" border="0" cellpadding="4">
									<tr>
										<td nowrap class="copy" width="15%" align="left"><bean:message
												bundle="coi" key="label.fullName" /></td>
										<td width="6">
											<div align="left">:</div>
										</td>
										<td class="copybold" nowrap colspan="2"><%=UtilFactory.dispEmptyStr(person.getFullName())%>
										</td>

										<td nowrap class="copy" width="15%" align="left"><bean:message
												bundle="coi" key="label.priorName" /></td>
										<td width="6">
											<div align="left">:</div>
										</td>
										<td class="copy" nowrap colspan="2"><b> <%=UtilFactory.dispEmptyStr(person.getPriorName())%>
										</b></td>
									</tr>
									<tr>
										<td nowrap class="copy" width="15%" align="left"><bean:message
												bundle="coi" key="label.userName" /></td>
										<td width="6">
											<div align="left">:</div>
										</td>
										<td class="copybold" nowrap colspan="2"><%=UtilFactory.dispEmptyStr(person.getUserName())%>
										</td>

										<td nowrap class="copy" width="15%" align="left"><bean:message
												bundle="coi" key="label.homeUnit" /></td>
										<td width="6">
											<div align="left">:</div>
										</td>
										<td class="copy" nowrap colspan="2"><b> <%=UtilFactory.dispEmptyStr(person.getHomeUnit())%>
										</b></td>
									</tr>
									<tr>
										<td nowrap class="copy" width="15%" align="left"><bean:message
												bundle="coi" key="label.email" /></td>
										<td width="6">
											<div align="left">:</div>
										</td>
										<td class="copybold" nowrap colspan="2"><%=UtilFactory.dispEmptyStr(person.getEmail())%>
										</td>

										<td nowrap class="copy" width="15%" align="left"><bean:message
												bundle="coi" key="label.dirTitle" /></td>
										<td width="6">
											<div align="left">:</div>
										</td>
										<td class="copy" nowrap colspan="2"><b> <%=UtilFactory.dispEmptyStr(person.getDirTitle())%>
										</b></td>
									</tr>
									<tr>
										<td nowrap class="copy" width="15%" align="left"><bean:message
												bundle="coi" key="label.offLocation" /></td>
										<td width="6">
											<div align="left">:</div>
										</td>
										<td class="copybold" nowrap colspan="2"><%=UtilFactory.dispEmptyStr(person.getOffLocation())%>
										</td>

										<td nowrap class="copy" width="15%" align="left"><bean:message
												bundle="coi" key="label.offPhone" /></td>
										<td width="6">
											<div align="left">:</div>
										</td>
										<td class="copy" nowrap colspan="2"><b> <%=UtilFactory.dispEmptyStr(person.getOffPhone())%>
										</b></td>
									</tr>
									<tr>
										<td nowrap class="copy" width="15%" align="left"><bean:message
												bundle="coi" key="label.secOffLoc" /></td>
										<td width="6">
											<div align="left">:</div>
										</td>
										<td width='20%' class="copybold" nowrap colspan="2"><%=UtilFactory.dispEmptyStr(person.getSecOffLoc())%>
										</td>

										<td nowrap class="copy" width="15%" align="left"><bean:message
												bundle="coi" key="label.secOffPhone" /></td>
										<td width="6">
											<div align="left">:</div>
										</td>
										<td class="copy" nowrap colspan="2"><b> <%=UtilFactory.dispEmptyStr(person.getSecOffPhone())%>
										</b></td>

									</tr>
									<tr>
										<td nowrap class="copy">&nbsp;</td>
										<td colspan="2" nowrap class="copy">&nbsp;</td>
									</tr>

								</table>
							</td>
						</tr>

						</logic:present>





					</table></td>
			</tr>
			<tr valign="top">
				<td class="copy"><br>&nbsp;&nbsp;<input type="button"
					value="Review Financial Entities"
					onclick="javascript:window.location='<%=path%>/getReviewFinEnt.do'"
					style="width: 300px"></td>
			</tr>
			<tr valign="top">
				<td class="copy"><br>&nbsp;&nbsp;<input type="button"
					value="Review Current Disclosures"
					onclick="javascript:window.location='<%=path%>/getReviewDiscl.do'"
					style="width: 300px"></td>
			</tr>

			<tr>
				<td height='10'>&nbsp;</td>
			</tr>
	</table>

</body>
</html:html>
