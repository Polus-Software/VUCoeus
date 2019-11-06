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
	import="java.util.Vector,java.util.Date,edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean,edu.mit.coeuslite.coiv2.beans.CoiUsersBean,edu.mit.coeuslite.coiv2.beans.Coiv2AssignDisclUserBean;"%>
<jsp:useBean id="loggedinUser" scope="session" class="java.lang.String" />
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
<%  String s = (String) request.getAttribute("disNum");
    String EMPTY_STRING = "";
    String link = EMPTY_STRING;
    if(s==null ||s.equalsIgnoreCase("")){
    s = "Select";
    }

    Boolean admin= (Boolean) session.getAttribute("isAdmin");
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
<%--var a=document.getElementById("sequence").length;
if(document.getElementById("sequence").selectedIndex == -1){
alert("please select version");
}--%>
if(document.getElementById("user").selectedIndex == -1){
alert("please select user");
}
var b=document.getElementById("user").length;
var seq = "<%=s%>";
<%--    for (var i=0;i < a;i++)
    {--%>
    <%--    if (document.forms[0].sequence.options[i].selected==true)
        {--%>
            for (var j=0;j < b;j++)
            {
                if (document.forms[0].user.options[j].selected==true)
                {
                var oOption = document.createElement("OPTION");
                document.forms[0].assignedList.options.add(oOption);
                oOption.text = <%--seq+":"+ document.forms[0].sequence.options[i].text +":"+  --%>document.forms[0].user.options[j].text;
                oOption.value = <%--seq+":"+ document.forms[0].sequence.options[i].value +":"+ --%>document.forms[0].user.options[j].text;
                }
<%--            }
        }--%>
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
//alert(document.forms[0].assignedList.length);
if(document.forms[0].assignedList.length != 0){
for (var i=0;i<combo.options.length;i++)
{
combo.options[i].selected=true;
}
<%--var sele = document.forms[0].discl.value;--%>
document.forms[0].action = "<%=path%>/saveDisclToUser.do";
document.forms[0].submit();
}else{
alert("Please select a user to proceed next..");
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
function searchWindow() {

    var winleft = (screen.width - 650) / 2;
    var winUp = (screen.height - 450) / 2;
    var win = "scrollbars=1,resizable=1,width=750,height=450,left="+winleft+",top="+winUp;
       sList = window.open('<%=request.getContextPath()%>/irbSearch.do?type=<bean:message
key="searchWindow.title.person"/>&search=true&searchName=PERSONSEARCH', "list", win);
        if (parseInt(navigator.appVersion) >= 4) {
        window.sList.focus();
        }
    }
function fetch_Data(result){
          var personId ="";
          var userName = "";
          var unitNum = "";
          var fullName = "";
          var homeUnit = "";
          if(result["PERSON_ID"] != 'null' && result["PERSON_ID"] != undefined){
          personId = result["PERSON_ID"];
          }
          if(result["USER_NAME"] != 'null' && result["USER_NAME"] != undefined){
          userName = result["USER_NAME"];
          }
          if(result["HOME_UNIT"] != 'null' && result["HOME_UNIT"] != undefined){
          unitNum = result["HOME_UNIT"];
          }
          if(result["FULL_NAME"] != 'null' && result["FULL_NAME"] != undefined){
          fullName = result["FULL_NAME"];
          }
          document.forms[0].action = "<%=path%>/saveDisclToUser.do?personId="+personId+"&UserName="+userName;
          document.forms[0].submit();

    }
    function removeLink(value,personId){
  if(confirm("Are you sure you want to remove User?")){
  document.forms[0].action = "<%=path%>/removeDisclToUser.do?userName="+value+"&personId="+personId;
  document.forms[0].submit();
   }
}
function compltReview(value,personId){
  document.forms[0].action = "<%=path%>/completeReviewAction.do?userName="+value+"&reviewerPersonId="+personId;
  document.forms[0].submit();
}
</script>
<html:form action="/assignDisclToUser.do">
	<body>

		<table id="bodyTable" class="table" style="width: 100%;" border="0"
			align="center">
			<%--<table id="attBodyTable" class="table" border="0" width="100%">--%>
			<tr style="background-color: #6E97CF; height: 22px;">
				<td
					style="color: #FFFFFF; font-size: 12px; font-weight: bold; padding: 2px 0 2px 3px;"
					colspan="3">
					<h1
						style="background-color: #6E97CF; color: #FFFFFF; float: left; font-size: 14px; font-weight: bold; margin: 0; padding: 2px 0 2px 0px; position: relative; text-align: left;">Disclosure
						Reviewer</h1>
				</td>
				<td align="right">
					<% link = "javaScript:searchWindow();";%> <html:link href="<%=link%>">
						<u> Add Reviewer</u>
					</html:link>

				</td>
			</tr>

			<%--<tr>
    <td colspan="4">
<b>Select Viewer :</b><br/><br/> </td></tr>
<tr>
    <td width="5%"> &nbsp;</td>
    <td width="30%">
<select name='numbers1' multiple='multiple' id="user" style="height: 250px;width:250px;">
<logic:present name="usersList">
<logic:iterate id="usr" name="usersList">
    <option value="<bean:write name="usr" property="userName"/>"><bean:write name="usr" property="userName"/>:<bean:write
name="usr" property="fullName"/></option>
   <option value="<bean:write name="usr" property="personId"/>"><bean:write name="usr" property="personId"/></option>

</logic:iterate>
</logic:present>
</select>
</td>
<td align="center" width="30%">
    <html:button styleClass="clsavebutton" onclick="javaScript:add();" property="button" value="ADD  >>"></html:button> <br/><br/>

   <html:button styleClass="clsavebutton" onclick="javaScript:remove();" property="button" value="Remove  <<"></html:button>
</td>
<td width="35%">
<logic:present name="message">
<logic:equal value="true" name="message">
<font color="red">Disclosure Assigned Successfully</font><br/>
</logic:equal>
</logic:present>

<html:select property="assignedList" name="assignDisclToUser" multiple='multiple' style="height: 250px;width: 250px;">
</html:select>
</td>
</tr>--%>
			<tr>
				<td colspan="4">&nbsp;</td>
			</tr>
			<%--<tr>
    <td colspan="4">
        &nbsp;
    </td>
</tr>
<tr>

    <td align="center" colspan="4">
<html:button styleClass="clsavebutton" onclick="javaScript:save(assignedList);" property="button" value="Save"></html:button>

</td>
</tr>--%>

			<%--new COI Assign reviewer starts--%>
			<table width='100%' cellpadding='3' cellspacing='0' class='tabtable'>
				<tr>
					<%--<td  class='theader' width='10%' nowrap>User Name</td>--%>
					<td class='theader' width="25%">Full Name</td>
					<td class='theader' width="10%">HomeUnit</td>
					<td class='theader' width="20%">Recommended Action</td>
					<td class='theader' nowrap width="25%">Review Complete
						Timestamp</td>
					<td class='theader'></td>
				</tr>

				<logic:present name="details">
					<% String strBgColor = "#DCE5F1";
                        int row=0;

                        %>
					<logic:iterate id="usr" name="details">
						<%if (row%2 == 0) {
                    strBgColor = "#D6DCE5";
                }
               else {
                strBgColor="#DCE5F1";
             } %>
						<%
                     Vector details = (Vector) request.getAttribute("details");
                    edu.mit.coeuslite.coiv2.beans.Coiv2AssignDisclUserBean userBean = (edu.mit.coeuslite.coiv2.beans.Coiv2AssignDisclUserBean)details.get(row);
                    String usrName = userBean.getFullName();
                    String usrId = userBean.getUserId();
                    String prsnId = userBean.getPersonId();
                    Date updatedTime=(Date)userBean.getUpdateTime() ;
                    String description=(String)userBean.getRecommendedAction();
                    String reviewCmplt=(String) userBean.getReviewCompleted();
                    String updateUser = userBean.getUpdateUserName();
                %>

						<%--if(!(user.get("acType").equals("D"))){--%>
						<tr bgcolor="<%=strBgColor%>"
							onmouseover="className='TableItemOn'"
							onmouseout="className='TableItemOff'">
							<%--<td class='copy'><bean:write name="usr" property="userName"/></td>--%>
							<td class='copy' nowrap><bean:write name="usr"
									property="fullName" /></td>
							<td class='copy'><logic:notEmpty name="usr"
									property="unitNumber">
									<bean:write name="usr" property="unitNumber" />
								</logic:notEmpty></td>
							<%
                String remove = "javaScript:removeLink('"+usrId+"','"+prsnId+"');";
                String cmpltlink = "javaScript:compltReview('"+usrName+"','"+prsnId+"');";
                 if(usrName.equalsIgnoreCase(updateUser)){
                 if(reviewCmplt!=null && reviewCmplt.equals("Y")){ %>
							<td class='copy'><%=description%></td>
							<td class='copy' nowrap><%=updatedTime%><b> by </b><%=updateUser%></td>
							<% }%>
							<%--<td class='copy' ><a href="<%=cmpltlink%>">Complete Review</a></td>
                   <td class='copy'/>--%>
							<% //}
                 } else {
                if(admin!=null && admin){
                 if(reviewCmplt!=null && reviewCmplt.equals("Y")){
                            %>
							<td class='copy'><a href="<%=cmpltlink%>"><%=description%></a></td>
							<td class='copy' nowrap><%=updatedTime%><b> by </b><%=updateUser%></td>
							<% }}}
                  if(admin!=null && admin){
                    if(reviewCmplt == null){
                           %>
							<td class='copy'><a href="<%=cmpltlink%>">Complete
									Review</a></td>
							<td class='copy' />
							<% }}else{ %>
							<td class='copy' colspan="2" />
							<% } %>

							<td class='copy' align=center><html:link href="<%=remove%>">
									<bean:message bundle="proposal" key="proposalRoles.Remove" />
								</html:link></td>
						</tr>
						<%//}
             row++;               %>
					</logic:iterate>
				</logic:present>



			</table>
			<%--COI Assign reviewer ends--%>
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
