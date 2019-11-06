<%@page contentType="text/html"
	import="java.util.Vector,edu.dartmouth.coeuslite.coi.beans.DisclosureBean,
edu.dartmouth.coeuslite.coi.beans.DisclosureBean,edu.mit.coeus.utils.DateUtils,java.util.Calendar"%>
<%@page pageEncoding="UTF-8" import="edu.mit.coeus.bean.*"%>
<%@page errorPage="ErrorPage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="person" scope="session"
	class="edu.mit.coeus.bean.PersonInfoBean" />
<jsp:useBean id="DisclHistory" scope="session" class="java.util.Vector" />
<bean:size id="historySize" name="DisclHistory" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JSP Page</title>
<%String path = request.getContextPath();%>
<link href="<%=path%>/coeuslite/dartmouth/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<script>
            function openDisclHistory(disclNumber, sequenceNum) {
                var url = "<%=path%>/getCompleteDiscl.do?acType=H&&disclNumber="+disclNumber+"&&seqNumber="+sequenceNum;
                window.open(url, "History", "scrollbars=1,resizable=yes, width=1000, height=600, left=10, top =80");
            }
        </script>
</head>
<body>
	<table align="center" width='100%' border="0" cellpadding="0"
		cellspacing="0" class="table">
		<logic:present name="DisclHistory" scope="session">

			<%//DisclosureBean discl=(DisclosureBean)session.getAttribute("CurrDisclPer");
            
            %>
			<%--  <logic:notEqual name="disclSize" value='0'>--%>
			<tr>
				<td colspan="6" class="theader">Disclosure History</td>
			</tr>
			<%--Modified for Case#4447 : Next phase of COI enhancements - Start --%>
			<%if(DisclHistory != null && DisclHistory.size() > 0){%>
			<tr class="theader" height="20px">
				<td>&nbsp;</td>
				<%--     <td width="181" align="left" class="theaderBlue">DisclosureNumber</td>
                <td width="181" align="left" class="theaderBlue">SequenceNumber</td>--%>
				<td width="181" height="20" align="left" class="theader">Status</td>
				<td align="left" class="theader" width="183">Last Updated</td>
				<td align="left" class="theader" width="183">Updated User</td>
				<td align="left" class="theader" width="614">Expiration Date</td>
			</tr>
			<%}%>
			<%--Case#4447 - End --%>
			<%String statusStr="";%>
			<logic:iterate id="discl" name="DisclHistory"
				type="edu.dartmouth.coeuslite.coi.beans.DisclosureBean">
				<%--  <logic:iterate id="discl" name="FinDiscl" type="edu.dartmouth.coeuslite.coi.beans.DisclosureBean" >--%>
				<logic:present name="DisclStatus" scope="session">
					<logic:iterate id="status" name="DisclStatus"
						type="edu.mit.coeuslite.utils.ComboBoxBean">
						<bean:define id="statuscode" name="status" property="code" />
						<logic:equal name='statuscode'
							value='<%=discl.getDisclosureStatusCode().toString()%>'>
							<bean:define id="statusDesc" name="status" property="description" />
							<%statusStr=(String)statusDesc;%>
						</logic:equal>
					</logic:iterate>
				</logic:present>
				<bean:define id="dtUpdate" name="discl" property="updateTimestamp"
					type="java.sql.Date" />
				<%long time=dtUpdate.getTime();
                Calendar cal=Calendar.getInstance();
                
                cal.setTimeInMillis(time);
                DateUtils dtUtils=new DateUtils();
                String dtStr=dtUtils.formatCalendar(cal,"MM/dd/yyyy hh:mm:ss a");
                String coiDisclNumber=discl.getCoiDisclosureNumber();
                int seqNumber=(discl.getSequenceNumber()).intValue();
                %>
				<tr bgcolor="#D6DCE5" bordercolor="#79A5F4" height="20px" id="row0">
					<td>&nbsp;<a
						href="javascript:openDisclHistory('<%=coiDisclNumber%>','<%=seqNumber%>')">View</a>&nbsp;&nbsp;&nbsp;
					</td>
					<%--    <td width='181px' align="left"><%=discl.getCoiDisclosureNumber()%></td>
                    <td width='181px' align="left"><%=discl.getSequenceNumber()%></td>--%>
					<td width="181" height="21" align="left"><%=statusStr%></td>
					<td align="left" width="220"><%=dtStr%>&nbsp;</td>
					<td align="left" width="220"><coeusUtils:formatOutput
							name="discl" property="updateUser" /></td>
					<td align="left" width="614"><coeusUtils:formatDate
							name="discl" property="expirationDate" /></td>
				</tr>
			</logic:iterate>

			<logic:equal name="historySize" value='0'>
				<tr>
					<td height="28" class="copybold" colspan="3">There is no
						Disclosure History available.</td>
				</tr>
			</logic:equal>
		</logic:present>
		<logic:notPresent name="DisclHistory" scope="session">
			<tr>
				<td height="28" class="copybold" colspan="3">There is no
					Disclosure History available.</td>
			</tr>
		</logic:notPresent>
	</table>
</body>
</html>
