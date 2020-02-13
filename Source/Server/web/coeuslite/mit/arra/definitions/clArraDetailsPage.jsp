<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>


<tiles:definition id="arraDetailsPage"
	page="/coeuslite/mit/utils/layouts/classicLayout.jsp" scope="request">

	<tiles:put name="header"
		value="/coeuslite/mit/arra/common/clArraMenuHeader.jsp" />

	<tiles:put name="menu"
		value="/coeuslite/mit/arra/common/clArraMenu.jsp" />

	<tiles:put name="footer"
		value="/coeuslite/mit/irb/common/cwCopyright.jsp" />
</tiles:definition>

