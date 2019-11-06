<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@include
	file="/coeuslite/mit/irb/definitions/cwProposalInformationPage.jsp"%>


<tiles:insert beanName="cwProposalInformation" beanScope="request">
	<tiles:put name="body" value="/coeuslite/mit/irb/cwPropMainMenu.jsp" />
</tiles:insert>