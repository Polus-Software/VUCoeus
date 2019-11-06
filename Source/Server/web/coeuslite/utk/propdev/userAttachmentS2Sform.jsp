<%-- 
    Document   : userAttachmentS2Sform
    Created on : Sep 3, 2013, 9:40:50 AM
    Author     : polusdev
--%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<%@ page import="java.util.Vector,edu.mit.coeus.utils.S2SConstants,edu.mit.coeus.s2s.bean.UserAttachedS2SFormBean,
         java.util.List,edu.wmc.coeuslite.utils.CoeusLineItemDynaList, org.apache.struts.validator.DynaValidatorForm"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld"  prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld"  prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"  prefix="coeusUtils"%>

<jsp:useBean id="summary" scope="session" class="java.util.ArrayList" />
<jsp:useBean id="lstUserAttachmentS2S" scope="session" class="java.util.ArrayList" />

<html:html locale="true">
    <head>
        <link href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css" rel="stylesheet" type="text/css">
    </head>
    <title>User Attached S2S Forms</title>
    <script language="JavaScript">
        var errValue = false;
        var errLock = false;    
        function called() {
            document.getElementById('attachmentId').style.display = 'block';
            document.getElementById('header_label').style.display = 'none';  
        }
    
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
    
        function toggle_Data(){
            if(document.userAttachmentS2Sform.acType.value == 'U'){
                called();
            }
        }
    
        function selectFile(){
            dataChanged();
            document.userAttachmentS2Sform.fileName.value = document.userAttachmentS2Sform.document.value;
            document.userAttachmentS2Sform.fileHidden.value = document.userAttachmentS2Sform.document.value;
        }

        function edit_userAttS2S_Data(data, selectedValue){
            document.userAttachmentS2Sform.acType.value = data;        
            document.userAttachmentS2Sform.action = "<%=request.getContextPath()%>/editUserAttS2SDetails.do?selectedIndex="+selectedValue;
            document.userAttachmentS2Sform.submit();
        }
    
        function save_userAttS2S_Data(){
            var isValid = validate_upload_form();
            if(isValid){
                if(document.userAttachmentS2Sform.acType.value == null || document.userAttachmentS2Sform.acType.value.length == 0){
                    document.userAttachmentS2Sform.acType.value = 'I';
                    document.userAttachmentS2Sform.action = "<%=request.getContextPath()%>/saveUserAttS2SDetails.do";
                    document.userAttachmentS2Sform.submit();
                }else{            
                    var selectedValue = document.userAttachmentS2Sform.selectedRow.value;
                    document.userAttachmentS2Sform.acType.value = 'U';
                    document.userAttachmentS2Sform.action = "<%=request.getContextPath()%>/saveUserAttS2SDetails.do?selectedIndex="+selectedValue;
                    document.userAttachmentS2Sform.submit();
                }
            }
        }
    
        function delete_userAttS2S_Data(data, selectedValue){
            var msg = '<bean:message bundle="proposal" key="userAttachS2S.delete.confirm" />';
            if (confirm(msg)==true){
                document.userAttachmentS2Sform.acType.value = data;
                document.userAttachmentS2Sform.action = "<%=request.getContextPath()%>/deleteUserAttS2SDetails.do?deletedIndex="+selectedValue;
                document.userAttachmentS2Sform.submit(); 
            }
        }
    
        function validate_upload_form(){
            var description = document.userAttachmentS2Sform.description.value;
            var filename = document.userAttachmentS2Sform.fileName.value;
            var validateDescription = false;
            var validateFile = false;
            var validateFlag = false;
            if(description != null && description.length >0){
                <!-- Do Nothing -->
                validateDescription = true;
            }else{
                alert('<bean:message key="userAttachS2S.save.DescriptionValidate" bundle="proposal"/>');
                validateDescription = false;
                document.userAttachmentS2Sform.description.focus();
                return false;
            }
            
            
            if(filename != null && filename.length >0){
                <!-- Do Nothing -->
                validateFile = true;
            }else{
                alert('<bean:message key="userAttachS2S.save.PdfValidate" bundle="proposal"/>');
                validateFile = false;
                document.userAttachmentS2Sform.fileName.focus();
            }
            
            if( validateDescription && validateFile ){
                validateFlag = true;
            }
            return validateFlag;
        }
    
        function view_form_data(file, selectedValue){
            window.open("<%=request.getContextPath()%>/viewUserAttS2SDetails.do?fileType="+file+"&selectedRow="+selectedValue); 
        }
    
        function view_xml_data(file, selectedValue){
            window.open("<%=request.getContextPath()%>/viewUserAttS2SDetails.do?fileType="+file+"&selectedRow="+selectedValue); 
         <%-- window.open("<%=request.getContextPath()%>/viewUserAttS2SDetails.do?fileType="+file+"&selectedRow="+selectedValue); --%>
            
        }
    
        function cancel_userAttS2S(){        
            clear_data();            
            document.getElementById('attachmentId').style.display = 'none';
            document.getElementById('header_label').style.display = 'block';            
        }
    
        function clear_data(){
            document.userAttachmentS2Sform.description.value = "";            
            document.userAttachmentS2Sform.document.value = "";
            document.userAttachmentS2Sform.acType.value = "";
            document.userAttachmentS2Sform.fileName.value = "";        
        }
    
        function getUserAttS2SDetails(description, proposalNumber, userAttachedFormNumber, enableSync, readOnly){

            var url = "<%=request.getContextPath()%>/getUserAttS2SDetails.do?description="+description+"&proposalNumber="+proposalNumber+"&userAttachedFormNumber="+userAttachedFormNumber+"&enableSync="+enableSync+"&readOnly="+readOnly;
            var winleft = (screen.width - 650) / 2;
            var winUp = (screen.height - 450) / 2;
            var win = "scrollbars=1,resizable=1,width=550,height=300,left="+winleft+",top="+winUp;                  
            sList = window.open(url, "list", win);                 
            if (parseInt(navigator.appVersion) >= 4) {
                window.sList.focus(); 
            }   
        }    
    </script>

    <%
       String mode = (String)session.getAttribute("mode"+session.getId());       
       boolean readOnly = false;
       if(mode!= null && mode.equals("display")){
           readOnly = true;
       }

        try{
        String editImage = request.getContextPath()+"/coeusliteimages/edit.gif";
        String deleteImage = request.getContextPath()+"/coeusliteimages/delete.gif";
        }catch(Exception ex){
            ex.printStackTrace();
        }
    %>

    <html:base/> 
    <body onkeypress='dataChanged()' onload="toggle_Data()">

        <html:form action="/getUserAttS2SDetails.do" method="post"  enctype="multipart/form-data">
            <table width="100%" height="100%"  border="0" cellpadding="0" cellspacing="0" class="table">
                <tr class="table">
                    <td>            
                        <table width="100%" border="0" cellpadding="0" cellspacing="0" class='tableheader'>
                            <tr>
                                <td height="2%" align="left" valign="top" class="theader">  User Attached S2S Forms  </td>
                                <td colspan='2' class='helptext'>
                                    <div id="helpText">            
                                        <bean:message bundle="proposal" key="helpText.userAttachS2S"/>
                                    </div>
                                </td>
                            </tr>
                        </table>            
                    </td>
                </tr> 
                <tr>
                    <td align = "left">
                        <font color="red"  >
                            <html:messages id="message" bundle="proposal" message="true" property="s2sFormUploadError">                
                                <li><bean:write name = "message"/></li>
                            </html:messages>
                        </font>
                    </td>
                    
                </tr>
                
                <tr>
                    <td class='copy'>
                        <font color='red'>
                            <logic:messagesPresent>
                                <script>errValue = true;</script>
                                <html:errors header="" footer="" />
                            </logic:messagesPresent>
                            <logic:messagesPresent message="true">
                                <html:messages id="message" message="true" property="errMsg">                
                                    <script>errLock = true;</script>
                                    <li><bean:write name = "message"/></li>
                                </html:messages>
                                <html:messages id="message" message="true" property="amendMsg">                
                                    <script>errLock = true;</script>
                                    <li><bean:write name = "message" bundle="proposal"/></li>
                                </html:messages>                    
                            </logic:messagesPresent>
                        </font>
                    </td>
                </tr>
                    <%--COEUSQA-4110 START--%>
                    <logic:present name="attachedS2SError">
                        <tr>
                            <td style="color: red;">
                                <ul>
                                    <li>The file you uploaded is not Valid. Please upload the appropriate Grants.gov form file.</li>
                                </ul>
                            </td>
                        </tr>
                    </logic:present>
                    <%--COEUSQA-4110 END--%>
                <tr>
                    <td colspan="3" align="left" valign="top">
                        <table width="100%" height="2%"  border="0" cellpadding="0" cellspacing="0" class="tableheader">
                            <tr>
                                <td>
                                    <div id='header_label' id='header_label' style='display: block;'>
                                        <%if(!readOnly){%>
                                        <html:link href="javascript:called();">                      
                                            <u><bean:message bundle="proposal" key="userAttachS2S.AddDocuments"/></u>
                                        </html:link>
                                        <%}%>
                                    </div> 
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>

                <!-- Add Documents: - Start  -->
                <tr>
                    <td  align="left" valign="top" class='core'>            
                        <div id="attachmentId" style="display:none;">
                            <table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0" class="tabtable">
                                <tr>
                                    <td>
                                        <table width="100%" border="0" cellpadding="1" cellspacing="5">
                                            <tr>
                                                <td  width="12%" align="right" class="copybold" nowrap>                                            
                                                    <bean:message bundle="proposal" key="userAttachS2S.Description"/>:
                                                </td>
                                                <td width='43%' align=left>&nbsp;<html:text property="description" maxlength="100" styleClass="textbox-long" style="width:290px;" disabled="<%=readOnly%>" readonly="false" onchange="dataChanged()"/></td>
                                            </tr>
                                          
                                            <tr>
                                                <td align="right" nowrap class="copybold">                                           
                                                    <bean:message bundle="proposal" key="userAttachS2S.FileName"/>:
                                                </td>

                                                <td align = "left" style="copy"> &nbsp;<html:file property="document" accept="application/pdf" onchange="selectFile()" maxlength="250" size = "50" disabled="<%=readOnly%>"/>
                                                    <html:text property="fileName" style="width: 650px;" styleClass="cltextbox-color" disabled="true" readonly="true"/>
                                                </td>
                                            </tr>
                                        </table>

                                    </td>
                                </tr> 
                                <%if(!readOnly){%>
                                <tr class='table'>                       
                                    <td class='savebutton'>                            
                                        <html:button property="Save" value="Save" styleClass="clsavebutton" onclick="save_userAttS2S_Data()" /> 
                                        <html:button property="Cancel" value="Cancel" styleClass="clsavebutton" onclick="cancel_userAttS2S()"/> 
                                    </td>
                                </tr>                   
                                <%}%>

                            </table>
                        </div>
                    </td>
                </tr>
                <!-- Add Documents: - End  -->

                <!-- List of Uploaded Documents: for Show All - Start -->
                <tr>
                    <td>
                        <logic:present name = "lstUserAttachmentS2S">
                            <logic:notEmpty name = "lstUserAttachmentS2S">
                                <tr>
                                    <td  align="left" valign="top" class='core'>
                                        <table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0" class="tabtable">                                    
                                            <tr align="center">
                                                <td colspan="10">
                                                    <table width="100%"  border="0" cellpadding="0" cellspacing="0" id="t1" class="sortable" >
                                                        <tr>                   
                                                            <td width="25%" align="left" class="theader"><bean:message bundle="proposal" key="userAttachS2S.FileName"/></td>
                                                            <td width="25%" align="left" class="theader"><bean:message bundle="proposal" key="userAttachS2S.FormName"/></td>
                                                            <td width="5%" align="left" class="theader"><bean:message bundle="proposal" key="userAttachS2S.Pdf"/></td>
                                                            <td width="5%" align="left" class="theader"><bean:message bundle="proposal" key="userAttachS2S.Xml"/></td>

                                                            <td width="5%" align="center" class="theader">&nbsp;</td>
                                                            <td width="5%" align="left" class="theader">&nbsp;</td>
                                                            <td width="5%" align="left" class="theader">&nbsp;</td>
                                                            <td width="5%" align="center" class="theader">&nbsp;</td>
                                                        </tr>
                                                        <% 
                                                        String strBgColor = "#DCE5F1";
                                                        int count = 0;
                                                        %>

                                                        <logic:iterate id="userAttachmentS2SList" name="lstUserAttachmentS2S" type="edu.mit.coeus.s2s.bean.UserAttachedS2SFormBean">
                                                            <% 
                                                            if (count%2 == 0)
                                                                strBgColor = "#D6DCE5";
                                                            else
                                                                strBgColor="#DCE5F1";                                                
                                                
                                                       // String docComments =  userAttachmentS2SList.getComments();
                                                        String formName = "";
                                                        if(userAttachmentS2SList.getFormName() != null){
                                                            formName = userAttachmentS2SList.getFormName();
                                                        }else{
                                                            formName = "None";
                                                        }
                                                        String userAttFormLink = "javascript:edit_userAttS2S_Data('U','" +count +"')";
                                                            %>
                                                            <tr  bgcolor="<%=strBgColor%>" width="25%" valign="top"  onmouseover="className='TableItemOn'"  onmouseout="className='TableItemOff'">                            
                                                                <td width="25%" height='20' align="left" class="copy">
                                                                    <%String divName="Panel"+count;%>
                                                                    <div id='<%=divName%>'>

                                                                        <%String divlink = "javascript:showHide(1,'"+count+"')";%>
                                                                        <html:link href="<%=divlink%>">                                                                   
                                                                            <%String imagePlus=request.getContextPath()+"/coeusliteimages/plus.gif";%>
                                                                            <html:img src="<%=imagePlus%>" border="0"/>                                                                    
                                                                        </html:link> &nbsp;&nbsp;
                                                                        <%if(!readOnly){%>
                                                                        <html:link href="<%=userAttFormLink%>" >
                                                                            <%=userAttachmentS2SList.getPdfFileName()%>
                                                                        </html:link> 
                                                                        <%}else{%>
                                                                        <%=userAttachmentS2SList.getPdfFileName()%>
                                                                        <%}%>

                                                                    </div>
                                                                    <% String divsnName="hidePanel"+count;%>
                                                                    <div id='<%=divsnName%>' style='display:none;'> 

                                                                        <% String divsnlink = "javascript:showHide(2,'"+count+"')";%>
                                                                        <html:link href="<%=divsnlink%>">                                                                   
                                                                            <%String imageMinus=request.getContextPath()+"/coeusliteimages/minus.gif";%>
                                                                            <html:img src="<%=imageMinus%>" border="0"/>
                                                                        </html:link> &nbsp;&nbsp;

                                                                        <%if(!readOnly){%>
                                                                        <html:link href="<%=userAttFormLink%>" >
                                                                            <%=userAttachmentS2SList.getPdfFileName()%>
                                                                        </html:link> 
                                                                        <%}else{%>
                                                                        <%=userAttachmentS2SList.getPdfFileName()%>
                                                                        <%}%>
                                                                    </div>
                                                                </td>                            
                                                                <td width="25%" align="left"  class="copy">

                                                                    <%if(!readOnly){%>
                                                                    <html:link href="<%=userAttFormLink%>" >
                                                                        <%=formName%>
                                                                    </html:link> 
                                                                    <%}else{%>
                                                                    <%=formName%>
                                                                    <%}%>
                                                                </td>
                                                                <td width="5%" align="left"  class="copy">
                                                                    <%String imagePdf = request.getContextPath()+"/coeusliteimages/none.gif";
                                                                    if(userAttachmentS2SList.getPdfFileName() != null){
                                                                        imagePdf=request.getContextPath()+"/coeusliteimages/complete.gif";%>
                                                                    <%}%>
                                                                    <html:img src="<%=imagePdf%>" border="0"/>
                                                                </td>
                                                                <td width="5%" align="left"  class="copy">                                                            
                                                                    <%
                                                                    String imageXml=request.getContextPath()+"/coeusliteimages/none.gif";
                                                                    if(userAttachmentS2SList.getFormName() != null){
                                                                    	imageXml=request.getContextPath()+"/coeusliteimages/complete.gif";
                                                                    }
                                                                    %>
                                                                    <html:img src="<%=imageXml%>" border="0"/>
                                                                </td>
                                                                <td width="5%"  align="center" nowrap class="copy">
                                                                    <%                                    
                                                                    boolean enableSync = false;                                                            
                                                                    %>
                                                                </td>
                                                                <td width="5%"  align="center" nowrap class="copy">
                                                                    <%                                    
                                                                    String viewFormLink = "javascript:view_form_data('pdf','" +count +"')";
                                                                    %>
                                                                    &nbsp;&nbsp; <html:link href="<%=viewFormLink%>">
                                                                        <bean:message bundle="proposal" key="userAttachS2S.ViewForm"/>
                                                                    </html:link>
                                                                </td>
                                                                <td width="5%" align="center"  nowrap class="copy">
                                                                    <%
                                                                    String viewXmlLink = "javascript:view_xml_data('xml','" +count +"')";
                                                                    %>
                                                                    &nbsp;&nbsp;<html:link href="<%=viewXmlLink%>">
                                                                        <bean:message bundle="proposal" key="userAttachS2S.ViewXml"/>&nbsp;
                                                                    </html:link>
                                                                </td> 
                                                                <td width="5%" align="center" nowrap class="copy">
                                                                    <%                                    
                                                                    String removeLink = "javascript:delete_userAttS2S_Data('D','" +count +"')";
                                                                    if(!readOnly){%>
                                                                    &nbsp;&nbsp; <html:link href="<%=removeLink%>">
                                                                        <bean:message bundle="proposal" key="userAttachS2S.Remove"/>
                                                                    </html:link>
                                                                    <%}%>
                                                                </td>
                                                            </tr>

                                            
                                                    <tr>
                                                        <td colspan="9" >
                                                            <%String divisionName="pan"+count;%>
                                                            <div id='<%=divisionName%>' style='display:none;'>
                                                                <%boolean isEntered = false;%>                                                                
                                                                <table width="100%" height="100%"  border="0">
                                                                    <tr class="theader"><td><bean:message bundle="proposal" key="userAttachS2S.Details"/></td></tr>
                                                                    <tr>
                                                                        <td>
                                                                            <table width="100%" height="100%" border="0"> 
                                                                                <tr class="copy">
                                                                                    <td><b><bean:message bundle="proposal" key="userAttachS2S.Description"/> :</b> &nbsp;<%=userAttachmentS2SList.getDescription()%> </td>
                                                                                </tr> 
                                                                                 <tr class="copy">
                                                                                    <%
                                                                                        String pdfUpdateTimeStamp = "";
                                                                                        String pdfUpdateUser = "";
                                                                                        if(userAttachmentS2SList.getUpdateTimestamp() != null){
                                                                                            pdfUpdateTimeStamp = userAttachmentS2SList.getUpdateTimestamp().toString();
                                                                                        }else{
                                                                                            pdfUpdateTimeStamp = "none";
                                                                                        }
                                                                                        if(userAttachmentS2SList.getUpdateUser() != null){
                                                                                            pdfUpdateUser = userAttachmentS2SList.getUpdateUser();
                                                                                        }else{
                                                                                            pdfUpdateUser = "none";
                                                                                        }
                                                                                      StringBuilder pdfUpdateDetails = new StringBuilder(pdfUpdateTimeStamp);
                                                                                      pdfUpdateDetails.append(" by ");
                                                                                      pdfUpdateDetails.append(pdfUpdateUser);
                                                                                    %>
                                                                                    <td><b><bean:message bundle="proposal" key="userAttachS2S.pdfUpdated"/> :</b> &nbsp;<%=pdfUpdateDetails%> </td>
                                                                                </tr>
                                                                                <tr class="copy">
                                                                                    <td><b><bean:message bundle="proposal" key="userAttachS2S.namespace"/> :</b> &nbsp;<%=userAttachmentS2SList.getNamespace()%> </td>
                                                                                </tr> 
                                                                            
                                                                            </table>
                                                                        </td>
                                                                    </tr>
                                                                </table>                                                                                                                                
                                                            </div>                                                            
                                                        </td>
                                                    </tr>

                                                <% count++;%>
                                            </logic:iterate>
                                                        
                                        </table>
                                    </td>
                                </tr>

                                </table>
                            </td>
                        </tr>
                    </logic:notEmpty>
                </logic:present>
                </td>
                </tr>
                <tr>
                    <td height='5'>        
                        <html:hidden property="acType"/>
                        <html:hidden property="selectedRow"/>                
                    </td>
                </tr>

            </table>
        </html:form>

        <script>
            document.userAttachmentS2Sform.fileName.value = document.userAttachmentS2Sform.fileHidden.value;
            var dataModified = '<%=request.getAttribute("dataModified")%>';    
            if(dataModified == 'modified' || (errValue && !errLock)){
                DATA_CHANGED = 'true';
            }
            LINK = "<%=request.getContextPath()%>/saveUserAttS2SDetails.do";
            FORM_LINK = document.userAttachmentS2Sform;
            PAGE_NAME = '<bean:message bundle="proposal" key="userAttachS2S.label"/>';
            function dataChanged(){
                DATA_CHANGED = 'true';
                var selectedValue = document.userAttachmentS2Sform.selectedRow.value;
                if(selectedValue!=null){
                    LINK = "<%=request.getContextPath()%>/saveUserAttS2SDetails.do?selectedIndex="+selectedValue;
                }else{
                    LINK = "<%=request.getContextPath()%>/saveUserAttS2SDetails.do";
                }
        
                FORM_LINK = document.userAttachmentS2Sform;
                PAGE_NAME = '<bean:message bundle="proposal" key="userAttachS2S.label"/>';
            }
            linkForward(errValue);      
        </script>  
        <script>
            var help = '<bean:message bundle="proposal" key="helpText.userAttachS2S"/>';
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