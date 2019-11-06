<%--
/*
 * @(#)ViewPendingDisc.jsp	1.0  05/10/2002
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  RaYaKu
 */
--%>
<%--
A View component that displays the all COI disclosures based up on the user
selection search criteria. In the beginning the page shows the all COI disclosures
for status "PI Reviewed" as default search results.

This page is using HNCTemplate.jsp as a  template that supports and layout the
components in a standard fashion.
--%>
<%@ page  errorPage="ErrorPage.jsp" %>
<%@ taglib uri='/WEB-INF/struts-template.tld' 	prefix='template' %>
<%@ taglib uri='/WEB-INF/struts-bean.tld' prefix='bean' %>
<%@ taglib uri='/WEB-INF/struts-logic.tld' prefix='logic' %>

<jsp:useBean id='collPendingDisc' class='java.util.Vector' scope='session' />

<%	System.out.println("Inside ViewPendingDisc.jsp");	%>

<!--check user login status -->
<coeusPrivilege:checkLogin />

<template:insert template='HNCTemplate.jsp'>

  <%-- Supply the Title information to the HNCTemplate.jsp --%>
  <template:put name='title'> <bean:message key='viewPendingDisc.title'/> </template:put>
  
  <%-- Supply the Header information to the HNCTemplate.jsp --%>
  <template:put name='header' content='CoeusHeader.jsp' />
  
  <%-- Supply the Navigation Page  information to the HNCTemplate.jsp --%>
  <template:put name='navbar' content='COILeftPane.jsp' />
  
  <%-- Supply the Actual Content information to the HNCTemplate.jsp  that is more dynamic--%>
  <template:put name='actualContent' content='ViewPendingDiscContent.jsp'/>
  
</template:insert>