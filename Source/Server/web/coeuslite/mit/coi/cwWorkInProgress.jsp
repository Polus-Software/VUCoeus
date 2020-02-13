<%-- 
    Document   : cwWorkInProgress
    Created on : Dec 29, 2008, 3:21:36 PM
    Author     : sharathk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" language="java"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="java.sql.Timestamp"%>

<%@page errorPage="ErrorPage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>

<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>

<jsp:useBean id="workInProgress" scope="request"
	class="java.util.Vector" />
<bean:size id="vectSize" name="workInProgress" />

<jsp:useBean id="person" scope="session"
	class="edu.mit.coeus.bean.PersonInfoBean" />

<html:html>
<head>
<title>Coeus Web</title>

<%String path = request.getContextPath();%>
<script src="<%=path%>/coeuslite/mit/utils/scripts/divSlide.js"></script>
<script src="<%=path%>/coeuslite/mit/utils/scripts/Balloon.js"></script>
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

        //Variabled used to increase/decrease the height of the Iframe after expanded to set to proper height
        //depending on the content.
        var strIframeId;
        var strDivId;

        var defaultStyle = "rowLine";
        var selectStyle = "rowHover";


    
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
                //alert(divRef.hasChildNodes());
        	if(!divRef.hasChildNodes()) {
                    addFrame(divId, historyFrame, url);
                }
                strDivId = divId;
                strIframeId = historyFrame;
                historyFrame = document.getElementById(historyFrame);
                var frameSrc = historyFrame.getAttribute("src");
                if(frameSrc == null || frameSrc == "") {
                    historyFrame.setAttribute("src", url);
                }
                
                //Sliding - START
                ds2 = new DivSlider();
                ds2.showDiv(divId, "setHeight()");
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

        /*
         * Called to set the height of the Iframe after expanded to set to proper height
         * depending on the content.
         */
        function setHeight(){
            var height = document.getElementById(strIframeId).contentWindow.document.body.scrollHeight + 10;
            if(height < 50) height = 50;
            document.getElementById(strIframeId).height = height;
            document.getElementById(strDivId).style.height = height;
        }
        
        preLoadImages(); 
   </script>
<html:base />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
</head>
<body>

	<a name="top"></a>
	<%--  ************  START OF BODY TABLE  ************************--%>
	<%int disclIndex = 0;%>
	<table width="980" height="100%" border="0" cellpadding="0"
		cellspacing="0" class="table">

		<tr>
			<td height="20" align="left" valign="top" class="tableheader"><bean:message
					bundle="coi" key="coiWIP.header" /></td>
		</tr>

		<!-- COI Work In Progress - Start  -->
		<tr>

			<td valign="top"><b>&nbsp;</b>

				<table width="98%" border="0" align="center" cellpadding="2"
					cellspacing="0" id="t1" class="tabtable">

					<tr class="tableheader">
						<td width="2%" align="left" class="theader"></td>
						<td width="19%" align="left" class="theader"><bean:message
								bundle="coi" key="label.personName" /></td>
						<td width="20%" align="left" class="theader"><bean:message
								bundle="coi" key="label.department" /></td>
						<td width="15%" align="left" class="theader"><bean:message
								bundle="coi" key="label.DiscDate" /></td>
						<td width="10%" align="left" class="theader"></td>

					</tr>


					<logic:present name="workInProgress" scope="request">
						<%  
                                                     String strBgColor = "#DCE5F1";
                                                   //  int size=reviewAnnualDisc.size();
                                                  //   System.out.println("VECTOR SIZE="+size);
                                                     
                                                    %>
						<logic:notEqual name='vectSize' value='0'>
							<logic:iterate id="discInfo" name="workInProgress"
								type="org.apache.commons.beanutils.DynaBean">
								<bean:define id="personID" name="discInfo" property="personId" />
								<bean:define id="fullName" name="discInfo" property="fullName" />
								<%                                  
                                                            if (disclIndex%2 == 0) {
                                                            strBgColor = "#D6DCE5"; 
                                                            }
                                                            else { 
                                                            strBgColor="#DCE5F1"; 
                                                            }
                                                            
                                                            %>

								<tr bgcolor='<%=strBgColor%>' id="row<%=disclIndex%>"
									class="rowLine" onmouseover="className='rowHover rowLine'"
									onmouseout="className='rowLine'">

									<td class="copy">
										<%
                                                                    //String personId = person.getPersonID();
                                                                    /* String moduleCode = (String)data.get("moduleCode");
                                                                    String moduleItemKey = (String)data.get("moduleItemKey");
                                                                    String coiDisclosureNumber = (String)data.get("coiDisclosureNumber");
                                                                    String url = ""+path+"/getDisclosureHistory.do?moduleCode="+moduleCode+"&moduleItemKey="+moduleItemKey+"&disclosureNum="+coiDisclosureNumber;
                                                                    
                                                                    String onClick = "selectRow('row"+disclIndex+"', 'historyRow"+disclIndex+"', 'historyData"+disclIndex+"' , 'toggle"+disclIndex+"', 'historyFrame"+disclIndex+"', '"+url+"')";*/
                                                                    // String url=""+path+"/getPIAnnualDiscl.do?PersonId="+personId;
                                                                    String url=""+path+"/getWipDisclosures.do?personId="+personID;
                                                                    String onClick="selectRow('row"+disclIndex+"', 'historyRow"+disclIndex+"', 'historyData"+disclIndex+"' , 'toggle"+disclIndex+"', 'historyFrame"+disclIndex+"', '"+url+"')";
                                                                    %> <script>
                                            var link = "javascript:<%=onClick%>";
                                            document.write("<a href=\""+link+"\" id=\"toggle<%=disclIndex%>\"> <img src='<%=request.getContextPath()%>/coeusliteimages/plus.gif' border='0' id='imgtoggle<%=disclIndex%>' name='imgtoggle<%=disclIndex%>'> </a>");
                                                                    </script>
										<noscript>
											<a target="_blank" href="<%=url%>">&gt;&gt;</a>
										</noscript>
									</td>

									<td width="19%" class="copy" valign=top wrap colspan="0">
										<coeusUtils:formatOutput name="discInfo" property="fullName" />
									</td>
									<td width="20%" class="copy" valign=top wrap colspan="0">
										<coeusUtils:formatOutput name="discInfo" property="leadUnit" />
									</td>
									<td width="15%" class="copy" valign=top wrap colspan="0">
										<bean:define id="updTS" name="discInfo"
											property="updtimestamp" /> <%
                                                                        if(updTS!=null){
                                                                    %> <%=edu.mit.coeus.utils.DateUtils.formatDate(Timestamp.valueOf(updTS.toString()),"MM-dd-yyyy  hh:mm a")%>
										<%
                                                                        }
                                                                        //String s = dateFormat.format(Timestamp.valueOf(updTS));    
                                                                    %> <%--<coeusUtils:formatOutput name="discInfo" property="updtimestamp"/>--%>
									</td>

									<td width="10%" class="copy" valign=top wrap colspan="0">
										<%--<a href="<%=request.getContextPath()%>/approveAllDisclosures.do?personId=<%=personID%>&person=<%=fullName%>" onclick="return confirmation('<bean:write name="discInfo" property="fullName"/>');">Approve</a>--%>
										<a href="<%=path%>/getPIFinEnt.do?personId=<%=personID%>">Financial
											Entity</a> <%--    <coeusUtils:formatDate name=ss"discInfo" property="updtimestamp" />
                                                  --%>
									</td>
								</tr>
								<tr id="historyRow<%=disclIndex%>" style="display: none"
									class="copy rowHover">
									<td colspan="5">
										<div id="historyData<%=disclIndex%>" style="overflow: hidden;"></div>
										<%--
                                                                        <iframe id="historyFrame<%=disclIndex%>" width="100%" name="historyFrame<%=disclIndex%>" scrolling="auto" marginheight="0" marginwidth="0" frameborder=0> 
                                                                        </iframe>--%>

									</td>
								</tr>
								<% disclIndex++ ;%>
							</logic:iterate>
						</logic:notEqual>
						<logic:equal name='vectSize' value='0'>
							<tr>
								<td colspan="5">
									<table width="95%" align="right" border="0">
										<tr>
											<td class="copybold" align='center'><bean:message
													bundle="coi" key="label.noData" /></td>
										</tr>
									</table>
								</td>
							</tr>
						</logic:equal>
					</logic:present>
				</table></td>
		</tr>
		<!-- COI Work In Progress - End  -->

		<tr>
			<td height='10'>&nbsp;</td>
		</tr>
	</table>

</body>
</html:html>


