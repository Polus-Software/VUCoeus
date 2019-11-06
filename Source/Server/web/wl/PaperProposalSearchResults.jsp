<%-- 
    Document   : PaperProposalSearchResults
    Created on : Feb 23, 2010, 11:11:22 AM
    Author     : sharathk
--%>

<%@page import="edu.mit.coeus.search.exception.CoeusSearchException"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="edu.mit.coeus.search.bean.*, java.util.*, edu.mit.coeuslite.utils.SearchUtil, edu.mit.coeus.utils.UtilFactory"%>
<%@page import="edu.mit.coeus.bean.PersonInfoBean, edu.mit.coeuslite.utils.SessionConstants"%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel=stylesheet href="<%=request.getContextPath()%>/coeuslite/mit/utils/css/coeus_styles.css" type="text/css">
        <title>Paper Proposal Search Results</title>
        <script>
            function searchAgain(){
                window.location="PaperProposalSearch.jsp?searchType=<%=session.getAttribute("type")%>";
                parent.resize();
            }

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
        function reviewProposal(proposalNumber, mode){
            parent.location="<%=request.getContextPath()%>/wl/ProposalReview.jsp?proposalNumber="+proposalNumber+"&apprSeq=1&proposalType=<%=session.getAttribute("type")%>&mode="+mode;
        }

    </script>
    <body>
         <%String proposalType = (String)session.getAttribute("type");%>
    <table width="100%" height="100%" border="0" cellpadding="0" cellspacing="5" class="table" align="center">
                <tr align="left" valign="top">
                    <td width="98%" valign="top">
                        <div style="height:450px;overflow : auto;">
                        <table width="98%" height="100%" border="0" cellpadding="0" cellspacing="5" class="tabtable">
                            <tr>
                                <td valign="top" height="25" class="tabtable copybold">
                                    Search Results:
                                </td>
                            </tr>
                            <tr>
                                <td valign="top" align="center">
                                    <table width="98%" border="0" cellpadding="3" cellspacing="0" class="tabtable">
                                        <tr class="theader">
                                            <%if(proposalType.equalsIgnoreCase("completed")){%>
                                                <td align="left" width="10%">Proposal #</td>
                                                <td align="left" width="15%">PI</td>
                                                <td align="left" width="15%">Reviewer</td>
                                                <td align="left" width="40%">Title</td>
                                                <td align="left" width="20%">Sponsor</td>
                                            <%}else {%>
                                                <td align="left" width="10%">Proposal #</td>
                                                <td align="left" width="20%">PI</td>
                                                <td align="left" width="50%">Title</td>
                                                <td align="left" width="20%">Sponsor</td>
                                            <%}%>
                                        </tr>

    <%
    try{
    session = request.getSession( true );
    //out.print(proposalType);
    SearchInfoHolderBean searchInfoHolder = (SearchInfoHolderBean)session.getAttribute("searchinfoholder");
    SearchExecutionBean searchExecution = new SearchExecutionBean(searchInfoHolder);
    boolean searchCriteriaEntered = SearchUtil.fillCriteria(searchExecution, searchInfoHolder, request);
    Hashtable searchResult = null ;
    boolean continueSearch = false;
    try{
    searchResult = searchExecution.executeSearchQuery();
    continueSearch = true;
    }catch(CoeusSearchException csx){
        if(csx.getMessage() != null && csx.getMessage().equals("No rows found with the current selection criteria")){
            continueSearch = true;
        }else{
            throw csx;
        }
    }
    if(continueSearch){
        String searchType = (String)session.getAttribute("type");
        if(searchType != null && searchType.equals("completed")){
            //Search Again for Inst Proposals
            ProcessSearchXMLBean processSearchXML = new ProcessSearchXMLBean("", "COMPLETEDINSTPROPREVIEWSEARCH");
            searchInfoHolder = processSearchXML.getSearchInfoHolder();
            searchExecution = new SearchExecutionBean(searchInfoHolder);
            searchCriteriaEntered = SearchUtil.fillCriteria(searchExecution, searchInfoHolder, request);
            Hashtable searchResult2 = null;
            try{
                searchResult2 = searchExecution.executeSearchQuery();
            }catch(CoeusSearchException csx){
                if(csx.getMessage() != null && csx.getMessage().equals("No rows found with the current selection criteria") && searchResult == null){
                    throw csx;
                }
            }
            if(searchResult!=null && searchResult2 != null){
                //Add searchResult2 to searchResult
                Vector result = (Vector)searchResult.get("reslist");
                Vector result2 = (Vector)searchResult2.get("reslist");
                result.addAll(result2);
                searchResult.put("reslist", result);
            }else if(searchResult2 != null){
                searchResult = searchResult2;
            }
        }
    }
    if(searchResult != null){
    Vector labels, result;
    Map row;
    labels = (Vector)searchResult.get("displaylabels");
    result = (Vector)searchResult.get("reslist");
    String mode = "m";//proposalType.equalsIgnoreCase("completed")?"d":"m";
    String perId = "";
    PersonInfoBean personInfo = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);

    for(int index=0; index<result.size(); index++) {
        row = (Map)result.get(index);
        perId = (String)row.get("PERSON_ID");
        if(proposalType.equalsIgnoreCase("completed") && !personInfo.getPersonID().equalsIgnoreCase(perId)){
            mode = "d";
        }else {
            mode = "m";
        }
        %>
        <tr class="rowLine">
            <%if(proposalType.equalsIgnoreCase("completed")){%>
            <td><a href="javascript:reviewCompletedProposal('<%=row.get("PROPOSAL_NUMBER")%>','<%=row.get("PERSON_ID")%>','<%=mode%>')"><%=row.get("PROPOSAL_NUMBER")%></a></td>
            <%}else{%>
            <td><a href="javascript:reviewProposal('<%=row.get("PROPOSAL_NUMBER")%>','<%=mode%>')"><%=row.get("PROPOSAL_NUMBER")%></a></td>
            <%}%>
            <td><%
            if(proposalType != null && (proposalType.equalsIgnoreCase("paper") || proposalType.equalsIgnoreCase("completed"))) {
                out.print(row.get("PI_NAME"));
            }else{
                out.print(row.get("PERSON_NAME"));
            }
            %></td>
            <%if(proposalType.equalsIgnoreCase("completed")){%>
            <td><%=row.get("FULL_NAME")%></td>
            <%}%>
            <td><%=row.get("TITLE")%></td>
            <td><%=row.get("SPONSOR_NAME")%></td>
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
                        </div>
                    </td>
                </tr>
                <tr>
                    <td height="25">
                        <input type="button" style="width:100" value="Search Again" onclick="searchAgain()"/>
                    <input type="button" style="width:100" value="Cancel" onclick="cancel()"/>
                    </td>
                </tr>
    </table>
    </body>
</html>
