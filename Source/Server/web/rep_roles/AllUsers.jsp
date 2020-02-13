<%-- 
    Document   : UsersSearch
    Created on : May 10, 2010, 11:54:22 AM
    Author     : Majid
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="edu.mit.coeus.search.bean.*, java.util.*, edu.mit.coeuslite.utils.SearchUtil"%>
<%@page import="edu.mit.coeus.bean.PersonInfoBean, edu.mit.coeuslite.utils.SessionConstants"%>
<%@page import="edu.mit.coeus.utils.UtilFactory, edu.mit.coeus.utils.dbengine.*, java.sql.*"%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel=stylesheet href="<%=request.getContextPath()%>/coeuslite/mit/utils/css/coeus_styles.css" type="text/css">
        <title>All Users in Units</title>
        <script>
            function cancel(){
                 parent.cancelSearch();
            }
        </script>
    </head>
    <script>
        function reviewCompletedProposal(proposalNumber, personId, mode){
            //parent.location="ProposalReview.jsp?proposalNumber=00000758&routingNumber=161&apprSeq=1&proposalType=electronic";
            parent.location="<%=request.getContextPath()%>/wl/ProposalReview.jsp?proposalNumber="+proposalNumber+"&apprSeq=1&proposalType=<%=session.getAttribute("type")%>&perId="+personId+"&mode="+mode;
        }
        function userInfo(userId){
            parent.location="<%=request.getContextPath()%>/roles/UserRoles.jsp?userId="+ userId;
        }

    </script>
    <body>
         <%String proposalType = (String)session.getAttribute("type");%>
         <%--
         <table width="100%">
	<tr valign="top" align="center">
		<td>
			<img src="<%=request.getContextPath()%>/coeusliteimages/header_coeus2.gif" width="90%" height="80">

		</td>
	</tr>
</table> --%>
   <table width="1000" height="100%" border="0" cellpadding="0" cellspacing="5" class="table" align="center">
            <tr align="left" valign="top">
                <td width="98%" valign="top">

                    <!-- header START -->
                    <table width="100%" border="0" cellpadding="0" cellspacing="0" STYLE="background-image:url('<%=request.getContextPath()%>/coeusliteimages/header_background.gif');border:0">
                        <tr>
                            <td><img src="<%=request.getContextPath()%>/coeusliteimages/header_coeus2.gif" width="675" height="42"></td>
                            <td class="copysmallwhite" align="right" nowrap>
                                <%PersonInfoBean personInfo = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
                                        out.print(personInfo.getFullName());
                                %>&nbsp;&nbsp;&nbsp;
                            </td>
                        </tr>
                        <tr>
                            <td height="25" align="left" nowrap>
                               
                            </td>
                            <td height="25" align="right" nowrap>
                                <a href="<%=request.getContextPath()%>"><font size='2' color='#D1E5FF'><u>Back to CoeusLite</u></font></a>&nbsp;&nbsp;&nbsp;
                            </td>
                        </tr>
                    </table>
                    <!-- header END -->
            </tr>









                <tr align="left" valign="top">
                    <td width="98%" valign="top">
             <%--           <div style="height:100%;overflow : auto;"> --%>
                        <table align="center" width="100%" border="0" cellspacing="0" cellpadding="2" class="tabtable">
                            <tr>
                                <td valign="top" height="25" class="tabtable copybold">
                                    All Users in Units:
                                </td>
                            </tr>
                            <tr>
                                <td valign="top" align="center">
                                    <table width="98%" border="0" cellpadding="3" cellspacing="0" class="tabtable">
                                        <tr class="theader">
                                                <td align="left" width="10%">ID #</td>
                                                <td align="left" width="15%">USERNAME</td>
                                                <td align="left" width="15%">UNITNUMBER</td>
                                                <td align="left" width="40%">UNITNAME</td>
                                        </tr>

    
   <%
                                                        DBEngineImpl dbEngine = new DBEngineImpl();
                                                        try {
                                                            Vector param = new Vector();
                                                            Vector result = null;
                                                            int listSize = 0;

                                                            if (dbEngine != null) {
                                                                result = new Vector();
                                                                result = dbEngine.executeRequest("Coeus",
                                                                        "call DW_GET_ALL_USERS( <<OUT RESULTSET rset>> )",
                                                                        "Coeus", param);
                                                                if (result != null) {
                                                                    listSize = result.size();
                                                                }
                                                            }

                                                            if (listSize > 0) {
                                                                for (int index = 0; index < listSize; index++) {
                                                                    HashMap map = (HashMap) result.get(index);
                                                                    /*if (index % 2 == 0) {
                                                                    out.print("<tr class='rowLine'>");
                                                                    } else {
                                                                    out.print("<tr class='rowline' bgcolor='#9DBFE9'>");
                                                                    }*/

                                                %>
        <tr class="rowLine">
            
            
            <td><a href="<%=request.getContextPath()%>/rep_roles/UserRoles.jsp?userId=<%=map.get("USER_ID")%>"><%=map.get("USER_ID")%></a></td>

           
            <td>
               <%=map.get("USER_NAME")%>
            </td>
            
            <td><%=map.get("PERSON_ID")%></td>
           
            <td><%=map.get("UNIT_NAME")%></td>
            
        </tr>
        <%
    }
    }else {
        //Search Returned no results
        out.print("<tr><td colspan='4'>Search returns zero results<td></tr>");
    }
    }catch(Exception ex){
        //out.println(ex.getMessage());
        out.print("<tr><td colspan='4'>"+ex.getMessage()+"<td></tr>");
        UtilFactory.log(ex.getMessage(), ex, "PaperProposalSearchResults.jsp", "");
    }
    %>
                                    </table>
                                </td>
                            </tr>
                        </table>
                     <%--      </div>   --%>
                    </td>
                </tr>
                
    </table>
    </body>
</html>
