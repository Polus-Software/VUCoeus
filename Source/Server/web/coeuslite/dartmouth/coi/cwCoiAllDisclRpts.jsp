<%@page contentType="text/html"
	import="edu.mit.coeus.utils.CoeusProperties,edu.mit.coeus.utils.DateUtils,
java.util.Calendar,
edu.mit.coeus.utils.CoeusPropertyKeys,
org.apache.struts.validator.DynaValidatorForm,
edu.dartmouth.coeuslite.coi.beans.DisclosureBean,
edu.mit.coeuslite.utils.ComboBoxBean"%>
<%@page pageEncoding="UTF-8" import="edu.mit.coeus.bean.*"%>
<%@page errorPage="ErrorPage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="person" scope="session"
	class="edu.mit.coeus.bean.PersonInfoBean" />
<%--<jsp:useBean id="CurrDisclPer"  scope="session" class="edu.dartmouth.coeuslite.coi.beans.DisclosureBean" />--%>
<%--<jsp:useBean id="FinDiscl" scope="session" class="java.util.Vector" />
<bean:size  id="disclSize" name="FinDiscl"/>--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<%String path = request.getContextPath();
        String policyPath=CoeusProperties.getProperty(CoeusPropertyKeys.POLICY_FILE);%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>COI</title>
<link href="<%=path%>/coeuslite/dartmouth/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<script src="<%=path%>/coeuslite/dartmouth/utils/scripts/divSlide.js"></script>
<script src="<%=path%>/coeuslite/dartmouth/utils/scripts/Balloon.js"></script>
<script> 
        //var lastPopup;
        function showPopUp(linkId, frameId, width, height, src, bookmarkId, url) {
            showBalloon(linkId, frameId, width, height, src, bookmarkId);
            //showBalloonPopup(frameId);
        }
        
        var lastRowId = -1;
        var lastDataRowId;
        var lastDivId;
        var lastLinkId;
        var expandedRow = -1;

        var defaultStyle = "rowLine";
        var selectStyle = "rowHover";
        
        function selectRow(rowId, dataRowId, divId, linkId, historyFrame, url) {
            var row = document.getElementById(rowId); 
            if(rowId == lastRowId) {
                //Same Row just Toggle
                var style = document.getElementById(dataRowId).style.display;
                if(style == "") {
                    style = "none";
                    styleClass = defaultStyle;
                    expandedRow = -1;
                    toggleText = plus.src;
                }else{
                    style = "";
                    styleClass = selectStyle;
                    expandedRow = rowId;
                    toggleText = minus.src;
                }
                //document.getElementById(dataRowId).style.display = style;
                //Sliding - START
                ds = new DivSlider();
                if(style == "") {
                    document.getElementById(dataRowId).style.display = "";
                    ds.showDiv(divId);
                }else {
                     ds.hideDiv(divId, "document.getElementById('"+dataRowId+"').style.display='none'");
                }
                //Sliding - END
                row.className = defaultStyle;
                var imgId = "img"+linkId;
                document.getElementById(imgId).src=toggleText;
            }else {
                document.getElementById(dataRowId).style.display = "";
                
                //Create IFrame 
                var divRef = document.getElementById(divId);
        	if(!divRef.hasChildNodes()) {
                    addFrame(divId, historyFrame, url);
                }
                
                historyFrame = document.getElementById(historyFrame);
                var frameSrc = historyFrame.getAttribute("src");
                if(frameSrc == null || frameSrc == "") {
                    historyFrame.setAttribute("src", url);
                }
                
                //Sliding - START
                ds2 = new DivSlider();
                ds2.showDiv(divId);
                //Sliding - END

                row.className = defaultStyle;
                expandedRow = rowId;
                var imgId = "img"+linkId;
                document.getElementById(imgId).src=minus.src;
                
                //reset last selected row
                if(lastRowId != -1) {
                    row = document.getElementById(lastRowId);
                    //document.getElementById(lastDataRowId).style.display = "none";

                    ds3 = new DivSlider();
                    ds3.hideDiv(lastDivId, "document.getElementById('"+lastDataRowId+"').style.display='none'");

                    row.className = defaultStyle;
                    var imgId = "img"+lastLinkId;
                    document.getElementById(imgId).src=plus.src;
                }
            }

            lastRowId = rowId;
            lastDivId = divId;
            lastDataRowId = dataRowId;
            lastLinkId = linkId;
        }
        
        
        var plus = new Image();
        var minus = new Image();
        
        function preLoadImages() {
            plus.src = "<%=path%>/coeusliteimages/plus.gif";
            minus.src = "<%=path%>/coeusliteimages/minus.gif";
        }
        
        function addFrame(divId, historyFrame, url) {
            iframeElem = document.createElement("IFRAME");
            document.createAttribute("src");
            iframeElem.setAttribute("src", url);
            iframeElem.setAttribute("id", historyFrame);
            iframeElem.setAttribute("width", "100%");
            iframeElem.setAttribute("scrolling", "auto");
            iframeElem.setAttribute("marginHeight", "0");
            iframeElem.setAttribute("marginWidth", "0");
            iframeElem.setAttribute("frameBorder", "0");
            var divRef = document.getElementById(divId);
            divRef.appendChild(iframeElem);
        }
        
        preLoadImages();   
			
        </script>
</head>
<body>
	<table width="982" align="left" border="0" cellpadding="0"
		cellspacing="0">
		<tr height="20px" id="row0">
			<td valign="top" class="copybold"><a
				href="javascript:selectRow('row0','historyRow0','historyData0','toggle0','historyFrame0','<%=path%>/getDisclRpts.do?mode=C&due=0')"
				id="toggle0"><img
					src='<%=request.getContextPath()%>/coeusliteimages/plus.gif'
					border='none' id="imgtoggle0" name="imgtoggle0" border="none"></a>
				&nbsp;&nbsp;<bean:message bundle="coi" key="link.header.current" /></td>
		</tr>
		<tr id="historyRow0" style="display: none" class="copy rowHover">
			<td align="left">
				<!--NOTE: DO NOT PUT THE DIV END TAG IN NEXT LINE NOT PUT EVEN A SINGLE SPACE
                                                                    MOZILLA RETURNS TRUE FOR HASCHILDNODES EVEN IF A SPACE IS PRESENT B/N START
                                                                    AND END TAG :(
                                   -->
				<div id="historyData0" style="overflow: hidden;" align="left"></div>
			</td>
		</tr>
		<tr height="20px" id="row1">
			<td valign="top" class="copybold"><a
				href="javascript:selectRow('row1','historyRow1','historyData1','toggle1','historyFrame1','<%=path%>/getDisclRpts.do?mode=A&due=90')"
				id="toggle1"><img
					src='<%=request.getContextPath()%>/coeusliteimages/plus.gif'
					border='none' id="imgtoggle1" name="imgtoggle1" border="none"></a>
				&nbsp;&nbsp;<bean:message bundle="coi" key="link.header.due90" /></td>
		</tr>
		<tr id="historyRow1" style="display: none" class="copy rowHover">
			<td colspan="2">
				<!--NOTE: DO NOT PUT THE DIV END TAG IN NEXT LINE NOT PUT EVEN A SINGLE SPACE
                                                                    MOZILLA RETURNS TRUE FOR HASCHILDNODES EVEN IF A SPACE IS PRESENT B/N START
                                                                    AND END TAG :(
                                   -->
				<div id="historyData1" style="overflow: hidden;"></div>
			</td>
		</tr>

		<tr height="20px" id="row2">
			<td valign="top" class="copybold"><a
				href="javascript:selectRow('row2','historyRow2','historyData2','toggle2','historyFrame2','<%=path%>/getDisclRpts.do?mode=A&due=60')"
				id="toggle2"><img
					src='<%=request.getContextPath()%>/coeusliteimages/plus.gif'
					border='none' id="imgtoggle2" name="imgtoggle2" border="none"></a>
				&nbsp;&nbsp;<bean:message bundle="coi" key="link.header.due60" /></td>
		</tr>
		<tr id="historyRow2" style="display: none" class="copy rowHover">
			<td colspan="2">
				<!--NOTE: DO NOT PUT THE DIV END TAG IN NEXT LINE NOT PUT EVEN A SINGLE SPACE
                                                                    MOZILLA RETURNS TRUE FOR HASCHILDNODES EVEN IF A SPACE IS PRESENT B/N START
                                                                    AND END TAG :(
                                   -->
				<div id="historyData2" style="overflow: hidden;"></div>
			</td>
		</tr>

		<tr height="20px" id="row3">
			<td class="copybold" valign="top"><a
				href="javascript:selectRow('row3','historyRow3','historyData3','toggle3','historyFrame3','<%=path%>/getDisclRpts.do?mode=A&due=30')"
				id="toggle3"><img
					src='<%=request.getContextPath()%>/coeusliteimages/plus.gif'
					border='none' id="imgtoggle3" name="imgtoggle3" border="none"></a>
				&nbsp;&nbsp;<bean:message bundle="coi" key="link.header.due30" /></td>
		</tr>
		<tr id="historyRow3" style="display: none" class="copy rowHover">
			<td colspan="2">
				<!--NOTE: DO NOT PUT THE DIV END TAG IN NEXT LINE NOT PUT EVEN A SINGLE SPACE
                                                                    MOZILLA RETURNS TRUE FOR HASCHILDNODES EVEN IF A SPACE IS PRESENT B/N START
                                                                    AND END TAG :(
                                   -->
				<div id="historyData3" style="overflow: hidden;"></div>
			</td>
		</tr>
		<tr height="20px" id="row4">
			<td class="copybold" valign="top"><a
				href="javascript:selectRow('row4','historyRow4','historyData4','toggle4','historyFrame4','<%=path%>/getDisclRpts.do?mode=A&due=-90')"
				id="toggle4"><img
					src='<%=request.getContextPath()%>/coeusliteimages/plus.gif'
					border='none' id="imgtoggle4" name="imgtoggle4" border="none"></a>
				&nbsp;&nbsp;<bean:message bundle="coi" key="link.header.overdue90" /></td>
		</tr>
		<tr id="historyRow4" style="display: none" class="copy rowHover">
			<td colspan="2">
				<!--NOTE: DO NOT PUT THE DIV END TAG IN NEXT LINE NOT PUT EVEN A SINGLE SPACE
                                                                    MOZILLA RETURNS TRUE FOR HASCHILDNODES EVEN IF A SPACE IS PRESENT B/N START
                                                                    AND END TAG :(
                                   -->
				<div id="historyData4" style="overflow: hidden;"></div>
			</td>
		</tr>

		<tr height="20px" id="row5">
			<td valign="top" class="copybold"><a
				href="javascript:selectRow('row5','historyRow5','historyData5','toggle5','historyFrame5','<%=path%>/getDisclRpts.do?mode=A&due=-60')"
				id="toggle5"><img
					src='<%=request.getContextPath()%>/coeusliteimages/plus.gif'
					border='none' id="imgtoggle5" name="imgtoggle5" border="none"></a>
				&nbsp;&nbsp;<bean:message bundle="coi" key="link.header.overdue60" /></td>
		</tr>
		<tr id="historyRow5" style="display: none" class="copy rowHover">
			<td colspan="2">
				<!--NOTE: DO NOT PUT THE DIV END TAG IN NEXT LINE NOT PUT EVEN A SINGLE SPACE
                                                                    MOZILLA RETURNS TRUE FOR HASCHILDNODES EVEN IF A SPACE IS PRESENT B/N START
                                                                    AND END TAG :(
                                   -->
				<div id="historyData5" style="overflow: hidden;"></div>
			</td>
		</tr>
		<tr height="20px" id="row6">
			<td class="copybold" valign="top"><a
				href="javascript:selectRow('row6','historyRow6','historyData6','toggle6','historyFrame6','<%=path%>/getDisclRpts.do?mode=A&due=-30')"
				id="toggle6"><img
					src='<%=request.getContextPath()%>/coeusliteimages/plus.gif'
					border='none' id="imgtoggle6" name="imgtoggle6" border="none"></a>
				&nbsp;&nbsp;<bean:message bundle="coi" key="link.header.overdue30" /></td>
		</tr>
		<tr id="historyRow6" style="display: none" class="copy rowHover">
			<td colspan="2">
				<!--NOTE: DO NOT PUT THE DIV END TAG IN NEXT LINE NOT PUT EVEN A SINGLE SPACE
                                                                    MOZILLA RETURNS TRUE FOR HASCHILDNODES EVEN IF A SPACE IS PRESENT B/N START
                                                                    AND END TAG :(
                                   -->
				<div id="historyData6" style="overflow: hidden;"></div>
			</td>
		</tr>
		<tr height="20px" id="row7">
			<td valign="top" class="copybold"><a
				href="javascript:selectRow('row7','historyRow7','historyData7','toggle7','historyFrame7','<%=path%>/getDisclRpts.do?mode=A&due=-1')"
				id="toggle7"><img
					src='<%=request.getContextPath()%>/coeusliteimages/plus.gif'
					border='none' id="imgtoggle7" name="imgtoggle7" border="none"></a>
				&nbsp;&nbsp;<bean:message bundle="coi" key="link.header.overdue0" /></td>
		</tr>
		<tr id="historyRow7" style="display: none" class="copy rowHover">
			<td colspan="2">
				<!--NOTE: DO NOT PUT THE DIV END TAG IN NEXT LINE NOT PUT EVEN A SINGLE SPACE
                                                                    MOZILLA RETURNS TRUE FOR HASCHILDNODES EVEN IF A SPACE IS PRESENT B/N START
                                                                    AND END TAG :(
                                   -->
				<div id="historyData7" style="overflow: hidden;"></div>
			</td>
		</tr>
	</table>
</body>
</html>
