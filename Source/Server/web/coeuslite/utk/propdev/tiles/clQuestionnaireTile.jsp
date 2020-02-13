<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@include file="/coeuslite/utk/propdev/definitions/clProposalInformation.jsp" %>

<% 
String actionFrom = (String)session.getAttribute("actionFrom");
if(actionFrom!=null && !actionFrom.equals("")){
    if("PROTOCOL".equals(actionFrom)){%>    
<%@include file="/coeuslite/mit/irb/definitions/cwProtocolInformation.jsp" %>
<tiles:insert  beanName="cwGeneralInfo" beanScope="request">
    <tiles:put  name="body" value="/coeuslite/utk/propdev/clQuestionnaire.jsp" />
</tiles:insert>        
<%}else if("IACUC_PROTOCOL".equals(actionFrom)){%>    
<%@include file="/coeuslite/mit/iacuc/definitions/cwProtocolInformation.jsp" %>
<tiles:insert  beanName="cwGeneralInfo" beanScope="request">
    <tiles:put  name="body" value="/coeuslite/utk/propdev/clQuestionnaire.jsp" />
</tiles:insert>        
<%}else if("DEV_PROPOSAL".equals(actionFrom)){%>
<tiles:insert  beanName="clProposalInformation" beanScope="request">
    <tiles:put name="body" value="/coeuslite/utk/propdev/clQuestionnaire.jsp" />
</tiles:insert>
<%}
}%>

