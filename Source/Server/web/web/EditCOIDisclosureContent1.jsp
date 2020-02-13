<%--
/*
 * @(#) EditCOIDisclosureContent1.jsp 1.0 2002/06/05
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
		edu.mit.coeus.coi.bean.CertificateDetailsBean,
		edu.mit.coeus.coi.bean.DisclosureInfoBean,
                java.net.URLEncoder,
                java.util.HashMap,
                java.util.Set,
                java.util.Iterator,
                org.apache.struts.action.Action" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" 	prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" 	prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" 	prefix="logic" %>
<%@ taglib uri="/WEB-INF/privilege.tld" prefix="priv" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"	prefix="coeusUtils" %>
<%@ include file="CoeusContextPath.jsp"  %>

<jsp:useBean  id="action" scope="session" class="java.lang.String" />
<jsp:useBean  id="disclosureNo" scope="session" class="java.lang.String" />
<jsp:useBean  id="discHeaderAwPrInfo" 	scope="session" class="edu.mit.coeus.coi.bean.DisclosureHeaderBean" />
<jsp:useBean  id="collCOICertDetails" 	scope="session" class="java.util.Vector" />
<bean:size id="collCOICertDetailsSize"   name="collCOICertDetails" />
<jsp:useBean  id="moduleCode" 	scope="session" class="java.lang.String" />
<jsp:useBean  id="disclosureHeader" scope="session" class="edu.mit.coeus.coi.bean.DisclosureHeaderBean" />
<jsp:useBean  id="collCOIStatus" scope="session" class="java.util.LinkedList" />
<jsp:useBean id = "userprivilege" class = "java.lang.String" scope="session"/>
<!-- CASE #1374 Begin -->
<jsp:useBean id="collProposalTypes" scope="session" class="java.util.Vector"/>
<jsp:useBean id="frmAddCOIDisclosure" scope="session" class="edu.mit.coeus.action.coi.AddCOIDisclosureActionForm"/>

<table width="100%" cellpadding="0" cellspacing="0" border="0">
 <tr>
  <td background="<bean:write name="ctxtPath"/>/images/separator.gif" width="1">
    <image src="<bean:write name="ctxtPath"/>/images/separator.gif" width="1">
  </td>

  <td width="645">
  	<html:form action="/editCOIDisclosure.do" >
  	
      <!-- show the information in hidden that are required to edit COI disclsoure -->
      <html:hidden name="frmAddCOIDisclosure" property="disclosureNo" />
      <html:hidden name="frmAddCOIDisclosure" property="userName" />
       <html:hidden name="frmAddCOIDisclosure"  property="accountType" value="U"/>
       <input type='hidden' name='hdnEditDiscl' value='yes'>
       <!-- CASE #1374 Begin -->
       <html:hidden name="frmAddCOIDisclosure" property="disclosureTypeCode" />
       <!-- CASE #1374 End -->
       
  	 <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr><td height="5"></td></tr>
          <tr><td height="23" bgcolor="#cccccc"> &nbsp;
            	<b>
            	<font face="Verdana, Arial, Helvetica, sans-serif" size="2">
				<bean:message key="editCOIDisclosure.header"/>
				<%--<bean:write name="disclosureNo" scope="request"/>--%>
				<bean:write name="disclosureHeader" scope="session" property="name"/>
            	</font>
            	</b>
          </td></tr>
          <tr><td>

	  <!-- CASE #1393 Comment Begin -->
	  <%-- 
	  <table width="100%" border="0" cellspacing="0" cellpadding="5">
                <tr><td>                
          	<!-- Show Errors -->
		<html:errors />
		<!-- For error message that needs dynamic content of fin ent info -->
		<logic:present name="errorEntityName">

			<bean:message key="error.addCOIDisclosure.entYesAnsNo1"/>

			<a href="<bean:write name='ctxtPath'/>/viewFinEntity.do?actionFrom=coiFinEntity&entityNo=<%=request.getAttribute("errorEntityNumber")%>&seqNo=<%=request.getAttribute("errorEntitySeqNo")%>"> <%=request.getAttribute("errorEntityName")%>.</a>

			<bean:message key="error.addCOIDisclosure.entYesAnsNo2"/>
		</logic:present>
		</td></tr>
	--%>
	<!-- CASE #1393 Comment End -->	
<!-- CASE #1374 Comment Begin -->
<%--
<!-- If this is a temp proposal, display a link to edit proposal information.  -->
		<logic:present name="showTempProposalEdit" >
		<tr>
		<td>
			<b><font color="red">*</font></b>
			<font class="fontBrown"> indicates required field</font>		
		</td>		
		<td colsapn="2" height="2" align="right"> &nbsp;
			<html:image page="/images/editTempProposal.gif" border="0"  alt="Edit Temporary Proposal" property="Edit Temporary Proposal" /> &nbsp; &nbsp;
			<html:hidden name="disclosureHeader" property="keyNumber" />
			<html:hidden name="frmAddCOIDisclosure" property="showTempProposalEdit" value="true" />
                </td></tr>			
		</logic:present>
--%>
<!-- CASE #1374 Comment End -->
	<!-- CASE #1393 Begin -->
	<table width="100%" border="0" cellspacing="0" cellpadding="5">
		<tr>
		<td colspan="3">
			<b><font color="red">*</font></b>
			<font class="fontBrown"> indicates required field</font>
		</td>
		<td  align="right">
                        <html:image page="/images/submit.gif" border="0" />
                      &nbsp; &nbsp; &nbsp;
                </td>
		</tr>	
		<logic:present name="<%=Action.ERROR_KEY%>">
		<tr>
		<td colspan="4">
			<b><bean:message key="validationErrorHeader"/></b><br>
			<bean:message key="validationErrorSubHeader2"/>
			</font>
		</td>
		</tr>
		</logic:present>
	</table>
	<!-- CASE #1393 End -->
	<table width="100%" border="0" cellspacing="0" cellpadding="5">
			<!-- CASE #1374 Begin -->
		
			<tr>	
				  <td valign="top" width="110">
					<logic:present name="frmAddCOIDisclosure" scope="session">
						<logic:equal name="frmAddCOIDisclosure" scope="session" 
							property="disclosureTypeCode" value="1">
							<bean:message key="addCOIDisclosure.label.awardTitle"/>
						</logic:equal>
						<logic:notEqual name="frmAddCOIDisclosure" scope="session" 
							property="disclosureTypeCode" value="1">
							 <bean:message key="addCOIDisclosure.label.proposalTitle"/>
						</logic:notEqual>
					</logic:present>
					:
				</td>
					<logic:equal name="frmAddCOIDisclosure" scope="session" property="disclosureTypeCode" value="3">
						<td valign="top" width="2">
							<b><font color="red">*</font></b>
						</td>
						<td colspan="2">
							<font color="red">
							<html:errors property="title"/>							</font>
							<textarea name="title" wrap='virtual' cols="40" rows="2" ><bean:write name="frmAddCOIDisclosure" scope="session" property="title"/></textarea>
						</td>
					</logic:equal>
					<logic:notEqual name="frmAddCOIDisclosure" scope="session" 
						property="disclosureTypeCode" value="3">
						<td colspan="3" align="left">
						<%--<b><bean:write name="frmAddCOIDisclosure" scope="session" 
							property="title"/></b>--%>
						<textarea name="title" disabled="true" wrap='virtual' cols="40" rows="2" ><bean:write name='frmAddCOIDisclosure' property='title' scope="session"/></textarea>						
						</td>
					</logic:notEqual>				 
			</tr>
			<logic:equal name="frmAddCOIDisclosure" scope="session" property="disclosureTypeCode" value="3">
			<tr>			
				<td valign="top" width="110">
				<bean:message key="addCOIDisclosure.label.proposalType"/>&nbsp;:&nbsp;
				</td>
				<td valign="top" width="2">
					<b><font color="red">*</font></b>
				</td>
				<td valign="top" colspan="2">
				<font color="red">
				<html:errors property="proposalType"/>
				</font>
				  <select name="typeCode">
				  	<%
				  	String preselectedTypeCode = frmAddCOIDisclosure.getTypeCode();
				  	System.out.println("preselectedTypeCode: "+preselectedTypeCode);
				  	String selected = "";
				  	for(int cnt=0; cnt< collProposalTypes.size(); cnt++){
				  		ComboBoxBean comboBox = (ComboBoxBean)collProposalTypes.get(cnt);
				  		selected = "";
				  		if(comboBox.getCode().equals(preselectedTypeCode)){
				  			selected = "selected";
				  		}
				  	
				  	%>
				  	<option value="<%=comboBox.getCode()%>"<%=selected%>><%=comboBox.getDescription()%></option>
				  	<%
				  	}
				  	%>
				  </select>
				</td>			
			</tr>
			</logic:equal>			
			<tr>			
				<td valign="top">
				<bean:message key="addCOIDisclosure.label.sponsor"/>&nbsp;:&nbsp;
				</td>
				<logic:equal name="frmAddCOIDisclosure" scope="session" 
					property="disclosureTypeCode" value="3">
					<td valign="top">
						<b><font color="red">*</font></b>
					</td>
					<td colspan="2">
						<font color="red">
						<html:errors property="sponsor"/>
						</font>
						<html:text property="sponsorName" size="25" value="<%=frmAddCOIDisclosure.getSponsorName()%>"/>				
					</td>	
				</logic:equal>
				<logic:notEqual name="frmAddCOIDisclosure" scope="session"
					property="disclosureTypeCode" value="3">
					<td colspan="3">
						<%--<b><bean:write name="frmAddCOIDisclosure" scope="session" 
							property="sponsorName" /></b>--%>
						<html:text size="45" disabled="true" property="sponsorName"/>
					</td>
				</logic:notEqual>
			</tr>
			<!-- CASE #1374 End -->
		
              </table>

<!-- CASE #1374 Comment Begin -->
<!-- Move disclosure details to bottom of page -->
<%--
<jsp:include page="EditCOIDisclosureContentInsert1.jsp" flush="true"/>
<jsp:include page="EditCOIDisclosureContentInsert1b.jsp" flush="true"/>
--%>
<!-- CASE #1374 Comment End -->
           </td>
           </tr>

          <!--<tr><td height="23">&nbsp;</td></tr>-->
          <tr><td height="23">


              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr><td background="<bean:write name="ctxtPath"/>/images/tabback.gif">
                    <table border=0 cellpadding=0 cellspacing=0>
                      <tr><td>
				<img src="<bean:write name="ctxtPath"/>/images/certification.gif" width="120" height="24">
			    </td></tr>
                    </table>
                </td></tr>
              </table>


             <!-- show COI Dislcolsure Certification Details -->
		  <logic:notEqual name="collCOICertDetailsSize" value="0">

		    <table width="100%" border="0" cellspacing="1" cellpadding="5">
              <tr><td colspan="2" height="5"></td></tr>
                  <td width="450" height="25" bgcolor="#CC9999">
                    <div align="center">
				<font color="#FFFFFF">
					<bean:message key="editCOIDisclosure.label.question"/>
				</font>
			  </div>
                  </td>
                  <!-- CASE #864 Add bgcolor="#CC9999" -->
                  <td width="128" height="25" bgcolor="#CC9999">
                    <div align="center">
				<font color="#FFFFFF">
					<bean:message key="editCOIDisclosure.label.answer"/>
				</font>
			  </div>
                  </td>
                </tr>
		<!-- CASE #1393 Comment Begin -->
		<%--
                <logic:iterate id="certificateDetails" name="collCOICertDetails" 
                	type="edu.mit.coeus.coi.bean.CertificateDetailsBean" >
		<%
			String certCode = certificateDetails.getCode();
            		String certDesc = certificateDetails.getQuestion();
	            	String numOfAns = certificateDetails.getNumOfAns();
	            	String answer = certificateDetails.getAnswer();
                        if(answer == null) {
                          answer = "";
    		        }
	         %>
                <tr bgcolor="#FBF7F7">
                  <td width="450" height="25">
                    <INPUT type='hidden'  name='hdnQuestionID' value='<%= certCode %>'>
                    <INPUT type='hidden'  name='hdnQuestionDesc' value='<%= certDesc %>'>
                    <INPUT type='hidden' name='hdnLastUpdate' value='<%= certificateDetails.getLastUpdate() %>'>
                    <div align="justify">
			<bean:write name="certificateDetails" property="question" />
			<a href='javascript:showQuestion("<bean:write name='ctxtPath' />/question.do?questionNo=<bean:write name='certificateDetails' property='code'/>",  "ae");'  method="POST">
			<bean:message key="financialEntityDetails.question.explanationLink" />
			</a>
                    </div>
                  </td>
                  <td width="135" height="25" valign="top">
                    <div align="justify">
				<font face="Verdana, Arial, Helvetica, sans-serif">
                      		<input type="radio" name="<%= certCode %>" <%= answer.equals("Y")?"checked":""  %> value="Y" >Yes
                      		<input type="radio" name="<%= certCode %>" <%= answer.equals("N")?"checked":""  %> value="N">No
					<% if(numOfAns!=null && Integer.parseInt(numOfAns)>2){ %>
                      			<input type="radio" name="<%= certCode %>" <%= answer.equals("X")?"checked":""  %> value="X">N/A <%}%>
                  	</font>
			  </div>
                 </td>
                </tr>
      		</logic:iterate> 
      		--%>
      		<!-- CASE #1393 Comment End -->
	         <!-- CASE #1393 Begin -->
		<logic:present name="<%=Action.ERROR_KEY%>">
		<tr>
		<td colspan="2" >
		<font color="red"
		<html:errors property="answerRequired"/>
		</font>
		</td>
		</tr>
		</logic:present>	         
	         <%
		if(collCOICertDetails != null){	
		for(int certCount = 0; certCount< collCOICertDetails.size(); certCount++){ 
			CertificateDetailsBean certificateDetails = 
				(CertificateDetailsBean)collCOICertDetails.get(certCount);
			String certCode = certificateDetails.getCode();
			String certDesc = certificateDetails.getQuestion();
			String numOfAns = certificateDetails.getNumOfAns();
			String answer = certificateDetails.getAnswer();
			String correspEntQuestLabel = certificateDetails.getCorrespEntQuestLabel();
			String questionLabel = certificateDetails.getLabel();
			System.out.println("correspEntQuestLabel: "+correspEntQuestLabel);
			System.out.println("questionLabel: "+questionLabel);
			if(answer == null) {
			  answer = "";
			}	         
			%>
			<tr bgcolor="#FBF7F7">
			  <td width="450" height="25">
			    <INPUT type='hidden'  name='hdnQuestionID' value='<%= certCode %>'>
			    <INPUT type='hidden'  name='hdnQuestionDesc' value='<%= certDesc %>'>
			    <INPUT type='hidden' name='hdnLastUpdate' value='<%= certificateDetails.getLastUpdate() %>'>
			    <div align="justify">
			
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
							System.out.println("entitiesWithYes.size() == 1");
				  %>
							<!--<font face="Verdana, Arial, Helvetica, sans-serif" size="1" color="red">-->
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
							<a href="<bean:write name='ctxtPath'/>/editFinEntity.do?actionFrom=editDiscl&entityNo=<%=entityNumber%>"> <%=entityName%></a>
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
						 <bean:message key="error.editCOIDisclosure.noFinEntAnsReqExpl"/>
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
						<a href="<bean:write name="ctxtPath"/>/getFinEntities.do?actionFrom=editDiscl">
						<bean:message key="error.addCOIDisclosure.entNoAnsYes3"/>
						</a>
						 </font>							
						<bean:message key="error.addCOIDisclsoure.entNoAnsYes4"/>
				<%
					}
				}
				%>                	
		       </logic:present>
			    <div align="justify">
			    	<b>Question <%=certificateDetails.getLabel()%>:</b>
				<%=certificateDetails.getQuestion()%>
				<a href='javascript:showQuestion("<bean:write name='ctxtPath' />/question.do?questionNo=<%=certificateDetails.getCode()%>/>",  "ae");'  method="POST">
				<bean:message key="financialEntityDetails.question.explanationLink" />
				</a>
			    </div>
			  </td>
			  <td width="135" height="25" valign="top">
			    <div>
				<!-- CASE #1374 Add * for required field -->
				<font color="red"><b>*</b></font>			    
				<!--<font face="Verdana, Arial, Helvetica, sans-serif">-->
				<input type="radio" name="<%= certCode %>" <%= answer.equals("Y")?"checked":""  %> value="Y" >Yes
				<input type="radio" name="<%= certCode %>" <%= answer.equals("N")?"checked":""  %> value="N">No
					<% if(numOfAns!=null && Integer.parseInt(numOfAns)>2){ %>
					<input type="radio" name="<%= certCode %>" <%= answer.equals("X")?"checked":""  %> value="X">N/A <%}%>
				<!--</font>-->
				  </div>
			 </td>
			</tr>		       
		<%
		       }//end for loop
               }//end if collCOICertDetails != null
               %>
               
	         <!-- CASE #1393 End -->                  

	  		</logic:notEqual>
			<INPUT type='hidden' name='hdnNumOfQs' value='<%= collCOICertDetailsSize %>' >
			<logic:equal name="collCOICertDetailsSize" value="0">
		            <tr><td colspan='3' height="23"><bean:message key="editCOIDisclosure.label.noQuestions" /></td></tr>
			</logic:equal>
            </table>
              <!-- End of certificate details -->
          </td></tr>

          <tr><td height="23">&nbsp;</td></tr>
          <tr><td height="23">

<jsp:include page="EditCOIDisclosureContentInsert2.jsp" flush="true"/>
<!-- CASE #1374 Begin -->
<% 	int userpriv = Integer.parseInt(userprivilege);
	if(userpriv > 0 ){
%>
<!-- CASE #1374 End -->
<jsp:include page="EditCOIDisclosureContentInsert1.jsp" flush="true"/>
<jsp:include page="EditCOIDisclosureContentInsert1b.jsp" flush="true"/>
<!-- CASE #1374 Begin-->
<%
	}
%>
<!-- CASE #1374 End -->

		</td></tr>
		<tr>
		<td height="2">&nbsp;</td>
		</tr>
		
		<tr>
		<td>
		<table>
		<!-- CASE #357 Begin -->
		<!-- If financial entities have changed since the time the disclosure was created, give user the option of synchronizing financial entities with most recent. -->
		<logic:present name="showSyncButton" scope="request">
			<!-- CASE #665 change width from 465 to 515 to line up buttons -->
			<tr><td width="515"><div>
			<bean:message key="editCOIDisclosure.sync.message" />
			</div></td>
			<td width="130">
			<html:image page="/images/sync.gif" border="0"  alt="Synchronize Financial Entities" property="Synchronize Financial Entities" />
			</td>
			</tr>
			</logic:present>
			<!-- CASE #357 End -->
			<tr>
			<!-- CASE #357 Comment Begin -->
			<!-- <td height="40">
			<div align="right"> -->
			<!-- CASE #357 Comment End -->
			<!-- CASE #357 Begin -->
			<div>
			<!-- CASE #665 Comment Begin -->
			<!--
			<td width= "515">
			&nbsp;
			<td width = "130" >
			</td>
			-->
			<!-- CASE #665 Begin -->
			<td width="645" colspan="2" align="right">
			<!-- CASE #665 End -->
			<!-- CASE #357 End -->
			<!-- CASE #665 Comment Begin -->
			<!-- Remove Back button -->
			<%--
			<a href="JavaScript:history.back();">
				<img src="<bean:write name="ctxtPath"/>/images/goback.gif" width="55" height="22" border="0">
			    </a>&nbsp;
			--%>
			<!-- CASE #665 Comment End -->
			<html:image page="/images/submit.gif" border="0" />
			&nbsp; &nbsp; &nbsp; </div>
			<!-- CASE #357 Begin -->
			</td></tr>
			</table>
          	</td></tr>
  	 </table>

        </html:form>

  </td>
 </tr>
</table>
<!-- CASE #1374 Begin -->
<!-- Show response window to confirm ent added or modified -->
<% if(request.getAttribute("FESubmitSuccess") != null){
	request.removeAttribute("FESubmitSuccess");
	System.out.println("FESubmitSuccess is not null");
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