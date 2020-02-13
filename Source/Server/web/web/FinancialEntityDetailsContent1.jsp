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

<jsp:useBean id="collEntityCertDetails" scope="session" class="java.util.Vector" />
<jsp:useBean id="collOrgTypes" scope="session" class="java.util.LinkedList" />
<jsp:useBean id="collEntityStatus" scope="session" class="java.util.LinkedList" />
<jsp:useBean id="collRelations" scope="session" class="java.util.LinkedList" />
<!-- CASE #1374 make frmFinancialEntityDetails available to JSP -->
<jsp:useBean id="frmFinancialEntityDetailsForm" class="edu.mit.coeus.action.coi.FinancialEntityDetailsActionForm"
	scope="request"/>
<%-- Find the size of collEntityCertDetails --%>
<bean:size id="certificatesSize" name="collEntityCertDetails" />
<%
//create a vector of comboboxbean instances to show Share owner ship options
Vector optionsShareOwnership = new Vector();
optionsShareOwnership.add(new ComboBoxBean(""," ") );
optionsShareOwnership.add(new ComboBoxBean("P","Public") );
optionsShareOwnership.add(new ComboBoxBean("V","Private") );
pageContext.setAttribute("optionsShareOwnership",optionsShareOwnership);
%>
<table width="100%"  cellpadding="0" cellspacing="0" border="0">
<html:form action="/finEntityDetails.do" >
 <tr>
  <td background="<bean:write name="ctxtPath"/>/images/separator.gif" width="1">
    <image src="<bean:write name="ctxtPath"/>/images/separator.gif" width="1">
  </td>
  <td width="645">
    <table width="100%" border="0" cellspacing="0" align="left" cellpadding="0">
		<tr>
		  <td height="5" width="100%"></td>
		</tr>
		<tr bgcolor="#cccccc">
		  <td height="23" width="100%" class="header" > &nbsp; 
            		<bean:message key="financialEntityDetails.header" />
            		<bean:write  name="frmFinancialEntityDetailsForm" property="userFullName" scope="request" />
			<html:hidden property="userFullName" />
		  </td>
		</tr>
	
		<tr>
		  <td colspan="2">
<!-- insert FinancialEntityDetailsContentInsert1.jsp -->
<jsp:include page="FinancialEntityDetailsContentInsert1.jsp" flush="true"/>
<!-- end insert -->
		<table width="100%" border="0" cellspacing="0" cellpadding="5">		
			<tr bgcolor="#FBF7F7">
				<td width="109" height="30" >
					<div align="left"><bean:message key="financialEntityDetails.name" /></div>
				</td>
				<td width="7" height="30" >:</td>
				<td width="2" valign="top"><font color="red"><b>*</b></font></td>
				<td width="50" height="30" >
					<div>
					<html:text  size="25"  property="name" />
					</b></div>
                		</td>
                		<!-- CASE #1600 Comment Begin 
                		Move this code to the next line.  Descriptions were changed to longer names, 
                		which makes the page stretch too wide.
				<td width="210" height="30" >
                  			<div align="left"><bean:message key="financialEntityDetails.type" /></div>
				</td>
				<td width="11" height="30" >:</td>
				<td width="235" height="30" >
					<div><b>
					<html:select property="typeCode" >
	 			        <html:options collection="collOrgTypes" property="code"    
	 			        labelProperty="description"  />
					</html:select>
					</b></div>
				</td>
                		CASE #1600 Comment End -->
                		<!-- CASE #1600 Begin -->
                		<!-- Move share ownership dropdown up from the line below -->
				<td  height="30" >
					<div align="left"><bean:message key="financialEntityDetails.shareOwnership" /></div>
				</td>
				<td >: </td>
				<td align="left" >
					<div><b>
                			<html:select property="shareOwnership" >
                                            <html:options collection="optionsShareOwnership" property="code" 
                                            labelProperty="description" />
					</html:select>
					</b></div>
				</td>
                		<!-- CASE #1600 End -->

			</tr>
			<!-- CASE #1374 Comment Begin -->
			<!-- Remove entity status and explanation for inactive status from the page -->
			<%--			
			<tr bgcolor="#F7EEEE">

				<td  height="30">
                  <div align="left"><bean:message key="financialEntityDetails.status" /></div>
				</td>
				<td  height="30">:</td>
				<td valign="top"><font color="red"><b>*</b></font></td>
				<td  height="30">
					<div><b>
					<html:select property="status" >
						<html:options collection="collEntityStatus" property="code" 
						labelProperty="description" />
					    </html:select>
					    </b></div>
				</td>
				<td  height="30">
					<div align="left"><bean:message key="financialEntityDetails.shareOwnership" /></div>
				</td>
				<td height="30">:</td>
				<td height="30">
					<div><b>
                			<html:select property="shareOwnership" >
                                            <html:options collection="optionsShareOwnership" property="code" 
                                            labelProperty="description" />
					</html:select>
					</b></div>
				</td>
			</tr>
			<tr bgcolor="#FBF7F7">
				<td  height="70" >
					<div align="left"><bean:message key="financialEntityDetails.explanation" /></div>
				</td>
				<td  height="70">:</td>
				<td>&nbsp;</td>
				<td colspan="4" height="30" bgcolor="#FBF7F7">
					<div>
                                        <textarea name="description"
                                            wrap='virtual' cols="40" rows="2" ><bean:write
                                                name='frmFinancialEntityDetailsForm' property='description'/></textarea>
                                        </div>
				</td>
			</tr>
			--%>
			<!-- CASE #1374 Comment End -->
			<!-- CASE #1374 Begin -->
			<tr bgcolor="#F7EEEE">
				<!-- CASE #1600 Comment Begin 
				Move share ownership up to the line above.
				<td  height="30" >
					<div align="left"><bean:message key="financialEntityDetails.shareOwnership" /></div>
				</td>
				<td >:</td>
				<td>&nbsp;</td>
				<td align="left" colspan="4" >
					<div><b>
                			<html:select property="shareOwnership" >
                                            <html:options collection="optionsShareOwnership" property="code" 
                                            labelProperty="description" />
					</html:select>
					</b></div>
				</td>
				CASE #1600 Comment End -->
				<!-- CASE #1600 Begin -->
				<!-- Moved entity type drop down, since descriptions changed to longer names -->
				<td height="30" >
                  			<div align="left"><bean:message key="financialEntityDetails.type" /></div>
				</td>
				<td height="30" >:</td>
				<td>&nbsp;</td>
				<td height="30" colspan="4">
					<div><b>
					<html:select property="typeCode" >
	 			        <html:options collection="collOrgTypes" property="code"    
	 			        labelProperty="description"  />
					</html:select>
					</b></div>
				</td>
				<!-- CASE #1600 End -->
			</tr>
			<tr>
			<td height="5">&nbsp;</td>
			</tr>
			<!-- CASE #1374 End -->
			<tr bgcolor="#CC9999">
				<td colspan="7" height="25">
					<div align="left"><font color="#FFFFFF"><b>
					<bean:message key="financialEntityDetails.personRelation.header" />
						</b>
					</font></div>
				</td>
			</tr>
			<tr bgcolor="#F7EEEE">
				<td  height="30">
				<div align="left"><bean:message key="financialEntityDetails.personRelation.type" /></div>

				</td>
				<td  height="30">:</td>
				<td valign="top"><b><font color="red">*</font></b></td>
                		<td  height="30">
                			<div><b>
                			<!-- CASE #1374 Comment Begin -->
                			<!-- Default to Please Select -->
                			<%--
					<html:select property="personRelationTypeCode" >
							<html:options collection="collRelations" property="code" 
							labelProperty="description" />
				    	</html:select>
				    	--%>
				<!-- CASE #1601 Comment Begin
				Drop-down list is alphabetized.  Move Other to the bottom of the list.
				<SELECT name="personRelationTypeCode">
					<option selected value="">- Please Select -</option>
					<%--
					String preselectedPersonReTypeCode = frmFinancialEntityDetailsForm.getPersonRelationTypeCode();
					System.out.println("preselectedPersonReTypeCode: "+preselectedPersonReTypeCode);
					int lstSize = collRelations.size();
					for(int i=0;i<lstSize;i++){
						ComboBoxBean comboBox = (ComboBoxBean)collRelations.get(i);
						String code = comboBox.getCode();
						String description = comboBox.getDescription();
						String strSelected = "";
						if(code.equalsIgnoreCase(preselectedPersonReTypeCode)) {
						    strSelected = "selected";
						}
						--%>
						<%--<option <%= strSelected %> value="<%= code %>"><%= description %></option>--%>
					<%--
					}
					--%>
				<!--</SELECT>
				CASE #1601 Comment End -->
				<!-- CASE #1601 Begin -->
				<!-- Drop-down list is alphabetized.  Move Other to the bottom of the list. -->
				<SELECT name="personRelationTypeCode">
					<option selected value="">- Please Select -</option>
					<%
					String preselectedPersonReTypeCode = frmFinancialEntityDetailsForm.getPersonRelationTypeCode();
					System.out.println("preselectedPersonReTypeCode: "+preselectedPersonReTypeCode);
					int lstSize = collRelations.size();
					for(int i=0;i<lstSize;i++){
						ComboBoxBean comboBox = (ComboBoxBean)collRelations.get(i);
						String code = comboBox.getCode();
						String description = comboBox.getDescription();
						String strSelected = "";
						if(!code.equals("5")){
							if(code.equalsIgnoreCase(preselectedPersonReTypeCode)) {
							    strSelected = "selected";
							}
							%>
							<option <%= strSelected %> value="<%= code %>"><%= description %></option>
						<%
						}
					}
					for(int i=0;i<lstSize;i++){
						ComboBoxBean comboBox = (ComboBoxBean)collRelations.get(i);
						String code = comboBox.getCode();
						String description = comboBox.getDescription();
						String strSelected = "";
						if(code.equals("5") ) {
							if (code.equalsIgnoreCase(preselectedPersonReTypeCode) ) {
						    		strSelected = "selected";
							}
							%>
							<option <%= strSelected %> value="<%= code %>"><%= description %></option>
						<%
						}
					}
					%>
				</SELECT>
				<!-- CASE #1601 End -->
				    	</b></div>
				</td>
				<td  height="30">
                  <div align="right">&nbsp;</div>
				</td>
				<td  height="30">&nbsp;</td>
				<td height="30">&nbsp; </td>
			</tr>
<!-- insert FinancialEntityDetailsContentInsert2.jsp -->
<jsp:include page="FinancialEntityDetailsContentInsert2.jsp" flush="true"/>
<!-- end insert -->
	</table>
	</tr>
	<tr>
		<td height="23" colspan="2">&nbsp;
	</tr>
	<tr>
		<td height="23" colspan="2">
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				valign="top">
				<tr>
					<td background="<bean:write name="ctxtPath"/>/images/tabback.gif">
					 <table border=0 cellpadding=0 cellspacing=0>
						<tr>
							<td> <img src="<bean:write name="ctxtPath"/>/images/certification.gif" width="120" height="24"></td>
						</tr>
					 </table>
					</td>
				</tr>
			</table>
<!-- Show all  Certificates if available-->
<jsp:include page="FinancialEntityDetailsContentInsert3.jsp" flush="true"/>

	</tr>
	<tr align="right">
		  <!-- CASE #665 Comment Begin -->
		  <%--
		  <td height="23" width="100%">
		  <div align="right">
			<a href="JavaScript:history.back();">
			<img src="<bean:write name="ctxtPath"/>/images/goback.gif" width="55" height="22" border="0"></a>
			<html:image src="images/submit.gif"  border="0" />
			</div>
		</td>
		--%>
		<!-- CASE #665 Comment End -->
		<!-- CASE #665 Begin -->
		  <!-- Remove Back button.  Line up submit buttons -->
		<td height="23" width="645" align="right">
			<html:image page="/images/submit.gif"  border="0" /> &nbsp;&nbsp;&nbsp;
		</td>
		<!-- CASE #665 End -->

		  <td height="23" width="0%">&nbsp;</td>
	</tr>
	</table>
  </td>
  </tr>
  </html:form>
</table>