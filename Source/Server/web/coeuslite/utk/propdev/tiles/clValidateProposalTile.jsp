<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%@include file="/coeuslite/wmc/budget/definitions/clBudgetVersionsInformationPage.jsp" %>
<%@include file="/coeuslite/wmc/budget/definitions/clBudgetInformationPage.jsp" %>
<%@include file="/coeuslite/utk/propdev/definitions/clProposalInformation.jsp" %>

<%String requestFrom = (String)request.getAttribute("VALIDATION_FROM");
if("BV".equals(requestFrom)){%>
    <tiles:insert  beanName="clBudgetVersions" beanScope="request">
    <tiles:put name="body" value="/coeuslite/utk/propdev/clProposalValidationRules.jsp" />
    </tiles:insert>
<%}else if("B".equals(requestFrom) || "BS".equals(requestFrom)){%>
    <tiles:insert  beanName="clBudgetSummary" beanScope="request">
    <tiles:put name="body" value="/coeuslite/utk/propdev/clProposalValidationRules.jsp" />
    </tiles:insert>
<%}else{%>
    <tiles:insert  beanName="clProposalInformation" beanScope="request">
    <tiles:put name="body" value="/coeuslite/utk/propdev/clProposalValidationRules.jsp" />
    </tiles:insert>
<%}%>
