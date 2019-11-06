<%-- 
    Document   : awardUnitinformation
    Created on : Dec 22, 2010, 6:07:50 PM
    Author     : vineetha
--%>

<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<tiles:definition id="clProposalUnitInformation"
	page="/coeuslite/mit/utils/layouts/awardLayout.jsp" scope="request">

	<tiles:put name="header"
		value="/coeuslite/mit/award/common/awardHeader.jsp" />
	<tiles:put name="body1"
		value="/coeuslite/mit/award/tile/displayCount.jsp" />
	<tiles:put name="footer"
		value="/coeuslite/mit/irb/common/cwCopyright.jsp" />
</tiles:definition>
