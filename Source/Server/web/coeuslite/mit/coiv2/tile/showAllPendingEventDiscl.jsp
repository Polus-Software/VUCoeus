<%-- 
    Document   : showAllPendingEventDiscl
    Created on : Sep 5, 2011, 4:18:06 PM
    Author     : twinkle
--%>

<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@include
	file="/coeuslite/mit/coiv2/tile/coiV2HeaderFooterNoLeftMenu.jsp"%>
<tiles:insert beanName="coiv2ViewDisclosure" beanScope="request">
	<tiles:put name="body"
		value="/coeuslite/mit/coiv2/jsp/showAllPendingCOIEventDiscl.jsp" />
</tiles:insert>
