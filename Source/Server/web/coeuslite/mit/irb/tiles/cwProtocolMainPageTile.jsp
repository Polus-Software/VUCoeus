<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@include file="/coeuslite/mit/irb/definitions/cwInformationPage.jsp"%>

<tiles:insert beanName="cwInformation" beanScope="request">
	<tiles:put name="body" value="/coeuslite/mit/irb/cwMainPage.jsp" />
</tiles:insert>
