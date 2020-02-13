<%-- 
    Document   : noRightsView
    Created on : Apr 26, 2010, 1:40:11 PM
    Author     : Sony
--%>

<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@include file="/coeuslite/mit/coiv2/tile/coiV2HeaderFooter.jsp"%>
<tiles:insert beanName="coiv2ViewDisclosure" beanScope="request">
	<tiles:put name="body"
		value="/coeuslite/mit/coiv2/jsp/noRightsMessage.jsp" />
</tiles:insert>
