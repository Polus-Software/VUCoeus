<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>


<tiles:definition  id="clProposalApprovalInformation" page="/coeuslite/utk/utils/layouts/clProposalApprovalLayout.jsp" scope="request">
	
  <tiles:put name="header"  value="/coeuslite/utk/propdev/common/clProposalHeader.jsp" />
 
  <tiles:put name="bodyHeader"  value="/coeuslite/utk/propdev/common/clProposalGeneralHeader.jsp" /> 
 
  <tiles:put name="footer"  value="/coeuslite/mit/irb/common/cwCopyright.jsp" />

</tiles:definition>
