<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@include
	file="/coeuslite/mit/iacuc/definitions/cwAmendmentSummery.jsp"%>

<tiles:insert beanName="clAmendmentDefination" beanScope="request">
	<tiles:put name="body"
		value="/coeuslite/mit/iacuc/cwApproveAmendmentRenewalSummary.jsp" />
</tiles:insert>