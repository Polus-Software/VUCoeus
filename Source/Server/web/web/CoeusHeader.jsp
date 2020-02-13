<%--
/*
 * @(#)CoeusHeader.jsp	1.0 2002/05/08 10:15:23 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  RaYaKu
 */
--%>
<%@page errorPage = "ErrorPage.jsp" %>
<%--
A Jsp page containing the Coeus Header information in Html format 
and this view component output is included in other jsp(parent) component.
--%>
<%@ taglib uri="/WEB-INF/struts-bean.tld"     prefix="bean" %>
<%@ include file="CoeusContextPath.jsp"  %>

<img src="<bean:write name="ctxtPath"/>/images/coeusheader.gif" height="111" usemap="#Map2" border="0" alt="OSP Banner"><map name="Map2"><area shape="rect" coords="721,57,773,72" href="http://web.mit.edu" alt="MIT Home" title="MIT Home"><area shape="rect" coords="722,73,773,87" href="http://web.mit.edu/osp/www/search.htm" alt="Search" title="Search"><area shape="rect" coords="723,89,774,106" href="http://web.mit.edu/osp/www/contactus.htm" alt="Contact Us" title="Contact Us"><area shape="poly" coords="127,27,162,27,179,38,244,38,252,55,317,56,330,70,407,70,409,103,128,103" href="http://web.mit.edu/osp/www/index.html" alt="OSP Home" title="OSP Home"></map>
