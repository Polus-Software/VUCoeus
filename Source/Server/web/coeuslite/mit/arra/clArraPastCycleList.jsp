
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@page
	import="java.util.HashMap,java.util.Date,java.text.SimpleDateFormat,
                edu.mit.coeuslite.arra.action.ArraBaseAction"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>

<jsp:useBean id="arraList" scope="request" class="java.util.Vector" />
<jsp:useBean id="arraColumnNames" scope="request"
	class="java.util.Vector" />
<jsp:useBean id="startAndEndDate" scope="request"
	class="edu.mit.coeus.arra.bean.ArraReportBean" />
<bean:size id="arraListSize" name="arraList" />

<html:html>
<head>
<title>Coeus Web</title>
<html:base />

<script language="javascript" type="text/JavaScript"
	src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/sorttable.js"></script>
<script>
         function viewXml(reportNo, mitAwardNo, reportId){
            document.arraAwardDetailsForm.action = "<%=request.getContextPath()%>/printArra.do?ACTION=VIEWXML&arraReportNo="+reportNo+"&arraReportAwardNo="+mitAwardNo+"&reportId="+reportId;
            document.arraAwardDetailsForm.submit();
         }
        </script>
</head>

<html:form action="/getArraDetails.do">
	<body>

		<table width="100%" height="100%" border="0" cellpadding="0"
			cellspacing="0" class="table">
			<tr class='copy' align="left">
				<logic:messagesPresent message="true">
					<html:messages message="true" bundle="arra" id="message"
						property="noRight">
						<td><font color="red"><li><bean:write
										name="message" /></li></font></td>
					</html:messages>
				</logic:messagesPresent>
			</tr>

			<tr>
				<td height="653" align="left" valign="top">
					<table width="100%" border="0" align="center" cellpadding="0"
						cellspacing="0" class="tabtable">
						<tr class='copybold'>
							<td></td>
						</tr>
						<% String header = (String)request.getAttribute("displayLabel");
                                                header = (header==null)?"Arra Awards":header;%>
						<tr>
							<td colspan="4" align="left" valign="top">
								<table width="100%" height="20" border="0" cellpadding="0"
									cellspacing="0" class="theader">
									<tr>
										<td>
											<%Date starDate = startAndEndDate.getReportPeriodStartDate();
                                                Date endDate = startAndEndDate.getReportPeriodEndDate();
                                                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                                                String strStartDate = (starDate==null)?"":dateFormat.format(starDate);
                                                String strEndDate = (endDate==null)?"":dateFormat.format(endDate);
                                                %> &nbsp;<%=header%>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
						</tr>
						<tr align="center">
							<td>
								<table width="100%" height="100%" border="0" cellpadding="2"
									cellspacing="0" id="t1" class="sortable">
									<tr>
										<%
                                            if(arraColumnNames != null && arraColumnNames.size()>0){
                                                for(int index=0;index<arraColumnNames.size();index++){
                                                    edu.mit.coeus.search.bean.DisplayBean displayBean = (edu.mit.coeus.search.bean.DisplayBean)arraColumnNames.elementAt(index);
                                                    if(displayBean.isVisible()){
                                                        String strColumnName = displayBean.getValue();
                                            %>
										<td nowrap class="theader"><%=strColumnName%></td>

										<%
                                                    }
                                                } %>

										<td class="theader">&nbsp;</td>
										<td class="theader">XML</td>
										<% }
                                            %>

									</tr>

									<!-- Award Rows -->
									<%  
                                        String strBgColor = "#DCE5F1";
                                        int count = 0;
                                        int lenCompare = 25;
                                        %>
									<logic:present name="arraList">
										<logic:iterate id="arraDetails" name="arraList"
											type="java.util.HashMap">
											<%
                                                if (count%2 == 0){
                                            strBgColor = "#D6DCE5";
                                                }else{
                                            strBgColor="#DCE5F1";
                                                }
                                                %>
											<tr bgcolor="<%=strBgColor%>" valign="top"
												onmouseover="className='TableItemOn'"
												onmouseout="className='TableItemOff'">
												<%  
                                                    String MITAwardNo = (String)arraDetails.get("MIT_AWARD_NUMBER");
                                                    String reportNo   = ((java.math.BigDecimal)arraDetails.get("ARRA_REPORT_NUMBER")).toString();
                                                    //ARRA PHASE 3 changes
                                                    String awardType = (String)arraDetails.get("AWARD_TYPE");
                                                    //Based on the Award type, report can be generated
                                                    String reportId = "ArraReport/ContractReport";
                                                    if(ArraBaseAction.GRANT_AWARD_TYPE.equalsIgnoreCase(awardType)){
                                                        reportId = "ArraReport/GrantLoanReport";
                                                    }
                                                    // Arra- Phase 2 Changes - Start
                                                    String versionNumber = ((java.math.BigDecimal)arraDetails.get("VERSION_NUMBER")).toString();
                                                    //String viewXml    = ((java.math.BigDecimal)arraDetails.get("VIEW_XML_RIGHT")).toString();
                                                    String hasMaintainArraRignt = ((java.math.BigDecimal)arraDetails.get("MAINTAIN_ARRA_REPORTS_RIGHT")).toString();
                                                    String isUserPiForAward = ((java.math.BigDecimal)arraDetails.get("IS_USER_PI")).toString();
                                                    // Arra- Phase 2 Changes = End                                                   
                                                    if(arraColumnNames != null && arraColumnNames.size()>0){
                                                        
                                                        for(int index=0;index<arraColumnNames.size();index++){
                                                            edu.mit.coeus.search.bean.DisplayBean displayBean = (edu.mit.coeus.search.bean.DisplayBean)arraColumnNames.elementAt(index);
                                                            if(!displayBean.isVisible())
                                                                continue;
                                                            String key = displayBean.getName();
                                                            if(key != null){
                                                                String value = arraDetails.get(key) == null ? "" : arraDetails.get(key).toString();
                                                                if("PROJECT_TITLE".equals(key)){
                                                                    lenCompare = 40;
                                                                }else{
                                                                    lenCompare = 25;
                                                                }
                                                                if(value != null && value.length() > lenCompare){
                                                                    value = value.substring(0,lenCompare-5);
                                                                    value = value+" ...";
                                                                }
                                                    %>
												<td align="left" nowrap class="copy"><a
													href="<%=request.getContextPath()%>/getArraDetails.do?arraReportNo=<%=reportNo%>&arraVersionNumber=<%=versionNumber%>&arraReportAwardNo=<%=MITAwardNo%>"><u><%=value%></u></a>
												</td>

												<%
                                                            }
                                                        } %>
												<td align="left" nowrap class="copy">&nbsp;</td>
												<td align="left" nowrap class="copy">
													<%-- Arra- Phase 2 Changes --%> <%-- <%if("1".equals(viewXml)){%> --%>
													<%if(("1".equals(hasMaintainArraRignt) ||
                                                                "1".equals(isUserPiForAward))){ %>
													<a
													href="javascript:viewXml('<%=reportNo%>','<%=MITAwardNo%>','<%=reportId%>')">
														<u>Save As</u>
												</a> <%}%>
												</td>
												<%
                                                    }
                                                    %>

											</tr>
											<% count++;%>
										</logic:iterate>
									</logic:present>
									<logic:equal name="arraListSize" value="0">
										<tr>
											<td colspan='3' height="23" align=center>
												<div>
													<bean:message bundle="proposal"
														key="generalInfoProposal.noResults" />
												</div>
											</td>
										</tr>
									</logic:equal>
								</table>
							</td>
						</tr>

						<tr>
							<td>&nbsp;</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td height='10'>&nbsp;</td>
			</tr>
		</table>
	</body>
</html:form>
</html:html>
