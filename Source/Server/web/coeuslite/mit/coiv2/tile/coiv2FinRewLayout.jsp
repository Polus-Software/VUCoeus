<%-- 
    Document   : coiv2FinRewLayout
    Created on : Jan 7, 2011, 3:59:46 PM
    Author     : jaishab
--%>

<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<html>
<head>
<link href="/mit/utils/css/ipf.css" rel="stylesheet" type="text/css">
</head>

<tiles:definition id="coiv2FinRewDisclosure"
	page="/coeuslite/mit/utils/layouts/classicLayout.jsp" scope="request">
	<tiles:put name="header"
		value="/coeuslite/mit/coiv2/jsp/coiMainHeaderCoiv2.jsp" />
	<tiles:put name="menu"
		value="/coeuslite/mit/coiv2/jsp/coiV2HomeMenu.jsp" />
	<tiles:put name="footer"
		value="/coeuslite/mit/irb/common/cwCopyright.jsp" />
</tiles:definition>
</html>
