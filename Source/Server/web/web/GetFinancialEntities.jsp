<%--
/*
 * @(#)GetFinancialEntities.jsp 1.0	05/18/2002   08:00:00 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  RaYaKu
 */
--%>
<%--
A View component that displays the all Financial Entities for this user.

This page is using HNCTemplate.jsp as a  template that supports and layout the
components in a standard fashion.
--%>
<%@ page  errorPage="ErrorPage.jsp"
	import="edu.mit.coeus.utils.CoeusConstants"%>
	
<%@ taglib uri='/WEB-INF/struts-template.tld' 	prefix='template' %>
<%@ taglib uri='/WEB-INF/struts-bean.tld' 		prefix='bean' %>
<%@ taglib uri='/WEB-INF/struts-logic.tld' 		prefix='logic' %>
<%@ taglib uri='/WEB-INF/response.tld'	 	  		prefix='res' %>
<%@ taglib uri='/WEB-INF/session.tld' 	 			prefix='sess' %>
<%@ taglib uri='/WEB-INF/privilege.tld' 		prefix='coeusPrivilege' %>

<!-- check user login status -->
<coeusPrivilege:checkLogin forwardName="<%=CoeusConstants.LOGIN_COI_KEY%>" />

<jsp:useBean id='allCOIDisclosures' class='java.util.Vector' scope='session' />
<jsp:useBean id='allCOIDisclosuresStatus' class='java.util.ArrayList'  scope='session'/>

<template:insert template='HNCTemplate.jsp'>
  <%-- Supply the Title information to the HNCTemplate.jsp --%>
  <template:put name='title'> <bean:message key='financialEntity.title'/> </template:put>
  
  <%-- Supply the Header information to the HNCTemplate.jsp --%>
  <template:put name='header' content='CoeusHeader.jsp' />
  
  <%-- Supply the Navigation Page  information to the HNCTemplate.jsp --%>
  <template:put name='navbar' content='COILeftPane.jsp' />
  
  <%-- Supply the Actual Content information to the HNCTemplate.jsp  that is more dynamic--%>
  <template:put name='actualContent' content='GetFinancialEntitiesContent.jsp'/>
</template:insert>