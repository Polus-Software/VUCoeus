<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@include
	file="/coeuslite/mit/coi/definitions/cwAnnualDiscDefinition.jsp"%>

<tiles:insert beanName="cwAnnualDiscDefinition" beanScope="request">
	<tiles:put name="body"
		value="/coeuslite/mit/coi/cwAnnualDisclosuresMain.jsp" />
</tiles:insert>
