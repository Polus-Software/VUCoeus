<%--
/*
 * @(#)AddCOIDisclosureContent.jsp	1.0 2002/06/12 01:32:13 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  RaYaKu
 */
--%>
<%-- This JSP file is for Adding a new COI Disclosure for a person--%>

<%@page import="edu.mit.coeus.coi.bean.CertificateDetailsBean,
		edu.mit.coeus.coi.bean.DisclosureInfoBean,
		edu.mit.coeus.coi.bean.ComboBoxBean,
                java.net.URLEncoder,
		java.util.HashMap,
		java.util.Set,
		java.util.Iterator,
		org.apache.struts.action.Action" 
	errorPage="ErrorPage.jsp" %>
	
<%@ taglib uri="/WEB-INF/struts-html.tld"     prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld"     prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld"     prefix="logic" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"     prefix="coeusUtils" %>

<%@ include file="CoeusContextPath.jsp"  %>

<!-- CASE #1374 All values are in actionform. DisclosureHeaderBean not needed -->
<%--<jsp:useBean id="disclosureHeaderBean" scope='session' class="edu.mit.coeus.coi.bean.DisclosureHeaderBean"/>--%>
<jsp:useBean id="collCOIDiscCertDetails" scope='session' class="java.util.Vector"/>
<jsp:useBean id="collCOIDisclosureInfo" scope='session' class="java.util.Vector"/>
<!-- CASE #1374 get coi status collection from session -->
<jsp:useBean  id="collCOIStatus" scope="session" class="java.util.LinkedList" />
<jsp:useBean id="personId" scope ="request" class = "java.lang.String" />
<bean:size id="coiDiscCertificatesSize" name="collCOIDiscCertDetails" />
<bean:size id="collCOIDisclosureInfoSize" name="collCOIDisclosureInfo"/>


 <table width="100%" border="0" cellpadding="0" cellspacing="0">
 <tr>
<td background="<bean:write name="ctxtPath"/>/images/separator.gif" width="1">
    <img src="<bean:write name="ctxtPath"/>/images/separator.gif" width="1">
</td>
<td width="657" valign="top">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<html:form action="/addNewCOIDisclosure.do" scope="session" >
	<html:hidden name="frmAddCOIDisclosure" property="disclosureNo" />
	<html:hidden name="frmAddCOIDisclosure" property="disclosureTypeCode" />
	<html:hidden name="frmAddCOIDisclosure" property="appliesToCode" />
    	<html:hidden name="frmAddCOIDisclosure" property="personId"/>
	<html:hidden name="frmAddCOIDisclosure" property="userName" />
	<html:hidden name="frmAddCOIDisclosure" property="accountType" value="I"/> <!-- Set to  I- Insert -->
	<!-- CASE #1374 Begin -->
	<html:hidden name="frmAddCOIDisclosure" property="personFullName" />
	<!-- CASE #1374 End -->
	<!-- CASE #1374 Comment Begin -->
	<!-- action form is stored in session, and values are populated in action class -->
	<%--	
	<html:hidden name="frmAddCOIDisclosure" property="personId" value="<%=personId%>" />
	<html:hidden name="frmAddCOIDisclosure" property="statusSeqNum" value="1"/>
	<html:hidden name="frmAddCOIDisclosure" property="seqNum" value="1"/>
	--%>
	<!-- CASE #1374 Comment End -->
<!-- insert AddCOIDisclosureContentInsert1.jsp -->
<jsp:include page="AddCOIDisclosureContentInsert1.jsp" flush="true"/>
<!-- end insert -->
<!--
	  <tr>
		<td height="23">&nbsp;</td>
	  </tr>
-->	  
	  <tr>
		<td height="23">
		  <table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
			  <td background="<bean:write name='ctxtPath'/>/images/tabback.gif">
				<table border=0 cellpadding=0 cellspacing=0>
				  <tr>
					<td> <img src="<bean:write name='ctxtPath'/>/images/certification.gif" width="120" height="24"></td>
				  </tr>
				</table>
			  </td>
			</tr>
		  </table>
		<!-- COI DIsclosure Certificates Information -->

		  <table width="100%" border="0" cellspacing="1" cellpadding="5" >
			<tr>
			  <td colspan="2" height="5"></td>
			</tr>
			<tr bgcolor="#CC9999">
			  <td width="450" height="25" bgcolor="#CC9999">
				<div align="center"><font color="#FFFFFF"><bean:message key="addCOIDisclosure.label.question"/></font></div>
			  </td>
			  <td width="135" height="25">
				<div align="right"><font color="#FFFFFF"><bean:message key="addCOIDisclosure.label.answer"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="javascript:opensmallwin('<%=request.getContextPath()%>/web/certQuestHelp.html','addDiscl');"><bean:message key="addCOIDisclosure.label.help"/></a>&nbsp;&nbsp;&nbsp;</font></div>
			  </td>
			  <!-- CASE #1393 Begin -->
			</tr>
			<logic:present name="<%=Action.ERROR_KEY%>">
			<tr>
			<td colspan="2" >
			<font color="red">
			<html:errors property="answerRequired"/>
			</font>
			</td>
			</tr>
			</logic:present>
			<!-- CASE #1393 End -->
			<%
			/*
			 * Struts1.0.2 does not support dynamic properties feature, so adopted MVC way, looking to move
			 * code to Taghandler class.
			 */
			 //CASE #1374 Get preselected values of questions and answers from session instead of request.
			// get the previously selected values of questions and answers if available then display them
			String preSelectedQuestions[] =(String[])session.getAttribute("selectedQuestions");
			String preSelectedAnswers[] =(String[])session.getAttribute("selectedAnswers");
			//get certificates.
			for(int certificateCount = 0 ; certificateCount < collCOIDiscCertDetails.size() ; certificateCount++){

				CertificateDetailsBean certificateDetailsBean = null;
				if( collCOIDiscCertDetails.elementAt(certificateCount) instanceof CertificateDetailsBean ){
					certificateDetailsBean = (CertificateDetailsBean) collCOIDiscCertDetails.elementAt(certificateCount);
				}
				String certCode = certificateDetailsBean.getCode();
				String certDesc = certificateDetailsBean.getQuestion();
				String numOfAns = certificateDetailsBean.getNumOfAns();
				String question =certificateDetailsBean.getQuestion();
				/*CASE #1393 Begin */
				String answer = certificateDetailsBean.getAnswer();
				String correspEntQuestLabel = certificateDetailsBean.getCorrespEntQuestLabel();
				String questionLabel = certificateDetailsBean.getLabel();
				/*CASE #1393 End */

				String checkedYes="";
				String checkedNo="";
				String checkedNA="";

				//get the preselected question
				String  preSelectedQuestionCode =null;

				/*if(  (preSelectedQuestions!=null) && (preSelectedQuestions.length>0 )  ){
					preSelectedQuestionCode=preSelectedQuestions[certificateCount];
				}*/

				String  preSelectedAnswerCode = null;
				if( (preSelectedAnswers!=null) && (preSelectedAnswers.length>0 ) ){
					preSelectedAnswerCode=preSelectedAnswers[certificateCount];
				}

				 if(preSelectedAnswerCode!=null){
					 if(preSelectedAnswerCode.equals("Y") ){
						 checkedYes="checked";
					 }

					 if(preSelectedAnswerCode.equals("N") ){
					 	checkedNo="checked";
					 }

					 if(preSelectedAnswerCode.equals("X") ){
						checkedNA="checked";
					 }
				 }
				 /*CASE #1393 Begin*/
				 else if(answer != null){
				 	if(answer.equals("Y") ){
				 		checkedYes="checked";
				 	}
				 	else if(answer.equals("N") ){
				 		checkedNo="checked";
				 	}
				 	if(answer.equals("X") ){
				 		checkedNA="checked";
				 	}
				 }
				 /* CASE #1393 End*/
				 System.out.println("#####answer: "+answer+"##### preSelectedAnswerCode: "+preSelectedAnswerCode);
				 System.out.println("checkedYes: "+checkedYes);
				 System.out.println("checkedNo: "+checkedNo);

			%>
			<tr bgcolor='<%=( certificateCount % 2 == 0 ? "#FBF7F7" : "#F7EEEE") %>' valign="top">
			  <td width="450" height="25">
				<INPUT type='hidden'  name='hdnQuestionID' value='<%= certCode%>' >
				<INPUT type='hidden'  name='hdnQuestionDesc' value='<%= certDesc%>'>
				<INPUT type='hidden'  name='hdnQSeqNum' value='1'>
			<!-- CASE #1393 Begin -->
			 <logic:present name="certQuestionErrors">
			 <%
			 	System.out.println("certQuestionErrors present");
				String errorCertCode = null;
				Object errorObject = null;
				HashMap entitiesWithYes = null;
				String errorCode = null;
				String entityNumber = null;
				String entityName = null;	
			 	
			 	HashMap certQuestionErrors = (HashMap)request.getAttribute("certQuestionErrors");
				Iterator keysIterator = certQuestionErrors.keySet().iterator();

				//Loop through errors to get any that pertain to this cert question.
				while(keysIterator.hasNext()){

					entitiesWithYes = null;
					errorCode = null;	         	
					errorCertCode = (String)keysIterator.next();

					if(errorCertCode != null &&  errorCertCode.equals(certCode)){
						errorObject = certQuestionErrors.get(errorCertCode);
						if(errorObject.getClass().getName().equals("java.util.HashMap")){
							entitiesWithYes = (HashMap)certQuestionErrors.get(errorCertCode);
							//System.out.println("entitiesWithYes.size(): "+entitiesWithYes.size());
						}
						else{
							errorCode = certQuestionErrors.get(errorCertCode).toString();
							//System.out.println("errorCode: "+errorCode);
						}
					}	
					//If error is because user answered No, and has one or more entities
					//with answer Yes, get the list of entities with Yes answer.
					if(entitiesWithYes != null){
						if(entitiesWithYes.size() == 1){
							//System.out.println("entitiesWithYes.size() == 1");
				  %>
							<font color="red">
							 <bean:message key="error.addCOIDisclosure.entYesAnsNoForOne"/>
							<%=correspEntQuestLabel%>
							 <bean:message key="error.addCOIDisclosure.entYesAnsNo2ForOne"/>
							 </font>
				<%
						}
						else{
							System.out.println("entitiesWithYes.size() > 1");
				%>
							<!--<font face="Verdana, Arial, Helvetica, sans-serif" size="1" color="red">-->
							<font color="red">
							 <bean:message key="error.addCOIDisclosure.entYesAnsNoForTwo"/>
							<%=correspEntQuestLabel%>							 
							 <bean:message key="error.addCOIDisclosure.entYesAnsNo2ForTwo"/>
							 </font>				
				<%
						}
						Iterator entities = entitiesWithYes.keySet().iterator();
						for(int cnt = 0; cnt<entitiesWithYes.size(); cnt++){
							entityNumber = (String)entities.next();
							entityName = (String)entitiesWithYes.get(entityNumber);
				  %>
							<a href="<bean:write name='ctxtPath'/>/editFinEntity.do?actionFrom=addDiscl&entityNo=<%=entityNumber%>"> <%=entityName%></a>
				  <%
							if(cnt<(entitiesWithYes.size()-1)){
								out.print(",");
							}
						}
						
						if(entitiesWithYes.size() == 1){
				  %>
							<!--<font face="Verdana, Arial, Helvetica, sans-serif" size="1" color="red">-->
							<font color="red">
							 <bean:message key="error.addCOIDisclosure.entYesAnsNo3ForOne"/>
							<%=correspEntQuestLabel%>
							<bean:message key="error.addCOIDisclosure.entYesAnsNo4ForOne"/>							 
							</font>
				<%
						}
						else{
				%>
							<!--<font face="Verdana, Arial, Helvetica, sans-serif" size="1" color="red">-->
							<font color="red">
							 <bean:message key="error.addCOIDisclosure.entYesAnsNo3ForTwo"/>
							<%=correspEntQuestLabel%>							 
							<bean:message key="error.addCOIDisclosure.entYesAnsNo4ForTwo"/>							 
							 </font>				
				<%
						}		

					}
					//User has answered Yes but has no financial entities
					else if(errorCode != null && errorCode.equals("-1")){

				%>
						<!--<font face="Verdana, Arial, Helvetica, sans-serif" size="1" color="red">-->
						<font color="red">
						 <bean:message key="error.addCOIDisclosure.noFinEntAnsReqExpl"/>
						 </font>			
				<%
					}
					//Error code -2: User answered Yes, but no fin ent with answer Yes to this question.
					else if(errorCode != null && errorCode.equals("-2")){
						//System.out.println("errorCode == -2");
				%>
						<!--<font face="Verdana, Arial, Helvetica, sans-serif" size="1" color="red">-->
						<font color="red">
						<bean:message key="error.addCOIDisclosure.entNoAnsYes"/>
						<%=correspEntQuestLabel%>							 
						<bean:message key="error.addCOIDisclosure.entNoAnsYes2"/>						
						<a href="<bean:write name="ctxtPath"/>/getFinEntities.do?actionFrom=addDiscl">
						<bean:message key="error.addCOIDisclosure.entNoAnsYes3"/>
						</a>
						 </font>							
						<bean:message key="error.addCOIDisclsoure.entNoAnsYes4"/>
				<%
					}
				}
				%>                	
		       </logic:present>
			
			<!-- CASE #1393 End -->
				<div align="justify"><font face="Verdana, Arial, Helvetica, sans-serif">
					<!-- CASE #1393 add question label -->
					<b><bean:message key="viewCOIDisclosureDetails.questionLabel"/>&nbsp;
					<%=certificateDetailsBean.getLabel()%>:</b>
					<%=question%>
					<a href="JavaScript:openwin('<bean:write name="ctxtPath"/>/question.do?questionNo=<%= certCode %>','question');">
					<bean:message key="addCOIDisclosure.label.explanationLink" />
					</a>
					</font>
				</div>
			  </td>
			  <td width="135" height="25" align="center">
				<div >
				<!-- CASE #1374 Add * for required field -->
				<font color="red"><b>*</b></font>
					<%--<%=checkedYes%>--%>
				  <input type="radio" name="<%= certCode %>"  value="Y" <%=checkedYes%>  >
					<bean:message key="addCOIDisclosure.label.yes"/>
					<%--<%=checkedNo%>--%>
				  <input type="radio" name="<%= certCode %>"  value="N"   <%=checkedNo%>>
					<bean:message key="addCOIDisclosure.label.no"/>
					<% if(numOfAns!=null && Integer.parseInt(numOfAns)>2){ %>
					  <input type="radio" name="<%= certCode %>"  value="X" <%=checkedNA%>>
						<bean:message key="addCOIDisclosure.label.na"/>
					<%}%>
			  </div>
			 </td>
			</tr>
			<% }	%>
				<INPUT type='hidden' name='hdnNumOfQs' value='<bean:write name="coiDiscCertificatesSize"/>' >
			<tr>
			  <td height="10">&nbsp;</td>
			  <td height="10">&nbsp;</td>
			</tr>
			<!-- if there are no certificates available then show the below information -->
			<logic:equal  name="coiDiscCertificatesSize" value="0" >
			<tr><td colspan='2' height="23">No Questions found</td></tr>
			</logic:equal>
		  </table>
		  <!-- End of COI Certificate Details -->


		</td>
	  </tr>
	  <!-- CASE #864 Comment Begin -->
	  <!--
	  <tr>
		<td height="23">&nbsp;</td>
	  </tr>
	  -->
	  <!-- CASE #864 Comment End -->
	  <tr>
		<td height="23">
		  <table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
			  <td background="<bean:write name='ctxtPath'/>/images/tabback.gif">
				<table border=0 cellpadding=0 cellspacing=0>
				  <tr>
					<td><img src="<bean:write name='ctxtPath'/>/images/finEntDisc.gif" width="167" height="24"></td>
				  </tr>
				</table>
			  </td>
			</tr>
		  </table>
		   <!--Entity Information begins -->
<jsp:include page="AddCOIDisclosureContentInsert2.jsp" flush="true"/>
		</td>
	  </tr>
	  <tr>
		<td height="40">
		  <div align="right">
		  <!-- CASE #665 Comment Begin -->
		  <%--<a href="JavaScript:history.back();"><img src="<bean:write name='ctxtPath'/>/images/goback.gif" width="55" height="22" border="0"></a>&nbsp;--%>
		  <!-- CASE #665 Comment End -->
		  <logic:notEqual  name="coiDiscCertificatesSize" value="0" >

				<html:image page="/images/submit.gif" border="0"/>
		  </logic:notEqual>
			  <%-- <html:image src="images/submit.gif" border="0"/> --%>
			&nbsp; &nbsp; &nbsp; </div>
		</td>
	  </tr>
	</html:form>
  </table>
</td>
</tr>
</table>
<!-- CASE #1374 Begin -->
<!-- Show response window to confirm ent added or modified -->
<% if(request.getAttribute("FESubmitSuccess") != null){
	request.removeAttribute("FESubmitSuccess");
	String message = (String)request.getAttribute("entityName");
	String actionType = (String)request.getAttribute("actionType");
	if(actionType != null){
		if(actionType.equals("I")){
			message += " successfully added.";
		}
		else if(actionType.equals("U") ){
			message += " successfully modified.";
		}
	}
	int indexOfApost = message.indexOf("'");
	if(indexOfApost != -1){
		int indexOfApostIncr = indexOfApost+1;
		int endOfMessage = message.length();
		String message1 = message.substring(0, indexOfApost);
		String message2 = message.substring(indexOfApostIncr, message.length());
		message = message1+message2;
	}	
	out.print("<script language='javascript'>");
	out.print("confirm('"+message+"');");
	out.print("</script>");
}

%>
<!-- CASE #1374 End -->
