<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@include
	file="/coeuslite/mit/irb/definitions/cwDisclMainInformation.jsp"%>
<tiles:insert beanName="cwMainDisclosure" beanScope="request">
	<tiles:put name="body"
		value="/coeuslite/dartmouth/coi/cwFinEntityStatus.jsp" />
</tiles:insert>