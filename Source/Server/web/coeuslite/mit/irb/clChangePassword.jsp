<%--
/*
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
--%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

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
						<td><b> &nbsp;Change Coeus Password for <%= session.getAttribute("userId") %>
						</b></td>
					</tr>
					<tr>
						<td>If you have a Coeus username, you can use this web page
							even if you've forgotten your Coeus password,<br> since you
							will be authenticated via your <!--<a  href="http://web.mit.edu/is/help/cert">-->
							MIT Certificate. <!--</a>-->
						</td>
					</tr>
					<tr>
						<td>You will be choosing a password for an Oracle database.
							It will not be case-sensitive. It must
							<div>
								<ul>
									<li>begin with a letter</li>
									<li>contain only the characters A-Z, 0-9, _, $, and #.
									<li>be no longer than 30 characters</li>
									<li>not match common words that are used by Oracle
										databases, such as TABLE, SELECT, INSERT, UPDATE, DELETE,
										ORDER, etc..</li>
								</ul>
							</div> Avoid easily guessable passwords. Don't use
							<div>
								<ul>
									<li>your first name, middle name, last name, nickname,
										initials, name of a friend, relative, or pet</li>
									<li>any word in a dictionary</li>
									<li>fewer than 5 characters</li>
									<li>your phone number, office number, anniversary,
										birthday</li>
								</ul>
							</div> Remember that even though your Coeus username must be the same
							as your Kerberos username, <br>your Coeus password should be
							different than your Kerberos password.
						</td>
					</tr>
					<!-- CASE #1393 Begin -->
					<%--<logic:present name="<%=Action.ERROR_KEY%>">
<tr>
	<td>
		&nbsp;
	</td>
	<td colspan="2">
	<bean:message key="validationErrorHeader"/>
	</font>
	</td>
</tr>
                </logic:present>--%>
					<!-- CASE #1393 End -->

					<!-- CASE #1393 Comment Begin -->
					<%--
<tr>
<td>
	<html:errors />
</td>
</tr>
                --%>
					<tr>
						<td>
							<table align="center" border="0" cellspacing="0" cellpadding="2"
								class="table">
								<tr>
									<td colspan="3">Enter the new password for <%= session.getAttribute("userId") %>
										below. Then, enter the same new password again.
									</td>
								</tr>
								<!-- CASE #1393 Comment End -->
								<html:form action="/changePassword.do" focus="newPassword">
									<tr>
										<td align="right"><bean:message bundle="coeus"
												key="prompt.password" /></td>
										<td><html:password property="newPassword" size="16"
												maxlength="16" redisplay="false" /></td>
										<td align="left" class="copybold"><font color="red">
												<html:errors bundle="coeus" property="newPassword" />
										</font> &nbsp;</td>
									</tr>
									<tr>
										<td align="right"><bean:message bundle="coeus"
												key="prompt.confirmPassword" /></td>
										<td><html:password property="confirmPassword" size="16"
												maxlength="16" redisplay="false" /></td>
										<td align="left" class="copybold"><font color="red">
												<html:errors bundle="coeus" property="confirmPassword" />
										</font> &nbsp;</td>
									</tr>
									<tr>
										<td colspan="3"><html:submit property="submit"
												value="Submit" style="clbutton" /></td>
									</tr>
								</html:form>
							</table> <br>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</html:html>
