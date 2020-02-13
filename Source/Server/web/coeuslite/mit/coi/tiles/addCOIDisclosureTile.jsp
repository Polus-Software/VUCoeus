<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@include
	file="/coeuslite/mit/irb/definitions/cwCOIInformationPage.jsp"%>

<tiles:insert beanName="cwCOIInformation" beanScope="request">
	<tiles:put name="body"
		value="/coeuslite/mit/coi/cwAddCOIDisclosure.jsp" />
</tiles:insert>

