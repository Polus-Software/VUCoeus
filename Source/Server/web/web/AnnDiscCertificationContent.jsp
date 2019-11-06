<%--
/*
 * @(#)AnnDiscCertificationContent.jsp	5/17/2004
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  coeus-dev-team
 */
--%>

<!-- Answers to the certification questions will
be validated against answers user has given to certification questions for
any disclosed financial entities.-->

<%@page import="edu.mit.coeus.coi.bean.CertificateDetailsBean,
		edu.mit.coeus.bean.PersonInfoBean,
		org.apache.struts.action.Action,
		java.util.HashMap,
		java.util.Iterator"
	errorPage="ErrorPage.jsp" %>
<%@ taglib uri="/WEB-INF/struts-html.tld"     prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld"     prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld"     prefix="logic" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"     prefix="coeusUtils" %>

<%@ include file="CoeusContextPath.jsp"  %>

<jsp:useBean id="annDiscCertQuestions" scope="session" class="java.util.Vector"/>
<jsp:useBean id="personId" scope ="request" class = "java.lang.String" />
<jsp:useBean id="personInfo" class="edu.mit.coeus.bean.PersonInfoBean" scope="session"/>
<bean:size id="annDiscCertQuestionsSize" name="annDiscCertQuestions" />
<!-- CASE #1393 Begin -->
<jsp:useBean id="activeFinEntities" class="java.util.Vector" scope="session"/>
<!-- CASE #1393 End -->

<html:form action="/annDiscCertificationValidation" name="frmAnnDiscCertification"
type="edu.mit.coeus.action.coi.AnnDiscCertificationActionForm">

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
		<bean:message key="annDiscCertification.header"/>
		<bean:write name="personInfo" property="fullName"/>
		</td>
          </tr>
          <tr>
            <td>
              <table width="100%" border="0" cellspacing="0" cellpadding="5">
                <tr>
                  <td>
                  <!-- CASE #1393 Comment Begin -->
                  <%--
			<html:errors />
			<!-- For error message that needs dynamic content of fin ent info -->
			<logic:present name="errorEntityName">

				<bean:message key="error.annDiscCertification.entYesAnsNo1"/>

     				<a href="<bean:write name='ctxtPath'/>/viewFinEntity.do?actionFrom=AnnualFE&entityNo=<%=request.getAttribute("errorEntityNumber")%>&seqNo=<%=request.getAttribute("errorEntitySeqNo")%>"> <%=request.getAttribute("errorEntityName")%>.</a>

				<bean:message key="error.annDiscCertification.entYesAnsNo2"/>
			</logic:present>
		--%>
		<!-- CASE #1393 Comment End -->
		<!-- CASE #1393 Begin -->
			<logic:present name="<%=Action.ERROR_KEY%>">
				<b><bean:message key="validationErrorHeader"/></b>
				<bean:message key="validationErrorSubHeader2"/>
				</font>
			</logic:present>		
		<!-- CASE #1393 End -->
		 </td>
		 </tr>
		 <tr>
		 <td>
			<div>
			<!--<font color="#663300" face="Verdana, Arial, Helvetica, sans-serif" size="2">-->
			<font class="fontBrown">
			<b>
			<!-- CASE #1374 Vary the instructions based on whether user has active financial entities -->
				<logic:equal name="frmAnnDiscCertification" property="hasActiveEntities" value="true">
					<bean:message key="annDiscCertification.content"/>
				</logic:equal>
				<logic:notEqual name="frmAnnDiscCertification" property="hasActiveEntities" value="true">
					<bean:message key="annDiscCertification.contentNoFinEnt"/>
				</logic:notEqual>			
			 </b>
			 </font>
			</div>
                  </td>
                </tr>
              </table>
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
			<!-- Begin Certificates Information -->

			  <table width="100%" border="0" cellspacing="1" cellpadding="5" >
				<tr>
				  <td colspan="2" height="5"></td>
				</tr>
				<tr bgcolor="#CC9999">
				  <td width="450" height="25" bgcolor="#CC9999">
					<div align="center"><font color="#FFFFFF"><bean:message key="addCOIDisclosure.label.question"/></font></div>
				  </td>
				  <td width="135" height="25">
					<div align="center"><font color="#FFFFFF"><bean:message key="addCOIDisclosure.label.answer"/></font></div>
				  </td>
				</tr>


				<%
				for(int certificateCount = 0 ; certificateCount < annDiscCertQuestions.size() ; certificateCount++){

					CertificateDetailsBean certificateDetailsBean = null;
					if( annDiscCertQuestions.elementAt(certificateCount) instanceof CertificateDetailsBean ){
						certificateDetailsBean = (CertificateDetailsBean) annDiscCertQuestions.elementAt(certificateCount);
					}
					String certCode = certificateDetailsBean.getCode();
					String certDesc = certificateDetailsBean.getQuestion();
					String numOfAns = certificateDetailsBean.getNumOfAns();
					//the answer populated by stored procedure based on answers to cert questions for fin //entities, or, if page is being reloaded after validation failure, the user selected
					//answers to the questions.
					String answer = certificateDetailsBean.getAnswer();
					String correspEntQuestLabel = certificateDetailsBean.getCorrespEntQuestLabel();

					String checkedYes="";
					String checkedNo="";
					String checkedNA="";

					// Set the cert questions to the appropriate values, based on user answers to cert //questions for any disclosed financial entities.

					if(answer != null){
						if(answer.equals("Y") ){
							checkedYes="checked";
						 }

						 else if(answer.equals("N") ){
							checkedNo="checked";
						 }

						 else if(answer.equals("X") ){
							checkedNA="checked";
						 }
					}

				%>

				
					<tr bgcolor='<%=( certificateCount % 2 == 0 ? "#FBF7F7" : "#F7EEEE") %>' valign="top">
					  <td width="450" height="25">
			<!-- CASE #1393 Begin -->
			
			 <logic:present name="certQuestionErrors" scope="session">
			 <%
			 	System.out.println("certQuestionErrors present");
				String errorCertCode = null;
				Object errorObject = null;
				HashMap entitiesWithYes = null;
				String errorCode = null;
				String entityNumber = null;
				String entityName = null;	
			 	
			 	HashMap certQuestionErrors = (HashMap)session.getAttribute("certQuestionErrors");
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
							<a href="<bean:write name='ctxtPath'/>/editFinEntity.do?actionFrom=annDiscCert&entityNo=<%=entityNumber%>"> <%=entityName%></a>
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
						 <bean:message key="error.annDiscCertification.noFinEntAnsReq"/>
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
						<a href="<bean:write name="ctxtPath"/>/getAnnDiscPendingFEs.do?actionFrom=annDiscCert">
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
						<INPUT type='hidden'  name='hdnQuestionID' value='<%= certCode%>' >
						<div align="justify"><font face="Verdana, Arial, Helvetica, sans-serif">
							<b><bean:message key="viewCOIDisclosureDetails.questionLabel"/>
							<%=certificateDetailsBean.getLabel()%>:</b>
							<%=certDesc%>
							<a href="JavaScript:openwin('<bean:write name="ctxtPath"/>/question.do?questionNo=<%= certCode %>','question');">
							<bean:message key="addCOIDisclosure.label.explanationLink" />
							</a>
							</font>
						</div>
					  </td>
					  <td width="135" height="25" align="center">
						<div ><font class="smallsize">
						  <input type="radio" name="<%= certCode %>"  value="Y" <%=checkedYes%>  >
							<bean:message key="addCOIDisclosure.label.yes"/>
						  <input type="radio" name="<%= certCode %>"  value="N"   <%=checkedNo%>>
							<bean:message key="addCOIDisclosure.label.no"/>
							<% if(numOfAns!=null && Integer.parseInt(numOfAns)>2){ %>
							  <input type="radio" name="<%= certCode %>"  value="X" <%=checkedNA%>>
								<bean:message key="addCOIDisclosure.label.na"/>
							<%}%>
					  </font></div>
					 </td>
					</tr>
				<% }	%>
					<INPUT type='hidden' name='hdnNumOfQs' value='<bean:write name="annDiscCertQuestionsSize"/>' >

				<!-- if there are no certificates available then show the below information -->
				<logic:equal  name="annDiscCertQuestionsSize" value="0" >
				<tr><td colspan='2' height="23"><div>No Questions found</div></td></tr>
				</logic:equal>
			  </table>
			  <!-- End of Certificates Information -->


			</td>
		  </tr>
		  <tr>
			<td height="23">&nbsp;</td>
		  </tr>
		  <tr>
			<td height="30">
			  <div align="right">
			  <a href="JavaScript:history.back();"><img src="<bean:write name='ctxtPath'/>/images/goback.gif" width="55" height="22" border="0"></a>&nbsp;
			  <logic:notEqual  name="annDiscCertQuestionsSize" value="0" >

					<html:image page="/images/submit.gif" border="0"/>
			  </logic:notEqual>
				&nbsp; &nbsp; &nbsp; </div>
			</td>
		  </tr>
		  </table>
		</td>
	</tr>
	</table>

</html:form>

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