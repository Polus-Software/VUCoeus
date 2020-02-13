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
<%@ page import="java.util.Vector,edu.mit.coeus.coi.bean.ComboBoxBean" %>
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


			<tr bgcolor="#FBF7F7">
				<td width="109" height="70" >
					<div align="left"><bean:message key="financialEntityDetails.personRelation.explanation" /></div>
				</td>
				<td width="7" height="70" >: </td>
				<td>&nbsp;</td>
				<td colspan="4" height="70" >
					<%--<html:textarea property="personRelationDesc" cols="40" rows="2"  />--%>
					<div>
                                        <textarea name="personRelationDesc"
                                            wrap='virtual' cols="40" rows="2" ><bean:write
                                                name='frmFinancialEntityDetailsForm' property='personRelationDesc'/></textarea>
                                        </div>
				</td>
			</tr>
		<%--	<tr>
				<td colspan="6" height="25" bgcolor="#CC9999">
				<div align="left"><font color="#FFFFFF"><b>&nbsp;
				<bean:message key="financialEntityDetails.orgRelation.header" />
				</b></font></div>
				</td>
			</tr>

			<tr>
				<td width="109" height="30" bgcolor="#FBF7F7">&nbsp;</td>
				<td colspan="5" height="30" bgcolor="#FBF7F7">
				<div align="left">
					<html:radio property="orgRelationType" value="Y" />
					<bean:message key="financialEntityDetails.orgRelation.related" />

					<html:radio property="orgRelationType" value="N" />
					<bean:message key="financialEntityDetails.orgRelation.notRelated" />

					<html:radio property="orgRelationType" value="X" />
					<bean:message key="financialEntityDetails.orgRelation.dontKnow" />
				</div>
				</td>
				</tr>
				<tr bgcolor="#F7EEEE">
					<td width="109" height="70">
					<div align="left">&nbsp;<bean:message key="financialEntityDetails.orgRelation.explanation" /></div>
					</td>
					<td width="7" height="70">: </td>
					<td colspan="4" height="70">
					<!--<html:textarea property="orgRelationDesc" cols="40" rows="2" />-->
                                        <textarea name="orgRelationDesc"
                                            wrap='virtual' cols="40" rows="2" ><bean:write
                                                name='frmFinancialEntityDetailsForm'
                                                    property='orgRelationDesc'/></textarea>
					</td>
				</tr>
		--%>
		<!-- CASE #1374 Comment Begin -->
		<%--
		<tr bgcolor="#F7EEEE">
			<td width="168" height="30" colspan="4">
			<div align="left">
				<bean:message key="financialEntityDetails.orgRelation.question" />
			</div>
			</td>
			<td colspan="3" width="456">
			<div align="left">
				<html:radio property="orgRelationType" value="Y" />
				<bean:message key="financialEntityDetails.orgRelation.related" />

				<html:radio property="orgRelationType" value="N" />
				<bean:message key="financialEntityDetails.orgRelation.notRelated" />

				<html:radio property="orgRelationType" value="X" />
				<bean:message key="financialEntityDetails.orgRelation.dontKnow" />
			</div>
			</td>
		</tr>
		--%>
		<!-- CASE #1374 Comment End -->
<!--For this release, don't include information on sponsor.

			<%--<tr bgcolor="#F7EEEE">
				<td width="109" height="30">
					<div align="left">&nbsp;<bean:message key="financialEntityDetails.label.sponsorID" /></div>
				</td>
				<td width="7" height="30">:</td>
				<td width="52" height="30">
				<html:text  size="15"  property="sponsorId" />
                        <font face="Verdana, Arial, Helvetica, sans-serif" size="2">
                        <a href="JavaScript:openwin('coeusSearch.do?searchname=sponsorSearch&fieldName=sponsorId','sponsor')"
                           method="POST"> <img src="<bean:write name="ctxtPath"/>/images/searchpage.gif"
                           width="22" height="20" border="0"> </a> </font>
                        </td>
			      <td height="25" width="210">
                          <div align="left"><bean:message key="financialEntityDetails.label.sponsorName"/></div>
			      </td>
			      <td  height="25" width="11" >:</td>
			      <td height="25" width="235">
                          <div align="left">
                            <b>
				      <bean:write  name="frmFinancialEntityDetailsForm" property="sponsorName" scope="request" />
			          </b>
                          </div>
				<html:hidden property="sponsorName" />
			      </td>
			      --%>
<!--End of commented out sponsor information-->
<!--Another version of gather sponsor information, commented out prior to the above version being
commented out.  -->
				<%--
				<td width="210" height="30" bgcolor="#FBF7F7">
                          <div align="left"><bean:message key="financialEntityDetails.label.sponsorName" /></div>
				</td>
				<td width="11" height="30" bgcolor="#FBF7F7">:</td>
				<td width="235" height="30" bgcolor="#FBF7F7">
				<bean:write  name="frmFinancialEntityDetailsForm" property="sponsorName" scope="request" />
				<html:hidden property="sponsorName" />
				</td>
				--%>
			<%--</tr>--%>







			<!-- If user request is Adding a new financial entity then don't show the
					  below features which are required only for Edit Financial entity.
					  In other way show these features when user requests to update the Financial entity.
			-->
<!--For this release, do not display last update and sequence number information.   -->
		<%--
			<logic:equal name="frmFinancialEntityDetailsForm" property="actionType" value="U">

			<tr bgcolor="#F7EEEE">
			<td width="109" height="25">
			<div align="left">&nbsp;<bean:message key="financialEntityDetails.label.lastUpdate"/></div>
			</td>
			    <td  height="25" width="7" >:</td>
			    <td height="25" width="52">
                  <div align="left"><b>

					<bean:write  name="frmFinancialEntityDetailsForm" property="lastUpdate" scope="request" />

					<html:hidden property="lastUpdate" />
					<html:hidden property="lastUpdateTimestamp" />

				</b></div>
			</td>
			    <td height="25" width="210">
                  <div align="left"><bean:message key="financialEntityDetails.label.updateUser"/></div>
			</td>
			    <td  height="25" width="11" >:</td>
			    <td height="25" width="235">
                  <div align="left"><b>
				<bean:write  name="frmFinancialEntityDetailsForm" property="lastUpdatedUser" scope="request" />
			</b></div>
			</td>
		</tr>
		<tr bgcolor="#F7EEEE"  >
			<td width="109" height="25">
				<div align="left">&nbsp;<bean:message key="financialEntityDetails.label.sequenceNo"/></div>

			</td>
			<td width="7" height="25">:</td>
			    <td width="52" height="25">
                  <div align="left"><b>
					<bean:write  name="frmFinancialEntityDetailsForm" property="sequenceNum" scope="request" />
				</b></div>
			</td>
			    <td width="210" height="25">
                  <div align="left">&nbsp;</div>
			</td>
			    <td width="11" height="25">
                  <div align="left">&nbsp;</div>
			</td>
			    <td width="235" height="25">
                  <div align="left">&nbsp;</div>
			</td>
			</tr>
			</logic:equal>
	--%>
			<%-- Finished hiding of controls that are not required for creating
					a new Financial Entity
			--%>
		<!-- hide information that is required to edit/add Financial entity -->
		<!-- Since lastUpdate and lastUpdateTimestamp are commented out above for this release, and the information is needed by the stored procedure, include them here.-->
		<!-- End added hidden fields.  -->
			<html:hidden property="actionType" />
			<html:hidden property="userName" />
			<html:hidden property="lastUpdatedUser" />
			<html:hidden property="sequenceNum" />
			<html:hidden property="number" />
			<html:hidden property="actionFrom" />
