<%@page errorPage="ErrorPage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="person" scope="session"
	class="edu.mit.coeus.bean.PersonInfoBean" />
<jsp:useBean id="perFinEnt" scope="session" class="java.util.Vector" />
<bean:size id="totentity" name="perFinEnt" />
<jsp:useBean id="dynaForm" scope="request"
	class="org.apache.struts.validator.DynaValidatorForm" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JSP Page</title>
<%String path = request.getContextPath();%>
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
	<logic:present name="perFinEnt" scope="session">
		<%  String strBgColor = "#DCE5F1";
                    int index = 0;%>
		<table width="986" align="center" border="0" cellpadding="0"
			cellspacing="0" class="table">
			<tr class="theaderBlue">
				<td height="20" class="theader" style="font-size: 14px" colspan="5">Financial
					Entities by <%=person.getFullName()%></td>
			</tr>
			<tr class="copybold">
				<td colspan='4'>&nbsp;</td>
			</tr>
			<tr class="theaderBlue">
				<td width="20px">&nbsp;</td>
				<td align="left" class="theader" width="207">Entity Name</td>
				<td align="left" class="theader" width="154">Status</td>
				<td align="left" class="theader" width="154">Last Updated</td>
				<td align="left" class="theader" width="87">&nbsp;</td>
			</tr>
			<logic:notEqual name="totentity" value='0'>
				<logic:iterate id="finEntDet" name="perFinEnt"
					type="org.apache.commons.beanutils.DynaBean">
					<bean:define id="entityNumber" name="finEntDet"
						property="entityNumber" />
					<bean:define id="entityName" name="finEntDet" property="entityName" />
					<input type="hidden" name="entityName" value="<%=entityName%>" />
					<input type="hidden" name="actionFrom" value="coiAnnFin" />

					<%                                  
                                if (index%2 == 0) {
                                strBgColor = "#D6DCE5";
                                } else {
                                strBgColor="#DCE5F1";
                                }
                                %>
					<tr bgcolor="<%=strBgColor%>" id="row<%=index%>" class="rowLine"
						onmouseover="className='rowHover rowLine'"
						onmouseout="className='rowLine'" height="22px">

						<td width="5%" class="copy" wrap colspan="0">
							<%
        //    String entityNumber = (String)finEntDet.get("entityNumber");
            String url = ""+path+"/getAnnFinEntHistory.do?entityNumber="+entityNumber;
            
            String onClick = "selectRow('row"+index+"', 'historyRow"+index+"', 'historyData"+index+"' , 'toggle"+index+"', 'historyFrame"+index+"', '"+url+"')";
            %> <script>
                                            var link = "javascript:<%=onClick%>";
                                            document.write("<a href=\""+link+"\" id=\"toggle<%=index%>\"> <img src='<%=request.getContextPath()%>/coeusliteimages/plus.gif' border='0' id='imgtoggle<%=index%>' name='imgtoggle<%=index%>'> </a>");
            </script>
							<noscript>
								<a target="_blank" href="<%=url%>">&gt;&gt;</a>
							</noscript>
						</td>
						<td class="copy"><a
							href='<%=path%>/reviewAnnFinEntity.do?entityNumber=<%=entityNumber%>'><coeusUtils:formatOutput
									name="finEntDet" property="entityName" /> </a></td>
						<td class="copy"><a
							href='<%=path%>/reviewAnnFinEntity.do?entityNumber=<%=entityNumber%>'><coeusUtils:formatOutput
									name="finEntDet" property="statusDesc" /> </a></td>
						<td class="copy"><a
							href='<%=path%>/reviewAnnFinEntity.do?entityNumber=<%=entityNumber%>'><coeusUtils:formatOutput
									name="finEntDet" property="updtimestamp" /> by <coeusUtils:formatOutput
									name="finEntDet" property="upduser" /></a></td>
						<td class="copy"><input type="hidden" name="entityName"
							value="<%=entityName%>" /> <input type="hidden" name="actionFrom"
							value="coiAnnFin" /> [ <logic:equal name="finEntDet"
								property="statusCode" value='1'>
								<a
									href='<%=path%>/deactivateAnnFinEnt.do?entityNumber=<%=entityNumber%>'>
									<%-- Commented for Case#4049 - "Remove" Entity label should be changed 
                                            <bean:message bundle="coi" key="financialEntity.removeLink"/> --%>
									<bean:message bundle="coi" key="financialEntity.makeInactive" />
								</a>

							</logic:equal> <logic:notEqual name="finEntDet" property="statusCode" value='1'>
								<a
									href='<%=path%>/activateAnnFinEnt.do?entityNumber=<%=entityNumber%>'>
									<bean:message bundle="coi" key="financialEntity.makeActiveLink" />
								</a>

							</logic:notEqual> ] &nbsp;&nbsp;</td>

					</tr>
					<tr id="historyRow<%=index%>" style="display: none"
						class="copy rowHover">
						<td colspan="5">
							<!--NOTE: DO NOT PUT THE DIV END TAG IN NEXT LINE NOT PUT EVEN A SINGLE SPACE
                                                                    MOZILLA RETURNS TRUE FOR HASCHILDNODES EVEN IF A SPACE IS PRESENT B/N START
                                                                    AND END TAG :(
                                   -->
							<div id="historyData<%=index%>" style="overflow: hidden;"></div>
						</td>
					</tr>
					<%index++;%>
				</logic:iterate>

				<!-- a change has done for coiv2 in the next line for setting new coi home -->
				<tr class="theaderBlue">
					<td align="right" colspan="5"><a
						href='<%=path%>/coiHome.do?Menu_Id=004'>Back to COI Home</a></td>
				</tr>
			</logic:notEqual>

			<logic:equal name="totentity" value='0'>
				<tr>
					<td colspan="3">There is no finanacial entities</td>
				</tr>
			</logic:equal>
		</table>
	</logic:present>
	<logic:notPresent name="perFinEnt" scope="session">
		<table width="986" align="center" border="0" cellpadding="0"
			cellspacing="0" class="table">
			<tr>
				<td colspan="3">There is no finanacial entities</td>
			</tr>
		</table>

	</logic:notPresent>


	<%
if(request.getAttribute("FESubmitSuccess") != null){
        request.removeAttribute("FESubmitSuccess");
        
        String entityName = (String)request.getAttribute("entityName");
        String actionType = (String)request.getAttribute("actionType");
        String reloadLocation = request.getContextPath()+"/listAnnFinEntity.do";
       String key="";
        //messageResources.getMessage("AnnualDisclosure.FinancialEntity.Add");
      if(actionType != null){
            if(actionType.equals("I")){
               key="AnnualDisclosure.FinancialEntity.Add";
            } else if(actionType.equals("U") ){
                key="AnnualDisclosure.FinancialEntity.Edit";
            } else if(actionType.equals("activate") ){
                 key="AnnualDisclosure.FinancialEntity.Active";
            } else if(actionType.equals("deactivate") ){
                 key="AnnualDisclosure.FinancialEntity.Inactive";
            }
        }
    /*    int indexOfApost = message.indexOf("'");
        if(indexOfApost != -1){
            int indexOfApostIncr = indexOfApost+1;
            int endOfMessage = message.length();
            String message1 = message.substring(0, indexOfApost);
            String message2 = message.substring(indexOfApostIncr, message.length());
            message = message1+message2;
        }
        out.print("<script language='javascript'>");
        out.print("alert('"+message+"');");
//out.print("openwintext('FESubmitSuccess.jsp', 'getFinEnt3');");//can't use because of popup blockers
        out.print("window.location='"+reloadLocation+"';");
        out.print("</script>");*/%>
	<script language='javascript'>
            alert('The Entity "<bean:message bundle="coi" key="AnnualDisclosure.FinancialEntity.Entity" arg0='<%=entityName%>'/>" <bean:message bundle="coi" key='<%=key%>'/>');
            window.location='<%=reloadLocation%>';
        </script>
	<%}

%>



</body>
</html>
