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
<jsp:useBean id="disclosurePerson" scope="request"
	class="java.lang.String" />
<%--<jsp:useBean id="CurrDisclPer"  scope="session" class="edu.dartmouth.coeuslite.coi.beans.DisclosureBean" />--%>
<%--<jsp:useBean id="FinDiscl" scope="session" class="java.util.Vector" />
<bean:size  id="disclSize" name="FinDiscl"/>--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<%String path = request.getContextPath();
        String policyPath=CoeusProperties.getProperty(CoeusPropertyKeys.POLICY_FILE);
        String disclType = request.getParameter("COI_DISCL_TYPE");
        %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>COI</title>
<link href="<%=path%>/coeuslite/dartmouth/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<script src="<%=path%>/coeuslite/dartmouth/utils/scripts/divSlide.js"></script>
<script src="<%=path%>/coeuslite/dartmouth/utils/scripts/Balloon.js"></script>
<script>
        var lastPopup;
            function showPopUp(linkId, frameId, width, height, src, bookmarkId) {
                showBalloon(linkId, frameId, width, height, src, bookmarkId);
            }
			  var lastRowId = -1;
        var lastDataRowId;
        var lastDivId;
        var lastLinkId;
        var expandedRow = -1;

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
		<tr>
			<td>
				<table width="100%" border="0" cellpadding="0" cellspacing="0"
					class="table" align="center">
					<tr>
						<td height="15" align="center" valign="center" class="header2"><bean:message
								bundle="coi" key="coiMain.titleSchool" /></td>
					</tr>
					<tr>
						<td height="15" align="center" valign="center" class="header2">for</td>
					</tr>
					<tr>
						<td height="15" align="center" valign="center" class="header2"><bean:message
								bundle="coi" key="coiMain.titleInvestigators" /></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td valign="top">
				<table align="center" width='100%' border="0" cellpadding="0"
					cellspacing="0" class="table">
					<tr>
						<td height="20" class="theader" style="font-size: 14px"><bean:message
								bundle="coi" key="coiMain.headerDiscl" /> <%=disclosurePerson%>
						</td>
						<td align="right" class="theaderBlue" b><a
							href='<%=policyPath%>' target="_blank" style="font-size: 16px"><bean:message
									bundle="coi" key="coiMain.purpose" />&nbsp;&nbsp;</a></td>
					</tr>
					<tr>
						<td height="2px" colspan='2'></td>
					</tr>
					<%String oldDiscl="";
                   String newDiscl="";
                   String tgtNew="#";
                   String tgtUpdate="#";
                   String tgtView="#";%>
					<logic:present name="CurrDisclPer" scope="session">

						<%DisclosureBean discl=(DisclosureBean)session.getAttribute("CurrDisclPer");                                                                            
                   String statusStr="";
                   newDiscl="disabled";
                   tgtUpdate= path+"/createDisclosure.do?operation=UPDATE";
                   //Modified for Case#4447 : Next phase of COI enhancements - Start
                   //tgtView=path+"/getCompleteDiscl.do?acType=V";  
                   String disclosureType = (String)request.getParameter("COI_DISCL_TYPE");
                   if(disclosureType!=null && disclosureType.equals("disclosureSearch")){
                        tgtView=path+"/getCompleteDiscl.do?acType=V&COI_DISCL_TYPE=disclosureSearch";       
                   }else{
                       tgtView=path+"/getCompleteDiscl.do?acType=V";       
                   }
                   //Case#4447 - End
                   //int value=discl.getDisclosureStatusCode();
                   %>
						<logic:present name="DisclStatus" scope="session">
							<logic:iterate id="status" name="DisclStatus"
								type="edu.mit.coeuslite.utils.ComboBoxBean">
								<bean:define id="statuscode" name="status" property="code" />
								<logic:equal name='statuscode'
									value='<%=discl.getDisclosureStatusCode().toString()%>'>
									<bean:define id="statusDesc" name="status"
										property="description" />
									<%statusStr=(String)statusDesc;%>
								</logic:equal>
							</logic:iterate>
						</logic:present>

						<%--  <logic:notEqual name="disclSize" value='0'>--%>
						<tr>
							<td colspan='2'>
								<table align="center" width='100%' border="0" cellpadding="0"
									cellspacing="0" class="table">
									<tr class="theader" height="20px">
										<td width="15px">&nbsp;</td>
										<td width="181" align="left" class="theader">Disclosure
											Number</td>
										<td width="181" height="20" align="left" class="theader">Status</td>
										<td align="left" class="theader" width="183">Last Updated
										</td>

										<td align="left" class="theader" width="614">Expiration
											Date</td>
									</tr>
									<%--  <logic:iterate id="discl" name="FinDiscl" type="edu.dartmouth.coeuslite.coi.beans.DisclosureBean" >--%>
									<bean:define id="dtUpdate" name="CurrDisclPer"
										property="updateTimestamp" type="java.sql.Date" />
									<%long time=dtUpdate.getTime();
                               Calendar cal=Calendar.getInstance();
                               cal.setTimeInMillis(time);
                               DateUtils dtUtils=new DateUtils();
                               String dtStr=dtUtils.formatCalendar(cal,"MM/dd/yyyy hh:mm:ss a");%>
									<!-- JM 5-27-2011 updated color per 4.4.2 -->
									<tr bgcolor="#CCCCCC" bordercolor="#999999" height="20px"
										id="row0">
										<%if(disclType != null && disclType.equals("disclosureSearch")) {%>
										<td><a
											href="javascript:selectRow('row0','historyRow0','historyData0','toggle0','historyFrame0','<%=path%>/coiMain.do?history=history&#38;COI_DISCL_TYPE=disclosureSearch')"
											id="toggle0"><img
												src='<%=request.getContextPath()%>/coeusliteimages/plus.gif'
												border='none' id="imgtoggle0" name="imgtoggle0"
												border="none"></a></td>
										<%}else{%>
										<td><a
											href="javascript:selectRow('row0','historyRow0','historyData0','toggle0','historyFrame0','<%=path%>/coiMain.do?history=history')"
											id="toggle0"><img
												src='<%=request.getContextPath()%>/coeusliteimages/plus.gif'
												border='none' id="imgtoggle0" name="imgtoggle0"
												border="none"></a></td>
										<%}%>
										<td width='181px' align="left"><%=discl.getCoiDisclosureNumber()%></td>
										<td width="181" align="left"><%=statusStr%></td>
										<td align="left" width="183"><%=dtStr%></td>
										<td align="left" width="614"><coeusUtils:formatDate
												name="CurrDisclPer" property="expirationDate" /></td>
									</tr>
									<tr id="historyRow0" style="display: none"
										class="copy rowHover">
										<td colspan="5">
											<!--NOTE: DO NOT PUT THE DIV END TAG IN NEXT LINE NOT PUT EVEN A SINGLE SPACE
                                                                    MOZILLA RETURNS TRUE FOR HASCHILDNODES EVEN IF A SPACE IS PRESENT B/N START
                                                                    AND END TAG :(
                                   -->
											<div id="historyData0" style="overflow: hidden;"></div>
										</td>
									</tr>
									<%--      </logic:iterate>                     --%>
									<%--       <tr><td height="20" colspan="5"><table width="100%"  border="0" cellpadding="0" cellspacing="0"  class="table">
                                   <tr>
                                       <td height="20" align="right" class="theaderBlue"><a href='<%=path%>/createDisclosure.do?operation=UPDATE'>Update Annual Disclosure</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                                       <td align="left" class="theaderBlue">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='<%=path%>/getCompleteDiscl.do?acType=V'>View Current Disclosure</a></td>
                                   </tr>
                       </table></td></tr>--%>
								</table>
							</td>
						</tr>



					</logic:present>
					<logic:notPresent name="CurrDisclPer" scope="session">
						<%          oldDiscl="disabled";
                        tgtNew= path+"/getCompleteDiscl.do";
                        %>

						<tr>
							<td height="28" class="copybold" colspan="2"><bean:message
									bundle="coi" key="coiMain.noDiscosure" /> <%--<a href='<%=path%>/getCompleteDiscl.do'>Complete Annual Disclosure </a>--%></td>
						</tr>
					</logic:notPresent>
				</table>
			</td>
		</tr>
		<tr>
			<td colspan="2" valign="top" height="595px">
				<table width="100%" border="0" align="center" cellpadding="0"
					cellspacing="0" class="table">
					<tr align="left">
						<td height="250px" valign="top">&nbsp;&nbsp;
							<li style="font-size: 16px"><b>Disclosure<font
									color="#990000"></font></b></li> <br> <%
String styleNew="";
String styleOld="";
String styleView="";
String styleSearch="";
boolean isDisclosureSearch = false;
if(newDiscl.equals("disabled")){
//JM 5-27-2011 updated color per 4.4.2
 styleNew="#747474";                           
}
else{
 styleNew="";                           
}        


if(oldDiscl.equals("disabled")){
//JM 5-27-2011 updated color per 4.4.2
 styleOld="#747474";                           
 styleView="D3D3D3";
}
else{
 styleOld="";                           
}    
//Added Case#4447 : FS_Next phase of COI enhancements - Start
String disclosure = (String)session.getAttribute("disclosure");
String searchDisclosure = "searchDisclosure";
if(searchDisclosure.equalsIgnoreCase(disclosure) || (disclType != null && disclType.equals("disclosureSearch"))){
    styleOld="D3D3D3";        
    styleSearch="D3D3D3";
    styleView="";
    isDisclosureSearch = true;
}
//Case#4447 - End
%>
							<table>
								<tr>
									<td width="50px" valign="top">&nbsp;</td>
									<td><logic:notPresent name="CurrDisclPer" scope="session">
											<a href='<%=tgtNew%>' <%=newDiscl%>>
										</logic:notPresent> <font color="<%=styleNew%>"
										style="font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold">Create
											New</font> <logic:notPresent name="CurrDisclPer" scope="session">
											</a>
										</logic:notPresent></td>
								</tr>
								<%if(isDisclosureSearch){%>
								<tr>
									<td width="50px" valign="top">&nbsp;</td>
									<td><font color="<%=styleNew%>"
										style="font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold">Update</font></td>
								</tr>
								<%}else{%>
								<tr>
									<td width="50px" valign="top">&nbsp;</td>
									<td><a href='<%=tgtUpdate%>' <%=oldDiscl%>><font
											color="<%=styleOld%>">Update</font></a></td>
								</tr>
								<%}%>
								<tr>
									<td width="50px" valign="top">&nbsp;</td>
									<td><a href='<%=tgtView%>' <%=oldDiscl%>><font
											color="<%=styleView%>">View</font></a></td>
								</tr>
							</table> &nbsp;&nbsp;
							<li style="font-size: 16px"><b><bean:message
										bundle="coi" key="coiMain.FinancialEntities" /><font
									color="#990000"><a id="linkFinEnt"
										href="javascript:showBalloon('linkFinEnt', 'FinEntHelp')"><img
											src='<%=path%>/coeusliteimages/help.jpg' border="none"
											height="15" width="15" alt="help"></a></font></b></li> <br>
							<table>
								<%if(isDisclosureSearch){%>
								<tr>
									<td width="50px" valign="top">&nbsp;</td>
									<td><font color="<%=styleNew%>"
										style="font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold">Create
											New</font></td>
								</tr>
								<tr>
									<td width="50px" valign="top">&nbsp;</td>
									<td><font color="<%=styleNew%>"
										style="font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold">Review/Update</font></td>
								</tr>
								<%}else{%>
								<tr>
									<td width="50px" valign="top">&nbsp;</td>
									<td><a
										href='<%=path%>/getAnnDisclFinEntity.do?actionFrom=main'><font
											color="<%=styleSearch%>">Create New</font></a></td>
								</tr>
								<tr>
									<td width="50px" valign="top">&nbsp;</td>
									<td><a href='<%=path%>/listAnnFinEntity.do'><font
											color="<%=styleSearch%>">Review/Update</font></a></td>
								</tr>
								<%}%>

							</table> <%--
&nbsp;&nbsp;<li style="font-size:16px"><b><bean:message bundle="coi" key="coiMain.proposals"/></b></li><br>
<table >
<tr><td width="50px" valign="top">&nbsp;</td><td><a href="#">Create New</a></td></tr>
<tr><td width="50px" valign="top">&nbsp;</td><td><a href="#">Review/Update</a></td></tr>
</table>--%>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	<div id="FinEntHelp"
		style="display: none; overflow: auto; position: absolute; width: 400; height: 250"
		onClick="hideBalloon('FinEntHelp')">
		<div
			style="overflow: auto; position: absolute; width: 400; height: 250">
			<table width="100%" height="100%" border="0" cellpadding="2"
				cellspacing="0" class="lineBorderWhiteBackgrnd">
				<tr valign="top">
					<td></td>
					<td align="right"><a
						href="javascript:hideBalloon('FinEntHelp')"><img border="0"
							src='<%=path%>/coeusliteimages/none.gif'></a></td>
				</tr>
				<tr>
					<td colspan="2" valign="top">
						<p>
							&nbsp;A company or public or non-profit organization from which
							you have received in the last 12 months, or expect to receive in
							the next 12 months, any financial remuneration exceeding $10,000
							(aggregate for yourself, your spouse, and your dependent
							children) for any of the following:<br>
						<li>Salary, director's fees, consulting payments, honoraria,
							royalties</li>
						<li>Payments for patents, copyrights or other intellectual
							property</li>
						<li>Other financial compensation</li>
					<br> <b>OR</b><br> &nbsp;any company in which you have
						significant monetary interests/value either in excess of $10,000
						in fair market value or in excess of 5 percent ownership
						(aggregate yourself, your spouse, and your dependent children)
						including any of the following: <br>
						<li>Stock or stock options</li>
						<li>monetary value</li>
						</p>
					</td>
				</tr>
			</table>
		</div>
	</div>
	<%
if(request.getAttribute("Certification") != null){
        request.removeAttribute("Certification");
        
        String message = "Your Annual COI Certification is now complete. Thank you!";
        String reloadLocation = request.getContextPath()+"/coiMain.do";        
        int indexOfApost = message.indexOf("'");
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
        out.print("</script>");
}

%>
	<%
if(request.getAttribute("DisclosureExit")!= null){
        request.removeAttribute("DisclosureExit");        
        String message = "Your information has been saved and you may return any time to complete this disclosure.";
          String reloadLocation = request.getContextPath()+"/coiMain.do";                
        int indexOfApost = message.indexOf("'");
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
        out.print("</script>");
}

%>
</body>
</html>
