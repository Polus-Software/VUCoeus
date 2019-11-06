<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%@include file="/coeuslite/utk/propdev/definitions/clProposalApprovalInformation.jsp" %>

<tiles:insert beanName="clProposalApprovalInformation" beanScope="request">
<%
edu.mit.coeus.routing.bean.RoutingBean routingBean = 
        (edu.mit.coeus.routing.bean.RoutingBean) session.getAttribute("routingBean"+session.getId());
routingBean = (routingBean == null) ? new edu.mit.coeus.routing.bean.RoutingBean() : routingBean;
if(routingBean.getModuleCode() == edu.mit.coeus.utils.ModuleConstants.PROTOCOL_MODULE_CODE){%>
        <tiles:put name="bodyHeader" value="/coeuslite/mit/irb/common/cwGeneralInfoHeader.jsp" /> 
<%}else if(routingBean.getModuleCode() == edu.mit.coeus.utils.ModuleConstants.IACUC_MODULE_CODE){%>
        <tiles:put name="bodyHeader" value="/coeuslite/mit/iacuc/common/cwGeneralInfoHeader.jsp" /> 
<%}%>
	<tiles:put name="body" value="/coeuslite/utk/propdev/clRoutingUserDetails.jsp" />
</tiles:insert>