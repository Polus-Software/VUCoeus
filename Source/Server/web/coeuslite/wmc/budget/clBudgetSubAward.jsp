<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<%@ page import="java.util.Vector,edu.wmc.coeuslite.budget.bean.CategoryBean,edu.mit.coeus.budget.BudgetSubAwardConstants,
                java.util.List,edu.wmc.coeuslite.utils.CoeusLineItemDynaList, org.apache.struts.validator.DynaValidatorForm"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld"  prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld"  prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"  prefix="coeusUtils"%>

<jsp:useBean id="summary" scope="session" class="java.util.ArrayList" />
<jsp:useBean id="lstSubAwardBudget" scope="session" class="java.util.ArrayList" />
<!-- JM 7-11-2013 added for organization drop-down -->
<jsp:useBean id="organizations" scope="session" class="java.util.Vector" />
<!-- JM END -->

<html:html locale="true">
    <head>
        <link href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css" rel="stylesheet" type="text/css">
    </head>
<title>Sub Award Budget</title>
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
        if(document.budgetSubAward.acType.value == 'U'){
            called();
        }
    }
    
    function selectFile(){
        dataChanged();
        document.budgetSubAward.fileName.value = document.budgetSubAward.document.value;
        document.budgetSubAward.fileHidden.value = document.budgetSubAward.document.value;
    }

    function edit_SubAward_Data(data, selectedValue){
        document.budgetSubAward.acType.value = data;        
        document.budgetSubAward.action = "<%=request.getContextPath()%>/editSubAwardBudget.do?selectedIndex="+selectedValue;
        document.budgetSubAward.submit();
    }
    
    function save_SubAward_Data(){
        var isValid = validate_upload_form();
        if(isValid){
            if(document.budgetSubAward.acType.value == null || document.budgetSubAward.acType.value.length == 0){
                document.budgetSubAward.acType.value = 'I';
                document.budgetSubAward.action = "<%=request.getContextPath()%>/saveSubAwardBudget.do";
                document.budgetSubAward.submit();
            }else{            
                var selectedValue = document.budgetSubAward.selectedRow.value;
                document.budgetSubAward.acType.value = 'U';
                document.budgetSubAward.action = "<%=request.getContextPath()%>/saveSubAwardBudget.do?selectedIndex="+selectedValue;
                document.budgetSubAward.submit();
            }
        }
    }
    
    function delete_SubAward_Data(data, selectedValue){
        var msg = '<bean:message bundle="budget" key="subAwardBudget.delete.confirm" />';
        if (confirm(msg)==true){
            document.budgetSubAward.acType.value = data;
            document.budgetSubAward.action = "<%=request.getContextPath()%>/deleteSubAwardBudget.do?deletedIndex="+selectedValue;
            document.budgetSubAward.submit(); 
        }
    }
    
    function validate_upload_form(){
        var organization = document.budgetSubAward.organizationName.value;
        var filename = document.budgetSubAward.fileName.value;
        var validateOrganizationFlag = false;
        var validateFlag = false;
        if(organization != null && organization.length >0){
            <!-- Do Nothing -->
            validateOrganizationFlag = true;
        }else{
            alert('<bean:message key="subAwardBudget.save.OrganizationValidate" bundle="budget"/>');
            validateOrganizationFlag = false;
            document.budgetSubAward.organizationName.focus();
        }
        if(validateOrganizationFlag){
            validateFlag = true;
        }
        return validateFlag;
    }
    
    function view_form_data(file, selectedValue){
        window.open("<%=request.getContextPath()%>/viewSubAwardBudget.do?fileType="+file+"&selectedRow="+selectedValue); 
    }
    
    function view_xml_data(file, selectedValue){
        window.open("<%=request.getContextPath()%>/viewSubAwardBudget.do?fileType="+file+"&selectedRow="+selectedValue); 
    }
    
    function cancel_SubAward(){        
        clear_data();
        document.getElementById('attachmentId').style.display = 'none';
        document.getElementById('header_label').style.display = 'block';
    }
    
    function clear_data(){
        document.budgetSubAward.organizationName.value = "";
        document.budgetSubAward.comments.value = "";
        document.budgetSubAward.document.value = "";
        document.budgetSubAward.acType.value = "";
        document.budgetSubAward.fileName.value = "";        
    }
    
    function getSubAwardDetails(organizationName, proposalNumber, versionNumber, subAwardNumber, enableSync, readOnly){

      	var url = "<%=request.getContextPath()%>/getSubAwardDetails.do?organizationName="+organizationName+"&proposalNumber="+proposalNumber+"&versionNumber="+versionNumber+"&subAwardNumber="+subAwardNumber+"&enableSync="+enableSync+"&readOnly="+readOnly;
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
   String budgetStatusMode = (String)session.getAttribute("budgetStatusMode");
   boolean readOnly = false;
   if(mode!= null && mode.equals("display")){
       readOnly = true;
   }else if(budgetStatusMode != null && budgetStatusMode.equals("complete")){
        readOnly = true;
   }
   boolean periodsGenerated = false;
   if(session.getAttribute("isPeriodGenerated") != null){
     periodsGenerated = ((java.lang.Boolean)session.getAttribute("isPeriodGenerated")).booleanValue();
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

<html:form action="/getSubAwardBudget.do" method="post"  enctype="multipart/form-data">
    <table width="100%" height="100%"  border="0" cellpadding="0" cellspacing="0" class="table">
        <tr class="table">
            <td>            
                <table width="100%" border="0" cellpadding="0" cellspacing="0" class='tableheader'>
                    <tr>
                        <td height="2%" align="left" valign="top" class="theader">  Sub Award budget  </td>
                        <td colspan='2' class='helptext'>
                            <div id="helpText">            
                                <bean:message bundle="budget" key="helpTextBudget.subAwardBudget"/>
                            </div>
                        </td>
                    </tr>
                </table>            
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
                            <li><bean:write name = "message" bundle="budget"/></li>
                        </html:messages>                    
                    </logic:messagesPresent>
                </font>
            </td>
        </tr>
        <tr>
            <td colspan="3" align="left" valign="top">
                <table width="100%" height="2%"  border="0" cellpadding="0" cellspacing="0" class="tableheader">
                    <tr>
                        <td>
                            <div id='header_label' id='header_label' style='display: block;'>
                                <%if(!readOnly){%>
                                <html:link href="javascript:called();">                      
                                    <u><bean:message bundle="budget" key="subAwardBudget.AddDocuments"/></u>
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
                                            <bean:message bundle="budget" key="subAwardBudget.Organization"/>:
                                            </td>
                                        <td width='43%' align=left>&nbsp;
                                        <!-- JM 7-11-2013 change to drop-down box -->
                                        <html:select property="organizationName">
                                        <html:option value="0"><i>Please select an organization</i></html:option>
                                        <% 
                                       	edu.mit.coeus.propdev.bean.ProposalSiteBean orgBean; 
                                       	String orgName = "";
                                        for(int i=0;i<organizations.size();i++) { 
	                                       	orgBean = (edu.mit.coeus.propdev.bean.ProposalSiteBean) organizations.get(i);
	                                       	orgName = orgBean.getLocationName();
                                        %>
                                        <html:option value="<%=orgName %>"><%=orgName %></html:option>
										<% } %>
                                        </html:select>
                                        <!-- JM END -->
                                        </td>
                                   </tr>
                                    <tr>
                                        <td width="15%" align="right" nowrap class="copybold">                                            
                                            <bean:message bundle="budget" key="subAwardBudget.Description"/>:
                                        </td>
                                        <td width='43%' align=left>&nbsp;<html:text property="comments" maxlength="200" styleClass="textbox-long" style="width:290px;" disabled="<%=readOnly%>" readonly="false" onchange="dataChanged()"/></td>
                                    </tr>
                                    <tr>
                                        <td align="right" nowrap class="copybold">                                           
                                            <bean:message bundle="budget" key="subAwardBudget.FileName"/>:
                                        </td>
                                        
                                        <td align = "left" style='copy'> 
                                            &nbsp;<html:file property="document" accept="application/pdf" onchange="selectFile()" maxlength="250" size = "50" disabled="<%=readOnly%>"/>
                                            <html:text property="fileName" style="width: 650px;" styleClass="cltextbox-color" disabled="true" readonly="true"/>
                                        </td>
                                    </tr>
                                </table>
                                
                            </td>
                        </tr> 
                        <%if(!readOnly){%>
                        <tr class='table'>                       
                            <td class='savebutton'>                            
                                <html:button property="Save" value="Save" styleClass="clsavebutton" onclick="save_SubAward_Data()" /> 
                                <html:button property="Cancel" value="Cancel" styleClass="clsavebutton" onclick="cancel_SubAward()"/> 
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
                <logic:present name = "lstSubAwardBudget">
                    <logic:notEmpty name = "lstSubAwardBudget">
                        <tr>
                            <td  align="left" valign="top" class='core'>
                                <table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0" class="tabtable">                                    
                                    <tr align="center">
                                        <td colspan="10">
                                            <table width="100%"  border="0" cellpadding="0" cellspacing="0" id="t1" class="sortable" >
                                                <tr>                   
                                                    <td width="15%" align="left" class="theader"><bean:message bundle="budget" key="subAwardBudget.Organization"/></td>
                                                    <td width="25%" align="left" class="theader"><bean:message bundle="budget" key="subAwardBudget.FormName"/></td>
                                                    <td width="5%" align="left" class="theader"><bean:message bundle="budget" key="subAwardBudget.Pdf"/></td>
                                                    <td width="5%" align="left" class="theader"><bean:message bundle="budget" key="subAwardBudget.Xml"/></td>
                                                    
                                                    <td width="5%" align="center" class="theader">&nbsp;</td>
                                                    <td width="5%" align="left" class="theader">&nbsp;</td>
                                                    <td width="5%" align="left" class="theader">&nbsp;</td>
                                                    <td width="5%" align="center" class="theader">&nbsp;</td>
                                                </tr>
                                                <% 
                                                String strBgColor = "#DCE5F1";
                                                int count = 0;
                                                %>
                                                
                                                <logic:iterate id="subAwardBudgetList" name="lstSubAwardBudget" type="edu.mit.coeus.budget.bean.BudgetSubAwardBean">
                                                    <% 
                                                    if (count%2 == 0)
                                                        strBgColor = "#D6DCE5";
                                                    else
                                                        strBgColor="#DCE5F1";                                                
                                                
                                                String docComments =  subAwardBudgetList.getComments();
                                                String formName = "";
                                                if(subAwardBudgetList.getFormName() != null){
                                                    formName = subAwardBudgetList.getFormName();
                                                }else{
                                                    formName = "None";
                                                }
                                                String subAwardFormLink = "javascript:edit_SubAward_Data('U','" +count +"')";
                                                    %>
                                                    <tr  bgcolor="<%=strBgColor%>" width="15%" valign="top"  onmouseover="className='TableItemOn'"  onmouseout="className='TableItemOff'">                            
                                                        <td width="15%" height='20' align="left" class="copy">
                                                            <%String divName="Panel"+count;%>
                                                            <div id='<%=divName%>'>
                                                                
                                                                <%String divlink = "javascript:showHide(1,'"+count+"')";%>
                                                                <html:link href="<%=divlink%>">                                                                   
                                                                    <%String imagePlus=request.getContextPath()+"/coeusliteimages/plus.gif";%>
                                                                    <html:img src="<%=imagePlus%>" border="0"/>                                                                    
                                                                </html:link> &nbsp;&nbsp;
                                                                <%if(!readOnly){%>
                                                                <html:link href="<%=subAwardFormLink%>" >
                                                                    <%=subAwardBudgetList.getOrganizationName()%>
                                                                </html:link> 
                                                                <%}else{%>
                                                                <%=subAwardBudgetList.getOrganizationName()%>
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
                                                                    <html:link href="<%=subAwardFormLink%>" >
                                                                        <%=subAwardBudgetList.getOrganizationName()%>
                                                                    </html:link> 
                                                                <%}else{%>
                                                                    <%=subAwardBudgetList.getOrganizationName()%>
                                                                <%}%>
                                                            </div>
                                                        </td>                            
                                                        <td width="25%" align="left"  class="copy">
                                                            
                                                            <%if(!readOnly){%>
                                                            <html:link href="<%=subAwardFormLink%>" >
                                                                <%=formName%>
                                                            </html:link> 
                                                            <%}else{%>
                                                                <%=formName%>
                                                            <%}%>
                                                        </td>
                                                        <td width="5%" align="left"  class="copy">
                                                            <%String imagePdf = request.getContextPath()+"/coeusliteimages/none.gif";
                                                            if(subAwardBudgetList.getPdfFileName() != null){
                                                                imagePdf=request.getContextPath()+"/coeusliteimages/complete.gif";%>
                                                            <%}%>
                                                            <html:img src="<%=imagePdf%>" border="0"/>
                                                        </td>
                                                        <td width="5%" align="left"  class="copy">                                                            
                                                            <%
                                                            String imageXml="";
                                                            if(subAwardBudgetList.getXmlUpdateUser() != null){
                                                                imageXml=request.getContextPath()+"/coeusliteimages/complete.gif";
                                                            }else{
                                                                imageXml=request.getContextPath()+"/coeusliteimages/none.gif";
                                                            }%>
                                                            <html:img src="<%=imageXml%>" border="0"/>
                                                        </td>
                                                        <td width="5%"  align="center" nowrap class="copy">
                                                            <%                                    
                                                            boolean enableSync = false;
                                                            if(BudgetSubAwardConstants.XML_GENERATED_SUCCESSFULLY.equals(subAwardBudgetList.getTranslationComments())){
                                                                enableSync = true;
                                                            }
                                                            
                                                            String subAwardDetailsLink = 
                                                                    "javascript:getSubAwardDetails('" +subAwardBudgetList.getOrganizationName()+ "'," +
                                                                    " '" +subAwardBudgetList.getProposalNumber()+ "', " +
                                                                    "'" +subAwardBudgetList.getVersionNumber()+ "', " +
                                                                    "'" +subAwardBudgetList.getSubAwardNumber()+ "', " +
                                                                    "'" +enableSync+ "', " +
                                                                    "'" +readOnly+ "')";
                                                                    if(periodsGenerated){%>
                                                            &nbsp;&nbsp; <html:link href="<%=subAwardDetailsLink%>">
                                                                <bean:message bundle="budget" key="subAwardBudget.Details"/>
                                                            </html:link>
                                                            <%}%>
                                                        </td>
                                                        <td width="5%"  align="center" nowrap class="copy">
                                                            <%                                    
                                                            String viewFormLink = "javascript:view_form_data('pdf','" +count +"')";
                                                            %>
                                                            &nbsp;&nbsp; <html:link href="<%=viewFormLink%>">
                                                                <bean:message bundle="budget" key="subAwardBudget.ViewForm"/>
                                                            </html:link>
                                                        </td>
                                                        <td width="5%" align="center"  nowrap class="copy">
                                                            <%
                                                            String viewXmlLink = "javascript:view_xml_data('xml','" +count +"')";
                                                            %>
                                                            &nbsp;&nbsp;<html:link href="<%=viewXmlLink%>">
                                                                <bean:message bundle="budget" key="subAwardBudget.ViewXml"/>&nbsp;
                                                            </html:link>
                                                        </td>                                                        
                                                        <td width="5%" align="center" nowrap class="copy">
                                                            <%                                    
                                                            String removeLink = "javascript:delete_SubAward_Data('D','" +count +"')";
                                                            if(!readOnly){%>
                                                            &nbsp;&nbsp; <html:link href="<%=removeLink%>">
                                                                <bean:message bundle="budget" key="subAwardBudget.Remove"/>
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
                                                                    <tr class="theader"><td><bean:message bundle="budget" key="subAwardBudget.Details"/></td></tr>
                                                                    <tr>
                                                                        <td>
                                                                            <table width="100%" height="100%" border="0">
                                                                                <tr class="copy">
                                                                                    <td><b><bean:message bundle="budget" key="subAwardBudget.attachments"/> :</b> &nbsp;<%=subAwardBudgetList.getPdfFileName()%> </td>
                                                                                </tr>
                                                                                <tr class="copy">
                                                                                    <td><b><bean:message bundle="budget" key="subAwardBudget.pdfFile"/> :</b> &nbsp;<%=subAwardBudgetList.getPdfFileName()%> </td>
                                                                                </tr>
                                                                                <tr class="copy">
                                                                                    <%
                                                                                        String pdfUpdateTimeStamp = "";
                                                                                        String pdfUpdateUser = "";
                                                                                        if(subAwardBudgetList.getPdfUpdateTimestamp() != null){
                                                                                            pdfUpdateTimeStamp = subAwardBudgetList.getPdfUpdateTimestamp().toString();
                                                                                        }else{
                                                                                            pdfUpdateTimeStamp = "none";
                                                                                        }
                                                                                        if(subAwardBudgetList.getPdfUpdateUser() != null){
                                                                                            pdfUpdateUser = subAwardBudgetList.getPdfUpdateUser();
                                                                                        }else{
                                                                                            pdfUpdateUser = "none";
                                                                                        }
                                                                                      StringBuilder pdfUpdateDetails = new StringBuilder(pdfUpdateTimeStamp);
                                                                                      pdfUpdateDetails.append(" by ");
                                                                                      pdfUpdateDetails.append(pdfUpdateUser);
                                                                                    %>
                                                                                    <td><b><bean:message bundle="budget" key="subAwardBudget.pdfUpdated"/> :</b> &nbsp;<%=pdfUpdateDetails%> </td>
                                                                                </tr>
                                                                                <tr class="copy">
                                                                                    <%String xmlUpdateTimeStamp = "";
                                                                                        String xmlUpdateUser = "";
                                                                                        if(subAwardBudgetList.getXmlUpdateTimestamp() != null){
                                                                                            xmlUpdateTimeStamp = subAwardBudgetList.getXmlUpdateTimestamp().toString();
                                                                                        }else{
                                                                                            xmlUpdateTimeStamp = "none";
                                                                                        }
                                                                                        if(subAwardBudgetList.getXmlUpdateUser() != null){
                                                                                            xmlUpdateUser = subAwardBudgetList.getXmlUpdateUser();
                                                                                        }else{
                                                                                            xmlUpdateUser = "none";
                                                                                        }
                                                                                      StringBuilder xmlUpdateDetails = new StringBuilder(xmlUpdateTimeStamp);
                                                                                      xmlUpdateDetails.append(" by ");
                                                                                      xmlUpdateDetails.append(xmlUpdateUser);
                                                                                    %>
                                                                                    <td><b><bean:message bundle="budget" key="subAwardBudget.xmlUpdated"/> :</b> &nbsp;<%=xmlUpdateDetails%> </td>
                                                                                </tr>
                                                                                <tr class="copy">
                                                                                    <%String subAwardUpdateTimeStamp = "";
                                                                                        String subAwardUpdateUser = "";
                                                                                        if(subAwardBudgetList.getUpdateTimestamp() != null){
                                                                                            subAwardUpdateTimeStamp = subAwardBudgetList.getUpdateTimestamp().toString();
                                                                                        }else{
                                                                                            subAwardUpdateTimeStamp = "none";
                                                                                        }
                                                                                        if(subAwardBudgetList.getUpdateUser() != null){
                                                                                            subAwardUpdateUser = subAwardBudgetList.getUpdateUser();
                                                                                        }else{
                                                                                            subAwardUpdateUser = "none";
                                                                                        }
                                                                                      StringBuilder subAwardUpdateDetails = new StringBuilder(subAwardUpdateTimeStamp);
                                                                                      subAwardUpdateDetails.append(" by ");
                                                                                      subAwardUpdateDetails.append(subAwardUpdateUser);
                                                                                    %>
                                                                                    <td><b><bean:message bundle="budget" key="subAwardBudget.subAwardUpdated"/> :</b> &nbsp;<%=subAwardUpdateDetails%> </td>
                                                                                </tr>
                                                                                <tr class="copy">
                                                                                    <td><b><bean:message bundle="budget" key="subAwardBudget.namespace"/> :</b> &nbsp;<%=subAwardBudgetList.getNamespace()%> </td>
                                                                                </tr>
                                                                                <tr class="copy">
                                                                                    <td><b><bean:message bundle="budget" key="subAwardBudget.status"/> :</b> &nbsp;<%=subAwardBudgetList.getTranslationComments()%> </td>
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
    document.budgetSubAward.fileName.value = document.budgetSubAward.fileHidden.value;
    var dataModified = '<%=request.getAttribute("dataModified")%>';    
    if(dataModified == 'modified' || (errValue && !errLock)){
        DATA_CHANGED = 'true';
    }
    LINK = "<%=request.getContextPath()%>/saveSubAwardBudget.do";
    FORM_LINK = document.budgetSubAward;
    PAGE_NAME = '<bean:message bundle="budget" key="subAwardBudget.label"/>';
    function dataChanged(){
        DATA_CHANGED = 'true';
        var selectedValue = document.budgetSubAward.selectedRow.value;
        if(selectedValue!=null){
            LINK = "<%=request.getContextPath()%>/saveSubAwardBudget.do?selectedIndex="+selectedValue;
        }else{
            LINK = "<%=request.getContextPath()%>/saveSubAwardBudget.do";
        }
        
        FORM_LINK = document.budgetSubAward;
        PAGE_NAME = '<bean:message bundle="budget" key="subAwardBudget.label"/>';
    }
    linkForward(errValue);      
</script>  
<script>
      var help = '<bean:message bundle="budget" key="helpTextBudget.subAwardBudget"/>';
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