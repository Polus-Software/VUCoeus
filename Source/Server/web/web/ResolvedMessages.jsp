<%--
/*
 * @(#)ResolvedMessages.jsp
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on May10, 2002, 8:00 AM
 *
 * @author  RYK
 * @version 1.0
 */
--%>
<%--
A View component that displays the all COI disclosures based up on the user
selection search criteria. In the beginning the page shows the all COI disclosures
for status "PI Reviewed" as default search results.

This page is using HNCTemplate.jsp as a  template that supports and layout the
components in a standard fashion.
--%>
<%@ page  errorPage="ErrorPage.jsp"
	import="edu.mit.coeus.utils.CoeusConstants"%>
	
<%@ taglib uri='/WEB-INF/struts-template.tld' prefix='template' %>
<%@ taglib uri='/WEB-INF/struts-bean.tld' prefix='bean' %>
<%@ taglib uri='/WEB-INF/struts-logic.tld' prefix='logic' %>
<%@ taglib uri='/WEB-INF/response.tld' prefix='res' %>
<%@ taglib uri='/WEB-INF/request.tld' prefix='req' %>
<%@ taglib uri='/WEB-INF/session.tld' prefix='sess' %>
<%@ taglib uri='/WEB-INF/privilege.tld' prefix='coeusPrivilege' %>

<!-- CASE #665 Begin -->
<req:setAttribute name="usePropDevErrorPage">true</req:setAttribute>
<!-- CASE #665 End -->	

<coeusPrivilege:checkLogin 
	requestedURL="getInboxMessages.do?messageType=resolved" 
	forwardName="<%=CoeusConstants.LOGIN_KEY%>" />

<!-- CASE #599 Comment Begin -->
<%--<jsp:include page="CoeusCacheRemove.jsp" flush="true" />--%>
<!-- CASE #599 Comment End -->

<template:insert template='HNCTemplateNoLeftPane.jsp'>
  <%-- Supply the Title information to the HNCTemplateNoLeftPane.jsp --%>
  <template:put name='title'> <bean:message key='resolved.title'/> </template:put>

  <%-- Supply the Header information to the HNCTemplateNoLeftPane.jsp --%>
  <template:put name='header' content='CoeusHeader.jsp' />

  <%-- Supply the Actual Content information to the HNCTemplateNoLeftPane.jsp  that is more dynamic--%>

  <template:put name='actualContent' content='ResolvedMessagesContent.jsp' />

</template:insert>
