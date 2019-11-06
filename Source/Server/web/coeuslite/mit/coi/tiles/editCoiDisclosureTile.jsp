
<%@include
	file="/coeuslite/mit/irb/definitions/cwCOIInformationPage.jsp"%>

<tiles:insert beanName="cwCOIInformation" beanScope="request">
	<tiles:put name="body"
		value="/coeuslite/mit/coi/cwEditCoiDisclosure.jsp" />
</tiles:insert>

