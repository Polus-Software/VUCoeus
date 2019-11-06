<%--
/*
 * @(#)NSFProgAnnDets.jsp
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on Dec 18, 2002
 *
 * @author  ES
 * @version 1.0
 */
--%>
<%--
A View component that displays the all NSF program announcements

This page is using CoeusTemplate.jsp as a  template that supports and layout the
components in a standard fashion.
--%>
<%@page  errorPage="ErrorPage.jsp"%>
<%@ taglib uri='/WEB-INF/struts-template.tld' prefix='template' %>
<%@ taglib uri='/WEB-INF/struts-bean.tld' prefix='bean' %>
<%@ taglib uri='/WEB-INF/struts-logic.tld' prefix='logic' %>
<%@ taglib uri='/WEB-INF/response.tld' prefix='res' %>
<%@ taglib uri='/WEB-INF/session.tld' prefix='sess' %>

<jsp:include page="CoeusCacheRemove.jsp" flush="true" />

<template:insert template='CoeusTemplate.jsp'>

<% System.out.println("in NSFProgAnnDets.jsp"); %>

  <%-- Supply the Title information to the CoeusTemplate.jsp --%>
  <template:put name='title'> <bean:message key='nsfProgAnnDets.title'/> </template:put>

  <%-- Supply the Header information to the CoeusTemplate.jsp --%>
  <template:put name='header' content='CoeusHeader.jsp' />

  <%-- Supply the Actual Content information to the CoeusTemplate.jsp  that is more dynamic--%>
  <template:put name='actualContent' content='NSFProgAnnDetsContent.jsp' />

</template:insert>
