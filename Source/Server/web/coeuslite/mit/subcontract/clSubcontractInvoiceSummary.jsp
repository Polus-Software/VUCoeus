<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@page
	import="java.util.Vector, org.apache.struts.validator.DynaValidatorForm"%>
<html:html>
<head>
<!--Added for Viewing Invoice Document - Start-->
<%
boolean isDocPresent = new Boolean(session.getAttribute("subcontractInvoiceDocument").toString()).booleanValue();
%>
<script>
    function viewDocument(){
        window.open("<%=request.getContextPath()%>/viewInvoice.do");
    }
</script>
<!--Added for Viewing Invoice Document - End-->
</head>
<body>
	<html:form action="/getSubcontractInvSummary.do">
		<table width="100%" border="0" cellpadding="4" cellspacing="0"
			class="tabtable">
			<tr>
				<td class="theader" height="20"><bean:message
						key="Subcontract.Label.SubcontractInvoiceDetails"
						bundle="subcontract" /></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td align=center>
					<table width="100%" border="0" cellpadding="4" cellspacing="0"
						class="tabtable">
						<tr class="table">
							<td height="20" class="theader">&nbsp;&nbsp;<bean:message
									key="Subcontract.Label.SubcontractSummary" bundle="subcontract" />
							</td>
						</tr>
						<logic:present name="subcontractInvoiceSummary">
							<logic:iterate id="summary" name="subcontractInvoiceSummary"
								type="org.apache.struts.validator.DynaValidatorForm">
								<tr>
									<td align=center>
										<table width="100%" border="0" cellpadding="4" cellspacing="0"
											class="tabtable">
											<tr>
												<td width="18%" class="copybold"><bean:message
														key="Subcontract.Label.SubcontractId" bundle="subcontract" />
													:</td>
												<td align=left class="copy"><bean:write name="summary"
														property="subcontractId" /></td>
												<td width="18%" class="copybold"><bean:message
														key="Subcontract.Label.SequenceNumber"
														bundle="subcontract" /> :</td>
												<td align=left class="copy"><bean:write name="summary"
														property="sequenceNumber" /></td>
											</tr>
											<tr>
												<td class="copybold"><bean:message
														key="Subcontract.Label.SubcontractStatus"
														bundle="subcontract" /> :</td>
												<td align=left class="copy"><bean:write name="summary"
														property="subcontractStatus" /></td>
												<td class="copybold"><bean:message
														key="Subcontract.Label.SubAwardType" bundle="subcontract" />
													:</td>
												<td align=left class="copy"><bean:write name="summary"
														property="subAwardType" /></td>
											</tr>
											<tr>
												<td class="copybold"><bean:message
														key="Subcontract.Label.Subcontractor" bundle="subcontract" />
													:</td>
												<td align=left colspan="3" class="copy"><bean:write
														name="summary" property="subcontractor" /></td>
											</tr>
											<tr>
												<td class="copybold"><bean:message
														key="Subcontract.Label.RequisitionerName"
														bundle="subcontract" /> :</td>
												<td align=left colspan="3" class="copy"><bean:write
														name="summary" property="requisitionerName" /></td>
											</tr>
											<tr>
												<td class="copybold"><bean:message
														key="Subcontract.Label.RequisitionerUnit"
														bundle="subcontract" /> :</td>
												<td align=left colspan="3" class="copy"><bean:write
														name="summary" property="requistionerUnit" /></td>
											</tr>
										</table>
									</td>
								</tr>
								<tr class="table">
									<td height="20" class="theader">&nbsp;&nbsp;<bean:message
											key="Subcontract.Label.InvoiceSummary" bundle="subcontract" />
									</td>
								</tr>
								<tr>
									<td align=center>
										<table width="100%" border="0" cellpadding="4" cellspacing="0"
											class="tabtable">
											<tr>
												<td width="18%" class="copybold"><bean:message
														key="Subcontract.Label.InvoiceNumber" bundle="subcontract" />
													:</td>
												<td align=left class="copy"><bean:write name="summary"
														property="invoiceNumber" /></td>
												<td class="copybold"><bean:message
														key="Subcontract.Label.InvoiceAmount" bundle="subcontract" />
													:</td>
												<td align=left class="copy"><bean:write name="summary"
														property="invoiceAmount" /></td>
												<td class="copybold"><bean:message
														key="Subcontract.Label.InvoiceStatus" bundle="subcontract" />
													:</td>
												<td align=left class="copy"><bean:write name="summary"
														property="invoiceStatus" /></td>
											</tr>
											<tr>
												<td class="copybold"><bean:message
														key="Subcontract.Label.InvoiceStartDate"
														bundle="subcontract" />:</td>
												<td align=left class="copy"><coeusUtils:formatDate
														name="summary" property="invoiceStartDate" /></td>
												<td class="copybold"><bean:message
														key="Subcontract.Label.InvoiceEndDate"
														bundle="subcontract" /> :</td>
												<td align=left class="copy"><coeusUtils:formatDate
														name="summary" property="invoiceEndDate" /></td>
												<td class="copybold"><bean:message
														key="Subcontract.Label.InvoiceEffectiveDate"
														bundle="subcontract" /> :</td>
												<td align=left class="copy"><coeusUtils:formatDate
														name="summary" property="invoiceEffectiveDate" /></td>
											</tr>
											<!--Added for Viewing Invoice Document - Start-->
											<tr>
												<%if(isDocPresent){%>
												<td class="copybold">Subcontract Invoice:</td>
												<td colspan="5">
													<%String viewDocument = "javaScript:viewDocument();";%> <html:link
														href="<%=viewDocument%>">
														<u>View</u>
													</html:link>
												</td>
												<%}else{%>
												<td class="copybold">Subcontract Invoice:</td>
												<td colspan="5"></td>
												<%}%>
											</tr>
											<!--Added for Viewing Invoice Document - End-->
										</table>
									</td>
								</tr>
							</logic:iterate>
						</logic:present>
					</table>
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
		</table>
		<script>
              linkForward(false);
        </script>
	</html:form>
</body>
</html:html>