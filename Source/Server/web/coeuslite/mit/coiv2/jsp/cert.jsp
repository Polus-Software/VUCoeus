<%-- 
    Document   : cert
    Created on : Nov 16, 2010, 10:14:47 AM
    Author     : vineetha
--%>


<%@ page
	import="java.util.Vector,edu.mit.coeuslite.coiv2.utilities.CoiConstants,edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JSP Page</title>
<%
String path = request.getContextPath();%>
<script>
              function saveAndcontinue()
            {
                document.forms[0].action= '<%=path%>'+"/screeningquestions.do";
                document.forms[0].submit();
            }
        </script>
</head>
<body>
	<html:form action="/updateFinancialEntityByProjects.do">
		<table id="attBodyTable" class="table" border="0" width="100%">
			<tr style="background-color: #6E97CF; height: 22px;">
				<td
					style="color: #FFFFFF; font-size: 12px; font-weight: bold; padding: 2px 0 2px 0px;"
					colspan="4">
					<h1
						style="background-color: #6E97CF; color: #FFFFFF; float: left; font-size: 14px; font-weight: bold; margin: 0; padding: 2px 0 2px 1px; position: relative; text-align: left;">Certification
						Details</h1>
				</td>
			</tr>

			<logic:present name="ApprovedDisclDetView">
				<logic:iterate id="disclDetails" name="ApprovedDisclDetView">
					<tr>
						<td
							style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: none;"
							width="15%" align="right">Certified By :</td>
						<td
							style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px;; float: none;"
							width="35%" align="left"><bean:write name="person"
								property="fullName" /></td>

						<td
							style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: none"
							width="18%" align="right">Certified On :</td>
						<td
							style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; float: none"
							align="left"><bean:write name="disclDetails"
								property="certificationTimestampNew" /></td>
					</tr>
					<tr>
						<td colspan="4">&nbsp;</td>
					</tr>


					<tr>
						<td colspan="4">
							<table id="attBodyTable" class="table" width="100%" border="0">
								<tr>
									<td
										style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; float: left;"
										width="600px">Certification Text:</td>
								</tr>
								<tr>
									<td
										style="color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; float: left; padding-left: 10px;"
										width="780px"><bean:write name="disclDetails"
											property="certificationText" /></td>
								</tr>
							</table>
						</td>
					</tr>
				</logic:iterate>
			</logic:present>

		</table>
	</html:form>
</body>
</html>
