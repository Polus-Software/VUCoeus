<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@include file="/coeuslite/mit/irb/definitions/cwAmendmentSummery.jsp"%>

<tiles:insert beanName="clAmendmentDefination" beanScope="request">
	<tiles:put name="body"
		value="/coeuslite/mit/irb/cwApproveAmendmentRenewalSummary.jsp" />
</tiles:insert>