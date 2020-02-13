<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@include
	file="/coeuslite/mit/irb/definitions/cwScheduleInformationPage.jsp"%>


<tiles:insert beanName="cwScheduleInformation" beanScope="request">
	<tiles:put name="body"
		value="/coeuslite/mit/irb/cwScheduleOtherActions.jsp" />
</tiles:insert>
