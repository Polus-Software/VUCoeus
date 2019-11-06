<%@page
	import="edu.mit.coeus.utils.CoeusFunctions, edu.mit.coeus.propdev.bean.*, java.util.*,edu.mit.coeus.utils.CoeusProperties,edu.mit.coeus.utils.CoeusPropertyKeys"%>


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
									<td valign="top">All Coeus Lite Users</td>
								</tr>
								<tr class="rowLine">
									<td valign="top"><a href="http://www.grants.gov"
										target="_blank">Grants. Gov</a></td>
								</tr>
								<tr class="rowLine">
									<td><a href="http://coeus.mit.edu/training"
										target="_blank">Training and Reference Materials</a></td>
								</tr>
								<tr class="rowLine">
									<td><a
										href="http://web.mit.edu/coeus/www/documents%20&%20forms/CoeusLite_Proposal_Development_Guide.pdf"
										target="_blank">CoeusLite Proposal Preparation Manual </a></td>
								</tr>
							</table>

						</td>
					</tr>
					<tr>
						<td valign="top" align="center">

							<table width="98%" border="0" cellpadding="3" cellspacing="0"
								class="tabtable">
								<tr class="theader">
									<td valign="top">For Coeus Premium Users</td>
								</tr>
								<tr class="rowLine">
									<td valign="top"><a
										href="<%=request.getContextPath()%>/CoeusWebStart.jsp"
										target="_blank">Launch Coeus Premium</a></td>
								</tr>
								<tr class="rowLine">
									<td valign="top"><a
										href="<%=request.getContextPath()%>/coeuslite/mit/iacuc/tiles/cwPasswordTile.jsp?contentPage=/coeuslite/mit/iacuc/clChangePassword.jsp">Change
											Password</a></td>
								</tr>
								<tr class="rowLine">
									<td valign="top"><a
										href="http://web.mit.edu/coeus/www/documents%20&%20forms/Premium_Proposal_Development_Guide.pdf"
										target="_blank">Premium Proposal Preparation Manual </a></td>
								</tr>
								<tr class="rowLine">
									<td valign="top"><a
										href="http://web.mit.edu/coeus/www/help_pages/coeus_help.htm"
										target="_blank">Help</a></td>
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
										<B><u>My IRB Protocols</u></B> - Allows Principal
										Investigators to prepare and submit human subjects protocol
										applications to the Committee on Use of Humans as Experimental
										Subjects and review detailed information associated with
										existing protocols. <b>Current not Implemented at MIT</b>
									</p>
									<p align="justify">
										<B><u>My Proposals</u></B> - Allows Principal Investigators
										and departmental administrators to prepare ans submit grant
										applications for Institutional Approval and allows for
										system-to-system transmission of applications to federal
										agencies through Grants.gov.
									</p>
									<p align="justify">
										<B><u>My COI Disclosures</u></B> - Allows Principal
										Investigators and Key Personnel to submit proposal-specific
										and annual financial conflict of interest disclosures as
										required by federal regulations and some non-federal sponsors.
									</p> <!--p align="justify"><B><u>Inbox</u></B> - List of Resolved and Unresolved messages from Coeus application with active links to Coeus Lite proposals listed by title and number.</p-->
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
												<td valign="top">For assistance<br> <a
													href="mailto:coeus-help@mit.edu">coeus-help@mit.edu</a>
												</td>
											</tr>
											<!-- Modified for COEUSDEV-22 : Disable Change Password functionality in CoeusLite with LDAP Authentication - Start 
                                                --Change Password link is disabled if the login mode is 'LDAP'
                                                -->
											<%String loginMode = CoeusProperties.getProperty(CoeusPropertyKeys.LOGIN_MODE);
                                                if(loginMode == null ||(loginMode != null && !loginMode.equalsIgnoreCase("LDAP"))){
                                                %>
											<tr class="rowLine">
												<td valign="top"><a
													href="<%=request.getContextPath()%>/coeuslite/mit/iacuc/tiles/cwPasswordTile.jsp?contentPage=/coeuslite/mit/iacuc/clChangePassword.jsp">Change
														Password</a></td>
											</tr>
											<%}%>
											<!-- COEUSDEV-22 : END -->
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
	<!--img src="https://counter.mit.edu/tally" alt="" width=1 height=1-->
</body>
</html>
