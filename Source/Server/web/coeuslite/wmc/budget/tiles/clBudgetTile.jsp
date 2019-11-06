<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%@include file="/coeuslite/wmc/budget/definitions/clBudgetInformationPage.jsp" %>

<tiles:insert  beanName="clBudgetSummary" beanScope="request">
<%String contentPage = "/coeuslite/wmc/budget/"+request.getParameter("content");%>
<tiles:put name="body" value="<%=contentPage%>" />
</tiles:insert>
