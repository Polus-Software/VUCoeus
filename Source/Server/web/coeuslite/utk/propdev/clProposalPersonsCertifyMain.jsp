<%--
    Document   : clProposalPersonsCertifyMain
    Created on : Dec 16, 2010, 9:20:42 AM
    Author     : athul
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">


<%@ page import="org.apache.struts.taglib.html.JavascriptValidatorTag,edu.mit.coeuslite.utils.CoeusLiteConstants,
        edu.mit.coeuslite.utils.DateUtils"%>
<%--print starts--%>
<%@ page import="java.util.*,edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean,edu.utk.coeuslite.propdev.bean.QuestionAnswerProposalSummaryBean, edu.wmc.coeuslite.utils.DynaBeanList, edu.mit.coeus.sponsormaint.bean.SponsorFormsBean"%>
<%--print ends--%>
<%@ taglib uri="/WEB-INF/struts-bean.tld"  prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld"  prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"  prefix="coeusUtils"%>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<jsp:useBean id="budgetPeriods" scope="request" class="java.util.Vector"/>
<jsp:useBean id="budgetReports" scope="request" class="java.util.Vector"/>
<%--<jsp:useBean id="BudgetForProposalDet" scope="request" class="java.util.Vector"/>--%>
<jsp:useBean id="ppcBudgetForProposalDet" scope="session" class="java.util.Vector"/>
<jsp:useBean  id="NarrativeInfo" scope="request" class="java.util.Vector"/>

<jsp:useBean id="splReview" scope="session" class="java.util.Vector" />
<jsp:useBean id="approval" scope="session" class="java.util.Vector" />
<jsp:useBean id="pdReviewList" scope="session" class="java.util.Vector" />

<%-- InvKey--%>

<%@ page import="org.apache.struts.taglib.html.JavascriptValidatorTag,
                org.apache.struts.validator.DynaValidatorForm"%>
<%@ page import="java.util.Vector,
                java.util.ArrayList,
                edu.mit.coeus.bean.*,
                edu.mit.coeus.unit.bean.*,
                edu.mit.coeus.rolodexmaint.bean.*,
                edu.mit.coeus.utils.ComboBoxBean,
                edu.mit.coeus.propdev.bean.ProposalLeadUnitFormBean,
                java.net.URLEncoder" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%-- page body header information --%>
<jsp:useBean id="proposalNumber" scope="request" class="java.lang.String" />
<jsp:useBean id="user" scope="session" class="edu.mit.coeus.bean.UserInfoBean" />
<jsp:useBean id="loggedinUser" scope="session" class="java.lang.String"/>
<jsp:useBean id="genInfoUpdTimestamp" scope="session" class="java.lang.String" />
<jsp:useBean id="createTimestamp" scope="session" class="java.lang.String" />
<jsp:useBean id="createUser" scope="session" class="java.lang.String" />
<jsp:useBean id="actionType" scope="request" class="java.lang.String" />
<jsp:useBean id="unitNumber" scope="session" class="java.lang.String" />
<jsp:useBean id="unitName" scope="session" class="java.lang.String" />
<jsp:useBean id="creationStatDesc" scope="session" class="java.lang.String" />
<jsp:useBean id="propInvPersonEditableColumns" scope="session" class="java.util.HashMap" />
<bean:size id="colSize" name ="propInvPersonEditableColumns" />
<%-- end of header information --%>

<jsp:useBean  id="investigatorRoles" scope="session" class="java.util.Vector"/>
<jsp:useBean  id="proposalInvKeyData" scope="session" class="java.util.Vector"/>
<%--<jsp:useBean  id="propInvBean" scope="session" class="java.util.Vector"/>--%>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>

<%-- InvKey--%>
 <%   boolean blnReadOnly = false;
      String mode=(String)session.getAttribute("mode"+session.getId());

      boolean modeValue=false;
      if(mode!=null && !mode.equals("")) {
         if(mode.equalsIgnoreCase("display")){
            modeValue=true;
            blnReadOnly=true;
         }
      }
      DynaValidatorForm formdata = (DynaValidatorForm)request.getAttribute("investigatorForm");
      String certifyRight = (String) session.getAttribute("CERTIFY_RIGHTS_EXIST");
      //Added for COEUSQA-2037 : Software allows you to delete an investigator who is assigned credit in the credit split window
      int investigatorCount = 0;
      Vector invKeyData = (Vector)session.getAttribute("proposalInvKeyData");
      if(invKeyData != null && invKeyData.size()>0){
          for(int index=0;index<invKeyData.size();index++){
             DynaValidatorForm propInv =  (DynaValidatorForm)invKeyData.get(index);
             String pInvFlag = (String)propInv.get("principalInvestigatorFlag");
             if(pInvFlag!=null && (pInvFlag.equals("Y") || pInvFlag.equals("N"))){
                 investigatorCount++;
             }

          }
      }
      //COEUSQA-2037 : End
  %>
<% DateUtils dateUtils = new edu.mit.coeuslite.utils.DateUtils();
   String enableProtocolToDevPropLink = (String)session.getAttribute("enableProtocolToDevPropLink");
%>



<html:html locale="true">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Certify Main</title>
    </head>
   <script language="javascript" type="text/JavaScript" src="<%=request.getContextPath()%>/coeuslite/mit/utils/scripts/divSlide.js"></script>
    <script language="javascript" type="text/JavaScript" src="..\scripts\_js.js"></script>
    <script language="javascript" type="text/JavaScript" src="..\scripts\CheckForward.js"></script>
<script language="JavaScript" type="text/JavaScript">
    //Div Hiding script starts......Athul
    function summaryHeader(value) {
        if(value==1) {

            document.getElementById('proposalHide').style.display = 'block';
            document.getElementById('proposalHideImage').style.display = 'block';
            document.getElementById('proposalDetailsShow').style.display = 'block';
            document.getElementById('proposalShow').style.display = 'none';
            document.getElementById('proposalShowImage').style.display = 'none';

        } else if(value==2) {
            document.getElementById('proposalHideImage').style.display = 'none';
            document.getElementById('proposalHide').style.display = 'none';
            document.getElementById('proposalDetailsShow').style.display = 'none';
            document.getElementById('proposalShowImage').style.display = 'block';
            document.getElementById('proposalShow').style.display = 'block';
        }
    }
// Div Hiding script ends......Athul

    function documentProgress(data,proposalNumber,moduleNumber,fileType){
        if(data=='V'){
            var fileType = 2;
            var link = "StreamingServlet?proposalNumber="
                        +proposalNumber
                        +"&moduleNumber="+moduleNumber
                        +"&reader=edu.utk.coeuslite.propdev.bean.NarrativeDocumentReader"
                        +"&fileType="+fileType;
            var winleft = (screen.width - 650) / 2;
            var winUp = (screen.height - 450) / 2;
            var win = "scrollbars=1,resizable=1,width=790,height=500,left="+winleft+",top="+winUp
            //alert(link);
            sList = window.open('<bean:write name='ctxtPath'/>'+"/"+link, "list", win);
       }
    }
//Investigator Script S T A R T S

function certifyInvestigator(personId, proposalNum, personName) {

          document.pdInvestKeyPersForm.action = "<%=request.getContextPath()%>/getInvKeyPersonsCertify.do?personId="+personId+"&proposalNumber="+proposalNum+"&personName="+personName+"&index=summary";
          document.pdInvestKeyPersForm.submit();

           }

           function navigate()
           {
               document.proposalPrint.action="<%=request.getContextPath()%>/ppcCertification.do";
               document.proposalPrint.submit();
           }
  function logout()
           {
               document.proposalPrint.action="<%=request.getContextPath()%>/logoutAction.do";
               document.proposalPrint.submit();
           }
</script>
<script language="javascript" type="text/JavaScript" src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/coeusLiteCalendar.js"></script>
<style type="text/css">

         .tab {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 12px;
	font-style: normal;
	line-height: normal;
	font-weight: normal;
	font-variant: normal;
	text-transform: none;
	color: #173B63;
	text-decoration: none;
	background-color: #D1E5FF;
	background-repeat: no-repeat;
        border-collapse:collapse;clear:both;
}
</style>

<script language="javascript" type="text/JavaScript" src="_js.js"></script>
<script language="JavaScript" type="text/JavaScript"></script>



<link rel=stylesheet href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/ipf.css" type="text/css">
<link href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css" rel="stylesheet" type="text/css">

<html:base/>
</head>
<body>
<%String plus = request.getContextPath()+"/coeusliteimages/plus.gif";
  String minus = request.getContextPath()+"/coeusliteimages/minus.gif";%>

  <html:form action="/proposalPersonsCertify">


      <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="table">
      <%--<table width='72%' height="100%" cellspacing="0" cellpadding="0" border="0" align="center" class="table" > class="tabtable"--%>
  <tr valign=top>
    <td>
         <logic:present name="invalidPerson">
             <div style="font-size:small;color:navy;"> This certification link was not for you. </div>
      </logic:present>
             <logic:notPresent name="invalidPerson">
        <table width="98%" align=center  border="0" cellpadding="0" cellspacing="0" class="tabtable">
        <tr valign=top>
            <td>
                <table width="100%"  border="0" cellpadding="0" cellspacing="0" class="tableheader">
                <tr valign=top>
                    <td width='50%' align=left nowrap>
                        <div id='proposalShowImage' style='display: none;'>
                            <html:link href="javaScript:summaryHeader('1');">
                            <html:img src="<%=plus%>" border="0"/></html:link>&nbsp;
                            <bean:message bundle="proposal" key="summary.proposalSummary"/>
                        </div>
                        <div id='proposalHideImage'>
                            <html:link href="javaScript:summaryHeader('2');">
                            <html:img src="<%=minus%>" border="0"/></html:link>&nbsp;
                            <bean:message bundle="proposal" key="summary.proposalSummary"/>
                        </div>
                    </td>
                    <td width='50%' align=right>
                        <html:link href="javaScript:summaryHeader('1');">
                        <div id='proposalShow' style='display: none;'>
                            <bean:message bundle="proposal" key="summary.show"/>
                        </div>
                        </html:link>
                        <html:link href="javaScript:summaryHeader('2');">
                        <div id='proposalHide'>
                            <bean:message bundle="proposal" key="summary.hide"/>
                        </div>
                        </html:link>
                    </td>
                </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td height='10'>
            </td>
        </tr>
        <tr>
            <td>
                <div id='proposalDetailsShow'>
                <table width="100%"  border="0" cellpadding="0" cellspacing="0">
                <tr>
                    <td>
                        <jsp:include  page="/coeuslite/utk/propdev/common/clProposalGeneralHeader.jsp" flush="true"/>
                    </td>
                </tr>
                <!----------------New Change Start For Budget total ---------------->
                 <tr>
            <td>
                <table width="100%" align=center border="0" cellpadding="0" cellspacing="0" class="table">
                <tr valign=top>
                    <td>
                        <div id='budgetTotal'>
                        <table width="100%" align=center border="0" cellpadding="0" cellspacing="0">
                        <tr>


                            <td align="left" width='20%'  nowrap class='copybold' style="color:white;">&nbsp;&nbsp;&nbsp;<bean:message bundle="proposal" key="summary.budgetTotal"/></td>

                            <td width='50%' align=right>

                            </td>
                        </tr>
                        </table>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div id='budgetTotalDetails'>
                        <table width="100%" align="center"  border="0" cellpadding="0" cellspacing="0" class="table">
                        <logic:iterate id="budgetData" name="ppcBudgetForProposalDet" type="org.apache.commons.beanutils.DynaBean">
                        <tr>

                             <td align="left" width='20%'  nowrap class='copybold'>&nbsp;&nbsp;&nbsp;<bean:message bundle="budget" key="budgetLabel.directCost" /></td>

                            <td width='22%' align="left" nowrap>
                                    <logic:present name="budgetData" property="totalDirectCost">
                                        <coeusUtils:formatString name="budgetData" property="totalDirectCost" formatType="currencyFormat"/>
                                    </logic:present>
                            </td>

                             <td align="left" width='20%'  nowrap class='copybold'>&nbsp;&nbsp;&nbsp;<bean:message bundle="budget" key="budgetLabel.inDirectCost" /></td>

                            <td width='22%' align="left" nowrap>
                                    <logic:present name="budgetData" property="totalIndirectCost">
                                        <coeusUtils:formatString name="budgetData" property="totalIndirectCost" formatType="currencyFormat"/>
                                    </logic:present>
                            </td>

                             <td align="left" width='20%'  nowrap class='copybold'>&nbsp;&nbsp;&nbsp;<bean:message bundle="budget" key="budgetLabel.totalCost" /></td>

                            <td width='22%' align="left" nowrap>
                                    <logic:present name="budgetData" property="totalCost">
                                        <coeusUtils:formatString name="budgetData" property="totalCost" formatType="currencyFormat"/>
                                    </logic:present>
                            </td>
                        </tr>
                        <tr>

                             <td align="left" width='20%'  nowrap class='copybold'>&nbsp;&nbsp;&nbsp;<bean:message bundle="budget" key="budgetLabel.underRecovery" /></td>

                            <td width='22%' align="left" nowrap>
                                    <logic:present name="budgetData" property="underrecoveryAmount">
                                        <coeusUtils:formatString name="budgetData" property="underrecoveryAmount" formatType="currencyFormat"/>
                                    </logic:present>
                            </td>

                             <td align="left" width='20%'  nowrap class='copybold'>&nbsp;&nbsp;&nbsp;<bean:message bundle="budget" key="budgetLabel.costShare" /></td>

                            <td width='22%' align="left" nowrap>
                                    <logic:present name="budgetData" property="costSharingAmount">
                                        <coeusUtils:formatString name="budgetData" property="costSharingAmount" formatType="currencyFormat"/>
                                    </logic:present>
                            </td>

                             <td align="left" width='20%'  nowrap class='copybold'>&nbsp;&nbsp;&nbsp;<bean:message bundle="budget" key="budgetSummary.period" /></td>

                            <td width='22%' align="left" nowrap>
                                    <logic:present name="budgetData" property="reqStartDateInitial">
                                        <coeusUtils:formatDate name="budgetData" property="reqStartDateInitial"/>
                                    </logic:present>
                                    <logic:present name="budgetData" property="reqEndDateInitial">
                                        <coeusUtils:formatDate name="budgetData" property="reqEndDateInitial"/>
                                    </logic:present>
                            </td>
                        </tr>
                        </logic:iterate>
                        </table>
                        </div>
                    </td>
                </tr>

               </table>
            </td>
        </tr>
                <!----------------New Change Ends For Budget total ---------------->


                </table>
                </div>
            </td>
        </tr>
<tr>

    <td>
        <div id='PersonDetails'>

            <table width="100%" align=center border="0" cellpadding="0" cellspacing="0" class="table">
                <tr valign=top>
                    <td>
            <table width="100%" align=center border="0" cellpadding="0" cellspacing="0" class="table">
            <tr>
                <td align="left" width='20%'  nowrap class='copybold' style="color:white;">&nbsp;&nbsp;&nbsp;Person Role And Details<%--<bean:message bundle="proposal" key="summary.budgetTotal"/>--%>
                </td>
                <td width='50%' align=right>
                </td>
            </tr>
            </table>

            <tr>
                <td>
                <table width="100%" align=center border="0" cellpadding="0" cellspacing="0" class="table">

            <%String Role = (String)session.getAttribute("ProposalRoles"); %>
                <tr valign=top>

                    <td nowrap class="copybold">&nbsp;&nbsp;&nbsp;
                       You are Listed As <%=Role%>
                    </td>
                    </tr>
                </table>

                    </td></tr></table>
        </div>

    </td>
</tr>


<logic:notPresent name="Is_From_ppc_main">
<tr>
    <td>
    <div id='PersonDetails'>
           <table width="100%" align=center border="0" cellpadding="0" cellspacing="0" class="table">
                <tr valign=top>
                    <td>
            <table width="100%" align=center border="0" cellpadding="0" cellspacing="0" class="table">
            <tr>


                <td align="left" width='20%'  nowrap class='copybold' style="color:white;">&nbsp;&nbsp;&nbsp;Navigate To<%--<bean:message bundle="proposal" key="summary.budgetTotal"/>--%>
                </td>

                <td width='50%' align=right>

                </td>
            </tr>
            </table>

                    </td>
                </tr>


            <tr><td>
            <table width="100%" align=center border="0" cellpadding="0" cellspacing="0" class="table">
                <tr valign=top>
                    <td width='22%' align="left" nowrap class='copybold'>&nbsp;&nbsp;&nbsp;

                            <a href="javascript:navigate()" style="text-decoration: underline;">
                            Certifications</a>
                </td>
            </tr>
            <tr>
                <td>
&nbsp;
                </td>
            </tr>
            <tr>
                <td>&nbsp;&nbsp;&nbsp;

                           Complete COI

                </td>
            </tr>
             <tr>
                <td>
&nbsp;
                </td>
            </tr>

            </table>
                    </td></tr></table>
    </div>
    </td>
</tr>
</logic:notPresent>


        </table>
                    </logic:notPresent>
    </td>
</tr>

<tr><td>&nbsp;</td></tr>


</table>

       </html:form>
    </body>
</html:html>

