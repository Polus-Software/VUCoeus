<%-- 
    Document   : travelViewTile
    Created on : Jun 6, 2012, 12:08:09 PM
    Author     : veena
--%>

<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@include file="/coeuslite/mit/coiv2/tile/disclHeaderFooter.jsp"%>
<tiles:insert beanName="headerfooterbean" beanScope="request">
	<tiles:put name="body" value="/coeuslite/mit/coiv2/jsp/addTravels.jsp" />
</tiles:insert>
