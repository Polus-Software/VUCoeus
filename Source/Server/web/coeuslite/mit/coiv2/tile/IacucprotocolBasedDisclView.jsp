<%-- 
    Document   : IacucprotocolBasedDisclView
    Created on : Sep 19, 2011, 5:33:30 PM
    Author     : twinkle
--%>

<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@include
	file="/coeuslite/mit/coiv2/tile/coiv2DisclMainInformation.jsp"%>
<tiles:insert beanName="coiv2MainDisclosure" beanScope="request">
	<tiles:put name="body"
		value="/coeuslite/mit/coiv2/jsp/iacucprotocolBasedDiscl.jsp" />
</tiles:insert>
