
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html;charset=UTF-8" language="java" 
import="edu.mit.coeuslite.utils.CoeusLiteConstants,
        edu.mit.coeuslite.utils.DateUtils"%>
<%@page import="org.apache.struts.taglib.html.JavascriptValidatorTag"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<jsp:useBean id="splReview" scope="session" class="java.util.Vector" />
<jsp:useBean id="approval" scope="session" class="java.util.Vector" />
<jsp:useBean id = "pdReviewList" scope="session" class="java.util.Vector" />
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>

<%  DateUtils dateUtils = new edu.mit.coeuslite.utils.DateUtils();
    String enableProtocolToDevPropLink = (String)session.getAttribute("enableProposalToProtocolLink");
    String enableIacucProtocolToDevPropLink = (String)session.getAttribute("enableIacucProtocolToDevPropLink");
    String propNumber = "";
    String path = request.getContextPath();

    boolean hasRight = false;
    if(request.getAttribute("hasRight") != null) {
        hasRight = (Boolean)request.getAttribute("hasRight");
     }
     boolean errorOccured = false;
     
%>
<html:html>
   <head>
        <title>Coeus Web</title>
        <html:base/>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <script language="javascript" type="text/JavaScript" src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/coeusLiteCalendar.js"></script>
        <script> 

        var errValue = false;
        var typeCode = '';        
        function delete_data(data,desc,timestamp,apcode,spcode,protonum){
        
         if (confirm("Are you sure you want to delete the special review?")==true){
            document.propDevSpecialReview.acType.value = data;
            document.propDevSpecialReview.specialReviewNumber.value = desc;
            document.propDevSpecialReview.approvalCode.value = apcode;
            document.propDevSpecialReview.specialReviewCode.value = spcode;
            document.propDevSpecialReview.spRevProtocolNumber.value = protonum;
            document.propDevSpecialReview.pdSpTimestamp.value = timestamp;
            document.propDevSpecialReview.action = "<%=request.getContextPath()%>/deleteSpecialReview.do";
            document.propDevSpecialReview.submit();
         }
        }
    
        function insert_data(data){
        document.propDevSpecialReview.acType.value = data;
        document.propDevSpecialReview.action = "<%=request.getContextPath()%>/propSpecialReview.do";   
        }          
        </script>
        
        <script>
        function validateForm(form) {
          //insert_data("I");
          return validatePropDevSpecialReview(form);
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
         
                var loc = '<bean:write name='ctxtPath'/>/coeuslite/mit/utils/dialogs/cwViewSpecialReview.jsp?value='+value+"&type=pS";                
                var newWin = window.open(loc,"formName",'dependent=1,toolbar=0,location=0,directories=0,status=0, menubar=0,scrollbars=0,resizable=0,width=' + w + ',height=' + h+ ',left='+ leftPos + ',top=' + topPos);
                newWin.window.focus();
         
         }        
         
         //Added for case#2990 - Proposal Special Review Enhancement - start
         function clearFields(sel){    
            dataChanged();
            typeCode = sel.options[sel.selectedIndex].value;
            
            //alert("SELECTED Code : "+ typeCode);
            if(typeCode == 1  ) {
               // alert("SELECTED Code in condition : "+ typeCode);
                <%if(enableProtocolToDevPropLink.equals("1") && hasRight){%>
                document.getElementById("mybtn").style.visibility = "visible";
                document.getElementById('hide_Search').style.visibility = "visible"
                <%}else{%>
                document.getElementById("mybtn").style.visibility ="hidden";
                document.getElementById('hide_Search').style.visibility = "hidden";
                <%}%>
            }
                //Modified for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
                //Modified for COEUSQA-3377 : Link to IACUC malfunctioning in Premium Proposal Dev - start
                //If parameter is enabled then only we need to provide search option
            if(typeCode == 2) {
                <%if(enableIacucProtocolToDevPropLink.equals("1")){%>
                document.getElementById('hide_Search').style.visibility = "visible";
                <%}else{%>
                document.getElementById('hide_Search').style.visibility = "hidden";
                document.getElementById("mybtn").style.visibility ="hidden";
                <%}%>
            }
            
            if(typeCode == '1'){
                     <%if(enableProtocolToDevPropLink.equals("1") && hasRight){%>
                        document.propDevSpecialReview.tempApprovalText.value = '';
                        document.propDevSpecialReview.tempApplicationDate.value = '';
                        document.propDevSpecialReview.tempApprovalDate.value = '';                    
                        document.getElementById('hide_Search').style.display = 'block';
                        document.getElementById('applnDate1').style.display = 'none';
                        document.getElementById('applnDate2').style.display = 'block';
                        document.getElementById('apprvDate1').style.display = 'none';
                        document.getElementById('apprvDate2').style.display = 'block';  
                        document.getElementById('divAppCode1').style.display = 'none'; 
                        document.getElementById('divAppCode2').style.display = 'block';                    
                        document.getElementById('divMandatory1').style.display = 'block';
                        document.getElementById('divMandatory2').style.display = 'none'; 
                      <%}%>  
                }else if(typeCode == '2'){
                     <%if(enableIacucProtocolToDevPropLink.equals("1")){%>
                                document.propDevSpecialReview.tempApprovalText.value = '';
                                document.propDevSpecialReview.tempApplicationDate.value = '';
                                document.propDevSpecialReview.tempApprovalDate.value = '';                    
                                document.getElementById('hide_Search').style.display = 'block';
                                document.getElementById("mybtn").style.visibility ="hidden";
                                document.getElementById('applnDate1').style.display = 'none';
                                document.getElementById('applnDate2').style.display = 'block';
                                document.getElementById('apprvDate1').style.display = 'none';
                                document.getElementById('apprvDate2').style.display = 'block';  
                                document.getElementById('divAppCode1').style.display = 'none'; 
                                document.getElementById('divAppCode2').style.display = 'block';                    
                                document.getElementById('divMandatory1').style.display = 'block';
                                document.getElementById('divMandatory2').style.display = 'none';   
                            <%}%>  
                      }else{
                            document.propDevSpecialReview.approvalCode.selectedIndex = '';
                            document.propDevSpecialReview.tempApplicationDate.value = '';
                            document.propDevSpecialReview.tempApprovalDate.value = '';
                            document.propDevSpecialReview.spRevProtocolNumber.value = '';
                            document.propDevSpecialReview.tempApprovalCode.value = '';
                            document.getElementById("mybtn").style.visibility ="hidden";
                            document.getElementById('hide_Search').style.display = 'none'; 
                            document.getElementById('applnDate1').style.display = 'block';
                            document.getElementById('applnDate2').style.display = 'none'; 
                            document.getElementById('apprvDate1').style.display = 'block';
                            document.getElementById('apprvDate2').style.display = 'none';   
                            document.getElementById('divAppCode1').style.display = 'block'; 
                            document.getElementById('divAppCode2').style.display = 'none';  
                            document.getElementById('divMandatory1').style.display = 'none';
                            document.getElementById('divMandatory2').style.display = 'block';                                        
                     }          
            //Modified COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
            //Modified for COEUSQA-3377 : Link to IACUC malfunctioning in Premium Proposal Dev - end
         }
         
         function openProtocolSearch(){
            var winleft = (screen.width - 650) / 2;
            var winUp = (screen.height - 450) / 2; 
            var win = "scrollbars=1,resizable=1,width=800,height=450,left="+winleft+",top="+winUp;            
            //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
            //if typeCode is 1 then it should allow for irb search
            //if typeCode is 2 then it should allow for iacuc search
            if(typeCode == '1'){ 
                sList = window.open('<%=request.getContextPath()%>/irbSearch.do?type=<bean:message key="searchWindow.title.protocol"/>&search=true&searchName=ALL_PROTOCOL_SEARCH', "list", win);
            }else if(typeCode == '2'){ 
                sList = window.open('<%=request.getContextPath()%>/iacucSearch.do?type=<bean:message bundle="iacuc" key="searchWindow.title.IACUCprotocol"/>&search=true&searchName=ALL_IACUC_WEB_SEARCH', "list", win);
            }
           //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
         }   
        function edit_data(desc,timestamp){
            document.propDevSpecialReview.action = "<%=request.getContextPath()%>/getPropDevSpecialReview.do?&specialReviewNumber="+desc+"&editMode=true";
            document.propDevSpecialReview.submit();
        }        
        function clearFormData(){
            document.propDevSpecialReview.acType.value='';
            document.propDevSpecialReview.specialReviewCode.value='';
            document.propDevSpecialReview.approvalCode.value='';
            document.propDevSpecialReview.applicationDate.value='';
            document.propDevSpecialReview.approvalDate.value='';
            document.propDevSpecialReview.comments.value='';
            document.propDevSpecialReview.specialReviewNumber.value='';
            document.propDevSpecialReview.spRevProtocolNumber.value='';
            document.propDevSpecialReview.pdSpTimestamp.value='';
            document.propDevSpecialReview.action = "<%=request.getContextPath()%>/getPropDevSpecialReview.do";
            document.propDevSpecialReview.submit();     
            showHide(1);
        }
        function showHide(val){
            if(val == 1){
                document.getElementById('showAddLink').style.display = "block";
                document.getElementById('showAddModifyPanel').style.display = "none";
            }else if(val == 2){
                document.getElementById('showAddLink').style.display = "none";
                document.getElementById('showAddModifyPanel').style.display = "block";
            }
        }
        
        function saveData(){
            document.propDevSpecialReview.action = "<%=request.getContextPath()%>/propSpecialReview.do";
            document.propDevSpecialReview.submit();     
        }
         function fetch_Data(result){   
            document.propDevSpecialReview.spRevProtocolNumber.value = '';
            document.propDevSpecialReview.tempApplicationDate.value = '';
            document.propDevSpecialReview.tempApprovalDate.value = '';
            document.propDevSpecialReview.tempApprovalText.value = '';
            
            if(typeCode == '1'){                
                document.propDevSpecialReview.spRevProtocolNumber.value = result["PROTOCOL_NUMBER"];
                if(result["APPLICATION_DATE"] != 'null' && result["APPLICATION_DATE"] != undefined ){ 
                    document.propDevSpecialReview.tempApplicationDate.value = result["APPLICATION_DATE"];
                }
                if(result["APPROVAL_DATE"] != 'null' && result["APPROVAL_DATE"] != undefined ){ 
                    document.propDevSpecialReview.tempApprovalDate.value = result["APPROVAL_DATE"];
                }
                document.propDevSpecialReview.tempApprovalCode.value = '2';
                document.propDevSpecialReview.tempApprovalText.value = result["PROTOCOL_STATUS_DESCRIPTION"];
                //Addded for Case#4354 - When ENABLE_PROTOCOL_TO_DEV_PROPOSAL is set - Start
                if(result["PROTOCOL_STATUS_CODE"] != 'null' && result["PROTOCOL_STATUS_CODE"] != undefined && result["PROTOCOL_STATUS_CODE"] == '203'){ 
                    var protocolNumber = result["PROTOCOL_NUMBER"];    
                    document.propDevSpecialReview.action = "<%=request.getContextPath()%>/propDevSpecialReview.do?&ExemptCheckList=true&protocolNumber="+protocolNumber;
                    document.propDevSpecialReview.submit();
                }
                //Case#4354 - End
            }
            
            //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
            //alert('typeCode..............'+typeCode);
            if(typeCode == '2'){     
                
                document.propDevSpecialReview.spRevProtocolNumber.value = result["PROTOCOL_NUMBER"];
                if(result["APPLICATION_DATE"] != 'null' && result["APPLICATION_DATE"] != undefined ){ 
                    document.propDevSpecialReview.tempApplicationDate.value = result["APPLICATION_DATE"];
                }
                
                if(result["APPROVAL_DATE"] != 'null' && result["APPROVAL_DATE"] != undefined ){ 
                    document.propDevSpecialReview.tempApprovalDate.value = result["APPROVAL_DATE"];
                }
                
                document.propDevSpecialReview.tempApprovalCode.value = '2';
                document.propDevSpecialReview.tempApprovalText.value = result["PROTOCOL_STATUS_DESCRIPTION"]; 
            }
            //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
         }
         //Added for case#2990 - Proposal Special Review Enhancement - end
         //Addded for Case#4354 - When ENABLE_PROTOCOL_TO_DEV_PROPOSAL is set - Start
         function loadExemptCheckList(){
            <%String specialReviewComment = (String)request.getAttribute("comments");
            if(specialReviewComment != null){%>
                 var spComments = '<%=specialReviewComment%>';   
                 document.propDevSpecialReview.comments.value = spComments;
             <%}%>
         }
         //Added for Case#4353 - End
        </script>

   </head>
<body onload="javascript:loadExemptCheckList()">
    
<%String proposalNo = (String)session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER+session.getId());
       if(proposalNo!= null){
           propNumber = proposalNo.toString();
          proposalNo = "["+"Proposal No. - "+ proposalNo+"]";
       }else{
          proposalNo = "";
       }
  String display =(String) request.getAttribute("display");
  String mode=(String)session.getAttribute("mode"+session.getId()); 
  boolean modeValue=false;
  if(mode!=null && !mode.equals("")) {   
      if(mode.equalsIgnoreCase("display")){
              modeValue=true;
      }
  }%>     
    

  <html:form action="/propSpecialReview"  method="post"> 
      <a name="top"></a>
             
    <%--  ************  START OF BODY TABLE  ************************--%> 
          
    <!-- New Template for cwViewFinEntity - Start   -->
  
    <table width="100%" height="100%"  border="0" cellpadding="4" cellspacing="0" class="table">

      <tr>
          <td height="20" align="left" valign="top" class="theader">
            <bean:message key="specialReviewLabel.SpecialReviews" bundle="proposal"/>
             &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=proposalNo%>
          </td>
      </tr>
      <% if(!modeValue) {%> 

      <tr class='copy'>
          <td class="copy">
            <font  color="red">            
              <logic:messagesPresent>
                <script>errValue = true;
                <%errorOccured = true;%>
                      
            </script>
                 <html:errors header="" footer=""/>
                

              </logic:messagesPresent>
                 <logic:messagesPresent message="true">
                     
                      <script>
                      errValue = true;
                    <%errorOccured = true;%>
                       
                  </script>
                      <html:messages id="message" message="true" property="error.specialreview" bundle="proposal">                
                           <li><bean:write name = "message"/></li>
                      </html:messages>
                      <html:messages id="message" message="true" property="error.invalidProtocolNo" bundle="proposal">                
                           <li><bean:write name = "message"/></li>
                      </html:messages>   
                      <html:messages id="message" message="true" property="approvalRequired" bundle="proposal">                
                           <li><bean:write name = "message"/></li>
                      </html:messages>      
                      <html:messages id="message" message="true" property="protocolNumberRequired" bundle="proposal">                
                           <li><bean:write name = "message"/></li>
                      </html:messages>  
                      <html:messages id="message" message="true" property="duplicateProtocol" bundle="proposal">                
                           <li><bean:write name = "message"/></li>
                      </html:messages>                                              
                 </logic:messagesPresent>
                  
                    <%
                        if(request.getAttribute("isValid") != null) {
                            String isValid = request.getAttribute("isValid").toString();
                            if(!isValid.equals("true")) {
                               
                    %>
                       <li>  Please fill the following details for the proposal...</li>
                       <li> <%=isValid%>.</li>
                    <%
                         }
                        }

                        if(request.getAttribute("streamProtocol") != null) {
                            if(request.getAttribute("streamProtocol").toString().equals("true")) {
                                %>
                                New Protocol is created successfully with the current Proposal details...
                       <%
                            }else {
                                %>
                                    Protocol creation failed because of some errors...
                                <%
                                }
                        }
                   %>
            </font>
          </td>
      </tr> 
      
      <!--  Special Review - Start  -->
      <tr>
          <td >
              <div id='showAddLink'> 
                  <%String divlink = "javascript:showHide(2)";%>
                  <html:link href="<%=divlink%>">
                      <u><bean:message key="proposalSpecialReview.AddSplReview" bundle="proposal"/></u>
                  </html:link>
              </div>
              
          </td>
      </tr>
      <tr>
        <td  align="left" valign="top" class='core'>
        <div id='showAddModifyPanel' class='table'>  
                <table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0" class="tabtable">
                    <%if(!modeValue){%>
                   
                    <%}%> 
                    

                    <tr>
                        <td class="copy">
                            <div id="helpText" class='helptext'>&nbsp;&nbsp;            
                                <bean:message bundle="proposal" key="helpTextProposal.SpecialReview"/>  
                            </div>           
                            <font color="red">*</font>Indicates Required Fields
                        </td>
                    </tr>
                    
                    <tr> 
                        <td width="70%" valign="top" class="copy" align="left">
                            <table width="100%"  border="0" cellpadding="0" cellspacing='0'> 
                                <tr>
                                    <td>
                                        <table  width='100%' border='0'>
                                            <tr>
                                                <td nowrap class="copybold"  width="7%" align="left" ><font color="red">*</font><bean:message key="specialReviewLabel.SpecialReview"/> :</td>
                                                <td width='30%' align=left class='copy'>&nbsp;
                                                    <html:select property="specialReviewCode" styleClass="cltextbox-medium" disabled="<%=modeValue%>" onchange="clearFields(this)" onkeyup="clearFields(this)">
                                                        <html:option value="" ><bean:message key="specialReviewLabel.pleaseSelect" bundle="proposal"/></html:option>
                                                        <html:options collection="splReview" property="code" labelProperty="description"/>
                                                    </html:select>
                                                </td>
                                                <td>
                                                    <input type="button"  id="mybtn" value="Start Protocol"  class="clsavebutton" style="visibility:hidden;" onclick="createNewProtocol();"/>
                                                </td>
                                                <%--<td width='3%'></td>--%>
                                                <%--<td></td>--%>
                                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                <td nowrap class="copybold"  width="7%" align="right">&nbsp;&nbsp;<font color="red">*</font><bean:message key="specialReviewLabel.Approval"/>:</td>
                                                <td align=left valign=bottom class='copy'>
                                                    <div id='divAppCode1' style='display: block;'>
                                                        &nbsp;
                                                        <html:select property="approvalCode" style="width:230px;" styleClass="cltextbox-medium" onchange="dataChanged()"  disabled="<%=modeValue%>" > 
                                                            <html:option value=""><bean:message key="specialReviewLabel.pleaseSelect"/></html:option>
                                                            <html:options collection="approval" property="code" labelProperty="description" />
                                                        </html:select>
                                                        
                                                    </div>
                                                    <div id='divAppCode2' style='display: none;'>
                                                        &nbsp;
                                                        <html:text property="tempApprovalText" styleClass="cltextbox-nonEditcolor" style="width: 220px;" disabled="<%=modeValue%>" readonly="true" />
                                                    </div>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                                
                                <tr>
                                    <td>
                                        
                                        <table  width='100%' border='0' >
                                            <tr>
                                                <td nowrap class="copybold" width="20%" align="left">
                                                    <div id='divMandatory1' style='display: none;'>
                                                        <font color="red">*</font>
                                                        <bean:message key="specialReviewLabel.ProtocolNo" bundle="proposal"/>:
                                                    </div>
                                                    <div id='divMandatory2' style='display: block;'>
                                                        <font color="red">&nbsp;</font>
                                                        <bean:message key="specialReviewLabel.ProtocolNo" bundle="proposal"/>:
                                                    </div>                        
                                                </td>
                                                <td class="copy" align="left" width='10%' nowrap>
                                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                    <html:text property="spRevProtocolNumber" styleClass="textbox" disabled="<%=modeValue%>" onchange="dataChanged()" maxlength="20" />
                                                    </td>  
                                                <!--Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start -->
                                                <!--If value of enableProtocolToDevPropLink is 1 and typecode is also 1 then it allows for IRB search -->
                                                <!--If value of enableIacucProtocolToDevPropLink is 1 and typecode is also 2 then it allows for IACUC search -->
                                                <%if(enableProtocolToDevPropLink.equals("1") || enableIacucProtocolToDevPropLink.equals("1")){%>
                                                <td width="10%" align="left">
                                                    <div id='hide_Search' style='display: none;'>
                                                        <%if(!modeValue){%>
                                                        <html:link href="javascript:openProtocolSearch()">
                                                            <u><bean:message bundle="proposal" key="generalInfoProposal.search"/></u> 
                                                        </html:link>
                                                        <%}%>
                                                    </div>                                                    
                                                </td>   
                                                <%}%>
                                                <!--Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end -->                  
                                                <td nowrap class="copybold"  width="5%" align="left" >&nbsp;&nbsp;<bean:message key="proposalSpecialReview.applDate" bundle="proposal"/>:</td>
                                                <td class="copy"  align=left width='10%' nowrap>
                                                    <div id='applnDate1' style='display: block;'>
                                                        &nbsp;
                                                        <html:text property="applicationDate" styleClass="textbox" disabled="<%=modeValue%>" onchange="dataChanged()"/>                                                                                                        
                                                        <%if(!modeValue){%>
                                                        <a id="hlIRBDate" onclick="displayCalendarWithTopLeft('applicationDate',10,25)" tabindex="32" href="javascript:void(0);"
                                                           runat="server"><img id="imgIRBDate" title="" height="16" alt="" onclick='dataChanged()' src="<bean:write name='ctxtPath'/>/coeusliteimages/cal.gif" width="16"
                                                                                border="0" runat="server">
                                                        </a>                              
                                                        <%}%>
                                                    </div>
                                                    <div id='applnDate2' style='display: none;'>
                                                         &nbsp;
                                                        <html:text property="tempApplicationDate" styleClass="cltextbox-nonEditcolor" style="width: 120px;" disabled="<%=modeValue%>" onchange="dataChanged()" readonly="true"/>                                                                            
                                                    </div>
                                                </td>
                                                <td nowrap class="copybold"  width="5%" align="left">&nbsp;&nbsp;<bean:message key="proposalSpecialReview.apprvDate" bundle="proposal"/>:</td>
                                                <td valign="top" nowrap class="copy" width="10%" align=left>                          
                                                    <div id='apprvDate1' style='display: block;'>                            
                                                        <html:text property="approvalDate" styleClass="textbox" disabled="<%=modeValue%>" onchange="dataChanged()"/>                                                        
                                                        <%if(!modeValue){%>                                      
                                                        <a id="hlIRBDate" onclick="displayCalendarWithTopLeft('approvalDate',10,-135)" tabindex="32" href="javascript:void(0);"
                                                           runat="server"><img id="imgIRBDate" title="" height="16" alt="" onclick='dataChanged()' src="<bean:write name='ctxtPath'/>/coeusliteimages/cal.gif" width="16"
                                                                                border="0" runat="server">
                                                        </a> 
                                                        <%}%>
                                                    </div>
                                                    <div id='apprvDate2' style='display: none;'>               
                                                        <html:text property="tempApprovalDate" styleClass="cltextbox-nonEditcolor" style="width: 120px;" disabled="<%=modeValue%>" onchange="dataChanged()" readonly="true"/>                            
                                                    </div>
                                                </td>
                                                <td width='40%'>
                                                    
                                                </td>
                                            </tr>
                                        </table>
                                        
                                    </td>
                                </tr>
                                
                                <tr>
                                    <td>                                
                                        <table border='0'>
                                            <tr>
                                                <td nowrap class="copybold"  width="7%"align="left" valign="top">&nbsp;&nbsp;<bean:message key="specialReviewLabel.Comments" bundle="proposal"/>: </td>
                                                <td class="copy">
                                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                    <html:textarea property="comments" styleClass="copy" cols="119" rows="3" onchange="dataChanged()" disabled="<%=modeValue%>"/>
                                                </td>
                                            </tr>
                                        </table>
                                        
                                    </td>
                                </tr>
                                
                            </table>
                        </td>
                    </tr>
                    <tr class='table'>
                        <td class='copy' nowrap colspan="6" >
                            <html:button property="Save" value="Save" styleClass="clsavebutton" onclick="saveData();"/>
                            <html:button property="Cancel" value="Cancel" styleClass="clsavebutton" onclick="clearFormData();"/>
                            
                        </td>
                    </tr>                            
                           
                    </table>
                    </div>
                </td>
            </tr>
                   <%if(!modeValue){%>
                   
                    

                    
                    
                   <%}%>
                <!--</td>
                </tr>   
                </table>
            </td>
            </tr>-->
      <% } %>
      <!--  Special Review - End  -->
       

      <!--  List of Special Review - Start -->  
      <tr>
       <td  align="left" valign="top" class='core'>
        <table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0" class="tabtable">
        
          <tr>
            <td colspan="4" align="left" valign="top">
              <table width="100%" height="20%"  border="0" cellpadding="0" cellspacing="0" class="tableheader">
                <tr>
                  <td> <bean:message key="specialReviewLabel.ListofSpReview" bundle="proposal"/>  </td>
                </tr>
              </table>
            </td>
          </tr> 
          
          <tr align="center">
              <td colspan="3"><br>
                 <DIV STYLE="overflow: auto; width: 775px; height: 330px; padding:0px; margin: 0px">
                 <table width="98%"  border="0" cellpadding="2" class="tabtable">
                   <tr align="left" >
                        <td width="20%" align="left" class="theader"><bean:message key="specialReviewLabel.SpecialReview" bundle="proposal"/></td>
                        <td width="20%"  class="theader"><bean:message key="specialReviewLabel.Approval" bundle="proposal"/></td>
                        <td width="13%"  class="theader"><bean:message key="specialReviewLabel.ProtocolNo" bundle="proposal"/></td>
                        <td width="15%"  class="theader"><bean:message key="proposalSpecialReview.applDate" bundle="proposal"/></td>
                        <td width="15%"  class="theader"><bean:message key="proposalSpecialReview.apprvDate" bundle="proposal"/></td>
                        <td width="10%"  class="theader"><bean:message key="specialReviewLabel.Comments" bundle="proposal"/></td>
                      <%if(!modeValue){%>
                        <td width="10%"  class="theader"></td>
                      <%}%>
                      <td width="10%"  class="theader"></td>
                   </tr> 
                   <%  String strBgColor = "#DCE5F1";
                       int count = 0;
                    %>                   
                   <logic:present name="pdReviewList">             
                         <logic:iterate id="pdreview" indexId="index" name="pdReviewList" type = "org.apache.struts.validator.DynaValidatorForm">
                         <% String protoNum = "";
                            if(pdreview.get("spRevProtocolNumber") == null){
                               protoNum = "";
                            }else{
                               protoNum = pdreview.get("spRevProtocolNumber").toString();  
                            }
                          %>
                          <% 
                             if (count%2 == 0){ 
                                strBgColor = "#D6DCE5"; 
                             }else {
                                strBgColor="#DCE5F1"; 
                             }
                             String editLink = "javascript:edit_data('"+pdreview.get("specialReviewNumber") +"','" +pdreview.get("pdSpTimestamp") +"')";
                           %>                   
                           <tr align="left" nowrap bgcolor="<%=strBgColor%>" onmouseover="className='TableItemOn'"  onmouseout="className='TableItemOff'">
                               <td class="copy">
                                   <%if(!modeValue){%>
                                   <html:link href="<%=editLink%>">
                                       <%=pdreview.get("specialReviewDescription").toString()%>
                                   </html:link>
                                   <%}else{%>
                                   <%=pdreview.get("specialReviewDescription").toString()%>
                                   <%}%>
                                   
                               </td>
                               <td class="copy">
                                  <%=pdreview.get("approvalDescription").toString()%>
                               </td>
                               <td class="copy">
                                   <%--Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start--%>
                                   <%-- <a href="<%=path%>/getProtocolData.do?SEARCH_ACTION=SEARCH_ACTION&protocolNumber=<%=protoNum%>&PAGE=G&sequenceNumber=1&notFromIsReviewer=true&Menu_Id=002" > <%=protoNum%> </a>--%>
                                   <%
                                   if(pdreview.get("specialReviewCode").equals("1") && (enableProtocolToDevPropLink.equals("1") || enableIacucProtocolToDevPropLink.equals("1"))) {%>
                                   <a href="<%=path%>/getProtocolData.do?SEARCH_ACTION=SEARCH_ACTION&protocolNumber=<%=protoNum%>&PAGE=G&sequenceNumber=1&notFromIsReviewer=true&Menu_Id=002" > <%=protoNum%> </a>
                                   <%}else if(pdreview.get("specialReviewCode").equals("2") && (enableProtocolToDevPropLink.equals("1") || enableIacucProtocolToDevPropLink.equals("1"))){%>
                                   <a href="<%=path%>/getIacucData.do?SEARCH_ACTION=SEARCH_ACTION&protocolNumber=<%=protoNum%>&PAGE=G&sequenceNumber=1&notFromIsReviewer=true&Menu_Id=002" > <%=protoNum%> </a> 
                                   <%}else{%>
                                    <%=protoNum%>
                                   <%}%>
                                   <%--Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end--%>
                               </td>
                               <td class="copy">
                               <%String applDate = "";
                                 if(pdreview.get("applicationDate") == null){
                                   applDate = "";
                                 }else{
                                   applDate = (String)pdreview.get("applicationDate");  
                                 }
                                %>
                               <%=applDate%>    
                               </td>
                               <td class="copy">
                               <%String apprvDate = "";
                                 if(pdreview.get("approvalDate") == null){
                                    apprvDate = "";
                                 }else{
                                    apprvDate = (String)pdreview.get("approvalDate");  
                                 }
                                %>
                               <%=apprvDate%>   
                               </td>
                               <td class="copy">
                               <%String comments = "";
                                 if(pdreview.get("comments") == null){
                                    comments = "";
                                 }else{
                                    comments = (String)pdreview.get("comments");
                                 }
                               %>
                                <%
                                    String viewLink = "javascript:view_comments('" +index+"')";
                                %>
                               
                                <html:link href="<%=viewLink%>">
                                    <bean:message key="label.View"/>
                                </html:link>
                                </td>
                               <td class="copy">
                                  <%if(!modeValue){%>
                                   <a href="javascript:delete_data('D','<%=pdreview.get("specialReviewNumber")%>','<%=pdreview.get("pdSpTimestamp")%>','<%=pdreview.get("approvalCode")%>','<%=pdreview.get("specialReviewCode")%>','<%=pdreview.get("spRevProtocolNumber")%>')">Remove</a> 
                                  <%}%>
                               </td>
                               <td class="copy"></td>
                           </tr>
                           <% count++;%>                    
                         </logic:iterate>
                   </logic:present>                  
                 </table>
                 </DIV>
              </td>
          </tr>

          <tr> <td> &nbsp; </td>
          </tr>
        </table>
       </td>
      </tr>   
      <!--  List of Special Review -End -->  
      
      <tr>
        <td height='10'>&nbsp; </td>
      </tr>   
      
    </table>
    <!-- New Template for cwViewFinEntity - End  -->
        
            <html:hidden property="acType" value=""/>
            <html:hidden property="proposalNumber"/>
            <html:hidden property="specialReviewNumber"/>
            <html:hidden property="pdSpTimestamp"/>  
            <html:hidden property="tempApprovalCode"/>   
            <html:hidden property="approvalCode"/>   
        </html:form>
    <script>
            /*var applDate = new calendar2(document.forms['propDevSpecialReview'].elements['applicationDate']);
            applDate.year_scroll = true;
            applDate.time_comp = false;
            
            var apprDate = new calendar2(document.forms['propDevSpecialReview'].elements['approvalDate']);
            apprDate.year_scroll = true;
            apprDate.time_comp = false;    */    
    </script>
    <iframe width=174 height=189 name="gToday:normal:agenda.js" id="gToday:normal:agenda.js"
            src="ipopeng.htm" scrolling="no" frameborder="0" style="visibility:visible; z-index:999; position:absolute; left:-500px; top:0px;">
    </iframe>
  

    
    <script>
          var help = '<bean:message bundle="proposal" key="helpTextProposal.SpecialReview"/>';
          help = trim(help);
          if(help.length == 0) {
            document.getElementById('helpText').style.display = 'none';
          }
          function trim(s) {
                return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
          }  
    </script>    
    
    
    
    
<script>
      DATA_CHANGED = 'false';
      if(errValue) {
            DATA_CHANGED = 'true';
      }
      LINK = "<%=request.getContextPath()%>/propSpecialReview.do?";
      FORM_LINK = document.propDevSpecialReview;
      document.propDevSpecialReview.acType.value = "I"; 
      if(document.propDevSpecialReview.pdSpTimestamp.value != ''){
        document.propDevSpecialReview.acType.value = "U";    
      }
      <%
      String acType = (String)request.getAttribute("acType");
      if(acType != null &&  "U".equals(acType)){%>
        document.propDevSpecialReview.acType.value = "U";    
      <%}
      %>
      
      PAGE_NAME = "<bean:message key="specialReviewLabel.SpecialReviews" bundle="proposal"/>";
      function dataChanged(){
        DATA_CHANGED = 'true';
      }
      linkForward(errValue);
</script> 
<script>
    <%if(enableProtocolToDevPropLink.equals("1")){%>
        var typeCode = document.propDevSpecialReview.specialReviewCode.value;
        if(typeCode == '1'){
            document.getElementById('hide_Search').style.display = 'block';
            document.getElementById('applnDate1').style.display = 'none';
            document.getElementById('applnDate2').style.display = 'block';
            document.getElementById('apprvDate1').style.display = 'none';
            document.getElementById('apprvDate2').style.display = 'block';  
            document.getElementById('divAppCode1').style.display = 'none'; 
            document.getElementById('divAppCode2').style.display = 'block';     
            document.getElementById('divMandatory1').style.display = 'block';
            document.getElementById('divMandatory2').style.display = 'none';                                
        }
    <%}%>
    <%if(enableIacucProtocolToDevPropLink.equals("1")){%>
        var typeCode = document.propDevSpecialReview.specialReviewCode.value;
        if(typeCode == '2'){
            document.getElementById('hide_Search').style.display = 'block';
            document.getElementById('applnDate1').style.display = 'none';
            document.getElementById('applnDate2').style.display = 'block';
            document.getElementById('apprvDate1').style.display = 'none';
            document.getElementById('apprvDate2').style.display = 'block';  
            document.getElementById('divAppCode1').style.display = 'none'; 
            document.getElementById('divAppCode2').style.display = 'block';     
            document.getElementById('divMandatory1').style.display = 'block';
            document.getElementById('divMandatory2').style.display = 'none';                                
        }
    <%}%>
    function createNewProtocol() {
           document.propDevSpecialReview.action = "<%=request.getContextPath()%>/newStubProtocol.do?proposalNumber=<%=propNumber%>"
            document.propDevSpecialReview.submit();
    }

     if(typeCode == '1'  ) {
     <%if(enableProtocolToDevPropLink.equals("1")){%>
     document.getElementById("mybtn").style.visibility = "visible";
     <%}%>
     } 

     else
     { document.getElementById("mybtn").style.visibility ="hidden"; }
     <%if(!modeValue && !errorOccured){%>
        if(document.propDevSpecialReview.acType.value == 'U'){
            javascript:showHide(2);
        }else{
            document.propDevSpecialReview.acType.value = "I";    
            javascript:showHide(1);
        }
     <%}%>
     <%if(errorOccured){%>
         javascript:showHide(2);
     <%}%>
    </script>

</body>
   
</html:html>
