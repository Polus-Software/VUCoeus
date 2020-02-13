<%--
/*
 * @(#)DiscSubmitSuccessContent.jsp	1.0 02/20/2004
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  coeus-dev-team
 */
--%>

<%@page errorPage = "ErrorPage.jsp" %>

<%@ taglib uri="/WEB-INF/struts-html.tld"     prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld"     prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld"     prefix="logic" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"     prefix="coeusUtils" %>

<%@ include file="CoeusContextPath.jsp"  %>

<jsp:useBean id="disclosureHeader" scope="request" class="edu.mit.coeus.coi.bean.DisclosureHeaderBean"/>
<jsp:useBean id="frmAddCOIDisclosure" scope="session" class="edu.mit.coeus.action.coi.AddCOIDisclosureActionForm"/>

 <table width="100%" border="0" cellpadding="0" cellspacing="0">
 <tr>
	<td width="657" valign="top">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">

			  <tr>
				<td height="23" bgcolor="#cccccc"> &nbsp;<b><font face="Verdana, Arial, Helvetica, sans-serif" size="2">
				<bean:message key="discSubmitSuccess.header" />
				<logic:equal name="frmAddCOIDisclosure" scope="session" property="accountType" value="I" >
					<bean:message key="discSubmitSuccess.submission" />
				</logic:equal>
				
				<logic:equal name="frmAddCOIDisclosure" scope="session" property="accountType" value="U" >
					<bean:message key="discSubmitSuccess.modification" />
				</logic:equal>				
				<bean:write name="frmAddCOIDisclosure" scope="session" property="personFullName" /> 
				</font></b> </td>
			  </tr>
		</table>

		<p />		
		<p />
		
		<table width="100%" border="0" cellspacing="0" cellpadding="5">

			<tr>
				<td align = "center" colspan="3">
				<font face="Verdana, Arial, Helvetica, sans-serif" size="2"><b>
				<bean:message key="discSubmitSuccess.message2" />
				<bean:write name="frmAddCOIDisclosure" scope="session" property="sponsorName" /> 

				
				<logic:equal name="frmAddCOIDisclosure" scope="session" 
				property="disclosureTypeCode" value="1">
					<bean:message key="discSubmitSuccess.award" />
				</logic:equal>
				<logic:notEqual name="frmAddCOIDisclosure" scope="session" 
				property="disclosureTypeCode" value="1">
					<bean:message key="discSubmitSuccess.proposal" />
				</logic:notEqual>
				
				<bean:message key="discSubmitSuccess.message3" />
				<i><bean:write name="frmAddCOIDisclosure" scope="session" property="title" /></i>
				
				<logic:equal name="frmAddCOIDisclosure" scope="session" property="accountType" value="I" >
					<bean:message key="discSubmitSuccess.message4Insert" />
				</logic:equal>
				
				<logic:equal name="frmAddCOIDisclosure" scope="session" property="accountType" value="U" >
					<bean:message key="discSubmitSuccess.message4Update" />
				</logic:equal>				
				 </b></font>
				</td>
			  </tr>
			  <tr>
			  	<td colspan="3" height="38">
			  		&nbsp;
			  	</td>
			  </tr>			  

			<!-- CASE #1374 Begin -->
			<tr>
				<td width="10%">&nbsp;</td>
				<td width="50%">
					<logic:equal name="frmAddCOIDisclosure" scope="session" property="accountType" value="I" >
						<a href="<bean:write name='ctxtPath'/>/viewCOIDisclosureDetails.do?action=edit&disclNo=<bean:write name='frmAddCOIDisclosure' property='disclosureNo'/>"><img src="<bean:write name='ctxtPath'/>/images/view_submitted_disclosure.gif" border="0"/></a>
					</logic:equal>

					<logic:equal name="frmAddCOIDisclosure" scope="session" property="accountType" value="U" >
						<a href="<bean:write name='ctxtPath'/>/viewCOIDisclosureDetails.do?action=edit&disclNo=<bean:write name='frmAddCOIDisclosure' property='disclosureNo'/>"><img src="<bean:write name='ctxtPath'/>/images/view_modified_disclosure.gif" 						border="0" /></a>
					</logic:equal>	
				</td>
				<td width="40%">
					<a href='<bean:write name='ctxtPath'/>/loginCOI.do'>
					<img src="<bean:write name='ctxtPath'/>/images/finished_disclosure.gif"
					border="0" />
					</a>				
				</td>
			</tr>
			
			<!-- CASE #1374 End -->
			
		

		</table>
		
	</td>
	</tr>
</table>

              