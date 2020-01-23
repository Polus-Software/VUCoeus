<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<%String proposalNumber=(String)session.getAttribute("proposalNumber"+session.getId());
%>
<html:html locale="true">
    <head>
    <title>Submit To Sponsor Message</title>
    <html:base/>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
    <script>    
    function showProposalDetails(){
        document.generalInfoProposal.action = "<%=request.getContextPath()%>/getGeneralInfo.do?&proposalNumber=<%=proposalNumber%>";
        document.generalInfoProposal.submit();
    }
    </script>    
  
    <html:base/> 

    </head>
    <body class='tabtable'>
    <html:form action = "/getGeneralInfo">
        <tr>
            <td>
                <table width="100%" border="0" cellpadding="2" cellspacing="1" class="tabtable">
                    
                        <tr align="center">
                            <td class = copybold>
                            <bean:message bundle="proposal" key="proposal.submitSponsorFailed"/>
                            </td>
                        </tr>
                    
                               
                </table>                                
            </td>
        </tr>
        <tr align="center">
            <td>
                <html:submit property="ok" value="OK" styleClass="clbutton" onclick="showProposalDetails()" />
            </td>    
        </tr>
     </html:form>    
    </body>
</html:html>