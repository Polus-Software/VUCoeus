<%@ page
	import="org.apache.struts.validator.DynaValidatorForm,java.sql.*,edu.dartmouth.coeuslite.coi.beans.DisclosureBean"%>

<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@page errorPage="ErrorPage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="disclRpts" scope="session" class="java.util.Vector" />
<bean:size id="vectSize" name="disclRpts" />
<jsp:useBean id="person" scope="session"
	class="edu.mit.coeus.bean.PersonInfoBean" />

<html:html>
<head>
<title>Coeus Web</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css" />
<script language="javascript" type="text/JavaScript"
	src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/sorttable.js"></script>
</head>
<body>
	<table width="980" border="0" align="left" height="100%" border="0"
		cellpadding="0" cellspacing="0" class="table">
		<tr height="20px">
			<td align="left" colspan="7" valign="top" class="theader"><bean:message
					bundle="coi" key="coiAnnualDisclosure.header" />:</td>
		</tr>
		<tr class="theaderblue" height="20px">
			<td width="19%" align="left" class="theader"><bean:message
					bundle="coi" key="label.personName" /></td>
			<td width="19%" align="left" class="theader"><bean:message
					bundle="coi" key="label.disclosureStatus" /></td>
			<td width="15%" align="left" class="theader"><bean:message
					bundle="coi" key="label.department" /></td>
			<td width="10%" align="left" class="theader"><bean:message
					bundle="coi" key="label.DiscDate" /></td>
			<td width="18%" align="left" class="theader"></td>

		</tr>
		<logic:present name="disclRpts" scope="session">
			<%  
                String strBgColor = "#DCE5F1";
                int disclIndex=0;
                //  int size=reviewAnnualDisc.size();
                //   System.out.println("VECTOR SIZE="+size);
                
                %>
			<logic:notEqual name='vectSize' value='0'>
				<logic:iterate id="discInfo" name="disclRpts"
					type="edu.dartmouth.coeuslite.coi.beans.DisclosureBean">
					<bean:define id="personID" name="discInfo" property="personId" />
					<bean:define id="fullName" name="discInfo" property="fullName" />
					<tr height="20px" bgcolor='<%=strBgColor%>' id="row<%=disclIndex%>"
						class="rowLine" onmouseover="className='rowHover rowLine'"
						onmouseout="className='rowLine'">
						<td width="19%" class="copy" valign=top wrap colspan="0"><coeusUtils:formatOutput
								name="discInfo" property="fullName" /></td>
						<%String statusStr="";%>
						<logic:present name="DisclStatus" scope="session">
							<logic:iterate id="status" name="DisclStatus"
								type="edu.mit.coeuslite.utils.ComboBoxBean">
								<bean:define id="statuscode" name="status" property="code" />
								<logic:equal name='statuscode'
									value='<%=discInfo.getDisclosureStatusCode().toString()%>'>
									<bean:define id="statusDesc" name="status"
										property="description" />
									<%statusStr=(String)statusDesc;%>
								</logic:equal>
							</logic:iterate>
						</logic:present>
						<td width="19%" class="copy" valign=top wrap colspan="0"><%=statusStr%>
						</td>
						<td width="15%" class="copy" valign=top wrap colspan="0"><coeusUtils:formatOutput
								name="discInfo" property="unitName" /></td>
						<td width="10%" class="copy" valign=top wrap colspan="0"><bean:define
								id="updTS" name="discInfo" property="updateTimestamp" /> <coeusUtils:formatOutput
								name="discInfo" property="updateTimestamp" /></td>
						<td width="18%" align="left" class="copy"></td>
					</tr>
					<%disclIndex++;%>
				</logic:iterate>
			</logic:notEqual>
			<logic:equal name='vectSize' value='0'>
				<tr height="20px">
					<td colspan="6" class="rowLine"
						onmouseover="className='rowHover rowLine'"
						onmouseout="className='rowLine'">&nbsp; <bean:message
							bundle="coi" key="AnnualDisclosure.NoDisclosure" />

					</td>
				</tr>
			</logic:equal>
		</logic:present>
		<tr>
			<td colspan="6"></td>
		</tr>
	</table>
</body>
</html:html>
