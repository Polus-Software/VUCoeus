<%--
/*
 * @(#)ViewFinancialEntityDetails.jsp	1.0	2002/06/10
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  RaYaKu
 */
--%>

<%-- A view component for listing details of Specific Financial Entity --%>
<%@page errorPage = "ErrorPage.jsp" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" 	prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" 	prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" 	prefix="logic" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"	prefix="coeusUtils" %>

<%@ include file="CoeusContextPath.jsp"  %>

<jsp:useBean id="entityDetails" scope="request" class="edu.mit.coeus.coi.bean.EntityDetailsBean" />
<jsp:useBean id="colFinCertificationDetails" 	scope="request" class="java.util.Vector" />
<html:html>
<head>
<title><bean:message key="viewFinEntity.title" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<style type="text/css">
	div {font-family:verdana;font-size:12px}
	a {color:blue;}
	a:active {color:red;}
	a:visited {color:blue;}
	a:hover {color:red;}
	h1 {font-size: 16px}
	h2 {font-size: 14px}
	h3 {font-size: 12px}
	body, td, th, p, a, h4, h3, h2, h1 {font-family:verdana, helvetica, sans-serif}
	body, td, h4 {font-family:verdana, helvetica, sans-serif; font-size:12px}
	.fontBrown {color:#7F1B00}
</style>
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr bgcolor="#CC9999">
      <td height="20">
      	<b>
      		<font color="#FFFFFF">	<bean:message key="viewFinEntity.header" />
      		</font></b>
      	</font>
      </td>
  </tr>
</table>
<!-- Financial Entity Details -->
  <table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr bgcolor="#FBF7F7" valign="top">
      <td width="105" height="25">
        <div align="left"><bean:message key="viewFinEntity.label.name" /></div>
      </td>
      <td width="6" height="25">
        <div align="left">:</div>
      </td>
      <td width="202" height="25">
        <div align="left"><b><coeusUtils:formatOutput name="entityDetails" property="name" defaultValue="&nbsp;" scope="request"/></b></div>
      </td>
      <td width="114" height="25">
        <div align="left"><bean:message key="viewFinEntity.label.type" />&nbsp;</div>
      </td>
      <td width="6" height="25">
        <div align="left">:</div>
      </td>
      <td width="362" height="25">
        <div align="left"><b><coeusUtils:formatOutput name="entityDetails" property="type" defaultValue="&nbsp;" scope="request"/> </b></div>
      </td>
    </tr>
    <tr bgcolor="#F7EEEE" valign="top">
      <td width="105" height="25">
        <div align="left"><bean:message key="viewFinEntity.label.status"/></div>
      </td>
      <td width="6" height="25">
        <div align="left">:</div>
      </td>
      <td width="202" height="25">
        <div align="left"><b><coeusUtils:formatOutput name="entityDetails" property="status" defaultValue="&nbsp;" scope="request"/></b></div>
      </td>
      <td width="114" height="25">
        <div align="left"><bean:message key="viewFinEntity.label.shareOwnership"/></div>
      </td>
      <td width="6" height="25">
        <div align="left">:</div>
      </td>
      <td width="362" height="25">
       <div align="left"><b>
          <logic:present name="entityDetails" property="shareOwnship">
          	<logic:equal name="entityDetails" property="shareOwnship" value="P" scope="request">
				<bean:message key="viewFinEntity.label.public"/>
			</logic:equal>
			<logic:notEqual name="entityDetails" property="shareOwnship" value="P" scope="request">
				<bean:message key="viewFinEntity.label.private"/>
			</logic:notEqual></b>
          </logic:present>
          <logic:notPresent name="entityDetails" property="shareOwnship">
          	&nbsp;
          </logic:notPresent>
		</div>
      </td>
    </tr>
    <tr bgcolor="#FBF7F7" valign="top">
      <td width="105" height="25">
        <div align="left"><bean:message key="viewFinEntity.label.explanation"/></div>
      </td>
      <td width="6" height="25">
        <div align="left">:</div>
      </td>
      <td colspan="4" height="25">
        <div align="left"><b><coeusUtils:formatOutput name="entityDetails" property="entityDescription" defaultValue="&nbsp;" scope="request"/> </b></div>
      </td>
    </tr>
    <tr>
      <td colspan="6" height="15" bgcolor="#CC9999">
        <div align="left"><font color="#FFFFFF"><b><bean:message key="viewFinEntity.personRelation"/></b></font></div>
      </td>
    </tr>
    <tr bgcolor="#FBF7F7" valign="top">
      <td width="105" height="25">
        <div align="left"><bean:message key="viewFinEntity.personRelation.label.type"/></div>
      </td>
      <td width="6" height="25">
        <div align="left">:</div>
      </td>
      <td width="202" height="25">
        <div align="left"><b><coeusUtils:formatOutput name="entityDetails" property="personReType" defaultValue="&nbsp;" scope="request"/></b> </div>
      </td>
      <td width="114" height="25">
        <div align="left">&nbsp;</div>
      </td>
      <td width="6" height="25">
        <div align="left">&nbsp;</div>
      </td>
      <td width="362" height="25">
        <div align="left">&nbsp; </div>
      </td>
    </tr>
    <tr bgcolor="#F7EEEE" valign="top">
      <td width="105" height="25">
        <div align="left"><bean:message key="viewFinEntity.personRelation.label.explanation"/></div>
      </td>
      <td width="6" height="25">
        <div align="left">:</div>
      </td>
      <td colspan="4" height="25">
        <div align="left"><b><coeusUtils:formatOutput name="entityDetails" property="personReDesc" defaultValue="&nbsp;" scope="request"/> </b></div>
      </td>
    </tr>
    <!-- CASE #1374 Comment Begin -->
    <%--
    <tr bgcolor="#F7EEEE" valign="top">
      <td colspan=5 height="25">
        <div align="left"><bean:message key="viewFinEntity.organizationRelationShip.label" /></div>
      </td>
      
      <td height="25">
        <div align="left"><b><coeusUtils:formatOutput name="entityDetails" property="orgRelationship" scope="request"/> </b></div>
      </td>
    </tr>
    --%>
    <!-- CASE #1374 Comment End -->
<!-- For this release, include only the organization relationship, no explanation.  -->
<%--
    <tr>
      <td colspan="6" height="15" bgcolor="#CC9999">
        <div align="left"><font color="#FFFFFF"><b>
        	<bean:message key="viewFinEntity.organizationRelationShip.label" />
	        <coeusUtils:formatOutput name="entityDetails" property="orgRelationship" scope="request"/>
	    </b></font></div>
      </td>
    </tr>
    <tr bgcolor="#FBF7F7" valign="top">
      <td width="105" height="25">
        <div align="left"><bean:message key="viewFinEntity.organizationRelationShip.label.explanation" /></div>
      </td>
      <td width="6" height="25">
        <div align="left">:</div>
      </td>
      <td colspan="4" height="25">
        <div align="left"><b><coeusUtils:formatOutput name="entityDetails" property="orgDescription" defaultValue="&nbsp;" scope="request"/> </b></div>
      </td>
    </tr>
--%>
<!-- For this release, don't include sponsor information.  -->
<%--
    <tr bgcolor="#FBF7F7" valign="top">
      <td width="105" height="25">
	<div align="left"><bean:message key="viewFinEntity.organizationRelationShip.label.sponsorid" /></div>
      </td>
      <td width="6" height="25">
	<div align="left">:</div>
      </td>
      <td width="202" height="25">
	<div align="left"><b> <coeusUtils:formatOutput name="entityDetails" property="sponsor" defaultValue="&nbsp;" scope="request"/> </b></div>
      </td>
      <td width="114" height="25">
        <div align="left"><bean:message key="viewFinEntity.organizationRelationShip.label.sponsor" />&nbsp;</div>
      </td>
      <td width="6" height="25">
	<div align="left">:</div>
      </td>
      <td width="362" height="25">
	<div align="left"><b><coeusUtils:formatOutput name="entityDetails" property="sponsorName" defaultValue="&nbsp;" scope="request"/> </b></div>
      </td>
    </tr>
--%>

    <!-- Dont include this information.  -->
<%--

    <tr bgcolor="#F7EEEE" valign="top">
      <td width="105" height="25">
        <div align="left"><bean:message key="viewFinEntity.organizationRelationShip.label.sequenceNo"/>.</div>
      </td>
      <td width="6" height="25">
        <div align="left">:</div>
      </td>
      <td width="202" height="25">
        <div align="left"><b><coeusUtils:formatOutput name="entityDetails" property="seqNumber" defaultValue="&nbsp;" scope="request"/> </b></div>
      </td>
      <td width="114" height="25">
        <div align="left">&nbsp;</div>
      </td>
      <td width="6" height="25">
        <div align="left">&nbsp;</div>
      </td>
      <td width="362" height="25">
        <div align="left">&nbsp;</div>
      </td>
    </tr>
  --%>
  </table>
<!-- Financial Entity Certification details -->

  <!--insert ViewFinancialEntityDetailsInsert.jsp -->
  <jsp:include page="ViewFinancialEntityDetailsInsert.jsp" />
  <!-- end insert -->
  
</body>
</html:html>
