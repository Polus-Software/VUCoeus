<%@ page contentType="text/html;charset=UTF-8" language="java"
	import="java.util.Vector"%>
<%@page
	import="edu.mit.coeuslite.utils.CoeusLiteConstants,
edu.mit.coeuslite.utils.bean.MenuBean"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="scheduleMenuItemsVector" scope="session"
	class="java.util.Vector" />

<% 
String scheduleId = (String)session.getAttribute("SCHEDULE_ID"+session.getId());
String contextPath =  request.getContextPath();
String agendaNum = (String)session.getAttribute("AGENDA_NUMBER"+session.getId());
%>
<html:html locale="true">
<html:base />

<head>
<title>Schedule Menu</title>
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<style type="text/css">
</style>
</head>
<body>
	<table width="100%" height="150%" border="0" cellpadding="0"
		cellspacing="0" class="table">

		<tr>
			<td align="left" valign="top">
				<table width="170" border="0" align="center" cellpadding="0"
					cellspacing="0">
					<tr class="coeusMenuSeparator">
						<td colspan='3' height='3'></td>
					</tr>
					<logic:iterate name="scheduleMenuItemsVector" id="schedulemenubean"
						type="edu.mit.coeuslite.utils.bean.MenuBean">
						<bean:define id="menuId" name="schedulemenubean" property="menuId" />

						<tr class="coeusMenuSeparator">
							<td colspan='3' height='3'></td>
						</tr>
						<tr>
							<td width="16%" height='19' align="left" valign="bottom"
								class="coeusMenu"><logic:equal name="schedulemenubean"
									property="dataSaved" value="true">
									<img
										src="<bean:write name='ctxtPath'/>/coeusliteimages/img_saved.gif"
										width="19" height="19">
								</logic:equal></td>
							<td width="80%" height='20' align="left" valign="top"
								class="coeusMenu"><bean:define id="name"
									name="schedulemenubean" property="menuName" /> <%--COEUSQA-2292 Agenda attachment available to reviewers from Lite START --%>
								<bean:define id="menuId" name="schedulemenubean"
									property="menuId" /> <%--COEUSQA-2292 Agenda attachment available to reviewers from Lite End --%>
								<bean:define id="link_Value" name="schedulemenubean"
									property="menuLink" /> <bean:define id="selected"
									name="schedulemenubean" property="selected" /> <bean:define
									id="fieldName" name="schedulemenubean" property="fieldName" />
								<bean:define id="dataSaved" name="schedulemenubean"
									property="dataSaved" /> <%if(((Boolean)selected).booleanValue()){%>
								<font color="#6D0202"><b> <%=name%>
								</b> </fornt> <%} else {%> <%-- Modified for COEUSQA-2292 Agenda attachment available to reviewers from Lite Start --%>
									<b> <%if(menuId.equals("A003")){%> <%String agendaDisp1ay= (String)name;%>
										<%if(agendaNum!=null && agendaDisp1ay!=null && agendaDisp1ay.equalsIgnoreCase("View Agenda") && Integer.parseInt(agendaNum)==0) {%>
										<a href="javascript:noAgenda()"> <%=name%>
									</a> <%} else {%> <a
										href='<%=contextPath%><%=link_Value%>&scheduleId=<%=scheduleId%>&SUBHEADER_ID=SH008&isAgenda=true&openAgenda=true'>
											<%=name%>
									</a> <%}%> <%}else{%> <a
										href='<%=contextPath%><%=link_Value%>&scheduleId=<%=scheduleId%>'>
											<%=name%>
									</a> <%}%>
								</b> <%-- Modified for COEUSQA-2292 Agenda attachment available to reviewers from Lite End --%>
									<%}%></td>

							<td width='4%' align=right nowrap class="selectedMenuIndicator">
								<logic:equal name="schedulemenubean" property="selected"
									value="true">
									<bean:message key="menu.selected" />
								</logic:equal>
							</td>
						</tr>
						<tr class="coeusMenuSeparator">
							<td colspan='3' height='1'></td>
						</tr>
					</logic:iterate>
				</table>
			</td>
		</tr>
	</table>
	<script>
        function noAgenda(){
         alert('<bean:message key="schedule_no_agenda_details"/>');
         
        }
    </script>
</body>


</html:html>
