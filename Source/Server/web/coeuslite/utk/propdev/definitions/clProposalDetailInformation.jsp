<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>


<tiles:definition  id="clProposalInformation" page="/coeuslite/mit/utils/layouts/classicLayout.jsp" scope="request">
	
  <tiles:put name="header"  value="/coeuslite/utk/propdev/common/clProposalHeader.jsp" />
    
  <tiles:put name="menu"    value="/coeuslite/utk/propdev/common/clProposalDetailMenu.jsp" /> 
  
  <tiles:put name="footer"  value="/coeuslite/mit/irb/common/cwCopyright.jsp" />

</tiles:definition>

