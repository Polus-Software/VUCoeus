<%--
    Document   : coiMainView
    Created on : Mar 20, 2010, 10:28:12 AM
    Author     : Jaisha
--%>

<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@include file="/coeuslite/mit/coiv2/admintiles/HeaderFooter.jsp"%>
<tiles:insert beanName="headerfooterbean" beanScope="request">
	<tiles:put name="body"
		value="/coeuslite/mit/coiv2/jsp/financialEntities.jsp" />
</tiles:insert>