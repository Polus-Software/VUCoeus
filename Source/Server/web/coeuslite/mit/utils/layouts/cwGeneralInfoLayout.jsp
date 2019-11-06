<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>

<%  //Frorces caches not to store new copy of the page from the origin server
    response.setHeader("Cache-Control", "no-cache");
    //directs caches not to store the page under any circumstance
    response.setHeader("Cache-Control", "no-store");
    //causes the proxy cache to see the page as "stale"
    response.setDateHeader("Expires", 0);
    //http 1.0 backward compatibility
    response.setHeader("Pragma", "no-cache");%>
<%String path = request.getContextPath();%>
<HTML>
<HEAD>

<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1252">
<link rel=stylesheet
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	type="text/css">
<script language="javascript" type="text/JavaScript"
	src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/Balloon.js"></script>
<script language="javascript" type="text/JavaScript"
	src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/divSlide.js"></script>
<title>Coeus Web Application</title>
<script>
            var lastPopup;
            function showPopUp(linkId, frameId, width, height, src, bookmarkId) {
                showBalloon(linkId, frameId, width, height, src, bookmarkId);
            }
        </script>
<script>
        var DATA_CHANGED = '';
        var LINK = '';
        var FORM_LINK;
        var PAGE_NAME = '';
        var CLICKED_LINK = '';
        var CONFIRM_MSG = '';
        // Added for COEUSQA-2724_Exceptions should be moved from Scientific Justification in IACUC_start
        var EXCEPTION_CHECKED_VALUE = '';
        var USDA_CHECKED_VALUE = '';
        // Added for COEUSQA-2724_Exceptions should be moved from Scientific Justification in IACUC_end
        function validate() {         
            if(LINK.indexOf('saveNewAmendmentRenewal.do') !=-1 ){
                if(CLICKED_LINK.indexOf('?')!=-1) {
                    CLICKED_LINK = CLICKED_LINK+"&Menu_Id=000";
                } else {
                    CLICKED_LINK = CLICKED_LINK+"?Menu_Id=000";
                }
            }
            //COEUSQA-1724 Email-Notifications For All Actions In IACUC - start
            if(PAGE_NAME == "<bean:message key="mailNotification.mailTitle" bundle="protocol"/>") {
                CONFIRM_MSG = "<bean:message key="mailNotification.sendconfirm" bundle="protocol"/> ";
                // 3783: No 'Do you want to save' When creating a new protocol and moving from General Info tab to Org
                 LINK = "<%=request.getContextPath()%>/mailAction.do?ConfirmType=SEND";
            }
            else if(PAGE_NAME == "<bean:message key="iacucMailNotification.mailTitle" bundle="iacuc"/>") {
                CONFIRM_MSG = "<bean:message key="mailNotification.sendconfirm" bundle="iacuc"/> ";
                 LINK = "<%=request.getContextPath()%>/iacucMailAction.do?ConfirmType=SEND";
            } 
            else {
                CONFIRM_MSG = "<bean:message key="confirmation.save"/> ";
            }           
           //COEUSQA-1724 Email-Notifications For All Actions In IACUC - end
           
            if( DATA_CHANGED == 'true' && confirm(CONFIRM_MSG+PAGE_NAME+"?")) {
                while(CLICKED_LINK.indexOf('&')!=-1){
                    CLICKED_LINK = CLICKED_LINK.replace("&","$");
                }
                if(LINK.indexOf('?')!=-1) {
                    FORM_LINK.action = LINK+"&CLICKED_LINK="+CLICKED_LINK;
                } else {
                    FORM_LINK.action = LINK+"?CLICKED_LINK="+CLICKED_LINK;
                }     
                
                if( FORM_LINK.name == "arraAwardDetailsForm"){
                    if(!validateArraDetails()){
                        return false;
                    }
                    if(document.arraAwardDetailsForm.projectStatus != null && document.arraAwardDetailsForm.projectStatus != undefined){
                        document.arraAwardDetailsForm.projectStatus.disabled=false;                
                    }
                    if(document.arraAwardDetailsForm.activityCode != null && document.arraAwardDetailsForm.activityCode != undefined){
                        document.arraAwardDetailsForm.activityCode.disabled=false;                
                    }
                    if(document.arraAwardDetailsForm.awardType != null && document.arraAwardDetailsForm.awardType != undefined){
                        document.arraAwardDetailsForm.awardType.disabled=false;                
                    }
                 }
                 // Added for COEUSQA-2724_Exceptions should be moved from Scientific Justification in IACUC_start
                 if( FORM_LINK.name == "iacucProtoSpeciesDynaBeansList"){
                    LINK = "<%=request.getContextPath()%>/saveIacucProtoSpecies.do?exception="+EXCEPTION_CHECKED_VALUE+"&usda="+USDA_CHECKED_VALUE; 
                    while(CLICKED_LINK.indexOf('&')!=-1){
                        CLICKED_LINK = CLICKED_LINK.replace("&","$");
                    }
                    if(LINK.indexOf('?')!=-1) {
                        FORM_LINK.action = LINK+"&CLICKED_LINK="+CLICKED_LINK;
                    } else {
                        FORM_LINK.action = LINK+"?CLICKED_LINK="+CLICKED_LINK;
                    }  
                }
                // Added for COEUSQA-2724_Exceptions should be moved from Scientific Justification in IACUC_end
                FORM_LINK.submit();
                return false;
            }
            return true;
        }
        function linkForward(errValue) {
              CLICKED_LINK = '<%=request.getParameter("CLICKED_LINK")%>';
              if(CLICKED_LINK == '' || CLICKED_LINK == null){
                 var winleft = (screen.width - 650) / 2;
                 var winUp = (screen.height - 450) / 2;  
                 var win = "scrollbars=1,resizable=1,width=770,height=450,left="+winleft+",top="+winUp                        
                 sList = window.open("<%=request.getContextPath()%>//getLockIdsList.do", "list", win);
              }
              if(!errValue && CLICKED_LINK!='null' && CLICKED_LINK.length>0) {
                while(CLICKED_LINK.indexOf('$')!=-1){
                    CLICKED_LINK = CLICKED_LINK.replace("$","&");
                }
                if(CLICKED_LINK.indexOf('.do') != -1
                    || CLICKED_LINK.indexOf('.jsp') != -1){
                    window.location= CLICKED_LINK;
                }
               
                CLICKED_LINK ='';
              }        
        }
    </script>
</HEAD>

<body text="#000000" link="#023264" alink="#023264" vlink="#023264">
	<!-- Commented and Modified For case# 3051-Floating scroll bar in Help in Protocol Screens in Lite - Start -->
	<!--
    <div  id="helpText" style="display:none;overflow:auto;position:absolute;width:300;height:250" onclick="hideBalloon('mitCOI')">
    <div style="overflow:auto;position:absolute;width:300;height:250">
        <table border="0" cellpadding="2" cellspacing="0" class="lineBorderWhiteBackgrnd">
            <tr><td></td><td align="right"><a href="javascript:hideBalloon('helpText')"><img border="0" src="<%=path%>/coeusliteimages/none.gif"></a></td></tr>
            <tr><td colspan="2">
                     <div id="txt">
                     </div>                            
                </td>
            </tr>
        </table>
    </div>
</div>
-->

	<div id="helpText"
		style="display: none; overflow: auto; position: absolute; width: 300; height: 250"
		onclick="hideBalloon('helpText')">
		<div style1="overflow:auto;position:absolute;width:300;height:250"
			class="helpPage">
			<div
				style="text-align: right; padding: 3Coueslite/mit/utils/scripts/Balloon.jspx">
				<a href="javascript:hideBalloon('helpText')"><img border="0"
					src="<%=path%>/coeusliteimages/none.gif"></a>
			</div>
			<div style="padding: 3px" id="txt"></div>
		</div>
	</div>
	<!-- Commented and Modified For case# 3051-Floating scroll bar in Help in Protocol Screens in Lite - End-->
	<table width='72%' height="100%" cellspacing="0" cellpadding="0"
		border="0" align="center">

		<tr>
			<td colspan="2" valign="top" height='104'><tiles:insert
					attribute="header" /></td>
		</tr>

		<tr>
			<td width="200" valign="top">
				<table width="200" border='0' cellspacing="4" cellpadding="0">
					<tr>
						<td><tiles:insert attribute='menu' /></td>
					</tr>
				</table>
			</td>
			<td width="820" valign="top" align=center>
				<table width='820' border='0' cellspacing="4" cellpadding="0"
					class="table">
					<tr>
						<td><tiles:insert attribute="subheader" /></td>
					</tr>
					<tr>
						<td><tiles:insert attribute="body" /></td>
					</tr>
				</table>
			</td>
		</tr>

		<tr>
			<td colspan="2" width='72%' valign=bottom align=center><tiles:insert
					attribute="footer" /></td>
		</tr>
	</table>
</body>
</html>

