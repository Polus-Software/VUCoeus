<%--
    Document   : coiv2DisclMainInformation
    Created on : Mar 20, 2010, 10:35:05 AM
    Author     : Sony
--%>

<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@page
	import="java.util.HashMap,edu.mit.coeuslite.coiv2.utilities.CoiConstants"%>

<%
            boolean disable = false;            

%>

<html>
<head>
<link href="/coeuslite/mit/utils/css/ipf.css" rel="stylesheet"
	type="text/css">
</head>

<tiles:definition id="headerfooterbean"
	page="/coeuslite/mit/coiv2/admintiles/BaseLayout.jsp" scope="request">
	<tiles:put name="header"
		value="/coeuslite/mit/coiv2/jsp/coiMainHeaderCoiv2.jsp" />
	<tiles:put name="footer"
		value="/coeuslite/mit/irb/common/cwCopyright.jsp" />
	<tiles:put name="subheader"
		value="/coeuslite/mit/coiv2/jsp/showcoiv2submenu.jsp" />
	<tiles:put name="menu"
		value="/coeuslite/mit/coiv2/jsp/coiV2HomeMenu.jsp" />
	<tiles:put name="body1"
		value="/coeuslite/mit/coiv2/tile/bodyHeader.jsp" />
</tiles:definition>
</html>