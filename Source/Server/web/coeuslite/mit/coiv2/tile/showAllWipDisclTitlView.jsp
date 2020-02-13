<%-- 
    Document   : showAllWipDisclTitlView
    Created on : Apr 20, 2012, 12:18:52 PM
    Author     : ajay
--%>

<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@include
	file="/coeuslite/mit/coiv2/tile/coiV2HeaderFooterNoLeftMenu.jsp"%>
<tiles:insert beanName="coiv2ViewDisclosure" beanScope="request">
	<tiles:put name="body"
		value="/coeuslite/mit/coiv2/jsp/showAllWipDisclView.jsp" />
</tiles:insert>
