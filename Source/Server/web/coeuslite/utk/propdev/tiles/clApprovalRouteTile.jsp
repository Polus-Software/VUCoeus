<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>



<%@include file="/coeuslite/utk/propdev/definitions/clApprovalRouteInformation.jsp" %>



<tiles:insert  beanName="clInboxInformation" beanScope="request">
<tiles:put name="body" value="/coeuslite/utk/propdev/clApprovalRoute.jsp" />
</tiles:insert>

