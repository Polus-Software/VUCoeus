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
			  <table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
				  <td colspan="6" height="5"></td>
				</tr>
				<tr>
				  <td width="91" height="30" bgcolor="#FBF7F7">
					<div align="left">&nbsp;<bean:message key="viewDisclosureDetails.label.personName"/> </div>
				  </td>
				  <td height="30" width="3" bgcolor="#FBF7F7"><div align="left">:</div></td>
				  <td height="30" width="207" bgcolor="#FBF7F7">
					<div align="left"><b>&nbsp;<bean:write name="personName" scope='request'/></b></div>
				  </td>
				  <td height="30" width="97" bgcolor="#FBF7F7">
					<div align="left"> <bean:message key="viewDisclosureDetails.label.entityName"/> </div>
				  </td>
				  <td height="30" width="3" bgcolor="#FBF7F7"><div align="left">:</div></td>
				  <td height="30" width="245" bgcolor="#FBF7F7">
					<div align="left"><b>&nbsp;<coeusUtils:formatOutput name="disclosureInfo" property="entityName" defaultValue="&nbsp;" scope="request" />  </b>
					</div>
				  </td>
				</tr>
				<tr>
				  <td width="91" height="30" bgcolor="#F7EEEE">
					<div align="left">&nbsp;<bean:message key="viewDisclosureDetails.label.conflictStatus"/></div>
				  </td>
				  <td height="30" width="3" bgcolor="#F7EEEE"><div align="left">:</div></td>
				  <td height="30" width="207" bgcolor="#F7EEEE">
					<div align="left"><b>&nbsp;<coeusUtils:formatOutput name="disclosureInfo" property="conflictStatus" defaultValue="&nbsp;" scope="request" /> </b>
					</div>
				  </td>
				  <td height="30" width="97" bgcolor="#F7EEEE">
					<div align="left"> <bean:message key="viewDisclosureDetails.label.reviewdBy"/> </div>
				  </td>
				  <td height="30" width="3" bgcolor="#F7EEEE"><div align="left">:</div></td>
				  <td height="30" width="245" bgcolor="#F7EEEE" >
					<div align="left"><b>&nbsp;<coeusUtils:formatOutput name="disclosureInfo" property="reviewer" defaultValue="&nbsp;" scope="request" /> </b>
					</div>
				  </td>
				</tr>
				<tr>
				  <td width="91" height="40" bgcolor="#FBF7F7" >
					<div align="left">&nbsp;<bean:message key="viewDisclosureDetails.label.desc"/></div>
				  </td>
				  <td width="3" height="40" bgcolor="#FBF7F7" ><div align="left">:&nbsp;</div></td>

				  <td width="207" height="40" bgcolor="#FBF7F7">
			      <%String description = "";
				String desc = "";
				description = disclosureInfo.getDesc();
				if ( (description != null ) && (description != "") ) {
				  desc = URLEncoder.encode(description);
				}
				if (desc.length() > 100 ) {%>
			      <bean:define id="stringOne" value="<%=disclosureInfo.getDesc().substring(0,100)%>"/>
			      <div align="left"> <b>
				<bean:write name="stringOne"/> <br>
				<a href="JavaScript:openwinDesc('CoeusDisclosureDescription.jsp?desc=<%= desc%>');">
				  <img src="<bean:write name='ctxtPath'/>/images/more.gif" width="22" height="20" border="0">
				</a>
			      </b> </div>
			      <%}else { %>
			      <div>
					<div align="left"><b>&nbsp;
					<bean:write name="disclosureInfo" property="desc" />
					<%--<coeusUtils:formatOutput name="disclosureInfo" property="desc" defaultValue="&nbsp;" />--%>
					</b></div>
			      </div>
			      <%}%>
			    </td>

				  <%--
				  <td width="207" height="40" bgcolor="#FBF7F7">
					<div align="left"><b>&nbsp; <coeusUtils:formatOutput name="disclosureInfo" property="desc" defaultValue="&nbsp;" scope="request" /> </b></div>
				  </td>
				  --%>

				  <td width="97" height="40" bgcolor="#FBF7F7">&nbsp;</td>
				  <td width="3" height="40" bgcolor="#FBF7F7">&nbsp;</td>
				  <td width="245" height="40" bgcolor="#FBF7F7">&nbsp;</td>
				</tr>


				<tr>
				  <td width="91" height="30" bgcolor="#FBF7F7">
                            <div align="left">&nbsp; <bean:message key="viewCOIDisclosureDetails.label.disclosureno"/> </div>
				  </td>
				  <td height="30" width="3" bgcolor="#FBF7F7"><div align="left">:</div></td>
				  <td height="30" width="207" bgcolor="#FBF7F7">
					<div align="left"><b>&nbsp; <bean:write name="disclosureNo" scope='request'/></b></div>
				  </td>
				  <td height="30" width="97" bgcolor="#FBF7F7">
                            <div align="left">&nbsp;<bean:message key="viewCOIDisclosureDetails.label.title"/> </div>
				  </td>
				  <td height="30" width="3" bgcolor="#FBF7F7"><div align="left">:</div></td>
				  <td height="30" width="245" bgcolor="#FBF7F7">
                            <div align="left"><b> <coeusUtils:formatOutput name="discHeaderAwPrInfo" property="title" defaultValue="&nbsp;" scope="request" /> </b>&nbsp;</div>
					</div>
				  </td>
				</tr>


				<%--
                        <tr>
                          <td width="100" height="20" bgcolor="#FBF7F7">
                            <div align="left">&nbsp; <bean:message key="viewCOIDisclosureDetails.label.title"/> </div>
                          </td>
                          <td height="20" bgcolor="#FBF7F7" width="6">:</td>
                          <td height="20" bgcolor="#FBF7F7" width="10">&nbsp;</td>
                          <td height="20" bgcolor="#FBF7F7" width="176">
                            <div align="left"><b> <coeusUtils:formatOutput name="discHeaderAwPrInfo" property="title" defaultValue="&nbsp;" scope="request" /> </b>&nbsp;</div>
                          </td>
                          <td height="20" bgcolor="#FBF7F7" width="123">&nbsp;</td>
                          <td height="20" bgcolor="#FBF7F7" width="3">&nbsp;</td>
                          <td height="20" bgcolor="#FBF7F7" width="238">&nbsp;</td>
                        </tr>
				--%>


				<tr>
				  <td width="91">&nbsp;</td>
				  <td width="3">&nbsp;</td>
				  <td width="207">&nbsp;</td>
				  <td width="97">&nbsp;</td>
				  <td width="3">&nbsp;</td>
				  <td width="245">
					<div align="right"> </div>
				  </td>
				</tr>
			  </table>
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
				  <td colspan="3" height="5"></td>
				</tr>
				<tr bgcolor="#CC9999">
				  <td width="120" height="25">
					<div align="center"><font color="#FFFFFF">
					<bean:message key="viewDisclosureDetails.label.history.conflictStatus"/></font>
					</div>
				  </td>
				  <td width="85" height="25">
					<div align="center"><font color="#FFFFFF">
					<bean:message key="viewDisclosureDetails.label.history.reviewedBy"/></font>
					</div>
				  </td>
				  <td width="441" height="25">
					<div align="center"><font color="#FFFFFF">
					<bean:message key="viewDisclosureDetails.label.history.desc"/></font>
					</div>
				  </td>
				</tr>
				<logic:present  name="disclosureHistory"  scope="request">
					  <logic:iterate id="discHistoryDetail" name="disclosureHistory" type="edu.mit.coeus.coi.bean.DisclosureInfoHistoryBean">
				<tr bgcolor="#FBF7F7" valign="top">
				  <td width="120" height="25">
					<div align="left">
						<a href="JavaScript:openwin('<bean:write name='ctxtPath'/>/viewFinEntity.do?actionFrom=entityDetails&entityNo=<bean:write name='discHistoryDetail' property='entityNumber'  />&seqNo=<bean:write name='discHistoryDetail' property='sequenceNumber' />' , 'EntityDetails');"  method="POST" >
								<coeusUtils:formatOutput name="discHistoryDetail" property="conflictStatus" defaultValue="&nbsp;" />
					   </a>
					</div>
				  </td>
				  <td width="85" height="25" align="center" bgcolor="#FBF7F7">
					<div >&nbsp;<coeusUtils:formatOutput name="discHistoryDetail" property="reviewer" defaultValue="&nbsp;" /> </div>
				  </td>



				  <td  width="441" height="25">
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
				  <td width="240" height="40">&nbsp;</td>
				  <td colspan="2" height="40">
					<div align="right">
					    <a href="JavaScript:history.back();"> <img src="<bean:write name="ctxtPath" />/images/goback.gif" width="55" height="22" border="0">
					    </a>&nbsp; &nbsp; &nbsp;
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