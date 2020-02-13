
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<html>
<head>
<link href="/mit/utils/css/ipf.css" rel="stylesheet" type="text/css">
</head>

<tiles:definition id="cwAnnualDiscDefinition"
	page="/coeuslite/mit/utils/layouts/classicLayout.jsp" scope="request">

	<tiles:put name="header"
		value="/coeuslite/mit/irb/common/cwCOIHeader.jsp" />

	<tiles:put name="menu"
		value="/coeuslite/mit/coi/common/cwAnnualDisclMenu.jsp" />

	<tiles:put name="footer"
		value="/coeuslite/mit/irb/common/cwCopyright.jsp" />

</tiles:definition>
</html>
