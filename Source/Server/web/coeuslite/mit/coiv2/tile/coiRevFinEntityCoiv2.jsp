<%-- 
    Document   : coiRevFinEntityCoiv2
    Created on : Sep 16, 2010, 5:11:49 PM
    Author     : New
--%>

<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@include
	file="/coeuslite/mit/coiv2/tile/coiV2HeaderFooterNoLeftMenu.jsp"%>
<tiles:insert beanName="coiv2ViewDisclosure" beanScope="request">
	<tiles:put name="body"
		value="/coeuslite/mit/coiv2/jsp/cwCoiV2RevFinEntity.jsp" />
</tiles:insert>