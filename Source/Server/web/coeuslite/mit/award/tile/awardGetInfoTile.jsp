<%-- 
    Document   : awardGetInfoTile
    Created on : Dec 29, 2010, 1:59:18 PM
    Author     : vineetha
--%>

<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@include
	file="/coeuslite/mit/award/definitions/awardGetInformation.jsp"%>
<tiles:insert beanName="awardGetInformation" beanScope="request">
	<tiles:put name="body"
		value="/coeuslite/mit/award/jsp/awardGetInfo.jsp" />
</tiles:insert>






