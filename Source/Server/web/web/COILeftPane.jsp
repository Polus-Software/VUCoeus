<%--
/*
 * @(#)COILeftPane.jsp  1.0	2002/05/08	11:45:12
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 
 * @author  RaYaKu 
 */
--%>
<%--
Coeus application Navigation page which helps the user in working coeus application 
and situated in Left hand side of a page, This page output is included in other view
component in runtime.  
It provides links to create  new disclosure and to view Financial Entities and COI Diclsoures.

Note*. The Navigation options(links) will vary on the user role (previlege) basis.
--%>
<%@ page errorPage="ErrorPage.jsp" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/session.tld" prefix="sess" %>
<%@ taglib uri="/WEB-INF/privilege.tld" prefix="priv" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<!-- CASE #665 Begin -->
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<!-- CASE #665 End -->

<%	System.out.println("inside COILeftPane.jsp");	%>
<%@ include file="CoeusContextPath.jsp"  %>
<jsp:useBean id = "userprivilege" class = "java.lang.String" scope="session"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0" 
	background="<bean:write name="ctxtPath"/>/images/coeusmenu-03.gif"> 
<tr>
	<td height="2" >&nbsp;</td>
</tr>
<tr>
<td>
<table width="100%" border="0" cellspacing="2" cellpadding="2" 
	background="<bean:write name="ctxtPath"/>/images/coeusmenu-03.gif"> 
<tr>
	<td>
		<html:link page="/addFinEntity.do?actionFrom=coiFinEntity">
		<bean:message key="coiLeftPane.addFinEnt"/>
		</html:link>
	</td>
</tr>
<tr>
	<td height="5">&nbsp;</td>
</tr>	
<tr>
    <td>
    <html:link page="/getFinEntities.do">
	    <bean:message key="coiLeftPane.reviewFinEnt"/>
	</html:link>
    </td>
</tr>
<tr>
	<td height="5">&nbsp;</td>
</tr>
<tr>
<td>
<!-- CASE #1374 Change to go directly to add coi discl page. -->
 <html:link page="/addCOIDisclosure.do"> 
  <bean:message key="coiLeftPane.addDisclosure"/>
 </html:link>
</td>
</tr>
<!-- CASE #1374 Begin -->
<tr>
	<td height="5">&nbsp;</td>
</tr>
<tr> 
<td>
 <html:link page="/getcoidisclosure.do">
  <bean:message key="coiLeftPane.reviewDisclosures"/>
  </html:link>	      
</td>
</tr>
<!-- CASE #1374 End -->
<!-- CASE #665 Comment Begin -->
<%--
<priv:hasOSPRight name="hasOspRightToView" value="<%=Integer.parseInt(userprivilege)%>">
<tr>
<td>
 <html:link page="javascript:openSearchWin('<bean:write name="ctxtPath"/>/coeusSearch.do?searchname=personSearch&fieldName=personId','person');">
  <img src="<bean:write name="ctxtPath"/>/images/sperson.gif" width="129"  border="0">
 </html:link>
</td>
</tr>
</priv:hasOSPRight>
--%>
<!-- CASE #665 Comment End -->
<!-- CASE #665 Begin -->
<logic:notEqual name="userprivilege" value="">
	<priv:hasOSPRight name="hasOspRightToView" value="<%=Integer.parseInt(userprivilege)%>">
		<tr>
			<td height="5">&nbsp;</td>
		</tr>
		<tr>
		<td>
		 <a href="javascript:openSearchWin('<bean:write name="ctxtPath"/>/coeusSearch.do?searchname=personSearch&fieldName=personId','person');">
		  <bean:message key="coiLeftPane.selectPerson"/>
		 </a>
		</td>
		</tr>
	</priv:hasOSPRight>
</logic:notEqual>
<!-- CASE #665 End -->
<!-- CASE #1046 Begin -->
<logic:present name="viewPendingDisc" scope="session">
		<tr>
			<td height="5">&nbsp;</td>
		</tr>
		<tr>
		<td>
		 <html:link page="/viewPendingDisc.do">
		  <bean:message key="coiLeftPane.viewPendingDisc"/>
		 </html:link>
		</td>
		</tr>
</logic:present>
<!-- CASE #1046 End -->
<tr>
	<td height="5">&nbsp;</td>
</tr>
<tr>
<td>
 <html:link forward="loginCOI">
  <bean:message key="coiLeftPane.COIHome"/>
 </html:link>
</td>
</tr>
<tr>
	<td height="5">&nbsp;</td>
</tr>
<tr>
<td>
 <html:link forward="welcome">
  <bean:message key="coiLeftPane.coeusHome"/>
 </html:link>
</td>
</tr>
<tr>
	<td height="5">&nbsp;</td>
</tr>
<tr>
<td>
	<a href="javascript:openwintext('<%=request.getContextPath()%>/web/COIInfo.html','coiLeftPane1');">
	<bean:message key="coiLeftPane.FAQ"/>
	</a>
</td>
</tr>
<tr>
<td height="30">&nbsp;</td>
</tr>
</table>
</td>
</tr>
<tr valign="bottom">
<td>
<p><img src="<bean:write name="ctxtPath"/>/images/bottom.gif" width="129" height="5"></p>
</td>
</tr>
</table>   