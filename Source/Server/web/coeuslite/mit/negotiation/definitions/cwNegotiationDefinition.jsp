<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>


<tiles:definition id="cwNegotiationDefinition"
	page="/coeuslite/mit/utils/layouts/cwGeneralInfoLayout.jsp"
	scope="request">

	<tiles:put name="header"
		value="/coeuslite/mit/negotiation/common/cwNegotiationHeader.jsp" />

	<tiles:put name="subheader"
		value="/coeuslite/mit/negotiation/common/cwNegotiationDetailHeader.jsp" />

	<tiles:put name="menu"
		value="/coeuslite/mit/negotiation/common/cwNegotiationMenu.jsp" />

	<tiles:put name="footer"
		value="/coeuslite/mit/irb/common/cwCopyright.jsp" />

</tiles:definition>

