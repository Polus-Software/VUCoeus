<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%@include file="/coeuslite/utk/propdev/definitions/clGeneralInformation.jsp" %>


<tiles:insert  beanName="clProposalInformation" beanScope="request">
	<tiles:put name="body" value="/coeuslite/utk/propdev/clGeneralInfo.jsp" />
</tiles:insert>