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
<jsp:useBean  id="disclosureNo" 	scope="request" class="java.lang.String" />

			  <table width="100%" border="0" cellspacing="0" cellpadding="5">
				<tr bgcolor="#FBF7F7" valign="top">
					<td width="105" height="25">
					<div align="left"><bean:message key="viewDisclosureDetails.label.personName"/> </div>
					</td>
					<td width="6" height="25">
						<div align="left">:</div>
					</td>
					<td width="202" height="25">
					<div align="left"><b><bean:write name="personName" scope='request'/></b></div>
					</td>
					<td width="114" height="25">
					<div align="left"> <bean:message key="viewDisclosureDetails.label.entityName"/> </div>
					</td>
					<td width="6" height="25">
						<div align="left">:</div>
					</td>
					<td width="362" height="25">
					<div align="left"><b><coeusUtils:formatOutput name="disclosureInfo" property="entityName" defaultValue="&nbsp;" scope="request" />  </b>
					</div>
					</td>
				</tr>

				<tr bgcolor="#F7EEEE" valign="top">
					<td width="105" height="25">
						<div align="left"><bean:message key="viewDisclosureDetails.label.conflictStatus"/></div>
					</td>
					<td width="6" height="25">
						<div align="left">:</div>
					</td>
					<td width="202" height="25">
						<div align="left"><b><coeusUtils:formatOutput name="disclosureInfo" property="conflictStatus" defaultValue="&nbsp;" scope="request" /> </b>
					</div>
					</td>
					<td width="114" height="25">
						<div align="left"> <bean:message key="viewDisclosureDetails.label.reviewdBy"/> </div>
					</td>
					<td width="6" height="25">
						<div align="left">:</div>
					</td>
					<td width="362" height="25">
						<div align="left"><b><coeusUtils:formatOutput name="disclosureInfo" property="reviewer" defaultValue="&nbsp;" scope="request" /> </b>
					</div>
					</td>
				</tr>


				<tr bgcolor="#FBF7F7" valign="top">
					<td width="105" height="25">
						<div align="left"><bean:message key="viewDisclosureDetails.label.desc"/></div>
					</td>
					<td width="6" height="25">
						<div align="left">:</div>
					</td>
					<td  height="25" colspan="4">
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
						<div align="left"><b>
						<bean:write name="disclosureInfo" property="desc" />

						</b></div>
				      </div>
				      <%}%>
						</td>
					</tr>
				<tr bgcolor="#F7EEEE" valign="top">
					<td width="105" height="25">
                            			<div align="left"> <bean:message key="viewCOIDisclosureDetails.label.disclosureno"/> </div>
					</td>
					<td width="6" height="25">
						<div align="left">:</div>
					</td>
					<td width="202" height="25">
						<div align="left"><b> <bean:write name="disclosureNo" scope='request'/></b></div>
					</td>
					<td width="114" height="25">
                            			<div align="left"><bean:message key="viewCOIDisclosureDetails.label.title"/> </div>
					</td>
					<td width="6" height="25">
						<div align="left">:</div>
					</td>
					<td width="362" height="25">
                            			<div align="left"><b> <coeusUtils:formatOutput name="discHeaderAwPrInfo" property="title" defaultValue="&nbsp;" scope="request" /> </b>&nbsp;</div>
					</td>
				</tr>


			  </table>
