<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<html>
<head>
<link href="/mit/utils/css/ipf.css" rel="stylesheet" type="text/css">
</head>

<tiles:definition id="cwArraInformation"
	page="/coeuslite/mit/utils/layouts/cwInformationLayout.jsp"
	scope="request">

	<tiles:put name="header"
		value="/coeuslite/mit/arra/common/clArraMenuHeader.jsp" />
	<tiles:put name="footer"
		value="/coeuslite/mit/irb/common/cwCopyright.jsp" />

</tiles:definition>
</html>
