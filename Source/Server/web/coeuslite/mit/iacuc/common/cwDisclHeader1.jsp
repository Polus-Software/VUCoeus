<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="user" scope="session"
	class="edu.mit.coeus.bean.UserInfoBean" />
<jsp:useBean id="person" scope="session"
	class="edu.mit.coeus.bean.PersonInfoBean" />
<jsp:useBean id="loggedinUser" scope="session" class="java.lang.String" />

<html:html locale="true">
<head>
<title>Research @ MIT - IRB</title>
<html:base />

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<script language="javascript" type="text/JavaScript" src="_js.js"></script>
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

function getLockIds() {            
            var winleft = (screen.width - 650) / 2;
            var winUp = (screen.height - 450) / 2;  
            var win = "scrollbars=1,resizable=1,width=770,height=450,left="+winleft+",top="+winUp                        
            sList = window.open("<%=request.getContextPath()%>//getLockIdsList.do", "list", win);           
          }  

</script>

<!--<link href="/coeuslite/mit/utils/css/ipf.css" rel="stylesheet" type="text/css"> -->
</head>

<%-- <jsp:useBean id="headerItemsVector" scope="application" class="java.util.Vector" /> --%>
<%
    java.util.Vector headerItemsVector = (java.util.Vector)session.getAttribute("headerItemsVector");
    java.lang.StringBuffer imagePathsSB = new java.lang.StringBuffer();
    imagePathsSB.append("MM_preloaduncimages(");
%>

<%-- <logic:iterate name="headerItemsVector" scope = "application" id="coeusHeaderBean" >

<bean:define id="path" name="coeusHeaderBean" property="imagePath"/> 
<% 
    imagePathsSB.append(path);
    imagePathsSB.append(",");
%> 
</logic:iterate> --%>
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
				<!-- JM 5-31-2011 updated per 4.4.2 -->
				<table width="100%" cellpadding="0" cellspacing="0"
					STYLE="background-color: black; border: 0">
					<tr>
						<!-- JM 5-31-2011 updated per 4.4.2 -->
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
									<!-- JM 5-31-2011 updated per 4.4.2 -->
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
						<td width="99%">
							<table bgcolor="#E1E3E8" width="100%" height="28" border="0"
								cellpadding="0" cellspacing="0"
								STYLE="background-image:url('<bean:write name='ctxtPath'/>/coeusliteimages/nav_background.gif');border:0">
								<tr>
									<%
                                    for (int i =0; i<headerItemsVector.size();  i++) {
                                        edu.mit.coeuslite.utils.bean.CoeusHeaderBean coeusHeaderBean = (edu.mit.coeuslite.utils.bean.CoeusHeaderBean)headerItemsVector.get(i); 
                                        String headerId = coeusHeaderBean.getHeaderId();
                                        String path = coeusHeaderBean.getImagePath();
                                        String menuId = "image"+i;    
                                        String onLoadImage=coeusHeaderBean.getOnLoadImage();    
                                        String link = request.getContextPath()+coeusHeaderBean.getLink();
                                        %>
									<%--   <td width="162">
                                                <%--if(headerId.equals("001")) {%>
                                                    <img src="<bean:write name='ctxtPath'/><%=onLoadImage%>" name="<%=menuId%>" 
                                                        width="162" height="24" border="0" >
                                                <%} else {--%>
									<%if(headerId.equals("004")) {%>
									<td align="right" height="100%" valign="top"><html:link
											page='<%=link%>'>
											<font size="5px" title="Back to Coi Home">Back to
												COIHome&nbsp;</font>
										</html:link></td>
									<%}%>

									<%    
                                    }
                                    %>
									<%--      </td>
                                    <% 
                                        String applicationName = request.getContextPath();
                                        String logonLink = applicationName+"/cwLogon.jsp";

                                        //String logonLink = applicationName;
                                    
                                     <td width="100%"></td> --%>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>

	</table>
</body>
</html:html>