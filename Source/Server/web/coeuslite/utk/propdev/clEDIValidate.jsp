<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils" %>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<jsp:useBean id="PROPOSAL_RULES_MAP" scope="session" type="java.util.Vector"/>
<html:html>
<%  String proposalNumber=(String)session.getAttribute("proposalNumber"+session.getId());
    String submissionSuccess =(String) request.getAttribute("submissionSuccess");
%>
<head>
<title>EDIValidation</title>
<script>
    function showProposalDetails(){
    document.forms[0].action = "<%=request.getContextPath()%>/getGeneralInfo.do?&proposalNumber=<%=proposalNumber%>";
    document.forms[0].submit();
    }
</script>
</head>
<body>
<html:form action="/getGeneralInfo.do">
<table width="100%" height="100%"  border="0" cellpadding="0" cellspacing="0" class="table">
<tr>
    <td>
        <table width="99%" align=center height="100%"  border="0" cellpadding="0" cellspacing="0" class="tabtable">
        <tr>
            <td>
                <table width="100%" height="100%"  border="0" cellpadding="0" cellspacing="0" class="tableheader">
                <tr>
                    <td class='redColor'>
                        <%if(submissionSuccess!=null && submissionSuccess.equals("submissionSuccess")) {%>
                        <bean:message bundle="proposal" key="proposal.proposalSubmitted"/>
                        <%} else { %>
                        <bean:message bundle="proposal" key="proposal.proposalErrors"/>
                        <%}%>                        
                    </td>
                </tr>
                </table>
            </td>
        </tr>
        
        <tr>
            <td>
                <div id="helpText" class='helptext'>            
                    <bean:message bundle="proposal" key="helpTextProposal.SubmitForApproval"/>  
                </div> 
            </td>
        </tr>
        
        <tr>
            <td height='10'>
            </td>
        </tr>
        
        
        
        <logic:present name="PROPOSAL_RULES_MAP" scope="session">
        <logic:iterate id="vecCheckEDIData" name="PROPOSAL_RULES_MAP" type="java.lang.String">
        <tr>
            <td align=center class='copybold'>
                <%if(submissionSuccess!=null && submissionSuccess.equals("submissionSuccess")) {%>
                    <bean:message bundle="proposal" key="proposal.successfullForRouting"/>
                <%} else { %>
                <%=vecCheckEDIData%>
                <%}%>
            </td>
        </tr>
        </logic:iterate>
        </logic:present>
        <tr>
            <td height='10'>
            </td>
        </tr>        
        </table>
    </td>
</tr>
<tr>
    <td height='10'>
    </td>
</tr>
<tr>
    <td align=center>
        <html:button property="ok" value="OK" styleClass="clbutton" onclick="showProposalDetails()" />
    </td>
</tr>

</table>
</html:form>
</body>
<script>
      var help = '<bean:message bundle="proposal" key="helpTextProposal.SubmitForApproval"/>';
      help = trim(help);
      if(help.length == 0) {
        document.getElementById('helpText').style.display = 'none';
      }
      function trim(s) {
            return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
      }  
</script>
</html:html>
