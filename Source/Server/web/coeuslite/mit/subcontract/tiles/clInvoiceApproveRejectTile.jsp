<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@include
	file="/coeuslite/mit/subcontract/definitions/clInvoiceApprovalInformation.jsp"%>


<tiles:insert beanName="clInvoiceApprovalInformation"
	beanScope="request">
	<tiles:put name="body"
		value="/coeuslite/mit/subcontract/clInvoiceApproveReject.jsp" />
</tiles:insert>