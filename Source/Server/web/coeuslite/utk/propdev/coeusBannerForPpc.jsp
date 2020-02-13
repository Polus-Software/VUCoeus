<%-- 
    Document   : coeusBannerForPpc
    Created on : Feb 15, 2011, 9:06:58 AM
    Author     : anishk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="/WEB-INF/struts-bean.tld"  prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld"  prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld"  prefix="coeusUtils"%>
<%@ include file= "/coeuslite/mit/utils/CoeusContextPath.jsp"  %>
<%-- page body header information --%>
<jsp:useBean id="proposalNumber" scope="request" class="java.lang.String" />
<jsp:useBean id="user" scope="session" class="edu.mit.coeus.bean.UserInfoBean" />
<jsp:useBean id="loggedinUser" scope="session" class="java.lang.String"/>
<jsp:useBean id="genInfoUpdTimestamp" scope="session" class="java.lang.String" />
<jsp:useBean id="createTimestamp" scope="session" class="java.lang.String" />
<jsp:useBean id="createUser" scope="session" class="java.lang.String" />
<jsp:useBean id="actionType" scope="request" class="java.lang.String" />
<jsp:useBean id="unitNumber" scope="session" class="java.lang.String" />
<jsp:useBean id="unitName" scope="session" class="java.lang.String" />
<jsp:useBean id="creationStatDesc" scope="session" class="java.lang.String" />
<jsp:useBean id="propInvPersonEditableColumns" scope="session" class="java.util.HashMap" />
<bean:size id="colSize" name ="propInvPersonEditableColumns" />
<%-- end of header information --%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
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
</script>
<script>
function callMenu(value){
    CLICKED_LINK = value;
    //return validateSavedInfo();
}
function getLockIds() {
            CLICKED_LINK = '';
            //if(validateSavedInfo()) {
                DATA_CHANGED = 'false';
                var winleft = (screen.width - 650) / 2;
                var winUp = (screen.height - 450) / 2;
                var win = "scrollbars=1,resizable=1,width=770,height=450,left="+winleft+",top="+winUp
                sList = window.open("<%=request.getContextPath()%>//getLockIdsList.do", "list", win);
            //}
          }
function open_proposal_search(link)
          {
            DATA_CHANGED = 'false';
            var winleft = (screen.width - 650) / 2;
            var winUp = (screen.height - 450) / 2;
            var win = "scrollbars=1,resizable=1,width=750,height=450,left="+winleft+",top="+winUp
            //var win = "toolbar=0,scrollbars=1,statusbar=0,menubar=0,resizable=1,width=650,height=400,left="+winleft+",top="+winUp
            sList = window.open('<%=request.getContextPath()%>'+"/"+link, "list", win);
          }
    function logout()
           {
               document.proposalPrint.action="<%=request.getContextPath()%>/logoutAction.do";
               document.proposalPrint.submit();
           }
</script>

<link rel=stylesheet href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/ipf.css" type="text/css">
<link href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css" rel="stylesheet" type="text/css">

<html:base/>
    </head>
    <body>
       <html:form action="/proposalPersonsCertify">
      <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
      
    <!-- headerer image-->

        <tr>
        <td>
            <table width="100%" border="0" cellpadding="0" cellspacing="0" STYLE="background-image:url('<bean:write name='ctxtPath'/>/coeusliteimages/header_background.gif');border:0">
                <tr>
                    <td><img src="<bean:write name='ctxtPath'/>/coeusliteimages/header_coeus2.gif" width="675" height="42"></td>
                    <td align="right" valign="top">
                        <a href="javascript:;" onClick="MM_openBrWindow('web/clFeedback.jsp?module=IPF','','toolbar=no,width=500,height=325')">
                    </td>
                    <td cellpadding="2" align="right"  class="copysmall">&nbsp;&nbsp;&nbsp;<span class="copysmallwhite">
                        <bean:message bundle="proposal" key="clProposalHeader.user"/>: &nbsp;<%=loggedinUser%>


                    </td>
                </tr>
                <tr>
                   <td height='20' colspan='3' align='right'>
                      <span class="copysmallwhite">
                            <html:link href="javascript:logout()">
                               <font size='2' color='#D1E5FF'><u>Logout</u></font>
                            </html:link>  &nbsp;&nbsp;&nbsp;
                        </span>
                   </td>
                </tr>
            </table>
        </td>
    </tr>
      
      </table>
        </html:form>
    </body>
</html>
