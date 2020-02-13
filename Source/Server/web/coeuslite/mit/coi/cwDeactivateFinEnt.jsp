
<%@ page import="org.apache.struts.validator.DynaValidatorForm"%>

<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="entityDetails" scope="session" class="java.util.Vector" />

<html:html>
<head>
<title>Coeus Web</title>
<html:base />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
</head>
<body>
	<html:form action="/deactivateFinEntSubmit.do" method="post">

		<table width="980" border="0" cellpadding="0" cellspacing="0"
			class="table">

			<% String actionFrom = request.getParameter("actionFrom");
        String entityName = (String)request.getAttribute("entityName");
        pageContext.setAttribute("entityName",entityName);
        pageContext.setAttribute("actionFrom",actionFrom);
        %>
			<tr>
				<td height="5" align="left" valign="top" class="theader"><bean:message
						bundle="coi" key="financialEntity.headerFinEnt" /> <%=entityName %>
				</td>
			</tr>

			<tr>
				<td height="20" align="left" valign="top"><br>
					<table width="99%" border="0" align="center" cellpadding="3"
						cellspacing="0" class="tabtable">

						<tr>
							<td height='20' class='copy'><bean:message bundle="coi"
									key="financialEntity.deActivateMsg" /></td>
						</tr>
						<tr class='copy' align="left">
							<td class="copy">
								<% boolean present =false ;%> <logic:messagesPresent
									message="true">
									<% present = true ; %>

								</logic:messagesPresent> <% if(!present){ %> <bean:message bundle="coi"
									key="label.inactiveSatus" /> <%}else{%> <font color='red'>
									<bean:message bundle="coi" key="label.inactiveSatus" />
							</font> <%}%>
							</td>
						</tr>
						<input type="hidden" name='actionFrom' value="<%=actionFrom%>">
						<input type="hidden" name='entityName' value="<%=entityName%>">

						<tr>
							<td class="copy"><html:textarea property="statusDesc"
									cols="80" rows="3" styleClass="copy" /></td>
						</tr>
						<tr>
							<td colspan="0" nowrap class="copy" align="center"></td>
						</tr>


					</table></td>
			</tr>

			<tr>
				<td height='20' nowrap class="savebutton">&nbsp;<html:submit
						property="save" value="Save" />
				</td>
			</tr>

			<!--<tr>
              <td height='10'>
                  &nbsp;
              </td>
          </tr> -->

		</table>

	</html:form>
</body>
</html:html>
