<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<%
String label =(String) session.getAttribute("actionMode");
//String editCommentsIndex = (String) request.getAttribute("editIndex");
//COEUSQA-1433 - Allow Recall from Routing - Start
boolean recallFlag = false;
boolean byPassFlag = false;
boolean isInLastApproval =((Boolean)session.getAttribute("isInLastStageApproval"+session.getId())).booleanValue();

//COEUSQA-1433 - Allow Recall from Routing - End
if(label!=null && label.equals("approve")){
    label = "Approve";
} else if(label!=null && label.equals("reject")){
    label = "Reject";
} else if(label!=null && label.equals("bypass")){
    label = "ByPass";
    byPassFlag = true;
}
//COEUSQA-1433 - Allow Recall from Routing - Start
 else if("recall".equals(label)){
    label = "Recall";
    recallFlag = true;
}
//COEUSQA-1433 - Allow Recall from Routing - Start
 else {
    label = "Save";
}
String strBgColor = "#DCE5F1";
String proposalNumber =(String)session.getAttribute("proposalNumber"+session.getId());
edu.mit.coeus.routing.bean.RoutingBean routingBean =
        (edu.mit.coeus.routing.bean.RoutingBean) session.getAttribute("routingBean"+session.getId());
routingBean = (routingBean == null) ? new edu.mit.coeus.routing.bean.RoutingBean() : routingBean;
// COEUSQA-1497: No ability to pass routing Authority in IRB Lite
boolean newApproversPresent = false;
String applicationPath = request.getContextPath();

%>
<%
   String mode = (String)session.getAttribute("mode"+session.getId());
   String budgetStatusMode = "";

   if(session.getAttribute("displayMode") != null) {
       budgetStatusMode = (String)session.getAttribute("displayMode");
   }
  // String budgetStatusMode = (String)session.getAttribute("displayMode");
   boolean readOnly = false;
   if(mode!= null && mode.equals("display")){
       readOnly = true;
      // session.setAttribute("readOnly",readOnly);
   }
    session.setAttribute("readOnly",readOnly);
   //else if(budgetStatusMode != null && budgetStatusMode.equals("complete")){
       // readOnly = true;
    //}
%>


<html>
    <head>
        <title>Approve Proposal</title>
        <script>
            var saveConfirmationReqd = 'false';
            function approve(index){
                var componentApproveAll = document.getElementsByName("dynaFormData[0].approveAll");
                //componentApproveAll.value = index;
                componentApproveAll.value = 0; // JM 8-23-2012           
                document.approvalRoutingList.action = "<%=request.getContextPath()%>/approveAllUsers.do?approveAll="+index;
                document.approvalRoutingList.submit();
            }

             var submitCount = 0;
            function performRejection(){
                if(submitCount == 0){
                    submitCount++;
                    document.approvalRoutingList.submit(); 
                }
            }
            function saveComments(){
                document.approvalRoutingList.action = "<%=request.getContextPath()%>/saveApprovalDetails.do?addingType=comments";
                document.approvalRoutingList.submit();
            }

            function saveAttachments(){
                var descriptionObject = document.getElementsByName('dynaFormData[0].description');
                var description = descriptionObject[0].value;
                if(description.length == 0){
                    alert('<bean:message key="error.UploadDoc.documentDescriptionMandatory"/>');
                    descriptionObject[0].focus();
                }else{
                    document.approvalRoutingList.action = "<%=request.getContextPath()%>/saveApprovalDetails.do?addingType=attachments";
                    document.approvalRoutingList.submit();
                }

            }

            function goBack(){
				// COEUSQA-1497: No ability to pass routing Authority in IRB Lite - Start
                var canNavigateBack = 'true';
                if(saveConfirmationReqd == 'true'){
                    if (!confirm('<bean:message key="routing.saveConfirmation" bundle="proposal"/>')==true){
                        canNavigateBack = false;
                    }
                }
                if(canNavigateBack == 'true'){
                    if(<%=routingBean.getModuleCode()%> == 3){
                        document.approvalRoutingList.action = "<%=request.getContextPath()%>/displayProposal.do?proposalNo=<%=proposalNumber%>&page=back";
                        document.approvalRoutingList.submit();
                    } else if(<%=routingBean.getModuleCode()%> == 7){
                        document.approvalRoutingList.action = "<%=request.getContextPath()%>/displayProtocol.do?protocolNumber=<%=routingBean.getModuleItemKey()%>";
                        document.approvalRoutingList.submit();
                    } else if(<%=routingBean.getModuleCode()%> == 9){
                        document.approvalRoutingList.action = "<%=request.getContextPath()%>/displayIacuc.do?protocolNumber=<%=routingBean.getModuleItemKey()%>";
                        document.approvalRoutingList.submit();
                    }
                }
                }


            function edit_comments(index){
                document.approvalRoutingList.action = "<%=request.getContextPath()%>/editApprovalComments.do?editIndex="+index;
                document.approvalRoutingList.submit();
            }

            function remove_comments(index){
              if(confirm("Are you sure you want to delete this comment?")== true){
                document.approvalRoutingList.action = "<%=request.getContextPath()%>/removeApprovalDetails.do?removeIndex="+index+"&type=comments";
                document.approvalRoutingList.submit();
                }
            }

            function remove_attachments(index){
               if(confirm("Are you sure you want to delete the attachment?")== true){
                document.approvalRoutingList.action = "<%=request.getContextPath()%>/removeApprovalDetails.do?removeIndex="+index+"&type=attachments";
                document.approvalRoutingList.submit();
                }
            }

            function view_attachments(index){
                window.open("<%=request.getContextPath()%>/viewApprovalAttachments.do?attachmentNo="+index);
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
                var loc = '<bean:write name='ctxtPath'/>/coeuslite/mit/utils/dialogs/cwViewText.jsp?value='+value;
                var newWin = window.open(loc,"formName",'dependent=1,toolbar=0,location=0,directories=0,status=0, menubar=0,scrollbars=0,resizable=0,width=' + w + ',height=' + h+ ',left='+ leftPos + ',top=' + topPos);
                newWin.window.focus();
            }
            // COEUSQA-1497: No ability to pass routing Authority in IRB Lite - Start
              var alternateApprover = 'true';
              var stopNumber = '0';
            function addApprover(maxStopNumber) {
                stopNumber = maxStopNumber;
                alternateApprover = 'false';
                var winleft = (screen.width - 650) / 2;
                var winUp = (screen.height - 450) / 2;
                var win = "scrollbars=1,resizable=1,width=750,height=450,left="+winleft+",top="+winUp;
                if(<%=routingBean.getModuleCode()%> == 3){
                    sList = window.open('<%=request.getContextPath()%>/generalProposalSearch.do?type=<bean:message key="searchWindow.title.person" bundle="proposal"/>&search=true&searchName=USERSEARCH', "list", win);
                }else if(<%=routingBean.getModuleCode()%> == 7){
                    sList = window.open('<%=request.getContextPath()%>/irbSearch.do?type=<bean:message key="searchWindow.title.person"/>&search=true&searchName=USERSEARCH', "list", win);
                }else if(<%=routingBean.getModuleCode()%> == 9){
                    sList = window.open('<%=request.getContextPath()%>/iacucSearch.do?type=<bean:message key="searchWindow.title.person"/>&search=true&searchName=USERSEARCH', "list", win);
                }

                if (parseInt(navigator.appVersion) >= 4) {
                    window.sList.focus();
                }
            }

            function fetch_Data(result){
                  if(result["USER_ID"] != 'null' && result["USER_ID"] != undefined){
                  userId = result["USER_ID"];
                  }
                  document.approvalRoutingList.action = "<%=request.getContextPath()%>/addApprover.do?newApprover="+userId+"&alternateApprover="+alternateApprover+"&stopNumber="+stopNumber;
                  document.approvalRoutingList.submit();
            }

            function addAlternateApprover(routingStopNumber) {
                alternateApprover = 'true';
                stopNumber = routingStopNumber;
                var winleft = (screen.width - 650) / 2;
                var winUp = (screen.height - 450) / 2;
                var win = "scrollbars=1,resizable=1,width=750,height=450,left="+winleft+",top="+winUp;

                if(<%=routingBean.getModuleCode()%> == 3){
                    sList = window.open('<%=request.getContextPath()%>/generalProposalSearch.do?type=<bean:message key="searchWindow.title.person" bundle="proposal"/>&search=true&searchName=USERSEARCH', "list", win);
                }else if(<%=routingBean.getModuleCode()%> == 7){
                    sList = window.open('<%=request.getContextPath()%>/irbSearch.do?type=<bean:message key="searchWindow.title.person"/>&search=true&searchName=USERSEARCH', "list", win);
                }

                if (parseInt(navigator.appVersion) >= 4) {
                    window.sList.focus();
                }
            }

            function removeApprover(stopNumber, approverNumber, alternateApprover){

            if (confirm('<bean:message key="routing.removeApproverConfirmation" bundle="proposal"/>')==true){
                document.approvalRoutingList.action = "<%=request.getContextPath()%>/removeApprover.do?stopNumber="+stopNumber+"&approverNumber="+approverNumber+"&alternateApprover="+alternateApprover;
                document.approvalRoutingList.submit();
                }
            }

            function performPass(){
                document.approvalRoutingList.action = "<%=request.getContextPath()%>/approveUser.do?operation=P";
                document.approvalRoutingList.submit();
            }

            function setSaveConfirmationRequired(){
                saveConfirmationReqd = 'true';
            }
            // COEUSQA-1497: No ability to pass routing Authority in IRB Lite - End

            <!-- COEUSQA-1433 - Allow Recall from Routing - Start -->
            function performValidation(showDialog){
               var showLastStageMsg = showDialog;
               var commentsObject = document.getElementsByName('dynaFormData[0].comments');
                var comments = commentsObject[0].value;
                if(comments.length == 0){
                    alert('<bean:message key="routing.commentsrequiresforRecall" bundle="proposal"/>');
                    commentsObject[0].focus();
                }else{
                    if (confirm('<bean:message key="routing.recallConfirmation" bundle="proposal"/>')==true){
                        if(showLastStageMsg == 'true'){
                            if(confirm('<bean:message key="routing.recallLastSatgeConfirmation" bundle="proposal"/>')==true){
                                document.approvalRoutingList.action = "<%=request.getContextPath()%>/approveUser.do";
                                document.approvalRoutingList.submit();
                            }else{
                                document.approvalRoutingList.action = "<%=request.getContextPath()%>/getApprovalActions.do?actionMode=recall";
                                document.approvalRoutingList.submit();
                            }
                        }else{
                            document.approvalRoutingList.action = "<%=request.getContextPath()%>/approveUser.do";
                            document.approvalRoutingList.submit();
                        }
                    }else{
                        document.approvalRoutingList.action = "<%=request.getContextPath()%>/getApprovalActions.do?actionMode=recall";
                        document.approvalRoutingList.submit();
                        commentsObject[0].value = comments;
                    }
                }
            }
            <!-- COEUSQA-1433 - Allow Recall from Routing - End -->
        </script>
    </head>
    <body>
        <html:form action="/approveUser.do" enctype="multipart/form-data">

            <table width='100%' border='0' cellpadding='0' cellspacing='0' class='tabtable'>
                <tr>
                    <td align=left>
                    <logic:present name="modeLock">

                      <html:messages id="message" message="true" property="acqLock" bundle="proposal">
                         <font color="red">   <bean:write name = "message"/></font>
                      </html:messages>
                    </logic:present>
                        <font color='red'><html:errors header="" footer=""/></font>

                    </td>
                </tr>
                <tr>
                    <logic:notPresent name="modeLock">
                    <td align=left>
                        <font color='red'>
                          <logic:messagesPresent message="true">
                              <html:messages id="message" message="true" property="commentsRequired" bundle="proposal">
                                   <li><bean:write name = "message"/></li>
                              </html:messages>
                              <html:messages id="message" message="true" property="commentsSize" bundle="proposal">
                                   <li><bean:write name = "message"/></li>
                              </html:messages>
                              <html:messages id="message" message="true" property="descriptionSize" bundle="proposal">
                                   <li><bean:write name = "message"/></li>
                              </html:messages>
                              <html:messages id="message" message="true" property="rejectioncomments" bundle="proposal">
                                   <li><bean:write name = "message"/></li>
                              </html:messages>
                              <html:messages id="message" message="true" property="descriptionRequired" bundle="proposal">
                                   <li><bean:write name = "message"/></li>
                              </html:messages>
                              <html:messages id="message" message="true" property="fileRequired" bundle="proposal">
                                   <li><bean:write name = "message"/></li>
                              </html:messages>
                              <%-- COEUSQA-1497: No ability to pass routing Authority in IRB Lite - Start--%>
                              <html:messages id="message" message="true" property="approverAlreadyPresent" bundle="proposal">
                                   <li><bean:write name = "message"/></li>
                              </html:messages>
                              <html:messages id="message" message="true" property="cannotRemovePrimApprover" bundle="proposal">
                                   <li><bean:write name = "message"/></li>
                              </html:messages>
                              <html:messages id="message" message="true" property="passCommentsReqd" bundle="proposal">
                                   <li><bean:write name = "message"/></li>
                              </html:messages>
                              <%-- COEUSQA-1497: No ability to pass routing Authority in IRB Lite - End--%>
                                 <html:messages id="message" message="true" property="acqLock" bundle="proposal">
                                    <script>errLock = true;</script>
                                   <li><bean:write name = "message"/></li>
                              </html:messages>
                          </logic:messagesPresent>
                        </font>
                        <font size=10px>
                    </td>
                </tr>
                <%String approveAll =(String) request.getAttribute("approveAll");%>
                <%if(approveAll==null){%>
                <tr>
                    <td class='tableheader' width='100%'>
                        <bean:message key="routing.addComments" bundle="proposal"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <table width='100%' border='0' cellpadding='0' cellspacing='0' class='tabtable'>
                            <logic:iterate id="dynaFormData" name="approvalRoutingList" property="list" type="org.apache.struts.action.DynaActionForm" indexId="index" scope="session">
                                <tr>
                                    <td width="7%" class='copybold' valign="top">
                                        &nbsp;&nbsp;<bean:message key="routing.commentsLabel" bundle="proposal"/>:
                                    </td>
                                    <td width="93%" align=left>
                                        &nbsp;<html:textarea property="comments" name="dynaFormData" indexed="true" styleClass="copy" cols="91" rows="5" onchange="setSaveConfirmationRequired();" disabled="<%=readOnly%>"/>
                                    </td>
                                </tr>
                            </logic:iterate>
                            <!-- COEUSQA-1433 - Allow Recall from Routing - Start -->
                            <%--if(label!=null && (!label.equals("ByPass"))){--%>
                            <%if(byPassFlag){%>
                                <%--No action required--%>
                            <%}else if(recallFlag){%>
                                <%--No action required--%>
                            <%}else{%>
                            <tr align=left valign="middle">
                                <td align=center height="30" colspan="2">&nbsp;&nbsp;&nbsp;&nbsp;
                                    &nbsp;<html:button property="addComments" value="   Add   " styleClass="clbutton" onclick="saveComments();" disabled="<%=readOnly%>"/>
                                </td>
                            </tr>
                            <%}%>
                            <!-- COEUSQA-1433 - Allow Recall from Routing - End -->
                            <logic:notEmpty name="approvalRoutingList" property="beanList" scope="session">
                            <tr>
                                <td align=left colspan="2">
                                    <table width='60%' border='0' cellpadding='0' cellspacing='0' class='tabtable'>
                                        <tr  class="theader">
                                            <td width="70%">
                                                <bean:message key="routing.listOfComments" bundle="proposal"/>
                                            </td>
                                            <td width="15%">

                                            </td>
                                            <td width="15%">

                                            </td>
                                        </tr>
                                        <logic:iterate id="dynaFormData" name="approvalRoutingList" property="beanList" type="org.apache.struts.action.DynaActionForm" indexId="index" scope="session">
                                            <%if (index.intValue()%2 == 0)
                                            strBgColor = "#D6DCE5";
                                            else
                                            strBgColor="#DCE5F1";%>
                                            <%
                                                String comments = (String) dynaFormData.get("comments");
                                                String editLink = "javascript:edit_comments('"+index+"')";
                                                String removeLink = "javascript:remove_comments('"+index+"')";
                                                if(comments == null){
                                                        comments = "";
                                                }
                                                String value = comments;
                                                 if(value != null && value.length() > 90){
                                                        value = value.substring(0,85);
                                                        value = value+" ...";
                                                    }
                                              %>
                                            <tr bgcolor="<%=strBgColor%>" valign="top" onmouseover="className='TableItemOn'" onmouseout="className='TableItemOff'">
                                                <td align="left" class="copy">
                                                     <html:link href="<%=editLink%>">
                                                             &nbsp;&nbsp;<%=value%>
                                                     </html:link>

                                                </td>
                                               <td class="copy">
                                                   <%
                                                     String viewLink = "javascript:view_comments('" +comments+"')";
                                                    %>
                                                    <html:link href="<%=viewLink%>">
                                                        <bean:message key="label.View"/>
                                                    </html:link>
                                               </td>
                                                <td align="center" class="copy">

                                                     <html:link href="<%=removeLink%>">
                                                          <bean:message key="routing.remove" bundle="proposal"/>
                                                     </html:link>

                                                </td>
                                            </tr>
                                        </logic:iterate>
                                    </table>
                                </td>
                            </tr>
                            <script> saveConfirmationReqd = 'true'; </script>
                            </logic:notEmpty>
                        </table>
                    </td>
                </tr>
                <!-- COEUSQA-1433 - Allow Recall from Routing - Start -->
                <%--if(label!=null && !label.equals("ByPass")){--%>
                <%if(byPassFlag){%>
                <%--No action required--%>
                <%}else if(recallFlag){%>
                    <tr>
                        <td align=left>

                            <% 
                            
                            String showInformation = "javascript:performValidation('"+isInLastApproval+"')";%>
                            <html:button property="Save" value="<%=label%>" styleClass="clbutton" disabled="<%=readOnly%>" onclick="<%=showInformation%>"/>
                            &nbsp;&nbsp;&nbsp;
                            <html:button property="cancel" value=" Back " styleClass="clbutton" onclick="goBack();"/>
                            <%if(newApproversPresent && !readOnly){%>
                            &nbsp;&nbsp;&nbsp;
                            <html:button property="Pass" value=" Pass " styleClass="clbutton" onclick="performPass();"/>
                            <%}%>
                        </td>
                    </tr>
                <%}else{%>
                <!-- COEUSQA-1433 - Allow Recall from Routing - End -->
                <tr>
                    <td class='tableheader' width='100%'>
                        <bean:message key="routing.attachments" bundle="proposal"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <table width='100%' border='0' cellpadding='0' cellspacing='0' class='tabtable'>
                            <logic:iterate id="dynaFormData" name="approvalRoutingList" property="infoList" type="org.apache.struts.action.DynaActionForm" scope="session">
                                <tr>
                                    <td width="7%" class="copybold" align=left valign="top">
                                        &nbsp;&nbsp;<bean:message key="routing.description" bundle="proposal"/>:
                                    </td>
                                    <td width="93%" align=left>
                                        &nbsp;<html:textarea property="description" name="dynaFormData" indexed="true" styleClass="copy" cols="90" rows="5" onchange="setSaveConfirmationRequired();" disabled="<%=readOnly%>"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td align=left class="copybold">
                                        &nbsp;&nbsp;<bean:message key="routing.attachment" bundle="proposal"/>:
                                    </td>
                                    <td align=left>
                                      &nbsp;<html:file property="attachments"  name="dynaFormData" indexed="true" onchange="setSaveConfirmationRequired();" disabled="<%=readOnly%>"/>
                                    </td>
                                </tr>
                            </logic:iterate>
                            <tr align=left valign="middle">
                                <td align=center height="30" colspan="2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <html:button property="addAttachments" value="   Add   " styleClass="clbutton" onclick="saveAttachments();" disabled="<%=readOnly%>"/>
                                </td>
                            </tr>
                            <logic:notEmpty name="approvalRoutingList" property="dataList" scope="session">
                                <tr>
                                    <td align=left colspan="2">
                                        <table width='60%' border='0' cellpadding='0' cellspacing='0' class='tabtable'>
                                            <tr  class="theader" c>
                                                <td width="70%">
                                                    <bean:message key="routing.attachmentDetails" bundle="proposal"/>
                                                </td>
                                                <td width="15%">

                                                </td>
                                                <td width="15%">

                                                </td>
                                            </tr>
                                            <logic:iterate id="dynaFormData" name="approvalRoutingList" property="dataList" type="org.apache.struts.action.DynaActionForm" indexId="index" scope="session">
                                                <%if (index.intValue()%2 == 0)
                                                strBgColor = "#D6DCE5";
                                                else
                                                strBgColor="#DCE5F1";%>
                                                <tr bgcolor="<%=strBgColor%>" valign="top" onmouseover="className='TableItemOn'" onmouseout="className='TableItemOff'">
                                                    <td align="left" class="copy">
                                                        <bean:write property="description" name="dynaFormData"/>
                                                    </td>
                                                    <%String removeLinkAttachments = "javascript:remove_attachments('"+index+"')";
                                                      String viewLinkAttachments = "javascript:view_attachments('"+index+"')";%>
                                                    <td align="left" class="copy">
                                                          <html:link href="<%=viewLinkAttachments%>">
                                                              <bean:message key="label.View"/>
                                                          </html:link>
                                                    </td>
                                                    <td align="left" class="copy">

                                                        <html:link href="<%=removeLinkAttachments%>">
                                                              <bean:message key="routing.remove" bundle="proposal"/>
                                                        </html:link>
                                                    </td>
                                                </tr>
                                            </logic:iterate>
                                        </table>
                                    </td>
                                </tr>
                                <script> saveConfirmationReqd = 'true'; </script>
                            </logic:notEmpty>
                        </table
                    </td>
                </tr>

                <%--  COEUSQA-1497: No ability to pass routing Authority in IRB Lite - Start --%>
                <%
                int maxStopNumber = 0;
                try{%>


                <logic:iterate id="dynaFormData" name="approvalRoutingList" property="list" type="org.apache.struts.action.DynaActionForm" indexId="index" scope="session">
                    <%

                    edu.mit.coeus.utils.CoeusVector newApproversList =(edu.mit.coeus.utils.CoeusVector) dynaFormData.get("newApprovers");
                    int size = 0;
                    if(newApproversList != null && !newApproversList.isEmpty()){
                        newApproversPresent = true;
                        size = newApproversList.size();

                    %>
                    <script> saveConfirmationReqd = 'true'; </script>
                    <tr>
                        <td class='tableheader' width='100%'>
                            New Approvers
                        </td>
                    </tr>

                    <tr>
                        <td >

                            <table width="60%" cellspacing="0" cellpadding="0" border="0" align="left"  class='table'>
                                <%

                                strBgColor = "#DCE5F1";
                                edu.mit.coeus.routing.bean.RoutingDetailsBean routingDetailsBean = null;
                                for(int listIndex = 0; listIndex <size; listIndex++){

                                    routingDetailsBean = ( edu.mit.coeus.routing.bean.RoutingDetailsBean) newApproversList.get(listIndex);

                                    maxStopNumber = routingDetailsBean.getStopNumber();

                                    if (routingDetailsBean.getStopNumber()%2 == 0)
                                        strBgColor = "#D6DCE9";
                                    else
                                        strBgColor="#DCE5F1";


                                %>
                                <tbody >

                                    <tr  bgcolor="<%=strBgColor%>">

                                        <%if(routingDetailsBean.isPrimaryApproverFlag()){%>
                                        <td  height='19' width="5%" class="copyBold"> <img src="<%=applicationPath%>/coeusliteimages/primary.gif"></td>
                                        <td height='19' width="55%" class="copyBold"> <%=routingDetailsBean.getUserName()%></td>
                                        <%
                                        String addAltApprLink = "javascript:addAlternateApprover('"+routingDetailsBean.getStopNumber()+"')";
                                        String removeApproverLink = "javascript:removeApprover('"+routingDetailsBean.getStopNumber()+"', '"+routingDetailsBean.getApproverNumber()+"', 'false')";
                                        %>
                                        <td  height='19' width="30%"> <a href="<%=addAltApprLink%>" >Add Altenate Approver </a></td>
                                        <td height='19'  width="10%"> <a href="<%=removeApproverLink%>" align="center" >Remove</a></td>
                                        <%} else {%>

                                        <td  height='19' width="5%" class="copyBold"> </td>
                                        <td height='19' width="55%" class="copyBold"><img src="<%=applicationPath%>/coeusliteimages/alternate.gif"> &nbsp;&nbsp; &nbsp; <%=routingDetailsBean.getUserName()%></td>
                                        <%
                                        String removeApproverLink = "javascript:removeApprover('"+routingDetailsBean.getStopNumber()+"', '"+routingDetailsBean.getApproverNumber()+"','true')";
                                        %>
                                        <td  height='19' width="30%"> </td>
                                        <td height='19'  width="10%"> <a href="<%=removeApproverLink%>" align="center" >Remove</a></td>

                                        <%}%>
                                    </tr>
                                </tbody>

                                <%}%>
                            </table>
                        </td>
                    </tr>

                    <%}%>
                </logic:iterate>

                <%}catch(Exception ex){
                ex.printStackTrace();
                }%>

                <tr>
                    <td>&nbsp;
                    </td>
                </tr>
                <tr>
                    <td align=left>
                        <%if("Reject".equalsIgnoreCase(label)){%>
                            <html:button property="Save" value="<%=label%>" styleClass="clbutton" disabled="<%=readOnly%>" onclick="performRejection();"/>
                        <% }else{%>
                             <html:submit property="Save" value="<%=label%>" styleClass="clbutton" disabled="<%=readOnly%>"/>
                        <%}%>
                        
                        &nbsp;&nbsp;&nbsp;
                        <html:button property="cancel" value=" Back " styleClass="clbutton" onclick="goBack();" />
                        <%if(newApproversPresent){%>
                        &nbsp;&nbsp;&nbsp;
                        <html:button property="Pass" value=" Pass " styleClass="clbutton" onclick="performPass();" disabled="<%=readOnly%>"/>
                        <%}%>
                        &nbsp;&nbsp;&nbsp;
                        <%if("Approve".equalsIgnoreCase(label)){%>
                        <%String link = "javaScript:addApprover('"+maxStopNumber+"')";%>
                        <%if(session.getAttribute("readOnly").equals(false)){%>
                        <html:link href="<%=link%>">
                            <u>Add Approver</u>
                        </html:link>
                              <%}%>
                        <%}%>
                    </td>

                </tr>


                <%}%>


                <%} else if(approveAll!=null && approveAll.equals("approveAll")){
                    edu.mit.coeus.utils.CoeusVector newApprovers = (edu.mit.coeus.utils.CoeusVector) request.getAttribute("newApprovers");
                    %>

                    <logic:iterate id="dynaFormData" name="approvalRoutingList" property="list" type="org.apache.struts.action.DynaActionForm" indexId="index" scope="session">
                    <%dynaFormData.set("newApprovers", newApprovers);%>
                     </logic:iterate>
                <%--  COEUSQA-1497: No ability to pass routing Authority in IRB Lite - End --%>
                <tr>
                    <td height='15'>
                    </td>
                </tr>
                <tr>
                    <td align=center class='copybold'>
                        <bean:message bundle="proposal" key="routing.approvalForStop"/>
                    </td>
                </tr>
                <tr>
                    <td>&nbsp;
                    </td>
                </tr>
                <tr>
                    <td align=center>
                        <!-- JM 10-11-2012 disable ability to approve at all stops
                        <html:button property="ok" value="Yes" styleClass="clbutton" onclick="approve('1')" disabled="<%=readOnly%>"/>
                        &nbsp;&nbsp;&nbsp;-->
                        <html:button property="cancel" value="No" styleClass="clbutton" onclick="approve('0')" disabled="<%=readOnly%>"/>

                    </td>
                </tr>
                <tr>
                    <td height='10'>
                    </td>
                </tr>
                <%}%>
                </logic:notPresent>
            </table>
        </html:form>
    </body>
</html>
