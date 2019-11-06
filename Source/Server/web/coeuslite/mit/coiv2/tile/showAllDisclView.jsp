<%-- 
    Document   : showAllDisclView
    Created on : Apr 28, 2010, 2:24:27 PM
    Author     : Mr
--%>

<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@include
	file="/coeuslite/mit/coiv2/tile/coiV2HeaderFooterNoLeftMenu.jsp"%>
<tiles:insert beanName="coiv2ViewDisclosure" beanScope="request">
	<tiles:put name="body"
		value="/coeuslite/mit/coiv2/jsp/showAllDiscl.jsp" />
</tiles:insert>