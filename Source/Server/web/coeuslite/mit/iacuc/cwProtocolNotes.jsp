<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ include file="/coeuslite/mit/utils/CoeusContextPath.jsp"%>
<%@ page
	import="edu.mit.coeuslite.utils.CoeusLiteConstants,edu.mit.coeuslite.iacuc.bean.ProtocolHeaderDetailsBean"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/coeus-utils.tld" prefix="coeusUtils"%>

<jsp:useBean id="protocolNotesData" scope="session"
	class="java.util.Vector" />

<%
    /*Commented for issue#55 fixes-Start*/
    //String mode=(String)session.getAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId());
    //boolean modeValue=false;
    //if(mode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE)){
    //    modeValue=true;
    //}
    /*Commented for issue#55 fixes-End*/
    // Modified and Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_start
    ProtocolHeaderDetailsBean headerBean=(ProtocolHeaderDetailsBean)session.getAttribute("iacucHeaderBean");
    String statusCode = new Integer(headerBean.getProtocolStatusCode()).toString();
    String show = (String)request.getAttribute("show");
    boolean modeValue=false;
    if("309".equals(statusCode)){
        show = null;   
        modeValue = true;
    }
    // Modified and added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_end
%>

<html:html locale="true">
<head>
<title>Protocol Notes</title>
<script language="JavaScript">            
            var index = "";
            var isError = "";
            var errValue = false;
            var errLock = false;
            //Function for opening comment for viewing
                function openDesc(value,desc) {
                //index = value;
                //var txtValue = desc;
                openWindow(value);        
            }    
            function openWindow(txtValue){
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
        
            var loc = '<bean:write name='ctxtPath'/>/coeuslite/mit/utils/dialogs/cwViewSpecialReview.jsp?value='+txtValue +'&type=N';
            var newWin = window.open(loc,"formName",'dependent=1,toolbar=0,location=0,directories=0,status=0, menubar=0,scrollbars=0,resizable=0,width=' + w + ',height=' + h+ ',left='+ leftPos + ',top=' + topPos);
            newWin.window.focus();
         
            }        
            function insertData(value) {
            var currentValue = document.getElementsByName('protocolNotes['+index+'].comments');
            currentValue[0].value = value;
            dataChanged();
            }
    
            //Function for hiding and showing the Add Notes section
            function called() {
         
            document.getElementById('open_window').style.display = 'block';
            document.getElementById('open_Add').style.display = 'none';         
            
            }
    
            //Function for saving protocol notes
            var restricted = 'N';
            function saveNotes(){
                //Commented for Coeus4.3 Protocol Notes enhancement - start
                /*
                if(document.iacucNotesForm.addRestrictedView.checked == true){
                    restricted = 'Y'
                }
                */
                //Commented for Coeus4.3 Protocol Notes enhancement - end
                document.iacucNotesForm.action = "<%=request.getContextPath()%>/saveIacucNotes.do?&restricted="+restricted;
                document.iacucNotesForm.submit();
                //Hide the code in first div tag
                document.getElementById('notesProtocol').style.display = 'none';
                //Display code in second div tag
                document.getElementById('saveDiv').style.display = 'block';
            }   
           function cancelNotes(){        
            document.iacucNotesForm.action = "<%=request.getContextPath()%>/getIacucNotes.do?page=N";
            document.iacucNotesForm.submit();
          }
    
        </script>


<html:base />
</head>
<body>

	<html:form action="/getIacucNotes.do">
		<script type="text/javascript">
                document.getElementById('txt').innerHTML = '<bean:message bundle="iacuc" key="helpPageTextProtocol.Notes"/>';
            </script>
		<div id='notesProtocol'>
			<table width="100%" height="100%" border="0" cellpadding="0"
				cellspacing="0" class="table" align='center'>

				<tr>
					<td>
						<table width="100%" border="0" cellpadding="0" cellspacing="0"
							class="tableheader">
							<tr>
								<td height="20" width="50%" align="left" valign="top"><bean:message
										bundle="iacuc" key="protocolNote.label.header" /></td>
								<td align="right" width="50%"><a id="helpPageText"
									href="javascript:showBalloon('helpPageText', 'helpText')">
										<bean:message bundle="iacuc" key="helpPageTextProtocol.help" />
								</a></td>
							</tr>
						</table>
					</td>
				</tr>



				<tr class="copy">
					<td align="left" width='99%'><font color="red"> <logic:messagesPresent>
							<script>errValue = true;</script>
							<html:errors header="" footer="" />
						</logic:messagesPresent> <logic:messagesPresent message="true">
							<script>errValue = true;</script>
							<html:messages id="message" message="true">
								<!-- If lock is deleted then show this message -->
								<html:messages id="message" message="true" property="errMsg">
									<script>errLock = true;</script>
									<li><bean:write name="message" /></li>
								</html:messages>
								<!-- If lock is acquired by another user, then show this message -->
								<html:messages id="message" message="true" property="acqLock">
									<script>errLock = true;</script>
									<li><bean:write name="message" /></li>
								</html:messages>
							</html:messages>
						</logic:messagesPresent> </font></td>
				</tr>

				<%if(!modeValue){%>
				<tr class='copybold' align='left' width='100%'>
					<td>
						<div id='open_Add'>

							<html:link href="javaScript:called();">
								<u>
								<bean:message bundle="iacuc" key="protocolNote.label.add" /></u>
							</html:link>

						</div>

					</td>
				</tr>

				<%}%>
				<!-- Add Protocol Notes - Start  -->
				<tr>
					<td align="left" valign="top" class='core'>
						<div id='open_window' style='display: none;'>
							<table width="100%" border="0" align="center" cellpadding="0"
								cellspacing="0" class="tabtable">
								<%-- Commented for case id#2627
                    <tr class="theader">
                        <td class="copybold">
                            &nbsp;&nbsp;
                            <font color="red">*</font> 
                            <bean:message key="protocolNote.label.required"/>
                            <!--Indicates Required Fields-->
                        </td>
                    </tr>  
                    --%>
								<tr>
									<td>
										<div id="helpText" class='helptext'>
											&nbsp;&nbsp;
											<bean:message key="helpTextProtocol.Notes" />
										</div>
									</td>
								</tr>



								<tr>
									<td>
										<table width="100%" border="0" cellpadding="0">
											<tr>

												<!-- Commented for Coeus4.3 Protocol Notes enhancement - start -->
												<%--
                    <td width="5%" align="left" valign="top" class="copybold" nowrap>
                        <bean:message key="protocolNote.label.restricted"/>:
                    </td>
                    <td width="20%" align="left" valign="top" class="copy" nowrap>
                        <html:checkbox name="iacucNotesForm" property="addRestrictedView" value="N" onclick="dataChanged()"/>
                    </td>
                    --%>
												<!-- Commented for Coeus4.3 Protocol Notes enhancement - end -->
											</tr>
											<tr>
												<td width="5%" align="left" class="copybold">
													<%-- Commented for case id#2627
                                        <font color="red">*</font>
                                        --%> <bean:message
														key="protocolNote.label.comment" />:
												</td>
												<td width="85%">
													<%--div class='copyitalics'><bean:message key="protocolNote.error.maxlength"/></div--%>
													<html:textarea name="iacucNotesForm" styleClass="copy"
														property="addComments" cols="120" rows="5"
														onchange="dataChanged()" />
												</td>
											</tr>

										</table>
									</td>
								</tr>
								<tr>
									<td height='10'>&nbsp;</td>
								</tr>

								<tr class="table">
									<td class='savebutton'><html:button property="Save"
											styleClass="clsavebutton" onclick="saveNotes()">
											<bean:message key="protocolNote.button.save" />
										</html:button> <html:button property="save" styleClass="clsavebutton"
											onclick="cancelNotes()">
											<bean:message bundle="iacuc" key="button.label.cancel" />
										</html:button></td>
								</tr>
							</table>
						</div>
					</td>
				</tr>
				<!-- Add Protocol Notes - Ends  -->



				<!-- List of Protocol Notes Start -->
				<tr>
					<td align="left" valign="top" class='core'>
						<table width="100%" border="0" align="center" cellpadding="0"
							cellspacing="0" class="tabtable">
							<tr>
								<td class="theader" align='left'><bean:message
										key="protocolNote.label.comment" /></td>
								<td class="theader" align='left'><bean:message
										key="protocolNote.label.by" /></td>
								<td class="theader" align='left'><bean:message
										key="protocolNote.label.time" /></td>
								<%--
                    <td width="10%" class="theader" align='left'>
                        <bean:message key="protocolNote.label.restricted"/>
                    </td>
                    --%>
								<td class="theader" align='left'><bean:message
										key="protocolNote.label.view" /></td>
							</tr>
							<%
            String strBgColor = "#D6DCE5";
            int index=0;
        %>
							<logic:present name="protocolNotesData">
								<logic:iterate id="protocolNotes" name="protocolNotesData"
									type="org.apache.commons.beanutils.DynaBean">
									<%
            
            if (index%2 == 0) {
                strBgColor = "#D6DCE5"; 
            }
            else { 
                strBgColor="#DCE5F1"; 
            }
            String comment = (String)protocolNotes.get("comments");
            String EMPTYSTRING ="";
            if(comment!=null && (!comment.equals(EMPTYSTRING))){
                comment=comment.length()>40?comment.substring(0,41)+"...":comment;
            }
            // Added for displaying user name for user Id
            String updateUserName  = (String)protocolNotes.get("updateUserName");
            updateUserName = updateUserName.length()>60?updateUserName.substring(0,60)+"...":updateUserName;
            //End
        %>
									<tr bgcolor="<%=strBgColor%>">
										<td class="copy" align='left'><%=comment%></td>
										<td class="copy" align='left'>
											<%--bean:write name="protocolNotes" property="updateUser"/--%>
											<%=updateUserName%>
										</td>
										<td class="copy" align='left'><bean:write
												name="protocolNotes" property="updateTimestamp" /></td>
										<%--
                            <td width="5%" class="copy" align='left'>
                                <html:checkbox name="protocolNotes" property="restrictedView" value="Y" disabled="true"/>
                            </td>    
                            --%>
										<td class="copy" align='left'>
											<%String link = "javaScript:openDesc('" +index +"')";%> <html:link
												href="<%=link%>">
												<bean:message key="protocolNote.label.view" />
											</html:link>
										</td>
									</tr>
									<tr>
										<td height=2></td>
									</tr>
									<% 
            index++;
        %>
								</logic:iterate>
							</logic:present>
						</table>
					</td>
				</tr>
				<!-- List of Protocol Notes End -->

			</table>
		</div>
		<div id='saveDiv' style='display: none;'>
			<table width="100%" height="100%" align="center" border="0"
				cellpadding="3" cellspacing="0" class="tabtable">
				<tr>
					<td align='center' class='copyred'><br> <bean:message
								bundle="budget" key="budgetMessages.pleaseWait" /></td>
				</tr>
			</table>
		</div>
		<script>
        <%
        // Modified for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_start
        if(show!=null){ %>
                document.getElementById('open_window').style.display = 'none';
                //document.getElementById('hide_Add').style.display = 'none';
                if(document.getElementById('open_Add')){
                document.getElementById('open_Add').style.display = 'block';
                }
        <%}else if(show == null && "309".equals(statusCode)) {%>
                    document.getElementById('open_window').style.display = 'none';
                    document.getElementById('hide_Add').style.display = 'none';
                    document.getElementById('open_Add').style.display = 'none';
        <%}else {%>       
                document.getElementById('open_window').style.display = 'block';
                //document.getElementById('hide_Add').style.display = 'block';
                document.getElementById('open_Add').style.display = 'none';
        <%}
            // Modified for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_end
            %>
            </script>

	</html:form>
	<script>
          DATA_CHANGED = 'false';
          if(errValue && !errLock) {
                DATA_CHANGED = 'true';
          }
          LINK = "<%=request.getContextPath()%>/saveIacucNotes.do?restricted="+restricted;
          FORM_LINK = document.iacucNotesForm;
          PAGE_NAME = " <bean:message key="protocolNote.label.header"/>";
          function dataChanged(){
            DATA_CHANGED = 'true';
            LINK = "<%=request.getContextPath()%>/saveIacucNotes.do?restricted="+restricted;
          }
          linkForward(errValue);
         </script>
	<script>
      var help = ' <bean:message key="helpTextProtocol.Notes"/>';
      help = trim(help);
      if(help.length == 0) {
        document.getElementById('helpText').style.display = 'none';
      }
      function trim(s) {
            return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
      }  
  </script>
</body>
</html:html>
