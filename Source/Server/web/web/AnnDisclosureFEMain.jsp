<%--
/*
 * @(#)AnnDisclosureFEMain.jsp 1.0 2002/06/03
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  Phaneendra Kumar Bhyri.
 */
--%>

<%--
A View component that displays all the Financial Entities not yet reviewed

This page is using HNCTemplate.jsp as a  template that supports and layout the
components in a standard fashion.
--%>
<%@ page  errorPage="ErrorPage.jsp"
	import="edu.mit.coeus.utils.CoeusConstants"%>

<%@ taglib uri = '/WEB-INF/struts-template.tld' prefix = 'template' %>
<%@ taglib uri = '/WEB-INF/struts-bean.tld' prefix = 'bean' %>
<%@ taglib uri = '/WEB-INF/struts-logic.tld' prefix = 'logic' %>
<%@ taglib uri = '/WEB-INF/response.tld' prefix = 'res' %>
<%@ taglib uri = '/WEB-INF/session.tld' prefix = 'sess' %>
<%@ taglib uri='/WEB-INF/privilege.tld' prefix='coeusPrivilege' %>

<%-- check user login status --%>
<coeusPrivilege:checkLogin forwardName="<%=CoeusConstants.LOGIN_COI_KEY%>" />

<template:insert template = "HNCTemplate.jsp" >
<%-- supply the title information to the HNCTemplate.jsp--%>
<template:put name = 'title'><bean:message key = 'annDisFinancialEntity.title' /> </template:put>

<%-- supply the header information to the HNCTemplate.jsp  --%>
<template:put name = 'header' content = 'CoeusHeader.jsp' />

<%-- supply the navigation pane to the HNCTemplate.jsp --%>
 <template:put name = 'navbar' content = 'AnnDisclosureFELeftPane.jsp' />

<%-- supply the actual content jsp with financialEntitites --%>
 <template:put name = 'actualContent' content = 'AnnDiscFEContent.jsp' />
</template:insert>