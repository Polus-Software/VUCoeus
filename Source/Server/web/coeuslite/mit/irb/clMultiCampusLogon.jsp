<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<jsp:useBean id="nodereader" scope="application"
	class="edu.mit.coeus.user.auth.multicampus.MultiCampusNodeReader" />


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
					<!-- JM 5-31-2011 updated per 4.4.2 -->
					<table width="100%" border="0" cellpadding="0" cellspacing="0"
						STYLE="background-color: black; border: 0">
						<tr>
							<!-- JM 5-31-2011 updated per 4.4.2 -->
							<td><img
								src="<bean:write name='ctxtPath'/>/coeusliteimages/irbBanner.gif"
								width="675" height="82"></td>
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
			<%
            String authAction = (String)session.getAttribute("AUTH_ACTION");
            if(authAction==null){
                authAction = "/userAuthAction.do";
            }
            %>
			<html:form action='<%=authAction%>' method="post" focus="username">
				<tr>
					<td>&nbsp&nbsp&nbsp;</td>
				</tr>
				<tr>
					<td>
						<table width="99%" hight="100%" cellspacing="0" cellpadding="0">
							<tr height="480">
								<td width="50%">
									<table width="500" height="472" border="0" cellpadding="0"
										cellspacing="0" bgcolor="d6d6d6" class="tabtable"
										align="center">
										<tr>
											<td align="center" colspan="3"><br>Use of this
												CoeusLite&trade; <br>requires a username and password.
												<%-- Case Id:3252 - START
                                            <P><b>If you are a new user for CoeusLite, please ask for a username and password by clicking on "Register" below..</b></p><br>
					               
                                            <a href="mailto:coeus-help@mit.edu?subject=Registration to Coeus Lite"  onMouseOver="swap('name_of_img','register')" 
                                            onMouseOut="swap('name_of_img','register_over')">
                                            <img name="name_of_img" src="<bean:write name='ctxtPath'/>/coeusliteimages/b_register.gif" border="0"></a>

                                            <br>If you already have created a login, sign in below.
                                            Case Id:3252 - END--%></td>
										</tr>

										<%--<tr>
                                        <td align="right" valign="bottom">
                                            <img src= "<bean:write name='ctxtPath'/>/coeusliteimages/c_username.gif"  border="0">
                                        </td>
                                        <td>&nbsp;</td>
                                        <td align="left" valign="bottom" >
                                            <img src= "<bean:write name='ctxtPath'/>/coeusliteimages/c_password.gif" border="0">
                                        </td>
                                    </tr>
                                    <tr>
                                        <td align="right" valign="top"><html:text property="username" styleClass="textbox"  maxlength="8" /></td>
                                        <td>&nbsp;</td>
                                        <td align="left" valign="top"><html:password property="password" styleClass="textbox"/></td>
                                    </tr>
                                    <tr>
                                        <td colspan="3" align="left" valign="top">
                                            <html:select property="campusCode">
                                                <html:optionsCollection name="nodereader" property="campusList" label="description" value="code"/>
                                            </html:select>
                                        </td>
                                    </tr>--%>
										<tr>
											<td valign="top" align="center" colspan="3">
												<table>
													<tr valign="center">
														<td align="right" class="copybold"><b>User Id : </b></td>
														<td align="left"><html:text property="username"
																styleClass="textbox" maxlength="20" /></td>
													</tr>
													<tr valign="center">
														<td align="right" class="copybold"><b>Password :
														</b></td>
														<td align="left"><html:password property="password"
																styleClass="textbox" /></td>
													</tr>
													<tr valign="center">
														<td align="right" class="copybold"><b>Campus : </b></td>
														<td align="left"><html:select property="campusCode">
																<html:optionsCollection name="nodereader"
																	property="campusList" label="description" value="code" />
															</html:select></td>
													</tr>
													<tr>
														<td colspan="2" align="center"><html:submit
																property="Logon" value="Login" styleClass="clbutton" />
															&nbsp;&nbsp;<html:reset styleClass="clbutton" /></td>
														<br>
														<%--<br>                                         
                                            <p align="center">
                                                <html:submit property="Logon" value="Login" styleClass="clbutton" />
                                                &nbsp;&nbsp;
                                                <html:reset styleClass="clbutton" />
                                            </p>
                                        </td>--%>
													</tr>
												</table>
											</td>
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
						<!-- JM 5-31-2011 updated per 4.4.2 -->
						<table bgcolor='#000000' width="100%" border="0">
							<tr>
								<td align="left"><font color="white" size="-1"> <bean:message
											key="label.copywriteMIT" />
								</font></td>
								<td align="right"><font color="white" size="-1"><bean:message
											key="label.copywirteCoeus" /> </font></td>
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
