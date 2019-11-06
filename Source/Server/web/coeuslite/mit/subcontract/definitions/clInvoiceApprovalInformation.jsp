<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>


<tiles:definition id="clInvoiceApprovalInformation"
	page="/coeuslite/mit/subcontract/utils/layouts/clInvoiceApproveRejectLayout.jsp"
	scope="request">

	<tiles:put name="header"
		value="/coeuslite/utk/propdev/common/clProposalHeader.jsp" />

	<tiles:put name="footer"
		value="/coeuslite/mit/irb/common/cwCopyright.jsp" />

</tiles:definition>
