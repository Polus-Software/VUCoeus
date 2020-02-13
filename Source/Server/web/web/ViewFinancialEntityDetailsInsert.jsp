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

<!-- Financial Entity Certification details -->
<table width="100%" border="0" cellspacing="0" cellpadding="5">
  <tr>
    <td width="843" height="25" bgcolor="#CC9999">
      <div align="center"><font color="#FFFFFF"><bean:message key="viewFinEntity.label.question"/></font></div>
    </td>
    <td width="176" height="25" bgcolor="#CC9999">
      <div align="center"><font color="#FFFFFF"><bean:message key="viewFinEntity.label.answer"/></font></div>
    </td>
  </tr>
<logic:present  name="colFinCertificationDetails"  >
   	<logic:iterate id="certficateDetails" name="colFinCertificationDetails" type="edu.mit.coeus.coi.bean.CertificateDetailsBean">

	<tr bgcolor="#FBF7F7">
	  <td width="843" height="25">
		<div align="justify">
		<!-- CASE #1374 Display question label. Don't display question id. -->
		<b><bean:message key="viewFinEntity.label.question"/>&nbsp;
		<bean:write name="certficateDetails" property="label"/></b>:
		<coeusUtils:formatOutput name="certficateDetails" property="question" defaultValue="&nbsp;" />
		<%--(<coeusUtils:formatOutput name="certficateDetails" property="code" defaultValue="&nbsp;" />)--%>
		</div>
</div>
	  </td>
	  <td width="176" height="25">
		<div align="center"> 
		<coeusUtils:formatOutput name="certficateDetails" property="answer" defaultValue="&nbsp;" />
		</div>
	  </td>
	</tr>
</logic:iterate>
</logic:present>
</table>
<!-- end Financial entity certification details -->
<table>
  <tr>
    <td width="843" height="25">&nbsp;</td>
    <td width="176" height="25">&nbsp;</td>
      <div align="right">&nbsp;<a href="JavaScript:window.close();"><img src="<bean:write name="ctxtPath"/>/images/close.gif" width="42" height="22" border="0"></a>
        &nbsp; &nbsp; 
        </div>
    </td>
  </tr>
</table>
