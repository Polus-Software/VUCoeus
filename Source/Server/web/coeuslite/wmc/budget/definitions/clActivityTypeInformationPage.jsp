<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<tiles:definition  id="clActivityType" page="/coeuslite/wmc/utils/layouts/clActivityTypeLayout.jsp" scope="request">
	
  <tiles:put name="header"  value="/coeuslite/wmc/budget/common/clBudgetHeader.jsp" />  
 
  <tiles:put name="bodyHeader"  value="/coeuslite/wmc/budget/common/clPropBudgetInvesHeader.jsp" />
  
  <tiles:put name="footer"  value="/coeuslite/mit/irb/common/cwCopyright.jsp" />

</tiles:definition>
