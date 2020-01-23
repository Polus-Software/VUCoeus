<%@page contentType="text/html"
	import="edu.mit.coeus.utils.CoeusFunctions,edu.mit.coeus.utils.DateUtils,java.sql.Timestamp"%>
<%@page pageEncoding="UTF-8" import="edu.mit.coeus.bean.*"%>
<%@page errorPage="ErrorPage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="person" scope="session"
	class="edu.mit.coeus.bean.PersonInfoBean" />
<jsp:useBean id="coiPerDisclosure" scope="session"
	class="org.apache.struts.validator.DynaValidatorForm" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<%String path = request.getContextPath();     %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>COI</title>
<link href="<%=path%>/coeuslite/dartmouth/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<script src="<%=path%>/coeuslite/dartmouth/utils/scripts/divSlide.js"></script>
<script src="<%=path%>/coeuslite/dartmouth/utils/scripts/Balloon.js"></script>
<script>
        function confirmaton(){
var confirmation=confirm("Do you want to certify this Financial Interest Disclosure?");
if(confirmation){
document.certification.submit();
}
else {
 return false;
}
}
        </script>
</head>
<body>
	<table width="980" align="center" height="264" border="0"
		cellpadding="0" cellspacing="0" class="table">
		<tr>
			<td height="20" class="theader" style="font-size: 14px"><bean:message
					bundle="coi" key="AnnualDisclosure.Certification.header" /></td>
		</tr>
		<tr bgcolor="#B3D5F7">
			<td height="70"><b><bean:message bundle="coi"
						key="AnnualDisclosure.Certification.subheader" />:</b><br> <bean:message
					bundle="coi" key="AnnualDisclosure.Certification.Text" /> </font></td>
		</tr>
		<tr bgcolor="#B3D5F7">
			<td align="center" height="44">
				<form name="coiPerDisclosure" action='<%=path%>/certify.do'>
					<html:hidden name="coiPerDisclosure" property="coiDisclosureNumber" />
					<html:hidden name="coiPerDisclosure" property="sequenceNumber" />
					<html:hidden name="coiPerDisclosure" property="personId" />

					<html:submit value="Certify" style="width:100"
						styleClass="clsavebutton" />
				</form>
			</td>
		</tr>
		<%
 Timestamp dbTimestamp = null;
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();        
        DateUtils date=new DateUtils();
        
 %>
		<tr bgcolor="#B3D5F7">
			<td align="center" height="21">Last Updated :<%=date.formatDate(dbTimestamp,"MM/dd/yyyy hh:mm:ss a")%></td>
		</tr>
		<tr bgcolor="#B3D5F7">
			<td align="center" height="37"><bean:message bundle="coi"
					key="AnnualDisclosure.Certification.ThanksText" /></td>
		</tr>
	</table>
	<%
if(request.getAttribute("Certification") != null){
        request.removeAttribute("Certification");
        
        String message = "Your Annual COI Certification is now complete. Thank you!";
                
        int indexOfApost = message.indexOf("'");
        if(indexOfApost != -1){
            int indexOfApostIncr = indexOfApost+1;
            int endOfMessage = message.length();
            String message1 = message.substring(0, indexOfApost);
            String message2 = message.substring(indexOfApostIncr, message.length());
            message = message1+message2;
        }
        out.print("<script language='javascript'>");
        out.print("confirm('"+message+"');");
//out.print("openwintext('FESubmitSuccess.jsp', 'getFinEnt3');");//can't use because of popup blockers
      //  out.print("window.location='"+reloadLocation+"';");
        out.print("</script>");
}

%>
</body>
</html>
