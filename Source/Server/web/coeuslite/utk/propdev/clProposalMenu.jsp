<%@ page contentType="text/html;charset=UTF-8" language="java" 
import="java.util.Vector"
%>
<%@page import="edu.mit.coeuslite.utils.CoeusLiteConstants,
                edu.mit.coeuslite.utils.bean.MenuBean,java.util.List"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"	prefix="coeusUtils" %>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>

<html:html locale="true">

<% String proposalNumber = (String)session.getAttribute("proposalNumber"+session.getId());
   String group="4";
   String mode = (String)session.getAttribute("mode") ;
    boolean readOnly = false;
    if(mode != null && mode.equals("display")){
        readOnly = true;
    }
    String link = "";
    int propYnqCount = 0;
    if(session.getAttribute("propYnqCount") != null) {
        propYnqCount = (Integer) session.getAttribute("propYnqCount");
    }
    
    boolean isPHSHSCTFormIncluded = false; 
    if(session.getAttribute("isPHSHumanSubjectCTFormIncluded"+session.getId()) != null){
        isPHSHSCTFormIncluded = ((Boolean)session.getAttribute("isPHSHumanSubjectCTFormIncluded"+session.getId())).booleanValue();
    }              
    
    // Added for COEUSDEV-1171 : Recall Link becomes Available in Lite with Validation Checks - Start
    boolean hasApproverRecallRight = false;
    boolean hasRecallRight = false;
    if(session.getAttribute("HAS_RECALL_RIGHTS")!=null){
    hasRecallRight = ((Boolean)session.getAttribute("HAS_RECALL_RIGHTS")).booleanValue();
    } 
    if(session.getAttribute("HAS_APPROVER_RECALL_RIGHTS")!=null){
    hasApproverRecallRight = ((Boolean)session.getAttribute("HAS_APPROVER_RECALL_RIGHTS")).booleanValue();
    }
    // Added for COEUSDEV-1171 : Recall Link becomes Available in Lite with Validation Checks - End
 
%>
    <html:base/>

    <head>
        <title>Untitled</title>     
    </head>
    <script>          
             function open_budget_summary(link){ 
                //Commented/Added for case#2776  - Allow concurrent Prop dev access in Lite - start
                //var value = "<%=request.getAttribute("createBudget")%>" ;
               var value = "<%=session.getAttribute("createBudget"+session.getId())%>"; 
               //Commented/Added for case#2776  - Allow concurrent Prop dev access in Lite - end
               var rights = "<%=readOnly%>";
                if(value == "createBudget" && rights == "false"){
                   var msg = '<bean:message bundle="proposal" key="generalInfoProposal.createNewBudget"/>';
                   if (confirm(msg)==true){                     
                     window.location= link;                    
                    }
                  }else{
                   window.location= link;
                  }
               }
 
             function checkProposal(value)
             {
                var proposalNumber = '<%=proposalNumber%>';
                if(proposalNumber == 'null' || proposalNumber == ''){
                    alert("<bean:message bundle="proposal" key="generalInfoProposal.saveProposalGeneral"/>");
                    return false;
                }
                CLICKED_LINK = value;
                return validateSavedInfo();
             }
                
             function showAlert(){
             alert("<bean:message bundle="proposal" key="generalInfoProposal.notImplemented"/>");
             }
             
           function deleteProposal(){
            
                var confirmMsg ;
            
                    confirmMsg = "Are you sure you want to delete the Proposal?";
                    if (confirm(confirmMsg)==true){
                        path =  "<%=request.getContextPath()%>/deleteProposal.do";
                        window.location= path;
                       }
            }
             
        </script>  
    <body>
       
            
        
            <table width="100%" align="left" border="0" cellpadding="0" cellspacing="5" class="table">

              <tr class="rowLine"><%int index=1;%>
                
                   <td align="left" valign="top">
                          <table width="100%"  border="0" align="center" cellpadding="2" cellspacing="0" class="table">
                              <%
                                int status = 0;
                                if(session.getAttribute("proposalStatus") != null) {
                                    status = Integer.parseInt(session.getAttribute("proposalStatus").toString());  
                                }
                              %>
                              <jsp:useBean id="proposalMenuItemsVector" scope="session" class="java.util.Vector" /> 
                              <logic:iterate name="proposalMenuItemsVector" id="proposalMenuBean" type="edu.mit.coeuslite.utils.bean.MenuBean">
                              <%
                              String linkName = proposalMenuBean.getMenuName();
                              String linkValue = proposalMenuBean.getMenuLink();
                              String groupName = proposalMenuBean.getGroup();
                              String menuId = proposalMenuBean.getMenuId();
                              boolean isMenuSaved = proposalMenuBean.isDataSaved();
                              // Added for COEUSDEV-1171 : Recall Link becomes Available in Lite with Validation Checks - Start
                               if(menuId.equals("P010") && (hasRecallRight && hasApproverRecallRight)){
                                  proposalMenuBean.setVisible(true);
                              }else if(menuId.equals("P010") && (!hasRecallRight || !hasApproverRecallRight)){
                                  proposalMenuBean.setVisible(false);
                              }
                              // Added for COEUSDEV-1171 : Recall Link becomes Available in Lite with Validation Checks - End
                              if(request.getAttribute("enableSelfCertication")==null && linkValue.equals("/rightPersonCertify")){
                                 proposalMenuBean.setVisible(false); 
                              }
                               %>
                              <%if(menuId.equals("P021")) {%>
                              <tr class = "menuHeaderName">
                                  <td colspan='3' ><%=linkName%></td>
                              </tr>
                              <%} else if(linkValue!=null && linkValue.equals("APPROVALACTON")){%>
                                </table>
                                <br>
                                <table width="200"  border="0" align="center" cellpadding="2" cellspacing="0" class="table"> 
                                 <tr class = "menuHeaderName">
                                  <td colspan='3' ><%=linkName%></td>
                                </tr>
                               <%}else {%>
                              <%if(proposalMenuBean.isVisible()){%>
                              
                              <%if(index==Integer.parseInt(groupName)){%>
                              <!--<tr>
                                  <td>&nbsp;</td>
                              </tr>-->
                          </table>
                          <br>
                          <table width="200"  border="0" align="center" cellpadding="2" cellspacing="0" class="table">
                          <%index++;}%>                               
                                       <!--<tr class="coeusMenuSeparator">
                                       <td colspan='3' height='2'>
                                       </td>
                                       </tr>-->
                               <tr> 
                                     <td width="16%"  height='16' align="left" valign="top" class = "coeusMenu">
                                         <%--Added for the CASE # COEUSQA_1394 - coeus lite proposal development application does not put a check mark next to Grants.Gov menu once Start --%>
                                         <%if("P013".equalsIgnoreCase(menuId)){%>
                                         <logic:equal name="grantsGovExist" scope="session" value="1">
                                             <img src="<bean:write name='ctxtPath'/>/coeusliteimages/img_saved.gif" width="19" height="19">
                                         </logic:equal>
                                         <%}else{%>
                                         <%-- Added for the CASE # COEUSQA_1394 - coeus lite proposal development application does not put a check mark next to Grants.Gov menu once End --%>        
                                         <!-- COEUSQA:3446 - Disable YNQ icon & menu-path in Proposal Development if all Proposal YNQ's are marked Inactive - Start -->
                                         <%if("P020".equalsIgnoreCase(menuId)){%>
                                            <%if(propYnqCount > 0) {%> 
                                                <logic:equal name="proposalMenuBean" property="dataSaved" value="true">
                                                  <img src="<bean:write name='ctxtPath'/>/coeusliteimages/img_saved.gif" width="19" height="19">
                                                </logic:equal>
                                            <%}%>
                                         <%} else {%>
                                            <logic:equal name="proposalMenuBean" property="dataSaved" value="true">
                                                 <img src="<bean:write name='ctxtPath'/>/coeusliteimages/img_saved.gif" width="19" height="19">
                                            </logic:equal>
                                        <%}%>
                                        <!-- COEUSQA:3446 - End -->   
                                        <%-- Added for the CASE # COEUSQA_1394 - coeus lite proposal development application does not put a check mark next to Grants.Gov menu once Start --%>        
                                        <%}%>
                                        <%-- Added for the CASE # COEUSQA_1394 - coeus lite proposal development application does not put a check mark next to Grants.Gov menu once End --%>        
                                    </td>
                                    <td width='80%'  align="left" valign="middle" class = "coeusMenu" > 
                                          <bean:define id="name" name="proposalMenuBean" property="menuName"/> 
                                          <bean:define id="link_Value" name="proposalMenuBean" property="menuLink"/> 
                                          <bean:define id="selected" name="proposalMenuBean" property="selected"/> 
                                          <bean:define id="fieldName" name="proposalMenuBean" property="fieldName"/>
                                          <bean:define id="isMenusaved" name="proposalMenuBean" property="dataSaved"/>
                                       <% if(linkValue.equals("/getBudget.do")){
                                            link = "javascript:return checkProposal('"+request.getContextPath()+"/getBudget.do?proposalNumber="+proposalNumber+"&isEditable="+mode+"&Menu_Id=003')";%>
                                            <a href="javascript:open_budget_summary('<%=request.getContextPath() %>/getBudget.do?proposalNumber=<%=proposalNumber%>&isEditable=<%=mode%>&Menu_Id=003')" onclick="<%=link%>"> <%=linkName%> </a>  
                                            
                                           <% }else if((linkValue.equals("/getGeneralInfo.do"))){
                                                    link = "javascript:return checkProposal('"+request.getContextPath()+"/getGeneralInfo.do?proposalNumber="+proposalNumber+"&isEditable="+mode+"&page=general')";%>
                                                        <% if(((Boolean)selected).booleanValue()) { %>
                                                            <font color="#6D0202"><b>
                                                            <%=linkName%>
                                                             </b> </font>
                                                           <%} else {%>
                                                           <a href="<%=request.getContextPath() %>/getGeneralInfo.do?proposalNumber=<%=proposalNumber%>&isEditable=<%=mode%>&page=general" onclick="<%=link%>"> 
                                                           <%=linkName%>
                                                           </a> 
                                                        <%}%>
                                           <%}else if(linkValue.equals("/empty")){%>                                          
                                            <a href="javascript:showAlert()"> <%=linkName%>
                                            </a>
                                            <%}else if((linkValue.equals("/displayProposal.do"))){

                                                link = "return checkProposal('"+request.getContextPath()+"/displayProposal.do?proposalNo="+proposalNumber+"')";
                                                %>
                                                <a href="<%=request.getContextPath() %>/displayProposal.do?proposalNo=<%=proposalNumber%>" onclick="<%=link%>">
                                                   <%=linkName%>
                                                </a>

                                           <%}else if((linkValue.equals("/displayGrantsGov.do"))){
                                                link = "return checkProposal('"+request.getContextPath()+"/grantsGovAction.do?proposalNumber="+proposalNumber+"&isEditable="+mode+"')";%>
                                                <a href="<%=request.getContextPath() %>/grantsGovAction.do?proposalNumber=<%=proposalNumber%>&isEditable=<%=mode%>" onclick="<%=link%>"> 
                                                   <%=linkName%>
                                                </a>
                                           <%}else if((linkValue.equals("/proposalPrint.do"))){
                                                link = "return checkProposal('"+request.getContextPath()+"/proposalPrintAction.do?proposalNumber="+proposalNumber+"&isEditable="+mode+"')";%>
                                                <a href="<%=request.getContextPath() %>/proposalPrintAction.do?proposalNumber=<%=proposalNumber%>&isEditable=<%=mode%>" onclick="<%=link%>"> 
                                                   <%=linkName%>
                                                </a> 
                                           <!-- COEUSQA:3446 - Disable YNQ icon & menu-path in Proposal Development if all Proposal YNQ's are marked Inactive - Start -->     
                                           <%}else if((linkValue.equals("/getYNQDetails.do"))){ %>    
                                             
                                                 <%if(propYnqCount > 0) {%>                                                  
                                                    <% link = "return checkProposal('"+request.getContextPath()+linkValue+"')";%>
                                                     <html:link page ="<%=linkValue%>" styleClass="copy" onclick="<%=link%>">
                                                    <%=linkName%></html:link>
                                                 <%}else{%>
                                                     <%=linkName%>
                                                 <%}%>   
                                           <!-- COEUSQA:3446 - End -->
                                           <%}else if("P222".equals(menuId)){ %>   
                                                 <%if(isPHSHSCTFormIncluded) {%> 
                                                     <html:link page ="<%=linkValue%>" styleClass="copy" onclick="<%=link%>"><%=linkName%></html:link>
                                                 <%}else{%>
                                                     <%=linkName%>
                                                 <%}%>
                                            <%}else if((linkValue.equals("/displayProposal"))){ 
                                                      link = "return checkProposal('"+request.getContextPath()+"/displayProposal.do?proposalNo="+proposalNumber+"')";%>
                                                      <a href="<%=request.getContextPath() %>/displayProposal.do?proposalNo=<%=proposalNumber%>"> 
                                                      <%=linkName%> 
                                                      </a>
                                           <%}else if((linkValue.equals("/rightPersonCertify"))){
                                                     link = "return checkProposal('"+request.getContextPath()+"/rightPersonCertify.do?proposalNumber="+proposalNumber+"')";%>
                                                     <a href="<%=request.getContextPath() %>/rightPersonCertify.do?proposalNumber=<%=proposalNumber%>"> 
                                                     <%=linkName%>
                                                     </a>   
                                           <%}else{%>
                                        <% if(((Boolean)selected).booleanValue()) { %>
                                                    <font color="#6D0202"><b>
                                                    <%=linkName%>
                                                     </b> </font>
                                                    <%}else {
                                                    if(menuId.equals("P026")){%>
                                                    <a href="javascript:deleteProposal();">
                                                        <%=linkName%>
                                                    </a>
                                                    <%}else{%>
                                                    <% link = "return checkProposal('"+request.getContextPath()+linkValue+"')";%>
                                                    <html:link page ="<%=linkValue%>" styleClass="copy" onclick="<%=link%>">
                                                    <%=linkName%></html:link>
                                                    <%}}%>
                                         <%}%>
                                        </a>
                                    </td>
                                    <td width='4%' align=right  class ="selectedMenuIndicator" >
                                        <logic:equal name="proposalMenuBean" property="selected" value="true">
                                                <bean:message key="menu.selected"/> 
                                        </logic:equal>
                                    </td>
                                  
                              </tr> 
                              <%}
                            }%>
                           </logic:iterate> 
                              <!--<tr><td>&nbsp;</td></tr>-->
                    </table>
                </td>
              </tr>
        </table>
    </body>
</html:html>