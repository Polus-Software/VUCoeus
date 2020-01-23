<%--
    Document   : Admin
    Created on : Apr 30, 2010, 2:23:39 PM
    Author     : Mr Lijo Joy
--%>

<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@include file="/coeuslite/mit/coiv2/admintiles/HeaderFooter.jsp"%>
<tiles:insert beanName="headerfooterbean" beanScope="request">
	<tiles:put name="body" value="/coeuslite/mit/coiv2/jsp/coiHome.jsp" />
</tiles:insert>
