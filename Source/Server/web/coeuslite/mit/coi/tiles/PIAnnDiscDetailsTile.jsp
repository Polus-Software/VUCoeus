<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@include
	file="/coeuslite/mit/irb/definitions/cwCOIInformationPage.jsp"%>

<tiles:insert beanName="cwCOIInformation" beanScope="request">
	<tiles:put name="body"
		value="/coeuslite/mit/coi/cwPIAnnDiscDetails.jsp" />
</tiles:insert>
<!-- web54607.mail.re2.yahoo.com compressed/chunked Tue Sep 11 07:30:06 PDT 2007 -->
