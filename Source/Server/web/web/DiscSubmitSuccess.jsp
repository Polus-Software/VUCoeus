<!--
/*
 * @(#)DiscSubmitSuccess.jsp	1.0  02/20/2004
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  coeus-dev-team
 */
-->
<!--
A View component that displays the disclosure submission confirmation message.
Give the user links to view the submitted disclosure or to view list of all the
user's disclosures.

This page is using HNCTemplate.jsp as a  template that supports and layout the
components in a standard fashion.
-->
<%@ page  errorPage="ErrorPage.jsp"
	import="edu.mit.coeus.utils.CoeusConstants"%>
	
<%@ taglib uri='/WEB-INF/struts-template.tld' 	prefix='template' %>
<%@ taglib uri='/WEB-INF/struts-bean.tld' prefix='bean' %>
<%@ taglib uri='/WEB-INF/struts-logic.tld' prefix='logic' %>
<%@ taglib uri='/WEB-INF/privilege.tld' prefix='coeusPrivilege' %>

<!--check user login status -->
<coeusPrivilege:checkLogin forwardName="<%=CoeusConstants.LOGIN_COI_KEY%>" />

<template:insert template='HNCTemplate.jsp'>
  
  <%-- Supply the Title information to the HNCTemplate.jsp --%>
  <template:put name='title'> <bean:message key='discSubmitSuccess.title'/> </template:put>
  
  <%-- Supply the Header information to the HNCTemplate.jsp --%>
  <template:put name='header' content='CoeusHeader.jsp' />
  
  <%-- Supply the Navigation Page  information to the HNCTemplate.jsp --%>
  <template:put name='navbar' content='COILeftPane.jsp' />
  
  <%-- Supply the Actual Content information to the HNCTemplate.jsp  that is more dynamic--%>
  <template:put name='actualContent' content='DiscSubmitSuccessContent.jsp'/>
</template:insert>