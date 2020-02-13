<%@ page contentType="text/html;charset=UTF-8" language="java"
	import="java.util.Vector"%>
<%@page
	import="edu.mit.coeuslite.utils.CoeusLiteConstants,
                edu.mit.coeuslite.utils.bean.MenuBean"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>

<html:html locale="true">
<html:base />

<head>
<title>Invoice Menu</title>
</head>
<body>
	<table width="100%" height="100%" border="0" cellpadding="0"
		cellspacing="0" class="table">

		<tr>
			<td align="left" valign="top">
				<table width="93%" border="0" align="center" cellpadding="0"
					cellspacing="0">
					<jsp:useBean id="invoiceMenuItemsVector" scope="session"
						class="java.util.Vector" />

					<logic:iterate name="invoiceMenuItemsVector" id="invoiceMenuBean"
						type="edu.mit.coeuslite.utils.bean.MenuBean">
						<%
                                          String linkName = invoiceMenuBean.getMenuName();
                                          String linkValue = invoiceMenuBean.getMenuLink();
                                          String groupName = invoiceMenuBean.getGroup();%>
						<%if(invoiceMenuBean.isVisible()){%>
						<tr>
							<td width="19" align="left" valign="top"><img
								src="<bean:write name='ctxtPath'/>/coeusliteimages/img_blank.gif"
								width="5" height="19"></td>
							<td width='100%' align="left" valign="middle" class="menu"
								STYLE="background-image:url('<bean:write name='ctxtPath'/>/coeusliteimages/tile_menu.gif');border:0">
								<html:link page="<%=linkValue%>" styleClass="copy">
									<%=linkName%>
								</html:link> </a>
							</td>
							<td width="18" align="left" valign="top"><img
								src="<bean:write name='ctxtPath'/>/coeusliteimages/img_menu_rt.gif"
								width="22" height="20"></td>
						</tr>
						<%}%>
					</logic:iterate>
					<tr>
						<td>&nbsp;</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</body>
</html:html>
