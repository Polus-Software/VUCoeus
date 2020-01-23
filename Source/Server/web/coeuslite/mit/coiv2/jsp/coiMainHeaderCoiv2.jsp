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
	href="<%=request.getContextPath() %>/coeuslite/mit/coiv2/css/layout.css"
	rel="stylesheet" type="text/css" />
<link
	href="<%=request.getContextPath() %>/coeuslite/mit/coiv2/css/styles.css"
	rel="stylesheet" type="text/css" />
<link
	href="<%=request.getContextPath() %>/coeuslite/mit/coiv2/css/collapsemenu.css"
	rel="stylesheet" type="text/css" />
<script language="JavaScript" type="text/JavaScript">
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


function open_protocol_search(link)
{

    var winleft = (screen.width - 650) / 2;
    var winUp = (screen.height - 450) / 2;
    var win = "scrollbars=1,resizable=1,width=900,height=450,left="+winleft+",top="+winUp
    //var win = "toolbar=0,scrollbars=1,statusbar=0,menubar=0,resizable=1,width=650,height=400,left="+winleft+",top="+winUp
    sList = window.open('<%=request.getContextPath()%>'+"/"+link, "list", win);

}

function getLockIds() {
            var winleft = (screen.width - 650) / 2;
            var winUp = (screen.height - 450) / 2;
            var win = "scrollbars=1,resizable=1,width=770,height=450,left="+winleft+",top="+winUp
            sList = window.open("<%=request.getContextPath()%>//getLockIdsList.do", "list", win);
    }

function showDialog(divId){
    width = document.getElementById(divId).style.pixelWidth;
    height = document.getElementById(divId).style.pixelHeight;
    sm(divId,width,height);
}
</script>
</head>
<jsp:useBean id="headerItemsVector" scope="session"
	class="java.util.Vector" />
<%
        java.lang.StringBuffer imagePathsSB = new java.lang.StringBuffer();
        imagePathsSB.append("MM_preloaduncimages(");
    %>

<%
    for (int i = 0;i<headerItemsVector.size();i++) {
        edu.mit.coeuslite.utils.bean.CoeusHeaderBean coeusHeaderBean = (edu.mit.coeuslite.utils.bean.CoeusHeaderBean)headerItemsVector.get(i);
        String path = coeusHeaderBean.getOnLoadImage();

        imagePathsSB.append(path);
        imagePathsSB.append(",");
        }
    %>

<%
    String imagePaths = imagePathsSB.substring(0,imagePathsSB.length()-1);
    imagePaths = imagePaths +")";
    %>
<body>

	<a name="top"></a>

	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td>
				<!-- JM 03-26-2012 changed background to VU black -->
				<table width="100%" border="0" cellpadding="0" cellspacing="0"
					STYLE="background-color: black; border: 0">
					<tr>
						<!-- JM 03-26-2012 changed gif height to 68 -->
						<td><img
							src="<bean:write name='ctxtPath'/>/coeusliteimages/header_coeus2.gif"
							width="675" height="68"></td>
						<td align="right" valign="top"><a href="javascript:;"
							onClick="MM_openBrWindow('web/clFeedback.jsp?module=IPF','','toolbar=no,width=500,height=325')"></td>
						<td cellpadding="2" align="right" class="copysmall">&nbsp;&nbsp;&nbsp;<span
							class="copysmallwhite"> <bean:message key="Header.user" />
								<%--User--%>: &nbsp;<%=loggedinUser%></td>
					</tr>
					<tr>
						<td height='20' colspan='3' align='right'><span
							class="copysmallwhite"> <html:link
									href="javaScript:getLockIds();">
									<!-- JM 03-26-2012 change current lock link color to white --->
									<font size='2' color='#FFFFFF'><u><bean:message
												key="locking.currentLockslabel" /></u></font>
								</html:link> &nbsp;&nbsp;&nbsp;
						</span></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td width="100%">
							<!-- JM 11-1-2012 changed height from 22 to 24 and update background color
                        <table bgcolor="#BACFE4" width="100%" height="24"  border="0" cellpadding="0" cellspacing="1" STYLE="background-image:url('<bean:write name='ctxtPath'/>/coeusliteimages/nav_background.gif');border:0">-->
							<table width="100%" height="24" border="0" cellpadding="0"
								cellspacing="0" class="mainmenubg"
								style="background-color: #fedc92;">
								<tr>
									<%
                                for (int i =0; i<headerItemsVector.size();  i++) {
                                    edu.mit.coeuslite.utils.bean.CoeusHeaderBean coeusHeaderBean = (edu.mit.coeuslite.utils.bean.CoeusHeaderBean)headerItemsVector.get(i);
                                    String path = coeusHeaderBean.getImagePath();
                                    String menuId = "image"+i;//coeusHeaderBean.getHeaderId();
                                    String onLoadImage=coeusHeaderBean.getOnLoadImage();
                                    String link = request.getContextPath()+coeusHeaderBean.getLink();
                                    String uri = request.getRequestURI();
                                    if (coeusHeaderBean.getLink().indexOf("coi") > -1) {
                            %>
									<!-- JM 11-1-2012 updated width from 98 to 120 -->
									<td><html:link page='<%=link%>'>
											<img src="<bean:write name='ctxtPath'/><%=onLoadImage%>"
												name="<%=menuId%>" width="120" height="24" border="0"
												onMouseOut="MM_swapImgRestore()"
												onMouseOver="MM_swapImage('<%=menuId%>','','<bean:write name='ctxtPath'/><%=path%>',1)">
										</html:link></td>
									<%
                                } else {
                            %>
									<!-- JM 11-1-2012 updated width from 98 to 120 -->
									<td><html:link page='<%=link%>'>
											<img src="<bean:write name='ctxtPath'/><%=path%>"
												name="<%=menuId%>" width="120" height="24" border="0"
												onMouseOut="MM_swapImgRestore()"
												onMouseOver="MM_swapImage('<%=menuId%>','','<bean:write name='ctxtPath'/><%=onLoadImage%>',1)">
										</html:link></td>
									<%
                                }
                            }
                                    String applicationName = request.getContextPath();
                                    //String logonLink = applicationName+"/logout.do";
                                    String logonLink = applicationName;
                                 %>
								</tr>
							</table>
						</td>
					</tr>






				</table>
			</td>
		</tr>
	</table>


	<%--<div id="reporterdetails" style="width: 980px">
	 		<h1>Financial Disclosure by <bean:write name="person" property="firstName"/></h1>
	 		<div class="Rdetails">
                            <div class="ColLeft" style="font-size: 12">Reporter Name :</div>
                            <div class="ColRight" style="font-size: 12"><bean:write name="person" property="firstName"/> </div>
			</div>
                        <div class="Rdetails">
					<div class="ColLeft" style="font-size: 12">Department : </div>
					<div class="ColRight" style="font-size: 12"><bean:write name="person" property="dirDept"/></div>
			</div>

			<div class="Rdetails">
					<div class="ColLeft" style="font-size: 12">Reporter Email :</div>
					<div class="ColRight" style="font-size: 12"><bean:write name="person" property="email"/></div>
			</div>

			<div class="Rdetails">
                                        <div class="ColLeft" style="font-size: 12">Office Location :</div>
                                        <div class="ColRight" style="font-size: 12"><bean:write name="person" property="offLocation"/> </div>
			</div>

                        <div class="Rdetails">
                                        <div class="ColLeft" style="font-size: 12">Office Phone :</div>
                                        <div class="ColRight" style="font-size: 12"><bean:write name="person" property="offPhone"/> </div>
			</div>

			<div class="Rdetails">
                                        <div class="ColLeft" style="font-size: 12">Secondary Office:</div>
                                        <div class="ColRight" style="font-size: 12"><bean:write name="person" property="secOffLoc"/></div>
			</div>


	 </div>

--%>


</body>
</html:html>
