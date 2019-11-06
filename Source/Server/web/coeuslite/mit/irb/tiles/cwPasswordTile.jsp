<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@include file="/coeuslite/mit/irb/definitions/cwInformationPage.jsp"%>

<tiles:insert beanName="cwInformation" beanScope="request">
	<%
        String contentPage = request.getParameter("contentPage");
    %>
	<tiles:put name="body" value="<%=contentPage%>" />
</tiles:insert>
