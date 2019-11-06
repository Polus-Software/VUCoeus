
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@page
	import="org.apache.struts.taglib.html.JavascriptValidatorTag,
                edu.mit.coeuslite.arra.action.ArraBaseAction"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="pastArraAwardDetails" scope="request"
	class="java.util.Vector" />

<html:html>
<head>
<title>Coeus Web</title>
<html:base />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<script language="javascript" type="text/JavaScript"
	src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/coeusLiteCalendar.js"></script>
<script>  
                 
        </script>
</head>
<script>
    function openLookupWindow(linkValue) {
    var winleft = (screen.width - 650) / 2;
    var winUp = (screen.height - 450) / 2;  
    var win = "scrollbars=1,resizable=0,width=810,height=450,left="+winleft+",top="+winUp
    link = linkValue;        
    sList = window.open(link, "list", win);
    }
    </script>
<body>

	<html:form action="/getPastReports" method="post">
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
												key="arra.pastReports.heading" /></td>
									</tr>
								</table>
							</td>
						</tr>
						<logic:empty name="pastArraAwardDetails">
							<tr align="center">
								<td class="copybold" align="left" colspan="4"><bean:message
										bundle="arra" key="arra.pastReports.noPastReports" /></td>
							</tr>
						</logic:empty>
						<tr align="center">
							<td>
								<table width="100%" align="center" border="0" cellpadding="3"
									cellspacing='0' class="tabtable">

									<%  String strBgColor = "#DCE5F1";
                                        int count = 0;
                                        String strPeriodStart,strPeriodEnd,strSubmissionDate;
                                        edu.mit.coeuslite.utils.DateUtils dateUtils = new edu.mit.coeuslite.utils.DateUtils();
                                        %>
									<logic:present name="pastArraAwardDetails">
										<tr>
											<!--<td width="20%" align="left" class="theader"><bean:message bundle="arra" key="arra.pastReports.periodStart"/></td>-->
											<td width="20%" align="left" class="theader"><bean:message
													bundle="arra" key="arra.pastReports.periodEnd" /></td>
											<td width="20%" align="left" class="theader"><bean:message
													bundle="arra" key="arra.pastReports.submissionDate" /></td>
											<td width="20%" align="left" class="theader"><bean:message
													bundle="arra" key="arra.pastReports.versionNumber" /></td>
											<td width="10%" align="left" class="theader"></td>
											<td width="10%" align="left" class="theader"></td>
										</tr>
										<logic:iterate id="pastAwdReports" indexId="index"
											name="pastArraAwardDetails"
											type="org.apache.struts.validator.DynaValidatorForm">
											<% 
                                                if (count%2 == 0)
                                                    strBgColor = "#D6DCE5";
                                                else
                                                    strBgColor="#DCE5F1"; 
                                                %>
											<%strPeriodStart = (String)pastAwdReports.get("reportPeriodStart");
                                                strPeriodStart = (strPeriodStart==null)?"":strPeriodStart.trim();
                                                if(!"".equals(strPeriodStart)) {
                                                    strPeriodStart = dateUtils.formatDate(strPeriodStart,"MM/dd/yyyy");
                                                }
                                                strPeriodEnd = (String)pastAwdReports.get("reportPeriodEnd");
                                                strPeriodEnd = (strPeriodEnd==null)?"":strPeriodEnd.trim();
                                                if(!"".equals(strPeriodEnd)) {
                                                    strPeriodEnd = dateUtils.formatDate(strPeriodEnd,"MM/dd/yyyy");
                                                }
                                                strSubmissionDate = (String)pastAwdReports.get("submissionDate");
                                                strSubmissionDate = (strSubmissionDate==null)?"":strSubmissionDate.trim();
                                               // if(!"".equals(strSubmissionDate)) {
                                                   // strSubmissionDate = dateUtils.formatDate(strSubmissionDate,"MM/dd/yyyy");
                                               // }                                               
                                                
                                                %>
											<% String arraReportNo = ((Integer) pastAwdReports.get("arraReportNumber")).toString();
                                                String arraReportAwardNo = (String) pastAwdReports.get("mitAwardNumber");
                                                String currentVersionNumber = ((Integer) pastAwdReports.get("versionNumber")).toString();
                                                String awardType = (String)pastAwdReports.get("awardType");
                                                //Based on the Award type, report can be generated
                                                String reportId = "ArraReport/ContractReport";
                                                if(ArraBaseAction.GRANT_AWARD_TYPE.equalsIgnoreCase(awardType)){
                                                    reportId = "ArraReport/GrantLoanReport";
                                                } %>

											<% String absPath = request.getContextPath()+"/printArraReportForVersion.do?repId="+reportId+"&arraReportNo="+arraReportNo+"&arraReportAwardNo="+arraReportAwardNo+"&arraVersionNumber="+currentVersionNumber;
                                                String viewPath = request.getContextPath()+"/getArraDetailsForVersion.do?&arraReportNo="+arraReportNo+"&arraReportAwardNo="+arraReportAwardNo+"&arraVersionNumber="+currentVersionNumber;
                                                
                                                %>

											<tr bgcolor="<%=strBgColor%>" valign="top"
												onmouseover="className='TableItemOn'"
												onmouseout="className='TableItemOff'">
												<!--<td width="20%" align="left" class="copy"><%=strPeriodStart%></td>-->
												<td width="20%" align="left" class="copy"><%=strPeriodEnd%></td>

												<td width="20%" align="left" class="copy"><%=strSubmissionDate%></td>
												<td width="20%" align="left" class="copy"><bean:write
														name="pastAwdReports" property="versionNumber" /></td>

												<td width="10%" align="left" class="copy"><a
													href="javaScript:openLookupWindow('<%=viewPath%>');" /><u>View</u></a></td>
												<td width="10%" align="left" valign="top" class="copy"><a
													href="<%=absPath%>" target='_blank' /><u>Print</u></a></td>
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
