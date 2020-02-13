<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@include
	file="/coeuslite/mit/negotiation/definitions/cwNegotiationDefinition.jsp"%>

<tiles:insert beanName="cwNegotiationDefinition" beanScope="request">
	<tiles:put name="body"
		value="/coeuslite/mit/negotiation/cwNegotiationActivities.jsp" />
</tiles:insert>


