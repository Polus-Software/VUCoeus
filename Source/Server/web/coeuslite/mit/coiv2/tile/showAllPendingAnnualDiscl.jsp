<%-- 
    Document   : showAllPendingAnnualDiscl
    Created on : Sep 2, 2011, 10:11:33 AM
    Author     : twinkle
--%>


<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@include
	file="/coeuslite/mit/coiv2/tile/coiV2HeaderFooterNoLeftMenu.jsp"%>
<tiles:insert beanName="coiv2ViewDisclosure" beanScope="request">
	<tiles:put name="body"
		value="/coeuslite/mit/coiv2/jsp/showAllPendingCOIDiscl.jsp" />
</tiles:insert>
