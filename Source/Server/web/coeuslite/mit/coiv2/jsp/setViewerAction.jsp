<%--
    Document   : setViewerAction
    Created on : May 8, 2010, 3:29:44 PM
    Author     : Roshin
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
<%
String path = request.getContextPath();
String statusChanged=(String)request.getAttribute("changedViewStatus");
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
<script type="text/javascript">
var reviewSts = 0;
function approve(){
reviewSts = 1;
document.forms[0].action = "<%=path%>/updateViewerStatus.do?&reviewSts="+reviewSts;
document.forms[0].submit();
//alert(reviewSts);
}
function disapprove(){
reviewSts = 2;
document.forms[0].action = "<%=path%>/updateViewerStatus.do?&reviewSts="+reviewSts;
document.forms[0].submit();
//alert(reviewSts);
}
function review(){
reviewSts = 3;
document.forms[0].action = "<%=path%>/updateViewerStatus.do?&reviewSts="+reviewSts;
document.forms[0].submit();
//alert(reviewSts); 
}
function set(){
debugger;
document.forms[0].action = "<%=path%>/updateViewerStatus.do?&reviewSts="+reviewSts;
document.forms[0].submit();
}
</script>
</head>
<script src="js/jquery.js" type="text/javascript"></script>
<body>
	<%--<td valign="top">--%>
	<table id="setStatusBodyTable" class="table" style="width: 100%;"
		border="0">
		<tr>
			<td><html:form action="/updateStatus.do">
					<logic:present name="status">
						<logic:equal value="true" name="status">
							<font color="red">Status Updated to <%= statusChanged%>
							</font>
						</logic:equal>
						<logic:equal value="false" name="status">
							<font color="red">Status Not Updated</font>
						</logic:equal>
					</logic:present>
					<br />
					<%--<logic:present name="status1">
    <logic:equal value="true" name="status1">
        <font color="red">Mail send successfully</font>
    </logic:equal>
    <logic:equal value="false" name="status1">
        <font color="red">Mail send failed</font>
    </logic:equal>
</logic:present><br/>--%>
					<logic:notPresent name="status">
						<div id="buttonDiv">
							&nbsp; &nbsp;
							<html:button styleClass="clsavebutton"
								onclick="javaScript:approve();" property="button"
								value="Recomend for Approval" style="width:200px"></html:button>
							<br>
							<br> &nbsp; &nbsp;
							<html:button styleClass="clsavebutton"
								onclick="javaScript:disapprove();" property="button"
								value="Recomend for Disapproval" style="width:200px"></html:button>
							<br>
							<br> &nbsp; &nbsp;
							<html:button styleClass="clsavebutton"
								onclick="javaScript:review();" property="button"
								value="Review by COI committee" style="width:200px"></html:button>
						</div>
					</logic:notPresent>
				</html:form></td>
		</tr>
	</table>


</body>
</html>
