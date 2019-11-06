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
<%@page errorPage = "ErrorPage.jsp" 
	import = "org.apache.struts.action.Action" %>
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
        <td background="<bean:write name="ctxtPath"/>/images/separator.gif" width="1">
            <image src="<bean:write name="ctxtPath"/>/images/separator.gif" width="1">
        </td>
    	<td width="657" valign="top">
      		<table width="100%" border="0" cellspacing="0" cellpadding="0">
      		<html:form action="/createmidyeardisclosure.do" >
          		<tr>
            			<td height="5"></td>
			</tr>
			<tr bgcolor="#cccccc">
				<td height="23"> <font size="2"><b>
				<font face="Verdana, Arial, Helvetica, sans-serif">
				&nbsp;&nbsp;<bean:message key="newCOIDisclosure.header"/>  
				<bean:write  name="personInfo" property="fullName" />
				</font></b></font>
				<html:hidden property="userName" />
				</td>
			</tr>
			<tr>
				<td>
				<%-- CASE #1393 Comment <html:errors/>  --%>&nbsp;
				</td>
			</tr>
			<tr>
				<td >
				
						
					<!-- CASE #957 Begin -->
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
				<logic:present name="<%=Action.ERROR_KEY%>">
				<table width="100%" border="0" cellspacing="0" cellpadding="7">
				<tr>
				<td colspan="4">
					<b><bean:message key="validationErrorHeader"/></b>
					<bean:message key="validationErrorSubHeader1"/>
					<html:errors/>					
				</td>
				</tr>
				</table>
				</logic:present>					
					<table width="100%" border="0" cellspacing="5" cellpadding="0">
						<tr>
						  <td colspan="4" height="30">
						  <%-- CASE #1374 Comment 
						    <div align="left"><font color="#7F1B00"><b> 
							 <bean:message key="newCOIDisclosure.proposalAwardSelect"/></b>
						</div>
							 --%>
							 <!-- CASE #1374 Begin -->
						    <font class="fontBrown">
						    <b>
							 <bean:message key="midyeardisclosure.proposalAwardSelect"/>
							</b> 
							</font>
							<!-- CASE #1374 End -->
						  </td>
						</tr>
					<!--CASE #957 End -->
						<tr>
						<td colspan="4" height="10"> &nbsp;</td>
						</tr>
						<tr>
						  <td width="4%" valign="top">
							<html:radio property="disclosureType" value="2" />
						  </td>
						  <td width="33%" height="55" valign="top">
						    <div align="left">
						    <b><bean:message key="newCOIDDisclosure.label.proposalNumber"/></b>
						    </div>
						      <!-- CASE #957 Comment Begin -->
						      <%-- 
						    <br>
						      <font size="1" color="#FF0000">
						      <bean:message key="newCOIDDisclosure.label.proposalSelect"/>
						      </font></font>
						      --%>
						      <!-- CASE #957 Comment End -->
						      </div>
						  </td>
						  <td width="20%" valign="top">
							  <html:text property="proposalNum" />
						  </td>
						  <td valign="top" width="43%"> <a href="javascript:openSearchWin('<bean:write name='ctxtPath'/>/coeusSearch.do?searchname=proposalSearch&fieldName=proposalNum','proposal');"><img src="<bean:write name='ctxtPath'/>/images/searchpage.gif" width="22" height="20" border="0"></a>
						  <div><b><bean:message key="newCOIDisclosure.label.proposalSearch" /></b></div>
						  </td>
						</tr>
                                                <tr>
						  <td width="4%" valign="top">
							<html:radio property="disclosureType" value="1" />
						  </td>
						  <td width="33%" height="50" valign="top">
						    <div align="left"><font face="Verdana, Arial, Helvetica, sans-serif"><b>
							<bean:message key="newCOIDDisclosure.label.awardNumber"/>
						    </b>
						      </div>
						  </td>
						  <td width="20%" valign="top">
								<html:text property="awardNum" />
						  </td>
						  <td width="43%" valign="top"> 
						  <a href="JavaScript:openwintext('<bean:write name='ctxtPath'/>/coeusSearch.do?searchname=awardSearch&fieldName=awardNum','award');"><img src="<bean:write name="ctxtPath"/>/images/searchpage.gif" width="22" height="20" border="0">
						  </a>
						  <div><b><bean:message key="newCOIDisclosure.label.awardSearch" /></b></div></td>
						</tr>
						<tr>
						  <td width="4%">&nbsp;</td>
						  <td width="33%">&nbsp;</td>
						  <td colspan="3" height="40">
						    <div align="left">
						    <!-- CASE #957 Comment Begin -->
						    <!-- Remove back button -->
						    <%--
							<a href="JavaScript:history.back();"><img src="<bean:write name='ctxtPath'/>/images/goback.gif" width="55" height="22" border="0"></a>&nbsp;
							--%>
							<!-- CASE #957 Comment End -->
							<html:image page="/images/ok.gif" border="0"  />
						    </div>
						  </td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
<%--
<!-- Check whether user has right to edit disclosures for other users.  If yes, display Select Person section of page.  Else, don't display. -->
					<priv:hasOSPRight name="hasOspRightToEdit" value="<%=Integer.parseInt(userprivilege)%>">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
					  <td background="<bean:write name='ctxtPath'/>/images/tabback.gif">
					    <table border=0 cellpadding=0 cellspacing=0>
					      <tr>
						<td> <img src="<bean:write name='ctxtPath'/>/images/selectPerson.gif" width="97" height="24"></td>
					      </tr>
					    </table>
					  </td>
					</tr>
					</table>
					<table width="100%" border="0" cellspacing="5" cellpadding="0">

						<tr>
						  <td colspan="5" height="30">
						    <div align="left"><font color="#7F1B00"><b>
							 <bean:message key="newCOIDisclosure.label.personSearch"/></b> </font>
						    </div>
						  </td>
						</tr>

						<tr bgcolor="#FBF7F7">
						  <td width="33%">
						    <div align="center"><font face="Verdana, Arial, Helvetica, sans-serif"><b>
							<bean:message key="newCOIDisclosure.label.personName"/></b></font>
						   </div>
						  </td>
						  <td width="20%">
						    <logic:equal name="personInfo" property="ownInfo" value="true">
							<html:text property="personName" readonly="true"/>
							<html:hidden property="personId"/>
						    </logic:equal>
						    <logic:notEqual name="personInfo" property="ownInfo" value="true">
							<html:text name="personInfo" property="fullName" readonly="true"/>
							<html:hidden name="personInfo" property="personID"/>
						    </logic:notEqual>
						  </td>
						  <td><font face="Verdana, Arial, Helvetica, sans-serif" size="2">
						    <a href="JavaScript:openwintext('coeusSearch.do?searchname=personSearch&fieldName=personId&reqPage=newdisc','person');">
						    <img src="<bean:write name='ctxtPath'/>/images/searchpage.gif" width="22" height="20" border="0">
						    </a></font>
						  </td>
						  <td>&nbsp;
						  </td>
						</tr>
					</table>
					</priv:hasOSPRight>
--%>

				</td>
			</tr>
          	</html:form>
      		</table>
	  </td>
	</tr>
</table>

