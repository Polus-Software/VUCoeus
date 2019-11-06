<%@ page import="org.apache.struts.validator.DynaValidatorForm"%>

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="edu.mit.coeus.bean.UserInfoBean"%>
<%@page errorPage="ErrorPage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>

<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="lockIdsList" scope="session" class="java.util.Vector" />
<%  UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("user"+session.getId());
        //String userId = (String)userInfoBean.getUserId().toUpperCase();
        // Added for displaying user name for user Id
        String userId = (String)session.getAttribute("loggedInUserName");
        //
%>

<html:html>
<head>
<link rel=stylesheet
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/ipf.css"
	type="text/css">
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<script language="javascript" type="text/JavaScript"
	src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/sorttable.js"></script>
<title>Current Locks</title>
<script>
 function removeLink(lockId, moduleItem){
    //alert("lockId -->>"+lockId);
    if(confirm("<bean:message key="locking.deleteMessage"/> "+ moduleItem +" ?")){
        document.lockingForm.action ="<%=request.getContextPath()%>/deleteLockId.do?selectedLockId="+lockId; 
        document.lockingForm.submit();
    }
 }

</script>

</head>
<body>
	<html:form action="/getLockIdsList.do">
		<a name="top"></a>
		<%--  ************  START OF BODY TABLE  ************************--%>
		<table width="750" border="0" cellpadding="0" cellspacing="0"
			class="table">

			<tr>
				<td height="50" align="left" valign="top"><table width="99%"
						border="0" align="center" cellpadding="0" cellspacing="0"
						class="tabtable">
						<tr>
							<td colspan="4" align="left" valign="top"><table
									width="100%" height="20" border="0" cellpadding="0"
									cellspacing="0" class="tableheader">
									<tr>
										<td><bean:message key="locking.currentLocks" /> <%=userId %>
										</td>
									</tr>
								</table></td>
						</tr>
						<tr>
							<td height='10'>&nbsp;</td>
						</tr>

						<tr align="center">
							<td>
								<table width="98%" align=center border="0" cellpadding="3"
									cellspacing='3' id="t1" class="sortable">
									<tr>
										<td width="15%" align="left" class="theader"><bean:message
												key="locking.module" /></td>
										<td width="10%" align="left" class="theader"><bean:message
												key="locking.item" /></td>
										<td width="10%" align="left" class="theader"><bean:message
												key="locking.user" /></td>
										<td width="13%" align="left" class="theader"><bean:message
												key="locking.createTimestamp" /></td>
										<td width="13%" align="left" class="theader"><bean:message
												key="locking.updateTimestamp" /></td>
										<td width="10%" align="left" class="theader">&nbsp;</td>
									</tr>

									<% String strBgColor = "#DCE5F1";
                          int count = 0;%>
									<logic:present name="lockIdsList">
										<logic:iterate id="lockIds" name="lockIdsList"
											type="org.apache.struts.validator.DynaValidatorForm">

											<% if (count%2 == 0) 
                            strBgColor = "#D6DCE5"; 
                         else 
                            strBgColor="#DCE5F1";%>

											<tr bgcolor="<%=strBgColor%>" valign="top"
												onmouseover="className='TableItemOn'"
												onmouseout="className='TableItemOff'">
												<td align="left" nowrap class="copy"><bean:write
														name="lockIds" property="module" /></td>
												<td align="left" nowrap class="copy"><bean:write
														name="lockIds" property="item" /></td>
												<td align="left" class="copy" nowrap>
													<%--bean:write name="lockIds" property="updateUser" /--%> <bean:write
														name="lockIds" property="LockUpdateUser" />
												</td>

												<td align="left" nowrap class="copy"><bean:write
														name="lockIds" property="createTimestamp" /> <%--  <coeusUtils:formatDate name="lockIds" property="createTimestamp" />  --%>
												</td>
												<td align="left" nowrap class="copy"><bean:write
														name="lockIds" property="updateTimestamp" /> <%--  <coeusUtils:formatDate name="lockIds" property="updateTimestamp" />   --%>
												</td>
												<% String lockId =(String) lockIds.get("lockId");                        
                            String module = (String) lockIds.get("module");
                            String item = (String) lockIds.get("item");
                            String moduleItem = module+" "+item;
                            String lockLink = "javaScript:removeLink('"+lockId+"', '"+moduleItem+"');"; 
                        
                        %>

												<td align="left" nowrap class="copy"><html:link
														href="<%= lockLink %>">
														<bean:message key="locking.remove" />
													</html:link></td>

											</tr>
											<%count++;%>
										</logic:iterate>
									</logic:present>
								</table>
							</td>
						</tr>
						<tr>
							<td height='10'>&nbsp;</td>
						</tr>

					</table></td>
			</tr>


			<tr>
				<td height='45' align="left">&nbsp; &nbsp; <html:link
						href="javascript:window.close();" styleClass="copybold">
						<bean:message key="locking.close" />
					</html:link>

				</td>
			</tr>


		</table>
</body>
</html:form>
</html:html>