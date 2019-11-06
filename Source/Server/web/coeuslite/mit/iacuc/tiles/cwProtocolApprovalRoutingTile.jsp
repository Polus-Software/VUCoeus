<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ include
	file="/coeuslite/mit/iacuc/definitions/cwProtocolDefinition.jsp"%>

<tiles:insert beanName="cwProtocolDefinition" beanScope="request">
	<tiles:put name="body"
		value="/coeuslite/mit/iacuc/cwProtocolRouting.jsp" />
</tiles:insert>