<%-- 
    Document   : deactivateFinEntTileCoiv2
    Created on : Sep 29, 2011, 5:39:01 PM
    Author     : indhulekha
--%>

<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@include
	file="/coeuslite/mit/irb/definitions/cwDisclosureInformation.jsp"%>
<tiles:insert beanName="cwDisclosure" beanScope="request">
	<tiles:put name="body"
		value="/coeuslite/mit/coiv2/jsp/cwDeactivateFinEntCoiv2.jsp" />
</tiles:insert>
