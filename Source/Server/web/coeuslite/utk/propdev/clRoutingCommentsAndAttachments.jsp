<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page import="edu.mit.coeus.utils.DateUtils" %>
<%@ page import="edu.utk.coeuslite.propdev.bean.EPSProposalHeaderBean" %>
<%@ page import="edu.mit.coeuslite.irb.bean.ProtocolHeaderDetailsBean,edu.mit.coeus.utils.CoeusVector,edu.mit.coeus.routing.bean.RoutingDetailsBean" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="<%=request.getContextPath()%>/coeuslite/mit/utils/css/coeus_styles.css" rel="stylesheet" type="text/css">
        <title><bean:message bundle="proposal" key="routing.commentsAttachmentsLabel"/></title>
        <script>
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
                     
           function view_attachments(routingNumber, mapNumber, levelNumber, stopNumber, approverNumber,index){
              window.open("<%=request.getContextPath()%>/routingAttachments.do?&routingNumber="
                +routingNumber+"&mapNumber="+mapNumber+"&levelNumber="+levelNumber+"&stopNumber="+stopNumber+"&approverNumber="+approverNumber+"&index="+index);
           }  
       
        </script>
    </head>
    <body>        
        <%
             CoeusVector cvExistingApproversWithCommentsAttachements = new CoeusVector();
             if(session.getAttribute("cvApprDetails")!=null)
       { cvExistingApproversWithCommentsAttachements=(CoeusVector)session.getAttribute("cvApprDetails");}%>
        <table width="100%" class='tabtable'>
            <tr class="theader" >
                <td align="left" height="26">
                    <bean:message bundle="proposal" key="routing.commentsAttachmentsLabel"/>
                </td>
                <td align="right" height="26" valign="middle">
                    <a href="javaScript:window.close();"><u>Close</u></a>
                </td>                
            </tr>
        </table>
        
        <table>
            <tr>
                <td>                    
                </td>
            </tr>
        </table>
        
        
        <table border="0" width="99%" cellspacing="0" cellpadding="0" align=center class="copy">
            <tr>
                <td  width="99%" height="100%">
                    
                    <table border="0" width="99%" cellspacing="0" cellpadding="0" align=center class="tabtable">
                        <logic:present name="cvApprDetails" scope="session">
                            <% int count = 1;
                               int cnt =0;
                            %>
                            <logic:notEmpty name="cvApprDetails" > 
                                <logic:iterate id="routingDetailsBean" name="cvApprDetails" type="edu.mit.coeus.routing.bean.RoutingDetailsBean" scope="session">                                    
                                    <% if(cvExistingApproversWithCommentsAttachements!=null){
                                         RoutingDetailsBean routBean= new  RoutingDetailsBean();
                                         routBean=(RoutingDetailsBean)cvExistingApproversWithCommentsAttachements.get(cnt);
                                        String approvalStatus=null;
                                        approvalStatus=routBean.getApprovalStatus();

                                     %>
                                    <tr>
                                        <td class="copybold" width="2%" height="25" >  
                                            <%String divName="Panel"+count;%>
                                            <div id='<%=divName%>' style='display:none;'>
                                                
                                                <%String divlink = "javascript:showHide(1,'"+count+"')";%>
                                                <html:link href="<%=divlink%>">
                                                    <%String imagePlus=request.getContextPath()+"/coeusliteimages/plus.gif";%>
                                                    <html:img src="<%=imagePlus%>" border="0"/>
                                                </html:link>                                                                                              
                                            </div>
                                            <%divName="hidePanel"+count;%>
                                            <div id='<%=divName%>' >
                                                
                                                <% divlink = "javascript:showHide(2,'"+count+"')";%>
                                                <html:link href="<%=divlink%>">
                                                    <%String imageMinus=request.getContextPath()+"/coeusliteimages/minus.gif";%>
                                                    <html:img src="<%=imageMinus%>" border="0"/>
                                                </html:link>                                                                                                
                                            </div>
                                        </td>
                                        <td class="copybold" width="72%" >
                                        <% if(approvalStatus=="Bypassed"){%>
                                        <bean:write property="updateUserName" name="routingDetailsBean" /> (<bean:write property="updateUser" name="routingDetailsBean" />)
                                        <%}else{%>
                                        <bean:write property="userName" name="routingDetailsBean" /> (<bean:write property="userId" name="routingDetailsBean" />)
                                        <%}%>
                                        </td>
                                        <td class="copybold" width="18%">&nbsp;<bean:write property="approvalStatus" name="routingDetailsBean" /> </td>                                                                                
                                    </tr>
                                                                     
                                    <%String strBgColor = "#DCE5F1"; 
                                    int row=0; %>  
                                    
                                    
                                    
                                    <bean:define id="cvComments"  name="routingDetailsBean" property="comments" type="java.util.Vector"  />  
                                    <tr>
                                        <td width="2%"></td>
                                        <td colspan="5">
                                            <%divName="pan"+count;%>
                                            <div id='<%=divName%>'>
                                                <table border="0" width="98%" cellspacing="0" cellpadding="0" align=left class="tabtable">
                                                    <logic:iterate id="routingCommentsBean" name="cvComments" type="edu.mit.coeus.routing.bean.RoutingCommentsBean">                                                         
                                                        <%if (row%2 == 0) {
                                                        strBgColor = "#D6DCE5"; 
                                                        }
                                                        else { 
                                                        strBgColor="#DCE5F1"; 
                                                        }   %>
                                                        <tr bgcolor="<%=strBgColor%>" onmouseover="className='TableItemOn'" onmouseout="className='TableItemOff'">
                                                            <td class='copy' width="45%" height="19" >
                                                                <bean:write property="comments" name="routingCommentsBean" />
                                                            </td>
                                                            <td  class='copy' width="10%" height="19" >
                                                                &nbsp;<bean:write property="updateTimestamp" name="routingCommentsBean" />
                                                            </td>
                                                            
                                                        </tr>                                                        
                                                        <%row++;%>
                                                    </logic:iterate>                                                    
                                                    
                                                    <bean:define id="cvAttachments"  name="routingDetailsBean" property="attachments" type="java.util.Vector"  />  
                                                    <% row=0; %>  
                                                    <logic:notEmpty name="cvAttachments" > 
                                                        <tr class='theader'>
                                                            <td height="19" >Attachments</td>  
                                                            <td height="19" ></td>                                                             
                                                        </tr>
                                                        
                                                        <logic:iterate id="routingAttachmentBean" name="cvAttachments" indexId="index" type="edu.mit.coeus.routing.bean.RoutingAttachmentBean"> 
                                                            <%if (row%2 == 0) {
                                                            strBgColor = "#D6DCE5"; 
                                                            }
                                                            else { 
                                                            strBgColor="#DCE5F1"; 
                                                            }   %>
                                                            <%String viewLinkAttachments = "javaScript:view_attachments("+routingDetailsBean.getRoutingNumber()+","+routingDetailsBean.getMapNumber()+","+routingDetailsBean.getLevelNumber()+","+routingDetailsBean.getStopNumber()+","+routingDetailsBean.getApproverNumber()+",'"+index+"')";
                                                            %>
                                                            <tr bgcolor="<%=strBgColor%>" onmouseover="className='TableItemOn'" onmouseout="className='TableItemOff'">
                                                                <td class='copy' width="45%" height="19" >
                                                                    <html:link href="<%=viewLinkAttachments%>">
                                                                        <bean:write property="description" name="routingAttachmentBean" />
                                                                    </html:link>                                                                           
                                                                </td>
                                                                <td class='copy' height="19"  width="10%">
                                                                    <bean:write property="updateTimestamp" name="routingAttachmentBean" />
                                                                </td>                                                                 
                                                            </tr>
                                                            <%row++;%>
                                                        </logic:iterate>
                                                    </logic:notEmpty>
                                                    
                                                </table>                                                
                                            </div>
                                        </td>
                                    </tr>
                                    <%}%>
                                   <% cnt++;
                                    count++;%>
                                </logic:iterate>
                            </logic:notEmpty>
                            <logic:empty name="cvApprDetails" >                                 
                                <tr>
                                    <td class="copybold" width="50%" height="25" >  
                                        <bean:message bundle="proposal" key="routing.noCommentsAttachmentsLabel"/>
                                    </td>
                                </tr>
                            </logic:empty>
                            
                        </logic:present>
                        <tr>
                            <td class="copybold">
                                &nbsp;
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
        
    </body>
</html>
