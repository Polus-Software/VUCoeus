<%-- 
    Document   : coiv2FinEntityView
    Created on : Sep 16, 2010, 2:31:22 PM
    Author     : ROSHIN
--%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@include
	file="/coeuslite/mit/coiv2/tile/coiv2HeaderFooterFinNoLeftMenu.jsp"%>
<tiles:insert beanName="coiv2FinViewDisclosure" beanScope="request">
	<tiles:put name="body"
		value="/coeuslite/mit/coiv2/jsp/cwCoiFinEntityCoiv2.jsp" />
</tiles:insert>


