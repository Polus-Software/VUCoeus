<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@include
	file="/coeuslite/mit/subcontract/definitions/clSubcontractInvInformation.jsp"%>


<tiles:insert beanName="clSubcontractInvoiceInformation"
	beanScope="request">
	<tiles:put name="body"
		value="/coeuslite/mit/subcontract/clSubcontractInvoiceSummary.jsp" />
</tiles:insert>