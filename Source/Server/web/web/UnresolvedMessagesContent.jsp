<%@page errorPage="ErrorPage.jsp"%>

<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="util" %>

<%@ include file="CoeusContextPath.jsp"  %>


<% System.out.println("Inside unresolvedMessagesContent.jsp"); %>
<jsp:useBean id="userInfoBean"
		class="edu.mit.coeus.bean.UserInfoBean"
		scope="session" />
<html:form action="/updateInboxMessages.do?messageType=unresolved" method="POST">

<table border="0" cellpadding="0" cellspacing="0" width="100%" >
<tr>
<td>
<table border="0"   cellpadding="0" cellspacing="0" width="98%" align="right">
            <tr>
            <td height="5" colspan="4"></td>
          </tr>
	<tr bgcolor="#cccccc">

		<td width="25%" height="28">&nbsp;
			<html:link page="/getInboxMessages.do?messageType=unresolved">
			<b><u><span style="color:#660000">
			Unresolved Messages
			</span></u></b>
			</html:link>
		</td>
		<td width="25%">
			<html:link page="/getInboxMessages.do?messageType=resolved">
			<b><u><span style="color:#660000">
			Resolved Messages
			</u></b></span>
			</html:link>
		</td>

		<td width="25%">
 		      <a href="javascript:openwintext('<bean:write name="ctxtPath"/>/coeusSearch.do?searchname=proposalDevSearchNoRoles&fieldName=proposalNum','proposal');">
			<b><u><span style="color:#660000">
                    Proposal Search
			</u></b></span>
                  </a>
		</td>

		<td width="25%">
			<html:link forward="welcome">
			<b><u><span style="color:#660000">
			Coeus Home
			</u></b></span>
			</html:link>
		</td>

	</tr>
	<tr>
		<td align="left" height="23" colspan="4">
		<b><!--<h2>-->
			&nbsp;Unresolved Messages for <bean:write name="userInfoBean" property="userName" />
		<!--</h2>--></b>
		</td>
	</tr>
</table>
</td>
</tr>

<logic:equal name="getInboxMessagesForm"
		property="totalMessages"
		value="0" >
<tr>
<td>
<table width="98%" align="right" border="0">
	<tr>
		<td>
			<b>There are no unresolved messages for
			<bean:write name="userInfoBean" property="userName" /></b>
		</td>
	</tr>
</table>
</td>
</tr>
</logic:equal>


<logic:greaterThan name="getInboxMessagesForm"
			property="totalMessages"
			value="0" >

<tr>
<td>
<table width="98%" align="right" border="0">
	<tr>
		<td>
		<html:image page="/images/moveSelected.gif" onclick="submit" border="0" />
		</td>

	</tr>
</table>
</tr>
</td>
<tr>
<td>
<table width="98%" align="right" cellpadding="2" cellspacing="2" >
	<tr bgcolor="#CC9999">
		<td>&nbsp;<html:img page="/images/checked.gif" border="0"/></td>
		<td>	<html:link page="/getInboxMessages.do?messageType=unresolved" anchor="legend">
				<html:img page="/images/gflag.gif" border="0" />
			</html:link>
		</td>
		<td><div align="center"><font color="#FFFFFF" >From</div></td>
		<td><div align="center"><font color="#FFFFFF" >Message</div></td>
		<td><div align="center"><font color="#FFFFFF" >Proposal Title</div></td>
		<td><div align="center"><font color="#FFFFFF" >Proposal Number</div></td>
		<td><div align="center"><font color="#FFFFFF" >Date Received</div></td>
	</tr>
<% int count =0; %>

<logic:iterate id= "index" name="getInboxMessagesForm" property="fromUser">

<%
	Integer countInt = new Integer(count);
	String countString = countInt.toString();
%>

<tr bgcolor='<%=( (count) % 2 == 0 ? "#FBF7F7" : "#F7EEEE") %>' valign="top">
	<td>
		<html:multibox property="whichMessagesAreChecked">
			<%= countInt %>
		</html:multibox>
	</td>
	<td>
		<logic:notEqual name="getInboxMessagesForm"
					property='<%= "daysUntilDeadline[" + countString + "]" %>'
					value="-1">
			<logic:greaterEqual name="getInboxMessagesForm"
						property='<%= "daysUntilDeadline[" + countString + "]" %>'
						value="0" >
				<logic:lessThan name="getInboxMessagesForm"
						property='<%= "daysUntilDeadline[" + countString + "]" %>'
						value="3" >
                                                <a href="<bean:write name="ctxtPath"/>/displayProposal.do?proposalNumber=<bean:write name="getInboxMessagesForm"
                                                          property='<%= "proposalNumber[" + countString + "]" %>' />">
					        <html:img page="/images/rflag.gif" border="0" />
                                                </a>

				</logic:lessThan>
				<logic:greaterEqual name="getInboxMessagesForm"
						property='<%= "daysUntilDeadline[" + countString + "]" %>'
						value="3" >
					<logic:lessThan name="getInboxMessagesForm"
							property='<%= "daysUntilDeadline[" + countString + "]" %>'
							value="5" >
                                                        <a href="<bean:write name="ctxtPath"/>/displayProposal.do?proposalNumber=<bean:write name="getInboxMessagesForm"
							    property='<%= "proposalNumber[" + countString + "]" %>' />">
					                  <html:img page="/images/yflag.gif" border="0" />
					                  </a>

					</logic:lessThan>
					<logic:greaterEqual name="getInboxMessagesForm"
							property='<%= "daysUntilDeadline[" + countString + "]" %>'
							value="5" >
						<logic:lessThan name="getInboxMessagesForm"
								property='<%= "daysUntilDeadline[" + countString + "]" %>'
								value="11" >
                                                          <a href="<bean:write name="ctxtPath"/>/displayProposal.do?proposalNumber=<bean:write name="getInboxMessagesForm"
							    property='<%= "proposalNumber[" + countString + "]" %>' />">
					                  <html:img page="/images/gflag.gif" border="0" />
					                  </a>
						</logic:lessThan>
					</logic:greaterEqual>
				</logic:greaterEqual>
			</logic:greaterEqual>
		</logic:notEqual>
		&nbsp;
	</td>

	<td>
		<bean:write name="getInboxMessagesForm" property='<%= "fromUser[" + countString + "]" %>' />

	</td>
	<td>
		<bean:write name="getInboxMessagesForm" property='<%= "message[" + countString + "]" %>' />
	</td>

	<!-- CASE #748 Comment Begin -->
	<%--
	<td>
		<logic:notEqual name="getInboxMessagesForm"
				property='<%= "proposalTitle[" + countString + "]" %>'
				value="null" >
					<a href="<bean:write name='ctxtPath'/>/displayProposal.do?proposalNumber=
					<bean:write name="getInboxMessagesForm"
							property='<%= "proposalNumber[" + countString + "]" %>' />">
					<u><bean:write name="getInboxMessagesForm"
							property='<%= "proposalTitle[" + countString + "]" %>' />
					</u></a>
		</logic:notEqual>
		&nbsp;
	</td>

	<td>
		<logic:notEqual name="getInboxMessagesForm"
				property='<%= "proposalNumber[" + countString + "]" %>'
				value="null" >
					<a href="<bean:write name='ctxtPath'/>/displayProposal.do?proposalNumber=
					<bean:write name="getInboxMessagesForm"
							property='<%= "proposalNumber[" + countString + "]" %>' />">
					<u><bean:write name="getInboxMessagesForm"
							property='<%= "proposalNumber[" + countString + "]" %>' />
					</u></a>
		</logic:notEqual>
		&nbsp;
	</td>
	--%>
	<!-- CASE #748 Comment End -->
	<!-- CASE #748 Begin -->
	<td>
		<logic:present name="getInboxMessagesForm"
				property='<%= "proposalTitle[" + countString + "]" %>'>
			<a href="<bean:write name='ctxtPath'/>/displayProposal.do?proposalNumber=
			<bean:write name="getInboxMessagesForm"
					property='<%= "proposalNumber[" + countString + "]" %>' />">
			<u><bean:write name="getInboxMessagesForm"
					property='<%= "proposalTitle[" + countString + "]" %>' />
			</u></a>
		</logic:present>
		&nbsp;
	</td>

	<td>
		<logic:present name="getInboxMessagesForm"
			property='<%= "proposalNumber[" + countString + "]" %>'>
			<a href="<bean:write name='ctxtPath'/>/displayProposal.do?proposalNumber=
			<bean:write name="getInboxMessagesForm"
					property='<%= "proposalNumber[" + countString + "]" %>' />">
			<u><bean:write name="getInboxMessagesForm"
					property='<%= "proposalNumber[" + countString + "]" %>' />
			</u></a>
		</logic:present>
		&nbsp;
	</td>

	<!-- CASE #748 End -->

	<td>
		<util:formatDate name="getInboxMessagesForm" property='<%= "arrivalDate[" + countString + "]" %>' />

	</td>
</tr>

<% count++; %>

</logic:iterate>

</table>
</td>
</tr>
<tr>
<td>
<table border="0" cellpadding="5" cellspacing="0" width="98%" align="right">
<a name="legend"></a>
	<tr>
		<td><b>Legend: </b>
		</td>
		<td>
			<html:img page="/images/gflag.gif" border="0" />
			Deadline within 10 days
		</td>
		<td>
			<html:img page="/images/yflag.gif" border="0" />
			Deadline within 4 days
		</td>
		<td>
			<html:img page="/images/rflag.gif" border="0" />
			Deadline within 2 days
		</td>
	</tr>
	<tr>
		<td colspan="4">
			<html:image page="/images/moveSelected.gif" onclick="submit" border="0" />
		</td>
	</tr>
</table>
</td>
</tr>
<logic:greaterEqual name="getInboxMessagesForm"
			property="totalMessages"
			value="5" >


<tr>
<td>
<table border="0"   cellpadding="0" cellspacing="0" width="98%" align="right">
	<tr bgcolor="#cccccc" >

		<td width="25%" height="28">&nbsp;
			<html:link page="/getInboxMessages.do?messageType=unresolved">
			<b><u><span style="color:#660000">
			Unresolved Messages
			</span></u></b>
			</html:link>
		</td>
		<td width="25%">
			<html:link page="/getInboxMessages.do?messageType=resolved">
			<b><u><span style="color:#660000">
			Resolved Messages
			</u></b></span>
			</html:link>
		</td>
		<td width="25%">
 		      <a href="javascript:openwintext('<bean:write name="ctxtPath"/>/coeusSearch.do?searchname=proposalDevSearchNoRoles&fieldName=proposalNum','proposal');">
			<b><u><span style="color:#660000">
                    	Proposal Search
			</u></b></span>
                  </a>
		</td>
		<td width="25%">
			<html:link forward="welcome">
			<b><u><span style="color:#660000">
			Coeus Home
			</u></b></span>
			</html:link>
		</td>

	</tr>
	<tr>
	<td height="5">&nbsp;</td>
	</tr>
</table>
</td>
</tr>
</table>
</logic:greaterEqual>
</logic:greaterThan>
</html:form>

