<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.Vector,edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>C O I</title>
<%String path = request.getContextPath();%>
<script src="<%=path%>/coeuslite/dartmouth/utils/scripts/divSlide.js"></script>
<script src="<%=path%>/coeuslite/dartmouth/utils/scripts/Balloon.js"></script>
<link href="<%=path%>/coeuslite/dartmouth/utils/css/coeus_styles.css" rel="stylesheet" type="text/css"/>
<link href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/layout.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/styles.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/collapsemenu.css" rel="stylesheet" type="text/css" />
</head>
<script src="js/jquery.js" type="text/javascript"></script>
<script type="text/javascript">


    function dothis(){
    alert("this");

    }

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
</script>
<body>
<table id="bodyTable" class="table" style="width: 960px;" border="0" align="center">
<tr style="background-color:#6E97CF;height: 22px;width: 960px;">
    <td colspan="7" style="background-color:#6E97CF;color:#FFFFFF;font-size:14px;font-weight:bold;margin: 0;padding: 2px 0 2px 10px;position: relative;text-align: left;"><strong>List Of Disclosures</strong></td>
</tr>
<tr>
<td style="background-color:#6E97CF;color:#173B63;font-size:12px;font-weight:bold;">&nbsp;</td>
<td style="background-color:#6E97CF;color:#173B63;font-size:12px;font-weight:bold;">&nbsp;</td>
<td style="background-color:#6E97CF;color:#173B63;font-size:12px;font-weight:bold;"><strong>Disclosure Number</strong></td>
<td style="background-color:#6E97CF;color:#173B63;font-size:12px;font-weight:bold;"><strong>Owned By</strong></td>
<td style="background-color:#6E97CF;color:#173B63;font-size:12px;font-weight:bold;">Disclosure Status</td>
<td style="background-color:#6E97CF;color:#173B63;font-size:12px;font-weight:bold;"><strong>Update Timestamp</strong></td>
<td style="background-color:#6E97CF;color:#173B63;font-size:12px;font-weight:bold;"><strong>Expiration Date</strong></td>
</tr>
<logic:present name="entityNameList">
       <%
            String strBgColor = "#DCE5F1";
            Vector projectNameList = (Vector)request.getAttribute("entityNameList");
            int index = 0;
        %>
<logic:iterate id="pjtTitle" name="entityNameList">
     <%
                                if (index%2 == 0) {
                                strBgColor = "#D6DCE5";
                                } else {
                                strBgColor="#DCE5F1";
                                }
     %>

   <tr bgcolor="<%=strBgColor%>" id="row<%=index%>" class="rowLine" onmouseover="className='rowHover rowLine'" onmouseout="className='rowLine'" height="22px">
       <td width="5%">
   <img src='<%=request.getContextPath()%>/coeusliteimages/plus.gif' border='none' id="imgtoggle<%=index%>" name="imgtoggle<%=index%>" border="none" onclick="javascript:selectProj(<%=index%>);"/>
   <img src='<%=request.getContextPath()%>/coeusliteimages/minus.gif' style="display: none;" border='none' id="imgtoggleminus<%=index%>" name="imgtoggleminus<%=index%>" border="none" onclick="javascript:selectProj(<%=index%>);"/>
   </td>

   <td>


       <html:link style="color: #003399;font-family:Arial,Helvetica,sans-serif;font-size:12px;font-weight:bold;float: left;font-size:12px;" action="" onclick="dothis()">Print</html:link>
  </td>
   <td>
       <b><bean:write name="pjtTitle" property="coiDisclosureNumber"/></b>
   </td>
   <td>
 <b><bean:write name="pjtTitle" property="userName"/></b>
   </td>
  <td>
 <b><bean:write name="pjtTitle" property="disclosureStatus"/></b>
   </td>
   <td>
 <b><bean:write name="pjtTitle" property="updateTimestamp"/></b>
   </td>
   <td>
 <b><bean:write name="pjtTitle" property="expirationDate"/></b>
   </td>
</tr>
<tr>
<td colspan="7">
<div id="<%=index%>" style="height: 1px;width: 955px;visibility: hidden;background-color: #9DBFE9;overflow-x: hidden; overflow-y: scroll;">
<table id="bodyTable" class="table" style="width: 960px;height: 100%;" border="0" >
<tr>
<td style="background-color:#6E97CF;color:#FFFFFF;font-size:12px;font-weight:bold;">&nbsp;</td>
<td style="background-color:#6E97CF;color:#FFFFFF;font-size:12px;font-weight:bold;">Disclosure Event</td>
<td style="background-color:#6E97CF;color:#FFFFFF;font-size:12px;font-weight:bold;"><strong>Disclosure Status</strong></td>
<td style="background-color:#6E97CF;color:#FFFFFF;font-size:12px;font-weight:bold;">Disposition Status</td>
<td style="background-color:#6E97CF;color:#FFFFFF;font-size:12px;font-weight:bold;"><strong>Last Updated</strong></td>
<td style="background-color:#6E97CF;color:#FFFFFF;font-size:12px;font-weight:bold;"><strong>Update User</strong></td>
<td style="background-color:#6E97CF;color:#FFFFFF;font-size:12px;font-weight:bold;"><strong>Expiration Date</strong></td>
</tr>
<logic:present name="message">
<logic:equal value="false" name="message">
<tr>
<td colspan="4">
<font color="red">No disclosures found</font>
</td>
</tr>
</logic:equal>
</logic:present>
<%         int i = 0;
           Vector disclVector = (Vector) request.getAttribute("pjtEntDetView");
%>
    <logic:iterate id="pjtEntView" name="pjtEntDetView">
            <% CoiDisclosureBean projectName = (CoiDisclosureBean)projectNameList.get(index);
            String name = projectName.getCoiDisclosureNumber();
             CoiDisclosureBean bean = (CoiDisclosureBean) disclVector.get(i);
%>
        <logic:equal value="<%=name%>" property="coiDisclosureNumber" name="pjtEntView">
<tr class="rowLineLight" onmouseover="className='rowHover rowLine'" onmouseout="className='rowLineLight'" height="22px">
<td>
<%String link1 = "/printprocess.do?&param1=" + bean.getCoiDisclosureNumber() +"&param2=" + bean.getSequenceNumber()+"&param3="+ bean.getPersonId()+"&param5="+bean.getModuleName();%>
<html:link style="color: #003399;font-family:Arial,Helvetica,sans-serif;font-size:12px;font-weight:bold;float: left;font-size:12px;" action="<%=link1%>">Print</html:link>
&nbsp;&nbsp;
</td>
<td>
   <b><bean:write name="pjtEntView" property="moduleName"/></b>
</td>
<td>
   <b><bean:write name="pjtEntView" property="disclosureStatus"/></b>
</td>
<td>
<b><bean:write name="pjtEntView" property="dispositionStatus"/></b>
</td>
<td>
<b><bean:write name="pjtEntView" property="updateTimestamp"/></b>
</td>
    <td>
        <b><bean:write name="pjtEntView" property="updateUser"/></b>
    </td>
    <td>
        <b><bean:write name="pjtEntView" property="expirationDate"/></b>
    </td>
</tr>
    </logic:equal>
         <%i++;%>
    </logic:iterate>
</table>
</div></td>
    </tr>
 <%index++;%>
</logic:iterate>
<%request.getSession().setAttribute("lastIndex",index);%>
</logic:present>
</table>
</body>
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
</html>
