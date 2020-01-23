<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>



<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<html>
<head>
<title>MIT Coeus Login ...</title>
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/ipf.css"
	rel="stylesheet" type="text/css">
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">

<script language="javascript" type="text/javascript">

            register = new Image();
            register.src = "<bean:write name='ctxtPath'/>/coeusliteimages/b_register.gif";
            register_over = new Image();
            register_over.src = "<bean:write name='ctxtPath'/>/coeusliteimages/b_register_over.gif";

            b_submit = new Image();
            b_submit.src = "<bean:write name='ctxtPath'/>/coeusliteimages/b_submit.gif";
            b_submit_over = new Image();
            b_submit_over.src = "<bean:write name='ctxtPath'/>/coeusliteimages/b_submit_over.gif";


            function swap(){
            if (document.images){
            for (var x=0;
            x<swap.arguments.length;
            x+=2) {
            document[swap.arguments[x]].src = eval(swap.arguments[x+1] + ".src");
            }
            }
            }
        </script>

</head>
<body>

	<table align="center" border="0" cellspacing="0" cellpadding="0"
		height="100%" width="93%" class="table">
		<tbody>

			<tr>
				<td>
					<table width="100%" border="0" cellpadding="0" cellspacing="0"
						STYLE="background-image:url('<bean:write name='ctxtPath'/>/coeusliteimages/header_background.gif');border:0">
						<tr>
							<td><img
								src="<bean:write name='ctxtPath'/><bean:message key="loginPage.headerImage"/>"
								width="100%" height="100"></td>
						</tr>
						</td>
						</tr>
					</table>
			<tr class="theader">
				<td>
					<table width="100%" cellspacing="0" cellpadding="0">
						<tr>
							<th align="Center">Coeus Login</th>


						</tr>
					</table>
				</td>
			</tr>

			<%--            <html:form action="/logonAction.do" method="post" focus="username">    --%>
			<html:form action='<%=(String)session.getAttribute("AUTH_ACTION")%>'
				method="post" focus="username">
				<tr>
					<td>&nbsp&nbsp&nbsp;</td>
				</tr>
				<tr>

					<td>
						<table width="99%" hight="100%" cellspacing="0" cellpadding="0"
							border="0">
							<tr height="480">
								<td width="50%">
									<table width="400" height="400" border="0" cellpadding="0"
										cellspacing="0" bgcolor="d6d6d6" class="tabtable"
										align="center">
										<tr>
											<td align="center" colspan="3"><br>Use of this
												CoeusLite&trade; <br>requires a username and password.
												<%-- Case Id : 3252 - START
                                            <P><b>If you are a new user for CoeusLite, please ask for a username and password by clicking on "Register" below..</b></p><br>
					               
                                            <a href="mailto:coeus-help@mit.edu?subject=Registration to Coeus Lite"  onMouseOver="swap('name_of_img','register')" 
                                            onMouseOut="swap('name_of_img','register_over')">
                                            <img name="name_of_img" src="<bean:write name='ctxtPath'/>/coeusliteimages/b_register.gif" border="0"></a>

                                            <br>If you already have created a login, sign in below.
                                            Case Id : 3252 - END --%></td>
										</tr>

										<tr>
											<td align="right" valign="bottom"><img
												src="<bean:write name='ctxtPath'/>/coeusliteimages/c_username.gif"
												border="0"></td>
											<td>&nbsp;</td>
											<td align="left" valign="bottom"><img
												src="<bean:write name='ctxtPath'/>/coeusliteimages/c_password.gif"
												border="0"></td>
										</tr>
										<tr>
											<td align="right" valign="top"><html:text
													property="username" styleClass="textbox" maxlength="8" /></td>
											<td>&nbsp;</td>
											<td align="left" valign="top"><html:password
													property="password" styleClass="textbox" /></td>
										</tr>
										<tr>
											<td align="center" colspan="3" valign=top><br>

												<p align="center">
													<!--  <input type="image" name="submit" src="<bean:write name='ctxtPath'/>/coeusliteimages/b_submit.gif" width="62" height="22" border="0" /> -->
													<html:submit property="Logon" value="Login"
														styleClass="clbutton" />
													&nbsp;&nbsp;
													<html:reset styleClass="clbutton" />
												</p></td>
										</tr>
									</table>
								</td>

								<td width="50%"><img
									src="<bean:write name='ctxtPath'/>/coeusliteimages/coeus_splash.jpg"
									width="630" height="468"></td>
							</tr>
						</table>

					</td>
				</tr>


				<tr>
					<td>
						<table bgcolor='#376DAA' width="100%" border="0">
							<tr>
								<td align="left"><font color="white" size="-1"> <bean:message
											key="label.copywriteMIT" />
								</font></td>
								<td align="right"><font color="white" size="-1"><bean:message
											key="PRODUCT_VERSION_LABEL" /> <%-- Case# 2840:Place Coeus version in footer of lite application --%>
										<bean:message bundle="coeusprop" key="PRODUCT_VERSION" /> </font></td>
							</tr>
						</table>
					</td>
				</tr>
	</table>
	</td>
	</tr>
	</html:form>
	</tbody>
	</table>

</body>
</html>
