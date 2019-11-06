<%-- 
    Document   : awardListTile
    Created on : Dec 22, 2010, 3:30:50 PM
    Author     : vineetha
--%>

<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@include
	file="/coeuslite/mit/award/definitions/awardUnitinformation.jsp"%>
<tiles:insert beanName="clProposalUnitInformation" beanScope="request">
	<tiles:put name="body" value="/coeuslite/mit/award/jsp/AwardList.jsp" />
</tiles:insert>
