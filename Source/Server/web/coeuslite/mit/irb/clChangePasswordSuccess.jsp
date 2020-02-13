<%@page errorPage="ErrorPage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>


<jsp:useBean id="userInfoBean" class="edu.mit.coeus.bean.UserInfoBean"
	scope="session" />

<html:html>
<body>
	<table width="100%" height="100%" border="0" cellpadding="0"
		cellspacing="5" class="table">
		<tr>
			<td valign="top">
				<table align="center" border="0" cellpadding="2" cellspacing="0"
					width="99%" class="tabtable">
					<tr class="theader">
						<td>&nbsp;success</td>
					</tr>
					<tr>
						<td><br> The password for <b><%=session.getAttribute("userId") %></b>
							has been successfully changed. <br>
						<br></td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</body>
</html:html>
