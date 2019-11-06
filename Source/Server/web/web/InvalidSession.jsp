<%--
/*
 * @(#)InvalidSession.jsp	1.0	2002/06/11	23:12:30
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 *
 * @author  RaYaKu
 */
--%>

<!--
Invalid Session page.  Give user an OK button to click to renew user validation.

This page is using HNCTemplate.jsp as a  template that supports and layout the
components in a standard fashion.
-->
<%@ taglib uri='/WEB-INF/struts-template.tld' 	prefix='template' %>
<%@ taglib uri='/WEB-INF/struts-bean.tld' 		prefix='bean' %>

<template:insert template='HNCTemplate.jsp'>
  
  <%-- Supply the Title information to the HNCTemplate.jsp --%>
  <template:put name='title'> <bean:message key='sessionexpired.title'/> </template:put>
  
  <%-- Supply the Header information to the HNCTemplate.jsp --%>
  <template:put name='header' content='CoeusHeader.jsp' />
  
  <%-- Supply the Navigation Page  information to the HNCTemplate.jsp --%>
  <!-- Don't use any left pane navigation bar for session expired page.  -->
  
  <%-- Supply the Actual Content information to the HNCTemplate.jsp  that is more dynamic--%>
  <template:put name='actualContent' content='InvalidSessionContent.jsp'/>

</template:insert>