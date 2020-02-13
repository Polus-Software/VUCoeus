<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@include
	file="/coeuslite/mit/iacuc/definitions/cwProtocolInformationPage.jsp"%>


<tiles:insert beanName="cwProtocolInformation" beanScope="request">
	<tiles:put name="body"
		value="/coeuslite/mit/iacuc/cwProtocolSearchHome.jsp" />
</tiles:insert>
