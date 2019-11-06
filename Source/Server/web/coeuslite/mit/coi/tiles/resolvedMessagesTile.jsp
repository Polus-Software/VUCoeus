<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@include
	file="/coeuslite/mit/coi/definitions/cwInboxInformationPage.jsp"%>

<tiles:insert beanName="cwInboxInformation" beanScope="request">
	<tiles:put name="body"
		value="/coeuslite/mit/coi/cwResolvedMessages.jsp" />
</tiles:insert>
