
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%--start 1--%>
<%@page import="org.apache.struts.taglib.html.JavascriptValidatorTag,
edu.mit.coeuslite.utils.CoeusLiteConstants, java.util.Vector"%>                
<%--end 1--%>               

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>

<html:html>
    <head>        
        <title>Coeus Web</title>
        <style>
            .cltextbox-color{ font-weight: normal; width: 500px}
        </style>
        <html:base/>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        
        <script>
            var validFlag;
            var idx;
            var errValue = false;
            var errLock = false;
            var selectedValue=0;
            
            function showHide(toggleId) {            
                //1-Show 0- Hide                
                if(toggleId == 1){                
                    document.getElementById('showOrHide').style.display = "none";
                    selectedValue=0;
                }else if(toggleId == 0){
                    document.scienceCodeForm.code.value = "";
                    document.scienceCodeForm.scienceCodeDescription.value = "";
                    document.getElementById('showOrHide').style.display = "";
                    document.getElementById('showScienceCode').style.display = "none"; 
                    selectedValue=1;
                }
            }            
            
            function search(){                
                if(document.scienceCodeForm.code.value==0){
                    open_search(true,1);
                }                       
            }
            
            function open_search(valid,index)
            {          
                validFlag = valid;
                idx = index;
                var winleft = (screen.width - 650) / 2;
                var winUp = (screen.height - 450) / 2;  
                var win = "scrollbars=1,resizable=1,width=800,height=450,left="+winleft+",top="+winUp

                sList = window.open('<%=request.getContextPath()%>/generalProposalSearch.do?type=<bean:message bundle="proposal" key="searchWindow.title.scienceCode"/>&search=true&searchName=WEBSCIENCECODESEARCH', "list", win);

                if (parseInt(navigator.appVersion) >= 4) {
                    window.sList.focus(); 
                }
            }
            
            function fetch_Data(result, scienceCode){        
                 document.scienceCodeForm.code.value = result["SCIENCE_CODE"];
                 document.scienceCodeForm.scienceCodeDescription.value = result["DESCRIPTION"];                 
                 dataChanged();         
            }
        
            function validateForm(form) { 
                insert_data('I');     
            }
            
            function insert_data(data){    
                //document.scienceCodeForm.acType.value = 'I';                
                document.scienceCodeForm.action = "<%=request.getContextPath()%>/saveProposalScienceCode.do";
            }
            
            function cancelScienceCode(){                
                document.scienceCodeForm.action = "<%=request.getContextPath()%>/getScienceCode.do";
                document.scienceCodeForm.submit();
            }
            
            function delete_data(scienceCode,timestamp){
                if (confirm("Are you sure you want to delete the science code?")==true){                                 
                    document.scienceCodeForm.awUpdateTimestamp.value=timestamp;
                    document.scienceCodeForm.awCode.value=scienceCode;
                    document.scienceCodeForm.action = "<%=request.getContextPath()%>/deleteProposalScienceCode.do";
                    document.scienceCodeForm.submit();
                }
            }
            
            function showBalloon(){
            
            }
        </script>
    </head>
    <body>
        <% 
            boolean hasValidationMessage = false;
            boolean modeValue = false;
            String mode=(String)session.getAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId());
            String scienceCodeMode = (String)session.getAttribute("proposalNumber"+session.getId());
            
            if(mode!=null && !mode.equals("")) {   
            if(mode.equalsIgnoreCase("display")){
                modeValue=true;
             }
            }       
        %>
        
        <html:form action="/saveProposalScienceCode"  method="post" onsubmit= "validateForm(this);" > 
            <a name="top"></a>
            <%--  ************  START OF BODY TABLE  ************************--%> 
            
            <!-- New Template for clProposalScienceCode - Start   -->             
            <table width="100%" height="100%"  border="0" cellpadding="0" cellspacing="0" class="table"> 
                <tr>
                    <td>
                        <table width="100%" border="0" cellpadding="0" cellspacing="0">
                            <tr>          
                                <td width="50%" height="20" align="left" valign="top" class="theader">  <bean:message bundle="proposal" key="proposal.scienceCode"/>
                                </td>
                                <td width="50%" class="theader" align="right">
                                    <a id="helpPageText" href="javascript:showBalloon('helpPageText', 'helpText')">
                                        <bean:message key="helpPageTextProtocol.help"/>
                                    </a>
                                </td>
                            </tr>                                                                                                                            
                        </table>
                    </td>
                </tr>
                
                <tr>
                    <td>
                        <!-- Showing the Locking messages - start -->
                        <logic:messagesPresent message = "true"> 
                            <script>errValue = true;</script>
                            <!-- If lock is deleted then show this message --> 
                            <font color='red'>
                                <html:messages id="message" message="true" property="errMsg" bundle="proposal">
                                    <script>errLock = true;</script>
                                    <li><bean:write name = "message"/></li>
                                </html:messages>
                                 <html:messages id="message" message="true" property="invalidScienceCode" bundle="proposal">
                                    <%hasValidationMessage = true;%>
                                    <li><bean:write name = "message"/></li>
                                </html:messages>
                            </font>
                        </logic:messagesPresent>
                        <!-- Showing the Locking messages - End -->
                    </td>
                </tr>
                 <% if(!modeValue){%>
                <tr class='table' >
                    <td><div id="showScienceCode"><a href="javascript:showHide(selectedValue);"><u>Add Science Code</u></a></div></td>
                </tr>
                <%}%>
                <tr>
                    <td  align="left" valign="top" class='core'>
                        <div id="showOrHide" style="display:none">
                            <table width="100%"  border="0" cellpadding="3" class="tabtable" cellspacing='3'>
                                <tr>                                               
                                    <td nowrap class="copybold" width="10%" align="right">                                    
                                        <bean:message bundle="proposal" key="proposal.scienceCodeNo"/>:&nbsp;
                                    </td>                                                
                                    <td class="copysmall" width="10%" align="left">
                                        <html:text property="code" readonly="true" styleClass="textbox-long" onchange="dataChanged()"  maxlength="200" />
                                    </td>
                                    <td width="10%" align="left">
                                        <div id='hide_Search' style='display: block;'>
                                            
                                            <html:link href="javascript:search()">
                                                <u><bean:message key="label.search"/></u> 
                                            </html:link>
                                            
                                        </div>                                                    
                                    </td>
                                    <td width="60%" align="left">
                                        &nbsp;
                                    </td>                                                
                                </tr>
                                
                                <tr>
                                    <td nowrap class="copybold" width="10%" align="right">
                                        <bean:message bundle="proposal" key="proposal.scienceCodeDescription"/>:&nbsp;
                                    </td>
                                    <td  colspan="3" width="20%" align="left">
                                        <html:text property="scienceCodeDescription" styleClass="cltextbox-color" readonly="true"/>
                                    </td>
                                </tr>
                                
                                <tr class="table">
                                    <td colspan="4" width="100%" class="table">
                                        
                                        <html:submit property="Save" value="Save" styleClass="clsavebutton" />
                                        <html:button property="Save" styleClass="clsavebutton" onclick="cancelScienceCode()">
                                            <bean:message bundle="iacuc" key="button.label.cancel"/>
                                        </html:button>
                                        
                                    </td>
                                </tr>
                                
                            </table>        
                        </div>                            
                    </td>
                </tr>
                
                <!-- List of Science Code - Start -->
                                
                <tr>
                    <td align="left" valign="top" class='core'>
                        <table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0" class="tabtable">
                            <tr align="center">
                                <td colspan="2">
                                    <table width="100%"  border="0" cellpadding="0" class="tabtable">
                                        <tr>
                                            <td width="20%" align="left" class="theader"><bean:message bundle="proposal" key="proposal.Code"/></td>
                                            <td width="45%" align="left" class="theader"><bean:message bundle="proposal" key="proposal.scienceCodeDescription"/></td>                                
                                            
                                            <td width="10%" align="left" class="theader"></td>
                                            
                                        </tr>
                                        <%
                                        //if (scienceCodes.size() > 0)
                                        //{
                                        String strBgColor = "#DCE5F1"; //BGCOLOR=EFEFEF  FBF7F7
                                        int count = 0;
                                        %>
                                        <logic:present name = "proposalScienceCodes">
                                            <logic:iterate id="scienceCodeBean" name="proposalScienceCodes" type="org.apache.struts.validator.DynaValidatorForm">
                                                
                                                <% 
                                                if (count%2 == 0)
                                                    strBgColor = "#D6DCE5";
                                                else
                                                    strBgColor="#DCE5F1"; 
                                                %>
                                                <tr  bgcolor="<%=strBgColor%>" onmouseover="className='TableItemOn'" onmouseout="className='TableItemOff'">
                                                    <td align="left" class="copy"><%=scienceCodeBean.get("code")%></td>
                                                    <td align="left" class="copy"><%=scienceCodeBean.get("scienceCodeDescription")%></td>                                        
                                                    <%   boolean scienceCodeflag = true;
                                                    if(!modeValue){
                                                    String removeLink = "javascript:delete_data('"+scienceCodeBean.get("awCode") +"','" +scienceCodeBean.get("awUpdateTimestamp") +"')";
                                                    %>
                                                    
                                                    <td nowrap class="copy">                                           
                                                        
                                                        <html:link href="<%=removeLink%>">
                                                            <bean:message bundle="proposal" key="proposal.scienceCode.Remove"/>
                                                        </html:link>
                                                    </td>
                                                    <%}else{%>
                                                    <td nowrap class="copy">                                                                                                                                                           
                                                    </td>
                                                    <%}%>
                                                </tr>
                                                <% count++;%>
                                            </logic:iterate>
                                        </logic:present>
                                        <%//}%>
                                        
                                    </table>
                                    
                                </td>
                            </tr>              
                            <tr>
                                <td>
                                    &nbsp;
                                </td>
                            </tr>
                    </table></td>
                </tr> 
                
                <!-- List of Science Code -End -->
                                
                                
                                
                <tr>
                    <td height='10'>
                        &nbsp;
                    </td>
                </tr>      
            </table>        
            <html:hidden property="awUpdateTimestamp"/>
            <html:hidden property="awCode"/>
            <html:hidden property="proposalNumber"/> 
        </html:form>
        <script>
            DATA_CHANGED = 'false';
            if(errValue && !errLock) {
                DATA_CHANGED = 'true';
            }            
            LINK = "<%=request.getContextPath()%>/saveProposalScienceCode.do";
            FORM_LINK = document.scienceCodeForm;
            PAGE_NAME = "<bean:message bundle="proposal" key="proposal.scienceCode"/>";
            function dataChanged(){
                DATA_CHANGED = 'true';
            }
            linkForward(errValue);
            <%if(hasValidationMessage){%>
            showHide(0);
            <%}%>

        </script>        
    </body>
</html:html>
