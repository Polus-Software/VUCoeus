<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"  prefix="coeusUtils"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<%@ page import="java.util.List, edu.mit.coeus.s2s.bean.FormInfoBean, edu.mit.coeus.s2s.validator.S2SValidationException"%>
<jsp:useBean id="PROPOSAL_RULES_MAP" scope="session" type="java.util.Vector"/>
<%String proposalNumber=(String)session.getAttribute("proposalNumber"+session.getId());
%>
<html:html>
<head><title>Proposal Errors</title>
<script>
    function showProposalDetails(){
    document.validateProposalForm.action = "<%=request.getContextPath()%>/getGeneralInfo.do?&proposalNumber=<%=proposalNumber%>";
    document.validateProposalForm.submit();
    }
</script>

</head>
<body>
<html:form action="/validateProposal.do" method="post">
<table width='100%' height='100%' border='0' cellpadding='2' cellspacing='0' class='tabtable'>




    <tr>
        <td colspan='2' class='tableheader'>
            <bean:message bundle="proposal" key="clProposalValidationRules.Proposal"/> <font color=red><%=proposalNumber%></font>
            <bean:message bundle="proposal" key="clProposalValidationRules.ProposalErrors"/>
        </td>
    </tr>

    <!-- Grants Gov validation Messages - START -->
        <%
        Exception ex = (Exception)request.getAttribute("Exception");
        if(ex != null) {%>
        <tr><td>
                <br>
                <table align="center" width="98%" border="0" cellspacing="0" cellpadding="0">
                    <tr><td>
                            <table width="100%" border="0" cellspacing="0" cellpadding="2" class="theader">
                                <tr><td>
                                        Error:
                                    </td>
                                </tr>
                            </table>
                    </td></tr>

                    <tr><td>
                            <div id="errorDetails" style="overflow:hidden">
                                <table align="center" width="100%" border="0" cellspacing="0" cellpadding="0" class="tabtable">

                                    <tr><td class="copy">
                                            <table width="100%" border="0">
                                                <tr><td>
                                                        <img src="<%=request.getContextPath()%>/coeusliteimages/error.gif" hspace="5" vspace="5">
                                                    </td>
                                                    <td class="copybold">
                                                        <font color="red">
                                                            <%
                                                            String exceptionMessage = ex.getMessage();
                                                            //if message has a link. display link in next line
                                                            int index = exceptionMessage.indexOf("<a");
                                                            if(index != -1){
                                                            exceptionMessage = exceptionMessage.substring(0, index) +"<br>"+exceptionMessage.substring(index, exceptionMessage.length());
                                                            }
                                                            out.print(exceptionMessage);
                                                        %></font>
                                                </td></tr>
                                            </table>
                                    </td></tr>
                                    <tr><td>
                                            <table align="center" width="98%" border="0" cellspacing="0" cellpadding="2" class="tabtable">
                                                <%
                                                if(ex instanceof S2SValidationException) {%>
                                                <tr><td><font color="red"><b>Please Correct the Following Errors:</b></font></td></tr>
                                                <%
                                                S2SValidationException s2SValidationException = (S2SValidationException)ex;
                                                List errList = s2SValidationException.getErrors();
                                                S2SValidationException.ErrorBean errorBean;
                                                FormInfoBean formInfoBean;
                                                for(index = 0; index < errList.size(); index++) {
                                                errorBean = (S2SValidationException.ErrorBean)errList.get(index);
                                                if(errorBean.getMsgObj() instanceof FormInfoBean) {
                                                formInfoBean = (FormInfoBean)errorBean.getMsgObj();
                                                %><tr class="rowLine table"><td><b><%=formInfoBean.getFormName()%></b></td></tr>
                                                <%
                                                }else {%>
                                                <tr class="rowLine"><td><%=errorBean.getMsgObj()%></td></tr>
                                                <%    }
                                                }
                                                }
                                                %>
                                    </table> <br>  </td></tr>
                                </table>
                            </div>
                    </td></tr>
                </table>
        </td></tr>
        <%}%>
    <!-- Grants Gov validation Messages - END -->

    <logic:present name="PROPOSAL_RULES_MAP" scope="session">
    <logic:iterate id="proposalError" name="PROPOSAL_RULES_MAP" type="java.lang.String">
    <tr>
        <td nowrap class ='copybold'> &nbsp; &nbsp;<li><%=proposalError%></li></td>
    </tr>
    <%--index++--%>
    </logic:iterate>
    </logic:present>
    <logic:present name="isPpcCompleteFlag">
    <logic:equal value="true" name="isPpcCompleteFlag">
    <tr>
        <td nowrap class ='copybold'>&nbsp;&nbsp;<li>Certification is not Completed</li></td>
    </tr>
                    </logic:equal>
</logic:present>
    <tr>
        <td height='15'>
        </td>
    </tr>
    <tr>
        <td colspan='2' align=center class='table'>
            <html:submit property="ok" value="OK" styleClass="clbutton" onclick="showProposalDetails()" />
        </td>
    </tr>
</table>
</html:form>
</body>
</html:html>
