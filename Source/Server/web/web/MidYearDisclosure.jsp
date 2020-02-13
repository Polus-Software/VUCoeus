<%--
/*
 * @(#)MidYearDisclosure.jsp	1.0 2002/06/10	17:14:19
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  RaYaKu
 */
--%>
<%--
A View component that provides options/choices for user to create
a new disclosure.

This page is using HNCTemplate.jsp as a  template that supports and layout the
components in a standard fashion.
--%>

<%@ page  errorPage="ErrorPage.jsp"
	import="edu.mit.coeus.utils.CoeusConstants"%>
	
<%@ taglib uri='/WEB-INF/struts-template.tld' 	prefix='template' %>
<%@ taglib uri='/WEB-INF/struts-bean.tld' 	prefix='bean' %>
<%@ taglib uri='/WEB-INF/privilege.tld' 	prefix='coeusPrivilege' %>

<!-- check user login status -->
<coeusPrivilege:checkLogin forwardName="<%=CoeusConstants.LOGIN_COI_KEY%>" />

<template:insert template='HNCTemplate.jsp'>
  <template:put name='title'> <bean:message key='newCOIDisclosure.title'/> </template:put>
  
  <template:put name='header' content='CoeusHeader.jsp' />
  
  <template:put name='navbar' content='COILeftPane.jsp' />  
  
  <template:put name='actualContent' content='MidYearDisclosureContent.jsp'/>
</template:insert>