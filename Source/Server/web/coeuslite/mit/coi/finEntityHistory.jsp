<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%String path = request.getContextPath();%>
<link rel=stylesheet
	href="<%=path%>/coeuslite/mit/utils/css/coeus_styles.css"
	type="text/css">
<title>Financial Entity History</title>
<script>
            function rowHover(rowId, styleName) {
                elemId = document.getElementById(rowId);
                elemId.style.cursor = "hand";
                //If row is selected retain selection style
                //Apply hover Style only if row is not selected
                
                    elemId.className = styleName;
                
            }
            
            function openFinEntity(entityNumber, sequenceNum) {
                var url = "<%=path%>/viewFinEntityCoiV2.do?entityNumber="+entityNumber+"&seqNum="+sequenceNum+"&header=no";
                window.open(url, "History", "resizable=yes, scrollbars=yes, width=1000, height=500, left=100, top = 100");
            }
        </script>
</head>
<body class="table">
	<b>History of changes:</b>
	<logic:equal name="certificatesSize" value="0">
		<table width="100%" align="right" border="0">
			<tr>
				<td align='center'><bean:message bundle="coi"
						key="financialEntity.noFinancialEnt" /></td>
			</tr>
		</table>
	</logic:equal>
	<logic:present name="FinEntityHistory" scope="request">
		<table width="100%" align="center" border="0" cellspacing="0"
			cellpadding="3" class="tabtable">
			<tr>
				<td width="10%" align="left" class="theader" nowrap><bean:message
						bundle="coi" key="label.status" /></td>
				<td width="10%" align="left" class="theader" nowrap><bean:message
						bundle="coi" key="label.entityRelType" /></td>
				<td width="10%" align="left" class="theader" nowrap><bean:message
						bundle="coi" key="label.relationshipDesc" /></td>
				<td width="10%" align="left" class="theader" nowrap><bean:message
						bundle="coi" key="label.organizationDesc" /></td>
				<td width="20%" align="left" class="theader" nowrap><bean:message
						bundle="coi" key="label.lastUpdated" /></td>
				<noscript>
					<td width="5%" align="left" class="theader" nowrap></td>
				</noscript>
			</tr>
			<logic:iterate id="data" name="FinEntityHistory"
				type="org.apache.commons.beanutils.DynaBean" indexId="ctr">

				<tr class="rowLine" id="row<%=ctr%>"
					onclick="openFinEntity('<bean:write name="data" property="entityNumber"/>','<bean:write name="data" property="sequenceNum"/>')"
					onmouseover="rowHover('row<%=ctr%>','rowHover rowLine')"
					onmouseout="rowHover('row<%=ctr%>','rowLine')">
					<td width="10%" align="left" class="copy"><logic:present
							name="data" property="statusCode">
							<logic:equal name="data" property="statusCode" value='1'>
								<bean:message bundle="coi" key="financialEntity.active" />
							</logic:equal>

							<logic:notEqual name="data" property="statusCode" value='1'>
								<bean:message bundle="coi" key="financialEntity.inactive" />
							</logic:notEqual>
						</logic:present></td>

					<td width="10%"><coeusUtils:formatOutput name="data"
							property="relationShipTypeDesc" /></td>

					<%Object commentObj=data.get("relationShipDesc");
        String comment="";
        if(commentObj==null){comment="";}
        else{comment=(String)commentObj;
            if(comment.trim().length()>25)
                {comment=(String)comment.substring(0,25)+"...";}}%>

					<td><%=comment%></td>

					<%commentObj=data.get("orgRelnDesc");
        if(commentObj==null){comment="";}
        else{comment=(String)commentObj;
            if(comment.trim().length()>25)
                {comment=(String)comment.substring(0,25)+"...";}}%>
					<td><%=comment%></td>

					<td width="20%" nowrap><coeusUtils:formatDate name="data"
							formatString="MM-dd-yyyy  hh:mm a" property="updtimestamp" />
						&nbsp; <bean:write name="data" property="upduser" /></td>

					<noscript>
						<td width="5%" class='copy'><a target="_blank"
							href='<%=path%>/viewFinEntity.do?entityNumber=<bean:write name="data" property="entityNumber"/>&seqNum=<bean:write name="data" property="sequenceNum"/>&header=no'>view</a>
						</td>
					</noscript>
				</tr>

			</logic:iterate>
			</logic:present>
		</table>
</body>

</html>
