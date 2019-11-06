<%-- 
    Document   : showProjectDetails
    Created on : Oct 18, 2011, 10:32:07 AM
    Author     : midhunmk
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html;charset=UTF-8" language="java"
	import="edu.mit.coeuslite.coiv2.beans.CoiAnnualProjectEntityDetailsBean,
                edu.mit.coeuslite.coiv2.beans.CoiProjectDiscListBean"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="COIProjectDetailList" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="COIProjectDetail" scope="session"
	class="edu.mit.coeuslite.coiv2.beans.CoiAnnualProjectEntityDetailsBean" />

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Project details</title>
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
</head>
<body style="background-color: #D1E5FF;">

	<logic:present name="COIProjectDetail">
		<table class="table"
			style="width: 98%; margin-left: 1%; margin-right: 1%;">
			<tr>
				<td>
					<table class="tabtable" style="width: 100%; padding: 10px;">
						<tr style="background-color: #6E97CF;">
							<td colspan="2"><font
								style="color: #FFFFFF; float: left; font-size: 14px; font-weight: bold;">
									&nbsp;&nbsp;&nbsp;Project History </font></td>

						</tr>
						<tr style="background-color: #9DBFE9;">
							<td
								style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: none;">Project
								ID :</td>
							<td
								style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-weight: normal; font-size: 12px; align: left;"><bean:write
									name="COIProjectDetail" property="moduleItemKey" /></td>
						</tr>
						<tr style="background-color: #9DBFE9;">
							<td
								style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: none;">Project
								Title :</td>
							<td
								style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-weight: normal; font-size: 12px; align: left;"><bean:write
									name="COIProjectDetail" property="coiProjectTitle" /></td>
						</tr>

						<tr style="background-color: #9DBFE9;">
							<td
								style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: none;">Start
								Date :</td>
							<td
								style="width: 50%; color: #333333; font-family: Arial, Helvetica, sans-serif; font-weight: normal; font-size: 12px; align: left;"><coeusUtils:formatDate
									name="COIProjectDetail" property="coiProjectStartDate" /></td>
						</tr>

						<tr style="background-color: #9DBFE9;">
							<td
								style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: none;">End
								Date :</td>
							<td
								style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-weight: normal; font-size: 12px; align: left;"><coeusUtils:formatDate
									name="COIProjectDetail" property="coiProjectEndDate" /></td>
						</tr>

					</table>
				</td>
			</tr>
			<tr>
				<td>

					<table class="tabtable" border="1" cellspacing="1" cellpadding="1"
						style="width: 100%;">

						<tbody>
							<tr style="background-color: #6E97CF;">
								<td colspan="4"><font
									style="color: #FFFFFF; float: left; font-size: 14px; font-weight: bold;">
										&nbsp;&nbsp;&nbsp;Disclosure List </font></td>
							</tr>
							<tr>
								<th class="theader" style="width: 25%" align="left">Disclosure
									Number</th>
								<th class="theader" style="width: 25%" align="left">Sequence</th>
								<th class="theader" style="width: 25%" align="left">Status
								</th>
								<th class="theader" style="width: 25%" align="left">Approved
									Date</th>

							</tr>
							<logic:notEmpty name="COIProjectDetailList">
								<%int rowCount = 0;
        String strBgColor= "#DCE5F1";%>

								<logic:iterate id="COIDetailitem" name="COIProjectDetailList"
									type="edu.mit.coeuslite.coiv2.beans.CoiProjectDiscListBean">
									<% if (rowCount%2 == 0){strBgColor = "#D6DCE5";}
                          else{strBgColor="#DCE5F1";}
                          rowCount++;
                        %>
									<tr bgcolor="<%=strBgColor%>" valign="top"
										onmouseover="className='TableItemOn'"
										onmouseout="className='TableItemOff'" class="TableItemOff">
										<td style="width: 25%" align="left"><bean:write
												name="COIDetailitem" property="coiDisclosureNumber" /></td>
										<td style="width: 25%" align="left"><bean:write
												name="COIDetailitem" property="coiSequenceNumber" /></td>
										<td style="width: 25%" align="left"><bean:write
												name="COIDetailitem" property="coiProjectStatusDesc" /></td>
										<td style="width: 25%" align="left"><bean:write
												name="COIDetailitem" property="coiUpdateTimeStamp" /></td>

									</tr>
								</logic:iterate>
							</logic:notEmpty>
							<logic:empty name="COIProjectDetailList">
								<tr>
									<td colspan="4">No details found</td>
								</tr>
							</logic:empty>
						</tbody>
					</table>
				</td>
			</tr>
		</table>
	</logic:present>




	<logic:notPresent name="COIProjectDetail">
		<div>No details found</div>
	</logic:notPresent>


</body>
</html>
