<%-- 
    Document   : RightPersonCertifyTile
    Created on : Dec 16, 2010, 2:23:45 PM
    Author     : anishk
--%>

<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%--<%@include file="/coeuslite/utk/propdev/definitions/clGeneralInformation.jsp" %>--%>
<logic:present name="Is_From_ppc_main">
<tiles:definition  id="clProposalInformation" page="/coeuslite/utk/utils/layouts/clProposalCertificationLayout.jsp" scope="request">
  
  <tiles:put name="header" value="/coeuslite/utk/propdev/coeusBannerForPpc.jsp" />

  <tiles:put name="subheader"  value="/coeuslite/utk/propdev/clRightPersonSubHeader.jsp" />


  <tiles:put name="menu"    value="/coeuslite/utk/propdev/rightCertifyMenu.jsp" />

  <tiles:put name="footer"  value="/coeuslite/mit/irb/common/cwCopyright.jsp" />

</tiles:definition>

</logic:present>
<logic:notPresent name="Is_From_ppc_main">
    <tiles:definition  id="clProposalInformation" page="/coeuslite/utk/utils/layouts/clProposalLayout.jsp" scope="request">

  <tiles:put name="header"  value="/coeuslite/utk/propdev/common/clProposalHeader.jsp" />

  <tiles:put name="subheader"  value="/coeuslite/utk/propdev/clRightPersonSubHeader.jsp" />

  <tiles:put name="menu"    value="/coeuslite/utk/propdev/rightCertifyMenu.jsp" />

  <tiles:put name="footer"  value="/coeuslite/mit/irb/common/cwCopyright.jsp" />

</tiles:definition>
</logic:notPresent>

<tiles:insert  beanName="clProposalInformation" beanScope="request">
	<tiles:put name="body" value="/coeuslite/utk/propdev/RightPersonCertify.jsp"/>
        <%--<tiles:put name="body" value="/coeuslite/utk/propdev/clQuestionnaire.jsp" />--%>
 </tiles:insert>
