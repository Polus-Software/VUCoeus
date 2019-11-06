<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>


<tiles:definition id="clSubcontractInvoiceInformation"
	page="/coeuslite/mit/subcontract/utils/layouts/clSubcontractInvoiceLayout.jsp"
	scope="request">

	<tiles:put name="header"
		value="/coeuslite/utk/propdev/common/clProposalHeader.jsp" />

	<tiles:put name="menu"
		value="/coeuslite/mit/subcontract/common/clInvoiceMenu.jsp" />

	<tiles:put name="footer"
		value="/coeuslite/mit/irb/common/cwCopyright.jsp" />

</tiles:definition>
