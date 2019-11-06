<%-- 
    Document   : PaperProposalSearch
    Created on : Feb 22, 2010, 4:43:03 PM
    Author     : sharathk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="edu.mit.coeus.search.bean.ProcessSearchXMLBean"%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel=stylesheet href="<%=request.getContextPath()%>/coeuslite/mit/utils/css/coeus_styles.css" type="text/css">
        <title>Paper Proposal Search</title>
        <script>
            function validate(){
                /*propNum = document.getElementById('P.PROPOSAL_NUMBER').value;
                piName = document.getElementById('P.PI_NAME').value;
                leadUnit = document.getElementById('P.LEAD_UNIT').value;
                sponsorCode = document.getElementById('P.SPONSOR_CODE').value;
                title = document.getElementById('P.TITLE').value;
                */
                //alert(propNum);
                var propNum = document.getElementById('P.PROPOSAL_NUMBER').value;
                document.getElementById('PAD.INST_PROPOSAL_NUMBER').value = propNum;
                parent.resize();
                return true;
            }

            function cancel(){
                 parent.cancelSearch();
            }
        </script>
    </head>
    <body>
        <!-- PROPOSALLOGSEARCH -->
        <%
                String searchType = request.getParameter("searchType");
                String searchName = null;
                String propNumber, piName, leadUnit, sponsorCode, status = null, title;
                String message = null;
                String searchHeader = "";
                if(searchType.equalsIgnoreCase("paper")) {
                    searchHeader = "Paper";
                    searchName = "PROPREVIEWLOGSEARCH";//JIRA COEUSDEV-545
                    propNumber = "P.PROPOSAL_NUMBER";
                    piName = "P.PI_NAME";
                    leadUnit = "P.LEAD_UNIT";
                    sponsorCode = "P.SPONSOR_CODE";
                    status = "P.LOG_STATUS";
                    title = "P.TITLE";
                }else if(searchType.equalsIgnoreCase("electronic")){
                    searchHeader = "Electronic";
                    searchName = "ALL_PROPOSALDEVSEARCHNOROLES";
                    propNumber = "P.PROPOSAL_NUMBER";
                    piName = "I.PERSON_NAME";
                    leadUnit = "U.UNIT_NUMBER";
                    sponsorCode = "P.SPONSOR_CODE";
                    //status = "P.CREATION_STATUS_CODE";
                    title = "P.TITLE";

                }else {
                    //Completed
                    searchHeader = "Completed";
                    //message = "currently searches completed proposals from Proposal_Log only";
                    searchName = "COMPLETEDPROPREVIEWSEARCH";
                    propNumber = "P.PROPOSAL_NUMBER";
                    piName = "P.PI_NAME";
                    leadUnit = "P.LEAD_UNIT";
                    sponsorCode = "P.SPONSOR_CODE";
                    //status = "P.LOG_STATUS";
                    title = "P.TITLE";
                }

                ProcessSearchXMLBean processSearchXML = new ProcessSearchXMLBean("", searchName);
                session.setAttribute("searchinfoholder", processSearchXML.getSearchInfoHolder());
                session.setAttribute("displayList", processSearchXML.getSearchInfoHolder());
                request.setAttribute("search", "true");
                //session.setAttribute("type", "proposalLogSearch");
                session.setAttribute("type", searchType);
        %>
        <!--a href="PaperProposalSearchResults.jsp?type=proposalLogSearch&P.PROPOSAL_NUMBER=00071019">Dummy Search</a-->
        <form name="propSearch" action="PaperProposalSearchResults.jsp" onsubmit="return validate();">
            <!--table width="100%" height="100%" border="0" cellpadding="0" cellspacing="5" class="table" align="center">
                <tr align="left" valign="top">
                    <td valign="top"-->
                        <table width="100%" align="center" border="0" cellpadding="3" cellspacing="0" class="tabtable">
                            <tr class="theader">
                                <td colspan="4"><%=searchHeader%> Proposal Search</td>
                            </tr>
                            <%if(message != null){%>
                            <tr>
                                <td colspan="4">
                                    <font color="red">*<%=message%></font>
                                </td>
                            </tr>
                            <%}%>
                            <tr>
                                <td>Proposal Number</td>
                                <td><input name="P.PROPOSAL_NUMBER" type="text"><input type="hidden" name="PAD.INST_PROPOSAL_NUMBER"/></td>
                                <td>PI name</td>
                                <td><input name="<%=piName%>" type="text"></td>
                            </tr>
                            <tr>
                                <td>Unit</td>
                                <td><input name="<%=leadUnit%>" type="text"></td>
                                <td>Sponsor Code</td>
                                <td><input name="<%=sponsorCode%>" type="text"></td>
                            </tr>
                            <%if(status != null){%>
                            <tr>
                                <td>Proposal Status</td>
                                <td>
                                    <select name="<%=status%>">
                                        <option value="">&nbsp;</option>
                                        <option value="V">Void</option>
                                        <option value="P">Pending</option>
                                        <option value="M">Merged</option>
                                        <option value="T">Temporary</option>
                                        <option value="S">Submitted</option>
                                    </select>
                                </td>
                            </tr>
                            <%}%>
                            <tr>
                                <td>Title</td>
                                <td colspan="3"><textarea name="<%=title%>" rows="4" cols="40"></textarea></td>
                            </tr>
                        </table>
                    <input type="submit" style="width:100" value="Search"/>
                    <input type="button" style="width:100" value="Cancel" onclick="cancel()"/>
                    <!--/td>
                </tr>
            </table-->
        </form>

        
    </body>
</html>
