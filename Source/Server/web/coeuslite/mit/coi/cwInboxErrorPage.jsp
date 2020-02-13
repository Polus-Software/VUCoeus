<%@ page
	import="edu.mit.coeus.exception.CoeusException,
org.apache.struts.validator.DynaValidatorForm"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@page errorPage="ErrorPage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="person" scope="session"
	class="edu.mit.coeus.bean.PersonInfoBean" />

<html:html>
<head>
<title>Coeus Web</title>
<html:base />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
</head>
<body>
	<%--   New DESIGN   TEMPLATE    --%>

	<%--  <html:form action="/keyStudyPerson"  method="post" onsubmit="return validateForm(this)"> --%>
	<a name="top"></a>
	<%--  ************  START OF BODY TABLE  ************************--%>

	<table width="980" height="100%" border="0" cellpadding="0"
		cellspacing="0" class="table">

		<% Exception objException = (Exception)request.getAttribute("Exception");
    String exceptionType = "Server encountered an unknown error.";
    String execCode = "";
    String errorMessage  = "";
        if(objException!=null){
           String strExceCode = "";          
           if(objException instanceof CoeusException){
           		/* CASE #665 Changed message displayed to user here.  */	
			   exceptionType = " Coeus encountered an error. ";
			   CoeusException coeusEx = (CoeusException)objException;
			   execCode = coeusEx.getUserMessage();
			   errorMessage=coeusEx.getUserMessage();
                           
			   //errorMessage+="<br />"+coeusEx.getMessage();
		   } else {
				errorMessage=objException.getClass().getName();
				errorMessage+="<br />"+objException.getMessage();
		   }

} %>
		<!-- EntityDetails - Start  -->
		<tr>
			<td height="119" align="left" valign="top"><table width="99%"
					border="0" align="center" cellpadding="0" cellspacing="0"
					class="tabtable">
					<tr>
						<td colspan="4" align="left" valign="top"><table width="100%"
								height="20" border="0" cellpadding="0" cellspacing="0"
								class="tableheader">
								<tr>
									<td><bean:message bundle="coi"
											key="label.coeusInboxErrorMsg" /></td>
								</tr>
							</table></td>
					</tr>

					<tr>

						<td class="copy" height="100">
							<!-- CASE #665 Change font size to match the rest of the page.  -->
							<div align="center">

								<%if(execCode.indexOf("error") != -1){ %>
								<bean:message key="<%= errorMessage%>" bundle="coi" />
								<%}else{%>
								<%=errorMessage%>
								<%}%>
								</b>
							</div>
						</td>
					</tr>
					<tr>
						<td height="23" align="center"><a
							href='javascript:history.back()'><bean:message bundle="coi"
									key="label.back" /></a></td>
					</tr>

					<tr>
						<td>&nbsp;</td>
					</tr>

				</table></td>
		</tr>
		<tr>
			<td height='10'>&nbsp;</td>
		</tr>


	</table>

</body>
</html:html>
