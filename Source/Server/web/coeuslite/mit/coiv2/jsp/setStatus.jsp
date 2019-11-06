<%--
    Document   : setStatus
    Created on : May 8, 2010, 3:29:44 PM
    Author     : Mr
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page
	import="java.util.Vector,edu.mit.coeuslite.coiv2.beans.Coiv2AttachmentBean,edu.mit.coeuslite.coiv2.utilities.CoiConstants,edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean,edu.mit.coeuslite.coiv2.services.CoiCommonService,java.util.Date;"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>C O I</title>
<%String path = request.getContextPath();
   String statusChanged=(String)request.getAttribute("changedStatus");
   String inputType = "";
   if(request.getParameter("param") != null) {
       inputType = request.getParameter("param").toString();
   }
   
   String selectedEntDesc = "No Conflict Exists";
   String selEntCode = "210";

   if(request.getAttribute("selectedEntDesc") != null && request.getAttribute("selectedEntDesc").toString().trim().length()!=0) {
       selectedEntDesc = (String)request.getAttribute("selectedEntDesc");
   }

   if(request.getAttribute("selEntCode") != null && request.getAttribute("selEntCode").toString().trim().length()!=0) {
       selEntCode = (String)request.getAttribute("selEntCode");
   }

   boolean isAssigned = false;
   if(request.getAttribute("assignedToReviewer") != null) {
     isAssigned = (Boolean)request.getAttribute("assignedToReviewer");
    }
 %>
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
<style type="text/css">
<!--
.buttonforstatus {
	border-right: gray 1px solid;
	border-top: #ffffff 1px solid;
	border-left: #ffffff 1px solid;
	border-bottom: dimgray 1px solid;
	background-color: #D6DFF7;
	width: 150px;
	font-size: 11px;
	font-weight: bold;
	font-family: Verdana, Arial, Helvetica, sans-serif;
}
-->
</style>

<script type="text/javascript">
  function setpageView() {
        var type = <%=inputType%>;
        <%if(inputType != null) {
            if(inputType.equals("pending")){
         %>
            pending();

         <%}else if(inputType.equals("approve")){%>
             approve();
         <%}else if(inputType.equals("disapprove")){%>
             disapprove();
          <%}%>
      <%}%>
    }
var dispSts = 0;
function update(){
debugger;
var dispSts = document.getElementById("dispositionStatus").value;
var disclSts = document.getElementById("disclosureStatus").value;
alert(dispSts+" "+disclSts);
if(dispSts != "" && disclSts != ""){
    document.forms[0].action = "<%=path%>/updateStatus.do?&discNoValue="+disclSts+"&dispoNoValue="+dispSts;
    document.forms[0].submit();
}else{
    if(dispSts == ""){
      alert("Please select Disposition Status");
    }
    if(disclSts == ""){
      alert("Please select Disclosure Status");
    }
}
}

function approve(){
debugger;
dispSts = 1;
//alert("dispSts");
document.getElementById("AppDiv").style.display = "block";
//document.getElementById("AppDiv").style.height = "auto";
document.getElementById("DisAppDiv").style.display = "none";
//document.getElementById("DisAppDiv").style.height = "1px";
document.getElementById("PendDiv").style.display = "none";
//document.getElementById("PendDiv").style.height = "1px";
document.getElementById("buttonDiv").style.display = "none";
//document.getElementById("buttonDiv").style.height = "1px";
}
function disapprove(){
debugger;
dispSts = 2;
//alert(dispSts);
document.getElementById("DisAppDiv").style.display = "block";
//document.getElementById("DisAppDiv").style.height = "auto";
document.getElementById("AppDiv").style.display = "none";
//document.getElementById("AppDiv").style.height = "1px";
document.getElementById("PendDiv").style.display = "none";
//document.getElementById("PendDiv").style.height = "1px";
document.getElementById("buttonDiv").style.display = "none";
//document.getElementById("buttonDiv").style.height = "1px";
}

function pending(){
debugger;
dispSts = 3;
//alert(dispSts);
document.getElementById("PendDiv").style.display = "block";
//document.getElementById("PendDiv").style.height = "auto";
document.getElementById("AppDiv").style.display = "none";
//document.getElementById("AppDiv").style.height = "1px";
document.getElementById("DisAppDiv").style.display = "none";
//document.getElementById("DisAppDiv").style.height = "1px";
document.getElementById("buttonDiv").style.display = "none";
//document.getElementById("buttonDiv").style.height = "1px";
}
function set(){
debugger;
//alert(dispSts); 
var disclSts = "null";
var disclosueStatus=null;
    
    var r=confirm("Do you want to complete this review?");
    if(r==true){
       document.forms[0].action = "<%=path%>/updateStatus.do?&discstatus=<%=selectedEntDesc%>&discNoValue=<%=selEntCode%>&dispoNoValue="+dispSts;
       document.forms[0].submit();
    }
    else{
       return;
    }

}

function setReviewStatus() {
    var reviewStatus = document.getElementById("disclosureStatus2").value;    
    var splitList = reviewStatus.split(":");
    var reviewStatusCode = splitList[0];
    var statusDesc = splitList[1];
    
     var r=confirm("Do you want to set the Review Status to "+statusDesc+"?");
    if(r==true){
       document.forms[0].action = "<%=path%>/updateStatus.do?&discstatus="+statusDesc+"&discNoValue=<%=selEntCode%>&dispoNoValue="+dispSts+"&reviewStatus="+reviewStatusCode;
       document.forms[0].submit();
    }
    else{
       return;
    }
}

</script>
</head>
<script src="js/jquery.js" type="text/javascript"></script>
<body>
	<%--<td valign="top">--%>
	<table id="setStatusBodyTable" class="table"
		style="width: 100%; height: auto;">
		<tr>
			<td><html:form action="/updateStatus.do">

					<logic:present name="message">
						<logic:equal value="true" name="message">
							<font color="red">You have successfully changed the
								disclosure status to <%= statusChanged%></font>
						</logic:equal>
						<logic:equal value="false" name="message">
							<font color="red">Disclosure Not Updated</font>
						</logic:equal>
					</logic:present>
					<br />

					<logic:present name="revmessage">
						<logic:equal value="true" name="revmessage">
							<font color="red">You have successfully changed the review
								status to <%= statusChanged%></font>
						</logic:equal>
						<logic:equal value="false" name="revmessage">
							<font color="red">Disclosure Not Updated</font>
						</logic:equal>
					</logic:present>
					<br />

					<div id="buttonDiv">
						<logic:present name="setStatusMessage">
							<logic:equal value="true" name="setStatusMessage">
&nbsp;&nbsp;<html:button styleClass="buttonforstatus"
									onclick="javaScript:pending();" style="width: 150px;"
									property="button" value="Set Disclosure Status"></html:button>
								<br />
								<br />
&nbsp;&nbsp;<html:button styleClass="clsavebutton"
									onclick="javaScript:approve();" property="button"
									style="width: 150px;" value="Approve"></html:button>
								<br />
								<br />
&nbsp;&nbsp;<html:button styleClass="clsavebutton"
									onclick="javaScript:disapprove();" property="button"
									style="width: 150px;" value="Disapprove"></html:button>
							</logic:equal>
						</logic:present>
					</div>

					<div id="PendDiv" style="display: none;">
						&nbsp;&nbsp; <b>Review Status:</b> <select id="disclosureStatus2"
							name="dispDisclStatusForm" class="textbox-long"
							style="width: 200px;">

							<option value="0">Select</option>
							<logic:present name="PendingDisclStatusList">
								<logic:iterate id="discView" name="PendingDisclStatusList">
									<option
										value="<bean:write name="discView" property="reviewStatusCode"/>:<bean:write name="discView" property="reviewStatus"/>"><bean:write
											name="discView" property="reviewStatus" /></option>
								</logic:iterate>
							</logic:present>
						</select> <br />
						<br /> &nbsp;&nbsp;
						<html:button styleClass="buttonforstatus"
							onclick="javaScript:setReviewStatus();" property="button"
							value="Set Review Status" style="width:150px;"></html:button>
					</div>
					<%-- code added for the new button Pending By Indhu -- end --%>

					<div id="AppDiv" style="display: none;">
						&nbsp;&nbsp;<b>Disclosure Status is set to '<%=selectedEntDesc%>'
							based on the Project-Financial Entity conflict status
						</b> <br />
						<br />
						<%--<html:button styleClass="clsavebutton" onclick="javaScript:update();" property="button" value="Set"></html:button>--%>
						&nbsp;&nbsp;
						<html:button styleClass="clsavebutton" onclick="javaScript:set();"
							property="button" value="Approve" style="width:150px;"></html:button>
					</div>

					<div id="DisAppDiv" style="display: none;">
						&nbsp;&nbsp;<b>Disclosure Status is set to '<%=selectedEntDesc%>'
							based on the Project-Financial Entity conflict status
						</b> <br />
						<br />
						<%--<html:button styleClass="clsavebutton" onclick="javaScript:update();" property="button" value="Set"></html:button>--%>
						&nbsp;&nbsp;
						<html:button styleClass="clsavebutton" onclick="javaScript:set();"
							property="button" value="Disapprove" style="width:150px;"></html:button>
					</div>
					<script>
    var type1 = '<%=inputType%>';
if(type1=="pending"){
   pending();
}else if(type1=="approve"){
approve();
}else{
disapprove();
}
</script>
				</html:form></td>
		</tr>
	</table>
</body>
</html>
