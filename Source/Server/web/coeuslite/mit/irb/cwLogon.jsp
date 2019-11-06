<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>



<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<html>
<head>
<!-- JM 5-31-2011 updated per 4.4.2 -->
<title>Coeus Login ...</title>
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
	<!-- COEUSDEV-657 ARRA - locking error - Start -->
	<% //If Session is new... then we  don't have an AUTH_ACTION. Rather than causing an exception, go to the userAuthAction.do
        //If the session timeout is occurred then forward to userAuthAction.do?reason=sessionExpired
        if (session==null || session.isNew()) {
            response.sendRedirect(request.getContextPath()+"/userAuthAction.do");
            return;
          }else if(session.getAttribute("AUTH_ACTION")==null){
            response.sendRedirect(request.getContextPath()+"/userAuthAction.do?reason=sessionExpired");
            return;
          }
        %>
	<!-- COEUSDEV-657 ARRA - locking error - End -->

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
								src="<bean:write name='ctxtPath'/><bean:message key="loginPage.headerImage"/>"
								width="675" height="82"></td>
						</tr>

					</table>
				</td>
			</tr>
			<tr class="theader">
				<td>
					<table width="100%" cellspacing="0" cellpadding="0">
						<tr>
							<!-- JM 5-31-2011 updated per 4.4.2 -->
							<th align="Center">CoeusLite &trade; Login</th>


						</tr>
					</table>
				</td>
			</tr>

			<html:form action='<%=(String)session.getAttribute("AUTH_ACTION")%>'
				method="post" focus="username">
				<tr>
					<td>&nbsp&nbsp&nbsp;</td>
				</tr>
				<%
            //JIRA COEUSQA-2864 - START
            if(request.getParameter("reason") != null && request.getParameter("reason").equalsIgnoreCase("sessionExpired")){
                out.print("<tr><td height='50px' align='center' class='tabtable'><p style='color:red;font-size:11pt;font-weight:bold'>Session Expired. Please re-login.</p></td></tr>");
            }
            //JIRA COEUSQA-2864 - START
            %>
				<tr>

					<td>
						<table width="99%" hight="100%" cellspacing="0" cellpadding="0"
							border="0">
							<tr height="480">
								<td width="50%">
									<!-- JM 5-31-2011 updated per 4.4.2 -->
									<table width="400" height="400" border="0" cellpadding="0"
										cellspacing="0" bgcolor="fef5e0" class="tabtable"
										align="center">
										<tr>
											<td align="center" colspan="3">
												<!-- JM 5-31-2011 updated message per 4.4.2 --> <br>The
												use of CoeusLite&trade; <br>requires a valid userid and
												password. <%-- Case Id : 3252 - START
                                            <P><b>If you are a new user for CoeusLite, please ask for a username and password by clicking on "Register" below..</b></p><br>
					               
                                            <a href="mailto:coeus-help@mit.edu?subject=Registration to Coeus Lite"  onMouseOver="swap('name_of_img','register')" 
                                            onMouseOut="swap('name_of_img','register_over')">
                                            <img name="name_of_img" src="<bean:write name='ctxtPath'/>/coeusliteimages/b_register.gif" border="0"></a>

                                            <br>If you already have created a login, sign in below.
                                            Case Id : 3252 - END --%>
											</td>
										</tr>
										<!-- COEUSDEV-657 ARRA - locking error - Start -->
										<%
                                    //If session timeout is occurred then the respective message is displayed
                                    String sessionTimeOut = (String)session.getAttribute("reason");
                                    if(session==null || session.isNew() || (sessionTimeOut!=null && sessionTimeOut.equals("sessionExpired"))){%>
										<tr>
											<td align="center" colspan="3">
												<p style='color: red; font-size: 11pt; font-weight: bold'>Session
													Expired. Please re-login.</p>
											</td>
										</tr>
										<%}%>
										<!-- COEUSDEV-657_LOG_ARRA - locking error - End -->
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
													property="username" styleClass="textbox" /></td>
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
				<!-- JM 7-28-2011 added link to Coeus Premium as work around for redirect issue -->
				<tr>
					<td><a
						style="color: blue; font-weight: bold; text-decoration: underline;"
						href="/coeus/CoeusWebStart.jsp">Coeus Premium Web Start</a></td>
				</tr>
				<!-- END -->
				<tr>
					<td>
						<!-- JM 5-31-2011 updated per 4.4.2 -->
						<table bgcolor='#000000' width="100%" border="0">
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
			</html:form>
		</tbody>
	</table>





</body>
</html>
