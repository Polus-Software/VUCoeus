<%@page contentType="text/html"
	import="edu.mit.coeus.utils.CoeusProperties,edu.mit.coeus.utils.CoeusPropertyKeys"%>
<%@page pageEncoding="UTF-8" import="edu.mit.coeus.bean.*"%>
<%@page errorPage="ErrorPage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="person" scope="session"
	class="edu.mit.coeus.bean.PersonInfoBean" />
<html>
<head>
<%String path = request.getContextPath();%>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>COI</title>

<link href="<%=path%>/coeuslite/dartmouth/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
</head>
<body>
	<table width="100%" cellspacing="0" border="0">
		<tr align="left">
			<td class='copy'><font color="red"> <logic:messagesPresent
						message="true">
						<html:messages id="msg" bundle="coi" message="true"
							property="answer">
							<bean:write name="msg" />
						</html:messages>
						<html:messages id="msg2" bundle="coi" message="true"
							property="duplicateSubmission">
							<bean:write name="msg2" />
						</html:messages>
						<html:messages id="msg3" bundle="coi" message="true"
							property="nodisclosure">
							<bean:write name="msg3" />
						</html:messages>
						<html:messages id="msg4" bundle="coi" message="true"
							property="invalidDisclosure">
							<bean:write name="msg4" />
						</html:messages>
						<html:messages id="msg5" bundle="coi" message="true"
							property="invalidEntity">
							<bean:write name="msg5" />
						</html:messages>
						<html:messages id="msg6" bundle="coi" message="true"
							property="noright">
							<bean:write name="msg6" />
						</html:messages>
						<html:messages id="msg7" bundle="coi" message="true"
							property="norightToViewDiscl">
							<bean:write name="msg7" />
						</html:messages>
						<html:messages id="msg8" bundle="coi" message="true"
							property="reduntantEntity">
							<bean:write name="msg8" />
						</html:messages>
						<html:messages id="msg9" bundle="coi" message="true"
							property="noOSPRight">
							<bean:write name="msg9" />
						</html:messages>
						<html:messages id="msg10" bundle="coi" message="true"
							property="noOSPViewRight">
							<bean:write name="msg10" />
						</html:messages>
						<html:messages id="msg11" bundle="coi" message="true"
							property="noQuestionnaire">
							<bean:write name="msg11" />
						</html:messages>
					</logic:messagesPresent> <logic:present name="Message">
						<%
                                        String warning = request.getAttribute("Message").toString();
                                    %>

						<%=warning%>
					</logic:present>
			</font></td>
		</tr>
	</table>
</body>
</html>