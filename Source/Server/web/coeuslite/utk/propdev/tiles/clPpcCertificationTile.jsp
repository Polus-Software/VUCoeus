<%-- 
    Document   : clPpcCertificationTile
    Created on : Jan 12, 2011, 6:16:45 PM
    Author     : anishk
--%>

<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%--<%@include file="/coeuslite/utk/propdev/definitions/clGeneralInformation.jsp" %>--%>

<tiles:definition  id="clProposalInformation" page="/coeuslite/utk/utils/layouts/clProposalCertificationLayout.jsp" scope="request">

  <tiles:put name="header"  value="/coeuslite/utk/propdev/clProposalPersonsCertifyMain.jsp" />



  <tiles:put name="menu"    value="/coeuslite/utk/propdev/rightCertifyMenu.jsp" />

  <tiles:put name="footer"  value="/coeuslite/mit/irb/common/cwCopyright.jsp" />

</tiles:definition>



<tiles:insert  beanName="clProposalInformation" beanScope="request">
	<tiles:put name="body" value="/coeuslite/utk/propdev/RightPersonCertify.jsp"/>
        <%--<tiles:put name="body" value="/coeuslite/utk/propdev/clQuestionnaire.jsp" />--%>
 </tiles:insert>
