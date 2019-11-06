
 <%--
/*
 * @(#)COIDisclosureDetailsContent.jsp	1.0 05/15/2002   04:36:12 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  RaYaKu
 */
--%>
<%--
A view component that displays the Details of COI disclosure like certification,
award/proposal information and page output is  in HNC Template in runtime.
--%>
<%@ page import="java.net.URLEncoder"%>
<%@page errorPage="ErrorPage.jsp" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" 	prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" 	prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" 	prefix="logic" %>
<%@ taglib uri="/WEB-INF/privilege.tld" prefix="priv" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"	prefix="coeusUtils" %>
<%@ include file="CoeusContextPath.jsp"  %>
<%	System.out.println("begin COIDisclosureDetailsContent.jsp");	%>
<jsp:useBean  id="action" 			scope="request" class="java.lang.String" />
<jsp:useBean  id="disclosureNo" 			scope="request" class="java.lang.String" />
<jsp:useBean  id="discHeaderAwPrInfo" 	scope="request" class="edu.mit.coeus.coi.bean.DisclosureHeaderBean" />
<jsp:useBean  id="moduleCode" 				scope="request" class="java.lang.String" />
<jsp:useBean  id="disclosureHeader" 		scope="request" class="edu.mit.coeus.coi.bean.DisclosureHeaderBean" />
<jsp:useBean id = "userprivilege" class = "java.lang.String" scope="session"/>

<%-- CASE#158 Begin --%>
<%-- CASE #212 Comment Begin
<jsp:useBean id = "loggedinpersonid" class = "java.lang.String" scope="session"/>
<jsp:useBean id = "personInfo" class = "edu.mit.coeus.coi.bean.PersonInfoBean" scope="session"/>
CASE # 212 Comment End --%>
<%-- CASE#158 End --%>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td background="<bean:write name="ctxtPath"/>/images/separator.gif" width="1">
            <image src="<bean:write name="ctxtPath"/>/images/separator.gif" width="1">
        </td>
        <td width="645">
          <table width="100%" border="0" cellspacing="0" align="left" cellpadding="0">
            <tr>
            <td height="5"></td>
          </tr>
          <tr bgcolor="#cccccc">
            <td height="23" class="header"> &nbsp;
				<bean:message key="viewCOIDisclosureDetails.header" />
				<%--<bean:write name="disclosureNo" scope="request"/>--%>
				<bean:write name="disclosureHeader" property="name" scope="request"/>
		</td>
          </tr>
          <tr>
            <td >
            	<!-- CASE #653 Add 5px cellpadding-->
             <table width="100%" border="0" cellspacing="0" cellpadding="5">
 		<tr>             
<!-- CASE #1374 Begin -->
				<td width="110">
				<logic:equal name="moduleCode" value="1">
					<bean:message key="addCOIDisclosure.label.awardTitle"/>
				</logic:equal>
				<logic:notEqual name="moduleCode" value="1">
					 <bean:message key="addCOIDisclosure.label.proposalTitle"/>
				</logic:notEqual>
				&nbsp;:&nbsp;
				</td>
				<td width="390">
				<b><bean:write name="discHeaderAwPrInfo" property="title"/></b>
			  	</td>
			  <td width="157">
<!-- CASE #1374 End -->                
<!-- CASE #653 Begin -->
		<logic:present name="noFinEntAnsReqExpl" >
			<bean:message key="viewCOIDisclosureDetails.noFinEntAnsReqExpl" />
		</logic:present>
<!-- CASE #653 End -->
                    <div align="right"><a href="JavaScript:history.back();"><img src="<bean:write name="ctxtPath" />/images/goback.gif" width="55" height="22" border="0"></a>&nbsp;

<!-- CASE # 212 Begin -->
<!-- If "showEditButton" attribute has been set in request by action class, then show the edit button. -->
	<logic:present name="showEditButton">
		<a href="JavaScript:window.location='<bean:write name="ctxtPath" />/viewCOIDisclosureDetails.do?action=edit&disclNo=<%= disclosureNo %>';" method="POST"><img src="<bean:write name='ctxtPath'/>/images/edit.gif" width="42" height="22" border="0"></a>
	</logic:present>
<!-- CASE #212 End. -->
                      &nbsp; &nbsp; &nbsp; </div>
                  </td>
                </tr> 
			<!-- CASE #1374 Begin -->
		
				
			<tr>
			<logic:equal name="frmAddCOIDisclosure" scope="session" property="disclosureTypeCode" value="3">
				<td >
				<bean:message key="addCOIDisclosure.label.proposalType"/> :
				</td>
				<td colspan="2">
				<b><bean:write name="frmAddCOIDisclosure" property="proposalType"/></b>
				</td>	
			</logic:equal>
			<logic:notEqual name="frmAddCOIDisclosure" scope="session" property="disclosureTypeCode" value="3">
				<td colspan="3">&nbsp;</td>
			</logic:notEqual>					
			
			</tr>	
			<tr>			
				<td>
				<bean:message key="addCOIDisclosure.label.sponsor"/>:
				</td>	
					<td colspan="2">
						<b><bean:write name="discHeaderAwPrInfo" property="sponsor"/>				</b>
					</td>	
			</tr>
				
			<!-- CASE #1374 End -->

              </table>
              <!-- CASE #1374 Comment Begin -->
              <!-- Move to bottom of page.  Show only if user has select person right -->
<%--              
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
              
                <tr>
                  <td background="<bean:write name="ctxtPath" />/images/tabback.gif">
                    <table border=0 cellpadding=0 cellspacing=0>
                      <tr>
                        <td> <img src="<bean:write name="ctxtPath" />/images/disclosure.gif" width="120" height="24"></td>
                      </tr>
                    </table>
                  </td>
                </tr>
              </table>

              <!-- show Desclosure Details -->
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td colspan="6" height="5"></td>
                </tr>
				<tr valign="top">
                  <td width="100" height="20" bgcolor="#FBF7F7">
                    <div align="left">&nbsp; <bean:message key="viewCOIDisclosureDetails.label.personName"/> </div>
                   </td>
                  <td width="6" height="20" bgcolor="#FBF7F7"><div>:</div></td>
                  <td width="176" height="20" bgcolor="#FBF7F7">
                    <div align="left"><b> <coeusUtils:formatOutput name="disclosureHeader" property="name" defaultValue="&nbsp;" scope="request"/>  </b></div>
                  </td>
                  <td width="123" height="20" bgcolor="#FBF7F7">
                    <div align="left">&nbsp; <bean:message key="viewCOIDisclosureDetails.label.disclosureno"/> </div>
                  </td>
                  <td width="3" height="20" bgcolor="#FBF7F7"><div>:</div></td>
                  <td width="238" height="20" bgcolor="#FBF7F7">
                    <div align="left"><b> <coeusUtils:formatOutput name="disclosureHeader" property="disclosureNo" defaultValue="&nbsp;" scope="request" /> </b> </div>
                  </td>
                </tr>
		<tr bgcolor="#F7EEEE" valign="top">
                  <td width="100" height="20">
                    <div align="left">&nbsp; <bean:message key="viewCOIDisclosureDetails.label.appliesTo"/> </div>
                  </td>
                  <td width="6" height="20"><div>:</div></td>
                  <td width="176" height="20">
                    <div align="left"><b> <coeusUtils:formatOutput name="disclosureHeader" property="appliesTo" defaultValue="&nbsp;" scope="request" /> </b>&nbsp;</div>
                  </td>
                  <td width="123" height="20">
                    <div align="left">&nbsp;

                    <logic:notEqual name="moduleCode" value="1" >
		 	<bean:message key="viewCOIDisclosureDetails.label.proposalNo"/>
		</logic:notEqual>
		<logic:equal name="moduleCode" value="1">
				<bean:message key="viewCOIDisclosureDetails.label.awardno"/>
		</logic:equal>

					</div>
				  </td>
                  <td width="3" height="20"><div>:</div></td>
                  <td width="238" height="20">
                    <div align="left"><b> <coeusUtils:formatOutput name="disclosureHeader" property="keyNumber" defaultValue="&nbsp;" scope="request" /> </b>&nbsp;</div>
                  </td>
                </tr>
                <tr valign="top">
                  <td width="100" height="20" bgcolor="#FBF7F7">
                    <div align="left">&nbsp; <bean:message key="viewCOIDisclosureDetails.label.title"/> </div>
                  </td>
                  <td height="20" bgcolor="#FBF7F7" width="6"><div>:</div></td>
                  <td height="20"  colspan='4' bgcolor="#FBF7F7" >
                    <div align="left"><b> <coeusUtils:formatOutput name="discHeaderAwPrInfo" property="title" defaultValue="&nbsp;" scope="request" /> </b>&nbsp;</div>
                  </td>
                </tr>
                <tr bgcolor="#F7EEEE" valign="top">
                  <td width="100" height="20">
                    <div align="left">&nbsp; <bean:message key="viewCOIDisclosureDetails.label.account"/> </div>
                 </td>
                  <td width="6" height="20"><div>:</div></td>
                  <td width="176" height="20">
                    <div align="left"><b> <coeusUtils:formatOutput name="discHeaderAwPrInfo" property="account" defaultValue="&nbsp;" scope="request" /> </b>&nbsp;</div>
                  </td>
                  
                  <td height="20" width="123">&nbsp; <bean:message key="viewCOIDisclosureDetails.label.sponsor"/></td>
                  <td height="20" width="3"><div>:</div></td>
                  <%	System.out.println("discHeaderAwPrInfo.getSponsor(): "+discHeaderAwPrInfo.getSponsor());	%>
                  <td height="20" width="238" bgcolor="#F7EEEE">
                  	<b><coeusUtils:formatOutput name="discHeaderAwPrInfo" property="sponsor" scope="request"/></b>
                  </td>


                </tr>
                <tr valign="top">
                  <td width="100" height="20" bgcolor="#FBF7F7">
                    <div align="left">&nbsp; <bean:message key="viewCOIDisclosureDetails.label.disclosureType"/> </div>
                  </td>
                  <td width="6" height="20" bgcolor="#FBF7F7"><div>:</div></td>
                  <td width="176" height="20" bgcolor="#FBF7F7">
                    <div align="left"><b>
			<!-- show the disclosure type in label mode(select box) if user requesition type is not edit
			-->
		<logic:present name="disclosureHeader" property="disclType">
			<logic:equal name="disclosureHeader" property="disclType" value="I" >
						 <bean:message key="viewCOIDisclosureDetails.label.initial"/>
			</logic:equal>
			<logic:notEqual name="disclosureHeader" property="disclType" value="I" >
						  <bean:message key="viewCOIDisclosureDetails.label.annual"/>
			</logic:notEqual>
		</logic:present>
			</b> </div>
                  </td>


                  
                  <td width="123" height="20" bgcolor="#FBF7F7">
                    <div align="left">&nbsp; <bean:message key="viewCOIDisclosureDetails.label.status"/> </div>
                  </td>
                  <td width="3" height="20" bgcolor="#FBF7F7"><div>:</div></td>
                  <td width="238" height="20" bgcolor="#FBF7F7">
                    <div align="left"><b> <coeusUtils:formatOutput name="discHeaderAwPrInfo" property="status" defaultValue="&nbsp;" scope="request" /> </b>&nbsp;</div>
                  </td>



                </tr>
                <tr bgcolor="#F7EEEE" valign="top">
                  <td width="100" height="20">
                    <div align="left">&nbsp; <bean:message key="viewCOIDisclosureDetails.label.reviewedBy"/>  </div>
                  </td>
                  <td height="20" width="6">
                    <div align="left">: </div>
                  </td>
                  <td height="20" width="176" >
                    <div align="left"><b> <coeusUtils:formatOutput name="disclosureHeader" property="reviewer" defaultValue="&nbsp;" scope="request" /> </b>&nbsp;</div>
                  </td>
                  
                  <td width="123" height="20" >
                    <div align="left">&nbsp; <bean:message key="viewCOIDisclosureDetails.label.disclosureStatus"/>  </div>
                  </td>
                  <td width="3" height="20" ><div>:</div></td>
                  <td width="238" height="20" >
                    <div align="left"><b> <bean:write name="disclosureHeader" property="disclStatus" /> </b>&nbsp;</div>
                  </td>                  

                </tr>
              </table>
              <!-- End disclosure details -->
              --%>
              <!-- CASE #1374 Comment End -->
            </td>
          </tr>
          <tr>
            <td height="23">
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td background="<bean:write name="ctxtPath" />/images/tabback.gif">
                    <table border=0 cellpadding=0 cellspacing=0>
                      <tr>
                        <td> <img src="<bean:write name="ctxtPath" />/images/certification.gif" width="120" height="24"></td>
                      </tr>
                    </table>
                  </td>
                </tr>
              </table>

<jsp:include page="COIDisclosureDetailsContentInsert.jsp" />
