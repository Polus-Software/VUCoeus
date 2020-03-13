<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<%@page import="java.util.Vector,edu.mit.coeuslite.utils.ComboBoxBean,edu.mit.coeus.s2s.phshumansubject.PHSHumanSubjectS2SAction,java.util.List,edu.mit.coeus.s2s.phshumansubject.bean.PHSHumanSubjectsBean,edu.mit.coeus.s2s.phshumansubject.bean.PHSHumanSubjectDelayedStudyBean,edu.mit.coeus.s2s.phshumansubject.bean.PHSHumanSubjectAttachments"%>
<%@page import="java.util.ArrayList"%>

<jsp:useBean id="phsHumanSubjectDelayedStudyBeanLst" scope="session" class="edu.mit.coeus.s2s.phshumansubject.bean.PHSHumanSubjectDelayedStudyBean" />
<jsp:useBean id="PHSHumanSubject" scope="session" class="edu.mit.coeus.s2s.phshumansubject.bean.PHSHumanSubjectsBean" />
<jsp:useBean id="phsHumanSubjectAttachments" scope="session" class="java.util.ArrayList" />
<jsp:useBean id="lstUserAttachmentS2S" scope="session" class="java.util.ArrayList" />
<jsp:useBean id="phsHumnSubjtDlyStdyList" scope="session" class="java.util.ArrayList" />
<html:html>
    <head>

        <script language="JavaScript">
            
            <% ArrayList attachmntList = (ArrayList)request.getAttribute("phsHumanSubjectAttachments");
               ArrayList humanSbjctAttachmntList = (ArrayList)request.getAttribute("lstUserAttachmentS2S");
               ArrayList delayedAttachmntList = (ArrayList)request.getAttribute("phsHumnSubjtDlyStdyList");
                boolean isAttachmentAdded = false;
                boolean isOtherAttAdded = false;
                boolean isHumansbctRcrdAdded = false;
                if(attachmntList != null){
                    for (int i = 0; i < attachmntList.size(); i++) {
                      PHSHumanSubjectAttachments  attachmnt = (PHSHumanSubjectAttachments)attachmntList.get(i);
                        if(attachmnt.getPhsHumnsubjtAttachmentType() == 1){
                            isAttachmentAdded = true;                        
                        }else if(attachmnt.getPhsHumnsubjtAttachmentType() == 2){
                            isOtherAttAdded = true;
                        }                  
                    } 
                }
                if(humanSbjctAttachmntList != null){
                    if(humanSbjctAttachmntList.size() > 0){
                        isHumansbctRcrdAdded = true;
                    }
                }
                if(delayedAttachmntList != null){
                    if(delayedAttachmntList.size() > 0){
                        isHumansbctRcrdAdded = true;
                    }
                }
                             

           PHSHumanSubjectsBean phsHumanSubjectsBean = (PHSHumanSubjectsBean)request.getAttribute("phsHumanSubjectinfo");
           List<String> exemptList = phsHumanSubjectsBean.getExemptionNumberList();  
           if(exemptList == null){
               exemptList =  new ArrayList<String>();
           }
           String isHumanTrue  = phsHumanSubjectsBean.getIsHuman();                
           String  showOtherAttchment= (String)request.getAttribute("showOtherAttchment");
           String  showAddAttchment= (String)request.getAttribute("showAddAttachment");
           java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("MM/dd/yyyy hh:mm a");
           java.sql.Timestamp timeStamp; 
           String updateTimeStamp = "";
           String mode=(String)session.getAttribute("mode"+session.getId()); 
           boolean modeValue=false;
           if(mode!=null && !mode.equals("")){   
            if(mode.equalsIgnoreCase("display")){
              modeValue=true;
            }
          }
        %>
            function enableAdd() {
                document.phsHumanSubjectsBean.Add.disabled = false;
            }
            function enableStudyRecordSave() {
                document.phsHumanSubjectsBean.StudyRecordSave.disabled = false;
            }
            function enableOtherRequestedInfoSave() {
                document.phsHumanSubjectsBean.OtherRequestedInfoSave.disabled = false;
            }
            function enableDelydOnstStdySave() {
                document.getElementById('delydOnstFilename').innerHTML = "";
                document.phsHumanSubjectsBean.DelydOnstStdySave.disabled = false;
            }
            function clearData(type) {
                if (type == "D") {
                    document.getElementById("updatedelayonset").style.display = "none";
                    document.getElementById("adddelayonset").style.display = "inline-block";
                    document.phsHumanSubjectsBean.DelydOnstStdySave.disabled = true;
                    document.getElementById("isAnticipatedCt").checked = false;
                    document.getElementById('delydOnstFilename').innerHTML = "";
                    document.phsHumanSubjectsBean.studyTitle.value = "";
                    document.phsHumanSubjectsBean.delayedFormFile.value = "";
                }
                else {
                    document.phsHumanSubjectsBean.StudyRecordSave.disabled = true;
                    document.phsHumanSubjectsBean.s2sFormFile.value = "";
                    document.phsHumanSubjectsBean.description.value = "";
                }
            }
           function showHumanSpecimenAttchmnt() {
                var isOtherAttAdded = '<%=isOtherAttAdded%>';
                var isInvolveHumanSpecimenList = document.getElementsByName("isInvolveHumanSpecimen");
                for( var i= 0; i<isInvolveHumanSpecimenList.length;i++){
                    if(isInvolveHumanSpecimenList[i].checked){
                      var isInvolveHumanSpecimen =  isInvolveHumanSpecimenList[i].value;
                    }
                }
                if(isInvolveHumanSpecimen == 'Y'){
                    if(document.getElementById("isHumanSpecimenTrue") != undefined){
                        document.getElementById("isHumanSpecimenTrue").style.display = "block";
                    }                        
                }else if(document.getElementById("isHumanSpecimenTrue") != undefined){
                        document.getElementById("isHumanSpecimenTrue").style.display = "none";
                }
                if(isOtherAttAdded == "true"){
                    if( document.getElementById("otherRequiredAttchmnt")!= undefined){
                        document.getElementById("otherRequiredAttchmnt").style.display = "none";
                    }  
                }else if(isOtherAttAdded == "false"){
                    if( document.getElementById("otherRequiredAttchmnt")!= undefined){
                        document.getElementById("otherRequiredAttchmnt").style.display = "block";
                    }
                }
            }
            function viewAttachment(ProposalNumber, attachmentNumber, attachmentType, contentType) {
                window.open("<%=request.getContextPath()%>/viewHumanOtherAttachment.do?ProposalNumber=" + ProposalNumber + "&attachmentNumber=" + attachmentNumber + "&attachmentType=" + attachmentType + "&contentType=" + contentType);
            }
            function viewHumanSubjectAttachment(ProposalNumber, formNumber, contentType) {
                window.open("<%=request.getContextPath()%>/viewPHSHumanSubjectDetails.do?ProposalNumber=" + ProposalNumber + "&formNumber=" + formNumber + "&contentType=" + contentType);
            }
            function deleteHumanSubjectAttachment(formNumber) {
               if (confirm("Do you want to Remove?")==true){
                document.phsHumanSubjectsBean.acType.value = "D";
                document.phsHumanSubjectsBean.formNumber.value = formNumber;
                document.phsHumanSubjectsBean.action = "<%=request.getContextPath()%>/deletePHSHumanSubjectS2SDetails.do?formNumber="+formNumber;
                document.phsHumanSubjectsBean.submit();
               }
            }
            function deleteAttachment(attachmentNumber) {
              if (confirm("Do you want to Remove?")==true){                
                document.phsHumanSubjectsBean.acType.value = "D";
                document.phsHumanSubjectsBean.attachmentNumber.value = attachmentNumber;
                document.phsHumanSubjectsBean.action = "<%=request.getContextPath()%>/saveHumanOtherAttachment.do";
                document.phsHumanSubjectsBean.submit();
              }
            }
            function deleteDlyAttachment(studyNumber, anticipated) {
              if (confirm("Do you want to Remove?")==true){
                document.phsHumanSubjectsBean.acType.value = "D";
                document.phsHumanSubjectsBean.studyNumber.value = studyNumber;
                document.phsHumanSubjectsBean.isAnticipatedCt.value = anticipated;
                document.phsHumanSubjectsBean.action = "<%=request.getContextPath()%>/saveDelayedOnsetStudies.do";
                document.phsHumanSubjectsBean.submit();
              }
            }
            
            function saveHeaderDetail(){                
                var isHumanTrue ='<%=isHumanTrue%>';
                var isHumansbctRcrdAdded ='<%=isHumansbctRcrdAdded%>';                
                var isInvolveHumanSpecimenList = document.getElementsByName("isInvolveHumanSpecimen");
                for( var i= 0; i<isInvolveHumanSpecimenList.length;i++){
                    if(isInvolveHumanSpecimenList[i].checked){
                      var isInvolveHumanSpecimen =  isInvolveHumanSpecimenList[i].value;
                    }
                }
                var isAttachmentAdded = '<%=isAttachmentAdded%>';
                if(isHumanTrue == 'N'){
                    if(isAttachmentAdded == 'true' && isInvolveHumanSpecimen == 'Y' || isInvolveHumanSpecimen == 'N' ){                        
                        document.phsHumanSubjectsBean.action = "<%=request.getContextPath()%>/savePHSHumanSubHeaderDetails.do";
                        document.phsHumanSubjectsBean.submit();
                    }
                    else if(isInvolveHumanSpecimen == '' || isInvolveHumanSpecimen == undefined){
                        alert("Please answer the Question 'Does the proposed research involve human specimens and/or data?'");
                    }
                    else {
                        alert("Please attach an explanation of why the application does not involve human subjects research under 'Add Documents'.");
                    }
                }else if(isHumanTrue == 'Y'){
                    if(isHumansbctRcrdAdded == 'false'){
                        alert("Please add at least one Attachment under the 'Human Subject Study Record(s)' or 'Delayed Onset Study(ies)' section.")
                    }
                    else{
                        document.phsHumanSubjectsBean.action = "<%=request.getContextPath()%>/savePHSHumanSubHeaderDetails.do";
                        document.phsHumanSubjectsBean.submit();
                    }                    
                }
            }
            function saveOtherAttachment(attachmentType) {  
                if (attachmentType == 'other') {
                    document.phsHumanSubjectsBean.phsHumnsubjtAttachmentType.value = "2";                    
                }else {
                    document.phsHumanSubjectsBean.phsHumnsubjtAttachmentType.value = "1";
                }
                document.phsHumanSubjectsBean.acType.value = "I";
                document.phsHumanSubjectsBean.action = "<%=request.getContextPath()%>/saveHumanOtherAttachment.do";
                document.phsHumanSubjectsBean.submit();
            }
            function saveS2sFormAttachment() {
                document.phsHumanSubjectsBean.action = "<%=request.getContextPath()%>/savePHSHumanSubjectS2SDetails.do";
                document.phsHumanSubjectsBean.submit();
            }
            function saveDelayedonsetStudty(actype) {
               if(document.phsHumanSubjectsBean.studyTitle.value != null && document.phsHumanSubjectsBean.studyTitle.value != ""){
                    if(validateStudytitle(actype)){
                            if (actype == "I") {
                                document.phsHumanSubjectsBean.acType.value = "I";
                                document.phsHumanSubjectsBean.action = "<%=request.getContextPath()%>/saveDelayedOnsetStudies.do";
                                document.phsHumanSubjectsBean.submit();
                            }else if (actype == "U") {
                                document.phsHumanSubjectsBean.acType.value = "U";
                                document.phsHumanSubjectsBean.action = "<%=request.getContextPath()%>/saveDelayedOnsetStudies.do";
                                document.phsHumanSubjectsBean.submit();
                            }
                   }else{
                      alert("Please insert an Unique Title.");
                     document.getElementById("studyTitle").focus();
                   }
               }else{
                  alert("Please provide a Study Title.");
                  document.getElementById("studyTitle").focus();
               }
            }
            function editdelydOnsetstdy(anticipated, studyTitle, fileName, studyNumber) {
                if (studyTitle == "null")
                {
                    studyTitle = null;
                }
                document.getElementById("updatedelayonset").style.display = "inline-block";
                document.getElementById("adddelayonset").style.display = "none";
                document.getElementById("isAnticipatedCt").checked = false;
                if (anticipated == "Y") {
                    document.getElementById("isAnticipatedCt").checked = true;
                }
                document.phsHumanSubjectsBean.studyNumber.value = studyNumber;
                document.phsHumanSubjectsBean.studyTitle.value = studyTitle;
                document.getElementById('delydOnstFilename').innerHTML = fileName;
            }
            function showHide(val, value) {
                var panel = 'Panel' + value;
                var pan = 'pan' + value;
                var hidePanel = 'hidePanel' + value;
                if (val == 1) {
                    document.getElementById(panel).style.display = "none";
                    document.getElementById(hidePanel).style.display = "block";
                    document.getElementById(pan).style.display = "block";
                }else if (val == 2) {
                    document.getElementById(panel).style.display = "block";
                    document.getElementById(hidePanel).style.display = "none";
                    document.getElementById(pan).style.display = "none";
                }
            }
            
            
           <%
           String filepath = (String)session.getAttribute("DOWNLOAD_PATH_HUMAN_SUBJECT");
           ArrayList phsList = new ArrayList();
           phsList = (ArrayList)request.getAttribute("phsHumnSubjtDlyStdyList");
           StringBuffer studyTitles = new StringBuffer();
           StringBuffer studyNumbers = new StringBuffer();
           for (int i = 0; i < phsList.size(); i++) {
                PHSHumanSubjectDelayedStudyBean bean = (PHSHumanSubjectDelayedStudyBean)phsList.get(i);  
                if (studyTitles.length() > 0) {
                    studyTitles.append(',');
                }
                if (studyNumbers.length() > 0) {
                    studyNumbers.append(',');
                }
                studyTitles.append('"').append(bean.getStudyTitle()).append('"');
                studyNumbers.append('"').append(bean.getStudyNumber()).append('"');
           }%>
               
           function validateStudytitle(actype){ 
               var uniqTitle = true;
               var studytitleArray = [ <%= studyTitles.toString() %> ];
               var studyNumberArray = [ <%= studyNumbers.toString() %> ];
               var title = document.phsHumanSubjectsBean.studyTitle.value;
               var number = document.phsHumanSubjectsBean.studyNumber.value;
               if(actype == "I"){
                    for(var value = 0;value<studytitleArray.length;value++){
                      if(title == studytitleArray[value]){
                          uniqTitle = false;
                      }
                    }
               }
               else{
                    for(var key = 0;key<studyNumberArray.length;key++){
                      if(number != studyNumberArray[key]){
                        if(title == studytitleArray[key]){
                             uniqTitle = false;
                        }
                      }  
                    }
               }

              return  uniqTitle;
       }
         function downloadHumanStdyForm() {
               var filepath = '<%=filepath.toString()%>';
               window.open(filepath);                    
            }
        </script>

    </head>
    <body> 
    <html:form  action="/getPHSHumanSubjectS2SDetails.do"  enctype="multipart/form-data"> 
        <table width="100%" height="100%"  border="0" cellpadding="0" cellspacing="0" class="table">
            <tr>
                <td height="25px" align="left" align="top" class="theader header_font "> <span class="padding-left-header">PHS Human Subjects and Clinical Trails Information</span>   </td>
            </tr>
            <tr>
                <td>
                    <div class="topDiv copybold">
                        <span>Please add Human Subjects on the Special Review tab and identify the appropriate approval status and exemption.
                            <br><br>
                            The following items are taken from the special Review tab and displayed here for your reference.
                            Any changes to these fields must be made on the Special Review tab and may impact the data items you are required to complete on this form.
                        </span>
                        <br><br>
                        <div>
                            <table  class="copybold">
                                <tr width="100%">
                                    <td width="50%">
                                        Are Human Subjects Involved?
                                    </td>
                                    <td width="50%" class="padding_left_radio">
                                <html:radio property='isHuman' value="Y" disabled="true"/>Yes
                                <html:radio property='isHuman' value="N" disabled="true"/>No
                                </td>
                                </tr>
                                <tr width="100%">
                                    <td width="50%">
                                        Is the Project Exempt from Federal regulations?
                                    </td>
                                    <td width="50%" class="padding_left_radio">
                                <html:radio property='exemptFedReg' value="Y" disabled="true"/>Yes
                                <html:radio property='exemptFedReg' value="N" disabled="true"/>No

                                </td>
                                </tr>
                                <tr width="100%">
                                    <td width="50%">
                                        Exemption number:
                                    </td>
                                    <td width="50%" class="padding_left">
                                        <%
                                            for(int size = 1; size <= 8 ; size++){                                        
                                                String checked = (exemptList.contains("E"+size) == true ? "Checked" : "");                                             
                                        %>
                                        <input type="checkbox" name="<%="E"+size%>" value="<%="E"+size%>" <%=checked%> disabled="true"><%=size%>                                
                                        <%
                                         }
                                        %>                                
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </div>
                </td>
            </tr>
            <tr>
                <td  height="10px"></td>
            </tr>
            <%if(isHumanTrue.equals("N")){%>
            <tr>
                <td class="copybold padding-left-header" style ="font-size:1.2em">
                    <span>If No to Human Subjects</span>
                </td>
            </tr>
            <tr>
                <td> 
                    <table class="copybold">
                        <tr>
                            <td>
                                Does the proposed research involve human specimens and/or data?
                            </td>
                            <td>
                        <html:radio property='isInvolveHumanSpecimen' name="phsHumanSubjectsBean" value="Y" onclick="showHumanSpecimenAttchmnt();" disabled="<%=modeValue%>"/>Yes
                        <html:radio property='isInvolveHumanSpecimen' name="phsHumanSubjectsBean" value="N" onclick="showHumanSpecimenAttchmnt();" disabled="<%=modeValue%>"/>No 
                </td>
            </tr>
        </table>
        </td>
        </tr>
        <tr >
            <td>     
                <div  class="copybold">
                    <div id="isHumanSpecimenTrue" class="border_box">
                        <table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0" class="tabtable">
                            <tr>
                                <td  height="30px" align="left" class="theader header_font"> Add Documents </td> 
                            </tr>
                            <%if(!modeValue){%>
                            <tr>
                                <td>
                                    <table width="100%" border="0" cellpadding="1" cellspacing="10" class="copybold">
                                        <tr>
                                            <td>                                            
                                                File:
                                            </td>
                                            <td>                                            
                                        <html:file property="humanSbjtFormFile" onchange="enableAdd();" disabled="<%=modeValue%>"/>
                                </td>
                                <td align="right"> 
                            <html:button property="Add" value="Add" styleClass="clsavebutton" disabled="true" onclick="saveOtherAttachment('document');"/> 
                            </td>
                            </tr>
                        </table>
                        </td>
                        </tr> 
                        <%}%>
                        </table>
                        <div>
                            <%if(showAddAttchment.equals("true")){%>
                            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tabtable">
                                <logic:present name = "phsHumanSubjectAttachments"> 
                                    <logic:notEmpty name = "phsHumanSubjectAttachments">    
                                        <tr class="white_border">        
                                            <td height="25px" align="left" class="theader padding-left-header">List of All Proposal Attachments</td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <table width="100%" border="0" cellpadding="0" cellspacing="0" class="sortable">
                                                    <tr class="padding-left-header">
                                                        <td width='35%' class="theader">
                                                            FileName
                                                        </td>
                                                        <td width='45%' class="theader">
                                                            Uploaded date by update user
                                                        </td>
                                                        <td width='10%' class="theader">
                                                        </td>
                                                        <td width='10%' class="theader">
                                                        </td>     
                                                    </tr>
                                                    <%
                                                     int count = 0;
                                                     String strBgColor = "#DCE5F1";
                                                     String viewlink;
                                                     String deletelink;
                                                    %>
                                                    <logic:iterate id="phsHumanSubjectAttachmentList" name="phsHumanSubjectAttachments" type="edu.mit.coeus.s2s.phshumansubject.bean.PHSHumanSubjectAttachments">
                                                        <logic:equal name="phsHumanSubjectAttachmentList" property="phsHumnsubjtAttachmentType" value="1">
                                                            <% 
                                                                if (count%2 == 0)
                                                                    strBgColor = "#D6DCE5";
                                                                else
                                                                    strBgColor="#DCE5F1"; 
  
                                                            %>
                                                            <tr  bgcolor="<%=strBgColor%>" onmouseover="className = 'TableItemOn'"  mouseout="className='TableItemOff'" onmouseout="className = 'TableItemOff'" >
                                                                <td width='35%' class='copy'>
                                                            <bean:write name="phsHumanSubjectAttachmentList" property="fileName"/>
                                                            </td>
                                                            <td width='45%' class='copy'>
                                                                <%
                                                                   timeStamp = phsHumanSubjectAttachmentList.getUpdateTimestamp();
                                                                   if(timeStamp != null && !timeStamp.equals("")){
                                                                       updateTimeStamp = dateFormat.format(timeStamp);
                                                                   }
                                                                %>
                                                                <%=updateTimeStamp%> &nbsp;by 
                                                                <%=phsHumanSubjectAttachmentList.getUpdateUser()%>
                                                            </td>
                                                            <td width='10%'class='copy'>
                                                                <%viewlink = "javaScript:viewAttachment('"+phsHumanSubjectAttachmentList.getProposalNumber()+"','"+phsHumanSubjectAttachmentList.getAttachmentNumber()+"','"+phsHumanSubjectAttachmentList.getPhsHumnsubjtAttachmentType()+"','"+phsHumanSubjectAttachmentList.getContentType()+"');";%>
                                                            <html:link href="<%=viewlink%>"> View</html:link>
                                                            </td>  
                                                            <td width='10%'class='copy'>
                                                                <%if(!modeValue){%>
                                                                <%deletelink = "javaScript:deleteAttachment('"+phsHumanSubjectAttachmentList.getAttachmentNumber()+"');";%>
                                                            <html:link href="<%=deletelink%>"> Remove</html:link> 
                                                                <%}%>
                                                            </td>
                                                             
                                                            </tr>
                                                            <%count++;%>
                                                        </logic:equal>
                                                    </logic:iterate>
                                                </table> 
                                            </td>
                                        </tr>                                                     
                                    </logic:notEmpty> 
                                </logic:present>
                            </table>
                            <%}%>               
                        </div>
                    </div>
                </div>
            </td>
        </tr>
        <%}else if(isHumanTrue.equals("Y")){%>
        <tr>
            <td class="copybold padding-left-header" style ="font-size:1.2em">
                    <span>If Yes to Human Subjects</span>
             </td>
        </tr>
        <tr>
            <td class="copybold padding-left-header">
                <div style="padding:5px;text-align:justify;">
                    <span>
                        You must add at least 1 of the following:
                        <ul>
                            <li>A Human Subjects Study record for each proposed Human Subject Study by adding the description, choosing the file, and clicking the 'Add' button.</li>
                            <li>A Delayed Onset Study in the Delayed Studies section(s) by adding the study title, choosing the file, and clicking the 'Add' button.<br><i>Delayed onset studies are those for which there is no well-defined plan for human subject involvement at the time of submission, per agency policies on delayed onset studies.  For delayed onset studies, you will provide the study name and a justification for omission of the human subject study information.</i></li>
                        </ul>
                    </span>
                </div><br>
             </td>
        </tr>
        <tr>               
            <td>
                <table width="100%"  border="0" align="center" cellpadding="3" cellspacing="0">
                    <tr class="white_border">
                        <td  height="30px" align="left" class="theader header_font"> Human Subject Study Record(s)
                            <span style="float:right;"><html:button property="Save" value="click here to extract Human Subject Study Record Attachment" onclick="javascript:downloadHumanStdyForm();"/></span></td>
                    </tr>
                </table>
            </td>   
        </tr>
        <logic:present name="attachedS2SError">
            <tr>
                <td style=" padding-top: 10px; color: red;"  valign="center">
                    <ul>
                        <li>
                            The file you uploaded is not Valid. Please upload the appropriate Grants.gov form file.
                        </li>
                    </ul>
                </td>
            </tr>
        </logic:present>
        <tr class="topDiv">
            <td>
                <%if(!modeValue){%>
                <div id="attachmentId"> 
                    <table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0" class="tabtable">

                        <tr>
                            <td>
                                <table width="100%" border="0" cellpadding="1" cellspacing="5" class="copybold">
                                    <tr >
                                        <td>
                                            Description:
                                        </td>
                                        <td>
                                    <html:textarea styleClass="textbox-longer" property="description"/>
                            </td>
                        </tr>
                        <tr>
                            <td>                                            
                                File:
                            </td>
                            <td>                                            
                        <html:file property="s2sFormFile" onchange="enableStudyRecordSave();" disabled="<%=modeValue%>"/>
                        </td>
                        </tr>
                    </table>
            </td>
        </tr>
        <tr>
            <td  class='savebutton'> 
        <html:button property="StudyRecordSave" value="Add" styleClass="clsavebutton" disabled="true" onclick="saveS2sFormAttachment();"/> 
        <html:button property="Cancel" value="Cancel" styleClass="clsavebutton" onclick="clearData('S');" disabled="<%=modeValue%>"/>
        </td>
        </tr>
        </table>
        </div>
        <%}%>  
        <logic:present name="phsHumanSubjectFileNotFound">
        <script>
            alert("The Human Subjects PDF is not found in the expected path.");
        </script>
        </logic:present>   
        
        <logic:present name="phsHumanSubjectFileError">
         <script>
            alert("Error on Fetching Human Subjects PDF.");
        </script>   
        </logic:present>
        <div>
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tabtable">
                <logic:present name = "lstUserAttachmentS2S"> 
                    <logic:notEmpty name = "lstUserAttachmentS2S">    
                        <tr class="white_border">        
                            <td height="25px" align="left" class="theader padding-left-header">List of Human Subject Study Records</td>
                        </tr>
                        <tr>
                            <td>
                                <table width="100%" border="0" cellpadding="0" cellspacing="0" class="sortable">
                                    <tr class="padding-left-header">
                                        <td width='30%' class="theader padding-left-header">
                                            FileName
                                        </td>
                                        <td width='30%' class="theader">
                                            
                                        </td>
                                        <td width='7%' class="theader">
                                            PDF
                                        </td>
                                        <td width='7%' class="theader">
                                            XML
                                        </td>
                                        <td width='9%' class="theader">
                                        </td>
                                        <td width='9%' class="theader">
                                        </td>
                                        <td width='8%' class="theader">
                                        </td>
                                    </tr>
                                    <%
                                     int count = 0;
                                     String strBgColor = "#DCE5F1";
                                     String viewlink;
                                     String deleteLink;%>
                                    <logic:iterate id="lstUserAttachmentS2SList" name="lstUserAttachmentS2S" type="edu.mit.coeus.s2s.phshumansubject.bean.PHSHumanSubjectFormBean">  
                                        <% 
                                            if (count%2 == 0)
                                                strBgColor = "#D6DCE5";
                                            else
                                                strBgColor="#DCE5F1"; 
                                                     
                                        %>
                                        <tr bgcolor="<%=strBgColor%>" onmouseover="className = 'TableItemOn'"  mouseout="className='TableItemOff'" onmouseout="className = 'TableItemOff'" >
                                            <td width='30%' class='copy padding-left-header'>
                                                <%String divName="Panel"+count;%>
                                                <div id='<%=divName%>'>
                                                    <%String divlink = "javascript:showHide(1,'"+count+"')";%>
                                                    <html:link href="<%=divlink%>">                                                                   
                                                        <%String imagePlus=request.getContextPath()+"/coeusliteimages/plus.gif";%>
                                                        <html:img src="<%=imagePlus%>" border="0"/>                                                                    
                                                    </html:link> &nbsp;&nbsp;
                                                    <bean:write name="lstUserAttachmentS2SList" property="pdfFileName"/> 
                                                </div>
                                                <% String divsnName="hidePanel"+count;%>
                                                <div id='<%=divsnName%>' style='display:none;'>
                                                    <% String divsnlink = "javascript:showHide(2,'"+count+"')";%>
                                                    <html:link href="<%=divsnlink%>">                                                                   
                                                        <%String imageMinus=request.getContextPath()+"/coeusliteimages/minus.gif";%>
                                                        <html:img src="<%=imageMinus%>" border="0"/>
                                                    </html:link>  &nbsp;&nbsp;
                                                    <bean:write name="lstUserAttachmentS2SList" property="pdfFileName"/> 
                                                </div>
                                            </td>
                                            <td width='30%' class='copy'>

                                            </td>
                                            <td width='7%'class='copy '>
                                                <%String imagePdf = request.getContextPath()+"/coeusliteimages/none.gif";
                                                       if(lstUserAttachmentS2SList.getPdfFileName() != null){
                                                       imagePdf=request.getContextPath()+"/coeusliteimages/complete.gif";
                                                       }%>   
                                        <html:img src="<%=imagePdf%>" border="0"/>                 
                                        </td>
                                        <td width='7%'class='copy'>
                                            <%String imageXml=request.getContextPath()+"/coeusliteimages/none.gif";
                                                if(lstUserAttachmentS2SList.getFormName() != null){
                                                    imageXml=request.getContextPath()+"/coeusliteimages/complete.gif";
                                                }
                                            %>
                                        <html:img src="<%=imageXml%>" border="0"/>
                                        </td>
                                        <td width='9%'class='copy'>
                                            <%viewlink = "javaScript:viewHumanSubjectAttachment('"+lstUserAttachmentS2SList.getProposalNumber()+"','"+lstUserAttachmentS2SList.getFormNumber()+"','pdf');";%>
                                        <html:link href="<%=viewlink%>">View Form</html:link>
                                        </td>   
                                        <td width='9%'class='copy'>
                                            <%viewlink = "javaScript:viewHumanSubjectAttachment('"+lstUserAttachmentS2SList.getProposalNumber()+"','"+lstUserAttachmentS2SList.getFormNumber()+"','xml');";%>
                                        <html:link href="<%=viewlink%>">View XML</html:link>
                                        </td> 
                                        <td width='9%'class='copy'>
                                            <%if(!modeValue){%>
                                                <%deleteLink = "javaScript:deleteHumanSubjectAttachment('"+lstUserAttachmentS2SList.getFormNumber()+"');";%>
                                                <html:link href="<%=deleteLink%>">Remove</html:link>
                                             <%}%>
                                        </td>
                                        </tr>
                                        <tr>
                                            <td  colspan="9">
                                                <%String divisionName="pan"+count;%>
                                                <div id='<%=divisionName%>' style='display:none;'>
                                                    <table width="100%" height="100%"  border="0" class="sortable">
                                                        <tr class="theader">
                                                            <td>Details</td> 
                                                        </tr>
                                                        <tr class="copy">
                                                            <td><b>Description:</b>&nbsp;<bean:write name="lstUserAttachmentS2SList" property="description"/></td>  
                                                        </tr>
                                                        <tr class="copy">
                                                            <td><b>Update User:</b>&nbsp; 
                                                                <%=lstUserAttachmentS2SList.getPdfUpdateUser()%>
                                                        </tr>
                                                        <tr class="copy">
                                                            <td><b>PDF Last Updated:</b>&nbsp;  
                                                                <%
                                                                    timeStamp = lstUserAttachmentS2SList.getUpdateTimestamp();
                                                                    if(timeStamp != null && !timeStamp.equals("")){
                                                                        updateTimeStamp = dateFormat.format(timeStamp);
                                                                    }
                                                                %>
                                                                <%=updateTimeStamp%>
                                                            </td>  
                                                        </tr>
                                                        <tr class="copy">
                                                            <td><b>NameSpace:</b>&nbsp;<bean:write name="lstUserAttachmentS2SList" property="namespace"/></td>  
                                                        </tr>
                                                    </table>
                                                </div>
                                            </td>     
                                        </tr>
                                        <%count++;%>
                                    </logic:iterate>
                                </table> 
                            </td>
                        </tr>                                                     
                    </logic:notEmpty> 
                </logic:present>
            </table>
        </div>
        </td>
        </tr>
        <tr>
            <td  height="10px"></td>
        </tr>
        <tr>
            <td>
                <div>
                    <table width="100%"  border="0" align="center" cellpadding="3" cellspacing="0">
                        <tr>
                            <td  height="30px" class="theader header_font">
                                Delayed Onset Study(ies) 
                            </td> 
                        </tr>
                    </table>
                </div>    
            </td> 
        </tr>
        <tr class="topDiv">
            <td>
                <%if(!modeValue){%>   
                <table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0" class="tabtable">
                    <tr>
                        <td>
                            <table width="100%" border="0" cellpadding="1" cellspacing="5" class="copybold">
                                <tr>
                                    <td>
                                        Study Title:
                                    </td>
                                    <td>
                                        <html:text styleClass="textbox-longer" property="studyTitle" styleId="studyTitle"/>
                                    </td>
                               </tr>
                                <tr>
                                    <td>                                            
                                        Anticipated Clinical Trail?
                                    </td>
                                    <td><html:checkbox styleId="isAnticipatedCt" property="isAnticipatedCt" value="Y"/></td>
                                </tr>
                                <tr>
                                    <td>Justification:</td>
                                    <td> <label id="delydOnstFilename"> </label>                                           
                                         <html:file property="delayedFormFile" onchange="enableDelydOnstStdySave();" disabled="<%=modeValue%>"/>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>  
                    <tr>
                        <td class='savebutton' width="100%"> 
                                <span id="updatedelayonset" style="display:none;"><html:button property="DelydOnstStdyUpdate" value="update" styleClass="clsavebutton" onclick="saveDelayedonsetStudty('U');"/> </span>
                                <span id="adddelayonset"><html:button property="DelydOnstStdySave" value="Add" styleClass="clsavebutton" disabled="true" onclick="saveDelayedonsetStudty('I');"/></span>
                        <html:button property="Cancel" value="Cancel" styleClass="clsavebutton" onclick="clearData('D');" /> 
                        </td>
                    </tr>
             </table>
        <%}%>
        <div>
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tabtable">
                <logic:present name = "phsHumnSubjtDlyStdyList"> 
                    <logic:notEmpty name = "phsHumnSubjtDlyStdyList">    
                        <tr class="white_border">        
                            <td height="25px" align="left" class="theader padding-left-header">List of Delayed Onset Study Records</td>
                        </tr>
                        <tr>
                            <td>
                                <table width="100%" border="0" cellpadding="0" cellspacing="0" class="sortable">
                                    <tr>
                                        <td width='15%' class="theader padding-left-header">
                                            Study Title
                                        </td>
                                        <td width='12%' class="theader" align="center">
                                            Anticipated
                                        </td>
                                        <td width='28%' class="theader ">
                                            FileName
                                        </td>
                                        <td width='28%' class="theader">
                                            Uploaded date by update user
                                        </td>
                                       
                                        <td width='5%' class="theader">
                                        </td>    
                                        <td width='7%' class="theader">
                                        </td>  
                                    </tr>
                                    <%
                                     int count = 0;
                                     String strBgColor = "#DCE5F1";
                                     String deletelink;
                                     String viewlink;
                                     String editlink;%>
                                    <logic:iterate id="phsHumnSubjtDlyStdy" name="phsHumnSubjtDlyStdyList" type="edu.mit.coeus.s2s.phshumansubject.bean.PHSHumanSubjectDelayedStudyBean">                                             
                                        <% 
                                            if (count%2 == 0)
                                                strBgColor = "#D6DCE5";
                                            else
                                                strBgColor="#DCE5F1";  
                                        %>
                                        <tr  bgcolor="<%=strBgColor%>" onmouseover="className = 'TableItemOn'"  mouseout="className='TableItemOff'" onmouseout="className = 'TableItemOff'" >
                                            <td width='15%'class='copy padding-left-header'>
                                                 <%=phsHumnSubjtDlyStdy.getStudyTitle()%>
                                            </td>
                                            <td width='12%'class='copy' align="center">
                                                <%  String completeImage= request.getContextPath()+"/coeusliteimages/checked.gif";%>
                                                <logic:equal name="phsHumnSubjtDlyStdy" property="isAnticipatedCt" value="Y">
                                                    <html:img src="<%=completeImage%>"/>  
                                                </logic:equal> 
                                            </td>
                                            <td width='28%' class='copy'>
                                                <%if(!modeValue){%>
                                                    <%editlink = "javaScript:editdelydOnsetstdy('"+phsHumnSubjtDlyStdy.getIsAnticipatedCt()+"','"+phsHumnSubjtDlyStdy.getStudyTitle()+"','"+phsHumnSubjtDlyStdy.getFileName()+"','"+phsHumnSubjtDlyStdy.getStudyNumber()+"');";%>
                                                    <html:link href="<%=editlink%>"> <%=phsHumnSubjtDlyStdy.getFileName()%> </html:link>
                                                <%}else{%>
                                                    <%=phsHumnSubjtDlyStdy.getFileName()%>
                                                <%}%>
                                            </td>
                                            <td width='28%' class='copy'>
                                                <%
                                             timeStamp = phsHumnSubjtDlyStdy.getUpdateTimestamp();
                                             if(timeStamp != null && !timeStamp.equals("")){
                                                 updateTimeStamp = dateFormat.format(timeStamp);
                                             }
                                                %>
                                                <%=updateTimeStamp%>&nbsp;by <%=phsHumnSubjtDlyStdy.getUpdateUser()%>
                                            </td>
                                            <td width='5%'class='copy'>
                                                <%viewlink = "javaScript:viewAttachment('"+phsHumnSubjtDlyStdy.getProposalNumber()+"','"+phsHumnSubjtDlyStdy.getStudyNumber()+"','"+3+"','"+phsHumnSubjtDlyStdy.getContentType()+"');";%>
                                            <html:link href="<%=viewlink%>"> View</html:link>
                                            </td>  
                                            <td width='7%'class='copy'>
                                                <%if(!modeValue){%>
                                                <%deletelink = "javaScript:deleteDlyAttachment('"+phsHumnSubjtDlyStdy.getStudyNumber()+"','"+phsHumnSubjtDlyStdy.getIsAnticipatedCt()+"');";%>
                                            <html:link href="<%=deletelink%>"> Remove</html:link>
                                                <%}%>
                                            </td> 
                                        </tr>
                                        <%count++;%>
                                    </logic:iterate>
                                </table> 
                            </td>
                        </tr>                                                     
                    </logic:notEmpty> 
                </logic:present>
            </table>
        </div>
        <%}%>
        </td>
        </tr>
        <tr>
            <td  height="10px"></td>
        </tr>
        <div>
            <tr class="topDiv">
                <td>   
                    <table width="100%"  border="0" align="center" cellpadding="3" cellspacing="0" class="tabtable">
                        <tr>
                            <td height="30px" align="left" class="theader header_font padding-left-header">
                                Other Requested Information 
                            </td> 
                        </tr>
                    </table>
                </td>    
            </tr>
            <tr class="topDiv">
                <td>
                    <div>
                        <%if(!modeValue){%>
                        <table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0" class="tabtable">
                            <tr>  
                            </tr>
                            <tr>
                                <td>
                                  <div id="otherRequiredAttchmnt"> 
                                    <table width="100%" border="0" cellpadding="1" cellspacing="10" class="copybold">
                                        <tr>
                                            <td>                                            
                                                File:
                                            </td>
                                            <td>                                            
                                                <html:file property="otherAttmntFormFile" onchange="enableOtherRequestedInfoSave();" disabled="<%=modeValue%>"/>  
                                            </td>
                                            <td align="right"> 
                                                   <html:button property="OtherRequestedInfoSave" value="Add" styleClass="clsavebutton" disabled="true" onclick="saveOtherAttachment('other');"/> 
                                            </td>
                                        </tr>
                                    </table>
                                  </div>
                </td>
            </tr>  
            </table>
            <%}%> 
            <div>
                <%if(showOtherAttchment == "true"){%>
                <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tabtable">
                    <logic:present name = "phsHumanSubjectAttachments"> 
                        <logic:notEmpty name = "phsHumanSubjectAttachments">    
                            <tr class="white_border">        
                                <td height="25px" align="left" class="theader  padding-left-header">List of Other Requested Information</td>
                            </tr>
                            <tr>
                                <td>
                                    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="sortable">
                                        <tr>
                                            <td width='35%' class="theader padding-left-header">
                                                FileName
                                            </td>
                                            <td width='45%' class="theader">
                                                Uploaded date by update user
                                            </td>
                                            <td width='10%' class="theader">
                                            </td>
                                            <td width='10%' class="theader">
                                            </td>     
                                        </tr>
                                        <%
                                         int count = 0;
                                         String strBgColor = "#DCE5F1";
                                         String deletelink;
                                         String viewlink;%>
                                        <logic:iterate id="phsHumanSubjectAttachmentList" name="phsHumanSubjectAttachments" type="edu.mit.coeus.s2s.phshumansubject.bean.PHSHumanSubjectAttachments">
                                            <logic:equal name="phsHumanSubjectAttachmentList" property="phsHumnsubjtAttachmentType" value="2">
                                                <% 
                                                    if (count%2 == 0)
                                                        strBgColor = "#D6DCE5";
                                                    else
                                                        strBgColor="#DCE5F1";  
                                                %>
                                                <tr  bgcolor="<%=strBgColor%>" onmouseover="className = 'TableItemOn'"  mouseout="className='TableItemOff'" onmouseout="className = 'TableItemOff'" >
                                                    <td width='35%' class='copy padding-left-header'>
                                                <bean:write name="phsHumanSubjectAttachmentList" property="fileName"/>
                                                </td>
                                                <td width='45%' class='copy'>
                                                    <%
                                                 timeStamp = phsHumanSubjectAttachmentList.getUpdateTimestamp();
                                                 if(timeStamp != null && !timeStamp.equals("")){
                                                     updateTimeStamp = dateFormat.format(timeStamp);
                                                 }
                                                    %>
                                                    <%=updateTimeStamp%> &nbsp;by 
                                                    <%=phsHumanSubjectAttachmentList.getUpdateUser()%>
                                                </td>
                                                <td width='10%'class='copy'>
                                                    <%viewlink = "javaScript:viewAttachment('"+phsHumanSubjectAttachmentList.getProposalNumber()+"','"+phsHumanSubjectAttachmentList.getAttachmentNumber()+"','"+phsHumanSubjectAttachmentList.getPhsHumnsubjtAttachmentType()+"','"+phsHumanSubjectAttachmentList.getContentType()+"');";%>
                                                <html:link href="<%=viewlink%>"> View</html:link>
                                                </td>  
                                                <td width='10%'class='copy'>
                                                    <%if(!modeValue){%>
                                                    <%deletelink = "javaScript:deleteAttachment('"+phsHumanSubjectAttachmentList.getAttachmentNumber()+"');";%>
                                                <html:link href="<%=deletelink%>"> Remove</html:link> 
                                                    <%}%>
                                                </td>
                                                </tr>
                                                <%count++;%>
                                            </logic:equal>
                                        </logic:iterate>
                                    </table> 
                                </td>
                            </tr>                                                     
                        </logic:notEmpty> 
                    </logic:present>
                </table>
                <%}%>
            </div>
        </div>

        </td>
        </tr>
        <tr>
            <td  height="8px"></td>
        </tr>
        <%if(!modeValue){%>
        <tr>
            <td class='savebutton'>
        <html:button property="Save" value="Save" styleClass="clsavebutton" disabled="<%=modeValue%>" onclick="javascript:saveHeaderDetail();"/>
        </td>
        </tr>
        <%}%>
        </table>
        <html:hidden property="acType"/>
        <html:hidden property="phsHumnsubjtAttachmentType"/>
        <html:hidden property="attachmentNumber"/>
        <html:hidden property="studyNumber"/>
        <html:hidden property="isHuman"/>
        <html:hidden property="isAnticipatedCt"/>
        <html:hidden property="formNumber"/>
        <html:hidden property="headerAcType"/>
        
    </html:form>
    <script>
        showHumanSpecimenAttchmnt();       
    </script>
</body>
</html:html>
