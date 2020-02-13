<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@include
	file="/coeuslite/mit/irb/definitions/cwDisclosureInformation.jsp"%>
<tiles:insert beanName="cwDisclosure" beanScope="request">
	<tiles:put name="body"
		value="/coeuslite/dartmouth/coi/cwCoiException.jsp" />
</tiles:insert>