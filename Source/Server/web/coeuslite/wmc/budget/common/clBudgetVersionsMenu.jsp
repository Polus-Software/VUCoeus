<%@ page contentType="text/html;charset=UTF-8" language="java" 
import="java.util.Vector"
%>
<%@page import="edu.mit.coeuslite.utils.bean.MenuBean,
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
    String mode = (String)session.getAttribute("mode"+session.getId());
%>
    <html:base/>
    <head>       
        
            <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">    
        <script>
             function call(link) {
                CLICKED_LINK = link;
                return validate();
             }
        </script>
        
    </head>
  <body>       
    <table width="96%" align="center" border="0" cellpadding="0" cellspacing="0" class="table">

          <tr>
               <td align="left" valign="top">
                   <table width="90%"  border="0" align="center" cellpadding="0" cellspacing="0">
                      <jsp:useBean id="budgetVersionMenuItemsVector" scope="session" class="java.util.Vector" /> 
                          <logic:iterate name="budgetVersionMenuItemsVector" id="budgetMenuBean" type="edu.mit.coeuslite.utils.bean.MenuBean">
                           <tr>
                                <%
                                      String linkName = budgetMenuBean.getMenuName();
                                      String linkValue = budgetMenuBean.getMenuLink();
                                      String groupName = budgetMenuBean.getGroup();
                                   %>
                                <td width="19" align="left" valign="top" ><img src="<bean:write name='ctxtPath'/>/coeusliteimages/img_blank.gif" width="5" height="19"></td>
                                <td width='100%' align="left" nowrap valign="middle" class="menu"  STYLE="background-image:url('<bean:write name='ctxtPath'/>/coeusliteimages/tile_menu.gif');border:0"> 
                                    
                                          <bean:define id="name" name="budgetMenuBean" property="menuName"/> 
                                          <bean:define id="link_Value" name="budgetMenuBean" property="menuLink"/> 
                                          <bean:define id="selected" name="budgetMenuBean" property="selected"/> 
                                          <bean:define id="fieldName" name="budgetMenuBean" property="fieldName"/>
                                                                                           
                                           <%  if (linkValue.equals("/getGeneralInfo.do")){
                                                   String link = "return call('"+request.getContextPath()+"/getGeneralInfo.do?proposalNumber="+proposalNumber+"&isEditable="+mode+"&Menu_Id=003')";%>
                                                <a href="<%=request.getContextPath() %>/getGeneralInfo.do?proposalNumber=<%=proposalNumber%>&isEditable=<%=mode%>&Menu_Id=003" onclick="<%=link%>"> <%=linkName%> </a>  
                                           <% }%>                                              
                                           <%  if (linkValue.equals("/getBudgetVersions.do")){ %>
                                           
                                            <% if(((Boolean)selected).booleanValue()) { %>
                                                    <font color="#6D0202"><b>
                                                    <%=linkName%>
                                                     </b> </font>
                                                   <%} else {                                            
                                                        String link = "return call('"+request.getContextPath()+"/getBudgetVersions.do?proposalNumber="+proposalNumber+"&isEditable="+mode+"')";%>
                                                <a href="<%=request.getContextPath() %>/getBudgetVersions.do?proposalNumber=<%=proposalNumber%>&isEditable=<%=mode%>" onclick="<%=link%>"> <%=linkName%> </a>  
                                           <% } }%>        
                                           
                                    </td>
                                <td width="22" align="left" valign="top"><img src="<bean:write name='ctxtPath'/>/coeusliteimages/img_menu_rt.gif" width="22" height="20"></td>
                          </tr>     
                       </logic:iterate>                                                        
                </table>
                <%}catch(Exception ex){
                    ex.printStackTrace();
                }%>
            </td>
          </tr>
    </table>
  </body>    
</html:html>
