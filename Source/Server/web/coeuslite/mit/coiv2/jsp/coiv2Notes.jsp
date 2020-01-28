<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@ page import="edu.mit.coeus.bean.PersonInfoBean,edu.mit.coeuslite.coiv2.services.CoiCommonService,edu.mit.coeuslite.coiv2.beans.CoiPersonProjectDetails,edu.mit.coeuslite.coiv2.beans.Coiv2NotesBean,java.util.Vector,java.util.HashMap,edu.mit.coeuslite.coiv2.utilities.CoiConstants"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld"  prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld"  prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"  prefix="coeusUtils"%>
<%    Vector notesList = (Vector) request.getAttribute("disclosureNotesData");
          String startDate = "";
          String endDate = "";
          String pjctTitle="";
          String moduleItemKey="";
          String awardTitle="";
          String pjctId="";
          String eventName="";
          int code=0;            
            boolean disable = false;            
            String path = request.getContextPath();
            PersonInfoBean loggedPer = (PersonInfoBean) session.getAttribute("LOGGED_IN_PERSON");
            String loggedperson=loggedPer.getUserId();
            //code added for displaying project details...starts
            String projectType = (String) request.getSession().getAttribute("projectType");
            if(projectType.equalsIgnoreCase("Proposal")||projectType.equalsIgnoreCase("iacucProtocol")||projectType.equalsIgnoreCase("Protocol")||projectType.equalsIgnoreCase("Award")||projectType.equalsIgnoreCase("Travel")){
            Vector pjctDetails = (Vector)request.getSession().getAttribute("projectDetailsListInSeesion");
            if(pjctDetails !=null){
            CoiPersonProjectDetails coiPersonProjectDetails =(CoiPersonProjectDetails) pjctDetails.get(0);
            code=coiPersonProjectDetails.getModuleCode() ;
            pjctTitle=coiPersonProjectDetails.getCoiProjectTitle();
            moduleItemKey=coiPersonProjectDetails.getModuleItemKey();
            awardTitle=coiPersonProjectDetails.getAwardTitle();
            pjctId=coiPersonProjectDetails.getModuleItemKey();
            eventName=coiPersonProjectDetails.getEventName();
            CoiCommonService coiCommonService1 = CoiCommonService.getInstance();
            if(coiPersonProjectDetails.getCoiProjectStartDate()!=null)
            {
                startDate = (String) coiCommonService1.getFormatedDate(coiPersonProjectDetails.getCoiProjectStartDate());
            }
            if(coiPersonProjectDetails.getCoiProjectEndDate()!=null)
            {
              endDate = (String) coiCommonService1.getFormatedDate(coiPersonProjectDetails.getCoiProjectEndDate());
            }}}
    //code added for displaying project details...ends
            
               
        boolean isAdmin=false;
       boolean isReviewer=false;
       isAdmin=(Boolean)request.getAttribute("isAdmin");
       isReviewer=(Boolean)request.getAttribute("isReviewer");    
          
           
%>

<html:html locale="true">

    <head>
        <title>Disclosure Notes</title>
        <script language="javaScript">
            function editNotes(entityNum,comments,title,restricedView,notepadEnrtyType,user)
            {
                var cannnotContinue='<%=disable%>';
                 var loggedPerson='<%=loggedperson%>'
                if(loggedPerson!=user)
                    {
                        alert("Persission Denied")
                        cannnotContinue='false';
                    }
                if(cannnotContinue=='false'){
                    document.forms[0].entiryNumber.value = entityNum;
                    document.forms[0].comments.value = comments;
                    document.forms[0].title.value = title;
                    document.forms[0].acType.value = "U";

                    document.forms[0].coiNotepadEntryTypeCode.value = notepadEnrtyType;
                    if(restricedView!=null && restricedView=="R"){
                        document.forms[0].resttrictedView.checked = true;
                    }
                }
            }
            function removeNotes(entityNum,coiDisclosureNumber,coiSequenceNumber,user)
            {
                var cannnotContinue='<%=disable%>';
                 var loggedPerson='<%=loggedperson%>'
                if(loggedPerson!=user)
                    {
                        alert("Persission Denied")
                        cannnotContinue='false';
                    }
                if(cannnotContinue=='false'){
                      if (confirm("Are you sure you want to delete the notes?")==true){
                    var operationType='<%= request.getAttribute("operationType")%>'
                    document.forms[0].entiryNumber.value = entityNum;
                    document.forms[0].coiDisclosureNumber.value = coiDisclosureNumber;
                    document.forms[0].coiSequenceNumber.value = coiSequenceNumber;
                    document.forms[0].acType.value = "R";
                    document.forms[0].action= '<%=path%>' +"/saveNotesCoiv2.do?operationType="+operationType;
                    document.forms[0].submit();
                    }
                }
            }
            function saveNotes()
            {
                var success=validateNotes();
                if(success==true){
                    var operationType='<%= request.getAttribute("operationType")%>'
                    document.forms[0].action= '<%=path%>' +"/saveNotesCoiv2.do?operationType="+operationType;
                    document.forms[0].submit();
                }

            }
            function validateNotes()
            {
                if(document.forms[0].title.value==null || document.forms[0].title.value.replace(/^\s+/,'').replace(/\s+$/,'')=='')
                {
                    alert('Please enter Note Topic');
                    document.forms[0].title.focus();
                    return false;

                }
                if(document.forms[0].coiNotepadEntryTypeCode.value==null || document.forms[0].coiNotepadEntryTypeCode.value.replace(/^\s+/,'').replace(/\s+$/,'')=='')
                {
                    alert('Please select Note Type');
                    document.forms[0].coiNotepadEntryTypeCode.focus();
                    return false;
                }
                if(document.forms[0].comments.value==null || document.forms[0].comments.value.replace(/^\s+/,'').replace(/\s+$/,'')=='')
                {
                    alert('Please enter Note Text');
                    document.forms[0].comments.focus();
                    return false;
                }

                return true;
            }
            function saveAndcontinue()
            {
                var operationType= '<%= request.getAttribute("operationType")%>'
                document.forms[0].action= '<%=path%>' +"/getCertificationCoiv2.do?operationType="+operationType;
                document.forms[0].submit();
            }
            function setFocus()
            {
                document.forms[0].title.focus();
            }
            function imposeMaxLength(Object, MaxLen)
            {
                return (Object.value.length <= MaxLen);
            }

             function exitToCoi(){
                var answer = confirm( "This operation will discard current changes.  Prior screen entries will be saved.  You may continue this disclosure event by selecting it from the Pending section of your COI home page.  Click 'OK' to continue." );
                 if(answer) {
                    document.location = '<%=request.getContextPath()%>/exitDisclosure.do';
                    window.location;
                 }
            }
        </script>

    </head>
    <body onload="javaScript:setFocus();">
       <html:form action="/saveNotesCoiv2.do">
   <div id="notesProtocol" style="vertical-align: top">
              <table style="width: 100%;" border="0" cellpadding="2" cellspacing="0" class="table" >
    <%--     code added for displaying project details...starts--%>
               <%
               if(code==7){
                    projectType="IRB Protocol";
                }
                if(code==9){
                    projectType="IACUC Protocol";
                }
                if(projectType.equals("IRB Protocol") || projectType != null && projectType.equalsIgnoreCase("IACUC Protocol")){%>
                <tr><td colspan="4"><b>For your project listed below, please answer the certification questionnaire:</b></td></tr>
                        <tr><td align="right" width="15%" style="float: none" nowrap><b><%=projectType%> # :</b></td>
                            <td style="float: none" align="left" width="35%"><%=moduleItemKey%></td>
                            <td width="15%" style="float: none"></td>
                            <td style="float: none" width="35%"></td>
                                </tr>
                        <tr><td align="right" width="15%" style="float: none" nowrap><b>Title :</b></td>
                            <td colspan="3" style="float: none" align="left" width="35%"><%=pjctTitle%></td>
                        <%--<td width="15%" style="float: none"></td>
                            <td style="float: none" width="35%"></td>--%>
                        </tr>
                        <tr>
                            <td align="right" width="15%" style="float: none" nowrap><b>Application Date :</b></td><td style="float: none" align="left" width="35%"><%=startDate%></td>
                            <td align="right" nowrap width="18%" style="float: none"><b>Expiration Date :</b></td><td align="left" width="35%"><%=endDate%></td>
                        </tr>
                <%}else if(projectType.equals("Proposal")){%>
                         <tr><td colspan="4"><b>For your project listed below, please answer the certification questionnaire:</b></td></tr>
                        <tr><td align="right" width="15%" style="float: none" nowrap><b>Proposal # :</b></td>
                            <td style="float: none" align="left" width="35%"><%=moduleItemKey%></td>
                            <td width="20%" style="float: none"></td>
                            <td style="float: none"></td>
                                </tr>
                        <tr><td align="right" width="15%" style="float: none" nowrap><b>Title :</b></td>
                            <td colspan="3" style="float: none" align="left" width="35%"><%=pjctTitle%></td>
                        <%--<td width="20%" style="float: none"></td>
                            <td style="float: none"></td>--%>
                        </tr>
                        <tr>
                            <td align="right" width="15%" style="float: none" nowrap><b>Start Date :</b></td><td style="float: none" align="left" width="35%"><%=startDate%></td>
                            <td align="right" width="10%" style="float: none"><b>End Date :</b></td><td align="left" width="38%"><%=endDate%></td>
                        </tr>
                <%} else if(projectType.equals("Award")){ %>
                     <tr><td colspan="4"><b>For your project listed below, please answer the certification questionnaire:</b></td></tr>
                        <tr><td align="right" width="15%" style="float: none" nowrap><b>Award # :</b></td>
                            <td  style="float: none" align="left" width="35%"><%=moduleItemKey%></td>
                            <td width="15%" style="float: none"></td>
                            <td style="float: none"></td>
                                </tr>
                        <tr><td align="right" width="15%" style="float: none" nowrap><b>Title:</b></td>
                            <td align="left" style="float: none" align="left" width="35%"><%=pjctTitle%></td>
                        <td width="15%" style="float: none"></td>
                            <td style="float: none"></td>
                        </tr>
                        <tr>
                            <td align="right" width="15%" style="float: none" nowrap><b>Start Date :</b></td><td style="float: none" align="left" width="35%"><%=startDate%></td>
                            <td align="right" width="15%" style="float: none"><b>End Date :</b></td><td align="left" width="30%"><%=endDate%></td>
                        </tr>
               <% } else if(projectType.equals("Travel")){ %>
                     <tr><td colspan="4"><b>For your project listed below, please answer the certification questionnaire:</b></td></tr>
                        <tr><td align="right" width="15%" style="float: none" nowrap><b>Travel # :</b></td>
                            <td style="float: none" align="left" width="35%"><%=moduleItemKey%></td>
                            <td width="15%" style="float: none"></td>
                            <td style="float: none"></td>
                                </tr>
                        <tr><td align="right" width="15%" style="float: none" nowrap><b>Title :</b></td>
                            <td colspan="3" style="float: none" align="left" width="35%"><%=pjctTitle%></td>
                      <%--  <td width="15%" style="float: none"></td>
                            <td style="float: none"></td>--%>
                        </tr>
                        <tr>
                            <td align="right" width="15%" style="float: none" nowrap><b>Start Date :</b></td><td style="float: none" align="left" width="35%"><%=startDate%></td>
                            <td align="right" width="18%" style="float: none"><b>End Date :</b></td><td align="left" width="30%"><%=endDate%></td>
                        </tr>
               <% } %>
        <%--      code added for displaying project details...ends--%>
                        <tr>
                            <td colspan="4">
                                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                    <tr> 
                                        <td height="20" align="left" valign="top" class="theader">
                                            Disclosure Notes
                                        </td>
                                        <td align="right" width="50%" class="theader">
                                            <a id="helpPageText" href="#">
                                            </a>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <!-- Add Disclosure Notes - Start  -->
                        <tr>
                            <td colspan="4" align="left" valign="top" class='core'>
                                <div id='open_window' style="vertical-align: top">
                                    <table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0" class="tabtable">
                                         <tr>
                                            <td>
                                                <table width="100%"  border="0" cellpadding="0">
                                                    <tr>
                                                        <td align="left" class="copybold"  style="width: 80px;">
                                                            Note Topic:
                                                        </td>
                                                        <td >
                                                            <html:text   styleClass="copy" disabled="<%=disable%>" property="title" maxlength="300"/>
                                                        </td>
                                                        <td align="left" class="copybold" style="width: 300px;">
                                                           <logic:notEmpty name="notepadTypeList">
                                                                Note Type:
                                                                <html:select property="coiNotepadEntryTypeCode" style="width:200px" styleClass="textbox-long" disabled="<%=disable%>" >
                                                                    <html:option value=""><bean:message key="specialReviewLabel.pleaseSelect"/></html:option>
                                                                    <html:options collection="notepadTypeList" property="code" labelProperty="description"  />>
                                                                </html:select>
                                                            </logic:notEmpty>
                                                            <logic:empty name="notepadTypeList">
                                                                Note Type:
                                                                &nbsp;<html:select  property="coiNotepadEntryTypeCode" disabled="<%=disable%>" styleClass="textbox-long" style="width:200px">
                                                                    <html:option value=""><bean:message key="specialReviewLabel.pleaseSelect"/></html:option>
                                                                </html:select>
                                                            </logic:empty>
                                                        </td>
                                                        <td align="left" class="copybold" style="width: 150px;">
                                                           <%if((isAdmin)||(isReviewer)){%>
                                                            Private Flag:                                                                                                    
                                                                       <%
                                                                           disable=false;
                                                                                                                                                                
                                                                                    %>                               
                                                            <html:checkbox styleClass="copy" disabled="<%=disable%>" value="R" property="resttrictedView"/>
                                                       <%}%>
                                                       
                                                        
                                                        </td>
                                                        <td align="left" style="width: 50px;">
                                                            &nbsp;
                                                        </td>
                                                        <td>
                                                            &nbsp;
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td class="copybold">
                                                            Note Text:
                                                        </td>
                                                        <td colspan="5">
                                                            <%disable=false;%>
                                                            <html:textarea styleClass="copy" disabled="<%=disable%>" property="comments" cols="120" rows="5"  onkeypress="return imposeMaxLength(this,1600);" />
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td height='10'>
                                                &nbsp;
                                            </td>
                                        </tr>
                                        <tr class="table">
                                            <td class='savebutton'>                                                
                                                <html:button property="Save" styleClass="clsavebutton" disabled="<%=disable%>"  onclick="javaScript:saveNotes();">
                                                    <bean:message key="protocolNote.button.save"/>
                                                </html:button>
                                            </td>
                                        </tr>
                                    </table>
                                </div>
                            </td>
                        </tr>
                        <!-- Add Disclosure Notes - Ends  -->
                        <!-- List of Disclosure Notes Start -->
                        <tr>
                            <td colspan="5" align="left" valign="top" class='core'>
                                <table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0" class="tabtable">

                                    <tr>
                                        <td colspan="8" align="left" valign="top">
                                            <table width="100%" height="20"  border="0" cellpadding="0" cellspacing="0" class="tableheader">
                                                <tr>
                                                    <td>
                                                        List of Disclosure Notes
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                     <tr>
                                        <td class="theader" align='left' style="width: 27%;">
                                           Topic
                                          </td>
                                        <td class="theader" align='left' style="width: 38%">
                                           Note
                                        </td> 
                                         <td class="theader" align='left' style="width: 30%">
                                             Update Timestamp
                                         </td>
                                         <td class="theader" align='left'colspan="2">
                                          </td>
                                    </tr>
                                    <%
                                                String strBgColor = "#D6DCE5";
                                                int index = 0;
                                                int i = 0;

                                    %>
                                    <logic:present name="disclosureNotesData">
                                        <logic:iterate id="disclosureNotes"  name="disclosureNotesData" type="edu.mit.coeuslite.coiv2.beans.Coiv2NotesBean">
                                            <%
                                                 boolean isShow=false;
                                                        if (index % 2 == 0) {
                                                            strBgColor = "#D6DCE5";
                                                        } else {
                                                            strBgColor = "#DCE5F1";
                                                        }
                                                        Coiv2NotesBean noteBean = (Coiv2NotesBean) notesList.get(i);
                                                        i++;
                                                         String userId=noteBean.getUpdateUser();
                                     if(noteBean.getResttrictedView().toString().trim().equalsIgnoreCase("R")){
                                         if(isAdmin){
                                             isShow=true;
                                         }else if(loggedperson.equalsIgnoreCase(userId)){
                                             isShow=true;
                                         }else{
                                             isShow=false;
                                         }
                                     }else {
                                          isShow = true;
                                       }
                                            %>
                                           <%if(isShow){%>
                                            <tr bgcolor="<%=strBgColor%>" class="rowLine" onmouseover="className='rowHover rowLine'" onmouseout="className='rowLine'">
                                              <%--   <tr bgcolor="<%=notstrBgColor%>" id="row<%=indexnote%>" class="rowLine" onmouseover="className='rowHover rowLine'" onmouseout="className='rowLine'" height="22px">--%>
                                              <td align='left' style="width: 27%;">
                                               <%-- <td class="copy" align='left' style="width: 25%;">--%>
                                                    <bean:write name="disclosureNotes" property="title"/>
                                                </td>
                                                <td align='left' style="width: 38%">
                                               <%-- <td class="copy" align='left' style="width: 25%;">--%>
                                                    <bean:write name="disclosureNotes" property="comments"/>
                                                </td>
                                                <%--<td class="copy" align='center'>
                                                    <bean:write name="disclosureNotes" property="updateUser"/>
                                                </td>--%>
                                                <td align='left' style="width: 30%">
                                               <%-- <td class="copy" style="width: 25%;">--%>
                                                    <bean:write name="disclosureNotes" property="updateTimestamp"/> by
                                                    <bean:write name="disclosureNotes" property="updateUserName"/>
                                                </td>
                                                <td class="copy" align='center'>
                                                    <%
                                                        String link = "javaScript:editNotes('" + noteBean.getEntiryNumber() + "','" + noteBean.getComments() + "','" + noteBean.getTitle() + "','" + noteBean.getResttrictedView() + "','" + noteBean.getCoiNotepadEntryTypeCode() + "','" + noteBean.getUpdateUser() + "')";
                                                    %>
                                                    <html:link href="<%=link%>">
                                                        Edit
                                                    </html:link>
                                                </td>
                                                <td class="copy" align='center'>
                                                    <%
                                                    String link1 = "javaScript:removeNotes('" + noteBean.getEntiryNumber() + "','" + noteBean.getCoiDisclosureNumber() + "','" + noteBean.getCoiSequenceNumber() + "','" + noteBean.getUpdateUser() + "')";
                                                    %>
                                                    <html:link href="<%=link1%>">
                                                        &nbsp;&nbsp;Remove
                                                    </html:link>&nbsp;&nbsp;&nbsp;
                                                </td>
                                            </tr>
                                            <tr>
                                                <td height=2>
                                                </td>
                                            </tr>
                                            <%
                                                  } index++;
                                            %>
                                           
                                            
                                        </logic:iterate>
                                    </logic:present>
                                    <td class='savebutton' align="left" colspan="4">
                                        <html:button onclick="javaScript:saveAndcontinue();" property="Save" styleClass="clsavebutton" style="width:150px;">
                                            Continue
                                        </html:button>
                                  
                                        <html:button onclick="javaScript:exitToCoi();" property="Save" styleClass="clsavebutton" style="width:150px;">
                                            Quit
                                        </html:button>
                                    </td>
                                </table>
                            </td>
                        </tr>
                        <!-- List of Disclosure Notes End -->
                    </table>
                </div>
<html:text  style="visibility: hidden" property="entiryNumber"/>
 <html:text  style="visibility: hidden" property="coiDisclosureNumber"/>
 <html:text  style="visibility: hidden" property="coiSequenceNumber"/>
 <html:text  style="visibility: hidden" property="acType"/>
</html:form>
</body>
</html:html>