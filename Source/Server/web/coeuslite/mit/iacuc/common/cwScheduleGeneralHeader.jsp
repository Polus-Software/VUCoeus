<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@page
	import="edu.mit.coeus.iacuc.bean.ScheduleDetailsBean,
edu.mit.coeus.utils.CoeusConstants,
java.text.SimpleDateFormat"%>
<%



ScheduleDetailsBean headerBean =(ScheduleDetailsBean)session.getAttribute("scheduleHeader"+session.getId());
String strMeetingDate ="";
String strScheduleDate = "";
String strSubmissionDeadlineDate = "";
String scheduledPlace = "";
String committeeName = "";

SimpleDateFormat dateFormat;
if(headerBean.getScheduleDate() !=null){
    dateFormat = new SimpleDateFormat(CoeusConstants.DEFAULT_DATE_FORMAT);
    strScheduleDate = dateFormat.format(headerBean.getScheduleDate());
}

if(headerBean.getMeetingDate() !=null){
    dateFormat = new SimpleDateFormat(CoeusConstants.DEFAULT_DATE_FORMAT);
    strMeetingDate = dateFormat.format(headerBean.getMeetingDate());
}

if(headerBean.getProtocolSubDeadLine() !=null){
    dateFormat = new SimpleDateFormat(CoeusConstants.DEFAULT_DATE_FORMAT);
    strSubmissionDeadlineDate = dateFormat.format(headerBean.getProtocolSubDeadLine());
}

//Modified for COEUSQA-2292 : Agenda attachment available to reviewers from Lite - Start
scheduledPlace = headerBean.getScheduledPlace() == null ? "" : headerBean.getScheduledPlace().toString();
//COEUSQA-2292 : End
if(scheduledPlace !=  null && scheduledPlace.length() > 35){
    scheduledPlace = scheduledPlace.substring(0, 32)+"...";
}

committeeName = headerBean.getCommitteeName();
if(committeeName !=  null && committeeName.length() > 35){
    committeeName = committeeName.substring(0, 32)+"...";
}

%>
<html>
<head>
<title>Schedule Info</title>
</head>

<body>
	<table width='100%' height='100%' border='0' cellpadding='0'
		cellspacing='0' class='table'>
		<tr>

			<td align="left" width='15%' nowrap class='copybold'>&nbsp;&nbsp;&nbsp;<bean:message
					key="scheduleHeader.scheduleId" />:
			</td>
			<td align="left" class='copy' width='20%' colspan='1' nowrap><%=headerBean.getScheduleId()%>
			</td>

			<td align="left" width='15%' nowrap class='copybold'><bean:message
					key="scheduleHeader.scheduledDate" />:</td>
			<td align="left" class='copy' width='20%' nowrap><%=strScheduleDate%>
			</td>

			<td align="left" width='15%' nowrap class='copybold'><bean:message
					key="scheduleHeader.scheduledPlace" />:</td>
			<td align="left" class='copy' width='20%' nowrap><%=scheduledPlace%>
			</td>

		</tr>

		<tr>

			<td align="left" width='15%' class='copybold' nowrap>&nbsp;&nbsp;&nbsp;<bean:message
					key="scheduleHeader.committeeId" />:&nbsp;
			</td>
			<td class='copy' width='20%' colspan='1' nowrap><%=headerBean.getCommitteeId()%></td>


			<td align="left" width='15%' class='copybold' nowrap><bean:message
					key="scheduleHeader.committeeName" />:&nbsp;</td>
			<td class='copy' width='20%' nowrap><%=committeeName%></td>

			<td align="left" width='15%' class='copybold' nowrap><bean:message
					key="scheduleHeader.protocolSubmissionDeadline" />:</td>
			<td class='copy' width='20%' nowrap><%=strSubmissionDeadlineDate%></td>
		</tr>
	</table>
</body>
</html>