<%--
/**
 * @(#)HumanSubjContent.jsp
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author: Coeus Dev Team
 * @version 1.0 $ $ Date Aug 6, 2002 $ $
 *
 * Display list of human subject trainees
 * Access HumanSubjBean
 * stored in request object by DisplayHumanSubjAction.
 * For attributes for which a null value is permitted in the database,
 * check for null value.  If value is null, display an empty table cell.
 */
--%>
<%@ page language="java"%>
<%@ page errorPage="ErrorPage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>


<html:html>
<body>

	<table height="100%" border="0" cellpadding="0" cellspacing="5"
		class="table">
		<tr>
			<td valign="top">

				<table align="center" border="0" cellpadding="1" cellspacing="0"
					class="tabtable">
					<tr class="theader">
						<td><b> People who have passed Human Subject Training </b></td>
					</tr>
					<tr>
						<td>

							<table align="center" border="0" cellpadding="1" cellspacing="0"
								class="tabtable">
								<tr class="theader">
									<td width="20%"><b>Full Name</b></td>
									<td width="28%"><b>Training</b></td>
									<td width="28%"><b>Department</b></td>
									<td width="10%"><b>Date Requested</b></td>
									<td width="10%"><b>Date Submitted</b></td>
									<td width="4%"><b>Score</b></td>
								</tr>

								<% int count =0; %>

								<logic:iterate id="index" name="humansubj" property="fullName">

									<%
                                    Integer countInt = new Integer(count);
                                    String countString = countInt.toString();
                                    %>

									<tr class="rowLine <%= (count % 2 == 0) ? "" : "rowHover"%>">
										<%-- bgcolor='<%=( (count) % 2 == 0 ? "#FBF7F7" : "#F7EEEE") %>'>--%>

										<td><bean:write name="humansubj"
												property='<%= "fullName[" + countString + "]"  %>' /></td>

										<td><bean:write name="humansubj"
												property='<%= "description[" + countString + "]"  %>' /></td>
										<!-- CASE #748 Comment Begin -->
										<%--
        <td>
             <logic:notEqual name="humansubj" property='<%= "unitName[" + countString + "]" %>' value="null">
		<bean:write name="humansubj" property='<%= "unitName[" + countString + "]" %>' />
	    </logic:notEqual>
            &nbsp
        </td>
         <td>
             <logic:notEqual name="humansubj" property='<%= "dateRequestedDisplayString[" + countString + "]" %>' value="null">
		<bean:write name="humansubj" property='<%= "dateRequestedDisplayString[" + countString + "]" %>' />
	    </logic:notEqual>
            &nbsp
        </td>
         <td>
             <logic:notEqual name="humansubj" property='<%= "dateSubmittedDisplayString[" + countString + "]" %>' value="null">
		<bean:write name="humansubj" property='<%= "dateSubmittedDisplayString[" + countString + "]" %>' />
	    </logic:notEqual>
            &nbsp
        </td>
        
                                    --%>
										<!-- CASE #748 Comment End -->
										<!-- CASE #748 Begin -->
										<td><logic:present name="humansubj"
												property='<%= "unitName[" + countString + "]" %>'>
												<bean:write name="humansubj"
													property='<%= "unitName[" + countString + "]" %>' />
											</logic:present> &nbsp</td>
										<td><logic:present name="humansubj"
												property='<%= "dateRequestedDisplayString[" + countString + "]" %>'>
												<bean:write name="humansubj"
													property='<%= "dateRequestedDisplayString[" + countString + "]" %>' />
											</logic:present> &nbsp</td>
										<td><logic:present name="humansubj"
												property='<%= "dateSubmittedDisplayString[" + countString + "]" %>'>
												<bean:write name="humansubj"
													property='<%= "dateSubmittedDisplayString[" + countString + "]" %>' />
											</logic:present> &nbsp</td>

										<!-- CASE #748 End -->
										<td><bean:write name="humansubj"
												property='<%= "score[" + countString + "]"  %>' /> &nbsp</td>
									</tr>

									<% count++; %>

								</logic:iterate>


							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</body>
</html:html>