<%--
/*
 * @(#)AnnDisclosureLeftPane.jsp 1.0 2002/06/05
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  Phaneendra Kumar . B
 */
--%>

<%@page errorPage = "ErrorPage.jsp" %>
<%--
Coeus application Navigation page which helps the user in navigating to other
and situated in Left hand side of a AnnualDisclosurespage, This page output is included in AnnDisclosureFEMain view
component at runtime.
--%>
<%@ taglib uri="/WEB-INF/struts-html.tld"     prefix="html" %>
<%@ taglib uri= '/WEB-INF/struts-bean.tld'     prefix='bean' %>
<%@ taglib uri= '/WEB-INF/struts-logic.tld'     prefix='logic' %>
<%@ taglib uri="/WEB-INF/session.tld" prefix="sess" %>
<%@ include file="CoeusContextPath.jsp"  %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"	  prefix="coeusUtils" %>
<!--
get all the Financial entities to display in the left pane of the Annual Disclosures
page.
-->
<jsp:useBean id="allAnnualDiscEntities" scope="session" class="java.util.Vector" />

<bean:size id="totalAnnualDiscEntities" name="allAnnualDiscEntities" />
<table width="100%" border="0" cellspacing="0" cellpadding="0" 
		background="<bean:write name="ctxtPath"/>/images/coeusmenu-03.gif" >
  <tr>
  	<td height="30">&nbsp;</td>
  </tr>
  <tr>
  	<td>

<%--
<TD>
<img src="<bean:write name="ctxtPath"/>/images/coeusmenu-03.gif" width="5">
</TD>
--%>

<table width="100%" border="0" cellspacing="0" cellpadding="2" 
background="<bean:write name="ctxtPath"/>/images/coeusmenu-03.gif" >
  <!-- CASE #912 Look for all AnnualDiscEntities in session instead of request -->
  <logic:present name="allAnnualDiscEntities"  scope="session" >
  <logic:iterate id="entity"  name="allAnnualDiscEntities" >
  <tr>
    <td width="20" align="right" valign="top" height="10" >
      <!-- CASE #410 Begin -->
      <logic:present name="entity" property="annDisclUpdated">
      	<logic:equal name="entity" property="annDisclUpdated" value="Y" >
      		<img src="<bean:write name="ctxtPath"/>/images/checked.gif" >
      	</logic:equal>
      	<logic:equal name="entity" property="annDisclUpdated" value="N" >
      		&nbsp;
      	</logic:equal>
      </logic:present>
      </td>
      <!-- CASE #410 End -->
      <td valign="top">
              <a href="<bean:write name="ctxtPath"/>/annualDisclosures.do?entityNumber=<bean:write name="entity" property="number" />"><bean:write name="entity" property="name" /></a>
            
       </td>
    </td>
  </tr>
  <tr>
    <td height="2" colspan="2">
    <image src="<bean:write name='ctxtPath'/>/images/coeusmenu-03.gif" width="1">
    </td>
  </tr>

  </logic:iterate> 
  </logic:present> 
  <logic:equal name="totalAnnualDiscEntities"  value="0" >
  </logic:equal>
  </table>
<table width="100%" border="0" cellspacing="0" cellpadding="0" 
		background="<bean:write name="ctxtPath"/>/images/coeusmenu-03.gif" >
<tr>
	<td height="30">&nbsp;</td>
</tr>
  <tr valign="bottom">
    <td>
      <img src="<bean:write name='ctxtPath'/>/images/bottom.gif" >
    </td>
  </tr>  
</table>
</TD>
</TR>
</TABLE>
