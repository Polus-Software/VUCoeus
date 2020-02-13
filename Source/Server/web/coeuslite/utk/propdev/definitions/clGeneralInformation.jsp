<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>


<tiles:definition  id="clProposalInformation" page="/coeuslite/utk/utils/layouts/clProposalLayout.jsp" scope="request">
	
  <tiles:put name="header"  value="/coeuslite/utk/propdev/common/clProposalHeader.jsp" />
  
  <tiles:put name="subheader"  value="/coeuslite/utk/propdev/common/clGeneralInfoHeader.jsp" />
  
  <tiles:put name="menu"    value="/coeuslite/utk/propdev/common/clProposalMenu.jsp" /> 
  
  <tiles:put name="footer"  value="/coeuslite/mit/irb/common/cwCopyright.jsp" />

</tiles:definition>

