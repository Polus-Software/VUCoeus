<%-- 
    Document   : ReviewComments
    Created on : Feb 4, 2010, 4:09:15 PM
    Author     : sharathk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="edu.mit.coeus.utils.UtilFactory, edu.mit.coeus.utils.dbengine.*, java.sql.*, java.util.*"%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel=stylesheet href="<%=request.getContextPath()%>/coeuslite/mit/utils/css/coeus_styles.css" type="text/css">
        <title>JSP Page</title>
        <script>
            //JIRA COEUSDEV-610 - START
            var sponsorSpecificStartIndex = -1;

            function commentCode16Clicked(){
                var selected = document.reviewComments.elements['check16'].checked;
                //disable all Sponsor Specific Comments when Code 16 is Selected
                //Enable all Sponsor Specific Comments when Code 16 is DeSelected
                disableSponsorSpecificComments(selected);
            }

            function sponsorSpecificCommentClicked(){
                formsLength = document.reviewComments.length;
                for(i=sponsorSpecificStartIndex; i<formsLength ; i++){
                    if(document.reviewComments.elements['check'+i].checked){
                        disableCommentCode16(true);
                        return;
                    }
                }
                disableCommentCode16(false);
            }
            
            //below methods are also called from parent window
            function disableSponsorSpecificComments(disable){
                formsLength = document.reviewComments.length;
                for(i=sponsorSpecificStartIndex; i<formsLength ; i++){
                    //document.reviewComments.elements['check'+i].checked = false;
                    document.reviewComments.elements['check'+i].disabled = disable;
                }
            }

            function disableCommentCode16(disable){
                //document.reviewComments.elements['check16'].disabled = disable;
            }

            function getSponsorSpecificStartIndex(){
                return sponsorSpecificStartIndex;
                //alert("okay");
            }
            function testcall() {
                alert("okay");
            }
            //JIRA COEUSDEV-610 - END
        </script>
    </head>
    <body>
        <form name="reviewComments">
            <!--table width="100%" height="100%" border="0" cellpadding="0" cellspacing="5" class="table" align="center">
                <tr align="left" valign="top">
                    <td width="98%" valign="top"-->

                        <!--table width="100%" height="100%" border="0" cellpadding="0" cellspacing="5" class="tabtable">
                            <tr>
                                <td valign="top" align="center"-->
                                    <table width="100%" border="0" cellpadding="3" cellspacing="0" class="tabtable">
                                        <tr class="theader">
                                            <td colspan="3" align="center">Select Review Comments</td>
                                        </tr>
                                        <%
                                        DBEngineImpl dbEngine = new DBEngineImpl();
                                        try {
                                            Vector result = null;
                                            int listSize = 0;
                                            boolean containsSponsorSpecific = false;
                                            if (dbEngine != null) {
                                                Vector param = new Vector();
                                                param.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, request.getParameter("proposalNumber")));
                                                result = dbEngine.executeRequest("Coeus",
                                                        "call GET_WL_CANNED_COMMENTS(<<PROPOSAL_NUMBER>>, <<OUT RESULTSET rset>> )",
                                                        "Coeus", param);
                                                if (result != null) {
                                                    listSize = result.size();
                                                    //JIRA COEUSDEV-573 - START
                                                   int lastIndex = 0;
                                                   boolean sponsorSpecific = true;
                                                   for (int index = 0; index < listSize; index++) {
                                                       HashMap map = (HashMap) result.get(index);
                                                       if (((String) map.get("SPONSOR_SPECIFIC")).equalsIgnoreCase("N")) {
                                                           //Initial Sort
                                                           if (sponsorSpecific == true && (index != lastIndex)) {
                                                               result.remove(index);
                                                               result.add(lastIndex, map);
                                                           } else {
                                                               sponsorSpecific = false;
                                                           }
                                                           lastIndex = lastIndex + 1;
                                                       } else {
                                                           sponsorSpecific = true;
                                                           containsSponsorSpecific = true;
                                                       }
                                                   }
                                                    //JIRA COEUSDEV-573 - END
                                                }
                                            }
                                            if (listSize > 0) {
                                                boolean sponsorSpecific = false;
                                                String functionCall = "";
                                                java.math.BigDecimal commentCode;
                                                for (int index = 0; index < listSize; index++) {
                                                    HashMap map = (HashMap) result.get(index);

                                                    if(!sponsorSpecific && ((String)map.get("SPONSOR_SPECIFIC")).equalsIgnoreCase("Y")){
                                                        sponsorSpecific = true;
                                                        out.print("<script>sponsorSpecificStartIndex = "+index+";</script>");
                                                        out.print("<tr class='theader'>");
                                                        out.print("<td colspan='3'>Sponsor Specific</td>");
                                                        out.print("</tr>");
                                                    }
                                                    commentCode = (java.math.BigDecimal)map.get("CANNED_REVIEW_COMMENT_CODE");
                                                    if(commentCode.intValue() == 16){
                                                        functionCall = "commentCode16Clicked()";
                                                    }else if(sponsorSpecific){
                                                        functionCall = "sponsorSpecificCommentClicked()";
                                                    }else {
                                                        functionCall = null;
                                                    }
                                        %>
                                        <tr class="rowLine">
                                            <td width="5%"><input value="<%=map.get("CANNED_REVIEW_COMMENT_CODE")%>:<%=map.get("DESCRIPTION")%>" type="checkbox" name="check<%=map.get("CANNED_REVIEW_COMMENT_CODE")%>" <%=functionCall==null?"":"onclick='"+functionCall+"'"%> <%=(commentCode.intValue() == 16 && containsSponsorSpecific)?"disabled":""%>/></td>
                                            <td width="5%"><%=map.get("CANNED_REVIEW_COMMENT_CODE")%></td>
                                            <td width="90%"><%=map.get("DESCRIPTION")%></td>

                                        </tr>
                                        <%
                                                }//End For
                                                }//End If
                                            }catch(Exception ex){
                                                out.print(ex.getMessage());
                                                UtilFactory.log(ex.getMessage(), ex, "ProposalComments.jsp", "");
                                            }
                                        %>
                                    </table>
                                <!--/td>
                            </tr>
                        </table-->
                    <!--/td>
                </tr>
            </table-->
        </form>
    </body>
</html>
