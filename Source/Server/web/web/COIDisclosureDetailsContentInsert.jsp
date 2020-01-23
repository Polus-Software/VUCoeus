
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
<jsp:useBean  id="collCOIDisclosureInfo"	scope="request" class="java.util.Vector" />
<bean:size id="collCOIDisclosureInfoSize"   name="collCOIDisclosureInfo" />
<jsp:useBean  id="collCOICertDetails" 			scope="request" class="java.util.Vector" />
<jsp:useBean  id="discHeaderAwPrInfo" 	scope="request" class="edu.mit.coeus.coi.bean.DisclosureHeaderBean" />
<jsp:useBean  id="moduleCode" 				scope="request" class="java.lang.String" />
<jsp:useBean  id="disclosureHeader" 		scope="request" class="edu.mit.coeus.coi.bean.DisclosureHeaderBean" />
<jsp:useBean id = "userprivilege" class = "java.lang.String" scope="session"/>


<%-- CASE#158 Begin --%>
<%-- CASE # 212 Comment Begin
<jsp:useBean id = "loggedinpersonid" class = "java.lang.String" scope="session"/>
<jsp:useBean id = "personInfo" class = "edu.mit.coeus.coi.bean.PersonInfoBean" scope="session"/>
CASE #212 Comment End --%>
<%-- CASE#158 End --%>

              <!-- show COI Disclosures certificates information -->
              <table width="100%" border="0" cellspacing="1" cellpadding="5">
                <tr>
                  <td colspan="2" height="5"></td>
                </tr>
                <tr bgcolor="#CC9999">
                  <td width="457" height="25" bgcolor="#CC9999">
                    <div align="center"><font color="#FFFFFF"><bean:message key="viewCOIDisclosureDetails.label.question"/> </font></div>
                  </td>
                  <td width="128" height="25">
                    <div align="center"><font color="#FFFFFF"><bean:message key="viewCOIDisclosureDetails.label.answer"/>  </font></div>
                  </td>
                </tr>

            <logic:present  name="collCOICertDetails"  >
            <%	int count = 0;	%>
		  <logic:iterate id="certificateDetails" name="collCOICertDetails" type="edu.mit.coeus.coi.bean.CertificateDetailsBean">

                <tr bgcolor='<%=( (count) % 2 == 0 ? "#FBF7F7" : "#F7EEEE") %>'>
                  <td width="497" height="25">
                    <div align="justify"><font face="Verdana, Arial, Helvetica, sans-serif">
			<!-- CASE #1374 Add question label -->
			<b><bean:message key="viewCOIDisclosureDetails.questionLabel"/>&nbsp;
			<bean:write name="certificateDetails" property="label"/>:</b>
			<bean:write name="certificateDetails" property="question"/>
                    <a href='javascript:showQuestion("<bean:write name='ctxtPath' />/question.do?questionNo=<bean:write name='certificateDetails' property='code'/>",  "ae");'  method="POST">
		    	<bean:message key="financialEntityDetails.question.explanationLink" />
		   </a>
                    &nbsp;</div>
                  </td>
                  <td width="128" height="25" valign="top">
                    <div align="center"> <font face="Verdana, Arial, Helvetica, sans-serif">

                  <logic:present name="certificateDetails" property="answer" >
                    <logic:equal name="certificateDetails" property="answer" value="Y" >
                    	<bean:message key="viewCOIDisclosureDetails.label.yes"/>
                    </logic:equal>
		<!-- CASE # 322 Comment Begin -->
		<%--
                    <logic:notEqual name="certificateDetails" property="answer" value="Y" >
				<bean:message key="viewCOIDisclosureDetails.label.no"/>
                    </logic:notEqual>
                --%>
                <!-- CASE # 322 Comment End -->
                <!-- CASE # 322 Begin -->
                <!-- If answer is neither yes or no, such as when shell disclosure
                has been automatically generated for a new award, then don't display any answer. -->
                	<logic:equal name="certificateDetails" property="answer" value="N" >
				<bean:message key="viewCOIDisclosureDetails.label.no"/>
                	</logic:equal>
                <!-- CASE # 322 End -->
                  </logic:present>

				     </font>&nbsp;</div>
                  </td>
                </tr>
				<%	count++;	%>
		</logic:iterate>
		</logic:present>
		</table>
            </td>
          </tr>
          <tr>
            <td height="23">&nbsp;</td>
          </tr>

          <tr>
            <td height="23">
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td background="<bean:write name="ctxtPath" />/images/tabback.gif">
                    <table border=0 cellpadding=0 cellspacing=0>
                      <tr>
                        <td> <img src="<bean:write name="ctxtPath" />/images/finEntDisc.gif" width="167" height="24"></td>
                      </tr>
                    </table>
                  </td>
                </tr>
              </table>
              <!-- show all entity info if user has financial entities-->
	<logic:notEqual name="collCOIDisclosureInfoSize" value="0">
              <table width="100%" border="0" cellspacing="1" cellpadding="5" align="right">
                <tr>
                  <td colspan="5" height="2"></td>
                </tr>
                <tr>
                	<td colspan="5" class="fontBrown">
                	<b><bean:message key="viewCOIDisclosureDetails.conflictStatusInstructions"/>
				<logic:equal name="moduleCode" value="1">
					<bean:message key="viewCOIDisclosureDetails.award"/>
				</logic:equal>
				<logic:notEqual name="moduleCode" value="1">
					 <bean:message key="viewCOIDisclosureDetails.proposal"/>
				</logic:notEqual>
                	</td>
                </tr>
                	
                <tr bgcolor="#CC9999">
                  <td width="113" height="25">
                   <div align="center"><font color="#FFFFFF">  <bean:message key="viewCOIDisclosureDetails.label.entityName"/> </font></div>
                  </td>
                  <td width="102" height="25">
                    <div align="center"><font color="#FFFFFF"> <bean:message key="viewCOIDisclosureDetails.label.conflictStatus"/> </font></div>
                  </td>
                  <td width="100" height="25">
                    <div align="center"><font color="#FFFFFF"> <bean:message key="viewCOIDisclosureDetails.label.reviwed"/> </font></div>
                  </td>
                  <td width="250" height="25">
                    <div align="center"><font color="#FFFFFF"> <bean:message key="viewCOIDisclosureDetails.label.explanation"/> </font></div>
                  </td>
                  <td width="20" height="25">
                    <div align="center"><font color="#FFFFFF">&nbsp; </font></div>
                  </td>
                  <%-- <bean:message key="viewCOIDisclosureDetails.label.viewDescription"/>--%>
                </tr>

		<logic:present  name="collCOIDisclosureInfo"  >
		<%	int count=0;	%>
		  <logic:iterate id="disclosureInfo" name="collCOIDisclosureInfo" type="edu.mit.coeus.coi.bean.DisclosureInfoBean">

	      <tr bgcolor='<%=( (count) % 2 == 0 ? "#FBF7F7" : "#F7EEEE") %>' valign="top">
                  <td width="113" height="25">
                    <div align="left">
                    <!-- Not working as the tag output is not parsed as per requirement, to be solved in next release-->
                    <%--
				<html:link  page="/viewDisclosureDetails.do?disclNo=<%=disclosureNo %>&amp;entNo=<bean:write name='disclosureInfo' property='entityNumber'/>" >
						<bean:write name='disclosureInfo' property='entityName'/>
				</html:link>
			--%>
			   <a href="<bean:write name='ctxtPath' />/viewDisclosureDetails.do?disclNo=<%=disclosureNo %>&amp;entNo=<coeusUtils:formatOutput name='disclosureInfo' property='entityNumber' defaultValue='&nbsp;' />" >
					<coeusUtils:formatOutput name='disclosureInfo' property='entityName' defaultValue='&nbsp;' />
				</a>
                    </div>
                  </td>
                  <td width="102" height="25">
                    <div align="left"> <coeusUtils:formatOutput name="disclosureInfo" property="conflictStatus" defaultValue="&nbsp;" /> &nbsp;</div>
                  </td>
                  <td width="100" height="25">
                    <div align="center">&nbsp; <coeusUtils:formatOutput name='disclosureInfo' property='reviewer' defaultValue='&nbsp;' /> </div>
                  </td>
<%--
                 <td width="250" height="25">
                    <div align="left">&nbsp; <coeusUtils:formatOutput name='disclosureInfo' property='desc' defaultValue='&nbsp;' /> </div>
                  </td>
 --%>
<!-- If explanation is more than 100 characters, add more icon in last column.  Icon is a link
to popup window with full text of explanation.  -->
 		<td width="250" height="25" align="left" >
                      <%String description = "";
                        String desc = "";
                        String more = "more" + count;
                        description = disclosureInfo.getDesc();
                        if ( (description != null ) && (description != "") ) {
                          desc = URLEncoder.encode(description);
                        }
                        if (desc.length() > 100 ) {
                        %>
                      <bean:define id="stringOne" value="<%=disclosureInfo.getDesc().substring(0,100)%>"/>
			<bean:define id="showMore" value="<%=more %>" />
                      <div>
                        <bean:write name="stringOne"/> <br>
                      </div>
                      <%}else {%>
			<div align="left">
			<coeusUtils:formatOutput name="disclosureInfo" property="desc" defaultValue="&nbsp;" />

			</div>
                      <%}%>
                    </td>
                  <td width="20" height="25">
                  	<logic:present name="showMore" >
                  	<logic:equal name="showMore" value="<%=more %>" >
                        <a href="JavaScript:openwinDesc('CoeusDisclosureDescription.jsp?desc=<%= desc %>');">
                          <img src="<bean:write name='ctxtPath'/>/images/more.gif" width="22" height="20" border="0">
                        </a>
			</logic:equal>

			</logic:present>

                    <div align="left">&nbsp;</div>
                  </td>
                </tr>
                <%	count++;	%>
                    </logic:iterate>
				</logic:present>

              </table>
              <!-- End list entity info -->
              </logic:notEqual>
              <logic:equal name="collCOIDisclosureInfoSize" value="0">
              <table width="100%" border="0" cellpadding="5" cellspacing="0">
              <tr>
              	<td height="2">&nbsp;</td>
              </tr>
              <tr>
              	<td>
              &nbsp;&nbsp;<bean:message key="viewCOIDisclosureDetails.noFinEnt"/>
              </td>
              </tr>
              </table>

              </logic:equal>

            </td>
          </tr>
          <tr>
          <td height="5">&nbsp;</td>
          </tr>
          <tr>
            <td height="23">
<!-- CASE #1374 Begin -->
<% 	int userpriv = Integer.parseInt(userprivilege);
	if(userpriv > 0 ){
%>
<!-- CASE #1374 End -->
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td background="<bean:write name="ctxtPath" />/images/tabback.gif">
                    <table border=0 cellpadding=0 cellspacing=0>
                      <tr>
                        <td> <img src="<bean:write name="ctxtPath" />/images/discdet.gif"  height="24"></td>
                      </tr>
                    </table>
                  </td>
                </tr>
              </table>
              <!-- show Disclosure Details -->
	<table width="100%" border="0" cellspacing="0" cellpadding="5">
                <tr>
                  <td colspan="6" height="10"></td>
                </tr>
		<tr bgcolor="#FBF7F7">
                  <td  width="123"> <bean:message key="viewCOIDisclosureDetails.label.sponsor"/></td>
                  <td  width="3"><div>:</div></td>
                  <%	System.out.println("discHeaderAwPrInfo.getSponsor(): "+discHeaderAwPrInfo.getSponsor());	%>
                  <td  width="238" >
                  	<b><coeusUtils:formatOutput name="discHeaderAwPrInfo" property="sponsor" scope="request"/></b>
                  </td>
		
                  <td width="123" >
                    <div align="left"><bean:message key="viewCOIDisclosureDetails.label.disclosureno"/> </div>
                  </td>
                  <td width="3" ><div>:</div></td>
                  <td width="238"  >
                    <div align="left"><b> <coeusUtils:formatOutput name="disclosureHeader" property="disclosureNo" defaultValue="&nbsp;" scope="request" /> </b> </div>
                  </td>
                </tr>
		<tr bgcolor="#F7EEEE" >
                  <td width="100" height="30">
                    <div align="left"><bean:message key="viewCOIDisclosureDetails.label.appliesTo"/> </div>
                  </td>
                  <td width="6" ><div>:</div></td>
                  <td width="176" >
                    <div align="left"><b> <coeusUtils:formatOutput name="disclosureHeader" property="appliesTo" defaultValue="&nbsp;" scope="request" /> </b></div>
                  </td>
                  <td width="123" >
                    <div align="left">

                    <logic:equal name="moduleCode" value="1" >
			 <bean:message key="viewCOIDisclosureDetails.label.awardno"/>
			</logic:equal>
			<logic:notEqual name="moduleCode" value="1">
					<bean:message key="viewCOIDisclosureDetails.label.proposalNo"/>
			</logic:notEqual>

			</div>
		  </td>
                  <td width="3" ><div>:</div></td>
                  <td width="238" >
                    <div align="left"><b> <coeusUtils:formatOutput name="disclosureHeader" property="keyNumber" defaultValue="&nbsp;" scope="request" /> </b>&nbsp;</div>
                  </td>
                </tr>
                <%--
                <tr valign="top">
                  <td width="100" height="30" bgcolor="#FBF7F7">
                    <div align="left"> <bean:message key="viewCOIDisclosureDetails.label.title"/> </div>
                  </td>
                  <td height="30" bgcolor="#FBF7F7" width="6"><div>:</div></td>
                  <td   colspan='4' bgcolor="#FBF7F7" >
                    <div align="left"><b> <coeusUtils:formatOutput name="discHeaderAwPrInfo" property="title" defaultValue="&nbsp;" scope="request" /> </b>&nbsp;</div>
                  </td>
                </tr>
                --%>
                <tr bgcolor="#FBF7F7" >
                  <td width="100" height="30">
                    <div align="left"><bean:message key="viewCOIDisclosureDetails.label.account"/> </div>
                 </td>
                  <td width="6" ><div>:</div></td>
                  <td width="176" >
                    <div align="left"><b> <coeusUtils:formatOutput name="discHeaderAwPrInfo" property="account" defaultValue="&nbsp;" scope="request" /> </b>&nbsp;</div>
                  </td>
                  <td width="123"  >
                  <div>
		<logic:equal name="moduleCode" value="1">
			<bean:message key="viewCOIDisclosureDetails.label.awardStatus"/>
		</logic:equal>
		<logic:notEqual name="moduleCode" value="1">
			 <bean:message key="viewCOIDisclosureDetails.label.proposalStatus"/>
		</logic:notEqual>
		</div>
                  </td>
                  <td width="3"  ><div>:</div></td>
                  <td width="238"  >
                    <div align="left"><b> <coeusUtils:formatOutput name="discHeaderAwPrInfo" property="status" defaultValue="&nbsp;" scope="request" /> </b>&nbsp;</div>
                  </td>

                </tr>
                <tr  bgcolor="#F7EEEE">
                  <td width="100" height="30" >
                    <div align="left"><bean:message key="viewCOIDisclosureDetails.label.disclosureType"/> </div>
                  </td>
                  <td width="6"  ><div>:</div></td>
                  <td width="176"  >
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
		<td width="123"  >
                    <div align="left"> <bean:message key="viewCOIDisclosureDetails.label.disclosureStatus"/>  </div>
                  </td>
                  <td width="3"  ><div>:</div></td>
                  <td width="238"  >
                    <div align="left"><b> <bean:write name="disclosureHeader" property="disclStatus" /> </b></div>
                  </td>    
                </tr>
                <tr bgcolor="#FBF7F7" >
                  <td width="100" height="30">
                    <div align="left"><bean:message key="viewCOIDisclosureDetails.label.reviewedBy"/>  </div>
                  </td>
                  <td  width="6">
                    <div align="left">: </div>
                  </td>
                  <td  width="176" >
                    <div align="left"><b> <coeusUtils:formatOutput name="disclosureHeader" property="reviewer" defaultValue="&nbsp;" scope="request" /> </b>
                    &nbsp;</div>
                  </td>
		<td width="100" height="30" bgcolor="#FBF7F7">
                    <div align="left"><bean:message key="viewCOIDisclosureDetails.label.lastUpdated"/> </div>
                   </td>
                  <td width="6"  bgcolor="#FBF7F7"><div>:</div></td>
                  <td width="176"  bgcolor="#FBF7F7">
                    <div align="left"><b> <coeusUtils:formatDate name="disclosureHeader" property="updatedDate"  scope="request"/>  </b></div>
                  </td>                  
                </tr>
              </table>              
              <!-- End disclosure details --> 
<!-- CASE #1374 Begin -->              
<%
	}
%>
<!-- CASE #1374 End -->              
          <tr>
            <td height="40">
              <div align="right">
                <a href="JavaScript:history.back();"><img src="<bean:write name="ctxtPath" />/images/goback.gif" width="55" height="22" border="0"></a>&nbsp;
<%--
    *    CASE#158 Begin
    *    IF userprivilege is 2, then user has Maintain COI role.  Then the user should be able to edit any disclosure.
    *    Make Edit button visible.
    *
    *    loggedinpersonid holds currently logged in user's person ID
    *    personInfo Holds person details of the currenlty selected person (whose disclosures the user is viewing)
    *    if loggedinpersonid = personInfo.getPersonID() then user is viewing his own disclosure
    *    else he is viewwing someone else's disclosure
    *    If a user is viewing his own disclosure and the disclosure's Reviewd By is set to PI then
    *    the user can edit the disclosure, make Edit button visible
--%>
<%-- CASE # 212 Comment begin. Logic moved to action class.

 <%
                if (Integer.parseInt(userprivilege) == 2)
                {
                  System.out.println("User Priv = 2");
                  %>
                  <a href="JavaScript:window.location='<bean:write name="ctxtPath" />/viewCOIDisclosureDetails.do?action=edit&disclNo=<%= disclosureNo %>';" method="POST"><img src="images/edit.gif" width="42" height="22" border="0"></a>
                  <%
                } else
                {
                  if ((loggedinpersonid.equals(personInfo.getPersonID())) && (Integer.parseInt(disclosureHeader.getReviewerCode()) == 1))
                  {
                  %>
                    <a href="JavaScript:window.location='<bean:write name="ctxtPath" />/viewCOIDisclosureDetails.do?action=edit&disclNo=<%= disclosureNo %>';" method="POST"><img src="images/edit.gif" width="42" height="22" border="0"></a>
                  <%
                  }
                }
                %>
CASE # 212 Comment End
--%>
<%--
CASE#158 End
--%>


<%--
CASE#158 Comment Begin

                <priv:hasOSPRight name="hasOspRightToEdit" value="<%=Integer.parseInt(userprivilege)%>">
                    <a href="JavaScript:window.location='<bean:write name="ctxtPath" />/viewCOIDisclosureDetails.do?action=edit&disclNo=<%= disclosureNo %>';" method="POST"><img src="images/edit.gif" width="42" height="22" border="0"></a>
                </priv:hasOSPRight>

                <priv:hasOSPRight name="noRights" value="<%=Integer.parseInt(userprivilege)%>">
                       <a href="JavaScript:window.location='<bean:write name="ctxtPath" />/viewCOIDisclosureDetails.do?action=edit&disclNo=<%= disclosureNo %>';" method="POST"><img src="images/edit.gif" width="42" height="22" border="0"></a>
                </priv:hasOSPRight>

CASE#158 Comment End
--%>
<%-- CASE # 212 Begin --%>
<%-- If "showEditButton" attribute has been set in request by action class, then show the edit button. --%>
	<logic:present name="showEditButton">
		<a href="JavaScript:window.location='<bean:write name="ctxtPath" />/viewCOIDisclosureDetails.do?action=edit&disclNo=<%= disclosureNo %>';" method="POST"><img src="<bean:write name='ctxtPath'/>/images/edit.gif" width="42" height="22" border="0"></a>
	</logic:present>
<%-- CASE #212 End. --%>
                &nbsp; &nbsp; &nbsp; </div>
            </td>
          </tr>
      </table>


	      </td>
	</tr>
</table>