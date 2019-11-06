<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@include
	file="/coeuslite/mit/iacuc/definitions/cwScheduleInformationPage.jsp"%>

<tiles:insert beanName="cwScheduleInformation" beanScope="request">
	<tiles:put name="body"
		value="/coeuslite/mit/iacuc/cwSubmittedProtocols.jsp" />
</tiles:insert>
