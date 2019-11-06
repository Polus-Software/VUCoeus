<? xml version="1.0" ?>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page
	import="org.apache.struts.taglib.html.JavascriptValidatorTag,
edu.mit.coeuslite.utils.CoeusLiteConstants,java.util.Vector,edu.mit.coeus.utils.ComboBoxBean"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>
<jsp:useBean id="vecAcAlternativeSearchesData" scope="session"
	class="java.util.Vector" />
<jsp:useBean id="vecAddedAlternativeSearches" scope="session"
	class="java.util.Vector" />
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>

<html:html>
<head>
<link
	href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/coeus_styles.css"
	rel="stylesheet" type="text/css">
<title>Coeus Web</title>
<script language="javaScript" type="text/JavaScript">
            var errValue = false;
            var errLock = false;
            
            function showHide(val){
                    if(val == 1){
                        document.getElementById('panel1').style.display = "block";
                    }else if(val == 2){
                        document.getElementById('panel2').style.display = "block";
                    }else if(val == 3){
                         if(document.getElementById('showSpPanel'))
                        {
                         document.iacucProtoAlternativeSearchForm.selectedAltSearchData.value = "";
                         document.getElementById('showSpPanel').style.display = "none";
                         document.getElementById('hideSpPanel').style.display = "block";
                         document.getElementById('speciesPanel').style.display = "block";
                         document.getElementById('altSearchPanel').style.display = "block";
                        }
                    }else if(val == 4){
                        if(document.getElementById('showSpPanel'))
                        {
                         document.getElementById('showSpPanel').style.display = "block";
                          document.getElementById('hideSpPanel').style.display = "none";
                          document.getElementById('speciesPanel').style.display = "none";
                          document.getElementById('altSearchPanel').style.display = "none";
                            }
                         }                    
            }   
            
            function saveAltSearchData(){
                document.iacucProtoAlternativeSearchForm.Save.disabled=true;
                //document.iacucProtoAlternativeSearchForm.alternativeDBSearchId.disabled=false;  
                if( document.iacucProtoAlternativeSearchForm.acType.value != 'U'){
                    document.iacucProtoAlternativeSearchForm.acType.value = 'I' ;
                }
                document.iacucProtoAlternativeSearchForm.action = "<%=request.getContextPath()%>/saveIacucProtoAlternativeSearch.do";
                document.iacucProtoAlternativeSearchForm.submit();
            }
            
            function modifyAlternativeSearch(alternativeSearchId,sequenceNumber){
                
                document.iacucProtoAlternativeSearchForm.action = "<%=request.getContextPath()%>/loadIacucProtoAlternativeSearch.do?alternativeSearchId="+alternativeSearchId+"&sequenceNumber="+sequenceNumber;
                document.iacucProtoAlternativeSearchForm.submit();
            }
            
            function removeAlternativeSearch(alternativeSearchId,sequenceNumber, updateTimestamp){
             if (confirm("Are you sure you want to remove this Alternative?")==true){
                document.iacucProtoAlternativeSearchForm.action = "<%=request.getContextPath()%>/removeIacucProtoAlternativeSearch.do?alternativeSearchId="+alternativeSearchId+"&sequenceNumber="+sequenceNumber+"&updateTimestamp="+updateTimestamp;
                document.iacucProtoAlternativeSearchForm.submit();
                }
            }
            function clearFormData(){
                document.iacucProtoAlternativeSearchForm.acType.value = '' ;
                //document.iacucProtoAlternativeSearchForm.alternativeDBSearchId.value = '' ;
                document.iacucProtoAlternativeSearchForm.searchDate.value = '' ;
                
                document.iacucProtoAlternativeSearchForm.yearsSearched.value = '' ;
                document.iacucProtoAlternativeSearchForm.keyWordsSearched.value = '' ;
                document.iacucProtoAlternativeSearchForm.comments.value = '' ;
                //document.iacucProtoAlternativeSearchForm.alternativeDBSearchId.value = '' ;
                
                showHide(4);
            }
            
            function cancelAlternativeSearch(){
                document.iacucProtoAlternativeSearchForm.acType.value = '' ;
                //document.iacucProtoAlternativeSearchForm.alternativeDBSearchId.value = '' ;
                document.iacucProtoAlternativeSearchForm.searchDate.value = '' ;
                document.iacucProtoAlternativeSearchForm.yearsSearched.value = '' ;
                document.iacucProtoAlternativeSearchForm.keyWordsSearched.value = '' ;
                document.iacucProtoAlternativeSearchForm.comments.value = '' ;
                //document.iacucProtoAlternativeSearchForm.alternativeDBSearchId.value = '' ;
                document.iacucProtoAlternativeSearchForm.action = "<%=request.getContextPath()%>/getIacucProtoAlternativeSearch.do";
                document.iacucProtoAlternativeSearchForm.submit();
            }

            <!--Added for 1485-user data entry is not restricted -(Wrapping) start-->
            function viewComments(count,selectedItem){
            <!--Added for 1485-user data entry is not restricted -(Wrapping) end-->
          
                var w = 550;
                var h = 213;
                if(navigator.appName == "Microsoft Internet Explorer") {
        
                    w = 522;
                    h = 196;
                }
                if (window.screen) {
        
                    leftPos = Math.floor(((window.screen.width - 500) / 2));
                    topPos = Math.floor(((window.screen.height - 350) / 2));
                }
                var loc = '<bean:write name='ctxtPath'/>/coeuslite/mit/utils/dialogs/cwViewIacucProtoAlterSearchComment.jsp?recordId='+count+'&alternativeSearchItem='+selectedItem;
                var newWin = window.open(loc,"formName","dependent=1,toolbar=0,location=0,directories=0,status=0, menubar=0,scrollbars=0,resizable=0,width=" + w + ",height=" + (h+7)+ ",left="+ leftPos + ",top=" + topPos);            
                newWin.window.focus();                 
            }
              

    </script>
<style>
.textbox-longer {
	font-weight: normal;
	width: 500px
}
</style>
</head>
<body>
	<% 
boolean hasValidationMessage = false;
String mode=(String)session.getAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId());
boolean modeValue=false;
String EMPTY_STRING = "";
if(mode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE)){
modeValue=true;
}else if(mode.equalsIgnoreCase(CoeusLiteConstants.MODIFY_MODE)){
modeValue=false;
}else if(mode.equalsIgnoreCase(CoeusLiteConstants.ADD_MODE)){
modeValue=false;
}
%>

	<%String protocolNo = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());

if(protocolNo!= null){ 
protocolNo = "["+"Protocol No. - "+ protocolNo+"]";
}else{
protocolNo = "";
}

String strProtocolNum = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
if(strProtocolNum == null)
strProtocolNum = "";    
String amendRenewPageMode = (String)session.getAttribute("amendRenewPageMode"+request.getSession().getId());
if(strProtocolNum.length() > 10 && (amendRenewPageMode == null || 
amendRenewPageMode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE))){
modeValue = true;

}%>
	<%try{%>

	<html:form action="/saveIacucProtoAlternativeSearch.do" method="post">
		<a name="top"></a>

		<script type="text/javascript">
    document.getElementById('txt').innerHTML = '<bean:message bundle="iacuc" key="helpPageTextProtocol.AlternativeSearch"/>';
    </script>
		<script language="javascript"
			src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/toolkit.js"
			type="text/javascript"></script>
		<script language="javascript"
			src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/common.js"
			type="text/javascript"></script>
		<script language="javascript"
			src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/lightbox.js"
			type="text/javascript"></script>

		<%--<link href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/Structure.css" rel="stylesheet" type="text/css" />--%>
		<link
			href="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/css/lightbox.css"
			rel="stylesheet" type="text/css" />
		<script language="javascript" type="text/JavaScript"
			src="<bean:write name='ctxtPath'/>/coeuslite/mit/utils/scripts/coeusLiteCalendar.js"></script>

		<table width="100%" height="100%" border="0" cellpadding="0"
			cellspacing="0" class="table">

			<tr>
				<td>
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<!-- JM 5-31-2011 updated class per 4.4.2 -->
							<td height="20" align="left" valign="center" class="tableheader">
								&nbsp; Alternative Searches</td>
							<td height="20" align="right" valign="top" class="theader">
								<a id="helpPageText"
								href="javascript:showBalloon('helpPageText', 'helpText')"> <u><bean:message
											bundle="iacuc" key="helpPageTextProtocol.help" /></u>
							</a>
							</td>&nbsp;&nbsp;
						</tr>
					</table>
				</td>
			</tr>


			<tr class='table' align="left">
				<td align="left"><logic:messagesPresent>
						<script>errValue = true; </script>
						<html:errors header="" footer="" />
					</logic:messagesPresent></td>
			</tr>
			<logic:messagesPresent message="true">
				<tr class='copy' align="left">
					<td><font color="red"> <script>errValue = true;</script>
							<%--<html:messages id="message" message="true">--%> <html:messages
								id="message" message="true" property="invalidSearchDate"
								bundle="iacuc">
								<script>errLock = true;</script>
								<li><bean:write name="message" /></li>
							</html:messages> <html:messages id="message" message="true"
								property="invalidSearchedDate" bundle="iacuc">
								<script>errLock = true;</script>
								<li><bean:write name="message" /></li>
							</html:messages> <html:messages id="message1" message="true"
								property="invalidAlterDBId" bundle="iacuc">
								<script>errLock = true;</script>
								<li><bean:write name="message1" /></li>
							</html:messages> <html:messages id="message2" message="true"
								property="invalidYearsSearched" bundle="iacuc">
								<script>errLock = true;</script>
								<li><bean:write name="message2" /></li>
							</html:messages> <html:messages id="message3" message="true"
								property="invalidKeyWordsSearched" bundle="iacuc">
								<script>errLock = true;</script>
								<li><bean:write name="message3" /></li>
								<%-- </html:messages>--%>

							</html:messages>

					</font></td>
				</tr>
			</logic:messagesPresent>
			<%--  <tr>
            <td  align="left" valign="top" class='core'>
                <table width="100%"  border="0" align="center" cellpadding="0" cellspacing="2" class="tabtable">
                --%>
			<tr>
				<td colspan="4" align="left" valign="top">
					<%if(!modeValue){%>
					<div id='showSpPanel' class='table'>
						&nbsp;
						<%String divlink = "javascript:showHide(3)";%>
						<html:link href="<%=divlink%>">
							<%String imagePlus=request.getContextPath()+"/coeusliteimages/plus.gif";%>
							<u>Add Alternative Search</u>
						</html:link>
						&nbsp;
					</div> <%}%>
				</td>
			</tr>
			<tr>
				<td width="100%">
					<div id='speciesPanel' style='display: none;'>
						<table width="100%" border="0" cellpadding="2" cellspacing="0"
							class="tabtable">

							<tr>
								<td width="15%" nowrap class="copybold" align="right"><font
									color="red">*</font>Search Date:</td>

								<td nowrap class="copy"><html:text property="searchDate"
										styleClass="textbox" disabled="<%=modeValue%>" maxlength="10"
										onchange="dataChanged()" /> <%if(!modeValue){%> <html:link
										href="javascript:void(0)"
										onclick="displayCalendarWithTopLeft('searchDate',8,25)"
										tabindex="32">
										<img id="imgIRBDate" title="" height="16" alt=""
											src="<bean:write name='ctxtPath'/>/coeusliteimages/cal.gif"
											width="16" onclick='dataChanged()' border="0" runat="server">
									</html:link> <%}%></td>


								<%--<td width="15%" nowrap class="copybold" align = "right">
                                                <font color="red">*</font>Database Searched:                                  
                                            </td>
                                            
                                            <td nowrap class="copy">
                                                <html:select name="iacucProtoAlternativeSearchForm" style="width=232px;" property="alternativeDBSearchId" styleClass="textbox-long" disabled="<%=modeValue%>" onchange="dataChanged()">
                                                    <html:option value=""> <bean:message bundle="proposal" key="generalInfoProposal.pleaseSelect"/> </html:option>
                                                    <html:options  collection="vecAcAlternativeSearchesData" property="code" labelProperty="description"/>
                                                </html:select>
                                            </td>--%>

							</tr>
							<tr>
								<td width="15%" nowrap class="copybold" align="right"
									valign="top"><font color="red">*</font>Database Searched:
								</td>
								<td nowrap width="85%" class="copy">
									<table width="100%" border="0" cellpadding="0" cellspacing="0">
										<%--Added for COEUSQA-2714 In the Alternative Search in IACUC-Start--%>
										<% int newCount = 0;%>
										<logic:iterate name="iacucProtoAlternativeSearchForm"
											property="vecAcAlternativeSearchesData"
											id="alternativeDBSearchId" indexId="index">
											<%if(newCount%2 == 0){%>
											<tr>
												<%}%>
												<td class="copy" width="2%" height="20" align="left"
													valign="top"><html:multibox
														property="selectedAltSearchData" style="copy"
														disabled="false">
														<bean:write name="alternativeDBSearchId" property="code" />
													</html:multibox></td>
												<td class="copy" width="48%" height="20" align="left"
													valign="top"><bean:write name="alternativeDBSearchId"
														property="description" /></td>
												<% if(newCount%2 == 1){%>
											</tr>
											<%}%>
											<% newCount++;%>
										</logic:iterate>
										<%--Added for COEUSQA-2714 In the Alternative Search in IACUC-End--%>
									</table>
								</td>
							</tr>


							<tr>


								<td nowrap class="copybold" align="right" valign="top"><font
									color="red">*</font>Years Searched:</td>

								<td class="copy" colspan="3"><html:textarea
										property="yearsSearched" styleClass="copy" cols="106" rows="2"
										disabled="<%=modeValue%>" onchange="dataChanged()" /></td>
							</tr>
							<tr>
								<td nowrap class="copybold" align="right" valign="top"><font
									color="red">*</font>Keywords Searched:</td>

								<td class="copy" colspan="3"><html:textarea
										property="keyWordsSearched" styleClass="copy" cols="106"
										rows="2" disabled="<%=modeValue%>" onchange="dataChanged()" />
								</td>
							</tr>

							<tr>
								<td width="15%" nowrap class="copybold" align="right"
									valign="top">Comments:</td>

								<td nowrap class="copy" colspan="3"><html:textarea
										property="comments" styleClass="copy" cols="106" rows="3"
										disabled="<%=modeValue%>" onchange="dataChanged()" /></td>
							</tr>

						</table>
					</div>
				</td>
			</tr>

			<%--  </table>
            </td>
        </tr>
        --%>
			<tr>
				<td class='copy' nowrap colspan="4">
					<div id='altSearchPanel' style='display: none;'>
						<table width="100%" border="0" cellpadding="1" cellspacing="0">
							<tr class='table'>
								<%if(!modeValue){%>
								<td width="15%" nowrap class="copybold" align="left"
									style="height: 22px;"><html:button
										style="width:100%;height:18px;" property="Save" value="Save"
										styleClass="clsavebutton" onclick="saveAltSearchData();" /></td>



								<td nowrap class="copybold" align="left">

									<div id='hideSpPanel' style='display: none;'>
										<html:button style="width:16%;height:18px;" property="Cancel"
											value="Cancel" styleClass="clsavebutton"
											onclick="cancelAlternativeSearch();" />
									</div>

								</td>

								<%}%>

							</tr>
						</table>
					</div>
				</td>
			</tr>



			<tr>
				<td align="left" valign="top" class='core'><table width="100%"
						border="0" align="center" cellpadding="0" cellspacing="0"
						class="tabtable">

						<tr align="center">
							<td colspan="4">

								<table width="100%" border="0" cellpadding="0" cellspacing="0"
									class="tabtable">
									<tr>
										<td width="15%" align="left" class="theader">Search Date</td>
										<td width="22%" align="left" class="theader">Database
											Searched</td>
										<td width="17%" align="left" class="theader">Years</td>
										<td width="18%" align="left" class="theader">Keywords</td>
										<td width="20%" align="left" class="theader">Comments</td>
										<td width="3%" align="center" class="theader"></td>
										<td width="2%" align="center" class="theader"></td>
										<td width="3%" align="center" class="theader"></td>
										<!--Added for 1485-user data entry is not restricted -(Wrapping) start-->
										<!-- <td width="5%" align="center" class="theader"></td>-->
										<!--Added for 1485-user data entry is not restricted -(Wrapping) end-->
									</tr>

									<%  String strBgColor = "#DCE5F1";
                            int count = 0;
                            String viewLink;
                            // <!--Added for 1485-user data entry is not restricted -(Wrapping) start-->
                            String selectedItem;
                            // <!--Added for 1485-user data entry is not restricted -(Wrapping) end-->
                            %>
									<%--   <logic:notEmpty name="iacucProtoAlternativeSearchForm" property="vecAddedAlternativeSearches">
                                
                                <logic:iterate id="alternativeSearchData"  name="iacucProtoAlternativeSearchForm" property="vecAddedAlternativeSearches" type="org.apache.struts.validator.DynaValidatorForm" indexId="index" >  
                          --%>
									<logic:present name="vecAddedAlternativeSearches">
										<logic:iterate id="alternativeSearchData"
											name="vecAddedAlternativeSearches"
											type="org.apache.struts.validator.DynaValidatorForm"
											indexId="index">
											<% 
                                    if (count%2 == 0)
                                    strBgColor = "#D6DCE5";
                                    else
                                    strBgColor="#DCE5F1";
                                    %>

											<tr bgcolor="<%=strBgColor%>" valign="top"
												onmouseover="className='TableItemOn'"
												onmouseout="className='TableItemOff'">

												<% 
                                        edu.mit.coeuslite.utils.DateUtils dateUtils = new edu.mit.coeuslite.utils.DateUtils();
                                        String date  = (String)alternativeSearchData.get("searchDate");
                                        date = (date==null)?"":date.trim();
                                        if(!"".equals(date)) {
                                        
                                        try{
                                        date = dateUtils.formatDate(date,"MM/dd/yyyy");
                                        } catch (Exception e){
                                        
                                        }
                                        } 
                                        alternativeSearchData.set("searchDate", date);
                                        %>

												<td nowrap width="15%" align="left" class="copy"><bean:write
														name="alternativeSearchData" property="searchDate" /></td>
												<td width="15%" align="left" class="copy"><bean:write
														name="alternativeSearchData"
														property="alternativeDBSearchDesc" /></td>
												<%--Added for 1485 - 2260-user data entry is not restricted (Wrapping) - Start--%>
												<%--<td nowrap width="20%" align="left" class="copy"><bean:write name="alternativeSearchData" property="yearsSearched"/>&nbsp;&nbsp;</td>--%>
												<%--<td nowrap width="20%" align="left" class="copy"><bean:write name="alternativeSearchData" property="keyWordsSearched"/>&nbsp;&nbsp;</td>--%>

												<%-- Wrap Years Searched --%>
												<%
                                        selectedItem = "yearsSearched";
                                        String yearsSearched  = (String)alternativeSearchData.get("yearsSearched");
                                        yearsSearched = (yearsSearched==null)?"":yearsSearched.trim();
                                        viewLink = "javaScript:viewComments('"+count+"','"+selectedItem+"')";
                                        if(yearsSearched.length() > 27){
                                              yearsSearched = yearsSearched.substring(0,20);
                                        %>
												<td width="20%" align="left" class="copy"><%=yearsSearched%><html:link
														href="<%=viewLink%>">&nbsp;[...]</html:link></td>
												<%}else{%>
												<td width="20%" align="left" class="copy"><%=yearsSearched%></td>
												<%}%>
												<%-- Wrap Keywords searched --%>
												<%
                                        selectedItem = "keyWordsSearched";
                                        String keyWords  = (String)alternativeSearchData.get("keyWordsSearched");
                                        keyWords = (keyWords==null)?"":keyWords.trim();
                                        viewLink = "javaScript:viewComments('"+count+"','"+selectedItem+"')";
                                        if(keyWords.length() > 27){
                                              keyWords = keyWords.substring(0,20);
                                        %>
												<td width="20%" align="left" class="copy"><%=keyWords%><html:link
														href="<%=viewLink%>">&nbsp;[...]</html:link></td>
												<%}else{%>
												<td width="20%" align="left" class="copy"><%=keyWords%></td>
												<%}%>
												<%-- Wrap Comments --%>
												<% 
                                        selectedItem = "comments";
                                        String comments  = (String)alternativeSearchData.get("comments");
                                        comments = (comments==null)?"":comments.trim();
                                        viewLink = "javaScript:viewComments('"+count+"','"+selectedItem+"')";
                                        if(comments.length() > 27){              
                                            comments = comments.substring(0,20);                      
                                        %>
												<td width="20%" align="left" class="copy"><%=comments%><html:link
														href="<%=viewLink%>">&nbsp;[...]</html:link></td>
												<%}else{%>
												<td width="20%" align="left" class="copy"><%=comments%></td>
												<%}%>
												<%--Added for 1485 - 2260-user data entry is not restricted (Wrapping) - end--%>
												<%if(!modeValue){%>
												<td width="8%" align="left" class="copy"><a
													href="javaScript:modifyAlternativeSearch('<bean:write name="alternativeSearchData" property="alternativeSearchId"/>','<bean:write name="alternativeSearchData" property="sequenceNumber"/>');">Modify</a></td>
												<td width="2%">&nbsp;&nbsp;</td>
												<td width="8%" align="center" class="copy"><a
													href="javaScript:removeAlternativeSearch('<bean:write name="alternativeSearchData" property="alternativeSearchId"/>','<bean:write name="alternativeSearchData" property="sequenceNumber"/>','<bean:write name="alternativeSearchData" property="awUpdateTimeStamp"/>');">Remove</a></td>
												<%}else{%>
												<td width="5%" align="left" class="copy"></td>
												<td width="5%" align="center" class="copy"></td>
												<%}%>
											</tr>
											<% count++;%>

										</logic:iterate>
									</logic:present>





									<tr>
										<td colspan="5" align="left" valign="top" class='core'>
											<div id='panel1' style='display: none;'>
												<table width="100%" border="0" align="center"
													cellpadding="0" cellspacing="0" class="tabtable">

												</table>
											</div>
										</td>
									</tr>

									</td>
									</tr>

								</table>


							</td>
						</tr>


					</table></td>
			</tr>

			<html:hidden property="acType" />
			<html:hidden property="protocolNumber" />
			<html:hidden property="sequenceNumber" />
			<html:hidden property="updateTimeStamp" />

			<html:hidden property="updateUser" />
			<html:hidden property="awUpdateTimeStamp" />

			<html:hidden property="awUpdateUser" />
			<html:hidden property="alternativeSearchId" />
			<html:hidden property="awAlternativeSearchId" />

			<tr>
				<td height='10'>&nbsp;</td>
			</tr>
		</table>


	</html:form>
	<script>
          DATA_CHANGED = 'false';
          if(errValue && !errLock) {
                DATA_CHANGED = 'true';
          }

          LINK = "<%=request.getContextPath()%>/saveIacucProtoAlternativeSearch.do"; 
          FORM_LINK = document.iacucProtoAlternativeSearchForm;
          PAGE_NAME = "<bean:message key="alternativeSearch.altSearch" bundle="iacuc"/>";
          function dataChanged(){
                DATA_CHANGED = 'true';
          }
          linkForward(errValue);
</script>
	<script>
      var help = '<bean:message key="helpTextProtocol.Subjects"/>';
      help = trim(help);
      if(help.length == 0) {
            document.getElementById('helpText').style.display = 'none';
      }
      function trim(s) {
            return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
      }  
      if( document.iacucProtoAlternativeSearchForm.acType.value == 'U' || errValue == true){
        showHide(3);
      } else {
        clearFormData();
      }
      
      <%if(hasValidationMessage){%>
        showHide(3);
      <%}%>

</script>
</body>
<%}catch(Exception e){
       e.printStackTrace();
    }
       %>
</html:html>
