<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@include
	file="/coeuslite/mit/arra/definitions/clArraInformationPage.jsp"%>

<tiles:insert beanName="cwArraInformation" beanScope="request">
	<tiles:put name="body"
		value="/coeuslite/mit/arra/clArraPastCycleList.jsp" />
</tiles:insert>
