<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@include
	file="/coeuslite/mit/irb/definitions/cwProtocolInformationPage.jsp"%>


<tiles:insert beanName="cwProtocolInformation" beanScope="request">
	<tiles:put name="body"
		value="/coeuslite/mit/irb/cwProtocolSearchHome.jsp" />
</tiles:insert>
