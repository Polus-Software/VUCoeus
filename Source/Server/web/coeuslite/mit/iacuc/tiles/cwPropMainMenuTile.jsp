<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@include
	file="/coeuslite/mit/iacuc/definitions/cwProposalInformationPage.jsp"%>


<tiles:insert beanName="cwProposalInformation" beanScope="request">
	<tiles:put name="body" value="/coeuslite/mit/iacuc/cwPropMainMenu.jsp" />
</tiles:insert>