<%--
    Document   : setViewerActionView
    Created on : May 8, 2010, 3:28:44 PM
    Author     : Roshin
--%>

<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@include file="/coeuslite/mit/coiv2/tile/disclHeaderFooter.jsp"%>
<tiles:insert beanName="headerfooterbean" beanScope="request">
	<tiles:put name="body"
		value="/coeuslite/mit/coiv2/jsp/setViewerAction.jsp" />
</tiles:insert>
