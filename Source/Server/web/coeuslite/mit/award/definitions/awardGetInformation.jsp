<%-- 
    Document   : awardGetInformation
    Created on : Dec 29, 2010, 2:04:17 PM
    Author     : vineetha
--%>

<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<tiles:definition id="awardGetInformation"
	page="/coeuslite/mit/utils/layouts/awardGetInfoLayout.jsp"
	scope="request">

	<tiles:put name="header"
		value="/coeuslite/mit/award/common/awardHeader.jsp" />
	<tiles:put name="menu"
		value="/coeuslite/mit/award/common/awardMenu.jsp" />

	<tiles:put name="footer"
		value="/coeuslite/mit/irb/common/cwCopyright.jsp" />

</tiles:definition>

