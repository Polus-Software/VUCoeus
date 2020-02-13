<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JSP Page</title>
</head>
<body>
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<%if(request.getAttribute("ONLY_HEADER") == null){%>
		<tr>
			<td><jsp:include
					page="/coeuslite/utk/propdev/clApprovalRoute.jsp" flush="true" /></td>
		</tr>
		<%}%>
	</table>
</body>
</html>
