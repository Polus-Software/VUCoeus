<%-- 
    Document   : SaveProposalReview
    Created on : Feb 3, 2010, 11:37:03 AM
    Author     : sharathk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="edu.mit.coeus.utils.UtilFactory, edu.mit.coeus.utils.dbengine.*, edu.mit.coeus.bean.PersonInfoBean, java.util.*"%>
<%@page import="edu.mit.coeuslite.utils.SessionConstants, edu.mit.coeus.utils.CoeusFunctions, edu.mit.coeus.exception.CoeusException"%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Proposal Review Save</title>
    </head>
    <body>
        <%
            DBEngineImpl dbEngine = new DBEngineImpl();
            String retLink = null;
            String proposalNumber = null, routingNumber, comments, updateUser, personId = null, acType, apprSeq, reviewperId;
            int reviewNumber = 0, reviewActionCode = 0, reviewRoleCode = 0, timeSpent = 0, timeSpentDLC = 0, submVehicleCode = 0;
            String deanoffice = null;
            java.sql.Timestamp updateTimestamp = null;
            CoeusFunctions coeusFunctions = new CoeusFunctions();
            try {
            proposalNumber = request.getParameter("proposalNumber");
            routingNumber = request.getParameter("routingNumber");
            apprSeq = request.getParameter("apprSeq");
            apprSeq = "1";
            reviewNumber = 1;
                //if(apprSeq != null || apprSeq.length() > 0) {
                //    reviewNumber = Integer.parseInt(apprSeq);
                //}//else{
                 //   reviewNumber = 1;
                //}

                /*if (routingNumber == null || routingNumber.length() == 0) {
                    Vector propCountParam = new Vector();
                    propCountParam.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, proposalNumber));
                    Vector propCountResult = dbEngine.executeFunctions("Coeus", "{ <<OUT INTEGER COUNT>> = call FN_GET_REVIEW_PROP_COUNT( <<PROPOSAL_NUMBER>>)}", propCountParam);
                    Map map = (Map) propCountResult.get(0);
                    String commentCount = (String) map.get("COUNT");
                    int newRoutingNum = Integer.parseInt(commentCount);
                    reviewNumber = newRoutingNum + 1;
                }*/
                PersonInfoBean personInfo = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
                updateUser = personInfo.getUserId();
                personId = personInfo.getPersonID();

                //COEUSDEV-574 - START
                /**
                * if the review is Agency Liason, another person should have made a CA review
                * if the review is CA, check if CA review already Exists
                */
                retLink = "/wl/ProposalReview.jsp?proposalNumber=" + proposalNumber + "&routingNumber=" + routingNumber + "&apprSeq="+apprSeq+"&proposalType="+request.getParameter("proposalType");
                Vector paramCheck = new Vector();
                Vector resultCheck = null;
                paramCheck.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, proposalNumber));

                reviewRoleCode = Integer.parseInt(request.getParameter("reviewRole"));

                if (dbEngine != null) {
                    resultCheck = new Vector(3, 2);
                    resultCheck = dbEngine.executeRequest("Coeus",
                            "call GET_ALL_PROP_REVIEW_DETAILS( <<PROPOSAL_NUMBER>>, <<OUT RESULTSET rset>> )",
                            "Coeus", paramCheck);
                }
                if (resultCheck != null && resultCheck.size() > 0) {
                    //out.print("<br> size"+resultCheck.size());
                    Map map;
                    String personIdDB = null;
                    String personIdOtherCAReview = null;
                    boolean hasOtherReview = false; //Code = 3
                    boolean hasCAReview = false; //Code = 1
                    boolean hasLAReview = false;
                    int reviewRoleCodeDB;
                    for (int i = 0; i < resultCheck.size(); i++) {
                        map = (Map) resultCheck.get(i);
                        personIdDB = map.get("PERSON_ID").toString().trim();
                        reviewRoleCodeDB = ((java.math.BigDecimal) map.get("REVIEW_ROLE_CODE")).intValue();
                        if (reviewRoleCodeDB == 1) {
                            hasCAReview = true;
                            personIdOtherCAReview = personIdDB;
                        }else if(reviewRoleCodeDB == 2){
                            hasLAReview = true;
                        } else if (reviewRoleCodeDB == 3) {
                            hasOtherReview = true;
                            personIdOtherCAReview = personIdDB;
                        }
                    }

                    if(hasOtherReview){
                        if(personIdOtherCAReview.equals(personId)){
                            if(reviewRoleCode == 2){//Trying to Update CA/Other Review as Agency Liason
                                //Throw Error
                                session.setAttribute("error", "This Proposal has Other Review. Cannot have more reviews.");
                                //response.sendRedirect(request.getContextPath() +retLink);
                                throw new CoeusException("validationError");
                            }
                        }else {
                            //throw Error
                            session.setAttribute("error", "This Proposal has Other Review. Cannot have more reviews.");
                            //response.sendRedirect(request.getContextPath() +retLink);
                            throw new CoeusException("validationError");
                        }
                    }else if(hasCAReview){ //Doesn't have Other Review
                       if(personIdOtherCAReview.equals(personId)){
                           if(reviewRoleCode == 2){//Trying to Update CA Review as Agency Liason
                               //Throw Error
                                session.setAttribute("error", "Cannot update CA Review as Agency Liason review.");
                                //response.sendRedirect(request.getContextPath() +retLink);
                                throw new CoeusException("validationError");
                           }else if(reviewRoleCode == 3 && resultCheck.size() > 1){//Trying to Update CA Review as Other
                               //Cannot update to other inless thats the ONLY Review
                                session.setAttribute("error", "Cannot update to Other review. Agency Liason Comment Exists");
                                //response.sendRedirect(request.getContextPath() +retLink);
                                throw new CoeusException("validationError");
                           }
                       }else{
                           if(reviewRoleCode != 2){//Can Only insert as Agency Liason
                                session.setAttribute("error", "This Proposal already has CA Review.");
                                //response.sendRedirect(request.getContextPath() +retLink);
                                throw new CoeusException("validationError");
                           }
                       }
                    }
                    //JIRA COEUSDEV 724 - START
                    if(hasLAReview && reviewRoleCode == 1){
                        //can't edit CA Review after LA review
                        session.setAttribute("error", "Cannot update CA review. Agency Liason Comment Exists");
                        throw new CoeusException("validationError");
                    }
                    //JIRA COEUSDEV 724 - END
                }//End if
                else if(reviewRoleCode == 2){ //No Review Exists for this proposal. Cannot save Agency Liason review (Code =2)
                    //Throw Error
                    session.setAttribute("error", "This Proposal doesn't have CA Review");
                    //response.sendRedirect(request.getContextPath() +retLink);
                    throw new CoeusException("validationError");
                }
                //COEUSDEV-574 - END

                //COEUSDEV-635 - START
                Vector param1 = new Vector();
                param1.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, proposalNumber));
                param1.addElement(new Parameter("REVIEW_NUMBER", DBEngineConstants.TYPE_INT, Integer.parseInt(apprSeq)));
                param1.addElement(new Parameter("PERSON_ID", DBEngineConstants.TYPE_STRING, personId));
                param1.add(new Parameter("MENTOR_PER_ID", DBEngineConstants.TYPE_STRING, null));

                Vector vecComments = dbEngine.executeRequest("Coeus",
                                            "call GET_PROP_REVIEW_COMMENTS( <<PROPOSAL_NUMBER>>, <<REVIEW_NUMBER>>, <<PERSON_ID>>, <<MENTOR_PER_ID>>, <<OUT RESULTSET rset>>)",
                                            "Coeus", param1);
                 int size = vecComments.size();
                 boolean hasCannedComment = false;
                 int numberOfCannedComments = 0;
                 String commentsToDelete = request.getParameter("deleteCommentNum");
                 String arrCommentsToDel[] = commentsToDelete.split(",");
                 List lstCommentsToDel = new ArrayList();
                 if(commentsToDelete != null && commentsToDelete.length() > 0){
                    for(int index = 0; index < arrCommentsToDel.length; index++) {
                        lstCommentsToDel.add(new Integer(arrCommentsToDel[index]));
                    }
                 }

                 java.math.BigDecimal commentCode;
                 for (int index = 0; index < size; index++) {
                    Map map = (Map) vecComments.get(index);
                    if(map.get("CANNED_REVIEW_COMMENT_CODE") != null) {
                        commentCode = (java.math.BigDecimal)map.get("COMMENT_NUMBER");
                        //Don't include Canned Comments which is to be deleted.
                        if(lstCommentsToDel.indexOf(new Integer(commentCode.intValue())) == -1){
                            //Does not contain in to be deleted list
                            numberOfCannedComments = numberOfCannedComments + 1;
                            break;
                        }
                    }
                 }

                String strComment = "";
                int cCount = 0, cIndex;
                 while (strComment != null) {
                    strComment = request.getParameter("comment" + cCount++);
                    if (strComment == null) {
                        break; //no Comments
                    }
                    cIndex = strComment.indexOf("~");
                    if (cIndex > 0) {
                        //Canned Comment
                        hasCannedComment = true;
                        numberOfCannedComments = numberOfCannedComments + 1;
                        //break;
                    }
                }
                
                if(numberOfCannedComments == 0) {
                    //Raise Error
                    session.setAttribute("error", "Proposal should have atleast one Canned Comment");
                    //response.sendRedirect(request.getContextPath() +retLink);
                    throw new CoeusException("validationError");
                }
                //COEUSDEV-635 - END

                //long time = new Date().getTime();
                java.sql.Timestamp timestamp = coeusFunctions.getDBTimestamp();
                Vector param = new Vector();
                Vector procedures = new Vector();
                Vector result = null;
                ProcReqParameter procReqParameter = new ProcReqParameter();
                String sql = null;
                acType = request.getParameter("acType");
                if (acType != null && acType.equals("U")) {
                    updateTimestamp = (java.sql.Timestamp) session.getAttribute("updateTimestamp");
                }
                reviewperId = request.getParameter("reviwerPerId");

                if(reviewperId == null){//update Review Details. Not mentor Comments
                    reviewActionCode = Integer.parseInt(request.getParameter("reviewAction"));
                    reviewRoleCode = Integer.parseInt(request.getParameter("reviewRole"));
                    timeSpent = Integer.parseInt(request.getParameter("timeSpent"));
                    timeSpentDLC = Integer.parseInt(request.getParameter("timeSpentDLC"));
                    submVehicleCode = Integer.parseInt(request.getParameter("submissionVehicle"));
                    deanoffice = request.getParameter("reviewdbyDeanoff");
                    if (deanoffice != null && deanoffice.equalsIgnoreCase("on")) {
                        deanoffice = "Y";
                    } else {
                        deanoffice = "N";
                    }
                    comments = request.getParameter("reviewComments");
                    
                    param.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, proposalNumber));
                    param.addElement(new Parameter("REVIEW_NUMBER", DBEngineConstants.TYPE_INT, reviewNumber));
                    param.addElement(new Parameter("ROUTING_NUMBER", DBEngineConstants.TYPE_STRING, routingNumber));
                    param.addElement(new Parameter("REVIEW_ACTION_CODE", DBEngineConstants.TYPE_INT, new Integer(reviewActionCode)));
                    param.addElement(new Parameter("REVIEW_ROLE_CODE", DBEngineConstants.TYPE_INT, new Integer(reviewRoleCode)));
                    param.addElement(new Parameter("REVIEW_TIME_SPENT_CODE", DBEngineConstants.TYPE_INT, new Integer(timeSpent)));
                    param.addElement(new Parameter("TIME_SPENT_ASSIS_DLC_CODE", DBEngineConstants.TYPE_INT, new Integer(timeSpentDLC)));
                    param.addElement(new Parameter("SUBMISSION_VEHICLE_CODE", DBEngineConstants.TYPE_INT, new Integer(submVehicleCode)));
                    param.addElement(new Parameter("REVIEWED_DEAN_OFFICE_FLAG", DBEngineConstants.TYPE_STRING, deanoffice));
                    param.addElement(new Parameter("COMMENTS", DBEngineConstants.TYPE_STRING, comments));
                    param.addElement(new Parameter("PERSON_ID", DBEngineConstants.TYPE_STRING, personId));
                    param.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, timestamp));
                    param.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, updateUser));
                    param.addElement(new Parameter("AW_PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, proposalNumber));
                    param.addElement(new Parameter("AW_REVIEW_NUMBER",DBEngineConstants.TYPE_INT, reviewNumber));
                    param.addElement(new Parameter("AW_PERSON_ID", DBEngineConstants.TYPE_STRING, personId));
                    param.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, acType != null && acType.equals("U") ? updateTimestamp : timestamp));
                    param.addElement(new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, acType));

                    sql = "call UPD_PROP_REVIEW_DETAILS( <<PROPOSAL_NUMBER>>, <<REVIEW_NUMBER>>, <<ROUTING_NUMBER>>, <<REVIEW_ACTION_CODE>>, <<REVIEW_ROLE_CODE>>,"
                            + " <<REVIEW_TIME_SPENT_CODE>>, <<TIME_SPENT_ASSIS_DLC_CODE>>, <<SUBMISSION_VEHICLE_CODE>>, <<REVIEWED_DEAN_OFFICE_FLAG>>,"
                            + " <<COMMENTS>>, <<PERSON_ID>>, <<UPDATE_TIMESTAMP>>, <<UPDATE_USER>>, <<AW_PROPOSAL_NUMBER>>, <<AW_REVIEW_NUMBER>>,"
                            + " <<AW_PERSON_ID>>, <<AW_UPDATE_TIMESTAMP>>, <<AC_TYPE>> )";

                    procReqParameter.setDSN("Coeus");
                    procReqParameter.setParameterInfo(param);
                    procReqParameter.setSqlCommand(sql.toString());
                    procedures.add(procReqParameter);

                }//End If-Not Mentor Comments

                //Comments - START
                //Delete Comments - START
                String strCommentsToDelete = request.getParameter("deleteCommentNum");
                String reviwerPerId = request.getParameter("reviwerPerId");
                String mentorPerId = null;
                //out.print("Comments To Delete : " + strCommentsToDelete);
                if (reviwerPerId != null && reviwerPerId.length() > 0) {
                    mentorPerId = personId;
                    personId = reviwerPerId;
                }
                //if (reviwerPerId != null && reviwerPerId.length() > 0) {
                if (strCommentsToDelete != null && strCommentsToDelete.length() > 0) {

                    String arrCommentsToDelete[] = strCommentsToDelete.split(",");
                    Vector delCommentParam;
                    for (int index = 0; index < arrCommentsToDelete.length; index++) {
                        delCommentParam = new Vector();
                        delCommentParam.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, proposalNumber));
                        delCommentParam.addElement(new Parameter("REVIEW_NUMBER", DBEngineConstants.TYPE_INT, reviewNumber));
                        delCommentParam.addElement(new Parameter("PERSON_ID", DBEngineConstants.TYPE_STRING, personId));
                        delCommentParam.addElement(new Parameter("COMMENT_NUMBER", DBEngineConstants.TYPE_INT, new Integer(arrCommentsToDelete[index])));
                        delCommentParam.addElement(new Parameter("CANNED_REVIEW_COMMENT_CODE", DBEngineConstants.TYPE_INT, null));
                        delCommentParam.addElement(new Parameter("COMMENTS", DBEngineConstants.TYPE_STRING, null));
                        delCommentParam.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, timestamp));
                        delCommentParam.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, updateUser));
                        delCommentParam.addElement(new Parameter("MENTOR_PERSON_ID", DBEngineConstants.TYPE_STRING, mentorPerId));

                        delCommentParam.addElement(new Parameter("AW_PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, proposalNumber));
                        delCommentParam.addElement(new Parameter("AW_REVIEW_NUMBER", DBEngineConstants.TYPE_INT, reviewNumber));
                        delCommentParam.addElement(new Parameter("AW_PERSON_ID", DBEngineConstants.TYPE_STRING, personId));
                        delCommentParam.addElement(new Parameter("AW_COMMENT_NUMBER", DBEngineConstants.TYPE_INT, new Integer(arrCommentsToDelete[index])));
                        delCommentParam.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, acType != null && acType.equals("U") ? updateTimestamp : timestamp));
                        delCommentParam.addElement(new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, "D"));

                        sql = "call UPD_PROP_REVIEW_COMMENTS( <<PROPOSAL_NUMBER>>, <<REVIEW_NUMBER>>, <<PERSON_ID>>, <<COMMENT_NUMBER>>,"
                                + " <<CANNED_REVIEW_COMMENT_CODE>>, <<COMMENTS>>, <<UPDATE_TIMESTAMP>>, <<UPDATE_USER>>, <<MENTOR_PERSON_ID>>,"
                                + " <<AW_PROPOSAL_NUMBER>>, <<AW_REVIEW_NUMBER>>, <<AW_PERSON_ID>>, <<AW_COMMENT_NUMBER>>, <<AW_UPDATE_TIMESTAMP>>, <<AC_TYPE>> )";

                        procReqParameter = new ProcReqParameter();
                        procReqParameter.setDSN("Coeus");
                        procReqParameter.setParameterInfo(delCommentParam);
                        procReqParameter.setSqlCommand(sql.toString());
                        procedures.add(procReqParameter);
                    }
                }
                //Delete Comments - END

                //Insert Comments
                int maxCommentNum = -1;
                Vector maxCommentParam = new Vector();
                maxCommentParam.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, proposalNumber));
                maxCommentParam.addElement(new Parameter("REVIEW_NUMBER", DBEngineConstants.TYPE_INT, reviewNumber));
                maxCommentParam.addElement(new Parameter("PERSON_ID", DBEngineConstants.TYPE_STRING, personId));
                result = dbEngine.executeFunctions("Coeus", "{ <<OUT INTEGER COUNT>> = call FN_GET_MAX_PRC_COMM_NUM( <<PROPOSAL_NUMBER>>, <<REVIEW_NUMBER>>, <<PERSON_ID>>)}", maxCommentParam);
                Map map = (Map) result.get(0);
                String commentCount = (String) map.get("COUNT");
                maxCommentNum = Integer.parseInt(commentCount);

                Integer id = null;
                String comment = "";
                int count = 0, index = -1;
                Vector commentParam;
                while (comment != null) {
                    comment = request.getParameter("comment" + count++);
                    if (comment == null) {
                        break; //no Comments
                    }
                    index = comment.indexOf("~");
                    if (index == 0) {
                        //text Comment
                        id = null;
                        comment = comment.substring(1);
                        //COEUSDEV-574 - Check for null - START
                        if(comment.trim().length() == 0){
                            //No Comments Entered.
                            continue;
                        }
                        //COEUSDEV-574 - Check for null - END
                    } else {
                        //Canned Comment
                        id = new Integer(comment.substring(0, index));
                        comment = comment.substring(index + 1);
                    }
                    //out.print("ID == null" + (id == null));
                    commentParam = new Vector();
                    commentParam.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, proposalNumber));
                    commentParam.addElement(new Parameter("REVIEW_NUMBER", DBEngineConstants.TYPE_INT, reviewNumber));
                    commentParam.addElement(new Parameter("PERSON_ID", DBEngineConstants.TYPE_STRING, personId));
                    commentParam.addElement(new Parameter("COMMENT_NUMBER", DBEngineConstants.TYPE_INT, new Integer(maxCommentNum)));
                    commentParam.addElement(new Parameter("CANNED_REVIEW_COMMENT_CODE", DBEngineConstants.TYPE_INTEGER, id));
                    commentParam.addElement(new Parameter("COMMENTS", DBEngineConstants.TYPE_STRING, comment));
                    commentParam.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, timestamp));
                    commentParam.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, updateUser));
                    commentParam.addElement(new Parameter("MENTOR_PERSON_ID", DBEngineConstants.TYPE_STRING, mentorPerId));

                    commentParam.addElement(new Parameter("AW_PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, proposalNumber));
                    commentParam.addElement(new Parameter("AW_REVIEW_NUMBER", DBEngineConstants.TYPE_INT, reviewNumber));
                    commentParam.addElement(new Parameter("AW_PERSON_ID", DBEngineConstants.TYPE_STRING, personId));
                    commentParam.addElement(new Parameter("AW_COMMENT_NUMBER", DBEngineConstants.TYPE_INT, new Integer(maxCommentNum)));
                    commentParam.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, timestamp));
                    commentParam.addElement(new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, "I"));

                    sql = "call UPD_PROP_REVIEW_COMMENTS( <<PROPOSAL_NUMBER>>, <<REVIEW_NUMBER>>, <<PERSON_ID>>, <<COMMENT_NUMBER>>,"
                            + " <<CANNED_REVIEW_COMMENT_CODE>>, <<COMMENTS>>, <<UPDATE_TIMESTAMP>>, <<UPDATE_USER>>, <<MENTOR_PERSON_ID>>,"
                            + " <<AW_PROPOSAL_NUMBER>>, <<AW_REVIEW_NUMBER>>, <<AW_PERSON_ID>>, <<AW_COMMENT_NUMBER>>, <<AW_UPDATE_TIMESTAMP>>, <<AC_TYPE>> )";

                    procReqParameter = new ProcReqParameter();
                    procReqParameter.setDSN("Coeus");
                    procReqParameter.setParameterInfo(commentParam);
                    procReqParameter.setSqlCommand(sql.toString());
                    procedures.add(procReqParameter);

                    maxCommentNum = maxCommentNum + 1;
                    //if(count>10) break;
                }
                //Comments - END

                if (dbEngine != null && procedures.size() > 0) {
                    result = new Vector(3, 2);
                    dbEngine.executeStoreProcs(procedures);
                }
                //out.print("Proposal Review Saved");
                String link = request.getParameter("link");
                if(link == null || link.trim().length() == 0) {
                    response.sendRedirect(request.getContextPath() + "/wl/ProposalReviewStats.jsp?proposalNumber=" + proposalNumber + "&routingNumber=" + routingNumber);
                }else{
                     response.sendRedirect(link);
                }
            }catch (CoeusException ce){
                if(ce.getMessage().equals("validationError")){
                    Map dataMap = new HashMap();
                    reviewRoleCode = Integer.parseInt(request.getParameter("reviewRole"));
                    timeSpent = Integer.parseInt(request.getParameter("timeSpent"));
                    timeSpentDLC = Integer.parseInt(request.getParameter("timeSpentDLC"));
                    submVehicleCode = Integer.parseInt(request.getParameter("submissionVehicle"));
                    deanoffice = request.getParameter("reviewdbyDeanoff");
                    if (deanoffice != null && deanoffice.equalsIgnoreCase("on")) {
                        deanoffice = "Y";
                    } else {
                        deanoffice = "N";
                    }
                    dataMap.put("proposalNumber", proposalNumber);
                    dataMap.put("reviewRole", ""+reviewRoleCode);
                    dataMap.put("timeSpent", ""+timeSpent);
                    dataMap.put("timeSpentDLC", ""+timeSpentDLC);
                    dataMap.put("submissionVehicle", ""+submVehicleCode);
                    dataMap.put("deanoffice", deanoffice);
                    dataMap.put("deleteCommentNum",request.getParameter("deleteCommentNum"));

                    //insertComments
                    int maxCommentNum = -1;
                    Vector maxCommentParam = new Vector();
                    maxCommentParam.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, proposalNumber));
                    maxCommentParam.addElement(new Parameter("REVIEW_NUMBER", DBEngineConstants.TYPE_INT, reviewNumber));
                    maxCommentParam.addElement(new Parameter("PERSON_ID", DBEngineConstants.TYPE_STRING, personId));
                    Vector result = dbEngine.executeFunctions("Coeus", "{ <<OUT INTEGER COUNT>> = call FN_GET_MAX_PRC_COMM_NUM( <<PROPOSAL_NUMBER>>, <<REVIEW_NUMBER>>, <<PERSON_ID>>)}", maxCommentParam);
                    Map map = (Map) result.get(0);
                    String commentCount = (String) map.get("COUNT");
                    maxCommentNum = Integer.parseInt(commentCount);
                    ArrayList insertedComments = new ArrayList();
                    Integer id = null;
                    String comment = "";
                    int count = 0, index = -1;
                    Vector commentParam;
                    while (comment != null) {
                        comment = request.getParameter("comment" + count++);
                        if (comment == null) {
                            break; //no Comments
                        }
                        if(comment.trim().length() > 0){
                            insertedComments.add(comment);
                        }
                    }
                    dataMap.put("insertedComments",insertedComments);
                    session.setAttribute("userData", dataMap);
                    response.sendRedirect(request.getContextPath() +retLink);
                }
            }catch (Exception ex) {
                out.print(ex.getMessage());
                UtilFactory.log(ex.getMessage(), ex, "ProposalReview.jsp", "");
            }

        %>
    </body>
</html>
