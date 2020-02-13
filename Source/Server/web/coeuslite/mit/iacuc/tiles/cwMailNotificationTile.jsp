<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@include
	file="/coeuslite/mit/iacuc/definitions/cwProtocolInformation.jsp"%>
<%@include
	file="/coeuslite/utk/propdev/definitions/clProposalInformation.jsp"%>

<% 
String actionFrom = (String)session.getAttribute("actionFrom");
if(actionFrom!=null && !actionFrom.equals("")){
    if(actionFrom.equals("IRB")){%>
<tiles:insert beanName="cwGeneralInfo" beanScope="request">
	<tiles:put name="body" value="/coeuslite/mit/iacuc/cwMailUI.jsp" />
</tiles:insert>
<%}else if(actionFrom.equals("DEV_PROPOSAL")){%>
<tiles:insert beanName="clProposalInformation" beanScope="request">
	<tiles:put name="body" value="/coeuslite/mit/iacuc/cwMailUI.jsp" />
</tiles:insert>
<%}
}%>

