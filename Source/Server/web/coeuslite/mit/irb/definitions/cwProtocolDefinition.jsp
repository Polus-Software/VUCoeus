<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<html>
<head>
<link href="/mit/utils/css/ipf.css" rel="stylesheet" type="text/css">
</head>

<tiles:definition id="cwProtocolDefinition"
	page="/coeuslite/mit/utils/layouts/cwGeneralInfoLayout.jsp"
	scope="request">

	<tiles:put name="header"
		value="/coeuslite/mit/irb/common/cwProtocolHeader.jsp" />

	<tiles:put name="subheader" value="" />

	<tiles:put name="menu" value="/coeuslite/mit/irb/common/cwMenu.jsp" />

	<tiles:put name="footer"
		value="/coeuslite/mit/irb/common/cwCopyright.jsp" />

</tiles:definition>
</html>
