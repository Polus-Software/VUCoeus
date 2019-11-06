<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>


<tiles:definition id="clAmendmentDefination"
	page="/coeuslite/utk/utils/layouts/clProposalInvCertifyLayout.jsp"
	scope="request">

	<tiles:put name="header"
		value="/coeuslite/mit/irb/common/cwProtocolHeader.jsp" />

	<tiles:put name="subheader"
		value="/coeuslite/mit/irb/common/cwGeneralInfoHeader.jsp" />

	<tiles:put name="footer"
		value="/coeuslite/mit/irb/common/cwCopyright.jsp" />

</tiles:definition>