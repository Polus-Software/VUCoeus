
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
<jsp:useBean  id="collCOIDisclosureInfo"	scope="request" class="java.util.Vector" />
<jsp:useBean  id="collCOICertDetails" 			scope="request" class="java.util.Vector" />
<jsp:useBean  id="moduleCode" 				scope="request" class="java.lang.String" />
<jsp:useBean  id="disclosureHeader" 		scope="request" class="edu.mit.coeus.coi.bean.DisclosureHeaderBean" />
<jsp:useBean id = "userprivilege" class = "java.lang.String" scope="session"/>
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
            <td height="23" > &nbsp;<font face="Verdana, Arial, Helvetica, sans-serif" size="2"> <b>
					<bean:message key="viewCOIDisclosureDetails.header" />
				</b></font>
			</td>
          </tr>

          <tr>
            <td height="23">
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td height="40">
                    <div align="right"><a href="JavaScript:history.back();"><img src="<bean:write name="ctxtPath" />/images/goback.gif" width="55" height="22" border="0"></a>&nbsp;
                    <priv:hasOSPRight name="hasOspRightToEdit" value="<%=Integer.parseInt(userprivilege)%>">
                       <a href="JavaScript:window.location='<bean:write name="ctxtPath" />/viewCOIDisclosureDetails.do?action=edit&disclNo=<%= disclosureNo %>';" method="POST"><img src="images/edit.gif" width="42" height="22" border="0"></a>
                    </priv:hasOSPRight>
                    <priv:hasOSPRight name="noRights" value="<%=Integer.parseInt(userprivilege)%>">
                       <a href="JavaScript:window.location='<bean:write name="ctxtPath" />/viewCOIDisclosureDetails.do?action=edit&disclNo=<%= disclosureNo %>';" method="POST"><img src="images/edit.gif" width="42" height="22" border="0"></a>
                    </priv:hasOSPRight>
                      &nbsp; &nbsp; &nbsp; </div>
                  </td>
                </tr>
              </table>
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
				<tr>
                  <td width="100" height="20" bgcolor="#FBF7F7">
                    <div align="left">&nbsp; <bean:message key="viewCOIDisclosureDetails.label.personName"/> </div>
                   </td>
                  <td width="6" height="20" bgcolor="#FBF7F7">:</td>
                  <td width="176" height="20" bgcolor="#FBF7F7">
                    <div align="left"><b> <coeusUtils:formatOutput name="disclosureHeader" property="name" defaultValue="&nbsp;" scope="request"/>  </b></div>
                  </td>
                  <td width="123" height="20" bgcolor="#FBF7F7">
                    <div align="left">&nbsp; <bean:message key="viewCOIDisclosureDetails.label.disclosureno"/> </div>
                  </td>
                  <td width="3" height="20" bgcolor="#FBF7F7">:</td>
                  <td width="238" height="20" bgcolor="#FBF7F7">
                    <div align="left"><b> <coeusUtils:formatOutput name="disclosureHeader" property="disclosureNo" defaultValue="&nbsp;" scope="request" /> </b> </div>
                  </td>
                </tr>
				<tr bgcolor="#F7EEEE">
                  <td width="100" height="20">
                    <div align="left">&nbsp; <bean:message key="viewCOIDisclosureDetails.label.appliesTo"/> </div>
                  </td>
                  <td width="6" height="20">:</td>
                  <td width="176" height="20">
                    <div align="left"><b> <coeusUtils:formatOutput name="disclosureHeader" property="appliesTo" defaultValue="&nbsp;" scope="request" /> </b>&nbsp;</div>
                  </td>
                  <td width="123" height="20">
                    <div align="left">&nbsp;

                    <logic:equal name="moduleCode" value="2" >
                   			 <bean:message key="viewCOIDisclosureDetails.label.proposalNo"/>
					</logic:equal>
					<logic:notEqual name="moduleCode" value="2">
							<bean:message key="viewCOIDisclosureDetails.label.awardno"/>
					</logic:notEqual>

					</div>
				  </td>
                  <td width="3" height="20">:</td>
                  <td width="238" height="20">
                    <div align="left"><b> <coeusUtils:formatOutput name="disclosureHeader" property="keyNumber" defaultValue="&nbsp;" scope="request" /> </b>&nbsp;</div>
                  </td>
                </tr>
                <tr>
                  <td width="100" height="20" bgcolor="#FBF7F7">
                    <div align="left">&nbsp; <bean:message key="viewCOIDisclosureDetails.label.title"/> </div>
                  </td>
                  <td height="20" bgcolor="#FBF7F7" width="6">:</td>
                  <td height="20" bgcolor="#FBF7F7" width="176">
                    <div align="left"><b> <coeusUtils:formatOutput name="discHeaderAwPrInfo" property="title" defaultValue="&nbsp;" scope="request" /> </b>&nbsp;</div>
                  </td>
                  <td height="20" bgcolor="#FBF7F7" width="123">&nbsp;</td>
                  <td height="20" bgcolor="#FBF7F7" width="3">&nbsp;</td>
                  <td height="20" bgcolor="#FBF7F7" width="238">&nbsp;</td>
                </tr>
                <tr bgcolor="#F7EEEE">
                  <td width="100" height="20">
                    <div align="left">&nbsp; <bean:message key="viewCOIDisclosureDetails.label.account"/> </div>
                 </td>
                  <td width="6" height="20">:</td>
                  <td width="176" height="20">
                    <div align="left"><b> <coeusUtils:formatOutput name="discHeaderAwPrInfo" property="account" defaultValue="&nbsp;" scope="request" /> </b>&nbsp;</div>
                  </td>
                  <td width="123" height="20">
                    <div align="left">&nbsp; <bean:message key="viewCOIDisclosureDetails.label.status"/> </div>
                  </td>
                  <td width="3" height="20">:</td>
                  <td width="238" height="20">
                    <div align="left"><b> <coeusUtils:formatOutput name="discHeaderAwPrInfo" property="status" defaultValue="&nbsp;" scope="request" /> </b>&nbsp;</div>
                  </td>
                </tr>
                <tr>
                  <td width="100" height="20" bgcolor="#FBF7F7">
                    <div align="left">&nbsp; <bean:message key="viewCOIDisclosureDetails.label.disclosureType"/> </div>
                  </td>
                  <td width="6" height="20" bgcolor="#FBF7F7">:</td>
                  <td width="176" height="20" bgcolor="#FBF7F7">
                    <div align="left"><b>
					<%-- show the disclosure type in label mode(select box) if user requesition type
					is not edit
                     --%>
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
                    <div align="left">&nbsp; <bean:message key="viewCOIDisclosureDetails.label.disclosureStatus"/>  </div>
                  </td>
                  <td width="3" height="20" bgcolor="#FBF7F7">:</td>
                  <td width="238" height="20" bgcolor="#FBF7F7">
                    <div align="left"><b> <bean:write name="disclosureHeader" property="disclStatus" /> </b>&nbsp;</div>
                  </td>
                </tr>
                <tr bgcolor="#F7EEEE">
                  <td width="100" height="20">
                    <div align="left">&nbsp; <bean:message key="viewCOIDisclosureDetails.label.reviewedBy"/>  </div>
                  </td>
                  <td height="20" width="6">
                    <div align="left">: </div>
                  </td>
                  <td height="20" width="176" bgcolor="#F7EEEE">
                    <div align="left"><b> <coeusUtils:formatOutput name="disclosureHeader" property="reviewer" defaultValue="&nbsp;" scope="request" /> </b>&nbsp;</div>
                  </td>
                  <td height="20" width="123">&nbsp;</td>
                  <td height="20" width="3">&nbsp;</td>
                  <td height="20" width="238" bgcolor="#F7EEEE">&nbsp;</td>
                </tr>
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
                        <td> <img src="<bean:write name="ctxtPath" />/images/certification.gif" width="120" height="24"></td>
                      </tr>
                    </table>
                  </td>
                </tr>
              </table>
              <!-- show COI DIsclosures certificates information -->
              <table width="100%" border="0" cellspacing="1" cellpadding="5" align="right">
                <tr>
                  <td colspan="2" height="5"></td>
                </tr>
                <tr bgcolor="#CC9999">
                  <td width="372" height="25" bgcolor="#CC9999">
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
                  <td width="372" height="25">
                    <div align="justify"><font face="Verdana, Arial, Helvetica, sans-serif"> <bean:write name="certificateDetails" property="question"/>
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

                    <logic:notEqual name="certificateDetails" property="answer" value="Y" >
					      <bean:message key="viewCOIDisclosureDetails.label.no"/>
                    </logic:notEqual>
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
              <!-- show all COI Dislcosures info -->
              <table width="100%" border="0" cellspacing="1" cellpadding="5" align="right">
                <tr>
                  <td colspan="5" height="5"></td>
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

	      <tr bgcolor='<%=( (count) % 2 == 0 ? "#FBF7F7" : "#F7EEEE") %>'>
                  <td width="113" height="25">
                    <div align="left">
                    <%-- Not working as the tag output is not parsed as per requirement, to be solved in next release
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
                    <div align="left">&nbsp; <coeusUtils:formatOutput name='disclosureInfo' property='reviewer' defaultValue='&nbsp;' /> </div>
                  </td>
			<%--
                  <td width="250" height="25">
                    <div align="left">&nbsp; <coeusUtils:formatOutput name='disclosureInfo' property='desc' defaultValue='&nbsp;' /> </div>
                  </td>
 			--%>

 		<td width="20" height="25" >
                      <%String description = "";
                        String desc = "";
                        description = disclosureInfo.getDesc();
                        if ( (description != null ) && (description != "") ) {
                          desc = URLEncoder.encode(description);
                        }
                        if (desc.length() > 100 ) {
                        %>
                      <bean:define id="stringOne" value="<%=disclosureInfo.getDesc().substring(0,100)%>"/>
                      <div align="left">
                        <bean:write name="stringOne"/> <br>
                        <a href="JavaScript:openwinDesc('CoeusDisclosureDescription.jsp?desc=<%= desc %>');">
                          <img src="<bean:write name='ctxtPath'/>/images/more.gif" width="22" height="20" border="0">
                        </a>
                      </b> </div>
                      <%}else {%>
			<div align="left">
			<coeusUtils:formatOutput name="disclosureInfo" property="desc" defaultValue="&nbsp;" />
			<%--<coeusUtils:formatOutput name="disclosureInfo" property="desc" defaultValue="&nbsp;" scope="request" />--%>
			</div>
                      <%}%>
                    </td>
                  <td width="20" height="25">
                    <div align="left">&nbsp;</div>
                  </td>
                </tr>
                <%	count++;	%>
                    </logic:iterate>
				</logic:present>

              </table>
            </td>
          </tr>
          <tr>
            <td height="40">
              <div align="right">
                <a href="JavaScript:history.back();"><img src="<bean:write name="ctxtPath" />/images/goback.gif" width="55" height="22" border="0"></a>&nbsp;
                <priv:hasOSPRight name="hasOspRightToEdit" value="<%=Integer.parseInt(userprivilege)%>">
                    <a href="JavaScript:window.location='<bean:write name="ctxtPath" />/viewCOIDisclosureDetails.do?action=edit&disclNo=<%= disclosureNo %>';" method="POST"><img src="images/edit.gif" width="42" height="22" border="0"></a>
                </priv:hasOSPRight>
                <priv:hasOSPRight name="noRights" value="<%=Integer.parseInt(userprivilege)%>">
                       <a href="JavaScript:window.location='<bean:write name="ctxtPath" />/viewCOIDisclosureDetails.do?action=edit&disclNo=<%= disclosureNo %>';" method="POST"><img src="images/edit.gif" width="42" height="22" border="0"></a>
                </priv:hasOSPRight>
                &nbsp; &nbsp; &nbsp; </div>
            </td>
          </tr>
      </table>

	      </td>
	</tr>
</table>