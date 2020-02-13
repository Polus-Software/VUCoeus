<%--
/*
 * @(#)ApprovalRouting.jsp
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on
 *
 * @author  coeus-dev-team
 * @version 1.0
 */
--%>
<%--
Approval Routing page.
Uses Approval Routing Template.
--%>

<%@ page  	errorPage="ErrorPage.jsp"
		import="edu.mit.coeus.utils.CoeusConstants"%>
<!-- CASE #748 End -->
<%@ taglib uri='/WEB-INF/struts-template.tld' prefix='template' %>
<%@ taglib uri='/WEB-INF/struts-bean.tld' prefix='bean' %>
<%@ taglib uri='/WEB-INF/struts-logic.tld' prefix='logic' %>
<%@ taglib uri='/WEB-INF/response.tld' prefix='res' %>
<%@ taglib uri='/WEB-INF/request.tld' prefix='req' %>
<%@ taglib uri='/WEB-INF/session.tld' prefix='sess' %>
<%@ taglib uri='/WEB-INF/privilege.tld' prefix='coeusPrivilege' %> 

<jsp:include page="CoeusCacheRemove.jsp" flush="true" />
<%@ include file="CoeusContextPath.jsp"  %>

<!-- CASE #665 Begin -->
<req:setAttribute name="usePropDevErrorPage">true</req:setAttribute>
<!-- CASE #665 End -->	

<%
System.out.println("request.getRequestURI(): "+request.getRequestURI()+request.getPathInfo());
%>

   <template:insert template='HNCTemplate.jsp'>

  <%-- Supply the Title information to the HNCTemplate.jsp --%>
  <template:put name='title'> <bean:message key='approvalRouting.title'/> </template:put>

  <%-- Supply the Header information to the HNCTemplate.jsp --%>
  <template:put name='header' content='CoeusHeader.jsp' />


  <%-- Supply the Navigation Page  information to the HNCTemplate.jsp --%>
  <template:put name='navbar' content='CoeusLeftPane.jsp' />


  <%-- Supply the Actual Content information to the HNCTemplate.jsp  that is more dynamic--%>
  <template:put name='actualContent' content='ApprovalRoutingContent.jsp'/>

</template:insert>

