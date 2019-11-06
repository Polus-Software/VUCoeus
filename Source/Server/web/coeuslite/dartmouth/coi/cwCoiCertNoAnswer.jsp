<%@page contentType="text/html"
	import="edu.mit.coeus.utils.CoeusProperties,edu.mit.coeus.utils.CoeusPropertyKeys"%>
<%@page pageEncoding="UTF-8" import="edu.mit.coeus.bean.*"%>
<%@page errorPage="ErrorPage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%--
The taglib directive below imports the JSTL library. If you uncomment it,
you must also add the JSTL library to the project. The Add Library... action
on Libraries node in Projects view can be used to add the JSTL 1.1 library.
--%>
<%--
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JSP Page</title>
<%String path = request.getContextPath();%>
<link href="<%=path%>/coeuslite/dartmouth/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
</head>
<body>
	<table width="970" align="center" height="200px" border="0"
		cellpadding="0" cellspacing="0" class="table">
		<tr>
			<td height="25" class="theader" style="font-size: 14px"><bean:message
					bundle="coi" key="AnnualDisclosure.Certification_No" /></td>
		</tr>
		<tr bgcolor="#B3D5F7">
			<td class="copy" valign="top"><br> <bean:message
					bundle="coi" key="AnnualDisclosure.certification.Text" /></td>
		</tr>
		<form action='<%=path%>/coiProposals.do' method="post">
			<%
          String coiDisclosureNumber="";
          if(request.getAttribute("coiDisclosureNumber")!=null){
          coiDisclosureNumber=(String)request.getAttribute("coiDisclosureNumber") ;
          
     }%>
			<INPUT type='hidden' name='coiDisclosureNumber'
				value='<%=coiDisclosureNumber%>'>
			<tr>
				<td align="center"><input type="submit" value="Continue"
					style="width: 100px"></td>
			</tr>
		</form>
	</table>
</body>
</html>
