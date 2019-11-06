<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%--Added for COEUSQA-3483 : Changes to Protocol Summary screen in Routing in Lite - start--%>
<%@ page
	import="java.util.*,edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean,edu.utk.coeuslite.propdev.bean.QuestionAnswerProposalSummaryBean, edu.wmc.coeuslite.utils.DynaBeanList, edu.mit.coeus.sponsormaint.bean.SponsorFormsBean"%>
<%--Added for COEUSQA-3483 : Changes to Protocol Summary screen in Routing in Lite - end--%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@ page
	import="org.apache.struts.validator.DynaValidatorForm,
        edu.mit.coeuslite.utils.CoeusLiteConstants,
        java.util.Vector,                
        edu.mit.coeuslite.utils.ComboBoxBean,
        edu.mit.coeuslite.utils.DateUtils"%>
<%@page import="org.apache.struts.taglib.html.JavascriptValidatorTag"%>
<jsp:useBean id="vecRolesType" scope="session" class="java.util.Vector" />
<jsp:useBean id="vecAffiliationType" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="proposalNumber" scope="request"
	class="java.lang.String" />
<jsp:useBean id="VulnerableData" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="splReview" scope="session" class="java.util.Vector" />
<jsp:useBean id="approval" scope="session" class="java.util.Vector" />
<jsp:useBean id="ReviewList" scope="session" class="java.util.Vector" />
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="uploadLatestData" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="historyData" scope="request" class="java.util.Vector" />
<jsp:useBean id="parentProtoData" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="protocolReferenceTypes" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="protocolReferenceList" scope="session"
	class="java.util.Vector" />
<%@page
	import="java.sql.Date, edu.mit.coeuslite.irb.bean.ProtocolHeaderDetailsBean, java.text.SimpleDateFormat"%>
<jsp:useBean id="getProtocolHeaderDetails" scope="session"
	class="edu.mit.coeuslite.irb.bean.ProtocolHeaderDetailsBean" />
<jsp:useBean id="areaData" scope="session" class="java.util.Vector" />
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%  DateUtils dateUtils = new edu.mit.coeuslite.utils.DateUtils();
%>





<html>
<html:html locale="true">
<title>IRBSummary</title>
<script language="javascript" type="text/JavaScript"
	src="<%=request.getContextPath()%>/coeuslite/mit/utils/scripts/divSlide.js"></script>
<script language="javascript" type="text/JavaScript"
	src="..\scripts\_js.js"></script>
<script language="javascript" type="text/JavaScript"
	src="..\scripts\CheckForward.js"></script>
<script language="JavaScript" type="text/JavaScript">
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
            document.getElementById('protohideimagecorrespondents').style.display = 'block';
            document.getElementById('corespHide').style.display = 'block';
            document.getElementById('correspDetails').style.display = 'block';
            document.getElementById('corespShow').style.display = 'none';
            document.getElementById('protoshowimagecorrespondents').style.display = 'none';
        }
           else if(value==6) {
            document.getElementById('protohideimagecorrespondents').style.display = 'none';
            document.getElementById('corespHide').style.display = 'none';
             document.getElementById('correspDetails').style.display = 'none';
            document.getElementById('corespShow').style.display = 'block';
            document.getElementById('protoshowimagecorrespondents').style.display = 'block';
        }
        else if(value==11) {
            document.getElementById('areaHideImage').style.display = 'block';
            document.getElementById('areaHide').style.display = 'block';
            document.getElementById('areaDetails').style.display = 'block';
            document.getElementById('areaShow').style.display = 'none';
            document.getElementById('areaShowImage').style.display = 'none';
        }
           else if(value==12) {
            document.getElementById('areaHideImage').style.display = 'none';
            document.getElementById('areaHide').style.display = 'none';
            document.getElementById('areaDetails').style.display = 'none';
            document.getElementById('areaShow').style.display = 'block';
            document.getElementById('areaShowImage').style.display = 'block';
        }
            else if(value==7) {
            document.getElementById('protosfundingsourcehideimage').style.display = 'block';
            document.getElementById('fundHide').style.display = 'block';
            document.getElementById('fundingSourceDetails').style.display = 'block';
            document.getElementById('fundShow').style.display = 'none';
            document.getElementById('protosfundingsourceshowimage').style.display = 'none';
        }
           else if(value==8) {
           document.getElementById('protosfundingsourcehideimage').style.display = 'none';
            document.getElementById('fundHide').style.display = 'none';
            document.getElementById('fundingSourceDetails').style.display = 'none';
            document.getElementById('fundShow').style.display = 'block';
            document.getElementById('protosfundingsourceshowimage').style.display = 'block';
        }
                 else if(value==9) {
            document.getElementById('protossubjectshideimage').style.display = 'block';
            document.getElementById('subjectHide').style.display = 'block';
            document.getElementById('subjectDetails').style.display = 'block';
            document.getElementById('subjectShow').style.display = 'none';
            document.getElementById('protossubjectsshowimage').style.display = 'none';
        }
           else if(value==10) {
            document.getElementById('protossubjectshideimage').style.display = 'none';
            document.getElementById('subjectHide').style.display = 'none';
            document.getElementById('subjectDetails').style.display = 'none';
            document.getElementById('subjectShow').style.display = 'block';
            document.getElementById('protossubjectsshowimage').style.display = 'block';
        
        }
                  else if(value==13) {
            document.getElementById('protosspecialreviewhideimage').style.display = 'block';
            document.getElementById('specialHide').style.display = 'block';
            document.getElementById('specialreviewDetails').style.display = 'block';
            document.getElementById('specialShow').style.display = 'none';
            document.getElementById('protosspecialreviewshowimage').style.display = 'none'
        }
           else if(value==14) {
            document.getElementById('protosspecialreviewhideimage').style.display = 'none';
            document.getElementById('specialHide').style.display = 'none';
            document.getElementById('specialreviewDetails').style.display = 'none';
            document.getElementById('specialShow').style.display = 'block';
            document.getElementById('protosspecialreviewshowimage').style.display = 'block'
        }
                  else if(value==19) {
            document.getElementById('attachmenthideimage').style.display = 'block';
            document.getElementById('attachHide').style.display = 'block';
            document.getElementById('atachmentDetails').style.display = 'block';
            document.getElementById('attachShow').style.display = 'none';
            document.getElementById('attachmentshowimage').style.display = 'none'
        }
           else if(value==20) {
             document.getElementById('attachmenthideimage').style.display = 'none';
            document.getElementById('attachHide').style.display = 'none';
            document.getElementById('atachmentDetails').style.display = 'none';
            document.getElementById('attachShow').style.display = 'block';
            document.getElementById('attachmentshowimage').style.display = 'block'
        }
           else if(value==21) {
           document.getElementById('protosreferencelisthideimage').style.display = 'block';
            document.getElementById('refHide').style.display = 'block';
            document.getElementById('refDetails').style.display = 'block';
             document.getElementById('refShow').style.display = 'none';
            document.getElementById('protosreferencelistshowimage').style.display = 'none';
        }
           else if(value==22) {
           document.getElementById('protosreferencelisthideimage').style.display = 'none';
            document.getElementById('refHide').style.display = 'none';
            document.getElementById('refDetails').style.display = 'none';
             document.getElementById('refShow').style.display = 'block';
            document.getElementById('protosreferencelistshowimage').style.display = 'block';
        }
        // Added for  COEUSQA-3483 : Changes to Protocol Summary screen in Routing in Lite - start
           else if(value==24) {
           document.getElementById('qnrmainDiv').style.display = 'none';
           document.getElementById('QuestionShowImage').style.display = 'block';
           document.getElementById('QuestiontHideImage').style.display = 'none';
           document.getElementById('QuestionShow').style.display = 'block';
           document.getElementById('QuestionHide').style.display = 'none';
        }
        else if(value==23) {
            document.getElementById('qnrmainDiv').style.display = 'block';
          document.getElementById('QuestionShowImage').style.display = 'none';
           document.getElementById('QuestiontHideImage').style.display = 'block';
           document.getElementById('QuestionShow').style.display = 'none';
           document.getElementById('QuestionHide').style.display = 'block';
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

          function view_comments1(val) {
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

                var loc = '<bean:write name='ctxtPath'/>/coeuslite/mit/utils/dialogs/cwViewSpecialReview.jsp?value='+value +'&type=R';
                alert(loc);
                var newWin = window.open(loc,"formName",'dependent=1,toolbar=0,location=0,directories=0,status=0, menubar=0,scrollbars=0,resizable=0,width=' + w + ',height=' + h+ ',left='+ leftPos + ',top=' + topPos);
              alert(newWin);
              newWin.window.focus();

         }
   function view_data(activityType,docType,versionNumber, sequenceNumber,documentId, isParent){
            //Modified for Case#3036 - Didn't recognize document file type in Protocol attachments - End
                //window.open("<%=request.getContextPath()%>/viewUploadAction.do?&docType="+docType+"&acType=V&versionNumber="+versionNumber+"&SeqNumber="+sequenceNumber+"&Parent="+isParent);
                window.open("<%=request.getContextPath()%>/viewUploadAction.do?&docType="+docType+"&acType=V&versionNumber="+versionNumber+"&SeqNumber="+sequenceNumber+"&Parent="+isParent+"&protocDocId="+documentId);
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
        var actionFrom='PROTOCOL';
        var protocolSummary='PROTOCOL_SUMMARY';
        window.open("<%=request.getContextPath()%>/saveQuestionnaireData.do?&questionnaireId="+qnrid+"&operation="+operation+"&printQuestions="+printQuestion+"&printAllQuestions="+printAllQuestion+"&printQnrId="+qnrid+"&protocolSummary="+protocolSummary);
    
    }
    //Added for COEUSQA-3483 : Changes to Protocol Summary screen in Routing in Lite - end

    </script>
</html:html>
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
			<td valign="top" align="center"><jsp:include
					page="/coeuslite/mit/irb/common/cwGeneralInfoHeader.jsp"
					flush="true" /></td>
		</tr>
		<tr>
			<td height='3'></td>
		</tr>

		<%--   ............ List of Investigator/Keypersons start.......................--%>
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
											<bean:message bundle="protocol"
												key="investigatorsLabel.Investigators" />
										</div>
										<div id='protohideimage' style='display: none;'>
											<html:link href="javaScript:summaryHeader('4');">
												<html:img src="<%=minus%>" border="0" />
											</html:link>
											&nbsp;
											<bean:message bundle="protocol"
												key="investigatorsLabel.Investigators" />
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

								<table width="98%" border="0" cellspacing="1" cellpadding="3"
									class="tabtable" align="center">
									<tr>
										<td width='24%' class='theader' align="left"><bean:message
												key="label.invesKeypersons.personName" /></td>
										<td width='24%' class='theader' align="left"><bean:message
												key="label.invesKeypersons.department" /></td>
										<td width='9%' class='theader'><bean:message
												key="label.invesKeypersons.leadUnit" /></td>
										<td width='15%' class='theader' align="left"><bean:message
												key="label.invesKeypersons.role" /></td>
										<td width='10%' class='theader' align="left"><bean:message
												key="label.invesKeypersons.affliate" /></td>
										<td width='6%' class='theader'><bean:message
												key="label.invesKeypersons.training" /></td>
									</tr>
									<% String strBgColor = "#DCE5F1"; //BGCOLOR=EFEFEF  FBF7F7
                   int count = 0;
                   String EMPTY_STRING = "";
                %>
									<logic:present name="personsData">
										<logic:iterate id="data" name="personsData"
											type="org.apache.struts.validator.DynaValidatorForm"
											scope="session">
											<%
                    String timeStamp =(String) data.get("updateTimestamp");
                    String personId = (String) data.get("personId");
                    String piFlag =(String) data.get("principleInvestigatorFlag");
                    String role = (String) data.get("role");
                    System.out.println("role >>>" +role);
                    if (count%2 == 0)
                        strBgColor = "#D6DCE5";
                    else
                        strBgColor="#DCE5F1";
                %>
											<tr bgcolor="<%=strBgColor%>"
												onmouseover="className='TableItemOn'"
												onmouseout="className='TableItemOff'">
												<td class='copy'><bean:write name="data"
														property="personName" /></td>
												<td class='copy'><bean:write name="data"
														property="departmentName" /></td>
												<td class='copy' align=center><logic:equal name="data"
														property="principleInvestigatorFlag" value="Y">
														<img
															src="<bean:write name='ctxtPath'/>/coeusliteimages/checked.gif">
													</logic:equal></td>
												<td class='copy'>
													<%
                         if (piFlag==null || piFlag.equals(EMPTY_STRING)) {%>
													<bean:write name="data" property="personRole" /> <%} else {%>
													<logic:equal name="data"
														property="principleInvestigatorFlag" value="Y">
														<bean:message
															key="label.invesKeypersons.principalInvestigator" />
													</logic:equal> <logic:equal name="data"
														property="principleInvestigatorFlag" value="N">
														<bean:message key="label.invesKeypersons.coInvestigator" />
													</logic:equal> <%}%>
												</td>
												<td class='copy'><bean:write name="data"
														property="affiliationTypeDescription" /></td>
												<%  String completeImage= request.getContextPath()+"/coeusliteimages/complete.gif";
                        String noneImage= request.getContextPath()+"/coeusliteimages/none.gif"; %>



												<td class='copy' align='center' width='6%'><logic:notPresent
														name="trainingStatusFlag">
														<html:img src="<%=noneImage%>" />
													</logic:notPresent> <logic:present name="trainingStatusFlag">
														<html:img src="<%=completeImage%>" />
													</logic:present></td>
											</tr>
											<%count++;%>
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
			<td height='3'></td>
		</tr>

		<%--/////////////irb summary inv key person  starts//////////////////////////--%>
		<%--   ............ List of Research Areas start.......................--%>
		<logic:notEmpty name="areaData">
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
												<html:link href="javaScript:summaryHeader('11');">
													<html:img src="<%=plus%>" border="0" />
												</html:link>
												&nbsp;
												<bean:message bundle="protocol"
													key="areasOfResch.AreasOfResearch" />
											</div>
											<div id='areaHideImage' style='display: none;'>
												<html:link href="javaScript:summaryHeader('12');">
													<html:img src="<%=minus%>" border="0" />
												</html:link>
												&nbsp;
												<bean:message bundle="protocol"
													key="areasOfResch.AreasOfResearch" />
											</div>
										</td>
										<td width='50%' align=right><html:link
												href="javaScript:summaryHeader('11');">
												<div id='areaShow' style='display: block;'>
													<bean:message bundle="proposal" key="summary.show" />
												</div>
											</html:link> <html:link href="javaScript:summaryHeader('12');">
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
											<td align="left" class="theader" width="20%"><bean:message
													key="areasOfResch.Code" bundle="protocol" /></td>
											<td align="left" class="theader" width=""><bean:message
													key="areasOfResch.Description" bundle="protocol" /></td>
										</tr>
										<logic:present name="areaData">
											<logic:iterate id="researchAreaList" name="areaData"
												type="org.apache.struts.validator.DynaValidatorForm">

												<%
                                       if (count%2 == 0)
                                          strBgColor = "#D6DCE5";
                                       else
                                          strBgColor="#DCE5F1";
                                    %>
												<tr bgcolor="<%=strBgColor%>" width="20%" valign="top"
													onmouseover="className='TableItemOn'"
													onmouseout="className='TableItemOff'">
													<td align="left" nowrap class="copy"><%=researchAreaList.get("researchAreaCode")%>
													</td>
													<td width="45%" align="left" nowrap class="copy"><%=researchAreaList.get("researchAreaDescription")%></td>

												</tr>
												<% count++;%>
											</logic:iterate>
										</logic:present>
									</table>
								</div>
							</td>
						</tr>
						<tr>
							<td height='2'></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td height='3'></td>
		</logic:notEmpty>
		<%--   ............ List of Research Areas ends.......................--%>
		<%--   ............ List of Funding Sources start.......................--%>


		<logic:notEmpty name="fundingSources">


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
											<div id='protosfundingsourceshowimage'
												style='display: block;'>
												<html:link href="javaScript:summaryHeader('7');">
													<html:img src="<%=plus%>" border="0" />
												</html:link>
												&nbsp;
												<bean:message bundle="protocol"
													key="protocolReviewComments.label.fundingsource" />
											</div>
											<div id='protosfundingsourcehideimage' style='display: none;'>
												<html:link href="javaScript:summaryHeader('8');">
													<html:img src="<%=minus%>" border="0" />
												</html:link>
												&nbsp;
												<bean:message bundle="protocol"
													key="protocolReviewComments.label.fundingsource" />
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
								<div id='fundingSourceDetails' style='display: none;'>

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
										<logic:present name="fundingSources">
											<logic:iterate id="fundingSourceBean" name="fundingSources"
												type="org.apache.struts.validator.DynaValidatorForm">

												<%
                                                               if (count%2 == 0)
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

													<% count++;%>
												
											</logic:iterate>
										</logic:present>
										<%//}%>
									</table>
								</div>
							</td>
						</tr>
						<tr>
							<td height='2'></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td height="3"></td>
			</tr>
		</logic:notEmpty>
		<%--   ............ List of Funding Sources ends.......................--%>
		<%--   ............ List of Subjects start.......................--%>
		<logic:notEmpty name="VulnerableData">
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
											<div id='protossubjectsshowimage' style='display: block;'>
												<html:link href="javaScript:summaryHeader('9');">
													<html:img src="<%=plus%>" border="0" />
												</html:link>
												&nbsp;
												<bean:message bundle="protocol"
													key="vulnSubLabel.VulnerableSubjects" />
											</div>
											<div id='protossubjectshideimage' style='display: none;'>
												<html:link href="javaScript:summaryHeader('10');">
													<html:img src="<%=minus%>" border="0" />
												</html:link>
												&nbsp;
												<bean:message bundle="protocol"
													key="vulnSubLabel.VulnerableSubjects" />
											</div>
										</td>
										<td width='50%' align=right><html:link
												href="javaScript:summaryHeader('9');">
												<div id='subjectShow' style='display: block;'>
													<bean:message bundle="proposal" key="summary.show" />
												</div>
											</html:link> <html:link href="javaScript:summaryHeader('10');">
												<div id='subjectHide' style='display: none;'>
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
								<div id='subjectDetails' style='display: none;'>
									<table width="98%" border="0" cellspacing="1" cellpadding="3"
										class="tabtable" align="center">
										<tr align="center" valign="top">
											<td width="50%" align="left" class="theader"><bean:message
													key="vulnSubLabel.VulnerableSubject" /></td>
											<td width="50%" align="left" class="theader"><bean:message
													key="vulnSubLabel.Count" /></td>

										</tr>
										<logic:present name="VulnerableData">
											<logic:iterate id="VulnerableBean" name="VulnerableData"
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
													<td align="left" nowrap class="copy"><%=VulnerableBean.get("vulnerableSubjectTypeDesc")%>
													</td>
													<td width="30%" align="left" nowrap class="copy"><bean:write
															name="VulnerableBean" property="subjectCount" /></td>

												</tr>

											</logic:iterate>
											<% count++;%>
										</logic:present>
										<%//}%>
									</table>
								</div>
							</td>
						</tr>
						<tr>
							<td height='2'></td>
						</tr>
					</table>

				</td>
			</tr>
			<tr>
				<td height="3"></td>
			</tr>
		</logic:notEmpty>
		<%--   ............ List of  Subjects ends.......................--%>

		<%--   ............ List of  Special Review starts.......................--%>
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
											<div id='protosspecialreviewshowimage'
												style='display: block;'>
												<html:link href="javaScript:summaryHeader('13');">
													<html:img src="<%=plus%>" border="0" />
												</html:link>
												&nbsp;
												<bean:message bundle="protocol"
													key="helpPageTextProtocol.ProtocolSpreview" />
											</div>
											<div id='protosspecialreviewhideimage' style='display: none;'>
												<html:link href="javaScript:summaryHeader('14');">
													<html:img src="<%=minus%>" border="0" />
												</html:link>
												&nbsp;
												<bean:message bundle="protocol"
													key="helpPageTextProtocol.ProtocolSpreview" />
											</div>
										</td>
										<td width='50%' align=right><html:link
												href="javaScript:summaryHeader('13');">
												<div id='specialShow' style='display: block;'>
													<bean:message bundle="proposal" key="summary.show" />
												</div>
											</html:link> <html:link href="javaScript:summaryHeader('14');">
												<div id='specialHide' style='display: none;'>
													<bean:message bundle="proposal" key="summary.hide" />
												</div>
											</html:link></td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td></td>
						</tr>
						<tr>
							<td height='2'></td>
						</tr>
						<tr>
							<td>
								<div id='specialreviewDetails' style='display: none;'>
									<table width="98%" border="0" cellspacing="1" cellpadding="3"
										class="tabtable" align="center">
										<tr align="center" valign="top">
											<td width="25%" align="left" class="theader"><bean:message
													key="fundingSrc.Type" /></td>

											<td width="15%" align="left" class="theader"><bean:message
													key="specialReviewLabel.Approval" /></td>
											<td width="15%" align="left" class="theader"><bean:message
													key="specialReviewLabel.ProtocolNo" /></td>
											<td width="15%" align="left" class="theader"><bean:message
													key="specialReviewLabel.ApplDate" /></td>
											<td width="15%" align="left" class="theader"><bean:message
													key="specialReviewLabel.ApprDate" /></td>
											<td width='25%' align="center" class='theader'><bean:message
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
						<tr>
							<td height='2'></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td height="3"></td>
			</tr>
		</logic:notEmpty>
		<%--   ............ List of Special Review ends.......................--%>
		<%--   ............ List of Attachments start.......................--%>
		<logic:notEmpty name="uploadLatestData">

			<tr valign=top>
				<td>
					<%--  <tr><td height="3"></td></tr>--%>
					<table width="98%" align=center border="0" cellpadding="0"
						cellspacing="0" class="tabtable">
						<tr valign=top>
							<td>
								<table width="100%" border="0" cellpadding="0" cellspacing="0"
									class="tableheader">
									<tr valign=top>
										<td width='50%' align=left nowrap>
											<div id='attachmentshowimage' style='display: block;'>
												<html:link href="javaScript:summaryHeader('19');">
													<html:img src="<%=plus%>" border="0" />
												</html:link>
												&nbsp;
												<bean:message bundle="protocol"
													key="otherAttachLabel.Attachment" />
											</div>
											<div id='attachmenthideimage' style='display: none;'>
												<html:link href="javaScript:summaryHeader('20');">
													<html:img src="<%=minus%>" border="0" />
												</html:link>
												&nbsp;
												<bean:message bundle="protocol"
													key="otherAttachLabel.Attachment" />
											</div>
										</td>
										<td width='50%' align=right><html:link
												href="javaScript:summaryHeader('19');">
												<div id='attachShow' style='display: none;'>
													<bean:message bundle="proposal" key="summary.show" />
												</div>
											</html:link> <html:link href="javaScript:summaryHeader('20');">
												<div id='attachHide' style='display: block;'>
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
								<div id='atachmentDetails' style='display: none;'>
									<table width="98%" border="0" cellspacing="1" cellpadding="3"
										class="tabtable" align="center">
										<tr align="center" valign="top">
											<td width="25%" align="left" class="theader"><bean:message
													key="uploadDocLabel.Type" /></td>
											<td width="60%" align="left" class="theader"
												style="padding-left: 8px;"><bean:message
													key="uploadDocLabel.Description" /></td>
											<td width='15%' align="center" class="theader"></td>
										</tr>
										<logic:present name="uploadLatestData">
											<logic:iterate id="uploadDocList" name="uploadLatestData"
												type="edu.mit.coeus.irb.bean.UploadDocumentBean">
												<%
                            if (count%2 == 0)
                                strBgColor = "#D6DCE5";
                            else
                                strBgColor="#DCE5F1";
                            int statusCode=uploadDocList.getStatusCode();
                            boolean showRemove=true;
                            if(statusCode == 3){
                                    showRemove=false;
                            }
                        %>

												<tr align="left" class="copy" bgcolor="<%=strBgColor%>"
													onmouseover="className='TableItemOn'"
													onmouseout="className='TableItemOff'">
													<td class="copy"><%=uploadDocList.getDocType()%></td>
													<td class="copy" align="justify"
														style="padding-left: 8px; padding-right: 8px;"><%=uploadDocList.getDescription()%>
														<%-- <html:link  href="<%=viewDesc%>">
                                   <bean:message key="uploadDocLabel.ViewDescription"/>
                                </html:link> --%></td>

													<td class="copy" align="center">
														<%
                                 String viewLink1 = "javascript:view_data('V','" +uploadDocList.getDocCode() +"',"+"'"+uploadDocList.getVersionNumber()+"','"+uploadDocList.getSequenceNumber()+"','"+uploadDocList.getDocumentId()+"','N')";
                                    %> &nbsp;&nbsp; <html:link
															href="<%=viewLink1%>">
															<bean:message key="label.View" />
														</html:link>

													</td>

													<%--              <td class="copy">
                                   <% viewLink = "javascript:view_data(" +uploadDocList.get +uploadDocList.DocCode() +"',"+"'"+uploadDocList.getVersionNumber()+"','"+uploadDocList.getSequenceNumber()+"','"+uploadDocList.getDocumentId()+"')";%>
                                        <html:link href="<%=viewLink%>">
                                           <bean:message key="label.View"/>
                                      </html:link></td>--%>

												</tr>
												<% count++;%>
											</logic:iterate>
										</logic:present>
									</table>
								</div>
							</td>
						</tr>
						<tr>
							<td height='2'></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td height="3"></td>
			</tr>
		</logic:notEmpty>

		<%--   ............ List of Attachments ends.......................--%>
		<%--Added for COEUSQA-3483 : Changes to Protocol Summary screen in Routing in Lite - start ---%>
		<logic:present name="questionAnsMapForIRB">
			<tr valign=top>
				<td>
					<table width="98%" align=center border="0" cellpadding="0"
						cellspacing="0" class="tabtable">
						<tr>
							<td valign="top">
								<table width="100%" border="0" cellpadding="0" cellspacing="0"
									class="tableheader">
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
                              Map questMap = (HashMap) request.getAttribute("questionAnsMapForIRB");
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

									<table width="98%" border="0" cellpadding="0" cellspacing="0"
										class="tabtable" align="center">
										<tr>
											<td colspan="4" valign="top">
												<div id='budgetReport'>
													<table width="100%" align=center border="0" cellpadding="0"
														cellspacing="0" class='theader'>
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
																	<div id='QnrShow<%=divIndex%>' style='display: block;'>
																		<bean:message bundle="proposal" key="summary.show" />
																	</div>
															</a> <a
																href="javaScript:dynamicQuestionsDivHide('Inner<%= divIndex%>','QnrShowImage<%= divIndex%>','QnrShow<%= divIndex%>','QnrHide<%= divIndex%>','QnrHideImage<%= divIndex%>');">
																	<div id='QnrHide<%=divIndex%>' style='display: none;'>
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
															<td class='copybold' align=left width="15%" valign="top">
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
                                                              if (!questBean.getAnswer().equalsIgnoreCase("Y")) {%>
																<input type="radio" id="question" value="Y"
																disabled="true" />Yes &nbsp; <input type="radio"
																id="question" value="N" checked="true" disabled="true" />No
																<% }
                                                          }
                                                          if (questBean.getValidAnswers().equalsIgnoreCase("YNX")) {
                                                              if (questBean.getAnswer().equalsIgnoreCase("Y")) {
                                                          %> <input
																type="radio" id="question" value="Y" checked="true"
																disabled="true" />Yes &nbsp; <input type="radio"
																id="question" value="N" disabled="true" />Yes &nbsp; <input
																type="radio" id="question" value="X" disabled="true" />N/A
																&nbsp; <% }
                                                              if (questBean.getAnswer().equalsIgnoreCase("N")) {%>

																<input type="radio" id="question" value="Y"
																disabled="true" />Yes &nbsp; <input type="radio"
																id="question" value="N" checked="true" disabled="true" />No
																&nbsp; <input type="radio" id="question" value="X"
																disabled="true" />N/A &nbsp; <% }
                                                              if (questBean.getAnswer().equalsIgnoreCase("X")) {%>
																<input type="radio" id="question" value="Y"
																disabled="true" />Yes &nbsp; <input type="radio"
																id="question" value="N" disabled="true" />Yes &nbsp; <input
																type="radio" id="question" value="X" checked="true"
																disabled="true" />N/A <% }
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

		<%--Added for COEUSQA-3483 : Changes to Protocol Summary screen in Routing in Lite - end--%>


	</table>

</body>

</html>

