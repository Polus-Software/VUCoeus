<%--
/*
 * @(#)FinancialEntityDetailsContent.jsp	1.0 2002/06/03 15:55:29
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author RaYaKu
 */
--%>
<%@page errorPage = "ErrorPage.jsp" %>
<%-- This JSP page is for Edit financial entity--%>
<%@ page import="java.util.Vector,edu.mit.coeus.coi.bean.ComboBoxBean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" 	prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" 	prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" 	prefix="logic" %>
<%@ include file="CoeusContextPath.jsp"  %>
<%-- The below beans are available in session scope because when this form is
validated at server side and any errors encounter then the show the same page
back to user with previous data but with validation errors too.
  --%>

<%	System.out.println("begin FinancialEntityDetailsContent.jsp");	%>
<jsp:useBean id="collEntityCertDetails" scope="session" class="java.util.Vector" />
<jsp:useBean id="collOrgTypes" scope="session" class="java.util.LinkedList" />
<jsp:useBean id="collEntityStatus" scope="session" class="java.util.LinkedList" />
<jsp:useBean id="collRelations" scope="session" class="java.util.LinkedList" />
<%	System.out.println("after jsp:useBean statements");	%>
<%-- Find the size of collEntityCertDetails --%>
<bean:size id="certificatesSize" name="collEntityCertDetails" />
<%
//create a vector of comboboxbean instances to show Share owner ship options
Vector optionsShareOwnership = new Vector();
optionsShareOwnership.add(new ComboBoxBean(""," ") );
optionsShareOwnership.add(new ComboBoxBean("P","Public") );
optionsShareOwnership.add(new ComboBoxBean("V","Private") );
pageContext.setAttribute("optionsShareOwnership",optionsShareOwnership);
%>
<table width="100%"  cellpadding="0" cellspacing="0" border="0">
<html:form action="/finEntityDetails.do" >
 <tr>
  <td background="<bean:write name="ctxtPath"/>/images/separator.gif" width="1">
    <image src="<bean:write name="ctxtPath"/>/images/separator.gif" width="1">
  </td>
  <td width="645">
    <table width="100%" border="0" cellspacing="0" align="left" cellpadding="0">
		<tr>
		  <td height="5" width="100%"></td>
		</tr>
		<tr bgcolor="#cccccc">
		  <td height="23" width="100%" > &nbsp; <font face="Verdana, Arial, Helvetica, sans-serif" size="2">
            		<b> <bean:message key="financialEntityDetails.header" />
            		</b></font>
		  </td>
		</tr>
		<tr>
		  <td height="23" colspan="2"> <%-- Display Errors --%> <html:errors/>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
			 <td height="30">
			 <div align="left">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
					 <td>
						<div align="left">&nbsp;<font color="#663300"><b>
							<font color="#7F1B00">
							<bean:message key="financialEntityDetails.personName" />:
							<bean:write  name="frmFinancialEntityDetailsForm" property="userFullName" scope="request" />
							<html:hidden property="userFullName" />
							</font></b></font>
						</div>
					 </td>
					 <td height="40">
						<div align="right">
							<a href="JavaScript:history.back();">
								<img src="<bean:write name="ctxtPath"/>/images/goback.gif" width="55" height="22" border="0">
							</a>&nbsp;
							<html:image page="/images/submit.gif"  border="0" />
							</a>
							&nbsp; &nbsp; &nbsp;
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
				<td> <img src="<bean:write name="ctxtPath"/>/images/entitydetails.gif" width="120" height="24"></td>
				</tr>
		 	 </table>
			</td>
		 </tr>
		</table>
		<table width="100%" border="0" cellspacing="0" cellpadding="5">
			<tr>
				<td colspan="6" height="5"></td>
			</tr>
			<tr>
				<td width="109" height="30" bgcolor="#FBF7F7">
					<div align="left"><bean:message key="financialEntityDetails.name" /></div>
				</td>
				<td width="7" height="30" bgcolor="#FBF7F7">:</td>
				<td width="52" height="30" bgcolor="#FBF7F7"> <html:text  size="30"  property="name" />
                </td>
				<td width="210" height="30" bgcolor="#FBF7F7">
                  <div align="left"><bean:message key="financialEntityDetails.type" /></div>
				</td>
				<td width="11" height="30" bgcolor="#FBF7F7">:</td>
				<td width="235" height="30" bgcolor="#FBF7F7">

					<html:select property="typeCode" >
	 			        <html:options collection="collOrgTypes" property="code"    labelProperty="description"  />
					</html:select>
				</td>
			</tr>
			<tr bgcolor="#F7EEEE">
				<td width="109" height="30">
					<div align="left"><bean:message key="financialEntityDetails.shareOwnership" /></div>
				</td>
				<td width="7" height="30">:</td>
				<td width="52" height="30">
                			<html:select property="shareOwnership" >
                                            <html:options collection="optionsShareOwnership" property="code" labelProperty="description" />
					</html:select>
				</td>
				<td width="210" height="30">
                  <div align="left"><bean:message key="financialEntityDetails.status" /></div>
				</td>
				<td width="11" height="30">:</td>
				<td width="235" height="30">
                				<html:select property="status" >
							<html:options collection="collEntityStatus" property="code" labelProperty="description" />
					    </html:select>
				</td>
			</tr>
			<tr>
				<td width="109" height="70" bgcolor="#FBF7F7">
					<div align="left">&nbsp;<bean:message key="financialEntityDetails.explanation" /></div>
				</td>
				<td width="7" height="70" bgcolor="#FBF7F7">:
					<div align="right"></div>
				</td>
				<td colspan="4" height="30" bgcolor="#FBF7F7">
					<%--<html:textarea property="description" wrap='virtual' cols="40" rows="2" />--%>
                                        <textarea name="description"
                                            wrap='virtual' cols="40" rows="2" /><bean:write
                                                name='frmFinancialEntityDetailsForm' property='description'/></textarea>
				</td>
			</tr>
			<tr bgcolor="#CC9999">
				<td colspan="6" height="25">
					<div align="left"><font color="#FFFFFF"><b><bean:message key="financialEntityDetails.personRelation.header" />
						</b>
					</font></div>
				</td>
			</tr>
			<tr bgcolor="#F7EEEE">
				<td width="109" height="30">
				<div align="left"><bean:message key="financialEntityDetails.personRelation.type" /></div>

				</td>
				<td width="7" height="30">:</td>

                <td width="52" height="30">
					<html:select property="personRelationTypeCode" >
							<html:options collection="collRelations" property="code" labelProperty="description" />
				    	</html:select>
				</td>
				<td width="210" height="30">
                  <div align="right">&nbsp;</div>
				</td>
				<td width="11" height="30">&nbsp;</td>
				<td width="235" height="30">&nbsp; </td>
			</tr>
			<tr>
				<td width="109" height="70" bgcolor="#FBF7F7">
					<div align="left"><bean:message key="financialEntityDetails.personRelation.explanation" /></div>
				</td>
				<td width="7" height="70" bgcolor="#FBF7F7">: </td>
				<td colspan="4" height="70" bgcolor="#FBF7F7">
					<%--<html:textarea property="personRelationDesc" cols="40" rows="2"  />--%>
                                        <textarea name="personRelationDesc"
                                            wrap='virtual' cols="40" rows="2"  /><bean:write
                                                name='frmFinancialEntityDetailsForm' property='personRelationDesc'/></textarea>
				</td>
			</tr>
		<%--	<tr>
				<td colspan="6" height="25" bgcolor="#CC9999">
				<div align="left"><font color="#FFFFFF"><b>&nbsp;
				<bean:message key="financialEntityDetails.orgRelation.header" />
				</b></font></div>
				</td>
			</tr>

			<tr>
				<td width="109" height="30" bgcolor="#FBF7F7">&nbsp;</td>
				<td colspan="5" height="30" bgcolor="#FBF7F7">
				<div align="left">
					<html:radio property="orgRelationType" value="Y" />
					<bean:message key="financialEntityDetails.orgRelation.related" />

					<html:radio property="orgRelationType" value="N" />
					<bean:message key="financialEntityDetails.orgRelation.notRelated" />

					<html:radio property="orgRelationType" value="X" />
					<bean:message key="financialEntityDetails.orgRelation.dontKnow" />
				</div>
				</td>
				</tr>
				<tr bgcolor="#F7EEEE">
					<td width="109" height="70">
					<div align="left">&nbsp;<bean:message key="financialEntityDetails.orgRelation.explanation" /></div>
					</td>
					<td width="7" height="70">: </td>
					<td colspan="4" height="70">
					<!--<html:textarea property="orgRelationDesc" cols="40" rows="2" />-->
                                        <textarea name="orgRelationDesc"
                                            wrap='virtual' cols="40" rows="2" ><bean:write
                                                name='frmFinancialEntityDetailsForm'
                                                    property='orgRelationDesc'/></textarea>
					</td>
				</tr>
		--%>

		<tr bgcolor="#F7EEEE">
			<td width="168" height="30" colspan="3">
			<div align="left">
				<bean:message key="financialEntityDetails.orgRelation.question" />
			</div>
			</td>
			<td colspan="3" width="456">
			<div align="left">
				<html:radio property="orgRelationType" value="Y" />
				<bean:message key="financialEntityDetails.orgRelation.related" />

				<html:radio property="orgRelationType" value="N" />
				<bean:message key="financialEntityDetails.orgRelation.notRelated" />

				<html:radio property="orgRelationType" value="X" />
				<bean:message key="financialEntityDetails.orgRelation.dontKnow" />
			</div>
			</td>
		</tr>
<!--For this release, don't include information on sponsor.

			<%--<tr bgcolor="#F7EEEE">
				<td width="109" height="30">
					<div align="left">&nbsp;<bean:message key="financialEntityDetails.label.sponsorID" /></div>
				</td>
				<td width="7" height="30">:</td>
				<td width="52" height="30">
				<html:text  size="15"  property="sponsorId" />
                        <font face="Verdana, Arial, Helvetica, sans-serif" size="2">
                        <a href="JavaScript:openwin('<bean:write name="ctxtPath"/>/coeusSearch.do?searchname=sponsorSearch&fieldName=sponsorId','sponsor')"
                           method="POST"> <img src="<bean:write name="ctxtPath"/>/images/searchpage.gif"
                           width="22" height="20" border="0"> </a> </font>
                        </td>
			      <td height="25" width="210">
                          <div align="left"><bean:message key="financialEntityDetails.label.sponsorName"/></div>
			      </td>
			      <td  height="25" width="11" >:</td>
			      <td height="25" width="235">
                          <div align="left">
                            <b>
				      <bean:write  name="frmFinancialEntityDetailsForm" property="sponsorName" scope="request" />
			          </b>
                          </div>
				<html:hidden property="sponsorName" />
			      </td>
			      --%>
<!--End of commented out sponsor information-->
<!--Another version of gather sponsor information, commented out prior to the above version being
commented out.  -->
				<%--
				<td width="210" height="30" bgcolor="#FBF7F7">
                          <div align="left"><bean:message key="financialEntityDetails.label.sponsorName" /></div>
				</td>
				<td width="11" height="30" bgcolor="#FBF7F7">:</td>
				<td width="235" height="30" bgcolor="#FBF7F7">
				<bean:write  name="frmFinancialEntityDetailsForm" property="sponsorName" scope="request" />
				<html:hidden property="sponsorName" />
				</td>
				--%>
			<%--</tr>--%>







			<!-- If user request is Adding a new financial entity then don't show the
					  below features which are required only for Edit Financial entity.
					  In other way show these features when user requests to update the Financial entity.
			-->
<!--For this release, do not display last update and sequence number information.   -->
		<%--
			<logic:equal name="frmFinancialEntityDetailsForm" property="actionType" value="U">

			<tr bgcolor="#F7EEEE">
			<td width="109" height="25">
			<div align="left">&nbsp;<bean:message key="financialEntityDetails.label.lastUpdate"/></div>
			</td>
			    <td  height="25" width="7" >:</td>
			    <td height="25" width="52">
                  <div align="left"><b>

					<bean:write  name="frmFinancialEntityDetailsForm" property="lastUpdate" scope="request" />

					<html:hidden property="lastUpdate" />
					<html:hidden property="lastUpdateTimestamp" />

				</b></div>
			</td>
			    <td height="25" width="210">
                  <div align="left"><bean:message key="financialEntityDetails.label.updateUser"/></div>
			</td>
			    <td  height="25" width="11" >:</td>
			    <td height="25" width="235">
                  <div align="left"><b>
				<bean:write  name="frmFinancialEntityDetailsForm" property="lastUpdatedUser" scope="request" />
			</b></div>
			</td>
		</tr>
		<tr bgcolor="#F7EEEE"  >
			<td width="109" height="25">
				<div align="left">&nbsp;<bean:message key="financialEntityDetails.label.sequenceNo"/></div>

			</td>
			<td width="7" height="25">:</td>
			    <td width="52" height="25">
                  <div align="left"><b>
					<bean:write  name="frmFinancialEntityDetailsForm" property="sequenceNum" scope="request" />
				</b></div>
			</td>
			    <td width="210" height="25">
                  <div align="left">&nbsp;</div>
			</td>
			    <td width="11" height="25">
                  <div align="left">&nbsp;</div>
			</td>
			    <td width="235" height="25">
                  <div align="left">&nbsp;</div>
			</td>
			</tr>
			</logic:equal>
	--%>
			<%-- Finished hiding of controls that are not required for creating
					a new Financial Entity
			--%>
		<!-- hide information that is required to edit/add Financial entity -->
		<!-- Since lastUpdate and lastUpdateTimestamp are commented out above for this release, and the information is needed by the stored procedure, include them here.-->
		<!-- End added hidden fields.  -->
			<html:hidden property="actionType" />
			<html:hidden property="userName" />
			<html:hidden property="lastUpdatedUser" />
			<html:hidden property="sequenceNum" />
			<html:hidden property="number" />
			<html:hidden property="actionFrom" />
		</table>
	</tr>
	<tr>
		<td height="23" colspan="2">&nbsp;
	</tr>
	<tr>
		<td height="23" colspan="2">
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				valign="top">
				<tr>
					<td background="<bean:write name="ctxtPath"/>/images/tabback.gif">
					 <table border=0 cellpadding=0 cellspacing=0>
						<tr>
							<td> <img src="<bean:write name="ctxtPath"/>/images/certification.gif" width="120" height="24"></td>
						</tr>
					 </table>
					</td>
				</tr>
			</table>
			<!-- Show all  Certificates if available-->
			<table width="100%" border="0" cellspacing="1" cellpadding="5" align="right">
				<tr>
					<td colspan="2" height="5"></td>
				</tr>
				<tr bgcolor="#CC9999">
					<td width="450" height="25" bgcolor="#CC9999">
					<div align="center"><font color="#FFFFFF"><bean:message key="financialEntityDetails.question.question"/></font></div>
					</td>
					<td width="128" height="25">
					<div align="center"><font color="#FFFFFF"><bean:message key="financialEntityDetails.question.answer"/></font></div>
					</td>
				</tr>
				<!-- Certificate Details (Question,descritpion and answer) of Financial Entity -->
				<% //Look for preselected information, if available please show them
				     String[] preSelectedQuestions=(String[])request.getAttribute("selectedQuestions");
				     String[] preSelectedAnswers=(String[])request.getAttribute("selectedAnswers");
					int certificateIndex=0;
				%>
				<logic:present name="collEntityCertDetails" scope="session" >
					<logic:iterate id="certificateDetails" name="collEntityCertDetails" type="edu.mit.coeus.coi.bean.CertificateDetailsBean">

				<tr bgcolor='<%=( (certificateIndex) % 2 == 0 ? "#FBF7F7" : "#F7EEEE") %>'>
					<td width="450" height="25" valign="top">
						<div align="justify">
						<%--
						As struts1.0.2 does not support the dynamic properties generation
						it opted hidden variable approach in regular MVC way, Seems there
						will be solution in 1.1(but still beta)
						--%>
						<%
						String certCode = certificateDetails.getCode();
						String certQuestionDesc = certificateDetails.getQuestion();
						String totalAnswers = certificateDetails.getNumOfAns();
						String certAnswer = certificateDetails.getAnswer();

						String certSeqNum = certificateDetails.getSeqNumber();
						if( (certSeqNum==null) || (certSeqNum.equals("null") ) ){
							certSeqNum = "1";
						}
						String ansDesc = "";

					//get the preselected question
						String  preSelectedQuestionCode =null;

						if( (preSelectedQuestions!=null)  &&  (preSelectedQuestions.length>0) ){
							preSelectedQuestionCode=preSelectedQuestions[certificateIndex];
						}
						//get preselected answer
						String  preSelectedAnswerCode = null;
						if( (preSelectedAnswers!=null) && (preSelectedAnswers.length>0 )  ){
							preSelectedAnswerCode=preSelectedAnswers[certificateIndex];
						}

						if(preSelectedQuestionCode!=null){
							 certCode = preSelectedQuestionCode;
						 }
						 if(preSelectedAnswerCode!=null){
							certAnswer = preSelectedAnswerCode;
						 }
				    	%>
						 <INPUT type='hidden' name='hdnQuestionID' value='<%= certCode %>'>
						 <INPUT type='hidden'  name='hdnQuestionDesc' value='<%= certQuestionDesc %>'>
                         <INPUT type='hidden'  name='hdnQSeqNum' value='<%= certSeqNum %>'>

						<bean:write name="certificateDetails" property="question" />
						<a href="JavaScript:openwin('<bean:write name="ctxtPath"/>/question.do?questionNo=<bean:write name="certificateDetails" property="code" />','question');" method="POST">
						<bean:message key="financialEntityDetails.question.explanationLink" />
						</a>
						</div>
					</td>
					<td width="128" height="25" valign="top">
					<div align="justify"> <font face="Verdana, Arial, Helvetica, sans-serif">

					<input type="radio" name="<%= certCode %>" <%= (certAnswer!=null && certAnswer.equals("Y"))?"checked":""  %> value="Y" >
						  Yes
				    <input type="radio" name="<%= certCode %>" <%= (certAnswer!=null && certAnswer.equals("N"))?"checked":""  %>  value="N">
						  No
					<% if(totalAnswers!=null && Integer.parseInt(totalAnswers)>2){	%>
	 			   <input type="radio" name="<%= certCode %>" <%= (certAnswer!=null && certAnswer.equals("X"))?"checked":""  %> value="X">
	 					 N/A
                   <% }
                   		certificateIndex++;
                   %>


					</font></div>
					</td>
				</tr>
				</logic:iterate>
				</logic:present>
				 <INPUT type='hidden' name='hdnNumOfQs' value='<%= certificatesSize %>' >
				<tr>
					<td colspan="2" height="40">
					<div align="right">&nbsp; &nbsp; &nbsp; &nbsp; </div>
					</td>
				</tr>
			</table>
			<%-- If no certificates are available for this Financial Entity then show the status --%>
		   <logic:equal 	name="certificatesSize" value="0" >
		   <table>
			<tr>
				<div align='center'><td colspan='3' height="23" align=center>
				<bean:message key="financialEntityDetails.question.noresults"/>
				</td></div>
			</tr>
		</table>
		</logic:equal>
		<!-- End of Certificates -->

	</tr>
	<tr align="right">
		  <td height="23" width="100%">
            <div align="right"><a href="JavaScript:history.back();">
			<img src="<bean:write name="ctxtPath"/>/images/goback.gif" width="55" height="22" border="0"></a>
				<html:image page="/images/submit.gif"  border="0" />
				</a></div>
		</td>
		  <td height="23" width="0%">&nbsp;</td>
	</tr>
	</table>
  </td>
  </tr>
  </html:form>
</table>