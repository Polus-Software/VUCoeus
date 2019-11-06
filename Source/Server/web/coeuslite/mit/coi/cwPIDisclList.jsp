<%@ page
	import="org.apache.struts.validator.DynaValidatorForm,java.sql.*"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>

<%--
The taglib directive below imports the JSTL library. If you uncomment it,
you must also add the JSTL library to the project. The Add Library... action
on Libraries node in Projects view can be used to add the JSTL 1.1 library.
--%>
<%--
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<jsp:useBean id="getCompletedAnnaulsForPer" scope="session"
	class="java.util.Vector" />
<bean:size id="vectSize" name="getCompletedAnnaulsForPer" />
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JSP Page</title>
<%String path = request.getContextPath();%>
<link rel=stylesheet
	href="<%=path%>/coeuslite/mit/utils/css/coeus_styles.css"
	type="text/css">
<script>
            function rowHover(rowId, styleName) {
                elemId = document.getElementById(rowId);
                elemId.style.cursor = "hand";
                //If row is selected retain selection style
                //Apply hover Style only if row is not selected
                
                    elemId.className = styleName;
                
            }
            
        </script>
</head>

<body>

	<table width="100%" align="center" border="0" cellspacing="0"
		cellpadding="3" class="tabtable">
		<tr>
			<td width="5%" align="left" class="theader" nowrap>&nbsp;</td>
			<td width="10%" align="left" class="theader" nowrap><bean:message
					bundle="coi" key="label.disclosureNumber" /></td>
			<td width="10%" align="left" class="theader" nowrap><bean:message
					bundle="coi" key="label.appliesTo" /></td>
			<td width="10%" align="left" class="theader" nowrap><bean:message
					bundle="coi" key="label.moduleItemKey" /></td>
			<td width="10%" align="left" class="theader" nowrap><bean:message
					bundle="coi" key="label.sponsor" /></td>
			<td width="20%" align="left" class="theader" nowrap><bean:message
					bundle="coi" key="label.title" /></td>
			<td width="15%" align="left" class="theader" nowrap><bean:message
					bundle="coi" key="label.lastUpdated" /></td>
		</tr>
		<logic:present name="getCompletedAnnaulsForPer" scope="session">
			<%  
            String strBgColor = "#DCE5F1";
            //  int size=reviewAnnualDisc.size();
            //   System.out.println("VECTOR SIZE="+size);
            
            %>
			<logic:iterate id="data" name="getCompletedAnnaulsForPer"
				type="org.apache.commons.beanutils.DynaBean">
				<tr class="rowLine" onmouseover="className='rowHover rowLine'"
					onmouseout="className='rowLine'">
					<td class="copy"><a target="_parent"
						href="viewCOIDisclosureDetails.do?disclosureNo=<bean:write name="data" property="coiDisclosureNumber"/>&requestedPage=<%= request.getAttribute("requestedPage")%>">view</a>
					</td>
					<td class="copy"><bean:write name="data"
							property="coiDisclosureNumber" /></td>
					<td class="copy"><bean:write name="data" property="module" />
					</td>
					<td class="copy"><bean:write name="data"
							property="moduleItemKey" /></td>
					<td class="copy"><bean:write name="data"
							property="sponsorName" /></td>
					<td class="copy"><bean:write name="data" property="title" /></td>
					<td nowrap><bean:define id="updTS" name="data"
							property="updtimestamp" /> <%
                        if(updTS!=null){
                    %> <%=edu.mit.coeus.utils.DateUtils.formatDate(Timestamp.valueOf(updTS.toString()),"MM-dd-yyyy  hh:mm a")%>
						<%
                        }
                    %></td>
				</tr>
			</logic:iterate>
		</logic:present>
	</table>

</body>
</html>
<!-- web54602.mail.re2.yahoo.com compressed/chunked Tue Sep 11 07:29:51 PDT 2007 -->
