<%@ page contentType="text/html;charset=UTF-8" language="java"
	import="java.util.Vector"%>
<%@page
	import="edu.mit.coeuslite.utils.CoeusLiteConstants,
                edu.mit.coeuslite.utils.bean.MenuBean,edu.mit.coeuslite.utils.CoeusliteMenuItems"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="negotiationMenuItems" scope="session"
	class="java.util.Vector" />

<% 
    String negotiationNumber = (String)session.getAttribute("negotiationNumber");
    String leadUnit = (String)session.getAttribute("leadUnit");
    Vector menuList = (Vector) session.getAttribute("negotiationMenuItems");
    int index=2;
%>
<html:html locale="true">
<html:base />

<head>
<title>Untitled</title>
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<style type="text/css">
</style>

</head>
<body>
	<table width="100%" height="100%" border="0" cellpadding="2"
		cellspacing="0" class="table" align="center">

		<tr>
			<td align="left" valign="top" height="425px">
				<table width="100%" border="0" align="center" cellpadding="0"
					cellspacing="0">
					<logic:iterate name="negotiationMenuItems" id="negotiationMenuBean"
						type="edu.mit.coeuslite.utils.bean.MenuBean">
						<bean:define id="menuId" name="negotiationMenuBean"
							property="menuId" />
						<% String linkName = negotiationMenuBean.getMenuName();
                             String group = negotiationMenuBean.getGroup();
                             group = (group==null)?"":group.trim();
                          %>
						<%if(index==Integer.parseInt(group)){%>
						<tr>
							<td>&nbsp;</td>
						</tr>
						<%index++;}%>
						<%if(negotiationMenuBean.isVisible()){%>
						<!-- <tr class="coeusMenuSeparator">
                                       <td colspan='3' height='2'>
                                       </td>
                                       </tr>  -->
						<tr>
							<td width="12%" height='19' align="left" valign="bottom"
								class="coeusMenu"><logic:equal name="negotiationMenuBean"
									property="dataSaved" value="true">
									<img
										src="<bean:write name='ctxtPath'/>/coeusliteimages/img_saved.gif"
										width="19" height="23">
								</logic:equal></td>
							<td width="80%" height='20' align="left" valign="middle"
								class="coeusMenu"><bean:define id="name"
									name="negotiationMenuBean" property="menuName" /> <bean:define
									id="link_Value" name="negotiationMenuBean" property="menuLink" />
								<bean:define id="selected" name="negotiationMenuBean"
									property="selected" /> <bean:define id="fieldName"
									name="negotiationMenuBean" property="fieldName" /> <bean:define
									id="dataSaved" name="negotiationMenuBean" property="dataSaved" />
								<%
                                        String absPath = request.getContextPath()+link_Value+"NEGOTIATION_NUMBER="+negotiationNumber.trim()+"&LEAD_UNIT_NUMBER="+leadUnit.trim();
                                        if(((Boolean)selected).booleanValue()){%>
								<font color="#6D0202"><b> <%
                                               String starIndicator = linkName.substring(0,1);
                                               String newLink = linkName.substring(1,linkName.length());
                                            %> <%if(starIndicator.equals("*")){%>
										<font color="red">*</font><%=newLink%> <%}else{%> <%=name%> <%}%>
								</b></font> <%} else {%> <%
                                                String starIndicator = linkName.substring(0,1);
                                                String newLink = linkName.substring(1,linkName.length());                                                
                                            %> <%if(starIndicator.equals("*")){%>
								<a href="<%=absPath%>" class='copy'> <font color="red">*</font><%=newLink%>
							</a> <%}else{%> <a href="<%=absPath%>" class='copy'> <%=name%>
							</a> <%}%> <%}%></td>
							<td width='4%' align=right nowrap class="selectedMenuIndicator">
								<logic:equal name="negotiationMenuBean" property="selected"
									value="true">
									<bean:message key="menu.selected" />
								</logic:equal>
							</td>
						</tr>
						<tr class="coeusMenuSeparator">
							<td colspan='3' height='1'></td>
						</tr>
						<%}%>
					</logic:iterate>
				</table>
			</td>
		</tr>
	</table>
</body>
</html:html>
