<%-- 
    Document   : completeReviewTile
    Created on : 20 Apr, 2012, 12:21:35 PM
    Author     : indhulekha
--%>

<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@include file="/coeuslite/mit/coiv2/tile/disclHeaderFooter.jsp"%>
<tiles:insert beanName="headerfooterbean" beanScope="request">
	<tiles:put name="body"
		value="/coeuslite/mit/coiv2/jsp/completeReview.jsp" />
</tiles:insert>
