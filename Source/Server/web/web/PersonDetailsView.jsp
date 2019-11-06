<%--
/*
 * @(#) PersonDetailsView.jsp 1.0 2002/06/10
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  Geo Thomas.
 */
--%>
<%-- 
    This Page is to display selected person's details.
    This page is using HNCTemplate.jsp as a  template that supports and layout the 
    components in a standard fashion.
--%>
<%@ page import="java.util.*,
		java.text.*,
		edu.mit.coeus.coi.bean.*,
		edu.mit.coeus.utils.CoeusConstants" 
		errorPage="ErrorPage.jsp"%>

<%@ taglib uri='/WEB-INF/struts-template.tld' prefix='template' %>
<%@ taglib uri='/WEB-INF/struts-bean.tld' prefix='bean' %>
<%@ taglib uri='/WEB-INF/struts-logic.tld' prefix='logic' %>
<%@ taglib uri='/WEB-INF/response.tld' prefix='res' %>
<%@ taglib uri='/WEB-INF/session.tld' prefix='sess' %>

<template:insert template='HNCTemplate.jsp'>
	
	<%-- Supply the Title information to the HNCTemplate.jsp --%>
  <template:put name='title'> <bean:message key='personDetailsView.title'/> </template:put>
  
  <%-- Supply the Header information to the HNCTemplate.jsp --%>
  <template:put name='header' content='CoeusHeader.jsp' />
  
  <%-- Supply the Navigation Page  information to the HNCTemplate.jsp --%>
  <template:put name='navbar' content='COILeftPane.jsp' />
  
  <%-- Supply the Actual Content information to the HNCTemplate.jsp  that is more dynamic--%>
  <template:put name='actualContent' content='PersonDetailsContent.jsp'/>
  
</template:insert>
