<%--
    Document   : cwCoiV2RevFinEntity
    Created on : Sep 16, 2010, 5:47:35 PM
    Author     : New
--%>

<%@page errorPage="ErrorPage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="person" scope="session"
	class="edu.mit.coeus.bean.PersonInfoBean" />
<jsp:useBean id="perFinEnt" scope="session" class="java.util.Vector" />
<%@ page import="java.util.Vector"%>
<bean:size id="totentity" name="perFinEnt" />
<jsp:useBean id="dynaForm" scope="request"
	class="org.apache.struts.validator.DynaValidatorForm" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JSP Page</title>
<%String path = request.getContextPath();
        HttpSession sess=request.getSession();
        String mode=(String)sess.getAttribute("mode");
         %>
<style>
#mbox {
	background-color: #6e97cf;
	padding: 0px 8px 8px 8px;
	border: 3px solid #095796;
}

#mbm {
	font-family: sans-serif;
	font-weight: bold;
	float: right;
	padding-bottom: 5px;
}

#ol {
	background-image:
		url('../coeuslite/mit/utils/scripts/modal/overlay.png');
}

.dialog {
	display: none
}

* html #ol {
	background-image: none;
	filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src="../coeuslite/mit/utils/scripts/modal/overlay.png",
		sizingMethod="scale");
}
</style>
<style>
.deleteRow {
	font-weight: bold;
	color: #CC0000;
	background-color: white;
}

.addRow {
	font-weight: bold;
	background-color: white;
}

.rowHeight {
	height: 25px;
}
</style>
<script
	src="<%=request.getContextPath()%>/coeuslite/mit/utils/scripts/modal/modaldbox.js"></script>
<style>
.cltextbox-medium {
	width: 160px;
}

.cltextbox-color {
	width: 160px;
	font-weight: normal;
}

.textbox {
	width: 160px;
	font-weight: normal;
}

.cltextbox-nonEditcolor {
	width: 160px;
	font-weight: normal;
}
</style>
<link href="<%=path%>/coeuslite/dartmouth/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<script src="<%=path%>/coeuslite/dartmouth/utils/scripts/divSlide.js"></script>
<script src="<%=path%>/coeuslite/dartmouth/utils/scripts/Balloon.js"></script>
<script language="javascript">
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
        function AddNewFinancialEntity()
        {
            window.location= "<%=path%>/getAnnDisclFinEntityCoiv2.do?actionFrom=main&createNew=newEntity";
        }

        function MyCoiHome()
        {

          window.location= "<%=path%>/coi.do?Menu_Id=004";
        }
        function returnDisclosure(){
          window.location= "<%=path%>/coiCentralAction.do";
        }

        function showHistoryPopup(desc,divId) {
            var width  = 650;
            var height = 170;
            sm(divId,width,height);
            document.getElementById('TxtAreaComments').innerHTML=desc;
            document.getElementById("mbox").style.left="385";
            document.getElementById("mbox").style.top="250";          
       }


    </script>
<!--    COEUS 3424 STARTS-->
<script
	src="<%=request.getContextPath()%>/coeuslite/mit/utils/scripts/modal/modaldbox.js"></script>
<script type="text/javascript">
    function showDialog(txtAreaID,id)
 {
     var width  = 650;//document.getElementById('divTXTDetails').style.pixelWidth;
     var height = 170;//document.getElementById('divTXTDetails').style.pixelHeight;
     sm(id,width,height);
     var txtValue=document.getElementById(txtAreaID).value;

     document.getElementById('TxtAreaComments').innerHTML=txtValue;
     document.getElementById("mbox").style.left="385";//450
     document.getElementById("mbox").style.top="250";//250
 }

  function show(txtAreaID,id)
 {
     var width  = 650;//document.getElementById('divTXTDetails').style.pixelWidth;
     var height = 170;//document.getElementById('divTXTDetails').style.pixelHeight;
     sm(id,width,height);
     var txtValue=document.getElementById(txtAreaID).value;
    
     document.getElementById('TxtComments').innerHTML=txtValue;
     document.getElementById("mbox").style.left="380";//450
     document.getElementById("mbox").style.top="250";//250
     document.getElementById("mbox").style.position="fixed !important";
     document.getElementById("mbox").style.position="absolute";
 }

 function showUseMit(txtAreaID,id)
 {
     var width  = 650;//document.getElementById('divTXTDetails').style.pixelWidth;
     var height = 170;//document.getElementById('divTXTDetails').style.pixelHeight;
     sm(id,width,height);
     var txtValue=document.getElementById(txtAreaID).value;
    
     document.getElementById('TxtArea').innerHTML=txtValue;
     document.getElementById("mbox").style.left="380";//450
     document.getElementById("mbox").style.top="250";//250
      document.getElementById("mbox").style.position="fixed !important";
     document.getElementById("mbox").style.position="absolute";
 }
</script>
<!--    COEUS 3424 ENDS-->



</head>
<body>
	<logic:present name="perFinEnt" scope="session">
		<%  String strBgColor = "#DCE5F1";
                    int index = 0;%>
		<table width="95%" align="center" border="0" cellpadding="0"
			cellspacing="0" class="table">
			<tr class="theaderBlue">
				<td height="20" class="theader" style="font-size: 14px">Financial
					Entities for <%=person.getFullName()%></td>
			</tr>
			<tr class="copybold">
				<td height="30" style="padding-left: 10px;">Your current list
					of financial entities is displayed on this page.</td>
			</tr>
			<tr>
				<td height="25" style="padding-left: 10px;"><input
					type="button" name="CreateNew" value="Add Financial Entities"
					class="clsavebutton" style="width: 150px"
					onclick="javascript:AddNewFinancialEntity();" /></td>
			</tr>
			<tr>
				<td>
					<table width="98%" align="center" border="0" cellpadding="0"
						cellspacing="0" class="table">
						<tr class="theaderBlue" height="25px">
							<td width="2%">&nbsp;</td>
							<td align="left" class="theader" width="12%">Entity Name</td>
							<td align="left" class="theader" width="20%">Status</td>
							<td align="left" class="theader" width="25%">Entity Business
								Focus</td>
							<td align="left" class="theader" width="19%">Use of MIT
								Resources</td>
							<td align="left" class="theader" width="27%">Last Updated</td>
							<td align="left" colspan="2" class="theader">&nbsp;</td>
						</tr>
						<logic:notEqual name="totentity" value='0'>
							<logic:iterate id="finEntDet" name="perFinEnt"
								type="org.apache.commons.beanutils.DynaBean">
								<bean:define id="entityNumber" name="finEntDet"
									property="entityNumber" />
								<bean:define id="entityName" name="finEntDet"
									property="entityName" />
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

									<td width="10px" class="copy" wrap colspan="0">
										<%
        //    String entityNumber = (String)finEntDet.get("entityNumber");
            String url = ""+path+"/getAnnFinEntHistoryCoiv2.do?entityNumber="+entityNumber;

            String onClick = "selectRow('row"+index+"', 'historyRow"+index+"', 'historyData"+index+"' , 'toggle"+index+"', 'historyFrame"+index+"', '"+url+"')";
            //coeus -3424 starts
            String  relDesc=((finEntDet.get("relationShipDesc")==null)?"":(finEntDet.get("relationShipDesc").toString()));
            String relDescTmp=relDesc;
            if(relDesc.length()>50){relDesc=relDesc.substring(0,50);
            relDesc+= "<a href=\"javaScript:show('txtAreaCommentId"+index+"','businessFocus')\"> [...]</a>";
            }
            //coeus -3424 ends
%> <script>
                                            var link = "javascript:<%=onClick%>";
                                            document.write("<a href=\""+link+"\" id=\"toggle<%=index%>\"> <img src='<%=request.getContextPath()%>/coeusliteimages/plus.gif' border='0' id='imgtoggle<%=index%>' name='imgtoggle<%=index%>'> </a>");
            </script>
										<noscript>
											<a target="_blank" href="<%=url%>">&gt;&gt;</a>
										</noscript>
									</td>
									<td class="copy"><coeusUtils:formatOutput name="finEntDet"
											property="entityName" /></td>
									<td class="copy"><coeusUtils:formatOutput name="finEntDet"
											property="statusDesc" />&nbsp; [ <logic:equal
											name="finEntDet" property="statusCode" value='1'>
											<a
												href='<%=path%>/deactivateCoiAnnFinEnt.do?entityNumber=<%=entityNumber%>'>
												<%-- Commented for Case#4049 - "Remove" Entity label should be changed
                <bean:message bundle="coi" key="financialEntity.removeLink"/> --%>
												<bean:message bundle="coi"
													key="financialEntity.makeInactive" />
											</a>
										</logic:equal> <logic:notEqual name="finEntDet" property="statusCode"
											value='1'>
											<a
												href='<%=path%>/activateCoiAnnFinEnt.do?entityNumber=<%=entityNumber%>'>
												<bean:message bundle="coi"
													key="financialEntity.makeActiveLink" />
											</a>
										</logic:notEqual> ] [ <a
										href="<%=path%>/reviewAnnFinEntityCoiv2.do?entityNumber=<%=entityNumber%>">Edit&nbsp;</a>]</td>

									<td class="copy"><%=relDesc%></td>
									<%
        //    String entityNumber = (String)finEntDet.get("entityNumber");

            //coeus -3424 starts
            String mitResource=((finEntDet.get("resoucreMit")==null)?"":(finEntDet.get("resoucreMit").toString()));
            String mitResources=mitResource;
            if(mitResource.length()>40){mitResource=mitResource.substring(0,40);
            mitResource+= "<a href=\"javaScript:showUseMit('mituse"+index+"','useOfMit')\"> [...]</a>";
            }
            //coeus -3424 ends
%>
									<td class="copy"><%=mitResource%></td>
									<%--<coeusUtils:formatOutput name="finEntDet" property="resoucreMit"/></td>--%>
									<td class="copy"><coeusUtils:formatDate name="finEntDet"
											property="updtimestamp" formatString="yyyy-MM-dd  hh:mm a" />
										by <coeusUtils:formatOutput name="finEntDet"
											property="upduser" /></td>
									<td class="copy"><input type="hidden" name="entityName"
										value="<%=entityName%>" /> <input type="hidden"
										name="actionFrom" value="coiAnnFin" /> <!--            coeus -3424 starts-->
										<input id="txtAreaCommentId<%=index%>" name="finEntDet"
										type="hidden" value="<%=relDescTmp%>" /> <!--            coeus -3424 ends-->
										&nbsp;&nbsp;</td>
									<td class="copy"><input style="display: none;"
										name="entityName" value="<%=entityName%>" /> <input
										style="display: none;" name="actionFrom" value="coiAnnFin" />
										<!--            coeus -3424 starts--> <input
										id="mituse<%=index%>" name="finEntDet" style="display: none;"
										value="<%=mitResources%>" /> <!--            coeus -3424 ends-->
										&nbsp;&nbsp;</td>
								</tr>
								<tr id="historyRow<%=index%>" style="display: none"
									class="copy rowHover">
									<td colspan="6">
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
							<%--<tr class="theaderBlue"> <td align="right" colspan="5"><a href='<%=path%>/coiHome.do?Menu_Id=004'>Back to COI Home</a></td></tr>--%>
						</logic:notEqual>
						<logic:equal name="totentity" value='0'>
							<tr>
								<td colspan="6">There is no finanacial entities</td>
							</tr>
						</logic:equal>
					</table>
				</td>
			</tr>
			<tr>
				<td height="25" style="padding-left: 10px;"><input
					type="submit" name="mycoihome" value="My COI Home"
					class="clsavebutton" onclick="javascript:MyCoiHome();" /> <% if(request.getSession().getAttribute("toFEPage")!= null){ %>
					<input type="submit" style="width: 180px;"
					value="Continue with Disclosure" class="clsavebutton"
					onclick="javascript:returnDisclosure();" /> <% } %></td>
			</tr>
		</table>

	</logic:present>
	<logic:notPresent name="perFinEnt" scope="session">
		<table width="95%" align="center" border="0" cellpadding="0"
			cellspacing="0" class="table">
			<tr>
				<td>There is no finanacial entities</td>
			</tr>
		</table>

	</logic:notPresent>
	<div id="businessFocus" class="dialog"
		style="width: 200px; overflow: hidden;">
		<table width="100%" border="0" cellpadding="1" cellspacing="1"
			class="table">
			<tr style="background-color: #6E97CF; font-size: 12px; margin: 0;">
				<td style="padding: 2px 0px 5px 0px"><font color="#333333"
					size="2px"><b>Entity Business Focus</b></font></td>
			</tr>
			<tr style="height: 100px; width: 200px;">

				<td align="left" style="height: 100px;">
					<div
						style="height: 100px; width: 650px; overflow-x: scroll; overflow-y: scroll; overflow: auto;">
						<label id="TxtComments"> </label>
					</div>
				</td>
			</tr>
			<tr style="background-color: #6E97CF;">
				<td align="center"><input type="button" value="Close"
					class="clsavebutton" onclick="hm('businessFocus');" /></td>

			</tr>
		</table>
	</div>
	<div id="useOfMit" class="dialog"
		style="width: 200px; overflow: hidden; position: absolute;">
		<table width="100%" border="0" cellpadding="1" cellspacing="1"
			class="table">
			<tr style="background-color: #6E97CF; font-size: 12px; margin: 0;">
				<td style="padding: 2px 0px 5px 0px"><font color="#333333"
					size="2px"><b>Use of MIT resources</b></font></td>
			</tr>
			<tr style="height: 100px; width: 200px;">
				<td align="left" style="height: 100px;">
					<div
						style="height: 100px; width: 650px; overflow-x: scroll; overflow-y: scroll; overflow: auto;">
						<label id="TxtArea"></label>
					</div>
				</td>
			</tr>
			<tr style="background-color: #6E97CF;">
				<td align="center"><input type="button" value="Close"
					class="clsavebutton" onclick="hm('useOfMit');" /></td>

			</tr>
		</table>
	</div>

	<div id="entityBusinessFocus" class="dialog"
		style="width: auto; overflow: hidden; position: absolute;">
		<table width="100%" border="0" cellpadding="1" cellspacing="1"
			class="table">
			<tr style="background-color: #6E97CF; font-size: 12px; margin: 0;">
				<td style="padding: 2px 0px 5px 0px"><font color="#333333"
					size="2px"><b>Entity Business Focus</b></font></td>
			</tr>
			<tr style="height: 100px; width: 200px;">
				<%-- <td align="center"> <textarea id="TxtAreaComments" cols="3" style=" height: 70px;resize:none; width: 430px;" disabled ></textarea></td>--%>
				<td align="left" style="height: 100px;">
					<div
						style="height: 100px; width: 650px; overflow-x: scroll; overflow-y: scroll; overflow: auto;">
						<label id="TxtAreaComments"></label>
					</div>
				</td>
			</tr>
			<tr style="background-color: #6E97CF;">
				<td align="center"><input type="button" value="Close"
					class="clsavebutton" onclick="hm('entityBusinessFocus');" /></td>

			</tr>
		</table>
	</div>

	<div id="divMITRcs" class="dialog"
		style="width: auto; overflow: hidden; position: absolute;">
		<table width="100%" border="0" cellpadding="1" cellspacing="1"
			class="table">
			<tr style="background-color: #6E97CF; font-size: 12px; margin: 0;">
				<td style="padding: 2px 0px 5px 0px"><font color="#333333"
					size="2px"><b>Use of MIT resources</b></font></td>
			</tr>
			<tr style="height: 100px; width: 200px;">
				<%-- <td align="center"> <textarea id="TxtAreaComments" cols="3" style=" height: 70px;resize:none; width: 430px;" disabled ></textarea></td>--%>
				<td align="left" style="height: 100px;">
					<div
						style="height: 100px; width: 650px; overflow-x: scroll; overflow-y: scroll; overflow: auto;">
						<label id="TxtAreaComments"></label>
					</div>
				</td>
			</tr>
			<tr style="background-color: #6E97CF;">
				<td align="center"><input type="button" value="Close"
					class="clsavebutton" onclick="hm('divMITRcs');" /></td>

			</tr>
		</table>
	</div>
	<!--    COEUS 3424 ENDS-->

	<%
if(request.getAttribute("FESubmitSuccess") != null){
        request.removeAttribute("FESubmitSuccess");

         String entityName = (String)request.getAttribute("entityName");
        String actionType = (String)request.getAttribute("actionType");
        String reloadLocation = request.getContextPath()+"/listAnnFinEntityCoiv2.do";
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
