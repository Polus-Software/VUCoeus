<%--
/*
 * @(#)COIDisclosureDetailsView.jsp 1.0 2002/05/16 16:36:12 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  RYK
 */
--%>
<%--
A view component that displays the Details of COI disclosure
and follows the HNCTemplate.jsp to layout the title,header,Navigationpage
and COIDisclosureDetails in a page.
--%>

<%@ page  errorPage="ErrorPage.jsp"
	import="edu.mit.coeus.utils.CoeusConstants"%>

<%@ taglib uri='/WEB-INF/struts-template.tld' prefix='template' %>
<%@ taglib uri='/WEB-INF/struts-bean.tld' prefix='bean' %>

<template:insert template='HNCTemplate.jsp'>
  <template:put name='title' ><bean:message key='viewCOIDisclosureDetails.title' /> </template:put>
  
  <template:put name='header' content='CoeusHeader.jsp' />
  <%-- Supply the Navigation page information --%>
  
  <template:put name='navbar' content='COILeftPane.jsp' />
  
  <template:put name='actualContent' content='COIDisclosureDetailsContent1.jsp'/>
</template:insert>