<%--
/*
 * @(#)ChangePasswordPageContent.jsp
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on June 12, 2002
 *
 * @author  Coeus Dev Team
 * @version 1.0
 */
--%>
<%@ page language="java" %>
<%@ page  errorPage="ErrorPage.jsp"
		import="org.apache.struts.action.Action" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix ="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix ="logic" %>

<jsp:useBean id="userInfoBean"
		class="edu.mit.coeus.bean.UserInfoBean"
		scope="session" />
		
<%@ include file="CoeusContextPath.jsp"  %>

<html:html >


<table border="0"  cellpadding="0" cellspacing="0" width="100%" >
            <tr>
            <td height="5"></td>
          </tr>
	<tr bgcolor="#cccccc">
		<td align="left" height="23" colspan="4">
			<b><!--<h2>-->
			&nbsp;Change Coeus Password for
			<bean:write name="userInfoBean" property="userName" />
			<!--</h2>--></b>
		</td>
	</tr>
</table>
<table border="0" cellpadding="2" cellspacing="2" width="100%">
            <tr>
            <td >&nbsp;</td>
          </tr>
	<tr>
		<td >
		If you have a Coeus username, you can use this web page even if you've forgotten
		your Coeus password, since you will be authenticated via your
		<!--<a  href="http://web.mit.edu/is/help/cert">-->
			MIT Certificate.
		<!--</a>-->
		</td>
	</tr>
	<tr>
		<td >You will be choosing a password for an Oracle database.  It will not be
		case-sensitive.  It must
		<div>
		<ul>
		    <li>begin with a letter</li>
		    <li>contain only the characters A-Z, 0-9, _, $, and #.
		    <li>be no longer than 30 characters</li>
		    <li>not match common words that are used by Oracle databases, such as
			TABLE, SELECT, INSERT, UPDATE, DELETE, ORDER, etc..</li>
		</ul>
		</div>
		Avoid easily guessable passwords.  Don't use
		<div>
		<ul>
		    <li>your first name, middle name, last name, nickname, initials, name of
		    a friend, relative, or pet</li>
		    <li>any word in a dictionary</li>
		    <li>fewer than 5 characters</li>
		    <li>your phone number, office number, anniversary, birthday</li>
		</ul>
		</div>
		Remember that even though your Coeus username must be the same as your Kerberos username,
		your Coeus password should be different than your Kerberos password.
		</td>
	</tr>
</table>
<hr />
<table>
<tr>
	<td colspan="3" >
		Enter the new password for <%= session.getAttribute("userId") %>  below.
		Then, enter the same new password again.
	</td>
</tr>
<!-- CASE #1393 Begin -->
<logic:present name="<%=Action.ERROR_KEY%>">
<tr>
	<td>
		&nbsp;
	</td>
	<td colspan="2">
	<bean:message key="validationErrorHeader"/>
	</font>
	</td>
</tr>
</logic:present>
<!-- CASE #1393 End -->

<!-- CASE #1393 Comment Begin -->
<%--
<tr>
<td>
	<html:errors />
</td>
</tr>
--%>
<!-- CASE #1393 Comment End -->
<html:form action="/changePassword.do" focus="newPassword">
<tr>
	<td width="240"><bean:message key="prompt.password" /></td>
	<td align="right" width="40">
		<html:password property="newPassword" size="16" maxlength="16" redisplay="false"/>
	</td>
	<td align="left">
		<font color="red" size="1">
		<html:errors property="newPassword"/>
		</font>	
		&nbsp;
	</td>
</tr>
<tr>
	<td><bean:message key="prompt.confirmPassword" /></td>
	<td align="right">
		<html:password property="confirmPassword" size="16" maxlength="16" redisplay="false"/>
	</td>
	<td align="left">
		<font color="red" size="1">
		<html:errors property="confirmPassword"/>
		</font>	
		&nbsp;
	</td>	
</tr>
<tr>
	<td colspan="2">&nbsp</td>
	<td>&nbsp;&nbsp;&nbsp;&nbsp;<html:submit property="submit" value="Submit"/></td>
</tr>
</html:form>
</table>
<hr />
</html:html>
