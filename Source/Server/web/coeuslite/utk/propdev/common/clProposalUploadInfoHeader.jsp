<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<%@page import="java.util.HashMap,java.sql.Date,edu.wmc.coeuslite.budget.bean.ProposalBudgetHeaderBean
,java.text.SimpleDateFormat"%>
<html>
<head>
<title>Proposal Details</title>
</head>

<body topmargin="0" leftmargin="0">
<%//this will be taken from a bean.
        String proposalNumber = (String)session.getAttribute("proposalNumber");
        String creationStatDesc = (String)session.getAttribute("creationStatDesc");
        String createUser = (String)session.getAttribute("createUser");
        String createTimestamp = (String)session.getAttribute("createTimestamp");


 %>
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="theader">
                  <tr align="left">
                   
                    <td align="center" class="theader" nowrap>&nbsp;<bean:message bundle="proposal" key="upload.proposalNumber"/><br>&nbsp;<%=proposalNumber%></td>
                    <td align="center" class="theader" nowrap>&nbsp;<bean:message bundle="proposal" key="upload.firstUser"/><br>&nbsp;<%=createUser%></td>
                    <td align="center" class="theader" nowrap>&nbsp;<bean:message bundle="proposal" key="upload.dateStarted"/><br>&nbsp;<%=createTimestamp%></td>
                    <td align="center" class="theader" nowrap>&nbsp;<bean:message bundle="proposal" key="upload.status"/><br>&nbsp;<%=creationStatDesc%></td>
                    
                    
                  </tr>
                  </table>
                  
</body>

</html>

