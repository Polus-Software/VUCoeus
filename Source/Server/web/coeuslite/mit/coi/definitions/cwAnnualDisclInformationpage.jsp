<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>


<tiles:definition id="cwAnnualDisclInformation"
	page="/coeuslite/mit/utils/layouts/cwInformationLayout.jsp"
	scope="request">

	<tiles:put name="header"
		value="/coeuslite/mit/coi/common/cwAnnualDisclHeader.jsp" />
	<tiles:put name="footer"
		value="/coeuslite/mit/irb/common/cwCopyright.jsp" />

</tiles:definition>

