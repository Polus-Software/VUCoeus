
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@page import="org.apache.struts.taglib.html.JavascriptValidatorTag"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@ page
	import="edu.mit.coeuslite.arra.bean.ArraAwardHeaderBean,edu.mit.coeuslite.utils.CoeusLiteConstants"%>
<%@page import="edu.mit.coeus.bean.CoeusReportGroupBean, java.util.*"%>
<html:html>
<head>
<title>Coeus Web</title>
<html:base />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<script language="javascript" type="text/JavaScript"
	src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/coeusLiteCalendar.js"></script>
<script>
        function updateData(){
                document.arraAwardDetailsForm.action = "<%=request.getContextPath()%>/markComplete.do?";
                document.arraAwardDetailsForm.submit();
            }
            </script>
</head>
<body>
	<% ArraAwardHeaderBean headerBean = (ArraAwardHeaderBean)session.getAttribute("arraAwardHeaderBean");
boolean modeValue=false;
boolean arraCompleted = false;
String mode=(String)session.getAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId());
if(headerBean!=null && "Y".equalsIgnoreCase(headerBean.getComplete()) || "S".equalsIgnoreCase(headerBean.getComplete())) {
    modeValue=true;
    arraCompleted = true;
}else if(CoeusLiteConstants.DISPLAY_MODE.equalsIgnoreCase(mode)){
    modeValue=true;
}else if(CoeusLiteConstants.MODIFY_MODE.equalsIgnoreCase(mode)){
    modeValue=false;
}
%>

	<html:form action="/fetchArraDetails" method="post">
		<a name="top"></a>

		<table width="100%" height="100%" border="0" cellpadding="4"
			cellspacing="0" class="table">

			<tr>
				<td colspan="2" class="theader">Print Arra Award Report</td>
			</tr>
			<tr>

				<td align="left" valign="top" class='core'>
					<table width="100%" border="0" align="center" cellpadding="0"
						cellspacing="0" class="tabtable">

						<%
            CoeusReportGroupBean repGrpBean = (CoeusReportGroupBean)request.getAttribute("Reports");
            if(repGrpBean != null && repGrpBean.getReports()!= null) {
                Set set = repGrpBean.getReports().keySet();
                Iterator iterator = set.iterator();
                String key, id, dispValue;
                CoeusReportGroupBean.Report repBean;
                int index = 0;
                while(iterator.hasNext()) {
                    key = (String)iterator.next();
                    repBean = (CoeusReportGroupBean.Report)repGrpBean.getReports().get(key);
                    id = repBean.getId();
                    dispValue = repBean.getDispValue();
                   
        %>

						<tr class="rowLine" id="row<%=index%>"
							onmouseover="rowHover('row<%=index%>','rowHover rowLine')"
							onmouseout="rowHover('row<%=index%>','rowLine')">
							<td class="copybold">
								<%if(dispValue.equalsIgnoreCase("Grant Loan Report")){%> <a
								href='<%=request.getContextPath()%>/printArraReport.do?repId=<%=id%>'
								target='_blank'><%=dispValue%></a><br> <%} else {%> <%=dispValue%>
								<%}%>
							</td>
						</tr>
						<% 
            index = index + 1;
            }
            }
        %>


					</table>
				</td>
			</tr>

		</table>

	</html:form>

	<iframe width=174 height=189 name="gToday:normal:agenda.js"
		id="gToday:normal:agenda.js" src="ipopeng.htm" scrolling="no"
		frameborder="0"
		style="visibility: visible; z-index: 999; position: absolute; left: -500px; top: 0px;">
	</iframe>


</body>

</html:html>
