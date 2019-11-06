<%-- 
    Document   : assignDisclView
    Created on : Apr 29, 2010, 2:54:48 PM
    Author     : Mr
--%>

<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%--<%@include file="/coeuslite/mit/coiv2/tile/coiV2HeaderFooterNoLeftMenu.jsp" %>--%>
<%@include file="/coeuslite/mit/coiv2/tile/disclHeaderFooter.jsp"%>
<tiles:insert beanName="headerfooterbean" beanScope="request">
	<%--<tiles:insert  beanName="coiv2ViewDisclosure" beanScope="request">--%>
	<%--<tiles:put name="body" value="/coeuslite/mit/coiv2/jsp/assignDisclToUser.jsp" />--%>
	<tiles:put name="body"
		value="/coeuslite/mit/coiv2/jsp/assignDisclosureToUser.jsp" />
</tiles:insert>




