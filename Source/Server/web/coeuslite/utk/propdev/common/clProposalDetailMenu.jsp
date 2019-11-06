<%@ page contentType="text/html;charset=UTF-8" language="java" 
import="java.util.Vector"
%>
<%@page import="edu.mit.coeuslite.utils.CoeusLiteConstants,
                edu.mit.coeuslite.utils.bean.MenuBean"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"	prefix="coeusUtils" %>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<html:html locale="true">
    <html:base/>
    <head>
        <title>Untitled</title>     
    </head>
<% String proposalNumber = (String)session.getAttribute("proposalNumber"+session.getId());
    boolean hasApproverRecallRight = false;
    boolean hasRecallRight = false;
    if(session.getAttribute("HAS_RECALL_RIGHTS")!=null){
    hasRecallRight = ((Boolean)session.getAttribute("HAS_RECALL_RIGHTS")).booleanValue();
    } 
    if(session.getAttribute("HAS_APPROVER_RECALL_RIGHTS")!=null){
    hasApproverRecallRight = ((Boolean)session.getAttribute("HAS_APPROVER_RECALL_RIGHTS")).booleanValue();
    }
    //COEUSQA-1433 - Allow Recall from Routing - End
    
    
    %>    
    <body> 
        <table width="100%" height="100%"  border="0" cellpadding="0" cellspacing="0" class="table">            
            
              <tr class="rowLine">
                   <td align="left" valign="top">
                       <table width="100%"  border="0" align="center" cellpadding="2" cellspacing="0" class="table">                          
                              <jsp:useBean id="proposalApprovalMenuItemsVector" scope="session" class="java.util.Vector" /> 
                              <logic:iterate name="proposalApprovalMenuItemsVector" id="proposalMenuBean" type="edu.mit.coeuslite.utils.bean.MenuBean">
                                 <%       String linkName = proposalMenuBean.getMenuName();
                                          String linkValue = proposalMenuBean.getMenuLink();
                                          String menuId = proposalMenuBean.getMenuId();
                                          String groupName = proposalMenuBean.getGroup();
                                          if(linkValue.equals("/getGeneralInfo.do")) {
                                              linkValue = proposalMenuBean.getMenuLink()+"?proposalNumber="+proposalNumber;
                                          }
                                          
                                          if(menuId.equals("P010") && (hasRecallRight && hasApproverRecallRight)){
                                            proposalMenuBean.setVisible(true);
                                          }else if(menuId.equals("P010") && (!hasRecallRight || !hasApproverRecallRight)){
                                            proposalMenuBean.setVisible(false);
                                          }
                                         if(request.getAttribute("enableSelfCertication")==null && menuId.equals("P013")){
                                            proposalMenuBean.setVisible(false); 
                                         } 
                      
                              %>
                         
                                                                                                                             
                         <% if(menuId.equals("P014")) {%>
                          </table>
                          <br>
                          <table width="100%"  border="0" align="center" cellpadding="2" cellspacing="0" class="table">
                           
                          <tr class = "menuHeaderName" height='20'>                            
                            <td colspan='4'><%=linkName%></td>
                          </tr>
                         
                          <% }else {          
                               if(proposalMenuBean.isVisible()){%>                                  
                               
                               <tr height='20'>
                                   <td width="10%" align="left" valign="top" class="coeusMenu"></td>
                                    <td width='80%' align="left" valign="middle" class="coeusMenu"> 
                                    
                                        <%if(linkValue!=null && linkValue.equals("/rightPersonCertify")){%>
                                        <a href="<%=request.getContextPath() %>/rightPersonCertify.do?proposalNumber=<%=proposalNumber%>"> 
                                                           <%=linkName%>
                                        </a>
                                        <%}else if(linkValue!=null && linkValue.equals("/displayProposal"))
                                        {%>
                                        <a href="<%=request.getContextPath() %>/displayProposal.do?proposalNo=<%=proposalNumber%>"> 
                                                           <%=linkName%>
                                        </a>
                                        <%}else{%>
                                          <html:link page ="<%=linkValue%>" styleClass="copy">
                                                <%=linkName%>
                                           </html:link>
                                         <%}%> 
                                        </a></td>
                                    <td width="10%" style="color: #6D0202;font-size: 12px;font-weight: bold;" class ="selectedMenuIndicator">
                                        <%if(linkValue!=null && linkValue.equals("/displayProposal")){%>
                                              <bean:message key="menu.selected"/> 
                                        <%}%>
                                    </td>                                   
                              </tr>
                           
                              <%}}%>
                              
                           </logic:iterate> 
                                                                                   
                    </table>
                </td>
              </tr>
        </table>
    </body>
</html:html>
