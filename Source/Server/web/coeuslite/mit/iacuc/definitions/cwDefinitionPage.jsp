<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<html>
<head>
<link href="/mit/utils/css/ipf.css" rel="stylesheet" type="text/css">
</head>

<tiles:definition id="cwDefinition"
	page="/coeuslite/mit/utils/layouts/classicLayout.jsp" scope="request">

	<tiles:put name="header"
		value="/coeuslite/mit/iacuc/common/cwIRBHeader.jsp" />

	<tiles:put name="menu" value="/coeuslite/mit/iacuc/common/cwMenu.jsp" />

	<tiles:put name="footer"
		value="/coeuslite/mit/iacuc/common/cwCopyright.jsp" />

</tiles:definition>
</html>
