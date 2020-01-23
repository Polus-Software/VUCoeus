<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%String path = request.getContextPath();%>
<link rel=stylesheet
	href="<%=path%>/coeuslite/mit/utils/css/coeus_styles.css"
	type="text/css">
<title>Financial Entity History</title>
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
<body class="table">
	<b>Related Disclosures:</b>
	<logic:equal name="certificatesSize" value="0">
		<table width="100%" align="right" border="0">
			<tr>
				<td align='center'><bean:message bundle="coi"
						key="financialEntity.noFinancialEnt" /></td>
			</tr>
		</table>
	</logic:equal>
	<logic:present name="ReviewDisclosureHistory" scope="request">
		<table width="100%" align="center" border="0" cellspacing="0"
			cellpadding="3" class="tabtable">
			<tr>
				<td width="5%" align="left" class="theader" nowrap>&nbsp;</td>
				<td width="10%" align="left" class="theader" nowrap><bean:message
						bundle="coi" key="label.disclosureNumber" /></td>
				<td width="15%" align="left" class="theader" nowrap><bean:message
						bundle="coi" key="label.status" /></td>
				<%--td width="10%" align="left" class="theader" nowrap><bean:message bundle="coi" key="label.appliesTo"/></td--%>
				<td width="10%" align="left" class="theader" nowrap><bean:message
						bundle="coi" key="label.moduleItemKey" /></td>
				<td width="10%" align="left" class="theader" nowrap><bean:message
						bundle="coi" key="label.sponsor" /></td>
				<td width="30%" align="left" class="theader" nowrap><bean:message
						bundle="coi" key="label.title" /></td>
				<td width="20%" align="left" class="theader" nowrap><bean:message
						bundle="coi" key="label.lastUpdated" /></td>
				<%--td width="10%" align="left" class="theader" nowrap><bean:message bundle="coi" key="label.userName"/></td--%>
			</tr>
			<%String discNum, reqDiscNum;%>
			<logic:iterate id="data" name="ReviewDisclosureHistory"
				type="org.apache.commons.beanutils.DynaBean" indexId="ctr">
				<%
                discNum = (String)data.get("disclNo");
                reqDiscNum = (String)request.getParameter("disclosureNum");
                if(reqDiscNum != null && !reqDiscNum.startsWith(discNum)) {
                %>

				<tr class="rowLine" onmouseover="className='rowHover rowLine'"
					onmouseout="className='rowLine'">
					<td class="copy"><a target="_parent"
						href="viewCOIDisclosureDetails.do?disclosureNo=<bean:write name="data" property="disclNo"/>">view</a>
					</td>
					<td class="copy"><bean:write name="data" property="disclNo" />
					</td>
					<td class="copy"><bean:write name="data" property="coiStatus" />
					</td>
					<%--td class="copy">
                        <bean:write name="data" property="module"/>
                    </td--%>
					<td class="copy"><bean:write name="data"
							property="moduleItemKey" /></td>
					<td class="copy"><bean:write name="data"
							property="sponsorName" /></td>
					<td class="copy"><bean:write name="data" property="title" /></td>
					<td nowrap><coeusUtils:formatDate name="data"
							formatString="MM-dd-yyyy  hh:mm a" property="updtimestamp" /> <bean:write
							name="data" property="upduser" /></td>
					<%--td>
                        <bean:write name="data" property="upduser"/>
                    </td--%>
				</tr>

				<%}%>

			</logic:iterate>
			</logic:present>
		</table>
</body>

</html>
