<%-- 
    Document   : historyView
    Created on : Apr 23, 2010, 4:04:56 PM
    Author     : Mr
--%>

<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@include file="/coeuslite/mit/coiv2/admintiles/HeaderFooter.jsp"%>
<tiles:insert beanName="headerfooterbean" beanScope="request">
	<tiles:put name="body" value="/coeuslite/mit/coiv2/jsp/showHistory.jsp" />
</tiles:insert>