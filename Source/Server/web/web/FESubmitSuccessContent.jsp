<%--
/*
 * @(#)FESubmitSuccessContent.jsp	1.0 02/20/2004
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  coeus-dev-team
 */
--%>

<!-- CASE #863 Begin -->
<%@page import ="edu.mit.coeus.action.coi.FinancialEntityDetailsActionForm" 
	errorPage="ErrorPage.jsp" %>
<!-- CASE #863 End -->
<%@ taglib uri="/WEB-INF/struts-html.tld"     prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld"     prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld"     prefix="logic" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"     prefix="coeusUtils" %>
<%@ include file="CoeusContextPath.jsp"  %>
<!-- CASE #863 Begin -->
<jsp:useBean id="frmFinancialEntityDetailsForm" scope="request" class="edu.mit.coeus.action.coi.FinancialEntityDetailsActionForm" />
<!-- CASe #863 End -->

 <table width="100%" border="0" cellpadding="0" cellspacing="0">
 <tr>
	<td width="657" valign="top">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">

			  <tr>
				<td height="23" bgcolor="#cccccc" class="header"> &nbsp;
				<bean:message key="FESubmitSuccess.header1" /> 
				<logic:equal name="frmFinancialEntityDetailsForm" property="actionType" value="I">
					<bean:message key="FESubmitSuccess.addedFor" />
				</logic:equal>
				<logic:equal name="frmFinancialEntityDetailsForm" property="actionType" value="U">
					 <bean:message key="FESubmitSuccess.modifiedFor" />
				</logic:equal>
				<bean:write name="frmFinancialEntityDetailsForm" property="userFullName" />
				</td>
			  </tr>
		</table>


		<table width="100%">
			<tr>
				<td align="right" height="22" colspan="3">
				<%--
				<a href="<bean:write name='ctxtPath'/>/loginCOI.do"><img src="<bean:write name='ctxtPath'/>/images/exit.gif" height="22" border="0"></a>
				--%>
				</td>
			</tr>
			<tr>
				<td height="10px">
				&nbsp
				</td>
			</tr>
		</table>
		<table width="100%">

			  <%--
			  <tr>
				<td align = "center" colspan="3">
				<font face="Verdana, Arial, Helvetica, sans-serif" size="2"><b>
				<bean:message key="FESubmitSuccess.message1" />
				</b></font>
			</tr>
			--%>
			<tr>
				<td align = "center" colspan="3">
				<font face="Verdana, Arial, Helvetica, sans-serif" size="2"><b>
				<bean:message key="FESubmitSuccess.message2" />
				<bean:write name="frmFinancialEntityDetailsForm" property="name" /> 
				
				<bean:message key="FESubmitSuccess.message3" />

				<logic:equal name="frmFinancialEntityDetailsForm" property="actionType" value="I">
					<bean:message key="FESubmitSuccess.added2" />
				</logic:equal>
				<logic:equal name="frmFinancialEntityDetailsForm" property="actionType" value="U">
					 <bean:message key="FESubmitSuccess.modified2" />
				</logic:equal>				
				 </b></font>
				</td>
			  </tr>
			  <tr>
			  	<td colspan="3" height="30">
			  		&nbsp;
			  	</td>
			  </tr>	

			<!-- CASE #863 Comment Begin -->
			<%--
			<tr>

				<td align="center" >
					<font face="Verdana, Arial, Helvetica, sans-serif" color="#7F1B00" size="1"><b>
					<logic:equal name="frmFinancialEntityDetailsForm" property="actionFrom" value="AnnualFE">
						<bean:message key="FESubmitSuccess.backToAnnDisc" />
					</logic:equal>
					<logic:notEqual name="frmFinancialEntityDetailsForm" property="actionFrom" value="AnnualFE">
						<bean:message key="FESubmitSuccess.listFEs" />
					</logic:notEqual>
					</b></font>
				</td>
								
					</td>
				<td align="center">
					<font face="Verdana, Arial, Helvetica, sans-serif" color="#7F1B00" size="1"><b>
						<bean:message key="FESubmitSuccess.viewFE" />
						<logic:equal name="frmFinancialEntityDetailsForm" property="actionType" value="I">
							<bean:message key="FESubmitSuccess.added" />
						</logic:equal>
						<logic:equal name="frmFinancialEntityDetailsForm" property="actionType" value="U">
							 <bean:message key="FESubmitSuccess.modified" />
						</logic:equal>					
					</font>				
				</td>
			</tr>			
			<tr>
				<td align = "center">
					<logic:equal name="frmFinancialEntityDetailsForm" property="actionFrom" value="AnnualFE">
						<a href='getAnnDiscPendingFEs.do'><img src="<bean:write name='ctxtPath'/>/images/ok.gif" border="0"></a>
					</logic:equal>
					<logic:notEqual name="frmFinancialEntityDetailsForm" property="actionFrom" value="AnnualFE">
						<a href='getFinEntities.do'><img src="<bean:write name='ctxtPath'/>/images/ok.gif" border="0"></a>					
					</logic:notEqual>
				</td>
				<td align = "center">				
					<a href='viewFinEntity.do?actionFrom=<bean:write name="frmFinancialEntityDetailsForm" property="actionFrom"/>&entityNo=<bean:write name="frmFinancialEntityDetailsForm" property="number"/>&seqNo=<bean:write name="frmFinancialEntityDetailsForm" property="sequenceNum"/>'><img src="<bean:write name='ctxtPath'/>/images/ok.gif" border="0"></a>
				</td>				
			</tr>
			--%>
			<!-- CASE #863 Comment End -->
			<!-- CASE #863 Begin -->
			<!-- Increment sequence number to show user financial entity just added/modified.  Put view fin entity on left and view list of fin entities on right. -->
			
			<!-- CASE #1374 Comment Begin -->
			<%--
			<tr>
				<td align="center">
					<font face="Verdana, Arial, Helvetica, sans-serif" color="#7F1B00" size="1"><b>
						<bean:message key="FESubmitSuccess.viewFE" />
						<logic:equal name="frmFinancialEntityDetailsForm" property="actionType" value="I">
							<bean:message key="FESubmitSuccess.added" />
						</logic:equal>
						<logic:equal name="frmFinancialEntityDetailsForm" property="actionType" value="U">
							 <bean:message key="FESubmitSuccess.modified" />
						</logic:equal>					
					</font>				
				</td>			

				<td align="center">
					<font face="Verdana, Arial, Helvetica, sans-serif" color="#7F1B00" size="1"><b>
					<logic:equal name="frmFinancialEntityDetailsForm" property="actionFrom" value="AnnualFE">
						<bean:message key="FESubmitSuccess.backToAnnDisc" />
					</logic:equal>
					<logic:notEqual name="frmFinancialEntityDetailsForm" property="actionFrom" value="AnnualFE">
						<bean:message key="FESubmitSuccess.listFEs" />
					</logic:notEqual>
					</b></font>
				</td>

			</tr>
			--%>
			<!-- CASE #1400 Comment Begin -->
			<!-- No longer need seq no in url.  Comment out here and remove from url below -->
			<%--				
			int newSequenceNum = Integer.parseInt(frmFinancialEntityDetailsForm.getSequenceNum())+1;
			--%>
			<%--
			<tr>
				<td align = "center">				
					<a href='<bean:write name='ctxtPath'/>/viewFinEntity.do?actionFrom=<bean:write name="frmFinancialEntityDetailsForm" property="actionFrom"/>&entityNo=<bean:write name="frmFinancialEntityDetailsForm" property="number"/>'><img src="<bean:write name='ctxtPath'/>/images/ok.gif" border="0"></a>
				</td>			
				<td align = "center">
					<logic:equal name="frmFinancialEntityDetailsForm" property="actionFrom" value="AnnualFE">
						<a href='<bean:write name='ctxtPath'/>/getAnnDiscPendingFEs.do'><img src="<bean:write name='ctxtPath'/>/images/ok.gif" border="0"></a>
					</logic:equal>
					<logic:notEqual name="frmFinancialEntityDetailsForm" property="actionFrom" value="AnnualFE">
						<a href='<bean:write name='ctxtPath'/>/getFinEntities.do'><img src="<bean:write name='ctxtPath'/>/images/ok.gif" border="0"></a>					
					</logic:notEqual>
				</td>				
			</tr>
			--%>
			<!-- CASE #863 End -->
			<!-- CASE #1374 Begin -->
			<%--
			<tr>
				<td width="25%">&nbsp;</td>
				<td width="50%">
				<a class="mediumsize" href='<bean:write name='ctxtPath'/>/viewFinEntity.do?actionFrom=<bean:write name="frmFinancialEntityDetailsForm" property="actionFrom"/>&entityNo=<bean:write name="frmFinancialEntityDetailsForm" property="number"/>'>
				<bean:message key="FESubmitSuccess.viewFE" />
				<logic:equal name="frmFinancialEntityDetailsForm" property="actionType" value="I">
					<bean:message key="FESubmitSuccess.added" />
				</logic:equal>
				<logic:equal name="frmFinancialEntityDetailsForm" property="actionType" value="U">
					 <bean:message key="FESubmitSuccess.modified" />
				</logic:equal>			
				</a>
				</td>
				<td width="25%">&nbsp;</td>
			</tr>
			<tr>
				<td >&nbsp;</td>
				<td >
					<logic:equal name="frmFinancialEntityDetailsForm" property="actionFrom" 
						value="AnnualFE">
						<a class="mediumsize" href='<bean:write name='ctxtPath'/>/getAnnDiscPendingFEs.do'>
						<bean:message key="FESubmitSuccess.backToAnnDisc" />	
						</a>
					</logic:equal>
					<logic:notEqual name="frmFinancialEntityDetailsForm" property="actionFrom" 
						value="AnnualFE">
						--%>
						<%--
						<a class="mediumsize" href='<bean:write name='ctxtPath'/>/getFinEntities.do'>
						<bean:message key="FESubmitSuccess.listFEs" />
						</a>					
						--%>
						<%--
						<a class="mediumsize" 
						href="<bean:write name='ctxtPath'/>/addFinEntity.do?actionFrom=coiFinEntity">
						<bean:message key="FESubmitSuccess.addFinEnt"/>
						</a>
					</logic:notEqual>			
				</td>
				<td >&nbsp;</td>
			</tr>
			<tr>
				<td >&nbsp;</td>
				<td>
					<bean:message key="FESubmitSuccess.addDisc1"/>
					<a href="<bean:write name='ctxtPath'/>/newdisclosure.do">
					<bean:message key="FESubmitSuccess.addDisc2"/>
					</a>
				</td>
				<td >&nbsp;</td>
			</tr>
			--%>
			<tr>
				<td width="20%">&nbsp;</td>
				<td width="60%">
				<table width="100%" cellpadding="5" cellspacing="0" border="0" bgcolor="fbf7f7">
				<tr>
					<td height="5">&nbsp;</td>
				</tr>
				<tr>
				<td>
					<ul>
					<li>
						<a class="mediumsize" href='<bean:write name='ctxtPath'/>/viewFinEntity.do?actionFrom=<bean:write name="frmFinancialEntityDetailsForm" property="actionFrom"/>&entityNo=<bean:write name="frmFinancialEntityDetailsForm" property="number"/>'>
						<bean:message key="FESubmitSuccess.viewFE" />
						<logic:equal name="frmFinancialEntityDetailsForm" property="actionType" value="I">
							<bean:message key="FESubmitSuccess.added" />
						</logic:equal>
						<logic:equal name="frmFinancialEntityDetailsForm" property="actionType" value="U">
							 <bean:message key="FESubmitSuccess.modified" />
						</logic:equal>			
						</a><br><br>
					</li>
					<li>
						<logic:equal name="frmFinancialEntityDetailsForm" property="actionFrom" 
							value="AnnualFE">
							<a class="mediumsize" href='<bean:write name='ctxtPath'/>/getAnnDiscPendingFEs.do'>
							<bean:message key="FESubmitSuccess.backToAnnDisc" />	
							</a><br><br>
						</logic:equal>
						<logic:notEqual name="frmFinancialEntityDetailsForm" property="actionFrom" 
							value="AnnualFE">
							<%--
							<a class="mediumsize" href='<bean:write name='ctxtPath'/>/getFinEntities.do'>
							<bean:message key="FESubmitSuccess.listFEs" />
							</a>					
							--%>
							<a class="mediumsize" 
							href="<bean:write name='ctxtPath'/>/addFinEntity.do?actionFrom=coiFinEntity">
							<bean:message key="FESubmitSuccess.addFinEnt"/>
							</a><br><br>
						</logic:notEqual>			
					</li>
					<li>
						<bean:message key="FESubmitSuccess.addDisc1"/><br>
						<a href="<bean:write name='ctxtPath'/>/newdisclosure.do">
						<bean:message key="FESubmitSuccess.addDisc2"/>
						</a>	
						<bean:message key="FESubmitSuccess.addDisc3"/>
					</li>
					</ul>
				</td>
				</tr>
				<tr>
				<td height="5">&nbsp;</td>
				</tr>
				</table>
				</td>
				<td width="20%">&nbsp;</td>
			</tr>
			
			<!-- CASE #1374 End -->
			<tr>
				<td height="22px" colspan="3">
				&nbsp
				</td>
			</tr>
			
<%--
<html:form action="/nextaction">
<table width="100%" border="0" cellpadding="2" cellspacing="2">
<tr>
	<td width="100">&nbsp;</td>
	<td width="557" class="mediumsize" colspan="2">
		<html:radio property="nextAction" value="1" />
		<b>
		<bean:message key="FESubmitSuccess.viewFE" />
		<logic:equal name="frmFinancialEntityDetailsForm" property="actionType" value="I">
			<bean:message key="FESubmitSuccess.added" />
		</logic:equal>
		<logic:equal name="frmFinancialEntityDetailsForm" property="actionType" value="U">
			 <bean:message key="FESubmitSuccess.modified" />
		</logic:equal>	
		</b>
	</td>
</tr>
<tr>
	<td width="100">&nbsp;</td>
	<td width="557" class="mediumsize" colspan="2">
		<html:radio property="nextAction" value="2" />
		<b>
		<logic:equal name="frmFinancialEntityDetailsForm" property="actionFrom" value="AnnualFE">
			<bean:message key="FESubmitSuccess.backToAnnDisc" />
		</logic:equal>
		<logic:notEqual name="frmFinancialEntityDetailsForm" property="actionFrom" value="AnnualFE">
			<bean:message key="FESubmitSuccess.listFEs" />
		</logic:notEqual>		
		</b>
	</td>
</tr>
<tr>
	<td width="100">&nbsp;</td>
	<td width="557" class="mediumsize" colspan="2">
		<html:radio property="nextAction" value="3" />
		<b>
		<bean:message key="FESubmitSuccess.addNewDisclosure"/>
		</b>
	</td>
</tr>
<tr>
	<td width="100">&nbsp;</td>
	<td width="557" class="mediumsize" colspan="2">
		<html:radio property="nextAction" value="4" />
		<b>
		<bean:message key="FESubmitSuccess.exit"/>
		</b>
	</td>
</tr>
<tr>
	<td>&nbsp;</td>
	<td width="200">&nbsp;</td>
	<td >
	<html:image page="/images/ok.gif" border="0"  />&nbsp;
	</td>
</tr>
</table>
	
	<input type="hidden" name="URL1" 
	value="/viewFinEntity.do?actionFrom=<%=frmFinancialEntityDetailsForm.getActionFrom()%>&entityNo=<%=frmFinancialEntityDetailsForm.getNumber()%>" />
	
	<logic:equal name="frmFinancialEntityDetailsForm" property="actionFrom" value="AnnualFE">
		<input type="hidden" name="URL2" 
		value='/getAnnDiscPendingFEs.do'/>
	</logic:equal>
	<logic:notEqual name="frmFinancialEntityDetailsForm" property="actionFrom" value="AnnualFE">
		<input type="hidden" name="URL2" 
		value='/getFinEntities.do'/>		
	</logic:notEqual>	
	<input type="hidden" name="URL3" value='/newdisclosure.do' />
	<input type="hidden" name="URL4" value='/loginCOI.do' />
</html:form>
--%>
	</table>
	
	</td>
	</tr>
</table>

