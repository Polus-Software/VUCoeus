<%-- 
    Document   : userAttachmentS2STile
    Created on : Sep 4, 2013, 9:53:31 AM
    Author     : polusdev
--%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%@include file="/coeuslite/utk/propdev/definitions/clProposalInformation.jsp" %>


<tiles:insert  beanName="clProposalInformation" beanScope="request">
	<tiles:put name="body" value="/coeuslite/utk/propdev/userAttachmentS2Sform.jsp" />
</tiles:insert>

