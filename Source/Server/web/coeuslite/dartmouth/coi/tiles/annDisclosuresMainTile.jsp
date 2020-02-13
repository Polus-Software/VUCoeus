<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@include
	file="/coeuslite/dartmouth/coi/definitions/cwAnnDisclDefinition.jsp"%>

<tiles:insert beanName="cwAnnDisclDefinition" beanScope="request">
	<tiles:put name="body"
		value="/coeuslite/dartmouth/coi/cwAnnDisclMain.jsp" />
</tiles:insert>
