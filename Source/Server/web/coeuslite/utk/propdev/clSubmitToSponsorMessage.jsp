<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>

<html:html locale="true">
    <head>
    <title>Submit To Sponsor Message</title>
    <html:base/>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
    <script>
        function open_inbox(){
            document.inboxForm.action = "<%=request.getContextPath()%>/getInboxMessages.do?messageType=unresolved&Menu_Id=005";
            document.inboxForm.submit();
        }
    </script>    
  
    <html:base/> 

    </head>
    <%
        String sequence = (String)request.getAttribute("Sequence");
        String instPropNumber = (String)request.getAttribute("InstituteProposal");
    %>
    <body class='tabtable'>
    <html:form action = "/getInboxMessages">
        <tr>
            <td>
                <table width="100%" border="0" cellpadding="2" cellspacing="1" class="tabtable">
                    
                    <% if(sequence != null && sequence.equals("N")) {%>
                        <tr align="center">
                            <td class = "copybold">
                                A new sequence number for Institute proposal
                                <%=instPropNumber%>
                                has been created.
                            </td>
                        </tr>
                    <%} else {%>
                        <tr align="center">
                            <td class = copybold>
                            Institute Proposal <%=instPropNumber%> has been generated.
                            </td>
                        </tr>
                    <%}%>
                               
                </table>                                
            </td>
        </tr>
        <tr align="center">
            <td>
                <html:button property="OK" value="OK" styleClass="clbutton" onclick="open_inbox()"/>
            </td>    
        </tr>
    </html:form>    
    </body>
</html:html>