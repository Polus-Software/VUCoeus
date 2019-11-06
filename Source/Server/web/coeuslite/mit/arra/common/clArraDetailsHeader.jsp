<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@page
	import="java.sql.Date,edu.mit.coeuslite.arra.bean.ArraAwardHeaderBean, java.text.SimpleDateFormat"%>

<html>
<head>
<!-- JM 7-2-2012 changed title for metrics -->
<title>Award Details</title>
</head>
<%try{
    ArraAwardHeaderBean headerBean=(ArraAwardHeaderBean)session.getAttribute("arraAwardHeaderBean");
    String awardNumber = headerBean.getAwardNumber()==null?"":headerBean.getAwardNumber();
    String piPersonName = headerBean.getPiPersonName()==null?"":headerBean.getPiPersonName();
    String fundingAgencyCode = headerBean.getFundingAgencyCode() == null ? "": " : "+headerBean.getFundingAgencyCode();
    String sponsor  = headerBean.getSponsorCode()==null?"":(headerBean.getSponsorCode()+fundingAgencyCode+" : "+headerBean.getSponsorName());
    sponsor=sponsor.length()>70?sponsor.substring(0,71)+"...":sponsor;
    String leadUnit  = headerBean.getLeadUnitNumber()==null?"":(headerBean.getLeadUnitNumber()+" : "+headerBean.getLeadUnitName());
    leadUnit=leadUnit.length()>70?leadUnit.substring(0,71)+"...":leadUnit;
    String title = headerBean.getTitle()==null?"":headerBean.getTitle();
    //title=title.length()>70?title.substring(0,71)+"...":title;
    
    Date endDate=headerBean.getProjectEndDate();
    Date startDate = headerBean.getProjectStartDate();
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    String strEndDate = (endDate==null)?"":dateFormat.format(endDate);
    String strStartDate = (startDate==null)?"":dateFormat.format(startDate);
    
    Date awardDate = headerBean.getAwardDate();
    String strAwardDate = (awardDate==null)?"":dateFormat.format(awardDate);
    String awardAccountNumber = headerBean.getAccountNumber();
    awardAccountNumber = (awardAccountNumber==null)?"":awardAccountNumber;
    
    String lineImage = request.getContextPath()+"/coeusliteimages/line4.gif";
    //COEUSDEV-608
    String reportPeriod = strStartDate+" to "+strEndDate;
%>
<body topmargin="0" leftmargin="0">

	<table width='100%' border="0" cellpadding="2" cellspacing="0"
		class="table">
		<tr>
			<td width="15%" nowrap class='copybold'>&nbsp;&nbsp;&nbsp;<bean:message
					bundle="arra" key="headerLabel.awardNumber" />&nbsp;
			</td>
			<td width="30%" class='copy' nowrap><%=awardNumber%></td>
			<td width="10%" nowrap class='copybold' align=left><bean:message
					bundle="arra" key="headerLabel.piName" />&nbsp;</td>
			<td width="40%" class='copy' nowrap><%=piPersonName%></td>
		</tr>
		<%-- <tr>
            <td  width="15%" nowrap class='copybold' >&nbsp;&nbsp;&nbsp;<bean:message bundle="arra" key="headerLabel.projectstartdate"/>&nbsp;</td>
            <td width="30%" class='copy' nowrap ><%=strStartDate%></td>
            <td  width="10%" nowrap class='copybold' align=left><bean:message bundle="arra" key="headerLabel.projectEndDate"/>&nbsp;</td>
            <td width="40%" class='copy' nowrap><%=strEndDate%></td>
        </tr> --%>

		<tr>
			<td width="15%" nowrap class='copybold' align="left">&nbsp;&nbsp;&nbsp;<bean:message
					bundle="arra" key="awardDetails.accountNumber" />&nbsp;
			</td>
			<td width="30%" class='copy' nowrap><%=awardAccountNumber%></td>
			<td width="10%" nowrap class='copybold' align="left"><bean:message
					bundle="arra" key="awardDetails.awardDate" />&nbsp;</td>
			<td width="40%" class='copy' nowrap><%=strAwardDate%></td>
		</tr>

		<tr>
			<td class='copybold' align="left">&nbsp;&nbsp;&nbsp;<bean:message
					bundle="arra" key="headerLabel.sponsor" />&nbsp;
			</td>
			<td class='copy' colspan="3" align=left nowrap><%=sponsor%></td>
		</tr>
		<tr>
			<td class='copybold' align="left">&nbsp;&nbsp;&nbsp;<bean:message
					bundle="arra" key="headerLabel.leadUnit" />&nbsp;
			</td>
			<td class='copy' colspan="3" align=left nowrap><%=leadUnit%></td>
		</tr>
		<tr>
			<td class='copybold' valign="top" align="left">&nbsp;&nbsp;&nbsp;<bean:message
					bundle="arra" key="headerLabel.title" />&nbsp;
			</td>
			<td class='copy' colspan="3" align=left><%=title%></td>
		</tr>
		<tr>
			<%--COEUSDEV-608 - START--%>
			<td class='copybold' valign="top" align="left">&nbsp;&nbsp;&nbsp;<bean:message
					bundle="arra" key="headerLabel.reportPeriod" />&nbsp;
			</td>
			<td class='copy' colspan="3" align=left><%=reportPeriod%></td>
			<%--COEUSDEV-608 - END--%>
		</tr>
	</table>

</body>
<%}catch(Exception e){ e.printStackTrace(); }%>
</html>