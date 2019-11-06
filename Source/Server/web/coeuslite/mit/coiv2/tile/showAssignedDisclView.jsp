<%-- 
    Document   : showAssignedDisclView
    Created on : May 6, 2010, 10:20:30 AM
    Author     : Mr
--%>

<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@include
	file="/coeuslite/mit/coiv2/tile/coiV2HeaderFooterNoLeftMenu.jsp"%>
<tiles:insert beanName="coiv2ViewDisclosure" beanScope="request">
	<tiles:put name="body"
		value="/coeuslite/mit/coiv2/jsp/showAssignedDiscl.jsp" />
</tiles:insert>
