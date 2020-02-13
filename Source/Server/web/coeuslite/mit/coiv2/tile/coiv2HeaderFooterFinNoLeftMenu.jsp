<%-- 
    Document   : coiv2HeaderFooterFinNoLeftMenu
    Created on : Sep 16, 2010, 4:16:28 PM
    Author     : New
--%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<html>
<head>
<link href="/mit/utils/css/ipf.css" rel="stylesheet" type="text/css">
</head>

<tiles:definition id="coiv2FinViewDisclosure"
	page="/coeuslite/mit/utils/layouts/cwInformationLayout.jsp"
	scope="request">
	<tiles:put name="header"
		value="/coeuslite/mit/coiv2/jsp/coiMainHeaderCoiv2.jsp" />
	<tiles:put name="menu"
		value="/coeuslite/mit/coiv2/jsp/coiV2HomeMenu.jsp" />
	<tiles:put name="footer"
		value="/coeuslite/mit/irb/common/cwCopyright.jsp" />
</tiles:definition>
</html>
