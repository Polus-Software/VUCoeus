
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@page import="org.apache.struts.taglib.html.JavascriptValidatorTag"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@ page
	import="edu.mit.coeuslite.arra.bean.ArraAwardHeaderBean,edu.mit.coeuslite.utils.CoeusLiteConstants"%>

<html:html>
<head>
<title>Coeus Web</title>
<html:base />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<script language="javascript" type="text/JavaScript"
	src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/coeusLiteCalendar.js"></script>
<script>
        function completeSubmission(){
                document.protocolActionsForm.action = "<%=request.getContextPath()%>/saveProtocolActions.do?fromCompleteActionMenu=true";
                document.protocolActionsForm.submit();
            }
         function cancelCompletion(){
                if(!confirm('<bean:message key="protocolAction.msg.cancelConfirmation"/> ')){
                    return;
                }
                document.protocolActionsForm.action = "<%=request.getContextPath()%>/cancelProtocolSubmission.do";
                document.protocolActionsForm.submit();
         }
        </script>
</head>
<body>
	<%

String submissionQnrCompleted = (String) request.getAttribute("submissionQuestionnaireCompleted"); 

String mandaroryQnrMessage = (String) request.getAttribute("mandatoryQnrMessage");  
String incompleteQnrMessage = (String) request.getAttribute("incompleteQnrMessage"); 
String newVersionQnrMessage = (String) request.getAttribute("newVersionQnrMessage"); 

%>

	<html:form action="/completeProtocolSubmission" method="post">
		<a name="top"></a>

		<table width="100%" height="100%" border="0" cellpadding="4"
			cellspacing="0" class="table">


			<tr>
				<td align="left" valign="top" class='core'>
					<table width="100%" border="0" align="center" cellpadding="3"
						cellspacing="0" class="tabtable">
						<tr>
							<td colspan="4" align="left" valign="top"><logic:messagesPresent
									message="true">

									<html:messages id="message" message="true">

										<html:messages id="message" message="true"
											property="protocolAction_exceptionCode.2015">
											<font color="red">
												<li><bean:write name="message" /></li>
											</font>
										</html:messages>
										<html:messages id="message" message="true"
											property="lockDeleted">
											<font color="red">
												<li><bean:write name="message" /></li>
											</font>
										</html:messages>
									</html:messages>
								</logic:messagesPresent></td>
						</tr>
						<logic:messagesNotPresent message="true">
							<tr>
								<td colspan="4" align="left" valign="top">
									<table width="100%" border="0" cellpadding="0" cellspacing="0"
										class="tableheader">
										<tr>
											<td><bean:message
													key="protocolAction.header.completeSubmission" /></td>
										</tr>
									</table>
								</td>
							</tr>

							<%if("NO".equalsIgnoreCase(submissionQnrCompleted)){%>
							<tr>
								<td><font color="red"> <% 
                       if(mandaroryQnrMessage != null && !"".equals(mandaroryQnrMessage)){%>
										<li><%=mandaroryQnrMessage%></li> <%}%> <%if(incompleteQnrMessage != null && !"".equals(incompleteQnrMessage)){%>
										<li><%=incompleteQnrMessage%></li> <%}%> <%if(newVersionQnrMessage != null && !"".equals(newVersionQnrMessage)){%>
										<li><%=newVersionQnrMessage%></li>

								</font></td>
							</tr>
							<%} %>
							<tr>
								<td height='40'></td>
							</tr>
							<%}else{%>
							<tr>
								<td class="copybold" height="20" class='savebutton'>
									&nbsp;&nbsp;<bean:message
										key="protocolAction.msg.completeSubmission" />
								</td>
							</tr>


							<tr class='table'>
								<td colspan='4' class='savebutton'><html:button
										property="Save" value="OK" styleClass="clsavebutton"
										onclick="completeSubmission()" disabled="false" /> <html:button
										property="Cancel" value="Cancel" styleClass="clsavebutton"
										onclick="cancelCompletion()" /></td>
							</tr>
							<script>document.protocolActionsForm.Save.focus();</script>
							<%}%>

						</logic:messagesNotPresent>
					</table>
				</td>
			</tr>

		</table>

	</html:form>

	<iframe width=174 height=189 name="gToday:normal:agenda.js"
		id="gToday:normal:agenda.js" src="ipopeng.htm" scrolling="no"
		frameborder="0"
		style="visibility: visible; z-index: 999; position: absolute; left: -500px; top: 0px;">
	</iframe>


</body>

</html:html>
