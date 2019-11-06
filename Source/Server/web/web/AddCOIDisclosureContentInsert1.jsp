<%--
/*
 * @(#)AddCOIDisclosureContent.jsp	1.0 2002/06/12 01:32:13 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.va;
 *
 * @author  RaYaKu
 */
--%>
<%-- This JSP file is for Adding a new COI Disclosure for a person--%>

<%@page import="edu.mit.coeus.coi.bean.CertificateDetailsBean,
		edu.mit.coeus.coi.bean.DisclosureInfoBean,
		edu.mit.coeus.coi.bean.ComboBoxBean,
                java.net.URLEncoder,
                org.apache.struts.action.Action" 
	errorPage="ErrorPage.jsp" %>
<%@ taglib uri="/WEB-INF/struts-html.tld"     prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld"     prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld"     prefix="logic" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"     prefix="coeusUtils" %>
<%@ include file="CoeusContextPath.jsp"  %>
<!-- CASE #1374 All values are in actionform. DisclosureHeaderBean not needed -->
<%--<jsp:useBean id="disclosureHeaderBean" scope='session' class="edu.mit.coeus.coi.bean.DisclosureHeaderBean"/>--%>
<jsp:useBean id="personId" scope ="request" class = "java.lang.String" />
<!-- CASE #1374 Begin -->
<!--Get collection of proposal types from session -->
<jsp:useBean id="collProposalTypes" scope="session" class="java.util.Vector" />
<!-- Put action form on page as bean, to access its attributes directly -->
<jsp:useBean id="frmAddCOIDisclosure" class="edu.mit.coeus.action.coi.AddCOIDisclosureActionForm" scope="session"/>
<!-- CASE #1374 End -->

	<tr>
	  <td height="5" width="100%"></td>
	</tr>	  
	  <tr>
		<td colspan="2" height="23" bgcolor="#cccccc" class="header"> 
		&nbsp;<b>
		<!--<font face="Verdana, Arial, Helvetica, sans-serif" size="2">-->
		&nbsp;<bean:message key="addCOIDisclosure.header"/>
		<!-- CASE #1374 Comment Begin -->
		<%--<bean:write name="frmAddCOIDisclosure" scope="session" property="disclosureNo" />--%>
		<!-- CASE #1374 Comment End -->
		<!-- CASE #1374 Begin -->
		<bean:write name="frmAddCOIDisclosure" scope="session" property="personFullName" />
		<!-- CASE #1374 End -->
		<!--</font>-->
		</b> </td>
	  </tr>
	  <tr>
		<td height="23">
		  <table width="100%" border="0" cellspacing="0" cellpadding="5">
			<!-- CASE #1374 Begin -->
			<tr>
			<td>
				<b><font color="red">*</font></b>
				<font class="fontBrown"> indicates required field</font>
			</td>
			  <td height="40" align="right">
				  <html:image page="/images/submit.gif" border="0"/>
				  &nbsp; &nbsp; &nbsp;
			  </td>			
			</tr>
		</table>
		<table width="100%" border="0" cellspacing="0" cellpadding="5">
		<!-- CASE #1393 Begin -->
			<logic:present name="<%=Action.ERROR_KEY%>">
			<tr>
			<td colspan="4">
				<b><bean:message key="validationErrorHeader"/></b>
				<bean:message key="validationErrorSubHeader2"/>
			</td>
			</tr>
			</logic:present>
		<!-- CASE #1393 End -->	
			<tr>	
				  <td valign="top" width="120">
					<logic:present name="frmAddCOIDisclosure" scope="session">
						<logic:equal name="frmAddCOIDisclosure" scope="session" property="disclosureTypeCode" value="1">
							<bean:message key="addCOIDisclosure.label.awardTitle"/>
						</logic:equal>
						<logic:notEqual name="frmAddCOIDisclosure" scope="session" property="disclosureTypeCode" value="1">
							 <bean:message key="addCOIDisclosure.label.proposalTitle"/>
						</logic:notEqual>
					</logic:present>
					&nbsp;:&nbsp;
				</td>
					<logic:equal name="frmAddCOIDisclosure" scope="session" property="disclosureTypeCode" value="3">
						<td valign="top">
							<b><font color="red">*</font></b>
						</td>
						<td colspan="2">
							<font color="red">
							<html:errors property="title"/>
							</font>
							<textarea name="title" wrap='virtual' cols="40" rows="2" ><bean:write name='frmAddCOIDisclosure' property='title' scope="session"/></textarea>
						</td>
					</logic:equal>
					<logic:notEqual name="frmAddCOIDisclosure" scope="session" 
						property="disclosureTypeCode" value="3">
						<td colspan="3">
						<%--<b><bean:write name="frmAddCOIDisclosure" property="title"/></b>--%>
						<textarea name="title" disabled="true" wrap='virtual' cols="40" rows="2" ><bean:write name='frmAddCOIDisclosure' property='title' scope="session"/></textarea>						
						</td>
					</logic:notEqual>
				  </td>					 
			</tr>
			<logic:equal name="frmAddCOIDisclosure" scope="session" property="disclosureTypeCode" value="3">
			<tr>			
				<td valign="top" width="120">
				<bean:message key="addCOIDisclosure.label.proposalType"/>&nbsp;:&nbsp;
				</td>
				<td valign="top" width="2">
					<b><font color="red">*</font></b>
				</td>
				<td valign="top" colspan="2">
				<font color="red">
				<html:errors property="proposalType"/>
				</font>
				  <select name="typeCode">
				  	<%
				  	String preselectedTypeCode = frmAddCOIDisclosure.getTypeCode();
				  	System.out.println("preselectedTypeCode: "+preselectedTypeCode);
				  	String selected = "";
				  	for(int cnt=0; cnt< collProposalTypes.size(); cnt++){
				  		ComboBoxBean comboBox = (ComboBoxBean)collProposalTypes.get(cnt);
				  		selected = "";
				  		if(comboBox.getCode().equals(preselectedTypeCode)){
				  			selected = "selected";
				  		}
				  	
				  	%>
				  	<option value="<%=comboBox.getCode()%>"<%=selected%>><%=comboBox.getDescription()%></option>
				  	<%
				  	}
				  	%>
				  </select>
				</td>				
			</tr>
			</logic:equal>	
			
			<tr>			
				<td valign="top">
				<bean:message key="addCOIDisclosure.label.sponsor"/>&nbsp;:&nbsp;
				</td>
				<logic:equal name="frmAddCOIDisclosure" scope="session" property="disclosureTypeCode" value="3">
				<td valign="top">
					<b><font color="red">*</font></b>
				</td>				
					<td colspan="2">
						<font color="red">
						<html:errors property="sponsor"/>
						</font>				
						<html:text property="sponsorName" size="25" />				
					</td>
				</logic:equal>
				<logic:notEqual name="frmAddCOIDisclosure" scope="session" property="disclosureTypeCode" value="3">
					<td colspan="3">
						<%--<b><bean:write name="frmAddCOIDisclosure" property="sponsorName"/></b>--%>
						<html:text size="45" disabled="true" property="sponsorName"/>
					</td>
				</logic:notEqual>
				
			</tr>				
			<!-- CASE #1374 End -->
<!-- CASE #864 Begin -->
	<!-- CASE #1393 Comment Begin -->
                  <%--
		<tr>
		<td colspan="2">
                  <!-- Show Errors -->

                 <html:errors/>
		<!-- For error message that needs dynamic content of fin ent info -->
		<logic:present name="errorEntityName">

			<bean:message key="error.addCOIDisclosure.entYesAnsNo1"/>

			<a href="<bean:write name='ctxtPath'/>/viewFinEntity.do?actionFrom=coiFinEntity&entityNo=<%=request.getAttribute("errorEntityNumber")%>&seqNo=<%=request.getAttribute("errorEntitySeqNo")%>"> <%=request.getAttribute("errorEntityName")%>.</a>

			<bean:message key="error.addCOIDisclosure.entYesAnsNo2"/>
		</logic:present>
		</td>
		</tr>
	<!-- CASE #1393 Comment End -->
--%>

<!-- CASE #864 End -->
		  </table>
		  <!-- CASE #1374 Comment Begin -->
		  <!-- Don't include disclosure details for add discl page.-->
		  <%--
		  <table width="100%" border="0" cellspacing="0" cellpadding="0">		  
			<tr>
			  <td background="<bean:write name='ctxtPath'/>/images/tabback.gif">
				<table border=0 cellpadding=0 cellspacing=0>
				  <tr>
					<td> <img src="<bean:write name='ctxtPath'/>/images/disclosure.gif" width="120" height="24"></td>
				  </tr>
				</table>
			  </td>
			</tr>
		  </table>
		  <table width="100%" border="0" cellspacing="0" cellpadding="5">
			<tr>
			  <td colspan="6" height="5"></td>
			</tr>
			<tr valign="top">
			  <td width="100" height="20" bgcolor="#FBF7F7">
				<div align="left"><bean:message key="addCOIDisclosure.label.personName"/></div>
			  </td>
			  <td width="1" height="20" bgcolor="#FBF7F7"><div>:</div></td>
			  <td width="176" height="20" bgcolor="#FBF7F7">
				<div align="left"><b><bean:write name="frmAddCOIDisclosure" scope="session" property="personFullName" /></b></div>
				<html:hidden property="personFullName" />
			  </td>
			  <td width="123" height="20" bgcolor="#FBF7F7">
				<div align="left"><bean:message key="addCOIDisclosure.label.disclosureNo"/></div>
			  </td>
			  <td width="3" height="20" bgcolor="#FBF7F7"><div>:</div></td>
			  <td width="238" height="20" bgcolor="#FBF7F7">
				<div align="left"><b><bean:write name="frmAddCOIDisclosure" scope="session" property="disclosureNo" /></b> </div>
			  </td>
			</tr>

			<tr bgcolor="#F7EEEE" valign="top">
			  <td width="100" height="20">
				<div align="left"><bean:message key="addCOIDisclosure.label.appliesTo"/></div>
			  </td>
			  <td width="1" height="20"><div>:</div></td>
			  <td width="176" height="20">
				<div align="left"><b>
				<logic:equal name="frmAddCOIDisclosure" scope="session" property="disclosureTypeCode" value="1">
					<bean:message key="addCOIDisclosure.label.award"/>
				</logic:equal>
				<logic:notEqual name="frmAddCOIDisclosure" scope="session" property="disclosureTypeCode" value="1">
					 <bean:message key="addCOIDisclosure.label.proposal"/>
				</logic:notEqual>
				</b></div>
			  </td>
			  <td width="123" height="20">
				<div align="left">
				 <logic:equal name="frmAddCOIDisclosure" scope="session" property="disclosureTypeCode" value="1">
					<bean:message key="addCOIDisclosure.label.awardNo"/>
				</logic:equal>
				<logic:notEqual name="frmAddCOIDisclosure" scope="session" property="disclosureTypeCode" value="1">
					 <bean:message key="addCOIDisclosure.label.proposalNo"/>
				</logic:notEqual>
				</div>
			  </td>
			  <td width="3" height="20"><div>:</div></td>
			  <td width="238" height="20">
				<div align="left"><b><bean:write name="frmAddCOIDisclosure" scope="session" property="appliesToCode" /></b> </div>
			  </td>
			</tr>

			<tr valign="top">
			  <td width="100" height="20" bgcolor="#FBF7F7">
				<div align="left"><bean:message key="addCOIDisclosure.label.title"/></div>
			  </td>
			  <td height="20" bgcolor="#FBF7F7" width="1"><div>:</div></td>
			  <td height="20" colspan='4' bgcolor="#FBF7F7" >
				<div align="left"><b><bean:write name="disclosureHeaderBean" property="title"/></b></div>
			  </td>
			</tr>
			<tr bgcolor="#F7EEEE" valign="top">
			  <td width="100" height="20">
				<div align="left"><bean:message key="addCOIDisclosure.label.account"/></div>
			  </td>
			  <td width="1" height="20"><div>:</div></td>
			  <td width="176" height="20" >
				<div align="left"><b><bean:write name="disclosureHeaderBean" property="account"/></b></div>
			  </td>
			  <td width="123" height="20">
				<div align="left"><bean:message key="addCOIDisclosure.label.status"/></div>
			  </td>
			  <td width="3" height="20"><div>:</div></td>
			  <td width="238" height="20">
				<div align="left"><b><bean:write name="disclosureHeaderBean" property="status"/></b></div>
			  </td>
			</tr>
			<tr valign="top">
			  <td width="100" height="20" bgcolor="#FBF7F7">
				<div align="left"><bean:message key="addCOIDisclosure.label.disclosureType"/></div>
			  </td>
			  <td width="1" height="20" bgcolor="#FBF7F7"><div>:</div></td>
			  <td width="176" height="20" bgcolor="#FBF7F7">
				<div align="left">
				<logic:present name="frmAddCOIDisclosure" scope="session" property="disclosureType" >
					<logic:equal name="frmAddCOIDisclosure" scope="session" property="disclosureType" value="A">
						<b><bean:message key="addCOIDisclosure.label.annual"/></b>
					</logic:equal>
					<logic:equal name="frmAddCOIDisclosure" scope="session" property="disclosureType" value="I">
						<b><bean:message key="addCOIDisclosure.label.initial" />
						</b>
					</logic:equal>
					<html:hidden name="frmAddCOIDisclosure" scope="session" property="disclosureType" />

				</logic:present>
				<logic:notPresent name="frmAddCOIDisclosure" scope="session" property="disclosureType" >
					&nbsp;
				</logic:notPresent>
				</div>
			  </td>
			  <td width="123" height="20" bgcolor="#FBF7F7">
				<div align="left"><bean:message key="addCOIDisclosure.label.disclosureStatus"/></div>
			  </td>
			  <td width="3" height="20" bgcolor="#FBF7F7"><div>:</div></td>
			  <td width="238" height="20" bgcolor="#FBF7F7">
				<div align="left"><b><bean:message key="addCOIDisclosure.label.piReviewed"/></b>
				<!-- Keep Disclousure Status  information in hidden property -->
				<html:hidden name="frmAddCOIDisclosure" scope="session" property="status" value="101"/>
				</div>
			  </td>
			</tr>
			<tr bgcolor="#F7EEEE" valign="top">
			  <td width="100" height="20">
				<div align="left"><bean:message key="addCOIDisclosure.label.reviewedBy"/></div>
			  </td>
			  <td height="20" width="1">
				<div align="left">: </div>
			  </td>
			  <td height="20" width="176" bgcolor="#F7EEEE">

				<div align="left"><b><bean:message key="addCOIDisclosure.label.initialReviewer"/></b>
				<!-- Keep Reviewer information in hidden property -->

                                <!--CASE#157 value of reviewer was being defaulted to 2 (OSP) prior to this fix
                                    This is set to 1 now (PI)  -->

				<html:hidden name="frmAddCOIDisclosure" scope="session" property="reviewer" value="1" />
			</div>
			  </td>
			  <td height="20" width="123">&nbsp;</td>
			  <td height="20" width="3">&nbsp;</td>
			  <td height="20" width="238" bgcolor="#F7EEEE">&nbsp;</td>
			</tr>
		  </table>
		  --%>
		  <!-- CASE #1374 Comment End -->
		</td>
	  </tr>