<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%@include file="/coeuslite/wmc/budget/definitions/clBudgetVersionsInformationPage.jsp" %>

<tiles:insert  beanName="clBudgetVersions" beanScope="request">
<tiles:put name="body" value="/coeuslite/wmc/budget/clActivityTypeChange.jsp" />
</tiles:insert>
