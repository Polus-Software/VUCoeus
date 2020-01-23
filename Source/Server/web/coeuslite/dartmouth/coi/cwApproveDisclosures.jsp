<%@ page
	import="org.apache.struts.validator.DynaValidatorForm,java.sql.*,edu.dartmouth.coeuslite.coi.beans.DisclosureBean"%>

<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@page errorPage="ErrorPage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="noConflicts" scope="session" class="java.util.Vector" />
<bean:size id="vectSize" name="noConflicts" />
<jsp:useBean id="potentialConflicts" scope="session"
	class="java.util.Vector" />
<bean:size id="vectSize1" name="potentialConflicts" />
<jsp:useBean id="person" scope="session"
	class="edu.mit.coeus.bean.PersonInfoBean" />


<html:html>
<head>
<title>COI:Approve Disclosure</title>

<script language='JavaScript'>

           function open_person_search(link)
             {
                 validFlag = "proposal";    
                 var winleft = (screen.width - 650) / 2;
                 var winUp = (screen.height - 450) / 2;  
                 var win = "scrollbars=1,resizable=1,width=790,height=450,left="+winleft+",top="+winUp
                 sList = window.open('<bean:write name='ctxtPath'/>'+"/"+link, "list", win);
             }
     
             function fetch_Data(result)
             {       
                document.coiDisclosure.fullName.value = result["FULL_NAME"] ;         
             }
             
             function confirmation(name){
   return confirm("Are you sure you want to approve all annual disclosures of "+name);   
    }

        </script>

<%String path = request.getContextPath();%>
<script src="<%=path%>/coeuslite/mit/utils/scripts/divSlide.js"></script>
<script src="<%=path%>/coeuslite/mit/utils/scripts/Balloon.js"></script>
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
	function openFinEntDet(personId,fullName) {
                var url1 = "<%=path%>/getPIFinEntSummary.do?personId="+personId+"&&fullName="+fullName;
                window.open(url1, "History", "scrollbars=1,resizable=yes, width=1000, height=700, left=100, top = 100");
            }	
       <%-- 4447 function openProjects(personId,fullName) {
                var url1 = "<%=path%>/getPIProjects.do?personId="+personId+"&&fullName="+fullName;
                window.open(url1, "History", "scrollbars=1,resizable=yes,width=1000, height=700, left=100, top = 100");
            } --%>
        </script>
<html:base />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<script language="javascript" type="text/JavaScript"
	src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/sorttable.js"></script>
</head>
<body>

	<%--  <html:form action="/keyStudyPerson"  method="post" onsubmit="return validateForm(this)"> --%>
	<a name="top"></a>
	<%--  ************  START OF BODY TABLE  ************************--%>
	<%int disclIndex = 0;%>
	<table width="980" height="100%" border="0" cellpadding="0"
		cellspacing="0" class="table">

		<tr>
			<td height="20" align="left" valign="top" class="tableheader"><bean:message
					bundle="coi" key="coiAnnualDisclosure.header" /></td>
		</tr>

		<!--    <tr>
        <td class="copy">&nbsp;&nbsp;
            <bean:message bundle="coi" key="coiDisclosure.coiDiscInfo"/> 
        </td>
    </tr>
  -->


		<!-- COI Disclosure - Start  -->
		<tr>

			<td valign="top"><b><bean:message bundle="coi"
						key="coiAnnualDisclosure.nothingtoDisclose" /> </b>

				<table width="98%" border="0" align="center" cellpadding="2"
					cellspacing="0" id="t1" class="tabtable">

					<tr class="tableheader">
						<td width="2%" align="left" class="theader"></td>
						<td width="19%" align="left" class="theader"><bean:message
								bundle="coi" key="label.personName" /></td>
						<td width="19%" align="left" class="theader"><bean:message
								bundle="coi" key="label.disclosureStatus" /></td>
						<!-- 4447 <td width="10%" align="left" class="theader"><bean:message bundle="coi" key="label.project"/> </td> -->
						<td width="15%" align="left" class="theader"><bean:message
								bundle="coi" key="label.department" /></td>
						<td width="10%" align="left" class="theader"><bean:message
								bundle="coi" key="label.DiscDate" /></td>
						<td width="18%" align="left" class="theader"></td>

					</tr>


					<logic:present name="noConflicts" scope="session">
						<%  
                                                     String strBgColor = "#DCE5F1";
                                                   //  int size=reviewAnnualDisc.size();
                                                  //   System.out.println("VECTOR SIZE="+size);
                                                     
                                                    %>
						<logic:notEqual name='vectSize' value='0'>
							<logic:iterate id="discInfo" name="noConflicts"
								type="edu.dartmouth.coeuslite.coi.beans.DisclosureBean">
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
									onmouseout="className='rowLine'" valign="top">

									<td class="copy" valign="top">
										<%
                                                                    //String personId = person.getPersonID();
                                                                    /* String moduleCode = (String)data.get("moduleCode");
                                                                    String moduleItemKey = (String)data.get("moduleItemKey");
                                                                    String coiDisclosureNumber = (String)data.get("coiDisclosureNumber");
                                                                    String url = ""+path+"/getDisclosureHistory.do?moduleCode="+moduleCode+"&moduleItemKey="+moduleItemKey+"&disclosureNum="+coiDisclosureNumber;
                                                                    
                                                                    String onClick = "selectRow('row"+disclIndex+"', 'historyRow"+disclIndex+"', 'historyData"+disclIndex+"' , 'toggle"+disclIndex+"', 'historyFrame"+disclIndex+"', '"+url+"')";*/
                                                                    // String url=""+path+"/getPIAnnualDiscl.do?PersonId="+personId;
                                                                    String url=""+path+"/getPIFinEntStatus.do?personId="+personID+"&person="+fullName;
                                                                    String onClick="selectRow('row"+disclIndex+"', 'historyRow"+disclIndex+"', 'historyData"+disclIndex+"' , 'toggle"+disclIndex+"', 'historyFrame"+disclIndex+"', '"+url+"')";
                                                                    %> <script>
                                            var link = "javascript:<%=onClick%>";
                                            document.write("<a href=\""+link+"\" id=\"toggle<%=disclIndex%>\"> <img  vspace="0" src='<%=request.getContextPath()%>/coeusliteimages/plus.gif' border='0' id='imgtoggle<%=disclIndex%>' name='imgtoggle<%=disclIndex%>'> </a>");
                                                                    </script>
										<noscript>
											<a target="_blank" href="<%=url%>">&gt;&gt;</a>
										</noscript>
									</td>

									<td width="19%" class="copy" valign=top wrap colspan="0">
										<coeusUtils:formatOutput name="discInfo" property="fullName" />
									</td>
									<%String statusStr="";%>
									<logic:present name="DisclStatus" scope="session">
										<logic:iterate id="status" name="DisclStatus"
											type="edu.mit.coeuslite.utils.ComboBoxBean">
											<bean:define id="statuscode" name="status" property="code" />
											<logic:equal name='statuscode'
												value='<%=discInfo.getDisclosureStatusCode().toString()%>'>
												<bean:define id="statusDesc" name="status"
													property="description" />
												<%statusStr=(String)statusDesc;%>
											</logic:equal>
										</logic:iterate>
									</logic:present>
									<td width="19%" class="copy" valign=top wrap colspan="0">
										<%=statusStr%>
									</td>
									<!-- 4447 <td width="10%" class="copy" valign=top wrap colspan="0" >
                                                                    <a href="javascript:openProjects('<%=personID%>','<%=fullName%>')" >
                                                                        <bean:message bundle="coi" key="label.project"/>
                                                                    </a>
                                                                </td> -->
									<td width="15%" class="copy" valign=top wrap colspan="0">
										<coeusUtils:formatOutput name="discInfo" property="unitName" />
									</td>
									<td width="10%" class="copy" valign=top wrap colspan="0">
										<bean:define id="updTS" name="discInfo"
											property="updateTimestamp" /> <coeusUtils:formatOutput
											name="discInfo" property="updateTimestamp" />
									</td>

									<td width="18%" class="copy" valign=top wrap colspan="0">
										<a
										href="<%=path%>/approvePerDisc.do?personId=<%=personID%>&person=<%=fullName%>"
										onclick="return confirmation('<bean:write name="discInfo" property="fullName"/>');">Approve</a>
										<%--    <coeusUtils:formatDate name=ss"discInfo" property="updtimestamp" />
                                                  --%>
									</td>
								</tr>
								<tr id="historyRow<%=disclIndex%>" style="display: none"
									class="copy rowHover">
									<td colspan="7">
										<!--NOTE: DO NOT PUT THE DIV END TAG IN NEXT LINE NOT PUT EVEN A SINGLE SPACE
                                                                    MOZILLA RETURNS TRUE FOR HASCHILDNODES EVEN IF A SPACE IS PRESENT B/N START
                                                                    AND END TAG :(
                                                                    -->
										<div id="historyData<%=disclIndex%>" style="overflow: hidden;"></div>
									</td>
								</tr>
								<% disclIndex++ ;%>
							</logic:iterate>
						</logic:notEqual>
						<logic:equal name='vectSize' value='0'>
							<tr>
								<td colspan="7">
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
		<!-- COI Disclosure - End  -->
		<tr>

			<td valign="top"><b><bean:message bundle="coi"
						key="coiAnnualDisclosure.potentialConflict" /></b>

				<table width="98%" border="0" cellpadding="2" cellspacing="0"
					align="center" id="t2" class="tabtable">
					<tr class="tableheader">
						<td width="2%" align="left" class="theader"></td>
						<td width="19%" align="left" class="theader"><bean:message
								bundle="coi" key="label.personName" /></td>
						<td width="19%" align="left" class="theader"><bean:message
								bundle="coi" key="label.disclosureStatus" /></td>
						<!-- 4447 <td width="10%" align="left" class="theader"><bean:message bundle="coi" key="label.project"/> </td> -->
						<td width="15%" align="left" class="theader"><bean:message
								bundle="coi" key="label.department" /></td>
						<td width="10%" align="left" class="theader"><bean:message
								bundle="coi" key="label.DiscDate" /></td>
						<td width="18%" align="left" class="theader"></td>

					</tr>


					<logic:present name="potentialConflicts" scope="session">
						<% // disclIndex = 0;
                                                     String strBgColor = "#DCE5F1";

                                                    %>
						<logic:notEqual name='vectSize1' value='0'>
							<logic:iterate id="discInfo1" name="potentialConflicts"
								type="edu.dartmouth.coeuslite.coi.beans.DisclosureBean">
								<bean:define id="personId" name="discInfo1" property="personId" />
								<bean:define id="fullName" name="discInfo1" property="fullName" />

								<%--       <html:hidden  property="personId" value='<%=(String)personId%>'/>--%>

								<%                                  
                                                           // request.setAttribute("personId",personId);
                                                            if (disclIndex%2 == 0) {
                                                            strBgColor = "#D6DCE5"; 
                                                            }
                                                            else { 
                                                            strBgColor="#DCE5F1"; 
                                                            }
                                                            //request.setAttribute("personId",personId);
                                                           // String personId=discInfo1.getPersonId();
                                                            %>

								<%String statusStr="";%>
								<logic:present name="DisclStatus" scope="session">
									<logic:iterate id="disclStatus" name="DisclStatus"
										type="edu.mit.coeuslite.utils.ComboBoxBean">
										<bean:define id="disclStatusCode" name="disclStatus"
											property="code" />
										<logic:equal name='disclStatusCode'
											value='<%=discInfo1.getDisclosureStatusCode().toString()%>'>
											<bean:define id="disclStatusDesc" name="disclStatus"
												property="description" />
											<%statusStr=(String)disclStatusDesc;%>
										</logic:equal>
									</logic:iterate>
								</logic:present>
								<tr bgcolor='<%=strBgColor%>' id="row<%=disclIndex%>"
									class="rowLine" onmouseover="className='rowHover rowLine'"
									onmouseout="className='rowLine'">

									<td class="copy">
										<%
                                                                    
                                                                    /* String moduleCode = (String)data.get("moduleCode");
                                                                    String moduleItemKey = (String)data.get("moduleItemKey");
                                                                    String coiDisclosureNumber = (String)data.get("coiDisclosureNumber");
                                                                    String url = ""+path+"/getDisclosureHistory.do?moduleCode="+moduleCode+"&moduleItemKey="+moduleItemKey+"&disclosureNum="+coiDisclosureNumber;
                                                                    
                                                                    String onClick = "selectRow('row"+disclIndex+"', 'historyRow"+disclIndex+"', 'historyData"+disclIndex+"' , 'toggle"+disclIndex+"', 'historyFrame"+disclIndex+"', '"+url+"')";*/
                                                                    // String url=""+path+"/getPIAnnualDiscl.do?PersonId="+personId;
                                                                    String url=""+path+"/getPIFinEntStatus.do?personId="+personId+"&fullName="+fullName;
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
										<coeusUtils:formatOutput name="discInfo1" property="fullName" />
									</td>
									<td width="19%" class="copy" valign=top wrap colspan="0">
										<%=statusStr%>
									</td>
									<!-- <td width="10%" class="copy" valign=top wrap colspan="0" >
                                                                    <a href="javascript:openProjects('<%=personId%>','<%=fullName%>')" >
                                                                        <bean:message bundle="coi" key="label.project"/>
                                                                    </a>
                                                                </td> -->
									<td width="15%" class="copy" valign=top wrap colspan="0">
										<coeusUtils:formatOutput name="discInfo1" property="unitName" />
									</td>
									<td width="10%" class="copy" valign=top wrap colspan="0">
										<bean:define id="updtt" name="discInfo1"
											property="updateTimestamp" /> <coeusUtils:formatOutput
											name="discInfo1" property="updateTimestamp" />
									</td>
									<td width="15%" class="copy" valign=top wrap colspan="0">
										<%--  <a href="javascript:openFinEntDet('<%=personId%>','<%=fullName%>');">Financial Entity</a>--%>
										<%--<coeusUtils:formatOutput name="discInfo" property="title"/>--%>
									</td>
								</tr>
								<tr id="historyRow<%=disclIndex%>" style="display: none"
									class="copy rowHover">
									<td colspan="7">
										<!--NOTE: DO NOT PUT THE DIV END TAG IN NEXT LINE NOT PUT EVEN A SINGLE SPACE
                                                                    MOZILLA RETURNS TRUE FOR HASCHILDNODES EVEN IF A SPACE IS PRESENT B/N START
                                                                    AND END TAG :(
                                                                    -->
										<div id="historyData<%=disclIndex%>" style="overflow: hidden;"></div>
									</td>
								</tr>
								<% disclIndex++ ;%>
							</logic:iterate>
						</logic:notEqual>
						<logic:equal name='vectSize1' value='0'>
							<tr>
								<td colspan="7">
									<table width="95%" align="right" border="0">
										<tr>
											<td class="copybold" align='center'><bean:message
													bundle="coi" key="label.noDatawithFe" /></td>
										</tr>
									</table>
								</td>
							</tr>
						</logic:equal>
					</logic:present>

				</table></td>
		</tr>
		<tr>
			<td height='10'>&nbsp;</td>
		</tr>
	</table>

</body>
</html:html>
<!-- web54607.mail.re2.yahoo.com compressed/chunked Fri Sep 14 13:19:08 PDT 2007 -->
