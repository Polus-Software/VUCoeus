<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@include
	file="/coeuslite/mit/irb/definitions/cwProtocolInformation.jsp"%>

<tiles:insert beanName="cwGeneralInfo" beanScope="request">
	<tiles:put name="body"
		value="/coeuslite/mit/irb/cwAmendmentRenewalSummary.jsp" />
</tiles:insert>
