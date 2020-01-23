<%--
    Document   : financialEntities
    Created on : May 8, 2010, 3:25:09 PM
    Author     : Mr
--%>
<%@page import="org.mozilla.javascript.Script"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="/WEB-INF/lib-tlds/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/lib-tlds/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/lib-tlds/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/lib-tlds/struts-logic.tld" prefix="logic"%>
<%@ page
	import="java.util.Vector,edu.mit.coeuslite.coiv2.utilities.CoiConstants,edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean,
         edu.mit.coeuslite.utils.bean.SubHeaderBean,edu.mit.coeuslite.coiv2.beans.CoiAnnualProjectEntityDetailsBean,
         edu.mit.coeuslite.coiv2.services.CoiCommonService,edu.mit.coeuslite.coiv2.beans.CoiProposalBean,java.util.TreeSet,java.util.ArrayList;"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>C O I</title>
<style>
#mbox {
	background-color: #6e97cf;
	padding: 0px 8px 8px 8px;
	border: 3px solid #095796;
}

#mbm {
	font-family: sans-serif;
	font-weight: bold;
	float: right;
	padding-bottom: 5px;
}

#ol {
	background-image:
		url('../coeuslite/mit/utils/scripts/modal/overlay.png');
}

.dialog {
	display: none
}

* html #ol {
	background-image: none;
	filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src="../coeuslite/mit/utils/scripts/modal/overlay.png",
		sizingMethod="scale");
}
</style>

<script
	src="<%=request.getContextPath()%>/coeuslite/mit/utils/scripts/modal/modaldbox.js"></script>
<script type="text/javascript">
             function closebtn(){
            hm('divDisclosureStaus');
        }

    function showDialog(txtAreaID,id)
 {
     var width  = 650;//document.getElementById('divTXTDetails').style.pixelWidth;
     var height = 170;//document.getElementById('divTXTDetails').style.pixelHeight;

     sm(id,width,height);

     var txtValue=document.getElementById(txtAreaID).value;
    // alert(txtValue);
     document.getElementById('TxtAreaComments').innerHTML=txtValue;
     document.getElementById("mbox").style.left="385";//450
     document.getElementById("mbox").style.top="250";//250
     //document.getElementById("mbox").style.position="fixed";
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

<style>
#mbox {
	background-color: #6e97cf;
	padding: 0px 8px 8px 8px;
	border: 3px solid #095796;
}

#mbm {
	font-family: sans-serif;
	font-weight: bold;
	float: right;
	padding-bottom: 5px;
}

#ol {
	background-image:
		url('../coeuslite/mit/utils/scripts/modal/overlay.png');
}

.dialog {
	display: none
}

* html #ol {
	background-image: none;
	filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src="../coeuslite/mit/utils/scripts/modal/overlay.png",
		sizingMethod="scale");
}
</style>
<style>
.deleteRow {
	font-weight: bold;
	color: #CC0000;
	background-color: white;
}

.addRow {
	font-weight: bold;
	background-color: white;
}

.rowHeight {
	height: 25px;
}
</style>
<script
	src="<%=request.getContextPath()%>/coeuslite/mit/utils/scripts/modal/modaldbox.js"></script>
<style>
.cltextbox-medium {
	width: 160px;
}

.cltextbox-color {
	width: 160px;
	font-weight: normal;
}

.textbox {
	width: 160px;
	font-weight: normal;
}

.cltextbox-nonEditcolor {
	width: 160px;
	font-weight: normal;
}
</style>
<%
String path = request.getContextPath();%>
<script src="<%=path%>/coeuslite/dartmouth/utils/scripts/divSlide.js"></script>
<script src="<%=path%>/coeuslite/dartmouth/utils/scripts/Balloon.js"></script>
<link href="<%=path%>/coeuslite/dartmouth/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css" />
<link
	href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/layout.css"
	rel="stylesheet" type="text/css" />
<link
	href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/styles.css"
	rel="stylesheet" type="text/css" />
<link
	href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/collapsemenu.css"
	rel="stylesheet" type="text/css" />

<%
    Vector val = (Vector) request.getAttribute("entityNameList");
     boolean isAdmin = false;
    if(session.getAttribute("isAdmin") != null) {
    isAdmin = (Boolean) session.getAttribute("isAdmin");
    }

     String projectId = "";
     Vector finEntDetails = (Vector)request.getAttribute("pjtEntDetView");

     String apprvdView = "";
     if(session.getAttribute("checkPrint") != null) {
         apprvdView = (String)session.getAttribute("checkPrint");
      }

       boolean isHistoryView = false;

     if(session.getAttribute("historyView") != null) {
         isHistoryView = (Boolean)session.getAttribute("historyView");
      }

       boolean continuePresent=false;
%>
<script type="text/javascript">
function selectProj(index){
    var a = "imgtoggle"+index;
//    alert(a);
//    alert("=====index===="+index+"  "+lastinx);
    if(document.getElementById(index).style.visibility == 'visible'){
      document.getElementById(index).style.visibility = 'hidden';
      document.getElementById(index).style.height = "1px";
      document.getElementById(a).src="<%=path%>/coeusliteimages/plus.gif";
    }else{
     last();
      //alert("inside else");
     ds = new DivSlider();
     ds.showDiv(index,1000);
     document.getElementById(index).style.visibility = 'visible';
     document.getElementById(index).style.height = "100px";
     document.getElementById(a).src="<%=path%>/coeusliteimages/minus.gif";
    }
}

        function changeStatusCode(index)
        {
            debugger;

            var propertyName = "dispositionStatus";
            var entityStatus = "entityStatus";
            var entityStatusName = "";
            var disclName = "";
            var k=0;

             <% if(finEntDetails != null) {
             for (int i = 0; i < finEntDetails.size(); i++) {%>

                k++;
            <%}}%>
            entityStatusName = entityStatus+index;

            for(var i=0; i< k; i++){
                disclName = propertyName+index+i;

                var value=document.getElementById(entityStatusName).value;

                if(document.getElementById(disclName) != null) {
                    document.getElementById(disclName).value=value;
                }

            }
        }

          function updateProjectDetails()
        {
             var k=0;
             var noOfPjts = 0

             var entityValue = "";
             var finEntDetails = [];

             var project = "dispositionStatus";
             var projectStatus = "";
             var discdetail = "disDetailNo";
             var discDetailNum = "";

            <% if(val != null) {
                for (int i = 0; i < val.size(); i++) {%>
                    k++;
            <%}}%>

             <%
             if(finEntDetails != null) {
                 for (int l = 0; l < finEntDetails.size(); l++) {%>
                    noOfPjts++;
            <%}}%>

            for(var i=0; i< k; i++){

                for(var m=0; m < noOfPjts; m++) {
                    projectStatus = project+i+m;
                    discDetailNum = discdetail+i+m;
                    if(document.getElementById(discDetailNum) != null && document.getElementById(projectStatus) != null){
                    entityValue =  document.getElementById(discDetailNum).value + ":" + document.getElementById(projectStatus).value;
                   // alert("value  :   " + entityValue);
                    finEntDetails.push(entityValue);
                }
                }
            }
             
            document.getElementById("entityDetails").value=finEntDetails;     
            document.forms[0].action= '<%=path%>' + "/updateFinancialEntityByProjects.do?fromReview=<%= request.getParameter("fromReview")%>";
            document.forms[0].submit();
        }

   function showComment(DescriptionDetails)
 {
     var width  =650;// document.getElementById('divTXTDetails').style.pixelWidth;
     var height = 320;//document.getElementById('divTXTDetails').style.pixelHeight;
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


     document.getElementById("mbox").style.left="385";//450
     document.getElementById("mbox").style.top="250";//250
 }
  function saveAndcontinue()
            {
                document.forms[0].action= '<%=path%>'+"/getProjectsByFinancialEntitiesView.do";
                document.forms[0].submit();
            }

    function sortNumber(a,b)
        {
        return b - a;
        }

  function changePjtStatus(startIndex,numOfPjts) {
        var propertyName = "entityStatus"+startIndex;
         var pjtName = "";
         var entityList = new Array();
         var dispositionStatus = "dispositionStatus";

         for(var i=0; i<numOfPjts; i++){
              pjtName = dispositionStatus+startIndex+i;
               var value;

              if(document.getElementById(pjtName) != null) {
                value = document.getElementById(pjtName).value;
                entityList[i]= value;
              }
          }
          entityList.sort(sortNumber);
          var worstStatus = entityList[0];
          document.getElementById(propertyName).value=worstStatus;
   }

               function show(entNo){
                //link="/reviewAnnFinEntityCoiv2.do";
                //String entityNumber= pjtBean1.getEntityNumber();
                var width1 = (screen.width - 650) / 2;
                var height1 = (screen.height - 450) / 2;
                var win = "scrollbars=1,resizable=1,width=800,height=450,left="+width1+",top="+height1;
                //var win = "toolbar=0,scrollbars=1,statusbar=0,menubar=0,resizable=1,width=650,height=400,left="+winleft+",top="+winUp
                //sm('div1',width1,height1);
                sList= window.open('<%=request.getContextPath()%>'+"/reviewAnnFinEntViewCoiv2.do?fromByFinEnt=true&entityNumber="+entNo,"");
            }

</script>
</head>

<body>
	<html:form action="/updateFinancialEntityByProjects.do">
		<table id="bodyTable" class="table" style="width: 100%; height: 100%;"
			border="0">
			<tr style="background-color: #6E97CF; height: 22px;">
				<td width="2%">&nbsp;</td>
				<td width="25%" align="left"
					style="background-color: #6E97CF; color: #FFFFFF; float: none; font-size: 14px; font-weight: bold; margin: 0;"><strong>&nbsp;Entity
						Names</strong></td>
				<td width="35%" align="left"
					style="background-color: #6E97CF; color: #FFFFFF; float: none; font-size: 14px; font-weight: bold; margin: 0;"><strong>Entity
						Business Focus</strong></td>
				<td width="25%" align="left" nowrap
					style="background-color: #6E97CF; color: #FFFFFF; float: none; font-size: 14px; font-weight: bold; margin: 0;"><strong>Entity
						Status</strong></td>
				<td align="right"><a
					href="<%= request.getContextPath()%>/getProjectsByFinancialEntitiesView.do">View
						By Projects</a></td>
			</tr>
			<%--<logic:present name="message">
<logic:equal value="false" name="message">
<tr>
<td colspan="4">
<font color="red">No financial entities found</font>
</td>
</tr>
</logic:equal>
</logic:present>--%>

			<logic:empty name="entityNameList">
				<tr>
					<td colspan="4"><font color="red">No financial entities
							found</font></td>
				</tr>
				<tr>
					<td colspan="4"><html:button
							onclick="javaScript:saveAndcontinue();" property="Save"
							styleClass="clsavebutton" style="width:150px;">
             Continue
         </html:button> <%continuePresent = true;%></td>
				</tr>
			</logic:empty>

			<logic:notEmpty name="entityNameList">
				<%
            String projectType = (String)request.getSession().getAttribute("param5");
            String strBgColor = "#DCE5F1";
            Vector projectNameList = (Vector)request.getAttribute("entityNameList");
             Vector pjtEntDetailsList = (Vector)request.getAttribute("pjtEntDetView");
            int index = 0;
            String entPjtId = "";
            TreeSet entStatusId = new TreeSet();
            String entStatusDescription = "";
            String entStatusCode = "";
             %>
				<logic:iterate id="pjtTitle" name="entityNameList">
					<logic:present name="pjtTitle">
						<%
                                   if (index%2 == 0) {
                                strBgColor = "#D6DCE5";
                                } else {
                                strBgColor="#DCE5F1";
                                }
            CoiAnnualProjectEntityDetailsBean pjtBean1 = (CoiAnnualProjectEntityDetailsBean) projectNameList.get(index);
            entPjtId = pjtBean1.getCoiProjectId();
            entStatusId = new TreeSet();
            //String Details="";
            String entityNumber=pjtBean1.getEntityNumber();
            String entityName = pjtBean1.getEntityName();
            if(pjtEntDetailsList != null) {
                  String PjtId = "";
                for(int i=0; i < pjtEntDetailsList.size(); i++) {
                    CoiAnnualProjectEntityDetailsBean entBean = (CoiAnnualProjectEntityDetailsBean) pjtEntDetailsList.get(i);
                    PjtId = (String)entBean.getCoiProjectId();
                    String entityName_l = entBean.getEntityName();
                    if(entityName_l != null && entityName_l.trim().equals(entityName.trim())) {
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

       %>

						<tr bgcolor="<%=strBgColor%>" id="row<%=index%>" class="rowLine"
							onmouseover="className='rowHover rowLine'"
							onmouseout="className='rowLine'" height="22px">
							<td colspan="1" width="2%"><img
								src='<%=request.getContextPath()%>/coeusliteimages/plus.gif'
								border='none' id="imgtoggle<%=index%>"
								name="imgtoggle<%=index%>" border="none"
								onclick="javascript:selectProj(<%=index%>);" /> <img
								src='<%=request.getContextPath()%>/coeusliteimages/minus.gif'
								style="visibility: hidden;" border='none'
								id="imgtoggleminus<%=index%>" name="imgtoggleminus<%=index%>"
								border="none" onclick="javascript:selectProj(<%=index%>);" /></td>
							<td valign="top" align="left" width="25%"><b>&nbsp;&nbsp;<a
									href="javascript:show('<%=entityNumber%>')"
									style="text-decoration: underline"><bean:write
											name="pjtTitle" property="entityName" /></a>
							</b></td>
							<%String commentObj=pjtBean1.getRelationshipDescription();
   commentObj = commentObj.replaceAll("\"", " ");
        String comments="";
        if(commentObj==null){comments="";}
        else{comments=(String)commentObj;
            if(comments.trim().length()>64)
                {comments=(String)comments.substring(0,64);
                 comments+="<a href=\"javaScript:showDialog('txtAreaCommentId"+index+"','divTXTDetails1')\">[...]</a>";

                 }}%>
							<td valign="top" align="left" width="35%"><%=comments%> <%--<bean:write name="pjtTitle" property="relationshipDescription"/></td>--%>

								<input id="txtAreaCommentId<%=index%>" name="finEntDet"
								type="hidden" value="<%=commentObj%>" /> <!--            coeus -3424 ends-->

								<%   if(isAdmin && !apprvdView.equalsIgnoreCase("approvedDisclosureview") && !isHistoryView) {%>
							<td width="25%" style="text-align: left"><select
								id="entityStatus<%=index%>" name="dispDisclStatusForm"
								onchange="changeStatusCode(<%=index%>);"
								style="text-align: left; width: 100%">
									<%--<option value="<bean:write name="pjtTitle" property="entityStatusCode"/>"><bean:write name="pjtTitle" property="entityStatus"/></option>--%>
									<option value="<%=entStatusCode%>"><%=entStatusDescription%></option>
									<logic:present name="typeList">
										<logic:iterate id="statusList" name="typeList">
											<option
												value="<bean:write name="statusList" property="code"/>"><bean:write
													name="statusList" property="description" /></option>
										</logic:iterate>
									</logic:present>
							</select></td>
							<%} else {%>
							<td width="20%"><b><bean:write name="pjtTitle"
										property="entityStatus" /></b></td>
							<%}%>
							<td align="right"></td>
							<!--   <td width="5%"><a href="javascript:show('<%=entityNumber%>')">View</a></td>-->
						</tr>
						<tr>
							<td colspan="5">
								<div id="<%=index%>"
									style="height: 1px; width: 100%; visibility: hidden; background-color: #9DBFE9; overflow-x: hidden; overflow-y: scroll;">
									<table id="bodyTable" class="table"
										style="width: 100%; height: 100%;" border="0">
										<tr
											style="height: 22px; background-color: #538dd5; width: 100%;">
											<td width="15%"
												style="background-color: #538dd5; color: #FFFFFF; float: none; font-size: 12px; font-weight: bold; margin: 0px; padding: 2px 0 2px 10px; text-align: left;"><strong>&nbsp;Discl.Event</strong></td>
											<td width="13%"
												style="background-color: #538dd5; color: #FFFFFF; float: none; font-size: 12px; font-weight: bold; margin: 0px; padding: 0px 0 2px 0px; text-align: left;"><strong>&nbsp;Project
													#</strong></td>
											<td width="15%"
												style="background-color: #538dd5; color: #FFFFFF; float: none; font-size: 12px; font-weight: bold; margin: 0; padding: 2px 0 2px 10px; text-align: left;"><strong>Project
													Name</strong></td>
											<td width="25%"
												style="background-color: #538dd5; color: #FFFFFF; float: none; font-size: 12px; font-weight: bold; margin: 0; padding: 2px 0 2px 10px; text-align: left;"><strong>Relationship
													Description</strong></td>
											<td width="20%"
												style="background-color: #538dd5; color: #FFFFFF; float: none; font-size: 12px; font-weight: bold; margin: 0; padding: 2px 0 2px 10px; text-align: left;"><strong>Discl.Event
													Status</strong></td>
											<td colspan="3" width="10%" align="left"
												style="background-color: #538dd5; color: #FFFFFF; float: none; font-size: 14px; font-weight: bold; margin: 0;"><strong>&nbsp;</strong></td>
										</tr>
										<%
        Vector entityPjtList = (Vector)request.getAttribute("pjtEntDetView");
        int index1 = 0;
        int numOfPjts =0;
    %>
										<logic:present name="pjtEntDetView">
											<logic:iterate id="pjtEntView" name="pjtEntDetView">
												<logic:equal value="<%=entityName%>" property="entityName"
													name="pjtEntView">
													<%numOfPjts++;%>
												</logic:equal>
											</logic:iterate>
										</logic:present>


										<logic:present name="pjtEntDetView">

											<%int entCount = 0;%>
											<logic:iterate id="pjtEntView" name="pjtEntDetView">

												<%CoiAnnualProjectEntityDetailsBean pjtBean = (CoiAnnualProjectEntityDetailsBean) entityPjtList.get(index1);
            String projectName = pjtBean.getEntityName();
            String linkFeed1;
             linkFeed1=pjtBean.getCoiProjectId()+"-"+pjtBean.getCoiDisclosureNumber();
                        String  link = "/ShowDisclosureDetails.do?coiProjectId="+linkFeed1;
            String eventName = pjtBean.getCoiProjectSponsor();
            if(projectName==null)
                {
                projectName=pjtBean.getCoiProjectTitle();
                }
            Vector projectNameList1 = (Vector)request.getAttribute("pjtEntView");
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
             if(projectName != null) {
%>

												<logic:equal value="<%=projectName%>" property="entityName"
													name="pjtTitle">
													<%
                    entCount++;
                    String DescriptionDetails = "";
                    DescriptionDetails += pjtBean.getEntityName()+"##";
                    DescriptionDetails += pjtBean.getRelationshipDescription()+"##";
                    DescriptionDetails += pjtBean.getOrgRelationDescription()+"##";
                    DescriptionDetails += pjtBean.getCoiProjectTitle()+"##" ;
                    DescriptionDetails += pjtBean.getCoiProjectId()+"##" ;

           %>
													<tr class="rowLineLight"
														onmouseover="className='rowHover rowLine'"
														onmouseout="className='rowLineLight'" style="height: 22px">

														<td style="padding: 2px 0 2px 10px; text-align: left;"
															nowrap>&nbsp;<%=projectType%></td>
														<td style="text-align: left;"><bean:write
																name="pjtEntView" property="coiProjectId" /></td>
														<td style="padding: 2px 0 2px 10px; text-align: left;"
															nowrap>
															<%if(projectType.equalsIgnoreCase("Travel")) {%> <%=eventName%>
															<%}else{
            String relatn=pjtBean.getCoiProjectTitle();
            String comment="";
        if(relatn==null){comment="";}
        else{comment=(String)relatn;
            if(comment.trim().length()>50)
                {comment=(String)comment.substring(0,50);
                 comment+="<a href=\"javaScript:showTitle('txtAreaComment"+index1+"','divTXT')\">[...]</a>";


            }}%><%=comment%> <input id="txtAreaComment<%=index1%>"
															name="finEntDet" type="hidden"
															value="<%=pjtBean.getCoiProjectTitle()%>" /> <% } %>
														</td>
														<%--<bean:write name="pjtEntView" property="coiProjectTitle"/>--%>


														<%String commentObj1=pjtBean.getOrgRelationDescription();
        commentObj1 = commentObj1.replaceAll("\"", " ");
        String comments1="";
        if(commentObj1==null){comments1="";}
        else{comments1=(String)commentObj1;
            if(comments1.trim().length()>60)
                {comments1=(String)comments1.substring(0,62);
                 comments1+="<a href=\"javaScript:showDialog('txtAreaCommentId1"+index1+"','divTXTDetails2')\">[...]</a>";

                 }}%>
														<td style="padding: 2px 0 2px 10px; text-align: left;">
															<div
																style="word-wrap: break-word; width: 254px; float: left; overflow: hidden"><%=comments1%></div>
															<input id="txtAreaCommentId1<%=index1%>" name="finEntDet"
															type="hidden" value="<%=commentObj1%>" /> <%--<bean:write name="pjtEntView" property="orgRelationDescription"/></td>--%>
															<%
       if(isAdmin && !apprvdView.equalsIgnoreCase("approvedDisclosureview") && !isHistoryView) {%>
														
														<td style="padding: 2px 0 2px 10px; text-align: left;">
															<select id="dispositionStatus<%=index%><%=index1%>"
															name="dispDisclStatusForm" style="width: 100%"
															onchange="changePjtStatus('<%=index%>','<%=numOfPjts%>')">
																<option
																	value="<bean:write name="pjtEntView" property="entityStatusCode"/>"><bean:write
																		name="pjtEntView" property="entityStatus" /></option>
																<logic:present name="typeList">
																	<logic:iterate id="statusList" name="typeList">
																		<option
																			value="<bean:write name="statusList" property="code"/>"><bean:write
																				name="statusList" property="description" /></option>
																	</logic:iterate>
																</logic:present>

														</select> <input type="hidden"
															id="disDetailNo<%=index%><%=index1%>"
															value="<bean:write name="pjtEntView" property="coiDiscDetailsNumber"/>" />
														</td>
														<%} else {%>
														<td style="padding: 2px 0 2px 10px; text-align: left;"><bean:write
																name="pjtEntView" property="entityStatus" /></td>
														<%}%>
														<td>&nbsp;&nbsp;&nbsp;<a
															href="javascript:populateHistoryList('<%=linkFeed1%>')">History</a></td>
														<%--<td width="5%">&nbsp;&nbsp;&nbsp;<a href="javascript:showComment('<%=DescriptionDetails%>')">View</a></td>--%>
													</tr>
												</logic:equal>
												<% } index1++;%>
											</logic:iterate>
											<%if(entCount == 0) {%>

											<tr>
												<td colspan="3"><font color="red">No financial
														entities found</font></td>
											</tr>
											<%}%>
										</logic:present>
									</table>
								</div>
							</td>
						</tr>
						<%index++;%>
					</logic:present>
				</logic:iterate>
				<input type="hidden" id="entityDetails" name="entityDetails"
					value="test" />
				<%request.getSession().setAttribute("lastIndex",index);%>
				<%
  if(isAdmin) {%>
				<logic:notPresent name="historyView">
					<tr>
						<%--<td width="1%"></td>--%>
						<td colspan="3">
							<%if(!apprvdView.equalsIgnoreCase("approvedDisclosureview") && !isHistoryView){%>
							<html:button styleClass="clsavebutton" value="Save"
								property="save" onclick="updateProjectDetails();"
								style="width:150px;" /> <%}%> <html:button
								onclick="javaScript:saveAndcontinue();" property="Save"
								styleClass="clsavebutton" style="width:150px;">
             Continue
         </html:button> <% continuePresent=true;%>
						</td>
					</tr>
				</logic:notPresent>
				<logic:present name="historyView">
					<%if(!continuePresent) {%>
					<tr>
						<td colspan="4"><html:button
								onclick="javaScript:saveAndcontinue();" property="Save"
								styleClass="clsavebutton" style="width:150px;">
             Continue
         </html:button></td>
					</tr>
					<%}%>
				</logic:present>
				<%}%>
			</logic:notEmpty>
			<%
    if(!isAdmin){
 %>
			<%if(!continuePresent) {%>
			<tr>
				<td class='savebutton' align="left" colspan="3"><html:button
						onclick="javaScript:saveAndcontinue();" property="Save"
						styleClass="clsavebutton" style="width:150px;">
             Continue
         </html:button></td>
			</tr>
			<%}}%>
			<script>
    function last(){
      var lastinx = "<%=request.getSession().getAttribute("lastIndex")%>";
   //   alert(lastinx);
      for(var i=0;i<lastinx;i=i+1){
  //    alert(i);
      var b = "imgtoggle"+i;
    if(document.getElementById(i).style.visibility == 'visible'){
      document.getElementById(i).style.visibility = 'hidden';
      document.getElementById(i).style.height = "1px";
      document.getElementById(b).src="<%=path%>/coeusliteimages/plus.gif";
}}}
</script>
		</table>
		<logic:notEmpty name="entityNameList">

			<div id="divDisclosureStaus" class="dialog"
				style="width: 450px; overflow: hidden; position: absolute;">
				<table class="table" style="width: 100%;" id="historyDet">
					<tr>
						<td style=""><label id="HeaderDetails">hi</label></td>
					</tr>
					<tr>
						<td><label id="HistoryList">hi2</label></td>
					</tr>
					<tr>
						<td align="center"><input type="button" value="Close"
							class="clsavebutton" onclick="hm('divDisclosureStaus')" /></td>
					</tr>
				</table>
			</div>

			<div id="divTXTDetails" class="dialog"
				style="width: 450px; height: 320px; overflow: auto; position: absolute;">
				<table width="100%" border="0" class="table">
					<tr style="background-color: #6E97CF; font-size: 12px; margin: 0;">
						<td colspan="2" style="padding: 2px 0px 5px 0px"><font
							color="#FFFFFF" size="2px"><b>Relationship Comments</b></font></td>
					</tr>
					<tr style="float: none; font-size: 12px; margin: 0;">
						<td style="height: 17px; width: 80px;"><b>Entity Name: </b></td>
						<td><b><label id="entName" /></b></td>
					</tr>
					<tr style="float: none; font-size: 12px; margin: 0; height: 17px;">
						<td><b>Project Title: </b></td>
						<td><b><label id="pjtTitle" /></b></td>
					</tr>
					<tr style="float: none; font-size: 12px; margin: 0;">
						<td><b>Project #: </b></td>
						<td><b><label id="pjtId" /></b></td>
					</tr>
					<tr style="float: none; font-size: 12px; margin: 0;">
						<td style="height: 15px;" colspan="2"><img width="100%"
							height="1" border="0"
							src="<%=request.getContextPath()%>/coeusliteimages/line4.gif" />
						</td>
					</tr>
					<tr style="background-color: #6E97CF; float: none; margin: 0;">
						<td style="height: 25px;" colspan="2"><b>Relationship
								Description:</b></td>
					</tr>
					<tr style="height: 70px; width: 450px;">
						<td colspan="2" align="left">
							<div
								style="height: 70px; width: 650px; overflow-x: scroll; overflow-y: scroll; overflow: auto;">
								<label id="reldesc"></label>
							</div>
						</td>
					</tr>

					<tr style="background-color: #6E97CF; float: none; margin: 0;">
						<td style="height: 25px;" colspan="2"><b>Relationship to
								Organization:</b></td>
					</tr>

					<tr style="height: 70px; width: 450px;">
						<td colspan="2" align="left">
							<div
								style="height: 70px; width: 650px; overflow-x: scroll; overflow-y: scroll; overflow: auto;">
								<label id="relOrgn"> </label>
							</div>
						</td>
					</tr>

					<tr style="background-color: #6E97CF;">
						<td style="height: 30px;" align="center" colspan="2"><input
							type="button" value="Close" Class="clsavebutton"
							onclick="hm('divDisclosureStaus')" /></td>

					</tr>
				</table>
			</div>

			<div id="divTXTDetails1" class="dialog"
				style="width: auto; overflow: hidden; position: absolute;">
				<table width="100%" border="0" cellpadding="1" cellspacing="1"
					class="table">
					<tr style="background-color: #6E97CF; font-size: 12px; margin: 0;">
						<td style="padding: 2px 0px 5px 0px"><font color="#FFFFFF"
							size="2px"><b>Entity Business Focus</b></font></td>
					</tr>
					<tr style="height: 100px; width: 200px;">
						<%-- <td align="center"> <textarea id="TxtAreaComments" cols="3" style=" height: 70px;resize:none; width: 430px;" disabled ></textarea></td>--%>
						<td align="left" style="height: 100px;">
							<div
								style="height: 100px; width: 650px; overflow-x: scroll; overflow-y: scroll; overflow: auto;">
								<label id="TxtAreaComments"></label>
							</div>
						</td>
					</tr>
					<tr style="background-color: #6E97CF;">
						<td align="center"><input type="button" value="Close"
							class="clsavebutton" onclick="hm('divTXTDetails1');" /></td>

					</tr>
				</table>
			</div>
			<div id="divTXTDetails2" class="dialog"
				style="width: auto; overflow: hidden; position: absolute;">
				<table width="100%" border="0" cellpadding="1" cellspacing="1"
					class="table">
					<tr style="background-color: #6E97CF; font-size: 12px; margin: 0;">
						<td style="padding: 2px 0px 5px 0px"><font color="#FFFFFF"
							size="2px"><b>Relationship Description</b></font></td>
					</tr>
					<tr style="height: 100px; width: 200px;">
						<%-- <td align="center"> <textarea id="TxtAreaComments" cols="3" style=" height: 70px;resize:none; width: 430px;" disabled ></textarea></td>--%>
						<td align="left" style="height: 100px;">
							<div
								style="height: 100px; width: 650px; overflow-x: scroll; overflow-y: scroll; overflow: auto;">
								<label id="TxtAreaComments"></label>
							</div>
						</td>
					</tr>
					<tr style="background-color: #6E97CF;">
						<td align="center"><input type="button" value="Close"
							class="clsavebutton" onclick="hm('divTXTDetails2');" /></td>

					</tr>
				</table>
			</div>
			<div id="divTXT" class="dialog"
				style="width: auto; overflow: hidden; position: absolute;">
				<table width="100%" border="0" cellpadding="1" cellspacing="1"
					class="table">
					<tr style="background-color: #6E97CF; font-size: 12px; margin: 0;">
						<td style="padding: 2px 0px 5px 0px"><font color="#FFFFFF"
							size="2px"><b>Project Name</b></font></td>
					</tr>
					<tr style="height: 100px; width: 200px;">

						<td align="left" style="height: 100px;">
							<div
								style="height: 100px; width: 650px; overflow-x: scroll; overflow-y: scroll; overflow: auto;">
								<label id="TxtArea"></label>
							</div>
						</td>
					</tr>
					<tr style="background-color: #6E97CF;">
						<td align="center"><input type="button" value="Close"
							class="clsavebutton" onclick="hm('divTXT');" /></td>

					</tr>
				</table>
			</div>

		</logic:notEmpty>

	</html:form>
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
</body>
</html>
