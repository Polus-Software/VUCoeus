<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<html:html>
<%  String proposalNumber=(String)session.getAttribute("proposalNumber"+session.getId());
%>
    <head>
        <style>
            .clsavebutton{ width: 130px; }            
        </style>        
        <title>
            Delete Proposal Page
        </title>
        <script>
    function showProposalDetails(){
    document.forms[0].action = "<%=request.getContextPath()%>/getGeneralInfo.do?&proposalNumber=<%=proposalNumber%>";
    document.forms[0].submit();
    }
        </script>
    </head>
    <body>
        <html:form action="/getGeneralInfo.do" method="POST">
            <table width="100%" cellpadding="2" cellspacing="0" class="table" align="center" >  
                <tr>
                    <td>
                        &nbsp;
                        <br>
                    </td>
                </tr>
                <tr>
                    <td align = "left">
                        <font color="red"  >
                            <html:messages id="message" bundle="proposal" message="true" property="proposalLocked">                
                                <li><bean:write name = "message"/></li>
                            </html:messages>
                        </font>
                    </td>
                    
                </tr>
                <tr>
                    <td align= "left">
                        <br>
                         <html:button property="Save" value="Return To proposal" styleClass="clsavebutton" onclick="showProposalDetails()" />
                    </td>
                </tr>
            </table>      
        </html:form>
    </body>
</html:html>


