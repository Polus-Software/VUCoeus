

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="edu.mit.coeuslite.utils.bean.WebTxnBean, edu.utk.coeuslite.propdev.bean.EPSProposalHeaderBean, java.util.*"%>
<%@page import="edu.mit.coeuslite.utils.SessionConstants, edu.mit.coeus.bean.PersonInfoBean, edu.mit.coeus.utils.dbengine.*, java.sql.*"%>
<%@page import="edu.mit.coeus.utils.UtilFactory, java.text.SimpleDateFormat, edu.mit.coeus.bean.*"%>
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
        <script language="javascript">
            var cannedReportIndex = new Array();//stores the row Index of table for Canned Comments
            var cannedReportId = new Array();//stores the report Id for Canned Comments
            var commentToDelete = new Array();
            var rowNum = -1;
            var insertFrom = 0;
           var SaveButon = false;
            var dataChanged = false;
            var formName = "roles";
            var commentsTable = "tabComments"; // table that start with "Administrator Assignement Roles
            var commentsTableBody = "tabCommentsBody";
            var hiddenTD = "tdHidden";
            var commentRow = "row";




            function showDialog(divId){
                width = document.getElementById(divId).style.pixelWidth;
                height = document.getElementById(divId).style.pixelHeight;
                sm(divId,width,height);
            }


            function closeCannedComments(){
                var iframeDocument;
                var formsLength = 0;
                if(document.getElementById('iframeId').contentDocument){
                    //Mozilla
                    iframeDocument = document.getElementById('iframeId').contentDocument;
                }else {
                    //IE
                    iframeDocument = window.frames['0'].document;
                }

                formsLength = iframeDocument.forms[0].length;
                 //alert(formsLength);
                var elemName;
                var desc;
                var rowData = new Array(2);
                for(i=0; i<formsLength ; i++){
                    elemName = "";//check"+i;
                    if(iframeDocument.forms['reviewComments'].elements[i].name.indexOf('check', 0) > -1){
                        elemName = iframeDocument.forms['reviewComments'].elements[i].name;
                        if(iframeDocument.forms['reviewComments'].elements[elemName].checked){
                            desc = iframeDocument.forms['reviewComments'].elements[elemName].value;
                            //alert("desc..............:  "+ desc);
                            rowData[0] = desc.substring(0, desc.indexOf(':'));

                          rowData[1] = desc.substring(0, desc.length);
                         //  alert("rowData[0] :"+ rowData[0] + "--- rowData[1] :"+ rowData[1] );
                            //cannedReportIndex[cannedReportIndex.length] = document.getElementById("tabComments").rows.length;
                            //cannedReportId[cannedReportId.length] = rowData[0];
                            cannedReportIndex[cannedReportIndex.length] = rowNum + 1;
                            cannedReportId[cannedReportId.length] = rowData[1];
                            //alert("just before call addRow function");
                            addRow(rowData);
                        }
                    }
                }

                   if (cannedReportIndex.length > 0){
                       document.forms[0].rolesWereSelected.value="yes";
                   }
                      // document.forms[0].btnsave.disabled=true;
                 //alert("cannedReportIndex.length  :" + cannedReportIndex.length);
                 //  }
                 // alert("cannedReportId  :" + cannedReportId);
                //document.proposalReview.reviewComments.value=comment;

                hm('cannedComments');

            }


            function submitChild(){
                var iframeDocument;
                var formsLength = 0;
                if(document.getElementById('iframeId').contentDocument){
                    //Mozilla
                    iframeDocument = document.getElementById('iframeId').contentDocument;
                }else {
                    //IE
                    iframeDocument = window.frames['0'].document;
                }

                iframeDocument.forms['reviewComments'].submit();

                 hm('cannedComments');
                window.location.reload();


            }

            function closeTextComment(){
                elemText = document.getElementById('textComment');
                rowData = new Array(2);
                rowData[0] = "";
                rowData[1] = elemText.value;
                addRow(rowData);
                hm('textCommentDiv');
            }

            function addRow(cellArr){
                //alert(cellArr);
                tableElement = document.getElementById(commentsTable); // table that start with "Administrator Assignement Roles
                //rowNum = tableElement.rows.length;
                rowNum = rowNum + 1;
                //alert(rowNum);
                var newRow   = tableElement.insertRow(tableElement.rows.length);
                newRow.setAttribute("id", commentRow+rowNum);
                newRow.className = "rowline";
                for(cols=0; cols < cellArr.length; cols++) {
                    // Insert a cell in the row at index
                    var newCell  = newRow.insertCell(cols);
                    // Append a text node to the cell
                    if(cellArr[cols].length > 0){
                        var newText  = document.createTextNode(cellArr[cols]);
                        newCell.appendChild(newText);
                    }
                }
                linkElem = document.createElement("a");
                linkElem.setAttribute("href", "javascript:deleteRow("+rowNum+")");
                linkElem.setAttribute("alt", "Delete");
                linkElem.setAttribute("title", "Delete");

                imgElem = document.createElement("img");
                imgElem.setAttribute("src", "../coeusliteimages/none.gif");
                imgElem.setAttribute("border", "0");

                linkElem.appendChild(imgElem);
                newCell  = newRow.insertCell(2);
                newCell.appendChild(linkElem);

                dataChanged = true;
            }

            function deleteRow(rowId){
                tableElement = document.getElementById(commentsTableBody);
                rowElem = document.getElementById(commentRow+rowId);
                res = confirm("Are you sure you want to delete comment : "+rowElem.childNodes.item(1).innerHTML);
                if(res) {
                    tableElement.removeChild(rowElem);
                    //tableElement.deleteRow(rowId);
                    //alert("index:"+cannedReportIndex+"; ID:"+ cannedReportId+" length:"+cannedReportId.length);
                    for(i=0; i<cannedReportIndex.length;i++){
                        if(cannedReportIndex[i] == rowId){
                            cannedReportIndex.splice(i, 1);
                            cannedReportId.splice(i, 1);
                        }
                    }
                }
                //alert("index:"+cannedReportIndex+"; ID:"+ cannedReportId+" length:"+cannedReportId.length);
            }

             // for disabling already checked checkboxes.
            function selectComments(){
                // alert("before if in selectComments function");
                if(cannedReportId.length > 0) {
                   // alert("They are selected roles in cannedReportId");
                    var iframeDocument;
                    if(document.getElementById('iframeId').contentDocument){
                        //Mozilla
                        iframeDocument = document.getElementById('iframeId').contentDocument;
                    }else {
                        //IE
                        iframeDocument = window.frames['0'].document;
                    }
                    for(i=0; i< cannedReportId.length; i++) {
                        // here where we disable already checked roles
                        iframeDocument.forms['reviewComments'].elements["check"+cannedReportId[i]].disabled = true;
                    }
                }
            }

            function markForDelete(commentId, rowId){
                commentToDelete[commentToDelete.length] = commentId;
                deleteRow(rowId);
                dataChanged = true;
            }

            function validate(){
                tableElement = document.getElementById(commentsTableBody);
                //nodeList = tableElement.childNodes;
                nodeList = tableElement.getElementsByTagName("TR");
                var commentId = 0;
                for(i=0;i<nodeList.length;i++){//first TR is Header
                    rowElem = nodeList.item(i);
                    if(rowElem.getAttribute("id") != null && rowElem.getAttribute("id").indexOf(commentRow, 0) > -1){
                        if(rowElem.getAttribute("id").substring(commentRow.length, rowElem.getAttribute("id").length) >= insertFrom) {
                            tdElem = document.getElementById(hiddenTD);
                            commentElem = document.createElement("input");
                            commentElem.setAttribute("type", "hidden");
                            commentElem.setAttribute("name", "comment"+commentId++);
                            commentElem.setAttribute("value",rowElem.childNodes.item(0).innerHTML+"~"+rowElem.childNodes.item(1).innerHTML);
                            tdElem.appendChild(commentElem);
                        }
                    }
                }
                //deleteCommentNum
                document.forms[formName].elements['deleteCommentNum'].value = commentToDelete.join(",");
                document.getElementById("reviewAction").disabled = false;
                return true;
            }

            function setDataChanged(){
                dataChanged = true;
            }

            function hyperlink(address){
                if(dataChanged){
                    click = confirm("Do you want to save Roles?");
                    if(click){  // you chose Ok from dialogbox
                        //set link
                        document.forms[formName].elements['link'].value = address;
                        //Submit Form
                        submitForm(formName);
                    }else {
                        window.location = address;
                    }
                }else{
                    window.location = address;
                }
            }

            function submitForm(formName){
                validate();
                document.forms[formName].submit();
            }

            function test(){
                alert("insert From"+insertFrom +" Comments To Delete: "+commentToDelete);
                alert("index: "+cannedReportIndex+" ; ID: "+cannedReportId);
                //document.forms[formName].elements['deleteCommentNum'].value = "okay";
                //alert(document.reviewComments.deleteCommentNum.value = "okay");
            }
        </script>
        <title>Coeus Reporting Roles Maintenance</title>
    </head>
    <body>
        <% boolean otherUserComments = false;
        Vector userResult = null;
        %>
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
                                <a href="javascript:hyperlink('<%=request.getContextPath()%>')"><font size='2' color='#D1E5FF'><u>Back to CoeusLite</u></font></a>&nbsp;&nbsp;&nbsp;
                            </td>
                        </tr>
                    </table>
                    <!-- header END -->
            </tr>
            <tr align="left" valign="top">
                <td height="100%" valign="top">

                    <table width="100%" height="100%" class="tabtable">
                        <tr>
                            <td valign="top">
                                <table width="100%" border="0" cellpadding="0" cellspacing="5">
                                    <tr>
                                        <td valign="top" height="25" class="copybold">
                                            <big>Coeus Reporting Roles Maintenance</big>
                                        </td>
                                    </tr>








                                    <%
                                    String userId0 = request.getParameter("userId");
                                       boolean userexists = true;
                                    if(userId0 != null) {

                                      try {
                                                  DBEngineImpl dbEngine = new DBEngineImpl();



                                                            Vector param = new Vector();
                                                            Vector result = null;
                                                            int listSize = 0;
                                                            param.addElement(new Parameter("AS_USER_ID", DBEngineConstants.TYPE_STRING, userId0));

                                                            if (dbEngine != null) {
                                                                result = new Vector();
                                                                result = dbEngine.executeFunctions("Coeus",
                                                                 "{ << OUT INTEGER RET >> = call FN_USER_EXISTS ( << AS_USER_ID >> ) }",param);

                                                               if (result.size() > 0) {

                                                                      HashMap resultSet;
                                                                      String  returnValue;
                                                                      resultSet = (HashMap) result.get(0);

                                                                      returnValue = resultSet.get("RET").toString();

                                                                          if (returnValue.equals("0")) {
                                                                          userexists = false;
                                          %>
                                                                          <tr class="rowLine">
                                                                              <!-- Majid added this 05/10/2010 -->
                                                                             <td>
                                                                           <FONT COLOR="red"> User <%=userId0%> does not exist</FONT>
                                                                             </td>
                                                                          </tr>
                                                                     <%    }

                                                              } //end if result.size
                                                            }


                                } catch (Exception ex) {
                                                            out.print("<tr><td colspan=4>"+ex.getMessage()+"</td></tr>");
                                                            UtilFactory.log(ex.getMessage(), ex, "ProposalReviewStats.jsp", "");

                                                        }


                              } else { userexists = true;    }


                        %>

                                           <tr>
                <td valign="top">
                    <br>
                    <form action="UserRoles.jsp" onsubmit="return validateForm()">
                        <table align="center" width="98%" height="80px" border="0" cellspacing="0" cellpadding="0" class="tabtable" >
                            <tr class="theader">
                                <td align="left">User Search </td>

                            </tr>


                            <tr class="copybold" align="left"> <!-- Details -->
                                <td>
                                    <table class="copybold" valign="top">

                                    <tr>

                                    <td align="left"  style="font-size:14px;font-style:bold;">User ID :</td>
                                    <td><input type="text" name="userId" /> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>


                                    <td><input type="submit" value="Search"></td>

                                    </tr>

                                    <tr>
                                        <td align="right">
				               <a href="<%=request.getContextPath()%>/rep_roles/AllUsers.jsp">
                                                   <u>  Show All users</u>
					          </a></td>
                                    </tr>

                                    </table>
                                </td>
                            </tr>
                        </table>
                 </form>
                </td>
            </tr><!--  Search Form - END -->






                                 <%
                                       String userId = request.getParameter("userId");
                                       if(userId != null  & userexists){

                                          %>





                                    <tr><td valign="top" align="center" height="45">
                                            <!-- Proposal Header Section -->
                                            <%

                                                  //  String userId = request.getParameter("userId");

                                                    DBEngineImpl dbEngine = new DBEngineImpl();
                                                        try {
                                                            Vector param = new Vector();
                                                            Vector result = null;
                                                            int listSize = 0;
                                                            param.addElement(new Parameter("AW_USER_ID", DBEngineConstants.TYPE_STRING, userId));
                                                            if (dbEngine != null) {
                                                                result = new Vector();
                                                                result = dbEngine.executeRequest("Coeus",
                                                                        "call DW_GET_USER_BY_ID(<< AW_USER_ID >> , <<OUT RESULTSET rset>> )",
                                                                        "Coeus", param);
                                                                if (result != null) {
                                                                    listSize = result.size();
                                                                }
                                                            }
                                                            if (listSize > 0) {
                                                                    HashMap map = (HashMap) result.get(0);





                                            %>
                                            <table width="100%" border="0" cellpadding="3" cellspacing="0" class="table">
                                                <tr>
                                                    <td align="right" class='copybold'>
                                                        User ID:
                                                    </td>
                                                    <td class="copy">
                                                       <%=map.get("USER_ID")%>
                                                    </td>

                                                    <td align="right" class='copybold'>
                                                        Unit Name
                                                    </td>
                                                    <td class="copy">
                                                        <%=map.get("UNIT_NAME")%>
                                                    </td>


                                                </tr>
                                                <tr >


                                                    <td align="right" class='copybold'>
                                                        User Name:
                                                    </td>
                                                    <td class="copy">
                                                        <%=map.get("USER_NAME")%>
                                                    </td>

                                                </tr>

                                                  <%

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

                                    <tr>
                                        <td valign="top" align="center">


                                                <table width="100%" border="0" cellpadding="3" cellspacing="0" class="tabtable">
                                                    <tr class="theader">
                                                        <td align="left" colspan="4" >
                                                            Roles assigned to user : <%=request.getParameter("userId")%>
                                                        </td>
                                                    </tr>

                                                     <%
                                   String userId2 = request.getParameter("userId");
                                   session.setAttribute( "userId2", userId2 );

                                                        try {
                                                            Vector param = new Vector();
                                                            Vector result = null;
                                                             String selectQuery = "";
                                                             String AW_USER_ID = "";
                                                            int listSize = 0;
                                              param.addElement(new Parameter("AW_USER_ID", DBEngineConstants.TYPE_STRING, userId2.toUpperCase()));
                                              selectQuery = "SELECT GRANTED_ROLE from dba_role_privs WHERE upper(grantee) = <<AW_USER_ID>> and GRANTED_ROLE in (select ROLE_NAME from OSP$REPORT_ROLES_LIST)";

                                                            if (dbEngine != null) {
                                                                result = new Vector();
                                                                 result = dbEngine.executeRequest("Coeus", selectQuery, "Coeus",param);
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
                                                       <tr>
                                                        <td align="left" class="copybold" width="25%" >
                                                            <FONT COLOR="green"> <%=map.get("GRANTED_ROLE")%></FONT>
                                                        </td>
                    <td>
                        <a href="<%=request.getContextPath()%>/rep_roles/RemoveRole.jsp?userId=+<%=userId2%>&grantedRole=<%=map.get("GRANTED_ROLE")%>"><u>Remove this role</u></a>
                    </td>



                                                    </tr>

                                                    <%
     } %>

           <tr>
                                                                        <td align="left" >
                           <a href="javascript:showDialog('grantRevokeDiv')"><u>Assign New Roles</u></a>
                    </td>
                                                                        </tr>
     <%
    }else { %>
        
        <tr><td colspan='4'>Search returns zero results<td></tr>
        <tr><td colspan='4'><a href="javascript:showDialog('grantRevokeDiv')"><u>Assign New Roles</u></a><td></tr>

        <%
    }
    }catch(Exception ex){
        //out.println(ex.getMessage());
        out.print("<tr><td colspan='4'>"+ex.getMessage()+"<td></tr>");
        UtilFactory.log(ex.getMessage(), ex, "UserRoles.jsp", "");
    }
    %>

                                                </table>

                                        </td>


                                    </tr>



                                                    <%}   //END IF userId0 not null %>
                                </table>

              <div id="grantRevokeDiv" class="dialog" style="width:600px;height:380px;overflow: hidden">
                <table class="table">

                    <tr>
                        <td colspan="2" align="center">
                <div style="overflow:auto;width:600px;height:350px">
                    <iframe id="iframeId" onload="return selectComments();" width="100%" height="100%" scrolling="auto" marginheight="0" marginwidth="0" frameborder="0" src="<%=request.getContextPath()%>/rep_roles/GrantRevokeRoles.jsp"></iframe>
                </div>
                        </td>
                    </tr>
                    <tr>
                        <td align="left" width="17%">
                            <input type="button" style="width:100" onclick="submitChild()" value="Add Roles">
                        </td>
                        <td align="left">
                            <input type="button" style="width:100" value="Cancel" onclick="hm('grantRevokeDiv')"/>
                        </td>
                    </tr>
                </table>
            </div>

                            </td>
                        </tr>
                    </table>
                </td>
            </tr>

        </table>







    </body>
</html>
