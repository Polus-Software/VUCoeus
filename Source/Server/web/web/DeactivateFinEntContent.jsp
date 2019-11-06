<!--
/*
 * @(#)DeactivateFinEntContent.jsp	1.0 02/20/2004
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  coeus-dev-team
 */
-->

<%@page errorPage = "ErrorPage.jsp" 
	import ="org.apache.struts.action.Action" %>

<%@ taglib uri="/WEB-INF/struts-html.tld"     prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld"     prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld"     prefix="logic" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"     prefix="coeusUtils" %>

<%@ include file="CoeusContextPath.jsp"  %>

<jsp:useBean id="frmFinancialEntityDetailsForm" scope="request" 
	class="edu.mit.coeus.action.coi.FinancialEntityDetailsActionForm"/>

 <table width="100%" border="0" cellpadding="0" cellspacing="0">
 <tr>
	<td width="657" valign="top">
	<html:form action="/deactivateFinEnt2">
		<!-- actionform stored in request object.  pass needed fields again -->
		<html:hidden property="actionFrom"/>
		<html:hidden property="number"/>
		<html:hidden property="name"/>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">

			  <tr>
				<td height="23" bgcolor="#cccccc"> &nbsp;<b><font face="Verdana, Arial, Helvetica, sans-serif" size="2">
				<bean:message key="deactivateFinEnt.header" />: 
				<bean:write name='frmFinancialEntityDetailsForm' property='name'/>							
				</font></b> </td>
			  </tr>
		</table>
		<table>
		<tr>
			<td >
			<font class="fontBrown">
			<b><bean:message key="deactivateFinEnt.intro"/></b>
			</font>
			</td>		
		</tr>
		<tr>
			<td height="5">&nbsp;</td>
		</tr>
		<tr>
			<td>
				<logic:present name="<%=Action.ERROR_KEY%>">
					<font color="red">
				</logic:present>
				<bean:message key="deactivateFinEnt.enterExplanation"/>
				<logic:present name="<%=Action.ERROR_KEY%>">
					</font>
				</logic:present>
			<td>
		</tr>
		<tr>
			<td>
			<textarea name="description" wrap='virtual' cols="40" rows="2" ><bean:write name='frmFinancialEntityDetailsForm' property='description'/></textarea>
			</td>
		</tr>
		<tr>
			<td align="right">
			<html:image page="/images/submit.gif" border="0"/>
			</td>
		
		</tr>
		</table>

		<p />		
		<p />
		
		
	</html:form>	
	</td>
	</tr>
</table>

              