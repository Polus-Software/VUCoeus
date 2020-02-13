<%--
    Document   : clRightPersonSubHeader
    Created on : Jan 3, 2011, 12:07:37 PM
    Author     : anishk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">


<%@ page import="org.apache.struts.taglib.html.JavascriptValidatorTag,edu.mit.coeuslite.utils.CoeusLiteConstants,
        edu.mit.coeuslite.utils.DateUtils"%>
<%--print starts--%>
<%@ page import="java.util.*,edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean,edu.utk.coeuslite.propdev.bean.QuestionAnswerProposalSummaryBean,
         edu.wmc.coeuslite.utils.DynaBeanList,
         edu.mit.coeus.sponsormaint.bean.SponsorFormsBean,edu.utk.coeuslite.propdev.bean.EPSProposalHeaderBean"%>
<%--print ends--%>
<%@ taglib uri="/WEB-INF/struts-bean.tld"  prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld"  prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"  prefix="coeusUtils"%>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<jsp:useBean id="budgetPeriods" scope="request" class="java.util.Vector"/>
<jsp:useBean id="budgetReports" scope="request" class="java.util.Vector"/>
<%--<jsp:useBean id="BudgetForProposalDet" scope="request" class="java.util.Vector"/>--%>
<jsp:useBean  id="NarrativeInfo" scope="request" class="java.util.Vector"/>

<jsp:useBean id="ppcBudgetForProposalDet" scope="session" class="java.util.Vector"/>

<jsp:useBean id="splReview" scope="session" class="java.util.Vector" />
<jsp:useBean id="approval" scope="session" class="java.util.Vector" />
<jsp:useBean id="pdReviewList" scope="session" class="java.util.Vector" />


<jsp:useBean id="ProposalRoles" scope="session" class="java.lang.String" />
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
<jsp:useBean  id="epsProposalHeaderBean"  scope="session" class="edu.utk.coeuslite.propdev.bean.EPSProposalHeaderBean"/>
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
   EPSProposalHeaderBean proposalHeaderBean=(EPSProposalHeaderBean)session.getAttribute("epsProposalHeaderBean");
%>



<html:html locale="true">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Certify Main</title>
       <%-- <script>
             function close_action(){
            document.personDynaBeansList.action = "<%=request.getContextPath()%>/getInvKeyPersons.do";
            document.personDynaBeansList.submit();
        }
        </script>--%>
    </head>
   <script language="javascript" type="text/JavaScript" src="<%=request.getContextPath()%>/coeuslite/mit/utils/scripts/divSlide.js"></script>
    <script language="javascript" type="text/JavaScript" src="..\scripts\_js.js"></script>
    <script language="javascript" type="text/JavaScript" src="..\scripts\CheckForward.js"></script>
<script language="JavaScript" type="text/JavaScript">



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
<link rel=stylesheet href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/ipf.css" type="text/css">
<link href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css" rel="stylesheet" type="text/css">

<html:base/>
</head>
<body>
<%String plus = request.getContextPath()+"/coeusliteimages/plus.gif";
  String minus = request.getContextPath()+"/coeusliteimages/minus.gif";%>

  <%String role = (String)session.getAttribute("ProposalRoles");%>

<!-- checking is not same user from premium -->
 <logic:present name="InvalidPersonFromPremium">
<logic:equal name="InvalidPersonFromPremium" value="true">
             <div style="color:navy;"> This certification link was not for you. </div>

 </logic:equal>
  </logic:present>

    <logic:notPresent name="InvalidPersonFromPremium">
    <logic:notEqual name="InvalidPersonFromPremium" value="true">
        <a name="top"> </a>

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table">
<tr>
    <td height='1'>
    </td>
</tr>
<logic:present name="frmSummary">
    <tr>
<td width='40%' class='copybold' align="right" style="padding-right: 15px">
      
                <a href="<%=request.getContextPath()%>/displayProposal.do?proposalNo=<%=proposalHeaderBean.getProposalNumber()%>"><u>Return to Proposal Summary</u></a>
                </td>
</tr>
</logic:present>

<logic:notPresent name="frmSummary">
<logic:notPresent name="disable_BackBtn">
    <logic:notPresent name="fromMailPPC">
<tr>
<td width='40%' class='copybold' align="right" style="padding-right: 15px">

     <a href="<%=request.getContextPath()%>/getInvKeyPersons.do"><u> Return to Investigator</u></a>
</td>
</tr>
    </logic:notPresent>
</logic:notPresent>
<logic:present name="disable_BackBtn">

<tr>
    <td width='20%' class='copybold' align="right" style="padding-right: 15px">

     <a href="<%=request.getContextPath()%>/getGeneralInfo.do?proposalNumber=<%=proposalHeaderBean.getProposalNumber()%>"><u> Return to Proposal</u></a>
    </td>

</tr>

</logic:present>
</logic:notPresent>
<tr valign=top>
    <td>
        <table width="100%" align=center  border="0" cellpadding="0" cellspacing="0" class="tabtable">
        <tr valign=top>
            <td>
                <table width="100%"  border="0" cellpadding="0" cellspacing="0" class="tableheader">
                <tr valign=top>
                    <td width='50%' align=left nowrap>
                        <div id='proposalShowImage'>

                            <bean:message bundle="proposal" key="summary.proposalSummary"/>
                        </div>
                    </td>

                </tr>
                </table>
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
                <logic:present name="ppcBudgetForProposalDet">
                    <logic:notEmpty name="ppcBudgetForProposalDet">


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
                            <table width="100%" align="center"  border="0" cellpadding="0" cellspacing="0" class="table" style="border: 0">
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

                             <td width='22%' align="left" nowrap style="padding:3px;">
                                    <logic:present name="budgetData" property="reqStartDateInitial">
                                        <coeusUtils:formatDate name="budgetData" property="reqStartDateInitial"/>
                                    </logic:present>
                                    <logic:present name="budgetData" property="reqEndDateInitial">
                                       - <coeusUtils:formatDate name="budgetData" property="reqEndDateInitial"/>
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
                </logic:notEmpty>
                </logic:present>
                <!----------------New Change Ends For Budget total ---------------->


                </table>
                </div>
            </td>
        </tr>
<!-- from summary showing person anme -->
<logic:present name="frmSummary">
    <% String personNameFrmSummary=(String)session.getAttribute("prop_persName");%>
<tr>

    <td>
        <div id='PersonDetails'>

            <table width="100%" align=center border="0" cellpadding="0" cellspacing="0" class="table" >             
            
                <tr>
                    <td width="10%" align="left"  nowrap class='copybold'>&nbsp;&nbsp;&nbsp;Person Name:
                    </td>
                    <td width="40%" nowrap class='copy'>&nbsp;&nbsp;
                         <%=personNameFrmSummary%>
                    </td>
                              
                   <% if(role!=null && role.trim().length()!=0){ %>
                    <td width="10%"align=left nowrap class='copybold'>&nbsp;&nbsp;&nbsp;Person Role:
                    </td>               
                    <td width="40%" nowrap class='copy' >&nbsp;&nbsp;
                                   <%=role%>
                    </td>
               <%}%>
                </tr>
           </table>
        </div>

    </td>
</tr>
</logic:present>

<logic:notPresent name="frmSummary">
    <logic:present name="hidePersons">
    <% String personNameFrmSummary=(String)session.getAttribute("prop_persName");%>
    <tr>

    <td>
        <div id='PersonDetails'>

            <table width="100%" align=center border="0" cellpadding="0" cellspacing="0" class="table" >               
              <tr>
                    <td width="10%" align="left"  nowrap class='copybold'>&nbsp;&nbsp;&nbsp;Person Name:
                    </td>
                    <td width="40%" nowrap class='copy'>&nbsp;&nbsp;
                        <%=personNameFrmSummary%>
                    </td>
                              
                   <% if(role!=null && role.trim().length()!=0){ %>
                    <td width="10%"align=left nowrap class='copybold'>&nbsp;&nbsp;&nbsp;Person Role:
                    </td>               
                    <td width="40%" nowrap class='copy'>&nbsp;&nbsp;<%=role%>
                    </td>
               <%}%>
                </tr>
                </table>
        </div>
    </td>
    </tr>
    </logic:present>
</logic:notPresent>
        </table>
    </td>
</tr>

<%--<logic:notPresent name="QuestMenu">

              <tr><td  height="5"></td></tr>
             <tr>
                 <td>
                     <div style="color:black;margin-left: 5%;">&nbsp;&nbsp;&nbsp;No Questionnaire for your role </div>

                 </td>
             </tr>
 </logic:notPresent>--%>
<logic:present name="QuestMenu">
        <logic:empty name="QuestMenu">
              <tr><td  height="5"></td></tr>
             <tr>
                 <td>
                     <div style="color:black;margin-left: 5%;">&nbsp;&nbsp;&nbsp;No Questionnaire Present</div>

                 </td>
             </tr>

         </logic:empty>
 </logic:present>



</table>
<!-- Name of the person whose Questionnaire is displaying -->

        <% String name=(String)session.getAttribute("prop_personName");
         String message = (String)request.getAttribute("ppcAnsweredDetails");
        if(name!=null) {%>
        <div style="height: 4px"></div>
    <table width="100%" align=center  border="0" cellpadding="0" cellspacing="0" class="tabtable">
          <tr valign=top>
            <td>
                <table width="100%"  border="0" cellpadding="0" cellspacing="0" class="tableheader">
                <tr valign=top>
                    <td width='50%' align=left nowrap>
                        <div id='name'>
                            <% if(message!=null){%>
                                 <center>   Certification for : <%=name%> and <%=message%></center> 
                            <%}else{%>
                                 <center>   Certification for : <%=name%> </center>
                            <%}%>   
                        </div>
                    </td>

                </tr>
                </table>
            </td>
        </tr>
    </table>


        <%}%>
</logic:notEqual>
</logic:notPresent>


        <%--<h1>Hello World!</h1>--%>
    </body>
</html:html>
