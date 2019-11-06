<%--
/*
 * @(#)ViewFinancialEntityContent.jsp  1.0.1 2002/06/08 11:45:12
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.

 * @author  RaYaKu
 */
--%>
<%--
A View component to show the Financial Entity Details
--%>
<%@page errorPage = "ErrorPage.jsp" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" 	prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" 	prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" 	prefix="logic" %>
<%@ taglib uri="/WEB-INF/privilege.tld" prefix="priv" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"	prefix="coeusUtils" %>
<%@ include file="CoeusContextPath.jsp"  %>
<jsp:useBean id="actionFrom" scope="request" class="java.lang.String"/>

<jsp:useBean id="personName" scope="request" class="java.lang.String" />
<jsp:useBean id="entityNo" scope="request" class="java.lang.String" />
<jsp:useBean id="seqNo" scope="request" class="java.lang.String" />
<jsp:useBean id="entityDetails" scope="request" class="edu.mit.coeus.coi.bean.EntityDetailsBean" />
<jsp:useBean id="colFinCertificationDetails" 	scope="request" class="java.util.Vector" />
<jsp:useBean id = "userprivilege" class = "java.lang.String" scope="session"/>
	      <!-- Financial Entity Details -->
		  <table width="100%" border="0" cellspacing="0" cellpadding="5" >
			<tr>
				<td colspan="6" height="5"></td>
			</tr>
			<tr bgcolor="#FBF7F7" valign="top">
				<td width="105" height="30">
					<div align="left"><bean:message key="viewFinEntity.label.name" /></div>
				</td>
				<td width="6" >
					<div align="left">:</div>
				</td>
				<td width="202" >
					<div align="left"><b> <coeusUtils:formatOutput name="entityDetails" property="name" defaultValue="&nbsp;" scope="request"/> </b></div>
				</td>
				<!-- CASE #1600 Begin -->
				<!-- Put share ownership on top line, entity type below it, to match edit fin ent page -->
				<td width="114" >
					<div align="left"><bean:message key="viewFinEntity.label.shareOwnership"/></div>
				</td>
				<td width="6" >
					<div align="left">:</div>
				</td>
				<td width="362" >
					<div align="left"><b>
		<logic:present name="entityDetails" property="shareOwnship" >
		    <logic:equal name="entityDetails" property="shareOwnship" value="P" scope="request">
			<bean:message key="viewFinEntity.label.public"/>
		    </logic:equal>
		    <logic:notEqual name="entityDetails" property="shareOwnship" value="P" scope="request">
			<bean:message key="viewFinEntity.label.private"/>
		    </logic:notEqual>
		</logic:present>
		 <logic:notPresent name="entityDetails" property="shareOwnship" >
		 &nbsp;
		 </logic:notPresent>
				  </b></div>
				</td>
			<!--CASE #1600 End -->				
				<%-- CASE #1600 Comment Begin 
				<td width="114" >
					<div align="left"><bean:message key="viewFinEntity.label.type" />&nbsp;</div>
				</td>
				<td width="6" >
					<div align="left">:</div>
				</td>
				<td width="362" >
					<div align="left"><b><coeusUtils:formatOutput name="entityDetails" property="type" defaultValue="&nbsp;" scope="request"/> </b></div>
				</td>
				CASE #1600 Comment End --%>
			</tr>
			<tr bgcolor="#F7EEEE" valign="top">
				<td width="105" height="30">
				<div align="left"><bean:message key="viewFinEntity.label.status"/></div>
				</td>
				<td width="6" >
					<div align="left">:</div>
				</td>
				<td width="202" >
					<div align="left"><b> <coeusUtils:formatOutput name="entityDetails" property="status" defaultValue="&nbsp;" scope="request"/> </b></div>
				</td>
				<!-- CASE #1600 Begin -->
				<!-- Put share ownership on top line, entity type below it, to match edit fin ent page -->
				<td width="114" >
					<div align="left"><bean:message key="viewFinEntity.label.type" />&nbsp;</div>
				</td>
				<td width="6" >
					<div align="left">:</div>
				</td>
				<td width="362" >
					<div align="left"><b><coeusUtils:formatOutput name="entityDetails" property="type" defaultValue="&nbsp;" scope="request"/> </b></div>
				</td>
				<!-- CASE #1600 End -->
				<%-- CASE #1600 Comment Begin 
				<td width="114" >
					<div align="left"><bean:message key="viewFinEntity.label.shareOwnership"/></div>
				</td>
				<td width="6" >
					<div align="left">:</div>
				</td>
				<td width="362" >
					<div align="left"><b>
		<logic:present name="entityDetails" property="shareOwnship" >
		    <logic:equal name="entityDetails" property="shareOwnship" value="P" scope="request">
			<bean:message key="viewFinEntity.label.public"/>
		    </logic:equal>
		    <logic:notEqual name="entityDetails" property="shareOwnship" value="P" scope="request">
			<bean:message key="viewFinEntity.label.private"/>
		    </logic:notEqual>
		</logic:present>
		 <logic:notPresent name="entityDetails" property="shareOwnship" >
		 &nbsp;
		 </logic:notPresent>
				  </b></div>
				</td>
			CASE #1600 Comment End --%>
			</tr>
			<tr bgcolor="#FBF7F7" valign="top">
				<td width="105" height="30">
					<div align="left"><bean:message key="viewFinEntity.label.explanation"/></div>
				</td>
				<td width="6" >
					<div align="left">:</div>
				</td>
				<td colspan="4" >
					<div align="left"><b>
						<coeusUtils:formatOutput name="entityDetails" property="entityDescription" defaultValue="&nbsp;" scope="request"/>
					</b></div>
				</td>
			</tr>
			<!-- CASE #1400 Begin -->
			<!-- Display Last Update -->
			<tr bgcolor="#F7EEEE" valign="top">
				<td width="105" height="30">
					<div align="left"><bean:message key="viewFinEntity.label.lastUpdate"/></div>
				</td>
				<td width="6" >
					<div align="left">:</div>
				</td>
				<td colspan="4" >
					<div align="left"><b>
						<coeusUtils:formatDate name="entityDetails" property="lastUpdate"  scope="request"/>
					</b></div>
				</td>
			</tr>				
			<!-- CASE #1400 End -->
			<tr>
				<td colspan="6" height="15" bgcolor="#CC9999">
					<div align="left"><font color="#FFFFFF">
						<b><bean:message key="viewFinEntity.personRelation"/>
					</b></font></div>
				</td>
			</tr>
			<tr bgcolor="#FBF7F7" valign="top">
				<td width="105" height="30">
					<div align="left"><bean:message key="viewFinEntity.personRelation.label.type"/></div>
				</td>
				<td width="6" >
					<div align="left">:</div>
				</td>
				<td width="202" >
					<div align="left"><b>
						<coeusUtils:formatOutput name="entityDetails" property="personReType" defaultValue="&nbsp;" scope="request"/>
					</b> </div>
				</td>
				<td width="114" >
					<div align="left">&nbsp;</div>
				</td>
			    <td width="6" >
					<div align="left">&nbsp;</div>
			    </td>
			    <td width="362" >
					<div align="left">&nbsp; </div>
			    </td>
			</tr>
			<tr bgcolor="#F7EEEE" valign="top">
			  <td width="105" height="30">
				<div align="left"><bean:message key="viewFinEntity.personRelation.label.explanation"/></div>
			  </td>
			  <td width="6" >
				<div align="left">:</div>
			  </td>
			  <td colspan="4" >
				<div align="left"><b>
					<coeusUtils:formatOutput name="entityDetails" property="personReDesc" defaultValue="&nbsp;" scope="request"/>
				</b></div>
			  </td>
			</tr>

	<!-- Brief info on organization's relationship to this entity -->
	<!-- CASE #1374 Comment Begin  Don't show any org relationship info -->
	<%--
			<tr bgcolor="#FBF7F7" valign="top">
			<td colspan="6" height="30" width = "100%">
			<div align="left"><bean:message key="viewFinEntity.organizationRelationShip.label"/>
			<b><coeusUtils:formatOutput name="entityDetails" property="orgRelationship" scope="request"/></b></div>
			</td>
			</tr>
	--%>
	<!-- CASE #1374 Comment End -->

<!-- More complete info on organization's relationship to this entity.  Later release will include more information here if user has maintain rights.-->
<%--
			<tr>
			  <td colspan="6" height="15" bgcolor="#CC9999">
				<div align="left"><font color="#FFFFFF"><b>
					<bean:message key="viewFinEntity.organizationRelationShip.label" />
		    <coeusUtils:formatOutput name="entityDetails" property="orgRelationship" scope="request"/>
	    </b></font></div>
			  </td>
			</tr>
			<tr bgcolor="#FBF7F7">
			  <td width="105" height="25">
				<div align="left">
					<bean:message key="viewFinEntity.organizationRelationShip.label.explanation" />
				</div>
			  </td>
			  <td width="6" height="25">
				<div align="left">:</div>
			  </td>
			  <td colspan="4" height="25">
				<div align="left"><b>
					<coeusUtils:formatOutput name="entityDetails" property="orgDescription" defaultValue="&nbsp;" scope="request"/>
				</b></div>
			  </td>
			</tr>

			<tr bgcolor="#FBF7F7">
				<td width="105" height="25">
					<div align="left"><bean:message key="viewFinEntity.organizationRelationShip.label.sponsorid" /></div>
				</td>
				<td width="6" height="25">
					<div align="left">:</div>
				</td>
				<td width="202" height="25">
					<div align="left"><b> <coeusUtils:formatOutput name="entityDetails" property="sponsor" defaultValue="&nbsp;" scope="request"/> </b></div>
				</td>
				<td width="114" height="25">
					<div align="left"><bean:message key="viewFinEntity.organizationRelationShip.label.sponsor" />&nbsp;</div>
				</td>
				<td width="6" height="25">
					<div align="left">:</div>
				</td>
				<td width="362" height="25">
					<div align="left"><b><coeusUtils:formatOutput name="entityDetails" property="sponsorName" defaultValue="&nbsp;" scope="request"/> </b></div>
				</td>
			</tr>
			<tr bgcolor="#FBF7F7">
			  <td width="105" height="25">
				<div align="left">
					<bean:message key="viewFinEntity.organizationRelationShip.label.lastUpdate"/>
				</div>
			  </td>
			  <td width="6" height="25">
				<div align="left">:</div>
			  </td>
			  <td height="25" width="202">
				<div align="left"><b>
				<coeusUtils:formatDate name="entityDetails" property="lastUpdate" />
				</b></div>
			  </td>
			  <td height="25" width="114">
				<div align="left"><bean:message key="viewFinEntity.organizationRelationShip.label.updateUser"/></div>
			  </td>
			  <td height="25" width="6">
				<div align="left">:</div>
			  </td>
			  <td height="25" width="362">
				<div align="left"><b>
					<coeusUtils:formatOutput name="entityDetails" property="updateUser" defaultValue="&nbsp;" scope="request"/>
				</b></div>
			  </td>
			</tr>
			<tr bgcolor="#F7EEEE">
			  <td width="105" height="25">
				<div align="left"><bean:message key="viewFinEntity.organizationRelationShip.label.sequenceNo"/>.</div>
			  </td>
			  <td width="6" height="25">
				<div align="left">:</div>
			  </td>
			  <td width="202" height="25">
				<div align="left"><b>
					<coeusUtils:formatOutput name="entityDetails" property="seqNumber" defaultValue="&nbsp;" scope="request"/>
				</b></div>
			  </td>
			  <td width="114" height="25">
				<div align="left">&nbsp;</div>
			  </td>
			  <td width="6" height="25">
				<div align="left">&nbsp;</div>
			  </td>
			  <td width="362" height="25">
				<div align="left">&nbsp;</div>
			  </td>
			</tr>

End of commented out section on organization's relationship to this entity
--%>
		</table>