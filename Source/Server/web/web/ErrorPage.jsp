	
<!--
/*
 * @(#)ErrorPage.jsp	1.0	2002/06/11	23:12:30
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
Error Page for the web application.

This page is using HNCTemplate.jsp as a  template that supports and layout the
components in a standard fashion.
-->
<%@page isErrorPage="true" %>

<%@ taglib uri='/WEB-INF/struts-template.tld' 	prefix='template' %>
<%@ taglib uri='/WEB-INF/struts-bean.tld' 	prefix='bean' %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%	System.out.println("inside ErrorPage.jsp");
%>
<jsp:include page="CoeusCacheRemove.jsp" flush="true" />
<template:insert template='HNCTemplate.jsp'>

  <!-- Supply the Title information to the HNCTemplate.jsp -->
  <template:put name='title'> <bean:message key='errorPage.title'/> </template:put>
  
  <!-- Supply the Header information to the HNCTemplate.jsp -->
  <template:put name='header' content='CoeusHeader.jsp' />
  
  <!-- Supply the Navigation Page  information to the HNCTemplate.jsp -->
  <!-- CASE #665 If a JSP or action class from PropDev has generated the error, use PropDev left nav bar -->
  <%
  	if(request.getAttribute("usePropDevErrorPage") != null){

  %>
  	<template:put name='navbar' content='CoeusLeftPane.jsp' />
  <%	
  	}else {
  %>
  	<template:put name='navbar' content='COILeftPane.jsp' />  
  <%
	}
  %>
  <!-- Supply the Actual Content information to the HNCTemplate.jsp  that is more dynamic-->
  <template:put name='actualContent' content='ErrorPageContent.jsp'/>
  
</template:insert>