
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="java.util.HashMap, 
                java.util.ArrayList, 
                java.util.Hashtable, 
                java.util.Iterator, 
                edu.mit.coeuslite.utils.CoeusLiteConstants"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<jsp:useBean  id="proposalList" scope="session" class="java.util.Vector"/>
<jsp:useBean  id="proposalColumnNames" scope="session" class="java.util.Vector"/>
<bean:size id="proposalListSize" name="proposalList"/>
<% String propNumber = (String)request.getAttribute("proposalNumber");
    //Added for case#2776 - Allow concurrent Prop dev access in Lite - start
    String proposalNumber = (String)session.getAttribute("proposalNumber"+session.getId());    
    //Added for case#2776 - Allow concurrent Prop dev access in Lite - end
%>

<html:html>
<head>
<title>Coeus Web</title>
<html:base/>

<script language="javascript" type="text/JavaScript" src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/sorttable.js"></script>
</head>
  <script>
        function openGeneralInfo(proposalNumber){
            document.generalInfoProposal.action = "<%=request.getContextPath()%>/getGeneralInfo.do?proposalNumber="+proposalNumber;
            alert(document.generalInfoProposal.action);
            document.generalInfoProposal.submit();
        }
   </script>
    <!-- Added for case#2776 - Allow concurrent Prop dev access in Lite - start -->
    <script>
        var createBudgetPopUp = "<%=request.getParameter("createBudgetPopUp")%>"; 
        if(createBudgetPopUp == "Y"){
            var msg = '<bean:message bundle="proposal" key="generalInfoProposal.createNewBudget"/>';
            if (confirm(msg)==true){                     
                window.location = "<%=request.getContextPath()%>/getBudget.do?proposalNumber=<%=proposalNumber%>&createBudgetPopUp=Y&listPage=Y";
            }  
        }
   </script>
   <!-- Added for case#2776 - Allow concurrent Prop dev access in Lite - end -->
<html:form action="/getBudget.do">
<body>
<logic:messagesPresent message = "true"> 
             
                 <html:messages id="message" message="true" property="noDisplayAndEditRights" bundle="proposal">
                 <script>
                    var msg = '<bean:write name = "message"/>';                  
                </script>
                 </html:messages> 
                <script>
                    var displayMode = "<%=session.getAttribute("mode"+session.getId())%>";                   
                    if(displayMode == "noRights"){
                        alert(msg);
                    }
                </script>

       </logic:messagesPresent>
<table width="100%" height="100%"  border="0" cellpadding="0" cellspacing="0" class="table">
    <tr>
        <td height="653" align="left" valign="top">
        <table width="99%"  border="0" align="center" cellpadding="0" cellspacing="0" class="tabtable">
            <tr class='copybold'>
            <td>
                <font color='red'>
                    <logic:messagesPresent message = "true">  
                        <html:messages id="message" message="true" property="creationProposalRightRequired" bundle="proposal">                
                            <font color="red"><li><bean:write name = "message"/></li></font>
                        </html:messages>
                        <!-- Added for case#2776 - Allow concurrent Prop dev access in Lite - start -->
                        <html:messages id="message" message="true" property="budgetDoesnotExist" bundle="budget">    
                        <script>
                            var errorMsg = '<bean:write name = "message"/>';   
                            alert(errorMsg);
                        </script>                                                        
                        </html:messages>   
                        <html:messages id="message" message="true" property="noModifyAndViewRights" bundle="budget">                
                        <script>
                            var errorMsg = '<bean:write name = "message"/>';   
                            alert(errorMsg);
                        </script>                                                        
                        </html:messages>   
                        <html:messages id="message" message="true" property="budgetLock" bundle="proposal">                        
                            <script>  
                                var hasModifyValue = "<%=session.getAttribute("hasRights")%>";
                                var errorMsg = '<bean:write name = "message"/>';   
                                if(hasModifyValue == "true"){
                                    if(confirm(errorMsg)==true){                                        
                                        document.syncPersonsDynaList.action = "<%=request.getContextPath()%>/getBudget.do?proposalNumber=<%=proposalNumber%>&displayMode=Y";
                                        document.syncPersonsDynaList.submit();                                           
                                    }
                                }else{
                                    alert(errorMsg);
                                }
                            </script>                        
                        </html:messages>
                        <html:messages id="message" message="true" property="cannotCreateBudget" bundle="budget">    
                        <script>
                            var errorMsg = '<bean:write name = "message"/>';   
                            alert(errorMsg);
                        </script>                                                        
                        </html:messages>                           
                        <!-- Added for case#2776 - Allow concurrent Prop dev access in Lite - end -->
                    </logic:messagesPresent>
                </font> 
            </td>
            </tr>
            <tr>
                <td colspan="4" align="left" valign="top">
                    <table width="100%" height="20"  border="0" cellpadding="0" cellspacing="0" class="tableheader">
                         <tr>
                            <td>  List of <%=session.getAttribute("proposalName")%>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>            
            <tr> 
                <td> &nbsp; </td>
            </tr>
            <tr align="center">
                <td>    
                         <table width="98%" height="100%" border="0" cellpadding="2" cellspacing="0" id="t1" class="sortable">
                                <tr>
                                
                                 <%
                                     if(proposalColumnNames != null && proposalColumnNames.size()>0){
                                         for(int index=0;index<proposalColumnNames.size();index++){
                                             edu.mit.coeus.search.bean.DisplayBean displayBean = (edu.mit.coeus.search.bean.DisplayBean)proposalColumnNames.elementAt(index);
                                             if(displayBean.isVisible()){
                                                 String strColumnName = displayBean.getValue();
                                                 %>
                                                    <td class="theader"><%=strColumnName%></td>
                                                   <!--<td class="tableheadersub3">&nbsp;&nbsp;</td>-->
                                                   
                                                 <%
                                             }
                                        }
                                        %>
                                        <!-- Added for case#2776 - Allow concurrent Prop dev access in Lite -->
                                        <td class="theader"><bean:message bundle="proposal" key="generalInfoProposal.Budget"/></td>
                                        <%
                                     }
                                 %>
                                 
                                </tr>
                                <%  
                                        String strBgColor = "#DCE5F1";
                                        int count = 0;
                                 %>
                                 <logic:present name="proposalList">
                                    <logic:iterate id="proposal" name="proposalList" type="java.util.HashMap">
                                <%
                                        if (count%2 == 0) 
                                            strBgColor = "#D6DCE5"; 
                                        else 
                                            strBgColor="#DCE5F1"; 
                                 %>
                                <tr bgcolor="<%=strBgColor%>" valign="top" onmouseover="className='TableItemOn'" onmouseout="className='TableItemOff'">
                                <%                                    
                                     if(proposalColumnNames != null && proposalColumnNames.size()>0){
                                         
                                         for(int index=0;index<proposalColumnNames.size();index++){
                                            edu.mit.coeus.search.bean.DisplayBean displayBean = (edu.mit.coeus.search.bean.DisplayBean)proposalColumnNames.elementAt(index);
                                            if(!displayBean.isVisible())
                                                continue;
                                            String key = displayBean.getName();                                           
                                            if(key != null){
                                                String value = proposal.get(key) == null ? "" : proposal.get(key).toString();                                               
                                                proposalNumber = (String)proposal.get("PROPOSAL_NUMBER");
                                                if(index==1 || index==2){
                                                    if(value != null && value.length() > 60){
                                                        value = value.substring(0,55);
                                                        value = value+" ...";
                                                    }
                                                }%>
                                   <td align="left" nowrap class="copy">
                                   <a href="<%=request.getContextPath()%>/getGeneralInfo.do?proposalNumber=<%=proposalNumber%>"><u><%=value%></u></a>
                                   </td>

                                     <%
                                            }
                                        }
                                     }
                                 %>
                                    <!-- Added for case#2776 - Allow concurrent Prop dev access in Lite - start -->
                                    <td align="left" nowrap class="copy">
                                        <a href="<%=request.getContextPath()%>/getBudget.do?proposalNumber=<%=proposalNumber%>&listPage=Y&isEditable=null">
                                            <u><bean:message bundle="proposal" key="generalInfoProposal.Budget"/></u>
                                        </a>
                                    </td>   
                                    <!-- Added for case#2776 - Allow concurrent Prop dev access in Lite - end -->
                                </tr>
                                 <% count++;%>
                                     </logic:iterate>
                                </logic:present>
                                <logic:equal name="proposalListSize" value="0" >
                                                    <tr>
                                                        <td colspan='3' height="23" align=center>
                                                        <div>
                                                     <bean:message bundle="proposal" key="generalInfoProposal.noResults"/> 
                                                        </div>
                                                        </td>
                                                    </tr>
                                </logic:equal>
                            </table>                                                     
                </td>
            </tr>
            
            <tr>
                <td>
                    &nbsp;
                </td>
            </tr>
        </table>
        </td>
    </tr>
    <tr>
        <td height='10'>
            &nbsp;
        </td>
    </tr>       
</table>
</body>
</html:form>
</html:html>
