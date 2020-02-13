<%--
/*
 * @(#)FinancialEntityDetails.jsp	1.0 2002/06/03	10:48:49
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  RaYaKu
 */
--%>
<%--
A View component that dispalys all components for Adding/Modifying the Financial Entity Detials.

This page is using HNCTemplate.jsp as a  template that supports and layout the
components in a standard fashion.
--%>

<%@ page  errorPage="ErrorPage.jsp"
	import="edu.mit.coeus.utils.CoeusConstants"%>
	
<%@ taglib uri='/WEB-INF/struts-template.tld' 	prefix='template' %>
<%@ taglib uri='/WEB-INF/struts-bean.tld' 		prefix='bean' %>
<%@ taglib uri='/WEB-INF/struts-logic.tld' 		prefix='logic' %>
<%@ taglib uri='/WEB-INF/privilege.tld' 		prefix='coeusPrivilege' %>

<!-- check user login status -->
<coeusPrivilege:checkLogin forwardName="<%=CoeusConstants.LOGIN_COI_KEY%>" />

<%	System.out.println("begin FinancialEntityDetailsContent.jsp");	%>

<template:insert template='HNCTemplate.jsp'>
  
  <!-- Supply the Title information to the HNCTemplate.jsp -->
  <template:put name='title'> <bean:message key='financialEntityDetails.title'/> </template:put>
  
  <!-- Supply the Header information to the HNCTemplate.jsp -->
  <template:put name='header' content='CoeusHeader.jsp' />
  
  <!-- CASE #1374 Additional options needed for left pane -->
  <!-- Supply the Navigation Page  information to the HNCTemplate.jsp-->
	<logic:present name="frmFinancialEntityDetailsForm" property="actionFrom" >
	<logic:equal name="frmFinancialEntityDetailsForm" property="actionFrom" value="coiFinEntity" >
	  	<template:put name='navbar' content='COILeftPane.jsp' />
	</logic:equal>
	<logic:notEqual name="frmFinancialEntityDetailsForm" property="actionFrom" value="coiFinEntity" >
		<template:put name='navbar' content='AnnFinEntityDetailsLeftPane.jsp' />
	</logic:notEqual>
	
</logic:present>
  
  <!-- Supply the Actual Content information to the HNCTemplate.jsp  that is more dynamic-->
  <template:put name='actualContent' content='FinancialEntityDetailsContent1.jsp'/>

</template:insert>