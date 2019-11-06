<%--
/*
 * @(#)ViewFinancialEntity.jsp   1.0	2002/05/31	17:44:12
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  RaYaKu
 */
--%>
<%--
A View component that displays details of one Financial Entity based up on the user
selection search criteria.

This page is using HNCTemplate.jsp as a  template that supports and layout the
components in a standard fashion.
--%>

<%@ page  errorPage="ErrorPage.jsp"
	import="edu.mit.coeus.utils.CoeusConstants"%>
	
<%@ taglib uri='/WEB-INF/struts-template.tld' 	prefix='template' %>
<%@ taglib uri='/WEB-INF/struts-bean.tld' 	prefix='bean' %>
<%@ taglib uri='/WEB-INF/struts-logic.tld' 	prefix='logic' %>
<%@ taglib uri='/WEB-INF/privilege.tld' 	prefix='coeusPrivilege' %>

<!-- check user login status -->
<coeusPrivilege:checkLogin forwardName="<%=CoeusConstants.LOGIN_COI_KEY%>" />

<template:insert template='HNCTemplate.jsp'>

  <%-- Supply the Title information to the HNCTemplate.jsp --%>
  <template:put name='title'><bean:message key="viewFinEntity.title" /></template:put>
  
  <%-- Supply the Header information to the HNCTemplate.jsp --%>
  <template:put name='header' content='CoeusHeader.jsp' />
  
  <%-- Supply the Navigation Page  information to the HNCTemplate.jsp
   If user is viewing this page when he is in COI Disclosure module then
   show him FinEntityDetailsLeftPane.jsp, otherwise show AnnFinEntityDetailsLeftPane.jsp
  --%>
  <logic:present name="actionFrom" scope="request" >
  		<logic:equal name="actionFrom" value="coiFinEntity" >
  			<template:put name='navbar' content='COILeftPane.jsp' />
  		 </logic:equal>
   		 <logic:notEqual name="actionFrom" value="coiFinEntity" >
			<template:put name='navbar' content='AnnFinEntityDetailsLeftPane.jsp' />
  		 </logic:notEqual>
  </logic:present>
  
  <%-- Supply the Actual Content information to the HNCTemplate.jsp  that is more dynamic--%>
  <template:put name='actualContent' content='ViewFinancialEntityContent1.jsp'/>
  
 </template:insert>