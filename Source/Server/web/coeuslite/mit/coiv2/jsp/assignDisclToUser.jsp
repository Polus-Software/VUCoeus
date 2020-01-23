<%--
    Document   : assignDisclView
    Created on : Apr 29, 2010, 2:54:48 PM
    Author     : Mr
--%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page
	import="java.util.Vector,edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean;"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>C O I</title>
<%String path = request.getContextPath();%>
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
</head>
<script src="js/jquery.js" type="text/javascript"></script>
<%String s = (String) request.getAttribute("disNum");
if(s==null ||s.equalsIgnoreCase("")){
s = "Select";
}
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
     ds = new DivSlider();
     ds.showDiv(index,1000);
     document.getElementById(index).style.visibility = 'visible';
     document.getElementById(index).style.height = "100px";
      document.getElementById(a).src="<%=path%>/coeusliteimages/minus.gif";
    }
}
function add(){
var a=document.getElementById("sequence").length;
if(document.getElementById("sequence").selectedIndex == -1){
alert("please select version");
}
if(document.getElementById("user").selectedIndex == -1){
alert("please select user");
}
var b=document.getElementById("user").length;
var seq = "<%=s%>";
    for (var i=0;i < a;i++)
    {
        if (document.forms[0].sequence.options[i].selected==true)
        {
            for (var j=0;j < b;j++)
            {
                if (document.forms[0].user.options[j].selected==true)
                {
                var oOption = document.createElement("OPTION");
                document.forms[0].assignedList.options.add(oOption);
                oOption.text = seq+":"+ document.forms[0].sequence.options[i].text +":"+  document.forms[0].user.options[j].text;
                oOption.value = seq+":"+ document.forms[0].sequence.options[i].value +":"+  document.forms[0].user.options[j].text;
                }
            }
        }
    }
}
function remove(){
var ind = 6;
for (var j=0;j < ind; j++)
{
if (document.forms[0].assignedList.options[j].selected==true)
{
var select=document.forms[0].assignedList;
select.remove(document.forms[0].assignedList.selectedIndex);
j--;
}}}
function save(combo){
if(document.forms[0].assignedList.length != 0){
for (var i=0;i<combo.options.length;i++)
{
combo.options[i].selected=true;
}
var sele = document.forms[0].discl.value;
document.forms[0].action = "<%=path%>/saveDisclToUser.do";
document.forms[0].submit();
}else{
alert("Select Disclosure and User");
}
}
function popsequnce(){
var sele = document.forms[0].discl.value;
var seleindx = document.forms[0].discl.selectedIndex;
if(seleindx!=0){
document.forms[0].action = "<%=path%>/popsequence.do?&disNo="+sele+"&seleindx="+seleindx;
document.forms[0].submit();
}
else{
alert("Please select one disclosure");
document.forms[0].action = "<%=path%>/assignDisclToUser.do";
document.forms[0].submit();
}
}
</script>
<html:form action="/assignDisclToUser.do">
	<body>

		<table id="bodyTable" class="table" style="width: 960px;" border="0"
			align="center">

			<%--<select name='numbers' multiple='multiple' id="discl" style="height: 100px;width: 150px;">
<logic:iterate id="pjtTitle" name="entityNameList">
<option><bean:write name="pjtTitle" property="coiDisclosureNumber"/></option>
</logic:iterate>
</select>--%>
			<logic:present name="message">
				<logic:equal value="true" name="message">
					<tr>
						<td colspan="2"><font color="red">Disclosure Assigned
								Successfully</font></td>
					</tr>
				</logic:equal>
			</logic:present>
			<tr>
				<td><b>Select Disclosure:</b><br /> <select name='numbers'
					id="discl" style="height: 20px; width: 200px;"
					onchange="javaScript:popsequnce();">
						<%--<logic:notPresent name="seqNameList">--%>
						<option>Select</option>
						<%--</logic:notPresent>--%>
						<logic:present name="entityNameList">
							<logic:iterate id="pjtTitle" name="entityNameList">

								<%--<logic:notEqual value="<%=s%>" name="disNum">--%>
								<option
									value="<bean:write name="pjtTitle" property="coiDisclosureNumber"/>:<bean:write name="pjtTitle" property="userName"/>"><bean:write
										name="pjtTitle" property="coiDisclosureNumber" />:
									<bean:write name="pjtTitle" property="userName" /></option>
								<%--</logic:notEqual>--%>
							</logic:iterate>
						</logic:present>
				</select></td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td><b>Select Version:</b><br /> <select name='numbers2'
					multiple='multiple' id="sequence"
					style="height: 100px; width: 200px;">
						<logic:present name="seqNameList">
							<%   int index = 0;
     Vector historyVector = (Vector) request.getAttribute("seqNameList");
%>
							<logic:iterate id="seqTitle" name="seqNameList">
								<%  CoiDisclosureBean bean = (CoiDisclosureBean) historyVector.get(index);
    String link = bean.getSequenceNumber().toString();

%>

								<option value="<%=link%>"><bean:write name="seqTitle"
										property="moduleName" />&nbsp;Based:&nbsp;V<%=link%></option>
								<%index++;%>
							</logic:iterate>
						</logic:present>
				</select></td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td width="30%"><html:select property="assignedList"
						name="assignDisclToUser" multiple='multiple'
						style="height: 100px;width: 400px;">
					</html:select></td>
				<td align="center" width="20%"><html:button
						styleClass="clsavebutton" onclick="javaScript:add();"
						property="button" value="<<"></html:button></td>
				<td width="50%"><b>Select User:</b><br /> <select
					name='numbers1' multiple='multiple' id="user"
					style="height: 100px; width: 200px;">
						<logic:present name="usersList">
							<logic:iterate id="usr" name="usersList">
								<option value="<bean:write name="usr" property="userName"/>"><bean:write
										name="usr" property="userName" /></option>
							</logic:iterate>
						</logic:present>
				</select></td>
			</tr>
			<tr>
				<td><html:button styleClass="clsavebutton"
						onclick="javaScript:save(assignedList);" property="button"
						value="Save"></html:button> &nbsp;<html:button
						styleClass="clsavebutton" onclick="javaScript:remove();"
						property="button" value="Remove"></html:button></td>
				<td>&nbsp;</td>
			</tr>
		</table>

		<%String seleindx = (String) request.getAttribute("seleindx");
if(seleindx==null ||seleindx.equalsIgnoreCase("")){
seleindx = "0";
}
%>
		<script>
var name = '<%=s%>';
if(name == null){
document.forms[0].discl.text = "Select";
}else{
document.forms[0].discl.selectedIndex = '<%=seleindx%>';
}
</script>
	</body>
</html:form>
</html>
