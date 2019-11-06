<%-- 
    Document   : protocolView
    Created on : Mar 20, 2010, 12:35:33 PM
    Author     : Sony
--%>

<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@include
	file="/coeuslite/mit/coiv2/tile/coiv2DisclMainInformation.jsp"%>
<tiles:insert beanName="coiv2MainDisclosure" beanScope="request">
	<tiles:put name="body"
		value="/coeuslite/mit/coiv2/jsp/protocolBasedDiscl.jsp" />
</tiles:insert>
