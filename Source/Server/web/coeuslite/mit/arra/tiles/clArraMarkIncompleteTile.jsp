<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@include
	file="/coeuslite/mit/arra/definitions/clArraReportInformation.jsp"%>


<tiles:insert beanName="arraDetails" beanScope="request">
	<tiles:put name="body"
		value="/coeuslite/mit/arra/clCopyArraDetails.jsp" />
</tiles:insert>