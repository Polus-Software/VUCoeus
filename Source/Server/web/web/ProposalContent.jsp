<%--
/**
 * @(#)ProposalContent.jsp
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author: Coeus Dev Team
 * @version 1.0 $ $ Date July 28, 2002 $ $
 *
 * Display proposal and budget summary information for a given,
 * user-requested proposal.
 * Access ProposalBean and BudgetSummaryBean for the given proposal,
 * stored in request object by DisplayProposalAction. 
 */
--%>
<%@ page language="java" %>
<%@ page  errorPage="/ErrorPage.jsp"
		import="edu.mit.coeus.utils.CoeusConstants" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"     prefix="util" %>

<%@ include file="CoeusContextPath.jsp"  %>

<jsp:useBean id="proposal" scope="request" class="edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean" />
<html:html>
<body>

<logic:present  name="proposal"  property="proposalNumber">
<table border="0"  width="100%" >
            <tr>
            <td height="5"></td>
          </tr>
<tr>
	<td height="23" bgcolor="#CCCCCC" width="100%" colspan="2" class="header">
	<!--<h2>-->
	&nbsp;<b>Proposal <bean:write name="proposal" property="proposalNumber" /> </b>
	<!--</h2>-->
	</td>
</tr>
<table width="100%" ><!--cellpadding="2" cellspacing="2" border="0">-->
<tr><td bgcolor=#aa3333 colspan=2><b><font color=#fffff7>Proposal Summary</font></b></td></tr>
<tr>
	<td width="35%">
		<bean:message key="title.label" />
	</td>
	<td>
	<logic:present name="proposal" property="title">
		<bean:write name="proposal" property="title" />
	</logic:present>
	&nbsp
	</td>
</tr>
<tr>
	<td>
		<bean:message key="proposalNumber.label" />
	</td>
	<td>
		<bean:write name="proposal" property="proposalNumber" />
	</td>
</tr>
<tr>
	<td>
		<bean:message key="sponsor.label" />
	</td>
	<td>
	<logic:present name="proposal" property="sponsorCode">
		<bean:write name="proposal" property="sponsorCode" />:
		<bean:write name="proposal" property="sponsorName" />
	</logic:present>
	&nbsp
	</td>
</tr>
<tr>
	<td>
		<bean:message key="principalInvestigator.label" />
	</td>

	<td>
 	  <logic:present name="proposal" property="investigators" >
		<logic:iterate id="proposalInvestigator" name="proposal" property="investigators" type="edu.mit.coeus.propdev.bean.ProposalInvestigatorFormBean">
			<logic:equal name="proposalInvestigator" property="principleInvestigatorFlag" value="true">
				<bean:write name="proposalInvestigator" property="personName" />
			</logic:equal>
		</logic:iterate>
  	  </logic:present>
	  &nbsp
	</td>
</tr>
<tr>
	<td>
		<bean:message key="leadUnit.label" />
	</td>
	<td>
		<bean:write name="proposal" property="ownedBy" />:
		<bean:write name="proposal" property="ownedByDesc" />
	</td>
</tr>
<tr>
	<td>
		<bean:message key="status.label" />
	</td>
	<td>
		<bean:write name="proposal" property="creationStatusDescription" />
	</td>
</tr>
<tr>
	<td>
		<bean:message key="deadline.label" />
	</td>
	<td>
	<util:formatDate name="proposal" property="deadLineDate"  />
	</td>
</tr>
<tr>
	<td>
		<bean:message key="startDate.label" />
	</td>
	<td>
	<util:formatDate name="proposal" property="requestStartDateInitial" />
	&nbsp
	</td>
</tr>
<tr>
	<td>
		<bean:message key="endDate.label" />
	</td>
	<td>
	<util:formatDate name="proposal" property="requestEndDateInitial" />
	&nbsp
	</td>
</tr>
</table>
<p />
<!-- Begin Budget Information -->
<table border = "0" width="100%">
<tr>
	<td bgcolor=#aa3333 colspan=2><b><font color=#fffff7>
		Budget Summary</font></b>
	</td>
</tr>
<logic:present name="budgetExists">
<logic:equal name="budgetExists" value="false">
	<tr>
		<td>
		No budget information exists for this proposal.
		</td>
	</tr>
</logic:equal>
<logic:equal name="budgetExists" value="true">
<tr>
	<td width="35%" >
		<bean:message key="totalDirectCost.label" />
	</td>
	<td >
	<logic:present name="budget" property="totalDirectCost"  >
		<%--	<bean:write name="budget" property ="totalDirectCost" />--%>
		<util:formatString name="budget" property="totalDirectCost" formatType="currencyFormat"/>
	</logic:present>
	&nbsp
	</td>
</tr>
<tr>
	<td>
		<bean:message key="totalIndirectCost.label" />
	</td>
	<td >
	<logic:present name="budget" property="totalIndirectCost" >
		<%--<bean:write name="budget" property ="totalIndirectCost" />--%>
		<util:formatString name="budget" property="totalIndirectCost" formatType="currencyFormat"/>		
	</logic:present>
	&nbsp
	</td>
</tr>
<tr>
	<td width="35%">
		<bean:message key="totalCost.label" />
	</td>
	<td >
	<logic:present name="budget" property="totalCost" >
		<%--<bean:write name="budget" property ="totalCost" />--%>
		<util:formatString name="budget" property="totalCost" formatType="currencyFormat"/>
		
	</logic:present>
	&nbsp
	</td>
</tr>
<tr>
	<td>
		<bean:message key="costSharingAmount.label" />
	</td>
	<td >
	<logic:present name="budget" property="costSharingAmount" >
		<%--<bean:write name="budget" property="costSharingAmount" />--%>
		<util:formatString name="budget" property="costSharingAmount" formatType="currencyFormat"/>
	</logic:present>
	&nbsp
	</td>
</tr>
<tr>
	<td>
		<bean:message key="underrecoveryAmount.label" />
	</td>
	<td >
	<logic:present name="budget" property="underRecoveryAmount" >
		<%--<bean:write name="budget" property="underRecoveryAmount" />--%>
		<util:formatString name="budget" property="underRecoveryAmount" formatType="currencyFormat"/>
	</logic:present>
	&nbsp
	</td>
</tr>
<tr>
	<td>
		<bean:message key="residualFunds.label" />
	</td>
	<td >	
	<logic:present name="budget" property="residualFunds" >
			<%--<bean:write name="budget" property ="residualFunds" />--%>
		<util:formatString name="budget" property="residualFunds" formatType="currencyFormat"/>
	</logic:present>
	&nbsp
	</td>
</tr>	
<tr>
	<td colspan='2'>
		<a href="javascript:openwin('<bean:write name='ctxtPath'/>/viewBudgetSummary.do?proposalNumber=<bean:write name="proposal" property="proposalNumber"/>&versionNumber=<bean:write name="budget" property="versionNumber"/>','BudgetSummary');">View Budget Summary </a>
		<%--<a href='<bean:write name='ctxtPath'/>/viewBudgetSummary.do?proposalNumber=<bean:write name="proposal" property="proposalNumber"/>&versionNumber=<bean:write name="budget" property="versionNumber"/>'>View Budget Summary </a>--%>
	</td>
</tr>	


</logic:equal>
</logic:present><!-- close logic:present budget exists -->
</table>
<!-- End budgetinformation -->
<!-- Begin narrative information -->
<p />
<logic:present  name="narratives"  property="moduleStatus" >
<table border = "0" width="100%">
<tr>
	<td bgcolor=#aa3333 colspan=2><b><font color=#fffff7>
		Narratives</font></b>
	</td>

</tr>
</table>
<table border = "0" width="100%">
<tr>
        <td width="35%"><b>Module Name</b></td>
        <td width="13%"><b>Module Status</b></td>
        <td width="52%"></td>
</tr>
<% int count =0; %>

<logic:iterate id= "index" name="narratives" property="moduleStatus">

<%
	Integer countInt = new Integer(count);
	String countString = countInt.toString();
%>

<tr bgcolor='<%=( (count) % 2 == 0 ? "#FBF7F7" : "#F7EEEE") %>'>

	<td>
              <logic:present name="narratives" property='<%= "moduleTitle[" + countString + "]" %>' >
		<bean:write name="narratives" property='<%= "moduleTitle[" + countString + "]" %>' />
              </logic:present>
	</td>

	<td>
		<bean:write name="narratives" property='<%= "moduleStatus[" + countString + "]" %>' />
	</td>


        <logic:equal name="narratives" property='<%= "narrativeViewRight[" + countString + "]" %>' value="1" >
        <td>
                <logic:present name="narratives" property='<%= "pdfModuleNumber[" + countString + "]" %>' >
                  <a href="<bean:write name='ctxtPath'/>/GetNarrativeDocument.do?proposalNumber=<bean:write name="proposal" property="proposalNumber" />&moduleNumber=<bean:write name="narratives" property='<%= "pdfModuleNumber[" + countString + "]" %>' />&documentType=PDF">
                    <u>View PDF</u>
                  </a>
		  &nbsp;&nbsp
                </logic:present>
                
                <logic:present name="narratives" property='<%= "sourceModuleNumber[" + countString + "]" %>'>
                    <a href="<bean:write name='ctxtPath'/>/GetNarrativeDocument.do?proposalNumber=<bean:write name="proposal" property="proposalNumber" />&moduleNumber=<bean:write name="narratives" property='<%= "sourceModuleNumber[" + countString + "]" %>' />&documentType=WORD">
                      <u>View Word</u>
                    </a>
                </logic:present>
                
        </td>
        </logic:equal>
        <logic:equal name="narratives" property='<%= "narrativeViewRight[" + countString + "]" %>' value="0" >
          <td>
  		  &nbsp;&nbsp
          </td>
        </logic:equal>

</tr>

<% count++; %>

</logic:iterate>
</table>
</logic:present><!-- close logic:present tag for narratives present -->
<!-- end narrative information -->
</logic:present><!-- close logic:present tag for proposalNumber present -->
</body>
</html:html>