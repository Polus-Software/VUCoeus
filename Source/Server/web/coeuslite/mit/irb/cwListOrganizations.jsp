<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page
	import="java.util.Vector,
                edu.mit.coeuslite.utils.bean.PlaceHolderBean"%>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<html>
<head>
<title>JSP Page</title>
</head>
<body>
	<table width="95%" border="0" cellpadding="0" class="tablemainsub2">
		<tr>
			<td width="30%" align="left" class="tableheadersub3"><bean:message
					key="generalInfoLabel.OrgType" /> <%--Organization Type--%></td>
			<td width="15%" align="left" class="tableheadersub3"><bean:message
					key="generalInfoLabel.OrgId" /> <%--Organization Id--%></td>
			<td width="30%" align="left" class="tableheadersub3"><bean:message
					key="generalInfoLabel.OrgName" /> <%--Organization Name--%></td>
			<%if(!modeValue){%>
			<td width="10%" align="left" class="tableheadersub3">&nbsp;</td>
			<%}%>

		</tr>
		<% String strBgColor = "#EFEFEF"; //BGCOLOR=EFEFEF  FBF7F7
              int count = 0;
            %>
		<logic:present name="organizations">
			<logic:iterate id="organizationBean" name="organizations"
				type="org.apache.struts.validator.DynaValidatorForm">
				<% 
                        if (count%2 == 0) 
                          strBgColor = "FBF7F7"; 
                        else 
                          strBgColor="EFEFEF"; 
                      %>
				<tr bgcolor="<%=strBgColor%>" valign="top">
					<td align="left" nowrap class="copy"><%=organizationBean.get("organizationTypeName")%>
					</td>
					<td align="left" nowrap class="copy"><%=organizationBean.get("organizationId")%>
					</td>
					<td align="left" nowrap class="copy"><%=organizationBean.get("organizationName")%>
					</td>
					<td align="center" nowrap class="copy">
						<%if(!modeValue){%> <a
						href="javascript:delete_data('D','<%=organizationBean.get("organizationId")%>','<%=organizationBean.get("locationUpdateTimestamp")%>')">
							<bean:message key="fundingSrc.Remove" /> <%--Remove--%>
					</a> <%}%>
					</td>
				</tr>
				<% count++;%>
			</logic:iterate>
		</logic:present>
	</table>
</body>
</html>
