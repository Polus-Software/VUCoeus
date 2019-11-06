<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%--Added for COEUSQA-3483 : Changes to Protocol Summary screen in Routing in Lite - start--%>
<%@ page
	import="java.util.*,edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean,edu.utk.coeuslite.propdev.bean.QuestionAnswerProposalSummaryBean, edu.wmc.coeuslite.utils.DynaBeanList, edu.mit.coeus.sponsormaint.bean.SponsorFormsBean"%>
<%--Added for COEUSQA-3483 : Changes to Protocol Summary screen in Routing in Lite - end--%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@ page
	import="org.apache.struts.validator.DynaValidatorForm ,
        edu.mit.coeuslite.utils.CoeusLiteConstants,
         java.util.Vector,java.util.ArrayList,
         edu.mit.coeuslite.utils.DateUtils,
         edu.mit.coeuslite.utils.CoeusLiteConstants,java.util.Vector,edu.mit.coeus.utils.ComboBoxBean"%>
<%@ page
	import="org.apache.struts.taglib.html.JavascriptValidatorTag,
                edu.mit.coeuslite.utils.CoeusLiteConstants"%>
<jsp:useBean id="vecRolesType" scope="session" class="java.util.Vector" />
<jsp:useBean id="vecAffiliationType" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="iacucPersonsData" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="vecKeyPersonRoles" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="iacucAreaData" scope="session" class="java.util.Vector" />
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="iacucSplReview" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="approval" scope="session" class="java.util.Vector" />
<jsp:useBean id="ReviewList" scope="session" class="java.util.Vector" />
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%  DateUtils dateUtils = new edu.mit.coeuslite.utils.DateUtils();
%>
<jsp:useBean id="uploadLatestData" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="historyData" scope="request" class="java.util.Vector" />
<jsp:useBean id="parentIacucData" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="vecAcAlternativeSearchesData" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="vecAddedAlternativeSearches" scope="session"
	class="java.util.Vector" />
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="vecProtoSpeciesData" scope="session"
	class="java.util.Vector" />
<html>
<title>IRBSummary</title>
<script language="javascript" type="text/JavaScript"
	src="<%=request.getContextPath()%>/coeuslite/mit/utils/scripts/divSlide.js"></script>
<script language="javascript" type="text/JavaScript"
	src="..\scripts\_js.js"></script>
<script language="javascript" type="text/JavaScript"
	src="..\scripts\CheckForward.js"></script>
<script
	src="<%=request.getContextPath()%>/coeuslite/mit/utils/scripts/divSlide.js"></script>
<script language="JavaScript" type="text/JavaScript">

        function selectProjIconPlus( divid)
        {
            var imgplus="projectimgtoggle"+divid;
            var imgminus="projectimgtoggleminus"+divid;
           
            imgplus=document.getElementById(imgplus);
            imgminus=document.getElementById(imgminus);
            divid=document.getElementById(divid);
            if(imgplus!=null){imgplus.style.display='none';}
            if(imgminus!=null){imgminus.style.display='block';}
            if(divid!=null){divid.style.display='block';divid.style.height='auto';}
          
        }

        function selectProjIconMinus( divid)
        {
            var imgplus="projectimgtoggle"+divid;
            var imgminus="projectimgtoggleminus"+divid;
            imgplus=document.getElementById(imgplus);
            imgminus=document.getElementById(imgminus);
            divid=document.getElementById(divid);
            if(imgplus!=null){imgplus.style.display='block';}
            if(imgminus!=null){imgminus.style.display='none';}
            if(divid!=null){divid.style.display='none';divid.style.height='1px';}
            } 
         function summaryHeader(value)
        {
             if(value==3) {
            document.getElementById('protohideimage').style.display = 'block';
            document.getElementById('invHide').style.display = 'block';
            document.getElementById('invDetails').style.display = 'block';
             document.getElementById('invShow').style.display = 'none';
            document.getElementById('protoshowimage').style.display = 'none';

        }
           else if(value==4) {
            document.getElementById('protohideimage').style.display = 'none';
            document.getElementById('invHide').style.display = 'none';
            document.getElementById('invDetails').style.display = 'none';
             document.getElementById('invShow').style.display = 'block';
            document.getElementById('protoshowimage').style.display = 'block';
        }
        else if(value==5) {
            document.getElementById('areaHideImage').style.display = 'block';
            document.getElementById('areaHide').style.display = 'block';
            document.getElementById('areaDetails').style.display = 'block';
            document.getElementById('areaShow').style.display = 'none';
            document.getElementById('areaShowImage').style.display = 'none';
        }
           else if(value==6) {
            document.getElementById('areaHideImage').style.display = 'none';
            document.getElementById('areaHide').style.display = 'none';
            document.getElementById('areaDetails').style.display = 'none';
            document.getElementById('areaShow').style.display = 'block';
            document.getElementById('areaShowImage').style.display = 'block';
        }
         else if(value==7) {
            document.getElementById('fundHideImage').style.display = 'block';
            document.getElementById('fundHide').style.display = 'block';
            document.getElementById('fundDetails').style.display = 'block';
            document.getElementById('fundShow').style.display = 'none';
            document.getElementById('fundShowImage').style.display = 'none';
        }
           else if(value==8) {
            document.getElementById('fundHideImage').style.display = 'none';
            document.getElementById('fundHide').style.display = 'none';
            document.getElementById('fundDetails').style.display = 'none';
            document.getElementById('fundShow').style.display = 'block';
            document.getElementById('fundShowImage').style.display = 'block';
        }
         else if(value==9) {
            document.getElementById('specialHideImage').style.display = 'block';
            document.getElementById('specialHide').style.display = 'block';
            document.getElementById('specialDetails').style.display = 'block';
            document.getElementById('specialShow').style.display = 'none';
            document.getElementById('specialShowImage').style.display = 'none';
        }
           else if(value==10) {
            document.getElementById('specialHideImage').style.display = 'none';
            document.getElementById('specialHide').style.display = 'none';
            document.getElementById('specialDetails').style.display = 'none';
            document.getElementById('specialShow').style.display = 'block';
            document.getElementById('specialShowImage').style.display = 'block';
        }
        else if(value==11) {
            document.getElementById('attachmentHideImage').style.display = 'block';
            document.getElementById('attachmentHide').style.display = 'block';
            document.getElementById('attachmentDetails').style.display = 'block';
            document.getElementById('attachmentShow').style.display = 'none';
            document.getElementById('attachmentShowImage').style.display = 'none';
        }
           else if(value==12) {
            document.getElementById('attachmentHideImage').style.display = 'none';
            document.getElementById('attachmentHide').style.display = 'none';
            document.getElementById('attachmentDetails').style.display = 'none';
            document.getElementById('attachmentShow').style.display = 'block';
            document.getElementById('attachmentShowImage').style.display = 'block';
        }
         else if(value==13) {
            document.getElementById('alternativeHideImage').style.display = 'block';
            document.getElementById('alternativeHide').style.display = 'block';
            document.getElementById('alternativeDetails').style.display = 'block';
            document.getElementById('alternativeShow').style.display = 'none';
            document.getElementById('alternativeShowImage').style.display = 'none';
        }
           else if(value==14) {
            document.getElementById('alternativeHideImage').style.display = 'none';
            document.getElementById('alternativeHide').style.display = 'none';
            document.getElementById('alternativeDetails').style.display = 'none';
            document.getElementById('alternativeShow').style.display = 'block';
            document.getElementById('alternativeShowImage').style.display = 'block';
        }
        else if(value==15) {
            document.getElementById('speciesHideImage').style.display = 'block';
            document.getElementById('speciesHide').style.display = 'block';
            document.getElementById('specdet').style.display = 'block';
            document.getElementById('specdet').style.height='auto';
            document.getElementById('speciesShow').style.display = 'none';
            document.getElementById('speciesShowImage').style.display = 'none';
        }
           else if(value==16) {
            document.getElementById('speciesHideImage').style.display = 'none';
            document.getElementById('speciesHide').style.display = 'none';
            document.getElementById('specdet').style.display = 'none';
            document.getElementById('specdet').style.height='1px'; 
            document.getElementById('speciesShow').style.display = 'block';
            document.getElementById('speciesShowImage').style.display = 'block';
        }
        //Added for  COEUSQA-3483 : Changes to Protocol Summary screen in Routing in Lite - start
         else if(value==23) {
            document.getElementById('qnrmainDiv').style.display = 'block';
          document.getElementById('QuestionShowImage').style.display = 'none';
           document.getElementById('QuestiontHideImage').style.display = 'block';
           document.getElementById('QuestionShow').style.display = 'none';
           document.getElementById('QuestionHide').style.display = 'block';
        }
        else if(value==24) {
           document.getElementById('qnrmainDiv').style.display = 'none';
           document.getElementById('QuestionShowImage').style.display = 'block';
           document.getElementById('QuestiontHideImage').style.display = 'none';
           document.getElementById('QuestionShow').style.display = 'block';
           document.getElementById('QuestionHide').style.display = 'none';
        }
       //Added for  COEUSQA-3483 : Changes to Protocol Summary screen in Routing in Lite - end
    }
       function view_comments(val) {
       var value;
       if(val.length<1000)
                {value=val; }
       else
                 {value=val.substring(0,1000);}
        var w = 550;
        var h = 213;
        if(navigator.appName == "Microsoft Internet Explorer") {
            w = 522;
            h = 196;
        }
        if (window.screen) {
               leftPos = Math.floor(((window.screen.width - 500) / 2));
               topPos = Math.floor(((window.screen.height - 350) / 2));
         }
                var loc = '<bean:write name='ctxtPath'/>/coeuslite/mit/utils/dialogs/cwViewSpecialReview.jsp?value='+value +'&type=S';
               <%--  alert(loc);--%>
                var newWin = window.open(loc,"formName",'dependent=1,toolbar=0,location=0,directories=0,status=0, menubar=0,scrollbars=0,resizable=0,width=' + w + ',height=' + h+ ',left='+ leftPos + ',top=' + topPos);

                newWin.window.focus();

         }


      function selectProjIcon()
      {
          document.getElementsByName(index) ='none';

      }

       function view_data(activityType,docType,versionNumber, sequenceNumber,documentId, isParent){
            //Modified for Case#3036 - Didn't recognize document file type in Protocol attachments - End
                //window.open("<%=request.getContextPath()%>/iacucviewUploadAction.do?&docType="+docType+"&acType=V&versionNumber="+versionNumber+"&SeqNumber="+sequenceNumber+"&Parent="+isParent);
                window.open("<%=request.getContextPath()%>/iacucviewUploadAction.do?&docType="+docType+"&acType=V&versionNumber="+versionNumber+"&SeqNumber="+sequenceNumber+"&Parent="+isParent+"&protocDocId="+documentId);
            }
            
            
   //Added for COEUSQA-3483 : Changes to Protocol Summary screen in Routing in Lite - start
   function dynamicQuestionsDivHide(contDiv,plusDiv,showdiv,hideDiv,minusDiv){
            document.getElementById(contDiv).style.display = 'none';
            document.getElementById(plusDiv).style.display = 'block';
            document.getElementById(showdiv).style.display = 'block';
            document.getElementById(minusDiv).style.display = 'none';
            document.getElementById(hideDiv).style.display = 'none';
      }

    function dynamicQuestionsDivShow(contDiv,plusDiv,showdiv,hideDiv,minusDiv){
            document.getElementById(contDiv).style.display = 'block';
            document.getElementById(plusDiv).style.display = 'none';
            document.getElementById(showdiv).style.display = 'none';
            document.getElementById(minusDiv).style.display = 'block';
            document.getElementById(hideDiv).style.display = 'block';

      }
      
    function showQuestion(link) {
        var winleft = (screen.width - 650) / 2;
        var winUp = (screen.height - 450) / 2;
        var win = "scrollbars=1,resizable=1,width=790,height=450,left="+winleft+",top="+winUp
        sList = window.open('<bean:write name='ctxtPath'/>'+"/"+link, "list", win);
        if (parseInt(navigator.appVersion) >= 4) {
                window.sList.focus();
        }
     }
     
     function view_questionnaire_data(qnrid){
        var operation="PRINT";
        var printQuestion = 'N';
        var printAllQuestion = 'Y';
        var actionFrom='IACUC_PROTOCOL';
        var iacucprotocolSummary='IACUC_PROTOCOL_SUMMARY';
        window.open("<%=request.getContextPath()%>/saveQuestionnaireData.do?&questionnaireId="+qnrid+"&operation="+operation+"&printQuestions="+printQuestion+"&printAllQuestions="+printAllQuestion+"&printQnrId="+qnrid+"&iacucprotocolSummary="+iacucprotocolSummary);
    
    }
    //Added for COEUSQA-3483 : Changes to Protocol Summary screen in Routing in Lite - end
        </script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JSP Page</title>
</head>
<%String plus = request.getContextPath()+"/coeusliteimages/plus.gif";
  String minus = request.getContextPath()+"/coeusliteimages/minus.gif";%>

<body>
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<%if(request.getAttribute("ONLY_HEADER") == null){%>
		<tr>
			<td><jsp:include
					page="/coeuslite/utk/propdev/clApprovalRoute.jsp" flush="true" /></td>
		</tr>
		<%}%>
	</table>
	<table width="100%" border="0" cellspacing="0" cellpadding="0"
		class="table">

		<tr>
			<td valign="top"><jsp:include
					page="/coeuslite/mit/iacuc/common/cwGeneralInfoHeader.jsp"
					flush="true" /></td>
		</tr>
		<tr>
			<td height='3'></td>
		</tr>
		<%--..............................inv key person...starts.......................--%>
		<tr valign=top>
			<td>
				<table width="98%" align=center border="0" cellpadding="0"
					cellspacing="0" class="tabtable">
					<tr valign=top>
						<td>
							<table width="100%" border="0" cellpadding="0" cellspacing="0"
								class="tableheader">
								<tr valign=top>
									<td width='50%' align=left nowrap>
										<div id='protoshowimage' style='display: block;'>
											<html:link href="javaScript:summaryHeader('3');">
												<html:img src="<%=plus%>" border="0" />
											</html:link>
											&nbsp;
											<bean:message bundle="iacuc"
												key="amendment.summary.protocolInvestigators" />
										</div>
										<div id='protohideimage' style='display: none;'>
											<html:link href="javaScript:summaryHeader('4');">
												<html:img src="<%=minus%>" border="0" />
											</html:link>
											&nbsp;
											<bean:message bundle="iacuc"
												key="amendment.summary.protocolInvestigators" />
										</div>
									</td>
									<td width='50%' align=right><html:link
											href="javaScript:summaryHeader('3');">
											<div id='invShow' style='display: block;'>
												<bean:message bundle="proposal" key="summary.show" />
											</div>
										</html:link> <html:link href="javaScript:summaryHeader('4');">
											<div id='invHide' style='display: none;'>
												<bean:message bundle="proposal" key="summary.hide" />
											</div>
										</html:link></td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td height='2'></td>
					</tr>
					<tr>
						<td>
							<div id='invDetails' style='display: none;'>

								<table width="98%" border="0" cellspacing="0" cellpadding="3"
									class="tabtable" align="center">
									<tr align="center" valign="top">
										<td width='24%' class="theader" align="left"><bean:message
												bundle="iacuc" key="label.invesKeypersons.personName" /></td>
										<td width='24%' class="theader" align="left"><bean:message
												bundle="iacuc" key="label.invesKeypersons.department" /></td>
										<td width='9%' class="theader"><bean:message
												bundle="iacuc" key="label.invesKeypersons.leadUnit" /></td>
										<td width='15%' class="theader" align="left"><bean:message
												bundle="iacuc" key="label.invesKeypersons.role" /></td>
										<td width='10%' class="theader" align="left"><bean:message
												bundle="iacuc" key="label.invesKeypersons.affliate" /></td>
										<td width='6%' class="theader"><bean:message
												bundle="iacuc" key="label.invesKeypersons.training" /></td>
									</tr>
									<% String strBgColor = "#DCE5F1"; //BGCOLOR=EFEFEF  FBF7F7
                   int count = 0;
                   int investCount = 0;
                %>
									<logic:present name="iacucPersonsData">
										<logic:iterate id="data" name="iacucPersonsData"
											type="org.apache.struts.validator.DynaValidatorForm"
											scope="session">
											<%
                    String piFlag =(String) data.get("principleInvestigatorFlag");
                    if(!"".equals(piFlag)){
                      investCount++;

                    }
                %>

										</logic:iterate>
									</logic:present>
									<logic:present name="iacucPersonsData">
										<logic:iterate id="data" name="iacucPersonsData"
											type="org.apache.struts.validator.DynaValidatorForm"
											scope="session">
											<%--

--%>
											<tr bgcolor="<%=strBgColor%>"
												onmouseover="className='TableItemOn'"
												onmouseout="className='TableItemOff'">
												<%--

                --%>
												<td width='24%' class='copy'><bean:write name="data"
														property="personName" /></td>
												<%--
                 --%>
												<td width='24%' class='copy'>
													<% java.util.ArrayList vecInvUnits = (ArrayList)data.get("investigatorUnits");

                      String deptName= "";
                        String EMPTY_STRING = "";
                        String piFlag1 =(String) data.get("principleInvestigatorFlag");
                      for(int index=0 ; index<vecInvUnits.size() ; index++){
                          org.apache.struts.validator.DynaValidatorForm unitForm = (org.apache.struts.validator.DynaValidatorForm)vecInvUnits.get(index);
                          deptName = (String)unitForm.get("departmentName");%>
													<li><%=deptName%> </ii> <%}%>
												<td width='9%' class='copy' align="center"><logic:equal
														name="data" property="principleInvestigatorFlag" value="Y">
														<img
															src="<bean:write name='ctxtPath'/>/coeusliteimages/checked.gif">
													</logic:equal></td>
												<td class='copy'>
													<%
                         if (piFlag1==null || piFlag1.equals(EMPTY_STRING)) {%>
													<bean:write name="data" property="personRoleDesc" /> <%} else {%>
													<logic:equal name="data"
														property="principleInvestigatorFlag" value="Y">
														<bean:message bundle="iacuc"
															key="label.invesKeypersons.principalInvestigator" />
													</logic:equal> <logic:equal name="data"
														property="principleInvestigatorFlag" value="N">
														<bean:message bundle="iacuc"
															key="label.invesKeypersons.coInvestigator" />
													</logic:equal> <%}%>
												</td>

												<td class='copy'><bean:write name="data"
														property="affiliationTypeDescription" /></td>
												<%  String completeImage= request.getContextPath()+"/coeusliteimages/complete.gif";
                        String noneImage= request.getContextPath()+"/coeusliteimages/none.gif";%>
												<td align='center'><logic:equal name="data"
														property="trainingStatusFlag" value="0">
														<html:img src="<%=noneImage%>" />
													</logic:equal> <logic:equal name="data" property="trainingStatusFlag"
														value="1">
														<html:img src="<%=completeImage%>" />
													</logic:equal></td>
												<%count++;%>
											</tr>
										</logic:iterate>
									</logic:present>
								</table>
							</div>
						</td>
					</tr>

				</table>
			</td>
		</tr>
		<tr>
			<td height="3"></td>
		</tr>
		<%--..............................inv key person...ends.......................--%>
		<%--   .......................... List of Research Areas start.......................--%>
		<logic:notEmpty name="iacucAreaData">
			<tr valign=top>
				<td>
					<table width="98%" align=center border="0" cellpadding="0"
						cellspacing="0" class="tabtable">
						<tr valign=top>
							<td>
								<table width="100%" border="0" cellpadding="0" cellspacing="0"
									class="tableheader">
									<tr valign=top>
										<td width='50%' align=left nowrap>
											<div id='areaShowImage' style='display: block;'>
												<html:link href="javaScript:summaryHeader('5');">
													<html:img src="<%=plus%>" border="0" />
												</html:link>
												&nbsp;
												<bean:message bundle="iacuc"
													key="amendment.summary.areasOfResearch" />
											</div>
											<div id='areaHideImage' style='display: none;'>
												<html:link href="javaScript:summaryHeader('6');">
													<html:img src="<%=minus%>" border="0" />
												</html:link>
												&nbsp;
												<bean:message bundle="iacuc"
													key="amendment.summary.areasOfResearch" />
											</div>
										</td>
										<td width='50%' align=right><html:link
												href="javaScript:summaryHeader('5');">
												<div id='areaShow' style='display: block;'>
													<bean:message bundle="proposal" key="summary.show" />
												</div>
											</html:link> <html:link href="javaScript:summaryHeader('6');">
												<div id='areaHide' style='display: none;'>
													<bean:message bundle="proposal" key="summary.hide" />
												</div>
											</html:link></td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td height='2'></td>
						</tr>
						<tr>
							<td>
								<div id='areaDetails' style='display: none;'>
									<table width="98%" border="0" cellspacing="1" cellpadding="3"
										class="tabtable" align="center">
										<tr align="center" valign="top">
											<td width="20%" align="left" class="theader"><bean:message
													key="areasOfResch.Code" /></td>
											<td align="left" class="theader"><bean:message
													key="areasOfResch.Description" /></td>
										</tr>
										<logic:present name="iacucAreaData">
											<logic:iterate id="researchAreaList" name="iacucAreaData"
												type="org.apache.struts.validator.DynaValidatorForm">

												<%
                                       if (count%2 == 0)
                                          strBgColor = "#D6DCE5";
                                       else
                                          strBgColor="#DCE5F1";
                                    %>
												<tr bgcolor="<%=strBgColor%>" valign="top"
													onmouseover="className='TableItemOn'"
													onmouseout="className='TableItemOff'">
													<td align="left" nowrap class="copy"><%=researchAreaList.get("researchAreaCode")%>
													</td>
													<td align="left" nowrap class="copy"><%=researchAreaList.get("researchAreaDescription")%></td>

												</tr>
												<% count++;%>
											</logic:iterate>
										</logic:present>
									</table>
								</div>
							</td>
						</tr>

					</table>
				</td>
			</tr>
			<tr>
				<td height="3"></td>
			</tr>
		</logic:notEmpty>
		<%--   ............ List of Research Areas ends.......................--%>
		<%--   ............ List of Funding Sources start.......................--%>
		<logic:notEmpty name="iacucFundingSources">
			<tr valign=top>
				<td>
					<table width="98%" align=center border="0" cellpadding="0"
						cellspacing="0" class="tabtable">
						<tr valign=top>
							<td>
								<table width="100%" border="0" cellpadding="0" cellspacing="0"
									class="tableheader">
									<tr valign=top>
										<td width='50%' align=left nowrap>
											<div id='fundShowImage' style='display: block;'>
												<html:link href="javaScript:summaryHeader('7');">
													<html:img src="<%=plus%>" border="0" />
												</html:link>
												&nbsp;
												<bean:message bundle="iacuc"
													key="amendment.summary.fundingSource" />
											</div>
											<div id='fundHideImage' style='display: none;'>
												<html:link href="javaScript:summaryHeader('8');">
													<html:img src="<%=minus%>" border="0" />
												</html:link>
												&nbsp;
												<bean:message bundle="iacuc"
													key="amendment.summary.fundingSource" />
											</div>
										</td>
										<td width='50%' align=right><html:link
												href="javaScript:summaryHeader('7');">
												<div id='fundShow' style='display: block;'>
													<bean:message bundle="proposal" key="summary.show" />
												</div>
											</html:link> <html:link href="javaScript:summaryHeader('8');">
												<div id='fundHide' style='display: none;'>
													<bean:message bundle="proposal" key="summary.hide" />
												</div>
											</html:link></td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td height='2'></td>
						</tr>
						<tr>
							<td>
								<div id='fundDetails' style='display: none;'>
									<table width="98%" border="0" cellspacing="1" cellpadding="3"
										class="tabtable" align="center">
										<tr align="center" valign="top">
											<td width="20%" align="left" class="theader"><bean:message
													key="fundingSrc.Type" /></td>
											<td width="25%" align="left" class="theader"><bean:message
													key="fundingSrc.SpnsUnitNo" /></td>
											<td width="45%" align="left" class="theader"><bean:message
													key="fundingSrc.Name" /></td>
										</tr>
										<logic:present name="iacucFundingSources">
											<logic:iterate id="fundingSourceBean"
												name="iacucFundingSources"
												type="org.apache.struts.validator.DynaValidatorForm">
												<%
                                                            int count1=0;
                                                               if (count1%2 == 0)
                                                               strBgColor = "#D6DCE5";
                                                               else
                                                               strBgColor="#DCE5F1";
                                                           %>
												<tr bgcolor="<%=strBgColor%>"
													onmouseover="className='TableItemOn'"
													onmouseout="className='TableItemOff'">
													<td class="copy"><%=fundingSourceBean.get("description")%>
													</td>
													<td class="copy"><%=fundingSourceBean.get("fundingSource")%>
													</td>
													<td class="copy"><%=fundingSourceBean.get("fundingSourceName")%>
													</td>
												</tr>
												<% count1++;%>
											</logic:iterate>
										</logic:present>
										<%--  <%//}%>--%>
									</table>
								</div>
							</td>
						</tr>

					</table>
				</td>
			</tr>
			<tr>
				<td height="3"></td>
			</tr>
		</logic:notEmpty>
		<%--   ............ List of Funding Sources Ends.......................--%>
		<%--   ............ List of Special Review start.......................--%>
		<logic:notEmpty name="ReviewList">
			<tr valign=top>
				<td>
					<table width="98%" align=center border="0" cellpadding="0"
						cellspacing="0" class="tabtable">
						<tr valign=top>
							<td>
								<table width="100%" border="0" cellpadding="0" cellspacing="0"
									class="tableheader">
									<tr valign=top>
										<td width='50%' align=left nowrap>
											<div id='specialShowImage' style='display: block;'>
												<html:link href="javaScript:summaryHeader('9');">
													<html:img src="<%=plus%>" border="0" />
												</html:link>
												&nbsp;
												<bean:message bundle="iacuc"
													key="amendment.summary.specialReview" />
											</div>
											<div id='specialHideImage' style='display: none;'>
												<html:link href="javaScript:summaryHeader('10');">
													<html:img src="<%=minus%>" border="0" />
												</html:link>
												&nbsp;
												<bean:message bundle="iacuc"
													key="amendment.summary.specialReview" />
											</div>
										</td>
										<td width='50%' align=right><html:link
												href="javaScript:summaryHeader('9');">
												<div id='specialShow' style='display: block;'>
													<bean:message bundle="proposal" key="summary.show" />
												</div>
											</html:link> <html:link href="javaScript:summaryHeader('10');">
												<div id='specialHide' style='display: none;'>
													<bean:message bundle="proposal" key="summary.hide" />
												</div>
											</html:link></td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td height='2'></td>
						</tr>
						<tr>
							<td>
								<div id='specialDetails' style='display: none;'>
									<table width="98%" border="0" cellspacing="1" cellpadding="3"
										class="tabtable" align="center">
										<tr align="center" valign="top">
											<td width="20%" align="left" class="theader"><bean:message
													key="specialReviewLabel.SpecialReview" /></td>
											<td nowrap width="15%" class="theader" align="left"><bean:message
													key="specialReviewLabel.Approval" /></td>
											<td nowrap width="12%" class="theader" align="left"><bean:message
													key="specialReviewLabel.ProtocolNo" /></td>
											<td nowrap width="15%" class="theader" align="left"><bean:message
													key="specialReviewLabel.ApplDate" /></td>
											<td nowrap width="15%" class="theader" align="left"><bean:message
													key="specialReviewLabel.ApprDate" /></td>
											<td nowrap width="15%" class="theader" align="left"><bean:message
													key="specialReviewLabel.ExpirationDate" bundle="iacuc" /></td>
											<td nowrap width='8%' class='theader' align="center"><bean:message
													key="specialReviewLabel.Comments" /></td>
										</tr>
										<logic:present name="ReviewList">
											<logic:iterate id="review" name="ReviewList"
												type="org.apache.struts.validator.DynaValidatorForm"
												scope="session">
												<% String protoNum = "";
                                            if(review.get("spRevProtocolNumber") == null){
                                                protoNum = "";
                                                }else{
                                                  protoNum = review.get("spRevProtocolNumber").toString();
                                                    }
                                            %>
												<%
                                               if (count%2 == 0)
                                               strBgColor = "#D6DCE5";
                                               else
                                               strBgColor="#DCE5F1";
                                               String editLink = "javascript:edit_data('"+review.get("specialReviewNumber") +"','" +review.get("updateTimestamp") +"')";
                                               boolean flag = true;
                                               if(review.get("showInLite") !=null &&
                                                       review.get("showInLite").equals("N")){
                                                   flag = false;
                                               }
                                           %>

												<tr align="left" class="copy" bgcolor="<%=strBgColor%>"
													onmouseover="className='TableItemOn'"
													onmouseout="className='TableItemOff'">
													<td align="left" nowrap class="copy"><%=review.get("specialReviewDescription")%>
													</td>
													<td class='copy'><%=review.get("approvalDescription").toString()%>
													</td>
													<td class='copy'><%=protoNum%></td>
													<td class='copy' align="left">
														<%String applDate = "";
                                              if(review.get("applicationDate") == null){
                                                  applDate = "";
                                                }else{
                                                  applDate = (String)review.get("applicationDate");
                                                }
                                                %> <%=applDate%>
													</td>
													<td class='copy' align="left">
														<%--String dateValue = dateUtils.formatDate(review.get("approvalDate").toString(),edu.mitweb.coeus.irb.DateUtils.MM_DD_YYYY);--%>
														<%String apprvDate = "";
                                                  if(review.get("approvalDate") == null){
                                                      apprvDate = "";
                                                    }else{
                                                      apprvDate = (String)review.get("approvalDate");
                                                    }
                                                    %> <%=apprvDate%></td>
													<td class='copy' align="left">
														<%--String dateValue = dateUtils.formatDate(review.get("approvalDate").toString(),edu.mitweb.coeus.iacuc.DateUtils.MM_DD_YYYY);--%>
														<%String expirationDate = "";
                                                  if(review.get("expirationDate") == null){
                                                      expirationDate = "";
                                                    }else{
                                                      expirationDate = (String)review.get("expirationDate");
                                                    }
                                                    %> <%=expirationDate%>
													</td>
													<td class='copy' align="center">
														<%
                                                    String viewLink = "javascript:view_comments('" +0+"')";
                                                %> <html:link
															href="<%=viewLink%>">
															<bean:message key="label.View" />
														</html:link>
													</td>
												</tr>

												<% count++;%>
											</logic:iterate>
										</logic:present>
										<%//}%>
									</table>
								</div>
							</td>
						</tr>

					</table>
				</td>
			</tr>
			<tr>
				<td height="3"></td>
			</tr>
		</logic:notEmpty>
		<%--   ............ List of Special Review end.......................--%>
		<%--   ............ List of Attachments start.......................--%>
		<logic:notEmpty name="uploadLatestData">
			<tr valign=top>
				<td>
					<table width="98%" align=center border="0" cellpadding="0"
						cellspacing="0" class="tabtable">
						<tr valign=top>
							<td>
								<table width="100%" border="0" cellpadding="0" cellspacing="0"
									class="tableheader">
									<tr valign=top>
										<td width='50%' align=left nowrap>
											<div id='attachmentShowImage' style='display: block;'>
												<html:link href="javaScript:summaryHeader('11');">
													<html:img src="<%=plus%>" border="0" />
												</html:link>
												&nbsp;
												<bean:message bundle="iacuc" key="uploadDocLabel.Attachment" />
											</div>
											<div id='attachmentHideImage' style='display: none;'>
												<html:link href="javaScript:summaryHeader('12');">
													<html:img src="<%=minus%>" border="0" />
												</html:link>
												&nbsp;
												<bean:message bundle="iacuc" key="uploadDocLabel.Attachment" />
											</div>
										</td>
										<td width='50%' align=right><html:link
												href="javaScript:summaryHeader('11');">
												<div id='attachmentShow' style='display: block;'>
													<bean:message bundle="proposal" key="summary.show" />
												</div>
											</html:link> <html:link href="javaScript:summaryHeader('12');">
												<div id='attachmentHide' style='display: none;'>
													<bean:message bundle="proposal" key="summary.hide" />
												</div>
											</html:link></td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td height='2'></td>
						</tr>
						<tr>
							<td>
								<div id='attachmentDetails' style='display: none;'>
									<table width="98%" border="0" cellspacing="1" cellpadding="3"
										class="tabtable" align="center">
										<tr align="center" valign="top">

											<td width="25%" align="left" class="theader"><bean:message
													key="uploadDocLabel.Type" /></td>
											<td width="65%" align="left" class="theader"><bean:message
													key="uploadDocLabel.Description" /></td>
											<td class="theader"></td>
											<!--<td width="15%" align="left" class="theader"><bean:message key="generalInfoLabel.Status"/></td>
                                        <td width="10%" align="left" class="theader"><bean:message key="label.AmendRenew.versionNo"/></td>-->
											<%--if(!modeValue){--%>

											<%--<td width="10%" align="center" class="theader">&nbsp;</td>--%>
										</tr>
										<logic:present name="uploadLatestData">
											<logic:iterate id="uploadDocList" name="uploadLatestData"
												type="edu.mit.coeus.iacuc.bean.UploadDocumentBean">
												<tr align="left" class="copy" bgcolor="<%=strBgColor%>"
													onmouseover="className='TableItemOn'"
													onmouseout="className='TableItemOff'">

													<td class="copy"><%=uploadDocList.getDocType()%></td>
													<td class="copy"
														style="padding-left: 8px; padding-right: 8px;"><%=uploadDocList.getDescription()%>
													</td>
													<td class="copy" align="center">
														<%
                   /* //Modified for Case#3036 - Didn't recognize document file type in Protocol attachments  -Start
                    viewLink = "javascript:view_data('V','" +uploadDocList.getDocCode() +"',"+"'"+uploadDocList.getVersionNumber()+"','"+uploadDocList.getSequenceNumber()+"','N')";
                    //Modified for Case#3036 - Didn't recognize document file type in Protocol attachments  -End*/
                   String viewLink1 = "javascript:view_data('V','" +uploadDocList.getDocCode() +"',"+"'"+uploadDocList.getVersionNumber()+"','"+uploadDocList.getSequenceNumber()+"','"+uploadDocList.getDocumentId()+"','N')";
                %> &nbsp;&nbsp; <html:link href="<%=viewLink1%>">
															<bean:message key="label.View" />
														</html:link>
													</td>
												</tr>
											</logic:iterate>
										</logic:present>
									</table>
								</div>
							</td>
						</tr>

					</table>
				</td>
			</tr>
			<tr>
				<td height="3"></td>
			</tr>
		</logic:notEmpty>
		<%--   ............ List of Attachments end.......................--%>
		<%--   ............ List of Alternative search starts.......................--%>
		<logic:notEmpty name="vecAddedAlternativeSearches">
			<tr valign=top>
				<td>
					<table width="98%" align=center border="0" cellpadding="0"
						cellspacing="0" class="tabtable">
						<tr valign=top>
							<td>
								<table width="100%" border="0" cellpadding="0" cellspacing="0"
									class="tableheader">
									<tr valign=top>
										<td width='50%' align=left nowrap>
											<div id='alternativeShowImage' style='display: block;'>
												<html:link href="javaScript:summaryHeader('13');">
													<html:img src="<%=plus%>" border="0" />
												</html:link>
												&nbsp;
												<bean:message bundle="iacuc"
													key="alternativeSearch.altSearch" />
											</div>
											<div id='alternativeHideImage' style='display: none;'>
												<html:link href="javaScript:summaryHeader('14');">
													<html:img src="<%=minus%>" border="0" />
												</html:link>
												&nbsp;
												<bean:message bundle="iacuc"
													key="alternativeSearch.altSearch" />
											</div>
										</td>
										<td width='50%' align=right><html:link
												href="javaScript:summaryHeader('13');">
												<div id='alternativeShow' style='display: block;'>
													<bean:message bundle="proposal" key="summary.show" />
												</div>
											</html:link> <html:link href="javaScript:summaryHeader('14');">
												<div id='alternativeHide' style='display: none;'>
													<bean:message bundle="proposal" key="summary.hide" />
												</div>
											</html:link></td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td height='2'></td>
						</tr>
						<tr>
							<td>
								<div id='alternativeDetails' style='display: none;'>
									<table width="98%" border="0" cellspacing="1" cellpadding="3"
										class="tabtable" align="center">
										<tr align="center" valign="top">
											<td width="15%" align="left" class="theader">Search
												Date</td>
											<td width="22%" align="left" class="theader">Database
												Searched</td>
											<td width="10%" align="left" class="theader">Years</td>
											<td width="20%" align="left" class="theader">Keywords</td>
											<td width="25%" align="left" class="theader">Comments</td>
										</tr>
										<logic:present name="vecAddedAlternativeSearches">
											<logic:iterate id="alternativeSearchData"
												name="vecAddedAlternativeSearches"
												type="org.apache.struts.validator.DynaValidatorForm"
												indexId="index">
												<%
                                    if (count%2 == 0)
                                    strBgColor = "#D6DCE5";
                                    else
                                    strBgColor="#DCE5F1";
                                    %>
												<%
                                       // edu.mit.coeuslite.utils.DateUtils dateUtils = new edu.mit.coeuslite.utils.DateUtils();
                                        String date  = (String)alternativeSearchData.get("searchDate");
                                        date = (date==null)?"":date.trim();
                                        if(!"".equals(date)) {

                                        try{
                                        date = dateUtils.formatDate(date,"MM/dd/yyyy");
                                        } catch (Exception e){

                                        }
                                        }
                                        alternativeSearchData.set("searchDate", date);
                                        %>
												<tr align="left" class="copy" bgcolor="<%=strBgColor%>"
													onmouseover="className='TableItemOn'"
													onmouseout="className='TableItemOff'">
													<td nowrap width="15%" align="left" class="copy"><bean:write
															name="alternativeSearchData" property="searchDate" /></td>
													<%--
--%>
													<td width="22%" align="left" class="copy"><bean:write
															name="alternativeSearchData"
															property="alternativeDBSearchDesc" /></td>
													<%--

  --%>
													<%
                                      String   selectedItem = "yearsSearched";
                                      String   yearsSearched  = (String)alternativeSearchData.get("yearsSearched");
                                       // yearsSearched = (yearsSearched==null)?"":yearsSearched.trim();
                                      //  viewLink = "javaScript:viewComments('"+count+"','"+selectedItem+"')";
                                       // if(yearsSearched.length() > 27){
                                           //   yearsSearched = yearsSearched.substring(0,20);
                                        %><%--
--%>
													<td width="10%" align="left" class="copy"><%=yearsSearched%></td>
													<%--
--%>
													<%
                                       selectedItem = "keyWordsSearched";
                                        String keyWords  = (String)alternativeSearchData.get("keyWordsSearched");
                                       // keyWords = (keyWords==null)?"":keyWords.trim();
                                       // viewLink = "javaScript:viewComments('"+count+"','"+selectedItem+"')";
                                       // if(keyWords.length() > 27){
                                          //    keyWords = keyWords.substring(0,20);
                                        %><%--
                                        --%>
													<td width="20%" align="justify" class="copy"><%=keyWords%></td>
													<%--
 --%>
													<%
                                        selectedItem = "comments";
                                        String comments  = (String)alternativeSearchData.get("comments");
                                      //  comments = (comments==null)?"":comments.trim();
                                       // viewLink = "javaScript:viewComments('"+count+"','"+selectedItem+"')";
                                      //  if(comments.length() > 27){
                                       //     comments = comments.substring(0,20);
                                        %>
													<td width="25%" align="justify"
														style="padding-left: 8px; padding-right: 8px" class="copy"><%=comments%></td>
													<%--
--%>
												</tr>
												<% count++;%>
											</logic:iterate>
										</logic:present>
									</table>
								</div>
							</td>
						</tr>

					</table>
				</td>
			</tr>
			<tr>
				<td height="3"></td>
			</tr>
		</logic:notEmpty>
		<%--   ............ List of Alternative search ends.......................--%>
		<%--   ............ List of Species/Groups starts.......................--%>
		<logic:notEmpty name="vecProtoSpeciesData">
			<tr valign=top>
				<td>
					<table width="98%" align=center border="0" cellpadding="0"
						cellspacing="0" class="tabtable">
						<tr valign=top>
							<td>
								<table width="100%" border="0" cellpadding="0" cellspacing="0"
									class="tableheader">
									<tr valign=top>
										<td width='50%' align=left nowrap>
											<div id='speciesShowImage' style='display: block;'>
												<html:link href="javaScript:summaryHeader('15');">
													<html:img src="<%=plus%>" border="0" />
												</html:link>
												&nbsp;
												<bean:message bundle="iacuc"
													key="iacuc.speciesGroups.addSpeGrops" />
											</div>
											<div id='speciesHideImage' style='display: none;'>
												<html:link href="javaScript:summaryHeader('16');">
													<html:img src="<%=minus%>" border="0" />
												</html:link>
												&nbsp;
												<bean:message bundle="iacuc"
													key="iacuc.speciesGroups.addSpeGrops" />
											</div>
										</td>
										<td width='50%' align=right><html:link
												href="javaScript:summaryHeader('15');">
												<div id='speciesShow' style='display: block;'>
													<bean:message bundle="proposal" key="summary.show" />
												</div>
											</html:link> <html:link href="javaScript:summaryHeader('16');">
												<div id='speciesHide' style='display: none;'>
													<bean:message bundle="proposal" key="summary.hide" />
												</div>
											</html:link></td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td height='2'></td>
						</tr>
						<tr>
							<td>
								<div id="specdet" style='display: none;'>
									<%
            Vector vecProtoSpeciesData1 = (Vector) session.getAttribute("vecProtoSpeciesData");
            int index = 0;

%>
									<logic:present name="vecProtoSpeciesData">
										<table width="98%" border="0" cellspacing="1" cellpadding="3"
											class="tabtable" align="center">

											<tr align="center" valign="top">
												<td width="3%" align="left" class="theader"></td>
												<td width="12%" align="left" class="theader">Group</td>
												<td width="16%" align="left" class="theader">Species</td>
												<td width="14%" align="left" class="theader">Species
													Strain</td>
												<td width="12%" align="left" class="theader">Pain
													Category</td>
												<td width="17%" align="left" class="theader">USDA
													Covered Type</td>
												<td width="10%" align="left" class="theader">Count Type</td>
												<td width="6%" align="left" class="theader">Count</td>
												<td width="8%" align="center" class="theader">Exception</td>
											</tr>

											<logic:iterate id="speciesandGroups"
												name="vecProtoSpeciesData">
												<tr class="copy" bgcolor="<%=strBgColor%>"
													onmouseover="className='TableItemOn'"
													onmouseout="className='TableItemOff'">
													<td><img
														src='<%=request.getContextPath()%>/coeusliteimages/plus.gif'
														border='none' style="display: none;"
														id="projectimgtoggle<%=index%>" border="none"
														onclick="javascript:selectProjIconPlus('<%=index%>');" />
														<img
														src='<%=request.getContextPath()%>/coeusliteimages/minus.gif'
														style="display: block;" border='none'
														id="projectimgtoggleminus<%=index%>" border="none"
														onclick="javascript:selectProjIconMinus('<%=index%>');" />
													</td>
													<%
                                DynaValidatorForm dynaForm = (DynaValidatorForm)vecProtoSpeciesData1.get(index);
                                String spname=  (String)dynaForm.get("speciesName");
                                String groupname=  (String)dynaForm.get("groupName");
                                Integer spId=(Integer)dynaForm.get("speciesId");
                                String paincat=  (String)dynaForm.get("painCategoryDesc");
                          %>
													<td align="left" class="copy"><%=dynaForm.getString("groupName")%></td>
													<td align="left" class="copy"><%=dynaForm.getString("speciesName")%></td>
													<%
                        String strain = "";
                        if(dynaForm.getString("strain")!=null){
                        strain = dynaForm.getString("strain");
                        }else{
                        strain = "";
                        }
                        %>
													<td align="left" class="copy"><%=strain%></td>
													<td align="left" class="copy"><%=dynaForm.getString("painCategoryDesc")%></td>
													<td align="left" class="copy"><%=dynaForm.getString("isUsdaCovered")%></td>

													<td align="left" class="copy"><logic:present
															name="speciesCountTypeDesc"><%=dynaForm.getString("speciesCountTypeDesc")%></logic:present></td>
													<%
String speciesCount = "";
if(dynaForm.getString("speciesCount")!=null){
speciesCount = dynaForm.getString("speciesCount");
}else{
speciesCount = "";
}
%>
													<td align="left" class="copy"><%=speciesCount%></td>

													<td align="center" class="copy"><logic:equal
															name="speciesandGroups" property="isExceptionPresent"
															value="Y">
															<img
																src="<bean:write name='ctxtPath'/>/coeusliteimages/checked.gif">
														</logic:equal></td>
												</tr>
												<tr>
													<td colspan="9">


														<div id="<%=index%>" style="display: block;">
															<table width="100%" align=center border="0"
																cellpadding="0" cellspacing="0" class="tabtable">
																<tr align="center" valign="top">
																	<td width="30%" align="left" class="theader">Procedure
																		category</td>
																	<td width="55%" align="left" class="theader">Procedure</td>
																	<td width="15%" align="center" class="theader">Procedure
																		Count</td>

																</tr>
																<logic:notPresent name="vecApplicableProcedures1">
																	<tr>
																		<td>No Procedures found</td>
																	</tr>
																</logic:notPresent>

																<logic:present name="vecApplicableProcedures1">
																	<div id="<%=index%>" style="display: block;">
																		<table width="100%" align=center border="0"
																			cellpadding="0" cellspacing="0" class="tabtable">

																			<%
                        Vector vecAcProcedures1 = (Vector) session.getAttribute("vecApplicableProcedures1");
                            int procCount = 0;
                            int ind = 0;
                    %>
																			<logic:iterate id="getProDetails"
																				name="vecApplicableProcedures1" scope="session">
																				<%
                        String procedure = "";
                         int spid1=0;
                         String paincat1="";
                         String groupname1="";
                         String spcsname = "";
                        if(vecAcProcedures1 != null && vecAcProcedures1.size() > 0) {
                                DynaValidatorForm dynaFormProc = (DynaValidatorForm)vecAcProcedures1.get(procCount);
                           spcsname =dynaFormProc.getString("speciesName");

                        groupname1= dynaFormProc.getString("groupName");
                          spid1=(Integer)dynaFormProc.get("speciesId");
                            paincat1= dynaFormProc.getString("procedureCategoryDesc");
                        }
                       // spId ==spid1
                         // paincat1!=paincat
                       // && groupname1.trim().equals(groupname)
                       if(spcsname.trim().equals(spname)&&spId ==spid1) {
                      %>
																				<tr align="left">
																					<td width="30%"><bean:write
																							name="getProDetails"
																							property="procedureCategoryDesc" /></td>
																					<td width="55%"><bean:write
																							name="getProDetails" property="procedureDesc" /></td>
																					<td align="center"><bean:write
																							name="getProDetails" property="count" /></td>
																				</tr>

																				<%
                      ind++;
                       }
                        %>
																				<%procCount++;%>
																			</logic:iterate>
																			<%if(ind==0){
%>
																			<tr>
																				<td>No Procedures found</td>
																			</tr>
																			<%}
%>
																			</logic:present>
																		</table>
																	</div>
																	<%
                    index++;
                %>
																	</td>
																	</tr>

																	</logic:iterate>
															</table>
															</logic:present>
														</div>
													</td>
												</tr>
										</table>
							</td>
						</tr>
						<tr>
							<td height="3"></td>
						</tr>
						</logic:notEmpty>
						<%--   ............ List of Species/Groups ends.......................--%>
						<%--.......................................end.............................--%>
						<%-- Added for  COEUSQA-3483 : Changes to Protocol Summary screen in Routing in Lite - start--%>
						<logic:present name="questionAnsMapForIACUC">
							<tr valign=top>
								<td>
									<table width="98%" align=center border="0" cellpadding="0"
										cellspacing="0" class="tabtable">
										<tr>
											<td valign="top">
												<table width="100%" border="0" cellpadding="0"
													cellspacing="0" class="tableheader">
													<tr>
														<td width='50%' align=left nowrap valign="top">
															<div id='QuestionShowImage' style='display: block;'>
																<html:link href="javaScript:summaryHeader('23');">
																	<html:img src="<%=plus%>" border="0" />
																</html:link>
																&nbsp;Questionnaire &nbsp;
															</div>
															<div id='QuestiontHideImage' style='display: none;'>
																<html:link href="javaScript:summaryHeader('24');">
																	<html:img src="<%=minus%>" border="0" />
																</html:link>
																&nbsp;Questionnaire
															</div>
														</td>



														<td width='50%' align=right valign="top"><html:link
																href="javaScript:summaryHeader('23');">
																<div id='QuestionShow' style='display: block;'>
																	<bean:message bundle="proposal" key="summary.show" />
																</div>
															</html:link> <html:link href="javaScript:summaryHeader('24');">
																<div id='QuestionHide' style='display: none;'>
																	<bean:message bundle="proposal" key="summary.hide" />
																</div>
															</html:link></td>
													</tr>
												</table>
											</td>

										</tr>
										<tr>
											<td height='2'></td>
										</tr>

										<tr>
											<td valign="top">
												<div id="qnrmainDiv" style='display: none;'>

													<%
                              int divIndex = 0;
                              Map questMap = (HashMap) request.getAttribute("questionAnsMapForIACUC");
                              Set keys = new HashSet();
                              Vector answerBeans = new Vector();
                              if (questMap != null) {
                                  
                                  keys = questMap.keySet();
                              }
                              for (Iterator it = keys.iterator(); it.hasNext();) {
                                  String questionaireName = (String) it.next();
                                  answerBeans = (Vector) questMap.get(questionaireName);
                                  divIndex++;                          
                              %>

													<table width="98%" border="0" cellpadding="0"
														cellspacing="0" class="tabtable" align="center">
														<tr>
															<td colspan="4" valign="top">
																<div id='budgetReport'>
																	<table width="100%" align=center border="0"
																		cellpadding="0" cellspacing="0" class='theader'>
																		<tr>

																			<td width='50%' align=left nowrap valign="top">
																				<div id='QnrShowImage<%=divIndex%>'
																					style='display: block;'>
																					<a
																						href="javaScript:dynamicQuestionsDivShow('Inner<%= divIndex%>','QnrShowImage<%= divIndex%>','QnrShow<%= divIndex%>','QnrHide<%= divIndex%>','QnrHideImage<%= divIndex%>');">
																						&nbsp;<html:img src="<%=plus%>" border="0" />
																					</a>&nbsp;<%= questionaireName%>
																				</div>
																				<div id='QnrHideImage<%=divIndex%>'
																					style='display: none;'>
																					<a
																						href="javaScript:dynamicQuestionsDivHide('Inner<%= divIndex%>','QnrShowImage<%= divIndex%>','QnrShow<%= divIndex%>','QnrHide<%= divIndex%>','QnrHideImage<%= divIndex%>');">
																						&nbsp;<html:img src="<%=minus%>" border="0" />
																					</a>&nbsp;<%= questionaireName%>
																				</div>
																			</td>

																			<td width='50%' align=right valign="top"><a
																				href="javaScript:dynamicQuestionsDivShow('Inner<%= divIndex%>','QnrShowImage<%= divIndex%>','QnrShow<%= divIndex%>','QnrHide<%= divIndex%>','QnrHideImage<%= divIndex%>');">
																					<div id='QnrShow<%=divIndex%>'
																						style='display: block;'>
																						<bean:message bundle="proposal" key="summary.show" />
																					</div>
																			</a> <a
																				href="javaScript:dynamicQuestionsDivHide('Inner<%= divIndex%>','QnrShowImage<%= divIndex%>','QnrShow<%= divIndex%>','QnrHide<%= divIndex%>','QnrHideImage<%= divIndex%>');">
																					<div id='QnrHide<%=divIndex%>'
																						style='display: none;'>
																						<bean:message bundle="proposal" key="summary.hide" />
																					</div>
																			</a></td>
																		</tr>
																	</table>
																</div>
															</td>
														</tr>

														<tr>
															<td valign="top"><div id="Inner<%=divIndex%>"
																	style='display: none;'>
																	<table width="100%" border="0" cellpadding="0"
																		cellspacing="0" style="font-size: 12px;">

																		<% int qstid = 0;
                                                  String qnrId = "";
                                                  String vrsn = "";
                                                  int qnrInt = 0;
                                                  int qvrsn = 0;%>
																		<%
                                                  for (Iterator it1 = answerBeans.iterator(); it1.hasNext();) {
                                                      QuestionAnswerProposalSummaryBean questBean = (QuestionAnswerProposalSummaryBean) it1.next();
                                                      String question = questBean.getQuestion();
                                                      String questid = questBean.getQuestionId();
                                                      qnrId = questBean.getQuestionnaireId();
                                                      qnrInt = Integer.parseInt(questBean.getQuestionnaireId());                                                      
                                                      qstid++;
                                                  %>

																		<tr>
																			<td colspan="4" valign="top"></td>
																		</tr>
																		<tr>
																			<td width="30" valign="top"></td>
																			<td width="30" valign="top"><%=qstid%> )</td>
																			<td valign="top">

																				<div><%= question%>
																				</div>

																			</td>
																			<td class='copybold' align=left width="15%"
																				valign="top">
																				<%String queLink = "javascript:showQuestion('/showQuestionExplanation.do?questionNo=" + questid + "&questionDesc=" + question + "')";%>
																				<html:link href="<%=queLink%>">
                                                              more
                                                          </html:link>
																			</td>
																		</tr>
																		<tr>
																			<td width="30" valign="top"></td>
																			<td width="30" valign="top"></td>

																			<td valign="top">
																				<%   if (questBean.getValidAnswers().equalsIgnoreCase("YN")) {
                                                              if (questBean.getAnswer().equalsIgnoreCase("Y")) {
                                                          
                                                          %> <input
																				type="radio" id="question" value="Y" checked="true"
																				disabled="true" />Yes &nbsp; <input type="radio"
																				id="question" value="N" disabled="true" />No <% }
                                                              if (questBean.getAnswer().equalsIgnoreCase("N")) {%>
																				<input type="radio" id="question" value="Y"
																				disabled="true" />Yes &nbsp; <input type="radio"
																				id="question" value="N" checked="true"
																				disabled="true" />No <% }
                                                          }
                                                          if (questBean.getValidAnswers().equalsIgnoreCase("YNX")) {
                                                              if (questBean.getAnswer().equalsIgnoreCase("Y")) {
                                                          %> <input
																				type="radio" id="question" value="Y" checked="true"
																				disabled="true" />Yes &nbsp; <input type="radio"
																				id="question" value="N" disabled="true" />Yes &nbsp;
																				<input type="radio" id="question" value="X"
																				disabled="true" />N/A &nbsp; <% }
                                                              if (questBean.getAnswer().equalsIgnoreCase("N")) {%>

																				<input type="radio" id="question" value="Y"
																				disabled="true" />Yes &nbsp; <input type="radio"
																				id="question" value="N" checked="true"
																				disabled="true" />No &nbsp; <input type="radio"
																				id="question" value="X" disabled="true" />N/A &nbsp;

																				<% }
                                                              if (questBean.getAnswer().equalsIgnoreCase("X")) {%>
																				<input type="radio" id="question" value="Y"
																				disabled="true" />Yes &nbsp; <input type="radio"
																				id="question" value="N" disabled="true" />Yes &nbsp;
																				<input type="radio" id="question" value="X"
																				checked="true" disabled="true" />N/A <% }
                                                          }
                                                          if (questBean.getValidAnswers().equalsIgnoreCase("Text")) {
                                                              String Answr = questBean.getAnswer();
                                                          %> <input
																				type="text" id="question" size="20" maxlength="10"
																				value="<%=Answr%>" disabled="true" /> <% }
                                                          if (questBean.getValidAnswers().equalsIgnoreCase("Search")) {
                                                              String Answrsrch = questBean.getAnswer();
                                                          %> <input
																				type="text" id="question" size="20" maxlength="10"
																				value="<%=Answrsrch%>" disabled="true" /> <% }%>


																			</td>

																		</tr>

																		<%
                                                  }
                                                  %>

																		<tr colspan="4">
																			<td valign="top"></td>
																		</tr>
																		<tr align="left" class='table'>

																			<td class='savebutton' colspan="4" nowrap
																				style="padding-top: 3px; padding-left: 5px; padding-bottom: 3px;"
																				valign="top">
																				<%String printbtn = request.getContextPath() + "/coeusliteimages/print-button.gif";%>
																				<a
																				href="javascript:view_questionnaire_data('<%=qnrInt%>')"><img
																					style="border-style: none;" alt="print"
																					src="<%=printbtn%>" /></a>

																			</td>

																		</tr>
																	</table>
																</div></td>
														</tr>
														<tr>
															<td height="2"></td>
														</tr>
													</table>


													<%
                              }
                              
                              %>



												</div>
											</td>
										</tr>
										<tr>
											<td height="2"></td>
										</tr>
									</table>
								</td>
							</tr>
						</logic:present>
						<%-- Added for  COEUSQA-3483 : Changes to Protocol Summary screen in Routing in Lite - end--%>
					</table>
</body>
</html>
