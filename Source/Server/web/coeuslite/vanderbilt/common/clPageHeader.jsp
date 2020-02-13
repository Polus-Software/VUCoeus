<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<jsp:useBean id="user" scope="session" class="edu.mit.coeus.bean.UserInfoBean" />
<jsp:useBean id="person" scope="session" class="edu.mit.coeus.bean.PersonInfoBean" />
<jsp:useBean id= "loggedinUser" scope="session" class="java.lang.String"/>

<html:html locale="true">
<head>
<title>CoeusLite</title>
<html:base/>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<script language="javascript" type="text/JavaScript" src="_js.js"></script>
<script language="JavaScript" type="text/JavaScript"></script>
<script language="JavaScript" type="text/JavaScript">

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

function callMenu(value){
    CLICKED_LINK = value;
}

function getLockIds() {
	CLICKED_LINK = '';
    DATA_CHANGED = 'false';
    var winleft = (screen.width - 650) / 2;
    var winUp = (screen.height - 450) / 2;  
    var win = "scrollbars=1,resizable=1,width=770,height=450,left="+winleft+",top="+winUp                        
    sList = window.open("<%=request.getContextPath()%>//getLockIdsList.do", "list", win);
}  

function open_proposal_search(link) {
	DATA_CHANGED = 'false';
    var winleft = (screen.width - 650) / 2;
    var winUp = (screen.height - 450) / 2;  
    var win = "scrollbars=1,resizable=1,width=750,height=450,left="+winleft+",top="+winUp
    sList = window.open('<%=request.getContextPath()%>'+"/"+link, "list", win);
}

function open_protocol_search(link) {
    var winleft = (screen.width - 650) / 2;
    var winUp = (screen.height - 450) / 2;  
    var win = "scrollbars=1,resizable=1,width=750,height=450,left="+winleft+",top="+winUp
    sList = window.open('<%=request.getContextPath()%>'+"/"+link, "list", win);
}

function open_negotiation_search(link) {
  CLICKED_LINK = '';
  if(validate()){
    DATA_CHANGED = 'false';
    var winleft = (screen.width - 650) / 2;
    var winUp = (screen.height - 450) / 2;  
    var win = "scrollbars=1,resizable=1,width=800,height=450,left="+winleft+",top="+winUp
   //var win = "toolbar=0,scrollbars=1,statusbar=0,menubar=0,resizable=1,width=650,height=400,left="+winleft+",top="+winUp
    sList = window.open('<%=request.getContextPath()%>'+"/"+link, "list", win);
  }
}

</script>
</head>
<% String value = "";%>
<% 
String pageId = request.getParameter("Menu_Id"); 
if (pageId == null) {
	pageId = "001";
}
%>
<body>
<table width="100%"  border="0" cellpadding="0" cellspacing="0">
	<tr>
    	<td>
        	<table width="100%" border="0" cellpadding="0" cellspacing="0" STYLE="background-color:black;border:0">
            	<tr>
                	<td><img src="<bean:write name='ctxtPath'/>/coeusliteimages/header_coeus2.gif" width="675" height="68"></td>
                    <td align="right" valign="top">
                    	<a href="javascript:;" onClick="MM_openBrWindow('web/clFeedback.jsp?module=IPF','','toolbar=no,width=500,height=325')">                       
                    </td>
                    <td cellpadding="2" align="right"  class="copysmall">&nbsp;&nbsp;&nbsp;<span class="copysmallwhite">
                        <bean:message bundle="proposal" key="clProposalHeader.user"/><%--User--%>: &nbsp;<%=loggedinUser%>                         
                    </td>
                </tr>
                <tr>
                	<td height='20' colspan='3' align='right'>
                   		<span class="copysmallwhite">
                        	<html:link href="javaScript:getLockIds();">
                            	<font size='2' color='#FFFFFF'><u><bean:message bundle="proposal" key="locking.currentLockslabel"/></u></font>
                            </html:link>  &nbsp;&nbsp;&nbsp;                       
                      	</span>
                   	</td>                
            	</tr>
        	</table>
     	</td>
	</tr>
	<tr>
    	<td>
        	<table width="100%" height="24"  border="0" cellpadding="0" cellspacing="0" STYLE="background-image:url('<bean:write name='ctxtPath'/>/coeusliteimages/tile_mainmenu.gif');border:0">
            	<tr>
                	<td width="100%">
                		<!-- JM 8-24-2012 added background color to menu for prettiness
                    	<table width="100%" height="24"  border="0" cellpadding="0" cellspacing="0" class="mainmenubg" style="background-color: #6E97CF;">-->
                    	<table width="100%" height="24"  border="0" cellpadding="0" cellspacing="0" class="mainmenubg" style="background-color: #fedc92;">
                        	<tr>
                            	<jsp:useBean id="headerItemsVector" scope="session" class="java.util.Vector" />
                                <%
                                for (int i =0; i<headerItemsVector.size();  i++) {
                                	edu.mit.coeuslite.utils.bean.CoeusHeaderBean coeusHeaderBean = (edu.mit.coeuslite.utils.bean.CoeusHeaderBean)headerItemsVector.get(i); 
                                    String path = coeusHeaderBean.getImagePath();
                                    String headerId = coeusHeaderBean.getHeaderId();
                                    String link = request.getContextPath()+coeusHeaderBean.getLink();
                                    String menuId = "image"+i;//coeusHeaderBean.getHeaderId();
                                    String onLoadImage = coeusHeaderBean.getOnLoadImage();
                                    boolean isSelected = coeusHeaderBean.isSelected();
                                    if (headerId.equals(pageId)) {
                                    	isSelected = true;
                                    }
                                    else {
                                    	isSelected = false;
                                    }
                              	%> 
                              	                                       
                                <td>
                                	<% 
                                	if (isSelected) { 
                                	%>
                                		<img src="<bean:write name='ctxtPath'/><%=onLoadImage%>" name="<%=menuId%>" 
                                        	width="120" height="24" border="0" >
                                    <% 
                                    }
                                	else if("008".equals(headerId)) {
                                    	value = "javascript:return callMenu('"+request.getContextPath()+link+"')";
                                   	%>
                                    	<html:link page='<%=link%>' onclick="<%=value%>"><img src="<bean:write name='ctxtPath'/><%=path%>" name="<%=menuId%>" 
                                        	width="130" height="24" border="0" onMouseOut="MM_swapImgRestore()" 
                                           	onMouseOver="MM_swapImage('<%=menuId%>','','<bean:write name='ctxtPath'/><%=onLoadImage%>',1)">
                                      	</html:link>
                                   	<% 
                                   	}
                                	else {
                                    	value = "javascript:return callMenu('"+request.getContextPath()+link+"')";
                                   	%>
                                        <html:link page='<%=link%>' onclick="<%=value%>"><img src="<bean:write name='ctxtPath'/><%=path%>" name="<%=menuId%>" 
                                        	width="120" height="24" border="0" onMouseOut="MM_swapImgRestore()" 
                                            onMouseOver="MM_swapImage('<%=menuId%>','','<bean:write name='ctxtPath'/><%=onLoadImage%>',1)">
                                      	</html:link>
                                 	<%
                                 	}
                                 	%>
                            	</td> 
                                                
                             	<%    
                                }
                                %>                                   
                                <td height="24" align="left" valign="top" class="mainmenubg"></td>
                                <% 
                                String applicationName = request.getContextPath();
                                String logonLink = applicationName;
                                %>                               
                         	</tr>                                
            	   		</table>
                                
       		           	<table width="100%" border="0"  class="tablemain">
                        	<tr STYLE="background-image:url('<bean:write name='ctxtPath'/>/coeusliteimages/tile_menu.gif');border:0">
                            	<td width='100%' align="right" valign="middle" class="copy"  STYLE="background-image:url('<bean:write name='ctxtPath'/>/coeusliteimages/body_background.gif');border:0"> 
								<% if ("003".equals(pageId)) { %>
										<%@include file="/coeuslite/vanderbilt/common/clProposalSubHeader.jsp" %>  
                            	<% } else if ("005".equals(pageId)) { %>
										<%@include file="/coeuslite/vanderbilt/common/clInboxSubHeader.jsp" %>  
                            	<% } else if ("009".equals(pageId)) { %>
										<%@include file="/coeuslite/vanderbilt/common/clAwardSubHeader.jsp" %>  
                            	<% } else if ("010".equals(pageId)) { %>
										<%@include file="/coeuslite/vanderbilt/common/clNegotiationSubHeader.jsp" %>  
								<% } %>
                            	</td>                                
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
