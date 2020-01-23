<%-- 
    Document   : SyncComments
    Created on : Mar 16, 2010, 2:54:06 PM
    Author     : sharathk
--%>

<%@page contentType="text/xml" pageEncoding="UTF-8"%>
<%@page import="java.util.*, edu.mit.coeus.utils.dbengine.*, edu.mit.coeus.utils.UtilFactory"%>

    <body>
        <%
        //Map map = request.getParameterMap();
        //Set set = map.keySet();
        //Iterator iterator = set.iterator();
        DBEngineImpl dbEngine = new DBEngineImpl();
        Enumeration  enum1 = request.getParameterNames();
        String proposalNumber = request.getParameter("proposalNumber");
        ArrayList commentlist = new ArrayList();
        ArrayList commentIdList = new ArrayList();
        String personId;
        Map map;
        int code = -1;
        try{
        //enum1.nextElement();
        //the enumeration returned by request is NOT in prder, so we cannot just disregard the first element as proposal number
        //First is the Proposal Number. The Query would return empty resultset.
        String paramName;
        while(enum1.hasMoreElements()){
            paramName = enum1.nextElement().toString();
            if(paramName.equals("proposalNumber")){continue;}
            
            personId = request.getParameter((String)paramName);
            if(personId == null || personId.length() == 0) break;
            Vector param = new Vector();
            param.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, proposalNumber));
            param.addElement(new Parameter("REVIEW_NUMBER", DBEngineConstants.TYPE_INT, 1));
            param.addElement(new Parameter("PERSON_ID", DBEngineConstants.TYPE_STRING, personId));
            param.addElement(new Parameter("MENTOR_PER_ID", DBEngineConstants.TYPE_STRING, null));
            
            Vector vecComments = dbEngine.executeRequest("Coeus",
                                            "call GET_PROP_REVIEW_COMMENTS( <<PROPOSAL_NUMBER>>, <<REVIEW_NUMBER>>, <<PERSON_ID>>, <<MENTOR_PER_ID>>, <<OUT RESULTSET rset>>)",
                                            "Coeus", param);
            //out.print("<P"+personId+">"+vecComments.size()+"</P"+personId+">");
            if(commentlist.size() == 0){
                commentlist.addAll(vecComments);
                for(int index=0; index<vecComments.size();index++){
                    map = (Map) vecComments.get(index);
                    if(map.get("CANNED_REVIEW_COMMENT_CODE") != null) {
                        commentIdList.add(((java.math.BigDecimal)map.get("CANNED_REVIEW_COMMENT_CODE")).intValue());
                    }
                }
            }else{
                
                for(int index=0; index<vecComments.size();index++){
                    map = (Map) vecComments.get(index);
                    if(map.get("CANNED_REVIEW_COMMENT_CODE") != null) {
                        code = ((java.math.BigDecimal)map.get("CANNED_REVIEW_COMMENT_CODE")).intValue();
                    }else {
                        //Text Comment
                        commentlist.add(map);
                        continue;
                    }
                    if(!commentIdList.contains(code)){
                        commentIdList.add(code);
                        commentlist.add(map);
                    }
                }
            }
        }

        //Genarate XML comments
        out.print("<table>");
        String description;
        for(int index=0;index<commentlist.size(); index++){
            map = (Map)commentlist.get(index);
            if(map.get("CANNED_REVIEW_COMMENT_CODE") != null) {
                code = ((java.math.BigDecimal)map.get("CANNED_REVIEW_COMMENT_CODE")).intValue();
            }else {
                code = -1;
            }
            description = (String) map.get("COMMENTS");
            out.print("<tr>");
            out.print("<td>"+(code==-1?"":code)+"</td>");
            out.print("<td>"+description+"</td>");
            out.print("</tr>");
        }
        out.print("</table>");
        }catch(NoSuchElementException nseEx){
            UtilFactory.log(nseEx.getMessage(), nseEx, "SyncComments.jsp", "");
        }%>
    </body>
