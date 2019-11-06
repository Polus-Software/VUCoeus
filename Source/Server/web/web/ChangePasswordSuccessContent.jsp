<%@page  errorPage="ErrorPage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>


<jsp:useBean id="userInfoBean"
		class="edu.mit.coeus.bean.UserInfoBean"
		scope="session" />
		
<%@ include file="CoeusContextPath.jsp"  %>

<html:html>
<table border="0"   cellpadding="0" cellspacing="0" width="100%" >
	<tr>
	<td height="5">
	</td>
	</tr>
	<tr>
		<td class="header" align="left" height="23" colspan="4" bgcolor="#cccccc">
			<b><!--<h2>-->
			&nbsp;Change password success for
                        <bean:write name="userInfoBean" property="userName" />
			<!--</h2>--></b>
		</td>

	</tr>
</table>
<table border="0" cellspacing="0" cellpadding="5">
	<tr>
	<td height="10">&nbsp;</td>
	</tr>
        <tr>
            <td>
              The password for <b><%=session.getAttribute("userId") %></b> has been successfully changed.
            </td>
        </tr>
</table>
</html:html>



