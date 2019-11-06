<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%@ page import="org.apache.struts.validator.DynaValidatorForm"%>
<%@page errorPage="ErrorPage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="DisclosureInfoDetail" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="DisclosureInfoHistory" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="COIHeaderInfo" scope="session" class="java.util.Vector" />
<jsp:useBean id="person" scope="session"
	class="edu.mit.coeus.bean.PersonInfoBean" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%String path = request.getContextPath();%>
<link rel=stylesheet
	href="<%=path%>/coeuslite/mit/utils/css/coeus_styles.css"
	type="text/css">
<title>History</title>
<script language="JavaScript">

         function open_new_window(link)
             {
                var winleft = (screen.width - 850) / 2;
                var winUp = (screen.height - 650) / 2;  
                var win = "scrollbars=1,resizable=1,width=975,height=650,left="+winleft+",top="+winUp
                sList = window.open(link, "list", win);
                if (parseInt(navigator.appVersion) >= 4) {
                    window.sList.focus(); 
                    }
             }

        </script>
</head>
<body class="table">
	<b>History of changes:</b>
	<!-- History Details - START -->

	<table width="100%" border="0" cellpadding="3" class="tabtable">
		<tr>
			<td width="15%" class="theader"><bean:message bundle="coi"
					key="label.relationShipToEntity" /></td>
			<td width="20%" align="left" class="theader"><bean:message
					bundle="coi" key="label.conflictStatus" /></td>
			<td width="15%" align="left" class="theader"><bean:message
					bundle="coi" key="label.reviewedBy" /></td>
			<td width="30%" align="left" class="theader"><bean:message
					bundle="coi" key="label.relationshipDesc" /></td>
			<td width="20%" align="left" class="theader"><bean:message
					bundle="coi" key="label.lastUpdate" /></td>
			<%--td width="10%" align="left" class="theader"><bean:message bundle="coi" key="label.userName"/></td--%>
		</tr>

		<logic:present name="DisclosureInfoHistory" scope="session">
			<%  int disclIndex = 0;
                String strBgColor = "#DCE5F1";
                
                %>
			<logic:iterate id="infoHistory" name="DisclosureInfoHistory"
				type="org.apache.commons.beanutils.DynaBean">

				<%                                  
                    if (disclIndex%2 == 0) {
                    strBgColor = "#D6DCE5";
                    } else {
                    strBgColor="#DCE5F1";
                    }
                    %>

				<tr bgcolor='<%=strBgColor%>' class="rowLine"
					onmouseover="className='rowHover rowLine'"
					onmouseout="className='rowLine'">
					<td width="15%" class="copy"><coeusUtils:formatOutput
							name="infoHistory" property="relationShipTypeDesc" /></td>
					<td width="20%" class="copy">
						<%     
                            java.util.Map params = new java.util.HashMap();
                            
                            params.put("entityNumber", infoHistory.get("entityNumber"));
                            params.put("seqNum", infoHistory.get("sequenceNum"));
                            params.put("entitySeqNum", infoHistory.get("entitySequenceNumber"));
                            pageContext.setAttribute("paramsMap", params);     
                            
                            %> <%--     <html:link  action="/viewFinEntity.do" name="paramsMap" scope="page" onclick="open_new_window(/viewFinEntity.do+paramsMap)">
                                                        <bean:write name="infoHistory" property="coiStatus"/>  </html:link> --%>
						<!--a href="JavaScript:open_new_window('<bean:write name='ctxtPath'/>/viewFinEntityDisplay.do?entityNumber=<bean:write name='infoHistory' property='entityNumber'/>&seqNum=<bean:write name='infoHistory' property='sequenceNum' />&entitySeqNum=<bean:write name='infoHistory' property='entitySequenceNumber'/>&header=no' ,'EntityDetails');"  method="POST" -->
						<a
						href="JavaScript:open_new_window('<bean:write name='ctxtPath'/>/viewFinEntityDisplay.do?entityNumber=<bean:write name='infoHistory' property='entityNumber'/>&seqNum=<bean:write name='infoHistory' property='entitySequenceNumber' />&header=no' ,'EntityDetails');"
						method="POST"> <u> <bean:write name="infoHistory"
									property="coiStatus" />
						</u>
					</a>
					</td>
					<td width="15%" class="copy"><bean:write name="infoHistory"
							property="coiReviewer" /></td>
					<td width="30%" class="copy"><bean:write name="infoHistory"
							property="description" /></td>
					<td width="20%" class="copy" nowrap><coeusUtils:formatDate
							name="infoHistory" formatString="MM-dd-yyyy  hh:mm a"
							property="updtimestamp" /> &nbsp; <bean:write name="infoHistory"
							property="upduser" /></td>
					<%--td class="copy"> <bean:write name="infoHistory" property="upduser"/> </td--%>
				</tr>
				<% disclIndex++ ;%>
			</logic:iterate>
		</logic:present>
	</table>

	<!-- History Details -End -->


</body>
</html>
