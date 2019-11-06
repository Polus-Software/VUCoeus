
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
                document.arraAwardDetailsForm.action = "<%=request.getContextPath()%>/markComplete.do?";
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
boolean modeValue=false;
boolean arraCompleted = false;
if(headerBean!=null && "Y".equalsIgnoreCase(headerBean.getComplete())){
    arraCompleted = true;
}
String mode=(String)session.getAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId());
if(headerBean!=null && "Y".equalsIgnoreCase(headerBean.getComplete()) || "S".equalsIgnoreCase(headerBean.getComplete())) {
    modeValue=true;    
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
				<td align="left" valign="top" class='core'>
					<table width="100%" border="0" align="center" cellpadding="3"
						cellspacing="0" class="tabtable">

						<tr>
							<td colspan="4" align="left" valign="top">
								<table width="100%" border="0" cellpadding="0" cellspacing="0"
									class="tableheader">
									<tr>
										<td><bean:message bundle="arra" key="arraSubmit.header" /></td>
									</tr>
								</table>
							</td>
						</tr>
						<%if(!modeValue){%>
						<tr>
							<td class="copybold" height="20" class='savebutton'><bean:message
									bundle="arra" key="arraSubmit.submitwarning" /></td>
						</tr>
						<tr>
							<td class="copybold" height="20" class='savebutton'><bean:message
									bundle="arra" key="arraSubmit.submitOK" /></td>
						</tr>
						<%}else if(arraCompleted){%>
						<tr>
							<td class="copybold" height="20" class='savebutton'>
								&nbsp;&nbsp;<bean:message bundle="arra"
									key="arraSubmit.alreadySubmitted" />
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
									styleClass="clsavebutton" onclick="updateData();"
									disabled="<%=modeValue || arraCompleted%>" /> <html:button
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
