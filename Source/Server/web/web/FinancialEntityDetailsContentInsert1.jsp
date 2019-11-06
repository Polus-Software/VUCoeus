<%--
/*
 * @(#)FinancialEntityDetailsContent.jsp	1.0 2002/06/03 15:55:29
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author RaYaKu
 */
--%>
<%@page errorPage = "ErrorPage.jsp" %>
<%-- This JSP page is for Edit financial entity--%>
<%@ page import="java.util.Vector,
	edu.mit.coeus.coi.bean.ComboBoxBean,
	org.apache.struts.action.Action" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" 	prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" 	prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" 	prefix="logic" %>
<%@ include file="CoeusContextPath.jsp"  %>
<%-- The below beans are available in session scope because when this form is
validated at server side and any errors encounter then the show the same page
back to user with previous data but with validation errors too.
  --%>

<%	System.out.println("begin FinancialEntityDetailsContent.jsp");	%>
<jsp:useBean id="collEntityCertDetails" scope="session" class="java.util.Vector" />
<jsp:useBean id="collOrgTypes" scope="session" class="java.util.LinkedList" />
<jsp:useBean id="collEntityStatus" scope="session" class="java.util.LinkedList" />
<jsp:useBean id="collRelations" scope="session" class="java.util.LinkedList" />
<%-- Find the size of collEntityCertDetails --%>
<bean:size id="certificatesSize" name="collEntityCertDetails" />

    <table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
		 <td height="30">
		 <div align="left">
			<!-- CASE #864 Add 5px cellpadding -->
			<table width="100%" border="0" cellspacing="0" cellpadding="2">
			<!-- CASE #864 Begin -->
			<!-- Move validation error msg to inside table with cellpadding -->
<!-- CASE #1393: Add font tags around html:errors tag and display header message if errors exist-->				
				<!-- Display errors -->
				<logic:present name="<%=Action.ERROR_KEY%>">
								<tr>
				<td colspan="2">
					<b><bean:message key="validationErrorHeader"/></b>
					<bean:message key="validationErrorSubHeader1"/>
					<html:errors/>
					</font>
				</td>
				</tr>					
				</logic:present>
			<!-- CASE #864 End -->		
				<tr>
				 <td>
					<!-- CASE #1374 Comment Begin -->
					<%--
					<div align="left">&nbsp;<font color="#663300"><b>
						<font color="#7F1B00">
						<bean:message key="financialEntityDetails.personName" />:
						<bean:write  name="frmFinancialEntityDetailsForm" property="userFullName" scope="request" />
						<html:hidden property="userFullName" />
						</font></b></font>
					</div>
					--%>
					<!-- CASE #1374 Comment End -->
					<!-- CASE #1374 Begin -->
					&nbsp;&nbsp;<font color="red"><b>*</b></font>
					<font class="fontBrown"> indicates required field</font>
					<!-- CASE #1374 End -->
				 </td>
				 <td height="40">
				 <div align="right">
				 <!-- CASE #665 Comment Begin -->
				 <!-- Remove Back Button -->
				 <%--
						<a href="JavaScript:history.back();">
							<img src="<bean:write name="ctxtPath"/>/images/goback.gif" width="55" height="22" border="0">
						</a>&nbsp;
				--%>
				<!-- CASE #665 Comment End -->
					<html:image page="/images/submit.gif"  border="0" />
					</a>
					&nbsp; &nbsp; &nbsp;
				</div>
				 </td>
				</tr>					
			</table>
		 </div>
		 </td>
				<tr>
				<td height="5">
					&nbsp;
				</td>
				</tr>				
	 </table>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	 <tr>
		<td background="<bean:write name="ctxtPath"/>/images/tabback.gif">
		 <table border=0 cellpadding=0 cellspacing=0>
			<tr>
			<td> <img src="<bean:write name="ctxtPath"/>/images/entitydetails.gif" width="120" height="24"></td>
			</tr>
		 </table>
		</td>
	 </tr>
	</table>
