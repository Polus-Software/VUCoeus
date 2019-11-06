<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ include
	file="/coeuslite/mit/irb/definitions/cwProtocolDefinition.jsp"%>

<tiles:insert beanName="cwProtocolDefinition" beanScope="request">
	<tiles:put name="body"
		value="/coeuslite/mit/irb/cwProtocolInformation.jsp" />
</tiles:insert>