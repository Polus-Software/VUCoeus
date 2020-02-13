<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@page
	import="java.util.Vector, org.apache.struts.action.DynaActionForm"%>
<jsp:useBean id="vecUserUnits" scope="request" class="java.util.Vector" />


<html:html>

<%
  String strHasQnr = (String) request.getAttribute("strHasQnr");
  String strHasAttachment = (String) request.getAttribute("strHasAttachments");
  String strHasOtherAttach = (String) request.getAttribute("strHasOtherAttach");
  Boolean canCopyQnr = (Boolean) request.getAttribute("canCopyQnr");
  
  String protocol = (String)session.getAttribute("PROTOCOL_NUMBER"+session.getId());
     
  boolean hasAttachments = true;
  boolean hasOtherAttach = true;
  boolean hasQnr = true;
  
  if(strHasAttachment.equals("YES")){
     hasAttachments = false;
  }
  
  if(strHasOtherAttach.equals("YES")){
     hasOtherAttach = false;
  }
  
  
  if(strHasQnr.equals("YES")){
     hasQnr = false;
  }
 
  %>
<head>
<title>JSP Page</title>
<script>
var errorMessageDisplayed = false;
function protocolCopy(){      
  var attachment = "N";
  if (document.copyProtocolForm.attachmentFlag.checked == true) {
      attachment = "Y";
  }
  
  var otherAttchment = "N";
  if (document.copyProtocolForm.otherAttachmentFlag.checked == true) {
      otherAttchment = "Y";
  }
  
  var questionnaire = "N";
  if (document.copyProtocolForm.questionnaireFlag.checked == true) {
     
      <%if(canCopyQnr != null && !canCopyQnr.booleanValue()){ %>
      if( errorMessageDisplayed == false && document.copyProtocolForm.questionnaireFlag.checked ==  true){
         document.getElementById('main').style.display = 'none';
         document.getElementById('error').style.display = 'block';
         document.getElementById('mainbutton').style.display = 'none';
         document.getElementById('errorbutton').style.display = 'block';
         document.getElementById('unitDiv').style.display = 'none';
         errorMessageDisplayed = true;
         return;
     }  
   <%}%>
   
      questionnaire = "Y";
  }
  
  document.copyProtocolForm.action ="<%=request.getContextPath()%>/copyIacuc.do?PAGE=CP&attachment="+attachment+"&otherAttchment="+otherAttchment+"&questionnaire="+questionnaire; 
  document.copyProtocolForm.submit();

}


function protocolCopyWithoutQnr(){      
  var attachment = "N";
  if (document.copyProtocolForm.attachmentFlag.checked == true) {
      attachment = "Y";
  }
  
  var otherAttchment = "N";
  if (document.copyProtocolForm.otherAttachmentFlag.checked == true) {
      otherAttchment = "Y";
  }
  
  var questionnaire = "N";  
  
  document.copyProtocolForm.action ="<%=request.getContextPath()%>/copyIacuc.do?PAGE=CP&attachment="+attachment+"&otherAttchment="+otherAttchment+"&questionnaire="+questionnaire; 
  document.copyProtocolForm.submit();

}


function goToGeneralInfo(){
  document.copyProtocolForm.action ="<%=request.getContextPath()%>/getIacucData.do?protocolNumber=<%=session.getAttribute("PROTOCOL_NUMBER"+session.getId())%>&PAGE=G&sequenceNumber=<%=session.getAttribute("SEQUENCE_NUMBER"+session.getId())%>"; 
  document.copyProtocolForm.submit();
}

</script>

</head>
<body>

	<html:form action="/getCopyProtocol.do">
		<table width="100%" height="100%" border="0" cellpadding="0"
			cellspacing="0" class='table'>

			<tr>
				<td class='core'>
					<table width="100%" height="100%" align=center border="0"
						cellpadding="5" cellspacing="1" class='tabtable'>



						<tr>
							<td>
								<div id="helpText" class='helptext'>
									<bean:message bundle="proposal"
										key="helpTextProposal.CopyProposal" />
								</div>
							</td>
						</tr>

						<tr>

							<td>
								<div id="main" style="display: block;">
									<table width="100%" height="100%" cellpadding="0"
										cellspacing="5">


										<tr>

											<td align="left" class='copybold'><html:checkbox
													property='attachmentFlag' value="Y"
													disabled="<%=hasAttachments%>" onclick="setBudgetFlag();" />
												<bean:message bundle="iacuc" key="copyProtocol.attachments" />
											</td>
										</tr>
										<tr>
											<td align="left" class='copybold'><html:checkbox
													property='otherAttachmentFlag' value="Y"
													disabled="<%=hasOtherAttach%>" /> <bean:message
													bundle="iacuc" key="copyProtocol.otherAttachments" /></td>
										</tr>
										<tr>
											<td align="left" class='copybold'><html:checkbox
													property='questionnaireFlag' value="Y"
													disabled="<%=hasQnr%>" /> <bean:message bundle="iacuc"
													key="copyProtocol.questionnaire" /></td>
										</tr>

									</table>
								</div>
							</td>
						</tr>


						<tr>
							<td class="copybold"><div id="error" style="display: none;">
									<bean:message bundle="iacuc"
										key="copyProtocol.invalidQuestionnaire" />
								</div></td>
						</tr>
						<tr>
							<td>
					</table>
				</td>
			</tr>

			<tr>
				<td class='savebutton'>
					<div id="mainbutton" style="display: block;">
						<html:button property="Ok" styleClass="clbutton" disabled=""
							onclick="protocolCopy()">
							<bean:message bundle="iacuc" key="copyProtocol.ok" />
						</html:button>
						&nbsp;&nbsp;&nbsp;
						<%--  <html:button property="Close" styleClass="clbutton" value="Cancel" onclick="JavaScript:window.history.back();"/>  --%>
					</div>
				</td>

			</tr>

			<tr>
				<td class='savebutton'>
					<div id="errorbutton" style="display: none;">
						<html:button property="Ok" styleClass="clsavebutton"
							onclick="protocolCopyWithoutQnr()">
            Ok
        </html:button>
						&nbsp;&nbsp;&nbsp;

						<html:button property="Cancel" styleClass="clsavebutton"
							onclick="goToGeneralInfo()">
            Cancel
        </html:button>
					</div>
				</td>

			</tr>

			<tr>
				<td class='savebutton'>
					<div id="errorbutton" style="display: none;">
						<html:button property="Ok" styleClass="clsavebutton"
							onclick="protocolCopy()">
            Ok
        </html:button>
						&nbsp;&nbsp;&nbsp;

						<html:button property="Cancel" styleClass="clsavebutton"
							onclick="goToGeneralInfo()">
            Cancel
        </html:button>
						<%--  <html:button property="Close" styleClass="clbutton" value="Cancel" onclick="JavaScript:window.history.back();"/>  --%>
					</div>
				</td>

			</tr>

			<tr>
				<td width='100%'>
					<div id='unitDiv' class='core' style='display: none;'>
						<table width="100%" height="100%" align=center border="0"
							cellpadding="0" cellspacing="0" class="tabtable">
							<tr>
								<td>
									<table width="100%" align=center border="0" cellpadding="0"
										cellspacing="0" class="tableheader">
										<tr>
											<td><bean:message bundle="proposal"
													key="copyProposal.unitHeader" /></td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td height='15'></td>
							</tr>


							<tr>
								<td height='15'></td>
							</tr>
						</table>
					</div>
				</td>
			</tr>

		</table>

		<tr>
			<td height='15'></td>
		</tr>

	</html:form>
	<script>
      var help = '<bean:message bundle="proposal" key="helpTextProposal.CopyProposal"/>';
      help = trim(help);
      if(help.length == 0) {
        document.getElementById('helpText').style.display = 'none';
      }
      function trim(s) {
            return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
      }  
</script>
</body>
</html:html>
