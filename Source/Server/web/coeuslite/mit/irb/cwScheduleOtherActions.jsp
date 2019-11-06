<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@page
	import="java.util.HashMap, 
java.util.ArrayList, 
java.util.Hashtable, 
java.util.Iterator, 
edu.mit.coeuslite.utils.CoeusLiteConstants,
edu.mit.coeus.irb.bean.OtherActionInfoBean,
edu.mit.coeus.utils.ComboBoxBean,
java.util.Vector"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="protocolList" scope="session" class="java.util.Vector" />
<jsp:useBean id="protocolColumnNames" scope="session"
	class="java.util.Vector" />

<%
HashMap hmOtherActions = (HashMap) request.getAttribute("otherActions");
Vector vecActionTypes = (Vector) request.getAttribute("actionTypes");
String actionTypeDesc = "";
Vector vecOtherActions;
%>

<html:html>
<head>
<title>Coeus Web</title>
<html:base />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<link href="/coeuslite/mit/utils/css/ipf.css" rel="stylesheet"
	type="text/css">
<script language="javascript" type="text/JavaScript"
	src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/sorttable.js"></script>
</head>
<body>

	<html:form action="/protocolList.do" method="post">


		<table width="800" height="100%" border="0" cellpadding="0"
			cellspacing="0" class="table">

			<tr>
				<td height="100%" align="left" valign="top">
					<table width="99%" border="0" align="center" cellpadding="0"
						cellspacing="0" class="tabtable">
						<tr>
							<td colspan="4" align="left" valign="top">
								<table width="100%" height="20" border="0" cellpadding="0"
									cellspacing="0">
									<tr>
										<td class="tableheader">Other Agenda items included in
											this meeting</td>

									</tr>
									<tr>
										<td>
											<% 
                                                 if(vecActionTypes != null && hmOtherActions != null){
                                                 ComboBoxBean comboBoxBean =null;
                                                 OtherActionInfoBean otherActionInfoBean;
                                                 HashMap hmAction = null;
                                                 for(int index = 0; index <vecActionTypes.size() ; index++){
                                                   comboBoxBean = (ComboBoxBean)   vecActionTypes.elementAt(index);
                                                   actionTypeDesc = comboBoxBean.getDescription();
                                                   vecOtherActions = (Vector) hmOtherActions.get(actionTypeDesc);
                                                   
                                                
                                             %>
										
									<tr class="copy">
										<td>&nbsp;</td>
									</tr>
									<tr class="table">

										<td>&nbsp;&nbsp; <b> <%=actionTypeDesc%>
										</b>

										</td>

									</tr>

									<%
                                               
                                               if(vecOtherActions != null && vecOtherActions.size() > 0) {   
                                                       for(int indx = 0; indx <vecOtherActions.size() ; indx++){
                                                 otherActionInfoBean = (OtherActionInfoBean )vecOtherActions.elementAt(indx);
                                                 
                                                       
                                               
                                               %>

									<tr>

										<td class="copy">
											<li><%=otherActionInfoBean.getItemDescription()%></li>
										</td>
									</tr>

									<%  }
                                                   }
                                                   }
                                                }
                                                
                                                
                                                %>

									</tr>

								</table>
							</td>
						</tr>
						</td>


					</table>
				</td>
			</tr>
			<tr>

			</tr>

		</table>


	</html:form>
</body>
</html:html>
