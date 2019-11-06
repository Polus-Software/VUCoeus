<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.Vector"%>

<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<%
Vector vecMessages = (Vector) request.getAttribute("budgetWarning");
String budgetPeriod = request.getParameter("budgetPeriod");
String rowIndex = request.getParameter("rowIndex");
String save = request.getParameter("Save") ;
String edit = request.getParameter("Edit") ;
String queryString = "/calculatePersonnelBudget.do?budgetPeriod=";
queryString = queryString.concat(budgetPeriod);
if(save != null && !"".equals(save)){
    queryString = queryString.concat("&Save="+save);
}
if(edit != null && !"".equals(edit)){
    queryString = queryString.concat("&Edit="+edit);
}
if(rowIndex != null && !"".equals(rowIndex)){
    queryString = queryString.concat("&rowIndex="+rowIndex);
}

queryString = queryString.concat("&WarningDisplayed=Y");
%>

<html:html>
    <head><title>
            Warning
        </title>
        <link href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css" rel="stylesheet" type="text/css">
    </head>
    <body>
        
        <html:form action="/calculatePersonnelBudget.do" method="post">  
            
            
            <table  width="100%"  border="0" align="center" cellpadding="0" cellspacing="0" class="table">
                <tr class='tableheader'>
                    <td>
                        <table width="100%" border="0" cellpadding="0" cellspacing="0">
                            <tr class='tableheader'>            
                                <td>
                                    <bean:message bundle="budget" key="personnelWarning.title"/>
                                </td>
                                
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                <td colspan="4" align="left" valign="top" class='core'>
                <table width="100%"  border="0" align="center" cellpadding="0" cellspacing="2" class="tabtable">
                    
                    
                    <%if(vecMessages != null && vecMessages.size() >0) {
                    for(int index = 0; index < vecMessages.size(); index++){%>
                    <tr>
                        <td align = "left">
                            <li> <%=vecMessages.elementAt(index)%> </li>     
                        </td>
                        <td>
                            <br>
                            &nbsp;
                            <br>
                        </td>
                    </tr>
                    <%}}%>
                    
                </table>
                <tr>
                    <td class='savebutton'>
                        <html:button  property="Save" styleClass="clsavebutton" onclick="showWar()">
                            <bean:message bundle="budget" key="personnelWarning.buttonContinue" />
                        </html:button>
                    </td>
                </tr>
                
                
            </table>
        </html:form>
        <script>
        function showWar() {
            document.budgetPersonnelDynaBean.action="<%=request.getContextPath()%><%=queryString%>";
            document.budgetPersonnelDynaBean.submit();
            } 
        </script>
    </body>
</html:html>

