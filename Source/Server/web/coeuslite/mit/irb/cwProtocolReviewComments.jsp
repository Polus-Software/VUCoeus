<%@ page contentType="text/html;charset=UTF-8" language="java"
	import="edu.mit.coeuslite.utils.CoeusLiteConstants,
edu.mit.coeuslite.utils.DateUtils,edu.mit.coeus.irb.bean.ProtocolSubmissionInfoBean
,edu.mit.coeus.bean.UserInfoBean,edu.mit.coeus.user.bean.UserMaintDataTxnBean,java.util.Vector,java.util.HashMap,edu.mit.coeuslite.irb.bean.ProtocolHeaderDetailsBean"%>
<%--end2--%>
<%@page import="org.apache.struts.taglib.html.JavascriptValidatorTag"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<jsp:useBean id="contingencyList" scope="request"
	class="java.util.Vector" />
<jsp:useBean id="myReviewComments" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="otherReviewerReviewComments" scope="session"
	class="java.util.Vector" />
<%-- Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments--%>
<jsp:useBean id="myReviewAttachments" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="otherReviewerReviewAttachments" scope="session"
	class="java.util.Vector" />
<%-- Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments--%>
<jsp:useBean id="recommendedActionList" scope="request"
	class="java.util.Vector" />

<%--Added for reviewcomments for pastSubmission 
<jsp:useBean id = "pastSubmissionComments" scope="request" class="java.util.Vector" />--%>

<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%
String DEFAULT_SCHEDULE_ID = "9999999999";
String minuteEntry = "";
String contingencyCode = "";
String removeLink = "" ;
Integer entryNumber ;
String loggedInPersonId = ""; 
String personId = "";
String personFullName = "";
String reviewerName = "";
// Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start
String attachmentDescription = "";
String otherRevAttachmntDescription = "";
String removeAttachment = "" ;
// Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end
boolean isEditLinkEnabled = false;
ProtocolSubmissionInfoBean protocolSubmissionInfoBean = (ProtocolSubmissionInfoBean)session.getAttribute("protocolSubmissionInfoBean");

//Modified for COEUSDEV-236 : PI can't see protocol he/she created - Start
//UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
//loggedInPersonId = userInfoBean.getPersonId();
loggedInPersonId = (String)request.getAttribute("userPersonId");
//COEUSDEV-236 : END

//Added for COEUSQA-2290 : New Minute entry type for Review Comments - Start
String minuteTypeCode = (String)request.getAttribute("minuteTypeCode");
//COEUSQA-2290 : End

// COEUSDEV-161: Changes to IRB reviewer comments functionality - Access peermissions - Start
// String userHasAdminRole =(String)session.getAttribute("userHasIRBAdminRole");
String userHasAdminRole =(String)request.getAttribute("userHasIRBAdminRole");
String userIsReviewer =(String) session.getAttribute(CoeusLiteConstants.USER_IS_REVIEWER+session.getId());
// COEUSDEV-161: Changes to IRB reviewer comments functionality - Access peermissions - End
//Added for  COEUSDEV-237 : Investigator cannot see review comments - Start
Boolean canUserCreateReviewComments = (Boolean)request.getAttribute("CAN_USER_CREATE_REVIEW_COMMNENTS");
//Commented for  COEUSDEV-303 : Review View menu items are not enabled if the user has reviewer role - Start
//Boolean canUserViewPastSubmissionComments = (Boolean)request.getAttribute("CAN_VIEW_PAST_SUBMISSION_COMMENTS");
//boolean canViewPastSubmissionComments = false;
//COEUSDEV-303 : End
boolean isUserCanAddComments = false;
if(canUserCreateReviewComments != null){
    isUserCanAddComments = canUserCreateReviewComments.booleanValue();
}
//Commented for  COEUSDEV-303 : Review View menu items are not enabled if the user has reviewer role - Start
//if(canUserViewPastSubmissionComments != null){
  //  canViewPastSubmissionComments = canUserViewPastSubmissionComments.booleanValue();
//}
//COEUSDEV-303 : End
//COEUSDEV-237 : End

//Added for COEUSQA-2288 : Ability for Reviewer to mark Review Comments complete and make recommendation - Start
java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("MM/dd/yyyy hh:mm a");
Boolean canCompleteReview = (Boolean)request.getAttribute("canCompleteReview");
if(canCompleteReview == null){
    canCompleteReview = false;
}
Boolean isAllReviewCommentsFinal = (Boolean)request.getAttribute("isAllReivewCommentsFinal");
if(isAllReviewCommentsFinal == null){
    isAllReviewCommentsFinal = false;
}
Boolean canViewUpdateDetails = (Boolean)request.getAttribute("canViewUpdateDetails");
if(canViewUpdateDetails == null){
    canViewUpdateDetails = false;
}
boolean modifyCommentsMode = false;
//COEUSQA-2288 : End

String scheduleId = "";
String protocolNumber = "";
int sequenceNumber = -1;
int submissionNumber = -1;

if(protocolSubmissionInfoBean != null){
    scheduleId = protocolSubmissionInfoBean.getScheduleId();
    protocolNumber = protocolSubmissionInfoBean.getProtocolNumber();
    sequenceNumber = protocolSubmissionInfoBean.getSequenceNumber();
    submissionNumber = protocolSubmissionInfoBean.getSubmissionNumber();
}
//Added for  COEUSDEV-237 : Investigator cannot see review comments - Start
if(protocolSubmissionInfoBean != null && protocolSubmissionInfoBean.getScheduleId() == null){
    scheduleId = DEFAULT_SCHEDULE_ID;
}
//COEUSDEV-237 : End

String showContingencyCodes = (String) request.getAttribute("showContingencCode");

String inViewMode = (String) request.getAttribute("inViewMode");

boolean notEditable = false;
if("Y".equalsIgnoreCase(inViewMode)){
    notEditable = true;
}
boolean isCommentNotEditable = notEditable;

String show = (String)request.getAttribute("show");
String showModify = (String)request.getAttribute("setModifyFlag");
//Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start
String showComment = (String)request.getAttribute("showComment");
//Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end
String isPreDefinedComments = (String)request.getParameter("showCodes");
String isPredefinedCommentSelected = (String)request.getAttribute("selectedPredefinedComment");
String continceyCommentPresent = (String) request.getAttribute("contingencyComment");
if("Y".equalsIgnoreCase(continceyCommentPresent)){
    if(showModify != null && showModify.equals("show")){
        isCommentNotEditable = false;
    }
    if("Y".equalsIgnoreCase(isPredefinedCommentSelected)){
            isCommentNotEditable = true;
    }
}
//Added for the case#3282/3284-reviewer views and comments-start
  //String show = (String)request.getAttribute("show");
  //String showModify = (String)request.getAttribute("setModifyFlag");
  boolean isViewPastSubmissionComments=false;
  
  HashMap mapGetPersonName =(HashMap) request.getAttribute("mapPersonName");
  // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start
  HashMap mapAttachPersonName =(HashMap) request.getAttribute("mapAttachPersonName");
  // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end
  ProtocolHeaderDetailsBean  protocolHeaderDetailsBean=(ProtocolHeaderDetailsBean) session.getAttribute("protocolHeaderBean");
  int protocolStatusCode=protocolHeaderDetailsBean.getProtocolStatusCode();
  String isFromReviewer = (String)session.getAttribute("isFromReviewer");
 
  // COEUSDEV-161: Changes to IRB reviewer comments functionality - Access peermissions - Start
  String strModifyComment = (String) request.getAttribute("canModifyComment");
  if(CoeusLiteConstants.YES.equalsIgnoreCase(strModifyComment)){
      isEditLinkEnabled = true;
  }
  // COEUSDEV-161: Changes to IRB reviewer comments functionality - Access peermissions - End
%>


<html:html>
<head>
<style>
.textbox {
	width: 40px;
}

.textbox-longer {
	width: 645px;
}

.errorwarning {
	color: red;
}

.errorwarning li {
	list-style: none;
}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Coeus Web</title>
<script>
       var errValue = false;

      function validateForm(type){//Modified for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments
        var val = document.reviewCommentsForm.acType.value;
        if(val!='U'){
            document.reviewCommentsForm.acType.value = 'I';
            // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start
            if( type == 'C'){
                document.reviewCommentsForm.entryTypeCode.value = <%=minuteTypeCode%>;
            }
            // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end
        }
            
            document.reviewCommentsForm.protocolNumber.value = '<%=protocolNumber%>';
            document.reviewCommentsForm.sequenceNumber.value = '<%=sequenceNumber%>';   
            document.reviewCommentsForm.submissionNumber.value = '<%=submissionNumber%>';    
            <%-- Modified for COEUSQA-2290 : New Minute entry type for Review Comments - Start 
            Changed the Entry Type Code from 3 to code from the parameter(IRB_MINUTE_TYPE_REVIEWER_COMMENT)
            document.reviewCommentsForm.entryTypeCode.value = 3;--%>
            
            <%--COEUSQA-2290 : End--%>            
            //Modified for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start
            if( type == 'C'){
                if(trim(document.reviewCommentsForm.minuteEntry.value) == ''){
                    alert('Please enter Review Comment');
                } else {
                    document.reviewCommentsForm.action = "<%=request.getContextPath()%>/saveReviewComments.do";
                    document.reviewCommentsForm.save.disabled = true;
                    document.reviewCommentsForm.submit();
                }
            }else if( type == 'A'){
                if(trim(document.reviewCommentsForm.reviewDocDescription.value) == ''){
                     alert('Please enter Attachment Description');
                }else if(trim(document.reviewCommentsForm.reviewFileName.value) == ''){
                     alert('Please select an Attachment');
                }else if(trim(document.reviewCommentsForm.reviewFileName.value) != ''   &&  trim(document.reviewCommentsForm.reviewDocDescription.value) == ''){
                    alert('Please enter Attachment Description');
                } else if(trim(document.reviewCommentsForm.reviewDocDescription.value) != '' &&  trim(document.reviewCommentsForm.reviewFileName.value) == ''){
                    alert('Please select an Attachment');
                } else{
                    document.reviewCommentsForm.action = "<%=request.getContextPath()%>/saveReviewComments.do";
                    document.reviewCommentsForm.save.disabled = true;
                    document.reviewCommentsForm.submit();
                }
                
                
            }
            //Modified for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start_end
            
        }

      function view_comments(entryNumber) {
            document.reviewCommentsForm.entryNumber.value = entryNumber;
            document.reviewCommentsForm.scheduleId.value = '<%=scheduleId%>';
            document.reviewCommentsForm.action = "<%=request.getContextPath()%>/getReviewComments.do?getMinutes=Y&editable=N";
            document.reviewCommentsForm.submit();      
     }  
            
     function edit_data(entryNumber, entryTypeCode, awUpdateTimestamp, personId, scheduleId){
        
            if(validate() == true){
                // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start
                document.reviewCommentsForm.actionPerformedFrom.value = 'C';
                // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end
                document.reviewCommentsForm.awUpdateTimestamp.value = awUpdateTimestamp;
                document.reviewCommentsForm.awScheduleId.value = scheduleId;
                document.reviewCommentsForm.awEntryNumber.value = entryNumber;
                document.reviewCommentsForm.personId.value = personId;
            
                document.reviewCommentsForm.acType.value = 'U';
                document.reviewCommentsForm.scheduleId.value = scheduleId;
                document.reviewCommentsForm.entryNumber.value = entryNumber;
                <%-- Modified for COEUSQA-2290 : New Minute entry type for Review Comments - Start 
                Changed the Entry Type Code from 3 to code from the parameter(IRB_MINUTE_TYPE_REVIEWER_COMMENT)
                document.reviewCommentsForm.entryTypeCode.value = 3;--%>
                document.reviewCommentsForm.entryTypeCode.value = entryTypeCode;
                <%--COEUSQA-2290 : End--%>
                document.reviewCommentsForm.protocolNumber.value = '<%=protocolNumber%>';
                document.reviewCommentsForm.sequenceNumber.value = '<%=sequenceNumber%>';   
                document.reviewCommentsForm.submissionNumber.value = '<%=submissionNumber%>';    
                document.reviewCommentsForm.action = "<%=request.getContextPath()%>/getReviewComments.do?getMinutes=Y&setModifyFlag=show";     
                document.reviewCommentsForm.submit();      
            }
      } 
      // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start
      function editAttachmentData(attachmentId, awUpdateTimestamp,createUser,createTimestamp,personId){
            
            if(validate() == true){
                document.reviewCommentsForm.actionPerformedFrom.value = 'A';                
                document.reviewCommentsForm.awUpdateTimestamp.value = awUpdateTimestamp;
                document.reviewCommentsForm.createUser.value = createUser;
                document.reviewCommentsForm.createTimestamp.value = createTimestamp;
                document.reviewCommentsForm.personId.value = personId;                
                document.reviewCommentsForm.acType.value = 'U';
                document.reviewCommentsForm.attachmentId.value = attachmentId;
                document.reviewCommentsForm.reviewDocFileName.value = document.reviewCommentsForm.reviewFileName.value;
                
                document.reviewCommentsForm.protocolNumber.value = '<%=protocolNumber%>';
                document.reviewCommentsForm.sequenceNumber.value = '<%=sequenceNumber%>';   
                document.reviewCommentsForm.submissionNumber.value = '<%=submissionNumber%>';    
                document.reviewCommentsForm.action = "<%=request.getContextPath()%>/getReviewComments.do?getAttachments=Y&setModifyFlag=show&showComment=A";
                document.reviewCommentsForm.submit();      
            }
      }
      
      function setActionForAttachment(){
        document.reviewCommentsForm.actionPerformedFrom.value = 'A';
        dataChanged();
      }
      
      function setActionForComments(){
        document.reviewCommentsForm.actionPerformedFrom.value = 'C';
        dataChanged();
      }
      
        function selectFile(){            
            document.reviewCommentsForm.reviewDocFileName.value = document.reviewCommentsForm.reviewDocument.value;            
            document.reviewCommentsForm.reviewFileName.value = document.reviewCommentsForm.reviewDocument.value;            
            setActionForAttachment();
        }        
               
        function viewAttachment(attachmentId,personId){                
            /*document.iacucReviewCommentsForm.attachmentId.value = attachmentId;
            document.iacucReviewCommentsForm.action = "<%=request.getContextPath()%>/viewIacucReviewAttachment.do";
            document.iacucReviewCommentsForm.submit();*/
            window.open("<%=request.getContextPath()%>/viewReviewAttachment.do?&attachmentId="+attachmentId);             
        }
        
        function deleteAttachment(attachmentId,protocolNumber,sequenceNumber,submissionNumber){
        if(validate() == true){
            if (confirm("Are you sure you want to remove the Attachment?")==true){                
                document.reviewCommentsForm.actionPerformedFrom.value = 'A';                
                document.reviewCommentsForm.attachmentId.value = attachmentId;
                document.reviewCommentsForm.protocolNumber.value = protocolNumber;
                document.reviewCommentsForm.sequenceNumber.value = sequenceNumber;
                document.reviewCommentsForm.submissionNumber.value = submissionNumber;
                document.reviewCommentsForm.acType.value = 'D';                
                document.reviewCommentsForm.action = "<%=request.getContextPath()%>/saveReviewComments.do";
                document.reviewCommentsForm.submit();
            }
         }
      }
      
        function cancelReviewAttachment(){  
            if(validate() == true){
                <%if(canUserCreateReviewComments){%>
                    if(document.getElementById('add_link') != null){
                        document.getElementById('add_link').style.display = 'block';
                        document.getElementById('open_window').style.display = 'none';
                        clear_data();
                        show_comments_page();
                    }
                <%}%>
                <%if(canCompleteReview){%>
                    document.getElementById('open_review_comments_panel').style.display = 'block';
                    document.getElementById('open_review_complete_panel').style.display = 'none'
                <%}%>
            }else{
                validateForm('A');
            }
        }
      // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end
      
       function deleteData(entryNumber,awTimestamp,scheduleId){
        if(validate() == true){
            if (confirm("Are you sure you want to remove the Comment?")==true){
                // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start
                document.reviewCommentsForm.actionPerformedFrom.value = 'C';
                // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end                
                document.reviewCommentsForm.awScheduleId.value = scheduleId;                
                document.reviewCommentsForm.acType.value = 'D';                
                document.reviewCommentsForm.awEntryNumber.value = entryNumber;                     
                document.reviewCommentsForm.awUpdateTimestamp.value = awTimestamp;                
                document.reviewCommentsForm.action = "<%=request.getContextPath()%>/saveReviewComments.do";
                document.reviewCommentsForm.submit();
            }
         }
      }
       
      //Added for COEUSQA-3331 : IACUC and IRB - Review Comments should be Private by default - start
      function set_default_value(){
            if(document.reviewCommentsForm.acType.value == 'I'){
                    document.reviewCommentsForm.privateCommentFlag.checked = true;
            }
       }
       //Added for COEUSQA-3331 : IACUC and IRB - Review Comments should be Private by default - end
       
       function  clear_data(){
            document.reviewCommentsForm.acType.value = null;
            document.reviewCommentsForm.minuteEntry.value = '';     
            document.reviewCommentsForm.protoContingencyCode.value = '';
            document.reviewCommentsForm.entryNumber.value = null;
            document.reviewCommentsForm.awEntryNumber.value = null;
            //document.reviewCommentsForm.entryTypeCode.value = null;
            document.reviewCommentsForm.privateCommentFlag.checked = false;
            document.reviewCommentsForm.finalFlag.checked = false;
            // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start
            document.reviewCommentsForm.actionPerformedFrom.value = null;
            document.reviewCommentsForm.attachmentId.value = ''; 
            document.reviewCommentsForm.reviewDocDescription.value = ''; 
            document.reviewCommentsForm.reviewDocFileName.value = ''; 
            document.reviewCommentsForm.reviewFileName.value = ''; 
            document.reviewCommentsForm.privateAttachmentFlag.checked = false;
            document.reviewCommentsForm.attachmentFinalFlag.checked = false;
            // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end
       }
       function show_contingency_data(){
            if(document.reviewCommentsForm.acType.value != 'U'){
                clear_data();
            }
            document.reviewCommentsForm.actionPerformedFrom.value = 'C';// Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments
            document.reviewCommentsForm.action = "<%=request.getContextPath()%>/getReviewComments.do?showCodes=Y&setModifyFlag=show";
            document.reviewCommentsForm.submit();
       }
  
     function add_cont_comments(contingencyCode){
            if(document.reviewCommentsForm.acType.value != 'U'){
                document.reviewCommentsForm.acType.value = 'I';     
            }
            document.reviewCommentsForm.actionPerformedFrom.value = 'C'; // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments
            document.reviewCommentsForm.protoContingencyCode.value = contingencyCode;
            
            document.reviewCommentsForm.action = "<%=request.getContextPath()%>/getContingencyDes.do?setModifyFlag=show";
            document.reviewCommentsForm.submit();
      } 
      function show_comments_page() {
            document.reviewCommentsForm.action = "<%=request.getContextPath()%>/getReviewComments.do";
            document.reviewCommentsForm.submit();
      }
      //Added for the case#3282/3284-reviewer views and comments-start
        //Function for hiding and showing the Add Notes section
        // Modified for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start
            function called(value,type) {
        // Modified for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end
                if(value=='1'){
                    <%if(canUserCreateReviewComments){%>
                        document.getElementById('add_link').style.display = 'none';  
                        document.getElementById('open_window').style.display = 'block';
                        //Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start                        
                        if(type == 'C'){
                            //Added for COEUSQA-3331 : IACUC and IRB - Review Comments should be Private by default - start
                            //While adding new comments privateFlag should be checked by default
                            set_default_value();
                            //Added for COEUSQA-3331 : IACUC and IRB - Review Comments should be Private by default - end
                            document.getElementById('open_comments_window').style.display = 'block';
                            document.getElementById('open_attachment_window').style.display = 'none';
                        }else if(type == 'A'){
                            document.getElementById('open_attachment_window').style.display = 'block';
                            document.getElementById('open_comments_window').style.display = 'none';                            
                        }
                        //Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end
                    <%}%>
                    <%if(canCompleteReview){%>
                        document.getElementById('open_review_complete_panel').style.display = 'none'
                    <%}%>

                }else if(value=='2'){
                    <%if(canUserCreateReviewComments){%>
                    if(document.getElementById('add_link') != null){
                        document.getElementById('add_link').style.display = 'block';
                        document.getElementById('open_window').style.display = 'none';
                        // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start
                        if( type == 'A'){
                            clear_data();
                            show_comments_page();
                        }
                        // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end
                    }
                    <%}%>
                    
                    //document.getElementById('hide_Add').style.display = 'none';
                    <%if(canCompleteReview){%>
                        document.getElementById('open_review_comments_panel').style.display = 'block';
                        document.getElementById('open_review_complete_panel').style.display = 'none'
                    <%}%>
                      
                }
                //Added for COEUSQA-2288 : Ability for Reviewer to mark Review Comments complete and make recommendation - Start
                else if(value=='3'){
                    document.getElementById('open_review_comments_panel').style.display = 'none';
                    <%if(canCompleteReview){%>
                        document.getElementById('open_review_complete_panel').style.display = 'block'
                    <%}%>
                  
                }else if(value=='4'){
                    
                    <%if(canUserCreateReviewComments){%>
                            document.getElementById('add_link').style.display = 'block';  
                            document.getElementById('open_window').style.display = 'none';
                    <%}%>
                    <%if(canCompleteReview){%>
                      document.getElementById('open_review_complete_panel').style.display = 'none'
                    <%}%>
                }
            }
            
            //Function for opening comment for viewing
           function openDesc(value,desc) {
                //index = value;
                //var txtValue = desc;
                openWindow(value,'MyReviewComments');         
            }  
            
            function openOtherReviewerComments(value) {
                //index = value;
                //var txtValue = desc;
                openWindow(value,'OtherReviewComments');        
            } 
            
          function openWindow(txtValue,commentsFrom){
            
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

                var loc = '<bean:write name='ctxtPath'/>/coeuslite/mit/utils/dialogs/cwViewProtocolReviewComments.jsp?reviewCommentsFrom='+commentsFrom+'&value='+txtValue;
                var newWin = window.open(loc,"formName","dependent=1,toolbar=0,location=0,directories=0,status=0, menubar=0,scrollbars=0,resizable=0,width=" + w + ",height=" + (h+7)+ ",left="+ leftPos + ",top=" + topPos);

                newWin.window.focus();
           }        
           
      //Added for Review comments for past submissions
        function showHide(val,value){
            var panel = 'Panel'+value;
            var pan = 'pan'+value;
            var hidePanel  = 'hidePanel'+value;
            if(val == 1){
                        document.getElementById(panel).style.display = "none";
                        document.getElementById(hidePanel).style.display = "block";
                        document.getElementById(pan).style.display = "block";
                    }
            else if(val == 2){
                        document.getElementById(panel).style.display = "block";
                        document.getElementById(hidePanel).style.display = "none";
                        document.getElementById(pan).style.display = "none";
                    }        

            }
            
            function openPastSubDesc(txtValue){
           
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
            
            var loc = '<bean:write name='ctxtPath'/>/coeuslite/mit/utils/dialogs/cwViewPastSubmissionReviewComments.jsp?value='+txtValue;
            var newWin = window.open(loc,"formName","dependent=1,toolbar=0,location=0,directories=0,status=0, menubar=0,scrollbars=0,resizable=0,width=" + w + ",height=" + (h+7)+ ",left="+ leftPos + ",top=" + topPos);
            newWin.window.focus();
         
            }   
            
      //Added for the case#3282/3284-reviewer views and comments-end
      //Added for COEUSQA-2288 : Ability for Reviewer to mark Review Comments complete and make recommendation - Start
      function reviewComplete(){
            document.reviewCommentsForm.action = "<%=request.getContextPath()%>/reviewComplete.do";
            document.reviewCommentsForm.submit();
      }
      
      function cancelReviewComment(){
        
        var isCommentNotEditable = <%=isCommentNotEditable%>;
            if(isCommentNotEditable == true){
                if(confirm('<bean:message key="protocolMyReview.msg.saveConfirmation"/>') == true){
                    validateForm('C');//Modified for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments
                }else{
                    called(4,null);//Modified by COEUSQA-2542_Allow Protocol Reviewer to upload Attachments
                    clear_data();
                    show_comments_page();
                }
            }else if(validate() == true){
                called(4,null);//Modified by COEUSQA-2542_Allow Protocol Reviewer to upload Attachments
                clear_data();
                show_comments_page();
            }
      }
      //Added for COEUSQA-2288 : Ability for Reviewer to mark Review Comments complete and make recommendation - End
        </script>

</head>
<body>
	<html:form action="/saveReviewComments" method="post"
		enctype="multipart/form-data">
		<%--Modified for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments--%>
		<script type="text/javascript">
                document.getElementById('txt').innerHTML = '<bean:message key="helpPageTextProtocol.ReviewComments"/>';
            </script>
		<%--Added for COEUSQA-2288 : Ability for Reviewer to mark Review Comments complete and make recommendation - Start 
             <tr>
             <td>--%>
		<div id='open_review_comments_panel'>
			<%--Added for COEUSQA-2288 : Ability for Reviewer to mark Review Comments complete and make recommendation - Start --%>
			<table width="100%" height="100%" border="0" cellpadding="0"
				cellspacing="0" class="table">
				<tr>
					<td>
						<table width="100%" border="0" cellpadding="0" cellspacing="0">

							<tr class='copy' align="left">
								<td colspan="2"><font color="red"> <html:errors />

								</font></td>
							</tr>
							<%-- Added for COEUSQA-2288 : Ability for Reviewer to mark Review Comments complete and make recommendation - Start--%>
							<logic:messagesPresent>
								<html:errors header="" footer="" />
								<script>errValue = true;</script>
								<tr class='copy' align="left">
									<td colspan="2"><font color="red"> <logic:messagesPresent
												message="true">
												<script>errValue = true;</script>
												<html:messages id="message" message="true">
													<html:messages id="message" message="true"
														property="errMsg">
														<script>errLock = true;</script>
														<bean:write name="message" />
													</html:messages>
												</html:messages>
											</logic:messagesPresent>
									</font></td>
								</tr>
							</logic:messagesPresent>
							<%-- Added for COEUSQA-2288 : Ability for Reviewer to mark Review Comments complete and make recommendation - Start--%>
							<tr>
								<td height="20%" align="left" valign="top" class="theader">
									<bean:message
										key="protocolReviewComments.protocolReviewComments" />
								</td>
								<td align="right" width="50%" class="theader"><a
									id="helpPageText"
									href="javascript:showBalloon('helpPageText', 'helpText')">
										<bean:message key="helpPageTextProtocol.help" />
								</a></td>
							</tr>
						</table>
					</td>
				</tr>
				<%if(canUserCreateReviewComments){%>

				<tr>
					<td height="10%">
						<%--Added for the case#3282/3284-reviewer views and comments-start --%>
						<%
                    // COEUSDEV-161: Changes to IRB reviewer comments functionality - Access peermissions - Start   
                   /*   if(CoeusLiteConstants.YES.equalsIgnoreCase(isFromReviewer)){
                         isEditLinkEnabled = true;
                      }else if(protocolStatusCode == 101 || protocolStatusCode == 203  || protocolStatusCode == 200  || protocolStatusCode ==201  || protocolStatusCode == 202 ){
                         isEditLinkEnabled = true;
                      }else{
                         isEditLinkEnabled = false;
                      }
                      */
                     // COEUSDEV-161: Changes to IRB reviewer comments functionality - Access peermissions - End
                     %> <%--if(isEditLinkEnabled){--%>
						<div id='add_link'>
							<%if(!"Y".equals(showContingencyCodes)){%>
							<%if(isUserCanAddComments){%>

							<html:link href="javaScript:called('1','C');">
								<%--Modified for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments --%>
								<u><bean:message key="protocolReviewComments.add.label" /></u>
							</html:link>

							<%}%>
							<%-- Added for COEUSQA-2288 : Ability for Reviewer to mark Review Comments complete and make recommendation - Start --%>
							<%--Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start --%>
							<%if(isUserCanAddComments){%>
							&nbsp;&nbsp;&nbsp;&nbsp;
							<html:link href="javaScript:called('1','A');">
								<%--Modified for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments --%>
								<u> Add Review Attachment</u>
							</html:link>
							<%}%>

							<%if(canCompleteReview){%>
							&nbsp;&nbsp;&nbsp;&nbsp;
							<html:link href="javaScript:called(3,null);">
								<%--Modified for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments --%>
								<u><bean:message key="protocolReviewComments.complete.label" /></u>
							</html:link>
							<%}%>

							<%--Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end --%>
							<%}%>
						</div> <%-- Added for COEUSQA-2288 : Ability for Reviewer to mark Review Comments complete and make recommendation - END --%>

						<%--Commented for COEUSQA-2288 : Ability for Reviewer to mark Review Comments complete and make recommendation - Start --%>
						<%--<div id='hide_Add' style='display: none;'>&nbsp;--%> <%--if(isEditLinkEnabled){--%>
						<%--<%if(isUserCanAddComments){%>
                            <html:link href="javaScript:called('2');">
                             <u><bean:message key="protocolNote.label.hide"/></u>
                            </html:link>                  
                         <%}%> --%> <%--</div> 
                        Commented for COEUSQA-2288 : Ability for Reviewer to mark Review Comments complete and make recommendation - End --%>
						<%--Added for the case#3282/3284-reviewer views and comments-end --%>
						<%--<div id="helpText" class='helptext'>&nbsp;&nbsp;            
                            <bean:message key="helpTextProtocol.ReviewComments"/>
                        </div> --%>


					</td>
				</tr>



				<tr>
					<td>

						<div id='open_window' style='display: block;'>
							<div id='open_comments_window' style='display: block;'>
								<table class="tabtable" border='0'>
									<tr>
										<td nowrap class="copybold" width="7%" align="left"
											valign="top">&nbsp;&nbsp;<bean:message
												key="protocolReviewComments.label.comment" />:
										</td>
										<td class="copy" colspan="5">&nbsp;&nbsp; <html:textarea
												styleId="commentsText" property="minuteEntry" cols="80"
												rows="4" onchange="setActionForComments()"
												readonly="<%=isCommentNotEditable%>" />

										</td>
									</tr>
									<tr>
										<td>&nbsp;</td>

										<td class="copy" align="left" cospan="2">&nbsp;&nbsp;&nbsp;
											<b> <bean:message
													key="protocolReviewComments.label.private" />:
										</b> <html:checkbox property="privateCommentFlag"
												styleClass="copy" onchange="setActionForComments()"
												disabled="<%=notEditable%>" /> <b> &nbsp; <bean:message
													key="protocolReviewComments.label.final" />:
										</b> <html:checkbox property="finalFlag" styleClass="copy"
												onchange="setActionForComments()"
												disabled="<%=notEditable%>" />
										</td>
										<%--<td class="copy">
                                        <b> &nbsp;
                                            <bean:message key="protocolReviewComments.label.contingencyCode"/> :
                                            <bean:write name="reviewCommentsForm" property="protoContingencyCode"/>
                                            
                                            <html:text property= "protoContingencyCode" styleClass="textbox"  readonly="true" size="5" maxlength="5" onchange="dataChanged()"/>
                                        </b>
                                    </td>  --%>
									</tr>
									<tr>

										<td class="copy" colspan="3"><html:button property="save"
												value="Save" styleClass="clsavebutton"
												onclick="validateForm('C')" />
											<%--Modified for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments--%>

											<%--Modified for COEUSQA-2288 : Ability for Reviewer to mark Review Comments complete and make recommendation - Start 
                                        <html:button property="save" value="Cancel"  styleClass="clsavebutton" onclick="javascript:called(2);clear_data();show_comments_page();"/>--%>
											<%
                                            
                                            String functionsToCall = "";
                                            if("Y".equalsIgnoreCase(isPreDefinedComments) || "Y".equals(showContingencyCodes)){
                                            functionsToCall = "javascript:clear_data();show_comments_page();";
                                            }else{
                                            functionsToCall = "javascript:cancelReviewComment();";
                                            }
                                            %> <html:button
												property="save" value="Cancel" styleClass="clsavebutton"
												onclick="<%=functionsToCall%>" /> <%--Modified for COEUSQA-2288 : Ability for Reviewer to mark Review Comments complete and make recommendation - End --%>
										</td>
										<%if("Y".equals(showContingencyCodes)){%>
										<td>&nbsp;</td>
										<%-- <td class="copy" align="right">
                                        <html:link href="javascript:show_comments_page()">
                                            <bean:message key="protocolReviewComments.returnToComments"/>
                                        </html:link>
                                    </td>--%>
										<%}else{%>
										<td>&nbsp;</td>
										<td class="copy" align="right"><html:link
												href="javascript:show_contingency_data()">
												<u><bean:message
														key="protocolReviewComments.selectContingencyCode" /></u>
											</html:link> <%}%></td> &nbsp;&nbsp;&nbsp;&nbsp;
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										&nbsp;&nbsp;&nbsp;&nbsp;

									</tr>
								</table>
							</div>
							<%--Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start --%>

							<%--Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end --%>
						</div> <%--Modified for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start --%>

						<%--Modified for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end --%>
					</td>
				</tr>
				<tr>
					<td>
						<div id='open_attachment_window' style='display: block;'>
							<table width="100%">
								<tr>
									<td colspan="4">
										<table class="tabtable" cellpadding="2" cellspacing="1"
											border='0' width="100%">
											<tr>
												<td class="copybold" align="right"><b><bean:message
															key="uploadDocLabel.Description" />:</b></td>
												<td colspan="3" class="copy"><html:text
														property="reviewDocDescription" maxlength="200"
														styleClass="textbox-long" style="width:290px;"
														disabled="<%=isCommentNotEditable%>" readonly="false"
														onchange="setActionForAttachment()" /></td>
											</tr>
											<tr>
												<td class="copybold" align="right"><b><bean:message
															key="uploadDocLabel.FileName" />:</b></td>
												<td colspan="3" class="copy"><html:file
														property="reviewDocument" onchange="selectFile()"
														maxlength="300" size="50"
														disabled="<%=isCommentNotEditable%>" /> <html:text
														property="reviewFileName" style="width: 750px;"
														styleClass="cltextbox-color" disabled="true"
														readonly="true" /></td>
											</tr>
											<tr>
												<td class="copybold" align="right"><b> <bean:message
															key="protocolReviewComments.label.private" />:
												</b></td>
												<td colspan="3" class="copy"><html:checkbox
														property="privateAttachmentFlag" styleClass="copy"
														onclick="setActionForAttachment()"
														disabled="<%=isCommentNotEditable%>" /></td>
											</tr>
											<tr>
												<td class="copy" colspan="4"><html:button
														property="save" value="Save" styleClass="clsavebutton"
														onclick="validateForm('A')" /> <html:button
														property="save" value="Cancel" styleClass="clsavebutton"
														onclick="javascript:cancelReviewAttachment();" /> <%--html:button property="save" value="Cancel"  styleClass="clsavebutton" onclick="javascript:called(2,'A');"/--%>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</div>

					</td>
				</tr>
				<%}%>
				<html:hidden property="acType" />
				<html:hidden property="protocolNumber" />
				<html:hidden property="sequenceNumber" />
				<html:hidden property="submissionNumber" />
				<html:hidden property="scheduleId" />
				<html:hidden property="personId" />
				<html:hidden property="awUpdateTimestamp" />
				<html:hidden property="entryNumber" />
				<html:hidden property="awEntryNumber" />
				<html:hidden property="entryTypeCode" />
				<html:hidden property="awScheduleId" />
				<html:hidden property="protoContingencyCode" />
				<html:hidden property="actionPerformedFrom" />
				<html:hidden property="reviewDocFileName" />
				<html:hidden property="attachmentId" />
				<html:hidden property="createUser" />
				<html:hidden property="createTimestamp" />
				<html:hidden property="attachmentFinalFlag" />
				<% 
            String strBgColor = "#c6cbd2"; //BGCOLOR=EFEFEF  FBF7F7
            int count = 0;
            int myAttachCount = 0;
            %>
				<%if("Y".equalsIgnoreCase(showContingencyCodes)){%>
				<tr>
					<td class="theader" align="left" height="20" colspan="4"><bean:message
							key="protocolReviewComments.contingencyLookUp" /></td>
				</tr>
				<table width="100%" border="0" cellpadding="0" class="tabtable">
					<tr align="left">
						<td width="5%" class="theader">
							<%--<bean:message key="protocolReviewComments.contingencyCode"/>--%>
						</td>
						<td width="80%" class="theader">
							<%--<bean:message key="protocolReviewComments.contingencyDescription"/>--%>
						</td>
						<td width="2%" class="theader"></td>
						<td width="13%" class="theader"></td>
					</tr>

					<logic:present name="contingencyList">
						<logic:iterate id="contList" name="contingencyList"
							type="org.apache.struts.validator.DynaValidatorForm">
							<% 
                        if (count%2 == 0)
                            strBgColor = "#D6DCE5"; 
                        else
                            strBgColor="#c6cbd2";
                        
                        %>
							<tr align="left" class="copy" bgcolor="<%=strBgColor%>"
								onmouseover="className='TableItemOn'"
								onmouseout="className='TableItemOff'">

								<td>&nbsp;&nbsp; <%=contList.get("protoContingencyCode")%>
								</td>
								<td class="copy"><%=contList.get("contingencyDescription")%>

								</td>
								<td>&nbsp;</td>
								<td class="copy">
									<% 
                                
                                contingencyCode =(String) contList.get("protoContingencyCode");
                                minuteEntry = (String) contList.get("contingencyDescription");
                                
                                String addCommentLink = "javascript:add_cont_comments('"+contingencyCode+"')";
                                %> <html:link href="<%=addCommentLink%>">
										<bean:message key="protocolReviewComments.addComment" />
									</html:link>

								</td>

							</tr>
							<% count++;%>
						</logic:iterate>
					</logic:present>
					<tr class="table" style="height: 14px;">
						<%--<td colspan="3" align="right">
                        <html:link href="javascript:show_comments_page()">
                            <bean:message key="protocolReviewComments.returnToComments"/>
                        </html:link>
                    </td>--%>
					</tr>
				</table>
				<%}else{%>

				<tr align="center">

					<%-- <td class= "theader" align="left" height="20">
                    <bean:message key="protocolReviewComments.listOfReviewComments"/>
                </td> --%>

					<table width="100%" border="0" cellpadding="2" cellspacing="0"
						class="tabtable">
						<%-- Modified for COEUSQA-2288 : Ability for Reviewer to mark Review Comments complete and make recommendation - Start --%>
						<tr align="left">
							<td width="100%" class="theader" colspan="10"><bean:message
									key="reviewComments.MyReviewComment.label" /></td>
						</tr>
						<%if(myReviewComments != null && myReviewComments.size()>0){ %>
						<tr align="left">
							<td width="3%" class="theader"></td>
							<td width="15%" class="theader"><bean:message
									key="protocolReviewComments.reviewer" /></td>
							<td width="50%" class="theader"><bean:message
									key="protocolReviewComments.comment" /></td>
							<td width="4%" class="theader"><bean:message
									key="protocolReviewComments.label.private" /></td>
							<td width="2%" class="theader"></td>
							<td width="4%" class="theader"><bean:message
									key="protocolReviewComments.label.final" /></td>
							<td width="3%" class="theader"></td>
							<td width="8%" class="theader"></td>
							<td width="8%" class="theader"></td>
							<td width="8%" class="theader"></td>
						</tr>
						<%}%>
						<%-- Modified for COEUSQA-2288 : Ability for Reviewer to mark Review Comments complete and make recommendation - Start --%>
						</tr>

						<% 
                    int index=0;
              %>
						<logic:empty name="myReviewComments">

							<tr align="center">
								<td class="copy" align="center" height="20"><bean:message
										key="protocolReviewComments.noMyReviewComments" /></td>
							</tr>

						</logic:empty>

						<logic:present name="myReviewComments">
							<logic:iterate id="review" name="myReviewComments"
								type="org.apache.struts.validator.DynaValidatorForm">

								<% 
                      //Added for COEUSDEV-237 : Investigator cannot see review comments
                      String userRightInReviewComment = (String)review.get("userRightInReviewComment");
                      //COEUSDEV-237 : End
                      if (count%2 == 0)
                          strBgColor = "#D6DCE5";
                      else
                          strBgColor="#c6cbd2";
                    
                    personId =(String) review.get("personId");
                    // COEUSDEV-161: Changes to IRB reviewer comments functionality - Access peermissions - Start
 //                   if("Y".equalsIgnoreCase((String)review.get("finalFlag")) || loggedInPersonId.equalsIgnoreCase(personId) || userHasAdminRole.equalsIgnoreCase("Y")){
                      //if(CoeusLiteConstants.YES.equalsIgnoreCase(userHasAdminRole) || isEditLinkEnabled){
                        //Commented for COEUSDEV-237 : Investigator cannot see review comments
                        //if("Y".equalsIgnoreCase((String)review.get("finalFlag")) || loggedInPersonId.equalsIgnoreCase(personId) ){
                          if(userRightInReviewComment != null && (userRightInReviewComment.equals("modifyComment") || userRightInReviewComment.equals("viewComment"))){
                        //COEUSDEV-237 : END  
                    // COEUSDEV-161: Changes to IRB reviewer comments functionality - Access peermissions - End   
                            minuteEntry = "";
                            if(review.get("minuteEntry") != null){
                                minuteEntry = review.get("minuteEntry").toString();
                            }
                            contingencyCode = (String) review.get("protoContingencyCode");
                            if(contingencyCode == null){
                                contingencyCode ="";
                            }
                            entryNumber = (Integer) review.get("entryNumber");
                            
                            String editLink ="";
                            editLink = "javascript:edit_data('" +review.get("entryNumber") +"','" +review.get("entryTypeCode") +"','" +review.get("updateTimestamp") +"','" +review.get("personId") +"','" +review.get("scheduleId") +"')";
                            
                            // COEUSDEV-161: Changes to IRB reviewer comments functionality - Access peermissions
                            //                        if(loggedInPersonId.equals(personId) ||  userHasAdminRole.equalsIgnoreCase("Y")){
                            //                            isEditLinkEnabled = true;
                            //                        }
                            personFullName = (String) review.get("personName");
                            if(personFullName == null || "null".equalsIgnoreCase(personFullName)){
                                reviewerName = (String) review.get("createUserName");
                                if(reviewerName == null || "null".equalsIgnoreCase(reviewerName)){
                                    reviewerName = "";
                                }
                            }else{
                                reviewerName = personFullName;
                            }
                            
                            
                      if(canViewUpdateDetails){%>
								<tr align="left" class="copy" bgcolor="<%=strBgColor%>">
									<%}else{%>
								
								<tr align="left" class="copy" bgcolor="<%=strBgColor%>"
									onmouseover="className='TableItemOn'"
									onmouseout="className='TableItemOff'">
									<%}%>

									<td>&nbsp;</td>
									<td class="copy">
										<%--  COEUSQA-2291 : Start --%> <%if("true".equals((String)review.get("canUserViewReviewer"))){%>
										<%=reviewerName%> <%}%> <%-- COEUSQA-2291 : End--%>
									</td>
									<td class="copy">
										<%
                              
                              String comment = "";
                              if(minuteEntry != null && minuteEntry.length() > 67){
                                  comment =   minuteEntry.substring(0,64)+"...";
                              }else{
                                  comment  =  minuteEntry;
                              }
                              
                              %> <%=comment%>

									</td>

									<td class='copy' align='center'><logic:equal name="review"
											property="privateCommentFlag" value="Y">
											<img
												src="<bean:write name='ctxtPath'/>/coeusliteimages/checked.gif">
										</logic:equal></td>
									<td>&nbsp;</td>
									<td class='copy' align='center'><logic:equal name="review"
											property="finalFlag" value="Y">
											<img
												src="<bean:write name='ctxtPath'/>/coeusliteimages/checked.gif">
										</logic:equal></td>
									<td>&nbsp;</td>
									<td class='copy'>
										<%-- <%
                            String commentLink = "javascript:view_comments('" +review.get("entryNumber") +"')";
                            %>
                            <html:link href="<%=commentLink%>">
                                <bean:message key="protocolReviewComments.view"/>
                            </html:link>--%> <%-- added for the case#3282-- Reviewer Views and Comments--%>
										<%String link = "javaScript:openDesc('" +index +"')";%> <html:link
											href="<%=link%>">
											<bean:message key="protocolReviewComments.view" />
										</html:link>
									</td>
									<td class='copy'>
										<%-- Commented for COEUSDEV-237 : Investigator cannot see review comments - Start
                              if(isEditLinkEnabled){--%> <%if(userRightInReviewComment != null && userRightInReviewComment.equals("modifyComment")){%><%--//COEUSDEV-237:End--%>
										<html:link href="<%=editLink%>">
											<bean:message key="protocolReviewComments.modify" />
										</html:link> <%}%><%--else{%>
                              <bean:message key="protocolReviewComments.modify"/>
                              <%}--%>
									</td>
									<td class='copy'>
										<%
                              
                              removeLink = "javascript:deleteData('" +entryNumber +"','" +review.get("updateTimestamp") +"','" +review.get("scheduleId") +"')";
                              %> <%-- Commented for COEUSDEV-237 : Investigator cannot see review comments - Start
                              <%--if(isEditLinkEnabled){--%> <%if(userRightInReviewComment != null && userRightInReviewComment.equals("modifyComment")){%><%--//COEUSDEV-237:End--%>
										<html:link href="<%=removeLink%>">
											<bean:message key="protocolReviewComments.remove" />
										</html:link> <%}%><%--else{%>
                              <bean:message key="protocolReviewComments.remove"/>
                              <%}--%>



									</td>

								</tr>
								</tr>
								<%if(canViewUpdateDetails){%>
								<tr bgcolor="<%=strBgColor%>">
									<td colspan="11">
										<!-- <table width='95%' class="table" align="center">-->
										<table width='95%' class="copy" align="center">
											<tr>
												<%String createUserName = (String)review.get("createUserName");
                                       if(createUserName != null && !"".equals(createUserName)){
                                       %>
												<td width='50%'>
													<%
                                         String createTimestamp = "";
                                         String displayFormatedDate = "";
                                          if(createUserName != null && createUserName.length() > 20){
                                            
                                            createUserName =   createUserName.substring(0,20)+"...";
                                           
                                          }
                                          createTimestamp = (String)review.get("createTimestamp");
                                          if(createTimestamp != null &&  !(createTimestamp.trim().equals(""))){
                                            java.sql.Timestamp timeStamp = java.sql.Timestamp.valueOf(createTimestamp);
                                            displayFormatedDate = dateFormat.format(timeStamp);
                                          }
                                          %>&nbsp;<b>Create by</b>
													&nbsp; <%=createUserName%>&nbsp;<b>at</b>&nbsp;<%=displayFormatedDate%>

												</td>
												<%}%>

												<td width='50%'>
													<%
                                          String updateUserName = (String)review.get("updateUserName"); 
                                            if(updateUserName != null && updateUserName.length() > 20){
                                              
                                            updateUserName =   updateUserName.substring(0,20)+"...";
                                          } 
                                          String updateTimestamp = (String)review.get("updateTimestamp");
                                          String displayFormatedDate = "";
                                          if(updateTimestamp != null &&  !(updateTimestamp.trim().equals(""))){
                                              java.sql.Timestamp timeStamp = java.sql.Timestamp.valueOf(updateTimestamp);
                                              displayFormatedDate = dateFormat.format(timeStamp);
                                          }
                                          %> &nbsp;<b>Last Update by</b>
													&nbsp; <%=updateUserName%>&nbsp;<b>at</b>&nbsp;<%=displayFormatedDate%>
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<%}%>


								<% count++; 
                      }
                      //}
                      %>
								<%         
                      index++;
                      %>
							</logic:iterate>
						</logic:present>
						<%-- Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start--%>
						<tr align="center">
							<table width="100%" border="0" cellpadding="2" cellspacing="0"
								class="tabtable">
								<tr align="left">
									<td width="100%" class="theader" colspan="10"><bean:message
											key="reviewComments.MyReviewAttachment.label" /></td>
								</tr>
								<%if(myReviewAttachments != null && myReviewAttachments.size()>0){%>
								<tr align="left">
									<td width="3%" class="theader"></td>
									<td width="15%" class="theader"><bean:message
											key="protocolReviewComments.reviewer" /></td>
									<td width="50%" class="theader"><bean:message
											key="protocolReviewComments.AttachmentDescription" /></td>
									<td width="4%" class="theader"><bean:message
											key="protocolReviewComments.label.private" /></td>
									<td width="2%" class="theader"></td>
									<td width="3%" class="theader"></td>
									<td width="8%" class="theader"></td>
									<td width="8%" class="theader"></td>
									<td width="8%" class="theader"></td>
									<td width="8%" class="theader"></td>
								</tr>
								<%}%>
								</tr>

								<% int myRevAttachIndex=0;%>
								<logic:empty name="myReviewAttachments">

									<tr align="center">
										<td class="copy" align="center" height="20"><bean:message
												key="protocolReviewComments.noMyReviewAttachments" /></td>
									</tr>

								</logic:empty>

								<logic:present name="myReviewAttachments">
									<logic:iterate id="review" name="myReviewAttachments"
										type="org.apache.struts.validator.DynaValidatorForm">

										<% String userRightInReviewComment = (String)review.get("userRightInReviewComment");
                      if (myAttachCount%2 == 0)
                          strBgColor = "#D6DCE5";
                      else
                          strBgColor="#c6cbd2";
                    
                    personId =(String) review.get("personId");                    
                          if(userRightInReviewComment != null && (userRightInReviewComment.equals("modifyComment") || userRightInReviewComment.equals("viewComment"))){
                            
                            attachmentDescription = "";
                            if(review.get("reviewDocDescription") != null){
                                attachmentDescription = review.get("reviewDocDescription").toString();
                            }
                            
                            String editLink ="";
                            //editLink = "javascript:edit_data('" +review.get("entryNumber") +"','" +review.get("entryTypeCode") +"','" +review.get("updateTimestamp") +"','" +review.get("personId") +"','" +review.get("scheduleId") +"')";
                            editLink = "javascript:editAttachmentData('" +review.get("attachmentId") +"','" +review.get("updateTimestamp") +"','" +review.get("createUser") +"','" +review.get("createTimestamp") +"','" +review.get("personId") +"')";
                            
                        
                            personFullName = (String) review.get("personName");
                            if(personFullName == null || "null".equalsIgnoreCase(personFullName)){
                                reviewerName = (String) review.get("createUserName");
                                if(reviewerName == null || "null".equalsIgnoreCase(reviewerName)){
                                    reviewerName = "";
                                }
                            }else{
                                reviewerName = personFullName;
                            }
                            
                            
                      if(canViewUpdateDetails){%>
										<tr align="left" class="copy" bgcolor="<%=strBgColor%>">
											<%}else{%>
										
										<tr align="left" class="copy" bgcolor="<%=strBgColor%>"
											onmouseover="className='TableItemOn'"
											onmouseout="className='TableItemOff'">
											<%}%>

											<td>&nbsp;</td>
											<td class="copy">
												<%if("true".equals((String)review.get("canUserViewReviewer"))){%>
												<%=reviewerName%> <%}%>
											</td>
											<td class="copy">
												<%String comment = "";
                              if(attachmentDescription != null && attachmentDescription.length() > 67){
                                  comment =   attachmentDescription.substring(0,64)+"...";
                              }else{
                                  comment  =  attachmentDescription;
                              }
                              %> <%=comment%>
											</td>

											<td class='copy' align='center'><logic:equal
													name="review" property="privateAttachmentFlag" value="Y">
													<img
														src="<bean:write name='ctxtPath'/>/coeusliteimages/checked.gif">
												</logic:equal></td>
											<td>&nbsp;</td>
											<td>&nbsp;</td>
											<td>&nbsp;</td>
											<td class='copy'>
												<%//String link = "javaScript:viewAttachment('" +myRevAttachIndex +"','" +review.get("attachmentId") +"','" +review.get("personId") +"')";%>
												<%String link = "javaScript:viewAttachment('" +review.get("attachmentId") +"','" +review.get("personId") +"')";%>
												<html:link href="<%=link%>">
													<bean:message key="protocolReviewComments.view" />
												</html:link>
											</td>
											<td class='copy'>
												<%if(userRightInReviewComment != null && userRightInReviewComment.equals("modifyComment")){%>
												<html:link href="<%=editLink%>">
													<bean:message key="protocolReviewComments.modify" />
												</html:link> <%}%>
											</td>
											<td class='copy'>
												<%
                              
                              //removeLink = "javascript:deleteData('" +entryNumber +"','" +review.get("updateTimestamp") +"','" +review.get("scheduleId") +"')";
                              removeAttachment = "javaScript:deleteAttachment('" +review.get("attachmentId") +"','" +review.get("protocolNumber") +"','" +review.get("sequenceNumber") +"','" +review.get("submissionNumber") +"')";
                              %> <%if(userRightInReviewComment != null && userRightInReviewComment.equals("modifyComment")){%>
												<html:link href="<%=removeAttachment%>">
													<bean:message key="protocolReviewComments.remove" />
												</html:link> <%}%>
											</td>

										</tr>
										</tr>
										<%if(canViewUpdateDetails){%>
										<tr bgcolor="<%=strBgColor%>">
											<td colspan="11">
												<table width='95%' class="copy" align="center">
													<tr>
														<%String createUserName = (String)review.get("createUserName");
                                       if(createUserName != null && !"".equals(createUserName)){
                                       %>
														<td width='50%'>
															<%
                                         String createTimestamp = "";
                                         String displayFormatedDate = "";
                                          if(createUserName != null && createUserName.length() > 20){
                                            
                                            createUserName =   createUserName.substring(0,20)+"...";
                                           
                                          }
                                          createTimestamp = (String)review.get("createTimestamp");
                                          if(createTimestamp != null &&  !(createTimestamp.trim().equals(""))){
                                            java.sql.Timestamp timeStamp = java.sql.Timestamp.valueOf(createTimestamp);
                                            displayFormatedDate = dateFormat.format(timeStamp);
                                          }
                                          %>&nbsp;<b>Create by</b>
															&nbsp; <%=createUserName%>&nbsp;<b>at</b>&nbsp;<%=displayFormatedDate%>

														</td>
														<%}%>

														<td width='50%'>
															<%
                                          String updateUserName = (String)review.get("updateUserName"); 
                                            if(updateUserName != null && updateUserName.length() > 20){
                                              
                                            updateUserName =   updateUserName.substring(0,20)+"...";
                                          } 
                                          String updateTimestamp = (String)review.get("updateTimestamp");
                                          String displayFormatedDate = "";
                                          if(updateTimestamp != null &&  !(updateTimestamp.trim().equals(""))){
                                              java.sql.Timestamp timeStamp = java.sql.Timestamp.valueOf(updateTimestamp);
                                              displayFormatedDate = dateFormat.format(timeStamp);
                                          }
                                          %> &nbsp;<b>Last Update by</b>
															&nbsp; <%=updateUserName%>&nbsp;<b>at</b>&nbsp;<%=displayFormatedDate%>
														</td>
													</tr>
												</table>
											</td>
										</tr>
										<%}%>
										<% myAttachCount++; 
                      }%>
										<%myRevAttachIndex++;%>
									</logic:iterate>
								</logic:present>
								<%-- Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end--%>
								<tr align="center">

									<%-- <td class= "theader" align="left" height="20">
                    <bean:message key="protocolReviewComments.listOfReviewComments"/>
                </td> --%>

									<table width="100%" border="0" cellpadding="2" cellspacing="0"
										class="tabtable">
										<%-- Modified for COEUSQA-2288 : Ability for Reviewer to mark Review Comments complete and make recommendation - Start --%>
										<tr align="left">
											<td width="100%" class="theader" colspan="10"><bean:message
													key="reviewComments.OtherReviewComments.label" /></td>
										</tr>
										<%if(otherReviewerReviewComments != null && otherReviewerReviewComments.size()>0){%>

										<tr align="left">
											<td width="3%" class="theader"></td>
											<td width="15%" class="theader"><bean:message
													key="protocolReviewComments.reviewer" /></td>
											<td width="50%" class="theader"><bean:message
													key="protocolReviewComments.comment" /></td>
											<td width="4%" class="theader"><bean:message
													key="protocolReviewComments.label.private" /></td>
											<td width="2%" class="theader"></td>
											<td width="4%" class="theader"><bean:message
													key="protocolReviewComments.label.final" /></td>
											<td width="3%" class="theader"></td>
											<td width="8%" class="theader"></td>
											<td width="8%" class="theader"></td>
											<td width="8%" class="theader"></td>
										</tr>
										<%}%>
										<%-- Modified for COEUSQA-2288 : Ability for Reviewer to mark Review Comments complete and make recommendation - Start --%>
										</tr>

										<% 
                    int index1=0;
              %>
										<logic:empty name="otherReviewerReviewComments">

											<tr align="center">
												<td class="copy" align="center" height="20"><bean:message
														key="protocolReviewComments.noOtherReviewerReviewComments" />
												</td>
											</tr>

										</logic:empty>
										<%int count1 = 0;%>
										<logic:present name="otherReviewerReviewComments">
											<logic:iterate id="review" name="otherReviewerReviewComments"
												type="org.apache.struts.validator.DynaValidatorForm">

												<% 
                      //Added for COEUSDEV-237 : Investigator cannot see review comments
                      String userRightInReviewComment = (String)review.get("userRightInReviewComment");
                      //COEUSDEV-237 : End
                      String strBgColor1 = "#c6cbd2";
                      if (count1%2 == 0)
                          strBgColor1 = "#D6DCE5";
                      else
                          strBgColor1="#c6cbd2";
                    
                    personId =(String) review.get("personId");
                    // COEUSDEV-161: Changes to IRB reviewer comments functionality - Access peermissions - Start
 //                   if("Y".equalsIgnoreCase((String)review.get("finalFlag")) || loggedInPersonId.equalsIgnoreCase(personId) || userHasAdminRole.equalsIgnoreCase("Y")){
                      //if(CoeusLiteConstants.YES.equalsIgnoreCase(userHasAdminRole) || isEditLinkEnabled){
                        //Commented for COEUSDEV-237 : Investigator cannot see review comments
                        //if("Y".equalsIgnoreCase((String)review.get("finalFlag")) || loggedInPersonId.equalsIgnoreCase(personId) ){
                          if(userRightInReviewComment != null && (userRightInReviewComment.equals("modifyComment") || userRightInReviewComment.equals("viewComment"))){
                        //COEUSDEV-237 : END  
                    // COEUSDEV-161: Changes to IRB reviewer comments functionality - Access peermissions - End   
                            minuteEntry = "";
                            if(review.get("minuteEntry") != null){
                                minuteEntry = review.get("minuteEntry").toString();
                            }
                            contingencyCode = (String) review.get("protoContingencyCode");
                            if(contingencyCode == null){
                                contingencyCode ="";
                            }
                            entryNumber = (Integer) review.get("entryNumber");
                            
                            String editLink ="";
                            editLink = "javascript:edit_data('" +review.get("entryNumber") +"','" +review.get("entryTypeCode") +"','" +review.get("updateTimestamp") +"','" +review.get("personId") +"','" +review.get("scheduleId") +"')";
                            
                            // COEUSDEV-161: Changes to IRB reviewer comments functionality - Access peermissions
                            //                        if(loggedInPersonId.equals(personId) ||  userHasAdminRole.equalsIgnoreCase("Y")){
                            //                            isEditLinkEnabled = true;
                            //                        }
                            personFullName = (String) review.get("personName");
                            if(personFullName == null || "null".equalsIgnoreCase(personFullName)){
                                reviewerName = (String) review.get("createUserName");
                                if(reviewerName == null || "null".equalsIgnoreCase(reviewerName)){
                                    reviewerName = "";
                                }
                            }else{
                                reviewerName = personFullName;
                            }
                            
                            
                      if(canViewUpdateDetails){%>
												<tr align="left" class="copy" bgcolor="<%=strBgColor1%>">
													<%}else{%>
												
												<tr align="left" class="copy" bgcolor="<%=strBgColor1%>"
													onmouseover="className='TableItemOn'"
													onmouseout="className='TableItemOff'">
													<%}%>

													<td>&nbsp;</td>
													<td class="copy">
														<%--COEUSQA-2291 : Start--%> <%if("true".equals((String)review.get("canUserViewReviewer"))){%>
														<%=reviewerName%> <%}%> <%--COEUSQA-2291 : End--%>
													</td>
													<td class="copy">
														<%
                              
                              String comment = "";
                              if(minuteEntry != null && minuteEntry.length() > 67){
                                  comment =   minuteEntry.substring(0,64)+"...";
                              }else{
                                  comment  =  minuteEntry;
                              }
                              
                              %> <%=comment%>

													</td>

													<td class='copy' align='center'><logic:equal
															name="review" property="privateCommentFlag" value="Y">
															<img
																src="<bean:write name='ctxtPath'/>/coeusliteimages/checked.gif">
														</logic:equal></td>
													<td>&nbsp;</td>
													<td class='copy' align='center'><logic:equal
															name="review" property="finalFlag" value="Y">
															<img
																src="<bean:write name='ctxtPath'/>/coeusliteimages/checked.gif">
														</logic:equal></td>
													<td>&nbsp;</td>
													<td class='copy'>
														<%-- <%
                            String commentLink = "javascript:view_comments('" +review.get("entryNumber") +"')";
                            %>
                            <html:link href="<%=commentLink%>">
                                <bean:message key="protocolReviewComments.view"/>
                            </html:link>--%> <%-- added for the case#3282-- Reviewer Views and Comments--%>
														<%String link = "javaScript:openOtherReviewerComments('" +index1 +"')";%>
														<html:link href="<%=link%>">
															<bean:message key="protocolReviewComments.view" />
														</html:link>
													</td>
													<td class='copy'>
														<%-- Commented for COEUSDEV-237 : Investigator cannot see review comments - Start
                              if(isEditLinkEnabled){--%> <%if(userRightInReviewComment != null && userRightInReviewComment.equals("modifyComment")){%><%--//COEUSDEV-237:End--%>
														<html:link href="<%=editLink%>">
															<bean:message key="protocolReviewComments.modify" />
														</html:link> <%}%><%--else{%>
                              <bean:message key="protocolReviewComments.modify"/>
                              <%}--%>
													</td>
													<td class='copy'>
														<%
                              
                              removeLink = "javascript:deleteData('" +entryNumber +"','" +review.get("updateTimestamp") +"','" +review.get("scheduleId") +"')";
                              %> <%-- Commented for COEUSDEV-237 : Investigator cannot see review comments - Start
                              <%--if(isEditLinkEnabled){--%> <%if(userRightInReviewComment != null && userRightInReviewComment.equals("modifyComment")){%><%--//COEUSDEV-237:End--%>
														<html:link href="<%=removeLink%>">
															<bean:message key="protocolReviewComments.remove" />
														</html:link> <%}%><%--else{%>
                              <bean:message key="protocolReviewComments.remove"/>
                              <%}--%>



													</td>

												</tr>
												</tr>
												<%if(canViewUpdateDetails){%>
												<tr bgcolor="<%=strBgColor1%>">
													<td colspan="11">
														<!-- <table width='95%' class="table" align="center">-->
														<table width='95%' class="copy" align="center">
															<tr>
																<%String createUserName = (String)review.get("createUserName");
                                       if(createUserName != null && !"".equals(createUserName)){
                                       %>
																<td width='50%'>
																	<%
                                         String createTimestamp = "";
                                         String displayFormatedDate = "";
                                          if(createUserName != null && createUserName.length() > 20){
                                            
                                            createUserName =   createUserName.substring(0,20)+"...";
                                           
                                          }
                                          createTimestamp = (String)review.get("createTimestamp");
                                          if(createTimestamp != null &&  !(createTimestamp.trim().equals(""))){
                                            java.sql.Timestamp timeStamp = java.sql.Timestamp.valueOf(createTimestamp);
                                            displayFormatedDate = dateFormat.format(timeStamp);
                                          }
                                          %>&nbsp;<b>Create by</b>
																	&nbsp; <%=createUserName%>&nbsp;<b>at</b>&nbsp;<%=displayFormatedDate%>

																</td>
																<%}%>

																<td width='50%'>
																	<%
                                          String updateUserName = (String)review.get("updateUserName"); 
                                            if(updateUserName != null && updateUserName.length() > 20){
                                              
                                            updateUserName =   updateUserName.substring(0,20)+"...";
                                          } 
                                          String updateTimestamp = (String)review.get("updateTimestamp");
                                          String displayFormatedDate = "";
                                          if(updateTimestamp != null &&  !(updateTimestamp.trim().equals(""))){
                                              java.sql.Timestamp timeStamp = java.sql.Timestamp.valueOf(updateTimestamp);
                                              displayFormatedDate = dateFormat.format(timeStamp);
                                          }
                                          %> &nbsp;<b>Last Update by</b>
																	&nbsp; <%=updateUserName%>&nbsp;<b>at</b>&nbsp;<%=displayFormatedDate%>
																</td>
															</tr>
														</table>
													</td>
												</tr>
												<%}%>


												<% count1++; 
                      }
                      //}
                      %>
												<%         
                      index1++;
                      %>
											</logic:iterate>
										</logic:present>

										<%-- Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start--%>

										<tr align="center">
											<table width="100%" border="0" cellpadding="2"
												cellspacing="0" class="tabtable">
												<tr align="left">
													<td width="100%" class="theader" colspan="10"><bean:message
															key="reviewComments.OtherReviewAttachments.label" /></td>
												</tr>
												<%if(otherReviewerReviewAttachments != null && otherReviewerReviewAttachments.size()>0){%>

												<tr align="left">
													<td width="3%" class="theader"></td>
													<td width="15%" class="theader"><bean:message
															key="protocolReviewComments.reviewer" /></td>
													<td width="50%" class="theader"><bean:message
															key="protocolReviewComments.comment" /></td>
													<td width="4%" class="theader"><bean:message
															key="protocolReviewComments.label.private" /></td>
													<td width="2%" class="theader"></td>
													<td width="4%" class="theader"></td>
													<td width="3%" class="theader"></td>
													<td width="8%" class="theader"></td>
													<td width="8%" class="theader"></td>
													<td width="8%" class="theader"></td>
												</tr>
												<%}%>
												</tr>

												<% 
                    int attachmntIndex1=0;
              %>
												<logic:empty name="otherReviewerReviewAttachments">

													<tr align="center">
														<td class="copy" align="center" height="20"><bean:message
																key="protocolReviewComments.noOtherReviewerReviewAttachments" />
														</td>
													</tr>

												</logic:empty>
												<%int attachmntCount1 = 0;%>
												<logic:present name="otherReviewerReviewAttachments">
													<logic:iterate id="review"
														name="otherReviewerReviewAttachments"
														type="org.apache.struts.validator.DynaValidatorForm">

														<%String userRightInReviewComment = (String)review.get("userRightInReviewComment");
                      String strBgColor1 = "#c6cbd2";
                      if (attachmntCount1%2 == 0)
                          strBgColor1 = "#D6DCE5";
                      else
                          strBgColor1="#c6cbd2";
                    
                    personId =(String) review.get("personId");                    
                          if(userRightInReviewComment != null && (userRightInReviewComment.equals("modifyComment") || userRightInReviewComment.equals("viewComment"))){
                            otherRevAttachmntDescription = "";
                            if(review.get("reviewDocDescription") != null){
                                otherRevAttachmntDescription = review.get("reviewDocDescription").toString();
                            }
                            String editLink ="";                            
                            editLink = "javascript:editAttachmentData('" +review.get("attachmentId") +"','" +review.get("updateTimestamp") +"','" +review.get("createUser") +"','" +review.get("createTimestamp") +"','" +review.get("personId") +"')";
                            personFullName = (String) review.get("personName");
                            if(personFullName == null || "null".equalsIgnoreCase(personFullName)){
                                reviewerName = (String) review.get("createUserName");
                                if(reviewerName == null || "null".equalsIgnoreCase(reviewerName)){
                                    reviewerName = "";
                                }
                            }else{
                                reviewerName = personFullName;
                            }
                            
                            
                      if(canViewUpdateDetails){%>
														<tr align="left" class="copy" bgcolor="<%=strBgColor1%>">
															<%}else{%>
														
														<tr align="left" class="copy" bgcolor="<%=strBgColor1%>"
															onmouseover="className='TableItemOn'"
															onmouseout="className='TableItemOff'">
															<%}%>

															<td>&nbsp;</td>
															<td class="copy">
																<%if("true".equals((String)review.get("canUserViewReviewer"))){%>
																<%=reviewerName%> <%}%>
															</td>
															<td class="copy">
																<%
                              
                              String comment = "";
                              if(otherRevAttachmntDescription != null && otherRevAttachmntDescription.length() > 67){
                                  comment =   otherRevAttachmntDescription.substring(0,64)+"...";
                              }else{
                                  comment  =  otherRevAttachmntDescription;
                              }
                              
                              %> <%=comment%>
															</td>

															<td class='copy' align='center'><logic:equal
																	name="review" property="privateAttachmentFlag"
																	value="Y">
																	<img
																		src="<bean:write name='ctxtPath'/>/coeusliteimages/checked.gif">
																</logic:equal></td>
															<td>&nbsp;</td>
															<td class='copy' align='center'>&nbsp;</td>
															<td>&nbsp;</td>
															<td class='copy'>
																<%//String link = "javaScript:openOtherReviewerComments('" +index1 +"')";%>
																<%String link = "javaScript:viewAttachment('" +review.get("attachmentId") +"','" +review.get("personId") +"')";%>
																<html:link href="<%=link%>">
																	<bean:message key="protocolReviewComments.view" />
																</html:link>
															</td>
															<td class='copy'>
																<%if(userRightInReviewComment != null && userRightInReviewComment.equals("modifyComment")){%>
																<html:link href="<%=editLink%>">
																	<bean:message key="protocolReviewComments.modify" />
																</html:link> <%}%>
															</td>
															<td class='copy'>
																<%removeAttachment = "javaScript:deleteAttachment('" +review.get("attachmentId") +"','" +review.get("protocolNumber") +"','" +review.get("sequenceNumber") +"','" +review.get("submissionNumber") +"')";
                              %> <%if(userRightInReviewComment != null && userRightInReviewComment.equals("modifyComment")){%>
																<html:link href="<%=removeAttachment%>">
																	<bean:message key="protocolReviewComments.remove" />
																</html:link> <%}%>
															</td>

														</tr>
														</tr>
														<%if(canViewUpdateDetails){%>
														<tr bgcolor="<%=strBgColor1%>">
															<td colspan="11">
																<table width='95%' class="copy" align="center">
																	<tr>
																		<%String createUserName = (String)review.get("createUserName");
                                       if(createUserName != null && !"".equals(createUserName)){
                                       %>
																		<td width='50%'>
																			<%
                                         String createTimestamp = "";
                                         String displayFormatedDate = "";
                                          if(createUserName != null && createUserName.length() > 20){
                                            
                                            createUserName =   createUserName.substring(0,20)+"...";
                                           
                                          }
                                          createTimestamp = (String)review.get("createTimestamp");
                                          if(createTimestamp != null &&  !(createTimestamp.trim().equals(""))){
                                            java.sql.Timestamp timeStamp = java.sql.Timestamp.valueOf(createTimestamp);
                                            displayFormatedDate = dateFormat.format(timeStamp);
                                          }
                                          %>&nbsp;<b>Create by</b>
																			&nbsp; <%=createUserName%>&nbsp;<b>at</b>&nbsp;<%=displayFormatedDate%>

																		</td>
																		<%}%>

																		<td width='50%'>
																			<%
                                          String updateUserName = (String)review.get("updateUserName"); 
                                            if(updateUserName != null && updateUserName.length() > 20){
                                              
                                            updateUserName =   updateUserName.substring(0,20)+"...";
                                          } 
                                          String updateTimestamp = (String)review.get("updateTimestamp");
                                          String displayFormatedDate = "";
                                          if(updateTimestamp != null &&  !(updateTimestamp.trim().equals(""))){
                                              java.sql.Timestamp timeStamp = java.sql.Timestamp.valueOf(updateTimestamp);
                                              displayFormatedDate = dateFormat.format(timeStamp);
                                          }
                                          %> &nbsp;<b>Last Update by</b>
																			&nbsp; <%=updateUserName%>&nbsp;<b>at</b>&nbsp;<%=displayFormatedDate%>
																		</td>
																	</tr>
																</table>
															</td>
														</tr>
														<%}%>
														<% attachmntCount1++; 
                      }
                      %>
														<%         
                      attachmntIndex1++;
                      %>
													</logic:iterate>
												</logic:present>

												<%-- Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end--%>
												<table width="100%" border="0" cellpadding="2"
													cellspacing="0" class="tabtable">
													<tr align="center">
														<td class="theader" align="left" height="20" colspan="11">
															<bean:message
																key="protocolReviewComments.reviewCOmmentsForPastSubmissions" />
														</td>
														<logic:empty name="pastSubmissionComments">
															<table width="100%" border="0" cellpadding="0"
																cellspacing="0" class="tabtable">
																<tr align="center">
																	<td class="copy" align="center" height="20"><bean:message
																			key="protocolReviewComments.noReviewCommentsFromPastSubmission" />

																	</td>
																</tr>
															</table>
														</logic:empty>
														<logic:notEmpty name="pastSubmissionComments">
													</tr>

													<tr align="left">

														<td class="theader"></td>
														<td class="theader"><bean:message
																key="protocolReviewComments.pastSubmissionType" /></td>
														<td class="theader"></td>
														<td class="theader"><bean:message
																key="protocolReviewComments.pastSubmissionDate" /></td>
														<td class="theader"></td>
														<td class="theader"><bean:message
																key="protocolReviewComments.lastActionPerformed" /></td>
													</tr>
													<% 
                          int pastSubmissionCount=0;
                          %>

													<logic:iterate id="pastSubmissionReviewComments"
														name="pastSubmissionComments"
														type="edu.mit.coeus.irb.bean.ProtocolSubmissionInfoBean">

														<%  if (count%2 == 0)
                               strBgColor = "#D6DCE5";
                               else
                               strBgColor="#c6cbd2";
                               %>


														<tr align="left" class="copy" bgcolor="<%=strBgColor%>"
															onmouseover="className='TableItemOn'"
															onmouseout="className='TableItemOff'">

															<td width="1%">
																<%String divName="Panel"+pastSubmissionCount;%>
																<div id='<%=divName%>'>

																	<%String divlink = "javascript:showHide(1,'"+pastSubmissionCount+"')";%>
																	<html:link href="<%=divlink%>">
																		<%String imagePlus=request.getContextPath()+"/coeusliteimages/plus.gif";%>
																		<html:img src="<%=imagePlus%>" border="0" />
																	</html:link>
																</div> <% String divsnName="hidePanel"+pastSubmissionCount;%>
																<div id='<%=divsnName%>' style='display: none;'>

																	<% String divsnlink = "javascript:showHide(2,'"+pastSubmissionCount+"')";%>
																	<html:link href="<%=divsnlink%>">
																		<%String imageMinus=request.getContextPath()+"/coeusliteimages/minus.gif";%>
																		<html:img src="<%=imageMinus%>" border="0" />
																	</html:link>
																</div>

															</td>
															<td class="copy">
																<% String submissionDesc = pastSubmissionReviewComments.getSubmissionTypeDesc(); %>
																<%=submissionDesc %>

															</td>
															<td>&nbsp;</td>
															<td class="copy">
																<% String submissionDate = pastSubmissionReviewComments.getSubmissionDate().toString();%>
																<%=submissionDate %>
															</td>
															<td>&nbsp;</td>
															<td class="copy">
																<% String actionDesc = pastSubmissionReviewComments.getActionTypeDesc(); %>
																<%=actionDesc %>

															</td>

														</tr>
														<tr>
															<td colspan="7" align="right">
																<%String divisionName="pan"+pastSubmissionCount;%>
																<div id='<%=divisionName%>' style='display: none;'>
																	<table width="100%" border="0" cellpadding="2"
																		cellspacing="0" class="tabtable">
																		<tr>
																			<td colspan="11" align="left" valign="top"><table
																					width="100%" height="20" border="0" cellpadding="0"
																					cellspacing="0" class="tableheader">
																					<tr>
																						<td><bean:message
																								key="protocolReviewComments.listOfReviewComments" />
																						</td>
																					</tr>
																				</table></td>
																		</tr>

																		<logic:empty property="protocolReviewComments"
																			name="pastSubmissionReviewComments">
																			<tr align="left">
																				<td colspan="10" class="copy" align="center">
																					<bean:message
																						key="protocolReviewComments.noReviewCommentsForPastSubmission" />
																				</td>

																			</tr>
																		</logic:empty>

																		<%String minuteEntryForSubmission = "";
                                                 int reviewCommentIndex =0;
                                                 String personName = "";
                                                 int submissionId;
                                                 %>
																		<logic:notEmpty name="pastSubmissionReviewComments"
																			property="protocolReviewComments">
																			<tr align="left">
																				<td width="1%" class="theader"></td>
																				<td width="15%" class="theader"><bean:message
																						key="protocolReviewComments.reviewer" /></td>
																				<td width="45%" class="theader"><bean:message
																						key="protocolReviewComments.comment" /></td>
																				<td width="4%" class="theader"><bean:message
																						key="protocolReviewComments.label.private" /></td>
																				<td width="2%" class="theader"></td>
																				<td width="4%" class="theader"><bean:message
																						key="protocolReviewComments.label.final" /></td>
																				<td width="3%" class="theader"></td>
																				<td width="8%" class="theader" colspan="3"></td>

																			</tr>
																			<logic:iterate id="reviewCommentsForPastSubmission"
																				name="pastSubmissionReviewComments"
																				property="protocolReviewComments"
																				type="edu.mit.coeus.irb.bean.MinuteEntryInfoBean">




																				<% 
                                                         //UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                                                         if (count%2 == 0)
                                                         strBgColor = "#D6DCE5";
                                                         else
                                                         strBgColor="#c6cbd2";
                                                         // COEUSDEV-161: Changes to IRB reviewer comments functionality - Access peermissions
//                                                         if( loggedInPersonId.equalsIgnoreCase(reviewCommentsForPastSubmission.getPersonId()) || userHasAdminRole.equalsIgnoreCase("Y") || reviewCommentsForPastSubmission.isFinalFlag()){
                                                       //Modified for Commented for COEUSDEV-237 : Investigator cannot see review comments - Start
                                                      //if(CoeusLiteConstants.YES.equalsIgnoreCase(userHasAdminRole) || isEditLinkEnabled){
                                                      //Commented for COEUSDEV-303 : Review View menu items are not enabled if the user has reviewer role - Start
                                                      //Only the viewable comments are avaialble in the collection
                                                      // if(CoeusLiteConstants.YES.equalsIgnoreCase(userHasAdminRole) || canViewPastSubmissionComments){//COEUSDEV-237:End
                                                      // if( loggedInPersonId.equalsIgnoreCase(reviewCommentsForPastSubmission.getPersonId()) 
                                                      //  || reviewCommentsForPastSubmission.isFinalFlag()){
                                                      //COEUSDEV-303 : End
                                                         if(reviewCommentsForPastSubmission.getMinuteEntry() != null){
                                                         minuteEntryForSubmission = reviewCommentsForPastSubmission.getMinuteEntry().toString();
                                                         
                                                         }
                                                         submissionId=reviewCommentsForPastSubmission.getSubmissionNumber();
                                                         if(reviewCommentsForPastSubmission.getPersonId()!=null && reviewCommentsForPastSubmission.getPersonId().length()>0){
                                                         mapGetPersonName = (mapGetPersonName == null)? new HashMap() : mapGetPersonName;
                                                         if(mapGetPersonName!=null){
                                                             personName = (String) mapGetPersonName.get(reviewCommentsForPastSubmission.getPersonId());
                                                         }
                                                         
                                                        // personName=(personName.length()>0)mess:"";
                                                       }
                                                        
                                                        
                                                         %>
																				<%if(reviewCommentsForPastSubmission.isUserAdmin()){%>
																				<tr align="left" class="copy"
																					bgcolor="<%=strBgColor%>" <%}else{%>
																					<tr align="left" class="copy" bgcolor="<%=strBgColor%>" onmouseover="className='TableItemOn'"  onmouseout="className='TableItemOff'">
                                                         <%}%>
                                                             <%--COEUSQA-2291 : Start--%>
                                                             <%if(("true").equals(reviewCommentsForPastSubmission.canUserViewRewierName())){
                                                                 reviewerName =reviewCommentsForPastSubmission.getReviewerName();%>
                                                             <%}else{
                                                                reviewerName = "";
                                                             }%>
                                                             <%if(reviewerName == null){
                                                             reviewerName = "";
                                                             }%>
                                                             <%--COEUSQA-2291 : End--%>
                                                             <td>
                                                                 &nbsp;
                                                             </td>
                                                             <td class ="copy">
                                                                 <%=reviewerName%>
                                                             </td>
                                                             <td class ="copy">
                                                                 
                                                                   <%
                                                                 
                                                                 String comment = "";
                                                                 if(minuteEntryForSubmission != null && minuteEntryForSubmission.length() > 67){
                                                                 comment =   minuteEntryForSubmission.substring(0,64)+"...";
                                                                 }else{
                                                                 comment  =  minuteEntryForSubmission;
                                                                 }
                                                                 
                                                                 %>                                                                 
                                                                 <%=comment%>
                                                             
                                                                 <span id="<%= pastSubmissionReviewComments.getSubmissionNumber()%><%= reviewCommentIndex %>" style="display:none"><%=minuteEntryForSubmission%></span>
                                                             </td>
                                                             
                                                             <td  class='copy' align='center'>
                                                                 <logic:equal name="reviewCommentsForPastSubmission" property="privateCommentFlag" value="true">
                                                                     <img src="<bean:write name='ctxtPath'/>/coeusliteimages/checked.gif">
                                                                 </logic:equal>
                                                                 
                                                             </td>
                                                             <td>
                                                                 &nbsp;
                                                             </td>
                                                             <td  class='copy' align='center'>
                                                                 <logic:equal name="reviewCommentsForPastSubmission" property="finalFlag" value="true">
                                                                     <img src="<bean:write name='ctxtPath'/>/coeusliteimages/checked.gif">
                                                                 </logic:equal>
                                                                 
                                                             </td>
                                                             <td>
                                                                 &nbsp;
                                                             </td>
                                                             <td class='copy'>
                                                          
                                                             </td>
                                                             <td class='copy'>
                                                          
                                                             </td>
                                                             <td  class='copy'>
               
                                                                 <%String link = "javaScript:openPastSubDesc('" +pastSubmissionReviewComments.getSubmissionNumber()+reviewCommentIndex+"')";%>                                                                 
                                                                 
                                                                 <html:link href="<%=link%>">
                                                                     <bean:message key="protocolReviewComments.view"/>
                                                                 </html:link>
                                                             </td>
                                                         </tr>
                                                        <%if(reviewCommentsForPastSubmission.isUserAdmin()){%>
                                                         <tr bgcolor="<%=strBgColor%>">
                                                             <td colspan="11">
                                                                 <!-- <table width='95%' class="table" align="center">-->
                                                                 <table width='98%' class="copy" align="center">
                                                                     <tr>                              
                                                                         <%String createUserName = reviewCommentsForPastSubmission.getCreateUserName();
                                                                         if(createUserName != null && !"".equals(createUserName)){
                                                                         %>
                                                                          <td width='50%'>
                                                                             <%
                                                                             String createTimestamp = "";
                                                                             String displayFormatedDate = ""; 
                                                                             if(createUserName != null && createUserName.length() > 20){
                                                                                 createUserName =   createUserName.substring(0,20)+"...";
                                                                             } 
                                                                             if(reviewCommentsForPastSubmission.getCreateTimestamp() != null){
                                                                             createTimestamp = reviewCommentsForPastSubmission.getCreateTimestamp().toString();
                                                                             }
                                                                             if(createTimestamp != null &&  !(createTimestamp.trim().equals(""))){
                                                                                 java.sql.Timestamp timeStamp = java.sql.Timestamp.valueOf(createTimestamp);
                                                                                 displayFormatedDate = dateFormat.format(timeStamp);
                                                                             }
                                                                             %><b>Create by</b> &nbsp; <%=createUserName%>&nbsp;<b>at</b>&nbsp;<%=displayFormatedDate%> 
                                                                             
                                                                         </td>
                                                                         <%}%>
                                                                    
                                                                         
                                                                         <td width='50%'>
                                                                             <%
                                                                             String updateUserName = reviewCommentsForPastSubmission.getUpdateUserName();
                                                                             if(updateUserName != null && updateUserName.length() > 20){
                                                                             updateUserName =   updateUserName.substring(0,20)+"...";
                                                                             } 
                                                                             String updateTimestamp = "";
                                                                             if(reviewCommentsForPastSubmission.getUpdateTimestamp() != null){
                                                                                 updateTimestamp = reviewCommentsForPastSubmission.getUpdateTimestamp().toString();
                                                                             }
                                                                             String displayFormatedDate = "";
                                                                             if(updateTimestamp != null &&  !(updateTimestamp.trim().equals(""))){
                                                                             java.sql.Timestamp timeStamp = java.sql.Timestamp.valueOf(updateTimestamp);
                                                                             displayFormatedDate = dateFormat.format(timeStamp);
                                                                             }
                                                                             %>
                                                                             <b>Last Update by</b> &nbsp; <%=updateUserName%>&nbsp;<b>at</b>&nbsp;<%=displayFormatedDate%> 
                                                                         </td>
                                                                     </tr>
																	</table>
															</td>
														</tr>
														<%}%>
														<% count++;  reviewCommentIndex++; 
                                                         //}
                                                         //}%>
													</logic:iterate>
													</logic:notEmpty>
													<%-- Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start--%>
													<tr>
														<td colspan="11" align="left" valign="top">
															<table width="100%" height="20" border="0"
																cellpadding="0" cellspacing="0" class="tableheader">
																<tr>
																	<td><bean:message
																			key="protocolReviewComments.listOfReviewAttachments" />
																	</td>
																</tr>
															</table>
														</td>
													</tr>

													<logic:empty property="protocolReviewAttachments"
														name="pastSubmissionReviewComments">
														<tr align="left">
															<td colspan="10" class="copy" align="center"><bean:message
																	key="protocolReviewComments.noReviewAttachmentsForPastSubmission" />
															</td>

														</tr>
													</logic:empty>

													<%
                                                 String attachmntDescription = "";
                                                 int reviewAttachmentIndex =0;
                                                 String attachmntPersonName = "";
                                                 int pastAttchSubmissionId;
                                                 %>
													<logic:notEmpty property="protocolReviewAttachments"
														name="pastSubmissionReviewComments">
														<tr align="left">
															<td width="1%" class="theader"></td>
															<td width="15%" class="theader"><bean:message
																	key="protocolReviewComments.reviewer" /></td>
															<td width="45%" class="theader">Description</td>
															<td width="4%" class="theader"><bean:message
																	key="protocolReviewComments.label.private" /></td>
															<td width="2%" class="theader"></td>
															<td width="3%" class="theader"></td>
															<td width="8%" class="theader" colspan="3"></td>

														</tr>
														<logic:iterate id="reviewAttachmentsForPastSubmission"
															name="pastSubmissionReviewComments"
															property="protocolReviewAttachments"
															type="edu.mit.coeus.irb.bean.ReviewAttachmentsBean">
															<% 
                                                         if (count%2 == 0)
                                                         strBgColor = "#D6DCE5";
                                                         else
                                                         strBgColor="#c6cbd2";                                                         
                                                         
                                                         if(reviewAttachmentsForPastSubmission.getDescription() != null){
                                                         attachmntDescription = reviewAttachmentsForPastSubmission.getDescription().toString();
                                                         }
                                                         
                                                         pastAttchSubmissionId=reviewAttachmentsForPastSubmission.getSubmissionNumber();
                                                         
                                                        if(reviewAttachmentsForPastSubmission.getPersonId()!=null && reviewAttachmentsForPastSubmission.getPersonId().length()>0){
                                                            mapAttachPersonName = (mapAttachPersonName == null)? new HashMap() : mapAttachPersonName;
                                                                if(mapAttachPersonName!=null){
                                                                    attachmntPersonName = (String) mapAttachPersonName.get(reviewAttachmentsForPastSubmission.getPersonId());
                                                                }
                                                        }
                                                         %>
															<%if(reviewAttachmentsForPastSubmission.isUserAdmin()){%>
															<tr align="left" class="copy" bgcolor="<%=strBgColor%>"
																<%}else{%>
																<tr align="left" class="copy" bgcolor="<%=strBgColor%>" onmouseover="className='TableItemOn'"  onmouseout="className='TableItemOff'">
                                                         <%}%>
                                                             
                                                             <%if(("true").equals(reviewAttachmentsForPastSubmission.getCanUserViewReviewerName())){
                                                                 reviewerName =reviewAttachmentsForPastSubmission.getReviewerName();%>
                                                             <%}else{
                                                                reviewerName = "";
                                                             }%>
                                                             <%if(reviewerName == null){
                                                             reviewerName = "";
                                                             }%>
                                                             
                                                             <td>
                                                                 &nbsp;
                                                             </td>
                                                             <td class ="copy">
                                                                 <%=reviewerName%>
                                                             </td>
                                                             <td class ="copy">
                                                                   <%String comment = "";
                                                                 if(attachmntDescription != null && attachmntDescription.length() > 67){
                                                                 comment =   attachmntDescription.substring(0,64)+"...";
                                                                 }else{
                                                                 comment  =  attachmntDescription;
                                                                 }%>                                                                 
                                                                 <%=comment%>
                                                             
                                                                 <span id="<%= pastSubmissionReviewComments.getSubmissionNumber()%><%= reviewAttachmentIndex %>" style="display:none"><%=attachmntDescription%></span>
                                                             </td>
                                                             
                                                             <td  class='copy' align='center'>
                                                                 <logic:equal name="reviewAttachmentsForPastSubmission" property="privateAttachmentFlag" value="Y">
                                                                     <img src="<bean:write name='ctxtPath'/>/coeusliteimages/checked.gif">
                                                                 </logic:equal>
                                                                 
                                                             </td>
                                                             <td>
                                                                 &nbsp;
                                                             </td>
                                                             <td class='copy'>
                                                          
                                                             </td>
                                                             <td class='copy'>
                                                          
                                                             </td>
                                                             <td  class='copy'>
                                                                 <%//String link = "javaScript:openPastSubDesc('" +pastSubmissionReviewComments.getSubmissionNumber()+reviewAttachmentIndex+"')";%>
                                                                 <%String link = "javaScript:viewAttachment('" +reviewAttachmentsForPastSubmission.getAttachmentNumber() +"','" +reviewAttachmentsForPastSubmission.getPersonId()+"')";%>
                                                                 <html:link href="<%=link%>">
                                                                     <bean:message key="protocolReviewComments.view"/>
                                                                 </html:link>
                                                             </td>
                                                         </tr>
                                                        <%if(reviewAttachmentsForPastSubmission.isUserAdmin()){%>
                                                         <tr bgcolor="<%=strBgColor%>">
                                                             <td colspan="11">
                                                                 <table width='98%' class="copy" align="center">
                                                                     <tr>                              
                                                                         <%String createUserName = reviewAttachmentsForPastSubmission.getCreateUserName();
                                                                         if(createUserName != null && !"".equals(createUserName)){
                                                                         %>
                                                                          <td width='50%'>
                                                                             <%
                                                                             String createTimestamp = "";
                                                                             String displayFormatedDate = ""; 
                                                                             if(createUserName != null && createUserName.length() > 20){
                                                                                 createUserName =   createUserName.substring(0,20)+"...";
                                                                             } 
                                                                             if(reviewAttachmentsForPastSubmission.getCreateTimestamp() != null){
                                                                             createTimestamp = reviewAttachmentsForPastSubmission.getCreateTimestamp().toString();
                                                                             }
                                                                             if(createTimestamp != null &&  !(createTimestamp.trim().equals(""))){
                                                                                 java.sql.Timestamp timeStamp = java.sql.Timestamp.valueOf(createTimestamp);
                                                                                 displayFormatedDate = dateFormat.format(timeStamp);
                                                                             }
                                                                             %><b>Create by</b> &nbsp; <%=createUserName%>&nbsp;<b>at</b>&nbsp;<%=displayFormatedDate%> 
                                                                             
                                                                         </td>
                                                                         <%}%>
                                                                    
                                                                         
                                                                         <td width='50%'>
                                                                             <%
                                                                             String updateUserName = reviewAttachmentsForPastSubmission.getUpdateUserName();
                                                                             if(updateUserName != null && updateUserName.length() > 20){
                                                                             updateUserName =   updateUserName.substring(0,20)+"...";
                                                                             } 
                                                                             String updateTimestamp = "";
                                                                             if(reviewAttachmentsForPastSubmission.getUpdateTimestamp() != null){
                                                                                 updateTimestamp = reviewAttachmentsForPastSubmission.getUpdateTimestamp().toString();
                                                                             }
                                                                             String displayFormatedDate = "";
                                                                             if(updateTimestamp != null &&  !(updateTimestamp.trim().equals(""))){
                                                                             java.sql.Timestamp timeStamp = java.sql.Timestamp.valueOf(updateTimestamp);
                                                                             displayFormatedDate = dateFormat.format(timeStamp);
                                                                             }
                                                                             %>
                                                                             <b>Last Update by</b> &nbsp; <%=updateUserName%>&nbsp;<b>at</b>&nbsp;<%=displayFormatedDate%> 
                                                                         </td>
                                                                     </tr>
												</table>
												</td>
												</tr>
												<%}%>
												<% count++;  reviewAttachmentIndex++; %>
												</logic:iterate>
												</logic:notEmpty>
												<%-- Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end--%>
											</table>

											</div>
											</td>
										</tr>

										<% pastSubmissionCount++; %>
										</logic:iterate>
										</logic:notEmpty>
										<!-- Review Complete Screen - Start-->

										<!-- Review Complete Screen - End-->
									</table>

									<%-- Added for COEUSQA-2288 : Ability for Reviewer to mark Review Comments complete and make recommendation - Start --%>

									</div>
									<%-- Added for COEUSQA-2288 : Ability for Reviewer to mark Review Comments complete and make recommendation - End --%>
									<%if(canCompleteReview){%>
									<div id='open_review_complete_panel'>
										<table width="100%" height="100%" border="0" cellpadding="0"
											cellspacing="0" class="tabtable">

											<table width="100%" border="0" cellpadding="0"
												cellspacing="0">

												<tr class='copy' align="left">
													<td colspan="2"><font color="red"> <html:errors />
													</font></td>
												</tr>
												<tr>
													<td height="20%" align="left" valign="top" class="theader">
														<bean:message
															key="protocolReviewComplete.header.completeReview" />
													</td>
												</tr>
											</table>


											<table width="100%" border="0" cellpadding="3"
												cellspacing="0" class="tabtable">
												<tr>
													<td class="copybold">&nbsp;&nbsp; <bean:message
															key="protocolReviewComplete.msg.reviewComplete" />
													</td>
												</tr>
												<%if(!isAllReviewCommentsFinal){%>
												<tr>
													<td class="copybold">&nbsp;&nbsp; <bean:message
															key="protocolReviewComplete.msg.commentsNotFinal" />
													</td>
												</tr>
												<%}%>
												<tr>
													<td class="copybold">&nbsp;&nbsp; <bean:message
															key="protocolReviewComplete.msg.selectRecommended" />
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <html:select
															name="reviewCommentsForm" property="recommendedAction"
															styleClass="textbox-long">
															<html:option value="">
																<bean:message key="generalInfoLabel.pleaseSelect" />
															</html:option>
															<html:options collection="recommendedActionList"
																property="code" labelProperty="description" />
														</html:select>
													</td>
												</tr>

											</table>
											<table width="100%" border="0" cellpadding="3"
												cellspacing="0" class="tabtable">
												<tr class="table">
													<td class="savebutton" align="center"><html:button
															property="save" value="Ok" styleClass="clsavebutton"
															onclick="javascript:reviewComplete();" /> <html:button
															property="save" value="Cancel" styleClass="clsavebutton"
															onclick="javascript:called(2,null);" />
														<%--Modified for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments --%>
													</td>
												</tr>
											</table>
										</table>
									</div>
									<%}%>

									<%}%>

									<%if(notEditable){%>
									<script>
            document.reviewCommentsForm.save.disabled = true;
            </script>
									<%}%>

									<script>
                       document.getElementById('open_review_comments_panel').style.display = 'block';
                       <%if(canCompleteReview){%>
                           document.getElementById('open_review_complete_panel').style.display = 'none'
                       <%}%>
                       <%--Added for the case#3282/3284-reviewer views and comments-start --%>
                        <% if(show!=null){ %>
                            <%if(canUserCreateReviewComments){%>
                                document.getElementById('add_link').style.display = 'block';
                                document.getElementById('open_window').style.display = 'none';
                            <%}else{%>
                                if(document.getElementById('open_window') != null){
                                  document.getElementById('open_window').style.display = 'none';
                                }
                            <%}%>
                        <%}else {%>       
                            document.getElementById('add_link').style.display = 'block';
                            if(document.getElementById('open_window') != null){
                              document.getElementById('open_window').style.display = 'none';
                            }
                        <%}%>
                        <%--Modified for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start --%>
                        <%--
                            if(showModify!=null && showModify.equalsIgnoreCase("show")){ %>
                                called('1');
                         <%}--%> 
                         <%
                            if(showModify!=null && showModify.equalsIgnoreCase("show") &&
                                showComment!=null && showComment.equalsIgnoreCase("C")){ %>
                                called('1','C');
                         <%}else if(showModify!=null && showModify.equalsIgnoreCase("show") &&
                                showComment!=null && showComment.equalsIgnoreCase("A")){%>  
                                called('1','A');
                                <%}%>
                         <%--Modified for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end --%>
                        <%--Added for the case#3282/3284-reviewer views and comments-end --%>
                    </script>
									</html:form>
									<script>
            
            <%if(request.getAttribute("setModifyFlag") == null){%>
                document.getElementById('open_attachment_window').style.display = 'none';
           <%}%>
          if(document.reviewCommentsForm.acType !=null && document.reviewCommentsForm.acType.value != 'U'){
            document.reviewCommentsForm.acType.value = 'I';
            document.reviewCommentsForm.scheduleId.value = '<%=scheduleId%>';
            document.reviewCommentsForm.protocolNumber.value = '<%=protocolNumber%>';
            document.reviewCommentsForm.sequenceNumber.value = '<%=sequenceNumber%>';   
            document.reviewCommentsForm.submissionNumber.value = '<%=submissionNumber%>';    
            <%-- Modified for COEUSQA-2290 : New Minute entry type for Review Comments - Start 
            Changed the Entry Type Code from 3 to code from the parameter(IRB_MINUTE_TYPE_REVIEWER_COMMENT)
            document.reviewCommentsForm.entryTypeCode.value = 3;
            <%--COEUSQA-2290 : End--%>
            document.reviewCommentsForm.entryTypeCode.value = <%=minuteTypeCode%>;
          }
          LINK = "<%=request.getContextPath()%>/saveReviewComments.do"; 
          FORM_LINK = document.reviewCommentsForm;
          PAGE_NAME = "<bean:message key="protocolReviewComments.reviewComments"/>";
          function dataChanged(){
            DATA_CHANGED = 'true';
            document.reviewCommentsForm.protoContingencyCode.value = "";
          }
          linkForward(errValue);
          
        </script>
									<script>
      var help = ' <bean:message key="helpTextProtocol.ReviewComments"/>';
      help = trim(help);
      if(help.length == 0) {
        document.getElementById('helpText').style.display = 'none';
      }
      function trim(s) {
            return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
      }  
        </script>
</body>
</html:html>
