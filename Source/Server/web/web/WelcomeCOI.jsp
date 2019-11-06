<!--
/*
 * @(#)WelcomeCOI.jsp
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on June 4, 2002
 *
 * @author  Coeus Dev Team
 * @version 1.0
 */
-->

<!--
Welcome to Conflict of Interest page

This page is using HNCTemplate.jsp as a  template that supports and layout the
components in a standard fashion.
-->

<%@ page  errorPage="ErrorPage.jsp"%>
<%@ taglib uri='/WEB-INF/struts-template.tld' prefix='template' %>
<%@ taglib uri='/WEB-INF/struts-bean.tld' prefix='bean' %>
<%@ taglib uri='/WEB-INF/struts-logic.tld' prefix='logic' %>
<%@ taglib uri='/WEB-INF/response.tld' prefix='res' %>
<%@ taglib uri='/WEB-INF/request.tld' prefix='req' %>
<%@ taglib uri='/WEB-INF/session.tld' prefix='sess' %>
<%@ taglib uri='/WEB-INF/privilege.tld' prefix='coeusPrivilege' %> 

<jsp:include page="CoeusCacheRemove.jsp" flush="true" />
	

<%	System.out.println("inside WelcomeCOI.jsp");	%>


<template:insert template='HNCTemplate.jsp'>
  <%-- Supply the Title information to the HNCTemplate.jsp --%>
  <template:put name='title'> <bean:message key='welcome.title'/> </template:put>

  <%-- Supply the Header information to the HNCTemplate.jsp --%>
  <template:put name='header' content='CoeusHeader.jsp' />

  <%-- Supply the Navigation Page  information to the HNCTemplate.jsp --%>
  <template:put name='navbar' content='COILeftPane.jsp' />


  <%-- Supply the Actual Content information to the HNCTemplate.jsp  that is more dynamic--%>
  <template:put name='actualContent' content='WelcomeCOIContent.jsp'/>

</template:insert>
