<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
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
<jsp:useBean id="BudgetForProposalDet" scope="request" class="java.util.Vector"/>
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
 <%
     String flag="";
     flag=(String)session.getAttribute("flag_DISPLAY_PROP");
     session.removeAttribute("flag_DISPLAY_PROP");
     boolean blnReadOnly = false;
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
<title>Summary</title>
  <script language="javascript" type="text/JavaScript" src="<%=request.getContextPath()%>/coeuslite/mit/utils/scripts/divSlide.js"></script>
    <script language="javascript" type="text/JavaScript" src="..\scripts\_js.js"></script>
    <script language="javascript" type="text/JavaScript" src="..\scripts\CheckForward.js"></script>
<script language="JavaScript" type="text/JavaScript">
    //Questionnaire Details  Div Hiding script starts......Athul
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
//Questionnaire Details  Div Hiding script ends......Athul
    function summaryHeader(value) {
        if(value==1) {

            document.getElementById('proposalHide').style.display = 'block';
            document.getElementById('proposalHideImage').style.display = 'block';
            document.getElementById('proposalDetailsShow').style.display = 'block';
            document.getElementById('proposalShow').style.display = 'none';
            document.getElementById('proposalShowImage').style.display = 'none';


            //testing budget----

          //  budgetDetails(2);

        } else if(value==2) {
            document.getElementById('proposalHideImage').style.display = 'none';
            document.getElementById('proposalHide').style.display = 'none';
            document.getElementById('proposalDetailsShow').style.display = 'none';
            document.getElementById('proposalShowImage').style.display = 'block';
            document.getElementById('proposalShow').style.display = 'block';



            //testing budget-------
           // budgetDetails(1);
        }else if(value==3) {
            document.getElementById('budgetHide').style.display = 'block';
            document.getElementById('budgetHideImage').style.display = 'block';
           // document.getElementById('budgetTotal').style.display = 'block';
            document.getElementById('budgetPeriod').style.display = 'block';
            document.getElementById('budgetReport').style.display = 'block';
            document.getElementById('budgetShow').style.display = 'none';
            document.getElementById('budgetShowImage').style.display = 'none';
        } else if(value==4) {
            hideBudget();
        } else if(value==5) {
            document.getElementById('narrativeHide').style.display = 'block';
            document.getElementById('narrativeHideImage').style.display = 'block';
            document.getElementById('narrativeDetails').style.display = 'block';
            document.getElementById('narrativeShow').style.display = 'none';
            document.getElementById('narrativeShowImage').style.display = 'none';
        } else if(value==6) {
            document.getElementById('narrativeHide').style.display = 'none';
            document.getElementById('narrativeHideImage').style.display = 'none';
            document.getElementById('narrativeDetails').style.display = 'none';
            document.getElementById('narrativeShow').style.display = 'block';
            document.getElementById('narrativeShowImage').style.display = 'block';
        }
        else if(value==7) {
            document.getElementById('specialReviewHide').style.display = 'block';
            document.getElementById('specialReviewHideImage').style.display = 'block';
            document.getElementById('specialReviewShow').style.display = 'none';
            document.getElementById('specialReviewShowImage').style.display = 'none';
              document.getElementById('ListReviewDetails').style.display = 'block';
        }
        else if(value==8) {
          hidereview();
        }

        //investigator key person    start
    if(value==9) {
            document.getElementById('InvKeyHide').style.display = 'block';
            document.getElementById('InvKeyHideImage').style.display = 'block';
            document.getElementById('InvKeyDetails').style.display = 'block';
            document.getElementById('InvKeyShow').style.display = 'none';
            document.getElementById('InvKeyShowImage').style.display = 'none';


            //testing budget----

            //budgetDetails(2);

        } else if(value==10) {
            document.getElementById('InvKeyHideImage').style.display = 'none';
            document.getElementById('InvKeyHide').style.display = 'none';
            document.getElementById('InvKeyDetails').style.display = 'none';
            document.getElementById('InvKeyShowImage').style.display = 'block';
            document.getElementById('InvKeyShow').style.display = 'block';



            //testing budget-------
            //budgetDetails(1);
    }

        //print

       else if(value==21) {
            document.getElementById('PrintHide').style.display = 'block';
            document.getElementById('PrintHideImage').style.display = 'block';
            document.getElementById('PrintShow').style.display = 'none';
            document.getElementById('PrintShowImage').style.display = 'none';
            document.getElementById('Gov').style.display = 'block';

            document.getElementById('SponsorHide').style.display = 'none';
            document.getElementById('SponsorHideImage').style.display = 'none';
            document.getElementById('SponsorShow').style.display = 'block';
            document.getElementById('SponsorShowImage').style.display = 'block';
            document.getElementById('Sponsor').style.display = 'block';
        }
        else if(value==22) {
           hideprint();
        }
//Questionnaire Main  Div Hiding script starts......Athul
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

//Questionnaire Main  Div Hiding script ends......Athul
    }

    function hideBudget() {
        document.getElementById('budgetHide').style.display = 'none';
        document.getElementById('budgetHideImage').style.display = 'none';
        //document.getElementById('budgetTotal').style.display = 'none';
        document.getElementById('budgetPeriod').style.display = 'none';
        document.getElementById('budgetReport').style.display = 'none';
        document.getElementById('budgetShow').style.display = 'block';
        document.getElementById('budgetShowImage').style.display = 'block';
       // document.getElementById('budgetTotalHide').style.display = 'none';
       // document.getElementById('budgetTotalHideImage').style.display = 'none';
        //document.getElementById('budgetTotalDetails').style.display = 'none';
       // document.getElementById('budgetTotalShow').style.display = 'block';
       // document.getElementById('budgetTotalShowImage').style.display = 'block';
        document.getElementById('budgetPeriodHide').style.display = 'none';
        document.getElementById('budgetPeriodHideImage').style.display = 'none';
        document.getElementById('budgetPeriodDetails').style.display = 'none';
        document.getElementById('budgetPeriodShow').style.display = 'block';
        document.getElementById('budgetPeriodShowImage').style.display = 'block';
        document.getElementById('budgetReportHide').style.display = 'none';
        document.getElementById('budgetReportHideImage').style.display = 'none';
        document.getElementById('budgetReportDetails').style.display = 'none';
        document.getElementById('budgetReportShow').style.display = 'block';
        document.getElementById('budgetReportShowImage').style.display = 'block';
    }

   function hidereview() {

        document.getElementById('ListReviewDetails').style.display = 'none';
        document.getElementById('specialReviewHide').style.display = 'none';
        document.getElementById('specialReviewHideImage').style.display = 'none';

        document.getElementById('specialReviewShow').style.display = 'block';
        document.getElementById('specialReviewShowImage').style.display = 'block';
        document.getElementById('ListReviewDetails').style.display = 'none';
    }

     function hideprint() {
         Govt(2);
         SponsorPrint(2);
        document.getElementById('PrintHide').style.display = 'none';
        document.getElementById('PrintHideImage').style.display = 'none';

       document.getElementById('Gov').style.display = 'none';

        document.getElementById('PrintShow').style.display = 'block';
        document.getElementById('PrintShowImage').style.display = 'block';

        document.getElementById('GovHide').style.display = 'none';
        document.getElementById('GovHideImage').style.display = 'none';
        document.getElementById('govDetails').style.display = 'none';
        document.getElementById('GovShow').style.display = 'block';
        document.getElementById('GovShowImage').style.display = 'block';
//sponsor
        document.getElementById('Sponsor').style.display = 'none';
        document.getElementById('SponsorHide').style.display = 'none';
        document.getElementById('SponsorHideImage').style.display = 'none';
        document.getElementById('sponsorDetails').style.display = 'none';
        document.getElementById('SponsorShow').style.display = 'block';
        document.getElementById('SponsorShowImage').style.display = 'block';
    }
function SponsorPrint(value) {
        if(value==1) {
            document.getElementById('SponsorHide').style.display = 'block';
            document.getElementById('SponsorHideImage').style.display = 'block';
            document.getElementById('sponsorDetails').style.display = 'block';
            document.getElementById('SponsorShow').style.display = 'none';
            document.getElementById('SponsorShowImage').style.display = 'none';

        } else if(value==2) {

            document.getElementById('SponsorHide').style.display = 'none';
            document.getElementById('SponsorHideImage').style.display = 'none';
            document.getElementById('sponsorDetails').style.display = 'none';
            document.getElementById('SponsorShow').style.display = 'block';
            document.getElementById('SponsorShowImage').style.display = 'block';
        }
     }
    function budgetDetails(value) {
        if(value==1) {
            document.getElementById('budgetTotalHide').style.display = 'block';
            document.getElementById('budgetTotalHideImage').style.display = 'block';
            document.getElementById('budgetTotalDetails').style.display = 'block';
            document.getElementById('budgetTotalShow').style.display = 'none';
            document.getElementById('budgetTotalShowImage').style.display = 'none';
        } else if(value==2) {
            document.getElementById('budgetTotalHide').style.display = 'none';
            document.getElementById('budgetTotalHideImage').style.display = 'none';
            document.getElementById('budgetTotalDetails').style.display = 'none';
            document.getElementById('budgetTotalShow').style.display = 'block';
            document.getElementById('budgetTotalShowImage').style.display = 'block';
        } else if(value==3) {
            document.getElementById('budgetPeriodHide').style.display = 'block';
            document.getElementById('budgetPeriodHideImage').style.display = 'block';
            document.getElementById('budgetPeriodDetails').style.display = 'block';
            document.getElementById('budgetPeriodShow').style.display = 'none';
            document.getElementById('budgetPeriodShowImage').style.display = 'none';
        } else if(value==4) {
            document.getElementById('budgetPeriodHide').style.display = 'none';
            document.getElementById('budgetPeriodHideImage').style.display = 'none';
            document.getElementById('budgetPeriodDetails').style.display = 'none';
            document.getElementById('budgetPeriodShow').style.display = 'block';
            document.getElementById('budgetPeriodShowImage').style.display = 'block';
        } else if(value==5) {
            document.getElementById('budgetReportHide').style.display = 'block';
            document.getElementById('budgetReportHideImage').style.display = 'block';
            document.getElementById('budgetReportDetails').style.display = 'block';
            document.getElementById('budgetReportShow').style.display = 'none';
            document.getElementById('budgetReportShowImage').style.display = 'none';
        } else if(value==6) {
            document.getElementById('budgetReportHide').style.display = 'none';
            document.getElementById('budgetReportHideImage').style.display = 'none';
            document.getElementById('budgetReportDetails').style.display = 'none';
            document.getElementById('budgetReportShow').style.display = 'block';
            document.getElementById('budgetReportShowImage').style.display = 'block';
     }
     }
     function specialreview(value) {
        if(value==1) {
            document.getElementById('ListReviewDetails').style.display = 'block';
        } else if(value==2) {
            document.getElementById('ListReviewDetails').style.display = 'none';
        }
     }


      function Govt(value) {
        if(value==1) {
            document.getElementById('GovHide').style.display = 'block';
            document.getElementById('GovHideImage').style.display = 'block';
            document.getElementById('govDetails').style.display = 'block';
            document.getElementById('GovShow').style.display = 'none';
            document.getElementById('GovShowImage').style.display = 'none';
        } else if(value==2) {
            document.getElementById('GovHide').style.display = 'none';
            document.getElementById('GovHideImage').style.display = 'none';
            document.getElementById('govDetails').style.display = 'none';
            document.getElementById('GovShow').style.display = 'block';
            document.getElementById('GovShowImage').style.display = 'block';
        }
     }
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

 function unit_search()
      {
      var winleft = (screen.width - 450) / 2;
            var winUp = (screen.height - 350) / 2;
            var win = "scrollbars=yes, resizable=1,width=675,height=360,left="+winleft+",top="+winUp
            sList = window.open('<%=request.getContextPath()%>/generalProposalSearch.do?type=<bean:message key="searchWindow.title.unit" bundle="proposal"/>&search=true&searchName=WEBLEADUNITSEARCH', "list", win);
            if (parseInt(navigator.appVersion) >= 4) {
            window.sList.focus();
            }
      }


function edit_data(data,personId,timestamp,invKeyRole,user){
          document.pdInvestKeyPersForm.acType.value = data;
          document.pdInvestKeyPersForm.personId.value = personId;
          document.pdInvestKeyPersForm.awUpdateTimestamp.value = timestamp;
          document.pdInvestKeyPersForm.propInvUpdateUser.value = user;
          document.pdInvestKeyPersForm.invRoleCode.value = invKeyRole;
          document.pdInvestKeyPersForm.action = "<%=request.getContextPath()%>/editInvKeyPersons.do?role="+invKeyRole;
          document.pdInvestKeyPersForm.submit();
     }

     function view_comments(personId, personName, piFlag)
        {
          if(piFlag == ""){
             piFlag = "KSP";
          }
          document.pdInvestKeyPersForm.action = "<bean:write name='ctxtPath'/>/getMoreDetails.do?personId="+personId+"&personName="+personName+"&piFlag="+piFlag;
          document.pdInvestKeyPersForm.submit();
         }

    function budgetPrintSummary(actionType,printTypeId, selectedIndex){              
        var checkBoxName = 'chkComments'+selectedIndex;            
        var checksElement = document.getElementsByTagName("INPUT");
        var checkBoxCount = checksElement.length;
        var isCommentChecked;            
            for (var index = 0; index < checkBoxCount; index++){
                    if (checksElement[index].name == checkBoxName){
                        isCommentChecked = checksElement[index].checked;                                        
                    }                                          
                }
            var anchorURL = "<%=request.getContextPath()%>/budgetPrintAction.do?printFinalVersion=Y&action="+actionType+"&repId="+printTypeId+"&chkComments="+isCommentChecked;                                                
            window.open(anchorURL);                               
    }

// Investigator Script S T O P S
</script>


          <%--..................printing script starts...............--%>
          <script src="<%=request.getContextPath()%>/coeuslite/mit/utils/scripts/divSlide.js"></script>
        <script LANGUAGE="JavaScript">
        var txtShow = "Show";
        var txtHide = "Hide";

        function showHidePanel(strDivName, imgId, linkId) {
        var disp = document.getElementById(strDivName).style.display;
        var markUp = document.getElementById(linkId).innerHTML;
        if(markUp == txtHide) {
            hide(strDivName, imgId, linkId);
        }else {
            show(strDivName, imgId, linkId);
        }
        }

        function hide(strDivName, imgId, linkId) {
            //document.getElementById(strDivName).style.display = "none";
            //document.getElementById(imgId).src=plus.src;
            //document.getElementById(linkId).innerHTML=txtShow;
            ds = new DivSlider();
            evalStr = "document.getElementById('"+imgId+"').src=plus.src;";
            evalStr = evalStr + "document.getElementById('"+linkId+"').innerHTML=txtShow;";
            ds.hideDiv(strDivName, evalStr);

        }

        function show(strDivName, imgId, linkId) {
            //document.getElementById(strDivName).style.display = "";
            //document.getElementById(imgId).src=minus.src;
            //document.getElementById(linkId).innerHTML=txtHide;
            evalStr = "document.getElementById('"+imgId+"').src=minus.src;";
            evalStr = evalStr + "document.getElementById('"+linkId+"').innerHTML=txtHide;";
            ds = new DivSlider();
            ds.showDiv(strDivName, evalStr);

        }

        var plus = new Image();
        var minus = new Image();
        function preLoadImages() {
        plus.src = "coeusliteimages/plus.gif";
        minus.src = "coeusliteimages/minus.gif";
        }


        var formDetails = new Array();
        var packageIndex = 0;
        var prevDivId = "";
        var prevPackageIndex = -1;

        var lastRowId = -1;
        var lastDataRowId;
        var lastLinkId;
        var lastDivId;
        var expandedRow = -1;

        var defaultStyle = "rowLine";
        var selectStyle = "rowHover copybold";

        var txtShow = "Show";
        var txtHide = "Hide";

        function selectRow(rowId, divId, dataRowId, linkId, packageIndex) {
            var row = document.getElementById(rowId);
            if(rowId == lastRowId) {
                //Same Row just Toggle
                var style = document.getElementById(dataRowId).style.display;
                if(style == "") {
                    style = "none";
                    styleClass = defaultStyle;
                    toggleText = txtShow;
                    expandedRow = -1;
                }else{
                    style = "";
                    styleClass = selectStyle;
                    toggleText = txtHide;
                    expandedRow = rowId;
                }
                //document.getElementById(dataRowId).style.display = style;
                ds = new DivSlider();
                if(style == "") {
                    document.getElementById(dataRowId).style.display = "";
                    ds.showDiv(divId);
                }else {
                    var evalStr = "document.getElementById('"+dataRowId+"').style.display = 'none'";
                    ds.hideDiv(divId, evalStr);
                }
                //row.className = styleClass;
                row.className = defaultStyle;

                document.getElementById(linkId).innerHTML=toggleText;
            }else {
                //document.getElementById(dataRowId).style.display = "";
                document.getElementById(dataRowId).style.display = "";
                ds = new DivSlider();
                ds.showDiv(divId);
                row.className = defaultStyle;

                document.getElementById(linkId).innerHTML=txtHide;
                //row.className = selectStyle;
                expandedRow = rowId;

                //reset last selected row
                if(lastRowId != -1) {
                    row = document.getElementById(lastRowId);
                    //document.getElementById(lastDataRowId).style.display = "none";
                    ds2 = new DivSlider();
                    var evalStr = "document.getElementById('"+lastDataRowId+"').style.display = 'none'"
                    ds2.hideDiv(lastDivId, evalStr);

                    document.getElementById(lastLinkId).innerHTML=txtShow;
                    row.className = defaultStyle;
                }
            }

            resetCheckBoxes(prevPackageIndex, false);

            prevPackageIndex = packageIndex;
            lastRowId = rowId;
            lastDataRowId = dataRowId;
            lastLinkId = linkId;
            lastDivId = divId;
        }

        function rowHover(rowId, styleName) {
            elemId = document.getElementById(rowId);
            elemId.style.cursor = "hand";
            //If row is selected retain selection style
            //Apply hover Style only if row is not selected
            if(rowId != expandedRow) {
                elemId.className = styleName;
            }
        }

        function resetCheckBoxes(packageIndex, value){
            //reset checkboxes for previous selected package
            if(prevPackageIndex != -1) {
                checkBoxArray = formDetails[packageIndex];
                for(index = 0; index < checkBoxArray.length; index++) {
                    elementName = checkBoxArray[index];
                    document.proposalPrint.elements[elementName].checked = value;
                }
            }//End if
        }

        function validateProposalPrint() {
            formSelected = false;
            //presently selected package would be saves as previousPackageIndex.
            checkBoxArray = formDetails[prevPackageIndex];
            for(index = 0; index < checkBoxArray.length; index++) {
                elementName = checkBoxArray[index];
                if(document.proposalPrint.elements[elementName].checked) {
                    formSelected = true;
                }
            }
            if(!formSelected) {
                alert("Select forms to print");
                return false;
            }
            return true;
        }

        <%
            DynaBeanList dynaBeanList= (DynaBeanList)request.getAttribute("grantsGov");
            int availForms = 0;
            if(dynaBeanList != null && dynaBeanList.getList() != null){
                availForms = dynaBeanList.getList().size();
            }
        %>
        var forms = <%=availForms%>;

        function printForms() {
            var checkBoxName;
            var selForms = "";
            var available = "";

            for(index=0; index < forms; index++) {
                checkBoxName = "listBean["+index+"].print"
                if(document.grantsGov.elements[checkBoxName].checked) {
                    if(selForms.length > 0) {
                        selForms = selForms + ","+ index;
                    }else {
                        selForms = ""+index;
                    }
                }
            }

            if(selForms.length == 0) {
                alert("<bean:message bundle="proposal" key="grantsgov.selectFormsToPrint"/>");
                return;
            }

            document.grantsGov.elements["simpleBean[0].submitType"].value = "Print";
            document.grantsGov.target="myWindow";
            document.grantsGov.submit();

            document.grantsGov.target="";
            //window.open("grantsGovAction.do?action=Print&selForms="+selForms);

        }//End Print Forms

        function selectDeselectAll(value){
            var checkBoxName;
            for(index=0; index < forms; index++) {
                checkBoxName = "listBean["+index+"].print"
                document.grantsGov.elements[checkBoxName].checked = value;
            }
        }

        function doNothing(){}

        preLoadImages();
        </script>

          <%--..................printing script stops...............--%>







<!--10 november 2010 new script  STARTS-->

<script language="javascript" type="text/JavaScript" src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/coeusLiteCalendar.js"></script>
        <script>
        var errValue = false;
        var typeCode = '';
        function delete_data(data,desc,timestamp,apcode,spcode,protonum){
         if (confirm("Are you sure you want to delete the special review?")==true){
            document.propDevSpecialReview.acType.value = data;
            document.propDevSpecialReview.specialReviewNumber.value = desc;
            document.propDevSpecialReview.approvalCode.value = apcode;
            document.propDevSpecialReview.specialReviewCode.value = spcode;
            document.propDevSpecialReview.spRevProtocolNumber.value = protonum;
            document.propDevSpecialReview.pdSpTimestamp.value = timestamp;
            document.propDevSpecialReview.action = "<%=request.getContextPath()%>/deleteSpecialReview.do";
            document.propDevSpecialReview.submit();
         }
        }

        function insert_data(data){
        document.propDevSpecialReview.acType.value = data;
        document.propDevSpecialReview.action = "<%=request.getContextPath()%>/propSpecialReview.do";
        }
        </script>
<%
//QuestionnaireAnswerHeaderBean questionnaireModuleObject =
 //           (QuestionnaireAnswerHeaderBean)session.getAttribute("questionnaireModuleObject");
 //   if(questionnaireModuleObject == null){
  //      questionnaireModuleObject = new QuestionnaireAnswerHeaderBean();
  //  }
  //  int count = 0;
  //  HashMap hmQuestionnaireData = (HashMap)session.getAttribute("questionnaireInfo");
  //  hmQuestionnaireData = (hmQuestionnaireData == null)? new HashMap() : hmQuestionnaireData;
    %>
        <script>
        function validateForm(form) {
          insert_data("I");
          return validatePropDevSpecialReview(form);
        }

      function view_comments(value) {
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

                var loc = '<bean:write name='ctxtPath'/>/coeuslite/mit/utils/dialogs/cwViewSpecialReview.jsp?value='+value+"&type=pS";
                var newWin = window.open(loc,"formName",'dependent=1,toolbar=0,location=0,directories=0,status=0, menubar=0,scrollbars=0,resizable=0,width=' + w + ',height=' + h+ ',left='+ leftPos + ',top=' + topPos);
                newWin.window.focus();

         }

         //Added for case#2990 - Proposal Special Review Enhancement - start
         function clearFields(sel){
            dataChanged();
            <%if(enableProtocolToDevPropLink.equals("1")){%>
                typeCode = sel.options[sel.selectedIndex].value;
                if(typeCode == '1'){
                    document.propDevSpecialReview.tempApprovalText.value = '';
                    document.propDevSpecialReview.tempApplicationDate.value = '';
                    document.propDevSpecialReview.tempApprovalDate.value = '';
                    document.getElementById('hide_Search').style.display = 'block';
                    document.getElementById('applnDate1').style.display = 'none';
                    document.getElementById('applnDate2').style.display = 'block';
                    document.getElementById('apprvDate1').style.display = 'none';
                    document.getElementById('apprvDate2').style.display = 'block';
                    document.getElementById('divAppCode1').style.display = 'none';
                    document.getElementById('divAppCode2').style.display = 'block';
                    document.getElementById('divMandatory1').style.display = 'block';
                    document.getElementById('divMandatory2').style.display = 'none';
                }else{
                    document.propDevSpecialReview.approvalCode.selectedIndex = '';
                    document.propDevSpecialReview.tempApplicationDate.value = '';
                    document.propDevSpecialReview.tempApprovalDate.value = '';
                    document.propDevSpecialReview.spRevProtocolNumber.value = '';
                    document.propDevSpecialReview.tempApprovalCode.value = '';
                    document.getElementById('hide_Search').style.display = 'none';
                    document.getElementById('applnDate1').style.display = 'block';
                    document.getElementById('applnDate2').style.display = 'none';
                    document.getElementById('apprvDate1').style.display = 'block';
                    document.getElementById('apprvDate2').style.display = 'none';
                    document.getElementById('divAppCode1').style.display = 'block';
                    document.getElementById('divAppCode2').style.display = 'none';
                    document.getElementById('divMandatory1').style.display = 'none';
                    document.getElementById('divMandatory2').style.display = 'block';
                }
            <%}%>
         }

         function openProtocolSearch(){
            var winleft = (screen.width - 650) / 2;
            var winUp = (screen.height - 450) / 2;
            var win = "scrollbars=1,resizable=1,width=800,height=450,left="+winleft+",top="+winUp;
            sList = window.open('<%=request.getContextPath()%>/irbSearch.do?type=<bean:message key="searchWindow.title.protocol"/>&search=true&searchName=ALL_PROTOCOL_SEARCH', "list", win);
         }

         function fetch_Data(result){
            document.propDevSpecialReview.spRevProtocolNumber.value = '';
            document.propDevSpecialReview.tempApplicationDate.value = '';
            document.propDevSpecialReview.tempApprovalDate.value = '';
            document.propDevSpecialReview.tempApprovalText.value = '';

            if(typeCode == '1'){
                document.propDevSpecialReview.spRevProtocolNumber.value = result["PROTOCOL_NUMBER"];
                if(result["APPLICATION_DATE"] != 'null' && result["APPLICATION_DATE"] != undefined ){
                    document.propDevSpecialReview.tempApplicationDate.value = result["APPLICATION_DATE"];
                }
                if(result["APPROVAL_DATE"] != 'null' && result["APPROVAL_DATE"] != undefined ){
                    document.propDevSpecialReview.tempApprovalDate.value = result["APPROVAL_DATE"];
                }
                document.propDevSpecialReview.tempApprovalCode.value = '2';
                document.propDevSpecialReview.tempApprovalText.value = result["PROTOCOL_STATUS_DESCRIPTION"];
                //Addded for Case#4354 - When ENABLE_PROTOCOL_TO_DEV_PROPOSAL is set - Start
                if(result["PROTOCOL_STATUS_CODE"] != 'null' && result["PROTOCOL_STATUS_CODE"] != undefined && result["PROTOCOL_STATUS_CODE"] == '203'){
                    var protocolNumber = result["PROTOCOL_NUMBER"];
                    document.propDevSpecialReview.action = "<%=request.getContextPath()%>/propSpecialReview.do?&ExemptCheckList=true&protocolNumber="+protocolNumber;
                    document.propDevSpecialReview.submit();
                }
                //Case#4354 - End
            }
         }
         //Added for case#2990 - Proposal Special Review Enhancement - end
         //Addded for Case#4354 - When ENABLE_PROTOCOL_TO_DEV_PROPOSAL is set - Start
         function loadExemptCheckList(){
            <%String specialReviewComment = (String)request.getAttribute("comments");
            if(specialReviewComment != null){%>
                 var spComments = '<%=specialReviewComment%>';
                 document.propDevSpecialReview.comments.value = spComments;
             <%}%>
         }
         //Added for Case#4353 - End
function certify(lvar,Pid,pNo)
{
    <%--document.pdInvestKeyPersForm.action="<%=request.getContextPath()%>/rightPersonCertify.do?proposalNumber="+lvar;
    document.pdInvestKeyPersForm.submit();--%>

    document.pdInvestKeyPersForm.action="<%=request.getContextPath()%>/toGetRoledQuestionnaire.do?personName="+lvar+"&propPersonId="+Pid+"&proposalNumber="+pNo+"&page=proposalSummary";   
    document.pdInvestKeyPersForm.submit();

}

<!--Added for Case#3524 - Add Explanation field to Questions - Start -->
function showQuestion(link) {
    var winleft = (screen.width - 650) / 2;
    var winUp = (screen.height - 450) / 2;
    var win = "scrollbars=1,resizable=1,width=790,height=450,left="+winleft+",top="+winUp
    sList = window.open('<bean:write name='ctxtPath'/>'+"/"+link, "list", win);
    if (parseInt(navigator.appVersion) >= 4) {
    window.sList.focus();
    }
}
<!--Added for Case#3524 - Add Explanation field to Questions - End -->
         function view_data(qnrid){


    //Modified with case 4287:Questionnaire Templates.
    //The printing would be Print Questions n Answers and PrintAll Questions by default.
    //var winleft = (screen.width - 650);
    //var winUp = (screen.height - 450);
    //var win = "scrollbars=1,resizable=0,width=400,height=100,left="+winleft+",top="+winUp
    //var url_value="<%=request.getContextPath()%>/coeuslite/utk/propdev/clPrintOption.jsp";
    //window.open(url_value,'',win);
    var operation="PRINT";
    var printQuestion = 'N';
    var printAllQuestion = 'Y';
    var actionFrom='DEV_PROPOSAL';
    var proposalSummary='PROPOSAL_SUMMARY';
    window.open("<%=request.getContextPath()%>/saveQuestionnaireData.do?&questionnaireId="+qnrid+"&operation="+operation+"&printQuestions="+printQuestion+"&printAllQuestions="+printAllQuestion+"&printQnrId="+qnrid+"&proposalSummary="+proposalSummary);
    //4287 End
}
        </script>
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
   <!--10 november 2010 new script STOPS -->
<html:base/>
</head>
<body onload="javascript:loadExemptCheckList()">
<%String plus = request.getContextPath()+"/coeusliteimages/plus.gif";
  String minus = request.getContextPath()+"/coeusliteimages/minus.gif";%>

  <html:form action="/propdevInvestigatorKeyPerson.do" >
  <html:form action="/propSpecialReview"  method="post">
        <%--<a name="top"> </a>--%>

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table">

<tr valign=top>
    <td>
        <table width="98%" align=center  border="0" cellpadding="0" cellspacing="0" class="tabtable">
        <%-- commeneted for COEUSQA-3483 : Changes to Protocol Summary screen in Routing in Lite - start --%>
        <%--<tr valign=top>
            <td>
                <table width="100%"  border="0" cellpadding="0" cellspacing="0" class="tableheader">
                <tr valign=top>
                    <td width='50%' align=left nowrap>
                        <div id='proposalShowImage' style='display: none;'>
                            <html:link href="javaScript:summaryHeader('1');">
                            <html:img src="<%=plus%>" border="0"/></html:link>&nbsp;<bean:message bundle="proposal" key="summary.proposalSummary"/>
                        </div>
                        <div id='proposalHideImage'>
                            <html:link href="javaScript:summaryHeader('2');">
                            <html:img src="<%=minus%>" border="0"/></html:link>&nbsp;<bean:message bundle="proposal" key="summary.proposalSummary"/>
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
        </tr>--%>
        <%-- commeneted for COEUSQA-3483 : Changes to Protocol Summary screen in Routing in Lite - end --%>
       
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
                        <logic:iterate id="budgetData" name="BudgetForProposalDet" type="org.apache.commons.beanutils.DynaBean">
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
                                        <coeusUtils:formatDate name="budgetData" property="reqStartDateInitial"/> -
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
        <tr><td height="2"></td></tr>
        </table>
    </td>
</tr>
<%-- ================= LIST OF INVESTIGATORS/KEY PERSONS  S T A R T ===============--> --%>
 <logic:notEmpty name = "proposalInvKeyData" scope="session">
<tr valign=top>
    <td>
 <table width="98%" align=center  border="0" cellpadding="0" cellspacing="0" class="tabtable">
        <tr valign=top>
            <td>
                <table width="100%"  border="0" cellpadding="0" cellspacing="0" class="tableheader">
                <tr valign=top>
                    <td width='50%' align=left nowrap>
                        <div id='InvKeyShowImage' style='display: none;'> <!-- -->
                            <html:link href="javaScript:summaryHeader('9');">
                            <html:img src="<%=plus%>" border="0"/></html:link>&nbsp;<bean:message bundle="proposal" key="proposalSummaryDetail.LstOfInvKey"/>
                       </div>
                        <div id='InvKeyHideImage'>
                            <html:link href="javaScript:summaryHeader('10');">
                            <html:img src="<%=minus%>" border="0"/></html:link>&nbsp;<bean:message bundle="proposal" key="proposalSummaryDetail.LstOfInvKey"/>
                       </div>
                    </td>



                    <td width='50%' align=right>
                        <html:link href="javaScript:summaryHeader('9');">
                        <div id='InvKeyShow' style='display: none;'>
                            <bean:message bundle="proposal" key="summary.show"/>
                        </div>
                        </html:link>
                        <html:link href="javaScript:summaryHeader('10');">
                        <div id='InvKeyHide'>
                            <bean:message bundle="proposal" key="summary.hide"/>
                        </div>
                        </html:link>
                    </td>
                </tr>
                </table>
            </td>

                 </tr>
<!---- to be filled--->

<tr>
            <td height='2'>
            </td>
        </tr>


                <tr>
                    <td align="center" valign="top">

                        <div  id='InvKeyDetails'>
                            <table width="98%"  border="0" cellspacing="1" cellpadding="3" class="tabtable" align="center">
                        <tr align="center" valign="top">
                            <td  align="left" class="theader"><bean:message bundle="proposal" key="proposalInvKeyPersons.Name"/></td>
                            <td  align="left" class="theader"><bean:message bundle="proposal" key="proposalInvKeyPersons.Department"/></td>
                            <td  align="left" class="theader"><bean:message bundle="proposal" key="proposalInvKeyPersons.column.leadUnit"/></td>
                            <td  align="left" class="theader"><bean:message bundle="proposal" key="proposalInvKeyPersons.column.multiPI"/></td>
                            <td  align="left" class="theader"><bean:message bundle="proposal" key="proposalInvKeyPersons.role"/></td>
                           <%-- <%if(flag.equalsIgnoreCase("1")){%>

                             <td  align="left" class="theader">Certify</td>
                             <%} else {%>--%>
                             <td  align="left" class="theader"></td>
                             <td  align="left" class="theader"></td>
                          <%--    <%}%> --%>
                        </tr>


                      <% String strBgColor_key = "#DCE5F1";  //BGCOLOR=EFEFEF  FBF7F7
                          int count_key = 0;
                         %>
                      <logic:present name="proposalInvKeyData">
                      <logic:iterate id="propInvBean" name="proposalInvKeyData"
                                     type="org.apache.struts.validator.DynaValidatorForm">
                                      
                       <% if (count_key%2 == 0)
                            strBgColor_key = "#D6DCE5";
                         else
                            strBgColor_key="#DCE5F1";
                       
                       
                    
                            
                            %>

                <%   //System.out.println("Hello=>personId=>"+propInvBean.get("personId"));%>

                      <%
                      	String piFlag = (String) propInvBean.get("principalInvestigatorFlag");
                                                                  							String multiPIFlag = (String) propInvBean.get("multiPIFlag");

                                                                  							
                                                                  							
                                                                  										
                                                                  			//Malini:Code to check the status flag added here:
                      							String fontClass = "copy";
                      							String isExternalFlag = (String) propInvBean.get("isExternal");
                      							String status = (String) propInvBean.get("status");
                      							if (status == null) {
                      								fontClass = "copyExternalPerson";
                      							} else if (status.trim().equals("I") && isExternalFlag.equals("N")) {
                      								fontClass = "copyInactivePerson";
                      							} else if (status.trim().equals("A") && isExternalFlag.equals("N")) {
                      								fontClass = "copy";
                      							} else if (status.trim().equals("I") && isExternalFlag.equals("Y")) {
                      								fontClass = "copyInactivePerson";
                      							} else if (status.trim().equals("")
                      									&& isExternalFlag.equals("Y")) {
                      								fontClass = "copyExternalPerson";
                      							} else {
                      								fontClass = "copyExternalPerson";
                      							}
                      %>

                          <% String invKeyRole = "2";   // default to key person role
                             if (piFlag!=null && (piFlag.equals("Y") || piFlag.equals("N"))) {
                                invKeyRole = "1";       // default to co-PI
                                if (piFlag.equals("Y")) {
                                   invKeyRole = "0";    // set to be PI but when pass it to delete_data (javascription function), the 'value'
                                                        // will not be set to "0"; must use special check in InvKeyPersonAction (Server side)
                                }
                             }%>
                  
<tr bgcolor="<%=strBgColor_key%>" onmouseover="className='TableItemOn'" onmouseout="className='TableItemOff'">
                 
                  <td class="<%=fontClass%>"> 

                                <%=propInvBean.get("personName")%>

                 </td>
                 <td class="copy">
                   <% java.util.ArrayList vecInvUnits = (ArrayList)propInvBean.get("investigatorUnits");
                           String strLeadUnit = "";
                           String strLeadUnitNum = "";
                           boolean isLeadUnit = false;
                           if (vecInvUnits != null&& vecInvUnits.size()>0) {
                               //Added for case#3296 - Lead Unit Check box placement - start
                               Vector vecUnits = new Vector();
                               //Find the unit which is the lead unit and add it at the first in the collection
                               for(int idx=0; idx<vecInvUnits.size(); idx++){
                                   ProposalLeadUnitFormBean propInvUnitsBean =
                                           (ProposalLeadUnitFormBean)vecInvUnits.get(idx);
                                   if(propInvUnitsBean.isLeadUnitFlag()){
                                       vecUnits.add(propInvUnitsBean);
                                       break;
                                   }
                               }
                               //Add the remaining units if any
                               for(int idx=0; idx<vecInvUnits.size(); idx++){
                                   ProposalLeadUnitFormBean propInvUnitsBean =
                                           (ProposalLeadUnitFormBean)vecInvUnits.get(idx);
                                   if(!propInvUnitsBean.isLeadUnitFlag()){
                                       vecUnits.add(propInvUnitsBean);
                                   }
                               }
                               //Added for case#3296 - Lead Unit Check box placement - end
                               for(int i=0 ; i<vecUnits.size() ; i++){
                                ProposalLeadUnitFormBean propInvUnitsBean = (ProposalLeadUnitFormBean)vecUnits.get(i);
                                strLeadUnit = propInvUnitsBean.getUnitName();
                                strLeadUnitNum = propInvUnitsBean.getUnitNumber();
                                isLeadUnit = propInvUnitsBean.isLeadUnitFlag();%>
                                <li><%=strLeadUnit%></li>
                           <%}
                      }%>

                <td class="copy">
                          <% if (piFlag !=null && piFlag.equals("Y")) { %>
                            <img src="<bean:write name='ctxtPath'/>/coeusliteimages/checked.gif">
                         <% } else { %>
                            <img src="<bean:write name='ctxtPath'/>/coeusliteimages/spacer.gif" width="11" height="11">
                         <% } %>
                            </td>

                <td class="copy">
                    <%if(multiPIFlag != null && multiPIFlag.equals("Y")){%>
                                <img src="<bean:write name='ctxtPath'/>/coeusliteimages/checked.gif">
                            <%}else{%>
                                <img src="<bean:write name='ctxtPath'/>/coeusliteimages/spacer.gif" width="11" height="11">
                            <%}%>
               </td>
               <td class="copy">
                    <logic:equal name="propInvBean" property="principalInvestigatorFlag" value="Y">
                                   Principal Investigator
                               </logic:equal>
                               <logic:equal name="propInvBean" property="principalInvestigatorFlag" value="N">
                                   Co-Investigator
                               </logic:equal>
                          <%String keyPersonRole = (String)propInvBean.get("keyPersRole");
                                String EMPTY_STRING = "";
                                if (keyPersonRole != null && !keyPersonRole.equals(EMPTY_STRING)) { %>
                                  <%=keyPersonRole%>
                              <%}%>
               </td>
                        <%


                             String completeImage= request.getContextPath()+"/coeusliteimages/complete.gif";
                             String noneImage= request.getContextPath()+"/coeusliteimages/none.gif";
                             String certifyPersonId = (String)propInvBean.get("personId");
                             String certifyPersonName = (String) propInvBean.get("personName");
                             //If certifyPersonName contains ' use ` instead so that javascript doesn't think its string concatenation character
                             certifyPersonName = certifyPersonName.replace('\'','`');
                             int certifyFlagValue = 0;
                             // JM 4-30-2013 commented this out because it doesn't work
                             //if(propInvBean.get("certifyFlag")!=null){
                             //certifyFlagValue =   Integer.parseInt(propInvBean.get("certifyFlag").toString()); 
                             //}
                              %>

                       <%if(flag.equalsIgnoreCase("1")){
                           // JM 6-22-2015 also commenteding this out because it doesn't work - causes page load to fail
                    	   //int certifyFlagValue = 0;
                           //if(propInvBean.get("certifyFlag")!=null && !propInvBean.get("certifyFlag").toString().isEmpty()){
                           //  certifyFlagValue =   Integer.parseInt(propInvBean.get("certifyFlag").toString()); 
                           //}
                        %>
                         <td align="center" class="copy">
                            <% if(certifyFlagValue != -1 ){ %>
                                                     <a href="javascript:certify('<%=propInvBean.get("personName")%>','<%=propInvBean.get("personId")%>','<%=propInvBean.get("proposalNumber")%>')"> <bean:message bundle="proposal" key="proposalInv.certify"/> </a>
                            <%}%>
                         </td>
                       <% } else {%>
                       <td align="center" class="copy">
                        <%if (certifyRight !=null && (certifyRight.equals("YES") || (modeValue && certifyRight.equals("NO") ))) {
                            if( piFlag.equals("Y") || piFlag.equals("N") ) {%>

                                <a href="javascript:certifyInvestigator('<%=propInvBean.get("personId")%>','<%=propInvBean.get("proposalNumber")%>','<%=certifyPersonName%>')"> <bean:message bundle="proposal" key="proposalInv.certify"/> </a>
                            <%}
                         }else{%>
                            Certify
                         <%}%>
                        </td>
                         <%}%>
                        <td align="left">
                                 <logic:equal name="propInvBean" property="certifyFlag" value="-1">
                                    &nbsp;
                                </logic:equal>
                                 <logic:equal name="propInvBean" property="certifyFlag" value="0">
                                    <html:img src="<%=noneImage%>"/>
                                </logic:equal>
                                <logic:equal name="propInvBean" property="certifyFlag" value="1">
                                    <html:img src="<%=completeImage%>"/>
                                </logic:equal>                                 
                        </td>

                      </logic:iterate>
                        </logic:present>
                            </tr>
                            </table>
                        </div>
                       </td>
                         </tr>
         <tr><td height="3"></td></tr>
                          </table>
                           </td>
                            </tr>
                             </logic:notEmpty>

                            <!-- investigator ends -->
<logic:notEmpty name = "budgetPeriods" scope="request">
<tr valign=top>
    <td>
        <table width="98%" align=center  border="0" cellpadding="0" cellspacing="0" class="tabtable">
        <tr valign=top>
            <td>
                <table width="100%"  border="0" cellpadding="0" cellspacing="0" class="tableheader">
                <tr valign=top>
                    <td width='50%' align=left nowrap>
                        <div id='budgetShowImage' style='display: none;'>
                            <html:link href="javaScript:summaryHeader('3');">
                            <html:img src="<%=plus%>" border="0"/></html:link>&nbsp;<bean:message bundle="proposal" key="summary.budgetSummary"/>
                        </div>
                        <div id='budgetHideImage'>
                            <html:link href="javaScript:summaryHeader('4');">
                            <html:img src="<%=minus%>" border="0"/></html:link>&nbsp;<bean:message bundle="proposal" key="summary.budgetSummary"/>
                        </div>
                    </td>
                     <td width='50%' align=right>
                        <html:link href="javaScript:summaryHeader('3');">
                        <div id='budgetShow' style='display: none;'>
                            <bean:message bundle="proposal" key="summary.show"/>
                        </div>
                        </html:link>
                        <html:link href="javaScript:summaryHeader('4');">
                        <div id='budgetHide'>
                            <bean:message bundle="proposal" key="summary.hide"/>
                        </div>
                        </html:link>
                    </td>
                </tr>
                </table>
            </td>
        </tr>

        <tr>
            <td height='1'>
            </td>
        </tr>

        <tr>
            <td align="center">
                <div style="width: 98%" align="center">
                        <div id='budgetPeriod'>
                        <table width="100%" align=center border="0" cellpadding="1" cellspacing="0" class='theader'>
                        <tr>
                            <td width='50%' align=left nowrap>
                                <div id='budgetPeriodShowImage' style='display: none;'>
                                    <html:link href="javaScript:budgetDetails('3');">
                                        &nbsp;<html:img src="<%=plus%>" border="0"/></html:link>&nbsp;<bean:message bundle="proposal" key="summary.budgetPeriod"/>
                                </div>
                                <div id='budgetPeriodHideImage'>
                                    <html:link href="javaScript:budgetDetails('4');">
                                        &nbsp;<html:img src="<%=minus%>" border="0"/></html:link>&nbsp;<bean:message bundle="proposal" key="summary.budgetPeriod"/>
                                </div>
                            </td>
                            <td width='50%' align=right>
                                <html:link href="javaScript:budgetDetails('3');">
                                <div id='budgetPeriodShow' style='display: none;'>
                                    <bean:message bundle="proposal" key="summary.show"/>
                                </div>
                                </html:link>
                                <html:link href="javaScript:budgetDetails('4');">
                                <div id='budgetPeriodHide'>
                                    <bean:message bundle="proposal" key="summary.hide"/>
                                </div>
                                </html:link>
                            </td>
                        </tr>

                        </table>
                        </div>


                        <logic:present name="budgetPeriods" scope="request">
                        <div id='budgetPeriodDetails'>
                            <table width="100%"  border="0" cellspacing="1" cellpadding="0" class="tabtable" >
                                <tr><td height="2" colspan="8"></td></tr>
                                <tr align="center" valign="top" >
                            <td  align="left" class="theader"><bean:message bundle="budget" key="summaryLabel.Period"/></td>
                            <td  align="left" class="theader"><bean:message bundle="budget" key="summaryLabel.StartDate"/> </td>
                            <td  align="left" class="theader"><bean:message bundle="budget" key="summaryLabel.EndDate"/> </td>
                            <td  align="left" class="theader"><bean:message bundle="budget" key="summaryLabel.DirectCost"/> </td>
                            <td  align="left" class="theader"><bean:message bundle="budget" key="summaryLabel.IndirectCost"/> </td>
                            <td  align="left" class="theader"><bean:message bundle="budget" key="summaryLabel.UnderRecovery"/> </td>
                            <td  align="left" class="theader"><bean:message bundle="budget" key="summaryLabel.CostSharing"/> </td>
                            <td  align="left" class="theader"><bean:message bundle="budget" key="summaryLabel.TotalCost"/> </td>
                        </tr>
                         <%
                            String strBgColor = "#DCE5F1";
                             int index=0;
                            %>

                        <logic:iterate id="data" name="budgetPeriods" type="org.apache.struts.validator.DynaValidatorForm">
                        <%
                           if (index%2 == 0) {
                                strBgColor = "#D6DCE5";
                            }
                           else {
                                strBgColor="#DCE5F1";
                             }  %>
                        <tr bgcolor="<%=strBgColor%>" onmouseover="className='TableItemOn'" onmouseout="className='TableItemOff'">
                            <td class="copy">
                                <coeusUtils:formatOutput name="data" property="budgetPeriod"/>
                            </td>
                            <td class="copy">
                                <coeusUtils:formatDate name="data" property="startDate"/>-
                            </td>
                            <td class="copy">
                                <coeusUtils:formatDate name="data" property="endDate"/>
                            </td>
                            <td class="copy">
                                 <coeusUtils:formatString name="data" property="totalDirectCost"
                                    formatType="currencyFormat"/>
                            </td>
                            <td class="copy">
                                 <coeusUtils:formatString name="data" property="totalIndirectCost"
                                    formatType="currencyFormat"/>
                            </td>
                            <td class="copy">
                                 <coeusUtils:formatString name="data" property="underRecoveryAmount"
                                    formatType="currencyFormat"/>
                            </td>
                            <td class="copy">
                                 <coeusUtils:formatString name="data" property="costSharingAmount"
                                    formatType="currencyFormat"/>
                            </td>
                            <td class="copy">
                                 <coeusUtils:formatString name="data" property="totalCost"
                                    formatType="currencyFormat"/>
                            </td>
                            <%
                             index++;
                           %>
                        </tr>
                        </logic:iterate>
                        </table>
                        </div>
                        </logic:present>

                </div>
            </td>
        </tr>

        <tr>
            <td height='1'>
            </td>
        </tr>

        <tr>
            <td align="center">
                <div style="width: 98%" align="center">

                        <div id='budgetReport'>
                        <table width="100%" align=center border="0" cellpadding="0" cellspacing="0" class='theader'>
                        <tr>
                            <td width='50%' align=left nowrap>
                                <div id='budgetReportShowImage' style='display: none;'>
                                    <html:link href="javaScript:budgetDetails('5');">
                                        &nbsp;<html:img src="<%=plus%>" border="0"/></html:link>&nbsp;<bean:message bundle="proposal" key="summary.budgetReport"/>
                                </div>
                                <div id='budgetReportHideImage'>
                                    <html:link href="javaScript:budgetDetails('6');">
                                        &nbsp;<html:img src="<%=minus%>" border="0"/></html:link>&nbsp;<bean:message bundle="proposal" key="summary.budgetReport"/>
                                </div>
                            </td>
                            <td width='50%' align=right>
                                <html:link href="javaScript:budgetDetails('5');">
                                <div id='budgetReportShow' style='display: none;'>
                                    <bean:message bundle="proposal" key="summary.show"/>
                                </div>
                                </html:link>
                                <html:link href="javaScript:budgetDetails('6');">
                                <div id='budgetReportHide'>
                                    <bean:message bundle="proposal" key="summary.hide"/>
                                </div>
                                </html:link>
                            </td>
                        </tr>
                        </table>
                        </div>

                        <div id='budgetReportDetails'>
                        <table width="100%" align=center border="0" cellpadding="0" cellspacing="0">
                            <logic:present name="budgetReports" scope="request">
                                <tr>
                                    <td align=left class='copy' width="10%">
                                    </td>
                                    <td align=left class='copybold' width="25%"><bean:message key="budget.summary.reportName" bundle="budget"/>
                                    </td>
                                    <td class="copybold" align="center" width="65%"><bean:message key="budget.printSummary.comments" bundle="budget"/>
                                    </td>
                                </tr>
                                
                                <logic:iterate id="data" name="budgetReports" type="edu.mit.coeus.bean.CoeusReportGroupBean.Report" indexId="printIndex">
                        <%-- Do not display Budget Total and Industrial Cumulative budget --%>
                        <logic:notEqual name="data" property="id" value="ProposalBudget/budgettotalbyperiod" >
                            <logic:notEqual name="data" property="id" value="ProposalBudget/indsrlcumbudget" >
                                <tr>
                                    <td align=left class='copy'></td>
                                    <td align=left class='copy'>
                                    
                                    <%
                                    String id = data.getId();
                                    String printBudget = "javaScript:budgetPrintSummary('print','"+id+"','"+printIndex+"')";%>                                     
                                    <a href="<%=printBudget%>" >                                        
                                        <u><bean:write name="data" property="dispValue" /></u>
                                    </a>
                                    </td>
                                    <td class="copybold" align="center">
                                        <input type="checkbox" name="chkComments<%=printIndex%>">
                                    </td> 
                                    
                                </tr>
                            </logic:notEqual>
                        </logic:notEqual>

                        </logic:iterate>
                        </logic:present>
                        </table>
                        </div>
                </div>
            </td>
        </tr>
        <tr><td height='2'></td>
        </tr>
        </table>
    </td>
</tr>
</logic:notEmpty>

<logic:notEmpty name = "NarrativeInfo"  scope="request">
<tr valign=top>
    <td>
        <table width="98%" align=center  border="0" cellpadding="0" cellspacing="0" class="tabtable">
        <tr valign=top>
            <td>
                <table width="100%"  border="0" cellpadding="0" cellspacing="0" class="tableheader">
                <tr valign=top>
                    <td width='50%' align=left nowrap>
                        <div id='narrativeShowImage' style='display: none;'>
                            <html:link href="javaScript:summaryHeader('5');">
                            <html:img src="<%=plus%>" border="0"/></html:link>&nbsp;<bean:message bundle="proposal" key="summary.attachments"/>
                        </div>
                        <div id='narrativeHideImage'>
                            <html:link href="javaScript:summaryHeader('6');">
                            <html:img src="<%=minus%>" border="0"/></html:link>&nbsp;<bean:message bundle="proposal" key="summary.attachments"/>
                        </div>
                    </td>
                    <td width='50%' align=right>
                        <html:link href="javaScript:summaryHeader('5');">
                        <div id='narrativeShow' style='display: none;'>
                            <bean:message bundle="proposal" key="summary.show"/>
                        </div>
                        </html:link>
                        <html:link href="javaScript:summaryHeader('6');">
                        <div id='narrativeHide'>
                            <bean:message bundle="proposal" key="summary.hide"/>
                        </div>
                        </html:link>
                    </td>
                </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td height='2'>
            </td>
        </tr>
        <tr>
            <td>
                 <div id='narrativeDetails'>
               <%-- <table width="98%" align=left border="0" cellpadding="0" cellspacing="0">
                <tr>
                <td width='2%'>
                    &nbsp;
                </td>
                <td>--%>

                <table width="98%" align=center border="0" cellspacing="1" cellpadding="3" class='tabtable'>
                <tr>

                    <td width='30%' class='theader'>
                        <bean:message bundle="proposal" key="summary.type"/>
                    </td>
                    <td width='50%' class='theader'>
                        <bean:message bundle="proposal" key="summary.description"/>
                    </td>
                   <td width='20%' class='theader'>
                        &nbsp;
                   </td>
                </tr>
                <logic:iterate id="narrativeData" name="NarrativeInfo"  type="org.apache.commons.beanutils.DynaBean">
                <tr class='copy'>
                    <td width='30%' class='copy'>
                        <coeusUtils:formatOutput name="narrativeData" property="moduleTitle"/>
                    </td>
                    <td width='50%' class='copy'>
                        <coeusUtils:formatOutput name="narrativeData" property="moduleStatus"/>
                    </td>

                    <td width='20%' class="copy" align=center>
                    <logic:equal name="narrativeData" property='viewRight' value="1" >
                        <%--
                        <a href="<bean:write name='ctxtPath'/>/GetNarrativeDocument.do?proposalNumber=<bean:write name="narrativeData" property="proposalNumber" />&moduleNumber=<bean:write name="narrativeData" property="pdfModuleNumber" />&documentType=PDF">
                        <u>View</u>
                        </a>
                        --%>
                        <% String pageSet = "javaScript:documentProgress('V','" +narrativeData.get("proposalNumber") +"','" +narrativeData.get("moduleNumber") +"','');";%>
                        <html:link href = "<%=pageSet%>">
                            <bean:message bundle="proposal" key="upload.view"/>
                        </html:link>
                    </logic:equal>
                </td>
                </tr>
                </logic:iterate>
                </table>

                <%--</td>
                </tr>
                </table>--%>
                     </div>
            </td>
        </tr>
        <tr>
            <td height='2'>
            </td>
        </tr>
        </table>
    </td>
</tr>
</logic:notEmpty>

<!-- Special Review New STARTS---->


<%String proposalNo = (String)session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER+session.getId());
       if(proposalNo!= null){
          proposalNo = "["+"Proposal No. - "+ proposalNo+"]";
       }else{
          proposalNo = "";
       }
  String display =(String) request.getAttribute("display");
   mode=(String)session.getAttribute("mode"+session.getId());
   modeValue=false;
  if(mode!=null && !mode.equals("")) {
      if(mode.equalsIgnoreCase("display")){
              modeValue=true;
      }
  }%>




<!----~~~~~~~~~   SPECIAL REVIEW     S T A R T S----------->

<%--<logic:notEmpty name = "splReview" scope="request">--%>
<logic:notEmpty name = "pdReviewList" scope="session">
<tr valign=top>
    <td>
 <table width="98%" align=center  border="0" cellpadding="0" cellspacing="0" class="tabtable">
        <tr valign=top>
            <td>
                <table width="100%"  border="0" cellpadding="0" cellspacing="0" class="tableheader">
                <tr valign=top>
                    <td width='50%' align=left nowrap>
                        <div id='specialReviewShowImage' style='display: block;'> <!-- -->
                            <html:link href="javaScript:summaryHeader('7');">
                            <html:img src="<%=plus%>" border="0"/></html:link>&nbsp;<bean:message key="specialReviewLabel.SpecialReview" bundle="proposal"/>
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=proposalNo%></div>
                        <div id='specialReviewHideImage' style='display: none;'>
                            <html:link href="javaScript:summaryHeader('8');">
                            <html:img src="<%=minus%>" border="0"/></html:link>&nbsp;<bean:message key="specialReviewLabel.SpecialReview" bundle="proposal"/>
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=proposalNo%></div>
                    </td>


                    <td width='50%' align=right>
                        <html:link href="javaScript:summaryHeader('7');">
                        <div id='specialReviewShow' style='display: block;'>
                            <bean:message bundle="proposal" key="summary.show"/>
                        </div>
                        </html:link>
                        <html:link href="javaScript:summaryHeader('8');">
                        <div id='specialReviewHide'  style='display: none;'>
                            <bean:message bundle="proposal" key="summary.hide"/>
                        </div>
                        </html:link>
                    </td>
                </tr>
                </table>
            </td>

                 </tr>
        <tr>
            <td height='2'>
            </td>
        </tr>



                <tr>
                    <td align="center" valign="top">

                        <div id='ListReviewDetails' style='display: none;'>
                            <table width="98%"  border="0" cellspacing="1" cellpadding="3" class="tabtable" align="center">
                        <tr align="center" valign="top">
                            <td  align="left" class="theader"><bean:message key="specialReviewLabel.SpecialReview" bundle="proposal"/></td>
                            <td  align="left" class="theader"><bean:message key="specialReviewLabel.Approval" bundle="proposal"/></td>
                            <td  align="left" class="theader"><bean:message key="specialReviewLabel.ProtocolNo" bundle="proposal"/></td>
                            <td  align="left" class="theader"><bean:message key="proposalSpecialReview.applDate" bundle="proposal"/></td>
                            <td  align="left" class="theader"><bean:message key="proposalSpecialReview.apprvDate" bundle="proposal"/></td>
                            <td  align="left" class="theader"><bean:message key="specialReviewLabel.Comments" bundle="proposal"/></td>

                        </tr>
                         <%
                            String strBgColor1 = "#DCE5F1";
                             int count=0;
                            %>

                         <logic:present name="pdReviewList">
                         <logic:iterate id="pdreview" indexId="index" name="pdReviewList" type = "org.apache.struts.validator.DynaValidatorForm">
                         <% String protoNum = "";
                            if(pdreview.get("spRevProtocolNumber") == null){
                               protoNum = "";
                            }else{
                               protoNum = pdreview.get("spRevProtocolNumber").toString();
                            }
                          %>
                          <%
                             if (count%2 == 0)
                                strBgColor1 = "#D6DCE5";
                             else
                                strBgColor1="#DCE5F1";
                           %>
                        <tr bgcolor="<%=strBgColor1%>" onmouseover="className='TableItemOn'" onmouseout="className='TableItemOff'">
                            <td class="copy">
                                 <%=pdreview.get("specialReviewDescription").toString()%>
                            </td>
                            <td class="copy">
                                <%=pdreview.get("approvalDescription").toString()%>
                            </td>
                            <td class="copy">
                                 <%=protoNum%>
                            </td>
                            <td class="copy">
                                 <%String applDate = "";
                                 if(pdreview.get("applicationDate") == null){
                                   applDate = "";
                                 }else{
                                   applDate = (String)pdreview.get("applicationDate");
                                 }
                                %>
                               <%=applDate%>
                            </td>
                            <td class="copy">
                                <%String apprvDate = "";
                                 if(pdreview.get("approvalDate") == null){
                                    apprvDate = "";
                                 }else{
                                    apprvDate = (String)pdreview.get("approvalDate");
                                 }
                                %>
                               <%=apprvDate%>
                            </td>
                            <td class="copy">
                                  <%String comments = "";
                                 if(pdreview.get("comments") == null){
                                    comments = "";
                                 }else{
                                    comments = (String)pdreview.get("comments");
                                 }
                               %>
                                <%
                                    String viewLink = "javascript:view_comments('" +index+"')";
                                %>
                                <html:link href="<%=viewLink%>">
                                    <bean:message key="label.View"/>
                                </html:link>
                            </td>

                             <% count++;%>

                        </tr>

                        </logic:iterate>

                        </table>
                        </div>

                        </logic:present>
                    </td>
               </tr>
               <tr><td height="2"></td></tr>
</table>
</td>
</tr>
</logic:notEmpty>
<!----~~~~~~~~~   SPECIAL REVIEW       E N D S----------->

               <!-- questionnaire starts-->
               <logic:present name="questionAnsMap">
                <tr valign=top>
                <td>
                 <table width="98%" align=center  border="0" cellpadding="0" cellspacing="0" class="tabtable">
                <tr>
                    <td valign="top">
                        <table width="100%"  border="0" cellpadding="0" cellspacing="0" class="tableheader">
                            <tr>
                                <td width='50%' align=left nowrap valign="top">
                                    <div id='QuestionShowImage' style='display: block;'>
                                        <html:link href="javaScript:summaryHeader('23');">
                                            <html:img src="<%=plus%>" border="0"/></html:link>&nbsp;Questionnaire
                                            &nbsp;</div>
                                        <div id='QuestiontHideImage' style='display: none;'>
                                        <html:link href="javaScript:summaryHeader('24');">
                                            <html:img src="<%=minus%>" border="0"/></html:link>&nbsp;Questionnaire
                                        </div>
                                    </td>



                                    <td width='50%' align=right valign="top">
                                    <html:link href="javaScript:summaryHeader('23');">
                                        <div id='QuestionShow' style='display: block;'>
                                            <bean:message bundle="proposal" key="summary.show"/>
                                        </div>
                                    </html:link>
                                    <html:link href="javaScript:summaryHeader('24');">
                                        <div id='QuestionHide' style='display: none;'>
                                            <bean:message bundle="proposal" key="summary.hide"/>
                                        </div>
                                    </html:link>
                                </td>
                            </tr>
                        </table>
                    </td>

                </tr>
                <tr><td height='2'></td></tr>

                <tr><td valign="top">
                        <div id="qnrmainDiv" style='display: none;'>

                            <%
                                        int divIndex = 0;
                                        Map questMap = (HashMap) request.getAttribute("questionAnsMap");
                                        Set keys = new HashSet();
                                        Vector answerBeans = new Vector();
                                        if (questMap != null) {

                                            keys = questMap.keySet();
                                        }
                                        for (Iterator it = keys.iterator(); it.hasNext();) {
                                            String questionaireName = (String) it.next();
                                            answerBeans = (Vector) questMap.get(questionaireName);
                                            divIndex++;
                                            //QuestionAnswerProposalSummaryBean qnrBean = (QuestionAnswerProposalSummaryBean) it.next();

                                            //qnrId = qnrBean.getQuestionnaireId();

                            %>

                            <table width="98%" border="0" cellpadding="0" cellspacing="0" class="tabtable" align="center">
                                <tr>
                                    <td colspan="4" valign="top">
                                        <div id='budgetReport'>
                                            <table width="100%" align=center border="0" cellpadding="0" cellspacing="0" class='theader'>
                                                <tr>

                                                    <td width='50%' align=left nowrap valign="top">
                                                        <div id='QnrShowImage<%=divIndex%>' style='display: block;'>
                                                            <a href="javaScript:dynamicQuestionsDivShow('Inner<%= divIndex%>','QnrShowImage<%= divIndex%>','QnrShow<%= divIndex%>','QnrHide<%= divIndex%>','QnrHideImage<%= divIndex%>');">
                                                                &nbsp;<html:img src="<%=plus%>" border="0" /></a>&nbsp;<%= questionaireName%>
                                                        </div>
                                                        <div id='QnrHideImage<%=divIndex%>'style='display: none;'>
                                                            <a href="javaScript:dynamicQuestionsDivHide('Inner<%= divIndex%>','QnrShowImage<%= divIndex%>','QnrShow<%= divIndex%>','QnrHide<%= divIndex%>','QnrHideImage<%= divIndex%>');">
                                                                &nbsp;<html:img src="<%=minus%>" border="0"/></a>&nbsp;<%= questionaireName%>
                                                        </div>
                                                    </td>

                                                    <td width='50%' align=right valign="top">
                                                        <a href="javaScript:dynamicQuestionsDivShow('Inner<%= divIndex%>','QnrShowImage<%= divIndex%>','QnrShow<%= divIndex%>','QnrHide<%= divIndex%>','QnrHideImage<%= divIndex%>');">
                                                            <div id='QnrShow<%=divIndex%>' style='display: block;'>
                                                                <bean:message bundle="proposal" key="summary.show"/>
                                                            </div>
                                                        </a>
                                                        <a href="javaScript:dynamicQuestionsDivHide('Inner<%= divIndex%>','QnrShowImage<%= divIndex%>','QnrShow<%= divIndex%>','QnrHide<%= divIndex%>','QnrHideImage<%= divIndex%>');">
                                                            <div id='QnrHide<%=divIndex%>' style='display: none;'>
                                                                <bean:message bundle="proposal" key="summary.hide"/>
                                                            </div>
                                                        </a>
                                                    </td>
                                                </tr>
                                            </table>
                                        </div>
                                    </td>
                                </tr>

                                <tr><td valign="top"><div id="Inner<%=divIndex%>" style='display: none;'>
                                            <table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size: 12px;" >





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
                                                                                                // vrsn = questBean.getVersionnumber();
                                                                                                //qvrsn = Integer.parseInt(questBean.getVersionnumber());

                                                                                                qstid++;
                                                %>

                                                <tr><td colspan="4" valign="top"></td></tr>
                                                <tr>
                                                    <td width="30" valign="top">
                                                    </td>
                                                    <td width="30"  valign="top">
                                                        <%=qstid%> )


                                                    </td>
                                                    <td  valign="top">

                                                        <div><%= question%>
                                                        </div>

                                                    </td>
                                                    <td class='copybold' align=left width="15%"  valign="top">
                                                        <%String queLink = "javascript:showQuestion('/showQuestionExplanation.do?questionNo=" + questid + "&questionDesc=" + question + "')";%>
                                                        <html:link href="<%=queLink%>">
                                                            more
                                                        </html:link>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td width="30"  valign="top">
                                                    </td>
                                                    <td width="30"  valign="top">
                                                    </td>

                                                    <td  valign="top">
                                                        <%   if (questBean.getValidAnswers().equalsIgnoreCase("YN")) {
                                                                                if (questBean.getAnswer().equalsIgnoreCase("Y")) {

                                                        %>
                                                        <input type="radio" id="question" value="Y" checked="true" disabled="true"/>Yes
                                                        &nbsp;
                                                        <input type="radio" id="question" value="N" disabled="true"/>No
                                                        <% }
                                   if (!questBean.getAnswer().equalsIgnoreCase("Y")) {%>
                                                        <input type="radio" id="question" value="Y"  disabled="true"/>Yes
                                                        &nbsp;
                                                        <input type="radio" id="question" value="N" checked="true" disabled="true"/>No
                                                        <% }
                                                                            }
                                                                            if (questBean.getValidAnswers().equalsIgnoreCase("YNX")) {
                                                                                if (questBean.getAnswer().equalsIgnoreCase("Y")) {
                                                        %>
                                                        <input type="radio" id="question" value="Y" checked="true" disabled="true"/>Yes
                                                        &nbsp;
                                                        <input type="radio" id="question" value="N" disabled="true"/>No
                                                        &nbsp;
                                                        <input type="radio" id="question" value="X" disabled="true"/>N/A
                                                        &nbsp;
                                                        <% }
                                                            if (questBean.getAnswer().equalsIgnoreCase("N")) {%>

                                                        <input type="radio" id="question" value="Y"  disabled="true"/>Yes
                                                        &nbsp;
                                                        <input type="radio" id="question" value="N" checked="true" disabled="true"/>No
                                                        &nbsp;
                                                        <input type="radio" id="question" value="X"  disabled="true"/>N/A
                                                        &nbsp;

                                                        <% }
                                                            if (questBean.getAnswer().equalsIgnoreCase("X")) {%>
                                                        <input type="radio" id="question" value="Y"  disabled="true"/>Yes
                                                        &nbsp;
                                                        <input type="radio" id="question" value="N" disabled="true"/>No
                                                        &nbsp;
                                                        <input type="radio" id="question" value="X" checked="true" disabled="true"/>N/A
                                                        <% }
                                                                            }
                                                                            if (questBean.getValidAnswers().equalsIgnoreCase("Text")) {
                                                                                String Answr = questBean.getAnswer();
                                                        %>

                                                        <input type="text" id="question"  size="20" maxlength="10" value="<%=Answr%>" disabled="true" />

                                                        <% }
                                                                            if (questBean.getValidAnswers().equalsIgnoreCase("Search")) {
                                                                                String Answrsrch = questBean.getAnswer();
                                                        %>
                                                        <input type="text" id="question"  size="20" maxlength="10" value="<%=Answrsrch%>" disabled="true" />
                                                        <% }%>


                                                    </td>

                                                </tr>

                                                <%
                                                      }
                                                %>

                                                <tr colspan="4"><td  valign="top"></td></tr>
                                                <tr align="left" class='table'>

                                                    <td class='savebutton' colspan="4" nowrap style="padding-top: 3px;padding-left: 5px;padding-bottom: 3px;"  valign="top">


                                                        <%String printbtn = request.getContextPath() + "/coeusliteimages/print-button.gif";%>
                                                        <a  href="javascript:view_data('<%=qnrInt%>')"><img style="border-style: none;" alt="print" src="<%=printbtn%>"/></a>


                                                    </td>

                                                </tr>
                                            </table>
                                        </div></td></tr>
                                  <tr><td height="2"></td></tr>
                            </table>


                            <%
                                        }

                            %>



                        </div>
                    </td>
                </tr>
    <tr><td height="2"></td></tr>
            </table>
           </td></tr>
        </logic:present>


<%--</table>--%>

        </html:form>
        </html:form>

 <%--<tr> <td> &nbsp; </td></tr>--%>
<!--=================questionnaire E N D S =============== -->
<%--<table align="center" border="0" cellspacing="0" cellpadding="3" width="100%" class="table">--%>
            <%--<tbody>--%>

          <%! int i=1,j=1 ;%>

          <logic:empty name="grantsGov" property="list" scope="request">
              <%  i=0;%>
          </logic:empty>

           <logic:empty name="proposalPrint" property="list" scope="request">
               <% j=0 ; %>
           </logic:empty>
          <%
          if((i==1)||(j==1)){
          %>
<tr valign="top">
    <td>
         <table width="98%" align=center  border="0" cellpadding="0" cellspacing="0" class="tabtable">
                <%--<html:form action="proposalPrintAction" target="_blank" onsubmit="return validateProposalPrint()">--%>
                     <tr valign=top>
            <td>
                <table width="100%"  border="0" cellpadding="0" cellspacing="0" class="tableheader">
                <tr valign=top>
                    <td width='50%' align=left nowrap>
                        <div id='PrintShowImage' style='display: block;'>
                            <html:link href="javaScript:summaryHeader('21');">
                            <html:img src="<%=plus%>" border="0"/></html:link>&nbsp;Proposal Print
                        &nbsp;</div>
                        <div id='PrintHideImage' style='display: none;'>
                            <html:link href="javaScript:summaryHeader('22');">
                            <html:img src="<%=minus%>" border="0"/></html:link>&nbsp;Proposal Print
                       </div>
                    </td>



                    <td width='50%' align=right>
                        <html:link href="javaScript:summaryHeader('21');">
                        <div id='PrintShow' style='display: block;'>
                            <bean:message bundle="proposal" key="summary.show"/>
                        </div>
                        </html:link>
                        <html:link href="javaScript:summaryHeader('22');">
                        <div id='PrintHide' style='display: none;'>
                            <bean:message bundle="proposal" key="summary.hide"/>
                        </div>
                        </html:link>
                    </td>
                </tr>
                </table>
            </td>

                 </tr>

<tr><td height="2"></td></tr>
                    <%--govt starts--%>

                     <tr>
                    <td>

                        <table width="98%" align=center border="0" cellpadding="0" cellspacing="0" >

                            <tr>
                    <td>
 <div id='Gov' style='display: none;'>
                        <table width="100%" align=center border="0" cellpadding="0" cellspacing="0" class='theader'>
                            <tr class="theader">
                                <!-- Form Details Header - START-->
                                <td width='50%' align=left nowrap="">
                                <div id='GovShowImage' style='display: block;'>
                                    <html:link href="javaScript:Govt('1');">
                                        &nbsp;<html:img src="<%=plus%>" border="0"/></html:link>&nbsp;Grants.Gov
                                </div>
                                <div id='GovHideImage' style='display: none;'>
                                    <html:link href="javaScript:Govt('2');">
                                        &nbsp;<html:img src="<%=minus%>" border="0"/></html:link>&nbsp;Grants.Gov
                                </div>
                            </td>
                            <td width='50%' align=right>
                                <html:link href="javaScript:Govt('1');">
                                <div id='GovShow' style='display: block;'>
                                    <bean:message bundle="proposal" key="summary.show"/>
                                </div>
                                </html:link>
                                <html:link href="javaScript:Govt('2');">
                                <div id='GovHide' style='display: block;'>
                                    <bean:message bundle="proposal" key="summary.hide"/>
                                </div>
                                </html:link>
                            </td>
                                <!-- Form Details Header - END-->
                            </tr>
                             </table>
                        </div>
                    </td>
                </tr>
<tr>
       <td align="center" valign="top">
                        <div id='govDetails' style='display: none;'>
                        <table width="100%" align="center"  border="0" cellpadding="0" cellspacing="0">
                            
                            <logic:notEmpty name="grantsGov" property="list" scope="request">
 <tr><td>
                            <html:form action="grantsGovAction">
                                <logic:iterate id="simpleBean" name="grantsGov" property="beanList" type="org.apache.struts.action.DynaActionForm" indexId="ctr">
                                <table width="100%" align="center"  border="0" cellpadding="0" cellspacing="0">
                                    <tr class="rowLine">
                                                    <td  class='copybold' width="10%"></td>
                                                    <td>
                                    <html:hidden property="submitType" name="simpleBean" indexed="true"/>
                                    <html:hidden property="submissionTypeCode" name="simpleBean" indexed="true"/>
                                    <html:hidden property="opportunityId" name="simpleBean" indexed="true"/>
                                    <html:hidden property="proposalNumber" name="simpleBean" indexed="true"/>
                                    <html:hidden property="opportunityTitle" name="simpleBean" indexed="true"/>
                                    <html:hidden property="cfdaNumber" name="simpleBean" indexed="true"/>
                                    <html:hidden property="competitionId" name="simpleBean" indexed="true"/>
                                                     </td>
                                     </tr>
                                  </table>
                                </logic:iterate>
                           
                                    <div id="forms" style="overflow:hidden">
                                        <table align="center" width="100%" border="0" cellspacing="0" cellpadding="2" class="tabtable">
                                            <tr class="rowLine">
                                                <td colspan="2">Select : <a href="javascript:selectDeselectAll(true)">All</a> | <a href="javascript:selectDeselectAll(false)">None</a></td>
                                                <!--<td>Form Name</td>-->
                                            </tr>
                                            <logic:iterate id="listBean" name="grantsGov" property="list" type="org.apache.struts.action.DynaActionForm" indexId="ctr">
                                                <tr class="rowLine">
                                                    <td  class='copybold' width="10%">&nbsp;</td>
                                                    <td>
                                                        <%
                                                                String strInclude = "false";
                                                                Boolean boolInclude = (Boolean)listBean.get("include");
                                                                if(boolInclude != null && boolInclude.booleanValue()) {
                                                                    strInclude = "true";
                                                                }
                                                        %>
                                                        <html:checkbox property="print" name="listBean" indexed="true"/>
                                                        <html:hidden property="proposalNumber" name="listBean" indexed="true"/>
                                                        <html:hidden property="includeSelected" name="listBean" indexed="true" value="<%=strInclude%>"/>
                                                        <html:hidden property="ns" name="listBean" indexed="true"/>
                                                        <html:hidden property="schUrl" name="listBean" indexed="true"/>
                                                        <html:hidden property="formName" name="listBean" indexed="true"/>
                                                        <html:hidden property="acType" name="listBean" indexed="true"/>
                                                    <!--</td>
                                                    <td>-->
                                                        <bean:write name="listBean" property="formName"/>
                                                    </td>
                                                </tr>
                                            </logic:iterate>
                                            <tr class="rowLine">
                                                <td  class='copybold' width="10%"></td>
                                                <td align="left">
                                                    <input type="button" value="Print Selected" onclick="printForms()"/>
                                                </td>
                                            </tr>
                                        </table>
                                    </div>
                            </html:form>
     </td></tr>
                            </logic:notEmpty>
                            <logic:empty name="grantsGov" property="list" scope="request">
                            <tr><td>
                            <div id="forms" style="overflow:hidden">
                                <table align="center" width="100%" border="0" cellspacing="0" cellpadding="2" class="tabtable">
                                <tr><td>Grants.Gov Forms Not Available.</td></tr>
                                </table>
                            </div>
                            </td></tr>
                            </logic:empty>
                            </table>


                      </div>


                    </td>
               </tr>
               <%--gov content stops--%>
                   </table>

                    </td>
                    </tr>
        <tr><td height="2"></td></tr>
          </tr><%--govt stops--%>
<html:form action="proposalPrintAction" target="_blank" onsubmit="return validateProposalPrint()">


                    <script>
                        var packageName = new Array();
                        var sponsorCode
                        <logic:iterate id="simpleBean" name="proposalPrint" property="beanList" type="edu.mit.coeus.sponsormaint.bean.SponsorFormsBean" indexId="ctr">
                            packageName[<%=ctr%>] = "<bean:write name="simpleBean" property="packageName"/>";
                        </logic:iterate>
                    </script>
 <tr>
                    <td>

                        <table width="98%" align=center border="0" cellpadding="0" cellspacing="0" >

                            <tr>
                    <td>
 <div id='Sponsor' style='display: none;'>
                        <table width="100%" align=center border="0" cellpadding="0" cellspacing="0" class='theader'>
                        <tr>
                            <td width='50%' align=left nowrap="">
                                <div id='SponsorShowImage' style='display: block;'>
                                    <html:link href="javaScript:SponsorPrint('1');">
                                        &nbsp;<html:img src="<%=plus%>" border="0"/></html:link>&nbsp;Sponsor Form Packages
                                </div>
                                <div id='SponsorHideImage' style='display: none;'>
                                    <html:link href="javaScript:SponsorPrint('2');">
                                        &nbsp;<html:img src="<%=minus%>" border="0"/></html:link>&nbsp;Sponsor Form Packages
                                </div>
                            </td>
                            <td width='50%' align=right>
                                <html:link href="javaScript:SponsorPrint('1');">
                                <div id='SponsorShow' style='display: block;'>
                                    <bean:message bundle="proposal" key="summary.show"/>
                                </div>
                                </html:link>
                                <html:link href="javaScript:SponsorPrint('2');">
                                <div id='SponsorHide' style='display: none;'>
                                    <bean:message bundle="proposal" key="summary.hide"/>
                                </div>
                                </html:link>
                            </td>
                        </tr>
                        </table>
                        </div>
                    </td>
                </tr>
<tr>
       <td align="center" valign="top">
                        <div id='sponsorDetails' style='display: none;'>
                        <table width="100%" align="center"  border="0" cellpadding="0" cellspacing="0">
                        <!-- Form Details - START -->

                        <% int prevPackageNumber = -1, packageNumber = -1, packageIndex = 0;
                            String prevSponsorCode = "", sponsorCode = "";
                        %>
                        <logic:iterate id="listBean" name="proposalPrint" property="list" type="org.apache.struts.action.DynaActionForm" indexId="ctr">
                            <%
                                packageNumber = ((Integer)listBean.get("packageNumber")).intValue();
                                sponsorCode =  (String)listBean.get("sponsorCode");
                                if(prevPackageNumber != packageNumber || !prevSponsorCode.equals(sponsorCode)) {
                                    if(prevPackageNumber != -1){
                                        //new package.%>
                            <script>

                                formDetails[packageIndex] = formPrintElement;
                                packageIndex = packageIndex + 1;
                            </script>
                            <%
                                packageIndex = packageIndex + 1;
                                    }
                                    if(packageIndex > 0){
                                        //end last package.%>
                                      </table></td></tr>
                                    <%}%>

                         <%--<div id="sponsor" style="overflow:hidden">--%>

                            <tr class="rowLine" id="row<%=packageIndex%>" onmouseover="rowHover('row<%=packageIndex%>','rowHover rowLine')" onmouseout="rowHover('row<%=packageIndex%>','rowLine')" onclick="selectRow('row<%=packageIndex%>', 'div<%=sponsorCode+packageNumber%>', '<%=sponsorCode+packageNumber%>', 'toggle<%=packageIndex%>', '<%=packageIndex%>')">
                                <td  class='copybold' width="10%"></td>
                                <td class="copybold">
                                    <script>document.write(packageName[packageIndex]);</script>
                                </td>
                                <td align="right" width="5%" class="copybold"><a href="javascript:doNothing()" id="toggle<%=packageIndex%>">Show</a></td>
                            </tr>

                            <tr id="<%=sponsorCode+packageNumber%>" style="display:none;">
                               <td  class='copybold' width="10%"></td>
                               <td colspan="2">
                            <div id="div<%=sponsorCode+packageNumber%>" style="overflow:hidden">
                            <table width="100%" border="0" cellspacing="0" cellpadding="0" class="copy rowHover">
                            <script>
                                var formPrintElement = new Array();
                                var formIndex = 0;
                            </script>
                             <tr class="copy rowHover">
                                <td>Select : <a href="javascript:resetCheckBoxes('<%=packageIndex%>', true)">All</a> | <a href="javascript:resetCheckBoxes('<%=packageIndex%>', false)">None</a> </td>
                             </tr>
                            <%
                                prevPackageNumber = packageNumber;
                                prevSponsorCode = sponsorCode;
                                }%>
                                <script>

                                    </script>
                            <tr class="copy rowHover">
                                <td>
                                    <html:checkbox property="print" name= "listBean" indexed="true"/>
                                    <script>
                                        formPrintElement[formIndex] = "listBean[<%=ctr%>].print";
                                        formIndex = formIndex + 1;
                                    </script>
                                    <bean:write property="pageDescription" name="listBean"/>
                                    <html:hidden property="rowId" name="listBean" indexed="true"/>
                                    <html:hidden property="sponsorCode" name="listBean" indexed="true"/>
                                    <html:hidden property="pageDescription" name="listBean" indexed="true"/>
                                    <html:hidden property="packageNumber" name="listBean" indexed="true"/>
                                    <html:hidden property="pageNumber" name="listBean" indexed="true"/>
                                </td>
                            </tr>

                        </logic:iterate>
                        <%if(prevPackageNumber != -1 && packageNumber != -1 ){%>
                            <script>
                                //Last Package Details
                                formDetails[packageIndex] = formPrintElement;
                            </script>
                           </table>
                            </div>

                            </td></tr>
                                                                             <%-- </div>--%>
                            <tr>
                                <td  class='copybold' width="10%"></td>
                                <td align="left" colspan="2">
                                    <html:submit value="Print Selected"/>
                                </td>
                            </tr>
                            <%}else{%>
                            <tr><td colspan="3">
                            <table align="center" width="100%" border="0" cellspacing="0" cellpadding="2" class="tabtable">
                                <tr><td>
                                    No Packages Found.
                                </td>
                            </tr>
                            </table>
                        </td>
                        </tr>
                            <%}%>
                         </table>

                       </div>

                    </td>
               </tr>
                    </html:form>        <!-- Form Details - END -->
                        </table>
                    </td>
                    </tr>
<tr><td height="2"></td></tr>


                    <%-- S2S Forms - START --%>

                    <%-- S2S Forms - END --%>



        </table>
</td>
</tr>
<% }  %>
<!--------Questionnaire Details  Div Hiding script starts......Athul------->
<tr>
    <td valign="top">

    </td>
</tr>

<!--------Questionnaire Details  Div Hiding script Ends......Athul------->
<tr>

</tr>
</table>
</body>
</html:html>
 