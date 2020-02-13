<%--
/*
 * @(#)LoginForm.jsp
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on March 08, 2006
 *
 * @author  Geo Thomas
 * @version 1.0
 */
--%>
<%--
This page is using HNCTemplate.jsp as a  template that supports and layout the
components in a standard fashion.
--%>
	
<%@ page  errorPage="ErrorPage.jsp"
	import="edu.mit.coeus.utils.CoeusConstants"%>
<%@ taglib uri='/WEB-INF/struts-template.tld' prefix='template' %>
<%@ taglib uri='/WEB-INF/struts-bean.tld' prefix='bean' %>
<%@ taglib uri='/WEB-INF/struts-html.tld' prefix='html' %>
<%@ taglib uri='/WEB-INF/struts-logic.tld' prefix='logic' %>
<%@ taglib uri='/WEB-INF/response.tld' prefix='res' %>
<%@ taglib uri='/WEB-INF/request.tld' prefix='req' %>
<%@ taglib uri='/WEB-INF/session.tld' prefix='sess' %>
<%@ taglib uri='/WEB-INF/privilege.tld' prefix='coeusPrivilege' %>


<template:insert template='HNCTemplate.jsp'>
  <%-- Supply the Title information to the HNCTemplate.jsp --%>
  <template:put name='title'> <bean:message key='loginForm.title'/> </template:put>


  <%-- Supply the Header information to the HNCTemplate.jsp --%>
  <template:put name='header' content='CoeusHeader.jsp' />


  <%-- Supply the Navigation Page  information to the HNCTemplate.jsp --%>
  <%--<template:put name='navbar' content='CoeusLeftPane.jsp' />--%>


  <%-- Supply the Actual Content information to the HNCTemplate.jsp  that is more dynamic--%>
  <template:put name='actualContent' content='LoginFormContent.jsp'/>

</template:insert>
