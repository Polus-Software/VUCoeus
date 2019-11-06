<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page import="edu.mit.coeus.utils.DateUtils" %>
<%@ page import="edu.utk.coeuslite.propdev.bean.EPSProposalHeaderBean" %>
<%@ page import="edu.mit.coeuslite.irb.bean.ProtocolHeaderDetailsBean" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
   
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="<%=request.getContextPath()%>/coeuslite/mit/utils/css/coeus_styles.css" rel="stylesheet" type="text/css">
        <title><bean:message bundle="proposal" key="summary.attachments"/></title>
         <script>
         <%--Modified for the case id 2574 begin--%>     
       function view_attachments(index){
           window.open("<%=request.getContextPath()%>/viewApprovalRoutingAttachments.do?&index="+index);
       }    
        <%--Modified for the case id 2574 end--%>      
        </script>
    </head>
    <body>

     <%-- Added for 4262 - Routing attachments not visible in Lite - Start--%>
        <table width='100%' border="0" cellpadding="0"  cellspacing="0" class="table">
          <tr>
                <td align="left" width='15%'  nowrap class='copybold'>
                  <!-- To check ModuleCode(3 - Proposal, 7 - protocol) -->
                  <% String moduleCode = (String)request.getSession().getAttribute("CommentsModuleCode");  
                    if(moduleCode.equals("3")){%>
                        <bean:message bundle="proposal" key="routing.proposalNumber"/>
                    <%}else if(moduleCode.equals("7")){%>
                        <bean:message bundle="proposal" key="routing.protocolNumber"/>
                    <%}else if(moduleCode.equals("9")){%>
                        <bean:message bundle="proposal" key="routing.protocolNumber"/>
                    <%}%>
                </td>
                <%EPSProposalHeaderBean proposalHeaderBean=(EPSProposalHeaderBean)session.getAttribute("epsProposalHeaderBean");
                ProtocolHeaderDetailsBean protocolHeaderBean = (ProtocolHeaderDetailsBean)session.getAttribute("protocolHeaderBean");
                edu.mit.coeuslite.iacuc.bean.ProtocolHeaderDetailsBean iacucHeaderBean = (edu.mit.coeuslite.iacuc.bean.ProtocolHeaderDetailsBean)session.getAttribute("iacucHeaderBean");
                %>
                
                <td class='copy' width='30%' colspan='1' nowrap><%=request.getAttribute("getCommentsModuleItemKey")%></td>
                <td align="left" width='15%'  nowrap class='copybold'>
                        <bean:message bundle="proposal" key="routing.approverName"/>
                    </td>
                 <td class='copy' width='30%' colspan='1' nowrap><%
                 String approverName = "";
                 approverName = request.getParameter("approverName");
                 String approverStatus = request.getParameter("approvalStatus");
                if(approverStatus.equals("B")){
                        if(moduleCode.equals("3")){                                                
                            approverName = proposalHeaderBean.getUpdateUserName();
                        }else if(moduleCode.equals("7")){
                            approverName = protocolHeaderBean.getUpdateUserName();
                        }else if(moduleCode.equals("9")){
                            approverName = iacucHeaderBean.getUpdateUserName();
                        }
               }else{
                         approverName = request.getParameter("approverName");
                 } %>
                                                                                                                
                        <%=approverName%></td> 
          </tr>
          <tr>
                <td align="left" width='15%'  nowrap class='copybold'>
                  <bean:message bundle="proposal" key="routing.leadUnit"/>
                </td>
                <td class='copy' width='30%' colspan='1' nowrap>
                <%if(moduleCode.equals("3")){%>
                    <%=proposalHeaderBean.getLeadUnitNumber()%>&nbsp;:&nbsp; <%=proposalHeaderBean.getLeadUnitName()%>&nbsp;
                 <%}else if(moduleCode.equals("7")){%>
                    <%=protocolHeaderBean.getUnitNumber()%>&nbsp;:&nbsp; <%=protocolHeaderBean.getLeadUnit()%>&nbsp;
                 <%}else if(moduleCode.equals("9")){%>
                    <%=iacucHeaderBean.getUnitNumber()%>&nbsp;:&nbsp; <%=iacucHeaderBean.getLeadUnit()%>&nbsp;
                 <%}%>
                </td>   
                    <td align="left" width='15%'  nowrap class='copybold'>
                        <bean:message bundle="proposal" key="routing.approvalDate"/>&nbsp;&nbsp;
                    </td>
                    
                    <td class='copy' width='30%' colspan='1' nowrap>
                        <%=(String)request.getAttribute("ApprovalDate")%>
                </td>
           </tr>
          </table>
          <%-- Case #:4262 Routing attachments not visible in Lite- End --%>
          
        <table width="100%" class='tabtable'>
            <tr class="theader" >
                <td height="20">
                    <bean:message bundle="proposal" key="summary.attachments"/>
                </td>
                <td align="right" valign="middle">
                    <a href="javaScript:window.close();"><u>Close</u></a>
                </td>                
            </tr>
            </table>
            
            <%-- Added for4262 -Routing attachments not visible in Lite  --%>
            <table border="0" width="99%" cellspacing="0" cellpadding="0" align=center class="tabtable">
                <logic:present name="ApprovalRoutingAttachments" scope="session">
                    <logic:iterate id="routingAttachmentBean" name="ApprovalRoutingAttachments" indexId="index" type="edu.mit.coeus.routing.bean.RoutingAttachmentBean" scope="session">
                        
                        <tr>
                            <td style="border:5px solid #789FD5;"></td>
                        </tr>
                        <tr>
                             <%--Modified for the case id 2574 begin--%>
                           <%-- 
                            <%
                            String fileName = routingAttachmentBean.getFileName();
                            byte[] attachment = routingAttachmentBean.getAttachment();
                            String routingNumber =routingAttachmentBean.getRoutingNumber();
                            int attachmentNumber = routingAttachmentBean.getAttachmentNumber();
                            String viewLinkAttachments = "javascript:view_attachments('"+index+"','"+fileName+"','"+attachment+"','"+attachmentNumber+"','"+routingNumber+"')";%>
                            <td class='copy'>
                            <html:link href="<%=viewLinkAttachments%>">
                                <bean:write property="fileName" name="routingAttachmentBean" />
                            </html:link> 
                            <td>
                            --%>
                           
                            <%
                             String viewLinkAttachments = "javascript:view_attachments('"+index+"')";%>
                            <td class='copy'>
                            <html:link href="<%=viewLinkAttachments%>">
                                <bean:write property="fileName" name="routingAttachmentBean" />
                            </html:link> 
                            <td>
                           <%--Modified for the case id 2574 end--%>
                        </tr>
                        
                    </logic:iterate>
                </logic:present>
            </table>
    
    </body>
</html>
