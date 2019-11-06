<%--
/*
 * @(#)ViewFinancialEntityContent.jsp  1.0.1 2002/06/08 11:45:12
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.

 * @author  RaYaKu
 */
--%>
<%--
A View component to show the Financial Entity Details
--%>
<%@page errorPage = "ErrorPage.jsp" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" 	prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" 	prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" 	prefix="logic" %>
<%@ taglib uri="/WEB-INF/privilege.tld" prefix="priv" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"	prefix="coeusUtils" %>
<%@ include file="CoeusContextPath.jsp"  %>
<jsp:useBean id="actionFrom" scope="request" class="java.lang.String"/>

<jsp:useBean id="personName" scope="request" class="java.lang.String" />
<jsp:useBean id="entityNo" scope="request" class="java.lang.String" />
<jsp:useBean id="seqNo" scope="request" class="java.lang.String" />
<jsp:useBean id="entityDetails" scope="request" class="edu.mit.coeus.coi.bean.EntityDetailsBean" />
<jsp:useBean id="colFinCertificationDetails" 	scope="request" class="java.util.Vector" />
<jsp:useBean id = "userprivilege" class = "java.lang.String" scope="session"/>
<%
 //create a hash map which holds the parameter names and values
java.util.HashMap htmlLinkValues = new java.util.HashMap();
htmlLinkValues.put("actionFrom", actionFrom); ;
htmlLinkValues.put("entityNo", entityNo); ;
htmlLinkValues.put("seqNo", seqNo);
pageContext.setAttribute("htmlLinkValues", htmlLinkValues);
%>
<table width="100%" cellpadding="0" cellspacing="0" border="0">
      <tr>
        <td background="<bean:write name="ctxtPath"/>/images/separator.gif" width="1">
            <img src="<bean:write name="ctxtPath"/>/images/separator.gif" width="1">
        </td>
        <td width="645">
          <table width="100%" border="0" cellspacing="0" align="left" cellpadding="0">
            <tr>
             <td height="5"></td>
           </tr>
           <tr bgcolor="#cccccc">
            <td height="23" > &nbsp;<font face="Verdana, Arial, Helvetica, sans-serif" size="2"> <b>
				<bean:message key="viewFinEntity.header" />
				<coeusUtils:formatOutput name="entityDetails" property="name" defaultValue="&nbsp;" scope="request"/>
				</b></font>
			</td>
		</tr>
		<tr valign="top">
			<td height="23">
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
				<td height="30">
					 <div align="left">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
							 <td>
								<div align="left">&nbsp;<font color="#663300"><b>
									<font color="#7F1B00">
									<bean:message key="viewFinEntity.personName" /> :
									<bean:write name="personName" scope="request" />
									</font></b></font>
								</div>
							</td>
							<td height="40">
								<div align="right">
									<a href="JavaScript:history.back();">
										<img src="<bean:write name="ctxtPath"/>/images/goback.gif" width="55" height="22" border="0">
									</a>&nbsp;
                                      <priv:hasOSPRight name="hasOspRightToEdit" value="<%=Integer.parseInt(userprivilege)%>">
									<%-- supply  htmlLinkValues(HashMap) which has got all param names and values --%>
									<html:link page="/editFinEntity.do" name="htmlLinkValues" >
										<img src="<bean:write name="ctxtPath"/>/images/edit.gif" width="55" height="22" border="0">
									</html:link> &nbsp; &nbsp; &nbsp;
									</priv:hasOSPRight>
									  <priv:hasOSPRight name="noRights" value="<%=Integer.parseInt(userprivilege)%>">
									<%-- Create an image button which submits the page with assigned querystring information(htmlLinkvalues) --%>
											<html:link page="/editFinEntity.do" name="htmlLinkValues" >
															<img src="<bean:write name="ctxtPath"/>/images/edit.gif" width="55" height="22" border="0">
											</html:link>
									  </priv:hasOSPRight>

								</div>
							</td>
							</tr>
			 			</table>
			 		</div>
			 	</td>
			 	</tr>
		    </table>
			 <table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td background="<bean:write name="ctxtPath"/>/images/tabback.gif">
						<table border=0 cellpadding=0 cellspacing=0>
						<tr>
						<td><img src="<bean:write name="ctxtPath"/>/images/entitydetails.gif" width="120" height="24"></td>
						</tr>
						</table>
					</td>
				</tr>
		      </table>
		      <!-- Financial Entity Details -->
			  <table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td colspan="6" height="5"></td>
				</tr>
				<tr bgcolor="#FBF7F7">
					<td width="105" height="25">
						<div align="left">&nbsp;<bean:message key="viewFinEntity.label.name" /></div>
					</td>
					<td width="6" height="25">
						<div align="left">:</div>
					</td>
					<td width="202" height="25">
						<div align="left"><b> <coeusUtils:formatOutput name="entityDetails" property="name" defaultValue="&nbsp;" scope="request"/> </b></div>
					</td>
					<td width="114" height="25">
						<div align="left"><bean:message key="viewFinEntity.label.type" />&nbsp;</div>
					</td>
					<td width="6" height="25">
						<div align="left">:</div>
					</td>
					<td width="362" height="25">
						<div align="left"><b><coeusUtils:formatOutput name="entityDetails" property="type" defaultValue="&nbsp;" scope="request"/> </b></div>
					</td>
				</tr>
				<tr bgcolor="#F7EEEE">
					<td width="105" height="25">
					<div align="left">&nbsp;<bean:message key="viewFinEntity.label.status"/></div>
					</td>
					<td width="6" height="25">
						<div align="left">:</div>
					</td>
					<td width="202" height="25">
						<div align="left"><b> <coeusUtils:formatOutput name="entityDetails" property="status" defaultValue="&nbsp;" scope="request"/> </b></div>
					</td>
					<td width="114" height="25">
						<div align="left"><bean:message key="viewFinEntity.label.shareOwnership"/></div>
					</td>
					<td width="6" height="25">
						<div align="left">:</div>
					</td>
					<td width="362" height="25">
						<div align="left"><b>
                        <logic:present name="entityDetails" property="shareOwnship" >
                            <logic:equal name="entityDetails" property="shareOwnship" value="P" scope="request">
                                <bean:message key="viewFinEntity.label.public"/>
                            </logic:equal>
                            <logic:notEqual name="entityDetails" property="shareOwnship" value="P" scope="request">
                                <bean:message key="viewFinEntity.label.private"/>
                            </logic:notEqual>
                        </logic:present>
                         <logic:notPresent name="entityDetails" property="shareOwnship" >
                         &nbsp;
                         </logic:notPresent>
					  </b></div>
					</td>
				</tr>
				<tr bgcolor="#FBF7F7">
					<td width="105" height="25">
						<div align="left">&nbsp;<bean:message key="viewFinEntity.label.explanation"/></div>
					</td>
					<td width="6" height="25">
						<div align="left">:</div>
					</td>
					<td colspan="4" height="25">
						<div align="left"><b>
							<coeusUtils:formatOutput name="entityDetails" property="entityDescription" defaultValue="&nbsp;" scope="request"/>
						</b></div>
					</td>
				</tr>
				<tr>
					<td colspan="6" height="15" bgcolor="#CC9999">
						<div align="left"><font color="#FFFFFF">
							<b>&nbsp;<bean:message key="viewFinEntity.personRelation"/>
						</b></font></div>
					</td>
				</tr>
				<tr bgcolor="#FBF7F7">
					<td width="105" height="25">
						<div align="left">&nbsp;<bean:message key="viewFinEntity.personRelation.label.type"/></div>
					</td>
					<td width="6" height="25">
						<div align="left">:</div>
					</td>
					<td width="202" height="25">
						<div align="left"><b>
							<coeusUtils:formatOutput name="entityDetails" property="personReType" defaultValue="&nbsp;" scope="request"/>
						</b> </div>
					</td>
					<td width="114" height="25">
						<div align="left">&nbsp;</div>
					</td>
		  		    <td width="6" height="25">
						<div align="left">&nbsp;</div>
				    </td>
				    <td width="362" height="25">
						<div align="left">&nbsp; </div>
				    </td>
				</tr>
				<tr bgcolor="#F7EEEE">
				  <td width="105" height="25">
					<div align="left">&nbsp;<bean:message key="viewFinEntity.personRelation.label.explanation"/></div>
				  </td>
				  <td width="6" height="25">
					<div align="left">:</div>
				  </td>
				  <td colspan="4" height="25">
					<div align="left"><b>
						<coeusUtils:formatOutput name="entityDetails" property="personReDesc" defaultValue="&nbsp;" scope="request"/>
					</b></div>
				  </td>
				</tr>

		<!-- Brief info on organization's relationship to this entity -->

				<tr bgcolor="#FBF7F7">
				<td colspan="6" height="25" width = "100%">
				<div align="left">&nbsp;<bean:message key="viewFinEntity.organizationRelationShip.label"/>
				<b><coeusUtils:formatOutput name="entityDetails" property="orgRelationship" scope="request"/></b></div>
				</td>
				</tr>
	<!-- More complete info on organization's relationship to this entity.  Later release will include more information here if user has maintain rights.-->
	<%--
				<tr>
				  <td colspan="6" height="15" bgcolor="#CC9999">
					<div align="left"><font color="#FFFFFF"><b>&nbsp;
						<bean:message key="viewFinEntity.organizationRelationShip.label" />
	                    <coeusUtils:formatOutput name="entityDetails" property="orgRelationship" scope="request"/>
                    </b></font></div>
				  </td>
				</tr>
				<tr bgcolor="#FBF7F7">
				  <td width="105" height="25">
					<div align="left">&nbsp;
						<bean:message key="viewFinEntity.organizationRelationShip.label.explanation" />
					</div>
				  </td>
				  <td width="6" height="25">
					<div align="left">:</div>
				  </td>
				  <td colspan="4" height="25">
					<div align="left"><b>
						<coeusUtils:formatOutput name="entityDetails" property="orgDescription" defaultValue="&nbsp;" scope="request"/>
					</b></div>
				  </td>
				</tr>

				<tr bgcolor="#FBF7F7">
					<td width="105" height="25">
						<div align="left">&nbsp;<bean:message key="viewFinEntity.organizationRelationShip.label.sponsorid" /></div>
					</td>
					<td width="6" height="25">
						<div align="left">:</div>
					</td>
					<td width="202" height="25">
						<div align="left"><b> <coeusUtils:formatOutput name="entityDetails" property="sponsor" defaultValue="&nbsp;" scope="request"/> </b></div>
					</td>
					<td width="114" height="25">
						<div align="left"><bean:message key="viewFinEntity.organizationRelationShip.label.sponsor" />&nbsp;</div>
					</td>
					<td width="6" height="25">
						<div align="left">:</div>
					</td>
					<td width="362" height="25">
						<div align="left"><b><coeusUtils:formatOutput name="entityDetails" property="sponsorName" defaultValue="&nbsp;" scope="request"/> </b></div>
					</td>
				</tr>
				<tr bgcolor="#FBF7F7">
				  <td width="105" height="25">
					<div align="left">&nbsp;
						<bean:message key="viewFinEntity.organizationRelationShip.label.lastUpdate"/>
					</div>
				  </td>
				  <td width="6" height="25">
					<div align="left">:</div>
				  </td>
				  <td height="25" width="202">
					<div align="left"><b>
					<coeusUtils:formatDate name="entityDetails" property="lastUpdate" />
					</b></div>
				  </td>
				  <td height="25" width="114">
					<div align="left"><bean:message key="viewFinEntity.organizationRelationShip.label.updateUser"/></div>
				  </td>
				  <td height="25" width="6">
					<div align="left">:</div>
				  </td>
				  <td height="25" width="362">
					<div align="left"><b>
						<coeusUtils:formatOutput name="entityDetails" property="updateUser" defaultValue="&nbsp;" scope="request"/>
					</b></div>
				  </td>
				</tr>
				<tr bgcolor="#F7EEEE">
				  <td width="105" height="25">
					<div align="left">&nbsp;<bean:message key="viewFinEntity.organizationRelationShip.label.sequenceNo"/>.</div>
				  </td>
				  <td width="6" height="25">
					<div align="left">:</div>
				  </td>
				  <td width="202" height="25">
					<div align="left"><b>
						<coeusUtils:formatOutput name="entityDetails" property="seqNumber" defaultValue="&nbsp;" scope="request"/>
					</b></div>
				  </td>
				  <td width="114" height="25">
					<div align="left">&nbsp;</div>
				  </td>
				  <td width="6" height="25">
					<div align="left">&nbsp;</div>
				  </td>
				  <td width="362" height="25">
					<div align="left">&nbsp;</div>
				  </td>
				</tr>
	End of commented out section on organization's relationship to this entity--%>
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
			  <td background="<bean:write name="ctxtPath"/>/images/tabback.gif">
				<table border=0 cellpadding=0 cellspacing=0>
				  <tr>
					<td><img src="<bean:write name="ctxtPath"/>/images/certification.gif" width="120" height="24"></td>
				  </tr>
				</table>
			  </td>
			</tr>
		  </table>
		  <!-- Certification-->
		  <table width="100%" border="0" cellspacing="1" cellpadding="5">
			<tr>
			  <td colspan="2" height="5"></td>
			</tr>
			<tr>

			  <td width="450" height="25" bgcolor="#CC9999">
				<div align="center"><font color="#FFFFFF">
					<bean:message key="viewFinEntity.label.question"/>
				</font></div>
			  </td>
			  <td width="128" height="25" bgcolor="#CC9999">
				<div align="center"><font color="#FFFFFF">
					<bean:message key="viewFinEntity.label.answer"/>
				</font></div>
			  </td>
			</tr>
			<!-- Certication Details of Financial Entity -->
			<logic:present  name="colFinCertificationDetails"  >
			<%	int count = 0;	%>
               	<logic:iterate id="certficateDetails" name="colFinCertificationDetails" type="edu.mit.coeus.coi.bean.CertificateDetailsBean">

            <tr bgcolor='<%=( (count) % 2 == 0 ? "#FBF7F7" : "#F7EEEE") %>'>
			 <%-- <td width="85" height="25" valign="top">
				<div align="center"><font face="Verdana, Arial, Helvetica, sans-serif">
			    	<a href='javascript:showQuestion("<bean:write name='ctxtPath' />/question.do?questionNo=<%=certficateDetails.getCode()%>",  "ae");'  method="POST">
				<coeusUtils:formatOutput name="certficateDetails" property="code" defaultValue="&nbsp;" />
				   </a>
				</font></div>
			  </td> --%>
			  <td valign="top">
				<div align="justify">
					<coeusUtils:formatOutput name="certficateDetails" property="question" defaultValue="&nbsp;"/>
				<a href='javascript:showQuestion("<bean:write name='ctxtPath' />/question.do?questionNo=<%=certficateDetails.getCode()%>",  "ae");'  method="POST">
				<bean:message key="viewFinEntity.question.explanationLink" />
				</a>
				</div>
			  </td>
			  <td height="25" valign="top">
				<div align="center"> <font face="Verdana, Arial, Helvetica, sans-serif">
					<coeusUtils:formatOutput name="certficateDetails" property="answer" defaultValue="&nbsp;" />
				 </font></div>
			  </td>
			</tr>
			<%	count++;	%>
				</logic:iterate>
			</logic:present>
			<tr><td colspan = "2" height="5"></td></tr>
			<tr>
			  <td colspan="2" height="35" >
				<div align="right"><a href="JavaScript:history.back();"><img src="<bean:write name="ctxtPath"/>/images/goback.gif" width="55" height="22" border="0"></a>&nbsp;
                                <priv:hasOSPRight name="hasOspRightToEdit" value="<%=Integer.parseInt(userprivilege)%>">
				<%-- Create an image button which submits the page with assigned querystring information(htmlLinkvalues) --%>
					<html:link page="/editFinEntity.do" name="htmlLinkValues" >
							<img src="<bean:write name="ctxtPath"/>/images/edit.gif" width="55" height="22" border="0">
					</html:link>
                                  </priv:hasOSPRight>
                                  <priv:hasOSPRight name="noRights" value="<%=Integer.parseInt(userprivilege)%>">
				<%-- Create an image button which submits the page with assigned querystring information(htmlLinkvalues) --%>
					<html:link page="/editFinEntity.do" name="htmlLinkValues" >
							<img src="<bean:write name="ctxtPath"/>/images/edit.gif" width="55" height="22" border="0">
					</html:link>
                                  </priv:hasOSPRight>
				  &nbsp; &nbsp; &nbsp;
                                  <font face="Verdana, Arial, Helvetica, sans-serif" size="2">
				  </font><font face="Verdana, Arial, Helvetica, sans-serif" size="2">
				  </font></div>
			  </td>
			</tr>
		  </table>
		</td>
	  </tr>
	</table>
  </td>
</tr>
</table>