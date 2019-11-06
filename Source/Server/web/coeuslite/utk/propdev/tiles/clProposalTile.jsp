<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@include file="/coeuslite/utk/propdev/definitions/clProposalUnitInformation.jsp" %>

<tiles:insert  beanName="clProposalUnitInformation" beanScope="request">
<%
    String contentPage = request.getParameter("contentPage");
%>
    <tiles:put name="body" value="<%=contentPage%>" />
</tiles:insert>