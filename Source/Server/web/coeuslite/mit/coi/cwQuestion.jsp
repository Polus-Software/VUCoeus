<%@ page import="org.apache.struts.validator.DynaValidatorForm"%>

<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@page errorPage="ErrorPage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>

<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="questionDetails" scope="request"
	class="java.util.Vector" />
<jsp:useBean id="questionExplanation" scope="request"
	class="java.lang.String" />
<jsp:useBean id="questionPolicy" scope="request"
	class="java.lang.String" />
<jsp:useBean id="questionRegulation" scope="request"
	class="java.lang.String" />

<html:html>
<head>
<link rel=stylesheet
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/ipf.css"
	type="text/css">
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<title>CoeusLite Web Page</title>
</head>
<body>
	<a name="top"></a>
	<%--  ************  START OF BODY TABLE  ************************--%>
	<table width="790" height="100%" border="0" cellpadding="0"
		cellspacing="0" class="table">
		<tr>
			<td height='10'>&nbsp;</td>
		</tr>
		<!-- CertificationQuestion - Start  -->
		<tr>
			<td height="50" align="left" valign="top"><table width="99%"
					border="0" align="center" cellpadding="0" cellspacing="0"
					class="tabtable">
					<tr>
						<td colspan="4" align="left" valign="top"><table width="100%"
								height="20" border="0" cellpadding="0" cellspacing="0"
								class="tableheader">
								<tr>
									<td><bean:message bundle="coi" key="question.certQuestion" />
									</td>
								</tr>
							</table></td>
					</tr>
					<logic:present name="questionDetails" scope="request">
						<logic:iterate name="questionDetails" id="questionData"
							type="org.apache.commons.beanutils.DynaBean">

							<tr>
								<td class="copy" width='5%' colspan="0" height="30">
									<div align="left">
										<bean:write name="questionData" property="questionId" />
										:
									</div>
								</td>

								<td height="30" width='90%' class="copy">
									<div align="left">
										<bean:write name="questionData" property="description" />
									</div>
								</td>
							</tr>

						</logic:iterate>
					</logic:present>

					<tr>
						<td>&nbsp;</td>
					</tr>

				</table></td>
		</tr>
		<!-- CertificationQuestion - End  -->
		<tr>
			<td height='10'>&nbsp;</td>
		</tr>
		<!-- Explanation - Start -->
		<tr>
			<td height="52" align="left" valign="top"><table width="99%"
					border="0" align="center" cellpadding="0" cellspacing="0"
					class="tabtable">
					<tr>
						<td colspan="4" align="left" valign="top"><table width="100%"
								height="20" border="0" cellpadding="0" cellspacing="0"
								class="tableheader">
								<tr>
									<td><bean:message bundle="coi" key="question.explanation" />
									</td>
								</tr>
							</table></td>
					</tr>
					<tr>
						<td class="copy" colspan="0"><%=request.getAttribute("questionExplanation")%>
							<!--bean:write name="questionData" property="explanation" /--></td>
					</tr>

					<tr>
						<td>&nbsp;</td>
					</tr>

				</table></td>
		</tr>
		<!-- Explanation -End -->

		<!--Policy -Start -->

		<tr>
			<td height="30" align="left" valign="top"><br>
				<table width="99%" border="0" align="center" cellpadding="0"
					cellspacing="0" class="tabtable">
					<tr>
						<td align="left" valign="top"><table width="100%" height="20"
								border="0" cellpadding="0" cellspacing="0" class="tableheader">
								<tr>
									<td><bean:message bundle="coi" key="question.policy" /></td>
								</tr>
							</table></td>
					</tr>
					<tr>

						<td class="copy" colspan="0"><%=request.getAttribute("questionPolicy")%>
							<!--bean:write name="questionData" property="explanation" /--></td>
					</tr>
					<tr>
						<td height='10'>&nbsp;</td>
					</tr>
				</table></td>
		</tr>
		<!--Policy -End -->
		<!--Regulation -Start -->

		<tr>
			<td height="30" align="left" valign="top"><br>
				<table width="99%" border="0" align="center" cellpadding="0"
					cellspacing="0" class="tabtable">
					<tr>
						<td align="left" valign="top"><table width="100%" height="20"
								border="0" cellpadding="0" cellspacing="0" class="tableheader">
								<tr>
									<td><bean:message bundle="coi" key="question.regulation" />
									</td>
								</tr>
							</table></td>
					</tr>
					<tr>

						<td class="copy" colspan="0"><%=request.getAttribute("questionRegulation")%>
							<!--bean:write name="questionData" property="explanation" /--></td>
					</tr>
					<tr>
						<td height='10'>&nbsp;</td>
					</tr>
				</table></td>
		</tr>
		<!--Regulation -End -->
		<tr>
			<td align="left" valign=middle>&nbsp;&nbsp; <%--<html:button  property="Close" styleClass="clbutton" value="Close" onclick="JavaScript:window.close();"  />--%>
				<html:link href="javascript:window.close();" styleClass="copybold">
					<bean:message bundle="budget"
						key="budgetLineItemDetailsButton.close" />
				</html:link>

			</td>
		</tr>

	</table>
</body>
</html:html>
