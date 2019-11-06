
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@page import="org.apache.struts.taglib.html.JavascriptValidatorTag"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@ page
	import="edu.mit.coeuslite.arra.bean.ArraAwardHeaderBean,edu.mit.coeuslite.utils.CoeusLiteConstants"%>

<html:html>
<head>
<title>Coeus Web</title>
<html:base />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<script language="javascript" type="text/JavaScript"
	src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/coeusLiteCalendar.js"></script>
<script>
        function updateData(){               
                document.arraAwardDetailsForm.action = "<%=request.getContextPath()%>/copyArraDetails.do?";
                document.arraAwardDetailsForm.submit();
            }
         function cancelCompletion(){
                document.arraAwardDetailsForm.action = "<%=request.getContextPath()%>/fetchArraDetails.do";
                document.arraAwardDetailsForm.submit();
         }
            </script>
</head>
<body>
	<% ArraAwardHeaderBean headerBean = (ArraAwardHeaderBean)session.getAttribute("arraAwardHeaderBean");
boolean arraInCompleted = false;
if(headerBean!=null && "N".equalsIgnoreCase(headerBean.getComplete())) {    
    arraInCompleted = true;
}
%>

	<html:form action="/copyArraDetails" method="post">
		<a name="top"></a>

		<table width="100%" height="100%" border="0" cellpadding="4"
			cellspacing="0" class="table">


			<tr>
				<td align="left" valign="top" class='core'>
					<table width="100%" border="0" align="center" cellpadding="3"
						cellspacing="0" class="tabtable">

						<tr>
							<td colspan="4" align="left" valign="top">
								<table width="100%" border="0" cellpadding="0" cellspacing="0"
									class="tableheader">
									<tr>
										<td><bean:message bundle="arra"
												key="arraMarkIncomplete.header" /></td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td class="copybold" height="20" class='savebutton'><bean:message
									bundle="arra" key="arraMarkIncomplete.submitwarning" /></td>
						</tr>
						<tr>
							<td class="copybold" height="20" class='savebutton'><bean:message
									bundle="arra" key="arraMarkIncomplete.submitOK" /></td>
						</tr>
						<%if(arraInCompleted){%>
						<tr>
							<td class="copybold" height="20" class='savebutton'>
								&nbsp;&nbsp;<bean:message bundle="arra"
									key="arraMarkIncomplete.alreadySubmitted" />
							</td>
						</tr>
						<%}%>
						<tr class='table'>
							<td colspan='4' class='savebutton'>
								<%--   <%if(!modeValue){%>
                <html:button property="Save" value="OK" styleClass="clsavebutton" onclick="updateData();"/>
                 <%}else{%>
                 <html:submit property="Save" value="OK" styleClass="clsavebutton"/>
                 <%}%> --%> <html:button property="Save" value="OK"
									styleClass="clsavebutton" onclick="updateData();" /> <html:button
									property="Cancel" value="Cancel" styleClass="clsavebutton"
									onclick="cancelCompletion();" />
							</td>
						</tr>

					</table>
				</td>
			</tr>
			<script>document.arraAwardDetailsForm.Save.focus();</script>
		</table>

	</html:form>

	<iframe width=174 height=189 name="gToday:normal:agenda.js"
		id="gToday:normal:agenda.js" src="ipopeng.htm" scrolling="no"
		frameborder="0"
		style="visibility: visible; z-index: 999; position: absolute; left: -500px; top: 0px;">
	</iframe>


</body>

</html:html>
