<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<% 
//System.out.println("************************************** "); 
//System.out.println("**** In clCopyright.jsp             ** "); 
//System.out.println("************************************** "); 
 boolean blnReadOnly = false;
 
%>
<html>
<head>
<title>Untitled</title>
</head>

<!-- JM 6-22-2011 updated background color to VU black -->
<body bgcolor="#000000">
	<!-- JM 6-22-2011 updated background color to VU black -->
	<table bgcolor='#000000' width="100%" border="0">
		<tr>
			<td align="left"><font color="white" size="-1"> <bean:message
						key="label.copywriteMIT" />
			</font></td>
			<td align="right"><font color="white" size="-1"><bean:message
						key="PRODUCT_VERSION_LABEL" /> <%-- Case# 2840:Place Coeus version in footer of lite application --%>
					<bean:message bundle="coeusprop" key="PRODUCT_VERSION" /> </font></td>
		</tr>
	</table>
</body>
</html>