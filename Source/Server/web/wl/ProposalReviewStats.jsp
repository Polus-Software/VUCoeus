<%-- 
    Document   : proposalReviewStats2
    Created on : Feb 17, 2010, 3:34:41 PM
    Author     : sharathk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="edu.mit.coeus.utils.UtilFactory, edu.mit.coeus.utils.dbengine.*, java.sql.*, java.util.*"%>
<%@page import="edu.mit.coeuslite.utils.SessionConstants, edu.mit.coeus.bean.PersonInfoBean, java.text.SimpleDateFormat"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel=stylesheet href="<%=request.getContextPath()%>/coeuslite/mit/utils/css/coeus_styles.css" type="text/css">
        <style>
            #mbox{background-color:#eee; padding:8px; border:2px outset #666;}
            #mbm{font-family:sans-serif;font-weight:bold;float:right;padding-bottom:5px;}
            #ol{background-image: url('../coeuslite/mit/utils/scripts/modal/overlay.png');}
            .dialog {display:none}

            * html #ol{background-image:none; filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src="../coeuslite/mit/utils/scripts/modal/overlay.png", sizingMethod="scale");}

        </style>
        <script src="<%=request.getContextPath()%>/coeuslite/mit/utils/scripts/modal/modaldbox.js"></script>
        <title>Proposal Review Stats</title>
        <script type="text/javascript">
            var arrDivId = ["tab1", "tab2", "tab3"];
            var arrTabId = ["td1", "td2", "td3"];
            var selectedDiv = "tab1";
            var selectedTab = "td1";
            var searchDiv;

            function showTab(divId, tabId){
                selectedDiv = divId;
                selectedTab = tabId;
                //Hide All Tab
                for(i=0; i<arrDivId.length; i++){
                    if(arrDivId[i] == divId) continue;
                    document.getElementById(arrDivId[i]).style.display="none";
                    document.getElementById(arrTabId[i]).className = "table";
                }
                document.getElementById(divId).style.display="";
            }

            function tabHover(tdId, styleName) {
                if(selectedTab == tdId) return;
                elemId = document.getElementById(tdId);
                elemId.style.cursor = "pointer";
                //If row is selected retain selection style
                //Apply hover Style only if row is not selected
                elemId.className = styleName;

            }

            function showSearch(searchDiv, searchType){
                this.searchDiv = searchDiv;
                //iframeElem = document.getElementById('propSearchFrame');
                //iframeElem.src="PaperProposalSearch.jsp?searchType="+searchType;
                width = document.getElementById(searchDiv).style.pixelWidth;
                height = document.getElementById(searchDiv).style.pixelHeight;
                sm(searchDiv,width,height);
            }

            /*function searchAgain(){
                document.getElementById('PaprePropFrame').src="PaperProposalSearch.jsp";

                //Change Div Size
                toWidth = 675;
                toHeight = 215;
                document.getElementById('mbox').style.width = toWidth+"px";
                document.getElementById('mbox').style.height = toHeight+"px";
                position = getPosition('mbox');

                if (parseInt(navigator.appVersion)>3) {
                 if (navigator.appName=="Netscape") {
                  winW = window.innerWidth-16;
                  winH = window.innerHeight-16;
                 }
                 if (navigator.appName.indexOf("Microsoft")!=-1) {
                  winW = document.body.offsetWidth-20;
                  winH = document.body.offsetHeight-20;
                 }
                }

                document.getElementById('mbox').style.left=(winW-toWidth)/2+"px";
                document.getElementById('mbox').style.top=(winH-toHeight)/2+"px";

                //Change Buttons
                document.getElementById('btnSearchAgain').style.display = "none";
                document.getElementById('btnSearch').style.display = "";
            }

            function searchPaperProps(iframeId){
                var iframeDocument;
                if(document.getElementById(iframeId).contentDocument){
                    //Mozilla
                    iframeDocument = document.getElementById(iframeId).contentDocument;
                }else {
                    //IE
                    iframeDocument = window.frames['0'].document;
                }
                iframeDocument.forms[0].submit();

                //Change Div Size
                toWidth = 1000;
                toHeight = 500;
                document.getElementById('mbox').style.width = toWidth+"px";
                document.getElementById('mbox').style.height = toHeight+"px";
                position = getPosition('mbox');

                if (parseInt(navigator.appVersion)>3) {
                 if (navigator.appName=="Netscape") {
                  winW = window.innerWidth-16;
                  winH = window.innerHeight-16;
                 }
                 if (navigator.appName.indexOf("Microsoft")!=-1) {
                  winW = document.body.offsetWidth-20;
                  winH = document.body.offsetHeight-20;
                 }
                }

                document.getElementById('mbox').style.left=(winW-toWidth)/2+"px";
                document.getElementById('mbox').style.top=(winH-toHeight)/2+"px";

                //Change Buttons
                document.getElementById('btnSearch').style.display = "none";
                document.getElementById('btnSearchAgain').style.display = "";
            }*/

        function resize(){
            var height= (document.getElementById('mbox').height || document.getElementById('mbox').scrollHeight || document.getElementById('mbox').clientHeight);
            var width = (document.getElementById('mbox').width || document.getElementById('mbox').scrollWidth || document.getElementById('mbox').clientWidth);

            if(width < 700){
                height = 500;
                width = 1000;
            }else {
                height = 240;
                width = 675;
            }
            if (parseInt(navigator.appVersion)>3) {
             if (navigator.appName=="Netscape") {
              winW = window.innerWidth-16;
              winH = window.innerHeight-16;
             }
             if (navigator.appName.indexOf("Microsoft")!=-1) {
              winW = document.body.offsetWidth-20;
              winH = document.body.offsetHeight-20;
              height = height + 25;
             }
            }
            document.getElementById('mbox').style.width = width+"px";
            document.getElementById('mbox').style.height = height+"px";
            document.getElementById('mbox').style.left=(winW-width)/2+"px";
            document.getElementById('mbox').style.top=(winH-height)/2+"px";

        }

        function cancelSearch(){
            toWidth = 675;
            toHeight = 240;
            document.getElementById('mbox').style.width = toWidth+"px";
            document.getElementById('mbox').style.height = toHeight+"px";
            hm(searchDiv);
        }

        function getPosition(id) {
            var curleft = curtop = 0;
            obj = document.getElementById(id);
            if (obj.offsetParent) {
                    curleft = obj.offsetLeft
                    curtop = obj.offsetTop
                    while (obj = obj.offsetParent) {
                            curleft += obj.offsetLeft
                            curtop += obj.offsetTop
                    }
            }
            return [curleft,curtop];
        }
        </script>
    </head>
    <body>
        <a name="top"></a>
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
                            <td height="25" colspan="2" align="right" nowrap>
                                <a href="<%=request.getContextPath()%>"><font size='2' color='#D1E5FF'><u>Back to CoeusLite</u></font></a>&nbsp;&nbsp;&nbsp;
                            </td>
                        </tr>
                    </table>
                    <!-- header END -->
                </td>
            </tr>
            <%
            if (request.getParameter("proposalNumber") != null && request.getParameter("routingNumber") != null) {
            %>
            <%--tr>
                <td class="tabtable rowLine" height="25">
                    <font color="red"><b>review details for Proposal <%=request.getParameter("proposalNumber")%> successfully saved.</b></font>
                </td>
            </tr--%>
            <script>
            alert("Review details for Proposal <%=request.getParameter("proposalNumber")%> is successfully saved.\nYou can find this review under Completed Reviews Section.");
            </script>
            <%
            }
            %>

            <tr align="left" valign="top">
                <td height="100%" valign="top">
                    <table width="100%" border="0" cellpadding="0" cellspacing="0">
                        <tr>
                            <td>
                                <table width="50%" border="0" cellpadding="0" cellspacing="0">
                                    <tr>
                                        <td align="center" class="tabtable" height="25" id="td1" onmouseover="tabHover('td1','tabtable')" onmouseout="tabHover('td1','table')" onclick="showTab('tab1', 'td1')">
                                            <b>Electronic Proposals</b>
                                        </td>
                                        <td align="center" class="table" id="td2" onmouseover="tabHover('td2','tabtable')" onmouseout="tabHover('td2','table')" onclick="showTab('tab2','td2')">
                                            <b>Paper Proposals</b>
                                        </td>

                                        <td align="center" class="table" id="td3" onmouseover="tabHover('td3','tabtable')" onmouseout="tabHover('td3','table')" onclick="showTab('tab3','td3')">
                                            <b>Completed Reviews</b>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        
                        <tr>
                            <td colspan="5">
                                <div id="tab1" style="display:">
                                <table width="100%" height="100%" border="0" cellpadding="0" cellspacing="5" class="tabtable">
                                    <tr>
                                        <td>
                                            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                                <tr>
                                                    <td valign="top" height="25" class="copybold" style="border-bottom: 1px solid #789FD5;">
                                                        <big>Electronic Proposals:</big>
                                                    </td>
                                                    <td width="20%" align="right" style="border-bottom: 1px solid #789FD5;">
                                                        <input type="button" value="Search" onclick="showSearch('propSearchDiv','electronic')"/>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                   
                                    <tr><td valign="top" align="center">

                                            <table width="98%" border="0" cellpadding="3" cellspacing="0" class="tabtable">
                                                <tr class="theader">
                                                    <td align="left" width="8%">Proposal #</td>
                                                    <td align="left" width="7%">Inst Prop #</td>
                                                    <td align="left" width="15%">PI</td>
                                                    <td align="left" width="45%">Title</td>
                                                    <td align="left" width="20%">Sponsor</td>
                                                    <td align="left" width="5%">Primary Appr</td>
                                                    <%--td align="left" width="4%">Status</td--%>
                                                </tr>
                                                <%
                                                        DBEngineImpl dbEngine = new DBEngineImpl();
                                                        try {
                                                            Vector param = new Vector();
                                                            Vector result = null;
                                                            int listSize = 0;
                                                            String userId = personInfo.getUserId();
                                                            param.addElement(new Parameter("USER_ID", DBEngineConstants.TYPE_STRING, userId));
                                                            if (dbEngine != null) {
                                                                result = new Vector();
                                                                result = dbEngine.executeRequest("Coeus",
                                                                        "call GETPROPREVIEWSTATSFORUSER( <<USER_ID>>, <<OUT RESULTSET rset>> )",
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
                                                    <td>
                                                        <a href="<%=request.getContextPath()%>/wl/ProposalReview.jsp?proposalNumber=<%=map.get("MODULE_ITEM_KEY")%>&routingNumber=<%=map.get("ROUTING_NUMBER")%>&apprSeq=<%=map.get("APPROVAL_SEQUENCE")%>&proposalType=electronic"><%=map.get("MODULE_ITEM_KEY")%></a>
                                                    </td>
                                                    <td>
                                                        <%=map.get("INST_PROPOSAL_NUMBER")%>
                                                    </td>
                                                    <td>
                                                        <%=map.get("PERSON_NAME")%>
                                                    </td>
                                                    <td>
                                                        <%=map.get("TITLE")%>
                                                    </td>
                                                    <td>
                                                        <%=map.get("SPONSOR_NAME")%>
                                                    </td>
                                                    <td align="center">
                                                        <%if(map.get("PRIMARY_APPROVER_FLAG").toString().equalsIgnoreCase("Y")){%>
                                                        <img src="../coeusliteimages/complete.gif" border="0"/>
                                                        <%}else{%>
                                                        <img src="../coeusliteimages/none.gif" border="0"/>
                                                        <%}%>
                                                    </td>
                                                    <%--td>
                                                        <%=map.get("STATUS_CODE")%><!-- 5=Submitted -->
                                                    </td--%>
                                                </tr>
                                                <%                    }
                                                            }//End if (list Size)
                                                            else {
                                                 %>
                                                        <tr>
                                                            <td colspan="4">No Electronic Proposals for <b><%=personInfo.getFullName()%></b></td>
                                                        </tr>
                                                 <%
                                                            }
                                                        } catch (Exception ex) {
                                                            out.print("<tr><td colspan=4>"+ex.getMessage()+"</td></tr>");
                                                            UtilFactory.log(ex.getMessage(), ex, "ProposalReviewStats.jsp", "");

                                                        }
                                                %>

                                            </table>

                                        </td></tr>
                                </table>
                                </div>

                                <div id="tab2" style="display:none">
                                    <table width="100%" border="0" cellpadding="0" cellspacing="5" class="tabtable">
                                    <tr>
                                        <td>
                                            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                                <tr>
                                                    <td valign="top" height="25" class="copybold" style="border-bottom: 1px solid #789FD5;">
                                                        <big>Paper Proposals:</big>
                                                    </td>
                                                    <td width="20%" align="right" style="border-bottom: 1px solid #789FD5;">
                                                        <input type="button" value="Search" onclick="showSearch('propLogSearchDiv','paper')"/>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td valign="top" align="center">
                                             <table width="98%" border="0" cellpadding="3" cellspacing="0" class="tabtable">
                                                <tr class="theader">
                                                    <td align="left" width="8%">Proposal #</td>
                                                    <td align="left" width="15%">PI</td>
                                                    <td align="left" width="50%">Title</td>
                                                    <td align="left" width="27%">Sponsor Name</td>
                                                </tr>
                                                <%
                                                try {
                                                    Vector param = new Vector();
                                                    Vector result = null;
                                                    int listSize = 0;
                                                    String userId = personInfo.getUserId();
                                                    param.addElement(new Parameter("USER_ID", DBEngineConstants.TYPE_STRING, userId));
                                                    if (dbEngine != null) {
                                                        result = new Vector();
                                                        result = dbEngine.executeRequest("Coeus",
                                                                "call GETPROPLOGSTATSFORUSER( <<USER_ID>>, <<OUT RESULTSET rset>> )",
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
                                                    <td>
                                                        <a href="<%=request.getContextPath()%>/wl/ProposalReview.jsp?proposalNumber=<%=map.get("PROPOSAL_NUMBER")%>&proposalType=paper"><%=map.get("PROPOSAL_NUMBER")%></a>
                                                    </td>
                                                    <td>
                                                        <%=map.get("PI_NAME")%>
                                                    </td>
                                                    <td>
                                                        <%=map.get("TITLE")%>
                                                    </td>
                                                    <td>
                                                        <%=map.get("SPONSOR_NAME")%>
                                                    </td>
                                                </tr>
                                                <%                    }
                                                    }//End if (list Size)
                                                    else {
                                                        %>
                                                        <tr>
                                                            <td colspan="4">No Paper Proposals for <b><%=personInfo.getFullName()%></b></td>
                                                        </tr>
                                                 <%
                                                    }
                                                } catch (Exception ex) {
                                                    out.print("<tr><td colspan=4>"+ex.getMessage()+"</td></tr>");
                                                    UtilFactory.log(ex.getMessage(), ex, "ProposalReviewStats.jsp", "");

                                                }
                                                %>

                                            </table>
                                        </td>
                                    </tr>
                                </table>
                                </div>

                                <div id="tab3" style="display:none">
                                     <table width="100%" border="0" cellpadding="0" cellspacing="5" class="tabtable">
                                    <tr>
                                        <td>
                                            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                                <tr>
                                                    <td valign="top" height="25" class="copybold" style="border-bottom: 1px solid #789FD5;">
                                                        <big>Completed Reviews:</big>
                                                    </td>
                                                    <td width="20%" align="right" style="border-bottom: 1px solid #789FD5;">
                                                        <input type="button" value="Search" onclick="showSearch('completedPropSearchDiv','completed')"/>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td valign="top" align="center">
                                             <table width="98%" border="0" cellpadding="3" cellspacing="0" class="tabtable">
                                                <tr class="theader">
                                                    <td align="left" width="8%">Proposal #</td>
                                                    <td align="left" width="7%">Inst Prop #</td>
                                                    <td align="left" width="15%">PI</td>
                                                    <td align="left" width="35%">Title</td>
                                                    <td align="left" width="20%">Sponsor Name</td>
                                                    <td align="left" width="15%">Last Updated</td>
                                                    <%--td width="10%">&nbsp;</td--%>
                                                </tr>
                                                <%
                                                try {
                                                    Vector param = new Vector();
                                                    Vector result = null;
                                                    int listSize = 0;
                                                    SimpleDateFormat dateFormatDisplay = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
                                                    String userId = personInfo.getUserId();
                                                    param.addElement(new Parameter("USER_ID", DBEngineConstants.TYPE_STRING, userId));
                                                    if (dbEngine != null) {
                                                        result = new Vector();
                                                        result = dbEngine.executeRequest("Coeus",
                                                                "call GETCOMPLETEDPROPSTATSFORUSER( <<USER_ID>>, <<OUT RESULTSET rset>> )",
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
                                                    <td>
                                                        <a href="<%=request.getContextPath()%>/wl/ProposalReview.jsp?proposalNumber=<%=map.get("PROPOSAL_NUMBER")%>&routingNumber=<%=map.get("ROUTING_NUMBER")%>&apprSeq=<%=map.get("REVIEW_NUMBER")%>&proposalType=<%=map.get("PROPOSAL_TYPE")%>"><%=map.get("PROPOSAL_NUMBER")%></a>
                                                    </td>
                                                    <td>
                                                        <%=map.get("INST_PROPOSAL_NUMBER") == null ? "" :map.get("INST_PROPOSAL_NUMBER")%>
                                                    </td>
                                                    <td>
                                                        <%=map.get("PI_NAME")%>
                                                    </td>
                                                    <td>
                                                        <%=map.get("TITLE")%>
                                                    </td>
                                                    
                                                    <td>
                                                        <%=map.get("SPONSOR_NAME")%>
                                                    </td>
                                                    <td>
                                                        <%
                                                        out.print(dateFormatDisplay.format(map.get("UPDATE_TIMESTAMP")));
                                                        %>
                                                    </td>
                                                    <%--td>
                                                        <%if(((String)map.get("PROPOSAL_TYPE")).equalsIgnoreCase("paper")){%>
                                                            <a href="<%=request.getContextPath()%>/wl/ProposalReview.jsp?proposalNumber=<%=map.get("PROPOSAL_NUMBER")%>&proposalType=<%=map.get("PROPOSAL_TYPE")%>">New Review</a>
                                                        <%}else{%>
                                                            &nbsp;
                                                        <%}%>
                                                    </td--%>
                                                </tr>
                                                <%                    }
                                                    }//End if (list Size)
                                                    else {
                                                        %>
                                                        <tr>
                                                            <td colspan="4">No Completed Proposals for <b><%=personInfo.getFullName()%></b></td>
                                                        </tr>
                                                 <%
                                                    }
                                                } catch (Exception ex) {
                                                    out.print("<tr><td colspan=4>"+ex.getMessage()+"</td></tr>");
                                                    UtilFactory.log(ex.getMessage(), ex, "ProposalReviewStats.jsp", "");

                                                }
                                                %>

                                            </table>
                                        </td>
                                    </tr>
                                </table>
                                </div>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td align="center">
                    <a href="#top">TOP</a>
                </td>
            </tr>
        </table>
        <!-- Proposal Log Search - START -->
        <div id="propLogSearchDiv" class="dialog" style="width:675px;height:240px;overflow: hidden">
            <table height="100%" width="100%" class="table">
                <tr>
                    <td width="100%">
                        <iframe id="propLogSearchFrame" src="PaperProposalSearch.jsp?searchType=paper" width="100%" height="100%" scrolling="auto" marginheight="0" marginwidth="0" frameborder="0"></iframe>
                    </td>
                </tr>
                <%--tr height="25">
                    <td align="left" width="17%">
                        <input type="button" id="btnSearch" style="width:100" value="Search" onclick="searchPaperProps('PaprePropFrame')"/>
                        <input type="button" id="btnSearchAgain" style="width:100" value="SearchAgain" style="display: none" onclick="searchAgain()"/>
                    </td>
                    <td align="left">
                        <input type="button" value="Cancel" style="width:100" onclick="cancelSearch()"/>
                    </td>
                </tr--%>
            </table>
            
        </div>
        <!-- Proposal Log Search - END -->

        <div id="propSearchDiv" class="dialog" style="width:675px;height:240px;overflow: hidden">
            <table height="100%" width="100%" class="table">
                <tr>
                    <td width="100%">
                        <iframe id="propSearchFrame" src="PaperProposalSearch.jsp?searchType=electronic" width="100%" height="100%" scrolling="auto" marginheight="0" marginwidth="0" frameborder="0"></iframe>
                    </td>
                </tr>
            </table>
        </div>

        <div id="completedPropSearchDiv" class="dialog" style="width:675px;height:240px;overflow: hidden">
            <table height="100%" width="100%" class="table">
                <tr>
                    <td width="100%">
                        <iframe id="completedPropSearchFrame" src="PaperProposalSearch.jsp?searchType=completed" width="100%" height="100%" scrolling="auto" marginheight="0" marginwidth="0" frameborder="0"></iframe>
                    </td>
                </tr>
            </table>
        </div>

    </body>
</html>
