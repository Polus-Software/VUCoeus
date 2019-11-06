<%@taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@include
	file="/coeuslite/mit/irb/definitions/cwProtocolRequestActionInformation.jsp"%>

<tiles:insert beanName="cwGeneralInfo" beanScope="request">
	<tiles:put name="body"
		value="/coeuslite/mit/irb/cwSubmissionQuestionnaire.jsp" />
</tiles:insert>
