<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@page
	import="java.util.Vector,org.apache.struts.validator.DynaValidatorForm"%>
<html:html>
<head>
<title>Approve Invoice</title>
<script>
            var errValue = false;
            function saveInvoice(invMode){
            document.subcontractInvoice.action = "<%=request.getContextPath()%>/saveInvoice.do?invoiceSaveMode="+invMode;
            document.subcontractInvoice.submit();
            }
            function goToSummary(lineNumber,seqNumber,subcontractId){
            document.subcontractInvoice.action = "<%=request.getContextPath()%>/getSubcontractInvSummary.do?invoiceSeqNum="+seqNumber+
                                                "&invoiceLineNum="+lineNumber+"&invoiceSubId="+subcontractId+"&Menu_Id=P001";
            document.subcontractInvoice.submit();
        
            }
        </script>
<%String invoiceMode = (String)session.getAttribute("invoiceApproveRejection");
Vector vecDetails = (Vector)session.getAttribute("subcontractInvoiceSummary");
System.out.println("123"+vecDetails);
String mode = (String) session.getAttribute("sub_Mode"+session.getId());
boolean isReadOnly = false;
if(mode != null && mode.equals("D")){
    isReadOnly = true;
}
int lineNumber = 0;
int seqNumber = 0;
String subcontractId = "";
if(vecDetails != null && vecDetails.size()>0){
        DynaValidatorForm dynaApproveReject = (DynaValidatorForm)vecDetails.get(0);
        lineNumber = Integer.parseInt(dynaApproveReject.get("lineNumber").toString());
        seqNumber = Integer.parseInt(dynaApproveReject.get("sequenceNumber").toString());
        subcontractId = (String)dynaApproveReject.get("subcontractId");
}
String saveLink = "";
boolean setApprove = false;
if(invoiceMode !=  null){
    if(invoiceMode.equals("Approve")){
        setApprove = true;
        saveLink = "javaScript:saveInvoice('Approve');";
       }else if(invoiceMode.equals("Reject")){
           setApprove = false;
           saveLink = "javaScript:saveInvoice('Reject');";
         }
}%>
</head>
<body>
	<html:form action="/saveInvoice.do" method="post">
		<table width='100%' border='0' cellpadding='0' cellspacing='0'
			class='tabtable'>
			<tr>
				<td align=left><logic:messagesPresent message="true">
						<font color="red"> <html:messages id="message"
								message="true" bundle="coi">
								<li><bean:write name="message" /></li>
							</html:messages>
						</font>
					</logic:messagesPresent></td>
			</tr>
			<tr>
				<td align=center class='copybold' height='25'><%=invoiceMode%>
					the Invoice</td>
			</tr>
			<tr>
				<td align=center><html:textarea property="approvalComments"
						disabled="<%=isReadOnly%>" styleClass="copy" cols="150" rows="5"
						onchange="dataChanged()" /></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td align=center><html:button property="Save"
						value="<%=invoiceMode%>" disabled="<%=isReadOnly%>"
						onclick="<%=saveLink%>" styleClass="clsavebutton" />

					&nbsp;&nbsp;&nbsp; <%String backToSummary = "javaScript:goToSummary('"+lineNumber+"','"+seqNumber+"','"+subcontractId+"');";%>
					<html:button property="cancel" value="Back"
						styleClass="clsavebutton" onclick="<%=backToSummary%>" /></td>

			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
		</table>
	</html:form>

	<script>
              DATA_CHANGED = 'false';              
              if(errValue){
                    DATA_CHANGED = 'true';
              }
              LINK = "<%=request.getContextPath()%>/saveInvoice.do?invoiceSaveMode=<%=invoiceMode%>";                    
              FORM_LINK = document.subcontractInvoice;              
              PAGE_NAME = 'Invoice <%=invoiceMode%> comments';              
              function dataChanged(){            
                    DATA_CHANGED = 'true';
              }
              linkForward(errValue);
        </script>

</body>
</html:html>