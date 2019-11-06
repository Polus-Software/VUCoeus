<%@taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@include
	file="/coeuslite/mit/iacuc/definitions/cwProtocolRequestActionInformation.jsp"%>

<tiles:insert beanName="cwGeneralInfo" beanScope="request">
	<tiles:put name="body"
		value="/coeuslite/mit/iacuc/cwCompleteProtocolAction.jsp" />
</tiles:insert>
