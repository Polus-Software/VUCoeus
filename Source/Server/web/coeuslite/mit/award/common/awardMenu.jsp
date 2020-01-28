<%-- 
    Document   : awardMenu
    Created on : Jul 28, 2011, 11:58:26 AM
    Author     : indhulekha
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="edu.mit.coeuslite.utils.CoeusLiteConstants,
                edu.mit.coeuslite.utils.bean.MenuBean"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"	prefix="coeusUtils" %>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<jsp:useBean id="menuItems" scope="session" class="java.util.Vector" />

<html:html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title></title>
       
    </head>
    <body>
        <table width="100%" height="100%"  border="0" cellpadding="0" cellspacing="3" class="table">

          <tr class="rowLine"><%int index=1;%>
               <td align="left" valign="top">
                   <table width="98%"  border="0" align="center" cellpadding="2" cellspacing="0" class="table">
                      <!-- Changed the  name to Solve session problem -->
                       <jsp:useBean id="awardInfoHeader" scope="session" class="java.util.Vector" />
                          <logic:iterate name="awardInfoHeader" id="awardMenuBean" type="edu.mit.coeuslite.utils.bean.MenuBean">

                               <%
                            String linkName = awardMenuBean.getMenuName();
                            String linkValue = awardMenuBean.getMenuLink();
                            String groupName = awardMenuBean.getGroup();
                            String menuId = awardMenuBean.getMenuId();
                            boolean isVisible = awardMenuBean.isVisible();
                          %>
                          <%if(isVisible) {
                              if(index == Integer.parseInt(groupName)) {%>
                               </table>
                          <br>
                          
                          <table width="100%"  border="0" align="center" cellpadding="2" cellspacing="0" class="table">
                          <%index++;} if(menuId.trim().equals("A007")) {%>
                          <tr class = "menuHeaderName"><td>Print</td></tr>
                          <%}%>
                          <tr>
                              <%if(linkValue.equals("/getAwardInfo.do")) {%>
                                 <td class="coeusMenu"> <font color="#6D0202"><%=linkName%> </font></td>
                                  <td align=right nowrap class ="selectedMenuIndicator" >
                                        <logic:equal name="awardMenuBean" property="selected" value="true">
                                                <bean:message key="menu.selected"/>
                                        </logic:equal>
                                    </td>
                                 <%} else if(menuId.trim().equals("A007")){%>
                                <td class="coeusMenu"><a href="<%=request.getContextPath()%>/awardPrintnew.do?awdType=2" target="_blank"><%=linkName%></a> </td>
                                <%}else if(menuId.trim().equals("A008")){%>
                                <td class="coeusMenu"><a href="<%=request.getContextPath()%>/awardPrintnew.do?awdType=1" target="_blank"><%=linkName%></a> </td>
                                <%}else if(menuId.trim().equals("A009")){%>
                                <td class="coeusMenu"><a href="<%=request.getContextPath()%>/awardPrintnew.do?awdType=3" target="_blank"><%=linkName%></a> </td>
                                <%}%>

                          </tr>
                         <%}%>
                       </logic:iterate>
                          <!--<tr>
                            <td>&nbsp;</td>
                          </tr>-->
                       </table>                    
               
            </td>
          </tr>
    </table>
    </body>
</html:html>
