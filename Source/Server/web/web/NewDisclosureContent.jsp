<%--
/*
 * @(#)NewDisclosureContent.jsp	1.0 2002/06/10
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  RaYaKu
 */
--%>
<%@page errorPage = "ErrorPage.jsp" %>
<%-- This JSP file is for allowing the user to select options for creating a new disclosure--%>
<%@ taglib uri="/WEB-INF/struts-html.tld"     prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld"     prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld"     prefix="logic" %>
<%@ taglib uri="/WEB-INF/privilege.tld" prefix="priv" %>
<jsp:useBean id = "userprivilege" class = "java.lang.String" scope="session"/>
<jsp:useBean id = "personInfo" class = "edu.mit.coeus.bean.PersonInfoBean" scope="session"/>

<%@ include file="CoeusContextPath.jsp"  %>
  <table width="100%"  cellpadding="0" cellspacing="0" border="0">
      <tr>
      <!--
        <td background="<bean:write name="ctxtPath"/>/images/separator.gif" width="1" >
            <image src="<bean:write name="ctxtPath"/>/images/separator.gif" width="1">
        </td>
        -->
    	<td width="657" valign="top">
      		<table width="100%" border="0" cellspacing="0" cellpadding="0">
      		<html:form action="/createnewdisclosure.do">
          		<tr>
            			<td height="5"></td>
			</tr>
			<tr bgcolor="#cccccc">
				<td height="23" class="header"> 
				&nbsp;&nbsp;
				<bean:message key="newCOIDisclosure.header"/>  
				<bean:write  name="frmNewDisclosure" property="userName" /> 
				<html:hidden property="userName" />
				</td>
			</tr>
			<tr>
				<td>
				<%--
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
						  <td background="<bean:write name='ctxtPath'/>/images/tabback.gif">
						    <table border=0 cellpadding=0 cellspacing=0>
						      <tr>
							<td> <img src="<bean:write name='ctxtPath'/>/images/selectPropAward.gif" width="156" height="24"></td>
						      </tr>
						    </table>
						  </td>
						</tr>
					</table>
				--%>
					<table width="90%" border="0" cellspacing="5" cellpadding="0" align="center">
						<tr>
							<td height="5">
							&nbsp;
							</td>
						</tr>
						<tr>
						  <td colspan="4" height="30" class="mediumsizebrown">
						  <!-- CASE #1393 Comment Begin -->
						  <%--<html:errors/>--%>
						  <!-- CASE #1393 Comment End -->
						<html:radio property="disclType" value="new" />
						 <b><bean:message key="newCOIDisclosure.newProposalSelect"/></b>
						  </td>
						</tr>
						<tr>
						<td colspan="4" height="5">&nbsp;</td>
						</tr>
					<!-- CASE #957 Begin -->
					</table>
					<table width="90%" border="0" cellspacing="5" cellpadding="0" align="center">
						<tr>
						  <td colspan="4" height="30" class="mediumsizebrown">
						  <html:radio property="disclType" value="midyear" />
						    <b>
							 <bean:message key="newCOIDisclosure.proposalAwardSelect"/>							</b> 
						  </td>
						</tr>
					<!--CASE #957 End -->
						<tr>
						<td colspan="4" height="5">&nbsp;</td>
						</tr>					
						<tr>
						  <td width="4%">&nbsp;</td>
						  <td width="33%">&nbsp;</td>
						  <td colspan="3" height="40">
						    <div align="left">
							<html:image page="/images/ok.gif" border="0"  />
						    </div>
						  </td>
						</tr>
					</table>
			
				</td>
			</tr>
			<tr>
				<td>


				</td>
			</tr>
          	</html:form>
      		</table>
	  </td>
	</tr>
</table>

