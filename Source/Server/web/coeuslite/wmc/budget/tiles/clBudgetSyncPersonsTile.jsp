<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%@include file="/coeuslite/wmc/budget/definitions/clBudgetSyncInformationPage.jsp" %>

<tiles:insert  beanName="clBudgetSync" beanScope="request">
<tiles:put name="body" value="/coeuslite/wmc/budget/clBudgetSyncPersons.jsp" />
</tiles:insert>
