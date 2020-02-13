<%-- 
    Document   : travelDetTile
    Created on : Mar 20, 2012, 11:57:12 AM
    Author     : indhulekha
--%>

<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@include
	file="/coeuslite/mit/coiv2/tile/coiv2DisclMainInformation.jsp"%>
<tiles:insert beanName="coiv2MainDisclosure" beanScope="request">
	<tiles:put name="body" value="/coeuslite/mit/coiv2/jsp/addTravels.jsp" />
</tiles:insert>
