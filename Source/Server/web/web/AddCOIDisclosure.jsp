<%--
/*
 * @(#)AddCOIDisclosure.jsp	1.0	2002/06/11	23:12:30
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 *
 * @author  RaYaKu
 */
--%>
<%@ page  errorPage="ErrorPage.jsp"
	import="edu.mit.coeus.utils.CoeusConstants"%>
<!--
A View component that displays the all COI disclosures based up on the user
selection search criteria. In the beginning the page shows the all COI disclosures
for status "PI Reviewed" as default search results.

This page is using HNCTemplate.jsp as a  template that supports and layout the
components in a standard fashion.
-->
<%@ taglib uri='/WEB-INF/struts-template.tld' 	prefix='template' %>
<%@ taglib uri='/WEB-INF/struts-bean.tld' 	prefix='bean' %>
<%@ taglib uri='/WEB-INF/privilege.tld' 	prefix='coeusPrivilege' %>

<%-- check user login status --%>
<coeusPrivilege:checkLogin forwardName="<%=CoeusConstants.LOGIN_COI_KEY%>" />

<template:insert template='HNCTemplate.jsp'>

  <%-- Supply the Title information to the HNCTemplate.jsp --%>
  <template:put name='title'> <bean:message key='addCOIDisclosure.title'/> </template:put>
  
  <%-- Supply the Header information to the HNCTemplate.jsp --%>
  <template:put name='header' content='CoeusHeader.jsp' />
  
  <%-- Supply the Navigation Page  information to the HNCTemplate.jsp --%>
  <template:put name='navbar' content='COILeftPane.jsp' />

  
  <%-- Supply the Actual Content information to the HNCTemplate.jsp  that is more dynamic--%>
  <template:put name='actualContent' content='AddCOIDisclosureContent1.jsp'/>
</template:insert>