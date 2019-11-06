<%@ page contentType="text/html;charset=UTF-8" language="java"
	import="java.util.Hashtable,java.util.Vector,edu.mit.coeuslite.iacuc.action.TreeView;"%>
<%--<%@ page import="edu.mitweb.coeus.irb.action.TreeView;" %>  --%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="personRespTrainingDetails" scope="session"
	class="java.util.Vector" />
<html:html locale="true">
<head>
<title>Training Details</title>
<html:base />
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/ipf.css"
	rel="stylesheet" type="text/css">
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
</head>
<body bgcolor="#376DAA">
	<%
        String personName = "";
        personName = (String) request.getAttribute("trainingDetailsPerson");                
        %>
	<html:form action="/getPersonTrainingDetails.do" method="post">
		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			class="tabtable">
			<tr>
				<td class="theader">Training Info for <%=personName%></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table width="100%" border="0" cellpadding="2" cellspacing="2"
						class="tabtable">
						<tr>
							<!-- COEUSQA:3537 - Coeus IACUC Training Revisions - Start -->
							<!--<td width= "32%" class="theader">Training</td> -->
							<td width="14%" class="theader">Species</td>
							<td width="14%" class="theader">Procedure Category</td>
							<td width="14%" class="theader">Procedure</td>
							<td width="10%" class="theader">Date Completed</td>
							<!--<td width= "10%" class="theader">Date Requested</td>
                                <td width= "10%" class="theader">Date Submitted</td>
                                <td width= "10%" class="theader">Date Acknowledged</td>
                                <td width= "10%" class="theader">Follow Up Date</td>-->
							<!-- COEUSQA:3537 - End -->
						</tr>
						<% int indexColor = 1;%>
						<logic:present name="personRespTrainingDetails">
							<logic:iterate id="data" name="personRespTrainingDetails"
								type="org.apache.struts.validator.DynaValidatorForm">

								<%  String strBgColor = "#D6DCE5";
                                    if (indexColor%2 == 0) {
                                        strBgColor = "#DCE5F1"; }
                                    %>
								<tr bgcolor="<%=strBgColor%>"
									onmouseover="className='TableItemOn'"
									onmouseout="className='TableItemOff'">
									<!-- COEUSQA:3537 - Coeus IACUC Training Revisions - Start -->
									<!--<td width= "32%" class="copy"><bean:write name="data" property="trainingDesc"/></td>-->
									<td width="14%" class="copy"><bean:write name="data"
											property="speciesName" /></td>
									<td width="14%" class="copy"><bean:write name="data"
											property="procedureCategory" /></td>
									<td width="14%" class="copy"><bean:write name="data"
											property="procedureDesc" /></td>
									<!--<td width= "10%" class="copy"><coeusUtils:formatDate name="data" property="dateRequested" /></td>-->
									<td width="10%" class="copy"><coeusUtils:formatDate
											name="data" property="dateSubmitted" /></td>
									<!-- <td width= "10%" class="copy"><coeusUtils:formatDate name="data" property="dateAcknowledged" /></td>
                                        <td width= "10%" class="copy"><coeusUtils:formatDate name="data" property="followUpDate" /></td> -->
								</tr>
								<% indexColor++;%>
							</logic:iterate>
						</logic:present>
					</table>
				</td>
			</tr>
		</table>
	</html:form>
</body>
</html:html>