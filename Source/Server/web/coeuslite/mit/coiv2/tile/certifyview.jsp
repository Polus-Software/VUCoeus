<%-- 
    Document   : certifyview
    Created on : Nov 16, 2010, 10:18:16 AM
    Author     : vineetha
--%>


<%--<%@include file="/coeuslite/mit/coiv2/tile/coiv2DisclMainInformation.jsp" %>
<tiles:insert  beanName="coiv2MainDisclosure" beanScope="request">--%>

<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@include file="/coeuslite/mit/coiv2/tile/disclHeaderFooter.jsp"%>
<tiles:insert beanName="headerfooterbean" beanScope="request">
	<tiles:put name="body" value="/coeuslite/mit/coiv2/jsp/cert.jsp" />
</tiles:insert>


