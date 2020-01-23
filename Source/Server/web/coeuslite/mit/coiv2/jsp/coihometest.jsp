<%-- 
    Document   : coihome
    Created on : Mar 11, 2010, 12:12:49 PM
    Author     : Mr
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>COI Home</title>
<style type="text/css">
<!--
.style1 {
	font-size: 18px;
	font-weight: bold;
}
-->
</style>
</head>

<body>
	<h2>Some changes</h2>
	<div align="center">
		<table width="985" border="1">
			<tr>
				<td width="979" bordercolor="#000000"><div align="left">
						Coeus Home<strong>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						</strong>My IRB Protocol<strong>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong>MyProposals<strong>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong>My
						COI<strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						</strong>Inbox<strong>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong>My
						ARRA<strong>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong>Logout
					</div></td>
			</tr>
		</table>

		<div align="center" class="style1">Financial Internal Disclosure</div>
		<br /> Financial Disclosure By,
		<bean:write name="loggedinperson" property="userId" />
		<table width="985" height="61" border="1">
			<tr>
				<td width="979"><strong>&nbsp;</strong>&nbsp;Report Name: <bean:write
						name="loggedinperson" property="userId" /> &nbsp; &nbsp; &nbsp;
					&nbsp; &nbsp; Report Address: <br /> <br /> <strong>&nbsp;&nbsp;</strong>Report
					Department:</td>
			</tr>
		</table>
		<br /> Current Financial Disclosure
		<table width="985" height="61" border="1">
			<tr>
				<td width="979"><strong>&nbsp;</strong>&nbsp;Discl No: &nbsp;
					&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; Discl
					Title: <br /> <br /> <strong>&nbsp;&nbsp;</strong>Discl Date:
					&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp;
					&nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp;
					&nbsp;&nbsp;<a href="disclosure_view.html">Show Disclosure</a>
					&nbsp; &nbsp; &nbsp; &nbsp;Show History</td>
			</tr>
		</table>
		<br />
	</div>
	<div style="padding-left: 100px">

		<html:link page="/getannualdisclosure.do">show my annual disclosure</html:link>
		<div
			style="border: 1px; border-color: #666; border-style: solid; padding: 5px; width: 200px;">
			Disclosures
			<ul>
				<li><a href="all_projects.html">Create Proposal Based</a></li>
				<li><a href="all_projects.html">Create Protocol Based</a></li>
				<li><a href="all_projects.html">Create Award Based</a></li>
				<li><a href="project_details_entry.html">Create Other</a></li>
			</ul>
		</div>
	</div>
	<p>&nbsp;</p>
	<p>&nbsp;</p>
	<p>&nbsp;</p>

</body>
</html>
