<%-- 
    Document   : showAllReviewCmpltView
    Created on : Apr 20, 2012, 1:50:42 PM
    Author     : veena
--%>

<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@include
	file="/coeuslite/mit/coiv2/tile/coiV2HeaderFooterNoLeftMenu.jsp"%>
<tiles:insert beanName="coiv2ViewDisclosure" beanScope="request">
	<tiles:put name="body"
		value="/coeuslite/mit/coiv2/jsp/showAllReviewComplt.jsp" />
</tiles:insert>
