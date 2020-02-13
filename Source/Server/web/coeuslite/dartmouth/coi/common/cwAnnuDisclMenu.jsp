<%@ page contentType="text/html;charset=UTF-8" language="java"
	import="java.util.Vector"%>
<%@page import="edu.mit.coeuslite.utils.bean.MenuBean"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="allAnnualDiscEntities" scope="session"
	class="java.util.Vector" />
<bean:size id="totalAnnualDiscEntities" name="allAnnualDiscEntities" />
<html:html locale="true">
<html:base />
<head>
<title>COI</title>
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
</head>
<body>
	<table width="100%" height="100%" border="0" cellpadding="3"
		cellspacing="0" class="table">

		<div align="left" width='50%'>
			<tr>
				<td align="left" valign="top">
					<table width="195" border="0" align="center" cellpadding="0"
						cellspacing="0" class="tabtable">
						<logic:present name="allAnnualDiscEntities" scope="session">
							<logic:iterate id="entity" name="allAnnualDiscEntities">
								<tr class="rowLine" onmouseover="className='rowHover rowLine'"
									onmouseout="className='rowLine'">
									<td width="22" height='19' align="left" valign="top"><logic:present
											name="entity" property="updated">
											<logic:equal name="entity" property="updated" value="Y">
												<img
													src="<bean:write name='ctxtPath'/>/coeusliteimages/img_saved.gif"
													width="22" height="19">
											</logic:equal>
										</logic:present></td>

									<td width="160" height='22' align="left" valign="top"><a
										href="<bean:write name="ctxtPath"/>/annDisclosure.do?entityNumber=<bean:write name="entity" property="entityNumber" />&validate=false"><bean:write
												name="entity" property="entityName" /></a></td>
								</tr>
							</logic:iterate>

						</logic:present>
						<logic:equal name="totalAnnualDiscEntities" value="0">
						</logic:equal>
					</table>
				</td>
			</tr>
		</div>
	</table>
</body>
</html:html>
