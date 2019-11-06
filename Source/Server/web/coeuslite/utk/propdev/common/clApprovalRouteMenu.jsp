<%@ page contentType="text/html;charset=UTF-8" language="java" 
import="java.util.Vector"
%>
<%@page import="edu.mit.coeuslite.utils.CoeusLiteConstants,edu.utk.coeuslite.propdev.bean.EPSProposalHeaderBean,edu.mit.coeuslite.utils.bean.MenuBean"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils" %>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<jsp:useBean  id="epsProposalHeaderBean"  scope="session" class="edu.utk.coeuslite.propdev.bean.EPSProposalHeaderBean"/>
<html:html locale="true">

<% EPSProposalHeaderBean proposalHeaderBean=(EPSProposalHeaderBean)session.getAttribute("epsProposalHeaderBean");
   String proposalNumber = (String)session.getAttribute("proposalNumber"+session.getId());
   String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
   String sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId());
   String group="4";
   String mode = (String)session.getAttribute("mode"+session.getId()) ;
   String statusCode=(String)request.getAttribute("statusCode") ;
    //COEUSQA-1433 - Allow Recall from Routing - Start
    boolean hasApproverRecallRight = false;
    boolean hasRecallRight = false;
    if(session.getAttribute("HAS_RECALL_RIGHTS")!=null){
       hasRecallRight = ((Boolean)session.getAttribute("HAS_RECALL_RIGHTS")).booleanValue();
    } 
    if(session.getAttribute("HAS_APPROVER_RECALL_RIGHTS")!=null){
       hasApproverRecallRight = ((Boolean)session.getAttribute("HAS_APPROVER_RECALL_RIGHTS")).booleanValue();
    }
    if(proposalHeaderBean.getProposalStatusCode().equals("6")){
     hasRecallRight = false;
    }
    //COEUSQA-1433 - Allow Recall from Routing - End
    boolean readOnly = false;
    if(mode != null && mode.equals("display")){
        readOnly = true;
    }
%>
    <html:base/>

    <head>
        <title>Untitled</title>     
    </head>
    <script>          
        function open_budget_summary(link){ 
        var value = "<%=request.getAttribute("createBudget")%>" ;
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
 
        function checkProposal()
        {
        if(<%=proposalNumber%>==null){
        alert("<bean:message bundle="proposal" key="generalInfoProposal.saveProposalGeneral"/>");
        return false;
        }
        }
                
        function showAlert(){
        alert("<bean:message bundle="proposal" key="generalInfoProposal.notImplemented"/>");
        }
    </script>  
    <body> 
        <!-- JM
        <table width="100%" height="100%"  border="0" cellpadding="0" cellspacing="0" class="table">
         -->
        <table width="200" height="100%"  border="0" cellpadding="0" cellspacing="0" class="table">        
            
        <tr>
        <td align="left" valign="top">
            <table width="93%"  border="0" align="center" cellpadding="0" cellspacing="0">
                <jsp:useBean id="ApprovalRouteMenuItemsVector" scope="session" class="java.util.Vector" /> 
                          
                <logic:iterate name="ApprovalRouteMenuItemsVector" id="proposalMenuBean" type="edu.mit.coeuslite.utils.bean.MenuBean">
                               <%
                                          String linkName = proposalMenuBean.getMenuName();
                                          String linkValue = proposalMenuBean.getMenuLink();
                                          String groupName = proposalMenuBean.getGroup();
                                          String menuId = proposalMenuBean.getMenuId();
                                          String previousRouting=(String) session.getAttribute("showPreviousRouting");
                                          int approvalSequence=(Integer)session.getAttribute("approvalSequenceNumber");
                                          String statusCodeRejected=(String)request.getAttribute("statusCode");
                                          String currentSubmissionNumber="";
                                          String originalSubmissionNumber="";

                                         if(session.getAttribute("currentSubmissionNumber")!=null && session.getAttribute("originalSubmissionNumber")!=null){
                                         currentSubmissionNumber=session.getAttribute("currentSubmissionNumber").toString();
                                         originalSubmissionNumber=session.getAttribute("originalSubmissionNumber").toString();
                                         }
                                          
                                          if(linkValue.equals("/displayProposal.do")) {
                                              linkValue = proposalMenuBean.getMenuLink()+"?proposalNo="+proposalNumber;
                                          } else if(linkValue.equals("/getGeneralInfo.do")) {
                                              linkValue = proposalMenuBean.getMenuLink()+"?proposalNumber="+proposalNumber;
                                          } else if(linkValue.equals("/displayProtocol.do")) {
                                              linkValue = proposalMenuBean.getMenuLink()+"?protocolNumber="+protocolNumber;
                                          } else if(menuId.equals("P007")) {
                                              linkValue = proposalMenuBean.getMenuLink()+"&protocolNumber="+protocolNumber
                                                      +"&sequenceNumber="+sequenceNumber;
                                          }
                                          // COEUSQA-1433 - Allow Recall from Routing - Start                                          
                                          if(menuId.equals("P010") && (hasRecallRight && hasApproverRecallRight)){
                                             proposalMenuBean.setVisible(true);
                                          }else if(menuId.equals("P010") && (!hasRecallRight || !hasApproverRecallRight)){
                                              proposalMenuBean.setVisible(false);
                                          }  
                                          else if(menuId.equals("P015") && (((approvalSequence<=2) && !(statusCode.equals("3")) ) || previousRouting==null))
                                           {
                                               proposalMenuBean.setVisible(false);
                                           }
                                          else if(((menuId.equals("P005"))||(menuId.equals("P004"))) && (!(currentSubmissionNumber.equals(originalSubmissionNumber))) && (previousRouting==null && ((approvalSequence>2) || statusCodeRejected.equals("3") || statusCodeRejected.equals("211") || statusCodeRejected.equals("214")))){
                                          proposalMenuBean.setVisible(false);
                                          }
                                          //COEUSQA-1433 - Allow Recall from Routing - End
                                          if(group.equals(groupName)) {
                                              group="5";
                                       %>
                    <tr><td>&nbsp;
                    </td></tr>
                                       <%}%>
                               <%if(proposalMenuBean.isVisible()){%>         
                    <tr>
                       <%-- <td width="19" align="left" valign="top" ><img src="<bean:write name='ctxtPath'/>/coeusliteimages/img_blank.gif" width="5" height="19"></td>--%>
                        <td width='100%' align="left" valign="middle" class="menu"  STYLE="border: 1px solid #6E97CF;background-color: #D1E5FF;">
                        <html:link page ="<%=linkValue%>" styleClass="copy">                                            
                                                   <%=linkName%>                                         
                        </html:link>
                        </a></td>
                        <%--<td width="18" align="left" valign="top"><img src="<bean:write name='ctxtPath'/>/coeusliteimages/img_menu_rt.gif" width="22" height="20"></td>--%>
                    </tr>
                              <%}%>
                </logic:iterate> 
                <tr><td>&nbsp;</td></tr>                                                       
            </table>
        </td>
        </tr>
         
        </table>
    </body>
</html:html>
