<%@ page contentType="text/html;charset=UTF-8" language="java" 
import="java.util.Vector"
%>
<%@page import="edu.mit.coeuslite.utils.CoeusLiteConstants,
                edu.mit.coeuslite.utils.bean.MenuBean,
                edu.mit.coeus.budget.bean.BudgetInfoBean"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"	prefix="coeusUtils" %>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<jsp:useBean id="menuItems" scope="session" class="java.util.Vector" />

<html:html locale="true">
<% try{ BudgetInfoBean budgetInfoBean  =  (BudgetInfoBean)session.getAttribute("budgetInfoBean");
    String proposalNumber = budgetInfoBean.getProposalNumber();
    String versionNumber = String.valueOf(budgetInfoBean.getVersionNumber());
    String mode = (String)session.getAttribute("mode"+session.getId());
%>
    <html:base/>
    <head>       
            <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">            
        <style type="text/css">
        <!--
        body {
	margin-left: 3px;
        }
        -->
        </style>        
        <script>          
             function generateAllPeriods(link)
             {
                CLICKED_LINK = link;
                if(validate()){
                    var winleft = (screen.width - 710) / 2;
                    var winUp = (screen.height - 400) / 2;  
                    var win = "scrollbars=1,resizable=0,width=770,height=390,left="+winleft+",top="+winUp
                    sList = window.open('<bean:write name='ctxtPath'/>'+"/"+link, "list", win);
                }

             }
             function call(link) {
                CLICKED_LINK = link;
                return validate();
             }
             function showAlert(){
             alert("<bean:message bundle="proposal" key="generalInfoProposal.notImplemented"/>");
             }         
        </script>        
    </head>
  <body>       
    <table width="100%" height="100%"  border="0" cellpadding="0" cellspacing="3" class="table">

          <tr class="rowLine"><%int index=1;%>
               <td align="left" valign="top">
                   <table width="98%"  border="0" align="center" cellpadding="2" cellspacing="0" class="table">
                      <jsp:useBean id="budgetMenuItemsVector" scope="session" class="java.util.Vector" /> 
                          <logic:iterate name="budgetMenuItemsVector" id="budgetMenuBean" type="edu.mit.coeuslite.utils.bean.MenuBean">
                          
                          <%
                            String linkName = budgetMenuBean.getMenuName();
                            String linkValue = budgetMenuBean.getMenuLink();
                            String groupName = budgetMenuBean.getGroup();
                            String menuId = budgetMenuBean.getMenuId();
                            boolean isVisible = budgetMenuBean.isVisible();
                            if("display".equals(mode) && "B023".equals(menuId)){
                                isVisible = false;
                            }
                          %>
                          <%if(index==Integer.parseInt(groupName)){%>
                          <!--<tr>
                            <td>&nbsp;</td>
                          </tr>-->
                          </table>
                          <br>
                          <table width="98%"  border="0" align="center" cellpadding="2" cellspacing="0" class="table">
                          <%index++;}%>
                          <%if(menuId.equals("B016") || menuId.equals("B017")) {%>
                          <tr class = "menuHeaderName">
                            <td></td>
                            <td colspan='2'><%=linkName%></td>
                          </tr>
                          <%}else {%>
                                <%if(isVisible){%>
                                <tr>
                                    <td class="coeusMenu"> 
                                        <logic:equal name="budgetMenuBean" property="dataSaved" value="true">
                                                 <img src="<bean:write name='ctxtPath'/>/coeusliteimages/img_saved.gif" width="22" height="19">
                                        </logic:equal>
                                   </td>
                                    
                                    <td class="coeusMenu"> 
                                          <bean:define id="name" name="budgetMenuBean" property="menuName"/> 
                                          <bean:define id="link_Value" name="budgetMenuBean" property="menuLink"/> 
                                          <bean:define id="selected" name="budgetMenuBean" property="selected"/> 
                                          <bean:define id="fieldName" name="budgetMenuBean" property="fieldName"/>
                                          <bean:define id="dataSaved" name="budgetMenuBean" property="dataSaved"/>
                                          <% if(linkValue.equals("/generateAllPeriods.do")){%>
                                          <a href="javaScript:return generateAllPeriods('/generateAllPeriods.do');" class="copy"> <%=linkName%></a>                                                    
                                           <% }else if (linkValue.equals("/getGeneralInfo.do")){
                                                    String link = "return call('"+request.getContextPath()+"/getGeneralInfo.do?proposalNumber="+proposalNumber+"&isEditable="+mode+"&Menu_Id=003')";%>
                                                <a href="<%=request.getContextPath() %>/getGeneralInfo.do?proposalNumber=<%=proposalNumber%>&isEditable=<%=mode%>&Menu_Id=003" onclick="<%=link%>"> <%=linkName%> </a>  
                                           <%}else if(linkValue.equals("/getBudgetSummary.do")){ %>
                                                
                                                     <% if(((Boolean)selected).booleanValue()) { %>
                                                    <font color="#6D0202"><b>
                                                    <%=linkName%>
                                                     </b> </font>
                                                   <%} else {
                                                       String link = "return call('"+request.getContextPath()+"/getBudgetSummary.do?proposalNumber="+proposalNumber+"&versionNumber="+versionNumber+"&isEditable="+mode+"')";%>
                                                   <a href="<%=request.getContextPath() %>/getBudgetSummary.do?proposalNumber=<%=proposalNumber%>&versionNumber=<%=versionNumber%>&isEditable=<%=mode%>" onclick="<%=link%>">
                                                   <%=linkName%></a>
                                                     <%}%>
                                                
                                            <%}else{%>
                                            <% if(((Boolean)selected).booleanValue()) { %>
                                                    <font color="#6D0202"><b>
                                                    <%=linkName%>
                                                     </b> </font>
                                                   <%}else if(linkValue.equals("/empty")){%>                                          
                                                        <a href="javascript:showAlert()"> <%=linkName%>
                                                        </a> 
                                                    <%}else {
                                                        String link = "return call('"+request.getContextPath()+linkValue+"')";%>
                                                   <html:link page ="<%=linkValue%>" styleClass="copy" onclick="<%=link%>">
                                                   <%=linkName%></html:link>
                                                     <%}%>
                                         <%}%>
                                    </td>
                                    <td align=right nowrap class ="selectedMenuIndicator" >
                                        <logic:equal name="budgetMenuBean" property="selected" value="true">
                                                <bean:message key="menu.selected"/> 
                                        </logic:equal>
                                    </td>
                                 <%}%>
                          </tr>
                          <%}%>
                           <!--<tr class="coeusMenuSeparator">
                           <td colspan='3' height='2'>
                           </td>
                           </tr>-->  
                       </logic:iterate>
                          <!--<tr>
                            <td>&nbsp;</td>
                          </tr>-->
                </table>
                <%}catch(Exception ex){
                    ex.printStackTrace();
                }%>
            </td>
          </tr>
    </table>
  </body>    
</html:html>
