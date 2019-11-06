<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@ page import="java.util.ListIterator"%>
<%@ page
	import="org.apache.struts.validator.DynaValidatorForm, java.util.Vector"%>
<%@ page import="edu.mit.coeus.utils.CoeusVector"%>
<%@ page import="edu.mit.coeuslite.utils.CoeusLiteConstants"%>
<%@ page import="edu.mit.coeus.utils.CoeusConstants"%>

<jsp:useBean id="amendRenevData" scope="request"
	class="java.util.Vector" />
<html:html>
<head>
<title>Amendments/Renewal Summary</title>
<%
        String modeVal = (String)session.getAttribute("iacucNewAmendRenew");// this is for setting mode value
        modeVal = modeVal!=null ? modeVal : "";
%>
<script language="javascript" type="text/JavaScript" src="_js.js"></script>
<script language="JavaScript">
function protocolHistory(){
    document.iacucAmendmentRenewalForm.action = "<%=request.getContextPath()%>/getIacucAmendmentRenewal.do?page=AR";
    document.iacucAmendmentRenewalForm.submit();
}

function view_data(activityType,docType,versionNumber, sequenceNumber,documentId, isParent){ 
    window.open("<%=request.getContextPath()%>/iacucviewUploadAction.do?&docType="+docType+"&acType=V&versionNumber="+versionNumber+"&SeqNumber="+sequenceNumber+"&Parent="+isParent+"&protocDocId="+documentId); 
}

var obj = new Array();
var index = 0;

function setForm(formData){
   /* alert(formData);
    for(count=0 ; count < index ; count++){
        var inputs = formData.getElementsByTagName("input");   
        for(var t = 0;t < inputs.length;t++){
            if(inputs[t].type == "checkbox" && inputs[t].name == obj[count]){
                inputs[t].checked = true;
                break;
            }
        }
    }*/
    for(count=0 ; count < index ; count++){
        var inputs = document.getElementsByName(obj[count]);
        inputs[0].checked = true;
    }
}

    function viewAnsweredQuestionsForQnr(qnrLink,completed,applicableSubmoduleCode,applicableModuleItemKey,apModuleSubItemKey) {
      
                var w = 830;
                var h = 513;
                if(navigator.appName == "Microsoft Internet Explorer") {
                    w = 830;
                    h = 563;
                }
                if (window.screen) {
                     /*leftPos = Math.floor(((window.screen.width )/ 2));
                    topPos = Math.floor(((window.screen.height) ));
                    alert(leftPos);*/
                    
                    leftPos = 115;
                    topPos = 120;
                }
               
                
                /*
                 //Default w,h is for 1024 by 768 pixels screen resolution
                var width = 220;
                var height = 475;
                var screenWidth = window.screen.width;
                var screenHeight = window.screen.height;
                if(screenWidth == 800 && screenHeight == 600){
                    width = 2;
                    height = 300;
                } else if(screenWidth == 1152 && screenHeight == 864){
                    width = 350;
                    height = 565;
                } else if(screenWidth == 1280 && screenHeight == 720){
                    width = 475;
                    height = 425;
                }else if(screenWidth == 1280 && screenHeight == 768){
                   width = 475;
                   height = 475;
                }else if(screenWidth == 1280 && screenHeight == 1024){
                   width = 475;
                    height = 725;
                }
                //width, height is for pop-up dialog 
                var width =  Math.floor(((screenWidth - width) / 2));
                var height = Math.floor(((screenHeight - height) / 2));
                
                */
                <%
                String protocolAmendmentNumber = request.getParameter("protocolAmendRenNumber");
                %>
                var loc = '<bean:write name='ctxtPath'/>/coeuslite/mit/utils/dialogs/cwViewIacucAnsweredQuestionsForQnr.jsp?actionFrom=IACUC_AMENDMENT_REWNEWAL_HISTORY&protocolAmendmentNumber=<%=protocolAmendmentNumber%>&link='+qnrLink+'&apSubModuleCode='+applicableSubmoduleCode+'&apModuleItemKey='+applicableModuleItemKey+'&apModuleSubItemKey='+apModuleSubItemKey+'&completed='+completed;
                var newWin = window.open(loc,"formName",'dependent=1,toolbar=0,location=0,directories=0,status=0, menubar=0,scrollbars=1,resizable=0,width=' + w + ',height=' + h+ ',left='+ leftPos + ',top=' + topPos);
                newWin.window.focus();
         
            }
</script>
</head>
<html:form action="/getIacucAmendmentRenewal.do" focus="summary">
	<body onload="setForm(this)">

		<%  
    
    Vector vecData =(Vector) session.getAttribute("iacucAmendRenevData");
    int iacucSummaryCount = Integer.parseInt((String)session.getAttribute("iacucSummaryCount"));
    Vector editedModulesForAmendments =(Vector) session.getAttribute("iacucEditedModulesForAmendments");
    Vector documentsForAmendments =(Vector) session.getAttribute("iacucDocumentsForAmendments");
    CoeusVector questionnaireVector = (CoeusVector)request.getAttribute("questionnaireVector");
    String EMPTY_STRING = "";
    String summaryData = EMPTY_STRING;
    DynaValidatorForm dynaFormModule = (DynaValidatorForm) session.getAttribute("iacucAmendRenewModulesSummary");
    DynaValidatorForm dynaForm = null;
    
    if(vecData!=null && vecData.size()>0 && vecData.size()>= iacucSummaryCount) {
        dynaForm =(DynaValidatorForm) vecData.get(iacucSummaryCount);
        summaryData = (String) dynaForm.get("summary");
    }
    %>

		<table width="100%" cellpadding='0' cellspacing='0' class='tabtable'>
			<tr>
				<td>
					<table width="100%" height="20" border="0" cellpadding="0"
						cellspacing="0" class="tableheader">
						<tr>
							<td>
								<%
                    String artype = (String)request.getAttribute("ARTYPE");
                    if(artype != null && artype.equals(Character.toString(CoeusConstants.IACUC_AMENDMENT))){
                    %> <bean:message
									key="label.AmendRenew.AmendmentSummery" /> <%}else if(artype != null && artype.equals(Character.toString(CoeusConstants.IACUC_RENEWAL))){%>
								<bean:message key="label.AmendRenew.RenewalSummery" /> <%--CoeusQA-1724-Added for new Amendment type-Start--%>
								<%}else if(artype!=null && artype.equals(Character.toString(CoeusConstants.IACUC_RENEWAL_WITH_AMENDMENT))) {%>
								<bean:message key="label.AmendRenew.AmendRenewSummary"
									bundle="iacuc" /> <%}else if(artype!=null && artype.equals(Character.toString(CoeusConstants.IACUC_CONTINUATION_CONTINUING_REVIEW))) {%>
								<bean:message key="label.AmendRenew.contRevSummary"
									bundle="iacuc" /> <%}else if(artype!=null && artype.equals(Character.toString(CoeusConstants.IACUC_CONTI_CONTINUING_REVIEW_WITH_AMEND))) {%>
								<bean:message key="label.AmendRenew.contRevAmendSummary"
									bundle="iacuc" /> <%}%> <%--CoeusQA-1724-Added for new Amendment type-End--%>
							</td>
							<td align="right"><html:link
									href="javaScript:protocolHistory();">
									<bean:message key="label.AmendRenew.ReturnToProtocolHistory" />
								</html:link></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table width="99%" height="100%" align=center border="0"
						cellpadding="1" cellspacing="1">
						<tr>
							<td>
								<%--html:textarea name="iacucAmendmentRenewalForm" property="summary" value="<%=summaryData%>" disabled="<%=modeValue%>" styleClass="copy" cols="150" rows="25" onchange="dataChanged()"/--%>
								<html:textarea name="iacucAmendmentRenewalForm"
									property="summary" value="<%=summaryData%>" disabled="true"
									styleClass="copy" cols="165" rows="5" /> <script>
                        if(navigator.appName == "Microsoft Internet Explorer")
                        {
                            if(document.iacucAmendmentRenewalForm.summary){
                            document.iacucAmendmentRenewalForm.summary.cols=188;
                            document.iacucAmendmentRenewalForm.summary.rows=5;
                                }
                        }                    
                </script>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td height="5"></td>
			</tr>

			<tr>
				<td class='tableheader'><bean:message
						key="label.AmendRenew.Module" /></td>
			</tr>

			<%if(editedModulesForAmendments != null && editedModulesForAmendments.size() > 0){%>
			<% 
for(ListIterator iteretor = editedModulesForAmendments.listIterator();iteretor.hasNext();){
String module = (String) iteretor.next();
%>
			<tr>
				<td><%= module%></td>
			</tr>
			<%}%>
			<%}else{%>
			<%-- Added for internal issue fix 1890 begin--%>
			<tr>
				<td>
					<%
        String artypeName = (String)request.getAttribute("ARTYPE");
        if(artypeName != null && artypeName.equals(Character.toString(CoeusConstants.IACUC_AMENDMENT))){
        %> <bean:message bundle="iacuc"
						key="label.AmendRenew.noDocEdit.amendment" /> <%}else if(artypeName != null && artypeName.equals(Character.toString(CoeusConstants.IACUC_RENEWAL))){%>
					<bean:message bundle="iacuc"
						key="label.AmendRenew.noDocEdit.renewal" /> <%} else if(artypeName!=null && artypeName.equals(Character.toString(CoeusConstants.IACUC_RENEWAL_WITH_AMENDMENT))) {%>
					<bean:message bundle="iacuc"
						key="label.AmendRenew.noDocEdit.amendRenew" /> <%--CoeusQA-1724-Added for new Amendment type-Start--%>
					<%} else if(artypeName!=null && artypeName.equals(Character.toString(CoeusConstants.IACUC_CONTINUATION_CONTINUING_REVIEW))) {%>
					<bean:message bundle="iacuc"
						key="label.AmendRenew.noDocEdit.contReview" /> <%} else if(artypeName!=null && artypeName.equals(Character.toString(CoeusConstants.IACUC_CONTI_CONTINUING_REVIEW_WITH_AMEND))) {%>
					<bean:message bundle="iacuc"
						key="label.AmendRenew.noDocEdit.contReviewAmend" /> <%}%> <%--CoeusQA-1724-Added for new Amendment type-End--%>
				</td>
			</tr>
			<%-- Added for internal issue fix 1890 end--%>
			<%}%>
			<% 
    String strBgColor = "#DCE5F1";
    int count = 0;
 %>
			<tr>
				<td height="5"></td>
			</tr>
			<logic:notEmpty name="documentsForAmendments">
				<tr class='tableheader'>
					<td><bean:message key="label.AmendRenew.Attachments" /></td>
				</tr>
				<tr>
					<td height="5"></td>
				</tr>
				<tr>
					<table border="0" width="100%" class='tabtable' cellspacing="0">
						<tr class='tableheader'>
							<td width="40%"><bean:message
									key="label.AmendRenew.Description" /></td>
							<td width="40%"><bean:message
									key="label.AmendRenew.Filename" /></td>
						</tr>
						<logic:iterate id="uploadDocList" name="documentsForAmendments"
							type="edu.mit.coeus.iacuc.bean.UploadDocumentBean">
							<% 
          String docDescription = uploadDocList.getDescription();
          String fileName = uploadDocList.getFileName();
           if (count%2 == 0) 
                strBgColor = "#D6DCE5"; 
           else 
                strBgColor="#DCE5F1";
         %>
							<tr>
								<td width="40%" valign="top" align="left" class="copy"><%=docDescription%>
								</td>
								<td width="40%" valign="top" align="left">
									<%
              String viewLink = "javascript:view_data('V','" +uploadDocList.getDocCode() +"',"+"'"+uploadDocList.getVersionNumber()+"','"+uploadDocList.getSequenceNumber()+"','"+uploadDocList.getDocumentId()+"','N')";
             %> <html:link href="<%=viewLink%>">
										<%=fileName%>
									</html:link>

								</td>
							</tr>
						</logic:iterate>
						<tr>
							<td height=2></td>
						</tr>
					</table>
				</tr>
		</table>
		<table class="tabtable" width="99%">
			</logic:notEmpty>
			<logic:notEmpty name="questionnaireVector">
				<tr>
					<td class='tableheader' colspan="2"><bean:message
							key="label.AmendRenew.QuestionnaireForms" /></td>
				</tr>
				<logic:iterate id="questionnaire" name="questionnaireVector"
					type="edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean">
					<tr>
						<%
            String label = questionnaire.getLabel();
            String questionA = questionnaire.getQuestionnaireCompletionId();
            String completed = questionnaire.getQuestionnaireCompletionFlag();
            int applicableSubmoduleCode  = questionnaire.getApplicableSubmoduleCode();
            String applicableModuleItemKey = questionnaire.getApplicableModuleItemKey();
            int apModuleSubItemKey = questionnaire.getApplicableModuleSubItemKey();
            String qnrLink = "/getIacucSubmissionQuestionnaire.do?questionnaireId="+questionnaire.getQuestionnaireId()+"&questionaireLabel="+questionnaire.getLabel();
            String alert = "This form " +label+ " is not answered for this protocol";
            %>
						<td width="100%" valign="top" align="left">
							<% 
            if(questionA == null){
             %> <a href="javascript:alert('<%= alert %>');"><%= label %></a>
							<% 
            }else{
             %> <a
							href="javaScript:viewAnsweredQuestionsForQnr('<%=qnrLink%>','<%=completed%>','<%=applicableSubmoduleCode%>','<%=applicableModuleItemKey%>','<%=apModuleSubItemKey%>')"><%= label %></a>
							<%}%>
						</td>
						<td height=5></td>
					</tr>
					<tr>
						<td height="5"></td>
					</tr>
				</logic:iterate>
			</logic:notEmpty>
		</table>
		<script>
        function validate() {
            return true;
        }
 </script>
	</body>
</html:form>
</html:html>
