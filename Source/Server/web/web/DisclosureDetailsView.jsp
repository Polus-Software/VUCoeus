<%--
/*
 * @(#)DisclosureDetailsView.jsp 1.0 2002/05/20 12:18:12 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  RaYaKu
 */
--%>
<%--
This JSP file is for listing the All Disclosures of the user and uses a HNCTemplate.jsp as a template
to layout/include Title,Header,Navigation and DislclosureDetails information.
--%>
<%@ page  errorPage="ErrorPage.jsp"
	import="edu.mit.coeus.utils.CoeusConstants"%>

<%@ taglib uri='/WEB-INF/struts-template.tld' prefix='template' %>
<%@ taglib uri='/WEB-INF/struts-bean.tld' prefix='bean' %>
<%@ taglib uri='/WEB-INF/privilege.tld' prefix='coeusPrivilege' %>

<!-- check user login status -->
<coeusPrivilege:checkLogin forwardName="<%=CoeusConstants.LOGIN_COI_KEY%>" />

<template:insert template='HNCTemplate.jsp'>
  <template:put name='title'> <bean:message key='viewDisclosureDetails.title'/> </template:put>
  
  <template:put name='header' content='CoeusHeader.jsp' />
  
  <%-- Supply the Navigation page information --%>

  <template:put name='navbar' content='COILeftPane.jsp' />

  
  <template:put name='actualContent' content='DisclosureDetailsViewContent1.jsp'/>
</template:insert>