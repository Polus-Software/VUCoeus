<%-- 
    Document   : workInProgressTile
    Created on : Dec 29, 2008, 3:20:35 PM
    Author     : sharathk
--%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@include
	file="/coeuslite/mit/irb/definitions/cwCOIInformationPage.jsp"%>

<tiles:insert beanName="cwCOIInformation" beanScope="request">
	<tiles:put name="body" value="/coeuslite/mit/coi/cwWorkInProgress.jsp" />
</tiles:insert>