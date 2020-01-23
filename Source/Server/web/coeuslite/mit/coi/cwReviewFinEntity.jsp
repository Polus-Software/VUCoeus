
<%@ page
	import="org.apache.struts.validator.DynaValidatorForm, org.apache.struts.action.DynaActionForm"%>

<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<jsp:useBean id="FinEntityData" scope="session" class="java.util.Vector" />
<jsp:useBean id="person" scope="session"
	class="edu.mit.coeus.bean.PersonInfoBean" />
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<bean:size id="certificatesSize" name="FinEntityData" />
<!--jsp:useBean id="FinEntityData" scope ="request" class="java.util.Vector" /-->

<html:html>
<head>
<title>Coeus Web</title>
<html:base />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

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

<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<style type="text/css">
<!--
body {
	margin-left: 3px;
}
-->
</style>
<script language="javascript" type="text/JavaScript"
	src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/sorttable.js"></script>
</head>
<body>
	<a name="top"></a>

	<table width="980" border="0" cellpadding="3" cellspacing="0"
		class="table">
		<% String actionFrom = (String)request.getAttribute("actionFrom");
request.setAttribute("actionFrom",actionFrom);%>
		<tr>
			<td height="20" align="left" valign="top" class="theader"><bean:message
					bundle="coi" key="financialEntity.headerFinEnt" /> <%=person.getFullName()%>
			</td>
		</tr>
		<tr>
			<td height='20' class="copy">&nbsp;&nbsp;<b> <bean:message
						bundle="coi" key="financialEntity.header" />
			</b>
			</td>
		</tr>
		<tr>
			<td height='20' class="copy">&nbsp;&nbsp; <input type="button"
				value="Add Financial Entities"
				onclick="javascript:window.location = '<%=path%>/getAddFinEnt.do'">
			</td>
		</tr>
		<logic:present name="backToDiscl">
			<tr class='copy'>
				<td><b>&nbsp; <logic:equal name="backToDiscl"
							value="addDiscl">
							<a
								href="<bean:write name="ctxtPath"/>/getAddDisclosure.do?actionFrom=editFinEnt">
								<bean:message bundle="coi"
									key="financialEntity.backToDisclAdding" />
							</a>
						</logic:equal> <logic:equal name="backToDiscl" value="editDiscl">
							<a
								href="<bean:write name="ctxtPath"/>/viewCOIDisclosureDetails.do?action=edit&actionFrom=editFinEnt">
								<bean:message bundle="coi"
									key="financialEntity.backToDisclEditing" />
							</a>
						</logic:equal>
				</b></td>
			</tr>
		</logic:present>

		<tr class='copybold' align="left">
			<font color="red"> <html:messages id="message"
					message="true">
					<bean:write name="message" />
				</html:messages>
			</font>
		</tr>
		<!-- FinancialEntityDetails - Start  -->
		<tr>
			<td align="left" valign="top"><logic:equal
					name="certificatesSize" value="0">
					<table width="95%" align="right" border="0">
						<tr>
							<td align='center'><bean:message bundle="coi"
									key="financialEntity.noFinancialEnt" /></td>
						</tr>
					</table>
				</logic:equal> <logic:present name="FinEntityData" scope="session">
					<table width="99%" align="center" border="0" cellpadding="3"
						id="t1" class="tabtable">
						<tr>
							<td width="2%" colspan="1" class="theader" nowrap>&nbsp;</td>
							<td width="25%" align="left" class="theader" nowrap><bean:message
									bundle="coi" key="label.entityName" /></td>
							<td width="8%" align="left" class="theader" nowrap><bean:message
									bundle="coi" key="label.status" /></td>
							<td width="40%" align="left" class="theader" nowrap><bean:message
									bundle="coi" key="label.relationShipToEntity" /></td>
							<td width="15%" align="left" class="theader" nowrap><bean:message
									bundle="coi" key="label.lastUpdated" /></td>
							<td width="10%" colspan="1" class="theader" nowrap>&nbsp;</td>

							<!--td width="25%" align="left" class="tableheadersub3"></td-->
						</tr>

						<%  String strBgColor = "#DCE5F1";
    int index=0;
    %>

						<logic:iterate id="data" name="FinEntityData"
							type="org.apache.commons.beanutils.DynaBean">

							<%   
    
    if (index%2 == 0) {
        strBgColor = "#D6DCE5";
    } else {
        strBgColor="#DCE5F1";
    }
    %>

							<%     
    
    java.util.Map params = new java.util.HashMap();
    params.put("entityNumber", data.get("entityNumber"));
    params.put("entityName", data.get("entityName"));
    params.put("actionFrom","coiFinEntity");
    pageContext.setAttribute("paramsMap", params); 
    %>
							<tr bgcolor="<%=strBgColor%>" id="row<%=index%>" class="rowLine"
								onmouseover="className='rowHover rowLine'"
								onmouseout="className='rowLine'">

								<td width="2%" class="copy" wrap colspan="0">
									<%
            String entityNumber = (String)data.get("entityNumber");
            String url = ""+path+"/getFinEntHistory.do?entityNumber="+entityNumber;
            
            String onClick = "selectRow('row"+index+"', 'historyRow"+index+"', 'historyData"+index+"' , 'toggle"+index+"', 'historyFrame"+index+"', '"+url+"')";
            %> <script>
                                            var link = "javascript:<%=onClick%>";
                                            document.write("<a href=\""+link+"\" id=\"toggle<%=index%>\"> <img src='<%=request.getContextPath()%>/coeusliteimages/plus.gif' border='0' id='imgtoggle<%=index%>' name='imgtoggle<%=index%>'> </a>");
            </script>
									<noscript>
										<a target="_blank" href="<%=url%>">&gt;&gt;</a>
									</noscript>
								</td>

								<td width="25%" class="copy" wrap colspan="0">
									<%--<html:link  action="/viewFinEntity.do" name="paramsMap" scope="page">--%>
									<coeusUtils:formatOutput name="data" property="entityName" /> <%--</html:link>--%>
								</td>

								<td width="8%" align="left" class="copy" width="10%"><logic:present
										name="data" property="statusCode">
										<logic:equal name="data" property="statusCode" value='1'>
											<%--<html:link  action="/viewFinEntity.do" name="paramsMap" scope="page">--%>
											<bean:message bundle="coi" key="financialEntity.active" />
											<%--</html:link>--%>
										</logic:equal>

										<logic:notEqual name="data" property="statusCode" value='1'>
											<%--<html:link  action="/viewFinEntity.do" name="paramsMap" scope="page">--%>
											<bean:message bundle="coi" key="financialEntity.inactive" />
											<%--</html:link>--%>
										</logic:notEqual>
									</logic:present></td>

								<td width="40%" align="left" nowrap class="copy">
									<%--<html:link  action="/viewFinEntity.do" name="paramsMap" scope="page">--%>
									<coeusUtils:formatOutput name="data"
										property="relationShipTypeDesc" /> <%--</html:link>--%> <logic:notEmpty
										name="data" property="relationShipDesc">
										<a id="link<%=index%>"
											href="javascript:showPopUp('link<%=index%>', 'relationShipDesc<%=index%>')"
											title="Click to View Relationship Description">[...]</a>
										<div id="relationShipDesc<%=index%>"
											style="display: none; overflow: auto; position: absolute; width: 250; height: 75"
											onclick="hideBalloon('relationShipDesc<%=index%>')">
											<table border="0" width="100%" cellpadding="2"
												cellspacing="0" class="lineBorderWhiteBackgrnd">
												<tr>
													<td style="border-style: none"></td>
													<td style="border-style: none" align="right"><a
														href="javascript:hideBalloon('relationShipDesc<%=index%>')"><img
															border="0" src="<%=path%>/coeusliteimages/none.gif"></a></td>
												</tr>
												<tr>
													<td style="border-style: none" colspan="2" valign="top">
														<bean:write name="data" property="relationShipDesc" />
													</td>
												</tr>
											</table>
										</div>
									</logic:notEmpty>
								</td>
								<!--Update Timestamp - START -->
								<td width="15%" align="left" class="copy" nowrap><coeusUtils:formatDate
										name="data" formatString="MM-dd-yyyy  hh:mm a"
										property="updtimestamp" /> <bean:write name="data"
										property="upduser" /></td>
								<!--Update Timestamp - END -->
								<td align="center" class='copy' width="10%" nowrap>[ <logic:equal
										name="data" property="statusCode" value='1'>
										<html:link action="/deactivateFinEnt" name="paramsMap"
											scope="page">
											<u> <bean:message bundle="coi"
													key="financialEntity.removeLink" />
											</u>
										</html:link>

									</logic:equal> <logic:notEqual name="data" property="statusCode" value='1'>
										<html:link action="/activateFinEnt" name="paramsMap"
											scope="page">
											<u> <bean:message bundle="coi"
													key="financialEntity.makeActiveLink" />
											</u>
										</html:link>

									</logic:notEqual> ] <!--</td>
                                     
                                    <td class='copy' align="center">-->
									[ <html:link action="/editFinEnt" name="paramsMap" scope="page">
										<u> <bean:message bundle="coi"
												key="financialEntity.editLink" />
										</u>
									</html:link> ]

								</td>
							</tr>

							<!-- History Details - START-->
							<tr id="historyRow<%=index%>" style="display: none"
								class="copy rowHover">
								<td colspan="6">
									<%--<div id="historyData<%=index%>" style="overflow:hidden;">
                <iframe id="historyFrame<%=index%>" width="100%" name="historyFrame<%=index%>" scrolling="auto" marginheight="0" marginwidth="0" frameborder=0> 
                </iframe>
            </div>--%> <%--
            NOTE: DO NOT PUT THE DIV END TAG IN NEXT LINE NOT PUT EVEN A SINGLE SPACE
            MOZILLA RETURNS TRUE FOR HASCHILDNODES EVEN IF A SPACE IS PRESENT B/N START
            AND END TAG :(
            --%>
									<div id="historyData<%=index%>" style="overflow: hidden;"></div>
								</td>
							</tr>
							<!-- History Details - END -->

							<% 
    index++;
    %>
						</logic:iterate>

					</table>
				</logic:present></td>
		</tr>
		<tr>
			<td align="left" valign="top">&nbsp;&nbsp; <input type="button"
				value="My COI Home"
				onclick="javascript:window.location = '<%=path%>/coi.do'"
				style="width: 200px">
			</td>
		</tr>
	</table>


	<%
if(request.getAttribute("FESubmitSuccess") != null){
        request.removeAttribute("FESubmitSuccess");
        
        String message = (String)request.getAttribute("entityName");
        String actionType = (String)request.getAttribute("actionType");
        String reloadLocation = request.getContextPath()+"/getReviewFinEnt.do";
        if(actionType != null){
            if(actionType.equals("I")){
                message += " successfully added.";
            } else if(actionType.equals("U") ){
                message += " successfully modified.";
            } else if(actionType.equals("activate") ){
                message += " made active.";
            } else if(actionType.equals("deactivate") ){
                message += " made inactive.";
            }
        }
        int indexOfApost = message.indexOf("'");
        if(indexOfApost != -1){
            int indexOfApostIncr = indexOfApost+1;
            int endOfMessage = message.length();
            String message1 = message.substring(0, indexOfApost);
            String message2 = message.substring(indexOfApostIncr, message.length());
            message = message1+message2;
        }
        out.print("<script language='javascript'>");
        // 2656:CoeusLite-COI Disclosure
//        out.print("confirm('"+message+"');");
        out.print("alert('"+message+"');");
//out.print("openwintext('FESubmitSuccess.jsp', 'getFinEnt3');");//can't use because of popup blockers
        out.print("window.location='"+reloadLocation+"';");
        out.print("</script>");
}

%>
</body>
</html:html>
