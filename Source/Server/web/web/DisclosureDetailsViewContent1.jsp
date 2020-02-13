<%--
/*
 * @(#)DisclosureDetailsViewContent.jsp 1.0 2002/05/20 16:00:23
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  RaYaKu
 */
--%>
<%--
A view component that displays the Details of choosen disclosure and its history,
Page output is included in DisclosureDetailsView.jsp in runtime.
--%>
<%@ page import="java.net.URLEncoder"%>
<%@page errorPage = "ErrorPage.jsp" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" 	prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" 	prefix="bean" %>
<%@ taglib uri="/WEB-INF/datetime.tld" 		prefix="dt" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" 	prefix="logic" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"	prefix="coeusUtils" %>
<%@ include file="CoeusContextPath.jsp"  %>
<jsp:useBean  id="personName" 	scope="request" class="java.lang.String" />
<jsp:useBean id="disclosureInfo"
		scope="request"
		class="edu.mit.coeus.coi.bean.DisclosureInfoDetailBean" />

<jsp:useBean  id="discHeaderAwPrInfo" 	scope="request" class="edu.mit.coeus.coi.bean.DisclosureHeaderBean" />
<jsp:useBean  id="disclosureNo" 			scope="request" class="java.lang.String" />
<%	System.out.println("begin DisclosureDetailsViewContent1.jsp");		%>

<table width="100%"  cellpadding="0" cellspacing="0" border="0">
  <tr>
	<td background="<bean:write name="ctxtPath"/>/images/separator.gif" width="1">
            <image src="<bean:write name="ctxtPath"/>/images/separator.gif" width="1">
        </td>
	<td >
	  <table width="100%" border="0" cellspacing="0" align="left" cellpadding="0">
		<tr>
		<td height="5"></td>
		  </tr>
		  <tr bgcolor="#cccccc">
			<td height="23" > &nbsp;<font face="Verdana, Arial, Helvetica, sans-serif" size="2"> <b>
					<bean:message key="viewDisclosureDetails.header1" />
					<bean:write name="disclosureNo" scope='request'/>
					<bean:message key="viewDisclosureDetails.header2" />
					<bean:write name="disclosureInfo" property="entityName" />
				</b></font>
			</td>
		  </tr>
		  <tr>
			<td height="23">
			  <table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
				  <td background="<bean:write name="ctxtPath" />/images/tabback.gif">
					<table border=0 cellpadding=0 cellspacing=0>
					  <tr>
						<td > <img src="<bean:write name="ctxtPath" />/images/finEntDetails.gif" width="144" height="24"></td>
					  </tr>
					</table>
				  </td>
				</tr>
			  </table>
			  <!-- Disclosure Details -->
<!-- insert DisclosureDetailsViewContentInsert.jsp -->
<jsp:include page="DisclosureDetailsViewContentInsert.jsp" />
<!-- end insert>

			  <!-- End of Disclosure details -->

			  <table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
				  <td background="<bean:write name="ctxtPath" />/images/tabback.gif">
					<table border=0 cellpadding=0 cellspacing=0>
					  <tr>
						<td> <img src="<bean:write name="ctxtPath" />/images/history.gif" width="120" height="24"></td>
					  </tr>
					</table>
				  </td>
				</tr>
			  </table>
			  <!-- Disclosure History -->
			  <table width="99%" border="0" cellspacing="1" cellpadding="5" align="right">
				<tr>
				  <td colspan="4" height="5"></td>
				</tr>
				<tr bgcolor="#CC9999">
				<!-- CASE #1400 Change column widths to add column for last updated -->
				  <td width="100" height="25">
					<div align="center"><font color="#FFFFFF">
					<bean:message key="viewDisclosureDetails.label.history.conflictStatus"/></font>
					</div>
				  </td>
				  <td width="80" height="25">
					<div align="center"><font color="#FFFFFF">
					<bean:message key="viewDisclosureDetails.label.history.reviewedBy"/></font>
					</div>
				  </td>
				  <td width="45">
					<div align="center"><font color="#FFFFFF">
					<bean:message key="viewDisclosureDetails.label.history.lastUpdate"/></font>
					</div>
				  <td width="300" height="25">
					<div align="center"><font color="#FFFFFF">
					<bean:message key="viewDisclosureDetails.label.history.desc"/></font>
					</div>
				  </td>
				</tr>
				<logic:present  name="disclosureHistory"  scope="request">
					  <logic:iterate id="discHistoryDetail" name="disclosureHistory" type="edu.mit.coeus.coi.bean.DisclosureInfoHistoryBean">
				<tr bgcolor="#FBF7F7" valign="top">
				  <td  height="25">
					<div align="left">
						<a href="JavaScript:openwin('<bean:write name='ctxtPath'/>/viewFinEntity.do?actionFrom=entityDetails&entityNo=<bean:write name='discHistoryDetail' property='entityNumber'  />&seqNo=<bean:write name='discHistoryDetail' property='sequenceNumber' />&entSeqNo=<bean:write name='discHistoryDetail' property='entitySequenceNumber' />' , 'EntityDetails');"  method="POST" >
								<coeusUtils:formatOutput name="discHistoryDetail" property="conflictStatus" defaultValue="&nbsp;" />
					   </a>
					</div>
				  </td>
				  <td  height="25" align="center" bgcolor="#FBF7F7">
					<div >&nbsp;<coeusUtils:formatOutput name="discHistoryDetail" property="reviewer" defaultValue="&nbsp;" /> </div>
				  </td>

				<!-- CASE #1400 Begin -->
				  <td  height="25" align="center" bgcolor="#FBF7F7">
					<div >&nbsp;<coeusUtils:formatDate name="discHistoryDetail" property="updatedDate" /> </div>
				  </td>
				<!-- CASE #1400 End -->
				  <td   height="25">
			      <%String hdescription = "";
				String hdesc = "";
				hdescription = discHistoryDetail.getDesc();
				if ( (hdescription != null ) && (hdescription != "") ) {
				  hdesc = URLEncoder.encode(hdescription);
				}
				if (hdesc.length() > 100 ) {%>
			      <bean:define id="stringhOne" value="<%=discHistoryDetail.getDesc().substring(0,100)%>"/>
			      <div align="left">
				<bean:write name="stringhOne"/> <br>
				<a href="JavaScript:openwinDesc('CoeusDisclosureDescription.jsp?desc=<%= hdesc%>');">
				  <img src="<bean:write name='ctxtPath'/>/images/more.gif" width="22" height="20" border="0">
				</a>
			      </div>
			      <%}else {%>
			      <div>
					<div align="left">&nbsp; <coeusUtils:formatOutput name="discHistoryDetail" property="desc" defaultValue="&nbsp;"  /> </div>
			      </div>
			      <%}%>
			    </td>

				  <%--
				  <td  height="25">
					<div align="left">&nbsp;<coeusUtils:formatOutput name="discHistoryDetail" property="desc" defaultValue="&nbsp;" /> </div>
				  </td>
				  --%>
				</tr>
					</logic:iterate>
				</logic:present>
				<tr>
				  <td height="40">&nbsp;</td>
				  <td colspan="3" height="40" >
					<div align="right">
					    <a href="JavaScript:history.back();"> <img src="<bean:write name="ctxtPath" />/images/goback.gif" width="55" height="22" border="0"></a>&nbsp; &nbsp; &nbsp;
					</div>
				  </td>
				</tr>
			  </table>
			  <!-- End of Disclosure history -->
			</td>
		  </tr>
  </table>
</td>
</tr>
</table>