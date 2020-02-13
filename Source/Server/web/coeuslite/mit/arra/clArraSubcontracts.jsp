
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@page import="org.apache.struts.taglib.html.JavascriptValidatorTag"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="arraAwardSubcontracts" scope="request"
	class="java.util.Vector" />

<html:html>
<head>
<title>Coeus Web</title>
<html:base />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<script language="javascript" type="text/JavaScript"
	src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/coeusLiteCalendar.js"></script>
<script>  
     function showDetails(subContractCode){
            document.arraAwardSubcontractForm.action = "<%=request.getContextPath()%>/getSubcontractDetails.do?subcontractCode="+subContractCode;
            document.arraAwardSubcontractForm.submit();
         }
            
        </script>
</head>
<body>

	<html:form action="/getArraSubcontracts" method="post">
		<a name="top"></a>

		<table width="100%" height="100%" border="0" cellpadding="4"
			cellspacing="0" class="table">
			<tr>
				<td align="left" valign="top">
					<table width="99%" border="0" align="center" cellpadding="2"
						cellspacing="0" class="tabtable">
						<tr>
							<td align="left" valign="top">
								<table width="100%" border="0" cellpadding="0" cellspacing="0"
									class="tableheader">
									<tr>
										<td><bean:message bundle="arra"
												key="awardSubcontract.subcontractList" /></td>
									</tr>
								</table>
							</td>
						</tr>
						<logic:empty name="arraAwardSubcontracts">
							<tr align="center">
								<td class="copybold" align="left" colspan="4"><bean:message
										bundle="arra"
										key="awardSubcontract.noSubcontracts.dispalyText" /></td>
							</tr>
						</logic:empty>
						<tr align="center">
							<td>
								<table width="100%" align="center" border="0" cellpadding="3"
									cellspacing='0' class="tabtable">

									<%  String strBgColor = "#DCE5F1";
                                        int count = 0;
                                        String strsubAwdDate,awardAmount,amtDispursed;
                                        edu.mit.coeuslite.utils.DateUtils dateUtils = new edu.mit.coeuslite.utils.DateUtils();
                                        %>
									<logic:present name="arraAwardSubcontracts">
										<tr>
											<td width="30%" align="left" class="theader"><bean:message
													bundle="arra" key="awardSubcontract.subcontractorName" /></td>
											<td width="10%" align="right" class="theader"><bean:message
													bundle="arra" key="awardSubcontract.awardDate" /></td>
											<td width="15%" align="right" class="theader"><bean:message
													bundle="arra" key="awardSubcontract.awardAmount" /></td>
											<td width="15%" align="right" class="theader"><bean:message
													bundle="arra" key="awardSubcontract.amtDispursed" /></td>
											<td width="13%" align="right" class="theader"><bean:message
													bundle="arra" key="awardDetails.jobsCreated" /></td>
											<td width="16%" align="left" class="theader"></td>
										</tr>
										<logic:iterate id="awdSubcontract" indexId="index"
											name="arraAwardSubcontracts"
											type="org.apache.struts.validator.DynaValidatorForm">
											<% 
                                                if (count%2 == 0)
                                                    strBgColor = "#D6DCE5";
                                                else
                                                    strBgColor="#DCE5F1"; 
                                                %>
											<tr bgcolor="<%=strBgColor%>" valign="top"
												onmouseover="className='TableItemOn'"
												onmouseout="className='TableItemOff'">
												<td width="30%" align="left" class="copy"><bean:write
														name="awdSubcontract" property="subcontractorName" /></td>
												<%strsubAwdDate = (String)awdSubcontract.get("subAwardDate");
                                                strsubAwdDate = (strsubAwdDate==null)?"":strsubAwdDate.trim();
                                                if(!"".equals(strsubAwdDate)) {
                                                    strsubAwdDate = dateUtils.formatDate(strsubAwdDate,"MM/dd/yyyy");
                                                }
                                                if(awdSubcontract.get("subAwardAmount")!= null){
                                                    awardAmount = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(((Double)awdSubcontract.get("subAwardAmount")).doubleValue());
                                                }else{
                                                    awardAmount = "";
                                                }
                                                if(awdSubcontract.get("subAwardAmtDispursed") != null){
                                                    amtDispursed = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(((Double)awdSubcontract.get("subAwardAmtDispursed")).doubleValue()); 
                                                }else{
                                                    amtDispursed = "";
                                                }
                                                %>
												<td width="10%" align="left" class="copy"><%=strsubAwdDate%></td>
												<td width="15%" align="right" class="copy"><%=awardAmount%></td>
												<td width="15%" align="right" class="copy"><%=amtDispursed%></td>
												<td width="13%" align="right" class="copy"><bean:write
														name="awdSubcontract" property="noOfJobs" /></td>
												<%String showDetails = "javaScript:showDetails('"+awdSubcontract.get("subcontractCode")+"')";%>
												<td width="16%" align="right" valign="top" class="copy"><a
													href="<%=showDetails%>"><bean:message bundle="arra"
															key="awardSubcontract.details" /></a></td>
											<tr>
												<% count++;%>
											
										</logic:iterate>
									</logic:present>

								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</html:form>
</body>

</html:html>
