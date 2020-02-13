<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%@include file="/coeuslite/wmc/budget/definitions/clBudgetInformationPage.jsp" %>

<tiles:insert  beanName="clBudgetSummary" beanScope="request">
<tiles:put name="body" value="/coeuslite/wmc/budget/clBudgetModular.jsp"/>
</tiles:insert>
