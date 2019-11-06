<%-- 
    Document   : ProposalReview
    Created on : Feb 1, 2010, 4:48:58 PM
    Author     : sharathk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="edu.mit.coeuslite.utils.bean.WebTxnBean, edu.utk.coeuslite.propdev.bean.EPSProposalHeaderBean, java.util.*"%>
<%@page import="edu.mit.coeuslite.utils.SessionConstants, edu.mit.coeus.bean.PersonInfoBean, edu.mit.coeus.utils.dbengine.*, java.sql.*"%>
<%@page import="edu.mit.coeus.utils.UtilFactory, java.text.SimpleDateFormat, edu.mit.coeus.bean.*"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel=stylesheet href="<%=request.getContextPath()%>/coeuslite/mit/utils/css/coeus_styles.css" type="text/css">
        <style>
            #mbox{background-color:#eee; padding:8px; border:2px outset #666;}
            #mbm{font-family:sans-serif;font-weight:bold;float:right;padding-bottom:5px;}
            #ol{background-image: url('../coeuslite/mit/utils/scripts/modal/overlay.png');}
            .dialog {display:none}

            * html #ol{background-image:none; filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src="../coeuslite/mit/utils/scripts/modal/overlay.png", sizingMethod="scale");}

        </style>
        <style>
            .deleteRow{font-weight:bold;color:#CC0000;background-color:white;}
            .addRow{font-weight:bold;background-color:white;}
            .rowHeight{height:25px;}
        </style>
        <script src="<%=request.getContextPath()%>/coeuslite/mit/utils/scripts/modal/modaldbox.js"></script>
        <script language="javascript">
            var cannedReportIndex = new Array();//stores the row Index of table for Canned Comments
            var cannedReportId = new Array();//stores the report Id for Canned Comments
            var commentToDelete = new Array();
            var rowNum = -1;
            var insertFrom = 0;

            var dataChanged = false;
            var formName = "proposalReview";
            var commentsTable = "tabComments";
            var commentsTableBody = "tabCommentsBody";
            var hiddenTD = "tdHidden";
            var commentRow = "row";

            function showDialog(divId){
                width = document.getElementById(divId).style.pixelWidth;
                height = document.getElementById(divId).style.pixelHeight;
                sm(divId,width,height);
            }

            function closeCannedComments(){
                var iframeDocument;
                var formsLength = 0;
                if(document.getElementById('iframeId').contentDocument){
                    //Mozilla
                    iframeDocument = document.getElementById('iframeId').contentDocument;
                }else {
                    //IE
                    iframeDocument = window.frames['0'].document;
                }
                formsLength = iframeDocument.forms[0].length;
                var elemName;
                var desc;
                var rowData = new Array(2);
                for(i=0; i<formsLength ; i++){
                    elemName = "";//check"+i;
                    if(iframeDocument.forms['reviewComments'].elements[i].name.indexOf('check', 0) > -1){
                        elemName = iframeDocument.forms['reviewComments'].elements[i].name;
                        if(iframeDocument.forms['reviewComments'].elements[elemName].checked){
                            desc = iframeDocument.forms['reviewComments'].elements[elemName].value;
                            rowData[0] = desc.substring(0, desc.indexOf(':'));
                            rowData[1] = desc.substring(desc.indexOf(':')+1, desc.length);

                            //cannedReportIndex[cannedReportIndex.length] = document.getElementById("tabComments").rows.length;
                            //cannedReportId[cannedReportId.length] = rowData[0];
                            cannedReportIndex[cannedReportIndex.length] = rowNum + 1;
                            cannedReportId[cannedReportId.length] = rowData[0];
                            addRow(rowData);
                        }
                    }
                }
                //document.proposalReview.reviewComments.value=comment;
                
                hm('cannedComments');

            }

            function closeTextComment(){
                elemText = document.getElementById('textComment');
                rowData = new Array(2);
                rowData[0] = "";
                rowData[1] = elemText.value;
                addRow(rowData);
                hm('textCommentDiv');
            }

            function addRow(cellArr){
                //alert(cellArr);
                tableElement = document.getElementById(commentsTable);
                //rowNum = tableElement.rows.length;
                rowNum = rowNum + 1;
                //alert(rowNum);
                var newRow   = tableElement.insertRow(tableElement.rows.length);
                newRow.setAttribute("id", commentRow+rowNum);
                newRow.className = "rowline addRow rowHeight";
                for(cols=0; cols < cellArr.length; cols++) {
                    // Insert a cell in the row at index
                    var newCell  = newRow.insertCell(cols);
                    // Append a text node to the cell
                    if(cellArr[cols].length > 0){
                        var newText  = document.createTextNode(cellArr[cols]);
                        newCell.appendChild(newText);
                    }
                }
                linkElem = document.createElement("a");
                linkElem.setAttribute("href", "javascript:deleteRow(null,"+rowNum+")");
                linkElem.setAttribute("alt", "Delete");
                linkElem.setAttribute("title", "Delete");

                imgElem = document.createElement("img");
                imgElem.setAttribute("src", "../coeusliteimages/none.gif");
                imgElem.setAttribute("border", "0");

                linkElem.appendChild(imgElem);
                newCell  = newRow.insertCell(2);
                newCell.appendChild(linkElem);
                
                dataChanged = true;
            }

            function deleteRow(commentId, rowId){
                tableElement = document.getElementById(commentsTableBody);
                rowElem = document.getElementById(commentRow+rowId);
                    //tableElement.removeChild(rowElem);
                    var className = rowElem.className;
                    if(className.indexOf("addRow") > 0){
                        //Aded Row Delete
                        res = confirm("Are you sure you want to delete comment : "+rowElem.childNodes.item(1).innerHTML);
                        if(res) {
                            tableElement.removeChild(rowElem);
                        }
                    }else {
                        rowElem.className="rowline deleteRow rowHeight";
                        //<a href="javascript:undoDelete(15, 4)" alt="Delete" title="Delete"><img border="0" src="../coeusliteimages/none.gif"></a>
                        innerhtml = rowElem.deleteCell(2);
                        newCell = rowElem.insertCell(2);
                        newCell.innerHTML = "<a href='javascript:undoDelete("+commentId+","+rowId+")' alt='Undo' title='Undo'>Undo</a>";
                        return;
                    }
                    //document.getElementById("myTD").innerHTML = "newText"
                    //tableElement.deleteRow(rowId);
                    //alert("index:"+cannedReportIndex+"; ID:"+ cannedReportId+" length:"+cannedReportId.length);
                    for(i=0; i<cannedReportIndex.length;i++){
                        if(cannedReportIndex[i] == rowId){
                            cannedReportIndex.splice(i, 1);
                            cannedReportId.splice(i, 1);
                        }
                    }
                //}
                //alert("index:"+cannedReportIndex+"; ID:"+ cannedReportId+" length:"+cannedReportId.length);
            }
            
            function undoDelete(commentId, rowId){
                if(commentId != null){
                    for(i=0; i<commentToDelete.length;i++){
                        if(commentToDelete[i] == commentId){
                            commentToDelete.splice(i, 1);
                        }
                    }
                }
                tableElement = document.getElementById(commentsTableBody);
                rowElem = document.getElementById(commentRow+rowId);
                rowElem.className="rowline";
                    innerhtml = rowElem.deleteCell(2);
                    newCell = rowElem.insertCell(2);
                    newCell.innerHTML = "<a href='javascript:markForDelete("+commentId+","+rowId+")' alt='Delete' title='Delete'><img border='0' src='../coeusliteimages/none.gif'></a>";
            }

            function selectComments(){
                if(cannedReportId.length > 0) {
                    var iframeDocument;
                    if(document.getElementById('iframeId').contentDocument){
                        //Mozilla
                        iframeDocument = document.getElementById('iframeId').contentDocument;
                    }else {
                        //IE
                        iframeDocument = window.frames['0'].document;
                    }
                     for(i=0; i< cannedReportId.length; i++) {
                        iframeDocument.forms['reviewComments'].elements["check"+cannedReportId[i]].disabled = true;
                     }
                    //JIRA COEUSDEV-610 - START
                    /*var sponsorSpecificStartIndex = window.frames['0'].getSponsorSpecificStartIndex();
                    if(sponsorSpecificStartIndex > 0) {
                        var disableSponsorSpecific = false;
                        for(i=0; i< cannedReportId.length; i++) {
                            if(cannedReportId[i] == 16){
                                //Canned Comment Code 16 selected. disable all Sponsor Specific
                                //window.frames['0'].disableSponsorSpecificComments(true);
                                disableSponsorSpecific = true;
                            }else if(cannedReportId[i] >= sponsorSpecificStartIndex){
                                //Sponsor Specific Code. disable Comment Code 16
                                //window.frames['0'].disableCommentCode16(true);
                                iframeDocument.forms['reviewComments'].elements["check16"].disabled = true;
                            }
                            iframeDocument.forms['reviewComments'].elements["check"+cannedReportId[i]].disabled = true;
                        }
                        //window.frames['0'].testcall();
                        if(disableSponsorSpecific){
                            disableSponsorSpecificComments();
                        }
                    }*/
                    //JIRA COEUSDEV-610 - END
                    
                }
            }

            function disableSponsorSpecificComments(){
                var iframeDocument;
                if(document.getElementById('iframeId').contentDocument){
                    //Mozilla
                    iframeDocument = document.getElementById('iframeId').contentDocument;
                }else {
                    //IE
                    iframeDocument = window.frames['0'].document;
                }
                var formsLength =  iframeDocument.forms['reviewComments'].length;
                var sponsorSpecificStartIndex = window.frames['0'].getSponsorSpecificStartIndex();
                for(i=sponsorSpecificStartIndex; i<formsLength ; i++){
                     iframeDocument.forms['reviewComments'].elements["check"+i].disabled = true;
                }
            }

            function markForDelete(commentId, rowId){
                commentToDelete[commentToDelete.length] = commentId;
                deleteRow(commentId, rowId);
                dataChanged = true;
            }

            function validate(){
                tableElement = document.getElementById(commentsTableBody);
                //nodeList = tableElement.childNodes;
                nodeList = tableElement.getElementsByTagName("TR");
                var commentId = 0;

                //if 'No Errors' comment exists, there shouldn't be any more Canned Comments - START
                //Check if 'No Errors' Comment Exists. comment Id = 0
                var noErrorCommentExists = false;
                for(i=0;i<cannedReportId.length;i++){
                    if(cannedReportId[i] == 0){
                        //check if the 'No Error' Comment is marked for deletion - START
                        rowElem = document.getElementById(commentRow+cannedReportIndex[i]);
                        var style = rowElem.className;
                        if(style.indexOf("deleteRow") == -1){
                            noErrorCommentExists = true;
                        }
                        //check if the 'No Error' Comment is marked for deletion - END
                        break;
                    }
                }
                if(noErrorCommentExists) {
                    for(i=0;i<nodeList.length;i++){//first TR is Header
                        rowElem = nodeList.item(i);
                        if(rowElem.getAttribute("id") != null && rowElem.getAttribute("id").indexOf(commentRow, 0) > -1){
                                style = rowElem.className;
                                if(style.indexOf("deleteRow") != -1) continue; //DO Not Consider Deleted rows Displayed
                                var value = rowElem.cells[0].innerHTML;
                                if(value.length>0 && parseInt(value)>0){
                                    //non Deleted Canned Comment Exists
                                    alert("Cannot have Canned Comments along with 'No Errors' Comment.");
                                    return false;
                                }
                        }
                    }
                }
                //if 'No Errors' comment exists, there shouldn't be any more Canned Comments - END

                for(i=0;i<nodeList.length;i++){//first TR is Header
                    rowElem = nodeList.item(i);
                    if(rowElem.getAttribute("id") != null && rowElem.getAttribute("id").indexOf(commentRow, 0) > -1){
                        if(rowElem.getAttribute("id").substring(commentRow.length, rowElem.getAttribute("id").length) >= insertFrom) {
                            style = rowElem.className;
                            if(style.indexOf("deleteRow") != -1) continue; //DO Not Consider Deleted rows Displayed
                            tdElem = document.getElementById(hiddenTD);
                            commentElem = document.createElement("input");
                            commentElem.setAttribute("type", "hidden");
                            commentElem.setAttribute("name", "comment"+commentId++);
                            commentElem.setAttribute("value",rowElem.childNodes.item(0).innerHTML+"~"+rowElem.childNodes.item(1).innerHTML);
                            tdElem.appendChild(commentElem);
                        }
                    }
                }
                //deleteCommentNum
                document.forms[formName].elements['deleteCommentNum'].value = commentToDelete.join(",");
                document.getElementById("reviewAction").disabled = false;
                return true;
            }

            function setDataChanged(){
                dataChanged = true;
            }

            function hyperlink(address){
                if(dataChanged){
                    click = confirm("Do you want to save Proposal Review?");
                    if(click){
                        //set link
                        document.forms[formName].elements['link'].value = address;
                        //Submit Form
                        submitForm(formName);
                    }else {
                        window.location = address;
                    }
                }else{
                    window.location = address;
                }
            }

            function submitForm(formName){
                if(validate()) {
                    document.forms[formName].submit();
                }
            }

            function test(){
                alert("insert From"+insertFrom +" Comments To Delete: "+commentToDelete);
                alert("index: "+cannedReportIndex+" ; ID: "+cannedReportId);
                //document.forms[formName].elements['deleteCommentNum'].value = "okay";
                //alert(document.reviewComments.deleteCommentNum.value = "okay");
            }
        </script>
        <title>Proposal Review</title>
    </head>
    <body>
        <% boolean otherUserComments = false;
        Vector userResult = null;
        %>
        <table width="1000" height="100%" border="0" cellpadding="0" cellspacing="5" class="table" align="center">
            <tr align="left" valign="top">
                <td width="98%" valign="top">

                    <!-- header START -->
                    <table width="100%" border="0" cellpadding="0" cellspacing="0" STYLE="background-image:url('<%=request.getContextPath()%>/coeusliteimages/header_background.gif');border:0">
                        <tr>
                            <td><img src="<%=request.getContextPath()%>/coeusliteimages/header_coeus2.gif" width="675" height="42"></td>
                            <td class="copysmallwhite" align="right" nowrap>
                                <%PersonInfoBean personInfo = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
                                        out.print(personInfo.getFullName());
                                %>&nbsp;&nbsp;&nbsp;
                            </td>
                        </tr>
                        <tr>
                            <td height="25" align="left" nowrap>
                                &nbsp;&nbsp;&nbsp;<a href="javascript:hyperlink('<%=request.getContextPath()%>')"><font size='2' color='#D1E5FF'><u>Back to CoeusLite</u></font></a>
                            </td>
                            <td height="25" align="right" nowrap>
                                <a href="javascript:hyperlink('<%=request.getContextPath()%>/wl/ProposalReviewStats.jsp')"><font size='2' color='#D1E5FF'><u>Back to Proposal Review</u></font></a>&nbsp;&nbsp;&nbsp;
                            </td>
                        </tr>
                    </table>
                    <!-- header END -->
            </tr>
            <tr align="left" valign="top">
                <td height="100%" valign="top">
                    
                    <table width="100%" height="100%" class="tabtable">
                        <tr>
                            <td valign="top">
                                <table width="100%" border="0" cellpadding="0" cellspacing="5">
                                    <tr>
                                        <td valign="top" height="25" class="copybold">
                                            <big>Proposal Review Details</big>
                                            <%Map userData = null;
                                            if(session.getAttribute("error") != null) {
                                                String proposalNumber = request.getParameter("proposalNumber");
                                                out.print("<br><font color='red'>"+session.getAttribute("error")+"</font>");
                                                session.removeAttribute("error");
                                                userData = (Map)session.getAttribute("userData");
                                                session.removeAttribute("userData");
                                                String errorProposalNumber = (String)userData.get("proposalNumber");
                                                if(!proposalNumber.equals(errorProposalNumber)){
                                                    //different proposal numbers, Do not displat error
                                                    userData = null;
                                                }
                                              }
                                            %>
                                        </td>
                                    </tr>
                                    <tr><td valign="top" align="center" height="80">
                                            <!-- Proposal Header Section -->
                                            <%
                                                    DBEngineImpl dbEngine = new DBEngineImpl();
                                                    String proposalNumber = request.getParameter("proposalNumber");
                                                    String routingNumber = request.getParameter("routingNumber");
                                                    String propType = request.getParameter("proposalType");
                                                    String apprSeq = request.getParameter("apprSeq");
                                                    String perId = (String)request.getParameter("perId");
                                                    if(apprSeq == null){
                                                        apprSeq = "0";
                                                    }
                                                    EPSProposalHeaderBean headerBean = null;
                                                    Map mapHeader = null;
                                                    String investigator, sponsor, period, title, leadUnit, lastUpdated;
                                                    investigator = sponsor = period = title = leadUnit = lastUpdated = "";
                                                    SimpleDateFormat dateFormatParse = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                                    SimpleDateFormat dateFormatDisplay = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
                                                    SimpleDateFormat dateDisplay = new SimpleDateFormat("MM/dd/yyyy");
                                                    java.util.Date date;
                                                    if(propType.equalsIgnoreCase("paper")){
                                                        //fn_get_inst_prop_for_dev_prop in dev2
                                                         Vector funcParam = new Vector();
                                                        funcParam.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, proposalNumber));
                                                        Vector propCountResult = dbEngine.executeFunctions("Coeus", "{ <<OUT String INST_PROP_NUM>> = call fn_get_inst_prop_for_dev_prop( <<PROPOSAL_NUMBER>>)}", funcParam);
                                                        Map map = (Map) propCountResult.get(0);
                                                        String instPropNum = (String) map.get("INST_PROP_NUM");
                                                        if(instPropNum != null && instPropNum.trim().length() > 0) {
                                                            proposalNumber = instPropNum;
                                                        }
                                                    }
                                                    boolean found = false;
                                                    //if(routingNumber != null && propType.equalsIgnoreCase("electronic")) {
                                                    //if(propType.equalsIgnoreCase("electronic")) {
                                                        HashMap hmProposalHeader = new HashMap();
                                                        WebTxnBean webTxnBean = new WebTxnBean();
                                                        hmProposalHeader.put("proposalNumber", proposalNumber);
                                                        Hashtable htProposalHeader = (Hashtable) webTxnBean.getResults(request, "getProposalHeaderData", hmProposalHeader);
                                                        Vector vecData = (Vector) htProposalHeader.get("getProposalHeaderData");
                                                        if(vecData != null && vecData.size() > 0) {
                                                            headerBean = (EPSProposalHeaderBean) vecData.get(0);
                                                            investigator = headerBean.getPersonName();
                                                            sponsor = headerBean.getSponsorCode() + " : " + headerBean.getSponsorName();
                                                            period = dateDisplay.format(headerBean.getProposalStartDate()) + " - " + dateDisplay.format(headerBean.getProposalEndDate());
                                                            title = headerBean.getTitle();
                                                            leadUnit = headerBean.getLeadUnitNumber() + " : " + headerBean.getLeadUnitName();
                                                            date = dateFormatParse.parse(headerBean.getUpdateTimestamp());
                                                            lastUpdated = dateFormatDisplay.format(date) + " by " + headerBean.getUpdateUser();
                                                            propType = "electronic";
                                                            found = true;
                                                        }
                                                    //}else {//Proposal Log
                                                        if (dbEngine != null && !found) {
                                                            if(routingNumber == null)routingNumber = "";
                                                            Vector param = new Vector();
                                                            param.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, proposalNumber));
                                                                Vector headerResult = dbEngine.executeRequest("Coeus",
                                                                "call GET_PROP_LOG_HEADER( <<PROPOSAL_NUMBER>>, <<OUT RESULTSET rset>>)", "Coeus", param);
                                                                if(headerResult != null && headerResult.size()>0){
                                                                    mapHeader = (Map)headerResult.get(0);
                                                                    investigator = (String)mapHeader.get("PI_NAME");
                                                                    sponsor = mapHeader.get("SPONSOR_CODE")+" : "+mapHeader.get("SPONSOR_NAME");
                                                                    period = "";
                                                                    title = (String)mapHeader.get("TITLE");
                                                                    leadUnit = mapHeader.get("LEAD_UNIT")+" : "+mapHeader.get("UNIT_NAME");;
                                                                    java.sql.Timestamp timestamp = (java.sql.Timestamp)mapHeader.get("UPDATE_TIMESTAMP");
                                                                    lastUpdated = dateFormatDisplay.format(timestamp) +" by "+mapHeader.get("UPDATE_USER");
                                                                    propType = "paper";
                                                                }
                                                         }
                                                    //}

                                            %>
                                            <table width="100%" border="0" cellpadding="3" cellspacing="0" class="table">
                                                <tr>
                                                    <td align="right" class='copybold'>
                                                        Investigator:
                                                    </td>
                                                    <td class="copy">
                                                        <%=investigator%>
                                                    </td>
                                                    <td align="right" class='copybold'>
                                                        Proposal number:
                                                    </td>
                                                    <td class="copy">
                                                        <%=proposalNumber%> &nbsp; <b>Review Number:</b> <%=apprSeq%>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td align="right" class='copybold'>
                                                        Agency/Sponsor:
                                                    </td>
                                                    <td class="copy">
                                                        <%=sponsor%>
                                                    </td>
                                                    <td align="right" class='copybold'>
                                                        Proposal Period
                                                    </td>
                                                    <td class="copy">
                                                        <%=period%>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td align="right" class='copybold'>
                                                        Title
                                                    </td>
                                                    <td colspan="3" class="copy">
                                                        <%=title%>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td align="right" class='copybold'>
                                                        Lead Unit:
                                                    </td>
                                                    <td class="copy" colspan="3">
                                                        <%=leadUnit%>
                                                    </td>
                                                    <%--td align="right" class='copybold'>
                                                        Last Updated:
                                                    </td>
                                                    <td class="copy">
                                                        <%=lastUpdated%>
                                                    </td--%>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                    <%
                            Map map;
                            int code, size;
                            boolean selected = false;
                            String description;
                            //JIRA COEUSDEV 724 - START
                            boolean viewMode = false;
                            //JIRA COEUSDEV 724 - END
                            try {
                                String acType = "I";
                                String mode = request.getParameter("mode");
                                if(perId == null){
                                    perId = personInfo.getPersonID();
                                }
                                String disabled = (mode!=null && mode.equalsIgnoreCase("d"))?"disabled":"";
                                Vector param = new Vector();
                                Parameter mentorParam = new Parameter("MENTOR_PER_ID", DBEngineConstants.TYPE_STRING, null);
                                param.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, proposalNumber));
                                if(apprSeq.equalsIgnoreCase("0")) {
                                    param.addElement(new Parameter("REVIEW_NUMBER", DBEngineConstants.TYPE_INT, 1));//Just get the first Review to Update
                                }else {
                                    param.addElement(new Parameter("REVIEW_NUMBER", DBEngineConstants.TYPE_INT, Integer.parseInt(apprSeq)));
                                }
                                param.addElement(new Parameter("PERSON_ID", DBEngineConstants.TYPE_STRING, perId));
                                Vector result = null;
                                int reviewActionCode = -1, reviewRoleCode = -1, timeSpent = -1, timeSpentDLC = -1, submVehicleCode = -1;
                                String deanOffice = "N", ggForm = "N";
                                String comments = "";
                                String reviwerPerId = null;
                                String reviewerName = "";
                                Timestamp timestamp = null;

                                //user Data After Error - START
                                String deletedComments = null;
                                int arrDeletedComments[] = null;
                                int arrDeletedCommentsindex[] = null;
                                //user Data After Error - END

                                Vector vecReviewAction = null, vecReviewRole = null, vecTimeSpent = null, vecSubmVehicle = null, vecComments = null;
                                if (dbEngine != null) {
                                    result = dbEngine.executeRequest("Coeus",
                                            "call GET_PROP_REVIEW_DETAILS( <<PROPOSAL_NUMBER>>, <<REVIEW_NUMBER>>, <<PERSON_ID>>, <<OUT RESULTSET rset>>)",
                                            "Coeus", param);
                                    if(result != null && result.size() > 0) {
                                        acType = "U";
                                        map = (Map)result.get(0);
                                        reviewActionCode = ((java.math.BigDecimal)map.get("REVIEW_ACTION_CODE")).intValue();
                                        reviewRoleCode = ((java.math.BigDecimal)map.get("REVIEW_ROLE_CODE")).intValue();
                                        timeSpent = ((java.math.BigDecimal)map.get("REVIEW_TIME_SPENT_CODE")).intValue();
                                        timeSpentDLC = ((java.math.BigDecimal)map.get("TIME_SPENT_ASSIS_DLC_CODE")).intValue();
                                        submVehicleCode = ((java.math.BigDecimal)map.get("SUBMISSION_VEHICLE_CODE")).intValue();
                                        deanOffice = (String)map.get("REVIEWED_DEAN_OFFICE_FLAG");
                                        ggForm = (String)map.get("GG_FORM");
                                        comments = (String)map.get("COMMENTS");
                                        timestamp = (Timestamp)map.get("UPDATE_TIMESTAMP");
                                        reviwerPerId = (String)map.get("PERSON_ID");
                                        //Check if session has userData after error
                                        //User Data - START
                                        if(userData != null){
                                            reviewRoleCode = Integer.parseInt((String)userData.get("reviewRole"));
                                            timeSpent = Integer.parseInt((String)userData.get("timeSpent"));
                                            timeSpentDLC = Integer.parseInt((String)userData.get("timeSpentDLC"));
                                            submVehicleCode = Integer.parseInt((String)userData.get("submissionVehicle"));
                                            deanOffice = (String)userData.get("deanoffice");
                                            deletedComments = (String)userData.get("deleteCommentNum");
                                            String arrStrDeletedComments[] = null;
                                            if(deletedComments != null && deletedComments.length()>0){
                                                arrStrDeletedComments = deletedComments.split(",");
                                                arrDeletedComments = new int[arrStrDeletedComments.length];
                                                arrDeletedCommentsindex = new int[arrStrDeletedComments.length];
                                                for(int i=0; i< arrStrDeletedComments.length; i++){
                                                    arrDeletedComments[i] = Integer.parseInt(arrStrDeletedComments[i]);
                                                }
                                            }
                                        }
                                        //User Data - END
                                        //Check if person is logged in person, else get person Info
                                        if(reviwerPerId.equalsIgnoreCase(personInfo.getPersonID())){
                                            reviewerName = personInfo.getFullName();
                                        }else {
                                            UserDetailsBean userDetails = new UserDetailsBean();
                                            PersonInfoBean reviewerpersonInfoBean = userDetails.getPersonInfo(reviwerPerId, true);
                                            reviwerPerId = reviewerpersonInfoBean.getPersonID();
                                            reviewerName = reviewerpersonInfoBean.getFullName();
                                        }
                                        
                                        session.setAttribute("updateTimestamp", timestamp);
                                        //JIRA COEUSDEV 724 - START
                                        if(reviewRoleCode == 1){ //CA review
                                            //if has LA Review, revert to view mode
                                            Vector funcParam = new Vector();
                                            funcParam.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, proposalNumber));
                                            Vector propCountResult = dbEngine.executeFunctions("Coeus", "{ <<OUT String PROP_COUNT>> = call FN_PROP_HAS_LA_REVIEW( <<PROPOSAL_NUMBER>>)}", funcParam);
                                            Map resultMap = (Map) propCountResult.get(0);
                                            String propCount = (String) resultMap.get("PROP_COUNT");
                                            if(Integer.parseInt(propCount) > 0){
                                                //LA review exists. display in View mode
                                                viewMode = true;
                                                disabled = "disabled";
                                            }
                                        }
                                        //JIRA COEUSDEV 724 - END
                                    }else {
                                        Vector ggParam = new Vector();
                                        ggParam.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, proposalNumber));
                                        result = dbEngine.executeFunctions("Coeus", "{ <<OUT INTEGER COUNT>> = call FN_IS_GG_RULE( <<PROPOSAL_NUMBER>>)}", ggParam);
                                        Map ggMap = (Map) result.get(0);
                                        String commentCount = (String) ggMap.get("COUNT");
                                        int count = Integer.parseInt(commentCount);
                                        ggForm = count > 0 ? "Y":"N";
                                        //new Entry
                                        reviewerName = personInfo.getFullName();
                                    }

                                    vecReviewRole = dbEngine.executeRequest("Coeus",
                                            "call get_prop_review_role( <<OUT RESULTSET rset>> )",
                                            "Coeus", null);

                                    vecReviewAction = dbEngine.executeRequest("Coeus",
                                            "call get_prop_review_action( <<OUT RESULTSET rset>> )",
                                            "Coeus", null);

                                    vecTimeSpent = dbEngine.executeRequest("Coeus",
                                            "call get_prop_review_time_spent( <<OUT RESULTSET rset>> )",
                                            "Coeus", null);

                                    vecSubmVehicle = dbEngine.executeRequest("Coeus",
                                            "call GET_SUBMISSION_VEHICLE( <<OUT RESULTSET rset>> )",
                                            "Coeus", null);

                                    param.addElement(mentorParam);
                                    vecComments = dbEngine.executeRequest("Coeus",
                                            "call GET_PROP_REVIEW_COMMENTS( <<PROPOSAL_NUMBER>>, <<REVIEW_NUMBER>>, <<PERSON_ID>>, <<MENTOR_PER_ID>>, <<OUT RESULTSET rset>>)",
                                            "Coeus", param);

                                    /*Check if Other users have commented on this proposal.
                                     * If yes, show sync link which would bring up a popup to
                                     * display other users and sync their comments.
                                     */
                                    if(mode==null || (mode!=null && !mode.equalsIgnoreCase("d"))){
                                         Vector userParam = new Vector();
                                         userParam.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, proposalNumber));
                                         userResult = dbEngine.executeRequest("Coeus",
                                                 "call GET_WL_COMMENT_USERS_FOR_PROP( <<PROPOSAL_NUMBER>>, <<OUT RESULTSET rset>>)", "Coeus", userParam);
                                         if (userResult != null && userResult.size() > 0) {//Result includes loggedin user.
                                             for(int index=0; index<userResult.size();index++) {
                                             Map userMap = (Map)userResult.get(index);
                                             /*String user = (String)userMap.get("UPDATE_USER");
                                                if(user.equalsIgnoreCase(personInfo.getUserId())){
                                                    userResult.remove(index);
                                                }*/
                                             String personId = (String)userMap.get("PERSON_ID");
                                             if(personId.equalsIgnoreCase(personInfo.getPersonID())){
                                                    userResult.remove(index);
                                                }
                                             }
                                             if(userResult.size() > 0) {
                                                otherUserComments = true;
                                             }
                                         }
                                     }
                                }
                        %>
                                    <tr>
                                        <td valign="top" align="center">
                                            <form action="<%=request.getContextPath()%>/wl/SaveProposalReview.jsp" name="proposalReview" method="post">

                                                <table width="100%" border="0" cellpadding="3" cellspacing="0" class="tabtable">
                                                    <tr class="theader">
                                                        <td colspan="4">
                                                            Review Details
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right" class="copybold" width="25%">
                                                            Reviewer :
                                                        </td>
                                                        <td  width="25%">
                                                            <%=reviewerName%>
                                                        </td>

                                                        <td align="right" class="copybold"  width="25%">
                                                            Reviewer Role :
                                                        </td>
                                                        <td  width="25%">
                                                            <select name="reviewRole" <%=disabled%> onchange="setDataChanged()">
                                                                <%
                                                                size = vecReviewRole.size();
                                                                for (int index = 0; index < size; index++) {
                                                                    map = (Map) vecReviewRole.get(index);
                                                                    code = ((java.math.BigDecimal)map.get("REVIEW_ROLE_CODE")).intValue();
                                                                    description = (String) map.get("DESCRIPTION");
                                                                    selected = (code == reviewRoleCode);
                                                                    out.print("<option value='" + code + "' "+(selected == true?"selected":"")+">" + description + "</option>");
                                                                }
                                                                %>
                                                            </select>
                                                        </td>
                                                    </tr>

                                                    <tr>
                                                        <td align="right" class="copybold">
                                                            Review Action :
                                                        </td>
                                                        <td>
                                                            <select name="reviewAction" id="reviewAction" <%=propType.equalsIgnoreCase("electronic") || (mode!= null && mode.equalsIgnoreCase("d"))?"disabled":""%> onchange="setDataChanged()" <%= (viewMode == true)?"disabled":""%>>
                                                                <%
                                                                size = vecReviewAction.size();
                                                                for (int index = 0; index < size; index++) {
                                                                    map = (Map) vecReviewAction.get(index);
                                                                    code = ((java.math.BigDecimal)map.get("REVIEW_ACTION_CODE")).intValue();
                                                                    selected = (code == reviewActionCode);
                                                                    description = (String) map.get("DESCRIPTION");
                                                                    out.print("<option value='" + code + "' "+(selected == true?"selected":"")+">" + description + "</option>");
                                                                }
                                                                %>
                                                            </select>
                                                        </td>
                                                        <td align="right" class="copybold">Review Completed on:</td>
                                                        <td><%=timestamp==null?"":dateFormatDisplay.format(timestamp)%></td>
                                                    </tr>

                                                    <tr>
                                                        <td align="right" class="copybold">
                                                            Time Spent Reviewing Proposal:
                                                        </td>
                                                        <td>
                                                            <select name="timeSpent" <%=disabled%> onchange="setDataChanged()">
                                                                <%
                                                                size = vecTimeSpent.size();
                                                                for (int index = 0; index < size; index++) {
                                                                    map = (Map) vecTimeSpent.get(index);
                                                                    code = ((java.math.BigDecimal)map.get("REVIEW_TIME_SPENT_CODE")).intValue();
                                                                    description = (String) map.get("DESCRIPTION");
                                                                    selected = (code == timeSpent);
                                                                    out.print("<option value='" + code + "' "+(selected == true?"selected":"")+">" + description + "</option>");
                                                                }
                                                                %>
                                                            </select>
                                                        </td>
                                                        <td align="right" class="copybold">
                                                            Reviewed by Dean's Office :
                                                        </td>
                                                        <td>
                                                            <input name="reviewdbyDeanoff" type="checkbox" <%=deanOffice.equalsIgnoreCase("Y")?"checked":""%> <%=disabled%> onchange="setDataChanged()">
                                                        </td>
                                                    </tr>

                                                    <tr>
                                                        <td align="right" class="copybold">
                                                            Time Spent Assisting DLC:
                                                        </td>
                                                        <td>
                                                            <select name="timeSpentDLC" <%=disabled%> onchange="setDataChanged()">
                                                                <%
                                                                size = vecTimeSpent.size();
                                                                for (int index = 0; index < size; index++) {
                                                                    map = (Map) vecTimeSpent.get(index);
                                                                    code = ((java.math.BigDecimal)map.get("REVIEW_TIME_SPENT_CODE")).intValue();
                                                                    description = (String) map.get("DESCRIPTION");
                                                                    selected = (code == timeSpentDLC);
                                                                    out.print("<option value='" + code + "' "+(selected == true?"selected":"")+">" + description + "</option>");
                                                                }
                                                                %>
                                                            </select>
                                                        </td>
                                                        <td align="right" class="copybold">
                                                            Submission Vehicle :
                                                        </td>
                                                        <td>
                                                            <select name="submissionVehicle" <%=disabled%> onchange="setDataChanged()">
                                                                <%
                                                                size = vecSubmVehicle.size();
                                                                for (int index = 0; index < size; index++) {
                                                                    map = (Map) vecSubmVehicle.get(index);
                                                                    code = ((java.math.BigDecimal)map.get("SUBMISSION_VEHICLE_CODE")).intValue();
                                                                    description = (String) map.get("DESCRIPTION");
                                                                    selected = (code == submVehicleCode);
                                                                    //pre select Coeus when its prop dev
                                                                    selected = (propType.equalsIgnoreCase("electronic") && acType!=null && acType.equalsIgnoreCase("I") && (code == 11)) || selected;
                                                                    out.print("<option value='" + code + "' "+(selected == true?"selected":"")+">" + description + "</option>");
                                                                }
                                                                %>
                                                            </select>
                                                        </td>
                                                    </tr>

                                                    <tr>
                                                        <td align="right" class="copybold">
                                                            Grant Gov Proposal :
                                                        </td>
                                                        <td>
                                                            <input name="grantsGovProposal" type="checkbox" disabled <%=ggForm.equalsIgnoreCase("Y")?"checked":""%>>
                                                        </td>
                                                    </tr>
                                                    <%if(otherUserComments && !viewMode){%>
                                                    <tr>
                                                        <td colspan="4" class="copybold" align="center">
                                                            This Proposal has been reviewed by other users.
                                                            <a href="javascript:showDialog('divSyncComments')">Sync Comments</a>
                                                        </td>
                                                    </tr>
                                                    <%}%>
                                                    <tr>
                                                        <td colspan="4">
                                                            <table width="100%" id="tabComments" class="tabtable">
                                                                <tbody id="tabCommentsBody">
                                                                <tr class="theader">
                                                                    <!--td width="5%">
                                                                        Id
                                                                    </td-->
                                                                    <td width="95%" colspan="2">
                                                                        Review Comment(s)
                                                                    </td>
                                                                    <td width="5%">
                                                                        Delete
                                                                    </td>
                                                                </tr>
                                                                <%
                                                                size = vecComments.size();
                                                                code = -1;
                                                                int commentNumber;
                                                                boolean disableCannedComment = false;
                                                                for (int index = 0; index < size; index++) {
                                                                    map = (Map) vecComments.get(index);
                                                                    if(map.get("CANNED_REVIEW_COMMENT_CODE") != null) {
                                                                        code = ((java.math.BigDecimal)map.get("CANNED_REVIEW_COMMENT_CODE")).intValue();
                                                                    }else {
                                                                        code = -1;
                                                                    }
                                                                    commentNumber = ((java.math.BigDecimal)map.get("COMMENT_NUMBER")).intValue();
                                                                    //JIRA COEUSDEV-611 - START
                                                                    if(code == 0){
                                                                        //no Canned Comments
                                                                        disableCannedComment = true;
                                                                    }
                                                                    //JIRA COEUSDEV-611 - END
                                                                    description = (String) map.get("COMMENTS");

                                                                    //User Data after Error - START
                                                                    if(userData != null && arrDeletedComments != null && arrDeletedComments.length > 0){
                                                                        for(int delIndex = 0; delIndex < arrDeletedComments.length; delIndex++){
                                                                            if(arrDeletedComments[delIndex] == commentNumber) {
                                                                                arrDeletedCommentsindex[delIndex] = index;
                                                                                break;
                                                                            }
                                                                        }
                                                                     }
                                                                    //User Data after Error - END
                                                                    %>
                                                                    <tr id="row<%=index%>" class="rowline" style="height:25px;">
                                                                        <td><%=code== -1?"":code%></td>
                                                                        <td><%=description%></td>
                                                                        <td>
                                                                            <%if((mode!= null && mode.equalsIgnoreCase("d")) || viewMode ) {%>
                                                                                &nbsp;
                                                                            <%}else{%>
                                                                                <a href="javascript:markForDelete(<%=commentNumber%>, <%=index%>)" alt="Delete" title="Delete"><img border="0" src="../coeusliteimages/none.gif"></a>
                                                                            <%}%>
                                                                        </td>
                                                                    </tr>
                                                                    <%if(code!=-1){%>
                                                                    <script>
                                                                        cannedReportId[cannedReportId.length] = <%=code%>;
                                                                        cannedReportIndex[cannedReportIndex.length] = <%=index%>;
                                                                    </script>
                                                                    <%
                                                                    }//End If
                                                                    }//End For
                                                                    %>
                                                                <script>
                                                                    rowNum = <%=(size - 1)%>;
                                                                    insertFrom = rowNum+1;
                                                                </script>
                                                                <!--tr class="rowline" id="firstRow">
                                                                    <td colspan="3">
                                                                        No Comments
                                                                    </td>
                                                                </tr-->
                                                                </tbody>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                    <!--tr>
                                                        <td colspan="2" align="center">
                                                            <input type="button" value="Add Text Comment" onclick="addTextComment()"/>
                                                        </td>
                                                        <td colspan="2" align="center">
                                                            <input type="button" value="Add Canned Comment(s)" onclick="addCannedComment()"/>
                                                        </td>
                                                    </tr-->
                                                    <%if(!(mode!=null && mode.equalsIgnoreCase("d"))){%>
                                                    <tr>
                                                        <td align="right">
                                                            <input type="button" value="Add Text Comment" onclick="javascript:showDialog('textCommentDiv')" <%=disabled%>/>
                                                        </td>
                                                        <td align="left">

                                                            <%//JIRA COEUSDEV-611 - START
                                                            if (disabled.trim().length() == 0 && !disableCannedComment) {
                                                                disableCannedComment = false;
                                                            }else {
                                                                disableCannedComment = true;
                                                            }
                                                            //JIRA COEUSDEV-611 - END
                                                            %>
                                                            <input type="button" value="Add Canned Comment(s)" onclick="javascript:showDialog('cannedComments')" <%--=disableCannedComment?"disabled":""--%> <%= (viewMode == true)?"disabled":""%>/>
                                                        </td>
                                                        <td colspan="2" align="right" id="tdHidden">
                                                            <input type="hidden" name="routingNumber" value="<%=routingNumber%>"/>
                                                            <input type="hidden" name="proposalNumber" value="<%=proposalNumber%>"/>
                                                            <input type="hidden" name="proposalType" value="<%=request.getParameter("proposalType")%>"/>
                                                            <input type="hidden" name="acType" value="<%=acType%>"/>
                                                            <input type="hidden" name="apprSeq" value="<%=apprSeq%>"/>
                                                            <input type="hidden" name="deleteCommentNum"/>
                                                            <input type="hidden" name="link"/>
                                                            <input type="button" name="Save" value="&nbsp;&nbsp;Save&nbsp;&nbsp;" <%=disabled%> onclick="submitForm('proposalReview')"/>
                                                <!--input type="button" name="Test" value="test" onclick="test()"/-->
                                                        </td>
                                                    </tr>
                                                    <%}%>
                                                </table>
                                            </form>
                                        </td>
                                    </tr>
                                    <%if(mode!=null && mode.equalsIgnoreCase("d")){
                                    param.remove(mentorParam);
                                    param.add(new Parameter("MENTOR_PER_ID", DBEngineConstants.TYPE_STRING, personInfo.getPersonID()));
                                    vecComments = dbEngine.executeRequest("Coeus",
                                            "call GET_PROP_REVIEW_COMMENTS( <<PROPOSAL_NUMBER>>, <<REVIEW_NUMBER>>, <<PERSON_ID>>, <<MENTOR_PER_ID>>, <<OUT RESULTSET rset>>)",
                                            "Coeus", param);
                                    %>
                                    <script>
                                        formName = "mentorProposalReview";
                                        commentsTable = "tabMentorComments";
                                        commentsTableBody = "tabMentorCommentsBody";
                                        hiddenTD = "tdHiddenMentor";
                                        commentRow = "mentorRow"
                                        //reset other variables
                                        cannedReportId = new Array();
                                        cannedReportIndex = new Array();
                                        commentToDelete = new Array();
                                        rowNum = -1;
                                        insertFrom = rowNum+1;
                                    </script>
                                    <tr>
                                        <td valign="top" class="copybold">
                                            <form action="<%=request.getContextPath()%>/wl/SaveProposalReview.jsp" name="mentorProposalReview" method="post" onsubmit="return validate()">
                                            <table width="100%" id="tabMentorComments" class="tabtable">
                                                <tbody id="tabMentorCommentsBody">
                                                <tr class="theader">
                                                    <td width="95%" colspan="2">
                                                        Mentor Comment(s)
                                                    </td>
                                                    <td width="5%">
                                                        Delete
                                                    </td>
                                                </tr>
                                                <%
                                                    size = vecComments.size();
                                                    code = -1;
                                                    for (int index = 0; index < size; index++) {
                                                        map = (Map) vecComments.get(index);
                                                        if(map.get("CANNED_REVIEW_COMMENT_CODE") != null) {
                                                            code = ((java.math.BigDecimal)map.get("CANNED_REVIEW_COMMENT_CODE")).intValue();
                                                        }else {
                                                            code = -1;
                                                        }
                                                        commentNumber = ((java.math.BigDecimal)map.get("COMMENT_NUMBER")).intValue();
                                                        description = (String) map.get("COMMENTS");
                                                        %>
                                                        <tr id="mentorRow<%=index%>" class="rowline">
                                                            <td>
                                                                <%=code== -1?"":code%>
                                                            </td>
                                                            <td>
                                                                <%=description%>
                                                            </td>
                                                            <td>
                                                                <a href="javascript:markForDelete(<%=commentNumber%>, <%=index%>)" alt="Delete" title="Delete"><img border="0" src="../coeusliteimages/none.gif"></a>
                                                            </td>
                                                        </tr>
                                                        <%if(code!=-1){%>
                                                        <script>
                                                            cannedReportId[cannedReportId.length] = <%=code%>;
                                                            cannedReportIndex[cannedReportIndex.length] = <%=index%>;
                                                        </script>
                                                        <%
                                                        }//End If
                                                        }//End For
                                                        %>
                                                    <script>
                                                        rowNum = <%=(size - 1)%>;
                                                        insertFrom = rowNum+1;
                                                    </script>
                                                </tbody>
                                            </table>
                                            <table width="100%">
                                                <tr>
                                                    <td align="right">
                                                        <input type="button" value="Add Text Comment" onclick="javascript:showDialog('textCommentDiv')"/>
                                                    </td>
                                                    <td align="left">
                                                        <input type="button" value="Add Canned Comment(s)" onclick="javascript:showDialog('cannedComments')"/>
                                                    </td>
                                                    <td width="25%">&nbsp;</td>
                                                    <td align="right" id="tdHiddenMentor">
                                                        <input type="hidden" name="routingNumber" value="<%=routingNumber%>"/>
                                                        <input type="hidden" name="proposalNumber" value="<%=proposalNumber%>"/>
                                                        <input type="hidden" name="acType" value="<%=acType%>"/>
                                                        <input type="hidden" name="apprSeq" value="<%=apprSeq%>"/>
                                                        <input type="hidden" name="deleteCommentNum"/>
                                                        <input type="hidden" name="reviwerPerId" value="<%=reviwerPerId%>"/>
                                                        <input type="hidden" name="link"/>
                                                        <input type="button" name="Save" value="&nbsp;&nbsp;Save&nbsp;&nbsp;"  onclick="submitForm('mentorProposalReview')"/>
                                                    </td>
                                                </tr>
                                            </table>
                                            </form>
                                        </td>
                                    </tr>
                                    <%}%>
                                    <%
                                    //User Data after Error - START
                                    if(userData != null){
                                        out.print("<script>");
                                        if(arrDeletedComments != null && arrDeletedComments.length > 0) {
                                            for(int index=0;index<arrDeletedComments.length;index++){
                                                out.print("markForDelete("+arrDeletedComments[index]+","+arrDeletedCommentsindex[index]+");");
                                            }
                                        }
                                        //Inserted Comments
                                        List list = (List)userData.get("insertedComments");
                                        String id, insertedComment;
                                        if(list != null && list.size() > 0){
                                        int index;
                                        for(int insertedIndex=0; insertedIndex<list.size(); insertedIndex++){
                                            insertedComment = (String)list.get(insertedIndex);
                                            index = insertedComment.indexOf("~");
                                            if (index == 0) {
                                                //text Comment
                                                id = "";
                                                insertedComment = insertedComment.substring(1);
                                            } else {
                                                //Canned Comment
                                                id = insertedComment.substring(0, index);
                                                insertedComment = insertedComment.substring(index + 1);
                                                out.print("cannedReportIndex[cannedReportIndex.length] = rowNum + 1;cannedReportId[cannedReportId.length] = "+id+";");
                                            }
                                            insertedComment = StringEscapeUtils.unescapeHtml(insertedComment);
                                            out.print("rowData = new Array(2);rowData[0] = '"+id+"';rowData[1] = '"+insertedComment+"';");
                                            out.print("addRow(rowData);");
                                        }
                                        }
                                        out.print("</script>");
                                    }
                                    //User Data after Error - END
                                    } catch (Exception ex) {
                                        out.print(ex.getMessage());
                                        UtilFactory.log(ex.getMessage(), ex, "ProposalReview.jsp", "");
                                    }
                                    %>
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>

        </table>

            <div id="cannedComments" class="dialog" style="width:600px;height:380px;overflow: hidden">
                <table class="table">
                    
                    <tr>
                        <td colspan="2" align="center">
                <div style="overflow:auto;width:600px;height:350px">
                    <iframe id="iframeId" onload="return selectComments();" width="100%" height="100%" scrolling="auto" marginheight="0" marginwidth="0" frameborder="0" src="<%=request.getContextPath()%>/wl/ReviewComments.jsp?proposalNumber=<%=proposalNumber%>"></iframe>
                </div>
                        </td>
                    </tr>
                    <tr>
                        <td align="left" width="17%">
                            <input type="button" style="width:100" onclick="closeCannedComments()" value=" OK ">
                        </td>
                        <td align="left">
                            <input type="button" style="width:100" value="Cancel" onclick="hm('cannedComments')"/>
                        </td>
                    </tr>
                </table>
            </div>

            <div id="textCommentDiv" class="dialog" style="width:600px;height:210px;overflow: hidden">
                <table class="table">
                    <tr>
                        <td colspan="2" align="center">
                <div style="overflow:auto;width:600px;height:175px">
                    <textarea id="textComment" rows="10" cols="72"></textarea>
                </div>
                        </td>
                    </tr>
                    <tr>
                        <td align="left" width="17%">
                            <input type="button" onclick="closeTextComment()" style="width:100" value="OK">
                        </td>
                        <td align="left">
                            <input type="button" value="Cancel" style="width:100" onclick="hm('textCommentDiv')"/>
                        </td>
                    </tr>
                </table>
            </div>
            
            <%if(otherUserComments && userResult != null){%>
            <script>
                function GetXmlHttpObject(handler)
                {
                    var objXMLHttp=null
                    if (window.XMLHttpRequest)
                    {
                        objXMLHttp=new XMLHttpRequest()
                    }
                    else if (window.ActiveXObject)
                    {
                        objXMLHttp=new ActiveXObject("MSXML2.XMLHTTP")
                    }
                    return objXMLHttp
                }

                 function syncComments(){
                    /*rowData = new Array(2);
                    rowData[0] = "";
                    rowData[1] = "Test Text Comment For Sync";
                    addRow(rowData);

                    rowData[0] = "5";
                    rowData[1] = "Canned Comment Sync Test - Cost Sharing";
                    cannedReportIndex[cannedReportIndex.length] = rowNum + 1;
                    cannedReportId[cannedReportId.length] = rowData[0];
                    addRow(rowData);
                    */
                   var params="?proposalNumber=<%=proposalNumber%>&";
                   for(i=0;i<<%=userResult.size()%>; i++){
                        if(document.forms[0].elements[i].checked){
                        params = params+"p"+i+"="+document.forms[0].elements[i].value+"&";
                        }
                    }
                    //alert(params);
                    xmlHttp=GetXmlHttpObject()
                    if (xmlHttp==null)
                    {
                        alert ("Browser does not support HTTP Request");
                        hm('divSyncComments');
                        return;
                    }
                    var url="SyncComments.jsp"+params;

                    xmlHttp.onreadystatechange=addReviewComments;
                    xmlHttp.open("GET",url,true);
                    xmlHttp.send(null);
                    hm('divSyncComments');
                }
                
                function addReviewComments(){
                    if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete")
                    {
                        var newCommentsAdded = false;
                        var elemTable = xmlHttp.responseXML.getElementsByTagName('table')[0];
                        var elemTr;
                        var arrNewRow = new Array(2);
                        var reportIdPresent = ","+cannedReportId.join(",")+",";
                        for(i=0;i<elemTable.getElementsByTagName('tr').length;i++){
                            elemTr = elemTable.getElementsByTagName('tr')[i];
                            var childNodes = elemTr.childNodes;
                            //alert(childNodes[0].text+" : "+childNodes[1].text);
                            //code above is working...
                            //Add to Comments Table
                            //check if the comment code is present in array. if user added new comments to browsers after opening.
                            if(childNodes[0].hasChildNodes()){
                                if(childNodes[0].textContent){//W3C
                                   arrNewRow[0] = childNodes[0].textContent;
                                }else{//IE
                                   arrNewRow[0] = childNodes[0].text;
                                }
                            }else {
                                //Text Comment
                                arrNewRow[0] = "";
                            }
                            
                            if(childNodes[1].textContent){//W3C
                               arrNewRow[1] = childNodes[1].textContent;
                            }else{//IE
                               arrNewRow[1] = childNodes[1].text;
                            }
                           
                            if(arrNewRow[0].length > 0){
                                //Canned Comments. check for duplicates
                                if(reportIdPresent.indexOf(","+arrNewRow[0]+",") == -1){
                                    cannedReportIndex[cannedReportIndex.length] = rowNum + 1;
                                    cannedReportId[cannedReportId.length] = arrNewRow[0];
                                    addRow(arrNewRow);
                                    newCommentsAdded = true;
                                }
                            }else {
                                //Text Comment. Just Add
                                if(!isDuplicateComment(arrNewRow[1])){
                                    addRow(arrNewRow);
                                    newCommentsAdded = true;
                                }
                            }
                        }
                        if(!newCommentsAdded){
                            alert("No New Comments to Add");
                        }
                    }
                }

                function selectDeselectAll(value){
                    for(i=0;i<<%=userResult.size()%>; i++){
                        document.forms[0].elements[i].checked = value;
                    }
                }

                function isDuplicateComment(comment){
                tableElement = document.getElementById(commentsTableBody);
                var commentId = 0;
                for(i=1;i<tableElement.rows.length;i++){//first TR is Header
                    rowElem = tableElement.rows[i];
                    var childNodes = rowElem.childNodes;
                    //alert(rowElem.childNodes.item(1).innerHTML+":"+childNodes[0].hasChildNodes());
                    if(!childNodes[0].hasChildNodes()){
                        //alert(comment+":"+rowElem.childNodes.item(1).innerHTML+">"+comment.length+":"+rowElem.childNodes.item(1).innerHTML.length)
                        if(comment==rowElem.childNodes.item(1).innerHTML){
                            return true;
                        }
                    }
                }
                return false;
            }
            </script>
            <div id="divSyncComments" class="dialog" style="width:400px;height:200px;overflow: hidden">
                <form name="frmComments">
                    <table class="table" width="100%" height="100%">
                        <tr>
                            <td colspan="2" class="copybold">
                                The Proposal Number <%=proposalNumber%> has following Reviews.<br> Select Reviews to sync.
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2" valign="top">
                                <div style="overflow:auto;width:400px;height:175px">
                                    <table class="tabtable" width="100%">
                                        <tr class="theader">
                                            <td width="40%">Reviewer</td>
                                            <td width="50%">Last Updated</td>
                                            <td width="10%">Select <a href="javascript:selectDeselectAll(true)">All</a>|<a href="javascript:selectDeselectAll(false)">None</a></td>
                                        </tr>
                                        <%
                                        Map userMap;
                                        String user;
                                        for(int index=0; index < userResult.size(); index++){
                                            userMap = (Map)userResult.get(index);
                                            user = (String)userMap.get("FULL_NAME");
                                            if(!user.equalsIgnoreCase(personInfo.getUserId())){
                                            %>
                                            <tr class="rowLine">
                                                <td><%=user%></td>
                                                <td><%=userMap.get("UPDATE_TIMESTAMP")%></td>
                                                <td><input type="checkbox" id="chk<%=index%>" value="<%=userMap.get("PERSON_ID")%>"/></td>
                                            </tr>
                                            <%
                                            }
                                        }
                                        %>
                                    </table>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td align="left" width="17%">
                                <input type="button" style="width:100" onclick="syncComments()" value=" OK ">
                            </td>
                            <td align="left">
                                <input type="button" style="width:100" value="Cancel" onclick="hm('divSyncComments')"/>
                            </td>
                        </tr>
                    </table>
                </form>
            </div>
            <%}%>
    </body>
</html>
