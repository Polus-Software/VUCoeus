<%--
/*
 * @(#) AnnualDisclosuresMain.jsp 1.0 2002/06/05
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  Geo Thomas.
 */
--%>

<%-- This JSP file is for listing the COI Disclosures of the user--%>
<%@page import="edu.mit.coeus.coi.bean.ComboBoxBean,
		org.apache.struts.action.Action,
                java.net.URLEncoder" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" 	prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" 	prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" 	prefix="logic" %>
<%@ taglib uri="/WEB-INF/privilege.tld" prefix="priv" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"	prefix="coeusUtils" %>
<%@ include file="CoeusContextPath.jsp"  %>

<jsp:useBean  id="action" 			scope="session" class="java.lang.String" />
<jsp:useBean  id="disclosureNo" 			scope="session" class="java.lang.String" />
<jsp:useBean  id="discHeaderAwPrInfo" 	scope="session" class="edu.mit.coeus.coi.bean.DisclosureHeaderBean" />
<jsp:useBean  id="moduleCode" 				scope="session" class="java.lang.String" />
<jsp:useBean  id="disclosureHeader" 		scope="session" class="edu.mit.coeus.coi.bean.DisclosureHeaderBean" />
<jsp:useBean  id="collCOIDisclStatus"		scope="session" class="java.util.LinkedList" />
<jsp:useBean  id="collCOIReviewer"		scope="session" class="java.util.LinkedList" />
<jsp:useBean id = "userprivilege" class = "java.lang.String" scope="session"/>

<priv:hasOSPRight name="hasOspRightToEdit" value="<%=Integer.parseInt(userprivilege)%>"> 
	<jsp:useBean id="hasMaintainCOI" class="java.lang.String" scope="session"/>
</priv:hasOSPRight>
<!-- CASE #1374 Show disclosure details section only if user has Maintain COI Right -->
	<logic:present name="hasMaintainCOI" scope="session">
	<!-- Disclosure Details -->
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
              <td height="5">&nbsp;</td>
              </tr>
                <tr><td background="<bean:write name="ctxtPath"/>/images/tabback.gif">
                    <table border=0 cellpadding=0 cellspacing=0>
                      <tr><td>
				<img src="<bean:write name="ctxtPath"/>/images/discdet.gif" width="120" height="24">
			    </td></tr>
                    </table>
                 </td></tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="0">

                <tr><td colspan="7" height="5">
                </td>
                </tr>
                 <logic:present name="<%=Action.ERROR_KEY%>">
                 
			 <tr>
			 <td width="7"></td>
			 <td colspan="6">
			 <font color="red">
			 <html:errors property="invalidDisclosureStatus"/><br><br>
			 <html:errors property="initial disclosure type invalid"/>
			 </font>
			 </td>
			 </tr>
                 </logic:present> 
                <tr>
                <td colspan="7" height="5">&nbsp;</td>
                </tr>                 
                <tr  bgcolor="#FBF7F7">
		<td width="5" height="30" >&nbsp;</td>
		<td width="100"  align="left" >
                    <div><bean:message key="editCOIDisclosure.label.personName"/></div>
                  </td>
                  <td width="6"  ><div>:</div></td>
                  <td width="176"   align="left" >
                    <div><b><bean:write name="disclosureHeader" scope="session" property="name"/></b></div>
                  </td>
                  <td width="123"   align="left" >
                    <div><bean:message key="editCOIDisclosure.label.disclosureNum"/></div>
                  </td>
                  <td width="3"  ><div>:</div></td>
                  <td width="238"   align="left" >
                    <div><b><bean:write name="disclosureHeader" scope="session" property="disclosureNo"/></b> </div>
                  </td>
                </tr>
                <tr bgcolor="#F7EEEE" >
                <td width="5" height="30" bcolor=#F7EEEE">&nbsp;</td>
                  <td width="100"  align="left" >
                    <div><bean:message key="editCOIDisclosure.label.appliesTo"/></div>
                  </td>
                  <td width="6" ><div>:</div></td>
                  <td width="176"  align="left" >
                    <div><b><bean:write name="disclosureHeader" scope="session" property="appliesTo"/>&nbsp;</b></div>
                  </td>
                  <td width="123"  align="left" >
                    <div>
			<logic:equal name="moduleCode" scope="session" value="1" >
				<bean:message key="editCOIDisclosure.label.awardNum"/>
			</logic:equal>
			<logic:notEqual name="moduleCode" scope="session" value="1">
				<bean:message key="editCOIDisclosure.label.proposalNo"/>
			</logic:notEqual>
			  &nbsp;
			</div>
                  </td>
                  <td width="3" ><div>:</div></td>
                  <td width="238"  align="left" >
                    <div><b><bean:write name="disclosureHeader" scope="session" property="keyNumber"/></b> </div>
                  </td>
                </tr>
                <!-- CASE #1374 Comment Begin -->
                <%--
                <tr >
                <td width="5" height="30" bgcolor=#FBF7F7">&nbsp;</td>
                  <td width="100"  bgcolor="#FBF7F7" align="left" >
                   <div> <bean:message key="editCOIDisclosure.label.title"/>
                    &nbsp;	
		</div></td>
                  <td  bgcolor="#FBF7F7" width="6" ><div>:</div></td>
                  <td  colspan='4' bgcolor="#FBF7F7" align="left" >
                    <div><b><bean:write name="discHeaderAwPrInfo" scope="session" property="title"/></b>
                    &nbsp;</div>
                  </td>
                </tr>
                --%>
                <!-- CASE #1374 Comment End -->
                <tr bgcolor="#FBF7F7" >
                	<td width="5" height="30" >&nbsp;</td>
                  <td width="100"  align="left" >
                    <div><bean:message key="editCOIDisclosure.label.account"/></div>
                  </td>
                  <td width="6" ><div>:</div></td>
                  <td width="176"  align="left" >
                    <div><b><bean:write name="discHeaderAwPrInfo" scope="session" property="account"/></b>&nbsp;</div>
                  </td>
                  
                  
                  <td  width="123"><bean:message key="viewCOIDisclosureDetails.label.sponsor"/></td>
                  <td  width="3"><div>:</div></td>
                  <td  width="238" >
                  	<b><coeusUtils:formatOutput name="frmAddCOIDisclosure" scope="session" property="sponsorName" /></b>
                  </td>                  



                </tr>
<!-- CASE #1374 Close logic:present tag-->
</logic:present>
