<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%@include file="/coeuslite/utk/propdev/definitions/clProposalApprovalInformation.jsp" %>

<tiles:insert  beanName="clProposalApprovalInformation" beanScope="request">
	<tiles:put name="body" value="/coeuslite/utk/propdev/clSubmitToSponsorMessage.jsp" />
</tiles:insert>

