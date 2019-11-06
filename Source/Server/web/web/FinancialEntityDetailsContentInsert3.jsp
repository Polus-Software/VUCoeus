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
<%-- Find the size of collEntityCertDetails --%>
<bean:size id="certificatesSize" name="collEntityCertDetails" />

	<!-- Show all  Certificates if available-->
	<table width="100%" border="0" cellspacing="1" cellpadding="5" align="right">
		<tr>
			<td colspan="2" height="5"></td>
		</tr>
		<tr bgcolor="#CC9999">
			<td width="450" height="25" bgcolor="#CC9999">
			<div align="center"><font color="#FFFFFF">
			<bean:message key="financialEntityDetails.question.question"/></font></div>
			</td>
			<td width="128" height="25">
			<div align="center"><font color="#FFFFFF">
			<bean:message key="financialEntityDetails.question.answer"/></font></div>
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

				<!-- CASE #1393 Begin -->
				<b>
				<bean:message key="viewFinEntity.label.question"/>
				<bean:write name="certificateDetails" property="label"/>
				</b>
				:
				<!-- CASE #1393 End -->
				<bean:write name="certificateDetails" property="question" />
				<a href="JavaScript:openwin('<bean:write name="ctxtPath"/>/question.do?questionNo=<bean:write name="certificateDetails" property="code" />','question');" method="POST">
				<bean:message key="financialEntityDetails.question.explanationLink" />
				</a>
				</div>
			</td>
			<td width="128" height="25" valign="top">
			<div align="justify">
			<font class="smallred"><b>*</b></font>
			<font face="Verdana, Arial, Helvetica, sans-serif">

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
		<!-- CASE #397 Begin -->
		<logic:equal name="certificatesSize" value="0" >
			<tr>
				<td colspan='3' height="23" align=center>
				<div>
				<bean:message key="financialEntityDetails.question.noresults"/>
				</div>
				</td>
			</tr>
		</logic:equal>
		<!-- CASE #397 End -->
	</table>
	<!-- CASE #397 Comment Begin -->
	<%-- If no certificates are available for this Financial Entity then show the status --%>
	<%--
   <logic:equal name="certificatesSize" value="0" >
   <table>
	<tr>
		<div align='center'><td colspan='3' height="23" align=center>
		<bean:message key="financialEntityDetails.question.noresults"/>
		</td></div>
	</tr>
</table>
</logic:equal>
--%>
<!-- End of Certificates -->