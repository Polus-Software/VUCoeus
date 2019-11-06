<%--
/*
 * @(#)AnnDisclosureFELeftPane.jsp 1.0 2002/05/08
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 *
 * @author  Phaneendra Kumar . B
 */
--%>


<%@page errorPage = "ErrorPage.jsp" %>
<%--
Coeus application Navigation page which helps the user in navigating to other
and situated in Left hand side of a page, This page output is included in AnnDisclosureFEMain view
component at runtime.
--%>
<%@ taglib uri="/WEB-INF/struts-bean.tld"     prefix="bean" %>
 <%@ include file="CoeusContextPath.jsp"  %>
 <%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
 <%@ taglib uri='/WEB-INF/struts-logic.tld' 	prefix='logic' %>

<table  width="100%" border="0" cellspacing="0" cellpadding="0" 
		background="<bean:write name='ctxtPath'/>/images/coeusmenu-03.gif">
 <tr>
	<td height="2">&nbsp;</td>
 </tr>
<tr>
<td>
	<table  width="100%" border="0" cellspacing="2" cellpadding="2" 
			background="<bean:write name='ctxtPath'/>/images/coeusmenu-03.gif">
		
	 <logic:notPresent name="annDiscCertification">
	 <logic:notPresent name="annDiscConfirmation">
	   <tr valign="top">
	     <td>
		<html:link page="/addFinEntity.do?actionFrom=AnnualFE">
		<%--<img src="<bean:write name='ctxtPath'/>/images/addfinancialentity.gif" width="129" height="21" border="0">--%>
		<bean:message key="annDiscFELeftPane.addFinEnt"/>
		</html:link> </td>
	  </tr>
	 <tr>
		<td height="5">&nbsp;</td>
	 </tr>  
	 </logic:notPresent>
	  </logic:notPresent>
	<tr>
	<td>
	 <html:link forward="loginCOI">
	  <%--<img src="<bean:write name='ctxtPath'/>/images/COIHome.gif" width="129" height="19" border="0">--%>
	  <bean:message key="annDiscFELeftPane.COIHome"/>
	 </html:link>
	</td>
	</tr>
	 <tr>
		<td height="5">&nbsp;</td>
	 </tr>
	<tr>
	<td>
	 <html:link forward="welcome">
	  <%--<img src="<bean:write name='ctxtPath'/>/images/coeusHome.gif" width="129" height="19" border="0">--%>
	  <bean:message key="annDiscFELeftPane.coeusHome"/>
	 </html:link>
	</td>
	</tr>
</table>
</td>
</tr>

<tr>
 <td height="30">&nbsp;</td>
</tr>
  <tr valign="bottom" bgcolor="#CC9999">
    <td><img src="<bean:write name='ctxtPath'/>/images/bottom.gif"
      width="129" height="5"></td>
  </tr>
</table>
