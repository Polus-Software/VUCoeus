<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%-- 
System.out.println("************************************** "); 
System.out.println("**** In clProtocolInformationPage.jsp ** "); 
System.out.println("************************************** "); 
--%>

<tiles:definition id="cwInboxInformation"
	page="/coeuslite/mit/utils/layouts/cwInformationLayout.jsp"
	scope="request">

	<tiles:put name="header"
		value="/coeuslite/mit/coi/common/cwInboxHeader.jsp" />
	<tiles:put name="footer"
		value="/coeuslite/mit/irb/common/cwCopyright.jsp" />

</tiles:definition>
