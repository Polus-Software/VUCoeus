<%-- 
    Document   : showuserrights
    Created on : Mar 26, 2010, 3:54:42 PM
    Author     : Mr
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/privilege.tld" prefix="priv"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@page import="edu.mit.coeuslite.coiv2.utilities.CoiConstants"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JSP Page</title>
</head>
<body>

	<logic:present name="<%=CoiConstants.IS_COI_ADMIN%>">yes this is Coi Admin</logic:present>
	<logic:present name="<%=CoiConstants.IS_COI_VIEWER%>">Coi Viewer</logic:present>

	<logic:equal name="rights" property="disclosure" value="1">Disclosure View</logic:equal>
	<logic:equal name="rights" property="disclosure" value="2">Diclosure Maintain</logic:equal>
	<logic:equal name="rights" property="disclosure" value="0">No Disclosure</logic:equal>
	<br>

	<logic:equal name="rights" property="note" value="1">Note view</logic:equal>
	<logic:equal name="rights" property="note" value="2">Note maintain</logic:equal>
	<logic:equal name="rights" property="note" value="0">no note</logic:equal>
	<br>


	<logic:equal name="rights" property="attachment" value="1">Attachment view</logic:equal>
	<logic:equal name="rights" property="attachment" value="2">attachment maintain</logic:equal>
	<logic:equal name="rights" property="attachment" value="0">no attachment</logic:equal>
	<br>
	<logic:present name="coiroleassigned">
		<logic:equal name="coiroleassigned" value="0">This user has no coi role assigned</logic:equal>
		<logic:equal name="coiroleassigned" value="1">This user coi role assigned</logic:equal>
	</logic:present>


	<br>





</body>
</html>
