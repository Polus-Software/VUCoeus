<%-- 
    Document   : coiV2DiscHomeInformation
    Created on : Apr 3, 2012, 2:01:04 PM
    Author     : UnNamed
--%>

<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>


<html>
<head>
<link href="/mit/utils/css/ipf.css" rel="stylesheet" type="text/css">
</head>

<tiles:definition id="coiv2MainDisclosure"
	page="/coeuslite/mit/coiv2/tile/coiV2HomeLayout.jsp" scope="request">

	<tiles:put name="header"
		value="/coeuslite/mit/coiv2/jsp/coiMainHeaderCoiv2.jsp" />

	<tiles:put name="subheader"
		value="/coeuslite/mit/coiv2/jsp/showcoiv2submenu.jsp" />

	<tiles:put name="menu"
		value="/coeuslite/mit/coiv2/jsp/coiV2HomeMenu.jsp" />

	<tiles:put name="footer"
		value="/coeuslite/mit/irb/common/cwCopyright.jsp" />

	<tiles:put name="body1"
		value="/coeuslite/mit/coiv2/tile/bodyHeader.jsp" />
</tiles:definition>
</html>