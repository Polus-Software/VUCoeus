<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%@include file="/coeuslite/utk/propdev/definitions/clProposalUnitInformation.jsp" %>


<tiles:insert  beanName="clProposalUnitInformation" beanScope="request">
	<tiles:put name="body" value="/coeuslite/utk/propdev/clUnitProposal.jsp" />
</tiles:insert>
