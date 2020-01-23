<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>


<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>C O I</title>
<%String path = request.getContextPath();%>
<script src="<%=path%>/coeuslite/dartmouth/utils/scripts/divSlide.js"></script>
<script src="<%=path%>/coeuslite/dartmouth/utils/scripts/Balloon.js"></script>
<%-- <script>
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
    </script>--%>
<link href="<%=request.getContextPath()%>/coiv2/css/layout.css"
	rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/coiv2/css/styles.css"
	rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/coiv2/css/collapsemenu.css"
	rel="stylesheet" type="text/css" />
<link
	href="<%=request.getContextPath()%>/coeuslite/dartmouth/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css" />
</head>
<script src="js/jquery.js" type="text/javascript"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$("#menu > li > a[@class=collapsed] ").find("+ ul").slideToggle("medium");
		$("#menu  > li > a").click(function() {
			$(this).toggleClass("expanded").toggleClass("collapsed").find("+ ul").slideToggle("medium");
		});
	});


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

  </script>
<body>
	<%--<div align="center">
<div id="wrapper">
<div id="main">--%>
	<div id="content"
		style="padding: 4px; width: 970px; background-color: #D1E5FF">
		<table style="background-color: #D1E5FF">
			<tr>
				<td valign="top">
					<table class="table" align="left" width="200px" bgcolor="#9DBFE9"
						style="padding: 4px;">
						<tr>
							<td>
								<div id="contentleft">
									<table class="table" align="center" width="98%"
										style="padding: 4px; border: 0">
										<tr class="menuHeaderName">
											<td colspan="3">Disclosure View</td>
										</tr>
										<tr class="rowline">
											<td width="16%" height='16' align="left" valign="top"
												class="coeusMenu"></td>
											<td width="80%" height='16' align="left" valign="middle"
												class="coeusMenu"><html:link
													page="/screeningquestions.do">Screening Questions</html:link>
											</td>
											<td width='4%' align=right class="selectedMenuIndicator"></td>
										</tr>
										<tr class="rowLine">
											<td width="16%" height='16' align="left" valign="top"
												class="coeusMenu"></td>
											<td width='80%' align="left" valign="middle"
												class="coeusMenu"><html:link
													page="/selectedFinancialentities.do"> Financial Entity</html:link>
											</td>
											<td width='4%' align=right class="selectedMenuIndicator"></td>
										</tr>
										<tr class="rowLine">
											<td width="16%" height='16' align="left" valign="top"
												class="coeusMenu"></td>
											<td align="left" valign="middle" class="coeusMenu"><html:link
													page="/notes.do">Notes</html:link></td>
											<td width='4%' align=right class="selectedMenuIndicator"></td>
										</tr>
										<tr class="rowLine">
											<td width="16%" height='16' align="left" valign="top"
												class="coeusMenu"></td>
											<td width="80%" align="left" valign="middle"
												class="coeusMenu"><html:link page="/attachments.do">Attachments</html:link>
											</td>
											<td width='4%' align=right class="selectedMenuIndicator"></td>
										</tr>
										<tr class="rowLine">
											<td width="16%" height='16' align="left" valign="top"
												class="coeusMenu"></td>
											<td width="80%" align="left" valign="middle"
												class="coeusMenu"><html:link page="/cert.do">Certification</html:link>
											</td>
											<td width='4%' align=right class="selectedMenuIndicator"></td>
										</tr>
										<tr class="rowLine">
											<td width="16%" height='16' align="left" valign="top"
												class="coeusMenu"></td>
											<td width="80%" align="left" valign="middle"
												class="coeusMenu"><html:link page="/cert.do">Disclosure Status</html:link>
											</td>
											<td width='4%' align=right class="selectedMenuIndicator"></td>
										</tr>
										<tr class="rowLine">
											<td width="16%" height='16' align="left" valign="top"
												class="coeusMenu"></td>
											<td width="80%" align="left" valign="middle"
												class="coeusMenu"><html:link page="/cert.do">Disposition Status</html:link>
											</td>
											<td width='4%' align=right class="selectedMenuIndicator"></td>
										</tr>
										<tr class="rowLine">
											<td width="16%" height='16' align="left" valign="top"
												class="coeusMenu"></td>
											<td width="80%" align="left" valign="middle"
												class="coeusMenu"><html:link page="/cert.do">Assign Rights</html:link>
											</td>
											<td width='4%' align=right class="selectedMenuIndicator"></td>
										</tr>
										<tr bgcolor="#9DBFE9">
											<td style="height: 410;">&nbsp;</td>
										</tr>
									</table>

								</div>
							</td>
						</tr>
					</table>
				</td>
				<td valign="top">





					<div id="contentright"
						style="padding: 0px; width: 760px; border: 0; vertical-align: top;">
						<div class="myForm"
							style="visibility: hidden; height: 1px; width: 100%"
							id="screeningquestionsdiv"></div>


						<div class="myForm"
							style="visibility: hidden; height: 1px; width: 100%; vertical-align: top;"
							id="financialentitydiv">

							<table id="bodyTable" class="table"
								style="width: 760px; height: 100%;" border="0">
								<tr style="background-color: #6E97CF;">
									<td><strong>Project Title</strong></td>
									<td><strong>Entity</strong></td>
									<td><strong>Entity Status</strong></td>
								</tr>
								<%
      String [] rowColors = {"#D6DCE5","#DCE5F1"};
      int i=0;
      String rowColor="";
                          %>
								<logic:present name="pjtEntDetView">
									<logic:iterate id="pjtEntView" name="pjtEntDetView">
										<%
                                                            if(i==0){
                                                                rowColor = rowColors[0];
                                                                i=1;
                                                            }
                                                               else{
                                                                rowColor = rowColors[1];
                                                                i=0;
                                                                }

                                                        %>
										<tr class="copybold" style="background-color: <%=rowColor%>">
											<td><bean:write name="pjtEntView"
													property="coiProjectTitle" /></td>
											<td><bean:write name="pjtEntView" property="entityName" />
											</td>
											<td><bean:write name="pjtEntView"
													property="entityStatus" /></td>
										</tr>
									</logic:iterate>
								</logic:present>


							</table>
						</div>

						<div
							style="width: 100%; height: 400px; overflow-x: hidden; overflow-y: scroll; vertical-align: top">
							<div class="myForm" style="visibility: hidden; height: 1px;"
								id="selectedFinancialentitiesdiv">
								<table id="bodyTable" class="table"
									style="width: 765px; height: 100%;" border="0">
									<tr>
										<td><strong> Disclosure Number</strong></td>
										<td><strong> Project</strong></td>
										<td><strong> Entity</strong</td>
										<td><strong> Status</strong></td>
									</tr>
									<logic:present name="finEntDetList">
										<%  String strBgColor = "#DCE5F1";
                    int index = 0;%>
										<logic:iterate id="finEntView" name="finEntDetList">
											<%
                                if (index%2 == 0) {
                                strBgColor = "#D6DCE5";
                                } else {
                                strBgColor="#DCE5F1";
                                }
                                %>
											<tr bgcolor="<%=strBgColor%>" id="row<%=index%>"
												class="rowLine" onmouseover="className='rowHover rowLine'"
												onmouseout="className='rowLine'" height="22px">
												<td>
													<%--                                                            <%
        //    String entityNumber = (String)finEntDet.get("entityNumber");
            String url = ""+path+"/getAnnFinEntHistory.do?entityNumber="+entityNumber;
            
            String onClick = "selectRow('row"+index+"', 'historyRow"+index+"', 'historyData"+index+"' , 'toggle"+index+"', 'historyFrame"+index+"', '"+url+"')";
            %>
            <script>
                                            var link = "javascript:<%=onClick%>";
                                            document.write("<a href=\""+link+"\" id=\"toggle<%=index%>\"> <img src='<%=request.getContextPath()%>/coeusliteimages/plus.gif' border='0' id='imgtoggle<%=index%>' name='imgtoggle<%=index%>'> </a>");
            </script>--%> <bean:write name="finEntView"
														property="entityName"></bean:write>
												</td>
												<td><bean:write name="finEntView"
														property="coiProjectTitle"></bean:write></td>
												<td><bean:write name="finEntView"
														property="coiDisclosureNumber"></bean:write></td>
												<td><bean:write name="finEntView"
														property="entityStatus"></bean:write></td>
											</tr>
											<%index++;%>
										</logic:iterate>
									</logic:present>
								</table>
							</div>
							<div class="myForm"
								style="visibility: hidden; height: 1px; vertical-align: top;"
								id="notesdiv">
								<div class="Formline4">

									<div class="FormColumn1">
										<strong> Notes Number</strong>
									</div>
									<div class="FormColumn1">
										<strong> Note</strong>
									</div>
									<div class="FormColumn1">
										<strong> Notes View</strong>
									</div>
									<div class="FormColumn1">
										<strong></strong>
									</div>
									<div class="FormColumn1">
										<strong> </strong>
									</div>
									<logic:present name="finEntDetList">
										<logic:iterate id="finEntView" name="finEntDetList">
											<div class="FormColumn1">
												<bean:write name="finEntView" property="coiProjectTitle"></bean:write>
											</div>
											<div class="FormColumn1">
												<bean:write name="finEntView" property="sequenceNumber"></bean:write>
											</div>
											<div class="FormColumn1">
												<bean:write name="finEntView" property="entityName"></bean:write>
											</div>
											<div class="FormColumn1"></div>
											<div class="FormColumn1"></div>
										</logic:iterate>
									</logic:present>
								</div>
							</div>
							<div class="myForm" style="visibility: hidden; height: 1px;"
								id="attachementdiv">ATTACHMENTS DETAILS HERE</div>
							<div class="myForm" style="visibility: hidden; height: 1px;"
								id="certdiv">CERTIFICATION DETAILS HERE</div>
							<div class="myForm" style="visibility: hidden; height: 1px;"
								id="disclstatus">DISCLOSURE STATUS HERE</div>
							<div class="myForm" style="visibility: hidden; height: 1px;"
								id="disposistatus">DISPOSITION STATUS HERE</div>
							<div class="myForm" style="visibility: hidden; height: 1px;"
								id="assignrights">ASSIGN RIGHTS HERE</div>


						</div>

					</div> <logic:present name="option">
						<logic:equal name="option" value="overview">
							<script> openDiv("overviewdiv",100)</script>
						</logic:equal>
						<logic:equal name="option" value="screeningquestions">
							<script> openDiv("screeningquestionsdiv",100)</script>
						</logic:equal>
						<logic:equal name="option" value="financialentities">
							<script> openDiv("financialentitydiv",100)</script>
						</logic:equal>
						<logic:equal name="option" value="notes">
							<script> openDiv("notesdiv",100)</script>
						</logic:equal>
						<logic:equal name="option" value="attachments">
							<script> openDiv("attachementdiv",100)</script>
						</logic:equal>
						<logic:equal name="option" value="cert">
							<script> openDiv("certdiv",100)</script>
						</logic:equal>
						<logic:equal name="option" value="selectedFinancialentities">
							<script> openDiv("selectedFinancialentitiesdiv",100)</script>
						</logic:equal>
						<logic:equal name="option" value="cert">
							<script> openDiv("disclstatus",100)</script>
						</logic:equal>
						<logic:equal name="option" value="cert">
							<script> openDiv("disposistatus",100)</script>
						</logic:equal>
						<logic:equal name="option" value="cert">
							<script> openDiv("assignrights",100)</script>
						</logic:equal>
					</logic:present> <%-- <div class="Formline5">

			  		 <input class="button" type="button" name="Save" id="save" value="Save"/>
					  <input class="button" type="button" name="Cancel" id="cancel" value="Cancel"/>

			  </div>--%>
					</div>
				</td>
			</tr>
		</table>
	</div>
	<%--</div>
</div>
</div>--%>
</body>
</html>
