<%--
/*
 * @(#)ApprovalRoutingMaps
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  coeus-dev-team
 */

  Approval routing maps and status in a single page
--%>

<%@page errorPage = "ErrorPage.jsp" %>
<%--
	All maps in left pane
--%>
<%@ taglib uri="/WEB-INF/struts-html.tld"     prefix="html" %>
<%@ taglib uri= '/WEB-INF/struts-bean.tld'     prefix='bean' %>
<%@ taglib uri= '/WEB-INF/struts-logic.tld'     prefix='logic' %>
<%@ taglib uri="/WEB-INF/session.tld" prefix="sess" %>
<%@ include file="CoeusContextPath.jsp"  %>

<jsp:useBean id="ApprovalMaps" scope="session" class="java.util.Vector" />
<jsp:useBean id="ApprovalStatus" scope="request" class="java.util.Vector" />
<jsp:useBean id="proposal" scope="request" class="edu.mit.coeus.propdev.bean.web.ProposalBean" />

<%--
the size of the collection of Maps to iterate .
--%>
<bean:size id="totalApprovalMaps" name="ApprovalMaps" />

<html:html>

<!--
  Display proposal summary
-->
    <table border="0" cellpadding="0" cellspacing="0" align="top" width="680">
    	<tr>
    	<td colspan="2" height="2">&nbsp;</td>
    	</tr>
	   <tr bgcolor="FFFFFF">
	    	<td colspan="2">
	    		<a href='<bean:write name="ctxtPath"/>/displayProposal.do?proposalNumber=<bean:write name="proposal" property="proposalNumber" />'>
	    		<img src='<bean:write name="ctxtPath" />/images/backpropinfo.gif' border="0">
	    		</a>

	    	</td>
	    </tr>
      <tr bgcolor="#CCCCCC" >
	  <td with="20%">
	    <bean:message key="title.label" />
	  </td>
<!-- CASE #748 Comment Begin -->
<%--
	  <td width="80%">
	    <logic:notEqual name="proposal" property="title" value="null">
	      <bean:write name="proposal" property="title" />
	    </logic:notEqual>
--%>
<!-- CASE #748 Comment End -->
<!-- CASE #748 Begin -->
	  <td width="80%">
	    <logic:present name="proposal" property="title">
	      <bean:write name="proposal" property="title" />
	    </logic:present>
	  </td>
<!-- CASE #748 End -->
      </tr>
      <tr bgcolor="#CCCCCC" >
	  <td with="20%">
	    <bean:message key="proposalNumber.label" />
	  </td>
	  <td width="80%">
	    <bean:write name="proposal" property="proposalNumber" />
	  </td>
      </tr>
      <tr bgcolor="#CCCCCC" >
	  <td with="20%">
	    <bean:message key="sponsor.label" />
	  </td>
<!-- CASE #748 Comment Begin -->
<%--
	  <td width="80%">
	    <logic:notEqual name="proposal" property="sponsorCode" value="null">
	      <bean:write name="proposal" property="sponsorCode" />:
	      <bean:write name="proposal" property="sponsorName" />
	    </logic:notEqual>
	  </td>
--%>
<!-- CASE #748 Comment End -->
<!-- CASE #748 Begin -->
	  <td width="80%">
	    <logic:present name="proposal" property="sponsorCode">
	      <bean:write name="proposal" property="sponsorCode" />:
	      <bean:write name="proposal" property="sponsorName" />
	    </logic:present>
	  </td>
<!-- CASE #748 End -->
      </tr>
      <tr bgcolor="#CCCCCC" >
	  <td with="20%">
	    <bean:message key="principalInvestigator.label" />
	  </td>
	  <td width="80%" valign="top">
	    <bean:write name="proposal" property="principalInvestigator" />
	  </td>
      </tr>
    </table>
    <br>

<%--
  Display maps in LHS
--%>
<table width="300" height="100%" border="1" cellspacing="0" cellpadding="2" align="left">
  <tr>
    <td bgcolor="#aa3333">
      <b>
	  <font color=#fffff7>
          <bean:message key="mapsTree.title"/>
        </font>
	</b>
    </td>
  </tr>
  <logic:present name="ApprovalMaps"  scope="session" >
    <logic:iterate id="Maps"  name="ApprovalMaps" type="edu.mit.coeus.propdev.bean.web.MesgApprMapsDetailsBean">
      <tr>
  	  <td>

	    <% for(int i=0; i<((Maps.getLevelNumber()- 1) * 2 ); i++) { %>
		   &nbsp;&nbsp;
	    <% } %>
            <img src="<bean:write name="ctxtPath"/>/images/<bean:write name="Maps" property="bitMap"/>" width="15">
          <a href="<bean:write name="ctxtPath"/>/MessageApprovalAction.do?action=status&proposalNumber=<bean:write name="Maps" property="proposalNumber"/>&mapId=<bean:write name="Maps" property="mapId"/>">
          <bean:write name="Maps" property="description" />
	  </td>
      </tr>
    </logic:iterate>
  </logic:present>
</table>

<%--
  Display status in RHS
--%>

<table width="380" height="100%" border="1" cellspacing="0" cellpadding="2">
  <tr>
    <td bgcolor="#aa3333" colspan=6>
      <b>
        <font color=#fffff7>
          <bean:message key="statusTree.stopMessage"/>
          <bean:message key="statusTree.keyQuotes"/>
	    <%= request.getAttribute("stopsIn") %>
          <bean:message key="statusTree.keyQuotes"/>
        </font>
      </b>
    </td>
  </tr>
  <logic:present name="ApprovalStatus"  scope="request" >
    <logic:iterate id="Status"  name="ApprovalStatus" type="edu.mit.coeus.propdev.bean.web.MesgApprStatusDetailsBean">
      <logic:greaterThan name="Status" property="sequentialStopNumber" value="0">
	  <tr>
          <td colspan=6>
            <font color=blue>
              <bean:message key="statusTree.stopTitle"/>
              <bean:write name="Status" property="sequentialStopNumber" />
            </font>
          </td>
        </tr>
      </logic:greaterThan>
      <tr>
        <% if((Status.getIndexLevel() - 1)  > 0) { %>
          <td>
	      &nbsp;
	    </td>
          <td>
            <img src="<bean:write name="ctxtPath"/>/images/<bean:write name="Status" property="approverImage"/>">
	    </td>
        <% }else { %>
          <td>
            <img src="<bean:write name="ctxtPath"/>/images/<bean:write name="Status" property="approverImage"/>">
	    </td>
          <td>
	      &nbsp;
	    </td>
        <% } %>
        <td>
          <% for(int i=0; i<((Status.getIndexLevel()- 1) ); i++) { %>
	           &nbsp;&nbsp;&nbsp;&nbsp;
	    <% } %>
          <img src="<bean:write name="ctxtPath"/>/images/<bean:write name="Status" property="statusImage"/>">
          <bean:write name="Status" property="userName" />
   	  </td>
        <td colspan=2>
          <bean:write name="Status" property="approvalStatus" />
   	  </td>
        <logic:equal name="Status" property="bypassApproval" value="1">
          <td>
	      <a href="<bean:write name="ctxtPath"/>/RefreshProposalAction.do?actionMode=ByPass&rowNumber=<bean:write name="Status" property="rowNumber"/>">
            <bean:message key="bypassRight.title" />
     	    </td>
        </logic:equal>
        <logic:notEqual name="Status" property="bypassApproval" value="1">
          <td>
            &nbsp;
     	    </td>
        </logic:notEqual>
      </tr>
    </logic:iterate>
  </logic:present>
</table>
<br><br>

<!-- CASE #599 Begin -->
<logic:equal name="proposal" property="toBeSubmitted" value="true">
<div>
<font color="red" size="2">
<bean:message key="approvalRouting.proposalApproved" />
</font>
</div>
</logic:equal>
<br><br>
<!-- CASE #500 End -->

<%--
  Display Legend
--%>

<table width="272" height="100%" border="0" cellspacing="0" cellpadding="0" align="left" bgcolor="#CCCCCC">
  <tr>
    <td>
      <b> <bean:message key="legend.label"/> </b>
    </td>
  </tr>
</table>
<table width="418" height="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#CCCCCC">
  <tr>
    <td>
      &nbsp;
    </td>
  </tr>
</table> <br>
<table border="0" cellspacing="0" cellpadding="0" align="left">
  <tr>
    <td>
      <img src="<bean:write name="ctxtPath"/>/images/approved.gif" width="15"> &nbsp;
      <bean:message key="approved.gif.label"/>
    </td>
    <td>
      <img src="<bean:write name="ctxtPath"/>/images/reject.gif" width="15"> &nbsp;
      <bean:message key="reject.gif.label"/>
    </td>
    <td>
      <img src="<bean:write name="ctxtPath"/>/images/tobesub.gif" width="15"> &nbsp;
      <bean:message key="tobesub.gif.label"/>
    </td>
    <td>
      <img src="<bean:write name="ctxtPath"/>/images/inprogress.gif" width="15"> &nbsp;
      <bean:message key="inprogress.gif.label"/>
    </td>
    <td>
      <img src="<bean:write name="ctxtPath"/>/images/primary.gif" width="15"> &nbsp;
      <bean:message key="primary.gif.label"/>
    </td>
    <td>
      <img src="<bean:write name="ctxtPath"/>/images/alternate.gif" width="15"> &nbsp;
      <bean:message key="alternate.gif.label"/>
    </td>
  </tr>

  <tr>
    <td>
      <img src="<bean:write name="ctxtPath"/>/images/bypass.gif" width="15"> &nbsp;
      <bean:message key="bypass.gif.label"/>
    </td>
    <td>
      <img src="<bean:write name="ctxtPath"/>/images/altappr.gif" width="15"> &nbsp;
      <bean:message key="altappr.gif.label"/>
    </td>
    <td>
      <img src="<bean:write name="ctxtPath"/>/images/waiting.gif" width="15"> &nbsp;
      <bean:message key="waiting.gif.label"/>
    </td>
    <td>
      <img src="<bean:write name="ctxtPath"/>/images/pass.gif" width="15">&nbsp;
      <bean:message key="pass.gif.label"/>
    </td>
    <td>
      <img src="<bean:write name="ctxtPath"/>/images/delegate.gif" width="15">&nbsp;
      <bean:message key="delegate.gif.label"/>
    </td>
  </tr>
      <tr>
      	<td colspan="5" height="1"> &nbsp; </td>
      </tr>

    <tr>
    	<td colspan="5">
    		<a href='<bean:write name="ctxtPath"/>/displayProposal.do?proposalNumber=<bean:write name="proposal" property="proposalNumber" />'>
    		<img src='<bean:write name="ctxtPath" />/images/backpropinfo.gif' border="0">
    		</a>
    	</td>
    </tr>
</table>
</html:html>
