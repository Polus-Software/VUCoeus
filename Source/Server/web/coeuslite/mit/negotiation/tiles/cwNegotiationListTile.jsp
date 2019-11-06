<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@include
	file="/coeuslite/mit/negotiation/definitions/cwNegotiationListDefinition.jsp"%>

<tiles:insert beanName="cwNegotiationListDefinition" beanScope="request">
	<tiles:put name="body"
		value="/coeuslite/mit/negotiation/cwNegotiationList.jsp" />
</tiles:insert>

