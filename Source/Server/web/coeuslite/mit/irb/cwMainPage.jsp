<%@page
	import="edu.mit.coeus.utils.CoeusFunctions, edu.mit.coeus.propdev.bean.*, java.util.*"%>
<html>
<head>
<title>CoeusLite</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>

<body>
	<table width="100%" height="100%" border="0" cellpadding="0"
		cellspacing="5" class="table">
		<tr align="left" valign="top">
			<td width="215" height="100%" valign="top">

				<table width="100%" height="100%" border="0" cellpadding="0"
					cellspacing="5" class="tabtable">
					<tr>
						<td valign="top" align="center">

							<table width="98%" border="0" cellpadding="3" cellspacing="0"
								class="tabtable">
								<tr class="theader">
									<td valign="top">Coeus Lite</td>
								</tr>
								<tr class="rowLine">
									<td valign="top"><a href="http://www.grants.gov"
										target="_blank">Grants. Gov</a></td>
								</tr>
							</table>

						</td>
					</tr>
					<tr>
						<td valign="top" align="center">

							<table width="98%" border="0" cellpadding="3" cellspacing="0"
								class="tabtable">
								<tr class="theader">
									<td valign="top">Coeus Premium</td>
								</tr>
								<tr class="rowLine">
									<td valign="top"><a
										href="<%=request.getContextPath()%>/CoeusWebStart.jsp"
										target="_blank">Launch Coeus Premium</a></td>
								</tr>
							</table>

						</td>
					</tr>
					<tr>
						<td height="200">&nbsp;</td>
					</tr>
				</table>

			</td>

			<td width="539">

				<table width="100%" height="100%" border="0" cellpadding="3"
					cellspacing="0" class="tabtable">
					<tr>
						<td valign="top">
							<div style="overflow: auto; width: 525; height: 600">
								<h1 align="center">Welcome to CoeusLite</h1>
								<%--<h3>&nbsp;</h3>
                                <h3><font color="#800000">Coeus Mission</font></h3>
                                <p align="justify">
                                    <font size="3"><strong>
                                            <font face="Verdana, Arial, Helvetica, sans-serif">
                                                To create, maintain and support the most robust electronic solution to manage all 
                                                components of sponsored projects from proposal development through award closeout 
                                                while fostering best practices by joining innovative technology with the knowledge 
                                                base of a national consortium of research stakeholders.
                                </font> </strong></font></p>
                                <p>&nbsp;</p>--%>
								<font face="verdana">
									<p class="copyred">
										<u>Primary Features of Coeus Lite include:</u>
									</p>
									<p align="justify">
										<B><u>My Protocols</u></B> - Allows Principal Investigators to
										prepare and submit protocol applications and review detailed
										information associated with their existing protocols.
									</p>
									<p align="justify">
										<B><u>My Proposals</u></B> - Allows Users to view and prepare
										grant applications and route their applications for
										Institutional Approval. Institution Administrators can see and
										approve grant applications. Institute Approvers can view,
										approve, or reject proposals for correction. OSP Authorized
										Administrators can submit applications to Grants.gov.
									</p>
									<p align="justify">
										<B><u>COI Disclosure</u></B> - Allows individuals to provide
										data and required updates on entities in which they hold
										interest.
									</p>
									<p align="justify">
										<B><u>Inbox</u></B> - List of Resolved and Unresolved messages
										from Coeus application with active links to Coeus Lite
										proposals listed by title and number.
									</p>
								</font>
							</div>
						</td>
					</tr>
					<%
                        String parameterValue = new CoeusFunctions().getParameterValue("PUBLIC_MESSAGE_ID");
                        HashMap data = new HashMap();
                        String message = null;
                        if (parameterValue != null && parameterValue.trim().length() > 0) {
                            data.put("MESSAGE_ID", parameterValue);
                            ProposalActionTxnBean proposalActionTxnBean = new ProposalActionTxnBean();
                            MessageBean messageBean = proposalActionTxnBean.getMessage(parameterValue);
                            message = messageBean==null ? null : messageBean.getMessage();
                        }
                        if(message != null && message.trim().length() > 0) {%>
					<tr>
						<td valign="bottom" class="copybold">
							<%--<iframe src="=request.getContextPath()%>/coeuslite/mit/irb/clMessageOftheDay.jsp" frameborder="0" class="table" width="100%" height="75" align="center" class="copybold">
                      </iframe>--%>

							<table width="100%" height="75" cellspacing="3" cellpadding="0">
								<tr>
									<td class="lineBorderWhiteBackgrnd">
										<div style="overflow: auto; width: 500; height: 75">
											<%
                                                out.print(message);
                                            %>
											<div>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<%}%>
				</table>
			</td>

			<td width="205" height="100%" valign="top">
				<table width="100%" height="100%" border="0" cellpadding="0"
					cellspacing="0" class="tabtable">
					<tr>
						<td valign="top">
							<table width="100%" cellpadding="0" cellspacing="5">
								<tr>
									<td valign="top" align="center">
										<table width="98%" border="0" cellpadding="3" cellspacing="0"
											class="tabtable">
											<tr class="rowLine">
												<td valign="top"><a
													href="<%=request.getContextPath()%>/coeuslite/mit/irb/tiles/cwPasswordTile.jsp?contentPage=/coeuslite/mit/irb/clChangePassword.jsp">Change
														Password</a></td>
											</tr>
										</table>
									</td>
								</tr>

								<tr>
									<td valign="top" align="center">
										<table width="98%" border="0" cellpadding="3" cellspacing="0"
											class="tabtable">
											<tr class="theader">
												<td valign="top">Coeus Mission</td>
											</tr>
											<tr class="rowLine">
												<td valign="top" class="copy">
													<p align="left">
														<i><font face="verdana" size="2">To create,
																maintain and support the most robust electronic solution
																to manage all components of sponsored projects from
																proposal development through award closeout while
																fostering best practices by joining innovative
																technology with the knowledge base of a national
																consortium of research stakeholders.</font></i>
													</p>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>

			</td>
		</tr>
	</table>
	<map name="Map">
		<area shape="rect" coords="24,3,100,23" href="meetings.htm">
	</map>
	<!--img src="http://counter.mit.edu/tally" alt="" width=1 height=1-->
</body>
</html>
