<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/privilege.tld" prefix="priv"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="user" scope="session"
	class="edu.mit.coeus.bean.UserInfoBean" />
<jsp:useBean id="person" scope="session"
	class="edu.mit.coeus.bean.PersonInfoBean" />
<jsp:useBean id="userprivilege" class="java.lang.String" scope="session" />
<jsp:useBean id="loggedinUser" scope="session" class="java.lang.String" />

<%@page import="edu.mit.coeuslite.utils.bean.SubHeaderBean"%>


<html:html locale="true">
<head>
<title>CoeusWeb</title>
<html:base />

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<script language="javascript" type="text/JavaScript" src="_js.js"></script>
<script
	src="<%=request.getContextPath()%>/coeuslite/mit/utils/scripts/modal/modaldbox.js"></script>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/coeuslite/mit/utils/scripts/modal/modaldbox.css"
	type="text/css" />
<link
	href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/layout.css"
	rel="stylesheet" type="text/css" />
<link
	href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/styles.css"
	rel="stylesheet" type="text/css" />
<link
	href="<%=request.getContextPath()%>/coeuslite/mit/coiv2/css/collapsemenu.css"
	rel="stylesheet" type="text/css" />
<script language="JavaScript" type="text/JavaScript">

                function exitToCoi(actionPath){
                    var answer = confirm("Are you sure you want to Exit to MYCOI.. ?");
                         if(answer) {
                             document.location = '<%=request.getContextPath()%>'+ actionPath;
                             window.location;

                         }
                 }

            <!--
            function MM_swapImgRestore() { //v3.0
                var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
            }

            function MM_preloadImages() { //v3.0
                var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
                    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
                        if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
                }

                function MM_findObj(n, d) { //v4.01
                    var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
                        d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
                    if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
                    for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
                    if(!x && d.getElementById) x=d.getElementById(n); return x;
                }

                function MM_swapImage() { //v3.0
                    var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
                        if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
                }
                //-->


                function open_disclosure_search(link)
                {

                    var winleft = (screen.width - 650) / 2;
                    var winUp = (screen.height - 450) / 2;
                    var win = "scrollbars=1,resizable=1,width=900,height=450,left="+winleft+",top="+winUp
                    //var win = "toolbar=0,scrollbars=1,statusbar=0,menubar=0,resizable=1,width=650,height=400,left="+winleft+",top="+wi        nUp
                    sList = window.open('<%=request.getContextPath()%>'+"/"+link, "list", win);

                }

                function getLockIds() {
                    var winleft = (screen.width - 650) / 2;
                    var winUp = (screen.height - 450) / 2;
                    var win = "scrollbars=1,resizable=1,width=770,height=450,left="+winleft+",top="+winUp
                    sList = window.open("<%=request.getContextPath()%>/getLockIdsList.do", "list", win);
                }

                function showDialog(divId){
                    width = document.getElementById(divId).style.pixelWidth;
                    height = document.getElementById(divId).style.pixelHeight;
                    sm(divId,width,height);
                }
        </script>
</head>
<jsp:useBean id="subheaderVectorCoiv2" scope="session"
	class="java.util.Vector" />

<body>
	<a name="top"></a>
	<table class="tablemain" width="100%" border="0" cellpadding="0"
		cellspacing="0">
		<tr
			style="background-image: url(/coeus44server/coeusliteimages/tile_menu.gif); border: 0pt none;">


			<td class="copy"
				style="background-image: url(/coeus44server/coeusliteimages/body_background.gif); border: 0pt none; height: 22px; text-align: right;">
				<%
                                for (int i = 0; i < subheaderVectorCoiv2.size(); i++) {
                                    SubHeaderBean bean = (SubHeaderBean) subheaderVectorCoiv2.get(i);
                                    String actionPath = bean.getSubHeaderLink();
                                    String subHeaderId = bean.getSubHeaderId();

                                    if(subHeaderId.equals("SH0015")) { %>
				<html:link href="<%=actionPath%>" target="_blank"><%=bean.getSubHeaderName()%></html:link>
				&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp; <%  }

                                    else if (bean.getProtocolSearchLink().equals("false") && !subHeaderId.equals("SH0012")) {%>

				<html:link action="<%=actionPath%>"><%=bean.getSubHeaderName()%></html:link>
				&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp; <%}else if(bean.getSubHeaderName().toString().equals("Exit To MyCOI")) {%>
				<a href="javaScript:exitToCoi('<%=actionPath%>')" class="menu"><%=bean.getSubHeaderName()%></a>
				<%}else if(subHeaderId.equals("SH0012")) {
                         boolean isAssignedDisclPresent = false;
                         if(session.getAttribute("isAssignedDisclPresent") != null) {
                             isAssignedDisclPresent = (Boolean)session.getAttribute("isAssignedDisclPresent");
                         }
                         if(isAssignedDisclPresent) {
                         %> <html:link action="<%=actionPath%>"><%=bean.getSubHeaderName()%></html:link>
				&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp; <%} }else {%> <a
				href="javaScript:open_disclosure_search('<%=actionPath%>');"
				class="menu"><%=bean.getSubHeaderName()%></a>

				&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp; <%}
                                }%>

			</td>
		</tr>
	</table>
</body>
</html:html>
