<%@page contentType="text/html"
	import="edu.mit.coeus.utils.CoeusProperties,edu.mit.coeus.utils.CoeusPropertyKeys,java.util.ArrayList,java.util.List,
org.apache.struts.validator.DynaValidatorForm,
java.util.Vector,
java.lang.Integer,
java.util.HashMap,
edu.mit.coeus.utils.DateUtils,
java.util.Calendar,
edu.dartmouth.coeuslite.coi.beans.DisclosureBean,
org.apache.struts.action.DynaActionForm,
java.util.List,edu.mit.coeuslite.utils.CoeusDynaBeansList"%>
<%@page pageEncoding="UTF-8" import="edu.mit.coeus.bean.*"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<jsp:useBean id="person" scope="session"
	class="edu.mit.coeus.bean.PersonInfoBean" />
<jsp:useBean id="PIFinEnt" scope="session" class="java.util.Vector" />
<bean:size id="totentity" name="PIFinEnt" />
<jsp:useBean id="coiStatus" scope="session" class="java.util.Vector" />
<bean:size id="totStat" name="coiStatus" />
<jsp:useBean id="disclosureStatus" scope="session"
	class="java.util.Vector" />
<bean:size id="totDisclStat" name="disclosureStatus" />
<html>
<head>
<%String path = request.getContextPath();
        String policyPath=CoeusProperties.getProperty(CoeusPropertyKeys.POLICY_FILE);
        String statusStr="";
         String fullName=(String)request.getAttribute("FullName");;
        %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>COI Update Financial Entity Conflict Status</title>
<link href="<%=path%>/coeuslite/dartmouth/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<script src="<%=path%>/coeuslite/dartmouth/utils/scripts/divSlide.js"></script>
<script src="<%=path%>/coeuslite/dartmouth/utils/scripts/Balloon.js"></script>
<script>
function changeAllStatus(){
var conflictStatus = document.getElementById("conflictStatus").value;

for(var elementIndex=0; elementIndex < document.statusfrm.elements.length; elementIndex++){
var elementName = document.forms[0].elements[elementIndex].name;
			if( (elementName.indexOf("slctConflictStatus") != -1)
				|| (elementName.indexOf("disclConflictStatus") != -1) ){
				document.forms[0].elements[elementIndex].value=conflictStatus;
                                }
			
}

}
<%--//var conflictStatus = document.forms[0].conflictStatus.value;
	/*for(var elementIndex=0; elementIndex < document.forms[0].elements.length; elementIndex++){
			var elementName = document.forms[0].elements[elementIndex].name;
			if( (elementName.indexOf("slctConflictStatus") != -1)
				|| (elementName.indexOf("disclConflictStatus") != -1) ){
				document.forms[0].elements[elementIndex].value=conflictStatus;*/
                                alert("value");
			}
                        --%>
function openFinEntity(entityNumber,sequenceNum) {
var url = "<%=path%>/reviewAnnFinEntityHist.do?entityNumber="+entityNumber+"&seqNum="+sequenceNum+"&header=no";
window.open(url, "History", "scrollbars=1,resizable=yes, width=1000, height=700, left=100, top = 100");
}
        <%--Case Added for Case#4447 : FS_Next phase of COI enhancements - Start--%>
        var defaultStyle = "rowLine";
        var selectStyle = "rowHover";
        
        function selectRow(rowId, dataRowId, divId, linkId, projectFrame, url) {
            var style = document.getElementById(dataRowId).style.display;
            var toggleText;
            if(style == ""){
                 style = "none";
                 styleClass = defaultStyle;
                 toggleText = plus.src;
                ds = new DivSlider();
                if(style == "") {
                    document.getElementById(dataRowId).style.display = "";
                    ds.showDiv(divId);
                }else {
                     ds.hideDiv(divId, "document.getElementById('"+dataRowId+"').style.display='none'");
                }
                var imgId = "img"+linkId;
                document.getElementById(imgId).src=toggleText;
            }else {
                document.getElementById(dataRowId).style.display = "";
                var divRef = document.getElementById(divId);
        	if(!divRef.hasChildNodes()) {
                    addFrame(divId, projectFrame, url);
                }
                
                projectFrame = document.getElementById(projectFrame);
                var frameSrc = projectFrame.getAttribute("src");
                if(frameSrc == null || frameSrc == "") {
                    projectFrame.setAttribute("src", url);
                }
                ds2 = new DivSlider();
                ds2.showDiv(divId);
                var imgId = "img"+linkId;
                toggleText = minus.src;
                document.getElementById(imgId).src=minus.src;
            }

        }
        
        
        var plus = new Image();
        var minus = new Image();
        
        function preLoadImages() {
            plus.src = "<%=path%>/coeusliteimages/plus.gif";
            minus.src = "<%=path%>/coeusliteimages/minus.gif";
        }
        
         function addFrame(divId, projectFrame, url) {
            iframeElem = document.createElement("IFRAME");
            document.createAttribute("src");
            iframeElem.setAttribute("src", url);
            iframeElem.setAttribute("id", projectFrame);
            iframeElem.setAttribute("width", "100%");
            iframeElem.setAttribute("scrolling", "auto");
            iframeElem.setAttribute("marginHeight", "0");
            iframeElem.setAttribute("marginWidth", "0");
            iframeElem.setAttribute("frameBorder", "0");
            var divRef = document.getElementById(divId);
            divRef.appendChild(iframeElem);
        }
        
        preLoadImages();   
      
        function submitPage(){
            document.statusfrm.action="<%=path%>/updPerConflictStatus.do";
            document.statusfrm.submit();
            alert("Conflict Status was updated on Financial Entities associated with "+'<%=fullName%>'+".");
            self.close();
        }
        <%--Case 4447 - end--%>     
        </script>
</head>
<body>
	<form name="statusfrm" method="post">
		<table align="center" width='100%' border="0" cellpadding="3"
			cellspacing="0" class="tabtable">
			<%
                String slctPerson="";
                //String fullName="";
                if(request.getAttribute("SelectedPerson")!=null){
                slctPerson=(String)request.getAttribute("SelectedPerson");
                //fullName=(String)request.getAttribute("FullName");
                request.setAttribute("FullName",fullName);
                }%>
			<input type="hidden" name="FullName" value=<%=fullName%> />
			<tr class="theaderBlue" height="10%">
				<td align="right" colspan="5"><a href="javascript:self.close()"><u>Close</u></a></td>
			</tr>
			<!--Addded for Case#4447 : Next phase of COI enhancements - Start-->
			<tr>
				<td>
					<table align="center" width="100%" class="table">
						<tr>
							<td colspan="5" class="theader">Update Disclosure status</td>
						</tr>
						<tr>
							<td colspan="0" class='copy' style='' bgcolor="#D1E5FF">
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Set disclosure status to: <select
								name="disclStatus" styleClass="textbox-long">
									<option value='0'>--Please Select--</option>
									<logic:present name="disclosureStatus" scope="session">
										<logic:iterate id="disclStatus" name="disclosureStatus"
											type="edu.mit.coeuslite.utils.ComboBoxBean">
											<option value='<%=disclStatus.getCode()%>'><coeusUtils:formatOutput
													name="disclStatus" property="description" /></option>
										</logic:iterate>
									</logic:present>
							</td>
						</tr>
						</tr>
						<!--Case#4447 - End-->
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table align="center" width="100%" class="tabtable"
						bgcolor="#D1E5FF">
						<tr>
							<td colspan="5" class="theader">List of Financial Entities
								for <%=fullName%></td>
						</tr>
						<logic:present name="PIFinEnt" scope="session">
							<% String strBgColor = "#DCE5F1";
                    int disclIndex = 0;
                    String selected="";%>


							<logic:notEqual name="totentity" value='0'>
								<tr>
									<td colspan="5" class='copy' style='' bgcolor="#D1E5FF">
										&nbsp;&nbsp;<bean:message bundle="coi"
											key="label.setConflictStatus" /> <select id="conflictStatus"
										styleClass="textbox-long" onchange="changeAllStatus();">
											<option>--Please Select--</option>
											<logic:present name="coiStatus" scope="session">
												<logic:iterate id="status" name="coiStatus"
													type="edu.mit.coeuslite.utils.ComboBoxBean">
													<option value='<%=status.getCode()%>'><coeusUtils:formatOutput
															name="status" property="description" /></option>
												</logic:iterate>
											</logic:present>
											<!-- <select> -->
									</td>
								</tr>
								<tr>
									<td>
										<table align="center" width="99%" class="table">
											<tr class="theaderBlue" width='100%'>
												<td height="20px" width="5px">&nbsp;</td>
												<td height="20px" width="10px">&nbsp;</td>
												<td align="left" class="theader" width="300px">Entity
													Name</td>
												<td align="left" class="theader" width="150px">Conflict
													Status</td>
												<td height="20px" width="15px">&nbsp;</td>
											</tr>

											<logic:iterate id="finEntDet" name="PIFinEnt"
												type="org.apache.commons.beanutils.DynaBean">
												<bean:define id="entityNumber" name="finEntDet"
													property="entityNumber" />
												<bean:define id="entityStatus" name="finEntDet"
													property="coiStatus" />
												<bean:define id="sequenceNum" name="finEntDet"
													property="entitySequenceNumber" />

												<INPUT type='hidden' name='slctConflictStat'
													value='<bean:write name="finEntDet" property="entityNumber"/>'>
												<INPUT type='hidden' name='hdnEntityNum'
													value='<bean:write name="finEntDet" property="entityNumber"/>'>
												<INPUT type='hidden' name='hdnEntSeqNum'
													value='<bean:write name="finEntDet" property="entitySequenceNumber"/>'>
												<INPUT type='hidden' name='hdnSeqNum'
													value='<bean:write name="finEntDet" property="sequenceNum"/>'>
												<INPUT type='hidden' name='hdnDisclNum'
													value='<bean:write name="finEntDet" property="disclNo"/>'>

												<%                                  
                            if (disclIndex%2 == 0) {
                            strBgColor = "#D6DCE5";
                            } else {
                            strBgColor="#DCE5F1";
                            }
                            String personId = request.getParameter("personId");
                            personId = personId == null ? "" : personId;
                            %>
												<tr bgcolor='<%=strBgColor%>' class="rowLine"
													onmouseover="className='rowHover rowLine'"
													onmouseout="className='rowLine'" width='100px'>
													<%-- Added for Case#4447 : FS_Next phase of COI enhancements - Start
                                  <td  height='20px'>&nbsp;</td> --%>
													<td><a
														href="javascript:selectRow('row<%=disclIndex+10%>','projectRow<%=disclIndex+10%>','projectData<%=disclIndex+10%>','toggle<%=disclIndex+10%>','projectFrame<%=disclIndex+10%>','<%=path%>/getFinEntPrj.do?entityNumber=<bean:write name="finEntDet" property="entityNumber"/>&entitySequenceNumber=<bean:write name="finEntDet" property="entitySequenceNumber"/>&personId=<%=personId%>&updSonflictStatus=<%="UpdateConflictStatus"%>')"
														id="toggle<%=disclIndex+10%>"> <img
															src='<%=request.getContextPath()%>/coeusliteimages/plus.gif'
															border='none' id="imgtoggle<%=disclIndex+10%>"
															name="imgtoggle<%=disclIndex+10%>" border="none">
													</a></td>
													<%--Case 4447 - End--%>
													<td width='15px'><a
														href="<%=path%>/reviewAnnFinEntityHist.do?entityNumber=<%=entityNumber%>&seqNum=<%=sequenceNum%>&header=no&review=review"
														target="_blank">View&nbsp;&nbsp;</a></td>
													<td class="copy"><coeusUtils:formatOutput
															name="finEntDet" property="entityName" /></td>
													<td class="copy"><select
														name="slctConflictStatus<%=entityNumber%>"
														styleClass="textbox-long">
															<logic:present name="coiStatus" scope="session">
																<logic:iterate id="status" name="coiStatus"
																	type="edu.mit.coeuslite.utils.ComboBoxBean">
																	<%selected="";%>
																	<logic:equal name="status" property="description"
																		value="<%=entityStatus.toString()%>">
																		<% selected="selected";%>
																	</logic:equal>
																	<option value='<%=status.getCode()%>' <%=selected%>><coeusUtils:formatOutput
																			name="status" property="description" /></option>
																</logic:iterate>
															</logic:present>
													</select> <%--   <html:select  name="finEntDet" property="coiStatus" >
                            <html:options collection="coiStatus" property="code" labelProperty="description"/>
                            </html:select>                        --%></td>
													<td>&nbsp;</td>
												</tr>
												<%-- Added for Case#4447 : FS_Next phase of COI enhancements - Start ---%>
												<tr id="projectRow<%=disclIndex+10%>" style="display: none"
													class="copy rowHover">
													<td colspan="5">
														<div id="projectData<%=disclIndex+10%>"
															style="overflow: hidden;"></div>
													</td>
												</tr>
												<%-- Case 4447 - End--%>
												<% disclIndex++;%>
											</logic:iterate>
											</tr>
											</td>
										</table>
							</logic:notEqual>

							<logic:equal name="totentity" value='0'>
								<tr>
									<td colspan="3"><bean:message bundle="coi"
											key="Annualdisclosure.Review.noFE" /></td>
								</tr>
							</logic:equal>
						</logic:present>
						<logic:notPresent name="PIFinEnt" scope="session">
							<tr>
								<td colspan="3"><bean:message bundle="coi"
										key="Annualdisclosure.Review.noFE" /></td>
							</tr>
						</logic:notPresent>
					</table>
		</table>
		<table align="center" width='100%' border="0" cellpadding="3"
			cellspacing="0" class="table">

			<tr>
				<td><html:submit value="Submit" styleClass="clsavebutton"
						onclick="javascript:submitPage();" /></td>
			</tr>
			<tr class="theaderBlue">
				<td align="right" colspan="5"><a href="javascript:self.close()"><u>Close</u></a></td>
			</tr>
		</table>
	</form>
</body>
</html>