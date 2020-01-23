<!--
/*
 * @(#)AnnDiscCertification.jsp	1.0	2004/5/17
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 *
 * @author  coeus-dev-team
 */
-->

<!--
A View component that displays the certification questions that apply to all 
disclosures for a given user.  Answers to the certification questions will
be validated against answers user has given to certification questions for
any disclosed financial entities.

This page is using HNCTemplate.jsp as a  template that supports and layout the
components in a standard fashion.
-->

<%@page errorPage = "ErrorPage.jsp" 
	%>
<%@ taglib uri='/WEB-INF/struts-template.tld' 	prefix='template' %>
<%@ taglib uri='/WEB-INF/struts-bean.tld' 	prefix='bean' %>
<%@ taglib uri='/WEB-INF/privilege.tld' 	prefix='coeusPrivilege' %>
<%@ taglib uri='/WEB-INF/request.tld' 		prefix='req' %>

<!-- check user login status -->
<coeusPrivilege:checkLogin forwardName="loginCOI"/>

<req:setAttribute name="annDiscCertification">annDiscCertification</req:setAttribute>

<template:insert template='HNCTemplate.jsp'>
  <%-- Supply the Title information to the HNCTemplate.jsp --%>
  <template:put name='title'> <bean:message key='annDiscCertification.title'/> </template:put>
  
  <%-- Supply the Header information to the HNCTemplate.jsp --%>
  <template:put name='header' content='CoeusHeader.jsp' />
  
  <%-- Supply the Navigation Page  information to the HNCTemplate.jsp --%>
  <template:put name='navbar' content='AnnDisclosureFELeftPane.jsp' />
  
  <%-- Supply the Actual Content information to the HNCTemplate.jsp  that is more dynamic--%>
  <!--<template:put name='actualContent' content='AnnDiscCertificationContent.jsp'/>-->
</template:insert>