<%-- 
    Document   : reviewerTile
    Created on : 19 Apr, 2012, 3:48:40 PM
    Author     : indhulekha
--%>


<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@include file="/coeuslite/mit/coiv2/tile/disclHeaderFooter.jsp"%>
<tiles:insert beanName="headerfooterbean" beanScope="request">
	<tiles:put name="body"
		value="/coeuslite/mit/coiv2/jsp/reviewerNote.jsp" />
</tiles:insert>