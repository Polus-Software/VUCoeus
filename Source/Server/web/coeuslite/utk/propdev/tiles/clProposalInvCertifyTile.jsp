<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%@include file="/coeuslite/utk/propdev/definitions/clProposalInvCertifyInformation.jsp" %>


<tiles:insert  beanName="clProposalInvCertify" beanScope="request">
	<tiles:put name="body" value="/coeuslite/utk/propdev/clProposalInvCertify.jsp" />
</tiles:insert>