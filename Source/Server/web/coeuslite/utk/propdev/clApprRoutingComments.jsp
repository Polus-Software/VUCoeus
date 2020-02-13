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
        <title><bean:message bundle="proposal" key="routing.commentsLabel"/></title>
       <%-- 
        Hidding and showing the Comments
        <Script language="JavaScript">
            function processVisiblity (blockId)
            {
                var obj = document.getElementById(blockId);
                var textShow = document.getElementById(blockId+"_show");
                var textHide = document.getElementById(blockId+"_hide");
                if (obj.style.display=='none') 
                { 
                    obj.style.display = 'block';
                    textShow.style.display = 'none';
                    textHide.style.display = 'block';
                } 
                else 
                {
                    obj.style.display = 'none';
                    textShow.style.display = 'block';
                    textHide.style.display = 'none';
                }
             }
       </script>--%>
    </head>
    <body>

     <%-- Added for 3915 - can't see comments in routing in Approval Routing - Start--%>
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
          <%-- Case #:3915 - End --%>
          
        <table width="100%" class='tabtable'>
            <tr class="theader" >
                <td height="20">
                    <bean:message bundle="proposal" key="routing.commentsLabel"/>
                </td>
                <td align="right" valign="middle">
                    <a href="javaScript:window.close();"><u>Close</u></a>
                </td>                
            </tr>
            </table>
            
            <%-- Added for 3915 - can't see comments in routing in Approval Routing --%>
            <table border="0" width="99%" cellspacing="0" cellpadding="0" align=center class="tabtable">
          <%--  <%int commentsID=0;%> --%>
            <logic:present name="ApprovalRoutingComments" scope="session">
                <logic:iterate id="routingCommentsBean" name="ApprovalRoutingComments" type="edu.mit.coeus.routing.bean.RoutingCommentsBean" scope="session">
                <%--
                   Hidding and showing the Comments
                    <%commentsID++;%> 
                    <tr class='theader'>
                  
                 <td width="18%" height="19" align="left" nowrap><b>Comment <%=commentsID%></b></td>
                  <td width="20%" height="19" align="center"></td>
                  <td width="20%" height="19">&nbsp;</td>
                  <td></td> 
                  <td width="20%" height="19" align=center>
                    <%String link="javaScript:processVisiblity('"+commentsID+"')";%>
                        <html:link href="<%=link%>">
                        <div id="<%=commentsID%>_show" style="display: none"><bean:message bundle="proposal" key="routing.show"/></div>
                        <div id="<%=commentsID%>_hide" style="display: block"><bean:message bundle="proposal" key="routing.hide"/></div>
                        </html:link>
                  </td>
                  
                    <div id="<%=commentsID%>" style="display: block"> 
	            
                       <%String strBgColor="#D6DCE5";%>--%>
                       <tr>
                          <td style="border:5px solid #789FD5;"></td>
                       </tr>
                       <tr>
                          <td class='copy'>
                             <bean:write property="comments" name="routingCommentsBean" />
                          <td>
                        </tr>
                        <%--</div>--%>
                </logic:iterate>
            </logic:present>
        </table>
    
    </body>
</html>
