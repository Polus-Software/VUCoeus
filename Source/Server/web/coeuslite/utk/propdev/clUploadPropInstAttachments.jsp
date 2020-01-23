<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<%@page import="java.util.Vector,edu.mit.coeuslite.utils.ComboBoxBean,
    edu.utk.coeuslite.propdev.bean.DocumentTypeBean,edu.mit.coeuslite.utils.DateUtils,
    edu.utk.coeuslite.propdev.bean.EPSProposalHeaderBean,edu.utk.coeuslite.propdev.action.ProposalBaseAction"%>
<jsp:useBean id="vecPersonnelPersonDetails" scope="session" class="java.util.Vector" />
<jsp:useBean id="vecDocumentType" scope="session" class="java.util.Vector" />
<jsp:useBean id="vecAttachmentsData" scope="session" class="java.util.Vector" />


<%
        String pageMode = (String)request.getParameter("page");
        String mode=(String)session.getAttribute("mode"+session.getId());        
        String currentProposalNumber=(String)session.getAttribute("proposalNumber"+session.getId());
        
        DateUtils dateUtils = new DateUtils();
        Vector vecDocument = (Vector) session.getAttribute("vecDocumentType");
        boolean modeValue=false;
        if(mode!=null && !mode.equals("")) {
            if(mode.equalsIgnoreCase("display")){
                modeValue=true;
                //session.setAttribute("proposalStatus", 8);         
            }
        }
        //Added for COEUSQA-2697 Cannot upload attach. in Lite to a Placeholder Nar. Type created in Premium-Start
        int proposalstatusCode = 0;
        if(session.getAttribute("proposalStatus") != null) {
            proposalstatusCode = Integer.parseInt(session.getAttribute("proposalStatus").toString());  
        }
        String status = (String)session.getAttribute("status");
        //Added for COEUSQA-2697 Cannot upload attach. in Lite to a Placeholder Nar. Type created in Premium-End
        boolean isVisible = (new Boolean(session.getAttribute("isVisible").toString())).booleanValue();
        Vector vecData = (Vector) session.getAttribute("vecAttachmentsData");
        System.out.println(vecData.size());
        if((vecData==null || vecData.size()==0) && modeValue){
            isVisible = true;
        }
        //Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress- Start
         String propPersonnel = "";
         if(session.getAttribute("propPersonnelVisible") != null && !session.getAttribute("propPersonnelVisible").equals("") ){
              propPersonnel = (String)session.getAttribute("propPersonnelVisible");                  
         }         
        //Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress - End
%>
<html:html>
<head>
    <title>
        JSP Page
    </title>
   
    <script>
        var errValue = false;
        var errLock = false;
        function moveTab(link)
        {       CLICKED_LINK = "<%=request.getContextPath()%>/upLoadProposal.do?page="+link;
                if(validateSavedInfo()) {
                    document.uploadProposalForm.action = "<%=request.getContextPath()%>/upLoadProposal.do?page="+link;
                    document.uploadProposalForm.submit();
                }
        }
        
        function documentProgress(data,number,updateUser,timeStamp,personId)
        {                   
                if(document.uploadProposalForm.acType.value != 'U'){
                    if(data == 'S')
                        document.uploadProposalForm.acType.value = 'I';
                    else {
                        document.uploadProposalForm.acType.value = 'D';                    
                    }
                }
                acType = document.uploadProposalForm.acType.value;
                if(data !='S'){
                    document.uploadProposalForm.updateUser.value = updateUser;
                    document.uploadProposalForm.awUpdateTimestamp.value = timeStamp;
                    document.uploadProposalForm.moduleNumber.value = number;
                    document.uploadProposalForm.bioNumber.value = number;
                }
              if(data=='S')
              {         document.uploadProposalForm.uploadStatusCode.disabled = false;
                        if('<%=pageMode%>' == 'I'){
                            document.uploadProposalForm.personId.disabled = false;
                            document.uploadProposalForm.description.disabled = false;
                            }
                        DATA_CHANGED = 'false';
                        document.uploadProposalForm.action = "<%=request.getContextPath()%>/upLoadProposalAction.do?page=<%=pageMode%>&acType="+acType;
                        document.uploadProposalForm.submit();
               }else if(data=='V'){
                        var fileType = 2;
                        DATA_CHANGED = 'false';
                        if('<%=pageMode%>'=='I'){
                            fileType = 1;
                        }
                            var link = "StreamingServlet?proposalNumber="+document.uploadProposalForm.proposalNumber.value+"&moduleNumber="+number+"&personId="+personId+"&bioNumber="+number+"&reader=edu.utk.coeuslite.propdev.bean.NarrativeDocumentReader&fileType="+fileType+"&userId="+document.uploadProposalForm.updateUser.value;
                            var winleft = (screen.width - 650) / 2;
                            var winUp = (screen.height - 450) / 2;  
                            var win = "scrollbars=1,resizable=1,width=790,height=500,left="+winleft+",top="+winUp
                            sList = window.open('<bean:write name='ctxtPath'/>'+"/"+link, "list", win);
                       // document.uploadProposalForm.action = "<%=request.getContextPath()%>/StreamingServlet?proposalNumber="+document.uploadProposalForm.proposalNumber.value+"&moduleNumber="+number+"&personId="+personId+"&bioNumber="+number+"&reader=edu.utk.coeuslite.propdev.bean.NarrativeDocumentReader&fileType="+fileType+"&userId="+document.uploadProposalForm.updateUser.value;
                       // document.uploadProposalForm.submit();
               }else if(data=='E'){
                    if('<%=pageMode%>' == 'I'){
                        document.uploadProposalForm.description.disabled = false;
                        document.uploadProposalForm.personId.value = personId;
                        document.getElementById('divPersonName1').style.display = 'none';
                        document.getElementById('divPersonName2').style.display = 'block';                                           
                    }
                    document.uploadProposalForm.action = "<%=request.getContextPath()%>/upLoadProposal.do?page="+'<%=pageMode%>'+'&acType='+data+'&number='+number+'&personId='+personId;
                    document.uploadProposalForm.submit();                    
               } else {
                    if(confirm("<bean:message bundle="proposal" key="uploadAttachments.deleteConfirmation"/>")) {
                        document.uploadProposalForm.action = "<%=request.getContextPath()%>/upLoadProposalRemoveView.do?page="+'<%=pageMode%>'+'&acType='+data+'&number='+number+'&personId='+personId;
                        document.uploadProposalForm.submit();
                    }
               }
        }
        
        function addDoc(value){
            if(value == 'A'){
                document.uploadProposalForm.uploadStatusCode.disabled = false;
                CLICKED_LINK = "<%=request.getContextPath()%>/upLoadProposal.do?page=<%=pageMode%>";
                if(validateSavedInfo()) {        
                    document.uploadProposalForm.action = "<%=request.getContextPath()%>/upLoadProposal.do?page=<%=pageMode%>";
                    document.uploadProposalForm.submit();
                }
            }else if(value == 'M'){
                document.getElementById('addDoc_fileTextButton').style.display = 'none';
                document.getElementById('addDoc_fileText').style.display = 'block';
                document.getElementById('addDoc_docText').style.display = 'block';
            }
        }
        
        function enableButton(){
            ENABLE_COMPONENT = 'Y';
            document.uploadProposalForm.Save.disabled = false;
            dataChanged();
        }
        
        function enableComponent(){
            if('<%=pageMode%>' == 'I'){
                document.uploadProposalForm.uploadStatusCode.disabled = false;
                document.uploadProposalForm.personId.disabled = false;
                document.uploadProposalForm.description.disabled = false;
            } else {
                document.uploadProposalForm.uploadStatusCode.disabled = false;
            }
        }
    </script>
</head>
<body>
<html:form action="/upLoadProposalAction.do"  enctype="multipart/form-data" >
<!-- JM 4-9-2013 update to set tab width (was 73%) -->
<table width="65%" height="100%"  border="0" cellpadding="0" cellspacing="0">
<tr>
<%if(pageMode==null || pageMode.equals("P")) {
    pageMode="P";%>
<td width="16%" height="27" align="center" valign="middle" STYLE="background-image:url('<bean:write name='ctxtPath'/>/coeusliteimages/tab1.gif');border:0;" 
    class="tabbg">
    <b><bean:message bundle="proposal" key="upload.proposalAttachments"/></b>
    <script>PAGE_NAME = "<bean:message bundle="proposal" key="upload.proposalAttachments"/>";</script>
</td>

<%} else {%>
<td width="16%" height="27" align="center" valign="middle" STYLE="background-image:url('<bean:write name='ctxtPath'/>/coeusliteimages/tab2.gif');border:0;cursor: pointer;" 
    class="tabbg" onclick="moveTab('P');">
    <bean:message bundle="proposal" key="upload.proposalAttachments"/>
</td>
<%}
if(pageMode.equals("I")){%>
<td width="16%" height="27" align="center" valign="middle" STYLE="background-image:url('<bean:write name='ctxtPath'/>/coeusliteimages/tab1.gif');border:0;" 
    class="tabbg">
    <b><bean:message bundle="proposal" key="upload.personnelAttachments"/></b>
    <script>PAGE_NAME = "<bean:message bundle="proposal" key="upload.personnelAttachments"/>";</script>
</td>
<%} else {%>
<td width="16%" height="27"  align="center" valign="middle" STYLE="background-image:url('<bean:write name='ctxtPath'/>/coeusliteimages/tab2.gif');border:0;cursor: pointer;" 
    class="tabbghl" onclick="moveTab('I');">
    <bean:message bundle="proposal" key="upload.personnelAttachments"/>
</td>
<%}
if(pageMode.equals("O")){%>
<td width="16%" height="27" align="center" valign="middle" STYLE="background-image:url('<bean:write name='ctxtPath'/>/coeusliteimages/tab1.gif');border:0;" 
    class="tabbg">
    <b><bean:message bundle="proposal" key="upload.institutionalAttachments"/></b>
    <script>PAGE_NAME = "<bean:message bundle="proposal" key="upload.institutionalAttachments"/>";</script>
</td>
<%} else {%>
<td width="16%" height="27"  align="center" valign="middle" STYLE="background-image:url('<bean:write name='ctxtPath'/>/coeusliteimages/tab2.gif');border:0;cursor: pointer;" 
    class="tabbg" onclick="moveTab('O');">
    <bean:message bundle="proposal" key="upload.institutionalAttachments"/>
</td>
<%}%>
</tr>
</table>
<table width="100%" align="center" height="100%"  border="0" cellpadding="0" cellspacing="0" class="table">
<tr>
    <td width='100%' class='core'>
    <table width="100%" height="100%" align='center' border="0" cellpadding="0" cellspacing="0" class="tabtable">
    <tr>
        <td height='20'>
        <table width="100%" align='center' border="0" cellpadding="0" cellspacing="0">
           
        <tr>
            <td>
                <table width='100%' border="0" cellpadding="0" cellspacing="0" class='tableheader'>
                <tr>
                    <td>
                        <bean:message bundle="proposal" key="upload.addDocuments"/> 
                    </td>
                </tr>
                </table>
            </td>
        </tr>  
      
        <tr>
            <td>                
                <div id="helpText" class='helptext'>            
                    <bean:message bundle="proposal" key="helpTextProposal.UploadAttachment"/>  
                </div>                
            </td>
        </tr>
        
        </table>
        
        </td>
    </tr>
    <tr>
        <td><%
                //Modified for COEUSQA-2697 Cannot upload attach. in Lite to a Placeholder Nar. Type created in Premium-Start
                if(ProposalBaseAction.PROPOSAL_APPROVAL_IN_PROGRESS != proposalstatusCode
                 &&  ProposalBaseAction.PROPOSAL_APPROVED != proposalstatusCode){
                if(mode!=null && ((!mode.equals(""))|| (!mode.equals("display"))))
                //Modified for COEUSQA-2697 Cannot upload attach. in Lite to a Placeholder Nar. Type created in Premium-End
                {%>
            <div id='addDoc_label' style='display: none;'>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <%String addDoc = "javascript:addDoc('A')";%>
                <html:link href="<%=addDoc%>">    
                    <u>Add Documents</u>
                </html:link>
            </div>
            <%}}%>
        </td>
    </tr>
    <tr>
        <td width='100%'>
        <table width="99%" height="100%" align=center border="0" cellpadding="0" cellspacing="3">
            <tr style='font-weight:normal;'>
            <td colspan="2">
                <font color="red">
               <logic:messagesPresent>
                    <script>errValue = true;</script>
                    <html:errors header="" footer="" bundle="proposal"/>
               </logic:messagesPresent>

               <logic:messagesPresent message="true">
               <script>errValue = true;</script>
                <html:messages id="message" message="true" property="duplicateRecordsNotAllowed" bundle="proposal">
                    <li><bean:write name = "message"/></li>
                 </html:messages>
                 <html:messages id="message" message="true" property="descriptionRequired" bundle="proposal">
                    <li><bean:write name = "message"/></li>
                 </html:messages>
                  <html:messages id="message" message="true" property="errMsg" bundle="proposal">
                    <script>errLock = true;</script>
                    <li><bean:write name = "message"/></li>
                  </html:messages>
               </logic:messagesPresent>
               </font>
            </td>
        </tr>
        <tr>
            <td width='10%' height='15'>
            </td>
        </tr>
        <tr>

            <td width='10%' class="copybold" nowrap><bean:message bundle="proposal" key="upload.documentType"/>:</td>
            <td width='25%'>
            <%if(!pageMode.equals("I")) {%>
                       <html:select  name="uploadProposalForm" property="uploadStatusCode" styleClass="clcombobox-bigger" disabled="<%=modeValue || isVisible%>" onchange="dataChanged()">
                            <html:option value=""> <bean:message bundle="proposal" key="generalInfoProposal.pleaseSelectNSF"/> </html:option>
                             <html:options collection="vecDocumentType" property="narrativeTypeCode" labelProperty="description"/>
                        </html:select>
             <%} else {%>
                       <html:select  name="uploadProposalForm" property="uploadStatusCode" styleClass="clcombobox-bigger" disabled="<%=modeValue%>" onchange="dataChanged()">
                             <html:option value=""> <bean:message bundle="proposal" key="generalInfoProposal.pleaseSelectNSF"/> </html:option>
                             <html:options collection="vecDocumentType" property="code" labelProperty="description"/>
                        </html:select>
             <%}%>
            </td>
            <td width='25%'>
            </td>
        </tr>
        <tr>

            <td width='10%' class="copybold" nowrap>
            <%if(!pageMode.equals("I")) {%>
                <bean:message bundle="proposal" key="upload.description"/>:
            <%} else {%>
                <bean:message bundle="proposal" key="upload.person"/>:
            <%}%>
            </td>
            <td width='25%'>
            <%if(!pageMode.equals("I")) {%>
                <%--div class='copyitalics'><bean:message bundle="proposal" key="uploadAttachments.descriptionlimit"/></div--%>
                <%--Modified for COEUSQA-2697 Cannot upload attach. in Lite to a Placeholder Nar. Type created in Premium-Start --%>
                <%if((ProposalBaseAction.PROPOSAL_APPROVAL_IN_PROGRESS == proposalstatusCode 
                      || ProposalBaseAction.PROPOSAL_APPROVED == proposalstatusCode) && status.equals("ENABLED")){%>
                <%--Modified for COEUSQA-2697 Cannot upload attach. in Lite to a Placeholder Nar. Type created in Premium-End --%>
                <html:textarea property="description" styleClass="copy" cols='55' disabled="<%=modeValue%>" onchange="dataChanged()"/>
                <script>
                        if(navigator.appName == "Microsoft Internet Explorer")
                        {
                            document.uploadProposalForm.description.cols=61;
                            document.uploadProposalForm.description.rows=3;
                        }
                </script>
                <%}else{%>
                <%--Modified for COEUSQA-2697 Cannot upload attach. in Lite to a Placeholder Nar. Type created in Premium-Start --%>
                <html:textarea property="description" styleClass="copy" cols='55' disabled="<%=modeValue || isVisible%>" onchange="dataChanged()"/>
                <script>
                        if(navigator.appName == "Microsoft Internet Explorer")
                        {
                            document.uploadProposalForm.description.cols=61;
                            document.uploadProposalForm.description.rows=3;
                        }
                </script>
                <%}%>
                <%--Modified for COEUSQA-2697 Cannot upload attach. in Lite to a Placeholder Nar. Type created in Premium-End --%>
             <%} else {%>         
                <div id="divPersonName1" style='display: block;'>
                    <html:select name="uploadProposalForm" property="personId" styleClass="clcombobox-bigger" disabled="<%=modeValue%>" onchange="dataChanged()">
                        <html:option value=""><bean:message bundle="proposal" key="generalInfoProposal.pleaseSelectNSF"/></html:option>
                        <html:options collection="vecPersonnelPersonDetails" property="code" labelProperty="description"/>
                    </html:select>  
                </div>
                <div id="divPersonName2" style='display: none;'>
                    <html:text property="personName" style="width: 325px;" styleClass="cltextbox-nonEditcolor" readonly="true"/>
                </div>
             <%}%>
            </td>
        </tr>
        <!-- Added for case#3450 - start -->
        <%if(pageMode.equals("I")) {%>
        <tr>
            <td width='10%' class="copybold" nowrap>            
                <bean:message bundle="proposal" key="upload.description"/>:            
            </td>
            <td>            
                <html:text property="description" styleClass="cltextbox" style="width: 325px;" disabled="<%=modeValue%>" onchange="dataChanged()"/>
            </td>                        
        </tr>
        <%}%>
        <!-- Added for case#3450 - end -->                
        <tr>
            <td width='10%' class="copybold" nowrap><bean:message bundle="proposal" key="upload.fileName"/>:</td>
            <td nowrap>
                <%if(!pageMode.equals("I")) {
                       //Modified for COEUSQA-2697 Cannot upload attach. in Lite to a Placeholder Nar. Type created in Premium-Start --
                    if((ProposalBaseAction.PROPOSAL_APPROVAL_IN_PROGRESS == proposalstatusCode 
                        || ProposalBaseAction.PROPOSAL_APPROVED == proposalstatusCode) && status.equals("ENABLED")){%>
                    <%--Modified for COEUSQA-2697 Cannot upload attach. in Lite to a Placeholder Nar. Type created in Premium-End --%>
                <div id='addDoc_docText'>
                    <html:file property="document" accept="" maxlength="150" size="50" disabled="<%=modeValue%>" onchange="dataChanged()"/>
                </div>
                <div id='addDoc_fileTextButton' style='display: none;'>
                    <html:text property="fileName" styleClass="cltextbox-color" style="width: 325px;" readonly="true" />
                <html:button property="ok" value="Upload New File"  styleClass="clsavebutton" onclick="addDoc('M');" /> 
                </div>
                <div id='addDoc_fileText' style='display: none;'>
                    <html:text property="fileName" styleClass="cltextbox-color" style="width: 325px;" readonly="true" />
                </div>
                <%--Modified for COEUSQA-2697 Cannot upload attach. in Lite to a Placeholder Nar. Type created in Premium-Start --%>
                 <%}else{%>
                 <div id='addDoc_docText'>
                    <html:file property="document" accept="" maxlength="150" size="50" disabled="<%=modeValue || isVisible%>" onchange="dataChanged()"/>
                </div>
                <div id='addDoc_fileTextButton' style='display: none;'>
                    <html:text property="fileName" styleClass="cltextbox-color" style="width: 325px;" readonly="true" />
                <html:button property="ok" value="Upload New File"  styleClass="clsavebutton" onclick="addDoc('M');" /> 
                </div>
                <div id='addDoc_fileText' style='display: none;'>
                    <html:text property="fileName" styleClass="cltextbox-color" style="width: 325px;" readonly="true" />
                </div>
                <%--Modified for COEUSQA-2697 Cannot upload attach. in Lite to a Placeholder Nar. Type created in Premium-End --%>
                 <%}} else {%>
                    <%--html:file property="document" accept="" maxlength="150" size="50" disabled="<%=modeValue%>" onchange="dataChanged()"/ Modified for Case#3577--%>
                        <%if(!propPersonnel.equals("") && propPersonnel.equals("N")){%>
                            <html:file property="document" accept="" maxlength="150" size="50" disabled="false" onchange="enableButton()"/>
                            <%--COEUSDEV-308: Application not checking module level rights for a user in Narrative - Start --%>    
                            <script>
                            if(document.uploadProposalForm.description.value != null && 
                                    document.uploadProposalForm.description.value != 'undefined'
                                    && document.uploadProposalForm.description.value != '') {
                                document.uploadProposalForm.document.disabled = false;
                            } else {
                                document.uploadProposalForm.document.disabled = true;
                            }
                           </script>
                           <%--COEUSDEV-308: Application not checking module level rights for a user in Narrative - End --%>    
                        <%}else{%>
                            <html:file property="document" accept="" maxlength="150" size="50" disabled="<%=modeValue%>" onchange="dataChanged()"/>
                        <%}%>
                 <%}%>
            </td>
        </tr>
        <%if(pageMode.equals("I")){%>
        <tr>
            <td>
                &nbsp;
            </td>
            <td>
                <html:text property="fileName" style="width: 400px;" styleClass="cltextbox-color" disabled="false" readonly="true"/>
            </td>
        </tr>  
        <%}%>
        <%--If condition modified for the case id COEUSQA-2778  Add Complete Checkbox to Institutional Attachments--%>
        <%-- if(pageMode.equals("P"){--%>
            <%if("P".equals(pageMode) || "O".equals(pageMode)){
             //Modified for COEUSQA-2697 Cannot upload attach. in Lite to a Placeholder Nar. Type created in Premium-Start

            if((ProposalBaseAction.PROPOSAL_APPROVAL_IN_PROGRESS == proposalstatusCode 
                || ProposalBaseAction.PROPOSAL_APPROVED == proposalstatusCode) && status.equals("ENABLED")){%>
             <%--Modified for COEUSQA-2697 Cannot upload attach. in Lite to a Placeholder Nar. Type created in Premium-End --%>
        <tr>
            <td width='10%' class="copybold" nowrap><bean:message bundle="proposal" key="upload.complete"/>:</td>
            <td>
                <html:checkbox styleId="moduleStatuId" property="moduleStatusCode" onchange="dataChanged()" disabled="<%=modeValue%>"  />
            </td>
        </tr> 
        <%--Modified for COEUSQA-2697 Cannot upload attach. in Lite to a Placeholder Nar. Type created in Premium-Start --%>
        <%}else{%>
                <tr>
            <td width='10%' class="copybold" nowrap><bean:message bundle="proposal" key="upload.complete"/>:</td>
            <td>
                <html:checkbox styleId="moduleStatuId" property="moduleStatusCode" onchange="dataChanged()" disabled="<%=modeValue || isVisible%>" />
            </td>
        </tr> 
        <%}}%>
        <%--Modified for COEUSQA-2697 Cannot upload attach. in Lite to a Placeholder Nar. Type created in Premium-End --%>
        <tr>
            <td width='10%' height='5'>
            </td>
        </tr>
        
        </table>
        </td>
    </tr>
    <tr class='table'>
            <!--td width='10%' nowrap></td-->
            <td class='savebutton' colspan='2'>
                <%--Modified for COEUSQA-2697 Cannot upload attach. in Lite to a Placeholder Nar. Type created in Premium-Start --%>
                <%if(!pageMode.equals("I")) {
                    if((ProposalBaseAction.PROPOSAL_APPROVAL_IN_PROGRESS == proposalstatusCode 
                       || ProposalBaseAction.PROPOSAL_APPROVED == proposalstatusCode) && status.equals("ENABLED")){%>
                <%--Modified for COEUSQA-2697 Cannot upload attach. in Lite to a Placeholder Nar. Type created in Premium-End --%>
                    <html:submit property="Save" value="Save"  styleClass="clsavebutton" disabled="<%=modeValue%>" onclick="documentProgress('S');" />
                    <%if(request.getParameter("acType") != null &&
                            request.getParameter("acType").equals("E") && !isVisible){%>
                    <script>
                        document.uploadProposalForm.document.disabled = false;
                        document.uploadProposalForm.description.disabled = false;
                        document.uploadProposalForm.Save.disabled = false;
                        document.uploadProposalForm.moduleStatusCode.disabled= false;
                    </script>
                    <%}%>
                <%--Modified for COEUSQA-2697 Cannot upload attach. in Lite to a Placeholder Nar. Type created in Premium-Start --%>
                <%}else{%>
                    <html:submit property="Save" value="Save"  styleClass="clsavebutton" disabled="<%=modeValue || isVisible%>" onclick="documentProgress('S');" />
                    <%if(request.getParameter("acType") != null &&
                            request.getParameter("acType").equals("E") && !isVisible){%>
                    <script>
                        document.uploadProposalForm.document.disabled = false;
                        document.uploadProposalForm.description.disabled = false;
                        document.uploadProposalForm.Save.disabled = false;
                        document.uploadProposalForm.moduleStatusCode.disabled= false;
                    </script>
                <%}}}else {%>
                <%--Modified for COEUSQA-2697 Cannot upload attach. in Lite to a Placeholder Nar. Type created in Premium-End --%>
                    <%--html:submit property="Save" value="Save"  styleClass="clsavebutton" disabled="<%=modeValue%>" onclick="documentProgress('S');" / Modified for Case#3577--%> 
                    <%if(!propPersonnel.equals("") && propPersonnel.equals("N")){%>
                        <html:submit property="Save" value="Save"  styleClass="clsavebutton" disabled="true" onclick="documentProgress('S');" /> 
                    <%}else{%>
                        <html:submit property="Save" value="Save"  styleClass="clsavebutton" disabled="<%=modeValue%>" onclick="documentProgress('S');" /> 
                    <%}%>
                <%}%>
            </td>
        </tr>
    </table>
    </td>
</tr>

<tr>
    <td width='100%' class='core'>
    <table width="100%" height="100%" align=center border="0" cellpadding="0" cellspacing="0" class="tabtable">
     <tr>
        <td>
            <table width="100%" align=center border="0" cellpadding="0" cellspacing="0" class="tableheader">
            <tr>
                <td>
                    <%if(pageMode.equals("P")) {%>
                    <bean:message bundle="proposal" key="upload.listofProposalAttachments"/>
                    <%} else if(pageMode.equals("I")) {%>
                    <bean:message bundle="proposal" key="upload.listofPersonnelAttachments"/>
                    <%} else if(pageMode.equals("O")) {%>
                    <bean:message bundle="proposal" key="upload.listofInstitutionalAttachments"/>
                    <%}%>
                </td>
            </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td height='15'>
        </td>
    </tr>
      <tr>
        <td>
            <table width="99%" align=center border="0" cellpadding="0" cellspacing="1" class="tabtable">

          <% int indexColor=1;
             if(pageMode.equals("I")){%>
            <tr>
                <td width='9%' class="theader">
                    <bean:message bundle="proposal" key="upload.person"/>
                </td>
            <logic:present name = "vecDocumentType">            
                <logic:iterate id="documentType" name="vecDocumentType" type="edu.mit.coeuslite.utils.ComboBoxBean">                
                <td width='18%' align ="center" class="theader" nowrap>
                    <bean:write name="documentType" property="description"/>
                </td>
                 </logic:iterate>                 
           </logic:present>
            </tr>
            <logic:present name = "vecAttachmentsData">            
            <logic:iterate id="vecAttachData" name="vecAttachmentsData" type="edu.utk.coeuslite.propdev.form.ProposalUploadForm">
            <%  String strBgColor="#D6DCE5";
                Vector vecDocumentDetails = vecAttachData.getVecDocumentDetails();               
                if (indexColor%2 == 0) { 
                    strBgColor = "#DCE5F1"; }%>  
            <tr bgcolor="<%=strBgColor%>" onmouseover="className='TableItemOn'"  onmouseout="className='TableItemOff'">

                <td width='9%' class='copy' >
                    <bean:write name="vecAttachData" property="personName"/>
                </td>
                <%try{ 
                  if(vecDocument!=null && vecDocument.size()>0) {
                 for(int index=0; index < vecDocument.size() ; index++) {
                    ComboBoxBean comboBoxBean =(ComboBoxBean) vecDocument.get(index);
                    boolean isExist = true;%>
                    <td width='18%' align ="center"  class='copy'>
                 <%
                    
                    if(vecDocumentDetails!=null && vecDocumentDetails.size()>0) {                     
                    for(int count=0 ; count < vecDocumentDetails.size() ; count++) {
                        DocumentTypeBean documentTypeBean = (DocumentTypeBean) vecDocumentDetails.get(count);
                        if(documentTypeBean!=null && documentTypeBean.getDocumentType().equals(comboBoxBean.getCode())) {
                            isExist = false;
                            String pageSet = "javaScript:documentProgress('E','"+documentTypeBean.getBioNumber()+"','','"+documentTypeBean.getTimeStamp()+"','"+documentTypeBean.getPersonId()+"');";
                               // System.out.println(" BIO NUMBER ********..........."+documentTypeBean.getBioNumber());
                                String date = documentTypeBean.getTimeStamp();
                                String time = date.substring(11,16);
                                date = dateUtils.formatDate(date,"MM/dd/yyyy");
                                String fileName = documentTypeBean.getFileName();
                                //Added/Modified for case#3450 - Displaying description instead of file name of the biosketch - start
                                String description = documentTypeBean.getDescription();
                                if(description == null || description.trim().equals("")){
                                    description = "";
                                }   
                                //String userName = vecAttachData.getUserName();
                                //if(userName == null){
                                //    userName =  vecAttachData.getUpdateUser();                                
                                //}                                                             
                                String userName = documentTypeBean.getUpdateUser();                                                                                                                                  
                                if(fileName == null || fileName.equals("null.pdf")){
                                    fileName = "";                                    
                                }                                
                                if(!description.equals("")){
                                    fileName = description;
                                }
                                //Added for case#3450 - Displaying description instead of file name of the biosketch - end
                             //if(!isVisible){
                            if((!propPersonnel.equals("") && propPersonnel.equals("N")) || !modeValue){%>
                            <html:link href = "<%=pageSet%>" styleClass="linkShowHide">
                                    <%=fileName%><br><i>(Uploaded:<%=date%> <%=time%> by <%=userName%>)</i><br>
                            </html:link>
                            <%} else {%>
                                    <%=fileName%><br><i>(Uploaded:<%=date%> <%=time%> by <%=userName%>)</i><br>
                            <%}%>
                            <%if(!modeValue) {
                                pageSet = "javaScript:documentProgress('D','"+documentTypeBean.getBioNumber()+"','','"+documentTypeBean.getTimeStamp()+"','"+documentTypeBean.getPersonId()+"');";%>
                                <html:link href = "<%=pageSet%>" styleClass="linkShowHide">
                                    <bean:message bundle="proposal" key="upload.remove"/>
                                </html:link> &nbsp;&nbsp;&nbsp;
                            <%} else {
                                isExist = true;
                            }%>                                 
                            <%pageSet = "javaScript:documentProgress('V','"+documentTypeBean.getBioNumber()+"','"+vecAttachData.getUpdateUser()+"','"+vecAttachData.getAwUpdateTimestamp()+"','"+vecAttachData.getPersonId()+"');";%>
                            <!-- COEUSDEV:783 - Cannot upload attachment in Lite to a Placeholder Personnel Type created in Premium - Start -->
                            <% if(documentTypeBean.isDocumentPresent() == true ) { %>
                            <html:link href = "<%=pageSet%>" styleClass="linkShowHide" >
                                <bean:message bundle="proposal" key="upload.view"/>
                            </html:link> &nbsp;&nbsp;&nbsp;
                            <%}%>
                            <!-- COEUSDEV:783 - End -->
             
                      <%}
                    }
                   }%>
                   </td> 
               <% }
                }
                }catch(Exception e){
                        e.printStackTrace();
             }%>

            </tr>
            <%indexColor++;%>
                 </logic:iterate>                 
           </logic:present> 
            <%} else { %>
            <tr>
                <td width='50%' class="theader">
                    <bean:message bundle="proposal" key="upload.type"/>
                </td>
                <td width='8%' class="theader">
                    <bean:message bundle="proposal" key="upload.complete"/>
                </td>
                <td width='22%' class="theader">
                    <bean:message bundle="proposal" key="upload.description"/>
                </td>
                <td width='10%' class="theader">

                </td>
                <td width='10%' class="theader">
                </td>                
            </tr>
         <logic:present name = "vecAttachmentsData">
            <logic:iterate id="vecAttachData" name="vecAttachmentsData" type="edu.utk.coeuslite.propdev.form.ProposalUploadForm">
            <%  String strBgColor = "#D6DCE5";
                if (indexColor%2 == 0) { 
                    strBgColor = "#DCE5F1"; }                                        
                    java.util.Map params = new java.util.HashMap();
                    params.put("moduleNumber", new Integer(vecAttachData.getModuleNumber())   );
                    params.put("acType", "D");                   
                    pageContext.setAttribute("paramsMap", params); 
                    
                    boolean canView = false;
                    boolean canModify = false;
                    char accessType = vecAttachData.getAccessType();
                    if(accessType == 'M'){
                        canView = true;
                        canModify = true;
                    } else if(accessType == 'R'){
                        canView = true;
                    }
                    
                    
                    
           %>
            <tr bgcolor="<%=strBgColor%>" onmouseover="className='TableItemOn'"  onmouseout="className='TableItemOff'">
                <td width='50%' class='copy' >
                <%String pageSet = "javaScript:documentProgress('E','"+vecAttachData.getModuleNumber()+"','"+vecAttachData.getUpdateUser()+"','"+vecAttachData.getAwUpdateTimestamp()+"');";
                // if(!isVisible){
                if(canModify){  %>
                <html:link href="<%=pageSet%>">
                    <bean:write name="vecAttachData" property="documentType"/><br>
                    <bean:write name="vecAttachData" property="fileName"/>
                            <%
                            String date = vecAttachData.getAwUpdateTimestamp();
                            String time = date.substring(11,16);
                            date = dateUtils.formatDate(date,"MM/dd/yyyy");
                            String userName = vecAttachData.getUserName();
                            if(userName == null){
                                userName =  vecAttachData.getUpdateUser();                                
                            }
                            //System.out.println(" IF Block User Name >>> "+userName); 
                            %>
                    <i>(Uploaded:<%=date%> <%=time%> by <%=userName%>)</i>
                </html:link>
                <%} else {%>
                    <bean:write name="vecAttachData" property="documentType"/><br>
                    <bean:write name="vecAttachData" property="fileName"/>
                            <%
                            String date = vecAttachData.getAwUpdateTimestamp();
                            String time = date.substring(11,16);
                            date = dateUtils.formatDate(date,"MM/dd/yyyy");
                            String userName = vecAttachData.getUserName();
                            if(userName == null){
                                userName =  vecAttachData.getUpdateUser();                                
                            }
                            //System.out.println("Else Block User Name >>> "+userName);
                            %>                               
                    <i>(Uploaded:<%=date%> <%=time%> by <%=userName%>)</i>                
                <%}%>
                </td>
                <%  String completeImage= request.getContextPath()+"/coeusliteimages/complete.gif";
                String noneImage= request.getContextPath()+"/coeusliteimages/none.gif";        %>
                <td align='center' class='copy' width='8%' nowrap>
                    <logic:equal name="vecAttachData" property="moduleStatusCode" value="I">
                        <html:img src="<%=noneImage%>"/>       
                    </logic:equal>
                    <logic:equal name="vecAttachData" property="moduleStatusCode" value="C">
                        <html:img src="<%=completeImage%>"/>  
                    </logic:equal>                      
                </td>
                <td width='22%' class='copy' >
                   <%if(vecAttachData.getDescription()!=null && vecAttachData.getDescription().length()>30) {
                        String description = vecAttachData.getDescription().substring(0,30);
                        description +="...";%>                
                        <%=description%>
                   <%} else {%>
                    <bean:write name="vecAttachData" property="description"/>
                   <% } %> 
                </td>
                <td width='10%' align=center>
                    <%pageSet = "javaScript:documentProgress('D','"+vecAttachData.getModuleNumber()+"','"+vecAttachData.getUpdateUser()+"','"+vecAttachData.getAwUpdateTimestamp()+"');";
                    // if(!modeValue &&  ! isVisible) {
                    if( !modeValue && canModify){ %>
                    <html:link  href="<%=pageSet%>" styleClass="linkShowHide">
                     <bean:message bundle="proposal" key="upload.remove"/>
                    </html:link>    
                    <% } else {%>
                     <bean:message bundle="proposal" key="upload.remove"/>
                    <%}%>
                </td>
                <td width='10%'align=center>
                    <%if(vecAttachData.getFileName() != null && !vecAttachData.getFileName().trim().equals("")
                        && canView){
                    pageSet = "javaScript:documentProgress('V','"+vecAttachData.getModuleNumber()+"','"+vecAttachData.getUpdateUser()+"','"+vecAttachData.getAwUpdateTimestamp()+"');";%>
                    <html:link href = "<%=pageSet%>" styleClass="linkShowHide">
                        <bean:message bundle="proposal" key="upload.view"/>
                    </html:link>
                    <%} else {%>
                        <bean:message bundle="proposal" key="upload.view"/>
                    <%}%>
                </td>
            </tr>            
            <%indexColor++;%>
          

            </logic:iterate>
           </logic:present>
           <%}%>
            </table>
        </td>
    </tr>
    <tr>
        <td height='15'>
        </td>
    </tr>    
    </table>
    </td>
</tr>
</table>

<html:hidden property="proposalNumber"/>
<html:hidden property="moduleNumber"/>
<html:hidden property="bioNumber"/>
<html:hidden property="acType"/>
<html:hidden property="fileBytes"/>
<html:hidden property="narrativeTypeGroup"/>
<html:hidden property="updateUser" />
<html:hidden property="awUpdateTimestamp" />
<html:hidden property="currentProposalNumber" value="<%=currentProposalNumber%>" />

</html:form>
<script>
      DATA_CHANGED = 'false';
      if(errValue && !errLock) {
            DATA_CHANGED = 'true';
      }
      LINK = "<%=request.getContextPath()%>/upLoadProposalAction.do?page=<%=pageMode%>";
      FORM_LINK = document.uploadProposalForm;
      function dataChanged(){
        DATA_CHANGED = 'true';
      }
      linkForward(errValue);
      if(document.uploadProposalForm.acType.value == 'U'){
          if(!<%=modeValue%>)
            document.getElementById('addDoc_label').style.display = 'block';
          // 3826: Coeus-Lite Upload Attachment Fails - Start
          // document.getElementById('addDoc_fileTextButton').style.display = 'block';
          // document.getElementById('addDoc_docText').style.display = 'none'; 
          if('<%=pageMode%>' == 'O' || '<%=pageMode%>' == 'P' ){
            // If the Upload Institutional Attachments  or  Upload Proposal Attachments Tab is Selected
            document.getElementById('addDoc_fileTextButton').style.display = 'block';
            document.getElementById('addDoc_docText').style.display = 'none'; 
          }
          // 3826: Coeus-Lite Upload Attachment Fails - End
          document.uploadProposalForm.uploadStatusCode.disabled = true;        
          if('<%=pageMode%>' == 'I'){
            document.uploadProposalForm.personId.disabled=true;           
          } else {
            ENABLE_COMPONENT = 'Y';
          }
      } else {
            document.uploadProposalForm.acType.value = 'I';
      }
</script>
<script>
      var help = '<bean:message bundle="proposal" key="helpTextProposal.UploadAttachment"/>';
      help = trim(help);
      if(help.length == 0) {
        document.getElementById('helpText').style.display = 'none';
      }
      function trim(s) {
            return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
      }  
</script>
    <script>
        <%
            String editFlag = request.getParameter("acType");
            if(editFlag != null && !editFlag.equals("") && editFlag.equals("E")){
        %>
            // Case# 3826: Coeus-Lite Upload Attachment Fails - Start
            // document.getElementById('divPersonName1').style.display = 'none';
            // document.getElementById('divPersonName2').style.display = 'block'; 
            if('<%=pageMode%>' == 'I'){
                // If the Upload Peronnel Attachments Tab is Selected
                document.getElementById('divPersonName1').style.display = 'none'; 
                document.getElementById('divPersonName2').style.display = 'block'; 
            } 
            // Case# 3826: Coeus-Lite Upload Attachment Fails - End
        <%}%>
    </script>
    <% session.setAttribute("status","ENABLED");%>
</body>
</html:html>