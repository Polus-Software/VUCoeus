<%-- 
    Document   : disclosureView
    Created on : Mar 22, 2010, 5:33:30 PM
    Author     : Sony
--%>

<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@include file="/coeuslite/mit/coiv2/tile/coiV2HeaderFooter.jsp"%>
<tiles:insert beanName="coiv2ViewDisclosure" beanScope="request">
	<tiles:put name="body"
		value="/coeuslite/mit/coiv2/jsp/ShowDiclosure.jsp" />
</tiles:insert>
