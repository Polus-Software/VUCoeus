<%--
    Document   : showHistory
    Created on : Apr 23, 2010, 4:03:13 PM
    Author     : Mr
--%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@page
	import="java.util.Vector,java.text.SimpleDateFormat,java.text.Format,java.util.Date,edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean,edu.mit.coeuslite.coiv2.services.CoiCommonService;"%>
<html:html locale="true">
<%
String path = request.getContextPath();%>
<head>
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

<script src="js/jquery.js" type="text/javascript"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$("#menu > li > a[@class=collapsed] ").find("+ ul").slideToggle("medium");
		$("#menu  > li > a").click(function() {
			$(this).toggleClass("expanded").toggleClass("collapsed").find("+ ul").slideToggle("medium");
		});
	});
function viewAttachment(entityNum,coiDisclosureNumber,coiSequenceNumber)
            {
               // window.location("request.getContextPath()/jsp/downloadPage.jsp");
                window.open("<%=request.getContextPath()%>/viewAttachment.do?&entityNum="+entityNum+"&disclosureNumber="+coiDisclosureNumber+"&SeqNumber="+coiSequenceNumber);
            }

function openDiv(divid,height){
        document.getElementById(divid).style.visibility = 'visible';
        document.getElementById(divid).style.height = height;
}

function changeColor(id, color){
    <%--alert("color of " + id + "is : "+" "+color);--%>
    element = document.getElementById(id);
    event.cancelBubble = true;
    oldcolor = element.currentStyle.backgroundColor;
    element.style.background = color;
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
    }
}
  </script>
</head>


<logic:notPresent name="historyDetView">
	<table id="bodyNoTable" class="table" style="width: 100%;" border="0">
		<tr style="background-color: #6E97CF; height: 22px; width: 100%;"
			valign="top">
			<td colspan="7"
				style="background-color: #6E97CF; color: #FFFFFF; font-size: 14px; font-weight: bold; margin: 0; padding: 2px 0 2px 10px; position: relative; text-align: left;"><strong>Disclosure
					History</strong></td>
		</tr>
		<tr>
			<td><p style="color: #ff0000; font-size: 12px;">No
					Disclosure History Available</p></td>
		</tr>
	</table>
</logic:notPresent>



<logic:present name="historyHeaderList">


	<%
            String strBgColor = "#DCE5F1";
            Vector historyVector = (Vector) request.getAttribute("historyDetView");
            String disclno = (String) request.getAttribute("disclno");
            Integer seqno = (Integer) request.getAttribute("seqno");
            CoiDisclosureBean coiDisclosure  = (CoiDisclosureBean)request.getAttribute("bean");
           // String diclstatus = coiDisclosure.getDisclosureStatus();
           // String owner = coiDisclosure.getUserName();
            CoiCommonService coiCommonService = CoiCommonService.getInstance();
           // String updateHeader = coiCommonService.getFormatedDate(coiDisclosure.getUpdateTimestamp());
           // String expireHeader = coiCommonService.getFormatedDate(coiDisclosure.getExpirationDate());
         %>
	<table id="bodyTable" class="table" style="width: 100%;" border="0">
		<tr style="background-color: #6E97CF; height: 22px; width: 100%;">
			<td colspan="7"
				style="background-color: #6E97CF; color: #FFFFFF; font-size: 14px; font-weight: bold; margin: 0; padding: 2px 0 2px 10px; position: relative; text-align: left;"><strong>Disclosure
					History</strong></td>
		</tr>
		<tr>
			<td
				style="background-color: #6E97CF; color: #FFFFFF; font-size: 12px; font-weight: bold;">&nbsp;</td>
			<td
				style="background-color: #6E97CF; color: #FFFFFF; font-size: 12px; font-weight: bold;"
				align="left">Event</td>

			<td
				style="background-color: #6E97CF; color: #FFFFFF; font-size: 12px; font-weight: bold;"
				align="left">Project #</td>
			<td
				style="background-color: #6E97CF; color: #FFFFFF; font-size: 12px; font-weight: bold;"
				align="left">Title</td>
			<td
				style="background-color: #6E97CF; color: #FFFFFF; font-size: 12px; font-weight: bold;"
				align="left">Disposition Status</td>
			<td
				style="background-color: #6E97CF; color: #FFFFFF; font-size: 12px; font-weight: bold;">Last
				Updated</td>
			<td
				style="background-color: #6E97CF; color: #FFFFFF; font-size: 12px; font-weight: bold;">&nbsp;</td>
		</tr>

		<%
            Vector historyheadList = (Vector)request.getAttribute("historyHeaderList");
            int index = 0;
            int code = 0;
            String moduleName = "";
        %>
		<logic:iterate id="historyTitle" name="historyHeaderList">
			<%
                                if (index%2 == 0) {
                                strBgColor = "#D6DCE5";
                                } else {
                                strBgColor="#DCE5F1";
                                }
     %>
			<%
             CoiDisclosureBean beanHeader = (CoiDisclosureBean) historyheadList.get(index);
             String eventType=beanHeader.getDescription();
             String disclosureNumber = beanHeader.getCoiDisclosureNumber();
             int seqNum = beanHeader.getSequenceNumber();
             String moduleItemKey = beanHeader.getModuleItemKey();

            coiCommonService = CoiCommonService.getInstance();
              String update = coiCommonService.getFormatedDate(beanHeader.getUpdateTimestamp());
            // String expireHeader = coiCommonService.getFormatedDate(beanHeader.getExpirationDate());
            if(!eventType.equals("Travel")){
%>
			<tr bgcolor="<%=strBgColor%>" id="row<%=index%>" class="rowLine"
				onmouseover="className='rowHover rowLine'"
				onmouseout="className='rowLine'" style="height: 22px;">
				<td>&nbsp;</td>
				<%
        String link = "/getHistoryView.do?&param1="+disclosureNumber+"&param2="+seqNum+"&param3="+eventType+"&param4="+moduleItemKey;
     %>
				<%-- <td>
     <% String link1 = "/getannualdisclosure.do?&param1=" + disclno +"&param2=" + beanHeader.getSequenceNumber()+"&param3=" + beanHeader.getPersonId()+"&param5=" + beanHeader.getModuleName()+"&param6="+"throughHistory";%>
<html:link style="color: #003399;font-family:Arial,Helvetica,sans-serif;font-size:12px;font-weight:bold;float: left;font-size:12px;" action="<%=link1%>">View</html:link>
</td>--%>
				<td align="left"><bean:write name="historyTitle"
						property="description" /></td>
				<td align="left"><bean:write name="historyTitle"
						property="moduleItemKey" /></td>

				<td align="left"><bean:write name="historyTitle"
						property="pjctName" /></td>
				<td align="left"><bean:write name="historyTitle"
						property="dispositionStatus" /></td>
				<td><bean:write name="historyTitle" property="updateTimestamp" />
				</td>
				<td><html:link action="<%=link%>">View</html:link></td>
			</tr>

			<%
  }
  index++;
 %>
		</logic:iterate>
		<%request.getSession().setAttribute("lastIndex",index);%>
		</logic:present>
	</table>

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
</html:html>
