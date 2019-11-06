<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%@include file="/coeuslite/utk/propdev/definitions/clProposalInformation.jsp" %>

<tiles:insert  beanName="clProposalInformation" beanScope="request">
    <%
       String contentPage = request.getParameter("contentPage");
    %>
        <tiles:put name="body" value="<%=contentPage%>" />
</tiles:insert>