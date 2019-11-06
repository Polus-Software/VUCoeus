<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.*,  edu.mit.coeus.utils.*, edu.mit.coeus.s2s.bean.*, edu.mit.coeus.s2s.*"%>
<%@page import="edu.wmc.coeuslite.utils.DynaBeanList, edu.mit.coeus.s2s.validator.*,edu.mit.coeus.s2s.bean.FormInfoBean"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
    <head>
        <title>Select opportunity</title>
       
        <script src="<%=request.getContextPath()%>/coeuslite/mit/utils/scripts/divSlide.js"></script>
        <script>
            var txtShow = "show";
            var txtHide = "hide";
            
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
        
            function setSubmitType(submitType) {
            document.grantsGov.elements["simpleBean[0].submitType"].value=submitType;
            }
       
            
            
            <%
                ApplicationInfoBean applicationInfoBean = (ApplicationInfoBean)request.getAttribute("submissionDetails");
                boolean submitted = false;
                int availForms = 0;
                if(applicationInfoBean != null) {
                    submitted = true;
                }
                HashMap rightFlags = (HashMap)request.getAttribute("rightFlags");
                boolean submitToSponsor = false, readyToSubmit = false, attrMatch = false;
                Boolean boolValue;
                boolValue = (Boolean)rightFlags.get(S2SConstants.SUBMIT_TO_SPONSOR);
                submitToSponsor = boolValue.booleanValue();

                boolValue = (Boolean)rightFlags.get(S2SConstants.IS_READY_TO_SUBMIT);
                readyToSubmit = boolValue.booleanValue();

                boolValue = (Boolean)rightFlags.get(S2SConstants.IS_ATTR_MATCH);
                attrMatch = boolValue.booleanValue();

                DynaBeanList dynaBeanList= (DynaBeanList)request.getAttribute("grantsGov");
                if(dynaBeanList != null && dynaBeanList.getList() != null){
                    availForms = dynaBeanList.getList().size();
                }

                boolean submit = (attrMatch && submitToSponsor && readyToSubmit);
                boolean refresh = true, selectOpportunity = false, delete = false, validate = true, print = true;

                refresh = submitted;
                selectOpportunity = !submitted;
                delete = !submitted;
                validate = !submitted;
                submit = submit && availForms>0 && !submitted;

                if(!submitted && !attrMatch){
                    refresh = false;
                    selectOpportunity = true;
                    delete = true;
                    validate = false;
                    submit = false;
                    print = false;
                }
            %>
            
            var refresh = <%=refresh%>;
            var selectOpportunity = <%=selectOpportunity%>;
            var canDelete = <%=delete%>;
            var validate = <%=validate%>;
            var print = <%=print%>;
            var submitted = <%=submitted%>;
            var attrMatch = <%=attrMatch%>;
            var forms = <%=availForms%>;
            
            function refreshSubmissionDetails() {
            if(!refresh) {
            alert("<bean:message bundle="proposal" key="grantsgov.refreshOnlyAfterSub"/>");
            return;
            }
            document.grantsGov.elements["simpleBean[0].submitType"].value="Refresh";
            document.grantsGov.submit();
            }
            
            function deleteOpportunity() {
            if(!canDelete){
            alert("<bean:message bundle="proposal" key="grantsgov.cannotDelete"/>");
            return;
            }
            document.grantsGov.elements["simpleBean[0].submitType"].value="DeleteOpportunity";
            document.grantsGov.submit();
            }
       
            function selectAnotheropportunity() {
            if(!selectOpportunity){
            alert("<bean:message bundle="proposal" key="grantsgov.cannotSelectAnotherOpp"/>");
            return;
            }
            document.grantsGov.elements["simpleBean[0].submitType"].value="SelectAnotherOpportunity";
            document.grantsGov.submit();
            }
       
            function validateForm() {
                if(!submitted && !attrMatch) {
                    alert("<bean:message bundle="proposal" key="grantsgov.cfdaProgramNumMismatch"/>");
                    return;
                }else if(!validate){
                    alert("<bean:message bundle="proposal" key="grantsgov.cannotValidate"/>");
                    return;
                }
            document.grantsGov.elements["simpleBean[0].submitType"].value="Validate";
            document.grantsGov.submit();
            }
       
            function submitToGrantsGov() {
            document.grantsGov.elements["simpleBean[0].submitType"].value = "Validate" + "SubmitToGrantsGov";
            document.grantsGov.submit();
            }
            
            function printForms() {
                var checkBoxName;
                var selForms = "";
                var available = "";
                
                for(index=0; index < forms; index++) {
                    checkBoxName = "listBean["+index+"].print"
                    if(document.grantsGov.elements[checkBoxName].checked) {
                        available = "listBean["+index+"].available";
                        if(document.grantsGov.elements[available].value != "true") {
                            alert("<bean:message bundle="proposal" key="grantsgov.selectAvailForms"/>");
                            return;
                        }
                        
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
                document.grantsGov.target="_blank";
                document.grantsGov.submit();
                
                document.grantsGov.target="";
                //window.open("grantsGovAction.do?action=Print&selForms="+selForms);
                
            }//End Print Forms
            
            function includeClicked(field, checkbox){
                document.grantsGov.elements[field].value = document.grantsGov.elements[checkbox].checked;
            }
            
            function selectDeselectAll(value){
                //var value = document.grantsGov.elements["checkboxAll"].checked;
                var checkBoxName;
                for(index=0; index < forms; index++) {
                    if(value == true) {
                        //Select All Included Only
                        includeSelected = "listBean["+index+"].includeSelected"
                        if(document.grantsGov.elements[includeSelected].value != "true"){
                            continue;
                        }
                    }
                    checkBoxName = "listBean["+index+"].print"
                    document.grantsGov.elements[checkBoxName].checked = value;
                }
            }
            
            function otherRadioBtnClicked(otherClicked) {
                if(otherClicked) {
                    var value = document.grantsGov.elements["simpleBean[0].revisionCodeOther"].checked;
                    document.grantsGov.elements["simpleBean[0].revisionCodeAward"][0].checked = false;
                    document.grantsGov.elements["simpleBean[0].revisionCodeAward"][1].checked = false;
                    document.grantsGov.elements["simpleBean[0].revisionCodeDuration"][0].checked = false;
                    document.grantsGov.elements["simpleBean[0].revisionCodeDuration"][1].checked = false;
                }else {
                    document.grantsGov.elements["simpleBean[0].revisionCodeOther"].checked = false;
                }
            }
            
            preLoadImages();
        </script>
    </head>
    <body>
           
        <table align="center" border="0" cellspacing="0" cellpadding="0" width="100%" class="table">
            <tbody>
                <tr class="theader">
                    <td>
                        <table width="100%" cellspacing="0" cellpadding="0">
                        <tr>
                        <th align="left"> Grants.Gov </th>
                    </td>
                        </tr>
                    </table>
                    </td>
                </tr>
                
                <html:form action="grantsGovAction">
                    
                    <!-- validation Messages - START -->
                    <%
                        Exception ex = (Exception)request.getAttribute("Exception");
                        String message = (String)request.getAttribute("Message");
                        if(ex != null) {%>
                        <tr><td>
                        <br>
                    <table align="center" width="98%" border="0" cellspacing="0" cellpadding="0">
                        <tr><td>
                        <table width="100%" border="0" cellspacing="0" cellpadding="2" class="theader">
                        <tr><td>
                            <a href="javascript:showHidePanel('errorDetails','imgErrorDetails','linkErrorDetails')">
                                &nbsp;<img src="<%=request.getContextPath()%>/coeusliteimages/minus.gif" border="0" id="imgErrorDetails" name="imgErrorDetails">&nbsp;
                            </a>Validation errors
                        </td>
                            <td align="right">
                                <a id="linkErrorDetails" href="javascript:showHidePanel('errorDetails','imgErrorDetails','linkErrorDetails')">hide</a>
                            </td>
                        </tr>
                        </table>
                        </td></tr>
                        
                        <tr><td>
                        <div id="errorDetails" style="overflow:hidden">
                          <table align="center" width="100%" border="0" cellspacing="0" cellpadding="0" class="tabtable">
                                     
                        <tr><td class="copy">
                        <table width="100%" border="0"> 
                        <tr><td>
                        <img src="<%=request.getContextPath()%>/coeusliteimages/error.gif" hspace="5" vspace="5">
                        </td>
                        <td class="copybold">
                            <font color="red">
                    <%
                    String exceptionMessage = ex.getMessage();
                    //if message has a link. display link in next line
                     int index = 0;
                    if(exceptionMessage != null && exceptionMessage.length() > 0) {
                        index = exceptionMessage.indexOf("<a");
                        if(index != -1){
                        exceptionMessage = exceptionMessage.substring(0, index) +"<br>"+exceptionMessage.substring(index, exceptionMessage.length());
                        }
                        out.print(exceptionMessage);
                    }
                %><br>Please Correct the Following Errors:</font>
                        </td></tr>
                        </table>
                    </td></tr>
                    <tr><td>
                    <table align="center" width="98%" border="0" cellspacing="0" cellpadding="2" class="tabtable">
                <%
                    if(ex instanceof S2SValidationException) {
                            S2SValidationException s2SValidationException = (S2SValidationException)ex;
                            List errList = s2SValidationException.getErrors();
                            S2SValidationException.ErrorBean errorBean;
                            FormInfoBean formInfoBean;
                            for(index = 0; index < errList.size(); index++) {
                                errorBean = (S2SValidationException.ErrorBean)errList.get(index);
                                if(errorBean.getMsgObj() instanceof FormInfoBean) {
                                    formInfoBean = (FormInfoBean)errorBean.getMsgObj();
                                    %><tr class="rowLine table"><td><b><%=formInfoBean.getFormName()%></b></td></tr>
                  <%                  
                  }else {%>
                        <tr class="rowLine"><td><%=errorBean.getMsgObj()%></td></tr>
                  <%              }
                %>
                                <%--<tr class="rowLine"><td><%=errorBean.getMsg()%></td></tr>--%>
                <%            }
                    }
                %>
                    </table> <br>  </td></tr>
                          </table>
                        </div>
                        </td></tr>
                
            </table>  </td></tr>
            <%}
                if(message != null && message.length() > 0) {%>
                    <script>alert("<%=message%>");</script>
            <%}%>
            <!-- validation Messages - END -->
            
                    <tr> <!-- Submission Status -->
                        <td>
                            <br>
                            <table align="center" width="98%" border="0" cellspacing="0" cellpadding="0">
                                <tr> <!-- Header -->
                                    <td>
                                        <table width="100%" border="0" cellspacing="0" cellpadding="2" class="theader"><tr><td>
                                            <a href="javascript:showHidePanel('submissionDetails','imgSubmissionDetails','linkSubmissionDetails')">
                                                &nbsp;<img src="<%=request.getContextPath()%>/coeusliteimages/minus.gif" border="0" id="imgSubmissionDetails" name="imgSubmissionDetails">&nbsp;
                                            </a>Submission Details ( <a href="javascript:refreshSubmissionDetails()">Refresh</a> )
                                        </td>
                                        <td align="right">
                                            <a id="linkSubmissionDetails" href="javascript:showHidePanel('submissionDetails','imgSubmissionDetails','linkSubmissionDetails')">hide</a>
                                        </td>
                                        </tr></table>
                                    </td>
                                </tr>
                                <tr><!-- Details -->
                                    <td colspan="2">
                                        <div id="submissionDetails" style="overflow:hidden">
                                            <table align="center" width="100%" border="0" cellspacing="0" cellpadding="0" class="tabtable">
                                                <%if(submitted){%>
                                                
                                                <tr><td><table cellpadding="2" class="copy">
                                                
                                                    <tr>
                                                        <td align="right" class="copybold" nowrap>Received Date: </td>
                                                        <td><%=applicationInfoBean.getReceivedDateTime()%></td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right" class="copybold" nowrap>Last Modified Date: </td>
                                                        <td><%=applicationInfoBean.getStatusDate()==null ? "" : applicationInfoBean.getStatusDate().toString()%></td>
                                                    </tr>
                    
                                                    <tr>
                                                        <td align="right" class="copybold" nowrap>Submission Status: </td>
                                                        <td><%=applicationInfoBean.getStatus()%></td>
                                                    </tr>
                    
                                                    <tr>
                                                        <td align="right" class="copybold" nowrap>Grants Tracking Id: </td>
                                                        <td><%=applicationInfoBean.getGrantsGovTrackingNumber()%></td>
                                                    </tr>
                    
                                                    <tr>
                                                        <td align="right" class="copybold" nowrap>Agency Tracking Id: </td>
                                                        <td><%=applicationInfoBean.getAgencyTrackingNumber()==null ? "" : applicationInfoBean.getAgencyTrackingNumber()%></td>
                                                    </tr>
                    
                                                    <tr>
                                                        <td align="right" class="copybold" nowrap valign="top">Notes: </td>
                                                        <td><%=((SubmissionDetailInfoBean)applicationInfoBean).getComments()%></td>
                                                    </tr>
    
                                                </table></td></tr>
    
                                                <tr>
                                                    <td colspan="2">
                                                        <br>
                                                        <table align="center" width="75%" border="0" cellpadding="0" cellspacing="0" class="table">
                                                            <tr>
                                                                <td class="theader">Attachments</td>
                                                            </tr>
                                                            <%List lstAttachments = applicationInfoBean.getAttachments();
                                                                if(lstAttachments != null && lstAttachments.size() > 0){
                                                                    Attachment attachment;
                                                                    for(int index = 0; index < lstAttachments.size(); index++) {
                                                                        attachment = (Attachment)lstAttachments.get(index);
                                                                        out.print("<tr class='rowLine'><td class='copybold'>"+attachment.getContentId()+"<td></tr>");
                                                                    }//End For Attachments
                                                                } else{%>
                                                            <tr><td>No Attachments</td></tr>
                                                            <%}%>
                                                        </table>
                                                        <br>
                                                    </td>
                                                </tr>
                                                <%}else{%>
                                                <tr>
                                                    <td>Submission Details not available.</td>
                                                </tr>
                                                <%}%>
                                            </table>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <% if(applicationInfoBean == null) {%>
                        <script>
                            hide('submissionDetails','imgSubmissionDetails','linkSubmissionDetails');
                        </script>
                        <%}%>
                    </tr> <!-- End Submission Status -->
            
                    <tr align="center"> <!-- Opportunity -->
                        <td>
                    
                            <logic:iterate id="simpleBean" name="grantsGov" property="beanList" type="org.apache.struts.action.DynaActionForm" indexId="ctr">

                                <br>
                                <table align="center" width="98%" border="0" cellspacing="0" cellpadding="0">
                                    <tr class="theader">
                                        <td>
                                            <table width="100%" border="0" cellspacing="0" cellpadding="2" class="theader">
                                                <tr>
                                                    <td align="left">
                                                    <a href="javascript:showHidePanel('opportunity','imgOpportunity','linkOpportunity')">
                                                        &nbsp;<img src="<%=request.getContextPath()%>/coeusliteimages/minus.gif" border="0" id="imgOpportunity" name="imgOpportunity">&nbsp;
                                                    </a>
                                                    Opportunity </td>
                                                    <td align="right">
                                                        <a id="linkOpportunity" href="javascript:showHidePanel('opportunity','imgOpportunity','linkOpportunity')">hide</a>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                    <tr>
                                    <td colspan="2">
                            
                                        <div id="opportunity" style="overflow:hidden">
                                            <table align="center" width="100%" border="0" cellspacing="0" cellpadding="2" class="tabtable">
                                                <tr>
                                                    <td align="right" class="copybold"><bean:message bundle="proposal" key="generalInfoProposal.opportunityId"/></td>
                                                    <td colspan="3"><bean:write name="simpleBean" property="opportunityId"/><html:hidden property="opportunityId" name="simpleBean" indexed="true"/><html:hidden property="proposalNumber" name="simpleBean" indexed="true"/></td>
                                                </tr>
                                                <tr>
                                                    <td align="right" class="copybold">Opportunity Title:</td>
                                                    <td colspan="3"><bean:write name="simpleBean" property="opportunityTitle"/><html:hidden property="opportunityTitle" name="simpleBean" indexed="true"/></td>
                                                </tr>
                                                <tr>
                                                    <td align="right" class="copybold">Submission Type:</td>
                                                    <td colspan="3">
                                                    <%-- //If Form Element is disabled. the value won't be submitted --%>
                                                        <%if(submitted){%>
                                                            <html:hidden name="simpleBean" property="submissionTypeCode" indexed="true"/>
                                                        <%}%>
                                                        <html:select property="submissionTypeCode" name="simpleBean" indexed="true" disabled="<%=submitted%>" onchange="dataChanged()">
                                                            <option>Select Submission Type</option>
                                                            <logic:iterate id="comboBoxBean" name="submissionTypes" type="edu.mit.coeus.utils.ComboBoxBean" indexId="ctr">
                                                                <% boolean select = false;
                                                                    String selected = "";
                                                                    if(simpleBean.get("submissionTypeCode").toString().equals(comboBoxBean.getCode())) {
                                                                        select = true;
                                                                        selected = "selected";
                                                                    }
                                                                %>
                                                                <option value="<bean:write name="comboBoxBean" property="code"/>" <%=selected%>><bean:write name="comboBoxBean" property="description"/></option>
                                                            </logic:iterate>
                                                        </html:select>
                                                        
                                                        </select>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td colspan="4">
                                                        <table align="center" width="90%"><tr><td>
                                                            <fieldset>
                                                                <legend class="copybold">Revision</legend>
                                                                <table width="80%" class="copy">
                                                                    <tr align="right">
                                                                        <td>Increase Award<html:radio property="revisionCodeAward" value="A" name="simpleBean" indexed="true" disabled="<%=submitted%>" onclick="otherRadioBtnClicked(false)" onchange="dataChanged()" /></td>
                                                                        <td>Decrease Award<html:radio property="revisionCodeAward" value="B" name="simpleBean" indexed="true" disabled="<%=submitted%>" onclick="otherRadioBtnClicked(false)" onchange="dataChanged()" /></td>
                                                                        <td>Other(Specify)<html:radio property="revisionCodeOther" value="E" name="simpleBean" indexed="true" disabled="<%=submitted%>" onclick="otherRadioBtnClicked(true)" onchange="dataChanged()" /></td>
                                                                    </tr>
                                                                    <tr align="right">
                                                                        <td>Decrease Duration<html:radio property="revisionCodeDuration" value="D" name="simpleBean" indexed="true" disabled="<%=submitted%>" onclick="otherRadioBtnClicked(false)" onchange="dataChanged()" /></td>
                                                                        <td>Increase Duration<html:radio property="revisionCodeDuration" value="C" name="simpleBean" indexed="true" disabled="<%=submitted%>" onclick="otherRadioBtnClicked(false)" onchange="dataChanged()" /></td>
                                                                        <td><html:text property="revisionOtherDescription" name="simpleBean" indexed="true" disabled="<%=submitted%>" onchange="dataChanged()" /></td>
                                                                    </tr>
                                                                </table>
                                                            </fieldset>
                                                        </td></tr></table>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td align="right" class="copybold"><bean:message bundle="proposal" key="generalInfoProposal.cfdaCode"/></td>
                                                    <td><bean:write name="simpleBean" property="cfdaNumber"/><html:hidden property="cfdaNumber" name="simpleBean" indexed="true"/></td>
                                                    <td align="right" class="copybold">Competition Id:</td>
                                                    <td width="100"><bean:write name="simpleBean" property="competitionId"/><html:hidden property="competitionId" name="simpleBean" indexed="true"/></td>
                                                </tr>
                        
                                                <tr>
                                                    <td align="right" class="copybold">Opening Date:</td>
                                                    <%--Bug Fix	JIRA COEUSDEV-451, COEUSQA-2493 (Clone) - START--%>
                                                    <td><bean:write name="simpleBean" property="openingDate"/><html:hidden property="openingDate" name="simpleBean" indexed="true"/></td>
                                                    <td align="right" class="copybold">Closing Date:</td>
                                                    <td width="100"><bean:write name="simpleBean" property="closingDate"/><html:hidden property="closingDate" name="simpleBean" indexed="true"/></td>
                                                    <%--Bug Fix	JIRA COEUSDEV-451, COEUSQA-2493 (Clone) - END--%>
                                                </tr>
                        
                                                <tr>
                                                    <td align="right" class="copybold">Schema URL:</td>
                                                    <td colspan="3"><a href="<bean:write name="simpleBean" property="schemaUrl"/>" target="_blank"><bean:write name="simpleBean" property="schemaUrl"/></a>
                                                    <html:hidden property="schemaUrl" name="simpleBean" indexed="true"/>
                                                    </td>
                                                </tr>
                        
                                                <tr>
                                                    <td align="right" class="copybold">Instruction Page:</td>
                                                    <td colspan="3"><a href="<bean:write name="simpleBean" property="instructionUrl"/>" target="_blank"><bean:write name="simpleBean" property="instructionUrl"/></a>
                                                    <html:hidden property="instructionUrl" name="simpleBean" indexed="true"/>
                                                    </td>
                                                </tr>
                                            </table>
                                        </div>
                            
                                    </td></tr>
                                </table>
                                <html:hidden property="submitType" name="simpleBean" indexed="true" value="Save"/>
                                <html:hidden property="updateTimestamp" name="simpleBean" indexed="true"/>
                            </logic:iterate>
                
                        </td>
                    </tr><!-- End Opportunity -->
                
                
                    <tr> <!-- Forms -->
                        <td>
                            <br>
                            <table align="center" width="98%" border="0" cellspacing="0" cellpadding="0">
                                <tr>
                                    <td>
                                        <table align="center" width="100%" border="0" cellspacing="0" cellpadding="2">
                                            <tr class="copybold, theader">
                                                <td>
                                                <a href="javascript:showHidePanel('forms','imgForms','linkForms')">
                                                    &nbsp;<img src="<%=request.getContextPath()%>/coeusliteimages/minus.gif" border="0" id="imgForms" name="imgForms">&nbsp;
                                                </a>Forms</td>
                                                <td align="right">
                                                    <a id="linkForms" href="javascript:showHidePanel('forms','imgForms','linkForms')">hide</a>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                                
                                <tr>
                                    <td colspan="2">
                                        <div id="forms" style="overflow:hidden">
                                            <table align="center" width="100%" border="0" cellspacing="0" cellpadding="2" class="tabtable">
                                                <tr class="copybold, theader">
                                                    <td>Form Name</td>
                                                    <td>Mandatory</td>
                                                    <td>Include</td>
                                                    <td>Desc</td>
                                                    <td width="150">Select to Print:<br> <a href="javascript:selectDeselectAll(true)">All Included</a> | <a href="javascript:selectDeselectAll(false)">None</a> </td>
                                                    <%--<td><input type="checkbox" name="checkboxAll" onclick="selectDeselectAll()"></td>--%>
                                                </tr>
                                                
                                                <logic:iterate id="listBean" name="grantsGov" property="list" type="org.apache.struts.action.DynaActionForm" indexId="ctr">
                                                    <tr class="rowLine">
                                                        <td>
                                                            <%--<html:text name= "listBean" property= "formName" indexed="true" />--%>
                                                            <bean:write name="listBean" property="formName"/>
                                                        </td>
                                                        <td>
                                                            <%--<html:checkbox property="mandatory" name= "listBean" indexed="true" disabled="true" />--%>
                                                            <html:hidden property="mandatory" name= "listBean" indexed="true" />
                                                            <logic:notEqual property="mandatory" name="listBean" value="true">
                                                                <!--<img src="../../../../coeusliteimages/reject.gif">--> &nbsp;
                                                            </logic:notEqual>
                                                            <logic:equal property="mandatory" name="listBean" value="true">
                                                                <img src="<%=request.getContextPath()%>/coeusliteimages/complete.gif">
                                                            </logic:equal>
                                                        </td>
                                                        <td>
                                                            <%
                                                                boolean disableInclude = false;
                                                                String strInclude = "";
                                                                if((listBean.get("available")).toString().equals("false") || (listBean.get("mandatory")).toString().equals("true")) {
                                                                    disableInclude = true;
                                                                }
                                                                if(submitted) {
                                                                    disableInclude = true;
                                                                }
                                                                String event = "includeClicked('listBean["+ctr+"].includeSelected', 'listBean["+ctr+"].include')";
                                                                Boolean boolInclude = (Boolean)listBean.get("include");
                                                                if(boolInclude != null && boolInclude.booleanValue()) {
                                                                    strInclude = "true";
                                                                }
                                                            %>
                                                            <%if(disableInclude){//Display Images%>
                                                                <logic:notEqual property="include" name="listBean" value="true">
                                                                    <img src="<%=request.getContextPath()%>/coeusliteimages/reject.gif">
                                                                </logic:notEqual>
                                                                <logic:equal property="include" name="listBean" value="true">
                                                                    <img src="<%=request.getContextPath()%>/coeusliteimages/complete.gif">
                                                                </logic:equal>
                                                                <html:hidden property="include" name= "listBean" indexed="true"/>
                                                            <%}else {//Display Checkbox%>
                                                            <html:checkbox property="include" name= "listBean" indexed="true" disabled="<%=disableInclude%>" onclick="<%=event%>" onchange="dataChanged()"/>
                                                            <%}%>
                                                        </td>
                                                        <td>
                                                            <%
                                                                if((listBean.get("available")).toString().equals("true")) {
                                                                        out.println("Available");
                                                                }else {
                                                                        out.println("Not Available");
                                                                }
                                                            %>
                                                            <html:hidden property="available" name="listBean" indexed="true"/>
                                                        </td>
                                                        <td align="center">
                                                            <html:checkbox property="print" name="listBean" indexed="true"/>
                                                            <html:hidden property="proposalNumber" name="listBean" indexed="true"/>
                                                            <html:hidden property="includeSelected" name="listBean" indexed="true" value="<%=strInclude%>"/>
                                                            <html:hidden property="ns" name="listBean" indexed="true"/>
                                                            <html:hidden property="schUrl" name="listBean" indexed="true"/>
                                                            <html:hidden property="formName" name="listBean" indexed="true"/>
                                                            <html:hidden property="acType" name="listBean" indexed="true"/>
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
                                        
                    <tr>
                    <td align="center"> &nbsp;</td>
                    </tr>
                
                    <tr>
                        <td align="center">
                            <a href="javascript:refreshSubmissionDetails()">Refresh</a>
                            &nbsp;&nbsp; | &nbsp;&nbsp;
                            <a href="javascript:selectAnotheropportunity()">Select Another Opportunity</a>
                            &nbsp;&nbsp; | &nbsp;&nbsp;
                            <a href="javascript:deleteOpportunity()">Delete Opportunity</a>
                            &nbsp;&nbsp; | &nbsp;&nbsp;
                            <a href="javascript:validateForm()">Validate</a>
                            &nbsp;&nbsp; | &nbsp;&nbsp;
                            <a href="javascript:printForms()">Print Selected Forms</a>
                        </td>
                    </tr>
                    
                    <tr> <!-- Buttons -->
                        <td align="center"><br>
                            <html:submit value="Save" disabled="<%=submitted%>"/>&nbsp;&nbsp;
<!-- JM 5-31-2011 removed per 4.4.2                             
                            <input type="button" value="Submit to Grants.gov" onclick="submitToGrantsGov()" <%=submit?"":"disabled"%>>
-->
                            <br><br>
                        </td>
                    </tr>
                
                </html:form>

            </tbody>
        </table>
        <script>
          var errValue = false;
          DATA_CHANGED = 'false';
          LINK = "<%=request.getContextPath()%>/grantsGovAction.do";
          FORM_LINK = document.grantsGov;
          PAGE_NAME = "Grants.gov";
          function dataChanged(){
                DATA_CHANGED = 'true';
          }
          linkForward(errValue);
        </script>
    </body>
</html>
