<%-- 
    Document   : clProposalPersonCertifyTile
    Created on : Jan 17, 2011, 12:30:29 PM
    Author     : anishk
--%>

<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%--<%@include file="/coeuslite/utk/propdev/definitions/clGeneralInformation.jsp" %>--%>

<tiles:definition  id="clProposalInformation" page="/coeuslite/utk/utils/layouts/clProposalPersonCertifyLayout.jsp" scope="request">



</tiles:definition>
<tiles:insert  beanName="clProposalInformation" beanScope="request">
	<tiles:put name="header" value="/coeuslite/utk/propdev/coeusBannerForPpc.jsp" />
        <tiles:put name="body" value="/coeuslite/utk/propdev/clProposalPersonsCertifyMain.jsp"/>
        <%--<tiles:put name="body" value="/coeuslite/utk/propdev/clQuestionnaire.jsp" />--%>
 </tiles:insert>