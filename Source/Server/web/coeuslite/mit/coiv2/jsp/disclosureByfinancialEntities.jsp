<%--
    Document   : financialEntities
    Created on : May 8, 2010, 3:25:09 PM
    Author     : Mr
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.Vector,java.util.Date,edu.mit.coeuslite.coiv2.utilities.CoiConstants,
         edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean,edu.mit.coeuslite.utils.bean.SubHeaderBean,
         edu.mit.coeuslite.coiv2.beans.CoiAnnualProjectEntityDetailsBean,edu.mit.coeuslite.coiv2.services.CoiCommonService,
         edu.mit.coeuslite.coiv2.beans.CoiProposalBean,java.util.TreeSet"%>
<html:html locale="true">
<head>
<title>C O I</title>
<script language="javascript" type="text/JavaScript" src="..\scripts\_js.js"></script>
    <script language="javascript" type="text/JavaScript" src="..\scripts\CheckForward.js"></script>
 <%! JspWriter writer;%>
<%
writer = out;
String path = request.getContextPath();%>

      <script src="<%=request.getContextPath()%>/coeuslite/mit/utils/scripts/modal/modaldbox.js"></script>

        <title>CoeusLite</title>
    <script language="javascript" type="text/JavaScript" src="..\scripts\_js.js"></script>
    <script language="javascript" type="text/JavaScript" src="..\scripts\CheckForward.js"></script>
<script src="<%=path%>/coeuslite/dartmouth/utils/scripts/divSlide.js"></script>
<script src="<%=path%>/coeuslite/dartmouth/utils/scripts/Balloon.js"></script>
<link href="<%=path%>/coeuslite/dartmouth/utils/css/coeus_styles.css" rel="stylesheet" type="text/css"/>
<link href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/layout.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/styles.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/collapsemenu.css" rel="stylesheet" type="text/css" />
<style>
            #mbox{background-color:#6e97cf; padding:0px 8px 8px 8px; border:3px solid #095796;}
            #mbm{font-family:sans-serif;font-weight:bold;float:right;padding-bottom:5px;}
            #ol{background-image: url('../coeuslite/mit/utils/scripts/modal/overlay.png');}
            .dialog {display:none}

            * html #ol{background-image:none; filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src="../coeuslite/mit/utils/scripts/modal/overlay.png", sizingMethod="scale");}

  </style>
<%
    Vector val = (Vector) request.getAttribute("pjtEntDetView");
     boolean isAdmin = false;
     int historyindex=0;
     Vector pjtList= new Vector();
    if(session.getAttribute("isAdmin") != null) {
    isAdmin = (Boolean) session.getAttribute("isAdmin");
    }
    Vector projectNameList1 = (Vector)request.getAttribute("entityNameList");
     String projectId = "";
     Vector finEntDetails = new Vector();
     String apprvdView = "";
     if(session.getAttribute("checkPrint") != null) {
         apprvdView = (String)session.getAttribute("checkPrint");
      }

     boolean isHistoryView = false;

      if(session.getAttribute("historyView") != null) {
         isHistoryView = (Boolean)session.getAttribute("historyView");
      }

     boolean continuePresent=false;
     boolean isEvent = false;
     if(session.getAttribute("isEvent") != null) {
         isEvent = (Boolean)session.getAttribute("isEvent");
         }
    %>
</head>
<script src="js/jquery.js" type="text/javascript"></script>
<script type="text/javascript">
        function closebtn(){
            hm('divDisclosureStaus');
        }
function selectProj(index){
    var a = "imgtoggle"+index;
    if(document.getElementById(index).style.visibility == 'visible'){
      document.getElementById(index).style.visibility = 'hidden';
      document.getElementById(index).style.height = "1px";
      document.getElementById(a).src="<%=path%>/coeusliteimages/plus.gif";
    }else{
        last();
     ds = new DivSlider();
     ds.showDiv(index,1000);
     document.getElementById(index).style.visibility = 'visible';
     document.getElementById(index).style.height = "100px";
      document.getElementById(a).src="<%=path%>/coeusliteimages/minus.gif";

       var a1 = "imgtoggleview"+index;
    var b1 = "div"+index
    if(document.getElementById(b1).style.visibility == 'visible'){
      document.getElementById(b1).style.visibility = 'hidden';
      document.getElementById(b1).style.height = "1px";
      document.getElementById(a1).src="<%=path%>/coeusliteimages/plus.gif";
    }
    }
}
  function showDisclDialog(divId)
 {
           <logic:empty name="COIProjectDetail">
                   alert("History not found");
          </logic:empty>
          <logic:notEmpty name="COIProjectDetail">
                width =650;// document.getElementById(divId).style.pixelWidth;
                height =250;// document.getElementById(divId).style.pixelHeight;
                sm(divId,width,height);
           </logic:notEmpty>

}
function selectProjView(index){
    var a = "imgtoggleview"+index;
    var b = "div"+index
    if(document.getElementById(b).style.visibility == 'visible'){
      document.getElementById(b).style.visibility = 'hidden';
      document.getElementById(b).style.height = "1px";
      document.getElementById(a).src="<%=path%>/coeusliteimages/plus.gif";
    }else{
        last();
     ds = new DivSlider();
     ds.showDiv(b,1000);
     document.getElementById(b).style.visibility = 'visible';
     document.getElementById(b).style.height = "100px";
      document.getElementById(a).src="<%=path%>/coeusliteimages/minus.gif";
    }
}
     function updateProjectDetails()
        {
            var entCount = 0;
             var k=0;
             var noOfPjts=0;
             var propertyName = "entityStatus";
             var discDetailName = "discDetail";
             var discDetailId = "";
              var entityName = "";
             var entityValue = "";
             var finEntDetails = [];
             <% if(val != null) {
                for (int i = 0; i < val.size(); i++) {%>
                k++;
            <%}}%>
            <% if(projectNameList1 != null) {
            for (int l = 0; l < projectNameList1.size(); l++) {%>
                    noOfPjts++;
            <%}}%>
            for(var m=0; m < noOfPjts; m++) {
                for(var i=0; i< k; i++){
                    entityName = propertyName+m+i;
                    discDetailId = discDetailName+m+i;
                    if(document.getElementById(discDetailId) != null && document.getElementById(entityName) != null) {
                        entityValue =  document.getElementById(discDetailId).value + ":" + document.getElementById(entityName).value;
                        finEntDetails.push(entityValue);
                     }
                }
            }
            document.getElementById("entityDetails").value=finEntDetails;
             document.forms[0].action= '<%=path%>' + "/saveProjectsByFinancialEntitiesView.do?&fromReview=<%= request.getParameter("fromReview")%>";
            document.forms[0].submit();
        }
      function changeStatusCode(index)
        {
            debugger;
            var propertyName = "entityStatus";
            var entityName = "";
            var str=[];
            var k=0;
        <% if(val != null) {
        for (int i = 0; i < val.size(); i++) {%>
         k++;
        <%}}%>
            var dispName = "dispositionStatus"+index;
              for(var i=0; i< k; i++)
                {
                    entityName = propertyName+index+i;
                    var value=document.getElementById(dispName).value;
                    if(document.getElementById(entityName) != null) {
                        document.getElementById(entityName).value=value;
                    }
                }
            }

     function sortNumber(a,b)
        {
        return b - a;
        }

  function changePjtStatus(startIndex,numOfEntity) {
        var propertyName = "entityStatus";
         var entityName = "";
         var entityList = new Array();
         var dispositionStatus = "dispositionStatus"+startIndex;

         for(var i = 0; i<numOfEntity; i++){
              entityName = propertyName+startIndex+i;
              var value;

              if(document.getElementById(entityName) != null) {
                 value = document.getElementById(entityName).value;
                    entityList[i]= value;
               }
          }
          entityList.sort(sortNumber);
          var worstStatus = entityList[0];

        document.getElementById(dispositionStatus).value=worstStatus;
   }


function OpenDetailsInfoPage(linkParameter)
          {
            var winleft = (screen.width - 1150) / 2;
            var winUp = (screen.height - 450) / 2;
            var win = "scrollbars=1,resizable=1,width=500,height=300,left="+winleft+",top="+winUp
            //var win = "toolbar=0,scrollbars=1,statusbar=0,menubar=0,resizable=1,width=650,height=400,left="+winleft+",top="+winUp
            sList = window.open('<%=request.getContextPath()%>'+"/ShowDisclosureDetails.do?coiProjectId="+linkParameter, "list", win);

          }
   function showAllProjects() {
       if(document.getElementById("pjtList").style.display == "none") {
           document.getElementById("pjtList").style.display="block";
           document.getElementById("showAll").style.display = "none";
           document.getElementById("hideAll").style.display = "block";
       }
   }

   function hideAllProjects() {

        if(document.getElementById("pjtList").style.display == "block") {
           document.getElementById("pjtList").style.display="none";
           document.getElementById("showAll").style.display = "block";
           document.getElementById("hideAll").style.display = "none";
       }
   }


    function showComment(DescriptionDetails)
 {
     var width  =650;// document.getElementById('divTXTDetails').style.pixelWidth;
     var height =320; document.getElementById('divTXTDetails').style.pixelHeight;
     sm('divTXTDetails',width,height);

     var descriptionList =DescriptionDetails.split("##");
     var entityName = "";
     var relationDesc = "";
     var orgReltnDesc = "";
     var pjtTitle = "";
     var pjtId = "";

     if(descriptionList[0] != null) {
         entityName = descriptionList[0];
     }
     if(descriptionList[1] != null) {
         relationDesc = descriptionList[1];
         if(relationDesc == 'null') {
             relationDesc = "";
         }
     }
     if(descriptionList[2] != null) {
         orgReltnDesc = descriptionList[2];
          if(orgReltnDesc == 'null') {
             orgReltnDesc = "";
         }
     }
     if(descriptionList[3] != null) {
         pjtTitle = descriptionList[3];
          if(pjtTitle == 'null') {
             pjtTitle = "";
         }
     }
     if(descriptionList[4] != null) {
         pjtId = descriptionList[4];
          if(pjtId == 'null') {
             pjtId = "";
         }
     }

     document.getElementById('entName').innerHTML=entityName;
     document.getElementById('reldesc').innerHTML=relationDesc;
     document.getElementById('relOrgn').innerHTML=orgReltnDesc;
     document.getElementById('pjtTitle').innerHTML=pjtTitle;
     document.getElementById('pjtId').innerHTML=pjtId;

     document.getElementById("mbox").style.left="385";
     document.getElementById("mbox").style.top="250";
 }
  function saveAndcontinue()
 {
     document.forms[0].action= '<%=path%>'+"/attachments.do";
     document.forms[0].submit();
 }
 function show(entNo){

                var width1 = (screen.width - 650) / 2;
                var height1 = (screen.height - 450) / 2;
                var win = "scrollbars=1,resizable=1,width=800,height=450,left="+width1+",top="+height1;
                sList= window.open('<%=request.getContextPath()%>'+"/reviewAnnFinEntViewCoiv2.do?fromByFinEnt=true&entityNumber="+entNo,"");
            }

function showDialog(txtAreaID,id)
 {
     var width  = 650;//document.getElementById('divTXTDetails').style.pixelWidth;
     var height = 170;//document.getElementById('divTXTDetails').style.pixelHeight;

     sm(id,width,height);
     var txtValue=document.getElementById(txtAreaID).value;
     document.getElementById('TxtAreaComments').innerHTML=txtValue;
     document.getElementById("mbox").style.left="385";//450
     document.getElementById("mbox").style.top="250";//250
     //document.getElementById("mbox").style.position="fixed";
 }
 function showDesc(txtAreaID,id)
 {
     var width  = 650;//document.getElementById('divTXTDetails').style.pixelWidth;
     var height = 170;//document.getElementById('divTXTDetails').style.pixelHeight;

     sm(id,width,height);
     var txtValue=document.getElementById(txtAreaID).value;
     document.getElementById('TxtAreaCommnt').innerHTML=txtValue;
     document.getElementById("mbox").style.left="385";//450
     document.getElementById("mbox").style.top="250";//250
 }
 function showTitle(txtAreaID,id)
 {
     var width  = 650;//document.getElementById('divTXTDetails').style.pixelWidth;
     var height = 170;//document.getElementById('divTXTDetails').style.pixelHeight;

     sm(id,width,height);

     var txtValue=document.getElementById(txtAreaID).value;
     document.getElementById('TxtArea').innerHTML=txtValue;
     document.getElementById("mbox").style.left="385";//450
     document.getElementById("mbox").style.top="250";//250
 }
</script>
    <html:form action="/saveProjectsByFinancialEntitiesView.do">
    <body>
   <table id="bodyTable" class="table" style="width: 100%;height: 100%;" border="0" >
<tr style="background-color:#6E97CF;height:22px;"> 
     <td width="2%" style="background-color:#6E97CF;">&nbsp;</td>
    <td style="width: 14%;background-color:#6E97CF;color:#FFFFFF;float:none;font-size:12px;font-weight:bold;margin: 0;text-align: left; "><strong>&nbsp;Discl.Event</strong></td>
    <td style="width: 14%;background-color:#6E97CF;color:#FFFFFF;float:none;font-size:12px;font-weight:bold;margin: 0;text-align: left;"><strong>&nbsp;Project #</strong></td>
    <td style="width: 32%;background-color:#6E97CF;color:#FFFFFF;float:none;font-size:12px;font-weight:bold;margin: 0;text-align: left;"><strong>&nbsp;Project Name</strong></td>
    <td style="width: 22%;background-color:#6E97CF;color:#FFFFFF;float:none;font-size:12px;font-weight:bold;margin: 0;text-align: left;"><strong>&nbsp;Discl.Event Status</strong></td>
    <td  width="10%"align="right"><a href="<%=path%>/financialentities.do">View By FinancialEntities</a></td>

        <%String projectType = (String)request.getSession().getAttribute("param5"); %>
</tr>
<%
   Vector entlist = new Vector()  ;
 if(request.getAttribute("entityNameList") != null) {
      entlist = (Vector)request.getAttribute("entityNameList");

     if(entlist != null && entlist.size() == 0) {
         if(request.getAttribute("entityNameListCurr") == null){
         %>
          <tr>
        <td colspan="8">
        <font color="red">No Projects found</font>
        </td>
        </tr>
        <tr><td>
            <html:button onclick="javaScript:saveAndcontinue();" property="Save" styleClass="clsavebutton" style="width:150px;">
                 Continue
             </html:button>
                 <%continuePresent = true;%>
            </td>
        </tr>
    <%}
         }
     }else { %>
        <tr>
        <td colspan="8">
        <font color="red">No Projects found</font>
        </td>
        </tr>
        <tr><td>
            <html:button onclick="javaScript:saveAndcontinue();" property="Save" styleClass="clsavebutton" style="width:150px;">
                 Continue
             </html:button>
            </td>
            <%continuePresent = true;%>
        </tr>
            <%}
 %>

   </table>
    <table id="bodyTable" class="table" style="width: 100%;height: 100%;" border="0" >
     <logic:present name="entityNameListCurr">

             <tr><td>

         <table id="bodyTable" class="table" style="width: 100%;height: 100%;" border="0" >
               <%
                    String strBgColor1 = "#DCE5F1";
                    Vector projectList = (Vector)request.getAttribute("entityNameListCurr");
                      int startIndex = 0;
                      String linkFeed1;
                      String entityNum1 = "";
                      boolean propPresent = false;
                      boolean irbPresent = false;
                      boolean iacucPresent = false;
                      boolean awardPresent = false;

                      TreeSet entStatusId = new TreeSet();
                            String pjtId = "";
                            String entStatusDescription = "";
                            String entStatusCode = "";
                        Vector entDetList = new Vector();

                        if(request.getAttribute("pjtEntDetViewCurr") != null) {
                            entDetList = (Vector)request.getAttribute("pjtEntDetViewCurr");
                        }
               %>
        <logic:iterate id="pjtTitle" name="entityNameListCurr">
                 <%
                            String startDate = "";
                            String endDate = "";
                            if (startIndex%2 == 0) {
                            strBgColor1 = "#D6DCE5";
                            } else {
                            strBgColor1="#DCE5F1";
                            }
                        CoiAnnualProjectEntityDetailsBean pjtBean1 = (CoiAnnualProjectEntityDetailsBean) projectList.get(startIndex);
                        CoiCommonService coiCommonService1 = CoiCommonService.getInstance();
                        projectId = pjtBean1.getCoiProjectId();
                        linkFeed1=projectId+"-"+pjtBean1.getCoiDisclosureNumber();
                        String  link = "/ShowDisclosureDetails.do?coiProjectId="+linkFeed1;
                        entityNum1 = pjtBean1.getEntityNumber();
                        int moduleCode1 = pjtBean1.getModuleCode();

                       if(moduleCode1 == 1) {
                           projectType = "Award";
                       }
                       else if(moduleCode1 == 2){
                           projectType = "Proposal";
                       }
                       else if(moduleCode1 == 3){
                           projectType = "Proposal";
                       }
                      else if(moduleCode1 == 7){
                           projectType = "IRB Protocol";
                       }
                       else if(moduleCode1 == 9){
                           projectType = "IACUC Protocol";
                       }
                        else if(moduleCode1 == 0){
                           projectType = "Travel";
                       }

                        pjtId = (String)pjtBean1.getCoiProjectId();
                            entStatusId = new TreeSet();
                                      if(entDetList != null) {
                                          String entPjtId = "";
                                        for(int i=0; i < entDetList.size(); i++) {
                                            CoiAnnualProjectEntityDetailsBean entBean = (CoiAnnualProjectEntityDetailsBean) entDetList.get(i);
                                            entPjtId = (String)entBean.getCoiProjectId();

                                            if(pjtId.trim().equals(entPjtId.trim())) {
                                                String entstatus = entBean.getEntityStatusCode() + ":" + entBean.getEntityStatus();
                                                entStatusId.add(entstatus);
                                            }
                                        }
                                      }

                                      if(entStatusId != null && entStatusId.size() > 0) {
                                         String statusDescCode =  (String)entStatusId.last();
                                         String[] splitList = statusDescCode.split(":");
                                         entStatusDescription =  splitList[1];
                                         entStatusCode =  splitList[0];
                                      }
                            if(!propPresent && (moduleCode1 == 2 || moduleCode1 == 3)) {
                      %>
                      <tr>
                          <td width="1px"></td>
                          <td colspan="4">
                              <label style="color: #333333;font-family:Arial,Helvetica,sans-serif;font-size:12px;">Following Proposals included in this disclosure</label>
                          </td>
                      </tr>
                      <%
                        propPresent = true;
                       }
                         if(!irbPresent && moduleCode1 == 7){
                       %>
                        <tr>
                          <td width="1px"></td>
                          <td colspan="4">
                              <label style="color: #333333;font-family:Arial,Helvetica,sans-serif;font-size:12px;">Following IRB Protocols included in this disclosure</label>
                          </td>
                      </tr>
                       <%
                        irbPresent = true;
                        }
                        if(!iacucPresent && moduleCode1 == 9){
                       %>

                       <tr>
                          <td width="1px"></td>
                          <td colspan="4">
                              <label style="color: #333333;font-family:Arial,Helvetica,sans-serif;font-size:12px;">Following IACUC Protocols included in this disclosure</label>
                          </td>
                      </tr>
                       <%
                            iacucPresent = true;
                        }
                        if(!awardPresent && moduleCode1 == 1) {
                       %>
                         <tr>
                          <td width="1px"></td>
                          <td colspan="4">
                              <label style="color: #333333;font-family:Arial,Helvetica,sans-serif;font-size:12px;">Following Awards included in this disclosure</label>
                          </td>
                      </tr>
                       <%
                             awardPresent = true;
                            }
                        %>
         <%if(moduleCode1 !=0) {%>
         <tr bgcolor="<%=strBgColor1%>" id="row<%=startIndex%>" class="rowLine" onmouseover="className='rowHover rowLine'" onmouseout="className='rowLine'" height="22px">
             <td style="text-align: left;" width="2%">
           <img src='<%=request.getContextPath()%>/coeusliteimages/plus.gif' border='none' id="imgtoggleview<%=startIndex%>" name="imgtoggleview<%=startIndex%>" border="none" onclick="javascript:selectProjView(<%=startIndex%>);"/>
           <img src='<%=request.getContextPath()%>/coeusliteimages/minus.gif' style="visibility: hidden;" border='none' id="imgtoggleminusview<%=startIndex%>" name="imgtoggleminusview<%=startIndex%>" border="none" onclick="javascript:selectProjView(<%=startIndex%>);"/>
         </td>
         <td  style="text-align: left;" width="14%">&nbsp;<%=projectType%></td>
         <td  style="text-align: left;" width="14%">&nbsp;<bean:write name="pjtTitle" property="coiProjectId"/></td>
         <td  style="text-align: left;" width="32%">&nbsp;<bean:write name="pjtTitle" property="coiProjectTitle"/></td>
            <%
               if(isAdmin && entityNum1 != null && !apprvdView.equalsIgnoreCase("approvedDisclosureview") && !isHistoryView)
            {%>
            <td  style="text-align: left;" width="22%">
                <select id="dispositionStatus<%=startIndex%>" name="dispDisclStatusForm" style="width: 100%" onchange="changeStatusCode(<%=startIndex%>);">
                <%--<option value="<bean:write name="pjtTitle" property="entityStatusCode"/>"><bean:write name="pjtTitle" property="entityStatus"/></option>--%>
                <option value="<%=entStatusCode%>"><%=entStatusDescription%></option>
                <logic:present name="typeList">
                <logic:iterate id="statusList" name="typeList">
                <option value="<bean:write name="statusList" property="code"/>"><bean:write name="statusList" property="description"/></option>
                </logic:iterate>
                </logic:present>
               </select>
            </td>
            <%} else if(entityNum1 != null){%>
            <td  style="text-align: left;" width="22%">&nbsp;<%--<bean:write name="pjtTitle" property="entityStatus"/>--%><%=entStatusDescription%></td>
            <%} else{%>
            <td  style="text-align: left;" width="22%">&nbsp;</td>
            <%}%>
            <td  align="center" width="10%">&nbsp;&nbsp;&nbsp;&nbsp; <a href="javascript:populateHistoryList('<%=linkFeed1%>')" </a>History</td>
         </tr> <tr>
            <td colspan="6">
                <div id="div<%=startIndex%>" style="height: 1px;width: 100%;visibility: hidden;background-color: #9DBFE9;overflow-x: hidden; overflow-y: scroll;">
                 <!--[if IE]>
            <style type="text/css">
                body {word-wrap: break-word;}
                </style>
                <![endif]-->
                    <table id="bodyTable" class="table" style="width: 100% ;height: 100%;" border="0" >
                <tr>
                    <td style="width:100px;background-color:#6E97CF;color:#FFFFFF;font-size:12px;font-weight:bold;"> &nbsp;<strong>Entity Name</strong></td>
                    <td style="width:200px;background-color:#6E97CF;color:#FFFFFF;font-size:12px;font-weight:bold;"> &nbsp;<strong>Entity Business Focus</strong></td>
                    <td style="background-color:#6E97CF;color:#FFFFFF;font-size:12px;font-weight:bold;"><strong>Relationship Description</strong></td>
                    <td style="background-color:#6E97CF;color:#FFFFFF;font-size:12px;font-weight:bold;"><strong>Entity Status</strong></td>
                    <%--<td width="5%" align="left" style="background-color:#6E97CF;color:#FFFFFF;float:none;font-size:14px;font-weight:bold;margin: 0;"><strong>&nbsp;&nbsp;Comments</strong></td>--%>

                </tr>
                <% int startIndex1=0;
                    int countPresent=0;
                    Vector entNameList = (Vector)request.getAttribute("pjtEntDetView");
                    int numOfEntity=0;
                %>

                 <logic:present name="pjtEntDetViewCurr">
                    <logic:iterate id="pjtEntView" name="pjtEntDetViewCurr">

                    <logic:equal value="<%=pjtId%>" property="coiProjectId" name="pjtEntView">
                        <%numOfEntity++;%>
                    </logic:equal>
                    </logic:iterate>
                </logic:present>

                <logic:present name="pjtEntDetViewCurr">
                    <logic:iterate id="pjtEntView" name="pjtEntDetViewCurr">
                            <% String projectName = (String)pjtBean1.getCoiProjectId();
                                CoiAnnualProjectEntityDetailsBean entityBean = (CoiAnnualProjectEntityDetailsBean)entNameList.get(startIndex1);
                                String entityNumber=entityBean.getEntityNumber();
                           String DescDetails = "";
                           String commentObj=entityBean.getRelationshipDescription();
                             commentObj = commentObj.replaceAll("\"", " ");
                            String comments="";
                            if(commentObj==null){comments="";}
                            else{comments=(String)commentObj;
                                if(comments.trim().length()>64)
                                    {comments=(String)comments.substring(0,64);
                                     comments+="<a href=\"javaScript:showDialog('txtAreaCommentId1"+startIndex1+"','divTXTDetails1')\">[...]</a>";
                            }}
                                 String commentOb=entityBean.getOrgRelationDescription();
                            commentOb = commentOb.replaceAll("\"", " ");
                            String commnt="";
                            if(commentOb==null){commnt="";}
                            else{
                                commnt=commentOb;
                                if(commnt.trim().length()>60)
                                    {commnt=commnt.substring(0,62);
                                    commnt+="<a href=\"javaScript:showDesc('txtAreaCommentId2"+startIndex1+"','divTXTDetails2')\">[...]</a>";

                             }}
                            %>
                    <logic:equal value="<%=pjtId%>" property="coiProjectId" name="pjtEntView">
                     <%
                        DescDetails += entityBean.getEntityName()+"##";
                        DescDetails += entityBean.getRelationshipDescription()+"##";
                        DescDetails += entityBean.getOrgRelationDescription();
                     %>
                    <logic:notEmpty name="pjtEntView" property="entityName">
                      <tr class="rowLineLight" onmouseover="className='rowHover rowLine'" onmouseout="className='rowLineLight'" style="height: 22px;">
                        <td>
                            &nbsp;<b><a href="javascript:show('<%=entityNumber%>')"><u><bean:write name="pjtEntView" property="entityName"/></u></a></b>
                        </td>
                        <td>
                            <div style="word-wrap: break-word;width:254px;float:left;overflow: hidden"><%=comments%></div>                       
                        </td>
                        <input id="txtAreaCommentId1<%=startIndex1%>" name="finEntDet" type="hidden" value="<%=commentObj%>" />
                         <td style="width:254px !important;">
                             <div style="word-wrap: break-word;width:254px;float:left;overflow: hidden"><%=commnt%></div>
                         </td>
                <input id="txtAreaCommentId2<%=startIndex1%>" name="finEntDet" type="hidden" value="<%=commentOb%>" />
                            <%countPresent = 1;%>
                            <%if(isAdmin && !apprvdView.equalsIgnoreCase("approvedDisclosureview") && !isHistoryView) {%>
                            <td style="text-align: left;">
                             <div style="word-wrap: break-word;width:154px;">
                                <select id="entityStatus<%=startIndex%><%=startIndex1%>" name="dispDisclStatusForm" onchange="changePjtStatus('<%=startIndex%>','<%=numOfEntity%>');">
                            <option value="<bean:write name="pjtEntView" property="entityStatusCode"/>"><bean:write name="pjtEntView" property="entityStatus"/></option>
                             <logic:present name="typeList">
                                <logic:iterate id="statusList" name="typeList">
                                <option value="<bean:write name="statusList" property="code"/>"><bean:write name="statusList" property="description"/></option>
                                </logic:iterate>
                             </logic:present>
                            </select>
                               <input type="hidden" id="discDetail<%=startIndex%><%=startIndex1%>" value="<bean:write name="pjtEntView" property="coiDiscDetailsNumber"/>" />
                             </div>
                                </td>
                           <%} else {%>
                            <td>
                                <b><bean:write name="pjtEntView" property="entityStatus"/></b>
                            </td>
                           <%}%>

                     </tr>
                   </logic:notEmpty>                     
                   </logic:equal>
                     <% startIndex1++; %>
                    </logic:iterate>
                   <%if(countPresent == 0) {%>
                    <tr><td colspan="2" style="padding-left: 25px;">
                           <font style="color: red">No financial entities found</font>
                    </td></tr>
                   <%} else {%>
                       <logic:empty name="pjtEntView" property="entityName">
                            <tr><td colspan="2" style="padding-left: 25px;">
                                    <font style="color: red">No financial entities found</font>
                            </td></tr>
                        </logic:empty>
                      <%}%>
                 </logic:present>
               </table>
              </div>
          </td>
          </tr>
          <%}%>
         <%startIndex++;%>
        </logic:iterate>
                <%request.getSession().setAttribute("lastIndex",startIndex);%>
                 <%
                  if(isAdmin && entityNum1 != null && !(entlist != null && entlist.size() > 0) && !apprvdView.equalsIgnoreCase("approvedDisclosureview") && !isHistoryView) {%>
                  <logic:notPresent name="historyView">
                 <tr>
                    <td>
                        <html:button styleClass="clsavebutton"  value="Save" property="save" onclick="updateProjectDetails();"/>
                    </td>
                              </tr>
                  </logic:notPresent>
                 <%}%>

        </table>
                 </td></tr>
             <tr><td>
        </logic:present>
            <logic:present name="entityNameListCurr">
                <div id="pjtList" style="display:none; overflow: hidden;" align="left">
             </logic:present>

                    <table id="bodyTable" class="table" style="width: 100%;height: 100%;" border="0">
        <logic:present name="entityNameList">
               <%
                    String strBgColor = "#DCE5F1";
                    Vector projectNameList = (Vector)request.getAttribute("entityNameList");
                      int index = 0;
                      String linkFeed;
                      String entityNum = "";
                      boolean propPresent_v = false;
                      boolean irbPresent_v = false;
                      boolean iacucPresent_v = false;
                      boolean awardPresent_v = false;

                       TreeSet entStatusId_v = new TreeSet();

                            String entStatusDescription_v = "";
                            String entStatusCode_v = "";
                        Vector entDetList_v = new Vector();

                        if(request.getAttribute("pjtEntDetView") != null) {
                            entDetList_v = (Vector)request.getAttribute("pjtEntDetView");
                        }
               %>
        <logic:iterate id="pjtTitle" name="entityNameList">
                 <%
                            String startDate1 = "";
                            String endDate1 = "";

                            if (index%2 == 0) {
                            strBgColor = "#D6DCE5";
                            } else {
                            strBgColor="#DCE5F1";
                            }
                         CoiAnnualProjectEntityDetailsBean pjtBean = (CoiAnnualProjectEntityDetailsBean) projectNameList.get(index);
                        //CoiCommonService coiCommonService1 = CoiCommonService.getInstance();
                        projectId = pjtBean.getCoiProjectId();
                        linkFeed=projectId+"-"+pjtBean.getCoiDisclosureNumber();
                        String  link1 = "/ShowDisclosureDetails.do?coiProjectId="+linkFeed;
                        entityNum = pjtBean.getEntityNumber();
                         int moduleCode = pjtBean.getModuleCode();

                       if(moduleCode == 1) {
                           projectType = "Award";
                       }
                       else if(moduleCode == 2){
                           projectType = "Proposal";
                           }
                      else if(moduleCode == 3){
                           projectType = "Proposal";
                           }
                    else if(moduleCode == 7){
                           projectType = "IRB Protocol";
                      }
                       else if(moduleCode == 9){
                           projectType = "IACUC Protocol";
                        }
                        else if(moduleCode == 0){
                           projectType = "Travel";
                       }

                         entStatusId_v = new TreeSet();

                         if(entDetList_v != null) {
                              String entPjtId = "";
                            for(int i=0; i < entDetList_v.size(); i++) {
                                CoiAnnualProjectEntityDetailsBean entBean = (CoiAnnualProjectEntityDetailsBean) entDetList_v.get(i);
                                entPjtId = (String)entBean.getCoiProjectId();

                                if(projectId.trim().equals(entPjtId.trim())) {
                                    String entstatus = entBean.getEntityStatusCode() + ":" + entBean.getEntityStatus();
                                    entStatusId_v.add(entstatus);
                                }
                            }
                          }

                          if(entStatusId_v != null && entStatusId_v.size() > 0) {
                             String statusDescCode =  (String)entStatusId_v.last();
                             String[] splitList = statusDescCode.split(":");
                             entStatusDescription_v =  splitList[1];
                             entStatusCode_v =  splitList[0];
                          }
                      if(!propPresent_v && (moduleCode == 2 || moduleCode == 3)) {
                      %>
                      <tr>
                          <td width="1px"></td>
                          <td colspan="4">
                              <label style="color: #333333;font-family:Arial,Helvetica,sans-serif;font-size:12px;">Following Proposals included in this disclosure</label>
                          </td>
                      </tr>
                      <% propPresent_v = true;
                        }

                         if(!irbPresent_v && moduleCode == 7) {
                      %>
                        <tr>
                          <td width="1px"></td>
                          <td colspan="4">
                              <label style="color: #333333;font-family:Arial,Helvetica,sans-serif;font-size:12px;">Following IRB Protocols included in this disclosure</label>
                          </td>
                      </tr>
                      <%
                        irbPresent_v = true;
                        }
                        if(!iacucPresent_v && moduleCode == 9){
                      %>
                        <tr>
                          <td width="1px"></td>
                          <td colspan="4">
                              <label style="color: #333333;font-family:Arial,Helvetica,sans-serif;font-size:12px;">Following IACUC Protocols included in this disclosure</label>
                          </td>
                      </tr>
                      <%
                        iacucPresent_v = true;
                       }
                        if(!awardPresent_v && moduleCode == 1) {
                      %>
                      <tr>
                          <td width="1px"></td>
                          <td colspan="4">
                              <label style="color: #333333;font-family:Arial,Helvetica,sans-serif;font-size:12px;">Following Awards  included in this disclosure</label>
                          </td>
                      </tr>
                      <%
                        awardPresent_v = true;
                        }
                      %>
                   <%if(moduleCode != 0) {%>
      <tr bgcolor="<%=strBgColor%>" id="row<%=index%>" class="rowLine" onmouseover="className='rowHover rowLine'" onmouseout="className='rowLine'" height="22px" align="left">
        <td style="text-align: left;width: 2px">
           <img src='<%=request.getContextPath()%>/coeusliteimages/plus.gif' border='none' id="imgtoggle<%=index%>" name="imgtoggle<%=index%>" border="none" onclick="javascript:selectProj(<%=index%>);"/>
           <img src='<%=request.getContextPath()%>/coeusliteimages/minus.gif' style="visibility: hidden;" border='none' id="imgtoggleminus<%=index%>" name="imgtoggleminus<%=index%>" border="none" onclick="javascript:selectProj(<%=index%>);"/>
        </td>
        <td  width="14%" align="left" style="text-align: left;">&nbsp;<%=projectType%></td>
        <td width="14%" style="text-align: left;">&nbsp;<bean:write name="pjtTitle" property="coiProjectId"/></td>
        <td width="32%" style="text-align: left;">
            <%  String relatn=pjtBean.getCoiProjectTitle();
            String comment="";
        if(relatn==null){comment="";}
        else{comment=(String)relatn;
            if(comment.trim().length()>88)
                {comment=(String)comment.substring(0,88);
                 comment+="<a href=\"javaScript:showTitle('txtAreaComment"+index+"','divTXT')\">[...]</a>";
            }}%><%=comment%>
            <input id="txtAreaComment<%=index%>" name="finEntDet" type="hidden" value="<%=pjtBean.getCoiProjectTitle()%>" />
        </td>
            <%
           if(isAdmin && entityNum != null && !apprvdView.equalsIgnoreCase("approvedDisclosureview") && !isHistoryView) {%>
           <td width="22%" style="text-align: left;">
            <select id="dispositionStatus<%=index%>" name="dispDisclStatusForm" style="width: 100%" onchange="changeStatusCode(<%=index%>);">
            <%--<option value="<bean:write name="pjtTitle" property="entityStatusCode"/>"><bean:write name="pjtTitle" property="entityStatus"/></option>--%>
            <option value="<%=entStatusCode_v%>"><%=entStatusDescription_v%></option>
            <logic:present name="typeList">
            <logic:iterate id="statusList" name="typeList">
            <option value="<bean:write name="statusList" property="code"/>"><bean:write name="statusList" property="description"/></option>
            </logic:iterate>
            </logic:present>
            </select>
         </td>
        <%} else if(entityNum != null){%>
        <td  width="22%"><%--<bean:write name="pjtTitle" property="entityStatus"/>--%><%=entStatusDescription_v%></td>
        <%} else{%>
        <td width="22%">&nbsp;</td>
        <%}%>
        <td align="center" width="10%">&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:populateHistoryList('<%=linkFeed%>')" >History  </a><%--<html:link action="<%=link1%>">History</html:link>--%></td>
        </tr>
            <tr>
            <td colspan="6">               
                <div id="<%=index%>" style="height: 1px;width: 100%;visibility: hidden;overflow: hidden;background-color: #9DBFE9;overflow-x: hidden; overflow-y: scroll;">
            <table id="bodyTable" class="table" style="width: 100% ;height: 100%;" border="0" >
                <tr style="height: 3px;">
              <td width="14%" style="background-color:#6E97CF;color:#FFFFFF;font-size:12px;font-weight:bold;" nowrap><strong>Entity Name</strong></td>
            <td width="30%" style="background-color:#6E97CF;color:#FFFFFF;font-size:12px;font-weight:bold;"><strong>&nbsp;Entity Business Focus</strong></td>
            <td width="30%"style="background-color:#6E97CF;color:#FFFFFF;font-size:12px;font-weight:bold;"><strong>Relationship Description</strong></td>
            <td width="22%" style="background-color:#6E97CF;color:#FFFFFF;font-size:12px;font-weight:bold;"><strong>Entity Status</strong></td>
            <%--<td width="5%" align="left" style="background-color:#6E97CF;color:#FFFFFF;float:none;font-size:14px;font-weight:bold;margin: 0;"><strong>&nbsp;&nbsp;Comments</strong></td>--%>
            </tr>
                <% int index1=0;
                    int countPresent1=0;
                    Vector entityNameList = (Vector)request.getAttribute("pjtEntDetView");
                    int entityCount=0;
                %>

                <logic:present name="pjtEntDetView">
                <logic:iterate id="pjtEntView" name="pjtEntDetView">
                        <% String projName1 = (String)pjtBean.getCoiProjectId();
                        %>
                    <logic:equal value="<%=projectId%>" property="coiProjectId" name="pjtEntView">
                       <% entityCount++;%>
                    </logic:equal>
                </logic:iterate>
                </logic:present>


            <logic:present name="pjtEntDetView">
                <logic:iterate id="pjtEntView" name="pjtEntDetView">
                        <% String projectName1 = (String)pjtBean.getCoiProjectId();
                           
                        %>
                    <logic:equal value="<%=projectId%>" property="coiProjectId" name="pjtEntView">
                        <%
                           CoiAnnualProjectEntityDetailsBean entBean = (CoiAnnualProjectEntityDetailsBean)entityNameList.get(index1);
                           String DescriptionDetails = "";
                           String entityNumber=entBean.getEntityNumber();
                        DescriptionDetails += entBean.getEntityName()+"##";
                        DescriptionDetails += entBean.getRelationshipDescription()+"##";
                        DescriptionDetails += entBean.getOrgRelationDescription()+"##";
                        DescriptionDetails += entBean.getCoiProjectTitle()+"##" ;
                        DescriptionDetails += entBean.getCoiProjectId()+"##" ;

                            String commentObj=entBean.getRelationshipDescription();
                            commentObj = commentObj.replaceAll("\"", " ");
                            String comments="";
                            if(commentObj==null){comments="";}
                            else{
                                comments=commentObj;
                                if(comments.trim().length()>58)
                                    {comments=comments.substring(0,58);
                                    comments+="<a href=\"javaScript:showDialog('txtAreaCommentId"+index1+"','divTXTDetails1')\">[...]</a>";
                             }}

                            String commentOb=entBean.getOrgRelationDescription();
                            commentOb = commentOb.replaceAll("\"", " ");
                            String commnt="";
                            if(commentOb==null){commnt="";}
                            else{
                                commnt=commentOb;
                                if(commnt.trim().length()>60)
                                    {commnt=commnt.substring(0,62);
                                    commnt+="<a href=\"javaScript:showDesc('txtAreaCommentId2"+index1+"','divTXTDetails2')\">[...]</a>";

                             }}
                        %>
                <logic:notEmpty name="pjtEntView" property="entityName">
                <tr class="rowLineLight" onmouseover="className='rowHover rowLine'" onmouseout="className='rowLineLight'" style="height: 22px;">
                <td>
                    &nbsp;  <b><a href="javascript:show('<%=entityNumber%>')"><u><bean:write name="pjtEntView" property="entityName"/></u></a></b>
                </td>
                <td>
                    <div style="word-wrap: break-word;width:254px;float:left;overflow: hidden"><%=comments%></div>
                </td>
                <input id="txtAreaCommentId<%=index1%>" name="finEntDet" type="hidden" value="<%=commentObj%>" />
                 <td style="width:254px !important;">
                     <div style="word-wrap: break-word;width:254px;float:left;overflow: hidden"><%=commnt%></div>
                 </td>
                <input id="txtAreaCommentId2<%=index1%>" name="finEntDet" type="hidden" value="<%=commentOb%>" />
                <%
                countPresent1 = 1;%>
             <%   if(isAdmin && !apprvdView.equalsIgnoreCase("approvedDisclosureview") && !isHistoryView) {%>

                <td style="text-align: left;">
               <div style="word-wrap: break-word;width:154px;">
                    <select id="entityStatus<%=index%><%=index1%>" name="dispDisclStatusForm" style="width: 100%" onchange="changePjtStatus('<%=index%>','<%=entityCount%>')">
               <option value="<bean:write name="pjtEntView" property="entityStatusCode"/>"><bean:write name="pjtEntView" property="entityStatus"/></option>
               <logic:present name="typeList">
               <logic:iterate id="statusList" name="typeList">
               <option value="<bean:write name="statusList" property="code"/>"><bean:write name="statusList" property="description"/></option>
               </logic:iterate>
               </logic:present>
               </select>
                   <input type="hidden" id="discDetail<%=index%><%=index1%>" value="<bean:write name="pjtEntView" property="coiDiscDetailsNumber"/>" />
               </div>
                          </td>
            <%} else {%>
                <td>
                    <b><bean:write name="pjtEntView" property="entityStatus"/></b>
                </td>
               <%}%>
                <%--<td width="5%">&nbsp;&nbsp;&nbsp;<a href="javascript:showComment('<%=DescriptionDetails%>')">View</a></td>--%>
               </tr>
                </logic:notEmpty>                
                </logic:equal>
               <% index1++; %>
                </logic:iterate>
               <%if(countPresent1 == 0) {%>
               <tr style="height: 2px;"><td colspan="2" style="padding-left: 25px;">
                    <font style="color: red">No financial entities found</font>
                </td></tr>
               <%} else {%>
                   <logic:empty name="pjtEntView" property="entityName">
                   <tr style="height: 2px;"><td colspan="2" style="padding-left: 25px;">
                             <font style="color: red">No financial entities found</font>
                        </td></tr>
                    </logic:empty>
                  <%}%>
             </logic:present>
            </table>
            </div></td>
      </tr><%}%>
         <%index++;%>
        </logic:iterate>
        <%request.getSession().setAttribute("lastIndex",index);%>
         <%
          if(isAdmin && entityNum != null) {%>
          <logic:notPresent name="historyView">
         <tr>
             <td width="1%"></td>
             <td class='savebutton' align="left" colspan="5">
             <%if(!apprvdView.equalsIgnoreCase("approvedDisclosureview") && !isHistoryView){%>
                <html:button styleClass="clsavebutton"  value="Save" property="save" onclick="updateProjectDetails();" style="width:150px;"/>

            <%}%>
                   <html:button onclick="javaScript:saveAndcontinue();" property="Save" styleClass="clsavebutton" style="width:150px;">
                     Continue
                 </html:button>
                     <%continuePresent = true;%>
            </td>
         </tr>
         </logic:notPresent>
         <%}%>
        </logic:present>
          <logic:present name="entityNameListCurr">
         <tr>
                    <td colspan="6">
                        <html:button styleClass="clsavebutton"  value="Hide All" property="showAll" onclick="hideAllProjects();" style="width:150px;"/>
                        <%if(!isAdmin) {%>
                         <html:button onclick="javaScript:saveAndcontinue();" property="Save" styleClass="clsavebutton" style="width:150px;">
                             Continue
                         </html:button>
                         <%}%>
                    </td>
         </tr>
          </logic:present>
         </table>
        <logic:present name="entityNameListCurr">
        </div>
        </logic:present>
                 </td></tr>
             <logic:notEmpty name="entityNameList">
                 <logic:notPresent name="checkPrint">
    <%
projectType = (String)request.getSession().getAttribute("param5");
if((session.getAttribute("param6") == null)&&!(projectType.equals("Annual")) &&!(projectType.equals("Revision"))){%>
             <tr>
                 <td>
                     <div id="showAll">
                        <html:button styleClass="clsavebutton"  value="Show All" property="showAll" onclick="showAllProjects();" style="width:150px;"/>
                        <%if(!isAdmin) {%>
                     <html:button onclick="javaScript:saveAndcontinue();" property="Save" styleClass="clsavebutton" style="width:150px;">
                         Continue
                     </html:button>
                   <% continuePresent = true;}%>
                  </div>
                 </td></tr>
             <%} %>
            <%-- <%if(session.getAttribute("param6") != null && (projectType.equals("Annual") || projectType.equals("Revision") )) {%>
                    <tr>
                    <td class='savebutton' align="left" colspan="6">
                       <html:button onclick="javaScript:saveAndcontinue();" property="Save" styleClass="clsavebutton" style="width:150px;">
                         Continue
                     </html:button>
                     </td>
                </tr>
             <%}%>--%>
             </logic:notPresent>
             </logic:notEmpty>

             <%--<logic:empty name="entityNameList">--%>
             <logic:present name="checkPrint">
             <%if(!isAdmin) {%>
                <tr>
                    <td class='savebutton' align="left" colspan="6">
                       <html:button onclick="javaScript:saveAndcontinue();" property="Save" styleClass="clsavebutton" style="width:150px;">
                         Continue
                     </html:button>
                     </td>
                </tr>
                <%
                    continuePresent = true;
                    }%>
             </logic:present>

              <%if(!continuePresent) {%>
                <tr>
                    <td class='savebutton' align="left" colspan="6">
                       <html:button onclick="javaScript:saveAndcontinue();" property="Save" styleClass="clsavebutton" style="width:150px;">
                         Continue
                     </html:button>
                     </td>
                </tr>
              <%}%>
              <input type="hidden" id="entityDetails" name="entityDetails" value="test" />
            <%--</logic:empty>--%>
         </table>
         <div id="divTXT" class="dialog" style="width:auto;overflow: hidden;position:absolute;">
    <table width="100%" border="0" cellpadding="1" cellspacing="1" class="table"  >
            <tr style="background-color:#6E97CF;font-size:12px;margin: 0;">
                <td style="padding: 2px 0px 5px 0px"> <font color="#FFFFFF" size="2px"><b>Project Name</b></font></td>
            </tr>
            <tr style="height: 100px;width:200px;">

                <td align="left" style="height: 100px;">
                    <div style="height: 100px;width: 650px;overflow-x: scroll; overflow-y: scroll;overflow: auto;">
                    <label id="TxtArea"></label>
                    </div>
                </td>
            </tr>
           <tr style="background-color:#6E97CF;">
              <td align="center" >
                  <input type="button" value="Close" class="clsavebutton" onclick="hm('divTXT');"/>
              </td>

           </tr>
        </table>
               </div>
<div id="divDisclosureStaus" class="dialog" style="width:450px;overflow: hidden;position:absolute;">
    <table class="table" style="width: 100%;" id="historyDet">
                 <tr>
                     <td style="">
                         <label id="HeaderDetails" >hi</label>
                      </td>
                 </tr>
                 <tr><td><label id="HistoryList">hi2</label>
                     </td></tr>
                 <tr><td align="center"><input type="button"  value="Close" class="clsavebutton" onclick="hm('divDisclosureStaus')"/></td></tr>
               </table>
</div>

<div id="divTXTDetails" class="dialog" style="width:450px;height: 320px;overflow: auto;position:absolute;">
        <table width="100%" border="0"  class="table">
             <tr  style="background-color:#6E97CF;font-size:12px;margin: 0;">
                 <td colspan="2" style="padding: 2px 0px 5px 0px"> <font color="#FFFFFF" size="2px"><b>Relationship Comments</b></font>
                </td>
             </tr>
            <tr style="float:none;font-size:12px;margin: 0;">
                <td style="height: 17px;width:80px;"><b>Entity Name: </b></td>
                <td><b><label id="entName"/></b></td>
            </tr>
             <tr style="float:none;font-size:12px;margin: 0;height: 17px;">
                <td><b>Project Title: </b></td>
                <td><b><label id="pjtTitle"/></b></td>
            </tr>
            <tr style="float:none;font-size:12px;margin: 0;">
                <td><b>Project #: </b></td>
                <td><b><label id="pjtId"/></b></td>
            </tr>
            <tr style="float:none;font-size:12px;margin: 0;">
                <td style="height: 15px;" colspan="2">
                <img width="100%" height="1" border="0" src="<%=request.getContextPath()%>/coeusliteimages/line4.gif"/>
                </td>
            </tr>
            <tr style="background-color:#6E97CF;float:none;margin: 0;">
                <td style="height: 25px;" colspan="2"><b>Relationship Description:</b></td>
            </tr>
             <tr style="height: 70px;width: 450px;">
                <td colspan="2" align="left">
                    <div style="height: 70px;width: 650px;overflow-x: scroll; overflow-y: scroll;overflow: auto;">
                    <label id="reldesc"></label> </div></td>
            </tr>

            <tr style="background-color:#6E97CF;float:none;margin: 0;">
                <td style="height: 25px;" colspan="2"><b>Relationship to Organization:</b></td>
            </tr>

             <tr style="height: 70px;width: 450px;">
                <td colspan="2" align="left">
                    <div style="height: 70px;width: 650px;overflow-x: scroll; overflow-y: scroll;overflow: auto;">
                    <label id="relOrgn">
                    </label>
                    </div>
                    </td>
            </tr>

            <tr style="background-color:#6E97CF;">
                <td style="height:30px;" align="center" colspan="2">
                  <input type="button" value="Close"  Class="clsavebutton" onclick="hm('divDisclosureStaus')" />
              </td>

           </tr>
        </table>
    </div>

   <div id="divTXTDetails1" class="dialog" style="width:auto;overflow: hidden;position:absolute;">
    <table width="100%" border="0" cellpadding="1" cellspacing="1" class="table"  >
            <tr  style="background-color:#6E97CF;font-size:12px;margin: 0;">
                <td style="padding: 2px 0px 5px 0px"> <font color="#FFFFFF" size="2px"><b>Entity Business Focus</b></font></td>
            </tr>
            <tr style="height: 100px;width:200px;">
               <%-- <td align="center"> <textarea id="TxtAreaComments" cols="3" style=" height: 70px;resize:none; width: 430px;" disabled ></textarea></td>--%>
                <td align="left" style="height: 100px;">
                    <div style="height: 100px;width: 650px;overflow-x: scroll; overflow-y: scroll;overflow: auto;">
                    <label id="TxtAreaComments"></label>
                    </div>
                </td>
            </tr>
           <tr style="background-color:#6E97CF;">
              <td align="center" >
                  <input type="button" value="Close" class="clsavebutton" onclick="hm('divTXTDetails1');"/>
              </td>

           </tr>
        </table>
    </div>
                <div id="divTXTDetails2" class="dialog" style="width:auto;overflow: hidden;position:absolute;">
    <table width="100%" border="0" cellpadding="1" cellspacing="1" class="table"  >
            <tr  style="background-color:#6E97CF;font-size:12px;margin: 0;">
                <td style="padding: 2px 0px 5px 0px"> <font color="#FFFFFF" size="2px"><b>Relationship Description</b></font></td>
            </tr>
            <tr style="height: 100px;width:200px;">
               <%-- <td align="center"> <textarea id="TxtAreaComments" cols="3" style=" height: 70px;resize:none; width: 430px;" disabled ></textarea></td>--%>
                <td align="left" style="height: 100px;">
                    <div style="height: 100px;width: 650px;overflow-x: scroll; overflow-y: scroll;overflow: auto;">
                    <label id="TxtAreaCommnt"></label>
                    </div>
                </td>
            </tr>
           <tr style="background-color:#6E97CF;">
              <td align="center" >
                  <input type="button" value="Close" class="clsavebutton" onclick="hm('divTXTDetails2');"/>
              </td>

           </tr>
        </table>
    </div>

<script>

 function last(){
      var lastinx = "<%=request.getSession().getAttribute("lastIndex")%>";
      for(var i=0;i<lastinx;i=i+1){
      var b = "imgtoggle"+i;
     if(document.getElementById(i).style.visibility == 'visible'){
      document.getElementById(i).style.visibility = 'hidden';
      document.getElementById(i).style.height = "1px";
      document.getElementById(b).src="<%=path%>/coeusliteimages/plus.gif";
}
}}
</script>
<logic:notEmpty name="COIProjectDetailList">
     <script type="text/javascript">
           var  width =650;// document.getElementById("divDisclosureStaus").style.pixelWidth;
           var  height =250;//document.getElementById("divDisclosureStaus").style.pixelHeight;
            sm("divDisclosureStaus",width,height);
            document.getElementById("mbox").style.left="385";//450
            document.getElementById("mbox").style.top="200";//250
     </script>
</logic:notEmpty>
      <script type="text/javascript">
function populateHistoryList(coipjtId)
    {
        var HttpRequest=init();
        function init(){
            if (window.XMLHttpRequest) {
                return new XMLHttpRequest();
            } else if (window.ActiveXObject) {
                  return new ActiveXObject("Microsoft.XMLHTTP");
            }
            else
            {
                return new ActiveXObject("Microsoft.XMLHTTP");
            }
        }

        function stateChanged()
        {
            updatepage(HttpRequest.responseText);
        }
        function processRequest(){
            if(HttpRequest.readyState==4 || HttpRequest.readyState=="complete"){
                stateChanged();
            }
        }
        function updatepage(str)
        {
            while(document.getElementById("divDisclosureStaus").hasChildNodes()){
            document.getElementById("divDisclosureStaus").removeChild(document.getElementById("divDisclosureStaus").firstChild);
            }

            var str1="";
            var str2="";
            if(str !=null){
            var listheader=str.split("#!#!#!#");
            str1=listheader[0];
            str2=listheader[1];
            }

            var newtext = document.createElement("label");
            newtext.innerHTML =str1;
            document.getElementById("divDisclosureStaus").appendChild(newtext);

            //var txt2 = str2;
            var newtext2 = document.createElement("label");
            newtext2.innerHTML = str2;
            document.getElementById("divDisclosureStaus").appendChild(newtext2);

        var  width =650;// document.getElementById("divDisclosureStaus").style.pixelWidth;
        var  height =250;// document.getElementById("divDisclosureStaus").style.pixelHeight;
        sm("divDisclosureStaus",width,height);
        document.getElementById("mbox").style.left="385";//460
        document.getElementById("mbox").style.top="200";//360
        }

        var url="<%=path%>/ShowDisclosureDetails.do?coiProjectId="+coipjtId;
        HttpRequest.open("POST",url,true);
        HttpRequest.onreadystatechange=processRequest;
        HttpRequest.send(null);
         };
     </script>
        <script>
         var index = 0;
         <%
         if(!projectType.equalsIgnoreCase("Annual") && !projectType.equalsIgnoreCase("Revision")){
             Vector pjtNameList = (Vector)request.getAttribute("ApprovedDisclDetView");
            if(pjtNameList != null && pjtNameList.size() > 0){
                for(int i=0; i < pjtNameList.size(); i++){
         %>
                var a = "imgtoggle"+index;
                document.getElementById(index).style.visibility = 'visible';
                document.getElementById(index).style.height = "auto";
                document.getElementById(a).src="<%=path%>/coeusliteimages/minus.gif";
                index++;
               <%    }
                }
            %>

            <%}%>
        </script>
    </body>
    </html:form>
</html:html>
