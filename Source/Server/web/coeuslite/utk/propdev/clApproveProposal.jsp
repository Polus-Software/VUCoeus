<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"  prefix="coeusUtils"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<% String label = request.getParameter("actionMode");
   if(label!=null && label.equals("approve")){
       label = "Approve";
   } else if(label!=null && label.equals("reject")){
       label = "Reject";
   } else if(label!=null && label.equals("bypass")){
       label = "ByPass";
   } else {
       label = "Save";
   }
%>
<html>
    <head>
        <title>Approve Proposal</title>
        <script>
            function approve(index){
                document.approvalRoute.approveAll.value = index;
                document.approvalRoute.action = "<%=request.getContextPath()%>/approveProposalAll.do";
                document.approvalRoute.submit();
            }
        </script>
    </head>
    <body>
    <html:form  action="/approveProposal.do" method="post">     
        <table width='100%' border='0' cellpadding='0' cellspacing='0' class='tabtable'>
                <tr >
                    <td class='tableheader' height='20%' width='100%'>
                        <%=label%>&nbsp;<bean:message bundle="proposal" key="routing.proposal"/>
                    </td>
                </tr>
                <tr>
                    <td align=left>
                        <font color='red'><html:errors header="" footer=""/></font>
                        <font size=10px>
                    </td>
                </tr>                
                <%String approveAll =(String) request.getAttribute("approveAll");%>
                <%if(approveAll==null) {%>
                <tr>
                    <td align=left class='copybold' height='25'>
                        Please <%=label%> the Proposal
                    </td>
                </tr>
                <tr>
                    <td align=left>
                       <html:textarea property="comments" styleClass="copy" cols="150" rows="5"/>
                    </td>
                </tr>
                <tr>
                    <td>&nbsp;
                    </td>
                </tr>
                <tr>
                    <td align=center>
                       <html:submit property="Save" value="<%=label%>" styleClass="clbutton"/>
                       &nbsp;&nbsp;&nbsp;
                       <html:button property="cancel" value="Back" styleClass="clbutton" onclick="JavaScript:window.history.back();"/>
                    </td>
                   
                </tr>
                <%} else if(approveAll!=null && approveAll.equals("approveAll")){%>
                <tr>
                    <td height='15'>
                    </td>
                </tr>
                <tr>
                    <td align=center class='copybold'>
                       <bean:message bundle="proposal" key="routing.approvalForStop"/>
                    </td>
                </tr>
                <tr>
                    <td>&nbsp;
                    </td>
                </tr>
                <tr>
                    <td align=center>
                    	<!-- JM 10-11-2012 disable ability to approve at all stops
                       <html:button property="ok" value="Yes" styleClass="clbutton" onclick="approve('1')" />
                       	&nbsp;&nbsp;&nbsp; -->
                       <html:button property="cancel" value="No" styleClass="clbutton" onclick="approve('0')"/>
                    </td>
                </tr>
                <tr>
                    <td height='10'>
                    </td>
                </tr>
                <%}%>
                <html:hidden name="approvalRoute" property="actionMode" />
                <html:hidden name="approvalRoute" property="approveAll" />
        </table>
       </html:form>
     </body>
</html>