<%-- 
    Document   : coiViewerView
    Created on : May 6, 2010, 3:17:24 PM
    Author     : Mr
--%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@include file="/coeuslite/mit/coiv2/admintiles/HeaderFooter.jsp"%>
<tiles:insert beanName="headerfooterbean" beanScope="request">
	<tiles:put name="body" value="/coeuslite/mit/coiv2/jsp/coiHome.jsp" />
</tiles:insert>