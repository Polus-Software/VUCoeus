<%-- 
    Document   : notes
    Created on : May 8, 2010, 12:44:31 PM
    Author     : Mr
--%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="edu.mit.coeus.bean.PersonInfoBean,edu.mit.coeuslite.coiv2.beans.Coiv2NotesBean,java.util.Vector,edu.mit.coeuslite.coiv2.beans.Coiv2AttachmentBean,edu.mit.coeuslite.coiv2.utilities.CoiConstants,edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%String path = request.getContextPath();
                    PersonInfoBean loggedPer = (PersonInfoBean) session.getAttribute("LOGGED_IN_PERSON");
                    String loggedperson = loggedPer.getUserId();
      boolean disable=false;
        boolean isAdmin=false;
       boolean isReviewer=false;
       
       if(request.getAttribute("isAdmin") != null) {
            isAdmin=(Boolean)request.getAttribute("isAdmin");
             }
       if(request.getAttribute("isReviewer") != null) {
       isReviewer=(Boolean)request.getAttribute("isReviewer");
       }           
         
       
      
                  
            %>
        <script type="text/javascript">
            function editNotes(entityNum,comments,title,restricedView,notepadEnrtyType,user)
            {
                var cannnotContinue=true;
                var loggedPerson='<%=loggedperson%>'
                if(loggedPerson!=user)
                {
                    alert("Permission Denied")
                    cannnotContinue=false;
                }
                if(cannnotContinue==true){
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
                var cannnotContinue=true;
                var loggedPerson='<%=loggedperson%>'
                if(loggedPerson!=user)
                {
                    alert("You don\'t have permission to remove the notes")
                    cannnotContinue=false;
                }
                if(cannnotContinue==true){
                    if (confirm("Are you sure you want to delete the note?")==true){
                        var isViewer='<%=request.getAttribute("isViewer")%>';
                        var operationType='<%= request.getAttribute("operationType")%>';
                        document.forms[0].entiryNumber.value = entityNum;
                        document.forms[0].coiDisclosureNumber.value = coiDisclosureNumber;
                        document.forms[0].coiSequenceNumber.value = coiSequenceNumber;
                        document.forms[0].acType.value = "R";
                        document.forms[0].action= '<%=path%>' +"/saveNotesCoiv2.do?&operationType="+operationType+"&isViewer="+isViewer;
                        document.forms[0].submit();
                    }
                }
            }
            function saveNotes()
            {
                var success=validateNotes();
                var isViewer='<%=request.getAttribute("isViewer")%>';
                if(success==true){
                    var operationType='<%= request.getAttribute("operationType")%>';
                    document.forms[0].action= '<%=path%>' +"/saveNotesCoiv2.do?&operationType="+operationType+"&isViewer="+isViewer;
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
            function setFocus()
            {
                document.forms[0].title.focus();
            }
             function saveAndcontinue()
            {
                document.forms[0].action= '<%=path%>'+"/certify.do";
                document.forms[0].submit();
            }

        </script>
        <script src="<%=path%>/coeuslite/dartmouth/utils/scripts/divSlide.js"></script>
        <script src="<%=path%>/coeuslite/dartmouth/utils/scripts/Balloon.js"></script>
        <link href="<%=path%>/coeuslite/dartmouth/utils/css/coeus_styles.css" rel="stylesheet" type="text/css"/>
        <link href="<%=request.getContextPath()%>/coiv2/css/layout.css" rel="stylesheet" type="text/css" />
        <link href="<%=request.getContextPath()%>/coiv2/css/styles.css" rel="stylesheet" type="text/css" />
        <link href="<%=request.getContextPath()%>/coiv2/css/collapsemenu.css" rel="stylesheet" type="text/css" />
    </head>
    <body>
       
        <html:form action="/saveNotesCoiv2.do">
<div id="notesProtocol" style="vertical-align: top">
   <%-- <logic:present name="userHaveRight">
       <logic:equal name="userHaveRight" value="true">--%>

   
    

 <logic:notPresent name="showAttachment">
          <table style="width: 100%;" height="100%"  border="0" cellpadding="0" cellspacing="0" class="table" align='center'>
              
              <tr><td class="theader" colspan="2">Disclosure Notes</td></tr>
              <tr><td class="copybold" align="left" valign="top">
                    <bean:message key="protocolNote.label.addNotes"/>
              </td></tr>
              <tr><td>

         <table width="100%"  border="0" cellpadding="0"> 
             <tr>
                 <td align="left" class="copybold"  style="width: 20%;">Note Topic:</td>
                 <td ><html:text   styleClass="copy"  name="coiv2NotesBean" property="title" maxlength="500"/></td>
                 <td align="left" class="copybold" style="width: 50%;">
                     Note Type:&nbsp;
                      <logic:notEmpty name="notepadTypeList">
                      <html:select property="coiNotepadEntryTypeCode" name="coiv2NotesBean" style="width:200px" styleClass="textbox-long" >
                      <html:option value=""><bean:message key="specialReviewLabel.pleaseSelect"/></html:option>
                      <html:options collection="notepadTypeList" property="code" labelProperty="description"  />>
                      </html:select>
                      </logic:notEmpty>
                      <logic:empty name="notepadTypeList">
                      <html:select  property="coiNotepadEntryTypeCode"  name="coiv2NotesBean" styleClass="textbox-long" style="width:200px">
                      <html:option value=""><bean:message key="specialReviewLabel.pleaseSelect"/></html:option>
                      </html:select>
                      </logic:empty>
                 </td>
                 <td align="left" class="copybold" style="width: 20%">
                    <% if((isAdmin)||(isReviewer)){%>
                     Private Flag:
                     <%
                         disable=false;
                     %>
                     <html:checkbox styleClass="copy" disabled="<%=disable%>" name="coiv2NotesBean" value="R" property="resttrictedView"/>
                <%}%>
                     </td>
            </tr>
            <tr>
                 <td class="copybold">
                        <%-- Commented for case id#2627
                           <font color="red">*</font>
                           <bean:message key="protocolNote.label.comment"/>:--%>
                     Note Text:
                 </td>
                 <td colspan="3">
                      <html:textarea styleClass="copy" property="comments" name="coiv2NotesBean" cols="100" rows="5"/>
                 </td>
           </tr>
        </table>
           </td></tr>

              <tr style="width: 100px;">
                  <td style="width: 50px;">
                   <html:button property="Save" styleClass="clsavebutton"   onclick="javaScript:saveNotes();">
                   <bean:message key="protocolNote.button.save"/>
                   </html:button>                    
             </td>
              </tr>
          </table>
 </logic:notPresent>
  
       <%-- </logic:equal>
   </logic:present>--%>
                     
                     
        <table id="noteBodyTable" class="table" style="width: 100%;" border="0" cellpadding="4" >
                <tr style="background-color:#6E97CF;height: 22px;width: 100%;">
                    <td style="color:#FFFFFF;font-size:12px;font-weight:bold;padding: 2px 0 2px 10px;" colspan="4">
                        List of Notes
                    </td>
                <logic:equal name="fromViewCurrent" value="true">
                    <td>&nbsp;</td>
                </logic:equal>
                </tr>
                <tr>
                     <td class="theader" align='left' style="width: 20%;">Topic</td>
 <%--code added to bring forward previously approved sequences notes start--%>
                     <logic:equal name="fromViewCurrent" value="true">
                         <td class="theader" align='left' >Project #:</td>
                     </logic:equal>
<%--code added to bring forward previously approved sequences notes ends?--%>
                     <td class="theader" align='left' >Note</td>
                     <td class="theader" align='left' style="width: 25%">Update Timestamp</td>
                     <td class="theader" align='left' style="width:10px;"></td>
                </tr>
                
    
                <logic:empty name="disclosureNotesData">
                    <tr>
                        <td colspan="8">
                            <font color="red">No notes found</font>
                        </td>
                    </tr>
                 </logic:empty>
            <%-- <logic:present name="message">
                    <logic:equal value="false" name="message">
                <tr>
                    <td colspan="4"><font color="red">No notes found</font></td>
                    <logic:equal name="fromViewCurrent" value="true">
                        <td>&nbsp;</td>
                    </logic:equal>
               </tr>
                    </logic:equal>
            </logic:present>--%>

         <logic:present name="disclosureNotesData">
                    <%
                                String notstrBgColor = "#DCE5F1";
                                int indexnote = 0;
                                int i = 0;
                                Vector notesList = (Vector) request.getAttribute("disclosureNotesData");
                    %>
                    <logic:iterate id="disclosureNotes"  name="disclosureNotesData" type="edu.mit.coeuslite.coiv2.beans.Coiv2NotesBean">
                        <%
                                    if (indexnote % 2 == 0) {
                                        notstrBgColor = "#D6DCE5";
                                    } else {
                                        notstrBgColor = "#DCE5F1";
                                    }
                                boolean isShow=false;
                                    Coiv2NotesBean noteBean = (Coiv2NotesBean) notesList.get(i);
                                    String moduleItmKey=noteBean.getModuleItemKey();
                                    if(moduleItmKey==null || moduleItmKey.equalsIgnoreCase(""))
                                        {
                                        moduleItmKey="";
                                    }
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
                                         isShow=true;
                                         }
                                                           
                        %>
                       <%if(isShow){%>
                            
                        <tr bgcolor="<%=notstrBgColor%>" id="row<%=indexnote%>" class="rowLine" onmouseover="className='rowHover rowLine'" onmouseout="className='rowLine'" height="22px">
                           <td style="width: 20%;"><bean:write name="disclosureNotes" property="title"/></td>
<%--code added to bring forward previously approved sequences notes start--%>
                    <logic:equal name="fromViewCurrent" value="true">
                           <td style="width: 20%;" ><%=moduleItmKey%></td>
                           <td align="justify" ><bean:write name="disclosureNotes" property="comments"/></td>
                    </logic:equal>
                    <logic:notEqual name="fromViewCurrent" value="true">
                            <td align="justify" ><bean:write name="disclosureNotes" property="comments"/></td>
                    </logic:notEqual>
<%--code added to bring forward previously approved sequences notes ends--%>
                            <td style="width: 25%;"><bean:write name="disclosureNotes" property="updateTimestamp"/>by
                             <bean:write name="disclosureNotes" property="updateUserName"/>
                            </td>
                            <td class="copy" align='left' style="width:10px;">

                                        <%String link1 = "javaScript:removeNotes('" + noteBean.getEntiryNumber() + "','" + noteBean.getCoiDisclosureNumber() + "','" + noteBean.getCoiSequenceNumber() + "','" + noteBean.getUpdateUser() + "')";%>
                                        <html:link href="<%=link1%>"> 
                                            Remove
                                        </html:link>
                                    

                            </td>
                        </tr>
                        <%}indexnote++;%>
                        
                    </logic:iterate>
                </logic:present>
                        <tr>
                            <td>
                                  <html:button onclick="javaScript:saveAndcontinue();" property="Save" styleClass="clsavebutton">
                                     Continue
                                 </html:button>
                            </td>
                        </tr>
            </table></div>
            <html:text  style="visibility: hidden" property="entiryNumber" name="coiv2NotesBean"/>
            <html:text  style="visibility: hidden" property="coiDisclosureNumber" name="coiv2NotesBean"/>
            <html:text  style="visibility: hidden" property="coiSequenceNumber" name="coiv2NotesBean"/>
            <html:text  style="visibility: hidden" property="acType" name="coiv2NotesBean"/>

      
</html:form>
</body>
</html>
