<%--
/*
 * @(#)AnnFinEntityDetailsLeftPane.jsp	1.0	2002/06/08 16:12:11
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  RaYaKu
 */
--%>
<%--
Coeus application Navigation page which helps the user in working coeus application
and situated in Left hand side of a page, This page output is included in other view
component in runtime.

When user likes to edit/add a financial entity and coming from any of annual disclosure pages,
this left pane would be displayed.

Note*. The Navigation options(links) will vary on the user role (previlege) basis.
--%>
<%@page errorPage = "ErrorPage.jsp" %>
	
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/privilege.tld" prefix="priv" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ include file="CoeusContextPath.jsp"  %>

<jsp:useBean id = "userprivilege" class = "java.lang.String" scope="session"/>

<table width="100%" border="0" cellspacing="0" cellpadding="0" 
	background="<bean:write name='ctxtPath'/>/images/coeusmenu-03.gif">

<tr>
	<td height="5">&nbsp;</td>
</tr>
<tr>
  <td>
  	<table width="100%" cellpadding="2" cellspacing="2"
  		background="<bean:write name="ctxtPath"/>/images/coeusmenu-03.gif"> 
  	
  	<tr>
  	<!-- CASE #1374 Comment Begin -->
  	<%--
  	<td>
		<html:link page="/getAnnDiscPendingFEs.do">
		  <img src="<bean:write name='ctxtPath'/>/images/adisclosure.gif" width="129" height="19" border="0">
		</html:link>
	</td>
	--%>
	<!-- CASE #1374 Comment End -->
	<!-- CASE #1374 Begin -->
	<td>
		<logic:present name="actionFrom" scope="request">
			<logic:equal name="actionFrom" value="AnnualFE">
				<html:link page="/getAnnDiscPendingFEs.do?actionFrom=editFinEnt">
				  <bean:message key="annFinEntity.backToAnnDisc"/>
				</html:link>			
			</logic:equal>
			<logic:equal name="actionFrom" value="annDiscCert">
				<html:link page="/annDiscCertification.do?actionFrom=editFinEnt">
				  <bean:message key="annFinEntity.backToAnnDisc"/>
				</html:link>			
			</logic:equal>
			<logic:equal name="actionFrom" value="editDiscl">
				<html:link page="/viewCOIDisclosureDetails.do?action=edit&actionFrom=editFinEnt">
				  <bean:message key="annFinEntity.backToEditDisc"/>
				</html:link>			
			</logic:equal>
			<logic:equal name="actionFrom" value="addDiscl">	
				<html:link page="/addCOIDisclosure.do?actionFrom=editFinEnt">
				  <bean:message key="annFinEntity.backToAddDisc"/>
				</html:link>
				
			</logic:equal>			
		</logic:present>
	</td>
	<!-- CASE #1374 End -->
	</tr>
	</table>
  </td>
</tr>
<tr>
<td height="79">&nbsp;</td>
</tr>
<tr valign="bottom">
<td>
  <p><img src="<bean:write name='ctxtPath'/>/images/bottom.gif" width="129" height="5"></p>
</td>
</tr>
</table>