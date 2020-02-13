<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>

<html:html locale="true">
<html:form action="/protocolList.do" method="post">
	<head>
<title>Protocol Details</title>
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<!--<link href="css/ipf.css" rel="stylesheet" type="text/css">
        <style type="text/css">
        </style>-->
<script language="JavaScript" type="text/JavaScript">
</script>
	</head>
	<body>
		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			class="table">

			<tr>
				<td>
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td class="copybold"><span class="copyboldred">&gt;&gt;</span>
								<bean:message key="label.ListofAllProtocols" /> <%--List of All Protocols--%>
								</span></td>
						</tr>
						</td>
					</table>
			</tr>
			<tr>
				<td>
					<table width="100%" border="0" cellpadding="0" class="tabtable">
						<tr>
							<td width="15%" align="left" class="theader"><bean:message
									key="specialReview.spRevProtocolNumber=Protocol Number" /> <%--Protocol Number--%></td>
							<td width="30%" align="left" class="theader"><bean:message
									key="irbGeneralInfoForm.title" /> <%--Title--%></td>
							<td width="15%" align="left" class="theader"><bean:message
									key="specialReviewLabel.ApprDate" /> <%--Approval Date--%></td>
							<td width="15%" align="left" class="theader"><bean:message
									key="label.ExpirationDate" /> <%--Expiration Date--%></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>

	</body>
</html:form>
</html:html>