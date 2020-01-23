<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%@ page
	import="org.apache.struts.validator.DynaValidatorForm,
java.util.Vector"%>
<%@page errorPage="ErrorPage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>

<jsp:useBean id="EntityProjects" scope="session"
	class="java.util.Vector" />
<bean:size id="totalEntityProjects" name="EntityProjects" />
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%String path = request.getContextPath();%>
<link rel=stylesheet
	href="<%=path%>/coeuslite/mit/utils/css/coeus_styles.css"
	type="text/css">
<title>History</title>
<script language="JavaScript">
            function view_comments(val,relationShip,module,moduleItemKey) {
                var value;
                var module=module;
                var moduleItemKey=moduleItemKey;
                var relationShip=relationShip;
                if(val.length<1000)
                    {value=val; }
               else
                   {value=val.substring(0,1000);}
               var w = 550;
               var h = 148;
               
               if(navigator.appName == "Microsoft Internet Explorer") {
                    w = 522;
                    h = 131;
                }
               if (window.screen) {
                leftPos = Math.floor(((window.screen.width - 500) / 2));
                topPos = Math.floor(((window.screen.height - 350) / 2));
               }
                var loc = '<bean:write name='ctxtPath'/>/coeuslite/dartmouth/utils/dialogs/cwProjectComments.jsp?relationShip='+relationShip+'&moduleItemKey='+moduleItemKey+'&module='+module +'&value='+value +'&type=S'+'&disabled=true';;
                var newWin = window.open(loc,"formName",'dependent=1,toolbar=0,location=0,directories=0,status=0, menubar=0,scrollbars=0,resizable=0,width=' + w + ',height=' + h+ ',left='+ leftPos + ',top=' + topPos);
                newWin.window.focus();
         
            }
        </script>
</head>
<body class="table">

	<b><bean:message bundle="coi" key="label.entityprojects" /></b>
	<table width="100%" border="0" cellpadding="3" class="tabtable">

		<%
            int index = 0;
            int disclIndex = 0;
            String strBgColor = "#DCE5F1";
            Vector entityPrj = (Vector)session.getAttribute("EntityProjects");
            if(entityPrj == null || (entityPrj != null && entityPrj.size() < 1)){
            %>
		<tr bgcolor='<%=strBgColor%>' class="rowLine"
			onmouseover="className='rowHover rowLine'"
			onmouseout="className='rowLine'">
			<td><bean:message bundle="coi"
					key="Annualdisclosure.Review.Entity.noPrj" /></td>
		</tr>
		<%}else{%>
		<logic:present name="EntityProjects" scope="session">
			<tr>
				<td width="15%" class="theader"><bean:message bundle="coi"
						key="label.sponsor" /></td>
				<td width="30%" align="left" class="theader"><bean:message
						bundle="coi" key="label.title" /></td>
				<td width="15%" align="left" class="theader"><bean:message
						bundle="coi" key="label.conflictStatus" /></td>
				<td width="25%" align="left" class="theader"><bean:message
						bundle="coi" key="label.awardProposalProtocol" /></td>
				<td width="20%" align="left" class="theader"><bean:message
						bundle="coi" key="label.number" /></td>
				<td width="10%" align="left" class="theader"><bean:message
						bundle="coi" key="label.comments" /></td>
			</tr>

			<logic:iterate id="disclosure" name="EntityProjects"
				type="org.apache.commons.beanutils.DynaBean">
				<%
                    
                    String EMPTY_STRING = "";
                    if (index % 2 == 0) {
                        strBgColor = "#D6DCE5";
                    } else {
                        strBgColor = "#DCE5F1";
                    }
                    %>

				<tr bgcolor='<%=strBgColor%>' class="rowLine"
					onmouseover="className='rowHover rowLine'"
					onmouseout="className='rowLine'">
					<td width="15%" class="copy"><bean:write name='disclosure'
							property='sponsorName' /></td>
					<td width="30%" class="copy"><bean:write name='disclosure'
							property='title' /></td>
					<td width="15%" class="copy">
						<%
                            Integer status = (Integer)disclosure.get("coiStatusCode");
                            if(status == 200){%> <bean:message
							bundle="coi" key="label.noConflict" /> <%}else if(status == 301 ){%>
						<bean:message bundle="coi" key="label.piPotentialConflict" /> <%}%>
					</td>
					<td width="25%" class="copy" align="left">
						<%Integer moduleCode = ((Integer)disclosure.get("moduleCode"));
                            String module = "";
                            if(moduleCode!=null && moduleCode == 1){%> <%module ="Award";%><bean:message
							bundle="coi" key="label.award" /> <%}else if(moduleCode!=null && moduleCode == 2){%>
						<%module ="Institute Proposal";%><bean:message bundle="coi"
							key="label.proposal" /> <%}else if(moduleCode!=null && moduleCode == 3){%>
						<%module ="Protocol";%><bean:message bundle="coi"
							key="label.protocol" /> <%}%>
					</td>
					<td widith="20%" class="copy"><bean:write name='disclosure'
							property='moduleItemKey' /></td>
					<%String relationShip = ((String)disclosure.get("description"));
                        relationShip = relationShip==null ? "" : relationShip;
                        %>
					<INPUT type='hidden' id="relComments" name="relComments<%=index%>"
						value="<%=relationShip%>">
					<%
                        String moduleItemKey = ((String)disclosure.get("moduleItemKey"));
                        String viewLink = "javascript:view_comments('"+index+"','"+relationShip+"','"+module+"','"+moduleItemKey+"')";
                        %>
					<td width="10%" class="copy"><html:link href="<%=viewLink%>">
							<bean:message bundle="coi" key="label.comments" />
						</html:link></td>
				</tr>
				<% index++;%>
			</logic:iterate>
		</logic:present>
		<%}%>
	</table>
</body>
</html>
