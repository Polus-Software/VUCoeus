<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page
	import="java.util.Vector,
                edu.mit.coeuslite.utils.bean.PlaceHolderBean,
                edu.mit.coeuslite.utils.CoeusLiteConstants"%>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>

<html>
<head>
</head>
<body>

	<%
    String mode=(String)session.getAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId());
            boolean modeValue=false;
            if(mode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE)){
                modeValue=true;
            }else if(mode.equalsIgnoreCase(CoeusLiteConstants.MODIFY_MODE)){
                modeValue=false;
            }else if(mode.equalsIgnoreCase(CoeusLiteConstants.ADD_MODE)){
                modeValue=false;
              }
%>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="1%" nowrap class="copy"><bean:message
						key="generalInfoLabel.OrgType" /> <%--Organization Type--%>:</td>
				<td class="copy"><html:select property="protocolOrgTypeCode"
						styleClass="textbox-longer" disabled="<%=modeValue%>">
						<html:options collection="OrganizationTypes" property="code"
							labelProperty="description" />
					</html:select></td>
			</tr>
			<tr>
				<td></td>
			</tr>
			<tr>
				<td nowrap class="copy"><bean:message
						key="generalInfoLabel.OrgName" /> <%--Organization Name:--%> :</td>
				<td valign="top" nowrap class="copy"><html:text
						name="iacucGeneralInfoForm" property="organizationName"
						styleClass="textbox-longer" maxlength="6" readonly="true"
						size="48" /> <%if(!modeValue){%> <a
					href="javascript:organization_search();" class="copysmall"> <bean:message
							key="label.search" />
				</a> <%}%></td>
			</tr>
			<tr>
				<td nowrap class="copy">&nbsp;</td>
				<td colspan="2" nowrap class="copy">&nbsp;</td>
			</tr>

		</table>
</body>
</html>
