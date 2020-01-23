<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<html>
<head>
<link href="/mit/utils/css/ipf.css" rel="stylesheet" type="text/css">
</head>

<tiles:definition id="cwScheduleInformation"
	page="/coeuslite/mit/utils/layouts/cwScheduleInfoLayout.jsp"
	scope="request">

	<tiles:put name="header"
		value="/coeuslite/mit/iacuc/common/cwProtocolHeader.jsp" />
	<tiles:put name="subheader"
		value="/coeuslite/mit/iacuc/common/cwScheduleGeneralHeader.jsp" />
	<tiles:put name="menu"
		value="/coeuslite/mit/iacuc/common/cwScheduleMenu.jsp" />

	<tiles:put name="footer"
		value="/coeuslite/mit/iacuc/common/cwCopyright.jsp" />

</tiles:definition>
</html>
