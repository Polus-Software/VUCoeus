<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<html>
<head>
<title>Coeus Redirect Page</title>
</head>
<body>
	<% response.sendRedirect(request.getContextPath()+"/userSecondaryAuthAction.do");%>
	<%-- <jsp:useBean id="beanInstanceName" scope="session" class="beanPackage.BeanClassName" /> --%>
	<%-- <jsp:getProperty name="beanInstanceName"  property="propertyName" /> --%>

</body>
</html>
