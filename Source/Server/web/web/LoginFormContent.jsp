<%--
/*
 * @(#)LoginFormContent.jsp
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on March 08, 2006
 *
 * @author  Geo Thomas
 * @version 1.0
 */
 errorPage="ErrorPage.jsp"
--%>
<%@ page language="java" %>
<%@ page 
		import="edu.mit.coeus.action.common.LoginForm" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix ="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix ="logic" %>

		
<%@ include file="CoeusContextPath.jsp"  %>

<html:html >


<table border="0"  cellpadding="0" cellspacing="0" width="100%" >
            <tr>
            <td height="5"></td>
          </tr>
	<tr bgcolor="#cccccc">
		<td align="center" height="23">
			<b><!--<h2>-->
			&nbsp;Please enter your User Id and Password
			<!--</h2>--></b>
		</td>
	</tr>
</table>
<hr />
<table width='100%'>
<!-- CASE #1393 Comment End -->
<html:form name="loginForm" type="edu.mit.coeus.action.common.LoginForm" action="/login" focus="userId">
<tr>
	<td colspan="2" align="right"><bean:message key="prompt.userId" />&nbsp;&nbsp;&nbsp;</td>
	<td colspan="2" align="left">
		<html:text property="userId" size="20" maxlength="16"/>
	</td>
</tr>
<tr>
	<td colspan="2" align="right"><bean:message key="prompt.password" />&nbsp;&nbsp;&nbsp;</td>
	<td colspan="2" align="left">
		<html:password property="password" size="20" maxlength="16" redisplay="false"/>
	</td>
</tr>
<tr>
	<td colspan="3" align="center"><html:submit property="submit" value="Submit"/></td>
</tr>
</html:form>
</table>
<hr />
</html:html>
