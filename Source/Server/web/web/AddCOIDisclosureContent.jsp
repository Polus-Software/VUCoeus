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
                java.net.URLEncoder"
	errorPage="ErrorPage.jsp" %>
<%@ taglib uri="/WEB-INF/struts-html.tld"     prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld"     prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld"     prefix="logic" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"     prefix="coeusUtils" %>
<%@ include file="CoeusContextPath.jsp"  %>

<jsp:useBean id="disclosureHeaderBean" scope='session' class="edu.mit.coeus.coi.bean.DisclosureHeaderBean"/>
<jsp:useBean id="collCOIDiscCertDetails" scope='session' class="java.util.Vector"/>
<jsp:useBean id="collCOIDisclosureInfo" scope='session' class="java.util.Vector"/>
<jsp:useBean  id="collCOIStatus" 		scope="request" class="java.util.LinkedList" />
<jsp:useBean id="personId" scope ="request" class = "java.lang.String" />
<bean:size id="coiDiscCertificatesSize" name="collCOIDiscCertDetails" />

 <table width="100%" border="0" cellpadding="0" cellspacing="0">
 <tr>
<td background="<bean:write name="ctxtPath"/>/images/separator.gif" width="1">
    <img src="<bean:write name="ctxtPath"/>/images/separator.gif" width="1">
</td>
<td width="657" valign="top">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<html:form action="/addNewCOIDisclosure.do">
	<html:hidden name="frmAddCOIDisclosure" property="disclosureNo" />
	<html:hidden name="frmAddCOIDisclosure" property="disclosureTypeCode" />
	<html:hidden name="frmAddCOIDisclosure" property="appliesToCode" />
    <html:hidden name="frmAddCOIDisclosure" property="personId" value="<%=personId%>" />
	<html:hidden name="frmAddCOIDisclosure" property="userName" />
	<html:hidden name="frmAddCOIDisclosure" property="statusSeqNum" value="1"/>
	<html:hidden name="frmAddCOIDisclosure" property="accountType" value="I"/> <%--  I- Insert --%>
	<html:hidden name="frmAddCOIDisclosure" property="seqNum" value="1"/>
	  <tr>
		<td height="23" bgcolor="#cccccc"> &nbsp;<b><font face="Verdana, Arial, Helvetica, sans-serif" size="2">
		<bean:message key="addCOIDisclosure.header"/>
		<bean:write name="frmAddCOIDisclosure" property="disclosureNo" />
		</font></b> </td>
	  </tr>
	  <tr>
		<td height="23">
		  <table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
			  <td height="40">
				<div align="right"><a href="JavaScript:history.back();"><img src="<bean:write name='ctxtPath'/>/images/goback.gif" width="55" height="22" border="0"></a>&nbsp;
				  <html:image src="images/submit.gif" border="0"/>
				  &nbsp; &nbsp; &nbsp; </div>
			  </td>
			</tr>
		  </table>
                  <%-- Show Errors --%>
                  <html:errors/>
		  <table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
			  <td background="<bean:write name='ctxtPath'/>/images/tabback.gif">
				<table border=0 cellpadding=0 cellspacing=0>
				  <tr>
					<td> <img src="<bean:write name='ctxtPath'/>/images/disclosure.gif" width="120" height="24"></td>
				  </tr>
				</table>
			  </td>
			</tr>
		  </table>
		  <table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
			  <td colspan="6" height="5"></td>
			</tr>

			<tr>
			  <td width="100" height="20" bgcolor="#FBF7F7">
				<div align="left">&nbsp;<bean:message key="addCOIDisclosure.label.personName"/></div>
			  </td>
			  <td width="6" height="20" bgcolor="#FBF7F7">:</td>
			  <td width="176" height="20" bgcolor="#FBF7F7">
				<div align="left"><b><bean:write name="frmAddCOIDisclosure" property="personFullName" /></b></div>
				<html:hidden property="personFullName" />
			  </td>
			  <td width="123" height="20" bgcolor="#FBF7F7">
				<div align="left">&nbsp;<bean:message key="addCOIDisclosure.label.disclosureNo"/></div>
			  </td>
			  <td width="3" height="20" bgcolor="#FBF7F7">:</td>
			  <td width="238" height="20" bgcolor="#FBF7F7">
				<div align="left"><b><bean:write name="frmAddCOIDisclosure" property="disclosureNo" /></b> </div>
			  </td>
			</tr>

			<tr bgcolor="#F7EEEE">
			  <td width="100" height="20">
				<div align="left">&nbsp;<bean:message key="addCOIDisclosure.label.appliesTo"/></div>
			  </td>
			  <td width="6" height="20">:</td>
			  <td width="176" height="20">
				<div align="left"><b>
				<logic:equal name="frmAddCOIDisclosure" property="disclosureTypeCode" value="1">
					<bean:message key="addCOIDisclosure.label.award"/>
				</logic:equal>
				<logic:notEqual name="frmAddCOIDisclosure" property="disclosureTypeCode" value="1">
					 <bean:message key="addCOIDisclosure.label.proposal"/>
				</logic:notEqual>
				</b></div>
			  </td>
			  <td width="123" height="20">
				<div align="left">&nbsp;
				 <logic:equal name="frmAddCOIDisclosure" property="disclosureTypeCode" value="1">
					<bean:message key="addCOIDisclosure.label.awardNo"/>
				</logic:equal>
				<logic:notEqual name="frmAddCOIDisclosure" property="disclosureTypeCode" value="1">
					 <bean:message key="addCOIDisclosure.label.proposalNo"/>
				</logic:notEqual>
				</div>
			  </td>
			  <td width="3" height="20">:</td>
			  <td width="238" height="20">
				<div align="left"><b><bean:write name="frmAddCOIDisclosure" property="appliesToCode" /></b> </div>
			  </td>
			</tr>

			<tr>
			  <td width="100" height="20" bgcolor="#FBF7F7">
				<div align="left">&nbsp;<bean:message key="addCOIDisclosure.label.title"/></div>
			  </td>
			  <td height="20" bgcolor="#FBF7F7" width="6">:</td>
			  <td height="20" colspan='4' bgcolor="#FBF7F7" width="176">
				<div align="left"><b><bean:write name="disclosureHeaderBean" property="title"/></b></div>
			  </td>
			</tr>
			<tr bgcolor="#F7EEEE">
			  <td width="100" height="20">
				<div align="left">&nbsp;<bean:message key="addCOIDisclosure.label.account"/></div>
			  </td>
			  <td width="6" height="20">:</td>
			  <td width="176" height="20">
				<div align="left"><b><bean:write name="disclosureHeaderBean" property="account"/></b></div>
			  </td>
			  <td width="123" height="20">
				<div align="left">&nbsp;<bean:message key="addCOIDisclosure.label.status"/></div>
			  </td>
			  <td width="3" height="20">:</td>
			  <td width="238" height="20">
				<div align="left"><b><bean:write name="disclosureHeaderBean" property="status"/></b></div>
			  </td>
			</tr>
			<tr>
			  <td width="100" height="20" bgcolor="#FBF7F7">
				<div align="left">&nbsp;<bean:message key="addCOIDisclosure.label.disclosureType"/></div>
			  </td>
			  <td width="6" height="20" bgcolor="#FBF7F7">:</td>
			  <td width="176" height="20" bgcolor="#FBF7F7">
				<div align="left">
				<%-- if disclosure type is annual (A) then show in lable format else show in
						html combo box(select).
				--%>
				<logic:present name="frmAddCOIDisclosure" property="disclosureType" >
					<logic:equal name="frmAddCOIDisclosure" property="disclosureType" value="A">
						<b><bean:message key="addCOIDisclosure.label.annual"/></b>
						<html:hidden name="frmAddCOIDisclosure" property="disclosureType" />
					</logic:equal>
					<logic:notEqual name="frmAddCOIDisclosure" property="disclosureType" value="A">
						<html:select property='disclosureType'>
							<html:option value='I'>Initial</html:option>
							<html:option value='A'>Annual</html:option>
						</html:select>
					</logic:notEqual>
				</logic:present>
				<logic:notPresent name="frmAddCOIDisclosure" property="disclosureType" >
					&nbsp;
				</logic:notPresent>
				</div>
			  </td>
			  <td width="123" height="20" bgcolor="#FBF7F7">
				<div align="left">&nbsp;<bean:message key="addCOIDisclosure.label.disclosureStatus"/></div>
			  </td>
			  <td width="3" height="20" bgcolor="#FBF7F7">:</td>
			  <td width="238" height="20" bgcolor="#FBF7F7">
				<div align="left"><b><bean:message key="addCOIDisclosure.label.piReviewed"/></b>
				<%-- Keep Disclousure Status  information in hidden property --%>
				<html:hidden name="frmAddCOIDisclosure" property="status" value="101"/>
				</div>
			  </td>
			</tr>
			<tr bgcolor="#F7EEEE">
			  <td width="100" height="20">
				<div align="left">&nbsp;<bean:message key="addCOIDisclosure.label.reviewedBy"/></div>
			  </td>
			  <td height="20" width="6">
				<div align="left">: </div>
			  </td>
			  <td height="20" width="176" bgcolor="#F7EEEE">
				<div align="left"><b><bean:message key="addCOIDisclosure.label.osp"/></b>
				<%-- Keep Reviewer information in hidden property --%>
				<html:hidden name="frmAddCOIDisclosure" property="reviewer" value="2" />
			</div>
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
				<div align="center"><font color="#FFFFFF"><bean:message key="addCOIDisclosure.label.answer"/></font></div>
			  </td>
			</tr>
			<%
			/*
			 * Struts1.0.2 does not support dynamic properties feature, so adopted MVC way, looking to move
			 * code to Taghandler class.
			 */
			// get the previously selected values of questions and answers if available then display them
			String preSelectedQuestions[] =(String[])request.getAttribute("selectedQuestions");
			String preSelectedAnswers[] =(String[])request.getAttribute("selectedAnswers");
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

				String checkedYes="";
				String checkedNo="";
				String checkedNA="";

				//get the preselected question
				String  preSelectedQuestionCode =null;

				if(  (preSelectedQuestions!=null) && (preSelectedQuestions.length>0 )  ){
					preSelectedQuestionCode=preSelectedQuestions[certificateCount];
				}

				String  preSelectedAnswerCode = null;
				if( (preSelectedAnswers!=null) && (preSelectedAnswers.length>0 ) ){
					preSelectedAnswerCode=preSelectedAnswers[certificateCount];
				}

				if(preSelectedQuestionCode!=null){
					 certCode = preSelectedQuestionCode;
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

			%>
			<tr bgcolor='<%=( certificateCount % 2 == 0 ? "#FBF7F7" : "#F7EEEE") %>'>
			  <!--<td width="85" height="25">
				<div align="center"><font face="Verdana, Arial, Helvetica, sans-serif">
				<a href="JavaScript:openwin('<bean:write name="ctxtPath"/>/question.do?questionNo=<%= certCode %>','question');"><%= certCode %></a></font></div>
			  </td>-->
			  <td width="450" height="25">
				<INPUT type='hidden'  name='hdnQuestionID' value='<%= certCode%>' >
				<INPUT type='hidden'  name='hdnQuestionDesc' value='<%= certDesc%>'>
				<INPUT type='hidden'  name='hdnQSeqNum' value='1'>
				<div align="justify"><font face="Verdana, Arial, Helvetica, sans-serif">
					<%=question%>
					<a href="JavaScript:openwin('<bean:write name="ctxtPath"/>/question.do?questionNo=<%= certCode %>','question');">
					<bean:message key="addCOIDisclosure.label.description" />
					</a>
					</font>
				</div>
			  </td>
			  <td width="135" height="25" align="center">
				<div ><font face="Verdana, Arial, Helvetica, sans-serif">
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
	  <tr>
		<td height="23">&nbsp;</td>
	  </tr>
	  <tr>
		<td height="23">
		  <table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
			  <td background="<bean:write name='ctxtPath'/>/images/tabback.gif">
				<table border=0 cellpadding=0 cellspacing=0>
				  <tr>
					<td> <img src="<bean:write name='ctxtPath'/>/images/finEntDisc.gif" width="167" height="24"></td>
				  </tr>
				</table>
			  </td>
			</tr>
		  </table>
		   <!--All COI Disclosures Information begins -->
		  <table width="100%" border="0" cellspacing="1" cellpadding="0" align="right">
			<tr>
			  <td colspan="5" height="5"></td>
			</tr>
			<tr bgcolor="#CC9999">
			  <td width="113" height="25">
				<div align="center"><font color="#FFFFFF"><bean:message key="addCOIDisclosure.label.entityName"/></font></div>
			  </td>
			  <td width="102" height="25">
				<div align="center"><font color="#FFFFFF"><bean:message key="addCOIDisclosure.label.conflictStatus"/></font></div>
			  </td>
			  <td width="111" height="25">
				<div align="center"><font color="#FFFFFF"><bean:message key="addCOIDisclosure.label.reviewedBy"/></font></div>
			  </td>
			  <td width="210" height="25">
				<div align="center"><font color="#FFFFFF"><bean:message key="addCOIDisclosure.label.description"/></font></div>
			  </td>
			  <td width="20" height="25">
              <div align="center"><font color="#FFFFFF">&nbsp;</font></div>
			  </td>
			</tr>
            <%--<bean:message key="addCOIDisclosure.label.editDescription"/> --%>
			<%-- Struts1.0.2 disadvantage: Action Form do not support creation of dynamic form
			properties but it is possible in struts1.1( still beta and not stable release).

			So creation of dynamic properties followed  regular JSP way.

			--%>


	<%
               // get the previously selected values of questions and answers if available then display them
               String preSelectedConfStatus[] =(String[])request.getAttribute("selectedConflictStatus");
               String preSelectedDesc[] =(String[])request.getAttribute("selectedDescription");
                int disclIndex = 0;
        %>
			<logic:iterate id="disclosureInfoBean" name="collCOIDisclosureInfo" type="edu.mit.coeus.coi.bean.DisclosureInfoBean">
			<INPUT type='hidden' name='hdnEntityNum' value='<bean:write name="disclosureInfoBean" property="entityNumber"/>'>
			<INPUT type='hidden' name='hdnEntSeqNum' value='<bean:write name="disclosureInfoBean" property="entSeqNumber"/>'>
			<INPUT type='hidden' name='hdnSeqNum' value='<bean:write name="disclosureInfoBean" property="seqNumber"/>'>
			<tr bgcolor="#FBF7F7">
			  <td width="113" height="25">
				<div align="center"><bean:write name="disclosureInfoBean" property="entityName" /></div>
			  </td>
			  <td width="102" height="25">
				<%-- <div align="center"><bean:message key="addCOIDisclosure.label.noConflict" /></div> --%>

                  	  <select name='sltDiscStat<%=disclIndex%>' >

                        <%
                        String  preSelectedConflictStatus = "";
                        if( (preSelectedConfStatus != null) && (preSelectedConfStatus.length > 0 ) ){
                            preSelectedConflictStatus = preSelectedConfStatus[disclIndex];
                        }
                        String strSelectedNPR = "";
                        String strSelectedNC = "";
                        String strSelectedPIC = "";

                        strSelectedNPR = "100".equals(preSelectedConflictStatus.trim())?"selected":"";
                        strSelectedNC = "200".equals(preSelectedConflictStatus.trim())?"selected":"";
                        strSelectedPIC = "301".equals(preSelectedConflictStatus.trim())?"selected":"";
                        %>
                            <option <%= strSelectedNPR %> value="100">Not Previously Reported</option>
                            <option <%= strSelectedNC %> value="200">No conflict</option>
                            <option <%= strSelectedPIC %> value="301">PI Identified Conflict</option>
                          </select>
			  </td>



<%--


                        <SELECT name='sltDiscStat<%=disclIndex%>'>
<%
					    //LinkedList lstCOIStatus = objDisclosureDetailsBean.getAllCOIStatus();
                        String  preSelectedConflictStatus = "";
                        if( (preSelectedConfStatus != null) && (preSelectedConfStatus.length > 0 ) ){
                            preSelectedConflictStatus = preSelectedConfStatus[disclIndex];
                        }
                        int lstSize = collCOIStatus.size();
                        String strSelected = "";
                        String code = "";
                        String description ="";
			for(int i=0;i<lstSize;i++){
                          ComboBoxBean objCombBean = (ComboBoxBean)collCOIStatus.get(i);
                          code = objCombBean.getCode();
                          description = objCombBean.getDescription();
                          strSelected = code.trim().equals(preSelectedConflictStatus.trim())?"selected":"";
%>
                        	<option <%= strSelected %> value="<%= code %>"><%= description %></option>
<%
                       }
%>
                        </SELECT>

			  </td>

--%>






			  <td width="111" height="25">
				<div align="center"><bean:message key="addCOIDisclosure.label.pi"/></div>
			  </td>
			  <td width="210" height="25">
<%
              String disclDescription = "";
              String  preSelectedDescription ="";
              if( (preSelectedDesc != null) && (preSelectedDesc.length > 0 ) ){
                preSelectedDescription = preSelectedDesc[disclIndex];
                disclDescription = preSelectedDescription;
              }else{
                   disclDescription = (disclosureInfoBean.getDesc() == null?"":disclosureInfoBean.getDesc());
              }
              String encDescription = URLEncoder.encode(disclDescription);
%>


              <INPUT type='text' size='25' name='description<%=disclIndex%>' value='<%= disclDescription %>'>

		  <%--
              <INPUT type='text' size='25' name='description<%=disclIndex%>' disabled
                        onfocus='javascript:changeFocus(sltDiscStat<%=disclIndex%>);' value='<%= disclDescription %>'>
		  --%>


				<%-- <div align="center"><coeusUtils:formatOutput name="disclosureInfoBean" property="desc"  defaultValue="&nbsp;" /></div> --%>
			  </td>
			  </td>
			  <td width="20" height="25">
               <a href="JavaScript:openwinDesc('EditDisclosureDescription.jsp?desc=<%=encDescription%>&index=<%=disclIndex++%>','Description');">
                    <img src="<bean:write name='ctxtPath'/>/images/more.gif" width="22" height="20" border="0">
               </a>
				<%-- <div align="center"><coeusUtils:formatDate name="disclosureInfoBean" property="lastUpdated" /></div> --%>
			  </td>

			</tr>
			</logic:iterate>
			<!-- Disclosure Information ends -->
		  </table>

		</td>
	  </tr>
	  <tr>
		<td height="40">
		  <div align="right"><a href="JavaScript:history.back();"><img src="<bean:write name='ctxtPath'/>/images/goback.gif" width="55" height="22" border="0"></a>&nbsp;
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
</script>
